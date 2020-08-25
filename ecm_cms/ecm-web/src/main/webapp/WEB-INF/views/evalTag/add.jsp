<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
$(function() {
	// 一二级类目级联，一级类目选择后，二级类目变动
	$("#cidL1Select").change(function() {
		$("#cidL2Select").select2('val', '');
		if (!this.value) {
			$("#cidL2Select").html('');
			$("#savedTags").hide();
			$("#savedTags").html('');
			$("#noTags").show();
			return;
		}
		$("#newTagBtn").siblings().remove();
		$.post(
			ctx + "/item/datagrid",
			{parentCid:this.value},
			function(data) {
				var optionsOfcidL2Select = '<option value=""">请选择</option>';
				$(data.rows).each(function() {
					optionsOfcidL2Select += '<option value="' + this.categoryCid + '">' + this.categoryCName + '</option>';
				});
				$("#cidL2Select").html(optionsOfcidL2Select);
			},
			'json'
		);
	});
	$("#cidL2Select").change(function() {
		$("#newTagBtn").siblings().remove();
		var cidL2 = this.value;
		if (!cidL2) {
			$("#savedTags").hide();
			$("#savedTags").html('');
			$("#noTags").show();
			return;
		}
		//1. 检索已存标签
		$.post(
			ctx + "/evalTag/queryTagsByCatL2",
			{cidL2: cidL2},
			function(data) {
				// 1.1 存在标签，塞入#savedTags，显示此区域
				if (data.success) {
					var savedTags = '';
					$(data.tags).each(function(){
						savedTags += this.tagName + "&#12288;&#12288;";
					})
					savedTags = savedTags.replace(/,$/, '');
					$("#savedTags").html(savedTags);
					$("#savedTags").show();
					$("#noTags").hide();
				}
				// 1.2不存在标签，隐藏#savedTags,显示#noTags
				else {
					$("#savedTags").hide();
					$("#savedTags").html('');
					$("#noTags").show();
				}
			},
			'json'
		);
	});
	
	$("#newTagBtn").click(function() {
		var inputMaterial = $("#material input")[0].outerHTML;
		$(this).before(inputMaterial);
	});
	
	$("#saveAddBtn").click(function() {
		// 要有二级类目
		if (!$("#cidL2Select").val()) {
			alert('请选择二级类目');
			return;
		}
		// 拼接新增标签
		var tagNamesToAdd = '';
		$("#newTagBtn").siblings('').filter(function() {
			return $.trim(this.value);
		}).each(function() {
			tagNamesToAdd += this.value + ',,,';
		});
		tagNamesToAdd = tagNamesToAdd.replace(/,,,$/, '');
		// 新增标签个数不能为0
		if (!tagNamesToAdd) {
			alert('至少增加一个标签');
			return;
		}
		$("#tagNamesToAdd").val(tagNamesToAdd);

		$.post(
			$("#addForm").prop('action'),
			$("#addForm").serialize(),
			function(data) {
				if (data.success) {
					alert(data.msg);
				}else {
					alert(data.msg[0]);
				}
				$("#cidL2Select").change();
			},
			'json'
		);
	});
	
}); // end of onload
</script>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<form id="addForm" action="${ctx}/evalTag/saveAdd"
				class="breadcrumb form-search">
				<div class="row-fluid" style="margin-top: 10px;">
					<div class="span4">
						<label for="cidL1"
							class="control-label">一级类目：</label>
						<select name="cidL1" class="control-label" id="cidL1Select">
							<option value="">请选择</option>
							<options items="${catsL1}" itemLabel="categoryCName" itemValue="categoryCid"/>
							<c:forEach items="${catsL1}" var="cat">
								<option value="${cat.categoryCid}">${cat.categoryCName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="span4">
						<label for="cidL2"
							class="control-label">二级类目：</label>
						<select name="cidL2" class="control-label" id="cidL2Select">
						</select>
					</div>
				</div>
				<input type="hidden" value="" name="tagNamesToAdd" id="tagNamesToAdd"><!-- 需要保存的标签名 -->
			</form>
			<p>已保存标签：</p>
			<hr>
			<div style="height:50px; display: none; color: red;" id="noTags">
				暂无
			</div>
			<div id="savedTags">
			</div>
			<hr>
			<!-- 新增标签区域 -->
			<div>
				<button id="newTagBtn">新增</button>
			</div>
			<hr>
			<div>
				<button id="saveAddBtn">保存</button>
			</div>
			<div style="display: none;" id="material">
				<input style="width:150px;"/>
			</div>
		</div>
	</div>
</body>
</html>