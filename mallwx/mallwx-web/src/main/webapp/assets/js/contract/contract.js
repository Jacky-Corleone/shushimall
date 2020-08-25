$(document).ready(function () {

    updateContractInfo.removeMat = new Array();
    createItemLi["items"] = new Array();
    if($("#sourcePage").val()=="buyer"){
        changeIconToRed(2);
    }
    if($("#sourcePage").val()=="seller"){
        changeIconToRed(3);
    }
    
    $("#tabs0").val($("#myTab0_Content0").html());
    $("#tabs1").val($("#myTab0_Content1").html());
    $("#tabs2").val($("#myTab0_Content2").html());
    $("#tabs3").val($("#myTab0_Content3").html());
});
var canRefresh = true;
var currentPage = 1;
var totalPage = 1;
var myScroll;

$(function () {
    loaded();
    canRefresh = false;
    $("#partBDetail").html('');
    /*    var page = 1;
     loadProducts("company", page);*/
});

function loaded() {
    refreshScroll("company");
}

function onkeyup_product(obj) {
    $(obj).bind("keyup", function () {
        this.value = this.value.replace(/\D/g,'');
    });
}


function searchContractInfoByCondition(e) {

    var contractName = $('#contractSearchForm').find("[name='contractName']").val();
    var companyName = $('#contractSearchForm').find("[name='companyName']").val();
    var itemName = $('#contractSearchForm').find("[name='itemName']").val();
    var supplierName = $('#contractSearchForm').find("[name='supplierName']").val();
    var printerName = $('#contractSearchForm').find("[name='printerName']").val();
    
    var sourcePage = $("#sourcePage").val();
    //var itemName = $('#contractSearchForm').find("[name='itemName']").val();
    if (contractName && companyName && itemName && supplierName && printerName && !$.trim(contractName) && !$.trim(companyName) && !$.trim(itemName)&& !$.trim(supplierName)&& !$.trim(printerName)) {
        return;
    }
    var requestUrl;
    var d_id = "";
    if (e == 0) {
        requestUrl = "contractPage";
        d_id = "contractList";
    }
    if (e == 1) {
        requestUrl = "approveContractInfo";
        d_id = "contractApproveList";
    }
    if (e == 2) {
        requestUrl = "confirmContractInfo";
        d_id = "contractConfirmList";
    }
    var temp = {};
    temp["contractName"] = contractName;
    temp["companyName"] = companyName;
    temp["itemName"] = itemName;
    temp["supplierName"] = supplierName;
    temp["printerName"] = printerName;


    
    //temp["itemName"]=itemName;
    var contractSearchModel = JSON.stringify(temp);
    addLoadMask();
    $.ajax({
        type: "post",
        dataType: "html",
        url: $("#contextPath").val() + "/contract/" + requestUrl,
        data: {
            contractSearchModel: contractSearchModel,
            sourcePage: sourcePage
        },
        success: function (result) {
            $("#"+d_id).html(result);
            removeLoadMask();	
        }
    	
    });

}

//修改协议
function modifyContract(contractId) {
    window.location = $("#contextPath").val() + "/contract/toContractUpdate?sourcePage=" + $("#sourcePage").val() + "&contractId=" + contractId;
}

//附件上传
var imgcount=0;
var sccount = 0;
var cimgcount=0;
//文件上传
function startUpload() {
    //判断图片格式
    var fileInput = $("#fileInput")[0].files[0].name;
    var maxSize = 10240000;
    if ($("#fileInput")[0].files[0].size > maxSize) {
        $("#fileInput").replaceWith('<input type="file" id="fileInput" class="wid_50 form-control hei_20" name="file" onchange="startUpload();" />');
        alert("上传文件过大！");
        return false;
    }
    $.ajaxFileUpload({
        url: $("#contextPath").val() + "/orderWx/upload?size=10240000", //用于文件上传的服务器端请求地址
        secureuri: false, //是否需要安全协议，一般设置为false
        fileElementId: 'fileInput', //文件上传域的ID
        dataType: 'text', //返回值类型 一般设置为json
        type: "post",
        success: function (data, status) {  //服务器成功响应处理函数
            data = JSON.parse(data.substring(data.indexOf("{"), data.indexOf("}") + 1));
            if (data.success) {
                sccount = sccount + 1;
                var trid = "trid" + sccount;
                appendtable(data.url, trid, fileInput);
            } else {
                alert(data.msg);
                $("#fileInput").replaceWith('<input type="file" id="fileInput" class="wid_50 form-control hei_20" name="file" onchange="startUpload();" />');
            }
        },
        error: function (data, status, e) {//服务器响应失败处理函数
            alert(JSON.stringify(data));
            alert(e);
            alert("亲系统繁忙，请稍后再试");
            $("#fileInput").replaceWith('<input type="file" id="fileInput" class="wid_50 form-control hei_20" name="file" onchange="startUpload();" />');
        }
    });
}
function appendtable(url, id, fileName) {
    	var imageUrl = $("#gix").val() + url;
    	 var number = $("input:hidden[name='contractUrlShowListNumber']").val();
    	 if(typeof(number) == "undefined"){
    		 number=0; 
    	 }
    	
    	$("#annex").val(url);
    	imgcount=imgcount+1;
    	cimgcount=imgcount+Number(number);
    	
        if(cimgcount < 6){
    	   $("#uploadLi").append("<p class='wid_150 fl font_right pad_r5' id='url"+id+"' style='line-height:30px;margin-left:25%'>附件:&nbsp;&nbsp;&nbsp;<a href='" + imageUrl + "' >" + fileName + "</a><a href=javascript:cancleimg('"+id+"','"+url+"') >&nbsp;&nbsp;&nbsp;删除</a><input type='hidden' id='urlname"+id+"' name='urlname' value='"+url+"'  /></br></p>  ");
        }else{
        	alert("附件数不能大于5个");
        }
        if(cimgcount>0){
        	 var a = $("input:hidden[name='urlname']");
             var picUrl="";
             $(a).each(function(){
             	 if($(this).val()){
             		 picUrl=picUrl+$(this).val()+",";
             	 }
             });
         	$("#annexcc").val(picUrl); 
        	
        } 
}
//清除指定tr
function cancleimg(id,url) {
	  $("#url"+id).remove("");
	  var number = $("input:hidden[name='contractUrlShowListNumber']").val();
      var c=$("#annexcc").val();
     
      imgcount=imgcount-1;

      if(imgcount==0){
    	  var d=c.replace(url,"");
    	  var e=d.replace(",","");
      	  $("#annexcc").val(e); 
      }else{
    	  var d=c.replace(url,"");
    	  var e=d.substring(0,d.length-1);
      	  $("#annexcc").val(e); 
      } 
}
function radioChange() {
    if ($(":radio:checked").val() == 0) {
        $("#choseDay").removeAttr("disabled");
        $("#choseMonth").attr("disabled", "disabled");
        $("#choseMonth").val("");
    }
    if ($(":radio:checked").val() == 1) {
        $("#choseDay").attr("disabled", "disabled");
        $("#choseDay").val("");
        $("#choseMonth").removeAttr("disabled");
    }
}

