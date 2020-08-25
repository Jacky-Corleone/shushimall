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

	function viewCouponInfo(id){
		var url="${ctx}/couponInfo/viewCouponInfo?id="+id;
	    var title = "查看优惠劵活动";
	    parent.openTab(url,title,"ge"+id);
	}
	function editCouponInfo(id){
	    var url="${ctx}/couponInfo/editCouponInfo?id="+id;
	    var title = "编辑优惠劵活动";
	    parent.openTab(url,title,"ge"+id);
	}
	function deleteCouponInfo(id){
		top.$.jBox.confirm("确认要删除吗？","系统提示",function(v,h,f){
            if(v == "ok"){
            	$.ajax({
                    url:"${ctx}/couponInfo/deleteCouponInfo",
                    type:"post",
                    data:{
                    	id:id
                    },
                    dataType:'json',
                    success:function(data){
                    	if(data.success){
                    		$.jBox.info(data.msg);
                    		location.href ="${ctx}/couponInfo/couponInfoList";
                    	}
                    }
                });
            }
        },{buttonsFocus:1});
	}
	//优惠劵送审
	function songsh(id,state){
		top.$.jBox.confirm("确认要送审吗？","系统提示",function(v,h,f){
            if(v == "ok"){
            	updateStateAjax(id,state);
            }
        },{buttonsFocus:1});
	}
	//优惠劵信息终止
	function stop(id,state){
		top.$.jBox.confirm("确认要终止吗？","系统提示",function(v,h,f){
            if(v == "ok"){
            	updateStateAjax(id,state);
            }
        },{buttonsFocus:1});
	}
	//修改优惠劵信息的ajax请求
	function updateStateAjax(id,state){
		$.ajax({
            url:"${ctx}/couponInfo/updateCouponInfoStatus",
            type:"post",
            data:{
            	id:id,
            	state:state
            },
            dataType:'json',
            success:function(data){
            	if(data.success){
            			location.href ="${ctx}/couponInfo/couponInfoList";
            	}else{
            		$.jBox.info(data.errorMessages);
            	}
            }
        });
	}
	var trObj;
	// 打开增加优惠券数量窗口
	function openModifyNumDialog(id, obj) {
		// 存储行对象
		trObj = obj;
		// 1.获取和清空
		var tr = $(obj).parent().parent().find('td');
		$("#priceValue td:eq(0)").html($(tr[9]).text());
		$("#priceValue td:eq(1)").html($(tr[10]).text());
		$("#couponNum").val('');
		$("#couponId").val(id);
		// 2.打开弹出框
		$("#amountIncWindow").modal("show");
	}

	// 关闭增加优惠券数量窗口
	function closeModifyNumDialog() {
		$("#amountIncWindow").modal("hide");
	}

    
    function compareUserCount(value){
    	//正整数
    	var zzs = /^[1-9]*[1-9][0-9]*$/;
    	if(!zzs.test(value)){
			$("#couponNum").val("1");
			alert("增加优惠券数量请输入大于0的整数");
			return false;
		}else{
			var c = parseInt(value) + parseInt($("#modify_oldNum").html());
			if(c > 999999){
				alert("优惠券数量最大为 99,9999");
				return false;
			}
		}
   		return true;
    }
	// 增加优惠券数量[动作]
	function modifyCouponsNum(){
    	var value = $("#couponNum").val();
    	var couponId = $("#couponId").val();
    	if(compareUserCount(value)){
    		$.ajax({
    			type: "POST",
    			dataType: "json",
    			url: $("#modifyCouponsNumForm").attr('action'),
    			data: $("#modifyCouponsNumForm").serialize(),
    			success: function(data){
    				alert(data.resultMessage);
    				if(data.resultMessage == "增加成功"){
    					var tr = $(trObj).parent().parent().find('td');
    					$(tr[9]).text(parseInt($(tr[9]).text())+parseInt(value));
    					closeModifyNumDialog();
    				}
    			}
    		});
    	}
    }
	//查看驳回原因
	function viewRefundReason(rejectReason){
		$("#viewReason").modal('show');
		$("#rejectReason").val(rejectReason);
		
	}
	function rejectClose(){
		$("#viewReason").modal('hide');
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
					<form:form id="searchForm" modelAttribute="couponsDTO" action="${ctx}/couponInfo/couponInfoList" method="post" class="breadcrumb form-search">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo }" />
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize }" />
						<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
						<div class="row-fluid" style="margin-top: 10px;">
							<div class="span4">
								<label class="label-left control-label" for=id title="优惠劵编码"> 优惠劵编码： </label>
								<form:input path="couponId" id="id" style="width:50%" type="text"  onkeyup="this.value=this.value.replace(/\D/g,'')" class="form-control" />
							</div>
							<div class="span4">
								<label class="label-left control-label" for="couponName" title="活动名称"> 优惠劵名称： </label>
								<form:input path="couponName" id="activityName" style="width:50%" type="text" class="form-control" />
							</div>
							<div class="span4">
								<label class="label-left control-label" for="state" title="活动状态"> 活动状态： </label>
								<form:select path="state" id="onlineState" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<form:option value="4">待送审</form:option>
									<form:option value="5">待审核</form:option>
									<form:option value="6">审核驳回</form:option>
									<form:option value="0">未开始</form:option>
									<form:option value="1">已开始</form:option>
									<form:option value="2">已结束</form:option>
									<form:option value="3">已终止</form:option>
								</form:select>
								<input type="submit" class="btn btn-primary"  value="搜索">
							</div>
						</div>
						<div class="row-fluid" style="margin-top: 10px;display:none">
							<div class="span4">
								<label class="label-left control-label" for="isAllItem" title="用户类型"> 用户类型： </label>
								<form:select path="couponUserType" id="userType" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<form:option value="0">全部用户</form:option>
									<form:option value="1">个人用户</form:option>
									<form:option value="2">企业用户</form:option>
								</form:select>
							</div>
							<div class="span4">
								<label class="label-left control-label" for="membershipLevel" title="会员等级"> 会员等级： </label>
								<form:select path="couponUserMembershipLevel" id="membershipLevel" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<c:set var="vipLevels" value="<%= VipLevelEnum.values() %>" />
									<c:forEach var="vipLevel" items="${vipLevels}">
										<form:option value="${vipLevel.id}">${vipLevel.name}</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						<div class="row-fluid" style="margin-top: 10px;display:none">
							<div class="span4">
								<label class="label-left control-label" for="membershipLevel" title="会员等级"> 所属平台： </label>
								<form:select path="platformId" id="platformId" style="width:50%" class="form-control">
									<form:option value="">请选择</form:option>
									<form:option value="4">全部</form:option>
									<form:option value="1">PC端</form:option>
									<form:option value="3">移动端</form:option>
								</form:select>
							</div>
							<div class="span2">
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
							<th>优惠劵编码</th>
							<th>优惠劵名称</th>
							<th>优惠劵类型</th>
							<!-- <th>用户类型</th>
							<th>会员等级</th>
							<th>使用平台</th> -->
							<th>适用范围</th>
	                        <th>优惠劵面额</th>
	                        <th>发放数量</th>
	                        <th>已领取数量</th>
	                        <th>有效期</th>
	                        <th>创建人</th>
	                        <th>创建时间</th>
	                        <th>状态</th>
	                        <th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${pager.list}" var="couponsDTO" varStatus="s">
	                      <tr id="${couponsDTO.id}" >
	                          <td><c:out value="${s.count}" /></td>
	                          <td>${couponsDTO.couponId}</td>
	                          <td style="width:80px;word-break:break-all;white-space:normal">${couponsDTO.couponName}</td>
	                          <td>
	                            <c:if test="${couponsDTO.couponType==1}">满减劵</c:if>
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
	                          <td>${couponsDTO.count}</td>
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
		                          <c:if test="${couponsDTO.state==6}"><spn style="color:red;cursor:pointer" title="点击此处查看驳回原因" onclick="viewRefundReason('${couponsDTO.rejectReason}');">审核驳回</spn></c:if>
		                          <c:if test="${couponsDTO.state==3}">已终止</c:if>
		                          <c:if test="${couponsDTO.state==2}">已结束</c:if>
		                          <c:if test="${couponsDTO.state==1}">已开始</c:if>
		                          <c:if test="${couponsDTO.state==0}">未开始</c:if>
	                          </td>
	                          <td>
	                          	<c:if test="${couponsDTO.state==4}">
	                          		<a href="javascript:void(0)" onclick="javascript:viewCouponInfo('${couponsDTO.couponId}')" >查看</a>
	                          		<a href="javascript:void(0)" onclick="javascript:editCouponInfo('${couponsDTO.couponId}')">编辑</a>
	                          		<a href="javascript:void(0)" onclick="javascript:songsh('${couponsDTO.couponId}',5)">送审</a>
	                          		<a href="javascript:void(0)" onclick="javascript:deleteCouponInfo('${couponsDTO.couponId}')">删除</a>
	                          	</c:if>
	                          	<c:if test="${couponsDTO.state==5}">
	                          		<a href="javascript:void(0)" onclick="javascript:viewCouponInfo('${couponsDTO.couponId}')" >查看</a>
	                          	</c:if>
	                          	<c:if test="${couponsDTO.state==6}">
	                          		<a href="javascript:void(0)" onclick="javascript:viewCouponInfo('${couponsDTO.couponId}')" >查看</a>
	                          		<a href="javascript:void(0)" onclick="javascript:editCouponInfo('${couponsDTO.couponId}')">编辑</a>
	                          		<a href="javascript:void(0)" onclick="javascript:songsh('${couponsDTO.couponId}',5)">送审</a>
	                          		<a href="javascript:void(0)" onclick="javascript:deleteCouponInfo('${couponsDTO.couponId}')">删除</a>
	                          	</c:if>
	                          	<c:if test="${couponsDTO.state==0}">
	                          		<a href="javascript:void(0)" onclick="javascript:viewCouponInfo('${couponsDTO.couponId}')" >查看</a>
	                          		<a href="javascript:void(0)" onclick="javascript:stop('${couponsDTO.couponId}',3)">终止</a>
	                          	</c:if>
	                          	<c:if test="${couponsDTO.state==1}">
	                          		<a href="javascript:void(0)" onclick="javascript:viewCouponInfo('${couponsDTO.couponId}')" >查看</a>
	                          		<a href="javascript:void(0)" onclick="javascript:stop('${couponsDTO.couponId}',3)">终止</a>
	                          		<a href="javascript:void(0)" onclick="javascript:openModifyNumDialog('${couponsDTO.couponId}',this)">增加优惠券数量</a>
	                          	</c:if>
	                          	<c:if test="${couponsDTO.state==2}">
	                          		<a href="javascript:void(0)" onclick="javascript:viewCouponInfo('${couponsDTO.couponId}')" >查看</a>
	                          		<a href="javascript:void(0)" onclick="javascript:deleteCouponInfo('${couponsDTO.couponId}')">删除</a>
	                          	</c:if>
	                          	<c:if test="${couponsDTO.state==3}">
	                          		<a href="javascript:void(0)" onclick="javascript:viewCouponInfo('${couponsDTO.couponId}')" >查看</a>
	                          		<a href="javascript:void(0)" onclick="javascript:deleteCouponInfo('${couponsDTO.couponId}')">删除</a>
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
	<div class="modal hide fade" id="amountIncWindow">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>增加优惠券数量</h3>
		</div>
		<div class="modal-body">
			<div class="container-fluid" style="text-align: center;">
			<form id="modifyCouponsNumForm" action="${ctx}/couponInfo/modifyCouponsNum">
				<table style="text-align: center;"
					class="table table-striped table-bordered table-condensed td-cen hhtd tree_table">
					<tr>
						<th>优惠券数量</th>
						<th>已领取数量</th>
						<th>请输入增加数量</th>
					</tr>
					<tbody>
						<tr id="priceValue">
							<td id="modify_oldNum"></td>
							<td></td>
							<td><input type="text" id="couponNum" name="couponNum" onkeyup="this.value=this.value.replace(/\D/g,'')"
								maxlength="6"></td>
						</tr>
					</tbody>
				</table>
				<input type="hidden" name="couponId" id="couponId">
			</form>
			</div>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0);" onclick="closeModifyNumDialog()" class="btn">关闭</a> <a
				href="javascript:void(0);" onclick="modifyCouponsNum()" class="btn">确定</a>
		</div>
	</div>
	
<!--驳回原因查看-->
<div class="modal hide fade" id="viewReason">
<form id="rejectId">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>平台优惠券活动驳回原因查看</h3>
    </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span8">
                    <label class="label-control" for="rejectReason" title="驳回原因">驳回原因</label>
                    <textarea rows="3"  id="rejectReason" style="resize: none;" readonly="readonly"></textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" id="btnCloseApprove" onclick="rejectClose()">取消</a>
        </div>
 </form>
</div>
	
</body>
</html>