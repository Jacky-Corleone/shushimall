<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>全国体验店管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function update(id){
			 window.location.href="${ctx}/cms/experStore/update?id="+id;
		}
		
		function deleteExperStore(id){
			 window.location.href="${ctx}/cms/experStore/delete?id="+id;
		}
		
		/* function changeSpec(id){
			 $("#specialSubjectCategoryId").val(id);
		} */
		
		function changedisplay(){
			if($("#disId").prop("checked")){
				 $("#displayFlagId").val(1);
			}else{
				 $("#displayFlagId").val("");
			}
			
		}
		
		function form(){
			window.location.href="${ctx}/cms/experStore/addOfPage";
		}
		
		function search(){
			$("#searchForm").submit();
		}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/experStore/list">体验店列表</a></li>
		<li ><a href="javascript:form();">体验店添加</a></li>
	</ul><br/>
	<form id="searchForm" action="${ctx}/cms/experStore/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<%-- <label>名称：</label>
		  <input id="specialSubjectNameId" name="specialSubjectName" value="${specialSubjectBean.specialSubjectName}"/>
		<label>专题分类：</label>
		 <select id="specialSubjectCategoryId" name="specialSubjectCategory" ><!-- onclick="changeSpec(this.value)" -->
		 	<option value="">请选择</option>
			<c:forEach items="${fns:getDictList('cms_special_category')}" var="spec">
				<option value="${spec.id}" <c:if test="${spec.id ==specialSubjectBean.specialSubjectCategory}">selected</c:if>>${spec.label}
			</c:forEach>
		</select>  --%>
		<!-- <input id="btnSubmit" class="btn btn-primary" type="button" onclick="search()"  value="查询"/> -->
	</form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>店铺名称</th>
				<th>店铺地址</th>
				<th>加入时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${empty page.list}">
		<tr>暂无数据</tr>
		</c:if>
		<c:if test="${not empty page.list }">
			<c:forEach items="${page.list}" var="experStore" varStatus="status">
				<tr>
					<td>
						${status.index + 1}
					</td>
					<td>
						${experStore.storeName}
					</td>
					<td>${experStore.address}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${experStore.createDate}"></fmt:formatDate></td>
					<td>
						<a href="javascript:update('${experStore.id}');">修改</a>&nbsp;&nbsp;
						<a href="javascript:deleteExperStore('${experStore.id}');">删除</a>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>