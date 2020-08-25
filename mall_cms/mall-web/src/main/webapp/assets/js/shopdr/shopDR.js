/**定义装修的常量*/
var ShopDecorate = {
	BANNER_MODULE_TYPE:1,//头部logo
	AD_MODULE_TYPE:2,//轮播的三个广告位
	HOT_MODULE_TYPE:3,//轮播下面的三个广告位
	PRO_HOT_TYPE:4,//推荐位的大图
	PRO_TYPE:5,//推荐位产品列表
	UPLOAD_SIZE:"5242880"//图片上传默认最大字节数
};

/**设置模版样式*/
ShopDecorate.setStyle = function(color){
	$("#shop_header .mid_search .search").css("border","2px solid "+color);
	$("#shop_header .left_img h3").css("color",color);
	$(".shop_color0").css("background-color",color);
	$(".shop_color1").css("background-color",color);
	$(".nav ul .focus_li").css({"background-color":this.getSeledMenuBgColor(color),"color":"#fff"});
	$(".ban").css({"width":"100%","backgroud-color":"#000"});
	$(".ban_img img").css({"width":"980px","min-height":"100px","background-color":"#000","overflow":"hidden"});
	$(".shop_font0,.shop_font0 a").css({"color":color}); 
	$("#shop_left .nTab .TabTitle .active").css({"border":"1px solid "+color,"border-top":"2px solid "+this.getSeledMenuBgColor(color),"color":"#747474"});
	$("#shop_left .left_content3 .icon_1").css({"background-color":color,"border":"1px solid #E50011","color":color});
	$("#shop_left h3,#shop_left dl dt,#shop_left .left_top").css({"height":"35px","line-height":"35px","border-bottom":"1px solid #e8e8e8","text-align":"left","font-weight":"bold","font-size":"14px","background":color,"color":"#fff"});
	/*分页样式*/
	$(".page ul li a.curr").css({"display":"block","background-color":color,"height":"30px","color":"#fff","border":"1px solid "+color});
	$(".page ul li a:hover.curr").css({"background":color,"border":"1px solid "+color});
	/*公告列表title背景颜色*/
	$(".left_box .left_top").css({"height":"35px","line-height":"35px","border-bottom":"1px solid #e8e8e8","text-align":"left","font-weight":"bold","font-size":"14px","background":color,"color":"#fff"});
	$(".shop_Notice_tit").css({"background-color":"#f1f1f1"});
	/*搜索条件选中背景*/
	$(".search_condition_box .right_list .focusbg").css({"background-color":"#E50011"});
	$(".border-10").css({"border-top":"2px solid #E50011"});
	/*第二套店铺广告位*/
	$(".shop_advert li").css({"border":"2px solid "+color});
	//优惠券样式
	$("#changColor").css({"height":"35px","line-height":"35px","border-bottom":"1px solid #e8e8e8","text-align":"left","font-weight":"bold","font-size":"14px","background":color,"color":"#fff"});
};

/**验证色值是否输入正确*/
ShopDecorate.validColor = function(color){
	var hexreg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
	if(!hexreg.test(color) || color.length<7 ){
		alert('您输入的色值不符合规范！');
		return false;
	}
	if(color == '#ffffff' || color == '#fff'){
		alert('不能设置白色哦');
		return false;
	}
	return true;
};

/**获取选中菜单的背景颜色*/
ShopDecorate.getSeledMenuBgColor = function(color){
	if("#E50011"==color){
		return "#c11208";
	}else if("#6aa532"==color){
		return "#4c9c00";
	}else if("#075fcc"==color){
		return "#004898";
	}else if("#d4ac4c"==color){
		return "#d69700";
	}else if("#ea4b87"==color){
		return "#d60051";
	}else{
		return color;
	}
};

/**获取页面模版默认的颜色设置*/
ShopDecorate.getDefaultColor = function(){
	var defaultColor = "#E50011";
	if(tmpColor!=null && tmpColor!=""){
		defaultColor = tmpColor;
	}
	return defaultColor;
};

/**退出装修页面跳转到模版列表页面*/
ShopDecorate.exit = function(){
	window.location.href = ctx + "/shopItemListIndexController/toTempleList";
};

