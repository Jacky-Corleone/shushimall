/*
 * 商品sku实现
 */
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
//点击销售属性sku 组装sku列表
function clickSellAttrClick(objId,formId,skuArrayVal){
	//操作sku提示
	var obj = $("#"+objId);
	if(doType != ""){
		var flag = false;
		if(confirm("修改sku属性可能影响商品促销活动的正常使用，请您谨慎操作！是否继续?")){
			flag = true;
		}
		if(!flag){
			if(obj.is(':checked')){
				$("#"+objId).removeAttr("checked");
			}else{
				$("#"+objId).attr("checked","true");
			}
			return;
		}
	}
	if(skuArrayVal == ''){
		comboxDivShow('skuLoadMessage');
	}
	//组装选中的销售属性
	var sellAttrId = obj.attr("sellAttrId");
	var sellAttrName = obj.attr("sellAttrName");
	var sellAttrValName = obj.attr("sellAttrValName");
	var sellAttrValId = obj.attr("sellAttrValId");
	var key = sellAttrId + ":" + sellAttrValId;
	//如果是选中状态
	if(obj.is(':checked')){
		//sellAttrOneMap  存放选中的销售属性，如果里面没有此销售属性key则新增，如果里面有则先删除再新增
		if(sellAttrOneMap[sellAttrId] == null){
			var mapValue = new Array();
			mapValue[0] = sellAttrValId;
			sellAttrOneMap[sellAttrId] = mapValue;
		}else{
			var mapValue = sellAttrOneMap[sellAttrId];
			mapValue.push(sellAttrValId);
			sellAttrOneMap[sellAttrId] = mapValue;
		}
		//存放sku标题
		titleMap[sellAttrId] = sellAttrName;
		//存放属性名称，在组装sku表格时显示使用
		valueMap[sellAttrValId] = sellAttrValName;
	}else{
		var mapValue = sellAttrOneMap[sellAttrId];
		mapValue.remove(sellAttrValId);
		if(mapValue.length == 0){
			delete sellAttrOneMap[sellAttrId];
		}else{
			sellAttrOneMap[sellAttrId] = mapValue;
		}
	}
	getSkuList(skuArrayVal,objId);
}
//组装sku列表
function getSkuList(skuArrayVal,objId){
	var obj = $("#"+objId);
	var sellAttrId = obj.attr("sellAttrId");
	//解析选中的销售属性集合
	var skuStr = "[";
	var index = 0;
	for(var keyEvery in sellAttrOneMap){
		var mapValue = sellAttrOneMap[keyEvery];
		if(mapValue.length > 0){
			if(index > 0){
				skuStr += ",";
			}
			skuStr += "[";
			for(var i = 0;i<mapValue.length;i++){
				if(i > 0)
					skuStr += ",";
				skuStr += "\""+keyEvery + ":" + mapValue[i]+"\"";
			}
			skuStr += "]";
		}
		index ++ ;
	}
	skuStr += "]";
	//获取sku列表
	$.ajax({
		url: path + "/sellcenter/sellProduct/getSkuList",
		type:"post",
		data:{
			skuStr:skuStr
		},
		dataType: "json",
		success:function(data){
			if(data.skuList.length > 0){
				if(data.flag==true){
					alert("最多sku数量不允许超过"+data.maxNum+"个");
					var mapValue = sellAttrOneMap[sellAttrId];
					mapValue.remove(obj.attr("sellAttrValId"));
					comboxDivHide('skuLoadMessage');
					$("#"+objId).removeAttr('checked')
					return;
				}else{
					//清空sku信息
					clearSkuInfo();
					$("#skuTable").show();
				}
				
			}else{
				$("#skuTable").hide();
			}
			var titleObj = {};
			for(var j in sellAttrOneMap){
				titleObj[j] = titleMap[j];
			}
			//添加标题
			skuTitleContentHtml(titleObj,"skuSellAreaPriceTd");
			
			//确定删除的元素
			if((skuArrayVal == null || skuArrayVal == "")){
				for (var p in skuArePriceTemp){
					var isDelete = true;
					$.each(data.skuList, function (n, pf) {
						if(p == pf.split(",").join(';')+";"){
							isDelete = false;
							return;
						}
					})
					if(isDelete){
						delete skuArePriceTemp[p];
					}
				}
			}
			//添加sku行内容
			$.each(data.skuList, function (n, pf) {
				var pfArray = pf.split(",");
				var skuAttributes = pf.split(",").join(';')+";";
				var valueObj = new Array();
				//var valueObj = {};
				for(var i = 0;i<pfArray.length;i++){
					var valueArray = pfArray[i].split(":");
					var valueId = valueArray[1];
					var valueName = valueMap[valueId];
					var Circle={  
						 "id":valueId,  
						 "name":valueName
					}
					//valueObj[valueId] = valueName;
					valueObj.push(Circle);
				}
				//追加sku阶梯价
				sellPriceDivNum++;
				areaNum ++;
				cityAreaPriceNum++;
				var skuArrayVal_ = skuArrayVal;
				if((skuArrayVal == null || skuArrayVal == "") && skuArePriceTemp[skuAttributes] != null){
					skuArrayVal_ = skuArePriceTemp[skuAttributes];
				}
				skuContentHtml(sellPriceDivNum,areaNum,cityAreaPriceNum,valueObj,"skuTable",skuAttributes,skuArrayVal_,'');
			});
			//隐藏加载内容
			if(skuArrayVal == ''){
				setTimeout("comboxDivHide('skuLoadMessage')",300); 
			}
		}
	});
}

//清空sku列表、sku图片
function clearSkuInfo(){
	//防止清空销售价标题内部的table
	var skuSellAreaPriceTdHtml = $("#skuSellAreaPriceTd").html();
	//清空除标题以外的内容 
	$("#skuTable tr:not(:first)").remove();
	$("#skuSellAreaPriceTd").html(skuSellAreaPriceTdHtml);
	//清空标题行中的属性列
	for(var h in titleMap){
		if($("#skuTable tr:eq(0) td:eq(0)").attr("id") != "skuSellAreaPriceTd"){
			$("#skuTable tr:eq(0) td:eq(0)").remove();
		}
	}
	//清空sku图片
	$("#skuPicDiv").empty();
}

//清空销售属性、缓存的销售属性、sku列表、sku图片
function clearSaleAttrAndSku(){
	clearSkuInfo();
	saleCategoryAttr = "";
	//清空销售属性
	$("#shopCategoryDiv").empty();
	//隐藏sku表格
	$("#skuTable").hide();
	//清空之前缓存的销售属性数据
	titleMap = {};
	valueMap = {};
	sellAttrOneMap = {};
}

