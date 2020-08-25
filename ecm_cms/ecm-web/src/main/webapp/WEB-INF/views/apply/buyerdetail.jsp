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
    <script type="text/javascript">

        $(document).ready(function() {
            //图片查看
            $('.showimg').fancyzoom({
                overlayColor: '#000',
                overlay: 0.6,
                imagezindex:100,
                showoverlay:false,
                Speed:400,
                shadow:true,
                shadowOpts:{ color: "#000", offset: 4, opacity: 0.2 },
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/',
                imgResizeScript:null,
                autoresize:true
            });
        });
    </script>
</head>
<body>
    <div class="content sub-content">
        <div class="content-body content-sub-body">
        <div class="container-fluid">
            <legend ><span class="content-body-bg">用户基本信息</span></legend>
            <div class="row-fluid">
                <div class="span2">
                    <label>用户编号:</label>
                </div>
                <div class="span3">
                    ${userInfo.userId}
                </div>
                <div class="span2">
                    <label>用户名:</label>
                </div>
                <div class="span3">
                    ${userInfo.userDTO.uname}
                </div>
            </div>
            <div class="row-fluid">
                <div class="span2">
                    <label>注册电话:</label>
                </div>
                <div class="span3">
                    ${userInfo.userDTO.umobile}
                </div>
                <div class="span2">
                    <label>注册邮箱:</label>
                </div>
                <div class="span3">
                    ${userInfo.userDTO.userEmail}
                </div>
            </div>
            <div class="row-fluid">
                <div class="span2">
                    <label>联系人姓名:</label>
                </div>
                <div class="span3">
                    ${userdtom.linkMan}
                </div>
                <div class="span2">
                    <label>所在部门:</label>
                </div>
                <div class="span3">
                    ${userdepartment}
                </div>
            </div>
            <div class="row-fluid">
                <div class="span2">
                    <label>固定电话:</label>
                </div>
                <div class="span3">
                    ${userdtom.linkPhoneNum}
                </div>
            </div>
        </div>

        <div class="container-fluid">
            <legend ><span class="content-body-bg">公司信息</span></legend>
            <div class="row-fluid">
                <div class="span2">
                    <label>公司名称:</label>
                </div>
                <div class="span3">
                    ${userInfo.userBusinessDTO.companyName}
                </div>
                <div class="span2">
                    <label>公司所在地:</label>
                </div>
                <div class="span3">
                    ${gsaddress}&nbsp;&nbsp;${userInfo.userBusinessDTO.companyDeclinedAddress}
                </div>
            </div>

            <div class="row-fluid">
                <div class="span2">
                    <label>公司性质:</label>
                </div>
                <div class="span3">
                    ${companyxz}
                </div>
                <div class="span2">
                    <label>公司人数:</label>
                </div>
                <div class="span3">
                    ${companyrs}
                </div>
            </div>

            <div class="row-fluid">
                <div class="span2">
                    <label>经营范围:</label>
                </div>
                <div class="span3">
                    ${userInfo.userBusinessDTO.businessScope}
                </div>
                <div class="span2">
                    <label>公司规模:</label>
                </div>
                <div class="span3">
                    ${companygm}
                </div>
            </div>

            <div class="row-fluid">
                <div class="span2">
                    <label>是否融资需求:</label>
                </div>
                <div class="span3">
                    ${isfinaceing}
                </div>
                <div class="span2">
                    <label>期望融资额度:</label>
                </div>
                <div class="span3">
                    ${userInfo.userBusinessDTO.financingNum}万元
                </div>
            </div>
            <c:forEach var="t" items="${null}">
                <div class="row-fluid">
                    <div class="span12">
                        设备名称:
                    </div>
                </div>


                <div class="row-fluid">
                    <div class="span6">
                        设备编号:
                    </div>
                    <div class="span6">
                        品牌:
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        分类:
                    </div>
                    <div class="span6">
                        规格:
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span12">
                        描述:
                    </div>
                </div>
            </c:forEach>
        </div>
        <!-- 如果是绿印用户，则需要显示认证信息 -->
        <c:if test="${userdtom.platformId==2}">
			<div class="container-fluid">
	            <legend ><span class="content-body-bg">资质信息</span></legend>
	            <div class="row-fluid">
	                <div class="span2">
	                    <label>绿色印刷环境标志产品认证证书:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="gpProductCertificationImg"src="${filePath}${userInfo.userBusinessDTO.gpProductCertification}" style="height: 100px;width: 150px">
                    </div>
	                <div class="span2">
	                    <label>印刷经营许可证:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="gpPrintBusinessLicenseImg"src="${filePath}${userInfo.userBusinessDTO.gpPrintBusinessLicense}" style="height: 100px;width: 150px">
                    </div>
	            </div>
	            <div class="row-fluid">
	                <div class="span2">
	                    <label>出版物印制许可证:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="gpPublicationLicenseImg"src="${filePath}${userInfo.userBusinessDTO.gpPublicationLicense}" style="height: 100px;width: 150px">
                    </div>
	                <div class="span2">
	                    <label>营业执照副本:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="businessLicencePicSrcImg"src="${filePath}${userInfo.userBusinessDTO.businessLicencePicSrc}" style="height: 100px;width: 150px">
                    </div>
	            </div>
	            <div class="row-fluid">
	                <div class="span2">
	                    <label>组织机构代码副本:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="organizationPicSrcImg"src="${filePath}${userInfo.userOrganizationDTO.organizationPicSrc}" style="height: 100px;width: 150px">
                    </div>
	                <div class="span2">
	                    <label>税务登记证副本:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="taxRegistrationCertificatePicSrcImg"src="${filePath}${userInfo.userTaxDTO.taxRegistrationCertificatePicSrc}" style="height: 100px;width: 150px">
                    </div>
	            </div>
	            <div class="row-fluid">
	                <div class="span2">
	                    <label>法人身份证（正面）:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="artificialPersonPicSrcImg"src="${filePath}${userInfo.userBusinessDTO.artificialPersonPicSrc}" style="height: 100px;width: 150px">
                    </div>
	                <div class="span2">
	                    <label>法人身份证（反面）:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="artificialPersonPicBackSrcImg"src="${filePath}${userInfo.userBusinessDTO.artificialPersonPicBackSrc}" style="height: 100px;width: 150px">
                    </div>
	            </div>
	            <div class="row-fluid">
	                <div class="span2">
	                    <label>开户许可证:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="bankAccountPermitsPicSrcImg"src="${filePath}${userInfo.userAccountDTO.bankAccountPermitsPicSrc}" style="height: 100px;width: 150px">
                    </div>
	                <div class="span2">
	                    <label>承诺书:</label>
	                </div>
	                <div class="span4">
                        <img class="showimg" title="点击查看大图" id="gpCommitmentBookImg"src="${filePath}${userInfo.userBusinessDTO.gpCommitmentBook}" style="height: 100px;width: 150px">
                    </div>
	            </div>
	            <div class="row-fluid">
	                <div class="span2">
	                    <label>ISO9001质量管理体系认证证书:</label>
	                </div>
	                <div class="span4">
	                	<c:if test="${!empty userInfo.userBusinessDTO.gpQualityManagementCertification }">
	                		<img class="showimg" title="点击查看大图" id="gpQualityManagementCertificationImg"src="${filePath}${userInfo.userBusinessDTO.gpQualityManagementCertification}" style="height: 100px;width: 150px">
	                	</c:if>
                    </div>
	                <div class="span2">
	                    <label>ISO14001环境管理体系认证证书:</label>
	                </div>
	                <div class="span4">
	                	<c:if test="${!empty userInfo.userBusinessDTO.gpEnvironmentalManagementCertification }">
                        	<img class="showimg" title="点击查看大图" id="gpEnvironmentalManagementCertificationImg"src="${filePath}${userInfo.userBusinessDTO.gpEnvironmentalManagementCertification}" style="height: 100px;width: 150px">
                        	</c:if>
                    </div>
	            </div>
	       </div>
       </c:if>
            <div class="container-fluid">
                <legend ><span class="content-body-bg">审核日志</span></legend>
                <div class="row-fluid">
                    <div class="span3">序号</div>
                    <div class="span3">审核状态</div>
                    <div class="span3">审核时间</div>
                    <div class="span3">审核描述</div>
                </div>
                <c:forEach items="${maplist}" var="s" varStatus="ss">
                    <div class="row-fluid">
                        <div class="span3">${s.no}</div>
                        <div class="span3">${s.stace}</div>
                        <div class="span3">${s.auditdate}</div>
                        <div class="span3">${s.remark}</div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>
