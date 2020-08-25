/**
 * 求购相关js
 */

$(document).ready(function(){
	
	//求购前台验证
	$(document).on("click", "#button_save", function(e){
		var inquiryNo = $("input[name='translationNo']").val();
		if( inquiryNo == null ||  inquiryNo == "" ){
			$(".errorMsg").html("求购编码必填！");
			return;
		};
		var inquiryName = $("input[name='translationName']").val();
		if( inquiryName == null ||  inquiryName == "" ){
			$(".errorMsg").html("求购名称必填！");
			return;
		};
		var beginDate = $("input[name='beginDate1']").val();
		if( beginDate == null ||  beginDate == "" ){
			$(".errorMsg").html("求购日期必填！");
			return;
		};
		var endDate = $("input[name='endDate1']").val();
		if( endDate == null ||  endDate == "" ){
			$(".errorMsg").html("截至报价日期必填！");
			return;
		};
		var deliveryDate = $("input[name='deliveryDate1']").val();
		if( deliveryDate == null ||  deliveryDate == "" ){
			$(".errorMsg").html("交货日期必填！");
			return;
		};
		var printerId = $("input[name='printerId']").val();
		if( printerId == null ||  printerId == "" ){
			$(".errorMsg").html("求购方必填！");
			return;
		};
		$("input[name='nums']").each(function(){
			if($(this).val() == ""){
				$(".errorMsg").html("数量必填！");
				throw "数量必填！";
			} 
			if($(this).val() == "0"){
				$(".errorMsg").html("数量必需大于0！");
				throw "数量必需大于0！";
			}
		});
		$("input[name='matAttributes']").each(function(){
        			if($(this).val() == ""){
        				$(".errorMsg").html("商品属性必填！");
        				throw "商品属性必填！";
        			}
        		});
		$("input[name='itemNames']").each(function(){
			if($(this).val() == ""){
				$(".errorMsg").html("物品名必填！");
				throw "数量必填！";
			} 
		});
		//求购截至时间、交货时间必须大于求购时间
		var start=new Date(beginDate); 
		var end1=new Date(endDate);  
		var end2=new Date(deliveryDate);  
		if(start > end1){
			$(".errorMsg").html("截止报价时间必须大于求购时间！");
			return;
		}
		if(start > end2){
			$(".errorMsg").html("交货时间必须大于求购时间！");
			return;
		}
		if($("input[name='nums']").length < 1){
			$(".errorMsg").html("请至少选择一个物品进行求购！");
			return;
		}
		//跑后台创建求购信息
		$(".errorMsg").html("");
		addLoadMask();
		$.ajax({
			type: "POST",
			dataType: "html",
			url: $("#translationForm").prop("action"),
			data:$("#translationForm").serializeArray(),
			success: function(data){
				removeLoadMask();
				toast(data);
				window.location="askItemInfo";
			}
		});
		
	});
	
	//新增一行物品的tr
	/*$(document).on("click","#add",function(){
		var trHTML = "<tr class='hei_32'><td class='wid_30'></td><td class='wid_30'></td><td class='wid_11'>箱</td><td class='wid_15'><span class='delete'>删除</span></td></tr>";
		$("#add").before(trHTML);
    });*/
	
	//删除新增加的tr
	$(document).on("click", ".delete", function(e) {
		if(confirm("确定删除？")){
			//将这条记录的物料id放到隐藏域中，
			var matCd = $(this).parent().parent().find("input[name='ids']").val();
			
			var flag = $(this).parent().parent().find("input[name='statusDetail']").val();
			var tempVar = $("#deleteIds").val();
			if(flag != "i"){
				if(tempVar == ""){
					tempVar = matCd;
				}else{
					tempVar = tempVar+","+matCd;
				}
			}
			$("#deleteIds").val(tempVar);
			$(this).parent().parent().remove();
		};
		 
		});

	//求购前台验证
	$(document).on("click", "#button_update", function(e){
		var inquiryNo = $("input[name='translationNo']").val();
		if( inquiryNo == null ||  inquiryNo == "" ){
			$(".errorMsg").html("求购编码必填！");
			return;
		};
		var inquiryName = $("input[name='translationName']").val();
		if( inquiryName == null ||  inquiryName == "" ){
			$(".errorMsg").html("求购名称必填！");
			return;
		};
		var beginDate = $("input[name='beginDate1']").val();
		if( beginDate == null ||  beginDate == "" ){
			$(".errorMsg").html("求购日期必填！");
			return;
		};
		var endDate = $("input[name='endDate1']").val();
		if( endDate == null ||  endDate == "" ){
			$(".errorMsg").html("截至报价日期必填！");
			return;
		};
		var deliveryDate = $("input[name='deliveryDate1']").val();
		if( deliveryDate == null ||  deliveryDate == "" ){
			$(".errorMsg").html("交货日期必填！");
			return;
		};
		var printerId = $("input[name='printerId']").val();
		if( printerId == null ||  printerId == "" ){
			$(".errorMsg").html("求购方必填！");
			return;
		};
		//求购截至时间、交货时间必须大于求购时间
		var start=new Date(beginDate); 
		var end1=new Date(endDate);  
		var end2=new Date(deliveryDate);  
		if(start > end1){
			$(".errorMsg").html("求购截止时间必须大于求购时间！");
			return;
		}
		if(start > end2){
			$(".errorMsg").html("交货时间必须大于求购时间！");
			return;
		}
		if($("input[name='nums']").length < 1){
			$(".errorMsg").html("请至少选择一个物品进行求购！");
			return;
		}
		$("input[name='nums']").each(function(){
			if($(this).val() == ""){
				$(".errorMsg").html("数量必填！");
				throw "数量必填！";
			} 
			if($(this).val() == "0"){
				$(".errorMsg").html("数量必需大于0！");
				throw "数量必需大于0！";
			}
		});
		$("input[name='matAttributes']").each(function(){
			if($(this).val() == ""){
				$(".errorMsg").html("商品属性必填！");
				throw "商品属性必填！";
			}
		});
		$("input[name='itemNames']").each(function(){
			if($(this).val() == ""){
				$(".errorMsg").html("物品名必填！");
				throw "数量必填！";
			} 
		});
		//跑后台创建求购信息
		$(".errorMsg").html("");
		addLoadMask();
		$.ajax({
			type: "POST",
			dataType: "html",
			url: $("#translationForm").prop("action"),
			data:$("#translationForm").serializeArray(),
			success: function(data){
				removeLoadMask();
				toast(data);
				window.location="askItemInfo";
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
	
	
	
	//卖家设置求购价格前台验证
	$("#button_update_response").click(function(){
		$(".errorMsg").html("");
		var d = new Date()
        var vYear = d.getFullYear()
        var vMon = d.getMonth() + 1
        var vDay = d.getDate()
        var s=vYear+"-"+(vMon<10 ? "0" + vMon : vMon)+"-"+(vDay<10 ? "0"+ vDay : vDay);
		var translationNo = $("#translationNo").val();
		var beginDate = $("#beginDate1").val();
		//求购截至时间、交货时间必须大于求购时间
		var start=new Date(beginDate); 
		//循环验证，每条明细的价格必填，有效时间必须大于求购时间，截至时间必须大于有效时间(始)
		$("input[name='price']").each(function(e){
			var price = $(this).val();
			var detailstartDate = $(this).parent().parent().find("input[name='detailstartDate']").val();
			var detailendDate = $(this).parent().parent().find("input[name='detailendDate']").val();
			if(price != ""){
				if(detailstartDate == ""){
					$(".errorMsg").html("开始时间必填!");
					toast("开始时间必填!");
					return false;
				}
				if(detailendDate == ""){
					$(".errorMsg").html("结束时间必填!");
					toast("结束时间必填!");
					return false;
				}
				if(new Date(detailstartDate) < new Date(s)){
					$(".errorMsg").html("开始时间不能小于今天!");
					toast("开始时间不能小于今天!");
					return false;
				}

				if(new Date(detailstartDate) > new Date(detailendDate)){
					$(".errorMsg").html("结束时间不能小于开始时间!");
					toast("结束时间不能小于有效期(始)!");
					return false;
				}
			}else{
				$(".errorMsg").html("请填写物品单价");
				toast("请填写物品单价");
				return false;
			}
		});
		if($(".errorMsg").html() != ""){
			return false;
		}
		//跑后台创建求购信息
		$(".errorMsg").html("");
		addLoadMask();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#inquiryForm").prop("action"),
			data:$("#inquiryForm").serializeArray(),
			success: function(data){
				removeLoadMask();
				toast(data);
				window.location="repAskItemInfo";
			},
			error:function(data){
				removeLoadMask();
				toast(data);
			}
		});
		
	});
	
	$("input[name='nums']").keydown(function () {  
	    var e = $(this).event || window.event;  
	    var code = parseInt(e.keyCode);  
	    if (code >= 96 && code <= 105 || code >= 48 && code <= 57 || code == 8 || code==110 || code==190) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	});  
	
});

//买家编辑求购信息
function updateTranslation(translationNo, status){
	//求购状态查询验证
	if(status != "0" && status != "4"){
		toast("只有状态为待提交、已驳回的才能编辑！");
		return;
	}
	window.location="updateAskItemInfo?translationNo="+translationNo;
}

/**查看求购详细信息
 * @param translationNo
 */
function lookTranslationInfo(translationNo, flag){
	window.location="lookTranslation"+"?translationNo="+translationNo+"&flag="+flag;
}

function goBackPage(url_str){
	window.location=url_str;
}

function deleteTranslation(translationNo, status){
	//求购状态查询验证
	if(status != "0"  && status != "4"){
		toast("只有状态为待提交的才能删除！");
		return;
	}
	
	kltconfirm("是否确认删除求购信息？",function(){
		addLoadMask();
		//跑后台创建求购信息
		$.ajax({
			type: "POST",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/deleteTranslation",
			data:{
				translationNo:translationNo
				},
			success: function(data){
				removeLoadMask();
				toast(data);
				window.location="askItemInfo";
			}
		});
	});
}
//求购提交
function commitTranslation(translationNo, status){
	//求购状态查询验证
	if(status != "0"  && status != "4"){
		toast("只有状态为待提交的才能提交！");
		return;
	}

	//跑后台创建求购信息

	kltconfirm("是否确认进行求购？",function(){
		addLoadMask();
		//跑后台创建求购信息
		$.ajax({
			type: "POST",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/commitTranslation",
			data:{
				translationNo:translationNo
				},
			success: function(data){
				removeLoadMask();
				toast(data);
				window.location="askItemInfo";
			}
		});
		
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

//求购审核
function auditTranslation(translationNo, status){
	//求购状态查询验证
	if(status != "1"){
		toast("只有状态为待审核的才能审核！");
		return;
	}
	if(!confirm("是否确认进行审核？")){
		return;
	}
	addLoadMask();
	//跑后台创建求购信息
	$.ajax({
		type: "POST",
		dataType: "html",
		url: $("#contextPath").val()+"/askItemInfoJavaController/auditTranslation",
		data:{
			translationNo:translationNo
			},
		success: function(data){
			removeLoadMask();
			toast(data);
			window.location="askItemInfo";
		}
	});
}

//供应商选择
function getCompany(){
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
    Idiv.style.height = document.documentElement.clientHeight*0.75+"px";
    Idiv.style.display = "block";
    // 以下部分要将弹出层居中显示
    Idiv.style.top = (document.documentElement.clientHeight - Idiv.clientHeight)
        /2 + document.documentElement.scrollTop-50 + "px";



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

function putCompanyValue(obj){
    var companyName=$(obj).find("[name='companyName']").val();
    var companyId=$(obj).find("[name='companyId']").val();
    $("input[name='supplierId']").each(function (i,n){
    	$(this).attr('value',companyId);
	});
    $("input[name='supplierName']").each(function (i,n){
    	$(this).attr('value',companyName);
	});
    closeDiv();


}

function putItemValue(obj){
    var itemName=$(obj).find("[name='itemName']").val();
    var itemId=$(obj).find("[name='itemId']").val();
    var cName=$(obj).find("[name='cName']").val();
    $("input[name='itemId']").each(function (i,n){
    	$(this).attr('value',itemId);
	});
    $("input[name='itemName']").each(function (i,n){
    	$(this).attr('value',itemName);
	});
    var trHTML = "<tr class='hei_32'><input type='hidden' name='itemIds' value='"+itemId+"'/><input type='hidden' name='status' value='i'/><td class='wid_30'>"+itemName+"</td><td class='wid_11'>箱</td><td class='wid_11'><input type='text' name='nums'/></td><td class='wid_15'><span class='delete'>删除</span></td></tr>";
	$("#add").before(trHTML);
    closeDiv();

}

function itemSelect(){
	 $.ajax({
	        url: $("#contextPath").val() + "/requestPriceController/getItemList",
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

	//window.open('productsListPrice','Derek','height=1800,width=600,status=yes,toolbar=yes,menubar=no,location=yes');
}

function showItemDiv(divId){
    if(divId == ""){
       return;
    }
	$("#"+divId).toggle();
}
//买家查询方法
function queryTranslation(num, flag){
	var supplierName = $("#myTab0_Content" + num).find("#supplierName").val();
	var supplyId = $("#myTab0_Content" + num).find("#supplierId").val();
	var itemName = $("#myTab0_Content" + num).find("#itemName").val();
	var translationName =  $("#myTab0_Content" + num).find("#translationName").val();
	//跑后台创建求购信息
	$(".errorMsg").html("");
	addLoadMask();
	$.ajax({
		type: "POST",
		dataType: "html",
		url: $("#contextPath").val()+"/askItemInfoController/queryAskItemInfo",
		data:{
			supplierName : supplierName,
			supplyId : supplyId,
			itemName : itemName,
			translationName : translationName,
			num : num,
			flag : flag
		},
		success: function(data){
			$("#queryDiv"+num).html(data);
			removeLoadMask();
		},error: function(data){
			alert("亲系统繁忙，请稍后再试")
		}
	});
}
//重置方法
function resetButton(){
	$("#itemName").val('');
	$("#translationName").val('');
}

//卖家查询方法
function queryRepAskItemInfoSeller(num, flag){
	var supplierName = $("#supplierName").val();
	var supplyId = $("#supplierId").val();
	var itemName = $("#itemName").val();
	var translationName =  $("#translationName").val();
	//跑后台创建求购信息
	$(".errorMsg").html("");
	addLoadMask();
	$.ajax({
		type: "POST",
		dataType: "html",
		url: $("#contextPath").val()+"/askItemInfoController/queryRepAskItemInfo",
		data:{
			supplierName : supplierName,
			supplyId : supplyId,
			itemName : itemName,
			translationName : translationName,
			num : num,
			flag : flag
		},
		success: function(data){
			$("#queryDiv"+ num).html(data);
			var names ={};
			names.itemName = itemName;
			names.translationName = translationName;
			//刷新条件查询的分页总数
			$.ajax({
				url : $("#contextPath").val()+"/askItemInfoController/queryRepAskItemInfoCount",
				type : 'post',
				dataType:"json",
				data :names,
				success : function(res) {
					$("#totalCount0").val(res.totalCount0);
					$("#totalCount1").val(res.totalCount1);
					$("#totalCount2").val(res.totalCount2);
					var t_url = $("#contextPath").val() +"/askItemInfoController/queryRepAskItemInfo?flag=response&num="+num;
				    var totalCount0 = $("#totalCount"+num).val();
				    $.dropDownFresh('3',t_url, totalCount0 , "queryDiv" + num,"wrapper",names);
				    removeLoadMask();
				},
				error : function() {
					console.log("网络错误");
				}
			});
		},error: function(data){
			alert("亲系统繁忙，请稍后再试")
		}
	});
}

//卖家确认求购
function commitRepTranslation(translationNo, status, endDate){
	//求购状态查询验证
	if(status != "3"){
		toast("只有状态为待报价的才能确认求购！");
		return;
	}
	var now = new Date();
	if(new Date(now) >  new Date(endDate)){
		toast("只有在截至报价时间之前的才可以报价确认！");
		return;
	}
	if(!confirm("是否确认报价？")){
		return;
	}
	addLoadMask();
	//跑后台修改求购状态为4
	$.ajax({
		type: "POST",
		dataType: "html",
		url: $("#contextPath").val()+"/askItemInfoJavaController/commitRepTranslation",
		data:{
			translationNo:translationNo
			},
		success: function(data){
			removeLoadMask();
			toast(data);
			window.location="repAskItemInfo";
		}
	});
}

//卖家编辑报价信息
function updateRepTranslation(translationNo, status, endDate){
	//求购状态查询验证
	if(status != "2"){
		toast("只有状态为待报价的求购的才能报价！");
		return;
	}
	var d = new Date();
	var vYear = d.getFullYear()
	var vMon = d.getMonth() + 1
	var vDay = d.getDate()
	var s=vYear+"/"+(vMon<10 ? "0" + vMon : vMon)+"/"+(vDay<10 ? "0"+ vDay : vDay);
	if(new Date(s) >  new Date(endDate)){
		toast("只有在截至报价时间之前的才可以报价！");
		return;
	}
	window.location="updateRepAskItemInfo"+"?translationNo="+translationNo;
}

//买家重新求购
function committranslationRe(translationNo, status){
	//求购状态查询验证
	if(status != "2" && status != "3"){
		toast("只有待报价、已确认才能重新求购！");
		return;
	}
	//跑后台修改求购状态为5
	kltconfirm("是否重新求购？",function(){
		addLoadMask();
		//跑后台创建求购信息
		$.ajax({
			type: "POST",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/commitTranslationRe",
			data:{
				translationNo:translationNo
				},
			success: function(data){
				removeLoadMask();
				toast(data);
				window.location="askItemInfo";
			}
		});
	});
	
	
	
	
}

//买家接收价格
function commitRequestTranslation(translationNo, status, checkName){
	//求购状态查询验证
	if(status != "2"){
		toast("只有状态为待报价的才能接收价格！");
		return;
	}
	var temp = $("input[name='"+checkName+"']:checked").length;
	if(temp == 0){
		toast("请等待卖家报价后至少选择一条信息进行接收!");
		return;
	}


	kltconfirm("是否重新求购？",function(){
		var detailIds = "";
		$("input:checkbox[name='"+checkName+"']:checked").each(function() 
	   {  
			if(detailIds == ""){
				detailIds =  $(this).val()
			}else{
				detailIds = detailIds + "," +  $(this).val();
			}
	   });

		addLoadMask();
		//跑后台创建求购信息
		//跑后台修改求购状态为5
		$.ajax({
			type: "POST",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/commitRequestTranslation",
			data:{
				translationNo : translationNo,
				detailIds : detailIds
				},
			success: function(data){
				removeLoadMask();
				toast(data);
				window.location="askItemInfo";
			}
		});
	});
	
	
	
	
	
	
}
//显示数量输入框
function showNumInput(inquiryNo){
	$("p[name='"+inquiryNo+"']").toggle();
}
var productList = new Array();


function checkAll(inquiryNo, obj){
	if(obj.checked){
		$("input[name='"+inquiryNo+"']").each(function(e){
			$(this).prop("checked",true);
			$(this).parent().parent().find("input[name='number']").parent().show();
		}); 
	}else{
		$("input[name='"+inquiryNo+"']").each(function(e){
			$(this).prop("checked",false);
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
    var supplierId = "1000000625";
    $.ajax({
        url: $("#contextPath").val() + "/requestPriceController/getItemList",
        data: {
            "Keyword": keyword,
            "supplierId": supplierId
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#itemSearchResult").html(data);
            showItemsDIV();
            closeBlackBack();
        }
    });
}

//展示物品弹出层
function showItemsDIV() {
    var Idiv = document.getElementById("itemSearchDIV");
    Idiv.style.display = "block";
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
    procbg.style.zIndex = "500";
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

function closeItemSearchDIV(){
    var Idiv = document.getElementById("itemSearchDIV");
    Idiv.style.display = "none";
    document.body.style.overflow = "auto"; // 恢复页面滚动条
    closeBlackBack();

}

function toggleMenu() {
    $("#navigation").toggle();
}

function number()
{
	var char = String.fromCharCode(event.keyCode)
	var re = /[0-9]/g
	event.returnValue = char.match(re) != null ? true : false
}

function filterInput()
{
	if (event.type.indexOf("key") != -1)
	{
	var re = /37|38|39|40/g
	if (event.keyCode.toString().match(re)) return false
	}
	event.srcElement.value = event.srcElement.value.replace(/[^0-9]/g, "")
}

function filterPaste()
{
	var oTR = this.document.selection.createRange()
	var text = window.clipboardData.getData("text")
	oTR.text = text.replace(/[^0-9]/g, "")
}

function createItemTR(categoryCid, categoryCname){
	
   if(ismodify){
	   obj.value = categoryCname;
	   if(obj.parentNode.parentNode.children[1].value == 'i'){
		   obj.parentNode.parentNode.children[2].children[1].value = categoryCid;
	   }else{
		   obj.parentNode.parentNode.children[2].value = categoryCid;
	   }
	   
   }else{
	   var temp = "<ul class='pass bg_01 mar_t10 pad_t5per'> <input type='hidden'  name='ids' value= ''/><input type='hidden'  name='statusDetail' value= 'i' />"+
	   "<li>" +
	   "<p class='wid_30 fl font_right pad_r5'><span style='font-weight:bold;'>类目名称：</span></p><input type='hidden'  name='category_ids' value='"+categoryCid+"' /><input type='text' onclick='showCategoryListDIV_modify(this)' name='category_names' readOnly class='wid_50 input_3'  maxlength='45' value='"+categoryCname+"' />" +
	   "</li>" +
	   "<li>" +
	   "<p class='wid_30 fl font_right pad_r5'><span style='font-weight:bold;'>物品名称：</span></p><input type='text'  name='itemNames' class='wid_50 input_3'  maxlength='45' value=''/>" +
	   "</li>" +
	   "<li>" +
	"<p class='wid_30 fl font_right pad_r5'><span style='font-weight:bold;'>物品属性：</span></p><input type='text' name='matAttributes' class='wid_50 input_3'  maxlength='200' value=''/>" +
	"</li>" +
	   "<li>" +
	   "<p class='wid_30 fl font_right pad_r5'><span style='font-weight:bold;'>物品数量：</span></p><input type='text' class='wid_50 input_3' name='nums' onkeypress='number()'  maxlength='11' onkeyup='filterInput()' onchange='filterInput()' onbeforepaste='filterPaste()' onpaste='return false'  /></li>"
	   + "<li><button class='fr button_3 pad_mlr5 delete'>删除</button>" +
	   "</li></ul>";
	   $("#itemList").append(temp);
   }
	closeItemSearchDIV();
}


var ismodify = false;
//物品选择弹出层
function showCategoryListDIV() {
	ismodify = false;
	addLoadMask();
	showItemsDIV();
	removeLoadMask();
}
var obj ;
function showCategoryListDIV_modify(_obj){
	obj = _obj ;
	ismodify = true;
	addLoadMask();
	showItemsDIV();
	removeLoadMask();
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

