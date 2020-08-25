$(document).ready(function(){
	//加载咨询内容
	$("#consultContent").load($("#contextPath").val()+"/productController/consult", {itemId: $("#itemId").val()});
	//省份的id
	$("#areaCode").val($.cookie('regionCode'));
	//构造页面”配送至“内容
	builAddress($.cookie('regionCode'));
	//获取省份
	getAddress(0, "Tabaddress_Content0");
	$(".js_guanqu").click(function(){
		$(".nTab_address").toggle();
		/*
		$('.nTab_address').mouseleave(function(){
			$(".nTab_address").hide();
		});
		*/
	});
	$("#Tabaddress li").click(function(){
		$(this).addClass("active").removeClass("normal");
		$(this).siblings().addClass("normal").removeClass("active");
		$("#Tabaddress_Content"+$(this).index()).parent().show();
		$("#Tabaddress_Content"+$(this).index()).parent().siblings().hide();
	});
	
	//构造购买列表标题
	var arrAttrName = new Array();
	var arrAttrId = new Array();
	//获取所有的属性列表
	$('.attrName_js').each(function(){
		var attrId_Name = $(this).attr("name");
		arrAttrId.push(parseInt(attrId_Name.split(":")[0]));
		arrAttrName.push(attrId_Name.split(":")[1]);
	});
	arrAttrId = arrAttrId.sort(function(a,b){return a>b?1:-1});//数组从小到大排序
	//console.log("arrAttrId===="+arrAttrId);
	//console.log("arrAttrName===="+arrAttrName);
	
	//商品属性点击 attrName_js
	var map = new Map();//属性map
	var map_attrValue = new Map();//属性值map
	var map_attrId_attrValueId = new Map();//用户点击的属性Id和属性值Id
	var map_attrId_attrValueName = new Map();//用户点击的属性Id和属性值Name
	//页面初始化加载
	$(".js_attr").each(function(){
		if($(this).hasClass("border-8")){
			var attrId_Name_click = $(this).parent().siblings(".attrName_js").attr("name");//属性
			var attrId = attrId_Name_click.split(":")[0];
			var attrName = attrId_Name_click.split(":")[1];
			var attrValueId_Name_click = $(this).attr("name");//属性值
			var attrValueId = attrValueId_Name_click.split(":")[0];
			var attrValueName = attrValueId_Name_click.split(":")[1];
			map.put(parseInt(attrId), attrName);
			map_attrValue.put(parseInt(attrValueId), attrValueName);
			map_attrId_attrValueId.put(parseInt(attrId), attrValueId);
			map_attrId_attrValueName.put(parseInt(attrId), attrValueName);
		}
	});
	var hasPrice = $("#hasPrice").val();//是否有价格，无价格显示我要询价
	if(!map.isEmpty()){
		//console.log("hasPrice="+hasPrice);
		var itemStatus = $("#itemStatus").val();//商品状态
		//console.log("销售状态itemStatus="+itemStatus);
		if(itemStatus==4){
			if(hasPrice==1 ){//商品有价格时加载， 我要询价 时不加载
				addCartHTML(arrAttrId, map, map_attrId_attrValueId);
				getPromotion($("#itemId").val());//页面促销信息
			}else{
				queryInquiry(arrAttrId, map, map_attrId_attrValueId);
			}
		}
	}
	
	$(".js_attr").click(function(){
		if(!$(this).hasClass("border-8")){
			$('.number_js').val("1");//购买数量重新设为1
			var attrId_Name_click = $(this).parent().siblings(".attrName_js").attr("name");//属性
			var attrId = attrId_Name_click.split(":")[0];
			var attrName = attrId_Name_click.split(":")[1];
			var attrValueId_Name_click = $(this).attr("name");//属性值
			var attrValueId = attrValueId_Name_click.split(":")[0];
			var attrValueName = attrValueId_Name_click.split(":")[1];
			if(!$(this).hasClass("border-8")){//判断是否选中
				$(this).removeClass("border-13").addClass("border-8");
				$(this).append('<b class="po_ab pa_04"></b>');
				$(this).siblings().removeClass("border-8").addClass("border-13");
				$(this).siblings().children().remove("b");
				map.put(parseInt(attrId), attrName);
				map_attrValue.put(parseInt(attrValueId), attrValueName);
				map_attrId_attrValueId.remove(parseInt(attrId));
				map_attrId_attrValueId.put(parseInt(attrId), attrValueId);
				map_attrId_attrValueName.remove(parseInt(attrId));
				map_attrId_attrValueName.put(parseInt(attrId), attrValueName);
			}
			//console.log("map===="+map.keys()+"      "+map.values());
			//console.log("map_attrValue===="+map_attrValue.keys()+"      "+map_attrValue.values());
			//console.log("map_attrId_attrValueId===="+map_attrId_attrValueId.keys()+"      "+map_attrId_attrValueId.values());
			//console.log("map_attrId_attrValueName===="+map_attrId_attrValueName.keys()+"      "+map_attrId_attrValueName.values());
			//console.log("-----------------------------------------");
			
			//var price_name = $("#doAskPrice").attr("name");//用户咨询过价格，读取价格
			//if(hasPrice==1 || (price_name!=null && typeof(price_name) != "undefined")){//商品有价格时加载， 我要询价 时不加载
				if($('#addSource').val()==3){
					addCombinationCartHTML($(this).attr("name"));
				} else{
					addCartHTML(arrAttrId, map, map_attrId_attrValueId);
				}
				getPromotion($("#itemId").val());//页面促销信息
				$("#myTab0 > li:first").click();//商品评价
				flushCommCount();
				getSkuScope();
			//}
				//初始化sku切换器
				$('.smallImgUp').trigger('click');
		}
	});
    
	//商品收藏
	$(".js_favourite").click(function(){
		var shopId = $("#shopId").val();//店铺id
		var itemId = $("#itemId").val();//商品Id
		var sellerId = $("#sellerId").val();//商家id
		var itemName = $("#itemName").val();//商品名称
		var skuId = $("#skuId").val();//SKU_ID
		var price = $("#marketPrice").val();//商品指导价格
		var pictureUrl;
		if(skuId==null || $.trim(skuId) == ''){
			alert("请先选择商品的销售属性信息");
			return;
		}
		var shoucangshangpin = $("#shoucangshangpin").text();
		if($.trim(shoucangshangpin) == "收藏商品"){
			$.ajax({
				type: "POST",
				dataType: "json",
				url: $("#contextPath").val()+"/favourite/addItem",
				data: {
					shopId: shopId,
					itemId: itemId,
					sellerId: sellerId,
					itemName: itemName,
					skuId: skuId,
					price: price,
					pictureUrl: pictureUrl
				},
				success: function(data){
					alert("收藏成功");
					$("#shoucangshangpin").html("已收藏");
					//$(".js_favourite").unbind("click");
				}
			});
		}
	});
	//店铺收藏
	$(".js_favourite_shop").click(function(){
		var shopId = $("#shopId").val();//店铺id
		var sellerId = $("#sellerId").val();//商家id
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/favourite/addShop",
			data: {
				shopId: shopId,
				sellerId: sellerId
			},
			success: function(data){
				alert("收藏成功");
				$(".js_favourite_shop").html("已收藏");
				$(".js_favourite_shop").unbind("click");
			}
		});
	});
	
	//我要询价
	$("#doAskPrice").click(function(){
		var skuId = $("#skuId").val();
		if(skuId==null || $.trim(skuId)==""){
			alert("请先选择商品的销售属性再发起询价");
		}else{
			$("#xunjia").show();
		}
		
	});
	$(".js_xunjia").click(function(){
		
		var regMobile = /^[1]([3|5|7|8][0-9]{1}|59|58|88|89)[0-9]{8}$/;
		var regEmail = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z\.-]+)\.([a-z]{2,6})$/;
		//询价必须是认证的买家
		var email = $("#email_xunjia").val();
		if (!regEmail.test(email)){
			$(".errorMsg").html("请输入正确的邮箱地址");
			return;
		}
		var cellphone = $("#mobile_xunjia").val();
		if(!regMobile.test(cellphone)){
			$(".errorMsg").html("请输入正确的手机号");
			return;
		}
		var qty = $("#inquiryQty").val();
		if(qty==null || $.trim(qty)==""){
			$(".errorMsg").html("请输入采购数量");
			return;
		}
		var comment = $("#comment").val();
		var buyerId = $.cookie('uid');
		var sellerId = $("#sellerId").val();
		var shopId = $("#shopId").val();
		var itemId = $("#itemId").val();
		var skuId = $("#skuId").val();
		if(skuId==null || $.trim(skuId)==""){
			alert("请先选择商品的销售属性再发起询价");
			return;
		}
		
		$(".errorMsg").html("");
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/productController/addInquiry",
			data: {
				email: email,
				cellphone: cellphone,
				qty: qty,
				comment: comment,
				buyerId: buyerId,
				sellerId: sellerId,
				shopId: shopId,
				itemId: itemId,
				skuId: skuId
			},
			success: function(data){
				if(data.success){
					alert("询价提交成功");
					$(".cancel").click();
				}else{
					alert("询价失败，原因："+data.errorMessages);
				}
			}
		});
	});
	
