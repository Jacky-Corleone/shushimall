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
  <li class="active"><a href="<c:url value='${fns:getAdminPath()}/cms/image/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">工程步骤<shiro:hasPermission name="cms:article:edit">${not empty article.id?'':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:article:edit">查看</shiro:lacksPermission></a></li>
</ul><br/>

<form:form id="inputForm" modelAttribute="article" action="${ctx}/cms/image/save" method="post" class="form-horizontal">
  <form:hidden path="id"/>
  <tags:message content="${message}"/>
  <div class="control-group">
    <label class="control-label" for="title">标题:</label>
    <div class="controls">
      <form:input path="title" htmlEscape="false" maxlength="100" class="input-xxlarge measure-input required"/>&nbsp;
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="articleData.copyfrom">排序:</label>
    <div class="controls">
      <form:input path="articleData.copyfrom" htmlEscape="false" maxlength="200" class="input-xlarge"/>
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
    <label class="control-label" for="content">正文:</label>
    <div class="controls">
      <form:textarea id="content" htmlEscape="true" path="articleData.content" rows="4" maxlength="10000"  style="width:700px;height:300px;" class="input-xxlarge"/>
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
  <div class="form-actions">
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
    <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
  </div>
</form:form>

</body>
</html>