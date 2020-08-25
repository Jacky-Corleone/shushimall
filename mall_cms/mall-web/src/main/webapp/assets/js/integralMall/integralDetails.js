$(document).ready(function(){
	$("#shop_left dl dd").click(function(){
		if($(this).children("i").hasClass("fa-angle-double-up")){
			$(this).children("i").removeClass("fa-angle-double-up").addClass("fa-angle-double-down");
			$(this).next("ul").show();
		}else{
			$(this).children("i").removeClass("fa-angle-double-down").addClass("fa-angle-double-up");
			$(this).next("ul").hide();
		}
	});
	//省份的id
	$("#areaCode").val($.cookie('regionCode'));
	$(".js_guanqu").click(function(){
		$(".nTab_address").toggle();
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
	//商品属性点击 attrName_js
	var hasPrice = $("#hasPrice").val();//是否有价格，无价格显示我要询价
	if(!map.isEmpty()){
		//console.log("hasPrice="+hasPrice);
		var itemStatus = $("#itemStatus").val();//商品状态
		//console.log("销售状态itemStatus="+itemStatus);
		if(itemStatus==4){
			if(hasPrice==1 ){//商品有价格时加载， 我要询价 时不加载
				addCartHTML(arrAttrId, map, map_attrId_attrValueId);
			}else{
				queryInquiry(arrAttrId, map, map_attrId_attrValueId);
			}
		}
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
				// 是否为集采商品
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
	function buildBuyListHTML(){
		var shopId = $("#shopId").val();
		var itemId = $("#itemId").val();
		var skuId = $("#skuId").val();
		var areaCode = $("#areaCode").val();
		var qty = $(".number_js").val();
		var sellerId = $("#sellerId").val();//商家id
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
				sellerId: sellerId
			},
			success: function(data){
				if(data.priceObject.success){
					var price = data.priceObject.result.skuPrice;
					var disPrice = data.disPrice;
					var qty = data.priceObject.result.qty;
					var skuPrice=price.toFixed(2);
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
				}
			}
		});
	}
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
});

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
//购买汇总
function buyCollect(){
	//重新读取价格、库存
	buildBuyListHTML();
	//var num = $('.number_js').val();//购买数量
	//var qty = $("#qty_js").val();//商品数量

	
}
function closeAddCartBomb(){
	$("#addCartBomb").hide();
}
//减一
function minus(obj){
	var num = $("#buy_item_count").val();
	if(parseInt(num)>1){
		$("#buy_item_count").val(parseInt(num)-1);
	}
}
//手动输入
function handEntry(obj){
	//延迟处理
	$(obj).val($(obj).val().replace(/\D/g,''));
	var num = $(obj).val();
	var qty = $("#qty_js").val();
	if(!isNaN(num)){
		if(parseInt(num) <= parseInt(qty)){
		}else{
			alert("采购量不能大于库存量");
			$(obj).val("1");
		}
	}
}
//加一
function plus(obj){
	var num =$("#buy_item_count").val();
	var qty = $("#qty_js").val();
	var number = parseInt(num)+1;
	if(number <= parseInt(qty)){
		$("#buy_item_count").val(parseInt(num)+1);
	} else{
		alert("采购量不能大于库存量");
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
//跳转到店铺列表
function toShopListIndex(cid){
	$("#shopCid").val(cid);
	$("#contentForm").submit();
}
/**
 * 立即兑换
 */
function immediateExchange(){
	// 校验用户积分是否充足
	$.ajax({
	   type: "POST",
	   dataType: "json",
	   url: $("#contextPath").val() + "/shopCart/validateIntegral",
	   data: {
			id: $("[name=id]").val(),
			integralType: $("[name=integralType]").val()
	   },
	   success: function(data){
			if(data){
				if(data.success){
					$("#immediateExchangeForm").submit();
				} else{
					alert(data.errorMessages[0]);
					return false;
				}
			} else{
				alert("服务器异常");
				return false;
			}
	   }
	});
}