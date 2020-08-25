<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>专题管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#reContent").focus();
			$("#inputForm").validate();
		});
		
		function cancel(){
			window.location.href="${ctx}/cms/specsub/list";
		}
		function save(){
			$("#inputForm").submit();
		}
		
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
        
        function closeUploadDiv(){
            $("#uploadPicDiv").modal('hide');
        }
        function startUpload(){
            $.ajaxFileUpload({
                    url: '${ctx}/fileUpload/uploadImg?date='+new Date(), //用于文件上传的服务器端请求地址
                    secureuri: false, //是否需要安全协议，一般设置为false
                    fileElementId: 'uploadPic', //文件上传域的ID
                    dataType: 'json', //返回值类型 一般设置为json
                    type:"post",
                    success: function (data, status){  //服务器成功响应处理函数
                        if(data.success){
                        	alert(2);
                             var html = "<a href='${filePath}"+data.url+"' target='_blank'><img src='${filePath}"+data.url+"' class='img-polaroid' style='width:50px;height:50px'></a>";
                            $("#imageDiv").html(html);
                            $("#specialSubjectPicId").val(data.url);
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
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/cms/specsub/list">专题列表</a></li>
		<li class="active"><a href="${ctx}/cms/specsub/add">专题添加</a></li>
	</ul><br/>
	<form:form  id="inputForm" modelAttribute="cmsSpecialSubjectDTO" action="${ctx}/cms/specsub/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:hidden path="id" id="id" value="${cmsSpecialSubjectDTO.id}"/>
		<form:hidden id="specialSubjectPicId" path="specialSubjectPic" value="${cmsSpecialSubjectDTO.specialSubjectPic}"/>
		<div class="control-group">
			<label class="control-label">专题名称：</label>
			<div class="controls">
				<form:input path="specialSubjectName" value="${cmsSpecialSubjectDTO.specialSubjectName}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品类别：</label>
			<div class="controls">
				<form:select path="productCategory" value="${cmsSpecialSubjectDTO.productCategory}">
				<form:options items="${cmsSpecialSubjectDTO.productList}" itemLabel="categoryCName" itemValue="categoryCid" htmlEscape="false" /> 
				</form:select>
			</div>
		</div>
		
		<!-- 网络 -->
		<div class="control-group">
			<label class="control-label">专题类别：</label>
			<div class="controls">
				<form:select path="specialSubjectCategory" items="${fns:getDictList('cms_special_category')}"  itemLabel="label" itemValue="label"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">选择模板：</label>
			<div class="controls">
				<form:select path="specialSubjectModel" value="${cmsSpecialSubjectDTO.specialSubjectModel}" >
					<form:options items="${cmsSpecialSubjectDTO.specialList}" itemLabel="name" itemValue="id" htmlEscape="false" /> 
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">标题：</label>
			<div class="controls">
				<form:input path="specialSubjectTitle" value="${cmsSpecialSubjectDTO.specialSubjectTitle}"/>
			</div>
		</div>
		
		<!-- 线下 -->
		<div class="control-group" >
			<label class="control-label">关键字:：</label>
			<div class="controls">
				<form:input path="keywords" value="${cmsSpecialSubjectDTO.keywords}"/>
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">类别简介：</label>
			<div class="controls">
				<form:textarea path="categoryProfile" value="${cmsSpecialSubjectDTO.categoryProfile}"/>
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">地址：</label>
			<div class="controls">
				<form:input path="address" value="${cmsSpecialSubjectDTO.address}"/>
			</div>
		</div>
		
		<div class="container-fluid">
			<label class="control-label">缩略图:</label>
			<span style="color: red;display: block;" id="imgSize1" >（允许的图片格式有JPG、JPEG、PNG、BMP，单张图片不能大于1M）</span>
<!-- 			<div class="controls"> -->
<%--                 <input type="hidden" id="image" name="image" value="${article.imageSrc}" /> --%>
<%-- 				<tags:ckfinder input="image" type="thumb" uploadPath="/cms/article" selectMultiple="false"/> --%>
<!-- 			</div> -->
		        <div class="row-fluid" id="picAddDiv">
		        <div id="imageDiv">
			        <c:if test="${not empty cmsSpecialSubjectDTO.specialSubjectPic}">
			        	<a href="${filePath}${cmsSpecialSubjectDTO.specialSubjectPic}" target="_blank"><img src="${filePath}${cmsSpecialSubjectDTO.specialSubjectPic}" class="img-polaroid" style="width:50px;height:50px"></a>
			        </c:if>
		        </div>
                    <input type="hidden" name="image" id="image" value="${cmsSpecialSubjectDTO.specialSubjectPic}" title="请上传至少一张图片">
                    <div><a class="btn" href="javascript:addPic('1')"><i class="icon-plus"></i>添加图片</a></div>
                </div>
                <div class="row-fluid">
                    <div class="span12"></div>
                </div>
		</div>
		
		 <div class="control-group" >
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" value="${cmsSpecialSubjectDTO.sort}"/>
			</div>
		</div>
		
		<div class="control-group" >
			<label class="control-label">前台列表：</label>
			<div class="controls">
				<form:radiobutton path="foregroundFlag" value="0"/>是
				<form:radiobutton path="foregroundFlag" value="1"/>否
			</div>
		</div>
		
		<div class="control-group" >
			<label class="control-label">是否显示：</label>
			<div class="controls">
				<form:radiobutton path="displayFlag" value="0"/>显示
				<form:radiobutton path="displayFlag" value="1"/>隐藏
			</div>
		</div> 
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-inverse" type="button" value="保存" onclick="save()"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="cancel();"/>
		</div>
	</form:form>
	
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
</body>
</html>