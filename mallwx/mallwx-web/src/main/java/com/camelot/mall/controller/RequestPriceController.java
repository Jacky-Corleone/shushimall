package com.camelot.mall.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.goodscenter.dto.InquiryMatDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.common.CommonService;
import com.camelot.mall.service.impl.CommonServiceImpl;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年6月2日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/requestPriceController")
public class RequestPriceController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private CommonServiceImpl commonServiceImpl;
	@Resource
	private MallBannerExportService mallBannerService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private MallRecExportService mallRecService;
	@Resource
	private InquiryExportService inquiryExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private CommonService commonService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private ShopExportService shopExportService;
	/**
	 * 跳转到询价主页面，同时将页面展示的数据准备好
	 * @author 成文涛 创建时间：2015-5-28 下午3:21:45
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("requestPrice")
	public String goRequestPrice(Pager pager, Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
    	Integer status = register.getuStatus();
//    	if(status !=4 && status != 6 ){
//    		return "redirect:/";
//    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("uid", userDTO.getUid());
		InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
		inquiryInfoDTO.setActiveFlag("1");
		inquiryInfoDTO.setCreateBy(userId+"");
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<DataGrid<InquiryInfoDTO>> er= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		//查询询价主单信息
		er = inquiryExportService.queryInquiryInfoList(inquiryInfoDTO, pager);
		SimpleDateFormat sm =  new SimpleDateFormat("yyyy-MM-dd");
		if( er.isSuccess() && er.getResult().getTotal() > 0 && er.getResult() != null ){
			JSONArray priceArr = new JSONArray();
			for( InquiryInfoDTO price: er.getResult().getRows() ){				
				//将物品的明细信息放到主表中
				//状态为1的只能查询主表得到物品名称
				ExecuteResult<DataGrid<Map>> detail = new ExecuteResult<DataGrid<Map>>();
				InquiryInfoDTO inquiryDTO = new InquiryInfoDTO();
				inquiryDTO.setActiveFlag("1");
				inquiryDTO.setInquiryNo(price.getInquiryNo());
				if("1".equals(price.getStatus())){
					//未提交状态的询价单没有生成明细只能查询主表关联产品大全当做明细信息展示
					detail =  inquiryExportService.queryInquiryInfoPager(inquiryDTO, pager);
				}else{
					InquiryMatDTO queryMaDTO = new InquiryMatDTO();
					queryMaDTO.setInquiryNo(price.getInquiryNo());
					queryMaDTO.setActiveFlag("1");
					//查询询价单明细表
					detail =  inquiryExportService.queryInquiryMatList(queryMaDTO, pager);
				}
				List<InquiryMatDTO> itemList = new ArrayList<InquiryMatDTO>();
				if(detail.isSuccess()){
					for(Map map : detail.getResult().getRows()){
						InquiryMatDTO matDTO = new InquiryMatDTO();
						if(map.get("id") != null && !"".equals(map.get("id"))){
							matDTO.setId(Long.parseLong(""+map.get("id")));
						}
						matDTO.setMatCd("" + map.get("matCd"));
						matDTO.setMatDesc(""+map.get("itemName"));
						if(map.get("shopId") != null && !"".equals(map.get("shopId"))){
							matDTO.setShopId(Integer.parseInt("" + map.get("shopId")));
							ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(Long.parseLong("" + map.get("shopId")));
							if(shop.getResult()!=null&&!shop.getResult().equals("")){	
								matDTO.setAlternate3(shop.getResult().getShopName());
							}

						}
						if(map.get("supplierId") != null && !"".equals(map.get("supplierId"))){
							matDTO.setSupplierId(Integer.parseInt("" + map.get("supplierId")));
							//获取供应商的名称
							if(map.get("shopId") != null && !"".equals(map.get("shopId"))){
								ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("shopId")));
								if(shop.getResult()!=null&&!shop.getResult().equals("")){
									matDTO.setAlternate1(shop.getResult().getShopName()!=null?shop.getResult().getShopName():"");
									//获取销售属性
									Map<String, String> mapAttr = new HashMap<String, String>();
									mapAttr.put("shopId","" + map.get("shopId"));
									mapAttr.put("skuId","" + map.get("matCd"));
									mapAttr.put("itemId","" + map.get("itemId"));
									mapAttr = putSalerAttr(mapAttr);
									matDTO.setAlternate4(mapAttr.get("salerAttr"));
									
								}
							
							}else{
								//李伟龙增加代码
								Map<String, String> mapAttr = new HashMap<String, String>();
								mapAttr.put("shopId","" + map.get("supplierId"));
								
								if(map.get("itemId").toString().equals(map.get("itemId").toString()) && map.get("skuId")!=null && !"".equals(map.get("skuId").toString())){
									mapAttr.put("skuId","" + map.get("skuId"));
								}else{
									mapAttr.put("skuId","" + map.get("matCd"));
								}
								
								mapAttr.put("itemId","" + map.get("itemId"));
								mapAttr = putSalerAttr(mapAttr);
								matDTO.setAlternate4(mapAttr.get("salerAttr"));
								//获取供应商名称
								ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("supplierId")));
								matDTO.setAlternate1(shop.getResult().getShopName());
								//李伟龙注释掉   下面两行 ，  经过走代码，判断出supplierId为shopid所以增加如上代码  ，
								//也判断出 matCd  == itemId为商品id，当matCd  ！= itemId  matCd为skuid
//								ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("supplierId")));
//								matDTO.setAlternate1(shop.getResult().getShopName());
							}
						}
						if(map.get("matPrice") != null && !"".equals(map.get("matPrice"))  && !"null".equals(map.get("matPrice"))){
							matDTO.setAlternate5("1");
							matDTO.setMatPrice(Double.parseDouble("" + map.get("matPrice")));
							matDTO.setStatus("" + map.get("status"));
						}
						if(!"1".equals(price.getStatus()) && map.get("beginDate") != null && !"null".equals(map.get("beginDate"))) {
							try {
								Date beginDate = sm.parse(""+map.get("beginDate"));
								Date endDate = sm.parse(""+map.get("endDate"));
								matDTO.setBeginDate(beginDate);
								matDTO.setEndDate(endDate);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						itemList.add(matDTO);
					}
				}
				price.setInquiryMatDTOs(itemList);
				priceArr.add(price);
			}
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords( priceArr );
		}
		JSONArray ja = commonServiceImpl.findCategory();
		model.addAttribute("itemList", ja);
		//全部报价
		model.addAttribute("pager", pager);			
		return "/requestPrice/requestPricePage";
	}
	
	/**
		 * 买家页面转换，全部询价、询价创建、待询价、待接收页面间的转换
		 * @author chengwt 创建时间：上午9:39:56
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping(value="/queryRequstPrice")
	public String queryAllRequstPrice(@RequestParam(value="flag",required=false,defaultValue="")String flag,
				@RequestParam(value="num",required=false,defaultValue="")String num,Model model,
				HttpServletRequest request,
				HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
    	Integer status = register.getuStatus();
//    	if(status !=4 && status != 6 ){
//    		return "redirect:/";
//    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		List<InquiryInfoDTO> dtos = new ArrayList<InquiryInfoDTO>();
		InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
		inquiryInfoDTO.setActiveFlag("1");
		inquiryInfoDTO.setCreateBy(userId+"");
		UserDTO userDTO = userExportService.queryUserById(userId);
		//全部询价
		if(!"1".equals(num)){
			if("2".equals(num)){
				//未提交询价
				inquiryInfoDTO.setStatus("1");
			}else if("3".equals(num)){
				//买家未确认询价
				inquiryInfoDTO.setStatus("2");
			}
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			ExecuteResult<DataGrid<InquiryInfoDTO>> er= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
			//查询主单信息
			er = inquiryExportService.queryInquiryInfoList(inquiryInfoDTO, pager);
			SimpleDateFormat sm =  new SimpleDateFormat("yyyy-MM-dd");
			if( er.isSuccess() && er.getResult() != null ){
				JSONArray priceArr = new JSONArray();
				for( InquiryInfoDTO price: er.getResult().getRows() ){
					JSONObject sp = JSON.parseObject(JSON.toJSONString(price));
					//将物品的明细信息放到主表中
					//状态为1的只能查询主表得到物品名称
					ExecuteResult<DataGrid<Map>> detail = new ExecuteResult<DataGrid<Map>>();
					InquiryInfoDTO inquiryDTO = new InquiryInfoDTO();
					inquiryDTO.setActiveFlag("1");
					inquiryDTO.setInquiryNo(price.getInquiryNo());
					if("1".equals(price.getStatus())){
						//未提交状态的询价单没有生成明细只能查询主表关联产品大全当做明细信息展示
						detail =  inquiryExportService.queryInquiryInfoPager(inquiryDTO, pager);
					}else{
						InquiryMatDTO queryMaDTO = new InquiryMatDTO();
						queryMaDTO.setInquiryNo(price.getInquiryNo());
						queryMaDTO.setActiveFlag("1");
						//查询求购单的明细
						detail =  inquiryExportService.queryInquiryMatList(queryMaDTO, pager);
					}
					List<InquiryMatDTO> itemList = new ArrayList<InquiryMatDTO>();
					if(detail.isSuccess()){
						for(Map map : detail.getResult().getRows()){
							InquiryMatDTO matDTO = new InquiryMatDTO();
							if(map.get("id") != null && !"".equals(map.get("id"))){
								matDTO.setId(Long.parseLong(""+map.get("id")));
							}
							matDTO.setMatCd(""+map.get("matCd"));
							matDTO.setMatDesc(""+map.get("itemName"));
							if(map.get("shopId") != null && !"".equals(map.get("shopId"))){
								matDTO.setShopId(Integer.parseInt(""+map.get("shopId")));
							}
							if(map.get("supplierId") != null && !"".equals(map.get("supplierId"))){
								matDTO.setSupplierId(Integer.parseInt(""+map.get("supplierId")));
								//获取供应商的名称
								if(map.get("shopId") != null && !"".equals(map.get("shopId"))){
									ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("shopId")));
									matDTO.setAlternate1(shop.getResult().getShopName());
									//获取销售属性
									Map<String, String> mapAttr = new HashMap<String, String>();
									mapAttr.put("shopId","" + map.get("shopId"));
									mapAttr.put("skuId","" + map.get("matCd"));
									mapAttr.put("itemId","" + map.get("itemId"));
									mapAttr = putSalerAttr(mapAttr);
									matDTO.setAlternate4(mapAttr.get("salerAttr"));
								}else{
									//李伟龙增加代码
									Map<String, String> mapAttr = new HashMap<String, String>();
									mapAttr.put("shopId","" + map.get("supplierId"));
									
									if(map.get("itemId").toString().equals(map.get("itemId").toString()) && map.get("skuId")!=null && !"".equals(map.get("skuId").toString())){
										mapAttr.put("skuId","" + map.get("skuId"));
									}else{
										mapAttr.put("skuId","" + map.get("matCd"));
									}
									
									mapAttr.put("itemId","" + map.get("itemId"));
									mapAttr = putSalerAttr(mapAttr);
									matDTO.setAlternate4(mapAttr.get("salerAttr"));
									//获取供应商名称
									ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("supplierId")));
									matDTO.setAlternate1(shop.getResult().getShopName());
									//李伟龙注释掉   下面两行 ，  经过走代码，判断出supplierId为shopid所以增加如上代码  ，
									//也判断出 matCd  == itemId为商品id，当matCd  ！= itemId  matCd为skuid
//									ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("supplierId")));
//									matDTO.setAlternate1(shop.getResult().getShopName());
								}
							}
							if(map.get("matPrice") != null && !"".equals(map.get("matPrice")) && !"null".equals(map.get("matPrice"))){
								matDTO.setStatus("" + map.get("status"));
								matDTO.setAlternate5("1");
								matDTO.setMatPrice(Double.parseDouble("" + map.get("matPrice")));
							}
							if(!"1".equals(price.getStatus()) && map.get("beginDate") != null && !"null".equals(map.get("beginDate"))) {
								try {
									Date beginDate = sm.parse(""+map.get("beginDate"));
									Date endDate = sm.parse(""+map.get("endDate"));
									matDTO.setBeginDate(beginDate);
									matDTO.setEndDate(endDate);
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							itemList.add(matDTO);
						}
					}
					price.setInquiryMatDTOs(itemList);
					priceArr.add(price);
				}
				pager.setTotalCount(er.getResult().getTotal().intValue());
				pager.setRecords( priceArr );
			}
			model.addAttribute("pager", pager);
		}else{
			//询价创建页面
			//调用dubbo生成询价单编号
			ExecuteResult<String> inquiryNo =  inquiryExportService.createInquiryNo();
			if(inquiryNo.isSuccess()){
				model.addAttribute("inquiryNo", inquiryNo.getResult());
			}
			SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd"); 
			String beginDate = sm.format(new Date());
			UserDTO userDto = userExportService.queryUserById(userId);
			model.addAttribute("beginDate", beginDate);
			model.addAttribute("uid", userId);
			if("".equals(userDto.getUname())){
				model.addAttribute("uName", "登录名为空");
			}else{
				model.addAttribute("uName", userDto.getUname());
			}
		}
		model.addAttribute("num", num);
		
		return "/requestPrice/requestPriceAll";
	}
	
	
	
	 /**
	 * 跳往询价修改页面
	 * @author chengwt 创建时间：上午9:30:08
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping("/updateInquiry")
	public String updateInquiry(HttpServletRequest request,
			HttpServletResponse response, Model model ){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
    	Integer status = register.getuStatus();
//    	if(status !=4 && status != 6 ){
//    		return "redirect:/";
//    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
	   	ExecuteResult<String> result = new ExecuteResult<String>();
	   	String inquiryNo = request.getParameter("inquiryNo");
	   	List<String> errs = new ArrayList<String>();
	   	UserDTO userDTO = userExportService.queryUserById(userId);
	   	InquiryInfoDTO dto = new InquiryInfoDTO();
	   	dto.setActiveFlag("1");
	   	dto.setInquiryNo(inquiryNo);
	   	ExecuteResult<DataGrid<InquiryInfoDTO>> inquiryDTOS= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
	   	Pager pager = new Pager();
	   	pager.setPage(1);
	   	pager.setRows(Integer.MAX_VALUE);
		//查询主单信息
	   	inquiryDTOS = inquiryExportService.queryInquiryInfoList(dto, pager);
		//未提交状态的询价单没有生成明细只能查询主表关联产品大全当做明细信息展示
	   	ExecuteResult<DataGrid<Map>> inquiryDTOList =  inquiryExportService.queryInquiryInfoPager(dto, pager);
		if(inquiryDTOS.isSuccess() && inquiryDTOS.getResult().getTotal() > 0){
			InquiryInfoDTO inquiryInfoDTO = inquiryDTOS.getResult().getRows().get(0);
			model.addAttribute("inquiryId", inquiryInfoDTO.getId());
			model.addAttribute("inquiryNo", inquiryInfoDTO.getInquiryNo());
			model.addAttribute("inquiryName", inquiryInfoDTO.getInquiryName());
			model.addAttribute("printerName", inquiryInfoDTO.getInquiryName());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			if(inquiryInfoDTO.getAnnex() == null ||  "".equals(inquiryInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != inquiryInfoDTO.getAnnex()) {
					fileName = inquiryInfoDTO.getAnnex().substring(inquiryInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", inquiryInfoDTO.getAnnex());
			}
			
			
			model.addAttribute("beginDate", inquiryInfoDTO.getBeginDate());
			model.addAttribute("endDate", inquiryInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", inquiryInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", inquiryInfoDTO.getRemarks());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			//取出map放入list中，前台展示用
			int i = 1;
			for(Map mapp : inquiryDTOList.getResult().getRows()){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				map.put("detailId", ""+mapp.get("id"));
				map.put("matCd", ""+mapp.get("matCd"));
				map.put("matDesc", ""+mapp.get("itemName"));
				map.put("shopId", ""+mapp.get("supplierId"));
				ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+mapp.get("supplierId")));
				
				//获取销售属性
				Map<String, String> mapAttr = new HashMap<String, String>();
				mapAttr.put("shopId","" + mapp.get("supplierId"));
				if(mapp.get("itemId").toString().equals(mapp.get("itemId").toString()) && mapp.get("skuId")!=null && !"".equals(mapp.get("skuId").toString())){
					mapAttr.put("skuId","" + mapp.get("skuId"));
				}else{
					mapAttr.put("skuId","" + mapp.get("matCd"));
				}
				mapAttr.put("itemId","" + mapp.get("itemId"));
				mapAttr = putSalerAttr(mapAttr);
				map.put("skuId","" + mapp.get("skuId"));
				map.put("salerAttr", mapAttr.get("salerAttr"));
				map.put("shopName", shop.getResult().getShopName());
				map.put("quantity", ""+mapp.get("quantity"));
				map.put("flag", "o");
				map.put("deliveryDate2", ""+mapp.get("deliveryDate2"));
				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
			model.addAttribute("update_flag", "1");
			JSONArray ja = commonServiceImpl.findCategory();
	        model.addAttribute("itemList", ja);
	        model.addAttribute("uid", userId);
	        model.addAttribute("uName", userDTO.getUname());
	        model.addAttribute("updateBy", userId);
		}else{
			//获取信息失败，则直接返回原页面
			return "/requestPrice/requestPricePage";
		}
		return "/requestPrice/requestPriceUpdate";
	}

	/**
	 * 跳往询价查看页面
	 * @author chengwt 创建时间：上午9:30:08
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping("/lookInquiry")
	public String lookInquiry( HttpServletRequest request,
			HttpServletResponse response, Model model ){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
	   	ExecuteResult<String> result = new ExecuteResult<String>();
	   	String inquiryNo = request.getParameter("inquiryNo");
	   	String flag = request.getParameter("flag");
	   	String resultStr = "/requestPrice/requestPriceShow";
	   	if("responsePrice".equals(flag)){
	   		resultStr = "/responsePrice/responsePriceShow";
	   	}
	   	List<String> errs = new ArrayList<String>();
	   	InquiryInfoDTO dto = new InquiryInfoDTO();
	   	dto.setActiveFlag("1");
	   	dto.setInquiryNo(inquiryNo);
	   	ExecuteResult<DataGrid<InquiryInfoDTO>> inquiryDTOS= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
	   	Pager pager = new Pager();
	   	pager.setPage(1);
	   	pager.setRows(Integer.MAX_VALUE);
		//查询主单信息
	   	inquiryDTOS = inquiryExportService.queryInquiryInfoList(dto, pager);
		if(inquiryDTOS.isSuccess() && inquiryDTOS.getResult().getTotal() > 0){
			InquiryInfoDTO inquiryInfoDTO = inquiryDTOS.getResult().getRows().get(0);
			UserDTO userDTO = userExportService.queryUserById(Long.parseLong(inquiryInfoDTO.getCreateBy()));
			model.addAttribute("uName", userDTO.getUname());
			model.addAttribute("inquiryId", inquiryInfoDTO.getId());
			model.addAttribute("inquiryNo", inquiryInfoDTO.getInquiryNo());
			model.addAttribute("inquiryName", inquiryInfoDTO.getInquiryName());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			model.addAttribute("status", inquiryInfoDTO.getStatus());
			if(inquiryInfoDTO.getAnnex() == null ||  "".equals(inquiryInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != inquiryInfoDTO.getAnnex()) {
					fileName = inquiryInfoDTO.getAnnex().substring(inquiryInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", inquiryInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", inquiryInfoDTO.getBeginDate());
			model.addAttribute("endDate", inquiryInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", inquiryInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", inquiryInfoDTO.getRemarks());
			ExecuteResult<DataGrid<Map>> inquiryDTOList = new ExecuteResult<DataGrid<Map>>();
			if("1".equals(inquiryInfoDTO.getStatus())){
				//未提交状态的询价单没有生成明细只能查询主表关联产品大全当做明细信息展示
				inquiryDTOList =  inquiryExportService.queryInquiryInfoPager(dto, pager);
			}else{
				InquiryMatDTO  inquiryMatDTO = new InquiryMatDTO();
				inquiryMatDTO.setActiveFlag("1");
				inquiryMatDTO.setInquiryNo(inquiryInfoDTO.getInquiryNo());
				if("responsePrice".equals(flag)) {
					UserDTO seller = userExportService.queryUserById(userId);
					inquiryMatDTO.setShopId(seller.getShopId().intValue());
				}
				//查询明细信息
				inquiryDTOList =  inquiryExportService.queryInquiryMatList(inquiryMatDTO, pager);
			}
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			//取出map放入list中，前台展示用
			int i = 1;
			if(inquiryDTOList.isSuccess() && inquiryDTOList.getResult().getTotal() > 0){
				for(Map mapp : inquiryDTOList.getResult().getRows()){
					map = new HashMap<String, String>();
					//查询供应商名称
					map.put("no", ""+i);
					map.put("matCd", ""+mapp.get("matCd"));
					map.put("matDesc", ""+mapp.get("itemName"));
					if(mapp.get("shopId") != null && !"".equals(mapp.get("shopId"))){
						ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+mapp.get("shopId")));
						map.put("shopId", ""+mapp.get("shopId"));
						map.put("shopName", shop.getResult().getShopName());
					}else{
						ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+mapp.get("supplierId")));
						map.put("shopId", ""+mapp.get("supplierId"));
						map.put("shopName", shop.getResult().getShopName());
					}
					map.put("deliveryDate", ""+mapp.get("deliveryDate"));
					map.put("quantity", ""+mapp.get("quantity"));
					map.put("flag", "1");
					map.put("deliveryDate2", ""+mapp.get("deliveryDate2"));
					if("2".equals(inquiryInfoDTO.getStatus()) || "3".equals(inquiryInfoDTO.getStatus())){
						if("null".equals(""+mapp.get("beginDate"))){
							map.put("beginDate", "");
						}else{
							map.put("beginDate", ""+mapp.get("beginDate"));
						}
						if("null".equals(""+mapp.get("endDate"))){
							map.put("endDate", "");
						}else{
							map.put("endDate", ""+mapp.get("endDate"));
						}
						if( "null".equals(""+mapp.get("matPrice")) ){
							map.put("matPrice", "");
						}else{
							map.put("matPrice", ""+mapp.get("matPrice"));
						}
						//获取销售属性
						Map<String, String> mapAttr = new HashMap<String, String>();
						mapAttr.put("shopId","" + mapp.get("shopId"));
						mapAttr.put("skuId","" + mapp.get("matCd"));
						mapAttr.put("itemId","" + mapp.get("itemId"));
						mapAttr = putSalerAttr(mapAttr);
						map.put("attrSale", mapAttr.get("salerAttr"));
					}else{
						//获取销售属性
						Map<String, String> mapAttr = new HashMap<String, String>();
						mapAttr.put("shopId","" + mapp.get("supplierId"));
						
						if(mapp.get("matCd").toString().equals(mapp.get("itemId").toString()) && mapp.get("skuId")!=null && !"".equals(mapp.get("skuId").toString())){
							mapAttr.put("skuId","" + mapp.get("skuId"));
						}else{
							mapAttr.put("skuId","" + mapp.get("matCd"));
						}
						mapAttr.put("itemId","" + mapp.get("itemId"));
						mapAttr = putSalerAttr(mapAttr);
						map.put("attrSale", mapAttr.get("salerAttr"));
					}
					inquiryMatDTOs.add(map);
					i += 1;
				}
			}
			model.addAttribute("details", inquiryMatDTOs);
		}else{
			if("responsePrice".equals(flag)){
			//获取信息失败，则直接返回原页面
				return "/responsePrice/responsePricePage";
			}else{
				return "/requestPrice/requestPricePage";
			}
		}
		return resultStr;
	}
	
	/**
		 * 跳往产品大全选择页面
		 * @author chengwt 创建时间：下午2:47:47
		 * @param model
		 * @param request
		 */
	@RequestMapping("/getItemList")
    public String getItemInfoDetail(Long brandId,
									String attributes,
									String cid,
									String areaCode,
									@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
									@RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
									Integer orderSort,
									String keyword,
									HttpServletRequest request,
									Model model) {
	    	Long id = null;
	    	if( cid != null && !"".equals(cid) ){
	    		if( cid.indexOf(":") != -1 ){
	        		String tmp = cid.substring(cid.lastIndexOf(":")+1, cid.length());
	        		id = Long.valueOf(tmp);
	    		}else{
	    			id = Long.valueOf(cid);
	    		}
	    	}
	    	Pager<ItemQueryOutDTO> pager = new Pager<ItemQueryOutDTO>();
	    	logger.debug("产品大全-商品列表查询参数："+id);
	    	pager.setRows(5);
			if (null != page) {
				pager.setPage(page);
			}
			ItemQueryInDTO inDTO = new ItemQueryInDTO();
			inDTO.setCid(id);
			inDTO.setItemName(keyword);
			//inDTO.setXgjFlag("1");
	    	ExecuteResult<DataGrid<ItemQueryOutDTO>>  er = this.itemService.queryXgjItemByCid(inDTO, pager);
	    	List<ItemQueryOutDTO> items = er.getResult().getRows();
	
	    	model.addAttribute("pager", pager);
	    	model.addAttribute("cid", cid);
			List<ItemQueryOutDTO> itemList = new ArrayList<ItemQueryOutDTO>();
	    	//根据物品信息找到店铺信息，供用户在页面选择供应商
	    	Map<String, List<ShopDTO>> maps = new HashMap<String, List<ShopDTO>>();
	    	for(ItemQueryOutDTO outDTO : items){
	    		ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
	    		itemInDTO.setPlatItemId(outDTO.getItemId());
	    		Pager<ItemQueryOutDTO> pagerList = new Pager<ItemQueryOutDTO>();
	    		pagerList.setRows(Integer.MAX_VALUE);
	    		pagerList.setPage(1);
	    		DataGrid<ItemQueryOutDTO> shopList = itemService.queryItemList(itemInDTO, pagerList);
	    		//查询店铺名字
	    		List<ShopDTO> shops = new ArrayList<ShopDTO>();
	    		if(shopList.getTotal() > 0){
		    		for(ItemQueryOutDTO out : shopList.getRows()){
		    			if(out.getShopId() != null && !"".equals(out.getShopId())){
			    			ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(out.getShopId().longValue());
			    			ShopDTO shopDto = new ShopDTO();
			    			shopDto.setShopId(out.getShopId());
			    			shopDto.setShopName(shop.getResult().getShopName());
			    			//李伟龙增加代码 start  临时使用LinkMan1字段作为商品id传到前台页面
			    			shopDto.setLinkMan1(out.getItemId()+"");
			    			//李伟龙增加代码 end
			    			shops.add(shopDto);
		    			}
		    		}
					if(shops.size() > 0){
						maps.put(""+outDTO.getItemId(), shops);
						itemList.add(outDTO);
					}
	    		}
	    	}
	    	page += 1;
			model.addAttribute("items", items);
	    	model.addAttribute("maps", maps);
	    	model.addAttribute("totalPage", er.getResult().getTotal());
	    	model.addAttribute("page", page);
	        model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
	        model.addAttribute("keyword",keyword);
			model.addAttribute("cid",cid);
	        return "/requestPrice/itemSelectPage";

    }
	

	/**
		 * 卖家询价主页面
		 * @author chengwt 创建时间：下午7:11:13
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("responsePrice")
	public String goresponsePrice(Pager pager, Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("uid", userDTO.getUid());
		if(userDTO.getShopId() != null && !"".equals(userDTO.getShopId())) {
			ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(userDTO.getShopId());
			model.addAttribute("shopName", shop.getResult().getShopName());
		}
		List<InquiryInfoDTO> dtos = new ArrayList<InquiryInfoDTO>();
		InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
		inquiryInfoDTO.setSupplierId(userDTO.getShopId().intValue());
		inquiryInfoDTO.setActiveFlag("1");
		List<String> statusList = new ArrayList<String>();
		statusList.add("2");
		statusList.add("3");
		statusList.add("4");
		statusList.add("5");
		inquiryInfoDTO.setStatusList(statusList);
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<DataGrid<InquiryInfoDTO>> er= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		//查询主单信息
		er = inquiryExportService.queryInquiryInfoList(inquiryInfoDTO, pager);
		SimpleDateFormat sm =  new SimpleDateFormat("yyyy-MM-dd");
		if( er.isSuccess() && er.getResult().getTotal() > 0 && er.getResult() != null ){
			JSONArray priceArr = new JSONArray();
			for( InquiryInfoDTO price: er.getResult().getRows() ){
				
				JSONObject sp = JSON.parseObject(JSON.toJSONString(price));
				//其他状态查询明细，获取供应商/物品价格等信息
				InquiryMatDTO queryMaDTO = new InquiryMatDTO();
				queryMaDTO.setActiveFlag("1");
				queryMaDTO.setInquiryNo(price.getInquiryNo());
				//只能查询当前卖家自己的询价单据
				queryMaDTO.setShopId(userDTO.getShopId().intValue());
				//查询明细信息
				ExecuteResult<DataGrid<Map>> detail =  inquiryExportService.queryInquiryMatList(queryMaDTO, pager);
				List<InquiryMatDTO> itemList = new ArrayList<InquiryMatDTO>();
				if(detail.isSuccess() && detail.getResult().getTotal() > 0){
					for(Map map : detail.getResult().getRows()){
						InquiryMatDTO matDTO = new InquiryMatDTO();
						if(map.get("id") != null && !"".equals(map.get("id"))){
							matDTO.setId(Long.parseLong(""+map.get("id")));
						}
						matDTO.setMatDesc(""+map.get("itemName"));
						matDTO.setAlternate2("" + map.get("cName"));
						if(map.get("shopId") != null && !"".equals(map.get("shopId"))){
							matDTO.setStatus("" + map.get("status"));
							matDTO.setSupplierId(Integer.parseInt(""+map.get("shopId")));
							ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(Long.parseLong("" + map.get("shopId")));
							matDTO.setAlternate1(shop.getResult().getShopName());
						}
						if(map.get("matPrice") != null && !"".equals(map.get("matPrice")) && !"null".equals(map.get("matPrice"))){
							matDTO.setMatPrice(Double.parseDouble("" + map.get("matPrice")));
						}
						if(map.get("beginDate") != null && !"null".equals(map.get("beginDate"))) {
							try {
								Date beginDate = sm.parse(""+map.get("beginDate"));
								Date endDate = sm.parse(""+map.get("endDate"));
								matDTO.setBeginDate(beginDate);
								matDTO.setEndDate(endDate);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						//获取销售属性
						Map<String, String> mapAttr = new HashMap<String, String>();
						mapAttr.put("shopId","" + map.get("shopId"));
						mapAttr.put("skuId","" + map.get("matCd"));
						mapAttr.put("itemId","" + map.get("itemId"));
						mapAttr = putSalerAttr(mapAttr);
						matDTO.setAlternate4(mapAttr.get("salerAttr"));
						itemList.add(matDTO);
					}
				}else{
					//查询明细表没有数据，则此条主单也不显示在卖家询价列表中
					continue;
				}
				if(itemList.size() > 0){
					price.setInquiryMatDTOs(itemList);
					priceArr.add(price);
				}
			}
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords( priceArr );
		}
		
		//全部报价
		model.addAttribute("pager", pager);
				
		
		return "/responsePrice/responsePricePage";
	}
	
	/**
		 * 卖家询价页面，全部报价、报价中、已接收询价页面转换
		 * @author chengwt 创建时间：上午9:40:39
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping(value="/queryResponsePrice")
	public String queryResponsePrice(@RequestParam(value="flag",required=false,defaultValue="")String flag,
				@RequestParam(value="num",required=false,defaultValue="")String num, HttpServletRequest request,
				HttpServletResponse response, Model model){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		List<InquiryInfoDTO> dtos = new ArrayList<InquiryInfoDTO>();
		InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
		inquiryInfoDTO.setActiveFlag("1");
		//待询价
		if("1".equals(num)){
			//已报价
			inquiryInfoDTO.setStatus("2");
		}else if("2".equals(num)){
			//已确认
			inquiryInfoDTO.setStatus("3");
		}else{
			List<String> statusList = new ArrayList<String>();
			statusList.add("2");
			statusList.add("3");
			inquiryInfoDTO.setStatusList(statusList);
		}
		UserDTO seller = this.userExportService.queryUserById(userId);
		inquiryInfoDTO.setSupplierId(seller.getShopId().intValue());
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<DataGrid<InquiryInfoDTO>> er= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		//查询询价单主单信息
		er = inquiryExportService.queryInquiryInfoList(inquiryInfoDTO, pager);
		SimpleDateFormat sm =  new SimpleDateFormat("yyyy-MM-dd");
		if( er.isSuccess() && er.getResult().getTotal() > 0 && er.getResult() != null ){
			JSONArray priceArr = new JSONArray();
			for( InquiryInfoDTO price: er.getResult().getRows() ){
				
				JSONObject sp = JSON.parseObject(JSON.toJSONString(price));
				
				//其他状态查询明细，获取供应商/物品价格等信息
				InquiryMatDTO queryMaDTO = new InquiryMatDTO();
				queryMaDTO.setActiveFlag("1");
				queryMaDTO.setInquiryNo(price.getInquiryNo());
				queryMaDTO.setShopId(seller.getShopId().intValue());
				//查询询价单明细信息
				ExecuteResult<DataGrid<Map>> detail =  inquiryExportService.queryInquiryMatList(queryMaDTO, pager);
				List<InquiryMatDTO> itemList = new ArrayList<InquiryMatDTO>();
				if(detail.isSuccess() && detail.getResult().getTotal() > 0){
					for(Map map : detail.getResult().getRows()){
						InquiryMatDTO matDTO = new InquiryMatDTO();
						if(map.get("id") != null && !"".equals(map.get("id"))){
							matDTO.setId(Long.parseLong(""+map.get("id")));
						}
						matDTO.setMatDesc(""+map.get("itemName"));
						matDTO.setAlternate2(""+map.get("cName"));
						if(map.get("shopId") != null && !"".equals(map.get("shopId"))){
							matDTO.setSupplierId(Integer.parseInt(""+map.get("shopId")));
							matDTO.setStatus("" + map.get("status"));
							ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(Long.parseLong("" + map.get("shopId")));
							matDTO.setAlternate1(shop.getResult().getShopName());
						}
						if(map.get("matPrice") != null && !"".equals(map.get("matPrice")) && !"null".equals(map.get("matPrice"))){
							matDTO.setMatPrice(Double.parseDouble("" + map.get("matPrice")));
						}
						if(map.get("beginDate") != null && !"null".equals(map.get("beginDate"))) {
							try {
								Date beginDate = sm.parse(""+map.get("beginDate"));
								Date endDate = sm.parse(""+map.get("endDate"));
								matDTO.setBeginDate(beginDate);
								matDTO.setEndDate(endDate);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						//获取销售属性
						Map<String, String> mapAttr = new HashMap<String, String>();
						mapAttr.put("shopId","" + map.get("shopId"));
						mapAttr.put("skuId","" + map.get("matCd"));
						mapAttr.put("itemId","" + map.get("itemId"));
						mapAttr = putSalerAttr(mapAttr);
						matDTO.setAlternate4(mapAttr.get("salerAttr"));
						itemList.add(matDTO);
					}
				}else{
					//查询明细表没有数据，则此条主单也不显示在卖家询价列表中
					continue;
				}
				if(itemList.size() > 0){
					price.setInquiryMatDTOs(itemList);
					priceArr.add(price);
				}
			}
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords( priceArr );
		}
		model.addAttribute("pager", pager);
		model.addAttribute("uid", seller.getShopId());
		if(userDTO.getShopId() != null && !"".equals(userDTO.getShopId())) {
			//根据店铺id获取店铺name
			ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(seller.getShopId());
			model.addAttribute("shopName", shop.getResult().getShopName());
		}
		model.addAttribute("num", num);
		
		return "/responsePrice/responseAll";
	}

	
	
	/**
		 * 卖家进入编辑报价界面
		 * @author chengwt 创建时间：上午10:20:19
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/updateResponseInquiry")
	public String updateResponseInquiry(HttpServletRequest request, HttpServletResponse response, Model model ){
	   	ExecuteResult<String> result = new ExecuteResult<String>();
	   	String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
	   	String inquiryNo = request.getParameter("inquiryNo");
	   	List<String> errs = new ArrayList<String>();
	   	InquiryInfoDTO dto = new InquiryInfoDTO();
	   	dto.setActiveFlag("1");
	   	dto.setInquiryNo(inquiryNo);
	   	ExecuteResult<DataGrid<InquiryInfoDTO>> inquiryDTOS= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
	   	Pager pager = new Pager();
	   	pager.setPage(1);
	   	pager.setRows(Integer.MAX_VALUE);
		//查询询价主单信息
	   	inquiryDTOS = inquiryExportService.queryInquiryInfoList(dto, pager);
	   	InquiryMatDTO inquiryMatDTO = new InquiryMatDTO();
	   	inquiryMatDTO.setActiveFlag("1");
	   	inquiryMatDTO.setInquiryNo(inquiryNo);
	   	UserDTO seller = this.userExportService.queryUserById(userId);
	   	inquiryMatDTO.setShopId(seller.getShopId().intValue());
	   	Pager pager2 = new Pager();
	   	pager2.setPage(1);
	   	pager2.setRows(Integer.MAX_VALUE);
		//查询询价单明细信息
	   	ExecuteResult<DataGrid<Map>> inquiryDTOList =  inquiryExportService.queryInquiryMatList(inquiryMatDTO, pager2);
		if(inquiryDTOS.isSuccess() && inquiryDTOS.getResult().getTotal() > 0){
			InquiryInfoDTO inquiryInfoDTO = inquiryDTOS.getResult().getRows().get(0);
			model.addAttribute("inquiryId", inquiryInfoDTO.getId());
			model.addAttribute("inquiryNo", inquiryInfoDTO.getInquiryNo());
			model.addAttribute("inquiryName", inquiryInfoDTO.getInquiryName());
			UserDTO userDTO = userExportService.queryUserById(Long.parseLong(inquiryInfoDTO.getCreateBy()));
			model.addAttribute("printerName", userDTO.getUname());
			model.addAttribute("supplierId", inquiryInfoDTO.getSupplierId());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			if(inquiryInfoDTO.getAnnex() == null ||  "".equals(inquiryInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != inquiryInfoDTO.getAnnex()) {
					fileName = inquiryInfoDTO.getAnnex().substring(inquiryInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", inquiryInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", inquiryInfoDTO.getBeginDate());
			model.addAttribute("endDate", inquiryInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", inquiryInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", inquiryInfoDTO.getRemarks());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			//取出map放入list中，前台展示用
			int i = 1;
			for(Map mapp : inquiryDTOList.getResult().getRows()){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				map.put("detailId", ""+mapp.get("id"));
				map.put("matCd", ""+mapp.get("matCd"));
				map.put("matDesc", ""+mapp.get("itemName"));
				//获取销售属性
				Map<String, String> mapAttr = new HashMap<String, String>();
				mapAttr.put("shopId","" + mapp.get("shopId"));
				mapAttr.put("skuId","" + mapp.get("matCd"));
				mapAttr.put("itemId","" + mapp.get("itemId"));
				mapAttr = putSalerAttr(mapAttr);
				map.put("attrSale", mapAttr.get("salerAttr"));
				map.put("quantity", ""+mapp.get("quantity"));
				if("null".equals(""+mapp.get("beginDate"))){
					map.put("beginDate", "");
				}else{
					map.put("beginDate", ""+mapp.get("beginDate"));
				}
				if("null".equals(""+mapp.get("endDate"))){
					map.put("endDate", "");
				}else{
					map.put("endDate", ""+mapp.get("endDate"));
				}
				if( mapp.get("matPrice") == null || "null".equals(""+mapp.get("matPrice")) || "".equals(""+mapp.get("matPrice"))){
					map.put("matPrice", "");
				}else{
					map.put("matPrice", ""+mapp.get("matPrice"));
				}
				map.put("deliveryDate2", ""+mapp.get("deliveryDate2"));

				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
		}else{
			//报错时跳转到卖家询价首页
			return "/responsePrice/responsePricePage";
		}
		return "/responsePrice/responsePriceUpdate";
	}
	
	   /**
 	 * 买家卖家询价按钮查询
 	 * @author chengwt 创建时间：上午9:29:58
 	 * @param model
 	 * @param request
 	 * @param response
 	 */
 @RequestMapping("/queryInquiry")
	public String queryInquiry(	HttpServletRequest request, HttpServletResponse response, Model model ){
	   	String ctoken = LoginToken.getLoginToken(request);
	 	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
	 	RegisterDTO register = userExportService.getUserByRedis(ctoken);
	 	if(register==null){
	 		return "redirect:/";
	 	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		ExecuteResult<String> result = new ExecuteResult<String>();
		String supplierName = request.getParameter("supplierName");
		String itemName = request.getParameter("itemName");
		String inquiryNo = request.getParameter("inquiryNo");
		String supplierId = request.getParameter("supplierId");
	 //flag为request的表示是买家查询，否则为卖家查询
		String flag = request.getParameter("flag");
	 //区分是那个页面的查询，根据此添加相应的询价单状态
		String num = request.getParameter("num");
		List<String> errs = new ArrayList<String>();
		UserDTO userDTO = userExportService.queryUserById(userId);
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
		inquiryInfoDTO.setActiveFlag("1");
		List<String> supplierIdList = new ArrayList<String>();
		if("request".equals(flag)){
			inquiryInfoDTO.setCreateBy(userId+"");
			if(!"".equals(supplierName)){
				
				ShopDTO shopDTO = new ShopDTO();
				shopDTO.setShopName(supplierName);
				//根据店铺id获取店铺名称
				ExecuteResult<DataGrid<ShopDTO>> shopList = shopExportService.findShopInfoByCondition(shopDTO, pager);
				if(shopList.getResult().getTotal() > 0){
					for(ShopDTO shop : shopList.getResult().getRows()){
						supplierIdList.add(""+shop.getShopId());
					}
					inquiryInfoDTO.setSupplierIdList(supplierIdList);
				}else{
					pager.setTotalCount(0);
					pager.setRecords(null);
					model.addAttribute("pager", pager);
					if("request".equals(flag)){
						return "/requestPrice/requestPriceModelPage";
					}else{
						return "/responsePrice/responsePriceModelPage";
					}
				}
			}
		}else{
			inquiryInfoDTO.setSupplierId(userDTO.getShopId().intValue());
		}
		inquiryInfoDTO.setItemName(itemName);
		inquiryInfoDTO.setInquiryNo(inquiryNo);
		ExecuteResult<DataGrid<InquiryInfoDTO>> er= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		if("request".equals(flag)){
			if("2".equals(num)){
				inquiryInfoDTO.setStatus("1");
			}else if("3".equals(num)){
				inquiryInfoDTO.setStatus("2");
			}
		}else{
			if("0".equals(num)){
				List<String> statusList = new ArrayList<String>();
				statusList.add("2");
				statusList.add("3");
				inquiryInfoDTO.setStatusList(statusList);
			}else if("1".equals(num)){
				List<String> statusList = new ArrayList<String>();
				statusList.add("2");
				inquiryInfoDTO.setStatusList(statusList);
			}else if("2".equals(num)){
				inquiryInfoDTO.setStatus("2");
			}else if("3".equals(num)){
				inquiryInfoDTO.setStatus("3");
			}
		}
	 //查询询价单主单信息
		er = inquiryExportService.queryInquiryInfoList(inquiryInfoDTO, pager);
	 	SimpleDateFormat sm =  new SimpleDateFormat("yyyy-MM-dd");
		if( er.isSuccess() && er.getResult() != null ){
			JSONArray priceArr = new JSONArray();
			for( InquiryInfoDTO price: er.getResult().getRows() ){
				
				JSONObject sp = JSON.parseObject(JSON.toJSONString(price));
				
				//将物品的明细信息放到主表中
				//状态为1的只能查询主表得到物品名称
				ExecuteResult<DataGrid<Map>> detail = new ExecuteResult<DataGrid<Map>>();
				InquiryInfoDTO inquiryDTO = new InquiryInfoDTO();
				inquiryDTO.setActiveFlag("1");
				inquiryDTO.setInquiryNo(price.getInquiryNo());
				if("1".equals(price.getStatus())){
					//未提交状态的询价单没有生成明细只能查询主表关联产品大全当做明细信息展示
					detail =  inquiryExportService.queryInquiryInfoPager(inquiryDTO, pager);
				}else{
					InquiryMatDTO queryMaDTO = new InquiryMatDTO();
					queryMaDTO.setInquiryNo(price.getInquiryNo());
					queryMaDTO.setActiveFlag("1");
					if(!"request".equals(flag)){
						if(userDTO.getShopId() != null) {
							queryMaDTO.setShopId(userDTO.getShopId().intValue());
						}
					}
					//查询询价单明细信息
					detail =  inquiryExportService.queryInquiryMatList(queryMaDTO, pager);
				}
				List<InquiryMatDTO> itemList = new ArrayList<InquiryMatDTO>();
				if(detail.isSuccess()){
					for(Map map : detail.getResult().getRows()){
						InquiryMatDTO matDTO = new InquiryMatDTO();
						if(map.get("id") != null && !"".equals(map.get("id"))){
							matDTO.setId(Long.parseLong(""+map.get("id")));
						}
						matDTO.setMatCd(""+ map.get("matCd"));
						matDTO.setMatDesc(""+map.get("itemName"));
						if(map.get("shopId") != null && !"".equals(map.get("shopId"))){
							matDTO.setStatus(""+map.get("status"));
							matDTO.setShopId(Integer.parseInt(""+map.get("shopId")));
							ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(Long.parseLong("" + map.get("shopId")));
							matDTO.setAlternate3(shop.getResult().getShopName());
						}
						if(map.get("supplierId") != null && !"".equals(map.get("supplierId"))){
							matDTO.setSupplierId(Integer.parseInt(""+map.get("supplierId")));
							//获取供应商的名称
							if(map.get("shopId") != null && !"".equals(map.get("shopId"))){
								ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("shopId")));
								matDTO.setAlternate1(shop.getResult().getShopName());
								//获取销售属性
								Map<String, String> mapAttr = new HashMap<String, String>();
								mapAttr.put("shopId","" + map.get("shopId"));
								mapAttr.put("skuId","" + map.get("matCd"));
								mapAttr.put("itemId","" + map.get("itemId"));
								mapAttr = putSalerAttr(mapAttr);
								matDTO.setAlternate4(mapAttr.get("salerAttr"));
							}else{
								//李伟龙增加代码
								Map<String, String> mapAttr = new HashMap<String, String>();
								mapAttr.put("shopId","" + map.get("supplierId"));
								
								if(map.get("itemId").toString().equals(map.get("itemId").toString()) && map.get("skuId")!=null && !"".equals(map.get("skuId").toString())){
									mapAttr.put("skuId","" + map.get("skuId"));
								}else{
									mapAttr.put("skuId","" + map.get("matCd"));
								}
								
								mapAttr.put("itemId","" + map.get("itemId"));
								mapAttr = putSalerAttr(mapAttr);
								matDTO.setAlternate4(mapAttr.get("salerAttr"));
								//获取供应商名称
								ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("supplierId")));
								matDTO.setAlternate1(shop.getResult().getShopName());
								
								//李伟龙注释掉   下面两行 ，  经过走代码，判断出supplierId为shopid所以增加如上代码  ，
								//也判断出 matCd  == itemId为商品id，当matCd  ！= itemId  matCd为skuid
//								ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+map.get("supplierId")));
//								matDTO.setAlternate1(shop.getResult().getShopName());
							}
						}
						if(map.get("matPrice") != null && !"".equals(map.get("matPrice")) && !"null".equals(map.get("matPrice"))){
							matDTO.setAlternate5("1");
							matDTO.setStatus("" + map.get("status"));
							matDTO.setMatPrice(Double.parseDouble("" + map.get("matPrice")));
						}
						if(!"1".equals(price.getStatus()) && map.get("beginDate") != null && !"null".equals(map.get("beginDate"))) {
							try {
								Date beginDate = sm.parse(""+map.get("beginDate"));
								Date endDate = sm.parse(""+map.get("endDate"));
								matDTO.setBeginDate(beginDate);
								matDTO.setEndDate(endDate);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						itemList.add(matDTO);
					}
				}
				price.setInquiryMatDTOs(itemList);
				priceArr.add(price);
			}
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords(priceArr);
		}
		model.addAttribute("pager", pager);
		if("request".equals(flag)){
			return "/requestPrice/requestPriceModelPage";
		}else{
			return "/responsePrice/responsePriceModelPage";
		}
	}

	/**
	 * 查询商品属性
	 * @author chengwt 创建时间：上午9:29:58
	 */
	public Map<String, String> putSalerAttr(Map contractMats) {
		ItemShopCartDTO dto = new ItemShopCartDTO();
		dto.setAreaCode("0");
		
		if(contractMats.get("shopId") != null && !"".equals(contractMats.get("shopId"))){
			dto.setShopId(Long.valueOf(contractMats.get("shopId").toString()));
		}
		
		if(contractMats.get("skuId") != null && !"".equals(contractMats.get("skuId")) && !contractMats.get("skuId").toString().equals("null")){
			dto.setSkuId(Long.valueOf(contractMats.get("skuId").toString()));
		}
		
		if(contractMats.get("itemId") != null && !"".equals(contractMats.get("itemId")) && !contractMats.get("itemId").toString().equals("null")){
			dto.setItemId(Long.valueOf(contractMats.get("itemId").toString()));
		}
		
		ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
		ItemShopCartDTO itemShopCartDTO = er.getResult();
		if (null != itemShopCartDTO) {
			contractMats.put("skuPicUrl", itemShopCartDTO.getSkuPicUrl());
			//商品属性
			String skuName = "";
			for (ItemAttr itemAttr : itemShopCartDTO.getAttrSales()) {
				skuName += itemAttr.getName();
				for (ItemAttrValue itemAttrValue : itemAttr.getValues()) {
					skuName += ":" + itemAttrValue.getName() + ";";
				}
			}
			contractMats.put("salerAttr", skuName);
		}
		return contractMats;
	}
	
	@RequestMapping(value = "/getSkuByItemIds")
	@ResponseBody
	public String getSkuByItemIds(HttpServletRequest request,String itemIds, String shopIds ,String shopNames, String itemName,String tempDate) {
		String itemIds_arry[] = itemIds.split(",");
		String shopIds_arry[] = shopIds.split(",");
		String shopNames_arry[] = shopNames.split(",");
		String result = "";
		int index = 0;
		String skuId ="";
		for(String itemId : itemIds_arry){
			ExecuteResult<ItemDTO> er= itemService.getItemById(Long.valueOf(itemId));
			if(er != null){
				List<SkuInfo> list = er.getResult().getSkuInfos();
				if(list != null){
					for(int i = 0;i< list.size();i++){
						skuId = list.get(i).getSkuId().toString();
						//获取销售属性
						Map<String, String> mapAttr = new HashMap<String, String>();
						mapAttr.put("shopId", shopIds_arry[index]);
						mapAttr.put("skuId", skuId);
						mapAttr.put("itemId", itemId);
						mapAttr = putSalerAttr(mapAttr);
						result +="<ul class='mar_lr5'><input type='hidden' name='itemIds' value='" + itemIds_arry[index] + "'/>" +
								"<input type='hidden' name='skuIds' value='" + skuId + "'/>" +
				                "<input type='hidden' name='statusDetail' value='i'/> <input type='hidden'  name='detailId' value= '' />" +
				                "<input type='hidden' name='shopId' value='" + shopIds_arry[index] + "'/>" +
				                "<div class='hei_32 border_2'>" +
				                "<p class='fl'><span style='font-weight:bold;'>物品名：</span><span>" + er.getResult().getItemName() + "</span></p>" +
				                "</div>" +
				                "<div class='hei_32 border_2'>" +
				                "<p class='fl'><span style='font-weight:bold;'>商品属性：</span><span>" + mapAttr.get("salerAttr") + "</span></p>" +
				                "</div>" +
				                "<div class='hei_32 border_2'>" +
				                "<p class='fl'><span style='font-weight:bold;'>供应商：</span><span>" + shopNames_arry[index] + "</span></p>" +
				                "</div>" +
				                "<div class='hei_32 border_2'>" +
				                "<p class='fl wid_75'><span style='font-weight:bold;'>交货时间：</span><input type='date' name='deliveryDates' class='form-control wid_50 hei_20' value='" + tempDate + "'/></p>" +
				                "</div>" +
				                "<div class='border_2 shop_order pad_tb5' >" +
				                "<p class='wid_95 fl shop_order_p'><span style='font-weight:bold;'>数量：</span><input type='text'  maxlength = '11' name='nums' onkeypress='number()' onkeyup='filterInput()' onchange='filterInput()' onbeforepaste='filterPaste()' onpaste='return false' class='form-control wid_50 hei_20'  />"
				                + "<button class='fr button_3 pad_mlr5 delete'>删除</button></p>" +
				                "<div class='clear'></div>" +
				                "</div></ul>";
					}
				}
			}
			index ++;
		}
//		if(!"".equals(result)){
//			result = "&_&" + result + "^_^";
//		}
		return result;
	}

}
