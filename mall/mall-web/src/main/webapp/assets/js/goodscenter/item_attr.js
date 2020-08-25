/*
 * 商品销售属性    
 */
//获取销售属性
var baseid="";
var addid="";
var auxid="";
var checkedItemIds='';
var selectChoiceid="";
var materialId="";
var base_checkedItemNames="";
var base_attrSaleStrList="";
var base_skuInfoList="";
var add_checkedItemNames="";
var add_attrSaleStrList="";
var add_skuInfoList="";
var aux_checkedItemNames="";
var aux_attrSaleStrList="";
var aux_skuInfoList="";
var gene_checkedItemNames="";
var gene_attrSaleStrList="";
var gene_skuInfoList="";
var html="";
function getShopCategoryAttribute(optionId,cid,plantItemId){
	//商品添加地址
	var url = path + "/sellcenter/sellProduct/getShopSellAttribute";
	if(plstItemId && !itemId){
		//从平台上传商品
		url = path + "/sellcenter/sellProduct/getPlantformCategory";
	}else if(itemId){
		//商品编辑
		if(addSource==3){
			$("#groupSkuId").empty();
			clearSkuInfo();
			$("#skuTable").hide();
			sellAttrOneMap={};
			sellSkuAttrMap = {}; 
			sellmap={};
			var id='';
			var html= "";
			url = path + "/sellcenter/sellProduct/getSubPackage";
		}else{
			url = path + "/sellcenter/sellProduct/getItemSaleAttrs";
		}
	}
	$.ajax({
		url:url,
		type:"post",
		data:{
			cid:cid,
			plantItemId:plantItemId,
			itemId:itemId,
			attrType:1
		},
		dataType: "json",
		success:function(data){
			var optionItem = $("#"+optionId);
			optionItem.empty();
			//遍历属性
			$.each(data.result, function (n, pf) {
				if(!plstItemId || itemId){
					if(addSource==3){
						var itemId=pf.itemId;
						var itemName=pf.itemName;
						id+=itemId+",";
						var attrsale = pf.attrSaleStr;
						var skuInfoIn=pf.attributesStr;
						var jsonobj=eval('('+attrsale+')'); 
						var skuInfo=eval('('+skuInfoIn+')'); 
						saveMap(jsonobj,skuInfo,itemId);
						gene_checkedItemNames+=itemName+",";
						gene_attrSaleStrList+=attrsale+"&";
						gene_skuInfoList+=skuInfoIn+"&";
						$("#gene_checkedItemNames").val(gene_checkedItemNames);
						$("#gene_attrSaleStrList").val(gene_attrSaleStrList);
						$("#gene_skuInfoList").val(gene_skuInfoList);
						var count="";
						if(data.lists!=""){
							$.each(data.lists, function (n, pf) {
								if(pf.addSource==1){
									if(pf.subItemId==itemId){
										count=pf.subNum;
									}
								}
							});
						}
						html+="<div  itemId='"+itemId+"' itemName='"+itemName+"' class=\"hei_30 fl\" style=\"min-width:200px;\">"
						html+="<span class=\"over_attr_val cursor\" >"+itemName+"</span>"+"&nbsp;&nbsp;";
						html+="数量"+"<input type=\"text\" id='"+itemId+"' maxlength='4' onkeyup=\"numInput(this)\" name=\"sub_num\" value='"+count+"' class=\"input_Style2 hei_24 wid_30 mar_l10\">";
						html+="</div>";
					}else{
						saleAttrHtml(pf,optionItem);
					}
				}else{
					var boo = initItemId == "" ? true:false;
					//缓存销售属性--销售属性值
					var mapValue = null;
					if(boo){
						mapValue = new Array();
						if(sellAttrOneMap[pf.id] == null){
						}else{
						    mapValue = sellAttrOneMap[pf.id];
						}
						//向mapValue中添加销售属性值
						$.each(pf.values, function (m, categoryVal) {
							mapValue.push(categoryVal.id);
							valueMap[categoryVal.id] = categoryVal.name;
						});
						//讲销售属性值和销售属性名称放入全局变量中，只要应用于生成sku
						sellAttrOneMap[pf.id] = mapValue;
						titleMap[pf.id] = pf.name;
					}
					//加载销售属性模板
					saleAttrHtml(pf,optionItem);
				}
			});
			$("#gene_checkedItemIds").val(id);
			$("#selectItemId").html(html);
			if(addSource==3){
				$.each(data.itemDtos, function (t, f) {
					var itemName=f.itemName;
					var sellAttrOneMap={};
					var sellSkuAttrMap = {}; 
					var sellmap={};
					var itemId=f.itemId;
					var attrsale = f.attrSaleStr;
					var skuInfoIn=f.attributesStr;
					var jsonobj=eval('('+attrsale+')'); 
					var skuInfo=eval('('+skuInfoIn+')');
					if(f.addSource=='4'){
						baseid+=itemId+",";
						base_checkedItemNames+=itemName+",";
						base_attrSaleStrList+=attrsale+"&";
						base_skuInfoList+=skuInfoIn+"&";
						selectChoiceid="selectChoiceid";
						checkedItemIds="basis_checkedItemIds";
						materialId="baseAddService";
						$("#"+checkedItemIds).val(baseid);
						$("#basis_checkedItemNames").val(base_checkedItemNames);
						$("#basis_attrSaleStrList").val(base_attrSaleStrList);
						$("#basis_skuInfoList").val(base_skuInfoList);
					}else if(f.addSource=='5'){
						addid+=itemId+",";
						selectChoiceid="selectZeng";
						checkedItemIds="added_checkedItemIds";
						materialId="valueaddId";
						add_checkedItemNames+=itemName+",";
						add_attrSaleStrList+=attrsale+"&";
					    add_skuInfoList+=skuInfoIn+"&";
					    $("#added_checkedItemNames").val(add_checkedItemNames);
						$("#added_attrSaleStrList").val(add_attrSaleStrList);
						$("#added_skuInfoList").val(add_skuInfoList);
						$("#"+checkedItemIds).val(addid);
					}else if(f.addSource=='6'){
						auxid+=itemId+",";
						selectChoiceid="selectFu";
						checkedItemIds="aux_checkedItemIds";
						materialId="materialId";
						$("#"+checkedItemIds).val(auxid);
						aux_checkedItemNames+=itemName+",";
						aux_attrSaleStrList+=attrsale+"&";
					    aux_skuInfoList+=skuInfoIn+"&";
					    $("#aux_checkedItemNames").val(aux_checkedItemNames);
						$("#aux_attrSaleStrList").val(aux_attrSaleStrList);
						$("#aux_skuInfoList").val(aux_skuInfoList);
					}
					createServceHtml(sellAttrOneMap,sellSkuAttrMap,sellmap,itemName,itemId,jsonobj,skuInfo,f.addSource,materialId,checkedItemIds,data.lists);
					$("#skuList").hide();
				});
			}
			saleCategoryAttr = "";
			//加载sku列表 应用于编辑页面初始化sku列表
			if(plstItemId && initItemId == ""){
				//从平台上传
				getSkuList(null,'');
			}else if(initItemId != ""){
				if(addSource==3){
					var res=data.itme.skuInfos;
					saveCombinationSkuHtml(res,'');
				}else{
					//加载sku 编辑
					initSkuInfo();
				}
				initItemId = "";
			}
			//查看页面
			if(isView){
				$("input[type='checkbox']").attr("disabled","disabled");
				$("p[name='addBtn']").hide();
				$("span[viewHide='true']").html("");
				$("button[viewHide='true']").hide();
				$("a[viewHide='true']").hide();
			}else{
				//绑定鼠标滑过事件，展示销售属性和属性值的编辑和删除按钮
				bindOver("over_attr_val");
			}
		}
	});
}
function createServceHtml(sellAttrOneMap,sellSkuAttrMap,sellmap,itemName,itemId,jsonobj,skuInfo,type,baseAddService,selectBaseItemId,lists){
	for(var i=0;i<jsonobj.length;i++){
		var sellAttrId=jsonobj[i].id;
	    var sellAttrName =jsonobj[i].name;
	    for(var j=0;j<jsonobj[i].values.length;j++){
	    	var sellAttrValName = jsonobj[i].values[j].name;
	    	var sellAttrValId = jsonobj[i].values[j].id;
	    	if(sellAttrOneMap[sellAttrId] == null){
				var mapValue = new Array();
				mapValue[0] = sellAttrValId;
				sellAttrOneMap[sellAttrId] = mapValue;
			}else{
				var mapValue = sellAttrOneMap[sellAttrId];
				mapValue.push(sellAttrValId);
				sellAttrOneMap[sellAttrId] = mapValue;
			}
	    	//sellAttrOneMap  存放选中的销售属性，如果里面没有此销售属性key则新增，如果里面有则先删除再新增
			//存放sku标题
			titleMap[sellAttrId] = sellAttrName;
			//存放属性名称，在组装sku表格时显示使用
			valueMap[sellAttrValId] = sellAttrValName;
	    }
	}
	var num="";
	if(lists!=''){
		$.each(lists, function (n, pf) {
			if(pf.addSource==type){
				if(pf.subItemId==itemId){
					num=pf.subNum;
				}
			}
		});
	}else{
		num=1;
	}
    var html="<div id='span_attrVal_"+itemId+"' itemId='"+itemId+"' itemName='"+itemName+"' class=\"hei_30 fl mar_l10\" style=\"width:100%;\">"
    if(isView){
    	html+="<div class=\"mar_r20 fl\" >";
    	html+="<span class=\"over_attr_val cursor\" >"+itemName+"</span>"+"&nbsp;&nbsp;";
    	html+="数量"+"<input type=\"text\" readonly=\"readonly\" maxlength='4' onkeyup=\"numInput(this)\" value='"+num+"'  class=\"sub_num input_Style2 hei_24 wid_30 mar_l10\" >";
    	html+="</div>";
    }else{
    	html+="<div class=\"mar_r20 fl\" >";
    	html+="<span class=\"over_attr_val cursor\" >"+itemName+"</span>"+"&nbsp;&nbsp;";
    	if(baseAddService=='valueaddId'){
    		 html+="<input type=\"hidden\" value='1' onkeyup=\"numInput(this)\" class=\"sub_num input_Style2 hei_24 wid_30 mar_l10\">"+"&nbsp;&nbsp;"+"<a class='cursor' style='color:red' onclick=\"deleteChar(this,'"+selectBaseItemId+"')\">删除</a>";
    	}else{
    		 html+="数量"+"<input type=\"text\" maxlength='4' onkeyup=\"numInput(this)\" value='"+num+"' class=\"sub_num input_Style2 hei_24 wid_30 mar_l10\">"+"&nbsp;&nbsp;"+"<a class='cursor' style='color:red' onclick=\"deleteChar(this,'"+selectBaseItemId+"')\">删除</a>";
    	}
    	html+="</div>";
    }
	for(var i=0;i<skuInfo.length;i++){
		var valueObj = new Array();
		var skuId=skuInfo[i].skuId;
		var attributes=skuInfo[i].attributes;
		var attr=attributes.split(";");
		for(var j = 0;j<attr.length;j++){
			if(attr[j]==""){
				continue;
			}
			var valueArray = attr[j].split(":");
			var attId=valueArray[0];
			var attName=titleMap[attId];
			var valueId = valueArray[1];
			var valueName = valueMap[valueId];
			var Circle={  
				 "id":valueId,  
				 "name":valueName,
				 "attrId":attId,
				 "attName":attName
			};
			valueObj.push(Circle);
		}
		var select = "";
		if(lists!=''){
			$.each(lists, function (n, pf) {
				if(pf.addSource==type){
					if(pf.subSkuId==skuId){
						select = "checked='checked'";
					}
				}
			});
		}
		    html += "<p class=\"mar_l10 fl\">";
		    html += "<input  id=\""+skuId+"\" "+select+" skuid=\""+skuId+"\"  addSource=\""+type+"\" name='serviceAttrCheck'   type=\"checkbox\" />";
		for(var h = 0;h < valueObj.length;h++){
			var obj = valueObj[h];
			html += "<span   attrId=\""+obj.attrId+"\" valueId=\""+obj.id+"\" attrType=\"2\" class=\"cursor\"  >"+obj.attName+':'+obj.name+"</span>"+"&nbsp;&nbsp;";
		}
		html += "</p>";
	}
	html += "</div>";
	$("#"+baseAddService).append(html);
}
function saveMap(jsonobj,skuInfo,itemId){
	for(var i=0;i<jsonobj.length;i++){  
	    var sellAttrId=jsonobj[i].id;
	    var sellAttrName =jsonobj[i].name;
	    for(var j=0;j<jsonobj[i].values.length;j++){
	    	var sellAttrValName = jsonobj[i].values[j].name;
	    	var sellAttrValId = jsonobj[i].values[j].id;
	    	if(sellAttrOneMap[sellAttrId] == null){
				var mapValue = new Array();
				mapValue[0] = sellAttrValId;
				sellAttrOneMap[sellAttrId] = mapValue;
			}else{
				var mapValue = sellAttrOneMap[sellAttrId];
				mapValue.push(sellAttrValId);
				sellAttrOneMap[sellAttrId] = mapValue;
			}
	    	//sellAttrOneMap  存放选中的销售属性，如果里面没有此销售属性key则新增，如果里面有则先删除再新增
			//存放sku标题
			titleMap[sellAttrId] = sellAttrName;
			//存放属性名称，在组装sku表格时显示使用
			valueMap[sellAttrValId] = sellAttrValName;
	    }
	}
	for(var i=0;i<skuInfo.length;i++){
		var skuId=skuInfo[i].skuId;
		var attributes=skuInfo[i].attributes;
		if(sellmap[itemId] == null){
			var mapValue = new Array();
			mapValue[0] = skuId;
			sellmap[itemId] = mapValue;
		}else{
			var mapValue = sellmap[itemId];
			mapValue.push(skuId);
			sellmap[itemId] = mapValue;
		}
		if(sellSkuAttrMap[skuId] == null){
			var mapValue = new Array();
			mapValue[0] = attributes;
			sellSkuAttrMap[skuId] = mapValue;
		}else{
			var mapValue = sellSkuAttrMap[skuId];
			mapValue.push(attributes);
			sellSkuAttrMap[skuId] = mapValue;
		}
	}
}
//组装销售属性展示模板   attrObj:属性对象
function saleAttrHtml(attrObj,parentObj){
	sellAttrNum ++ ;
	var bindObjIds = new Array();
	var html = "<form action=\"\" id=\"sellAttr_form_"+sellAttrNum+"\"><p class=\"mar_l10 mar_t10\">";
	html += "<span formNum=\""+sellAttrNum+"\" class=\"font_14 over_attr_val cursor\" id=\"sale_attr_title_"+attrObj.id+"\" attrId=\""+attrObj.id+"\" attrType=\"1\" style=\"font-weight:bold;\">"+attrObj.name+"</span></p>";
	html += "<div class=\"color_select mar_l10 mar_t10 hei_30 \" id=\"shopSellAttrDiv_"+sellAttrNum+"\">";
	html += "<input type=\"hidden\" name=\"attr.id\" value=\""+attrObj.id+"\" />";
	html += "<input type=\"hidden\" name=\"attr.name\" value=\""+attrObj.name+"\" />";
	html += "<input type=\"hidden\" name=\"attr.status\" value=\"1\" />";
	//添加属性值
	$.each(attrObj.values, function (m, categoryVal) {
		var select = "";
		//平台上传的时候、编辑的时候，默认数据初始化选中
		if(plstItemId && !itemId){
			select = "checked='checked'";
		}else if(attrSaleStr.indexOf(attrObj.id+":"+categoryVal.id+";") >= 0){
			select = "checked='checked'";
		}
		//新属性保存之后选中原来的数据
		if(saleCategoryAttr.indexOf(attrObj.id+":"+categoryVal.id+";") >= 0){
			select = "checked='checked'";
		}
		html += "<p class=\"mar_l10\">";
		html += "<input  onchange=\"clickSellAttrClick('check_"+categoryVal.id+"','','')\" "+select+" name='sellAttrValueCheck' attrSaleStr='"+attrObj.id+":"+categoryVal.id+";' sellAttrId='"+attrObj.id+"' sellAttrName='"+attrObj.name+"' sellAttrValId='"+categoryVal.id+"' sellAttrValName='"+categoryVal.name+"' id=\"check_"+categoryVal.id+"\" type=\"checkbox\" />";
		html += "<span title=\"点击执行修改或删除\" formNum=\""+sellAttrNum+"\" attrId=\""+categoryVal.id+"\" attrType=\"2\" class=\"over_attr_val cursor\" id='span_attrVal_"+categoryVal.id+"'>"+categoryVal.name+"</span>";
		html += "</p>";
		//bindObjIds.push("span_attrVal_"+categoryVal.id);
	});
	html += "<div id='hideAttrIDV_"+sellAttrNum+"'></div>";
	//如果是平台上传则取消属性自定义
	html += "<p name=\"addBtn\" class=\"mar_l10 hei_30 fl\" ><a class=\"cursor\" onclick=\"addSellAttrValue('hideAttrIDV_"+sellAttrNum+"',"+sellAttrNum+")\">+添加属性值</a></p>";
	html +="<p id=\"btnP_"+sellAttrNum+"\" style=\"display:none;\" class='fl mar_20'>";
	html += "<button class=\"button_4 hei_24 \" id=\"cateBtnSave_"+attrObj.id+"\" type=\"button\" onclick=\"saveSellAttr('sellAttr_form_"+sellAttrNum+"',"+sellAttrNum+")\">保存</button> ";//保存a
	html += "<button class=\"button_4 hei_24 attr_cancle\" id=\"cateBtnCancle_"+attrObj.id+"\" type=\"button\" onclick=\"cancleSaveSellAttr('hideAttrIDV_"+sellAttrNum+"','btnP_"+sellAttrNum+"')\">取消</button> ";//保存a
	html += "</p>";
	html += "</div><div class=\"clear\"></div></form>";
	parentObj.append(html);
	//为每一个表单定义校验 解决jquery validate form嵌套问题 
	vali("sellAttr_form_"+sellAttrNum);
}