//选择公司弹出层
function getCompany(e, obj, page) {
	//初始化滚动条位置
	myScroll.scrollTo(0, 0, 0);
	setTimeout(refresh(obj), 100);
//	setTimeout(searchCompany(), 200);
	
    addLoadMask();
    if (!page) {
        currentPage = 1;
        page = currentPage;
        totalPage = 1;
    }
    $.ajax({
        url: $("#contextPath").val() + "/contract/getCompanyNameList",
        data: {
            "uType": $("#searchType").val(),
            "page": page,
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#IdivOperate").val(e);
            $("#partBDetail" + obj).html(data);
            $("#pullUpLabel" + obj).html("上拉加载更多...");
            canRefresh = true;
            removeLoadMask();
            showDiv();
        },
        error: function () {
            alert("网络错误");
        }
    });
}

//公司弹出层展示
function showDiv() {
    var Idiv = document.getElementById("Idiv");
    Idiv.style.display = "block";

}

function closeDiv(obj) // 公司弹出层关闭
{
    $(obj).parent().parent().toggle();
    removeLoadMask();
}

function closeOrderDiv(obj){
    $(obj).parent().parent().parent().toggle();
    revmoveLoadMask();
}


//选择并填入公司名称
function putCompanyValue(obj) {
    var sp = $("#sourcePage").val();
    var operateType = $("#IdivOperate").val();
    var companyName = $(obj).find("[name='companyName']").val();
    var companyId = $(obj).find("[name='companyId']").val();
    var uName = $(obj).find("[name='userName']").val();
    $('#contractSearchForm').find("[name='companyName']").val(companyName);
    if (sp == "seller") {
        //判断传入的操作符，如果是0将值赋予查询FORM是1赋予创建FORM
        if (operateType == 0) {
            $('#contractSearchForm').find("[name='printerName']").val(companyName);
            $('#contractSearchForm').find("[name='printerId']").val(companyId);
        }
        if (operateType == 1) {
            $('#contractCreateForm').find("[name='printerName']").val(companyName);
            $('#contractCreateForm').find("[name='printerId']").val(companyId);
        }
    }
    if (sp == "buyer") {
        if (operateType == 0) {
            $('#contractSearchForm').find("[name='supplierName']").val(companyName);
            $('#contractSearchForm').find("[name='supplierId']").val(companyId);
        }
        if (operateType == 1) {
            $('#contractCreateForm').find("[name='supplierName']").val(companyName);
            $('#contractCreateForm').find("[name='supplierId']").val(companyId);
        }
    }
    if ($("#itemList ul")) {
        $("#itemList ul").each(function () {
            if ($(this).find("[name='id']").val()) {
                updateContractInfo.removeMat.push($(this).find("[name='id']").val());
            }
        });
    }
    createItemLi.items.splice(0);
    $("#itemList").html("");
    $("#companyCloseButton").trigger('click');
}
//协议明细弹出层加载
function showContractDetail(contractId) {

    addLoadMask();
    var param = {};
    param.callback = function () {
        $("#contractDetailsDIV").hide()
    };
    registerGoBack(param);
    $.ajax({
        url: $("#contextPath").val() + "/contract/getContractDetail",
        data: {
            "contractId": contractId
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#contractDetailList").html(data);
            showContractDetailsDIV();
            removeLoadMask();
        }
    });
}


