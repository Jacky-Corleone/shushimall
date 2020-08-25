<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>帮助中心管理</title>
	<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
	
	<script type="text/javascript">

		$(document).ready(function() {
			var ue = UE.getEditor('editor_id',{
				serverUrl:'${ctx}/ueditor/exec',
				imageUrlPrefix:"${filePath}"
			});
			
			ue.addListener('ready',function(){
		
				var imgObjs = $("#ueditor_0").contents().find("img");
				imgObjs.each(function () {
		           if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
		        	   $(this).attr("src","${filePath}"+$(this).attr("src"));
		           }
		           if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
		        	   $(this).attr("_src","${filePath}"+$(this).attr("_src"));
		           }
		   	 	});
		   	 	ue.setDisabled('fullscreen');
	   	 	});
		});


		function add(val) {
			var htmlString = UE.getEditor('editor_id').getContent().toString();
			var end = htmlString.indexOf('</p>');
			if(htmlString.substring(3,end).trim() == '&nbsp;'){
				$("#editor_id").html('');
			}else{
				$("#editor_id").html(UE.getEditor('editor_id').getContent());
			}
			$("#inputForm").validate();
            $("#mallStatus").val(val);
            var id = $("#mallId").val();
            if (id == "") {
                $("#inputForm").submit();
            } else {
                $("#inputForm").attr("action","${ctx}/mallHelpDocument/edit").submit();
            }
		}


		function getMallClassify(selectValue){
			$.ajax({
		        type : 'GET',
		        contentType : 'application/json',
		        url : "${ctx}/mallClassify/getMallClassifyMap",
		        data:{
		        	type:$("#mallType").val()
		        },
		        dataType : 'json',
		        success : function(data) {
		        	$("#mallClassifyId").empty();
		            $.each(data.data, function(i, item) {
		            	$("<option value='"+item.id+"'>"+item.title+"</option>").appendTo($("#mallClassifyId"));
		            });
		        }
		      });
		}
	</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="mallDocumentDTO" method="post" action="${ctx}/mallHelpDocument/save" class="form-horizontal">
		<form:hidden path="mallId"/>
		<form:hidden path="mallContentUrl" id="mallContentUrl"/>
		<form:hidden path="mallStatus" id="mallStatus"/>
		<tags:message content="${message}"/>

		<div class="control-group">
			<label class="control-label" for="mallTitle">帮助文档主题:</label>
			<div class="controls">
				<form:input path="mallTitle" htmlEscape="false" maxlength="47" class="required"/>
				<span>限20个字符以内</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="mallTitle">排序:</label>
			<div class="controls">
				<form:input path="mallSortNum" htmlEscape="false" maxlength="2" class="required" title="不能为空且只能是数字"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="mallType">所属分类:</label>
			<div class="controls">
				<form:select path="mallType" id="mallType" onchange="getMallClassify('')" class="required">
					<form:option value="" label="请选择"/>
					<c:forEach items="${typeList}" var="typeVal">
						<c:forEach var="entry" items="${typeVal}">
							<form:option value="${entry.key }" label="${entry.value }"/>
						</c:forEach>
					</c:forEach>
				</form:select>
				<form:select path="mallClassifyId" id="mallClassifyId">
					<c:forEach items="${mallClassifyDTOList}" var="mallClassify">
						<form:option  value="${mallClassify.id }" label="${mallClassify.title }"/>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="editor_id">帮助文档内容:</label>
			<div class="controls">
				<textarea id="editor_id" name="content" style="width:700px;height:300px;" required="true">${mallDocumentDTO.mallContentUrl}</textarea>
			</div>
		</div>
		<div class="control-group">
            <div class="controls">
			<a class="btn btn-primary" type="button" href="${ctx}/mallHelpDocument/list">取消</a>
            </div>
		</div>
	</form:form>
</body>
</html>