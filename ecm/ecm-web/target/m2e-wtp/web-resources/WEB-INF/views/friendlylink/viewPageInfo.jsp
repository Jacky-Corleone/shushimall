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
		//$("friendlylinkPageDBId").val();
		$("#searchForm").submit();
	}

	/* function viewCouponInfo(id){
		var url="${ctx}/couponInfo/viewCouponInfo?id="+id;
	    var title = "查看优惠劵活动";
	    parent.openTab(url,title,"ge"+id);
	}
	function editCouponInfo(id){
	    var url="${ctx}/couponInfo/editCouponInfo?id="+id;
	    var title = "编辑优惠劵活动";
	    parent.openTab(url,title,"ge"+id);
	} */
	
	
	
	function pagesub(n, s) {
		$.jBox.tip("正在加载列表，请稍等",'loading',{opacity:0});
		if(!'${isView }'){
		$("#skuList").modal('show');
		if (n > 0) {

		} else {
			n = 1;
		}
		if (s > 0) {

		} else {
			s = 10;
		}
		$("#pageNo").val(n);
		$("#pageSize").val(s);

		var url = $("#searchForm1").attr("action");
		var searchFormObj = $('#searchForm1').serialize()+"&num="+$("#numId").val();
		$.ajax({
			url : url,
			type : "post",
			data : searchFormObj,
			cache : false,
			success : function(html) {
				$.jBox.closeTip();
				$("#skuList .goodList").html(html);
				if (checkboxsInit(4,'#skuList .goodList')) {
            		$("#selAllCheck").prop('checked',true);
            	};
			}
		});
		}
	}
	function deleteFriendlylinkPage(id){
		top.$.jBox.confirm("确认要解除绑定吗？","系统提示",function(v,h,f){
            if(v == "ok"){
            	var pageId = $("#friendlylinkPageDBId").val();
            	$.ajax({
                    url:"${ctx}/friendlylink/unboundlink",
                    type:"post",
                    data:{
                    	itemId:id,
                    	pageId:pageId
                    },
                    dataType:'json',
                    success:function(data){
                    	if(data.success){
                    		$.jBox.info(data.msg);
                    		location.href ="${ctx}/friendlylink/viewpage?id="+pageId;
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
					<form:form id="searchForm" modelAttribute="couponsDTO" action="${ctx}/friendlylink/viewpage" method="post" class="breadcrumb form-search">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo }" />
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize }" />
						<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
						<input id="friendlylinkPageDBId" name="id" type="hidden" value="${friendlylinkPageDB.id}" />
						 <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">页面名称：</div>
	                    <div class="span7">
	                    ${friendlylinkPageDB.pageName}
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">页面URL：</div>
	                    <div class="span7">
	                    ${friendlylinkPageDB.pageUrl}
	                    </div>
	                </div>
					</form:form>
				</div>
			</div>
			 <legend ><span class="content-body-bg">已添加的友情链接列表</span></legend>
			<div class="container-fluid" style="margin-top: 10px">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>序号</th>
							<th>友情链接名称</th>
							<th>友情链接URL</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="friendlylinkItem" varStatus="s">
	                      <tr id="${friendlylinkItem.id}" >
	                          <td><c:out value="${s.count}" /></td>
	                          <td>${friendlylinkItem.linkName}</td>
	                          <td>${friendlylinkItem.linkUrl}</td>
	                          <td>
	                          		<a href="javascript:void(0)" onclick="javascript:deleteFriendlylinkPage('${friendlylinkItem.id}')">解除绑定</a>
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