/*
 * 商品初始化
 */

///////////////////////全局变量///////////////////////////
var maxValueValidate = 9999999;
var areaNum = 0; //阶梯价中地区行标识
var cityAreaPriceNum = 0; //销售数量价格标识
var sellPriceDivNum = 0; //阶梯价弹出层标识
var areaPriceJson = ""; //地域价json
var areaPriceObj = {}; //添加商品对应的阶梯价
var productPicNum = 10;//商品图片的数量
var titleMap = {}; //选中的销售属性集合
var valueMap = {};//选中的销售属性值集合
var sellAttrOneMap = {}; //选中的sku集合
var skuArePriceTemp = {};
var saleCategoryAttr = "";//缓存选中的销售属性，主要在销售属性添加、商品编辑时使用。
var sellAttrNum = 0;
//存放地域数据
var bufferAreaIdIdVal = "";
var bufferAreaNameIdVal = "";
var showAreaNameIdVal = "";
var rowNumVal = "";
var tableIdVal = "";
var sellAttrOneMap={};
var sellSkuAttrMap = {}; 
var sellmap={};
///////////////////页面初始化////////////////////////
$(document).ready(function(){
	//加载左侧菜单
	$("#leftSeller").load(path + "/leftSeller");
	//引入效果方法
	xiaoguo();
	//绑定元素事件
	 bindEvent();
	//编辑操作数据初始化
	initEdit();
	//查看页面操作
	showView();
	//校验是否有店铺类目，如果没有则提示
	//var flag = validateShopCategory();
	//if(!flag){
	//	alert("请先设置店铺类目");
	//	window.location.href= path + "/sellcenter/sellProduct/goodsList";
	//} else{// 校验是否有运费模版，如果没有则提示
		flag = validateShopFreightTemplate();
		if(!flag){
			alert("请先设置运费模版");
			window.location.href= path + "/storecenter/freightTemplate/addFreightTemplate";
		}
	//}
    if(addSource==3){
		$("#saleAttrId").hide();
		$("#addSaleAttrId").hide();
		$("#packageGoodId").show();
		$("#addPackageGoodId").show();
		$("#shopCategoryDiv").hide();
		$("#baseServiceId").show();
		$("#addBaseId").show();
		$("#addbaseServiceId").show();
		$("#addserviceId").show();
		$("#auxiliaryId").show();
		$("#auxiliaryserviceId").show();
		if(isView){
			$("#selectNormal").css("display","none");
			$("#selectBase").css("display","none");
			$("#selectAdd").css("display","none");
			$("#selectAux").css("display","none");
		}
	}else{
		$("#saleAttrId").show();
		$("#addSaleAttrId").show();
		$("#packageGoodId").hide();
		$("#addPackageGoodId").hide();
		$("#shopCategoryDiv").show();
		$("#baseServiceId").hide();
		$("#addBaseId").hide();
		$("#addbaseServiceId").hide();
		$("#addserviceId").hide();
		$("#auxiliaryId").hide();
		$("#auxiliaryserviceId").hide();
	}
    if(addSource==''||addSource==null||addSource==undefined){
    	$("#goodTypeId").show();
    }else if(addSource=='1'||addSource=='3'){
    	$("#goodTypeId").hide();
    	var hideAddSorceId=$("#hideAddSorceId");
    	var html="<input type='hidden' name='addSource' value='"+addSource+"' id='addSource'/>";
    	hideAddSorceId.html(html);
    }else if(addSource=='4'||addSource=='5'||addSource=='6'){
    	$("#goodTypeId").show();
    }
    //普通商品选择商品
	$("#selectNormal").live("click",function(){
		$("#itemTypeId").val(1);
		$("#echoItemId").val("selectid");
		$("#checkedItemType").val("gene");
		queryItems();
		$("#confirmButton").attr("onclick","cofirmSku('')")
	});
	//选择基础服务商品
	$("#selectBase").live("click",function(){
		$("#itemTypeId").val(4);
		$("#echoItemId").val("selectChoiceid");
		$("#checkedItemIds").val("");
		$("#checkedItemNames").val("");
		$("#attrSaleStrList").val("");
		$("#skuInfoList").val("");
		$("#checkedItemType").val("basis");
		queryItems();
		$("#confirmButton").attr("onclick","confirmBase('','baseAddService','4','basis','basis_checkedItemIds','')")
	});
	//选择增值服务商品
	$("#selectAdd").live("click",function(){
		$("#itemTypeId").val(5);
		$("#echoItemId").val("selectZeng");
		$("#checkedItemType").val("added");
		queryItems();
		$("#confirmButton").attr("onclick","confirmBase('','valueaddId','5','added','added_checkedItemIds','')")
	});
	//选择辅助材料商品
	$("#selectAux").live("click",function(){
		$("#itemTypeId").val(6);
		$("#echoItemId").val("selectFu");
		$("#checkedItemType").val("aux");
		queryItems();
		$("#confirmButton").attr("onclick","confirmBase('','materialId','6','aux','aux_checkedItemIds','')")
	});
});

