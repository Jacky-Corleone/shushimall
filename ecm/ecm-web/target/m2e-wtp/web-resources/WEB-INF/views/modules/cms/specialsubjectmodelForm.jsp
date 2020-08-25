<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>专题模版管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#reContent").focus();
			$("#inputForm").validate();
		});
		
		function cancel(){
			window.location.href="${ctx}/cms/specsubmodel/list";
		}
		var fileName="";
		function save(){
			if(fileName==".ftl" && fileName!=""){
				$("#inputForm").submit();
			}else{
				alert("请选择正确的文件！");
				return false;
			}
		}
		
		
		function getFileName(){
			var result =/\.[^\.]+/.exec($("#fileId").val());
			if(result!=".ftl"){
				alert("请选择ftl格式文件");
				return false;
			}else{
				fileName = result;
				return false;
			}
			
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/cms/specsubmodel/list">专题模版列表</a></li>
		<li class="active"><a href="${ctx}/cms/specsubmodel/add">专题模版添加</a></li>
	</ul><br/>
	<form id="inputForm" action="${ctx}/cms/specsubmodel/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<input type="hidden" name="id" id="id" value="${id}"/>
		<div class="control-group">
			<label class="control-label">专题模版名称：</label>
			<div class="controls">
				<input name="name" value="${cmsSpecialSubjectModel.name}"/>
			</div>
		</div>
		
		<div class="control-group" >
			<label class="control-label">选择模版：</label>
			<div class="controls">
				<input type="file" id="fileId" name="filePath" value="<c:if test='${empty cmsSpecialSubjectModel.name}'></c:if><c:if test='${not empty cmsSpecialSubjectModel.name}'>${cmsSpecialSubjectModel.name}.ftl</c:if>" onchange="getFileName();"/><a></a>
			</div>
		</div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-inverse" type="button" value="保存" onclick="save()"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="cancel();"/>
		</div>
	</form>
</body>
</html>