package com.camelot.payment;

import java.util.List;
import java.util.Map;

import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.dto.FactorageJournalDTO;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;

/**
 * 供客户端调用的支付远程接口
 * 
 * @author - learrings
 * @createDate - 2015-3-02
 */
public interface PaymentExportService {

	/**
	 * 支付初始化，签名验证：[system，orderNo，totalFee]+key
	 * 
	 * @param param -  system[系统编号]/sellerId[卖家ID]/orderNo[订单号]/totalFee[支付金额]  必传参数
	 * @return
	 */
	ExecuteResult<Integer> payIndex(PayReqParam param) throws Exception;
	
	/**
	 * 支付信息生成银行支付链接
	 * 
	 * @param param - orderNo[订单号]/payBank[支付银行]-必传参数
	 * @return  result.isSuccess() -- true/false --生成链接结果</br>
	 *                  result.getResult() -- 1[正在支付]/2[支付成功] --生成链接状态</br>
	 * 					 result.getResultMessage() -- 当 getResult()==1时有效,支付链接内容
	 */
	ExecuteResult<Integer> pay(PayReqParam param) throws Exception;

	/**
	 * 接收支付结果通知
	 * 
	 * @param parameterMap - 支付参数（必传isNotify-同步[false]or异步[true]）
	 *                           
	 * @return
	 * @throws 
	 */
	ExecuteResult<OrderInfoPay> payResult(Map<String, String> parameterMap,String payBank) ;
	/**
	 * 微信接收支付结果通知
	 * 
	 * @param resultXml - 响应参数
	 *                           
	 * @return
	 * @throws 
	 */
	ExecuteResult<Map<String, String>> parseXml(String resultXml);
	
	/**
	 * 查询订单支付结果
	 * 
	 * @param outTradeNo - 订单号
	 * @return
	 */
	ExecuteResult<OrderInfoPay> paySearch(String outTradeNo) ;
	
	/**
	 * 根据对外订单号组查询有效的子支付订单记录对象
	 * 
	 * @param orderNo
	 * @return
	 */
	List<OrderInfoPay> findChildTransByOutTrades(String outTradeNos);
	/**
	 * 根据订单号查询有效的支付订单记录对象
	 * 
	 * @param orderNo
	 * @return
	 */
	ExecuteResult<OrderInfoPay> findTransByOrderNo(String orderNo);
	
	/**
	 * 订单价格修改重新生成支付记录
	 * 
	 * @return
	 */
	ExecuteResult<String> modifyOrderPrice(String orderNo) throws Exception;
	
	/**
	 * 接收网银在线查询订单的支付结果 TODO 未测试
	 * 
	 * @return
	 */
	void paySearchResult(Map<String, String> parameterMap) ;
	
	/**
	 * 保存手续费记录
	 * 
	 * @param factorageJournalDTO
	 */
	void saveFactorageJournal(FactorageJournalDTO factorageJournalDTO) throws Exception;
	
	/**
	 * 根据条件查询手续费记录
	 * 
	 * @param factorageJournalDTO - 可空
	 * @param Pager - 可空
	 * @return
	 */
	DataGrid<FactorageJournalDTO> findFactorageJournal(FactorageJournalDTO factorageJournalDTO, @SuppressWarnings("rawtypes") Pager Pager);
	
	/**
	 * 平台同意退款
	 * 
	 * @return
	 */
	ExecuteResult<Integer> refundApply(RefundReqParam refundReqParam) ;
	
	/**
	 * TODO 即将废弃 退款处理
	 * 
	 * @return
	 */
	ExecuteResult<RefundPayParam> refundDeal(RefundPayParam refundPayParam) ;

	/**
	 * TODO 即将废弃 根据单据编号查询退款记录
	 * 
	 * @return
	 */
	ExecuteResult<RefundPayParam> findRefInfoByRefNo(String refundNo) ;
	
	
	/**
	 * TODO 即将废弃 根据条件查询退款记录
	 * 
	 * @param RefundPayParam - 可空
	 * @param pager - 可空
	 * @return
	 */
	DataGrid<RefundPayParam> findRefInfoByCondition(RefundPayParam refundPayParam,@SuppressWarnings("rawtypes") Pager pager);

    /**
     * TODO 即将废弃 根据退货编码查找退单
     * @param returnGoodsCodeNo
     * @return
     */
    ExecuteResult<RefundPayParam> findRefInfoByReturnGoodsCode(String returnGoodsCodeNo) ;

}
