<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>子站商城热门关键字设置</title>
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
		
		
		function deleteDto(id){
			var submit = function (v, h, f) {
				console.log(v)
				if (v == 'ok') {
					$.ajax({
						url : "${ctx}/station/mallKeyWords/delete",
						type : "POST",
						data :{
							id:id
						},
						dataType : "json",
						success : function(data) {
							if(data.success){
								$.jBox.prompt(data.msg, '消息', 'info', { closed: function () {
									$("#keyWordsQueryForm").submit();

								} });
							}else{
								$.jBox.error(data.msg);
							}
						},
						error : function() {
							$.jBox.error("系统错误！请稍后再试！");
						}
					});
				}

				if (v == 'cancel') {
					//jBox.tip('已取消。');
				}

				return true;
			};

			$.jBox.confirm("是否删除该关键词？", "提示", submit);
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
			$("#keyWordsQueryForm").attr("action","${ctx}/station/mallKeyWords/list").submit();
	    	return false;
	    }
	</script>
</head>

<body>
	<div class="content sub-content">
		<form:form id="keyWordsQueryForm" modelAttribute="dto" action="${ctx}/station/mallKeyWords/list" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="themeType" name="themeType" type="hidden" value="${dto.themeType}"/>
			<div class="control-group">
			
				<div class="controls">
					<label class="control-label">&nbsp;&nbsp;&nbsp;&nbsp;关键词:</label>
					<input type="text" name="word" size="50" class="required" title="热门关键字" value="${dto.word}"/>
					<label>子站:</label>
	                   <form:select id="themeId" path="themeId">
	                       <form:option value="" label="请选择子站"/>
	                       <form:options items="${themeList}"  itemLabel="themeName"  itemValue="id" />
	                   </form:select>
					<input type="submit" id="btnQuerySubmit" class="btn btn-primary" onclick="return subQueryForm();" value="查询"/>
					<input id="btnunset"  class="btn  btn-primary"  type="reset" value="重置" onclick="unset();" />
					<a href="${ctx}/station/mallKeyWords/toedit?themeType=${dto.themeType}" class="btn btn-primary" >新增关键字</a>
				</div>
			</div>
			<div class="control-group" >
				<table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed">
					<tr>
						<th>序号</th>
		                <th>关键词</th>
		                <th>子站名称</th>
		                <th>创建时间</th>
		                <th>操作</th>
					</tr>
					<c:forEach items="${page.list}" var="dto" varStatus="s">
					<tr>
						<td><c:out value="${s.count}" /></td>
						<td>${dto.word}</td>
						<td>${dto.themeName}</td>
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