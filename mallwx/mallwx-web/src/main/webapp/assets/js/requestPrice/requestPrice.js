/**
 * 询价相关js
 */
$(document).ready(function () {

    //询价前台验证
    $(document).on("click", "#button_save", function (e) {
        var inquiryNo = $("input[name='inquiryNo']").val();
        if (inquiryNo == null || inquiryNo == "") {
            $(".errorMsg").html("询价编码必填！");
            return;
        }
        ;
        var inquiryName = $("input[name='inquiryName']").val();
        if (inquiryName == null || inquiryName == "") {
            $(".errorMsg").html("询价名称必填！");
            return;
        }
        ;
        var beginDate = $("input[name='beginDate1']").val();
        if (beginDate == null || beginDate == "") {
            $(".errorMsg").html("询价日期必填！");
            return;
        }
        ;
        var endDate = $("input[name='endDate1']").val();
        if (endDate == null || endDate == "") {
            $(".errorMsg").html("截至报价日期必填！");
            return;
        }
        ;
        var deliveryDate = $("input[name='deliveryDate1']").val();
        if (deliveryDate == null || deliveryDate == "") {
            $(".errorMsg").html("交货日期必填！");
            return;
        }
        ;
        var printerId = $("input[name='printerId']").val();
        if (printerId == null || printerId == "") {
            $(".errorMsg").html("采购方必填！");
            return;
        }
        ;
        $("input[name='nums']").each(function () {
            if ($(this).val() == "") {
                $(".errorMsg").html("数量必填！");
                throw "数量必填！";
            }
            if ($(this).val() == "0") {
                $(".errorMsg").html("数量必需大于0！");
                throw "数量必需大于0！";
            }
        });
        $("input[name='deliveryDates']").each(function () {
            if ($(this).val() == "") {
                $(".errorMsg").html("明细中交货时间必填！");
                throw "明细中交货时间必填！";
            }
        });
        //询价截至时间、交货时间必须大于询价时间
        var date=beginDate.substr(0, 4)+"-"+beginDate.substr(5, 2)+"-"+beginDate.substr(8, 2); 
        
        var start = new Date(date);
        var end1 = new Date(endDate);
        var end2 = new Date(deliveryDate);
        if (start > end1) {
            $(".errorMsg").html("询价截止时间必须大于询价时间！");
            return;
        }
        if (start > end2) {
            $(".errorMsg").html("交货时间必须大于询价时间！");
            return;
        }
        if ($("input[name='nums']").length < 1) {
            $(".errorMsg").html("请至少选择一个物品进行询价！");
            return;
        }
        //跑后台创建询价信息
        $(".errorMsg").html("");
        addLoadMask();
        $.ajax({
            type: "POST",
            dataType: "html",
            url: $("#inquiryForm").prop("action"),
            data: $("#inquiryForm").serializeArray(),
            success: function (data) {
                removeLoadMask();
                toast(data);
                window.location = "requestPrice";
            }
        });
    });

    //新增一行物品的tr
    /*$(document).on("click","#add",function(){
     var trHTML = "<tr class='hei_32'><td class='wid_30'></td><td class='wid_30'></td><td class='wid_11'>箱</td><td class='wid_15'><span class='delete'>删除</span></td></tr>";
     $("#add").before(trHTML);
     });*/

    //删除新增加的tr
    $(document).on("click", ".delete", function (e) {
    	//将这条记录的物料id放到隐藏域中，
    	var doc = $(this);
        kltconfirm("确定删除？",function(){
        	var matCd = doc.parent().parent().parent().find("input[name='detailId']").val();
            var flag = doc.parent().parent().parent().find("input[name='status']").val();
            var tempVar = $("#deleteIds").val();
            if (flag != "i") {
                if (tempVar == "") {
                    tempVar = matCd;
                } else {
                    tempVar = tempVar + "," + matCd;
                }
            }
            $("#deleteIds").val(tempVar);
            doc.parent().parent().parent().remove();
        });
    });

    //询价前台验证
    $(document).on("click", "#button_update", function (e) {
        var inquiryNo = $("input[name='inquiryNo']").val();
        if (inquiryNo == null || inquiryNo == "") {
            $(".errorMsg").html("询价编码必填！");
            return;
        }
        ;
        var inquiryName = $("input[name='inquiryName']").val();
        if (inquiryName == null || inquiryName == "") {
            $(".errorMsg").html("询价名称必填！");
            return;
        }
        ;
        var beginDate = $("input[name='beginDate1']").val();
        if (beginDate == null || beginDate == "") {
            $(".errorMsg").html("询价日期必填！");
            return;
        }
        ;
        var endDate = $("input[name='endDate1']").val();
        if (endDate == null || endDate == "") {
            $(".errorMsg").html("截至报价日期必填！");
            return;
        }
        ;
        var deliveryDate = $("input[name='deliveryDate1']").val();
        if (deliveryDate == null || deliveryDate == "") {
            $(".errorMsg").html("交货日期必填！");
            return;
        }
        ;
        var printerId = $("input[name='printerId']").val();
        if (printerId == null || printerId == "") {
            $(".errorMsg").html("采购方必填！");
            return;
        }
        ;
        //询价截至时间、交货时间必须大于询价时间
        var start = new Date(beginDate);
        var end1 = new Date(endDate);
        var end2 = new Date(deliveryDate);
        if (start > end1) {
            $(".errorMsg").html("询价截止时间必须大于询价时间！");
            return;
        }
        if (start > end2) {
            $(".errorMsg").html("交货时间必须大于询价时间！");
            return;
        }
        if ($("input[name='nums']").length < 1) {
            $(".errorMsg").html("请至少选择一个物品进行询价！");
            return;
        }
        $("input[name='nums']").each(function () {
            if ($(this).val() == "") {
                $(".errorMsg").html("数量必填！");
                throw "数量必填！";
            }
            if ($(this).val() == "0") {
                $(".errorMsg").html("数量必需大于0！");
                throw "数量必需大于0！";
            }
        });
        //跑后台创建询价信息
        $(".errorMsg").html("");
        addLoadMask();
        $.ajax({
            type: "POST",
            dataType: "html",
            url: $("#inquiryForm").prop("action"),
            data: $("#inquiryForm").serializeArray(),
            success: function (data) {
                removeLoadMask();
                toast(data);
                window.location = "requestPrice";
            }
        });

    });


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

    $("input[name='price']").keydown(function (e) {

        numberformat(this);
    });


    //卖家设置询价价格前台验证
    $("#button_update_response").click(function () {
        var d = new Date();
        var vYear = d.getFullYear()
        var vMon = d.getMonth() + 1
        var vDay = d.getDate()
        var s = vYear + "-" + (vMon < 10 ? "0" + vMon : vMon) + "-" + (vDay < 10 ? "0" + vDay : vDay);
        var inquiryNo = $("#inquiryNo").val();
        var beginDate = $("#beginDate1").val();
        //询价截至时间、交货时间必须大于询价时间
        var start = new Date(beginDate);
        //循环验证，每条明细的价格必填，有效时间必须大于询价时间，截至时间必须大于有效时间(始)
        $("input[name='price']").each(function (e) {
            var price = $(this).val();
            var detailstartDate = $(this).parent().parent().find("input[name='detailstartDate']").val();
            var detailendDate = $(this).parent().parent().find("input[name='detailendDate']").val();
            var itemNames = $(this).parent().parent().parent().find("input[name='itemNames']").val();
            if (price != "") {
                if (detailstartDate == "") {
                    $(".errorMsg").html("物品【" + itemNames + "】开始时间必填!");
                    throw "物品【" + itemNames + "】开始时间必填!";
                }
                if (detailendDate == "") {
                    $(".errorMsg").html("物品【" + itemNames + "】结束时间必填!");
                    throw "物品【" + itemNames + "】结束时间必填!";
                }
                if (new Date(detailstartDate) < new Date(s)) {
                    $(".errorMsg").html("物品【" + itemNames + "】开始时间不能小于今天!");
                    throw "物品【" + itemNames + "】结束时间不能小于今天!";
                }
                if (new Date(detailstartDate) > new Date(detailendDate)) {
                    $(".errorMsg").html("物品【" + itemNames + "】结束时间不能小于开始时间!");
                    throw "物品【" + itemNames + "】结束时间不能小于开始时间!";
                }
            }
        });
        $("input[name='nums']").each(function (e) {
            if ($(this).val() == "") {
                $(".errorMsg").html("数量必填!");
                throw "数量必填!";
            }
        });

        //跑后台创建询价信息
        $(".errorMsg").html("");
        addLoadMask();
        $.ajax({
            type: "post",
            dataType: "html",
            url: $("#inquiryForm").prop("action"),
            data: $("#inquiryForm").serializeArray(),
            success: function (data) {
                removeLoadMask();
                toast(data);
                window.location = "responsePrice";
            },
            error: function (data) {
                removeLoadMask();
                toast(data);
            }
        });

    });

});

