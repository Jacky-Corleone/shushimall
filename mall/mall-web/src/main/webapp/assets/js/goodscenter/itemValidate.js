$(function(){
	//校验
	$("#itemForm").validate({
        rules: {
        	cid:"required",
        	itemName: "required",
        	//shopCid:"required",
        	brand:"required",
        	shopFreightTemplateId:"required",
        	marketPrice:{
        		marketPriceValidate:true,
        		number:true
        	},
        	/*inventory:{
        		required:true,
        		number:true,
        		itemInventoryValidate:true
        	},*/
        	marketPrice2:{
        		marketPrice2Validate:true,
        		number:true
        	},
        	guidePrice:{
        		guidePriceValidate:true,
        		number:true
        	},
        	addSource:{
        		required:true
        	},
        	productId:{
        		required:true,
        		number:true
        	},
        	packingList:{
        		required:true
        	},
        	afterService:{
        		required:true
        	},
        	itemSellPriceShow:{
        		itemSellPrice:'areaPriceTable',
        		validateSellPrice:["","提示"]
        	},
        	itemSellCategoryShow:{
        		itemSellCategoryValidate:true,
        		skuSellPriceValidate:[true,"校验"],
        		skuCostValidate:["cosePrice","sku的底价不能为空"],
        		skuInventoryValidate:["inventory","sku的库存不能为空且只能输入数字"],
        		skuProductIdValidate:["skuProductId","sku的商品编号不能为空"]
        	},
        	itemDetailShow:{
        		itemDetailValidate:true
        	},
        	itemPicShow:{
        		itemPicValidate:true
        	},
        	skuPicShow:{
        		skuPicValidate:true
        	},
        	attributesStrShow:{
        		plantCategoryValidate:true
        	},
        	shopFreightTemplateIdStrShow:{
        		shopFreightTemplateIdValidate:true
        	}
		},
		excludeValidator:['itemSellPriceShow','itemSellCategoryShow','itemDetailShow'
		                  	,'itemPicShow','skuPicShow','attributesStrShow','shopFreightTemplateIdStrShow']
    });
	//校验市场价
	jQuery.validator.addMethod("marketPriceValidate", function(value, element, param) {
		if(!$("#hasPriceCheck").is(':checked')){
			if(!value||value==""){
				return false;
			}
		}
		return true;
	}, $.validator.format("必须填写"));
	//校验底价
	jQuery.validator.addMethod("marketPrice2Validate", function(value, element, param) {
		if(!$("#hasPriceCheck").is(':checked')){
			if(!value||value==""){
				return false;
			}
		}
		return true;
	}, $.validator.format("必须填写"));
	//校验报价
	jQuery.validator.addMethod("guidePriceValidate", function(value, element, param) {
		if(!$("#hasPriceCheck").is(':checked')){
			if(!value||value==""){
				return false;
			}
		}
		return true;
	}, $.validator.format("必须填写"));
	//自定义商品销售价格校验
	jQuery.validator.addMethod("itemSellPrice", function(value, element, param) {
		if($("#hasPriceCheck").is(':checked')){
			return true;
		}else{
			 var tableId = param;
			  if(tableId.length > 0){
				  return $("#"+tableId).find("tr").length > 1
			  }
			  $(window).scrollTop($("#info_span").offset().top- 100);
			  return false;
		}
	}, $.validator.format("商品销售价不符合规则,请设置全国销售价!"));
	
	//校验销售属性是否选择
	jQuery.validator.addMethod("plantCategoryValidate", function(value, element, param) {
		var sellCategory = $("#platformAttributeDiv input[type='checkbox']");
		var boo = false;
		$.each(sellCategory, function (n, shopCategory) {
			if($("#"+shopCategory.id).is(':checked')){
				boo = true;
				return;
			}
		});
		if(!boo){
			$(window).scrollTop($("input[name='attributesStrShow']").offset().top- 100);
		}
	  return boo;
	}, $.validator.format("请选择商品属性!"));
	//校验运费模版是否选择
	jQuery.validator.addMethod("shopFreightTemplateIdValidate", function(value, element, param) {
		return $("#shopFreightTemplateId").val();
	}, $.validator.format("请选择商品运费模版!"));
	
	//校验销售属性是否选择
	jQuery.validator.addMethod("itemSellCategoryValidate", function(value, element, param) {
		var addSource=$("#addSource").val();
		if(addSource==3){
			var sellCategory = $("#groupSkuId input[type='checkbox']");
		}else{
			var sellCategory = $("#shopCategoryDiv input[type='checkbox']");
		}
		var boo = false;
		$.each(sellCategory, function (n, shopCategory) {
			if($("#"+shopCategory.id).is(':checked')){
				boo = true;
				return;
			}
		});
		if(!boo){
			if(addSource==3){
				$(window).scrollTop($("#packageGoodId").offset().top- 100);
			}else{
				$(window).scrollTop($("input[name='itemSellCategoryShow']").offset().top- 100);
			}
		}
	  return boo;
	}, $.validator.format("请选择商品销售属性值!"));
	
	//校验sku销售价格
	jQuery.validator.addMethod("skuSellPriceValidate", function(value, element, param) {
		var addSource=$("#addSource").val();
		if(!$("#hasPriceCheck").is(':checked')){
			for(var i = 0;i<sellPriceDivNum+1;i++){
				if($("#areaAllTable_"+i).length > 0){
					if($("#skuAreaPriceQGTable_"+i).find("tr").length <= 1){
						param[1] = "请设置sku销售价!";
						if(addSource==3){
							$(window).scrollTop($("#packageGoodId").offset().top- 100);
						}else{
							$(window).scrollTop($("input[name='itemSellCategoryShow']").offset().top- 100);
						}
						return false;
					}
					var validateMessage = valueValidate("areaAllTable_"+i,true,true,"2");
					if(validateMessage != ""){
						param[1] = "sku阶梯价中："+validateMessage;
						if(addSource==3){
							$(window).scrollTop($("#packageGoodId").offset().top- 100);
						}else{
							$(window).scrollTop($("input[name='itemSellCategoryShow']").offset().top- 100);
						}
						return false;
					}
				}
			}
		}
		return true;
	}, $.validator.format("{1}"));
	
	//校验sku基本信息
	jQuery.validator.addMethod("skuCostValidate", function(value, element, param) {
		var isPass = true;
		if(!$("#hasPriceCheck").is(':checked')){
			for(var i = 0;i<sellPriceDivNum+1;i++){
				if($("#costPrice_"+i).length > 0){  
					if(!$("#costPrice_"+i) || $("#costPrice_"+i).val() == ""){
						isPass = false;
						break;
					}
				}
			}
		}
		return isPass;
	}, $.validator.format("{1}"));
	
	//校验sku基本信息
	jQuery.validator.addMethod("skuInventoryValidate", function(value, element, param) {
		var isPass = true;
		for(var i = 0;i<sellPriceDivNum+1;i++){
			if($("#"+i+"_skuInventory").length > 0){
				if(!$("#"+i+"_skuInventory") || $("#"+i+"_skuInventory").val() == ""){
					isPass = false;
					break;
				}
				var chinese = /^[0-9]*$/;
				//验证
				if (!chinese.test($("#"+i+"_skuInventory").val()))
				{
					isPass = false;
					break;
				}
			}
		}
		return isPass;
	}, $.validator.format("{1}"));
	
	//校验sku商品编号信息
	jQuery.validator.addMethod("skuProductIdValidate", function(value, element, param) {
		var isPass = true;
		for(var i = 0;i<sellPriceDivNum+1;i++){
			
			if($("#"+i+"_skuProductId").length > 0){
				//去除前后空格然后判断是否为“”
				if(!$("#"+i+"_skuProductId") || $.trim($("#"+i+"_skuProductId").val()) == ""){
					isPass = false;
					break;
				}
			}
		}
		return isPass;
	}, $.validator.format("{1}"));
	
	//校验商品详情  必填
	jQuery.validator.addMethod("itemDetailValidate", function(value, element, param) {
		var obj = UE.getEditor('editor').getContent();
		if(!obj || obj == ""){
			$(window).scrollTop($("input[name='itemDetailShow']").offset().top);
			return false;
		}
		return true;
	}, $.validator.format("商品详情不能为空!"));
	
	//校验商品图片，至少上传一张
	jQuery.validator.addMethod("itemPicValidate", function(value, element, param) {
		var isPass = false;
		for(var i = 1;i<=4;i++){
			var obj = $("#picUrls_"+i);
			if(obj.val() != ""){
				isPass = true;
				break;
			}
		}
		if(!isPass){
			$(window).scrollTop($("input[name='itemPicShow']").offset().top - 100);
		}
		return isPass;
	}, $.validator.format("至少需要上传一张商品图片"));
	
	
	//校验sku图片
	jQuery.validator.addMethod("skuPicValidate", function(value, element, param) {
		for(var i = 0;i<sellPriceDivNum+1;i++){
			var everyPicFlag = false;
			var isObj = false;
			for(var j = 0;j<=4;j++){
				var obj = $("#"+i+"_skuPicInUrl_"+j);
				if(obj.length > 0){
					isObj = true;
					if(obj.val() != ""){
						everyPicFlag = true;
					}
				}
			}
			if(isObj && !everyPicFlag){
				$(window).scrollTop($("input[name='skuPicShow']").offset().top - 100);
				return false;
			}
		}
		return true;
	}, $.validator.format("至少需要上传一张sku图片"));
	
	//校验库存 inventory 总库存必须等于sku库存总和
	jQuery.validator.addMethod("itemInventoryValidate", function(value, element, param) {
		var inventory = $("#inventory").val();
		var allInventory = 0;
		if(inventory){
			for(var i = 0;i<sellPriceDivNum+1;i++){
				var skuInventory = $("#"+i+"_skuInventory");
				if(skuInventory.length > 0){
					var skuInventoryFloatVal = parseFloat(skuInventory.val());
					if(!isNaN(skuInventoryFloatVal)){
						allInventory += parseFloat(skuInventory.val());
					}
				}
			}
			if(parseFloat(inventory) != allInventory){
				return false;
			}
		}
		return true;
	}, $.validator.format("商品库存量应等于sku库存总和"));
	
	//校验销售价是否符合规范
	jQuery.validator.addMethod("validateSellPrice", function(value, element, param) {
		if(!$("#hasPriceCheck").is(':checked')){
			//校验阶梯价规则
			var message = valueValidate("areaAllTable",true,true,"2");
			if(message != ""){
				param[1] = "地域价与阶梯价 【"+message+"】";
				return false;
			}
		}
		return true;
	}, $.validator.format("{1}"));
});