//协议明细弹出层
function showContractDetailsDIV() {
    var Idiv = document.getElementById("contractDetailsDIV");
    Idiv.style.display = "block";
}


function deleteContract(contractId, contractNo, contractName,sourcePage) {
    kltconfirm("确认删除协议号为[" + contractNo + "]的[" + contractName + "]协议吗？", function () {
    	window.location.href=$("#contextPath").val() + "/contract/deleteContractInfo?sourcePag="+sourcePage+"&contractId="+contractId
//        $.ajax({
//            url: $("#contextPath").val() + "/contract/deleteContractInfo",
//            data: {
//                "contractId": contractId
//            },
//            type: 'post',
//            success: function (data) {
//                alert("删除成功！", function () {
//                  //  location.reload();
//                	window.location=$("#contextPath").val() + "/contract/contractPage?sourcePage=" + sourcePage;
//                	//return;
//                })
//            },
//            error: function () {
//                alert("网络错误");
//            }
//
//        });

    });

}

function contractPageTabs(thisObj, Num) {
    if (thisObj.className == "active")
        return;
    
    var tabObj = thisObj.parentNode.id;
    var tabList = document.getElementById(tabObj).getElementsByTagName("li");

    for (i = 0; i < tabList.length; i++) {
        if (i == Num) {
            thisObj.className = "active";
            if (Num == 0) {
                window.location = $("#contextPath").val() + "/contract/contractPage?sourcePage=" + $("#sourcePage").val();
            }
            if(Num == 1){
            	$("#myTab0_Content0").empty();
            	$("#myTab0_Content2").empty();
            	$("#myTab0_Content3").empty();
            	$("#myTab0_Content1").html($("#tabs1").val());
            }
            if (Num == 2) {
            	$("#myTab0_Content0").empty();
            	$("#myTab0_Content1").empty();
            	$("#myTab0_Content3").empty();
            	$("#myTab0_Content2").html($("#tabs2").val());
                addLoadMask();
                $.ajax({
                    url: $("#contextPath").val() + "/contract/approveContractInfo?sourcePage=" + $("#sourcePage").val(),
                    type: 'post',
                    dataType: 'html',
                    success: function (data) {
                        $("#contractApproveList").html(data);
                        removeLoadMask();
                    }
                });
            }
            if (Num == 3) {
            	$("#myTab0_Content0").empty();
            	$("#myTab0_Content1").empty();
            	$("#myTab0_Content2").empty();
            	$("#myTab0_Content3").html($("#tabs3").val());
                addLoadMask();
                $.ajax({
                    url: $("#contextPath").val() + "/contract/confirmContractInfo?sourcePage=" + $("#sourcePage").val(),
                    type: 'post',
                    dataType: 'html',
                    success: function (data) {
                        $("#contractConfirmList").html(data);
                        removeLoadMask();
                    }
                });

            }
            document.getElementById(tabObj + "_Content" + i).style.display = "block";
        } else {
            $("#searchFrom" + i).hide();
            tabList[i].className = "normal";
            document.getElementById(tabObj + "_Content" + i).style.display = "none";
        }
    }
}

//物品选择弹出层
function showItemSearchDIV() {
    addLoadMask();
    if ("seller" == $("#sourcePage").val() || "update" == $("#sourcePage").val()) {
        var supplierId = $("#userId").val();
    }
    if ("buyer" == $("#sourcePage").val()) {
        var supplierId = $.trim($("#contractCreateForm").find("[name='supplierId']").val());
    }
    if (supplierId && "" != supplierId) {
        searchItem();
    } else {
        toast("请选择卖方信息");
        removeLoadMask();
    }

}
//展示物品弹出层
function showItemDIV() {
    var Idiv = document.getElementById("itemSearchDIV");
    Idiv.style.display = "block";
}

//搜索物品
function searchItem() {
    var keyword = $.trim($("#searchKeyWord").val());
    var supplierId = $.trim($("#contractCreateForm").find("[name='supplierId']").val());

    $.ajax({
        url: $("#contextPath").val() + "/contract/searchItem",
        data: {
            "Keyword": keyword,
            "supplierId": supplierId
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#itemSearchResult").html(data);
            showItemDIV();
            removeLoadMask();
        }
    });
}
//同意审批
function approveContract(contractId, operation, reason) {
    addLoadMask();
    var sourcePage = $("#sourcePage").val();

    $.ajax({
        url: $("#contextPath").val() + "/contract/approveContractInfo",
        data: {
            contractId: contractId,
            operation: operation,
            reason: reason,
            sourcePage: sourcePage
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            if (operation == "拒绝" || operation == "同意") {
                $("#contractApproveList").html(data);
                removeLoadMask();
            } else {
                window.location = $("#contextPath").val() + "/contract/contractPage?sourcePage=" + sourcePage;
            }
        }
    });
}

//协议确认
function confirmContract(contractId, operation, reason) {
    addLoadMask();
    var sourcePage = $("#sourcePage").val();
    $.ajax({
        url: $("#contextPath").val() + "/contract/confirmContractInfo",
        data: {
            contractId: contractId,
            operation: operation,
            reason: reason,
            sourcePage: sourcePage
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            if (operation == "拒绝" || operation == "同意") {
                $("#contractConfirmList").html(data);
                removeLoadMask();
            } else {
                window.location = $("#contextPath").val() + "/contract/contractPage?sourcePage=" + sourcePage;
            }

        }
    });
}


