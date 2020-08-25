package com.camelot.aftersale.dao;

import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface TradeReturnGoodsDetailMybatisDAO  extends BaseDAO<TradeReturnGoodsDetailDTO>{
	public void deleteAll(@Param("idList") List<Long> idList);
	public int updateSelective(@Param("tradeReturnGoodsDetail") TradeReturnGoodsDetailDTO tradeReturnGoodsDetail);
	public int updateAllWithDateTimeCheck(@Param("tradeReturnGoodsDetailDTO") TradeReturnGoodsDetailDTO tradeReturnGoodsDetail, @Param("prevUpdDt") Date prevUpdDt);
	public int updateSelectiveWithDateTimeCheck(@Param("tradeReturnGoodsDetailDTO") TradeReturnGoodsDetailDTO tradeReturnGoodsDetail, @Param("prevUpdDt") Date prevUpdDt);
	public List<TradeReturnGoodsDetailDTO> searchByCondition(@Param("entity") TradeReturnGoodsDetailDTO tradeReturnGoodsDetail);
	public long updateSelectiveByIdList(@Param("tradeReturnGoodsDetailDTO") TradeReturnGoodsDetailDTO tradeReturnGoodsDetail, @Param("idList") List<Long> idList);
	public long updateAllByIdList(@Param("tradeReturnGoodsDetailDTO") TradeReturnGoodsDetailDTO tradeReturnGoodsDetail, @Param("idList") List<Long> idList);
}