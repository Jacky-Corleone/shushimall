<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
            var ue = UE.getEditor('content',{
				serverUrl:'${ctx}/ueditor/exec',
				imageUrlPrefix:"${filePath}"
			});
			
			ue.addListener('ready',function(){
		
				var imgObjs = $("#ueditor_0").contents().find("img");
				imgObjs.each(function () {
		           if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
		        	   $(this).attr("src","${filePath}"+$(this).attr("src"));
		           }
		           if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
		        	   $(this).attr("_src","${filePath}"+$(this).attr("_src"));
		           }
		   	 	});
		   	 	
	   	 	});
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
                    }else if (UE.getEditor('content').getContent()=="" && $("#link").val().trim()==""){
                        top.$.jBox.tip('请填写正文','warning');
                    }else{
                        $("#content").val(UE.getEditor('content').getContent());
                        loading('正在提交，请稍等...');
                        form.submit();
                    }
				}
			});
		});
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
            $.ajaxFileUpload({
                        url: '${ctx}/fileUpload/uploadImg?date='+new Date(), //用于文件上传的服务器端请求地址
                        secureuri: false, //是否需要安全协议，一般设置为false
                        fileElementId: 'uploadPic', //文件上传域的ID
                        dataType: 'json', //返回值类型 一般设置为json
                        type:"post",
                        success: function (data, status){  //服务器成功响应处理函数
                            if(data.success){
                                var html = "<a href='${filePath}"+data.url+"' target='_blank'><img src='${filePath}"+data.url+"' class='img-polaroid' style='width:50px;height:50px'></a>";
                                $("#imageDiv").html(html);
                                $("#image").val(data.url);
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
		<li><a href="${ctx}/cms/article/?category.id=${article.category.id}">文章列表</a></li>
		<li class="active"><a href="<c:url value='${fns:getAdminPath()}/cms/article/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">文章<shiro:hasPermission name="cms:article:edit">${not empty article.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:article:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="article" action="${ctx}/cms/article/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">归属栏目:</label>
			<div class="controls">
                <tags:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
					title="栏目" url="/cms/category/treeData" module="article" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="required"/>&nbsp;
<!--                 <span> -->
<!--                     <input id="url" type="checkbox" onclick="if(this.checked){$('#linkBody').show()}else{$('#linkBody').hide()}$('#link').val()"><label for="url">外部链接</label> -->
<!--                 </span> -->
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="title">标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="200" class="input-xxlarge required"/>&nbsp;
<!-- 				<label>颜色:</label> -->
<%-- 				<form:select path="color" class="input-mini"> --%>
<%-- 					<form:option value="" label="默认"/> --%>
<%-- 					<form:options items="${fns:getDictList('color')}" itemLabel="label" itemValue="value" htmlEscape="false" /> --%>
<%-- 				</form:select> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="title">归属城市:</label>
			 <div class="controls">
		    <form:select path="cityid" class="input">
			<form:option value="" label="全国"/> 
			<form:options items="${addresList}" itemLabel="name" itemValue="code" htmlEscape="false" /> 
			</form:select>
            </div>
		</div>
		<div class="control-group">
			<label class="control-label" for="title">副标题:</label>
			   <div class="controls">
                <form:input path="title2" htmlEscape="false" maxlength="200" class="input-xlarge"/>
            </div>
		</div>
<!--         <div id="linkBody" class="control-group" style="display:none"> -->
<!--             <label class="control-label" for="link">外部链接:</label> -->
<!--             <div class="controls"> -->
<%--                 <form:input path="link" htmlEscape="false" maxlength="200" class="input-xlarge"/> --%>
<!--                 <span class="help-inline">绝对或相对地址。</span> -->
<!--             </div> -->
<!--         </div> -->
		<div class="control-group">
			<label class="control-label" for="title">文章标签：</label>
			   <div class="controls">
                
            </div>
		</div>
		<div class="control-group">
			<label class="control-label" for="title">产品类别:</label>
			   <div class="controls">
		    <form:select path="cateid" class="input">
			<form:option value="" label=""/> 
			<form:options items="${platformList}" itemLabel="categoryCName" itemValue="categoryCid" htmlEscape="false" /> 
			</form:select>  
			</div>
         </div>
         <div class="control-group">
		    <label class="control-label" for="title">品牌:</label>
			   <div class="controls">
 		    <form:select path="brandid" class="input">
			<form:option value="" label=""/> 
			<form:options items="${brandAllList}" itemLabel="brandName" itemValue="brandId" htmlEscape="false" /> 
			</form:select> 
            </div>
         </div>
		<div class="control-group">
			<label class="control-label" for="keywords">关键字:</label>
			<div class="controls">
				<form:input path="keywords" htmlEscape="false" maxlength="200" class="input-xlarge"/>
				<span class="help-inline">多个关键字，用空格分隔。</span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label" for="weight">权重:</label>
			<div class="controls">
				<form:input path="weight" htmlEscape="false" maxlength="200" class="input-mini required digits"/>&nbsp;
				<span>
					<input id="weightTop" type="checkbox" onclick="$('#weight').val(this.checked?'999':'0')"><label for="weightTop">置顶</label>
				</span>
				&nbsp;过期时间：
				<input id="weightDate" name="weightDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					value="<fmt:formatDate value="${article.weightDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				<span class="help-inline">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</span>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label" for="description">摘要:</label>
			<div class="controls">
				<form:textarea path="description" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
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
			        <c:if test="${not empty article.image}">
			        	<a href="${filePath}${article.image}" target="_blank"><img src="${filePath}${article.image}" class="img-polaroid" style="width:50px;height:50px"></a>
			        </c:if>
		        </div>
                    <input type="hidden" name="image" id="image" value="${article.image}" title="请上传至少一张图片">
                    <div><a class="btn" href="javascript:addPic('1')"><i class="icon-plus"></i>添加图片</a></div>
                </div>
                <div class="row-fluid">
                    <div class="span12"></div>
                </div>
		</div>
		<div class="control-group">
			<label class="control-label" for="content">正文:</label>
			<div class="controls">
			<form:textarea id="content" htmlEscape="true" path="articleData.content" rows="4" maxlength="10000"  style="width:700px;height:300px;" class="input-xxlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="articleData.copyfrom">来源:</label>
			<div class="controls">
				<form:input path="articleData.copyfrom" htmlEscape="false" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">相关文章:</label>
			<div class="controls">
				<form:hidden id="articleDataRelation" path="articleData.relation" htmlEscape="false" maxlength="200" class="input-xlarge"/>
				<ol id="articleSelectList"></ol>
				<a id="relationButton" href="javascript:" class="btn">添加相关</a>
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
						top.$.jBox.open("iframe:${ctx}/cms/article/selectList?pageSize=8", "添加相关",$(top.document).width()-220,$(top.document).height()-180,{
							buttons:{"确定":true}, loaded:function(h){
								$(".jbox-content", top.document).css("overflow-y","hidden");
							}
						});
					});
				</script>
			</div>
		</div>
	<%-- 	<div class="control-group">
			<label class="control-label" for="articleData.allowComment">是否允许评论:</label>
			<div class="controls">
				<form:radiobuttons path="articleData.allowComment" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="posidList">推荐位:</label>
			<div class="controls">
				<form:checkboxes path="posidList" items="${fns:getDictList('cms_posid')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label" for="posidList">文章创建者：</label>
			<div class="controls">
				<shiro:principal property="name"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="posidList"> 最后编辑者:</label>
			<div class="controls">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="createDate">发布时间:</label>
			<div class="controls">
				<input id="createDate" name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<shiro:hasPermission name="cms:article:audit">
			<div class="control-group">
				<label class="control-label" for="delFlag">发布状态:</label>
				<div class="controls">
					<form:radiobuttons path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
					<span class="help-inline"></span>
				</div>
			</div>
		</shiro:hasPermission>
		<shiro:hasPermission name="cms:category:edit">
            <div class="control-group">
                <label class="control-label" for="customContentView">自定义内容视图:</label>
                <div class="controls">
                      <form:select path="customContentView">
                          <form:option value="" label="默认视图"/>
                          <form:options items="${contentViewList}" htmlEscape="false"/>
                      </form:select>
                      <span class="help-inline">自定义内容视图名称必须以"${article_DEFAULT_TEMPLATE}"开始</span>
                </div>
            </div>
        <%--     <div class="control-group">
                <label class="control-label" for="viewConfig">自定义视图参数:</label>
                <div class="controls">
                      <form:input path="viewConfig" htmlEscape="true"/>
                      <span class="help-inline">视图参数例如: {count:2, title_show:"yes"}</span>
                </div>
            </div> --%>
		</shiro:hasPermission>
	<%-- 	<c:if test="${not empty article.id}">
			<div class="control-group">
				<label class="control-label">查看评论:</label>
				<div class="controls">
					<input id="btnComment" class="btn" type="button" value="查看评论" onclick="viewComment('${ctx}/cms/comment/?module=article&contentId=${article.id}&status=0')"/>
					<script type="text/javascript">
						function viewComment(href){
							top.$.jBox.open('iframe:'+href,'查看评论',$(top.document).width()-220,$(top.document).height()-180,{
								buttons:{"关闭":true},
								loaded:function(h){
									$(".jbox-content", top.document).css("overflow-y","hidden");
									$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
									$("body", h.find("iframe").contents()).css("margin","10px");
								}
							});
							return false;
						}
					</script>
				</div>
			</div>
		</c:if> --%>
		<div class="form-actions">
			<shiro:hasPermission name="cms:article:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
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
</body>
</html>