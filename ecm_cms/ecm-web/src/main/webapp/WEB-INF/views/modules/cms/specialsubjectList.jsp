<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>专题模版管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function updateSpecialsubject(id){
			 window.location.href="${ctx}/cms/specsub/add?id="+id;
		}
		
		function deleteSpecialsubject(id){
			 window.location.href="${ctx}/cms/specsub/delete?id="+id;
		}
		
		function releaseSpecialsubject(id){
			 window.location.href="${ctx}/cms/specsub/publish?id="+id;
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
			window.location.href="${ctx}/cms/specsub/add";
		}
		
		function search(){
			$("#searchForm").submit();
		}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/specsub/list">模版列表</a></li>
		<li ><a href="javascript:form();">模版添加</a></li>
	</ul><br/>
	<form id="searchForm" action="${ctx}/cms/specsub/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="displayFlagId" name="displayFlag" type="hidden" value=""/>
		<label>专题名称：</label>
		  <input id="specialSubjectNameId" name="specialSubjectName" value="${specialSubjectBean.specialSubjectName}"/>
		<label>专题分类：</label>
		 <select id="specialSubjectCategoryId" name="specialSubjectCategory" ><!-- onclick="changeSpec(this.value)" -->
		 	<option value="">请选择</option>
			<c:forEach items="${fns:getDictList('cms_special_category')}" var="spec">
				<option value="${spec.id}" <c:if test="${spec.id ==specialSubjectBean.specialSubjectCategory}">selected</c:if>>${spec.label}</option><!-- <c:if test="${spec.label ==specialSubjectBean.specialSubjectCategory}">selected</c:if> -->
			</c:forEach>
		</select> 
		<input type="checkbox" id="disId" onclick="changedisplay()" <c:if test="${specialSubjectBean.displayFlag==1}">checked</c:if>/>前台列表显示
		<input id="btnSubmit" class="btn btn-primary" type="button" onclick="search()"  value="查询"/>
	</form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>专题分类</th>
				<th>专题名称</th>
				<th>专题LOGO图片</th>
				<th>专题地址</th>
				<th>加入时间</th>
				<th>排序</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${empty page.list}">
		<tr>暂无数据</tr>
		</c:if>
		<c:if test="${not empty page.list }">
			<c:forEach items="${page.list}" var="cmSSList" varStatus="status">
				<tr>
					<td>
						${status.index + 1}
					</td>
					<td>
						${cmSSList.specialSubjectCategory}
					</td>
					<td>
						${cmSSList.specialSubjectName}
					</td>
					<td>${cmSSList.specialSubjectPic}</td>
					<td>${cmSSList.address}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${cmSSList.createDate}"></fmt:formatDate></td>
					<td>${cmSSList.sort}</td>
					<td>
						<a href="javascript:releaseSpecialsubject('${cmSSList.id}')">发布</a>&nbsp;&nbsp;
						<a href="javascript:updateSpecialsubject('${cmSSList.id}')">修改</a>&nbsp;&nbsp;
						<a href="javascript:deleteSpecialsubject('${cmSSList.id}')">删除</a>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
	</table>
</body>
</html>