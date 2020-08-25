package com.camelot.mall.interceptor;

import java.math.BigDecimal;

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
import com.camelot.util.WebUtil;
/**
 * 
 * <p>Description: [成长值拦截器(评价,评价+晒单，购物)]</p>
 * Created on 2015-12-4
 * @author  <a href="mailto: xxx@camelotchina.com">化亚会</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class GrowthValueInterceptor extends HeaderTopInterceptor{
	
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
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//获取成长值的获取类型
		String growthValueType = (String)request.getAttribute("GrowthValue");
		if(growthValueType!=null&&growthValueType.trim()!=""){ //不为空或者空字段
			//通过成长值的获取类型查询成长值配置信息
			growthDto = new GrowthDTO();
			growthDto.setType(Integer.parseInt(growthValueType));
			growthDto = growthService.getGrowthDTO(growthDto);  // 通过成长值增加类型  查询增加的成长值多少
			if(growthDto.getGrowthValue() == null){
				growthDto.setGrowthValue(BigDecimal.ZERO);
			}
			//获取用户userid
			Long userId = WebUtil.getInstance().getUserId(request);
			if(userId==null)
				return;
			//成长值记录DTO  参数
			userGrowthDto = new UserGrowthDTO();
			userGrowthDto.setType(growthValueType);
			userGrowthDto.setUserId(userId.intValue());
			userGrowthDto.setGrowthValue(growthDto.getGrowthValue());
			//通过id查找用户信息
			userDto = userExportService.queryUserByfId(userId);
			if(userDto.getGrowthValue() == null){
				userDto.setGrowthValue(BigDecimal.ZERO);
			}
			try {
				//对成长值做处理，并添加成长值记录，修改用户信息中的总成长值
				if(growthValueType.equals(Constants.GROWTH_VALUE_SHOPPING)){ //购物获取  "2"
					BigDecimal payValue = new BigDecimal(request.getAttribute("payValue").toString());
					userGrowthDto.setGrowthValue(payValue.multiply(growthDto.getGrowthValue().divide(new BigDecimal("100"))));
					//添加成长值记录 
					userGrowthService.addUserGrowthInfo(userGrowthDto);
					//修改用户信息中的总成长值
					userDto.setGrowthValue(userDto.getGrowthValue().add(payValue.multiply(growthDto.getGrowthValue().divide(new BigDecimal("100")))));
					userExportService.modifyUserInfo(userDto);
				}else if(growthValueType.equals(Constants.GROWTH_VALUE_EVALUATION)){ //评价获取  "3"
					//添加成长值记录 
					userGrowthService.addUserGrowthInfo(userGrowthDto);
		 			//修改用户信息中的总成长值
					userDto.setGrowthValue(growthDto.getGrowthValue().add(userDto.getGrowthValue()));
					userExportService.modifyUserInfo(userDto);
				}else if(growthValueType.equals(Constants.GROWTH_VALUE_EVALUATION_AND_EXPOSURE)){ //评价晒照获取  "4"
					//添加成长值记录
					userGrowthService.addUserGrowthInfo(userGrowthDto);
					//修改用户信息中的总成长值
					userDto.setGrowthValue(growthDto.getGrowthValue().add(userDto.getGrowthValue()));
					userExportService.modifyUserInfo(userDto);
				}
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
