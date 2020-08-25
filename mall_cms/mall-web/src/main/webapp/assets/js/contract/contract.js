$(document).ready(function () {

    $("#addGoods").click(function () {
        window.open('goodsListContract', 'Derek', 'height=800,width=1000,status=yes,toolbar=yes,menubar=no,location=yes');
    });

    updateContractInfo.removeMat = new Array();
    createItemLi["items"] = new Array();
    function numberformat(domInput) {
        $(domInput).css("ime-mode", "disabled");
        $(domInput).bind("keypress", function (e) {
            var code = (e.keyCode ? e.keyCode : e.which);  //兼容火狐 IE
            if (!$.browser.msie && (e.keyCode == 0x8))  //火狐下 不能使用退格键
            {
                return;
            }
            return code >= 48 && code <= 57 || code == 46;
        });
        $(domInput).bind("blur", function () {
            if (this.value.lastIndexOf(".") == (this.value.length - 1)) {
                this.value = this.value.substr(0, this.value.length - 1);
            } else if (isNaN(this.value)) {
                this.value = " ";
            }
        });
        $(domInput).bind("paste", function () {
            var s = clipboardData.getData('text');
            if (!/\D/.test(s));
            value = s.replace(/^0*/, '');
            return false;
        });
        $(domInput).bind("dragenter", function () {
            return false;
        });
        $(domInput).bind("keyup", function () {
            this.value = this.value.replace(/[^\d.]/g, "");
            //必须保证第一个为数字而不是.
            this.value = this.value.replace(/^\./g, "");
            //保证只有出现一个.而没有多个.
            this.value = this.value.replace(/\.{2,}/g, ".");
            //保证.只出现一次，而不能出现两次以上
            this.value = this.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        });
    }

});

//附件上传
var sccount = 0;
//文件上传
function startUpload() {
    //判断图片格式
    var fileInput = $("#fileInput")[0].files[0].name;
    var maxSize = 10240000;
    if ($("#fileInput")[0].files[0].size > maxSize) {
        $("#fileInput").replaceWith('<input type="file" id="fileInput" class="wid_260 input_Style2 hei_30" name="file" onchange="startUpload();" />');
        alert("上传文件过大！");
        return false;
    }

    $.ajaxFileUpload({
        url: $("#contextPath").val() + "/fileUpload/upload?size=10240000", //用于文件上传的服务器端请求地址
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
                $("#fileInput").replaceWith('<input type="file" id="fileInput" class="wid_260 input_Style2 hei_30" name="file" onchange="startUpload();" />');
            }
        },
        error: function (data, status, e) {//服务器响应失败处理函数
            alert(JSON.stringify(data));
            alert(e);
            alert("亲系统繁忙，请稍后再试");
            $("#fileInput").replaceWith('<input type="file" id="fileInput" class="wid_260 input_Style2 hei_30" name="file" onchange="startUpload();" />');
        }
    });
}

function appendtable(url, id, fileName) {
    var imageUrl = $("#gix").val() + url;
    $("#annex").val(url);
    //console.log($("#annex").val());
    $("#uploadLi").html("<span class='font_wid mar_r10'>附件 : </span><span class='wid_30 over_ell'><a href='" + imageUrl + "' target='view_window'>" + fileName + "</a></span><a style='line-height:30px;margin-left:5%' href=javascript:cancleimg() >删除</a>");

}

//清除指定tr
function cancleimg() {
    $("#uploadLi").html("<span class='font_wid mar_r10'>附件 : </span><input type='file' class='wid_260 input_Style2 hei_30' id='fileInput' name='file' onchange='startUpload();'/>");
    $("#annex").val('');
}


function priceTrim(obj) {
    $(obj).bind("keyup", function () {
        this.value = this.value.replace(/[^\d.]/g, "");
        //必须保证第一个为数字而不是.
        this.value = this.value.replace(/^\./g, "");
        //保证只有出现一个.而没有多个.
        this.value = this.value.replace(/\.{2,}/g, ".");
        //保证.只出现一次，而不能出现两次以上
        this.value = this.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    });
    //$(obj).value=obj.value.replace('^[0-9]+(.[0-9]{2})?$','');
}

