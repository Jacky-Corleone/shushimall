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
    <title>办理仲裁</title>
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
            parent.getTabandrefress('投诉仲裁','办理仲裁',false);
        }
        function closedetailandrefresh(){
            $.jBox.prompt('仲裁完成', '消息', 'info', { closed: function () { parent.getTabandrefress('投诉仲裁','办理仲裁',true); } });
        }
        function closedetailandrefresh1(){
            $.jBox.prompt('仲裁已经关闭', '消息', 'info', { closed: function () { parent.getTabandrefress('投诉仲裁','办理仲裁',true); } });
        }
        //买家赢仲裁
        function zcwc(){
            var id='${map.id}';
            //是否退款
            var orderPriceflag="no";
            if(document.getElementById("orderPriceCheckboxid${map.type}").checked){
                orderPriceflag="yes";
            }
            //退款金额
            var orderPrice=$("#orderPriceid${map.type}").val();
            //是否扣除质保金
            var zbPriceflag="no";
            if(document.getElementById("zbPriceCheckboxid${map.type}").checked){
                zbPriceflag="yes";
            }
            var orderPriceCheckboxseller="no";
            if(document.getElementById("orderPriceCheckboxsellerid${map.type}").checked){
                orderPriceCheckboxseller="yes";
            }
            //扣除的质保金金额
            var zbPrice=$("#zbPriceid${map.type}").val();
            var comment=$("#commentid").val();
            var tkdid=$("#tkdid").val();
            $("#popUpDiv").modal("show");
            $.ajax({
                url:"${ctx}/complain/buyerwin/",
                type:"post",
                data:{
                    id:id,
                    orderPriceflag:orderPriceflag,
                    orderPrice:orderPrice,
                    zbPriceflag:zbPriceflag,
                    zbPrice:zbPrice,
                    comment:comment,
                    tkdid:tkdid,
                    orderPriceCheckboxseller:orderPriceCheckboxseller
                },
                dataType:'json',
                success:function(data){
                    $("#popUpDiv").modal("hide");
                    if(data.success){
                        //返回列表页
                        closedetailandrefresh();
                    }else{
                        $.jBox.prompt(data.msg, '消息', 'error');
                    }
                },error:function(){
                    $("#popUpDiv").modal("hide");
                    $.jBox.prompt("系统繁忙，请稍后再试", '消息', 'error');
                }
            });
        }
        //卖家赢仲裁
        function jszc(){
            var id='${map.id}';
            var comment=$("#commentid").val();
            var tkdid=$("#tkdid").val();
            $("#popUpDiv").modal("show");
            $.ajax({
                url:"${ctx}/complain/sellerwin/",
                type:"post",
                data:{
                    id:id,
                    comment:comment,
                    tkdid:tkdid
                },
                dataType:'json',
                success:function(data){
                    $("#popUpDiv").modal("hide");
                    if(data.success){
                        //返回列表页
                        closedetailandrefresh1();
                    }else{
                        $.jBox.prompt(data.msg, '消息', 'error');
                    }
                },error:function(){
                    $("#popUpDiv").modal("hide");
                    $.jBox.prompt("系统繁忙，请稍后再试", '消息', 'error');
                }
            });
        }
        //查看退款协议
        function showmj(id){
            var url = "${ctx}/customerservice/showdetail?id="+id;
            var title = "退款或售后协议";
            var id = "退款或售后协议";
            parent.openTab(url,title,id);
        }
        function lockorder(orderId,btid){
            $("#popUpDiv").modal("show");
            $.ajax({
                url:"${ctx}/complain/lockorder/",
                type:"post",
                data:{
                    orderId:orderId
                },
                dataType:'json',
                success:function(data){
                    $("#popUpDiv").modal("hide");
                    if(data.success){
                        $.jBox.prompt(data.msg, '消息', 'info');
                        $("#"+btid).attr("value","订单解锁").attr("onclick","unlockorder(\'"+orderId+"\',\'"+btid+"\')");
                    }else{
                        $.jBox.prompt(data.msg, '消息', 'error');
                    }
                },error:function(){
                    $("#popUpDiv").modal("hide");
                    $.jBox.prompt("系统繁忙，请稍后再试", '消息', 'error');
                }
            });
        }
        function unlockorder(orderId,btid){
            $("#popUpDiv").modal("show");
            $.ajax({
                url:"${ctx}/complain/unlockorder/",
                type:"post",
                data:{
                    orderId:orderId
                },
                dataType:'json',
                success:function(data){
                    $("#popUpDiv").modal("hide");
                    if(data.success){
                        $.jBox.prompt(data.msg, '消息', 'info');
                        $("#"+btid).attr("value","锁定订单").attr("onclick","lockorder(\'"+orderId+"\',\'"+btid+"\')");
                    }else{
                        $.jBox.prompt(data.msg, '消息', 'error');
                    }
                },error:function(){
                    $("#popUpDiv").modal("hide");
                    $.jBox.prompt("系统繁忙，请稍后再试", '消息', 'error');
                }
            });
        }
        //多选框互斥
        function checkoutbox(id1,id2,id3){
            $("input[name='checkAll']").attr("checked",false);
            if(id1){
                var checkbox=$("#"+id1+":checked");
                if(checkbox&&checkbox.size()>0){
                    if(id2){
                        $("#"+id2).attr("checked",false);
                    }
                    if(id3){
                        $("#"+id3).attr("checked",false);
                    }
                }
            }
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
            width: 80%;
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
           }
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
            font-size: 15px;
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
    </style>
