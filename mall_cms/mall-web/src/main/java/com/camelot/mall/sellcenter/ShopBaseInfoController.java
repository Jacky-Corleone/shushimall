package com.camelot.mall.sellcenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.service.ShopClientService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalDTO;
import com.camelot.storecenter.dto.ShopModifyInfoDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
import com.camelot.storecenter.dto.indto.ShopInfoModifyInDTO;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopEvaluationService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopModifyInfoExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [店铺类目管理]</p>
 * Created on 2015年3月6日
 * @author  <a href="mailto: zhoule@camelotchina.com">周 乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/shopBaseInfoController")
public class ShopBaseInfoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShopBaseInfoController.class);
	
	@Resource
	private ShopExportService shopExportService;
	
	@Resource
	private ItemCategoryService itemCategoryService;
	
	@Resource
	private AddressBaseService addressBaseService;
	
	@Resource
	private ShopModifyInfoExportService shopModifyInfoExportService;
	
	@Resource
	private ShopBrandExportService shopBrandExportService;
	
	@Resource
	private ShopCategoryExportService shopCategoryExportService;
	
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private ShopClientService shopClientService;
	
	@Resource 
	private ItemBrandExportService itemBrandExportService;
	
	@Resource 
	private ItemExportService itemExportService;
	
	@Resource
	private ShopEvaluationService shopEvaluationService;
	
	@RequestMapping("toIndex")
	public String toIndex(HttpServletRequest request, Model model){
        //校验店铺是否已关闭
        Long shopId = WebUtil.getInstance().getShopId(request);
        ExecuteResult<ShopDTO> result = shopExportService.findShopInfoById(shopId);
        ShopDTO shop = result.getResult();
        if(shop==null || shop.getStatus()==4 || shop.getRunStatus()==2){//如果店铺状态为关闭则不允许进入店铺信息编辑页面、类目编辑页面和品牌编辑页面
            model.addAttribute("message", "店铺已关闭，无法操作店铺信息");
            model.addAttribute("siteUrl", "/shopBaseInfoController/toView");
            return "/message";
        }

		return "/sellcenter/shop/shopIndex";
	}
	
	@RequestMapping("toEdit")
	public String toEdit(HttpServletRequest request, Model model){
		Long shopId = WebUtil.getInstance().getShopId(request);
		//店铺信息
		ExecuteResult<ShopDTO> result = shopExportService.findShopInfoById(shopId);
		ShopDTO shopInfo = result.getResult();
		if(null==shopInfo.getInitialCondition()){//起批设置的条件设置一个默认值
			shopInfo.setInitialCondition(1);
		}
		if(null==shopInfo.getMutilCondition()){//混批条件设置一个默认值
			shopInfo.setMutilCondition(1);
		}
		model.addAttribute("shopInfo", result.getResult());
		model.addAttribute("auditStatus", getAuditStatus(shopId));
		
		return "/sellcenter/shop/shopBaseInfoEdit";
	}

	@RequestMapping("toView")
	public String toView(HttpServletRequest request, Model model){
		Long shopId = WebUtil.getInstance().getShopId(request);
		//店铺信息
		ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(shopId);
		//用户信息
		RegisterDTO userDto = userExportService.getUserByRedis(LoginToken.getLoginToken(request));
		userDto.setUserEmail(UserDtoUtils.hideUserEmail(userDto.getUserEmail()));
		userDto.setUserMobile(UserDtoUtils.hideUserCellPhone(userDto.getUserMobile()));
		//查询当前店铺的所有经营类目信息
		ExecuteResult<List<ShopCategoryDTO>> shopcategorylist = shopCategoryExportService.queryShopCategoryList(shopId, ShopCategoryStatus.PASS.getCode());
		Long[] scids = shopClientService.buildShopCategoryIds(shopcategorylist.getResult());
		ExecuteResult<List<ItemCatCascadeDTO>> cascadeCategoryList = itemCategoryService.queryParentCategoryList(scids);
		Map<String, String> categoryNameList = shopClientService.buildCategoryNameList(cascadeCategoryList.getResult());//组装一级、二级、三级类目的名称
		
		//根据品牌id查询品牌信息，并组装品牌logo的图片地址
		ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
		shopBrandDTO.setShopId(shopId);
        shopBrandDTO.setStatus(2);
		ExecuteResult<DataGrid<ShopBrandDTO>> shopBrandList = shopBrandExportService.queryShopBrandAll(shopBrandDTO, null);
		Long[] brandIds = shopClientService.buildBrandIds(shopBrandList);
		ExecuteResult<List<ItemBrandDTO>> brandlist = itemBrandExportService.queryItemBrandByIds(brandIds);
		
		//店铺评价信息
		ShopEvaluationQueryInDTO shopEvaluationQueryInDTO = new ShopEvaluationQueryInDTO();
		shopEvaluationQueryInDTO.setByShopId(shopId);
		ExecuteResult<ShopEvaluationTotalDTO> shopEvaluationTotal = shopEvaluationService.queryShopEvaluationTotal(shopEvaluationQueryInDTO);
		ShopEvaluationTotalDTO shopEvaluation = shopEvaluationTotal.getResult();
		model.addAttribute("shopEvaluationResult", shopEvaluation);
		
		model.addAttribute("shopBrandList", brandlist.getResult());	//当前店铺经营的所有品牌信息
		
		model.addAttribute("shopInfo", shopInfo.getResult());		
		model.addAttribute("userInfo", userDto);
		model.addAttribute("auditStatus", getAuditStatus(shopId));
		model.addAttribute("categoryNameList", categoryNameList);	//当前店铺正在经营的类目名称集合
		return "/sellcenter/shop/shopBaseInfoView";
	}
	
	/**
	 * 获取基本信息审核状态
	 * 申请所处状态 :-1还未申请变更过; 0, 新建(待审核); 1, 审核通过 2, 审核不通过;
	 */
	private Integer getAuditStatus(Long shopId) {
		ExecuteResult<List<ShopModifyInfoDTO>> shopmodify = shopModifyInfoExportService.queryShopModifyInfoById(shopId);
		List<ShopModifyInfoDTO> list = shopmodify.getResult();
		if(list==null || list.size()==0){//还未申请变更过，返回-1
			return -1;
		}
		ShopModifyInfoDTO dto = list.get(0);
		return dto.getApplyStatus();
	}
	
	/**判断页签加载内容*/
	@RequestMapping("judgeForward")
	@ResponseBody
	public Map<String, Object> judgeForward(Integer tabIndex, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		Long shopId = WebUtil.getInstance().getShopId(request);
		if(0==tabIndex){//基本信息
			map.put("loadurl", "/shopBaseInfoController/toEdit");
		}else if(1==tabIndex){//经营类目
			//正在审核的店铺类目信息
			ExecuteResult<List<ShopCategoryDTO>> shopcategorylist = shopCategoryExportService.queryShopCategoryList(shopId, ShopCategoryStatus.APPLY.getCode());
			List<ShopCategoryDTO> list = shopcategorylist.getResult();
			if(list!=null && list.size()>0){//存在新申请的数据：跳转到类目查看页面
				map.put("loadurl", "/shopManageCategoryController/toView");
			}else{//跳转到品牌编辑页面
				map.put("loadurl", "/shopManageCategoryController/toEdit");
			}
		}else if(2==tabIndex){//经营品牌
			//正在审核的店铺品牌信息
			ExecuteResult<List<ShopBrandDTO>> result = shopBrandExportService.queryShopBrandList(shopId, 1);//查询该店铺新申请的数据
			List<ShopBrandDTO> list = result.getResult();
			if(list!=null && list.size()>0){//存在新申请的数据：跳转到品牌查看页面
				map.put("loadurl", "/shopManageBrandController/toView");
			}else{//跳转到品牌编辑页面
				map.put("loadurl", "/shopManageBrandController/toEdit");
			}
		}
		return map;
	}
	
	/**查询省市区*/
	@ResponseBody
	@RequestMapping("/queryAddress")
	public List<AddressBase> queryAddress(String parentCode,Model model) {
		List<AddressBase> addressList = addressBaseService.queryAddressBase(parentCode);
		return addressList;
	}
	
	/**关闭店铺*/
	@ResponseBody
	@RequestMapping("closeShop")
	public Map<String, Object> closeShop(HttpServletRequest request){
		//1、关闭店铺
		Map<String,Object> result = new HashMap<String,Object>();
		Long shopId = WebUtil.getInstance().getShopId(request);
		ExecuteResult<String> shopStatusResult = shopExportService.modifyShopRunStatus(shopId, 2);
		//2、下架该店铺所有商品
		ItemStatusModifyDTO itemStatusDTO = new ItemStatusModifyDTO();
		itemStatusDTO.setShopId(shopId);
		itemStatusDTO.setItemStatus(5);
		ExecuteResult<String> itemStatusResult = itemExportService.modifyShopItemStatus(itemStatusDTO);
		
		if(shopStatusResult.isSuccess() && itemStatusResult.isSuccess()){
			result.put("result", "success");
		}else{
			result.put("result", "failure");
		}
		return result;
	}
	
	/**开启店铺*/
	@ResponseBody
	@RequestMapping("openShop")
	public Map<String, Object> openShop(HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
        result.put("result", "failure");
        Long shopId = WebUtil.getInstance().getShopId(request);

        //店铺信息
        ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(shopId);
        ShopDTO origShopDTO = shopInfo.getResult();
        if(null==origShopDTO){
            return result;
        }
        if(origShopDTO.getStatus()==4){
            result.put("message", "平台已关闭店铺，请先向平台申请开通！");
            return result;
        }
		ExecuteResult<String> retVal = shopExportService.modifyShopRunStatus(shopId, 1);
		if(retVal.isSuccess()){
			result.put("result", "success");
		}
		return result;
	}
	
	/**修改店铺信息*/
	@ResponseBody
	@RequestMapping("/modifyShopInfo")
	public Map<String, Object> modifyShopInfo(ShopDTO shopDTO, HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", "failure");
		if(shopDTO.getShopId()==null){
			return result;
		}
		//店铺信息
		ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(shopDTO.getShopId());
		ShopDTO origShopDTO = shopInfo.getResult();
		if(null==origShopDTO){
			return result;
		}
		origShopDTO.setShopName(shopDTO.getShopName());	//店铺名称
		origShopDTO.setLogoUrl(shopDTO.getLogoUrl());//店铺logo地址
		origShopDTO.setKeyword(shopDTO.getKeyword());//关键字
		origShopDTO.setIntroduce(shopDTO.getIntroduce());//店铺简介
		origShopDTO.setMainSell(shopDTO.getMainSell());//店铺主营
        if(shopDTO.getInitialMount()!=null && shopDTO.getInitialMount()>0){
            origShopDTO.setInitialMount(shopDTO.getInitialMount());//起批数量
        }
		origShopDTO.setInitialPrice(shopDTO.getInitialPrice());// 起批价格
		origShopDTO.setMountMin(shopDTO.getMountMin());// 批混数量要求
		origShopDTO.setMutilCondition(shopDTO.getMutilCondition());	//混批条件
		if(shopDTO.getMutilPrice()==null){
			origShopDTO.setMutilPrice(2);//是否混批（1为混批，2为不混批）
		}else{
			origShopDTO.setMutilPrice(1);//是否混批（1为混批，2为不混批）
		}
        if(shopDTO.getInitialCondition()==null){
            origShopDTO.setInitialCondition(1);// 起批条件：默认为1，代表或
        }else{
            origShopDTO.setInitialCondition(shopDTO.getInitialCondition());// 起批条件，1为或，2为且
        }
		origShopDTO.setPriceMin(shopDTO.getPriceMin());	//混批金额要求
		origShopDTO.setProvinceCode(shopDTO.getProvinceCode());//省份代码
		origShopDTO.setProvinceName(shopDTO.getProvinceName());//省份名字
		origShopDTO.setCityCode(shopDTO.getCityCode());//市的代码
		origShopDTO.setCityName(shopDTO.getCityName());//市的名字
		origShopDTO.setDistrictCode(shopDTO.getDistrictCode());//区的代码
		origShopDTO.setDistrictName(shopDTO.getDistrictName());//区的名字
		origShopDTO.setStreetName(shopDTO.getStreetName());//街道地址
		origShopDTO.setZcode(shopDTO.getZcode());//邮编
		origShopDTO.setMobile(shopDTO.getMobile());//手机号码
		origShopDTO.setAreaCode(shopDTO.getAreaCode());//座机区号
		origShopDTO.setExtensionNumber(shopDTO.getExtensionNumber());//分机号码
		origShopDTO.setLandline(shopDTO.getLandline());//座机号码
		origShopDTO.setLinkMan1(shopDTO.getLinkMan1());//销售负责人
		origShopDTO.setLinkPhoneNum1(shopDTO.getLinkPhoneNum1());//销售负责人电话
		origShopDTO.setLinkMan2(shopDTO.getLinkMan2());//联系人二
		origShopDTO.setLinkPhoneNum2(shopDTO.getLinkPhoneNum2());//联系人二电话
		origShopDTO.setDirectSale(shopDTO.getDirectSale());//是否直营
		origShopDTO.setRemark(shopDTO.getRemark());//备注
		origShopDTO.setModified(new Date());		//更新时间
		origShopDTO.setModifyStatus(1);				//店铺状态设置为待审核
		origShopDTO.setStatus(1);  //设置状态为申请
        origShopDTO.setShopType(shopDTO.getShopType());//店铺类型 1 品牌商 2经销商
        origShopDTO.setBrandType(shopDTO.getBrandType());//品牌类型  1 国内品牌 2国际品牌
        origShopDTO.setBusinessType(shopDTO.getBusinessType());//经营类型  1自有品牌 2 代理品牌
        origShopDTO.setDisclaimer(shopDTO.getDisclaimer());//免责声明
        origShopDTO.setTrademarkRegistCert(shopDTO.getTrademarkRegistCert());//商标注册证/商品注册申请书扫描件
        origShopDTO.setInspectionReport(shopDTO.getInspectionReport());//质检、检疫、检验报告/报关单类扫描件
        origShopDTO.setProductionLicense(shopDTO.getProductionLicense());//卫生/生产许可证扫描件
        origShopDTO.setMarketingAuth(shopDTO.getMarketingAuth());//销售授权书扫描件
		
		ShopInfoModifyInDTO shopInfoModifyInDTO = new ShopInfoModifyInDTO();
		shopInfoModifyInDTO.setShopDTO(origShopDTO);
		String s = JSON.toJSONString(shopInfoModifyInDTO);
        LOGGER.info("===============shopInfo==============");
        LOGGER.info(s);
        LOGGER.info("===============shopInfo==============");
		//修改店铺信息
		ExecuteResult<String> retVal = shopExportService.modifyShopInfo(shopInfoModifyInDTO);
		
		if(retVal.isSuccess()){
			result.put("result", "success");
		}else{
			LOGGER.error(retVal.getErrorMessages().get(0));
		}
		return result;
	}

    @RequestMapping("/message")
    public String domainNotFound(HttpServletRequest request, Model model){
        model.addAttribute("message", "您所访问的店铺不存在！");
        model.addAttribute("indexUrl", "indexUrl");
        return "/message";
    }
}
