package com.camelot.goodscenter.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dao.CategoryAttrDAO;
import com.camelot.goodscenter.dao.ItemAttributeDAO;
import com.camelot.goodscenter.dao.ItemAttributeValueDAO;
import com.camelot.goodscenter.dao.ItemBrandDAO;
import com.camelot.goodscenter.dao.ItemCategoryDAO;
import com.camelot.goodscenter.dao.ItemMybatisDAO;
import com.camelot.goodscenter.domain.ItemAttrBean;
import com.camelot.goodscenter.domain.ItemAttrSeller;
import com.camelot.goodscenter.domain.ItemAttrValueBean;
import com.camelot.goodscenter.domain.ItemAttrValueSeller;
import com.camelot.goodscenter.domain.ItemCategoryCascade;
import com.camelot.goodscenter.dto.BrandOfShopDTO;
import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.CategoryAttrDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.outdto.QueryChildCategoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
/**
 * 平台类目列表接口实现
 * @author 周立明
 *
 */
@Service("itemCategoryService")
public class ItemCategoryServiceImpl implements ItemCategoryService{
	private final static Logger logger = LoggerFactory.getLogger(ItemCategoryServiceImpl.class);
	@Resource
	private ItemCategoryDAO itemCategoryDAO;
	@Resource
	private CategoryAttrDAO categoryAttrDAO;
	@Resource
	private ItemMybatisDAO itemMybatisDAO;
	@Resource
	private ItemBrandDAO itemBrandDAO;
	@Resource
	private ItemAttributeDAO itemAttributeDAO;
	@Resource
	private ItemAttributeValueDAO itemAttributeValueDAO;
	
