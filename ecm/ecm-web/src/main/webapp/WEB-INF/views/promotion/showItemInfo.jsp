<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<table id="treeTable" style="text-align: center;" class="table table-striped table-bordered table-condensed td-cen hhtd">
	<thead>
	    <th style="width: 240px;">商品信息</th>
		<th>商品编号</th>
		<th>报价</th>
		<th style="width: 240px;">店铺名称</th>
		<th>商品状态</th>
	</thead>
	<tbody>
		<c:forEach var="itemInfos" items="${page.list }">
			<c:set value="${ fn:split(itemInfos, ',') }" var="itemInfo" />
			<tr>
				<c:forEach var="info" items="${itemInfo }" varStatus="s">
					<td>${info }</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="pagination">${page}</div>