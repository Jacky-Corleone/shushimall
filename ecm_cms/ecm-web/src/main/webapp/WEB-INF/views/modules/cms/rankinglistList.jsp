<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>排行榜管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function deleteRankingList(id){
			var keywords = $("#keywordsId").val();
			window.location.href="${ctx}/cms/rankingList/delete?id="+id+"&keywords="+keywords;
		}
		function updateRankingList(id){
			var keywords = $("#keywordsId").val();
			window.location.href="${ctx}/cms/rankingList/form?id="+id+"&keywords="+keywords;
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/rankingList/list">排行榜列表</a></li>
		<li ><a href="${ctx}/cms/rankingList/form">排行榜添加</a></li>
	</ul><br/>
	<form id="searchForm" action="${ctx}/cms/rankingList/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>关键字 ：</label><input id="keywordsId" name="keywordsId" maxlength="50" class="input-small" value="${keywords}"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>发布</th>
				<th>推荐</th>
				<th>排行榜名称</th>
				<th>标题</th>
				<th>系统类型</th>
				<th>添加时间</th>
				<th>发布时间</th>
				<th>添加人</th>
				<th>排序</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rankingList">
			<tr>
				<td>
					<c:if test="${rankingList.release==1}">
						是
					</c:if>
					<c:if test="${rankingList.release==0}">
						否
					</c:if>
				</td>
				<td>
					<c:if test="${rankingList.recommend==1}">
						是
					</c:if>
					<c:if test="${rankingList.recommend==0}">
						否
					</c:if>
				</td>
				<td><a href="${ctx}/cms/ranking/list?cid=${rankingList.id}">${rankingList.cname}</a></td>
				<td><a href="#">${rankingList.title}</a></td><!-- <fmt:formatDate value="${guestbook.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> -->
				<td>${fns:getDictLabel(rankingList.cateid, 'cms_ranking_list', '无')}</td><!-- ${fns:getDictList('cms_ranking_list')} -->
				<td><fmt:formatDate value="${rankingList.addtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${rankingList.releasetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${rankingList.adminid}</td>
				<td>${rankingList.sort}</td>
				<td>
					<a href="#">预览  </a>
					<a href="#">发布 </a>
					<a href="javascript:updateRankingList('${rankingList.id}');">编辑</a>
					<a href="javascript:deleteRankingList('${rankingList.id}');">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>