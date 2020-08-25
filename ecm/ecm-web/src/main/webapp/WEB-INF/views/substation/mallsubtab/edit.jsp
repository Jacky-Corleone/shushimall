<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>页签编辑</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@include file="/WEB-INF/views/include/dialog.jsp"%>
<jsp:useBean id="command"  class="com.camelot.sellercenter.mallSubTab.dto.MallSubTabDTO" scope="request" ></jsp:useBean>
<script type="text/javascript" src="${ctxStatic}/ganged.js"></script>
<script type="text/javascript">
$(function(){
	$(".template").hide();
	$(".item1").show();
	$.fn.numeral=function(bl){//限制金额输入、兼容浏览器、屏蔽粘贴拖拽等
	      $(this).keypress(function(e){
	          var keyCode=e.keyCode?e.keyCode:e.which;
	        if(bl){//浮点数
	          if((this.value.length==0 || this.value.indexOf(".")!=-1) && keyCode==46) return false;
	          return keyCode>=48&&keyCode<=57||keyCode==46||keyCode==8;
	        }else{//整数
	          return  keyCode>=48&&keyCode<=57||keyCode==8;
	        }
	      });
	      $(this).bind("copy cut paste", function (e) { // 通过空格连续添加复制、剪切、粘贴事件
	          if (window.clipboardData)//clipboardData.setData('text', clipboardData.getData('text').replace(/\D/g, ''));
	              return !clipboardData.getData('text').match(/\D/);
	          else
	              event.preventDefault();
	       });
	      $(this).bind("dragenter",function(){return false;});
	      $(this).css("ime-mode","disabled");
	      $(this).bind("focus", function() {  
	        if (this.value.lastIndexOf(".") == (this.value.length - 1)) {  
	            this.value = this.value.substr(0, this.value.length - 1);
	        } else if (isNaN(this.value)) {  
	            this.value = "";  
	        }  
	    });  
	}

	//$("#inputForm").validate();
});

function checkLimit(event){
	$("#sortNums").numeral(false);//限制只能输入整数
}

function showtem(v){
	$(".template").hide();
	$(".item"+v).show();
}


