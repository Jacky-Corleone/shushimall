package com.camelot.ecm.report;

import com.camelot.common.DateUtil;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.report.UserAndShopReportDTO;
import com.camelot.usercenter.service.UserReportService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sevelli[贾伟光] on 2015/5/5 0005.
 */
@Controller
@RequestMapping(value = "${adminPath}/report")
public class ReportController {
    @Resource
    private UserReportService userReportService;

    @RequestMapping(value = "userCount")
    public String userCount(Model model){
        ExecuteResult<UserAndShopReportDTO> result =  userReportService.getCustomerAndShopReportSum();
        model.addAttribute("userCount",result.getResult());
        return "report/userCount";
    }
    @RequestMapping(value = "userCountAjax")
    @ResponseBody
    public Map userCountAjax(){
        Map map = new HashMap();
        Date beginDate = DateUtils.addDays(new Date(),-7);
        Date endDate = DateUtils.addDays(new Date(),-1);
        ExecuteResult<List<UserAndShopReportDTO>> result = userReportService.getCustomerAndShopReportByCondition(beginDate, endDate);
        map.put("list",result.getResult());
        return map;
    }
}
