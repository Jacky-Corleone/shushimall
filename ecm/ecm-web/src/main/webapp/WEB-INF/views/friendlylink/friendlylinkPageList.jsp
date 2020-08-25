<%@page import="com.camelot.openplatform.common.enums.VipLevelEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>平台优惠劵列表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">
td {
	text-align: center;
}
h3 {
	color: #000000;
	height: 46px;
	line-height: 46px;
	text-indent: 20px;
	font-size: 15px;
	font-family: \5FAE\8F6F\96C5\9ED1;
	font-weight: 500;
}
</style>
<script type="text/javascript">
	//翻页
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
	}
	function viewFriendlylinkPage(id){
		var url="${ctx}/friendlylink/viewpage?id="+id;
	    var title = "查看页面友情链接详情";
	    parent.openTab(url,title,"ge"+id);
	}
	
	function boundLink(id){
		var url="${ctx}/friendlylink/boundlink?id="+id;
	    var title = "绑定页面友情链接";
	    parent.openTab(url,title,"ge"+id);
	}
	function editFriendlylinkPage(id,pageName,pageUrl){
	    var url="${ctx}/friendlylink/editpage?id="+id;
	    var title = "编辑友情链接";
	    parent.openTab(url,title,"ge"+id);
	}
	function deleteFriendlylinkPage(id){
		top.$.jBox.confirm("确认要删除吗？","系统提示",function(v,h,f){
			console.info("===========>>>");
            if(v == "ok"){
            	console.info("已确认===========>>>");
            	$.ajax({
                    url:"${ctx}/friendlylink/deletepage",
                    type:"post",
                    data:{
                    	id:id
                    },
                    dataType:'json',
                    success:function(data){
                    	console.info("删除成功=========>>"+data.success);
                    	if(data.success){
                    		console.info("删除成功=========>>"+data.success);
                    		$.jBox.info(data.msg);
                    		location.href ="${ctx}/friendlylink/pagelist";
                    	}
                    }
                });
            }
        },{buttonsFocus:1});
	}

</script>
<style>
label.label-left {
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
					<form:form id="searchForm" modelAttribute="friendlylinkPage" action="${ctx}/friendlylink/pagelist" method="post" class="breadcrumb form-search">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo }" />
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize }" />
						<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
						<div class="row-fluid" style="margin-top: 10px;">
						</div>
					</div>
					</form:form>
				</div>
			</div>
			<div class="container-fluid" style="margin-top: 10px">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>序号</th>
							<th>友情链接名称</th>
							<th>友情链接url</th>
	                        <th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="friendlylinkPage" varStatus="s">
	                      <tr id="${friendlylinkPage.id}" >
	                          <td><c:out value="${s.count}" /></td>
	                          <td style="width:80px;word-break:break-all;white-space:normal">${friendlylinkPage.pageName}</td>
	                          <td>${friendlylinkPage.pageUrl}</td>
	                          <%-- <td>
	                           <c:forEach items="${friendlylinkPage.pageItems}" var="linkItem">
	                           		${linkItem.item.linkName}
	                           </c:forEach>
	                           </td> --%>
	                          <td>
	                          		<a href="javascript:void(0)" onclick="javascript:viewFriendlylinkPage('${friendlylinkPage.id}')">详情</a>
	                          		<a href="javascript:void(0)" onclick="javascript:editFriendlylinkPage('${friendlylinkPage.id}')">编辑</a>
	                          		<a href="javascript:void(0)" onclick="javascript:boundLink('${friendlylinkPage.id}')">绑定新友情链接</a>
	                          		<a href="javascript:void(0)" onclick="javascript:deleteFriendlylinkPage('${friendlylinkPage.id}')">删除</a>
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