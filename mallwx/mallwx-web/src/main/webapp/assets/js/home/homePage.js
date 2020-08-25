
$(function(){
	var floors=$("div[floorflag]");
	changeIconToRed(0);
	for(var f=0;f<floors.length; f++){
		var floor=floors[f];
		var floorid=$(floor).attr("floorflag");
		var floorcount=$(floor).attr("floorcount");
		loadFloor(floorid,floorcount);
	}
	
	var swiper = new Swiper('.swiper-container', {
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        pagination: '.swiper-pagination',// 如果需要分页器
        paginationClickable: true,
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: 5000,
        autoplayDisableOnInteraction: false,
        paginationBulletRender: function (index, className) {
			//分页标签样式
			return '<span style="border: 2px #fff solid;opacity: 0.7" class="' + className + '">'+ '</span>';
		}
    });
	
	var region = $.cookie('regionCode');
	if(!region){
		selectRegion("11");
	}
});

function loadFloor(floorid,floorcount){
	$.ajax(
		{
			url:"home/floor",
			type:"post",
			data:{"fid":floorid,"floorcount":floorcount},
			success:function(data){
				$("#floor"+floorid).html(data);
				//$("#floorinner"+floorid).removeClass('h_zero').addClass('floorfadein');
			},
			error:function(){
				console.log("网络错误");
			}
		});
}

function toggleMenu(){
	$("#navigation").toggle();
}

// 根据选中的类目3加载商品
function loadProducts(itemId) {
	window.location="search/toSearch?searchType=0&searchItemId="+itemId;
}
function toSearch(){
	var searchContent=$("#searchInput").val();
	if(searchContent == '' || searchContent == null || searchContent == undefined){
		return;
	}
	var fleshUrl="search/toSearch";
	var _form = $("<form></form>");
	_form.attr('action',fleshUrl);
	input1 = $("<input type='hidden' name='content' />");
	input1.attr("value",searchContent);
	input2 = $("<input type='hidden' name='searchType' />");
	input2.attr("value",searchType);
	_form.attr('method','post');
	_form.css('display','none');
	_form.append(input1);
	_form.append(input2);
	_form.appendTo("body");
	_form.submit();
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
function login(){
	$.ajax({
		url:"user/login",
		data:{"userName":$("#username").val(),"password":$("#password").val(),"targetUrl":$("#targetUrl").val()},
		type:"post",
		dataType:"json",
		success:function(res){
			if(res.errmsg){
				alert(res.errmsg);
			}else{
				//alert('登陆成功');
				var _targetUrl=encodeURI($("#targetUrl").val()); 
				if(!(_targetUrl == "" || _targetUrl==undefined || _targetUrl==null)){
					$("#targetUrl").val("");
					window.location=_targetUrl;
				}else{
					window.location="./";
				}
			}
		}
	});
}
function unlogin(){
	$.ajax({
		url:'user/unlogin',
		success:function(res){
			if(res == 'true'){
				window.location="./";
			}
		}
	})
}
function ppp(e){
	window.event.cancelBubble=true;
}
function selectRegion(code){
	$.cookie('regionCode',code,{expires: 30, path:"/"});
}

function toPersonCenter(_this){
	var targUrl=$(_this).attr('ahref');
	window.location=targUrl;
}



var searchType=0;
function typeClick(para){
	if(0==para){
		$("#typeTitle").html('商品');
		$("#searchProductSortPanel").removeClass("none");
		$("#searchShopSortPanel").addClass('none');
		searchType=0;
	}else{
		$("#typeTitle").html('店铺');
		$("#searchShopSortPanel").removeClass("none");
		$("#searchProductSortPanel").addClass('none');
		searchType=1;
	}
	changeType(1);
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