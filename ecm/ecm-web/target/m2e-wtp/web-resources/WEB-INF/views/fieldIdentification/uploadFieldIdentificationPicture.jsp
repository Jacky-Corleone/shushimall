<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实地认证图片</title>
<meta name="decorator" content="default"/>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
	/*图片上传按钮样式*/
	.z-upload{overflow:hidden; width:100px;height:30px;line-height:30px;color:#fff;position:relative;display:block;}
	.z-upload .file-img{font-size:40px;width:100px;height:30px;filter:alpha(opacity=0);opacity:0;position:absolute;left:0px;top:0px;}
	.level{width:151px; height:151px; float:left;}
</style>
<script type="text/javascript">
	//上传工具函数
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
	}

	//上传图片操作
	function upload(){
		//显示进度条
      	showProgressBar();
		$('#pictureForm').ajaxSubmit({
            type:'post',
            dataType:'json',
            success:function(data){
                $.jBox.info(data.message);
              	//AJAX请求后续操作
                afterUploadAJax();
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){
            	$.jBox.info("系统繁忙,请稍后再试!");
            	//AJAX请求后续操作
            	afterUploadAJax();
            }

        })
	}

	//显示进度条
    function showProgressBar(){
    	$("#progressBarDiv").modal("show");
    }
	//隐藏进度条
    function hideProgressBar(){
    	$("#progressBarDiv").modal("hide");
    }

  	//AJAX请求后续操作
    function afterUploadAJax(){
    	//隐藏进度条
        hideProgressBar();
      	//如果是已受理（待审核、审核通过、审核驳回），返回已受理实地认证列表
      	if('${acceptStatus}' == 'accepted'){
            window.location.href = "${ctx}/fieldIdentificationAudit/accepted";
        //如果是未受理，返回未受理实地认证列表
      	}else if('${acceptStatus}' == 'unAccept'){
            window.location.href = "${ctx}/fieldIdentificationAudit/unAccept";
      	}
    }
