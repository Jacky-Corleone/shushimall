package com.camelot.mall.basecenter;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;

@Controller
@RequestMapping("/webmsg")
public class WebsiteMessageController {

	
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService;
	
	@RequestMapping("/queryCount")
	@ResponseBody
	public Object queryMsgCount(WebSiteMessageDTO messageDTO) {
		long count = baseWebSiteMessageService.queryCount(messageDTO);
		return count;
	}
}