function onkeyup_product(obj) {
    $(obj).bind("keyup", function () {
        this.value = this.value.replace(/\D/g,'');
    });
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

/*function goodsDetailInfo(itemId, itemName, itemName2, itemName3) {
 var ulLength = $(".right_xinxi ul").length;
 // 选中的商品信息添加到询价明细中
 var ulHTML = "<ul class='list_top hei_40' id='ul'> <li class='wid_50'>" + ulLength + "</li>" +
 "<li class='wid_150'><input type='hidden' value='" + itemId + "' name='matCd'/> " + itemId + "</li>" +
 "<li class='wid_230'>+itemName+</li>" +
 "<li class='wid_130' name='matSpec'>" + itemName2 + "</li>" +
 "<li class='wid_110 font_cen' name='matUnit'>" + itemName3 + "</li>" +
 "<li class='wid_200'>" +
 "<input type='text' onkeyup='priceTrim(this)' maxlength='8' class='wid_50 input_Style2 hei_24'  name='matPrice' />" +
 "<i class='mar_lr10'>元</i></li>" +
 "<li class='wid_80 font_cen font_7a' onclick='deleteChosenTR(this)'>删除</li></ul>";
 $("#addGoods").before(ulHTML);
 }*/

// 获取选中的乙方信息
function getPartBDetail(e) {
    var sp = $("#sourcePage").val();
    var oper = $("#IdivOperate").val();
    var childs = $(e).children();
    if (sp == "buyer") {
        if (oper == 0) {
            $('#companyName').val(childs[0].textContent);
            $("#supplierId").val(childs[4].textContent);
        }
        if (oper == 1) {
            $('#companyName').val(childs[0].textContent);
            $('#supplierConector').val(childs[1].textContent);
            $('#supplierShopId').val($(e).find("[name='shopId']").val());
            $('#conSupplierId').val($(e).find("[name='conSupplierId']").val());
            //console.log($(e).find("[name='conSupplierId']").val());
            $('#mobilePhone').val(childs[2].textContent);
            $('#Email').val(childs[3].textContent);
        }

    }
    if (sp == "seller") {
        if (oper == 0) {
            $('#companyName').val(childs[0].textContent);
            $("#printerId").val(childs[4].textContent);
        }
        if (oper == 1) {
            $('#buyerCompany').val(childs[0].textContent);
            $('#buyerCompayId').val($(e).find("[name='conSupplierId']").val());
            //console.log($(e).find("[name='conSupplierId']").val());
            $('#buyerConector').val(childs[1].textContent);
            $('#buyerMobile').val(childs[2].textContent);
            $('#buyerEmail').val(childs[3].textContent);
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

    $("#Idiv").toggle();
    closeLoadMask();
}

//获取选中的联系人信息
function changeConector(e) {
//	$('#companyName').val(e.cells[0].textContent);
    $('#supplierConector').val(e.cells[1].textContent);
    $('#mobilePhone').val(e.cells[2].textContent);
    $('#Email').val(e.cells[3].textContent);
    $('#conSupplierId').val(e.cells[4].textContent);
    $('#conParentId').val(e.cells[5].textContent);
    closeOdiv();
}

/* 提交：基本信息 */
function addContractInfo() {
    //onkeyup="this.value=this.value.replace(^[0-9]+(.[0-9]{2})?$,'')"
//    console.debug($("#itemList ul").find("input[type='text'][name='matPrice']"));

    var obj = $("#itemList ul");
    var beginDate = $("#beginDate").val();
    var endDate = $("#endDate").val();
    var begin = new Date(beginDate);
    var end = new Date(endDate);
    var paymentType = $(":radio:checked").val();
    var sourcePage = $("#sourcePage").val();
    var paymentDays
    var contractName = $("#contractName").val();
    //console.log(contractName);
    if (!contractName || "" == $.trim(contractName)) {
        alert("协议名称不能为空！");
        return;
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
    if(!paymentType || ""==$.trim(paymentType)){
    	alert("协议账期不能为空！");
        return;
    }
    //时间验证
    if (paymentType == 1) {
        paymentDays = $("#choseMonth").val();
    }
    if (paymentType == 0) {
        paymentDays = $("#choseDay").val();
    }
    if(!paymentDays||""==$.trim(paymentDays)){
    	alert("协议账期不能为空！");
        return;
    }
    if (obj[0] == null) {
        alert("请选择物品");
        return;
    }

    var flag = true;
    $("#itemList ul").find("input[type='text'][name='matPrice']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的单价不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的单价必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }
    var flag = true;
    $("#itemList ul").find("input[type='text'][name='number']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的数量不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的数量必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }
    var flag = true;
    $("#itemList ul").find("input[type='text'][name='cost']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的总价值不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的总价值必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }
    createLoadMask();
    var temp = new Array();
    for (var i = 0; i < obj.length; i++) {
        temp.push(getProduct(obj[i]));
    }
    var paymentTemp = {};
    paymentTemp['paymentType'] = paymentType;
    paymentTemp['paymentDays'] = paymentDays;
    var contractPaymentTerm = JSON.stringify(paymentTemp);

    var contractMatDTOs = JSON.stringify(temp);
    var contractInfo = $("#contractInfoForm").serialize();
    contractInfo = decodeURIComponent(contractInfo, true);
    //var ContractPaymentTermDTO =
    contractInfo = contractInfo.replace(/&/g, "\",\"");
    contractInfo = contractInfo.replace(/=/g, "\":\"");
    contractInfo = "{\"" + contractInfo + "\"}";
    $.ajax({
        type: "post",
        dataType: "text",
        url: $("#contextPath").val() + "/contractAdd",
        data: {
            "contractInfo": contractInfo,
            "contractMatDTOs": contractMatDTOs,
            "contractPaymentTerm": contractPaymentTerm,
            "sourcePage": sourcePage
        },
        success: function (result) {

            //console.log(result);
            result = eval("(" + result + ")");
            //console.log(result.success)
            if (result.success) {
                alert("保存成功");
                window.location = $("#contextPath").val() + "/contract?sourcePage=" + $("#sourcePage").val();
            }
            if (!result.success) {
                var temp = new Array();
                for (var er in result.errorMessages) {
                    temp.push(er);
                }
            }
            closeLoadMask();
        },
        error: function () {
            closeLoadMask();
            alert("网络错误！！");
        }
    });
}


/* 提交：基本信息立即发布 */
function immediately() {
    //onkeyup="this.value=this.value.replace(^[0-9]+(.[0-9]{2})?$,'')"
//    console.debug($("#itemList ul").find("input[type='text'][name='matPrice']"));

    var obj = $("#itemList ul");
    var beginDate = $("#beginDate").val();
    var endDate = $("#endDate").val();
    var begin = new Date(beginDate);
    var end = new Date(endDate);
    var paymentType = $(":radio:checked").val();
    var sourcePage = $("#sourcePage").val();
    var paymentDays
    var contractName = $("#contractName").val();
    //console.log(contractName);
    if (!contractName || "" == $.trim(contractName)) {
        alert("协议名称不能为空！");
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

    if (obj[0] == null) {
        alert("请选择物品");
        return;
    }
    
    var flag = true;
    $("#itemList ul").find("input[type='text'][name='matPrice']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的单价不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的单价必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }
    var flag = true;
    $("#itemList ul").find("input[type='text'][name='number']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的数量不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的数量必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }
    var flag = true;
    $("#itemList ul").find("input[type='text'][name='cost']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的总价值不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的总价值必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }

    createLoadMask();
    var temp = new Array();
    for (var i = 0; i < obj.length; i++) {
        temp.push(getProduct(obj[i]));
    }
    var paymentTemp = {};
    paymentTemp['paymentType'] = paymentType;
    paymentTemp['paymentDays'] = paymentDays;
    var contractPaymentTerm = JSON.stringify(paymentTemp);

    var contractMatDTOs = JSON.stringify(temp);
    var contractInfo = $("#contractInfoForm").serialize();
    contractInfo = decodeURIComponent(contractInfo, true);
    //var ContractPaymentTermDTO =
    contractInfo = contractInfo.replace(/&/g, "\",\"");
    contractInfo = contractInfo.replace(/=/g, "\":\"");
    contractInfo = "{\"" + contractInfo + "\"}";
    $.ajax({
        type: "post",
        dataType: "text",
        url: $("#contextPath").val() + "/immediatelyup",
        data: {
            "contractInfo": contractInfo,
            "contractMatDTOs": contractMatDTOs,
            "contractPaymentTerm": contractPaymentTerm,
            "sourcePage": sourcePage
        },
        success: function (result) {

            //console.log(result);
            result = eval("(" + result + ")");
            //console.log(result.success)
            if (result.success) {
                alert("发布成功");
                window.location = $("#contextPath").val() + "/contract?sourcePage=" + $("#sourcePage").val();
            }
            if (!result.success) {
                var temp = new Array();
                for (var er in result.errorMessages) {
                    temp.push(er);
                }
            }
            closeLoadMask();
        },
        error: function () {
            closeLoadMask();
            alert("网络错误！！");
        }
    });
}



function updateContractInfo() {
	var contractId = $("#id").val();
    var contractOrderNo = $("#contractOrderNo").val();
    var contractName = $("#contractName").val();
    var obj = $("#itemList ul");
    var beginDate = $("#beginDate").val();
    var endDate = $("#endDate").val();
    var begin = new Date(beginDate);
    var end = new Date(endDate);
    var paymentType = $(":radio:checked").val();
    var paymentDays;
    var needApprove;
    var sp = $("#sourcePage").val();
    if(obj.length == 0){
    	alert("请选择商品！");
        return;
    }
    if (contractName == "") {
        alert("协议名称不能为空！");
        return;
    }
    if (contractOrderNo == "") {
        alert("协议序号不能为空！")
        return;
    }
    if (beginDate == null || beginDate == "") {
        alert("协议开始时间不能为空");
        return;
    }
    if (endDate == null || endDate == "") {
        alert("协议开始时间不能为空");
        return;
    }
    if (begin > end) {
        alert("合同开始日期不能大于合同结束日期");
        return;
    }
    if (begin == end) {
        alert("合同开始日期不能等于合同结束日期！");
        return;
    }
    if(!paymentType || ""==$.trim(paymentType)){
    	alert("协议账期不能为空！");
        return;
    }
    //时间验证
    if (paymentType == 1) {
        paymentDays = $("#choseMonth").val();
    }
    if (paymentType == 0) {
        paymentDays = $("#choseDay").val();
    }
    if(!paymentDays||""==$.trim(paymentDays)){
    	alert("协议账期不能为空！");
        return;
    }
    var flag = true;
    $("#itemList ul").find("input[type='text'][name='matPrice']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的单价不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的单价必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }
    var flag = true;
    $("#itemList ul").find("input[type='text'][name='number']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的数量不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的数量必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }
    var flag = true;
    $("#itemList ul").find("input[type='text'][name='cost']").each(function (index) {
        if (!this.value) {
            alert("第" + (index+1) + "行的总价值不能为空");
            flag = false;
            if (!flag) {
                return false;
            }
        }
        //console.debug(parseInt(this.value) + '---' + (index + 1));
        if (!parseFloat(this.value) > 0) {
            alert("第" + (index+1) + "行的总价值必须大于0");
            flag = false;
            if (!flag) {
                return false;
            }
        }
    });
    if (!flag) {
        return false;
    }
 	var protocolTypeId = $("#protocolTypeId").val();
    var temp = new Array();
    for (var i = 0; i < obj.length; i++) {
 //   	obj.find("[name='itemName']").val()
    	var id = $("[name='id']")[i].value;
    	var matPrice = $("[name='matPrice']")[i].value;
    	var matCd = $("[name='matCd']")[i].value;
   
    	if(protocolTypeId=="2"){
    	  	var number = $("[name='number']")[i].value;
    	}
    	if(protocolTypeId=="3"){
    		var cost = $("[name='cost']")[i].value;   
    	}
    	var temp1 = {};
    	temp1["id"] = id;
    	temp1["matPrice"] = matPrice;
    	temp1["matCd"] = matCd;
    	temp1["protocolTypeId"] = protocolTypeId;
    	if(protocolTypeId=="2"){
        	temp1["number"] = number;
    	}
    	if(protocolTypeId=="3"){
    		temp1["cost"] = cost; 
    	}
    	matCd
        temp.push(temp1);
    }
    if ($("#needApprove").prop("checked") == true) {
        needApprove = $("#needApprove").val();
        //console.log($("#needApprove").val());
    }

    var paymentTemp = {};
    paymentTemp['paymentType'] = paymentType;
    paymentTemp['paymentDays'] = paymentDays;
    var contractPaymentTerm = JSON.stringify(paymentTemp);
    var contractMatDTOs = JSON.stringify(temp);
    var contractInfo = $("#contractInfoForm").serialize();
    contractInfo = decodeURIComponent(contractInfo, true);
    var removeMats = JSON.stringify(updateContractInfo.removeMat);
    //var ContractPaymentTermDTO =
    contractInfo = contractInfo.replace(/&/g, "\",\"");
    contractInfo = contractInfo.replace(/=/g, "\":\"");
    contractInfo = "{\"" + contractInfo + "\"}";
    $.ajax({
        type: "post",
        dataType: "html",
        url: $("#contextPath").val() + "/contractUpdate",
        data: {
            "contractInfo": contractInfo,
            "contractMatDTOs": contractMatDTOs,
            "contractPaymentTerm": contractPaymentTerm,
            "needApprove": needApprove,
            "removeMat": removeMats
        },
        success: function (result) {
            $("#messDetail").html(result);
            alert("协议修改成功！");
            window.location = $("#contextPath").val() + "/contract?sourcePage=" + sp;
        },
        error: function () {
            alert("网络错误");
        }
    });

}

function getProduct(obj) {
    var dom = $(obj);
    var matCd = dom.find("[name='matCd']").val();
    var itemName = dom.find("[name='itemName']").val();
    var matPrice = dom.find("[name='matPrice']").val();
    var protocolType=$("#protocolType").val(); 
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

function searchCompany() {
    $("#partBDetail").html("");
    var sp = $("#sourcePage").val();
    var type;
    if (sp == "buyer") {
        type = 3
    }
    if (sp == "seller") {
        type = 2
    }
    var company = $.trim($("#Idiv").find("[name='company']").val());
    if ("请输入要查询的物品名称" == company) {
        company = null;
    }
    $.ajax({
        url: $("#contextPath").val() + "/getSellerBuyerDetail",
        data: {
            "uType": type,
            "company": company
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#partBDetail").html(data);
            showDiv();
        }

    });

}
function getCompany(e) {
    createLoadMask();
    var sp = $("#sourcePage").val();
    var type;
    if (sp == "buyer") {
        type = 3
    }
    if (sp == "seller") {
        type = 2
    }
    $.ajax({
        url: $("#contextPath").val() + "/getSellerBuyerDetail",
        data: {
            "uType": type
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#partBDetail").html(data);
            $("#IdivOperate").val(e);
            showDiv();
        }

    });
    // 以下部分使整个页面至灰不可点击
}
function toCompanyPage(page){
    var sp = $("#sourcePage").val();
    var type;
    if (sp == "buyer") {
        type = 3
    }
    if (sp == "seller") {
        type = 2
    }
    var company = $.trim($("#Idiv").find("[name='company']").val());
    $.ajax({
        url: $("#contextPath").val() + "/getSellerBuyerDetail",
        data: {
            "uType": type,
            "company": company,
            "page": page
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#partBDetail").html(data);
        }
    });
}


// 显示弹出层
function showDiv() {
    var obj = document.getElementById("Idiv");
    obj.style.display = "block";
    // 以下部分要将弹出层居中显示

}

function searchContractInfo(e, page) {
    var contractInfo = {};
    contractInfo['contractName'] = $("#contractName").val();
    contractInfo['companyName'] = $("#companyName").val();
    contractInfo['itemName'] = $("#itemName").val();
    contractInfo['status'] = $("#contractStatus").val();
    contractInfo['searchType'] = e;

    if(!page){
        page=1;
    }
    var contractSearchModel = JSON.stringify(contractInfo);
    $.ajax({
        url: $("#contextPath").val() + "/searchContractInfo",
        data: {
            "contractSearchModel": contractSearchModel,
            "sourcePage": $("#sourcePage").val(),
            "page": page,
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#contractList").html(data);
        },
        error: function () {
            alert("网络错误！");
        }

    });
}


function toUpdateContract(sp) {
    var a = $("input:checkbox[name='contractId']:checked");
    //var a=JSON.stringify($("input:checkbox[name=contractInfoNo]:checked"));
    if (a.length == 0) {
        alert("请选择一个协议");
        return;
    }
    if (a.length > 1) {
        alert("同一时间只能修改一个协议");
        return;
    }
    var status = $(a[0]).parent().parent().find("[name='status']").val();
    if ("3" == status || "1" == status || "5" == status || "6" == status ||"10"==status) {
        alert("该协议不能修改");
        return;
    }
    var contractId = $(a[0]).val();
    window.location = $("#contextPath").val() + "/toContractUpdate" + "?contractId=" + contractId + "&sourcePage=" + sp;
}

function goBack(){
	history.go(-1);
}

function deleteContract() {
    var a = $("input:checkbox[name='contractId']:checked");
    if (a.length == 0) {
        alert("请选择一个协议");
        return;
    }
    var temp = new Array();
    for (var i = 0; i < a.length; i++) {
        var obj = $(a[i]).parent().parent();
        var contractNo = obj.find("[name='contractNo']").val();
        var status = obj.find("[name='status']").val();
        if (status == 1 || status == 3 || status == 5 || status == 6 || status == 8) {
            alert("合同编号为" + contractNo + "的协议不可删除！请重新选择！");
            return;
        } else {
            temp.push(contractNo);
        }
    }
    var contractNos = JSON.stringify(temp);
    if(confirm("确定删除该协议吗?")){
    	$.ajax({
            url: $("#contextPath").val() + "/deleteContractInfo",
            data: {
                "contractNos": contractNos
            },
            type: 'post',
            dataType: 'html',
            success: function (data) {
                alert("删除成功！")
                location.reload();
            }
        });
    }
}

//复选框事件
//全选、取消全选的事件
function selectAll() {
    if ($("#all_select").is(":checked")) {
        $("input:checkbox[id='subCheck']").prop("checked", true);
        //$(":checkbox").attr("checked", true);
    } else {
        $("input:checkbox[id='subCheck']").prop("checked", false);
        //$(":checkbox").attr("checked", false);
    }
}
//子复选框的事件
function setSelectAll() {
    //当没有选中某个子复选框时，SelectAll取消选中
    if (!$("#subcheck").checked) {
        $("#all_select").prop("checked", false);
    }
    var chsub = $("input:checkbox[id='subCheck']").length; //获取subcheck的个数
    var checkedsub = $("input:checkbox[id='subCheck']:checked").length; //获取选中的subcheck的个数
    if (checkedsub == chsub) {
        $("#all_select").prop("checked", true);
    }
}

function getContractDetail(contractId, contractNo) {
    createLoadMask();
    $("#orderContractNo").val(contractNo);
    $.ajax({
        url: $("#contextPath").val() + "/getDetailInfo",
        data: {
            "contractId": contractId
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {

            $("#opt_1_content").html(data);

            closeLoadMask();
        },
        error: function () {
            alert("网络错误");
            closeLoadMask();
        }
    });
    getContractOrder();
}

function showConfirmDIV(type, op) {
    var nos = new Array();
    var n = []
    var temp = {};
    var a = $("input:checkbox[name='contractId']:checked");
    var reason;
    var register = $("#register").val()
    if (a.length == 0) {
        alert("请选择一个协议");
        return;
    }
    for (var i = 0, len = a.length; i < len; i++) {
        var t = $(a[i]).parent().parent().find("[name='status']").val();
        var createBy = $(a[i]).parent().parent().find("[name='createBy']").val();
        if (register != createBy) {
            alert("您不是协议[" + $(a[i]).parent().parent().find("[name='contractName']").val() + "]的创建人，无法进行此操作！！");
            return;
        }
        if (!temp[t]) {
            temp[t] = true;
            n.push(t);
        }
        nos.push($(a[i]).val());
    }
    if (n.length > 1) {
        alert("当前已选择了多种状态的协议,不可执行同一操作!")
        return;
    }

    if ("确认" == type) {
        if (("同意" == op || "拒绝" == op) && "3" != n[0]) {
            alert("请选择协议状态为待确认的协议进行此操作！");
            return;
        } else if ("重新提交" == op && !("4" == n[0] || "0" == n[0] || "已审批" == n[0] )) {
            alert("当前选择的协议状态无法进行此操作！");
            return;
        } else if ("发布" == op && "5" != n[0]) {
            alert("请选择协议状态为待发布的协议进行此操作！");
            return;
        }else if("终止"==op&&"10"==n[0]){
        	alert("此协议已被终止！")
            return;
        }else if (confirm("确定执行此请求吗?")) {
            if ("拒绝" == op) {
                reason = promptCheck();
                if (reason == null) {
                    return;
                }
            }
            confirmContract(nos, op, reason);
        }
    }
    if ("审批" == type) {
        if (("同意" == op || "拒绝" == op) && "1" != n[0]) {
            alert("请选择协议状态为待审批的协议进行此操作！");
            return;
        } else if ("重新提交" == op && !("2" == n[0] || "7" == n[0])) {
            alert("当前选择的协议状态无法进行此操作");
        } else if (confirm("确定执行此请求吗？")) {
            if ("拒绝" == op) {
                reason = promptCheck();
                if (reason == null) {
                    return;
                }
            }
            approveContract(nos, op, reason);
        }
    }
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

//协议审批
function approveContract(contractNos, operation, reason) {
    createLoadMask();
    var sourcePage = $("#sourcePage").val();
    var contractNos = JSON.stringify(contractNos);
    $.ajax({
        url: $("#contextPath").val() + "/approveContractInfo",
        data: {
            contractNos: contractNos,
            operation: operation,
            reason: reason,
            sourcePage: sourcePage
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            if (operation == "拒绝" || operation == "同意") {
                closeLoadMask();
                location.reload();
            } else {
                window.location = $("#contextPath").val() + "/contract?sourcePage=" + sourcePage;
            }
        }
    });
}


//协议确认
function confirmContract(contractNos, operation, reason) {
    createLoadMask();
    var sourcePage = $("#sourcePage").val();
    var contractNos = JSON.stringify(contractNos);
    $.ajax({
        url: $("#contextPath").val() + "/confirmContractInfo",
        data: {
            contractNos: contractNos,
            operation: operation,
            reason: reason,
            sourcePage: sourcePage
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            if (operation == "拒绝" || operation == "同意") {
                closeLoadMask();
                location.reload();
            } else {
                window.location = $("#contextPath").val() + "/contract?sourcePage=" + sourcePage;
            }

        }
    });
}

function createLoadMask() {
    $("#loadMask").show();
}

function closeLoadMask() {
    $("#loadMask").hide();
}

// 公司弹出层关闭
function closeDiv(obj) {
    $(obj).parent().parent().parent().hide();
    //增加恢复页面滚动条
    closeItemSearchDIV();
    closeLoadMask();
}
//取消跳回到全部协议 
function closeDiv1(obj) {
  window.location = $("#contextPath").val() + "/contract/" + "?sourcePage=" + obj;
}

//物品选择弹出层
function showItemSearchDIV() {
	var shopId ;
    createLoadMask();
    if ($("#sourcePage").val() == "seller") {
         shopId = $.trim($("#sellerSupplierId").val());
    }
    if ($("#sourcePage").val() == "buyer") {
         shopId = $.trim($("#supplierShopId").val());
    }
    if (shopId && shopId != "") {
        searchItem(shopId);
    } else {
        alert("请选择卖家信息");
        closeLoadMask();
    }
}

//搜索物品
function searchItem(shopId) {
    var keyword = $.trim($("#searchKeyWord").val());
    $.ajax({
        url: $("#contextPath").val() + "/contract/searchItem",
        data: {
            "Keyword": keyword,
            "shopId": shopId
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#itemSearchResult").html(data);
            showItemDIV();
            closeLoadMask();
        }
    });
}

function toItemPage(page) {
    var shopId = $("#itemSearchResult").find("[name='shopId']").val();
    var keyword = $.trim($("#itemSearchDIV").find("[name='keyword']").val());
    if ("请输入要查询的物品名称" == keyword) {
        keyword = null;
    }
    $.ajax({
        url: $("#contextPath").val() + "/contract/searchItem",
        data: {
            "shopId": shopId,
            "keyword": keyword,
            "page": page
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#itemSearchResult").html(data);
            showItemDIV();
        }
    });
}
function searchItemByKeyword() {
    var shopId = $("#itemSearchResult").find("[name='shopId']").val();
    var keyword = $("#itemSearchDIV").find("[name='keyword']").val();
    if (keyword && '' != $.trim(keyword)) {
        if ("请输入要查询的物品名称" == $.trim(keyword)) {
            keyword = null;
        }
        $.ajax({
            url: $("#contextPath").val() + "/contract/searchItem",
            data: {
                "shopId": shopId,
                "keyword": keyword
            },
            type: 'post',
            dataType: 'html',
            success: function (data) {
                $("#itemSearchResult").html(data);
            }
        });
    }

}

//展示物品弹出层
function showItemDIV() {
    $("#itemSearchDIV").show();
    document.body.style.overflow = "hidden";
}

function closeItemSearchDIV() {
    $("#itemSearchDIV").hide();
    document.body.style.overflow = "auto"; // 恢复页面滚动条
}


function empty(){
	   $("#itemList").html("");
	   createItemLi["items"] = new Array();
}

function display(){
	  var protocolType=$("#protocolTypeId").val();
	  if(protocolType=="2"){
		  $("#znumber").show();
		  $("#dj").hide();
		  $("#zcost").hide();
	  }else if(protocolType=="3"){
		  $("#zcost").show();
		  $("#dj").hide();
		  $("#znumber").hide();
	  }else{
		  $("#dj").show();
		  $("#zcost").hide();
		  $("#znumber").hide();
	  }
}


function createItemLi() {
    var a = $("input:checkbox[name='itemOrder']:checked");
    var protocolType=$("#protocolTypeId").val();
    var temp = "";
    $(a).each(function () {
        var obj = $(this).parent().parent();
        var itemName = obj.find("[name='itemName']").val();
        var itemPrice = obj.find("[name='itemPrice']").val();
        var itemSkuId = obj.find("[name='itemSkuId']").val();
        var attrSale = obj.find("[name='attrSale']").val();
        var proType = obj.find("[name='proType']").val();
        if(protocolType=="1"){
        temp += "<ul class='list_top hei_40' id='ul'>" +
        	"<input name='id' type='hidden' value='-1'>&nbsp;</input>" +
            "<li class='wid_150 over_ell font_cen' title='"+itemSkuId+"'>" + itemSkuId + "&nbsp;</li>" +
            "<li class='wid_150 over_ell font_cen' title='"+proType+"'>" + proType + "&nbsp;</li>" +
            "<input type='hidden' name='itemName' value='" + itemName + "'>" +
            "<input type='hidden' name='matCd' value='" + itemSkuId + "'>" +
            "<li class='wid_230 over_ell font_cen' title='"+itemName+"' style='text-align: center'>" + itemName + "&nbsp;</li>" +
            "<li class='wid_110 over_ell font_cen' title='"+attrSale+"'>" + attrSale + "&nbsp;</li>" +
            "<li class='wid_200 font_cen over_ell' style='text-align: center'>" +
            "<input id='matPrice' type='text' onkeyup='priceTrim(this)' maxlength='8' class='wid_50 input_Style2 hei_24' value='" + itemPrice + "' name='matPrice' />" +
            "<i class='mar_lr10 over_ell font_cen'>元</i></li>"+
            "<li class='wid_80 over_ell font_cen font_7a' onclick='removeItem(this)'>删除</li></ul>";
        }
        if(protocolType=="2"){
            temp += "<ul class='list_top hei_40' id='ul'>" +
            	"<input name='id' type='hidden' value='-1'>&nbsp;</input>" +
                "<li class='wid_120 over_ell font_cen' title='"+itemSkuId+"'>" + itemSkuId + "&nbsp;</li>" +
                "<li class='wid_160 over_ell font_cen' title='"+proType+"'>" + proType + "&nbsp;</li>" +
                "<input type='hidden' name='itemName' value='" + itemName + "'>" +
                "<input type='hidden' name='matCd' value='" + itemSkuId + "'>" +
                "<li class='wid_150 over_ell font_cen' title='"+itemName+"' style='text-align: center'>" + itemName + "&nbsp;</li>" +
                "<li class='wid_150 over_ell font_cen' title='"+attrSale+"'>" + attrSale + "&nbsp;</li>" +
                "<li class='wid_140 font_cen over_ell' style='text-align: center'>" +
                "<input id='matPrice' type='text' onkeyup='priceTrim(this)' maxlength='8' class='wid_50 input_Style2 hei_24' value='" + itemPrice + "' name='matPrice' />" +
                "<i class='mar_lr10 over_ell font_cen'>元</i></li>"+
                "<li class='wid_140 over_ell font_cen' style='text-align: center'>" +
           	 	"<input  type='text' onkeyup='onkeyup_product(this)' maxlength='8' class='wid_50 input_Style2 hei_24'   name='number' id='number' />"+
           		"<i class='mar_lr10 over_ell font_cen'>个</i></li>" +
                "<li class='wid_80 over_ell font_cen font_7a' onclick='removeItem(this)'>删除</li></ul>";
            }
        if(protocolType=="3"){
            temp += "<ul class='list_top hei_40' id='ul'>" +
            	"<input name='id' type='hidden' value='-1'>&nbsp;</input>" +
                "<li class='wid_120 over_ell font_cen' title='"+itemSkuId+"'>" + itemSkuId + "&nbsp;</li>" +
                "<li class='wid_160 over_ell font_cen' title='"+proType+"'>" + proType + "&nbsp;</li>" +
                "<input type='hidden' name='itemName' value='" + itemName + "'>" +
                "<input type='hidden' name='matCd' value='" + itemSkuId + "'>" +
                "<li class='wid_150 over_ell font_cen' title='"+itemName+"' style='text-align: center'>" + itemName + "&nbsp;</li>" +
                "<li class='wid_150 over_ell font_cen' title='"+attrSale+"'>" + attrSale + "&nbsp;</li>" +
                "<li class='wid_140 font_cen over_ell' style='text-align: center'>" +
                "<input id='matPrice' type='text' onkeyup='priceTrim(this)' maxlength='8' class='wid_50 input_Style2 hei_24' value='" + itemPrice + "' name='matPrice' />" +
                "<i class='mar_lr10 over_ell font_cen'>元</i></li>"+
	           	"<li class='wid_140 over_ell font_cen' style='text-align: center'>" +
	           	"<input  type='text' onkeyup='priceTrim(this)' maxlength='8' class='wid_50 input_Style2 hei_24'   name='cost' id='cost' />" +
	           	"<i class='mar_lr10 over_ell font_cen'>元</i></li>" +
                "<li class='wid_80 over_ell font_cen font_7a' onclick='removeItem(this)'>删除</li></ul>";
            }
    });
    $("#itemList").append(temp);
    if ($("#itemList ul")) {
        $("#itemList ul").each(function () {
            createItemLi.items.push($(this).find("[name='matCd']").val());
        });
    }
    closeItemSearchDIV();
    closeLoadMask();
}

function checkIsChosen(e) {
    //var skuId = $("#itemList [name='matCd']").val();
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

function removeItem(e) {
    if (confirm("确定删除？")) {
        var skuId = $(e).parent().find("[name='matCd']").val();
        var matId = $(e).parent().find("[name='id']").val();
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
        //console.log(createItemLi.items);
        $(e).parent().remove();
    }
}


//显示审批人列表
function showApproveList(e) {
    //console.debug($(e));
    if ($(e).is(":checked")) {
        $("#approveList").removeAttr("disabled");
        $("#approveBy").attr("style", "display:block");
    } else {
        $("#approveList").attr("disabled", "disabled");
        $("#approveBy").attr("style", "display:none");
    }
}

function contractOrder(sp) {

    //console.debug($("input:checkbox[name='contractId']:checked"));

    var a = $("input:checkbox[name='contractId']:checked");

    if (a.length == 0) {
        alert("请选择一个协议");
        return;
    }
    if (a.length > 1) {
        alert("只能选择一个协议进行下单");
        return;
    }
    var status = $(a[0]).parent().parent().find("[name='status']").val();
    if (!(6 == status || 8 == status)) {
        alert("该协议无法下单！");
        return;
    }
    var contractId = $(a[0]).val();
    window.location = $("#contextPath").val() + "/contract/contractOrder" + "?contractId=" + contractId + "&sourcePage=" + sp;
}
//下单方法
function orderItem() {
    if (confirm("确定要下单吗？")) {
        var itemList = $("#itemList ul").find("input:checkbox[name='contractId']:checked");

        var contractBeginDate = new Date($("#orderContract").find("[name='contractBeginDate']").val());
        contractBeginDate.setHours(0, 0, 0, 0);
        var contractEndDate = new Date($("#orderContract").find("[name='contractEndDate']").val());
        contractEndDate.setHours(23, 59, 59, 0)
        var contractNo = $("#orderContract").find("[name='contractNo']").val();
        var nowDate = new Date();
        var access = true;
        $("[name='quantity']").each(function () {
        	//当被选择时在校验
        	if($(this).parent().parent().find("input:checkbox[name='contractId']")[0].checked){
        		var quantity = $(this).val();
            	var cost = $(this).parent().find("[name='cost']").val();
            	var zcost = $(this).parent().find("[name='countpay']").val();
            	var matPrice = $(this).parent().find("[name='matPriceec']").val();
            	var accountsnum=$(this).parent().find("input[name='number']").val();//总数量
            	var zquantity=$(this).parent().find("input[name='countNumber']").val();//已购买数量
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
            		if(costnumber<0){
                		alert("已超出允许购买总金额。");
                		access = false; //跳出循环
                		return false;
                	}
            	}else{
            		var costnumber=cost-matPrice*quantity-zcost;
            		if(costnumber<0){
                		alert("已超出允许购买总金额。");
                		access = false; //跳出循环
                		return false;
                	}
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
        if ($("#itemList").find("[name='quantity']").val() < 1) {
            alert("请输入正确的下单数量");
            return;
        }
        debugger
        if (nowDate - contractBeginDate >= 0 && nowDate - contractEndDate <= 0) {
            var temp = new Array();
            for (var i = 0, len = itemList.length; i < len; i++) {
                temp.push(getContractMats($(itemList[i]).parent().parent()));
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
            alert("当期日期不在协议日期内，无法下单");
            return;
        }
    }
}

function getContractMats(obj) {
    var dom = $(obj);
    var matId = dom.find("[name='matId']").val();
    var quantity = dom.find("[name='quantity']").val();

    var contractMatDTO = {};
    contractMatDTO.matId = matId;
    contractMatDTO.quantity = quantity;
    return contractMatDTO;
}

function getContractOrder() {
    var contractNo = $("#orderContractNo").val();
    var sourcePage = $("#sourcePage").val();
    //console.log(contractNo);
    if (contractNo == "") {
        return;
    }
    var page = 1;
    $.ajax({
        url: $("#contextPath").val() + "/contract/getContractOrder",
        data: {
            "contractNo": contractNo,
            "sourcePage": sourcePage,
            "page": page
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#orderDetail").html(data);
        }
    });

}   

function accounts(o){
	var dom = $(o);
	var accountsnum=dom.parent().find("input[name='number']").val();//总数量
	var protocolType=$("#protocolType").val(); //协议类型
	var quantity=$(o).val();//购买数量
	var cost=dom.parent().find("input[name='cost']").val();//商品总价值
	var matPrice=dom.parent().find("input[name='matPriceec']").val();//商品单价 
	var zquantity=dom.parent().find("input[name='countNumber']").val();//已购买数量
	var zcost=dom.parent().find("input[name='countpay']").val();//已购买金额
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
		if(num>0){
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
