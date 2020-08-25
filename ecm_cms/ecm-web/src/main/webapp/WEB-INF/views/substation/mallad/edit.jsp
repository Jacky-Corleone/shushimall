<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商城广告编辑</title>
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
<script type="text/javascript" src="${ctxStatic}/ganged.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#cid").text('a')
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
		//加载类目地区下拉框
		
/*		 var selects = document.getElementsByName("publishFlag");
		 selects[0].checked= true;*/
		 //定时和即时切换事件
		/* $("[name='publishFlag']").click(function(){
			var publishFlag = $(this).val();
			if(publishFlag == 1){
				$("#publishFlag").show();
			}else{
				$("#publishFlag").hide();
			}
		}); */
        <c:if test="${dto.adType==3}">
            $("#isClose").show();
            $("#cidDiv").hide();
        </c:if>
        <c:if test="${dto.adType==4}">
        $("#isClose").hide();
        $("#cidDiv").show();
        </c:if>
        isTimeFlag();
        
        //根据广告类型，判断是否显示店铺编号和商品编号
		if(type[0].value==1 || '${dto.adType}' == '1' || '${dto.adType}' == '5'){
			
			if('${dto.adType}' == '1' || type[0].value==1){
				$("#skuIdLabel").text($("#skuIdLabel").text().replace('商品','店铺'));
			}else{
				$("#skuIdLabel").text($("#skuIdLabel").text().replace('店铺','商品'));
			}
			
			$("#skuIdLabel").parent().show();
			$("#skuId").addClass("required");
		} else{
			$("#skuIdLabel").parent().hide();
			$("#skuId").removeClass("required");
		}
        
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
				if(${oss_ftp_change == 0}){
					$("#"+showImg).attr("src","${filePath}"+data.url+"@250w_100h_2e_100sh");
				}else{
					$("#"+showImg).attr("src","${filePath}"+data.url);
				}
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
			$("#skuIdLabel").parent().show();
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：266*200");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#hotSell").hide();
            $("#cid").attr("required",false);
		}
        if(self.value==2){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：500*380");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#hotSell").hide();
            $("#cid").attr("required",false);
		}
        if(self.value==3){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：1200*250或1600*250");
            $("#isClose").show();
            $("#cidDiv").hide();
            $("#hotSell").hide();
            $("#cid").attr("required",false);
        }
        if(self.value==4){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：168*98");
            $("#isClose").hide();
            $("#cidDiv").show();
            $("#cid").attr("required",true);
            $("#hotSell").hide();
        }
        if(self.value==5){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：190*190");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#hotSell").show();
            $("#cid").attr("required",false);
        }
        if(self.value==6){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：280*80");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#hotSell").show();
            $("#cid").attr("required",false);
        }if(self.value==10){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：100*50");
            $("#isClose").hide();
            $("#cidDiv").hide();
            $("#hotSell").show();
            $("#cid").attr("required",false);
        }
        var themeId=$("#themeId").val();
		var adType=$("#adType").val();
		findThreeCate(themeId,adType);
		if(adType!=4){
			$("#cid").val("");
		}
        /* if(self.value==6){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：220*80");
            $("#isClose").hide();
            $("#cidDiv").hide();
        } */
	}
	function changeInputWithAdType(select) {
		switch(select.value) {
		case "1":
			$("#skuIdLabel").text($("#skuIdLabel").text().replace('商品','店铺'));
			break;
		case "5":
			$("#skuIdLabel").text($("#skuIdLabel").text().replace('店铺','商品'));
			break;
		default:
			break;
		}
		if(select.value == 1 || select.value == 5){
			$("#skuIdLabel").parent().show();
			$("#skuId").addClass("required");
		} else{
			$("#skuIdLabel").parent().hide();
			$("#skuId").removeClass("required");
		}
	}
	function sellImgSize(sell){
		if(sell.value==1){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：237*215");
        }
		if(sell.value==2){
            $("#showimgmsg").text("仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M，建议图片尺寸：241*430");
        }
	}
	function changeCateGory(obj){
		var themeId=$(obj).val();
		var adType=$("#adType").val();
		findThreeCate(themeId,adType);
	}
	function findThreeCate(themeId,adType){
		if(adType==4){
			var html="<option value=''>请选择三级类目</option>";
			var url="${ctx}/station/mallAdvertise/findThreeCate?themId="+themeId;
			 $.ajax({
	            url:url,
	            type:"post",
	            dataType:'json',
	            success:function(data){
	           	 $("#cid").html("");
	           	 if(data.success){
	           		 $(data.obj).each(function(i,item){
	                        html += "<option ";
	                    	if(item.categoryCid=='${dto.cid}'){
	                        	html +=" selected";
	                        }
	                    	html +=" value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
	                   });
	           		 $("#cid").html(html);
	           		$("#cid").select2();
	           	 }
	            }
	        });
		}
	}