function number() {
    var char = String.fromCharCode(event.keyCode)
    var re = /[0-9]/g
    event.returnValue = char.match(re) != null ? true : false
}

function filterInput() {
    if (event.type.indexOf("key") != -1) {
        var re = /37|38|39|40/g
        if (event.keyCode.toString().match(re)) return false
    }
    event.srcElement.value = event.srcElement.value.replace(/[^0-9]/g, "")
}

function filterPaste() {
    var oTR = this.document.selection.createRange()
    var text = window.clipboardData.getData("text")
    oTR.text = text.replace(/[^0-9]/g, "")
}

//买家编辑询价信息
function updateInquiry(inquiryNo, status) {
    //询价状态查询验证
    if (status != "1") {
        toast("只有状态为待发布的才能编辑！");
        return;
    }
    window.location = "updateInquiry" + "?inquiryNo=" + inquiryNo;
}

/**查看询价详细信息
 * @param inquiryNo
 */
function lookInquiryInfo(inquiryNo, flag) {
	addLoadMask();
    window.location = "lookInquiry" + "?inquiryNo=" + inquiryNo + "&flag=" + flag;
    removeLoadMask();
}

function goBackPage(url_str) {
    window.location = url_str;
}

function deleteInquiry(inquiryNo, status) {
    //询价状态查询验证
    if (status != "1") {
        toast("只有状态为待发布的才能删除！");
        return;
    }
    
    kltconfirm("是否确认删除询价信息？",function(){
    	 addLoadMask();
    	    //跑后台删除询价信息
    	    $.ajax({
    	        type: "POST",
    	        dataType: "html",
    	        url: $("#contextPath").val() + "/requestPriceJavaController/deleteInquiry",
    	        data: {
    	            inquiryNo: inquiryNo
    	        },
    	        success: function (data) {
    	            removeLoadMask();
    	            toast(data);
    	            window.location = "requestPrice";
    	        }
    	    });
	});
    
   
}
//询价
function commitnquiry(inquiryNo, status) {
    //询价状态查询验证
    if (status != "1") {
        toast("只有状态为待发布的才能发布！");
        return;
    }
    kltconfirm("是否确认进行询价？",function(){
    	addLoadMask();
        //跑后台提交询价信息
        $.ajax({
            type: "POST",
            dataType: "html",
            url: $("#contextPath").val() + "/requestPriceJavaController/commitInquiry",
            data: {
                inquiryNo: inquiryNo
            },
            success: function (data) {
                toast(data);
                window.location = "requestPrice";
                removeLoadMask();
            }
        });
    });
    return;
}

