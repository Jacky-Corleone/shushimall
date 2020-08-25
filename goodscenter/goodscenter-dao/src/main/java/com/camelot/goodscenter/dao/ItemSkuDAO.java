package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.ItemSku;
import com.camelot.goodscenter.domain.ItemSkuPicture;
import com.camelot.goodscenter.domain.TradeInventory;
import com.camelot.goodscenter.domain.TradeSkuPrice;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemSkuDAO extends BaseDAO<ItemSku>{

	/**
	 * 
	 * <p>Discription:[获取SKU编码]</p>
	 * Created on 2015-3-13
	 * @return
	 * @author:wangcs
	 */
	Long getSkuId();

	/**
	 * 
	 * <p>Discription:[插入SKU图片]</p>
	 * Created on 2015-3-14
	 * @param skuPic
	 * @author:wangcs
	 */
	void insertSkuPicture(ItemSkuPicture skuPic);

	/**
	 * 
	 * <p>Discription:[插入SKU区域阶梯价]</p>
	 * Created on 2015-3-14
	 * @param skuPrice
	 * @author:wangcs
	 */
	void insertSkuPrice(TradeSkuPrice skuPrice);

	/**
	 * 
	 * <p>Discription:[插入SKU价格日志]</p>
	 * Created on 2015-3-14
	 * @param skuPrice
	 * @author:wangcs
	 */
	void insertSkuPriceLog(TradeSkuPrice skuPrice);

	/**
	 * 
	 * <p>Discription:[插入SKU库存]</p>
	 * Created on 2015-3-14
	 * @param inv
	 * @author:wangcs
	 */
	void insertItemSkuInventory(TradeInventory inv);

	/**
	 * 
	 * <p>Discription:[删除SKU图片]</p>
	 * Created on 2015-3-17
	 * @param skuId
	 * @author:wangcs
	 */
	void deleteSkuPictures(@Param("skuId") Long skuId);

	/**
	 * 
	 * <p>Discription:[删除SKU]</p>
	 * Created on 2015-3-17
	 * @param deleteSkus
	 * @author:wangcs
	 */
	void deleteSkuById(@Param("skuId") Long skuId);

	/**
	 * 
	 * <p>Discription:[查询SKU库存]</p>
	 * Created on 2015-3-18
	 * @param skuId
	 * @return
	 * @author:wangcs
	 */
	Integer querySkuInventory(Long skuId);

	/**
	 * 
	 * <p>Discription:[删除SKU阶梯价格]</p>
	 * Created on 2015-3-19
	 * @param skuId
	 * @author:wangcs
	 */
	void deleteSkuSellPrices(@Param("skuId") Long skuId);

	/**
	 * 
	 * <p>Discription:[更新SKU库存]</p>
	 * Created on 2015-3-25
	 * @param inv
	 * @author:wangcs
	 */
	void updateItemSkuInventory(@Param("entity") TradeInventory inv);

	/**
	 * <p>Discription:[查询SKU商品编号是否存在]</p>
	 * 
	 * @param productId
	 * @return
	 */
	Integer queryProductIdExist(String productId);
}