</script>
</head>
<body>
	<form id="pictureForm" method="post" action="${ctx}/fieldIdentificationPicture/upload">
		<div class="control-group" style="clear:both; margin-top:20px;>
			<span class="content-body-bg">1-厂房产品图片：</span>
			<div class="controls">
				<c:choose>
					<c:when test="${pictureMap['0-0'].pictureSrc != null}">
						<div class="level">
							<img src="${filePath}${pictureMap['0-0'].pictureSrc}" id="workshopPicture01" style="width:150px;height:150px;" class="mar_lr10 fl showimg">
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="workshopPicture01File" name="file" class="file-img" onchange="startUpload('workshopPicture01File','workshopPicture01','workshopPicture01Src')">
								<input name="pictureDTOList[0].id" type="hidden" value="${pictureMap['0-0'].id}">
								<input name="pictureDTOList[0].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[0].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[0].pictureType" type="hidden" value="0"/>
								<input name="pictureDTOList[0].sortNumber" type="hidden" value="0"/>
								<input id="workshopPicture01Src" name="pictureDTOList[0].pictureSrc" type="hidden" value="${pictureMap['0-0'].pictureSrc}"/>
								<input name="pictureDTOList[0].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
				        </div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
	                     	<img id="workshopPicture01" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="workshopPicture01File" name="file" class="file-img" onchange="startUpload('workshopPicture01File','workshopPicture01','workshopPicture01Src')">
								<input name="pictureDTOList[0].id" type="hidden" value="${pictureMap['0-0'].id}">
								<input name="pictureDTOList[0].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[0].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[0].pictureType" type="hidden" value="0">
								<input name="pictureDTOList[0].sortNumber" type="hidden" value="0">
								<input id="workshopPicture01Src" name="pictureDTOList[0].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[0].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
					    </div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['0-1'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['0-1'].pictureSrc}" id="workshopPicture02" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="workshopPicture02File" name="file" class="file-img" onchange="startUpload('workshopPicture02File','workshopPicture02','workshopPicture02Src')">
								<input name="pictureDTOList[1].id" type="hidden" value="${pictureMap['0-1'].id}">
								<input name="pictureDTOList[1].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[1].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[1].pictureType" type="hidden" value="0">
								<input name="pictureDTOList[1].sortNumber" type="hidden" value="1">
								<input id="workshopPicture02Src" name="pictureDTOList[1].pictureSrc" type="hidden" value="${pictureMap['0-1'].pictureSrc}"/>
								<input name="pictureDTOList[1].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
					    </div>
	                </c:when>
	                <c:otherwise>
	                    <div class="level">
	                     	<img id="workshopPicture02" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                 	<span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="workshopPicture02File" name="file" class="file-img" onchange="startUpload('workshopPicture02File','workshopPicture02','workshopPicture02Src')">
								<input name="pictureDTOList[1].id" type="hidden" value="${pictureMap['0-1'].id}">
								<input name="pictureDTOList[1].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[1].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[1].pictureType" type="hidden" value="0">
								<input name="pictureDTOList[1].sortNumber" type="hidden" value="1">
								<input id="workshopPicture02Src" name="pictureDTOList[1].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[1].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
					    </div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['0-2'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['0-2'].pictureSrc}" id="workshopPicture03" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="workshopPicture03File" name="file" class="file-img" onchange="startUpload('workshopPicture03File','workshopPicture03','workshopPicture03Src')">
								<input name="pictureDTOList[2].id" type="hidden" value="${pictureMap['0-2'].id}">
								<input name="pictureDTOList[2].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[2].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[2].pictureType" type="hidden" value="0">
								<input name="pictureDTOList[2].sortNumber" type="hidden" value="2">
								<input id="workshopPicture03Src" name="pictureDTOList[2].pictureSrc" type="hidden" value="${pictureMap['0-2'].pictureSrc}"/>
								<input name="pictureDTOList[2].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
				        </div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="workshopPicture03" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="workshopPicture03File" name="file" class="file-img" onchange="startUpload('workshopPicture03File','workshopPicture03','workshopPicture03Src')">
								<input name="pictureDTOList[2].id" type="hidden" value="${pictureMap['0-2'].id}">
								<input name="pictureDTOList[2].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[2].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[2].pictureType" type="hidden" value="0">
								<input name="pictureDTOList[2].sortNumber" type="hidden" value="2">
								<input id="workshopPicture03Src" name="pictureDTOList[2].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[2].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
				        </div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
             		<c:when test="${pictureMap['0-3'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['0-3'].pictureSrc}" id="workshopPicture04" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="workshopPicture04File" name="file" class="file-img" onchange="startUpload('workshopPicture04File','workshopPicture04','workshopPicture04Src')">
								<input name="pictureDTOList[3].id" type="hidden" value="${pictureMap['0-3'].id}">
								<input name="pictureDTOList[3].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[3].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[3].pictureType" type="hidden" value="0">
								<input name="pictureDTOList[3].sortNumber" type="hidden" value="3">
								<input id="workshopPicture04Src" name="pictureDTOList[3].pictureSrc" type="hidden" value="${pictureMap['0-3'].pictureSrc}"/>
								<input name="pictureDTOList[3].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
				        </div>
	                </c:when>
	                <c:otherwise>
	                    <div class="level">
	                     	<img id="workshopPicture04" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="workshopPicture04File" name="file" class="file-img" onchange="startUpload('workshopPicture04File','workshopPicture04','workshopPicture04Src')">
								<input name="pictureDTOList[3].id" type="hidden" value="${pictureMap['0-3'].id}">
								<input name="pictureDTOList[3].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[3].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[3].pictureType" type="hidden" value="0">
								<input name="pictureDTOList[3].sortNumber" type="hidden" value="3">
								<input id="workshopPicture04Src" name="pictureDTOList[3].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[3].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
				        </div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['0-4'].pictureSrc != null}">
	                    <div class="level">
		                    <img src="${filePath}${pictureMap['0-4'].pictureSrc}" id="workshopPicture05" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								 <button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								 <input type="file" id="workshopPicture05File" name="file" class="file-img" onchange="startUpload('workshopPicture05File','workshopPicture05','workshopPicture05Src')">
								 <input name="pictureDTOList[4].id" type="hidden" value="${pictureMap['0-4'].id}">
								 <input name="pictureDTOList[4].userId" type="hidden" value="${userId}">
								 <input name="pictureDTOList[4].extendId" type="hidden" value="${extendId}">
								 <input name="pictureDTOList[4].pictureType" type="hidden" value="0">
								 <input name="pictureDTOList[4].sortNumber" type="hidden" value="4">
								 <input id="workshopPicture05Src" name="pictureDTOList[4].pictureSrc" type="hidden" value="${pictureMap['0-4'].pictureSrc}"/>
								 <input name="pictureDTOList[4].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
				        </div>
	                </c:when>
	                <c:otherwise>
	                   <div class="level">
	                    	<img id="workshopPicture05" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="workshopPicture05File" name="file" class="file-img" onchange="startUpload('workshopPicture05File','workshopPicture05','workshopPicture05Src')">
								<input name="pictureDTOList[4].id" type="hidden" value="${pictureMap['0-4'].id}">
								<input name="pictureDTOList[4].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[4].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[4].pictureType" type="hidden" value="0">
								<input name="pictureDTOList[4].sortNumber" type="hidden" value="4">
								<input id="workshopPicture05Src" name="pictureDTOList[4].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[4].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
				        </div>
	                </c:otherwise>
	            </c:choose>
			</div>
		</div>