//阶梯加确认
function parseAllContryAreaPrice(skuAreaTableId,showTableId,hideTableId){
	//校验阶梯价规则
	var validateMessage = valueValidate(skuAreaTableId,true,true,"2");
	if(validateMessage != ""){
		alert(validateMessage);
		return false;
	}
	//将全国的阶梯价展示到页面上
	var showAllContryAreaPrice = "[";
	for(var allContryTrNum = 0;allContryTrNum<areaNum+1;allContryTrNum++){
		for(var j = 0;j<cityAreaPriceNum+1;j++){
			var sellMinNumObj = $("#"+skuAreaTableId+" tr[areType='allContry'] #"+allContryTrNum+"_"+j+"_minNum");
			if(sellMinNumObj.length > 0){
				var minNum = sellMinNumObj.val();
				var maxNum = $("#"+skuAreaTableId+" tr[areType='allContry'] #"+allContryTrNum+"_"+j+"_"+"maxNum").val();
				var sellPrice = $("#"+skuAreaTableId+" tr[areType='allContry'] #"+allContryTrNum+"_"+j+"_"+"sellPrice").val();
				var areaIdVal = "";
				var areaNameVal = "";
				//组装阶梯价格json
				if(areaPriceJson != "["){
					areaPriceJson += ",";
				}
				var areaPriceJsonOne = "{\"minNum\":\""+minNum+"\",\"maxNum\":\""+maxNum+"\",\"sellPrice\":\""+sellPrice+"\",\"areaId\":\""+areaIdVal+"\",\"areaName\":\""+areaNameVal+"\"}";
				if(showAllContryAreaPrice != "["){
					showAllContryAreaPrice += ",";
				}
				showAllContryAreaPrice += areaPriceJsonOne;
			}
		}
	}
	areaPrice(showAllContryAreaPrice+"]",showTableId);
	//关闭区域层
	$("#"+hideTableId).hide();
}

//动态生成sku标题
function skuTitleContentHtml(titleObj,id){
	var html = "";
	for(var titleEve in titleObj){
		html += "<td class=\"wid_110 hei_40 font_cen\" id=\"skuTitle_"+titleEve+"\">"+titleObj[titleEve]+"</td>";
	}
	//html += "</tr>";
	$("#"+id).before(html);
}

