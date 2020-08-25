package com.camelot.mall.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.openplatform.util.SysProperties;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.common.CommonService;
import com.camelot.mall.common.CommonServiceImpl;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;

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
	private TranslationExportService translationExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private CommonService commonService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private ShopExportService shopExportService;
	/**
	 * 跳转到求购单买家主页
	 * @author 成文涛 创建时间：2015-5-28 下午3:21:45
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/askItemInfo")
	public String goAskItemInfo(Pager pager,Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
    		return "redirect:/user/login";
    	}
		//没有认证通过的用户，不支持此功能
		if(register.getUserType() == 1){
			return "/no_authentication";
		}
    	//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("uid", userDTO.getUid());
		List<TranslationInfoDTO> dtos = new ArrayList<TranslationInfoDTO>();
		TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
		translationInfoDTO.setActiveFlag("1");
//		translationInfoDTO.setCreateBy(""+userId);
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(userDTO.getUid());
		translationInfoDTO.setCreateBys(idsEr.getResult());
		pager.setRows(5);
		ExecuteResult<DataGrid<TranslationInfoDTO>> er= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
		//查询主单信息
		er = translationExportService.queryTranslationInfoList(translationInfoDTO, pager);
		
		if( er.isSuccess() && er.getResult() != null ){
			JSONArray dtoArr = new JSONArray();
			for( TranslationInfoDTO dto: er.getResult().getRows() ){
				UserDTO seller = this.userExportService.queryUserById(Long.parseLong(dto.getCreateBy()));
				if(seller == null || seller.getCompanyName()== null || "null".equals(seller.getCompanyName())){
					dto.setAlternate1("公司名称为空");
				}else{
					dto.setAlternate1(seller.getCompanyName());
				}
				dto.setTranslationNo(dto.getTranslationNo());
				dtoArr.add(dto);
			}
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords( dtoArr );
		}
		
		model.addAttribute("pager", pager);
		return "/askItemInfo/askItemInfoPage";
	}

	/**
	 * 跳转到求购单创建主页
	 * @author 成文涛 创建时间：2015-5-28 下午3:21:45
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/askItemInfoCreatePage")
	public String askItemInfoCreatePage(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		//调用dubbo生成求购编码
		ExecuteResult<String> translationNo =  translationExportService.createTranslationNo();
		
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("uName", userDTO.getCompanyName());
		model.addAttribute("uid", userId);
		if(translationNo.isSuccess()){
			model.addAttribute("translationNo", translationNo.getResult());
		}
		model.addAttribute("annex", "null");
		return "/askItemInfo/askItemInfoCreatePage";
	}

	
	
	/**
		 * 跳往求购编辑界面
		 * @author chengwt 创建时间：下午4:07:38
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/askItemInfoUpdate")
	public String askItemInfoUpdate(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
    		return "redirect:/user/login";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO seller = this.userExportService.queryUserById(userId);
		String dtoNo = request.getParameter("translationNo");
		String result = "/askItemInfo/askItemInfoPage";
		TranslationInfoDTO dto = new TranslationInfoDTO();
		Pager pager = new Pager();
		pager.setRows(1);
		dto.setTranslationNo(dtoNo);
		dto.setActiveFlag("1");
		//查询求购主单信息（未去重）
		ExecuteResult<DataGrid<TranslationInfoDTO>> translationDTOS =  translationExportService.queryTranslationInfoList(dto,pager);
		pager.setRows(Integer.MAX_VALUE);
		//查询求购主单信息(已去重)，当做求购明细，展示求购里的产品类别名称等信息
		ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(dto, pager);
		if(translationDTOS.isSuccess()){
			TranslationInfoDTO translationInfoDTO = translationDTOS.getResult().getRows().get(0);
			model.addAttribute("translationId", translationInfoDTO.getId());
			model.addAttribute("translationNo", translationInfoDTO.getTranslationNo());
			model.addAttribute("translationName", translationInfoDTO.getTranslationName());
			model.addAttribute("printerName", translationInfoDTO.getTranslationName());
			model.addAttribute("printerId", translationInfoDTO.getPrinterId());
			model.addAttribute("supplierId", translationInfoDTO.getSupplierId());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			if(translationInfoDTO.getAnnex() == null ||  "".equals(translationInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != translationInfoDTO.getAnnex()) {
					fileName = translationInfoDTO.getAnnex().substring(translationInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", translationInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", translationInfoDTO.getBeginDate());
			model.addAttribute("endDate", translationInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", translationInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", translationInfoDTO.getRemarks());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			//取出map放入list中，前台展示用
			int i = 1;
			for(Map mapp : translationDTOList.getResult().getRows()){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				map.put("ids", ""+mapp.get("id"));
				map.put("category_ids", ""+mapp.get("categoryId"));
				map.put("category_names", ""+mapp.get("cName"));
				map.put("matCd", ""+mapp.get("matCd"));
				map.put("matAttribute", ""+mapp.get("matAttribute"));
				map.put("quantity", ""+mapp.get("quantity"));
				//存放标识，在保存时用来区分，u代表需要更新
				map.put("flag", "u");
				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
			model.addAttribute("uid", userId);
			if(seller == null || seller.getCompanyName()== null || "null".equals(seller.getCompanyName())){
				model.addAttribute("uName", "公司名称为空");
			}else{
				model.addAttribute("uName", seller.getCompanyName());
			}
			result = "/askItemInfo/askItemInfoCreatePage";
		}
		return result;
	}
	
	/**
		 * 跳往卖家求购报价界面
		 * @author chengwt 创建时间：上午10:06:20
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/repAskItemInfo")
	public String repAskItemInfo(Pager pager,Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
    		return "redirect:/user/login";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO seller = this.userExportService.queryUserById(userId);
		List<TranslationInfoDTO> dtos = new ArrayList<TranslationInfoDTO>();
		TranslationInfoDTO translationDTO = new TranslationInfoDTO();
		translationDTO.setActiveFlag("1");
		pager.setRows(5);
		List<String> statusList = new ArrayList<String>();
		statusList.add("2");
		statusList.add("3");
		statusList.add("4");
		statusList.add("5");
		ExecuteResult<DataGrid<TranslationInfoDTO>> er= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
		//查询求购单主单信息
		er = translationExportService.queryTranslationInfoList(translationDTO, pager);
		if( er.isSuccess() && er.getResult() != null ){
			JSONArray dtoArr = new JSONArray();
			for( TranslationInfoDTO dto: er.getResult().getRows() ){
				UserDTO user = this.userExportService.queryUserById(Long.parseLong(dto.getCreateBy()));
				if(user == null || user.getCompanyName()== null || "null".equals(user.getCompanyName())){
					dto.setAlternate1("公司名称为空");
				}else{
					dto.setAlternate1(user.getCompanyName());
				}
				//店铺下有此类目，才能显示此求购
				dtoArr.add(dto);
			}
			
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords( dtoArr );
		}
		
		model.addAttribute("pager", pager);
		//根据店铺id查询店铺名称
		ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(seller.getShopId());
		model.addAttribute("shopId", shop.getResult().getShopId());
		model.addAttribute("shopName", shop.getResult().getShopName());
		return "/askItemInfo/RepAskItemInfoPage";
	}



	/**
	 * 跳转到求购单卖家报价页面
	 * @author 成文涛 创建时间：2015-5-28 下午3:21:45
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/repAskItemInfoUpdate")
	public String repAskItemInfoUpdate(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
    		return "redirect:/user/login";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String translationNo = request.getParameter("translationNo");
		String result = "/askItemInfo/RepAskItemInfoPage";
		TranslationInfoDTO dto = new TranslationInfoDTO();
		Pager pager = new Pager();
		pager.setRows(1);
		dto.setTranslationNo(translationNo);
		dto.setActiveFlag("1");
		//查询求购单主单信息
		ExecuteResult<DataGrid<TranslationInfoDTO>> translationDTOS =  translationExportService.queryTranslationInfoList(dto,pager);
		//查询明细中的supplyID为登陆人所属的supplyId相同的才展示
		TranslationMatDTO detailDTO = new TranslationMatDTO();
		detailDTO.setActiveFlag("1");
		detailDTO.setShopId(userDTO.getShopId().intValue());
		detailDTO.setTranslationNo(translationNo);
		//暂时把人员公司关系注释
		pager.setRows(Integer.MAX_VALUE);
		//查询求购单主单(未去重)，当做明细展示
		ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(dto, pager);
		//查询求购单明细
		ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(detailDTO, pager);
		List<Map> mapList = new ArrayList<Map>();
		//标示字符串，i标示保存时插入，u标示修改
		String addFlag ="i";
		
		if(translationDTOS.isSuccess()){
			TranslationInfoDTO translationInfoDTO = translationDTOS.getResult().getRows().get(0);
			UserDTO user = this.userExportService.queryUserById(Long.parseLong(translationInfoDTO.getCreateBy()));
			model.addAttribute("translationId", translationInfoDTO.getId());
			model.addAttribute("translationNo", translationInfoDTO.getTranslationNo());
			model.addAttribute("translationName", translationInfoDTO.getTranslationName());
			if(user == null || user.getCompanyName()== null || "null".equals(user.getCompanyName())){
				model.addAttribute("printerName", "公司名字为空");
			}else{
				model.addAttribute("printerName", translationInfoDTO.getTranslationName());
			}
			model.addAttribute("printerId", translationInfoDTO.getPrinterId());
			model.addAttribute("supplierId", translationInfoDTO.getSupplierId());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			if(translationInfoDTO.getAnnex() == null ||  "".equals(translationInfoDTO.getAnnex()) || "null".equals(translationInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != translationInfoDTO.getAnnex()) {
					fileName = translationInfoDTO.getAnnex().substring(translationInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", translationInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", translationInfoDTO.getBeginDate());
			model.addAttribute("endDate", translationInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", translationInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", translationInfoDTO.getRemarks());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			int i = 1;
			//取出map放入list中，前台展示用
			//明细中有此供应商的则只修改此供应商的这条记录，否则，查询主单的物品名、数量信息
			if(detailDTOList.getResult().getTotal() > 0){
				mapList = detailDTOList.getResult().getRows();
				addFlag = "u";
			}else{
				mapList = translationDTOList.getResult().getRows();
			}
			for(Map dtoItem : mapList){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				map.put("category_ids", ""+dtoItem.get("categoryId"));
				map.put("category_names", ""+dtoItem.get("cName"));
				if("u".equals(addFlag)){
					map.put("matDesc", ""+dtoItem.get("matDesc"));
					map.put("detailStartDate", ""+dtoItem.get("beginDate"));
					map.put("detailEndDate", ""+dtoItem.get("endDate"));
				}else{
					map.put("matDesc", ""+dtoItem.get("matCd"));
				}
			
				map.put("quantity", ""+dtoItem.get("quantity"));
				if("null".equals(dtoItem.get("matPrice")) || dtoItem.get("matPrice") == null){
					map.put("matPrice",  "");
				}else{
					map.put("matPrice",  ""+dtoItem.get("matPrice"));
				}
				map.put("matAttribute", ""+dtoItem.get("matAttribute"));
				map.put("addFlag", addFlag);
				map.put("id", ""+dtoItem.get("id"));
				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
			model.addAttribute("update_flag", "1");
			result = "/askItemInfo/RepAskItemInfoUpdatePage";
		}
		return result;
	}

	/**
	 * 暂未启用
	 * @author 成文涛 创建时间：2015-5-28 下午3:21:45
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSellerBuyerDetail")
	public String getSellerBuyerDetail(
			Pager<UserDTO> page, Model model, Integer uType) {
		UserType userType = UserType.getEnumBycode(uType);
		DataGrid<UserDTO> users=new DataGrid<UserDTO>();
		
		if (userType != null) {
			 users = userExportService
					.findUserListByCondition(null, userType, page);
		}
		page.setTotalCount(users.getTotal().intValue());
		page.setRecords(users.getRows());
		model.addAttribute("userList", users.getRows());
		model.addAttribute("page", page);
		
		return "/askItemInfo/askItemInfoDetail";

	}
	
	
	/**
	 * 查询求购信息
	 * @author chengwt 创建时间：下午2:26:12
	 * @param model
	 * @param request
	 * @param response
	 */
