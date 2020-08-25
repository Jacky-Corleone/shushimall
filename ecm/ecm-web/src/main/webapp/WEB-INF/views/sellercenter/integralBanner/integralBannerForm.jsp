<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>轮播图编辑</title>
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

			$("#name").focus();
			$("#inputForm").validate({
                rules:{
                    sortNumber:{
                        required:true,
                        digits:true
                    }
                }
            });
			init();
			$("input[name='status']").click(function(){
				isTimeDiv();
			});
			
			$("#intime").click(function(){
				$("#startTime").attr("required",false);					
				$("#endTime").attr("required",false);					
			});

			$("#ontime").click(function(){
				$("#startTime").attr("required",true);					
				$("#endTime").attr("required",true);					
			});
			
		});

		function init(){
			if("${mallBanner.id}"!=""){
				$("#textA").text("修改轮播图");
			}else{
				$("#textA").text("新增轮播图");
			}
			isTimeFlag();
			isTimeDiv();
		}
		function isTimeFlag(){
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if(startTime=="" && endTime==""){
				$("input[name=status]:eq(0)").attr("checked",'checked');
			} else {
				$("input[name=status]:eq(1)").attr("checked",'checked');
			}
		}
		function isTimeDiv(){
			var timeFlag = $("input[name='status']:checked").val();
			if(timeFlag=="1"){
				$("#startTimeDiv").show();
				 $("#endTimeDiv").show();
			}else{
				$("#startTimeDiv").hide();
				$("#endTimeDiv").hide();
			}
		}
		//工具方法：上传图片
		var showImgEle = "";
		var urlSaveElel = "";
		function uploadImg(showImgId,urlInput){
			$("#fileInput").click();
			showImgEle = showImgId;
			urlSaveElel = urlInput;
		}
		function fileChange(){
			startUpload("fileInput",showImgEle,urlSaveElel);
		}
		function startUpload(fileElementId, showImg,urlInput){
			//判断图片格式
			var fileInput = $("#"+fileElementId).val();
			var extStart = fileInput.lastIndexOf(".");
			var ext = fileInput.substring(extStart,fileInput.length).toUpperCase();
			if(ext!=".JPG" && ext!=".jpg" && ext!=".JPEG" && ext!=".jpeg" && ext!=".PNG" && ext!=".png" && ext!=".BMP" && ext!=".bmp"){
				alert("图片限于jpg,jpeg,png,bmp格式");
				return false;
			}
			$.ajaxFileUpload({
				url: '${ctx}/fileUpload/uploadsize?size=1048576&date='+new Date(), //用于文件上传的服务器端请求地址
				secureuri: false, //是否需要安全协议，一般设置为false
				fileElementId: fileElementId, //文件上传域的ID
				dataType: 'json', //返回值类型 一般设置为json
				type:"post",
				success: function (data, status){  //服务器成功响应处理函数
					if(data.success){
                        $("#"+showImg).attr("src","${filePath}"+data.url);
                        //图片查看
                        $('#'+showImg).fancyzoom({
                            Speed: 400,
                            showoverlay: false,
                            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
                        });
                        $("#"+urlInput).val("${filePath}"+data.url);
						//return false;
					}else{
                        $.jBox.info(data.msg);
                    }
					$("#"+fileElementId).val("");
				},
				error: function (data, status, e){//服务器响应失败处理函数
                    $.jBox.info("系统繁忙请稍后再试");
                    $("#"+fileElementId).val("");
				}
			});
		};
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
			$("#skuList .goodList #treeTable tr").dblclick(function(){
				setSkuid($(this).find("td:eq(4)").html());
			});
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
	function setSkuid(skuId){
		if(skuId!=null && skuId!=""){
			$("#skuId").val(skuId);
		}else{
			var $this = $("input:radio:checked[name='tr_skuId']");
			$("#skuId").val($this.attr("id"));
		}
		$("#skuList").modal('hide');
	}
	
	//表单提交
    function submitForm(){
    	 if($("#inputForm").valid()){
    		var imgUrl = $("#bannerUrlImg").attr("src");
    		if(imgUrl != null && imgUrl != ""){
    		 $('#inputForm').submit();
    		}else{
    			alert("轮播图不能为空！");
    		}
         }else{
        	 return;
         }
    	
    }
	
	</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="mallBanner" action="${ctx}/sellercenter/integralBanner/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
       <!--  <input type="hidden" name="status" id="status" value="1"/> -->
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">轮播图名称:</label>
			<div class="controls">
				<input type="text" value="${mallBanner.title}" name="title" id="title" maxlength="30" required="true">
				<span>限30个字符以内</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">SkuId:</label>
			<div class="controls">
				<input type="text" value="${mallBanner.skuId}" onkeyup='numInput(this,0)' name="skuId" id="skuId" required="true" maxlength="20" title="不能为空且只能是数字">
				<a class="btn" href="javascript:pagesub()"><iclass="icon-plus"></i>选择商品</a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">积分:</label>
			<div class="controls">
				<input type="text" value="${mallBanner.integral}" onkeyup='numInput(this,0)' name="integral" id="integral" required="true" maxlength="5" title="不能为空且只能是数字">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">显示顺序:</label>
			<div class="controls">
				<input type="text" value="${mallBanner.sortNumber}" name="sortNumber" id="sortNumber" maxlength="2" required="true" title="不能为空且只能是数字">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推荐位图片：</label>
			<div class="controls">
                <label style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：1000*400</label>
				<%--<input type="text" value="仅支持jpg,jpeg,pang,bmp格式" class="z-input01" disabled="disabled">--%>
				<span class="z-upload">
					<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
					<input type="file" id="bannerUrlFile" class="file-img" name="file" onchange="startUpload('bannerUrlFile','bannerUrlImg','bannerUrl')" >
					<input name="bannerUrl" id="bannerUrl" type="hidden" value="${filePath}${mallBanner.bannerUrl }"  />
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片预览:</label>
<!-- 			<span style="color: red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;建议图片尺寸:670*450，图片最大不能超过2M</span> -->
			<div class="controls">
                <c:choose>
                    <c:when test="${mallBanner.bannerUrl!=null}">
                        <img src="${filePath}${mallBanner.bannerUrl }" id="bannerUrlImg"
                             alt="轮播图" width="155" height="45" class="mar_lr10 fl showimg" >
                    </c:when>
                    <c:otherwise>
                        <img  id="bannerUrlImg"
                             alt="轮播图" width="155" height="45" class="mar_lr10 fl showimg" >
                    </c:otherwise>
                </c:choose>
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
                <input type="radio" name="status" value="0" id="intime">即时发布</input>
                <input type="radio" name="status" value="1" id="ontime">定时发布</input>

			</div>
		</div>
		<div class="control-group" id="startTimeDiv">
			<label class="control-label">发布开始时间：</label>
			<div class="controls">
				<input name="startTime" id="startTime" value="<fmt:formatDate value='${mallBanner.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" type="datetime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,minDate:'%y-%M-%d %H:%m:%s',maxDate:'#F{$dp.$D(\'endTime\')}'});"/>
			</div>
		</div>
		<div class="control-group" id="endTimeDiv">
			<label class="control-label">发布结束时间：</label>
			<div class="controls">
				<input name="endTime" id="endTime" value="<fmt:formatDate value='${mallBanner.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" type="datetime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')||\'%y-%M-%d %H:%m:%s\'}'});"/>
			</div>
		</div>
		<div class="control-group">
            <div class="controls">

                <input id="btnSubmit" class="btn btn-primary" type="button"  onclick="submitForm()" value="保 存"/>&nbsp;

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