package com.camelot.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.domain.RefundOrder;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface RefundOrderDAO  extends BaseDAO<RefundOrder>{
	
	/**
	 * 根据单据编号查询
	 * 
	 * @param RefundNo
	 * @return
	 */
	public RefundOrder selectRefInfoByRefNo(@Param("refundNo")String refundNo);
	
	/**
	 * 根据退货单号查询
	 * 
	 * @param RefundNo
	 * @return
	 */
	public RefundOrder selectRefundByRePro(@Param("repro")String repro);
	// TODO 废弃代码
    public RefundOrder findRefInfoByReturnGoodsCode(@Param("reproNo")String refundNo);
	
    public List<RefundOrder> selectRefundOrderByOrderIdAndRefundGoodsStatus(@Param("orderId") String orderId,@Param("states") Integer... states);
}