//供应商选择
function getCompany() {
    //var uType=$("#userType").val();
    $.ajax({
        url: $("#contextPath").val() + "/contract/getCompanyNameList",
        data: {
            "uType": '3'
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#partBDetail").html(data);
            showDiv();
        }

    });
    // 以下部分使整个页面至灰不可点击
    var procbg = document.createElement("div"); // 首先创建一个div
    procbg.setAttribute("id", "mybg"); // 定义该div的id
    procbg.style.background = "#000000";
    procbg.style.width = "100%";
    procbg.style.height = "100%";
    procbg.style.position = "fixed";
    procbg.style.top = "0";
    procbg.style.left = "0";
    procbg.style.zIndex = "500";
    procbg.style.opacity = "0.6";
    procbg.style.filter = "Alpha(opacity=70)";
    // 背景层加入页面
    document.body.appendChild(procbg);
    document.body.style.overflow = "hidden"; // 取消滚动条

}

function showDiv() {
    var Idiv = document.getElementById("Idiv");
    var mou_head = document.getElementById('mou_head');
    Idiv.style.height = document.documentElement.clientHeight * 0.75 + "px";
    Idiv.style.display = "block";
    // 以下部分要将弹出层居中显示
    Idiv.style.top = (document.documentElement.clientHeight - Idiv.clientHeight)
        / 2 + document.documentElement.scrollTop - 50 + "px";


}

function closeDiv() // 关闭弹出层
{
    var Idiv = document.getElementById("Idiv");
    Idiv.style.display = "none";
    document.body.style.overflow = "auto"; // 恢复页面滚动条
    var body = document.getElementsByTagName("body");
    var mybg = document.getElementById("mybg");
    body[0].removeChild(mybg);
}

function putCompanyValue(obj) {
    var companyName = $(obj).find("[name='companyName']").val();
    var companyId = $(obj).find("[name='companyId']").val();
    $("input[name='supplierId']").each(function (i, n) {
        $(this).attr('value', companyId);
    });
    $("input[name='supplierName']").each(function (i, n) {
        $(this).attr('value', companyName);
    });
    closeDiv();


}

function putItemValue(obj) {
    var itemName = $(obj).find("[name='itemName']").val();
    var itemId = $(obj).find("[name='itemId']").val();
    var cName = $(obj).find("[name='cName']").val();
    $("input[name='itemId']").each(function (i, n) {
        $(this).attr('value', itemId);
    });
    $("input[name='itemName']").each(function (i, n) {
        $(this).attr('value', itemName);
    });
    var trHTML = "<tr class='hei_32'><input type='hidden' name='detailId' value=''/><input type='hidden' name='itemIds' value='" + itemId + "'/><input type='hidden' name='status' value='i'/><td class='wid_30'>" + itemName + "</td><td class='wid_11'><input type='text' onkeypress='number()' onkeyup='filterInput()' onchange='filterInput()' onbeforepaste='filterPaste()' onpaste='return false' name='nums'/></td><td class='wid_15'><span class='delete'>删除</span></td></tr>";
    $("#add").before(trHTML);
    closeDiv();

}

function itemSelect() {
    var cid = $("#cid").val();
    $.ajax({
        url: $("#contextPath").val() + "/requestPriceController/getItemList",
        data: {
            "uType": '3',
            cid: cid
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#partBDetail").html(data);
            showDiv();
        }

    });
    // 以下部分使整个页面至灰不可点击
    var procbg = document.createElement("div"); // 首先创建一个div
    procbg.setAttribute("id", "mybg"); // 定义该div的id
    procbg.style.background = "#000000";
    procbg.style.width = "100%";
    procbg.style.height = "100%";
    procbg.style.position = "fixed";
    procbg.style.top = "0";
    procbg.style.left = "0";
    procbg.style.zIndex = "500";
    procbg.style.opacity = "0.6";
    procbg.style.filter = "Alpha(opacity=70)";
    // 背景层加入页面
    document.body.appendChild(procbg);
    document.body.style.overflow = "hidden"; // 取消滚动条

    //window.open('productsListPrice','Derek','height=1800,width=600,status=yes,toolbar=yes,menubar=no,location=yes');
}

