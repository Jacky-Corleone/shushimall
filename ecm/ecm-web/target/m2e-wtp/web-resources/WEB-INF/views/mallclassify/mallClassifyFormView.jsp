<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新建文档分类</title>
	<meta name="decorator" content="default"/>
	
</head>
<body>
	<form:form id="inputForm" modelAttribute="mallClassifyDTO" method="post" action="${ctx}/mallClassify/save" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" >类型:</label>
			<div class="controls">
				<form:select path="type" >
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
			</div>
		</div>
		<div class="control-group">
            <div class="controls">
			<a class="btn btn-primary" type="button" href="${ctx}/mallClassify/list">取消</a>
            </div>
		</div>
	</form:form>
</body>
</html>