<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单手续费明细</title>
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
                <form:form id="searchForm" modelAttribute="factorageJournal" action="${ctx}/settle/factorageJournal" method="post" class="breadcrumb form-search">
                    <input id="page" name="page" type="hidden" value="${page.pageNo}"/>
                    <input id="rows" name="rows" type="hidden" value="${page.pageSize}"/>
                    <%-- <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/> --%>
                    <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                            <label class="label-left control-label"  for="noticeTitle" title="订单编号">
                               	 订单编号：
                            </label>
                            <form:input path="orderNo" id="orderNo" style="width:50%" type="text" class="form-control"   />
                        </div>
                        <div class="span5">
                        	<label class="label-left control-label"  for="noticeTitle" title="状态">
                                	状态：
                            </label>
                             <select name="fstatus" id="fstatus" style="width:50%"  class="form-control">
                                 <c:forEach items="${factorageStatuss}" var="factorageStatus" varStatus="s">
                                 	 <option <c:if test="${fstatus==factorageStatus.facCode}">selected="selected"</c:if> value="${factorageStatus.facCode}">${factorageStatus.label}</option>
                                 </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                            <label class="label-left control-label"  for="createDtBegin" title="创建时间">
                              	  创建时间：
                            </label>
                            <input value="${factorageJournal.createdBegin}" name="createdBegin" id="createdBegin" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:30%" type="text" class="form-control"   />
                         	   到<input value="${factorageJournal.createdEnd}" name="createdEnd" id="createdEnd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:30%" type="text" class="form-control"   />
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
                    <th style="width: 5%;">订单编号</th>
                    <th style="width: 16%;">手续费</th>
                    <th style="width: 5%;">状态</th>
                    <th style="width: 12%;">创建时间</th>
                    <th style="width: 12%;">编辑时间</th>
                </tr></thead>
                <tbody>
                <c:forEach items="${page.list}" var="factorageJournalDTO" varStatus="s">
                    <tr>
                        <td><c:out value="${s.count}" /></td>
                        <td>${factorageJournalDTO.orderNo}</td>
                        <td>${factorageJournalDTO.factorage}</td>
                       
                        <td>
                            ${factorageJournalDTO.status.label}
                        </td>
                        <td>
                            <fmt:formatDate value="${factorageJournalDTO.created}" pattern="yyyy-MM-dd hh:mm" type="date" dateStyle="long" />
                        </td>
                        <td>
                            <fmt:formatDate value="${factorageJournalDTO.updated}" pattern="yyyy-MM-dd hh:mm" type="date" dateStyle="long" />
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