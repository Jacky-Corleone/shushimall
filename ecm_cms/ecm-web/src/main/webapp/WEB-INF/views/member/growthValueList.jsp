<%--
  Created by IntelliJ IDEA.
  User: menpg
  Date: 2015/3/2
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@page import="com.camelot.openplatform.common.enums.GrowthTypeEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>成长值列表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
	function page(n, s) {
		$.jBox.tip("正在刷新列表，请稍等", 'loading', {
			opacity : 0
		});
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		doSearch();
	}
	function doSearch() {
		$("#searchForm").submit();
	}
</script>
<style>

table.td-cen th, .td-cen td {
	text-align: center;
}

.hhtd td {
	word-wrap: break-word;
	word-break: break-all;
}
</style>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<tags:message content="${message}" />
			<ul style="list-style-type: none;">
				<li>总成长值：${user.growthValue}</li>
				<li>会员等级：${user.vipLevel}</li>
			</ul>
			<form:form id="searchForm" modelAttribute="userDTO" action="${ctx}/member/growthValueList" method="post">
				<input id="uid" name="uid" type="hidden" value="${userDTO.uid}" />
				<input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
				<input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
			</form:form>
			<table id="contentTable"
				class="table table-striped table-bordered table-condensed td-cen hhtd">
				<thead>
					<tr>
						<th width="25%">序号</th>
						<th width="25%">获取方式</th>
						<th width="25%">获取成长值</th>
						<th width="25%">获取时间</th>
					</tr>
				</thead>
				<tbody id="tabletbody">
					<c:forEach items="${page.list}" var="growthVal" varStatus="s">
						<tr>
							<td>${s.count}</td>
							<td><c:set var="growthTypeEnums" value="<%=GrowthTypeEnum.values()%>" />
								<c:forEach var="growthTypeEnum" items="${growthTypeEnums}">
									${growthTypeEnum.id==growthVal.type?growthTypeEnum.name:''}
								</c:forEach></td>
							<td>${growthVal.growthValue}</td>
							<td><fmt:formatDate value="${growthVal.createTime}"
									pattern="yyyy-MM-dd HH:mm" type="both" dateStyle="long" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="pagination">${page}</div>
		</div>
	</div>
</body>