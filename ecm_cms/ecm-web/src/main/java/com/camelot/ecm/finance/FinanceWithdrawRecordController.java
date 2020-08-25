package com.camelot.ecm.finance;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.common.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.FinanceWithdrawRecordExportService;
import com.camelot.payment.dto.CiticPayJournalDto;
import com.camelot.payment.dto.FinanceWithdrawRecordDTO;
import com.camelot.payment.dto.citic.req.QueryTransferDto;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 
 * <p>Description: [提现记录的controller]</p>
 * Created on 2015年12月28日
 * @author  <a href="mailto: xxx@camelotchina.com">化亚会</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/financeWithdraw")
public class FinanceWithdrawRecordController extends BaseController {
	
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private FinanceWithdrawRecordExportService financeWithdrawRecordService;  //提現記錄的接口
	@Resource
	private CiticExportService citicExportService; //中信银行的接口
	 
	/**
	 * 
	 * <p>Discription:[查询提现记录]</p>
	 * Created on 2015年12月28日
	 * @return
	 * @author:{化亚会}
	 */
	@RequestMapping(value = "financeWithdrawIndex")
	public String financeWidthIndex(@ModelAttribute("fwRecord") FinanceWithdrawRecordDTO financeWithdrawRecordDTO, Model model,Pager pager){
		if (pager.getPage() < 1) {
			pager.setPage(1);
		}
		if (pager.getRows() < 1) {
			pager.setRows(20);
		}
		DataGrid<FinanceWithdrawRecordDTO> financeWithdrawRecordList = this.financeWithdrawRecordService
				.queryFinanceWithdrawByCondition(financeWithdrawRecordDTO, pager);
		Page<FinanceWithdrawRecordDTO> page = new Page<FinanceWithdrawRecordDTO>();

		page.setCount(financeWithdrawRecordList.getTotal());
		page.setList(financeWithdrawRecordList.getRows());
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		model.addAttribute("page", page);
		model.addAttribute("fwRecord", financeWithdrawRecordDTO); //回显
		return "finance/financeWithdrawRecord";
	}
	
	/**
	 * 
	 * <p>Discription:[调用中心银行的接口  查看提现状态]</p>
	 * Created on 2015年12月29日
	 * @return
	 * @author:[化亚会]
	 */
	@ResponseBody
	@RequestMapping(value = "queryStatus")
	public Json queryStatusForZxyh(String tradeNo) throws Exception{
		Json json=new Json();
		String str = "";
		try {
			CiticPayJournalDto citicPayJournalDto = new CiticPayJournalDto();
			citicPayJournalDto.setOrderParentTradeNo(tradeNo);
			List<CiticPayJournalDto> citicPayJournalDtoList = citicExportService.findCiticPayJournalList(citicPayJournalDto);
			if(citicPayJournalDtoList == null || citicPayJournalDtoList.size() == 0){
				str = "对外交易号无效!";
				json.setSuccess(false);
				return json;
			}
			String outTradeNo = citicPayJournalDtoList.get(0).getOutTradeNo();
			//调用中信银行的接口  查看提现状态
			QueryTransferDto queryTransferDto=citicExportService.queryTransfer(outTradeNo);
			FinanceWithdrawRecordDTO financeWithdrawRecordDTO = new FinanceWithdrawRecordDTO();
			financeWithdrawRecordDTO.setTradeNo(tradeNo);
			if(!(queryTransferDto!=null&&"AAAAAAA".equals(queryTransferDto.getStatus()))){ //查询失败
				str = "提交查看请求失败!";
				json.setSuccess(false);
				return json;
			}else{ //查询成功
				if(queryTransferDto.getList().get(0).getStt().equals("0")){ //提现成功
					str = "提现成功";
					financeWithdrawRecordDTO.setStatus(50);
					financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
				}else if(queryTransferDto.getList().get(0).getStt().equals("1")){ //提现失败
					str = "提现失败";
					financeWithdrawRecordDTO.setStatus(40);
					financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
				}else if(queryTransferDto.getList().get(0).getStt().equals("2")){ //未知
					str = "未知";
					financeWithdrawRecordDTO.setStatus(60);
					financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
				}else if(queryTransferDto.getList().get(0).getStt().equals("3")){ //审核拒绝
					str = "审核拒绝";
					financeWithdrawRecordDTO.setStatus(70);
					financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
				}else if(queryTransferDto.getList().get(0).getStt().equals("4")){ //用户撤销
					str = "用户撤销";
					financeWithdrawRecordDTO.setStatus(80);
					financeWithdrawRecordDTO.setFailReason(queryTransferDto.getList().get(0).getStatusText());
				}
			}
			//更改提现记录表中的提现状态
			financeWithdrawRecordDTO.setTradeNo(tradeNo);
			boolean bol = this.financeWithdrawRecordService.updateForTradeNo(financeWithdrawRecordDTO);
			if(!bol){
				str = "更新提现状态失败，请稍后重试！";
				json.setSuccess(false);
			}else{
				json.setSuccess(true);
			}
		} catch (Exception e) {
			str = "提交查看请求失败!";
			json.setSuccess(false);
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		json.setMsg(str);
		return json;
	}
}
