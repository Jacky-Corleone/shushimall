<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>积分管理</title>
<meta name="decorator" content="default" />

<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		var selectJson;
		var $isView = "${isView }";
		if($isView==""){
			parsePlat();
		}
		typeChange('${integralConfigDTO.integralType}');
		if ($isView == 'view'|| $isView == 'edit') {
			$("#integralType").prop("disabled", true);
			$("#platformId").prop("disabled", true);
		}
		if($isView == 'view'){
			$(":input[name='addStep']").hide();
			$(":input[name='cancelButton']").hide();
			$(":input[name='getIntegral']").prop("disabled", true);
			$(":input[name='startPrice']").prop("disabled", true);
			$(":input[name='endPrice']").prop("disabled", true);
			$("#useIntegral").prop("disabled", true);
			$("#exchangeRate").prop("disabled", true);
		}
		$("#integralType").change(function() {
			typeChange(this.value);
			if(this.value>4){
				clickTypeSelect(this.value-2);
			}else{
				clickTypeSelect(this.value);
			}
		});
		$("input[name='endPrice']:last").live("focusout",function(){
			var $this = $(this);
			if(!(parseFloat($this.val())>parseFloat($("input[name='startPrice']:last").val()))){
				$.jBox.confirm("止价必须大于起价，请重新输入",'系统提示',function(v,h,f){
					if(v=='ok'){
						$this.val("");
						$this.focus();
					}
				});
			}else if(parseFloat($this.val())>parseFloat(getMaxPrice())){
				$this.val(formatNum(getMaxPrice(),2));
			}else{
				$this.val(formatNum($this.val(),2));
			}
		});
		$(".integral-rule:eq(0) input[name='getIntegral']:last").live("focusout",function(){
			var $this = $(this);
			if(!$this.val()){
				$.jBox.confirm("获取积分不可为空，请重新输入",'系统提示',function(v,h,f){
					if(v=='ok'){
						$this.focus();
					}
				});
				return;
			}
		});
		
	});
	function formatNum(num,fixed){
		return new Number(num).toFixed(fixed);
	}
	function getMaxPrice(){
		return 999999999.99;
	}
	function typeChange(val) {
		if (val == 1 || val == '') {
			$(".integral-rule").hide();
			$(".integral-rule:eq(0)").show();
			$(".rule-money").show();
			$(":input[name='startPrice']").addClass("required");
			$(":input[name='endPrice']").addClass("required");
			$(":input[name='getIntegral']").addClass("required");
			$("#getIntegral").removeClass("required");
			$("#useIntegral").removeClass("required");
			$("#exchangeRate").removeClass("required");
		} else if (val == 2) {
			$(".integral-rule").hide();
			$(".integral-rule:eq(1)").show();
			$(".rule-money").hide();
			$(".rule-money").removeClass("required");
			$(":input[name='startPrice']").removeClass("required");
			$(":input[name='endPrice']").removeClass("required");
			$(":input[name='getIntegral']").removeClass("required");
			$("#getIntegral").addClass("required");
			$("#useIntegral").removeClass("required");
			$("#exchangeRate").removeClass("required");
		} else if (val == 3) {
			$(".integral-rule").hide();
			$(".integral-rule:eq(2)").show();
			$(".rule-money").hide();
			$(".rule-money").removeClass("required");
			$(":input[name='startPrice']").removeClass("required");
			$(":input[name='endPrice']").removeClass("required");
			$(":input[name='getIntegral']").removeClass("required");
			$("#getIntegral").removeClass("required");
			$("#useIntegral").addClass("required");
			$("#exchangeRate").removeClass("required");
		} else if (val == 4) {
			$(".integral-rule").hide();
			$(".integral-rule:eq(3)").show();
			$(".rule-money").hide();
			$(".rule-money").removeClass("required");
			$(":input[name='startPrice']").removeClass("required");
			$(":input[name='endPrice']").removeClass("required");
			$(":input[name='getIntegral']").removeClass("required");
			$("#getIntegral").removeClass("required");
			$("#useIntegral").removeClass("required");
			$("#exchangeRate").addClass("required");
		} else {
			$(".integral-rule").hide();
			$(".integral-rule:eq(4)").show();
			$(".rule-money").hide();
			$(".rule-money").removeClass("required");
			$(":input[name='startPrice']").removeClass("required");
			$(":input[name='endPrice']").removeClass("required");
			$(":input[name='getIntegral']").removeClass("required");
			$(":input[name='getIntegral']:last").addClass("required");
			$("#useIntegral").removeClass("required");
			$("#exchangeRate").removeClass("required");
		}
	}
	function integralSave() {
		var isValide = $("#integralForm").valid();
		if (isValide) {
			if($('#integralType').val()==1){
				if(checkPrice()){
					$('#integralForm').submit();
				}
			}else{
				$('#integralForm').submit();
			}
		}
	};
	function editSave() {
		
		var isValide = $("#integralForm").valid();
		if (isValide) {
			$('#integralForm').attr("action","${ctx}/integral/edit");
			if($('#integralType').val()==1){
				if(checkPrice()){
					$('#integralForm').submit();
				}
			}else{
				$('#integralForm').submit();
			}
		}
	};
	
	function addRule() {
		var i = $(".rule-flag").val();
		var $endPrice = $(".integral-rule:eq(0) input[name='endPrice']:last").val();
		var $getIntegral = $(".integral-rule:eq(0) input[name='getIntegral']:last").val();
		if(!$endPrice||!$getIntegral){
			return;
		}
		if(parseFloat($endPrice)==parseFloat(getMaxPrice())){
			return;
		}
		$(".rule-flag").val(i + 1);
		var $html = "<div class='controls money-integral new-rule"+i+"' style='padding-top:10px;clear:both;'>"
				+ "<label class='rule-money' style='float: left;'>"
				+ "金额起价：<input name='startPrice' readOnly='readOnly' value='"+$endPrice+"' class='required'/>"
				+ "</label>"
				+ "<label class='rule-money' style='float: left;'>"
				+ "金额止价：<input name='endPrice' maxlength='12' onkeyup='numInput(this,2)' class='required'/>"
				+ "</label>"
				+ "<label style='float: left;'>"
				+ "获取积分比例：<input name='getIntegral' onkeyup='numInput(this,4)' class='required'/>"
				+ "</label>"
				+ "<input type='button' style='float: right;' readOnly='readonly' value='取消' onclick='removeRule("
				+ i + ")'/>" + "</div>";
		$(".integral-rule:eq(0)").append($html);
	}
	function removeRule(i) {
		$(".new-rule" + i).remove();
	}
	function removeLoopRule(i){
		$(".loop-rule" + i).remove();
	}
	function parsePlat() {
		$.ajax({
			url : "${ctx}/integral/getPlatId",
			type : "post",
			dataType : "JSON",
			success : function(data) {
				if(data.type.length==0){
					alert("没有可以添加积分设置！");
					location.href="${ctx}/integral/list";
					return;
				}
				selectJson = data.list;
				if(!isContains(1,data.type)){
					$("#integralType option[value='1']").remove();
				}
				if(!isContains(2,data.type)){
					$("#integralType option[value='2']").remove();
				}
				if(!isContains(3,data.type)){
					$("#integralType option[value='3']").remove();
				}
				if(!isContains(4,data.type)){
					$("#integralType option[value='4']").remove();
				}
				if(!isContains(5,data.type)){
					$("#integralType option[value='7']").remove();
				}
				if($("#integralType").val()>4){
					clickTypeSelect($("#integralType").val()-2);
				}else{
					clickTypeSelect($("#integralType").val());
				}
				typeChange($("#integralType").val());
				$('#integralType').select2();
			}

		});
	}
	
	function isContains(val,list){
		if(list.length < 1){
			return false;
		}
		var flag = false;
		$.each(list,function(i,v){
			if(val == v){
				flag = true;
			}
		});
		return flag;
	}
	
	function clickTypeSelect (v){
		var pHtml = "";
		$.each(selectJson,function(idx,ele){
			var flag0 = true ;
			var flag2 = true ;
			if(ele.integralType==v){
				if(ele.platformId==0&&flag0){
					flag0 = false;
					pHtml +="<option value = \""+ele.platformId+"\">舒适100平台</option>";
				}
				if(ele.platformId==2&&flag2){
					flag2 = false;
					pHtml +="<option value = \""+ele.platformId+"\">绿印平台</option>";
				}
			}
		});
		$("#platformId").html(pHtml);
		$('#platformId').select2();
	}
	
