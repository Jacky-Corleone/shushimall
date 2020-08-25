package com.camelot.goodscenter.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemBrandDAO;
import com.camelot.goodscenter.dao.ItemCategoryBrandDAO;
import com.camelot.goodscenter.dao.ItemMybatisDAO;
import com.camelot.goodscenter.domain.ItemBrand;
import com.camelot.goodscenter.domain.ItemCategoryBrand;
import com.camelot.goodscenter.dto.BrandOfShopDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.Pinyin4j;
import com.camelot.openplatform.util.HZSM;

@Service("itemBrandService")
public class ItemBrandExportServiceImpl implements ItemBrandExportService{
	
	private static final Logger logger = LoggerFactory.getLogger(ItemBrandExportServiceImpl.class);
	
	@Resource
	private ItemBrandDAO itemBrandDAO;
	@Resource
	private ItemMybatisDAO itemMybatisDAO;
	@Resource
	private ItemCategoryBrandDAO itemCategoryBrandDAO;


	@Override
	public ExecuteResult<ItemBrandDTO> addItemBrand(ItemBrandDTO itemBrandDTO) {
		ExecuteResult<ItemBrandDTO> result = new ExecuteResult<ItemBrandDTO>();
		try {
			ItemBrand itemBrand = this.getItemBrandFromDTO(itemBrandDTO);
			//校验同一类别下不能存在重名的品牌
			ItemCategoryBrand dbIcb = this.itemCategoryBrandDAO.queryICBByName(itemBrandDTO);
			if(dbIcb != null){
				result.setResult(itemBrandDTO);
				result.addErrorMessage("品牌名称重复不能添加！");
				return result;
			}
			//如果品牌名称不为空 生成首字母
			if(itemBrand.getBrandName()!=null&&!"".equals(itemBrand.getBrandName())){
//				String brandKey = Pinyin4j.makeStringByStringSet(Pinyin4j.getPinyin(itemBrand.getBrandName()));
//				itemBrand.setBrandKey(brandKey.substring(0, 1));
				itemBrand.setBrandKey(HZSM.getWordStart(itemBrand.getBrandName()));
			}
			itemBrandDAO.add(itemBrand);
			//保存类目与商品的关联表信息
			ItemCategoryBrand param = new ItemCategoryBrand();
			param.setSecondLevCid(itemBrandDTO.getSecondLevCid());
			param.setThirdLevCid(itemBrandDTO.getThirdLevCid());
			param.setBrandId(itemBrand.getBrandId());
			this.checkAndAddICB(param);
			itemBrandDTO.setBrandId(itemBrand.getBrandId());
			result.setResult(itemBrandDTO);
//			}
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[从DTO获取ItemBrand对象]</p>
	 * Created on 2015-2-28
	 * @param itemBrandDTO
	 * @return
	 * @author:[创建者中文名字]
	 */
	private ItemBrand getItemBrandFromDTO(ItemBrandDTO itemBrandDTO) {
		ItemBrand brand= new ItemBrand();;
		try {
			brand.setBrandId(itemBrandDTO.getBrandId());
			brand.setBrandLogoUrl(itemBrandDTO.getBrandLogoUrl());
			brand.setBrandName(itemBrandDTO.getBrandName());
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
		return brand;
	}

	@Override
	public ExecuteResult<ItemBrandDTO> modifyItemBrand(ItemBrandDTO itemBrandDTO) {
		ItemBrand itemBrand  = new ItemBrand();
		ExecuteResult<ItemBrandDTO> result = new ExecuteResult<ItemBrandDTO>();
		try {
			itemBrand = this.getItemBrandFromDTO(itemBrandDTO);
			itemBrandDAO.update(itemBrand);
			result.setResult(itemBrandDTO);
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public DataGrid<ItemBrandDTO> queryItemBrandAllList(Pager page) {
		DataGrid<ItemBrandDTO> data = new DataGrid<ItemBrandDTO>();
		try {
			List<ItemBrandDTO> dtos = itemBrandDAO.queryItemBrandAllList(page);
			long count = itemBrandDAO.queryCount(null);
			data.setRows(dtos);
			data.setTotal(count);
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
		return data;
	}

	@Override
	public DataGrid<ItemBrandDTO> queryItemBrandList(BrandOfShopDTO brandOfShopDTO) {
		DataGrid<ItemBrandDTO> data = new DataGrid<ItemBrandDTO>();
		try {
			List<ItemBrandDTO> dtos = itemBrandDAO.queryItemBrandList(brandOfShopDTO);
			//long count = itemBrandDAO.queryCount(null);  
			data.setRows(dtos);
			data.setTotal(Long.valueOf(dtos.size()));
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
		return data;
	}

	@Override
	public ExecuteResult<ItemBrandDTO> addCategoryBrandBatch(ItemBrandDTO itemBrandDTO) {
		ExecuteResult<ItemBrandDTO> result = new ExecuteResult<ItemBrandDTO>();
		try {
			Long[] brandIds = itemBrandDTO.getBrandIds();
			if(brandIds == null){//校验品牌ID必填
				return result;
			}
			ItemCategoryBrand itemCategoryBrand = null;
			for(int i=0;i<brandIds.length;i++){
				itemCategoryBrand = new ItemCategoryBrand();
				itemCategoryBrand.setSecondLevCid(itemBrandDTO.getSecondLevCid());
				itemCategoryBrand.setThirdLevCid(itemBrandDTO.getThirdLevCid());
				itemCategoryBrand.setBrandId(brandIds[i]);
				this.checkAndAddICB(itemCategoryBrand);
			}
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	private void checkAndAddICB(ItemCategoryBrand itemCategoryBrand) {
		try {
			ItemCategoryBrand dbIcb = this.itemCategoryBrandDAO.queryICBByBrandId(itemCategoryBrand);
			if(dbIcb!=null){
				return;
			}else{
				itemCategoryBrandDAO.add(itemCategoryBrand);
			}
		} catch (Exception e) {
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public ExecuteResult<List<ItemBrandDTO>> queryItemBrandByIds(Long... ids) {
		logger.info("============开始根据ID组查询品牌=============");
		ExecuteResult<List<ItemBrandDTO>> result = new ExecuteResult<List<ItemBrandDTO>>();
		try {
			if(ids==null || ids.length<=0){
				return result;
			}
			List<ItemBrandDTO> brands = this.itemBrandDAO.queryBrandByIds(Arrays.asList(ids));
			result.setResult(brands);
		} catch (Exception e) {
			logger.error("执行方法【queryItemBrandByIds】报错：{}",e.getMessage());
			result.addErrorMessage("执行方法【queryItemBrandByIds】报错："+e.getMessage());
			throw new RuntimeException(e);
		} finally{
			logger.info("============结束根据ID组查询品牌=============");
		}
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<ItemBrandDTO>> queryBrandList(ItemBrandDTO itemBrandDTO, Pager page) {
		
		ExecuteResult<DataGrid<ItemBrandDTO>> result=new ExecuteResult<DataGrid<ItemBrandDTO>>();
		
		try {
			DataGrid<ItemBrandDTO> dataGrid=new DataGrid<ItemBrandDTO>();
			List<ItemBrandDTO> list=itemBrandDAO.queryBrandList(itemBrandDTO, page);
			Long count=itemBrandDAO.queryCountBrandList(itemBrandDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error("error:："+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteBrandById(Long brandId) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			ItemQueryInDTO itemInDTO=new ItemQueryInDTO();
			List<Long> brandIdList = new ArrayList<Long>();//品牌ID组
			brandIdList.add(brandId);
			itemInDTO.setBrandIdList(brandIdList);
			 List<ItemQueryOutDTO> listItemDTO = itemMybatisDAO.queryItemList(itemInDTO, null);
			if(listItemDTO.size()==0){
				//Long count=itemCategoryBrandDAO.queryCbByBrandId(brandId);
				//if(count==0){
					itemBrandDAO.updateStatus(brandId);
					result.setResult("1");
					result.setResultMessage("success");
				//}else{
				//	result.setResult("0");
				//	result.setResultMessage("品牌下关联类目");
				//}
		
			}else{
				result.setResult("0");
				result.setResultMessage("品牌下存在商品");
			}
			
		} catch (Exception e) {
			result.setResult("0");
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error("deleteBrandById error====== ："+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	@Override
	public ExecuteResult<String> deleteCategoryBrand(Long cid,Long brandId) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			ItemQueryInDTO itemInDTO=new ItemQueryInDTO();
			List<Long> brandIdList = new ArrayList<Long>();//品牌ID组
			brandIdList.add(brandId);
			itemInDTO.setBrandIdList(brandIdList);
			itemInDTO.setCid(cid);
			List<Integer> itemStatusList = new ArrayList<Integer>();
			itemStatusList.add(1);
			itemStatusList.add(2);
			itemStatusList.add(20);
			itemStatusList.add(3);
			itemStatusList.add(4);
			itemStatusList.add(5);
			itemStatusList.add(6);
			itemStatusList.add(7);
			itemInDTO.setItemStatusList(itemStatusList);
			List<ItemQueryOutDTO> listItemDTO = itemMybatisDAO.queryItemList(itemInDTO, null);
			if(listItemDTO.size()==0){
				//Long count=itemCategoryBrandDAO.queryCbByBrandId(brandId);
				//if(count==0){
					itemBrandDAO.deleteItemBrand(cid,brandId);
					result.setResult("1");
					result.setResultMessage("success");
				//}else{
				//	result.setResult("0");
				//	result.setResultMessage("品牌下关联类目");
				//}
		
			}else{
				result.setResult("0");
				result.setResultMessage("品牌下存在商品");
			}
			
		} catch (Exception e) {
			result.setResult("0");
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error("deleteBrandById error====== ："+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteBrand(Long brandId) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			List<Long> brandIdList = new ArrayList<Long>();//品牌ID组
			brandIdList.add(brandId);
			List<ItemBrandDTO> listItemBrandDTO = itemBrandDAO.queryBrandCategoryById(brandId);
			if(listItemBrandDTO.size()==0){
					itemBrandDAO.updateStatus(brandId);
					result.setResult("1");
					result.setResultMessage("success");
			}else{
				result.setResult("0");
				result.setResultMessage("类目下存在品牌");
			}
			
		} catch (Exception e) {
			result.setResult("0");
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error("deleteBrandById error====== ："+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	} 
}