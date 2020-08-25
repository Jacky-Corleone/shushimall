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
    <title>订单明细</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
        function opengooddetail(itemid,skuid){
            var url="${mallPath}/productController/details?id="+itemid+"&skuId="+skuid;
            window.open(url);
        }
    </script>
    <style>
        table{}
        .y-tb1{
            width: 100%;
            border:1px solid #fda99e;
            background: #ffeee6;
            height: 50px;
        }.y-tb1 td{
            border-top: 0px solid #eee;
            border-bottom: 0px solid #eee;
            border-left:0px solid #eee;
            border-right: 0px solid:#eee;
        }
        .mb20 {
            margin-bottom: 20px;
        }
        .z-tbl {
            width: 100%;
            border-spacing: 0;
        }
        .z-tbl td {
            padding: 5px 5px;
            line-height: 16px;
            text-align: left;
        }

        div.y-box {
            width: 95%;
            border: 1px solid #ddd;
        }.z-box{
            border: 1px solid #dadada;
            background: #ededed;
            padding: 4px;
            margin-bottom: 10px;
        }.z-box2{
            background: #fff;
            padding: 15px;
        }.item {
             padding: 10px;
             border-bottom: 1px solid #eee;
         }
        h3{
            color:#000000;
            height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }.hx{
            line-height: 6px;
            border-left: 3px solid #e94645;
            font-size: 14px;
            text-indent: 5px;
            margin-bottom: 10px;
            margin-top: 10px;
        }
        h5{
            color:#000000;
            height: 30px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
            padding: 5px 5px;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
        <div class="y-box">
        <h3 class="h3">
            订单详情
        </h3>
        <table id="contentTable1" class="y-tb1">
            <colgroup>
                <col width="10%">
                <col width="23.33%">
                <col width="10%">
                <col width="23.33%">
                <col width="10%">
                <col width="23.33%">
            </colgroup>
            <tbody>
                <tr>
                    <td style="text-align: right">订单号：</td>
                    <td style="text-align: left">${map.id}</td>
                    <td style="text-align: right">订单状态：</td>
                    <td style="text-align: left">${map.orderStace}</td>
                    <td style="text-align: right">下单时间：</td>
                    <td style="text-align: left">
                        ${map.ordertime}
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="z-box">
            <div class="z-box2">
            <table class="z-tbl">
                <colgroup>
                    <col width="50%">
                    <col width="50%">
                </colgroup>
                 <tr>
                     <td>订单编号：${map.id}</td>
                     <td>订单状态：${map.orderStace}</td>
                 </tr>
                <tr>
                    <td>商家账号：${map.sellerName}</td>
                    <td>客户账号：${map.buyerName}</td>
                </tr>
                <tr>
                    <td>下单时间：${map.ordertime}</td>
                    <td>订单完成时间：${map.updateTime}</td>
                </tr>
            </table>
            </div>
        </div>
           <%-- <div class="z-box">
                <h3 class="h3">订单跟踪</h3>
                <div class="z-box2">
                    <table class="z-tbl">
                        <colgroup>
                            <col width="50%">
                            <col width="50%">
                        </colgroup>
                        <tr>
                            <td>物流公司：${map.wlname}</td>
                            <td>运单号：${map.wlcode}</td>
                        </tr>
                    </table>
                </div>
            </div>--%>


            <div class="z-box">
                <h3 class="h3">订单商品</h3>
                <div class="z-box2">
                    <table class="z-tbl">
                        <colgroup>
                            <col width="20%">
                            <col width="19%">
                            <col width="15%">
                            <col width="15%">
                            <col width="15%">
                            <col width="16%">
                        </colgroup>
                        <c:forEach items="${map.itemList}" var="map">
                        <tr>
                            <td >商品名称：${map.itemName}</td>
                            <td >商品编号：${map.itemId}</td>
                            <td >商品单价：${map.price}</td>
                            <td >商品数量：${map.num}</td>
                            <td >商品总价：${map.priceTotal}</td>
                            <td >
                                <a href="${mallPath}/productController/details?id=${map.itemId}&skuId=${map.skuId}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">订单商品:
                                <img src="${filePath}${map.skuUrl}" style="width: 40px;height: 30px">
                                </a>
                            </td>
                        </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>


            <div class="z-box">
                <h3 class="h3">订单信息</h3>
                <div class="z-box2">
                    <div class="item">
                        <h5 class="h5" style="margin-bottom: 10px;">收货人信息</h5>
                        <table class="z-tbl">
                            <colgroup>
                                <col width="100%">
                            </colgroup>
                            <tr>
                                <td>收货人：${map.name}</td>
                            </tr>
                            <tr>
                                <td>地址：${map.addressp}</td>
                            </tr>
                            <tr>
                                <td>手机号码：${map.mobile}</td>
                            </tr>
                        </table>
                    </div>

                    <div class="item">
                        <table class="z-tbl">
                            <colgroup>
                                <col width="100%">
                            </colgroup>
                            <tr>
                                <td>付款方式：${map.zflx}</td>
                            </tr>
                            <tr>
                                <td>付款时间：${map.paymenttime}</td>
                            </tr>
                            <tr>
                                <td>商品金额：￥${map.totalprice}</td>
                            </tr>
                            <tr>
                                <td>运费金额：￥${map.freightyf}</td>
                            </tr>
                            <tr>
                                <td>优惠金额：￥${map.totaldiscount}</td>
                            </tr>
                            <tr>
                                <td>应支付金额：￥${map.paymentprice}</td>
                            </tr>
                            <tr>
                                <td>优惠码：${map.promocode}</td>
                            </tr>

                        </table>
                    </div>

                    <div class="item">
                        <h5 class="h5" style="margin-bottom: 10px;">发票信息：${map.iffp}</h5>
                        <table class="z-tbl">
                            <colgroup>
                                <col width="100%">
                            </colgroup>
                            <tr>
                                <td>发票台头：${map.fptt}</td>
                            </tr>
                        </table>
                    </div>

                    <div class="item">
                        <h5 class="h5" style="margin-bottom: 10px;">订单备注：${map.comment}</h5>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>
</body>
</html>
