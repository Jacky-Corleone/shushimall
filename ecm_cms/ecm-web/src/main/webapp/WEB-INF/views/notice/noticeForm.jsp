<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>公告管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
	
	
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
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
		   	 	
	   	 	});
			//公告内容赋值
			$("#noticeTitle").focus();
			$("#inputForm").validate();
			$("#noticeType").change(function(){
				if(this.value==2){
					$("#noticeUrlDiv").show();
					$("#noticeUrl").addClass("url");
					$("#editorIdDiv").hide();
					$("textarea[name='content']").removeClass("required");
				}else if(this.value==1){
					$("#editorIdDiv").show();
					$("textarea[name='content']").addClass("required");
					$("#noticeUrlDiv").hide();
					$("#noticeUrl").removeClass("url");
				}
			});
			//进入编辑页面时，判断是审核类型公告
			var noticeType=$("#noticeType").val();
			if(noticeType&&noticeType=='2'){
				$("#noticeUrlDiv").show();
				$("#noticeUrl").addClass("url");
				$("#editorIdDiv").hide();
				$("textarea[name='content']").removeClass("required");
            }else if(noticeType&&noticeType=='1'){
            	$("#editorIdDiv").show();
				$("textarea[name='content']").addClass("required");
				$("#noticeUrlDiv").hide();
				$("#noticeUrl").removeClass("url");
            }
		});
		
		
		
		function noticeAdd(val) {
            var noticeType=$("#noticeType").val();
          
            if(noticeType&&noticeType=='1'){
            	if(UE.getEditor('editor_id').getContent()!=''){
    				$("#noticeContent").val(UE.getEditor('editor_id').getContent());
    				$("textarea[name='content']").removeClass("required");
                }else{
                	$("textarea[name='content']").addClass("required");
                	$.jBox.info("请填写公告内容");
                	return;
                }
            }
            if(noticeType&&noticeType=='2'){
            	if($("#noticeUrl").val()==''){
            		$.jBox.info("请填写公告链接");
            		return ;
            	}
            }
            $("#noticeStatus").val(val);
            
            var id = $("#noticeId").val();
            if (id == "") {
                $("#inputForm").submit();
            } else {
                $("#inputForm").attr("action", "${ctx}/notice/edit").submit();
            }
		};
	</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="mallNoticeDTO" method="post" action="${ctx}/notice/save" class="form-horizontal">
		<form:hidden path="noticeId"/>
		<form:hidden path="noticeContent" id="noticeContent"/>
		<form:hidden path="noticeStatus" id="noticeStatus"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="noticeTitle">公告标题:</label>
			<div class="controls">
				<form:input path="noticeTitle" id="noticeTitle" htmlEscape="false" maxlength="20" class="required"/>
			</div>
		</div>
        <div class="control-group">
            <label class="control-label" for="noticeType">公告类型:</label>
            <div class="controls">
                <form:select id="noticeType" path="noticeType" class="input-small" >
                    <form:option value="1" label="文字公告"/>
                    <form:option value="2" label="链接公告"/>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="platformId">平台类型:</label>
            <div class="controls">
                <form:select id="platformId" path="platformId" class="input-small" >
                    <form:option value="0" label="舒适100平台"/>
<%--                     <form:option value="2" label="绿印平台"/> --%>
                </form:select>
            </div>
        </div>
        
        <div id="noticeUrlDiv" class="control-group" style="display:none">
            <label class="control-label" for="noticeUrl">公告链接:</label>
            <div class="controls">
                <form:input path="url" id="noticeUrl" htmlEscape="false" cssStyle="width: 400px;" maxlength="200" class="url"/>
              	 示例：http://www.xxxx.com
            </div>
        </div>
        <div id="editorIdDiv" class="control-group" style="display:block">
			<label class="control-label" for="editor_id">公告内容:</label>
			<div class="controls">
				<textarea id="editor_id" name="content" style="width:700px;height:300px;" class="required" >${mallNoticeDTO.noticeContent}</textarea>
			</div>
		</div>
		
		<div class="control-group">
            <div class="controls">
				<input class="btn btn-primary" type="button" onclick="noticeAdd(1)" value="发布"/>&nbsp;
				<input class="btn btn-primary" type="button" onclick="noticeAdd(2)" value="保存草稿"/>&nbsp;
				<input class="btn btn-primary" type="button" onclick="history.go(-1)" value="取消"/>&nbsp;
			</div>
		</div>
	</form:form>
</body>
</html>