//动态生成sku内容
function skuContentHtml(sellPriceDivNum,areaNum,cityAreaPriceNum,valueObj,id,skuValue,skuArrayVal,skuin){
	var namePrefix = "skuInfos["+sellPriceDivNum+"].sellPrices["+cityAreaPriceNum+"].";
	var idPrefix = areaNum + "_" + cityAreaPriceNum + "_";
	var skuIdPrefix = "skuInfos["+sellPriceDivNum+"].";
	var isAllContry = true;
	//解析阶梯价并组装阶梯价
	var picObj = "";
	var sellPrices = "";
	var skuInventory = "";
	var costPrice = "";
	var skuId = "";
	
	//商品编号
	var skuProductId="";
	
	if(skuArrayVal != null && skuArrayVal != ""){
		isAllContry = skuArrayVal["isAllContry"];
		//获取阶梯价
		if(!skuArrayVal["skuInfos"].length){
			var skuInfo = skuArrayVal["skuInfos"];
			skuId = skuInfo.skuId;
			picObj = skuInfo.skuPics;
			sellPrices = skuInfo.sellPrices;
			if(skuInfo.skuInventory != null){
				skuInventory = skuInfo.skuInventory;
			}
			if(skuInfo.sellPrices[0].costPrice != null){
				costPrice = skuInfo.sellPrices[0].costPrice;
			}
			
			//商品编号
			if(skuInfo.skuProductId != null){
				skuProductId = skuInfo.skuProductId;
				
			}
			
		}else{
			$.each(skuArrayVal["skuInfos"],function(n,skuInfo){
				if(skuInfo.attributes == skuValue){
					
					skuId = skuInfo.skuId;
					picObj = skuInfo.skuPics;
					sellPrices = skuInfo.sellPrices;
					if(skuInfo.skuInventory != null){
						skuInventory = skuInfo.skuInventory;
						
					}
					if(skuInfo.sellPrices[0] != null && skuInfo.sellPrices[0].costPrice != null){
						costPrice = skuInfo.sellPrices[0].costPrice;
					}
					
					//商品编号
					if(skuInfo.skuProductId != null){
						skuProductId = skuInfo.skuProductId;
						
					}
					
					return;
				}
			});
		}
	}
	
	var html = "<tr id=\"skuOneTr_"+sellPriceDivNum+"\" class=\"hei_30\">";
	for(var h = 0;h < valueObj.length;h++){
		var obj = valueObj[h];
		html += "<td class=\"wid_110 hei_40 font_cen\" name=\"skuValue_"+obj.id+"\" id=\"skuValue_"+obj.id+"\">"+obj.name+"</td>";
	}
	html += "<td>";
	//sku隐藏
	html += "<input type=\"hidden\" name=\""+skuIdPrefix+"attributes\" value=\""+skuValue+"\" />";
	html += "<input type=\"hidden\" name=\""+skuIdPrefix+"skuId\" value=\""+skuId+"\" />";
	html += "<input type=\"hidden\" name=\""+skuIdPrefix+"subSkuIds\" value=\""+skuin+"\" />";
	html += "<table class=\"wid_130 mar_l10 fl\" id=\"skuAreaPriceQGTable_"+sellPriceDivNum+"\">";
	html += "<tr class=\"bg_06\"><td class=\"wid_60 font_cen hei_24\">订购量</td>";
    html += "<td class=\"wid_60 font_cen hei_24\">销售价(元)</td></tr></table>";
    html += "<p class=\" wid_100 fl wid_130 hei_35 mar_l10 font_1c cursor bianji1\" onclick=\"comboxDivShow('showPriDiv_"+sellPriceDivNum+"')\">";
    if(isView){
    	html += "查看地域价与阶梯价";
    }else{
    	html += "编辑地域价与阶梯价";
    }
    html += "</p>";
    //弹出层
    html += "<div class=\"shop_xinxi mar_t20 po_re\">";
	html += "<div class=\"diloge_dijia1\" style=\"display:none; z-index:2;\" id=\"showPriDiv_"+sellPriceDivNum+"\">"	;
	html += "<table class=\"areaTable border-4\" style=\"border-collapse:collapse;\" id=\"areaAllTable_"+sellPriceDivNum+"\">";
	html += "<tr class=\"hei_35 font_cen bg_05\">";
	html += "<th width=\"60\" height=\"35\" class=\"border-4\">编号</th>";
	html += "<th class=\"wid_220 border-4\">采购量</th>";
	html += "<th width=\"120\" class=\"border-4\"><font class=\"ftred\">*</font>销售价(元)</th>";
	html += "<th width=\"60\" class=\"border-4\">编辑</th>";
	html += "<th width=\"190\" class=\"border-4\">适用范围</th>";
	html += "</tr>";
	if(isAllContry){
		html += "<tr areType=\"allContry\" class=\"diloge_td\"><p></p>";
		html += "<td class=\"wid_60 font_cen\">1</td>";
		html += "<td colspan=\"3\" class=\"border-1\" width=\"400\" id=\"areaPriceTD_"+areaNum+"\">";
		html += "<ul class=\"cg-ul mar_t10\" id=\"areaPriceUl_"+cityAreaPriceNum+"\">";
		html += "<li class=\"wid_220 fl\">";
		html += "<input type=\"text\" value=\"1\" validateType=\"minNum\" maxlength=\"7\" onkeyup=\"numInput(this,0)\" id=\""+idPrefix+"minNum\" name=\""+namePrefix+"minNum\" class=\"wid_80 input_Style2 hei_30\">";
		html += " ~ ";
		html += "<input type=\"text\" placeholder=\"最大值\" maxlength=\"7\" onkeyup=\"numInput(this,0)\" title=\"采购量必须以9999999结束\" validateType=\"maxNum\" id=\""+idPrefix+"maxNum\" name=\""+namePrefix+"maxNum\" class=\"wid_80 input_Style2 hei_30\">";
		html += "</li>";
		html += "<li class=\" fl\">";
		html += "<input type=\"text\" class=\"wid_80 input_Style2 hei_30\" onkeyup=\"numInput(this)\" maxlength=\"12\" validateType=\"sellPrice\" id=\""+idPrefix+"sellPrice\" name=\""+namePrefix+"sellPrice\" >";
		html += "<input type=\"hidden\" id=\""+idPrefix+"areaId\" validateType=\"area\" name=\""+namePrefix+"areaId\" value=\"0\" />";
		html += "<input type=\"hidden\" id=\""+idPrefix+"areaName\" name=\""+namePrefix+"areaName\" value=\"全国\" />";
		 html += "<input type=\"hidden\" proper=\"costPrice\" id=\""+idPrefix+"costPrice\" name=\""+namePrefix+"costPrice\" value=\"\" />";
		html += "</li>";
		html += "<li class=\" fr mar_20 mar_t5 \" style=\"width:35px;\">";
		html += "<i class=\"demo-icons fa-trash font_16 cursor\" onclick=\"removeElement('areaPriceUl_"+cityAreaPriceNum+"')\">";
		html += "<a href=\"javascript:;\"  class=\"font_fe\"></a>";
		html += "</i>";
		html += "</li>";
		html += "</ul>";
		html += "<div class=\"clear\" id=\"areaPriceAfterDiv_"+areaNum+"\"></div>";
		html += "<div class=\"hei_35\">";
		html += "<i class=\"demo-icons fa-plus font_14b font_fa mar_l14 cursor\" onclick=\"addAreaPrice('skuInfos["+sellPriceDivNum+"].','areaPriceAfterDiv_"+areaNum+"','areaPriceTD_"+areaNum+"',"+areaNum+",'costPrice_"+sellPriceDivNum+"')\"></i>";
		html += "</div>";
		html += "</td>";
		html += "<th width=\"190 font_cen \" id=\"china_all\">全国 ";
		html += "<div id=\"city_ignore\"></div>";
		html += "<input type=\"hidden\" id=\"areaId_"+areaNum+"\" name=\"\" value=\"0\" />";
		html += "<input type=\"hidden\" id=\"areaName_"+areaNum+"\" name=\"\" value=\"全国\" />";
		html += "</th></tr>";
	}
	html += "</table>";
	html += "<div class=\"hei_35 font_cen font_18 font_1c cursor\">";
	html += "<a viewHide=\"true\" onclick=\"addArea('skuInfos["+sellPriceDivNum+"].','areaAllTable_"+sellPriceDivNum+"','costPrice_"+sellPriceDivNum+"')\" style='font-size:14px'>点击此处维护区域价</a></div>";
	html += "<div class=\"hei_60 bg_08 \">";
	html += "<span viewHide=\"true\" class=\"pad_l10\" style=\"margin-right:250px;\">点击\"+\"增加阶梯价</span>";
	html += "<button viewHide=\"true\" type=\"button\" class=\"font_14 button_2 hei_32  but_qx1\" id=\"skuBtn_"+sellPriceDivNum+"\" onclick=\"parseAllContryAreaPrice('areaAllTable_"+sellPriceDivNum+"','skuAreaPriceQGTable_"+sellPriceDivNum+"','showPriDiv_"+sellPriceDivNum+"')\">确定</button>";
	html += "&nbsp;&nbsp;<button type=\"button\" viewStyle=\"cancle\" class=\"font_14 button_2 hei_32 but_qx1\" onclick=\"comboxDivHide('showPriDiv_"+sellPriceDivNum+"')\">取消</button>";
	html += "</div>";
	html += "</div>";
	html += "</td>";
	html += "<td><input type=\"text\" onkeyup=\"numInput(this)\" maxlength=\"12\" value=\""+costPrice+"\" onblur=\"setCostPrice('costPrice_"+sellPriceDivNum+"','skuOneTr_"+sellPriceDivNum+"')\"  name=\"costPrice_"+sellPriceDivNum+"\" id=\"costPrice_"+sellPriceDivNum+"\" class=\"wid_120 hei_30 border-1 mar_l10\" /></td>";  
	html += "<td><input type=\"text\" onkeyup=\"numInput(this,0)\" onblur=\"calculationInventory()\" maxlength=\"7\" name=\""+skuIdPrefix+"skuInventory\" id=\""+sellPriceDivNum+"_skuInventory\" value=\""+skuInventory+"\" class=\"wid_120 hei_30 border-1 mar_l10\" /> </td>";
	
	//商品编号
//	html += "<td><input type=\"text\" onkeyup=\"numInput(this,0)\" onblur=\"calculationInventory()\" maxlength=\"7\" name=\""+skuProductId+"skuInventory\" id=\""+productNum+"_skuInventory\" value=\""+skuInventory+"\" class=\"wid_120 hei_30 border-1 mar_l10\" /> </td>";
	
	if($("#directsale").val()==1){
	html += "<td><input type=\"text\" stype=\"width:140px;\" maxlength=\"20\" onblur=\"checkSkuProductId(this)\" id=\""+sellPriceDivNum+"_skuProductId\" name=\""+skuIdPrefix+"skuProductId\" value=\""+skuProductId+"\" class=\"hei_30 border-1 mar_l10\" /> </td>";
	}
	if(!isView){
		html += "<td><i style=\"margin-left: 55px;\"  onclick=\"deleteSkuTr('"+skuValue+"')\" class=\"demo-icons fa-trash font_16 cursor\"></td>";
	}
	html += "</tr>";
	$("#"+id).append(html);
	//加载阶梯价
	initItemOrSkuSellPrices("skuInfos["+sellPriceDivNum+"].","areaAllTable_"+sellPriceDivNum,"areaPriceAfterDiv_","skuBtn_"+sellPriceDivNum,sellPrices,"skuAreaPriceQGTable_"+sellPriceDivNum);
	//追加sku图片
	skuPicContentHtml(valueObj,"skuPicDiv",skuIdPrefix,sellPriceDivNum,picObj);
	//展示页面
	if(isView){
		$("span[viewHide='true']").html("").attr("style","margin-right:400px");
		$("button[viewHide='true']").hide();
		$("button[viewStyle='cancle']").removeClass("button_3").addClass("button_2");
		$("a[viewHide='true']").hide();
		$("input[type='text']").attr("readonly","readonly");
	}
}

