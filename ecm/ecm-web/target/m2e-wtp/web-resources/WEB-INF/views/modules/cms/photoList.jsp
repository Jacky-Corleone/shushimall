<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>图片管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			/*// 表格排序
			//tableSort({callBack : page});
            $("#addPic").click(function(){
                var url = "${ctx}/sellercenter/mallbanner/form";
                parent.openTab(url,"添加轮播图",0);
            });*/
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/cms/photoMange/").submit();
	    	return false;
	    }
        function edit(id){
            var url = "${ctx}/cms/photoMange/form?id="+id;
            parent.openTab(url,"编辑轮播图",id);
        }
	</script>
</head>
<body>

    <div class="content sub-content">
        <div class="content-body content-sub-body">
            <%--<div class="container-fluid">
                <div class="row-fluid">
                    <div class="span3 offset9 text-right">
                        <button id="addPic" class="btn btn-primary">添加轮播图</button>
                    </div>
                </div>
            </div>--%>
            <div class="container-fluid" style="margin-top: 10px">

                <table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed" >
                    <thead><tr>
                        <th style="width: 2%">序号</th>
                        <th style="width: 2%">所属上级</th>
                        <th style="width: 2%">图片类型</th>
                        <th style="width: 2%">图片名称</th>
                        <th style="width: 2%">备注</th>
                        <th style="width: 10%">图片路径</th>
                        <th style="width: 10%">作者</th>
                        <th style="width: 2%">更新者</th>
                        <th style="width: 4%">其他操作</th>
                    </tr></thead>
                    <c:forEach items="${page.list}" var="cmsImage" varStatus="s">
                        <tr>
                            <td><c:out value="${s.count}" /></td>
                            <td>${cmsImage.title}</td>
                            <td>${cmsImage.imgtype}</td>
                            <td>${cmsImage.imgname}</td>
                            <td>${cmsImage.remark}</td>
                            <td><a href="${filePath}${cmsImage.path}" target="_blank">${filePath}${cmsImage.path}</a></td>
                            <td>${cmsImage.author}</td>
                            <td>${cmsImage.editor}</td>
                            <td>
                            <a href="${ctx}/cms/photoMange/form?id=${cmsImage.id}" >编辑</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="pagination text-right">${page}</div>
        </div>
    </div>
  <%--  <form:form id="searchForm" modelAttribute="user" action="${ctx}/sellercenter/mallbanner/" method="post" >
        <tags:message content="${message}"/>
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    </form:form>--%>
</body>
</html>