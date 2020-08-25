package com.camelot.mall.requestPrice;

import java.math.BigDecimal;
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.goodscenter.dto.InquiryMatDTO;
import com.camelot.goodscenter.dto.InquiryOrderDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.requestPriceService.RequestPriceService;
import com.camelot.mall.shopcart.Product;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.searchcenter.dto.SearchBrand;
import com.camelot.searchcenter.dto.SearchCategory;
import com.camelot.searchcenter.dto.SearchItemAttr;
import com.camelot.searchcenter.dto.SearchItemSku;
import com.camelot.searchcenter.dto.SearchItemSkuInDTO;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.searchcenter.service.SearchExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.StationUtil;
import com.camelot.util.WebUtil;


//询价类
@Controller
@RequestMapping("/requestPriceJavaController")
public class RequestPriceJavaController {
	@Resource
	private InquiryExportService inquiryExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private SearchExportService searchExportService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private RequestPriceService requestPriceService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private RedisDB redisDB;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	/**
	 * 创建询价单信息
	 * @author chengwt 创建时间：下午2:26:12
	 * @param request
	 */
	    @ResponseBody
	    @RequestMapping("/addInquiry")
		public String addInquiry(InquiryInfoDTO inquiryInfoDTO , String beginDate1, String endDate1 , String deliveryDate1,String deleteId,  String[] detailId,String[] shopId , String[] nums, String[] flag , String[] deliveryDates,String[] skuId, HttpServletRequest request ){
	    	String message = "success";
	    	Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO = userExportService.queryUserById(userId);
	    	//将获取到的前台的
	    	inquiryInfoDTO.setStatus("1");
	    	inquiryInfoDTO.setActiveFlag("1");
	    	inquiryInfoDTO.setCreateBy(""+userId);
	    	inquiryInfoDTO.setCreateDate(new Date());
	    	SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	try {
				inquiryInfoDTO.setBeginDate(sp.parse(beginDate1));
				inquiryInfoDTO.setEndDate(sp.parse(endDate1));
				inquiryInfoDTO.setDeliveryDate(sp.parse(deliveryDate1));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "保存失败！时间转换错误";
			}
	    	Map<String, String> resultMap = new HashMap<String, String>();
	    	resultMap = requestPriceService.addInquiryNew(inquiryInfoDTO, ""+userId, deleteId, detailId, shopId, nums, flag, deliveryDates, skuId);
	    	return resultMap.get("result");
		}
	    
	    
	    /**
		 * 立即发布 Status为2
		 * @author zhm 创建时间：2015年11月2日11:49:48
		 * @param request
		 */
		    @ResponseBody
		    @RequestMapping("/immediately")
			public String immediately(InquiryInfoDTO inquiryInfoDTO , String beginDate1, String endDate1 , String deliveryDate1,String deleteId,  String[] detailId,String[] shopId , String[] nums, String[] flag , String[] deliveryDates, HttpServletRequest request ){
		    	String message = "success";
		    	Long userId = WebUtil.getInstance().getUserId(request);
				UserDTO userDTO = userExportService.queryUserById(userId);
		    	//将获取到的前台的
		    	inquiryInfoDTO.setStatus("1");
		    	inquiryInfoDTO.setActiveFlag("1");
		    	inquiryInfoDTO.setCreateBy(""+userId);
		    	inquiryInfoDTO.setCreateDate(new Date());
		    	SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	try {
					inquiryInfoDTO.setBeginDate(sp.parse(beginDate1));
					inquiryInfoDTO.setEndDate(sp.parse(endDate1));
					inquiryInfoDTO.setDeliveryDate(sp.parse(deliveryDate1));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "保存失败！时间转换错误";
				}
		    	Map<String, String> resultMap = new HashMap<String, String>();
		    	
		    	resultMap = requestPriceService.upimmediately(inquiryInfoDTO, ""+userId, deleteId, detailId, shopId, nums, flag, deliveryDates);
		    	InquiryInfoDTO dto = new InquiryInfoDTO();
				requestPriceService.commitInquiry(inquiryInfoDTO.getInquiryNo(), "" + userId, dto);
				//将结果放入request中，仅供站内信发送拦截器使用
				ExecuteResult<String> result = new ExecuteResult<String>();
				request.setAttribute("inquiryName", inquiryInfoDTO.getInquiryName());
				request.setAttribute("shopIds", shopId);
				request.setAttribute("result", result);
				request.setAttribute("releaseResult", resultMap.get("result"));
				request.setAttribute("userName", userDTO.getUname());
				request.setAttribute("userId", userId);
		    	return resultMap.get("result");
			}
	    
	    