<!------------------------------------------------------------------------------------------------------------------------------------------------------>
		<div class="control-group" style="clear:both; margin-top:200px;>>
			<span class="content-body-bg">2-企业大门图片：</span>
			<div class="controls">
	        	<c:choose>
	            	<c:when test="${pictureMap['1-0'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['1-0'].pictureSrc}" id="doorPicture01" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="doorPicture01File" name="file" class="file-img" onchange="startUpload('doorPicture01File','doorPicture01','doorPicture01Src')">
								<input name="pictureDTOList[5].id" type="hidden" value="${pictureMap['1-0'].id}">
								<input name="pictureDTOList[5].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[5].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[5].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[5].sortNumber" type="hidden" value="0"/>
								<input id="doorPicture01Src" name="pictureDTOList[5].pictureSrc" type="hidden" value="${pictureMap['1-0'].pictureSrc}"/>
								<input name="pictureDTOList[5].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
	                    <img id="doorPicture01" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
	                    <span class="z-upload">
							<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
							<input type="file" id="doorPicture01File" name="file" class="file-img" onchange="startUpload('doorPicture01File','doorPicture01','doorPicture01Src')">
							<input name="pictureDTOList[5].id" type="hidden" value="${pictureMap['1-0'].id}">
							<input name="pictureDTOList[5].userId" type="hidden" value="${userId}">
							<input name="pictureDTOList[5].extendId" type="hidden" value="${extendId}">
							<input name="pictureDTOList[5].pictureType" type="hidden" value="1"/>
							<input name="pictureDTOList[5].sortNumber" type="hidden" value="0"/>
							<input id="doorPicture01Src" name="pictureDTOList[5].pictureSrc" type="hidden"/>
							<input name="pictureDTOList[5].uploadorId" type="hidden" value="${uploadorId}">
				        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['1-1'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['1-1'].pictureSrc}" id="doorPicture02" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="doorPicture02File" name="file" class="file-img" onchange="startUpload('doorPicture02File','doorPicture02','doorPicture02Src')">
								<input name="pictureDTOList[6].id" type="hidden" value="${pictureMap['1-1'].id}">
								<input name="pictureDTOList[6].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[6].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[6].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[6].sortNumber" type="hidden" value="1"/>
								<input id="doorPicture02Src" name="pictureDTOList[6].pictureSrc" type="hidden" value="${pictureMap['1-1'].pictureSrc}"/>
								<input name="pictureDTOList[6].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="doorPicture02" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="doorPicture02File" name="file" class="file-img" onchange="startUpload('doorPicture02File','doorPicture02','doorPicture02Src')">
								<input name="pictureDTOList[6].id" type="hidden" value="${pictureMap['1-1'].id}">
								<input name="pictureDTOList[6].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[6].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[6].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[6].sortNumber" type="hidden" value="1"/>
								<input id="doorPicture02Src" name="pictureDTOList[6].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[6].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['1-2'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['1-2'].pictureSrc}" id="doorPicture03" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="doorPicture03File" name="file" class="file-img" onchange="startUpload('doorPicture03File','doorPicture03','doorPicture03Src')">
								<input name="pictureDTOList[7].id" type="hidden" value="${pictureMap['1-2'].id}">
								<input name="pictureDTOList[7].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[7].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[7].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[7].sortNumber" type="hidden" value="2"/>
								<input id="doorPicture03Src" name="pictureDTOList[7].pictureSrc" type="hidden" value="${pictureMap['1-2'].pictureSrc}"/>
								<input name="pictureDTOList[7].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="doorPicture03" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="doorPicture03File" name="file" class="file-img" onchange="startUpload('doorPicture03File','doorPicture03','doorPicture03Src')">
								<input name="pictureDTOList[7].id" type="hidden" value="${pictureMap['1-2'].id}">
								<input name="pictureDTOList[7].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[7].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[7].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[7].sortNumber" type="hidden" value="2"/>
								<input id="doorPicture03Src" name="pictureDTOList[7].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[7].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['1-3'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['1-3'].pictureSrc}" id="doorPicture04" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="doorPicture04File" name="file" class="file-img" onchange="startUpload('doorPicture04File','doorPicture04','doorPicture04Src')">
								<input name="pictureDTOList[8].id" type="hidden" value="${pictureMap['1-3'].id}">
								<input name="pictureDTOList[8].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[8].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[8].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[8].sortNumber" type="hidden" value="3"/>
								<input id="doorPicture04Src" name="pictureDTOList[8].pictureSrc" type="hidden" value="${pictureMap['1-3'].pictureSrc}"/>
								<input name="pictureDTOList[8].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="doorPicture04" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="doorPicture04File" name="file" class="file-img" onchange="startUpload('doorPicture04File','doorPicture04','doorPicture04Src')">
								<input name="pictureDTOList[8].id" type="hidden" value="${pictureMap['1-3'].id}">
								<input name="pictureDTOList[8].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[8].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[8].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[8].sortNumber" type="hidden" value="3"/>
								<input id="doorPicture04Src" name="pictureDTOList[8].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[8].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['1-4'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['1-4'].pictureSrc}" id="doorPicture05" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="doorPicture05File" name="file" class="file-img" onchange="startUpload('doorPicture05File','doorPicture05','doorPicture05Src')">
								<input name="pictureDTOList[9].id" type="hidden" value="${pictureMap['1-4'].id}">
								<input name="pictureDTOList[9].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[9].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[9].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[9].sortNumber" type="hidden" value="4"/>
								<input id="doorPicture05Src" name="pictureDTOList[9].pictureSrc" type="hidden" value="${pictureMap['1-4'].pictureSrc}"/>
								<input name="pictureDTOList[9].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="doorPicture05" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="doorPicture05File" name="file" class="file-img" onchange="startUpload('doorPicture05File','doorPicture05','doorPicture05Src')">
								<input name="pictureDTOList[9].id" type="hidden" value="${pictureMap['1-4'].id}">
								<input name="pictureDTOList[9].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[9].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[9].pictureType" type="hidden" value="1"/>
								<input name="pictureDTOList[9].sortNumber" type="hidden" value="4"/>
								<input id="doorPicture05Src" name="pictureDTOList[9].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[9].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
			</div>
		</div>

