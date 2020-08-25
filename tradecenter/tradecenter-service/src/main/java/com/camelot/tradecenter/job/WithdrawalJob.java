package com.camelot.tradecenter.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.common.enums.WithdrawEnums;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.FinanceWithdrawRecordExportService;
import com.camelot.payment.dao.CiticPayJournalDAO;
import com.camelot.payment.dao.FinanceWithdrawRecordDAO;
import com.camelot.payment.domain.CiticPayJournal;
import com.camelot.payment.dto.FinanceWithdrawRecordDTO;
import com.camelot.payment.dto.citic.req.QueryTransferDto;
import com.ibm.icu.text.SimpleDateFormat;

/** 
 * <p>Description: [提现定时任务]</p>
 * Created on 2015年12月28日
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class WithdrawalJob {
	@Resource
	private CiticExportService  citicExportService;
	@Resource
	private FinanceWithdrawRecordExportService financeWithdrawRecordExportService;
	@Resource 
	private CiticPayJournalDAO citicPayJournalDAO;
	@Resource
	private FinanceWithdrawRecordDAO financeWithdrawRecordDAO;
	private static final Logger logger = LoggerFactory.getLogger(PayBuyerReportJob.class);
	public void updateWithdrawalStatus(){
		Integer recordStatus[]={WithdrawEnums.WithdrawalApplicationSuccess.getCode(),WithdrawEnums.WithdrawDispose.getCode()};//查询提现申请处理中和申请成功的记录
		FinanceWithdrawRecordDTO dto=new FinanceWithdrawRecordDTO();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		dto.setCreatedBegin(sf.format(new Date()));
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);
		dto.setCreatedEnd(sf.format(c.getTime()));
		dto.setRecordStatus(recordStatus);//申请成功
		Pager<FinanceWithdrawRecordDTO> page = new Pager<FinanceWithdrawRecordDTO>();
		
		page.setRows(100);
		DataGrid<FinanceWithdrawRecordDTO> res=financeWithdrawRecordExportService.queryFinanceWithdrawByCondition(dto, page);
		if(res.getRows()!=null&&res.getTotal()>0){
			int i = 0 ;
			for(FinanceWithdrawRecordDTO financeWithdrawRecordDTO:res.getRows()){
				try {
					String parentOutTradeNo = financeWithdrawRecordDTO.getTradeNo();
					CiticPayJournal  citicPayJournal  = new CiticPayJournal();
					citicPayJournal.setOrderParentTradeNo(parentOutTradeNo);
					List<CiticPayJournal> citicPayJournalList =  citicPayJournalDAO.queryList(citicPayJournal, null);//.queryCount(citicPayJournal);
					if(citicPayJournalList == null || citicPayJournalList.size() == 0){
						return;
					}
					citicPayJournal = citicPayJournalList.get(0);
					
					QueryTransferDto queryTransferDto=citicExportService.queryTransfer(citicPayJournal.getOutTradeNo());
					
					if(!(queryTransferDto!=null&&"AAAAAAA".equals(queryTransferDto.getStatus()))){
//						financeWithdrawRecordDTO.setStatus(WithdrawEnums.WithdrawQueryFail.getCode());//提现查询失败
						financeWithdrawRecordDTO.setFailReason("无法获取提现结果，请联系平台！");
						logger.error("提现记录状态查询失败："+queryTransferDto.getStatusText());
					}else{
//						financeWithdrawRecordDTO.setStatus(WithdrawEnums.WithdrawalSuccess.getCode());//提现查询成功
						
						if(queryTransferDto.getList().get(0).getStt().equals("0")){ //提现成功
							financeWithdrawRecordDTO.setStatus(WithdrawEnums.WithdrawalSuccess.getCode());
							financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
						}else if(queryTransferDto.getList().get(0).getStt().equals("1")){ //提现失败
							financeWithdrawRecordDTO.setStatus(WithdrawEnums.WithdrawalFail.getCode());
							financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
						}else if(queryTransferDto.getList().get(0).getStt().equals("2")){ //未知
							financeWithdrawRecordDTO.setStatus(WithdrawEnums.WithdrawalUnknow.getCode());
							financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
						}else if(queryTransferDto.getList().get(0).getStt().equals("3")){ //审核拒绝
							financeWithdrawRecordDTO.setStatus(WithdrawEnums.WithdrawalUnVerify.getCode());
							financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
						}else if(queryTransferDto.getList().get(0).getStt().equals("4")){ //用户撤销
							financeWithdrawRecordDTO.setStatus(WithdrawEnums.WithdrawalDel.getCode());
							financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					financeWithdrawRecordDTO.setFailReason("无法获取提现结果，请联系平台！");
					logger.error("提现记录状态查询失败："+e.getMessage());
				}  
				financeWithdrawRecordDAO.update(financeWithdrawRecordDTO);
			}
		}
	}
}
