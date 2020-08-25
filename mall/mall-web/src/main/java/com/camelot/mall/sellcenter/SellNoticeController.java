package com.camelot.mall.sellcenter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;
import com.camelot.sellercenter.notice.service.NoticeExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.JsonHelper;
import com.camelot.util.WebUtil;
/**
 * 
 * <p>
 * Description: [公告管理]
 * </p>
 * Created on 2015-2-27
 * 
 * @author <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "/sellcenter/sellNotice")
public class SellNoticeController{

	@Autowired
	private NoticeExportService noticeExportService;
	
	@Resource
	private UserExportService userExportService;
	
	/**
	 * 获取公告列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String list(Integer page,HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Pager<MallNoticeDTO> pager = new Pager<MallNoticeDTO>();
		if(page == null){
			page = 1;
		}
		pager.setPage(page);
		
		MallNoticeDTO dto= new MallNoticeDTO();
		dto.setPlatformId(WebUtil.getInstance().getShopId(request));
		DataGrid dg = noticeExportService.queryNoticeList(pager, dto);
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());
		
		if (pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页。最好是直接改Pager类中的endPageIndex的初始值为1
			pager.setEndPageIndex(pager.getStartPageIndex());
		}
		
		model.addAttribute("pager", pager);
		return "sellcenter/notice/noticeList";
	}
	
	/**
	 * 跳转到公告添加/编辑页面
	 * @param mallNoticeDTO
	 * @param model
	 * @return
	 */
	@RequestMapping({ "form"})
	public String form(MallNoticeDTO mallNoticeDTO, Model model,String action) {
		String message = "添加公告";
		if(mallNoticeDTO.getNoticeId() != null){
			mallNoticeDTO = noticeExportService.getNoticeInfo(mallNoticeDTO.getNoticeId());
			message = "编辑公告";
			if(action != null && action.equals("view")){
				message = "查看公告";
			}
		}
		model.addAttribute("mallNoticeDTO", mallNoticeDTO);
		model.addAttribute("message", message);
		model.addAttribute("action", action);
		return "sellcenter/notice/noticeForm";
	}
	
	/**
	 * 保存公告
	 * @param mallNoticeDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(MallNoticeDTO mallNoticeDTO, Integer page,HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes) {
		
		mallNoticeDTO.setPlatformId(WebUtil.getInstance().getShopId(request));
		mallNoticeDTO.setNoticeType(1);
		ExecuteResult<String> result = noticeExportService
				.addNotice(mallNoticeDTO);
		return "redirect:" + "/sellcenter/sellNotice?page="+page;
	}
	
	/**
	 * 编辑公告
	 * @param mallNoticeDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("edit")
	public String edit(MallNoticeDTO mallNoticeDTO, Integer page,HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes) {
		ExecuteResult<String> result = noticeExportService.modifyNoticeInfo(mallNoticeDTO);
		return "redirect:" + "/sellcenter/sellNotice?page="+page;
	}
	
	/**
	 * 置顶/取消置顶
	 * @param mallNoticeDTO
	 * @return
	 */
	@RequestMapping("recommend")
	public String recommend(MallNoticeDTO mallNoticeDTO,Integer page,HttpServletRequest request){
		System.out.println(mallNoticeDTO.getNoticeId()+"==="+mallNoticeDTO.getPlatformId()+"==="+mallNoticeDTO.getSortNum());
		ExecuteResult<String> result = noticeExportService.modifyNoticeRecommend(mallNoticeDTO.getNoticeId(), mallNoticeDTO.getIsRecommend());
		return "redirect:" + "/sellcenter/sellNotice?page=1";
		
	}
	
	/**
	 * 发布/下架
	 * @param mallNoticeDTO
	 * @return
	 */
	@RequestMapping("status")
	public String status(MallNoticeDTO mallNoticeDTO,Integer page,HttpServletRequest request){
		//调用发布/下架接口
		ExecuteResult<String> result = noticeExportService.modifyNoticeStatus(mallNoticeDTO.getNoticeId(), mallNoticeDTO.getNoticeStatus());
		return "redirect:" + "/sellcenter/sellNotice?page="+page;
	}
	/**
	 * 上移 +1
	 * 下移 -1
	 * */
	@RequestMapping("updateNoticSortNum")
	@ResponseBody
	public Map<String,Object> updateNoticSortNum(MallNoticeDTO mallNoticeDTO,Integer page,Integer modifyNum,HttpServletRequest request){
		
		Map<String,Object> result = new HashMap<String,Object>();
		System.out.println(mallNoticeDTO.getNoticeId()+"==="+mallNoticeDTO.getPlatformId()+"==="+mallNoticeDTO.getSortNum()+"==="+modifyNum);

		ExecuteResult<MallNoticeDTO> dg = noticeExportService.updateNoticSortNum(mallNoticeDTO, modifyNum);
		
		return result;
	}
	
	@RequestMapping("deleteNotice")
	public String deleteNotice(HttpServletResponse response,MallNoticeDTO mallNoticeDTO){
		noticeExportService.deleteNoticeById(mallNoticeDTO.getNoticeId());
		JsonHelper.success(response);
		return null;
	}

}
