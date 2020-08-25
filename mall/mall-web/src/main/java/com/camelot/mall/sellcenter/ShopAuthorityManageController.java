package com.camelot.mall.sellcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.usercenter.dto.ChildUserDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.dto.UserPermissionDTO;
import com.camelot.usercenter.dto.indto.StoreUserResourceInDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserStorePermissionExportService;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.JsonHelper;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [店铺权限管理]</p>
 * Created on 2015年3月17日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/shopAuthorityManageController")
public class ShopAuthorityManageController {
	
	@Resource
	private UserStorePermissionExportService userStorePermissionExportService;
	
	@Resource
	private UserExportService userExportService;

	@Resource
	private ShopExportService shopService;
	
	/**
	 * <p>Discription:[跳转权限列表页面]</p>
	 * Created on 2015年3月18日
	 * @param 
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/tolist")
	public String tolist(Integer page, Integer moduleType, ItemQueryInDTO itemInDTO,
			HttpServletRequest request, Model model) {
		Pager<TradeOrdersQueryInDTO> pagerin = new Pager<TradeOrdersQueryInDTO>();
		if(null != page){
			pagerin.setPage(page);
		}
		/*** 取userid ***/
		Long parentId = WebUtil.getInstance().getUserId(request);
		ExecuteResult<DataGrid<ChildUserDTO>> executeResult = userStorePermissionExportService.queryChildUserList(parentId, moduleType, pagerin);
		DataGrid<ChildUserDTO> dataGrid = executeResult.getResult();
		
		List<ChildUserDTO> childUserDTOs = dataGrid.getRows();
		List<ChildUserListPost> childUserListPosts = new ArrayList<ChildUserListPost>();
		for(ChildUserDTO childUserDTO : childUserDTOs){
			String resourceName = "";
			List<UserMallResourceDTO> userMallResourceDTOs = childUserDTO.getUserMallResourceList();
			for(UserMallResourceDTO userMallResourceDTO : userMallResourceDTOs){
				resourceName = resourceName + userMallResourceDTO.getResourceName() + "|";
			}
			if(resourceName.length()>0){
				resourceName = resourceName.substring(0, resourceName.length()-1);
			}
			
			//查询店铺名称
			String name = "";
			ChildUserListPost childUserListPost = new ChildUserListPost();
			childUserListPost.setShopId(childUserDTO.getShopId());
			if(childUserDTO.getShopId()!=null){
				ExecuteResult<ShopDTO> er = shopService.findShopInfoById(childUserDTO.getShopId());
				if(er!=null&&er.getResult()!=null){
					childUserListPost.setShopName(er.getResult().getShopName());
				}
			}
			childUserListPost.setUpdateTime(childUserDTO.getUpdateTime());
			childUserListPost.setUserId(childUserDTO.getUserId());
			childUserListPost.setUsername(childUserDTO.getUsername());
			childUserListPost.setResourceIds(resourceName);
			childUserListPost.setNickName(childUserDTO.getNickName());
			
			childUserListPosts.add(childUserListPost);
		}
		
		Pager<ChildUserListPost> pager = new Pager<ChildUserListPost>() ;
		pager.setPage(pagerin.getPage());
		pager.setTotalCount(dataGrid.getTotal().intValue());
		pager.setRecords(childUserListPosts);
		
		if (pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
			pager.setEndPageIndex(pager.getStartPageIndex());
		}
		model.addAttribute("moduleType", moduleType);
		model.addAttribute("pager", pager);
		
		long uid = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(uid);
		if(userDTO!=null && userDTO.getParentId()!=null){//是否为子账号：子账号不允许创建子账号
			model.addAttribute("isChildAccount", "y");
		}else{
			model.addAttribute("isChildAccount", "n");
		}
		
