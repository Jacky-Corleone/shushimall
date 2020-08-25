package com.camelot.mall.sellcenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.util.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopDeliveryTypeDTO;
import com.camelot.storecenter.dto.ShopFreightTemplateDTO;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;
import com.camelot.storecenter.dto.emums.ShopFreightTemplateEnum;
import com.camelot.storecenter.service.ShopDeliveryTypeService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFreightTemplateService;
import com.camelot.storecenter.service.ShopPreferentialWayService;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [运费模板维护]</p>
 * Created on 2015年10月27日
 *
 * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/storecenter/freightTemplate")
public class ShopFreightTemplateController {
    private final static Logger logger= LoggerFactory.getLogger(ShopFreightTemplateController.class);
    @Resource
    private ShopFreightTemplateService shopFreightTemplateService;
    @Resource
    private AddressBaseService addressBaseService;
    @Resource
    private ShopDeliveryTypeService shopDeliveryTypeService;
    @Resource
    private ShopPreferentialWayService shopPreferentialWayService;
    @Resource
    private ItemExportService itemExportService;
    @Resource
	private ShopExportService shopExportService;
    @RequestMapping("queryFreight")
	public String freightTemplateList(Integer page, HttpServletRequest request, Model model, ShopFreightTemplateDTO dto) {
		Long shopId = WebUtil.getInstance().getShopId(request);
		if (shopId == null) {
			return "redirect:/user/login";
		}
		dto.setDelState("1");
		dto.setShopId(shopId);
		Pager<ShopFreightTemplateDTO> pager = new Pager<ShopFreightTemplateDTO>();
		if (null != page) {
			pager.setPage(page);
		}
		ExecuteResult<DataGrid<ShopFreightTemplateDTO>> listFreight = shopFreightTemplateService.queryShopFreightTemplateList(dto, pager);
		List<ShopFreightTemplateDTO> listFreightTemplate = new ArrayList<ShopFreightTemplateDTO>();
		for (ShopFreightTemplateDTO shopFreightTemplate : listFreight.getResult().getRows()) {
			// 运送方式
			if (shopFreightTemplate.getDeliveryType() != null) {
				String deliveryTypes = "";
				String[] str = shopFreightTemplate.getDeliveryType().toString().split(",");
				for (int i = 0; i < str.length; i++) {
					if (Long.parseLong(str[i]) == ShopFreightTemplateEnum.ShopPreferentialWayEnum.EXPRESSAGE.getCode()) {
						deliveryTypes += ShopFreightTemplateEnum.ShopPreferentialWayEnum.EXPRESSAGE.getLabel() + ",";
					}
					if (Long.parseLong(str[i]) == ShopFreightTemplateEnum.ShopPreferentialWayEnum.EMS.getCode()) {
						deliveryTypes += ShopFreightTemplateEnum.ShopPreferentialWayEnum.EMS.getLabel() + ",";
					}
					if (Long.parseLong(str[i]) == ShopFreightTemplateEnum.ShopPreferentialWayEnum.SURFEMAIL.getCode()) {
						deliveryTypes += ShopFreightTemplateEnum.ShopPreferentialWayEnum.SURFEMAIL.getLabel()+ ",";
					}
				}
				shopFreightTemplate.setDeliveryName(deliveryTypes);
			}

			// 计价方式
			if (shopFreightTemplate.getValuationWay() != null
					&& shopFreightTemplate.getValuationWay() == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.NUMBERS.getCode()) {
				shopFreightTemplate.setValuationWayName(ShopFreightTemplateEnum.ShopDeliveryTypeEnum.NUMBERS.getLabel());
			}
			if (shopFreightTemplate.getValuationWay() != null
					&& shopFreightTemplate.getValuationWay() == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.VOLUME.getCode()) {
				shopFreightTemplate.setValuationWayName(ShopFreightTemplateEnum.ShopDeliveryTypeEnum.VOLUME.getLabel());
			}
			if (shopFreightTemplate.getValuationWay() != null
					&& shopFreightTemplate.getValuationWay() == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.WEIGHT.getCode()) {
				shopFreightTemplate.setValuationWayName(ShopFreightTemplateEnum.ShopDeliveryTypeEnum.WEIGHT.getLabel());
			}
			listFreightTemplate.add(shopFreightTemplate);
		}
		pager.setRecords(listFreightTemplate);
		pager.setTotalCount(listFreight.getResult().getTotal().intValue());
		model.addAttribute("pager", pager);
		model.addAttribute("shopFreightTemplateDTO", dto);
		return "sellcenter/shop/freight/shop_freightIndex";
	}