function closeItemSearchDIV() {
    var Idiv = document.getElementById("itemSearchDIV");
    Idiv.style.display = "none";
    removeLoadMask();
}

function checkIsChosen(e) {
    var skuId = $(e).parent().parent().find("[name='itemSkuId']").val();
    if (createItemLi.items.length > 0) {
        for (var i = 0, len = createItemLi.items.length; i < len; i++) {
            if (skuId == createItemLi.items[i]) {
                alert("该物品已添加到列表中！");
                $(e).prop("checked", false);
                return;
            }
        }
    }
}

function empty(){
	   $("#itemList").html("");
	   createItemLi["items"] = new Array();
}

function createItemLi() {
    var a = $("input:checkbox[name='itemOrder']:checked");
	var protocolType=$("#protocolTypeId").val();
	
    var temp = "";

    $(a).each(function () {
        var obj = $(this).parent().parent().parent();
        var itemName = obj.find("[name='itemName']").val();
        var itemPrice = obj.find("[name='itemPrice']").val();
        var itemSkuId = obj.find("[name='itemSkuId']").val();
        var salerAttr = obj.find("[name='salerAttr']").val();
        temp += "<ul class='mar_lr5'>" +
            "<div class='hei_32 border_2'>" +
            "<p class='wid_95 fl shop_order_p over_ell'><span>商品名称：" + itemName + "</span></p>" +
            "<input type='hidden' name='itemName' value='" + itemName + "'>" +
            "<input type='hidden' name='matCd' value='" + itemSkuId + "'>" +
            "<input type='hidden' name='id'>" +
            "</div>" +
            "<div class='border_2 shop_order pad_tb5'>" +
            "<p class='wid_95 fl shop_order_p over_ell'><span>商品编号：" + itemSkuId + "</span></p>" +
            "<p class='wid_95 fl shop_order_p over_ell'><span>商品属性：" + salerAttr + "</span></p>" ;
            if(protocolType=="3"){
            	 temp +="<p class='wid_95 fl shop_order_pLs over_ell'>总价值：￥<input type='text' onkeyup='priceTrim(this)' maxlength='8' class='form-control wid_30 hei_24'  name='cost' id='cost' />";
            	 temp +="<p class='wid_95 fl shop_order_p over_ell'>价&nbsp;&nbsp;&nbsp;格：￥<input type='text' onkeyup='priceTrim(this)' maxlength='8' class='form-control wid_30 hei_24' value='" + itemPrice + "' name='matPrice' />";
            	
            }else if(protocolType=="2"){
            	 temp +="<p class='wid_95 fl shop_order_pLs over_ell'>数量：&nbsp;&nbsp;&nbsp;<input type='text' onkeyup='onkeyup_product(this)' maxlength='8' class='form-control wid_30 hei_24'  name='number' id='number' />";
            	 temp +="<p class='wid_95 fl shop_order_p over_ell'>价格：￥<input type='text' onkeyup='priceTrim(this)' maxlength='8' class='form-control wid_30 hei_24' value='" + itemPrice + "' name='matPrice' />";
            	
            }else{
            	 temp +="<p class='wid_95 fl shop_order_p over_ell'>价格：￥<input type='text' onkeyup='priceTrim(this)' maxlength='8' class='form-control wid_30 hei_24' value='" + itemPrice + "' name='matPrice' />";
            }
            
         
        temp += "<button class='fr button_3 pad_mlr5' onclick='removeItem(this)'>删除</button></p>" +
            "<div class='clear'></div>" +
            "</div></ul>";
    });
    $("#itemList").append(temp);
    if ($("#itemList ul")) {
        $("#itemList ul").each(function () {
            createItemLi.items.push($(this).find("[name='matCd']").val());
        });
    }
    closeItemSearchDIV();
}

function removeItem(e) {
    var skuId = $(e).parent().parent().parent().find("[name='matCd']").val();
    var matId = $(e).parent().parent().parent().find("[name='id']").val();

    if (matId && "" != matId) {
        updateContractInfo.removeMat.push(matId);
    }
    if (createItemLi.items) {
        for (var i = 0, len = createItemLi.items.length; i < len; i++) {
            if (skuId == createItemLi.items[i]) {
                createItemLi.items.splice(i, 1);

            }
        }

    }
    console.log(createItemLi.items);
    $(e).parent().parent().parent().remove();
}

