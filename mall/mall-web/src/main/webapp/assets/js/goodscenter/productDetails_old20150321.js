$(document).ready(function(){
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
	$(".js_attr").click(function(){
		var attrId_Name_click = $(this).parent().siblings(".attrName_js").attr("name");//属性
		var attrId = attrId_Name_click.split(":")[0];
		var attrName = attrId_Name_click.split(":")[1];
		var attrValueId_Name_click = $(this).attr("name");//属性值
		var attrValueId = attrValueId_Name_click.split(":")[0];
		var attrValueName = attrValueId_Name_click.split(":")[1];
		if($(this).hasClass("border-8")){
			$(this).removeClass("border-8").addClass("border-4");
			$(this).children().remove("b");
			var removeMap_bl = true;
			var siblingActive = $(this).siblings().each(function(){
				if($(this).hasClass("border-8")){
					removeMap_bl = false;
					return false;
				}
			});
			if(removeMap_bl){
				map.remove(parseInt(attrId));
			}
			map_attrValue.remove(parseInt(attrValueId));
			var arrAttrValueId = map_attrId_attrValueId.get(parseInt(attrId));
			if(arrAttrValueId!=null){
				for(var i=0;i<arrAttrValueId.length;i++){
					if(arrAttrValueId[i] == attrValueId){
						arrAttrValueId.splice(i,1);
					}
				}
				map_attrId_attrValueId.put(parseInt(attrId), arrAttrValueId)
			}
			var arrAttrValueName = map_attrId_attrValueName.get(parseInt(attrId));
			if(arrAttrValueName!=null){
				for(var i=0;i<arrAttrValueName.length;i++){
					if(arrAttrValueName[i] == attrValueName){
						arrAttrValueName.splice(i,1);
					}
				}
				map_attrId_attrValueName.put(parseInt(attrId), arrAttrValueName);
			}
		}else{
			$(this).removeClass("border-4").addClass("border-8");
			$(this).append('<b class="po_ab pa_04"></b>');
			map.put(parseInt(attrId), attrName);
			map_attrValue.put(parseInt(attrValueId), attrValueName);
			var arrAttrValueId = map_attrId_attrValueId.get(parseInt(attrId));
			if(arrAttrValueId!=null){
				arrAttrValueId.push(attrValueId);
			}else{
				arrAttrValueId = new Array();
				arrAttrValueId.push(attrValueId);
			}
			map_attrId_attrValueId.put(parseInt(attrId), arrAttrValueId)
			var arrAttrValueName = map_attrId_attrValueName.get(parseInt(attrId));
			if(arrAttrValueName!=null){
				arrAttrValueName.push(attrValueName);
			}else{
				arrAttrValueName = new Array();
				arrAttrValueName.push(attrValueName);
			}
			map_attrId_attrValueName.put(parseInt(attrId), arrAttrValueName)
		}
		//console.log("map===="+map.keys()+"      "+map.values());
		//console.log("map_attrValue===="+map_attrValue.keys()+"      "+map_attrValue.values());
		//console.log("map_attrId_attrValueId===="+map_attrId_attrValueId.keys()+"      "+map_attrId_attrValueId.values());
		//console.log("map_attrId_attrValueName===="+map_attrId_attrValueName.keys()+"      "+map_attrId_attrValueName.values());
		//console.log("-----------------------------------------");
		var arrAttrId_map = map.keys();
		arrAttrId_map = arrAttrId_map.sort(function(a,b){return a>b?1:-1});//数组从小到大排序
		//console.log((arrAttrId_map.toString() == arrAttrId.toString()));
		if(arrAttrId_map.toString() == arrAttrId.toString()){//判断所有的属性是否都选择了
			//[["1:11"],["2:21","2:22","2:23"],["3:31"]]
			var skuId = new Array();
			for(var i=0;i<arrAttrId_map.length;i++){
				var attrId = arrAttrId_map[i];
				var arrAttrValueId = map_attrId_attrValueId.get(attrId);
				var skuId_chil = new Array();
				for(var j=0;j<arrAttrValueId.length;j++){
					skuId_chil.push(attrId+":"+arrAttrValueId[j]+";")
				}
				skuId.push(skuId_chil);
			}
			var jsonText = JSON.stringify(skuId);
			//匹配 skuInfos
			var attributesMap = new Map();//属性map
			$(".attributes").each(function(){
				attributesMap.put($.trim($(this).html()), $(this).parent().index());
			});
			$.ajax({
			   type: "POST",
			   dataType: "json",
			   url: $("#contextPath").val()+"/productController/skuDescartes",
			   data: {
					jsonStr: jsonText
			   },
			   success: function(data){
					$(".js_buyList").empty();//先清空列表
					//购买列表标题
					if(typeof($("#title_js").html()=="undefined")){
						var titleHTML = buildBuyListTitleHTML(arrAttrName);
						$(".js_buyList").append(titleHTML);
					}
					//购买汇总清零
					$(".collectNum_js").html("0");
					$(".collectMoney_js").html("0");
					for(var i=0;i<data.length;i++){
						var sku = data[i];
						//console.log(sku);
						var skuInfosIndex = attributesMap.get(sku);
						if(skuInfosIndex!=null){
							var skuInfoObj = $("#skuInfos ul").eq(skuInfosIndex);
							var attributes = skuInfoObj.find("li.attributes").html();
							var skuId = skuInfoObj.find("li.skuId").html();
							var skuInventory = skuInfoObj.find("li.skuInventory").html();
							var skuPicUrl = skuInfoObj.find("li.skuPicUrl").html();
							var skuType = skuInfoObj.find("li.skuType").html();
							var arrAttributes = sku.split(";");
							var arrShowNames = new Array();
							for(var k=0;k<arrAttributes.length;k++){
								//读取的数据格式：1:12;2:22;3:32;
								if(arrAttributes[k]!=null && arrAttributes[k].length>0){
									arrShowNames.push(map_attrValue.get(arrAttributes[k].split(":")[1]));
								}
							}
							var buyListHTML = buildBuyListHTML(arrShowNames,skuId,skuInventory,skuPicUrl,skuType);
							$(".js_buyList").append(buyListHTML);
							$(".js_buyList").show();
							$(".js_buyAll").show();
							$(".js_addCart").show();
							buyCollect();
						}else{
							var arrAttributes = sku.split(",");
							var arrShowNames = new Array();
							for(var k=0;k<arrAttributes.length;k++){
								//读取的数据格式：1:12,2:22,3:32
								arrShowNames.push(map_attrValue.get(arrAttributes[k].split(":")[1]));
							}
							var buyListHTML = buildBuyListHTML_gray(arrShowNames,skuId,skuInventory,skuPicUrl,skuType);
							$(".js_buyList").append(buyListHTML);
							$(".js_buyList").show();
							$(".js_buyAll").show();
							$(".js_addCart").show();
						
						}
					}
			   }
			});
		}else{
			$(".js_buyList").hide();
			$(".js_buyAll").hide();
			$(".js_addCart").hide();
		}
	});
	//商品收藏
	$(".js_favourite").click(function(){
		var shopId = $("#shopId").val();//店铺id
		var itemId = $("#itemId").val();//商品Id
		var sellerId = $("#sellerId").val();//商家id
		var itemName = $("#itemName").val();//商品名称
		var price = $("#marketPrice").val();//商品指导价格
		var pictureUrl;
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/favourite/addItem",
			data: {
				shopId: shopId,
				itemId: itemId,
				sellerId: sellerId,
				itemName: itemName,
				price: price,
				pictureUrl: pictureUrl
			},
			success: function(data){
				
				alert("收藏成功");
				
			}
		});
	});
	//我要询价
	$("#doAskPrice").click(function(){
		$("#xunjia").show();
	});
	$(".js_xunjia").click(function(){
		var regMobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
		var regEmail = /^([0-9a-z_\.-]+)@([0-9a-z\.-]+)\.([a-z]{2,6})$/;
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
				
		$(".errorMsg").html("");
		$.ajax({
			type: "POST",
			dataType: "json",
			url: $("#contextPath").val()+"/productController/addInquiry",
			data: {
				email: email,
				cellphone: cellphone,
				qty: qty,
				comment: comment
			},
			success: function(data){
				if(data.success){
					alert("询价提交成功");
					$(".cancel").click();
				}
			}
		});
	});
});
//购买汇总
function buyCollect(){
	var collectNum = 0;//总数量
	var collectPrice = 0;//总价格
	$("ul.product_js").each(function(){
		var num = $(this).children().find('input.number_js').val();//购买数量
		var qty = $(this).children().filter('.qty').html();//商品数量
		if(num>0){
			if(parseInt(num) <= parseInt(qty)){
				collectNum = collectNum + parseInt(num);
				//var skuId = $(this).children().find('input[name="skuId"]').val();
				var price = $.trim($(this).children('li.skuPrice').html());//读取价格
				collectPrice = collectPrice + (num * parseInt(price)); 
			}else{
				alert("采购量不能大于库存量");
			}
		}
	});
	$(".collectNum_js").html(collectNum);
	$(".collectMoney_js").html(collectPrice);
}
//加入进货车
function addCart(){
	var shopId = $("#shopId").val();//店铺id
	var itemId = $("#itemId").val();//商品Id
	var sellerId = $("#sellerId").val();//商家id
	var arrShopCart = new Array();
	$("ul.product_js").each(function(){
		var quantity = $(this).children().find('input.number_js').val();//购买数量
		if(quantity>0){
			var skuId = $(this).children().find('input[name="skuId"]').val();
			var shopCart = new ShopCart(shopId,itemId,quantity,skuId,sellerId);
			arrShopCart.push(shopCart);
		}
	});
	if(arrShopCart.length>0){
		var jsonShopCart = JSON.stringify(arrShopCart);
		console.log("jsonShopCart==="+jsonShopCart);
		$.ajax({
			type: "POST",
			dataType: "text",
			url: $("#contextPath").val()+"/shopCart/batchAdd",
			async:false,//同步执行
			data: {
				products: jsonShopCart
			},
			success: function(data){
				
				alert("购物车加入成功");
				
			}
		});
	}else{
		alert("购物车加入失败，未购买任何商品！");
	}
}
//创建加入购物车对象
function ShopCart(shopId,itemId,quantity,skuId,sellerId){
	this.shopId = shopId;//店铺ID
	this.itemId = itemId;//商品id
	this.quantity = quantity;//数量
	this.skuId = skuId;
	this.sellerId = sellerId;//商家id
	if (typeof sayName != "function" ){
		ShopCart.prototype.sayName= function(){};
    }
}
//减一
function minus(obj){
	var num = $(obj).siblings('input.number_js').val();
	if(parseInt(num)!=0){
		$(obj).siblings("input.number_js").val(parseInt(num)-1);
	}
	buyCollect();
}
//手动输入
function handEntry(obj){
	//延迟处理
	$(obj).val($(obj).val().replace(/\D/g,''));
	setTimeout(function(){
		var num = $(obj).val();
		var qty = $(obj).parent().parent().siblings("li.qty").html();
		if(!isNaN(num)){
			if(parseInt(num) <= parseInt(qty)){
				buyCollect();
			}else{
				alert("采购量不能大于库存量");
			}
			
		}
	},1000);
}
//加一
function plus(obj){
	var num = $(obj).siblings("input.number_js").val();
	var qty = $(obj).parent().parent().siblings("li.qty").html();
	var number = parseInt(num)+1;
	if(number <= parseInt(qty)){
		$(obj).siblings("input.number_js").val(parseInt(num)+1);
		buyCollect();
	}
}
//删除
function deleteItem(obj){
	var num = $(obj).parent().parent().remove();
	buyCollect();
}
function buildBuyListTitleHTML(arrAttrName){
	var titleHTML = '<ul id="title_js" class="font_14b bg_05 border-3">';
		for(var i=0;i<arrAttrName.length;i++){
			titleHTML += '<li>'+arrAttrName[i]+'</li>';
		}
		titleHTML += '<li>单价</li>';
		titleHTML += '<li>库存量</li>';
		titleHTML += '<li>采购量</li>';
		titleHTML += '<li>操作</li>';
		titleHTML += '</ul>';
	return titleHTML;
}
function buildBuyListHTML(arrShowNames,skuId,skuInventory,skuPicUrl,skuType){
	var buyListHTML;
	//skuInventory 库存
	var shopId = $("#shopId").val();
	var itemId = $("#itemId").val();
	var regionCode = $.cookie('regionCode');
	$.ajax({
		type: "POST",
		dataType: "json",
		url: $("#contextPath").val()+"/productController/getSkuPrice",
		async:false,//同步执行
		data: {
			skuId: skuId,
			qty: 1,
			shopId: shopId,
			itemId:itemId,
			areaCode: regionCode
		},
		success: function(data){
			if(data.success){
				var skuPrice = data.result.skuPrice;
				var qty = data.result.qty;
				console.log("skuPrice==="+skuPrice);
				if(skuPrice!=null && typeof(skuPrice)!="undefined"){
					buyListHTML = '<ul class="product_js">';
					for(var i=0;i<arrShowNames.length;i++){
					buyListHTML += '	<li>'+arrShowNames[i]+'</li>';
					}
					buyListHTML += '	<li class="skuPrice">'+skuPrice+'</li>';
					if(qty!=null && typeof(qty)!="undefined"){
						buyListHTML += '	<li class="qty">'+qty+'</li>';
					}else{
						buyListHTML += '	<li class="qty">0</li>';
					}
					buyListHTML += '	<li>';
					buyListHTML += '		<div class="wrap-input">';
					buyListHTML += '			<input name="skuId" value="'+skuId+'" type="hidden">';
					buyListHTML += '			<a class="btn-reduce border-4" href="javascript:void(0);" onclick="minus(this)">-</a>';
					buyListHTML += '			<input value="1" class="text border-4 number_js" onkeyup="handEntry(this);">';
					buyListHTML += '			<a class="btn-add border-4" href="javascript:void(0);" onclick="plus(this)">+</a>';
					buyListHTML += '		</div>';
					buyListHTML += '	</li>';
					buyListHTML += '	<li><a href="javascript:void(0);" onclick="deleteItem(this)">删除</a></li>';
					buyListHTML += '</ul>';
				}
			}
		}
	});
	//获取活动价
	/*
	$.ajax({
		type: "POST",
		dataType: "json",
		url: $("#contextPath").val()+"/productController/getPromotion",
		async:false,//同步执行
		data: {
			skuid: 1
		},
		success: function(data){
		}
	});
	*/
	return buyListHTML;
}
function buildBuyListHTML_gray(arrShowNames,skuId,skuInventory,skuPicUrl,skuType){
	//skuInventory 库存
	var buyListHTML = '<ul class="product_js">';
		for(var i=0;i<arrShowNames.length;i++){
		buyListHTML += '	<li>'+arrShowNames[i]+'</li>';
		}
		buyListHTML += '	<li>--</li>';
		buyListHTML += '	<li>--</li>';
		buyListHTML += '	<li>';
		buyListHTML += '		<div class="wrap-input">';
		buyListHTML += '			<input name="skuId" value="'+skuId+'" type="hidden">';
		buyListHTML += '			<a class="btn-reduce border-4" href="javascript:void(0);">-</a>';
		buyListHTML += '			<input value="0" class="text border-4 number_js" disabled="disabled">';
		buyListHTML += '			<a class="btn-add border-4" href="javascript:void(0);">+</a>';
		buyListHTML += '		</div>';
		buyListHTML += '	</li>';
		buyListHTML += '	<li><a href="javascript:void(0);" onclick="deleteItem(this)">删除</a></li>';
		buyListHTML += '</ul>';
	return buyListHTML;
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
//选择省、市
function addressSelection(addressId, code, name){
	if(addressId==0){
		$("#Tabaddress_Content1").empty();
		getAddress(code, "Tabaddress_Content1");
		$(".js_tabAddress_one").html(name);
		$(".js_tabAddress_two").html("请选择");
		$("#Tabaddress_Content0").parent().hide();
		$(".js_tabAddress_one").parent().removeClass("active").addClass("normal");
		$("#Tabaddress_Content1").parent().show();
		$(".js_tabAddress_two").parent().addClass("active").removeClass("none").removeClass("normal");
		$(".js_tabAddress_two").parent().show();
	}else{
		$(".js_tabAddress_two").html(name);
		$(".js_address").html($(".js_tabAddress_one").html()+name);
		$(".nTab_address").hide();
	}
}