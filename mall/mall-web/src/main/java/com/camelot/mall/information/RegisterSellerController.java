package com.camelot.mall.information;

import java.math.BigDecimal;
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
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.mall.dto.ShopInfoInRedisDTO;
import com.camelot.mall.service.ShopClientService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopDomainExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserAccountDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserManageDTO;
import com.camelot.usercenter.dto.userInfo.UserOrganizationDTO;
import com.camelot.usercenter.dto.userInfo.UserTaxDTO;
import com.camelot.usercenter.enums.UserEnums.DealerType;
import com.camelot.usercenter.enums.UserEnums.ERPType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/information/registerSeller")
public class RegisterSellerController {

	@Resource
	private RedisDB redisDB;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private UserCompanyService userCompanyService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ShopCategoryExportService shopCategoryExportService;
	@Resource
	private ShopBrandExportService shopBrandExportService;
	@Resource
	private ItemBrandExportService itemBrandExportService;
	@Resource
	private ShopClientService shopClientService;
	@Resource
	private ShopDomainExportService shopDomainExportService;

	/**
	 * 卖家认证
	 */
	@RequestMapping("/initSeller")
	public String initSeller(HttpServletRequest request,Model model) {
		//获取省份/经营类型/erp类型
        List<com.camelot.basecenter.domain.AddressBase> addressBaseList = addressBaseService.queryAddressBase("0");
		model.addAttribute("addressList", addressBaseList);
		model.addAttribute("dealerTypes", DealerType.values());
		model.addAttribute("erpTypes", ERPType.values());
		//获取用户ID
		Long userId = WebUtil.getInstance().getUserId(request);
		model.addAttribute("userId",userId);

		//判断是否已经认证
		UserDTO userDTO = userExportService.queryUserById(userId);
		Integer uStatus = userDTO.getUserstatus();
		if( uStatus > 4 ){
			return "redirect:/information/informationSeller/initLoad";
		}

		/*加载对应数据:已经认证买家、直接认证卖家*/
		//营业执照信息:获取redis中的对象/买家认证-营业执照信息
		String stagingKeyBusiness = getStagingKeyInRedis("business", request);
		Object stagingObjBusiness = redisDB.getObject(stagingKeyBusiness);
		UserBusinessDTO userBusinessDTO = (UserBusinessDTO) stagingObjBusiness;
		//组织机构代码信息:获取redis中的对象
		String stagingKeyOrganization = getStagingKeyInRedis("organization", request);
		Object stagingObjOrganization = redisDB.getObject(stagingKeyOrganization);
		UserOrganizationDTO userOrganizationDTO = (UserOrganizationDTO) stagingObjOrganization;
		//公司税务登记证信息:获取redis中的对象
		String stagingKeyTax = getStagingKeyInRedis("tax", request);
		Object stagingObjTax = redisDB.getObject(stagingKeyTax);
		UserTaxDTO userTaxDTO = (UserTaxDTO) stagingObjTax;
		//出金账户信息:获取redis中的对象
		String stagingKeyAccount = getStagingKeyInRedis("account", request);
		Object stagingObjAccount = redisDB.getObject(stagingKeyAccount);
		UserAccountDTO userAccountDTO = (UserAccountDTO) stagingObjAccount;
		//公司经营信息:获取redis中的对象
		String stagingKeyManage = getStagingKeyInRedis("manage", request);
		Object stagingObjManage = redisDB.getObject(stagingKeyManage);
		UserManageDTO userManageDTO = (UserManageDTO) stagingObjManage;
		//3-买家待审核，4-买家审核通过
		if(uStatus==3 || uStatus==4){
			ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(userId);
			UserInfoDTO userInfoDTO = userInfoResult.getResult();
			model.addAttribute("extendId", userInfoDTO.getExtendId());
			if(userBusinessDTO == null){
				userBusinessDTO = userInfoDTO.getUserBusinessDTO();
			}
		}
		model.addAttribute("userBusinessDTO", userBusinessDTO);
		model.addAttribute("userOrganizationDTO", userOrganizationDTO);
		model.addAttribute("userTaxDTO", userTaxDTO);
		model.addAttribute("userAccountDTO", userAccountDTO);
		model.addAttribute("userManageDTO", userManageDTO);

		//卖家店铺运营信息
		ShopDTO shopInfo = new ShopDTO();			//店铺信息
		Long[] brandIds = new Long[]{}; 			//品牌id
		Long[] scids = new Long[]{};				//三级类目id
		ExecuteResult<List<ItemBrandDTO>> brandlist = new ExecuteResult<List<ItemBrandDTO>>();//卖家已选的品牌数据列表
		Map<String, String> categoryNameList = new HashMap<String, String>();//卖家已选的类目信息列表字符串
		Object shopInfoInRedis = redisDB.getObject(this.getStagingKeyInRedis("shopApply", request));//店铺信息

		if(null==shopInfoInRedis){//未暂存过或者已经提交：从数据库中查询店铺信息
			Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
			if(null!=shopId){
				shopInfo = shopExportService.findShopInfoById(shopId).getResult();
				ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
				shopBrandDTO.setShopId(shopId);
				ExecuteResult<DataGrid<ShopBrandDTO>> shopBrandList = shopBrandExportService.queryShopBrand(shopBrandDTO, null);
				brandIds = shopClientService.buildBrandIds(shopBrandList);
				ExecuteResult<List<ShopCategoryDTO>> shopcategorylist = shopCategoryExportService.queryShopCategoryList(shopId, ShopCategoryStatus.PASS.getCode());
				scids = shopClientService.buildShopCategoryIds(shopcategorylist.getResult());
			}
		}else{//已经暂存过：存在于redis中
			ShopInfoInRedisDTO shopInfoRedis = (ShopInfoInRedisDTO) shopInfoInRedis;
			shopInfo = shopInfoRedis.getShopDTO();
			scids = shopClientService.getCids(shopInfoRedis);
			brandIds = shopClientService.getBrandIds(shopInfoRedis);
			model.addAttribute("brandIds", shopInfoRedis.getBrandIds());
		}
		if(null != shopInfo){
			brandlist = itemBrandExportService.queryItemBrandByIds(brandIds);
			ExecuteResult<List<ItemCatCascadeDTO>> cascadeCategoryList = itemCategoryService.queryParentCategoryList(scids);
			categoryNameList = shopClientService.buildCategoryNameList(cascadeCategoryList.getResult());//组装一级、二级、三级类目的名称
		}
		//平台一级类目
		DataGrid<ItemCategoryDTO> categorylist = itemCategoryService.queryItemCategoryList(0L);

		model.addAttribute("levOneCategoryList", categorylist.getRows());//平台一级类目
		model.addAttribute("shopInfo", shopInfo);		//当前店铺信息
		model.addAttribute("shopBrandList", brandlist.getResult());	//当前店铺经营的所有品牌信息
		model.addAttribute("categoryNameList", categoryNameList);	//当前店铺正在经营的类目名称集合
		model.addAttribute("existCids", shopClientService.buildExistsCids(scids));	//当前店铺正在经营的类目id数组

		model.addAttribute("pageState", "register");
		return "/information/registerSeller";
	}