function addContractInfo() {
    //onkeyup="this.value=this.value.replace(^[0-9]+(.[0-9]{2})?$,'')"
    //console.debug();

    var obj = $("#itemList ul");
    var contractName = $("#contractName").val();
    var contractOrderNo = $("#contractOrderNo").val();
    var beginDate = $("#beginDate").val();
    var endDate = $("#endDate").val();
    var begin = new Date(beginDate);
    var end = new Date(endDate);
    var paymentType = $(":radio:checked").val();
    var choseDay = $("#choseDay").val();
    var choseMonth = $("#choseMonth").val();
    var sourcePage = $("#sourcePage").val();
    var annexcc = $("#annexcc").val();	
    var paymentDays;

    var flag = true;
    $("#itemList ul").find("input[type='text'][name='matPrice']").each(function (index) {
        if (!this.value) {
            toast("物品价格不能为空");
            flag = false;
        }
        if (!parseFloat(this.value) > 0) {
            toast("物品价格必须大于0");
            flag = false;
        }
    });

    	  $("#itemList ul").find("input[type='text'][name='number']").each(function (index) {
    	        if (!this.value) {
    	            toast("物品数量不能为空");
    	            flag = false;
    	        }
    	        if (!parseFloat(this.value) > 0) {
    	            toast("物品数量必须大于0");
    	            flag = false;
    	        }
    	    });
    	

  
 
    $("#itemList ul").find("input[type='text'][name='cost']").each(function (index) {
        if (!this.value) {
            toast("物品总价值不能为空");
            flag = false;
        }
        if (!parseFloat(this.value) > 0) {
            toast("物品总价值不能为空");
            flag = false;
        }
    });
 
    
    if (!flag) {
        return false;
    }

    if (!contractOrderNo || "" == contractOrderNo) {
        alert("协议序号不能为空！");
        return;
    }
    //时间验证
    if (!contractName || contractName == "") {
        alert("协议名字不能为空！");
        return;
    }

    if (beginDate == null || beginDate == "") {
        alert("协议开始时间不能为空！");
        return;
    }
    if (endDate == null || endDate == "") {
        alert("协议开始时间不能为空！");
        return;
    }
    if (begin - end > 0) {
        alert("合同开始日期不能大于合同结束日期");
        return;
    }
    if (begin - end == 0) {
        alert("合同开始日期不能等于合同结束日期！");
        return;
    }
    if (!choseDay && !choseMonth) {
        if (choseDay == "" && choseMonth == "") {
            alert("请选择账期");
            return;
        }
    }
    if (obj.length == 0) {
        alert("请选择合同物品");
        return;
    }
    kltconfirm("确认要执行此操作吗？", function () {
        addLoadMask();
        if (paymentType == 1) {
            paymentDays = $("#choseMonth").val();
        }
        if (paymentType == 0) {
            paymentDays = $("#choseDay").val();
        }

        var temp = new Array();
        for (var i = 0; i < obj.length; i++) {
            temp.push(getProduct(obj[i]));
        }
        var paymentTemp = {};
        paymentTemp['paymentType'] = paymentType;
        paymentTemp['paymentDays'] = paymentDays;
        var contractPaymentTerm = JSON.stringify(paymentTemp);
        var contractMatDTOs = JSON.stringify(temp);
        var contractInfo = $("#contractCreateForm").serialize();

        
        contractInfo = decodeURIComponent(contractInfo, true);
        //contractInfo=encodeURI(encodeURI(contractInfo));
        //var ContractPaymentTermDTO =
        contractInfo = contractInfo.replace(/&/g, "\",\"");
        contractInfo = contractInfo.replace(/=/g, "\":\"");
        contractInfo = "{\"" + contractInfo + "\"}";
        $.ajax({
            type: "post",
            dataType: "json",
            url: $("#contextPath").val() + "/contract/contractAdd",
            data: {
                "contractInfo": contractInfo,
                "contractMatDTOs": contractMatDTOs,
                "contractPaymentTerm": contractPaymentTerm,
                "sourcePage": sourcePage,
                "annexcc":annexcc
                
            },
            success: function (result) {
                console.log(result);
                console.log(result.success)
                if (result.success==true) {
                    toast("保存成功", function () {
                        window.location = $("#contextPath").val() + "/contract/contractPage?sourcePage=" + $("#sourcePage").val();
                    });

                } else{
                    var temp = new Array();
                    for (var er in result.errorMessages) {
                        temp.push(er);
                    }
                }
                removeLoadMask();
            },
            error: function () {
                removeLoadMask();
                alert("网络错误！！");
            }
        });

    });
}


function getProduct(obj) {
    var dom = $(obj);
    var matCd = dom.find("[name='matCd']").val();
    var itemName = dom.find("[name='itemName']").val();
    var matPrice = dom.find("[name='matPrice']").val();
    var protocolType=$("#protocolType").val(); //协议类型
    var number=dom.find("[name='number']").val();
    var cost=dom.find("[name='cost']").val()
    var contractMatDTO = {};
    contractMatDTO.matCd = matCd;
    contractMatDTO.itemName = itemName;
    contractMatDTO.matPrice = matPrice;
    contractMatDTO.protocolType = protocolType;
    contractMatDTO.number = number;
    contractMatDTO.cost = cost;

    return contractMatDTO;
}

function priceTrim(obj) {
    $(obj).bind("keyup", function () {
//        this.value = this.value.replace(/[^\d.]/g, "");
//        //必须保证第一个为数字而不是.
//        this.value = this.value.replace(/^\./g, "");
//        //保证只有出现一个.而没有多个.
//        this.value = this.value.replace(/\.{2,}/g, ".");
//        //保证.只出现一次，而不能出现两次以上
//        this.value = this.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    	 this.value=(this.value.match(/\d+(\.\d{0,2})?/)||[''])[0]
    });
    //$(obj).value=obj.value.replace('^[0-9]+(.[0-9]{2})?$','');
}