//生成sku图片
function skuPicContentHtml(valueObj,picDivId,skuIdPrefix,picNum,picObj){
	var html="<div class=\"border-5 pad_tb10\">"
    html += "<ul class=\"mar_tb10 list_top\">";
	for(var h = 0;h < valueObj.length;h++){
		var obj = valueObj[h];
		html += " <li name=\"img_attr_"+obj.id+"\" class=\"wid_80 font_cen fl mar_r10\">"+obj.name+"</li>";
	}
	html +="</ul>";
	html +="<ul  class=\"mar_tb10 list_top\">";
	html += "<li class=\"po_re po_re_over border-1\">";
	html += "<img type=\"skuImg\" src=\"\"  alt=\"\" id=\""+picNum+"_skuPicUrl_0\" index=\"0\"  width=\"80\" height=\"80\" >";
	html += "<div class=\"c-upload none\">";
	html += "<span id=\""+picNum+"_skuPS_0\" >图片上传</span>";
	html += "<input id=\""+picNum+"_fileupload_0\" type=\"file\" class=\"file-img cursor\" name=\"file\" onchange=\"fileChange('"+picNum+"_fileupload_0','"+picNum+"_skuPicUrl_0','"+picNum+"_skuPicInUrl_0','"+picNum+"skuPicUrl_1')\">";
	html += "</div>";
	html += "</li>";
	html += "<li class=\"font_cen po_re mar_20 po_re_over border-1\">";
	html += "<img type=\"skuImg\" src=\"\"  alt=\"\" id=\""+picNum+"_skuPicUrl_1\" index=\"1\" width=\"80\" height=\"80\" >";
	html += "<div class=\"c-upload none\">";
	html += "<span id=\""+picNum+"_skuPS_1\">图片上传</span>";
	html += "<input id=\""+picNum+"_fileupload_1\" type=\"file\" class=\"file-img cursor\" name=\"file\" onchange=\"fileChange('"+picNum+"_fileupload_1','"+picNum+"_skuPicUrl_1','"+picNum+"_skuPicInUrl_1','"+picNum+"skuPicUrl_2')\">";
	html += "</div>";
	html += "</li>";
	html += "<li class=\"font_cen po_re mar_20 po_re_over border-1\">";
	html += "<img type=\"skuImg\" src=\"\"  alt=\"\" id=\""+picNum+"_skuPicUrl_2\" index=\"2\" width=\"80\" height=\"80\" >";
	html += "<div class=\"c-upload none\">";
	html += "<span id=\""+picNum+"_skuPS_2\">图片上传</span>";
	html += "<input id=\""+picNum+"_fileupload_2\" type=\"file\" class=\"file-img cursor\" name=\"file\" onchange=\"fileChange('"+picNum+"_fileupload_2','"+picNum+"_skuPicUrl_2','"+picNum+"_skuPicInUrl_2','"+picNum+"skuPicUrl_3')\">";
	html += "</div>";
	html += "</li>";
	html += "<li class=\"font_cen po_re mar_20 po_re_over border-1\">";
	html += "<img type=\"skuImg\" src=\"\"  alt=\"\" id=\""+picNum+"_skuPicUrl_3\" index=\"3\" width=\"80\" height=\"80\" >";
	html += "<div class=\"c-upload none\">";
	html += "<span id=\""+picNum+"_skuPS_3\">图片上传</span>";
	html += "<input id=\""+picNum+"_fileupload_3\" type=\"file\" class=\"file-img cursor\" name=\"file\" onchange=\"fileChange('"+picNum+"_fileupload_3','"+picNum+"_skuPicUrl_3','"+picNum+"_skuPicInUrl_3','"+picNum+"skuPicUrl_4')\">";
	html += "</div>";
	html += "</li>";
	html += "<li class=\"font_cen po_re mar_20 po_re_over border-1\">";
	html += "<img type=\"skuImg\" src=\"\"  alt=\"\" id=\""+picNum+"_skuPicUrl_4\" index=\"4\" width=\"80\" height=\"80\" >";
	html += "<div class=\"c-upload none\">";
	html += "<span id=\""+picNum+"_skuPS_4\">图片上传</span>";
	html += "<input id=\""+picNum+"_fileupload_4\" type=\"file\" class=\"file-img cursor\" name=\"file\" onchange=\"fileChange('"+picNum+"_fileupload_4','"+picNum+"_skuPicUrl_4','"+picNum+"_skuPicInUrl_4','')\">";
	html += "</div>";
	html += "</li>";
	html += "<input type=\"hidden\" name=\""+skuIdPrefix+"skuPics[0].picUrl\" id=\""+picNum+"_skuPicInUrl_0\" />";
	html += "<input type=\"hidden\" name=\""+skuIdPrefix+"skuPics[1].picUrl\" id=\""+picNum+"_skuPicInUrl_1\" />";
	html += "<input type=\"hidden\" name=\""+skuIdPrefix+"skuPics[2].picUrl\" id=\""+picNum+"_skuPicInUrl_2\" />";
	html += "<input type=\"hidden\" name=\""+skuIdPrefix+"skuPics[3].picUrl\" id=\""+picNum+"_skuPicInUrl_3\" />";
	html += "<input type=\"hidden\" name=\""+skuIdPrefix+"skuPics[4].picUrl\" id=\""+picNum+"_skuPicInUrl_4\" />";
	html += "<li class=\"wid_110 font_1c font_cen \" style=\"display:none\">使用商品图</li>";
	html += "</ul>";
	html += "<div class=\"clear\"></div>";
	html +="<div>";
	$("#"+picDivId).append(html);
	//追加图片路径
	if(picObj != null && picObj != ""){
		//获取阶梯价
		var index = 0;
		$.each(picObj,function(n,pic){
			$("#"+picNum+"_skuPicUrl_"+index).attr("src",imageServerAddr+pic.picUrl);
			$("#"+picNum+"_skuPicInUrl_"+index).val(pic.picUrl);
			$("#"+picNum+"_skuPS_"+index).attr("href",imageServerAddr+pic.picUrl);
			index++;
		});
	}
	//设定上传/查看那图片
	 $(".po_re_over").mouseover(function(){
		$(this).find(".c-upload").show();
	 });
	 $(".po_re_over").mouseout(function(){
		$(this).find(".c-upload").hide();
	 });
	 if(isView){
		 //隐藏没有的图片框
		 var imgP = $(".sku_img_over img[type='skuImg']");
		 imgP.each(function(){
			 if($(this).attr("src") == ""){
				$(this).parent().hide();
			 }
		 }); 
		 $(".c-upload span").addClass("findSkuImg cursor").html("图片查看");
		 //$(".sku_img_over p[type='skuImgP']").addClass("findSkuImg").html("图片查看");
		 $(".sku_img_over input[type='file']").remove();
		 initPicView("findSkuImg");
	 }
}

