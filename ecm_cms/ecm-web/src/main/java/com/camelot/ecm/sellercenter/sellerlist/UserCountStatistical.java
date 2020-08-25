package com.camelot.ecm.sellercenter.sellerlist;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 *  商家管理，信息变更审核
 * @author - 门光耀
 * @message- 商家信息审核，店铺信息审核
 * @createDate - 2015-3-9
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/usercountstatistical")
public class UserCountStatistical extends BaseController {
    /**
     *
     * @author - 门光耀
     * @message- 用户统计页面
     * @createDate - 2015-3-10
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/resultofstatistical")
        public ModelAndView resultOfStatistical(){
        Map<String,Object> statistical=new HashMap<String, Object>();

        return new ModelAndView("/sellercenter/userstatisticalresult/userstatisticalresult","statistical",statistical);
    }
}