<!------------------------------------------------------------------------------------------------------------------------------------------------------>
		<div class="control-group" style="clear:both; margin-top:200px;>>
			<span class="content-body-bg">3-办公场所图片:</span>
			<div class="controls">
	        	<c:choose>
	            	<c:when test="${pictureMap['2-0'].pictureSrc != null}">
	                	<div class="level">
		                	<img src="${filePath}${pictureMap['2-0'].pictureSrc}" id="officialPicture01" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="officialPicture01File" name="file" class="file-img" onchange="startUpload('officialPicture01File','officialPicture01','officialPicture01Src')">
								<input name="pictureDTOList[10].id" type="hidden" value="${pictureMap['2-0'].id}">
								<input name="pictureDTOList[10].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[10].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[10].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[10].sortNumber" type="hidden" value="0"/>
								<input id="officialPicture01Src" name="pictureDTOList[10].pictureSrc" type="hidden" value="${pictureMap['2-0'].pictureSrc}"/>
								<input name="pictureDTOList[10].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                    <div class="level">
		                    <img id="officialPicture01" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="officialPicture01File" name="file" class="file-img" onchange="startUpload('officialPicture01File','officialPicture01','officialPicture01Src')">
								<input name="pictureDTOList[10].id" type="hidden" value="${pictureMap['2-0'].id}">
								<input name="pictureDTOList[10].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[10].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[10].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[10].sortNumber" type="hidden" value="0"/>
								<input id="officialPicture01Src" name="pictureDTOList[10].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[10].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                    </div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['2-1'].pictureSrc != null}">
	                    <div class="level">
		                    <img src="${filePath}${pictureMap['2-1'].pictureSrc}" id="officialPicture02" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="officialPicture02File" name="file" class="file-img" onchange="startUpload('officialPicture02File','officialPicture02','officialPicture02Src')">
								<input name="pictureDTOList[11].id" type="hidden" value="${pictureMap['2-1'].id}">
								<input name="pictureDTOList[11].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[11].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[11].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[11].sortNumber" type="hidden" value="1"/>
								<input id="officialPicture02Src" name="pictureDTOList[11].pictureSrc" type="hidden" value="${pictureMap['2-1'].pictureSrc}"/>
								<input name="pictureDTOList[11].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                    </div>
	                </c:when>
	                <c:otherwise>
	                    <div class="level">
		                    <img id="officialPicture02" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="officialPicture02File" name="file" class="file-img" onchange="startUpload('officialPicture02File','officialPicture02','officialPicture02Src')">
								<input name="pictureDTOList[11].id" type="hidden" value="${pictureMap['2-1'].id}">
								<input name="pictureDTOList[11].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[11].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[11].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[11].sortNumber" type="hidden" value="1"/>
								<input id="officialPicture02Src" name="pictureDTOList[11].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[11].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                    </div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['2-2'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['2-2'].pictureSrc}" id="officialPicture03" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="officialPicture03File" name="file" class="file-img" onchange="startUpload('officialPicture03File','officialPicture03','officialPicture03Src')">
								<input name="pictureDTOList[12].id" type="hidden" value="${pictureMap['2-2'].id}">
								<input name="pictureDTOList[12].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[12].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[12].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[12].sortNumber" type="hidden" value="2"/>
								<input id="officialPicture03Src" name="pictureDTOList[12].pictureSrc" type="hidden" value="${pictureMap['2-2'].pictureSrc}"/>
								<input name="pictureDTOList[12].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="officialPicture03" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="officialPicture03File" name="file" class="file-img" onchange="startUpload('officialPicture03File','officialPicture03','officialPicture03Src')">
								<input name="pictureDTOList[12].id" type="hidden" value="${pictureMap['2-2'].id}">
								<input name="pictureDTOList[12].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[12].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[12].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[12].sortNumber" type="hidden" value="2"/>
								<input id="officialPicture03Src" name="pictureDTOList[12].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[12].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['2-3'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['2-3'].pictureSrc}" id="officialPicture04" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="officialPicture04File" name="file" class="file-img" onchange="startUpload('officialPicture04File','officialPicture04','officialPicture04Src')">
								<input name="pictureDTOList[13].id" type="hidden" value="${pictureMap['2-3'].id}">
								<input name="pictureDTOList[13].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[13].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[13].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[13].sortNumber" type="hidden" value="3"/>
								<input id="officialPicture04Src" name="pictureDTOList[13].pictureSrc" type="hidden" value="${pictureMap['2-3'].pictureSrc}"/>
								<input name="pictureDTOList[13].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                  	<div class="level">
		                    <img id="officialPicture04" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="officialPicture04File" name="file" class="file-img" onchange="startUpload('officialPicture04File','officialPicture04','officialPicture04Src')">
								<input name="pictureDTOList[13].id" type="hidden" value="${pictureMap['2-3'].id}">
								<input name="pictureDTOList[13].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[13].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[13].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[13].sortNumber" type="hidden" value="3"/>
								<input id="officialPicture04Src" name="pictureDTOList[13].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[13].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                  	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['2-4'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['2-4'].pictureSrc}" id="officialPicture05" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="officialPicture05File" name="file" class="file-img" onchange="startUpload('officialPicture05File','officialPicture05','officialPicture05Src')">
								<input name="pictureDTOList[14].id" type="hidden" value="${pictureMap['2-4'].id}">
								<input name="pictureDTOList[14].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[14].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[14].id" type="hidden">
								<input name="pictureDTOList[14].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[14].sortNumber" type="hidden" value="4"/>
								<input id="officialPicture05Src" name="pictureDTOList[14].pictureSrc" type="hidden" value="${pictureMap['2-4'].pictureSrc}"/>
								<input name="pictureDTOList[14].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="officialPicture05" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="officialPicture05File" name="file" class="file-img" onchange="startUpload('officialPicture05File','officialPicture05','officialPicture05Src')">
								<input name="pictureDTOList[14].id" type="hidden" value="${pictureMap['2-4'].id}">
								<input name="pictureDTOList[14].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[14].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[14].pictureType" type="hidden" value="2"/>
								<input name="pictureDTOList[14].sortNumber" type="hidden" value="4"/>
								<input id="officialPicture05Src" name="pictureDTOList[14].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[14].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
			</div>
		</div>

