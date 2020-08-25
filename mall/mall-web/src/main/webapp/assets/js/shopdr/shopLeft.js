/**
 * 初始化店铺页面左侧数据
 */
function initShopLeft(){
	categoryEventInit();
	loadShopFavourite();
	loadHotProduct();
}

/**
 * 加载店铺收藏数据 
 */
function loadShopFavourite(){
	$.post(ctx+"/shopItemListIndexController/loadFavourite",{shopId:$("#shopId").val()},function(response){
		if(response.result==null || response.result.length==0){
			return;
		}
		var result = response.result;
		for(var i=0; i<result.length; i++){
			var obj = result[i]["itemShopCartDTO"];
			//商品详情页面地址
			if(obj == null){
				return ;
			}
			var detailUrl = ctx + "/productController/details?id=" + obj["itemId"] + "&skuId="+obj["skuId"];
			//商品图片路径
			var imageSrc = imageServerAddr + obj["skuPicUrl"];
			var price = obj["skuPrice"];
			if(obj["hasPrice"] == false){
				price = "暂无报价";
			}
			var sb = "";
			sb += "<li class='fore border-6'>";
			sb += "<div class='i_img'>";
			sb += "<a href='"+detailUrl+"' target='_blank'><img width='54' height='54' src='"+imageSrc+"' /></a>";
			sb += "</div>";
		    sb += "<div class='i_font_big'>";
			sb += "<a href='"+detailUrl+"' title='"+obj["itemName"]+"' target='_blank'>"+obj["itemName"]+"</a><br />";
			sb += "<strong class='shop_font0'>￥"+ price + "</strong>";
			sb += "</div>";
			sb += "</li>";
			$("#favouritePros").append(sb);
		}
	},"json");
}

/**
 * 加载销量最好的商品 
 */
function loadHotProduct(){
	$.post(ctx+"/shopItemListIndexController/loadHotProduct",{shopId:$("#shopId").val()},function(response){
		if(response.result==null || response.result.length==0){
			return;
		}
		var result = response.result;
		for(var i=0; i<result.length; i++){
			var obj = result[i];
			//商品详情页面地址
			var detailUrl = ctx + "/productController/details?id=" + obj["itemId"] + "&skuId="+obj["skuId"];
			//商品图片路径
			var imageSrc = imageServerAddr + obj["picUrl"];
            var sb = "";
			sb += "<li class='fore border-6'>";
			sb += "<div class='i_img'>";
			sb += "<a href='"+detailUrl+"' target='_blank'><img width='54' height='54' src='"+imageSrc+"' /></a>";
			sb += "</div>";
		    sb += "<div class='i_font_big'>";
			sb += "<a href='"+detailUrl+"' title='"+obj["itemName"]+"' target='_blank'>"+obj["itemName"]+"</a><br />";
			sb += "<strong class='shop_font0'>￥" + getActPrice(obj) + "</strong>";
			sb += "</div>";
			sb += "</li>";
			$("#hotPros").append(sb);
		}
	},"json");
}

/**
 * 获取商品实际价格
 * @param obj
 * @returns {string}
 */
function getActPrice(obj){
    var _actPrice = "";
    if(obj["skuInquiryPirce"]){
        _actPrice = obj["skuInquiryPirce"];
    }else{
        if(obj["hasPrice"] == 1){
            _actPrice=obj["skuPrice"];
        }else{
            _actPrice = "暂无报价";
        }
    }
    return _actPrice;
}

/**左侧产品分类事件绑定初始化*/
function categoryEventInit(){
	//默认隐藏子类目
	$("i[class='demo-icons fa-angle-right']").parent().parent().hide();
	
	$("dd.cursor").bind("click",function(){
		$(this).next("ul").toggle(
//				function(){
//					//显示子类目
//					$(this).slideUp("normal");
//				},function(){}
		);
	});
	
	//根据店铺类目查询店铺商品
	$("dl ul li").bind("click",function(){
		var cid = $(this).attr("cid");
		$("#shopCid").val(cid);
		topage(1);
	});
};