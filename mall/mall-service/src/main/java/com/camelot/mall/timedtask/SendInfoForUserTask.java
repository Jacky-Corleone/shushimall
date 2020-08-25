package com.camelot.mall.timedtask;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import com.camelot.mall.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.mall.Constants;
import com.camelot.mall.util.MessageTemplateFileUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;

/**
 * Description: 给用户发送消息的定时器任务，间隔时间在spring-quartz.xml中配置
 *  
 * @author 王东晓
 * 
 * create on 2015-05-21
 * 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 *
 *
 * Description :使用redisDB的消息队列，和定时任务实现邮件和短信的异步发送，数据库做备份
 * redis消息队列是由双向链表实现，在获取双向链表表头和表尾数据的效率非常高。
 * 
 * 在定时器配置文件spring-quartz.xml中设置定时器的执行周期为1秒钟执行一次getInfoFromRedis()方法
 * 
 * 1、在redis消息队列中获取key为message的队列对头的数据，如果表头数据为空，说明消息队列中没有需要发送消息的数据；如果不为空，则反之
 * 2、如果消息队列中存在数据，则获取到该对头数据，将其存放在redis中的List中（List中的数据代表正在发送的数据）；
 * 3、根据数据内容给相应的用户发送相应的信息，
 * 4、如果给用户发送信息失败（发送失败包括：1调用的接口返回发送失败的状态，2调用发送消息的dubbo是发送异常），则将数据重新放入redis的消息队列队尾
 * 并将其在list中删除，并修改消息发送的次数，修改数据库
 * 5、如果消息发送成功，则修改数据库，将消息设置为已发送
 * 
 * 6、在第一步时，如果在队列获取对头信息时为空，此时需要查询数据库，查看数据库中是否有需要发送消息的数据
 * 
 * 7、查询数据库前，先检查与上次查询数据库的时间间隔（每次查询数据库时会记录查询数据库的时间），时间间隔在配置文件message_template.properties中，默认为30分钟
 * 
 * 8、如果与上一次的时间间隔小于配置文件中的时间间隔，则不再查询数据库
 * 
 * 9、反之，查询数据库，调用线程同步的方法sendInfoToRedis();（设置为线程同步的目的：解决多个线程同时访问数据库，第一个线程获取数据后放入队列中，第二个线程也可能会获取数据放入队列）
 * 
 * 10、因为sendInfoToRedis()是线程同步的，所以有可能大量的线程在此阻塞。所以当线程开始执行同步方法时，需要再次判断和上次查询数据库的时间间隔
 * 
 * 11、如果时间间隔不符合要求，则直接退出，不查询数据库
 * 
 * 12、如果时间间隔符合要求，则继续查询数据库，并且设置本次查询数据库的时间
 * 
 * 13、查询数据库，查询条件：未发送、发送的次数在0-5之间、每次查询10000条数据
 * 
 * 14、在数据库中查询到需要有发送的数据，则将数据存放在redis队列中
 * 
 */
@Controller
public class SendInfoForUserTask {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private RedisDB redisDB;
	@Resource
	private MessageService messageService;
	@Resource
	private BaseSendMessageService baseSendMessageService;

	private Date lastQueryDBTime =new Date() ;
	
	
	
