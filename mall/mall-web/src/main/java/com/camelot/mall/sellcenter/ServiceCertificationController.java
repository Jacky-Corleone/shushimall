package com.camelot.mall.sellcenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.camelot.basecenter.dto.PlatformServiceRuleDTO;
import com.camelot.basecenter.service.PlatformServiceRuleExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationAuditDTO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationPictureDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.dto.userrule.UserPlatformServiceRuleDTO;
import com.camelot.usercenter.enums.UserEnums.FieldIdentificationAuditStatus;
import com.camelot.usercenter.service.FieldIdentificationAuditService;
import com.camelot.usercenter.service.FieldIdentificationPictureService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserPlatformServiceRuleService;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [服务认证]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: @camelotchina.com"></a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/sellcenter/service")
public class ServiceCertificationController {
    private Logger LOG = LoggerFactory.getLogger(ServiceCertificationController.class);

	@Resource
	private PlatformServiceRuleExportService platformServiceRuleExportService;

	@Resource
	private UserPlatformServiceRuleService userPlatformServiceRuleService;

	@Resource
	private UserExportService userExportService;

	@Resource
	private UserExtendsService userExtendsService;

	@Resource
	private FieldIdentificationAuditService auditService;

	@Resource
	private FieldIdentificationPictureService pictureService;

	/**
	 * 实地认证展示
	 * @param request
	 * @param model
	 * @return
	 * @author Klaus
	 */
	@RequestMapping("/fieldIdentification")
	public String fieldIdentification(HttpServletRequest request, Model model){
		//用户信息集合
		UserInfoDTO userInfoDTO = getUserInfoDTO(request);

		//营业执照信息开始------------------------------------------------------------------------------------------------
		UserBusinessDTO userBusinessDTO = userInfoDTO.getUserBusinessDTO();
		model.addAttribute("userBusinessDTO", userBusinessDTO);

		//经营范围
		String businessScope = userBusinessDTO.getBusinessScope();
		boolean endComma = businessScope.endsWith(",");
		//如果逗号结尾
		if(endComma){
			int endCommaIndex = businessScope.lastIndexOf(",");
			businessScope = businessScope.substring(0, endCommaIndex);
		}
		model.addAttribute("businessScope", businessScope);

		//成立日期
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
		Date businessLicenceDate = userBusinessDTO.getBusinessLicenceDate();
		String finalBusinessLicenceDate = sdf.format(businessLicenceDate);
		model.addAttribute("businessLicenceDate", finalBusinessLicenceDate);

		//经营期限
		Date businessLicenceIndate = userBusinessDTO.getBusinessLicenceIndate();
		String finalBusinessLicenceIndate = "";
		if(businessLicenceIndate == null){
			finalBusinessLicenceIndate = "长期有效";
		}else{
			finalBusinessLicenceIndate = sdf.format(businessLicenceIndate);
		}
		model.addAttribute("businessLicenceIndate", finalBusinessLicenceIndate);
		//营业执照信息结束------------------------------------------------------------------------------------------------

		//卖家ID
		Long userId = WebUtil.getInstance().getUserId(request);
		//实地认证审核开始----------------------------------------------------------------------------------------------------
		FieldIdentificationAuditDTO auditDTO = auditService.queryAuditByUserId(userId);
		//提交审核标识
		boolean applyed = false;
		if(null != auditDTO){
			//已提交审核
			applyed = true;
			Integer auditStatus = auditDTO.getAuditStatus();
			model.addAttribute("auditStatus", auditStatus);
			//审核驳回时查看驳回原因
			if(FieldIdentificationAuditStatus.REJECT.getCode() == auditStatus){
				String auditRemark = auditDTO.getAuditRemark();
				model.addAttribute("auditRemark", auditRemark);
			}
		}
		model.addAttribute("applyed", applyed);
		//实地认证审核结束----------------------------------------------------------------------------------------------------

		//实地认证图片开始------------------------------------------------------------------------------------------------------
		//组装图片查询DTO
		FieldIdentificationPictureDTO queryPictureDTO = new FieldIdentificationPictureDTO();
        queryPictureDTO.setUserId(userId);
        queryPictureDTO.setExtendId(userInfoDTO.getExtendId());

        //查询图片集合
        DataGrid<FieldIdentificationPictureDTO> pictureDataGrid = pictureService.findPictureListByCondition(queryPictureDTO, null);
        List<FieldIdentificationPictureDTO> pictureList = pictureDataGrid.getRows();

        //组装图片数组
	    Map<String, FieldIdentificationPictureDTO> pictureMap = new HashMap<String, FieldIdentificationPictureDTO>();
	    FieldIdentificationPictureDTO pictureDTO = new FieldIdentificationPictureDTO();
	    //上传标识
	    boolean uploaded = false;
	    int pictureListSize = pictureList.size();
	    if(null != pictureList && pictureListSize > 0){
	    	//已上传
	    	uploaded = true;
			for(int i=0; i<pictureListSize; i++){
				pictureDTO = pictureList.get(i);
				//将图片DTO放入数组
				pictureMap.put(pictureDTO.getPictureType()+"-"+pictureDTO.getSortNumber(), pictureDTO);
			}
		}
	    model.addAttribute("uploaded", uploaded);
        model.addAttribute("pictureMap", pictureMap);
        //实地认证图片结束------------------------------------------------------------------------------------------------------

		return "sellcenter/serviceCertification/fieldIdentification";
	}

