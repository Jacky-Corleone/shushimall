<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新建文档分类</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function addAndUpdate(val) {
			$("#inputForm").validate();
			var growthValue = $("#growthValue").val();
			if(NumberCheck(growthValue)){
				$("#inputForm").submit();
			}else{
				alert("请输入正确格式的值!");
			}
			
		}

		 function NumberCheck(num) 
		 {
		  var re=/^\d*\.{0,1}\d{0,2}$/;
		  return re.exec(num) != null;
		 }
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="growthDTO" method="post" action="${ctx}/growth/save" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<c:if test="${growthDTO.id != 0 && growthDTO.id != null}">
		<form:hidden path="type"/>
		</c:if>
		<c:if test="${growthDTO.id == 0 || growthDTO.id == null}">
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
		</c:if>
		<div class="control-group">
		  
			<label class="control-label" >
				<c:choose>
					<c:when test="${growthDTO.type=='2'}">
					  百分比:
					</c:when>
					<c:otherwise>
					成长值:
					</c:otherwise>
				</c:choose>
			</label>
			<div class="controls">
				<form:input path="growthValue" htmlEscape="false" maxlength="40" class="required"/>
			</div>
		</div>
		<div class="control-group">
            <div class="controls">
            <input class="btn btn-primary" type="button" onclick="addAndUpdate()" value="确定"/>&nbsp;
			<a class="btn btn-primary" type="button" href="${ctx}/growth/list">取消</a>
            </div>
		</div>
	</form:form>
</body>
</html>