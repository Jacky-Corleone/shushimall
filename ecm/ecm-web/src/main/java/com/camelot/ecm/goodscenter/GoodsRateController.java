package com.camelot.ecm.goodscenter;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.settlecenter.dto.SettleItemExpenseDTO;
import com.camelot.settlecenter.service.SettleItemExpenseExportService;
import com.thinkgem.jeesite.common.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sevelli on 15-4-22.
 */
@Controller
@RequestMapping(value = "${adminPath}/goodsRate")
public class GoodsRateController extends BaseController {
    @Resource
    private SettleItemExpenseExportService settleItemExpenseExportService;

    @RequiresPermissions("goods:payrate")
    @RequestMapping("/update")
    @ResponseBody
    public Map update(Long itemId,BigDecimal payRate){
        Map map = new HashMap();
        SettleItemExpenseDTO dto = new SettleItemExpenseDTO();
        dto.setItemId(itemId);
        ExecuteResult<DataGrid<SettleItemExpenseDTO>> result =  settleItemExpenseExportService.querySettleItemExpense(dto, null);
        if (result.isSuccess()&&result.getResult()!=null&&result.getResult().getRows()!=null
                &&result.getResult().getRows().size()>0){
            SettleItemExpenseDTO returnDto = result.getResult().getRows().get(0);
            returnDto.setRebateRate(payRate);
            ExecuteResult<String> returnStr = settleItemExpenseExportService.modifySettleItemExpense(returnDto);
            if (returnStr.isSuccess()){
                map.put("success",true);
                map.put("msg",returnStr.getResult());
            }else{
                map.put("success",false);
                map.put("msg",returnStr.getResultMessage());
            }
        }else{
            SettleItemExpenseDTO sidto = new SettleItemExpenseDTO();
            sidto.setItemId(itemId);
            sidto.setRebateRate(payRate);
            ExecuteResult<String> returnStr = settleItemExpenseExportService.addSettleItemExpense(sidto);
            if (returnStr.isSuccess()){
                map.put("success",true);
                map.put("msg",returnStr.getResult());
            }else{
                map.put("success",false);
                map.put("msg",returnStr.getResultMessage());
            }
        }
        return map;
    }
}
