var myScroll;
var canBeLoaded=true;
var pager={};
var orderDetailTptObj;
var statementDetailTptObj;
$(function(){
	var myDate = new Date();
	$("#yyyyid").val(myDate.getFullYear());
    var userType=$("#_userType").val();
    if(userType == '3'){
        changeIconToRed(3);
    }else{
        changeIconToRed(2);
    }
    orderDetailTptObj=Handlebars.compile($("#orderDetailTpt").html());
    statementDetailTptObj=Handlebars.compile($("#statementDetailTpt").html());
    //changeScrollHeight();
    $(".wrapper")[0].addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
    myScroll = new IScroll('.wrapper',{
        shrinkScrollbars:'clip',
        fadeScrollbars:true,
        scrollbars: true,
        click:true,
        probeType:3
    });
    myScroll.on('scrollStart',function(){
        this.___scrollStartY___=this.y;
    });
    myScroll.on('scroll',function(){
        if((this.y<this.maxScrollY-50)&& (this.___scrollStartY___-this.y)>50){
            if(canBeLoaded){
                canBeLoaded=false;
                //$("#pullUpLabel").html('数据加载中……');
                nextPage();
            }
        }
    });
    /*myScroll.on('scrollEnd',function(){
     if((this.y<this.maxScrollY-50)&& (this.___scrollStartY___-this.y)>50){
     $("#pullUpLabel").html('松开加载……');
     }
     });*/

    var _orderStatuses={
        '1':'待付款',
        '2':'待配送',
        '3':'待收货',
        '4':'待评价',
        '5':'已完成',
        '6':'已取消',
        '7':'待审核',
        '8':'待确认'
    };
    Handlebars.registerHelper('getOrderStatus', function(status){
        if(status==undefined){
            return "";
        }
        return _orderStatuses[status];
    });

    //默认显示当前月份的订单
    var currentDate=new Date();
    var month=currentDate.getMonth();
    changeOrderStatus(month+1);

});

function queryRquestPriceInfo(thisObj, Num) {
    if (thisObj.className == "active")
        return;
    var tabObj = thisObj.parentNode.id;
    var tabList = document.getElementById(tabObj)
        .getElementsByTagName("li");

    for (i = 0; i < tabList.length; i++) {
        if (i == Num) {
        	if(Num == 0){
        		showGenarteOrder();
        	}
            thisObj.className = "active";
            document.getElementById("myTab0" + "_Content" + i).style.display = "block";
            myScroll.refresh();
        } else {
            tabList[i].className = "normal";
            document.getElementById("myTab0" + "_Content" + i).style.display = "none";
        }
    }
}
//传到后台年月份
var _num_;
function changeOrderStatus( Aum) {
    addLoadMask();
    _num_=Aum;
    $("#ImyTab0_Content").empty();
    pager.userType = $("#_userType").val();
    pager.page=1;
    pager.month=Aum;
    pager.year=$("#yyyyid").val();
    loadOrders(pager,function(data){
        if(data.orderInfos.length < 1){
            toast('该月没有订单记录');
        }
    });
}
function nextPage(){
    //加载下一页数据
    pager.page=pager.page+1;
    loadOrders(pager);
}
function loadOrders(param,fun){
    uncheckAllCheck();
    $.ajax({
        type : "POST",
        url : "statement/statementMMinfo",
        data :param,
        dataType : "json",
        success : function(data) {
            if(_isfunction(fun)){
                fun(data);
            }
            $("#ImyTab0_Content").append(orderDetailTptObj(data));
            pager.page=data.pager.page;
            if(data.pager.page>=data.pager.totalPage){
                pager.page=data.pager.totalPage;
              // $("#pullUpLabel").html('已到最后一页');
                toast('已到最后一页');
            }else{
                $("#pullUpLabel").html('上滑继续加载');
            }
            myScroll.refresh();
            removeLoadMask();
            canBeLoaded=true;
        }
    });
}
//查看对账单
function showGenarteOrder() {
    addLoadMask();
    $.ajax({
        type : "POST",
        url : "statement/showStatement",
        data:$("#searchForm").serialize(),
        dataType : "json",
        success : function(data) {
            $("#myTab0_Content_1").empty().append(statementDetailTptObj(data));
            removeLoadMask();
        }
    });
}

function checkAll(_this){
    if($(_this).hasClass("fa-circle-thin")){
        $(_this).removeClass("fa-circle-thin").addClass("fa-check-circle");
        $("i[ischeck]").removeClass("fa-circle-thin").addClass("fa-check-circle");
    }
    else if($(_this).hasClass("fa-check-circle")){
        $(_this).removeClass("fa-check-circle").addClass("fa-circle-thin");
        $("i[ischeck]").removeClass("fa-check-circle").addClass("fa-circle-thin");
    }
}
function checkThis(_this){
    if($(_this).hasClass("fa-circle-thin")){
        $(_this).removeClass("fa-circle-thin").addClass("fa-check-circle");
    }
    else if($(_this).hasClass("fa-check-circle")){
        $(_this).removeClass("fa-check-circle").addClass("fa-circle-thin");
    }

}

//生成对账单
function genarteOrder(){
    addLoadMask();
    var userType = $("#_userType").val();
    var iCheck=$("i[ischeck]");
    var arry=new Array();
    for(var i=0;i<iCheck.length;i++){
        if($(iCheck[i]).hasClass("fa-check-circle")){
            arry.push($(iCheck[i]).attr("orderId"));
        }
    }
    if(arry.length>0){
        $.ajax({
            url:'statement/newStatement',
            data:{"datas":JSON.stringify(arry),
            	  "userType":userType
            	  },
            type:'post',
            success:function(res){
                if(res=='true'){
                    toast("对账单已生成！");
                }else{toast("生成失败");}

                $("#ImyTab0_Content").empty();
                pager.page=1;
                pager.month=_num_;
                pager.year=$("#yyyyid").val();
                //				 	var obj = obj.parentNode.id;
                loadOrders(pager,function(data){
                    removeLoadMask();
                    if(data.orderInfos.length < 1){

                        toast('该月没有订单记录');
                    }
                });
            }
        });
    }else{
        toast("请选择订单！");
        removeLoadMask();
        return;
    }
}

function orderDetail(orderId){
	var userType=$("#_userType").val();
	var orderSource = "";
	if(userType == '2'){
		orderSource ="buyers";
	}else if(userType == '3'){
		orderSource ="saler";
	}
    window.location.href="orderWx/queryOrderInfoBuyer?orderId="+orderId + "&orderSource=" + orderSource;
}


window.onresize=changeScrollHeight;
function changeScrollHeight(){
    //TabTitle bg_01 class
    //myTab0_Content1 id
    var windowheight=window.document.body.offsetHeight;
    $(".wrapper").css('height',(windowheight-50-73-52-39-2)+'px');
}

function uncheckAllCheck(){
    $("#allCheckBtn").removeClass("fa-check-circle").addClass("fa-circle-thin");
}

function resetBtn(){
	$("#supplierName").val('');
	$("#inquiryNo").val('');
}

$(document).ready(function() { 

	showGenarteOrder();
	}); 



