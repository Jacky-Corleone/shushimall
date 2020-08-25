package com.camelot.mall.information;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.mall.service.ShopClientService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopInfoModifyInDTO;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopDomainExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.DealerType;
import com.camelot.usercenter.enums.UserEnums.ERPType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserContractService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/information/progressSeller")
public class ProgressSellerController {
    private Logger LOG = LoggerFactory.getLogger(ProgressSellerController.class);

	@Resource
	private UserExportService userExportService;
	@Resource
	private UserContractService userContractService;
	@Resource
	private SattleCatExpenseExportService sattleCatExpenseExportService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private ShopCategoryExportService shopCategoryExportService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private ShopBrandExportService shopBrandExportService;
	@Resource
	private ItemBrandExportService itemBrandExportService;
	@Resource
	private ShopClientService shopClientService;
	@Resource
	private ShopDomainExportService shopDomainExportService;

	/**
	 * 审核进度
	 * @return
	 */
	@RequestMapping("initProgress")
	public String initProgress(HttpServletRequest request,Model model){
		Long userId = WebUtil.getInstance().getUserId(request);
		model.addAttribute("userId",userId);
		/* 卖家资质审核 */
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("userDTO",userDTO);
		ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(userId);
		model.addAttribute("userInfoDTO",userInfoResult.getResult());
		/* 合同信息 */
		UserContractDTO userContractDTO  = new UserContractDTO();
		userContractDTO.setShopId(userDTO.getShopId());
		ExecuteResult<UserContractDTO> userContractResult = userContractService.findUserContractByCondition(userContractDTO);
		model.addAttribute("userContractDTO",userContractResult.getResult());
		/* 店铺确定-店铺开通 */
		ExecuteResult<ShopDTO> shopResult = shopExportService.findShopInfoById(userDTO.getShopId());
		model.addAttribute("shopDTO",shopResult.getResult());
		/* 付款确定 */
		//查询类目ID
		ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
		shopCategoryDTO.setShopId(userDTO.getShopId());
		ExecuteResult<DataGrid<ShopCategoryDTO>> shopCategoryResult = shopCategoryExportService.queryShopCategoryAll(shopCategoryDTO, null);
		List<ShopCategoryDTO> shopCategoryList = shopCategoryResult.getResult().getRows();
		model.addAttribute("shopCategorySum",shopCategoryList.size());
		model.addAttribute("shopCategoryList",shopCategoryList);
		if(shopCategoryList!=null && shopCategoryList.size()>0){
			Long[] cids = new Long[shopCategoryList.size()];
			for(int i=0; i < shopCategoryList.size(); i++){
				cids[i] = shopCategoryList.get(i).getCid();
			}
			//查询：类目名称
			ExecuteResult<List<ItemCatCascadeDTO>> itemCatCascadeResult = itemCategoryService.queryParentCategoryList(cids);
			List<ItemCatCascadeDTO> itemCatCascadeList = itemCatCascadeResult.getResult();
			Map<Long,List<String>> itemCatCascadeMap = new HashMap<Long,List<String>>();
			//一级类目
			for(ItemCatCascadeDTO itemCatFirst : itemCatCascadeList){
				String firstName = itemCatFirst.getCname();
				//二级类目
				for(ItemCatCascadeDTO itemCatSecond : itemCatFirst.getChildCats()){
					String secondName = itemCatSecond.getCname();
					//三级类目
					for(ItemCatCascadeDTO itemCatThird : itemCatSecond.getChildCats()){
						List<String> itemCatNameList = new ArrayList<String>();
						itemCatNameList.add(firstName);
						itemCatNameList.add(secondName);
						itemCatNameList.add(itemCatThird.getCname());
						itemCatCascadeMap.put(itemCatThird.getCid(), itemCatNameList);
					}
				}
			}
			//查询：平台质量保证金-平台使用费
			ExecuteResult<List<SettleCatExpenseDTO>> settleCatExpenseResult = sattleCatExpenseExportService.queryByIds(cids);
			List<SettleCatExpenseDTO> settleCatExpenseList = settleCatExpenseResult.getResult();
			Map<Long,SettleCatExpenseDTO> settleCatExpenseMap = new HashMap<Long,SettleCatExpenseDTO>();
			for(SettleCatExpenseDTO settleCatExpense:settleCatExpenseList){
				settleCatExpenseMap.put(settleCatExpense.getCategoryId(), settleCatExpense);
			}
			//数据处理-用于显示
			List<List<String>> marginList = new ArrayList<List<String>>();
			List<List<String>> royaltiesList = new ArrayList<List<String>>();
			double marginSummary = 0L;
			double royaltiesSummary = 0L;
			for(int i=0; i < shopCategoryList.size(); i++){

				List<String> margin = new ArrayList<String>();
				List<String> royalties = new ArrayList<String>();
				ShopCategoryDTO shopCategory = shopCategoryList.get(i);
				Long cid = shopCategory.getCid();

				//类目对象：一级名称/二级名称/三级名称
				List<String> itemCatNameList = itemCatCascadeMap.get(cid);
				margin.add(itemCatNameList.get(0));
				royalties.add(itemCatNameList.get(0));
				margin.add(itemCatNameList.get(1));
				royalties.add(itemCatNameList.get(1));
				margin.add(itemCatNameList.get(2));
				royalties.add(itemCatNameList.get(2));

				//金额：平台质量保证金-平台使用费
				SettleCatExpenseDTO settleCatExpense = settleCatExpenseMap.get(cid);
				if(settleCatExpense!=null){
					BigDecimal cashDeposit = settleCatExpense.getCashDeposit();
					BigDecimal serviceFee = settleCatExpense.getServiceFee();

					royalties.add(String.format("%.2f", serviceFee));
					margin.add(String.format("%.2f", cashDeposit));
					marginList.add(margin);
					royaltiesList.add(royalties);

					marginSummary += cashDeposit.doubleValue();
					royaltiesSummary += serviceFee.doubleValue();
				}
			}
			model.addAttribute("marginList",marginList);
			model.addAttribute("royaltiesList",royaltiesList);
			model.addAttribute("marginSummary",String.format("%.2f", marginSummary));
			model.addAttribute("royaltiesSummary",String.format("%.2f", royaltiesSummary));
		}
		return "/information/progressSeller";
	}