	/**
	 * 提交实地认证
	 * @param request
	 * @return
	 * @author Klaus
	 */
	@RequestMapping("/submitIdentification")
	@ResponseBody
	public Map<String, Object> submitIdentification(HttpServletRequest request){
		Map<String, Object> retMap = new HashMap<String, Object>();
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		//用户信息集合
		UserInfoDTO userInfoDTO = getUserInfoDTO(request);
		//卖家扩展Id
		Long extendId = userInfoDTO.getExtendId();
		//营业执照细信息
		UserBusinessDTO userBusinessDTO = userInfoDTO.getUserBusinessDTO();

		//卖家Id
		Long userId = WebUtil.getInstance().getUserId(request);
		//实地认证审核
		FieldIdentificationAuditDTO auditDTO = auditService.queryAuditByUserId(userId);
		//未提交审核
		if(null == auditDTO){
			auditDTO = new FieldIdentificationAuditDTO();
			auditDTO.setUserId(userId);
			auditDTO.setExtendId(extendId);
			//企业名称
			String companyName = userBusinessDTO.getCompanyName();
			auditDTO.setCompanyName(companyName);
			auditDTO.setAuditStatus(0);
			executeResult = auditService.addAudit(auditDTO);
		//已提交审核
		}else{
			//修改审核状态并清空审核人与审核意见
			executeResult = auditService.modifyAuditStatus(auditDTO.getId(), null, null, FieldIdentificationAuditStatus.UNACCEPT.getCode());
		}
		retMap.put("success", executeResult.isSuccess());
		return retMap;
	}

	/**
	 * 得到用户信息集合
	 * @param request
	 * @return
	 * @author Klaus
	 */
	private UserInfoDTO getUserInfoDTO(HttpServletRequest request) {
		//卖家ID
		Long userId = WebUtil.getInstance().getUserId(request);
		//卖家扩展信息
		ExecuteResult<UserInfoDTO> result = userExtendsService.findUserInfo(userId);
		UserInfoDTO userInfoDTO = result.getResult();
		return userInfoDTO;
    }

	/**
	 *初始化 服务保障计划列表
	 * @return
	 */
	@RequestMapping("initProtecList")
	public String initProtectList(Model model,HttpServletRequest request,Integer page){
		PlatformServiceRuleDTO platformServiceRuleDto = new PlatformServiceRuleDTO();
		Pager<PlatformServiceRuleDTO> pager = new Pager<PlatformServiceRuleDTO>();
		//获取当前登录用户信息
//		String uId = getLoginUserId(request).toString();
		Long uId = WebUtil.getInstance().getUserId(request);
		System.out.println("uid:"+uId);
		if(page == null){
			page = 1;
		}
		pager.setPage(page);
		try {
			//服务信息
			ExecuteResult<DataGrid<PlatformServiceRuleDTO>> list = platformServiceRuleExportService.queryList(platformServiceRuleDto, pager);
//			List<PlatformServiceRuleDTO> platformServiceRuleDtoList = list.getResult().getRows();
            //查询当前登录用户所在店铺的所有用户的id
            String[] uids = getShopUserIdsByCurUserShopId(request);
            //匹配规则
            ExecuteResult<List<UserPlatformServiceRuleDTO>> executeResult = userPlatformServiceRuleService.getUserPlatformRuleList(uids);
            List<UserPlatformServiceRuleDTO> curUserApplyedList = executeResult.getResult();//当前用户申请的认证服务

//			List<UserPlatformServiceRuleDTO> userPlatformServiceRuleDTOList = executeResult.getResult();
			//判断该id用户下是否有该服务规则
			pager.setRecords(list.getResult().getRows());
			pager.setTotalCount(list.getResult().getTotal().intValue());
			model.addAttribute("pager", pager);
			model.addAttribute("platformServiceRuleDTO", list.getResult().getRows());
			model.addAttribute("userPlatformServiceRuleDTO", curUserApplyedList);

		} catch (Exception e) {
			LOG.error("初始化 服务保障计划列表：", e);
		}
		return "sellcenter/serviceCertification/servicesProtect";
	}

