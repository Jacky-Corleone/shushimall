<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>qq客服维护</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">


        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function addCustomerShow(){
            $("#addCustomerDiv").modal('show');
            $("#addQqCustomerNumber").val("");
        }
        function doAdd(){
            var customerQqNumber = $("#addQqCustomerNumber").val();
            if(!customerQqNumber){
                $.jBox.info("请输入正确的QQ号!");
                return false;
            }
            var reg=/^\d{5,10}$/;
            var r = reg.test(customerQqNumber);
            if(!r){
                $.jBox.info('请输入正确的QQ号!');
                return false;
            }

            //QQ唯一性
            var single = 1;
            $.ajax({
                async: false,
                type: "post",
                dataType: "json",
                url:"${ctx}/qqCustomer/verify",
                data:{customerQqNumber:customerQqNumber},
                success: function(data){
                    //已存在
                    if(data.verify){
                        single = 2;
                    }
                },
                error:function(data){
                    $.jBox.info("系统繁忙，请稍后再试！");
                }
            });
            if(single == 2){
                $.jBox.info("QQ号已存在!");
                return false;
            }

             $.ajax({
                type: "post",
                dataType: "json",
                url:"${ctx}/qqCustomer/add",
                data:{customerQqNumber:customerQqNumber},
                success: function(data){
                    window.location.href = "${ctx}/qqCustomer/list";
                    $.jBox.info(data.message);
                },
                error:function(data){
                    $.jBox.info("系统繁忙，请稍后再试！");
                }
            }); 
        }
        function cancelAdd() {
            $("#addCustomerDiv").modal('hide');
            $("#addQqCustomerNumber").val("");
        }
        function deleteCustomer(CustomerId){
            $.jBox.confirm("您确定要删除客服号吗？","",function (v, h, f) {
                if (v == 'ok'){
                    $.ajax({
                        type: "post",
                        dataType: "json",
                        url:"${ctx}/qqCustomer/delete",
                        data:{id:CustomerId},
                        success: function(data){
                            window.location.href = "${ctx}/qqCustomer/list";
                            $.jBox.info(data.message);
                        },
                        error:function(data){
                            $.jBox.info("系统繁忙，请稍后再试！");
                        }
                    });
                }
                return true; //close
            });
        }

        function updateCustomerShow(updateCustomerId, updateQQCustomerNumber){
            $("#updateCustomerDiv").modal('show');
            $("#updateCustomerId").val(updateCustomerId);
            $("#updateQQCustomerNumber").val(updateQQCustomerNumber);
        }
        function doUpdate(){
            var Id = $('#updateCustomerId').val();
            var customerQqNumber = $('#updateQQCustomerNumber').val();
            if(!customerQqNumber){
                $.jBox.info("请输入正确的QQ号!");
                return false;
            }
            var reg=/^\d{5,10}$/;
            var r = reg.test(customerQqNumber);
            if(!r){
                $.jBox.info('请输入正确的QQ号!');
                return false;
            }
            //QQ唯一性
            var single = 1;
            $.ajax({
                async: false,
                type: "post",
                dataType: "json",
                url:"${ctx}/qqCustomer/verify",
                data:{
                    customerQqNumber:customerQqNumber
                },
                success: function(data){
                    //已存在
                    if(data.verify){
                        single = 2;
                    }
                },
                error:function(data){
                    $.jBox.info("系统繁忙，请稍后再试！");
                }
            });
            if(single == 2){
                $.jBox.info("QQ号已存在!");
                return false;
            }
            $.ajax({
                type: "post",
                dataType: "json",
                url:"${ctx}/qqCustomer/modify",
                data:{id:Id, customerQqNumber:customerQqNumber},
                success: function(data){
                    $.jBox.info(data.message);
                    window.location.href = "${ctx}/qqCustomer/list";
                },
                error:function(data){
                    $.jBox.info("系统繁忙，请稍后再试！");
                }
            });
        }
        function cancelUpdate() {
            $("#updateCustomerDiv").modal('hide');
            $("#updateCustomerId").val("");
            $("#updateQQCustomerNumber").val("");
        }
        //设置默认客服
        function setDefaultCustomer(Id){
            $.jBox.confirm("您确定要设为默认客服吗？","",function (v, h, f) {
                if (v == 'ok'){
                    $.ajax({
                        type: "post",
                        dataType: "json",
                        url:"${ctx}/qqCustomer/setDefaultCustomer",
                        data:{Id:Id},
                        success: function(){
                            window.location.href = "${ctx}/qqCustomer/list";
                        },
                        error:function(data){
                            $.jBox.info("系统繁忙，请稍后再试！");
                        }
                    });
                }
                return true; //close
            });

        }
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <a class="btn btn-primary" href="javascript:void(0);" onclick="addCustomerShow();">添加客服</a>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/qqCustomer/list"  >
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
        </form>
        <table id="treeTable" class="table table-striped table-bordered table-condensed">
            <tr>
                <th>客服腾讯账号</th>
                <th>是否默认客服</th>
                <th>创建日期</th>
                <th>最后修改日期</th>
                <th>操作</th>
            </tr>
            <c:forEach items="${page.list}" var="qqCustomerDTO">
                <tr>
                    <td>${qqCustomerDTO.customerQqNumber}</td>
                    <td>
                        ${qqCustomerDTO.isDefault == 1?'是':'否'}
                    </td>
                    <td><fmt:formatDate value="${qqCustomerDTO.createDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td><fmt:formatDate value="${qqCustomerDTO.lastUpdateDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                    <td>
                        <c:if test="${qqCustomerDTO.isDefault != 1}">
                            <a href="javascript:void(0)" onclick="setDefaultCustomer(${qqCustomerDTO.id})">设为默认客服</a>|
                        </c:if>
                        <a href="javascript:void(0)" onclick="updateCustomerShow(${qqCustomerDTO.id}, ${qqCustomerDTO.customerQqNumber})">修改</a>｜
                        <a href="javascript:void(0)" onclick="deleteCustomer(${qqCustomerDTO.id})">删除</a>
                    </td>

                </tr>
            </c:forEach>
        </table>
        <div class="pagination">${page}</div>
    </div>
</div>

<div class="modal hide fade" id="addCustomerDiv" style="overflow-y:auto">
    <form id="addCustomerForm">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>添加客服</h3>
        </div>
        <div class="modal-body" style="overflow-y: auto; height: 80px">
            <div class="container-fluid">
                <span class="font_wid">腾讯客服账号：</span>
                <input id="addQqCustomerNumber"  class="input_Style2 wid_90 hei_30" type="text" />
               <br/><span class="font_wid">客服QQ请点击 <a style="color:red" href="http://shang.qq.com/v3/widget.html " >立刻开通推广QQ</a>,进行开通QQ客服组件功能.
</span>
            </div>
        </div>
        <div class="modal-footer">
            <a href="javascript:void(0);" class="btn" onclick="doAdd()">保存</a>
            <a href="javascript:void(0);" class="btn" onclick="cancelAdd()">取消</a>
        </div>
    </form>
</div>

<div class="modal hide fade" id="updateCustomerDiv" style="overflow-y:auto">
    <form id="updateCustomerForm">
        <input id="updateCustomerId" name="id" type="hidden">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>修改客服号</h3>
        </div>
        <div class="modal-body" style="overflow-y: auto; height: 80px">
            <div class="container-fluid">
                <span class="font_wid">腾讯客服账号：</span>
                <input id="updateQQCustomerNumber" name="qqCustomerNumber" class="input_Style2 wid_90 hei_30" type="text" />
                <br/><span class="font_wid">客服QQ请点击 <a style="color:red" href="http://shang.qq.com/v3/widget.html " >立刻开通推广QQ</a>,进行开通QQ客服组件功能.
</span>         
            </div>
        </div>
        <div class="modal-footer">
            <a href="javascript:void(0);" class="btn" onclick="doUpdate()">保存</a>
            <a href="javascript:void(0);" class="btn" onclick="cancelUpdate()">取消</a>
        </div>
    </form>
</div>

</body>
</html>