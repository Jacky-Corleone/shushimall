<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>
  	<title>tdk设置</title>
  	<meta name="decorator" content="default"/>
  	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript" src="${ctxStatic}/tdk/tdkInfo.js"></script>
    <link href="${ctxStatic}/kindeditor/themes/default/default.css"
	rel="stylesheet" />
<script src="${ctxStatic}/kindeditor/kindeditor-min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/kindeditor/lang/zh_CN.js"
	type="text/javascript"></script>
  </head>
  
  <body>
  	<form:form id="tdkForm" modelAttribute="tdkDTO" method="post" action="${ctx}/tdk/addTDK" class="form-horizontal">
  	<div class="control-group" hidden="hidden">
	  		<label class="control-label">ID</label>
	  		<div class="controls">
				<form:input path="id" htmlEscape="false" maxlength="20"/>
			</div>
	  	</div>
	  	<div class="control-group">
	  		<label class="control-label">标题</label>
	  		<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="200" title="最长长度为200"/>
			</div>
	  	</div>
  		<div class="control-group">
	  		<label class="control-label">页面描述</label>
	  		<div class="controls">
	  			<form:textarea path="description" cssStyle="width:600" htmlEscape="false" rows="5" maxlength="200" title="最长长度为200"/>
			</div>
	  	</div>
	  	<div class="control-group">
	  		<label class="control-label">关键字</label>
	  		<div class="controls">
	  			<form:textarea path="keywords" cssStyle="width:600" htmlEscape="false" rows="5" maxlength="200" title="最长长度为200"/>
			</div>
	  	</div>
	  	<div class="control-group">
            <div class="controls">
            <c:if test="${tdkDTO.title==null&&tdkDTO.description==null&&tdkDTO.keywords==null&&tdkDTO.createTime==null&&tdkDTO.modifyTime==null}">
            	<input id="addSubmit" class="btn btn-primary" value="添加" onclick="TDK.addTDK()"/>
            	<!-- <input id="addSubmit" class="btn btn-primary" type="submit" value="添加"/> -->
            </c:if>
			<c:if test="${tdkDTO.title!=null||tdkDTO.description!=null||tdkDTO.keywords!=null||tdkDTO.createTime!=null||tdkDTO.modifyTime!=null}">
            	<input id="addSubmit" class="btn btn-primary" value="修改" onclick="TDK.modifyTDK()"/>
            </c:if>
		    </div>
        </div>
  	</form:form>
  </body>
</html>