function showItemDiv(divId) {
    $("#" + divId).toggle();
}

function reset(){
	 $("#supplierName").val('');
	 $("#itemName").val('');
	 $("#inquiryNo").val('');
}

function queryInquiry(num, flag) {
    var supplierName = $("#supplierName").val();
    var itemName = $("#itemName").val();
    var inquiryNo = $("#inquiryNo").val();
    var supplierId = $("#supplierId").val();
    //跑后台创建询价信息
    $(".errorMsg").html("");
    addLoadMask();
    $.ajax({
        type: "POST",
        dataType: "html",
        url: $("#contextPath").val() + "/requestPriceController/queryInquiry",
        data: {
            supplierName: supplierName,
            itemName: itemName,
            inquiryNo: inquiryNo,
            num: num,
            flag: flag,
            supplierId: supplierId
        },
        success: function (data) {
        	removeLoadMask();
            $("#queryDiv" + num).html(data);
        }
    });

}


//卖家确认询价
function commitResponseInquiry(inquiryNo, status, endDate) {
    //询价状态查询验证
    if (status != "3") {
        toast("只有状态为已报价的才能确认询价！");
        return;
    }
    var now = new Date();
    if (new Date(now) > new Date(endDate)) {
        toast("只有在截至报价时间之前的才可以报价确认！");
        return;
    }
    if (!confirm("是否确认报价？")) {
        return;
    }
    addLoadMask();
    //跑后台修改询价状态为4
    $.ajax({
        type: "POST",
        dataType: "html",
        url: $("#contextPath").val() + "/requestPriceJavaController/commitResponseInquiry",
        data: {
            inquiryNo: inquiryNo
        },
        success: function (data) {
            removeLoadMask();
            toast(data);
            window.location = "responsePrice";
        }
    });
}

//卖家编辑报价信息
function updateResponseInquiry(inquiryNo, status, endDate) {
    //询价状态查询验证
    if (status != "2") {
        toast("只有状态为报价中的才能报价编辑！");
        return;
    }
    var now = new Date();
    if (new Date() >  new Date(endDate)) {
        toast("只有在截至报价时间之前的才可以报价确认！");
        return;
    }
    window.location = "updateResponseInquiry" + "?inquiryNo=" + inquiryNo;
}

//买家重新询价
function commitnquiryRe(inquiryNo, status) {
    //询价状态查询验证
    if (status != "2" && status != "3") {
        toast("只有状态为报价中、已确认的才能重新询价！");
        return;
    }
    
    kltconfirm("是否重新询价？",function(){
    	addLoadMask();
        //跑后台修改询价状态为5
        $.ajax({
            type: "POST",
            dataType: "html",
            url: $("#contextPath").val() + "/requestPriceJavaController/commitnquiryRe",
            data: {
                inquiryNo: inquiryNo
            },
            success: function (data) {
                removeLoadMask();
                toast(data);
                window.location = "requestPrice";
            }
        });
	});
}

