<%--
  Created by IntelliJ IDEA.
  User: menpg
  Date: 2015/3/2
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@page import="com.camelot.maketcenter.dto.emums.IntegralTypeEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>积分列表</title>
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
	function changeTabs(obj, integralType) {
		$.jBox.tip("正在刷新列表，请稍等", 'loading', {
			opacity : 0
		});
		$("#pageNo").val(1);
		$("#pageSize").val(10);
		$("#integralType").val(integralType);
		doSearch();
	}
	function doSearch() {
		$("#searchForm").submit();
	}
</script>
<style>

h3{
    color:#000000;
    height: 46px;
    text-indent: 20px;
    font-size: 15px;
    font-family: \5FAE\8F6F\96C5\9ED1;
    font-weight: 500;
}.h3-current{
    width:160px;
    height: 36px;
    text-align: center;
}

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
			<form:form id="searchForm" modelAttribute="userIntegralTrajectoryDTO" action="${ctx}/member/integralList" method="post"
				style="display:none;">
				<input id="integralType" name="integralType" type="hidden" value="${userIntegralTrajectoryDTO.integralType}" />
				<input id="userId" name="userId" type="hidden" value="${userIntegralTrajectoryDTO.userId}" />
				<input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
				<input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
			</form:form>
			<!-- 标题开始 -->
			<ul class="nav nav-tabs">
				<li class="${type==null?'active':'' }">
					<a href="javascript:void(0);" onclick="changeTabs(this);"><h3 class="h3-current">积分明细</h3></a></li>
				<li class="${type==1?'active':'' }">
					<a href="javascript:void(0);" onclick="changeTabs(this,1);"><h3 class="h3-current">积分收入</h3></a></li>
				<li class="${type==2?'active':'' }">
					<a href="javascript:void(0);" onclick="changeTabs(this,2);"><h3 class="h3-current">积分支出</h3></a></li>
			</ul>
			<table id="contentTable"
				class="table table-striped table-bordered table-condensed td-cen hhtd">
				<thead>
					<tr>
						<th width="25%">序号</th>
						<th width="25%">积分变化</th>
						<th width="25%">日期</th>
						<th width="25%">来源</th>
					</tr>
				</thead>
				<tbody id="tabletbody">
					<c:forEach items="${page.list}" var="integral" varStatus="s">
						<tr>
							<td>${s.count}</td>
							<td>${integral.integralValue}</td>
							<td><fmt:formatDate value="${integral.usingTime}"
									pattern="yyyy-MM-dd HH:mm" type="both" dateStyle="long" /></td>
							<td><c:set var="integralTypeEnums" value="<%=IntegralTypeEnum.values()%>" />
								<c:forEach
									var="integralTypeEnum" items="${integralTypeEnums}">
									${integralTypeEnum.code==integral.integralType?integralTypeEnum.lable:''}
								</c:forEach></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="pagination">${page}</div>
		</div>
	</div>
</body>