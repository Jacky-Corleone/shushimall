package com.camelot.aftersale.service;


import java.math.BigDecimal;

import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 *  退款接口 供TradeReturnExportService调用，不对外暴露接口
 * 
 * @Description -
 */
public interface TradeRefundService {
	
	/**
	 * 退款申请，只做记录处理
	 * 
	 * @param refundPayParam 
	 * @param orderNo 订单号,buyerId,reproNo,refundAmount,orderPayBank必传
	 * @return
	 */
	ExecuteResult<RefundPayParam> refundApply(RefundPayParam refundPayParam) ;
	
	/**
	 *  退款处理,订单是否支付由外层校验
	 * 
	 * @param refundPayParam - reproNo -退货单号
	 * @param isBuildSettleDetail - 是否已生成结算明细
	 * @Param isReturnFactorage   - 是否退手续费
	 * @Param isReturnCommission  - 是否退佣金
	 * @return
	 */
	ExecuteResult<String> refundDeal(RefundPayParam refundPayParam,boolean isBuildSettleDetail,boolean isReturnFactorage,boolean isReturnCommission) throws Exception ;
	
	/**
	 * 根据单据编号查询退款记录
	 * 
	 * @return
	 */
	ExecuteResult<RefundPayParam> findRefInfoByRefNo(String refundNo) ;
	
	/**
	 * 根据条件查询退款记录
	 * 
	 * @param RefundPayParam - 可空
	 * @param pager - 可空
	 * @return
	 */
	DataGrid<RefundPayParam> findRefInfoByCondition(RefundPayParam refundPayParam,@SuppressWarnings("rawtypes") Pager pager);

	 /**
     * 根据退货编码查找退单[TODO 即将废弃]
     * @param returnGoodsCodeNo
     * @return
     */
    ExecuteResult<RefundPayParam> findRefInfoByReturnGoodsCode(String returnGoodsCodeNo) ;
    
    /**
	 * 根据退货单号查询退款记录
	 * 
	 * @return
	 */
	ExecuteResult<RefundPayParam> selectRefundByRePro(String rePro) ;
	/**
	 * 根据订单号，查询该订单的已退金额
	 */
	BigDecimal getTotalRefundAmountByOrderNo(String orderNo);
}