//应用到所有sku
function skuUserAll(type){
	if("cost" == type){
		 var costPrice = $("#costPrice").val();
		 for(var i = 0;i<sellPriceDivNum+1;i++){
			 var costObj = $("#costPrice_"+i);
			 if(costObj.length > 0){
				 costObj.val(costPrice);
				 setCostPrice("costPrice","skuOneTr_"+i);
			 }
		 }
	}else if("skuInventory" == type){
		var costPrice = $("#skuAllInventory").val();
		//计算总sku库存
		 var inventory_all = 0;
		 for(var i = 0;i<sellPriceDivNum+1;i++){
			 var costObj = $("#"+i+"_skuInventory");
			 if(costObj.length > 0){
				 costObj.val(costPrice);
				 inventory_all += parseFloat(costPrice == "" ? "0" : costPrice);
			 }
		 }
		 $("#inventory").val(inventory_all);
	}else if("skuSellPrice" == type){
		//加载提示层
		$("#sellPriceLoadMessage").show();
		//获取每一个sku阶梯价div
		var allContryPriceHtml = $("#areaPriceTable").html();
		if(!$.isEmptyObject(areaPriceObj)){
			for(var i = 0;i<=sellPriceDivNum;i++){
				var tableId = "areaAllTable_"+i;
				if($("#"+tableId).length > 0 ){
					initSkuAreaPrice(tableId,i,areaPriceObj,allContryPriceHtml);
				}
			}
		}
		//隐藏弹出层
		setTimeout("comboxDivHide('sellPriceLoadMessage')",300); 
	}
}

var html = "";
//加载sku对应的阶梯价
function initSkuAreaPrice(tableId,tableNum,valueArray,allContryPriceHtml){
	if($("#"+tableId).length > 0 ){
		//清空除标题外的内容
		$("#"+tableId).find("tr:not(:first)").empty();
		for(var key in valueArray){
			areaNum ++;
			//添加地区行
			html = "";
			if(key == 0){
				//添加全国行
				var addToArray = {};
				addToArray["isAddAreaPriceContent"] = false;
				addToArray["isAllContry"] = true;
				addToArray["areaId"] = "";
				addToArray["areaName"] = "";
				html = areaContentHtml("skuInfos["+tableNum+"].","","",areaNum,"","",parseInt(key)+1,"costPrice_"+tableNum,addToArray);
			}else{
				//添加地区行
				var addToArray = {};
				addToArray["isAddAreaPriceContent"] = false;
				addToArray["isAllContry"] = false;
				addToArray["areaId"] = "";
				addToArray["areaName"] = "";
				html = areaContentHtml("skuInfos["+tableNum+"].","","",areaNum,"","",parseInt(key)+1,"costPrice_"+tableNum,addToArray);
			}
			$("#"+tableId).append(html);
			//添加阶梯价格
			var areaPriceHtml = "";
			var valueT = valueArray[key];
			var areaId = "";
			var areaName = "";
			if(valueT != ""){
				var valueObj = $.parseJSON(valueT);
				//遍历行数据
				$.each(valueObj, function (n, areaPrice) {
					cityAreaPriceNum++;
					var namePrefix = "skuInfos["+tableNum+"].sellPrices["+cityAreaPriceNum+"].";
					var idPrefix = areaNum + "_" + cityAreaPriceNum + "_";
					areaPriceHtml += areaPriceContentHtml(namePrefix,idPrefix,cityAreaPriceNum,"","costPrice_"+tableNum,areaPrice);
					areaId = areaPrice.areaId;
					areaName = areaPrice.areaName;
				});
			}
			$("#areaPriceAfterDiv_"+areaNum).before(areaPriceHtml);
			//追加地区数据
			if(key != 0){
				$("#areaId_"+areaNum).val(areaId);
				$("#areaName_"+areaNum).val(areaName);
				$("#showArea_"+areaNum).text(areaName);
			}else{
				$("#areaId_"+areaNum).val(0);
				$("#areaName_"+areaNum).val("全国");
				$("#showArea_"+areaNum).text("全国");
				qgIndex = areaNum; //记录全国阶梯价所在行的索引
				//展示全国销售价格
				//$("#skuBtn_"+tableNum).click();
				//parseAllContryAreaPrice(tableId,"skuAreaPriceQGTable_"+tableNum,"showPriDiv_"+tableNum);
				$("#skuAreaPriceQGTable_"+tableNum).empty();
				$("#skuAreaPriceQGTable_"+tableNum).html(allContryPriceHtml);
			}
		}
	}
}

