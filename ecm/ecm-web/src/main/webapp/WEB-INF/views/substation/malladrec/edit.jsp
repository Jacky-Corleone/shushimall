<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>楼层推荐位添加</title>
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
		//楼层名称  下来   加载页面  默认选中“请选择楼层”
// 		$("#s2id_recId>a>span").text("请选择楼层");
		 var c1 = $("#themeId").val();
		 var c2 = $("#recId").val();
		 if(c1 == ""||c2 == ""){
			 var html = "<option value=''>请选择楼层</option>";
			 $("#recId").html(html);
		 }
        //图片查看
        $('.showimg').fancyzoom({
            Speed: 400,
            showoverlay: false,
            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
        });

		 var selects = document.getElementsByName("status");
		 selects[0].checked= true;
		$("#inputForm").validate({
            rules:{
                recId:"required",
                recType:"required",
                itemId:{
                    required:true,
                    digits:true
                },
                title:"required",
                url:"required",
                picSrc:"required",
                sortNum:{
                    required:true,
                    digits:true
                }
            }
        });
		$("[name='status']").click(function(){
			var timeFlag = $(this).val();
			if(timeFlag == 0){
				$("#startTime1").show();
			}else{
				$("#startTime1").hide();
			}
		});
        isTimeFlag();
        $("#btnSubmit").click(function(){
            if($("#inputForm").valid()){
                $("#inputForm").submit();
            }
        });
        var startTime = "<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>";
        $('#startTime').val(startTime);
        var endTime = "<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>";
        $('#endTime').val(endTime);
	});
    function isTimeFlag(){
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        if(!endTime){
            $("input[name=status]:eq(0)").attr("checked",'checked');
        } else {
            $("input[name=status]:eq(1)").attr("checked",'checked');
            $("#startTime1").show();
        }
    }
  //表单提交
    function submitForm(){
    	$('#startTime_').val($('#startTime').val());
    	$('#endTime_').val($('#endTime').val());
    	$('#inputForm').submit();
    }
	//工具方法：上传图片
	function startUpload(fileElementId, showImg, urlInput){
		var fileInput = $("#"+fileElementId).val();
		var extStart = fileInput.lastIndexOf(".");
		var ext = fileInput.substring(extStart,fileInput.length).toUpperCase();
		if(ext!=".JPG" && ext!=".JPEG" && ext!=".PNG" && ext!=".BMP"){
			alert("图片限于JPG,JPEG,PNG,BMP格式");
			return false;
		}
		$.ajaxFileUpload({
            url: '${ctx}/fileUpload/uploadsize?size=1048576&date='+new Date(), //用于文件上传的服务器端请求地址
			secureuri: false, //是否需要安全协议，一般设置为false
			fileElementId: fileElementId, //文件上传域的ID
			dataType: 'json',
			type:"post",
			success: function (data, status){
				if(!data.success){
                    $.jBox.info(data.msg);
				}else{
                    $("#"+showImg).attr("src","${filePath}"+data.url);
                    $("#"+urlInput).val(data.url);
                }
				$('.showimg').fancyzoom({
		            Speed: 400,
		            showoverlay: false,
		            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
		        });
			},
			error: function (data, status, e){
                $.jBox.error("系统繁忙，请稍后再试");
			}
		});
	};

	function changeImgsize(self){
		if(self.value==1){
            $("#imgspan").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：230*173");
		}else if(self.value==2){
            $("#imgspan").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：210*639");
		}else if(self.value==3){
            $("#imgspan").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：400*80");
        }else{
            $("#imgspan").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：100*40");
        }
	}
	//根据选择的频道，查询楼层
	function findThemeFool(thiz){
		 var c1 = $("#themeId").val();
		 if(c1 == ""){
			 var html = "<option value=''>请选择楼层</option>";
			 $("#s2id_recId>a>span").text("请选择楼层");
			 $("#recId").html(html);
			 return;
		 }
		$("#s2id_recId>a>span").text("请选择楼层");
		$.ajax({
      		url : "${ctx}/station/mallAdRec/mallReclist",
      		type : "POST",
      		data :"themeId="+thiz.value,// 你的formid
      		dataType : "json",
      		success : function(obj) {
      			$("#recId").empty();
      			$("#recId").append("<option value='' selected='selected'>请选择楼层</option>");
      			for(var i =0 ; i < obj.length ; i++){
      				$("#recId").append("<option value='"+obj[i].idDTO+"'>"+obj[i].titleDTO+"</option>");
      			}
      		},
      		error : function(xmlHttpRequest, textStatus, errorThrown) {
      			$.jBox.info("系统错误！请稍后再试！");
      		}
      	});
	}
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="dto" action="${ctx}/station/mallAdRec/edit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="pageNo" value="${page.pageNo}">
		<input type="hidden" name="pageSize" value="${page.pageSize}">
		<input type="hidden" name="statusRecId" value="${statusDTO.recId}">
		<input type="hidden" name="statusRecType" value="${statusDTO.recType}">
		<input type="hidden" name="statusTitle" value="${statusDTO.title}">
		<input type="hidden" name="statusStatus" value="${statusDTO.status}">
		<input type="hidden" name="statusTimeFlag" value="${statusDTO.timeFlag}">
		<input type="hidden" name="statusStartTime" value="<fmt:formatDate value='${statusDTO.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
		<input type="hidden" name="statusEndTime" value="<fmt:formatDate value='${statusDTO.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">频道名称:</label>
			<div class="controls">
				<form:select path="themeId"  onchange="findThemeFool(this)" id="themeId"  class="required" >
				<form:option value="" label="请选择频道"></form:option>
					<form:options items="${themes}" itemLabel="themeName" itemValue="id"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">楼层名称:</label>
			<div class="controls">
				<form:select path="recId" id="recId">
					<form:option value="" label="请选择楼层"></form:option>
					<form:options items="${recs}" itemLabel="titleDTO" itemValue="idDTO"/>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">推荐位类型:</label>
			<div class="controls">
				<form:select path="recType"  onchange="changeImgsize(this)">
					<form:option value="1" label="推荐商品"/>
					<%-- <form:option value="2" label="推荐活动"/>
                    <form:option value="3" label="顶部广告"/>
                    <form:option value="4" label="底部广告"/> --%>
                </form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">序号:</label>
			<div class="controls">
				<form:input path="sortNum" id="sortNum" htmlEscape="false" maxlength="50" title="序号只能是整数"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">商品编号:</label>
			<div class="controls">
				<form:input path="itemId" htmlEscape="false" maxlength="50"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">推荐主题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="50"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">指向链接:</label>
			<div class="controls">
				<form:input path="url" htmlEscape="false" maxlength="500"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">推荐位图片：</label>
			<div class="controls">
				<%--<input type="text" class="z-input01" disabled="disabled">--%>
                <span id="imgspan" style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：230*220</span>
				<span class="z-upload">
					<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
					<input type="file" id="picSrcFile" name="file" class="file-img" onchange="startUpload('picSrcFile','picSrcImg','picSrc')">
				</span>
                <input id="picSrc" name="picSrc" type="hidden" value="${dto.picSrc}" required="true" title="请上传图片"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">图片预览:</label>
			<div class="controls">
	             <c:choose>
	                 <c:when test="${dto.picSrc!=null}">
	                     <img src="${filePath}${dto.picSrc}" id="picSrcImg" alt="楼层推荐位" width="250" height="100" class="mar_lr10 fl showimg" >
	                 </c:when>
	                 <c:otherwise>
	                     <img id="picSrcImg" alt="楼层推荐位" width="250" height="100" class="mar_lr10 fl showimg" >
	                 </c:otherwise>
	             </c:choose>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label"></label>
			<div class="controls">
				<form:radiobutton path="status" id="status"  value="1" label="即时发布"/>
				<form:radiobutton path="status" id="status" value="0" label="定时发布"/>
			</div>
		</div>

		<div id="startTime1" class="control-group" style="display: none;">
			<label class="control-label">定时发布时间</label>
			<div class="controls">
				<form:input path="startTime" id="startTime" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<input id="startTime_" name="startTime_" type="hidden" />
			</div>
            <label class="control-label" for=endTime>定时结束时间:</label>
            <div class="controls">
                <form:input path="endTime" id="endTime" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                 <input id="endTime_" name="endTime_" type="hidden" />
            </div>
		</div>

		<div class="control-group">
            <div class="controls">
                <input id="btnSubmit" class="btn" type="button" onclick="submitForm()" value="保 存"/>&nbsp;
                <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
		</div>
	</form:form>
</body>
</html>