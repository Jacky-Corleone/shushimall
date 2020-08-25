package com.camelot.ecm.fieldIdentification.audit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationAuditDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.enums.UserEnums.FieldIdentificationAuditStatus;
import com.camelot.usercenter.service.FieldIdentificationAuditService;
import com.camelot.usercenter.service.UserExtendsService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 实地认证审核控制层
 * @author Klaus
 */
@Controller
@RequestMapping(value = "${adminPath}/fieldIdentificationAudit")
public class FieldIdentificationAuditController extends BaseController {

	@Resource
	private FieldIdentificationAuditService auditService;

	@Resource
	private UserExtendsService userExtendsService;

	/**
	 * 展示未受理审核列表
	 * @param auditDTO
	 * @param pager
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "unAccept")
	public String listUnAcceptAudit(@ModelAttribute("auditDTO")FieldIdentificationAuditDTO auditDTO, Pager<FieldIdentificationAuditDTO> pager, Model model) {
        DataGrid<FieldIdentificationAuditDTO> dataGrid = auditService.findAuditListByCondition(auditDTO, FieldIdentificationAuditStatus.UNACCEPT, pager);
        Page<FieldIdentificationAuditDTO> page = new Page<FieldIdentificationAuditDTO>();
        page.setCount(dataGrid.getTotal());
        page.setList(dataGrid.getRows());
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        model.addAttribute("page", page);
        model.addAttribute("auditDTO", auditDTO);
		return "fieldIdentification/unAcceptFieldIdentificationAudit";
	}

	/**
	 * 展示已受理审核列表
	 * @param auditDTO
	 * @param pager
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "accepted")
	public String listAcceptedAudit(@ModelAttribute("auditDTO")FieldIdentificationAuditDTO auditDTO, Pager<FieldIdentificationAuditDTO> pager, Model model) {
        DataGrid<FieldIdentificationAuditDTO> authDataGrid = new DataGrid<FieldIdentificationAuditDTO>();
        DataGrid<FieldIdentificationAuditDTO> passDataGrid = new DataGrid<FieldIdentificationAuditDTO>();
        DataGrid<FieldIdentificationAuditDTO> rejectDataGrid = new DataGrid<FieldIdentificationAuditDTO>();
        DataGrid<FieldIdentificationAuditDTO> acceptedDataGrid = new DataGrid<FieldIdentificationAuditDTO>();
        Long count = 0L;
        List<FieldIdentificationAuditDTO> list = new ArrayList<FieldIdentificationAuditDTO>();
        if(null == auditDTO.getAuditStatus()){
        	acceptedDataGrid = auditService.findAuditListByCondition(auditDTO, FieldIdentificationAuditStatus.ACCEPTED, pager);
        	count = acceptedDataGrid.getTotal();
        	list.addAll(acceptedDataGrid.getRows());
        }else{
        	Integer auditStatus = auditDTO.getAuditStatus();
        	switch(auditStatus)
        	{
        		case 1:
        			authDataGrid = auditService.findAuditListByCondition(auditDTO, UserEnums.FieldIdentificationAuditStatus.AUTH, pager);
        			count = authDataGrid.getTotal();
        			list.addAll(authDataGrid.getRows());
        			break;
        		case 2:
        			passDataGrid = auditService.findAuditListByCondition(auditDTO, UserEnums.FieldIdentificationAuditStatus.PASS, pager);
        			count = passDataGrid.getTotal();
        			list.addAll(passDataGrid.getRows());
        			break;
        		case 3:
        			rejectDataGrid = auditService.findAuditListByCondition(auditDTO, UserEnums.FieldIdentificationAuditStatus.REJECT, pager);
        			count = rejectDataGrid.getTotal();
        			list.addAll(rejectDataGrid.getRows());
        			break;
        	}
        }
        Page<FieldIdentificationAuditDTO> page = new Page<FieldIdentificationAuditDTO>();
        page.setCount(count);
        page.setList(list);
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        model.addAttribute("page", page);
        model.addAttribute("allStatus",UserEnums.FieldIdentificationAuditStatus.values());
		return "fieldIdentification/acceptedFieldIdentificationAudit";
	}

	/**
	 * 修改审核状态
	 * @param primaryKey
	 * @param auditRemark
	 * @param auditStatus
	 * @return
	 */
	@RequestMapping(value = "modifyAuditStatus")
	@ResponseBody
    public Map<String, Object> modifyAuditStatus(Long primaryKey, String auditRemark, int auditStatus){
		Map<String, Object> map = new HashMap<String, Object>();
		String auditorId = UserUtils.getUser().getId();
		ExecuteResult<String> executeResult = auditService.modifyAuditStatus(primaryKey, auditorId, auditRemark, auditStatus);
		map.put("success",executeResult.isSuccess());
        map.put("auditStatus",auditStatus);
        return map;
    }

	/**
	 * 查看卖家审核提交信息
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "showAuditDetail")
	public String showAuditDetail(Long userId, Model model){
		UserBusinessDTO userBusinessDTO = getUserInfoDTO(userId);
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
		return "fieldIdentification/fieldIdentificationDetail";
	}

	/**
	 * 得到营业执照信息
	 * @param userId
	 * @return
	 */
	private UserBusinessDTO getUserInfoDTO(Long userId) {
		ExecuteResult<UserInfoDTO> result = userExtendsService.findUserInfo(userId);
		UserInfoDTO userInfoDTO = result.getResult();
		UserBusinessDTO userBusinessDTO = userInfoDTO.getUserBusinessDTO();
		return userBusinessDTO;
    }

}

