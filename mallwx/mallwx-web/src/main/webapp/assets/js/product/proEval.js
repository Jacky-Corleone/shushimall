var currentPage = 0;
var myScroll0 = {};
var myScroll1 = {};
var myScroll2 = {};
var pager0 = {};
var pager1 = {};
var pager2 = {};
var canRefresh0 = true;
var canRefresh1 = true;
var canRefresh2 = true;
var evalContentHbs;
$(function() {
	pager0.pageCode = 1;
	pager1.pageCode = 1;
	pager2.pageCode = 1;
	evalContentHbs = Handlebars.compile($("#evalContent").html());
	loadScroll();
	loadEvalAll(_itemId, _skuId, 1, allCallback);// 全部评价
	loadEvalPositive(_itemId, _skuId, 1, posCallback);// 好评
	loadEvalNagetive(_itemId, _skuId, 1, nagCallback);// 差评
});

function loadEvalAll(__itemId, __skuId, pageCode, callback) {
	var skuId = __skuId;
	var itemId = __itemId;

	var params = {};
	params.page = pageCode;
	params.skuId = skuId;
	params.itemId = itemId;
	loadEval(params, callback);
}
function loadEvalPositive(__itemId, __skuId, pageCode, callback) {
	var skuId = __skuId;
	var itemId = __itemId;

	var params = {};
	params.page = pageCode;
	params.skuId = skuId;
	params.itemId = itemId;
	params.scopeLevel = 2;
	loadEval(params, callback);
}
function loadEvalNagetive(__itemId, __skuId, pageCode, callback) {
	var skuId = __skuId;
	var itemId = __itemId;

	var params = {};
	params.page = pageCode;
	params.skuId = skuId;
	params.itemId = itemId;
	params.scopeLevel = 3;
	loadEval(params, callback);
}

function loadEval(_params, callback) {
	$.ajax({
		url : "product/getItemEvaluationJson",
		data : _params,
		type : "post",
		dataType : "json",
		success : function(res) {
			callback(res);
		},
		error : function() {
			//console.log("网络错误");
		}
	});
}
function allCallback(res) {
	// console.log(res);
	pager0.currentPage = res.pager.page;
	// console.log(evalContentHbs(res.pager));
	$("#evalList0").append(evalContentHbs(res.pager));
	console.log(pager0.currentPage);
	if (pager0.currentPage >= res.pager.totalPage) {
		$("#pullUpLabel0").html("没有更多评论了……");
	}
	$("#pullDownLabel0").html("下拉刷新");
	myScroll0.refresh();
	canRefresh0 = true;
}
function posCallback(res) {
	console.log(res);
	
	pager1.currentPage = res.pager.page;
	console.log(evalContentHbs(res.pager));
	$("#evalList1").html(evalContentHbs(res.pager));
	if (pager1.currentPage >= res.pager.totalPage) {
		$("#pullUpLabel1").html("没有更多评论了……");
	}
	$("#pullDownLabel1").html("下拉刷新");
	myScroll1.refresh();
	canRefresh1 = true;
}
function nagCallback(res) {
	console.log(res);
	pager2.currentPage = res.pager.page;
	console.log(evalContentHbs(res.pager));
	$("#evalList2").html(evalContentHbs(res.pager));
	if (pager2.currentPage >= res.pager.totalPage) {
		$("#pullUpLabel2").html("没有更多评论了……");
	}
	$("#pullDownLabel2").html("下拉刷新");
	myScroll2.refresh();
	canRefresh2 = true;
}
function loadScroll() {
	myScroll0 = new iScroll('myTab0_Content0', {
		vScrollbar : true,
		hscrollbar : false,
		topOffset : 0,
		onScrollStart:function(){
			this.___scrollStartY___=this.y;
		},
		onTouchEnd : function() {
			if (this.y > 50) {
				refresh0();
			}
			if (this.y < this.maxScrollY - 50) {
				nextPage0();
			}
		},
		onScrollMove : function() {
			if (this.y > 50) {
				$("#pullDownLabel0").html("松开刷新");
			}
			if ((this.y < this.maxScrollY - 50)&& (this.___scrollStartY___-this.y)>50) {
				$("#pullUpLabel0").html("松开继续加载");
			}
		}
	});
	myScroll1 = new iScroll('myTab0_Content1', {
		vScrollbar : true,
		hscrollbar : false,
		topOffset : 0,
		onScrollStart:function(){
			this.___scrollStartY___=this.y;
		},
		onTouchEnd : function() {
			if (this.y > 50) {
				refresh1();
			}
			if (this.y < this.maxScrollY - 50) {
				nextPage1();
			}
		},
		onScrollMove : function() {
			if (this.y > 50) {
				$("#pullDownLabel1").html("松开刷新");
			}
			if ((this.y < this.maxScrollY - 50)&& (this.___scrollStartY___-this.y)>50) {
				$("#pullUpLabel1").html("松开继续加载");
			}
		}
	});
	myScroll2 = new iScroll('myTab0_Content2', {
		vScrollbar : true,
		hscrollbar : false,
		topOffset : 0,
		onScrollStart:function(){
			this.___scrollStartY___=this.y;
		},
		onTouchEnd : function() {
			if (this.y > 50) {
				refresh2();
			}
			if (this.y < this.maxScrollY - 50) {
				nextPage2();
			}
		},
		onScrollMove : function() {
			if (this.y > 50) {
				$("#pullDownLabel2").html("松开刷新");
			}
			if ((this.y < this.maxScrollY - 50)&& (this.___scrollStartY___-this.y)>50) {
				$("#pullUpLabel2").html("松开继续加载");
			}
		}
	});
}

