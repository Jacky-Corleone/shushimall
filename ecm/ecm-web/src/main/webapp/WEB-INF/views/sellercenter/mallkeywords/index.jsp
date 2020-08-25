<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商城热门关键字设置</title>
	<meta name="decorator" content="default"/>
	 <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<script type="text/javascript">
		$(function()
		{
			if('${msg}' == 'addSuccess')
			{
				$.jBox.info("添加成功！");
			}else if( '${msg}' == 'delSuccess')
			{
				$.jBox.info("删除成功！");
			}
			
		});
		
		function subForm()
		{
			if(!!$('#word').val())
			{
				$("#keyWordsForm").submit();
				return true;
			}else
			{
				alert('关键字不能为空！');
				$('#word').focus();
				return false;
			}
		}
		
		function deleteDto(id)
		{
			if(confirm("你真的要删除吗？"))
			{
				$("#keyWordsForm").attr("action","${ctx}/mallKeyWords/delete?id=" + id).submit();
			}
		}
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#keyWordsQueryForm").attr("action","${ctx}/mallKeyWords/list").submit();
	    	return false;
	    }
	</script>
</head>

<body>
	<div class="content sub-content">
		<form:form id="keyWordsForm" action="${ctx}/mallKeyWords/add" method="post">
	 		<div class="control-group">
				<div class="controls">
					<label class="control-label">关键词:</label>
					<input type="text" name="word" id="word"  size="50" class="required" title="热门关键字"/>
					<input type="submit" id="btnSubmit" class="btn btn-primary" onclick="return subForm();" value="添加"/>
				</div>
			</div>
		</form:form>
		<form:form id="keyWordsQueryForm" action="${ctx}/mallKeyWords/list" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="control-group">
				<div class="controls">
					<label class="control-label">关键词:</label>
					<input type="text" name="word" size="50" class="required" title="热门关键字" value="${dto.word}"/>
					<input type="submit" id="btnQuerySubmit" class="btn btn-primary" onclick="return subQueryForm();" value="查询"/>
				</div>
			</div>
			<div class="control-group">
				<table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed">
					<tr>
						<th>序号</th>
		                <th>关键词</th>
		                <th>创建时间</th>
		                <th>操作</th>
					</tr>
					<c:forEach items="${page.list}" var="dto" varStatus="s">
					<tr>
						<td><c:out value="${s.count}" /></td>
						<td>${dto.word}</td>
						<td><fmt:formatDate value="${dto.created}"  type="date" dateStyle="default"/></td>
						<td><a href="javascript:void(0)" onclick="deleteDto(${dto.id})">删除</a></td>
					</tr>
					</c:forEach>
				</table>
				<div class="pagination">${page}</div>
			</div>
		</form:form>
	</div>
</body>
</html>