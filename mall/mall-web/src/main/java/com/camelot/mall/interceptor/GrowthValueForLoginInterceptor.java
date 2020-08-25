package com.camelot.mall.interceptor;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.basecenter.dto.GrowthDTO;
import com.camelot.basecenter.service.GrowthService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserGrowthDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserGrowthService;
import com.camelot.util.DateUtil;
/**
 * 
 * <p>Description: [成长值拦截器(登录)]</p>
 * Created on 2015-12-11
 * @author  <a href="mailto: xxx@camelotchina.com">化亚会</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class GrowthValueForLoginInterceptor extends HeaderTopInterceptor{
	
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private RedisDB redisDB;
	@Resource
	private GrowthService growthService;  //成长值配置接口
	private GrowthDTO growthDto;  //成长值配置传参DTO
	@Resource
	private UserExportService userExportService;    //用户信息的接口
	private UserDTO userDto;  //用户信息传单DTO
	@Resource
	private UserGrowthService userGrowthService;  //成长值记录接口
	private UserGrowthDTO userGrowthDto;  //成长值记录传参DTO
	
	public static int SECONDS = 24*60*60; //一天包含的秒数
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//获取用户userid
		if(request.getAttribute("GrowthUserId")==null)
			return;
		Long userId = Long.parseLong(request.getAttribute("GrowthUserId").toString());
		if(userId==null)
			return;
		//先查询redis中，是否有之前的登录拦截痕迹  true有  false无
		boolean bel = redisDB.exists(Constants.GROWTH_FOR_LOGIN+userId);
		//获取当天时间的字符串  “yyyy-MM-dd”
		String newDate = DateUtil.getNewDate(new Date(), "yyyy-MM-dd");
		if(bel){//有之前的拦截痕迹    则获取之前拦截时间，做对比
			String date = redisDB.get(Constants.GROWTH_FOR_LOGIN+userId);  //获取之前拦截时间
			if(!date.equals(newDate)){ //redis中记录的拦截时间和当前时间不一致，这认为是当前天第一次登录
				//通过成长值的获取类型查询成长值配置信息
				growthDto = new GrowthDTO();
				growthDto.setType(Integer.parseInt(Constants.GROWTH_VALUE_LOGIN));
				growthDto = growthService.getGrowthDTO(growthDto);  // 通过成长值增加类型  查询增加的成长值多少
				if(growthDto.getGrowthValue() == null){
					growthDto.setGrowthValue(BigDecimal.ZERO);
				}
				//成长值记录DTO  参数
				userGrowthDto = new UserGrowthDTO();
				userGrowthDto.setType(Constants.GROWTH_VALUE_LOGIN);
				userGrowthDto.setUserId(userId.intValue());
				userGrowthDto.setGrowthValue(growthDto.getGrowthValue());
				//通过id查找用户信息
				userDto = userExportService.queryUserByfId(userId);
				if(userDto.getGrowthValue() == null){
					userDto.setGrowthValue(BigDecimal.ZERO);
				}
				//对成长值做处理，并添加成长值记录，修改用户信息中的总成长值
				try {
					//添加成长值记录
					userGrowthService.addUserGrowthInfo(userGrowthDto);
					//修改用户信息中的总成长值
					userDto.setGrowthValue(growthDto.getGrowthValue().add(userDto.getGrowthValue()));
					userExportService.modifyUserInfo(userDto);
					//修改redis中的拦截信息n
					redisDB.setAndExpire(Constants.GROWTH_FOR_LOGIN+userId, newDate, SECONDS);
				} catch (Exception e) {
					LOG.info("成长值异常信息："+e.getMessage());
					e.printStackTrace();
				}
			}else{
				return;
			}
		}else{//无之前的拦截痕迹  认为当前天第一次登录
			//通过成长值的获取类型查询成长值配置信息
			growthDto = new GrowthDTO();
			growthDto.setType(Integer.parseInt(Constants.GROWTH_VALUE_LOGIN));
			growthDto = growthService.getGrowthDTO(growthDto);  // 通过成长值增加类型  查询增加的成长值多少
			if(growthDto.getGrowthValue() == null){
				growthDto.setGrowthValue(BigDecimal.ZERO);
			}
			//成长值记录DTO  参数
			userGrowthDto = new UserGrowthDTO();
			userGrowthDto.setType(Constants.GROWTH_VALUE_LOGIN);
			userGrowthDto.setUserId(userId.intValue());
			userGrowthDto.setGrowthValue(growthDto.getGrowthValue());
			//通过id查找用户信息
			userDto = userExportService.queryUserByfId(userId);
			if(userDto.getGrowthValue() == null){
				userDto.setGrowthValue(BigDecimal.ZERO);
			}
			//对成长值做处理，并添加成长值记录，修改用户信息中的总成长值
			try {
				//添加成长值记录
				userGrowthService.addUserGrowthInfo(userGrowthDto);
				//修改用户信息中的总成长值
				userDto.setGrowthValue(growthDto.getGrowthValue().add(userDto.getGrowthValue()));
				userExportService.modifyUserInfo(userDto);
				//修改redis中的拦截信息
				redisDB.setAndExpire(Constants.GROWTH_FOR_LOGIN+userId, newDate, SECONDS);
			} catch (Exception e) {
				LOG.info("成长值异常信息："+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
