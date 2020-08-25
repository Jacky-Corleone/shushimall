<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>热门关键字新增</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@include file="/WEB-INF/views/include/dialog.jsp"%>
<jsp:useBean id="command"  class="com.camelot.sellercenter.mallSubTab.dto.MallSubTabDTO" scope="request" ></jsp:useBean>
<script type="text/javascript" src="${ctxStatic}/ganged.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	//加载类目地区下拉框
	if(${wordDTO.themeType == 2}){
		onCSelect('${wordDTO.cid}');
	}else{
		onAddressSelect('${wordDTO.addressId}');
	}
})

//保存事件
function btnSubmit(){
	var themeId=$("#themeId").val();
	var word = $("#word").val();
	if(themeId!=null && themeId.trim().length>0){
	}else{
		$.jBox.info('请选择主题！');
		return false;
    }
	if(word!=null && word.trim().length>0){
	}else{
		$.jBox.info('请填关键词！');
		return false;
    }
	$("#inputForm").submit();
		
}
function backList(){
	window.location.href="${ctx}/station/mallKeyWords/list?themeType=${wordDTO.themeType }";
}
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="wordDTO" action="${ctx}/station/mallKeyWords/add" method="post" class="form-horizontal">
		<input type="hidden" name="themeType" value="${wordDTO.themeType }" />
		
		<c:if test="${wordDTO.themeType == 2 }">
			<div class="control-group">
				<label class="control-label">类目:</label>
				<div class="controls">
					<label class="lbl" id="control_label">
						<form:select path="cid" required="true" onchange="onThemeSelect(this.value,'${wordDTO.addressId }','${wordDTO.themeId }','${wordDTO.themeType }')">
							<form:option value="" label="选择类目"></form:option>
						</form:select>
					</label>
				</div>
			</div>
		</c:if>
		<c:if test="${wordDTO.themeType == 3 }">
			<div class="control-group">
				<label class="control-label">地区:</label>
				<div class="controls">
					<label class="lbl" id="address_label">
						<form:select path="addressId" onchange="onThemeSelect('${ wordDTO.cid}',this.value,'${wordDTO.themeId }','${wordDTO.themeType }')">
							<form:option value="" label="请选择地区"></form:option>
						</form:select>
					</label>
				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">子站:</label>
			<div class="controls">
				<form:select id="themeId" path="themeId">
					<form:option value="" label="请选择"/>
					<form:options items="${themeList}"  itemLabel="themeName"  itemValue="id" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">关键词:</label>
			<div class="controls">
				<input type="text" name="word" id="word" maxlength="30"  size="50" class="required" title="热门关键字"/>
			</div>
		</div>
		<div class="control-group">
            <div class="controls">
               <button type="button" class="btn btn-primary btn-xs" onclick="btnSubmit()">保存</button>&nbsp;
               <input id="btnCancel" class="btn" type="button" value="返 回" onclick="backList()"/>
            </div>
		</div>
	</form:form>
</body>
</html>