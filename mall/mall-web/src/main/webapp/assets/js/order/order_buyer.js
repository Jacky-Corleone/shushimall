$(function(){
		//加载左侧菜单
		$("#leftBuyer").load($("#contextPath").val()+"/leftBuyer");
		//申请退款弹层
		/*
		$(".js_shenqing_refund").click(function(){
			var orderId = $(this).attr("name");
			$("#boxContent").load($("#contextPath").val()+"/order/refundBox", {orderId: orderId}, function(){
				$(".js_choice").show();
			});
		});
		*/
		//查看物流信息 弹框取消
		$(".js_logistics_cancle").click(function(){
			$(".js_logistics").hide();
		});
		
		//延期付款 和  待付款 选项卡切换
		$('#tabs li').click(function(){
		  var $this = $(this);
		  var $t = $this.index();
		  $li.removeClass();
		  $this.addClass('active');
		});
});
//条件查询
function submitGoodsForm(){
	var orderId = $("input[name='orderId']").val();
	if( !isChineseChar(orderId) ){
		//条件 搜索时 初始化 分页page   start 李伟龙	2015年9月6日 13:27:31
		$("input[name='page']").val("1");
		//条件 搜索时 初始化 分页page   end 李伟龙
		var form = document.all.queryForm;
		form.submit();
	}else{
		alert("查询条件：订单号不能为中文!");
	}
};
//中文校验
function isChineseChar(str){     
   var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;  
   return reg.test(str);  
} 
//到指定的分页页面
function topage(page){
	$("input[name='page']").val(page);
	$("#queryForm").submit();
}

//查看
function getLastMonthYestdy() {
	var select_time = document.all.select_time.value;
	var daysInMonth = new Array([ 0 ], [ 31 ], [ 28 ], [ 31 ], [ 30 ], [ 31 ],
		[ 30 ], [ 31 ], [ 31 ], [ 30 ], [ 31 ], [ 30 ], [ 31 ]);
	var date = new Date();
	var strYear = date.getFullYear();
	var strDay = date.getDate();
	var strMonth = date.getMonth() + 1;

	if (select_time == 0) {
		document.all.createStart.value = "";
		document.all.createEnd.value = "";
		return ;
	} else if (select_time == 2) {
		select_time = 3;
	} else if (select_time == 3) {
		strYear = strYear -1;
		datastr = strYear + "-" + strMonth + "-" + strDay;
		document.all.createStart.value = datastr;
		document.all.createEnd.value = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		return ;
	}

	strMonth = strMonth - select_time+1;
	if (strYear % 4 == 0 && strYear % 100 != 0) {
		daysInMonth[2] = 29;
	}
	if (strMonth - 1 <= 0) {
		strYear -= 1;
		strMonth = strMonth - 1 + 12;
	} else {
		strMonth -= 1;
	}
	strDay = daysInMonth[strMonth] >= strDay ? strDay : daysInMonth[strMonth];
	datastr = strYear + "-" + strMonth + "-" + strDay;
	document.all.createEnd.value = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	document.all.createStart.value = datastr;
};
  
  //调整tab页签效果
