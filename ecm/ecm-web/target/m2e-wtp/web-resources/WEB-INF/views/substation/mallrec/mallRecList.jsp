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
                url = "${ctx}/station/mallRec/form？themeType=${mallRecDTO.recTypeDTO}";
                parent.openTab(url,"添加楼层","mr0");
            });
        });
		function isShow(themeType,idDTO,statusDTO,obj) {
			var url = "${ctx}/station/mallRec/modifyStatus";
			 top.$.jBox.confirm("确认要"+$(obj).text()+"吗？","系统提示",function(v,h,f){
	                if(v == "ok"){
	                	$.ajax({
	       	             url : url,
	       	             type : "post",
	       	             dataType : "json",
	       	             data:{
	       	            	 themeType:themeType,
	       	            	 idDTO:idDTO,
	       	            	 statusDTO:statusDTO
	       	             },
	       	             success : function(data) {
	       	                 if(data.success){
	       	                     $.jBox.success("操作成功!",'提示',{closed:function(){
	       	                    	 var pageNo = $("#pageNo").val();
	       	                    	 var pageSize = $("#pageSize").val();
	       	                        page(pageNo,pageSize);
	       	                     }});
	       	                 }else{
	       	                     $.jBox.info(data.msg);
	       	                 }
	       	             },
	       	             error : function(xmlHttpRequest, textStatus, errorThrown) {
	       	                 $.jBox.info("系统错误！请稍后再试！");
	       	             }
	       	         });	                }
	            });
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/station/mallRec/list/?themeType=${mallRecDTO.recTypeDTO}").submit();
	    	return false;
	    }
        function edit(id){
            url = "${ctx}/mallRec/form?idDTO="+id+"&themeType=${mallRecDTO.recTypeDTO}";
            parent.openTab(url,"编辑楼层","mr"+id);
        }
        function deleteSub(id){
            var submit = function (v, h, f) {
                if (v == 'ok') {
                    $.ajax({
                        url : "${ctx}/station/mallRec/delete?themeType=${mallRecDTO.recTypeDTO}",
                        type : "POST",
                        data :{
                            idDTO:id
                        },
                        dataType : "json",
                        success : function(data) {
                            if(data.success){
                                $.jBox.tip(data.msg);
                                window.location.reload();
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

            $.jBox.confirm("是否删除该楼层？", "提示", submit);
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

	<tags:message content="${message}"/>

    <div class="content sub-content">
        <div class="content-body content-sub-body">
        	<form:form id="searchForm" modelAttribute="mallRecDTO" action="${ctx}/station/mallRec/list/" method="post" class="breadcrumb form-search">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				
				<div class="row-fluid">
					<div class="span12">
						<label>楼层：</label>
						<form:input path="titleDTO" cssClass="input-medium"/>
						
						<label>&nbsp; &nbsp;&nbsp;&nbsp;频 &nbsp;&nbsp;&nbsp;&nbsp;道：</label>
	                   <form:select path="themeId" cssClass="input-medium">
	                       <form:option value="" label="请选择频道"/>
	                       <c:forEach items="${themeList}" var="theme">
	                       		<c:if test="${theme.status==1}">
	                       			<option value="${theme.id}" ${theme.id==themeId?'selected':''}>${theme.themeName}</option>
	                       		</c:if>
	                       </c:forEach>
	                   </form:select>
						
						<label>状态：</label>
			            <form:select path="statusDTO" cssClass="input-medium">
			                <form:option value="" label="请选择状态"/>
			                <form:option value="1" label="展示中"/>
			                <form:option value="0" label="已下架"/>
			            </form:select>
					</div>
				</div>
				<div class="row-fluid">
					<div sclass="span12">
							<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				            <input id="btnSubmit" class="btn btn-primary" type="reset" value="重置"  onclick="unset();"/>
				            <a href="${ctx}/station/mallRec/form?themeType=${mallRecDTO.recTypeDTO}"  class="btn btn-primary">添加楼层</a>
		            </div>
	            </div>
			</form:form>
            <div class="container-fluid" style="margin-top: 10px">
                <form name="searchForm" id="searchForm" method="post">

                    <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
                    <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
                    <table id="contentTable" class="table table-striped table-bordered table-condensed">

                        <tr>
                            <th>编号</th>
                            <th>楼层名称</th>
                            <th>频道名称</th>
                            <th>楼层排序</th>
                            <!-- <th>色值</th>
                            <th>背景色值</th> -->
                            <th>创建时间</th>
                            <th>修改时间</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${page.list}" var="mallRecView" varStatus="s">
                            <tr>
                                <td><c:out value="${s.count}" /></td>
                                <td>${mallRecView.mallRec.titleDTO}</td>
                                <td>
                                	<c:forEach items="${themeList}" var="theme">
										<c:if test="${theme.id == mallRecView.mallRec.themeId}">
											${theme.themeName}
										</c:if>
									</c:forEach>
                                </td>
                                <td>${mallRecView.mallRec.floorNumDTO}</td>
                               <%--  <td>${mallRecView.mallRec.colorrefDTO}</td>
                                <td>${mallRecView.mallRec.bgColorDTO}</td> --%>
                                <td><fmt:formatDate value="${mallRecView.mallRec.createdDTO}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                <td><fmt:formatDate value="${mallRecView.mallRec.modifiedDTO}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
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

                                        <a id="cc" href="javascript:void(0)" onclick="isShow('${mallRecDTO.recTypeDTO}','${mallRecView.mallRec.idDTO}','${mallRecView.mallRec.statusDTO}' ,this)">${mallRecView.mallRec.statusDTO==1?'下架':'上架'}</a>

                                        <c:if test="${mallRecView.mallRec.statusDTO==0}">
                                            &nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/station/mallRec/form?idDTO=${mallRecView.mallRec.idDTO}&themeType=${mallRecDTO.recTypeDTO}">编辑</a>
                                            &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="deleteSub(${mallRecView.mallRec.idDTO})">删除</a>
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