	    @RequestMapping(value = "/searchItem")
		public String searchItem(Long brandId, String attributes, Long cid, String areaCode, Integer page,Integer orderSort, String keyword, Model model, HttpServletRequest request) {
			if(StringUtils.isNotBlank(keyword) || null != cid){
				SearchItemSkuInDTO inDto = new SearchItemSkuInDTO();
				String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
				Long uid = WebUtil.getInstance().getUserId(request);

				if(uid != null)
					inDto.setBuyerId(uid);

				inDto.setAreaCode(region);
				if(StringUtils.isNotEmpty(keyword)){
					inDto.setKeyword(keyword);
				}
				if(null != brandId){
					List<Long> brandIds = new ArrayList<Long>();
					brandIds.add(brandId);
					inDto.setBrandIds(brandIds);
				}
				if(StringUtils.isNotEmpty(attributes)){
					inDto.setAttributes(attributes);
				}
				if(null != cid){
					inDto.setCid(cid);
				}
				if(StringUtils.isNotEmpty(areaCode)){
					inDto.setAreaCode(areaCode);
				}
				Pager<SearchItemSkuOutDTO> pager_SearchItem = new Pager<SearchItemSkuOutDTO>();
				pager_SearchItem.setRows(28);
				if(null != page){
					pager_SearchItem.setPage(page);
				}
				if(orderSort != null){
					//1 时间升序 2时间降序
					inDto.setOrderSort(orderSort);
				}else{
					//默认降序排序
					inDto.setOrderSort(2);
				}
				inDto.setPager(pager_SearchItem);





			//	LOG.debug("inDto====="+JSON.toJSONString(inDto));
				SearchItemSkuOutDTO outDto = searchExportService.searchItemSku(inDto);
				DataGrid<SearchItemSku> itemSkus = outDto.getItemSkus();//商品列表
				pager_SearchItem.setTotalCount(itemSkus.getTotal().intValue());


				List<SearchBrand> brandList = outDto.getBrands();//品牌
				List<SearchItemAttr> attrList = outDto.getAttributes();//商品非销售属性
				List<SearchCategory> categories = outDto.getCategories();//商品类别
				model.addAttribute("pager", pager_SearchItem);

				//shopId
				List<SearchItemSku> listSearchItemSku = itemSkus.getRows();
				model.addAttribute("itemSkus", listSearchItemSku);
				if(itemSkus.getRows()!=null && listSearchItemSku.size()>0){
					model.addAttribute("itemSkus_isNull", "false");
				}else{
					model.addAttribute("itemSkus_isNull", "true");
				}
				//读取店铺名称
				Map<Long, String> map = new HashMap<Long, String>();
				if(itemSkus.getRows()!=null && listSearchItemSku.size()>0){
					for(SearchItemSku searchItemSku : listSearchItemSku){
						String valueShopName = map.get(searchItemSku.getShopId());
						if(StringUtils.isEmpty(valueShopName)){
							ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(searchItemSku.getShopId());
							if(shopInfo!=null && shopInfo.getResult()!=null){
								map.put(shopInfo.getResult().getShopId(), shopInfo.getResult().getShopName());
							}
						}

					}
				}
				//构造店铺数据的json
				JSONArray jsonArray = new JSONArray();
				for (Map.Entry<Long, String> entry : map.entrySet()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("shopId", entry.getKey());
					jsonObject.put("shopName", entry.getValue());
					//获取客服站点
					jsonObject.put("stationId", StationUtil.getStationIdByShopId(entry.getKey()));
					jsonArray.add(jsonObject);
				}
				model.addAttribute("brandList", brandList);
				model.addAttribute("attrList", attrList);
				model.addAttribute("categories", categories);
				model.addAttribute("jsonArrayShop", jsonArray);
				model.addAttribute("keyword", keyword);
				model.addAttribute("brandId",brandId);
				model.addAttribute("attributes",attributes);
				model.addAttribute("cid",cid);
				model.addAttribute("orderSort",inDto.getOrderSort());

//				LOG.debug("pager====="+JSON.toJSONString(pager_SearchItem));
//				LOG.debug("itemSkus====="+JSON.toJSONString(itemSkus.getRows()));
//				LOG.debug("brandList====="+JSON.toJSONString(brandList));
//				LOG.debug("attrList====="+JSON.toJSONString(attrList));
//				LOG.debug("categories====="+JSON.toJSONString(categories));
//				LOG.debug("jsonArrayShop====="+JSON.toJSONString(jsonArray));
			}else{
				model.addAttribute("pager", null);
				model.addAttribute("itemSkus", null);
				model.addAttribute("brandList", null);
				model.addAttribute("attrList", null);
				model.addAttribute("categories", null);
				model.addAttribute("jsonArrayShop", null);
				model.addAttribute("keyword", null);
				model.addAttribute("brandId",null);
				model.addAttribute("attributes",null);
				model.addAttribute("cid",null);
				model.addAttribute("orderSort",null);
			}
			return "/requestPrice/goodsListPage";
		}
	    