//组装全国阶梯价展示表格 {minNum:1,maxNum:2,sellPrice:12,areaId:1,areaName,2}
function areaPrice(areaPrices,tableId){
	var priceTable = $("#"+tableId);
	//删除table除标题以外的行
	priceTable.find("tr:not(:first)").remove();
	//追加行
	var areaPriceObj = $.parseJSON(areaPrices);
	$.each(areaPriceObj, function (n, areaPrice) {
		var tr = "<tr>";
		tr += "<td class=\"wid_60 font_cen hei_24\">"+areaPrice.minNum + " - " + areaPrice.maxNum +"</td>";
		tr += "<td class=\"wid_60 font_cen hei_24\">"+areaPrice.sellPrice +"</td>";
		tr += "</tr>";
		priceTable.append(tr);
	});
}
//添加销售数量阶梯价
function addAreaPrice(namePre,id,tdId,rowNumV,costPriceId){
	//校验采购量
	var validateMessage = valueValidate(tdId,true,false,"1");
	if(validateMessage != ""){
		alert(validateMessage);
		return false;
	}
	//获取上条数据最小销售数量
	var minNum = $("#"+tdId+" ul:last input:eq(0)").val();
	//获取上条数据最大销售数量
	var maxNum = $("#"+tdId+" ul:last input:eq(1)").val();
	if(maxNum == null){
		maxNum = 0;
	}
	
	//校验最大值是否达到上线
	if(parseInt(maxNum) >= maxValueValidate){
		alert("最大采购量已经达到上限值,不允许继续添加");
		return false;
	}
	/* if(maxNum == "最大值"){
		alert("上一条数据的采购量已经覆盖了全值【1-最大值】，无需再增");
		return false;
	} */
	cityAreaPriceNum ++ ;
	var namePrefix = namePre + "sellPrices["+cityAreaPriceNum+"].";
	//var idPrefix = "sellPrices["+cityAreaPriceNum+"].";
	var idPrefix = rowNumV + "_" + cityAreaPriceNum + "_";
	var html = areaPriceContentHtml(namePrefix,idPrefix,cityAreaPriceNum,parseInt(maxNum)+1,costPriceId,null);
	$("#"+id).before(html);
	//保存所在的地域
	var areaIdVal = $("#areaId_"+rowNumV).val();//获取已经选择的地域id
	var areaNameVal = $("#areaName_"+rowNumV).val();//获取已经选择的地域名称
	$("#"+idPrefix+"areaId").val(areaIdVal);
	$("#"+idPrefix+"areaName").val(areaNameVal);
	
}
//添加地区行
function addArea(namePrefix,tableId,costPriceId){
	areaNum ++;
	cityAreaPriceNum++;
	var areaNumSelf = areaNum;
	var namePre = namePrefix;
	namePrefix += "sellPrices["+cityAreaPriceNum+"].";
	//var idPrefix = "sellPrices["+cityAreaPriceNum+"].";
	var idPrefix = areaNumSelf + "_" + cityAreaPriceNum + "_";
    var html = areaContentHtml(namePre,namePrefix,idPrefix,areaNumSelf,cityAreaPriceNum,1
    		,parseInt($("#"+tableId+" tr:last td:first").html())+1,costPriceId,null);
    $("#"+tableId).append(html);
}

//组装销售数量-价格内容
function areaPriceContentHtml(namePrefix,idPrefix,areaPriceNum,defaultMinNum,costPriceId,valueArray){
	var minNum = defaultMinNum;
	var maxNum = "";
	var sellPrice = "";
	var areaId = "";
	var areaName = "";
	//获取阶梯成本价
	var costPriceValue = "";
	if(valueArray != null){
		minNum = valueArray["minNum"];
		maxNum = valueArray["maxNum"];
		sellPrice = valueArray["sellPrice"];
		areaId = valueArray["areaId"];
		areaName = valueArray["areaName"];
		if(valueArray["costPrice"] != null){
			costPriceValue = valueArray["costPrice"];
		}
	}
	
	if(costPriceValue == "" && $("#"+costPriceId).length > 0){
		costPriceValue = $("#"+costPriceId).val();
	}
	var html = "<ul class=\"cg-ul mar_t10\" id=\"areaPriceUl_"+areaPriceNum+"\"><li class=\"wid_220 fl\">";
	//拼接最小数量 默认为上条数据最大数量+1
	html += "<input type=\"text\" validateType='minNum' maxlength=\"7\" onkeyup=\"numInput(this,0)\" value=\""+minNum+"\" id=\""+idPrefix+"minNum\" name=\""+namePrefix+"minNum\" placeholder=\"\"  class=\"wid_80 input_Style2 hei_30\" />";
	html += " ~ ";
	//拼接最大数量，默认为最大值
	html += "<input type=\"text\" validateType='maxNum' maxlength=\"7\" onkeyup=\"numInput(this,0)\" value=\""+maxNum+"\" title=\"采购量必须以9999999结束\" placeholder=\"最大值\" id=\""+idPrefix+"maxNum\" name=\""+namePrefix+"maxNum\" class=\"wid_80 input_Style2 hei_30\" />";
	html += "</li><li class=\"fl\">";
	//拼接销售价
    html += "<input type=\"text\" onkeyup=\"numInput(this)\" maxlength=\"12\" class=\"wid_80 input_Style2 hei_30\" validateType='sellPrice' value=\""+sellPrice+"\" id=\""+idPrefix+"sellPrice\" name=\""+namePrefix+"sellPrice\" />";    
    html += "<input type=\"hidden\" id=\""+idPrefix+"areaId\" validateType=\"area\" name=\""+namePrefix+"areaId\" value=\""+areaId+"\" />";
    html += "<input type=\"hidden\" id=\""+idPrefix+"areaName\" name=\""+namePrefix+"areaName\" value=\""+areaName+"\" />";
    html += "<input type=\"hidden\" proper=\"costPrice\" id=\""+idPrefix+"costPrice\" name=\""+namePrefix+"costPrice\" value=\""+costPriceValue+"\" />";
    html += "</li>";
    if(!isView){
    	html += "<li class=\" fr mar_20 mar_t5 \" style=\"width:35px;\"><i onclick=\"removeElement('areaPriceUl_"+areaPriceNum+"')\" class=\"demo-icons fa-trash font_16 cursor\"></i>";
    }
    html += "</li></ul>"; 
    return html;
}

//组装地区销售价格
function areaContentHtml(namePre,namePrefix,idPrefix,rowNum,areaPriceNum,defaultMinNum,numIndex,costPriceId,addToArray){
	var isAllContry = false;
	var isAddAreaPriceContent = true;
	var areaId = "";
	var areaName = "";
	var showAreaName = "选择地区";
	var areaType = "";
	if(addToArray != null){
		//解析追加的参数，主要用于编辑时使用
		isAllContry = addToArray["isAllContry"];
		if(isAllContry){
			areaType = "allContry";
		}
		isAddAreaPriceContent = addToArray["isAddAreaPriceContent"];
		
		if(addToArray["areaId"] != null){
			areaId = addToArray["areaId"];
		}
		if(addToArray["areaName"] != null){
			areaName = addToArray["areaName"];
			showAreaName = areaName;
		}
	}
	//var areaSubfixArray = areaSubfix.split("_");
	//var rowNum = areaSubfixArray[areaSubfixArray.length - 1];
	var html = "<tr areType=\""+areaType+"\" class=\"diloge_td border-9\" id=\"areaPriceTR_"+rowNum+"\"><p></p><td class=\"wid_60 font_cen\">"+numIndex+"<br/>";
	if(!isView){
		html += "<a class=\"cursor as\" onclick=\"removeElement('areaPriceTR_"+rowNum+"')\">删除</a>";
	}
	html == "</td>";
	html += "<td colspan=\"3\" class=\"border-1\" width=\"400\" id=\"areaPriceTD_"+rowNum+"\">";
	if(isAddAreaPriceContent){
		html += areaPriceContentHtml(namePrefix,idPrefix,areaPriceNum,1,costPriceId,null);
	}
	html += "<div class=\"clear\" id=\"areaPriceAfterDiv_"+rowNum+"\"></div>";
    html += "<div class=\"hei_35\">";
    if(!isView){
    	html += "<i onclick=\"addAreaPrice('"+namePre+"','areaPriceAfterDiv_"+rowNum+"','areaPriceTD_"+rowNum+"',"+rowNum+",'"+costPriceId+"')\" class=\"demo-icons fa-plus font_14b font_fa mar_l14 cursor\">";
    	html += "</i>";
    }
    html += "</div></td><th width=\"190 font_cen \"  id=\"area\"> ";
    if(isAllContry == true){
    	html += "全国";
    	html += "<div id=\"city_ignore\"></div>";
    }else{
    	html += "<div style=\"margin: 0px auto; width: 100%; overflow: hidden; max-height: 68px;\"><a class='cursor as' id=\"showArea_"+rowNum+"\"";
    	if(!isView){
    		html += "onclick=\"selectArea('areaId_"+rowNum+"','areaName_"+rowNum+"','showArea_"+rowNum+"',"+rowNum+")\"";
    	}
    	html += ">"+showAreaName+"</a></div>";
    }
    html += "<div id=\"city_ignore\"></div>";
   	html += "<input type=\"hidden\" id=\"areaId_"+rowNum+"\" value=\""+areaId+"\" /><input type=\"hidden\" id=\"areaName_"+rowNum+"\" value=\""+areaName+"\" />";
    html += "</th></tr>";
    return html;
}

