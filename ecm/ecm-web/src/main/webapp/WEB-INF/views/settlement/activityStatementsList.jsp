<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优惠活动结算单</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">
		td {
		   text-align: center;
		} 
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnquery").click(function(){
				$("#pageNo").val(1);
				$("#pageSize").val(10);
				$("#searchForm").attr("action","${ctx}/settle/activityStatements").submit();
			});
			 // 表格排序
			$("#btnExport").click(function(){
	        	var page=1;
	            var rows=$("#pageSize").val();
	            var orderId=$("#orderId").val();
	            var shopName=$("#shopName").val();
	            var state=$("#state").val();
	            var createdBegin=$("#createdBegin").val();
	            var createdEnd=$("#createdEnd").val();
	            $.ajax({
	                url:"${ctx}/settle/selectPageList/",
	                type:"post",
	                data:{
	                    page:page,
	                    rows:rows,
	                    orderId:orderId,
	                    state:state,
	                    shopName:shopName,
	                    createdBegin:createdBegin,
	                    createdEnd:createdEnd
	                },
	                dataType:'json',
	                success:function(data){
	                    if(data.success){
	                        var count=data.obj.count;
	                        if(count>0){
	                        	top.$.jBox.confirm("确认要导出当前页数据吗？","系统提示",function(v,h,f){
	                                if(v == "ok"){
	                                    $("#searchForm").attr("action","${ctx}/settle/exportCurrentpage").submit();
	                                }
	                            },{buttonsFocus:1});
	                            top.$('.jbox-body .jbox-icon').css('top','55px');
	                        }
	                    }
	                }
	            })
	        });
	        
	    });

	    function exportall(){
	        var size=500;
	        var page=1;
	        var rows=$("#pageSize").val();
	        var orderId=$("#orderId").val();
            var shopName=$("#shopName").val();
            var state=$("#state").val();
            var createdBegin=$("#createdBegin").val();
            var createdEnd=$("#createdEnd").val();
	        $.ajax({
	            url:"${ctx}/settle/selectPageList/",
	            type:"post",
	            data:{
	            	page:page,
                    rows:rows,
                    orderId:orderId,
                    state:state,
                    shopName:shopName,
                    createdBegin:createdBegin,
                    createdEnd:createdEnd
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
	                    $("#searchForm").attr("action","${ctx}/settle/exportCurrentpage").submit();
	                    page=page+1;
	                    callback(up,size,page,count,callback);
	                }else{
	                    page=page+1;
	                    callback(up,size,page,count,callback);
	                }
	            },{buttonsFocus:1});
	        }
	    }
	    
	    
	        
		function unset(){
	            $(':input','#searchForm')
	                    .not(':button, :submit, :reset, :hidden')
	                    .val('')
	                    .removeAttr('checked')
	                    .removeAttr('selected');
	            $("#pageNo").val(1);
	            $("#pageSize").val(10);
	            $("#searchForm").attr("action","${ctx}/settle/activityStatements").submit();
	    }
	
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/settle/activityStatements").submit();
	    }
	
	</script>
    <style>
        label.label-left{
            width: 25%;
            text-align: right;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
            <div class="row-fluid">
                <form:form id="searchForm" modelAttribute="activityStatements" action="${ctx}/settle/activityStatements" method="post" class="breadcrumb form-search">
                    <input id="pageNo" name="page" type="hidden" value="${page.pageNo}"/>
                    <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}"/>
                    <%-- <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/> --%>
                    <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                            <label class="label-left control-label"  for="noticeTitle" title="订单编号">
                               	 订单编号：
                            </label>
                            <form:input path="orderId" id="orderId" style="width:50%" type="text" class="form-control"   />
                        </div>
                        <div class="span5">
                        	<label class="label-left control-label"  for="noticeTitle" title="状态">
                                	状态：
                            </label>
                            <select name="state" id="state" style="width:50%"  class="form-control">
                            	<option value="0">全部</option>
                            	<option value="1" <c:if test="${1==activityStatements.state}">selected="selected"</c:if>>有效</option>
                                <option value="2" <c:if test="${2==activityStatements.state}">selected="selected"</c:if>>无效</option>
                            </select>
                        </div>
                    </div>
                    <div class="row-fluid" style="margin-top:10px;">
                    <div class="span5">
                    <label class="label-left control-label"  for="noticeTitle" title="店铺名称">
                               	店铺名称：
                            </label>
                             <form:input path="shopName" id="shopName" style="width:50%" type="text" class="form-control"   />
                        </div>
                        <div class="span5">
                            <label class="label-left control-label"  for="createDtBegin" title="创建时间">
                              	  创建时间：
                            </label>
                            <input value="${activityStatements.createdBegin}" name="createdBegin" id="createdBegin" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:30%" type="text" class="form-control"   />
                         	   到<input value="${activityStatements.createdEnd}" name="createdEnd" id="createdEnd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:30%" type="text" class="form-control"   />
                        </div>
                        
                    </div>
                    
                    <div class="row-fluid" style="margin-top: 8px;">
                        <div class="span5">
                            <label class="label-left control-label"></label>
                            <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" value="查询" />
                            <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="reset" value="重置" onclick="unset();" />
                        </div>
                        <div class="span5">
                            <label class="label-left control-label"></label>
                            <input id="btnExport"  class="btn  btn-primary" style="width: 26%;" type="button" value="导出当前页"  />
                            <input id="btnExportALL"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="exportall()" value="导出全部"  />
                        </div>
                       
                    </div>
                </form:form>
            </div>
        </div>
        <div class="container-fluid" style="margin-top: 10px">
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead><tr>
                    <th style="width: 5%;">序号</th>
                    <th style="width: 5%;">订单编号</th>
                    <th style="width: 5%;">店铺ID</th>
                    <th style="width: 5%;">店铺名称</th>
                    <th style="width: 12%;">创建时间</th>
                    <th style="width: 12%;">更新时间</th>
                    <th style="width: 5%;">活动优惠总金额</th>
                    <th style="width: 5%;">活动退款总金额</th>
                    <th style="width: 5%;">活动结算总金额</th>
                    <th style="width: 5%;">状态</th>
                </tr></thead>
                <tbody>
                <c:forEach items="${page.list}" var="activityStatementsDTO" varStatus="s">
                    <tr>
                        <td><c:out value="${s.count}" /></td>
                        <td>${activityStatementsDTO.orderId}</td>
                        <td>${activityStatementsDTO.shopId}</td>
                        <td>${activityStatementsDTO.shopName}</td>
                        <td>
                            <fmt:formatDate value="${activityStatementsDTO.createTime}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
                        </td>
                        <td>
                            <fmt:formatDate value="${activityStatementsDTO.updateTime}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
                        </td>
                        <td>${activityStatementsDTO.totalDiscountAmount}</td>
                        <td>${activityStatementsDTO.totalRefundAmount}</td>
                        <td>${activityStatementsDTO.totalSettleAmount}</td>
                        <td>
                            <c:if test="${activityStatementsDTO.state==1}"> 有效</c:if>
                            <c:if test="${activityStatementsDTO.state==2}"> 无效</c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="pagination text-right">${page}</div>
    </div>
</div>
</body>
</html>