function copySpuAreaPrice(){
	var obj = $("#showPriDivOne");
	var skuSalePrice = $("#skuSalePriceId");
	queryValue(obj);
	obj.remove();
	skuSalePrice.append(obj.prop("outerHTML"));
	$("#showPriDivOne").removeClass("diloge_dijia").addClass("diloge_dijia1");
	$("#showPriDivOne").show();
}
function bianji(){
	var obj = $("#showPriDivOne");
	var itemSalePrice = $("#itemSalePriceId");
	queryValue(obj);
	itemSalePrice.append(obj.prop("outerHTML"));
	obj.remove();
	$("#showPriDivOne").removeClass("diloge_dijia1").addClass("diloge_dijia");
	$("#showPriDivOne").show();
}
function queryValue(obj){
	$("input,textarea,button", obj).each(function() {
		this.setAttribute('value',this.value);
	});
	$(":radio,:checkbox", obj).each(function() {
		if (this.checked) this.setAttribute('checked', 'checked');
		elsethis.removeAttribute('checked');
	});
	$("option", obj).each(function() {
		if (this.selected) this.setAttribute('selected', 'selected');
		elsethis.removeAttribute('selected');
	});
}
//选择基础服务点击确定按钮
function confirmBase(skuArrayVal,baseAddService,type,checkedItemType,selectid,lists){
	$("#"+baseAddService).empty();
	var sellAttrOneMap={};
	var sellSkuAttrMap = {}; 
	var sellmap={};
	var name='';
	var id='';
	$("#"+selectid).val();
	
	var checkedItemIds = $("#"+checkedItemType+"_checkedItemIds").val();
	var checkedItemNames = $("#"+checkedItemType+"_checkedItemNames").val();
	var attrSaleStrList = $("#"+checkedItemType+"_attrSaleStrList").val();
	var skuInfoS = $("#"+checkedItemType+"_skuInfoList").val();
	var itemIds = checkedItemIds.split(",");     
	var itemNames = checkedItemNames.split(",");     
	var attrsaleList = attrSaleStrList.split("&");     
	var skuInfoList = skuInfoS.split("&"); 
	if(itemIds!=null && itemIds!=""){
		for (var i=0;i<itemIds.length-1;i++)
		{
			var itemId =itemIds[i];
			var itemName=itemNames[i];
		    id+=itemId+",";
			if(skuArrayVal == ''){
				comboxDivShow('skuLoadMessage');
			}
			//组装选中的销售属性
			var attrsale = attrsaleList[i];
			var jsonobj=eval('('+attrsale+')'); 
			var skuInfo=skuInfoList[i];
			var skuInfo=eval('('+skuInfo+')'); 
			createServceHtml(sellAttrOneMap,sellSkuAttrMap,sellmap,itemName,itemId,jsonobj,skuInfo,type,baseAddService,selectid,lists)
		}
	}
	/*$('[name=checkItem]').each(function() {
		if (this.checked){
			var checkId = $(this).attr("id");
			var itemName=$("#"+checkId).attr("itemname");
			var obj = $("#"+checkId);
			var itemId=obj.attr("itemId");
			if(name==""||name==null||name==undefined){
				name=itemName;
				id=itemId;
			}else{
				name+=","+itemName;
				id+=","+itemId;
			}
			if(skuArrayVal == ''){
				comboxDivShow('skuLoadMessage');
			}
			//组装选中的销售属性
			var attrsale = $("#"+checkId).attr("attrSaleStr");
			var jsonobj=eval('('+attrsale+')'); 
			var skuInfo=$("#"+checkId).attr("skuinfo");
			var skuInfo=eval('('+skuInfo+')'); 
			createServceHtml(sellAttrOneMap,sellSkuAttrMap,sellmap,itemName,itemId,jsonobj,skuInfo,type,baseAddService,selectBaseItemId,selectid,lists)
		}
	});*/
	$("#"+selectid).val(id);
	$("#skuList").hide();
	if(skuArrayVal == ''){
		setTimeout("comboxDivHide('skuLoadMessage')",300); 
	}
}
function deleteChar(mine,selectId){
	var id=$(mine).parent().parent().attr('id');
	var itemId=$(mine).parent().parent().attr('itemId');
	var arr=$("#"+selectId).val().split(",");
	arr.remove(itemId);
	var iiid=arr.join(",");
	$("#"+selectId).val(iiid);
	$("#"+id).empty();
}
function queryItems(){
	//加载弹出层
	$("#skuList").show();
	$("#skuList .goodList").html("");
	var url = $("#queryForm").attr("action");
	var searchFormObj = $('#queryForm').serialize();
	$.ajax({
		url : url,
		type : "post",
		data : searchFormObj,
		dataType:"text",
		cache : false,
		success : function(html) {
			$("#skuList .goodList").html(html);
			//checkedIds();
		},
		error:function(e){
			alert('请求超时');
		}
	});
}


