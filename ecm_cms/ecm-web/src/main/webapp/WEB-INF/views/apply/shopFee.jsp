<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>卖家缴费信息</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#treeTable").treeTable({expandLevel : 5});
            $("#searchForm").validate({
                rules: {
                    uid: {
                        digits: true
                    },
                    shopId:{
                        digits: true
                    }
                }
            });
            $("#btnQuery").click(function(){
                $("#searchForm").submit();
            });

            $("#btnSubApprove").click(function(){
                var shopId = $("#shopIdInfo").val();
                $.ajax({
                    url:"updateShopFee",
                    data:"shopId="+shopId,
                    dataType:"json",
                    success:function(data){
                        if(data.success){
                            $.jBox.info(data.msg);
                            $($("#tr"+shopId).children()[4]).html("已确认");
                            var html = "<a href='javascript:void(0)' onclick='approveShopFee(1,"+shopId+")'>查看详情</a>";
                            $($("#tr"+shopId).children()[5]).html(html);
                        }else{
                            $.jBox.info(data.msg);
                        }
                        $("#approveDiv").modal('hide');
                    }
                });
            });

            $("#btnCloseApprove").click(function(){
                $("#approveDiv").modal('hide');
            });

        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function approveShopFee(flag,id){
            $($("#proFee tr:not(:first)")).each(function(i,t){
                $(t).remove();
            });
            $($("#serFee tr:not(:first)")).each(function(i,t){
                $(t).remove();
            });
            $("#shopIdInfo").val(id);
            if(2==flag){
                $("#btnSubApprove").show();
            }else{
                $("#btnSubApprove").hide();
            }
            $("#approveDiv").modal('show');
            // 加载缴费信息
            $.ajax({
                url:"feeInfo",
                data:"shopId="+id,
                dataType:"json",
                success:function(data){
                    if(data.feeList){
                        $.each(data.feeList,function(i,t){
                            var phtml = "<tr>";
                            phtml += "<td>"+(i+1)+"</td>";
                            phtml += "<td>"+ t.cname+"</td>";
                            phtml += "<td>"+ t.subcname+"</td>";
                            phtml += "<td>"+ t.tcname+"</td>";
                            phtml += "<td>"+ t.cashDeposit+"元</td>";
                            phtml += "</tr>";

                            var sehtml = "<tr>";
                            sehtml += "<td>"+(i+1)+"</td>";
                            sehtml += "<td>"+ t.cname+"</td>";
                            sehtml += "<td>"+ t.subcname+"</td>";
                            sehtml += "<td>"+ t.tcname+"</td>";
                            sehtml += "<td>"+ t.serviceFee+"元</td>";
                            sehtml += "</tr>";
                            $("#proFee").append(phtml);
                            $("#serFee").append(sehtml);
                        });

                    }
                }
            });
        }

    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/shopFee/list"  >
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="companyName" title="公司名称">
                            公司名称
                        </label>
                        <input name="companyName" id="companyName"  type="text" class="form-control" value="${user.companyName}" />
                    </div>
                    <div class="span6">
                        <label class="label-control" for="uid" title="商家编号">
                            商家编号
                        </label>
                        <input type="text"  id="uid" name="uid"  value="${user.uid}" title="只能是数字"/>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="shopId" title="店铺编号">
                            店铺编号
                        </label>
                        <input type="text"  id="shopId" name="shopId"  value="${user.shopId}"/>
                    </div>
                    <div class="span6">
                        <label class="label-control" for="paymentStatus" title="缴费状态">
                            缴费状态
                        </label>
                        <select name="paymentStatus" id="paymentStatus">
                            <option value="">全部</option>
                            <option value="0"<c:if test="${user.paymentStatus==0}">selected="selected" </c:if>>待缴费</option>
                            <option value="1"<c:if test="${user.paymentStatus==1}">selected="selected" </c:if>>待确认</option>
                            <option value="2"<c:if test="${user.paymentStatus==2}">selected="selected" </c:if>>已确认</option>
                        </select>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        <button class="btn btn-primary" id="btnQuery">查询</button>
                    </div>
                </div>
            </div>
        </form>

            <table id="treeTable" class="table table-striped table-bordered table-condensed">
                <tr>
                    <th>编号</th>
                    <th>公司名称</th>
                    <th>商家编号</th>
                    <th>店铺编号</th>
                    <th>缴费状态</th>
                    <th>操作</th>
                </tr>
                <c:forEach items="${page.list}" var="s" varStatus="status">
                    <tr id="tr${s.shopId}">
                        <td>${status.count}</td>
                        <td>${s.companyName}</td>
                        <td>${s.uid}</td>
                        <td>${s.shopId}</td>
                        <td>
                            <c:choose>
                                <c:when test="${s.paymentStatus==1}">
                                    待确认
                                </c:when>
                                <c:otherwise>
                                    已确认
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${s.paymentStatus==1}">
                                    <a href="javascript:void(0)" onclick="approveShopFee(2,${s.shopId})">确认缴费</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:void(0)" onclick="approveShopFee(1,${s.shopId})">查看详情</a>
                                </c:otherwise>
                            </c:choose>

                        </td>
                    </tr>
                </c:forEach>

            </table>
        <div class="pagination">${page}</div>
    </div>
</div>

<input type="hidden" name="shopIdInfo" value="" id="shopIdInfo">
<!--卖家缴费信息弹出框-->
<div class="modal hide fade" id="approveDiv" style="overflow-y:auto ">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>卖家缴费信息</h3>
    </div>
        <div class="modal-body" style="overflow-y: auto;height: 320px">
            <div class="container-fluid">
                <legend ><span class="content-body-bg">平台质量保证金</span></legend>
                <table  class="table table-striped table-bordered table-condensed" id="proFee">
                    <tr>
                        <th>序号</th>
                        <th>一级类目</th>
                        <th>二级类目</th>
                        <th>三级类目</th>
                        <th>金额</th>
                    </tr>
                </table>

            </div>
            <div class="container-fluid">
                <legend ><span class="content-body-bg">平台使用费</span></legend>
                <table class="table table-striped table-bordered table-condensed" id="serFee">
                    <tr>
                        <th>序号</th>
                        <th>一级类目</th>
                        <th>二级类目</th>
                        <th>三级类目</th>
                        <th>金额</th>
                    </tr>
                </table>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary hide" id="btnSubApprove" >通过</a>
            <a href="#" class="btn" id="btnCloseApprove">取消</a>
        </div>

</div>

</body>
</html>