/**上传装修图片*/
ShopDecorate.uploadImg = function (option){
	var logofile = document.getElementById(option.fileInput).value;
	var ext = logofile.substring(logofile.length-3).toLowerCase();
	if (ext!="jpg" && ext!="jpeg" && ext!="bmp" && ext!="png"){
		alert("只允许上传jpeg、jpg、bmp、png格式的文件！");
		return false; 
	}
	var url = ctx + "/fileUpload/fixedUpload?date="+new Date().getTime();
	if(option.width){
		//url += "&width="+option.width;
	}
	if(option.height){
		//url += "&height="+option.height;
	}
	if(option.size){
		//url += "&size="+option.size;
	}
	$.ajaxFileUpload({
		url: url, //用于文件上传的服务器端请求地址
		secureuri: false, //是否需要安全协议，一般设置为false
		fileElementId: option.fileInput, //文件上传域的ID
		dataType: 'json', //返回值类型 一般设置为json
		cache: false,
		type:"post",
		success: function (data, status){  //服务器成功响应处理函数
			if(data.success){
				$("#"+option.previewImage).attr("src",imageServerAddr+data.url);
                $("#"+option.pictureUrlName).attr("src",imageServerAddr+data.url);
				$("#"+option.showFileDir).val(data.url);
			}else{
				alert(data.msg);
			}
		},
		error: function (data, status, e){//服务器响应失败处理函数
			alert("图片上传出错，请重新上传！");
		}
	});
};

/**保存某个模块的装修信息*/
ShopDecorate.saveData = function(option){
    $("#"+option.dialogId).hide();
    $.ajax({
        type: "POST",
        url: ctx + "/shopItemListIndexController/temporarySave",
        timeout: 100000,
        data: option.data,
        dataType:"json",
        success: function(response){
            if(response.result=='success'){
                $("#"+option.pictureUrlName).attr("src",imageServerAddr+option.pictureUrl);
            }
        },
        error: function(){
            alert("操作出错，请重新操作");
        }
    });
};

/**
 * 组装页面某个模块的装修信息
 * @param dialogIdExpr 弹窗的id的表达式
 * @param eleId 元素id
 */
ShopDecorate.buildDecorate = function(dialogId, eleId){
	var picUrl = $("#"+eleId+"_picUrl").val();//存入数据库的图片路径
	var title = $("#"+eleId+"_title").val();//弹框显示的标题
	var id = $("#"+eleId+"_id").val();//装修信息id
	var modultType = $("#"+eleId+"_modultType").val();//装修模块类型：页头、广告位等
	var position = $("#"+eleId+"_position").val();//装修模块位置
	var pictureUrlName = eleId+"_pictureUrlName";//装修模块位置显示图片的img元素的id
	var width = $("#"+eleId+"_width").val();//图片宽度
	var height = $("#"+eleId+"_height").val();//图片高度
	var chainUrl = $("#"+eleId+"_chainUrl").val();//图片链接地址
	
	$("#"+dialogId+"_id").val(id);
	$("#"+dialogId+"_pictureUrlText").val(picUrl);
	$("#"+dialogId+"_pictureUrlName").val(pictureUrlName);
	$("#"+dialogId+"_position").val(position);
	$("#"+dialogId+"_modultType").val(modultType);
	//$("#"+dialogId+"_eleId").val(eleId);
	$("#"+dialogId+"_dialogTitle").html(title);
	var src = "";
	if(picUrl!=null && picUrl!=""){
		src = imageServerAddr + picUrl;
	}
	$("#"+dialogId+"_previewImage").attr("src",src);
	$("#"+dialogId+"_width").val(width);
	$("#"+dialogId+"_height").val(height);
	$("#"+dialogId+"_chainUrl").val(chainUrl);
	$("#"+dialogId+"_supportSize").text(width+"*"+height+" px");
	
	$("#"+dialogId).show();
};

/**推荐位编辑弹框中的店铺商品加载*/
ShopDecorate.loadShopProduct = function(){
	$.ajax({ 
		url: ctx + "/shopItemListIndexController/queryShopProduct", 
		type: "post",
		cache: false,
		data: "shopId="+$("#shopId").val()+"&itemName="+$("#_itemName").val()+"&id="+$("#_itemId").val(),
		dataType: "html",
		success: function(response){
			$("#prolist").html(response);	
		}
	});
};
