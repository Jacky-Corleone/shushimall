package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;



import com.camelot.goodscenter.dto.TradeInventoryDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface TradeInventoryDAO extends BaseDAO<TradeInventoryInDTO> {

	/**
	 * <p>Discription:[根据skuId查询库存]</p>
	 * Created on 2015年3月18日
	 * @param skuId
	 * @return
	 * @author:[杨芳]
	 */
 public TradeInventoryDTO queryBySkuId(Long skuId);
  /**
   * <p>Discription:[根据条件查询库存列表]</p>
   * Created on 2015年3月18日
   * @param tradeInventoryInDTO
   * @param page
   * @return
   * @author:[杨芳]
   */
  public List<TradeInventoryOutDTO> queryTradeInventoryList(@Param("entity")TradeInventoryInDTO tradeInventoryInDTO,@Param("page")Pager page);
 
  /**
   * <p>Discription:[根据skuId查询商品的名称、状态]</p>
   * Created on 2015年3月18日
   * @param skuId
   * @return
   * @author:[杨芳]
   */
  public TradeInventoryOutDTO queryItemBySkuId(Long skuId);
  
 /**
  * <p>Discription:[根据skuIds批量修改库存量]</p>
  * Created on 2015年3月18日
  * @param skuId
  * @param inventory
  * @return
  * @author:[杨芳]
  */
  public Integer modifyInventoryBySkuIds(@Param("skuid")Long skuId,@Param("inventory")Integer inventory);
}