var realList;
function checkedIds(){
	$('input[name=checkItem]').each(function() {
		var id=$(this).attr("itemId");
		var checkId=$("#echoItemId").val();
		var itemids=$("#"+checkId).val();
		var ids = itemids.split(",");
		if(ids.length > 0){
			realList = ids;
		}
		if(realList.indexOf(id) != -1){
			$(this).attr("checked","checked");
		}
		
	})
}
//效果
function xiaoguo(){
	//初始化图片查看
	initPicView("findImg");
	//关闭地区弹出层
	 $("#city_close").click(function(){
		 $(".all_chain").hide();
	 });
	 $("#city_close").click(function(){
		 $(".all_chain").hide();
	 });
	 //图片悬浮层展示
	 $(".imgUl li").mouseover(function(){
		$(this).find(".c-upload").show();
	 });
	 $(".imgUl li").mouseout(function(){
		$(this).find(".c-upload").hide();
	 });
}
////////////////初始化事件绑定//////////////////////////
function bindEvent(){
	//平台分类 二级联动
	$("#itemCategoryLevelOne").change(function(){
		emptySelect('itemCategoryLevelTwo',"二级分类");
		emptySelect('itemCategoryLevelThree',"三级分类");
		//编辑页面中取消平台分类和商品属性、商品销售属性的关联
		if(!itemId && addSource==2){
			//清空sku信息
			clearSaleAttrAndSku();
		}
		optionItems($("#itemCategoryLevelOne").val(),"",1, "itemCategoryLevelTwo");
	});
	//平台分类 三级联动
	$("#itemCategoryLevelTwo").change(function(){
		emptySelect('itemCategoryLevelThree',"三级分类");
		optionItems($("#itemCategoryLevelOne").val(),$("#itemCategoryLevelTwo").val(),2, "itemCategoryLevelThree");
	});
	
	//选择第三级平台分类 
	$("#itemCategoryLevelThree").change(function(){
		//获取品牌
		emptySelect('brandId',"请选择");
		getShopBrands('brandId',$("#itemCategoryLevelThree").val());
		//编辑页面中取消平台分类和商品属性、商品销售属性的关联
		if(!itemId){
			if(addSource==2){
				//清空sku信息
				clearSaleAttrAndSku();
			}
			//获取平台商品属性
			emptySelect('platformAttributeDiv',"");
			//获取平台销售属性
			getPlantformAttribute('platformAttributeDiv',$("#itemCategoryLevelThree").val(),'','');
			//获取店铺销售属性
			getShopCategoryAttribute('shopCategoryDiv',$("#itemCategoryLevelThree").val(),'');
		}else{
			//获取平台商品属性
			emptySelect('platformAttributeDiv',"");
			//获取平台销售属性
			getPlantformAttribute('platformAttributeDiv',$("#itemCategoryLevelThree").val(),'','');
		}
	});
	//店铺分类
	$("#shopCategoryLevelOne").change(function(){
		var optionItem = $("#shopCategoryLevelTwo");
		optionItem.empty();
		optionItem.append($("<option>").text("二级分类").val(""));
		optionShopItems($("#shopCategoryLevelOne").val(), "shopCategoryLevelTwo");
	});
	// 根据运费模版初始化校验重量和体积
	if($("#shopFreightTemplateId").val()){
		$("#shopFreightTemplateLoadMessage").show();
		$.ajax({
			url:path + "/storecenter/freightTemplate/queryById",
			type:"post",
			data:{
				shopFreightTemplateId:$("#shopFreightTemplateId").val()
			},
			dataType: "json",
			success:function(data){
				if(data.success){
					var shopFreightTemplateDiv = $("#shopFreightTemplateDiv");
					shopFreightTemplateDiv.empty();
					// 显示运费模版详情
					var templateName = data.result.templateName;
					var ul = $("<ul>");
					ul.append("<li>模版名称：" + templateName + "</li>");
					if(data.result.valuationWay){
						if(data.result.valuationWay == 2){// 计价方式：重量
							$("#weightRequired").show();
							$("#weight").rules("add",{required: true});
						}else if(data.result.valuationWay == 3){// 计价方式：体积
							$("#volumeRequired").show();
							$("#volume").rules("add",{required:true});
						}
					}
					shopFreightTemplateDiv.append(ul);
				}else{
					alert("操作失败!");
				}
				setTimeout("comboxDivHide('shopFreightTemplateLoadMessage')",300); 
			},
			error:function(evt, request, settings){
				setTimeout("comboxDivHide('shopFreightTemplateLoadMessage')",300); 
				alert("请求超时!");
			}
		});
	}
	// 运费模版切换
	$("#shopFreightTemplateId").change(function(){
		var shopFreightTemplateDiv = $("#shopFreightTemplateDiv");
		shopFreightTemplateDiv.empty();
		if($(this).val()){
			$.ajax({
				url:path + "/storecenter/freightTemplate/queryById",
				type:"post",
				data:{
					shopFreightTemplateId:$(this).val()
				},
				dataType: "json",
				success:function(data){
					$("#shopFreightTemplateLoadMessage").show();
					if(data.success){
						// 显示运费模版详情
						var templateName = data.result.templateName;
						var ul = $("<ul>");
						ul.append("<li>模版名称：" + templateName + "</li>");
						if(data.result.valuationWay == 2){// 计价方式：重量
							$("#weightRequired").show();
							$("#volumeRequired").hide();
							$("#weight").rules("add",{required: true});
							$("#volume").rules("remove","required");
						}else if(data.result.valuationWay == 3){// 计价方式：体积
							$("#volumeRequired").show();
							$("#weightRequired").hide();
							$("#volume").rules("add",{required:true});
							$("#weight").rules("remove","required");
						}else{
							$("#weight").rules("remove","required");
							$("#volume").rules("remove","required");
							$("#weightRequired").hide();
							$("#volumeRequired").hide();
						}
						shopFreightTemplateDiv.append(ul);
					}else{
						alert("操作失败!");
					}
					setTimeout("comboxDivHide('shopFreightTemplateLoadMessage')",300); 
				}
			});
		}
	});
	//点击暂无报价按钮
	 $("#hasPriceCheck").click(function(){
		 if($(this).is(':checked')){
			 $(".mandatory").css('display','none');
		 }else{
			 $(".mandatory").css("display",""); 
		 }
		
	 });
}


//校验店铺类目
function validateShopCategory(){
	var shopCategorySelect = $("#shopCategoryLevelOne option");
	var flag = false;
	shopCategorySelect.each(function () {
		if($(this).val() != ""){
			flag = true;
		}
	});
	return flag;
}

