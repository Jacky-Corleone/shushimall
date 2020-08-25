<%@page import="com.camelot.openplatform.common.enums.VipLevelEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>平台优惠券审核列表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">
td {
	text-align: center;
}
</style>
<script type="text/javascript">
	//翻页
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
	}

	function viewCouponInfo(id){
		var url="${ctx}/couponInfo/viewCouponInfo?id="+id;
	    var title = "查看优惠券活动";
	    parent.openTab(url,title,"ge"+id);
	}
	//审核通过
	function verifyPass(id,state){
		top.$.jBox.confirm("确认要审核通过吗？","系统提示",function(v,h,f){
            if(v == "ok"){
            	updateStateAjax(id,state,'');
            }
        },{buttonsFocus:1});
	}
	//审核驳回
	function verifyBack(id,state){
		$("#statusId").val(state);
		$("#couponInfoId").val(id);
		
		$("#rejectId").validate({
            rules: {
            	rejectReason:"required"
            }
        });
		 $("#rejectDiv").modal('show');
	}		 
		
		/* top.$.jBox.confirm("确认要驳回该审核吗？","系统提示",function(v,h,f){
            if(v == "ok"){
            	updateStateAjax(id,onlineState);
            }
        },{buttonsFocus:1});
	} */
	
	//驳回通过
    function rejectAjax(){
    	var isValide = $("#rejectId").valid();
        if(isValide){
        var auditRemark=$("#rejectReason").val();
        $("#popUpDiv").modal('show');
        var id=$("#couponInfoId").val();
        var status=$("#statusId").val();
        updateStateAjax(id,status,auditRemark);
       }
    }
	function rejectClose(){
		$("#couponInfoId").val("");
        $("#statusId").val("");
        $("#rejectReason").val("");
		$("#rejectDiv").modal('hide');
	}
	//修改优惠券信息的ajax请求
	function updateStateAjax(id,state,auditRemark){
		$.ajax({
            url:"${ctx}/couponInfo/updateCouponInfoStatus",
            type:"post",
            data:{
            	id:id,
            	state:state,
            	auditRemark:auditRemark
            },
            dataType:'json',
            success:function(data){
            	if(data.success){
            			location.href ="${ctx}/couponInfo/couponsInfoVerifyIndex";
            	}else{
            		$.jBox.info(data.errorMessages);
            	}
            	
            }
        });
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
					<form:form id="searchForm" modelAttribute="couponsDTO" action="${ctx}/couponInfo/couponsInfoVerifyIndex" method="post" class="breadcrumb form-search">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo }" />
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize }" />
						<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
						<div class="row-fluid" style="margin-top: 10px;">
							<div class="span4">
								<label class="label-left control-label" for=id title="优惠券编码"> 优惠券编码： </label>
								<form:input path="couponId" id="id" style="width:50%" type="text"  onkeyup="this.value=this.value.replace(/\D/g,'')" class="form-control" />
							</div>
							<div class="span4">
								<label class="label-left control-label" for="couponName" title="活动名称"> 优惠券名称： </label>
								<form:input path="couponName" id="activityName" style="width:50%" type="text" class="form-control" />
							</div>
							<div class="span2">
                				<input type="submit" class="btn btn-primary"  value="搜索">
							</div>
							<div class="span4" style="display:none;">
								<label class="label-left control-label" for="membershipLevel" title="会员等级"> 所属平台： </label>
								<form:select path="platformId" id="platformId" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<form:option value="4">全部</form:option>
									<form:option value="1">PC端</form:option>
									<form:option value="3">移动端</form:option>
								</form:select>
							</div>
						</div>
						<div class="row-fluid" style="margin-top: 10px;display:none;">
							<div class="span4">
								<label class="label-left control-label" for="isAllItem" title="用户类型"> 用户类型： </label>
								<form:select path="couponUserType" id="userType" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<form:option value="0">全部用户</form:option>
									<form:option value="1">个人用户</form:option>
									<form:option value="2">企业用户</form:option>
								</form:select>
							</div>
							<div class="span4" style="display:none;">
								<label class="label-left control-label" for="membershipLevel" title="会员等级"> 会员等级： </label>
								<form:select path="couponUserMembershipLevel" id="membershipLevel" style="width:50%" class="form-control">
									<c:set var="vipLevels" value="<%= VipLevelEnum.values() %>" />
									<form:option value="">请选择</form:option>
									<c:forEach var="vipLevel" items="${vipLevels}">
										<form:option value="${vipLevel.id}">${vipLevel.name}</form:option>
									</c:forEach>
								</form:select>
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
							<th>优惠券编码</th>
							<th>优惠券名称</th>
							<th>优惠券类型</th>
							<!-- <th>用户类型</th>
							<th>会员等级</th>
							<th>使用平台</th> -->
							<th>适用范围</th>
	                        <th>优惠券面额</th>
	                        <th>发放数量</th>
	                        <th>有效期</th>
	                        <th>创建人</th>
	                        <th>创建时间</th>
	                        <th>状态</th>
	                        <th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="couponsDTO" varStatus="s">
	                      <tr id="${couponsDTO.id}" >
	                          <td><c:out value="${s.count}" /></td>
	                          <td>${couponsDTO.couponId}</td>
	                          <td>${couponsDTO.couponName}</td>
	                          <td>
	                            <c:if test="${couponsDTO.couponType==1}">满减券</c:if>
	                          	<c:if test="${couponsDTO.couponType==2}">折扣券</c:if>
	                          	<c:if test="${couponsDTO.couponType==3}">现金券</c:if>
	                          </td>
	                          <%-- <td>
	                          	<c:if test="${couponsDTO.couponUserType==0}">全部用户</c:if>
	                          	<c:if test="${couponsDTO.couponUserType==1}">个人用户</c:if>
	                          	<c:if test="${couponsDTO.couponUserType==2}">企业用户</c:if>
	                          </td>
	                          <td>
	                          	<c:set var="membershipLevel" value="${couponsDTO.couponUserMembershipLevel}"/>
								<c:forEach var="vipLevel" items="${vipLevels}">
									<c:if test="${fn:contains(membershipLevel, vipLevel.id)}">
										<div>${vipLevel.name}</div>
									</c:if>
								</c:forEach>
	                          </td>
	                          <td>
	                          	<c:if test="${couponsDTO.platformId==1}">PC端</c:if>
	                          	<c:if test="${couponsDTO.platformId==3}">移动端</c:if>
	                          	<c:if test="${couponsDTO.platformId==4}">全部平台</c:if>
	                          </td> --%>
	                          <td>
	                          	<c:if test="${couponsDTO.couponUsingRange==1}">平台通用类</c:if>
	                          	<c:if test="${couponsDTO.couponUsingRange==2}">店铺通用类</c:if>
	                          	<c:if test="${couponsDTO.couponUsingRange==3}">品类通用类</c:if>
	                            <c:if test="${couponsDTO.couponUsingRange==4}">SKU使用类</c:if>
	                          </td>
	                          <td>${couponsDTO.couponAmount}</td>
	                          <td>${couponsDTO.couponNum}</td>
	                          <td>
	                            <fmt:formatDate value="${couponsDTO.couponStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>至
	                            <fmt:formatDate value="${couponsDTO.couponEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                          </td>
	                          <td>${couponsDTO.userName}</td>
	                          <td>
	                            <fmt:formatDate value="${couponsDTO.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                          </td>
	                          <td>
		                          <c:if test="${couponsDTO.state==4}">待送审</c:if>
		                          <c:if test="${couponsDTO.state==5}">待审核</c:if>
		                          <c:if test="${couponsDTO.state==6}">审核驳回</c:if>
		                          <c:if test="${couponsDTO.state==3}">已终止</c:if>
		                          <c:if test="${couponsDTO.state==2}">已结束</c:if>
		                          <c:if test="${couponsDTO.state==1}">已开始</c:if>
		                          <c:if test="${couponsDTO.state==0}">未开始</c:if>
	                          </td>
	                          <td>
	                          	<c:if test="${couponsDTO.state==5}">
	                          	    <a href="javascript:void(0)" onclick="javascript:verifyPass('${couponsDTO.couponId}',1)" >审核通过</a>
	                          		<a href="javascript:void(0)" onclick="javascript:verifyBack('${couponsDTO.couponId}',6)" >审核驳回</a>
	                          		<a href="javascript:void(0)" onclick="javascript:viewCouponInfo('${couponsDTO.couponId}')" >查看</a>
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
	<!--审核驳回-->
<div class="modal hide fade" id="rejectDiv">
<form id="rejectId">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>平台优惠券活动驳回</h3>
    </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span8">
                    <label class="label-control" for="rejectReason" title="备注">备注</label>
                    <input type="hidden" id="couponInfoId"/>
                    <input type="hidden" id="statusId"/>
                    <textarea rows="3"  name="rejectReason" id="rejectReason" style="resize: none;" maxlength="250" ></textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary" id="btnSubApprove" onclick="rejectAjax()">提交</a>
            <a href="#" class="btn" id="btnCloseApprove" onclick="rejectClose()">取消</a>
        </div>
 </form>
</div>
<!--进度条-->
<div class="modal hide fade" id="popUpDiv">
    <div class="modal-body">
        <div class="progress progress-striped active">
            <div class="bar" style="width: 100%;"></div>
        </div>
    </div>
</div>
</body>
</html>