//	$(".js_qing_manjian").hover(function(){
//		$(".js_xiang_manjian").show();
//    },function(){
//		$(".js_xiang_manjian").hide();
//    })
    $("#promotion_manjian").hover(function(){
    	$(".js_xiang_manjian").show();
    },function(){
    	$(".js_xiang_manjian").hide();
    })
//    $(".js_qing_manjian").mouseleaver(function(){
//    	$(".js_xiang_manjian").hide();
//    })
//    $(".js_qing_manjian").mouseenter(function(){
//		$(".js_xiang_manjian").show();
//    })
//    $("#promotionMsg_manjian").mouseleaver(function(){
//    	$(".js_xiang_manjian").hide();
//    })
//    $("#promotionMsg_manjian").mouseenter(function(){
//    	$(".js_xiang_manjian").show();
//    })
	$(".js_down_zhijiang").click(function(){
		$(".js_xiang_zhijiang").hide();
    })
    $(".js_qing_zhijiang").click(function(){
		$(".js_xiang_zhijiang").show();
    })
    $(".js_qing_youhui").click(function(){
    	$(".js_xiang_youhui").show();
    })
    $(".js_down_youhui").click(function(){
		$(".js_xiang_youhui").hide();
    })
	$("#shop_left dl dd").click(function(){
		if($(this).children("i").hasClass("fa-angle-double-up")){
			$(this).children("i").removeClass("fa-angle-double-up").addClass("fa-angle-double-down");
			$(this).next("ul").show();
		}else{
			$(this).children("i").removeClass("fa-angle-double-down").addClass("fa-angle-double-up");
			$(this).next("ul").hide();
		}
	});
	// 运送方式
	$("#store-prompt-ESM").click(function(event){
		if($("#store-prompt-ESM-ul").is(":hidden")){
			$(this).prop("class","store-prompt-focus");
            $("#store-prompt-ESM-ul").show();
            event.stopPropagation();
		} else{
    		$(this).prop("class","store-prompt");
            $("#store-prompt-ESM-ul").hide();
			event.stopPropagation();
		}
	});
	$(document).click(function(){
        $("#store-prompt-ESM").prop("class","store-prompt");
        $("#store-prompt-ESM-ul").hide();
    });
	// 初始化运送方式切换事件
	initDeliveryTypeChangeEvent();
	// 增值服务
	$(".js_value_added_item").click(function(){
		if($(this).hasClass("border-8")){
			$("#valueAddedItem_" + $(this).attr("value")).remove();
			$(this).removeClass("border-8").addClass("border-13");
			$(this).children().remove("b");
			$(this).next("select").hide();
		} else{
			$(this).append('<input type="hidden" name="valueAddedSkuId" id="valueAddedItem_' + $(this).attr("value") + '" value="' + $(this).next("select").find("option:selected").val() + '"/>');
			$(this).removeClass("border-13").addClass("border-8");
			$(this).append('<b class="po_ab pa_04"></b>');
			$(this).next("select").show();
		}
		if($("#skuId").val()){
			buildBuyListHTML();
		}
    });
	// 增值服务的sku切换
	$(".js_value_added_item_value").change(function(){
		$("#valueAddedItem_" + $(this).val().split("-")[0]).val($(this).val());
		if($("#skuId").val()){
			buildBuyListHTML();
		}
	});
});