function changeTabs(thisObj, Num) {
	if (thisObj.className == "active"){
		return;
	}
	$("input[name='state']").val(Num);
	$("input[name='page']").val("1");
	
	$("input[name='shopName']").val("");
	$("input[name='orderId']").val("");
	$("#select_time").val(0);
	$("input[name='createStart']").val("");
	$("input[name='createEnd']").val("");
	$("#shipmentType").val("");
	$("#queryForm").submit();
};
//调整tab页签效果
function changeTabs1(thisObj, Num) {
	if (thisObj.className == "active"){
		return;
	}
	if(Num!=null||Num!=""){
		$("input[name='approveStatus']").val(Num);
	}
	$("input[name='page']").val("1");
	
	$("input[name='shopName']").val("");
	$("input[name='orderId']").val("");
	$("#select_time").val(0);
	$("input[name='createStart']").val("");
	$("input[name='createEnd']").val("");
	$("#shipmentType").val(1);
	$("#queryForm").submit();
};
//待付款  和  分期付款
function changeShipmentype(thisObj, Num, state) {
	if (thisObj.className == "active"){
		return;
	}
	
	$("#shipmentType").val(Num);
	$("input[name='state']").val(state);
	$("input[name='page']").val("1");
	
	$("input[name='shopName']").val("");
	$("input[name='orderId']").val("");
	$("#select_time").val(0);
	$("input[name='createStart']").val("");
	$("input[name='createEnd']").val("");
	$("#queryForm").submit();
};
//取消订单
function orderStatusHandle(obj, orderId, orderStatus){
	if(confirm("您确定要取消此订单吗？")){
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/order/cancelOrder",
			data: {
				orderId: orderId,
				orderStatus: orderStatus
			},
			success: function(data){
				if(data.success){
					$("#queryForm").submit();
				}else{
					alert(data.errorMessages);
				}
			}
		});
	}
}
function deleteOrderById(orderId){
	if(confirm("您确定要删除此订单吗？")){
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/order/deleteOrderById",
			data: {
				orderId: orderId
			},
			success: function(data){
				if(data.success){
					$("#queryForm").submit();
				}else{
					alert(data.errorMessages);
				}
			}
		});
	}
}
//延迟收货
function delayPayment(orderId, refund){
		if(confirm("如果您确定操作延期，该订单将会被延期收货14天！")){
			$.ajax({
				type: "POST",
				dataType: "json",
				url: $("#contextPath").val()+"/order/delayDelivery",
				data: {
					orderId: orderId
				},
				success: function(data){
					if(data.success){
						$("#queryForm").submit();
					}else{
						alert(data.errorMessages);
					}
				}
			});
		}
	
}
//确认收货
function confirmReceipt(){
	$("#payerror").html("");
	var orderId = $("#orderId_pay").val();
	var paypwd = $("#paypwd").val();
	if(orderId==null || $.trim(orderId)==""){
		$("#payerror").html("请指定要操作的订单");
		return;
	}
	if(paypwd==null || $.trim(paypwd)==""){
		$("#payerror").html("请输入支付密码");
		return;
	}
	if(confirm("点击确定之后，您的付款将直接到卖家账户里，请务必收到货后再确定")){
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/order/confirmReceipt",
			data: {
				orderId: orderId,
				paypwd: paypwd
			},
			success: function(data){
				if(data.success){
					$("#queryForm").submit();
				}else{
					$("#payerror").html(data.errorMessages);
				}
			}
		});
	}
}
//确认收货
function openBomb(orderId, refund){
	$("#paypwd").val("");
	$("#payerror").html("");
	$("#confirmReceipt").attr({"disabled":"disabled"});
	if (refund == 2) {
		if(confirm("您申请的退款/退货正在处理中，点击“确认收货”会关闭卖家未处理或拒绝退款的申请，请确认是否继续？")){
			$("#orderId_pay").val(orderId);
			$("#addCartBomb").show();
			$("#refund").val(refund);
		} else{
			return false;
		}
	} else{
		$("#orderId_pay").val(orderId);
		$("#addCartBomb").show();
		$("#refund").val(refund);
	}
	$.ajax({
		url: $("#contextPath").val() + "/order/validateExistPaymentCode",
		data: {},
		type: "post",
		dataType: "json",
		success: function(data){
			if( data.success ){
				$("#confirmReceipt").removeAttr("disabled");
			} else{
				//$("#confirmReceipt").removeClass("button_1").addClass("button_3");
				$("#payerror").html(data.errorMessages);
			}
		}
	});
}
function closeBomb(){
	$("#addCartBomb").hide();
}
//function buyAgain(shopId,itemId,sellerId,quantity,areaCode){
//	$.ajax({
//		url: "$request.contextPath/shopCart/add",
//		data: {
//			"shopId": shopId,
//			"itemId":itemId,
//			"sellerId":sellerId,
//			"quantity":quantity,
//			"regionId":areaCode,
//			"skuId":skuId
//		},
//		type: "post",
//		dataType: "json",
//		success: function(data){
//			if( data.success ){
//				location.href = "$request.contextPath/shopCart/toCart";
//			}
//		}
//	});
//}
// 再次购买
function buyAgain(orderId){
	$.ajax({
		url: $("#contextPath").val() + "/shopCart/checkOrderItemsInventory",
		data: {
			"orderId": orderId
		},
		type: "post",
		dataType: "json",
		success: function(data){
			if( data.success ){
				location.href = $("#contextPath").val() + "/shopCart/buyAgain?orderId=" + orderId;
			} else{
				alert(data.errorMessages[0]);
			}
		}
	});
}
//查看物流信息
function addLogistics(obj, logisticsNo, logisticsCompany){
	$(".js_logistics").show();
	$("#logisticsNo").val(logisticsNo);
	$("#logisticsCompany").val(logisticsCompany);

}