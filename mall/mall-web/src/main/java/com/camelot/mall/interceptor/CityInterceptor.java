package com.camelot.mall.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
/**
 * Created by Administrator on 2015/5/28.
 */
public class CityInterceptor implements HandlerInterceptor {
    //tdk存储在redis中的key值
    private static final String KEY_TDK_REDIS = "city";

    @Resource
    private AddressBaseService addressBaseService;
    @Resource
    private MallThemeService mallThemeService;
    
    
    @Resource
    private RedisDB redisDB;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//从缓存中查询地区数据，如果不存在则取数据库查询
    	Map<String,List<AddressBaseDTO>> newAddressBaseDTOMap = (Map<String,List<AddressBaseDTO>>)redisDB.getObject(KEY_TDK_REDIS);
    	if(newAddressBaseDTOMap == null){
    		//获取二级城市
        	List<AddressBaseDTO> addressBaseDTOList = addressBaseService.getAddressesByLevel(2);
        	//获取地域子站
        	DataGrid<MallThemeDTO> dg = this.mallThemeService.queryMallThemeList(null, "1", null);
        	newAddressBaseDTOMap = new LinkedHashMap<String,List<AddressBaseDTO>>();
        	AddressBaseDTO addressBaseDTO = null;
        	if(dg == null || dg.getRows() == null || dg.getRows().size() == 0)
    			return true;
        	//首页只展示在地域子站中维护的地区
        	for(int i = 0;i<addressBaseDTOList.size();i++){
        		addressBaseDTO = addressBaseDTOList.get(i);
        		for(MallThemeDTO mallThemeDTO : dg.getRows()){
        			if(mallThemeDTO.getCityCode()==null||!mallThemeDTO.getCityCode().toString().equals(addressBaseDTO.getCode()))
        				continue;
        			//根据首字母排序
        			List<AddressBaseDTO> newAddressBaseDTOList = newAddressBaseDTOMap.get(addressBaseDTO.getNameFirstLetter());
        			if(newAddressBaseDTOList == null){
        				newAddressBaseDTOList = new ArrayList<AddressBaseDTO>();
        				newAddressBaseDTOList.add(addressBaseDTO);
        				newAddressBaseDTOMap.put(addressBaseDTO.getNameFirstLetter(),newAddressBaseDTOList);
        			}else{
        				newAddressBaseDTOList.add(addressBaseDTO);
        			}
        		}
        	}
        	//地区数据放入缓存中
        	redisDB.addObject(KEY_TDK_REDIS, newAddressBaseDTOMap,3600);
    	}
        request.setAttribute("newAddressBaseDTOMap", newAddressBaseDTOMap);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