//检查商品编号是否已经存在
function checkSkuProductId(obj){
	$.ajax({
		url:path + "/sellcenter/sellProduct/checkProductId",
		type:"post",
		data:{
			productId:obj.value,
		},
		dataType: "json",
		success:function(data){
			if(data.result){
				//submit();
			}else{
				if(data.message=="商品编号不能为空"){
					alert("商品编号不能为空");
				}else if(confirm("商品编号已存在，请重新输入！")){
				}
				//$("#"+btnId).hideMsg();
			}
		}
	});
}



//设置成本价
function setCostPrice(id,trId){
	var value = $("#"+id).val();
	$("#"+trId+" input[proper='costPrice']").val(value);
}

//选择地区
function selectArea(bufferAreaIdId,bufferAreaNameId,showAreaNameId,rowNumV){
	bufferAreaIdIdVal = bufferAreaIdId;
	bufferAreaNameIdVal = bufferAreaNameId;
	showAreaNameIdVal = showAreaNameId;
	rowNumVal = rowNumV;
	tableIdVal = "";
	$(".all_chain").show();
	
	//回显
	initProvinceCitys($("#"+bufferAreaIdIdVal).val());
}
//展示已经选择的地区
function showSelectArea(selectAreaIdVal,selectAreaNameVal){
	//存放已经选择的地区
	$("#"+bufferAreaIdIdVal).val(selectAreaIdVal);
	$("#"+bufferAreaNameIdVal).val(selectAreaNameVal);
	$("#"+showAreaNameIdVal).text(selectAreaNameVal);
	$("#"+showAreaNameIdVal).attr("title",selectAreaNameVal);
	//展示不包含的地区
	/* var allConIgnoreDiv = $("#"+tableIdVal+" #city_ignore");
	if(allConIgnoreDiv.html() == ""){
		allConIgnoreDiv.html("不包含地区:"+selectAreaNameVal);
	}else{
		allConIgnoreDiv.append(","+selectAreaNameVal);
	}
	 */
	//遍历每个隐藏的地区元素并赋值
	for(var i = 0;i<=cityAreaPriceNum;i++){
		var objId = $("#"+rowNumVal+"_"+i+"_areaId");
		var objName = $("#"+rowNumVal+"_"+i+"_areaName");
		if(objId.length>0){
			objId.val(selectAreaIdVal);
			objName.val(selectAreaNameVal);
		}
	}
	bufferAreaIdIdVal = "";
	bufferAreaNameIdVal = "";
	showAreaNameIdVal = "";
	rowNumVal = "";
	$(".all_chain").hide();
}
//计算总库存=sku库存相加之和
function calculationInventory(){
	 var inventory_all = 0;
	 for(var i = 0;i<sellPriceDivNum+1;i++){
		 var costObj = $("#"+i+"_skuInventory");
		 if(costObj.length > 0 && costObj.val() && costObj.val().replace(/\s/g,"") && !isNaN(costObj.val())){
			 inventory_all += parseFloat(costObj.val());
		 }
	 }
	 $("#inventory").val(inventory_all == 0 ? "":inventory_all);
}
//删除一行sku
function deleteSkuTr(skuValue){
	if(addSource==3){
		var array =skuValue;
		$('[name=sellAttrCheck]').each(function() {
			if($(this).attr("skuattributes")==array){
				var id=$(this).attr("id");
				$("#"+id).click();
			}
		})
	}else{
		var array = skuValue.split(";")[0].split(":")[1];
		$("#check_"+array).click();
	}
}

