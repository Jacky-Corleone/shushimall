<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>楼层管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
        $(document).ready(function(){
            $("#addRec").click(function(){
                url = "${ctx}/mallRec/form";
                parent.openTab(url,"添加楼层","mr0");
            });
        });
		function isShow(idDTO,statusDTO,obj,floorType,addressType) {
			var url=window.href='${ctx}/mallRec/modifyStatus?idDTO='+idDTO+'&statusDTO='+statusDTO+'&floorType='+floorType+'&addressType='+addressType+'&pageNo='+$("#pageNo").val();
			confirmx('确认要'+$(obj).text()+'吗？',url);
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/mallRec/list/").submit();
	    	return false;
	    }
        function edit(id){
            url = "${ctx}/mallRec/form?idDTO="+id;
            parent.openTab(url,"编辑楼层","mr"+id);
        }
        
        function delShow(idDTO,statusDTO,obj,addressType) {
			var url=window.href='${ctx}/mallRec/delRecById?idDTO='+idDTO+'&addressType='+addressType+'&pageNo='+$("#pageNo").val();
			confirmx('确认要'+$(obj).text()+'吗？',url);
		}
        
	</script>
</head>
<body>

	<tags:message content="${message}"/>

    <div class="content sub-content">
        <div class="content-body content-sub-body">
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span3 offset9 text-right">
                        <a href="${ctx}/mallRec/form?addressType=${addressType}"  class="btn btn-primary">添加楼层</a>
                    </div>
                </div>
            </div>
            <div class="container-fluid" style="margin-top: 10px">
                <form name="searchForm" id="searchForm" method="post">

                    <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
                    <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
                    <table id="contentTable" class="table table-striped table-bordered table-condensed">

                        <tr>
                            <th>编号</th>
                            <th>楼层名称</th>
                            <th>楼层排序</th>
                            <th>创建时间</th>
                            <th>发布时间</th>
                            <th>是否为热卖单品</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${page.list}" var="mallRecView" varStatus="s">
                            <tr>
                                <td><c:out value="${s.count}" /></td>
                                <td>${mallRecView.mallRec.titleDTO}</td>
                                <td>${mallRecView.mallRec.floorNumDTO}</td>
                                <td><fmt:formatDate value="${mallRecView.mallRec.createdDTO}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                <td><fmt:formatDate value="${mallRecView.mallRec.modifiedDTO}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                <td>
                                	  <c:choose>
                                        <c:when test="${mallRecView.mallRec.floorTypeDTO==1}">
                                 			是
                                        </c:when>
                                        <c:otherwise>
                                       		否
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${mallRecView.mallRec.statusDTO==0}">
                                            已下架
                                        </c:when>
                                        <c:when test="${mallRecView.mallRec.statusDTO==1}">
                                            展示中
                                        </c:when>
                                    </c:choose>
                                </td>

                                    <td>
                                        <a href="${ctx}/mallRec/form?idDTO=${mallRecView.mallRec.idDTO}&addressType=${addressType}">编辑</a>
                                        <a id="cc" href="javascript:void(0)" onclick="isShow('${mallRecView.mallRec.idDTO}','${mallRecView.mallRec.statusDTO}' ,this,'${mallRecView.mallRec.floorTypeDTO}','${addressType }')">${mallRecView.mallRec.statusDTO==1?'下架':'上架'}</a>
                                    	<c:if test="${mallRecView.mallRec.statusDTO==0}" >
                                    		<a id="del" href="javascript:void(0)" onclick="delShow('${mallRecView.mallRec.idDTO}','${mallRecView.mallRec.statusDTO}' ,this,'${addressType }')">删除</a>
                                    	</c:if>
                                    	
                                    </td>
                            </tr>
                        </c:forEach>
                    </table>
                </form>
            </div>
            <div class="pagination">${page}</div>
        </div>
    </div>
</body>
</html>
