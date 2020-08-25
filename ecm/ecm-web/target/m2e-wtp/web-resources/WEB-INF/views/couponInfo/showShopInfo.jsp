<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
    	<tr>
	        <th>店铺编号</th>
	        <th>店铺名称</th>
	        <th>商家编号</th>
        </tr>
	</thead>
	<tbody>  
	<c:forEach items="${page.list}" var="rangeDTO" varStatus="s">
	<tr>
		<td>${rangeDTO.shopId}</td>
		<td>${rangeDTO.shopName}</td>
		<td>${rangeDTO.sellerId}</td>
	</tr>
	</c:forEach>
	</tbody>
</table>
<div class="pagination">${page}</div>