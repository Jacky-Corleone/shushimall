<%@page import="com.camelot.openplatform.common.enums.VipLevelEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>促销活动列表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
td {
	text-align: center;
}

fontStyle{
	font-family:"微软雅黑";
	font-size:10px;
	color:#000000;
}
</style>
<script type="text/javascript">
//翻页
function page(n,s){
	$("#page").val(n);
	$("#rows").val(s);
	$("#searchForm").submit();
}

	function viewPromotion(id){
		var url="${ctx}/frPromotion/viewFrPromotion?id="+id;
	    var title = "查看满减活动";
	    parent.openTab(url,title,"ge"+id);
	}
	//审核通过
	function verifyPass(id,onlineState){
		var now=new Date(); 
		var end=new Date($("#date_"+id).html());  
		
		if(Date.parse(now) > Date.parse(end)){
			alert("活动已经结束,不能审核通过");
			return;
		}
		top.$.jBox.confirm("确认要审核通过吗？","系统提示",function(v,h,f){
            if(v == "ok"){
            	updateStateAjax(id,onlineState,'');
            }
        },{buttonsFocus:1});
	}
	//审核驳回浮层弹出
	function verifyBackLayer(id,onlineState){
		$("#auditDismissedMsgLayer").modal("show");
		$("#idMsg").val(id);
		$("#onlineStateMsg").val(onlineState);

	}
	//审核驳回
	function verifyBack(){
		var now=new Date(); 
		var end=new Date($("#date_"+id).html());  
		
		if(Date.parse(now) > Date.parse(end)){
			alert("友情提示：活动已经结束");
		}
		var id = $("#idMsg").val();
		var onlineState = $("#onlineStateMsg").val();
		var msg = $("#auditDismissedMsg").val();
		if(msg.trim().length<=0){
			alert("请输入驳回原因");
			return;
		}
		top.$.jBox.confirm("确认要驳回该审核吗？","系统提示",function(v,h,f){
            if(v == "ok"){
            	updateStateAjax(id,onlineState,msg);
            }
        },{buttonsFocus:1});
	}
	//关闭浮层
	function closeAudit(){
		$("#closeLayer").click();
	}
	//修改促销信息的ajax请求
	function updateStateAjax(id,onlineState,auditDismissedMsg){
		$.ajax({
            url:"${ctx}/frPromotion/updateFrPromotionStatus",
            type:"post",
            data:{
            	id:id,
            	onlineState:onlineState,
            	auditDismissedMsg:auditDismissedMsg  //驳回原因
            },
            dataType:'json',
            success:function(data){
            	if(data.success){
            		location.href ="${ctx}/frPromotion/frPromotionVerifyIndex";
            	}
            }
        });
	}
	function searchPromotionVerify(n,s){
		if(n>0){
			
		}else{
			n =1;
		}
		if(s>0){
			
		}else{
			s =10;
		}
		$("#page").val(n);
		$("#rows").val(s);
		$("#searchForm").submit();
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
					<form:form id="searchForm" modelAttribute="promotionInfo" action="${ctx}/frPromotion/frPromotionVerifyIndex" method="post" class="breadcrumb form-search">
						<input id="page" name="page" type="hidden" value="${page.pageNo}" />
						<input id="rows" name="rows" type="hidden" value="${page.pageSize}" />
						<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
						<div class="row-fluid" style="margin-top: 10px;">
							<div class="span4">
								<label class="label-left control-label" for=id title="活动编码"> 活动编码： </label>
								<form:input path="id" id="id" style="width:50%" type="text" class="form-control" />
							</div>
							<div class="span4">
								<label class="label-left control-label" for="activityName" title="活动名称"> 活动名称： </label>
								<form:input path="activityName" id="activityName" style="width:50%" type="text" class="form-control" />
							</div>
							<div class="span4">
								<label class="label-left control-label" for="membershipLevel" title="会员等级"> 所属平台： </label>
								<form:select path="platformId" id="platformId" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<form:option value="1">舒适100平台</form:option>
								</form:select>
							</div>
						</div>
						<div class="row-fluid" style="margin-top: 10px;">
							<div class="span4">
								<label class="label-left control-label" for="isAllItem" title="活动范围"> 活动范围： </label>
								<form:select path="isAllItem" id="isAllItem" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<form:option value="1">所有商品</form:option>
									<form:option value="2">部分商品</form:option>
								</form:select>
							</div>
							
							<div class="span2">
                				<input type="button" class="btn btn-primary" onclick="javascript:searchPromotionVerify()"   value="搜索">
							</div>
							<%-- <div class="span4">
								<label class="label-left control-label" for="isAllItem" title="用户类型"> 用户类型： </label>
								<form:select path="userType" id="userType" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<form:option value="0">全部用户</form:option>
									<form:option value="1">个人用户</form:option>
									<form:option value="2">企业用户</form:option>
								</form:select>
							</div>
							<div class="span4">
								<label class="label-left control-label" for="membershipLevel" title="会员等级"> 会员等级： </label>
								<form:select path="membershipLevel" id="membershipLevel" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<c:set var="vipLevels" value="<%= VipLevelEnum.values() %>" />
									<c:forEach var="vipLevel" items="${vipLevels}">
										<form:option value="${vipLevel.id}">${vipLevel.name}</form:option>
									</c:forEach>
								</form:select>
							</div> --%>
						</div>
						
					</form:form>
				</div>
			</div>
			<div class="container-fluid" style="margin-top: 10px">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th style="width: 5%;">序号</th>
							<th style="width: 8%;">活动编码</th>
							<th style="width: 14%;">活动名称</th>
							<th style="width: 8%;">活动范围</th>
							<!-- <th style="width: 8%;">用户类型</th> -->
<!-- 							<th style="width: 8%;">会员等级</th> -->
							<th style="width: 8%;">活动广告语</th>
							<th style="width: 8%;">所属平台</th>
							<th style="width: 8%;">活动状态</th>
							<th style="width: 8%;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="promotionDTO" varStatus="s">
	                      <tr id="${promotionDTO.id}" >
	                          <td><c:out value="${s.count}" /></td>
	                          <td>${promotionDTO.id}</td>
	                          <td>${promotionDTO.activityName}</td>
	                          <td>
	                          	<c:if test="${promotionDTO.isAllItem==1}">所有商品</c:if>
	                          	<c:if test="${promotionDTO.isAllItem==2}">部分商品</c:if>
	                          </td>
	                         <%--  <td>
	                          	<c:if test="${promotionDTO.userType==0}">全部用户</c:if>
	                          	<c:if test="${promotionDTO.userType==1}">个人用户</c:if>
	                          	<c:if test="${promotionDTO.userType==2}">企业用户</c:if>
	                          </td> --%>
	                          <%-- <td>
	                          	<c:set var="membershipLevel" value="${promotionDTO.membershipLevel}"/>
								<c:forEach var="vipLevel" items="${vipLevels}">
									<c:if test="${fn:contains(membershipLevel, vipLevel.id)}">
										<div>${vipLevel.name}</div>
									</c:if>
								</c:forEach>
							  </td> --%>
	                          <td>${promotionDTO.words}</td>
	                          <td>
	                          	<c:if test="${promotionDTO.platformId==1}">舒适100平台</c:if>
	                          </td>
	                          <td>
		                          <c:if test="${promotionDTO.onlineState==4}">待送审</c:if>
		                          <c:if test="${promotionDTO.onlineState==5}">待审核</c:if>
		                          <c:if test="${promotionDTO.onlineState==6}">审核驳回</c:if>
		                          <c:if test="${promotionDTO.onlineState==3}">已中止</c:if>
		                          <c:if test="${promotionDTO.onlineState==2}">已结束</c:if>
		                          <c:if test="${promotionDTO.onlineState==1}">已开始</c:if>
		                          <c:if test="${promotionDTO.onlineState==0}">未开始</c:if>
	                          </td>
	                          <td>
	                          	<c:if test="${promotionDTO.onlineState==5}">
	                          		<a href="javascript:void(0)" onclick="javascript:verifyPass(${promotionDTO.id},1)" >审核通过</a>
	                          		<span style="display:none" id="date_${promotionDTO.id}"><fmt:formatDate value='${promotionDTO.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/></span>
	                          		<a href="javascript:void(0)" onclick="javascript:verifyBackLayer(${promotionDTO.id},6)" >审核驳回</a>
	                          		<a href="javascript:void(0)" onclick="javascript:viewPromotion(${promotionDTO.id})" >查看</a>
	                          	</c:if>
	                          </td>
	                          
	                      </tr>
	                   </c:forEach>
					</tbody>
				</table>
			</div>
			<div class="pagination text-right">${page}</div>
		</div>
	</div>
	<!--审核驳回的弹出框 -->
	<div class="modal hide fade in" style="width:500px;height:220px;align:center;" id="auditDismissedMsgLayer">
		<div class="modal-header">
        <button type="button" class="close" id="closeLayer" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h5 id="titleId">审核驳回</h5>
    	</div>
		<label style="margin-left:10px;"><h5 class="fontStyle">驳回原因：</h5></label><br/>
		<textarea rows="5" cols="10" id="auditDismissedMsg"style="width:95%;margin-left:10px;" name=""></textarea><br/>
		<input type="button" style="margin-left:10px;" onclick="verifyBack()" value="提交"/>
		<input type="button" style="margin-left:10px;" onclick="closeAudit()" value="取消"/>
		<input type="hidden" id="idMsg"/>  <!-- id信息 -->
 		<input type="hidden" id="onlineStateMsg"/> <!--状态信息   -->
	</div>
</body>
</html>