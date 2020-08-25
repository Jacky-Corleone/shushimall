package com.camelot.ecm.salesorder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.odps.udf.JSONArrayAdd;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.common.Json;
import com.camelot.ecm.SalesOrder.TradeOrderQuery;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersEnclosureDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.tradecenter.service.TradeOrdersEnclosureService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * Created by sevelli on 15-3-3.
 */
@Controller
@RequestMapping(value = "${adminPath}/salesorder")
public class SalesOrderController extends BaseController{
    @Resource
    private TradeOrderExportService tradeOrderExportService;
    @Resource
    private AddressBaseService addressBaseService;
    @Resource
    private UserExportService userExportService;
    @Resource
    private ItemExportService itemExportService;
    @Resource
	private TradeOrdersEnclosureService tradeOrdersEnclosureService;//订单附件
    @Resource
	private RedisDB redisDB;
    /**
     * 订单查询
     * @author - 门光耀
     * @message- 订单信息普通查询
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "list")
    public ModelAndView list(Model model){
        Map<String,Object> orderMap=new HashMap<String, Object>();
        TradeOrdersQueryInDTO tradeOrdersQueryInDTO=new TradeOrdersQueryInDTO();
        Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
        pager.setPage(1);
        pager.setRows(10);
        ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult= tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
        DataGrid<TradeOrdersDTO> dtoDataGrid=executeResult.getResult();
        Page page=new Page();
        page.setPageSize(10);
        page.setPageNo(1);
        if(dtoDataGrid!=null){
            List<TradeOrdersDTO> tradeOrdersDTOList=dtoDataGrid.getRows();
            if(tradeOrdersDTOList!=null&&tradeOrdersDTOList.size()>0){
               Map<Long,String> userMap=pPUser(tradeOrdersDTOList,true,true);
               page.setList(dtoToMap(tradeOrdersDTOList,userMap));
            }
            page.setCount(dtoDataGrid.getTotal());
        }else{
            page.setCount(0);
        }
        Map<String,List<Map<String,String>>> mapzt=stateFz();
        orderMap.put("mapzt",mapzt);
        orderMap.put("page",page);
        // 分页查询
        return new ModelAndView("salesorder/list","orderMap",orderMap);
    }
    //根据ids查询出订单的用户
    private Map<Long,String> pPUser(List<TradeOrdersDTO> tradeOrdersDTOList,boolean ifseller,boolean ifbuyer){
        Map<Long,String> map=new HashMap<Long, String>();
        if(tradeOrdersDTOList!=null&&tradeOrdersDTOList.size()>0){
            Map<Long,String> map1=new HashMap<Long, String>();
            //查询该订单是否上传房型图
            Map<String,String> houseMap=new HashMap<String, String>();
            Iterator<TradeOrdersDTO> iterator=tradeOrdersDTOList.iterator();
            while(iterator.hasNext()){
                TradeOrdersDTO tradeOrdersDTO=iterator.next();
                if(ifseller){
                    if(tradeOrdersDTO.getSellerId()!=null){
                        map1.put(tradeOrdersDTO.getSellerId(),"1");
                    }
                }
                if(ifbuyer){
                    if(tradeOrdersDTO.getBuyerId()!=null){
                        map1.put(tradeOrdersDTO.getBuyerId(),"1");
                    }
                }
                houseMap.put(tradeOrdersDTO.getOrderId(), "1");
            }
            if(map1.size()>0){
                Iterator<Long> iteratorids=map1.keySet().iterator();
                List<String> ids=new ArrayList<String>();
                //根据ids查询用户
                while(iteratorids.hasNext()){
                    ids.add(new BigDecimal(iteratorids.next()).toPlainString());
                }
                //UserDTO userDTO=new UserDTO();
                //DataGrid<UserDTO> userDTODataGrid1=userExportService.findUserListByCondition(userDTO, null,null);
                ExecuteResult<List<UserDTO>> executeResult =userExportService.findUserListByUserIds(ids);
                List<UserDTO> list=executeResult.getResult();
                if(list!=null&&list.size()>0){
                    Iterator<UserDTO> iteratordto=list.iterator();
                    while(iteratordto.hasNext()){
                        UserDTO userDTO=iteratordto.next();
                        map.put(userDTO.getUid(),userDTO.getUname());
                    }
                }
            }
            for (Map.Entry<String, String> entry : houseMap.entrySet()) {
            	TradeOrdersEnclosureDTO tradeOrdersEnclosureDTO = new TradeOrdersEnclosureDTO();
            	tradeOrdersEnclosureDTO.setOrderId(entry.getKey());
            	tradeOrdersEnclosureDTO.setIsDelete(0);
            	Pager<TradeOrdersEnclosureDTO> pager = new Pager<TradeOrdersEnclosureDTO>();
            	pager.setPage(1);
            	pager.setRows(500);
            	List<TradeOrdersEnclosureDTO> list = this.tradeOrdersEnclosureService.queryEnclosures(tradeOrdersEnclosureDTO, pager).getRows();
            	if (list!=null && list.size()>0) {
            		JSONArray jsonArray = new JSONArray();
            		for (TradeOrdersEnclosureDTO dto : list) {
            			JSONObject jo = new JSONObject();
            			jo.put("id", dto.getId());
            			jo.put("orderId", dto.getOrderId());
            			jo.put("enclosureName", dto.getEnclosureName());
            			jo.put("enclosureUrl", dto.getEnclosureUrl());
            			jo.put("type", dto.getType());
            			jo.put("remark", dto.getRemark());
            			jo.put("isImg", dto.getIsImg());
            			jsonArray.add(jo);
            			if (dto.getRemark()!=null && !"".equals(dto.getRemark())) {
    						if (dto.getType() == 1) {
    							map.put(1L, String.valueOf(dto.getId()));
    							map.put(2L, dto.getRemark());
    						}
    					}
					}
            		map.put(Long.valueOf(entry.getKey()), jsonArray.toJSONString());
				}
            }
        }
        return map;
    }
    /**
     * 订单查询
     * @author - 门光耀
     * @message- 订单信息普通查询，列表查询数据封装
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,String>> dtoToMap(List<TradeOrdersDTO> list,Map<Long,String> map){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(list!=null&&list.size()>0){
            Map<String,Map<String,String>> stateTable =stateTable();
            //orderStace:订单状态,pjStace:评价状态,qxStace:取消状态
            //订单状态
            Map<String,String> orderStace=stateTable.get("orderStace");
            //卖家 买家 评价状态
            Map<String,String> pjStace=stateTable.get("pjStace");
            //取消
            Map<String,String> qxStace=stateTable.get("qxStace");
            //支付形式
            Map<String,String> zflx=stateTable.get("zflx");
          //支付方式
            Map<String,String> isPayPeriod=stateTable.get("isPayPeriod");
            //是否有发票
            //Map<String,String> iffp=stateTable.get("iffp");
            Iterator<TradeOrdersDTO> iterator=list.iterator();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int i=1;
            while(iterator.hasNext()){
                TradeOrdersDTO tradeOrdersDTO=iterator.next();
                Map<String,String> mapNum=new HashMap<String, String>();
                mapNum.put("num",String.valueOf(i));
                i++;
                if(tradeOrdersDTO.getOrderId()!=null){
                    //订单编号
                    mapNum.put("id",tradeOrdersDTO.getOrderId().toString());
                }
                //卖家编号
                if(tradeOrdersDTO.getSellerId()!=null){
                    mapNum.put("sellerName",map.get(tradeOrdersDTO.getSellerId()));
                }
                if(tradeOrdersDTO.getBuyerId()!=null){
                    //买家编号
                    mapNum.put("buyerName",map.get(tradeOrdersDTO.getBuyerId()));
                    mapNum.put("buyerId",tradeOrdersDTO.getBuyerId().toString());
                }
                //用户名?是否是收货人?
                mapNum.put("name",tradeOrdersDTO.getName());
                //订单状态
                if(tradeOrdersDTO.getState()!=null){
                    mapNum.put("orderStace",orderStace.get(tradeOrdersDTO.getState().toString()));
                    //判断商品是否为服务商品
                    if(isServiceOrder(tradeOrdersDTO.getItems())){
                    	if(tradeOrdersDTO.getState() == 3){
                    		mapNum.put("orderStace", "待验收");
                    	}else if(tradeOrdersDTO.getState() == 2){
                    		mapNum.put("orderStace", "待服务");
                    	}
                    }else{
                    	if(tradeOrdersDTO.getState() == 3){
                    		mapNum.put("orderStace", "待收货");
                    	}else if(tradeOrdersDTO.getState() == 2){
                    		mapNum.put("orderStace", "待配送");
                    	}
                    }
                }
                //订单状态
                if(tradeOrdersDTO.getState()!=null){
                	mapNum.put("state",tradeOrdersDTO.getState().toString());
                }
                //支付方式
                if(tradeOrdersDTO.getPayPeriod()!=null){
                	mapNum.put("isPayPeriod","延期支付");
                }else{
                	mapNum.put("isPayPeriod", "立即支付");
                }
                //买家评价状态
                if(tradeOrdersDTO.getEvaluate()!=null){
                    mapNum.put("buyerPj",pjStace.get(String.valueOf(tradeOrdersDTO.getEvaluate())));
                }
                //卖家评价状态
                if(tradeOrdersDTO.getSellerEvaluate()!=null){
                    mapNum.put("sellerPj",pjStace.get(String.valueOf(tradeOrdersDTO.getSellerEvaluate())));
                }
                //是否取消（无）
                if(tradeOrdersDTO.getYn()!=null){
                    mapNum.put("qxflag",qxStace.get(tradeOrdersDTO.getYn().toString()));
                }
                //配送方式（无对应）
                if(tradeOrdersDTO.getShipmentType()!=null){
                    mapNum.put("shipMentType",tradeOrdersDTO.getShipmentType().toString());
                }
                //实际支付金额
                if(tradeOrdersDTO.getPaymentPrice()!=null){
                    mapNum.put("paymentprice",tradeOrdersDTO.getPaymentPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                }
                //下单时间(字段不对)
                if(tradeOrdersDTO.getCreateTime()!=null){
                    mapNum.put("ordertime",simpleDateFormat.format(tradeOrdersDTO.getCreateTime()));
                }
                //全部地址
                mapNum.put("address",tradeOrdersDTO.getFullAddress());
                //手机号码
                mapNum.put("mobile",tradeOrdersDTO.getMobile());
                //支付时间
                if(tradeOrdersDTO.getPaymentTime()!=null){
                    mapNum.put("paymenttime",simpleDateFormat.format(tradeOrdersDTO.getPaymentTime()));
                }
                if(tradeOrdersDTO.getUpdateTime()!=null){
                    if(tradeOrdersDTO.getState()!=null&&tradeOrdersDTO.getState().intValue()==5){
                        mapNum.put("updateTime",simpleDateFormat.format(tradeOrdersDTO.getUpdateTime()));
                    }
                }
                //主订单号
                if(tradeOrdersDTO.getParentOrderId()!=null){
                    mapNum.put("parentOrderId",String.valueOf(tradeOrdersDTO.getParentOrderId()));
                }
                //优惠码
                mapNum.put("promocode",tradeOrdersDTO.getPromoCode());
                //所属平台
	            if(null!=tradeOrdersDTO.getPlatformId()){
	        	   mapNum.put("platformId", tradeOrdersDTO.getPlatformId().toString());
	            }
                //支付方式
                if(tradeOrdersDTO.getPaymentType()!=null){
                    mapNum.put("zflx",zflx.get(String.valueOf(tradeOrdersDTO.getPaymentType())));
                }
                //是否支付
                if(tradeOrdersDTO.getPaid()!=null){
                	mapNum.put("paid",tradeOrdersDTO.getPaid()+"");
                }
                //是否为延期支付
                if(tradeOrdersDTO.getPayPeriod()!=null){
                	mapNum.put("payPeriod",tradeOrdersDTO.getPayPeriod()+"");
                }
                //是否上传房型图
                mapNum.put("ishouse", map.get(Long.valueOf(tradeOrdersDTO.getOrderId())));
                listMap.add(mapNum);
            }
        }
        return listMap;
    }
    /**
     * 订单查询
     * @author - 门光耀
     * @message- 订单信息普通查询,明细信息查询封装
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,Object>> dtoToMapDetail(List<TradeOrdersDTO> list,Map<Long,String> map){
        List<Map<String,Object>> listMap=new ArrayList<Map<String, Object>>();
        if(list!=null&&list.size()>0){
            Iterator<TradeOrdersDTO> i=list.iterator();
            Map<String,String> mapids=new HashMap<String, String>();
            //便利地址id,获取省市区的名称
           /* Map<String,String> dy=new HashMap<String, String>();
            while(i.hasNext()){
                TradeOrdersDTO rd=i.next();
                if(rd.getProvinceId()!=null){
                    mapids.put(rd.getProvinceId().toString(),rd.getProvinceId().toString());
                }
                if(rd.getCityId()!=null){
                    mapids.put(rd.getCityId().toString(),rd.getCityId().toString());
                }
                if(rd.getCountyId()!=null){
                    mapids.put(rd.getCountyId().toString(),rd.getCountyId().toString());
                }
            }
            if(mapids.size()>0){
                dy=queryDiYu(mapids);
            }*/
            Map<String,Map<String,String>> stateTable =stateTable();
            //orderStace:订单状态,pjStace:评价状态,qxStace:取消状态
            //订单状态
            Map<String,String> orderStace=stateTable.get("orderStace");
            //卖家 买家 评价状态
            Map<String,String> pjStace=stateTable.get("pjStace");
            //取消
            Map<String,String> qxStace=stateTable.get("qxStace");
            //支付方式
            Map<String,String> zflx=stateTable.get("zflx");
            //是否有发票
            Map<String,String> iffp=stateTable.get("iffp");
            Iterator<TradeOrdersDTO> iterator=list.iterator();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while(iterator.hasNext()){
                TradeOrdersDTO tradeOrdersDTO=iterator.next();
                Map<String,Object> mapNum=new HashMap<String, Object>();
                if(tradeOrdersDTO.getOrderId()!=null){
                    //订单编号
                    mapNum.put("id",tradeOrdersDTO.getOrderId().toString());
                }
                //卖家编号
                if(tradeOrdersDTO.getSellerId()!=null){
                    mapNum.put("sellerName",map.get(tradeOrdersDTO.getSellerId()));
                }
                if(tradeOrdersDTO.getBuyerId()!=null){
                    //买家编号
                    mapNum.put("buyerName",map.get(tradeOrdersDTO.getBuyerId()));
                    mapNum.put("buyerId",tradeOrdersDTO.getBuyerId());
                }
                //用户名?是否是收货人?
                mapNum.put("name",tradeOrdersDTO.getName());
                //订单状态
                if(tradeOrdersDTO.getState()!=null){
                    mapNum.put("orderStace",orderStace.get(tradeOrdersDTO.getState().toString()));
                    //判断商品是否为服务商品
                    if(isServiceOrder(tradeOrdersDTO.getItems())){
                    	if(tradeOrdersDTO.getState() == 3){
                    		mapNum.put("orderStace", "待验收");
                    	}else if(tradeOrdersDTO.getState() == 2){
                    		mapNum.put("orderStace", "待服务");
                    	}
                    }else{
                    	if(tradeOrdersDTO.getState() == 3){
                    		mapNum.put("orderStace", "待收货");
                    	}else if(tradeOrdersDTO.getState() == 2){
                    		mapNum.put("orderStace", "待配送");
                    	}
                    }
                }
                //订单评价状态还尚未获取（无）
                //订单取消状态尚未获取（无）
                //订单完成时间（无）
                //是否取消（无）
                //配送方式（无对应）
                if(tradeOrdersDTO.getShipmentType()!=null){
                    mapNum.put("shipMentType",tradeOrdersDTO.getShipmentType().toString());
                }
                //实际支付金额
                if(tradeOrdersDTO.getPaymentPrice()!=null){
                    mapNum.put("paymentprice",tradeOrdersDTO.getPaymentPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                }
                //商品金额
                if(tradeOrdersDTO.getTotalPrice()!=null){
                    mapNum.put("totalprice",tradeOrdersDTO.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                }
                //总优惠金额
                if(tradeOrdersDTO.getTotalDiscount()!=null){
                    mapNum.put("totaldiscount",tradeOrdersDTO.getTotalDiscount().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                }else{
                    mapNum.put("totaldiscount","0.00");
                }
                //运费
                if(tradeOrdersDTO.getFreight()!=null){
                    mapNum.put("freightyf",tradeOrdersDTO.getFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                }
                //下单时间(字段不对)
                if(tradeOrdersDTO.getCreateTime()!=null){
                    mapNum.put("ordertime",simpleDateFormat.format(tradeOrdersDTO.getCreateTime()));
                }
                //全部地址
                mapNum.put("address",tradeOrdersDTO.getFullAddress());
                //拼接并封装地域封装地域
                String address="";
                /*if(tradeOrdersDTO.getProvinceId()!=null){
                    address=dy.get(tradeOrdersDTO.getProvinceId().toString());
                }
                if(tradeOrdersDTO.getCityId()!=null){
                    address=address+">>"+dy.get(tradeOrdersDTO.getCityId().toString());
                }
                if(tradeOrdersDTO.getCountyId()!=null){
                    address=address+">>"+dy.get(tradeOrdersDTO.getCountyId().toString());
                }
                address=address+">>"+tradeOrdersDTO.getDetailAddress();*/
                if(tradeOrdersDTO.getFullAddress()!=null&&!"".equals(tradeOrdersDTO.getFullAddress())){
                    address=tradeOrdersDTO.getFullAddress();
                }
/*                if(tradeOrdersDTO.getDetailAddress()!=null&&!"".equals(tradeOrdersDTO.getDetailAddress())){
                    address=address+">>"+tradeOrdersDTO.getDetailAddress();
                }*/
                mapNum.put("addressp",address);
                //手机号码
                mapNum.put("mobile",tradeOrdersDTO.getMobile());
                //支付类型
                if(tradeOrdersDTO.getPaymentType()!=null){
                    mapNum.put("zflx",zflx.get(tradeOrdersDTO.getPaymentType().toString()));
                }
                //支付时间
                if(tradeOrdersDTO.getPaymentTime()!=null){
                    mapNum.put("paymenttime",simpleDateFormat.format(tradeOrdersDTO.getPaymentTime()));
                }
                //是否有发票
                if(tradeOrdersDTO.getInvoice()!=null){
                    mapNum.put("iffp",iffp.get(tradeOrdersDTO.getInvoice().toString()));
                }
                //发票台头
                mapNum.put("fptt",tradeOrdersDTO.getInvoiceTitle());
                //订单备注
                mapNum.put("comment",tradeOrdersDTO.getMemo());
                //物流公司
                mapNum.put("wlname",tradeOrdersDTO.getLogisticsCompany());
                //物流编号
                mapNum.put("wlcode",tradeOrdersDTO.getLogisticsNo());
                if(tradeOrdersDTO.getUpdateTime()!=null){
                    if(tradeOrdersDTO.getState()!=null&&tradeOrdersDTO.getState().intValue()==5){
                        mapNum.put("updateTime",simpleDateFormat.format(tradeOrdersDTO.getUpdateTime()));
                    }
                }
                mapNum.put("promocode",tradeOrdersDTO.getPromoCode());
                //是否上传房型图
                String string = map.get(Long.valueOf(tradeOrdersDTO.getOrderId()));
                JSONArray array = JSONArray.parseArray(string);
                mapNum.put("ishouse", array);
                mapNum.put("enclosureId",map.get(1L));
                mapNum.put("enclosureRemark",map.get(2L));
                listMap.add(mapNum);
            }
        }
        return listMap;
    }
    
    private boolean isServiceOrder(List<TradeOrderItemsDTO> orderList){
    	boolean is = true ; 
    	if(null != orderList && !orderList.isEmpty() && orderList.size() > 0){
        	for(TradeOrderItemsDTO item : orderList){
        		ExecuteResult<ItemDTO> itemResult =itemExportService.getItemById(item .getItemId());
        		if(null != itemResult.getResult()){
        			if(itemResult.getResult().getAddSource() < 4){
        				is = false;
        				break;
        			}
        		}
        	}
        }
    	return is;
    }

    private List<Map<String,String>> selectOrderItems(List<TradeOrdersDTO> listdtos){
        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        if(listdtos!=null&&listdtos.size()>0){
            TradeOrdersDTO dto=listdtos.get(0);
            List<TradeOrderItemsDTO> items=dto.getItems();
            if(items!=null&&items.size()>0){
                Iterator<TradeOrderItemsDTO> iterator=items.iterator();
                while(iterator.hasNext()){
                    TradeOrderItemsDTO item=iterator.next();
                    Map<String,String> map=new HashMap<String, String>();
                    if(item.getNum()!=null){
                        map.put("num",String.valueOf(item.getNum()));
                    }
                    if(item.getPayPrice()!=null){
                        map.put("price",item.getPayPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                    }
                    if(item.getPayPriceTotal()!=null){
                        map.put("priceTotal",item.getPayPriceTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                    }
                    if(item.getSkuId()!=null){
                        map.put("skuId",String.valueOf(item.getSkuId()));
                    }
                    if(item.getItemId()!=null){
                        map.put("itemId",String.valueOf(item.getItemId()));
                    }
                    if(item.getSkuId()!=null){
                        ItemShopCartDTO skuDto=new ItemShopCartDTO();
                        skuDto.setItemId(item.getItemId());
                        skuDto.setSkuId(item.getSkuId());
                        skuDto.setShopId(dto.getShopId());
                        ExecuteResult<ItemShopCartDTO> executeResult= itemExportService.getSkuShopCart(skuDto);
                        ItemShopCartDTO itemShopCartDTO=executeResult.getResult();
                        if(itemShopCartDTO!=null){
                            map.put("itemName",itemShopCartDTO.getItemName());
                            map.put("skuUrl",itemShopCartDTO.getSkuPicUrl());
                        }
                    }
                    list.add(map);
                }
            }
        }

        return list;
    }

    @RequestMapping("/selectordertable")
    @ResponseBody
    public Json<Page<Map<String,String>>> selectOrderTable(HttpServletRequest request){
        Json json=new Json();
        TradeOrdersQueryInDTO tradeOrdersQueryInDTO=new TradeOrdersQueryInDTO();
        Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
        String page=request.getParameter("page");
        if(page==null||"".equals(page)){
            page="1";
        }
        String rows=request.getParameter("rows");
        if(rows==null||"".equals(rows)){
            rows="10";
        }
        pager.setPage(new Integer(page));
        pager.setRows(new Integer(rows));
        //订单号
        String orderNum=request.getParameter("orderNum");
        //卖家账号
        String userName1=request.getParameter("userName1");
        Map<Long,String> userMap=new HashMap<Long, String>();
        //是否需要调用查询订单的方法
        boolean ifseller=true;
        boolean ifbyer=true;
        //是否需要再次查询用户的数据
        List<Long> sellerids=new ArrayList<Long>();
        List<Long> buyerids=new ArrayList<Long>();
        if(userName1!=null&&!"".equals(userName1)){
            UserDTO userDTO=new UserDTO();
            userDTO.setUname(userName1);
            DataGrid<UserDTO> userDTODataGrid1=userExportService.findUserListByCondition(userDTO,null,null);
            List<UserDTO> userDTOList=userDTODataGrid1.getRows();
            if(userDTOList!=null&&userDTOList.size()>0){
                Iterator<UserDTO> iteratoruser1=userDTOList.iterator();
                Map<Long,String> useridmap=new HashMap<Long, String>();
                while(iteratoruser1.hasNext()){
                    UserDTO userDTO1=iteratoruser1.next();
                    if(userDTO1.getUid()!=null){
                        useridmap.put(userDTO1.getUid(),"1");
                        userMap.put(userDTO1.getUid(),userDTO1.getUname());
                    }
                }
                if(useridmap.size()>0){
                    Iterator<Long> iterator1=useridmap.keySet().iterator();
                    while(iterator1.hasNext()){
                        sellerids.add(iterator1.next());
                    }
                    //设置卖家的ids
                    tradeOrdersQueryInDTO.setSellerIdList(sellerids);
                }else{
                    ifseller=false;
                }
            }else{
                ifseller=false;
            }
        }
        //买家账号
        String userName2=request.getParameter("userName2");
        if(userName2!=null&&!"".equals(userName2)){
            UserDTO userDTO=new UserDTO();
            userDTO.setUname(userName2);
            DataGrid<UserDTO> userDTODataGrid1=userExportService.findUserListByCondition(userDTO,null,null);
            List<UserDTO> userDTOList=userDTODataGrid1.getRows();
            if(userDTOList!=null&&userDTOList.size()>0){
                Iterator<UserDTO> iteratoruser1=userDTOList.iterator();
                Map<Long,String> useridmap=new HashMap<Long, String>();
                while(iteratoruser1.hasNext()){
                    UserDTO userDTO1=iteratoruser1.next();
                    if(userDTO1.getUid()!=null){
                        useridmap.put(userDTO1.getUid(),"1");
                        userMap.put(userDTO1.getUid(),userDTO1.getUname());
                    }
                }
                if(useridmap.size()>0){
                    Iterator<Long> iterator1=useridmap.keySet().iterator();
                    while(iterator1.hasNext()){
                        buyerids.add(iterator1.next());
                    }
                    //设置买家的ids
                    tradeOrdersQueryInDTO.setBuyerIdList(buyerids);
                }else{
                    ifbyer=false;
                }
            }else{
                ifbyer=false;
            }
        }
        Page pagetable=new Page();
        pagetable.setPageNo(pager.getPage());
        pagetable.setPageSize(pager.getRows());
        if(ifseller&&ifbyer){
			if (orderNum != null && !"".equals(orderNum)) {
				tradeOrdersQueryInDTO.setOrderId(orderNum);
			}
            //订单状态
            String orderStace=request.getParameter("orderStace");
            if(orderStace!=null&&!"".equals(orderStace)){
                tradeOrdersQueryInDTO.setState(new Integer(orderStace));
            }
            //买家家评价状态
            String pjStace1=request.getParameter("pjStace1");
            if(pjStace1!=null&&!"".equals(pjStace1)){
                tradeOrdersQueryInDTO.setEvaluate(new Integer(pjStace1));
            }
            //取消状态
            String qxFlag=request.getParameter("qxFlag");
            if(qxFlag!=null&&!"".equals(qxFlag)){
                tradeOrdersQueryInDTO.setCancelFlag(new Integer(qxFlag));
            }
            //卖家评价状态
            String pjStace2=request.getParameter("pjStace2");
            if(pjStace2!=null&&!"".equals(pjStace2)){
                tradeOrdersQueryInDTO.setSellerEvaluate(new Integer(pjStace2));
            }
            //支付类型
            String paymentType=request.getParameter("paymentType");
            if(paymentType!=null&&!"".equals(paymentType)){
                tradeOrdersQueryInDTO.setPaymentType(new Integer(paymentType));
            }
            String parentOrderId=request.getParameter("parentOrderId");
            if(parentOrderId!=null&&!"".equals(parentOrderId)){
                tradeOrdersQueryInDTO.setParentOrderId(parentOrderId);
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
                tradeOrdersQueryInDTO.setPlatformId(new Integer(platformId));
            }
            //支付方式。1：延期支付，2立即支付
            String payType = request.getParameter("payType");
            if(!StringUtils.isEmpty(payType)){
            	tradeOrdersQueryInDTO.setIsPayPeriod(Integer.parseInt(payType));
            }
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                //下单时间开始
                String createTimeBegin=request.getParameter("createTimeBegin");
                if(createTimeBegin!=null&&!"".equals(createTimeBegin)){
                    tradeOrdersQueryInDTO.setCreateStart(sdf.parse(createTimeBegin));
                }
                //下单时间结束
                String createTimeEnd=request.getParameter("createTimeEnd");
                if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                    Calendar   calendar   =   new   GregorianCalendar();
                    calendar.setTime(sdf.parse(createTimeEnd));
                    //calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                    Date end=calendar.getTime();
                    tradeOrdersQueryInDTO.setCreateEnd(end);
                }
            }catch(Exception e){
                json.setSuccess(false);
            }
            ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult= tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
            DataGrid<TradeOrdersDTO> dataGrid=executeResult.getResult();
            if(dataGrid!=null){
                List<TradeOrdersDTO> list=dataGrid.getRows();
                if(list!=null&&list.size()>0){
                    //用于判断是否根据卖家ids查询卖家的信息
                    boolean ifss=true;
                    //用于判断是否根据买家ids查询买家的信息
                    boolean ifsb=true;
                    if(tradeOrdersQueryInDTO.getSellerIdList()!=null&&tradeOrdersQueryInDTO.getSellerIdList().size()>0){
                        ifss=false;
                    }
                    if(tradeOrdersQueryInDTO.getBuyerIdList()!=null&&tradeOrdersQueryInDTO.getBuyerIdList().size()>0){
                        ifsb=false;
                    }
                    Map<Long,String> userMap1=pPUser(list,ifss,ifsb);
                    userMap.putAll(userMap1);
                    pagetable.setList(dtoToMap(list,userMap));
                }
                pagetable.setCount(dataGrid.getTotal());
            }else{
                pagetable.setCount(0);
            }
        }else{
            pagetable.setCount(0);
        }
        json.setObj(pagetable);
        json.setSuccess(true);
        json.setMsg(pagetable.toString());
        return json;
    }
    @RequestMapping("/selectordertablecount")
    @ResponseBody
    public Json<Page<Map<String,String>>> selectOrderTableCount(HttpServletRequest request){
        Json json=new Json();
        TradeOrdersQueryInDTO tradeOrdersQueryInDTO=new TradeOrdersQueryInDTO();
        Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
        String page="1";
        String rows="1";
        pager.setPage(new Integer(page));
        pager.setRows(new Integer(rows));
        //订单号
        String orderNum=request.getParameter("orderNum");
        //卖家账号
        String userName1=request.getParameter("userName1");
        Map<Long,String> userMap=new HashMap<Long, String>();
        //是否需要调用查询订单的方法
        boolean ifseller=true;
        boolean ifbyer=true;
        //是否需要再次查询用户的数据
        List<Long> sellerids=new ArrayList<Long>();
        List<Long> buyerids=new ArrayList<Long>();
        if(userName1!=null&&!"".equals(userName1)){
            UserDTO userDTO=new UserDTO();
            userDTO.setUname(userName1);
            DataGrid<UserDTO> userDTODataGrid1=userExportService.findUserListByCondition(userDTO,null,null);
            List<UserDTO> userDTOList=userDTODataGrid1.getRows();
            if(userDTOList!=null&&userDTOList.size()>0){
                Iterator<UserDTO> iteratoruser1=userDTOList.iterator();
                Map<Long,String> useridmap=new HashMap<Long, String>();
                while(iteratoruser1.hasNext()){
                    UserDTO userDTO1=iteratoruser1.next();
                    if(userDTO1.getUid()!=null){
                        useridmap.put(userDTO1.getUid(),"1");
                        userMap.put(userDTO1.getUid(),userDTO1.getUname());
                    }
                }
                if(useridmap.size()>0){
                    Iterator<Long> iterator1=useridmap.keySet().iterator();
                    while(iterator1.hasNext()){
                        sellerids.add(iterator1.next());
                    }
                    //设置卖家的ids
                    tradeOrdersQueryInDTO.setSellerIdList(sellerids);
                }else{
                    ifseller=false;
                }
            }else{
                ifseller=false;
            }
        }
        //买家账号
        String userName2=request.getParameter("userName2");
        if(userName2!=null&&!"".equals(userName2)){
            UserDTO userDTO=new UserDTO();
            userDTO.setUname(userName2);
            DataGrid<UserDTO> userDTODataGrid1=userExportService.findUserListByCondition(userDTO,null,null);
            List<UserDTO> userDTOList=userDTODataGrid1.getRows();
            if(userDTOList!=null&&userDTOList.size()>0){
                Iterator<UserDTO> iteratoruser1=userDTOList.iterator();
                Map<Long,String> useridmap=new HashMap<Long, String>();
                while(iteratoruser1.hasNext()){
                    UserDTO userDTO1=iteratoruser1.next();
                    if(userDTO1.getUid()!=null){
                        useridmap.put(userDTO1.getUid(),"1");
                        userMap.put(userDTO1.getUid(),userDTO1.getUname());
                    }
                }
                if(useridmap.size()>0){
                    Iterator<Long> iterator1=useridmap.keySet().iterator();
                    while(iterator1.hasNext()){
                        buyerids.add(iterator1.next());
                    }
                    //设置买家的ids
                    tradeOrdersQueryInDTO.setBuyerIdList(buyerids);
                }else{
                    ifbyer=false;
                }
            }else{
                ifbyer=false;
            }
        }
        Page pagetable=new Page();
        pagetable.setPageNo(pager.getPage());
        pagetable.setPageSize(pager.getRows());
        if(ifseller&&ifbyer){
            if(orderNum!=null&&!"".equals(orderNum)){
                tradeOrdersQueryInDTO.setOrderId(orderNum);
            }
            //订单状态
            String orderStace=request.getParameter("orderStace");
            if(orderStace!=null&&!"".equals(orderStace)){
                tradeOrdersQueryInDTO.setState(new Integer(orderStace));
            }
            //买家家评价状态
            String pjStace1=request.getParameter("pjStace1");
            if(pjStace1!=null&&!"".equals(pjStace1)){
                tradeOrdersQueryInDTO.setEvaluate(new Integer(pjStace1));
            }
            //取消状态
            String qxFlag=request.getParameter("qxFlag");
            if(qxFlag!=null&&!"".equals(qxFlag)){
                tradeOrdersQueryInDTO.setCancelFlag(new Integer(qxFlag));
            }
            //卖家评价状态
            String pjStace2=request.getParameter("pjStace2");
            if(pjStace2!=null&&!"".equals(pjStace2)){
                tradeOrdersQueryInDTO.setSellerEvaluate(new Integer(pjStace2));
            }
            //支付类型
            String paymentType=request.getParameter("paymentType");
            if(paymentType!=null&&!"".equals(paymentType)){
                tradeOrdersQueryInDTO.setPaymentType(new Integer(paymentType));
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
                tradeOrdersQueryInDTO.setPlatformId(new Integer(platformId));
            }
            String parentOrderId=request.getParameter("parentOrderId");
            if(parentOrderId!=null&&!"".equals(parentOrderId)){
                tradeOrdersQueryInDTO.setParentOrderId(parentOrderId);
            }
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                //下单时间开始
                String createTimeBegin=request.getParameter("createTimeBegin");
                if(createTimeBegin!=null&&!"".equals(createTimeBegin)){
                    tradeOrdersQueryInDTO.setCreateStart(sdf.parse(createTimeBegin));
                }
                //下单时间结束
                String createTimeEnd=request.getParameter("createTimeEnd");
                if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                    Calendar   calendar   =   new   GregorianCalendar();
                    calendar.setTime(sdf.parse(createTimeEnd));
                    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                    Date end=calendar.getTime();
                    tradeOrdersQueryInDTO.setCreateEnd(end);
                }
            }catch(Exception e){
                json.setSuccess(false);
            }
            ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult= tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
            DataGrid<TradeOrdersDTO> dataGrid=executeResult.getResult();
            if(dataGrid!=null){
                pagetable.setCount(dataGrid.getTotal());
            }else{
                pagetable.setCount(0);
            }
        }else{
            pagetable.setCount(0);
        }
        json.setObj(pagetable);
        json.setSuccess(true);
        json.setMsg(pagetable.toString());
        return json;
    }