//校验运费模版
function validateShopFreightTemplate(){
	var shopFreightTemplateSelect = $("#shopFreightTemplateId option");
	var flag = false;
	shopFreightTemplateSelect.each(function () {
		if($(this).val() != ""){
			flag = true;
		}
	});
	return flag;
}


//////////////////查看、编辑数据初始化//////////////////////////
function showView(){
	if(isView){
		$("input").not("#keyword_searchForm")
			.attr("readonly","readonly");
		$("textarea").attr("disabled","disabled");
		$("select").attr("disabled","disabled");
		$("input[type='checkbox']").attr("disabled","disabled");
	}
}

//编辑操作
function initEdit(){
	if(plstItemId){
		//获取平台销售属性
		getPlantformAttribute('platformAttributeDiv',attributesStr,plstItemId,'change');
		//获取产品销售属性
		getShopCategoryAttribute('shopCategoryDiv',attrSaleStr,plstItemId);
		if(itemId != ""){
			//获取店铺类目
			$("#shopCategoryLevelOne").change();
			//获取商品对象
			var itemDTOJson = itemDTOJsonStr;
			//获取商品对应的阶梯价
			initItemSellPrices(itemDTOJson.sellPrices);
		}
	}else if(itemId){
		if($("#hasPrice").val() == 2){
			 $(".mandatory").css('display','none');
		}else{
			 $(".mandatory").css('display','');
		}
		//getSkuAttribute();
		//平台分类
		$("#itemCategoryLevelThree").change();
		//获取店铺类目
		$("#shopCategoryLevelOne").change();
		
		//获取平台销售属性
		getPlantformAttribute('platformAttributeDiv',$("#itemCategoryLevelThree").val(),'','change');
		//获取店铺销售属性
		getShopCategoryAttribute('shopCategoryDiv','','');
		
		$("select[name='addSource']").attr("readonly","readonly");//编辑时商品类型不可编辑
		//获取商品对象
		var itemDTOJson = itemDTOJsonStr;
		//获取商品对应的阶梯价
		initItemSellPrices(itemDTOJson.sellPrices);
		//initSkuList(itemDTOJson.attrSaleStr,itemDTOJson.skuInfos);
	}
}

//初始化sku数据，主要在编辑页面中使用
function initSkuInfo(){
	//加载弹出层
	$("#skuLoadMessage").show();
	//获取商品对象
	var itemDTOJson = itemDTOJsonStr;
	//缓存sku数据
	$.each(itemDTOJson.skuInfos,function(n,skuInfo){
		var skuArrayValTemp = {};		
		skuArrayValTemp["isAllContry"] = false;
		skuArrayValTemp["skuInfos"] = skuInfo;
		skuArePriceTemp[skuInfo.attributes] = skuArrayValTemp;
	});
	//加载sku列表
	if(addSource==3){
		initGroupSkuList(itemDTOJson.skuInfos);
	}else{
		initSkuList(itemDTOJson.attrSaleStr,itemDTOJson.skuInfos);
	}
	//$("#skuLoadMessage").hide();
	setTimeout("comboxDivHide('skuLoadMessage')",300); 
}