//绑定销售属性、销售属性值编辑事件
function bindOver(className){ 
    $("."+className).click(function(e) {
    	if($("#saleDiv").length > 0){
    		return;
    	}
    	var obj = $(this);
    	//元素类型 用来标识销售属性或者销售属性值 (1:销售属性  2:销售属性值)
    	var attrType = obj.attr("attrType");
    	//销售属性名称长度限制文6个字符，销售属性值长度限制为28个字符。
    	var maxlength = "6";
    	if(attrType == "2"){
    		maxlength = "28";
    	}
    	var id = obj.attr("attrId");
    	var formId = "sellAttr_form_" + obj.attr("formNum");
    	//获取点击元素的上一个元素(checkbox)
    	var prevObj = obj.prev();
    	var html = "<div class=\"po_fai\" id=\"saleDiv\">";
    	html += "<div class=\"po_fai_bg\"></div>";
    	html += "<div class=\"po_main1 pad_t10\" >";
    	html += "<div class=\"hei_32 bg_05 pad_l10 font_14b\">";
    	html += "<i class=\"demo-icons fa-times-circle font_16 fr mar_r10\" onclick=\"removeElement('saleDiv')\"></i>销售属性</div>";
    	html += "<div class=\"shop_xinxi mar_t10\">";
    	html += "<ul class=\"add_xx mar_l10\">";
    	html += "<li class=\"wid_300 mar_l8 mar_tb10\">销售属性 : ";
    	html += "<input type=\"text\" maxlength=\""+maxlength+"\"  id=\"attr_edit_name\" value=\""+obj.text()+"\" class=\"border-4 wid_180 mar_l10 input_Style2 hei_30\" /></li>";
    	html += "<li class=\"wid_260 mar_l8 mar_tb10\">";
    	html += "<input type=\"button\" class=\"button_2 hei_35 font_14b\" id=\"attr_edit_save\" onclick=\"modifyAttr('modify',"+id+","+attrType+",'"+formId+"')\" value=\"保存\"/>";
    	html += "<input type=\"button\" class=\"button_2 hei_35 font_14b mar_l10\" id=\"attr_edit_delete\" onclick=\"modifyAttr('delete',"+id+","+attrType+",'"+formId+"')\" value=\"删除\"/>";
    	html += "</li></ul></div><div class=\"clear\"></div></div></div>";
    	$("body").append(html);
    });
}