function vali(id){
	$("#"+id).validate({
		debug:true,
		rules: {
		}
	})
}

//校验阶梯价中的最大值 参数: 需要校验的表单的上级id、是否校验价格、是否校验地区、校验级别 :暂时只针对最大值，1:校验输入内容的非空、数字有效性 2:校验全部采购量的最大值、输入的有效性
function valueValidate(parentId,isValidatePrice,isValidateArea,validateLevel){
	if(parentId != ""){
		//校验最小采购量
		var minMessage = validateMinNum(parentId);
		if(minMessage != ""){
			return minMessage;
		}
		//校验地区
		if(isValidateArea){
			var areaMessage = validateArea(parentId);
			if(areaMessage != ""){
				return areaMessage;
			}
		}
		//校验最大采购量
		var maxMessage = validateMaxNum(parentId,validateLevel);
		if(maxMessage != ""){
			return maxMessage;
		}
		//校验价格
		if(isValidatePrice){
			var sellPriceMessage = validateSellPrice(parentId);
			if(sellPriceMessage != ""){
				return sellPriceMessage;
			}
		}
	}
	return "";
}
//校验最小采购量输入是否合法
function validateMinNum(parentId){
	var minNumObj = $("#"+parentId+" input[validateType='minNum']");
	var message = "";
	minNumObj.each(function(){
		var value = $(this).val();
		if(!value || value == ""){
			message = "最小采购量不允许为空";
			$(this).focus();
			return false;
		}
		if(!validateNum(value)){
			message = "最小采购量只允许输入数字";
			$(this).focus();
			return false;
		}
	});
	return message;
}