// 	function priceTrim() {
// 	    $("#integralForm :input").bind("keyup", function () {
// 	        this.value = this.value.replace(/[^\d.]/g, "");
// 	        必须保证第一个为数字而不是.
// 	        this.value = this.value.replace(/^\./g, "");
// 	        保证只有出现一个.而没有多个.
// 	        this.value = this.value.replace(/\.{2,}/g, ".");
// 	        保证.只出现一次，而不能出现两次以上
// 	        this.value = this.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
// 	    });
// 	}
	function numInput(obj,length){
		var lengthEval = eval("/^\\d*(?:\\.\\d{0,"+length+"})?$/");
		if(obj.value==obj.value2)
			return;
		if(length == 0 && obj.value.search(/^\d*$/)==-1)
			obj.value=(obj.value2)?obj.value2:'';
		else if(obj.value.search(lengthEval)==-1)
			obj.value=(obj.value2)?obj.value2:'';
		else obj.value2=obj.value;
	}
	
	function checkPrice(){
		var flag = false;
		var startPrice = $(".integral-rule:eq(0) input[name='startPrice']");
		var endPrices = $(".integral-rule:eq(0) input[name='endPrice']");
		var getIntegral = $(".integral-rule:eq(0) input[name='getIntegral']");
		for(var i = 0 ; i < startPrice.length ; i++ ){
			if(!getIntegral[i].value){
				$.jBox.error("获取积分值不可为空，请再次检查");
				$(".integral-rule:eq(0) input[name='getIntegral']eq("+i+")").focus();
				break;
			}
			if(i == 0){
				$(".integral-rule:eq(0) input[name='startPrice']:first").val(formatNum(0, 2));
				if(startPrice.length == 1){
					$.jBox.confirm("价格区间最大值必须为"+getMaxPrice()+",若确认将自动修改价格区间最大值为"+getMaxPrice(),"系统提示",function(v,h,f){
						if(v=='ok'){
							flag = true;
							$(".integral-rule:eq(0) input[name='endPrice']:last").val(formatNum(getMaxPrice(), 2));
							$('#integralForm').submit();
						}
					});
				}
				continue;
			}else if(parseFloat(endPrices[i-1].value)==parseFloat(startPrice[i].value)){
			}else{
				$.jBox.error("价格区间不连续，请再次检查");
				$(".integral-rule:eq(0) input[name='endPrice']:eq("+i-1+")").focus();
				break;
			}
			if(i == startPrice.length - 1){
				if(parseFloat(endPrices[i].value)==parseFloat(getMaxPrice())){
					flag = true;
				}else{
					$.jBox.confirm("价格区间最大值必须为"+getMaxPrice()+",若确认将自动修改价格区间最大值为"+getMaxPrice(),"系统提示",function(v,h,f){
						if(v=='ok'){
							flag = true;
							$(".integral-rule:eq(0) input[name='endPrice']:last").val(formatNum(getMaxPrice(), 2));
							$('#integralForm').submit();
						}
					});
				}
			}
		}
		return flag;
	}
