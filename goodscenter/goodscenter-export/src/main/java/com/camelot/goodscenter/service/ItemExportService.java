package com.camelot.goodscenter.service;

import java.math.BigDecimal;
import java.util.List;

import com.camelot.goodscenter.dto.ItemAdDTO;
import com.camelot.goodscenter.dto.ItemBaseDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemShopCidDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.dto.SkuPictureDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [描述该类概要功能介绍 :商品信息的接口类 Item]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ItemExportService {
	/**
	 * <p>Discription:[方法功能中文描述:商品信息管理的批量操作 （修改商品的状态）]</p>
	 * Created on 2015年2月4日
	 * @param ids id组
	 * @param changeReason 下架或锁定或审核驳回时给出的理由
	 * @param itemStatus 商品的状态
	 * @return
	 * @author:[chenx]
	 */
	public ExecuteResult<String> modifyItemStatusBatch(ItemStatusModifyDTO statusDTO);
	
	/**
	 * 
	 * <p>Discription:[
	 * 按照店铺ID修改商品状态
	 * ]</p>
	 * Created on 2015-4-20
	 * @param statusDTO  shopId  status changeReason必填
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<String> modifyShopItemStatus(ItemStatusModifyDTO statusDTO);
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询商品的信息列表]</p>
	 * Created on 2015年2月4日
	 * @param itemInDTO 入参
	 * @param page 分页
	 * @return
	 * @author:[创建者中文名字]
	 */
	public DataGrid<ItemQueryOutDTO> queryItemList(ItemQueryInDTO itemInDTO, Pager page);
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询商品的信息列表]</p>  加入了选择每页的显示条数 例每页显示50条-->10 默认当前页变成首页，10-->50 按自然顺序显示
	 * Created on 2015年12月30日
	 * @param itemInDTO 入参
	 * @param page 分页
	 * @return
	 * @author:[创建者中文名字]
	 */
	public DataGrid<ItemQueryOutDTO> queryItemListFlag(ItemQueryInDTO itemInDTO, Pager page,Integer oldRows);
    /**
     * 
     * <p>Discription:[方法功能中文描述:根据id查询商品详情]</p>
     * Created on 2015年2月4日
     * @param itemId
     * @return
     * @author:[wangcs]
     */
	public ExecuteResult<ItemDTO> getItemById(Long itemId);
	/**
	 * 根据skuId 查询商品详情信息
	 * @param skuId
	 * @return
	 * @author 王东晓
	 */
	public ExecuteResult<ItemDTO> getItemBySkuId(Long skuId);
	
	
	
	/**
     * 
     * <p>Discription:[查询商品基础数据【商品主表数据】]</p>
     * Created on 2015年11月30日
     * @param itemId
     * @return
     * @author:[zhangzq]
     */
	public ExecuteResult<ItemBaseDTO> getItemBaseInfoById(Long itemId);
	
	/**
	 * <p>Discription:[
	 * 商品信息添加<br>
	 * 平台调用：operator:2 平台<br>
	 * 		暂存：itemStatus:4 在售;  platLinStatus:1 未符合待入库; <br>
	 * 		发布：itemStatus:4 在售;  platLinStatus:3 已入库; <br>
	 * 卖家调用：operator:1 卖家<br>
	 * 		1)从平台库选择：addSource:2 <br>
	 * 			暂存：itemStatus:1 未发布; plstItemId:从平台库选择的商品ID<br>
	 * 			发布：itemStatus:2 待审核; plstItemId:从平台库选择的商品ID<br>
	 * 		2)自定义添加：addSource:1<br>
	 * 			暂存：itemStatus:1 未发布; <br>
	 * 			发布：itemStatus:2 待审核; <br>
	 * 		
	 * ]</p>
	 * Created on 2015年2月5日
	 * @param itemDTO
	 * @return
	 * @author:[wangcs]
	 */
	public ExecuteResult<ItemDTO> addItemInfo(ItemDTO itemDTO);
	
	/**
	 * 
	 * <p>Discription:[根据ID更改商品信息]</p>
	 * Created on 2015年2月5日
	 * @param itemId
	 * @return
	 * @author:[wangcs]
	 */
	public ExecuteResult<ItemDTO> modifyItemById(ItemDTO itemDTO);
	
	/**
	 * 
	 * <p>Discription:[
	 * 查询商品信息，可分页，分页对象传空，查询所有
	 * ]</p>
	 * Created on 2015-3-19
	 * @param itemDTO
	 * @return
	 * @author:wangcs
	 */
