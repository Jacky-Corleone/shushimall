/**
 * 求购相关js
 */
$(document).ready(function(){
	
	//默认求购时间为当前时间
	if($("#beginDate1").val() == "" || $("#beginDate1").val() == null){
		var s = (new Date()).getSeconds();
		var m = (new Date()).getMinutes(); 
		var h = (new Date()).getHours();
		var yyyy = (new Date()).getFullYear();
		var mm = (new Date()).getMonth()+1;
		var dd = (new Date()).getDate();
		var now = yyyy+"-"+(mm<10 ? "0" + mm : mm) +"-"+ (dd<10 ? "0" + dd : dd);
		$("#beginDate1").val(now);
	}
	
	
	//求购前台验证
	$("#businessBtn").click(function(){
		var translationNo = $("#translationNo").val();
		
		if( translationNo == null ||  translationNo == "" ){
			$(".errorMsg").html("求购编码必填！");
			return;
		};
		var translationName = $("#translationName").val();
		if( translationName == null ||  translationName == "" ){
			$(".errorMsg").html("求购名称必填！");
			return;
		};
		var beginDate = $("#beginDate1").val();
		if( beginDate == null ||  beginDate == "" ){
			$(".errorMsg").html("求购日期必填！");
			return;
		};
		var endDate = $("#endDate1").val();
		if( endDate == null ||  endDate == "" ){
			$(".errorMsg").html("截至报价日期必填！");
			return;
		};
		var deliveryDate = $("#deliveryDate1").val();
		if( deliveryDate == null ||  deliveryDate == "" ){
			$(".errorMsg").html("交货日期必填！");
			return;
		};
		var printerId = $("#printerId").val();
		if( printerId == null ||  printerId == "" ){
			$(".errorMsg").html("印刷厂必填！");
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
		if($("input[name='nums']").length == 0){
			$(".errorMsg").html("请至少选择一件物品进行求购！");
			return;
		}
		$("input[name='nums']").each(function(e){
			if($(this).val() == ""){
				$(".errorMsg").html("数量必填！");
				throw "数量必填！";
				$(this).focus();
			} 
			if($(this).val() == "0"){
				$(".errorMsg").html("数量必需大于0！");
				throw "数量必需大于0！";
				$(this).focus();
			}
		});
		$("input[name='itemNames']").each(function(e){
			if($(this).val() == ""){
				$(".errorMsg").html("物品名称必填！");
				throw "物品名称必填！";
				$(this).focus();
			} 
		});
		$("input[name='matAttributes']").each(function(e){
			if($(this).val() == ""){
				$(".errorMsg").html("商品属性必填！");
				throw "商品属性必填！";
				$(this).focus();
			} 
		});
		//跑后台创建求购信息
		$(".errorMsg").html("");
		createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/addTranslationInfo",
			data:$("#translationForm").serializeArray(),
			success: function(data){
				alert(data);
				window.location="askItemInfo";
			},
			error:function(data){
				alert(data);
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
	
	//删除新增加的tr
	$(document).on("click", ".delete", function(e) {
		if(confirm("确定删除？")){
			//将这条记录的物料id放到隐藏域中，后台保存时需要将此物料的主单删除
			var ids = $(this).parent().find("input[name='ids']").val();
			
			var flag = $(this).parent().find("input[name='flag']").val();
			var tempVar = $("#deleteId").val();
			if(flag == "u"){
				if(tempVar == ""){
					tempVar = ids;
				}else{
					tempVar = ids+","+matCd;
				}
			}
			$("#deleteId").val(tempVar);
			$(this).parent().remove();
		};
		 
		});
	
	//跳往修改求购信息页面
	$(document).on("click", "#button_update", function(e) {
	
		var obj=document.getElementsByName('translationNo'); 
		  var s='';  
		  var checkNum = 0;
		  $('input[name="translationNo"]:checked').each(function(){    
			  checkNum += 1;
			  }); 
		  if(checkNum < 1){
			  alert("请选择一条求购信息进行修改!");
			  return;
		  }
		  if(checkNum > 1){
			  alert("您只能选择一条求购信息进行修改!");
			  return;
		  }
		  var status = "";
		  $(".list_li li input[type=checkbox]").each(function(){
			    if(this.checked){
			    	 status  = $(this).parent().find("input:first").val();
			    	 s = $(this).val(); 
			    }
			}); 
		  if(status != "0" && status != "4"){
	    		alert("求购单【"+s+"】，状态不是待提交、已驳回！");
	    		return;
	    	}
		window.location="askItemInfoUpdate"+"?translationNo="+s;
	});
	
	function chk(){    
		  var obj=document.getElementsByName('translationNo'); 
		  var s='';    
		  for(var i=0; i<obj.length; i++){    
		    if(obj[i].checked) s+=obj[i].value+',';   
		  }    
		  alert(s==''?'你还没有选择任何内容！':s);    
		} 
	
	function jqchk(){  //jquery获取复选框值    
		  var chk_value =[];    
		  $('input[name="translationNo"]:checked').each(function(){    
		   chk_value.push($(this).val());    
		  });    
		  alert(chk_value.length==0 ?'你还没有选择任何内容！':chk_value);    
		} 
	
		//删除
	$(document).on("click", "#button_delete", function(e) {
		var obj=document.getElementsByName('translationNo'); 
		  var ids="";    
		  for(var i=0; i<obj.length; i++){   
			  var x = 1;
		    if(obj[i].checked) {
		    	ids+=obj[i].value+",";   
		    	//状态不是1的不能删除
		    	var status = $(obj[i]).parent().find("input:first").val();
		    	if(status != "0" && status != "4"){
		    		alert("求购单【"+obj[i].value+"】状态不是待提交、已驳回！");
		    		return;
		    	}
		    	x += 1;
		    }
		  }
		  if(ids==""){
			  alert("请选择需要删除的求购单！");
			  return;
		  }  
		  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认删除选中的求购信息？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/deleteTranslation",
			data:{
				ids: ids
				},
			success: function(result){
				alert("删除成功！")
				 window.location="askItemInfo";
			},
			error:function(result){
				alert('删除失败！');
				window.location="askItemInfo";
			}
		});
	});
	
	//审核
	$(document).on("click", "#button_audit", function(e) {
		var obj=document.getElementsByName('translationNo'); 
		  var ids="";    
		  for(var i=0; i<obj.length; i++){   
			  var x = 1;
		    if(obj[i].checked) {
		    	ids+=obj[i].value+",";   
		    	//状态不是1的不能删除
		    	var status = $(obj[i]).parent().find("input:first").val();
		    	if(status != "1"){
		    		alert("求购单【"+obj[i].value+"】状态不是待审核！");
		    		return;
		    	}
		    	x += 1;
		    }
		  }
		  if(ids==""){
			  alert("请选择需要审核的求购单！");
			  return;
		  }  
		  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认审核选中的求购信息？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/auditTranslation",
			data:{
				ids: ids
				},
			success: function(result){
				alert("审核成功！")
				 window.location="askItemInfo";
			},
			error:function(result){
				alert('审核失败！');
				window.location="askItemInfo";
			}
		});
	});
	
	
	//求购
	$(document).on("click", "#button_commit", function(e) {
		var obj=document.getElementsByName('translationNo'); 
		  var ids="";    
		  for(var i=0; i<obj.length; i++){   
			  var x = 1;
		    if(obj[i].checked){ 
		    	//状态不是1的不能求购
		    	var status = $(obj[i]).parent().find("input:first").val();
		    	if(status != "0" && status != "4"){
		    		alert("求购单【"+obj[i].value+"】状态不是待提交、已驳回！");
		    		return;
		    	}
		    	x += 1;
		    	ids+=obj[i].value+",";   
		    }
		    
		  }
		  if(ids==""){
			  alert("请选择需要求购的求购单！");
			  return;
		  }  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认进行求购？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/commitTranslation",
			data:{
				ids: ids
				},
			success: function(result){
				alert(result)
				 window.location="askItemInfo";
			},
			error:function(result){
				alert(result);
				window.location="askItemInfo";
			}
		});
	});
	
	
	//重新求购
	$(document).on("click", "#button_commit_re", function(e) {
		var obj=document.getElementsByName('translationNo'); 
		  var ids="";    
		  for(var i=0; i<obj.length; i++){   
			  var x = 1;
		    if(obj[i].checked){ 
		    	//状态不是1的不能求购
		    	var status = $(obj[i]).parent().find("input[name='status']").val();
		    	if(status != "2" && status != "3"){
		    		alert("求购单【"+obj[i].value+"】状态不是待报价或者已接受状态,无法执行重新求购操作！");
		    		return;
		    	}
		    	x += 1;
		    	ids+=obj[i].value+",";   
		    }
		    
		  }
		  if(ids==""){
			  alert("请选择需要重新求购的求购单！");
			  return;
		  }  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认重新进行求购？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/commitTranslationRe",
			data:{
				ids: ids
				},
			success: function(result){
				alert(result)
				 window.location="askItemInfo";
			},
			error:function(result){
				alert(result);
				window.location="askItemInfo";
			}
		});
	});
	
	
	//接受
	$(document).on("click", "#button_commit_yes", function(e) {
		var obj=document.getElementsByName('translationNo'); 
		  var ids="";
		  var checkCount = $("input[name='translationNo']:checked").length;
		  if(checkCount == 0){
			  alert("请选择一条求购单！");
			  return;
		  }
		  if(checkCount > 1){
			  alert("您只能选择一条求购单进行接受！");
			  return;
		  }
		  $("input[name='translationNo']:checked").each(function(e){
			  var status = $(this).parent().find("input[name='status']").val();
	    	  if(status != "2"){
	    	  	alert("求购单【"+$(this).val()+"】状态不是待报价！");
	    	  	throw "求购单【"+$(this).val()+"】状态不是待报价！";
	    	  }
	    	  ids=$(this).val();  
		  });
		  
		  var detailId = "";
		  $("input[name='itemDetail']:checked").each(function(e){
			  if(detailId == ""){
				  detailId = $(this).val();
			  }else{
				  detailId = detailId + ","+$(this).val();
			  }
			  var price = $(this).parent().find("input[name='detailPrice']").val();
			  var detailName = $(this).parent().find("input[name='detailName']").val();
			  if(price == "" || price == "null"){
				  alert("【"+detailName+"】还未报价，请等卖家报价后再接受!");
				  throw "【"+detailName+"】还未报价，请等卖家报价后再接受!";
			  }
		  });
		  
		  if(detailId == ""){
			  alert("请等待卖家报价后至少选择一条信息接受求购！");
			  return;
		  }
		  
		  if(!confirm("是否确认接受价格？")){
			  return;
		  }  
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/acceptTranslationInfo",
			data:{
				ids: ids,
				detailId : detailId
				},
			success: function(result){
				alert(result)
				 window.location="askItemInfo";
			},
			error:function(result){
				alert(result);
				window.location="askItemInfo";
			}
		});
	});
	//增加一行
	$(document).on("click","#add", function(e){

		window.open('categoryListPage','Derek','width='+ (window.screen.availWidth-10)+',height='+(window.screen.availHeight-30)+ ',top=0,left=0,resizable=yes,status=yes,menubar=no,scrollbars=no');

	});
	
	//卖家跳往修改求购信息页面
	$(document).on("click", "#button_update_response", function(e) {
	
		var obj=document.getElementsByName('translationNo'); 
		  var s='';  
		  var checkNum = 0;
		  $('input[name="translationNo"]:checked').each(function(){    
			  checkNum += 1;
			  }); 
		  if(checkNum < 1){
			  alert("请选择一条求购信息进行报价!");
			  return;
		  }
		  if(checkNum > 1){
			  alert("您只能选择一条求购信息进行报价!");
			  return;
		  }
		  var status = "";
		  var endDate = "";
		  $(".list_li li input[type=checkbox]").each(function(){
			    if(this.checked){
			    	 status  = $(this).parent().find("input[name='status']").val();
					endDate  = $(this).parent().find("input[name='endDate']").val();
			    }
			});
		var yyyy = (new Date()).getFullYear();
		var mm = (new Date()).getMonth()+1;
		var dd = (new Date()).getDate();
		var now = yyyy+"-"+(mm<10 ? "0" + mm : mm) +"-"+ (dd<10 ? "0" + dd : dd) + " 00:00:00";
		if(new Date(endDate) < new Date(now)){
			alert("报价不在询价截止时间内，无法报价!");
			return;
		}
		  if(status != "2" ){
	    		alert("选中的记录，状态不是待报价！");
	    		return;
	    	}
		  $('input[name="translationNo"]:checked').each(function(){    
			     s = $(this).val();    
			  });  
		window.location="repAskItemInfoUpdate"+"?translationNo="+s;
	});
	
	//卖家确认求购信息
	$(document).on("click", "#button_commit_response", function(e) {
		var obj=document.getElementsByName('translationNo'); 
		  var ids="";    
		  for(var i=0; i<obj.length; i++){    
			  var x = 1;
		    if(obj[i].checked) {
		    	//状态不是1的不能求购
		    	var status = $(obj[i]).parent().find("input:first").val();
		    	if(status != "3"){
		    		alert("选中的第"+x+"条记录，状态不是未报价！");
		    		return;
		    	}
		    	x += 1;
		    	ids+=obj[i].value+",";   
		    }
		  }
		  if(ids==""){
			  alert("请选择需要确认的求购单！");
			  return;
		  }  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认进行求购？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/commitRepTranslationInfo",
			data:{
				ids: ids
				},
			success: function(result){
				alert("价格确认成功！")
				 window.location="repAskItemInfo";
			},
			error:function(result){
				alert('价格确认失败！');
				window.location="repAskItemInfo";
			}
		});
	});
	
	
	//卖家设置求购价格前台验证
	$("#responseButtonSave").click(function(){
		var translationNo = $("#translationNo").val();
		var beginDate = $("#beginDate1").val();
		//求购截至时间、交货时间必须大于求购时间
		var start=new Date(beginDate);
		var yyyy = (new Date()).getFullYear();
		var mm = (new Date()).getMonth()+1;
		var dd = (new Date()).getDate();
		var now = yyyy+"-"+(mm<10 ? "0" + mm : mm) +"-"+ (dd<10 ? "0" + dd : dd);
		//循环验证，每条明细的价格必填，有效时间必须大于询价时间，截至时间必须大于有效时间(始)
		$("input[name='nums']").each(function (e){
				if($(this).val() == "") {
					$(".errorMsg").html("数量必填!");
					$(this).focus();
					throw "数量必填!";
				}
			}
		);
		$("input[name='price']").each(function(){
			if($(this).val() != ""){
				var detailstartDate = $(this).parent().parent().find("input[name='detailstartDate']").val();
				var detailendDate = $(this).parent().parent().find("input[name='detailendDate']").val();
				if( detailstartDate == ""){
					$(".errorMsg").html("有效日期(始)必填!");
					$(this).parent().parent().find("input[name='detailstartDate']").focus();
					throw "有效日期(始)必填!";
				}
				if(new Date(now) > new Date(detailstartDate)){
					$(".errorMsg").html("有效日期(始)不能小于今天!");
					$(this).parent().parent().find("input[name='detailstartDate']").focus();
					throw "有效日期(始)不能小于今天!";
				}
				if(detailendDate == ""){
					$(".errorMsg").html("有效日期(止)必填!");
					$(this).parent().parent().find("input[name='detailendDate']").focus();
					throw "有效日期(止)必填!";
				}
				if(new Date(detailstartDate) > new Date(detailendDate)){
					$(".errorMsg").html("有效日期(止)不能小于有效日期(始)!");
					$(this).parent().parent().find("input[name='detailendDate']").focus();
					throw "有效日期(止)不能小于有效日期(始)!";
				}
			}
		});
		createBlackBack();
		//跑后台创建求购信息
		$(".errorMsg").html("");
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/askItemInfoJavaController/saveRepTranslationInfo",
			data:$("#translationForm").serializeArray(),
			success: function(data){
				alert(data);
				window.location="repAskItemInfo";
			},
			error:function(data){
				alert(data);
			}
		});
		
	});
	
	
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
	    procbg.style.zIndex = "80";
	    procbg.style.opacity = "0.6";
	    procbg.style.filter = "Alpha(opacity=70)";
	    $(procbg).append(divText);
	    // 背景层加入页面
	    document.body.appendChild(procbg);
	    document.body.style.overflow = "hidden"; // 取消滚动条
	}
	
	
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
	
	
});

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

