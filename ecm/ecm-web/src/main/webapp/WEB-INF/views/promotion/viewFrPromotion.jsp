<%@page import="com.camelot.openplatform.common.enums.VipLevelEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>满减活动添加</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
td {
	text-align: center;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		
		if(${promotion.isAllItem==2}){
			$.ajax({
				url:"${ctx}/frPromotion/findSelectedFrItem",
	            type:"post",
	            data:{
	            	id:${promotion.id}
	            },
	            dataType:'html',
	            success:function(data){
	            	$("#itemListDiv").html(data);
	            	$("#meetPrice").html($("#meetPriceTemp").val());
					$("#discountPrice").html($("#discountPriceTemp").val());
	            }
			});
		}
		
	});
	
	//翻页
	function page(n,s){
		$("#page").val(n);
		$("#rows").val(s);
		$.jBox.tip("正在加载列表，请稍等",'loading',{opacity:0});
		$.ajax({
            url:"${ctx}/frPromotion/findSelectedFrItem",
            type:"post",
            data:{
            	page:n,
            	rows:s,
            	id:${promotion.id}
            },
            dataType:'html',
            success:function(data){
            	$.jBox.closeTip();
            	$("#itemListDiv").html(data);
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
				<form name="addPromotionForm" modelAttribute="promotion" id="addPromotionForm" action="${ctx}/frPromotion/saveFrPromotion">
					<input name="selectItemKey" id="selectItemKey1" value="${selectItemKey}" type="hidden"></input>	
	                <legend ><span class="content-body-bg">基本信息</span></legend>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">活动名称：</div>
	                    <div class="span7">${promotion.activityName }</div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">活动规则：</div>
	                    <div class="span7">当上述所选商品在单笔订单满  <span id="meetPrice">${meetPrice}</span>元，订单总额减少<span id="discountPrice">${discountPrice}</span>元
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">开始时间：</div>
	                    <div class="span7"><fmt:formatDate value='${promotion.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/></div>
	                </div>
	                 <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">结束时间：</div>
	                    <div class="span7"><fmt:formatDate value='${promotion.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/></div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">广告语：</div>
	                    <div class="span7 ">${promotion.words}</div>
	                </div>
	                </br>
	                <legend ><span class="content-body-bg">权限信息</span></legend>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">用户类型：</div>
	                    <div class="span7">
	                    	<c:if test="${promotion.userType=='0'}">全部用户</c:if>
	                    	<c:if test="${promotion.userType=='1'}">个人用户</c:if>
	                    	<c:if test="${promotion.userType=='2'}">企业用户</c:if>
	                    </div>
	                </div>
	                <%-- <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">会员等级：</div>
	                    <div class="span7">
                    		<c:set var="membershipLevel" value="${promotion.membershipLevel}"/>
                    		<c:set var="vipLevels" value="<%= VipLevelEnum.values() %>" />
                    		<c:forEach var="vipLevel" items="${vipLevels}">
                    			<c:if test="${fn:contains(membershipLevel, vipLevel.id)}">
                    				<span>${vipLevel.name}</span>&nbsp&nbsp&nbsp
                    			</c:if>
                    		</c:forEach>
	                    </div>
	                </div> --%>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">平台类型：</div>
	                    <div class="span7">
	                    	<c:if test="${promotion.platformId=='1'}">舒适100平台</c:if>
	                    </div>
	                </div>
	                <c:if test="${promotion.onlineState=='6'}">
                	<div class="row-fluid" style="margin-top: 10px">
	                	<div class="span1">驳回原因：</div>
		                <div class="span7">${promotion.auditDismissedMsg}</div>
			        </div>
			        </c:if>
		            </br>
	                <legend ><span class="content-body-bg">商品信息</span></legend>
	                
	                <div class="row-fluid" style="margin-top: 10px">
	                	<div class="span1">商品范围：</div>
	                	<div class="span7">
	                    	<c:if test="${promotion.isAllItem=='1'}">所有商品</c:if>
	                    	<c:if test="${promotion.isAllItem=='2'}">部分商品</c:if>
	                    </div>
					</div>
					</br>
					<c:if test="${promotion.isAllItem=='2'}">
						<div class="row-fluid" style="margin-top: 10px">
		                    <div id="itemListDiv"></div>
		                </div>
	                </c:if>
                </div>
				</form>
            </div>
		</div>
	</div>
</body>
</html>