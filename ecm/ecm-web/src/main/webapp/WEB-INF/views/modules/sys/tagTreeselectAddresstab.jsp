<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>地域管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			// 表格排序
			tableSort({callBack : page});
			
		});
		
		function page(n,s){
			var parentCode = $("#contentTable").attr("title");
			var url = "${ctx}/tag/treeselectadresstab?parentCode="+parentCode;
			
			console.info(url);
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action",url).submit();
	    	return false;
	    }
		
		var ondelete = function(){
			var id = $("#contentTable").attr("title");
			var url = "${ctx}/tag/treeselectadresstab?parentCode="+id;
			window.location.href = url;
			//window.parent.frames["iframeTreeWithTab"].location.href = url;
			
		}
		var  leftTreeNodeDelete = function(){
			var code  = "${lastDeleteACode}";
			console.info(code);
			window.parent.frames["iframeTree"].deleteNode(code);
		}
	</script>
</head>
<body>
	<div id="importBox" class="hide">
	</div>
    
	 <form:form id="searchForm" modelAttribute="parentCodeTag" name="${parentCodeTag}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
	</form:form> 
	
	<tags:message content="${message}"/>
	<table id="contentTable" title="${parentCodeTag}"  class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th class="sort name">名称</th>
		<th>级别</th>
		<th>创建时间</th>
		<th>修改时间</th>
		<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="user">
			<tr>
				<td>${user.name}</td>
				<%-- <td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td> --%>
				<td>${user.level}</td>
				<td>
					<fmt:formatDate value="${user.createtime}" type="both" />
				
				</td>
				<td>
				  <fmt:formatDate value="${user.updatetime}" type="both"/>
				</td>
				
				<td>
    				<a href="${ctx}/tag/addressbase/show?code=${user.code}">修改</a>
					<a href="${ctx}/tag/addressbase/delete?code=${user.code}" onclick="return confirmx('确认要删除吗？', this.href,leftTreeNodeDelete)">删除</a>
					<a href="${ctx}/tag/addressbase/show?parentcode=${user.code}&level=${user.level}">添加下级</a>
					
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
		<c:if test="${resultW.errCode == 1 && resultW.flag =='delete'}">
	
			<script type="text/javascript">
					var id  = "${lastDeleteACode}";
					window.parent.frames["iframeTree"].deleteNode(id);
			
			</script>
			
	</c:if>
	
</body>
</html>