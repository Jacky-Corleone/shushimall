<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>集采活动添加</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<script src="${ctxStatic}/centralPurchase/centralPurchase_valid.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/loadMsg/loadMsg.js"></script>
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
<script type="text/javascript">
var $isAdd = true;
	$(document).ready(function() {
		$(".estimatePrice").hide();
		//图片查看
        $('.showimg').fancyzoom({
            Speed: 400,
            showoverlay: false,
            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
        });
        $("#perPerchaseMaxNum").focusout(function(){
    	 	var num = new Number($(this).val());
    	 	if(num==0){
    	 		$(this).val(1);
    	 	}else{
    	 		$(this).val(num);
    	 	}
    	 });
    	 $("#releaseGoodsMaxNum").focusout(function(){
    	 	var num = new Number($(this).val());
    	 	if(num==0){
    	 		$(this).val(1);
    	 	}else{
    	 		$(this).val(num);
    	 	}
    	 });
		var $estimatePrice = '${queryCentralPurchasingDTO.estimatePrice}';
		if($estimatePrice){
			$("#estimatePrice").val($estimatePrice);
			var $array = $estimatePrice.split("-");
			var $estimatePrice1 = $array[0];
			$("#estimatePrice1").val($estimatePrice1);
			if($array.length==2){
				var $estimatePrice2 = $array[1];
				$(".estimatePrice").show();
				$(":radio:eq(1)").prop("checked",true);
				$("#estimatePrice2").val($estimatePrice2);
			}else{
				$(":radio:eq(0)").prop("checked",true);
			}
		}else{
			$(":radio:eq(0)").prop("checked",true);
		}
		
		$(":radio").change(function(){
			if($(this).val()==1){
				$(".estimatePrice").show();
			}else{
				$(".estimatePrice").hide();
			}
		});
		if('${isView }'){
			$("input").attr("disabled",true);
			$("#platformId").prop("disabled",true);
			
		}
		if("${queryCentralPurchasingDTO.activitesDetailsId}"){
			$("#centralPurchasing").attr("action","${ctx }/purchase/activityUpdate");
			$isAdd = false;
		}else{
			$("#centralPurchasing").attr("action","${ctx }/purchase/activitySave");
		}
		$("#btnPublish").click(function() {
			if(!'${isView }'){
				$("#btnPublish").loadingMsg();
				var isValid = $("#centralPurchasing").valid();
				if(isValid){
					$("#activityStatus").val(1);
					if(checkTime()){
						checkSkuUnique('btnPublish');
					}else{
						$("#btnPublish").hideMsg();
					}
				}else{
					$("#btnPublish").hideMsg();
				}
			}
		});
		$("#btnConfirm").click(function() {
			if(!'${isView }'){
				$("#btnConfirm").loadingMsg();
				var isValid = $("#centralPurchasing").valid();
				if(isValid){
					$("#activityStatus").val(0);
					if(checkTime()){
						checkSkuUnique('btnConfirm');
					}else{
						$("#btnConfirm").hideMsg();
					}
				}else{
					$("#btnConfirm").hideMsg();
				}
			}
		});
		 $("#btnCancel").click(function(){
			 if(!'${isView }'){
             top.$.jBox.confirm("确认要取消吗？","系统提示",function(v,h,f){
                 if(v == "ok"){
                 	history.go(-1);
                 }
             },{buttonsFocus:1});
       		}
		 });
		
		 $("#estimatePrice2").focusout(function(){
		 	var flag1 =  $("#estimatePrice1").val();
	    	var flag2 =  $("#estimatePrice2").val();
	    	if(parseFloat(flag2)<=parseFloat(flag1)){
	    		$.jBox.error("预估价不合法！");
	    	}
		 });
	});
	function addPic(flag) {
		if(!'${isView }'){
		switch (flag) {
		case '1':
			$("#tempFlag").val("1");
			break;
		case '2':
			$("#tempFlag").val("2");
			break;
		}
		$("#uploadPic").val("");
		$("#uploadPicDiv").modal('show');
		}
	}
	function getSku() {
		var $this = $("input:radio:checked[name='tr_skuId']");;
		var skuId = $this.attr("id");
		var $html;
		if(!skuId){
			$html = "<a class='btn' href='javascript:pagesub()'><i></i>选择商品</a>";
		}else{
			$("#skuId").val(skuId);
			$("#itemId").val($this.next().attr("id"));
			$("#detail_cid").val($this.next().next().attr("id"));
			var $tr = $this.parents("tr");
			$html = $tr.children("td:eq(2)").html()
					+ $tr.children("td:eq(5)").html()
					+ "<a class='btn' href='javascript:pagesub()'><i></i>修改商品</a>";
		}
		$(".item-show").html($html);
		$("#skuList").modal('hide');
	}
	function closeUploadDiv() {
		$("#uploadPicDiv").modal('hide');
	}
	function closeskuListDiv() {
		$("#skuList").modal('hide');
	}
	function startUpload() {
		$
				.ajaxFileUpload({
					url : '${ctx}/fileUpload/uploadImg', //用于文件上传的服务器端请求地址
					secureuri : false, //是否需要安全协议，一般设置为false
					fileElementId : 'uploadPic', //文件上传域的ID
					dataType : 'json', //返回值类型 一般设置为json
					type : "post",
					success : function(data, status) { //服务器成功响应处理 函数
						if (data.success) {
							$(".activityImg").prop("src",'${filePath}'+data.url);
							$(".activityImg").show();
							$('.showimg').fancyzoom({
					            Speed: 400,
					            showoverlay: false,
					            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
					        });
							$("#activityImg").val(data.url);
							$("#uploadPicDiv").modal('hide');
						} else {
							$.jBox.error(data.msg);
						}
					},
					error : function(data, status, e) {//服务器响应失败处理函数
						$.jBox.error(e);
					}
				});

	}

	function checkCategory() {
		if ($('#platformId1 option:selected').val()) {
			if (!$('#platformId2 option:selected').val()) {
				$.jBox.info("请选择二级目录");
				return false;
			} else if (!$('#cid option:selected').val()) {
				$.jBox.info("请选择三级目录");
				return false;
			}
		}
		return true;
	}
	function pagesub(n, s) {
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
		$("#gsListPlatformId").val($("#platformId").val());
		var url = $("#searchForm").attr("action");
		var searchFormObj = $('#searchForm').serialize();
		$.ajax({
			url : url,
			type : "post",
			data : searchFormObj,
			cache : false,
			success : function(html) {
				$("#skuList .goodList").html(html);
			}
		});
		}
	}

	function page(n, s) {
		pagesub(n,s);
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
	function checkTime(){
		var activitySignUpTime = $("#activitySignUpTime").val();
		var activitySignUpEndTime = $("#activitySignUpEndTime").val();
		var activityBeginTime = $("#activityBeginTime").val();
		var activityEndTime = $("#activityEndTime").val();
		if(activityEndTime > activityBeginTime && activityBeginTime >= activitySignUpEndTime
				&& activitySignUpEndTime > activitySignUpTime){
			return true;
		}else{
			return false;
		}
	}
	function checkSkuUnique(buttonId){
		var activitySignUpTime = $("#activitySignUpTime").val();
		var activityEndTime = $("#activityEndTime").val();
		var skuId = $("#skuId").val();
		var activitesDetailsId =  "${queryCentralPurchasingDTO.activitesDetailsId}";
		$.ajax({
			url : "${ctx}/purchase/checkSkuUnique",
			type : "post",
			data : {
				skuId:skuId,
				activitySignUpTime:activitySignUpTime,
				activityEndTime:activityEndTime,
				activitesDetailsId:activitesDetailsId
			},
			dataType:"json",
			success : function(data) {
				if(data.success){
					$.ajax({
						url : $('#centralPurchasing').prop("action"),
						type : "post",
						data : $('#centralPurchasing').serialize(),
						dataType : "html",
						success : function(){
							if($isAdd){
								$("#"+buttonId).hideMsg();
								parent.getNewTabAndCloseOld("${ctx }/purchase/activityList","集采活动列表",parent.getMenuIdByName("集采活动列表"),parent.getMenuIdByName("集采活动添加"));
							}else{
								$("#"+buttonId).hideMsg();
								parent.getNewTabAndCloseOld("${ctx }/purchase/activityList","集采活动列表",parent.getMenuIdByName("集采活动列表"),"ge${queryCentralPurchasingDTO.activitesDetailsId}");
							}
						},
						error : function(){
							$("#"+buttonId).hideMsg();
						}
					});
				}else{
					$("#"+buttonId).hideMsg();
					$.jBox.error("该时间段内，该商品已参加集采活动，请重新选择");
				}
			},
			error : function(e) {
				$("#"+buttonId).hideMsg();
				return false;
			}
		});
	}
</script>
</head>
<body onload="setupZoom();">
	<!--图片上传弹出框-->
	<div class="modal hide fade" id="uploadPicDiv">
		<input type="hidden" name="tempFlag" id="tempFlag">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>添加上传图片</h3>
		</div>
		<div class="modal-body">
			<p>
				<input type="file" id="uploadPic" name="file" />
			</p>
		</div>
		<div class="modal-footer">
			<a href="javascript:closeUploadDiv()" class="btn">关闭</a> <a
				href="javascript:startUpload()" class="btn btn-primary">确定</a>
		</div>
	</div>
	<!--图片上传弹出框-->
	<div class="modal hide fade" id="skuList"
		style="width:90%;left:360px;overflow:auto;height:80%">
		<div class="modal-header" style="height:10%">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>选择商品</h3>
		</div>
		<div class="modal-body" style="height:58%">
			<div class="container-fluid">
				<form:form name="searchForm" modelAttribute="goods" id="searchForm"
					method="post" action="${ctx}/purchase/gsListShop">
					<input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
					<input id="gsListPlatformId" name="platformId" type="hidden"/>
					<input id="pageSize" name="rows" type="hidden"
						value="${page.pageSize}" />
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
					<div class="row-fluid" style="margin-top: 10px;">
						<div class="span3">
							<label>商品名称：&nbsp;</label><input type="text"
								value="${goods.itemQueryInDTO.itemName}"
								name="itemQueryInDTO.itemName" class="input-medium" />
						</div>
						<div class="span3">
							<label>商品编码：</label><input type="text"
								onkeyup='numInput(this,0)'
								value="${goods.itemQueryInDTO.itemId}" name="itemQueryInDTO.itemId"
								class="input-medium" title="只能是数字" />
						</div>
						<input type="hidden" value="4" name="itemQueryInDTO.itemStatus" class="input-medium" />
						<div class="span3">
							<label>店铺名称：</label><input type="text" value="${goods.shopName}"
								name="shopName" class="input-medium" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span2" style="margin-top: 15px;">
							<input type="button" class="btn btn-primary"
								onclick="return pagesub();" value="搜索">
						</div>
					</div>
				</form:form>
			</div>
			<div class="container-fluid goodList" style="text-align: center;">
			</div>
		</div>
		<div class="modal-footer" style="height:10%">
			<a href="javascript:closeskuListDiv()" class="btn">关闭</a> <a
				href="javascript:getSku()" class="btn btn-primary">确定</a>
		</div>
	</div>

	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<form:form id="centralPurchasing" modelAttribute="queryCentralPurchasingDTO" name="centralPurchasing" method="post">
				<input type="hidden" id="detail_cid" name="cid" value="${cid }">
				<input type="hidden" id="activityStatus" name="activityStatus" value="${activityStatus }">
				<input type="hidden" name="activitesDetailsId" value="${queryCentralPurchasingDTO.activitesDetailsId }">
				<div class="container-fluid">
					<legend>
						<span class="content-body-bg">集采活动</span>
					</legend>
					<div class="row-fluid">
						<div class="span2"><span style="color: #FF0000">*</span>平台类型:</div>
						<div class="span7">
							<form:select path="platformId" id="platformId" title="请选择平台类型"
								style="width:205px;" value="${queryCentralPurchasingDTO.platformId }" >
								 <form:option value="0" label="舒适100平台"/>
<%--								 <form:option value="2" label="绿印平台"/>--%>
							</form:select>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span2"><span style="color: #FF0000">*</span>活动名称:</div>
						<div class="span7">
							<input name="activityName" id="activityName"
								value="${queryCentralPurchasingDTO.activityName }" type="text"
								class="form-control" title="请输入活动名称" />
						</div>
					</div>
					<c:if test="${not empty queryCentralPurchasingDTO.activitesDetailsId}">
						<div class="row-fluid">
							<div class="span2">&nbsp;活动编码:</div>
							<div class="span7">
								<input
									value="${queryCentralPurchasingDTO.activitesDetailsId }" type="text"
									readOnly="readonly"
									class="form-control" title="请输入活动编码" />
							</div>
						</div>
					</c:if>

					<div class="row-fluid">
						<div class="span2"><span style="color: #FF0000">*</span>报名开始时间</div>
						<div class="span7">
							<input id="activitySignUpTime" name="activitySignUpTime"
								value="<fmt:formatDate value="${queryCentralPurchasingDTO.activitySignUpTime}" type="both" />"
								type="text" class="Wdate"
								readOnly="readOnly"
								<c:if test="${not isView }">
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true,maxDate:'#F{$dp.$D(\'activitySignUpEndTime\')}'});" 
								</c:if>
								 title="请输入报名开始时间"
								/>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span2"><span style="color: #FF0000">*</span>报名结束时间</div>
						<div class="span7">
							<input id="activitySignUpEndTime" name="activitySignUpEndTime"
								value="<fmt:formatDate value="${queryCentralPurchasingDTO.activitySignUpEndTime}" type="both" />"
								type="text" class="Wdate"
								readOnly="readOnly"
								<c:if test="${not isView }">
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true,minDate:'#F{$dp.$D(\'activitySignUpTime\')||\'%y-%M-%d %H:%m:%s\'}',maxDate:'#F{$dp.$D(\'activityBeginTime\')}'});" 
								</c:if> 
								title="请输入报名结束时间"
								/>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span2"><span style="color: #FF0000">*</span>活动开始时间</div>
						<div class="span7">

							<input id="activityBeginTime" name="activityBeginTime"
								value="<fmt:formatDate value="${queryCentralPurchasingDTO.activityBeginTime}" type="both"/>"
								type="text" class="Wdate"
								readOnly="readOnly"
								<c:if test="${not isView }">
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true,minDate:'#F{$dp.$D(\'activitySignUpEndTime\')||\'%y-%M-%d %H:%m:%s\'}',maxDate:'#F{$dp.$D(\'activityEndTime\')}'});" 
								</c:if>
								title="请输入活动开始时间"
								/>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span2"><span style="color: #FF0000">*</span>活动结束时间</div>
						<div class="span7">
							<input id="activityEndTime" name="activityEndTime"
								value="<fmt:formatDate value="${queryCentralPurchasingDTO.activityEndTime}"	type="both"/>"
								type="text" class="Wdate"
								readOnly="readOnly"
								<c:if test="${not isView }">
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true,minDate:'#F{$dp.$D(\'activityBeginTime\')||\'%y-%M-%d %H:%m:%s\'}'});" 
								</c:if>
								title="请输入活动结束时间"
								/>
						</div>
					</div>
				</div>

				<div class="container-fluid">
					<legend>
						<span class="content-body-bg"><span style="color: #FF0000">*</span>活动图片</span>
					</legend>
					<span style="color: red;display: block;" id="imgSize1">（允许的图片格式有JPG、JPEG、PNG、BMP，单张图片不能大于1M,建议图片尺寸：260*230）</span>
					<div class="row-fluid" id="picAddDiv">
						<input type="hidden" name="activityImg" id="activityImg"
							value="${queryCentralPurchasingDTO.activityImg }"
							title="请上传一张图片">
						<img src='${filePath}${queryCentralPurchasingDTO.activityImg }' class='showimg activityImg' style='width:50px;height:50px; 
							<c:choose>
								<c:when test="${not empty queryCentralPurchasingDTO && not empty queryCentralPurchasingDTO.activityImg }">
		                         	display:inline
								</c:when>
								<c:otherwise>
		                         	display:none
								</c:otherwise>
							</c:choose>
						'/>
					<div>
						<c:if test="${!isView }">
							<a class="btn" href="javascript:addPic('1')"><i
								class="icon-plus"></i>添加图片</a>
						</c:if>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12"></div>
					</div>
				</div>
				<div class="container-fluid">
					<legend>
						<span class="content-body-bg"><span style="color: #FF0000">*</span>活动商品</span>
					</legend>
					<div class="row-fluid">
						<input type="hidden" name="skuId" id="skuId"
							value="${queryCentralPurchasingDTO.skuId }" title="请选择商品">
						<input type="hidden" name="itemId" id="itemId"
							value="${queryCentralPurchasingDTO.itemId }" title="请选择商品">
						<div class="item-show">
							<c:if test="${not empty tradeInventoryOutDTO }">
							 <c:forEach items="${tradeInventoryOutDTO.skuPicture }" var="skuPicture" varStatus="skuIndex">
							 	<c:if test="${skuIndex.count == 1 }">
			                        <img class="showimg" style="height: 100px;width: 100px;padding-left: 10px"  src="${filePath}${skuPicture.picUrl}" >
							 	</c:if>
	                        </c:forEach>
	                        <label style="width: 120px;">
	                        <a href="${mallPath}/productController/details?id=${tradeInventoryOutDTO.itemId}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">
	                        ${tradeInventoryOutDTO.itemName}
	                        </a>
	                        </label>
                        	<c:forEach items="${tradeInventoryOutDTO.itemAttr }" var="itemAttrName">
                        		${itemAttrName.name } :
                        		<c:forEach items="${itemAttrName.values }" var="itemAttrValue">
                        			${itemAttrValue.name}
                        		</c:forEach>
                        	</c:forEach>
                        	</c:if>
                        	<c:if test="${!isView }">
							<a class="btn" href="javascript:pagesub()"><i
								class="icon-plus"></i>选择商品</a>
							</c:if>
						</div>
					</div>
					<div class="row-fluid" style="margin-top:10px">
						<div class="span2"><span style="color: #FF0000">*</span>预估价</div>
						<div class="span3 input-append">
							<input id="radio0" type="radio" name="chooseEstimatePriceType" value="0"><label>单值</label>
							<input id="radio1" type="radio" name="chooseEstimatePriceType" value="1"><label>区间</label>
							<br/>
							<input id="estimatePrice1" type="text"
								onkeyup='numInput(this,2)' 
								class="form-control" title="" />
							<label class="estimatePrice">至</label>
							<input id="estimatePrice2"
								type="text"
								onkeyup='numInput(this,2)'
								class="form-control estimatePrice" title="" />
							<label>元</label>
							<input id="estimatePrice" name="estimatePrice"
								type="hidden"
								class="form-control" title="" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span2"><span style="color: #FF0000">*</span>单用户最大采购量</div>
						<div class="span7">
							<input name="perPerchaseMaxNum" id="perPerchaseMaxNum"
								value="${queryCentralPurchasingDTO.perPerchaseMaxNum }"
								onkeyup='numInput(this,0)' 
								type="text" class="form-control" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span2"><span style="color: #FF0000">*</span>集采商品活动数量</div>
						<div class="span7">
							<input type="text" name="releaseGoodsMaxNum"
								id="releaseGoodsMaxNum" onkeyup='numInput(this,0)' 
								value="${queryCentralPurchasingDTO.releaseGoodsMaxNum }"
								title="" /> 
						</div>
					</div>
				</div>
				<c:if test="${!isView }">
				<div class="span3">
					<button class="btn btn-danger btn-block" type="button" loadMsg="正在发布..." 
						id="btnPublish">立即发布</button>
				</div>
				<div class="span3">
					<button class="btn btn-success btn-block" type="button" loadMsg="正在保存..." 
						id="btnConfirm">保存</button>
				</div>
				</c:if>
			</form:form>
		</div>
	</div>
</body>
</html>
