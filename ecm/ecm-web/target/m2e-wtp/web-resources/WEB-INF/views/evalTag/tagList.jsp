<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style>
h3 {
	color: #000000;
	height: 46px;
	line-height: 46px;
	text-indent: 20px;
	font-size: 15px;
	font-family: \5FAE\8F6F\96C5\9ED1;
	font-weight: 500;
}

table.td-cen th, .td-cen td {
	text-align: center;
}

.hhtd td {
	word-wrap: break-word;
	word-break: break-all;
}

.tag-view {
	font-size: 20px;
	background-color: rgb(68, 173, 217);
	color: rgb(255, 255, 255);
	border-right-width: 0px;
	margin-right: 20px;
	padding: 0px 7px;
	cursor: default;
}
</style>
<script type="text/javascript">
$(function() {
	// 一二级类目级联，一级类目选择后，二级类目变动
	$("#cidL1Select").change(function() {
		$("#cidL2Select").select2('val','');
		if (!this.value) {
			$("#cidL2Select").html('');
			return;
		}
		$.post(
			ctx + "/item/datagrid",
			{parentCid:this.value},
			function(data) {
				var optionsOfcidL2Select = '<option value="" selected>请选择</option>';
				$(data.rows).each(function() {
					optionsOfcidL2Select += '<option value="' + this.categoryCid + '">' + this.categoryCName + '</option>';
				});
				$("#cidL2Select").html(optionsOfcidL2Select);
			},
			'json'
		);
	});
	
	// 编辑界面保存按钮操作
	$("#saveEditBtn").click(function() {
		$.jBox.confirm("确定保存吗？","系统提示",function(v){
			if ('ok' !== v)
				return;
			if (!$.trim($("#newTagName").val())) {
				alert('新标签名不能为空');
				$("#newTagName").focus();
				return;
			}
			$.post(
				ctx + '/evalTag/updateNameOfOneTag',
				{
					tagId: $("#newTagName").siblings(':hidden').val(),
					tagName: $("#newTagName").val()
				},
				function(data) {
					if (data.success) {
						alert('保存成功');
						$("#btnquery").click();
					}else {
						alert('保存失败');
					}
				},
				'json'
			)
		});
	});
	
	// 删除页面保存按钮操作
	$("#delTagsBtn").click(function() {
		$.jBox.confirm("确定删除选中的标签吗？","系统提示",function(v){
			if ('ok' !== v)
				return;
			var allChecked = $("#delPanel table :checked");
			if (allChecked.length == 0) {
				alert('请选中至少一项');
				return;
			}
			var toDelTagIds = '';
			allChecked.each(function() {
				var id = this.id.slice(6);
				if (!isNaN(id)) {
					toDelTagIds += id + ',';
				}
			})
			toDelTagIds = toDelTagIds.replace(/,$/, '');
			$.post(
				ctx + '/evalTag/deleteTags',
				{
					cidL2: $("#catIdOfDeleted").val(),
					toDelTagIds: toDelTagIds
				},
				function(data) {
					if (data.success) {
						alert('删除成功');
						$("#btnquery").click();
					}else {
						alert('删除失败');
					}
				},
				'json'
			)
		});
	});
}); // end of onload



// 通过检索按钮检索
function searchList() {
	$("#searchForm").submit();
}

