<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>卖家认证信息</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#searchForm").validate({
                rules: {
                    uid: {
                        digits: true
                    }
                }
            });
            $("#treeTable").treeTable({expandLevel : 5});
            $("#btnQuery").click(function(){
                if($("#searchForm").valid()){
                    $("#searchForm").attr("action","${ctx}/apply/sellerinfo");
                    $("#searchForm").submit();
                }
            });
            <%--$("#btnExport").click(function(){--%>
                <%--if($("#searchForm").valid()){--%>
                    <%--var url = "${ctx}/apply/exportUser?flag=0";--%>
                    <%--$("#searchForm").attr('action',url);--%>
                    <%--$("#searchForm").submit();--%>

                <%--}--%>
            <%--});--%>
            <%--$("#btnExportAll").click(function(){--%>
                <%--if($("#searchForm").valid()){--%>
                    <%--var url = "${ctx}/apply/exportUser?flag=1";--%>
                    <%--$("#searchForm").attr('action',url);--%>
                    <%--$("#searchForm").submit();--%>

                <%--}--%>
            <%--});--%>

            $("#approveForm").ajaxForm({
                url:'${ctx}/apply/saveApprove',
                type:'post',
                dataType:'json'
            }).submit(function(){
                $(this).ajaxSubmit();
                return false;
            });
            $("#btnSubApprove").click(function(){
                var auditstatus = $('input:radio[name=auditStatus]:checked').val();
                if(auditstatus==0||auditstatus=='0'){
                    var auditRemarkmen=$("#auditRemark").val();
                    if(!auditRemarkmen){
                        $.jBox.info("请输入审核意见");
                        return;
                    }
                    $("#userstatus").val(5);
                }
                if(auditstatus==2||auditstatus=='2'){
                    $("#userstatus").val(6);
                }

                $("#popUpDiv").modal("show");
                $("#approveDiv").modal('hide');
                //提交审核

                $('#approveForm').ajaxSubmit({
                    type:'post',
                    dataType:'json',
                    success:function(data){
//                    data = $.parseJSON(data);
                    $("#popUpDiv").modal("hide");
                    $("#btnSubApprove").attr("disabled","disabled");
                    var html = "<a href='#javascript:void(0)' onclick='showZZ("+data.uid+")'>查看资质</a>";
                    var uid = $("#uid").val();
                    if(data.success&&data.auditStatus==0){
                        $("#auditRemark").val("");
                        $.jBox.info("驳回成功");
                        $($("#tr"+uid).children()[4]).html("已驳回");
                        $($("#tr"+uid).children()[7]).html(html);
                    }
                    if(data.success&&data.auditStatus==2){
                        $("#auditRemark").val("");
                        $.jBox.info("审核成功");
                        $($("#tr"+uid).children()[4]).html("审核通过");
                        $($("#tr"+uid).children()[7]).html(html);
                    }
                    if(!data.success){
                        $.jBox.error(data.msg);
                    }

                    $("#btnSubApprove").attr("disabled",false);
                }});
            });
            $("#btnCloseApprove").click(function(){
                $("#approveDiv").modal('hide');
            });
            $("#showZZbtn").click(function(){
                var uid = $("#uid").val();
                showZZ(uid);
            });
        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/apply/sellerinfo");
            $("#searchForm").submit();
            return false;
        }

        //打开审核认证窗口
        function showApprove(uid){
            $("#userStatusSpan").html($($("#tr"+uid).children()[4]).html());
            $("#uid").val(uid);
            $("#approveDiv").modal('show');
        }
        //查看被选择人资质
        function showZZ(uid){
            var url = "${ctx}/apply/detailm?uid="+uid;
//            $("#contractDetailDiv").modal('show');
//            $("#detailIframe").attr("src",url);
            var title = "查看卖家资质";
            var id = "u"+uid;
            parent.openTab(url,title,id);
        }
        function frozeUser(uid,flag){//1冻结，0解冻
            var title = "";
            if(flag==0){
                title = "确认要解除冻结吗";
            }else{
                title = "确认要冻结账号吗";
            }
            top.$.jBox.confirm(title,"系统提示",function(v,h,f){
                if(v == "ok"){
                    $.ajax({
                        url:"${ctx}/apply/frozeUser",
                        data:"uid="+uid+"&flag="+flag,
                        dataType:"json",
                        success:function(data){
                            if(data.success){
                                if(flag==0){
                                    $.jBox.info("解除冻结成功");
                                    $($("#tr"+uid).children()[5]).html("可用");
                                    var html = "<a href='#javascript:void(0)' onclick='frozeUser("+uid+",1)'>冻结</a>";
                                    $($("#tr"+uid).children()[7]).html(html);
                                }
                                if(flag==1){
                                    $.jBox.info("冻结成功");
                                    $($("#tr"+uid).children()[5]).html("已冻结");
                                    var html = "<a href='#javascript:void(0)' onclick='frozeUser("+uid+",0)'>解冻</a>";
                                    $($("#tr"+uid).children()[7]).html(html);
                                }
                            }else{
                                $.jBox.error(data.msg);
                            }
                        }
                    });
                }
            });
        }
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/apply/sellerinfo"  >
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <input name="usertype" value="" type="hidden">
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span3">
                        <label class="label-control" for="uid" title="商家编号">
                            商家编号：
                        </label>
                        <input type="text"  id="code" name="uid"  value="${sellerinfo.uid}" title="只能是数字" class="form-control input-medium"/>
                    </div>
                    <div class="span3">
                        <label class="label-control" for="companyName" title="公司名称">
                            公司名称：
                        </label>
                        <input name="companyName" id="companyName"  type="text" class="form-control input-medium" value="${sellerinfo.companyName}" />
                    </div>
                    <div class="span3">
                        <label class="label-control" for="auditStatus" title="审核状态">
                            审核状态：
                        </label>
                        <select name="auditStatus" id="auditStatus" class="input-medium">
                            <option value="">全部</option>
                            <c:forEach items="${allstatus}" var="t">
                                <c:choose>
                                    <c:when test="${sellerinfo.auditStatus==t.code}">
                                        <option value="${t.code}" selected="selected">${t.label}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${t.code}">${t.label}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>

                        </select>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span5">
                        <label class="label-control" for="startTime" title="审核时间">
                            审核时间：
                        </label>
                        <input id="startTime" name="auditorBegin" value='${sellerinfo.auditorBegin}' type="text" class="form-control input-medium Wdate" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly">到
                        <input id="endTime" name="auditorEnd" value='${sellerinfo.auditorEnd}' type="text" class="form-control input-medium Wdate" onclick="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly">
                    </div>
                    <div class="span1">
				    </div>
                    <div class="span3">
								<label class="label-left control-label" for="platformId"
									title="平台类型">平台类型：</label>
								<select name="platformId" id="platformId"
									class="input-medium">
									<option value="">所有</option>
									<option value="0" <c:if test="${sellerinfo.platformId==0}">selected="selected"</c:if>>舒适100平台 </option>
