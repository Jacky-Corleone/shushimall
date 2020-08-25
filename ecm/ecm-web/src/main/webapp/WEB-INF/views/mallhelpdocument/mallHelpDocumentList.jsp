<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>帮助中心管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">
		td {
		   text-align: center;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			// 表格排序
			//tableSort({callBack : page});
            $("#addDoc").click(function(){
                var url = "${ctx}/mallHelpDocument/form";
                parent.openTab(url,"添加帮助文档","d0");
            });
		});

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		function edit(id){
            var url = "${ctx}/mallHelpDocument/form?mallId="+id;
            parent.openTab(url,"编辑帮助文档","d"+id);
        }
		
		function view(id){
            var url = "${ctx}/mallHelpDocument/view?mallId="+id;
            parent.openTab(url,"编辑帮助文档","d"+id);
        }
        function updateStatus(docid,status){
            var url="${ctx}/mallHelpDocument/status";
            $.ajax({
                url:url,
                type:"post",
                data:{
                    mallId:docid,
                    mallStatus:status
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        $.jBox.prompt(data.msg, '消息', 'info', { closed: function () {
                            $("#searchForm").submit();
                        }
                        });
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(){
                    $.jBox.info("亲，系统繁忙请稍后再试");
                }
            });
        }
	</script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
            <form:form id="searchForm" modelAttribute="mallDocumentDTO" action="${ctx}/mallHelpDocument/list" method="post" class="breadcrumb form-search">
                <tags:message content="${message}"/>
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <div class="row-fluid">
                    <div style="width: 100%">
                        <label >帮助文档主题：</label>
                        <form:input path="mallTitle" class="input-medium"/>
                        <label>类型：</label>
                        <form:select path="mallType" id="mallType" class="input-medium">
                            <form:option value="" label="请选择" />
                            <c:forEach items="${typeList}" var="typeVal">
                                <c:forEach var="entry" items="${typeVal}">
                                    <form:option value="${entry.key }" label="${entry.value }" />
                                </c:forEach>
                            </c:forEach>
                        </form:select>
                    </div>

                    <div class="row-fluid" style="margin-top: 10px">
                        <div class="span7">
                            <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时间类型：</label>
                            <select name="timeType" id="timeType" class="input-medium">
                                <option value="createTime" <c:if test="${timeType=='createTime'}">selected="selected"</c:if>>创建时间</option>
                                <option value="publicTime" <c:if test="${timeType=='publicTime'}">selected="selected"</c:if>>发布时间</option>
                            </select>
                            <input id="beginTime" name="beginTime" type="text" value="${beginTime}"
                                   readonly="readonly" maxlength="20" class="input-small Wdate"
                                   onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
                            至:
                            <input id="endTime" name="endTime" type="text" readonly="readonly" value="${endTime}"
                                   maxlength="20" class="input-small Wdate"
                                   onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
                        </div>
                        <div class="span2">
                            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
                        </div>
                    </div>

                </div>

            </form:form>
        </div>
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span3 offset9 text-right">
                    <a href="${ctx}/mallHelpDocument/form" class="btn btn-primary" >添加帮助文档</a>
                </div>
            </div>
            <div class="row-fluid">
                <table id="contentTable" class="table table-striped table-bordered table-condensed">
                    <thead><tr>
                        <th>序号</th>
                        <th>编号</th>
                        <th>帮助主题</th>
                        <th>一级分类</th>
                        <th>二级分类</th>
                        <th>创建时间</th>
                        <th>发布时间</th>
                        <th>其他操作</th>
                    </tr></thead>
                    <tbody>
                    <c:forEach items="${page.list}" var="mallDocumentDTO" varStatus="s">
                        <tr>
                            <td><c:out value="${s.count}" /></td>
                            <td>${mallDocumentDTO.mallId}</td>
                            <td>${mallDocumentDTO.mallTitle}</td>
                            <td>
                                <c:forEach items="${typeList}" var="typeVal">
                                    <c:forEach var="entry" items="${typeVal}">
                                        <c:if test="${entry.key == mallDocumentDTO.mallType }">
                                            <c:out value="${entry.value }"></c:out>
                                        </c:if>
                                    </c:forEach>
                                </c:forEach>
                            </td>
                            <td>${mallDocumentDTO.mallClassifyTitle}</td>
                            <td>
                                <fmt:formatDate value="${mallDocumentDTO.mallCreated}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" />
                            </td>
                            <td>
                                <fmt:formatDate value="${mallDocumentDTO.mallStartTime}" pattern="yyyy-MM-dd HH:mm:ss" type="date" dateStyle="long" />
                            </td>

                            <td>
                                    <%--<a href="${ctx}/mallHelpDocument/form?mallId=${mallDocumentDTO.mallId}">编辑</a>--%>
                                <c:if test="${mallDocumentDTO.mallStatus == null ||mallDocumentDTO.mallStatus == 1}">
                                    <a href="${ctx}/mallHelpDocument/form?mallId=${mallDocumentDTO.mallId}">编辑</a>
                                    <a href="javascript:updateStatus('${mallDocumentDTO.mallId}','2')">发布</a>
                                    <a href="${ctx}/mallHelpDocument/view?mallId=${mallDocumentDTO.mallId}">查看</a>
                                </c:if>
                                <c:if test="${ mallDocumentDTO.mallStatus == 2}">
                                    <a href="javascript:updateStatus('${mallDocumentDTO.mallId}','1')">下架</a>
                                    <a href="${ctx}/mallHelpDocument/view?mallId=${mallDocumentDTO.mallId}">查看</a>
                                </c:if>
                            </td>

                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="pagination text-right">${page}</div>
            </div>

        </div>

    </div>
</div>

</body>
</html>