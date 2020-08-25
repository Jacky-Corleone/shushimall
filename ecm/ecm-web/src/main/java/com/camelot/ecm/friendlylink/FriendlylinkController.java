package com.camelot.ecm.friendlylink;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.basecenter.dto.FriendlylinkItemDTO;
import com.camelot.basecenter.dto.FriendlylinkPageDTO;
import com.camelot.basecenter.dto.FriendlylinkPageItemDTO;
import com.camelot.basecenter.service.FriendlylinkItemService;
import com.camelot.basecenter.service.FriendlylinkPageItemService;
import com.camelot.basecenter.service.FriendlylinkPageService;
import com.camelot.common.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/friendlylink")
public class FriendlylinkController extends BaseController{
	@Resource
	private FriendlylinkItemService friendlylinkItemService;
	
	@Resource
	private FriendlylinkPageService friendlylinkPageService;
	
	@Resource
	private FriendlylinkPageItemService friendlylinkPageItemService;	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "itemlist")
	 public String getItemList(FriendlylinkItemDTO friendlylinkItemDTO, Page page,Model model){
		
		if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		DataGrid<FriendlylinkItemDTO> questyFriendlylinkItemDTO = friendlylinkItemService.questyFriendlylinkItem(friendlylinkItemDTO, pager);
		page.setList(questyFriendlylinkItemDTO.getRows());
		page.setCount(questyFriendlylinkItemDTO.getTotal());
		model.addAttribute("page", page);
		return "friendlylink/friendlylinkItemList";
	}
	
	
	@RequestMapping(value = "additem")
	 public String addItem(FriendlylinkItemDTO friendlylinkItemDTO, Model model){
		
		return "friendlylink/addFriendlylinkItem";
	}
	
	@RequestMapping("savefriendlylinkitem")
	public String saveFriendlylinkItem(Model model, HttpServletRequest request,@ModelAttribute("friendlylinkItem") FriendlylinkItemDTO friendlylinkItem) {
		if(friendlylinkItem != null){
			if(friendlylinkItem.getId() == null){
				friendlylinkItemService.saveFriendlylinkItem(friendlylinkItem);
			}else {
				friendlylinkItemService.editFriendlylinkItem(friendlylinkItem);
			}
		}
		return "redirect:" + SysProperties.getAdminPath() + "/friendlylink/itemlist";
		
	}
	
	@RequestMapping("edititem")
	public String editFriendlylinkItem(Model model, HttpServletRequest request,@ModelAttribute("friendlylinkItem") FriendlylinkItemDTO friendlylinkItem) {
		String id = request.getParameter("id");
		ExecuteResult<FriendlylinkItemDTO> itemDB = friendlylinkItemService.findFriendlylinkItemById(Long.parseLong(id));
		model.addAttribute("friendlylinkItemDB", itemDB.getResult());
		return "friendlylink/addFriendlylinkItem";
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "pagelist")
	 public String getPageList(FriendlylinkPageDTO friendlylinkPageDTO, Page page,Model model){
		
		if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		DataGrid<FriendlylinkPageDTO> questyFriendlylinkPage = friendlylinkPageService.getFriendlylinkPages(friendlylinkPageDTO, pager);
		page.setList(questyFriendlylinkPage.getRows());
		page.setCount(questyFriendlylinkPage.getTotal());
		model.addAttribute("page", page);
		return "friendlylink/friendlylinkPageList";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("deleteitem")
	@ResponseBody
	public Json deleteItem(HttpServletRequest request) {
		Json res=new Json();
		String itemId = request.getParameter("id");
		try{
			
			friendlylinkItemService.deleteFriendlylinkItemById(Long.parseLong(itemId));
			res.setSuccess(true);
			res.setMsg("删除成功");
		}catch(Exception e){
			res.setSuccess(true);
			res.setMsg("删除失败");
		}
		return res;
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "viewpage")
	 public String viewPageDetail(Model model, HttpServletRequest request, FriendlylinkItemDTO friendlylinkItemDTO,Page page){
		if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		String id = request.getParameter("id");
		long pageId = Long.parseLong(id);
		ExecuteResult<FriendlylinkPageDTO> friendlylinkPageDB = friendlylinkPageService.findFriendlylinkPageById(pageId);
		DataGrid<FriendlylinkItemDTO> selectedItems = friendlylinkItemService.getSelectedItems(pageId, pager);
		DataGrid<FriendlylinkItemDTO> selectedItemsCount = friendlylinkItemService.getSelectedItems(pageId, null);
		page.setList(selectedItems.getRows());
		page.setCount(selectedItemsCount.getTotal());
		model.addAttribute("page", page);
		model.addAttribute("friendlylinkPageDB", friendlylinkPageDB.getResult());
		return "friendlylink/viewPageInfo";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("unboundlink")
	@ResponseBody
	public Json unboundlink(HttpServletRequest request) {
		Json res=new Json();
		String itemId = request.getParameter("itemId");
		String pageId = request.getParameter("pageId");
		try{
			FriendlylinkPageItemDTO pageItemDTO = new FriendlylinkPageItemDTO();
			pageItemDTO.setItemId(Long.parseLong(itemId));
			pageItemDTO.setPageId(Long.parseLong(pageId));
			friendlylinkPageItemService.deleteFriendlylinkPageItem(pageItemDTO);
			res.setSuccess(true);
			res.setMsg("删除成功");
		}catch(Exception e){
			res.setSuccess(true);
			res.setMsg("删除失败");
		}
		return res;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "boundlink")
	 public String boundLink(Model model, HttpServletRequest request, Page page){
		if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		String id = request.getParameter("id");
		long pageId = Long.parseLong(id);
		ExecuteResult<FriendlylinkPageDTO> friendlylinkPageDB = friendlylinkPageService.findFriendlylinkPageById(pageId);
		DataGrid<FriendlylinkItemDTO> UnselectedItems = friendlylinkItemService.getUnSelectedItems(pageId, pager);
		DataGrid<FriendlylinkItemDTO> UnselectedItemsCount = friendlylinkItemService.getUnSelectedItems(pageId, null);
		page.setList(UnselectedItems.getRows());
		page.setCount(UnselectedItemsCount.getTotal());
		model.addAttribute("page", page);
		model.addAttribute("friendlylinkPageDB", friendlylinkPageDB.getResult());
		return "friendlylink/boundLinkList";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("boundfriendlylink")
	@ResponseBody
	public Json boundFrendlyLink(HttpServletRequest request) {
		Json res=new Json();
		String itemId = request.getParameter("itemId");
		String pageId = request.getParameter("pageId");
		try{
			FriendlylinkPageItemDTO pageItemDTO = new FriendlylinkPageItemDTO();
			pageItemDTO.setItemId(Long.parseLong(itemId));
			pageItemDTO.setPageId(Long.parseLong(pageId));
			friendlylinkPageItemService.saveFriendlylinkPageItem(pageItemDTO);
			res.setSuccess(true);
			res.setMsg("绑定成功");
		}catch(Exception e){
			res.setSuccess(true);
			res.setMsg("绑定失败");
		}
		return res;
		
	}
	
	
	@RequestMapping(value = "addpage")
	 public String addPage(FriendlylinkItemDTO friendlylinkItemDTO, Model model){
		
		return "friendlylink/addFriendlylinkPage";
	}
	
	@RequestMapping("savefriendlylinkpage")
	public String saveFriendlylinkPage(Model model, HttpServletRequest request,@ModelAttribute("friendlylinkPage") FriendlylinkPageDTO friendlylinkPage) {
		if(friendlylinkPage != null){
			if(friendlylinkPage.getId() == null){
				friendlylinkPageService.saveFriendlylinkPage(friendlylinkPage);
			}else {
				friendlylinkPageService.editFriendlylinkPage(friendlylinkPage);
			}
		}
		return "redirect:" + SysProperties.getAdminPath() + "/friendlylink/pagelist";
		
	}
	
	
	@RequestMapping("editpage")
	public String editFriendlylinkPage(Model model, HttpServletRequest request,@ModelAttribute("friendlylinkPage") FriendlylinkPageDTO friendlylinkPage) {
		String id = request.getParameter("id");
		ExecuteResult<FriendlylinkPageDTO> pageDB = friendlylinkPageService.findFriendlylinkPageById(Long.parseLong(id));
		model.addAttribute("friendlylinkPageDB", pageDB.getResult());
		return "friendlylink/addFriendlylinkPage";
		
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@RequestMapping("deletepage")
	public Json deletePage(Model model, HttpServletRequest request,@ModelAttribute("friendlylinkPage") FriendlylinkPageDTO friendlylinkPage) {
		Json res=new Json();
		String pageId = request.getParameter("id");
		
		try{
			friendlylinkPageService.deleteFriendlylinkItemById(Long.parseLong(pageId));
			ExecuteResult<String> pageDB = friendlylinkPageItemService.deleteFriendlylinkPageItemByPageId(Long.parseLong(pageId));
			res.setSuccess(true);
			res.setMsg("删除成功");
		}catch(Exception e){
			res.setSuccess(true);
			res.setMsg("删除失败");
		}
		return res;
		
	}
	
	
	
}