// 推荐套餐切换
function getPackageSku(){
	var itemId = $("#itemId").val();
	var skuId = $("#skuId").val();
	var region = $("#areaCode").val();
	var packageSkuId = $("[name=packageSkuId] option:selected").val();
	$.ajax({
		url: $("#contextPath").val()+"/productController/getPackageSku",
		data: {
			itemId: itemId,
			skuId: skuId,
			region: region,
			packageSkuId: packageSkuId
		},
		type: "post",
		dataType: "html",
		success: function(data){
			$("#packageSku").html(data);
		}
	});
}

// 运送方式切换事件
function initDeliveryTypeChangeEvent(){
	$("#store-prompt-ESM-ul li").click(function(){
		$("#defaultDeliveryType").text($(this).text());
		$(this).find("b").show();
		$(this).css("background","#fff7f1");
		$(this).siblings().css("background","");
		$(this).siblings().find("b").hide();
	});
}
//获取商品评分
function getSkuScope(){
	var buyerId = $.cookie('uid');
	var sellerId = $("#sellerId").val();
	var shopId = $("#shopId").val();
	var itemId = $("#itemId").val();
	var skuId = $("#skuId").val();
	$.ajax({
		type: "POST",
		dataType: "json",
		url: $("#contextPath").val()+"/productController/getItemScope",
		data: {
			shopId: shopId,
			id: itemId,
			skuId: skuId
		},
		success: function(data){
		 var result=data.skuScope;
		 if(result==null||result==""||result=="undefined"){
			 //$("#scoreId").html('<span class="star sa0"></span>');
		 }else{
			 if(parseFloat(result)>4.4){
					$("#scoreId").html('<span class="star sa5"></span>');
				}
				else if(parseFloat(result)>4.2){
					$("#scoreId").html('<span class="star-on"></span><span class="star-on"></span><span class="star-on"></span><span class="star-on"></span><span class="star-half"></span>');
				}
				else if(parseFloat(result)>3.4){
					$("#scoreId").html('<span class="star sa4"></span>');
				}
				else if(parseFloat(result)>3.2){
					$("#scoreId").html('<span class="star-on"></span><span class="star-on"></span><span class="star-on"></span><span class="star-half"></span><span class="star-off"></span>');
				}
				else if(parseFloat(result)>2.4){
					$("#scoreId").html('<span class="star sa3"></span>');
				}
				else if(parseFloat(result)>2.2){
					$("#scoreId").html('<span class="star-on"></span><span class="star-on"></span><span class="star-half"></span><span class="star-off"></span><span class="star-off"></span>');
				}
				else if(parseFloat(result)>1.4){
					$("#scoreId").html('<span class="star sa2"></span>');
				}
				else if(parseFloat(result)>1.2){
					$("#scoreId").html('<span class="star-on"></span><span class="star-half"></span><span class="star-off"></span><span class="star-off"></span><span class="star-off"></span>');
				}
				else if(parseFloat(result)>0.4){
					$("#scoreId").html('<span class="star sa1"></span>');
				}
				else if(parseFloat(result)>0.2){
					$("#scoreId").html('<span class="star-half"></span><span class="star-off"></span><span class="star-off"></span><span class="star-off"></span><span class="star-off"></span>');
				}
				else{
					$("#scoreId").html('<span class="star sa0"></span>');
				 }  
		 }
	  }
});
	
	
}
//读取用户询价
function queryInquiry(arrAttrId, map, map_attrId_attrValueId){
	var logging_status = $("#logging_status").val();//判断用户是否登录
	var buyerId = $.cookie('uid');//用户id
	var sellerId = $("#sellerId").val();//商家id
	var itemId = $("#itemId").val();
	var skuId = $("#skuId").val();
	if(logging_status == "true"){
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/productController/queryInquiry",
			data: {
				sellerId: sellerId,
				buyerId: buyerId,
				skuId: skuId,
				itemId: itemId
			},
			success: function(data){
				//console.info(data);
				if(data.success){
					$("#doAskPrice").attr("name", "");
					var res = data.result;
					if(res!=null && typeof(res)!="undefined"){
						$("#doAskPrice").attr("name", res.inquiryPrice);
						$("#doAskPrice").unbind( "click" );//有价格后解除点击事件
						addCartHTML(arrAttrId, map, map_attrId_attrValueId);
						getPromotion($("#itemId").val());//页面促销信息
						var marketPrice = $("#marketPrice").val();//市场价
						$("#marketPrice_del").html("￥"+marketPrice);
					}else{
						$("#cuxiaojia").hide();
						$("#shoujia").html('<span class="font_fe">暂无报价</span>');
						$("#marketPrice_del").html("");
						$("#doAskPrice").bind("click", function() {
							var skuId = $("#skuId").val();
							if(skuId==null || $.trim(skuId)==""){
								alert("请先选择商品的销售属性再发起询价");
							}else{
								$("#xunjia").show();
							}
						});
						querySkuPics(skuId);
					}
				}
			}
		});
	}else{
		querySkuPics(skuId);
	}
}
//根据skuId查询sku图片
function querySkuPics(skuId){
	$.ajax({
		type: "POST",
		dataType: "json",
		url: $("#contextPath").val()+"/productController/querySkuPics",
		data: {
			skuId: skuId
		},
		success: function(data){
			if(data && data.skuPicUrl){
				var imageServerAddr = $("#imageServerAddr").val();
				var skuPicUrl = data.skuPicUrl;
				$("#midimg").attr("src", imageServerAddr+skuPicUrl);
				var skuPics = data.skuPics;
				if(skuPics!=null && typeof(skuPics)!="undefined"){
					var picHtml = '<ul>';
					for(var i=0;i<skuPics.length;i++){
						picHtml += '<li><img src="'+imageServerAddr+''+skuPics[i].picUrl+'"/></li>';
					}
					picHtml += '</ul>';
					$("#imageMenu").empty().html(picHtml);
				}
				//图片效果重新加载
				$.getScript($("#contextPath").val()+"/assets/js/goodscenter/details.js");
			}
		}
	});
}
//购买汇总
function buyCollect(){
	//重新读取价格、库存
	buildBuyListHTML();
	//var num = $('.number_js').val();//购买数量
	//var qty = $("#qty_js").val();//商品数量

	
}

