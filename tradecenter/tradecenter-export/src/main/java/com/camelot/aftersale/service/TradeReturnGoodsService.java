package com.camelot.aftersale.service;


import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import java.util.Date;
import java.util.List;
/**
 *  退货单接口 供TradeReturnExportService调用，不对外暴露接口
 * 
 * @Description -
 */
public interface TradeReturnGoodsService {

    public String getCodeNo();

    /**
     * 创建对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> createTradeReturnGoodsDTO(TradeReturnGoodsDTO tradeReturnGoods);

    /**
     * 更新对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> updateTradeReturnGoodsDTO(TradeReturnGoodsDTO tradeReturnGoods);

    /**
     * 根据ID删除对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> deleteById(Long tradeReturnGoodsId);

    /**
     * 删除所有对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> deleteAll(List<Long> idList);

    /**
     * 分页查询
     */
    public DataGrid<TradeReturnGoodsDTO> searchTradeReturnGoodsDTOs(Pager<TradeReturnGoodsDTO> pager, TradeReturnGoodsDTO tradeReturnGoods);

    /**
     * 根据ID查询对象
     */
    public TradeReturnGoodsDTO getTradeReturnGoodsDTOById(Long tradeReturnGoodsId);

    /**
     * 更新对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> updateSelective(TradeReturnGoodsDTO tradeReturnGoods);

    /**
     * 根据时间戳更新对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> updateSelectiveWithDateTimeCheck(TradeReturnGoodsDTO tradeReturnGoods, Date prevUpdDt);

    /**
     * 根据条件查询
     */
    public List<TradeReturnGoodsDTO> searchByCondition(TradeReturnGoodsDTO tradeReturnGoods);
    
    /**
     * 根据订单和退货单状态查询退货单
     */
    public List<TradeReturnGoodsDTO> searchByOrderIdAndState(String orderId , Integer... state );
    /**
     * 根据IDLIST 更新对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> updateSelectiveByIdList(TradeReturnGoodsDTO tradeReturnGoods, List<Long> idList);

    /**
     * 根据ID更新所有数据
     */
    public ExecuteResult<TradeReturnGoodsDTO> updateAllByIdList(TradeReturnGoodsDTO tradeReturnGoods, List<Long> idList);

    /**
     * 逻辑删除对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> defunctById(Long tradeReturnGoodsId);

    /**
     * 批量逻辑删除对象
     */
    public ExecuteResult<TradeReturnGoodsDTO> defunctByIdList(List<Long> idList);
}
