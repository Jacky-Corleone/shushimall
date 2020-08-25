<%@page import="com.camelot.openplatform.common.enums.VipLevelEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>优惠券活动查看</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
td {
	text-align: center;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		
		$("input[name='couponType']").click(function(){
			//1 满减券
	    	if(this.value == '1'){
	    		$("#full").show();
	    		$("#meetPrice").show();
	    		$("#reduction").show();
	    		$("#quota").hide();
	    		$("#couponMax").hide();
	    	//2 折扣券   
	    	}else if(this.value == '2'){
	    		$("#quota").show();
	    		$("#couponMax").show();
	    		$("#full").hide();
	    		$("#meetPrice").hide();
	    		$("#reduction").hide();
	    	//3 现金券
	    	}else if(this.value == '3'){
	    		$("#full").hide();
	    		$("#meetPrice").hide();
	    		$("#reduction").hide();
	    		$("#quota").hide();
	    		$("#couponMax").hide();
	    	}
			
			$("#meetPrice").val("");
			$("#couponAmount").val("");
			$("#couponMax").val("");
			
		});
		$("input[name='couponUsingRange']").click(function(){
			//店铺通用类
			if($(this).val()==2){
				showItem();
				$("#itemCountShow").show();
				$("#itemCount").html(0);
			}else if($(this).val()==3){
				$("#itemCountShow").show();
				$("#itemCount").html(0);
			}else if($(this).val()==4){
				pagesub();
				$("#itemCountShow").show();
				$("#itemCount").html(0);
			}else{
				$("#itemCountShow").hide();
			}
			$("#numId").val("");
		});
		
	});
	//弹出商品选择框
	function selectItemWin(){
		if($("#treeTable").length == 0){
			showItem();
		}else{
			$("#addItemDiv").modal("show");
		}
	}
	//查询商品
	function showItem(){
		page(1,10);
	}
	//翻页
	function page(n,s){
		if(n>0){
			
		}else{
			n =1;
		}
		if(s>0){
			
		}else{
			s =10;
		}
		$("#pageNo").val(n);
		$("#pageSize").val(s); 
		$("#searchForm").submit();
		return false;
    }
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
		var searchFormObj = $('#searchForm1').serialize();
		$.ajax({
			url : url,
			type : "post",
			data : searchFormObj,
			cache : false,
			success : function(html) {
				$.jBox.closeTip();
				$("#skuList .goodList").html(html);
			}
		});
		}
	}
	function closeskuListDiv() {
		$("#skuList").modal('hide');
	}
	function closeshopListDiv() {
		$("#addItemDiv").modal('hide');
	}
	//保存促销信息
	function saveFrPromotion(){
		var meetPrice = $("#meetPrice").val();
		var couponAmount=$("#couponAmount").val();
		var couponMax=$("#couponMax").val();
		var userType=$("input[name='couponType']").val();
		if(userType==1){
			if( meetPrice == null || meetPrice == '' || couponAmount == null || couponAmount == ''){
				alert("请输入满减规则！");
				return;
			}
		}else if(userType==2){
			if( couponMax == null || couponMax == '' || couponAmount == null || couponAmount == ''){
				alert("请输入折扣规则！");
				return;
			}
		}else if(userType==3){
			if( couponMax == null || couponMax == ''){
				alert("请输入现金值！");
				return;
			}
		}
		$("#addCouponInfoForm").validate({
            rules: {
            	"couponId": "required",
            	"couponName": "required",
            	"couponType": "required",
            	"couponStartTime": "required",
            	"couponEndTime": "required",
            	"couponUserMembershipLevel": "required",
            	"couponUserType": "required",
            	"platformId": "required"
            	
            }
        });
		var isValide = $("#addCouponInfoForm").valid();
		
		if(isValide){
			//结束时间必须大于开始时间
			var startTime = $("#addCouponInfoForm").val();
			var endTime = $("#couponEndTime").val();
			if(new Date(startTime) >= new Date(endTime)){
				alert("结束时间必须大于开始时间！");
				return ;
			}
			/* //计算选中商品的个数
			var itemCount = $("#itemCount").html();
			var isAllItem = null;
			$("input[name='promotionInfoDTO.isAllItem']").each(function(){
				if(this.checked){
					isAllItem = this.value;
					return ;
				}
			});
			
			if(itemCount == 0 && isAllItem == 2){ 
				alert("请选择商品！");
				return ;
			} */
			
			$("#addCouponInfoForm").submit();
		}
		
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
				<form name="addCouponInfoForm" id="searchForm"  modelAttribute="couponInfo"  action="${ctx}/couponInfo/viewCouponInfo">
	                <legend ><span class="content-body-bg">基本信息</span></legend>
	                <input name="createUser" id="createUser" maxlength="20"  value="${user.id}"  type="hidden" class="form-control"/>
	                <input name="costAllocation" maxlength="20"  value="1"  type="hidden" class="form-control"/>
	                <input name="id" maxlength="20"  value="${id}" id="Id" type="hidden"  class="form-control"/>
	                <input name="couponUsingRange" maxlength="20"  value="${couponsDTO.couponUsingRange}"  type="hidden" class="form-control"/>
	                <input id="pageNo" name="pageNo" type="hidden" value="${pager.pageNo }" />
					<input id="pageSize" name="pageSize" type="hidden" value="${pager.pageSize }" />
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">申请人：</div>
	                    <div class="span7">
	                    ${user.name }
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">优惠券编号：</div>
	                    <div class="span7">
	                    ${couponsDTO.couponId }
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">优惠券名称：</div>
	                    <div class="span7">
	                    ${couponsDTO.couponName }
	                    </div>
	                </div>
	                 <div class="row-fluid" style="margin-top: 10px">
	                	<div class="span1">成本分摊：</div>
	                	 <div class="span7">
		                                                                        平台承担
                         </div>		                
	                </div>
	                
	                
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">优惠券类型：</div>
	                    <div class="span7">
	                        <c:if test="${couponsDTO.couponType=='1'}">满减券</c:if>
			                <c:if test="${couponsDTO.couponType=='2'}">折扣券</c:if>
			                <c:if test="${couponsDTO.couponType=='3'}">现金券</c:if>
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">优惠券面额：</div>
	                    <div class="span7">
	                    <c:if test="${couponsDTO.couponType=='1'}">
	                                                                            满${couponsDTO.meetPrice}减${couponsDTO.couponAmount}
	                    </c:if>
	                    <c:if test="${couponsDTO.couponType=='2'}">
	                         ${couponsDTO.couponAmount}限额${couponsDTO.couponMax}
	                    </c:if>
	                    <c:if test="${couponsDTO.couponType=='3'}">
	                         ${couponsDTO.couponAmount}
	                    </c:if>
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">开始时间：</div>
	                    <div class="span7">
	                       <fmt:formatDate value='${couponsDTO.couponStartTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
	                    </div>
	                </div>
	                 <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">结束时间：</div>
	                    <div class="span7">
	                      <fmt:formatDate value='${couponsDTO.couponEndTime}' pattern='yyyy-MM-dd HH:mm:ss'/>
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">优惠券说明：</div>
	                    <div class="span7">
	                    ${couponsDTO.couponExplain }
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">申请数量：</div>
	                    <div class="span7">
	                    ${couponsDTO.couponNum }
	                    </div>
	                </div>
	                </br>
	                <legend ><span class="content-body-bg">权限信息</span></legend>
	                
	                <div class="row-fluid" style="margin-top: 10px;display:none">
	                <div class="span1">用户类型：</div>
	                    <div class="span7">
	                        <c:if test="${couponsDTO.couponUserType=='0'}">全部用户</c:if>
	                    	<c:if test="${couponsDTO.couponUserType=='1'}">个人用户</c:if>
	                    	<c:if test="${couponsDTO.couponUserType=='2'}">企业用户</c:if>
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px;display:none">
	                <div class="span1">会员等级：</div>
	                    <div class="span7">
                    		<c:set var="membershipLevel" value="${couponsDTO.couponUserMembershipLevel}"/>
                    		<c:set var="vipLevels" value="<%= VipLevelEnum.values() %>" />
                    		<c:forEach var="vipLevel" items="${vipLevels}">
                    			<c:if test="${fn:contains(membershipLevel, vipLevel.id)}">
                    				<span>${vipLevel.name}</span>&nbsp&nbsp&nbsp
                    			</c:if>
                    		</c:forEach>
	                    </div>
		            </div>
		            <div class="row-fluid" style="margin-top: 10px;display:none">
			           <div class="span1">平台类型：</div>
	                    <div class="span7">
	                    	<c:if test="${couponsDTO.platformId=='1'}">PC端</c:if>
	                    	<c:if test="${couponsDTO.platformId=='3'}">移动端</c:if>
	                    	<c:if test="${couponsDTO.platformId=='4'}">全部平台</c:if>
	                    </div>
			        </div>
			        <div class="row-fluid" style="margin-top: 10px;">
	                    <div class="span1">适用范围：</div>
	                    <div class="span7">
	                        <c:if test="${couponsDTO.couponUsingRange=='1'}">平台通用类</c:if>
	                    	<c:if test="${couponsDTO.couponUsingRange=='2'}">店铺通用类</c:if>
	                    	<c:if test="${couponsDTO.couponUsingRange=='3'}">品类通用类</c:if>
	                    	<c:if test="${couponsDTO.couponUsingRange=='4'}">SKU使用类</c:if>
	                    </div>
	                </div>
		            </br>
		            <div class="container-fluid" style="margin-top: 10px">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					   <thead>
						<c:if test="${couponsDTO.couponUsingRange=='2'}">
	                    <tr>
	                    <th>店铺编号</th>
	                    <th>店铺名称</th>
	                    <th>商家编号</th>
	                    </tr>
	                    </thead>
	                   <tbody>  
		                  <c:forEach items="${pager.list}" var="rangeDTO" varStatus="s">
		                    <tr>
			                    <td>${rangeDTO.itemId}</td>
			                    <td>${rangeDTO.itemName}</td>
			                    <td>${rangeDTO.skuAttr}</td>
		                    </tr>
		                    </c:forEach>
	                    </c:if>
	                    <c:if test="${couponsDTO.couponUsingRange=='3'}">
	                    <tr>
	                    <th>一级类目</th>
	                    <th>二级类目</th>
	                    <th>三级类目</th>
	                    </tr>
	                    </thead>
	                   <tbody>  
		                  <c:forEach items="${pager.list}" var="rangeDTO" varStatus="s">
		                    <tr>
			                    <td>${rangeDTO.itemName}</td>
			                    <td>${rangeDTO.itemId}</td>
			                    <td>${rangeDTO.skuAttr}</td>
		                    </tr>
		                    </c:forEach>
	                    </c:if>
	                    <c:if test="${couponsDTO.couponUsingRange=='4'}">
	                    <tr>
	                    <th>商品名称</th>
	                    <th>sku编码</th>
	                    <th>销售属性</th>
	                    <th>所属类目</th>
	                    <th>销售价</th>
	                    </tr>
	                    </thead>
	                   <tbody>  
		                  <c:forEach items="${pager.list}" var="rangeDTO" varStatus="s">
		                    <tr>
			                    <td>${rangeDTO.itemName}</td>
			                    <td>${rangeDTO.itemId}</td>
			                    <td>
	                        	<c:forEach items="${rangeDTO.skuAttr }" var="itemAttrName">
	                        		${itemAttrName.name } :
	                        		<c:forEach items="${itemAttrName.values }" var="itemAttrValue">
	                        			${itemAttrValue.name}
	                        		</c:forEach>
	                        	</c:forEach>
                                </td>
                                <td>
								<c:forEach items="${rangeDTO.categoryNme }" var="itemCatCascadeDTO">
									${itemCatCascadeDTO.cname} /
									<c:forEach items="${itemCatCascadeDTO.childCats }" var="childCats">
										${childCats.cname} /
										<c:forEach items="${childCats.childCats }" var="childCat">
											${childCat.cname }
										</c:forEach>
									</c:forEach>
								</c:forEach>
						        </td>
                                <td>
								<c:if test="${rangeDTO.price != null}">
									<c:forEach items="${rangeDTO.price }" var="areaPrices">
										${areaPrices.sellPrice}
									</c:forEach>
								</c:if>
						       </td>
		                    </tr>
		                    </c:forEach>
	                    </c:if>
                  </tbody>
                    </table>
                    <c:if test="${couponsDTO.couponUsingRange=='2' || couponsDTO.couponUsingRange=='3' || couponsDTO.couponUsingRange=='4'}">
                     <div class="pagination text-right">${pager}</div>
                    </c:if>
                     
				</form>
            </div>
		</div>
	</div>	
	
</body>
</html>