package com.camelot.mall.home;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.service.MallDocumentService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月3日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/document")
public class DocumentController {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private MallDocumentService mallDocumentService;

    @Resource
    private UserExportService userExportService;
	
	@RequestMapping("/{type}")
	public String toView(@PathVariable("type")Integer type,MallDocumentDTO dto,Model model,HttpServletRequest request){
		LOG.debug("====>"+JSON.toJSONString(dto));
		MallDocumentDTO doc = new MallDocumentDTO();
		dto.setMallStatus(2);
		doc.setMallType(type);
		doc.setMallStatus(2);
		
		@SuppressWarnings("rawtypes")
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(100);
		pager.setSort("mallClassifyId");
		pager.setOrder("asc");
		DataGrid<MallDocumentDTO> dg = this.mallDocumentService.queryMallDocumentList(doc, pager);
		LOG.debug(JSON.toJSONString(dg.getRows()));
		model.addAttribute("documents", dg.getRows());
		
		if( dto.getMallId() != null ){
			MallDocumentDTO document = this.mallDocumentService.getMallDocumentById(Long.valueOf(dto.getMallId()));
			model.addAttribute("document", document);
		}else if( dto.getMallClassifyId() != null ){
			doc.setMallClassifyId(dto.getMallClassifyId());
			DataGrid<MallDocumentDTO> classifyDoc = this.mallDocumentService.queryMallDocumentList(doc, pager);
			if( classifyDoc.getRows() != null && classifyDoc.getRows().size() > 0 ){
				model.addAttribute("document", classifyDoc.getRows().get(0));
			}
		}else if( dto.getMallClassifyTitle() != null ){
			doc.setMallClassifyTitle(dto.getMallClassifyTitle());
			DataGrid<MallDocumentDTO> classifyDoc = this.mallDocumentService.queryMallDocumentList(doc, pager);
			if( classifyDoc.getRows() != null && classifyDoc.getRows().size() > 0 ){
				model.addAttribute("document", classifyDoc.getRows().get(0));
			}
		}

        String userToken = LoginToken.getLoginToken(request);
        if(userToken!=null) {
            RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
            if (registerDto != null) {
                model.addAttribute("userId", String.valueOf(registerDto.getUid()));
            }else{
                model.addAttribute("userId", "");
            }
        }

		return "/home/document";
	}
	
}
