<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>留言管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function updateSpecialsubjectmodel(id){
			window.location.href="${ctx}/cms/specsubmodel/add?id="+id; 
		}
		
		function deleteSpecialsubjectmodel(id){
			window.location.href="${ctx}/cms/specsubmodel/delete?id="+id;
		}
		
		function changeSpecialsubjectmodelList(id){
			/* $("#rankingListId").val(id); */
		}
		
		function form(){
			window.location.href="${ctx}/cms/specsubmodel/add"; 
		}
		
		function release(id){
			/* window.location.href="${ctx}/cms/specsubmodel/add?id="+id;  */
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/specsubmodel/list">专题模版列表</a></li>
		<li ><a href="javascript:form();">专题模版添加</a></li>
	</ul><br/>
	<form id="searchForm" action="${ctx}/cms/specsubmodel/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>专题模版名称：</label>
		 <input id="nameId" name="name" value="${name}"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>专题模版名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		 <c:if test="${empty page.list}">
		<tr>暂无数据</tr>
		</c:if>
		<c:if test="${not empty page.list}">
			<c:forEach items="${page.list}" var="cmSSList" varStatus="status">
				<tr>
					<td>
						${status.index + 1}
					</td>
					<td>
						${cmSSList.name}
					</td>
					<td>
						<a href="javascript:updateSpecialsubjectmodel('${cmSSList.id}')">修改</a>&nbsp;&nbsp;
						<a href="javascript:deleteSpecialsubjectmodel('${cmSSList.id}')">删除</a>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
	</table>
	 <div class="pagination">${page}</div>
</body>
</html>