package com.camelot.mall.sellcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.camelot.usercenter.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.goodscenter.dto.InventoryModifyDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.WebUtil;
/** 
 * <p>Description: [卖家中心-库存管理]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/sellcenter/itemInventory")
public class ItemInventoryController {
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private TradeInventoryExportService tradeInventoryExportService;
	/**
	 * <p>Discription:商品管理-库存列表查询</p>
	 */
	
	@RequestMapping("initItemInventory")
	public String initItemInventory(HttpServletRequest request,TradeInventoryInDTO dto,Integer page,Model model){
		Pager<TradeInventoryOutDTO> pager = new Pager<TradeInventoryOutDTO>();
		if(page == null){
			page = 1;
		}
//		dto.setSellerId(74L);
//		System.out.println("用户ID："+this.getLoginUserId(request));
		//获取用户id
//		dto.setSellerId(this.getLoginUserId(request));
        Long userId = WebUtil.getInstance().getUserId(request);
        UserDTO userDTO = userExportService.queryUserById(userId);
        if(userDTO.getParentId() == null){//主账号id
            dto.setSellerId(userId);
        }else{//子账号id
            dto.setSellerId(userDTO.getParentId());
        }
		pager.setPage(page);
		ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(dto, pager);

		if(dg.getResult() != null){
			pager.setRecords(dg.getResult().getRows());
			pager.setTotalCount(dg.getResult().getTotal().intValue());
		}else{
			pager.setRecords(new ArrayList<TradeInventoryOutDTO>());
			pager.setTotalCount(1);
		}
		model.addAttribute("TradeInventoryInDTO", dto);
		model.addAttribute("pager", pager);
		return "sellcenter/sellgoods/itemInventory";
	}
	
	/**
	 * 批量修改库存
	 * @return
	 */
	@RequestMapping("editInventory")
	@ResponseBody
	public Map<String,Object> editInventory(Long[] ids,Integer[] inventories){
		
		Map<String,Object> result = new HashMap<String,Object>();
		List<InventoryModifyDTO> dto = new ArrayList<InventoryModifyDTO>();
		for(int i= 0;i < ids.length;i++){
			InventoryModifyDTO inventoryModifyDTO = new InventoryModifyDTO();
			inventoryModifyDTO.setSkuId(ids[i]);
			inventoryModifyDTO.setTotalInventory(inventories[i]);
   		    dto.add(inventoryModifyDTO);
		}
		ExecuteResult<String> eResult = tradeInventoryExportService.modifyInventoryByIds(dto);
		eResult.setResultMessage("更新库存成功");
		result.put("messager", eResult.getResultMessage());
		return result;
	}
}
