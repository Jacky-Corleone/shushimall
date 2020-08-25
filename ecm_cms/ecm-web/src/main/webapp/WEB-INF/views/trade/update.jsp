<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>金融账户设置</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/kindeditor/themes/default/default.css"
	rel="stylesheet" />
<script src="${ctxStatic}/kindeditor/kindeditor-min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/kindeditor/lang/zh_CN.js" type="text/javascript"></script>
</head>
<body>
	<form:form id="accountForm" name="update" modelAttribute="financeAccountInfoDto"
		method="post" action="${ctx}/trade/update" class="form-horizontal">
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label" >金融云支付账号:</label>
			<div class="controls">
				<form:input path="financialCloudPaymentAccountId" id="account" htmlEscape="false" maxlength="100" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >全名:</label>
			<div class="controls">
				<form:input path="fullName" htmlEscape="false" maxlength="50"
					class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >所属银行:</label>
			<div class="controls">
				<form:input path="ownedBank" htmlEscape="false" maxlength="50"
					class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >银行登录用户名:</label>
			<div class="controls">
				<form:input path="bankLoginUsername" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >主账户名称:</label>
			<div class="controls">
				<form:input path="masterAccountName" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >主账户帐号:</label>
			<div class="controls">
				<form:input path="masterAccountNumber" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >集群ID:</label>
			<div class="controls">
				<form:input path="clusterId" htmlEscape="false" maxlength="50"
					class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >结算账户名称:</label>
			<div class="controls">
				<form:input path="settlementAccountName" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >结算账户帐号:</label>
			<div class="controls">
				<form:input path="settlementAccountNumber" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >佣金账户名称:</label>
			<div class="controls">
				<form:input path="commissionAccountName" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >佣金账户帐号:</label>
			<div class="controls">
				<form:input path="commissionAccountNumber" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >代理出金账户名称:</label>
			<div class="controls">
				<form:input path="actingOutGoldAccountName" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >代理出金账户帐号:</label>
			<div class="controls">
				<form:input path="actingOutGoldAccountNumber" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >公共计息收费账户名:</label>
			<div class="controls">
				<form:input path="publicChargingFeesAccountName" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >公共计息收费账户帐号:</label>
			<div class="controls">
				<form:input path="publicChargingFeesAccountNumber"
					htmlEscape="false" maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >公共调账户名称:</label>
			<div class="controls">
				<form:input path="publicAccountName" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >公共调账户帐号:</label>
			<div class="controls">
				<form:input path="publicAccountNumber" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >资金初始化账户名称:</label>
			<div class="controls">
				<form:input path="initializationFundsAccountName" htmlEscape="false"
					maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >资金初始化账户帐号:</label>
			<div class="controls">
				<form:input path="initializationFundsAccountNumber"
					htmlEscape="false" maxlength="50" class="required " />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >退款账户名称:</label>
			<div class="controls">
				<form:input path="refundAccountName" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >退款账户帐号:</label>
			<div class="controls">
				<form:input path="refundAccountNumber" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >支付宝结算账户名称:</label>
			<div class="controls">
				<form:input path="settlementAlipayAccountName" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >支付宝结算账户帐号:</label>
			<div class="controls">
				<form:input path="settlementAlipayAccountNumber" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >支付宝退款账户名称:</label>
			<div class="controls">
				<form:input path="refundAlipayAccountName" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >支付宝退款账户帐号:</label>
			<div class="controls">
				<form:input path="refundAlipayAccountNumber" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >微信结算账户名称:</label>
			<div class="controls">
				<form:input path="settlementWXAccountName" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >微信结算账户帐号:</label>
			<div class="controls">
				<form:input path="settlementWXAccountNumber" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >微信退款账户名称:</label>
			<div class="controls">
				<form:input path="refundWXAccountName" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >微信退款账户帐号:</label>
			<div class="controls">
				<form:input path="refundWXAccountNumber" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >手续费补足账户名称:</label>
			<div class="controls">
				<form:input path="poundageFillAccountName" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >手续费补足账户帐号:</label>
			<div class="controls">
				<form:input path="poundageFillAccountNumber" htmlEscape="false" maxlength="50" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" >前置机地址:</label>
			<div class="controls">
				<form:input path="citicFrontEndProcessor" htmlEscape="false" maxlength="100" class="required userName" />
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
					<input id="addSubmit" class="btn btn-primary" type="submit"
						value="更新" />&nbsp;
			</div>
		</div>
	</form:form>
</body>
</html>