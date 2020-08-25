package com.camelot.ecm.statistical;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/tenantStatistical")
public class TenantStatisticalController extends BaseController {
	
	@RequiresPermissions("sys:user:view")
	@RequestMapping("init")
	public String initTenantStatistical(Model model) {
		System.out.println("--");
		return "statistical/tenantStatistical";
	}

}