//加入立即购买
function toProOrderView(activitesDetailsId){
	var itemId = $("#itemId").val();//商品Id
	var itemStatus;
	var msg;
	$.ajax({
		type: "POST",
		dataType: "text",
		url: $("#contextPath").val()+"/productController/getItemStatus",
		async:false,//同步执行
		data: {
			id: itemId
		},
		success: function(data){
			itemStatus = parseInt($.trim(data));
		}
	});
	if(itemStatus!=4){
		alert("商品不是在售状态，无法购买！");
		return;
	}
	var shopId = $("#shopId").val();//店铺id
	
	var sellerId = $("#sellerId").val();//商家id
	var skuId = $("#skuId").val();//商家id
	if(skuId==null || skuId===""){
		alert("请先选择商品的销售属性信息");
		return;
	}
	var quantity = $('.number_js').val();//购买数量
	var areaCode = $("#areaCode").val();
	var qty = $("#qty_js").val();//商品数量
	var itemPrice = $("#itemPrice_js").val();//售价
	if(itemPrice==null || typeof(itemPrice)=="undefined"){	
		alert("商品无价格不能购买");
		return;
	}
	// 增值服务
	var valueAddedSkuIds = [];
	$.each($("[name=valueAddedSkuId]"), function(i, o){
		valueAddedSkuIds.push($(o).val());
	});
	var arrShopCart = new Array();
	if(quantity>0){
		if(qty && parseInt(qty) <= 0){
			alert("该商品已售罄，无法购买");
			return;
		}
		if(parseInt(quantity) > parseInt(qty)){
			//购买数量  大于  商品数量
			alert("采购量不能大于库存量");
			return;
		}
		//校验购买的商品是否属于自己的店铺，防止刷单
		$.ajax({
			type: "POST",
			dataType: "text",
			url: $("#contextPath").val()+"/productController/checkItem",
			async:false,//同步执行
			data: {
				id: itemId
			},
			success: function(data){
				data = eval("("+data+")");
				if(data.success == true){
					alert(data.msg);
					msg=data.msg;
				}
			},
			error:function(data){
				data = eval("("+data+")");
				alert(data.msg);
				msg=data.msg;
			}
		});
		if(msg==null || msg==''|| msg==undefined){
			var shopCart = new ShopCart(shopId,itemId,quantity,skuId,sellerId,areaCode,activitesDetailsId,valueAddedSkuIds);
			arrShopCart.push(shopCart);
		}else{
			return;
		}
	}
	if(arrShopCart.length>0){
		var jsonShopCart = JSON.stringify(arrShopCart);
		//console.log("jsonShopCart==="+jsonShopCart);
		$.ajax({
			type: "POST",
			dataType: "JSON",
			url: $("#contextPath").val()+"/shopCart/promptlybuyProducts",
			async:false,//同步执行
			data: {
				products: jsonShopCart
			},
			success: function(data){
				//alert("购物车加入成功");
				if(data=="0"){
                    alert("库存不足，不支持购买！");
                    return false;
                }else if(data=="1"){
                    window.location.href=$("#contextPath").val()+"/shopCart/toOrderViewPromptly?orderType=2";
                }else{
                    alert("请重新购买！");
                    return false;
                }
			}
		});
	}else{
		alert("购物车加入失败，未购买任何商品！");
	}
}

