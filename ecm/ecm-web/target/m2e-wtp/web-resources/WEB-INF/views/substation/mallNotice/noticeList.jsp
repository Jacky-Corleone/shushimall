<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>子站公告列表</title>
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
            $("#addNotice").click(function(){
                var url = "${ctx}/station/mallNotice/form?themeType=${mallNoticeDTO.themeType}";
                parent.openTab(url,"添加公告","n0");
            });
		});
        function unset(){
            $(':input','#searchForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $("#pageNo").val(1);
            $("#pageSize").val(10);
            $("#searchForm").submit();
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    }
        function moveNotice(noticeId,sortNum,isRecommend,modifyNum){
        	var url = "${ctx}/station/mallNotice/moveNotice?noticeId="+noticeId+"&isRecommend="+isRecommend+"&modifyNum="+modifyNum+"&sortNum="+sortNum+"&platformId=0";
        	var submit = function (v, h, f) {
        		if (v == true)
				 	$.ajax({
						url : url,
						type : "get",
						dataType : "json",
						success : function(data) {
							if(data.success){
								$.jBox.success("操作成功!",'提示',{closed:function(){
									$("#searchForm").submit();
								}});
								
							}else{
								$.jBox.info("移动时出错,请稍后再试!");
							}
							
						},
						error : function(xmlHttpRequest, textStatus, errorThrown) {
							$.jBox.info("系统错误！请稍后再试！");
						}
					});
        		else  
					jBox.tip("取消", 'info');  
				return true; 
        	}
        	// 自定义按钮  
			$.jBox.confirm("是否确认移动？", "提示", submit, { buttons: { '确定': true, '取消': false} }); 
				 
    	}
        function deleteNotice(noticeId){
        	var url = "${ctx}/station/mallNotice/deleteNotice?noticeId="+noticeId;
        	var submit = function (v, h, f) {
        		if (v == true) 
		        	$.ajax({
						url : url,
						type : "get",
						dataType : "json",
						success : function(data) {
							if(data.success){
								$.jBox.success("操作成功!",'提示',{closed:function(){
									$("#searchForm").submit();
								}});
							}else{
								$.jBox.info("删除时出错,请稍后再试!");
							}
						},
						error : function(xmlHttpRequest, textStatus, errorThrown) {
							$.jBox.info("系统错误！请稍后再试！");
						}
					});
        		else
        			jBox.tip("取消", 'info');  
				return true; 
        	}
        			
        	$.jBox.confirm("确认要删除吗？", "提示", submit, { buttons: { '确定': true, '取消': false} });
				 
    	}
        function updatestace(noticeId,status){
            var url = "${ctx}/station/mallNotice/status";
                $.ajax({
                    url : url,
                    type : "post",
                    dataType : "json",
                    data:{
                    	noticeId:noticeId,
                    	noticeStatus:status
                    },
                    success : function(data) {
                        if(data.success){
                            $.jBox.success("操作成功!",'提示',{closed:function(){
                                $("#searchForm").submit();
                            }});
                        }else{
                            $.jBox.info(data.msg);
                        }
                    },
                    error : function(xmlHttpRequest, textStatus, errorThrown) {
                        $.jBox.info("系统错误！请稍后再试！");
                    }
                });
        }
        function editNotice(id){
            var url = "${ctx}/station/mallNotice/form?noticeId="+id;
            parent.openTab(url,"编辑公告","n1"+id);
        }
        function viewNotice(id){
            var url = "${ctx}/station/mallNotice/viewNotice?id="+id;
            parent.openTab(url,"查看公告","n2"+id);
        }

        function recommend(noticeId,isRecommend){
            var url = "${ctx}/station/mallNotice/recommend";
            $.ajax({
                url : url,
                type : "post",
                dataType : "json",
                data:{
                	noticeId:noticeId,
                    isRecommend:isRecommend
                },
                success : function(data) {
                    if(data.success){
                        $.jBox.success("操作成功!",'提示',{closed:function(){
                            $("#searchForm").submit();
                        }});
                    }else{
                        $.jBox.info(data.msg);
                    }
                },
                error : function(xmlHttpRequest, textStatus, errorThrown) {
                    $.jBox.info("系统错误！请稍后再试！");
                }
            });
        }
	</script>
    <style>
        label.label-left{
            width: 25%;
            text-align: right;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
            <div class="row-fluid">
                <form:form id="searchForm" modelAttribute="mallNoticeDTO" action="${ctx}/station/mallNotice/list" method="post" class="breadcrumb form-search">
                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                    <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                    <input id="themeType" name="themeType" type="hidden" value="${mallNoticeDTO.themeType }" />
                    <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                            <label class="label-left control-label"  for="title" title="公告标题">
                                公告标题：
                            </label>
                            <form:input path="noticeTitle" id="title" style="width:50%" type="text" class="form-control"   />
                        </div>
                        <div class="span5">
                            <label class="label-left control-label" for="noticeType" title="公告类型">
                                公告类型：
                            </label>
                            <form:select path="noticeType" id="noticeType" style="width:50%" class="form-control">
                                <form:option value="">请选择</form:option>
                                <form:option value="1">文字公告</form:option>
                                <form:option value="2">链接公告</form:option>
                            </form:select>
                        </div>

                    </div>
                    <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                            <label class="label-left control-label"  for="createDtBegin" title="创建时间">
                                创建时间：
                            </label>
                            <input value="<fmt:formatDate value="${mallNoticeDTO.createDtBegin}" pattern="yyyy-MM-dd" type="date" dateStyle="long" />" name="createDtBegin" id="createDtBegin" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:30%" type="text" class="form-control"   />
                            至<input value="<fmt:formatDate value="${mallNoticeDTO.createDtEnd}" pattern="yyyy-MM-dd" type="date" dateStyle="long" />"  name="createDtEnd" id="createDtEnd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:30%" type="text" class="form-control"   />
                        </div>
                        <div class="span5">
                            <label class="label-left control-label"  for="createDtBegin" title="发布时间">
                                发布时间：
                            </label>
                            <input value="<fmt:formatDate value="${mallNoticeDTO.publishDtBegin}" pattern="yyyy-MM-dd" type="date" dateStyle="long" />" name="publishDtBegin" id="publishDtBegin" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:30%" type="text" class="form-control"   />
                            至<input value="<fmt:formatDate value="${mallNoticeDTO.publishDtEnd}" pattern="yyyy-MM-dd" type="date" dateStyle="long" />" name="publishDtEnd" id="publishDtEnd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width:30%" type="text" class="form-control"   />
                        </div>
                    </div>
                    <div class="row-fluid" style="margin-top:10px;">
                        <div class="span5">
                            <label class="label-left control-label" for="noticeType" title="公告状态">
                               频道：
                            </label>
							<form:select id="themeId" path="themeId" class="form-control">
								<form:option value="" label="请选择频道"/>
								<c:forEach items="${themeList}" var="theme">
		                       		<c:if test="${theme.status==1}">
		                       			<option value="${theme.id}" ${theme.id==themeId?'selected':''}>${theme.themeName}</option>
		                       		</c:if>
		                       </c:forEach>
							</form:select>
                        </div>
                        <div class="span5">
                            <label class="label-left control-label" for="noticeType" title="公告状态">
                                公告状态：
                            </label>
                            <form:select path="noticeStatus" id="noticeStatus" style="width:50%" class="form-control">
                                <form:option value="">请选择</form:option>
                                <form:option value="1">展示中</form:option>
                                <form:option value="2">已下架</form:option>
                            </form:select>
                        </div>

                    </div>
                    <div class="row-fluid" style="margin-top: 8px;">
                        <div class="span5">
                            <label class="label-left control-label"></label>
                            <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="page(1,10)" value="查询" />
                            <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="reset" value="重置" onclick="unset();" />
                        </div>
                        <div class="span5">
                            <label class="label-left control-label">&nbsp;</label>
                            <input id="addNotice"  class="btn  btn-primary" style="width: 26%;" type="button" value="添加公告"  />
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <div class="container-fluid" style="margin-top: 10px">
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead><tr>
                    <th style="width: 5%;">序号</th>
                    <th style="width: 5%;">编号</th>
                    <th style="width: 16%;">公告标题</th>
                    <th style="width: 11%;">频道名称</th>
                    <th style="width: 8%;">公告类型</th>
                    <th style="width: 15%;">公告链接</th>
                    <th style="width: 10%;">创建时间</th>
                    <th style="width: 10%;">发布时间</th>
                    <th style="width: 5%;">状态</th>
                    <th style="width: 10%;">移动</th>
                    <th style="width: 15%;">其他操作</th>
                </tr></thead>
                <tbody>
                <c:forEach items="${page.list}" var="mallNoticeDTO" varStatus="s">
                    <tr>
                            <%-- <td>${mallNoticeDTO.sortNum}</td> --%>
                        <td><c:out value="${s.count}" /></td>
                        <td>${mallNoticeDTO.noticeId}</td>
                        <td>${mallNoticeDTO.noticeTitle}</td>
                        <td>
                        	<c:forEach items="${themeList}" var="theme">
								<c:if test="${theme.id == mallNoticeDTO.themeId}">
									${theme.themeName}
								</c:if>
							</c:forEach>
                        </td>
                        <td>
                            <c:if test="${not empty mallNoticeDTO.noticeType}">
                                <c:if test="${mallNoticeDTO.noticeType=='1'}">
                                    文字公告
                                </c:if>
                                <c:if test="${mallNoticeDTO.noticeType=='2'}">
                                    链接公告
                                </c:if>
                            </c:if>
                        </td>
                        <td style="word-break:break-all;">
                        	<c:if test="${mallNoticeDTO.noticeType=='2'}">
	                        	<a href="${mallNoticeDTO.url}" target="_blank">
	                                    ${mallNoticeDTO.url}
	                            </a>
                        	</c:if>
                            
                        </td>
                        <td>
                            <fmt:formatDate value="${mallNoticeDTO.created}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
                        </td>
                        <td align="center" valign="middle">
                            <c:if test="${mallNoticeDTO.noticeStatus == 1}">
                                <fmt:formatDate value="${mallNoticeDTO.modified}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${mallNoticeDTO.noticeStatus == 1}">
                                展示中
                            </c:if>
                            <c:if test="${mallNoticeDTO.noticeStatus == 2}">
                                已下架
                            </c:if>
                        </td>
                        <td style="width:11%;">
                        	<c:if test="${mallNoticeDTO.isRecommend == 0}">
	                            <a onclick="recommend('${mallNoticeDTO.noticeId}','1')" href="javascript:void(0)">置顶</a>
                        	</c:if>
                        	<c:if test="${mallNoticeDTO.isRecommend == 1}">
	                            <a onclick="recommend('${mallNoticeDTO.noticeId}','0')" href="javascript:void(0)">取消置顶</a>
                        	</c:if>
<%--                             <c:if test="${mallNoticeDTO.isRecommend == 0}"> --%>
                            <a href="javascript:void(0);" onclick="moveNotice('${mallNoticeDTO.noticeId}','${mallNoticeDTO.sortNum}','${mallNoticeDTO.isRecommend}',-1)">上移</a>
<%--                             </c:if> --%>
                            <a href="javascript:void(0);" onclick="moveNotice('${mallNoticeDTO.noticeId}','${mallNoticeDTO.sortNum}','${mallNoticeDTO.isRecommend}',1)">下移</a>
                        </td>

                        <td>
                            <c:if test="${mallNoticeDTO.noticeStatus == 1}">
                                <a onclick="updatestace('${mallNoticeDTO.noticeId}','2')" href="javascript:void(0)">下架</a>
                                <a href="javascript:void(0)" onclick="viewNotice(${mallNoticeDTO.noticeId})" >查看</a>
                            </c:if>
                            <c:if test="${mallNoticeDTO.noticeStatus == 2}">
                                <a onclick="updatestace('${mallNoticeDTO.noticeId}','1')" href="javascript:void(0)">发布</a>
                                <a href="javascript:void(0)" onclick="editNotice(${mallNoticeDTO.noticeId})" >编辑</a>
                                <a href="javascript:void(0);" onclick="deleteNotice('${mallNoticeDTO.noticeId}')">删除</a>
                                <a href="javascript:void(0)" onclick="viewNotice(${mallNoticeDTO.noticeId})" >查看</a>
                            </c:if>
                        </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="pagination text-right">${page}</div>
    </div>
</div>
</body>
</html>