<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>排行榜管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#reContent").focus();
			$("#inputForm").validate();
		});
		
		function cancel(){
			var keywordsId = $("#keywordsId").val();
			window.location.href="${ctx}/cms/rankingList/list?keywords="+keywordsId;
		}
		function saveRanking(){
			$("#inputForm").submit();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/cms/rankingList/list">排行榜列表</a></li>
		<li class="active"><a href="${ctx}/cms/rankingList/form">排行榜添加</a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="rankingList" action="${ctx}/cms/rankingList/save" method="post" class="form-horizontal">
		<form:hidden path="id" value="${rankingList.id}"/>
		<input id="keywordsId" type="hidden" name="keywordsId" value="${keywords}"/>
		<div class="control-group">
			<label class="control-label">排行榜名称：</label>
			<div class="controls">
				<form:input path="cname" value="${rankingList.cname}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">标题：</label>
			<div class="controls">
				<form:input path="title" value="${rankingList.title}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排行榜分类：</label>
			<div class="controls">
				<form:select path="cateid" items="${fns:getDictList('cms_ranking_list')}" itemLabel="label" itemValue="value"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">keywords：</label>
			<div class="controls">
				<form:input path="keywords" value="${rankingList.keywords}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述：</label>
			<div class="controls">
				<form:textarea path="intro" value="${rankingList.intro}" rows="4"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否推荐：</label>
			<div class="controls">
				<form:radiobuttons path="recommend" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" value="${rankingList.sort}"/>
			</div>
		</div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-inverse" type="submit" value="保存" onclick="saveRanking()"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="cancel();"/>
		</div>
	</form:form>
</body>
</html>