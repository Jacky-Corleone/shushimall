<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>楼层广告添加</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	$(document).ready(function() {
		$("#inputForm").validate({
            rules:{
                sortNum:{
                    digits:true
                }
            }
        });
		 var selects = document.getElementsByName("timeFlag"); 
		 selects[0].checked= true; 
		$("[name='timeFlag']").click(function(){
			var timeFlag = $(this).val();
			if(timeFlag == 1){
				$("#startTime").show();
			}else{
				$("#startTime").hide();
			}
		});
	});
</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="dto" action="${ctx}/mallAdWords/edit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">楼层名称:</label>
			<div class="controls">
				<form:select path="recId">
					<form:options items="${recs}" itemLabel="titleDTO" itemValue="idDTO"/>
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">推荐位类型:</label>
			<div class="controls">
				<form:select path="recType">
					<form:option value="3" label="顶部广告词"/>
					<form:option value="4" label="左侧广告词"/>
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
			<label class="control-label">广告词:</label>
			<div class="controls">
				<form:input path="adWords" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">指向链接:</label>
			<div class="controls">
				<form:input path="url" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label"></label>
			<div class="controls">
				<form:radiobutton path="timeFlag" value="0" label="即时发布"/>
				<form:radiobutton path="timeFlag" value="1" label="定时发布"/>
			</div>
		</div>
		
		<div id="startTime" class="control-group" style="display: none;">
			<label class="control-label">发布时间</label>
			<div class="controls">
				<form:input path="startTime" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
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