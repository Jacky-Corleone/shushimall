<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新建文档分类</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		function mallClassifyAdd(val) {
			$("#inputForm").validate();
			var id = $("#id").val();
			if(id == ""){
				$("#inputForm").submit();
			}else{
				$("#inputForm").attr("action","${ctx}/mallClassify/edit").submit();
			}
		}

	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="mallClassifyDTO" method="post" action="${ctx}/mallClassify/save" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" >类型:</label>
			<div class="controls">
				<form:select path="type" class="required">
					<form:option value="" label="请选择"/>
					<c:forEach items="${typeList}" var="typeVal">
						<c:forEach var="entry" items="${typeVal}">
							<form:option value="${entry.key }" label="${entry.value }"/>
						</c:forEach>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="title">分类名称:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="40" class="required"/>
				<span>限20个字符以内</span>
			</div>
		</div>
		<div class="control-group">
            <div class="controls">
				<input class="btn btn-primary" type="button" onclick="mallClassifyAdd()" value="确定"/>&nbsp;
				<a class="btn btn-primary" type="button" href="${ctx}/mallClassify/list">取消</a>
            </div>
		</div>
	</form:form>
</body>
</html>