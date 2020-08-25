<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我猜你喜欢列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/mallAdvertise/indexwc").submit();
	    	return false;
	    }
        function add(){
            var url = "${ctx}/mallAdvertise/toEditwc";
            parent.openTab(url,"我猜你喜欢新增","我猜你喜欢新增");
        }
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="dto" action="${ctx}/mallAdvertise/indexwc/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="addressType" name="addressType" type="hidden" value="${addressType}"/>
		<div>
			<label>类型：</label>
			<form:select path="adType" cssClass="input-medium">
				<form:option value="5" label="我猜你喜欢"></form:option>
			</form:select>
			<label>关键字：</label>
			<form:input path="adTitle" htmlEscape="false" maxlength="50" cssClass="input-medium"/>
            <label>状态：</label>
            <form:select path="status" cssClass="input-medium">
                <form:option value="" label="全部"></form:option>
                <form:option value="1" label="上架"></form:option>
                <form:option value="0" label="下架"></form:option>
            </form:select>
		</div>
		<div style="margin-top:8px; margin-left: 53px;" >
			<form:select path="timeFlag" cssClass="input-medium">
				<form:option value="1" label="创建时间"/>
				<form:option value="2" label="发布时间"/>
                <form:option value="3" label="下架时间"/>
 			</form:select>
 			<form:input id="d4311" path="startTime" class="Wdate input-medium" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>到
 			<form:input path="endTime" class="Wdate input-medium" onfocus="WdatePicker({minDate:'\#F{\$dp.\$D(\\'d4311\\')}',dateFmt:'yyyy-MM-dd'})"/>
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
            <input id="btnSubmit1" class="btn btn-primary" type="button" value="新增" onclick="add()" />
        </div>
	</form:form>

	<tags:message content="${message}"/>
	<table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 4%">序号</th>
				<th style="width: 4%">广告名称</th>
				<th style="width: 10%">指向链接</th>
				<th style="width: 4%">类型</th>
				<th style="width: 2%">顺序号</th>
				<th style="width: 5%">创建时间</th>
				<th style="width: 5%">发布时间</th>
                <th style="width: 5%">下架时间</th>
                <th style="width: 2%">状态</th>
				<th style="width: 4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="mallAd" varStatus="s">
				<tr>
					<td><c:out value="${s.count}" /></td>
					<td>${mallAd.title}</td>
					<td><a href="${mallAd.adURL}" target="_blank">${mallAd.adURL}</a></td>
					<td>
						热销
					</td>
					<td>${mallAd.sortNumber}</td>
					<td>
						<fmt:formatDate value="${mallAd.created}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" />
					</td>
					<td>
						<fmt:formatDate value="${mallAd.startTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" />
					</td>
                    <td>
                        <fmt:formatDate value="${mallAd.endTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" />
                    </td>
                    <td>
                        <c:if test="${ mallAd.status == 1 }">
                            上架
                        </c:if>
                        <c:if test="${ mallAd.status == 0 or mallAd.status == null }">
                            下架
                        </c:if>
                    </td>
						<td>
							<a href="${ctx}/mallAdvertise/toEditwc?id=${mallAd.id}&pageNo=${page.pageNo}&pageSize=${page.pageSize}&adType=${dto.adType}&adTitle=${dto.adTitle}&status=${dto.status}&timeFlag=${dto.timeFlag}&startTime=<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd'/>&endTime=<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd'/>">修改</a>
							<c:if test="${ mallAd.status == 1 }">
								<a href="${ctx}/mallAdvertise/publishwc?id=${mallAd.id}&status=0&pageNo=${page.pageNo}&pageSize=${page.pageSize}&statusAdType=${dto.adType}&statusAdTitle=${dto.adTitle}&statusStatus=${dto.status}&statusTimeFlag=${dto.timeFlag}&statusStartTime=<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd'/>&statusEndTime=<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd'/>" >下架</a>
							</c:if>
							<c:if test="${ mallAd.status == 0 or mallAd.status == null }">
								<a href="${ctx}/mallAdvertise/publishwc?id=${mallAd.id}&status=1&pageNo=${page.pageNo}&pageSize=${page.pageSize}&statusAdType=${dto.adType}&statusAdTitle=${dto.adTitle}&statusStatus=${dto.status}&statusTimeFlag=${dto.timeFlag}&statusStartTime=<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd'/>&statusEndTime=<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd'/>" >上架</a>
							</c:if>
						</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>



</body>
</html>