<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>积分商城广告编辑</title>
<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
/*图片上传按钮样式*/
.z-upload{overflow:hidden; width:100px;height:30px;line-height:30px;color:#fff;position:relative;display:block;}
.z-upload .file-img{font-size:40px;width:100px;height:30px;filter:alpha(opacity=0);opacity:0;position:absolute;left:0px;top:0px;}
.c-upload{position:absolute;left:0;bottom:0px;overflow:hidden;height:20px;line-height:20px;color:#fff;width:100%;background:rgba(0, 0, 0, .5);z-index:999;}
.c-upload .file-img{width:80px;height:50px;filter:alpha(opacity=0);opacity:0;z-index:999;text-align:center;}
.c-upload span{width:80px;height:50px; padding-left:15px;}
</style>
<script type="text/javascript">
	$(document).ready(function() {
        //图片查看
        $('.showimg').fancyzoom({
            Speed: 400,
            showoverlay: false,
            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
        });
        var type=$("#adType");
        changeImgsize(type[0]);
		$("#inputForm").validate({
            rules:{
                sortNumber:{
                    digits:true
                }
            }
        });
		/* var selects = document.getElementsByName("status");
		 selects[0].checked= true;*/
		$("[name='status']").click(function(){
			var status = $(this).val();
			if(status == 0){
				$("#publishFlag").show();
			}else{
				$("#publishFlag").hide();
			}
		});
        <c:if test="${dto.adType==3}">
            $("#isClose").show();
            $("#cidDiv").hide();
        </c:if>
        <c:if test="${dto.adType==4}">
        $("#isClose").hide();
        $("#cidDiv").show();
        </c:if>
        isTimeFlag();
        
        var startTime = "<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>";
        $('#startTime').val(startTime);
        var endTime = "<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>";
        $('#endTime').val(endTime);
        
	});
	function closeskuListDiv() {
		$("#skuList").modal('hide');
	}
    function isTimeFlag(){
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        if(!endTime){
            $("input[name=status]:eq(0)").attr("checked",'checked');
        } else {
            $("input[name=status]:eq(1)").attr("checked",'checked');
            $("#publishFlag").show();
        }
    }
    //表单提交
    function submitForm(){
    	$('#startTime_').val($('#startTime').val());
    	$('#endTime_').val($('#endTime').val());
    	$('#inputForm').submit();
    }
  	//工具方法：上传图片
	function startUpload(fileElementId, showImg, urlInput){
		var fileInput = $("#"+fileElementId).val();
		var extStart = fileInput.lastIndexOf(".");
		var ext = fileInput.substring(extStart,fileInput.length).toUpperCase();
		if(ext!=".JPG" && ext!=".JPEG" && ext!=".PNG" && ext!=".BMP"){
			alert("图片限于JPG,JPEG,PNG,BMP格式");
			return false;
		}
		$.ajaxFileUpload({
			url: '${ctx}/fileUpload/uploadsize?size=1048576&date='+new Date(),
			secureuri: false, //是否需要安全协议，一般设置为false
			fileElementId: fileElementId, //文件上传域的ID
			dataType: 'json',
			type:"post",
			success: function (data, status){
				if(!data.success){
                    $.jBox.info(data.msg);
					return ;
				}
				$("#"+showImg).attr("src","${filePath}"+data.url);
                //图片查看
                $('#'+showImg).fancyzoom({
                    Speed: 400,
                    showoverlay: false,
                    imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
                });
				$("#"+urlInput).val(data.url);
			},
			error: function (data, status, e){
                $.jBox.info("系统繁忙请稍后再试")
			}
		});
	};
	function changeImgsize(self){
		if(self.value==1){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：330*150");
            $("#isClose").hide();
            $("#cidDiv").hide();
		}
        if(self.value==2){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：500*380");
            $("#isClose").hide();
            $("#cidDiv").hide();
		}
        if(self.value==3){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：1200*80");
            $("#isClose").show();
            $("#cidDiv").hide();
        }
        if(self.value==4){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：100*40");
            $("#isClose").hide();
            $("#cidDiv").show();
        }
        if(self.value==5){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：130*130");
            $("#isClose").hide();
            $("#cidDiv").hide();
        }
        if(self.value==6){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：270*80");
            $("#isClose").hide();
            $("#cidDiv").hide();
        }
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
						$("#scid").html(html);
						break;
					// 加载品牌
					case '3':
						$("#brand").html(html);
						break;
					}
				}
			});

}
	function setSkuid(){
		var $this = $("input:radio:checked[name='tr_skuId']");
		$("#skuId").val($this.attr("id"));
		$("#skuList").modal('hide');
	}
