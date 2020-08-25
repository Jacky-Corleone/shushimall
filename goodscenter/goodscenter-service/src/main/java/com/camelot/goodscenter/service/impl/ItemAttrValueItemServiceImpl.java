package com.camelot.goodscenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemAttrValueItemDAO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemAttrValueItemDTO;
import com.camelot.goodscenter.service.ItemAttrValueItemExportService;
import com.camelot.openplatform.common.ExecuteResult;

@Service("itemAttrValueItemExportService")
public class ItemAttrValueItemServiceImpl implements ItemAttrValueItemExportService{
	private static final Logger logger = LoggerFactory.getLogger(ItemAttrValueItemServiceImpl.class);
	@Resource
	private ItemAttrValueItemDAO itemAttrValueItemDAO;
	
	@Override
	public ExecuteResult<String> addItemAttrValueItem(List<ItemAttrValueItemDTO> itemAttrValueItemList) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		
		try {
			for (ItemAttrValueItemDTO itemAttrValueItemDTO : itemAttrValueItemList) {
				itemAttrValueItemDAO.add(itemAttrValueItemDTO);
			}
		} catch (Exception e) {
			logger.error(" addItemAttrValueItem error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<String> modifyItemAttrValueItem(List<ItemAttrValueItemDTO> itemAttrValueItemList) {
		ExecuteResult<String> result=new ExecuteResult<String>();
	
		try {
			itemAttrValueItemDAO.updatestatus(itemAttrValueItemList.get(0).getItemId());
			for (ItemAttrValueItemDTO itemAttrValueItemDTO : itemAttrValueItemList) {
				
				itemAttrValueItemDAO.add(itemAttrValueItemDTO);
			}
		} catch (Exception e) {
			logger.error(" modifyItemAttrValueItem error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<String> deleteItemAttrValueItem(
			Long... valueId) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		
		try {
			for (int i = 0; i < valueId.length; i++) {
				itemAttrValueItemDAO.updatestatusbyValueId(valueId[i]);
			}
		} catch (Exception e) {
			logger.error(" modifyItemAttrValueItem error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<List<ItemAttr>> queryItemAttrValueItem(
			ItemAttrValueItemDTO itemAttrValueItemDTO) {
		ExecuteResult<List<ItemAttr>> result=new ExecuteResult<List<ItemAttr>>();
		try {
		//	List<ItemAttrValueItemDTO> list=itemAttrValueItemDAO.queryList(itemAttrValueItemDTO, null);
			
			List<ItemAttr>  attrlist=itemAttrValueItemDAO.queryAttrList(itemAttrValueItemDTO);
			for (ItemAttr itemAttr : attrlist) {
				itemAttrValueItemDTO.setAttrId(itemAttr.getId());
				List<ItemAttrValue> valueList=itemAttrValueItemDAO.queryValueList(itemAttrValueItemDTO);
				itemAttr.setValues(valueList);
			}
			result.setResult(attrlist);
		} catch (Exception e) {
			logger.error(" modifyItemAttrValueItem error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

}
