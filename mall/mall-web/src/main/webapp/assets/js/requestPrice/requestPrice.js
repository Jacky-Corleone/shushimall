/**
 * 询价相关js
 */
$(document).ready(function(){
	
	//默认询价时间为当前时间
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
	
	
	//询价前台验证
	$("#businessBtn").click(function(){
		var inquiryNo = $("#inquiryNo").val();
		
		if( inquiryNo == null ||  inquiryNo == "" ){
			$(".errorMsg").html("询价编码必填！");
			return;
		};
		var inquiryName = $("#inquiryName").val();
		if( inquiryName == null ||  inquiryName == "" ){
			$(".errorMsg").html("询价名称必填！");
			return;
		};
		var beginDate = $("#beginDate1").val();
		if( beginDate == null ||  beginDate == "" ){
			$(".errorMsg").html("询价日期必填！");
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
		//询价截至时间、交货时间必须大于询价时间
		var start=new Date(beginDate); 
		var end1=new Date(endDate);  
		var end2=new Date(deliveryDate);  
		if(start > end1){
			$(".errorMsg").html("询价截止时间必须大于询价时间！");
			return;
		}
		if(start > end2){
			$(".errorMsg").html("交货时间必须大于询价时间！");
			return;
		}
		
		if($("input[name='nums']").length == 0){
			$(".errorMsg").html("请至少选择一件物品进行询价");
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
		$("input[name='deliveryDates']").each(function(){
			if($(this).val() == ""){
				$(".errorMsg").html("明细中交货日期必填！");
				throw "明细中交货日期必填！";
			}
		});
		//跑后台创建询价信息
		$(".errorMsg").html("");
		createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/addInquiry",
			data:$("#inquiryForm").serializeArray(),
			success: function(data){
				alert(data);
				window.location="requestPrice";
			},
			error:function(data){
				alert(data);
			}
		});
		
	});
	
	
	
	//询价前台验证-立即验证（新加zhm）
	$("#immediately").click(function(){
		
		var inquiryNo = $("#inquiryNo").val();
		
		if( inquiryNo == null ||  inquiryNo == "" ){
			$(".errorMsg").html("询价编码必填！");
			return;
		};
		var inquiryName = $("#inquiryName").val();
		if( inquiryName == null ||  inquiryName == "" ){
			$(".errorMsg").html("询价名称必填！");
			return;
		};
		var beginDate = $("#beginDate1").val();
		if( beginDate == null ||  beginDate == "" ){
			$(".errorMsg").html("询价日期必填！");
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
		//询价截至时间、交货时间必须大于询价时间
		var start=new Date(beginDate); 
		var end1=new Date(endDate);  
		var end2=new Date(deliveryDate);  
		if(start > end1){
			$(".errorMsg").html("询价截止时间必须大于询价时间！");
			return;
		}
		if(start > end2){
			$(".errorMsg").html("交货时间必须大于询价时间！");
			return;
		}
		
		if($("input[name='nums']").length == 0){
			$(".errorMsg").html("请至少选择一件物品进行询价");
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
		$("input[name='deliveryDates']").each(function(){
			if($(this).val() == ""){
				$(".errorMsg").html("明细中交货日期必填！");
				throw "明细中交货日期必填！";
			}
		});
		//跑后台创建询价信息
		$(".errorMsg").html("");
		createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/immediately",
			data:$("#inquiryForm").serializeArray(),
			success: function(data){
				alert(data);
				window.location="requestPrice";
			},
			error:function(data){
				alert(data);
			}
		});
		
	});
	
	
	
	
	
	
	
	
	
	
	//直接弹出新窗口，选择物品
	$("#add").click(function(){
		//将已经选择的物品id记录下
		var itemId = "";
		$("input[name='matCd']").each(function(e){
			if(itemId == ""){
				itemId = $(this).val();
			}else{
				itemId = itemId+ ","+$(this).val();
			}
		});
		window.open('productsListPrice?cid=&itemId=','Derek','width='+ (window.screen.availWidth-10)+',height='+(window.screen.availHeight-30)+ ',top=0,left=0,resizable=yes,status=yes,menubar=no,scrollbars=no');
    });
	
	//删除新增加的tr
	$(document).on("click", ".delete", function(e) {
		if(confirm("确定删除？")){
			//将这条记录的物料id放到隐藏域中，后台保存时需要将此物料的主单删除
			var matCd = $(this).parent().find("input:last").val();
			var flag = $(this).parent().find("input:first").val();
			var tempVar = $("#deleteId").val();
			if(flag == "u"){
				if(tempVar == ""){
					tempVar = matCd;
				}else{
					tempVar = tempVar+","+matCd;
				}
			}
			$("#deleteId").val(tempVar);
			$(this).parent().remove();
			var index = 1;
			$("ul[name='ul']").each(function(){
				$(this)[0].children[0].childNodes[0].data = index;
				index ++;
			});
		};
	});
	
	//跳往修改询价信息页面
	$(document).on("click", "#button_update", function(e) {
	
		var obj=document.getElementsByName('inquiryNo'); 
		  var s='';  
		  var checkNum = 0;
		  $('input[name="inquiryNo"]:checked').each(function(){    
			  checkNum += 1;
			  }); 
		  if(checkNum < 1){
			  alert("请选择一条询价信息进行修改!");
			  return;
		  }
		  if(checkNum > 1){
			  alert("您只能选择一条询价信息进行修改!");
			  return;
		  }
		  var status = "";
		  $('input[name="inquiryNo"]:checked').each(function(){ 
		    	 status  = $(this).parent().find("input:first").val();
		    	 s = $(this).val();
			}); 
		  if(status != "1"){
	    		alert("询价单【"+s+"】状态不是待发布！");
	    		return;
	    	}
		window.location="requestPriceUpdate"+"?inquiryNo="+s;
	});
	
	function chk(){    
		  var obj=document.getElementsByName('inquiryNo'); 
		  var s='';    
		  for(var i=0; i<obj.length; i++){    
		    if(obj[i].checked) s+=obj[i].value+',';   
		  }    
		  alert(s==''?'你还没有选择任何内容！':s);    
		} 
	
	function jqchk(){  //jquery获取复选框值    
		  var chk_value =[];    
		  $('input[name="inquiryNo"]:checked').each(function(){    
		   chk_value.push($(this).val());    
		  });    
		  alert(chk_value.length==0 ?'你还没有选择任何内容！':chk_value);    
		} 
	
		//删除
	$(document).on("click", "#button_delete", function(e) {
		var obj=document.getElementsByName('inquiryNo'); 
		  var ids="";    
		  for(var i=0; i<obj.length; i++){   
			  var x = 1;
		    if(obj[i].checked) {
		    	ids+=obj[i].value+",";   
		    	//状态不是1的不能删除
		    	var status = $(obj[i]).parent().find("input:first").val();
		    	if(status != "1"){
		    		alert("询价单【"+obj[i].value+"】状态不是待发布！");
		    		return;
		    	}
		    	x += 1;
		    }
		  }
		  if(ids==""){
			  alert("请选择需要删除的询价单！");
			  return;
		  }  
		  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认删除选中的询价信息？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/deleteInquiry",
			data:{
				ids: ids
				},
			success: function(result){
				alert("删除成功！")
				 window.location="requestPrice";
			},
			error:function(result){
				alert('删除失败！');
				window.location="requestPrice";
			}
		});
	});
	
	
	//询价
	$(document).on("click", "#button_commit", function(e) {
		var obj=document.getElementsByName('inquiryNo');
		
		
		  var ids="";    
		  for(var i=0; i<obj.length; i++){   
			  var x = 1;
		    if(obj[i].checked){ 
		    	//状态不是1的不能询价
		    	var status = $(obj[i]).parent().find("input:first").val();
		    	if(status != "1"){
		    		alert("询价单【"+obj[i].value+"】状态不是待发布！");
		    		return;
		    	}
		    	x += 1;
		    	ids+=obj[i].value+",";   
		    }
		    
		  }
		  if(ids==""){
			  alert("请选择需要询价的询价单！");
			  return;
		  }  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认进行询价？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/commitInquiry",
			data:{
				ids: ids
				},
			success: function(result){
				alert(result)
				 window.location="requestPrice";
			},
			error:function(result){
				alert(result);
				window.location="requestPrice";
			}
		});
	});
	
	
	//重新询价
	$(document).on("click", "#button_commit_re", function(e) {
		var obj=document.getElementsByName('inquiryNo'); 
		  var ids="";    
		  for(var i=0; i<obj.length; i++){   
			  var x = 1;
		    if(obj[i].checked){ 
		    	//状态不是1的不能询价
		    	var status = $(obj[i]).parent().find("input:first").val();
		    	if(status != "2" && status != "3"){
		    		alert("询价单【"+obj[i].value+"】状态不是报价中、已接收！");
		    		return;
		    	}
		    	x += 1;
		    	ids+=obj[i].value+",";   
		    }
		    
		  }
		  if(ids==""){
			  alert("请选择需要重新询价的询价单！");
			  return;
		  }  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认重新进行询价？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/commitInquiryRe",
			data:{
				ids: ids
				},
			success: function(result){
				alert(result)
				 window.location="requestPrice";
			},
			error:function(result){
				alert(result);
				window.location="requestPrice";
			}
		});
	});
	
	
	//接受询价
	$(document).on("click", "#button_commit_accept", function(e) {
		var obj=document.getElementsByName('inquiryNo'); 
		var checkCount = $("input[name='inquiryNo']:checked").length;
		  if(checkCount == 0){
			  alert("请选择一条询价单！");
			  return;
		  }
		  var ids="";    
		  $("input[name='inquiryNo']:checked").each(function(e){
			  var status = $(this).parent().find("input[name='status']").val();
	    	  if(status != "2"){
	    	  	alert("询价单【"+$(this).val()+"】状态不是报价中！");
	    	  	throw "询价单【"+$(this).val()+"】状态不是报价中！";
	    	  }
	    	  ids=$(this).val();  
		  });
		  if(ids==""){
			  alert("请选择需要确认价格的询价单！");
			  return;
		  }  
		  
		  var detailIds = "";
		  $("input[name='itemDetail']:checked").each(function(e){
			  if(detailIds == ""){
				  detailIds = $(this).val();
			  }else{
				  detailIds = detailIds + ","+$(this).val();
			  }
			  var ifPrice = $(this).parent().find("input[name='ifPrice']").val();
			  var detailName = $(this).parent().find("input[name='detailName']").val();
			  if(ifPrice != "1" ){
				  alert("【"+detailName+"】还未报价，请等卖家报价后再接收!");
				  throw "【"+detailName+"】还未报价，请等卖家报价后再接收!";
			  }
		  });
		  if(detailIds == ""){
			  alert("请等待卖家报价后至少选择一条信息接收询价单！");
			  return;
		  }
		  if(!confirm("是否确认价格？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/commitInquiryYes",
			data:{
				ids: ids,
				detailIds : detailIds
				},
			success: function(result){
				alert(result)
				 window.location="requestPrice";
			},
			error:function(result){
				alert(result);
				window.location="requestPrice";
			}
		});
	});

	
	//卖家确认询价信息
	$(document).on("click", "#button_commit_response", function(e) {
		var obj=document.getElementsByName('inquiryNo'); 
		  var ids="";    
		  for(var i=0; i<obj.length; i++){    
			  var x = 1;
		    if(obj[i].checked) {
		    	//状态不是1的不能询价
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
			  alert("请选择需要确认的询价单！");
			  return;
		  }  
		  ids = ids.substring(0, ids.length - 1);
		  if(!confirm("确认进行价格确认？")){
			  return;
		  }
		  createBlackBack();
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/responseCommitInquiry",
			data:{
				ids: ids
				},
			success: function(result){
				alert("价格确认成功！")
				 window.location="responsePrice";
			},
			error:function(result){
				alert('价格确认失败！');
				window.location="responsePrice";
			}
		});
	});
	
	
	//卖家设置询价价格前台验证
	$("#responseButtonSave").click(function(){
		var inquiryNo = $("#inquiryNo").val();
		var beginDate = $("#beginDate").val();
		//询价截至时间、交货时间必须大于询价时间
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
		//跑后台创建询价信息
		$(".errorMsg").html("");
		$.ajax({
			type: "post",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/resopnseModifyInquiry",
			data:$("#inquiryForm").serializeArray(),
			success: function(data){
				alert(data);
				window.location="responsePrice";
			},
			error:function(data){
				alert(data);
			}
		});
		
	});

	$(document).on("click", "#button_create_order", function(e) {
		var inquiryNo = "";
		var status = "";
		$("input[name='inquiryNo']:checked").each(function(e) {
			 status = $(this).parent().find("input[name='status']").val();
			if (status != "2" && status != "3") {
				alert("询价单【" + $(this).val() + "】状态不是报价中、已接收！");
				throw "询价单【" + $(this).val() + "】状态不是报价中、已接收！";
			}
			inquiryNo = $(this).val();
		});
		var d = new Date();
		var vYear = d.getFullYear()
		var vMon = d.getMonth() + 1
		var vDay = d.getDate()
		var s=vYear+"-"+(vMon<10 ? "0" + vMon : vMon)+"-"+(vDay<10 ? "0"+ vDay : vDay);
		if($("input[name='inquiryNo']:checked").length == 0){
			alert("请选择一条询价单进行下单！");
			return;
		}
		var ids = "";
		var nums = "";
		$("input[name='itemDetail']:checked").each(function(e){
			//判断物品是否在有效时间内
			var detailBeginDate  =  $(this).parent().find("input[name='detailBeginDate']").val();
			var detailEndDate  =  $(this).parent().find("input[name='detailEndDate']").val();
			var detailName  =  $(this).parent().find("input[name='detailName']").val()
			var detailStatus  =  $(this).parent().find("input[name='detailStatus']").val();
			if(new Date(s) < new Date(detailBeginDate) || new Date(s) > new Date(detailEndDate)){
				alert("物品【"+detailName+"】不在询价有效期内，无法下单!");
				throw "物品【"+detailName+"】不在询价有效期内，无法下单!";
			}
			if(detailStatus != "" && detailStatus != "1" && detailStatus != "null" ){
				alert("物品【"+detailName+"】价格未接收，无法下单!");
				throw "物品【"+detailName+"】价格未接收，无法下单!";
			}
			if($(this).parent().parent().find("input[name='number']").val() == ""){
				alert("请输入下单数量！");
				throw "请输入下单数量!";
			}
			if($(this).parent().parent().find("input[name='number']").val() < 1){
				alert("下单数量必须大于0！");
				throw "下单数量必须大于0!";
			}
			if($(this).parent().find("input[name='ifPrice']").val() != "1" ){
				alert("请等待卖家报价后再下单！");
				throw "请等待卖家报价后再下单!";
			}
			if(nums == "") {
				nums = $(this).parent().parent().find("input[name='number']").val();
				ids = $(this).val();
			}else{
				nums = nums +","+$(this).parent().parent().find("input[name='number']").val();
				ids = ids +","+$(this).val();
			}
		});
		if(ids == ""){
			alert("请至少选择一条物品进行下单！");
			return;
		}
		createBlackBack();
		//跑后台
		$.ajax({
			type: "POST",
			dataType: "html",
			url: $("#contextPath").val()+"/requestPriceJavaController/createOrderInfo",
			data:{
				inquiryNo : inquiryNo,
				nums : nums,
				detailIds :ids,
				status :status,
			},
			success: function(data){
				$("#contractNo").val(inquiryNo);
				document.getElementById("requestPriceForm").submit();
			},
			error : function(data) {
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
	
	
});

//卖家跳往修改询价信息页面
function responsePriceUpdate(obje) {

	var obj=document.getElementsByName('inquiryNo'); 
	  var s='';  
	  var checkNum = 0;
	  $('input[name="inquiryNo"]:checked').each(function(){    
		  checkNum += 1;
		  }); 
	  if(checkNum < 1){
		  alert("请选择一条询价信息进行修改!");
		  return;
	  }
	  if(checkNum > 1){
		  alert("您只能选择一条询价信息进行修改!");
		  return;
	  }
	  var status = "";
	  var endDate = "";
	  var inquiryNo = "";
	  $("input[name='inquiryNo']:checked").each(function(){
	    	 status  = $(this).parent().find("input[name='status']").val();
	    	 endDate  = $(this).parent().find("input[name='endDate']").val() + " 23:59:59";
	    	 inquiryNo = $(this).val();
		});
	  if(new Date(endDate) < new Date()){
		  alert("报价不在询价截止时间内，无法报价!");
		  return;
	  }
	  if(status != "2"  ){
    		alert("询价单【"+inquiryNo+"】状态不是报价中！");
    		return;
    	}
	  $('input[name="inquiryNo"]:checked').each(function(){    
		     s = $(this).val();    
		  });  
	window.location="responsePriceUpdate"+"?inquiryNo="+s;
}


function lookInquiryInfo(flag,userType){
	var checkNum = 0;
	$('input[name="inquiryNo"]:checked').each(function(){
		checkNum += 1;
	});
	if(checkNum < 1){
		alert("请选择一条询价信息进行查看!");
		return;
	}
	if(checkNum > 1){
		alert("您只能选择一条询价信息进行查看!");
		return;
	}
	$('input[name="inquiryNo"]:checked').each(function(){
		s = $(this).val();
	});
	window.location="requestPriceLook"+"?inquiryNo="+s+"&flag="+flag+"&userType="+userType;
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

function select_all(obj){
	if(obj.checked){
		$("input[name='inquiryNo']").each(function(e){
			$(this).prop("checked",true);
		}); 
	}else{
		$("input[name='inquiryNo']").each(function(e){
			$(this).prop("checked",false);
		});
	}
}

function goHistoryPage(flag){
	if(flag == 'request'){
		window.location="requestPrice";
	}else{
		window.location="responsePrice";
	}
}

function createShopListDiv(itemId,itemName, obj){
	//判断物品是否选择重复
	var itemIds = $("#itemNames").val();
	var itemIdArr = new Array();
	itemIdArr = itemIds.split(",");
	for(var i = 0; i< itemIdArr.length ;i++){
		if(itemName == itemIdArr[i]){
			alert("["+itemName+"]已经选择过，请重新选择！");
			return;
		}
	}
	createBlackBack();
	$.ajax({
        url: "getShopList",
        data: {
            "itemId": itemId,
            "itemName": itemName
        },
        type: 'post',
        dataType: 'html',
        success: function (data) {
            $("#itemSearchResult").html(data);
            showItemsDIV();
            
        }
    });
}

//展示物品弹出层
function showItemsDIV() {
    var Idiv = document.getElementById("Idiv");
    Idiv.style.display = "block";
}

function closeItemSearchDIV(){
    var Idiv = document.getElementById("Idiv");
    Idiv.style.display = "none";
    document.body.style.overflow = "auto"; // 恢复页面滚动条
    closeBlackBack();

}

//关闭加载层
function closeBlackBack() {
    var body = document.getElementsByTagName("body");
    var mybg = document.getElementById("mybg");
    body[0].removeChild(mybg);
}

//关闭供应商选择复选框
function closeDiv() {
    $("#Idiv").hide();
    closeBlackBack();
   document.body.style.overflow = "auto"; // 恢复页面滚动条
    
}

function commitShopId(itemId, itemName, obj){
	if($("input[name='"+itemId+"']:checked").length == 0){
		$(".errorMsg").html("请至少选择一个供应商!");
		return;
	}
	var itemIds="";
	var shopNames="";
	var shopIds = "";
	$("input[name='"+itemId+"']:checked").each(function(e){
		if(itemIds == ""){
			itemIds = $(this).val();
			shopNames = $(this).parent().find("input[name='shopName']").val();
			shopIds = $(this).parent().find("input[name='shopId']").val();
		}else{
			itemIds = itemIds + ","+$(this).val();
			shopNames = shopNames + ","+$(this).parent().find("input[name='shopName']").val();
			shopIds = shopIds + ","+$(this).parent().find("input[name='shopId']").val();
		}
	});
	window.opener.goodsDetailInfo(itemId, itemName, itemIds, shopNames , shopIds);
	$("#Idiv").hide();
    closeBlackBack();
	window.close();
}

function selectAll(obj, itemId){
	if(obj.checked){
		$("input[name='"+itemId+"']").each(function(e){
			$(this).prop("checked",true);
		}); 
	}else{
		$("input[name='"+itemId+"']").each(function(e){
			$(this).prop("checked",false);
		});
	}
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

function ischeckAll(){
	var isc = true;
	$("input[name='itemDetail']").each(function(e){
		if(!this.checked){
			isc = false;
		}
	});
	if(isc){
		$("#checkAllDetail_checkAll").prop("checked",true);
	}else{
		$("#checkAllDetail_checkAll").prop("checked",false);
	}
}

function ischeckAllInquiryNo(){
	var isc = true;
	$("input[name='inquiryNo']").each(function(e){
		if(!this.checked){
			isc = false;
		}
	});
	if(isc){
		$("#all_select").prop("checked",true);
	}else{
		$("#all_select").prop("checked",false);
	}
}

function onLookInfo(obj,inquiryNo,flag){
	//后台传值查询询价单明细信息
	$.ajax({
		type: "post",
		dataType: "html",
		url: $("#contextPath").val()+"/requestPriceJavaController/getDetaiInfo",
		data:{
			inquiryNo: inquiryNo,
			flag : flag
		},
		success: function(result){
			var result1 = result.split("%");
			//询价明细
			$("#information1 tbody").html('');
			if(result.split("%")[0] != "\""){
				var temp = "<tr class='font_cen bg_05'><td class='border-1 wid_50 font_cen'><input type='checkbox' checked id='checkAllDetail_checkAll' onclick='checkAllDetail(this);'/></td> <td class='border-1 wid_110 font_cen' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>供应商</td>"+
					"<td class='border-1 wid_110 font_cen' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>物品名称</td><td class='border-1 wid_110 font_cen' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>物品属性</td><td class='border-1 wid_110 font_cen'>交货日期</td><td class='border-1 wid_110 font_cen'>有效期(始)</td><td class='border-1 wid_110 font_cen'>有效期(止)</td><td class='border-1 wid_80 font_cen'>状态</td><td class='border-1 wid_80 font_cen'>数量</td>"+
					"<td class='border-1 wid_50 font_cen'>单价</td></tr>";
				$("#information1 tbody").html(temp+result.split("%")[0]);
			}
			//订单明细
			$("#information2 tbody").html('');
			if(result.split("%")[1] != "\""){
				var temp = "<tr class='font_cen bg_05'> <td class='border-1 wid_110'>询价编号</td>"+
					"<td class='border-1 wid_110'>订单编号</td><td class='border-1 wid_110'>订单状态</td>  <td class='border-1 wid_80'>总金额</td><td class='border-1 wid_80'>订单详情</td></tr>";
				$("#information2 tbody").html(temp+result.split("%")[1]);
			}else{
				var temp = "<tr class='font_cen bg_05'> <td class='border-1 wid_110'>询价编号</td>"+
					"<td class='border-1 wid_110'>订单编号</td><td class='border-1 wid_110'>订单状态</td>  <td class='border-1 wid_80'>总金额</td><td class='border-1 wid_80'>订单详情</td></tr>";
				$("#information2 tbody").html(temp);
			}
		},
		error:function(result){
			alert('faile');
		}
	});

}



