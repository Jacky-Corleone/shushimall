<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商城广告列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/station/mallAdvertise/index?advType=${dto.advType}").submit();
	    	return false;
	    }
        function edit(id){
            var url = "${ctx}/mallAdvertise/toEdit?id="+id+"&advType=${dto.advType}";
            parent.openTab(url,"编辑商城广告","ma"+id);
        }
        function add(){
            var url = "${ctx}/mallAdvertise/toEdit?advType=${dto.advType}";
            parent.openTab(url,"新增商城广告","ma0");
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
			var url = "${ctx}/station/mallAdvertise/delete?id=" + AdvertiseId;
			if (confirm("确认删除？")) {
				$.ajax({
					url: url,
					type: "get",
					dataType: "json",
					success: function (data) {
						if (data.messages == "删除成功!") {
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
		
		//上架  下架
		function updateStatus(id,status,advType){
			var url = "${ctx}/station/mallAdvertise/publish";
	         $.ajax({
	             url : url,
	             type : "post",
	             dataType : "json",
	             data:{
	            	 id:id,
	            	 status:status,
	            	 advType:advType
	             },
	             success : function(data) {
	                 if(data.success){
	                     $.jBox.success("操作成功!",'提示',{closed:function(){
	                         $("#searchForm").submit();
	                     }});
	                 }else{
	                     $.jBox.info(data.msg);
	                 }
	             },
	             error : function(xmlHttpRequest, textStatus, errorThrown) {
	                 $.jBox.info("系统错误！请稍后再试！");
	             }
	         });
		}
		
		 
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="dto" action="${ctx}/station/mallAdvertise/index/?advType=${dto.advType}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>频道：</label>
            <form:select id="themeId" path="themeId" cssClass="input-medium">
                <form:option value="" label="请选择频道"/>
                <c:forEach items="${themeList}" var="theme">
                		<c:if test="${theme.status==1}">
                			<option value="${theme.id}" ${theme.id==themeId?'selected':''}>${theme.themeName}</option>
                		</c:if>
                </c:forEach>
            </form:select>
			<label>类型：</label>
			<form:select path="adType" cssClass="input-medium">
				<form:option value="" label="请选择类型"></form:option>
				<form:option value="1" label="旗舰店"/>
                <form:option value="3" label="底部广告"/>
                <form:option value="4" label="类目广告"/>
                <form:option value="6" label="本周推荐"/>
                <form:option value="5" label="精品•推荐"/>
                <form:option value="10" label="旗舰店底部广告位"/>
			</form:select>
			<label>广告名称：</label>
			<form:input path="adTitle" htmlEscape="false" maxlength="50" cssClass="input-medium"/>
            <label>状态：</label>
            <form:select path="status" cssClass="input-medium">
                <form:option value="" label="请选择状态"></form:option>
                <form:option value="0" label="已下架"/>
                <form:option value="1" label="展示中"/>
            </form:select>
		</div>
		<div style="margin-top:8px; margin-left: 0px;" >
			<label>时间：</label>
			<form:select path="timeFlag" cssClass="input-medium">
				<form:option value="1" label="创建时间"/>
				<form:option value="2" label="发布时间"/>
 			</form:select>
 			<form:input id="d4311" path="startTime" class="Wdate input-medium" readonly="true" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" />至
 			<form:input path="endTime" class="Wdate input-medium" readonly="true" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" />
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
            <input id="btnunset"  class="btn  btn-primary"  type="reset" value="重置" onclick="unset();" />
             <a href="${ctx}/station/mallAdvertise/toEdit?advType=${dto.advType}" class="btn btn-primary" >新增广告</a>
        </div>
	</form:form>

	<tags:message content="${message}"/>
	<table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 4%">序号</th>
				<th style="width: 4%">广告名称</th>
				<th style="width: 4%">频道名称</th>
				<th style="width: 10%">指向链接</th>
				<th style="width: 4%">类型</th>
				<th style="width: 2%">顺序号</th>
				<th style="width: 5%">创建时间</th>
				<!-- <th style="width: 5%">发布时间</th>
                <th style="width: 5%">下架时间</th> -->
                <th style="width: 5%">状态</th>
                <th style="width: 4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="mallAd" varStatus="s">
				<tr>
                    <td><c:out value="${s.count}" /></td>
					<td>${mallAd.title}</td>
					<td>
						<c:forEach items="${themeList}" var="theme">
							<c:if test="${theme.id == mallAd.themeId}">
								${theme.themeName}
							</c:if>
						</c:forEach>
					</td>
					<td><a href="${mallAd.adURL}" target="_blank">${mallAd.adURL}</a></td>
					<td>
						<c:if test="${mallAd.adType == 1 }">
							旗舰店
						</c:if>
						<c:if test="${mallAd.adType == 3 }">
							底部广告
						</c:if>
                        <c:if test="${mallAd.adType == 4 }">
                            类目广告
                        </c:if>
                        <c:if test="${mallAd.adType == 5 }">
                            精品•推荐
                        </c:if>
                          <c:if test="${mallAd.adType == 6 }">
                          本周推荐
                        </c:if>
                          <c:if test="${mallAd.adType == 10 }">
                          旗舰店底部广告位
                        </c:if>
					</td>
					<td>${mallAd.sortNumber}</td>
					<td>
						<fmt:formatDate value="${mallAd.created}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
					</td>
					<%-- <td>
						<fmt:formatDate value="${mallAd.startTime}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
					</td>
                    <td>
                        <fmt:formatDate value="${mallAd.endTime}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
                    </td> --%>
                    <td>
                        <c:choose>
                            <c:when test="${ mallAd.status == 1 }">展示中</c:when>
                            <c:when test="${ mallAd.status == 0 }">已下架</c:when>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${ mallAd.status == 1 }">
                            <a href="javascript:void(0);" onclick="updateStatus('${mallAd.id}',0,${dto.advType})">下架</a>
                        </c:if>
                        <c:if test="${ mallAd.status == 0 or mallAd.status == null }">
                        	<a href="${ctx}/station/mallAdvertise/toEdit?id=${mallAd.id}&advType=${dto.advType}">修改</a>
                            <a href="javascript:void(0);" onclick="updateStatus('${mallAd.id}',1,${dto.advType})">上架</a>
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