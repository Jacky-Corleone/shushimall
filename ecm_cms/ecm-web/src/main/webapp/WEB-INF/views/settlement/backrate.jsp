<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>返点设置</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#treeTable").treeTable({expandLevel : 5});
            $("#btnQuery").click(function(){
                $("#searchForm").submit();
            });
            $("#dtoForm").validate({
                rules:{
                    categoryId:{
                        required:true,
                        remote:{
                            type:"get",
                            dataType: "json",
                            url:"cidValidate",
                            data:{
                                categoryId:function(){return $("#categoryId3").val();}
                            }
                        }
                    },
                    cashDeposit:{
                        required:true,
                        number:true,
                        min:0
                    },
                    serviceFee:{
                        required:true,
                        number:true,
                        min:0
                    },
                    rebateRate:{
                        required:true,
                        number:true,
                        max:1,
                        min:0
                    }

                },
                messages: {
                    categoryId:{
                        required:"类目不能为空！",
                        remote:jQuery.validator.format("类目相关的扣点记录已经存在，如需要修改请先查找到记录然后修改")
                    },
                    cashDeposit:{
                        required:"保证金不能为空！",
                        number:"费用只能是大于0数字",
                        min:"费用只能是大于0数字"
                    },
                    serviceFee:{
                        required:"服务费不能为空！",
                        number:"费用只能是大于0数字",
                        min:"费用只能是大于0数字"
                    },
                    rebateRate:{
                        required:"扣点不能为空！",
                        number:"扣点只能是大于0数字",
                        max:"扣点不能大于1",
                        min:"扣点不能小于0"
                    }

                }
            });
            $("#btnSubForm").click(function(){
                if($("#dtoForm").valid()){
                    $("#dtoForm").submit();
                }
            });
            $("#btnCloseForm").click(function(){
                $("#addBackRateDiv").modal("hide");
            });
            $("#btnAddBackRate").click(function(){
                loadsub2('1');
                $("#addBackRateDiv").modal("show");
            });

        });
        function editDto(id,fcname,subcname,tcname){
            $("#addBackRateDiv").modal("show");
            $("#dtoId").val(id);
            $("#cashDeposit").val($($("#tr"+id).children()[2]).html());
            $("#serviceFee").val($($("#tr"+id).children()[3]).html());
            $("#rebateRate").val($($("#tr"+id).children()[4]).html());

            var itemHtml = "<label class='label-control' for='categoryId3' title='平台分类'>平台分类</label>";
            itemHtml += "<span class='uneditable-input'>"+fcname+">>"+subcname+">>"+tcname+"</span>";
            $("#editItemDiv").html(itemHtml);
            $("#titleId").html("修改扣点类目信息");

        }
        function deleteR(id){
            if(window.confirm("您确定要删除此条记录？")){
                window.location.href = "${ctx}/settle/deleteBr?id="+id;
            }
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        function loadsub(flag){
            var html = "<option value=''>全部</option>";
            var url;

            switch (flag){
                case '1':
                    var pid = $("#platformId1").val();
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $('#platformId2').select2("val", "");
                    $("#categoryId").html(html);
                    $("#categoryId").select2("val","");
                    break;
                case '2':
                    var pid = $("#platformId2").val();
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $("#categoryId").select2("val","");
                    break;
            }
            $.ajax({
                url:url,
                type:"post",
                dataType:'json',
                success:function(data){
                    $(data).each(function(i,item){
                        html += "<option value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                    });
                    switch (flag){
                        //加载第二级
                        case '1':
                            $("#platformId2").html(html);
                            break;
                        //加载第三级
                        case '2':
                            $("#categoryId").html(html);
                            break;
                    }
                }
            });

        }
        function loadsub2(flag){
            var html = "<option value=''>全部</option>";
            var url;

            switch (flag){
                case '1':
                    url = "${ctx}/brand/getChildCategory?pCid=0";
                    $("#categoryId1").html(html);
                    $("#categoryId1").select2("val", "");
                    $("#categoryId2").html(html);
            		$("#categoryId2").select2("val","");
            		$("#categoryId3").html(html);
            		$("#categoryId3").select2("val","");
                    break;
                case '2':
                    var pid = $("#categoryId1").val();
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $("#categoryId2").select2("val", "");
            		$("#categoryId3").html(html);
            		$("#categoryId3").select2("val","");
                    break;
                case '3':
                    var pid = $("#categoryId2").val();
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $("#categoryId3").html(html);
            		$("#categoryId3").select2("val","");
                    break;
            }
           	$.ajax({
                   url:url,
                   type:"post",
                   dataType:'json',
                   success:function(data){
                       $(data).each(function(i,item){
                           html += "<option value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                       });
                       switch (flag){
                           //加载第一级
                           case '1':
                               $("#categoryId1").html(html);
                               break;
                           //加载第二级
                           case '2':
                               $("#categoryId2").html(html);
                               break;
                           //加载第三级
                           case '3':
                               $("#categoryId3").html(html);
                               break;
                       }
                   }
               });
        }

    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/settle/backrate"  >
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span7">
                        <label class="label-control" for="categoryId" title="平台分类">
                            平台分类
                        </label>
                        <select name="fcid" id="platformId1"  class="form-control input-medium" onchange="loadsub('1')">
                            <option value="">全部</option>
                            <c:forEach items="${platformList}" var="item">
                                <option value="${item.categoryCid}" <c:if test="${item.categoryCid==backrate.fcid}">selected="selected"</c:if> >${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                        <select name="subcid" id="platformId2"  class="form-control input-medium" onchange="loadsub('2')">
                            <option value="">全部</option>
                            <c:forEach items="${subItemList}" var="item">
                                <option value="${item.categoryCid}" <c:if test="${item.categoryCid==backrate.subcid}">selected="selected"</c:if> >${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                        <select name="categoryId" id="categoryId"   class="form-control input-medium">
                            <option value="">全部</option>
                            <c:forEach items="${tItemList}" var="item">
                                <option value="${item.categoryCid}" <c:if test="${item.categoryCid==backrate.tcid}">selected="selected"</c:if> >${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="span3">
                        <button class="btn btn-primary" id="btnQuery">查询</button>
                    </div>
                </div>

            </div>
        </form>
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span6">
                        <button class="btn btn-primary" id="btnAddBackRate">新增扣点类目</button>
                    </div>
                </div>
                <div class="row-fluid"><div class="span12"></div></div>
            </div>
            <table id="treeTable" class="table table-striped table-bordered table-condensed">
                <tr>
                    <th>序号</th>
                    <th>平台类目</th>
                    <th>保证金（元）</th>
                    <th>平台使用费（元）</th>
                    <th>交易扣点</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                <c:forEach items="${page.list}" var="item" varStatus="s">
                    <tr id="tr${item.id}">
                        <td>${s.count}</td>
                        <c:choose>
                            <c:when test="${item.tcmane!=null}">
                                <td>${item.fcname}>>${item.subcname}>>${item.tcmane}</td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                        <td>${item.cashDeposit}</td>
                        <td>${item.serviceFee}</td>
                        <td>${item.rebateRate}</td>
                        <td><fmt:formatDate value="${item.created}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td><a href="javascript:void(0)" onclick="editDto(${item.id},'${item.fcname}','${item.subcname}','${item.tcmane}')">修改</a>｜<a href="javascript:void(0)" onclick="deleteR(${item.id})">删除</a></td>
                    </tr>
                </c:forEach>
            </table>
        <div class="pagination">${page}</div>
    </div>
</div>
<!--新增扣点类目-->
<div class="modal hide fade" id="addBackRateDiv">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3 id="titleId">新增扣点类目</h3>
    </div>
    <form name="dtoForm" id="dtoForm" action="${ctx}/settle/addBackRate" method="post">
        <input type="hidden" name="id" id="dtoId">
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span12" id="editItemDiv">
                    <label class="label-control" for="categoryId3" title="平台分类">
                        平台分类
                    </label>
                    <select name="fcid" id="categoryId1"  class="input-small" onchange="loadsub2('2')">
                    	<option value=''>全部</option>
                    </select>
                    <select name="subcid" id="categoryId2"  class="input-small" onchange="loadsub2('3')">
                    	<option value=''>全部</option>
                    </select>
                    <select name="categoryId" id="categoryId3"   class="input-small">
                    	<option value=''>全部</option>
                    </select>
                </div>

            </div>
            <div class="row-fluid">
                <div class="span6">
                    <label class="label-control" for="cashDeposit" title="保证金">
                        保&nbsp;&nbsp;证&nbsp;&nbsp;金
                    </label>
                    <input type="text" id="cashDeposit" name="cashDeposit" class="input-small">（元）
                </div>
                <div class="span6">
                    <label for="serviceFee" title="平台使用费">
                        平台使用费
                    </label>
                    <input type="text" id="serviceFee" name="serviceFee" class="input-small">（元）
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <label class="label-control" for="rebateRate" title="交易扣点">交易扣点</label>
                    <input type="text" name="rebateRate" id="rebateRate" class="input-small">
                    <span>扣点率必须小于1</span>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span4 offset4">
                    <a href="#" class="btn btn-primary" id="btnSubForm">提交</a>
                    <a href="#" class="btn" id="btnCloseForm">取消</a>
                </div>
            </div>
        </div>

    </form>
</div>
</body>
</html>