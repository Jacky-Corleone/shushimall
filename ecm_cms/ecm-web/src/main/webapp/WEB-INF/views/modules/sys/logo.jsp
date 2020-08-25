<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>logo设置</title>
	<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>

    <link href="${ctxStatic}/kindeditor/themes/default/default.css" rel="stylesheet" />
    <script src="${ctxStatic}/kindeditor/kindeditor-min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/kindeditor/lang/zh_CN.js" type="text/javascript"></script>

	<script type="text/javascript">
        var picUrlImg;
		$(document).ready(function() {
            //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });

			picUrlImg = $("#picUrl").val();
			$("#btnSubmit").click(function(){
				var platformId = $('input:radio[name="platformId"]:checked').val();
	    		var logoName = $("#logoName").val();
	    		var logoName_bl = false;
				if(logoName && $.trim(logoName).length > 0){
					logoName_bl = true;
	        	}else{
	        		$.jBox.info('请填写logo名称！');
	        		return;
	            }
				var picUrl = $("#picUrl").val();
	    		var picUrl_bl = false;
				if(picUrl && $.trim(picUrl).length > 0){
                    picUrl.replace("${filePath}","");
					picUrl_bl = true;
	        	}else{
	        		$.jBox.info('请上传logo图片！');
	        		return;
	            }
	    		if(logoName_bl && picUrl_bl){
	    			$.ajax({
	    				type: "post",
	        			url: "${ctx}/sys/logo/update",
	        			dataType:"json",
	        			data:{
	        				logoName: logoName,
	        				picUrl: picUrl,
	        				platformId: platformId
	            		},
	        			success: function(data){
	        				if(data.success){
	        					var result = data.result;
	        					if(result>0){
	        						$.jBox.info("logo保存成功");
	            				}
	        				}
	        			}
	        		});
	        	}
	        });

			$("#btnReset").click(function(){
                var url='${filePath}'+picUrlImg;
				$("#picUrlImg").attr("src",url);
				$('#picUrl').val(picUrlImg);
                //图片查看
                $('.showimg').fancyzoom({
                    Speed: 400,
                    showoverlay: false,
                    imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
                });
				$('#inputForm')[0].reset();
			});
			
			// 科印信息加载
			$("#platformId1").click(function(){
				location.href = "${ctx}/sys/logo/info";
			});
			
			// 绿印信息加载
			$("#platformId2").click(function(){
				var platformId = $('input:radio[name="platformId"]:checked').val();
				location.href = "${ctx}/sys/logo/info?platformId=" + platformId;
			});
		});
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
            if(ext!=".JPG" && ext!=".jpg" && ext!=".BMP"&&ext!=".bmp"&& ext!=".PNG"&&ext!=".PNG"&& ext!=".JPEG" && ext!=".jpeg"&&ext!=".gif"&&ext!=".GIF"){
                $.jBox.info("图片限于JPG,BMP,PNG,JPEG格式");
                return false;
            }
			$.ajaxFileUpload({
                url: '${ctx}/fileUpload/uploadsize?size=1048576&date='+new Date(), //用于文件上传的服务器端请求地址
				secureuri: false, //是否需要安全协议，一般设置为false
				fileElementId: fileElementId, //文件上传域的ID
				dataType: 'content', //返回值类型 一般设置为json
				type:"post",
				success: function (data, status){
				    if(data.success){
                        //服务器成功响应处理函数
                        $("#"+showImg).attr("src","${filePath}"+data.url);
                        $("#"+urlInput).val("${filePath}"+data.url);
                        $("#"+fileElementId).val("");
                    }else{
                        $.jBox.info(data.msg);
                    }
				},
				error: function (data, status, e){//服务器响应失败处理函数
                    $.jBox.info("系统繁忙，请稍后再试");
				}
			});
		};
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">平台:</label>
			<div class="controls">
				<c:choose>
					<c:when test="${platformId == 2 }">
						<input type="radio" name="platformId" value="" id="platformId1"><label for="platformId1">舒适100</label>
<!-- 						<input type="radio" name="platformId" value="2" id="platformId2" checked="checked"><label for="platformId2">上海绿印中心</label> -->
					</c:when>
					<c:otherwise>
						<input type="radio" name="platformId" value="" id="platformId1" checked="checked"><label for="platformId1">舒适100</label>
<!-- 						<input type="radio" name="platformId" value="2" id="platformId2"><label for="platformId2">上海绿印中心</label> -->
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">logo名称:</label>
			<div class="controls">
				<label class="lbl">
					<input type="text" value="${logoDTO.logoName}" name="logoName" id="logoName" maxlength="15" style="width:220px;">
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">url地址:</label>
			<div class="controls">
				<label class="lbl">
					<c:choose>
						<c:when test="${platformId == 2 }">
							<input type="text" value="www.shushi100.com" class="z-input01" disabled="disabled" style="width:220px;">
						</c:when>
						<c:otherwise>
							<input type="text" value="www.shushi100.com" class="z-input01" disabled="disabled" style="width:220px;">
						</c:otherwise>
					</c:choose>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上传图片:</label>
			<div class="controls">
				<%--<input type="text" value="仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：227*90"  class="z-input01" disabled="disabled" style="width:540px;">--%>
				<label style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：227*90</label>
                <input type="file" id="fileInput" style="display:none;" name="file" onchange="fileChange()" />
				<input name="picUrl" id="picUrl" type="hidden" value="${logoDTO.picUrl }"/>
				<a class="button_4 hei_32" onclick="uploadImg('picUrlImg','picUrl')" > 浏览图片 </a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片预览:</label>
			<span style="color: red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;建议图片尺寸:227*90</span>
			<div class="controls">
				<img src="${filePath}${logoDTO.picUrl }" id="picUrlImg"
						alt="" width="155" height="45" class="mar_lr10 fl showimg">
			</div>
		</div>
		<div class="control-group">
            <div class="controls">
                <input id="btnSubmit" class="btn btn-primary" type="button" value="保 存"/>
                <input id="btnReset" class="btn btn-primary" type="button" value="重 置" />
            </div>
		</div>
	</form:form>
</body>
</html>