//加入进货车
//activitesDetailsId：集采活动详情ID
function addCart(activitesDetailsId){
	var itemId = $("#itemId").val();//商品Id
	var itemStatus;
	var msg;
	$.ajax({
		type: "POST",
		dataType: "text",
		url: $("#contextPath").val()+"/productController/getItemStatus",
		async:false,//同步执行
		data: {
			id: itemId
		},
		success: function(data){
			itemStatus = parseInt($.trim(data));
		}
	});
	if(itemStatus!=4){
		alert("商品不是在售状态，无法购买！");
		return;
	}
	var shopId = $("#shopId").val();//店铺id
	
	var sellerId = $("#sellerId").val();//商家id
	var skuId = $("#skuId").val();//商家id
	if(skuId==null || skuId===""){
		alert("请先选择商品的销售属性信息");
		return;
	}
	var quantity = $('.number_js').val();//购买数量
	var areaCode = $("#areaCode").val();
	var qty = $("#qty_js").val();//商品数量
	var itemPrice = $("#itemPrice_js").val();//售价
	if(itemPrice==null || typeof(itemPrice)=="undefined"){	
		alert("商品无价格不能加入购物车,请先询价");
		return;
	}
	// 增值服务
	var valueAddedSkuIds = [];
	$.each($("[name=valueAddedSkuId]"), function(i, o){
		valueAddedSkuIds.push($(o).val());
	});
	var arrShopCart = new Array();
	if(quantity>0){
		if(qty && parseInt(qty) <= 0){
			alert("该商品已售罄，无法购买");
			return;
		}
		if(parseInt(quantity) > parseInt(qty)){
			//购买数量  大于  商品数量
			alert("采购量不能大于库存量");
			return;
		}
		//校验购买的商品是否属于自己的店铺，防止刷单
		$.ajax({
			type: "POST",
			dataType: "text",
			url: $("#contextPath").val()+"/productController/checkItem",
			async:false,//同步执行
			data: {
				id: itemId
			},
			success: function(data){
				data = eval("("+data+")");
				if(data.success == true){
					alert(data.msg);
					msg=data.msg;
				}
			},
			error:function(data){
				data = eval("("+data+")");
				alert(data.msg);
				msg=data.msg;
			}
		});
		if(msg==null || msg==''|| msg==undefined){
			var shopCart = new ShopCart(shopId,itemId,quantity,skuId,sellerId,areaCode,activitesDetailsId,valueAddedSkuIds);
			arrShopCart.push(shopCart);
		}else{
			return;
		}
	}
	if(arrShopCart.length>0){
		var jsonShopCart = JSON.stringify(arrShopCart);
		console.log(jsonShopCart);
		//console.log("jsonShopCart==="+jsonShopCart);
		$.ajax({
			type: "POST",
			dataType: "text",
			url: $("#contextPath").val()+"/shopCart/batchAdd",
			async:false,//同步执行
			data: {
				products: jsonShopCart
			},
			success: function(data){
				//alert("购物车加入成功");
				$("#addCartBomb").show();
			}
		});
	}else{
		alert("购物车加入失败，未购买任何商品！");
	}
}
function closeAddCartBomb(){
	$("#addCartBomb").hide();
}
//创建加入购物车对象
function ShopCart(shopId,itemId,quantity,skuId,sellerId,areaCode,activitesDetailsId,valueAddedSkuIds){
	this.shopId = shopId;//店铺ID
	this.itemId = itemId;//商品id
	this.quantity = quantity;//数量
	this.skuId = skuId;
	this.sellerId = sellerId;//商家id
	this.regionId = areaCode;//区域
	this.activitesDetailsId = activitesDetailsId;//集采活动ID
	this.valueAddedSkuIds = valueAddedSkuIds;//增值服务
	if (typeof sayName != "function" ){
		ShopCart.prototype.sayName= function(){};
    }
}
//减一
function minus(obj){
	var num = $(obj).siblings('input.number_js').val();
	if(parseInt(num)>1){
		$(obj).siblings("input.number_js").val(parseInt(num)-1);
		var hasPrice = $("#hasPrice").val();//是否有价格，有价格时再去加载价格、库存，无价格显示我要询价
		var price_name = $("#doAskPrice").attr("name");//用户咨询过价格，读取价格
		if(hasPrice==1 || (price_name!=null && typeof(price_name) != "undefined")){//商品有价格时加载， 我要询价 时不加载
			buildBuyListHTML();
		}
	}
}
//手动输入
function handEntry(obj){
	//延迟处理
	$(obj).val($(obj).val().replace(/\D/g,''));
//	setTimeout(function(){
//		var num = $(obj).val();
//		var qty = $("#qty_js").val();
//		if(!isNaN(num)){
//			if(parseInt(num) <= parseInt(qty)){
//				var hasPrice = $("#hasPrice").val();//是否有价格，有价格时再去加载价格、库存，无价格显示我要询价
//				var price_name = $("#doAskPrice").attr("name");//用户咨询过价格，读取价格
//				if(hasPrice==1 || (price_name!=null && typeof(price_name) != "undefined")){//商品有价格时加载， 我要询价 时不加载
//					buildBuyListHTML();
//				}
//			}else{
//				alert("采购量不能大于库存量");
//				$(obj).val("1");
//			}
//		}
//	},1000);
	var num = $(obj).val();
	var qty = $("#qty_js").val();
	if(!isNaN(num)){
		if(qty && parseInt(qty) <= 0){
			alert("该商品已售罄，无法购买");
			$(obj).val("1");
			return;
		}
		if(parseInt(num) <= parseInt(qty)){
			var hasPrice = $("#hasPrice").val();//是否有价格，有价格时再去加载价格、库存，无价格显示我要询价
			var price_name = $("#doAskPrice").attr("name");//用户咨询过价格，读取价格
			if(hasPrice==1 || (price_name!=null && typeof(price_name) != "undefined")){//商品有价格时加载， 我要询价 时不加载
				buildBuyListHTML();
			}
		}else{
			if(num !=''){
				alert("采购量不能大于库存量");
				$(obj).val("1");
			}
		}
	}
}
//加一
function plus(obj){
	var num = $(obj).siblings("input.number_js").val();
	var qty = $("#qty_js").val();
	var number = parseInt(num)+1;
	if(qty && parseInt(qty) <= 0){
		alert("该商品已售罄，无法购买");
		return;
	}
	if(number <= parseInt(qty)){
		$(obj).siblings("input.number_js").val(parseInt(num)+1);
		var hasPrice = $("#hasPrice").val();//是否有价格，有价格时再去加载价格、库存，无价格显示我要询价
		var price_name = $("#doAskPrice").attr("name");//用户咨询过价格，读取价格
		if(hasPrice==1 || (price_name!=null && typeof(price_name) != "undefined")){//商品有价格时加载， 我要询价 时不加载
			buildBuyListHTML();
		}
	} else{
		alert("采购量不能大于库存量");
	}
}
//删除
function deleteItem(obj){
	var num = $(obj).parent().parent().remove();
	buyCollect();
}