	    /**
	    	 * 获取询价明细信息,订单明细信息
	    	 * @author chengwt 创建时间：下午5:42:50
	    	 * @param request
	    	 */
	    @RequestMapping(value = "/getDetaiInfo")
	    @ResponseBody
		public String getDetaiInfo( HttpServletRequest request) {
			String inquiryNo = request.getParameter("inquiryNo");
			String flag = request.getParameter("flag");
			InquiryInfoDTO dto = new InquiryInfoDTO();
			InquiryOrderDTO orderDTO = new InquiryOrderDTO();
			dto.setInquiryNo(inquiryNo);
			dto.setActiveFlag("1");
			orderDTO.setInquiryNo(inquiryNo);
			orderDTO.setActiveFlag("0");
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			//
			ExecuteResult<DataGrid<InquiryInfoDTO>>  dtoInfo = inquiryExportService.queryInquiryInfoList(dto,pager);
			//查询询价明细
			ExecuteResult<DataGrid<Map>>  resultDetail = new ExecuteResult<DataGrid<Map>>();
			//=======old start
			if("1".equals(dtoInfo.getResult().getRows().get(0).getStatus())){
				resultDetail = inquiryExportService.queryInquiryInfoPager(dto, pager);
			}else{
				InquiryMatDTO matDTO = new InquiryMatDTO();
				matDTO.setInquiryNo(inquiryNo);
				matDTO.setActiveFlag("1");
				//区分买家卖家标识，卖家只能看自己的报价信息
				if(!"request".equals(flag)) {
					Long userId = WebUtil.getInstance().getUserId(request);
					UserDTO userDTO = userExportService.queryUserById(userId);
					matDTO.setShopId(userDTO.getShopId().intValue());
				}
				resultDetail = inquiryExportService.queryInquiryMatList(matDTO, pager);
			}
			//=======old start
			//=======new start
//			//区分买家卖家标识，卖家只能看自己的报价信息
//			if(!"request".equals(flag)) {
//				Long userId = WebUtil.getInstance().getUserId(request);
//				UserDTO userDTO = userExportService.queryUserById(userId);
//				dto.setSupplierId(userDTO.getShopId().intValue());
//			}
//			resultDetail = inquiryExportService.queryInquiryInfoPager(dto, pager);
			//=======new end
			//查询订单明细
			ExecuteResult<DataGrid<InquiryOrderDTO>>  resultOrder = inquiryExportService.queryInquiryOrderList(orderDTO, pager);
			List<InquiryMatDTO> inquiryMatDTOs = new ArrayList<InquiryMatDTO>();
			String result1 = "";
			String result2 = "";
			int i = 0;
			SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
			//取出map放入list中，前台展示用
			if(resultDetail.isSuccess()){
				for(Map dtoItems : resultDetail.getResult().getRows()){
					i += 1;
					String ifPrice = "0";
					if(dtoItems.get("matPrice") != null && !"".equals(dtoItems.get("matPrice")) && !"null".equals(dtoItems.get("matPrice"))){
						ifPrice = "1";
					}
					result1 = result1 + "<tr><td class='border-1 wid_50 font_cen'><input type='checkbox' onclick='ischeckAll()' name='itemDetail' value='"+dtoItems.get("id")+"' checked/>"
							+ "<input type='hidden' name='ifPrice' value='"+ifPrice+"' />"
							+ "<input type='hidden' name='detailName' value='"+dtoItems.get("itemName")+"'/><input type='hidden' name='detailStatus' value='"+dtoItems.get("status")+"' />";
					if(dtoItems.get("beginDate") != null && !"".equals(dtoItems.get("beginDate")) && !"null".equals(dtoItems.get("beginDate"))) {
						result1 = result1 +"<input type='hidden' name='detailBeginDate' value='" + dtoItems.get("beginDate").toString().split(" ")[0] + "' />"
								+"<input type='hidden' name='detailEndDate' value='" + dtoItems.get("endDate").toString().split(" ")[0] + "' />";
					}else{
						result1 = result1 +"<input type='hidden' name='detailBeginDate' value='' />"
								+"<input type='hidden' name='detailEndDate' value='' />";
					}
					result1 = result1 +"</td>";
					//拼接前台的html
						//查询供应商名称
					if(dtoItems.get("shopId") != null && !"".equals(dtoItems.get("shopId")) && !"null".equals(dtoItems.get("shopId"))  ){
						ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+dtoItems.get("shopId")));
						result1 = result1 + "<td class='border-1 wid_110 font_cen' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+shop.getResult().getShopName()+"'>"+shop.getResult().getShopName()+" </td>";
					}else{
						ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(Long.parseLong(""+dtoItems.get("supplierId")));
						result1 = result1 + "<td class='border-1 wid_110 font_cen' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+shop.getResult().getShopName()+"'>"+shop.getResult().getShopName()+" </td>";
					}
					result1 = result1 + "<td class='border-1 wid_110 font_cen' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+dtoItems.get("itemName")+"'>"+dtoItems.get("itemName")+"</td>";
					
					//供应商id 
					if(dtoItems.get("supplierId") != null && !"".equals(dtoItems.get("supplierId")) && !"null".equals(dtoItems.get("supplierId"))){
					    ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
					    itemInDTO.setItemId(Integer.parseInt(dtoItems.get("matCd").toString()));
					    itemInDTO.setItemName(dtoItems.get("itemName").toString());
					    itemInDTO.setSkuId(Long.parseLong(dtoItems.get("skuId").toString()));
					    DataGrid<ItemQueryOutDTO> s = itemExportService.queryItemList(itemInDTO, pager);
					    if(s.getRows()!= null){
						dtoItems.put("shopId",s.getRows().get(0).getShopId());
					    }
					}
					
					
					if(dtoItems.get("shopId") != null && !"".equals(dtoItems.get("shopId")) && !"null".equals(dtoItems.get("shopId"))){
						//获取销售属性
						Map<String, String> mapAttr = new HashMap<String, String>();
						mapAttr.put("shopId","" + dtoItems.get("shopId"));
						if(dtoItems.get("itemId").toString().equals(dtoItems.get("matCd").toString())){
							mapAttr.put("skuId","" + dtoItems.get("skuId"));
						}else{
							mapAttr.put("skuId","" + dtoItems.get("matCd"));
						}
						mapAttr.put("itemId","" + dtoItems.get("itemId"));
						mapAttr = putSalerAttr(mapAttr);
						result1 = result1 + "<td class='border-1 wid_110 font_cen' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+mapAttr.get("salerAttr")+"'>"+mapAttr.get("salerAttr")+"</td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_110 font_cen' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'></td>";
					}
					if(dtoItems.get("deliveryDate2") == null || "null".equals(dtoItems.get("deliveryDate2"))){
						result1 = result1 + "<td class='border-1 wid_110'> </td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_110 font_cen'>"+dtoItems.get("deliveryDate2").toString().split(" ")[0]+" </td>";
					}
					if(dtoItems.get("beginDate") == null || "null".equals(dtoItems.get("beginDate"))){
						result1 = result1 + "<td class='border-1 wid_110'> </td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_110 font_cen'>"+dtoItems.get("beginDate").toString().split(" ")[0]+" </td>";
					}
					if(dtoItems.get("endDate") == null || "null".equals(dtoItems.get("endDate"))){
						result1 = result1 + "<td class='border-1 wid_110'> </td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_110 font_cen'>"+dtoItems.get("endDate").toString().split(" ")[0]+" </td>";
					}
					if(dtoItems.get("shopId") != null && !"".equals(dtoItems.get("shopId")) && !"null".equals(dtoItems.get("shopId")) && "1".equals(dtoItems.get("status"))){
						result1 = result1 + "<td class='border-1 wid_80 font_cen'>已接受</td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_80 font_cen'>未接受</td>";
					}
					if("request".equals(flag)) {
						result1 = result1 + "<td class='border-1 wid_80 font_cen'><input name='number' value=" + dtoItems.get("quantity") + " onkeypress='number()' onkeyup='filterInput()' onchange='filterInput()' onbeforepaste='filterPaste()' onpaste='return false' class='input_Style2 wid_50'/></td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_80 font_cen'>" + dtoItems.get("quantity") + "</td>";
					}
					if("null".equals(""+dtoItems.get("matPrice"))){
						result1 = result1 + "<td class='border-1 wid_50'> </td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_50 font_cen'> "+dtoItems.get("matPrice")+"</td>";
					}
					result1 = result1 + "</tr>";
				}
			}
			//取出map放入list中，前台展示用
			if(resultOrder.isSuccess()){
				i = 0;
			
				for(InquiryOrderDTO orderList : resultOrder.getResult().getRows()){
					//根据订单号查询订单信息显示订单详细信息
					TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
					tradeOrdersQueryInDTO.setOrderId(orderList.getOrderNo());
					ExecuteResult<DataGrid<TradeOrdersDTO>> orderResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
					if(orderResult.isSuccess()) {
						for(TradeOrdersDTO order : orderResult.getResult().getRows()) {
							//拼接前台的html
							result2 = result2 + "<tr>";
							result2 = result2 + "<td class='border-1 wid_110 font_cen'>" + inquiryNo + "</td>";
							result2 = result2 + "<td class='border-1 wid_210 font_cen'>" + order.getOrderId() + "</td>";
							if(order.getState().intValue() == 1) {
								result2 = result2 + "<td class='border-1 wid_110 font_cen'>待付款</td>";
							}else if(order.getState().intValue() == 2){
								result2 = result2 + "<td class='border-1 wid_110 font_cen'>待配送</td>";
							}else if(order.getState().intValue() == 3){
								result2 = result2 + "<td class='border-1 wid_110 font_cen'>待确认送货</td>";
							}else if(order.getState().intValue() == 4){
								result2 = result2 + "<td class='border-1 wid_110 font_cen'>待评价</td>";
							}else if(order.getState().intValue() == 5){
								result2 = result2 + "<td class='border-1 wid_110 font_cen'>已完成</td>";
							}else{
								result2 = result2 + "<td class='border-1 wid_110'> </td>";
							}
							if(order.getTotalPrice() != null){
								result2 = result2 + "<td class='border-1 wid_80 font_cen'>" + (order.getTotalPrice().doubleValue() + order.getFreight().doubleValue()) +"</td>";
							}else {
								result2 = result2 + "<td class='border-1 wid_80'> </td>";
							}
							if("request".equals(flag)) {
								result2 = result2 + "<td class='border-1 wid_80 font_cen'><a class='font_1c cursor' href='/mall-web/order/queryOrderInfoBuyer?orderId=" + order.getOrderId() + "' target='_blank'>订单详情</a></td>";
							}else{
								result2 = result2 + "<td class='border-1 wid_80 font_cen'><a class='font_1c cursor' href='/mall-web/order/queryOrderInfoSeller?orderId=" + order.getOrderId() + "' target='_blank'>订单详情</a></td>";
							}
							result2 = result2 + "</tr>";
						}
					}
				}
			}
			return result1+"%"+result2;
		}

		/**获取商品属性
		 * @param contractMats
		 * @return
		 */
		public Map<String, String> putSalerAttr(Map contractMats) {
			ItemShopCartDTO dto = new ItemShopCartDTO();
			dto.setAreaCode("0");
			dto.setShopId(Long.valueOf(contractMats.get("shopId").toString()));
			dto.setSkuId(Long.valueOf(contractMats.get("skuId").toString()));
			dto.setItemId(Long.valueOf(contractMats.get("itemId").toString()));
			ExecuteResult<ItemShopCartDTO> er = itemExportService.getSkuShopCart(dto); //调商品接口查url
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
	    
	    /**
		 *询价单删除，active字段置为0
		 * @author chengwt 创建时间：下午7:33:36
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/deleteInquiry")
	@ResponseBody
	public String deleteInquiry(Model model, HttpServletRequest request,
			HttpServletResponse response){
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String ids = request.getParameter("ids");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = requestPriceService.deleteInquiry(""+userId, ids);
		
		return resultMap.get("result");
	}
	
	/**
		 * 询价提交，生成询价单明细，status字段置为2
		 * @author chengwt 创建时间：上午9:45:34
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/commitInquiry")
	@ResponseBody
	public String commitInquiry(Model model, HttpServletRequest request,
			HttpServletResponse response){
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String ids = request.getParameter("ids");
		InquiryInfoDTO dto = new InquiryInfoDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = requestPriceService.commitInquiry(ids, "" + userId, dto);
		//将结果放入request中，仅供站内信发送拦截器使用
		ExecuteResult<String> result = new ExecuteResult<String>();
		request.setAttribute("result", result);
		request.setAttribute("ids",ids);
		request.setAttribute("releaseResult",resultMap.get("result"));
		request.setAttribute("userName",userDTO.getUname());
		request.setAttribute("userId",userId);
		return resultMap.get("result");
	}
	
	
	/**
		 * 卖家保存报价信息
		 * @author chengwt 创建时间：上午10:35:35
		 * @param request
		 */
	@ResponseBody
    @RequestMapping("/resopnseModifyInquiry")
	public String resopnseModifyInquiry(InquiryInfoDTO inquiryInfoDTO  , String[] id, String[] nums, String[] price, String[] detailstartDate , String[] detailendDate , HttpServletRequest request ){
		Map<String, String> resultMap = new HashMap<String, String>();
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		inquiryInfoDTO.setSupplierId(userDTO.getShopId().intValue());
		resultMap = requestPriceService.resopnseModifyInquiry(inquiryInfoDTO, "" + userId, id, nums, price, detailstartDate, detailendDate);
		ExecuteResult<String> result = new ExecuteResult<String>();
		request.setAttribute("result", result);
		request.setAttribute("updResult", resultMap.get("result"));
		request.setAttribute("userId", inquiryInfoDTO.getPrinterId());
		request.setAttribute("sellerName", userDTO.getUname());
		return resultMap.get("result");
	}
	
	
	/**
		 * 卖家确认价格暂未使用
		 * @author chengwt 创建时间：下午3:17:23
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/responseCommitInquiry")
	@ResponseBody
	public String responseCommitInquiry(Model model, HttpServletRequest request,
			HttpServletResponse response){
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String ids = request.getParameter("ids");
		InquiryInfoDTO dto = new InquiryInfoDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		dto.setSupplierId(userDTO.getShopId().intValue());
		resultMap = requestPriceService.responseCommitInquiry(dto, "" + userId, ids);
		return resultMap.get("result");
	}
	
	
	/**
		 * 重新询价，status字段置为2，明细的status字段置为空
		 * @author chengwt 创建时间：下午3:07:38
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/commitInquiryRe")
	@ResponseBody
	public String commitInquiryRe(Model model, HttpServletRequest request,
			HttpServletResponse response){
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String ids = request.getParameter("ids");
		InquiryInfoDTO dto = new InquiryInfoDTO();
		InquiryMatDTO dtoDetail = new InquiryMatDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = requestPriceService.commitInquiryRe(dto, "" + userId, ids);
		return resultMap.get("result");
	}
	
	
	/**
		 * 买家确认价格，将status字段置为3，相关明细的status字段置为1，表明此条明细被接收
		 * @author chengwt 创建时间：上午10:31:38
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/commitInquiryYes")
	@ResponseBody
	public String commitInquiryYes(Model model, HttpServletRequest request,
			HttpServletResponse response){
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String ids = request.getParameter("ids");
		String detailIds = request.getParameter("detailIds");
		InquiryInfoDTO dto = new InquiryInfoDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = requestPriceService.commitInquiryYes(dto, "" + userId, ids, detailIds);
		ExecuteResult<String> result = new ExecuteResult<String>();
		request.setAttribute("result", result);
		request.setAttribute("updResult", resultMap.get("result"));
		request.setAttribute("detailIds", detailIds);
		request.setAttribute("userId", userId);
		request.setAttribute("userName", userDTO.getUname());
		return resultMap.get("result");
	}

	/**
	 * 下单
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/createOrderInfo")
	@ResponseBody
	public String createOrderInfo(Model model, HttpServletRequest request,
								   HttpServletResponse response){
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String inquiryNo = request.getParameter("inquiryNo");
		String nums = request.getParameter("nums");
		String detailIds = request.getParameter("detailIds");
		String status = request.getParameter("status");
		//未接收直接下单的需要先将询价单状态单据状态改为3
		if("2".equals(status)) {
			Map<String, String> resultMap = new HashMap<String, String>();
			InquiryInfoDTO dto = new InquiryInfoDTO();
			resultMap = requestPriceService.commitInquiryYes(dto, "" + userId, inquiryNo, detailIds);
			if(!"接收成功!".equals(resultMap.get("result"))){
				return "下单失败!"+resultMap.get("result");
			}
		}
		//查询物品相关信息，将信息存入redis中
		String[] idList = detailIds.split(",");
		String[] numList = nums.split(",");
		List<Product> productList = new ArrayList<Product>();
		for(int i=0 ; i < idList.length; i++) {
			InquiryMatDTO inquiryMatDTO = new InquiryMatDTO();
			inquiryMatDTO.setId(Long.parseLong(idList[i]));
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			ExecuteResult<DataGrid<Map>> inquiryDTOList = inquiryExportService.queryInquiryMatList(inquiryMatDTO, pager);
			for(Map mapp : inquiryDTOList.getResult().getRows()){
				Product product = new Product();
				product.setCid(Long.parseLong("" + mapp.get("cid")));
				product.setQuantity(Integer.parseInt(numList[i]));
				product.setSkuPrice(new BigDecimal("" + mapp.get("matPrice")));
				product.setSkuId(Long.parseLong("" + mapp.get("matCd")));
				product.setItemId(Long.parseLong("" + mapp.get("itemId")));
				product.setShopId(Long.parseLong("" + mapp.get("shopId")));
				product.setSellerId(Long.parseLong("" + mapp.get("supplierId")));
				product.setRegionId("0");
				productList.add(product);
			}
		}
		String jsonProducts = JSONArray.toJSONString(productList);
		this.redisDB.set(userId+"Redis", jsonProducts);
		return "success";
	}
}
