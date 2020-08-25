<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>留言管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function updateRanking(id,cid){
			window.location.href="${ctx}/cms/ranking/form?id="+id+"&cid="+cid;
		}
		
		function deleteRanking(id,cid){
			window.location.href="${ctx}/cms/ranking/delete?id="+id+"&cid="+cid;
		}
		
		function changeRankingList(id){
			$("#cid").val(id);
		}
		
		function form(){
			var cid = $("#rankingListId").val();
			window.location.href="${ctx}/cms/ranking/form?cid="+cid;
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/ranking/list?cid=${rankingListBean.id}">排行榜子信息列表</a></li>
		<li ><a href="javascript:form();">排行榜子信息添加</a></li>
	</ul><br/>
	<form id="searchForm" action="${ctx}/cms/ranking/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="cid" name="cid" type="hidden" value="${rankingListBean.id}"/>
		<label>排行榜类别：</label>
	 <select id="rankingListId" name="rankingListCid" onclick="changeRankingList(this.value)">
			<c:forEach items="${rankingList}" var="rankinglist">
				<option value="${rankinglist.id }" <c:if test="${rankinglist.id ==rankingListBean.id}">selected</c:if>>${rankinglist.cname}</option>
			</c:forEach>
		</select> 
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	网络
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>排序</th>
				<th>品牌名称</th>
				<th>品牌简介</th>
				<th>排名趋势</th>
				<th>类型</th>
				<th>人气</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${empty networkList}">
		<tr>暂无数据</tr>
		</c:if>
		<c:if test="${not empty networkList }">
			<c:forEach items="${networkList}" var="networkRanking" varStatus="status">
				<tr>
					<td>
						${status.index+1}
					</td>
					<td>
						${networkRanking.brand}
					</td>
					<td>
						${networkRanking.brandintro}
					</td>
					<td>${networkRanking.trend}</td>
					<td>
						<c:if test="${networkRanking.type==0}">
							网络排行
						</c:if>
					</td>
					<td>${networkRanking.viewnum}</td>
					<td>
						<a href="javascript:updateRanking('${networkRanking.id}','${networkRanking.cid}');">编辑</a>
						<a href="javascript:deleteRanking('${networkRanking.id}','${networkRanking.cid}');">删除</a>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
	</table>
	线下
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>排序</th>
				<th>品牌名称</th>
				<th>品牌国家</th>
				<th>热卖型号</th>
				<th>品牌占有率</th>
				<th>口碑评分</th>
				<th>类型</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${empty offlineList }">
		<tr>暂无数据</tr>
		</c:if>
		<c:if test="${not empty offlineList}">
			<c:forEach items="${offlineList}" var="offlineRanking" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>${offlineRanking.brand}	</td>
					<td>${offlineRanking.brandCountry}</td>
					<td>${offlineRanking.hotType}</td>
					<td>${offlineRanking.occupancy}</td>
					<td>${offlineRanking.viewnum}</td>
					<td>
					<c:if test="${offlineRanking.type==1}">
					线下排行
					</c:if>
					</td>
					<td>
						<a href="javascript:updateRanking('${offlineRanking.id}','${offlineRanking.cid}');">编辑</a>
						<a href="javascript:deleteRanking('${offlineRanking.id}','${offlineRanking.cid}');">删除</a>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
	</table>
</body>
</html>