<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>选择标签</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

		$(document).ready(function(){
			//top.abcd();

		});

		function view(href){
			top.$.jBox.open('iframe:'+href,'查看文章',$(top.document).width()-220,$(top.document).height()-120,{
				buttons:{"关闭":true},
				loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
					$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
				}
			});
			return false;
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<div style="margin:10px;">
	<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/image/selectList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>栏目：</label><tags:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
					title="栏目" url="/cms/category/treeData" module="article" cssClass="input-small"/>
		<%--<label>标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;--%>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
	</form:form>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th style="text-align:center;">选择</th><th>标签名</th><th>类型</th><th>描述</th></tr></thead>
			<tbody>
			<c:forEach items="${page.list}" var="article">
				<tr>
					<td style="text-align:center;"><input type="checkbox" name="checkboxs" value="${article.tagid}" /></td>
						<%--<td><a href="${ctx}/cms/article/form?id=${article.tagid}" title="${article.tagid}" onclick="return view(this.href);">${fns:abbr(article.tagid,40)}</a></td>--%>
					<td>${article.tagname}</td>
					<td>${article.type}</td>
					<td>${article.art_seo}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	<div class="pagination">${page}</div>
	</div>
<script>
	window.onload=function(){

			$('.jbox-button').on('click',function(){
				var obj=$("input[name=id]");
				var arr=[];
				for(var i=0; i<obj.length;i++){
					if(obj.eq(i).checked = true)
					{
						arr.push(i);
					}
				}
				alert(arr);

			})
	}
</script>
</body>
</html>