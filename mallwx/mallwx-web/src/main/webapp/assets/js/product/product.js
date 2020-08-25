var skusVertx;
var _attrt;
var _attrtcity;
var _evalOnlyOne;
var _shopId="";
var _proFR;
var valueidArray = new Array();
var attrIdArray = new Array();



//var attr_id=$(_this).attr("attrid");
//$(".js_attr[attrid='"+attr_id+"']").attr("_selected",false).removeClass("border_e3").addClass("border_ee");
//$(_this).attr("_selected", "true").removeClass("border_ee").addClass("border_e3");
$(function() {
	_product_content=Handlebars.compile($("#product_content").html());
	_attrt=Handlebars.compile($("#product_attr").html());
	//增加城市选择弹出层
	_attrtcity=Handlebars.compile($("#product_attrcity").html());
	_evalOnlyOne=Handlebars.compile($("#detail_eval").html());
	_proFR=Handlebars.compile($("#detail_proFR").html());
	reloadDetail(_skuId);
});

$(document).ready(function(){
      setTimeout(addback,1000);
});

function addback(){
	var lasturl = document.referrer;
	var _param={}
    _param.id="gobackDetail";
    _param.callback=function(){
    	if(document.referrer.indexOf('toSearch') == -1){
    		//不刷新返回
    		//history.back(-1);
    		//刷新返回
    		window.location.href=lasturl;
    	}else{
    		//关键字
        	var searchContent=decodeURI(window.location.href.split('keyword=')[1]);
        	var fleshUrl="search/toSearch";
        	var _form = $("<form></form>");
        	_form.attr('action',fleshUrl);
        	input1 = $("<input type='hidden' name='content' />");
        	input1.attr("value",searchContent);
        	input2 = $("<input type='hidden' name='searchType' />");
        	input2.attr("value",0);
        	_form.attr('method','post');
        	_form.css('display','none');
        	_form.append(input1);
        	_form.append(input2);
        	_form.appendTo("body");
        	_form.submit();
    	}
    }
    registerGoBackte(_param);
}

function registerGoBackte(_option){
    //return;//屏蔽掉
    var _key=_option.key;//预留参数key
    var _fun=_option.callback;//返回时的回调
    var _param={};
    _param.key=_key;
    history.pushState(_param,"nothing");
    window.onpopstate=function(e){
        _fun();
        window.onpopstate=null;
    }
    if(_option.fires){//绑定多个触发回调的事件
        for(var _i=0;_i<_option.fires.length;_i++){
            var _fire=(_option.fires)[_i];
            var _e=_fire.event||'click';
            $(_fire.selector).unbind(_e);
            $(_fire.selector).bind(_e,function(){
                history.back();
            })
        }
    }
    if(_option.selector){//绑定一个触发回调的事件
        var _e=_option.event||'click';
        $(_option.selector).unbind(_e);
        $(_option.selector).bind(_e,function(){
            history.back();
        })
    }
}

function gotoShop(_shopId){
	window.location="shopBaseInfoController/toView?shopId="+_shopId;
}
function reloadDetail(__skuId){
	// 加载商品详细信息
	$.ajax({
		url : 'product/details2',
		data : {
			"id" : _itemId,
			"skuId" : __skuId
		},
		type : 'post',
		dataType:"json",
		success : function(res) {
			$("#wrapper").html(_product_content(res));
			$("#attrWindow").html(_attrt(res));
			var nicai = "";
			for(var i = 0;i<valueidArray.length;i++){
				$("button[valueid="+valueidArray[i]+"]").attr("_selected", "true").removeClass("border_ee").addClass("border_e3").attr("onclick","");
			}
			skusVertx=res.skusVertx;
			loadComplete(res);
		},
		error : function() {
			console.log("网络错误");
		}
	});

}

//sku弹出层
function show_shop_forword(){;
 
	show_shop();
}
function show_shop_back(){
	show_hide();
}
function show_shop() {
	$("#attrWindow").removeClass("slidup outdown").addClass("sliddown");
}
function show_hide() {
	$("#attrWindow").removeClass("sliddown").addClass("slidup");
}


