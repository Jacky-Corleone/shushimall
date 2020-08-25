package com.camelot.mall.growth;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.VipLevelEnum;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserGrowthDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserGrowthService;
import com.camelot.util.WebUtil;

/**
 * 
 * <p>Description: [成长值Controller]</p>
 * Created on 2015-12-9
 * @author  <a href="mailto: xxx@camelotchina.com">化亚会</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/growth")
public class GrowthValueController {
	
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private UserExportService userExportService;    //用户信息的接口
	
	@Resource
	private UserGrowthService userGrowthService;  //成长值记录接口
	
	/**
	 * 
	 * <p>Discription:[查询成长记录和用户信息]</p>
	 * Created on 2015-12-10
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 * @author:[化亚会]
	 */
	@RequestMapping("/growthList")
	public String growthList(HttpServletRequest request, Model model,Integer page,String toPage){
		//获取用户id
		long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDto = userExportService.queryUserByfId(userId);
		if (userDto.getGrowthValue() == null) {
			userDto.setGrowthValue(BigDecimal.ZERO);
		}
		//获取会员等级
		VipLevelEnum vip = VipLevelEnum.getVipLevelEnumByGrowthValue(userDto.getGrowthValue());
		if(vip!=null){
			userDto.setVipLevel(vip.getName());
		}
		if(userDto!=null){
			Pager<UserGrowthDTO> pager = new Pager<UserGrowthDTO>();
			//分页对象-pager
			if(null != page){
				pager.setPage(page);
			}
			ExecuteResult<DataGrid<UserGrowthDTO>> userGrowthResult = userGrowthService.selectList(userId, pager);
			DataGrid<UserGrowthDTO> userGrowthGrid = userGrowthResult.getResult();
			pager.setTotalCount(userGrowthGrid.getTotal().intValue());
			pager.setRecords(userGrowthGrid.getRows());
			//解决列表为空时，多余的0页
			if (pager.getEndPageIndex() == 0) {	
				pager.setEndPageIndex(pager.getStartPageIndex());
			}	
			model.addAttribute("pager", pager);
			model.addAttribute("user",userDto);
		} else {
			model.addAttribute("messages", "查询异常!");
		}
		if(toPage!=null){
			return "/growthValue/growthListPage";
		}
		return "/growthValue/growthListView";
	}
	

}