</head>
<body>
<div class="content sub-content">
<div class="content-body content-sub-body" style="margin-left:3%;">
<div class="y-box">
<h3 class="h3">
    仲裁详情
</h3>

<div class="z-box">
    <table class="z-tbl">
        <colgroup>
            <col width="50%">
            <col width="50%">
        </colgroup>
        <tr>
            <td><h3 class="h3">买家信息</h3></td>
            <td><h3 class="h3">店铺信息</h3></td>
        </tr>
    </table>
    <div class="z-box2">
        <table class="z-tbl">
            <colgroup>
                <col width="50%">
                <col width="50%">
            </colgroup>
            <tr>
                <td>买家名称：${map.buyerName}</td>
                <td>店铺名称：${map.shopName}</td>
            </tr>
            <tr>
                <td>买家编码：${map.buyerCode}</td>
                <td>店铺编码：${map.shopCode}</td>
            </tr>
            <tr>
                <td>联系电话：${map.buyerMobile}</td>
                <td>联系电话：${map.shopMobile}</td>
            </tr>
        </table>
    </div>
</div>

<div class="z-box">
    <table class="z-tbl">
        <colgroup>
            <col width="50%">
            <col width="50%">
        </colgroup>
        <tr>
            <td><h3 class="h3">投诉方：
	            <c:choose>
	            	<c:when test="${map.tsList[0].complainType=='1'}">买家</c:when>
	            	<c:otherwise>卖家</c:otherwise>
	            </c:choose>
            </h3></td>
            <td><h3 class="h3">上传凭证</h3></td>
        </tr>
    </table>
    <input type="hidden" id="tkdid" value="${map.refundId }">
    <div class="z-box2">
        <table class="z-tbl">
            <colgroup>
                <col width="50%">
                <col width="50%">
            </colgroup>
            <tr>
                <td>投诉人电话：${map.tsList[0].mjmobile}</td>
                <td rowspan="${map.tsList[0].listurlsize1}">
                   <c:forEach items="${map.tsList[0].listurl1}" var="item">
                        <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td>投诉人邮箱：${map.tsList[0].mjemail}</td>
                <c:if test="${map.tsList[0].listurlsize1=='1'}">
                <c:if test="${not empty map.tsList[0].listurlsize2}">
                    <td rowspan="${map.tsList[0].listurlsize2}">
                        <c:forEach items="${map.tsList[0].listurl2}" var="item">
                                <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
                        </c:forEach>
                    </td>
                </c:if>
                </c:if>
            </tr>
            <tr>
                <td>投诉&nbsp;&nbsp;时间：${map.tsList[0].createDate}</td>
                <c:if test="${map.tsList[0].listurlsize1=='1'}">
                <c:if test="${not empty map.tsList[0].listurlsize3}">
                    <td rowspan="${map.tsList[0].listurlsize3}">
                        <c:forEach items="${map.listurl3}" var="item">
                                <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
                        </c:forEach>
                    </td>
                </c:if>
                </c:if>
              <c:if test="${map.tsList[0].listurlsize1=='2'}">
                  <c:if test="${not empty map.tsList[0].listurlsize2}">
                      <td rowspan="${map.tsList[0].listurlsize2}">
                          <c:forEach items="${map.tsList[0].listurl2}" var="item">
                                  <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
                          </c:forEach>
                      </td>
                  </c:if>
              </c:if>
            </tr>
            <tr>
                <td>投诉&nbsp;&nbsp;原因：${map.tsList[0].tsnr}</td>
                <c:if test="${map.tsList[0].listurlsize1=='1'}">
                <c:if test="${not empty map.tsList[0].listurlsize4}">
                    <td rowspan="${map.tsList[0].listurlsize4}">
                        <c:forEach items="${map.tsList[0].listurl4}" var="item">
                                <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
                        </c:forEach>
                    </td>
                </c:if>
                </c:if>
                <c:if test="${map.tsList[0].listurlsize1=='2'}">
                    <c:if test="${not empty map.tsList[0].listurlsize3}">
                        <td rowspan="${map.tsList[0].listurlsize3}">
                            <c:forEach items="${map.tsList[0].listurl3}" var="item">
                                    <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
                            </c:forEach>
                        </td>
                    </c:if>
                </c:if>
            </tr>
            <tr>
                <td colspan="2">投诉&nbsp;&nbsp;说明：${map.tsList[0].tscomment}</td>
            </tr>
        </table>
    </div>
