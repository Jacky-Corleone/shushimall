	package com.camelot.mall.controller;
	
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
	
	import com.alibaba.fastjson.JSONArray;
	import com.alibaba.fastjson.JSONObject;
	import com.camelot.goodscenter.dto.InquiryInfoDTO;
	import com.camelot.goodscenter.dto.TranslationInfoDTO;
	import com.camelot.goodscenter.dto.TranslationMatDTO;
	import com.camelot.goodscenter.service.ItemExportService;
	import com.camelot.goodscenter.service.TranslationExportService;
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
	 * 
	 * <p>Description: [描述该类概要功能介绍：求购管理]</p>
	 * Created on 2016年1月28日
	 * @author  <a href="mailto: zihanmin@camelotchina.com">訾瀚民</a>
	 * @version 1.0 
	 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
	 */
	
	
	@Controller
	@RequestMapping("/askItemInfoController")
	public class AskItemInfoController {
	
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
		private UserExportService userExportService;
		@Resource
		private CommonService commonService;
		@Resource
		private ItemExportService itemService;
		@Resource
		private TranslationExportService translationExportService;
		@Resource
		private ShopExportService shopExportService;
	
		/**
		 * 
		 * <p>Discription:[方法功能中文描述：跳转到求购主页面，同时将页面展示的数据准备好]</p>
		 * Created on 2015-5-28 下午3:21:45
		 * @param pager
		 * @param model
		 * @param request
		 * @param response
		 * @return
		 * @author:[ 成文涛 ]
		 */
		@RequestMapping("askItemInfo")
		public String goAskItemInfo(Pager pager, Model model, HttpServletRequest request,
				HttpServletResponse response){
			String ctoken = LoginToken.getLoginToken(request);
	    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
	    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
	    	if(register==null){
	    		return "redirect:/";
	    	}
	    	// 	Integer status = register.getuStatus();
	//    	if(status !=4 && status != 6 ){
	//    		return "redirect:/";
	//    	}
			//查询用户信息
			Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO = userExportService.queryUserById(userId);
			model.addAttribute("uid", userDTO.getUid());
			// 	List<InquiryInfoDTO> dtos = new ArrayList<InquiryInfoDTO>();
			TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
			translationInfoDTO.setActiveFlag("1");
			translationInfoDTO.setCreateBy("" + userId);
			pager.setRows(Integer.MAX_VALUE);
			ExecuteResult<DataGrid<TranslationInfoDTO>> er= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
			String oldNo ="";
			//查询求购主单信息，查询出的数据时去重之后的
			er = translationExportService.queryTranslationInfoList(translationInfoDTO, pager);
			if( er.isSuccess() && er.getResult().getTotal() > 0 && er.getResult() != null ){
				JSONArray priceArr = new JSONArray();
				
				for( TranslationInfoDTO translationDTO: er.getResult().getRows() ){
					if(!oldNo.contains((translationDTO.getTranslationNo()))){
						translationInfoDTO.setTranslationNo(translationDTO.getTranslationNo());
						TranslationMatDTO dto = new TranslationMatDTO();
						dto.setActiveFlag("1");
						dto.setTranslationNo(translationDTO.getTranslationNo());
						dto.setSupplierId(translationDTO.getSupplierId());
	//					dto.setMatDesc(translationDTO.getItemName());
						dto.setAlternate3(translationDTO.getTranslationName());
						//查询求购明细
						ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(translationInfoDTO, pager);
						ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(dto, pager);	
						List<Map> mapList = new ArrayList<Map>();
						List<TranslationMatDTO> itemList = new ArrayList<TranslationMatDTO>();
						//由于刚审核完的求购单还未生成明细，所以需要先根据求购明细表的数据来判断，明细表数据大于0，则展示明细表的数据，否则展示主表数据
						if(detailDTOList.getResult().getTotal() > 0){
							mapList = detailDTOList.getResult().getRows();
						}else{
							mapList = translationDTOList.getResult().getRows();
						}
						//取出map放入list中，前台展示用
						if(mapList.size() > 0){
							for(Map dtoItem : mapList){
								TranslationMatDTO matDTO = new TranslationMatDTO();
								matDTO.setId(Long.parseLong(""+dtoItem.get("id")));
								
								if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))){
									if( dtoItem.get("status") != null && !"".equals( dtoItem.get("status")) && !"null".equals( dtoItem.get("status"))) {
										matDTO.setStatus(Integer.parseInt("" + dtoItem.get("status")));
									}
									//根据店铺的id，获取店铺名字在前台展示
									ExecuteResult<ShopDTO> shopRe = shopExportService.findShopInfoById(Long.parseLong(""+dtoItem.get("shopId")));
									matDTO.setAlternate1(shopRe.getResult().getShopName());
									matDTO.setMatDesc(""+dtoItem.get("matDesc"));
									if(dtoItem.get("matPrice") != null && !"".equals(dtoItem.get("matPrice")) && !"null".equals(dtoItem.get("matPrice"))) {
										matDTO.setMatPrice(Double.parseDouble("" + dtoItem.get("matPrice")));
									}
								}else{
									matDTO.setAlternate1("");
									matDTO.setMatDesc(""+dtoItem.get("itemName"));
								}
								if(dtoItem.get("matAttribute") != null && !"null".equals(dtoItem.get("matAttribute"))) {
									matDTO.setMatAttribute("" + dtoItem.get("matAttribute"));
								}
								if(dtoItem.get("cName") != null && !"null".equals(dtoItem.get("cName"))) {
									matDTO.setAlternate2("" + dtoItem.get("cName"));
								}
								if(dtoItem.get("quantity") != null && !"null".equals(dtoItem.get("quantity"))) {
									matDTO.setQuantity(Integer.parseInt("" + dtoItem.get("quantity")));
								}
								itemList.add(matDTO);
							}
							translationDTO.setTranslationMatDTOs(itemList);
							priceArr.add(translationDTO);
						}
						oldNo += translationDTO.getTranslationNo() +";";
					}
				}
				pager.setTotalCount(er.getResult().getTotal().intValue());
				pager.setRecords( priceArr );
			}
			//查询产品类目的信息存放在页面，供求购单新建时使用
			JSONArray ja = commonServiceImpl.findCategory();
			model.addAttribute("itemList", ja);
			UserDTO userDto = userExportService.queryUserById(userId);
			if("".equals(userDto.getUname())){
				model.addAttribute("uName", "登录名为空");
			}else{
				model.addAttribute("uName", userDto.getUname());
			}
			//全部报价
			model.addAttribute("pager", pager);			
			return "/askItemInfo/askItemInfoPage";
		}
		
		
		/**
		 * 
		 * <p>Discription:[方法功能中文描述：买家求购页面转换，全部求购、求购创建、待提交、待接收页面转换]</p>
		 * Created on 2016年1月28日
		 * @param flag
		 * @param request
		 * @param num 区分那个页面的标识 0为全部求购页面， 1 求购创建页面，2待提交页面 3待接收页面
		 * @param model
		 * @return
		 * @author:[chengwt]
		 */
		
		@RequestMapping(value="/queryTranslation")
		public String queryTranslation(@RequestParam(value="flag",required=false,defaultValue="")String flag,HttpServletRequest request,
					@RequestParam(value="num",required=false,defaultValue="")String num,Model model){
			// String ctoken = LoginToken.getLoginToken(request);
			//查询用户信息
			Long userId = WebUtil.getInstance().getUserId(request);
			// 	UserDTO userDTO = userExportService.queryUserById(userId);
			// List<TranslationInfoDTO> dtos = new ArrayList<TranslationInfoDTO>();
			TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
			translationInfoDTO.setActiveFlag("1");
			translationInfoDTO.setCreateBy(""+userId);
			//全部求购
			if(!"1".equals(num)){
				if("2".equals(num)){
					//未提交求购
					List<String> list = new ArrayList<String>();
					list.add("0");
					list.add("4");
					translationInfoDTO.setStatusList(list);
				}else if("3".equals(num)){
					//买家未确认求购
					translationInfoDTO.setStatus("2");
				}
				Pager pager = new Pager();
				pager.setPage(1);
				pager.setRows(Integer.MAX_VALUE);
				ExecuteResult<DataGrid<TranslationInfoDTO>> er= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
				//查询求购单主单信息
				String oldNo ="";
				er = translationExportService.queryTranslationInfoList(translationInfoDTO, pager);
				if( er.isSuccess() && er.getResult().getTotal() > 0 && er.getResult() != null ){
					JSONArray priceArr = new JSONArray();
					for( TranslationInfoDTO translationDTO: er.getResult().getRows() ){
						if(!oldNo.contains(translationDTO.getTranslationNo())){
							translationInfoDTO.setTranslationNo(translationDTO.getTranslationNo());
							TranslationMatDTO dto = new TranslationMatDTO();
							dto.setActiveFlag("1");
							dto.setTranslationNo(translationDTO.getTranslationNo());
							//查询求购明细
							ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(translationInfoDTO, pager);
							ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(dto, pager);	
							List<Map> mapList = new ArrayList<Map>();
							List<TranslationMatDTO> itemList = new ArrayList<TranslationMatDTO>();
							//由于刚审核完的求购单还未生成明细，所以需要先根据求购明细表的数据来判断，明细表数据大于0，则展示明细表的数据，否则展示主表数据
							if(detailDTOList.getResult().getTotal() > 0){
								mapList = detailDTOList.getResult().getRows();
							}else{
								mapList = translationDTOList.getResult().getRows();
							}
							//取出map放入list中，前台展示用
							if(mapList.size() > 0){
								for(Map dtoItem : mapList){
									TranslationMatDTO matDTO = new TranslationMatDTO();
									matDTO.setId(Long.parseLong(""+ dtoItem.get("id")));
									matDTO.setMatDesc(""+dtoItem.get("itemName"));
									if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))){
										if( dtoItem.get("status") != null && !"".equals( dtoItem.get("status")) && !"null".equals( dtoItem.get("status"))) {
											matDTO.setStatus(Integer.parseInt("" + dtoItem.get("status")));
										}
										ExecuteResult<ShopDTO> shopRe = shopExportService.findShopInfoById(Long.parseLong(""+dtoItem.get("shopId")));
										matDTO.setAlternate1(shopRe.getResult().getShopName());
										matDTO.setMatDesc(""+dtoItem.get("matDesc"));
										if(dtoItem.get("matPrice") != null && !"".equals(dtoItem.get("matPrice")) && !"null".equals(dtoItem.get("matPrice"))) {
											matDTO.setMatPrice(Double.parseDouble("" + dtoItem.get("matPrice")));
										}
									}else{
										matDTO.setAlternate1("");
										matDTO.setMatDesc(""+dtoItem.get("matCd"));
									}
									if(dtoItem.get("matAttribute") != null && !"null".equals(dtoItem.get("matAttribute"))) {
										matDTO.setMatAttribute("" + dtoItem.get("matAttribute"));
									}
									matDTO.setAlternate2("" + dtoItem.get("cName"));
									matDTO.setQuantity(Integer.parseInt("" + dtoItem.get("quantity")));
									itemList.add(matDTO);
								}
								translationDTO.setTranslationMatDTOs(itemList);
								priceArr.add(translationDTO);
							}
							oldNo += translationDTO.getTranslationNo() +";";
						}
					}
					pager.setTotalCount(er.getResult().getTotal().intValue());
					pager.setRecords( priceArr );
					UserDTO userDto = userExportService.queryUserById(userId);
					if("".equals(userDto.getUname())){
						model.addAttribute("uName", "登录名为空");
					}else{
						model.addAttribute("uName", userDto.getUname());
					}
				}
				model.addAttribute("pager", pager);
			}else{
				//求购创建页面
				//调用dubbo生成求购编码
				ExecuteResult<String> translationNo =  translationExportService.createTranslationNo();
				if(translationNo.isSuccess()){
					model.addAttribute("translationNo", translationNo.getResult());
				}
				SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
				String beginDate = sm.format(new Date());
				model.addAttribute("beginDate", beginDate);
				model.addAttribute("uid", userId);
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
			
			return "/askItemInfo/askItemInfoAllPage";
		}
		 /**
		  * 
		  * <p>Discription:[方法功能中文描述:跳往求购更新页面]</p>
		  * Created on 2016年1月28日
		  * @param request
		  * @param model
		  * @return
		  * @author:[chengwt]
		  */
		@RequestMapping("/updateAskItemInfo")
		public String updateAskItemInfo(HttpServletRequest request, Model model ){
			//查询用户信息
			Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO = userExportService.queryUserById(userId);
			// 	ExecuteResult<String> result = new ExecuteResult<String>();
		   	String translationNo = request.getParameter("translationNo");
		 // 	List<String> errs = new ArrayList<String>();
		 // 	String ctoken = LoginToken.getLoginToken(request);
		   	TranslationInfoDTO dto = new TranslationInfoDTO();
		   	dto.setActiveFlag("1");
		   	dto.setTranslationNo(translationNo);
		   	ExecuteResult<DataGrid<TranslationInfoDTO>> translationDTOS= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
		   	Pager pager = new Pager();
		   	pager.setPage(1);
		   	pager.setRows(Integer.MAX_VALUE);
			//查询求购主单信息
		   	translationDTOS = translationExportService.queryTranslationInfoList(dto, pager);
			//查询求购主单(关联类别的主单信息)
		   	ExecuteResult<DataGrid<Map>> translatioDTOList =  translationExportService.queryTranslationInfoPager(dto, pager);
			if(translationDTOS.isSuccess() && translationDTOS.getResult().getTotal() > 0){
				TranslationInfoDTO translationDTO = translationDTOS.getResult().getRows().get(0);
				model.addAttribute("translationNo", translationDTO.getTranslationNo());
				model.addAttribute("translationName", translationDTO.getTranslationName());
				if(translationDTO.getAnnex() == null ||  "".equals(translationDTO.getAnnex())){
					model.addAttribute("annex", "null");
				}else{
					String fileName = null;
					if (null != translationDTO.getAnnex()) {
						fileName = translationDTO.getAnnex().substring(translationDTO.getAnnex().lastIndexOf("/") + 1);
					}
					model.addAttribute("annex", translationDTO.getAnnex());
					model.addAttribute("fileName", fileName);
				}
				model.addAttribute("beginDate", translationDTO.getBeginDate());
				model.addAttribute("endDate", translationDTO.getEndDate());
				model.addAttribute("deliveryDate", translationDTO.getDeliveryDate());
				model.addAttribute("remarks", translationDTO.getRemarks());
				//图片服务器地址
				model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
				List<Map> translationMatDTOs = new ArrayList<Map>();
				Map<String, String> map = new HashMap<String, String>();
				//取出map放入list中，前台展示用
				for(Map mapp : translatioDTOList.getResult().getRows()){
					map = new HashMap<String, String>();
					map.put("translationId", ""+mapp.get("id"));
					map.put("matCd", ""+mapp.get("matCd"));
					map.put("quantity", ""+mapp.get("quantity"));
					map.put("matAttribute", ""+mapp.get("matAttribute"));
					map.put("category_ids", ""+mapp.get("categoryId"));
					map.put("category_names", ""+mapp.get("cName"));
					map.put("flag", "u");
					translationMatDTOs.add(map);
				}
				model.addAttribute("details", translationMatDTOs);
				model.addAttribute("update_flag", "1");
		        model.addAttribute("uid", userId);
		        model.addAttribute("uName", userDTO.getUname());
		        model.addAttribute("updateBy", userId);
			}else{
				//获取信息失败，则直接返回原页面
				return "/askItemInfo/askItemInfoPage";
			}
			JSONArray ja = commonServiceImpl.findCategory();
			model.addAttribute("itemList", ja);
			return "/askItemInfo/askItemInfoUpdate";
		}
	
	
		/**
		 * 
		 * <p>Discription:[方法功能中文描述:跳往求购查看页面]</p>
		 * Created on 2016年1月28日
		 * @param request
		 * @param model
		 * @return
		 * @author:[chengwt]
		 */
		
		@RequestMapping("/lookTranslation")
		public String lookTranslation(HttpServletRequest request, Model model ){
			//查询用户信息
			Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO = userExportService.queryUserById(userId);
			//   	ExecuteResult<String> result = new ExecuteResult<String>();
		   	String translationNo = request.getParameter("translationNo");
			//flag是区分求购页面还是报价页面的标识，response是报价页面跳转的标识
		   	String flag = request.getParameter("flag");
		   	String resultStr = "/askItemInfo/askItemInfoShow";
		   	if("response".equals(flag)){
		   		resultStr = "/askItemInfo/repAskItemInfoShow";
		   	}
		 // 	List<String> errs = new ArrayList<String>();
		   	TranslationInfoDTO dto = new TranslationInfoDTO();
		   	dto.setActiveFlag("1");
		   	dto.setTranslationNo(translationNo);
		   	ExecuteResult<DataGrid<TranslationInfoDTO>> translationInfoDTOS= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
		   	Pager pager = new Pager();
		   	pager.setPage(1);
		   	pager.setRows(Integer.MAX_VALUE);
			//查询求购主单信息
		   	translationInfoDTOS = translationExportService.queryTranslationInfoList(dto, pager);
			if(translationInfoDTOS.isSuccess() && translationInfoDTOS.getResult().getTotal() > 0){
				TranslationMatDTO translationMatDTO = new TranslationMatDTO();
				translationMatDTO.setActiveFlag("1");
				translationMatDTO.setTranslationNo(translationNo);
				if("response".equals(flag)){
					translationMatDTO.setShopId(userDTO.getShopId().intValue());
				}
				TranslationInfoDTO translationInfDTO = translationInfoDTOS.getResult().getRows().get(0);
				model.addAttribute("translationId", translationInfDTO.getId());
				model.addAttribute("translationNo", translationInfDTO.getTranslationNo());
				model.addAttribute("translationName", translationInfDTO.getTranslationName());
				model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
				UserDTO buyer = userExportService.queryUserById(Long.parseLong(translationInfDTO.getCreateBy()));
				if(buyer != null && !"".equals(buyer.getUname())) {
					//ExecuteResult<ShopDTO> shopRe = shopExportService.findShopInfoById(userDTO.getShopId());
					model.addAttribute("printerName", buyer.getUname());
				}else{
					model.addAttribute("printerName", "登录名为空");
				}
				model.addAttribute("supplierId", translationInfDTO.getSupplierId());
				if(translationInfDTO.getAnnex() == null ||  "".equals(translationInfDTO.getAnnex())){
					model.addAttribute("annex", "null");
				}else{
					String fileName = null;
					if (null != translationInfDTO.getAnnex()) {
						fileName = translationInfDTO.getAnnex().substring(translationInfDTO.getAnnex().lastIndexOf("/") + 1);
					}
					model.addAttribute("annex", translationInfDTO.getAnnex());
					model.addAttribute("fileName", fileName);
				}
				model.addAttribute("beginDate", translationInfDTO.getBeginDate());
				model.addAttribute("endDate", translationInfDTO.getEndDate());
				model.addAttribute("deliveryDate", translationInfDTO.getDeliveryDate());
				model.addAttribute("remarks", translationInfDTO.getRemarks());
				model.addAttribute("status", translationInfDTO.getStatus());
				model.addAttribute("refusalReason",translationInfDTO.getRefuseReason());
				List<Map> matDTOs = new ArrayList<Map>();
				Map<String, String> map = new HashMap<String, String>();
				//查询明细中的supplyID为登陆人所属的supplyId相同的才展示
				TranslationMatDTO detailDTO = new TranslationMatDTO();
				detailDTO.setActiveFlag("1");
				if("response".equals(flag)) {
					detailDTO.setShopId(userDTO.getShopId().intValue());
				}
				detailDTO.setTranslationNo(translationNo);
				//取出map放入list中，前台展示用
				int i = 1;
				//如果状态为2，则查询主表的物品名称、数量当作明细，标志为I表明需要插入，否则查询明细表，标志为u，需要更新
				pager.setRows(Integer.MAX_VALUE);
				ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(dto, pager);
				ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(detailDTO, pager);
				List<Map> mapList = new ArrayList<Map>();
				//取出map放入list中，前台展示用
				//明细中有此供应商的则只修改此供应商的这条记录，否则，查询主单的物品名、数量信息
				if(detailDTOList.getResult().getTotal() > 0){
					mapList = detailDTOList.getResult().getRows();
				}else{
					mapList = translationDTOList.getResult().getRows();
				}
				//标示字符串，i标示保存时插入，u标示修改
				String addFlag ="i";
				if(detailDTOList.getResult().getTotal() < 1){
					detailDTOList = translationExportService.queryTranslationInfoPager(translationInfDTO, pager);
	
					for(Map mapp : mapList){
						map = new HashMap<String, String>();
						map.put("no", ""+i);
						map.put("matCd", ""+mapp.get("matCd"));
						map.put("quantity", ""+mapp.get("quantity"));
						if(mapp.get("matAttribute") != null && !"null".equals(mapp.get("matAttribute"))) {
							map.put("matAttribute", "" + mapp.get("matAttribute"));
						}
						map.put("category_ids", ""+mapp.get("categoryId"));
						map.put("category_names", ""+mapp.get("cName"));
						map.put("flag_status", "i");
						matDTOs.add(map);
						i += 1;
					}
	
				}else{
					//查询明细信息
					detailDTOList = translationExportService.queryTranslationMatList(translationMatDTO, pager);
	
					for(Map mapp : mapList){
						map = new HashMap<String, String>();
						map.put("no", ""+i);
						map.put("id", ""+mapp.get("id"));
						map.put("matCd", ""+mapp.get("matDesc"));
						map.put("quantity", ""+mapp.get("quantity"));
						map.put("flag_status", "u");
						if(mapp.get("beginDate") == null || "null".equals(""+mapp.get("beginDate"))){
							map.put("beginDate", "");
						}else{
							map.put("beginDate", ""+mapp.get("beginDate"));
						}
						if(mapp.get("endDate") == null || "null".equals(""+mapp.get("endDate"))){
							map.put("endDate", "");
						}else{
							map.put("endDate", ""+mapp.get("endDate"));
						}
						if( mapp.get("matPrice") == null || "null".equals(""+mapp.get("matPrice")) ){
							map.put("matPrice", "");
						}else{
							map.put("matPrice", ""+mapp.get("matPrice"));
						}
						if(mapp.get("matAttribute") != null && !"null".equals(mapp.get("matAttribute"))) {
							map.put("matAttribute", "" + mapp.get("matAttribute"));
						}
						map.put("category_ids", ""+mapp.get("categoryId"));
						map.put("category_names", ""+mapp.get("cName"));
						matDTOs.add(map);
						i += 1;
					}
				}
				model.addAttribute("details", matDTOs);
			}else{
				if("responsePrice".equals(flag)){
				//获取信息失败，则直接返回原页面
					return "/askItemInfo/repAskItemInfoPage";
				}else{
					return "/askItemInfo/askItemInfoPage";
				}
			}
			return resultStr;
		}
		
	
	
		
		/**
		 * 
		 * <p>Discription:[方法功能中文描述：卖家求购主页面]</p>
		 * Created on 2016年1月28日
		 * @param model
		 * @param request
		 * @param response
		 * @return
		 * @author:[chengwt]
		 */
		@RequestMapping("repAskItemInfo")
		public String goresponsePrice(Model model, HttpServletRequest request,
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
			if("".equals(userDTO.getCompanyName())){
				model.addAttribute("uName", "公司名字为空");
			}else{
				model.addAttribute("uName", userDTO.getCompanyName());
			}
			
			Pager pager = new Pager();
			pager.setRows(Integer.MAX_VALUE);
			pager.setPage(1);
			//全部
			TranslationInfoDTO translationInfoDTO0 = new TranslationInfoDTO();
			translationInfoDTO0.setActiveFlag("1");
			List<String> statusList = new ArrayList<String>();
			statusList.add("2");
			statusList.add("3");
			translationInfoDTO0.setStatusList(statusList);
			ExecuteResult<DataGrid<TranslationInfoDTO>> er0 = translationExportService.queryTranslationInfoList(translationInfoDTO0, pager);
			if(er0.getResult()!=null){
				model.addAttribute("totalCount0", er0.getResult().getTotal());
			}
			//已报价
			TranslationInfoDTO translationInfoDTO1 = new TranslationInfoDTO();
			translationInfoDTO1.setActiveFlag("1");
			translationInfoDTO1.setStatus("2");
			ExecuteResult<DataGrid<TranslationInfoDTO>> er1 = translationExportService.queryTranslationInfoList(translationInfoDTO1, pager);
			if(er1.getResult()!=null){
				model.addAttribute("totalCount1", er1.getResult().getTotal());
			}
			//已确认
			TranslationInfoDTO translationInfoDTO2 = new TranslationInfoDTO();
			translationInfoDTO2.setActiveFlag("1");
			translationInfoDTO2.setStatus("3");
			ExecuteResult<DataGrid<TranslationInfoDTO>> er2 = translationExportService.queryTranslationInfoList(translationInfoDTO2, pager);
			if(er2.getResult()!=null){
				model.addAttribute("totalCount2", er2.getResult().getTotal());
			}
			
			return "/askItemInfo/repAskItemInfoPage";
		}
		
		
		/**
		 * 
		 * <p>Discription:[方法功能中文描述：根据tab页刷新条件查询总数]</p>
		 * Created on 2016年1月28日
		 * @param request
		 * @param model
		 * @param itemName 物品名称
		 * @param translationName 求购名称
		 * @return
		 * @author:[訾瀚民]
		 */
		
		
		
	@RequestMapping("queryRepAskItemInfoCount")
	@ResponseBody
	public String queryRepAskItemInfoCount(HttpServletRequest request,Model model,String itemName ,String translationName){
		JSONObject jores=new JSONObject();
		Pager pager = new Pager();
		pager.setRows(Integer.MAX_VALUE);
		pager.setPage(1);
		//全部
		TranslationInfoDTO translationInfoDTO0 = new TranslationInfoDTO();
		translationInfoDTO0.setActiveFlag("1");
		translationInfoDTO0.setItemName(itemName);
		translationInfoDTO0.setTranslationName(translationName);
		List<String> statusList = new ArrayList<String>();
		statusList.add("2");
		statusList.add("3");
		translationInfoDTO0.setStatusList(statusList);
		ExecuteResult<DataGrid<TranslationInfoDTO>> er0 = translationExportService.queryTranslationInfoList(translationInfoDTO0, pager);
		if(er0.getResult()!=null){
			jores.put("totalCount0", er0.getResult().getTotal());
		}
		//已报价
		TranslationInfoDTO translationInfoDTO1 = new TranslationInfoDTO();
		translationInfoDTO1.setActiveFlag("1");
		translationInfoDTO1.setStatus("2");
		translationInfoDTO1.setItemName(itemName);
		translationInfoDTO1.setTranslationName(translationName);
		ExecuteResult<DataGrid<TranslationInfoDTO>> er1 = translationExportService.queryTranslationInfoList(translationInfoDTO1, pager);
		if(er1.getResult()!=null){
			jores.put("totalCount1", er1.getResult().getTotal());
		}
		//已确认
		TranslationInfoDTO translationInfoDTO2 = new TranslationInfoDTO();
		translationInfoDTO2.setActiveFlag("1");
		translationInfoDTO2.setStatus("3");
		translationInfoDTO2.setItemName(itemName);
		translationInfoDTO2.setTranslationName(translationName);
		ExecuteResult<DataGrid<TranslationInfoDTO>> er2 = translationExportService.queryTranslationInfoList(translationInfoDTO2, pager);
		if(er2.getResult()!=null){
			jores.put("totalCount2", er2.getResult().getTotal());
		}
		return jores.toJSONString();
	}
		
	
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述：卖家求购页面转换，全部报价、报价中、已确认页面转换]</p>
	 * Created on 2016年1月28日
	 * @param request
	 * @param flag
	 * @param num 区分那个页面的标识 0为全部报价页面， 1 报价中页面，2已确认页面
	 * @param model
	 * @return
	 * @author:[chengwt]
	 */
	
	
	
		@RequestMapping(value="/queryRepAskItemInfo")
		public String queryResponsePrice(HttpServletRequest request, 
					@RequestParam(value="flag",required=false,defaultValue="")String flag,
					@RequestParam(value="num",required=false,defaultValue="")String num,Model model){
			String	itemName = request.getParameter("itemName");
			String	translationName = request.getParameter("translationName");
			String	pageNoStr = request.getParameter("pageNo");
			TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
			translationInfoDTO.setItemName(itemName);
			translationInfoDTO.setTranslationName(translationName);
			translationInfoDTO.setActiveFlag("1");
			//待求购
			if("1".equals(num)){
				//已报价
				translationInfoDTO.setStatus("2");
			}else if("2".equals(num)){
				//已确认
				translationInfoDTO.setStatus("3");
			}else{
				List<String> statusList = new ArrayList<String>();
				statusList.add("2");
				statusList.add("3");
				translationInfoDTO.setStatusList(statusList);
			}
			Pager pager = new Pager();
			int pageNo = 1;
			if(pageNoStr != null && !"".equals(pageNoStr) ){
				pageNo = Integer.parseInt(pageNoStr);
			}
			pager.setPage(pageNo);
			pager.setRows(3);
			ExecuteResult<DataGrid<TranslationInfoDTO>> er= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
			String oldStr = "";
			//查询求购主单信息
			er = translationExportService.queryTranslationInfoList(translationInfoDTO, pager);
			if( er.isSuccess() && er.getResult().getTotal() > 0 && er.getResult() != null ){
				JSONArray priceArr = new JSONArray();
				//查询明细用的分页
				Pager pagerMat = new Pager();
				pagerMat.setPage(1);
				pagerMat.setRows(Integer.MAX_VALUE);
				for( TranslationInfoDTO translationDTO: er.getResult().getRows() ){
					if(!oldStr.contains(translationDTO.getTranslationNo())){
						TranslationMatDTO dto = new TranslationMatDTO();
						dto.setActiveFlag("1");
						dto.setTranslationNo(translationDTO.getTranslationNo());
						//查询求购明细
						//由于刚审核完的求购单还未生成明细，所以需要先根据求购明细表的数据来判断，明细表数据大于0，则展示明细表的数据，否则展示主表数据
						ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(translationDTO, pagerMat);
						ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(dto, pagerMat);	
						List<Map> mapList = new ArrayList<Map>();
						List<TranslationMatDTO> itemList = new ArrayList<TranslationMatDTO>();
	
						if(detailDTOList.getResult().getTotal() > 0){
							mapList = detailDTOList.getResult().getRows();
						}else{
							mapList = translationDTOList.getResult().getRows();
						}
						//取出map放入list中，前台展示用
						if(mapList.size() > 0){
							for(Map dtoItem : mapList){
								TranslationMatDTO matDTO = new TranslationMatDTO();
								matDTO.setId(Long.parseLong("" + dtoItem.get("id")));
								matDTO.setMatDesc("" + dtoItem.get("itemName"));
								if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))){
									//根据店铺id获取店铺name
									ExecuteResult<ShopDTO> shopRe = shopExportService.findShopInfoById(Long.parseLong(""+dtoItem.get("shopId")));
									matDTO.setAlternate1(shopRe.getResult().getShopName());
									if( dtoItem.get("status") != null && !"".equals( dtoItem.get("status")) && !"null".equals( dtoItem.get("status"))) {
										matDTO.setStatus(Integer.parseInt("" + dtoItem.get("status")));
									}
									matDTO.setMatDesc(""+dtoItem.get("matDesc"));
									if(dtoItem.get("matPrice") != null && !"".equals(dtoItem.get("matPrice")) && !"null".equals(dtoItem.get("matPrice"))) {
										matDTO.setMatPrice(Double.parseDouble("" + dtoItem.get("matPrice")));
									}
								}else{
									matDTO.setAlternate1("");
									matDTO.setMatDesc(""+dtoItem.get("matCd"));
								}
								if(dtoItem.get("matAttribute") != null && !"null".equals(dtoItem.get("matAttribute"))) {
									matDTO.setMatAttribute("" + dtoItem.get("matAttribute"));
								}
								matDTO.setAlternate2("" + dtoItem.get("cName"));
								matDTO.setQuantity(Integer.parseInt("" + dtoItem.get("quantity")));
								itemList.add(matDTO);
							}
							translationDTO.setTranslationMatDTOs(itemList);
							priceArr.add(translationDTO);
						}
						oldStr += translationDTO.getTranslationNo() + ";";
					}
				}
				pager.setTotalCount(er.getResult().getTotal().intValue());
				pager.setRecords( priceArr );
			}
			model.addAttribute("pager", pager);
			model.addAttribute("num", num);
			return "/askItemInfo/repAskItemInfoPageAll";
		}
		
		/**
		 * 
		 * <p>Discription:[方法功能中文描述：卖家进入编辑界面]</p>
		 * Created on 2016年1月28日
		 * @param request
		 * @param model
		 * @return
		 * @author:[chengwt]
		 */
		@RequestMapping("/updateRepAskItemInfo")
		public String updateRepAskItemInfo(HttpServletRequest request, Model model ){
			//查询用户信息
			Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO = userExportService.queryUserById(userId);
			//   	ExecuteResult<String> result = new ExecuteResult<String>();
		   	String translationNo = request.getParameter("translationNo");
		 //   	List<String> errs = new ArrayList<String>();
		   	TranslationInfoDTO dto = new TranslationInfoDTO();
		   	dto.setActiveFlag("1");
		   	dto.setTranslationNo(translationNo);
		   	ExecuteResult<DataGrid<TranslationInfoDTO>> translationDTOS= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
		   	Pager pager = new Pager();
		   	pager.setPage(1);
		   	pager.setRows(Integer.MAX_VALUE);
			//查询求购主单信息
		   	translationDTOS = translationExportService.queryTranslationInfoList(dto, pager);
		   	TranslationMatDTO translationMatDTO = new TranslationMatDTO();
			translationMatDTO.setActiveFlag("1");
			translationMatDTO.setTranslationNo(translationNo);
			translationMatDTO.setSupplierId(userId.intValue());
			if(translationDTOS.isSuccess() && translationDTOS.getResult().getTotal() > 0){
				TranslationInfoDTO translationInfDTO = translationDTOS.getResult().getRows().get(0);
				model.addAttribute("translationId", translationInfDTO.getId());
				model.addAttribute("translationNo", translationInfDTO.getTranslationNo());
				model.addAttribute("createBy", translationInfDTO.getCreateBy());
				model.addAttribute("translationName", translationInfDTO.getTranslationName());
				model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
				UserDTO buyer = userExportService.queryUserById(Long.parseLong(translationInfDTO.getCreateBy()));
				if(buyer != null  && !"".equals(buyer.getUname())) {
					//ExecuteResult<ShopDTO> shopRe = shopExportService.findShopInfoById(userDTO.getShopId());
					model.addAttribute("printerName", buyer.getUname());
				}else{
					model.addAttribute("printerName", "登录名为空");
				}
				model.addAttribute("supplierId", translationInfDTO.getSupplierId());
				if(translationInfDTO.getAnnex() == null ||  "".equals(translationInfDTO.getAnnex())){
					model.addAttribute("annex", "null");
				}else{
					String fileName = null;
					if (null != translationInfDTO.getAnnex()) {
						fileName = translationInfDTO.getAnnex().substring(translationInfDTO.getAnnex().lastIndexOf("/") + 1);
					}
					model.addAttribute("annex", translationInfDTO.getAnnex());
					model.addAttribute("fileName", fileName);
				}
				model.addAttribute("beginDate", translationInfDTO.getBeginDate());
				model.addAttribute("endDate", translationInfDTO.getEndDate());
				model.addAttribute("deliveryDate", translationInfDTO.getDeliveryDate());
				model.addAttribute("remarks", translationInfDTO.getRemarks());
				List<Map> matDTOs = new ArrayList<Map>();
				Map<String, String> map = new HashMap<String, String>();
				//查询明细中的supplyID为登陆人所属的supplyId相同的才展示
				TranslationMatDTO detailDTO = new TranslationMatDTO();
				detailDTO.setActiveFlag("1");
				detailDTO.setShopId(userDTO.getShopId().intValue());
				detailDTO.setTranslationNo(translationNo);
				//取出map放入list中，前台展示用
				int i = 1;
				//如果状态为2，则查询主表的物品名称、数量当作明细，标志为I表明需要插入，否则查询明细表，标志为u，需要更新
				pager.setRows(Integer.MAX_VALUE);
				ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(dto, pager);
				ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(detailDTO, pager);
				List<Map> mapList = new ArrayList<Map>();
				//取出map放入list中，前台展示用
				//明细中有此供应商的则只修改此供应商的这条记录，否则，查询主单的物品名、数量信息
				if(detailDTOList.getResult().getTotal() > 0){
					mapList = detailDTOList.getResult().getRows();
				}else{
					mapList = translationDTOList.getResult().getRows();
				}
				//标示字符串，i标示保存时插入，u标示修改
			   	if(detailDTOList.getResult().getTotal() < 1){
					//查询求购主单(关联产品类别的信息)当做明细信息展示
			   		detailDTOList = translationExportService.queryTranslationInfoPager(translationInfDTO, pager);
			   		for(Map mapp : mapList){
						map = new HashMap<String, String>();
						map.put("no", ""+i);
						map.put("matCd", ""+mapp.get("matCd"));
						map.put("quantity", ""+mapp.get("quantity"));
						if(mapp.get("matAttribute") != null && !"null".equals(mapp.get("matAttribute"))) {
							map.put("matAttribute", "" + mapp.get("matAttribute"));
						}
						map.put("category_ids", ""+mapp.get("categoryId"));
						map.put("category_names", ""+mapp.get("cName"));
						map.put("flag_status", "i");
						matDTOs.add(map);
						i += 1;
					}
			   	}else{
					//查询求购明细信息展示
			   		detailDTOList = translationExportService.queryTranslationMatList(translationMatDTO, pager);
					for(Map mapp : mapList){
						map = new HashMap<String, String>();
						map.put("no", ""+i);
						map.put("id", ""+mapp.get("id"));
						map.put("matCd", ""+mapp.get("matDesc"));
						map.put("quantity", ""+mapp.get("quantity"));
						map.put("flag_status", "u");
						if(mapp.get("beginDate") == null || "null".equals(""+mapp.get("beginDate"))){
							map.put("beginDate", "");
						}else{
							map.put("beginDate", ""+mapp.get("beginDate"));
						}
						if(mapp.get("endDate") == null || "null".equals(""+mapp.get("endDate"))){
							map.put("endDate", "");
						}else{
							map.put("endDate", ""+mapp.get("endDate"));
						}
						if( mapp.get("matPrice") == null || "null".equals(""+mapp.get("matPrice")) ){
							map.put("matPrice", "");
						}else{
							map.put("matPrice", ""+mapp.get("matPrice"));
						}
						if(mapp.get("matAttribute") != null && !"null".equals(mapp.get("matAttribute"))) {
							map.put("matAttribute", "" + mapp.get("matAttribute"));
						}
						map.put("category_ids", ""+mapp.get("categoryId"));
						map.put("category_names", ""+mapp.get("cName"));
						matDTOs.add(map);
						i += 1;
					}
			   	}
				model.addAttribute("details", matDTOs);
			}else{
				//报错时跳转到卖家求购首页
				return "/askItemInfo/repAskItemInfoPage";
			}
			return "/askItemInfo/repAskItemInfoUpdate";
		}
		   /**
		    * 
		    * <p>Discription:[方法功能中文描述:买家、卖家按钮求购查询方法]</p>
		    * Created on 2016年1月28日
		    * @param request
		    * @param model
		    * @return
		    * @author:[chengwt]
		    */
		
	 @RequestMapping("/queryAskItemInfo")
		public String queryAskItemInfo(HttpServletRequest request, Model model ){
		//查询用户信息
			Long userId = WebUtil.getInstance().getUserId(request);
			// UserDTO userDTO = userExportService.queryUserById(userId);
			// ExecuteResult<String> result = new ExecuteResult<String>();
			TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
			String	supplyId = request.getParameter("supplyId");
			String	supplierName = request.getParameter("supplierName");
			//不根据卖家查询
	//		if(supplyId!=null&&supplierName!=null&&!"".equals(supplyId)){
	//			translationInfoDTO.setSupplierId(Integer.parseInt(supplyId));
	//		}
			if(null!=supplierName&&supplyId==null&&!"".equals(supplierName)){
				ShopDTO  shopDTO=null;
				shopDTO=new ShopDTO();
				shopDTO.setShopName(supplierName);
				//根据供应商的名称查询该供应商是否存在
				ExecuteResult<DataGrid<ShopDTO>> searchShop = shopExportService.findShopInfoByCondition(shopDTO, null);
				if(searchShop.getResult().getRows().size()>0){
					for (ShopDTO element : searchShop.getResult().getRows()) {
						Long sellerId = element.getSellerId();
						translationInfoDTO.setSupplierId(sellerId.intValue());
					}
			}
			}
			String itemName = request.getParameter("itemName");
			String translationName = request.getParameter("translationName");
		  //flag为request的表示是买家查询，否则为卖家查询
			String flag = request.getParameter("flag");
		  //区分是那个页面的查询，根据此添加相应的求购单状态
			String num = request.getParameter("num");
			// 	List<String> errs = new ArrayList<String>();
			
			translationInfoDTO.setActiveFlag("1");
			if("request".equals(flag)){
				translationInfoDTO.setCreateBy(""+userId);
			}
			translationInfoDTO.setItemName(itemName);
			translationInfoDTO.setTranslationName(translationName);
			
			ExecuteResult<DataGrid<TranslationInfoDTO>> er= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			if("request".equals(flag)){
				if("2".equals(num)){
					//未提交求购
					List<String> list = new ArrayList<String>();
					list.add("0");
					list.add("4");
					translationInfoDTO.setStatusList(list);
				}else if("3".equals(num)){
					translationInfoDTO.setStatus("2");
				}
			}else{
				if("0".equals(num)){
					List<String> statusList = new ArrayList<String>();
					statusList.add("2");
					statusList.add("3");
					translationInfoDTO.setStatusList(statusList);
				}else if("1".equals(num)){
					translationInfoDTO.setStatus("2");
				}else if("2".equals(num)){
					translationInfoDTO.setStatus("2");
				}else if("3".equals(num)){
					translationInfoDTO.setStatus("5");
				}
			}
			String oldNo ="";
			//查询求购主单信息，查询出的数据时去重之后的
					er = translationExportService.queryTranslationInfoList(translationInfoDTO, pager);
					if( er.isSuccess() && er.getResult().getTotal() > 0 && er.getResult() != null ){
						JSONArray priceArr = new JSONArray();
						for( TranslationInfoDTO translationDTO: er.getResult().getRows() ){
							if(!oldNo.contains((translationDTO.getTranslationNo()))){
								translationInfoDTO.setTranslationNo(translationDTO.getTranslationNo());
								TranslationMatDTO dto = new TranslationMatDTO();
								dto.setActiveFlag("1");
								dto.setTranslationNo(translationDTO.getTranslationNo());
								dto.setSupplierId(translationDTO.getSupplierId());
	//							dto.setMatDesc(translationDTO.getItemName());
								dto.setAlternate3(translationDTO.getTranslationName());
	//							dto.setCreateBy(translationDTO.getCreateBy());
								//查询求购明细
								ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(translationInfoDTO, pager);
								ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(dto, pager);	
								List<Map> mapList = new ArrayList<Map>();
								List<TranslationMatDTO> itemList = new ArrayList<TranslationMatDTO>();
								//由于刚审核完的求购单还未生成明细，所以需要先根据求购明细表的数据来判断，明细表数据大于0，则展示明细表的数据，否则展示主表数据
								if(detailDTOList.getResult().getTotal() > 0){
									mapList = detailDTOList.getResult().getRows();
								}else{
									mapList = translationDTOList.getResult().getRows();
								}
								//取出map放入list中，前台展示用
								if(mapList.size() > 0){
									for(Map dtoItem : mapList){
										TranslationMatDTO matDTO = new TranslationMatDTO();
										matDTO.setId(Long.parseLong(""+dtoItem.get("id")));
										if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))){
											if( dtoItem.get("status") != null && !"".equals( dtoItem.get("status")) && !"null".equals( dtoItem.get("status"))) {
												matDTO.setStatus(Integer.parseInt("" + dtoItem.get("status")));
											}
											//根据店铺的id，获取店铺名字在前台展示
											ExecuteResult<ShopDTO> shopRe = shopExportService.findShopInfoById(Long.parseLong(""+dtoItem.get("shopId")));
											matDTO.setAlternate1(shopRe.getResult().getShopName());
											matDTO.setMatDesc(""+dtoItem.get("matDesc"));
											if(dtoItem.get("matPrice") != null && !"".equals(dtoItem.get("matPrice")) && !"null".equals(dtoItem.get("matPrice"))) {
												matDTO.setMatPrice(Double.parseDouble("" + dtoItem.get("matPrice")));
											}
										}else{
											matDTO.setAlternate1("");
											matDTO.setMatDesc(""+dtoItem.get("itemName"));
										}
										if(dtoItem.get("matAttribute") != null && !"null".equals(dtoItem.get("matAttribute"))) {
											matDTO.setMatAttribute("" + dtoItem.get("matAttribute"));
										}
										if(dtoItem.get("cName") != null && !"null".equals(dtoItem.get("cName"))) {
											matDTO.setAlternate2("" + dtoItem.get("cName"));
										}
										if(dtoItem.get("quantity") != null && !"null".equals(dtoItem.get("quantity"))) {
											matDTO.setQuantity(Integer.parseInt("" + dtoItem.get("quantity")));
										}
										
										itemList.add(matDTO);
									}
									translationDTO.setTranslationMatDTOs(itemList);
									priceArr.add(translationDTO);
								}
								oldNo += translationDTO.getTranslationNo() +";";
							}
						}
						pager.setTotalCount(er.getResult().getTotal().intValue());
						pager.setRecords( priceArr );
					}
			model.addAttribute("pager", pager);
			if("request".equals(flag)){
				return "/askItemInfo/askItemInfoModelPage";
			}else{
				return "/askItemInfo/repAskItemInfoModelPage";
			}
		}
		/**
		 * 
		 * <p>Discription:[方法功能中文描述:产品类别选择]</p>
		 * Created on 2016年1月28日
		 * @param brandId
		 * @param attributes
		 * @param cid
		 * @param areaCode
		 * @param page
		 * @param pageSize
		 * @param orderSort
		 * @param keyword
		 * @param request
		 * @param model
		 * @return
		 * @author:[chengwt]
		 */
	 
	 
		@RequestMapping("/getCategoryList")
		public String getCategoryList(Long brandId,
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
			return "/requestPrice/itemSelectPage";
	
		}
	}
