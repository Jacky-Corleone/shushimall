//加载页面之后，根据登录的用户，加载购物车中的数据
$(function() {
	// 加载购物车商品
	$.ajax({
		url : 'cart/getCartProducts',
		type : 'post',
		success : function(res) {
			fillCart(res);
		},
		error : function() {
			//console.log('网络错误');
		}
	});
	// 加载热销商品
	$.ajax({
		url : 'cart/getHotProductPage',
		type : 'post',
		success : function(res) {
			$("#hotProductContent").html(res);
		},
		error : function() {
			//console.log('网络错误');
		}
	});

});

function changePriceByfavour(obj){
	var skuid = obj.parentElement.children[1].value;
	var quantity = $(obj.parentNode.parentNode).find("input[skuid='"+skuid+"']")[0].value;
	var shopid = obj.parentElement.children[3].value;
	$.ajax({
		type: 'post',
		url: "cart/edit",
		data: {"skuId":skuid,"shopId":shopid,"quantity": quantity,"promId":obj.value},
		dataType: "text",
		success: function(data){
			fillCart(data);
		}
	});
}

function fillCart(res){
	$("#shopInfo").html(res);
	$("[isproductframe]").on("taphold",function(){
		var _skuid=$(this).attr('id');
		kltconfirm("确认删除此商品？",function(){
			deleteProduct(_skuid);
		});
	})
}
function toOrderConfirm(total) {
	// 跳转到订单确认页面
	
	$.ajax({
		url : 'user/checkLogin',
		async : true,
		dataType:'json',
		success : function(res) {
			if (res.isLogin) {
				$.ajax({
					url : 'cart/validate',
					type : "post",
					success : function(res) {
						var resObj = JSON.parse(res);
						if (resObj.success) {
							if(total>0){
								window.location = "orderWx/toOrder";	
							}else{
								alert("请您选择商品！");
							}
						} else {
							if (resObj.act_code == 0) {
									// 需要登录
								} else {
									alert(resObj.msg);
							}
						}
					},
					error : function() {
						console.log("网络错误");
					}
				});
			} else {
		
				
				$('#targetUrl').val('orderWx/toOrder');
				showLogin();
			}
		}
	});

}
var changeProTimeout;
var key;
function changeProductTimeout(skuId, δ){
	//注掉长摁删除功能
	//clearTimeout(changeProTimeout);
	var checkIcon = $("[checkIcon][skuId='" + skuId + "']");
	var isChecked = checkIcon.hasClass("fa-check-circle");
	var proNumInput = $("input[skuId='" + skuId + "']");
	var previousValue = proNumInput.attr("previousValue");
	var proNum = proNumInput.val();
	var store = proNumInput.attr("proStore");
	var proNum2 = 0;
	proNum2 = parseInt(proNum) + δ;
	if (proNum2 > parseInt(store)) {
		alert("库存不足");
		proNumInput.val(store);
		proNum2 = parseInt(store);
	}
	if (proNum2 < 1) {
		proNumInput.val(1);
		kltconfirm("确认删除此商品？",function(){
			deleteProduct(skuId);
		},function(){
			proNum2=1;
			changeProduct(skuId,proNum2,isChecked);
		});
		return;
	}
	proNumInput.val(proNum2);
	clearTimeout(key)
	key = setTimeout('changeProduct("'+skuId+'",'+proNum2+','+isChecked+')',1000);
}
function changeProduct(skuId,quantity,ischecked) {// 点击+ -号
	var postParam = {};
	postParam.checked = ischecked;
	postParam.quantity = quantity;
	postParam.skuId = skuId;
	$.ajax({
		url : "cart/changeProduct",
		data : postParam,
		type : 'post',
		success : function(res) {
			fillCart(res);
			// 加载购物车信息
			$.ajax({
				url : 'user/shocar',
				type : 'post',
				dataType:"json",
				success : function(res) {
					//console.log(JSON.stringify(res));
					if(res.quantity>0){
						 $("#cart_dd").html(res.quantity);
						 $("#cart_dd").show();
					}else{
						$("#cart_dd").hide();
					} 
				
					 
				},
				error : function() {
					console.log("网络错误");
				}
			});
			
			
			
		},
		error : function() {
			//console.log('网络错误');
		}
	});

}

function deleteProduct(skuId) {

	$.ajax({
		url : 'cart/del',
		data : {
			skuId : skuId
		},
		type : 'post',
		success : function(res) {
			fillCart(res);
			// 加载购物车信息
			$.ajax({
				url : 'user/shocar',
				type : 'post',
				dataType:"json",
				success : function(res) {
					//console.log(JSON.stringify(res));
					if(res.quantity>0){
						 $("#cart_dd").html(res.quantity);
						 $("#cart_dd").show();
					}else{
						$("#cart_dd").hide();
					} 
					 
				},
				error : function() {
					console.log("网络错误");
				}
			});

		},
		error : function() {
			//console.log('网络错误');
		}
	});
}

