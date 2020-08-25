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
			var a=$("#acateidCheck").val();
			/*alert(a);
			$("#acateid option[value='效果图']").attr("selected", true);*/
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
					}else if (CKEDITOR.instances.content.getData()=="" && $("#link").val().trim()==""){
						top.$.jBox.tip('请填写正文','warning');
					}else{
						$("#content").val(UE.getEditor('content').getContent());
						loading('正在提交，请稍等...');
						form.submit();
					}
				}
			});
			/*$("#btnTagSubmit").click(function(){
				var obj=document.getElementsByName('checkboxs'); //选择所有name="'test'"的对象，返回数组
				//取到对象数组后，我们来循环检测它是不是被选中
				var s='';//选中的id
				var alls='';//所有id
				var artId= $("#hiddenId").val();//文章id
				for(var i=0; i<obj.length; i++){
					if(obj[i].checked) s+=obj[i].value+','; //如果选中，将value添加到变量s中
					alls+=obj[i].value+',';
				}
				//那么现在来检测s的值就知道选中的复选框的值了
				alert(s==''?'你还没有选择任何内容！':s);
				if(s!=null&&s!=""){
				var url = "${ctx}/cms/image/tagsSave";
				$("#tagDiv").css("display","none");
				$.post( url , {checkIds: s,ids :alls,artId:artId},function(){
				});
				}
			})*/
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
								$("#picAddDiv").prepend(html);
								$("#picAddDiv").modal('hide');
							}else{
								$.jBox.error(data.msg);
							}

						},
						error: function (data, status, e){//服务器响应失败处理函数
							$.jBox.error(e);
						}
					}
			);
			var html = "" +
					"<label class='control-label' for='articleData.copyfrom'>图片名称:</label>" +
					"<div class='controls'>"+
						"<input name='imgname' htmlEscape='false' maxlength='64' class='input-xlarge'/>"+
					"</div>" +
					"<label class='control-label' for='articleData.copyfrom'>备注:</label>" +
					"<div class='controls'>"+
					"<input name='remark' htmlEscape='false' maxlength='64' class='input-xlarge'/>"+
					"</div>";
			$("#picAddDiv").prepend(html);
			$("#uploadPicDiv").modal('hide');
		}
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li><a href="${ctx}/cms/article/?category.id=${article.category.id}">案例列表</a></li>
	<li class="active"><a href="<c:url value='${fns:getAdminPath()}/cms/image/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">图集<shiro:hasPermission name="cms:article:edit">${not empty article.id?'管理':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:article:edit">查看</shiro:lacksPermission></a></li>
</ul><br/>

<form:form id="inputForm" modelAttribute="article" action="${ctx}/cms/image/save" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<tags:message content="${message}"/>
	<div class="control-group">
		<label class="control-label">归属栏目:</label>
		<div class="controls">
			<tags:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
							 title="栏目" url="/cms/category/treeData" module="article" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="required"/>&nbsp;
                <span>
                    <input id="url" type="checkbox" onclick="if(this.checked){$('#linkBody').show()}else{$('#linkBody').hide()}$('#link').val()"><label for="url">外部链接</label>
                </span>
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
		<label class="control-label" for="articleData.copyfrom">文章类型:</label>
		<div class="controls">
				<input type="hidden" value="${article.acateid}" id="acateidCheck">
				<select name="acateid" id="acateid">
					<option value="工程案例">工程案例</option>
					<option value="效果图">效果图</option>
				</select>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="articleData.copyfrom">产品类别:</label>
		<div class="controls">
			<form:input path="cateid" htmlEscape="false" maxlength="64" class="input-xlarge"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="title">标题:</label>
		<div class="controls">
			<form:input path="title" htmlEscape="false" maxlength="100" class="input-xxlarge measure-input required"/>&nbsp;
			<label>颜色:</label>
			<form:select path="color" class="input-mini">
				<form:option value="" label="默认"/>
				<form:options items="${fns:getDictList('color')}" itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
		</div>
	</div>
	<div id="linkBody" class="control-group" style="display:none">
		<label class="control-label" for="link">外部链接:</label>
		<div class="controls">
			<form:input path="link" htmlEscape="false" maxlength="200" class="input-xlarge"/>
			<span class="help-inline">绝对或相对地址。</span>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="keywords">关键字:</label>
		<div class="controls">
			<form:input path="keywords" htmlEscape="false" maxlength="200" class="input-xlarge"/>
			<span class="help-inline">多个关键字，用空格分隔。</span>
		</div>
	</div>
	<div class="control-group">
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
	</div>
	<div class="control-group">
		<label class="control-label" for="description">摘要:</label>
		<div class="controls">
			<form:textarea path="description" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">缩略图:</label>
		<div class="controls">
			<input type="hidden" id="image" name="image" value="${article.imageSrc}" />
			<tags:ckfinder input="image" type="thumb" uploadPath="/cms/article" selectMultiple="false"/>
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
		<label class="control-label">相关标签:</label>
		<div class="controls">
			<form:hidden id="articleDataRelation" path="articleData.relation" htmlEscape="false" maxlength="200" class="input-xlarge"/>
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
			<a id="relationTagButton" href="javascript:" class="btn">添加标签</a>
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
				$("#relationTagButton").click(function(){
					top.$.jBox.open("iframe:${ctx}/cms/article/selectTagList?pageSize=8", "添加标签",$(top.document).width()-220,$(top.document).height()-180,
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
		<label class="control-label">相关文章:</label>
		<div class="controls">
			<form:hidden id="articleDataRelation" path="articleData.relation" htmlEscape="false" maxlength="200" class="input-xlarge"/>
			<ol id="articleSelectList"></ol>
			<div id="showRelation">
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
			<a id="relationButton" href="javascript:" class="btn">添加相关</a>
			<script type="text/javascript">

				function abcd(){
				}

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
					top.$.jBox.open("iframe:${ctx}/cms/article/selectList?pageSize=8", "添加相关2222",$(top.document).width()-220,$(top.document).height()-180,{
						buttons:{"确定":true}, loaded:function(h){
							$(".jbox-content", top.document).css("overflow-y","hidden");
						},submit: function (v, o, f) {
							//console.log(o);
							var ifr = $(o).children()[0];
							console.log(ifr);
							var jb = $(ifr).contents();
							console.log(jb);
							var vl = "";
							var tagsList="<table>";
							$(jb).find("input[name='checkboxs']:checked").each(function(i,n){
								tagsList+="<tr><td><input name='articleRelation' type='hidden' value='"+$(this).val()+"' /></td><td><input type='text' value='"+$(this).parent().next('td').next('td').text()+"' readonly /></td><td><a id='deleteBtn' href='javascript:' class='btn' onclick='delRow(this)'>删除</a></td></tr>";
							});
							/*function delTableRow(n){
							 n.parentNode.removeChild(n);
							 }   删除*/
							$("#showRelation").append(tagsList+"</table>");

						}
					});
				});
				function delRow(obj){ //参数为A标签对象
					var row = obj.parentNode.parentNode; //A标签所在行
					var tb = row.parentNode; //当前表格
					var rowIndex = row.rowIndex; //A标签所在行下标
					tb.deleteRow(rowIndex); //删除当前行
				}
			</script>
		</div>
	</div>
	<div class="control-group">
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
		<div class="control-group">
			<label class="control-label" for="viewConfig">自定义视图参数:</label>
			<div class="controls">
				<form:input path="viewConfig" htmlEscape="true"/>
				<span class="help-inline">视图参数例如: {count:2, title_show:"yes"}</span>
			</div>
		</div>
	</shiro:hasPermission>
	<c:if test="${not empty article.id}">
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
	</c:if>
	<div class="form-actions">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
</form:form>
<%--<div id="tagDiv" style="position: absolute;display: none">
	<div >
		<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/image/list" method="post" class="breadcrumb form-search">
			&lt;%&ndash;	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
            &lt;%&ndash;	<label>栏目：</label><tags:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
                                                   title="栏目" url="/cms/category/treeData" module="article" cssClass="input-small"/>
                <label>标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;&ndash;%&gt;&ndash;%&gt;
		</form:form>
		<input type="hidden" id="hiddenId" name="artId" value="${article.id}">

		<div class="pagination" STYLE="float: left">${page}</div>
		<div>
		<input id="btnTagSubmit" class="btn btn-primary" style="float: right" type="button" value="保 存"/>&nbsp;
		</div>
	</div>
</div>--%>
</body>
</html>