//买家接收价格
function commitRequestInquiry(inquiryNo, status, checkName) {
    var d = new Date();
    var vYear = d.getFullYear()
    var vMon = d.getMonth() + 1
    var vDay = d.getDate()
    var s = vYear + "-" + (vMon < 10 ? "0" + vMon : vMon) + "-" + (vDay < 10 ? "0" + vDay : vDay);
    //询价状态查询验证
    if (status != "2") {
        toast("只有状态为报价中的才能接收价格！");
        return;
    }
    var temp = $("input[name='" + checkName + "']:checked").length;
    if (temp == 0) {
        toast("请至少选择一个物品接收询价，或等待卖家报价后接收价格！");
        return;
    }
    $("#" + inquiryNo).find("input[type='checkbox']:checked").each(function (e) {
        if ($(this).val() != "null") {
            /*//判断物品是否在有效时间内
             var detailBeginDate  =  $(this).parent().parent().find("input[name='detailBeginDate']").val();
             var detailEndDate  =  $(this).parent().parent().find("input[name='detailEndDate']").val();
             if(new Date(s) < new Date(detailBeginDate) || new Date(s) > new Date(detailEndDate)){
             alert("物品【"+detailMatDesc+"】不在询价有效期内，无法接收!");
             throw "物品【"+detailMatDesc+"】不在询价有效期内，无法接收!";
             }*/
            var detailMatDesc = $(this).parent().parent().find("input[name='detailMatDesc']").val();
            if ($(this).parent().parent().find("input[name='ifPrice']").val() != "1") {
                toast("物品【" + detailMatDesc + "】还未报价，无法接收!");
                throw "物品【" + detailMatDesc + "】还未报价，无法接收!";
            }
        }
    });
    kltconfirm("是否接收价格？",function(){
    	var detailIds = "";
        $("input:checkbox[name='" + checkName + "']:checked").each(function () {
            if (detailIds == "") {
                detailIds = $(this).val();
            } else {
                detailIds = detailIds + "," +  $(this).val();
            }
        });
        addLoadMask();
        //跑后台修改询价状态为5
        $.ajax({
            type: "POST",
            dataType: "html",
            url: $("#contextPath").val() + "/requestPriceJavaController/commitRequestInquiry",
            data: {
                inquiryNo: inquiryNo,
                detailIds: detailIds
            },
            success: function (data) {
                removeLoadMask();
                toast(data);
                window.location = "requestPrice";
            }
        });
    });
}
//显示数量输入框
function showNumInput(inquiryNo) {
	if($("input[value='" + inquiryNo + "']").is(":checked")){
		$("p[name='" + inquiryNo + "']").show();
	}else{
		$("p[name='" + inquiryNo + "']").hide();
	}
}
var productList = new Array();
//下单
function createOrderInfo(inquiryNo, status, obj) {
    var d = new Date();
    var vYear = d.getFullYear()
    var vMon = d.getMonth() + 1
    var vDay = d.getDate()
    var s = vYear + "-" + (vMon < 10 ? "0" + vMon : vMon) + "-" + (vDay < 10 ? "0" + vDay : vDay);
    /*if($("#"+inquiryNo).find("input[type='checkbox']:checked").length < 1){
     toast("请等待卖家报价后下单至少选择一条物品进行下单！");
     return;
     }*/
    if ($("input:checkbox[name='" + inquiryNo + "']:checked").length < 1) {
        toast("请等待卖家报价后下单至少选择一条物品进行下单！");
        return;
    }
    var ids = "";
    var nums = "";
    $("input:checkbox[name='" + inquiryNo + "']:checked").each(function (e) {
        if ($(this).val() != "null") {
            //判断物品是否在有效时间内
            var detailBeginDate = $(this).parent().parent().find("input[name='detailBeginDate']").val();
            var detailEndDate = $(this).parent().parent().find("input[name='detailEndDate']").val();
            var detailMatDesc = $(this).parent().parent().find("input[name='detailMatDesc']").val()
            var detailStatus = $(this).parent().parent().find("input[name='detailStatus']").val();
            if (new Date(s) < new Date(detailBeginDate) || new Date(s) > new Date(detailEndDate)) {
                toast("物品【" + detailMatDesc + "】不在询价有效期内，无法下单!");
                throw "物品【" + detailMatDesc + "】不在询价有效期内，无法下单!";
            }

            if (detailStatus != "" && detailStatus != "1" && detailStatus != "null" && detailStatus != "0") {
                toast("物品【" + detailMatDesc + "】价格未接收，无法下单!");
                throw "物品【" + detailMatDesc + "】价格未接收，无法下单!";
            }
            if ($(this).parent().parent().find("input[name='number']").val() == "") {
                toast("请输入下单数量！");
                throw "请输入下单数量!";
            }
            if ($(this).parent().parent().find("input[name='ifPrice']").val() != "1") {
                toast("请等待卖家报价后再下单！");
                throw "请等待卖家报价后再下单!";
            }
            if (nums == "") {
                nums = $(this).parent().parent().find("input[name='number']").val();
                ids = $(this).parent().parent().find("input[name='detailid']").val();
            } else {
                nums = nums + "," + $(this).parent().parent().find("input[name='number']").val();
                ids = ids + "," + $(this).parent().parent().find("input[name='detailid']").val();
            }
        }
    });
    if (ids == "") {
        toast("请至少选择一条物品进行下单！");
        return;
    }
    //如果询价状态为2，在此时改为3
    var detailIds = "";
    $("input:checkbox[name='" + inquiryNo + "']:checked").each(function () {
        if (detailIds == "") {
            detailIds = $(this).val()
        } else {
            detailIds = "," + detailIds;
        }
    });
    $("#requestPriceForm").find("[name='contractNo']").val(inquiryNo);
    addLoadMask();
    //跑后台修改询价状态为3
    $.ajax({
        type: "POST",
        dataType: "html",
        url: $("#contextPath").val() + "/requestPriceJavaController/createOrderInfo",
        data: {
            inquiryNo: inquiryNo,
            detailIds: detailIds,
            nums: nums,
            ids: ids,
            status: status
        },
        success: function (data) {
            document.getElementById("requestPriceForm").submit();
        },
        error: function (data) {
            removeLoadMask();
            toast(data);
            window.location = "requestPrice";
        }
    });

}

function checkAll(inquiryNo, obj) {
    if (obj.checked) {
        $("input[name='" + inquiryNo + "']").each(function (e) {
            $(this).prop("checked", true);
            $(this).parent().parent().find("input[name='number']").parent().show();
        });
    } else {
        $("input[name='" + inquiryNo + "']").each(function (e) {
            $(this).prop("checked", false);
            $(this).parent().parent().find("input[name='number']").parent().hide();
        });
    }
}

//物品选择弹出层
function showItemSearchDIV() {
    addLoadMask();
    searchItem();
}