//获取平台属性
function getPlantformAttribute(optionId,cid,plantItemId,change){
	var url = "/sellcenter/sellProduct/getPlantformAttribute";
	if(itemId&&change){
		url = "/sellcenter/sellProduct/getItemSaleAttrs";
	}
	$.ajax({
		url: path + url,
		type:"post",
		data:{
			cid:cid,
			plantItemId:plantItemId,
			attrType:2,
			itemId:itemId
		},
		dataType: "json",
		success:function(data){
			var optionItem = $("#"+optionId);
			optionItem.empty();
			//遍历属性
			$.each(data.result, function (n, pf) {
				 if(!plstItemId && !change){
					//添加销售属性
					optionItem.append("<li id='li_"+pf.attrAttrId+"'><p class='wid_90 hei_35 font_right fl cursor over_ell' title='"+pf.attrAttrName+"'>"+pf.attrAttrName+":<p/></li>");
					var optionItemEvery = $("#li_"+pf.attrAttrId);
					//添加属性值
					$.each(pf.valueList, function (n, categoryVal) {
						var select = "";
						if(attributesStr.indexOf(pf.attrAttrId+":"+categoryVal.attrValueId+";")>=0){
							select = "checked='checked'";
						}
						optionItemEvery.append("<p class='fl wid_110  hei_35 cursor over_ell' title='"+categoryVal.attrValueName+"'><input type=\"checkbox\" "+
								select+" name='plantformVal' plantAttrId='"+pf.attrAttrId+
								":"+categoryVal.attrValueId+";' id='plC_"+categoryVal.attrValueId+
								"' value='"+categoryVal.attrValueId+"' class=\"mar_lr10 \" />"
								+categoryVal.attrValueName+"</p>");
					});
				 }else{
					//添加销售属性
					optionItem.append("<li  id='li_"+pf.id+"'><p class='wid_90 hei_35 font_right fl cursor over_ell' title='"+pf.name+"'>"+pf.name+"：</p></li>");
					var optionItemEvery = $("#li_"+pf.id);
					//添加属性值
					$.each(pf.values, function (n, categoryVal) {
						var select = "";
						if(!itemId){
							//如果是平台上传添加则默认选中
							select = "checked='checked'";
						}else{
							//如果是编辑则将添加时的属性选中
							if(attributesStr.indexOf(pf.id+":"+categoryVal.id+";")>=0){
								select = "checked='checked'";
							}
						}
						optionItemEvery.append("<span class='cursor fl over_ell  hei_35' style='width:150px' title='"+categoryVal.name+"'><input type=\"checkbox\" "+
								select+" name='plantformVal' plantAttrId='"+pf.id+
								":"+categoryVal.id+";' id='plC_"+categoryVal.id+
								"' value='"+categoryVal.id+"' class=\"mar_lr20\" />"+
								categoryVal.name+"</span>");
					});
				 }
			});
			if(isView){
				$("input[type='checkbox']").attr("disabled","disabled");
			}
		}
	});
}
//加载商品阶梯价
function initItemSellPrices(sellPricesObj){
	initItemOrSkuSellPrices("","areaAllTable","areaPriceAfterDiv_","areaPriceSaveBtn",sellPricesObj);
}
//组装阶梯价 主要在编辑中初始化数据的时候使用
function initItemOrSkuSellPrices(namePre,tableId,areaPriceAfterDiv,saveBtn,sellPricesObj,skuAreaPriceQGTableId){
	var areaObj = {};
	var isClick = false;
	var contryHtml = "";
	var isAllContry = false;
	//组装阶梯价
	$.each(sellPricesObj,function(n,itemSellPrice){
		isClick = true;
		cityAreaPriceNum++;
		var namePrefix = namePre + "sellPrices["+cityAreaPriceNum+"].";
		var areaId = itemSellPrice.areaId;
		if(areaObj[areaId] == null){
			var html = "";
			//添加地区行
			areaNum ++;
			areaObj[areaId] = areaNum;
			var areaNumSelf = areaObj[areaId];
			var idPrefix = areaNumSelf + "_" + cityAreaPriceNum + "_";
			//添加全国
			if(areaId == 0 || areaId==""){
				var addToArray = {};
				addToArray["isAddAreaPriceContent"] = false;
				addToArray["isAllContry"] = true;
				addToArray["areaId"] = "0";
				addToArray["areaName"] = "全国";
				html = areaContentHtml(namePre,namePrefix,idPrefix,areaNumSelf,cityAreaPriceNum,'',areaNum,'',addToArray);
				isAllContry = true;
			}else{
				//添加地区行
				var addToArray = {};
				addToArray["isAddAreaPriceContent"] = false;
				addToArray["isAllContry"] = false;
				addToArray["areaId"] = areaId;
				addToArray["areaName"] = itemSellPrice.areaName;
				html = areaContentHtml(namePre,namePrefix,idPrefix,areaNumSelf,cityAreaPriceNum,"",areaNum,'',addToArray);
				isAllContry = false;
			}
			$("#"+tableId).append(html);
		}
		//添加地区对应的阶梯价
		var num = areaObj[areaId];
		var idPrefix = num + "_" + cityAreaPriceNum + "_";
		var areaPriceHtml = areaPriceContentHtml(namePrefix,idPrefix,cityAreaPriceNum,"","",itemSellPrice);
		$("#"+areaPriceAfterDiv+num).before(areaPriceHtml);
		//拼装全国阶梯价
		if(isAllContry){
			contryHtml += "<tr>";
			contryHtml += "<td class='wid_60 font_cen hei_24'>"+itemSellPrice.minNum+" - "+itemSellPrice.maxNum+"</td>";
			contryHtml += "<td class='wid_60 font_cen hei_24'>"+itemSellPrice.sellPrice+"</td>";
			contryHtml += "</tr>";
		}
	});
	if(isClick){
		if(!skuAreaPriceQGTableId){
			$("#"+saveBtn).click();
		}else{
			$("#"+skuAreaPriceQGTableId).html($("#"+skuAreaPriceQGTableId).html() + contryHtml);
		}
	}
}
function initGroupSkuList(skuListJson){
	var skuArrayVal = {};
	skuArrayVal["isAllContry"] = false;
	skuArrayVal["skuInfos"] = skuListJson;
	clickGroup(skuArrayVal);
}
//加载sku列表
function initSkuList(attrsale,skuListJson){
	var categoryArray = attrsale.split(";");
	var skuArrayVal = {};
	skuArrayVal["isAllContry"] = false;
	skuArrayVal["skuInfos"] = skuListJson;
	for(var j = 0;j<categoryArray.length;j++){
		var categoryIdVal = categoryArray[j].split(":")[1];
		//组装选中的销售属性
		var objId = "check_"+categoryIdVal;
		var obj = $("#"+objId);
		var sellAttrId = obj.attr("sellAttrId");
		var sellAttrName = obj.attr("sellAttrName");
		var sellAttrValName = obj.attr("sellAttrValName");
		var sellAttrValId = obj.attr("sellAttrValId");
		var key = sellAttrId + ":" + sellAttrValId;
		//如果是选中状态
		if(obj.is(':checked')){
			if(sellAttrOneMap[sellAttrId] == null){
				var mapValue = new Array();
				mapValue[0] = sellAttrValId;
				sellAttrOneMap[sellAttrId] = mapValue;
			}else{
				var mapValue = sellAttrOneMap[sellAttrId];
				mapValue.push(sellAttrValId);
				sellAttrOneMap[sellAttrId] = mapValue;
			}
			titleMap[sellAttrId] = sellAttrName;
			valueMap[sellAttrValId] = sellAttrValName;
		}
	}
	getSkuList(skuArrayVal,'');
}


