<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商城广告编辑</title>
<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <%@ include file="/WEB-INF/views/include/spectrum.jsp"%>
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
        var type=$("#adType");
        changeImgsize(type[0]);
		$("#inputForm").validate({
            rules:{
	                sortNumber:{
	                    digits:true
	                }
            	}
        });
		/* var selects = document.getElementsByName("status");
		 selects[0].checked= true;*/
		$("[name='status']").click(function(){
			var status = $(this).val();
			if(status == 0){
				$("#publishFlag").show();
			}else{
				$("#publishFlag").hide();
			}
		});
        <c:if test="${dto.adType==3}">
            $("#isClose").show();
            $("#cidDiv").hide();
        </c:if>
        <c:if test="${dto.adType==4}">
        	$("#isClose").hide();
        	$("#cidDiv").show();
        </c:if>
        <c:if test="${dto.adType==6}">
	        $("#isClose").hide();
	        $("#cidDiv").hide();
	        $("#skuDiv").show();
	        $("#imgListDiv").show();
        </c:if>
        <c:if test="${dto.adType==7}">
        	$("#clickDiv").show();
        </c:if>
        isTimeFlag();
        
        var startTime = "<fmt:formatDate value='${dto.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>";
        $('#startTime').val(startTime);
        var endTime = "<fmt:formatDate value='${dto.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>";
        $('#endTime').val(endTime);
        
	});
	
	$(function(){   
	    $("#priceColour").spectrum({
			/*是否弹框显示选择颜色*/
			/* flat: true, */
			color: "rgb(${dto.priceColour})",
			showInput: true,
			className: "full-spectrum",
			showInitial: true,
			showPalette: true,
			showSelectionPalette: true,
			maxPaletteSize: 10,
			/*控制选取的颜色格式如：#444 hex,255, 128, 0 rgb*/
			preferredFormat: "rgb",
			localStorageKey: "spectrum.example",
	        hide: function (color) {
				var value=color.toString().substring(4,color.toString().length-1);
				$("#priceColour").val(value);
	        },change: function(color) {
	        	var value=color.toString().substring(4,color.toString().length-1);
				$("#priceColour").val(value);
			},
			palette: [
			            ["#000000", "#434343", "#666666", "#999999", "#b7b7b7", "#cccccc", "#d9d9d9", "#efefef", "#f3f3f3", "#ffffff"],
					    ["#980000", "#ff0000", "#ff9900", "#ffff00", "#00ff00", "#00ffff", "#4a86e8", "#0000ff", "#9900ff", "#ff00ff"],
					    ["#e6b8af", "#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d9ead3", "#c9daf8", "#cfe2f3", "#d9d2e9", "#ead1dc"],
					    ["#dd7e6b", "#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#a4c2f4", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
					    ["#cc4125", "#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6d9eeb", "#6fa8dc", "#8e7cc3", "#c27ba0"],
					    ["#a61c00", "#cc0000", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3c78d8", "#3d85c6", "#674ea7", "#a64d79"],
					    ["#85200c", "#990000", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#1155cc", "#0b5394", "#351c75", "#741b47"],
					    ["#5b0f00", "#660000", "#783f04", "#7f6000", "#274e13", "#0c343d", "#1c4587", "#073763", "#20124d", "#4c1130"]
			        ]
		});
    	$("#priceColour").show();
	});
	
    function isTimeFlag(){
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        if(!endTime){
            $("input[name=status]:eq(0)").attr("checked",'checked');
        } else {
            $("input[name=status]:eq(1)").attr("checked",'checked');
            $("#publishFlag").show();
        }
    }
    //表单提交
    function submitForm(){
    	$('#startTime_').val($('#startTime').val());
    	$('#endTime_').val($('#endTime').val());
    	var type=$("#adType").val();
    	var addressType=$("#addressType").val();
    	if(addressType=='1'){
    		var themeId = $("#themeName").val();
    		if(themeId==null || themeId==undefined || themeId==""){
    			$('#themeNameTitle').html('必填信息');
    			$('#themeName').attr("class","required error");
    			return false;
    		}
    	}else{
    		$('#themeNameTitle').html('');
			$('#themeName').attr("class","");
    	}
    	if(type=='6'){
    		var skuId = $('#skuId').val();
    		var adSrc2 = $('#adSrc2').val();
    		var adSrc3 = $('#adSrc3').val();
    		var priceColour = $('#priceColour').val();
    		if(skuId==null || skuId==undefined || skuId==""){
    			$('#skuTitle').html('商品套装SKUID不能为空');
    			$('#skuId').attr("class","required error");
    			return false;
    		}
    		if(adSrc2==null || adSrc2==undefined || adSrc2==""){
    			$('#adSrc2Title').html('必填信息');
    			$('#adSrc2').attr("class","required error");
    			return false;
    		}
    		if(adSrc3==null || adSrc3==undefined || adSrc3==""){
    			$('#adSrc3Title').html('必填信息');
    			$('#adSrc3').attr("class","required error");
    			return false;
    		}
    		if(priceColour==null || priceColour==undefined || priceColour==""){
    			$('#priceColourTitle').html('必填信息');
    			$('#priceColour').attr("class","required error");
    			return false;
    		}
    	}
    	else{
			$('#skuTitle').html('');
			$('#skuId').attr("class","");
			$('#adSrc2Title').html('');
			$('#adSrc2').attr("class","");
			$('#adSrc3Title').html('');
			$('#adSrc3').attr("class","");
			$('#priceColourTitle').html('');
			$('#priceColour').attr("class","");
		}
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
			url: '${ctx}/fileUpload/uploadsize?size=1048576&date='+new Date(),
			secureuri: false, //是否需要安全协议，一般设置为false
			fileElementId: fileElementId, //文件上传域的ID
			dataType: 'json',
			type:"post",
			success: function (data, status){
				if(!data.success){
                    $.jBox.info(data.msg);
					return ;
				}
				$("#"+showImg).attr("src","${filePath}"+data.url);
                //图片查看
                $('#'+showImg).fancyzoom({
                    Speed: 400,
                    showoverlay: false,
                    imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
                });
				$("#"+urlInput).val(data.url);
			},
			error: function (data, status, e){
                $.jBox.info("系统繁忙请稍后再试")
			}
		});
	};
	function changeImgsize(self){
		if(self.value==1){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：291*200");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#skuDiv").hide();
            $("#housetypeDiv").hide();
            $("#descriptionDiv").hide();
            $("#remarksDiv").hide();
            $("#clickDiv").hide();
            $("#imgListDiv").hide();
            $("#lrStyle").hide();
		}
        if(self.value==2){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：500*380");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#skuDiv").hide();
            $("#housetypeDiv").hide();
            $("#descriptionDiv").hide();
            $("#remarksDiv").hide();
            $("#clickDiv").hide();
            $("#imgListDiv").hide();
            $("#lrStyle").hide();
		}
        if(self.value==3){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：1200*80");
            $("#isClose").show();
            $("#cidDiv").hide();
            $("#skuDiv").hide();
            $("#housetypeDiv").hide();
            $("#descriptionDiv").hide();
            $("#remarksDiv").hide();
            $("#clickDiv").hide();
            $("#imgListDiv").hide();
            $("#lrStyle").hide();
        }
        if(self.value==4){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：100*40");
            $("#isClose").hide();
            $("#cidDiv").show();
            $("#skuDiv").hide();
            $("#housetypeDiv").hide();
            $("#descriptionDiv").hide();
            $("#remarksDiv").hide();
            $("#clickDiv").hide();
            $("#imgListDiv").hide();
            $("#lrStyle").hide();
        }
        if(self.value==5){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：392*300");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#skuDiv").hide();
            $("#housetypeDiv").hide();
            $("#descriptionDiv").hide();
            $("#remarksDiv").hide();
            $("#clickDiv").hide();
            $("#imgListDiv").hide();
            $("#lrStyle").hide();
        }
        if(self.value==6){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：433*380");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#skuDiv").show();
            $("#housetypeDiv").hide();
            $("#descriptionDiv").hide();
            $("#remarksDiv").hide();
            $("#imgListDiv").show();
            $("#clickDiv").hide();
            $("#skuId").addClass("required");
            $("#lrStyle").show();
        }
        if(self.value==7){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：1200*100");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#skuDiv").hide();
            $("#housetypeDiv").hide();
            $("#descriptionDiv").hide();
            $("#remarksDiv").hide();
            $("#clickDiv").show();
            $("#imgListDiv").hide();
            $("#lrStyle").hide();
        }
        if(self.value==11){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：270*80");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#skuDiv").hide();
            $("#housetypeDiv").hide();
            $("#descriptionDiv").hide();
            $("#remarksDiv").hide();
            $("#clickDiv").hide();
            $("#imgListDiv").hide();
            $("#lrStyle").hide();
        }
	}
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
</head>
<body>

	<form:form id="inputForm" modelAttribute="dto" action="${ctx}/mallAdvertise/edit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="pageNo" value="${page.pageNo}">
		<input type="hidden" name="pageSize" value="${page.pageSize}">
		<input type="hidden" name="statusAdType" value="${statusDTO.adType}">
		<input type="hidden" name="statusAdTitle" value="${statusDTO.adTitle}">
		<input type="hidden" name="statusTimeFlag" value="${statusDTO.timeFlag}">
		<input type="hidden" name="statusStartTime" value="<fmt:formatDate value='${statusDTO.startTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
		<input type="hidden" name="statusEndTime" value="<fmt:formatDate value='${statusDTO.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
		<input type="hidden" name="statusStatus" value="${statusDTO.status}">
		<input type="hidden" name="addressType" id="addressType" value="${addressType}">
		<div class="control-group">
			<label class="control-label" for="adType">广告类型:</label>
			<div class="controls">
				<form:select id="adType" path="adType" class="input-small" onchange="changeImgsize(this)">
					<form:option value="1" label="主题广告"/>
					<c:if test="${addressType != 1}">
						<form:option value="2" label="登录广告"/>
					</c:if>
                    <form:option value="3" label="头部广告"/>
                    <form:option value="4" label="类目广告"/>
                    <form:option value="5" label="推荐服务"/>
                    <form:option value="6" label="推荐套装"/>
                    <form:option value="7" label="在线轻松购"/>
                    <form:option value="11" label="底部广告"/>
				</form:select>
			</div>
		</div>
        <div class="control-group hide" id="isClose">
            <label class="control-label" for="close">是否可关闭:</label>
            <div class="controls">
                <form:radiobutton path="close" value="1" checked="checked" label="可关闭"/>
                <form:radiobutton path="close" value="2" label="不可关闭"/>
            </div>
        </div>
        <div class="control-group hide" id="cidDiv">
            <label class="control-label" for="cid">类目</label>
            <div class="controls">
                <select id="cid" name="cid" class="input-small">
                    <c:forEach items="${itemList}" var="t">
                        <c:choose>
                            <c:when test="${t.categoryCid==dto.cid}">
                                <option value="${t.categoryCid}" selected >${t.categoryCName}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${t.categoryCid}">${t.categoryCName}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </div>
		<div class="control-group">
			<label class="control-label" for="sortNumber">广告序号:</label>
			<div class="controls">
				<form:input path="sortNumber" id="sortNumber" htmlEscape="false" maxlength="50" class="required" title="序号只能是整数"/>
			</div>
		</div>
		<div class="control-group hide" id = "skuDiv">
			<label class="control-label" for="skuId">商品套装skuId:</label>
			<div class="controls">
				<form:input path="skuId"  htmlEscape="false" maxlength="50"  />
				<label id="skuTitle" for="skuId" class="error"></label>
			</div>
		</div>

		<div class="control-group" id="ggtitle">
			<label class="control-label" for="title">广告标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="30" class="required"/>
			</div>
		</div>
		
        <div class="control-group hide" id="housetypeDiv">
            <label class="control-label" for="applicableType">适用户型:</label>
			<div class="controls">
				<form:input path="applicableType" htmlEscape="false" maxlength="30" />
			</div>
        </div>
        
        <div class="control-group hide" id="descriptionDiv">
            <label class="control-label" for="titleDescription">广告语:</label>
			<div class="controls">
				<form:input path="titleDescription" htmlEscape="false" maxlength="30" />
			</div>
        </div>
        
        <div class="control-group hide" id="remarksDiv">
            <label class="control-label" for="titleRemarks">备注:</label>
			<div class="controls">
				<form:input path="titleRemarks" htmlEscape="false" maxlength="30" />
			</div>
        </div>

		<div class="control-group">
			<label class="control-label" for="adURL">指向链接:</label>
			<div class="controls">
				<form:input path="adURL" htmlEscape="false" maxlength="500" class="required"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">推荐位图片：</label>
			<div class="controls">
				<%--<input type="text" class="z-input01" disabled="disabled">--%>
                <span id="showimgmsg" style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M</span>
				<span class="z-upload">
					<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
					<input type="file" id="adSrcFile" name="file" class="file-img" onchange="startUpload('adSrcFile','adSrcImg','adSrc')">
					<input id="adSrc" name="adSrc" type="hidden" value="${dto.adSrc}" required="required"/>
				</span>

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">图片预览:</label>
			<div class="controls">
	             <c:choose>
	                 <c:when test="${dto.adSrc!=null}">
	                     <img src="${filePath}${dto.adSrc}" id="adSrcImg" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
	                 </c:when>
	                 <c:otherwise>
	                     <img id="adSrcImg" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
	                 </c:otherwise>
	             </c:choose>
			</div>
		</div>
		
		<div id="imgListDiv">
			<div class="control-group">
				<label class="control-label">套装中部图片：</label>
				<div class="controls">
	                <span id="showimgmsg2" style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：300*380</span>
					<span class="z-upload">
						<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
						<input type="file" id="adSrcFile2" name="file" class="file-img" onchange="startUpload('adSrcFile2','adSrcImg2','adSrc2')">
						<input id="adSrc2" name="adMiddleSrc" type="hidden" value="${dto.adMiddleSrc}" />
					</span>
					<label id="adSrc2Title" for="adSrc2" class="error"></label>
	
				</div>
			</div>
	
			<div class="control-group">
				<label class="control-label">图片预览:</label>
				<div class="controls">
		             <c:choose>
		                 <c:when test="${dto.adMiddleSrc!=null}">
		                     <img src="${filePath}${dto.adMiddleSrc}" id="adSrcImg2" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
		                 </c:when>
		                 <c:otherwise>
		                     <img id="adSrcImg2" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
		                 </c:otherwise>
		             </c:choose>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">套装说明图片：</label>
				<div class="controls">
					<%--<input type="text" class="z-input01" disabled="disabled">--%>
	                <span id="showimgmsg3" style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：1200*380</span>
					<span class="z-upload">
						<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
						<input type="file" id="adSrcFile3" name="file" class="file-img" onchange="startUpload('adSrcFile3','adSrcImg3','adSrc3')">
						<input id="adSrc3" name="adBackSrc" type="hidden" value="${dto.adBackSrc}"/>
					</span>
					<label id="adSrc3Title" for="adSrc3" class="error"></label>
	
				</div>
			</div>
	
			<div class="control-group">
				<label class="control-label">图片预览:</label>
				<div class="controls">
		             <c:choose>
		                 <c:when test="${dto.adBackSrc!=null}">
		                     <img src="${filePath}${dto.adBackSrc}" id="adSrcImg3" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
		                 </c:when>
		                 <c:otherwise>
		                     <img id="adSrcImg3" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
		                 </c:otherwise>
		             </c:choose>
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label">价格颜色:</label>
				<div class="controls">
					<form:input path="priceColour"  htmlEscape="false" maxlength="30" type="text" class="triggerSet" />
					<label id="priceColourTitle" for="priceColour" class="error"></label>
				</div>
			</div>
		</div>
		
		<c:if test="${addressType ==1 }">
			<div class="control-group " >
				<label class="control-label">所属子站:</label>
				<div class="controls">
					<form:input type="hidden" path="themeId"/>
					<input type="text" id="themeName" readonly value="${themeDTO.themeName }"/>
					<a class="btn" id="modal-686826" role="button" href="#addthemeDiv" onclick="redioBotton();" data-toggle="modal">选择地区子站</a>
					<label id="themeNameTitle" for="themeName" class="error"></label>
				</div>
	        </div>
		</c:if>

        <div class="control-group hide"  id="clickDiv">
			<label class="control-label" >图片是否可点击:</label>
			<div class="controls">
				<form:radiobutton path="isClick" name="isClick" value="1" checked="checked" label="是"/>
				<form:radiobutton path="isClick" name="isClick" value="0"  label="否"/>
			</div>
		</div>
		<div class="control-group hide"  id="lrStyle">
			<label class="control-label" >广告布局:</label>
			<div class="controls">
				<form:radiobutton path="lrStyle" checked="checked" value="1"  label="套装说明在左"/>
				<form:radiobutton path="lrStyle" value="2"  label="套装说明在右"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="publishFlag"></label>
			<div class="controls">
				<form:radiobutton path="status" name="status" value="1" label="即时发布"/>
				<form:radiobutton path="status" name="status" value="0" label="定时发布"/>
			</div>
		</div>

		<div id="publishFlag" class="control-group" style="display: none;">
			<label class="control-label" for=publishFlag>定时发布时间:</label>
			<div class="controls">
				<form:input path="startTime" id="startTime" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<input id="startTime_" name="startTime_" type="hidden" />
			</div>
            <label class="control-label" for=publishFlag>定时结束时间:</label>
            <div class="controls">
                <form:input path="endTime" id="endTime"  class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
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
    
	
</body>
</html>