	@RequestMapping("/userInfo")
	public String userInfo(String pageState, HttpServletRequest request,Model model) {
		//获取省份
		model.addAttribute("addressList", addressBaseService.queryAddressBase("0"));
		//经营类型
		model.addAttribute("dealerTypes", DealerType.values());
		//erp类型
		model.addAttribute("erpTypes", ERPType.values());
		//获取用户ID
		Long userId = WebUtil.getInstance().getUserId(request);
		model.addAttribute("userId",userId);
		if(userId == null){
			return "500";
		}
		//店铺id
		Long shopId = WebUtil.getInstance().getShopId(request);
		model.addAttribute("shopId",shopId);
		//店铺信息
		ShopDTO shopDTO = shopExportService.findShopInfoById(shopId).getResult();
		model.addAttribute("shopDTO", shopDTO);
		//加载对应数据
		ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(userId);
		UserInfoDTO userInfoDTO = userInfoResult.getResult();
		model.addAttribute("extendId", userInfoDTO.getExtendId());
		model.addAttribute("userBusinessDTO", userInfoDTO.getUserBusinessDTO());
		model.addAttribute("userOrganizationDTO", userInfoDTO.getUserOrganizationDTO());
		model.addAttribute("userTaxDTO", userInfoDTO.getUserTaxDTO());
		model.addAttribute("userAccountDTO", userInfoDTO.getUserAccountDTO());
		model.addAttribute("userManageDTO", userInfoDTO.getUserManageDTO());
		//营业执照信息-营业执照所在地
		String businessLicenceAddressCode = userInfoDTO.getUserBusinessDTO() == null ? "":userInfoDTO.getUserBusinessDTO().getBusinessLicenceAddress();
		model.addAttribute("fianalBusinessLicenceAddress",this.getNameByCode(businessLicenceAddressCode));
		//营业执照信息-公司所在地
		String companyAddressCode = userInfoDTO.getUserBusinessDTO() == null ? "":userInfoDTO.getUserBusinessDTO().getCompanyAddress();
		model.addAttribute("finalCompanyAddress",this.getNameByCode(companyAddressCode));
		//出金账户信息-开户行支行所在地
		String bankBranchIsLocatedCode = userInfoDTO.getUserAccountDTO() == null ? "":userInfoDTO.getUserAccountDTO().getBankBranchIsLocated();
		model.addAttribute("finalBankBranchIsLocated",this.getNameByCode(bankBranchIsLocatedCode));

		model.addAttribute("pageState", pageState);
		return "/information/progressUserInfo";
	}

