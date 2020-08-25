package com.camelot.goodscenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dao.ItemAttributeDAO;
import com.camelot.goodscenter.dao.ItemAttributeValueDAO;
import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.CategoryAttrDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.service.ItemAttributeExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;

@Service("itemAttributeExportService")
public class ItemAttributeExportServiceImpl implements ItemAttributeExportService {

	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private ItemAttributeDAO itemAttributeDAO;
	@Resource
	private ItemAttributeValueDAO itemAttributeValueDAO;
	
	@Resource
	private ItemCategoryService itemCategoryService;
	
	@Override
	public ExecuteResult<ItemAttr> addItemAttribute(ItemAttr itemAttr) {
		ExecuteResult<ItemAttr> result = new ExecuteResult<ItemAttr>();
		if(StringUtils.isBlank(itemAttr.getName())){
			result.addErrorMessage("属性名不能为空！");
			return result;
		}
		this.itemAttributeDAO.add(itemAttr);
		result.setResult(itemAttr);
		return result;
	}

	@Override
	public ExecuteResult<ItemAttr> deleteItemAttribute(ItemAttr itemAttr) {
		ExecuteResult<ItemAttr> result = new ExecuteResult<ItemAttr>();
		ItemAttr param = new ItemAttr();
		param.setId(itemAttr.getId());
		param.setStatus(2);
		this.itemAttributeDAO.update(param);
		result.setResult(itemAttr);
		return result;
	}

	@Override
	public ExecuteResult<ItemAttr> modifyItemAttribute(ItemAttr itemAttr) {
		ExecuteResult<ItemAttr> result = new ExecuteResult<ItemAttr>();
		if(itemAttr.getId() == null){
			result.addErrorMessage("属性ID不能为空！");
			return result;
		}
		if(StringUtils.isBlank(itemAttr.getName())){
			result.addErrorMessage("属性名不能为空！");
			return result;
		}
		this.itemAttributeDAO.update(itemAttr);
		result.setResult(itemAttr);
		return result;
	}

	@Override
	public ExecuteResult<ItemAttrValue> addItemAttrValue(ItemAttrValue itemAttrValue) {
		ExecuteResult<ItemAttrValue> result = new ExecuteResult<ItemAttrValue>();
		if(itemAttrValue.getAttrId()==null){
			result.addErrorMessage("属性ID不能为空！");
			return result;
		}
		if(StringUtils.isBlank(itemAttrValue.getName())){
			result.addErrorMessage("属性值名称不能为空！");
			return result;
		}
		this.itemAttributeValueDAO.add(itemAttrValue);
		result.setResult(itemAttrValue);
		return result;
	}

	@Override
	public ExecuteResult<ItemAttrValue> deleteItemAttrValue(ItemAttrValue itemAttrValue) {
		ExecuteResult<ItemAttrValue> result = new ExecuteResult<ItemAttrValue>();
		ItemAttrValue param = new ItemAttrValue();
		param.setId(itemAttrValue.getId());
		param.setStatus(2);
		this.itemAttributeValueDAO.update(param);
		return result;
	}

	@Override
	public ExecuteResult<ItemAttrValue> modifyItemAttrValue(ItemAttrValue itemAttrValue) {
		ExecuteResult<ItemAttrValue> result = new ExecuteResult<ItemAttrValue>();
		if(itemAttrValue.getId() == null){
			result.addErrorMessage("属性值ID不能为空！");
			return result;
		}
		if(StringUtils.isBlank(itemAttrValue.getName())){
			result.addErrorMessage("属性值名称不能为空！");
			return result;
		}
		ItemAttrValue param = new ItemAttrValue();
		param.setId(itemAttrValue.getId());
		param.setName(itemAttrValue.getName());
		this.itemAttributeValueDAO.update(param);
		return result;
	}

	@Override
	public ExecuteResult<List<ItemAttr>> addItemAttrValueBack(CatAttrSellerDTO inDTO,int operator) {
		ExecuteResult<List<ItemAttr>>  result=new ExecuteResult<List<ItemAttr>> ();
		DataGrid<CategoryAttrDTO> dataGridca = new DataGrid<CategoryAttrDTO>();
		List<ItemAttr> list=new ArrayList<ItemAttr>();
		if(operator==1){//商家
			ExecuteResult<List<ItemAttr>> catRe = itemCategoryService.queryCatAttrSellerList(inDTO);
			if(!catRe.isSuccess()){
				return catRe;
			}
			List<ItemAttr> dbAttrs = catRe.getResult();
			ItemAttr attr = null;
			for (ItemAttr ca : dbAttrs) {
				attr = new ItemAttr();
				attr.setName(ca.getName());
				this.itemAttributeDAO.add(attr);
				List<ItemAttrValue> listav=ca.getValues();
				ItemAttrValue attrValue = null;
				ItemAttrValue dbValue = null;
				List<ItemAttrValue> reValues = new ArrayList<ItemAttrValue>();
				for (int i=0;i<listav.size();i++) {
					dbValue = listav.get(i);
					attrValue = new ItemAttrValue();
					attrValue.setAttrId(attr.getId());
					attrValue.setName(dbValue.getName());
					this.itemAttributeValueDAO.add(attrValue);
					reValues.add(attrValue);
				}
				attr.setValues(reValues);
				list.add(attr);
			}
		}else if(operator==2){//平台
			dataGridca = itemCategoryService.queryCategoryAttrList(inDTO.getCid(), inDTO.getAttrType());
			for (CategoryAttrDTO ca : dataGridca.getRows()) {
				ItemAttr itemAttr=new ItemAttr();
				itemAttr.setName(ca.getAttrAttrName());
				this.itemAttributeDAO.add(itemAttr);
				
				List<ItemAttrValue> listav=new ArrayList<ItemAttrValue>();
				for (CategoryAttrDTO cavalue: ca.getValueList()) {
					ItemAttrValue itemAttrValue=new ItemAttrValue();
					itemAttrValue.setName(cavalue.getAttrValueName());
					itemAttrValue.setAttrId(itemAttr.getId());
					this.itemAttributeValueDAO.add(itemAttrValue);
					listav.add(itemAttrValue);
				}
				itemAttr.setValues(listav);
				list.add(itemAttr);
			}
		}
		
		
		
		result.setResult(list);
		return result;
	}

}