</div>

<c:if test="${fn:length(map.tsList)>1}">
	<div class="z-box">
	    <table class="z-tbl">
	        <colgroup>
	            <col width="50%">
	            <col width="50%">
	        </colgroup>
	        <tr>
	            <td><h3 class="h3">辩解方：
	            	<c:choose>
		            	<c:when test="${map.tsList[1].complainType=='1'}">买家</c:when>
		            	<c:otherwise>卖家</c:otherwise>
		            </c:choose>
	            </h3></td>
	            <td><h3 class="h3">上传凭证</h3></td>
	        </tr>
	    </table>
	    <div class="z-box2">
	        <table class="z-tbl">
	            <colgroup>
	                <col width="50%">
	                <col width="50%">
	            </colgroup>
	            <tr>
	                <td>辩解人电话：${map.tsList[1].mjmobile}</td>
	                <td rowspan="${map.tsList[1].listurlsize1}">
	                   <c:forEach items="${map.tsList[1].listurl1}" var="item">
	                        <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
	                    </c:forEach>
	                </td>
	            </tr>
	            <tr>
	                <td>辩解人邮箱：${map.tsList[1].mjemail}</td>
	                <c:if test="${map.tsList[1].listurlsize1=='1'}">
	                <c:if test="${not empty map.tsList[1].listurlsize2}">
	                    <td rowspan="${map.tsList[1].listurlsize2}">
	                        <c:forEach items="${map.tsList[1].listurl2}" var="item">
	                                <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
	                        </c:forEach>
	                    </td>
	                </c:if>
	                </c:if>
	            </tr>
	            <tr>
	                <td>辩解&nbsp;&nbsp;时间：${map.tsList[1].createDate}</td>
	                <c:if test="${map.tsList[1].listurlsize1=='1'}">
	                <c:if test="${not empty map.tsList[1].listurlsize3}">
	                    <td rowspan="${map.tsList[1].listurlsize3}">
	                        <c:forEach items="${map.listurl3}" var="item">
	                                <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
	                        </c:forEach>
	                    </td>
	                </c:if>
	                </c:if>
	              <c:if test="${map.tsList[1].listurlsize1=='2'}">
	                  <c:if test="${not empty map.tsList[1].listurlsize2}">
	                      <td rowspan="${map.tsList[1].listurlsize2}">
	                          <c:forEach items="${map.tsList[1].listurl2}" var="item">
	                                  <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
	                          </c:forEach>
	                      </td>
	                  </c:if>
	              </c:if>
	            </tr>
	            <tr>
	                <td>辩解&nbsp;&nbsp;原因：${map.tsList[1].tsnr}</td>
	                <c:if test="${map.tsList[1].listurlsize1=='1'}">
	                <c:if test="${not empty map.tsList[1].listurlsize4}">
	                    <td rowspan="${map.tsList[1].listurlsize4}">
	                        <c:forEach items="${map.tsList[1].listurl4}" var="item">
	                                <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
	                        </c:forEach>
	                    </td>
	                </c:if>
	                </c:if>
	                <c:if test="${map.tsList[1].listurlsize1=='2'}">
	                    <c:if test="${not empty map.tsList[1].listurlsize3}">
	                        <td rowspan="${map.tsList[1].listurlsize3}">
	                            <c:forEach items="${map.tsList[1].listurl3}" var="item">
	                                    <img class="showimg" style="width: 120px;height: 80px;" src="${filePath}${item}">
	                            </c:forEach>
	                        </td>
	                    </c:if>
	                </c:if>
	            </tr>
	            <tr>
	                <td colspan="2">辩解&nbsp;&nbsp;说明：${map.tsList[1].tscomment}</td>
	            </tr>
	        </table>
	    </div>
	</div>