//销售属性/属性值 编辑/删除操作
function modifyAttr(doType,id,attrType,formId){
	//删除
	var url = "/sellcenter/sellProduct/modifySaleAttr";
	if(doType == "delete"){
		var flag = false;
		if(confirm("删除销售属性可能影响到商品sku,您确定要删除吗?")){
			flag = true;
		}
		if(!flag){
			return;
		}
		url = "/sellcenter/sellProduct/deleteSaleAttr";
	}else{
	
		var name=$("#attr_edit_name").val(); 
		    if(name == "" || name.length == 0){
			alert("属性名称不能为空");
			return false;
		}
	}
	$.ajax({
		url: path + url,
		type:"post",
		data:{
			id:id,
			name:$("#attr_edit_name").val(),
			attrType:attrType
		},
		dataType: "json",
		success:function(data){
			//执行成功
			if(data.result){
				if(doType == "delete"){
					//删除
					if(attrType == "1"){
						//删除销售属性
						var thisObj = $("#"+formId+" #sale_attr_title_"+id);
						delete sellAttrOneMap[id];
						//重新生成sku列表
						getSkuList('','');
						thisObj.parent().parent().remove();
					}else{
						//删除销售属性值
						var thisObj = $("#"+formId+" #span_attrVal_"+id);
						var attrObj = thisObj.prev();
						if(attrObj.is(':checked')){
							//删除全局变量中缓存的数值
							var sellAttrId = attrObj.attr("sellattrid");
							var mapValue = sellAttrOneMap[sellAttrId];
							mapValue.remove(id);
							if(mapValue.length == 0){
								delete sellAttrOneMap[sellAttrId];
							}else{
								sellAttrOneMap[sellAttrId] = mapValue;
							}
							//重新生成sku列表
							getSkuList('','');
						}
						//删除销售属性值以及前面的复选框
						thisObj.parent().remove();
					}
				}else{
					//编辑
					if(attrType == "1"){
						//编辑销售属性
						$("#"+formId+" #sale_attr_title_"+id).text(name);
						//修改销售属性值缓存的属性名称
						$("#"+formId+" input[sellattrid="+id+"]").attr("sellattrname",name);
						$("#"+formId+" input[name='attr.name']").val(name);
						//修改sku列表标题
						$("#skuTable #skuTitle_"+id).html(name);
					}else{
						//编辑销售属性值
						$("#"+formId+" #span_attrVal_"+id).text(name);
						$("#"+formId+" #check_"+id).attr("sellattrvalname",name);
						//获取sku列表修改并执行修改
						var skuValueObjs = $("#skuTable td[name='skuValue_"+id+"']");
						$.each(skuValueObjs, function (n, svo) {
							$(svo).html(name);
						})
						//获取sku图片列表并执行修改
						var skuImgObjs = $("#skuPicDiv li[name='img_attr_"+id+"']");
						$.each(skuImgObjs, function (n, sio) {
							$(sio).html(name);
						})
					}
				}
			}else{
				alert("执行失败")
			}
			removeElement('saleDiv');
		}
	})
}