function buildBuyListHTML(){
	var shopId = $("#shopId").val();
	var itemId = $("#itemId").val();
	var skuId = $("#skuId").val();
	var areaCode = $("#areaCode").val();
	var qty = $(".number_js").val();
	var sellerId = $("#sellerId").val();//商家id
	// 增值服务
	var valueAddedSkuIds = [];
	$.each($("[name=valueAddedSkuId]"), function(i, o){
		valueAddedSkuIds.push($(o).val());
	});
	$.ajax({
		type: "POST",
		dataType: "json",
		url: $("#contextPath").val()+"/productController/getSkuPrice",
		async:false,//同步执行
		data: {
			skuId: skuId,
			qty: qty,
			shopId: shopId,
			itemId:itemId,
			areaCode: areaCode,
			sellerId: sellerId,
			valueAddedSkuIds: valueAddedSkuIds
		},
		success: function(data){
			if(data.priceObject.success){
				var price = data.priceObject.result.skuPrice;
				var disPrice = data.disPrice;
				var qty = data.priceObject.result.qty;
				var skuPrice=price.toFixed(2);
				if(skuPrice!=null && typeof(skuPrice)!="undefined"){
					if(disPrice!=null && typeof(disPrice)!="undefined"){
						var barginPrice=disPrice.toFixed(2);
						$("#cuxiaojia").show();
						$("#itemCXPrice_js").html("￥"+barginPrice);
						$("#shoujia").html("<div class=\"mid_f_right\" id=\"itemPrice_js\"><del>￥"+skuPrice+"</del></div>");
					}else{
						//隐藏促销价信息
						$("#cuxiaojia").hide();
						$("#shoujia").html('<strong class="font_fe font_16" ><span id="itemPrice_js">￥'+skuPrice+'</span></strong>');
					}
				}else{
					$("#shoujia").html('<strong class="font_fe font_16" ><span id="itemPrice_js"></span></strong>');
				}
				if(qty!=null && typeof(qty)!="undefined"){
					$("#qty_js").val(qty);
					if(parseInt(qty)>0){
						$("#qty_js").siblings("strong").html("有货（库存"+qty+"件）");
						$("#qty_js").siblings("span").show();
					}else{
						$("#qty_js").siblings("strong").html("缺货");
						$("#qty_js").siblings("span").hide();
					}
				}
				
				var imageServerAddr = $("#imageServerAddr").val();
				var skuPicUrl = data.priceObject.result.skuPicUrl;
				$("#midimg").attr("src", imageServerAddr+skuPicUrl);
				var skuPics = data.priceObject.result.skuPics;
				if(skuPics!=null && typeof(skuPics)!="undefined"){
					var picHtml = '<ul>';
					for(var i=0;i<skuPics.length;i++){
						picHtml += '<li><img src="'+imageServerAddr+''+skuPics[i].picUrl+'"/></li>';
					}
					picHtml += '</ul>';
					$("#imageMenu").empty().html(picHtml);
				}
				//图片效果重新加载
				$.getScript($("#contextPath").val()+"/assets/js/goodscenter/details.js");
				//推荐套餐
				getPackageSku();
			}
		}
	});
}
//获取活动信息
function getPromotion(ItemId){
	if(ItemId!=null && typeof(ItemId)!="undefined"){
		var shopId = $("#shopId").val();
		var skuId = $("#skuId").val();
		var sellerId = $("#sellerId").val();//商家id
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/productController/getPromotion",
			async:false,//同步执行
			data: {
				ItemId: ItemId,
				shopId: shopId,
				skuId: skuId,
				sellerId:sellerId
			},
			success: function(data){
				var promotionFullReduction = data.promotionFullReduction;
				if(promotionFullReduction!=null && typeof(promotionFullReduction)!='undefined' && promotionFullReduction.length>0){
					$("#promotion_manjian").show();
					var meetPrice = promotionFullReduction[0].meetPrice;//满足金额
					var discountPrice = promotionFullReduction[0].discountPrice;//优惠金额
					var activityName = promotionFullReduction[0].activityName;//促销名称
					//$("#promotionMsg_manjian").html("满"+meetPrice+"减"+discountPrice);
					
//					$("#promotionMsg_manjian").html(activityName);
					$("#promotionMsg_manjian").html("满 "+meetPrice+" 减 "+discountPrice);
					$("#promotionList_manjian").html("");
					for(var i=0;i<promotionFullReduction.length;i++){
						var manjian='商品总额满'+promotionFullReduction[i].meetPrice+'减'+promotionFullReduction[i].discountPrice+'元'
						$("#promotionList_manjian").append('<ul class="hei_30"><li class="wid_90 fl over_ell" title="'+promotionFullReduction[i].activityName+'">'+promotionFullReduction[i].activityName+'</li><li class="wid_160 fl over_ell" title="'+manjian+'">'+manjian+'</li> <li class="wid_170 fl" >剩余时间：<span countDown="true" serverTime="'+promotionFullReduction[i].serverTime+'" startTime="'+promotionFullReduction[i].startTime+'" noStartTip="还没有开始" endTime="'+promotionFullReduction[i].endTime+'" endTip="已结束" callBack="">正在计算时间...</span></li></ul>');
					}
				}else{
					$("#promotion_manjian").hide();
				}
				var promotionMarkdown = data.promotionMarkdown;
				if(promotionMarkdown!=null && typeof(promotionMarkdown)!='undefined' && promotionMarkdown.length>0){
					//$("#promotion_zhijiang").show();
					$("#cuxiaojia").show();
					for(var i=0;i<promotionMarkdown.length;i++){
						var disPrice=promotionMarkdown[i].disPrice.toFixed(2);
						var skuShowPrice=promotionMarkdown[i].skuShowPrice.toFixed(2);
						//设置促销价格
						$("#itemCXPrice_js").html("￥"+disPrice);
				        $("#shoujia").html("<div class=\"mid_f_right\" id=\"itemPrice_js\"><del>￥"+skuShowPrice+"</del></div>");
						/*<td class="border-1 wid_90 hei_32 font_cen">'+promotionMarkdown[i].minNum+'</td><td class="border-1 wid_90 hei_32 font_cen">'+promotionMarkdown[i].maxNum+'</td>*/
					}
				}else{
					//隐藏促销价信息
					$("#cuxiaojia").hide();
				}
				showCountDown();
			}
		});
	}
	
}
function showCountDown(){
	//查询带有countDown属性的元素
	$("[countDown]").each(function(){
		if(this.getAttribute('countDown')=="true"){
			// 服务器时间
			serverTime = new Date(this.getAttribute('serverTime'));
			var startTime = this.getAttribute('startTime');
			var noStartTip = this.getAttribute('noStartTip');
			var endTime = this.getAttribute('endTime');
			var endTip = this.getAttribute('endTip');
			var callBackFunction = this.getAttribute('callBack');
			(new Timer(this,startTime,noStartTip,endTime,endTip,callBackFunction)).countDown();
		}
		
	});
}

