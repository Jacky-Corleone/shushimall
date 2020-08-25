<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>公告列表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
td {
	text-align: center;
}
</style>
<script type="text/javascript">
	</script>
<style>
label.label-left {
	width: 25%;
	text-align: right;
}
</style>
</head>
<body>
	
	<div class="content sub-content">
    <div class="content-body content-sub-body">
	
	 <div class="row-fluid">
        <div class="span3 offset9 text-right">
           <c:if test="${page.count <4}">
           <a href="${ctx}/growth/form" class="btn btn-primary" >新增成长值设置</a>
           </c:if>
        </div>
    </div>
    <div class="row-fluid">
        <table id="contentTable" class="table table-striped table-bordered table-condensed">
            <thead><tr>
                <th>序号</th>
                <th>类型</th>
                <th>成长值/百分比</th>
                <th>操作</th>
            </tr></thead>
            <tbody>
            <c:forEach items="${page.list}" var="growthDTO" varStatus="s">
                <tr>
                    <td><c:out value="${s.count}" /></td>
                    <td>
                        <c:forEach items="${typeList}" var="typeVal">
                            <c:forEach var="entry" items="${typeVal}">
                                <c:if test="${entry.key == growthDTO.type }">
                                    <c:out value="${entry.value }"></c:out>
                                </c:if>
                            </c:forEach>
                        </c:forEach>
                    </td>
                    <td>${growthDTO.growthValue}</td>
                        <td>
                         <a href="${ctx}/growth/form?id=${growthDTO.id}&type=${growthDTO.type}&growthValue=${growthDTO.growthValue}&userLevel=${growthDTO.userLevel}">编辑</a>
                        </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
	
</body>
</html>