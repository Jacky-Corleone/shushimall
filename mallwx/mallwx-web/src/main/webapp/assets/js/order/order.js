
//===============添加物流信息弹出层DIV的js代码  start=================================

	$(function(){
		//提交物流信息
		$(".js_logistics_submit").click(function(){
			var orderItemId = $("#deliveryOrderItemId").val();		//订单详情Id
			var itemId = $("#deliveryItemId").val();				//商品ID
			var orderId = $("#deliveryOrderId").val();				//订单ID
			var shopFreightTemplateId = $("#deliveryShopFreightTemplateId").val(); //运费模板
			var deliveryNumber = $("#deliveryNumber").val();		//物流单号
			var deliveryCompanyCode = $("#deliveryCompanyCode").val();	//物流公司代码
			var deliveryCompanyName = $("#deliveryCompanyName").val();	//物流公司代码
			var deliveryRemark = $("#deliveryRemark").val();		//备注信息
			//是否将此模板应用到其他商品（默认否）
			var isSametemplate = "no";
			var chkObjs = document.getElementsByName("isSametemplate");
	        for(var i=0;i<chkObjs.length;i++){
	            if(chkObjs[i].checked){
	                isSametemplate = chkObjs[i].value;
	            }
	        }
			if($.trim(deliveryNumber) == ""){alert("请填写物流编号"); return;}
			if($.trim(deliveryCompanyCode) == ""){alert("请选择物流公司"); return;}
			$.ajax({
				type: "POST",
				dataType: "json",
				url: $("#contextPath").val() + "/delivery/sendDeliberyMessage",
				data: {
					orderItemId: orderItemId,
					itemId: itemId,
					orderId: orderId,
					shopFreightTemplateId: shopFreightTemplateId,
					deliveryNumber: deliveryNumber,
					deliveryCompanyCode: deliveryCompanyCode,
					deliveryRemark: deliveryRemark,
					isSametemplate: isSametemplate
				},
				success: function(data){
					if(data.success){
						alert(data.message);
						$(".js_logistics").hide();
					}else{
						alert(data.message);
					}
				}
			});
		});
		//添加物流信息 弹框取消
		$(".js_logistics_cancle").click(function(){
			$(".js_logistics").hide();
		});
		//添加物流信息 弹框取消
		$(".js_logistics_cancle_buyer").click(function(){
			$(".js_logistics_buyer").hide();
		});
		
	});
	
	//添加物流信息
	function addDeliveryInfo(orderId, itemId, orderItemId, shopFreightTemplateId){
		//orderItemId:订单详情ID; itemId:item ID; orderId:订单ID; 
		$("#deliveryAddPage_div").css({'display':'block','position':'fixed'});
		//清空缓存
		$("#deliveryNumber").val("");
		$("#deliveryCompanyCode").val("");
		$("#deliveryCompanyName").val("");
		$("#deliveryRemark").val("");
		$("#isSametemplate_yes").checked=false;
		$("#isSametemplate_no").checked=false;
		$("#deliveryOrderItemId").val("");
		$("#deliveryItemId").val("");
		$("#deliveryOrderId").val();
		$("#deliveryShopFreightTemplateId").val();
		//加载数据
		$.ajax({
			type: "post",
			dataType: "json",
			url: $("#contextPath").val() + "/delivery/toAddDelivery",
			data: {
				orderItemId: orderItemId,
				itemId: itemId,
				orderId: orderId
			},
			success: function(data){
				//页面数据填写
				setLogisticsValue(data.deliveryDTO, orderId, itemId, orderItemId, shopFreightTemplateId);
			},
			error : function(e){
				console.log(e);
			}
			
		});
	}
	function setLogisticsValue(deliveryDTO, orderId, itemId, orderItemId, shopFreightTemplateId){
		
		$("#deliveryNumber").val(deliveryDTO.deliveryNumber);
		$("#deliveryCompanyCode").val(deliveryDTO.deliveryCompanyCode);
		$("#deliveryCompanyName").val(deliveryDTO.deliveryCompanyName);
		$("#deliveryRemark").val(deliveryDTO.deliveryRemark);
		if(deliveryDTO.isSametemplate=="yes"){
			document.getElementById('isSametemplate_yes').checked=true;
		}
		if(deliveryDTO.isSametemplate=="no"){
			document.getElementById('isSametemplate_no').checked=true;
		}
		$("#deliveryOrderItemId").val(orderItemId);
		$("#deliveryItemId").val(itemId);
		$("#deliveryOrderId").val(orderId);
		$("#deliveryShopFreightTemplateId").val(shopFreightTemplateId);
	}
	
	
	
	