function validateValue(t, event) {
	var proNumInput = $(t);
	proNumInput.val(proNumInput.val().replace(/\D/g, ''));
	var _num = proNumInput.val();
	var previousValue = proNumInput.attr("previousValue");
	var store = proNumInput.attr("proStore");
	if (_num == '' || _num == null || _num == undefined) {
		_num = 1;
	}
	var _num2 = parseInt(_num);
	if (_num2 > parseInt(store)) {
		alert("库存不足");
		_num2 = parseInt(store);
	}
	if (_num2 <= 0) {
		_num2 = 1;
	}
	if (_num2 > 99) {
		_num2 = 99;
	}
	//proNumInput.val(previousValue);
	// t.onchange();
}

function onchangeValue(t) {
	var proNumInput = $(t);
	var skuId = proNumInput.attr('skuId');
	changCount(skuId,parseInt(t.value));
}

function changCount(skuId,δ){
	//注掉长摁删除功能
	//clearTimeout(changeProTimeout);
	var checkIcon = $("[checkIcon][skuId='" + skuId + "']");
	var isChecked = checkIcon.hasClass("fa-check-circle");
	var proNumInput = $("input[skuId='" + skuId + "']");
	var previousValue = proNumInput.attr("previousValue");
	var proNum = proNumInput.val();
	var store = proNumInput.attr("proStore");
	var proNum2 = δ;
	if (proNum2 > parseInt(store)) {
		alert("库存不足");
		proNumInput.val(store);
		proNum2 = parseInt(store);
	}
	if (proNum2 < 1) {
		proNumInput.val(1);
		kltconfirm("确认删除此商品？",function(){
			deleteProduct(skuId);
		},function(){
			proNum2=1;
			changeProduct(skuId,proNum2,isChecked);
		});
		return;
	}
	proNumInput.val(proNum2);
	changeProTimeout=setTimeout('changeProduct("'+skuId+'",'+proNum2+','+isChecked+')',300);
}

function proPick(t) {
	var icon = $(t);
	var skuId = icon.attr("skuId");
	var proNumInput = $("input[skuId='" + skuId + "']");
	var isChecked = icon.hasClass("fa-check-circle");
	var num = proNumInput.val();
	var postParam = {};
	postParam.checked = !isChecked;
	postParam.quantity = num;
	postParam.skuId = skuId;
	
	$.ajax({
		url : "cart/changeProduct",
		data : postParam,
		type : 'post',
		success : function(res) {
			fillCart(res);
		},
		error : function() {
			//console.log('网络错误');
		}
	});
}

function checkShop(t) {
	var icon = $(t);
	var shopId = icon.attr("shopId");
	var isChecked = icon.hasClass("fa-check-circle");
	var postParam = {};
	postParam.checked = !isChecked;
	postParam.shopid = shopId;
	$.ajax({
		url : "cart/checkShop",
		data : postParam,
		type : 'post',
		success : function(res) {
			fillCart(res);
			
		},
		error : function() {
			//console.log('网络错误');
		}
	});
}

function checkAll(t) {
	var icon = $(t);
	var isChecked = icon.hasClass("fa-check-circle");
	var postParam = {};
	postParam.checked = !isChecked;
	$.ajax({
		url : "cart/checkAll",
		data : postParam,
		type : 'post',
		success : function(res) {
			fillCart(res);
		},
		error : function() {
			//console.log('网络错误');
		}
	});
}

function login() {
	$.ajax({
		url : "user/login",
		data : {
			"userName" : $("#username").val(),
			"password" : $("#password").val()
		},
		type : "post",
		dataType : "json",
		success : function(res) {
			if (res.errmsg) {
				alert(res.errmsg);
			} else {
				// alert('登陆成功');
				window.location.reload();
			}
		}
	});
}

function toProductDetail(_this){
	var freshUrl=$(_this).attr("freshUrl");
	console.log(freshUrl);
	if(freshUrl){
		var _index=freshUrl.indexOf("?");
		if(_index>=0){
			var targetFix=freshUrl.substr(_index);
			window.location="product/toDetail"+targetFix;
		}
	}
}

//删除购物车 增加勾选删除功能 zhm
function delPick() {
	var icon = $("i[name='danxk']")
		kltconfirm("确认删除此商品？", function() {
			var	objskuId=null;
			$("i[name='danxk']").each(function(e) {
				var delskid = this.attributes[3].nodeValue;
				var proNumInput = $("input[skuId='" + delskid + "']")
				var isChecked = this.classList[1];
				if (isChecked == "fa-check-circle") {
					var num = proNumInput.val();
					var postParam = {};
					postParam.checked = !isChecked;
					postParam.quantity = num;
					postParam.skuId = delskid;
					objskuId+=delskid+",";
				}
			});
			objskuId=objskuId.substring(4,objskuId.length-1);
			deleteProductAll(objskuId);
		});
	   
}
function deleteProductAll(objskuId) {
	
	$.ajax({
		url : 'cart/delAll',
		data : {
			objskuId : objskuId
		},
		type : 'post',
		success : function(res) {
			fillCart(res);
		
		},
		error : function() {
			//console.log('网络错误');
		}
	});
}



