package com.camelot.mall.basecenter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.util.WebUtil;

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
public class AddressInfoController {

	@Resource
	private AddressInfoService addressInfoService;
	/**
	 * 
	 * <p>Discription:[收获人地址查询]</p>
	 * Created on 2015年3月11日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping("/addressList")
	@ResponseBody
	public DataGrid<AddressInfoDTO> addressList(HttpServletRequest request){
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
    		return null;
    	}
		DataGrid<AddressInfoDTO> dg = addressInfoService.queryAddressinfo(Long.valueOf(uid));
		return dg;
	}
	/**
	 * 
	 * <p>Discription:[查询收获地址详情]</p>
	 * Created on 2015年3月11日
	 * @param id
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping("/addressInfo")
	@ResponseBody
	public ExecuteResult<AddressInfoDTO> addressInfo(Long id){
		ExecuteResult<AddressInfoDTO> result = new ExecuteResult<AddressInfoDTO>();
		List<String> errorMess = new ArrayList<String>();
		if(id==null){
			errorMess.add("收获地址Id不能为空！");
			result.setErrorMessages(errorMess);
			return result;
		}
		result = addressInfoService.queryAddressinfoById(id);
		System.out.println("ExecuteResult<AddressInfoDTO>==="+JSON.toJSONString(result));
		return result;
	}
	
	@RequestMapping("/addAddress")
	@ResponseBody
	public ExecuteResult<String> addAddress(HttpServletRequest request){
		ExecuteResult<String> result = new ExecuteResult<String>();
		ExecuteResult<AddressInfoDTO> resultBuild =  buildAddressInfoDTO(request);
		if(resultBuild.getErrorMessages().size()>0){
			result.setErrorMessages(resultBuild.getErrorMessages());
			return result;
		}
		
		if( resultBuild.getResult().getId() != null  ){
			result = addressInfoService.modifyAddressInfo(resultBuild.getResult());
		}else{
			result = addressInfoService.addAddressInfo(resultBuild.getResult());
		}
		System.out.println("ExecuteResult<String>==="+JSON.toJSONString(result));
		return result;
	}
	
	public ExecuteResult<AddressInfoDTO> buildAddressInfoDTO(HttpServletRequest request){
		ExecuteResult<AddressInfoDTO> result = new ExecuteResult<AddressInfoDTO>();
		List<String> errorMess = new ArrayList<String>();
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			errorMess.add("uid不能为空，并且必须是数字");
    	}
		String provicecode = request.getParameter("provicecode");//省编号
		if(StringUtils.isEmpty(provicecode) || !StringUtils.isNumeric(provicecode)){
			errorMess.add("provicecode不能为空，并且必须是数字");
    	}
		String citycode = request.getParameter("citycode");//市编号
		if(StringUtils.isEmpty(citycode) || !StringUtils.isNumeric(citycode)){
			errorMess.add("citycode不能为空，并且必须是数字");
    	}
		String countrycode = request.getParameter("countrycode");//县、县级市、区编号
		if(StringUtils.isEmpty(countrycode) || !StringUtils.isNumeric(countrycode)){
			errorMess.add("countrycode不能为空，并且必须是数字");
    	}
		String fulladdress = request.getParameter("fulladdress");//详细地址
		if(StringUtils.isEmpty(fulladdress)){
			errorMess.add("fulladdress不能为空！");
    	}
		String isdefault = request.getParameter("isdefault");//1:默认2:不是
		if(!"1".equals(isdefault) && !"2".equals(isdefault)){
			errorMess.add("isdefault值不合法");
    	}
		String contactperson = request.getParameter("contactperson");//联系人、收货人
		if(StringUtils.isEmpty(contactperson)){
			errorMess.add("contactperson不能为空！");
    	}
		String contactphone = request.getParameter("contactphone");//联系人手机
		if(StringUtils.isEmpty(contactphone) || !StringUtils.isNumeric(contactphone)){
			errorMess.add("contactphone不能为空，并且必须是数字");
    	}
		String contacttel = request.getParameter("contacttel");//联系人座机
		
		if(errorMess.size()>0){
			result.setErrorMessages(errorMess);
			return result;
		}
		String id = request.getParameter("addressId");
		AddressInfoDTO addressInofDTO = new AddressInfoDTO();
		addressInofDTO.setBuyerid(Long.valueOf(uid));
		addressInofDTO.setProvicecode(provicecode);
		addressInofDTO.setCitycode(citycode);
		addressInofDTO.setCountrycode(countrycode);
		addressInofDTO.setFulladdress(fulladdress);
		addressInofDTO.setIsdefault(Integer.parseInt(isdefault));
		addressInofDTO.setContactperson(contactperson);
		addressInofDTO.setContactphone(contactphone);
		addressInofDTO.setContacttel(contacttel);
		if( id != null && !"".equals(id) )
			addressInofDTO.setId(Long.valueOf(id));
		result.setResult(addressInofDTO);
		return result;
	}
	
	/**
	 * <p>Discription: 设置默认收货地址，用于订单核对页</p>
	 * Created on 2015年4月3日
	 * @param request
	 * @param id
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/setDefAddress")
	@ResponseBody
	public ExecuteResult<String> setDefAddress( HttpServletRequest request, Long id ){
		Long uid = WebUtil.getInstance().getUserId(request);
		return this.addressInfoService.modifyDefaultAddress(id, uid);
	}
	
	@RequestMapping("/delAddress")
	@ResponseBody
	public ExecuteResult<String> delAddress(Long id){
		return this.addressInfoService.removeAddresBase(id);
	}
}
	
