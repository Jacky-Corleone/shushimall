package com.camelot.mall.integral;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserCreditDTO;
import com.camelot.usercenter.dto.UserCreditLogDTO;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
import com.camelot.usercenter.service.UserCreditExportService;
import com.camelot.usercenter.service.UserCreditLogExportService;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/integralRecord")
public class IntegralRecordController {
	
	@Resource
	private UserCreditExportService userCreditExportService;

	@Resource
	private UserCreditLogExportService userCreditLogExportService;
	@Resource
	private UserIntegralTrajectoryExportService userIntegralTrajectoryService;
	
	@RequestMapping("/init")
	public String initIntegralRecord(HttpServletRequest request, Integer page, Model model) {
		Long userId = WebUtil.getInstance().getUserId(request);
		
		ExecuteResult<UserCreditDTO> userCreditResult= userCreditExportService.getUserCreditByUserId(userId);
		if(userCreditResult.isSuccess()){
			UserCreditDTO userCreditDTO = userCreditResult.getResult();
			Pager<UserCreditLogDTO> pager = new Pager<UserCreditLogDTO>();
			if(userCreditDTO != null){
				//分页对象-pager
				if(null != page){
					pager.setPage(page);
				}
				ExecuteResult<DataGrid<UserCreditLogDTO>> userCreditLogResult = userCreditLogExportService.queryUserCreditLogList(userCreditDTO.getId(), pager);
				DataGrid<UserCreditLogDTO> userCreditLogGrid = userCreditLogResult.getResult();
				pager.setTotalCount(userCreditLogGrid.getTotal().intValue());
				pager.setRecords(userCreditLogGrid.getRows());
				//解决列表为空时，多余的0页
				if (pager.getEndPageIndex() == 0) {	
					pager.setEndPageIndex(pager.getStartPageIndex());
				}
			}
			model.addAttribute("pager", pager);
		} else {
			model.addAttribute("messages", "查询异常!");
		}
		return "/integral/integralRecord";
	}
	@RequestMapping("/list")
	public String listUserIntegral(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO,HttpServletRequest request, Pager page, Model model){
		String type = request.getParameter("type");
		if(null != type && !"".equals(type)){
			userIntegralTrajectoryDTO.setIntegralType(Integer.valueOf(type));
		}
		Long userId = WebUtil.getInstance().getUserId(request);
		userIntegralTrajectoryDTO.setUserId(userId);
		ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> er = userIntegralTrajectoryService.queryUserIntegralByType(userIntegralTrajectoryDTO, page);
		if(er.isSuccess()){
			DataGrid<UserIntegralTrajectoryDTO> dg = er.getResult();
			page.setTotalCount(dg.getTotal().intValue());
			page.setRecords(dg.getRows());
		}
		ExecuteResult<Long> totalIntegral =  userIntegralTrajectoryService.queryTotalIntegral(userId);
		model.addAttribute("totalIntegral",totalIntegral.getResult());
		model.addAttribute("pager", page);
		model.addAttribute("type", type);
		return "/integral/myIntegralList";
	}
	
}
