<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>快递管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/express/list">快递列表</a></li>
		<li class="active"><a
			href="${ctx}/express/form?id=${dictionary.id}">快递<shiro:hasPermission
					name="basecenter:express:edit">${not empty dictionary.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="basecenter:express:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form id="inputForm" action="${ctx}/express/save"
		method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${dictionary.id }" />
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">名称：</label>
			<div class="controls">
				<input name="name" htmlEscape="false" maxlength="50" type="text"
					value="${dictionary.name }" class="input-xlarge " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">编码：</label>
			<div class="controls">
				<input name="code" htmlEscape="false" maxlength="30" type="text"
					value="${dictionary.code }" class="input-xlarge " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">类型：</label>
			<div class="controls">
				<select name="type" style="width: 140px;" >
					<c:choose>
						<c:when test="${dictionary.type != null }">
							<option value="${dictionary.type }">${dictionary.type }</option>
						</c:when>
						<c:otherwise>
							<option value="delivery">delivery</option>
						</c:otherwise>
					</c:choose>
				</select>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="保 存" />&nbsp; <input id="btnCancel" class="btn" type="button"
				value="返 回" onclick="history.go(-1)" />
		</div>
	</form>
</body>
</html>