function toggleMenu() {
    $("#navigation").toggle();
}

function updateContractInfo() {
    var contractNo = $("#contractNo").val();
    var contractOrderNo = $("#contractOrderNo").val();
    var obj = $("#itemList ul");
    var beginDate = $("#beginDate").val();
    var endDate = $("#endDate").val();
    var needApprove = $("#needApprove").val();
    var begin = new Date(beginDate);
    var end = new Date(endDate);
    var annexcc = $("#annexcc").val();	

    var paymentType = $(":radio:checked").val();
    var paymentDays;

    var flag = true;
    $("#itemList ul").find("input[type='text'][name='matPrice']").each(function (index) {
        if (!this.value) {
            toast("物品价格不能为空");
            flag = false;
        }
        if (!parseFloat(this.value) > 0) {
            toast("物品价格必须大于0");
            flag = false;
        }
    });

    	  $("#itemList ul").find("input[type='text'][name='number']").each(function (index) {
    	        if (!this.value) {
    	            toast("物品数量不能为空");
    	            flag = false;
    	        }
    	        if (!parseFloat(this.value) > 0) {
    	            toast("物品数量必须大于0");
    	            flag = false;
    	        }
    	    });
    	

  
 
    $("#itemList ul").find("input[type='text'][name='cost']").each(function (index) {
        if (!this.value) {
            toast("物品总价值不能为空");
            flag = false;
        }
        if (!parseFloat(this.value) > 0) {
            toast("物品总价值不能为空");
            flag = false;
        }
    });
 
    
    if (!flag) {
        return false;
    }
    

    if (!contractOrderNo || "" == contractOrderNo) {
        alert("协议序号不能为空");
        return;
    }
    //时间验证
    if (paymentType == 1) {
        paymentDays = $("#choseMonth").val();
    }
    if (paymentType == 0) {
        paymentDays = $("#choseDay").val();
    }
    if (beginDate == null || beginDate == "") {
        alert("协议开始时间不能为空");
        return;
    }
    if (endDate == null || endDate == "") {
        alert("协议开始时间不能为空");
        return;
    }
    if (begin - end > 0) {
        alert("合同开始日期不能大于合同结束日期");
        return;
    }
    if (begin - end == 0) {
        alert("合同开始日期不能等于合同结束日期！");
        return;
    }
    if (obj.length < 1) {
        alert("请选择协议物品！");
        return;
    }
    kltconfirm("确认要执行此操作吗？",function(){
        addLoadMask();
        var temp = new Array();
        for (var i = 0; i < obj.length; i++) {
            temp.push(getProduct(obj[i]));
        }
        var paymentTemp = {};
        paymentTemp['paymentType'] = paymentType;
        paymentTemp['paymentDays'] = paymentDays;
        var contractPaymentTerm = JSON.stringify(paymentTemp);
        var contractMatDTOs = JSON.stringify(temp);
        var removeMats = JSON.stringify(updateContractInfo.removeMat);
        var contractInfo = $("#contractCreateForm").serialize();
        contractInfo = decodeURIComponent(contractInfo, true);
        contractInfo = contractInfo.replace(/&/g, "\",\"");
        contractInfo = contractInfo.replace(/=/g, "\":\"");
        contractInfo = "{\"" + contractInfo + "\"}";

        $.ajax({
            type: "post",
            dataType: "json",
            url: $("#contextPath").val() + "/contract/contractUpdate",
            data: {
                "contractInfo": contractInfo,
                "contractMatDTOs": contractMatDTOs,
                "contractPaymentTerm": contractPaymentTerm,
                "removeMat": removeMats,
                "needApprove": needApprove,
                "annexcc": annexcc
                
            },
            success: function (result) {
          
                if (result.success==true) {
                    toast("保存成功",function(){
                        window.location = $("#contextPath").val() + "/contract/contractPage?sourcePage=" + $("#sourcePage").val();
                    });
                }
                if (!result.success==false) {
                    var temp = new Array();
                    for (var er in result.errorMessages) {
                        temp.push(er);
                    }
                }
                removeLoadMask();
            },
            error: function () {
                removeLoadMask();
                alert("网络错误");
            }
        });
    });


}

function closeMessageDiv(obj) {
    $(obj).parent().parent().toggle();
    removeLoadMask();
    window.location = $("#contextPath").val() + "/contract/contractPage";
}
function orderContract(contractId) {
    addLoadMask();

    $.ajax({
        url: $("#contextPath").val() + "/contract/contractOrder",
        data: {
            "contractId": contractId
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#orderContract").html(data);
            showContractOrderDIV();
            removeLoadMask();
        },
        error: function () {
            alert("网络错误！");
            removeLoadMask();
        }
    });

}

function showContractOrderDIV() {
    var Idiv = document.getElementById("orderContractDIV");
    Idiv.style.display = "block";
}

