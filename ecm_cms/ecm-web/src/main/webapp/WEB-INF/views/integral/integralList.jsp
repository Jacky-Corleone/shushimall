<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>积分管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
td {
	text-align: center;
}
</style>
<script type="text/javascript">
		$(document).ready(function() {
			// 表格排序
			//tableSort({callBack : page});
            $("#addNotice").click(function(){
                var url = "${ctx}/notice/form";
                parent.openTab(url,"添加公告","n0");
            });
		});
        function unset(){
            $(':input','#integralConfig')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $("#pageNo").val(1);
            $("#pageSize").val(10);
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#integralConfig").submit();
	    }
        function deleteNotice(noticeId){
        	var url = "${ctx}/notice/deleteNotice?noticeId="+noticeId;
        	if (confirm('是否确认删除！')) {
				 $.ajax({
						url : url,
						type : "get",
						dataType : "json",
						success : function(data) {
							if(data.success){
								$.jBox.success("操作成功!",'提示',{closed:function(){
									$("#searchForm").submit();
								}});
							}else{
								$.jBox.info("删除时出错,请稍后再试!");
							}
						},
						error : function(xmlHttpRequest, textStatus, errorThrown) {
							$.jBox.info("系统错误！请稍后再试！");
						}
					});
			 }
    	}
        function editNotice(id){
            var url = "${ctx}/notice/form?noticeId="+id;
            parent.openTab(url,"编辑公告","n1"+id);
        }
        function viewNotice(id){
            var url = "${ctx}/notice/viewNotice?noticeId="+id;
            parent.openTab(url,"查看公告","n2"+id);
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
        <div class="span3 offset9 text-right">
           <c:if test="${fn:length(page.list) <8}">
           <a href="${ctx}/integral/form" class="btn btn-primary" >新增积分配置</a>
           </c:if>
        </div>
    </div>
			</div>
			<div class="container-fluid" style="margin-top: 10px">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th >序号</th>
							<th >积分类型</th>
							<th >平台类型</th>
							<th >其他操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="integralConfigDTO" varStatus="s">
							<tr>
								<td><c:out value="${s.count}" /></td>
								<td>
									<c:if test="${not empty integralConfigDTO.integralType}">
										<c:if test="${integralConfigDTO.integralType=='1'}">
	                                    	金额返还积分
	                                	</c:if>
										<c:if test="${integralConfigDTO.integralType=='2'}">
	                                    	评价获取积分
	                                	</c:if>
										<c:if test="${integralConfigDTO.integralType=='3'}">
	                                    	订单使用积分
	                                	</c:if>
										<c:if test="${integralConfigDTO.integralType=='4'}">
	                                    	积分兑换金额
	                                	</c:if>
										<c:if test="${integralConfigDTO.integralType=='7'}">
	                                    	评价+晒单可获得积分
	                                	</c:if>
									</c:if>
								</td>
								<td>
									<c:if test="${not empty integralConfigDTO.platformId}">
										<c:if test="${integralConfigDTO.platformId=='0'}">
	                                    	舒适100平台
	                                	</c:if>
<%-- 										<c:if test="${integralConfigDTO.platformId=='2'}"> --%>
<!-- 	                                    	绿印平台 -->
<%-- 	                                	</c:if> --%>
									</c:if>
								</td>
								<td>
									<a href="${ctx }/integral/form?integralType=${integralConfigDTO.integralType}&&platformId=${integralConfigDTO.platformId}&&isView=view">查看</a>&nbsp;&nbsp;&nbsp;
									<a href="${ctx }/integral/form?integralType=${integralConfigDTO.integralType}&&platformId=${integralConfigDTO.platformId}&&isView=edit">编辑</a>
								</td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>