		return "/sellcenter/shop/shopAuthorityManage";
	}

	/**
	 * <p>Discription:[跳转新增页面]</p>
	 * Created on 2015年3月18日
	 * @param 
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/shopEmployeeAdd")
	public String shopEmployeeAdd(Model model, Integer moduleType, HttpServletRequest request) {
		ExecuteResult<List<UserMallResourceDTO>> executeResult = userStorePermissionExportService.queryParentResourceList(moduleType, moduleType);
		List<UserMallResourceDTO> resources = executeResult.getResult();
		model.addAttribute("resources", resources);
		model.addAttribute("moduleType", moduleType);
		
		return "/sellcenter/shop/shopEmployeeAdd";
	}
	
	/**
	 * <p>Discription:[新增子账号]</p>
	 * Created on 2015年3月18日
	 * @param 
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/addEmploy")
	@ResponseBody
	public Map<String, String> addShopEmployee(StoreUserResourceInDTO storeUserResourceInDTO, 
			HttpServletResponse response,String checkbox,String nickname,HttpServletRequest request) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("result", "failure");
		result.put("message", "新增用户失败!");
		if(storeUserResourceInDTO==null || StringUtils.isBlank(storeUserResourceInDTO.getUsername())){
			return result;
		}
		//校验用户名是否已存在
		boolean retVal = userExportService.verifyLoginName(storeUserResourceInDTO.getUsername());
		if(retVal){
			result.put("message", "用户名已存在!");
			return result;
		}
		/*** 取userid，shopid ***/
		long uid = WebUtil.getInstance().getUserId(request);
		Long shopId = WebUtil.getInstance().getShopId(request);
		
		UserDTO userDTO = userExportService.queryUserById(uid);
		checkbox = checkbox.substring(0, checkbox.length()-1);
		String[] cbs = checkbox.split(",");
		Integer[] ints = new Integer[cbs.length];
		for(int i = 0 ;i < cbs.length ;i ++){
			ints[i] = Integer.parseInt(cbs[i]);
		}
		
		storeUserResourceInDTO.setResourceIds(ints);
		storeUserResourceInDTO.setParentId(uid);
		storeUserResourceInDTO.setShopId(shopId);
		storeUserResourceInDTO.setNickName(nickname);
		storeUserResourceInDTO.setPassword(MD5.encipher(storeUserResourceInDTO.getPassword()));
		storeUserResourceInDTO.setUserDTO(userDTO);
		
		
		try{
			ExecuteResult<String> executeResult = userStorePermissionExportService.addStoreUserResource(storeUserResourceInDTO);
			if((executeResult.getResultMessage()).equals("success")){
				result.put("result", "success");
				result.put("message", "创建成功！");
			}
		}catch(Exception e){
			result.put("message", "系统繁忙，请稍后再试！");
		}
		return result;
	}
	
	/**
	 * <p>Discription:[跳转修改页面]</p>
	 * Created on 2015年3月18日
	 * @param 
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/shopEmployeeUpdate")
	public String shopEmployeeUpdate(Model model ,String username ,String nickname, String resourceIds , String userId,String action, Integer moduleType) {
		ExecuteResult<List<UserMallResourceDTO>> executeResult = userStorePermissionExportService.queryParentResourceList(moduleType, moduleType);
		List<UserMallResourceDTO> resources = executeResult.getResult();
		model.addAttribute("resources", resources);
		model.addAttribute("username", username);
		model.addAttribute("userId", userId);
		model.addAttribute("nickname", nickname);
		
		String[] resourceId = resourceIds.split("\\|");
		String checkId= "";
		for( UserMallResourceDTO userMallResourceDTO : resources){
			for(int i = 0;i < resourceId.length;i++){
				if(resourceId[i].equals(userMallResourceDTO.getResourceName()) ){
					checkId = checkId + userMallResourceDTO.getId()+"|";
				}
			}
		}
		if(StringUtils.isNotBlank(checkId)){
			checkId = checkId.substring(0, checkId.length()-1);
		}
		model.addAttribute("checkId", checkId);
		model.addAttribute("action", action);
		model.addAttribute("moduleType", moduleType);
		
		return "/sellcenter/shop/shopEmployeeUpdate";
	}
	
	/**
	 * <p>Discription:[修改操作]</p>
	 * Created on 2015年3月18日
	 * @param 
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/updateEmploy")
	public String updateShopEmployee(HttpServletResponse response,
			HttpServletRequest request,String nickname, String username , String checkbox , String userId , Integer moduleType) {
		
		/*** 取userid，shopid ***/
		Long shopId = WebUtil.getInstance().getShopId(request);
		checkbox = checkbox.substring(0, checkbox.length()-1);
		String[] cbs = checkbox.split(",");
		Integer[] ints = new Integer[cbs.length];
		for(int i = 0 ;i < cbs.length ;i ++){
			ints[i] = Integer.parseInt(cbs[i]);
		}
		
		try{
			UserPermissionDTO userPermissionDTO = new UserPermissionDTO();
			userPermissionDTO.setResourceIds(ints);
			userPermissionDTO.setUserName(username);
			userPermissionDTO.setUserId(Long.parseLong(userId));
			userPermissionDTO.setShopId(shopId);
			userPermissionDTO.setModularType(moduleType);
			ExecuteResult<String> executeResult = userStorePermissionExportService.modifyUserResourceById(userPermissionDTO );
            UserDTO userDTO = userExportService.queryUserById(Long.parseLong(userId));
            if(null!=userDTO){
                userDTO.setNickname(nickname);
                userExportService.modifyUserInfo(userDTO);
            }
			JsonHelper.success(response);
		}catch(Exception e){
			JsonHelper.failure(response);
		}
		return null;
	}
	/**
	 *重置密码 
	 * */
	@RequestMapping("resetPwt")
	@ResponseBody
	public Map<String,Object> resetPwt(HttpServletRequest request,Long userId){
		Map<String,Object> result = new HashMap<String,Object>();
		ExecuteResult<String> retResult = userExportService.resetUserPassword(userId, MD5.encipher("123456"));
		if(retResult.isSuccess()){
			result.put("result", "success");
		}else{
			result.put("result", "failure");
		}
		return result;
	}
	/**
	 * <p>Discription:[删除操作]</p>
	 * Created on 2015年3月24日
	 * @param 
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/deleteEmploy")
	public String deleteShopEmployee(HttpServletResponse response, String userId, Integer moduleType) {
		try {
			Long delteId = Long.parseLong(userId);
			ExecuteResult<String> executeResult = userStorePermissionExportService.deleteUserById(moduleType, delteId);
			if ((executeResult.getResultMessage()).equals("success")) {
				JsonHelper.success(response);
			} else {
				JsonHelper.failure(response);
			}
		} catch (Exception e) {
			JsonHelper.failure(response);
		}

		return null;
	}

}