function orderItem() {
    var itemList = $("#contractOrderItem ul").find("input:checkbox[name='chosenItem']:checked");
    var contractBeginDate = new Date($("#orderContract").find("[name='contractBeginDate']").val());
    var contractEndDate = new Date($("#orderContract").find("[name='contractEndDate']").val());
    var contractNo = $("#orderContract").find("[name='contractNo']").val();

    var nowDate = new Date();
    var access = true;
    $("[name='quantity']").each(function () {
    	var quantity = $(this).val();
    	var cost = $(this).parent().siblings().find("[name='cost']").val();
    	var zcost = $(this).parent().siblings().find("[name='countpay']").val();
    	var matPrice = $(this).parent().siblings().find("[name='matPriceec']").val();
    	var accountsnum=$(this).parent().siblings().find("input[name='number']").val();//总数量
    	var zquantity=$(this).parent().siblings().find("input[name='countNumber']").val();//已购买数量
    	if( typeof(zquantity) == "undefined"){
    		var num=accountsnum-quantity;
    		if(num<0){
        		alert("已超出允许购买总数量。");
        		access = false; //跳出循环
        		return false;
        	}
    	}else{
    		var num=accountsnum-quantity-zquantity;
    		if(num<0){
        		alert("已超出允许购买总数量。");
        		access = false; //跳出循环
        		return false;
        	}
    	}
    	if( typeof(zcost) == "undefined"){
    		var costnumber=cost-matPrice*quantity;
    		if(costnumber<=0){
        		alert("已超出允许购买总金额。");
        		access = false; //跳出循环
        		return false;
        	}
    	}else{
    		var costnumber=cost-matPrice*quantity-zcost;
    		if(costnumber<=0){
        		alert("已超出允许购买总金额。");
        		access = false; //跳出循环
        		return false;
        	}
    	}
    	
    //	var costnumber=cost-matPrice*quantity-zcost;
    	
    })
    if(!access){
    	return ;
    }
    if (itemList.length == 0) {
        alert("请选择下单物品！！！");
        return;
    }
    if ($("#contractOrderItem").find("[name='quantity']").val() < 1) {
        alert("请输入正确的下单数量");
        return;
    }
    
  
    if (nowDate - contractBeginDate >= 0 && nowDate - contractEndDate <= 0) {
        var temp = new Array();
        for (var i = 0, len = itemList.length; i < len; i++) {
            temp.push(getContractMats($(itemList[i]).parent().parent().parent()));
        }
        var jsonProducts = JSON.stringify(temp);
        $.ajax({
            url: $("#contextPath").val() + "/contract/orderItem",
            type: "post",
            dataType: "html",
            data: {
                "jsonProducts": jsonProducts
            },
            success: function (data) {
                $("#orderType").val(1);
                $("#orderContractNo").val(contractNo);
                document.getElementById("contractOrderForm").submit();
            },
            error: function () {
                alert("网络错误！");
            }

        });

    } else {
        alert("当前日期不在协议日期内，无法下单");
    }
}

function getContractMats(obj) {
    var dom = $(obj);
    var matId = dom.find("[name='matId']").val();
    var quantity = dom.find("[name='quantity']").val();
    var cost = dom.find("[name='cost']").val();
    var number = dom.find("[name='number']").val(); 
    var protocolType=$("#protocolType").val();
    
    var contractMatDTO = {};
    contractMatDTO.matId = matId;
    contractMatDTO.quantity = quantity;
    contractMatDTO.cost = cost;
    contractMatDTO.number = number;
    contractMatDTO.protocolType=protocolType;

    return contractMatDTO;
}

function showConfirmDIV(cid, cn, type, op) {
    var reason;
    kltconfirm("确定" + op + "协议:[" + cn + "]的" + type + "请求吗？", function () {
        if (type == "确认") {
            if (op == "拒绝") {
                reason = promptCheck();
                if (reason == null) {
                    return;
                }
            }
            confirmContract(cid, op, reason);
        }
        if (type == "审批") {
            if (op == "拒绝") {
                reason = promptCheck();
                if (reason == null) {
                    return;
                }
            }
            approveContract(cid, op, reason);
        }
    });
    /*    if (confirm("确定" + op + "协议:[" + cn + "]的" + type + "请求吗？")) {

     if (type == "确认") {
     if (op == "拒绝") {
     reason = promptCheck();
     if (reason == null) {
     return;
     }
     }
     confirmContract(cid, op, reason);
     }
     if (type == "审批") {
     if (op == "拒绝") {
     reason = promptCheck();
     if (reason == null) {
     return;
     }
     }
     approveContract(cid, op, reason);
     }
     }*/
}

function promptCheck() {
    var reason = prompt("请输入拒绝原因", "");
    if (reason == null) {
        return null;
    } else if (!$.trim(reason)) {
        return promptCheck();
    } else {
        return reason;
    }
}

//下拉刷新
function refresh(obj) {
    if (!canRefresh) {
        return;
    }
    canRefresh = false;
    var page = 1;
    $("#partBDetail" + obj).html('');
    loadProducts(obj, page);
}

// 下一页
function nextPage(obj) {
    if (!canRefresh) {
        return;
        console.log(canRefresh);

    }
    if (currentPage > totalPage) {
        $("#pullUpLabel" + obj).html("没有更多信息了");
        return
    }
    $("#pullUpLabel" + obj).html("加载中……");
    canRefresh = false;
    console.log(canRefresh);
    var page = currentPage + 1;
    loadProducts(obj, page);

}

