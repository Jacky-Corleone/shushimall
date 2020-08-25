<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>短信系统设置</title>
	<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>

    <script type="text/javascript">
		$(document).ready(function() {
			
			$("#inputForm").validate({
				submitHandler:function(form){
		            $.jBox.info("submitted");
		            form.submit();  
		        },
				rules: {  
					msgHost: {
					    // required: true,
					    required: false,
					    maxlength:128
					},
					msgUrl: {
						// required: true,
						required: false,
					    maxlength:128
					},
					msgAccount: {
					    required: true,
					    maxlength:256
					},
					msgPassword: {
					    required: true,
					    maxlength:32
					},
					msgPszsubport: {
						// required: true,
						required: false,
					    maxlength:20
					},
					msgSoapaddress: {
						required: true
					    // required: false
					}
				},  
				messages: {
					msgHost:{
						required: "请输入url地址",
						maxlength: jQuery.format("host地址不能大于{0}个字符")
					},
					msgUrl:{
						required: "请输入url地址",
						maxlength: jQuery.format("url地址不能大于{0}个字符")
					},
					msgAccount:{
						required: "请输入序列号",
						maxlength: jQuery.format("序列号不能大于{0}个字符")
					},
					msgPassword: {
					    required: "请输入key",
					    maxlength: jQuery.format("key不能大于{0}个字符")
					},
					msgPszsubport: {
					    required: "请输入子端口号",
					    maxlength: jQuery.format("子端口号不能大于{0}个字符")
					},
					msgSoapaddress: {
					    required: "请输入web接口地址"
					}
				}
			});
			
		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="smsConfig" action="${ctx}/sys/sms/modifySmsConfig" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<form:hidden path="id"/>
		<!-- 
		<div class="control-group">
			<label class="control-label">host地址:</label>
			<div class="controls">
				<label class="lbl">
					<form:input path="msgHost" htmlEscape="false" maxlength="128"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">url地址:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="msgUrl" maxlength="128"/>
				</label>
			</div>
		</div>
		 -->
		<div class="control-group">
			<label class="control-label">web接口地址:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="msgSoapaddress" maxlength="256"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">序列号:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="msgAccount" maxlength="256"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">key:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="msgPassword" maxlength="32"/>
				</label>
			</div>
		</div>
		<!-- 
		<div class="control-group">
			<label class="control-label">子端口号:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="msgPszsubport" maxlength="20"/>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">web接口地址:</label>
			<div class="controls">
				<label class="lbl">
					<form:input htmlEscape="false" path="msgSoapaddress" maxlength="20"/>
				</label>
			</div>
		</div>
		 -->
		<div class="control-group">
            <div class="controls">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
            </div>
		</div>
	</form:form>
</body>
</html>