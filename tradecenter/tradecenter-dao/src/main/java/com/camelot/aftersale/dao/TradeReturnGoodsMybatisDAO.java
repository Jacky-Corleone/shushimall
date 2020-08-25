package com.camelot.aftersale.dao;

import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.openplatform.common.Pager;
import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import java.util.Date;
import java.util.List;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface TradeReturnGoodsMybatisDAO  extends BaseDAO<TradeReturnGoodsDTO>{
	public void deleteAll(@Param("idList") List<Long> idList);
	public int updateSelective(@Param("tradeReturnGoods") TradeReturnGoodsDTO tradeReturnGoods);
	public int updateAllWithDateTimeCheck(@Param("tradeReturnGoodsDTO") TradeReturnGoodsDTO tradeReturnGoods, @Param("prevUpdDt") Date prevUpdDt);
	public int updateSelectiveWithDateTimeCheck(@Param("tradeReturnGoodsDTO") TradeReturnGoodsDTO tradeReturnGoods, @Param("prevUpdDt") Date prevUpdDt);
	public List<TradeReturnGoodsDTO> searchByCondition(@Param("entity") TradeReturnGoodsDTO tradeReturnGoods);
	public List<TradeReturnGoodsDTO> searchByOrderIdAndState(@Param("orderId") String orderId,@Param("states") Integer... state);
	public long searchByConditionCount(@Param("tradeReturnGoods") TradeReturnGoodsDTO tradeReturnGoods);
	public List<TradeReturnGoodsDTO> searchByConditionByPager(@Param("pager") Pager<TradeReturnGoodsDTO> pager, @Param("tradeReturnGoodsDTO") TradeReturnGoodsDTO tradeReturnGoods);
	public long updateSelectiveByIdList(@Param("tradeReturnGoodsDTO") TradeReturnGoodsDTO tradeReturnGoods, @Param("idList") List<Long> idList);
	public long updateAllByIdList(@Param("tradeReturnGoodsDTO") TradeReturnGoodsDTO tradeReturnGoods, @Param("idList") List<Long> idList);

    public String getCodeNo();
    /**
     * 根据退货单号更新tradereturngoods
     * @return
     */
    public int updateByCodeNo(@Param("tradeReturnGoods") TradeReturnGoodsDTO tradeReturnGoods);
}