//保存事件
function btnSubmit(){
	var title=$("#title").val();
	var redId=$("#redId").val();
	var id = $("#id").val();//页签id
	var sortNum=$("#sortNum").val();//序号
	if(!$("#inputForm").valid()){
		return false;
	}
	var templateId=$("#templateId").val();//模板号
	var status=$("#status:checked").val();//状态
		$.ajax({
			type: "post",
			url: "${ctx}/station/subtab/addEdit?themeType=${themeType}",
			dataType:"json",
			data:{
				title: title,
				redId: redId,
				sortNum: sortNum,
				id: id,
				//url:url,
				status:status,
				templateId:templateId
    		},
			success: function(data){

				if(data.msg=="1"){
					window.location.href="${ctx}/station/subtab/subTabIndex?themeType=${themeType}";
				}else{
					jQuery.jBox.info('保存失败！');
					return false;
				}
			} ,
			error:function(data){
				jQuery.jBox.error('请求失败！');
			}
		});
		
		
}
$(document).ready(function() {
	//加载类目地区下拉框
	if(${dto.themeType == 2}){
		onCSelect('${dto.cid}');
	}else{
		onAddressSelect('${dto.addressId}');
	}
	onThemeSelect('${dto.cid}','${dto.addressId}','${dto.themeId }','${dto.themeType }');
})
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="dto"  class="form-horizontal">
		<input type="hidden" name="id" id="id" value="${dto.id }"/>

		<c:if test="${dto.themeType == 2 }">
			<div class="control-group">
				<label class="control-label">类目:</label>
				<div class="controls">
					<label class="lbl" id="control_label">
						<form:select path="cid" onchange="onThemeSelect(this.value,'${dto.addressId }','${dto.themeId }','${dto.themeType }')">
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
				<label class="lbl" id="theme_label">
					<form:select path="themeId" required="true" onchange="onRecSelect(this.value,'${dto.redId }')">
						<form:option value="" label="请选择子站" />
					</form:select>
				</label>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">楼层名称:</label>
			<div class="controls">
				<form:select id="redId" required="true" path="redId">
					<form:option value="" label="请选择楼层" />
					<form:options items="${mallRec}"  itemLabel="titleDTO"  itemValue="idDTO"/>
				</form:select>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">序号:</label>
			<div class="controls">
				<input placeholder="序号只能是整数" class="required number" type="number" value="${dto.sortNum}" id="sortNum"  name="sortNum" htmlEscape="false" maxlength="4" />
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">页签名称:</label>
			<div class="controls">
				<input type="text" value="${dto.title}" id="title" name="title" htmlEscape="false" maxlength="10" class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">模板号:</label>
			<div class="controls">
			<form:select id="templateId" path="templateId" onchange="showtem(this.value)">
					<form:options items="${model }"  itemLabel="remark"  itemValue="parameter1" />
			</form:select>
			</div>
		</div>
		<div class="control-group tem" >
			<label class="control-label">模板示例:</label>
			<div class="controls" >
			<div >
			<div class="template item1">
				<table width="500px;" height="280" border="2">
					<tr>
					  <td rowspan="2"><div align="center">①</div></td><td height="137"><div align="center">②</div></td>
					<td><div align="center">③</div></td><td><div align="center">④</div></td></tr>
					<tr><td height="133"><div align="center">⑤</div></td>
					<td><div align="center">⑥</div></td><td><div align="center">⑦</div></td></tr>
				</table>
			</div>
			<div class="template item2">
				<table width="500" height="280" border="2">
				  <tr>
				    <td colspan="3"><div align="center">①</div></td>
				    <td><div align="center">②</div></td>
				    <td><div align="center">③</div></td>
				  </tr>
				  <tr>
				    <td><div align="center">④</div></td>
				    <td><div align="center">⑤</div></td>
				    <td><div align="center">⑥</div></td>
				    <td><div align="center">⑦</div></td>
				    <td><div align="center">⑧</div></td>
				  </tr>
				</table>	
			</div>
			<div class="template item3">
				<table width="500" height="280" border="2">
				  <tr>
				    <td width="84"><div align="center">①</div></td>
				    <td width="176"><div align="center">②</div></td>
				    <td width="105"><div align="center">③</div></td>
				    <td width="98"><div align="center">④</div></td>
				  </tr>
				  <tr>
				    <td><div align="center">⑤</div></td>
				    <td><div align="center">⑥</div></td>
				    <td><div align="center">⑦</div></td>
				    <td><div align="center">⑧</div></td>
				  </tr>
				</table>
			</div>
			<div class="template item4">
				<table width="500" height="240" border="2">
				  <tr>
				    <td colspan="2" rowspan="2">①</td>
				    <td rowspan="2">②</td>
				    <td>③</td>
				    <td>④</td>
				  </tr>
				  <tr>
				    <td>⑤</td>
				    <td>⑥</td>
				  </tr>
				  <tr>
				    <td>⑦</td>
				    <td>⑧</td>
				    <td>⑨</td>
				    <td>⑩</td>
				    <td>⑪</td>
				  </tr>
				</table>
			</div>
			<div class="template item5">
				<table width="526" height="200" border="2">
				  <tr>
				    <td colspan="2"><div align="center">①</div></td>
				    <td><div align="center">②</div></td>
				    <td><div align="center">③</div></td>
				    <td><div align="center">④</div></td>
				  </tr>
				  <tr>
				    <td><div align="center">⑤</div></td>
				    <td><div align="center">⑥</div></td>
				    <td><div align="center">⑦</div></td>
				    <td><div align="center">⑧</div></td>
				    <td><div align="center">⑨</div></td>
				  </tr>
				</table>
			</div>
			<div class="template item6">
				<table width="500" height="320" border="2">
				  <tr>
				    <td height="211" colspan="2"><div align="center">①</div></td>
				    <td><div align="center">②</div></td>
				    <td><div align="center">③</div></td>
				    <td><div align="center">④</div></td>
				  </tr>
				  <tr>
				    <td><div align="center">⑤</div></td>
				    <td><div align="center">⑥</div></td>
				    <td><div align="center">⑦</div></td>
				    <td><div align="center">⑧</div></td>
				    <td><div align="center">⑨</div></td>
				  </tr>
				</table>
			</div>
			<div class="template item7">
				<table width="500" height="200" border="2">
				  <tr>
				    <td width="175"><div align="center">①</div></td>
				    <td width="100"><div align="center">②</div></td>
				    <td width="100"><div align="center">③</div></td>
				    <td width="100"><div align="center">④</div></td>
				  </tr>
				  <tr>
				    <td><div align="center">⑤</div></td>
				    <td><div align="center">⑥</div></td>
				    <td><div align="center">⑦</div></td>
				    <td><div align="center">⑧</div></td>
				  </tr>
				</table>
			</div>
			<div class="template item8">
				<table width="500" height="200" border="2">
				  <tr>
				    <td colspan="3"><div align="center">①</div></td>
				    <td><div align="center">②</div></td>
				    <td><div align="center">③</div></td>
				  </tr>
				  <tr>
				    <td><div align="center">④</div></td>
				    <td><div align="center">⑤</div></td>
				    <td><div align="center">⑥</div></td>
				    <td colspan="2"><div align="center">⑦</div></td>
				  </tr>
				</table>
			</div>
			</div>
			</div>
		</div>
		
		<%-- <div class="control-group">
			<label class="control-label">指向链接:</label>
			<div class="controls">
				<input type="text" value="${dto.url}" name="url" id="url" htmlEscape="false" maxlength="500"/>
			</div>
		</div> --%>

		<div class="control-group">
			<label class="control-label" for="status">状态:</label>
			<div class="controls">
			               <c:choose>
                            <c:when test="${dto.status == 1}">
                               启用<input id="status" name="status"  value="1"  checked="checked" type="radio"/>
				不启用<input id="status"  name="status"  value="0" type="radio">
                            </c:when>
                             <c:when test="${dto.status == 0}">
                               启用<input id="status" name="status" value="1"  type="radio"/>
				不启用<input id="status" value="0" name="status" checked="checked"  type="radio">
                            </c:when>
                            <c:otherwise>
						        启用<input id="status" name="status" value="1"  checked="checked"  type="radio"/>
				不启用<input id="status" name="status" value="0" type="radio">
						    </c:otherwise>
						</c:choose>
			</div>
		</div>

		<div class="control-group">
            <div class="controls">
               <button type="button" class="btn btn-primary btn-xs" onclick="btnSubmit()">保存</button>&nbsp;
                <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
		</div>
	</form:form>
</body>
</html>