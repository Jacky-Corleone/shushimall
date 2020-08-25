<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>子站页签维护</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">

	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/station/subtab/subTabIndex?themeType=${dto.themeType}").submit();
    	return false;
    }

	function deleteSub(id){
		var submit = function (v, h, f) {
			console.log(v)
			if (v == 'ok') {
				$.ajax({
					url : "${ctx}/station/subtab/deleteMallSub?themeType=${dto.themeType}",
					type : "POST",
					data :{
						id:id
					},
					dataType : "json",
					success : function(data) {
						if(data.success){
							$.jBox.prompt(data.msg, '消息', 'info', { closed: function () {
								$("#searchForm").submit();

							} });
						}else{
							$.jBox.error(data.msg);
						}
					},
					error : function() {
						$.jBox.error("系统错误！请稍后再试！");
					}
				});
			}

			if (v == 'cancel') {
				//jBox.tip('已取消。');
			}

			return true;
		};

		$.jBox.confirm("是否删除该页签？", "提示", submit);
	}
    //是否下架
    function isShow(id,status,obj) {
		var url=window.href='${ctx}/station/subtab/modifyStatus?id='+id+'&status='+status+'&pageNo='+$("#pageNo").val()+"&themeType=${dto.themeType}";
		confirmx('确认要'+$(obj).text()+'吗？',url);
	}
    function unset(){ //重置
        $(':input','#searchForm')
                .not(':button, :submit, :reset, :hidden')
                .val('')
                .removeAttr('checked')
                .removeAttr('selected');
        $("#pageNo").val(1);
        $("#pageSize").val(10);
        $("#searchForm").submit();
    }
</script>
</head>
<body>

	<form:form id="searchForm" modelAttribute="dto" action="${ctx}/station/subtab/subTabIndex?themeType=${dto.themeType}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>楼层：</label>
			<form:select path="redId" cssClass="input-medium">
				<form:option value="" label="请选择楼层"></form:option>
				<form:options items="${mallRec}" itemLabel="titleDTO" itemValue="idDTO"/>
			</form:select>
			<label>页签名称：</label>
			<form:input path="title" htmlEscape="false" maxlength="50" cssClass="input-medium"/>
            <label>状态：</label>
            <form:select path="status" cssClass="input-medium">
                <form:option value="" label="请选择状态"/>
                <form:option value="1" label="启用"/>
                <form:option value="0" label="不启用"/>
            </form:select>
		</div>
		<div style="margin-top:8px;">
 			<label>创建时间：</label>
 			<form:input id="d4311" path="startTime" class="Wdate input-medium" readonly="true" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" />至
 			<form:input path="endTime" class="Wdate input-medium" readonly="true" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"  />
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
            <a href="${ctx}/station/subtab/toedit?themeType=${dto.themeType}" class="btn btn-primary" >新增页签</a>
            <input id="btnunset"  class="btn  btn-primary"  type="reset" value="重置" onclick="unset();" />
        </div>
	</form:form>

	<tags:message content="${message}"/>
	<table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed" >
		<thead>
			<tr>
				<th style="width: 1%">序号</th>
				<!-- <th style="width: 10%">指向链接</th> -->
				<th style="width: 5%">页签名称</th>
				<th style="width: 2%">顺序号</th>
				<th style="width: 2%">状态</th>
				<th style="width: 3%">模板</th>
				<th style="width: 2%">楼层名称</th>
				<th style="width: 5%">创建时间</th>
                <th style="width: 5%">改动时间</th>
				<th style="width: 3%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="sub" varStatus="s">
				   <tr>
				       <td><c:out value="${s.count }" /></td>
				       <%-- <td>${sub.url }</td> --%>
				       <td>${sub.title }</td>
				       <td>${sub.sortNum }</td>
				       <td>
                               <c:choose>
                            <c:when test="${sub.status == 1}">
                               启用
                            </c:when>
                             <c:when test="${sub.status == 0}">
                               不启用
                            </c:when>
						</c:choose>
                         </td>
                         <td>${sub.remark }</td>
                         <td>${sub.recName }</td>
				       <td><fmt:formatDate value="${sub.created }" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /></td>
				       <td><fmt:formatDate value="${sub.modified }" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" /></td>
				       <td>
						   <a href="${ctx}/station/subtab/toedit?id=${sub.id}&themeType=${dto.themeType}">编辑</a>&nbsp;&nbsp;|
                           <a id="cc" href="javascript:void(0)" onclick="isShow('${sub.id}','${sub.status}' ,this)">${sub.status==1?'禁用':'启用'}</a>&nbsp;&nbsp;|
						   <a id="delete" href="javascript:void(0)" onclick="deleteSub('${sub.id}')">删除</a>
					   </td>
				   </tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>