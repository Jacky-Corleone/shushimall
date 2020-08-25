package com.camelot.mall.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.mall.sellcenter.UserDtoUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

	@Resource
	private UserExportService userExportService = null;
	
	@Resource
	private AddressInfoService addressInfoService = null;

	@Resource
	private AddressBaseService addressBaseService = null;
	
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService = null;
	

	@RequestMapping("addr")
	public String addrpage(Model model, HttpServletRequest request){
		Long userId = WebUtil.getInstance().getUserId(request);
		DataGrid<AddressInfoDTO> dataGrid = addressInfoService.queryAddressinfo(userId);
		if(dataGrid != null)
		{
			for(AddressInfoDTO address :  dataGrid.getRows())
			{
				address.setContactphone(UserDtoUtils.hideUserCellPhone(address.getContactphone()));
			}
		}
		model.addAttribute("addrList", dataGrid);
		
		List<AddressBase> addressList = addressBaseService.queryAddressBase("0");
		
		model.addAttribute("addressList", addressList);
		return "/user/buyer/shop-addr";
	}
	
	@RequestMapping("saveaddr")
	public String saveAddr(Model model, AddressInfoDTO addressInfoDTO, String[] defauleAddr, HttpServletRequest request){
		Long userId = WebUtil.getInstance().getUserId(request);
		addressInfoDTO.setBuyerid(userId);
		if(defauleAddr != null){
			addressInfoDTO.setIsdefault(1);
		} else {
			addressInfoDTO.setIsdefault(2);
		}
		addressInfoService.addAddressInfo(addressInfoDTO);
		return "redirect:/buyer/addr";
	}
	
	@RequestMapping("getaddr")
	public String getAddr(Long id,Model model){
		ExecuteResult<AddressInfoDTO> result = addressInfoService.queryAddressinfoById(id);
		if(result.isSuccess()){
			model.addAttribute("addr", result.getResult());
		}
		ExecuteResult<List<AddressBaseDTO>> nowAddressList = addressBaseService.queryNameByCode(result.getResult().getProvicecode(),result.getResult().getCitycode(),result.getResult().getCountrycode());
		
		model.addAttribute("proviceName", nowAddressList.getResult().get(0).getName());
		model.addAttribute("cityName", nowAddressList.getResult().get(1).getName());
		model.addAttribute("countryName", "");
		//不取三级联动
//		model.addAttribute("countryName", nowAddressList.getResult().get(2).getName());
		
		List<AddressBase> addressList = addressBaseService.queryAddressBase("0");
		
		model.addAttribute("addressList", addressList);
		
		return "/user/buyer/edit_shop_addr";
	}
	
	@ResponseBody
	@RequestMapping("getAddress")
	public AddressInfoDTO getAddress(Long id){
		ExecuteResult<AddressInfoDTO> result = addressInfoService.queryAddressinfoById(id);
		return result.getResult();
	}
	
	@RequestMapping("modifyAddr")
	public String modifyAddr(AddressInfoDTO addressInfoDTO, String[] defauleAddr, HttpServletRequest request){
		if(defauleAddr != null){
			addressInfoDTO.setIsdefault(1);
			//wangdongxiao@camelotchina.com王东晓添加
			//修改地址时，如果将这个地址设置为默认地址，应将其他地址设置为非默认地址，保证默认地址的唯一
			Long userId = WebUtil.getInstance().getUserId(request);
			addressInfoService.modifyDefaultAddress(addressInfoDTO.getId(), userId);
		} else {
			addressInfoDTO.setIsdefault(2);
		}
		addressInfoService.modifyAddressInfo(addressInfoDTO);
		
		return "redirect:/buyer/addr";
	}
	/**
	 * 点击地址中的默认地址复选框，将当前地址转换为默认地址或取消默认地址
	 * 
	 * @author 王东晓
	 * create date 2015-05-21
	 * 
	 * @param id  地址id
	 * @param flag  如果是1或者null，表示设置默认地址，如果是2，表示取消默认地址
	 * @param defauleAddr  1表示默认地址，2表示不是默认地址
	 * @return
	 */
	@RequestMapping("modifyDefAddr")
	@ResponseBody
	public String modifyDefAddr(Long id, Integer flag, HttpServletRequest request) {
		Long userId = WebUtil.getInstance().getUserId(request);
		ExecuteResult<String> executeResult = null;
		if (null == flag || flag == 1) {// 设置默认地址
			executeResult = addressInfoService.modifyDefaultAddress(id, userId);
		} else if (flag == 2) {// 取消默认地址
			AddressInfoDTO addressInfoDTO = new AddressInfoDTO();
			addressInfoDTO.setId(id);
			addressInfoDTO.setIsdefault(2);
			executeResult = addressInfoService.modifyAddressInfo(addressInfoDTO);
		}
		if (executeResult != null && executeResult.isSuccess()) {
			return "true";
		}
		return "false";
	}
	
	@RequestMapping("deladdr")
	public String delAddr(Long addr){
		addressInfoService.removeAddresBase(addr);
		return "redirect:/buyer/addr";
	}
	
	@RequestMapping("news")
	public String newspage(Model model, Integer page, HttpServletRequest request,Integer wmRead,String messageDate){
		
		Pager<WebSiteMessageDTO> pager = new Pager<WebSiteMessageDTO>();
		if(page != null){
			pager.setPage(page);
		}
		Long userId = WebUtil.getInstance().getUserId(request);
		WebSiteMessageDTO siteMessageDTO = new WebSiteMessageDTO();
		siteMessageDTO.setWmToUserid(userId);
		if(wmRead!=null){
			siteMessageDTO.setWmRead(wmRead);
		}
		if(!StringUtils.isEmpty(messageDate)){
			siteMessageDTO.setWmCreated(Date.valueOf(messageDate));
		}
		DataGrid<WebSiteMessageDTO> allMag = baseWebSiteMessageService.queryWebSiteMessageList(siteMessageDTO, pager);
		siteMessageDTO.setType(1);
		DataGrid<WebSiteMessageDTO> sysMag = baseWebSiteMessageService.queryWebSiteMessageList(siteMessageDTO, pager);
		model.addAttribute("allMag", allMag);
		model.addAttribute("sysMag", sysMag);
		model.addAttribute("newsType", "news");
		model.addAttribute("read", wmRead);
		model.addAttribute("messageDate", messageDate);
		pager.setRecords(allMag.getRows());
		pager.setTotalCount(allMag.getTotal().intValue());
		model.addAttribute("page", pager);
		return "/user/buyer/news-center";
	}
	
	@RequestMapping("sysnews")
	public String sysnews(Model model, WebSiteMessageDTO dto, Integer page, HttpServletRequest request,Integer wmRead,String messageDate){
		Long userId = WebUtil.getInstance().getUserId(request);
		dto.setWmToUserid(userId);
		Pager<WebSiteMessageDTO> pager = new Pager<WebSiteMessageDTO>();
		if(page != null){
			pager.setPage(page);
		}
		if(wmRead!=null){
			dto.setWmRead(wmRead);
		}
		if(!StringUtils.isEmpty(messageDate)){
			dto.setWmCreated(Date.valueOf(messageDate));
		}
		if(dto.getType() == null){
			dto.setType(1);
		}
		DataGrid<WebSiteMessageDTO> sysMag = baseWebSiteMessageService.queryWebSiteMessageList(dto, pager);
		if(dto.getType() != null){
			dto.setType(null);
		}
		DataGrid<WebSiteMessageDTO> allMag = baseWebSiteMessageService.queryWebSiteMessageList(dto, pager);
		model.addAttribute("allMag", allMag);
		
		model.addAttribute("sysMag", sysMag);
		model.addAttribute("newsType", "sysnews");
		model.addAttribute("read", wmRead);
		model.addAttribute("messageDate", messageDate);
		model.addAttribute("page", pager);
		return "/user/buyer/news-center";
	}
	
	@RequestMapping("setread")
	public String setRead(Model model,String[] ids,String newsType,String wmRead,String messageDate){
		ExecuteResult<String> result = baseWebSiteMessageService.modifyWebSiteMessage(ids,"2");
		model.addAttribute("msg", result.getResultMessage());
		return "redirect:/buyer/"+newsType+"?wmRead="+wmRead+"&messageDate="+messageDate;
	}
	
	
	
	 @RequestMapping(value="/ajaxSetread")
	 @ResponseBody
	public Map  ajaxSetread(Model model,String[] ids,String newsType,String wmRead,String messageDate){
		ExecuteResult<String> result = baseWebSiteMessageService.modifyWebSiteMessage(ids,"2");
		model.addAttribute("msg", result.getResultMessage());
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", ids);
		if(result.isSuccess()){
			map.put("meessage", result.getResultMessage());
			return map;
		}else{
			map.put("meessage", result.getErrorMessages());
			return map;
		}
	}
	/**
	 * 根据id删除站内信
	 * 
	 * @author 王东晓
	 * create date 2015-05-28
	 * 
	 * @param ids  站内信id
	 * @return
	 */
	@RequestMapping("delNews")
	public String delNews(String[] ids,Model model,String newsType,String wmRead,String messageDate){
		
		ExecuteResult<String> result = baseWebSiteMessageService.modifyWebSiteMessage(ids,"3");
		String message  = "";
		if(result.isSuccess()){
			message = "删除成功";
		}else{
			message="删除失败";
		}
		model.addAttribute("message", message);
		return "redirect:/buyer/"+newsType+"?wmRead="+wmRead+"&messageDate="+messageDate;
	}
}