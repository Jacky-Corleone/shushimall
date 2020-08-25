<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
		var onClickBack = function(){
			var id  = $("#leftTreeSelectNode").val();
			var url = "${ctx}/tag/treeselectadresstab?parentCode="+id;
			window.location.href = url;
			//window.parent.frames["iframeTreeWithTab"].location.href = url;
			
		}
			
		
	</script>
</head>
<body>
    <input id="test1" type="hidden" value="${resultW.flag}">
    <input id="test2" type="hidden" value="${resultW.errCode}">
    <input type="hidden" id="leftTreeSelectNode" name="leftTreeSelectNode" value="${leftTreeSelectNode}">
	<form:form id="inputForm" modelAttribute="addressBaseDTO" action="${ctx}/tag/addressbase/save" method="post" class="form-horizontal">
		<tags:message content="${resultW.message}"/>
		<input type="hidden" id="id" name="id" value="${addressBaseDTO.id}">
		<input type="hidden" id="code" name="code" value="${addressBaseDTO.code}">
		<input type="hidden" id="parentcode" name="parentcode" value="${addressBaseDTO.parentcode}">
		
		<input type="hidden" id="yn" name="yn" value="1">
		<div class="control-group">
			<label class="control-label">地域名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
	<%-- 	<div class="control-group">
			<label class="control-label">地域编码:</label>
			<div class="controls">
			
				<c:choose>
					<c:when test="${addressBaseDTO.code == null}">
						<form:input path="code" htmlEscape="false" maxlength="50" class="required" />
					</c:when>
					<c:when test="${resultW.errCode != 1 && resultW.flag == 'add' }">
						<form:input path="code" htmlEscape="false" maxlength="50" class="required" />
					</c:when>
					<c:otherwise>
							<form:input path="code" htmlEscape="false" maxlength="50" readonly ="true"/>
					</c:otherwise>
				</c:choose>
				   
				
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">级别:</label>
			<div class="controls">
				<form:input path="level" htmlEscape="false"  maxlength="50" class="required" readonly="true"/>
			</div>
		</div>
		
		<%-- <div class="control-group">
			<label class="control-label">启用状态:</label>
			<div class="controls">
			<c:choose>
				<c:when test="${addressBaseDTO.yn ==0 && addressBaseDTO.code != null}">
				
						<input type="radio" name="yn" value="1" >启用
						<input type="radio" name="yn" value="0" checked="checked">禁用
				
				</c:when>
				<c:otherwise>
						<input type="radio" name="yn" value="1" checked="checked"> 启用
						<input type="radio" name="yn" value="0" > 禁用
				
				</c:otherwise>
			</c:choose>
			
			</div>
		</div> --%>
		
		<div class="control-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="btnback" class="btn btn-primary" type="button" onclick="onClickBack();" value="返回"/>
		</div>
	</form:form>
	<c:if test="${resultW.errCode == 1 && resultW.flag == 'add'}" >
	
			<script type="text/javascript">
					var parentid = "${addressBaseDTO.parentcode}"
					var addCode = "${addCode}"
					var newNode  = {id:addCode,name:"${addressBaseDTO.name}"};
					window.parent.frames["iframeTree"].appendNode(parentid,newNode);
					//var iframe = window.parent.document.getElementById("iframeTree");
					
			
			</script>
			
	</c:if>
	
	<c:if test="${resultW.errCode == 1 && resultW.flag =='update'}">
	
			<script type="text/javascript">
					var id = "${addressBaseDTO.code}";
					var newname = "${addressBaseDTO.name}";
					var upNode  = {id:"${addressBaseDTO.code}",name:"${addressBaseDTO.name}"};
					window.parent.frames["iframeTree"].updateNode(id,newname);
			
			</script>
			
	</c:if>
	
</body>
</html>