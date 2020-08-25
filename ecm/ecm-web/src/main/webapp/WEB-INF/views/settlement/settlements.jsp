<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>结算单查询</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#treeTable").treeTable({expandLevel : 1});
            $("#searchForm").validate({
                rules:{
                    settlement_id:{
                        digits:true
                    }
                }
            });
            $("#btnQuery").click(function(){
                $("#seachFrom").submit();
            });
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
            $("#btnAllSettle").click(function(){
                // 批量结算
                var dat = [];
                $("input:checked:not(#allCheck)").each(function(i,t){
                    dat.push($(t).val());
                });
                $.ajax({
                    url:"settle",
                    dataType:"json",
                    data:"ids="+dat,
                    success:function(data){
                        if(data.success){
                            $.jBox.info(data.msg,'info', { closed: function () { $("#btnQuery").click() } });
                        }else{
                            $.jBox.info(data.msg);
                        }
                    }
                });
            });

        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
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
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/settle/settlements"  >
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="container-fluid">
               <%--  <div class="row-fluid">
                    <div class="span9">
                        <label class="label-control" for="platformId" title="平台分类">
                            平台分类
                        </label>
                        <select name="platformId1" id="platformId1"  class="form-control input-small" onchange="loadsub('1')">
                            <option value="">一级类目</option>
                            <c:forEach items="${platformList}" var="item">
                                <option value="${item.categoryCid}">${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                        <select name="platformId2" id="platformId2"  class="form-control input-small" onchange="loadsub('2')">
                            <option value="">二级类目</option>
                            <c:forEach items="${subItemList}" var="item">
                                <option value="${item.categoryCid}" <c:if test="${item.categoryCid==backrate.subcid}">selected="selected"</c:if> >${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                        <select name="platformId" id="platformId"   class="form-control input-small" >
                            <option value="">三级类目</option>
                            <c:forEach items="${tItemList}" var="item">
                                <option value="${item.categoryCid}" <c:if test="${item.categoryCid==backrate.tcid}">selected="selected"</c:if> >${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div> --%>
                <div class="row-fluid" style="margin-top: 10px">
                    <div class="span4">
                        <label class="label-control" for="shopName" title="店铺名称">
                            店铺名称
                        </label>
                        <input id="shopName" name="shopName" value="${shopName}" type="text" class="input-medium">
                    </div>
                    <div class="span4">
                        <label class="label-control" for="settlement_id" title="结算单号">
                            结算单号
                        </label>
                        <input id="settlement_id" name="settlement_id" value="${settle.settlement_id}" type="text" class="input-medium" title="结算单号只能是数字">
                    </div>
                    <div class="span4">
                        <label class="label-control" for="status" title="结算状态">
                            结算状态
                        </label>
                        <select name="status" id="status" class="input-medium" >
                            <option value="">全部</option>
                            <option value="0" ${settle.status==0 ?"selected='selected'":""}>待结算</option>
                            <option value="1" ${settle.status==1 ?"selected='selected'":""}>已结算</option>
                            <%-- <c:forEach items="${allStatus}" var="t">
	                            <c:choose>
	                                <c:when test="${settle.status==t.code}">
	                                    <option value="${t.code}" selected="selected">${t.label}</option>
	                                </c:when>
	                                <c:otherwise>
	                                    <option value="${t.code}">${t.label}</option>
	                                </c:otherwise>
	                            </c:choose>
                            </c:forEach> --%>
                        </select>
                    </div>
                </div>

                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="settlementDatestr" title="应结算日期">
                            应结算日期
                        </label>
                        <input id="settlementDatestr" name="settlementDatestr" value='<fmt:formatDate value="${settle.settlementDatestr}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-small Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly="readonly">到
                        <input id="settlementDateend" name="settlementDateend" value='<fmt:formatDate value="${settle.settlementDateend}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-small Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly="readonly">
                    </div>
                    <div class="span6">
                        <label class="label-control" for="havePaidDatestr" title="结算日期">
                            结算日期
                        </label>
                        <input id="havePaidDatestr" name="havePaidDatestr" value='<fmt:formatDate value="${settle.havePaidDatestr}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-small Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly="readonly">到
                        <input id="havePaidDateend" name="havePaidDateend" value='<fmt:formatDate value="${settle.havePaidDateend}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-small Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly="readonly">
                    </div>
                </div>

                <div class="row-fluid">

                    <div class="span6">
                        <button class="btn btn-primary" id="btnQuery">查询</button>
                    </div>
                </div>
            </div>
        </form>
            <div class="container-fluid">
                <table id="treeTable" class="table table-striped table-bordered table-condensed">
                    <tr>
                        <th>序号</th>
                        <th>店铺名称</th>
                        <th>结算单号</th>
                        <th>状态</th>
                        <th>应结算日期</th>
                        <th>结算日期</th>
                    </tr>
                    <c:forEach items="${page.list}" varStatus="s" var="t">
                        <tr id="tr${t.settlement.id}">
                            <td>
                                ${s.count}
                                <%-- <input type="checkbox" id="ck${t.settlement.id}" name="ck" value="${t.settlement.id}" data=""> --%>
                            </td>
                            <td>${t.settlement.shopName}</td>
                            <td>${t.settlement.id}</td>
                            <td>${t.settlement.statusLabel}</td>
                            <td><fmt:formatDate value="${t.settlement.settlementDate}" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${t.settlement.havePaidDate}" pattern="yyyy-MM-dd"/></td>
                        </tr>
                        <tr id="str${t.settlement.id}" pId="tr${t.settlement.id}">
                            <td colspan="6">
                                <table style="width: 100%;height: 100%">
                                    <tr>
                                        <td rowspan='<c:if test="${fn:length(t.settlementDetailList)>0}">${fn:length(t.settlementDetailList)+1}</c:if>'>
                                            店铺：<br/>
                                                ${t.settlement.shopName}<br/>
                                            本期结算总额：<br/>
                                            ￥${t.settlement.settlementTotalMoney}元<br/>
                                           <%--  本期扣点总额：<br/>
                                            ￥${t.settlement.commissionTotalMoney}元<br/> --%>
                                        </td>
                                        <td>订单号</td>
                                        <td>订单金额</td>
                                        <td>扣点金额</td>
                                        <!-- <td>手续费</td> -->
                                        <td>退款金额</td>
                                        <td>结算金额</td>
                                       <!--  <td>结算状态</td> -->
                                        <td>备注</td>
                                    </tr>
                                    <c:forEach items="${t.settlementDetailList}" var="d">
                                        <tr>
                                            <td>${d.orderId}</td>
                                            <td>${d.orderPrice}</td>
                                            <td>${d.commission}</td>
                                            <%-- <td>${d.factorage}</td> --%>
                                            <td>${d.refundMoney}</td>
                                            <td>${d.sellerCashAccountIncome}</td>
                                            <%-- <td>
                                                ${d.statusLabel}
                                            </td> --%>
                                            <td>${d.remark}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <!-- <div class="row-fluid">
                    <div class="span1">
                        <input type="checkbox" id="allCheck">全选
                    </div>
                    <div class="span2">
                        <button type="button" class="btn btn-primary" id="btnAllSettle">批量结算</button>
                    </div>
                </div> -->
                <div class="pagination">${page}</div>
            </div>

    </div>
</div>

</body>
</html>