////////////////////////店铺联动//////////////////////////
function optionShopItems(parentCode, optionId){
	$.ajax({
		url: path + "/sellcenter/sellProduct/getShopCategory",
		type:"post",
		data:{
			parentCid:parentCode
		},
		dataType: "json",
		success:function(data){
			if(optionId == "shopCategoryLevelTwo"){
				if(data.result.length == 0){
					alert("未设置二级店铺类目");
				}
			}
			//获取地域数据
			var optionItem = $("#"+optionId);
			$.each(data.result, function (n, category) {
				var option = $("<option>").text(category.cname).val(category.cid);
				if(category.cid == shopCid){
					option.attr("selected",true);
				}
				optionItem.append(option);
			});
		}
	});
}

/////////////////平台分类///////////////////////////////
function optionItems(levelOneVal,levelTwoVal,level,optionId){
	$.ajax({
		url:path + "/sellcenter/sellProduct/getItemCategorys",
		type:"post",
		data:{
			levelOneId:levelOneVal,
			levelTwoId:levelTwoVal,
			level:level
		},
		dataType: "json",
		success:function(data){
			//获取地域数据
			var optionItem = $("#"+optionId);
			$.each(data.result, function (n, category) {
				var option = $("<option>").text(category.cname).val(category.cid);
				optionItem.append(option);
			});
		}
	});
}

//////////////////获取经营的品牌///////////////////////////
function getShopBrands(optionId,cid){
	$.ajax({
		url:path + "/sellcenter/sellProduct/getShopBrands",
		type:"post",
		data:{
			cid:cid
		},
		dataType: "json",
		success:function(data){
			//获取地域数据
			var optionItem = $("#"+optionId);
			$.each(data.result, function (n, category) {
				var option = $("<option>").text(category.brandName).val(category.brandId);
				if(category.brandId == brand){
					option.attr("selected",true);
				}
				optionItem.append(option);
			});
		}
	});
}

//取消按钮
function cancle(page,rows){
	if(!isView){
		if(confirm("确定要放弃本次操作吗？")){
			window.location.href= path + "/sellcenter/sellProduct/goodsList?page="+page+"&rows="+rows+"&oldRows="+rows;//旧的每页显示条数和新的一样 因为查看 编辑 发布 上下架  均未操作每页显示条数这个属性，相等是为了在这个方法中goodsList()让每页的显示条数保持不变
	 	}
	}else{
		window.location.href= path + "/sellcenter/sellProduct/goodsList?page="+page+"&rows="+rows+"&oldRows="+rows;
	}
}

//////////////////////工具方法///////////////////////
//初始化图片查看
function initPicView(elementClassName){
	$('.'+elementClassName).fancyzoom({
		Speed:400,
		showoverlay:false,
		imgDir: path + '/assets/plugin/fancyzoom/ressources/'
	});
}
//删除元素
function removeElement(id){
	$("#"+id).remove();
}
//清空下拉列表
function emptySelect(id,defaultVal){
	var obj = $("#"+id);
	obj.empty();
	if(defaultVal != ""){
		obj.append($("<option>").text(defaultVal).val(""));
	}
}	
function cancleSaveSellAttr(hideId,hideBtn){
	$("#"+hideId).empty();
	$("#"+hideBtn).hide();
}
//图片上传实现类
function startUpload(fileElementId, showImg,urlInput,showElementId){
	$.ajaxFileUpload({
		url: path + '/fileUpload/fixedUpload?date='+new Date(), //用于文件上传的服务器端请求地址
		secureuri: false, //是否需要安全协议，一般设置为false
		fileElementId: fileElementId, //文件上传域的ID
		dataType: 'json', //返回值类型 一般设置为json
		data:{
			size:1048576
		},
		type:"post",
		success: function (data, status){  //服务器成功响应处理函数
			if(data.success){
				if(!data.url || data.url==null || data.url==""){
					alert("图片上传失败，请重新上传！");
					$("#"+showImg).attr("src","");
					return;
				}
				$("#"+showImg).attr("src",imageServerAddr+data.url);
				$("#"+urlInput).val(data.url);
				$("#"+fileElementId).val("");
				if(showElementId != ""){
					$("#"+showElementId).show();
				}
			}else{
				alert(data.msg);
			}
		},
		error: function (data, status, e){//服务器响应失败处理函数
		}
	});
};
//删除数组元素
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};
//弹出层显示
function comboxDivShow(id){
	$("#"+id).show();
}

function comboxDivShowAAA(id){
	$("#"+id).show();
	$(this).css("style","display:none;z-index:2;top:955px;left:400px");
}
//弹出层隐藏
function comboxDivHide(id){
	$("#"+id).hide();
}
//输入字符显示
function numInput(obj,length){
	if(obj.value==obj.value2)
		return;
	if(length == 0 && obj.value.search(/^\d*$/)==-1)
		obj.value=(obj.value2)?obj.value2:'';
	else if(obj.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)
		obj.value=(obj.value2)?obj.value2:'';
	else obj.value2=obj.value;
}

