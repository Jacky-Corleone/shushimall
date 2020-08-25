package com.camelot.goodscenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dao.ItemMybatisDAO;
import com.camelot.goodscenter.dao.ItemSkuInquiryPriceDAO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.dto.SkuPictureDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuInquiryPriceExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [商品SKU询价的实现]</p>
 * Created on 2015年3月11日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("itemSkuInquiryPriceExportService")
public class ItemSkuInquiryPriceExportServiceImpl implements ItemSkuInquiryPriceExportService {

	@Resource
	private ItemSkuInquiryPriceDAO itemSkuInquiryPriceDAO;
	@Resource
	private ItemMybatisDAO itemMybatisDAO;
	/**
	 * <p>Discription:[根据id查询详情]</p>
	 * Created on 2015年3月28日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
	  public ExecuteResult<ItemSkuInquiryPriceDTO> queryById(Long id){
		  ExecuteResult<ItemSkuInquiryPriceDTO> er=new  ExecuteResult<ItemSkuInquiryPriceDTO>();
		  ItemSkuInquiryPriceDTO sku=itemSkuInquiryPriceDAO.queryById(id);
		  try{
			  if(sku != null){
				  er.setResult(sku); 
				  er.setResultMessage("success");
			  }else{
				 er.setResultMessage("您要查询的数据不存在"); 
			  }
		  }catch(Exception e){
			  er.setResultMessage(e.getMessage());
			  throw new RuntimeException(e);
		  }
		  return er;
	  }
	/**
	 * <p>Discription:[商品SKU询价的列表查询]</p>
	 * Created on 2015年3月11日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>> queryList(ItemSkuInquiryPriceDTO dto,Pager page) {
		ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>> er=new ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>>();
		DataGrid<ItemSkuInquiryPriceDTO> dg=new DataGrid<ItemSkuInquiryPriceDTO>();
		List<ItemSkuInquiryPriceDTO> list=itemSkuInquiryPriceDAO.queryList(dto, page);
		Long count=itemSkuInquiryPriceDAO.queryCount(dto);
		try{
			if(list != null ){
				for(ItemSkuInquiryPriceDTO i:list){
					//根据skuId查询sku图片
					List<SkuPictureDTO> p=itemMybatisDAO.querySkuPics(i.getSkuId());
					i.setPictureUrl(p.get(0).getPicUrl());
				}
				dg.setRows(list); 
				dg.setTotal(count);
				er.setResult(dg);
			}else{
				er.setResultMessage("要查询的数据不存在");
			}
		
		er.setResultMessage("success");
		}catch(Exception e){
		   er.setResultMessage("error");
		   throw new RuntimeException(e);
		}
		return er;
	}
	 /**
	    * <p>Discription:[发起询价]</p>
	    * Created on 2015年3月12日
	    * @param dto
	    * @return
	    * @author:[杨芳]
	    */
	@Override
   public ExecuteResult<String> addItemSkuInquiryPrice(ItemSkuInquiryPriceDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		try{
			if(dto.getSkuId()==null || dto.getSkuId().equals(0L)){
				er.setResultMessage("skuId不能为空");
				return er;
			}
			if(dto.getBuyerId()==null || dto.getBuyerId().equals(0L)){
				er.setResultMessage("买家id不能为空");
				return er;
			}
			if(dto.getSellerId()==null || dto.getSellerId().equals(0L)){
				er.setResultMessage("卖家id不能为空");
				return er;
			}
			if(dto.getShopId()==null || dto.getShopId().equals(0L)){
				er.setResultMessage("店铺id不能为空");
				return er;
			}
			if(dto.getItemId()==null || dto.getItemId().equals(0L)){
				er.setResultMessage("商品id不能为空");
				return er;
			}
			if(dto.getInquiryQty()==null || dto.getInquiryQty().equals(0L)){
				er.setResultMessage("询价数量不能为空");
				return er;
			}
			if(dto.getCellphone()==null || dto.getCellphone().equals("")){
				er.setResultMessage("联系方式不能为空");
				return er;
			}
			itemSkuInquiryPriceDAO.add(dto);
			er.setResult("success");
		}catch(Exception e){
			er.setResult("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	    * <p>Discription:[询价修改]</p>
	    * Created on 2015年3月12日
	    * @param dto
	    * @return
	    * @author:[杨芳]
	    */
	@Override
	public ExecuteResult<String> modifyItemSkuInquiryPrice(ItemSkuInquiryPriceDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		ItemSkuInquiryPriceDTO itemSkuInquiryPriceDTO=itemSkuInquiryPriceDAO.queryById(dto.getId());
		try{
		  if(itemSkuInquiryPriceDTO != null){
			  if(dto.getInquiryPrice()!=null){
				  itemSkuInquiryPriceDTO.setInquiryPrice(dto.getInquiryPrice());  
			  }
			  if(dto.getStartTime() != null){
				  itemSkuInquiryPriceDTO.setStartTime(dto.getStartTime());
			  }
			  if(dto.getEndTime() != null){
				  itemSkuInquiryPriceDTO.setEndTime(dto.getEndTime()); 
			  }
			  if(dto.getInquiryRemark().trim() != null){
				  itemSkuInquiryPriceDTO.setInquiryRemark(dto.getInquiryRemark().trim()); 
			  }
			  if(itemSkuInquiryPriceDAO.updateBySelect(itemSkuInquiryPriceDTO)>0){
				  er.setResult("修改成功");
			  }else{
				  er.setResult("修改失败");
			  }
		  }
		}catch(Exception e){
			er.setResult("error");
			throw new RuntimeException(e);
		}
		
		return er;
	}
	/**
		* <p>Discription:[根据买家id、卖家id、商品id、店铺id、sku号]</p>
		* Created on 2015年3月18日
		* @param dto
		* @return
		* @author:[杨芳]
		*/
	public ExecuteResult<ItemSkuInquiryPriceDTO> queryByIdsAndNumber(ItemSkuInquiryPriceDTO dto) {
		ExecuteResult<ItemSkuInquiryPriceDTO> er = new ExecuteResult<ItemSkuInquiryPriceDTO>();
		try {
			if (dto != null) {
				if (dto.getBuyerId() == null || dto.getBuyerId().equals(0L)) {
					er.setResultMessage("买家id不能为空");
					return er;
				}
				if (dto.getSellerId() == null || dto.getSellerId().equals(0L)) {
					er.setResultMessage("卖家id不能为空");
					return er;
				}
				if (dto.getSkuId() == null || dto.getSkuId().equals(0L)) {
					er.setResultMessage("skuId不能为空");
					return er;
				}
				if (dto.getItemId() == null || dto.getItemId().equals(0L)) {
					er.setResultMessage("商品id不能为空");
					return er;
				}
				ItemSkuInquiryPriceDTO itemSkuInquiryPriceDTO = itemSkuInquiryPriceDAO.selectByIdsAndNumber(dto);
				List<SkuPictureDTO> pics = this.itemMybatisDAO.querySkuPics(dto.getSkuId());
				if (pics != null && pics.size() > 0 && itemSkuInquiryPriceDTO != null) {
					itemSkuInquiryPriceDTO.setPictureUrl(pics.get(0).getPicUrl());
					itemSkuInquiryPriceDTO.setSkuPics(pics);
				}
				er.setResult(itemSkuInquiryPriceDTO);
				er.setResultMessage("success");
			}
		} catch (Exception e) {
			er.setResultMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}
}
