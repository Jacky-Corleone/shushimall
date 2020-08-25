package com.camelot.mall.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.sellercenter.malltheme.service.MallThemeService;

/**
 * 省市拦截器，目前用于添加商品时维护区域价格
 * @author fuyu
 *
 */
public class ProvinceCitysInterceptor implements HandlerInterceptor {
    //tdk存储在redis中的key值
    private static final String KEY_TDK_REDIS = "provincecitys";

    @Resource
    private AddressBaseService addressBaseService;
    @Resource
    private MallThemeService mallThemeService;
    
    
    @Resource
    private RedisDB redisDB;

    @SuppressWarnings("unchecked")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//从缓存中查询,如果不存在则取数据库查询
    	List<Map<AddressBaseDTO, List<AddressBaseDTO>> > list = (List<Map<AddressBaseDTO, List<AddressBaseDTO>> >)redisDB.getObject(KEY_TDK_REDIS);
    	if(list == null){
        	list = new ArrayList<Map<AddressBaseDTO,List<AddressBaseDTO>>>();
        	List<AddressBaseDTO> provinceList = addressBaseService.getAddressesByLevel(1); //获取省
    		List<AddressBaseDTO> cityList = addressBaseService.getAddressesByLevel(2); //获取市
    		
    		for(AddressBaseDTO province : provinceList) {
    			//去掉全国
    			if (province.getCode().equals("0")) {
					continue;
				}
    			Map<AddressBaseDTO, List<AddressBaseDTO>> map = new HashMap<AddressBaseDTO, List<AddressBaseDTO>>();
    			List<AddressBaseDTO> citys = new ArrayList<AddressBaseDTO>();
    			for(AddressBaseDTO city : cityList) {
    				if(city.getParentcode().equals(province.getCode())) {
    					citys.add(city);
    				}
    			}
    			map.put(province, citys);
    			list.add(map);
    		}
        	
    		//地区数据放入缓存中
    		redisDB.addObject(KEY_TDK_REDIS, list,3600);
        	
    	}
        request.setAttribute("provinceCitys", list);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