function page(page, rows) {
	$("#page").val(page);
	searchList();
}
</script>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<form:form id="searchForm"
				commandName="evalTagsOfCatDTO" class="breadcrumb form-search">
				<input type="hidden" name="page" id="page" value="${page.pageNo}"/>
				<div class="row-fluid" style="margin-top: 10px;">
					<div class="span4">
						<form:label path="cidL1"
							cssClass="control-label">一级类目：</form:label>
						<form:select path="cidL1" cssClass="control-label" id="cidL1Select">
							<form:option value="">请选择</form:option>
							<form:options items="${catsL1}" itemLabel="categoryCName" itemValue="categoryCid"/>
						</form:select>
					</div>
					<div class="span4">
						<form:label path="cidL2"
							cssClass="control-label">二级类目：</form:label>
						<form:select path="cidL2" cssClass="control-label" id="cidL2Select">
							<form:option value="">请选择</form:option>
							<form:options items="${catsL2}" itemLabel="categoryCName" itemValue="categoryCid"/>
						</form:select>
					</div>
				</div>
				<div class="row-fluid" style="margin-top: 8px;">
					<div class="span4">
						<label class="label-left control-label"></label>
						<input
							id="btnquery"
							class="btn  btn-primary" type="button"
							onclick="searchList()" value="查询" />
					</div>
				</div>
			</form:form>
			<table id="contentTable"
				class="table table-striped table-bordered table-condensed td-cen hhtd">
				<colgroup>
					<col width="60px"/>
					<col width="200px"/>
					<col width="200px"/>
					<col width=""/>
					<col width="200px"/>
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>一级类目</th>
						<th>二级类目</th>
						<th>标签列表</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="tabletbody">
					<c:forEach items="${page.list}" var="catTag" varStatus="s">
						<tr>
							<td>${s.count}</td>
							<td>${catTag.catL1.categoryCName}</td>
							<td>${catTag.catL2.categoryCName}</td>
							<td>
								<c:forEach items="${catTag.evalTagGroups}" var="tag">
									&#12288;<span>${tag.tagName}</span>
								</c:forEach></td>
							<td>
								<a onclick="openViewPanel(${catTag.catL2.categoryCid})"
									href="javascript:void(0)">查看</a>&#12288;<a 
								onclick="openEditPanel(${catTag.catL2.categoryCid})"
									href="javascript:void(0)">编辑</a>&#12288;<a 
									onclick="openDelPanel(${catTag.catL2.categoryCid})"
									href="javascript:void(0)">删除</a>
								</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="pagination">${page}</div>
		</div>
	</div>
	<div class="modal hide fade" id="viewPanel" style="margin-top:-35px;margin-left:-510px;width:1010px;height:250px;overflow: auto;">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
			<h5>查看标签</h5></div>
		<div style="margin: 50px auto; width: 80%;">
		</div>
	</div>
	<div class="modal hide fade" id="editPanel" style="margin-top:-35px;margin-left:-510px;width:1010px;height:200px;overflow: auto;">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
			<h5>编辑标签</h5></div>
		<div class="row-fluid" style="margin-top: 50px;">
			<div class="span1" style="padding: 5px 0px 0px 2px;">
				选择标签：
			</div>
			<div class="span3" style="margin-left: 0px;">
				<select>
				</select>
			</div>
			<div class="span7">
				将<input readonly="readonly" id="currentTagName" />更改为
				<input id="newTagName">
				<input type="hidden">
				<button id="saveEditBtn" disabled>保存</button>
			</div>
		</div>
	</div>
	<div class="modal hide fade" id="delPanel" style="margin-top:-35px;margin-left:-510px;width:1010px;height:450px;overflow: auto;">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
			<h5>删除标签</h5></div>
		<div>
			<table style="margin: 50px auto; width: 400px;"
				class="table table-striped table-bordered table-condensed td-cen hhtd">
				<colgroup>
					<col width="50px">
					<col>
				</colgroup>
				<tr>
					<th><input type="checkbox" id="allCheck"/></th>
					<th style="font-size: 20px;">标签</th>
				</tr>
			</table>
		</div>
		<div style="text-align: center">
			<button id="delTagsBtn">删除选中</button>
			<input type="hidden" id="catIdOfDeleted">
		</div>
	</div>

	<script type="text/javascript">
	
		// 打开查看界面
		function openViewPanel(cidL2) {
			$.post(
				ctx + "/evalTag/queryTagsByCatL2",
				{cidL2: cidL2},
				function(data) {
					if (data.success) {
						var tagsText = '';
						var pre = '<span class="tag-view">';
						var post = '</span>';
						$(data.tags).each(function() {
							tagsText += pre + this.tagName + post;
						});
						$("#viewPanel div:eq(1)").html(tagsText);
					} else {
						$("#viewPanel div:eq(1)").html('未查询到标签');
					}
				},
				'json'
			);

			$("#viewPanel").modal('show');
		}
	
		// 打开编辑界面
		function openEditPanel(cidL2) {
			$("#editPanel select").html('');
			$("#saveEditBtn").siblings("input").val('');
			$.post(
				ctx + "/evalTag/queryTagsByCatL2",
				{cidL2: cidL2},
				function(data) {
					// 1.1 存在标签，塞入#savedTags，显示此区域
					if (data.success) {
						var savedTags = '<option value="">请选择</option>';
						$(data.tags).each(function(){
							savedTags += '<option value="' + this.tagId + '">' + this.tagName + '</option>';
						})
						savedTags = savedTags.replace(/,$/, '');
						$("#editPanel select").html(savedTags);
						$("#editPanel select").select2('val','');
						$("#editPanel select").change(function() {
							if (this.value !== '') {
								var selected = $(this).children(':selected');
								$("#currentTagName").val(selected.text());
								$("#currentTagName").siblings(":hidden").val(selected.val());
								$("#newTagName").focus();
								$("#saveEditBtn").removeProp('disabled');
							} else {
								$("#saveEditBtn").prop('disabled', true);
							}
						});
					} else {
						$("#editPanel select").parent().html('该类目下无标签');
						$("#currentTagName").parent().parent().hide();
					}
				},
				'json'
			);
			$("#editPanel").modal('show');
		}
		
		// 打开删除界面
		function openDelPanel(cidL2) {
			$("#catIdOfDeleted").val(cidL2);
			$("#allCheck").prop('checked', false);
			$.post(
				ctx + "/evalTag/queryTagsByCatL2",
				{cidL2: cidL2},
				function(data) {
					if (data.success) {
						var table = $("#delPanel table");
						var trs = '';
						$(data.tags).each(function(){
							var tr = '<tr><td><input id="toDel_'+this.tagId+'" type="checkbox"/></td><td>'
							+ this.tagName +'</td></tr>';
							trs += tr;
						});
						$("tr:gt(0)", table).remove();
						table.append(trs);
						
						var allCheckboxes = $("#delPanel table :checkbox");
						var itemCheckboxes = allCheckboxes.slice(1,allCheckboxes.length);
						allCheckboxes.each(function(index) {
							$(this).change(function() {
								if (index == 0) {
									if (this.checked) {
										itemCheckboxes.prop('checked',true);
									} else {
										allCheckboxes.prop('checked',false);
									}
								} else {
									if (this.checked) {
										$("#allCheck").prop('checked',
												$("#delPanel table :checked").length == itemCheckboxes.length?true:false);
									} else {
										$("#allCheck").prop('checked', false);
									}
								}
							})
						})
						
						$("#delPanel").modal('show');
					}
				},
				'json'
			);
		}
	</script>
</body>
</html>