</script>
</head>
<body>

	<form:form id="integralForm" modelAttribute="integralConfigDTO"
		method="post" action="${ctx}/integral/save" class="form-horizontal">
		<form:hidden path="configId" value="${integralConfigDTO.configId }" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label" for="integralType">积分配置类型:</label>
			<div class="controls">
			<c:if test="${ isView==null}">
				<form:select id="integralType" path="integralType" class="input-medium" value="${integralConfigDTO.platformId }">
                   <form:option value="1" label="金额返还积分"/>
                   <form:option value="2" label="评价获取积分"/>
                   <form:option value="3" label="订单使用积分"/>
                   <form:option value="4" label="积分兑换金额"/>
                   <form:option value="7" label="评价+晒单获取积分"/>
				</form:select>
			</c:if>
			<c:if test="${ isView=='edit'||isView=='view'}">
			<input name="integralType" type="hidden" value="${integralConfigDTO.integralType }"/>
			 <form:select id = "integralType" path="integralType" class="input-medium" value="${integralConfigDTO.integralType }" >
                   <form:option value="1" label="金额返还积分"/>
                   <form:option value="2" label="评价获取积分"/>
                   <form:option value="3" label="订单使用积分"/>
                   <form:option value="4" label="积分兑换金额"/>
                   <form:option value="7" label="评价+晒单获取积分"/>
               </form:select>
			</c:if>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="platformId">平台类型:</label>
			<div class="controls">
			<c:if test="${ isView==null}">
				<form:select id="platformId" path="platformId" class="input-medium" value="${integralConfigDTO.platformId }">
                    <form:option value="0" label="舒适100平台"/>
<%--                     <form:option value="2" label="绿印平台"/> --%>
				</form:select>
			</c:if>
			<c:if test="${ isView=='edit'||isView=='view'}">
			<input name="platformId" type="hidden" value="${integralConfigDTO.platformId }"/>
			<form:select id="platformId" path="platformId"  class="input-medium" value="${integralConfigDTO.platformId }">
                    <form:option value="0" label="舒适100平台"/>