//添加商品销售属性值
function addSellAttrValue(contentId,bufNum){
	//将按钮、输入框显示
	$("#hideAttrIDV_"+bufNum).show();
	$("#btnP_"+bufNum).show();
	sellAttrNum ++ ;
	var html = "<p class=\"mar_l10 fl\"><input type=\"checkbox\" /><input type=\"hidden\"  name=\"attr.values["+sellAttrNum+"].status\" value=\"1\" /><input type=\"text\" maxlength=\"28\" id=\"attrValue_"+sellAttrNum+"\" name=\"attr.values["+sellAttrNum+"].name\" class=\"wid_40 input_Style2 hei_30 mar_lr5\" value=\"\" /></p>";
	$("#"+contentId).append(html);
}
//添加商品销售属性
function addSellAttr(contentId){
	sellAttrNum ++ ;
	var html = "<form action=\"\" name=\"sellAttr_form_"+sellAttrNum+"\" id=\"sellAttr_form_"+sellAttrNum+"\"><p class=\"mar_l10 mar_t10\">";
	html += "<input type=\"text\" name=\"attr.name\" maxlength=\"6\" id=\"sellAttr_"+sellAttrNum+"\" class=\"wid_80 input_Style2 hei_30 mar_lr5\"  />";
	html += "<input type=\"hidden\" name=\"attr.status\" value=\"1\" />";
	html += "</p>";
	html += "<div class=\"color_select mar_l10 mar_t10 hei_30 \" id=\"shopSellAttrDiv_"+sellAttrNum+"\">";
	html += "<div id='hideAttrIDV_"+sellAttrNum+"'>";
	html += "<p class=\"mar_l10 fl\"><input type=\"checkbox\" /><input type=\"hidden\" name=\"attr.values["+sellAttrNum+"].status\" value=\"1\" /><input type=\"text\" maxlength=\"28\" name=\"attr.values["+sellAttrNum+"].name\" id=\"attrValue_"+sellAttrNum+"\" class=\"wid_40 input_Style2 hei_30 mar_lr5\" /></p>";
	html += "</div>";
	//如果是平台上传则取消属性自定义
	html += "<p name=\"addBtn\" class=\"mar_l10 hei_30 fl\" ><a class=\"cursor\" onclick=\"addSellAttrValue('hideAttrIDV_"+sellAttrNum+"',"+sellAttrNum+")\">+添加属性值</a></p>";
	html +="<p class='fl mar_20'>";
	html += "<button class=\"button_4 hei_24 \" type=\"button\" onclick=\"saveSellAttr('sellAttr_form_"+sellAttrNum+"',"+sellAttrNum+")\">保存</button> ";
	html += "<button class=\"button_4 hei_24 attr_cancle\" type=\"button\" onclick=\"removeElement('sellAttr_form_"+sellAttrNum+"')\">取消</button> ";
	html += "</p>";
	html += "</div><div class=\"clear\"></div></form>";
	$("#"+contentId).append(html);
	//为每一个表单定义校验 解决jquery validate form嵌套问题 
	vali("sellAttr_form_"+sellAttrNum);
}

