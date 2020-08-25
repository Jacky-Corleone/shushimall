package com.camelot.goodscenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemMybatisDAO;
import com.camelot.goodscenter.dao.ItemPriceDAO;
import com.camelot.goodscenter.dao.ItemSkuDAO;
import com.camelot.goodscenter.dao.TradeInventoryDAO;
import com.camelot.goodscenter.domain.ItemSku;
import com.camelot.goodscenter.dto.InventoryModifyDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.SellPrice;
import com.camelot.goodscenter.dto.SkuPictureDTO;
import com.camelot.goodscenter.dto.TradeInventoryDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [库存]</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("tradeInventoryExportService")
public class TradeInventoryServiceImpl implements TradeInventoryExportService {
   
	@Resource
	private TradeInventoryDAO tradeInventoryDAO;
	@Resource
	private ItemPriceDAO itemPriceDAO;
	@Resource
	private ItemMybatisDAO itemMybatisDAO;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ItemSkuDAO itemSkuDAO;
	
	/**
	 * <p>Discription:[根据skuId查询库存]</p>
	 * Created on 2015年3月18日
	 * @param skuId
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<TradeInventoryDTO> queryTradeInventoryBySkuId(Long skuId) {
		ExecuteResult<TradeInventoryDTO> er=new ExecuteResult<TradeInventoryDTO>();
		try{
			er.setResult(tradeInventoryDAO.queryBySkuId(skuId));
			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据条件查询库存列表]</p>
	 * Created on 2015年3月14日
	 * @param dto
	 * @param page
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<DataGrid<TradeInventoryOutDTO>> queryTradeInventoryList(TradeInventoryInDTO dto,Pager page) {
	  ExecuteResult<DataGrid<TradeInventoryOutDTO>> result=new ExecuteResult<DataGrid<TradeInventoryOutDTO>>();
	  DataGrid<TradeInventoryOutDTO> dg=new DataGrid<TradeInventoryOutDTO>();
	  try{
		  List<TradeInventoryOutDTO> list=tradeInventoryDAO.queryTradeInventoryList(dto, page);
		  Long count=tradeInventoryDAO.queryCount(dto);
		  for(TradeInventoryOutDTO out:list){
			 //根据skuId查询商品sku的图片
			List<SkuPictureDTO> pics=itemMybatisDAO.querySkuPics(out.getSkuId());
			out.setSkuPicture(pics);
		    //根据skuId查询sku的销售价
			List<SellPrice> sp=itemPriceDAO.querySkuSellPrices(out.getSkuId());
			out.setAreaPrices(sp);
			//根据skuId查询商品的名称、编码、状态、类目id、市场价以及sku的销售属性集合：keyId:valueId
		  	TradeInventoryOutDTO td=tradeInventoryDAO.queryItemBySkuId(out.getSkuId());
		    out.setItemName(td.getItemName());
		    out.setItemId(td.getItemId());
		    out.setItemStatus(td.getItemStatus());
		    out.setMarketPrice(td.getMarketPrice());
		    out.setSkuStatus(td.getSkuStatus());
		    out.setCid(td.getCid());
		    out.setGuidePrice(td.getGuidePrice());
		    //根据cid查询类目属性
		    ExecuteResult<List<ItemCatCascadeDTO>> er=itemCategoryService.queryParentCategoryList(td.getCid());
		    out.setItemCatCascadeDTO(er.getResult());
		    //根据sku的销售属性keyId:valueId查询商品属性
		 //   System.out.println(td.getAttributes()+"//////");
		    ExecuteResult<List<ItemAttr>> itemAttr=itemCategoryService.queryCatAttrByKeyVals(td.getAttributes());
		    out.setItemAttr(itemAttr.getResult());
		  }
		  dg.setRows(list);
		  dg.setTotal(count);
		  result.setResult(dg);
	  }catch(Exception e){
		  result.setResultMessage("error"); 
		  throw new RuntimeException(e);
	  }
	 return result;
	}
    /**
     *  
     * <p>Discription:[批量修改库存]</p>
     * Created on 2015年3月14日
     * @param dto  
     * @return
     * @author:[杨芳]
     */
	@Override
	 public ExecuteResult<String> modifyInventoryByIds(List<InventoryModifyDTO> dtos){
		ExecuteResult<String> er = new ExecuteResult<String>();
		if (dtos == null || dtos.isEmpty()) {
			er.addErrorMessage("参数不能为空！");
			return er;
		}
		ItemDTO item = null;
		ItemDTO dbItem = null;
		for (InventoryModifyDTO im : dtos) {
			TradeInventoryDTO dbSkuInv = tradeInventoryDAO.queryBySkuId(im.getSkuId());//数据库原库存
			tradeInventoryDAO.modifyInventoryBySkuIds(im.getSkuId(), im.getTotalInventory());
			ItemSku sku = this.itemSkuDAO.queryById(im.getSkuId());
			dbItem = this.itemMybatisDAO.getItemDTOById(sku.getItemId());
			item = new ItemDTO();
			item.setItemId(sku.getItemId());
			//计算商品总库存
			item.setInventory(dbItem.getInventory()-(dbSkuInv.getTotalInventory()-im.getTotalInventory()));
			this.itemMybatisDAO.updateItem(item);
		}
		 return er;
	 }
	
}