//搜索物品
function searchItem() {
    var keyword = $.trim($("#searchKeyWord").val());
    $.ajax({
        url: $("#contextPath").val() + "/requestPriceController/getItemList",
        data: {
            keyword: keyword
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#itemSearchResult").html(data);
        	var totalPage = $("#totalPage").val();
            if (totalPage <= 5) {
                $("#pullUpLabel").html("没有更多信息了");
            } else {
                $("#pullUpLabel").html("上拉加载更多...");
            }
            showItemsDIV();
            removeLoadMask();
        }
    });
}

var currentPage = 0;
var pageSize = 5;
var leimu3Id;
var canRefresh = true;
var myScroll;
$(function () {
    var cid = $("#cid").val();
    loaded();
    canRefresh = false;
    $("#itemSearchResult").html('');
    var data = {};
    data.page = 1;
    data.keyword = $.trim($("#searchKeyWord").val());
    data.pageSize = 5;
    data.cid = cid;
    leimu3Id = '';
    loadProducts(data);
});

function loadProducts(data) {
    $.ajax({
        url: $("#contextPath").val() + "/requestPriceController/getItemList",
        type: 'post',
        data: data,
        dataType: 'html',
        success: function (res) {
        	currentPage = page;
        	$("#itemSearchResult").append(res);
        	var totalPage = $("#totalPage").val();
            if (totalPage <= 5) {
                $("#pullUpLabel").html("没有更多信息了");
            } else {
                $("#pullUpLabel").html("上拉加载更多...");
            }
            canRefresh = true;
            $("#pullDownLabel").html("下拉刷新");
            setTimeout(function () {
                myScroll.refresh();
            }, 200);
        },
        error: function () {
            alert("网络错误");
        }
    });
}

//下拉刷新
function refresh() {
    if (!canRefresh) {
        return;
    }
    var cid = $("#cid").val();
    canRefresh = false;
    var data = {};
    data.cid = cid;
    data.page = 1;
    data.keyword = $.trim($("#searchKeyWord").val());
    data.pageSize = 5;
    $("#itemSearchResult").html('');
    loadProducts(data);
}


function loaded() {
    refreshScroll();
}


function changeKeyWord() {
    canRefresh = false;
    $("#itemSearchResult").html('');
    var data = {};
    data.cid = leimu3Id;
    data.page = 1;
    data.keyword = $.trim($("#searchKeyWord").val());
    pageSize = 5;
    loadProducts(data);
}


//下一页
function nextPage() {
    if (!canRefresh) {
        return;
    }
    $("#pullUpLabel").html("加载中……");
    var cid = $("#cid").val();
    canRefresh = false;
    var data = {};
    data.cid = cid;
    data.page = currentPage + 1;
    data.keyword = $.trim($("#searchKeyWord").val());
    data.pageSize = 5;
    loadProducts(data);
}

function refreshScroll() {
    myScroll = new iScroll(
        'wrapper',
        {
            vScrollbar: true,
            hscrollbar: false,
            topOffset: 40,
            onBeforeScrollStart: function (e) {//激活input、select 、textare的点击事件
                var nodeType = e.explicitOriginalTarget ? e.explicitOriginalTarget.nodeName.toLowerCase() : (e.target ? e.target.nodeName.toLowerCase() : '');
                if (nodeType != 'select' && nodeType != 'option' && nodeType != 'input' && nodeType != 'textarea') {
                    e.preventDefault();
                }
            },
            onScrollStart: function () {
            	//活动开始的时候记录当前的y
                this.___scrollStartY___ = this.y;
            },

            onScrollMove: function () {
                if (this.y > 50) {
                    $("#pullDownLabel").html("松开刷新");
                }
                if ((this.y < this.maxScrollY - 50) && (this.___scrollStartY___ - this.y) > 50) {
                    $("#pullUpLabel").html("松开继续加载");
                }
            },
            onTouchEnd: function () {
                if (this.y > 50) {//开始刷新数据
                    setTimeout(refresh(), 100);
                }
                if ((this.y < this.maxScrollY - 50 ) && (this.___scrollStartY___ - this.y) > 50) {
                	//加载下一页
                    setTimeout(nextPage(), 100);
                }
            }

        });
}

