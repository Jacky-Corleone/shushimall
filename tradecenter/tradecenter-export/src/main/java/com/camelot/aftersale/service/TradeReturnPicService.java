package com.camelot.aftersale.service;

import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import java.util.Date;
import java.util.List;
/**
 *  退货单图片接口 供TradeReturnExportService调用，不对外暴露接口
 * 
 * @Description -
 */
public interface TradeReturnPicService {


    /**
     * 创建对象
     */
    public ExecuteResult<TradeReturnPicDTO> createTradeReturnPicDTO(TradeReturnPicDTO tradeReturnPic);

    /**
     * 更新对象
     */
    public ExecuteResult<TradeReturnPicDTO> updateTradeReturnPicDTO(TradeReturnPicDTO tradeReturnPic);

    /**
     * 根据ID删除对象
     */
    public ExecuteResult<TradeReturnPicDTO> deleteById(Long tradeReturnPicId);

    /**
     * 删除所有对象
     */
    public ExecuteResult<TradeReturnPicDTO> deleteAll(List<Long> idList);

    /**
     * 分页查询
     */
    public DataGrid<TradeReturnPicDTO> searchTradeReturnPicDTOs(Pager<TradeReturnPicDTO> pager, TradeReturnPicDTO tradeReturnPic);

    /**
     * 根据ID查询对象
     */
    public TradeReturnPicDTO getTradeReturnPicDTOById(Long tradeReturnPicId);

    /**
     * 更新对象
     */
    public ExecuteResult<TradeReturnPicDTO> updateSelective(TradeReturnPicDTO tradeReturnPic);

    /**
     * 根据时间戳更新对象
     */
    public ExecuteResult<TradeReturnPicDTO> updateSelectiveWithDateTimeCheck(TradeReturnPicDTO tradeReturnPic, Date prevUpdDt);

    /**
     * 根据条件查询
     */
    public List<TradeReturnPicDTO> searchByCondition(TradeReturnPicDTO tradeReturnPic);

    /**
     * 根据IDLIST 更新对象
     */
    public ExecuteResult<TradeReturnPicDTO> updateSelectiveByIdList(TradeReturnPicDTO tradeReturnPic, List<Long> idList);

    /**
     * 根据ID更新所有数据
     */
    public ExecuteResult<TradeReturnPicDTO> updateAllByIdList(TradeReturnPicDTO tradeReturnPic, List<Long> idList);

    /**
     * 逻辑删除对象
     */
    public ExecuteResult<TradeReturnPicDTO> defunctById(Long tradeReturnPicId);

    public ExecuteResult<String> defunctByReturnGoodsId(Long returnGoodsId);

    /**
     * 批量逻辑删除对象
     */
    public ExecuteResult<TradeReturnPicDTO> defunctByIdList(List<Long> idList);
}
