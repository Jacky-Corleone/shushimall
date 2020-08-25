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
			$("#searchForm").attr("action","${ctx}/station/mallAdvertise/indexwc?themeType=${dto.themeType}").submit();
	    	return false;
	    }
        function edit(id){
            var url = "${ctx}/station/mallAdvertise/toEditwc?id="+id+"&themeType=${dto.themeType}";
            parent.openTab(url,"修改热销","修改热销");
        }
        function add(){
            var url = "${ctx}/station/mallAdvertise/toEditwc?themeType=${dto.themeType}";
            parent.openTab(url,"新增热销","新增热销");
        }
        function unset(){ //重置
            $(':input','#searchForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $("#pageNo").val(1);
            $("#pageSize").val(10);
            $("#searchForm").submit();
        }

		function deleteAdvertise(AdvertiseId) {
			var url = "${ctx}/mallAdvertise/delete?id=" + AdvertiseId;
			if (confirm("确认删除？")) {
				$.ajax({
					url: url,
					type: "get",
					dataType: "json",
					success: function (data) {
						console.log(data);
						if (data.success) {
							$.jBox.success("操作成功!", '提示', {
								closed: function () {
									$("#searchForm").submit();
								}
							});
						} else {
							$.jBox.info("删除出错,请稍后再试!");
						}
					},
					error: function (xmlHttpRequest, textStatus, errorThrown) {
						$.jBox.info("系统错误！请稍后再试！");
					}

				})
			}
		}

	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="dto" action="${ctx}/station/mallAdvertise/indexwc/?themeType=${dto.themeType}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>子站:</label>
			<form:select id="themeId" path="themeId">
				<form:option value="" label="请选择子站"/>
				<form:options items="${themeList}"  itemLabel="themeName"  itemValue="id" />
			</form:select>
			<label>类型：</label>
			<form:select path="adType" cssClass="input-medium">
				<form:option value="5" label="热销"></form:option>
			</form:select>
			<label>广告名称：</label>
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
 			</form:select>
 			<form:input id="d4311" path="startTime" class="Wdate input-medium" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" readonly="true" />至
 			<form:input path="endTime" class="Wdate input-medium" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" readonly="true" />
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
            <input id="btnSubmit1" class="btn btn-primary" type="button" value="新增热销" onclick="add()" />
            <input id="btnunset"  class="btn  btn-primary"  type="reset" value="重置" onclick="unset();" />
        </div>
	</form:form>

	<tags:message content="${message}"/>
	<table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 4%">序号</th>
				<th style="width: 4%">广告名称</th>
				<th style="width: 4%">子站名称</th>
				<th style="width: 10%">指向链接</th>
				<th style="width: 4%">类型</th>
				<th style="width: 2%">顺序号</th>
				<th style="width: 5%">创建时间</th>
				<!-- <th style="width: 5%">发布时间</th>
                <th style="width: 5%">下架时间</th> -->
                <th style="width: 2%">状态</th>
				<th style="width: 4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="mallAd" varStatus="s">
				<tr>
					<td><c:out value="${s.count}" /></td>
					<td>${mallAd.title}</td>
					<td>${mallAd.themeName}</td>
					<td><a href="${mallAd.adURL}" target="_blank">${mallAd.adURL}</a></td>
					<td>
						<c:if test="${mallAd.adType== 5 }">
							热销小图
						</c:if>
						<c:if test="${mallAd.adType== 6 }">
							热销大图
						</c:if>
					</td>
					<td>${mallAd.sortNumber}</td>
					<td>
						<fmt:formatDate value="${mallAd.created}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" />
					</td>
					<%-- <td>
						<fmt:formatDate value="${mallAd.startTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" />
					</td>
                    <td>
                        <fmt:formatDate value="${mallAd.endTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" />
                    </td> --%>
                    <td>
                        <c:if test="${ mallAd.status == 1 }">
                            上架
                        </c:if>
                        <c:if test="${ mallAd.status == 0 or mallAd.status == null }">
                            下架
                        </c:if>
                    </td>
						<td>
							<a href="javascript:edit(${mallAd.id})">修改</a>
							<c:if test="${ mallAd.status == 1 }">
								<a href="${ctx}/station/mallAdvertise/publishwc?id=${mallAd.id}&status=0&themeType=${dto.themeType}" >下架</a>
							</c:if>
							<c:if test="${ mallAd.status == 0 or mallAd.status == null }">
								<a href="${ctx}/station/mallAdvertise/publishwc?id=${mallAd.id}&status=1&themeType=${dto.themeType}" >上架</a>
								<a href="javascript:void(0);" onclick="deleteAdvertise('${mallAd.id}')">删除</a>
							</c:if>
						</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>



</body>
</html>