//===============添加物流信息弹出层DIV的js代码  end=================================	
	
//===============物流公司弹出层DIV的js代码  start===================================
	
	//获取物流公司弹出层查询
	function getDeliveryCompany(page) {
	    $("#partBDetail").html("");
	    var deliveryCompany = $.trim($("#DeliveryCompanyDIV").find("[name='deliveryCompany_DIV']").val());
	    if ("请输入要查询的名称" == deliveryCompany) {
	    	deliveryCompany = null;
	    }
	    $.ajax({
	        url: $("#contextPath").val() + "/delivery/getDeliveryCompany",
	        data: {
	            "companyName": deliveryCompany,
	            "curPage": page
	        },
	        type: 'post',
	        dataType: 'html',
	        success: function (data) {
	            $("#partBDetail").html(data);
	            showDiv();
	        }
	    });
	    
	}
	
	// 获取选中的物流信息
	function getPartCompany(obj) {
	    var childs = $(obj).children();
        $('#deliveryCompanyCode').val(childs[0].innerHTML);//textContent对IE8无效，故修改成innerHTML
        $("#deliveryCompanyName").val(childs[1].innerHTML);

	    $("#DeliveryCompanyDIV").toggle();
	    
	    $("#deliveryCompany_DIV").val("");
	}

	//显示弹出层
	function showDiv() {
	    // 以下部分要将弹出层居中显示
//	    $("#DeliveryCompanyDIV").show();
	    $('#DeliveryCompanyDIV').css('display','block');
	}
	
	// 物流公司弹出层关闭
	function closeDiv(obj) {
	    $(obj).parent().parent().parent().hide();
	}
	
//===============物流公司弹出层DIV的js代码  end===================================

	
//查看物流信息
function queryDelivery(orderItemId){
	$(".js_logistics_buyer").show();
	//清空缓存
	$("#deliveryNumber_buyer").val("");
	$("#deliveryCompany_buyer").val("");
	$("#deliveryRemark_buyer").val("");
	$("#deliveryOrderItemId_buyer").val("");
	$("#deliveryData_buyer").html("");
	//加载数据
	$.ajax({
		type: "POST",
		dataType: "json",
		url: $("#contextPath").val() + "/delivery/getDeliveryInfo",
		data: {
			orderItemId: orderItemId
		},
		success: function(data){
			if(data.success){
				//页面数据填写
				setDelfiveryInfo(data.deliveryDTO,data.pushDelibery);
			}else{
				alert("获取数据失败");
			}
		}
	});
}
//显示物流信息
function setDelfiveryInfo(deliveryDTO,pushDelibery){
	$("#deliveryMessage_buyer").empty();
	$("#deliveryNumber_buyer").val(deliveryDTO.deliveryNumber);
	$("#deliveryCompanyCode_buyer").val(deliveryDTO.deliveryCompanyCode);
	$("#deliveryCompanyName_buyer").val(deliveryDTO.deliveryCompanyName);
	$("#deliveryRemark_buyer").text(deliveryDTO.deliveryRemark);
	var lastResult = pushDelibery.lastResult;
	var dataArray = null;
	if(lastResult){
		dataArray = lastResult.data;
	}
	if(dataArray != null){
		var $html = '';
		for(var i = 0 ; i < dataArray.length ; i++){
			var data = dataArray[i];
			$html =$html+"<li  style='height : 35px;text-align:left;' class='font_cen mar_l5'><p style='color: #333;line-height: 20px;'>" + FormatDate(data.time ) +"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+     data.context + "</p></li>";
			
		}
		$("#deliveryMessage_buyer").append($html);
	}
}
function FormatDate (strTime) {
    var date = new Date(strTime);
    var minute = date.getMinutes() < 10 ? "0"+date.getMinutes() : date.getMinutes();
    var second = date.getSeconds() < 10 ? "0"+date.getSeconds() : date.getSeconds();
    return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate() 
    	+ " " + date.getHours() + ":" + minute + ":" + second;
}