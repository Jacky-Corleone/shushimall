package com.camelot.aftersale.service;


import java.util.List;
import java.util.Map;

import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoQueryDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.tradecenter.dto.TradeOrdersDTO;

/**
 *  对外暴露退货退款接口
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-5-6
 */
public interface TradeReturnExportService {
    /**
     *  创建退货单 已经退货商品明细 传入参数退货单信息 退货商品LIST
     * @param insertDto
     * @return
     */
    public ExecuteResult<TradeReturnGoodsDTO> createTradeReturn(TradeReturnInfoDto insertDto,TradeReturnStatusEnum status) throws CommonCoreException;

    /**
     * 修改退货单 退款或退货
     * @param dto
     * @param status
     * @return
     */

    public ExecuteResult<TradeReturnGoodsDTO> updateTradeReturnStatus(TradeReturnGoodsDTO dto,TradeReturnStatusEnum status);

    /**
     * 查询 退货单列表
     * @param queryDto
     * @return
     */
    public DataGrid<TradeReturnGoodsDTO> getTradeReturnInfoDto(Pager<TradeReturnGoodsDTO> pager, TradeReturnInfoQueryDto queryDto);

    /**
     * 根据退货单 号查询退货商品明细
     * @param returnGoodsId
     * @return
     */
    public List<TradeReturnGoodsDetailDTO> getTradeReturnGoodsDetailReturnId(String returnGoodsId);

    /**
     * 查询退货单以 及退货单商品明细
     * @return
     */
    public ExecuteResult<TradeReturnInfoDto> getTradeReturnInfoByReturnGoodsId(String returnGoodId);

    /**
     * 根据查询条件查询 退货单明细
     * @return
     */
    public ExecuteResult<List<TradeReturnGoodsDetailDTO>>  getTradeReturnGoodsDetaiByCondition(TradeReturnGoodsDetailDTO dto);

    /**
     * 更新 退货单图片信息
     * @param dto
     * @return
     */
    public ExecuteResult<TradeReturnPicDTO> updateTradeReturnPicDTO(TradeReturnPicDTO dto);
    /**
     * 
     * <p>Discription:[更新退货单]</p>
     * Created on 2015-9-28
     * @param dto
     * @return
     */
    public ExecuteResult<TradeReturnGoodsDTO> updateTradeReturnGoods(TradeReturnGoodsDTO dto);
    /**
     * 
     * <p>Discription:[退款异步回调]</p>
     * @author zhouzhijun
     * @since 2015-10-13
     * @param params 异步参数
     * @param payBank 
     * @return
     */
    public ExecuteResult<Integer> refundResult(Map<String,String> params,String payBank);
    
    /**
	 * 根据单据编号查询退款记录
	 * 
	 * @return
	 */
    public ExecuteResult<RefundPayParam> findRefInfoByRepro(String rePro) ;
    /**
     * 更新退货详细信息
     */
    public ExecuteResult<TradeReturnGoodsDetailDTO> updateTradeReturnGoodsDetailDTO(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail);
    
    /**
     * 根据订单ID判断该订单下的商品是否已经全部退款
     * 
     * @param orderItem 订单
     * @return
     */
    public boolean isAllItemReturn(TradeOrdersDTO orderItem,Integer... state);
    
    /**
     * 
     * <p>Discription:[根据订单ID查询该订单下商品的退款数量、积分退还数量、金额退还数量]</p>
     * Created on 2015年12月30日
     * @param orderItem
     * @param state
     * @return key：itemReturnNum：商品的退款数量；integralReturnNum：积分退还数量；returnAmount：金额退还数量
     * @author:[宋文斌]
     */
    public ExecuteResult<Map<String,Object>> getReturnNum(TradeOrdersDTO orderItem, Integer... state);
    
    /**
     * 
     * <p>Discription:[获取订单下面的所有商品个数]</p>
     * Created on 2015年12月30日
     * @param orderItem
     * @return
     * @author:[宋文斌]
     */
    public ExecuteResult<Integer> getOrderItemNum(TradeOrdersDTO orderItem);
    /**
     * 
     * <p>Discription:[通过退款单id退还平台给卖家结算的钱]</p>
     * Created on 2016-1-8
     * @param returnId
     * @return
     * @author:[王鹏]
     */
    public ExecuteResult<String> calTotalRefundGoods(String returnId);
    
    /**
     * 
     * <p>Discription:[计算积分退还]</p>
     * Created on 2016-1-8
     * @param returnId
     * @return
     * @author:[宋文斌]
     */
    public ExecuteResult<String> calcRefundIntegral(String returnId);
    
    /**
     * 
     * <p>Discription:[有密退款支付宝回调方法]</p>
     * Created on 2016年2月29日
     * @param params
     * @param payBank
     * @return
     * @author:[李伟龙]
     */
    public ExecuteResult<Integer> apRefundResult(Map<String,String> params,String payBank);
}