	@RequestMapping("/shopInfo")
	public String shopInfo(String pageState, HttpServletRequest request,Model model) {
		//获取用户ID
		Long userId = WebUtil.getInstance().getUserId(request);
		model.addAttribute("userId",userId);
		if(userId == null){
			return "500";
		}
		//平台一级类目
		DataGrid<ItemCategoryDTO> categorylist = itemCategoryService.queryItemCategoryList(0L);
		model.addAttribute("levOneCategoryList", categorylist.getRows());
		//店铺id
		Long shopId = WebUtil.getInstance().getShopId(request);
		model.addAttribute("shopId",shopId);
		//店铺信息
		ShopDTO shopInfo = shopExportService.findShopInfoById(shopId).getResult();
		if("edit".equals(pageState)){
			shopInfo.setShopUrl(shopInfo.getShopUrl().replace(".shgreen.shushi100.com", ""));//去除shgreen.printhome.com
		}
		model.addAttribute("shopInfo", shopInfo);

		/*类目信息*/
		ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
		shopCategoryDTO.setShopId(shopId);
		ExecuteResult<DataGrid<ShopCategoryDTO>> shopCategoryResult = shopCategoryExportService.queryShopCategoryAll(shopCategoryDTO, null);
		List<ShopCategoryDTO> shopCategoryList = shopCategoryResult.getResult().getRows();
		Long[] scids = shopClientService.buildShopCategoryIds(shopCategoryList);
		ExecuteResult<List<ItemCatCascadeDTO>> cascadeCategoryList = itemCategoryService.queryParentCategoryList(scids);
		//当前店铺正在经营的类目id数组
		model.addAttribute("existCids", shopClientService.buildExistsCids(scids));

		//卖家已选的类目信息列表字符串：组装一级、二级、三级类目的名称
		Map<String, String> categoryNameList = shopClientService.buildCategoryNameList(cascadeCategoryList.getResult());//
		model.addAttribute("categoryNameList", categoryNameList);

		/*卖家已选的品牌数据列表*/
		ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
		shopBrandDTO.setShopId(shopId);
		ExecuteResult<DataGrid<ShopBrandDTO>> shopBrandResult = shopBrandExportService.queryShopBrandAll(shopBrandDTO,null);
		List<ShopBrandDTO> shopBrandList = shopBrandResult.getResult().getRows();
		Long[] brandIds = new Long[shopBrandList.size()];
		String addBrandIds = "";
		for(int i=0; i < shopBrandList.size(); i++){
			ShopBrandDTO shopBrand = shopBrandList.get(i);
			brandIds[i] = shopBrand.getBrandId();
			addBrandIds += shopBrand.getCid() + ":" + shopBrand.getBrandId() + ",";
		}
		ExecuteResult<List<ItemBrandDTO>> brandlist = itemBrandExportService.queryItemBrandByIds(brandIds);
		model.addAttribute("shopBrandList", brandlist.getResult());
		if(!"".equals(addBrandIds)){
			model.addAttribute("addBrandIds", addBrandIds.substring(0, addBrandIds.length()-1));
		}

		model.addAttribute("pageState", pageState);
		return "/information/progressShopInfo";
	}

	@ResponseBody
	@RequestMapping("submintUserInfo")
	public Map<String,Object> submintUserInfo(UserInfoDTO userInfoDTO){
		Map<String,Object> map = new HashMap<String,Object>();

		//更改用户状态
		UserDTO userDTO = userExportService.queryUserById(userInfoDTO.getUserId());
//		驳回后重新提交卖家认证不修改type
//		userDTO.setUsertype(3);
		userDTO.setUserstatus(5);
		userExportService.modifyUserInfo(userDTO);
		//更改审批记录状态
		userDTO.setAuditStatus(1);
		userExportService.modifyUserInfoAndAuditStatus(userDTO);

		//保存-提交：营业执照信息/组织机构代码信息/税务登记证信息/出金账户信息
		userInfoDTO.setUserType(UserType.SELLER);
		userInfoDTO.getUserBusinessDTO().setBusinessStatus(1); 				//提交：营业执照信息
		userInfoDTO.getUserOrganizationDTO().setOrganizationStatus(1);		//提交：组织机构代码信息
		userInfoDTO.getUserTaxDTO().setTaxStatus(1);						//提交：税务登记证信息
		userInfoDTO.getUserAccountDTO().setBankAccountStatus(1);			//提交：出金账户信息
		userExtendsService.updateUserExtends(userInfoDTO);

		map.put("message", "提交成功!");
		return map;
	}

