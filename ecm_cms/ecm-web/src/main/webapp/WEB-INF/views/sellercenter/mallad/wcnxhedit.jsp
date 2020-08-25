<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我猜你喜欢</title>
<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
        //图片查看
        $('.showimg').fancyzoom({
            Speed: 400,
            showoverlay: false,
            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
        });

		/*$("#inputForm").validate({
            rules:{
                sortNumber:{
                    digits:true
                }
            }
        });*/
/*		 var selects = document.getElementsByName("publishFlag");
		 selects[0].checked= true; */
		$("[name='publishFlag']").click(function(){
			var publishFlag = $(this).val();
			if(publishFlag == 1){
				$("#publishFlag").show();
			}else{
				$("#publishFlag").hide();
			}
		});
        isTimeFlag();
	});
    function isTimeFlag(){
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        if(!endTime){
            $("input[name=publishFlag]:eq(0)").attr("checked",'checked');
        } else {
            $("input[name=publishFlag]:eq(1)").attr("checked",'checked');
            $("#publishFlag").show();
        }
    }
	function openFileChose(){
		$("#fileUpload").trigger("click");
	}
    function fileChange(){
        $("#fileUpload").click();
        $("#fileUpload").change(function(){
            uploadFile();
        });
    }
    function uploadFile(){
        //判断图片格式
        var fileInput = $("#fileUpload").val();
        var extStart = fileInput.lastIndexOf(".");
        var ext = fileInput.substring(extStart,fileInput.length).toUpperCase();
        if(ext!=".JPG" && ext!=".jpg" && ext!=".BMP"&&ext!=".bmp"&& ext!=".PNG"&&ext!=".PNG"&& ext!=".JPEG" && ext!=".jpeg"&&ext!=".gif"&&ext!=".GIF"){
            $.jBox.info("图片限于JPG,BMP,PNG,JPEG格式");
            return false;
        }
    	$.ajaxFileUpload({
            url: '${ctx}/fileUpload/uploadsize?size=1048576&date='+new Date(),
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'fileUpload', //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json
            type:"post",
            success: function (data, status){  //服务器成功响应处理函数
                if(data.success){
                    $("[name='adSrc']").val(data.url);
                    $("#preview").attr("src","${filePath}"+data.url);
                }else{
                    $.jBox.info(data.msg);
                }
            },
            error: function (data, status, e){//服务器响应失败处理函数
                $.jBox.info("系统繁忙，上传失败，请稍后再试");
            }
        });
    }
	function changeImgsize(self){
		if(self.value==1){
            $("#isClose").hide();
            $("#cidDiv").hide();
		}
        if(self.value==2){
            $("#isClose").hide();
            $("#cidDiv").hide();
		}
        if(self.value==3){
            $("#isClose").show();
            $("#cidDiv").hide();
        }
        if(self.value==4){
            $("#isClose").hide();
            $("#cidDiv").show();
        }
	}
</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="dto" action="${ctx}/mallAdvertise/editwc" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="pageNo" value="${page.pageNo}">
		<input type="hidden" name="pageSize" value="${page.pageSize}">
		<input type="hidden" name="statusAdType" value="${statusDTO.adType}">
		<input type="hidden" name="statusAdTitle" value="${statusDTO.adTitle}">
		<input type="hidden" name="statusTimeFlag" value="${statusDTO.timeFlag}">
		<input type="hidden" name="statusStartTime" value="<fmt:formatDate value='${statusDTO.startTime}' pattern='yyyy-MM-dd'/>">
		<input type="hidden" name="statusEndTime" value="<fmt:formatDate value='${statusDTO.endTime}' pattern='yyyy-MM-dd'/>">
		<input type="hidden" name="statusStatus" value="${statusDTO.status}">
		<div class="control-group">
			<label class="control-label" for="adType">广告类型:</label>
			<div class="controls">
				<form:select id="adType" path="adType" class="input-small" onchange="changeImgsize(this)">
					<form:option value="5" label="热销"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sortNumber">广告序号:</label>
			<div class="controls">
				<form:input path="sortNumber" id="sortNumber" htmlEscape="false" maxlength="50" class="required" title="序号只能是整数"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="title">广告标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="30" class="required"/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label" for="adURL">指向链接:</label>
			<div class="controls">
				<form:input path="adURL" htmlEscape="false" maxlength="128" class="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="adSrc">推荐位图片:</label>
			<div class="controls">
				<form:input type="hidden"  path="adSrc"   htmlEscape="false" maxlength="256" class="required" readonly="true"/>
				<%--<a id="fileExplore" class="btn" href="javascript:fileChange();" style="margin-left: -7px;">
					<i class="icon-search"></i>
				</a>--%>
                <span style="color: red;display: block;" id="imgSize2">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：130*130</span>
				<input id="fileUpload" name="file" type="file" class="file-img" onchange="uploadFile()">
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label"></label>
            <div class="controls">
                <c:if test="${dto.adSrc!=null}">
				    <img class="showimg" id="preview"  src="${filePath}${dto.adSrc}" style="width: 432px; height: 200px; border: 1px solid #f9f9f9;">
                </c:if>
                <c:if test="${dto.adSrc==null}">
                    <img class="showimg" id="preview"  src="" style="width: 432px; height: 200px; border: 1px solid #f9f9f9;">
                </c:if>
            </div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="publishFlag"></label>
			<div class="controls">
				<form:radiobutton path="publishFlag" value="0" label="即时发布"/>
				<form:radiobutton path="publishFlag" value="1" label="定时发布"/>
			</div>
		</div>
		
		<div id="publishFlag" class="control-group" style="display: none;">
			<label class="control-label" for=publishFlag>定时发布时间:</label>
			<div class="controls">
				<form:input path="startTime" id="startTime" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
			</div>
            <label class="control-label" for=publishFlag>定时结束时间:</label>
            <div class="controls">
                <form:input path="endTime" id="endTime" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
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