<%-- 									<option value="2" <c:if test="${sellerinfo.platformId==2}">selected="selected"</c:if>>绿印平台 </option> --%>
								</select>
				</div>
                    <div class="span3">
                        <button class="btn btn-primary" id="btnQuery">查询</button>
                        <%--<button class="btn btn-primary" id="btnExport">导出</button>--%>
                        <%--<button class="btn btn-primary" id="btnExportAll">导出所有</button>--%>
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
                <th>账号状态</th>
                <th>平台类型</th>
                <th>操作</th>
                <th>账号操作</th>
            </tr>

            <c:forEach items="${page.list}" var="item" varStatus="status">
                <tr id="tr${item.uid}" >
                    <td>${status.count}</td>
                    <td>${item.uid}</td>
                    <td>${item.companyName}</td>
                    <td><fmt:formatDate value="${item.updated}" pattern="yyyy-MM-dd"/> </td>
                    <td>
                        ${item.userAuditStatusLabel}

                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.deleted==0}">
                                可用
                            </c:when>
                            <c:when test="${item.deleted==1}">
                                已冻结
                            </c:when>
                        </c:choose>

                    </td>
                    <td>
                            <c:choose>
                                <c:when test="${item.platformId==null}">舒适100平台</c:when>
<%--                                 <c:when test="${item.platformId==2}">绿印平台</c:when> --%>
                            </c:choose>
                    </td>
                    <td>
                        <c:if test="${5==item.userstatus&&item.auditStatus!=0}">
                            <a href="javascript:void(0)" onclick="showApprove(${item.uid})">审核</a>｜
                        </c:if>

                        <a href="#javascript:void(0)" onclick="showZZ(${item.uid})">查看资质</a>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.deleted==0}">
                                <a href="#javascript:void(0)" onclick="frozeUser(${item.uid},1)">冻结</a>
                            </c:when>
                            <c:when test="${item.deleted==1}">
                                <a href="#javascript:void(0)" onclick="frozeUser(${item.uid},0)">解冻</a>
                            </c:when>
                        </c:choose>
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
        <h3>审核卖家</h3>
    </div>
    <form name="approveForm" id="approveForm" action="${ctx}/apply/saveApprove">
        <input type="hidden" name="uid" value="" id="uid">
        <input type="hidden" name="usertype" id="usertype" value="3">
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