//保存销售属性
function saveSellAttr(formId,formNum){
	//判断是否选择平台分类
//	var categoryVal = $("#itemCategoryLevelThree").val();
//	if(!categoryVal){
//		alert("请选择平台分类");
//		return false;
//	}
	//缓存已经选中的销售属性 防止用户保存销售属性后之sku丢失
	var sellCategory = $("#shopCategoryDiv input[type='checkbox']");
	var i = 0;
	$.each(sellCategory, function (n, shopCategory) {
		if($("#"+shopCategory.id).is(':checked')){
			saleCategoryAttr += $("#"+shopCategory.id).attr("attrsalestr");
		}
	});
	//保存新增的销售属性
	var formObj = $('#'+formId).serialize();
	//校验销售属性是否为空
	var attr = $("#"+formId+" input[name='attr.name']").val();
	if(attr == ""){
		alert("销售属性不能为空");
		$("#"+formId+" input[name='attr.name']").focus();
		return false;
	}
	var cid = $("#itemCategoryLevelThree").val();
	$.ajax({
		url: path + "/sellcenter/sellProduct/saveShopSellAttribute",
		type:"post",
		data:formObj,
		dataType: "json",
		success:function(data){
			var attrObj = data.itemAttr;
			//添加销售属性值
			if(data.saveType == "attrValue"){
				$.each(attrObj.values, function (n, attrValueObj) {
					var addAttrValue = "<p class=\"mar_l10\">";
					addAttrValue += "<input   onchange=\"clickSellAttrClick('check_"+
								attrValueObj.id+"','','')\" name='sellAttrValueCheck' attrSaleStr='"+attrObj.id+":"+attrValueObj.id+";' sellAttrId='"+
								attrObj.id+"' sellAttrName='"+attrObj.name+"' sellAttrValId='"+attrValueObj.id+"' sellAttrValName='"+
								attrValueObj.name+"' id=\"check_"+attrValueObj.id+"\" type=\"checkbox\" />";
					addAttrValue += "<span title=\"点击执行修改或删除\" formNum=\""+formNum+"\" attrId=\""+attrValueObj.id+"\" attrType=\"2\" class=\"over_attr_val cursor\" id='span_attrVal_"+attrValueObj.id+"' >"+attrValueObj.name+"</span>";
					addAttrValue += "</p>";
					$("#hideAttrIDV_"+formNum).before(addAttrValue);
				});
			}else{
				var optionId = "shopCategoryDiv";
				var optionItem = $("#"+optionId);
				saleAttrHtml(attrObj,optionItem);
			}
			//绑定鼠标滑过事件，展示销售属性和属性值的编辑和删除按钮
			bindOver("over_attr_val");
			//隐藏添加的属性值文本框
			$("#"+formId+" .attr_cancle").click();
			//console.log(data);
			//刷新销售属性
			//getShopCategoryAttribute('shopCategoryDiv',$("#itemCategoryLevelThree").val(),'');
		}
	});
}

