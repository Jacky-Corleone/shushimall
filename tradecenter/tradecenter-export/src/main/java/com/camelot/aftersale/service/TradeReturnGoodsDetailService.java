package com.camelot.aftersale.service;


import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import java.util.Date;
import java.util.List;

/**
 *  退货单详情接口 供TradeReturnExportService调用，不对外暴露接口
 * 
 * @Description -
 */
public interface TradeReturnGoodsDetailService {


    /**
     * 创建对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> createTradeReturnGoodsDetailDTO(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail);

    /**
     * 更新对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> updateTradeReturnGoodsDetailDTO(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail);

    /**
     * 根据ID删除对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> deleteById(Long tradeReturnGoodsDetailId);

    /**
     * 删除所有对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> deleteAll(List<Long> idList);

    /**
     * 分页查询
     */
    public DataGrid<TradeReturnGoodsDetailDTO> searchTradeReturnGoodsDetailDTOs(Pager<TradeReturnGoodsDetailDTO> pager, TradeReturnGoodsDetailDTO tradeReturnGoodsDetail);

    /**
     * 根据ID查询对象
     */
    public TradeReturnGoodsDetailDTO getTradeReturnGoodsDetailDTOById(Long tradeReturnGoodsDetailId);

    /**
     * 更新对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> updateSelective(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail);

    /**
     * 根据时间戳更新对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> updateSelectiveWithDateTimeCheck(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail, Date prevUpdDt) ;

    /**
     * 根据条件查询
     */
    public List<TradeReturnGoodsDetailDTO> searchByCondition(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail);

    /**
     * 根据IDLIST 更新对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> updateSelectiveByIdList(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail, List<Long> idList);

    /**
     * 根据ID更新所有数据
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> updateAllByIdList(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail, List<Long> idList);

    /**
     * 逻辑删除对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> defunctById(Long tradeReturnGoodsDetailId);

    /**
     * 批量逻辑删除对象
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> defunctByIdList(List<Long> idList);
}
