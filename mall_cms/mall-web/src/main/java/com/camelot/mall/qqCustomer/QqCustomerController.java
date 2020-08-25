package com.camelot.mall.qqCustomer;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.QqCustomerDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.QqCustomerService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;

import groovy.util.IFileNameFinder;

/**
 * 
 * <p>Description: [QQ客服Controller]</p>
 * Created on 2016年2月3日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/qqCustomer")
public class QqCustomerController {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private QqCustomerService qqCustomerService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ShopExportService shopService;
	/**
	 * 
	 * <p>Discription:[获取QQ客服列表]</p>
	 * Created on 2016年2月3日
	 * @param model
	 * @param request
	 * @param pager	分页
	 * @param contractSearchModel	查询参数
	 * @return
	 * @author:[李伟龙]
	 */
	@RequestMapping("qqCustomerList")
    public String qqCustomerList(Model model, HttpServletRequest request, Pager pager, String contractSearchModel) {
		//默认返回页面
		String returnUrl = "/qqCustomer/qqCustomerList";
		// 获取当前登录用户信息
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (registerDTO == null) {
		    return "redirect:/user/login";
		}
		//查询条件
		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
		//客服类型
		qqCustomerDTO.setCustomerType(Constants.TYPE_SHOP);
		//卖家id
		qqCustomerDTO.setUserId(registerDTO.getUid());
		//是否删除
		qqCustomerDTO.setDeletedFlag(Constants.NO_DEL);
		//有查询条件时候返回查询条件页面
		if (StringUtils.isNotEmpty(contractSearchModel)) {
			JSONObject searchModel = JSONObject.parseObject(contractSearchModel, JSONObject.class);
		    String customerQqNumber = searchModel.getString("customerQqNumber");
		    //根据客服QQ号查询
		    if (StringUtils.isNotEmpty(customerQqNumber)) {
		    	qqCustomerDTO.setCustomerQqNumber(customerQqNumber);
		    }
			returnUrl = "/qqCustomer/qqCustomerPage";
		}
		//查询结果
		ExecuteResult<DataGrid<QqCustomerDTO>> result = qqCustomerService.selectListByConditionAll(pager, qqCustomerDTO);
		if(null != result.getResult() ){
			pager.setTotalCount(result.getResult().getTotal().intValue());
			pager.setRecords(result.getResult().getRows());
			model.addAttribute("pager", pager);
		}
		//查询是否有默认用户
		String isDef = "0";
		qqCustomerDTO.setIsDefault(Constants.IS_DEL);
		ExecuteResult<DataGrid<QqCustomerDTO>> isDefresult = qqCustomerService.selectListByConditionAll(pager, qqCustomerDTO);
		if(null != isDefresult.getResult()){
			if(null != isDefresult.getResult().getRows() && isDefresult.getResult().getRows().size() > 0){
				isDef = "1";
			}
		}
		model.addAttribute("isDef", isDef);
		return returnUrl;
	}
	
	/**
	 * 
	 * <p>Discription:[QQ客服新增修改方法]</p>
	 * Created on 2016年2月3日
	 * @param model
	 * @param request
	 * @param id	QQ客服表主键
	 * @param customerQqNumber	QQ号码
	 * @param isDefault	是否默认客服 0 否 1是
	 * @param add_modify 新增 add 或者修改 modify
	 * @return
	 * @author:[李伟龙]
	 */
	@RequestMapping("qqCustomerSave")
    @ResponseBody
    public ExecuteResult<Boolean> couponsUserUpdate(Model model, HttpServletRequest request, String id,String customerQqNumber,Integer isDefault,String add_modify) {
    	ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
    	// 获取当前登录用户信息
    	String token = LoginToken.getLoginToken(request);
    	RegisterDTO registerDTO = userExportService.getUserByRedis(token);
    	if (registerDTO == null) {
    	    result.setResultMessage("您还没有登录，请先登录");
    	    return result;
    	}
    	QqCustomerDTO qqCustomer = new QqCustomerDTO();
    	qqCustomer.setCustomerQqNumber(customerQqNumber);
    	qqCustomer.setCustomerType(Constants.TYPE_SHOP);
    	qqCustomer.setDeletedFlag(0);
    	ExecuteResult<DataGrid<QqCustomerDTO>> isDefresult = qqCustomerService.selectListByConditionAll(new Pager<QqCustomerDTO>(1,10),qqCustomer);
    	//验证QQ是否唯一
    	if(StringUtils.isEmpty(id)){
    		if(null == isDefresult.getResult().getRows() || isDefresult.getResult().getRows().size() > 0 ){
    			result.setResult(false);
    			result.setResultMessage("QQ号码需要唯一");
    			return result;
    		}
    	}else{
    		if(null != isDefresult.getResult().getRows()&&isDefresult.getResult().getRows().size()>0){
    			if(isDefresult.getResult().getRows().size()>1||!isDefresult.getResult().getRows().get(0).getId().equals(Long.valueOf(id))){
    				result.setResult(false);
        			result.setResultMessage("QQ号码需要唯一");
        			return result;
    			}
    		}
    	}
    	//QQ客服信息
    	QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
    	//新增
    	if(Constants.ADD.equals(add_modify)){
    		qqCustomerDTO.setCustomerQqNumber(customerQqNumber);
    		qqCustomerDTO.setCustomerType(Constants.TYPE_SHOP);
    		qqCustomerDTO.setUserId(registerDTO.getUid());
    		// 获取店铺信息
    		ShopDTO shop = null;
    		ShopDTO queryShop = new ShopDTO();
    		queryShop.setSellerId(registerDTO.getUid());
    		ExecuteResult<DataGrid<ShopDTO>> shopResule = shopService.findShopInfoByCondition(queryShop, new Pager<ShopDTO>());
    		if (null != shopResule.getResult()) {
    		    List<ShopDTO> shopList = shopResule.getResult().getRows();
    		    if (null != shopList) {
	    			shop = shopList.get(0);
	    			qqCustomerDTO.setShopId(shop.getShopId());
	        		qqCustomerDTO.setShopName(shop.getShopName());
    		    }
    		}
    		
    		qqCustomerDTO.setIsDefault(isDefault);
    		qqCustomerDTO.setLastOperatorId(registerDTO.getUid());
    		qqCustomerDTO.setLastUpdateDate(new Date());
    		qqCustomerDTO.setDeletedFlag(Constants.NO_DEL);
    		
    		if(new Integer(1).equals(isDefault)){
				//修改所有的客服变成非默认客服
				QqCustomerDTO customer = new QqCustomerDTO();
				customer.setCustomerType(Constants.TYPE_SHOP);
				customer.setShopId(customer.getShopId());
				qqCustomerService.updateMRCustomer(customer);
			}
    		//执行新增
    		qqCustomerService.addQqCustomer(qqCustomerDTO);
    		
    		result.setResultMessage("新增成功");
    	}else if(Constants.MODIFY.equals(add_modify)){
    		if(null != id && !"".equals(id)){
    			qqCustomerDTO.setId(Long.parseLong(id));
    		}else{
    			result.setResultMessage("修改失败");
    		}
    		//查询结果
    		ExecuteResult<DataGrid<QqCustomerDTO>> updateResult = qqCustomerService.selectListByConditionAll(new Pager(), qqCustomerDTO);
    		if(null != updateResult.getResult() ){
    			qqCustomerDTO = updateResult.getResult().getRows().get(0);
    			if(new Integer(1).equals(isDefault)){
    				//修改所有的客服变成非默认客服
    				QqCustomerDTO customer = new QqCustomerDTO();
    				customer.setCustomerType(Constants.TYPE_SHOP);
    				customer.setShopId(customer.getShopId());
    				qqCustomerService.updateMRCustomer(customer);
    			}
    			//添加修改信息
    			qqCustomerDTO.setCustomerQqNumber(customerQqNumber);
    			qqCustomerDTO.setIsDefault(isDefault);
        		qqCustomerDTO.setLastOperatorId(registerDTO.getUid());
        		qqCustomerDTO.setLastUpdateDate(new Date());
        		//执行修改
        		if(qqCustomerService.updateQqCustomer(qqCustomerDTO) > 0){
        			result.setResultMessage("修改成功");
        		}else{
        			result.setResultMessage("修改失败");
        		}
    		}
    	}
    	return result;
     }
    
	/**
	 * 
	 * <p>Discription:[逻辑删除QQ客服]</p>
	 * Created on 2016年2月3日
	 * @param model
	 * @param request
	 * @param ids id字符串
	 * @return
	 * @author:[李伟龙]
	 */
	@RequestMapping("qqCustomerDelete")
	public String couponsDelete(Model model, HttpServletRequest request,@RequestParam(value = "ids") String ids) {
		// 获取当前登录用户信息
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (registerDTO == null) {
			return "redirect:/user/login";
		}

		if (StringUtils.isNotEmpty(ids)) {
			String[] couponsIdsArry = ids.split(";");
			for (String id : couponsIdsArry) {
				QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
				qqCustomerDTO.setId(Long.parseLong(id));
				ExecuteResult<DataGrid<QqCustomerDTO>> updateResult = qqCustomerService.selectListByConditionAll(new Pager(), qqCustomerDTO);
	    		if(null != updateResult.getResult() ){
	    			qqCustomerDTO = updateResult.getResult().getRows().get(0);
	    			qqCustomerDTO.setDeletedFlag(Constants.IS_DEL);
	    			qqCustomerService.updateQqCustomer(qqCustomerDTO);
	    		}
			}
		}
		return "redirect:/qqCustomer/qqCustomerList";
	}
}