	@ResponseBody
	@RequestMapping("submintShopInfo")
	public Map<String,Object> submintShopInfo(ShopDTO shopDTO, String addBrandIds, String addCategoryCids){
		Map<String,Object> map = new HashMap<String,Object>();

		//店铺信息
		shopDTO.setStatus(1);			//店铺建新状态;1是申请，2是通过，3是驳回， 4是平台关闭，5是开通
		shopDTO.setModifyStatus(1);		//店铺信息修改状态，1为待审核，2为驳回，3为修改通过
		shopDTO.setShopUrl(shopDTO.getShopUrl()+".shgreen.shushi100.com");
		//店铺品牌List
		List<ShopBrandDTO> shopBrandList = new ArrayList<ShopBrandDTO>();
		String[] brandIds = addBrandIds.split(",");
		for(String shopBrand : brandIds){
			if(shopBrand!=null && !"".equals(shopBrand)){
				String[] brandId = shopBrand.split(":");
				ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
				shopBrandDTO.setCid(Long.parseLong(brandId[0]));
				shopBrandDTO.setBrandId(Long.parseLong(brandId[1]));
				shopBrandList.add(shopBrandDTO);
			}
		}

		//店铺类目List
		List<ShopCategoryDTO> shopCategoryList = new ArrayList<ShopCategoryDTO>();
		String[] categoryCids = addCategoryCids.split(",");
		for(String cid : categoryCids){
			if(cid!=null && !"".equals(cid)){
				ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
				shopCategoryDTO.setCid(Long.parseLong(cid));
				shopCategoryList.add(shopCategoryDTO);
			}
		}

		//调用方法：店铺信息,并提交审批流
		ShopInfoModifyInDTO shopInfoModifyInDTO = new ShopInfoModifyInDTO();
		shopInfoModifyInDTO.setShopDTO(shopDTO);
		shopInfoModifyInDTO.setShopBrandList(shopBrandList);
		shopInfoModifyInDTO.setShopCategoryList(shopCategoryList);
		shopExportService.modifyShopInfoUpdate(shopInfoModifyInDTO);

		map.put("message", "提交成功!");
		return map;
	}

	@ResponseBody
	@RequestMapping("updateUserContract")
	public Map<String,Object> updateUserContract(String state, String auditRemark, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		UserContractDTO  userContractDTO  = new UserContractDTO();
		userContractDTO.setShopId(userDTO.getShopId());
		ExecuteResult<UserContractDTO> findResult = userContractService.findUserContractByCondition(userContractDTO);
		userContractDTO = findResult.getResult();
		//【驳回-0】【确定-3】
		if("0".equals(state)){
			userContractDTO.setContractStatus(0);
			userContractDTO.setAuditRemark(auditRemark);
		}else if("3".equals(state)){
			userContractDTO.setContractStatus(3);
		}
		ExecuteResult<UserContractDTO> modifyResult = userContractService.modifyUserContractById(userContractDTO, String.valueOf(userId));
		List<String> error = modifyResult.getErrorMessages();
		if(error!=null && error.size()>0){
			map.put("message", error);
		} else {
			map.put("userContractDTO", modifyResult.getResult());
			map.put("message", "操作成功!");
		}
		return map;
	}

	/**
	 * 获取地址名称
	 * @param code
	 * @return
	 */
	private String getNameByCode(String code){
		String name = "";
		if(StringUtils.isNotEmpty(code)){
			String[] codeArray = code.split(",");
			if(codeArray == null || codeArray.length == 0)
				return "";
			for(String codeEvery : codeArray){
				//获取编码对应的地址
				String[] codes = new String[]{codeEvery};
				ExecuteResult<List<AddressBaseDTO>> result = addressBaseService.queryNameByCode(codes);
				List<AddressBaseDTO> addressBaseDTOList = result.getResult();
				String address = "";
				if(addressBaseDTOList != null && !addressBaseDTOList.isEmpty() && addressBaseDTOList.size() >0)
					address = addressBaseDTOList.get(0).getName();
				if(StringUtils.isNotEmpty(name)){
					name += ",";
				}
				name += address;
			}
		}
		return name;
	}
}