	@RequestMapping("/orderdetail")
	public ModelAndView orderDetail(String orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isBlank(orderId)) {
			TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
			Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
			pager.setPage(1);
			pager.setRows(10);
			tradeOrdersQueryInDTO.setOrderId(orderId);
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
			DataGrid<TradeOrdersDTO> dataGrid = executeResult.getResult();
			if (dataGrid != null) {
				List<TradeOrdersDTO> list = dataGrid.getRows();
				Map<Long, String> userMap = pPUser(list, true, true);
				List<Map<String, Object>> listmap = dtoToMapDetail(list, userMap);
				if (listmap != null && listmap.size() > 0) {
					map = listmap.get(0);
				}
				List<Map<String, String>> listsku = selectOrderItems(list);
				// List<List<Map<String,String>>> listfc=huanHang(listsku,3);
				map.put("itemList", listsku);
			}
		}
		return new ModelAndView("salesorder/salesorderdetail", "map", map);
	}
	
	@RequestMapping("/orderPlanDetail")
	public ModelAndView orderPlanDetail(String orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isBlank(orderId)) {
			TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
			Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
			pager.setPage(1);
			pager.setRows(10);
			tradeOrdersQueryInDTO.setOrderId(orderId);
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
			DataGrid<TradeOrdersDTO> dataGrid = executeResult.getResult();
			if (dataGrid != null) {
				List<TradeOrdersDTO> list = dataGrid.getRows();
				Map<Long, String> userMap = pPUser(list, true, true);
				List<Map<String, Object>> listmap = dtoToMapDetail(list, userMap);
				if (listmap != null && listmap.size() > 0) {
					map = listmap.get(0);
				}
				List<Map<String, String>> listsku = selectOrderItems(list);
				// List<List<Map<String,String>>> listfc=huanHang(listsku,3);
				map.put("itemList", listsku);
			}
		}
		return new ModelAndView("salesorder/salesOrderPlanDetail", "map", map);
	}
	
    private List<List<Map<String,String>>> huanHang(List<Map<String,String>> listMap,int page){
        List<List<Map<String,String>>> huanHang=new ArrayList<List<Map<String, String>>>();
        if(listMap!=null&&listMap.size()>0){
            int ps=listMap.size()/page;
            int lou=listMap.size()%page;
            if(ps>0){
                for(int i=0;i<ps;i++){
                    List<Map<String,String>> list=new ArrayList<Map<String, String>>();
                    for(int j=page*(ps-1);j<page*ps;j++){
                        list.add(listMap.get(j));
                    }
                    huanHang.add(list);
                }
            }
            if(lou>0){
                int start=listMap.size()-lou;
                List<Map<String,String>> list=new ArrayList<Map<String, String>>();
                for(int i=start;i<listMap.size();i++){
                    list.add(listMap.get(i));
                }
                huanHang.add(list);
            }
        }
        return huanHang;
    }
    @RequestMapping(value = "export")
    public String export(HttpServletRequest request,HttpServletResponse response,Pager pager){

        String fileName = "订单数据"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
        try {
            Json<Page<Map<String,String>>> json=selectOrderTable(request);
            if(json.isSuccess()){
                Page<Map<String,String>> page=json.getObj();
                List<Map<String,String>> listMap=page.getList();
                if(listMap!=null&&listMap.size()>0){
                    //TODO 导出订单数据
                    List<TradeOrderQuery> orderList = new ArrayList<TradeOrderQuery>();
                    for (Map<String,String> dto:listMap){
                        TradeOrderQuery query = new TradeOrderQuery();
                        try {
                            PropertyUtils.copyProperties(query,dto);
                        } catch (IllegalAccessException e) {
                            logger.error("复制订单属性出错", e);
                        } catch (InvocationTargetException e) {
                            logger.error("复制订单属性出错", e);
                        } catch (NoSuchMethodException e) {
                            logger.error("复制订单属性出错",e);
                        }
                        orderList.add(query);
                    }

                    new ExportExcel("用户数据", TradeOrderQuery.class).setDataList(orderList).write(response, fileName).dispose();
                }
            }
        } catch (IOException e) {
            logger.error("导出订单出错",e);
        }
        return null;
    }
    /**
     * 订单查询
     * orderStace:订单状态,pjStace:评价状态,qxStace:取消状态
     * @author - 门光耀
     * @message- 订单状态封装(用于前台展示)
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private Map<String,List<Map<String,String>>> stateFz(){
        Map<String,List<Map<String,String>>> map=new HashMap<String, List<Map<String, String>>>();
        //订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
        List<Map<String,String>> mapListstace=new ArrayList<Map<String, String>>();
        Map orderStaceMap1=new HashMap();
        orderStaceMap1.put("code","1");
        orderStaceMap1.put("name","待付款");
        Map orderStaceMap2=new HashMap();
        orderStaceMap2.put("code","2");
        orderStaceMap2.put("name","待配送/服务");
        Map orderStaceMap3=new HashMap();
        orderStaceMap3.put("code","3");
        orderStaceMap3.put("name","待收货/验收");
        Map orderStaceMap4=new HashMap();
        orderStaceMap4.put("code","4");
        orderStaceMap4.put("name","待评价");
        Map orderStaceMap5=new HashMap();
        orderStaceMap5.put("code","5");
        orderStaceMap5.put("name","已完成");
        Map orderStaceMap6=new HashMap();
        orderStaceMap6.put("code","6");
        orderStaceMap6.put("name","已取消");
        Map orderStaceMap7=new HashMap();
        orderStaceMap7.put("code","7");
        orderStaceMap7.put("name","已关闭");
        mapListstace.add(orderStaceMap1);
        mapListstace.add(orderStaceMap2);
        mapListstace.add(orderStaceMap3);
        mapListstace.add(orderStaceMap4);
        mapListstace.add(orderStaceMap5);
        mapListstace.add(orderStaceMap6);
        mapListstace.add(orderStaceMap7);
        //订单状态
        map.put("orderStace",mapListstace);
        //买家 卖家评价(1:无,2:是)
        List<Map<String,String>> mjlist1=new ArrayList<Map<String, String>>();
        Map<String,String> mjMap1=new HashMap<String, String>();
        Map<String,String> mjMap2=new HashMap<String, String>();
        mjMap1.put("code","1");
        mjMap1.put("name","无");
        mjMap2.put("code","2");
        mjMap2.put("name","是");
        mjlist1.add(mjMap1);
        mjlist1.add(mjMap2);
        //卖家和买家评价状态
        map.put("pjStace",mjlist1);

        //取消订单(1:否，2:是)
        List<Map<String,String>> qxlist=new ArrayList<Map<String, String>>();
        Map<String,String> qxMap1=new HashMap<String, String>();
        Map<String,String> qxMap2=new HashMap<String, String>();
        qxMap1.put("code","1");
        qxMap1.put("name","否");
        qxMap2.put("code","2");
        qxMap2.put("name","是");
        qxlist.add(qxMap1);
        qxlist.add(qxMap2);
        map.put("qxStace",qxlist);
        return map;
    }
    /**
     * 订单查询
     * orderStace:订单状态,pjStace:评价状态 ,qxStace:取消状态,zflx 支付方式,iffp 是否有发票
     * @author - 门光耀
     * @message- 订单状态封装(用于前台展示)
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private Map<String,Map<String,String>> stateTable(){
        Map<String,Map<String,String>> map=new HashMap<String, Map<String, String>>();
        //订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
        Map orderStaceMap1=new HashMap();
        orderStaceMap1.put("1","待付款");
        orderStaceMap1.put("2","待配送/服务");
        orderStaceMap1.put("3","待收货/验收");
        orderStaceMap1.put("4","待评价");
        orderStaceMap1.put("5","已完成");
        orderStaceMap1.put("6","已取消");
        orderStaceMap1.put("7","已关闭");
        //订单状态
        map.put("orderStace",orderStaceMap1);
        //买家 卖家评价(1:无,2:是)
        Map<String,String> mjMap1=new HashMap<String, String>();
        mjMap1.put("1","未评价");
        mjMap1.put("2","已评价");
        //卖家和买家评价状态
        map.put("pjStace",mjMap1);
        //取消订单(1:否，2:是)
        Map<String,String> qxMap1=new HashMap<String, String>();
        qxMap1.put("1","未取消");
        qxMap1.put("2","已取消");
        map.put("qxStace",qxMap1);
        Map<String,String> zflx=new HashMap<String, String>();
        zflx.put("0","支付宝");
        zflx.put("1","网银");
        zflx.put("2","小印支付");
        zflx.put("3","线下");
        zflx.put("4","支付宝银行");
        zflx.put("5","微信");
        zflx.put("6","微信PC端");
        zflx.put("7","积分支付");
        zflx.put("8","银联");
        zflx.put("100","支付宝手机端");
        zflx.put("101","网银在线手机端");
        map.put("zflx",zflx);
        Map<String,String> iffp=new HashMap<String, String>();
        iffp.put("1","无发票");
        iffp.put("2","有发票");
        map.put("iffp",iffp);
        Map<String,String> isPayPeriod=new HashMap<String, String>();
        isPayPeriod.put("1","延期支付");
        isPayPeriod.put("2","立即支付");
        map.put("isPayPeriod",isPayPeriod);
        return map;
    }

    private Map<String,String> queryDiYu(Map<String,String> mapcodes){
        Map<String,String> mapdy=new HashMap<String, String>();
        if(mapcodes!=null&&mapcodes.size()>0){
            String[] dyCodes =new String[mapcodes.keySet().size()];
            Iterator<String> i=mapcodes.keySet().iterator();
            int y=0;
            while(i.hasNext()){
                dyCodes[y]=i.next();
                y++;
            }
            ExecuteResult<List<AddressBaseDTO>> result=addressBaseService.queryNameByCode(dyCodes);
            List<AddressBaseDTO> listadds=result.getResult();
            if(listadds!=null&&listadds.size()>0){
                Iterator<AddressBaseDTO> iterator=listadds.iterator();
                while(iterator.hasNext()){
                    AddressBaseDTO addDto=iterator.next();
                    mapdy.put(addDto.getCode(),addDto.getName());
                }
            }
        }
        return mapdy;
    }
    
    
    @ResponseBody
    @RequestMapping({ "verifyCount" })
    public ExecuteResult<String> verifyCount(String orderId,long isImg,Long buyerId) {
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	TradeOrdersEnclosureDTO query = new TradeOrdersEnclosureDTO();
		query.setIsImg(isImg);
		query.setIsDelete(0);
		query.setType(-1);
		query.setOrderId(orderId);
		List<TradeOrdersEnclosureDTO> list = tradeOrdersEnclosureService.queryEnclosures(query, new Pager<TradeOrdersEnclosureDTO>()).getRows();
		if (isImg==1) {
			if (list.size() >= 5) {
				result.setResultMessage("error");
				return result;
			}
		}else if(isImg == 2){
			if (list.size() >= 1) {
				result.setResultMessage("error");		
				return result;
			}
		}
		result.setResultMessage("success");
		return result;
    }
    
    @ResponseBody
	@RequestMapping({ "addEnclosure" })
	public ExecuteResult<String> addEnclosure(HttpServletRequest request, String url,String orderId,String enclosureName,String remark,long isImg,long id,long type,Long buyerId) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		TradeOrdersEnclosureDTO dto = new TradeOrdersEnclosureDTO();
        dto.setEnclosureUrl(url);
        dto.setIsDelete(0);
        dto.setOrderId(orderId);
        dto.setEnclosureName(enclosureName);
        dto.setRemark(remark);
        dto.setId(id);
        dto.setType(type);
        dto.setIsImg(isImg);
        ExecuteResult<TradeOrdersEnclosureDTO> addEnclosure = tradeOrdersEnclosureService.addEnclosure(dto);
        result.setResult(String.valueOf(addEnclosure.getResult().getId()));
        result.setResultMessage("success");
        //取redis中值 判断是否已经发生短信  
        if (buyerId!=null) {
        	String planBuyerId = redisDB.get(buyerId+"planBuyerId");
        	if (planBuyerId==null || "".equals(planBuyerId) || planBuyerId=="0") {
        		request.setAttribute("planBuyerId", buyerId);
        		redisDB.set(buyerId+"planBuyerId", buyerId.toString());
        	}
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("delEnclosure")
	public ExecuteResult<String> delEnclosure(long id){
		ExecuteResult<String> result = new ExecuteResult<String>();
		if(id!=0){
			result = tradeOrdersEnclosureService.delEnclosure(id);
		}
		return result;
	}
}