//展示物品弹出层
function showItemsDIV() {
    var Idiv = document.getElementById("itemSearchDIV");
    Idiv.style.display = "block";
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

//创建加载层
function createBlackBack() {
    var divText = "<div style='width:50%;height:20%;margin:auto;padding-top:40%;text-align:center;color:#ffffff;z-index:501;'>" +
        "玩命加载中........." +
        "</div>";
    var procbg = document.createElement("div"); // 首先创建一个div
    procbg.setAttribute("id", "mybg"); // 定义该div的id
    procbg.style.background = "#000000";
    procbg.style.width = "100%";
    procbg.style.height = "100%";
    procbg.style.position = "fixed";
    procbg.style.top = "0";
    procbg.style.left = "0";
    procbg.style.zIndex = "70";
    procbg.style.opacity = "0.6";
    procbg.style.filter = "Alpha(opacity=70)";
    $(procbg).append(divText);
    // 背景层加入页面
    document.body.appendChild(procbg);
    document.body.style.overflow = "hidden"; // 取消滚动条
}

//关闭加载层
function closeBlackBack() {
    var body = document.getElementsByTagName("body");
    var mybg = document.getElementById("mybg");
    if(mybg){
    	body[0].removeChild(mybg);
    }
}

function closeItemSearchDIV() {
    var Idiv = document.getElementById("itemSearchDIV");
    Idiv.style.display = "none";
    document.body.style.overflow = "auto"; // 恢复页面滚动条
    closeBlackBack();

}

function toggleMenu() {
    $("#navigation").toggle();
}


function createItemLi() {
    var a = $("input:checkbox[name='itemOrder']:checked");
    var temp = "";
    var tempDate = $("#deliveryDate1").val();
    $(a).each(function () {
        var obj = $(this).parent().parent();
        var itemName = obj.find("[name='itemName']").val();
        var itemPrice = obj.find("[name='itemPrice']").val();
        var itemId = obj.find("[name='itemId']").val();
        var shopIds = obj.find("[name='shopIds']").val();
        var shopNames = obj.find("[name='shopNames']").val();
        //取到商品的id 李伟龙增加
        var itemIds = obj.find("[name='itemIds']").val();
        
        if (shopIds == "" || itemIds == "") {
            toast("请选择至少一条物品进行询价！");
            return;
        }
        
        if (shopIds == "") {
            toast("物品[" + itemName + "]请选择供应商！");
            throw "物品[" + itemName + "]请选择供应商！";
        }
        //打开等待条
        addLoadMask();
        $.ajax({
        	type: "POST",
    		dataType: "text",
    		url: "getSkuByItemIds",
            data: {
            	itemIds: itemIds ,
            	shopIds: shopIds ,
            	shopNames : shopNames,
            	itemName : itemName,
            	tempDate : tempDate
            },
            success: function (data) {
                $("#itemList").append(data);
                closeItemSearchDIV();
              //关闭等待条
                removeLoadMask();
            },
    		error:function(xhr,errmsg,obj){
    			_msg=errmsg;
    		}
        });
        //李伟龙注释掉  新增上面ajax获取商品sku方法
        //for (var i = 0; i < shopIdArr.length; i++) {
            /*temp += "<tr class='bg_02 hei_32'><input type='hidden' name='itemIds' value='"+itemId+"'/>"+
             "<input type='hidden' name='statusDetail' value='i'/> <input type='hidden'  name='detailId' value= '' />"+
             "<td class='wid_15 hei_30' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+itemName+"</td><td class='wid_30 hei_30' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'><input type='hidden' name='shopId' value='"+shopIdArr[i]+"'/>"+shopNameArr[i]+
             "</td><td class='wid_15'><input type='text'  maxlength='11' class='hei_30 wid_80' name='nums' onkeypress='number()' onkeyup='filterInput()' onchange='filterInput()' onbeforepaste='filterPaste()' onpaste='return false'/></td>" +
             "<td class='wid_15 hei_30'><span class='delete'>删除</span></td></tr>";*/
        /*李伟龙注释掉  新增上面ajax获取商品sku方法
            temp += "<ul class='mar_lr5'><input type='hidden' name='itemIds' value='" + itemId + "'/>" +
                "<input type='hidden' name='statusDetail' value='i'/> <input type='hidden'  name='detailId' value= '' />" +
                "<input type='hidden' name='shopId' value='" + shopIdArr[i] + "'/>" +
                "<div class='hei_32 border_2'>" +
                "<p class='fl'><span style='font-weight:bold;'>物品名：</span><span>" + itemName + "</span></p>" +
                "</div>" +
                "<div class='hei_32 border_2'>" +
                "<p class='fl'><span style='font-weight:bold;'>供应商：</span><span>" + shopNameArr[i] + "</span></p>" +
                "</div>" +
                "<div class='hei_32 border_2'>" +
                "<p class='fl wid_75'><span style='font-weight:bold;'>交货时间：</span><input type='date' name='deliveryDates' class='form-control wid_50 hei_20' value='" + tempDate + "'/></p>" +
                "</div>" +
                "<div class='border_2 shop_order pad_tb5' >" +
                "<p class='wid_95 fl shop_order_p'><span style='font-weight:bold;'>数量：</span><input type='text'  maxlength = '11' name='nums' onkeypress='number()' onkeyup='filterInput()' onchange='filterInput()' onbeforepaste='filterPaste()' onpaste='return false' class='form-control wid_50 hei_20'  />"
                + "<button class='fr button_3 pad_mlr5 delete'>删除</button></p>" +
                "<div class='clear'></div>" +
                "</div></ul>";
        }
    	*/
    });
    
}

function removeItem(e) {
    var skuId = $(e).parent().parent().parent().find("[name='matCd']").val();
    console.log(skuId);
    console.log(createItemLi.items);
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

function changeKeyWord() {
    var keyWord = $("#keyWord").val();
    $.ajax({
        url: $("#contextPath").val() + "/requestPriceController/getItemList",
        data: {
            "keyWord": keyWord,
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#partBDetail").html(data);
            showDiv();
        }
    });
}

function showShopInfo(obj, divId) {
    //将此div中的供应商的id更新到隐藏域中
    var shopId = "";
    var shopName = "";
  //商品Id集合 李伟龙增加代码 start
    var itemNames ="";
    //===========end
    if ($(obj).prop("checked")) {
        //判断是否前台已经选中，如果有此产品大全，则不能选中
        $("input[name='itemIds']").each(function (e) {
            if (divId == $(this).val()) {
                toast("不能选择重复的产品大全信息!");
                $(obj).attr("checked", false);
                throw "产品大全已经选过！";
            }
        });
        if ($("input[name='" + divId + "']").length == 0) {
            toast("此商品大全下没有找到店铺信息，请选择其他物品进行询价！");
            $(obj).removeAttr("checked");
            return;
        }
        $("input[name='" + divId + "']:checked").each(function (e) {
            if (shopId == "") {
                shopId = $(this).val();
                shopName = $(this).parent().find("input[name='shopName']").val();
              //商品Id集合 李伟龙增加代码 start
                itemNames = $(this).parent().find("input[name='linkMan1']").val();
                //===========end
            } else {
                shopId = shopId + "," + $(this).val();
                shopName = shopName + "," + $(this).parent().find("input[name='shopName']").val();
              //商品Id集合 李伟龙增加代码 start
                itemNames = itemNames + "," + $(this).parent().find("input[name='linkMan1']").val();
              //===========end
            }
        });
    }
    $(obj).parent().parent().find("input[name='shopIds']").each(function (e) {
        $(this).val(shopId);
    });
    $(obj).parent().parent().find("input[name='shopNames']").each(function (e) {
        $(this).val(shopName);
    });
    //商品Id集合 李伟龙增加代码 start
    $(obj).parent().parent().parent().find("input[name='itemIds']").each(function (e) {
        $(this).val(itemNames);
    });
    //===========end
    if(obj.checked){
    	$("#" + divId).show();
    }else{
    	$("#" + divId).hide();
    }
    
}

function checkAllCurrShop(thisObj,ddId){
	$("input[name='" + ddId + "']").each(function(){
		if(thisObj.checked){
			$(this)[0].checked=true;
			 changeShopIdInfo($(this)[0], ddId);
	 	}else{
	 		$($(this)[0]).attr("checked",false);
	 		changeShopIdInfo($(this)[0], ddId);
	 	}
	});
}

function idCheckAll(ddId){
	var statue = 0;
	$("input[name='" + ddId + "']").each(function () {
		if(!$(this)[0].checked){
			statue = 1;
		}
    });
	if(statue == 0){
		$("#c_"+ddId)[0].checked=true;
	}
}

function changeShopIdInfo(obj, ddId) {
	if(!obj.checked){
		$("#c_"+ddId)[0].checked=false;
	}
    //将此div中的供应商的id更新到隐藏域中
    var shopId = "";
    var shopName = "";
    //商品Id集合 李伟龙增加代码 start
    var itemNames ="";
    //===========end
    $("input[name='" + ddId + "']:checked").each(function (e) {
        if (shopId == "") {
            shopId = $(this).val();
            shopName = $(this).parent().find("input[name='shopName']").val();
            //商品Id集合 李伟龙增加代码 start
            itemNames = $(this).parent().find("input[name='linkMan1']").val();
            //===========end
        } else {
            shopId = shopId + "," + $(this).val();
            shopName = shopName + "," + $(this).parent().find("input[name='shopName']").val();
          //商品Id集合 李伟龙增加代码 start
            itemNames = itemNames + "," + $(this).parent().find("input[name='linkMan1']").val();
          //===========end
        }
    });
    $(obj).parent().parent().parent().find("input[name='shopIds']").each(function (e) {
        $(this).val(shopId);
    });
    $(obj).parent().parent().parent().find("input[name='shopNames']").each(function (e) {
        $(this).val(shopName);
    });
  //商品Id集合 李伟龙增加代码 start
    $(obj).parent().parent().parent().find("input[name='itemIds']").each(function (e) {
        $(this).val(itemNames);
    });
    //===========end
    idCheckAll(ddId);
}

function changeVal(obj){
	var value = $(obj).val();
	if($(obj).val().indexOf('T')>0){
		value = value.replace('T',' ');
		$(obj).val(value);
	}
}

function checkDate(obj, beginDate) {
    if (new Date($(obj).val()) < new Date(beginDate)) {
        //$(obj).parent().add("<span style='color:red;' class='errorMsg'>时间必须大于询价开始时间</span>");
        $(obj).val(beginDate);
    }
}

function changeType(obj) {
    $(obj).attr("type", "date");
}

function onChangeType(obj) {
    $(obj).attr("type", "text");
}


function loadItem(cid) {
    $("#cid").val(cid);
    canRefresh = false;
    $("#itemSearchResult").html('');
    var data = {};
    data.page = 1;
    data.keyword = $("#keyWord").val();
    data.pageSize = 5;
    data.cid = cid;
    leimu3Id = '';
    loadProducts(data);
    toggleMenu();
}

function changeInputStyle(obj){
    $(obj).attr('type','datetime-local');
}
