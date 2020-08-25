	package com.camelot.goodscenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemCategoryDAO;
import com.camelot.goodscenter.dao.ItemMybatisDAO;
import com.camelot.goodscenter.dao.ItemOldDAO;
import com.camelot.goodscenter.dao.ItemPictureDAO;
import com.camelot.goodscenter.domain.ItemCategory;
import com.camelot.goodscenter.domain.ItemPicture;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemOldDTO;
import com.camelot.goodscenter.dto.ItemPictureDTO;
import com.camelot.goodscenter.dto.indto.ItemOldInDTO;
import com.camelot.goodscenter.dto.indto.ItemOldSeachInDTO;
import com.camelot.goodscenter.dto.outdto.ItemOldOutDTO;
import com.camelot.goodscenter.dto.outdto.ItemOldSeachOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemOldExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
@Service("itemOldExportService")
public class ItemOldServiceImpl implements ItemOldExportService {
	private static final Logger LOGGER=LoggerFactory
			.getLogger(ItemOldServiceImpl.class);
	@Resource
	private ItemOldDAO itemOldDAO;
	@Resource
	private ItemPictureDAO itemPictureDAO;
	@Resource
	private ItemMybatisDAO itemMybatisDAO;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ItemCategoryDAO itemCategoryDAO;