    /**
     * 跳转到运费模板新增页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("addFreightTemplate")
	public String toaddFreightTemplate(HttpServletRequest request, Model model) {
    	Long shopId = WebUtil.getInstance().getShopId(request);
		if (shopId == null) {
			return "redirect:/user/login";
		}
		// 根据模板id获取运送方式信息当新增运送方式时增加一个JSONArray集合
		List<ShopDeliveryTypeDTO> newListDeliverType = new ArrayList<ShopDeliveryTypeDTO>();
		model = addOrUpdate(model, newListDeliverType);// 获取省份、发货时间、计价方式
		ShopFreightTemplateDTO dto = new ShopFreightTemplateDTO();
		model.addAttribute("freightTemplate", dto);
		model.addAttribute("shippingmethod", "");// 运送方式
		model.addAttribute("addEditType", "1");// addEditType 1新增，2修改
		ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(shopId);
		if(shopInfo.isSuccess()&&shopInfo.getResult()!=null){
			model.addAttribute("shopInfo", shopInfo.getResult());
		}
		return "sellcenter/shop/freight/shop_addFreightTemplate";
	}

    /**
     * 新增运费模板
     * @param dto
     * @param request
     * @param model
     * @param addEditType
     * @param full
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public Map<String,Object> save(ShopFreightTemplateDTO dto,HttpServletRequest request,Model model,String addEditType,String full){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        ShopFreightTemplateDTO templateDto=dto;
        try {
			Long sellerId = WebUtil.getInstance().getUserId(request); // 用户id（卖家id）
			Long shopId = WebUtil.getInstance().getShopId(request); // 店铺id
			dto.setSellerId(sellerId);
			dto.setShopId(shopId);
			dto.setDelState("1");
			if (addEditType != null && addEditType.equals("1")) {
				ExecuteResult<ShopFreightTemplateDTO> ex = shopFreightTemplateService.addShopFreightTemplate(dto);
				if (ex.isSuccess() && ex.getResult() != null && ex.getResult().getPostageFree() == 1) {
					ShopFreightTemplateDTO freightDto = ex.getResult();
					// 新增运送方式
					addDeliverType(freightDto);
					// 新增优惠方式
					List<ShopPreferentialWayDTO> preferentialWayDTOList = dto.getShopPreferentialWayList();
					for (ShopPreferentialWayDTO preferentialWay : preferentialWayDTOList) {
						if (!StringUtils.isEmpty(full) && preferentialWay.getStrategy() == 2) {
							preferentialWay.setFull(new BigDecimal(full));
						}
						preferentialWay.setSellerId(sellerId);
						preferentialWay.setShopId(shopId);
						preferentialWay.setDelState("1");
						preferentialWay.setTemplateId(freightDto.getId());
						if (preferentialWay.getDeliveryType() == null) {
							preferentialWay.setDelState("2");
						}
						shopPreferentialWayService.addShopPreferentialWay(preferentialWay);
					}
				}
            } else{
				dto.setUpdateTime(new Date());
				ExecuteResult<ShopFreightTemplateDTO> updatetemplate = shopFreightTemplateService.update(dto);
				if (updatetemplate.isSuccess()&&new Integer(1).equals(templateDto.getPostageFree())) {
					List<ShopDeliveryTypeDTO> listDeliveryType = templateDto.getShopDeliveryTypeList();
					for (ShopDeliveryTypeDTO listDelivery : listDeliveryType) {
						// 运送方式
						int i = 0;
						if (null == listDelivery.getDeliveryType()) {
							i = 1;
						} else {
							if (i != 1) {
								// 修改运送方式数据
								if (listDelivery.getId() != null) {
									if (templateDto.getDeliveryType().indexOf(
											String.valueOf(listDelivery.getDeliveryType())) != -1) {
										listDelivery.setUpdateTime(new Date());
										shopDeliveryTypeService.updateShopDeliveryType(listDelivery);
									} else {// 当运送模式选框去掉
										listDelivery.setUpdateTime(new Date());
										listDelivery.setDelState("2");
										shopDeliveryTypeService.updateShopDeliveryType(listDelivery);
									}
								} else {
									// 新增运送方式数据
									if (templateDto.getDeliveryType().indexOf(
											String.valueOf(listDelivery.getDeliveryType())) != -1) {
										listDelivery.setSellerId(sellerId);
										listDelivery.setShopId(shopId);
										listDelivery.setTemplateId(templateDto.getId());
										listDelivery.setDelState("1");
										shopDeliveryTypeService.addShopDeliveryType(listDelivery);
									}
								}
							}
						}
					}
					List<ShopPreferentialWayDTO> preferentialWayList = templateDto.getShopPreferentialWayList();
					for (ShopPreferentialWayDTO preferentialWay : preferentialWayList) {
						if(preferentialWay.getId()==null){//优惠方式为空
							ShopPreferentialWayDTO wayDto=new ShopPreferentialWayDTO();
							wayDto.setTemplateId(templateDto.getId());
							ExecuteResult<List<ShopPreferentialWayDTO>> res=shopPreferentialWayService.queryShopPreferentialWay(wayDto);
							ShopPreferentialWayDTO shopWay=res.getResult().get(0);
							preferentialWay.setId(shopWay.getId());
						}
						if(preferentialWay.getFull()==null){
							preferentialWay.setFull(new BigDecimal(0));
						}
						if(preferentialWay.getReduce()==null){
							preferentialWay.setReduce(new BigDecimal(0));
						}
						if (preferentialWay.getDeliveryType() == null) {// 优惠方式为空，将优惠方式更新为无效
							preferentialWay.setDeliveryType(0);
							preferentialWay.setDelState("2");
							preferentialWay.setStrategy(null);
						} else {
							preferentialWay.setDelState("1");
						}
						preferentialWay.setUpdateTime(new Date());
						if (StringUtils.isNotBlank(full) && preferentialWay.getStrategy() == 2) {
							preferentialWay.setReduce(new BigDecimal(0));
							preferentialWay.setFull(new BigDecimal(full));
						}
						shopPreferentialWayService.updateShopPreferentialWay(preferentialWay);
					}
				}
            }
            resultMap.put("result", true);
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("message", e.getMessage());
			e.printStackTrace();
		}
        return resultMap;
    }

    //新增和修改运送方式公共方法
	public void addDeliverType(ShopFreightTemplateDTO freightDto) {
		String param = freightDto.getDeliveryType();
		String[] strs = new String[] {};
		if (param != null) {
			strs = param.split(",");
		}
		// 运送方式
		List<ShopDeliveryTypeDTO> listDelivery = freightDto.getShopDeliveryTypeList();
		for (int i = 0; i < strs.length; i++) {
			for (ShopDeliveryTypeDTO listDe : listDelivery) {
				if (listDe.getDeliveryType() != null && Long.parseLong(strs[i]) == listDe.getDeliveryType()) {
					listDe.setSellerId(freightDto.getSellerId());
					listDe.setShopId(freightDto.getShopId());
					listDe.setTemplateId(freightDto.getId());
					listDe.setDelState("1");
					shopDeliveryTypeService.addShopDeliveryType(listDe);
				}
			}
		}
	}

    /**
     * 修改跳转到运费模板页
     * @param request
     * @param model
     */
    @RequestMapping("updateFreightTemplate")
	public String toUpdateFreightTemplate(HttpServletRequest request, Model model) {
		// 获取运费模板信息
		String id = request.getParameter("id");
		String doType=request.getParameter("doType");
		ShopFreightTemplateDTO freightTemplateDTO = shopFreightTemplateService.queryShopFreightTemplateById(Long.parseLong(id));
		if (freightTemplateDTO != null) {
			// 根据模板id获取优惠方式信息
			ShopPreferentialWayDTO shopPrefDTO = new ShopPreferentialWayDTO();
			shopPrefDTO.setTemplateId(freightTemplateDTO.getId());
			shopPrefDTO.setDelState("1");
			ExecuteResult<List<ShopPreferentialWayDTO>> preferentialWayList = shopPreferentialWayService.queryShopPreferentialWay(shopPrefDTO);
			Integer preferentialWay = null;// 优惠方式
			Integer strategy = null;// 策略
			BigDecimal full = null;// 满多少件/重量/体积
			BigDecimal reduce = null;// 减多少钱（元）
			Long wayId = null;// 优惠方式id
			if (preferentialWayList.getResult() != null && preferentialWayList.getResult().size() > 0) {
				preferentialWay = preferentialWayList.getResult().get(0).getDeliveryType();
				strategy = preferentialWayList.getResult().get(0).getStrategy();
				full = preferentialWayList.getResult().get(0).getFull();
				reduce = preferentialWayList.getResult().get(0).getReduce();
				wayId = preferentialWayList.getResult().get(0).getId();
			}
			ShopDeliveryTypeDTO shopDeliveryTypeDTO = new ShopDeliveryTypeDTO();
			shopDeliveryTypeDTO.setTemplateId(freightTemplateDTO.getId());
			shopDeliveryTypeDTO.setDelState("1");// 查找未删除的
			ExecuteResult<List<ShopDeliveryTypeDTO>> deliveryTypeResult = shopDeliveryTypeService.queryShopDeliveryType(shopDeliveryTypeDTO);
			model.addAttribute("valuationWay", freightTemplateDTO.getValuationWay());// 计价方式
			model.addAttribute("preferentialWay", preferentialWay);// 优惠方式多一个金额
			model.addAttribute("strategy", strategy);// 策略
			model.addAttribute("full", full);// 满多少件/重量/体积
			model.addAttribute("reduce", reduce);// 减多少钱（元）
			model.addAttribute("wayId", wayId);// 优惠方式id
			model = addOrUpdate(model, deliveryTypeResult.getResult());// 获取省份、发货时间、计价方式
			model.addAttribute("deliveryTime", freightTemplateDTO.getDeliveryTime());// 发货时间
			model.addAttribute("postageFree", freightTemplateDTO.getPostageFree());// 是否包邮
			model.addAttribute("valuationWay", freightTemplateDTO.getValuationWay());// 计价方式
			model.addAttribute("shippingmethod", freightTemplateDTO.getDeliveryType());// 运送方式
			model.addAttribute("freightTemplate", freightTemplateDTO);// 运费模板实体信息
			model.addAttribute("addEditType", "2");// addEditType 1新增，2修改
			model.addAttribute("deliveryTypeResultSize", deliveryTypeResult.getResult().size());// 运送方式数据数
			if(StringUtils.isNotBlank(doType)){
				model.addAttribute("doType", doType);
				// 复制模版
				if("copy".equals(doType)){
					freightTemplateDTO.setId(null);
					model.addAttribute("addEditType", "1");// 复制=新建
				}
			}
		}
		return "sellcenter/shop/freight/shop_addFreightTemplate";
	}