//图片上传
function fileChange(fileInputId,showImgId,urlInput,showNextImgId){
	startUpload(fileInputId,showImgId,urlInput,showNextImgId); 
}
//选择商品点击确定组合sku
function cofirmSku(skuArrayVal){
	$("#groupSkuId").empty();
	clearSkuInfo();
	$("#skuTable").hide();
	sellAttrOneMap={};
	sellSkuAttrMap = {}; 
	sellmap={};
	var html= "";
	var checkedItemIds = $("#gene_checkedItemIds").val();
	var checkedItemNames = $("#gene_checkedItemNames").val();
	var attrSaleStrList = $("#gene_attrSaleStrList").val();
	var skuInfoS = $("#gene_skuInfoList").val();
	var itemIds = checkedItemIds.split(",");     
	var itemNames = checkedItemNames.split(",");     
	var attrsaleList = attrSaleStrList.split("&");     
	var skuInfoList = skuInfoS.split("&");  
	for (var i=0;i<itemIds.length-1;i++)
	{
		var itemId =itemIds[i];
		var itemName=itemNames[i];
		if(skuArrayVal == ''){
			comboxDivShow('skuLoadMessage');
		}
		//组装选中的销售属性
		var attrsale = attrsaleList[i];
		var jsonobj=eval("("+attrsale+")"); 
		var skuInfo=skuInfoList[i];
		var skuInfo=eval("("+skuInfo+")"); 
			//操作sku提示
			/*if(doType != ""){
				var flag = false;
				if(confirm("修改sku属性可能影响商品促销活动的正常使用，请您谨慎操作！是否继续?")){
					flag = true;
				}
				if(!flag){
					if(obj.is(':checked')){
						$("#"+objId).removeAttr("checked");
					}else{
						$("#"+objId).attr("checked","true");
					}
					return;
				}
			}
			if(skuArrayVal == ''){
				comboxDivShow('skuLoadMessage');
			}
			//组装选中的销售属性
			var attrsale = $("#"+checkId).attr("attrSaleStr");
			var skuInfo=$("#"+checkId).attr("skuinfo");
			var jsonobj=eval('('+attrsale+')'); 
			var skuInfo=eval('('+skuInfo+')');
			 */
			//如果是选中状态
//			if(obj.is(':checked')){
				saveMap(jsonobj,skuInfo,itemId);
//			}
			html+="<div  itemId='"+itemId+"' itemName='"+itemName+"' class=\"hei_30 fl\" style=\"min-width:200px;\">"
			html+="<span class=\"over_attr_val cursor\" >"+itemName+"</span>"+"&nbsp;&nbsp;";
			html+="数量"+"<input type=\"text\" id='"+itemId+"' onkeyup=\"numInput(this)\" name=\"sub_num\" value='1' class=\"input_Style2 hei_24 wid_30 mar_l10\">";
			html+="</div>";
	}
	$("#selectItemId").html(html);
	saveCombinationSkuHtml('',skuArrayVal);
	$("#skuList").hide();
}
    //组合商品生成销售属性
	function saveCombinationSkuHtml(res,skuArrayVal){
		//解析选中的销售属性集合
		var skuStr = "[";
		var index = 0;
		for(var keyEvery in sellmap){
			var mapValue = sellmap[keyEvery];
			if(mapValue.length > 0){
				if(index > 0){
					skuStr += ",";
				}
				skuStr += "[";
				for(var i = 0;i<mapValue.length;i++){
					if(i > 0)
						skuStr += ",";
					skuStr += "\""+mapValue[i]+"\"";
				}
				skuStr += "]";
			}
			index ++ ;
		}
		skuStr += "]";
			//获取sku列表
			$.ajax({
				url: path + "/sellcenter/sellProduct/getGroupSkuList",
				type:"post",
				data:{
					skuStr:skuStr
				},
				dataType: "json",
				success:function(data){
					if(data.lists.length > 0){
						if(data.flag==true){
							alert("最多sku数量不允许超过"+data.maxNum+"个");
							var mapValue = sellAttrOneMap[sellAttrId];
							mapValue.remove(obj.attr("sellAttrValId"));
							comboxDivHide('skuLoadMessage');
							return;
						}else{
							$.each(data.lists, function (n, pf) {
								var attribute='';
								var valueObj = new Array();
								for(var i = 0;i<pf.length;i++){ 
									for(var keyEvery in sellSkuAttrMap){
										if(pf[i]==keyEvery){
											var value = sellSkuAttrMap[keyEvery];
											attribute+=value;
										}
									}
								}  
								var select = "";
								var attr=attribute.split(";");
								var skuAttributes = attribute.split(",").join(';');
								if(res!=''){
									$.each(res, function (n, pf) {
										if(pf.attributes==skuAttributes){
											select = "checked='checked'";
										}
									});
								}
								for(var i = 0;i<attr.length;i++){
									if(attr[i]==""){
										continue;
									}
									var valueArray = attr[i].split(":");
									var valueId = valueArray[1];
									var valueName = valueMap[valueId];
									var Circle={  
										 "id":valueId,  
										 "name":valueName
									}
									valueObj.push(Circle);
								}
								var html;
								var html = "<div class=\"mar_l10 hei_30 fl\" style=\"min-width:180px;\">";
								html += "<input class=\"mar_lr10\" id=\"check_"+n+"\" "+select+" skuin=\""+pf+"\" attrs=\""+attr+"\" onchange=\"clickGroup('')\"   name='sellAttrCheck' skuAttributes=\""+skuAttributes+"\"  type=\"checkbox\" />";
								for(var h = 0;h < valueObj.length;h++){
									var obj = valueObj[h];
									if(h!=valueObj.length-1){
										html += "<span attrId=\""+obj.id+"\" attrType=\"2\" class=\"over_attr_val cursor\" id='span_attrVal_"+obj.id+"' >"+obj.name+"</span>+";
									}else{
										html += "<span   attrId=\""+obj.id+"\" attrType=\"2\" class=\"over_attr_val cursor\" id='span_attrVal_"+obj.id+"' >"+obj.name+"</span>";
									}
								}
								html += "</div>";
								$("#groupSkuId").append(html);
						  });
						}
						if(itemId != ""){
							initSkuInfo();
						}
						if(isView){
							$("input[type='checkbox']").attr("disabled","disabled");
						}	
					}else{
						$("#skuTable").hide();
					}
					//隐藏加载内容
				 if(skuArrayVal == ''){
					setTimeout("comboxDivHide('skuLoadMessage')",300); 
				}
				}
			});
		}
	  //组合商品生成sku列表
	function clickGroup(skuArrayVal){
		if(skuArrayVal == ''){
			comboxDivShow('skuLoadMessage');
		}
		//清空sku信息
		clearSkuInfo();
		$("#skuTable").show();
		var titleObj = {};
		for(var j in sellAttrOneMap){
			titleObj[j] = titleMap[j];
		}
		//添加标题
		skuTitleContentHtml(titleObj,"skuSellAreaPriceTd");
		//确定删除的元素
		if((skuArrayVal == null || skuArrayVal == "")){
			for (var p in skuArePriceTemp){
				var isDelete = true;
				$('[name=sellAttrCheck]:checked').each(function() {
					var pf=$(this).attr("attrs");
					var skuAttributes = pf.split(",").join(';');
					if(p == skuAttributes){
						isDelete = false;
						return;
					}
				})
				  if(isDelete){
						delete skuArePriceTemp[p];
				}
			}
		}
		$('[name=sellAttrCheck]:checked').each(function() {
			var skuin=$(this).attr("skuin");
			var pf=$(this).attr("attrs");
			var pfArray = pf.split(",");
			var skuAttributes = pf.split(",").join(';');
			var valueObj = new Array();
			for(var i = 0;i<pfArray.length;i++){
				if(pfArray[i]==""){
					continue;
				}
				var valueArray = pfArray[i].split(":");
				var valueId = valueArray[1];
				var valueName = valueMap[valueId];
				var Circle={  
					 "id":valueId,  
					 "name":valueName
				}
				valueObj.push(Circle);
			} 
			//追加sku阶梯价
			sellPriceDivNum++;
			areaNum ++;
			cityAreaPriceNum++;
			var skuArrayVal_ = skuArrayVal;
			if((skuArrayVal == null || skuArrayVal == "") && skuArePriceTemp[skuAttributes] != null){
				skuArrayVal_ = skuArePriceTemp[skuAttributes];
			}
			skuContentHtml(sellPriceDivNum,areaNum,cityAreaPriceNum,valueObj,"skuTable",skuAttributes,skuArrayVal_,skuin);
		});
		if ($(":checkbox[name=sellAttrCheck]:checked").size() == 0) {
			$("#skuTable").hide();
		}
		//隐藏加载内容
		if(skuArrayVal == ''){
			setTimeout("comboxDivHide('skuLoadMessage')",300); 
		}
}
