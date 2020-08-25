<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>提现记录</title>
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
				$("#page").val(1);
				$("#rows").val(10);
				$("#searchForm").submit();
			})
		});
        function unset(){
            $(':input','#searchForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $("#page").val(1);
            $("#rows").val(10);
            $("#searchForm").submit();
        }
		function page(n,s){
			$("#page").val(n);
			$("#rows").val(s);
			$("#searchForm").submit();
	    }
		
		//中信银行  查看提现状态
		function queryStatus(tradeNo){
	            $.ajax({
	                url: "${ctx}/financeWithdraw/queryStatus",
	                dataType:"json",
	                data:{
	                    tradeNo: tradeNo
	                },
	                success: function(data){
	                   alert(data.msg);
	                   if(data.success){
	                	   //查询并且修改成功  刷新页面
	                	   $("#btnquery").click();  
	                   }
	                }
	            });
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
                <form:form id="searchForm" modelAttribute="fwRecord" action="${ctx}/financeWithdraw/financeWithdrawIndex" method="post" class="breadcrumb form-search">
                    <input id="page" name="page" type="hidden" value="${page.pageNo}"/>
                    <input id="rows" name="rows" type="hidden" value="${page.pageSize}"/>
                    <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                        	<label class="label-left control-label"  for="noticeTitle" title="用户名">
                                	用户名：
                            </label>
                            <input type="text" name="userName" value="${fwRecord.userName}"/>
                        </div>
                    </div>
                    <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                            <label class="label-left control-label"  for="createDtBegin" title="创建时间">
                              	  创建时间：
                            </label>
                            <input value="${fwRecord.createdBegin}" name="createdBegin" id="createdBegin" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:30%" type="text" class="form-control"   />
                         	   到<input value="${fwRecord.createdEnd}" name="createdEnd" id="createdEnd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:30%" type="text" class="form-control"   />
                        </div>
                        <div class="span5">
                        </div>
                    </div>
                     <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                            <label class="label-left control-label"  for="createDtBegin" title="状态">
                              	  状态：
                            </label>
                             <select name="status" id="status" style="width:50%"  class="form-control">
                             	<option value="" <c:if test="${fwRecord.status==''}">selected="selected"</c:if>>请选择</option>
                             	<option value="10" <c:if test="${fwRecord.status==10}">selected="selected"</c:if>>提现处理中</option>
                             	<option value="20" <c:if test="${fwRecord.status==20}">selected="selected"</c:if>>提现申请失败</option>
                             	<option value="30" <c:if test="${fwRecord.status==30}">selected="selected"</c:if>>提现申请成功</option>
                             	<option value="40" <c:if test="${fwRecord.status==40}">selected="selected"</c:if>>提现失败</option>
                             	<option value="50" <c:if test="${fwRecord.status==50}">selected="selected"</c:if>>提现成功</option>
                             	<option value="60" <c:if test="${fwRecord.status==60}">selected="selected"</c:if>>未知</option>
                             	<option value="70" <c:if test="${fwRecord.status==70}">selected="selected"</c:if>>审核拒绝</option>
                             	<option value="80" <c:if test="${fwRecord.status==80}">selected="selected"</c:if>>用户撤销</option>
                             </select>
                        </div>
                        <div class="span5">
                        </div>
                    </div>
                    <div class="row-fluid" style="margin-top: 8px;">
                        <div class="span5">
                            <label class="label-left control-label"></label>
                            <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" value="查询" />
                            <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="reset" value="重置" onclick="unset();" />
                        </div>
                        <div class="span5">
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <div class="container-fluid" style="margin-top: 10px">
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead><tr>
                    <th style="width: 5%;">序号</th>
                    <th style="width: 5%;">交易号</th>
                    <th style="width: 16%;">用户名</th>
                    <th style="width: 5%;">提现金额</th>
                    <th style="width: 12%;">创建时间</th>
                    <th style="width: 12%;">状态</th>
                    <th style="width: 12%;">失败原因</th>
                    <th style="width: 12%;">操作</th>
                </tr></thead>
                <tbody>
                <c:forEach items="${page.list}" var="fwRecord" varStatus="s">
                    <tr>
                        <td><c:out value="${s.count}" /></td>
                        <td>${fwRecord.tradeNo}</td>
                        <td>${fwRecord.userName}</td>
                        <td>
                            ${fwRecord.amount}
                        </td>
                        <td>
                            <fmt:formatDate value="${fwRecord.createdTime}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
                        </td>
                        <td>
                        	<c:if test="${fwRecord.status==10}">提现处理中</c:if>
                        	<c:if test="${fwRecord.status==20}">提现申请失败</c:if>
                        	<c:if test="${fwRecord.status==30}">提现申请成功</c:if>
                        	<c:if test="${fwRecord.status==40}">提现失败</c:if>
                        	<c:if test="${fwRecord.status==50}">提现成功</c:if>
                        	<c:if test="${fwRecord.status==60}">未知</c:if>
                        	<c:if test="${fwRecord.status==70}">审核拒绝</c:if>
                        	<c:if test="${fwRecord.status==80}">用户撤销</c:if>
                        </td>
                        <td>
                            ${fwRecord.failReason}
                        </td>
                        <td>
                            <c:if test="${fwRecord.status==30}"><a href="javascript:queryStatus('${fwRecord.tradeNo}')">查看提现状态</a></c:if>
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