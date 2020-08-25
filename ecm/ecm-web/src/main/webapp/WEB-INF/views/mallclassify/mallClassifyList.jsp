<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文档分类列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">
		td {
		   text-align: center;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {

            $("#addDocType").click(function(){
                var url = "${ctx}/mallClassify/form";
                parent.openTab(url,"添加文档分类","d0");
            });
		});

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
        function edit(id,title,type){
            var url = "${ctx}/mallClassify/form?id="+id+"&title="+title+"&type="+type;
            parent.openTab(url,"编辑文档分类","mc"+id);
        }
	</script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
	<form:form id="searchForm" modelAttribute="mallClassifyDTO" action="${ctx}/mallClassify/list" method="post">
		<tags:message content="${message}"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="row-fluid">
            <div class="span3">
                <label>类型：</label>
                <form:select path="type" cssClass="input-medium">
                    <form:option value="" label="请选择"/>
                    <c:forEach items="${typeList}" var="typeVal">
                        <c:forEach var="entry" items="${typeVal}">
                            <form:option value="${entry.key }" label="${entry.value }"/>
                        </c:forEach>
                    </c:forEach>
                </form:select>
            </div>
            <div class="span5">
			    <label>创建时间：</label>
				<input id="startTime" name="startTime" type="text" value="${startTime}"
					readonly="readonly" maxlength="20" class="input-small Wdate"
					onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				至:
				<input id="endTime" name="endTime" type="text" readonly="readonly" value="${endTime}"
					maxlength="20" class="input-small Wdate"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
            </div>
            <div class="span2">
                &nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
            </div>
		</div>
	</form:form>
    <div class="row-fluid">
        <div class="span3 offset9 text-right">
            <a href="${ctx}/mallClassify/form" class="btn btn-primary" >添加文档类型</a>
        </div>
    </div>
    <div class="row-fluid">
        <table id="contentTable" class="table table-striped table-bordered table-condensed">
            <thead><tr>
                <th>序号</th>
                <th>编号</th>
                <th>类型</th>
                <th>分类</th>
                <th>创建时间</th>
                <th>其他操作</th>
            </tr></thead>
            <tbody>
            <c:forEach items="${page.list}" var="mallClassifyDTO" varStatus="s">
                <tr>
                    <td><c:out value="${s.count}" /></td>
                    <td>${mallClassifyDTO.id}</td>
                    <td>
                        <c:forEach items="${typeList}" var="typeVal">
                            <c:forEach var="entry" items="${typeVal}">
                                <c:if test="${entry.key == mallClassifyDTO.type }">
                                    <c:out value="${entry.value }"></c:out>
                                </c:if>
                            </c:forEach>
                        </c:forEach>
                    </td>
                    <td>${mallClassifyDTO.title}</td>
                    <td>
                        <fmt:formatDate value="${mallClassifyDTO.created}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
                    </td>
                        <td>
                                <%--<a href="${ctx}/mallClassify/form?id=${mallClassifyDTO.id}&title=${mallClassifyDTO.title}&type=${mallClassifyDTO.type}">编辑</a>--%>
                            <c:if test="${mallClassifyDTO.status == 1}">
                                <a href="${ctx}/mallClassify/status?id=${mallClassifyDTO.id}&status=2" >发布</a>
                                <a href="${ctx}/mallClassify/form?id=${mallClassifyDTO.id}&title=${mallClassifyDTO.title}&type=${mallClassifyDTO.type}">编辑</a>
                            </c:if>
                            <c:if test="${mallClassifyDTO.status == null || mallClassifyDTO.status == 2}">
                                <a href="${ctx}/mallClassify/status?id=${mallClassifyDTO.id}&status=1">下架</a>
                            </c:if>
                        </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination text-right">${page}</div>
    </div>

</div></div>
</body>
</html>