@RequestMapping("/queryTranslation")
public String queryTranslation(Pager pager,Model model, HttpServletRequest request,
		HttpServletResponse response){
	String translationNo = request.getParameter("translationNo");
	String supplierId = request.getParameter("supplierId");
	String supplierName = request.getParameter("supplierName");
	String matCd = request.getParameter("matCd");
	String createBy = request.getParameter("createBy");
	//是否卖家查询标志
	String sellerFlag = request.getParameter("sellerFlag");
	List<TranslationInfoDTO> dtos = new ArrayList<TranslationInfoDTO>();
	TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
	if("1".equals(sellerFlag)){
		createBy = null;
	}else{
		translationInfoDTO.setCreateBy(createBy);
		if(!"".equals(supplierName)){
			ShopDTO shopDTO = new ShopDTO();
			shopDTO.setShopName(supplierName);
			//根据店铺名称查询店铺id，由于是模糊查询，则查询不到店铺id时，则放入id为1的店铺
			ExecuteResult<DataGrid<ShopDTO>> shopList = shopExportService.findShopInfoByCondition(shopDTO, pager);
			List<String> supplierIdList = new ArrayList<String>();
			if(shopList.getResult().getTotal() == 0){
				supplierIdList.add("1");
			}else{
				for(ShopDTO shop : shopList.getResult().getRows()){
					supplierIdList.add(""+shop.getShopId());
				}
			}
			translationInfoDTO.setSupplierIdList(supplierIdList);
		}
	}
	translationInfoDTO.setTranslationNo(translationNo);
	translationInfoDTO.setActiveFlag("1");
	translationInfoDTO.setMatCd(matCd);
	pager.setRows(5);
	ExecuteResult<DataGrid<TranslationInfoDTO>> er= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
	er = translationExportService.queryTranslationInfoList(translationInfoDTO, pager);
	if( er.isSuccess() && er.getResult() != null ){
		JSONArray dtoArr = new JSONArray();
		for( TranslationInfoDTO dto: er.getResult().getRows() ){
			
			JSONObject sp = JSON.parseObject(JSON.toJSONString(dto));
			
			UserDTO user = this.userExportService.queryUserById(Long.parseLong(dto.getCreateBy()));
			if(user == null || user.getCompanyName()== null || "null".equals(user.getCompanyName())){
				dto.setAlternate1("公司名称为空");
			}else{
				dto.setAlternate1(user.getCompanyName());
			}
			dtoArr.add(dto);
		}
		
		pager.setTotalCount(er.getResult().getTotal().intValue());
		pager.setRecords( dtoArr );
	}
	
	model.addAttribute("pager", pager);
	model.addAttribute("sellerFlag", sellerFlag);
	
	
	return "/askItemInfo/askItemInfoModelPage";
}

	/**
	 * 选择类目界面
	 * @param pager
	 * @param cid
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/categoryListPage")
	public String categoryListPage( Pager<ItemQueryOutDTO> pager,String cid ,HttpServletRequest request, Model model ) {
		JSONArray categoryes = this.commonService.findCategory();
		model.addAttribute("categoryes", categoryes);
		logger.debug("ALL CATEGORYES:" +  categoryes.toJSONString());


		model.addAttribute("cid", cid);
		return "/askItemInfo/categoryListPage";
	}

	/**
	 * 进入查看求购信息界面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/lookAskItemInfo")
	public String lookAskItemInfo(Model model, HttpServletRequest request,
									   HttpServletResponse response ,String userType){
		String ctoken = LoginToken.getLoginToken(request);
		
		
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String translationNo = request.getParameter("translationNo");
		//区分买家卖家进入查看页面的标识，response为卖家
		String flag = request.getParameter("flag");
		TranslationInfoDTO dto = new TranslationInfoDTO();
		Pager pager = new Pager();
		pager.setRows(1);
		dto.setTranslationNo(translationNo);
		dto.setActiveFlag("1");

		//查询主单信息
		ExecuteResult<DataGrid<TranslationInfoDTO>> translationDTOS =  translationExportService.queryTranslationInfoList(dto,pager);
		//查询明细中的supplyID为登陆人所属的supplyId相同的才展示
		TranslationMatDTO detailDTO = new TranslationMatDTO();
		detailDTO.setActiveFlag("1");
		if("response".equals(flag)) {
			//如果是卖家进入则只能查看卖家自己的报价信息
			detailDTO.setShopId(userDTO.getShopId().intValue());
		}
		detailDTO.setTranslationNo(translationNo);
		//暂时把人员公司关系注释
		pager.setRows(Integer.MAX_VALUE);
		//查询求购明细信息
		ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(dto, pager);
		ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(detailDTO, pager);
		List<Map> mapList = new ArrayList<Map>();
		//标示字符串，i标示保存时插入，u标示修改
		String addFlag ="i";

		if(translationDTOS.isSuccess()){
			TranslationInfoDTO translationInfoDTO = translationDTOS.getResult().getRows().get(0);
			UserDTO user = this.userExportService.queryUserById(Long.parseLong(translationInfoDTO.getCreateBy()));
			model.addAttribute("translationId", translationInfoDTO.getId());
			model.addAttribute("translationNo", translationInfoDTO.getTranslationNo());
			model.addAttribute("translationName", translationInfoDTO.getTranslationName());
			if(user == null || user.getCompanyName()== null || "null".equals(user.getCompanyName())){
				model.addAttribute("printerName", "公司名字为空");
			}else{
				model.addAttribute("printerName", user.getCompanyName());
			}
			model.addAttribute("printerId", translationInfoDTO.getPrinterId());
			model.addAttribute("supplierId", translationInfoDTO.getSupplierId());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			if(translationInfoDTO.getAnnex() == null ||  "".equals(translationInfoDTO.getAnnex()) || "null".equals(translationInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != translationInfoDTO.getAnnex()) {
					fileName = translationInfoDTO.getAnnex().substring(translationInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", translationInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", translationInfoDTO.getBeginDate());
			model.addAttribute("endDate", translationInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", translationInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", translationInfoDTO.getRemarks());
			model.addAttribute("refusalReason", translationInfoDTO.getRefuseReason());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			int i = 1;
			//取出map放入list中，前台展示用
			//明细中有此供应商的则只修改此供应商的这条记录，否则，查询主单的物品名、数量信息
			if(detailDTOList.getResult().getTotal() > 0){
				mapList = detailDTOList.getResult().getRows();
				addFlag = "u";
			}else{
				mapList = translationDTOList.getResult().getRows();
			}
			for(Map dtoItem : mapList){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))) {
					//根据店铺id获取店铺名称信息
					ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(Long.parseLong("" + dtoItem.get("shopId")));
					map.put("shopName", shop.getResult().getShopName());
				}
				map.put("category_ids", ""+dtoItem.get("categoryId"));
				map.put("category_names", ""+dtoItem.get("cName"));
				if("u".equals(addFlag)){
					map.put("matDesc", ""+dtoItem.get("matDesc"));
					map.put("detailStartDate", ""+dtoItem.get("beginDate"));
					map.put("detailEndDate", ""+dtoItem.get("endDate"));
				}else{
					map.put("matDesc", ""+dtoItem.get("matCd"));
				}

				map.put("quantity", ""+dtoItem.get("quantity"));
				if("null".equals(dtoItem.get("matPrice")) || dtoItem.get("matPrice") == null){
					map.put("matPrice",  "");
				}else{
					map.put("matPrice",  ""+dtoItem.get("matPrice"));
				}
				map.put("matAttribute", ""+dtoItem.get("matAttribute"));
				map.put("addFlag", addFlag);
				map.put("id", ""+dtoItem.get("id"));
				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
			model.addAttribute("update_flag", "1");
			model.addAttribute("userType", userType);
		}
		model.addAttribute("flag", flag);
		return "/askItemInfo/askItemInfoLookPage";
	}
	

}