/////////////保存 编辑//////////////////
//组装商品属性
function attributeHtml(plantSellCategory){
	var attributeObj = $("#allAttributesDiv");
	var id = "";
	var html = "";
	var attr_num = -1;
	var attr_value_num = 0;
	var attributesStr = ""; //选中的非销售属性
	$.each(plantSellCategory, function (n, category) {
		var plantattrid = $("#"+category.id).attr("plantattrid");
		if($("#"+category.id).is(':checked')){
			attributesStr += plantattrid;
		}
		var values = plantattrid.replace(";","").split(":");
		var attrId = values[0];
		var attrValueId = values[1];
		if(id != attrId){
			attr_num++;
			html += "<input type='hidden' name='attributes["+attr_num+"].id' value='"+attrId+"' />";
			attr_value_num = 0;
			id = attrId;
		}
		html += "<input type='hidden' name='attributes["+attr_num+"].values["+attr_value_num+"].id' value='"+attrValueId+"' />";
		attr_value_num ++ ;
	});
	attributeObj.html(html);
	$("#attributesStr").val(attributesStr);
}
function saleSkuHtml(skuTaoCan){
	var html = "";
	var flag=false;
	var attr_num = -1;
	var attrSaleObj = $("#selectAttrSku");
	$.each(skuTaoCan, function (n, category) {
		attr_num++;
		var skuid = $(this).attr("skuid");
		var addSource = $(this).attr("addSource");
		var subNum=$(this).parent().parent().find(".sub_num").val();
		html += "<input type='hidden' name='itemSkuPackageDTOs["+attr_num+"].subSkuId' value='"+skuid+"' />";
		html += "<input type='hidden' name='itemSkuPackageDTOs["+attr_num+"].addSource' value='"+addSource+"' />";
		html += "<input type='hidden' name='itemSkuPackageDTOs["+attr_num+"].subNum' value='"+subNum+"' />";
		if(subNum==""||subNum==null||subNum==undefined||subNum==0){
			flag=true;
		}
	});
	attrSaleObj.html(html);
	return flag;
}
//组装商品销售属性
function saleHtml(sellCategory,addSource){
	var attrSaleObj = $("#allAttrSalesDiv");
	var id = "";
	var html = "";
	var attr_num = -1;
	var attr_value_num = 0;
	var attrSaleStr = ""; //选中的非销售属性
	var array=[];
	$.each(sellCategory, function (n, category) {
		if(addSource==3){//组合套装商品
			var ass = $("#"+category.id).attr("skuAttributes");
			if($("#"+category.id).is(':checked')){
				if(attrSaleStr==""){
					attrSaleStr=ass;
				}else{
					attrSaleStr += "|"+ass;
				}
			}
		}else{//普通商品
			var ass = $("#"+category.id).attr("attrsalestr");
			if($("#"+category.id).is(':checked')){
				attrSaleStr += ass;
			}
		}
		if(addSource==3){//组合套装商品
			var ass=ass.substring(0,ass.length-1);
			var values=ass.split(";");
			for(var i=0;i<values.length;i++){
				if(array.indexOf(values[i])==-1){//不存在
					array.push(values[i]);
				}else{
					continue;
				}
				var attrSals=values[i].split(":");
					var attrId = attrSals[0];
					var attrValueId = attrSals[1];
					if(id != attrId){
						attr_num++;
						html += "<input type='hidden' name='attrSale["+attr_num+"].id' value='"+attrId+"' />";
						attr_value_num = 0;
						id = attrId;
					}
					html += "<input type='hidden' name='attrSale["+attr_num+"].values["+attr_value_num+"].id' value='"+attrValueId+"' />";
					attr_value_num ++ ;
			}
		}else{ //普通商品
			var values = ass.replace(";","").split(":");
			var attrId = values[0];
			var attrValueId = values[1];
			if(id != attrId){
				attr_num++;
				html += "<input type='hidden' name='attrSale["+attr_num+"].id' value='"+attrId+"' />";
				attr_value_num = 0;
				id = attrId;
			}
			html += "<input type='hidden' name='attrSale["+attr_num+"].values["+attr_value_num+"].id' value='"+attrValueId+"' />";
			attr_value_num ++ ;
		}
	});
	attrSaleObj.html(html);
	$("#attrSaleStr").val(attrSaleStr);
}

