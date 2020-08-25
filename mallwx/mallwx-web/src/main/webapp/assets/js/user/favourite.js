var currentPage0 = 0;
var currentPage1 = 0;
var pageSize = 8;
var searchResultTpltOjb = Handlebars.compile($("#searchResultTplt").html());
var searchShopResultTpltOjb = Handlebars.compile($("#searchShopResultTplt").html());
var leimu3Id;
var canRefresh=true;
var orderSort=2;//排序依据
var orderSortShop=2;//店铺排序依据
var searchType=0;//商品还是店铺
var myScroll0;
var myScroll1;


$(function(){
    loaded ();//初始化滚动组件
    canRefresh=false;//只允许同时一个加载进程
    $("#searchGoodsList").html('');
    $("#searchShopList").html('');
    var data = {};
    data.page = 1;
    data.rows = 8;
    loadProducts1(data);
    loadProducts0(data);
});


function loadProducts0(data,fun) {
    $.ajax({
        url : "buyercenter/favouriteProductsAjax",
        type : 'post',
        data : data,
        dataType : 'json',
        success : function(res) {
            currentPage0 = res.page;
            if (currentPage0 >= res.totalPage) {
                currentPage0= res.totalPage;
                $("#pullUpLabel0").html("没有更多信息了");
                $("#searchGoodsList").append(searchResultTpltOjb(res));
            } else {
                $("#searchGoodsList").append(searchResultTpltOjb(res));
                $("#pullUpLabel0").html("上拉加载更多...");
            }
            canRefresh=true;
            $("#pullDownLabel0").html("下拉刷新");
            setTimeout(function(){
                myScroll0.refresh();},200);
            if(_isfunction(fun)){
                fun(res);
            }
        },
        error : function() {
            console.log("网络错误");
        }
    });

}
function loadProducts1(data,fun) {

    $.ajax({
        url : "buyercenter/favouriteShopsAjax",
        type : 'post',
        data : data,
        dataType : 'json',
        success : function(res) {
            currentPage1 = res.page;
            if (currentPage1 >= res.totalPage) {
                currentPage1= res.totalPage;
                $("#pullUpLabel1").html("没有更多信息了");
                $("#searchShopList").append(searchShopResultTpltOjb(res));
            } else {
                $("#searchShopList").append(searchShopResultTpltOjb(res));
                $("#pullUpLabel1").html("上拉加载更多...");
            }
            canRefresh=true;
            $("#pullDownLabel1").html("下拉刷新");
            setTimeout(function(){
                myScroll1.refresh();},200);
            if(_isfunction(fun)){
                fun(res);
            }
        },
        error : function() {
            console.log("网络错误");
        }
    });

}

// 下一页
function nextPage0() {
    if(!canRefresh){
        return;
    }
    $("#pullUpLabel0").html("加载中……");
    canRefresh=false;
    var data = {};
    data.page = currentPage0 + 1;
    data.pageSize = 8;
    loadProducts0(data);
}

//下拉刷新
function refresh0(){
    if(!canRefresh){
        return;
    }
    canRefresh=false;
    var data = {};
    data.page =1;
    data.rows = 8;
    $("#searchGoodsList").html('');
    loadProducts0(data);
}
// 下一页
function nextPage1() {
    if(!canRefresh){
        return;
    }
    $("#pullUpLabel1").html("加载中……");
    canRefresh=false;
    var data = {};
    data.page = currentPage1 + 1;
    data.rows = 8;
    loadProducts1(data);
}

//下拉刷新
function refresh1(){
    if(!canRefresh){
        return;
    }
    canRefresh=false;
    var data = {};
    data.page =1;
    data.rows = 8;
    data.orderSort=orderSort;
    $("#searchShopList").html('');
    loadProducts1(data);
}

function loaded () {
    refreshScroll();
}

function refreshScroll() {
    //商品
    myScroll0 = new iScroll(
        'wrapper0',
        {
            vScrollbar:true,
            hscrollbar:false,
            topOffset : 40,
            onScrollStart:function(){
                this.___scrollStartY___=this.y;
            },
            onScrollMove : function() {
                if(this.y>50){
                    $("#pullDownLabel0").html("松开刷新");
                }
                if((this.y<this.maxScrollY-50)&& (this.___scrollStartY___-this.y)>50){
                    $("#pullUpLabel0").html("松开继续加载");
                }
            },
            onTouchEnd : function() {
                if(this.y>50){
                    setTimeout(refresh0(), 100);
                }
                if((this.y<this.maxScrollY-50 )&& (this.___scrollStartY___-this.y)>50){
                    setTimeout(nextPage0(),100);
                }
            },
            onBeforeScrollStart:function(e){
                　　var nodeType = e.explicitOriginalTarget ? e.explicitOriginalTarget.nodeName.toLowerCase() : (e.target ? e.target.nodeName.toLowerCase() : '');
					　　if(nodeType != 'select' && nodeType != 'option' && nodeType != 'input' && nodeType != 'textarea'){
                　　 　　e.preventDefault();
					　　}
            }

        });
    //店铺
    myScroll1 = new iScroll(
    'wrapper1',
    {
        vScrollbar:true,
        hscrollbar:false,
        topOffset : 40,
        onScrollStart:function(){
            this.___scrollStartY___=this.y;
        },
        onScrollMove : function() {
            if(this.y>50){
                $("#pullDownLabel1").html("松开刷新");
            }
            if((this.y<this.maxScrollY-50)&& (this.___scrollStartY___-this.y)>50){
                $("#pullUpLabel1").html("松开继续加载");
            }
        },
        onTouchEnd : function() {
            if(this.y>50){
                setTimeout(refresh1(), 100);
            }
            if((this.y<this.maxScrollY-50 )&& (this.___scrollStartY___-this.y)>50){
                setTimeout(nextPage1(),100);
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







function queryRquestPriceInfo(thisObj, Num) {
    if (thisObj.className == "active")
        return;
    var tabs=$("[istabcontent]").hide();
    //tabs.attr('class','normal');
    var thisTab=$("#myTab0_Content"+Num).show();

    $(thisObj).attr('class','active');
    $(thisObj).siblings().attr('class','normal');
    if(Num===0){
    setTimeout(function(){
        myScroll0.refresh();},200);
    }
    if(Num===1){
    setTimeout(function(){
        myScroll1.refresh();},200);
    }
}