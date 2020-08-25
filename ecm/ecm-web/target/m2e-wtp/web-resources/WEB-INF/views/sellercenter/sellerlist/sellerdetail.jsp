<%--
  Created by IntelliJ IDEA.
  User: menpg
  Date: 2015/3/2
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>用户查询</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            // 表格排序
            //tableSort({callBack : page});
            $("#btnexport").click(function(){
                top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        $("#searchForm").attr("action","${ctx}/sellerlist/export").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });
        });
        function unset(){
            $("#searchForm").clean().submit;
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/sellerlist/sellerlist/").submit();
            return false;
        }
    </script>
    <style>
        table {
            max-width: 100%;
            background-color: transparent;
            border-collapse: collapse;
            border-spacing: 0;
        }
        .y-tb1{
            border-top: 1px solid #eee;
            border-bottom: 1px solid #eee;
            border-left:1px solid #eee;
            border-right: 1px solid:#eee;
        }.y-tb1 td{
            border-top: 1px solid #eee;
            border-bottom: 1px solid #eee;
            border-left:1px solid #eee;
            border-right: 1px solid:#eee;
        }
        .mb20 {
            margin-bottom: 20px;
        }
        .z-tbl {
            width: 100%;
            text-align: center;
        }
        .z-tbl td {
            border-bottom: 1px dotted #ccc;
            padding: 10px 5px;
            line-height: 16px;
            color: #666;
            text-align: center;
        }
        div{
           width: 95%;
        }
        .y-box {
            border: 1px solid #ddd;
        }
        h3{
            color:#000000;
            height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }.h3-current{
            width:160px;
            height: 36px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#"><h3 class="h3-current">公司信息</h3></a></li>
            <li class=""><a href="${ctx}/sellerdetail/sellerdetailshop?uid=${company.uid}"><h3 class="h3-current">店铺信息</h3></a></li>
            <li class=""><a href="${ctx}/sellerdetail/sellerdetailcontract?uid=${company.uid}"><h3 class="h3-current">合同及财务信息</h3></a></li>
        </ul>
        <div class="y-box">
        <h3 class="h3">
            公司信息
        </h3>
        <table id="contentTable" class="table y-tb1 mb20 z-tbl">
            <colgroup>
                <col width="17%">
                <col width="17%">
                <col width="17%">
                <col width="17%">
                <col width="16%">
                <col width="16%">
            </colgroup>
            <tbody>
                <tr>
                    <td style="background: #fbfaf8;">公司名称</td>
                    <td>${company.user.userBusinessDTO.companyName}</td>
                    <td style="background: #fbfaf8;">商家编码</td>
                    <td>${company.uid}</td>
                    <td style="background: #fbfaf8;">公司地址</td>
                    <td>${company.address}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">是否融资需求</td>
                    <td>${company.isfinacing}</td>
                    <td style="background: #fbfaf8;">融资金额</td>
                    <td>${company.user.userBusinessDTO.financingNum}万元</td>
                    <td style="background: #fbfaf8;">公司详细地址</td>
                    <td>${company.user.userBusinessDTO.companyDeclinedAddress}</td>
                </tr>
                <tr>
                <c:if test="${company.user.userBusinessDTO.isSanzheng==1 }">
                    <td style="background: #fbfaf8;">统一社会信用代码</td>
                    <td>${company.user.userBusinessDTO.unifiedCreditCode}</td>
                </c:if>
                <c:if test="${company.user.userBusinessDTO.isSanzheng==0 }">
                    <td style="background: #fbfaf8;">营业执照号</td>
                    <td>${company.user.userBusinessDTO.businessLicenceId}</td>
                </c:if>
                    <td style="background: #fbfaf8;">营业执照成立日期</td>
                    <td>${company.businesslicencedate}</td>
                    <td style="background: #fbfaf8;">营业执照有效期</td>
                    <td>${company.businesslicenceindate}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">营业执照所在地</td>
                    <td>${company.businesslicenceaddress}</td>
                    <td style="background: #fbfaf8;">营业执照详细地址</td>
                    <td>${company.user.userBusinessDTO.businessLicencAddressDetail}</td>
                    <td style="background: #fbfaf8;">营业执照电子版</td>
                    <td>
                        <c:if test="${not empty company.user.userBusinessDTO.businessLicencePicSrc}">
                            <a href="${filePath}${company.user.userBusinessDTO.businessLicencePicSrc}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>
                        </c:if>
                    </td>
                </tr>

                <tr>
                    <td style="background: #fbfaf8;">法人代表姓名</td>
                    <td>${company.user.userBusinessDTO.artificialPersonName}</td>
                    <td style="background: #fbfaf8;">身份证号</td>
                    <td>${company.user.userBusinessDTO.idCardNum}</td>
                    <td style="background: #fbfaf8;">法人身份证电子版</td>
                    <td>
                        <c:if test="${not empty company.user.userBusinessDTO.artificialPersonPicSrc}">
                            <a href="${filePath}${company.user.userBusinessDTO.artificialPersonPicSrc}" target="_Blank" class="z-link01" style="">正面</a>
                        </c:if>
                        <c:if test="${not empty company.user.userBusinessDTO.artificialPersonPicBackSrc}">
                            <a href="${filePath}${company.user.userBusinessDTO.artificialPersonPicBackSrc}" target="_Blank" class="z-link01" style="">背面</a>
                        </c:if>
                    </td>
                </tr>

                <tr>
                    <td style="background: #fbfaf8;">注册资本</td>
                    <td>${company.user.userBusinessDTO.registeredCapital}万元</td>
                    <td style="background: #fbfaf8;">经营范围</td>
                    <td colspan="3">${company.user.userBusinessDTO.businessScope}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">公司电话</td>
                    <td>${company.user.userBusinessDTO.companyPhone}</td>
                    <td style="background: #fbfaf8;">联系人</td>
                    <td>${company.user.userBusinessDTO.linkman}</td>
                    <td style="background: #fbfaf8;">联系人手机</td>
                    <td>${company.user.userBusinessDTO.linkmanPhone}</td>
                </tr>
                <c:if test="${company.user.userBusinessDTO.isSanzheng==0}">
                <tr>
                    <td style="background: #fbfaf8;">组织机构代码</td>
                    <td>${company.user.userOrganizationDTO.organizationId}</td>
                    <td style="background: #fbfaf8;">组织机构代码有效期</td>
                    <td>${company.user.userOrganizationDTO.userfulTime}</td>
                    <td style="background: #fbfaf8;">组织机构代码证电子版</td>
                    <td>
                        <c:if test="${not empty company.user.userOrganizationDTO.organizationPicSrc}">
                            <a href="${filePath}${company.user.userOrganizationDTO.organizationPicSrc}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>
                        </c:if>

                        <%--<a href="${company.yyzzurl}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>--%>
                    </td>
                </tr>
                <tr>
                    <td colspan="6"></td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">税务人识别码</td>
                    <td>${company.user.userTaxDTO.taxManId}</td>
                    <td style="background: #fbfaf8;">纳税人类型</td>
                    <td>${company.user.userTaxDTO.taxpayerType}</td>
                    <td style="background: #fbfaf8;">税务人类型税码</td>
                    <td>${company.user.userTaxDTO.taxpayerCode}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">税务登记证电子版</td>
                    <td>
                        <c:if test="${not empty company.user.userTaxDTO.taxRegistrationCertificatePicSrc}">
                            <a href="${filePath}${company.user.userTaxDTO.taxRegistrationCertificatePicSrc}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>
                        </c:if>
                        <%--<a href="${company.yyzzurl}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>--%>
                    </td>
                    <td style="background: #fbfaf8;">纳税人资格证电子版</td>
                    <td>
                        <c:if test="${not empty company.user.userTaxDTO.taxpayerCertificatePicSrc}">
                            <a href="${filePath}${company.user.userTaxDTO.taxpayerCertificatePicSrc}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>
                        </c:if>
                        <%--<a href="${company.yyzzurl}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>--%>

                    </td>
                    <td></td>
                    <td></td>
                </tr>
                </c:if>
            </tbody>
        </table>

        <h3 class="h3">
            经营信息
        </h3>
        <table id="contentTable2" class="table y-tb1 mb20 z-tbl">
            <colgroup>
                <col width="17%">
                <col width="17%">
                <col width="17%">
                <col width="17%">
                <col width="16%">
                <col width="16%">
            </colgroup>
            <tbody>
            <tr>
                <td style="background: #fbfaf8;">经营类型</td>
                <td>${company.dealertype}</td>
                <td style="background: #fbfaf8;">
                    公司官网地址
                </td>
                <td>
                    <a href="${company.user.userManageDTO.homePage}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">${company.user.userManageDTO.homePage}</a>
                </td>
                <td style="background: #fbfaf8;">最近一年销量</td>
                <td>${company.user.userManageDTO.saleNum}万元</td>
            </tr>
            <tr>
                <td style="background: #fbfaf8;">同类电子商务经验</td>
                <td>${company.ishavebusiness}</td>
                <td style="background: #fbfaf8;">网站运营人数</td>
                <td>${company.user.userManageDTO.webOperaPeo}人</td>
                <td style="background: #fbfaf8;">ERP类型</td>
                <td>${company.erptype}</td>
            </tr>
            </tbody>
        </table>

            <h3 class="h3">
                关联店铺
            </h3>
            <table id="contentTableshop" class="y-tb1 z-tbl">
                <colgroup>
                    <col width="34%">
                    <col width="34%">
                    <col width="32%">
                </colgroup>
                <tbody>
                <tr>
                    <td style="background: #fbfaf8;">店铺编码</td>
                    <td style="background: #fbfaf8;">店铺名称</td>
                    <td style="background: #fbfaf8;">店铺状态</td>
                </tr>
                    <tr>
                        <td>${company.shopDTO.shopId}</td>
                        <td>${company.shopDTO.shopName}</td>
                        <td>${company.shopstatus}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
