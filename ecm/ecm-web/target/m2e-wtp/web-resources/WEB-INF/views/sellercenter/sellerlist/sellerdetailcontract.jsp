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
            line-height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }.h3-current{
            height: 36px;
            width:160px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
        <ul class="nav nav-tabs">
            <li class=""><a href="${ctx}/sellerdetail/sellerdetail?uid=${contract.uid}"><h3 class="h3-current">公司信息</h3></a></li>
            <li class=""><a href="${ctx}/sellerdetail/sellerdetailshop?uid=${contract.uid}"><h3 class="h3-current">店铺信息</h3></a></li>
            <li class="active"><a href="#"><h3 class="h3-current">合同及财务信息</h3></a></li>
        </ul>
        <div class="y-box">
        <h3 class="h3">
            结算信息
        </h3>
        <table id="contentTable1" class="y-tb1 z-tbl">
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
                    <td style="background: #fbfaf8;">银行开户名</td>
                    <td>${contract.user.userAccountDTO.bankAccountName}</td>
                    <td style="background: #fbfaf8;">开户行支行名</td>
                    <td>${contract.user.userAccountDTO.bankName}</td>
                    <td style="background: #fbfaf8;">开户银行所在地</td>
                    <td>
                        ${contract.address}
                    </td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">开户银行联行号</td>
                    <td>${contract.user.userAccountDTO.bankBranchJointLine}</td>
                    <td style="background: #fbfaf8;">银行账户</td>
                    <td>${contract.user.userAccountDTO.bankAccount}</td>
                    <td style="background: #fbfaf8;">银行开户许可证电子版</td>
                    <td>
                        <c:if test="${not empty contract.user.userAccountDTO.bankAccountPermitsPicSrc}">
                            <a href="${filePath}${contract.user.userAccountDTO.bankAccountPermitsPicSrc}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">账户是否中信银行</td>
                    <td>${contract.isciticbank}</td>
                    <td style="background: #fbfaf8;"></td>
                    <td></td>
                    <td style="background: #fbfaf8;"></td>
                    <td>
                    </td>
                </tr>
            </tbody>
        </table>



            <h3 class="h3">
                合同信息
            </h3>
            <table id="contentTable2" class="y-tb1 z-tbl">
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
                    <td style="background: #fbfaf8;">合同开始时间</td>
                    <td>${contract.starttime}</td>
                    <td style="background: #fbfaf8;">合同结束时间</td>
                    <td>${contract.endtime}</td>
                    <td style="background: #fbfaf8;">合同电子版</td>
                    <td>
                        <c:if test="${not empty contract.contractJssAddr}">
                            <a href="${filePath}${contract.contractJssAddr}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">查看</a>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">结算周期</td>
                    <td>7天(每周一结算)</td>
                    <td style="background: #fbfaf8;">质保金</td>
                    <td>${contract.zb}元</td>
                    <td style="background: #fbfaf8;">平台使用费</td>
                    <td>${contract.pt}元</td>
                </tr>
                </tbody>
            </table>

            <h3 class="h3">
                经营类目信息
            </h3>
            <table id="contentTable3" class="y-tb1 z-tbl">
                <colgroup>
                    <col width="25%">
                    <col width="25%">
                    <col width="25%">
                    <col width="15%">
                    <col width="10%">
                </colgroup>
                <tbody>
                <tr>
                    <td style="background: #fbfaf8;">一级分类</td>
                    <td style="background: #fbfaf8;">二级分类</td>
                    <td style="background: #fbfaf8;">三级分类</td>
                    <td style="background: #fbfaf8;">类目状态</td>
                    <td style="background: #fbfaf8;">扣点</td>
                </tr>
                <c:forEach items="${contract.leimu}" var="comm">
                    <tr>
                        <td>${comm.yn}</td>
                        <td>${comm.bn}</td>
                        <td>${comm.sn}</td>
                        <td>${comm.cstace}</td>
                        <td>${comm.kd}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

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
                    <td style="background: #fbfaf8;">收款账户</td>
                    <td>${contract.citimap.sellerWithdrawsCashAccount}</td>
                    <td style="background: #fbfaf8;">冻结账户</td>
                    <td >${contract.citimap.sellerFrozenAccount}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