//校验最大采购量输入是否合法
function validateMaxNum(parentId,validateLevel){
	var obj = $("#"+parentId+" input[validateType='maxNum']");
	var message = "";
	var maxIndex = 0;
	//校验最大数量
	obj.each(function(index){
		var value = $(this).val();
		//判断是否为空，公用部分
		if(!value || value == ""){
			message = "最大采购量不允许为空";
			$(this).focus();
			return false;
		}
		if(!validateNum(value)){
			message = "最大采购量只允许输入数字";
			$(this).focus();
			return false;
		}
		//校验最大值是否大于最小值
		//获取最小值
		var minObj = $(this).prev();
		if(index == 0){
			if(minObj.length > 0 && minObj.val() != "1"){
				message = "最小采购量应从1开始";
				minObj.focus();
				return false;
			}
		}
		if(minObj.length > 0 && minObj.val()){
			if(parseInt(minObj.val()) >= parseInt(value)){
				message = "最大采购量应大于最小采购量";
				$(this).focus();
				return false;
			}
		}
	});
	//如果上面的校验都通过
	if(message == "" && validateLevel == "2"){
		var areaName = "";
		var boo = false;
		//校验每一个地区对应的最大采购量是否是闭环
		for(var i = 0;i<areaNum+1;i++){
			var areaNameObj = $("#"+parentId+" #areaName_"+i);
			if(areaNameObj.length>0){
				var num = 0;
				areaName = areaNameObj.val();
				for(var j = 0;j<cityAreaPriceNum+1;j++){
					var maxObj = $("#"+parentId+" #"+i+"_"+j+"_"+"maxNum");
					//校验采购数量是否连贯
					var nextMinObj = $("#areaPriceUl_"+j).next().find("li input[validatetype='minNum']");
					if(maxObj.length > 0 && nextMinObj.length > 0){
						var nextMinObjVal = parseFloat(nextMinObj.val());
						if((parseFloat(maxObj.val()) + 1) != nextMinObjVal){
							message = "采购数量不连贯";
							maxObj.focus();
							return message;
						}
					}
					if(maxObj.length > 0 && parseFloat(maxObj.val()) >= maxValueValidate){
						num ++ ;
					}
				}
				if(num == 0){
					$("#"+parentId+" input[validateType='maxNum']:last").focus();
					return "采购量必须包含大于"+maxValueValidate+"的值";
				}
				/*else if(num > 1){
					return "地区【"+areaName+"】采购量含有多个【最大值】";
				}*/
			}
		}
	}
	return message;
}

//校验销售价格输入是否合法
function validateSellPrice(parentId){
	var obj = $("#"+parentId+" input[validateType='sellPrice']");
	var message = "";
	obj.each(function(){
		var value = $(this).val();
		if(!value || value == ""){
			message = "销售价格不允许为空";
			$(this).focus();
			return;
		}
		if(!validateNum(value)){
			message = "销售价格只允许输入数字";
			$(this).focus();
			return;
		}
	});
	return message;
}

//校验地区是否选择
function validateArea(parentId){
	var obj = $("#"+parentId+" input[validateType='area']");
	var message = "";
	obj.each(function(){
		var value = $(this).val();
		if(!value || value == ""){
			message = "请选择地区";
			$(this).focus();
			return;
		}
	});
	return message;
}

//验证数字合法性
function validateNum(value){
	return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value);
}







