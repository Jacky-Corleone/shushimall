package com.camelot.payment.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.domain.RefundTransations;

public interface RefundTransationsDAO extends BaseDAO<RefundTransations>{
	/**
	 * 
	 * <p>Discription:[根据退款对外交易号查询最新的退款记录]</p>
	 * Created on 2015-10-15
	 * @param codeNo
	 * @return
	 * @author:[王鹏]
	 */
	public List<RefundTransations> queryRefundTransationByRefundNo(@Param("outRefundNo")String outRefundNo);
}
