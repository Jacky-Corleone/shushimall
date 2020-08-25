var currentPage = 0;
var pageSize = 8;
var searchResultTpltOjb = Handlebars.compile($("#searchResultTplt").html());
var searchShopResultTpltOjb = Handlebars.compile($("#searchShopResultTplt").html());
var leimu3Id;
var canRefresh=true;
var orderSort=2;//排序依据
var orderSortShop=2;//店铺排序依据
var searchType=0;//商品还是店铺
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
var myScroll;
$(function(){
	loaded ();
	canRefresh=false;
	$("#searchGoodsList").html('');
	var data = {};
	data.page = 1;
	data.keyword=$("#keyWord").val();
	data.pageSize = 8;
	data.cid=_searchItemId;
	leimu3Id=_searchItemId;
	loadProducts(data);
});
// 根据选中的类目3加载商品
function loadItem(itemId) {
	canRefresh=false;
	leimu3Id=itemId;
	$("#searchGoodsList").html('');
	$("#keyWord").val('');
	var data = {};
	data.cid = itemId;
	data.page = 1;
	data.keyword="";
	data.pageSize = 8;
	data.orderSort=orderSort;
	loadProducts(data);
	toggleMenu();
}

function loadProducts(data,fun) {
	if(searchType==0){
		$.ajax({
			url : "search/searchItem",
			type : 'post',
			data : data,
			dataType : 'json',
			success : function(res) {
				currentPage = res.pager.page;
				if (currentPage >= res.pager.totalPage) {
					currentPage= res.pager.totalPage;
					$("#pullUpLabel").html("没有更多信息了");
					$("#searchGoodsList").append(searchResultTpltOjb(res));
				} else {
					$("#searchGoodsList").append(searchResultTpltOjb(res));
					$("#pullUpLabel").html("上拉加载更多...");
				}
				canRefresh=true;
				$("#pullDownLabel").html("下拉刷新");
				setTimeout(function(){
				myScroll.refresh();},200);
				if(_isfunction(fun)){
					fun(res);
				}
			},
			error : function() {
				console.log("网络错误");
			}
		});
	}else{
		$.ajax({
			url : "search/searchShop",
			type : 'post',
			data : data,
			dataType : 'json',
			success : function(res) {
				currentPage = res.pager.page;
				if (currentPage >= res.pager.totalPage) {
					currentPage= res.pager.totalPage;
					$("#pullUpLabel").html("没有更多信息了");
					$("#searchGoodsList").append(searchShopResultTpltOjb(res));
				} else {
					$("#searchGoodsList").append(searchShopResultTpltOjb(res));
					$("#pullUpLabel").html("上拉加载更多...");
				}
				canRefresh=true;
				$("#pullDownLabel").html("下拉刷新");
				setTimeout(function(){
					myScroll.refresh();},200);
				if(_isfunction(fun)){
					fun(res);
				}
			},
			error : function() {
				console.log("网络错误");
			}
		});
	}
}
function changeKeyWord() {
	var searchContent=$("#keyWord").val();
	if(searchContent == '' || searchContent == null || searchContent == undefined){
		return;
	}
	canRefresh=false;
	$("#searchGoodsList").html('');
	var data = {};
	data.cid = leimu3Id;
	data.page = 1;
	data.keyword=$("#keyWord").val();
	pageSize = 8;
	data.orderSort=orderSort;
	$("#pullUpLabel").html('加载中……');
	loadProducts(data);
}
// 下一页
function nextPage() {
	if(!canRefresh){
		return;
	}
	$("#pullUpLabel").html("加载中……");
	canRefresh=false;
	var data = {};
	data.cid = leimu3Id;
	data.page = currentPage + 1;
	data.keyword=$("#keyWord").val();
	data.pageSize = 8;
	data.orderSort=orderSort;
	loadProducts(data);
}

function toggleMenu() {
	$("#navigation").toggle();
}

//下拉刷新
function refresh(){
	if(!canRefresh){
		return;
	}
	canRefresh=false;
	var data = {};
	data.cid = leimu3Id;
	data.page =1;
	data.keyword=$("#keyWord").val();
	data.pageSize = 8;
	data.orderSort=orderSort;
	$("#searchGoodsList").html('');
	loadProducts(data);
}

function loaded () {
	refreshScroll();
}




function refreshScroll() {
	myScroll = new iScroll(
			'wrapper',
			{
				vScrollbar:true,
				hscrollbar:false,
				topOffset : 40,
				onScrollStart:function(){
					this.___scrollStartY___=this.y;
				},
				onScrollMove : function() {
					if(this.y>50){
						$("#pullDownLabel").html("松开刷新");
					}
					if((this.y<this.maxScrollY-50)&& (this.___scrollStartY___-this.y)>50){
						$("#pullUpLabel").html("松开继续加载");
					}
				},
				onTouchEnd : function() {
					if(this.y>50){
						setTimeout(refresh(), 100);
					}
					if((this.y<this.maxScrollY-50 )&& (this.___scrollStartY___-this.y)>50){
						setTimeout(nextPage(),100);
					}
				},
				onBeforeScrollStart:function(e){
					　　var nodeType = e.explicitOriginalTarget ? e.explicitOriginalTarget.nodeName.toLowerCase() : (e.target ? e.target.nodeName.toLowerCase() : '');
					　　if(nodeType != 'select' && nodeType != 'option' && nodeType != 'input' && nodeType != 'textarea'){
					　　 　　e.preventDefault();
					　　}
				}

			});
}



