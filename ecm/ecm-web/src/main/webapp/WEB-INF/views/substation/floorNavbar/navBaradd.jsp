<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>楼层添加</title>
	<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>

    <link href="${ctxStatic}/kindeditor/themes/default/default.css" rel="stylesheet" />
    <script src="${ctxStatic}/kindeditor/kindeditor-min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/kindeditor/lang/zh_CN.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/ganged.js"></script>
	<script type="text/javascript">
        var picUrlImg;
		$(document).ready(function() {

			//导航栏名称
		    $("#nType").change(function(){
		    	if("1"==$(this).val()){
		    		$("#logoName").attr("maxlength","5");
		    		$("#logoNameLabel").html("温馨提示：最多5个汉字");
		    	}else if("2"==$(this).val()){
		    		$("#logoName").attr("maxlength","13");
		    		$("#logoNameLabel").html("温馨提示：最多13个汉字");
		        }else if("3"==$(this).val()){
		        	$("#logoName").attr("maxlength","6");
		        	$("#logoNameLabel").html("温馨提示：最多6个汉字");
		        }
		    });

			$("input[name='isImg']:eq(0)").attr("checked",'checked');
			$("#imgDiv").hide();
            //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });

			picUrlImg = $("#picUrl").val();
			$("#btnSubmit").click(function(){
	    		var logoName = $("#logoName").val();
	    		var redId = $("#redId").val();
	    		var sortNum = $("#sortNum").val();
	    		var logoName_bl = false;
	    		var nType = $("#nType").val();
				if(logoName!=null && logoName.trim().length>0){
					logoName_bl = true;
	        	}else{
	        		$.jBox.info('请填写名称！');
	        		return false;
	            }
				if("1"==nType && logoName.length>5){
					$.jBox.info('类型一导航栏名称最多5个汉字！');
	        		return false;
				}else if("2"==nType && logoName.length>13){
					$.jBox.info('类型二导航栏名称最多13个汉字！');
	        		return false;
				}else if("3"==nType && logoName.length>6){
					$.jBox.info('类型三导航栏名称最多6个汉字！');
	        		return false;
				}
				var picUrl = $("#picUrl").val();
				var iconLink = $("#iconLink").val();
	    		var iconLink_bl = false;
				if(iconLink!=null && iconLink.trim().length>0){
					iconLink_bl = true;
	        	}else{
	        		$.jBox.info('请填写链接地址！');
	        		return false;
	            }
				var sortNum_bl =false;
				if(sortNum!=null && sortNum.trim().length>0){
					sortNum_bl = true;
	        	}else{
	        		$.jBox.info('请填写排序号！');
	        		return false;
	            }
				var url=picUrl.replace("${filePath}","");
	    		if( iconLink_bl && logoName_bl){
	    			$.ajax({
	    				type: "post",
	        			url: "../floorNavbar/update?themeType=${mallRecommNavDTO.themeType}",
	        			dataType:"json",
	        			data:{
	        				title: logoName,
	        				iconLink: iconLink,
	        				url: url,
							radioValue:$("#intRadio").val(),
	        				redId: redId,
	        				sortNum: sortNum
	            		},
	        			success: function(data){
	        				if(data.success){
	        					var result = data.success;
	        					if(result){
	        						window.location.href="${ctx}/station/floorNavbar/navBarlist?msg=addSuccess&themeType=${mallRecommNavDTO.themeType}";
	        						$.jBox.info("图标保存成功");
	            				}

	        				}
	        			},
	            		 error:function(date){
	            			alert("失败");
	            		}
	        		});
	        	}
	    		return false;
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
				$('#inputForm').reset();
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
				dataType: 'json', //返回值类型 一般设置为json
				type:"post",
				success: function (data, status){
				    if(data.success){
                        //服务器成功响应处理函数
						if(${oss_ftp_change == 0}){
							$("#"+showImg).attr("src","${filePath}"+data.url+"@150w_45h_2e_100sh");
						}else{
							$("#"+showImg).attr("src","${filePath}"+data.url);
						}
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
		$(document).ready(function() {
			//加载类目地区下拉框
			if(${mallRecommNavDTO.themeType == 2}){
				onCSelect('${mallRecommNavDTO.cid}');
			}else{
				onAddressSelect('${mallRecommNavDTO.addressId}');
			}
		})
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="mallRecommNavDTO" method="post" class="form-horizontal">
		<input type="hidden" id="intRadio" value="1"><%--是否为图片，默认为图片类型--%>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<label class="lbl">
					<input type="hidden" value="${mallRecommNavDTO.id}" name="id" id="id" maxlength="15" style="width:220px;">
					<input type="text" value="${mallRecommNavDTO.title}" name="title" id="logoName" maxlength="5" style="width:220px;">
				</label>
				<label id="logoNameLabel" style='color:red'>温馨提示：最多5个汉字</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
				<label class="lbl">
					<input type="text" value="${mallRecommNavDTO.url}" name="iconLink" id="iconLink" maxlength="500" style="width:220px;">
				</label>
			</div>
		</div>
		<c:if test="${mallRecommNavDTO.themeType == 2 }">
			<div class="control-group">
				<label class="control-label">类目:</label>
				<div class="controls">
					<label class="lbl" id="control_label">
						<form:select path="cid" required="true" onchange="onThemeSelect(this.value,'${mallRecommNavDTO.addressId }','${mallRecommNavDTO.themeId }','${mallRecommNavDTO.themeType }')">
							<form:option value="" label="选择类目"></form:option>
						</form:select>
					</label>
				</div>
			</div>
		</c:if>
		<c:if test="${mallRecommNavDTO.themeType == 3 }">
			<div class="control-group">
				<label class="control-label">地区:</label>
				<div class="controls">
					<label class="lbl" id="address_label">
						<form:select path="addressId" onchange="onThemeSelect('${mallRecommNavDTO.cid }',this.value,'${mallRecommNavDTO.themeId }','${mallRecommNavDTO.themeType }')">
							<form:option value="" label="请选择地区"></form:option>
						</form:select>
					</label>
				</div>
			</div>
		</c:if>
		
		<div class="control-group">
			<label class="control-label">子站:</label>
			<div class="controls">
				<label class="lbl">
					<form:select path="themeId" onchange="onRecSelect(this.value,'${mallRecommNavDTO.recId }')">
						<form:option value="" label="请选择子站" />
					</form:select>
				</label>
			</div>
		</div>
	   <div class="control-group">
		<label class="control-label">楼层:</label>
			<div class="controls">
				<label class="lbl" id="rec_label">
					<form:select id="redId" path="recId">
						<form:option value="" label="请选择楼层"/>
						<form:options items="${mallRec}"  itemLabel="titleDTO"  itemValue="idDTO" />
					</form:select>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序号:</label>
			<div class="controls">
				<label class="lbl">
					<input  type="number" value="${mallRecommNavDTO.sortNum}" name="sortNum" id="sortNum" maxlength="10" style="width:220px;">
				</label>
			</div>
		</div>
		<%--<div id="typeDiv">
		<div class="control-group">
			<label class="control-label">导航类型:</label>
			<div class="controls">
				<select id="nType">
					<option name="类型一" value="1">类型一</option>
					<option name="类型二" value="2">类型二</option>
					<option name="类型三" value="3">类型三</option>
				</select>
			</div>
		</div>
		</div>--%>
		<%--<div class="control-group">
			<label class="control-label">是否为图片:</label>
			<div class="controls">
				<label class="lbl">
					<input type="radio"  name="isImg" value="0" />否
					<input type="radio"  name="isImg" value="1" />是
				</label>
			</div>
		</div>--%>
		<%--<div id="imgDiv">--%>
		<div class="control-group">
			<label class="control-label">上传图片:</label>
			<div class="controls">
				<%--<input type="text" value="仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：227*90"  class="z-input01" disabled="disabled" style="width:540px;">--%>
				<label style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：208*465px</label>
                <input type="file" id="fileInput" style="display:none;" name="file" onchange="fileChange()" />
				<input name="imgPath" id="picUrl" type="hidden" value="${mallRecommNavDTO.picSrc }"/>
				<a class="button_4 hei_32" onclick="uploadImg('picUrlImg','picUrl')" > 浏览图片 </a>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片预览:</label>
			<div class="controls">
				<c:choose>
					<c:when test="${oss_ftp_change == 0}">
						<img  src="${filePath}${mallIconSerDTO.imgPath }@155w_45h_2e_100sh" id="picUrlImg"
							  alt="" width="155" height="45" class="mar_lr10 fl showimg" >
					</c:when>
					<c:otherwise>
						<img  src="${filePath}${mallIconSerDTO.imgPath }" id="picUrlImg"
							  alt="" width="155" height="45" class="mar_lr10 fl showimg" >
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<%--</div>--%>
		<div class="control-group">
            <div class="controls">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
                <input id="btnReset" class="btn btn-primary" type="reset" value="重 置" />
            </div>
		</div>
	</form:form>
</body>
</html>