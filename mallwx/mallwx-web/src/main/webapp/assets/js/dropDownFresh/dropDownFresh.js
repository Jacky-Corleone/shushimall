//var nScrollHight = 0;//滚动条的高度
//var nScrollTop = 0;//滚动条距离浏览器顶端的高度
//var pageSize = 10;//每页条数
//var pageNo = 2;//页数
//var totalItem = 0;//总记录数
//
//$.dropDownFresh = function(pageSize,url,totalItem, appendId, selectElement,data,callback) {
//	var windowHeight = $(window).height();//浏览器高度 
//	if(selectElement){
//		$("#"+selectElement).scroll(function(){
////				nScrollHight = document.body.scrollHeight || document.documentElement.scrollHeight;
////				nScrollTop = document.body.scrollTop || document.documentElement.scrollTop;
//				nScrollHight = $("#"+selectElement)[0].scrollHeight;
//				nScrollTop = $("#"+selectElement)[0].scrollTop;
//				if(nScrollTop + windowHeight >= nScrollHight){//滚动条触发事件
//					if(pageNo*pageSize >= totalItem){//页面加载超出总条数
//						$("span").remove();
//						$("#" + appendId).append("<span>没有数据了<span>");
//					}else{
//						loadData(pageSize,url,totalItem, appendId ,data,callback);
//						pageNo++;
//						//调用回到函数
//						if(callback){
//							callback("hhe");
//						}
//					}
//				}
//			});
//	}else{
//		$(window).scroll(function(){
//			nScrollHight = document.body.scrollHeight || document.documentElement.scrollHeight;
//			nScrollTop = document.body.scrollTop || document.documentElement.scrollTop;
//			if(nScrollTop + windowHeight >= nScrollHight){//滚动条触发事件
//				if(pageNo*pageSize >= totalItem){//页面加载超出总条数
//					$("span").remove();
//					$("#" + appendId).append("<span>没有数据了<span>");
//				}else{
//					loadData(pageSize,url,totalItem, appendId ,data,callback);
//					pageNo++;
//					//调用回到函数
//					if(callback){
//						callback("hhe");
//					}
//				}
//			}
//		});
//	}
//};
//
//function loadData(pageSize,url,totalItem, appendId,data,callback){
//	if(!data){
//		data = {};
//	}
//	if(url.indexOf("?")>-1){
//		url = url+"&pageNo="+pageNo+"&pageSize="+pageSize;
//	}else{
//		url = url+"?pageNo="+pageNo+"&pageSize="+pageSize;
//	}
//	$.ajax({
//	   type: "POST",
//	   url: url,
//	   dataType: "html",
//	   data: data,
//	   success: function(data){
//		   $("#" + appendId).append(data);
//	   },
//	   error: function(){
//		   alert("系统繁忙,稍后再试");
//	   }
//	});
//}
//-----------------------------------------------------------------------------------------------------//
var dropDownPage = {};
var dropDownPageFlag = true;
$.dropDownFresh = function(pageSize,url,totalItem, appendId, selectElement,data,callback) {
	dropDownPage.pageNo = 2;
	dropDownPage.pageSize = pageSize;
	dropDownPage.url = url;
	dropDownPage.totalItem = totalItem;
	dropDownPage.appendId = appendId;
	dropDownPage.selectElement = selectElement;
	dropDownPage.data = data;
	dropDownPage.callback = callback;
}
var windowHeight = $(window).height();//浏览器高度 
$(document).ready(function(){
	$("#"+dropDownPage.selectElement).scroll(function(){
		nScrollHight = $("#"+dropDownPage.selectElement)[0].scrollHeight;
		nScrollTop = $("#"+dropDownPage.selectElement)[0].scrollTop;
		if(nScrollTop + windowHeight >= nScrollHight && dropDownPageFlag){//滚动条触发事件
			if((dropDownPage.pageNo-1)*(dropDownPage.pageSize) >= dropDownPage.totalItem){//页面加载超出总条数
				//$(".spandata").remove();
				//$("#" + dropDownPage.appendId).append("<p class='spandata hei_32 font_cen'>没有数据了<p>");
				toast("没有数据了");
			}else{
				dropDownPageFlag = false;
				loadData(dropDownPage.pageSize,dropDownPage.url,dropDownPage.totalItem, dropDownPage.appendId ,dropDownPage.data,dropDownPage.callback);
				dropDownPage.pageNo++;
				//调用回到函数
				if(dropDownPage.callback){
					callback("hhe");
				}
			}
		}
	});
});
$(document).ready(function(){
	$(window).scroll(function(){
		nScrollHight = document.body.scrollHeight || document.documentElement.scrollHeight;
		nScrollTop = document.body.scrollTop || document.documentElement.scrollTop;
		if(nScrollTop + windowHeight >= nScrollHight && dropDownPageFlag){//滚动条触发事件
			if((dropDownPage.pageNo-1)*(dropDownPage.pageSize) >= dropDownPage.totalItem){//页面加载超出总条数
				//$(".spandata").remove();
				//$("#" + dropDownPage.appendId).append("<p class='spandata hei_32 font_cen'>没有数据了<p>");
				toast("没有数据了");
			}else{
				dropDownPageFlag = false;
				loadData(dropDownPage.pageSize,dropDownPage.url,dropDownPage.totalItem, dropDownPage.appendId ,dropDownPage.data,dropDownPage.callback);
				dropDownPage.pageNo++;
				//调用回到函数
				if(dropDownPage.callback){
					callback("hhe");
				}
			}
		}
	});
});

function loadData(pageSize,url,totalItem, appendId,data,callback){
	if(!data){
		data = {};
	}
	if(!dropDownPage.pageNo){
		pageNo = 2;
	}else{
		pageNo = dropDownPage.pageNo;
	}
	if(!pageSize){
		pageSize = 10;
	}
	if(url.indexOf("?")>-1){
		url = url+"&pageNo="+pageNo+"&pageSize="+pageSize;
	}else{
		url = url+"?pageNo="+pageNo+"&pageSize="+pageSize;
	}
	$.ajax({
	   type: "POST",
	   url: url,
	   dataType: "html",
	   data: data,
	   success: function(data){
		   $(".spandata").remove();
		   $("#" + appendId).append(data);
		   dropDownPageFlag = true;
	   },
	   error: function(){
		   alert("系统繁忙,稍后再试");
	   }
	});
}