<!------------------------------------------------------------------------------------------------------------------------------------------------------>
		<div class="control-group" style="clear:both; margin-top:200px;>
			<span class="content-body-bg">4-其他证书图片：</span>
			<div class="controls">
	        	<c:choose>
	            	<c:when test="${pictureMap['3-0'].pictureSrc != null}">
	                	<div class="level">
		                	<img src="${filePath}${pictureMap['3-0'].pictureSrc}" id="certificatePicture01" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="certificatePicture01File" name="file" class="file-img" onchange="startUpload('certificatePicture01File','certificatePicture01','certificatePicture01Src')">
								<input name="pictureDTOList[15].id" type="hidden" value="${pictureMap['3-0'].id}">
								<input name="pictureDTOList[15].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[15].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[15].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[15].sortNumber" type="hidden" value="0"/>
								<input id="certificatePicture01Src" name="pictureDTOList[15].pictureSrc" type="hidden" value="${pictureMap['3-0'].pictureSrc}"/>
								<input name="pictureDTOList[15].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="certificatePicture01" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="certificatePicture01File" name="file" class="file-img" onchange="startUpload('certificatePicture01File','certificatePicture01','certificatePicture01Src')">
								<input name="pictureDTOList[15].id" type="hidden" value="${pictureMap['3-0'].id}">
								<input name="pictureDTOList[15].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[15].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[15].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[15].sortNumber" type="hidden" value="0"/>
								<input id="certificatePicture01Src" name="pictureDTOList[15].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[15].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['3-1'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['3-1'].pictureSrc}" id="certificatePicture02" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="certificatePicture02File" name="file" class="file-img" onchange="startUpload('certificatePicture02File','certificatePicture02','certificatePicture02Src')">
								<input name="pictureDTOList[16].id" type="hidden" value="${pictureMap['3-1'].id}">
								<input name="pictureDTOList[16].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[16].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[16].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[16].sortNumber" type="hidden" value="1"/>
								<input id="certificatePicture02Src" name="pictureDTOList[16].pictureSrc" type="hidden" value="${pictureMap['3-1'].pictureSrc}"/>
								<input name="pictureDTOList[16].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="certificatePicture02" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="certificatePicture02File" name="file" class="file-img" onchange="startUpload('certificatePicture02File','certificatePicture02','certificatePicture02Src')">
								<input name="pictureDTOList[16].id" type="hidden" value="${pictureMap['3-1'].id}">
								<input name="pictureDTOList[16].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[16].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[16].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[16].sortNumber" type="hidden" value="1"/>
								<input id="certificatePicture02Src" name="pictureDTOList[16].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[16].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['3-2'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['3-2'].pictureSrc}" id="certificatePicture03" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="certificatePicture03File" name="file" class="file-img" onchange="startUpload('certificatePicture03File','certificatePicture03','certificatePicture03Src')">
								<input name="pictureDTOList[17].id" type="hidden" value="${pictureMap['3-2'].id}">
								<input name="pictureDTOList[17].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[17].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[17].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[17].sortNumber" type="hidden" value="2"/>
								<input id="certificatePicture03Src" name="pictureDTOList[17].pictureSrc" type="hidden" value="${pictureMap['3-2'].pictureSrc}"/>
								<input name="pictureDTOList[17].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="certificatePicture03" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="certificatePicture03File" name="file" class="file-img" onchange="startUpload('certificatePicture03File','certificatePicture03','certificatePicture03Src')">
								<input name="pictureDTOList[17].id" type="hidden" value="${pictureMap['3-2'].id}">
								<input name="pictureDTOList[17].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[17].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[17].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[17].sortNumber" type="hidden" value="2"/>
								<input id="certificatePicture03Src" name="pictureDTOList[17].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[17].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['3-3'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['3-3'].pictureSrc}" id="certificatePicture04" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="certificatePicture04File" name="file" class="file-img" onchange="startUpload('certificatePicture04File','certificatePicture04','certificatePicture04Src')">
								<input name="pictureDTOList[18].id" type="hidden" value="${pictureMap['3-3'].id}">
								<input name="pictureDTOList[18].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[18].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[18].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[18].sortNumber" type="hidden" value="3"/>
								<input id="certificatePicture04Src" name="pictureDTOList[18].pictureSrc" type="hidden" value="${pictureMap['3-3'].pictureSrc}"/>
								<input name="pictureDTOList[18].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="certificatePicture04" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="certificatePicture04File" name="file" class="file-img" onchange="startUpload('certificatePicture04File','certificatePicture04','certificatePicture04Src')">
								<input name="pictureDTOList[18].id" type="hidden" value="${pictureMap['3-3'].id}">
								<input name="pictureDTOList[18].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[18].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[18].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[18].sortNumber" type="hidden" value="3"/>
								<input id="certificatePicture04Src" name="pictureDTOList[18].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[18].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
	            <c:choose>
	                <c:when test="${pictureMap['3-4'].pictureSrc != null}">
	                 	<div class="level">
		                    <img src="${filePath}${pictureMap['3-4'].pictureSrc}" id="certificatePicture05" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">修改图片</button>
								<input type="file" id="certificatePicture05File" name="file" class="file-img" onchange="startUpload('certificatePicture05File','certificatePicture05','certificatePicture05Src')">
								<input name="pictureDTOList[19].id" type="hidden" value="${pictureMap['3-4'].id}">
								<input name="pictureDTOList[19].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[19].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[19].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[19].sortNumber" type="hidden" value="4"/>
								<input id="certificatePicture05Src" name="pictureDTOList[19].pictureSrc" type="hidden" value="${pictureMap['3-4'].pictureSrc}"/>
								<input name="pictureDTOList[19].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:when>
	                <c:otherwise>
	                 	<div class="level">
		                    <img id="certificatePicture05" style="width:150px;height:150px;" class="mar_lr10 fl showimg" >
		                    <span class="z-upload">
								<button type="button" class="button_4 font_12 border-1 button_small">上传图片</button>
								<input type="file" id="certificatePicture05File" name="file" class="file-img" onchange="startUpload('certificatePicture05File','certificatePicture05','certificatePicture05Src')">
								<input name="pictureDTOList[19].id" type="hidden" value="${pictureMap['3-4'].id}">
								<input name="pictureDTOList[19].userId" type="hidden" value="${userId}">
								<input name="pictureDTOList[19].extendId" type="hidden" value="${extendId}">
								<input name="pictureDTOList[19].pictureType" type="hidden" value="3"/>
								<input name="pictureDTOList[19].sortNumber" type="hidden" value="4"/>
								<input id="certificatePicture05Src" name="pictureDTOList[19].pictureSrc" type="hidden"/>
								<input name="pictureDTOList[19].uploadorId" type="hidden" value="${uploadorId}">
					        </span>
	                 	</div>
	                </c:otherwise>
	            </c:choose>
			</div>
		</div>
		<div style="clear:both; margin-top:200px; text-align:center;">
       		<a href="#" onclick="upload()" class="btn btn-primary">上传</a>
		</div>
		<!--进度条Div开始-->
	    <div class="modal hide fade" id="progressBarDiv">
	        <div class="modal-body">
	            <div class="progress progress-striped active">
	                <div class="bar" style="width: 100%;"></div>
	            </div>
	        </div>
	    </div>
	    <!--进度条Div结束-->
	</form>
</body>
</html>