//控制购物车是否显示
function addCartHTML(arrAttrId, map, map_attrId_attrValueId){
	var arrAttrId_map = map.keys();
	arrAttrId_map = arrAttrId_map.sort(function(a,b){return a>b?1:-1});//数组从小到大排序
	//console.log((arrAttrId_map.toString() == arrAttrId.toString()));
	if(arrAttrId_map.toString() == arrAttrId.toString()){//判断所有的属性是否都选择了
		//[["1:11"],["2:21","2:22","2:23"],["3:31"]]
		var skuAttributes = "";
		for(var i=0;i<arrAttrId_map.length;i++){
			var attrId = arrAttrId_map[i];
			var arrAttrValueId = map_attrId_attrValueId.get(attrId);
			skuAttributes += attrId+":"+arrAttrValueId+";";
		}
		//console.log("skuAttributes=="+skuAttributes);
		//匹配 skuInfos
		var attributesMap = new Map();//属性map
		$(".attributes").each(function(){
			attributesMap.put($.trim($(this).html()), $(this).parent().index());
		});
		var skuInfosIndex = attributesMap.get(skuAttributes);
		if(skuInfosIndex!=null){
			var skuInfoObj = $("#skuInfos ul").eq(skuInfosIndex);
			var attributes = skuInfoObj.find("li.attributes").html();
			var skuId = skuInfoObj.find("li.skuId").html();
			var skuInventory = skuInfoObj.find("li.skuInventory").html();
			var skuPicUrl = skuInfoObj.find("li.skuPicUrl").html();
			var skuType = skuInfoObj.find("li.skuType").html();
			$("#skuId").val(skuId);
			getFavouriteSku();
			var hasPrice = $("#hasPrice").val();//是否有价格，有价格时再去加载价格、库存，无价格显示我要询价
			var price_name = $("#doAskPrice").attr("name");//用户咨询过价格，读取价格
			if(hasPrice==1 || (price_name!=null && typeof(price_name) != "undefined")){//商品有价格时加载， 我要询价 时不加载
				buildBuyListHTML();
			}else{
				queryInquiry(arrAttrId, map, map_attrId_attrValueId);
			}
		}
	}else{
		//$(".js_addCart").hide();
	}
}