</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="dto" action="${ctx}/sellercenter/integralAdvertise/edit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="pageNo" value="${page.pageNo}">
		<input type="hidden" name="pageSize" value="${page.pageSize}">
		<input type="hidden" name="statusAdType" value="${statusDTO.adType}">
		<input type="hidden" name="statusAdTitle" value="${statusDTO.adTitle}">
		<input type="hidden" name="statusTimeFlag" value="${statusDTO.timeFlag}">
		<input type="hidden" name="statusStartTime" value="<fmt:formatDate value='${statusDTO.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
		<input type="hidden" name="statusEndTime" value="<fmt:formatDate value='${statusDTO.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
		<input type="hidden" name="statusStatus" value="${statusDTO.status}">
		<div class="control-group">
			<label class="control-label" for="adType">广告类型:</label>
			<div class="controls">
				<form:select id="adType" path="adType" class="input-small" onchange="changeImgsize(this)">
					<form:option value="7" label="首页广告"/>
					<form:option value="8" label="商品推荐位"/>
                    <form:option value="9" label="兑你喜欢"/>
				</form:select>
			</div>
		</div>
        <div class="control-group hide" id="isClose">
            <label class="control-label" for="close">是否可关闭:</label>
            <div class="controls">
                <form:radiobutton path="close" value="1" label="可关闭"/>
                <form:radiobutton path="close" value="2" label="不可关闭"/>
            </div>
        </div>
        <div class="control-group hide" id="cidDiv">
            <label class="control-label" for="cid">类目</label>
            <div class="controls">
                <select id="cid" name="cid" class="input-small">
                    <c:forEach items="${itemList}" var="t">
                        <c:choose>
                            <c:when test="${t.categoryCid==dto.cid}">
                                <option value="${t.categoryCid}" selected >${t.categoryCName}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${t.categoryCid}">${t.categoryCName}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </div>
		<div class="control-group">
			<label class="control-label" for="sortNumber">广告序号:</label>
			<div class="controls">
				<form:input path="sortNumber" id="sortNumber" htmlEscape="false" maxlength="50" class="required" title="序号只能是整数"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="title">广告标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="30" class="required"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label" for="adURL">SkuId:</label>
			<div class="controls">
				<form:input id="skuId" path="skuId" htmlEscape="false" maxlength="20" onkeyup='numInput(this,0)' class="required" title="不能为空且只能是数字"/><a class="btn" href="javascript:pagesub()"><iclass="icon-plus"></i>选择商品</a>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="adURL">积分:</label>
			<div class="controls">
				<form:input path="integral" htmlEscape="false" maxlength="5" onkeyup='numInput(this,0)' class="required" title="不能为空且只能是数字"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">推荐位图片：</label>
			<div class="controls">
				<%--<input type="text" class="z-input01" disabled="disabled">--%>
                <span id="showimgmsg" style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M</span>
				<span class="z-upload">
					<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
					<input type="file" id="adSrcFile" name="file" class="file-img" onchange="startUpload('adSrcFile','adSrcImg','adSrc')">
					<input id="adSrc" name="adSrc" type="hidden" value="${dto.adSrc}" required="true"/>
				</span>

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">图片预览:</label>
			<div class="controls">
	             <c:choose>
	                 <c:when test="${dto.adSrc!=null}">
	                     <img src="${filePath}${dto.adSrc}" id="adSrcImg" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
	                 </c:when>
	                 <c:otherwise>
	                     <img id="adSrcImg" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
	                 </c:otherwise>
	             </c:choose>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="publishFlag"></label>
			<div class="controls">
				<form:radiobutton path="status" name="status" value="1" label="即时发布"/>
				<form:radiobutton path="status" name="status" value="0" label="定时发布"/>
			</div>
		</div>

		<div id="publishFlag" class="control-group" style="display: none;">
			<label class="control-label" for=publishFlag>定时发布时间:</label>
			<div class="controls">
				<form:input path="startTime" id="startTime" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})"/>
				<input id="startTime_" name="startTime_" type="hidden" />
			</div>
            <label class="control-label" for=publishFlag>定时结束时间:</label>
            <div class="controls">
                <form:input path="endTime" id="endTime"  class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"/>
                <input id="endTime_" name="endTime_" type="hidden" />
            </div>
		</div>
	<div class="control-group">
            <div class="controls">
               <input id="btnSubmit" class="btn" type="button" onclick="submitForm()" value="保 存"/>&nbsp;
                <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
		</div>
		

	</form:form>
	 <!--图片上传弹出框-->
	<div class="modal hide fade" id="skuList"
		style="width:90%;left:360px;overflow:auto;">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>选择商品</h3>
		</div>
		<div class="modal-body" style="height:280px">
			<div class="container-fluid">
				<form:form name="searchForm" modelAttribute="goods" id="searchForm"
					method="post" action="${ctx}/purchase/gsListShop">
					<input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
					<input id="gsListPlatformId" name="platformId" value="0" type="hidden"/>
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
							<form:select id="scid" path="cid" name="goods.cid" cssClass="input-medium">
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
		<div class="modal-footer">
			<a href="javascript:closeskuListDiv()" class="btn">关闭</a> <a
				href="javascript:setSkuid()" class="btn btn-primary">确定</a>
		</div>
	</div>
</body>
</html>