	//平台类目添加功能的实现
	@Override
	public ExecuteResult<String> addItemCategory(ItemCategoryDTO itemCategoryDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			itemCategoryDAO.add(itemCategoryDTO);
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	//平台类目查询所有类目功能的实现
	@Override
	public DataGrid<ItemCategoryDTO> queryItemCategoryAllList(Pager Pager) {
	    DataGrid<ItemCategoryDTO> dataGrid = new DataGrid<ItemCategoryDTO>();
	    
	    try {
			List<ItemCategoryDTO> list= itemCategoryDAO.queryItemCategoryAllList(null,Pager);
			Long count = itemCategoryDAO.queryCount(null);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
		return dataGrid;
	}
    //根据父级id查询平台类目功能的实现
	@Override
	public DataGrid<ItemCategoryDTO> queryItemCategoryList(Long parentCid){
		
		DataGrid<ItemCategoryDTO> dataGrid = new DataGrid<ItemCategoryDTO>();
		
		try {
			ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
			itemCategoryDTO.setCategoryParentCid(parentCid);
			List<ItemCategoryDTO> list= itemCategoryDAO.queryItemCategoryAllList(itemCategoryDTO,null);
			Long count = itemCategoryDAO.queryCount(itemCategoryDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
		return dataGrid;
	}
    //平台类目属性添加(1.向商品属性表中添加商品属性名称并返回属性id2.向商品类别属性表中添加属性类型和类目id)
	@Override
	public ExecuteResult<Long> addCategoryAttr(Long cid, String attrName,
			Integer attrType,int isSenior) {
		ExecuteResult<Long> result = new ExecuteResult<Long>();
		CategoryAttrDTO categoryAttrDTO = new CategoryAttrDTO();
		categoryAttrDTO.setAttrAttrName(attrName);
		categoryAttrDTO.setIsSenior(isSenior);
		try {
			categoryAttrDAO.addAttr(categoryAttrDTO);
			categoryAttrDTO.setAttrCid(cid);
			categoryAttrDTO.setAttrAttrType(attrType);
			categoryAttrDAO.add(categoryAttrDTO);
			result.setResult(categoryAttrDTO.getAttrAttrId());
			result.setResultMessage("添加成功");
		} catch (Exception e) {
			result.setResultMessage("添加失败");
			logger.error("error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
   
    //平台类目属性查询
	@Override
	public DataGrid<CategoryAttrDTO> queryCategoryAttrList(Long cid,
			Integer attrType) {
		DataGrid<CategoryAttrDTO> dataGrid = new DataGrid<CategoryAttrDTO>();
		
		try {
			CategoryAttrDTO dto = new  CategoryAttrDTO();   //定义商品类别属性关系类
			List<CategoryAttrDTO> categoryAttrDTOs = new ArrayList<CategoryAttrDTO>();//属性id及属性值得对象列表
			List<CategoryAttrDTO> attrDTOs = new ArrayList<CategoryAttrDTO>();//属性id及属性值得对象列表
			//很据类目id和商品类型查询属性id和属性值
			dto.setAttrCid(cid);
			dto.setAttrAttrType(attrType);
			categoryAttrDTOs = categoryAttrDAO.queryAttrNameList(dto);
			//遍历属性值列表将每一对值存入DTO列表字段中
			for(int i=0; null!=categoryAttrDTOs && i<categoryAttrDTOs.size(); i++){
				CategoryAttrDTO attrDTO = categoryAttrDTOs.get(i);
				attrDTO.setAttrCid(cid);
				attrDTO.setValueList(categoryAttrDAO.queryValueNameList(attrDTO));
				attrDTOs.add(attrDTO);
			}
			dataGrid.setRows(attrDTOs);
			dataGrid.setTotal(new Long(categoryAttrDTOs.size()));
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
        
		return dataGrid;
	}
    //平台类目属性值添加(1.向商品属性值表中添加属性值名称并返回属性值id2.向商品类目属性值关系表中添加属性id和类目id)
	@Override
	public ExecuteResult<Long> addCategoryAttrValue(Long cid, Long attrId,
			String valueName) {
		ExecuteResult<Long> result = new ExecuteResult<Long>();
		try {
			CategoryAttrDTO categoryAttrDTO = new CategoryAttrDTO();
			categoryAttrDTO.setAttrValueName(valueName);
			categoryAttrDTO.setAttrAttrId(attrId);
			categoryAttrDAO.addAttrValue(categoryAttrDTO);
			categoryAttrDTO.setAttrCid(cid);
			categoryAttrDTO.setAttrAttrId(attrId);
			categoryAttrDAO.addCategoryAttrValue(categoryAttrDTO);
			result.setResult(categoryAttrDTO.getAttrValueId());
			result.setResultMessage("添加成功");
		} catch (Exception e) {
			result.setResultMessage("添加失败");
			logger.error("error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}	
		return result;
	}
	
	@Override
	public ExecuteResult<List<ItemCatCascadeDTO>> queryParentCategoryList(Long... cids) {
		return queryParentCategoryList(3,cids);
	}
	
	@Override
	public ExecuteResult<ItemAttr> addItemAttrSeller(CatAttrSellerDTO inDTO) {
		logger.info("==============开始增加卖家商品类目属性==================");
		ExecuteResult<ItemAttr> result = new ExecuteResult<ItemAttr>();
		try {
			ItemAttr attr = inDTO.getAttr();
			//添加属性表
			ItemAttrBean attrBean = this.addItemAttr(attr);
			attr.setId(attrBean.getId());
			//添加 卖家类目和属性的关系表
			ItemAttrSeller attrSeller = new ItemAttrSeller();
			attrSeller.setAttrId(attrBean.getId());
			attrSeller.setAttrName(attrBean.getName());
			attrSeller.setAttrType(inDTO.getAttrType());
			attrSeller.setCategoryId(inDTO.getCid());
			attrSeller.setSellerId(inDTO.getSellerId());
			attrSeller.setShopId(inDTO.getShopId());
			attrSeller = this.insertItemAttrSeller(attrSeller);
			result.setResult(attr);
		} catch (Exception e) {
			logger.error("执行方法【addItemAttrSeller】报错：{}",e);
			result.addErrorMessage("执行方法【addItemAttrSeller】报错："+e);
			throw new RuntimeException(e);
		} finally{
			logger.info("==============结束增加卖家商品类目属性==================");
		}
		return result;
	}
	
	@Override
	public ExecuteResult<List<ItemAttr>> queryCatAttrSellerList(CatAttrSellerDTO inDTO) {
		logger.info("==============开始查询卖家属性查询==================");
		ExecuteResult<List<ItemAttr>> result = new ExecuteResult<List<ItemAttr>>();
		try {
			if(this.checkNull(inDTO)){
				result.addErrorMessage("参数为空！");
				return result;
			}
			List<ItemAttr> attrs = new ArrayList<ItemAttr>();
			//查询商家属性
			List<ItemAttrSeller> attrRels = this.categoryAttrDAO.queryAttrSellerList(inDTO);
			//循环属性 查询关联的属性值
			ItemAttr attr = null;
			for (ItemAttrSeller attrSeller : attrRels) {
				attr = new ItemAttr();
				attr.setId(attrSeller.getAttrId());
				attr.setName(attrSeller.getAttrName());
				//查询对应属性值查询
				this.setAttrValues(attr,attrSeller);
				attrs.add(attr);
			}
			result.setResult(attrs);
		} catch (Exception e) {
			logger.error("执行方法【queryCatAttrSellerList】报错：{}",e);
			result.addErrorMessage("执行方法【queryCatAttrSellerList】报错："+e);
			throw new RuntimeException(e);
		} finally{
			logger.info("==============结束查询卖家属性查询==================");
		}
		return result;
	}
	
	@Override
	public ExecuteResult<ItemAttrValue> addItemAttrValueSeller(CatAttrSellerDTO inDTO) {
		logger.info("==============开始增加卖家商品类目属性==================");
		ExecuteResult<ItemAttrValue> result = new ExecuteResult<ItemAttrValue>();
		try {
			ItemAttrValue attrValue = inDTO.getAttrValue();
			ItemAttrSeller attrSeller = this.categoryAttrDAO.getItemAttrSeller(inDTO.getSellerId(), inDTO.getShopId(),
					inDTO.getCid(), attrValue.getAttrId());
			if(attrSeller==null){
				result.addErrorMessage("该类目没有属性：属性ID："+attrValue.getAttrId());
				return result;
			}
			
			//添加属性值表
			ItemAttrValueBean attrValBean = this.insertItemAttrValSeller(attrValue);
			//添加商家属性值关联表
			ItemAttrValueSeller attrValSeller = new ItemAttrValueSeller();
			attrValSeller.setSellerAttrId(attrSeller.getSellerAttrId());
			attrValSeller.setValueId(attrValBean.getId());
			this.categoryAttrDAO.insertItemAttrValueSeller(attrValSeller);
			attrValue.setId(attrValBean.getId());
			result.setResult(attrValue);
		} catch (Exception e) {
			logger.error("执行方法【addItemAttrValueSeller】报错：{}",e);
			result.addErrorMessage("执行方法【addItemAttrValueSeller】报错："+e);
			throw new RuntimeException(e);
		} finally{
			logger.info("==============结束增加卖家商品类目属性==================");
		}
		return result;
	}
	
	
	
	
	/**
	 * 
	 * <p>Discription:[拼装]</p>
	 * Created on 2015-3-9
	 * @param dataCats
	 * @return
	 * @author:wangcs
	 */
	private List<ItemCatCascadeDTO> getItemCateCascadeDTO(List<ItemCategoryCascade> dataCats) {
		List<ItemCatCascadeDTO> result = new ArrayList<ItemCatCascadeDTO>();
		ItemCatCascadeDTO firstDTO = null;
		ItemCatCascadeDTO secdDTO = null;
		ItemCatCascadeDTO thirdDTO = null;
		Map<ItemCatCascadeDTO, Map<ItemCatCascadeDTO, List<ItemCatCascadeDTO>>> reMap = new HashMap<ItemCatCascadeDTO, Map<ItemCatCascadeDTO,List<ItemCatCascadeDTO>>>();
		for (ItemCategoryCascade cat : dataCats) {
			firstDTO = new ItemCatCascadeDTO();
			secdDTO = new ItemCatCascadeDTO();
			thirdDTO = new ItemCatCascadeDTO();
			firstDTO.setCid(cat.getCid());
			firstDTO.setCname(cat.getCname());
			secdDTO.setCid(cat.getSecondCatId());
			secdDTO.setCname(cat.getSecondCatName());
			thirdDTO.setCid(cat.getThirdCatId());
			thirdDTO.setCname(cat.getThirdCatName());
			if(!reMap.containsKey(firstDTO)){
				reMap.put(firstDTO, new HashMap<ItemCatCascadeDTO, List<ItemCatCascadeDTO>>());
			}
			this.addSecondCat(reMap.get(firstDTO),secdDTO,thirdDTO);
		}
		result = this.getItemCatList(reMap);
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[从map里解析类目层级对象]</p>
	 * Created on 2015-3-9
	 * @param reMap
	 * @return
	 * @author:wangcs
	 */
	private List<ItemCatCascadeDTO> getItemCatList(
			Map<ItemCatCascadeDTO, Map<ItemCatCascadeDTO, List<ItemCatCascadeDTO>>> reMap) {
		List<ItemCatCascadeDTO> result = new ArrayList<ItemCatCascadeDTO>();
		Iterator<Entry<ItemCatCascadeDTO,Map<ItemCatCascadeDTO,List<ItemCatCascadeDTO>>>> ite = reMap.entrySet().iterator();
		ItemCatCascadeDTO dto = null;
		while(ite.hasNext()){
			Entry<ItemCatCascadeDTO,Map<ItemCatCascadeDTO,List<ItemCatCascadeDTO>>> kv = ite.next();
			dto = kv.getKey();
			dto.setChildCats(this.getSecondCatsFromMap(kv.getValue()));
			result.add(dto);
		}
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[从map里解析二级类目层级对象]</p>
	 * Created on 2015-3-9
	 * @param value
	 * @author:wangcs
	 */
	private List<ItemCatCascadeDTO> getSecondCatsFromMap(Map<ItemCatCascadeDTO, List<ItemCatCascadeDTO>> value) {
		List<ItemCatCascadeDTO> result = new ArrayList<ItemCatCascadeDTO>();
		Iterator<Entry<ItemCatCascadeDTO,List<ItemCatCascadeDTO>>> ite = value.entrySet().iterator();
		ItemCatCascadeDTO dto = null;
		while(ite.hasNext()){
			Entry<ItemCatCascadeDTO,List<ItemCatCascadeDTO>> kv = ite.next();
			dto = kv.getKey();
			dto.setChildCats(kv.getValue());
			result.add(dto);
		}
		return result;
	}
	/**
	 * 
	 * <p>Discription:[拼接二三级类目]</p>
	 * Created on 2015-3-9
	 * @param map
	 * @param secdDTO
	 * @param thirdDTO
	 * @author:wangcs
	 */
	private void addSecondCat(Map<ItemCatCascadeDTO, List<ItemCatCascadeDTO>> map, ItemCatCascadeDTO secdDTO,
			ItemCatCascadeDTO thirdDTO) {
		List<ItemCatCascadeDTO> list = new ArrayList<ItemCatCascadeDTO>();
		if(!map.containsKey(secdDTO)){
			list.add(thirdDTO);
			map.put(secdDTO, list);
		}else {
			if(!map.get(secdDTO).contains(thirdDTO)){
				map.get(secdDTO).add(thirdDTO);
			}
		}
		
	}
	
	
	/**
	 * 
	 * <p>Discription:[向商品属性对象中添加属性值]</p>
	 * Created on 2015-3-11
	 * @param attr
	 * @param attrSeller
	 * @author:wangcs
	 */
	private void setAttrValues(ItemAttr attr, ItemAttrSeller attrSeller) {
		List<ItemAttrValueSeller> vs = this.categoryAttrDAO.queryAttrValueSellerList(attrSeller.getSellerAttrId());
		List<ItemAttrValue> list = new ArrayList<ItemAttrValue>();
		ItemAttrValue iav = null;
		for (ItemAttrValueSeller av : vs) {
			iav = new ItemAttrValue();
			iav.setId(av.getValueId());
			iav.setName(av.getValueName());
			list.add(iav);
		}
		attr.setValues(list);
	}
	/**
	 * 
	 * <p>Discription:[验证空值]</p>
	 * Created on 2015-3-11
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	private boolean checkNull(CatAttrSellerDTO inDTO) {
		if(inDTO==null){
			return true;
		}
		if(inDTO.getAttrType() == null){
			return true;
		}
		if(inDTO.getCid()== null){
			return true;
		}
		if(inDTO.getSellerId() == null){
			return true;
		}
		if(inDTO.getShopId() == null){
			return true;
		}
		return false;
	}
	
	
	
	
	/**
	 * 
	 * <p>Discription:[向数据库中插入item_attr商家属性关联表]</p>
	 * Created on 2015-3-11
	 * @param attrSeller
	 * @return
	 * @author:wangcs
	 */
	private ItemAttrSeller insertItemAttrSeller(ItemAttrSeller attrSeller) {
		this.categoryAttrDAO.insertItemAttrSeller(attrSeller);
		return attrSeller;
	}
	/**
	 * 
	 * <p>Discription:[添加商品属性,插入单表数据]</p>
	 * Created on 2015-3-11
	 * @param attr
	 * @return
	 * @author:wangcs
	 */
	private ItemAttrBean addItemAttr(ItemAttr attr) {
		ItemAttrBean bean = new ItemAttrBean();
		bean.setName(attr.getName());
		this.categoryAttrDAO.insertItemAttr(bean);
		return bean;
	}
	
	/**
	 * 
	 * <p>Discription:[向数据库中插入item_attribute_value商品属性值]</p>
	 * Created on 2015-3-11
	 * @param attrValue
	 * @return
	 * @author:wangcs
	 */
	private ItemAttrValueBean insertItemAttrValSeller(ItemAttrValue attrValue) {
		ItemAttrValueBean bean = new ItemAttrValueBean();
		bean.setAttrId(attrValue.getAttrId());
		bean.setName(attrValue.getName());
		this.categoryAttrDAO.insertItemAttrValue(bean);
		return bean;
	}

	/**
	 * 查询销售属性--销售属性值
	 * @param attr 销售属性：销售属性值；
	 * @return 销售属性--销售属性值
	 */
	@Override
	public ExecuteResult<List<ItemAttr>> queryCatAttrByKeyVals(String attr) {
		ExecuteResult<List<ItemAttr>> result = new ExecuteResult<List<ItemAttr>>();
		List<ItemAttr> resultList = new ArrayList<ItemAttr>();
		List<ItemAttr> newResultList = new ArrayList<ItemAttr>();
		try {
			List<String> attrIds = new ArrayList<String>();	//属性
			List<String> attrValIds = new ArrayList<String>();//属性值
			//
			if(!StringUtils.isBlank(attr)){
				String[] keyVals = attr.split(";");
				String[] strs = null;
				for (String str : keyVals) {
					strs = str.split(",");
					for(String keyVal : strs){
						String[] kvs = keyVal.split(":");
						if(!attrIds.contains(kvs[0])){
							attrIds.add(kvs[0]);
						}
						if(!attrValIds.contains(kvs[1])){
							attrValIds.add(kvs[1]);
						}
					}
					
				}
			}
			if(attrIds.isEmpty()){
				return result;
			}
			//查询所有属性对象
			resultList = this.itemCategoryDAO.queryItemAttrList(attrIds);
			//查询属性值对象
			for (ItemAttr itemAttr : resultList) {
				ItemAttr itemAttrModel = new ItemAttr();
				itemAttrModel.setId(itemAttr.getId());
				itemAttrModel.setName(itemAttr.getName());
				List<ItemAttrValue> valueList = this.itemCategoryDAO.queryItemAttrValueList(itemAttr.getId(), attrValIds);
				itemAttrModel.setValues(valueList);
				itemAttr.setValues(valueList);
				newResultList.add(itemAttrModel);
			}
			result.setResult(newResultList);
		} catch (Exception e) {
			logger.error("执行方法【getItemAttrList】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	

	/**
	 * 查询销售属性--销售属性值copy后的记录
	 * @param attr 销售属性：销售属性值；
	 * @return 复制后的销售属性--销售属性值
	 */
	@Override
	public ExecuteResult<List<ItemAttr>> addCatAttrByKeyValsBak(String attr) {
		ExecuteResult<List<ItemAttr>> attr_result = this.queryCatAttrByKeyVals(attr);
		ExecuteResult<List<ItemAttr>> result = new ExecuteResult<List<ItemAttr>>();
		if(attr_result.isSuccess()){
			List<ItemAttr> new_list = new ArrayList<ItemAttr>();
			List<ItemAttr> query_list = attr_result.getResult();
			//执行copy
			ItemAttr itemAttr = null;
			for (ItemAttr ca : query_list) {
				itemAttr = new ItemAttr();
				itemAttr.setName(ca.getName());
				this.itemAttributeDAO.add(itemAttr);
				List<ItemAttrValue> listav=ca.getValues();
				ItemAttrValue attrValue = null;
				ItemAttrValue dbValue = null;
				List<ItemAttrValue> reValues = new ArrayList<ItemAttrValue>();
				for (int i=0;i<listav.size();i++) {
					dbValue = listav.get(i);
					attrValue = new ItemAttrValue();
					attrValue.setAttrId(itemAttr.getId());
					attrValue.setName(dbValue.getName());
					this.itemAttributeValueDAO.add(attrValue);
					reValues.add(attrValue);
				}
				itemAttr.setValues(reValues);
				new_list.add(itemAttr);
			}
			result.setResult(new_list);
		}else{
			result.setErrorMessages(attr_result.getErrorMessages());
		}
		return result;
	}
	
	@Override
	public ExecuteResult<String> deleteItemCategory(Long cid) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		//有子类目的类目不能删除
		DataGrid<ItemCategoryDTO> childCat =  this.queryItemCategoryList(cid);
		if(childCat.getRows()!=null && childCat.getRows().size()>0){
			result.addErrorMessage("类目存在子类目，不允许删除！");
			return result;
		}
		//类目下有品牌的不能删除
		BrandOfShopDTO brandOfShopDTO = new BrandOfShopDTO();
		brandOfShopDTO.setThirdCid(cid);
		List<ItemBrandDTO>  brands = this.itemBrandDAO.queryItemBrandList(brandOfShopDTO);
		if(brands!=null && brands.size()>0){
			result.addErrorMessage("类目下存在品牌，不允许删除！");
			return result;
		}
		//有商品的类目不能删除
		ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
		itemInDTO.setCid(cid);
		List<ItemQueryOutDTO> items = this.itemMybatisDAO.queryItemList(itemInDTO, null);
		if(items!=null && items.size()>0){
			result.addErrorMessage("类目下存在商品，不允许删除！");
			return result;
		}
		//删除类目
		ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
		itemCategoryDTO.setCategoryCid(cid);
		itemCategoryDTO.setCategoryStatus(2);
		this.itemCategoryDAO.updateBySelect(itemCategoryDTO);
		return result;
	}
	
	@Override
	public ExecuteResult<String> deleteCategoryAttr(Long cid, Long attr_id, Integer attrType) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if (attrType != 1 && attrType != 2) {
			result.addErrorMessage("参数不正确");
		}
		if (cid == null || attr_id == null) {
			result.addErrorMessage("参数不正确");
		}
		if (attrType == 2) {// 如果删除的是平台类目属性，需要判断有没有商品正在使用
			CategoryAttrDTO attrDTO = new CategoryAttrDTO();
			attrDTO.setAttrCid(cid);
			attrDTO.setAttrAttrId(attr_id);
			// 查询该属性的所有属性值
			List<CategoryAttrDTO> attrDTOs = categoryAttrDAO
					.queryValueNameList(attrDTO);
			if (attrDTOs != null && attrDTOs.size() > 0) {
				for (CategoryAttrDTO categoryAttrDTO : attrDTOs) {
					String keyValue = attr_id + ":"
							+ categoryAttrDTO.getAttrValueId();
					List<ItemDTO> itemDTOs = itemMybatisDAO
							.queryItemDTOByAttr(keyValue);
					if (itemDTOs != null && itemDTOs.size() > 0) {
						result.addErrorMessage("有商品正在使用该属性，不能删除");
						return result;
					}
				}
			}
		} 
		// 删除商品类别属性值关系
		categoryAttrDAO.deleteCategoryAttrValueByAttrId(attr_id);
		// 删除商品类别属性关系
		categoryAttrDAO.deleteCategoryAttrByAttrId(attr_id);
		// 删除商品的属性值
		itemAttributeValueDAO.deleteByAttrId(attr_id);
		// 删除商品的属性
		itemAttributeDAO.delete(attr_id);
		result.setResult("删除成功");
		return result;
	}
	
	@Override
	public ExecuteResult<String> deleteCategoryAttrValue(Long cid, Long attr_id, Long value_id, Integer attrType) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if (attrType != 1 && attrType != 2) {
			result.addErrorMessage("参数不正确");
		}
		if (cid == null || attr_id == null || value_id == null) {
			result.addErrorMessage("参数不正确");
		}
		if (attrType == 2) {// 如果删除的是平台类目属性，需要判断有没有商品正在使用
			String keyValue = attr_id + ":" + value_id;
			List<ItemDTO> itemDTOs = itemMybatisDAO
					.queryItemDTOByAttr(keyValue);
			if (itemDTOs != null && itemDTOs.size() > 0) {
				result.addErrorMessage("有商品正在使用该属性值，不能删除");
				return result;
			}
		}
		// 删除商品类别属性值关系
		categoryAttrDAO.deleteCategoryAttrValueByValueId(value_id);
		// 删除属性值
		itemAttributeValueDAO.delete(value_id);
		result.setResult("删除成功");
		return result;
	}
	@Override
	public ExecuteResult<String> updateCategory(
			ItemCategoryDTO itemCategoryDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if(null==itemCategoryDTO.getCategoryCid()){
			result.addErrorMessage("类目id为空，不可以修改该类目");
		}else{
			itemCategoryDAO.updateBySelect(itemCategoryDTO);
		}
		return result;
	}
	@Override
	public ExecuteResult<QueryChildCategoryOutDTO> queryAllChildCategory(ItemCategoryDTO itemCategoryDTO) {
		ExecuteResult<QueryChildCategoryOutDTO> result = new ExecuteResult<QueryChildCategoryOutDTO>();
		if(itemCategoryDTO.getCategoryCid()!=null && itemCategoryDTO.getCategoryCid()>0){
			QueryChildCategoryOutDTO queryChildCategory = categoryAttrDAO.queryChildCategory(itemCategoryDTO);
			result.setResult(queryChildCategory);
		}else{
			result.addErrorMessage("父类目id不能为空！");
		}
		
		return result;
	}
	/**
	 * <p>Discription:[只根据三级类目查询一级类目]</p>
	 * Created on 2015年11月3日
	 * @param cids
	 * @return
	 * @author:[刘喜洋]
	 */
	@Override
	public ExecuteResult<List<ItemCatCascadeDTO>> queryParentCategoryList(Integer lev,Long... cids) {
		ExecuteResult<List<ItemCatCascadeDTO>> result = new ExecuteResult<List<ItemCatCascadeDTO>>();
		if(cids==null || cids.length<=0){
			result.setResult(new ArrayList<ItemCatCascadeDTO>());
			return result;
		}
		//查询数据库中的一二三级类目
		List<ItemCategoryCascade> dataCats = this.itemCategoryDAO.queryParentCats(lev,cids);
		//封装成返回对象
		List<ItemCatCascadeDTO> cats = this.getItemCateCascadeDTO(dataCats);
		result.setResult(cats);
		return result;
	}
	@Override
	public DataGrid<ItemCategoryDTO> queryItemByCategoryLev(Integer categoryLev) {
		DataGrid<ItemCategoryDTO> dataGrid = new DataGrid<ItemCategoryDTO>();
		
		try {
			List<ItemCategoryDTO> list = itemCategoryDAO.queryItemByCategoryLev(categoryLev);
			dataGrid.setRows(list);
			dataGrid.setTotal(Long.decode((null!=list?list.size():0)+""));
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
		return dataGrid;
	
	}
	@Override
	public ExecuteResult<ItemCategoryDTO> queryItemByCategoryById(Long cid) {
		ExecuteResult<ItemCategoryDTO> result = new ExecuteResult<ItemCategoryDTO>();
		if(cid == null){
			result.addErrorMessage("类目id不能为空");
			return result;
		}
		try{
			ItemCategoryDTO dto=itemCategoryDAO.queryById(cid);
			result.setResult(dto);
		}catch(Exception e){
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
		   return result;
	}
	@Override
	public ExecuteResult<List<ItemCategoryDTO>> queryThirdCatsList(Long cid) {
		logger.info("\n 方法[{}]，入参：[{}]", "ItemCategoryServiceImpl-queryThirdCatsList", cid);
		ExecuteResult<List<ItemCategoryDTO>> result = new ExecuteResult<List<ItemCategoryDTO>>();
		try {
			List<ItemCategoryDTO> categoryDTOs = itemCategoryDAO.queryThirdCatsList(cid);
			result.setResult(categoryDTOs);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.info("\n 方法[{}]，异常：[{}]", "ItemCategoryServiceImpl-queryThirdCatsList", e);
		}
		logger.info("\n 方法[{}]，出参：[{}]", "ItemCategoryServiceImpl-queryThirdCatsList", JSONObject.toJSONString(result));
		return result;
	}

}
