package com.camelot.ecm.portal;

import com.thinkgem.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sevelli on 15-3-13.
 */
@Controller
@RequestMapping(value = "${adminPath}/portal")
public class PortalController extends BaseController{
    @RequestMapping(value = "index")
    public String index(){
        return "portal/index";
    }
}
