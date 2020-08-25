<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<html>
<head>
	<title>子站导航编辑</title>
	<meta name="decorator" content="default"/>
<style type="text/css">
/*图片上传按钮样式*/
.z-upload{overflow:hidden; width:100px;height:30px;line-height:30px;color:#fff;position:relative;display:block;}
.z-upload .file-img{font-size:40px;width:100px;height:30px;filter:alpha(opacity=0);opacity:0;position:absolute;left:0px;top:0px;}
.c-upload{position:absolute;left:0;bottom:0px;overflow:hidden;height:20px;line-height:20px;color:#fff;width:100%;background:rgba(0, 0, 0, .5);z-index:999;}
.c-upload .file-img{width:80px;height:50px;filter:alpha(opacity=0);opacity:0;z-index:999;text-align:center;}
.c-upload span{width:80px;height:50px; padding-left:15px;}
</style>
	<script type="text/javascript">
		function isTimeFlag(){
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if(startTime=="" && endTime==""){
				$("input[name=timeFlag]:eq(0)").attr("checked",'checked');
			} else {
				$("input[name=timeFlag]:eq(1)").attr("checked",'checked');
			}
		}
		function isTimeDiv(){
			var timeFlag = $("input[name='timeFlag']:checked").val();
			if(timeFlag=="0"){
				$("#startTimeDiv").show();
				 $("#endTimeDiv").show();
			}else{
				$("#startTimeDiv").hide();
				$("#endTimeDiv").hide();
			}
		}
		$(function(){
			$("#status").blur(function(){
			  var status = $(this).val();
			  if(status==1 ||status==2){
					 $(this).next("span").html("");
			  }else{
				 $(this).next("span").html("请正确书写状态");
			  }
			})
	    	$("#navLink").blur(function(){
				var reg = $(this).attr("reg");
				var val = $(this).val();
				var obj = new RegExp(reg);
				if(obj.test(val)){
					$("#navLink").next("span").html("");
				}else{
					$("#navLink").next("span").html("请正确书写URL");
				}
			})
				$("#sortNum").blur(function(){
				var reg = $(this).attr("reg");
				var val = $(this).val();
				var obj = new RegExp(reg);
				if(obj.test(val)){
					$("#sortNum").next("span").html("");
				}else{
					$("#sortNum").next("span").html("顺序只能是数字");
				}
			})
		});

	</script>
</head>
<body>
<input type="text" value="${dto.navTitle}" name="navTitle" id="title" maxlength="30" >
	<form:form id="inputForm" modelAttribute="dto"  action="${ctx}/station/mallNavigation/update?themeType=${dto.themeType}" method="post" class="form-horizontal">
        <input type="hidden" name="id" id="id" value="${dto.id}"/>

		<div class="control-group">
			<label class="control-label">子站:</label>
			<div class="controls">
				<label class="lbl">
					<form:select id="themeId" path="themeId">
						<form:options items="${themeList}"  itemLabel="themeName"  itemValue="id" />
					</form:select>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">子站导航名称:</label>
			<div class="controls">
				<input type="text" value="${dto.navTitle}" name="navTitle" id="title" maxlength="8" required="true">
				<span>限8个字符以内</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">指向链接:</label>
			<div class="controls">
				<input type="text" value="${dto.navLink}" name="navLink" id="navLink" maxlength="100" required="true" reg="^(http(s)?:\/\/)?(www\.)?[\w-]+\.\w{2,4}(\/)?$">
				<span>商城导航URL地址</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">显示顺序:</label>
			<div class="controls">
				<input type="text" value="${dto.sortNum}" name="sortNum" id="sortNum" maxlength="2" required="true" title="不能为空且只能是数字" reg="^[0-9]*$">
			</div>
		</div>
		
	   <div class="control-group">
			<label class="control-label">启用状态:</label>
			<div class="controls">
				<input type="text" value="${dto.status}" name="status" id="status" maxlength="2" required="true" title="不能为空且只能是数字">
			    <span>1是启用2是停用</span>
			</div>
		</div>
		
		<div class="control-group" id="startTimeDiv">
			<label class="control-label">发布开始时间：</label>
			<div class="controls">
				<input id="startTime" name="created" value="${dto.created}" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				 onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
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