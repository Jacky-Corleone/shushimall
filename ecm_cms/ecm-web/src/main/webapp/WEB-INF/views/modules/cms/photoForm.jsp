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

	<form:form id="inputForm" modelAttribute="cmsImageDto" action="${ctx}/cms/photoMange/save" method="post" class="form-horizontal">
      	<input type="hidden" name="cmsImageDto.id" id="status" value="${cmsImageDtoPage.id}"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">图片名称:</label>
			<div class="controls">
				<input type="text" value="${cmsImageDtoPage.imgname}" name="cmsImageDto.imgname" id="imgname" maxlength="30" required="true">
			</div>
		</div>
		<%--<div class="control-group">
			<label class="control-label">图片类型:</label>
			<div class="controls">
				<input type="text" value="${cmsImageDtoPage.imgtype}" name="cmsImageDto.imgtype" id="title" maxlength="30" required="true">
			</div>
		</div>--%>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<input type="text" value="${cmsImageDtoPage.remark}" name="cmsImageDto.remark" id="bannerLink" required="true">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片：</label>
			<div class="controls">
				<label style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：670*450</label>
					<%--<input type="text" value="仅支持jpg,jpeg,pang,bmp格式" class="z-input01" disabled="disabled">--%>
				<span class="z-upload">
					<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
					<input type="file" id="bannerUrlFile" class="file-img" name="file" onchange="startUpload('bannerUrlFile','bannerUrlImg','bannerUrl')">
					<input name="bannerUrl" id="bannerUrl" type="hidden" value="${filePath}${cmsImageDtoPage.path }" required="true"/>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片预览:</label>
			<span style="color: red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;建议图片尺寸:670*450，图片最大不能超过2M</span>
			<div class="controls">
                <c:choose>
                    <c:when test="${cmsImageDtoPage.path!=null}">
                        <img src="${filePath}${cmsImageDtoPage.path}" id="bannerUrlImg"
                             alt="图片" width="155" height="45" class="mar_lr10 fl showimg" >
                    </c:when>
                    <c:otherwise>
                        <img  id="bannerUrlImg"
                             alt="图片" width="155" height="45" class="mar_lr10 fl showimg" >
                    </c:otherwise>
                </c:choose>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">相关标签:</label>
			<div class="controls">
				<ol id="articleSelectList"></ol>
				<div id="showTags">
					<table>
						<c:forEach items="${cmsArticleTagsListDtoList.rows}" var="tagList">
							<tr>
								<td>
									<input name="tagId" type="hidden" value="${tagList.id}" /></td>
								</td>
								<td>
									<input type='text' value="${tagList.tagname}" readonly />
								</td>
								<td>
									<a id='deleteBtn' href='javascript:' class='btn' onclick='delRow(this)'>删除</a>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<a id="relationButton" href="javascript:" class="btn">添加标签</a>
				<script type="text/javascript">
					var articleSelect = [];
					function articleSelectAddOrDel(id,title){
						var isExtents = false, index = 0;
						for (var i=0; i<articleSelect.length; i++){
							if (articleSelect[i][0]==id){
								isExtents = true;
								index = i;
							}
						}
						if(isExtents){
							articleSelect.splice(index,1);
						}else{
							articleSelect.push([id,title]);
						}
						articleSelectRefresh();
					}
					function articleSelectRefresh(){
						$("#articleDataRelation").val("");
						$("#articleSelectList").children().remove();
						for (var i=0; i<articleSelect.length; i++){
							$("#articleSelectList").append("<li>"+articleSelect[i][1]+"&nbsp;&nbsp;<a href=\"javascript:\" onclick=\"articleSelectAddOrDel('"+articleSelect[i][0]+"','"+articleSelect[i][1]+"');\">×</a></li>");
							$("#articleDataRelation").val($("#articleDataRelation").val()+articleSelect[i][0]+",");
						}
					}
					$.getJSON("${ctx}/cms/article/findByIds",{ids:$("#articleDataRelation").val()},function(data){
						for (var i=0; i<data.length; i++){
							articleSelect.push([data[i][1],data[i][2]]);
						}
						articleSelectRefresh();
					});
					$("#relationButton").click(function(){
						top.$.jBox.open("iframe:${ctx}/cms/image/selectList?pageSize=8", "添加相关",$(top.document).width()-220,$(top.document).height()-180,
								{
									buttons:{"确定":true}, loaded:function(h){
									//alert(33);
									$(".jbox-content", top.document).css("overflow-y","hidden");
								},submit: function (v, o, f) {
									//console.log(o);
									var ifr = $(o).children()[0];
									var jb = $(ifr).contents();
									var vl = "";
									var tagsList="<table>";
									$(jb).find("input[name='checkboxs']:checked").each(function(i,n){
										tagsList+="<tr><td><input name='tagId' type='hidden' value='"+$(this).val()+"' /></td><td><input type='text' value='"+$(this).parent().next("td").html()+"' readonly /></td><td><a id='deleteBtn' href='javascript:' class='btn' onclick='delRow(this)'>删除</a></td></tr>";
									});
									/*function delTableRow(n){
									 n.parentNode.removeChild(n);
									 }   删除*/
									$("#showTags").append(tagsList+"</table>");

								}
								});

					});
					function delRow(obj){ //参数为A标签对象
						var row = obj.parentNode.parentNode; //A标签所在行
						var tb = row.parentNode; //当前表格
						var rowIndex = row.rowIndex; //A标签所在行下标
						tb.deleteRow(rowIndex); //删除当前行
					}
					$("#jbox-states").on('click','.jbox-content',function(){
						$(document.getElementById('iframeId').contentWindow.document.body).html()

					})
				</script>
			</div>
		</div>

		<div class="control-group">
            <div class="controls">

                <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;

                <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
        </div>
	</form:form>
</body>
</html>