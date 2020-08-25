package com.camelot.mall.basecenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.CookieHelper;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;

/**
 * 
 * <p>Description: [获取省市区 ]</p>
 * Created on 2015年3月6日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/address")
public class AddressBaseController {

	@Resource
	private AddressBaseService addressBaseService;

	@Resource
	private MallThemeService mallThemeService;
	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年3月6日
	 * @param id 
	 * 			查省份，id = 0
	 * 			查省下面的市， id= 省份id
	 * 			查市下面的区/县， id= 市id
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping("/query")
	public List<AddressBase> queryAddressBase(String id) {
		List<AddressBase> listshen=addressBaseService.queryAddressBase(id);
		return listshen;
	}
	
	/**
	 * 
	 * <p>Discription:[获取所有的2级地址]</p>
	 * Created on 2016年2月25日
	 * @return
	 * @author:[WHW]
	 */
	@ResponseBody
	@RequestMapping("/queryL2")
	public List<AddressBaseDTO> queryAddressBaseL2() {
		String[] codes = mallThemeService.queryGroupCityCode();
		if(codes.length==0){
			return null;
		}
		ExecuteResult<List<AddressBaseDTO>> er =addressBaseService.queryNameByCode(codes);
		return er.getResult();
	}
	
	/**
	 * <p>Discription:[验证当前区域是否在子站维护数据中]</p>
	 * Created on 2016年3月1日
	 * @return 1:存在 0:不存在
	 * @author:[WHW]
	 */
	@ResponseBody
	@RequestMapping("/validationMallTheme")
	public String validationMallTheme(Long addressCode) {
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		mallThemeDTO.setCityCode(addressCode);
		DataGrid<MallThemeDTO> dg = mallThemeService.queryMallThemeList(mallThemeDTO,"1",new Pager());
		if(dg.getRows()!=null&&dg.getRows().size()>0){
			return "1";
		}
		return "0";
	}
	/**
	 * 
	 * <p>Discription:[根据省市区的code查询名称]</p>
	 * Created on 2015年3月12日
	 * @param code
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody	
	@RequestMapping("/getNameByCode")
	public ExecuteResult<AddressBaseDTO> getNameByCode(Integer code) {
		ExecuteResult<AddressBaseDTO> dto = addressBaseService.queryNameById(code);
		return dto;
	}
	
	/**
	 * <p>通过<code>areaCode</code>取得</p>
	 * <ul>
	 * 	<li>完整的地址名称：如果code是level3</li>
	 * 	<li>此<code>areaCode</code>对应的地址名称：如果code是level1</li>
	 * </ul>
	 * 
	 * Created on 2016年2月14日
	 * @param areaCode 地址的code
	 * @return
	 * @author 顾雨
	 */
	@ResponseBody
	@RequestMapping("/getCodeAndQualifiedName")
	public Object getCodeAndQualifiedName(int areaCode,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> CodeAndName = new HashMap<String, Object>();
		if(areaCode == 0){
			areaCode = 4201;
			CookieHelper.setCookie(response, Constants.REGION_CODE, "4201");
		}
		if (isLevelOne(areaCode)) {
			ExecuteResult<AddressBaseDTO> OneAddressInER = addressBaseService.queryNameById(areaCode);
			AddressBaseDTO OneAddress = OneAddressInER.getResult();
			String qualifiedName = OneAddress.getName();
			String code = OneAddress.getCode();
			CodeAndName.put("qualifiedName", qualifiedName);
			CodeAndName.put("code", code);
		} else { // 这里默认为Level2
			String qualifiedName = addressBaseService.getQualifiedName(areaCode);
			int code = areaCode;
			CodeAndName.put("qualifiedName", qualifiedName);
			CodeAndName.put("code", code);
		}
		
		return CodeAndName;
	}

	private boolean isLevelOne(int areaCode) {
		return areaCode < 100 ? true : false;
	}
	
}
	
