var pager0 = {};
var pager1 = {};
var pager2 = {};
var prodInfoTem;
var myScroll;
var _item3Id;
var activeTag=0;
var canbeRefresh0=true;
var canbeRefresh1=true;
var canbeRefresh2=true;
$(function() {
	prodInfoTem = Handlebars.compile($("#proInfoHbs").html());
	favProdHbsTem = Handlebars.compile($("#favProdHbs").html());
	saleProdHbsTem = Handlebars.compile($("#saleProdHbs").html());

	refreshScroll();

	var _params = {};
	_params.shopId = _shopId;
	_params.page=1;
	load0(_params);
	load1(_params);
	load2(_params);
});

$(document).ready(function(){
    setTimeout(addback,1000);
});

function addback(){
	var lasturl = document.referrer;
	var _param={}
  _param.id="gobackShop";
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
      	input2.attr("value",1);
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

// 加载下一页
function nextPage(_num) {
	if (_num == 0) {
		var _params = {};
		_params.shopId = _shopId;
		_params.page = pager0.page + 1;
		load0(_params);
	}
	if (_num == 1) {
		var _params = {};
		_params.shopId = _shopId;
		_params.page = pager1.page + 1;
		load1(_params);
	}
}

function load0(_params) {
	if(!canbeRefresh0){
		return;
	}
	canbeRefresh0=false;
	$("#loadmore0").html("加载中……");
	$.ajax({
		url : 'shopItemListIndexController/toListIndex2',
		data : _params,
		type : 'post',
		dataType : 'json',
		success : function(resObj) {
			pager0.page = resObj.pager.page;
			$("#prods0").append(prodInfoTem(resObj));
			$("#loadmore0").html("上滑加载更多……");
			if (resObj.page >= resObj.totalPage) {
				$("#loadmore0").html("没有更多信息了……");
			}
			setTimeout(function() {
				myScroll.refresh();
				canbeRefresh0=true;
			}, 300);
		}
	});
}

function load1(_params) {
	if(!canbeRefresh1){
		return;
	}
	canbeRefresh1=false;
	$("#loadmore1").html("加载中……");
	// 销量排行
	$.ajax({
		url : 'shopItemListIndexController/loadHotProduct',
		data : _params,
		type : 'post',
		dataType : 'json',
		success : function(resObj) {
			pager1.page = resObj.page;
			$("#prods1").append(saleProdHbsTem(resObj));
			$("#loadmore1").html("上滑加载更多……");
			if (resObj.page >= resObj.totalPage) {
				$("#loadmore1").html("没有更多信息了……");
			}
			setTimeout(function() {
				myScroll.refresh();
				canbeRefresh1=true;
			}, 300);
		}
	});
}
function load2(_params) {
	if(!canbeRefresh2){
		return;
	}
	canbeRefresh2=false;
	$("#loadmore2").html("加载中……");
	// 收藏排行
	$.ajax({
		url : 'shopItemListIndexController/loadFavourite',
		data : _params,
		type : 'post',
		dataType : 'json',
		success : function(resObj) {
			pager2.page = resObj.page;
			$("#prods2").append(favProdHbsTem(resObj));
			if (resObj.page >= resObj.totalPage) {
				$("#loadmore2").html("没有更多信息了……");
			}
		}
	});
}

function showItem(t, type) {
	var itemId = $(t).attr('categoryCid');
	if (type === 2) {

		$("[isItem2]").hide(300);
		$("[isItem2][parentId=" + itemId + "]").stop();
		$("[isItem2][parentId=" + itemId + "]").toggle(300);
	}
	if (type === 3) {
		$("[isItem3]").hide(300);
		$("[isItem3][parentId=" + itemId + "]").stop();
		$("[isItem3][parentId=" + itemId + "]").toggle(300);
	}
}
function toggleItem() {
	$("#navigation").toggle();
}
// 收藏店铺
function addShopFavourite(sellerId, shopId) {
	$.ajax({
		url : "favourite/addShop",
		type : "post",
		data : {
			"sellerId" : sellerId,
			"shopId" : shopId
		},
		dataType : "json",
		success : function(data) {
			if (data.success) {
				alert("收藏成功");
				$("#favourBtn").text("已收藏").attr('disabled', true);
			} else {
				alert("系统繁忙，请稍后再试！");
			}
		}
	});
}

// 根据类目3加载商品列表
function loadProducts(_itemId) {
	toggleItem();
	if(!canbeRefresh0){
		return;
	}
	canbeRefresh0=false;
	$("#prods0").html("");
	_item3Id = _itemId;
	var _param = {};
	_param.shopId = _shopId;
	_param.cid=_itemId;
	_param.keyword = $("#searchInput").val();
	_param.page = 1;// 搜索之后，页面初始化为1
	$.ajax({
		url : 'shopItemListIndexController/queryShopProduct',
		data : _param,
		type : 'post',
		dataType : 'json',
		success : function(resObj) {
			pager0.page = resObj.page;
			$("#prods0").append(prodInfoTem(resObj));
			if (resObj.page >= resObj.totalPage) {
				$("#loadmore0").html("没有更多信息了……");
			}
			setTimeout(function(){
				myScroll.refresh();
			},200);
			canbeRefresh0=true;
		}
	});
}
function refreshScroll() {
	myScroll = new iScroll('wrapper', {
		vScrollbar : true,
		hscrollbar : false,
		topOffset : 0,
		onScrollStart : function() {
			this.___scrollStartY___ = this.y;
		},
		onScrollMove : function() {
			if ((this.y < this.maxScrollY - 50)
					&& (this.___scrollStartY___ - this.y) > 50) {
				$("#loadmore"+activeTag).html("松开继续加载");
			}
		},
		onTouchEnd : function() {
			if ((this.y < this.maxScrollY - 50)
					&& (this.___scrollStartY___ - this.y) > 50) {
				setTimeout(nextPage(activeTag), 200);
			}
		}
	});
}

function changeKeyword(){
	_item3Id="";
	if(!canbeRefresh0){
		return;
	}
	canbeRefresh0=false;
	$("#prods0").html("");
	var _param = {};
	_param.shopId = _shopId;
	_param.cid=_item3Id;
	_param.itemName = $("#searchInput").val();
	_param.page = 1;// 搜索之后，页面初始化为1
	$.ajax({
		url : 'shopItemListIndexController/queryShopProduct',
		data : _param,
		type : 'post',
		dataType : 'json',
		success : function(resObj) {
			pager0.page = resObj.page;
			$("#prods0").append(prodInfoTem(resObj));
			if (resObj.page >= resObj.totalPage) {
				$("#loadmore0").html("没有更多信息了……");
			}
			setTimeout(function(){
				myScroll.refresh();
			},200);
			canbeRefresh0=true;
		}
	});
}

function toCoupons(shopId){
	window.location.href = $("#contextPath").val() + "/coupons/index?type=1&costAllocation=2&shopId=" + shopId;
}