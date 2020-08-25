<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>积分商城广告列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sellercenter/integralAdvertise/index").submit();
	    	return false;
	    }
        function edit(id){
            var url = "${ctx}/sellercenter/integralAdvertise/toEdit?id="+id;
            parent.openTab(url,"编辑积分商城广告","ma"+id);
        }
        function add(){
            var url = "${ctx}/sellercenter/integralAdvertise/toEdit";
            parent.openTab(url,"新增积分商城广告","ma0");
        }
        function toDelete(id){
        	top.$.jBox.confirm("确认要删除广告吗？","系统提示",function(v,h,f){
                if(v == "ok"){
		        	$.ajax({
						url : "${ctx}/sellercenter/integralAdvertise/toDelete",
						type : "post",
						data : {
							id:id
						},
						dataType : "json",
						success : function(data) {
							if(data.result){
								parent.$.jBox.info("删除成功","系统提示");
							}else{
								parent.$.jBox.error("删除失败","系统提示");
							}
							page();
						},
						error : function() {
							parent.$.jBox.error("删除失败","系统提示");
							page();
						}
					});
        		}
        	});
        }
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="dto" action="${ctx}/sellercenter/integralAdvertise/index/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>类型：</label>
			<form:select path="adType" cssClass="input-medium">
				<form:option value="" label="请选择类型"></form:option>
				<form:option value="7" label="首页广告"/>
				<form:option value="8" label="商品推荐位"/>
                <form:option value="9" label="兑你喜欢"/>
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
		<div style="margin-top:8px; margin-left: 53px;" >
			<form:select path="timeFlag" cssClass="input-medium">
				<form:option value="1" label="创建时间"/>
				<form:option value="2" label="发布时间"/>
                <form:option value="3" label="下架时间"/>
 			</form:select>
 			<form:input id="d4311" path="startTime" class="Wdate input-medium" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>到
 			<form:input path="endTime" class="Wdate input-medium" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',dateFmt:'yyyy-MM-dd'})"/>
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
            <a href="${ctx}/sellercenter/integralAdvertise/toEdit" class="btn btn-primary" >新增</a>
        </div>
	</form:form>

	<tags:message content="${message}"/>
	<table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 4%">序号</th>
				<th style="width: 4%">广告标题</th>
				<th style="width: 10%">SkuId</th>
				<th style="width: 10%">积分</th>
				<th style="width: 4%">类型</th>
				<th style="width: 2%">顺序号</th>
				<th style="width: 5%">创建时间</th>
				<th style="width: 5%">发布时间</th>
                <th style="width: 5%">下架时间</th>
                <th style="width: 5%">状态</th>
                <th style="width: 4%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="mallAd" varStatus="s">
				<tr>
                    <td><c:out value="${s.count}" /></td>
					<td>${mallAd.title}</td>
					<td>${mallAd.skuId}</td>
					<td>${mallAd.integral}</td>
					<td>
						<c:if test="${mallAd.adType == 7 }">
							首页广告
						</c:if>
						<c:if test="${mallAd.adType == 8 }">
							 商品推荐位
						</c:if>
                        <c:if test="${mallAd.adType == 9 }">
                           	兑你喜欢
                        </c:if>
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
                        <c:choose>
                            <c:when test="${ mallAd.status == 1 }">展示中</c:when>
                            <c:when test="${ mallAd.status == 0 }">已下架</c:when>
                        </c:choose>
                    </td>
                    <td>
                        <a href="#" onclick="toDelete(${mallAd.id})">删除</a>
                        <a href="${ctx}/sellercenter/integralAdvertise/toEdit?id=${mallAd.id}&pageNo=${page.pageNo}&pageSize=${page.pageSize}&adType=${dto.adType}&adTitle=${dto.adTitle}&status=${dto.status}&timeFlag=${dto.timeFlag}&startTime=<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd'/>&endTime=<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd'/>">修改</a>
                        <c:if test="${ mallAd.status == 1 }">
                            <a href="${ctx}/sellercenter/integralAdvertise/publish?id=${mallAd.id}&status=0&pageNo=${page.pageNo}&pageSize=${page.pageSize}&statusAdType=${dto.adType}&statusAdTitle=${dto.adTitle}&statusStatus=${dto.status}&statusTimeFlag=${dto.timeFlag}&statusStartTime=<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd'/>&statusEndTime=<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd'/>" >下架</a>
                        </c:if>
                        <c:if test="${ mallAd.status == 0 or mallAd.status == null }">
                            <a href="${ctx}/sellercenter/integralAdvertise/publish?id=${mallAd.id}&status=1&pageNo=${page.pageNo}&pageSize=${page.pageSize}&statusAdType=${dto.adType}&statusAdTitle=${dto.adTitle}&statusStatus=${dto.status}&statusTimeFlag=${dto.timeFlag}&statusStartTime=<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd'/>&statusEndTime=<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd'/>" >上架</a>
                        </c:if>
                    </td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>



</body>
</html>