	public ShopDeliveryTypeDTO allJsonObject(Integer deliveryType) {
		ShopDeliveryTypeDTO shopDeliveryTypeDTO = new ShopDeliveryTypeDTO();
		shopDeliveryTypeDTO.setDeliveryTo("0");
		shopDeliveryTypeDTO.setDeliveryType(deliveryType);
		return shopDeliveryTypeDTO;
	}

    /**
     * 获取省份、发货时间、计价方式下拉框
     * @param model
     * @return
     */
    public Model addOrUpdate(Model model,List<ShopDeliveryTypeDTO> listDeliverType){
		// 获取省份
		List<AddressBase> addressBaseList = addressBaseService.queryAddressBase("0");
		model.addAttribute("addressList", addressBaseList);
		// 发货时间动态取值
		String deliveryTime = ShopFreightTemplateEnum.ShopDeliveryTimeEnum.DELIVERY.getLabel();
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		String[] strs = deliveryTime.split(",");
		for (String deliStr : strs) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("deliveryName", deliStr);
			listMap.add(maps);
		}
		model.addAttribute("listMap", listMap);
		// 计价方式
		ShopFreightTemplateEnum.ShopDeliveryTypeEnum[] shopDeliveryTypeEnum = ShopFreightTemplateEnum.ShopDeliveryTypeEnum.values();
		model.addAttribute("deliveryTypeEnum", shopDeliveryTypeEnum);
		// 运送方式
		ShopFreightTemplateEnum.ShopPreferentialWayEnum[] shopPreferentialWayEnum = ShopFreightTemplateEnum.ShopPreferentialWayEnum.values();
		model.addAttribute("shopPreferentialWayEnum", shopPreferentialWayEnum);
//        List<ShopDeliveryTypeDTO> newListDeliverType=new ArrayList<ShopDeliveryTypeDTO>();
        JSONArray jsonArray = new JSONArray();
		// 根据模板id获取运送方式信息
        if(listDeliverType!=null && listDeliverType.size()>0){
			String codeStr = "";
			for (ShopDeliveryTypeDTO listDeliver : listDeliverType) {
				codeStr += listDeliver.getDeliveryType() + ",";
				String[] codes=null;
				if(null!=listDeliver.getDeliveryTo()){
					codes=listDeliver.getDeliveryTo().split("、");
				}
				String[] DeliverNames=null;
				if(null!=listDeliver.getDeliveryAddress()){
					DeliverNames = listDeliver.getDeliveryAddress().split("、");
				}
				
				JSONArray jsonArray1 = new JSONArray();
				for (int i = 0; i < codes.length; i++) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", codes[i]);
					if(null!=DeliverNames){
						jsonObject.put("codeName", DeliverNames[i]);
					}
					jsonArray1.add(jsonObject);
				}
				JSONObject jsonObject1 = new JSONObject();
				jsonObject1.put("address", jsonArray1);
				jsonObject1.put("deliveryTypeResult", listDeliver);
				jsonArray.add(jsonObject1);
//                newListDeliverType.add(listDeliver);
//                Integer preferenWay=listDeliver.getDeliveryType();//运送方式id
//                Integer code=null;
//                for(ShopFreightTemplateEnum.ShopPreferentialWayEnum prefreWay:shopPreferentialWayEnum){
//                    code=prefreWay.getCode();//枚举code运送方式id
//                    if(code==preferenWay){
//                        break;
//                    }
//                }
            }
			for (ShopFreightTemplateEnum.ShopPreferentialWayEnum prefreWay : shopPreferentialWayEnum) {
				Integer code = prefreWay.getCode();// 枚举code运送方式id
				if (codeStr.indexOf(String.valueOf(prefreWay.getCode())) == -1) {
					// newListDeliverType.add(allJsonObject(code));
					JSONObject jsonObject1 = new JSONObject();
					jsonObject1.put("deliveryTypeResult", allJsonObject(code));
					jsonArray.add(jsonObject1);
				}
			}
		} else {
			for (ShopFreightTemplateEnum.ShopPreferentialWayEnum prefreWay : shopPreferentialWayEnum) {
				Integer code = prefreWay.getCode();// 枚举code运送方式id
				// newListDeliverType.add(allJsonObject(code));
				JSONObject jsonObject1 = new JSONObject();
				jsonObject1.put("deliveryTypeResult", allJsonObject(code));
				jsonArray.add(jsonObject1);
			}
		}
        model.addAttribute("jsonArray",jsonArray);
        return model;
    }

    /**
     * 删除运费模板（商品）
     * item_status：商品状态。1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁， 30： 删除
     * 只删状态为1和5
     * @param request
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
	public Map<String, Object> deleteFreightTemplate(HttpServletRequest request) {
		String temIds = request.getParameter("templateId");
		Long templateId = Long.parseLong(temIds);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
		itemQueryInDTO.setShopFreightTemplateId(templateId);
		// 未删除
		List<Integer> itemStatusList = new ArrayList<Integer>();
		itemStatusList.add(ItemStatusEnum.NOT_PUBLISH.getCode());
		itemStatusList.add(ItemStatusEnum.AUDITING.getCode());
		itemStatusList.add(ItemStatusEnum.SHELVING.getCode());
		itemStatusList.add(ItemStatusEnum.SALING.getCode());
		itemStatusList.add(ItemStatusEnum.UNSHELVED.getCode());
		itemStatusList.add(ItemStatusEnum.LOCKED.getCode());
		itemStatusList.add(ItemStatusEnum.APPLYING.getCode());
		itemStatusList.add(ItemStatusEnum.REJECTED.getCode());
		itemQueryInDTO.setItemStatusList(itemStatusList);
		DataGrid<ItemQueryOutDTO> itemQueryOutList = itemExportService.queryItemList(itemQueryInDTO, null);
		try {
			if (itemQueryOutList.getRows() == null || itemQueryOutList.getRows().size() == 0) {
				ShopFreightTemplateDTO shopTemplate = new ShopFreightTemplateDTO();
				shopTemplate.setId(templateId);
				shopTemplate.setDelState("2");
				ExecuteResult<ShopFreightTemplateDTO> exTemplate = shopFreightTemplateService.update(shopTemplate);
				if (exTemplate.isSuccess()) {
					ShopDeliveryTypeDTO deliveryType = new ShopDeliveryTypeDTO();
					deliveryType.setTemplateId(templateId);
					deliveryType.setDelState("2");
					shopDeliveryTypeService.updateShopDeliveryType(deliveryType);
					ShopPreferentialWayDTO preferentialWayDTO = new ShopPreferentialWayDTO();
					preferentialWayDTO.setTemplateId(templateId);
					preferentialWayDTO.setDelState("2");
					shopPreferentialWayService.updateShopPreferentialWay(preferentialWayDTO);
				}
				resultMap.put("result", true);
			} else {
				resultMap.put("message", "此模板正在被使用，无法删除！");
			}
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("message", e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}


    /**
     * 删除运送方式
     * @param request
     * @return
     */
    @RequestMapping("deleteDeliveryType")
    @ResponseBody
	public Map<String, Object> deleteDeliveryType(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String deliverId = request.getParameter("id");
			Long id = Long.parseLong(deliverId);
			ShopDeliveryTypeDTO shopDeliveryTypeDTO = new ShopDeliveryTypeDTO();
			shopDeliveryTypeDTO.setDelState("2");
			shopDeliveryTypeDTO.setId(id);
			ExecuteResult<String> ex = shopDeliveryTypeService.updateShopDeliveryType(shopDeliveryTypeDTO);
			if (ex.isSuccess()) {
				resultMap.put("result", true);
			}
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("message", e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
    
    /**
	 * 
	 * <p>Description: [根据运费模版ID查询]</p>
	 * Created on 2015年11月4日
	 * @param shopFreightTemplateId
	 * @param request
	 * @param response
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/queryById")
	@ResponseBody
	public ExecuteResult<ShopFreightTemplateDTO> queryById(Long shopFreightTemplateId, HttpServletRequest request,
			HttpServletResponse response) {
		ExecuteResult<ShopFreightTemplateDTO> er = shopFreightTemplateService.queryById(shopFreightTemplateId);
		return er;
	}
	/**
	 * 
	 * <p>Discription:[校验模板名称是否重复]</p>
	 * Created on 2015-11-6
	 * @param request
	 * @return
	 * @author:[王鹏]
	 */
	@ResponseBody
    @RequestMapping(value="checkName")
	public Json approveReject(HttpServletRequest request) {
		Long shopId = WebUtil.getInstance().getShopId(request);
		Json json = new Json();
		try {
			String templateName = request.getParameter("templateName");
			String name = "";
			String id = request.getParameter("id");
			if (StringUtils.isNotBlank(id)) {
				ExecuteResult<ShopFreightTemplateDTO> res = shopFreightTemplateService.queryById(Long.parseLong(id));
				if (res.isSuccess() && res.getResult() != null) {
					name = res.getResult().getTemplateName();
				}
			}
			ShopFreightTemplateDTO dto = new ShopFreightTemplateDTO();
			dto.setTemplateName(templateName);// 模板名称
			dto.setShopId(shopId);
			dto.setDelState("1");// 未删除
			dto.setShopId(shopId);
			ExecuteResult<DataGrid<ShopFreightTemplateDTO>> listFreight = shopFreightTemplateService
					.queryShopFreightTemplateList(dto, null);
			if (!listFreight.getResult().getRows().isEmpty() || listFreight.getResult().getRows().size() != 0) {
				if (!templateName.equals(name)) {
					json.setMsg("模板名称已存在，为方便您的使用，建议您更改模板名称，是否继续保存？");
					json.setSuccess(true);
				}
			} else {
				json.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			json.setMsg("系统出现意外错误，请联系管理员");
			json.setSuccess(false);
		}
		return json;
	}
}