<%--                     <form:option value="2" label="绿印平台"/> --%>
             </form:select>
			</c:if>
			</div>
		</div>
		
		<div class="control-group integral-rule">
			<label class="control-label" for="getIntegral">获取规则:</label>
			<input class="rule-flag" type="hidden" value="1" />
			<c:if test="${ isView==null}">
				<input class="btn btn-warning" 
						style="margin-left:20px;margin-bottom:10px" value='增加阶梯规则'
						onclick="addRule()" readOnly="readOnly" name="addStep"/>
				<div class="controls">
					<label class="rule-money" style="float: left;"> 
						金额起价：<input name="startPrice" value="0.00" readOnly="readOnly" /> 
					</label> 
					<label class="rule-money" style="float: left;"> 
						金额止价：<input name="endPrice" value="999999999.99"/> 
					</label> 
					<label style="float: left;"> 
						获取积分：<input name="getIntegral"/> 
					</label>
				</div>
			</c:if>
			<c:if test="${ isView=='edit'|| isView=='view'}">
				<input class="btn btn-warning" readOnly="readonly"
						style="margin-left:20px;margin-bottom:10px" value='增加阶梯规则'
						onclick="addRule()" name="addStep"/>
				<c:forEach items="${mapList }" var="item"  varStatus="status">
					<div class="controls money-integral loop-rule${status.index}" style="padding-top:10px;clear:both;">
					<label class="rule-money" style="float: left;"> 
						金额起价：<input name="startPrice" value="${item.startPrice }" readOnly="readonly" onkeyup='numInput(this,2)' /> 
					</label> 
					<label class="rule-money" style="float: left;"> 
						金额止价：<input name="endPrice" value="${item.endPrice }" maxlength="12" onkeyup='numInput(this,2)'  /> 
					</label> 
					<label style="float: left;">
						 获取积分比例：<input name="getIntegral" onkeyup='numInput(this,4)' value="${item.getIntegral }" /> 
					</label>
					<c:if test="${status.index != 0 }">
						<input type='button' style='float: right;' value='取消' onclick="removeLoopRule('${status.index}')" name="cancelButton"/><br>
					</c:if>
					</div>
				</c:forEach>
			</c:if>
		</div>
		<div class="control-group integral-rule" hidden="hidden">
			<label class="control-label" for="getIntegral">获取规则:</label>
			<div class="controls">
				<label> 评价可获得积分：<form:input path="getIntegral" id="getIntegral" name="getIntegral"
						value="${integralConfigDTO.getIntegral }"  onkeyup='numInput(this,0)'/> </label>
			</div>
		</div>
		<div class="control-group integral-rule" hidden="hidden">
			<label class="control-label" for="useIntegral">使用规则:</label>
			<div class="controls">
				<label> 每个订单可使用积分：<form:input path="useIntegral"
						value="${integralConfigDTO.useIntegral }"  onkeyup='numInput(this,0)'/> </label>
			</div>
		</div>
		<div class="control-group integral-rule" hidden="hidden">
			<label class="control-label" for="exchangeRate">兑换规则:</label>
			<div class="controls">
				<label> 每个积分兑换的金额：<form:input path="exchangeRate"
						value="${integralConfigDTO.exchangeRate }"  onkeyup='numInput(this,2)'/> </label>
			</div>
		</div>
		<div class="control-group integral-rule" hidden="hidden">
			<label class="control-label" for="getIntegral">获取规则:</label>
			<div class="controls">
				<label> 评价+晒单可获得积分：<form:input path="getIntegral" name="getIntegral"
						value="${integralConfigDTO.getIntegral }"  onkeyup='numInput(this,0)'/> </label>
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
			<c:if test="${ isView==null}">
				<input class="btn btn-primary" type="button"
					onclick="integralSave()" value="保存" />&nbsp; <input
					class="btn btn-primary" type="button" onclick="history.go(-1)"
					value="取消" />&nbsp;
			</c:if>
			<c:if test="${ isView=='view'}">
				 <input
					class="btn btn-primary" type="button" onclick="history.go(-1)"
					value="取消" />&nbsp;
			</c:if>
			<c:if test="${ isView=='edit'}">
				 <input class="btn btn-primary" type="button"
					onclick="editSave()" value="保存" />&nbsp; <input
					class="btn btn-primary" type="button" onclick="history.go(-1)"
					value="取消" />&nbsp;
			</c:if>
			</div>
		</div>
	</form:form>
</body>
</html>