</c:if>

<c:if test="${map.type=='1'}">
    <div class="z-box">
        <h3 class="h3">退款信息</h3>
        <div class="z-box2">
            <table class="z-tbl">
                <colgroup>
                    <col width="33.33%">
                    <col width="33.33%">
                    <col width="33.33%">
                </colgroup>
                <tr>
                    <td>退款编码：${map.refundNo}</td>
                    <td>订单编号：${map.orderId}</td>
                    <td>退货原因：${map.thresion}</td>
                </tr>
                <tr>
                    <td>运费金额：${map.ordertkYf}</td>
                    <td>订单金额：${map.ordertkJe}</td>
                    <td><a target="_blank" href="javascript:showmj('${map.thid}')">点击查看退款详情</a></td>
                </tr>
                <tr>
                    <td colspan="3">
                        问题描述：${map.rekark}
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <div class="z-box">
        <h3 class="h3">仲裁</h3>
        <div class="z-box2">
            <table class="z-tbl">
                <colgroup>
                    <col width="100%">
                </colgroup>
                <tr>
                    <td>
                        <input name="sellerwin"  id="orderPriceCheckboxsellerid${map.type}" type="checkbox" value="1" onclick=" checkoutbox('orderPriceCheckboxsellerid${map.type}','orderPriceCheckboxid${map.type}','')" />
                        卖家赢得仲裁：卖家胜利，系统不强制退款，退款需要卖家和买家协商。
                    </td>
                </tr>
                <tr>
                    <td>
                        <input name="orderPriceCheckbox" checked="checked" id="orderPriceCheckboxid${map.type}" type="checkbox" value="1" onclick=" checkoutbox('orderPriceCheckboxid${map.type}','orderPriceCheckboxsellerid${map.type}','')" />
                        卖家退款给买家<input name="orderPrice" readonly="readonly" id="orderPriceid${map.type}" value="${map.ordertkamount}" style="width:25%" type="text" class="form-control" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <input disabled="disabled" name="zbPriceCheckbox" type="checkbox" id="zbPriceCheckboxid${map.type}" value="1"/>
                        扣除卖家质保金<input disabled="disabled" name="zbpPice" id="zbPriceid${map.type}" style="width:25%" type="text" class="form-control" />
                    </td>
                </tr>
            </table>
        </div>
    </div>
