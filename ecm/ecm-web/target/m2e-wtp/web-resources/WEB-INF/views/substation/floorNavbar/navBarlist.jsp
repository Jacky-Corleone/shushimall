<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>楼层导航栏</title>
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
			}else if( '${msg}' == 'editSuccess')
			{
				$.jBox.info("编辑成功！");
			}
			
		});
		
		function subForm()
		{
			$("#titleQueryForm").attr("action","${ctx}/station/floorNavbar/add?themeType=${dto.themeType}").submit();
		}
		
		function deleteDto(id)
		{
			if(confirm("你真的要删除吗？"))
			{
				$("#titleQueryForm").attr("action","${ctx}/station/floorNavbar/delete?id=" + id+"&themeType=${dto.themeType}").submit();
			}
		}
		function inUse(id)//下架
		{
			if(confirm("你真的要下架吗？"))
			{
				$("#titleQueryForm").attr("action","${ctx}/station/floorNavbar/setStatusZero?id=" + id+"&themeType=${dto.themeType}").submit();
			}
		}
		function outOffUse(id)//上架
		{
			if(confirm("你真的要上架吗？"))
			{
				$("#titleQueryForm").attr("action","${ctx}/station/floorNavbar/setStatusOne?id=" + id+"&themeType=${dto.themeType}").submit();
			}
		}
		function unset(){ //重置
            $(':input','#titleQueryForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $("#pageNo").val(1);
            $("#pageSize").val(10);
            $("#titleQueryForm").submit();
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#titleQueryForm").attr("action","${ctx}/station/floorNavbar/navBarlist?themeType=${dto.themeType}").submit();
	    	return false;
	    }
	</script>
</head>

<body>
	<div class="content sub-content">
		<form:form id="titleQueryForm"  modelAttribute="dto" action="${ctx}/station/floorNavbar/navBarlist?themeType=${dto.themeType}" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div>
					<%--<label>导航类型:</label>
				<form:select path="nType" cssClass="input-medium">
						<form:option value="" label="请选择类型"/>
						<form:option value="1" label="类型一"/>
						<form:option value="2" label="类型二"/>
               	 		<form:option value="3" label="类型三"/>
               	 		<form:option value="4" label="类型四"/>
				</form:select>--%>
				<label>楼层:</label>
				<form:select id="recId" path="recId">
					<form:option value="" label="请选择类型"/>
					<form:options items="${mallRec}"  itemLabel="titleDTO"  itemValue="idDTO" />
				</form:select>
					<label>导航名称:</label>
					<form:input path="title" htmlEscape="false" maxlength="50" cssClass="input-medium"/>
					<input type="submit" id="btnQuerySubmit" class="btn btn-primary" onclick="return subQueryForm();" value="查询"/>
					<input type="button" id="btnSubmit" class="btn btn-primary" onclick="subForm()" value="添加导航"/>
					<input id="btnunset"  class="btn  btn-primary"  type="reset" value="重置" onclick="unset();" />
			</div>
			<div class="control-group">
				<table id="contentTable"
							class="table table-striped table-bordered table-condensed">

							<tr>
								<th>编号</th>
								<th>导航名称</th>
								<th>指向链接</th>
								<%--<th>导航类型</th>--%>
								<th>创建时间</th>
								<th>操作</th>
							</tr>
							<c:forEach items="${page.list}" var="dto" varStatus="s">
								<tr>
									<td><c:out value="${s.count}" /></td>
									<td>${dto.title}</td>
									<td>${dto.url}</td>
									<%--<td><c:choose>
										<c:when test="${dto.nType==1}">
             										 类型一                            
                                        </c:when>
										<c:when test="${dto.nType==2}">
           											 类型二                                 
                                        </c:when>
                                        <c:when test="${dto.nType==3}">
           											 类型三                                 
                                        </c:when>
                                        <c:when test="${dto.nType==4}">
           											 类型四                             
                                        </c:when>
										</c:choose>
									</td>--%>
									<td><fmt:formatDate value="${dto.created}"  type="date" dateStyle="default"/></td>
									<td>
									<c:choose>
										<c:when test="${dto.status==1}">
                                          <a href="javascript:void(0)" onclick="inUse(${dto.id})">下架</a>
                                        </c:when>
										<c:when test="${dto.status==0}">
           	 								<a href="javascript:void(0)" onclick="outOffUse(${dto.id})">上架</a>                             
                                        </c:when>
									</c:choose>
										<a href="${ctx}/station/floorNavbar/edit?id=${dto.id}&themeType=${dto.themeType}">编辑</a>
										<a id="cc" href="javascript:void(0)"
										<td><a href="javascript:void(0)" onclick="deleteDto(${dto.id})">删除</a></td>
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