	@Override
	public ExecuteResult<String> addItemOld(ItemOldInDTO itemOldInDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			//获取商品ID
			Long itemId = itemMybatisDAO.getItemId();
			ItemOldDTO itemOld = itemOldInDTO.getItemOldDTO();
			itemOld.setItemId(itemId);
			itemOldDAO.add(itemOld);
			for (ItemPictureDTO itemPictureDTO : itemOldInDTO.getItemPictureDTO()) {
				if(StringUtils.isNotBlank(itemPictureDTO.getPictureUrl())){
					itemPictureDTO.setItemId(itemId);
					itemPictureDAO.add(this.PicDTOto(itemPictureDTO));
				}
			}
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error("addItemOld error-----"+e.getMessage());
			throw new RuntimeException();
		}
		return result;
	}
	
	
	@Override
	public ExecuteResult<String> modifyItemOld(ItemOldInDTO itemOldInDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			itemOldDAO.update(itemOldInDTO.getItemOldDTO());
			Long itemId=itemOldInDTO.getItemOldDTO().getItemId();
			itemPictureDAO.deleteItemPicture(itemId);
			for (ItemPictureDTO itemPictureDTO : itemOldInDTO.getItemPictureDTO()) {
				itemPictureDTO.setItemId(itemId);
				itemPictureDAO.add(this.PicDTOto(itemPictureDTO));
			}
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error("modifyItemOld error-----"+e.getMessage());
			throw new RuntimeException();
		}
		return result;
	}
	
	
	@Override
	public ExecuteResult<DataGrid<ItemOldDTO>> queryItemOld(ItemOldDTO itemOldDTO, Pager page) {
		
		ExecuteResult<DataGrid<ItemOldDTO>> result=new ExecuteResult<DataGrid<ItemOldDTO>>();
		
		try {
			DataGrid<ItemOldDTO> dataGrid=new DataGrid<ItemOldDTO>();
			List<ItemOldDTO> list = itemOldDAO.queryList(itemOldDTO, page);
			Long count = itemOldDAO.queryCount(itemOldDTO);
			for (ItemOldDTO iodto : list) {
				List<ItemPicture> pList = itemPictureDAO.queryItemPicsById(iodto.getItemId());
				iodto.setImgUrl(pList.get(0).getPictureUrl());
			}
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error("queryItemOld error-----"+e.getMessage());
			throw new RuntimeException();
		}
		
		return result;
	}
	@Override
	public ExecuteResult<ItemOldOutDTO> getItemOld(Long itemId) {
		ExecuteResult<ItemOldOutDTO> result=new ExecuteResult<ItemOldOutDTO> ();
		
		
		try {
			 ItemOldOutDTO iooDTO=new ItemOldOutDTO();
			 ItemOldDTO itemOldOutDTO = itemOldDAO.queryById(itemId);
			 List<ItemPicture> pList = itemPictureDAO.queryItemPicsById(itemId);
			 //类转换
			 List<ItemPictureDTO> pdtoList=new ArrayList<ItemPictureDTO>();
			 for (ItemPicture itemPicture : pList) {
				ItemPictureDTO dto = this.PictoDTO(itemPicture);
				pdtoList.add(dto);
			}
			 
			 iooDTO.setItemOldDTO(itemOldOutDTO);
			 iooDTO.setItemPictureDTO(pdtoList);
			 result.setResult(iooDTO);
			 result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error("getItemOld error-----"+e.getMessage());
			throw new RuntimeException();
		}
		 
		 
		return result;
	}
	@Override
	public ExecuteResult<String> modifyItemOldStatus(String comment,String platformUserId,Long status, Long... itemId) {
		 ExecuteResult<String> result=new  ExecuteResult<String>();
		try {
			for (int i = 0; i < itemId.length; i++) {
				itemOldDAO.updateStatus(itemId[i],status,comment,platformUserId);
			}
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error("modifyItemOldStatus error-----"+e.getMessage());
			throw new RuntimeException();
		}
		return result;
	}



	@Override
	public ExecuteResult<ItemOldSeachOutDTO> seachItemOld(ItemOldSeachInDTO itemOldSeachInDTO,Pager page) {
			ExecuteResult<ItemOldSeachOutDTO> result=new ExecuteResult<ItemOldSeachOutDTO>();
			
			
			try {
				ItemOldSeachOutDTO iosOutDTO=new ItemOldSeachOutDTO();
				DataGrid<ItemOldDTO> dataGrid=new DataGrid<ItemOldDTO>();
				
				List<Long> catIdSet = new ArrayList<Long>();//类目ID
				Long cid = itemOldSeachInDTO.getCid();
				if(cid!=null){//类目ID 转换为三级类目ID组
					List<ItemCategoryDTO> catsList = this.itemCategoryDAO.queryThirdCatsList(cid);
					for (ItemCategoryDTO itemCategory : catsList) {
						catIdSet.add(itemCategory.getCategoryCid());
					}
				}
				itemOldSeachInDTO.setCids(catIdSet);
				//根据条件查询第一页商品
				List<ItemOldDTO> list=itemOldDAO.querySeachItemOldList(itemOldSeachInDTO, page);
				//根据条件查询所有商品商品
				List<ItemOldDTO> listall=itemOldDAO.querySeachItemOldList(itemOldSeachInDTO, null);
				
				//查询商品图片
				for (ItemOldDTO iodto : list) {
					List<ItemPicture> pList = itemPictureDAO.queryItemPicsById(iodto.getItemId());
					if(pList!=null && pList.size()>0){
						iodto.setImgUrl(pList.get(0).getPictureUrl());
					}
				}
			
				//拼装类目ID查询完整 1 2 3 级类目信息
				Long[] cids =new Long[listall.size()];//类目ID
				for (int i = 0; i < listall.size(); i++) {
					cids[i]=listall.get(i).getCid();
				}
				ExecuteResult<List<ItemCatCascadeDTO>> it = itemCategoryService.queryParentCategoryList(cids);
				
				dataGrid.setRows(list);
				dataGrid.setTotal(Long.valueOf(listall.size()));
				iosOutDTO.setCatList(it.getResult());
				iosOutDTO.setItemOldDTOs(dataGrid);
				result.setResult(iosOutDTO);
				result.setResultMessage("success");
			} catch (Exception e) {
				result.setResultMessage("error");
				result.getErrorMessages().add(e.getMessage());
				LOGGER.error("modifyItemOldStatus error-----"+e.getMessage());
				throw new RuntimeException();
			}
			
			return result;
		
	}

	
	/**
	 * 
	 * <p>Discription:[类转换方法]</p>
	 * Created on 2015-5-5
	 * @param itemPictureDTO
	 * @return
	 * @author:yuht
	 */
	private ItemPicture PicDTOto(ItemPictureDTO itemPictureDTO){
		ItemPicture ip=new ItemPicture();
		ip.setPictureId(itemPictureDTO.getPictureId());//图片id
		ip.setItemId(itemPictureDTO.getItemId());//商品id
		ip.setPictureUrl(itemPictureDTO.getPictureUrl());//图片url
		ip.setSortNumber(itemPictureDTO.getSortNumber());//排序号,排序号最小的作为主图，从1开始
		ip.setPictureStatus(itemPictureDTO.getPictureStatus());//图片状态:1 有效 2： 无效
		ip.setSellerId(itemPictureDTO.getSellerId());//卖家id
		ip.setCreated(itemPictureDTO.getCreated());//创建日期
		ip.setModified(itemPictureDTO.getModified());//修改日期
		ip.setShopId(itemPictureDTO.getShopId());//店铺id
		return ip;
	}
	
	/**
	 * 
	 * <p>Discription:[类转换]</p>
	 * Created on 2015-5-5
	 * @param itemPictureDTO
	 * @return
	 * @author:yuht
	 */
	private ItemPictureDTO PictoDTO(ItemPicture itemPicture){
		ItemPictureDTO ip=new ItemPictureDTO();
		ip.setPictureId(itemPicture.getPictureId());//图片id
		ip.setItemId(itemPicture.getItemId());//商品id
		ip.setPictureUrl(itemPicture.getPictureUrl());//图片url
		ip.setSortNumber(itemPicture.getSortNumber());//排序号,排序号最小的作为主图，从1开始
		ip.setPictureStatus(itemPicture.getPictureStatus());//图片状态:1 有效 2： 无效
		ip.setSellerId(itemPicture.getSellerId());//卖家id
		ip.setCreated(itemPicture.getCreated());//创建日期
		ip.setModified(itemPicture.getModified());//修改日期
		ip.setShopId(itemPicture.getShopId());//店铺id
		return ip;
	}








	

	





	
}