//运费弹出层
function show_shop_forword_ctiy(){
	
	show_shop_ctiy();
}
function show_shop_back_ctiy(){
	show_hide_ctiy();
}
function show_shop_ctiy() {
	$("#attrWindowcity").removeClass("slidup1 outdown1").addClass("sliddown1");
	// 加载商品详细信息
	$.ajax({
		url : 'product/details2',
		data : {
			"id" : _itemId,
			"skuId" : _skuId
		},
		type : 'post',
		dataType:"json",
		success : function(res) {
		//	console.log(JSON.stringify(res));
		$("#wrapper").html(_product_content(res));
			//$("#attrWindow").html(_attrt(res));
			//$("#main").html(res);
			$("#attrWindowcity").html(_attrtcity(res));
			skusVertx=res.skusVertx;
			loadComplete(res);
		},
		error : function() {
			console.log("网络错误");
		}
	});
}
function show_hide_ctiy() {
	$("#attrWindowcity").removeClass("sliddown1").addClass("slidup1");
	var province_select=$("#province_select").val();
	var province_name=$("#province_select").find("option:selected").text();
	var city_name=$("#city_select").find("option:selected").text();
	var area_name=$("#area_select").find("option:selected").text();
	// 加载商品详细信息
	$.ajax({
		url : 'product/details2',
		data : {
			"id" : _itemId,
			"skuId" : _skuId,
			"province_select" :province_select,
			"city_name" :city_name,
			"province_name" :province_name,
			"area_name" :area_name
		},
		type : 'post',
		dataType:"json",
		success : function(res) {
		//	console.log(JSON.stringify(res));
		$("#wrapper").html(_product_content(res));
			//$("#attrWindow").html(_attrt(res));
			//$("#main").html(res);
			$("#attrWindowcity").html(_attrtcity(res));
			skusVertx=res.skusVertx;
			loadComplete(res);
		},
		error : function() {
			console.log("网络错误");
		}
	});
	
}

function addToCart() {
	var attrs = $(".js_attr");
	var _length = attrs.length;
	var keyArray=new Array();
	for ( var i = 0; i < _length; i++) {
		// console.log($(attrs[i]).attr("attrid")+"---");
		var _attr = $(attrs[i]);
		var selected = _attr.attr("_selected");
		if (selected === "true") {
			var _key = _attr.attr("attrid") + ":" + _attr.attr("valueid");
			keyArray.push(_key);
		}
	}
	keyArray.sort();
	var _key="";
	for(var i=0;i<keyArray.length;i++){
		_key=_key+keyArray[i]+";";
	}
	if(keyAndSkus[_key]){
		addCart(keyAndSkus[_key]);
	}else{
		toast("请选择所有属性");
		show_shop_forword();
	}
}
function addCart(skuId){
	var itemId = _itemId;//$("#itemId").val();//商品Id
	var itemStatus="";
	var _msg=null;
	$.ajax({
		type: "POST",
		dataType: "text",
		url: "product/getItemStatus",
		async:false,//同步执行
		data: {
			id: itemId
		},
		success: function(data){
			itemStatus = parseInt($.trim(data));
		},
		error:function(xhr,errmsg,obj){
			_msg=errmsg;
		}
	});
	if(_msg != null){
		alert("网络情况不好，请稍后再试");
		return;
	}
	if(itemStatus!=4){
		alert("商品不是在售状态，无法购买！");
		return;
	}
	var shopId = $("#shopId").val();//店铺id
	
	var sellerId = $("#sellerId").val();//商家id
	var quantity = $('#number_js').val();//购买数量
	var areaCode = $("#areaCode").val();
	var qty = $("#qty_js").val();//商品数量
	var itemPrice = $("#itemPrice_js").val();//售价
	if(itemPrice==null || typeof(itemPrice)=="undefined"){	
		alert("商品无价格不能加入购物车,请先询价");
		return;
	}
	var arrShopCart = new Array();
	if(quantity>0){
		if(parseInt(quantity) > parseInt(qty)){
			//购买数量  大于  商品数量
			alert("采购量不能大于库存量");
			return;
		}
		var shopCart ={}; //new ShopCart(shopId,itemId,quantity,skuId,sellerId,areaCode);
		shopCart.shopId=shopId;
		shopCart.itemId=itemId;
		shopCart.quantity=quantity;
		shopCart.skuId=skuId;
		shopCart.sellerId=sellerId;
		shopCart.regionId=areaCode;
		arrShopCart.push(shopCart);
	}
	//console.log(JSON.stringify(arrShopCart));
	if(arrShopCart.length>0){
		var jsonShopCart = JSON.stringify(arrShopCart);
		//console.log("jsonShopCart==="+jsonShopCart);
		addLoadMask();
		$.ajax({
			type: "POST",
			dataType: "text",
			url: "cart/batchAdd",
			async:false,//同步执行
			data: {
				products: jsonShopCart
			},
			success: function(data){
				toast("购物车加入成功");
				//window.location.href=window.location.href;
				//$("#addCartBomb").show();
				jQuery(function($) {  
					// 加载购物车信息
					$.ajax({
						url : 'user/shocar',
						type : 'post',
						dataType:"json",
						success : function(res) {
							//console.log(JSON.stringify(res));
							if(res.quantity>0){
								 $("#cart_tjdd").html(res.quantity);
								 $("#cart_tjdd").show();
							}else{
								$("#cart_tjdd").hide();
							} 
						},
						error : function() {
							console.log("网络错误");
						}
					});
				});  
				removeLoadMask();	
			}
		});
	}else{
		alert("购物车加入失败，未购买任何商品！");
	}
}
function add2cart(skuId){
	$.ajax({
		url:'cart/add2Cart',
		data:{"skuId":skuId},
		type:'post',
		dataType: "json",
		success:function(res){
			//var resJ=JSON.parse(res);
			console.log(res);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert(JSON.stringify(XMLHttpRequest)+"      ,    "+textStatus+"   ,    "+errorThrown);
		}
	});
}


