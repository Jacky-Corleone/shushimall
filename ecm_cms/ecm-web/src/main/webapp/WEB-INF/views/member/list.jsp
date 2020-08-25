<%--
  Created by IntelliJ IDEA.
  User: menpg
  Date: 2015/3/2
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.camelot.openplatform.common.enums.VipLevelEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>用户查询</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
		// Form所有值清零
        function resetForm(){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            $(':input','#searchForm')
	            .not(':button, :submit, :reset, :hidden')
	            .val('')
	            .removeAttr('checked')
	            .removeAttr('selected');
            search();
        }
        function page(n,s){
        	$.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            $("#pageNo").val(n);
            $("#pageSize").val(s);
        	doSearch();
        }
        function search(){
        	page(1,10);
        }
        function doSearch() {
			$("#searchForm").submit();
        }
        // 会员积分明细和会员成长值明细
        function showDetail(method, title, userId){
            var url = "${ctx}/member/"+ method +"?userId="+userId+"&uid="+userId;
            parent.openTab(url,title,title);
        }
    </script>
<style>
h3 {
	color: #000000;
	height: 46px;
	line-height: 46px;
	text-indent: 20px;
	font-size: 15px;
	font-family: \5FAE\8F6F\96C5\9ED1;
	font-weight: 500;
}

table.td-cen th, .td-cen td {
	text-align: center;
}

.hhtd td {
	word-wrap: break-word;
	word-break: break-all;
}
</style>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<tags:message content="${message}" />
			<form:form id="searchForm"
				commandName="userDTO" class="breadcrumb form-search">
				<%-- 隐藏域 --%>
				<input id="pageNo" name="page"
					type="hidden" value="${page.pageNo}" />
				<input id="pageSize" name="rows"
					type="hidden" value="${page.pageSize}" />
				<%-- end --%>
				<div class="row-fluid" style="margin-top: 10px;">
					<div class="span4">
						<form:label path="uid"
							cssClass="control-label" title="会员编号">会员编号：</form:label>
						<form:input path="uid"
							cssClass="form-control input-medium" maxlength="10"
							onkeyup="this.value=this.value.replace(/\D/g,'')"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							/>
					</div>
					<div class="span4">
						<form:label path="uname"
							cssClass="control-label" title="用户名">用户名：</form:label>
						<form:input path="uname"
							cssClass="form-control input-medium"/>
					</div>
					<div class="span4">
						<form:label path="vipLevel"
							cssClass="control-label" title="会员等级">会员等级：</form:label>
						<form:select path="vipLevel"
							cssClass="form-control input-medium">
							<form:option value="">请选择</form:option>
							<c:set var="vipLevels" value="<%=VipLevelEnum.values()%>" />
							<form:options items="${vipLevels}" itemLabel="name" itemValue="id"/>
						</form:select>
					</div>
				</div>
				<div class="row-fluid" style="margin-top: 8px;">
					<div class="span4">
						<label class="label-left control-label"></label>
						<input
							id="btnquery"
							class="btn  btn-primary" type="button"
							onclick="search()" value="查询" />&#12288;<input
							id="btncancle"
							class="btn  btn-primary" type="button" value="重置"
							onclick="resetForm();" />
					</div>
				</div>
			</form:form>
			<table id="contentTable"
				class="table table-striped table-bordered table-condensed td-cen hhtd">
				<colgroup>
					<col width=""/>
					<col width=""/>
					<col width=""/>
					<col width=""/>
					<col width=""/>
					<col width=""/>
					<col width=""/>
					<col width=""/>
					<col width="200px"/>
				</colgroup>
				<thead>
					<tr>
						<th>序号</th>
						<th>会员编号</th>
						<th>用户名</th>
						<th>手机号</th>
						<th>注册时间</th>
						<th>会员等级</th>
						<th>会员成长值</th>
						<th>会员积分</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="tabletbody">
					<c:forEach items="${page.list}" var="member" varStatus="s">
						<tr>
							<td>${s.count}</td>
							<td>${member.uid}</td>
							<td>${member.uname}</td>
							<td>${member.umobile}</td>
							<td><fmt:formatDate value="${member.created}"
									pattern="yyyy-MM-dd HH:mm" type="both" dateStyle="long" />
							</td>
							<td>${member.vipLevel}</td>
							<td>${member.growthValue}</td>
							<td>${member.totalIntegral}</td>
							<td><c:if test="${member.uid!=''}">
									<a
										onclick="showDetail('growthValueList', '会员成长值明细', ${member.uid})"
										href="javascript:void(0)">成长值明细</a>&#12288;<a
										onclick="showDetail('integralList', '会员积分明细', ${member.uid})"
										href="javascript:void(0)">积分明细</a>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="pagination">${page}</div>
		</div>
	</div>
</body>
</html>
