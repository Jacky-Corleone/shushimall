<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>查看卖家资质</title>
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
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });
        });
    </script>
</head>
<body>
    <div class="content sub-content">
        <div class="content-body content-sub-body">
            <div class="container-fluid">
                <legend ><span class="content-body-bg">营业执照信息</span></legend>
                <div class="row-fluid">
                    <div class="span2">
                        <label>公司名称:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.companyName}
                    </div>
                    <div class="span2">
                        <label>注册号(营业执照号):</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.businessLicenceId}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>法定代表人姓名:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.artificialPersonName}
                    </div>
                    <div class="span2">
                        <label>注册资本:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.registeredCapital}万元
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>经营范围:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.businessScope}
                    </div>
                    <div class="span2">
                        <label>营业执照有效期:</label>
                    </div>
                    <div class="span4">
                        ${businessLicenceIndate}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>公司所在地:</label>
                    </div>
                    <div class="span4">
                        ${gsaddress}&nbsp;&nbsp;${userInfo.userBusinessDTO.companyDeclinedAddress}
                    </div>
                    <div class="span2">
                        <label>公司电话:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.companyPhone}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>公司是否有融资需求:</label>
                    </div>
                    <div class="span4">
                        ${isfinacing}
                    </div>
                    <div class="span2">
                        <label>融资金额:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.financingNum}万元
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>公司联系人:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.linkman}
                    </div>
                    <div class="span2">
                        <label>联系人手机:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.linkmanPhone}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>证件类型:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.idCardType}
                    </div>
                    <div class="span2">
                        <label>证件证号:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.idCardNum}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>营业执照成立日期:</label>
                    </div>
                    <div class="span4">
                        ${businessLicenceDate}
                    </div>
                    <div class="span2">
                        <label>营业执照详细地址;</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userBusinessDTO.businessLicencAddressDetail}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>营业执照所在地:</label>
                    </div>
                    <div class="span4">
                        ${yyzzaddress}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>法人身份证电子版:</label>
                    </div>
                    <div class="span4">
                        <img class="showimg" title="点击查看大图" id="businessLicencePicSrcImg"src="${filePath}${userInfo.userBusinessDTO.artificialPersonPicSrc}" style="height: 100px;width: 150px">
                        <img class="showimg" src="${filePath}${userInfo.userBusinessDTO.artificialPersonPicBackSrc}" style="height: 100px;width: 150px">
                    </div>
                    <div class="span2">
                        <label>营业执照副本电子版:</label>
                    </div>
                    <div class="span4">
                        <img class="showimg" src="${filePath}${userInfo.userBusinessDTO.businessLicencePicSrc}" style="height: 100px;width: 150px">
                    </div>
                </div>
            </div>
            <div class="container-fluid">
                <legend ><span class="content-body-bg">组织机构代码信息</span></legend>
                <div class="row-fluid">
                    <div class="span2">
                        <label>组织机构代码:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userOrganizationDTO.organizationId}
                    </div>
                    <div class="span2">
                        <label>组织机构代码有效期:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userOrganizationDTO.userfulTime}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>组织机构代码证电子版:</label>
                    </div>
                    <div class="span4">
                        <img class="showimg" src="${filePath}${userInfo.userOrganizationDTO.organizationPicSrc}" style="height: 100px;width: 150px">
                    </div>
                </div>
            </div>
            <div class="container-fluid">
                <legend ><span class="content-body-bg">公司税务登记证信息</span></legend>
                <div class="row-fluid">
                    <div class="span2">
                        <label>税务人识别号:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userTaxDTO.taxManId}
                    </div>
                    <div class="span2">
                        <label>纳税人类型:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userTaxDTO.taxpayerType}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>税务人类型税码:</label>
                    </div>
                    <div class="span4">
                        ${userInfo.userTaxDTO.taxpayerCode}
                    </div>
                    <div class="span6">

                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>纳税人资格证电子版:</label>
                    </div>
                    <div class="span4">

                        <img class="showimg" src="${filePath}${userInfo.userTaxDTO.taxpayerCertificatePicSrc}" style="width: 150px;height: 100px">
                    </div>
                    <div class="span2">
                        <label>税务登记证电子版:</label>
                    </div>
                    <div class="span4">
                        <img class="showimg" src="${filePath}${userInfo.userTaxDTO.taxRegistrationCertificatePicSrc}" style="width: 150px;height: 100px">
                    </div>
                </div>
            </div>
            <div class="container-fluid">
                <legend ><span class="content-body-bg">绑定出金账户</span></legend>
                <div class="row-fluid">
                    <div class="span2">
                        <label>银行开户名:</label>
                    </div>
                    <div class="span3">
                        ${userInfo.userAccountDTO.bankAccountName}
                    </div>
                    <div class="span2">
                        <label>公司银行帐号:</label>
                    </div>
                    <div class="span3">
                        ${userInfo.userAccountDTO.bankAccount}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>开户行支行名称:</label>
                    </div>
                    <div class="span3">
                        ${userInfo.userAccountDTO.bankName}
                    </div>
                    <div class="span2">
                        <label>开户行支行联行号:</label>
                    </div>
                    <div class="span3">
                        ${userInfo.userAccountDTO.bankBranchJointLine}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>是否中信银行:</label>
                    </div>
                    <div class="span3">
                        ${iszx}
                    </div>
                    <div class="span2">
                        <label>开户行支行所在地:</label>
                    </div>
                    <div class="span3">
                        ${yhaddress}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>银行开户许可证电子版:</label>
                    </div>
                    <div class="span3">
                        <img class="showimg" src="${filePath}${userInfo.userAccountDTO.bankAccountPermitsPicSrc}" style="width: 150px;height: 100px">
                    </div>
                </div>
            </div>

            <div class="container-fluid">
                <legend ><span class="content-body-bg">经营信息</span></legend>
                <div class="row-fluid">
                    <div class="span2">
                        <label>经营类型:</label>
                    </div>
                    <div class="span3">
                        ${dealertype}
                    </div>
                    <div class="span2">
                        <label>公司官网地址:</label>
                    </div>
                    <div class="span3">
                        <a href="${userInfo.userManageDTO.homePage}" target="_blank">${userInfo.userManageDTO.homePage}</a>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>最近一年销售额:</label>
                    </div>
                    <div class="span3">
                        ${userInfo.userManageDTO.saleNum}万元
                    </div>
                    <div class="span2">
                        <label>同类电子商务网站经验:</label>
                    </div>
                    <div class="span3">
                        ${ishavebussiness}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>网站运营人数:</label>
                    </div>
                    <div class="span3">
                        ${userInfo.userManageDTO.webOperaPeo}人
                    </div>
                    <div class="span2">
                        <label>ERP类型:</label>
                    </div>
                    <div class="span3">
                        ${erptype}
                    </div>
                </div>
            </div>

            <div class="container-fluid">
                <legend ><span class="content-body-bg">卖家店铺运营申请</span></legend>
                <div class="row-fluid">
                    <div class="span2">
                        <label>店铺名称:</label>
                    </div>
                    <div class="span10">
                        ${shop.shopName}
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>运营分类选择:</label>
                    </div>
                    <div class="span10">
                        <c:forEach items="${shopCategories}" var="s" varStatus="ss">
                            ${s.yn}->${s.bn}->${s.sn}
                        </c:forEach>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>运营商品品牌:</label>
                    </div>
                    <div class="span10">
                        <c:forEach items="${shopBrands}" var="s" varStatus="ss">
                            <img class="showimg" src="${filePath}${s.url}" style="width: 100px;height: 50px">
                        </c:forEach>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span2">
                        <label>发货（退货）地址:</label>
                    </div>
                    <div class="span10">
                        ${shopaddress}
                    </div>
                </div>
            </div>
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
