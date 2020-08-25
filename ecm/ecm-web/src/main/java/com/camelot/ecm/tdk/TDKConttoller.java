package com.camelot.ecm.tdk;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.basecenter.service.BaseTDKConfigService;
import com.camelot.common.EcmConstants;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;

/**
 * <p>Description: [TDK设置]</p>
 * Created on 2015年5月12日
 * @author  王东晓
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Controller
@RequestMapping(value = "${adminPath}/tdk")
public class TDKConttoller {
	@Resource
	private BaseTDKConfigService baseTDKConfigService;
	@Resource 
	private RedisDB redisDB;
	
	/**
     * <p>Description: [TDK属性展示]</p>
     * Created on 2015年5月12日
     * @author  王东晓
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	@RequestMapping(value="tdkIndex")
	public String tdkIndex(@ModelAttribute("tdkDTO") BaseTDKConfigDTO tdkDTO,Model model){
		//查询TDK详细属性信息
		ExecuteResult<BaseTDKConfigDTO> executeResult = baseTDKConfigService.queryBaseTDKConfig(null);
		//判断查询过程中是否发生异常
		if(executeResult.isSuccess()){
//			BaseTDKConfigDTO tdk = new BaseTDKConfigDTO();
//			tdk.setTitle("title");
//			tdk.setDescription("description");
//			tdk.setKeywords("keywords");
			//如果返回的结果为空,向前台页面传递一个空的TDO信息，否则想前台传递查询结果
			if(executeResult.getResult()==null){
				model.addAttribute("tdkDTO", tdkDTO);
			}else{
				model.addAttribute("tdkDTO", executeResult.getResult());
			}
		}else{
			//查询过程中发生异常，想前台页面发送异常信息
			model.addAttribute("errorMsg","查询过程中发生异常！");
		}
		return "tdk/tdkInfo";
	}
	
	/**
     * <p>Description: [添加TDK信息]</p>
     * Created on 2015年5月12日
     * @author  王东晓
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	@ResponseBody
	@RequestMapping(value="addTDK")
	public Map<String,Object> addTDK(BaseTDKConfigDTO tdkDTO){
		ExecuteResult<BaseTDKConfigDTO> executeResult = baseTDKConfigService.saveBaseTDKConfig(tdkDTO);
		//判断保存是否成功
		Map<String,Object> map = new HashMap<String,Object>();
		if(executeResult.isSuccess()){
			map.put("resultMsg", "保存成功！");
			redisDB.addObject(EcmConstants.TKD_KEY, tdkDTO);
		}else{
			map.put("resultMsg", "保存失败，请重试！");
		}
		return map;
	}
	
	/**
     * <p>Description: [更新TDK信息]</p>
     * Created on 2015年5月12日
     * @author  王东晓
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	@ResponseBody
	@RequestMapping(value="modifyTDK")
	public Map<String,Object> modifyTDK(BaseTDKConfigDTO tdkDTO){
		ExecuteResult<BaseTDKConfigDTO> executeResult = baseTDKConfigService.modifyBaseTDKConfig(tdkDTO);
		//判断更新是否成功
		Map<String,Object> map = new HashMap<String,Object>();
		if(executeResult.isSuccess()){
			map.put("resultMsg", "更新成功！");
			redisDB.addObject(EcmConstants.TKD_KEY, tdkDTO);
		}else{
			map.put("resultMsg", "更新失败，请重试！");
		}
		return map;
	}
	
}
