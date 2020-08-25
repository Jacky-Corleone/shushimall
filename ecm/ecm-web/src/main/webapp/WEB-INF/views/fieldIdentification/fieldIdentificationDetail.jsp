<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>查看买家资质</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<style>
		.row-fluid .span2{
            text-align: right;
        }
	</style>
	<script type="text/javascript"></script>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
        	<div class="container-fluid">
           		<legend ><span class="content-body-bg">实地认证信息</span></legend>
           		<div class="row-fluid">
               		<div class="span2"><label>企业名称:</label></div>
	                <div class="span3">${userBusinessDTO.companyName}</div>
	                <div class="span2"><label>企业类型:</label></div>
	                <div class="span3">${userBusinessDTO.companyQualt.label}</div>
           		</div>
	            <div class="row-fluid">
	                <div class="span2"><label>企业法人代表:</label></div>
	                <div class="span3">${userBusinessDTO.artificialPersonName}</div>
	                <div class="span2"><label>注册资本:</label></div>
	                <div class="span3">${userBusinessDTO.registeredCapital}</div>
	            </div>
	            <div class="row-fluid">
	            	<div class="span2"><label>经营范围:</label></div>
	                <div class="span3">${businessScope}</div>
	            </div>
	            <div class="row-fluid">
	                <div class="span2"><label>成立日期:</label></div>
	                <div class="span3">${businessLicenceDate}</div>
	                <div class="span2"><label>经营期限:</label></div>
	                <div class="span3">${businessLicenceIndate}</div>
	            </div>
	            <div class="row-fluid">
	                <div class="span2"><label>工商注册号:</label></div>
	                <div class="span3">${userBusinessDTO.businessLicenceId}</div>
	            </div>
       		</div>
        </div>
    </div>
</body>
</html>
