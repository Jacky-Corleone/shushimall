<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>支付记录查询</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#treeTable").treeTable({expandLevel : 1});
            $("#btnQuery").click(function(){
                var url = "${ctx}/settle/payrecord";
                $("#searchForm").attr("action",url);
                $("#searchForm").submit();
            });
            /*$("#btnExport").click(function(){
                var url = "${ctx}/settle/exportPay?flag=0";
                $("#searchForm").attr("action",url);
                $("#searchForm").submit();
            });
            $("#btnExportAll").click(function(){
                var url = "${ctx}/settle/exportPay?flag=1";
                $("#searchForm").attr("action",url);
                $("#searchForm").submit();
            });*/

            $("#allCheck").click(function(){
                if($("#allCheck").attr("checked")){
                    $(":checkbox:not(#allCheck)").each(function(i,t){
                        $(t).attr("checked",true);
                    });
                }else{
                    $(":checkbox:not(#allCheck)").each(function(i,t){
                        $(t).attr("checked",false);
                    });
                }
            });


        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            var url = "${ctx}/settle/payrecord";
            $("#searchForm").attr("action",url);
            $("#searchForm").submit();
            return false;
        }
        function loadsub(flag){
            var html;
            var url;

            switch (flag){
                case '1':
                    var pid = $("#platformId1").val();
                    html = "<option value=''>二级类目</option>";
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $('#platformId2').select2("val", "");
            		$("#platformId").html("<option value=''>三级类目</option>");
            		$("#platformId").select2("val","");
                    $("#shopId").empty();
                    break;
                case '2':
                    var pid = $("#platformId2").val();
                    html = "<option value=''>三级类目</option>";
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $("#platformId").select2("val","");
                    $("#shopId").empty();
                    break;
            }
            $.ajax({
                url:url,
                type:"post",
                dataType:'json',
                success:function(data){
                    $(data).each(function(i,item){
                        html += "<option value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                    });
                    switch (flag){
                        //加载第二级
                        case '1':
                            $("#platformId2").html(html);
                            break;
                        //加载第三级
                        case '2':
                            $("#platformId").html(html);
                            break;
                    }
                }
            });

        }
        function checkShop(){

        }

        function loadOrderInfo(orderId){
            var url = "${ctx}/salesorder/orderdetail?orderId="+orderId;
            parent.openTab(url,"订单详情",'order'+orderId);
        }
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/settle/payrecord"  >
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span4">
                        <label class="label-control" for="orderId" title="订单号">
                            订&nbsp;&nbsp;单&nbsp;&nbsp;号
                        </label>
                        <input id="orderId" name="orderId" value="${orderQuery.orderId}" type="text" class="input-medium">
                    </div>
                    <div class="span6">
                        <label class="label-control" for="paymentTimeStart" title="支付时间">
                            支付时间
                        </label>
                        <input id="paymentTimeStart" name="paymentTimeStart" value='<fmt:formatDate value="${orderQuery.paymentTimeStart}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-medium Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly="readonly">到
                        <input id="paymentTimeEnd" name="paymentTimeEnd" value='<fmt:formatDate value="${orderQuery.paymentTimeEnd}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-medium Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly="readonly">
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span4">
                        <label class="label-control" for="paymentType" title="支付方式">
                            支付方式
                        </label>
                        <select name="paymentType" id="paymentType" class="input-medium">
                            <option value="">全部</option>
                            <%-- <option value="1" <c:if test="${orderQuery.paymentType==1}">selected="selected" </c:if> >京东支付</option>
                            <option value="2" <c:if test="${orderQuery.paymentType==2}">selected="selected" </c:if> >小印支付</option> --%>
                            <option value="0" <c:if test="${orderQuery.paymentType==0}">selected="selected" </c:if> >支付宝</option>
                            <%-- <option value="3" <c:if test="${orderQuery.paymentType==3}">selected="selected" </c:if> >线下支付</option>
                            <option value="4" <c:if test="${orderQuery.paymentType==4}">selected="selected" </c:if> >支付宝其他银行</option>
                            <option value="5" <c:if test="${orderQuery.paymentType==5}">selected="selected" </c:if> >微信</option>
                            <option value="6" <c:if test="${orderQuery.paymentType==6}">selected="selected" </c:if> >微信pc端</option>
                            <option value="100" <c:if test="${orderQuery.paymentType==100}">selected="selected" </c:if> >支付宝手机端</option>
                            <option value="101" <c:if test="${orderQuery.paymentType==101}">selected="selected" </c:if> >京东支付手机端</option> --%>
                            <option value="8" <c:if test="${orderQuery.paymentType==8}">selected="selected" </c:if> >银联</option>
                        </select>
                    </div>
                    <div class="span4">
                        <label class="label-control" for="state" title="订单状态">
                            订单状态
                        </label>
                        <select name="state" id="state" class="input-medium">
                            <option value="">全部</option>
                            <option value="1" <c:if test="${orderQuery.state==1}">selected="selected" </c:if>>待付款</option>
                            <option value="2" <c:if test="${orderQuery.state==2}">selected="selected" </c:if>>待配送/服务</option>
                            <option value="3" <c:if test="${orderQuery.state==3}">selected="selected" </c:if>>待收货/验收</option>
                            <option value="4" <c:if test="${orderQuery.state==4}">selected="selected" </c:if>>待评价</option>
                            <option value="5" <c:if test="${orderQuery.state==5}">selected="selected" </c:if>>已完成</option>
                            <option value="6" <c:if test="${orderQuery.state==6}">selected="selected" </c:if>>已取消</option>
                            <option value="7" <c:if test="${orderQuery.state==7}">selected="selected" </c:if>>已关闭</option>
                        </select>
                    </div>

                    <div class="span4">
                        <button class="btn btn-primary" id="btnQuery">查询</button>
                        <%--<button class="btn btn-primary" id="btnExport">导出当前页</button>
                        <button class="btn btn-primary" id="btnExportAll">导出所有记录</button>--%>
                    </div>
                </div>
            </div>
        </form>

        <table id="treeTable" class="table table-striped table-bordered table-condensed">
            <tr>
                <th>序号</th>
                <th>订单编号</th>
                <th>商家名称</th>
                <th>买家名称</th>
                <th>订单状态</th>
                <th>支付金额</th>
                <th>支付类型</th>
                <th>支付时间</th>
                <th>操作</th>
            </tr>
            <c:forEach items="${page.list}" varStatus="s" var="t">
                <tr id="tr${t.orderId}">
                    <td><c:out value="${s.count}" /></td>
                    <td>${t.orderId}</td>
                    <td>${t.sellerId}</td>
                    <td>${t.buyerId}</td>
                    <td>
                        <c:choose>
                            <c:when test="${t.state==1}">待付款</c:when>
                            <c:when test="${t.state==2}">
                          		<c:choose>
                         			<c:when test="${t.orderType==3}">待服务</c:when>
                          			<c:when test="${t.orderType==4}">待配送</c:when>
                     		 	</c:choose>
                            </c:when>
                            <c:when test="${t.state==3}">
                            	<c:choose>
                         			<c:when test="${t.orderType==3}">待验收</c:when>
                          			<c:when test="${t.orderType==4}">待收货</c:when>
                     		 	</c:choose>
                            </c:when>
                            <c:when test="${t.state==4}">待评价</c:when>
                            <c:when test="${t.state==5}">已完成</c:when>
                            <c:when test="${t.state==6}">已取消</c:when>
                            <c:when test="${t.state==7}">已关闭</c:when>
                        </c:choose>
                    </td>
                    <td>${t.paymentPrice}</td>
                    <td>
                        <c:choose>
                            <c:when test="${t.paymentType==0}">支付宝</c:when>
                            <c:when test="${t.paymentType==1}">京东支付</c:when>
                            <c:when test="${t.paymentType==2}">小印支付</c:when>
                            <c:when test="${t.paymentType==3}">线下支付</c:when>
                            <c:when test="${t.paymentType==4}">支付宝其他银行</c:when>
                            <c:when test="${t.paymentType==5}">微信</c:when>
                            <c:when test="${t.paymentType==6}">微信pc端</c:when>
                            <c:when test="${t.paymentType==100}">支付宝手机端</c:when>
                            <c:when test="${t.paymentType==101}">京东支付手机端</c:when>
                            <c:when test="${t.paymentType==8}">银联</c:when>
                        </c:choose>
                    </td>
                    <td><fmt:formatDate value="${t.paymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><a href="javascript:void(0)" onclick="loadOrderInfo(${t.orderId})">查看详情</a></td>
                </tr>
            </c:forEach>
        </table>
        <div class="pagination">${page}</div>
    </div>
</div>

</body>
</html>