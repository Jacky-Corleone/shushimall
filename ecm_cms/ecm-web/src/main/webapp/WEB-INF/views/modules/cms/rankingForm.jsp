<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>排行榜管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#reContent").focus();
			$("#inputForm").validate();
			changeType('${ranking.type}');
		});
		
		function cancel(cid){
			window.location.href="${ctx}/cms/ranking/list?cid="+cid;
		}
		function saveRanking(){
			$("#inputForm").submit();
		}
		
		function changeType(value){
			if(value==0){
				$("#typeId").val(value);
				$(".networkDiv").css("display","block");
				$(".offlineDiv").css("display","none");
				$("#hotTypeId").val("");
				$("#brandCountryId").val("");
				$("#occupancyId").val("");
				$("#scoreId").val("");
			}else if(value==1){
				$("#typeId").val(value);
				$(".networkDiv").css("display","none");
				$(".offlineDiv").css("display","block");
				$("#brandintroId").val("");
				$("#viewnumId").val("");
			}	
		}
		
		function changeTrend(value){
			$("#trendId").val(value);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/cms/ranking/list?cid=${ranking.cid}">排行榜子信息列表</a></li>
		<li class="active"><a href="${ctx}/cms/ranking/form?cid=${ranking.cid}">排行榜子信息添加</a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="ranking" action="${ctx}/cms/ranking/save" method="post" class="form-horizontal">
		<form:hidden path="id" value="${ranking.id}"/>
		<form:hidden path="cid" value="${ranking.cid}"/>
		<div class="control-group">
			<label class="control-label">品牌名称：</label>
			<div class="controls">
				<form:input path="brand" value="${ranking.brand}"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">类别：</label>
			<div class="controls">
				<form:radiobutton path="type"  value="0" htmlEscape="false" onclick="changeType(0)"/>网络排行榜
				<form:radiobutton path="type"  value="1" htmlEscape="false" onclick="changeType(1)"/>线下排行榜
			</div>
		</div>
		
		<!-- 网络 -->
		<div class="control-group networkDiv">
			<label class="control-label">品牌介绍：</label>
			<div class="controls">
				<form:input path="brandintro" value="${ranking.brandintro}"/>
			</div>
		</div>
		<div class="control-group networkDiv" >
			<label class="control-label">排名趋势：</label>
			<div class="controls">
				<form:radiobutton path="trend"  value="0" htmlEscape="false"/>无变化
				<form:radiobutton path="trend"  value="1" htmlEscape="false"/>上升
				<form:radiobutton path="trend"  value="2" htmlEscape="false"/>下降
			</div>
		</div>
		<div class="control-group networkDiv">
			<label class="control-label">人气：</label>
			<div class="controls">
				<form:input path="viewnum" value="${ranking.viewnum}"/>
			</div>
		</div>
		
		<!-- 线下 -->
		<div class="control-group offlineDiv" style="display: none;">
			<label class="control-label">热卖型号:：</label>
			<div class="controls">
				<form:input path="hotType" value="${ranking.hotType}"/>
			</div>
		</div>
		<div class="control-group offlineDiv" style="display: none;">
			<label class="control-label">品牌国家：</label>
			<div class="controls">
				<form:input path="brandCountry" value="${ranking.brandCountry}"/>
			</div>
		</div>
		<div class="control-group offlineDiv" style="display: none;">
			<label class="control-label">品牌占有率：</label>
			<div class="controls">
				<form:input path="occupancy" value="${ranking.occupancy}"/>
			</div>
		</div>
		
		<div class="control-group offlineDiv" style="display: none;">
			<label class="control-label">口碑评分：</label>
			<div class="controls">
				<form:input path="score" value="${ranking.occupancy}"/>
			</div>
		</div>
		
		<div class="form-actions">
		<input id="btnSubmit" class="btn btn-inverse" type="submit" value="保存" onclick="saveRanking()"/>
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="cancel('${ranking.cid}');"/>
	</div>
	</form:form>
	
	
</body>
</html>