var skuAndValueOjbectList = new Array();
var keyAndSkus = new Object();
// 信息加载完之后要做的事情
function loadComplete(res) {
	// 轮播图初始化
	var mySwiper = new Swiper('.swiper-container', {
		pagination : '.swiper-pagination',
		loop : false,autoplay : 5000,
		paginationClickable : true,
		paginationBulletRender: function (index, className) {
			return '<span style="border: 2px #fff solid;opacity: 0.7" class="' + className + '">'+ '</span>';
		}
	});
	var _length = skusVertx.length;

	for ( var i = 0; i < _length; i++) {
		var keyStr = skusVertx[i].attributes;
		var keyArray = keyStr.split(";");
		keyArray.sort();
		var _str = "";
		for ( var j = 0; j < keyArray.length; j++) {
			if (_str + keyArray[j] != "") {
				_str = _str + keyArray[j] + ";";
			}
		}
		keyAndSkus[_str] = skusVertx[i].skuId;
	}
	_skuId=res.skuId;
	_shopId=res.shopInfo.shopId;
	loadEval();
	//加载活动
	if(_skuId != ""){
		loadPromotion();
	}
	$("#_btns_").show();
	//console.log(JSON.stringify(keyAndSkus));
}

// 点击某个销售属性的值
function chooseAttr(_this) {
	var attr_id=$(_this).attr("attrid");
	var key = true;
	for(var i = 0;i<attrIdArray.length;i++){
		if(attr_id==attrIdArray[i]){
			valueidArray[i]=$(_this).attr("valueid");
			key = false;
		}
	}
	if(key){
		attrIdArray[attrIdArray.length]=attr_id;
		valueidArray[valueidArray.length]=$(_this).attr("valueid");
	}
	$(".js_attr[attrid='"+attr_id+"']").attr("_selected",false).removeClass("border_e3").addClass("border_ee");
	$(_this).attr("_selected", "true").removeClass("border_ee").addClass("border_e3");
	
	var attrs = $(".js_attr");
	var _length = attrs.length;
	var keyArray=new Array();
	for ( var i = 0; i < _length; i++) {
		// console.log($(attrs[i]).attr("attrid")+"---");
		var _attr = $(attrs[i]);
		var selected = _attr.attr("_selected");
		if (selected === "true") {
			var _key = _attr.attr("attrid") + ":" + _attr.attr("valueid");
			keyArray.push(_key);
		}
	}
	keyArray.sort();
	var _key="";
	for(var i=0;i<keyArray.length;i++){
		_key=_key+keyArray[i]+";";
	}
	//console.log(keyAndSkus[_key]);
	if(keyAndSkus[_key]){
		//重新加载页面
		//product/toDetail
		//window.location="product/toDetail?id="+_itemId+"&skuId="+keyAndSkus[_key];
		reloadDetail(keyAndSkus[_key]);
	}
}

//改变数量
function addQty(δ){
	var shopId = $("#shopId").val();
	var itemId = $("#itemId").val();
	var skuId = $("#skuId").val();
	var areaCode = $("#areaCode").val();
	var _storage=$("#qty_js").html();//库存
	var qty = $("#number_js").val();//这个变量是数量
	qty=parseInt(qty)+δ;
	if(qty<1){
		return;
	}
	if(qty>parseInt(_storage)){
		alert("库存不足");
		return;
	}
	var sellerId = $("#sellerId").val();//商家id
	if(isNaN(qty)){
		qty=1;
	}
	$("#number_js").val(qty);
	var data={
			skuId: skuId,
			qty: qty,
			shopId: shopId,
			itemId:itemId,
			areaCode: areaCode,
			sellerId: sellerId
		};
	loadQty(data);
}
function loadQty(data){
	$.ajax({
		type: "POST",
		dataType: "json",
		url: "product/getSkuPrice",
		data: data,
		success:function(resObj){
			//console.log(resObj);
			if(resObj.success){
				var skuPrice = resObj.result.skuPrice;
				var qty = resObj.result.qty;//这个qty是库存
				//alert(skuPrice+"   "+qty);
				$("#qty_js").html(qty);
				$("[qty_js]").html(qty);
				$("[guidPrice]").html("￥"+skuPrice);
				//$("attrimgid").attr('src')
			}
		}
	});
}

