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
    <title>订单查询</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {

            $("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出数据吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        var n=$("li.active").text();
                        $("#pageNo").val(n);
                        $("#pageSize").val("10");

                        $("#searchForm").attr("action","${ctx}/salesorder/export").submit();
                    }
                },{buttonsFocus:1});
            });
        });
        function unset(){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            $("#searchForm").attr("action","${ctx}/salesorder/list/").submit();
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            selectpage(n);
            return false;
        }
        function selectpage(p){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            var page=p;
            var rows=$("#pageSize").val();
            //订单号
            var orderNum=$("#orderNum").val();
            //卖家账号
            var userName1=$("#userName1").val();
            //买家账号
            var userName2=$("#userName2").val();
            //订单状态
            var orderStace=$("#orderStace").val();
            //买家家评价状态
            var pjStace1=$("#pjStace1").val();
            //取消状态
            var qxFlag=$("#qxFlag").val();
            //卖家评价状态
            var pjStace2=$("#pjStace2").val();
            //下单时间开始
            var createTimeBegin=$("#createTimeBegin").val();
            //下单时间结束
            var createTimeEnd=$("#createTimeEnd").val();
            //主订单号
            var parentOrderId=$("#parentOrderId").val();
            //支付类型
            var paymentType=$("#paymentType").val();
          //平台类型
            var platformId=$("#platformId").val();
          //支付类型，1延期支付，2立即支付
            var payType = $("#payType").val();
            $.ajax({
                url:"${ctx}/salesorder/selectordertable/",
                type:"post",
                data:{
                page:page,
                rows:rows,
                orderNum:orderNum,
                userName1:userName1,
                userName2:userName2,
                orderStace:orderStace,
                pjStace1:pjStace1,
                qxFlag:qxFlag,
                pjStace2:pjStace2,
                createTimeBegin:createTimeBegin,
                createTimeEnd:createTimeEnd,
                parentOrderId:parentOrderId,
                paymentType:paymentType,
                platformId:platformId,
                payType:payType
                },
                dataType:'json',
                success:function(data){
                    $.jBox.closeTip();
                    if(data.success){
                        var html="<div class='pagination'>"+data.msg+"</div>";
                        $(".pagination").replaceWith(html);
                        var html2="";
                        var num=0;
                        $(data.obj.list).each(function(i,item){
                            num++;
                            html2=html2+"<tr><td>"+num+"</td><td>"+getcode(item.id)+"</td><td>"+getcode(item.parentOrderId)+"</td><td>"+getcode(item.sellerName)+"</td><td>"+getcode(item.buyerName)+"</td><td>"+getcode(item.name)+"</td><td>"+getcode(item.orderStace)+"</td><td>"+getcode(item.buyerPj)+"</td><td>"
                                    +getcode(item.sellerPj)+"</td><td>"+getcode(item.qxflag)+"</td><td>"+getcode(item.paymentprice)+"</td>"
                            if(item.payPeriod != null && item.payPeriod != ''){
                            	html2=html2+"<td>延期支付</td>"
                            }else{
                            	html2=html2+"<td>立即支付</td>"
                            }
                            html2 = html2+ "<td>"+getcode(item.zflx)+"</td><td>"+getcode(item.ordertime)+"</td><td>"+getcode(item.address)+"</td>"
                            
                            if(item.id){
                                html2=html2+"<td><a href='javascript:void(0)' onclick='salesdetail(\""+item.id+"\")'>明细<a>"
                                if(item.ishouse){
                                	html2=html2+"&nbsp;<a href='javascript:void(0)' onclick='orderPlanDetail(\""+item.id+"\")'>设计图<a>";
                                }
                                html2=html2+"</td>";
                            }else{
                                html2=html2+"<td></td>"
                            }
                            html2=html2+"</tr>";
                        });
                        $("#tabletbody").html(html2);
                        $("#pageNo").val(data.obj.pageNo);
                        $("#pageSize").val(data.obj.pageSize);
                    }
                },error:function(){
                    $.jBox.closeTip();
                }
            });
        }
        function getcode(date){
            if(date){
                return date;
            }else{
                return '';
            }
        }
        function salesdetail(uid){
            var url = "${ctx}/salesorder/orderdetail?orderId="+uid;
            var title = "订单明细";
            var id = "订单明细";
            parent.openTab(url,title,id);
        }
        
        function orderPlanDetail(uid){
            var url = "${ctx}/salesorder/orderPlanDetail?orderId="+uid;
            var title = "设计图上传";
            var id = "设计图上传";
            parent.openTab(url,title,id);
        }
        function exportall(){
            var size=500;
            var page=1;
            var rows=$("#pageSize").val();
            //订单号
            var orderNum=$("#orderNum").val();
            //卖家账号
            var userName1=$("#userName1").val();
            //买家账号
            var userName2=$("#userName2").val();
            //订单状态
            var orderStace=$("#orderStace").val();
            //买家家评价状态
            var pjStace1=$("#pjStace1").val();
            //取消状态
            var qxFlag=$("#qxFlag").val();
            //卖家评价状态
            var pjStace2=$("#pjStace2").val();
            //下单时间开始
            var createTimeBegin=$("#createTimeBegin").val();
            //下单时间结束
            var createTimeEnd=$("#createTimeEnd").val();
            //主订单号
            var parentOrderId=$("#parentOrderId").val();
            //支付类型
            var paymentType=$("#paymentType").val();
            //平台类型
            var platformId=$("#platformId").val();
            $.ajax({
                url:"${ctx}/salesorder/selectordertablecount/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    orderNum:orderNum,
                    userName1:userName1,
                    userName2:userName2,
                    orderStace:orderStace,
                    pjStace1:pjStace1,
                    qxFlag:qxFlag,
                    pjStace2:pjStace2,
                    createTimeBegin:createTimeBegin,
                    createTimeEnd:createTimeEnd,
                    parentOrderId:parentOrderId,
                    paymentType:paymentType,
                    platformId:platformId
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var count=data.obj.count;
                        if(count&&count>0){
                            var up=count/size;
                            var uy=count%size;
                            if(uy>0){
                                up=up+1;
                            }
                            exporthd(up,size,1,count,exporthd);
                        }
                    }else{
                        $.jBox.info("亲，系统繁忙请稍后再导出");
                    }
                },error:function(){
                    $.jBox.info("亲，系统繁忙请稍后再导出");
                }
            });
        }
        function exporthd(up,size,page,count,callback){
            if(page<=up){
                $("#pageNo").val(page);
                $("#pageSize").val(size);
                var tiao1=(page-1)*size+1;
                var tiao2=tiao1+size-1;
                if(tiao2>count){
                    tiao2=count;
                }
                var qurenmsg="确认要导出"+tiao1+"~"+tiao2+"的数据吗?"
                top.$.jBox.confirm(qurenmsg,"系统提示",function(v,h,f){
                    if(v=='ok'){
                        $("#searchForm").attr("action","${ctx}/salesorder/export").submit();
                        page=page+1;
                        callback(up,size,page,count,callback);
                    }else{
                        page=page+1;
                        callback(up,size,page,count,callback);
                    }
                },{buttonsFocus:1});
            }
        }
    </script>
    <style>
        h3{
            color:#000000;
            height: 46px;
            line-height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }

        table.td-cen th,.td-cen td{
            text-align: center;
        }.hhtd td{
             word-wrap:break-word;
             word-break:break-all;
         }

    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/salesorder/list/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${orderMap.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${orderMap.page.pageSize}" />
            <div class="row-fluid" style="margin-top:10px;">
                <div class="span4">
                    <label class="control-label"  for="orderNum" title="订单号">
                        &nbsp;&nbsp;&nbsp;订单号：
                    </label>
                    <input name="orderNum" id="orderNum"  type="text" class="form-control input-medium" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="18" />
                </div>
                <div class="span4">
                    <label class="control-label" for="parentOrderId" title="主订单号">
                        主订单号：
                    </label>
                    <input name="parentOrderId" id="parentOrderId"  type="text" class="form-control　input-medium" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="18" />
                </div>
                <div class="span4">
                    <label class="control-label" for="orderStace" title="支付类型">
                        支付类型：
                    </label>
                    <select name="paymentType" id="paymentType" class="form-control input-medium">
                        <option value="">请选择</option>
                        <option value="0">支付宝</option>
                        <option value="8">银联</option>
                        <!-- <option value="100">支付宝手机端</option>
                        <option value="4">支付宝银行</option>
                        <option value="1">京东支付</option>
                        <option value="101">京东支付手机端</option>
                        <option value="2">小印支付</option>
                        <option value="5">微信</option>
                        <option value="6">微信PC端</option>
                        <option value="7">积分支付</option>
                        <option value="3">线下</option> -->
                    </select>
                </div>
            </div>
            <div class="row-fluid" style="margin-top:10px;">

                <div class="span4">
                    <label class="control-label" for="orderStace" title="订单状态">
                        订单状态：
                    </label>
                    <select name="orderStace" id="orderStace" class="form-control input-medium">
                        <option value="">请选择</option>
                        <c:forEach items="${orderMap.mapzt.orderStace}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="span4">
                    <label class="control-label" for="pjStace1" title="买家评价状态">
                        买家评价：
                    </label>
                    <select name="pjStace1" id="pjStace1" class="form-control input-medium">
                        <option value="">请选择</option>
                        <c:forEach items="${orderMap.mapzt.pjStace}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="span4">
                    <label class="control-label" for="qxFlag" title="是否取消">
                        是否取消：
                    </label>
                    <select name="qxFlag" id="qxFlag"  class="form-control input-medium">
                        <option value="">请选择</option>
                        <c:forEach items="${orderMap.mapzt.qxStace}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="row-fluid" style="margin-top:8px ">
                <div class="span4">
                    <label class="control-label" for="pjStace2" title="卖家评价状态">
                        卖家评价：
                    </label>
                    <select name="pjStace2" id="pjStace2" class="form-control input-medium">
                        <option value="">请选择</option>
                        <c:forEach items="${orderMap.mapzt.pjStace}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="span4">
                    <label class="control-label" for="createTimeBegin" title="下单时间">
                        下单时间：
                    </label>
                    <input name="createTimeBegin" id="createTimeBegin" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createTimeEnd\')}',dateFmt:'yyyy-MM-dd'});"  type="text" class="input-small Wdate" />
                    至
                    <input name="createTimeEnd" id="createTimeEnd" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createTimeBegin\')}',dateFmt:'yyyy-MM-dd'});"  type="text" class="input-small Wdate" />
                </div>
                <div class="span4">
                    <label class="control-label" for="userName1" title="商家账号">
                        商家账号：
                    </label>
                    <input name="userName1" id="userName1"  type="text" class="form-control　input-medium" />
                </div>
            </div>
            <div class="row-fluid" style="margin-top:8px ">
            	<div class="span4">
                    <label class="control-label" for="payType" title="支付方式">
                        支付方式：
                    </label>
                    <select name="payType" id="payType" 
									class="form-control input-medium">
									<option value="">所有</option>
									<!-- <option value="1">延期付款</option> -->
									<option value="2">立即支付</option>
								</select>
                </div>
                <div class="span4">
                    <label class="control-label" for="userName2" title="买家账号">
                        买家账号：
                    </label>
                    <input name="userName2" id="userName2"  type="text" class="form-control input-medium" />
                </div>
                
      <!--          <div class="span3">
								<label class="label-left control-label" for="platformId"
									title="平台类型">平台类型：</label>
								<select name="platformId" id="platformId" 
									class="form-control input-medium">
									<option value="">所有</option>
									<option value="0">舒适100平台</option>
									<option value="2">绿印平台</option> 
								</select>
				</div>
		-->  		
            </div>
            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span4">
                    <label class="label-left control-label"></label>
                    <input id="btnquery"  class="btn  btn-primary"  type="button" onclick="selectpage(1)" value="查询" />
                    <input id="btncancle"  class="btn  btn-primary" type="button" value="重置" onclick="unset();" />
                    <input id="btnExport"  class="btn  btn-primary" type="button" value="导出当前页" />
                    <input id="btnExportAll" onclick="exportall()" class="btn  btn-primary" type="button" value="导出所有" />
                </div>
            </div>
        </form:form>
        <table id="contentTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
            <thead>
            <tr>
                <th width="3%">序号</th>
                <th width="8%">订单编号</th>
                <th width="7%">主订单号</th>
                <th width="7%">商家账号</th>
                <th width="6%">买家账号</th>
                <th width="7%">收货人</th>
                <th width="5%">订单状态</th>
                <th width="5%">买家评价</th>
                <th width="5%">卖家评价</th>
                <th width="4%">是否取消</th>
                <%--<th width="5%">配送方式</th>--%>
                <th width="4%">订单金额</th>
                <th width="4%">支付方式</th>
                <th width="4%">付款形式</th>
               <!--<th width="5%">优惠码</th>--> 
                <th width="9%">下单时间</th>
                <th width="15%">配送地址</th>
              <!-- <th width="9%">平台类型</th>-->
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${orderMap.page.list}" var="order" varStatus="s">
                <tr>
                    <td><c:out value="${s.count}" /></td>
                    <td>${order.id}</td>
                    <td>${order.parentOrderId}</td>
                    <td>${order.sellerName}</td>
                    <td>${order.buyerName}</td>
                    <td>${order.name}</td>
                    <td>${order.orderStace}</td>
                    <td>${order.buyerPj}</td>
                    <td>${order.sellerPj}</td>
                    <td>${order.qxflag}</td>
                    <%--<td>${order.shipMentType}</td>--%>
                    <td>${order.paymentprice}</td>
                    <td>
                    	<c:choose>
                            <c:when test="${order.payPeriod != null}">延期付款</c:when>
                            <c:otherwise>立即支付</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${order.zflx}</td>
                    <!--<td>${order.promocode}</td>-->
                    <td>${order.ordertime}</td>
                    <td>${order.address}</td>
                   <!--   <td>    
                             <c:choose>
                                <c:when test="${order.platformId==null}">舒适100平台</c:when>
<%--                                 <c:when test="${order.platformId==2}">绿印平台</c:when> --%>
                            </c:choose>
                            </td>-->
                    <td>
                        <c:if test="${order.id!=''}">
                            <a onclick="salesdetail('${order.id}')" href="javascript:void(0)">明细</a>&nbsp;
                            <c:if test="${order.ishouse != '' && order.ishouse !=null}">
                            	<a onclick="orderPlanDetail('${order.id}')" href="javascript:void(0)">设计图</a>
                            </c:if>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${orderMap.page}</div>
    </div>
</div>
</body>
</html>