</c:if>
<c:if test="${map.type=='2'}">
    <div class="z-box">
        <h3 class="h3">售后信息</h3>
        <div class="z-box2">
            <table class="z-tbl">
                <colgroup>
                    <col width="33.33%">
                    <col width="33.33%">
                    <col width="33.33%">
                </colgroup>
                <tr>
                    <td>退货原因：${map.thresion}</td>
                    <td>订单编号：${map.orderId}</td>
                    <td>退货快递公司：${map.kdcompany}</td>
                </tr>
                <tr>
                    <td>运费金额：${map.ordertkYf}</td>
                    <td>订单金额：${map.ordertkJe}</td>
                    <td>退货快递单号：${map.kdno}</td>
                </tr>
                <tr>
                    <td>
                        <a target="_blank" href="javascript:showmj('${map.thid}')">点击查看退款详情</a>
                    </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="3">问题描述：${map.rekark}</td>
                </tr>
            </table>
        </div>
    </div>

    <div class="z-box">
        <h3 class="h3">仲裁</h3>
        <div class="z-box2">
            <table class="z-tbl">
                <colgroup>
                    <col width="100%">
                </colgroup>
                <tr>
                    <td>
                        <input name="sellerwin"  id="orderPriceCheckboxsellerid${map.type}" type="checkbox" value="1"  onclick="checkoutbox('orderPriceCheckboxsellerid${map.type}','orderPriceCheckboxid${map.type}','')"  />
                        卖家赢得仲裁：卖家胜利，系统不强制退款，退款需要卖家和买家协商。
                    </td>
                </tr>
                <tr>
                    <td>
                        <input name="orderPriceCheckbox" checked="checked" id="orderPriceCheckboxid${map.type}" type="checkbox" value="1" onclick="checkoutbox('orderPriceCheckboxid${map.type}','orderPriceCheckboxsellerid${map.type}','')" />
                        卖家退款给买家<input readonly="readonly" name="orderPrice" id="orderPriceid${map.type}" value="${map.ordertkamount}" style="width:25%" type="text" class="form-control" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <input disabled="disabled" name="zbPriceCheckbox" type="checkbox" id="zbPriceCheckboxid${map.type}" value="1"/>
                        扣除卖家质保金<input disabled="disabled" name="zbpPice" id="zbPriceid${map.type}" style="width:25%" type="text" class="form-control" />
                    </td>
                </tr>
            </table>
        </div>
    </div>
</c:if>


<div class="z-box">
    <h3 class="h3">仲裁留言</h3>
    <div class="z-box2">
        <table class="z-tbl">
            <colgroup>
                <col width="100%">
            </colgroup>
            <tr>
                <td>
                    <textarea id="commentid" name="comment" class="z-edit">客户就是上帝，卖家你就委屈下吧</textarea>
                </td>
            </tr>
        </table>
    </div>
</div>

<div class="row-fluid" style="margin-top: 8px;">
    <div class="span5">
        <label class="label-left control-label"></label>
        <input id="btnquery1"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="zcwc()" value="仲裁" />
        <c:if test="${map.lock=='1'}">
            <input id="btnquery3"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="lockorder('${map.orderId}','btnquery3')" value="锁定订单" />
        </c:if>
        <c:if test="${map.lock=='2'}">
            <input id="btnquery4"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="unlockorder('${map.orderId}','btnquery4')" value="订单解锁" />
        </c:if>
        <%--<input id="btnquery2"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="jszc()" value="结束仲裁" />--%>
        <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="button" value="关闭" onclick="closedetail();" />
    </div>
</div>
</div>
</div>
</div>

<div class="modal hide fade" id="popUpDiv">
    <div class="modal-body">
        <div class="progress progress-striped active">
            <div class="bar" style="width: 100%;"></div>
        </div>
    </div>
</div>
</body>
</html>
