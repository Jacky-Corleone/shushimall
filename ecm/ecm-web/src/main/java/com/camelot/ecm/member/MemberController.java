package com.camelot.ecm.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.VipLevelEnum;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserGrowthDTO;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserGrowthService;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * <p>
 * Description: [用户管理类]
 * </p>
 * Created on 2015年12月9日
 * 
 * @author <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("${adminPath}/member")
public class MemberController extends BaseController {
	@Resource
	private UserExportService userExportService;

	@Resource
	private UserIntegralTrajectoryExportService userIntegralTrajectoryService;
	
	@Resource
	private UserGrowthService userGrowthService;
	/**
	 * 会员信息查询
	 * 
	 * <p>
	 * Discription:[会员信息查询：会员编号、会员的用户名、手机号、注册时间、会员等级、会员成长值、会员积分、操作（查看详情）]
	 * </p>
	 * Created on 2015年12月9日
	 * 
	 * @param userDTO
	 * @param model
	 * @return
	 * @author:[顾雨]
	 */
	@RequestMapping("/list")
	public ModelAndView list(UserDTO userDTO, Pager<UserDTO> pager, Model model) {

		DataGrid<UserDTO> users = userExportService.findUserListByCondition(userDTO, null, pager);
		Page<UserDTO> page = new Page<UserDTO>(pager.getPage(), pager.getRows(), users.getTotal(), users.getRows());
		for (UserDTO user : page.getList()) {
			// 转换会员等级
			user.setVipLevel(evalVipLevel(Integer.parseInt(user.getVipLevel())));
			// 查询并设置会员积分总和
			user.setTotalIntegral(userIntegralTrajectoryService.queryTotalIntegral(user.getUid()).getResult());
		}

		model.addAttribute("page", page);

		return new ModelAndView("member/list");
	}
	

	
	/**
	 * 会员积分明细
	 * <p>Discription:[获取会员积分列表：积分变化、日期、来源]</p>
	 * Created on 2015年12月16日
	 * @param userIntegralTrajectoryDTO 表单封装DTO
	 * @param pager 页面封装对象
	 * @param model
	 * @return
	 * @author [顾雨]
	 */
	@RequestMapping("/integralList")
	public ModelAndView integralList(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO, Pager<UserIntegralTrajectoryDTO> pager, Model model){
		ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> er = userIntegralTrajectoryService.queryUserIntegralByType(userIntegralTrajectoryDTO, pager);
		Page<UserIntegralTrajectoryDTO> page = new Page<UserIntegralTrajectoryDTO>(pager.getPage(), pager.getRows());
		if(er.isSuccess()) {
			DataGrid<UserIntegralTrajectoryDTO> dg = er.getResult();
			page.setCount(dg.getTotal());
			page.setList(dg.getRows());
		}
		model.addAttribute("page", page);
		model.addAttribute("type", userIntegralTrajectoryDTO.getIntegralType());
		
		return new ModelAndView("member/integralList");
	}
	
	/**
	 * 会员成长值明细
	 * <p>Discription:[获取会员积分列表：获取方式、获取成长值、获取时间]</p>
	 * Created on 2015年12月16日
	 * @param userDTO 表单封装DTO
	 * @param pager 页面封装对象
	 * @param model
	 * @return
	 * @author [顾雨]
	 */
	@RequestMapping("/growthValueList")
	public ModelAndView growthValueList(UserDTO userDTO, Pager<UserDTO> pager, Model model){

		DataGrid<UserDTO> users = userExportService.findUserListByCondition(userDTO, null, new Pager());
		UserDTO user = users.getRows().get(0);
		user.setVipLevel(VipLevelEnum.getNameStrByIdStr(user.getVipLevel()));
		model.addAttribute("user", user);
		
		Page<UserGrowthDTO> page = new Page<UserGrowthDTO>(pager.getPage(), pager.getRows());
		ExecuteResult<DataGrid<UserGrowthDTO>> userGrowthResult = userGrowthService.selectList(userDTO.getUid(), pager);
		if(userGrowthResult.isSuccess()) {
			DataGrid<UserGrowthDTO> dg = userGrowthResult.getResult();
			page.setCount(dg.getTotal());
			page.setList(dg.getRows());
		}
		model.addAttribute("page", page);
		
		return new ModelAndView("member/growthValueList");
	}

	/**
	 * 转换会员等级
	 * 
	 * <p>
	 * Discription: [ 根据会员等级数值转换成字符 普通会员：1 铜牌会员：2 银牌会员：3
	 * 金牌会员：4 钻石会员：5 ]
	 * </p>
	 * Created on 2015年12月9日
	 * 
	 * @param growthValue
	 * @return
	 * @author:[顾雨]
	 */
	private String evalVipLevel(Integer vipLevelId) {
		return VipLevelEnum.getEnumById(vipLevelId).getName();
	}
}