function searchCompany() {
    $("#partBDetailcompany").html("");
    var sp = $("#sourcePage").val();
    var type;
    if (sp == "buyer") {
        type = 3
    }
    if (sp == "seller") {
        type = 2
    }
    var company = $("#Idiv").find("[name='company']").val();
    console.log(company);
    $.ajax({
        url: $("#contextPath").val() + "/contract/getCompanyNameList",
        data: {
            "uType": type,
            "company": company,
            "page": 1
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#pullUpLabelcompany").html("上拉加载更多...");
            $("#partBDetailcompany").html(data);
            canRefresh = true;
            $("#pullDownLabelcompany").html("下拉刷新");
            showDiv();
        }

    });

}

function loadProducts(obj, page) {
    var company = $("#Idiv").find("[name='company']").val();
    $.ajax({
        url: $("#contextPath").val() + "/contract/getCompanyNameList",
        data: {
            "uType": $("#searchType").val(),
            "company": company,
            "page": page
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#partBDetail" + obj).append(data);
            $("#pullUpLabel" + obj).html("上拉加载更多...");
            canRefresh = true;
            $("#pullDownLabel" + obj).html("下拉刷新");
            setTimeout(function () {
                myScroll.refresh();
            }, 200);
        },
        error: function () {
            alert("网络错误");
        }
    });

}

function refreshScroll(obj) {
    myScroll = new iScroll(
        obj,
        {
            vScrollbar: true,
            hscrollbar: false,
            topOffset: 20,
            onBeforeScrollStart: function (e) {
                var nodeType = e.explicitOriginalTarget ? e.explicitOriginalTarget.nodeName.toLowerCase() : (e.target ? e.target.nodeName.toLowerCase() : '');
                if (nodeType != 'select' && nodeType != 'option' && nodeType != 'input' && nodeType != 'textarea') {
                    e.preventDefault();
                }
            },
            onScrollStart: function () {
                this.___scrollStartY___ = this.y;
            },
            onScrollMove: function () {
                if (this.y > 50) {
                    $("#pullDownLabel" + obj).html("松开刷新");
                } 
                if ((this.y < this.maxScrollY - 50) && (this.___scrollStartY___ - this.y) > 50) {
                    $("#pullUpLabel" + obj).html("松开继续加载");
                }
            },
            onTouchEnd: function () {
                if (this.y > 50) {
                    setTimeout(refresh(obj), 100);
                }
                if ((this.y < this.maxScrollY - 50 ) && (this.___scrollStartY___ - this.y) > 50) {
                    setTimeout(nextPage(obj), 100);
                }
            }
        });
}

function reset() {
    $('#contractSearchForm').find("[name='contractName']").val("");
    $('#contractSearchForm').find("[name='printerId']").val("");
    $('#contractSearchForm').find("[name='supplierId']").val("");
    $('#contractSearchForm').find("[name='itemName']").val("");
    $('#contractSearchForm').find("[name='contractName']").val("");
    $('#contractSearchForm').find("[name='supplierName']").val("");
    $('#contractSearchForm').find("[name='printerName']").val("");
    $('#companyName').val("");
    $('#partBName').val("");
}

//显示审批人列表
function showApproveList(e) {
	if($(e).is(":checked")){
		   $("#approveList").removeAttr("disabled");
		   $("#approveBy").attr("style","display:block");
	   }else{
		   $("#approveList").attr("disabled","disabled");
		   $("#approveBy").attr("style","display:none");
	   }
}


//协议类型
function protocolTypeSelecT() {
	var protocolType=$("#protocolTypeId").val()
	if(protocolType=="2"){
		$("#quantity").show();
		$("#globas").hide();
	}else if (protocolType=="3"){
		$("#globas").show();
		$("#quantity").hide();
	}else{
		$("#quantity").hide();
		$("#globas").hide();
	}
}

function accounts(o){
	var accountsnum=$(o).parent().siblings().find("input[name='number']").val();//总数量
	var protocolType=$("#protocolType").val(); //协议类型
	var quantity=$(o).val();//购买数量
	var cost=$(o).parent().siblings().find("input[name='cost']").val();//商品总价值
	var matPrice=$(o).parent().siblings().find("input[name='matPriceec']").val();//商品单价 
	var zquantity=$(o).parent().siblings().find("input[name='countNumber']").val();//已购买数量
	var zcost=$(o).parent().siblings().find("input[name='countpay']").val();//已购买金额
	var message="剩余数量：";
	var messagecost="剩余总金额：￥";
	if( typeof(zquantity) == "undefined"){
		var num=accountsnum-quantity;
	}else{
		var num=accountsnum-quantity-zquantity;
	}
	if( typeof(zcost) == "undefined"){
		var costnumber=cost-matPrice*quantity;
	}else{
		var costnumber=cost-matPrice*quantity-zcost;
	}

	if(protocolType=="2"){
		if(num>=0){
			alert(message+num);
		}else{
			alert("已超出允许购买数量。");
		}
	}else if(protocolType=="3"){
		if(costnumber>=0){
			alert(messagecost+costnumber);
		}else{
			alert("已超出允许购买总金额。");
		}
	}	
}