function translationInfoShow(obj, flag, translationNo){
	//后台传值查询求购单明细信息
	$.ajax({
		type: "post",
		dataType: "html",
		url: $("#contextPath").val()+"/askItemInfoJavaController/getDetaiInfo",
		data:{
			translationNo: translationNo,
			flag : flag
			},
		success: function(result){
			var result1 = result.split("%");
			//询价明细
			$("#information1 tbody").html('');
			if(result.split("%")[0] != "\""){
				var temp = "<tr class='font_cen bg_05'><td class='border-1 wid_50'><input type='checkbox' onclick='checkAllDetail(this);' /></td> <td class='border-1 wid_120'>供应商</td>"+
                "<td class='border-1 wid_120'>类目名称</td><td class='border-1 wid_120'>物品名称</td><td class='border-1 wid_120'>商品属性</td><td class='border-1 wid_80'>状态</td><td class='border-1 wid_80'>数量</td>"+
                "<td class='border-1 wid_80'>单价</td></tr>";
				$("#information1 tbody").html(temp+result.split("%")[0]);
			}
			/*//订单明细
			$("#information2 tbody").html('');
			if(result.split("%")[1] != "\""){
				var temp = "<tr class='font_cen bg_05'> <td class='border-1 wid_110'>求购编号</td>"+
                        "<td class='border-1 wid_200'>订单编号</td></tr>";
				$("#information2 tbody").html(temp+result.split("%")[1]);
			}else{
				var temp = "<tr class='font_cen bg_05'> <td class='border-1 wid_180'>求购编号</td>"+
                "<td class='border-1 wid_200'>订单编号</td></tr>";
				$("#information2 tbody").html(temp);
			}*/
		},
		error:function(result){
			alert('faile');
		}
	});
}

function checkAllDetail(obj){
	if(obj.checked){
		$("input[name='itemDetail']").each(function(e){
			$(this).prop("checked",true);
		});
	}else{
		$("input[name='itemDetail']").each(function(e){
			$(this).prop("checked",false);
		});
	}
}

function select_all(obj){
	if(obj.checked){
		$("input[name='translationNo']").each(function(e){
			$(this).prop("checked",true);
		}); 
	}else{
		$("input[name='translationNo']").each(function(e){
			$(this).prop("checked",false);
		});
	}
}

function goBackPage(flag){
     if(flag == "request"){
		 window.location="askItemInfo";
	 }else{
		 window.location="repAskItemInfo";
	 }
}

function lookAskItemInfo(flag,userType){
	var checkNum = 0;
	$('input[name="translationNo"]:checked').each(function(){
		checkNum += 1;
	});
	if(checkNum < 1){
		alert("请选择一条求购信息进行查看!");
		return;
	}
	if(checkNum > 1){
		alert("您只能选择一条求购信息进行查看!");
		return;
	}
	$('input[name="translationNo"]:checked').each(function(){
		s = $(this).val();
	});
	window.location="lookAskItemInfo"+"?translationNo="+s+"&flag="+flag+"&userType="+userType;
}

