package com.camelot.aftersale.dao;

import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
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
public interface TradeReturnPicMybatisDAO  extends BaseDAO<TradeReturnPicDTO>{
	public void deleteAll(@Param("idList") List<Long> idList);
	public int updateSelective(@Param("tradeReturnPic") TradeReturnPicDTO tradeReturnPic);
	public int updateAllWithDateTimeCheck(@Param("tradeReturnPic") TradeReturnPicDTO tradeReturnPic, @Param("prevUpdDt") Date prevUpdDt);
	public int updateSelectiveWithDateTimeCheck(@Param("tradeReturnPic") TradeReturnPicDTO tradeReturnPic, @Param("prevUpdDt") Date prevUpdDt);
	public List<TradeReturnPicDTO> searchByCondition(@Param("entity") TradeReturnPicDTO tradeReturnPic);
	public long updateSelectiveByIdList(@Param("tradeReturnPic") TradeReturnPicDTO tradeReturnPic, @Param("idList") List<Long> idList);
	public long updateAllByIdList(@Param("tradeReturnPic") TradeReturnPicDTO tradeReturnPic, @Param("idList") List<Long> idList);
    public Integer updateAllByReturnGoodsId(@Param("tradeReturnPic") TradeReturnPicDTO tradeReturnPic);
}