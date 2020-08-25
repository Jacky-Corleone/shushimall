<%--
  Created by IntelliJ IDEA.
  User: menpg
  Date: 2015/3/2
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>服务规则新增</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
        function getcode(date){
            if(date){
                return date;
            }else{
                return '';
            }
        }
        function closeUploadDiv(){
            $("#uploadPicDiv").modal('hide');
        }
        function startUpload(){
            $('#picAddDiv img').remove();
            //判断图片格式
            var fileInput = $("#uploadPic").val();
            var extStart = fileInput.lastIndexOf(".");
            var ext = fileInput.substring(extStart,fileInput.length).toUpperCase();
            if(ext!=".JPG" && ext!=".jpg" && ext!=".BMP"&&ext!=".bmp"&& ext!=".PNG"&&ext!=".PNG"&& ext!=".JPEG" && ext!=".jpeg"&&ext!=".gif"&&ext!=".GIF"){
                $.jBox.info("图片限于JPG,BMP,PNG,JPEG格式");
                return false;
            }
            $.ajaxFileUpload({
                        url: '${ctx}/fileUpload/uploadsize?size=512000&date='+new Date(), //用于文件上传的服务器端请求地址
                        secureuri: false, //是否需要安全协议，一般设置为false
                        fileElementId: 'uploadPic', //文件上传域的ID
                        dataType: 'json', //返回值类型 一般设置为json
                        type:"post",
                        success: function (data, status){  //服务器成功响应处理函数
                            if(data.success){
                                //图片上传
                                var html = "<div id='fileinputid' class='preview-pic'><img class='showimg' src='${filePath}"+data.url+"'class='img-polaroid' style='width:68px;height:68px'>"+"</div>";
                                $("#fileinputid").html(html);
                                $("#fileurlid").val(data.url);
                                $("#uploadPicDiv").modal('hide');
                                $('.showimg').fancyzoom({
                                    Speed: 400,
                                    showoverlay: false,
                                    imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
                                });
                            }else{
                                $.jBox.info(data.msg);
                            }
                        },
                        error: function (data, status, e){//服务器响应失败处理函数
                            $.jBox.error("系统繁忙，请稍后再试");
                        }
                    }
            );
        }
        function addPic(){
            $("#uploadPicDiv").modal('show');
        }
        function subminplat(){
            var fileurlid=$("#fileurlid").val();
            var name=$("#name").val();
            var mx=$("#mx").val();
            var simple=$("#simple").val();
            var detail=$("#detail").val();
            if(!fileurlid){
                alert("请上传图片");
                return;
            }
            if(!name){
                alert("请输入规则名");
                return;
            }
            if(!mx){
                alert("请选择帮助文档");
                return;
            }
            if(!simple){
                alert("请填写简介");
                return;
            }
            if(!detail){
                alert("请填写详情描述");
                return;
            }
            $.ajax({
                url:"${ctx}/serverule/addrult",
                type:"post",
                data:{
                    fileurlid:fileurlid,
                    name:name,
                    mx:mx,
                    simple:simple,
                    detail:detail
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        $.jBox.prompt(data.msg, '消息', 'info', { closed: function () {
                            window.location.href="${ctx}/serverule/list";
                        } });
                    }else{
                        $.jBox.info(data.msg);
                    }
                }
            });
        }

        function docname(){
        	$("#lx").html("<option value=''>请选择文档类型</option>");
        	$("#lx").select2("val","");
        	$("#mx").html("<option value=''>请选择具体文档</option>");
        	$("#mx").select2("val","");
            $.ajax({
                type : 'GET',
                contentType : 'application/json',
                url : "${ctx}/mallClassify/getMallClassifyMap",
                data:{
                    type:$("#mallType").val(),
                    status:'2'
                },
                dataType : 'json',
                success : function(data) {
                    var html="<option value=''>请选择文档类型</option>";
                    $.each(data.data, function(i, item) {
                        html=html+"<option value='"+item.id+"'>"+item.title+"</option>";
                    });
                    $("#lx").html(html);
                }
            });
        }
        
        function lxofmx(){
            var docclass= $("#lx").val();
            $("#mx").html("<option value=''>请选择具体文档</option>");
        	$("#mx").select2("val","");
            $.ajax({
                url:"${ctx}/serverule/selectdoc",
                type:"post",
                data:{
                    docclass:docclass
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var html="<option value=''>请选择具体文档</option>";
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                                html += "<option value='"+getcode(item.mallId)+"'>"+getcode(item.mallTitle)+"</option>";
                            });
                        }
                        $("#mx").html(html);
                    }
                }
            });
        }
    </script>
    <style>
        table {
            max-width: 100%;
            background-color: transparent;
            border-collapse: collapse;
            border-spacing: 0;
        }
        .y-tb1{
            border-top: 1px solid #eee;
            border-bottom: 1px solid #eee;
            border-left:1px solid #eee;
            border-right: 1px solid:#eee;
        }.y-tb1 td{
             border-top: 1px solid #eee;
             border-bottom: 1px solid #eee;
             border-left:1px solid #eee;
             border-right: 1px solid:#eee;
         }.y-tb1 th{
              border-top: 1px solid #eee;
              border-bottom: 1px solid #eee;
              border-left:1px solid #eee;
              border-right: 1px solid:#eee;
              font-weight:normal;
              color:#0000ff;
          }
        .mb20 {
            margin-bottom: 20px;
        }
        .z-tbl {
            width: 100%;
            text-align: center;
        }
        .z-tbl td {
            border-bottom: 1px dotted #ccc;
            padding: 10px 5px;
            line-height: 16px;
            color: #666;
            text-align: center;
        }.z-tbl th {
             border-bottom: 1px dotted #ccc;
             padding: 10px 5px;
             line-height: 16px;
             color: #666;
             text-align: center;
         }
        div{
        }
        .y-box {
            width: 95%;
            border: 1px solid #eee;
            border-top: 2px solid #00a2ca;
            margin-bottom: 20px;
        }.bd {
             padding: 14px 0;
         }.z-item {
              overflow: hidden;
              padding: 6px 0;
          }.z-tt {
               display:inline;
               float: left;
               width: 70px;
               padding-left: 20px;
               height: 30px;
               line-height: 30px;
               color: #999;
           }.z-bd {
                display:inline;
                overflow: hidden;
                color: #666;
                float: left;
            }.preview-pic {
                 display: inline-block;
                 zoom: 1;
                 border: 1px solid #eee;
                 border-radius: 2px;
                 -moz-border-radius: 2px;
                 -webkit-border-radius: 2px;
             }.hd {
                  width: 100%;
                  height: 44px;
                  line-height: 44px;
                  border-bottom: 1px solid #eee;
                  position: relative;
              }

        h3{
            color:#000000;
            height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }.h3-current{
             width:160px;
             height: 36px;
             text-align: center;
         }
        button.btn-bac{
            background: #ccc;;
        }
        a.y-hx {
            text-decoration:none;
        }
        ul.ul1{
            display:inline;
            list-style:none;
            display:block;
        }
        li.li1{
            margin-bottom: 10px;
            margin-right: 20px;
            float:left;
            display:block;
        }
        span.gray-color{
            color: #999;
        }
        input.z-input01 {
            padding: 6px;
            border: 1px solid #eee;
            color: #666;
            background: #f9f9f9;
            width: 286px;
            height: 35px;
            border-radius: 2px;
            -moz-border-radius: 2px;
            -webkit-border-radius: 2px;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
            <div class="y-box">
                <div class="hd">
                    <h3 class="h3">创建规则</h3>
                </div>
                <div class="bd">
                    <div class="z-item">
                        <div class="z-tt">
                            <span>上传图片:</span>
                        </div>
                        <div class="z-bd">
                            <label id="fileinputid" >单张图片不能大于500K;允许的图片格式有jpg、jpeg、png、JPG、JPEG、PNG</label>
                            <input name="fileurl" id="fileurlid" value="" hidden="hidden">
                            <a class="btn" href="javascript:addPic()"><i class="icon-plus"></i>添加图片</a>
                        </div>
                    </div>
                    <div class="z-item">
                        <div class="z-tt">
                            <span>名称:</span>
                        </div>
                        <div class="z-bd">
                            <input class="z-input01" value="${map.ruleName}" name="name" id="name" maxlength="100" />
                        </div>
                    </div>
                    <div class="z-item" style="margin-top:10px">
                        <div class="z-tt">
                            <span>帮助文档:</span>
                        </div>
                        <div style="display:inline-block;overflow:hidden;color:#666;height:50px;">
                            <select name="mallType" id="mallType" style="width: 140px;" onchange="docname()" class="required">
                                <option value="">请选择文档类型</option>
                                <c:forEach items="${map.typeList}" var="typeVal">
                                    <c:forEach var="entry" items="${typeVal}">
                                        <option value="${entry.key }">${entry.value }</option>
                                    </c:forEach>
                                </c:forEach>
                            </select>
                            <select name="lx" id="lx" style="width:140px;" onchange="lxofmx()">
                                <option value="">请选择文档类型</option>
                            </select>
                            <select name="mx" id="mx" style="width:140px;" >
                                <option value="">请选择具体文档</option>
                            </select>
                        </div>
                    </div>
                    <div class="z-item">
                        <div class="z-tt">
                            <span>简介:</span>
                        </div>
                        <div class="z-bd">
                            <input class="z-input01" id="simple" value="${map.simple}" maxlength="100" />
                        </div>
                    </div>
                    <div class="z-item">
                        <div class="z-tt">
                            <span>详情描述:</span>
                        </div>
                        <div class="z-bd">
                            <%--<form:textarea path="describeUrl"  htmlEscape="false"  maxlength="16" class="required" style="width:700px;height:300px;"/>--%>
                            <textarea id="detail" name="detail" style="height: 130px;width: 500px;" onkeydown="javascript:if(this.value.length>300)this.value=this.value.substr(0,300);">${map.details}</textarea>
                        </div>
                    </div>
                    <div class="z-item">
                        <div class="z-tt">
                        </div>
                        <div class="z-bd">
                            <a class="btn" href="javascript:subminplat()"><i class="icon-plus"></i>确认提交</a>
                        </div>
                    </div>
                </div>
            </div>
    </div>
</div>
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
