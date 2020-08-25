<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>快递管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/express/list">快递列表</a></li>
		<shiro:hasPermission name="basecenter:express:edit">
			<li><a href="${ctx}/express/form">快递添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="dictionaryDTO"
		action="${ctx}/express/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="rows" type="hidden"
			value="${page.pageSize}" />
		<div class="row-fluid" style="margin-top: 10px;">
			<div class="span3">
				<label>名称：&nbsp;</label>
				<form:input path="name" htmlEscape="false" maxlength="50"
					class="input-medium" />
			</div>
			<div class="span3">
				<label>编码：&nbsp;</label>
				<form:input path="code" htmlEscape="false" maxlength="30"
					class="input-medium" />
			</div>
		</div>
		<div class="row-fluid" style="margin-top: 10px;">
			<div class="span4">
				<label class="label-left control-label"></label> <input
					id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
				<input class="btn  btn-primary" id="btncancle" onclick="unset();"
					type="button" value="重置">

			</div>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>编码</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="dictionaryDTO">
				<tr>
					<td><a href="${ctx}/express/form?id=${dictionaryDTO.id}">
							${dictionaryDTO.name} </a></td>
					<td>${dictionaryDTO.code}</td>
					<td><a href="${ctx}/express/form?id=${dictionaryDTO.id}">修改</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>