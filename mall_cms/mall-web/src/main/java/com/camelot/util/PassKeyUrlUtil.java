package com.camelot.util;

import java.util.HashMap;
import java.util.Map;

public class PassKeyUrlUtil {
	//key:需要过滤的Url 
	//value:这个url中需要加密的字段,如果有多个，则以“,”分开：如：a,b,c.如果有多个加密条件则用;分开
	public static Map<String , String> passKeyUrlMap = new HashMap<String,String>();
	
	static{
		//买家中心订单列表页面，点击订单详情
		passKeyUrlMap.put("/order/queryOrderInfoBuyer", "orderId");
		//卖家中心订单列表页面，点击订单详情
		passKeyUrlMap.put("/order/queryOrderInfoSeller", "orderId");
		//卖家中心订单列表页面，点击打印发货单
		passKeyUrlMap.put("/order/toPrint", "orderId");
		//平台商品编辑和查看
		passKeyUrlMap.put("/sellcenter/sellProduct/plantForm", "itemId");
		//非平台商品编辑和查看
		passKeyUrlMap.put("/sellcenter/sellProduct/form", "itemId");
		//卖家退款详情页
		passKeyUrlMap.put("/order/refundInfoSeller", "returnGoodId");
		//卖家、买家查看退款/退货页面
		passKeyUrlMap.put("/order/refundSubmitSucc", "returnId,skuId;orderId,skuId");
		//卖家查看投诉协议页面
		passKeyUrlMap.put("/complain/gocomplainsellerdetail", "complainid");
		//买家查看投诉协议页面
		passKeyUrlMap.put("/complain/gocomplaindetail", "complainid");
		//卖家 查看卖家仲裁
		passKeyUrlMap.put("/complain/gocomplainselleradd", "tradeReturnid");
		//卖家 查看买家仲裁
		passKeyUrlMap.put("/complain/gocomplainadd", "tradeReturnid");
		
		//买家 申请退款/退货、申请售后
		passKeyUrlMap.put("/order/refundAgreement", "orderId,skuId");
		
		passKeyUrlMap.put("/order/refundInfoBuyer", "returnGoodId");
		
		
	}
	


}
