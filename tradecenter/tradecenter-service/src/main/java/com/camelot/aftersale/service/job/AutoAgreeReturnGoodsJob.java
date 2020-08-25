package com.camelot.aftersale.service.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.MessageContentTypeEnum;
import com.camelot.openplatform.common.enums.MessageTypeEnum;
import com.camelot.openplatform.common.util.MessageTemplateFileUtil;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

public class AutoAgreeReturnGoodsJob {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource
	private TradeReturnExportService tradeReturnExportService;
    @Resource
    private TradeReturnGoodsService tradeReturnGoodsService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService;
	@Resource
	private BaseSendMessageService baseSendMessageService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
    /**
     * 买家发出退货申请 卖家7天 不处理 自动同意退货
     */
	public void executeJob(){

		System.out.println("买家发出退货申请 卖家7天 不处理 自动同意退货");
        TradeReturnGoodsDTO tradeReturnDto=new TradeReturnGoodsDTO();
        tradeReturnDto.setState(1);  //查询是  1 退款申请等待卖家确认中 的数据
        Date preDate= DateUtils.dayOffset(new Date(), -7);
        tradeReturnDto.setTimeNode(preDate);

        List<TradeReturnGoodsDTO>  resData=tradeReturnGoodsService.searchByCondition (tradeReturnDto);
        if(resData!=null&&resData.size()>0){

            for(TradeReturnGoodsDTO item:resData){
                item.setState(3); // 改为 3 退款申请达成,等待买家发货
                item.setLastUpdDt(new Date());
                tradeReturnGoodsService.updateSelective(item);
                ExecuteResult<TradeReturnGoodsDTO> executeResult = tradeReturnExportService.updateTradeReturnStatus(item, TradeReturnStatusEnum.PASS);
               
                if(!executeResult.isSuccess()){
                	log.error("自动确认退款申请失败");
                	return ;
                }
                this.sendMessage(item,item.getOrderId());

            }
        }
	}
	
	//卖家7天内没有对买家的申请退款进行处理，则自动同意退款，并同时给卖家邮箱和短信发送同意通知
	private void sendMessage(TradeReturnGoodsDTO item,String orderId){
		
        List<String> sellerIdList = new ArrayList<String>(1);
        sellerIdList.add(item.getSellerId());
        //需要获取卖家用户的基本信息:电话和邮箱
        ExecuteResult<List<UserDTO>> userResult = userExportService.findUserListByUserIds(sellerIdList);
        if(!userResult.isSuccess()&&userResult.getResult().size()==0){
        	log.error("买家退款申请7天内卖家没有做出处理，在给卖家发送通知消息时获取用户信息失败。。。");
        	return ;
        }
        
        //获取短信模板信息
        MessageTemplateFileUtil messageTemplateFileUtil = MessageTemplateFileUtil.getInstance();
        String smsMessage = messageTemplateFileUtil.getValue("sms_auto_agree_return_goods_s");
        String mailMessage = messageTemplateFileUtil.getValue("mail_auto_agree_return_goods_s");
        String wsMessage = messageTemplateFileUtil.getValue("ws_auto_agree_return_goods_s");
        String mailContentTop = messageTemplateFileUtil.getValue("mail_content_top");
        String mailContentBottom = messageTemplateFileUtil.getValue("mail_content_bottom");
        String mailTitle = messageTemplateFileUtil.getValue("mail_title");
        
        UserDTO userDTO = userResult.getResult().get(0);
        //判断卖家属于那个平台
        //根据订单ID获取此订单是属于哪个平台的订单
        ExecuteResult<TradeOrdersDTO>  tradeOrderResult = tradeOrderExportService.getOrderById(orderId);
        if(!tradeOrderResult.isSuccess()){
        	log.error("查询不到此订单信息..订单号："+orderId);
        	return ;
        }
        Integer platformId = tradeOrderResult.getResult().getPlatformId();
        
        smsMessage = smsMessage.replaceAll("orderId", orderId);
        mailMessage = mailMessage.replaceAll("orderId", orderId);
        wsMessage = wsMessage.replaceAll("orderId", orderId);
        
        final BaseSendMessageDTO smsMessageDTO = new BaseSendMessageDTO();
        smsMessageDTO.setAddress(userDTO.getUmobile());
        smsMessageDTO.setContent(smsMessage);
        smsMessageDTO.setType(MessageTypeEnum.SMS.getId());
        smsMessageDTO.setContentType(MessageContentTypeEnum.NOTIC.getId());
        smsMessageDTO.setPlatformId(platformId);
        if(!StringUtils.isEmpty(wsMessage)){
        	//发送短信通知
        	ExecuteResult<String> result = baseSendMessageService.sendMessageToMQ(smsMessageDTO);
        }else{
        	log.error("没有获取到短信模板信息");
        }
        
        
        final BaseSendMessageDTO emailMessageDTO = new BaseSendMessageDTO();
        emailMessageDTO.setAddress(userDTO.getUserEmail());
        emailMessageDTO.setTitle(mailTitle);
        emailMessageDTO.setContent(mailContentTop+mailMessage+mailContentBottom);
        emailMessageDTO.setType(MessageTypeEnum.MAIL.getId());
        emailMessageDTO.setContentType(MessageContentTypeEnum.NOTIC.getId());
        emailMessageDTO.setPlatformId(platformId);
        if(!StringUtils.isEmpty(wsMessage)){
	        //发送邮件通知
        	ExecuteResult<String> result = baseSendMessageService.sendMessageToMQ(emailMessageDTO);
        }else{
        	log.error("没有获取到邮件模板信息");
        }
        //发送站内信
        WebSiteMessageDTO webSiteMessageDTO = new WebSiteMessageDTO();
		webSiteMessageDTO.setWmRead(1);
		webSiteMessageDTO.setWmCreated(new Date());
		webSiteMessageDTO.setModified(new Date());
		webSiteMessageDTO.setType(1);
        webSiteMessageDTO.setWmToUserid(userDTO.getUid());
		webSiteMessageDTO.setWmToUsername(userDTO.getUname());
		webSiteMessageDTO.setPlatformId(platformId);
		//信息内容
		webSiteMessageDTO.setWmContext(wsMessage); 
		if(!StringUtils.isEmpty(wsMessage)){
			
			baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
		}else{
        	log.error("没有获取到邮件模板信息");
        }
		log.info("开始向卖家推送站内信完成");
        
	}
}
