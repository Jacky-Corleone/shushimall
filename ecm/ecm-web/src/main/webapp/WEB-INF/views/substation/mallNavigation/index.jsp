<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>子站导航设置</title>
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
				$("#keyWordsQueryForm").attr("action","${ctx}/station/mallNavigation/delete?id=" + id+"&themeType=${dto.themeType}").submit();
			}
		}

		function toAdd(){
			$("#keyWordsQueryForm").attr("action","${ctx}/station/mallNavigation/toAdd?themeType=${dto.themeType}").submit();
		}

		function unset(){ //重置
            $(':input','#keyWordsQueryForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $("#pageNo").val(1);
            $("#pageSize").val(10);
            $("#keyWordsQueryForm").submit();
        }

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#keyWordsQueryForm").attr("action","${ctx}/station/mallNavigation/list?themeType=${dto.themeType}").submit();
	    	return false;
	    }
		function outOffUse(id,status){
			$("#keyWordsQueryForm").attr("action","${ctx}/station/mallNavigation/motifyMallNavStatus?id="+id+"&status="+status+"&themeType=${dto.themeType}").submit();
		}
	</script>
</head>

<body>
	<div class="content sub-content">

		<form:form id="keyWordsQueryForm" modelAttribute="dto" action="${ctx}/station/mallNavigation/list?themeType=${dto.themeType}" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="control-group">
				<div class="controls">
					<label>子站:</label>
					<form:select id="themeId" path="themeId">
						<form:option value="" label="请选择子站"/>
						<form:options items="${themeList}"  itemLabel="themeName"  itemValue="id" />
					</form:select>
					<label class="control-label">子站导航名称:</label>
					<input type="text" name="navTitle" size="50" class="required" title="商城导航名称" value="${dto.navTitle}"/>
					<input type="submit" id="btnQuerySubmit" class="btn btn-primary" onclick="return subQueryForm();" value="查询"/>
				    <input type="button" id="addButton" class="btn btn-primary" onclick="toAdd()" value="添加商城导航"/>
				    <input id="btnunset"  class="btn  btn-primary"  type="reset" value="重置" onclick="unset();" />

				</div >
			</div>
			<div class="control-group">
				<table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed">
					<tr>
						<th>序号</th>
						<th>子站名称</th>
		                <th>名称</th>
		                <th>连接</th>
		                <th>排序</th>
		                <th>状态</th>
		                <th>创建时间</th>
		                <th>操作</th>
					</tr>
					<c:forEach items="${page.list}" var="mallNav" varStatus="s">
					<tr>
						<td><c:out value="${s.count}" /></td>
						<td>${mallNav.themeName}</td>
						<td>${mallNav.navTitle}</td>
                        <td>${mallNav.navLink}</td>
                        <td>${mallNav.sortNum}</td>
                        <td><c:if test="${mallNav.status=='1'}">启用</c:if><c:if test="${mallNav.status=='0'}">不启用</c:if></td>
						<td><fmt:formatDate value="${mallNav.created}"  type="date" dateStyle="default"/></td>
						<td><a href="javascript:void(0)" onclick="deleteDto('${mallNav.id}')">删除</a>&nbsp&nbsp&nbsp<a href="${ctx}/station/mallNavigation/toEdit?id=${mallNav.id}&themeType=${dto.themeType}">修改</a>
						<c:if test="${mallNav.status==0}">
           	 								<a href="javascript:void(0)" onclick="outOffUse(${mallNav.id},1)">启用</a>
                        </c:if>
                        <c:if test="${mallNav.status==1}">
           	 								<a href="javascript:void(0)" onclick="outOffUse(${mallNav.id},0)">不启用</a>
                        </c:if>
						</td>
					</tr>
					</c:forEach>
				</table>
				<div class="pagination">${page}</div>
			</div>
		</form:form>
	</div>
</body>
</html>