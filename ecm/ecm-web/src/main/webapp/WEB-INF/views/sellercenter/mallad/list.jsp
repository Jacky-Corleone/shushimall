<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<script type="text/javascript">
	function setvalues(themeId,themeName){
		$("#pdId").val(themeId);
		$("#pdName").val(themeName);
	}
</script>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<div class="container-fluid">
				<form:form id="mallThemeForm" modelAttribute="mallThemeDTO"
					action="${ctx}/sellercenter/mallTheme/adlist" method="post">
					<input id="pdId" type="hidden">
					<input id="pdName" type="hidden">
					<input id="pageNo" name="pageNo" type="hidden"
						value="${page.pageNo}" />
					<input id="pageSize" name="pageSize" type="hidden"
						value="${page.pageSize}" />
					<div class="row-fluid">
						<div class="span6">
							<label>地区</label>
							<form:select path="provinceCode">
								<form:option value="" label="请选择地区"></form:option>
								<form:options items="${addresList}" itemLabel="name"
									itemValue="code" />
							</form:select>
						</div>
						<div class="span6">
							<input class="btn  btn-primary" type="button" value="查询"
								onclick="page(1,10);" /> <input id="btnunset"
								class="btn  btn-primary" type="reset" value="重置"
								onclick="unset();" />
						</div>
					</div>
			</div>
			<div>

				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<tr>
						<th>编号</th>
						<th>频道名称</th>
						<th>频道类型</th>
						<th>省份</th>
						<th>城市</th>
<!-- 						<th>区县</th> -->
						<th>排序号</th>
					</tr>
					<c:forEach items="${page.list}" var="mallTheme" varStatus="s">
						<tr>
							<td>
								<input style="margin: 3px 3px" type="radio" name="pd" value="${mallTheme.id}" <c:if test="${mallTheme.id == themeId}"> checked="checked" </c:if>  onclick="setvalues('${mallTheme.id}','${mallTheme.themeName}')"/> 
								<c:out value="${s.count}" />
							</td>
							<td>${mallTheme.themeName }</td>
							<td><c:if test="${mallTheme.type == '1'}">首页</c:if> <c:if
									test="${mallTheme.type == '2'}">类目频道</c:if> <c:if
									test="${mallTheme.type == '3'}">地区频道</c:if></td>
							<td><c:forEach items="${addresList}" var="province">
									<c:if test="${province.code==mallTheme.provinceCode }">
							 			${province.name }
							 		</c:if>
								</c:forEach></td>
							<td><c:forEach items="${addressArray}" var="city">
									<c:if test="${city.code==mallTheme.cityCode }">
							 			${city.name }
							 		</c:if>
								</c:forEach></td>
							<%-- <td><c:forEach items="${addressArray}" var="village">
									<c:if test="${village.code==mallTheme.villageCode }">
							 			${village.name }
							 		</c:if>
								</c:forEach></td> --%>
							<td>${mallTheme.sortNum }</td>
						</tr>
					</c:forEach>
				</table>
				<div class="pagination">${page}</div>
			</div>
			</form:form>
		</div>
	</div>
</body>
</html>