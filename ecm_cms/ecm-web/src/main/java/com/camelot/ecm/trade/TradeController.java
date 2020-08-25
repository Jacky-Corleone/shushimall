package com.camelot.ecm.trade;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.DualAccountInfoService;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.thinkgem.jeesite.common.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * <p>
 * 金融账户设置
 * </p>
 * 
 * @author yangsaibei
 * 
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部 2015年4月17日
 * 
 */

@Controller
@RequestMapping(value = "${adminPath}/trade")
public class TradeController extends BaseController {
	@Autowired
	private DualAccountInfoService dualAccountInfoService;
	
	/**
	 * <p>
	 *  添加金融账户设置
	 * </p>
	 * @param financeAccountInfoDto
	 * @param model
	 * @return
	 */
	@RequestMapping({ "add", "" })
	public String addAccount(FinanceAccountInfoDto financeAccountInfoDto, Model model) {
		dualAccountInfoService.createAccountInfoDto(financeAccountInfoDto);
		model.addAttribute("financeAccountInfoDto", financeAccountInfoDto);
		return "trade/update";
	}
	
	/**
	 * <p>
	 *  更新金融账户设置
	 * </p>
	 * @param financeAccountInfoDto
	 * @param model
	 * @return
	 */
	@RequestMapping({ "update", "" })
	public String updateAccount(FinanceAccountInfoDto financeAccountInfoDto, Model model) {
		financeAccountInfoDto.setAccountId("1");
		dualAccountInfoService.updateAccountInfoDTO(financeAccountInfoDto);
		model.addAttribute("financeAccountInfoDto", financeAccountInfoDto);
		return "trade/update";
	}
	
	/**
	 * <p>
	 *  获取金融账户设置
	 * </p>
	 * @param financeAccountInfoDto
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/getaccount", "" })
	public String getAccount(String accountId, Model model) {
		accountId = "1";
		FinanceAccountInfoDto financeAccountInfoDto = new FinanceAccountInfoDto();
		ExecuteResult<FinanceAccountInfoDto> financeAccountInfoDtoById = dualAccountInfoService.getFinanceAccountInfoDtoById(accountId);
		if(financeAccountInfoDtoById.getResult() !=null){
			financeAccountInfoDto = financeAccountInfoDtoById.getResult();
			model.addAttribute("financeAccountInfoDto", financeAccountInfoDto);
			return "trade/update";
		}
			
		model.addAttribute("financeAccountInfoDto", financeAccountInfoDto);
		return "trade/accountinfo";
	}

}