//保存商品
function saveGood(status,btnId,page,rows){
	//是否有报价
	var hasPrice = "1";
	if($("#hasPriceCheck").is(':checked')){
		hasPrice = "2";
	}
	$("#hasPrice").val(hasPrice);
	//商品描述
	$("#describeUrl").val(UE.getEditor('editor').getContent());
	//规格参数
	$("#specification").val(UE.getEditor('editor2').getContent());
	//服务支持
	$("#afterService").val(UE.getEditor('editor4').getContent());
	//定时上架
	var timingListing = "";
	if($('#timeState').is(':checked')) {
		timingListing = $("#timingListingDate").val();
	}
	$("#timingListing").val(timingListing);
	$("#itemStatus").val(status);
	//获取选中的平台销售属性
	var plantSellCategory = $("#platformAttributeDiv input[type='checkbox']");
	//加载商品属性
	attributeHtml(plantSellCategory);
	//获取店铺销售属性
	var str="";
	if(addSource==3){
		var mark=false;
		$("input[name='sub_num']").each(function(){
		   var itemId=$(this).attr("id");
		   var value=$(this).val();
		   if(value==""||value==null||value==undefined||value==0){
			  mark=true;
		   }
		   if(str==""){
			   str=itemId+":"+value; 
		   }else{
			   str+=";"+itemId+":"+value;
		   }
		});
		if(mark){
			alert("普通商品的数量不能为空且不能为0！");
			return;   
		}
		$("#subNumId").val(str);
		var sellCategory = $("#groupSkuId input[type='checkbox']");
		var skuTaoCan=$("input:checkbox[name='serviceAttrCheck']:checked");
		var flag=saleSkuHtml(skuTaoCan);
		if(flag){
			alert("服务商品的数量不能为空且不能为0！");
			return;
		}
	}else{
		var sellCategory = $("#shopCategoryDiv input[type='checkbox']");
	}
	saleHtml(sellCategory,addSource);
	//获取商品图片
	var picUrl = "";
	for(var i = 0;i<productPicNum;i++){
		if($("#picUrls_"+i).length > 0 && $("#picUrls_"+i).val() != ""){
			if(picUrl != ""){
				picUrl += ",";
			}
			picUrl += $("#picUrls_"+i).val();
		}
	}
	$("#itemPics").val(picUrl);
	
    if(itemId != ""){
		$("#itemForm").attr("action",path + "/sellcenter/sellProduct/edit");
	}
	$("#"+btnId).loadingMsg();
	//校验
	var isValid = $("#itemForm").valid();
	if(!isValid){
		$("#"+btnId).hideMsg();
		return;
	}
	//计算总库存
	calculationInventory();
	
	//比较规则:市场指导价>报价>底价    如果不符合逻辑则
	//市场指导价
	var marketPrice = $("#marketPrice").val();
	//报价
	var guidePrice = $("#guidePrice").val();
	//底价
	var marketPrice2 = $("#marketPrice2").val();
	
	var message = "";
	if(parseFloat(marketPrice) <= parseFloat(guidePrice)){
		message = "商品的市场指导价没有大于报价,确定要提交吗?";
	}
	if(message == "" && parseFloat(guidePrice) <= parseFloat(marketPrice2)){
		message = "商品的报价没有大于底价,确定要提交吗?";
	}
	if(message != ""){
		var isCon = false;
		if(confirm(message)){
			isCon = true;
		}
		if(!isCon){
			$("#"+btnId).hideMsg();
			return;
		}
	}
	$.ajax({
		url:path + "/sellcenter/sellProduct/validateItemName",
		type:"post",
		data:{
			itemName:$('#itemName').val(),
			itemId:itemId
		},
		dataType: "json",
		success:function(data){
			if(data.success){
				submit(page,rows);
			}else{
				if(confirm("您的店铺已有相同名称的商品，是否继续保存？")){
					submit(page,rows);
				}else{
					$("#"+btnId).hideMsg();
				}
			}
		}
	});
	
	function submit(page,rows){
		//$("#itemForm").submit();
		var url = $("#itemForm").attr("action");
		var itemFormObj = $('#itemForm').serialize();
		$.ajax({
			url:url,
			type:"post",
			data:itemFormObj,
			dataType: "json",
			timeout: 60000,
			success:function(data){
				if(data.result){
					alert("操作成功!");
					window.location.href = path + "/sellcenter/sellProduct/goodsList?page="+page+"&rows="+rows+"&oldRows="+rows;
				}else if(data.message){
					alert(data.message);
				}else{
					alert("操作失败!");
				}
				$("#"+btnId).hideMsg();
			},
			error:function(evt, request, settings){
				if(evt.status == "600" ){
			    	$("#winFastLogin").show();
				}else{
					alert("请求超时!");
				}
				$("#"+btnId).hideMsg();
			}
		});
	}
};
//确定阶梯价
function confirmLadder(){
		//校验阶梯价规则
		var validateMessage = valueValidate("areaAllTable",true,true,"2");
		if(validateMessage != ""){
			alert(validateMessage);
			return false;
		}
		//解析添加的阶梯价
		areaPriceJson = "[";
		//将全国的阶梯价展示到页面上
		var showAllContryAreaPrice = "[";
		var key = 0;
		for(var i = 0;i<areaNum+1;i++){
			var areaIdObj = $("#areaAllTable #areaId_"+i);
			if(areaIdObj.length>0){
				var areaIdVal = $("#areaAllTable #areaId_"+i).val();
				var areaNameVal = $("#areaAllTable #areaName_"+i).val();
				var valueJson = "";
				for(var j = 0;j<cityAreaPriceNum+1;j++){
					//获取销售数量、销售价格、地区并组装成json
					var sellMinNumObj = $("#areaAllTable #"+i+"_"+j+"_minNum");
					//判断对象是否存在
					if(sellMinNumObj.length>0){
						var minNum = sellMinNumObj.val();
						var maxNum = $("#areaAllTable #"+i+"_"+j+"_"+"maxNum").val();
						var sellPrice = $("#areaAllTable #"+i+"_"+j+"_"+"sellPrice").val();
						//组装阶梯价格json
						if(areaPriceJson != "["){
							areaPriceJson += ",";
						}
						var areaPriceJsonOne = "{\"minNum\":\""+minNum+"\",\"maxNum\":\""+maxNum+"\",\"sellPrice\":\""+sellPrice+"\",\"areaId\":\""+areaIdVal+"\",\"areaName\":\""+areaNameVal+"\"}";
						areaPriceJson += areaPriceJsonOne;
						//组装全国阶梯价json
						if(areaIdVal == "0"){
							if(showAllContryAreaPrice != "["){
								showAllContryAreaPrice += ",";
							}
							showAllContryAreaPrice += areaPriceJsonOne;
						}
						//组装商品阶梯价
						if(valueJson != ""){
							valueJson += ",";
						}
						valueJson += areaPriceJsonOne;
					}
				}
				//将商品阶梯价组装成json格式
				if(valueJson != ""){
					valueJson = "[" + valueJson +"]";
				}
				areaPriceObj[key++] = valueJson;
			}
		}
		areaPriceJson+="]";
		//将全国阶梯价展示到页面上
		areaPrice(showAllContryAreaPrice+"]","areaPriceTable");
		areaPrice(showAllContryAreaPrice+"]","skuAreaPriceQGTable");
		$("#showPriDivOne").hide();
	}
