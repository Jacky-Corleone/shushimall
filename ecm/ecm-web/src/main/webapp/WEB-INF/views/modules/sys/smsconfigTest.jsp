<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>短信系统设置</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			// 手机号码验证 
			jQuery.validator.addMethod("isMobile", function(value, element) { 
			  var length = value.length; 
			  var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
			  return this.optional(element) || (length == 11 && mobile.test(value)); 
			}, "请正确填写您的手机号码"); 
			
			$("#smsSendForm").validate({
				submitHandler:function(form){
		            
		            form.submit();  
		        },
				rules: {
					receiverPhone: {
					    required: true,
					    maxlength:11,
					    isMobile: true
					},
					sendContent: {
					    required: true,
					    maxlength:70
					}
				},  
				messages: {
					receiverPhone:{
						required: "请输入您的手机号码",
						maxlength: jQuery.format("手机号码不能大于{0}个字符")
					},
					sendContent:{
						required: "请输入发送内容",
						maxlength: jQuery.format("发送内容不能大于{0}个字符")
					}
				}
			});
			
		});
	</script>
</head>
<body>
	<form:form id="smsSendForm" action="${ctx}/sys/sms/sendSms" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">接收手机:</label>
			<div class="controls">
				<label class="lbl">
					<input type="text" id="receiverPhone" name="receiverPhone" maxlength="11"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">发送内容:</label>
			<div class="controls">
				<label class="lbl">
					<textarea id="sendContent" name="sendContent" rows="3"  maxlength="70"></textarea>
				</label>
			</div>
		</div>
		
		<div class="control-group">
            <div class="controls">
                <input id="sendSmsBtn" class="btn btn-primary" type="submit" value="发送测试短信"/>
            </div>
		</div>
	</form:form>
</body>
</html>