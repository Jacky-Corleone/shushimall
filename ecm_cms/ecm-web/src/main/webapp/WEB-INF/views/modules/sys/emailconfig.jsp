<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>短信系统设置</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#inputEmailForm").validate({
				submitHandler:function(form){
		                
		            form.submit();  
		        },
				rules: {  
					sendName: {
					    required: true,
					    maxlength:50
					},
					sendAddress: {
					    required: true,
					    maxlength:100
					},
					receiveServer: {
					    required: true,
					    maxlength:100
					},
					receiveServerPort: {
					    required: true,
					    maxlength:10
					},
					sendServer: {
					    required: true,
					    maxlength:100
					},
					sendServerPort: {
					    required: true,
					    maxlength:10
					}
				},  
				messages: {
					sendName:{
						required: "请输入发件人名称",
						maxlength: jQuery.format("发件人名称不能大于{0}个字符")
					},
					sendAddress:{
						required: "请输入发件人地址",
						maxlength: jQuery.format("发件人地址不能大于{0}个字符")
					},
					receiveServer:{
						required: "请输入接收服务器",
						maxlength: jQuery.format("接收服务器不能大于{0}个字符")
					},
					receiveServerPort: {
					    required: "请输入接收端口",
					    maxlength: jQuery.format("接收端口号不能大于{0}个字符")
					},
					sendServer: {
					    required: "请输入发送服务器",
					    maxlength: jQuery.format("发送服务器不能大于{0}个字符")
					},
					receiveServerPort: {
					    required: "请输入发送端口",
					    maxlength: jQuery.format("发送端口号不能大于{0}个字符")
					}
				}
			});
			
		});
	</script>
</head>
<body>

	<form:form id="inputEmailForm" modelAttribute="emailConfig" action="${ctx}/sys/email/modifyEmailConfig" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">发件人名称:</label>
			<div class="controls">
				<label class="lbl">
					<form:hidden path="id"/>
					<form:input path="sendName" htmlEscape="false" maxlength="50"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发件人地址称:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="sendAddress" maxlength="100"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务器类型:</label>
			<div class="controls">
				<label class="lbl">
					<form:select path="emailType">
			            <form:option value="1">POP3</form:option>
			            <form:option value="2">SMTP</form:option>
			            <form:option value="3">IMSP</form:option>
			        </form:select>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">接收服务器:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="receiveServer" maxlength="100"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">接收端口:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="receiveServerPort" maxlength="10"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发送服务器:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="sendServer" maxlength="100"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发送端口:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="sendServerPort" maxlength="10"/>
				</label>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">用户名称:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="loginEmail"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户密码:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="loginPassword"/>
				</label>
			</div>
		</div>
		<div class="control-group">
            <div class="controls">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
            </div>
		</div>
	</form:form>
</body>
</html>