    /**
     * 根据当前登录用户的店铺id查询该店铺内所有用户的用户id数组
     * @param request
     * @return
     */
    private String[] getShopUserIdsByCurUserShopId(HttpServletRequest request) {
        Long shopId = WebUtil.getInstance().getShopId(request);
        UserDTO userDTO = new UserDTO();
        userDTO.setShopId(shopId);
        DataGrid<UserDTO> dataGrid = userExportService.findUserListByCondition(userDTO,null,null);
        List<UserDTO> userDTOList = dataGrid.getRows();
        String[] shopUserIds = new String[userDTOList.size()];

        for(int i=0; userDTOList!=null && i<userDTOList.size(); i++){
            UserDTO us = userDTOList.get(i);
            shopUserIds[i] = us.getUid().toString();
        }
        return shopUserIds;
    }

    /**
	 * 服务保障申请
	 * @return
	 */
	@RequestMapping("applyService")
	public String applyService(Long ruleId,String flag,Model model){
		Map<String,Object> result = new HashMap<String,Object>();
		PlatformServiceRuleDTO platformServiceRuleDto = new PlatformServiceRuleDTO();
		Pager<PlatformServiceRuleDTO> page = new Pager<PlatformServiceRuleDTO>();
		try {
			platformServiceRuleDto.setRuleId(ruleId);
			ExecuteResult<DataGrid<PlatformServiceRuleDTO>> list = platformServiceRuleExportService.queryList(platformServiceRuleDto, page);
			List<PlatformServiceRuleDTO> ruleDto = list.getResult().getRows();

			PlatformServiceRuleDTO ruleServiceDto = ruleDto.get(0);
			result.put("ruleServiceDto", ruleServiceDto);
			model.addAttribute("ruleServiceDto",ruleServiceDto);
		} catch (Exception e) {
			// TODO: handle exception
		}
		result.put("messager", "申请成功");
		return "sellcenter/serviceCertification/servicesApply";

	}
	/**
	 * 退出保障
	 * */
	@RequestMapping("applicationHandle")
	@ResponseBody
	public Map<String,Object> applicationHandle(Long ruleId,HttpServletRequest request){

        Map<String,Object> result = new HashMap<String,Object>();
        LOG.info("退出服务认证，规则ID："+ruleId);
        try {
            /**
             * 退出服务认证时，需要考虑到申请服务认证时，可能是店铺中任何一个员工申请的，而退出时也可能是任何一个人操作的，
             * 所以退出服务认证时，需要根据用户id查询用户所在店铺的所有用户id，并退出该店铺内所有用户的改认证服务
             */

            //查询当前登录用户所在店铺的所有用户的id
            String[] uids = getShopUserIdsByCurUserShopId(request);
            for(int i=0; uids!=null && i<uids.length; i++){
                UserPlatformServiceRuleDTO userPlatformServiceRuleDTO = new UserPlatformServiceRuleDTO();
                userPlatformServiceRuleDTO.setUserId(Long.parseLong(uids[i]));
                //状态修改
                userPlatformServiceRuleDTO.setRuleId(ruleId);
                //更新状态
                ExecuteResult<Integer> results = userPlatformServiceRuleService.delUserPlatformService(userPlatformServiceRuleDTO);
                LOG.info(results.getResultMessage());
            }
			result.put("messager", "提交申请成功");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	/**
	 * 保存申请
	 * */
	@RequestMapping("saveApplication")
	@ResponseBody
	public Map<String,Object> saveApplication(Long ruleId,HttpServletRequest request){

		Map<String,Object> result = new HashMap<String,Object>();
		UserPlatformServiceRuleDTO userPlatformServiceRuleDTO = new UserPlatformServiceRuleDTO();

		System.out.println("修改ID："+ruleId);
		try {
//			userPlatformServiceRuleDTO.setUserId(getLoginUserId(request));
			userPlatformServiceRuleDTO.setUserId(WebUtil.getInstance().getUserId(request));
			userPlatformServiceRuleDTO.setRuleId(ruleId);

			//更新状态
			ExecuteResult<UserPlatformServiceRuleDTO> results = userPlatformServiceRuleService.saveUserPlatformServiceRule(userPlatformServiceRuleDTO);
			System.out.println(results.getResultMessage());
			result.put("messager", "提交申请成功");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
