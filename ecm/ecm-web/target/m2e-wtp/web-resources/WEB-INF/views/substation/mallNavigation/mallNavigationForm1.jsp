<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
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
<script type="text/javascript" src="${ctxStatic}/ganged.js"></script>
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
			$("#inputForm").submit(function(){
				var reg = $("#sortNum").attr("reg");
				var val = $("#sortNum").val();
				var obj = new RegExp(reg);
				if(obj.test(val)){
					$("#sortNum").next("span").html("");
				}else{
					$("#sortNum").next("span").html("<a style='color:#FF0000;'>顺序只能是数字</a>");
					return false;
				}
			})
		});
		$(document).ready(function() {
			//加载类目地区下拉框
			if(${dto.themeType == 2}){
				alert(1);
				onCSelect('${dto.cid}');
			}else{
				alert(2);
				onAddressSelect('${dto.addressId}');
			}
		})
	</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="dto"  action="${ctx}/station/mallNavigation/update?themeType=${dto.themeType}" method="post" class="form-horizontal">
        <input type="hidden" name="id" id="id" value="${dto.id}"/>

		<c:if test="${dto.themeType == 2 }">
			<div class="control-group">
				<label class="control-label">类目:</label>
				<div class="controls">
					<label class="lbl" id="control_label">
						<form:select path="cid" required="true" onchange="onThemeSelect(this.value,'${dto.addressId }','${dto.themeId }','${dto.themeType }')">
							<form:option value="" label="选择类目"></form:option>
						</form:select>
					</label>
				</div>
			</div>
		</c:if>
		<c:if test="${dto.themeType == 3 }">
			<div class="control-group">
				<label class="control-label">地区:</label>
				<div class="controls">
					<label class="lbl" id="address_label">
						<form:select path="addressId" onchange="onThemeSelect('${dto.cid }',this.value,'${dto.themeId }','${dto.themeType }')">
							<form:option value="" label="请选择地区"></form:option>
						</form:select>
					</label>
				</div>
			</div>
		</c:if>
		
		<div class="control-group">
			<label class="control-label">子站:</label>
			<div class="controls">
				<label class="lbl">
					<form:select id="themeId" path="themeId">
						<form:option value="" label="请选择子站"></form:option>
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
				<span>商城导航显示顺序只能为数字</span>
			</div>
		</div>
		
	   <div class="control-group">
			<label class="control-label">启用状态:</label>
			<div class="controls">
			<select name="status">
				 <option value="1" <c:if test="${dto.status=='1'}">selected</c:if>  >启用</option>
				 <option value="0" <c:if test="${dto.status=='0'}">selected</c:if> >未启用</option>
			</select>
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