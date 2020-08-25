package com.camelot.mall.vip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.maketcenter.dto.VipCardDTO;
import com.camelot.maketcenter.service.VipCardService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.util.DateUtil;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [vip卡管理类]</p>
 * Created on 2015年11月17日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/sellcenter/vipCard")
public class VipCardController {
	@Resource
	private VipCardService vipCardService;
	
	@RequestMapping("")
	public String queryVipCardList(Integer page,VipCardDTO vipCard,HttpServletRequest request,Model model){
		//默认显示第一页
		if(page == null || page.intValue() == 0){
			page = 1;
		}
		Long uid = WebUtil.getInstance().getUserId(request);
		Pager<VipCardDTO> pager = new Pager<VipCardDTO>();
		pager.setPage(page);
		if(uid!=null){
			vipCard.setBuyer_id(uid);
		}else{
			return "redirect:/user/login";
		}
		
		//查询vip卡数据集合
		ExecuteResult<DataGrid<VipCardDTO>> result = vipCardService.queryVipCardList(vipCard, pager);
		//查询vip卡总数量
		ExecuteResult<Long> resultCount = vipCardService.queryCountVipCard(vipCard);
		List<VipCardDTO> vipCardDTOList = new ArrayList<VipCardDTO>();
		if(result.isSuccess() && result.getResult() != null && result.getResult().getRows() != null){
			vipCardDTOList = result.getResult().getRows();
		}
		//计算时间差
		for(VipCardDTO vipCardDTO : vipCardDTOList){
			String str=DateUtil.getBetweenTime(new Date(), vipCardDTO.getEnd_data());
			if(Integer.parseInt(str)<0){
				str="0";
			}
	        vipCardDTO.setEffective_time(str);
		}
		pager.setRecords(vipCardDTOList);
		pager.setTotalCount(resultCount.getResult().intValue());
		model.addAttribute("pager", pager);
		return "vipCard/vip_card_list";
	}
	
}