function refresh0() {
	if (!canRefresh0) {
		return;
	}
	canRefresh0 = false;
	$("#evalList0").html("");
	var skuId = _skuId;
	var itemId = _itemId;
	var params = {};
	params.page = 1;
	params.skuId = skuId;
	params.itemId = itemId;
	loadEval(params, allCallback);
}
function refresh1() {
	if (!canRefresh1) {
		return;
	}
	canRefresh1 = false;
	$("#evalList1").html("");
	var skuId = _skuId;
	var itemId = _itemId;
	var params = {};
	params.page = 1;
	params.skuId = skuId;
	params.itemId = itemId;
	params.scopeLevel = 1;
	loadEval(params, posCallback);
}
function refresh2() {
	if (!canRefresh2) {
		return;
	}
	canRefresh2 = false;
	$("#evalList2").html("");
	var skuId = _skuId;
	var itemId = _itemId;
	var params = {};
	params.page = 1;
	params.skuId = skuId;
	params.itemId = itemId;
	params.scopeLevel = 3;
	loadEval(params, nagCallback);
}

function nextPage0() {
	if (!canRefresh0) {
		return;
	}
	canRefresh0 = false;
	var skuId = _skuId;
	var itemId = _itemId;
	var params = {};
	params.page = pager0.pageCode + 1;
	params.skuId = skuId;
	params.itemId = itemId;
	loadEval(params, allCallback);
}
function nextPage1() {
	if (!canRefresh0) {
		return;
	}
	canRefresh0 = false;
	var skuId = _skuId;
	var itemId = _itemId;
	var params = {};
	params.page = pager1.pageCode + 1;
	params.skuId = skuId;
	params.scopeLevel = 3;
	params.itemId = itemId;
	loadEval(params, allCallback);
}
function nextPage2() {
	if (!canRefresh0) {
		return;
	}
	canRefresh0 = false;
	var skuId = _skuId;
	var itemId = _itemId;
	var params = {};
	params.page = pager2.pageCode + 1;
	params.skuId = skuId;
	params.scopeLevel = 3;
	params.itemId = itemId;
	loadEval(params, allCallback);
}