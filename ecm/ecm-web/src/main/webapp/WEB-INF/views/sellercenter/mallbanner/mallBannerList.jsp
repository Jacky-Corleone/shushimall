<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>轮播图设置</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">

		var param = '${addressType != null?"?addressType=1":""}';
		var secondparam = '${addressType != null?"&addressType=1":""}';
		$(document).ready(function() {
			// 表格排序
			//tableSort({callBack : page});
            $("#addPic").click(function(){
                var url = "${ctx}/sellercenter/mallbanner/form" + param;
                parent.openTab(url,"添加轮播图",0);
            });
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sellercenter/mallbanner/" + param).submit();
	    	return false;
	    }
        function edit(id){
            var url = "${ctx}/sellercenter/mallbanner/form?id="+id+secondparam;
            parent.openTab(url,"编辑轮播图",id);
        }
	</script>
</head>
<body>

    <div class="content sub-content">
        <div class="content-body content-sub-body">
	        <form:form id="searchForm" commandName="mallBannerDTO" action="${ctx}/sellercenter/mallbanner/list" class="breadcrumb form-search"  method="post" >
		        <tags:message content="${message}"/>
		        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		       	<input id="addressType" name="addressType" type="hidden" value="${addressType}" />
		    	
	            <div class="container-fluid">
		       		<div class="row-fluid">
		       			<c:if test="${addressType == 1}">
		                    <div class="span4">
		                    	<label>子站名称：</label>
		                    	<form:select path="themeId">
		                    		<form:option value="" label="--请选择--"></form:option>
		                    		<form:options items="${themeList}" itemLabel="themeName" itemValue="id"/>
		                    	</form:select>
		                    </div>
		                 </c:if>
		                 <div class="span4">
		                   	<label>轮播图名称：</label>
		                   	<form:input path="title"/>
		                 </div>
						<div class="span2">
							<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
						</div>
					</div>
		        </div>
	            <div class="container-fluid">
	                <div class="row-fluid">
	                    <div class="span3 offset9 text-right">
	                        <button id="addPic" class="btn btn-primary">添加轮播图</button>
	                    </div>
	                </div>
	             </div>
		   </form:form>
            <div class="container-fluid" style="margin-top: 10px">
                <table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed" >
                    <thead><tr>
                        <th style="width: 2%">序号</th>
                        <th style="width: 2%">编号</th>
                        <th style="width: 2%">banner主题</th>
                        <th style="width: 10%">banner图片</th>
                        <th style="width: 10%">指向链接</th>
                        <th style="width: 2%">显示顺序</th>
                        <th style="width: 4%">其他操作</th>
                    </tr></thead>
                    <c:forEach items="${page.list}" var="mallBanner" varStatus="s">
                        <tr>
                            <td><c:out value="${s.count}" /></td>
                            <td>${mallBanner.id}</td>
                            <td>${mallBanner.title}</td>
                            <td><a href="${filePath}${mallBanner.bannerUrl}" target="_blank">${filePath}${mallBanner.bannerUrl}</a></td>
                            <td><a href="<c:choose><c:when test="${fn:contains(mallBanner.bannerLink,'http')}"></c:when>
                            <c:otherwise>http://</c:otherwise></c:choose>${mallBanner.bannerLink}" target="_blank">${mallBanner.bannerLink}</a></td>
                            <td>${mallBanner.sortNumber}</td>
                                <%-- <td style="text-align:center" >
                                    <a href="${ctx}/sellercenter/mallbanner/moveUp?id=${mallBanner.id}&sortNum=${mallBanner.sortNumber}">上移</a><br>
                                    <a href="${ctx}/sellercenter/mallbanner/moveDown?id=${mallBanner.id}&sortNum=${mallBanner.sortNumber}">下移</a>
                                </td> --%>
                            <td>
                                    <%--<a href="${ctx}/sellercenter/mallbanner/form?id=${mallBanner.id}" target="_blank">编辑</a>|--%>
                                <c:if test="${mallBanner.status=='1'}">
                                    <a href="${ctx}/sellercenter/mallbanner/release?id=${mallBanner.id}${addressType != null?"&addressType=1":""}">下架</a>
                                </c:if>
                                <c:if test="${mallBanner.status=='0'}">
                                    <a href="${ctx}/sellercenter/mallbanner/form?id=${mallBanner.id}${addressType != null?"&addressType=1":""}" >编辑</a>
                                    <a href="${ctx}/sellercenter/mallbanner/offShelves?id=${mallBanner.id}${addressType != null?"&addressType=1":""}">发布</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="pagination text-right">${page}</div>
        </div>
    </div>
</body>
</html>