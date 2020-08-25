<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>图集管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			if($("#link").val()){
				$('#linkBody').show();
				$('#url').attr("checked", true);
			}
			$("#title").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					if ($("#categoryId").val()==""){
						$("#categoryName").focus();
						top.$.jBox.tip('请选择归属栏目','warning');
					}else if (CKEDITOR.instances.content.getData()=="" && $("#link").val().trim()==""){
						top.$.jBox.tip('请填写正文','warning');
					}else{
						loading('正在提交，请稍等...');
						form.submit();
					}
				}
			});
		});
		var divId=201;
		function addPic(flag){
			switch (flag){
				case '1':
					$("#tempFlag").val("1");
					break;
				case '2':
					$("#tempFlag").val("2");
					break;
			}
			$("#uploadPicDiv").modal('show');
		}
		function closeUploadDiv(){
			$("#uploadPicDiv").modal('hide');
		}
		function startUpload(){
			divId=divId+1;
			var url="";
			$.ajaxFileUpload({
						url: '${ctx}/fileUpload/uploadImg?date='+new Date(), //用于文件上传的服务器端请求地址
						secureuri: false, //是否需要安全协议，一般设置为false
						fileElementId: 'uploadPic', //文件上传域的ID
						dataType: 'json', //返回值类型 一般设置为json
						type:"post",
						success: function (data, status){  //服务器成功响应处理函数
							if(data.success){
								/*url=${filePath}+data.url;*/
								var html = "<div class='controls'><a href='${filePath}"+data.url+"' target='_blank'><img src='${filePath}"+data.url+"' class='img-polaroid' style='width:50px;height:50px'/></a></div>";
								var htmlAll = "<div id="+divId+">" +html+
								"<div class='controls'><input type='hidden' name='imgPath' value="+url+"></input></div>" +
								"<label class='control-label'>图片名称:</label>" +
								"<div class='controls'>"+
								"<input name='imgname' htmlEscape='false' maxlength='64' class='input-xlarge'/>"+
								"</div>" +
								"<label class='control-label' >备注:</label>" +
								"<div class='controls'>"+
								"<input name='remark' htmlEscape='false' maxlength='64' class='input-xlarge'/>"+
								"</div>" +
								"<div class='controls'>"+
								"<input id='btn' type='button' class='btn' onclick='deleteImg("+divId+");' value='删除'/>"+
								"</div>" +
								"</div>";
								$("#picAddDiv").append(htmlAll);
								$("#uploadPicDiv").modal('hide');
							}else{
								$.jBox.error(data.msg);
							}

						},
						error: function (data, status, e){//服务器响应失败处理函数
							$.jBox.error(e);
						}
					}
			);
			
		}
		function deleteImg(id){
			$("#"+id).remove();
		}
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li><a href="${ctx}/cms/article/?category.id=${article.category.id}">案例列表</a></li>
	<li class="active"><a href="<c:url value='${fns:getAdminPath()}/cms/image/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">案例<shiro:hasPermission name="cms:article:edit">${not empty article.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:article:edit">查看</shiro:lacksPermission></a></li>
</ul><br/>

<form:form id="inputForm" modelAttribute="article" action="${ctx}/cms/image/saveImg" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<tags:message content="${message}"/>
	<div class="container-fluid">
		<legend ><span class="content-body-bg">商品图片</span></legend>
		<span style="color: red;display: block;" id="imgSize1" >（允许的图片格式有JPG、JPEG、PNG、BMP，单张图片不能大于1M）</span>
		<c:forEach items="${imgList}" var="img" varStatus="status">
			<div id="${ status.index + 1}">
				<div class="controls">
					<input name="imgPath" type="hidden" value="${img.path}" htmlEscape="false" maxlength="64" class="input-xlarge"/>
				</div>
			<label class="control-label">图片名称:</label>
			<div class="controls">
				<input name="imgname" value="${img.imgname}" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			</div>
			<label class="control-label">备注:</label>
			<div class="controls">
				<input name="remark" value="${img.remark}" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			</div>
			<div class="controls">
				<input id="btn" type="button" class="btn" onclick="deleteImg(${ status.index + 1});" value="删除"/>
			</div>
			</div>
		</c:forEach>
		<div class="row-fluid" id="picAddDiv">
			<input type="hidden" name="picUrls" id="picUrls" title="请上传至少一张图片">
		</div>
		<div><a class="btn" href="javascript:addPic('1')"><i class="icon-plus"></i>添加图片</a></div>
		<div class="row-fluid">
			<div class="span12"></div>
		</div>
	</div>
	<div class="form-actions">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
	<!--图片上传弹出框-->
	<div class="modal hide fade" id="uploadPicDiv">
		<input type="hidden" name="tempFlag" id="tempFlag">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>添加上传图片</h3>
		</div>
		<div class="modal-body">
			<p>
				<input type="file" id="uploadPic" name="file" />
			</p>
		</div>
		<div class="modal-footer">
			<a href="javascript:closeUploadDiv()" class="btn">关闭</a>
			<a href="javascript:startUpload()" class="btn btn-primary">确定</a>
		</div>
	</div>
</form:form>

</body>
</html>