	/**
	 * 暂存：卖家认证信息
	 */
	@ResponseBody
	@RequestMapping("stagingInformation")
	public Map<String,Object> stagingInformation(int stagingType, UserInfoDTO userInfoDTO, String dealerType, String erpType,
			String addBrandIds, String addCategoryCids, ShopDTO shopDTO, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		switch(stagingType)
		{
			case 1:
				//营业执照信息
				redisDB.addObject(this.getStagingKeyInRedis("business", request), userInfoDTO.getUserBusinessDTO());
				break;
			case 2:
				//组织机构代码证
				redisDB.addObject(this.getStagingKeyInRedis("organization", request), userInfoDTO.getUserOrganizationDTO());
				break;
			case 3:
				//税务登记证
				redisDB.addObject(this.getStagingKeyInRedis("tax", request), userInfoDTO.getUserTaxDTO());
				break;
			case 4:
				//开户银行许可证
				redisDB.addObject(this.getStagingKeyInRedis("account", request), userInfoDTO.getUserAccountDTO());
				break;
			case 5:
				//经营信息
				UserManageDTO userManageDTO = userInfoDTO.getUserManageDTO();
				if(!StringUtils.isBlank(dealerType)){
					userManageDTO.setDealerType(DealerType.valueOf(dealerType));
				}
				if(!StringUtils.isBlank(erpType)){
					userManageDTO.setErpType(ERPType.valueOf(erpType));
				}
				redisDB.addObject(this.getStagingKeyInRedis("manage", request), userManageDTO);
				break;
			case 6:
				//店铺申请运营信息
				ShopInfoInRedisDTO origShopDTO = new ShopInfoInRedisDTO();
				origShopDTO.setShopDTO(shopDTO);
				origShopDTO.setBrandIds(addBrandIds);
				origShopDTO.setCids(addCategoryCids);
				redisDB.addObject(this.getStagingKeyInRedis("shopApply", request), origShopDTO);
				break;
			default:
				break;
		}
		map.put("result", "暂存成功!");
		return map;
	}

