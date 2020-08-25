<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<table id="treeTable"
	class="table table-striped table-bordered table-condensed td-cen hhtd">
	<tr>
		<th>序号</th>
		<th>活动名称</th>
		<th>活动编码</th>
		<th>采购单位</th>
		<th>联系人</th>
		<th>联系电话</th>
		<th>联系地址</th>
		<th>预期采购数量</th>
		<th>预期采购价格</th>
	</tr>

	<c:forEach items="${page.list}" var="querySignUpInfoDTO"
		varStatus="s">
		<tr>
			<td><c:out value="${s.count}" />
			</td>
			<td>${querySignUpInfoDTO.activityName }</td>
			<td>${querySignUpInfoDTO.activitesDetailsId }</td>
			<td>${querySignUpInfoDTO.enterpriseName }</td>
			<td>${querySignUpInfoDTO.enterpriseLinkman }</td>
			<td>${querySignUpInfoDTO.phoneNo }</td>
			<td>${querySignUpInfoDTO.address }</td>
			<td>${querySignUpInfoDTO.enterpriseEstimateNum }</td>
			<td>${querySignUpInfoDTO.enterpriseEstimatePrice }</td>
		</tr>
	</c:forEach>
</table>
<div class="pagination">${page}</div>


