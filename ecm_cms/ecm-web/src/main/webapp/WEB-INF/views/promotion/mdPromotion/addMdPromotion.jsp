<%@page import="com.camelot.openplatform.common.enums.VipLevelEnum"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
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
	var pageInitLoad = true;
	
	$(function() {
		var disFlag = false;
		if('${promotion.isAllItem}' == '2'){
			showItemInfo();
			disFlag = false;
		}
		$("#addItemDiv").mouseenter(function(){ 
			disFlag = false;
		});
		$("#addItemDiv").mouseleave(function(){ 
			disFlag = true;
		});
		$(document).mouseup(function(){
			if(disFlag){
				showItemInfo();
				disFlag = false;
			}
		});
		$(".itemModalClose").click(function(){
			showItemInfo();
			disFlag = false;
		});
	$("input[name='isAllItem']").click(function() {
		var thiz = this;
		//查询设置的活动时间内，是否有商品直降活动冲突
		var startTimeStr = $("#startTimeStr").val();
		var endTimeStr = $("#endTimeStr").val();
		if (startTimeStr == null
				|| startTimeStr == ''
				|| endTimeStr == null
				|| endTimeStr == '') {
			alert("请先选择活动时间！");
			thiz.checked = false;
			return;
		}

		//选中部分商品
		if (this.value == "2") {
			$(".itemListShow").show();
			$("#addBottonSpan").show();
		} else {
			$("#itemCountShow").hide();
			$.ajax({
				url : "${ctx}/mdPromotion/getMdPromotionConflict",
				type : "post",
				data : {
					shopId : 0,
					startTimeStr : startTimeStr,
					endTimeStr : endTimeStr
				},
				dataType : 'json',
				success : function(data) {
					if (!data.success) {
						alert("存在活动冲突的商品，请选择部分商品参加活动！");
						thiz.checked = false;
						return;
					}
					$("#addBottonSpan").hide();
					$(".itemListShow").hide();
				}
			});
		}
	});
	markdownTypeChange();

		// 活动规则的两个radio（折扣、金额）改变时，需要对字样、input元素进行更改。
		$("[name=markdownType]").bind('change',
				markdownTypeChange);
		var tmp = $("#platformId").val();
		if (tmp == "1")
			tmp = "0";
		$("input[name='itemQueryInDTO.platformId']").val(tmp);

		function markdownTypeChange() {
			/*
			由于编辑直降活动时也使用该页面，而页面加载完后会触发此方法，
			规定页面初始化后不清空，之后触发该方法才会清空
			 */
			if (pageInitLoad === true) {
				pageInitLoad = false;
			} else {
				$("#discountPercent").val('');
			}

			$("#discountPercent").unbind('keyup');
			var textToChange = $("#discountPercent").next();
			var markdownType = $("[name=markdownType]:checked")
					.val();
			if (markdownType == 1) {
				textToChange.html("折");
				$("#discountPercent").prop('maxLength', 5);
				$("#discountPercent").prop('name', 'discountPercent');
				$("#discountPercent").bind('keyup',numInput4Discount);
			} else {
				textToChange.html("元");
				$("#discountPercent").prop('maxLength', 9);
				$("#discountPercent").prop('name', 'promotionPrice');
				$("#discountPercent").bind('keyup', floatInput);
			}

			// 针对floatInput()：重置value2属性值
			$("#discountPercent").prop('value2', '');
		}
		function floatInput() {
			if (this.value == this.value2)
				return;
			else if (this.value.search(/^\d*(?:\.\d{0,2})?$/) == -1)
				this.value = (this.value2) ? this.value2 : '';
			else if (this.value.startsWith('0')
					&& this.value.search(/^0\d+/) == 0)
				this.value = (this.value2) ? this.value2 : '';
			else
				this.value2 = this.value;
		}
	});

	function redioClick() {
		$("input[name='isAllItem']").each(function() {
			if ($(this).val() == 2) {
				if ($(this).attr("checked")) {
					$("#itemCountShow").show();
					showItem();
				} else {
					alert("请选择商品信息！");
				}
			}
		});
	}

	//弹出商品选择框
	function selectItemWin() {
		showItem();
	}
	//查询商品
	function showItem() {
		page(1, 10);
	}
	//翻页
	function page(n, s) {

		var startTimeStr = $("#startTimeStr").val();
		var endTimeStr = $("#endTimeStr").val();
		if (startTimeStr == null || startTimeStr == '' || endTimeStr == null
				|| endTimeStr == '') {
			alert("请先选择活动时间！");
			return;
		}

		$("#page").val(n);
		$("#rows").val(s);
		$.jBox.tip("正在加载列表，请稍等", 'loading', {
			opacity : 0
		});
		$.ajax({
			url : "${ctx}/mdPromotion/getItemList?startTimeStr=" + startTimeStr
					+ "&endTimeStr=" + endTimeStr + "&promotionInfoId="
					+ $("#promotionInfoDTOId").val(),
			type : "post",
			data : $("#selectItemForm").serialize(),
			dataType : 'html',
			success : function(data) {
				$("#itemListDiv").html(data);
				$.jBox.closeTip();
				$("#addItemDiv").modal("show");
				$("input[name='changeFlag']").val("");
			}
		});
	}

	function loadsub(flag) {
		var html;
		var url;
		switch (flag) {
		case '1':
			var pid = $("#platformId1").val();
			if (pid == null || pid <= 0) {
				pid = '${goods.platformId1}';
			}
			html = "<option value=''>二级分类</option>";
			url = "${ctx}/brand/getChildCategory?pCid=" + pid;
			$("#platformId2").html("<option value=''>二级分类</option>");
			$("#platformId2").select2("val", "");
			$("#cid").html("<option value=''>三级分类</option>");
			$("#cid").select2("val", "");
			$("#brand").empty();
			break;
		case '2':
			var pid = $("#platformId2").val();
			if (pid == null || pid <= 0) {
				pid = '${goods.platformId2}';
			}
			html = "<option value=''>三级分类</option>";
			url = "${ctx}/brand/getChildCategory?pCid=" + pid;
			$("#cid").html("<option value=''>三级分类</option>");
			$("#cid").select2("val", "");
			$("#brand").empty();
			break;
		case '3':
			var cid = $("#cid").val();
			var secondCid = $("#platformId2").val();

			url = "${ctx}/brand/getCategoryBrand?secondCid=" + secondCid
					+ "&thirdCid=" + cid;
			break;
		}
		$
				.ajax({
					url : url,
					type : "post",
					dataType : 'json',
					success : function(data) {
						$(data)
								.each(
										function(i, item) {
											if ('1' == flag) {
												/* var a =item.categoryCid;
												$.jBox.info(a); */
												/* var b ='${twoLevelId}';
												$.jBox.info(b); */
												html += "<option ";
												if (item.categoryCid == '${goods.platformId2}') {
													html += " selected";
												}
												html += " value='"
														+ item.categoryCid
														+ "'>"
														+ item.categoryCName
														+ "</option>";
											} else if ('2' == flag) {
												/* $.jBox.info(${itemDTO.cid});
												$.jBox.info(item.categoryCid); */
												html += "<option ";
												if (item.categoryCid == '${goods.cid}') {
													html += " selected ";
												}
												html += "value='"
														+ item.categoryCid
														+ "'>"
														+ item.categoryCName
														+ "</option>";
											}
										});
						switch (flag) {
						//加载第二级
						case '1':
							$("#platformId2").html(html);
							break;
						//加载第三级
						case '2':
							$("#cid").html(html);
							break;
						// 加载品牌
						case '3':
							$("#brand").html(html);
							break;
						}
					}
				});

	}
	//保存促销信息
	function saveMdPromotion(onlineState) {
		var discountPercent = $("#discountPercent").val();
		if (discountPercent == null || discountPercent == '') {
			alert("请输入直降规则！");
			return;
		}
		if (Number(discountPercent) == 0) {
			$("#discountPercent").focus()
			alert("折扣不能为0！");
			return;
		}

		$("#addPromotionForm").validate({
			rules : {
				"activityName" : "required",
				"startTimeStr" : "required",
				"endTimeStr" : "required",
				"platformId" : "required",
				"isAllItem" : "required"
			}
		});
		var isValide = $("#addPromotionForm").valid();

		if (isValide) {
			//结束时间必须大于开始时间
			var startTime = $("#startTimeStr").val();
			var endTime = $("#endTimeStr").val();
			if (new Date(startTime) >= new Date(endTime)) {
				alert("结束时间必须大于开始时间！");
				return;
			}
			/* if(new Date(startTime.replace(/-/g,"/"))<new Date()){
				alert("开始时间必须大于当前时间！");
				return false;
			} */
			//计算选中商品的个数
			var itemCount = $("#itemCount").html();
			var isAllItem = null;
			$("input[name='isAllItem']").each(function() {
				if (this.checked) {
					isAllItem = this.value;
					return;
				}
			});

			if (itemCount == 0 && isAllItem == 2) {
				alert("请选择商品！");
				return;
			}
			$("#discountPercentTemp").val(discountPercent / 10);
			$("#onlineState").val(onlineState);
			$.jBox.confirm(onlineState == '5' ? "确定将此直降活动提交审核？"
					: "确定将此直降活动提交保存？", "系统提示", function(v, h, f) {
				if (v == "ok") {
					$("#addPromotionForm").submit();
				}
			}, {
				buttonsFocus : 1
			});
			// 			$("#addPromotionForm").submit();
			var id = $("#promotionInfoDTOId").val();
			$("#ge" + id).html("直降活动列表");
		}
	}
	//输入字符显示
	function numInput(obj, length) {
		if (obj.value == obj.value2)
			return;
		if (length == 0 && obj.value.search(/^\d*$/) == -1)
			obj.value = (obj.value2) ? obj.value2 : '';
		else if (obj.value.search(/^\d*(?:\.\d{0,2})?$/) == -1)
			obj.value = (obj.value2) ? obj.value2 : '';
		else
			obj.value2 = obj.value;
	}
	//改变选择框平台id
	function changePlatformId(val) {
		if (val == "1")
			val = "0";
		$("input[name='itemQueryInDTO.platformId']").val(val);
	}
	// 折扣验证
	function numInput4Discount() {
		var obj = this;
		if (obj.value2 === undefined) {
			obj.value2 = '';
		}
		var value = obj.value;
		if (!value)
			return;
		if (value.search(/^\d(\.\d{0,2})?$/) == -1 // 找不到x.xx
				|| value.search(/^0\.00$/) == 0) { // 找到0.00
			if (value.search(/^10(\.0{0,2})?$/) == -1) { // 找不到10.00
				obj.value = obj.value2;
			} else {
				obj.value2 = value;
			}
		} else {
			obj.value2 = value;
		}
	}
	//改变选择框平台id
	function changePlatformId(val) {
		top.$.jBox.confirm("改变平台类型后先前选择的部分商品将清空!", "系统提示", function(v, h, f) {
			if (v == "ok") {
				if (val == "1")
					val = "0";
				$("input[name='itemQueryInDTO.platformId']").val(val);
				$("input[name='changeFlag']").val("true");
				//查询商品
				function showItem() {
					page(1, 10);
				}
				var itemCount = $("#itemCount").html("0");
			}
		}, {
			buttonsFocus : 1
		});
	}

	function hidePopupWindow() {
		showItemInfo(1,10);
		$("#addItemDiv").modal('hide');
	}
	function showItemInfo(n,s){
		var selectItemKey = $("#selectItemKey1").val();
		$.ajax({
            url:"${ctx}/frPromotion/getItemInfoForShow",
            type:"post",
            data:{
            	selectItemKey:selectItemKey,
            	page:n,
    			rows:s
            },
            dataType:'html',
            success:function(data){
            	$("#showItemInfo").html(data);
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
				<form name="addPromotionForm" id="addPromotionForm" action="${ctx}/mdPromotion/saveMdPromotion"
					method="post">
					<input name="selectItemKey" id="selectItemKey1"
						value="${selectItemKey}" type="hidden"></input>
					<legend>
						<span class="content-body-bg">基本信息</span>
					</legend>
					<input name="type" id="type" type="hidden" value="1"></input> 
					<input name="id" id="promotionInfoDTOId" type="hidden" value="${promotion.id}"></input>
					<input name="onlineState" id="onlineState"  type="hidden" value="${promotion.onlineState}"></input> 
					<input name="viewType" id="viewType" type="hidden" value="${viewType}"></input>
					<input name="userType" id="userType" type="hidden" value="0"></input>
					<div class="row-fluid" style="margin-top: 10px">
						<div class="span1">活动名称：</div>
						<div class="span7">
							<input name="activityName" id="activityName"
								maxlength="20" type="text" value="${promotion.activityName }"
								class="form-control" title="请输入直降活动名称，最多20个字符" /><span
								style="color: #FF0000">*</span>
						</div>
					</div>
					<div class="row-fluid" style="margin-top: 10px">
						<div class="span1">活动规则：</div>
						<div class="span7">
							<p>
								<input type="radio" name="markdownType" value="1"
									${mdPromotion.markdownType!=2?'checked':''}>折扣 <input
									type="radio" name="markdownType" value="2"
									${mdPromotion.markdownType==2?'checked':''}>金额
							</p>
							<p style="margin-bottom: 0px;">
								所选商品统一享有 <input name="discountPercent" id="discountPercent"
									type="text" maxlength="5"
									value="${mdPromotion.markdownType==2?mdPromotion.promotionPrice : mdPromotion.discountPercent}"
									class="input-medium" /><span>折</span>优惠。<span
									style="color: #FF0000">*</span> <input
									name="discountPercentTemp" id="discountPercentTemp"
									type="hidden" />
							</p>
						</div>
					</div>
					<div class="row-fluid" style="margin-top: 10px">
						<div class="span1">开始时间：</div>
						<div class="span7">
							<input name="startTimeStr" id="startTimeStr"
								value="<fmt:formatDate value='${promotion.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
								type="datetime" class="Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,minDate:'%y-%M-%d %H:%m:%s',maxDate:'#F{$dp.$D(\'endTimeStr\')}'});" />
						</div>
					</div>
					<div class="row-fluid" style="margin-top: 10px">
						<div class="span1">结束时间：</div>
						<div class="span7">
							<input name="endTimeStr" id="endTimeStr"
								value="<fmt:formatDate value='${promotion.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
								type="datetime" class="Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,minDate:'#F{$dp.$D(\'startTimeStr\')||\'%y-%M-%d %H:%m:%s\'}'});" />
						</div>
					</div>
					<div class="row-fluid" style="margin-top: 10px">
						<div class="span1">广告语：</div>
						<div class="span3 input-append">
							<input name="words" id="words" maxlength="20"
								type="text" value="${promotion.words}" class="form-control" />
						</div>
					</div>
					 </br>
					<legend>
						<span class="content-body-bg">权限信息</span>
					</legend>

					<%--<div class="row-fluid" style="margin-top: 10px">
						<label class="control-label" for="userType">用户类型：</label> <select
							name="userType" id="userType"
							class="form-control">
							<option value="" label=""
								<c:if test="${promotion.userType==''}">selected="true"</c:if>>请选择</option>
							<option value="0" label=""
								<c:if test="${promotion.userType=='0'}">selected="true"</c:if>>全部用户</option>
							<option value="1" label=""
								<c:if test="${promotion.userType=='1'}">selected="true"</c:if>>个人用户</option>
							<option value="2" label=""
								<c:if test="${promotion.userType=='2'}">selected="true"</c:if>>企业用户</option>
						</select>

					</div>
					<div class="row-fluid" style="margin-top: 10px">
						<label class="control-label" for="membershipLevel">会员等级：</label>
						<c:set var="membershipLevel" value="${promotion.membershipLevel}" />
						<c:set var="vipLevels" value="<%=VipLevelEnum.values()%>" />
						<c:forEach items="${vipLevels}" var="vipLevel">
							<label><input name="membershipLevel"
								type="checkbox" value="${vipLevel.id}"
								<c:if test="${fn:contains(membershipLevel, vipLevel.id)}">
								   checked="checked"
								</c:if> />${vipLevel.name}
							</label>
						</c:forEach>
					</div> --%>
					<div class="control-group" style="margin-top: 10px">
						<label class="control-label" for="platformId">平台类型：</label> <select
							name="platformId" id="platformId"
							class="form-control" onchange="changePlatformId(this.value)">
							<option value="1" label=""
								<c:if test="${promotion.platformId=='1'}">selected="true"</c:if>>舒适100平台</option>
						</select>
					</div>
					<c:if test="${promotion.onlineState=='6'}">
						<div class="control-group" style="margin-top: 10px">
							<label class="control-label">驳回原因：</label>
							${promotion.auditDismissedMsg}
						</div>
					</c:if>
					</br>
					<legend>
						<span class="content-body-bg">商品信息</span>
					</legend>

					<div class="control-group" style="margin-top: 10px">
						<div class="controls">
							<input type="radio" name="isAllItem" value="1"
								<c:if test="${promotion.isAllItem=='1'}">checked="checked"</c:if>>所有商品</input>
							<input type="radio" name="isAllItem" value="2"
								<c:if test="${promotion.isAllItem=='2' || promotion.isAllItem==null }">checked="checked"</c:if>>部分商品</input>
							<span id="addBottonSpan"><input id="addBotton"
								onclick="javascript:redioClick();" class="btn btn-warning"
								style="margin-left: 20px; margin-bottom: 10px" type="button"
								value="增加商品"></span>
						</div>
					</div>
					<div class="row-fluid" style="margin-top: 10px" id="itemCountShow">
						<c:if
							test="${promotion.isAllItem=='2' || promotion.isAllItem== null}">
							<div class="span7">
								共选择了<strong><span id="itemCount"> <c:if
											test="${selectedItemCount == null}">
									   0
									</c:if>${selectedItemCount}</span></strong>个商品
							</div>
							<div id="showItemInfo" class="itemListShow"></div>
					</div>
					</c:if>
			</div>
			<div class="span2">
				<input type="button" class="btn btn-primary"
					onclick="saveMdPromotion('4')" value="保存" />
				<c:if
					test="${promotion.onlineState=='4' || promotion.onlineState== null || promotion.onlineState=='6'}">
					<input type="button" class="btn btn-primary"
						onclick="saveMdPromotion('5')" value="送审" />
				</c:if>
			</div>
			</form>
		</div>
	</div>
	</div>

	<!--商品列表显示-->
<div class="modal hide fade" id="addItemDiv" style="margin-top:-35px;margin-left:-510px;width:1010px;height:450px;overflow: auto;">
    <div class="modal-header">
        <button type="button" class="close itemModalClose" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h5 id="titleId">选择参加活动的商品</h5>
    </div>
    <div class="container-fluid">
	    <form:form name="selectItemForm" id="selectItemForm" action="" modelAttribute="goods" method="post">
		    <input id="page" name="page" type="hidden" value="" />
		    <input id="rows" name="rows" type="hidden" value="" />
		    <input name="itemQueryInDTO.platformId"  type="hidden"  />
		    <input name="changeFlag"  type="hidden"  />
		    <input name="selectItemKey" id="selectItemKey2" value="${selectItemKey}" type="hidden"></input>
		    <div class="row-fluid">
				<div class="span8">
					<label>平台分类：</label>
					<form:select path="platformId1" name="goods.platformId1"
						cssClass="input-medium" onchange="loadsub('1')">
						<form:option value="" label="一级分类"></form:option>
						<c:forEach items="${platformList}" var="item">
							<option
								<c:if test="${item.categoryCid==goods.platformId1}">selected="true"</c:if>
								value="${item.categoryCid}">${item.categoryCName}</option>
						</c:forEach>
					</form:select>
					<form:select path="platformId2" name="goods.platformId2"
						cssClass="input-medium" onchange="loadsub('2')">
						<form:option value="" label="二级分类"></form:option>
						<c:forEach items="${twoList}" var="item">
							<option
								<c:if test="${item.categoryCid==goods.platformId2}">selected="true"</c:if>
								value="${item.categoryCid}">${item.categoryCName}</option>
						</c:forEach>
					</form:select>
					<form:select path="cid" name="goods.cid" cssClass="input-medium">
						<form:option value="" label="三级分类"></form:option>
						<c:forEach items="${thirdList}" var="item">
							<option value="${item.categoryCid}">${item.categoryCName}</option>
						</c:forEach>
					</form:select>
				</div>
			</div>	
	    	<div class="row-fluid" style="margin-top: 10px">
	            <div class="span4"><label>商品名称：&nbsp;</label>
	                <input name="itemQueryInDTO.itemName" id="itemName" type="text" class="form-control" title="请输入商品名称"/>
	            </div>
	            <div class="span4"><label>商品编号：&nbsp;</label>
	                <input name="itemQueryInDTO.id" id="id" type="text"  onkeyup="numInput(this,0)"  class="form-control" title="请输入商品编号"/>
	            </div>
	            <div class="span4">
	            	<label>店铺名称：&nbsp;</label>
	                <input name="shopName" id="shopName" type="text" class="form-control" title="请输入商品名称"/>
	            </div>
	            <div class="span2">
	             	<input type="button" class="btn btn-primary" onclick="showItem()" value="搜索">
	            </div>
	        </div>
	        
	    </form:form>
    </div>
    <div id="itemListDiv"></div>
    <div class="modal-footer">
			<a
				href="javascript:hidePopupWindow()" class="btn btn-primary">确定</a>
	</div>
</div>

</body>
</html>
