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
		});

		function init(){
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
	</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="mallBanner" action="${ctx}/sellercenter/mallbanner/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="addressType" id="addressType" value="${addressType}">
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
			<label class="control-label">指向链接:</label>
			<div class="controls">
				<input type="text" value="${mallBanner.bannerLink}" name="bannerLink" id="bannerLink" required="true">
				<span style="color:red;">请不要加“http://”（提示信息需要产品给出）</span>
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
                <label style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：670*450</label>
				<%--<input type="text" value="仅支持jpg,jpeg,pang,bmp格式" class="z-input01" disabled="disabled">--%>
				<span class="z-upload">
					<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
					<input type="file" id="bannerUrlFile" class="file-img" name="file" onchange="startUpload('bannerUrlFile','bannerUrlImg','bannerUrl')">
					<input name="bannerUrl" id="bannerUrl" type="hidden" value="${filePath}${mallBanner.bannerUrl }" required="true"/>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片预览:</label>
			<span style="color: red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;建议图片尺寸:670*450，图片最大不能超过2M</span>
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
		
		<c:if test="${addressType ==1 }">
			<div class="control-group " >
				<label class="control-label">所属子站:</label>
				<div class="controls">
					<input type="hidden" name="themeId" value="${themeDTO.id}" id="themeId"/>
					<input type="text" id="themeName" readonly value="${themeDTO.themeName}"/>
					<a class="btn" id="modal-686826" role="button" href="#addthemeDiv" onclick="redioBotton();" data-toggle="modal">选择地区子站</a>
					<label id="themeNameTitle" for="themeName" class="error"></label>
				</div>
	        </div>
		</c:if>
		<div class="control-group">
			<div class="controls">
                <input type="radio" name="status" value="0" >即时发布</input>
                <input type="radio" name="status" value="1" >定时发布</input>

			</div>
		</div>
		<div class="control-group" id="startTimeDiv">
			<label class="control-label">发布开始时间：</label>
			<div class="controls">
				<input id="startTime" name="startTime" value="${mallBanner.startTime}" type="text" readonly="readonly" class="Wdate"
				 onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group" id="endTimeDiv">
			<label class="control-label">发布结束时间：</label>
			<div class="controls">
				<input id="endTime" name="endTime" value="${mallBanner.endTime}" type="text" readonly="readonly" class="Wdate"
				 onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
            <div class="controls">

                <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;

                <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
        </div>
	</form:form>
	
		<div class="modal hide fade" id="addthemeDiv" role="dialog" aria-hidden="true" aria-labelledby="myModalLabel"
			style="margin-top: -35px; margin-left: -510px; width: 1010px; height: 520px; overflow-y: auto">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h3>子站选择</h3>
			</div>
			<div class="modal-body" style="overflow-y: auto; height: 380px">

				<div id="themeListDiv"></div>

			</div>
			<div class="modal-footer">
				<a href="javascript:void(0);" class="btn" onclick="doUpdate()">确认</a>
				<a href="javascript:void(0);" class="btn" onclick="cancelUpdate()">取消</a>
			</div>
		</div>   
	<script type="text/javascript">
		function redioBotton(){
			 showtheme();
		}
		function showtheme(){
			page(1,10);
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$.jBox.tip("正在加载列表，请稍等",'loading',{opacity:0});
			$.ajax({
	           url:"${ctx}/sellercenter/mallTheme/adlist",
	           type:"post",
	           data:$("#mallThemeForm").serialize(),
	           dataType:'html',
	           success:function(data){
	           	$("#themeListDiv").html(data);
	           	$.jBox.closeTip();
	           	$("#addthemeDiv").modal("show");
	           }
	       });
	   }
		function doUpdate(){
			$("#themeId").val($("#pdId").val());
			$("#themeName").val($("#pdName").val());
			$("#addthemeDiv").modal("hide");
		}
		function cancelUpdate(){
			$("#themeId").val();
			$("#themeName").val();
			$("#addthemeDiv").modal("hide");
		}
	</script>
</body>

</html>