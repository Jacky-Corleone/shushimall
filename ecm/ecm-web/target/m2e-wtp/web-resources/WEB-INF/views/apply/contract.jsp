<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>卖家合同</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">
        $(document).ready(function() {
            <%--$("#contractType").setValue("${contract.contractType}");--%>
            <%--$("#contractStatus").setValue("${contract.contractStatus}");--%>
            $("#treeTable").treeTable({expandLevel : 5});
            $("#searchForm").validate({
                rules: {
                    creatorId: {
                        digits: true
                    }
                }
            });
            $("#btnQuery").click(function(){
                if($("#searchForm").valid()){
                    $("#searchForm").submit();
                }
            });
            $("#uploadContract").click(function(){
                var contractFile = $("#contractFile").val();
                if(!contractFile){
                    $.jBox.info("请选择合同上传");
                    return false;
                }
                $("#popUpDiv").modal("show");
                $.ajaxFileUpload({
                            url: '${ctx}/fileUpload/upload?date='+new Date(), //用于文件上传的服务器端请求地址
                            secureuri: false, //是否需要安全协议，一般设置为false
                            fileElementId: 'contractFile', //文件上传域的ID
                            dataType: 'json', //返回值类型 一般设置为json
                            data:{"exts":[".PDF"]},
                            type:"post",
                            success: function (data, status){  //服务器成功响应处理函数
                                $("#popUpDiv").modal("hide");
                                if(data.success){
                                    $("#contractJssAddr").val(data.url);
                                    $.jBox.info("合同上传成功");
                                    var html = "<div class='row-fluid' ><div class='span12'><a href='${filePath}"+data.url+"' target='_blank'>合同连接</a></div></div>";
                                    $("#contractBtndiv").html(html);
                                }else{
                                    $.jBox.info(data.msg);
                                }

                            },
                            error: function (data, status, e){//服务器响应失败处理函数
                                $.jBox.error(e);
                            }
                        }
                );
            });
            $("#uploadContract3").click(function(){
                var contractFile = $("#contractFile3").val();
                if(!contractFile){
                    $.jBox.info("请选择合同上传");
                    return false;
                }
                $("#popUpDiv").modal("show");
                $.ajaxFileUpload({
                            url: '${ctx}/fileUpload/upload?date='+new Date(), //用于文件上传的服务器端请求地址
                            secureuri: false, //是否需要安全协议，一般设置为false
                            fileElementId: 'contractFile3', //文件上传域的ID
                            dataType: 'json', //返回值类型 一般设置为json
                            data:{"exts":[".PDF"]},
                            type:"post",
                            success: function (data, status){  //服务器成功响应处理函数
                                $("#popUpDiv").modal("hide");
                                if(data.success){
                                    $("#contractJssAddr").val(data.url);
                                    $.jBox.info("合同上传成功");
                                    var html = "<div class='row-fluid' ><div class='span12'><a href='${filePath}"+data.url+"' target='_blank'>合同连接</a></div></div>";
                                    $("#contractBtndiv3").html(html);
                                }else{
                                    $.jBox.info(data.msg);
                                }

                            },
                            error: function (data, status, e){//服务器响应失败处理函数
                                $.jBox.error(e);
                            }
                        }
                );
            });
            $("#btnSubContract").click(function(){
                var contractId = $("#contractId1").val();
                if(null==contractId||""==contractId){
                    $.jBox.info("请填写合同编码");
                    $("#contractId1").focus();
                    return false;
                }
                var contractAdd = $("#contractJssAddr").val();
                if(null==contractAdd||""==contractAdd){
                    $.jBox.info("请上传PDF格式合同");
                    return false;
                }
                var contractStarttime = $("#contractStarttime1").val();
                if(contractStarttime==null||contractStarttime==""){
                    $.jBox.info("合同日期开始时间不能为空");
                    $("#contractStarttime1").focus();
                    return false;
                }
                var contractEndtime = $("#contractEndtime1").val();
                if(contractEndtime==null||contractEndtime==""){
                    $.jBox.info("合同日期结束时间不能为空");
                    $("#contractEndtime1").focus();
                    return false;
                }
                var id = $("#contractId").val();

                var contractType = $("#contractType1").val();
                var creatorId = $("#creatorId1").val();

                var dat ="id="+id+ "&contractJssAddr="+contractAdd+"&contractId="+contractId+"&contractType="+contractType+"&contractStarttime="+contractStarttime+"&contractEndtime="+contractEndtime+"&creatorId="+creatorId+"&flag=0";
                $.ajax({
                    url:"${ctx}/contract/updateUrl",
                    data:dat,
                    dataType:"json",
                    success:function(data){
                        //更新列表数据
                        if(data.success){
                            $("#uploadDiv").modal('hide');
                            $.jBox.info("合同上传成功");
                            if(data.contract.contractType==1){
                                $($("#tr"+data.contract.id).children()[1]).html("商家入驻");
                            }
                            if(data.contract.contractType==2){
                                $($("#tr"+data.contract.id).children()[1]).html("续签合同");
                            }
                            if(data.contract.contractType==3){
                                $($("#tr"+data.contract.id).children()[1]).html("调整类目");
                            }

                            $($("#tr"+data.contract.id).children()[2]).html(data.contract.contractId);
                            $($("#tr"+data.contract.id).children()[5]).html(convertDate(new Date(data.contract.modified)));
                            $($("#tr"+data.contract.id).children()[6]).html("待审核");
                            var html = "<a href='javascript:void(0)' onclick='showContract("+data.contract.id+")'>查看合同</a>";
                            $($("#tr"+data.contract.id).children()[7]).html(html);

                        }else{
                            $.jBox.info(data.msg);
                            $("#contractId1").focus();
                        }

                    }
                });
            });
            $("#btnSubContract3").click(function(){
                var contractAdd = $("#contractJssAddr").val();
                if(null==contractAdd||""==contractAdd){
                    $.jBox.info("请上传PDF格式合同");
                    return false;
                }
                var contractId = $("#contractId3").val();
                var id = $("#contractId").val();
                var dat ="id="+id+ "&contractJssAddr="+contractAdd+"&contractId="+contractId+"&flag=1";
                $.ajax({
                    url:"${ctx}/contract/updateUrl",
                    data:dat,
                    dataType:"json",
                    success:function(data){
                        //更新列表数据
                        if(data.success){
                            $("#reuploadDiv").modal('hide');
                            $.jBox.info("合同上传成功");
                            var html = "<a href='javascript:void(0)' onclick='uploadContract2("+data.contract.id+")'>重新上传</a>|<a href='javascript:void(0)' onclick='showContract("+data.contract.id+")'>查看合同</a>";
                            $($("#tr"+data.contract.id).children()[7]).html(html);

                        }else{
                            $.jBox.info(data.msg);
                            $("#contractId1").focus();
                        }

                    }
                });
            });
            $("#confirmContract").click(function(){
                var ifChecked = $("#confirmContract").attr("checked");
                if(ifChecked||ifChecked=='checked'){
                    $("#btnSubContract").attr("disabled",false);
                }else{
                    $("#btnSubContract").attr("disabled","disabled");
                }

            });
            $("#btnCloseUpload").click(function(){
                $("#uploadDiv").modal('hide');
            });
            $("#btnCloseUpload3").click(function(){
                $("#reuploadDiv").modal('hide');
            });
            $("#btnCloseInfo").click(function(){
                $("#contractInfoDiv").modal('hide');
            });

        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        //打开合同上传窗口
        function uploadContract(id,contractId,creatorId){
            $("#contractId").val(id);
            $("#contractId1").val(contractId);
            $("#creatorId1").val(creatorId);

            $("#uploadDiv").modal('show');
            $("#contractBtndiv").html("");
            $($("#auditLogTable tr:not(:first)")).each(function(i,t){
                $(t).remove();
            });
            $.ajax({
                url:"${ctx}/contract/info?id="+id,
                dataType:"json",
                success:function(data){
                    var auditHtml = "";
                    $(data.dataGridContractAudit.rows).each(function (i,item){
                        auditHtml +=   "<tr>" ;
                        auditHtml +=   "<td>"+(i+1)+"</td>" ;

                        if(1==item.status){
                            auditHtml +=   "<td>待上传</td>" ;
                        }
                        if(2==item.status){
                            auditHtml +=   "<td>待审核</td>" ;
                        }
                        if(0==item.status){
                            auditHtml +=   "<td>已驳回</td>" ;
                        }
                        if(3==item.status){
                            auditHtml +=   "<td>已确认</td>" ;
                        }
                        var date = new Date(item.auditDate);
                        auditHtml +=   "<td>"+convertDate(date)+"</td>" ;
                        if(item.remark){
                            auditHtml +=   "<td>"+item.remark+"</td>" ;
                        }else{
                            auditHtml +=   "<td>无</td>" ;
                        }

                        auditHtml +=   "</tr>";
                    });
                    $("#auditLogTable").append(auditHtml);
                }
            });
        }
        //打开合同重新上传窗口
        function uploadContract2(id){
            $("#contractId").val(id);
            $("#reuploadDiv").modal('show');
            $("#contractBtndiv3").html("");
            $($("#auditLogTable3 tr:not(:first)")).each(function(i,t){
                $(t).remove();
            });
            $.ajax({
                url:"${ctx}/contract/info?id="+id,
                dataType:"json",
                success:function(data){
                    $("#contractId3").val(data.contractId);
                    $("#creatorId3").val(data.creatorId);
                    if(1==data.contractType){//1：商家入驻，2：续签，3、调整类目
                        $("#contractType3").val("商家入驻");
                    }
                    if(2==data.contractType){//1：商家入驻，2：续签，3、调整类目
                        $("#contractType3").val("续签");
                    }
                    if(3==data.contractType){//1：商家入驻，2：续签，3、调整类目
                        $("#contractType3").val("调整类目");
                    }
                    $("#contractStarttime3").val(convertDate(new Date(data.contractStarttime)));
                    $("#contractEndtime3").val(convertDate(new Date(data.contractEndtime)));
                    var auditHtml = "";
                    $(data.dataGridContractAudit.rows).each(function (i,item){
                        auditHtml +=   "<tr>" ;
                        auditHtml +=   "<td>"+(i+1)+"</td>" ;
                        if(1==item.status){
                            auditHtml +=   "<td>待上传</td>" ;
                        }
                        if(2==item.status){
                            auditHtml +=   "<td>待审核</td>" ;
                        }
                        if(0==item.status){
                            auditHtml +=   "<td>已驳回</td>" ;
                        }
                        if(3==item.status){
                            auditHtml +=   "<td>已确认</td>" ;
                        }
                        var date = new Date(item.auditDate);
                        auditHtml +=   "<td>"+convertDate(date)+"</td>" ;
                        if(item.remark){
                            auditHtml +=   "<td>"+item.remark+"</td>" ;
                        }else{
                            auditHtml +=   "<td>无</td>" ;
                        }

                        auditHtml +=   "</tr>";
                    });
                    $("#auditLogTable3").append(auditHtml);
                }
            });
        }
        //打开合同详细信息窗口
        function showContract(contractId){
//            $("#contractId").val(contractId);
            $($("#auditTable tr:not(:first)")).each(function(i,t){
                $(t).remove();
            });
            $.ajax({
                url:"${ctx}/contract/info?id="+contractId,
                dataType:"json",
                success:function(data){
                    $("#contractIdInfo").html(data.contractId);
                    $("#userIdInfo").html(data.creatorId);
                    if(data.contractType==1){
                        $("#contractTypeInfo").html("商家入驻");
                    }
                    if(data.contractType==2){
                        $("#contractTypeInfo").html("续签合同");
                    }
                    if(data.contractType==3){
                        $("#contractTypeInfo").html("调整类目");
                    }
                    var date = new Date(data.createdTime);
                    $("#createdTimeInfo").html(convertDate(date));
                    $("#caddres").attr("href",'${filePath}'+data.contractJssAddr);
                    var auditHtml = "";
                    $(data.dataGridContractAudit.rows).each(function (i,item){
                        auditHtml +=   "<tr>" ;
                        auditHtml +=   "<td>"+(i+1)+"</td>" ;
                        if(1==item.status){
                            auditHtml +=   "<td>待上传</td>" ;
                        }
                        if(2==item.status){
                            auditHtml +=   "<td>待审核</td>" ;
                        }
                        if(0==item.status){
                            auditHtml +=   "<td>已驳回</td>" ;
                        }
                        if(3==item.status){
                            auditHtml +=   "<td>已确认</td>" ;
                        }
                        date = new Date(item.auditDate);
                        auditHtml +=   "<td>"+convertDate(date)+"</td>" ;
                        if(item.remark){
                            auditHtml +=   "<td>"+item.remark+"</td>" ;
                        }else{
                            auditHtml +=   "<td>无</td>" ;
                        }
                        auditHtml +=   "</tr>";
                    });
                    $("#auditTable").append(auditHtml);
                }
            });
            $("#contractInfoDiv").modal('show');
        }

        function convertDate(date){
            if(date!=null){
                var year = date.getFullYear();
                var month = (date.getMonth()+1)<10?'0'+(date.getMonth()+1):(date.getMonth()+1);
                var date1 = date.getDate()<10?'0'+date.getDate():date.getDate();
                var hour = date.getHours()<10?'0'+date.getHours():date.getHours();
                var mi = date.getMinutes()<10?'0'+date.getMinutes():date.getMinutes();
                var sec = date.getSeconds()<10?'0'+date.getSeconds():date.getSeconds();
                return  year+"-"+month+"-"+date1+" "+hour+":"+mi+":"+sec;
            }else{
                return "";
            }

        }
    </script>

</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/contract/list"  >
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="companyName" title="公司名称">
                            公司名称
                        </label>
                        <input name="companyName" id="companyName"  type="text" class="form-control" value="${contract.companyName}" />
                    </div>
                    <div class="span6">
                        <label class="label-control" for="creatorId" title="商家编号">
                            商家编号
                        </label>
                        <input type="text"  id="creatorId" name="creatorId" class="form-control"  value="${contract.creatorId}" title="只能是数字"/>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="contractType" title="合同类型">
                            合同类型
                        </label>
                        <select id="contractType" name="contractType">
                            <option value="">全部</option>
                            <c:forEach items="${alltypes}" var="t">
                                <c:choose>
                                    <c:when test="${contract.contractType==t.code}">
                                        <option value="${t.code}" selected="selected">${t.label}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${t.code}">${t.label}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="span6">
                        <label class="label-control" for="contractStatus" title="合同状态">
                            合同状态
                        </label>
                        <select name="contractStatus" id="contractStatus">
                            <option value="">全部</option>
                            <c:forEach items="${allstatus}" var="t">
                                <c:choose>
                                    <c:when test="${contract.contractStatus==t.code}">
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
                    <div class="span6">
                        <label class="label-control" for="createdBegin" title="提交时间">
                            提交时间
                        </label>
                        <input id="createdBegin" name="createdBegin" value="${contract.createdBegin}" type="text" class="form-control input-small Wdate" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createdEnd\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly">到
                        <input id="createdEnd" name="createdEnd" value="${contract.createdEnd}"  type="text" class="form-control input-small Wdate" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createdBegin\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly">
                    </div>
                    <div class="span6">
                        <button class="btn btn-primary" id="btnQuery">查询</button>
                    </div>
                </div>
            </div>
        </form>

            <table id="treeTable" class="table table-striped table-bordered table-condensed">
                <tr>
                    <th>序号</th>
                    <th>合同类型</th>
                    <th>合同编号</th>
                    <th>商家编号</th>
                    <th>公司名称</th>
                    <th>合同提交时间</th>
                    <th>合同状态</th>
                    <th>操作</th>
                </tr>
                <c:forEach items="${page.list}" var="s" varStatus="status">
                    <tr id="tr${s.id}">
                        <td>${status.count}</td>
                        <td>
                            <c:choose>
                                <c:when test="${s.contractType==1}">
                                    商家入驻
                                </c:when>
                                <c:when test="${s.contractType==2}">
                                    续签
                                </c:when>
                                <c:when test="${s.contractType==3}">
                                    调整类目
                                </c:when>
                            </c:choose>
                        </td>
                        <td>${s.contractId}</td>
                        <td>${s.creatorId}</td>
                        <td>${s.companyName}</td>
                        <td><fmt:formatDate value="${s.modified}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td>
                            <c:choose>
                                <c:when test="${1==s.contractStatus}">
                                    待上传
                                </c:when>
                                <c:when test="${0==s.contractStatus}">
                                    已驳回
                                </c:when>
                                <c:when test="${3==s.contractStatus}">
                                    己确认
                                </c:when>
                                <c:when test="${2==s.contractStatus}">
                                    待审核
                                </c:when>
                                <c:otherwise>待上传</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${ null==s.contractStatus || 1==s.contractStatus ||0==s.contractStatus}">
                                    <a href="javascript:void(0)" onclick="uploadContract('${s.id}','${s.contractId}','${s.creatorId}');">上传合同</a>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${3==s.contractStatus}">
                                        <a href="javascript:void(0)" onclick="uploadContract2('${s.id}');">重新上传</a>|
                                    </c:if>
                                    <a href="javascript:void(0)" onclick="showContract(${s.id})">查看合同</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        <div class="pagination">${page}</div>
    </div>
</div>


<input type="hidden" name="contractId" value="" id="contractId">
<input type="hidden" name="contractJssAddr" id="contractJssAddr">

<!--合同上传弹出框-->
<div class="modal hide fade" id="uploadDiv" style="overflow-y: auto">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>合同上传</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <div class="span6">
                <label class="label-control" for="contractId1" title="合同编号">合同编号</label>
                <input type="text" id="contractId1" name="contractId" class="form-control input-medium"/>
            </div>
            <div class="span6">
                <label class="label-control" for="creatorId1" title="商家编号">商家编号</label>
                <input type="text" id="creatorId1" name="creatorId" class="form-control input-medium" readonly/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label class="label-control" for="contractStarttime1" title="合同日期">合同日期</label>
                <input type="text" id="contractStarttime1" name="contractStarttime" class="form-control input-medium Wdate" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'contractEndtime1\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly"/>到
                <input type="text" id="contractEndtime1" name="contractEndtime" class="form-control input-medium Wdate" onclick="WdatePicker({minDate:'#F{$dp.$D(\'contractStarttime1\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <label class="label-control" for="contractType1" title="合同类型">合同类型</label>
                <select id="contractType1" class="input-medium">
                    <c:forEach items="${alltypes}" var="t">
                        <option value="${t.code}">${t.label}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="row-fluid" style="margin-top: 10px">
            <div class="span12 input-append" id="divUpload">
                <label class="label-control" for="contractFile" title="电子版">电子版(PDF)</label>
                <input type="file" id="contractFile" name="file" class="span6 input-file input-medium"/>
                <button class="btn btn-primary" id="uploadContract">上传</button>
            </div>
        </div>
        <div class="row-fluid" id="contractBtndiv">

        </div>
        <div class="row-fluid">
            <div class="span12">
                <input type="checkbox" id="confirmContract"/><span style="color:#FF0000 ">必选</span>
                “合同类型”一经选择，不可更改，请确认相关信息是否准确
            </div>
        </div>

        <div class="row-fluid">
            <table class="table table-striped table-bordered table-condensed" id="auditLogTable">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>审核时状态</th>
                    <th>审核时间</th>
                    <th>审核描述</th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="row-fluid">
            <div class="span4 offset4">
                <button href="#" class="btn btn-primary" id="btnSubContract" disabled="disabled">提交</button>
                <button href="#" class="btn" id="btnCloseUpload">取消</button>
            </div>
        </div>
    </div>
</div>
<!--合同重新上传弹出框-->
<div class="modal hide fade" id="reuploadDiv" style="overflow-y: auto">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>重新上传合同</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <div class="span6">
                <label class="label-control" for="contractId3" title="合同编号">合同编号</label>
                <input type="text" id="contractId3" name="contractId" class="form-control input-medium" readonly/>
            </div>
            <div class="span6">
                <label class="label-control" for="creatorId3" title="商家编号">商家编号</label>
                <input type="text" id="creatorId3" name="creatorId" class="form-control input-medium" readonly/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label class="label-control" for="contractStarttime3" title="合同日期">合同日期</label>
                <input type="text" id="contractStarttime3" name="contractStarttime" class="form-control input-medium"  readonly="readonly"/>到
                <input type="text" id="contractEndtime3" name="contractEndtime" class="form-control input-medium " readonly="readonly"/>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <label class="label-control" for="contractType3" title="合同类型">合同类型</label>
                <input id="contractType3" class="form-control input-medium" type="text" readonly>
            </div>
        </div>
        <div class="row-fluid" style="margin-top: 10px">
            <div class="span12 input-append" id="divUpload3">
                <label class="label-control" for="contractFile" title="电子版">电子版(PDF)</label>
                <input type="file" id="contractFile3" name="file" class="span6 input-file input-medium"/>
                <button class="btn btn-primary" id="uploadContract3">上传</button>
            </div>
        </div>
        <div class="row-fluid" id="contractBtndiv3"> </div>
        <div class="row-fluid">
            <table class="table table-striped table-bordered table-condensed" id="auditLogTable3">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>审核时状态</th>
                    <th>审核时间</th>
                    <th>审核描述</th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="row-fluid">
            <div class="span4 offset4">
                <button href="#" class="btn btn-primary" id="btnSubContract3">提交</button>
                <button href="#" class="btn" id="btnCloseUpload3">取消</button>
            </div>
        </div>
    </div>

</div>
<!--合同信息出框-->
<div class="modal hide fade" id="contractInfoDiv">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>合同基本信息</h3>
    </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span4">
                    <label>合同编号:<span id="contractIdInfo"></span></label>
                </div>
                <div class="span4">
                    <label>商家编号:<span id="userIdInfo"></span></label>
                </div>
                <div class="span4">
                    <label>合同类型:<span id="contractTypeInfo"></span></label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span4">
                    <label>合同日期:<span id="createdTimeInfo"></span></label>
                </div>
                <div class="span4">
                    <label>电子版:<a href="javascript:void(0)" id="caddres" target="_blank">查看</a></label>
                </div>
            </div>
            <legend ><span class="content-body-bg">审核日志</span></legend>
            <div class="row-fluid">
                <table class="table table-striped table-bordered table-condensed" id="auditTable">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>审核时状态</th>
                        <th>审核时间</th>
                        <th>审核描述</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" id="btnCloseInfo">取消</a>
        </div>
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