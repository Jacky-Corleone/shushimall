<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>轮播图设置</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<script type="text/javascript">
		$(function()
		{
			if('${msg}' == 'addSuccess')
			{
				$.jBox.info("添加成功！");
			}else if( '${msg}' == 'delSuccess')
			{
				$.jBox.info("删除成功！");
			}else if( '${msg}' == 'editSuccess')
			{
				$.jBox.info("编辑成功！");
			}else if( '${msg}' == 'publishSuccess')
			{
				$.jBox.info("发布成功！");
			}
					
		});
		$(document).ready(function() {
			$("#addButton").click(function(){
				$("#pageNo").val(1);
				$("#pageSize").val(10);
				$("#keyWordsQueryForm").submit();
			});

			$("#title").focus(function(){
				$(this).attr("value", "");
				$(this).style.color=r="#000";
			});
			$("#title").blur(function(){
				if($(this).val()==""){
					$(this).attr("value", "轮播图名称");
					$(this).style.color=r="#999";
				}
			});
		});

        function deleteDto(id)
        {
            if(confirm("你真的要删除吗？"))
            {
            	window.location.href = "${ctx}/station/mallbanner/delete?id=" + id+"&themeType=${dto.bannerType}";
//                 $("#keyWordsQueryForm").attr("action","${ctx}/station/mallbanner/delete?id=" + id+"&themeType=${dto.bannerType}").submit();
            }
        }

        function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#keyWordsQueryForm").attr("action","${ctx}/station/mallbanner/list?themeType=${dto.bannerType}").submit();
	    	return false;
	    }
        function edit(id){
            var url = "${ctx}/station/mallbanner/form?id="+id+"&themeType=${dto.bannerType}";
            parent.openTab(url,"编辑轮播图",id);
        }
        function unset(){ //重置
            $(':input','#keyWordsQueryForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $("#pageNo").val(1);
            $("#pageSize").val(10);
            $("#keyWordsQueryForm").submit();
        }
        
      //发布  下架
    	function updateStatus(id,status){
    		var url = "${ctx}/station/mallbanner/release";
             $.ajax({
                 url : url,
                 type : "post",
                 dataType : "json",
                 data:{
                	 id:id,
                	 status:status
                 },
                 success : function(data) {
                     if(data.success){
                         $.jBox.success("操作成功!",'提示',{closed:function(){
                             $("#keyWordsQueryForm").submit();
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
</head>
<body>

    <div class="content sub-content">
        <div class="content-body content-sub-body">
           <div class="container-fluid">
                <form:form id="keyWordsQueryForm" modelAttribute="dto" name="keyWordsQueryForm" action="${ctx}/station/mallbanner/list?themeType=${dto.bannerType}" method="post" class="breadcrumb form-search">
                	 <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
       				 <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        			 <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                	<div class="row-fluid">
	                   <label>轮播图名称:</label>
                       <form:input id="imgName"  value="${title}" path="title" title="轮播图名称" cssClass="input-medium"></form:input>
	                   <label>频道:</label>
	                   <form:select id="themeId" path="themeId" cssClass="input-medium">
	                       <form:option value="" label="请选择频道"/>
	                       <c:forEach items="${themeList}" var="theme">
	                       		<c:if test="${theme.status==1}">
	                       			<option value="${theme.id}" ${theme.id==themeId?'selected':''}>${theme.themeName}</option>
	                       		</c:if>
	                       </c:forEach>
	                   </form:select>
	                    <input type="button" id="addButton" class="btn btn-primary" value="查询"/>
	                    <input id="btnunset"  class="btn  btn-primary"  type="reset" value="重置" onclick="unset();" />
	                     <a id="addPic" class="btn btn-primary" href="${ctx}/station/mallbanner/form?themeType=${dto.bannerType}">添加轮播图</a>
               		 </div>
                </form:form>
            </div>
            <div class="container-fluid" style="margin-top: 10px">
                <table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed" >
                    <thead><tr>
                        <th style="width: 2%">序号</th>
                        <th style="width: 2%">轮播图名称</th>
                        <th style="width: 2%">频道名称</th>
                        <th style="width: 10%">轮播图图片</th>
                        <th style="width: 10%">指向链接</th>
                        <th style="width: 2%">显示顺序</th>
                        <th style="width: 4%">其他操作</th>
                    </tr></thead>
                    <c:forEach items="${page.list}" var="mallBanner" varStatus="s">
                        <tr>
                            <td><c:out value="${s.count}" /></td>
                            <td>${mallBanner.title}</td>
                            <td>
                            	<c:forEach items="${themeList}" var="theme">
										<c:if test="${theme.id == mallBanner.themeId}">
											${theme.themeName}
										</c:if>
									</c:forEach>
                           	</td>
                            <td><a href="${filePath}${mallBanner.bannerUrl}" target="_blank">${filePath}${mallBanner.bannerUrl}</a></td>
                            <td><a href="${mallBanner.bannerLink}" target="_blank">${mallBanner.bannerLink}</a></td>
                            <td>${mallBanner.sortNumber}</td>
                            <td>
                                <c:if test="${mallBanner.status=='1'}">
                                    <a onclick="updateStatus(${mallBanner.id},0)" href="javascript:void(0);">下架</a>
                                </c:if>
                                <c:if test="${mallBanner.status=='0'}">
                                    <a href="${ctx}/station/mallbanner/form?id=${mallBanner.id}&themeType=${dto.bannerType}" >编辑</a>
                                    <a onclick="updateStatus(${mallBanner.id},1)" href="javascript:void(0);">发布</a>
                                    <a onclick="deleteDto(${mallBanner.id})" href="javascript:void(0)">删除</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="pagination text-right">${page}</div>
        </div>
    </div>
<%--     <form:form id="searchForm" modelAttribute="user" action="${ctx}/station/mallbanner/?themeType=${dto.bannerType}" method="post" > --%>
<%--           <tags:message content="${message}"/> --%>
         
<%--     </form:form> --%>
</body>
</html>