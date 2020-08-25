<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>公告查看</title>
	<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<link href="${ctxStatic}/kindeditor/themes/default/default.css" rel="stylesheet" />
	<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//公告内容赋值
			$("#noticeTitle").focus();
			
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
		
		function goBack(url,title){
			parent.openTab("${ctx}"+url,title,'7dd40d99f10a4c5e835d4a12cb27d23b');
		}
		
		function noticeBack(){
			window.location.href="${ctx}/station/mallNotice/list?themeType=3";
		}
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
				<label>${mallNoticeDTO.noticeTitle}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="noticeTitle">频道名称:</label>
			<div class="controls">
				<label>
					<c:forEach items="${themeList}" var="theme">
						<c:if test="${theme.id == mallNoticeDTO.themeId}">
							${theme.themeName}
						</c:if>
					</c:forEach>
				</label>
			</div>
		</div>
        <div class="control-group">
            <label class="control-label" for="noticeType">公告类型:</label>
            <div class="controls">
                <c:if test="${not empty mallNoticeDTO.noticeType}">
                    <c:if test="${mallNoticeDTO.noticeType=='1'}">
                        <label>文字公告</label>
                    </c:if>
                    <c:if test="${mallNoticeDTO.noticeType=='2'}">
                        <label>链接公告</label>
                    </c:if>
                </c:if>
            </div>
        </div>
        <c:if test="${mallNoticeDTO.noticeType=='2'}">
        	<div class="control-group">
	            <label class="control-label" for="noticeTitle">公告链接:</label>
	            <div class="controls">
	                <a href="${mallNoticeDTO.url}" target="_blank">
	                    ${mallNoticeDTO.url}
	                </a>
	            </div>
	        </div>
        </c:if>
        
        <c:if test="${mallNoticeDTO.noticeType=='1'}">
	        <div class="control-group">
				<label class="control-label" for="editor_id">公告内容:</label>
				<div class="controls">
					<textarea id="editor_id" name="content" style="width:700px;height:300px;" class="required">${mallNoticeDTO.noticeContent}</textarea>
				</div>
			</div>
        </c:if>
	</form:form>
</body>
</html>