</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="dto" action="${ctx}/station/mallAdvertise/edit?themeType=${dto.advType}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<div class="control-group">
			<label class="control-label" for="adType">广告类型:</label>
			<div class="controls">
				<form:select id="adType" path="adType" class="input-small" onchange="changeImgsize(this);changeInputWithAdType(this);">
					<form:option value="1" label="旗舰店"/>
                    <form:option value="3" label="底部广告"/>
                    <form:option value="4" label="类目广告"/>
                    <form:option value="6" label="本周推荐"/>
                    <form:option value="5" label="精品•推荐"/>
                    <form:option value="10" label="旗舰店底部广告位"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">频道:</label>
			<div class="controls">
				<label class="lbl">
					<form:select id="themeId" path="themeId" required="true" readonly="readonly" onchange="changeCateGory(this)">
						<form:option value="" label="请选择频道"></form:option>
						<form:options items="${themeList}"  itemLabel="themeName"  itemValue="id" />
					</form:select>
				</label>
			</div>
		</div>
		 <div class="control-group hide" id="cidDiv">
            <label class="control-label" for="cid">三级类目:</label>
            <div class="controls">
                <select id="cid" name="cid" class="input-small" >
                     <option value="">请选择三级类目</option>
                </select>
            </div>
        </div>
		<div class="control-group">
			<label class="control-label" for="sortNumber">广告序号:</label>
			<div class="controls">
				<form:input path="sortNumber" id="sortNumber" htmlEscape="false" maxlength="50" class="required" title="序号只能是整数"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="skuId" id="skuIdLabel">店铺编号:</label>
			<div class="controls">
				<form:input path="skuId" id="skuId" htmlEscape="false" maxlength="50" class="required" title="编号只能是整数"/>
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
				<form:input path="adURL" htmlEscape="false" maxlength="500" class="required"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">推荐位图片：</label>
			<div class="controls">
				<%--<input type="text" class="z-input01" disabled="disabled">--%>
                <span id="showimgmsg" style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M</span>
				<span class="z-upload" style="height:60px;">
					<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
					<input type="file" id="adSrcFile" name="file" class="file-img" onchange="startUpload('adSrcFile','adSrcImg','adSrc')">
					<input id="adSrc" name="adSrc" type="hidden" value="${dto.adSrc}" required="true"/>
				</span>

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">图片预览:</label>
			<div class="controls">
	             <c:choose>
	                 <c:when test="${dto.adSrc!=null}">
						 <c:choose>
							 <c:when test="${oss_ftp_change == 0}">
								 <img src="${filePath}${dto.adSrc}@250w_100h_2e_100sh" id="adSrcImg" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
							 </c:when>
							 <c:otherwise>
								 <img src="${filePath}${dto.adSrc}" id="adSrcImg" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
							 </c:otherwise>
						 </c:choose>
	                 </c:when>
	                 <c:otherwise>
	                     <img id="adSrcImg" alt="商城广告" width="250" height="100" class="mar_lr10 fl showimg" >
	                 </c:otherwise>
	             </c:choose>
			</div>
		</div>

		<div class="control-group" style="display: none">
			<label class="control-label" for="publishFlag"></label>
			<div class="controls">
				<form:radiobutton path="publishFlag" value="1" label="即时发布"/>
			</div>
		</div>

		<div id="publishFlag" class="control-group" style="display: none;">
			<label class="control-label" for=publishFlag>定时发布时间:</label>
			<div class="controls">
				<form:input path="startTime" id="startTime" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
			</div>
            <label class="control-label" for=publishFlag>定时结束时间:</label>
            <div class="controls">
                <form:input path="endTime" id="endTime"  class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
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