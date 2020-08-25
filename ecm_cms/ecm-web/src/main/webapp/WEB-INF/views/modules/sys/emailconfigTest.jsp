<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>短信系统设置</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#emailSendForm").validate({
				submitHandler:function(form){
		            
		            form.submit();  
		        },
				rules: {
					receiverAccount: {
					    required: true,
					    email: true 
					},
					content: {
					    required: true
					}
				},  
				messages: {
					receiverAccount:{
						required: "请输入您的email地址",
						email: "请输入正确的email地址"  
					},
					content:{
						required: "请输入发送内容"
					}
				}
			});
			
		});
	</script>
</head>
<body>

	<form:form id="emailSendForm" action="${ctx}/sys/email/sendEmail" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">收件人账号:</label>
			<div class="controls">
				<label class="lbl">
					<input type="text" id="receiverAccount" name="receiverAccount"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发送内容:</label>
			<div class="controls">
				<label class="lbl">
					<textarea id="content" name="content" rows="3"></textarea>
				</label>
			</div>
		</div>
		
		<div class="control-group">
            <div class="controls">
                <input id="sendSmsBtn" class="btn btn-primary" type="submit" value="发送测试邮件"/>
            </div>
		</div>
	</form:form>
</body>
</html>