//控制购物车是否显示(组合商品)
function addCombinationCartHTML(skuAttributes){
	//匹配 skuInfos
	var attributesMap = new Map();//属性map
	$(".attributes").each(function(){
		attributesMap.put($.trim($(this).html()), $(this).parent().index());
	});
	var skuInfosIndex = attributesMap.get(skuAttributes);
	if(skuInfosIndex!=null){
		var skuInfoObj = $("#skuInfos ul").eq(skuInfosIndex);
		var attributes = skuInfoObj.find("li.attributes").html();
		var skuId = skuInfoObj.find("li.skuId").html();
		var skuInventory = skuInfoObj.find("li.skuInventory").html();
		var skuPicUrl = skuInfoObj.find("li.skuPicUrl").html();
		var skuType = skuInfoObj.find("li.skuType").html();
		$("#skuId").val(skuId);
		getFavouriteSku();
		var hasPrice = $("#hasPrice").val();//是否有价格，有价格时再去加载价格、库存，无价格显示我要询价
		var price_name = $("#doAskPrice").attr("name");//用户咨询过价格，读取价格
		if(hasPrice==1 || (price_name!=null && typeof(price_name) != "undefined")){//商品有价格时加载， 我要询价 时不加载
			buildBuyListHTML();
		}
	}
}
//读取该sku是否已经收藏
function getFavouriteSku(){
	var itemId = $("#itemId").val();
	var skuId = $("#skuId").val();
	$.ajax({
	   type: "POST",
	   dataType: "json",
	   url: $("#contextPath").val()+"/productController/getFavouriteSku",
	   data: {
			skuId: skuId,
			itemId: itemId
	   },
	   success: function(data){
			if(data!=null && data.favouriteSku=="true"){
				$("#shoucangshangpin").html("已收藏");
				//$(".js_favourite").unbind("click");
			}else{
				$("#shoucangshangpin").html("收藏商品");
			}
	   }
	});
}
//获取省市区
function getAddress(addressId, domId){
	$.ajax({
	   type: "POST",
	   dataType: "json",
	   url: $("#contextPath").val()+"/address/query",
	   data: {
			id: addressId
	   },
	   success: function(data){
			for(var i=0;i<data.length;i++){
				var name = data[i].name;
				var code = data[i].code;
				$("#"+domId).append('<li onclick="addressSelection('+addressId+','+code+',\''+name+'\')">'+name+'</li>');
			}
	   }
	});
}
//商品属性：配送至 数据构造，默认取省下的第一个市，市下第一个区/县
function builAddress(addressId){
	var addressText = $.cookie('region');
	
	$.ajax({
		type: 'POST',
		dataType: 'json',
		url: $("#contextPath").val() + '/address/getCodeAndQualifiedName',
		data: {
			areaCode: addressId
		},
		success: function(data) {
			var areaCode = data.code;
			var qualifiedName = data.qualifiedName;
			$(".js_address").html(qualifiedName);
			$(".js_address").prop('title', qualifiedName);
			$("#areaCode").val(areaCode);
		}
	});
}
//选择省、市
function addressSelection(addressId, code, name){
	var max_code_of_province = 82;
	if(addressId==0){
		$("#Tabaddress_Content1").empty();
		getAddress(code, "Tabaddress_Content1");
		//存放省份Id
		$("#areaCode").val(code);
		$(".js_tabAddress_one").html(name);
		$(".js_tabAddress_two").html("请选择");
		$("#Tabaddress_Content0").parent().hide();
		$(".js_tabAddress_one").parent().removeClass("active").addClass("normal");
		$("#Tabaddress_Content1").parent().show();
		$(".js_tabAddress_two").parent().addClass("active").removeClass("none").removeClass("normal");
		$(".js_tabAddress_two").parent().show();
	}
	/*else if (+addressId <= max_code_of_province) {
		$("#Tabaddress_Content2").empty();
		getAddress(code, "Tabaddress_Content2");
		$(".js_tabAddress_two").html(name);
		$("#Tabaddress_Content1").parent().hide();
		$(".js_tabAddress_two").parent().removeClass("active").addClass("normal");
		$("#Tabaddress_Content2").parent().show();
		$(".js_tabAddress_three").parent().addClass("active").removeClass("none").removeClass("normal");
		$(".js_tabAddress_three").parent().show();
	}*/
	else{
		$(".js_address").html($(".js_tabAddress_one").html()/*+$(".js_tabAddress_two").html()*/+name);
		$(".js_address").attr("title", $(".js_tabAddress_one").html()/*+$(".js_tabAddress_two").html()*/+name);
		$("#areaCode").val(code);
		$(".nTab_address").hide();
		
		$.cookie('region',name,{expires: 7, path:"/"});
		$.cookie('regionCode',code,{expires: 7, path:"/"});
		$.ajax({
			url:$("#contextPath").val()+"/address/validationMallTheme",
			type:"POST",
			data:{"addressCode":code},
			dataType:"text",
			success:function(d){
				if(d=="1"){
					$("#region").text(name);
				}else{
					$("#region").text("全国");
				}
			}
		});
//		location.reload();
		//重新读取价格、库存,并计算总额
		var hasPrice = $("#hasPrice").val();//是否有价格，有价格时再去加载价格、库存，无价格显示我要询价
		if(hasPrice==1){
			buyCollect();
		}
		// 获取运送方式
		$.ajax({
		   type: "POST",
		   dataType: "json",
		   url: $("#contextPath").val()+"/productController/getDeliveryType",
		   data: {
				itemId: $('#itemId').val(),
				regionId: addressId
		   },
		   success: function(data){
			   if(data && data.length > 0){
				   var content = '<span id="defaultDeliveryType">';
				   if(data[0].deliveryType == 1){
					   content += '快递';
				   } else if(data[0].deliveryType == 2){
					   content += 'EMS';
				   } else if(data[0].deliveryType == 3){
					   content += '平邮';
				   }
				   if(data[0].groupFreight == 0){
					   content += ' 包邮';
				   } else{
					   content += ' ￥' + data[0].groupFreight.toFixed(2);
				   }
				   content += '</span><i class="demo-icons fa-caret-down"></i><ul id="store-prompt-ESM-ul" style="display: none">';
				   // 1快递，2EMS，3平邮
				   $.each(data, function(i, o){
					   if(i == 0){
						   content += '<li style="background:#fff7f1">';
					   }else{
						   content += '<li>';
					   }
					   if(o.deliveryType == 1){
						   content += '快递';
					   } else if(o.deliveryType == 2){
						   content += 'EMS';
					   } else if(o.deliveryType == 3){
						   content += '平邮';
					   }
					   if(o.groupFreight == 0){
						   content += ' 包邮';
					   } else{
						   content += ' ￥' + o.groupFreight.toFixed(2);
					   }
					   if(i == 0){
						   content += '<b class="po_ab pa_04" ></b></li>';
					   }else{
						   content += '<b class="po_ab pa_04" style="display: none" ></b></li>';
					   }
				   });
				   content += '</ul>';
				   $("#store-prompt-ESM").html(content);
				   // 初始化运送方式切换事件
				   initDeliveryTypeChangeEvent();
			   }else{
				   $("#store-prompt-ESM").html("包邮");
			   }
		   }
		});
	}
}
//跳转到店铺列表
function toShopListIndex(cid){
	$("#shopCid").val(cid);
	$("#contentForm").submit();
}

//时间转换
function formartDate(dataFormate, time) {
     var date = new Date();
     date.setTime(time);
     return date.pattern(dataFormate);
}
Date.prototype.pattern = function(fmt) {
     var o = {
          "M+" : this.getMonth() + 1, //月份    
          "d+" : this.getDate(), //日    
          "h+" : this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时    
          "H+" : this.getHours(), //小时    
          "m+" : this.getMinutes(), //分    
          "s+" : this.getSeconds(), //秒    
          "q+" : Math.floor((this.getMonth() + 3) / 3), //季度    
          "S" : this.getMilliseconds()
     //毫秒    
     };
     var week = {
          "0" : "日",
          "1" : "一",
          "2" : "二",
          "3" : "三",
          "4" : "四",
          "5" : "五",
          "6" : "六"
     };
     if (/(y+)/.test(fmt)) {
          fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
                    .substr(4 - RegExp.$1.length));
     }
     if (/(E+)/.test(fmt)) {
          fmt = fmt.replace(RegExp.$1,
                    ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "星期" : "周")
                              : "")
                              + week[this.getDay() + ""]);
     }
     if (/(e+)/.test(fmt)) {
          fmt = fmt.replace(RegExp.$1,
                    ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "星期" : "周")
                              : "")
                              + this.getDay());
     }
     for ( var k in o) {
          if (new RegExp("(" + k + ")").test(fmt)) {
               fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
                         : (("00" + o[k]).substr(("" + o[k]).length)));
          }
     }
     return fmt;
}

function closenTabAddress(){
	$(".nTab_address").hide();
}