	/**
	 * 提交方法
	 */
	@ResponseBody
	@RequestMapping("submintInformation")
	public Map<String,Object> submintInformation(String addBrandIds, String addCategoryCids, ShopDTO shopDTO, UserInfoDTO userInfoDTO, String dealerType, String erpType, UserCompanyDTO userCompanyDTO,HttpServletRequest request, Model model){
		Map<String,Object> map = new HashMap<String,Object>();
        try{
            //获取用户ID
            Long userId = userInfoDTO.getUserId();
            UserDTO userDTO = userExportService.queryUserById(userId);
		    /* 判断是否已经认证 */
            Integer userstatus = userDTO.getUserstatus();
            if(userstatus > 4){
                map.put("userInfoMessage", "当前用户状态,不可卖家认证!");
                return map;
            }

		    /* 保存：营业执照信息/组织机构代码信息/税务登记证信息/(入/出)金账户信息 */
            userInfoDTO.setUserType(UserType.SELLER);							//卖家认证
            userInfoDTO.getUserOrganizationDTO().setOrganizationStatus(1);		//提交-状态：组织机构代码信息
            userInfoDTO.getUserTaxDTO().setTaxStatus(1);						//提交-状态：税务登记证信息
		    /* 保存店铺信息 */
            map = this.saveShopInfoInDB(userId, addBrandIds, addCategoryCids, shopDTO);

            userInfoDTO.getUserBusinessDTO().setBusinessStatus(1); 					//提交-状态：营业执照信息
            userInfoDTO.getUserAccountDTO().setBankAccountStatus(1);				//提交-状态：(入/出)金账户信息

            //设置erp类型和公司类型
            UserManageDTO userManageDTO = userInfoDTO.getUserManageDTO();
            if(userManageDTO != null){
                if(!StringUtils.isBlank(dealerType)){
                    userManageDTO.setDealerType(DealerType.valueOf(dealerType));
                }
                if(!StringUtils.isBlank(erpType)){
                    userManageDTO.setErpType(ERPType.valueOf(erpType));
                }
            }
            //去掉businessCode的最后一个逗号
            UserBusinessDTO ubdto = userInfoDTO.getUserBusinessDTO();
            if(null != ubdto){
                String businessScope = ubdto.getBusinessScope();
                if(org.apache.commons.lang3.StringUtils.isNotBlank(businessScope) && businessScope.endsWith(",")){
                    ubdto.setBusinessScope(businessScope.substring(0, businessScope.length()-1));
                }
            }

            map.putAll(this.saveUserInfo(userInfoDTO));

		    /* 更改用户状态-更改审批记录状态 */
            String result = map.get("result").toString();
            if("success".equals(result)){
                //更改用户状态  liwl修改  提交认证时不修改type 
//            	userDTO.setUsertype(2);
                userDTO.setUserstatus(5);
                userExportService.modifyUserInfo(userDTO);
                //更改审批记录状态
                userDTO.setAuditStatus(1);
                userExportService.modifyUserInfoAndAuditStatus(userDTO);

                //清空redis中的对象:营业执照信息/组织机构代码证/税务登记证/开户银行许可证/经营信息/店铺申请运营信息
                redisDB.del(this.getStagingKeyInRedis("business", request));
                redisDB.del(this.getStagingKeyInRedis("organization", request));
                redisDB.del(this.getStagingKeyInRedis("tax", request));
                redisDB.del(this.getStagingKeyInRedis("account", request));
                redisDB.del(this.getStagingKeyInRedis("manage", request));
                redisDB.del(this.getStagingKeyInRedis("shopApply", request));
                map.put("userInfoMessage", "提交成功!");
            }else{
                map.put("userInfoMessage", "提交失败!");
            }
        } catch (Exception e) {
            map.put("message",e.getMessage());
            e.printStackTrace();
        }

		return map;
	}