//加载一条评论
function loadEval(){
	var _params={};
	_params.itemId=_itemId;
	_params.skuId=_skuId;
	_params.rows=1;
	$.ajax({
		url:'product/getItemEvaluationJson2',
		type:'post',data:_params,dataType:'json',
		success:function(res){
			$("#proEvalListOne").html(_evalOnlyOne(res));
			$("#evalCount").html("("+res.pager.totalCount+")");
		}
	});
}
//加载活动
function loadPromotion(){
	var _params={};
	_params.shopId=_shopId;
	_params.skuId=_skuId;
	$.ajax({
		url:'product/getPromotion',
		type:'post',data:_params,dataType:'json',
		success:function(res){
			//console.log(res);
			var _content="";
			//满减
			var promotionFullReduction = res.promotionFullReduction;
			if(promotionFullReduction){
				_content=_content+_proFR(res);
			}
			
			//直降
			var promotionMarkdown = res.promotionMarkdown;
			if(promotionMarkdown){
				
			}
			if(_content==""){
				_content="无";
			}
			$("#_promotion").html(_content);
		}
	});
}
function validateProQtyKeydonw(_this,_event){
	var proNumInput=$(_this);
	proNumInput.val(proNumInput.val().replace(/\D/g, ''));
	proNumInput.attr('previousValue',proNumInput.val());
}
var _timeout;
function validateProQty(_this,_event){
	var proNumInput=$(_this);
	var previousValue=proNumInput.attr('previousValue');
	proNumInput.val(proNumInput.val().replace(/\D/g, ''));
	var storeNum=parseInt($("#qty_js").val());
	if(parseInt(proNumInput.val())>storeNum){
		alert("库存不足");
		proNumInput.val(storeNum);
	}
	if(!(previousValue == proNumInput.val())){
		clearTimeout(_timeout);
		_timeout=setTimeout(addQty(0),200);
	}
}
//跳转到商品评价页面
function showMoreEval(){
	window.location="product/toEval?itemId="+_itemId+"&skuId="+$("#skuId").val();
}




//跳转到
function gotoShop(_shopId){
	if(_shopId=="" || _shopId == undefined || _shopId == null){return;}
	window.location="shopBaseInfoController/toView?shopId="+_shopId;
}

function gotoCart(){
	window.location="cart/tocart";
}
var _gix="";
Handlebars.registerHelper('putGix', function(obj){
	_gix=obj;
	return "";
});
Handlebars.registerHelper('getGix', function(obj){
	return _gix+obj;
});
Handlebars.registerHelper('getPicUrl0', function(urls){
	if(urls){
	return urls[0];}else{return "";}
});

Handlebars.registerHelper('tojson', function(obj){
	console.log("tojson++++   "+ JSON.stringify(obj));
	return "";
});
Handlebars.registerHelper('changeUrl', function(obj,gix){
	var tarUrl='src="'+gix;
	if(obj){
		return obj.replace(/src="/g,tarUrl);
	}else{
		return "";
	}
});


//商品收藏


function js_favourite(){
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
	kltconfirm("是否确认收藏商品？",function(){
   	    //跑后台删除询价信息
   	    
   	    $.ajax({
   	        type: "POST",
   	        dataType: "html",  
   	        url : 'product/addItem',
   	      data: {
				shopId: shopId,
				itemId: itemId,
				sellerId: sellerId,
				itemName: itemName,
				skuId: skuId,
				price: price,
				pictureUrl: pictureUrl
			},
   	        success: function (data) {
   	            toast(data);
   	            $("#shoucangshangpin").html("已收藏");
   	        }
   	    });
	});
}

	
	function xjts(){
		
		alert("该功能暂不支持手持设备，请使用电脑登录www.printhome.com进行询价！");
		
	}
	
	
	
	//增加城市选择级联功能
	function js_getAddress(){
		getAddress(0, "province_select");
		$("#province_select").change( function() {
			$('#city_select option:first').siblings().remove();
			$('#area_select:first').siblings().remove();
			getAddress($("#province_select").val(), "city_select");		
		});
		//获取区/县
		$("#city_select").change( function() {
			$('#area_select option:first').siblings().remove();
			getAddress($("#city_select").val(), "area_select");		
		});
	};
	function getAddress(addressId, domId){
		$.ajax({
		   type: "POST",
		   dataType: "json",
		   url: "orderWx/query",
		   data: {
				id: addressId
		   },
		   success: function(data){
			   $(data).each(function(i,obj){
					$("#"+domId).append('<option value="'+obj.code+'">'+obj.name+'</option>');
			   });
		   }
		});
	}

