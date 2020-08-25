package com.camelot.goodscenter.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.ItemPrice;
import com.camelot.goodscenter.domain.PriceQueryParam;
import com.camelot.goodscenter.dto.SellPrice;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemPriceDAO extends BaseDAO<ItemPrice> {

	/**
	 * 
	 * <p>Discription:[根据商品ID查询所有阶梯价]</p>
	 * Created on 2015-3-6
	 * @param itemId
	 * @return
	 * @author:wangcs
	 */
	public List<SellPrice> queryItemSellPrices(@Param("id") Long itemId);

	/**
	 * 
	 * <p>Discription:[根据SKUid 区域编码  数量 查询 SKU价格]</p>
	 * Created on 2015-3-6
	 * @param skuId
	 * @param areaCode
	 * @param qty
	 * @return
	 * @author:wangcs
	 */
	public BigDecimal getSkuAreaPrice(@Param("param") PriceQueryParam param);

	/**
	 * 
	 * <p>Discription:[根据SKUid 查询SKU的所有地域阶梯价格]</p>
	 * Created on 2015-3-7
	 * @param skuId
	 * @return
	 * @author:wangcs
	 */
	public List<SellPrice> querySkuSellPrices(@Param("id") Long skuId);

	/**
	 * 
	 * <p>Discription:[根据商品ID 区域编码  数量 查询商品价格]</p>
	 * Created on 2015-3-7
	 * @param param
	 * @return
	 * @author:wangcs
	 */
	public BigDecimal getItemPrice(@Param("param") PriceQueryParam param);

	/**
	 * 
	 * <p>Discription:[根据商品ID删除商品阶梯价]</p>
	 * Created on 2015-3-17
	 * @param itemId
	 * @author:wangcs
	 */
	public void deleteItemPrices(@Param("itemId") Long itemId);
}
