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
    <title>退款协议</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });
        });
         function closedetail(){
             parent.closeTab("退款或售后协议");
         }
         function opengooddetail(itemid,skuid){
             var url="${mallPath}/productController/details?id="+itemid+"&skuId="+skuid;
             window.open(url);
         }
    </script>
    <style>
        table{}
        .tabth th {
            font-size: 1.1em;
            text-align: left;
            padding-top: 5px;
            padding-bottom: 4px;
        }
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
            width: 90%;
            border: 1px solid #ddd;
        }.z-box{
             border-top: 2px solid #00a2ca;
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
           }.returnStep{width:650px;margin:20px auto 30px;}
            .returnStep ul{padding-left:5px;height:28px;}
            .returnStep li{color:#999;float:left;}
            .returnStep li.first{width:140px;}
            .returnStep li.second{width:190px;}
            .returnStep li.third{width:166px;}
            .returnStep li.current{color:#333;}
            .returnStep .step0{width:650px;height:20px;overflow:hidden;background:url("${ctx}/../static/picture/tk-step.png") no-repeat 0 -80px;}
            .returnStep .step1{width:650px;height:20px;overflow:hidden;background:url("${ctx}/../static/picture/tk-step.png") no-repeat 0 0px;}
            .returnStep .step2{width:650px;height:20px;overflow:hidden;background:url("${ctx}/../static/picture/tk-step.png") no-repeat 0 -20px;}
            .returnStep .step3{width:650px;height: 20px;overflow:hidden;background:url("${ctx}/../static/picture/tk-step.png") no-repeat 0 -40px;}
            .returnStep .step4{width:650px;height:20px;overflow:hidden;background:url("${ctx}/../static/picture/tk-step.png") no-repeat 0 -60px;}
            .bj-left { width: 250px;padding: 0 5px;float: left;}
            .bj-right{margin-left:233px;padding:20px 0 0 70px;background:#fff;min-height:520px;}
            .bj-left .item{padding:20px 10px;}
            .bj-left .item h4{color:#666;}
            .bj-left .item li{line-height:24px;}
            .bj .tit{width:70px;padding-right:5px;color:#999;text-align:right;display:inline-block;}
            .bj-right li{padding-bottom:25px;}
            .bj-right .tit{color:#666;width:140px;font-size:14px;}
            .bj-right p{line-height:20px;}
        h3{
            color:#000000;
            height: 46px;
            /*text-indent: 20px;*/
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
            font-size: 14px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
            padding: 5px 5px;
        }
        textarea.z-edit {
            width: 400px;
            height: 100px;
            min-height: 46px;
            color: #666;
            line-height: 23px;
            padding: 6px;
            border: 1px solid #eee;
            background: #f9f9f9;
            border-radius: 2px;
            -moz-border-radius: 2px;
            -webkit-border-radius: 2px;
        }
        li{list-style: none;}
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
        <div class="y-box">
            <h3 class="h3">
                退款协议查询
            </h3>
            <div class="z-box">
                <h3 class="h3">退款进度</h3></td>
                <div class="z-box2">
                    <div class="returnStep">
                        <ul>
                            <li class="first <c:if test="${map.step=='step1'}">current</c:if>">买家选择退货货品</li>
                            <li class="second <c:if test="${map.step=='step2'}">current</c:if>">买家填写退款原因、金额</li>
                            <li class="third <c:if test="${map.step=='step3'}">current</c:if>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;退款处理</li>
                            <li class="forth <c:if test="${map.step=='step4'}">current</c:if>">&nbsp;&nbsp;&nbsp;&nbsp;退款完成</li>
                        </ul>
                        <div class="${map.step}"></div>
                    </div>
                </div>
            </div>

            <div class="z-box">
                <table class="z-tbl">
                    <colgroup>
                        <col width="100%">
                    </colgroup>
                    <tr>
                        <td><h3 class="h3">以下是你选择退款的商品 </h3>共选择<span style="color: red;">${map.size}</span>种商品，本次退款货品金额总计<span style="color: red;">${map.refundAll}</span>元</td>
                    </tr>
                </table>
                <div class="z-box2">
                    <table class="z-tbl">
                        <colgroup>
                            <col width="40%">
                            <col width="20%">
                            <col width="20%">
                            <col width="20%">
                        </colgroup>
                        <tr>
                            <th style="background: #FFFFFF;text-align: center;">货品</th>
                            <th style="background: #FFFFFF;text-align: center;">单价(元)</th>
                            <th style="background: #FFFFFF;text-align: center;">数量</th>
                            <th style="background: #FFFFFF;text-align: center;">订单状态</th>
                        </tr>
                        <c:forEach items="${map.detailList}" var="item">
                            <tr>
                                <td style="text-align: center;">
                                    <a href="javascript:void(0)" onclick="opengooddetail(${item.itemId},${item.skuId})">
                                        <img src="${filePath}${item.picUrl}" width="80" height="80">
                                    </a>
                                    <a href="javascript:void(0)" onclick="opengooddetail(${item.itemId},${item.skuId})">${item.goodName}</a>
                                </td>
                                <td style="text-align: center;">
                                <span style="color: red;">${item.payPrice}</span>
                                </td>
                                <td style="text-align: center;">${item.returnCount}</td>
                                <td style="text-align: center;">${map.orderStace}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>

            <div class="z-box">
                <table class="z-tbl">
                    <colgroup>
                        <col width="100%">
                    </colgroup>
                    <tr>
                        <td><h3 class="h3">退款原因及金额</h3></td>
                    </tr>
                </table>
                <div style="padding: 0px;" class="bj z-box2">
                    <div style="background:#FBFBFF;" class="bj-left">
                        <div class="item">
                            <h5>订单摘要</h5>
                            <ul >
                                <li><span class="tit">订单号：</span>${map.orderId}</li>
                                <li><span class="tit">下单时间：</span>${map.orderCreatedate}</li>
                                <li><span class="tit">付款时间：</span>${map.orderPaydate}</li>
                                <li>
                                    <span class="tit">订单状态：</span>
                                    <span style="color: #008000;">${map.orderStace}</span>
                                </li>
                                <li>
                                    <span class="tit">实付款：</span>
                                    <span style="color: red">${map.paymentPrice}</span>
                                </li>
                                <li><span class="tit"></span>
                                    (含退运费￥<span style="color: red">${map.freight}）</span>
                                </li>
                            </ul>
                        </div>
                        <div class="item">
                            <h5>买家信息</h5>
                            <ul >
                                <li>电话：${map.buyerPhone}</li>
                                <li>账号：${map.buyerName}</li>
                            </ul>
                        </div>
                        <div class="item">
                            <h5>卖家信息</h5>
                            <ul >
                                <li>${map.sellerShopName}</li>
                                <li>电话：${map.sellerPhone}</li>
                                <li>账号：${map.sellerName}</li>
                            </ul>
                        </div>
                    </div>
                    <div class="bj-right">
                        <c:if test="${not empty map.complainStace}">
                            <div style="text-align: center;background: #fafafa;margin: 0 20px 40px 0px;height: 60px;line-height: 60px;">
                                <span style="color: red;font-size: 18px;top: 10px;">${map.complainStace}</span>
                            </div>
                        </c:if>
                        <ul>
                            <li><span class="tit" style="width:70px ">退款原因：</span><span>${map.returnResion}</span></li>
                            <li>
                                <span class="tit" style="width:70px ">需要退款的货品金额：</span>
                                <span style="color: red">${map.refundAll}</span>
                            </li>
                            <li style="word-break:break-all;"><span class="tit" style="width:70px ">退款说明：</span>${map.remark}</li>
                            <li>
                                <span class="tit" style="float: left;width: 70px;height:230px">凭证：</span>
                                <span >
                                    <c:forEach items="${map.listpic}" var="item">
                                            <img class="showimg" src="${filePath}${item}" width="118" height="88" >
                                    </c:forEach>
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <%--<div class="z-box">
                <h3 class="h3">买卖双方来往协议历史记录</h3>
                <div class="z-box2">
                    <table class="z-tbl">
                        <colgroup>
                            <col width="100%">
                        </colgroup>
                        <tr>
                            <td>
                                <textarea id="commentid" name="comment" class="z-edit">${map.comment}</textarea>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>--%>

            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="button" value="关闭" onclick="closedetail();" />
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
