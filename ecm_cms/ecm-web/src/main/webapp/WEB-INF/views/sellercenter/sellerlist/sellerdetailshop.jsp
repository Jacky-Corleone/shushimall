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
            <li class=""><a href="${ctx}/sellerdetail/sellerdetail?uid=${shop.uid}"><h3 class="h3-current">公司信息</h3></a></li>
            <li class="active"><a href="#"><h3 class="h3-current">店铺信息</h3></a></li>
            <li class=""><a href="${ctx}/sellerdetail/sellerdetailcontract?uid=${shop.uid}"><h3 class="h3-current">合同及财务信息</h3></a></li>
        </ul>
        <div class="y-box">
        <h3 class="h3">
            店铺基本信息
        </h3>
        <table id="contentTable" class="y-tb1 z-tbl">
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
                    <td style="background: #fbfaf8;">店铺名称</td>
                    <td>${shop.shopDTO.shopName}</td>
                    <td style="background: #fbfaf8;">店铺编码</td>
                    <td>${shop.shopDTO.shopId}</td>
                    <td style="background: #fbfaf8;">店铺状态</td>
                    <td>
                        ${shop.status}
                    </td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">商家编码</td>
                    <td>${shop.uid}</td>
                    <td style="background: #fbfaf8;">商家状态</td>
                    <td>${shop.userstatus}</td>
                    <td style="background: #fbfaf8;">商家账号</td>
                    <td>${shop.user.userAccountDTO.bankAccount}</td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">开店时间</td>
                    <td>
                        ${shop.passTime}
                    </td>
                    <td style="background: #fbfaf8;">开店终止时间</td>
                    <td >
                        ${shop.endTime}
                    </td>
                    <td>店铺域名</td>
                    <td>
                    <c:if test="${shop.shopDTO.platformId==null}" > 
                           ${shop.shopDTO.shopUrl}.shushi100.com 
                     </c:if> 
                       <c:if test="${shop.shopDTO.platformId=='2'}" > 
                           ${shop.shopDTO.shopUrl}
                     </c:if> 
                    </td>
                </tr>
                <tr>
                    <td style="background: #fbfaf8;">店铺地址</td>
                    <td colspan="5">
                        ${shop.shopaddress}
                    </td>
                </tr>
            </tbody>
        </table>
            <h3 class="h3">
                经营类目信息
            </h3>
            <table id="contentTableshop" class="y-tb1 z-tbl">
                <colgroup>
                    <col width="28.3%">
                    <col width="28.3%">
                    <col width="28.3%">
                    <col width="15%">
                </colgroup>
                <tbody>
                <tr>
                    <td style="background: #fbfaf8;">一级分类</td>
                    <td style="background: #fbfaf8;">二级分类</td>
                    <td style="background: #fbfaf8;">三级分类</td>
                    <td style="background: #fbfaf8;">类目状态</td>
                </tr>
                <c:forEach items="${shop.leimu}" var="comm">
                    <tr>
                        <td>${comm.yn}</td>
                        <td>${comm.bn}</td>
                        <td>${comm.sn}</td>
                        <td>${comm.cstace}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
