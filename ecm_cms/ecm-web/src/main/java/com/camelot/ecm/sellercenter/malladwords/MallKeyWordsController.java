package com.camelot.ecm.sellercenter.malladwords;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.mallword.dto.MallWordDTO;
import com.camelot.sellercenter.mallword.service.MallWordExportService;
import com.thinkgem.jeesite.common.persistence.Page;

/**
 * 商城首页关键词  设置
 * @author jianghuaFan
 *
 */
@Controller
@RequestMapping("${adminPath}/mallKeyWords")
public class MallKeyWordsController 
{
	
	@Resource
	private MallWordExportService mallWordService;
	
	/**
	 * 添加热门关键字
	 * @param wordDTOList
	 * @return
	 */
	@RequestMapping("/add")
	public String add(MallWordDTO wordDTO)
	{
		if(wordDTO != null && StringUtils.isNotBlank(wordDTO.getWord()))
		{
			mallWordService.add(wordDTO);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/mallKeyWords/list?msg=addSuccess";
	}
	
	/**
	 * 
	 * 删除热门关键字
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(String id)
	{
		if(StringUtils.isNotBlank(id))
		{
			mallWordService.delete(new Long(id));
		}
		return "redirect:" + SysProperties.getAdminPath() + "/mallKeyWords/list?msg=delSuccess";
	}
	
	/**
	 * 热门关键字 展示列表
	 * @param model
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, String msg, MallWordDTO wordDTO)
	{
		model.addAttribute("msg", msg);
		if(wordDTO == null)
		{
			wordDTO = new MallWordDTO();
		}
		Page<MallWordDTO> p = new Page<MallWordDTO>(request, response);
		Pager pager = new Pager();
		pager.setPage(p.getPageNo());
		pager.setRows(p.getMaxResults());
		
		DataGrid<MallWordDTO> dg = this.mallWordService.datagrid(wordDTO, pager);
		p.setCount(dg.getTotal());
		p.setList(dg.getRows());
		p.setOrderBy(pager.getOrder());
		model.addAttribute("page",p);
		model.addAttribute("dto", wordDTO);
		
		model.addAttribute("result", dg.getRows());
		return "/sellercenter/mallkeywords/index";
	}
	
}