	/**
	 * 保存：用户信息集合（含营业执照，组织代码，税务，账户，中信账户）
	 */
	public Map<String,Object> saveUserInfo(UserInfoDTO userInfoDTO){
		Map<String,Object> map = new HashMap<String,Object>();
		ExecuteResult<UserInfoDTO> result;
		if(userInfoDTO.getExtendId()!=null){
			result = userExtendsService.updateUserExtends(userInfoDTO);
		}else{
			result = userExtendsService.createUserExtends(userInfoDTO);
		}
		map.put("userInfoDTO", result.getResult());
		map.put("message", result.getResultMessage());
		map.put("error", result.getErrorMessages());
		return map;
	}

	/**
	 * 保存：店铺信息
	 */
	@ResponseBody
	@RequestMapping("/saveShopInfo")
	public Map<String, Object> saveShopInfoInDB(Long userId, String addBrandIds, String addCategoryCids, ShopDTO shopDTO){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", "failure");
		//店铺信息
		ShopDTO origShopDTO = new ShopDTO();
		origShopDTO.setCityCode(shopDTO.getCityCode());//市的代码
		origShopDTO.setCityName(shopDTO.getCityName());//市的名字
		origShopDTO.setDistrictCode(shopDTO.getDistrictCode());//区的代码
		origShopDTO.setDistrictName(shopDTO.getDistrictName());//区的名字
		origShopDTO.setProvinceCode(shopDTO.getProvinceCode());//省份代码
		origShopDTO.setProvinceName(shopDTO.getProvinceName());//省份名字
		origShopDTO.setShopName(shopDTO.getShopName());	//店铺名称
		origShopDTO.setShopUrl(shopDTO.getShopUrl());//店铺域名
		origShopDTO.setStreetName(shopDTO.getStreetName());//街道地址
		origShopDTO.setSellerId(userId);//卖家id
		origShopDTO.setStatus(1);//状态默认为新申请
		origShopDTO.setShopType(shopDTO.getShopType());//店铺类型
		origShopDTO.setBrandType(shopDTO.getBrandType());//品牌类型
		origShopDTO.setBusinessType(shopDTO.getBusinessType());//经营类型
		origShopDTO.setDisclaimer(shopDTO.getDisclaimer());
		origShopDTO.setMarketingAuth(shopDTO.getMarketingAuth());//销售授权书扫描件
		origShopDTO.setTrademarkRegistCert(shopDTO.getTrademarkRegistCert());//商标注册证/商品注册申请书扫描件
		origShopDTO.setInspectionReport(shopDTO.getInspectionReport());;//质检、检疫、检验报告/报关单类扫描件
		origShopDTO.setProductionLicense(shopDTO.getProductionLicense());//卫生/生产许可证扫描件
		origShopDTO.setInitialPrice(BigDecimal.ZERO);// 设置起批价格
		origShopDTO.setInitialMount(1L);// 设置起批数量
		//新增店铺信息
		ExecuteResult<String> retVal = shopExportService.saveShopInfo(origShopDTO);

		if(retVal.getErrorMessages()!=null && retVal.getErrorMessages().size()>0){
			result.put("result", "error");
		} else {
			//店铺id
			Long shopId = Long.parseLong(retVal.getResult());
			//保存类目数据
			shopClientService.addShopCategory(addCategoryCids, userId, shopId);
			//保存品牌数据
			shopClientService.addShopBrand(addBrandIds, userId, shopId);

			//将店铺id更新到用户表中
			UserDTO userDTO = userExportService.queryUserById(userId);
			userDTO.setShopId(shopId);
			userExportService.modifyUserInfo(userDTO);

			result.put("result", "success");
		}
		return result;
	}

	/**
	 * 获取：暂存key
	 */
	private String getStagingKeyInRedis(String type,HttpServletRequest request) {
		Long userId = WebUtil.getInstance().getUserId(request);//卖家id
		return "staging_"+type+"_"+userId;
	}


	/**
	 * 校验店铺域名是否存在
	 * @param shopUrl
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkShopUrl")
	public Boolean saveShopInfoInDB(String shopUrl,HttpServletRequest request){
		//获取当前店铺ID
		Long shopId = WebUtil.getInstance().getShopId(request);
		ExecuteResult<Boolean> shopUrlResult = shopDomainExportService.existShopUrl(shopUrl,shopId);

        return shopUrlResult.getResult();
	}

}