//	public DataGrid<ItemDTO> queryItemDTOList(SearchInDTO inDTO,Pager<ItemDTO> pager);
	
	/**
	 * 
	 * <p>Discription:[查询组合商品信息用于购物车展示]</p>
	 * Created on 2016年2月17日
	 * @param skuDTO 店铺ID，商品ID，skuId，地域编码
	 * @return 返回商品名称 SKU价格 skuId SKU属性
	 * @author:[宋文斌]
	 */
	public ExecuteResult<ItemShopCartDTO> getCombinationSkuShopCart(ItemShopCartDTO skuDTO);
	
	/**
	 * 
	 * <p>Discription:[查询商品信息用于购物车展示]</p>
	 * Created on 2015-3-4
	 * @param skuDto 店铺ID，商品ID，skuId，地域编码
	 * @return 返回商品名称 SKU价格 skuId SKU属性
	 * @author:wangcs
	 */
	public ExecuteResult<ItemShopCartDTO> getSkuShopCart(ItemShopCartDTO skuDto);
	
	/**
	 * 
	 * <p>Discription:[批量修改商品广告语]</p>
	 * Created on 2015-3-10
	 * @param ads
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<String> modifyItemAdBatch(List<ItemAdDTO> ads);
	
	/**
	 * 
	 * <p>Discription:[批量修改商品店铺分类]</p>
	 * Created on 2015-3-10
	 * @param ads
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<String> modifyItemShopCidBatch(List<ItemShopCidDTO> ads);
	

	/**
	 * 
	 * <p>Discription:[根据Cid查询商品信息]</p>
	 * Created on 2015-3-19
	 * @param cid
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ItemQueryOutDTO>>  queryItemByCid(Long cid,Pager page);
	
	/**
	 * 
	 * <p>Discription:[根据商品名称，Cid查询商品信息]</p>
	 * Created on 2015-3-19
	 * @param cid
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ItemQueryOutDTO>> queryItemByCidAndName(Long cid, String itemName, Pager page);

	/**
	 *
	 * <p>Discription:[询价查询商品信息]</p>
	 * Created on 2015-3-19
	 * @param cid
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ItemQueryOutDTO>>  queryXgjItemByCid(ItemQueryInDTO inDTO,Pager page);

	/**
	 * 
	 * <p>Discription:[根据平台商品ID 查询卖该商品的店铺]</p>
	 * Created on 2015-3-20
	 * @param itemId
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<DataGrid<ItemQueryOutDTO>> queryItemByPlatItemId(Long itemId,Pager<Long> pager);
	
	/**
	 * 
	 * <p>Discription:[修改平台商品库商品入库状态]</p>
	 * Created on 2015-3-24
	 * @param ids
	 * @param status  plat_link_status 与平台商品库关联状态：1：未符合待入库2：待入库3：已入库4：删除
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<String> modifyItemPlatStatus(List<Long> ids,Integer status);
	
	/**
	 * 
	 * <p>Discription:[
	 * 删除商品
	 * 除在售 和 待审核的商品都可以删除
	 * ]</p>
	 * Created on 2015-5-6
	 * @param itemId
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<String> deleteItem(Long itemId);
	
	/**
	 * 
	 * <p>Discription:[将商品添加到平台商品库]</p>
	 * Created on 2015-6-1
	 * @param itemId
	 * @return3
	 * @author:wangcs
	 */
	public ExecuteResult<String> addItemToPlat(Long itemId);
	
	/**
	 * 
	 * <p>Description: [根据skuId查询销售属性]</p>
	 * Created on 2015年8月13日
	 * @param skuId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<String> queryAttrBySkuId(Long skuId);
	
	/**
	 * 
	 * <p>Description: [根据平台的非销售属性（attributes）查询商品信息]</p>
	 * 只能根据一对key-value查询，不支持多个.
	 * Created on 2015年8月16日
	 * @param attr 非销售属性的key-value形式。例如：“1653:2641”
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<List<ItemDTO>> queryItemByAttr(String attr);
	
	/**
	 * 
	 * <p>Description: [根据商品名称和店铺ID查询商品,非模糊查询]</p>
	 * Created on 2015年8月17日
	 * @param itemName
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<List<ItemDTO>> queryItemByItemNameAndShopId(String itemName,Long shopId);
	
	/**
	 * 
	 * <p>Description: [查询SKU图片]</p>
	 * Created on 2015年9月29日
	 * @param skuId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<List<SkuPictureDTO>> querySkuPics(Long skuId);
	
	/**
	 * 
	 * <p>Discription:[更新上架时间，为空时更新调此方法]</p>
	 * Created on 2015-10-30
	 * @param ItemDTO
	 * @author:[王鹏]
	 */
	public ExecuteResult<String>  updateTimingListing(ItemDTO ItemDTO);

	/**
	 * <p>Discription:[检查商品编号是否已经存在]</p>
	 * 
	 * @param productId
	 * @return
	 */
	public ExecuteResult<Integer> checkProductIdExist(String productId);
	
	/**
	 * <p>Discription:[根据itemId 串，查询对应的item 数据信息]</p>
	 * Created on 2015年11月3日
	 * @param iids
	 * @return
	 * @author:[王鹏]
	 */
	public List<ItemDTO> getItemDTOByItemIds(Long[] iids);
	
	
	/**
	 * <p>更新商品的是否置顶状态</p>
	 * Created on 2016年2月22日
	 * @param items 一个或多个itemId
	 * @param placedTop 置顶
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<String> updatePlacedTop(List<Long> items, Integer placedTop);
	/**
	 * 
	 * <p>Discription:[商品id集合查询小于直降金额的商品]</p>
	 * Created on 2016年1月28日
	 * @param itemIds
	 * @param markdownprice
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<List<Long>> findItemsByMrkDownPrice(Long[] itemIds,BigDecimal markdownprice);
}