function orderSortEvent(_t){
	var searchflag=$(_t).attr('searchflag');
	var arrow=$(_t).children();

	var isdown=arrow.hasClass('arrowdown');
	if(isdown){
		orderSort=searchflag*2-1;
	}else{
		orderSort=searchflag*2;
	}

	var data = {};
	data.page = 1;
	data.keyword=$("#keyWord").val();
	data.pageSize = 8;
	data.cid=leimu3Id;
	data.orderSort=orderSort;
	var callback=function(res){

		var _orderSort=res.orderSort;
		var _index=Math.floor((_orderSort-1)/2);
		var aimChild=$("#searchProductSortPanel").children().eq(_index);

		aimChild.siblings().removeClass('searchSortRed');
		aimChild.addClass('searchSortRed');
		$("#searchProductSortPanel").find('.arrowup').removeClass('arrowup').addClass('arrowdown');
		if(_orderSort%2 == 1){
			arrow.removeClass('arrowdown').addClass('arrowup');
		}else{
			arrow.removeClass('arrowup').addClass('arrowdown');
		}
	}
	$("#pullUpLabel").html("加载中……");
	$("#searchGoodsList").html('');
	loadProducts(data,callback);
}
function orderShopSortEvent(_t){
	var searchflag=$(_t).attr('searchflag');
	var arrow=$(_t).children();

	var isdown=arrow.hasClass('arrowdown');
	if(isdown){
		orderSort=searchflag*2-1;
	}else{
		orderSort=searchflag*2;
	}

	var data = {};
	data.page = 1;
	data.keyword=$("#keyWord").val();
	data.pageSize = 8;
	data.orderSort=orderSort;
	var callback=function(res){

		var _orderSort=res.orderSort;
		var _index=Math.floor((_orderSort-1)/2);
		var aimChild=$("#searchShopSortPanel").children().eq(_index);

		aimChild.siblings().removeClass('searchSortRed');
		aimChild.addClass('searchSortRed');
		$("#searchShopSortPanel").find('.arrowup').removeClass('arrowup').addClass('arrowdown');
		if(_orderSort%2 == 1){
			arrow.removeClass('arrowdown').addClass('arrowup');
		}else{
			arrow.removeClass('arrowup').addClass('arrowdown');
		}
	}
	$("#pullUpLabel").html("加载中……");
	$("#searchGoodsList").html('');
	loadProducts(data,callback);
}

function flushProDatas(){
	var data = {};
	data.page = 1;
	data.keyword=$("#keyWord").val();
	data.pageSize = 8;
	data.cid=leimu3Id;

	data.orderSort=orderSort;
	var callback=function(res){
		var _orderSort=res.orderSort;
		var _index=Math.floor((_orderSort-1)/2);
		var aimChild=$("#searchProductSortPanel").children().eq(_index);
		$("#searchProductSortPanel .arrowup").removeClass('arrowup').addClass('arrowdown');
		if(_orderSort%2 == 1){
			aimChild.children().removeClass('arrowdown').addClass('arrowup');
		}else{
			aimChild.children().removeClass('arrowup').addClass('arrowdown');
		}
	}
	$("#pullUpLabel").html("加载中……");
	$("#searchGoodsList").html('');
	loadProducts(data,callback);
}

function flushShopDatas(){
	var data = {};
	data.page = 1;
	data.keyword=$("#keyWord").val();
	data.pageSize = 8;

	data.orderSort=orderSortShop;
	var callback=function(res){
		var _orderSort=res.orderSort;
		var _index=Math.floor((_orderSort-1)/2);
		var aimChild=$("#searchShopSortPanel").children().eq(_index);
		$("#searchShopSortPanel .arrowup").removeClass('arrowup').addClass('arrowdown');
		if(_orderSort%2 == 1){
			aimChild.children().removeClass('arrowdown').addClass('arrowup');
		}else{
			aimChild.children().removeClass('arrowup').addClass('arrowdown');
		}
	}
	$("#pullUpLabel").html("加载中……");
	$("#searchGoodsList").html('');
	loadProducts(data,callback);
}

function changeType(res){
	if(res==0){
		$("#typePanel").toggle();
		$("#unseebg").toggle();
	}else{
		$("#typePanel").hide();
		$("#unseebg").hide();
	}

}
function typeClick(para){
	if(0==para){
		$("#typeTitle").html('商品');
		$("#searchProductSortPanel").removeClass("none");
		$("#searchShopSortPanel").addClass('none');
		if(searchType!=0){
			//根据商品重新加载
			searchType=0;
			//orderSort=2;
			var searchContent=$("#keyWord").val();
			if(searchContent == '' || searchContent == null || searchContent == undefined){

			}else{
				flushProDatas();
			}
		}
		searchType=0;
	}else{
		$("#typeTitle").html('店铺');
		$("#searchShopSortPanel").removeClass("none");
		$("#searchProductSortPanel").addClass('none');
		if(searchType!=1){
			//根据店铺重新加载
			searchType=1;
			//orderSortShop=2;
			var searchContent=$("#keyWord").val();
			if(searchContent == '' || searchContent == null || searchContent == undefined){

			}else{
				flushShopDatas();
			}
		}
		searchType=1;
	}
	changeType(1);
}