	/**
	 * 定时器方法，在redis中获取短信邮件信息发送给用户
	 * @throws Exception 
	 */
	public void getInfoFromRedis(){
		
		//在redis的邮件消息队列中获取队列头部邮件信息
		Object message = redisDB.headPop(Constants.MESSAGE_KEY);
		//消息队列中不存在需要发送的消息，直接返回
		if(message==null){
//			logger.info("队列中没有数据");
			if(!this.verifyTime()){
				return ;
			}
			//在消息队列空闲的时候，去查询数据库，看是否有需要发短信活邮件的数据
			if(!this.sendInfoToRedis()){
				return ;
			}
		}
		
		BaseSendMessageDTO baseSendMessageDTO =  (BaseSendMessageDTO) message;
		//将在消息队列中获取得到的数据，存到redis中的list中,list中的消息都是正在发送的消息，
		//当在数据库中取消息的时候要判断list中是否存在此条信息，如果存在，则不将此条信息存入消息队列
		redisDB.addObject(baseSendMessageDTO.getId()+"", message);
		//发送改消息
		logger.info("开始给用户("+baseSendMessageDTO.getAddress()+")发送信息");
		this.sendInfo(baseSendMessageDTO);
		
	}
	/**
	 * 根据获取到在队列中获取到的信息发送邮件
	 * 
	 * @param baseSendMessageDTO
	 * @throws Exception 
	 */
	private void sendInfo(BaseSendMessageDTO baseSendMessageDTO){
		
		try{
			ExecuteResult<String> result = null;
			//如果是短信消息，发送短信，否则发送邮件
			if(Constants.SMS_TYPE.equals(baseSendMessageDTO.getType())){
				logger.info("开始给用户("+baseSendMessageDTO.getAddress()+")发送短信,发送的内容为："+baseSendMessageDTO.getContent());
				result = messageService.sendSms(baseSendMessageDTO.getAddress(), baseSendMessageDTO.getContent());
				
			}else if(Constants.MAIL_TYPE.equals(baseSendMessageDTO.getType())){
				result = messageService.sendEmail(baseSendMessageDTO.getAddress(), baseSendMessageDTO.getTitle(), baseSendMessageDTO.getContent());
			}
			//邮件发送不成功,将其移到redis的消息队列中
			
			if(!result.isSuccess()){
				logger.info("给用户("+baseSendMessageDTO.getAddress()+")发送信息 失败:"+result.getErrorMessages());
				this.releftPushToRedisQuene(baseSendMessageDTO);
			}else{
				
				/**
				 * 修改为批量修改数据库
				 */
				
				//邮件发送成功后，修改数据库，设置为已发送
				baseSendMessageDTO.setIsSend("1");
				Integer num = baseSendMessageDTO.getSendNum()==null?1:baseSendMessageDTO.getSendNum()+1;
				baseSendMessageDTO.setSendNum(num);
				baseSendMessageService.editBaseSendMessage(baseSendMessageDTO);
				logger.info("给用户("+baseSendMessageDTO.getAddress()+")发送信息成功，并成功修改数据库，发送的内容为："+baseSendMessageDTO.getContent());
			}
		}catch(Exception e){
			//发送邮件失败，可能调用邮件发送服务超时，处理连接超时的问题
			/**
			 * 此处先将dubbo连接超时的问题作为发送不成功处理，后期再做性能调优
			 */
			logger.info("发生异常！！！！！！");
			e.printStackTrace();
			this.releftPushToRedisQuene(baseSendMessageDTO);
		}finally{
			//无论消息发送成功还是失败，都将此消息在redis中的list中删除
			redisDB.del(baseSendMessageDTO.getId()+"");
		}
		
	}
	
	
	/**
	 * 当邮件或者短信发送失败后，将此条信息重新放入redis消息队列中
	 * @throws Exception 
	 */
	public void releftPushToRedisQuene(BaseSendMessageDTO baseSendMessageDTO){
		//将获取到的字符串转换为对象
		//因为发送失败，所有需要设置DTO中的发送次数+1
		Integer num = baseSendMessageDTO.getSendNum()==null?1:baseSendMessageDTO.getSendNum()+1;
		baseSendMessageDTO.setSendNum(num);
		
		//如果发送的次数没有超过5次，则将消息重新放入redis消息队列中,否则直接在redisList中直接删掉这条信息
		if(num<5){
			
			redisDB.tailPush(Constants.MESSAGE_KEY, baseSendMessageDTO);
			
			//修改数据库，将此条邮件消息的发送次数+1
			logger.info(baseSendMessageDTO.getAddress()+"第"+num+"次发送失败！！！！！");
			baseSendMessageService.editBaseSendMessage(baseSendMessageDTO);
			
		}else{
			logger.info(baseSendMessageDTO.getAddress()+"第5次发送失败！！！！！");
		}
	}
	
	/**
	 * 在数据库中获取短信存入到redis
	 * @throws IOException 
	 */
	private synchronized boolean sendInfoToRedis(){
		
		//如果在线程阻塞的情况下（如两个线程阻塞），第一个线程查询数据库完成（包括发生异常，和正常返回数据），
		//第二个线程则判断和前一个线程查询数据库的时间间隔，如果小于配置文件中的时间间隔，则不再查询数据库
		if(!this.verifyTime()){
			return true;
		}
		//如果在线程阻塞的情况下（如两个线程阻塞），第一个线程查询数据库成功，并且获取到数据放到了消息队列中，
		//消息队列中有数据,不再查询数据库
//		if(redisDB.headPop(Constants.MESSAGE_KEY)!=null){
//			return true;
//		}
		//设置查询数据库的时间
		lastQueryDBTime = new Date();
		this.getInfoFormMySql();
		return false;
	}
	
	
	/**
	 * 查询数据库是否存在需要发送消息的信息，如果有，则放入redis队列
	 * @throws IOException 
	 */
	private  void getInfoFormMySql(){
		
		//查询没有发送过的信息 
		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
		baseSendMessageDTO.setIsSend("0");
		baseSendMessageDTO.setMinSendNum(0);
		baseSendMessageDTO.setMaxSendNum(5);
		Pager pager = new Pager();
		pager.setRows(10000);
		DataGrid<BaseSendMessageDTO> dataGrid = baseSendMessageService.questyBaseSendMessage(baseSendMessageDTO, pager);
		//如果有需要发送的消息
		if(dataGrid.getTotal()!=0){
			
			for(int i = 0 ; i < dataGrid.getRows().size() ; i++){
				System.out.println(dataGrid.getRows().get(i).getAddress());

				//判断这个条消息，在redis中的list中是否存在,如果不存在，则将这条信息存放在消息队列队尾
				if(StringUtils.isBlank(redisDB.get(dataGrid.getRows().get(i).getId()+""))){
					redisDB.tailPush(Constants.MESSAGE_KEY, dataGrid.getRows().get(i));
				}else{
					logger.info("list存在此条消息");
				}
			}
		}else{
			logger.info("数据库中不存在需要发送消息的数据");
		}
	}
	/**
	 * 检验时间，判断时间是否和上次查询数据库的时间间隔超过配置文件中设定的时间间隔
	 * @return
	 */
	private boolean verifyTime(){
		//读取配置文件，获取查询数据库频率的参数，以分钟为单位
		String queryDbTime = MessageTemplateFileUtil.getInstance().getValue("query_db_time");
		//如果配置文件中没有配置此项，则默认为30分钟
		int queryDbTimeInterval = 30;
		if(!StringUtils.isBlank(queryDbTime)){
			queryDbTimeInterval = Integer.valueOf(queryDbTime);
		}
		//如果和上次查询数据库间隔超过30分钟，则在消息队列为空时查询数据库
		if(lastQueryDBTime!=null&&(new Date().getTime()-lastQueryDBTime.getTime())/1000/60 >=queryDbTimeInterval){
			return true;
		}
		return false;
	}

}
