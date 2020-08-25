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
        <div class="y-box">
        <h3 class="h3">
            公司信息
        </h3>
        <table id="contentTable1" class="table y-tb1 mb20 z-tbl">
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
                <td>${by.user.userBusinessDTO.companyName}</td>
                <td style="background: #fbfaf8;">公司所在地</td>
                <td>${by.gsaddress}</td>
                <td style="background: #fbfaf8;">公司明细地址</td>
                <td>${by.user.userBusinessDTO.companyDeclinedAddress}</td>
            </tr>
            <tr>
                <td style="background: #fbfaf8;">公司性质</td>
                <td>${by.companyqualt}</td>
                <td style="background: #fbfaf8;">公司人数</td>
                <td>${by.companypeonum}</td>
                <td style="background: #fbfaf8;">公司规模</td>
                <td>${by.companyscale}</td>
            </tr>
            <tr>
                <td style="background: #fbfaf8;">是否融资需求</td>
                <td>${by.isfinacing}</td>
                <td style="background: #fbfaf8;">经营范围</td>
                <td colspan="3">${by.user.userBusinessDTO.businessScope}</td>
            </tr>
            <tr>
                <td style="background: #fbfaf8;">期望融资额度</td>
                <td>${by.user.userBusinessDTO.financingNum}万元</td>
                <td style="background: #fbfaf8;"></td>
                <td></td>
                <td style="background: #fbfaf8;"></td>
                <td></td>
            </tr>
            </tbody>
        </table>

            <h3 class="h3">
                用户信息
            </h3>
            <table id="contentTablew2" class="table y-tb1 mb20 z-tbl">
                <colgroup>
                    <col width="20%">
                    <col width="30%">
                    <col width="20%">
                    <col width="30%">
                </colgroup>
                <tbody>
                <tr>
                    <td style="background: #fbfaf8;">用户编号</td>
                    <td>${by.userdto.uid}</td>
                    <td style="background: #fbfaf8;">用户名</td>
                    <td>${by.userdto.uname}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">昵称</td>
                    <td>${by.userdto.nickname}</td>
                    <td style="background: #fbfaf8;">注册电话</td>
                    <td>${by.userdto.umobile}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">注册邮箱</td>
                    <td>${by.userdto.userEmail}</td>
                    <td style="background: #fbfaf8;">联系人姓名</td>
                    <td>${by.userdto.linkMan}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">所在部门</td>
                    <td>${by.department}</td>
                    <td style="background: #fbfaf8;">固定电话</td>
                    <td>${by.userdto.linkPhoneNum}</td>
                </tr>
                </tbody>
            </table>

            <%--<h3 class="h3">
                绑定入金账户
            </h3>
            <table id="contentTablew3" class="table y-tb1 mb20 z-tbl">
                <colgroup>
                    <col width="20%">
                    <col width="30%">
                    <col width="20%">
                    <col width="30%">
                </colgroup>
                <tbody>
                <tr>
                    <td style="background: #fbfaf8;">开户名</td>
                    <td>${by.user.userAccountDTO.bankAccountName}</td>
                    <td style="background: #fbfaf8;">银行账号</td>
                    <td>${by.user.userAccountDTO.bankAccount}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">开户行支行名称</td>
                    <td>${by.user.userAccountDTO.bankName}</td>
                    <td style="background: #fbfaf8;">开户行支行联行号</td>
                    <td>${by.user.userAccountDTO.bankBranchJointLine}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">开户行支行所在地</td>
                    <td>${by.yhaddress}</td>
                    <td style="background: #fbfaf8;">银行开户许可证电子版</td>
                    <td>

                        &lt;%&ndash;<a href="${by.yyzzurl}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>&ndash;%&gt;
                        <a href="${filePath}${by.user.userAccountDTO.bankAccountPermitsPicSrc}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>
                    </td>
                </tr>
                </tbody>
            </table>--%>

            <h3 class="h3">
                中信账户信息
            </h3>
            <table id="contentTable4" class="y-tb1 z-tbl">
                <colgroup>
                    <col width="20%">
                    <col width="30%">
                    <col width="20%">
                    <col width="30%">
                </colgroup>
                <tbody>
                <tr>
                    <td style="background: #fbfaf8;">融资账户</td>
                    <td>${by.user.userCiticDTO.buyerFinancingAccount}</td>
                    <td style="background: #fbfaf8;">支付账户</td>
                    <td >${by.user.userCiticDTO.buyerPaysAccount}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
