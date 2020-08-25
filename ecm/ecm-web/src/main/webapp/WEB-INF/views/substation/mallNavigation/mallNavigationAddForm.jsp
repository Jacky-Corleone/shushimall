<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>子站导航编辑</title>
	<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<script type="text/javascript" src="${ctxStatic}/ganged.js"></script>
	<script type="text/javascript">
	$(function() {
		//加载类目地区下拉框
		if(${dto.themeType == 2}){
			onCSelect('${dto.cid}');
		}else{
			onAddressSelect('${dto.addressId}');
		}
	})
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
			
			$("#themeId").blur(function(){
				var reg = $(this).attr("reg");
				var val = $(this).val();
				var obj = new RegExp(reg);
				if(obj.test(val)){
					$(this).next("span").html("");
			  	}else{
				 	$(this).next("span").html("请选择子站");
			  	}
			})
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
		})
		
	</script>
</head>
<body>

	<form:form id="inputForm" modelAttribute="dto"  action="${ctx}/station/mallNavigation/add?themeType=${dto.themeType}" method="post" class="form-horizontal">
        <input type="hidden" name="id" id="id" value="1"/>

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
					<form:select id="themeId" path="themeId" required="true">
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
				<input type="text" value="${dto.navLink}" name="navLink" id="bannerLink" maxlength="100" required="true" reg="^(http(s)?:\/\/)?(www\.)?[\w-]+\.\w{2,4}(\/)?$">
				<span>商城导航URL地址</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">显示顺序:</label>
			<div class="controls">
				<input type="text" value="${dto.sortNum}" name="sortNum" id="sortNumber"  maxlength="2" required="true" title="不能为空且只能是数字" reg="^[0-9]*$">
			</div>
		</div>
		
	   <div class="control-group">
			<label class="control-label">启用状态:</label>
			<div class="controls">
			<select name="status" id="sortNumber">
			       <option value="0">不启用</option>
			       <option value="1">启用</option>
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