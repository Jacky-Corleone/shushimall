package com.camelot.goodscenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dao.ItemAttributeDAO;
import com.camelot.goodscenter.dao.ItemAttributeValueDAO;
import com.camelot.goodscenter.dao.ItemCategoryDAO;
import com.camelot.goodscenter.dao.ItemMybatisDAO;
import com.camelot.goodscenter.dao.ItemPictureDAO;
import com.camelot.goodscenter.dao.ItemPriceDAO;
import com.camelot.goodscenter.dao.ItemSkuDAO;
import com.camelot.goodscenter.dao.ItemSkuPackageDAO;
import com.camelot.goodscenter.dao.TradeInventoryDAO;
import com.camelot.goodscenter.domain.Item;
import com.camelot.goodscenter.domain.ItemPicture;
import com.camelot.goodscenter.domain.ItemPrice;
import com.camelot.goodscenter.domain.ItemSku;
import com.camelot.goodscenter.domain.ItemSkuPicture;
import com.camelot.goodscenter.domain.PriceQueryParam;
import com.camelot.goodscenter.domain.TradeInventory;
import com.camelot.goodscenter.domain.TradeSkuPrice;
import com.camelot.goodscenter.dto.ItemAdDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemAttrValueItemDTO;
import com.camelot.goodscenter.dto.ItemBaseDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemShopCidDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.dto.SellPrice;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.SkuPictureDTO;
import com.camelot.goodscenter.dto.TradeInventoryDTO;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.dto.enums.ItemPlatLinkStatusEnum;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemAttrValueItemExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemPriceService;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.goodscenter.service.utill.ItemDTOToDomainUtil;
import com.camelot.openplatform.common.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("itemExportService")
public class ItemExportServiceImpl implements ItemExportService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ItemExportServiceImpl.class);
	@Resource
	private ItemMybatisDAO itemMybatisDAO;
	@Resource
	private ItemPictureDAO itemPictureDAO;
	@Resource
	private ItemPriceDAO itemPriceDAO;
	@Resource
	private ItemPriceService itemPriceService;
	@Resource
	private ItemSkuDAO itemSkuDAO;
	@Resource
	private TradeInventoryDAO tradeInventoryDAO;
	@Resource
	private ItemCategoryDAO itemCategoryDAO;
	@Resource
	private ItemAttrValueItemExportService itemAttrValueItemExportService;
	@Resource
	private ItemAttributeDAO itemAttributeDAO;
	@Resource
	private ItemAttributeValueDAO itemAttributeValueDAO;
	@Resource
	private ItemSkuPackageService itemSkuPackageService;
	@Resource
	private ItemSkuPackageDAO itemSkuPackageDAO;

	/**
	 * 
	 * <p>
	 * Discription:[方法功能中文描述:批量修改商品状态]
	 * </p>
	 * Created on 2015年2月4日
	 * 
	 * @param ids
	 * @param changeReason
	 * @param itemStatus
	 * @return
	 * @author:[chenx]
	 */
	@Override
	public ExecuteResult<String> modifyItemStatusBatch(
			ItemStatusModifyDTO statusDTO) {
		LOGGER.info("================开始批量修改商品状态=====================");
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		try {
			if (statusDTO.getItemStatus() == null) {
				executeResult.addErrorMessage("商品状态为空！");
				return executeResult;
			}
			if (statusDTO.getOperator() == null) {
				executeResult.addErrorMessage("Operator为空！");
				return executeResult;
			}
			List<Long> ids = statusDTO.getItemIds();
			if (ids == null || ids.size() <= 0) {
				executeResult.addErrorMessage("商品ID为空！");
				return executeResult;
			}
			// 平台下架商品 下架通过平台商品发布的所有商品
			if (2 == statusDTO.getOperator()
					&& statusDTO.getItemStatus() == ItemStatusEnum.UNSHELVED
							.getCode()) {
				List<ItemDTO> items = null;
				List<Long> subIds = null;
				for (Long id : ids) {
					// 查询通过选择平台商品发布的商家商品
					items = this.itemMybatisDAO.querySellerItems(id);
					// 下架所有商品
					subIds = new ArrayList<Long>();
					for (ItemDTO item : items) {
						if (item.getAddSource() == ItemAddSourceEnum.PLATFORM.getCode()) {// 从平台上传的商品
							subIds.add(item.getItemId());
						}
					}
					if (subIds.isEmpty()) {
						continue;
					} else {
						itemMybatisDAO.updateItemStatusBatch(subIds,
								"平台对应商品已经下架！", statusDTO.getItemStatus());
					}
				}
				this.itemMybatisDAO.updateItemStatusBatch(ids,
						statusDTO.getChangeReason(), statusDTO.getItemStatus());
				// 商家上架商品 校验平台商品状态 平台商品下架的 该商品不能上架
			} else if (1 == statusDTO.getOperator()
					&& statusDTO.getItemStatus() == ItemStatusEnum.SALING
							.getCode()) {
				itemMybatisDAO.updateItemStatusBatch(
						this.getInputItemIds(statusDTO),
						statusDTO.getChangeReason(), statusDTO.getItemStatus());
				// 审核通过卖家商品 并且要加入到平台商品库 的商品 查询出原有商品 并且加入平台商品库
			} else if (1 == statusDTO.getOperator()
					&& statusDTO.getItemStatus() == ItemStatusEnum.SHELVING
							.getCode()) {
				List<Long> modifyItemIds = this.getInputItemIds(statusDTO);
				statusDTO.setItemIds(modifyItemIds);
				if (!modifyItemIds.isEmpty()) {
					if (statusDTO.isCreatePlatItem()) {
						this.copyItemToPlat(statusDTO);
					}
					itemMybatisDAO.updateItemStatusBatch(modifyItemIds,
							statusDTO.getChangeReason(),
							statusDTO.getItemStatus());
				}
			} else {
				// 其他修改
				itemMybatisDAO.updateItemStatusBatch(ids,
						statusDTO.getChangeReason(), statusDTO.getItemStatus());
			}
			executeResult.setResultMessage("操作成功");
		} catch (Exception e) {
			LOGGER.error("执行方法【modifyItemStatusById】报错：{}", e);
			executeResult.addErrorMessage("执行方法【modifyItemStatusById】报错："
					+ e.getMessage());
			throw new RuntimeException(e);
		} finally {
			LOGGER.info("================结束批量修改商品状态=====================");
		}
		return executeResult;
	}

	/**
	 * 
	 * <p>
	 * Discription:[过滤对应平台库商品 已经下架的 店铺商品]
	 * </p>
	 * Created on 2015-6-26
	 * 
	 * @return
	 * @author:wangcs
	 */
	private List<Long> getInputItemIds(ItemStatusModifyDTO statusDTO) {
		ItemQueryInDTO inDTO = new ItemQueryInDTO();
		List<Long> idList = new ArrayList<Long>();
		for (Long itemId : statusDTO.getItemIds()) {
			inDTO = new ItemQueryInDTO();
			inDTO.setId(Integer.parseInt(Long.toString(itemId)));
			List<ItemQueryOutDTO> items = this.itemMybatisDAO.queryItemList(
					inDTO, null);
			if (!items.isEmpty()) {
				inDTO.setId(null);
				inDTO.setPlatItemId(items.get(0).getItemId());
				List<ItemQueryOutDTO> dbItem = this.itemMybatisDAO
						.queryItemList(inDTO, null);
				if (dbItem != null && dbItem.size() > 0) {
					if (ItemStatusEnum.UNSHELVED.getCode() >= dbItem.get(0)
							.getItemStatus()) {
						continue;
					}
				}
			}

			idList.add(itemId);
		}
		return idList;
	}

	/**
	 * 
	 * <p>
	 * Discription:[查询出原有商品 并且加入平台商品库]
	 * </p>
	 * Created on 2015-5-4
	 * 
	 * @param statusDTO
	 * @author:wangcs
	 */
	private void copyItemToPlat(ItemStatusModifyDTO statusDTO) {
		for (Long itemId : statusDTO.getItemIds()) {
			ItemDTO item = this.getItemById(itemId).getResult();
			if (item == null) {
				continue;
			}
			// 修改原商品为 已加入商品库
			ItemDTO param = new ItemDTO();
			param.setItemId(item.getItemId());
			param.setCopied(2);
			this.itemMybatisDAO.updateItem(param);
			// 设置加入平台商品库的商品属性
			item.setItemStatus(ItemStatusEnum.SALING.getCode());
			item.setListtingTime(new Date());
			item.setDelistingTime(null);
			item.setPlatLinkStatus(ItemPlatLinkStatusEnum.STORED.getCode());
			item.setOperator(2);// 平台商品
			item.setAddSource(null);
			item.setShopCid(null);
			item.setShopId(null);
			item.setSellerId(null);
			item.setPlstItemId(null);
			item.setTimingListing(null);
			item.setShopFreightTemplateId(null);
			// 查询商品的销售属性
			String attrSale = item.getAttrSaleStr();
			List<ItemAttr> attrSales = this.getItemAttrList(attrSale);
			StringBuffer buffer = new StringBuffer();
			List<Long> ids = new ArrayList<Long>();
			String str = "";
			for (ItemAttr attr : attrSales) {
				Long id = this.insertAttribute(attr);
				for (ItemAttrValue value : attr.getValues()) {
					value.setAttrId(id);
					Long valueId = this.insertAttributeValue(value);
					str = id + ":" + valueId + ";";
					buffer.append(str);
				}
			}
			item.setAttrSale(attrSales);
			item.setAttrSaleStr(buffer.toString());
			// 加入平台商品库
			this.addItemInfo(item);
			// 店铺商品入库时，该商品前台商品来源显示平台上传
			// param.setAddSource(ItemAddSourceEnum.PLATFORM.getCode());
			param.setPlstItemId(item.getItemId());
			this.itemMybatisDAO.updateItem(param);
		}
	}

	/**
	 * 
	 * <p>
	 * Discription:[方法功能中文描述:查询商品的信息列表]
	 * </p>
	 * Created on 2015年2月4日
	 * 
	 * @param itemInDTO
	 * @param page
	 * @return
	 * @author:[chenx]
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DataGrid<ItemQueryOutDTO> queryItemList(ItemQueryInDTO itemInDTO,
			Pager page) {
		DataGrid<ItemQueryOutDTO> dataGrid = new DataGrid<ItemQueryOutDTO>();
		try {
			List<ItemQueryOutDTO> listItemDTO = itemMybatisDAO.queryItemList(
					itemInDTO, page);
			Long size = itemMybatisDAO.queryCount(itemInDTO);
			List<SellPrice> prices = null;
			List<ItemPicture> pics = null;
			for (ItemQueryOutDTO item : listItemDTO) {
				String attrSale = item.getAttrSaleStr();
				List<ItemAttr> attrSales = this.getItemAttrList(attrSale);
				//查询商品的销售属性集合
				item.setAttrSale(attrSales);
				// 查询价格
				prices = this.itemPriceDAO
						.queryItemSellPrices(item.getItemId());
				item.setAreaPrices(prices);
				// 查询图片
				pics = this.itemPictureDAO.queryItemPicsById(item.getItemId());
				if (!pics.isEmpty()) {
					item.setPictureUrl(pics.get(0).getPictureUrl());
				}
				// 查询商品SKU 阶梯价 图片 库存 成本价
				List<SkuInfo> skus = this.getItemSkuList(item.getItemId());
				item.setSkuInfos(skus);
			}
			dataGrid.setTotal(size);
			dataGrid.setRows(listItemDTO);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return dataGrid;
	}

	/**
	 * 
	 * <p>
	 * Discription:[方法功能中文描述:查询商品的信息列表]
	 * </p>
	 * Created on 2015年2月4日
	 * 
	 * @param itemInDTO
	 * @param page
	 * @return
	 * @author:[chenx]
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DataGrid<ItemQueryOutDTO> queryItemListFlag(ItemQueryInDTO itemInDTO,
			Pager page,Integer oldRows) {
		DataGrid<ItemQueryOutDTO> dataGrid = new DataGrid<ItemQueryOutDTO>();
		try {
			Long size = itemMybatisDAO.queryCount(itemInDTO);
			if(page.getRows()>oldRows){//新选择的每页显示的条数大于旧的每页显示的条数即10(旧)->50(新)
				int newPage=((page.getPage()-1)*oldRows)/page.getRows()+1;
				page.setPage(newPage);
			}else if(page.getRows()<oldRows){//50->10  默认回到首页
				page.setPage(1);//-->pageOffset = (page - 1) * rows=0
			}//相等不变
			List<ItemQueryOutDTO> listItemDTO = itemMybatisDAO.queryItemListByCreateTime(
					itemInDTO, page);
			List<SellPrice> prices = null;
			List<ItemPicture> pics = null;
			for (ItemQueryOutDTO item : listItemDTO) {
				// 查询价格
				prices = this.itemPriceDAO
						.queryItemSellPrices(item.getItemId());
				item.setAreaPrices(prices);
				// 查询图片
				pics = this.itemPictureDAO.queryItemPicsById(item.getItemId());
				if (!pics.isEmpty()) {
					item.setPictureUrl(pics.get(0).getPictureUrl());
				}
			}
			dataGrid.setTotal(size);
			dataGrid.setRows(listItemDTO);
			//dataGrid.setPageNum(page.getPage());//当前页码
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return dataGrid;
	}
	
	/**
	 * 
	 * <p>
	 * Discription:[方法功能中文描述：根据id查询详情]
	 * </p>
	 * Created on 2015年2月4日
	 * 
	 * @param itemId
	 * @return
	 * @author:[wangcs]
	 */
	@Override
	public ExecuteResult<ItemDTO> getItemById(Long itemId) {
		LOGGER.info("\n 方法[{}]，入参：[{}]", "ItemExportServiceImpl-getItemById",
				itemId);
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		try {
			ItemDTO itemDTO = itemMybatisDAO.getItemDTOById(itemId);
			if (itemDTO == null) {
				result.addErrorMessage("没有该商品信息！");
				return result;
			}
			// 销售属性
			String attrSale = itemDTO.getAttrSaleStr();
			List<ItemAttr> attrSales = new ArrayList<ItemAttr>();
			if (itemDTO.getAddSource() != null && itemDTO.getAddSource().intValue() == ItemAddSourceEnum.COMBINATION.getCode()) {
				attrSales = this.getCombinationItemAttrList(attrSale);
			} else {
				attrSales = this.getItemAttrList(attrSale);
			}
			itemDTO.setAttrSale(attrSales);
			// 非销售属性
			String attrStr = itemDTO.getAttributesStr();
			List<ItemAttr> attrs = this.getItemAttrList(attrStr);
			itemDTO.setAttributes(attrs);
			// 查询商品阶梯价
			List<SellPrice> sellPrices = this.itemPriceDAO
					.queryItemSellPrices(itemId);
			itemDTO.setSellPrices(sellPrices);
			// 商品图片
			String[] picUrls = this.getItemPictures(itemDTO.getItemId());
			itemDTO.setPicUrls(picUrls);

			// 查询商品SKU 阶梯价 图片 库存 成本价
			List<SkuInfo> skus = this.getItemSkuList(itemId);
			itemDTO.setSkuInfos(skus);
			result.setResult(itemDTO);
		} catch (Exception e) {
			LOGGER.error("执行方法【getItemById】报错:", e);
			result.addErrorMessage("执行方法【getItemById】报错:" + e.getMessage());
			throw new RuntimeException(e);
		}
		LOGGER.info("\n 方法[{}]，出参：[{}]", "ItemExportServiceImpl-getItemById",
				JSONObject.toJSONString(result));
		return result;
	}

	/**
	 * 
	 * <p>
	 * Discription:[查询商品基础数据【商品主表数据】]
	 * </p>
	 * Created on 2015年11月30日
	 * 
	 * @param itemId
	 * @return
	 * @author:[zhangzq]
	 */
	public ExecuteResult<ItemBaseDTO> getItemBaseInfoById(Long itemId) {
		ExecuteResult<ItemBaseDTO> result = new ExecuteResult<ItemBaseDTO>();
		try {
			ItemBaseDTO itemBaseDTO = new ItemBaseDTO();
			ItemDTO itemDTO = itemMybatisDAO.getItemDTOById(itemId);
			BeanUtils.copyProperties(itemDTO, itemBaseDTO);
			result.setResult(itemBaseDTO);
		} catch (Exception e) {
			LOGGER.error("执行方法【getItemBaseInfoById】报错:", e);
			result.addErrorMessage("执行方法【getItemBaseInfoById】报错:"
					+ e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 
	 * <p>
	 * Discription:[获取商品图片]
	 * </p>
	 * Created on 2015-3-18
	 * 
	 * @param itemId
	 * @return
	 * @author:wangcs
	 */
	private String[] getItemPictures(Long itemId) {
		List<ItemPicture> pics = this.itemPictureDAO.queryItemPicsById(itemId);
		List<String> picUrls = new ArrayList<String>();
		for (ItemPicture itemPicture : pics) {
			picUrls.add(itemPicture.getPictureUrl());
		}
		return picUrls.toArray(new String[] {});
	}

	@Override
	public ExecuteResult<ItemDTO> addItemInfo(ItemDTO itemDTO) {
		LOGGER.info("=============开始发布商品====================");
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		try {
			// 校验空值
			if (itemDTO == null) {
				result.addErrorMessage("参数为空！");
				return result;
			}
			Integer operator = itemDTO.getOperator();
			if (operator == 1) {// 卖家添加商品
				result = this.addSellerItem(itemDTO);
			} else if (operator == 2) {// 平台添加商品
				result = this.addPlatformItem(itemDTO);
			} else {
				result.addErrorMessage("orperator值不正确！" + operator);
				return result;
			}

		} catch (Exception e) {
			LOGGER.error("执行方法【addItemInfo】报错:", e);
			result.addErrorMessage("执行方法【addItemInfo】报错:" + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			LOGGER.info("=============结束发布商品====================");
		}
		return result;
	}

	@Override
	public ExecuteResult<ItemDTO> modifyItemById(ItemDTO itemDTO) {
		LOGGER.info("=============开始修改商品信息=================");
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		try {
			// 校验空值
			if (itemDTO == null) {
				result.addErrorMessage("参数为空！");
				return result;
			}
			ItemDTO dbItem = this.itemMybatisDAO.getItemDTOById(itemDTO
					.getItemId());
			if (dbItem == null) {// 商品信息不能为空
				result.addErrorMessage("没有查询到该商品信息！");
				return result;
			}
			Integer operator = itemDTO.getOperator();
			if (operator == 1) {// 卖家修改商品
				result = this.modifySellerItem(itemDTO);
			} else if (operator == 2) {// 平台修改商品
				result = this.modifyPlatformItem(itemDTO);
			} else {
				result.addErrorMessage("orperator值不正确！" + operator);
				return result;
			}
		} catch (Exception e) {
			LOGGER.error("执行方法【modifyItemById】报错:{}", e);
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			LOGGER.info("=============结束修改商品信息=================");
		}

		return result;
	}
	
	@Override
	public ExecuteResult<ItemShopCartDTO> getCombinationSkuShopCart(ItemShopCartDTO skuDTO) {
		ExecuteResult<ItemShopCartDTO> result = new ExecuteResult<ItemShopCartDTO>();
		try {
			if (skuDTO == null) {
				result.addErrorMessage("参数为空！");
				return result;
			}
			ItemSku sku = this.itemMybatisDAO.getItemSkuById(skuDTO);
			if (sku == null) {
				result.addErrorMessage("没有查询到该SKU！");
				return result;
			}
			List<ItemAttr> attrList = this.getCombinationItemAttrList(sku.getAttributes());
			skuDTO.setAttrSales(attrList);
			skuDTO.setItemName(sku.getItemName());
			skuDTO.setCid(sku.getCid());
			skuDTO.setItemStatus(sku.getItemStatus());
			skuDTO.setSkuScope(sku.getSkuScope());
			skuDTO.setHasPrice(sku.getHasPrice() == 1 ? true : false);
			List<SkuPictureDTO> pics = this.itemMybatisDAO.querySkuPics(sku.getSkuId());
			if (pics != null && pics.size() > 0) {
				skuDTO.setSkuPicUrl(pics.get(0).getPicUrl());
			}
			skuDTO.setSkuPics(pics);
			// 查询价格
			BigDecimal skuPrice = this.getSkuPrice(skuDTO);
			skuDTO.setSkuPrice(skuPrice);
			// 增加库存
			Integer qty = this.tradeInventoryDAO.queryBySkuId(skuDTO.getSkuId()).getTotalInventory();
			skuDTO.setQty(qty);
			result.setResult(skuDTO);
		} catch (Exception e) {
			result.addErrorMessage("报错信息：" + e.getMessage());
			LOGGER.error("执行方法【getSkuShopCart】报错：{}", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<ItemShopCartDTO> getSkuShopCart(ItemShopCartDTO skuDTO) {
		ExecuteResult<ItemShopCartDTO> result = new ExecuteResult<ItemShopCartDTO>();
		try {
			if (skuDTO == null) {
				result.addErrorMessage("参数为空！");
				return result;
			}
			ItemSku sku = this.itemMybatisDAO.getItemSkuById(skuDTO);
			if (sku == null) {
				result.addErrorMessage("没有查询到该SKU！");
				return result;
			}

			List<ItemAttr> attrList = this.getItemAttrList(sku.getAttributes());
			skuDTO.setAttrSales(attrList);
			skuDTO.setItemName(sku.getItemName());
			skuDTO.setCid(sku.getCid());
			skuDTO.setItemStatus(sku.getItemStatus());
			skuDTO.setSkuScope(sku.getSkuScope());
			skuDTO.setHasPrice(sku.getHasPrice() == 1 ? true : false);
			List<SkuPictureDTO> pics = this.itemMybatisDAO.querySkuPics(sku.getSkuId());
			if (pics != null && pics.size() > 0) {
				skuDTO.setSkuPicUrl(pics.get(0).getPicUrl());
			}
			skuDTO.setSkuPics(pics);
			// 查询价格
			BigDecimal skuPrice = this.getSkuPrice(skuDTO);
			skuDTO.setSkuPrice(skuPrice);
			// 增加库存
			Integer qty = this.tradeInventoryDAO.queryBySkuId(skuDTO.getSkuId()).getTotalInventory();
			skuDTO.setQty(qty);
			result.setResult(skuDTO);
		} catch (Exception e) {
			result.addErrorMessage("报错信息：" + e.getMessage());
			LOGGER.error("执行方法【getSkuShopCart】报错：{}", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> modifyItemAdBatch(List<ItemAdDTO> ads) {
		LOGGER.info("================开始批量修改商品广告词=============");
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			if (ads == null || ads.size() <= 0) {
				result.addErrorMessage("参数为空！");
				return result;
			}
			this.itemMybatisDAO.modifyItemAdBatch(ads);
		} catch (Exception e) {
			result.addErrorMessage("执行方法【modifyItemAdBatch】报错：{}"
					+ e.getMessage());
			LOGGER.error("执行方法【modifyItemAdBatch】报错：{}", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			LOGGER.info("================结束批量修改商品广告词=============");
		}
		return result;
	}

	@Override
	public ExecuteResult<String> modifyItemShopCidBatch(
			List<ItemShopCidDTO> cids) {
		LOGGER.info("============开始批量修改商品店铺分类=============");
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			if (cids == null || cids.size() <= 0) {
				result.addErrorMessage("参数为空！");
				return result;
			}
			this.itemMybatisDAO.modifyItemShopCidBatch(cids);
		} catch (Exception e) {
			result.addErrorMessage("执行方法【modifyItemShopCidBatch】报错:{}"
					+ e.getMessage());
			LOGGER.info("执行方法【modifyItemShopCidBatch】报错:{}", e);
			throw new RuntimeException(e);
		} finally {
			LOGGER.info("============开始批量修改商品店铺分类=============");
		}
		return result;
	}

	/**
	 * 
	 * <p>
	 * Discription:[添加卖家商品]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param itemDTO
	 * @author:wangcs
	 */
	private ExecuteResult<ItemDTO> addSellerItem(ItemDTO itemDTO)
			throws Exception {
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		// 校验空值
		result = this.checkExistSellerItemNull(itemDTO);
		if (!result.isSuccess()) {
			return result;
		}
		Item item = ItemDTOToDomainUtil.itemDTO2Item(itemDTO);
		// 先保存商品基本信息并返回itemId
		Long itemId = this.insertItem(item);
		// 保存商品销售属性
		List<ItemAttr> saleAttrs = itemDTO.getAttrSale();
		this.addItemAttrs(saleAttrs, 1, itemId);
		// 保存商品非销售属性
		List<ItemAttr> attrs = itemDTO.getAttributes();
		this.addItemAttrs(attrs, 2, itemId);
		itemDTO.setItemId(itemId);
		// 把商品Id和图片路径保存到商品图片表
		this.insertItemPics(itemDTO);
		// 保存地域阶梯价以及商品id到商品价格表里
		this.insertItemSellPrices(itemDTO);
		// 保存SKU相关信息
		this.insertItemSkuInfo(itemDTO);
		//保存套装商品关联服务商品信息
		this.insertItemPackageDto(itemDTO);
		result.setResult(itemDTO);
		return result;
	}
	//保存套装商品关联服务商品信息
	private void insertItemPackageDto(ItemDTO itemDTO){
		List<ItemSkuPackageDTO> itemPackageDtos=itemDTO.getItemSkuPackageDTOs();
		if(!itemPackageDtos.isEmpty()&&itemPackageDtos.size()>0){
			for(ItemSkuPackageDTO  itemSkuPackageDTO:itemPackageDtos){
					itemSkuPackageDTO.setPackageItemId(itemDTO.getItemId());
					itemSkuPackageDAO.addItemSkuPackage(itemSkuPackageDTO);
			}
		}
	}
	/**
	 * 
	 * <p>
	 * Discription:[添加商品属性]
	 * </p>
	 * Created on 2015-6-1
	 * 
	 * @param saleAttrs
	 *            属性列表
	 * @param attrType
	 *            属性类型:1:销售属性;2:非销售属性
	 * @author:wangcs
	 */
	private void addItemAttrs(List<ItemAttr> saleAttrs, int attrType,
			long itemId) {
		List<ItemAttrValueItemDTO> paramList = new ArrayList<ItemAttrValueItemDTO>();
		ItemAttrValueItemDTO dto = null;
		for (ItemAttr attr : saleAttrs) {
			for (ItemAttrValue value : attr.getValues()) {
				dto = new ItemAttrValueItemDTO();
				dto.setAttrId(attr.getId());
				dto.setAttrType(attrType);
				dto.setValueId(value.getId());
				dto.setItemId(itemId);
				paramList.add(dto);
			}
		}
		if (paramList.size() > 0) {
			this.itemAttrValueItemExportService.addItemAttrValueItem(paramList);
		}
	}

	/**
	 * 
	 * <p>
	 * Discription:[保存SKU相关信息]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param skuInfos
	 * @author:wangcs
	 */
	private void insertItemSkuInfo(ItemDTO itemDTO) {
		List<SkuInfo> skus = itemDTO.getSkuInfos();
		ItemSku itemSku = null;
		for (SkuInfo sku : skus) {
			if (sku.getAttributes() != null) {// 前台页面会传入空的无效SKU 这里过滤
				itemSku = new ItemSku();
				itemSku.setAttributes(sku.getAttributes());
				itemSku.setItemId(itemDTO.getItemId());
				itemSku.setSellerId(itemDTO.getSellerId());
				itemSku.setShopId(itemDTO.getShopId());
				itemSku.setSkuId(this.itemSkuDAO.getSkuId());
				/*if(StringUtils.isNotEmpty(sku.getSubSkuIds())){
					String addSourceSku=sku.getSubSkuIds().replace(",", ";");
					itemSku.setAddSourceSku(addSourceSku);
				}*/
				// 插入SKU表
				this.itemSkuDAO.add(itemSku);
				sku.setSkuId(itemSku.getSkuId());
				// 插入SKU 图片
				this.inserItemSkuPictures(sku, itemDTO);
				// 插入SKU区域阶梯价 和 SKU区域阶梯价日志
				this.insertItemSkuSellPrices(sku, itemDTO);
				// 插入SKU库存表
				this.insertItemSkuInventory(sku, itemDTO);
				//向组合套装关联表添加关联sku信息
				this.insertItemSkuPackage(sku);
			}
		}
		//更新组合套装商品数量
		if(itemDTO.getAddSource()==ItemAddSourceEnum.COMBINATION.getCode()){
			this.updateItemSkuPackageNum(itemDTO);
		}
	}
	private void updateItemSkuPackageNum(ItemDTO itemDTO){
		ItemSkuPackageDTO itemSkuPackage=new ItemSkuPackageDTO();
		itemSkuPackage.setPackageItemId(itemDTO.getItemId());
		itemSkuPackage.setAddSource(1);
		List<ItemSkuPackageDTO> dtos=itemSkuPackageService.getPackages(itemSkuPackage);
		String subNum=itemDTO.getSubNum();
		String[] subNums=subNum.split(";");
		List<String> ss=Arrays.asList(subNums);
		for(ItemSkuPackageDTO dto:dtos){
			for(String aa:ss){
				String[] ids=aa.split(":");
				String itemId=ids[0];
				String num=ids[1];
				if(dto.getSubItemId().equals(Long.valueOf(itemId))){
					dto.setSubNum(Integer.parseInt(num));
					itemSkuPackageDAO.update(dto);
				}
			}
		}
	}
	/**
	 * 
	 * <p>
	 * Discription:[插入SKU库存表]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param sku
	 * @author:wangcs
	 */
	private void insertItemSkuInventory(SkuInfo sku, ItemDTO itemDTO) {
		TradeInventory inv = new TradeInventory();
		inv.setCreateUser(itemDTO.getSellerId());
		inv.setOccupiedInventory(0);
		inv.setSellerId(itemDTO.getSellerId());
		inv.setShopId(itemDTO.getShopId());
		inv.setSkuId(sku.getSkuId());
		inv.setTotalInventory(sku.getSkuInventory());
		inv.setUpdateUser(itemDTO.getSellerId());
		this.itemSkuDAO.insertItemSkuInventory(inv);
	}
	/**
	 * 向组合套装关联表添加关联sku信息
	 * @param sku
	 */
	private void insertItemSkuPackage(SkuInfo sku){
		String subSkuIds=sku.getSubSkuIds();
		if(StringUtils.isNotBlank(subSkuIds)){
			itemSkuPackageService.add(sku.getSkuId(), subSkuIds.split(","));
		}
	}
	/**
	 * 
	 * <p>
	 * Discription:[插入SKU区域阶梯价 和 SKU区域阶梯价日志]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param sku
	 * @author:wangcs
	 */
	private void insertItemSkuSellPrices(SkuInfo sku, ItemDTO itemDTO) {
		LOGGER.info("======开始插入SKU价格=======");
		TradeSkuPrice skuPrice = null;
		LOGGER.info("传入参数：" + JSON.toJSONString(sku.getSellPrices()));
		for (SellPrice price : sku.getSellPrices()) {
			if (price.getMinNum() != null && price.getMaxNum()!=null) {// 前台会传空的 进行过滤
				skuPrice = new TradeSkuPrice();
				skuPrice.setAreaId(price.getAreaId());
				skuPrice.setAreaName(price.getAreaName());
				skuPrice.setCostPrice(price.getCostPrice());
				skuPrice.setItemId(itemDTO.getItemId());
				skuPrice.setMarketPrice(price.getMaketPirce());
				skuPrice.setMaxNum(price.getMaxNum());
				skuPrice.setMinNum(price.getMinNum());
				skuPrice.setSellerId(itemDTO.getSellerId());
				skuPrice.setSellPrice(price.getSellPrice());
				skuPrice.setShopId(itemDTO.getShopId());
				skuPrice.setSkuId(sku.getSkuId());
				skuPrice.setUpdateUser(itemDTO.getSellerId());
				skuPrice.setCreateUser(itemDTO.getSellerId());
				this.itemSkuDAO.insertSkuPrice(skuPrice);
				this.itemSkuDAO.insertSkuPriceLog(skuPrice);
			}
		}
		LOGGER.info("======结束插入SKU价格=======");
	}

	/**
	 * 
	 * <p>
	 * Discription:[插入SKU 图片]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param sku
	 * @author:wangcs
	 */
	private void inserItemSkuPictures(SkuInfo sku, ItemDTO itemDTO) {
		ItemSkuPicture skuPic = null;
		for (SkuPictureDTO pic : sku.getSkuPics()) {
			if (!StringUtils.isBlank(pic.getPicUrl())) {// 去掉空值
				skuPic = new ItemSkuPicture();
				skuPic.setPictureUrl(pic.getPicUrl());
				skuPic.setSellerId(itemDTO.getSellerId());
				skuPic.setShopId(itemDTO.getShopId());
				skuPic.setSkuId(sku.getSkuId());
				skuPic.setSortNumber(pic.getSortNumber());
				this.itemSkuDAO.insertSkuPicture(skuPic);
			}
		}
	}

	/**
	 * 
	 * <p>
	 * Discription:[卖家添加商品时校验空值]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param itemDTO
	 * @return
	 * @author:wangcs
	 */
	private ExecuteResult<ItemDTO> checkExistSellerItemNull(ItemDTO itemDTO) {
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		if (itemDTO.getSellerId() == null) {
			result.addErrorMessage("参数【SellerId】为空！");
			return result;
		}
		/*if (itemDTO.getShopCid() == null) {
			result.addErrorMessage("参数【ShopCid】为空！");
			return result;
		}*/
		if (StringUtils.isBlank(itemDTO.getItemName())) {
			result.addErrorMessage("参数【ItemName】为空！");
			return result;
		}
		if (itemDTO.getCid() == null) {
			result.addErrorMessage("参数【Cid】为空！");
			return result;
		}
		if (itemDTO.getBrand() == null) {
			result.addErrorMessage("参数【Brand】为空！");
			return result;
		}
		if (itemDTO.getHasPrice() == null) {
			result.addErrorMessage("参数【HasPrice】为空！");
			return result;
		}
		// if(itemDTO.getProductId() == null){
		// result.addErrorMessage("参数【ProductId】为空！");
		// return result;
		// }
		if (itemDTO.getHasPrice()==1&&itemDTO.getMarketPrice() == null) {
			result.addErrorMessage("参数【MarketPrice】为空！");
			return result;
		}
		if (itemDTO.getHasPrice()==1&&itemDTO.getMarketPrice2() == null) {
			result.addErrorMessage("参数【MarketPrice2】为空！");
			return result;
		}
		if (itemDTO.getInventory() == null) {
			result.addErrorMessage("参数【Inventory】为空！");
			return result;
		}
		// if(itemDTO.getWeight() == null){
		// result.addErrorMessage("参数【Weight】为空！");
		// return result;
		// }
		if (StringUtils.isBlank(itemDTO.getDescribeUrl())) {
			result.addErrorMessage("参数【DescribeUrl】为空！");
			return result;
		}
		if (StringUtils.isBlank(itemDTO.getPackingList())) {
			result.addErrorMessage("参数【PackingList】为空！");
			return result;
		}
		if (itemDTO.getAddSource() == null) {
			result.addErrorMessage("参数【AddSource】为空！");
			return result;
		}
		if (itemDTO.getItemStatus() == null) {
			result.addErrorMessage("参数【ItemStatus】为空！");
			return result;
		}
		if (itemDTO.getPicUrls() != null && itemDTO.getPicUrls().length <= 0) {
			result.addErrorMessage("参数【PicUrls】为空！");
			return result;
		}
		if (itemDTO.getShopId() == null) {
			result.addErrorMessage("参数【ShopId】为空！");
			return result;
		}
		if (StringUtils.isBlank(itemDTO.getAttributesStr())) {
			result.addErrorMessage("参数【AttributesStr】为空！");
			return result;
		}
		if (StringUtils.isBlank(itemDTO.getAttrSaleStr())) {
			result.addErrorMessage("参数【AttrSaleStr】为空！");
			return result;
		}
		if (itemDTO.getSellPrices() == null) {
			result.addErrorMessage("参数【SellPrices】为空！");
			return result;
		}
		if (itemDTO.getSkuInfos() == null) {
			result.addErrorMessage("参数【SkuInfos】为空！");
			return result;
		}
		if (itemDTO.getOperator() == null) {
			result.addErrorMessage("参数【Operator】为空！");
			return result;
		}
		// if(itemDTO.getGuidePrice()==null){
		// result.addErrorMessage("参数【GuidePrice】为空！");
		// return result;
		// }
		if (itemDTO.getOrigin() == null) {
			result.addErrorMessage("参数【Origin】为空！");
			return result;
		}
		// 销售属性
		if (itemDTO.getAttrSale() == null || itemDTO.getAttrSale().isEmpty()) {
			result.addErrorMessage("参数【AttrSale】为空！");
			return result;
		}
		// 非销售属性
		if (itemDTO.getAttributes() == null
				|| itemDTO.getAttributes().isEmpty()) {
			result.addErrorMessage("参数【Attributes】为空！");
			return result;
		}
		// 运费,绿印的暂时不校验
		if (itemDTO.getPlatformId() == null
				|| itemDTO.getPlatformId() != Constants.PLATFORM_ID) {
			if (itemDTO.getShopFreightTemplateId() == null) {
				result.addErrorMessage("参数【ShopFreightTemplateId】为空！");
				return result;
			}
		}
		return result;
	}

	/**
	 * 
	 * <p>
	 * Discription:[插入商品区域价]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param itemDTO
	 * @author:wangcs
	 */
	private void insertItemSellPrices(ItemDTO itemDTO) {
		if (null != itemDTO.getSellPrices()) {
			for (SellPrice sellPrice : itemDTO.getSellPrices()) {
				if (sellPrice.getMinNum() != null&&sellPrice.getMaxNum()!=null) {
					ItemPrice itemPrice = ItemDTOToDomainUtil
							.itemDTO2SellPrice(itemDTO, sellPrice);
					itemPrice.setItemId(itemDTO.getItemId());
					this.itemPriceDAO.add(itemPrice);
				}
			}
		}
	}

	/**
	 * 
	 * <p>
	 * Discription:[插入ITEM 表数据 ]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param item
	 * @return
	 * @author:wangcs
	 */
	private Long insertItem(Item item) {
		item.setItemId(this.itemMybatisDAO.getItemId());
		itemMybatisDAO.addItem(item);
		return item.getItemId();
	}

	/**
	 * 
	 * <p>
	 * Discription:[插入itemAttribute表]
	 * </p>
	 * Created on 2015-9-23
	 * 
	 * @return
	 * @author:[wangp]
	 */
	private Long insertAttribute(ItemAttr itemAttr) {
		itemAttributeDAO.add(itemAttr);
		return itemAttr.getId();
	}

	/**
	 * 
	 * <p>
	 * Discription:[插入itemAttributeValue表]
	 * </p>
	 * Created on 2015-9-23
	 * 
	 * @param itemAttrValue
	 * @return
	 * @author:[wangp]
	 */
	private Long insertAttributeValue(ItemAttrValue itemAttrValue) {
		this.itemAttributeValueDAO.add(itemAttrValue);
		return itemAttrValue.getId();
	}

	/**
	 * 
	 * <p>
	 * Discription:[ 添加平台商品库商品 暂时保存： itemStatus:4 platLinkStatus:1 确定发布:
	 * itemStatus:4 platLinkStatus:3 ]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param itemDTO
	 * @author:wangcs
	 */
	private ExecuteResult<ItemDTO> addPlatformItem(ItemDTO itemDTO)
			throws Exception {
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		// 校验空值
		result = this.checkExistPlatItemNull(itemDTO);
		if (!result.isSuccess()) {
			return result;
		}
		Item item = ItemDTOToDomainUtil.itemDTO2Item(itemDTO);
		// 先保存商品基本信息并返回itemId
		Long itemId = this.insertItem(item);
		// 保存平台商品非销售属性
		this.addItemAttrs(itemDTO.getAttributes(), 2, itemId);
		// 保存平台商品销售属性
		this.addItemAttrs(itemDTO.getAttrSale(), 1, itemId);
		itemDTO.setItemId(itemId);
		// 把商品Id和图片路径保存到商品图片表
		this.insertItemPics(itemDTO);
		result.setResult(itemDTO);
		return result;
	}

	/**
	 * 
	 * <p>
	 * Discription:[把商品Id和图片路径保存到商品图片表]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param itemId
	 * @param picUrls
	 * @author:wangcs
	 */
	private void insertItemPics(ItemDTO itemDTO) {
		// 把商品Id和图片路径保存到商品图片表
		ItemPicture itemPicture = null;
		for (String pictureUrl : itemDTO.getPicUrls()) {
			if (null != pictureUrl) {
				itemPicture = new ItemPicture();
				itemPicture.setItemId(itemDTO.getItemId());
				itemPicture.setSellerId(itemDTO.getSellerId());
				itemPicture.setShopId(itemDTO.getShopId());
				itemPicture.setPictureUrl(pictureUrl);
				this.itemPictureDAO.add(itemPicture);
			}
		}
	}

	/**
	 * 
	 * <p>
	 * Discription:[检查平台商品必填参数是否存在空值]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param itemDTO
	 * @return
	 * @author:wangcs
	 */
	private ExecuteResult<ItemDTO> checkExistPlatItemNull(ItemDTO itemDTO) {
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		if (StringUtils.isBlank(itemDTO.getItemName())) {
			result.addErrorMessage("参数【ItemName】为空！");
			return result;
		}
		if (itemDTO.getCid() == null) {
			result.addErrorMessage("参数【Cid】为空！");
			return result;
		}
		if (itemDTO.getBrand() == null) {
			result.addErrorMessage("参数【Brand】为空！");
			return result;
		}
		if (itemDTO.getHasPrice() == null) {
			result.addErrorMessage("参数【HasPrice】为空！");
			return result;
		}
		// if(itemDTO.getProductId() == null){
		// result.addErrorMessage("参数【ProductId】为空！");
		// return result;
		// }
		if (StringUtils.isBlank(itemDTO.getDescribeUrl())) {
			result.addErrorMessage("参数【DescribeUrl】为空！");
			return result;
		}
		if (itemDTO.getItemStatus() == null) {
			result.addErrorMessage("参数【ItemStatus】为空！");
			return result;
		}
		if (itemDTO.getPicUrls() == null || itemDTO.getPicUrls().length <= 0) {
			result.addErrorMessage("参数【PicUrls】为空！");
			return result;
		}
		// if(itemDTO.getGuidePrice()==null){
		// result.addErrorMessage("参数【GuidePrice】为空！");
		// return result;
		// }
		if (itemDTO.getPlatLinkStatus() == null) {
			result.addErrorMessage("参数【PlatLinkStatus】为空！");
			return result;
		}
		// 销售属性
		// if (itemDTO.getAttrSale() == null || itemDTO.getAttrSale().isEmpty())
		// {
		// result.addErrorMessage("参数【AttrSale】为空！");
		// return result;
		// }
		// 非销售属性
		if (itemDTO.getAttributes() == null
				|| itemDTO.getAttributes().isEmpty()) {
			result.addErrorMessage("参数【Attributes】为空！");
			return result;
		}
		return result;
	}

	/**
	 * 
	 * <p>
	 * Discription:[获取商品的SKU信息 查询商品SKU 阶梯价 图片 库存 成本价 商品编号]
	 * </p>
	 * Created on 2015-3-13
	 * 
	 * @param itemId
	 * @return
	 * @author:wangcs
	 */
	private List<SkuInfo> getItemSkuList(Long itemId) throws Exception {
		List<SkuInfo> list = new ArrayList<SkuInfo>();
		list = this.itemMybatisDAO.queryItemSkuInfo(itemId);
		List<SellPrice> sellPrices = null;
		List<SkuPictureDTO> skuPics = null;
		for (SkuInfo sku : list) {
			// 阶梯价
			sellPrices = this.itemPriceService.querySkuSellPrices(sku
					.getSkuId());
			// 图片
			skuPics = this.itemMybatisDAO.querySkuPics(sku.getSkuId());
			TradeInventoryDTO dto = this.tradeInventoryDAO.queryBySkuId(sku
					.getSkuId());
			sku.setSkuInventory(dto.getTotalInventory());

			sku.setSkuPics(skuPics);
			sku.setSellPrices(sellPrices);
		}
		return list;
	}

	public ExecuteResult<List<SkuPictureDTO>> querySkuPics(Long skuId) {
		ExecuteResult<List<SkuPictureDTO>> result = new ExecuteResult<List<SkuPictureDTO>>();
		List<SkuPictureDTO> skuPics = this.itemMybatisDAO.querySkuPics(skuId);
		result.setResult(skuPics);
		return result;
	}
	
	/**
	 * <p>
	 * Discription:[根据属性字符串查询属性和属性值]
	 * </p>
	 * Created on 2015年2月5日
	 * 
	 * @param attrSale
	 * @return
	 * @author:[创建者中文名字]
	 */
	private List<ItemAttr> getItemAttrList(String attr) {
		List<ItemAttr> resultList = new ArrayList<ItemAttr>();

		try {
			List<String> attrIds = new ArrayList<String>(); // 属性
			List<String> attrValIds = new ArrayList<String>();// 属性值
			//
			if (!StringUtils.isBlank(attr)) {
				String[] keyVals = attr.split(";");
				String[] strs = null;
				for (String str : keyVals) {
					strs = str.split(",");
					for (String keyVal : strs) {
						String[] kvs = keyVal.split(":");
						if (!attrIds.contains(kvs[0])) {
							attrIds.add(kvs[0]);
						}
						if (!attrValIds.contains(kvs[1])) {
							attrValIds.add(kvs[1]);
						}
					}

				}
			}
			if (attrIds.isEmpty()) {
				return resultList;
			}
			// 查询所有属性对象
			resultList = this.itemCategoryDAO.queryItemAttrList(attrIds);
			// 查询属性值对象
			for (ItemAttr itemAttr : resultList) {
				List<ItemAttrValue> valueList = this.itemCategoryDAO
						.queryItemAttrValueList(itemAttr.getId(), attrValIds);
				itemAttr.setValues(valueList);
			}
		} catch (Exception e) {
			LOGGER.error("执行方法【getItemAttrList】报错:" + e.getMessage());
			throw new RuntimeException(e);
		}
		return resultList;
	}

	/**
	 * 
	 * <p>Description: [根据属性字符串查询组合商品的属性和属性值]</p>
	 * Created on 2016年02月17日
	 * @param attr 属性字符串
	 * @return
	 * @author:[宋文斌]
	 */
	private List<ItemAttr> getCombinationItemAttrList(String attr) {
		List<ItemAttr> resultList = new ArrayList<ItemAttr>();
		List<ItemAttrValue> attrValues = new ArrayList<ItemAttrValue>();
		try {
			ItemAttr itemAttr = new ItemAttr();
			itemAttr.setName("通用");
			List<String> attrIds = new ArrayList<String>(); // 属性
			List<String> attrValIds = new ArrayList<String>();// 属性值
			if (!StringUtils.isBlank(attr)) {
				// 组合商品的sku
				String[] itemskuAttrs = attr.split("\\|");
				for (String itemskuAttr : itemskuAttrs) {
					String[] keyVals = itemskuAttr.split(";");
					String[] strs = null;
					for (String str : keyVals) {
						strs = str.split(",");
						for (String keyVal : strs) {
							String[] kvs = keyVal.split(":");
							if (!attrIds.contains(kvs[0])) {
								attrIds.add(kvs[0]);
							}
							if (!attrValIds.contains(kvs[1])) {
								attrValIds.add(kvs[1]);
							}
						}
					}
					if (attrIds.isEmpty()) {
						return resultList;
					}
					List<ItemAttrValue> valueList = new ArrayList<ItemAttrValue>();
					// 查询属性值对象
					for (String attrId : attrIds) {
						List<ItemAttrValue> valueResult = this.itemCategoryDAO.queryItemAttrValueList(
								Long.parseLong(attrId), attrValIds);
						for (ItemAttrValue iav : valueResult) {
							for (ItemAttrValue value : valueList) {
								if (value.getId().equals(iav.getId())) {
									continue;
								}
							}
							valueList.add(iav);
						}
					}
					ItemAttrValue itemAttrValue = new ItemAttrValue();
					itemAttrValue.setItemSkuAttr(itemskuAttr);
					String itemSkuName = "";
					for (ItemAttrValue itemAttrValue_ : valueList) {
						itemSkuName += itemAttrValue_.getName();
					}
					itemAttrValue.setItemSkuName(itemSkuName);
					attrValues.add(itemAttrValue);
					attrIds.clear();
					attrValIds.clear();
				}
				itemAttr.setValues(attrValues);
				resultList.add(itemAttr);
			}
		} catch (Exception e) {
			LOGGER.error("执行方法【getItemAttrList】报错:", e);
			throw new RuntimeException(e);
		}
		return resultList;
	}

	/**
	 * 
	 * <p>
	 * Discription:[更新卖家商品]
	 * </p>
	 * Created on 2015-3-17
	 * 
	 * @param itemDTO
	 * @return
	 * @author:wangcs
	 */
	private ExecuteResult<ItemDTO> modifySellerItem(ItemDTO itemDTO) {
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		result = this.checkExistSellerItemNull(itemDTO);
		if (!result.isSuccess()) {
			return result;
		}
		// 修改商品信息
		this.itemMybatisDAO.update(itemDTO);
		// 修改商品图片
		this.updateItemPictures(itemDTO);
		// 修改商品阶梯价
		this.updateItemPrices(itemDTO);
		// 修改SKU相关信息
		this.updateItemSkuInfos(itemDTO);
		// 修改商品关联属性
		this.modifyItemAttrs(itemDTO.getAttributes(), itemDTO.getAttrSale(),
				itemDTO.getItemId());
		//修改组合服务商品关联关系
		this.modifyItemPackage(itemDTO);
		return result;
	}
	/**
	 * 修改组合服务商品关联关系
	 * @param itemDTO
	 */
	private void modifyItemPackage(ItemDTO itemDTO){
		if(itemDTO.getAddSource()==ItemAddSourceEnum.COMBINATION.getCode()){
			ItemSkuPackageDTO dto=new ItemSkuPackageDTO();
	    	dto.setPackageItemId(itemDTO.getItemId());
	    	List<Integer> addSources =new ArrayList<Integer>();
	    	addSources.add(ItemAddSourceEnum.BASICSERVICE.getCode());
	    	addSources.add(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());
	    	addSources.add(ItemAddSourceEnum.AUXILIARYMATERIAL.getCode());
	    	dto.setAddSources(addSources);
	    	List<ItemSkuPackageDTO> lists=itemSkuPackageService.getPackages(dto);
			if(!lists.isEmpty()&&lists.size()>0){
				for(ItemSkuPackageDTO itemSkuPackageDTO :lists){
					itemSkuPackageDAO.delete(itemSkuPackageDTO);
				}
			}
			//新增
			this.insertItemPackageDto(itemDTO);
		}
	}
	/**
	 * 
	 * <p>
	 * Discription:[修改商品属性]
	 * </p>
	 * Created on 2015-6-1
	 * 
	 * @param attributes
	 *            非销售属性
	 * @param attrSales
	 *            销售属性
	 * @param itemId
	 *            商品ID
	 * @author:wangcs
	 */
	private void modifyItemAttrs(List<ItemAttr> attributes,
			List<ItemAttr> attrSales, Long itemId) {
		List<ItemAttrValueItemDTO> paramList = new ArrayList<ItemAttrValueItemDTO>();
		ItemAttrValueItemDTO dto = null;
		for (ItemAttr attr : attributes) {// 非销售属性
			for (ItemAttrValue value : attr.getValues()) {
				dto = new ItemAttrValueItemDTO();
				dto.setAttrId(attr.getId());
				dto.setAttrType(2);
				dto.setValueId(value.getId());
				dto.setItemId(itemId);
				paramList.add(dto);
			}
		}
		for (ItemAttr attr : attrSales) {// 销售属性
			for (ItemAttrValue value : attr.getValues()) {
				dto = new ItemAttrValueItemDTO();
				dto.setAttrId(attr.getId());
				dto.setAttrType(1);
				dto.setValueId(value.getId());
				dto.setItemId(itemId);
				paramList.add(dto);
			}
		}
		this.itemAttrValueItemExportService.modifyItemAttrValueItem(paramList);
	}

	/**
	 * 
	 * <p>
	 * Discription:[ 更新商品SKU信息<br>
	 * 传入的参数说明：<br>
	 * 修改的SKU skuid不为空 这些SKU做修改<br>
	 * 删除的SKU 不传入 这些根据数据库原有的SKU与传入的SKU做对比筛选 <br>
	 * 添加的SKU 插入新SKU<br>
	 * 
	 * ]
	 * </p>
	 * Created on 2015-3-17
	 * 
	 * @param itemDTO
	 * @author:wangcs
	 */
	private void updateItemSkuInfos(ItemDTO itemDTO) {
		List<SkuInfo> insertSkus = new ArrayList<SkuInfo>();
		List<SkuInfo> updateSkus = new ArrayList<SkuInfo>();
		Map<Long, SkuInfo> existsSkus = new HashMap<Long, SkuInfo>();
		for (SkuInfo skuDTO : itemDTO.getSkuInfos()) {
			// 判断SKUID是否为空
			if (skuDTO.getSkuId() != null) {
				existsSkus.put(skuDTO.getSkuId(), skuDTO);
				updateSkus.add(skuDTO);
			} else {
				insertSkus.add(skuDTO);
			}
		}
		List<SkuInfo> dbSkus = this.itemMybatisDAO.queryItemSkuInfo(itemDTO
				.getItemId());
		Map<Long, SkuInfo> deleteSkus = new HashMap<Long, SkuInfo>();
		for (SkuInfo skuInfo : dbSkus) {
			deleteSkus.put(skuInfo.getSkuId(), skuInfo);
		}

		// 筛选出需要删除的SKU
		for (int i = 0; i < dbSkus.size(); i++) {
			SkuInfo sku = dbSkus.get(i);
			if (existsSkus.containsKey(sku.getSkuId())) {
				deleteSkus.remove(sku.getSkuId());
			}
		}
		// 插入需要新添加的SKU
		LOGGER.info("插入的SKU：" + JSON.toJSONString(insertSkus));
		itemDTO.setSkuInfos(insertSkus);
		this.insertItemSkuInfo(itemDTO);
		// 更新需要更新的SKU
		LOGGER.info("更新的SKU：" + JSON.toJSONString(insertSkus));
		itemDTO.setSkuInfos(updateSkus);
		this.updateModifiedSku(itemDTO);
		// 删除需要删除的SKU
		LOGGER.info("删除的SKU：" + JSON.toJSONString(insertSkus));
		
		for(Entry<Long, SkuInfo>  entry : deleteSkus.entrySet()) {
			Long key = entry.getKey();
			//更新组合套装关联表添加关联sku信息
			ItemSkuPackageDTO itemSkuPackageDTO=new ItemSkuPackageDTO();
			itemSkuPackageDTO.setPackageSkuId(key);
	    	List<ItemSkuPackageDTO> lists=itemSkuPackageService.getPackages(itemSkuPackageDTO);
	    	if(!lists.isEmpty()&&lists.size()>0){
	    		itemSkuPackageService.delete(itemSkuPackageDTO);
	    	}
			this.itemSkuDAO.deleteSkuById(key);
			  
		} 
	}

	/**
	 * 
	 * <p>
	 * Discription:[更新需要更新的SKU]
	 * </p>
	 * Created on 2015-3-17
	 * 
	 * @param itemDTO
	 * @author:wangcs
	 */
	private void updateModifiedSku(ItemDTO itemDTO) {
		LOGGER.info("==========开始更新SKU信息============");
		ItemSku itemSku = null;
		for (SkuInfo skuInfo : itemDTO.getSkuInfos()) {
			itemSku = new ItemSku();
			itemSku.setAttributes(skuInfo.getAttributes());
			itemSku.setItemId(itemDTO.getItemId());
			itemSku.setSellerId(itemDTO.getSellerId());
			itemSku.setShopId(itemDTO.getShopId());
			itemSku.setSkuId(skuInfo.getSkuId());
			//商品编号
			itemSku.setProductId(skuInfo.getSkuProductId());
			itemSku.setSkuStatus(1);
			this.itemSkuDAO.update(itemSku);
			// 更新SKU库存
			this.updateItemInventory(skuInfo, itemDTO);
			// 更新SKU价格
			this.updateItemSkuPrices(skuInfo, itemDTO);
			// 更新SKU图片
			this.updateItemSkuPictures(skuInfo, itemDTO);
		}
		LOGGER.info("==========结束更新SKU信息============");
	}

	/**
	 * 
	 * <p>
	 * Discription:[修改SKU库存]
	 * </p>
	 * Created on 2015-3-25
	 * 
	 * @param skuInfo
	 * @param itemDTO
	 * @author:wangcs
	 */
	private void updateItemInventory(SkuInfo skuInfo, ItemDTO itemDTO) {
		this.tradeInventoryDAO.modifyInventoryBySkuIds(skuInfo.getSkuId(),
				skuInfo.getSkuInventory());
	}

	/**
	 * 
	 * <p>
	 * Discription:[更新SKU图片]
	 * </p>
	 * Created on 2015-3-17
	 * 
	 * @param skuInfo
	 * @param itemDTO
	 * @author:wangcs
	 */
	private void updateItemSkuPictures(SkuInfo skuInfo, ItemDTO itemDTO) {
		this.itemSkuDAO.deleteSkuPictures(skuInfo.getSkuId());
		this.inserItemSkuPictures(skuInfo, itemDTO);
	}

	/**
	 * 
	 * <p>
	 * Discription:[ 更新SKU区域阶梯价 ]
	 * </p>
	 * Created on 2015-3-17
	 * 
	 * @param skuInfo
	 * @param itemDTO
	 * @author:wangcs
	 */
	private void updateItemSkuPrices(SkuInfo skuInfo, ItemDTO itemDTO) {
		LOGGER.info("=============开始更新SKU价格=================");
		this.itemSkuDAO.deleteSkuSellPrices(skuInfo.getSkuId());
		LOGGER.info("=============删除价格成功SKU价格=================");
		this.insertItemSkuSellPrices(skuInfo, itemDTO);
		LOGGER.info("=============结束更新SKU价格=================");
	}

	/**
	 * 
	 * <p>
	 * Discription:[更新商品区域阶梯价格]
	 * </p>
	 * Created on 2015-3-17
	 * 
	 * @param itemDTO
	 * @author:wangcs
	 */
	private void updateItemPrices(ItemDTO itemDTO) {
		// 删除原来的价格
		this.itemPriceDAO.deleteItemPrices(itemDTO.getItemId());
		// 插入新的价格
		this.insertItemSellPrices(itemDTO);
	}

	/**
	 * 
	 * <p>
	 * Discription:[修改商品图片]
	 * </p>
	 * Created on 2015-3-17
	 * 
	 * @param itemDTO
	 * @author:wangcs
	 */
	private void updateItemPictures(ItemDTO itemDTO) {
		// 删除原来的商品图片
		this.itemPictureDAO.deleteItemPicture(itemDTO.getItemId());
		// 添加新的商品图片
		this.insertItemPics(itemDTO);
	}

	/**
	 * 
	 * <p>
	 * Discription:[平台修改商品]
	 * </p>
	 * Created on 2015-3-17
	 * 
	 * @param itemDTO
	 * @return
	 * @author:wangcs
	 */
	private ExecuteResult<ItemDTO> modifyPlatformItem(ItemDTO itemDTO) {
		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		// 校验空值
		result = this.checkExistPlatItemNull(itemDTO);
		if (!result.isSuccess()) {
			result.addErrorMessage("必要参数存在空值！");
			return result;
		}
		// 修改商品信息
		this.itemMybatisDAO.updateItem(itemDTO);
		// 修改商品图片
		this.updateItemPictures(itemDTO);
		// 修改商品关联属性
		this.modifyItemAttrs(itemDTO.getAttributes(), itemDTO.getAttrSale(),
				itemDTO.getItemId());
		result.setResult(itemDTO);
		return result;
	}

	/**
	 * 
	 * <p>
	 * Discription:[获取商品价格]
	 * </p>
	 * Created on 2015-3-5
	 * 
	 * @param skuId
	 * @param areaCode
	 * @param itemId
	 * @param shopId
	 * @return
	 * @author:wangcs
	 */
	private BigDecimal getSkuPrice(ItemShopCartDTO skuDTO) throws Exception {
		PriceQueryParam param = new PriceQueryParam();
		param.setAreaCode(skuDTO.getAreaCode());
		param.setItemId(skuDTO.getItemId());
		param.setQty(skuDTO.getQty() == null ? 1 : skuDTO.getQty());
		param.setShopId(skuDTO.getShopId());
		param.setSkuId(skuDTO.getSkuId());
		param.setBuyerId(skuDTO.getBuyerId());
		param.setSellerId(skuDTO.getSellerId());
		if (!skuDTO.isHasPrice()) {
			BigDecimal price = this.itemPriceService.getInquiryPrice(param);
			skuDTO.setHasPrice(price == null ? false : true);
		}
		return this.itemPriceService.getSkuShowPrice(param);

	}

	// @Override
	// public DataGrid<ItemDTO> queryItemDTOList(SearchInDTO
	// inDTO,Pager<ItemDTO> pager) {
	// LOGGER.info("=============开始查询商品===============");
	// DataGrid<ItemDTO> dg = new DataGrid<ItemDTO>();
	// try {
	// if(inDTO.getAttributes()!=null){
	// String[] attrs = inDTO.getAttributes().split(";");
	// for (String attr : attrs) {
	// inDTO.getAttrList().add(attr);
	// }
	// }
	// List<ItemDTO> items = this.itemMybatisDAO.queryItemDTOList(inDTO,pager);
	// for (ItemDTO itemDTO : items) {
	// itemDTO.setSkuInfos(this.getItemSkuList(itemDTO.getItemId()));
	// }
	// Long total = this.itemMybatisDAO.queryItemDTOCount(inDTO);
	// dg.setRows(items);
	// dg.setTotal(total);
	// } catch (Exception e) {
	// LOGGER.error("执行方法【queryItemDTOList】报错：{}",e);
	// throw new RuntimeException(e);
	// } finally{
	// LOGGER.info("=============开始查询商品===============");
	// }
	// return dg;
	// }

	@SuppressWarnings("rawtypes")
	@Override
	public ExecuteResult<DataGrid<ItemQueryOutDTO>> queryItemByCidAndName(
			Long cid, String itemName, Pager page) {
		ExecuteResult<DataGrid<ItemQueryOutDTO>> result = new ExecuteResult<DataGrid<ItemQueryOutDTO>>();
		DataGrid<ItemQueryOutDTO> dataGrid = new DataGrid<ItemQueryOutDTO>();
		try {
			List<ItemQueryOutDTO> list = new ArrayList<ItemQueryOutDTO>();
			Long count = 0l;
			if (cid != null) {
				ItemCategoryDTO itemCategory = itemCategoryDAO.queryById(cid);
				if (itemCategory.getCategoryLev() == 3) {
					// 入过传入是三级类目 用三级类目查询商品
					ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
					itemInDTO.setCid(cid);
					itemInDTO.setOperator(2);
					itemInDTO.setItemStatus(4);
					itemInDTO.setItemName(itemName);
					DataGrid<ItemQueryOutDTO> d1 = this.queryItemList(
							itemInDTO, page);
					d1.getRows();
					list = d1.getRows();
					count += d1.getTotal();
				}
				if (itemCategory.getCategoryLev() == 2) {
					// 入过传入是2级类目 用二级类目查询下面的三级类目 用三级类目查询商品
					ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
					itemCategoryDTO.setCategoryParentCid(cid);
					List<ItemCategoryDTO> itemcList = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO, null);
					List<Long> idsList = new ArrayList<Long>();
					for (int i = 0; i < itemcList.size(); i++) {
						idsList.add(itemcList.get(i).getCategoryCid());
					}
					Long[] ids = idsList.toArray(new Long[] {});
					ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
					if (ids.length > 0) {
						itemQueryInDTO.setCids(ids);
						itemQueryInDTO.setOperator(2);
						itemQueryInDTO.setItemStatus(4);
						itemQueryInDTO.setItemName(itemName);
						DataGrid<ItemQueryOutDTO> d2 = this.queryItemList(
								itemQueryInDTO, page);
						d2.getRows();
						list = d2.getRows();
						count += d2.getTotal();
					} else {
						list = null;
					}
					/*
					 * list = itemMybatisDAO.queryItemList(itemQueryInDTO,page);
					 * count+= itemMybatisDAO.queryCount(itemQueryInDTO);
					 */
				}
				if (itemCategory.getCategoryLev() == 1) {
					// 传入的是一级类目

					// 用一级类目查询旗下的二级类目
					ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
					itemCategoryDTO.setCategoryParentCid(cid);
					List<ItemCategoryDTO> itemcList2 = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO, null);
					List<Long> cids2List = new ArrayList<Long>();
					for (int i = 0; i < itemcList2.size(); i++) {
						cids2List.add(itemcList2.get(i).getCategoryCid());
					}
					Long[] cids2 = cids2List.toArray(new Long[] {});
					// 用二级类目数字 查询旗下的三级类目
					ItemCategoryDTO itemCategoryDTO2 = new ItemCategoryDTO();
					if (cids2.length > 0) {
						itemCategoryDTO2.setCategoryParentCids(cids2);
					}
					List<ItemCategoryDTO> itemcList3 = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO2, null);
					List<Long> cids3List = new ArrayList<Long>();
					for (int i = 0; i < itemcList3.size(); i++) {
						cids3List.add(itemcList3.get(i).getCategoryCid());
					}
					Long[] cids3 = cids3List.toArray(new Long[] {});
					// 用三级类目组 查询旗下的商品
					ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
					if (cids3.length > 0) {
						itemQueryInDTO.setCids(cids3);
						itemQueryInDTO.setOperator(2);
						itemQueryInDTO.setItemStatus(4);
						itemQueryInDTO.setItemName(itemName);
						DataGrid<ItemQueryOutDTO> d3 = this.queryItemList(
								itemQueryInDTO, page);
						d3.getRows();
						list = d3.getRows();
						count += d3.getTotal();
					} else {
						list = null;
					}
				}
			} else {
				ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
				itemQueryInDTO.setOperator(2);
				itemQueryInDTO.setItemStatus(4);
				itemQueryInDTO.setItemName(itemName);
				DataGrid<ItemQueryOutDTO> d4 = this.queryItemList(
						itemQueryInDTO, page);
				d4.getRows();
				list = d4.getRows();
				count += d4.getTotal();
			}
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ExecuteResult<DataGrid<ItemQueryOutDTO>> queryItemByCid(Long cid,
			Pager page) {
		ExecuteResult<DataGrid<ItemQueryOutDTO>> result = new ExecuteResult<DataGrid<ItemQueryOutDTO>>();
		DataGrid<ItemQueryOutDTO> dataGrid = new DataGrid<ItemQueryOutDTO>();
		try {
			List<ItemQueryOutDTO> list = new ArrayList<ItemQueryOutDTO>();
			Long count = 0l;
			if (cid != null) {
				ItemCategoryDTO itemCategory = itemCategoryDAO.queryById(cid);
				if (itemCategory.getCategoryLev() == 3) {
					// 入过传入是三级类目 用三级类目查询商品
					ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
					itemInDTO.setCid(cid);
					itemInDTO.setOperator(2);
					itemInDTO.setItemStatus(4);
					DataGrid<ItemQueryOutDTO> d1 = this.queryItemList(
							itemInDTO, page);
					d1.getRows();
					list = d1.getRows();
					count += d1.getTotal();
				}
				if (itemCategory.getCategoryLev() == 2) {
					// 入过传入是2级类目 用二级类目查询下面的三级类目 用三级类目查询商品
					ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
					itemCategoryDTO.setCategoryParentCid(cid);
					List<ItemCategoryDTO> itemcList = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO, null);
					List<Long> idsList = new ArrayList<Long>();
					for (int i = 0; i < itemcList.size(); i++) {
						idsList.add(itemcList.get(i).getCategoryCid());
					}
					Long[] ids = idsList.toArray(new Long[] {});
					ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
					if (ids.length > 0) {
						itemQueryInDTO.setCids(ids);
						itemQueryInDTO.setOperator(2);
						itemQueryInDTO.setItemStatus(4);
						DataGrid<ItemQueryOutDTO> d2 = this.queryItemList(
								itemQueryInDTO, page);
						d2.getRows();
						list = d2.getRows();
						count += d2.getTotal();
					} else {
						list = null;
					}
					/*
					 * list = itemMybatisDAO.queryItemList(itemQueryInDTO,page);
					 * count+= itemMybatisDAO.queryCount(itemQueryInDTO);
					 */
				}
				if (itemCategory.getCategoryLev() == 1) {
					// 传入的是一级类目

					// 用一级类目查询旗下的二级类目
					ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
					itemCategoryDTO.setCategoryParentCid(cid);
					List<ItemCategoryDTO> itemcList2 = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO, null);
					List<Long> cids2List = new ArrayList<Long>();
					for (int i = 0; i < itemcList2.size(); i++) {
						cids2List.add(itemcList2.get(i).getCategoryCid());
					}
					Long[] cids2 = cids2List.toArray(new Long[] {});
					// 用二级类目数字 查询旗下的三级类目
					ItemCategoryDTO itemCategoryDTO2 = new ItemCategoryDTO();
					if (cids2.length > 0) {
						itemCategoryDTO2.setCategoryParentCids(cids2);
					}
					List<ItemCategoryDTO> itemcList3 = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO2, null);
					List<Long> cids3List = new ArrayList<Long>();
					for (int i = 0; i < itemcList3.size(); i++) {
						cids3List.add(itemcList3.get(i).getCategoryCid());
					}
					Long[] cids3 = cids3List.toArray(new Long[] {});
					// 用三级类目组 查询旗下的商品
					ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
					if (cids3.length > 0) {
						itemQueryInDTO.setCids(cids3);
						itemQueryInDTO.setOperator(2);
						itemQueryInDTO.setItemStatus(4);
						DataGrid<ItemQueryOutDTO> d3 = this.queryItemList(
								itemQueryInDTO, page);
						d3.getRows();
						list = d3.getRows();
						count += d3.getTotal();
					} else {
						list = null;
					}
				}
			} else {
				ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
				itemQueryInDTO.setOperator(2);
				itemQueryInDTO.setItemStatus(4);
				DataGrid<ItemQueryOutDTO> d4 = this.queryItemList(
						itemQueryInDTO, page);
				d4.getRows();
				list = d4.getRows();
				count += d4.getTotal();
			}
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ExecuteResult<DataGrid<ItemQueryOutDTO>> queryXgjItemByCid(
			ItemQueryInDTO inDTO, Pager page) {
		ExecuteResult<DataGrid<ItemQueryOutDTO>> result = new ExecuteResult<DataGrid<ItemQueryOutDTO>>();
		DataGrid<ItemQueryOutDTO> dataGrid = new DataGrid<ItemQueryOutDTO>();
		try {
			List<ItemQueryOutDTO> list = new ArrayList<ItemQueryOutDTO>();
			Long count = 0l;
			if (inDTO.getCid() != null) {
				ItemCategoryDTO itemCategory = itemCategoryDAO.queryById(inDTO
						.getCid());
				if (itemCategory.getCategoryLev() == 3) {
					// 入过传入是三级类目 用三级类目查询商品
					ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
					itemInDTO.setCid(inDTO.getCid());
					if (inDTO.getXgjFlag() != null) {
						itemInDTO.setXgjFlag(inDTO.getXgjFlag());
					}
					itemInDTO.setOperator(2);
					itemInDTO.setItemStatus(4);
					DataGrid<ItemQueryOutDTO> d1 = this.queryItemList(
							itemInDTO, page);
					d1.getRows();
					list = d1.getRows();
					count += d1.getTotal();
				}
				if (itemCategory.getCategoryLev() == 2) {
					// 入过传入是2级类目 用二级类目查询下面的三级类目 用三级类目查询商品
					ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
					itemCategoryDTO.setCategoryParentCid(inDTO.getCid());
					List<ItemCategoryDTO> itemcList = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO, null);
					List<Long> idsList = new ArrayList<Long>();
					for (int i = 0; i < itemcList.size(); i++) {
						idsList.add(itemcList.get(i).getCategoryCid());
					}
					Long[] ids = idsList.toArray(new Long[] {});
					ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
					if (ids.length > 0) {
						itemQueryInDTO.setCids(ids);
						if (inDTO.getXgjFlag() != null) {
							itemQueryInDTO.setXgjFlag(inDTO.getXgjFlag());
						}
						itemQueryInDTO.setOperator(2);
						itemQueryInDTO.setItemStatus(4);
						DataGrid<ItemQueryOutDTO> d2 = this.queryItemList(
								itemQueryInDTO, page);
						d2.getRows();
						list = d2.getRows();
						count += d2.getTotal();
					} else {
						list = null;
					}
					/*
					 * list = itemMybatisDAO.queryItemList(itemQueryInDTO,page);
					 * count+= itemMybatisDAO.queryCount(itemQueryInDTO);
					 */
				}
				if (itemCategory.getCategoryLev() == 1) {
					// 传入的是一级类目

					// 用一级类目查询旗下的二级类目
					ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
					itemCategoryDTO.setCategoryParentCid(inDTO.getCid());
					List<ItemCategoryDTO> itemcList2 = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO, null);
					List<Long> cids2List = new ArrayList<Long>();
					for (int i = 0; i < itemcList2.size(); i++) {
						cids2List.add(itemcList2.get(i).getCategoryCid());
					}
					Long[] cids2 = cids2List.toArray(new Long[] {});
					// 用二级类目数字 查询旗下的三级类目
					ItemCategoryDTO itemCategoryDTO2 = new ItemCategoryDTO();
					if (cids2.length > 0) {
						itemCategoryDTO2.setCategoryParentCids(cids2);
					}
					List<ItemCategoryDTO> itemcList3 = itemCategoryDAO
							.queryItemCategoryAllList(itemCategoryDTO2, null);
					List<Long> cids3List = new ArrayList<Long>();
					for (int i = 0; i < itemcList3.size(); i++) {
						cids3List.add(itemcList3.get(i).getCategoryCid());
					}
					Long[] cids3 = cids3List.toArray(new Long[] {});
					// 用三级类目组 查询旗下的商品
					ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
					if (cids3.length > 0) {
						itemQueryInDTO.setCids(cids3);
						if (inDTO.getXgjFlag() != null) {
							itemQueryInDTO.setXgjFlag(inDTO.getXgjFlag());
						}
						itemQueryInDTO.setOperator(2);
						itemQueryInDTO.setItemStatus(4);
						DataGrid<ItemQueryOutDTO> d3 = this.queryItemList(
								itemQueryInDTO, page);
						d3.getRows();
						list = d3.getRows();
						count += d3.getTotal();
					} else {
						list = null;
					}
				}
			} else {
				ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
				if (inDTO.getXgjFlag() != null) {
					itemQueryInDTO.setXgjFlag(inDTO.getXgjFlag());
				}
				itemQueryInDTO.setOperator(2);
				itemQueryInDTO.setItemStatus(4);
				itemQueryInDTO.setItemName(inDTO.getItemName());
				DataGrid<ItemQueryOutDTO> d4 = this.queryItemList(
						itemQueryInDTO, page);
				d4.getRows();
				list = d4.getRows();
				count += d4.getTotal();
			}
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<ItemQueryOutDTO>> queryItemByPlatItemId(
			Long itemId, Pager<Long> pager) {
		ExecuteResult<DataGrid<ItemQueryOutDTO>> result = new ExecuteResult<DataGrid<ItemQueryOutDTO>>();
		DataGrid<ItemQueryOutDTO> dg = new DataGrid<ItemQueryOutDTO>();
		ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
		itemInDTO.setPlatItemId(itemId);
		Pager<ItemQueryOutDTO> page = new Pager<ItemQueryOutDTO>();
		page.setRows(pager.getRows());
		page.setPage(pager.getPage());
		List<ItemQueryOutDTO> list = this.itemMybatisDAO.queryItemList(
				itemInDTO, page);
		dg.setTotal(this.itemMybatisDAO.queryCount(itemInDTO));
		dg.setRows(list);
		result.setResult(dg);
		return result;
	}

	@Override
	public ExecuteResult<String> modifyItemPlatStatus(List<Long> ids,
			Integer status) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if (ids == null || ids.size() <= 0) {
			result.addErrorMessage("参数为空！");
			return result;
		}
		this.itemMybatisDAO.updateItemPlatStatus(ids, status);
		return result;
	}

	@Override
	public ExecuteResult<String> modifyShopItemStatus(
			ItemStatusModifyDTO statusDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if (statusDTO == null) {
			result.addErrorMessage("参数为空！");
			return result;
		}
		if (statusDTO.getItemStatus() == null) {
			result.addErrorMessage("商品状态是空！");
			return result;
		}
		this.itemMybatisDAO.updateShopItemStatus(statusDTO);
		return result;
	}

	@Override
	public ExecuteResult<String> deleteItem(Long itemId) {
		ExecuteResult<String> result = new ExecuteResult<String>();

		ItemDTO item = this.itemMybatisDAO.getItemDTOById(itemId);
		if (item == null) {
			result.addErrorMessage("商品不存在！");
			return result;
		}
		// 在售 和 待审核的商品不可以删除
		if (ItemStatusEnum.SALING.getCode() == item.getItemStatus()
				|| ItemStatusEnum.AUDITING.getCode() == item.getItemStatus()) {
			result.addErrorMessage("商品是在售或者待审核状态，不能删除！");
			return result;
		}
		item.setItemStatus(ItemStatusEnum.DELETED.getCode());
		this.itemMybatisDAO.updateItem(item);
		return result;
	}

	@Override
	public ExecuteResult<String> addItemToPlat(Long itemId) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<Long> itemIds = new ArrayList<Long>();
		itemIds.add(itemId);
		ItemStatusModifyDTO dto = new ItemStatusModifyDTO();
		dto.setItemIds(itemIds);
		this.copyItemToPlat(dto);
		return result;
	}

	@Override
	public ExecuteResult<String> queryAttrBySkuId(Long skuId) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		ItemSku itemSku = itemSkuDAO.queryById(skuId);
		if (itemSku == null) {
			result.addErrorMessage("销售属性不存在！");
			return result;
		}
		result.setResult(itemSku.getAttributes());
		return result;
	}

	@Override
	public ExecuteResult<List<ItemDTO>> queryItemByAttr(String attr) {
		ExecuteResult<List<ItemDTO>> result = new ExecuteResult<List<ItemDTO>>();
		List<ItemDTO> dtos = new ArrayList<ItemDTO>();
		if (StringUtils.isBlank(attr) && attr.split(":").length < 2) {
			result.addErrorMessage("参数不正确！");
			return result;
		}
		try {
			List<ItemDTO> itemDTOs = itemMybatisDAO.queryItemDTOByAttr(attr);
			if (itemDTOs != null && itemDTOs.size() > 0) {
				System.out.println("-====================================="
						+ itemDTOs.size());
				String[] realKeyValues = attr.split(":");
				for (ItemDTO itemDTO : itemDTOs) {
					// 得到该商品的非销售属性
					String attributesStr = itemDTO.getAttributesStr();
					String[] keyVals = attributesStr.split(";");// xxxx:xxxx
					String[] strs = null;
					for (String str : keyVals) {
						strs = str.split(":");
						if (realKeyValues[0].equals(strs[0])
								&& realKeyValues[1].equals(strs[1])) {
							// 销售属性
							String attrSale = itemDTO.getAttrSaleStr();
							List<ItemAttr> attrSales = this
									.getItemAttrList(attrSale);
							itemDTO.setAttrSale(attrSales);
							// 非销售属性
							String attrStr = itemDTO.getAttributesStr();
							List<ItemAttr> attrs = this
									.getItemAttrList(attrStr);
							itemDTO.setAttributes(attrs);
							// 查询商品阶梯价
							List<SellPrice> sellPrices = this.itemPriceDAO
									.queryItemSellPrices(itemDTO.getItemId());
							itemDTO.setSellPrices(sellPrices);
							// 商品图片
							String[] picUrls = this.getItemPictures(itemDTO
									.getItemId());
							itemDTO.setPicUrls(picUrls);

							// 查询商品SKU 阶梯价 图片 库存 成本价
							List<SkuInfo> skus = this.getItemSkuList(itemDTO
									.getItemId());
							itemDTO.setSkuInfos(skus);
							dtos.add(itemDTO);
							break;
						}
					}
				}
			}
			result.setResult(dtos);
		} catch (Exception e) {
			LOGGER.error("执行方法【queryItemByAttr】报错:", e);
			result.addErrorMessage("执行方法【queryItemByAttr】报错:" + e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<List<ItemDTO>> queryItemByItemNameAndShopId(
			String itemName, Long shopId) {
		ExecuteResult<List<ItemDTO>> result = new ExecuteResult<List<ItemDTO>>();
		List<ItemDTO> itemDTOs = itemMybatisDAO.queryItemByItemNameAndShopId(
				itemName, shopId);
		result.setResult(itemDTOs);
		return result;
	}

	@Override
	public ExecuteResult<String> updateTimingListing(ItemDTO itemDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if (itemDTO.getItemId() == null) {
			result.addErrorMessage("商品id为空！");
			return result;
		}
		itemMybatisDAO.updateTimingListing(itemDTO);
		return result;
	}

	@Override
	public ExecuteResult<Integer> checkProductIdExist(String productId) {
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();

		Integer idExist = itemSkuDAO.queryProductIdExist(productId);

		if (idExist == null || idExist == 0) {
			return result;
		}
		
		result.addErrorMessage("商品编号已经存在！");
		return result;
	}

	@Override
	public List<ItemDTO> getItemDTOByItemIds(Long[] iids) {
		return this.itemMybatisDAO.getItemDTOByItemIds(iids);
	}

	@Override
	public ExecuteResult<ItemDTO> getItemBySkuId(Long skuId) {

		ExecuteResult<ItemDTO> result = new ExecuteResult<ItemDTO>();
		// 根据skuId查询Sku信息
		ItemSku itemSku = itemSkuDAO.queryById(skuId);
		if (itemSku == null) {
			result.addErrorMessage("不存在此skuID");
			return result;
		}
		Long itemId = itemSku.getItemId();
		result = this.getItemById(itemId);
		return result;
	}

	@Override
	public ExecuteResult<String> updatePlacedTop(List<Long> items, Integer placedTop) {
		ExecuteResult<String> execResult = new ExecuteResult<String>();
		if (CollectionUtils.isEmpty(items)) {
			execResult.addErrorMessage("请选择至少一个商品");
		}
		try {
			int updatedRows = itemMybatisDAO.updatePlacedTop(items, placedTop);
			if (updatedRows > 0) {
				execResult.setResultMessage("success");
			} else {
				execResult.addErrorMessage("操作失败");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			execResult.addErrorMessage("操作失败");
		}
		
		return execResult;
	}
	@Override
	public ExecuteResult<List<Long>> findItemsByMrkDownPrice(Long[] itemIds, BigDecimal markdownprice) {
		ExecuteResult<List<Long>> result = new ExecuteResult<List<Long>>();
		List<Long> lists=new ArrayList<Long>();
		if(itemIds==null||itemIds.length==0){
			result.addErrorMessage("商品集合id不能为空。");
		}
		if(markdownprice == null){
			result.addErrorMessage("直降金额不能为空。");
		 }
	   outer:for(int i=0;i<itemIds.length;i++){
	     try {
	        List<SkuInfo> SkuInfos = this.itemMybatisDAO.queryItemSkuInfo(itemIds[i]);
			for(SkuInfo sku:SkuInfos){
				// 阶梯价
				List<SellPrice> sellPrices = this.itemPriceService.querySkuSellPrices(sku.getSkuId());
				for(SellPrice sellprice:sellPrices){
					if(sellprice.getSellPrice().compareTo(markdownprice)<0){
						lists.add(itemIds[i]);
						continue outer;
					}
				}
			 } 
		  }catch (Exception e) {
			e.printStackTrace();
		  }
		}
		result.setResult(lists);
		return result;
	}
}
