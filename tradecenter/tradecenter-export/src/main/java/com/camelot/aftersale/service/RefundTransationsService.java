package com.camelot.aftersale.service;
import com.camelot.aftersale.dto.RefundTransationsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [退款记录信息]</p>
 * Created on 2015-10-15
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface RefundTransationsService {
	/**
	 * 
	 * <p>Discription:[创建退款记录]</p>
	 * Created on 2015-10-15
	 * @param refundTransationsDTO
	 * @return
	 * @author:[王鹏]
	 */
    public ExecuteResult<RefundTransationsDTO> createRefundTransationsDTO(RefundTransationsDTO refundTransationsDTO);

    /**
     * 
     * <p>Discription:[更新退款记录信息]</p>
     * Created on 2015-10-15
     * @param refundTransationsDTO
     * @return
     * @author:[王鹏]
     */
    public ExecuteResult<String> updateRefundTransationsDTO(RefundTransationsDTO refundTransationsDTO);

    /**
     * 
     * <p>Discription:[分页查询退款记录信息]</p>
     * Created on 2015-10-15
     * @param pager
     * @param refundTransationsDTO
     * @return
     * @author:[王鹏]
     */
    public DataGrid<RefundTransationsDTO> searchRefundTransationsDTO(Pager<RefundTransationsDTO> pager, RefundTransationsDTO refundTransationsDTO);
    /**
     * 
     * <p>Discription:[根据退款单号查询退款记录]</p>
     * Created on 2015-10-15
     * @param refundNo退款对外交易号
     * @return
     * @author:[王鹏]
     */
    public ExecuteResult<RefundTransationsDTO> queryRefundTransationByRefundNo(String refundNo);


}
