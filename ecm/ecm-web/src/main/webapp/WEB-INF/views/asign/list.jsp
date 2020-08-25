<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>指派列表</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
</head>
<body>
div class="content sub-content">
<div class="content-body content-sub-body">
    <tags:message content="${message}"/>
    <form name="searchForm" id="searchForm" method="post" action="${ctx}/asign/list"  >
        <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
        <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span3">
                    <label class="label-control" for="uid" title="商家编号">
                        商家编号
                    </label>
                    <input type="text"  name="uid"  value="" title="只能是数字" class="form-control input-medium"/>
                </div>
                <div class="span3">
                    <label class="label-control" for="shopId" title="店铺编号">
                        公司名称
                    </label>
                    <input name="shopId" id="shopId"  type="text" class="form-control input-medium" value="" />
                </div>
                <div class="span5">
                    <label class="label-control" for="startTime" title="申请时间">
                        审核时间
                    </label>
                    <input id="startTime" name="auditorBegin" value='' type="text" class="form-control input-medium Wdate" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly">到
                    <input id="endTime" name="auditorEnd" value='' type="text" class="form-control input-medium Wdate" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly">
                </div>
            </div>
            <div class="row-fluid">
                <div class="span4">
                    <label class="label-control" for="userName" title="商家名称">
                        商家名称
                    </label>
                    <input name="userName" id="userName"  type="text" class="form-control input-medium" value="" />
                </div>
                <div class="span4">
                    <label class="label-control" for="shopName" title="店铺名称">
                        店铺名称
                    </label>
                    <input name="shopName" id="shopName"  type="text" class="form-control input-medium" value="" />
                </div>
            </div>
            <div class="row-fluid">
                <div class="span4">
                    <label class="label-control" for="asignStatus" title="指派状态">
                        指派状态
                    </label>
                    <input name="asignStatus" id="asignStatus"  type="text" class="form-control input-medium" value="" />
                </div>
                <div class="span4">
                    <label class="label-control" for="userId" title="店铺名称">
                        负责人
                    </label>
                    <input name="userId" id="userId"  type="text" class="form-control input-medium" value="" />
                </div>
            </div>
            <div class="row-fluid">
                <div class="span4">
                    <button class="btn btn-primary" id="btnQuery">查询</button>
                </div>
            </div>
        </div>
    </form>

    <table id="treeTable" class="table table-striped table-bordered table-condensed">
        <tr>
            <th>序号</th>
            <th>商家编号</th>
            <th>公司名称</th>
            <th>审核提交时间</th>
            <th>审核状态</th>
            <th>操作</th>
        </tr>

        <c:forEach items="${page.list}" var="item" varStatus="status">
            <tr id="tr${item.uid}" >
                <td>${status.count}</td>
                <td><a href="#">${item.uid}</a></td>
                <td>${item.companyName}</td>
                <td><fmt:formatDate value="${item.updated}" pattern="yyyy-MM-dd"/></td>
                <td>
                        ${item.userAuditStatusLabel}

                </td>
                <td>
                    <c:if test="${3==item.userstatus&&item.auditStatus==1}">
                        <a href="javascript:void(0)" onclick="showApprove(${item.uid})">审核</a>｜
                    </c:if>

                    <a href="#javascript:void(0)" onclick="showZZ(${item.uid})">查看资质</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div class="pagination">${page}</div>
</div>
</div>
<!--审核弹出框-->
<div class="modal hide fade" id="approveDiv">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>审核买家</h3>
    </div>
    <form name="approveForm" id="approveForm" action="${ctx}/apply/saveApprove">
        <input type="hidden" name="uid" value="" id="uid">
        <input type="hidden" name="usertype" id="usertype" value="2">
        <input type="hidden" name="userstatus" id="userstatus">
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span4">商家状态：<span id="userStatusSpan"></span></div>
                <div class="span4"><a id="showZZbtn" href="javascript:void(0)" class="btn btn-primary">查看资质</a></div>
            </div>
            <div class="row-fluid">
                <div class="span8"></div>
            </div>
            <div class="row-fluid">
                <div class="span8">
                    <label class="radio">
                        <input type="radio" name="auditStatus" id="auditStatus1" value="2" checked>
                        审核通过
                    </label>
                    <label class="radio">
                        <input type="radio" name="auditStatus" id="auditStatus2" value="0">
                        驳回申请
                    </label>

                </div>
            </div>

            <div class="row-fluid">
                <div class="span8">
                    <label class="label-control" for="auditRemark" title="备注">备注</label>
                    <textarea rows="3"  name="auditRemark" id="auditRemark"></textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary" id="btnSubApprove">提交</a>
            <a href="#" class="btn" id="btnCloseApprove">取消</a>
        </div>
    </form>
</div>
<!--进度条-->
<div class="modal hide fade" id="popUpDiv">
    <div class="modal-body">
        <div class="progress progress-striped active">
            <div class="bar" style="width: 100%;"></div>
        </div>
    </div>
</div>
</body>
</html>
