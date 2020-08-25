<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>楼层广告词</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/mallAdWords/index").submit();
	    	return false;
	    }
        function edit(id){
            var url = "${ctx}/mallAdWords/toEdit?id="+id;
            parent.openTab(url,"编辑楼层广告词","maw"+id);
        }
	</script>
</head>

<body>

	<form:form id="searchForm" modelAttribute="dto" action="${ctx}/mallAdWords/index/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>楼层：</label>
			<form:select path="recId" cssClass="input-medium">
				<form:option value="" label="请选择楼层"></form:option>
				<form:options items="${recs}" itemLabel="titleDTO" itemValue="idDTO"/>
			</form:select>
			<label>类型：</label>
			<form:select path="recType" cssClass="input-medium">
				<form:option value="34" label="请选择类型"></form:option>
				<form:option value="3" label="顶部广告词"/>
				<form:option value="4" label="左侧广告词"/>
			</form:select>
			<label>广告词：</label>
			<form:input path="title" htmlEscape="false" maxlength="50" cssClass="input-medium"/>
		</div>
		<div style="margin-top:8px; margin-left: 53px;">
			<form:select path="timeFlag" cssClass="input-medium">
				<form:option value="1" label="创建时间"/>
				<form:option value="2" label="发布时间"/>
 			</form:select> 
 			<label>从</label><form:input id="d4311" path="startTime" class="Wdate input-medium" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>到
 			<form:input path="endTime" class="Wdate input-medium" onfocus="WdatePicker({minDate:'\#F{\$dp.\$D(\\'d4311\\')}',dateFmt:'yyyy-MM-dd'})"/>
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
        </div>

	</form:form>

	<tags:message content="${message}"/>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>广告词</th>
				<th>指向链接</th>
				<th>类型</th>
				<th>楼层</th>
				<th>顺序号</th>
				<th>创建时间</th>
				<th>发布时间</th>
                <th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="mallAdWords">
				<tr>
					<td>${mallAdWords.adWords}</td>
					<td><a href="${mallAdWords.url}" target="_blank">${mallAdWords.url}</a></td>
					<td>
						<c:if test="${mallAdWords.recType == 3}">
							顶部广告词
						</c:if>
						<c:if test="${mallAdWords.recType == 4}">
							左侧广告词
						</c:if>
					</td>
					<td>${mallAdWords.recName}</td>
					<td>${mallAdWords.sortNum}</td>
					<td>
						<fmt:formatDate value="${mallAdWords.created}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /> 
					</td>
					<td>
						<fmt:formatDate value="${mallAdWords.startTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /> 
					</td>
						<td>
							<a href="edit(${mallAdWords.id})">修改</a>
							<c:if test="${ mallAdWords.status == 1 }">
								<a href="${ctx}/mallAdWords/publish?id=${mallAdWords.id}&status=0" >下架</a>
							</c:if>
							<c:if test="${ mallAdWords.status == 0 or mallAdWords.status == null }">
								<a href="${ctx}/mallAdWords/publish?id=${mallAdWords.id}&status=1" >上架</a>
							</c:if>
						</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
	
	
</body>
</html>