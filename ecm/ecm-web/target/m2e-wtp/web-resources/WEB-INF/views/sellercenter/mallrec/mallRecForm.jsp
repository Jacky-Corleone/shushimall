<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>楼层基本信息</title>
	<meta name="decorator" content="default"/>
	<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
	            rules:{
	            	titleDTO:"required",
	            	categoryIdDTO:"required",
	            	floorTypeDTO:"required",
	                floorNumDTO:{
	                    required:true,
	                    digits:true
	                }
	            }
	        })
	        $("#btnSubmit").click(function(){
	            if($("#inputForm").valid()){
	                $("#inputForm").submit();
	            }
	        })
		})
		function redioBotton(){
			 showtheme();
		}
		function showtheme(){
			page(1,10,themeId);
		}
		function page(n,s){
			var themeId = $("#themeId").val();
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$.jBox.tip("正在加载列表，请稍等",'loading',{opacity:0});
			$.ajax({
	           url:"${ctx}/sellercenter/mallTheme/adlist?themeId="+themeId,
	           type:"post",
	           data:$("#mallThemeForm").serialize(),
	           dataType:'html',
	           success:function(data){
	           		$("#themeListDiv").html(data);
	           		$.jBox.closeTip();
	           }
	       });
	   }
		function doUpdate(){
			$("#themeId").val($("#pdId").val());
			$("#themeName").val($("#pdName").val());
			$("#modal-container").modal("hide");
		}
		function cancelUpdate(){
			$("#themeId").val();
			$("#themeName").val();
			$("#modal-container").modal("hide");
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="mallRec" action="${ctx}/mallRec/save" method="post" class="form-horizontal">
		<form:hidden path="idDTO"/>
		<input type="hidden" name="addressType" value="${addressType }">
		<div class="control-group">
			<label class="control-label" for="titleDTO">楼层名称:</label>
			<div class="controls">
				<form:input path="titleDTO" htmlEscape="false" maxlength="16" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="titleDTO">英文名称:</label>
			<div class="controls">
				<form:input path="engNameDTO" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="floorNumDTO">楼层排序:</label>
			<div class="controls">
				<form:input path="floorNumDTO"  htmlEscape="false" maxlength="10" class="required"/>
			</div>
		</div>
		<!-- 
		<div class="control-group">
			<label class="control-label" for="categoryIdDTO">经营商品分类:</label>
			<div class="controls">
				<select name="categoryIdDTO" id="categoryIdDTO" style="width:27%" class="form-control">
                    <option value="">请选择</option>
                    <c:forEach items="${itemCateGoryList}" var="item">
                        <option  <c:if test="${item.categoryCid==mallRec.categoryIdDTO}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
                    </c:forEach>
                </select>
			</div>
		</div>
		 -->
		<c:if test="${addressType == 1}">
			<div class="control-group">
				<label class="control-label">所属子站:</label>
				<div class="controls">
					<form:input  type="hidden" path="themeId"/>
					<input type="text" id="themeName" value="${themeDTO.themeName }"/>
					<a class="btn" id="modal-686826" role="button" href="#modal-container" onclick="redioBotton();" data-toggle="modal">选择地区子站</a>
				</div>
			</div>
		</c:if>
			
		<div class="control-group">
			<label class="control-label" for="floorNumDTO">是否为热卖单品:</label>
			<div class="controls">
				<input type="radio" name = "floorTypeDTO" <c:if test="${mallRec.floorTypeDTO==1}">checked="checked"</c:if> value = "1"/>是
				<c:choose>
					<c:when test="${mallRec==null||mallRec.floorTypeDTO==null}">
						<input type="radio" name = "floorTypeDTO" checked="checked" value = "0"/>否<br/>
					</c:when>
					<c:otherwise>
						<input type="radio" name = "floorTypeDTO" <c:if test="${mallRec.floorTypeDTO==0}">checked="checked"</c:if> value = "0"/>否<br/>
					</c:otherwise>
				</c:choose>
				<span style="color:red">热卖单品楼层有效上架唯一,当其中一个上架其他热卖单品楼层会自动下架.</span>
			</div>
		</div>
		<div class="control-group">
            <div class="controls">
                <input class="btn btn-primary" type="button" id="btnSubmit" value="保存"/>
                <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
        </div>
	</form:form>
	<div class="modal hide fade" id="modal-container" role="dialog" aria-hidden="true" aria-labelledby="myModalLabel"
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