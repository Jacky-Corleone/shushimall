<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>集采活动列表</title>
<meta name="decorator" content="default" />
<style>
h3 {
	color: #000000;
	height: 46px;
	line-height: 46px;
	text-indent: 20px;
	font-size: 15px;
	font-family: \5FAE\8F6F\96C5\9ED1;
	font-weight: 500;
}

table.td-cen th,.td-cen td {
	text-align: center;
	vertical-align: middle;
}

.hhtd td {
	word-wrap: break-word;
	word-break: break-all;
}
</style>
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
		$(document).ready(function() {

            //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });

			$("#treeTable").treeTable({expandLevel : 5});
            $("#searchForm").validate({
                rules: {
                    "itemQueryInDTO.id": {
                        digits: true
                    }
                }
            });
		});
		function editActivity(id){
			var url="${ctx}/purchase/activityAdd?activitesDetailsId="+id;
            var title = "编辑集采活动";
            parent.openTab(url,title,"ge"+id);
		}
		function viewActivity(id){
			var url="${ctx}/purchase/activityAdd?activitesDetailsId="+id+"&isView=true";
            var title = "查看集采活动";
            parent.openTab(url,title,"ge"+id);
		}
		function deleteActivity(id){
			location.href = "${ctx}/purchase/activityUpdate?activitesDetailsId="+id+"&activityStatus=4";
		}
		function pauseActivity(id){
			location.href = "${ctx}/purchase/activityUpdate?activitesDetailsId="+id+"&activityStatus=3";
		}
		function publishActivity(id){
			location.href = "${ctx}/purchase/activityUpdate?activitesDetailsId="+id+"&activityStatus=1";
		}
		function viewSignUpInfo(id){
			$.ajax({
				url : "${ctx}/purchase/viewSignUpInfo?activitesDetailsId="+id,
				cache : false,
				dataType:"text",
				success : function(html) {
					$("#signUpInfoBody").html(html);
				}
			});
			$("#signUpInfo").modal('show');
		}
		function closesignUpInfo(){
			$("#signUpInfo").modal('hide');
		}
		function managePurchasePrice(id,num,name,item,price){
			$("#managePurchasePrice").modal('show');
			var name = $("#activityName_"+id).val();
			var skuId = $("#skuId_"+id).val();
			var price = $("#centralPurchasingPrice_"+id).val();
			
			var $html = "<td>" + id + "</td><td>" + name + "</td><td>" + skuId
					+ "<td><input id='priceDetailId' type='hidden' value='"+id+"'/><input id='managePrice' type='text' value='"+price+"'/></td>";
			$("#priceValue").html($html);
		}
		function closeMng(){
			$("#managePurchasePrice").modal('hide');
		}
		function submitMng(){
			var activitesDetailsId = $("#priceDetailId").val();
			var centralPurchasingPrice = $("#managePrice").val();
			if(centralPurchasingPrice==0){
				$("#managePurchasePrice").hide();
				return;
			}
			$.ajax({
				url : '${ctx}/purchase/activityUpdate',
				type : "post",
				data : {
					activitesDetailsId:activitesDetailsId,
					centralPurchasingPrice:centralPurchasingPrice
				},
				success : function() {
					$("#managePurchasePrice").modal('hide');
					var price = new Number(centralPurchasingPrice);
					$("#centralPurchasingPrice_"+activitesDetailsId).val(price.toFixed(2));
					$(".centralPurchasingPrice_"+activitesDetailsId).text(price.toFixed(2));
					alert("修改成功");
				}
			});
		}
		function pagesub(n,s){
			//if(checkCategory()){
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
			//}
	    }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s); 
			$("#searchForm").submit();
	    	return false;
	    }
		//输入数字显示
		function numInput(obj,length){
			if(obj.value==obj.value2)
				return;
			if(length == 0 && obj.value.search(/^\d*$/)==-1)
				obj.value=(obj.value2)?obj.value2:'';
			else if(obj.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)
				obj.value=(obj.value2)?obj.value2:'';
			else obj.value2=obj.value;
		}
	</script>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<div class="container-fluid">
				<tags:message content="${message}" />
				<form name="searchForm" id="searchForm" method="post"
					action="${ctx}/purchase/activityList">
					<input id="pageNo" name="pageNo" type="hidden"
						value="${page.pageNo}" /> <input id="pageSize" name="pageSize"
						type="hidden" value="${page.pageSize}" />
					<div class="row-fluid">
						<div class="span3">
							<label>活动名称：</label> <input name="activityName" type="text"
								class="input-medium"
								value="${queryCentralPurchasingDTO.activityName }" />
						</div>
						<div class="span3">
							<label>活动编码：</label> <input name="activitesDetailsId" type="text"
								class="input-medium"
								onkeyup="numInput(this,0)"
								value="${queryCentralPurchasingDTO.activitesDetailsId }" />
						</div>
						<div class="span4">
							<label>所属平台：</label>
							<select name="platformId" id="platformId" title="请选择平台类型"
								style="width:205px;">
								 <option value="1">全部</option>
								 <option value="0" <c:if test="${queryCentralPurchasingDTO.platformId eq 0}">selected</c:if> >舒适100平台</option>
							</select>
						</div>
						</div>
						<div class="row-fluid">
						<div class="span3">
							<label>商品名称：</label><input type="text"
								value="${queryCentralPurchasingDTO.itemName}" name="itemName"
								class="input-medium" />
						</div>
						<div class="span2">
							<input type="button" class="btn btn-primary" onclick="pagesub();"
								value="搜索">
						</div>
					</div>

				</form>
			</div>
			<div class="container-fluid" style="text-align: center;">
				<form name="soldOutAll" id="soldOutAll" method="post"
					action="${ctx}/goodList/soldGoodsOutAll">
					<table id="treeTable" style="text-align: center;"
						class="table table-striped table-bordered table-condensed td-cen hhtd">
						<tr>
							<th>活动编码</th>
							<th>活动名称</th>
							<th>商品编码</th>
							<th style="width:100px">商品名称</th>
							<th>商品属性</th>
							<th>预估价</th>
							<th>活动状态</th>
							<th>报名人数</th>
							<th>集采价</th>
							<th>平台类型</th>
							<th>操作</th>
						</tr>
						<tbody>
							<c:forEach items="${pager.list}" var="queryCentralPurchasingDTO"
								varStatus="s">
								<tr id="">
									<td>${queryCentralPurchasingDTO.activitesDetailsId }</td>
									<td><a
										href="${mallPath}/productController/details?id=${queryCentralPurchasingDTO.itemId}&skuId=${queryCentralPurchasingDTO.skuId}&centralPurchasingId=${queryCentralPurchasingDTO.activitesDetailsId}" target="_Blank">${queryCentralPurchasingDTO.activityName}</a>
										<input type="hidden"
										id="activityName_${queryCentralPurchasingDTO.activitesDetailsId }"
										value="${queryCentralPurchasingDTO.activityName}" /></td>
									<td><input type="hidden"
										id="skuId_${queryCentralPurchasingDTO.activitesDetailsId }"
										value="${queryCentralPurchasingDTO.skuId}" />
										${queryCentralPurchasingDTO.skuId}</td>
									<td  style="width:100px">
										${queryCentralPurchasingDTO.itemName }
									</td>
									<td>
										<c:forEach items="${queryCentralPurchasingDTO.itemAttr}" var="itemAttrName">
										    ${itemAttrName.name }:
											<c:forEach items="${itemAttrName.values}" var="itemAttrValue">
													${itemAttrValue.name }
											</c:forEach>
										</c:forEach>
									</td>
									<td>${queryCentralPurchasingDTO.estimatePrice}</td>
									<td><c:if
											test="${queryCentralPurchasingDTO.detailedStatus=='0'}">
											<p style="color:#a0ba59;">未发布</p>
										</c:if> <c:if test="${queryCentralPurchasingDTO.detailedStatus=='1'}">
											<p style="color:#a0ba59;">活动进行中</p>
										</c:if> <c:if test="${queryCentralPurchasingDTO.detailedStatus=='2'}">
											<p style="color:#a0ba59;">报名进行时</p>
										</c:if> <c:if test="${queryCentralPurchasingDTO.detailedStatus=='3'}">
											<p style="color:#a0ba59;">未开始报名</p>
										</c:if> <c:if test="${queryCentralPurchasingDTO.detailedStatus=='4'}">
											<p style="color:#a0ba59;">报名结束</p>
										</c:if> <c:if test="${queryCentralPurchasingDTO.detailedStatus=='5'}">
											<p style="color:#a0ba59;">已售罄</p>
										</c:if> <c:if test="${queryCentralPurchasingDTO.detailedStatus=='6'}">
											<p style="color:#a0ba59;">活动终止</p>
										</c:if> <c:if test="${queryCentralPurchasingDTO.detailedStatus=='7'}">
											<p style="color:#a0ba59;">活动结束</p>
										</c:if>
									</td>
									<td>${queryCentralPurchasingDTO.signUpNum }</td>

									<td
										class="centralPurchasingPrice_${queryCentralPurchasingDTO.activitesDetailsId}"
										>
										${queryCentralPurchasingDTO.centralPurchasingPrice }</td>
									<td>
									<c:choose>
		                        		<c:when test="${queryCentralPurchasingDTO.platformId == 2}">
		                        			绿印平台
		                        		</c:when>
		                        		<c:otherwise>
		                        			舒适100平台
		                        		</c:otherwise>
		                        	</c:choose>
		                        	<input type="hidden"
										id="centralPurchasingPrice_${queryCentralPurchasingDTO.activitesDetailsId}"
										value="${queryCentralPurchasingDTO.centralPurchasingPrice }" />
									</td>
									<td>
									<a href="javascript:viewSignUpInfo(${queryCentralPurchasingDTO.activitesDetailsId })">查看报名信息</a><br/>
									<a href="javascript:void(0)"
										onclick="viewActivity(${queryCentralPurchasingDTO.activitesDetailsId})">查看</a>
										<c:if
											test="${queryCentralPurchasingDTO.detailedStatus == '0'}">
											<a href="javascript:void(0)"
												onclick="editActivity(${queryCentralPurchasingDTO.activitesDetailsId})">编辑</a>
											<a href="javascript:void(0)"
												onclick="publishActivity(${queryCentralPurchasingDTO.activitesDetailsId})">发布</a>
											<a href="javascript:void(0)"
												onclick="deleteActivity(${queryCentralPurchasingDTO.activitesDetailsId})">删除</a>
										</c:if> <c:if
											test="${queryCentralPurchasingDTO.detailedStatus == '1' || queryCentralPurchasingDTO.detailedStatus == '5'}">
											<a href="javascript:void(0)"
												onclick="pauseActivity(${queryCentralPurchasingDTO.activitesDetailsId})">终止</a>
										</c:if> <c:if
											test="${queryCentralPurchasingDTO.detailedStatus == '2' || queryCentralPurchasingDTO.detailedStatus == '4'}">
											<a href="javascript:void(0)"
												onclick="pauseActivity(${queryCentralPurchasingDTO.activitesDetailsId})">终止</a>
											<a href="javascript:void(0)"
												onclick="managePurchasePrice(${queryCentralPurchasingDTO.activitesDetailsId})">维护集采价</a>
										</c:if> <c:if
											test="${queryCentralPurchasingDTO.detailedStatus == '3'}">
											<a href="javascript:void(0)"
												onclick="deleteActivity(${queryCentralPurchasingDTO.activitesDetailsId})">删除</a>
											<a href="javascript:void(0)"
												onclick="pauseActivity(${queryCentralPurchasingDTO.activitesDetailsId})">终止</a>
											<a href="javascript:void(0)"
												onclick="managePurchasePrice(${queryCentralPurchasingDTO.activitesDetailsId})">维护集采价</a>
										</c:if> <c:if
											test="${queryCentralPurchasingDTO.detailedStatus == '6' || queryCentralPurchasingDTO.detailedStatus == '7'}">
											<a href="javascript:void(0)"
												onclick="deleteActivity(${queryCentralPurchasingDTO.activitesDetailsId})">删除</a>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</form>
				<div class="pagination">${pager}</div>
			</div>

		</div>
	</div>
	<div class="modal hide fade" id="signUpInfo"
		style="width:90%;left:360px;overflow:auto;">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>用户报名信息</h3>
		</div>
		<div class="modal-body" id="signUpInfoBody" style="height:280px">
		</div>
		<div class="modal-footer" style="text-align: center;">
			<a href="javascript:closesignUpInfo()" class="btn">关闭</a>
		</div>
	</div>
	<div class="modal hide fade" id="managePurchasePrice">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>维护集采价</h3>
		</div>
		<div class="modal-body">
			<div class="container-fluid" style="text-align: center;">
				<table style="text-align: center;"
					class="table table-striped table-bordered table-condensed td-cen hhtd tree_table">
					<tr>
						<th>活动编码</th>
						<th>活动名称</th>
						<th>商品编码</th>
						<th>集采价</th>
					</tr>
					<tbody>
						<tr id="priceValue">

						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="modal-footer">
			<a href="javascript:closeMng()" class="btn">关闭</a> <a
				href="javascript:submitMng()" class="btn">确定</a>
		</div>
	</div>
</body>
</html>