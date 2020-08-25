
//===============添加物流信息弹出层DIV的js代码  start=================================

	$(function(){
		//提交物流信息
		$(".js_logistics_submit").click(function(){
			//防止重复提交AJAX请求
			$(".js_logistics_submit").loadingMsg();
			var mobile = $("#mobiletelephone").val();		//订单详情Id
			var orderItemId = $("#deliveryOrderItemId").val();		//订单详情Id
			var itemId = $("#deliveryItemId").val();				//商品ID
			var orderId = $("#deliveryOrderId").val();				//订单ID
			var shopFreightTemplateId = $("#deliveryShopFreightTemplateId").val(); //运费模板
			var deliveryNumber = $("#deliveryNumber").val();		//物流单号
			var deliveryCompanyCode = $("#deliveryCompanyCode").val();	//物流公司代码
			var deliveryCompanyName = $("#deliveryCompanyName").val();	//物流公司名称
			var deliveryRemark = $("#deliveryRemark").val();		//备注信息
			//是否将此模板应用到其他商品（默认否）
			var isSametemplate = "no";
			var chkObjs = document.getElementsByName("isSametemplate");
	        for(var i=0;i<chkObjs.length;i++){
	            if(chkObjs[i].checked){
	                isSametemplate = chkObjs[i].value;
	            }
	        }
			if($.trim(deliveryNumber) == ""){
				alert("请填写物流编号");
				$(".js_logistics_submit").hideMsg();
				return;
			}
			if($.trim(deliveryCompanyCode) == ""){
				alert("请选择物流公司");
				$(".js_logistics_submit").hideMsg();
				return;
			}
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
					isSametemplate: isSametemplate,
					"mobile":mobile
				},
				success: function(data){
					if(data.success){
						alert(data.message);
						$(".js_logistics_submit").hideMsg();
						$(".js_logistics").hide();
						$("#queryForm").submit();
					}else{
						$(".js_logistics_submit").hideMsg();
						alert(data.message);
					}
				}
			});
		});
		//添加物流信息 弹框取消
		$(".js_logistics_cancle").click(function(){
			$(".js_logistics").hide();
		});
		
	});
	
	//添加物流信息
	function addDeliveryInfo(orderId, itemId, orderItemId, shopFreightTemplateId,mobile){
		//orderItemId:订单详情ID; itemId:item ID; orderId:订单ID; 
		//$(".js_logistics").show();
		
		$("#deliveryAddPage_div").show();
		
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
		$("#mobiletelephone").val(mobile);
		//加载数据
		$.ajax({
			type: "POST",
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
			}
		});
	}
	function setLogisticsValue(deliveryDTO, orderId, itemId, orderItemId, shopFreightTemplateId){
		
		$("#deliveryNumber").val(deliveryDTO.deliveryNumber);
		$("#deliveryCompanyCode").val(deliveryDTO.deliveryCompanyCode);
		$("#deliveryCompanyName").val(deliveryDTO.deliveryCompanyName);
		$("#deliveryRemark").val(deliveryDTO.deliveryRemark);
		$("#mobiletelephone").val(deliveryDTO.mobile);
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
	    $("#DeliveryCompanyDIV").show();
	}
	
	// 物流公司弹出层关闭
	function closeDiv(obj) {
	    $(obj).parent().parent().parent().hide();
	}
	
//===============物流公司弹出层DIV的js代码  end===================================

	
	
 