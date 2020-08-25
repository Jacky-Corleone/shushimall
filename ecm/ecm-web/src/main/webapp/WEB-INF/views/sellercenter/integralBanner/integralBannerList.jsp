<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>积分商城轮播图设置</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			// 表格排序
			//tableSort({callBack : page});
            $("#addPic").click(function(){
                var url = "${ctx}/sellercenter/integralBanner/form";
                parent.openTab(url,"添加轮播图",0);
            });
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sellercenter/integralBanner/").submit();
	    	return false;
	    }
        function edit(id){
            var url = "${ctx}/sellercenter/integralBanner/form?id="+id;
            parent.openTab(url,"编辑轮播图",id);
        }
	</script>
</head>
<body>

    <div class="content sub-content">
        <div class="content-body content-sub-body">
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span3 offset9 text-right">
                        <button id="addPic" class="btn btn-primary">添加轮播图</button>
                    </div>
                </div>
            </div>
            <div class="container-fluid" style="margin-top: 10px">

                <table id="contentTable" style="word-break:break-all" class="table table-striped table-bordered table-condensed" >
                    <thead><tr>
                        <th style="width: 2%">序号</th>
                        <th style="width: 2%">编号</th>
                        <th style="width: 2%">banner主题</th>
                        <th style="width: 10%">banner图片</th>
                        <th style="width: 10%">SkuId</th>
                        <th style="width: 10%">积分</th>
                        <th style="width: 2%">显示顺序</th>
                        <th style="width: 4%">其他操作</th>
                    </tr></thead>
                    <c:forEach items="${page.list}" var="mallBanner" varStatus="s">
                        <tr>
                            <td><c:out value="${s.count}" /></td>
                            <td>${mallBanner.id}</td>
                            <td>${mallBanner.title}</td>
                            <td><a href="${filePath}${mallBanner.bannerUrl}" target="_blank">${filePath}${mallBanner.bannerUrl}</a></td>
                            <td>${mallBanner.skuId}</td>
                            <td>${mallBanner.integral}</td>
                            <td>${mallBanner.sortNumber}</td>
                                <%-- <td style="text-align:center" >
                                    <a href="${ctx}/sellercenter/integralBanner/moveUp?id=${mallBanner.id}&sortNum=${mallBanner.sortNumber}">上移</a><br>
                                    <a href="${ctx}/sellercenter/integralBanner/moveDown?id=${mallBanner.id}&sortNum=${mallBanner.sortNumber}">下移</a>
                                </td> --%>
                            <td>
                                    <%--<a href="${ctx}/sellercenter/integralBanner/form?id=${mallBanner.id}" target="_blank">编辑</a>|--%>
                                <c:if test="${mallBanner.status=='1'}">
									<c:choose>
										<c:when test="${mallBanner.startTime>nowTime}">
											<a href="${ctx}/sellercenter/integralBanner/form?id=${mallBanner.id}" >编辑</a>
                                    		<a href="${ctx}/sellercenter/integralBanner/save?id=${mallBanner.id}&dstype=2">发布</a>								
										</c:when>
										<c:otherwise>
											<a href="${ctx}/sellercenter/integralBanner/release?id=${mallBanner.id}">下架</a>
										</c:otherwise>
									</c:choose>
                                </c:if>
                                <c:if test="${mallBanner.status=='0'}">
                                    <a href="${ctx}/sellercenter/integralBanner/form?id=${mallBanner.id}" >编辑</a>
                                    <a href="${ctx}/sellercenter/integralBanner/offShelves?id=${mallBanner.id}">发布</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="pagination text-right">${page}</div>
        </div>
    </div>
    <form:form id="searchForm" modelAttribute="user" action="${ctx}/sellercenter/integralBanner/" method="post" >
        <tags:message content="${message}"/>
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    </form:form>
</body>
</html>