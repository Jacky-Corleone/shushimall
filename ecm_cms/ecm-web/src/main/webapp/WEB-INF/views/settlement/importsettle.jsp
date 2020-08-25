<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>结算单导入</title>
    <meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script>
        $(document).ready(function(){
            $("#btnQuery").click(function(){
                $("#seachResultFrom").submit();
            });
        });
        function importSettlement(thiz){
        	var type = ''
        	if(thiz.id=='importAlipayBtn'){
        		type = 'AP';
        	}else if(thiz.id=='importWXBtn'){
        		type = 'WX';
        	}else{
        		type = 'CB';
        	}
        	$.ajaxFileUpload({
                url: '${ctx}/settle/importSettle?type='+type, //用于文件上传的服务器端请求地址
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'file', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                type:"post",
                success: function (data, status){  //服务器成功响应处理函数
                    if(data.success){
                        $.jBox.info(data.msg);
                        $("#searchForm").submit();
                    }else{
                        $.jBox.error(data.msg);
                    }

                },
                error: function (data, status, e){//服务器响应失败处理函数
                    $.jBox.error(e);
                }
            }
    );
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/settle/import/").submit();
        }
    </script>
</head>
<body>
    <div class="content sub-content">
        <div class="content-body content-sub-body">
            <div class="container-fluid">
                <form  name="impForm" id="impForm">
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="file" title="请选择文件">
                            请选择文件
                        </label>
                        <input type="file" id="file" name="file" />
                        <span style="color:red;">*只能导入后缀名为.xlsx的excel文件</span>
                    </div>
                    <div class="span6">
                        <a href="${ctxStatic}/settle_imp_template.xls" class="btn btn-info" target="_blank">京东支付模板下载</a>
                        <a href="${ctxStatic}/settle_alipay_imp_template.xlsx" class="btn btn-info" target="_blank">支付宝模板下载</a>
                        <a href="${ctxStatic}/settle_weixin_imp_template.xlsx" class="btn btn-info" target="_blank">微信模板下载</a>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                       <button class="btn btn-primary" type="button" id="importBtn" onclick="importSettlement(this)">导入（京东支付）</button>
                       <button class="btn btn-primary" type="button" id="importAlipayBtn" onclick="importSettlement(this)">导入（支付宝）</button>
                       <button class="btn btn-primary" type="button" id="importWXBtn" onclick="importSettlement(this)">导入（微信）</button>
                    </div>
                </div>
                </form>
                <form id="seachResultFrom" action="${ctx}/settle/import">
	                <div class="row-fluid">
	                    <div class="span4">
	                        <label class="label-control" for="settlement_id" title="对外交易编号">
	                            对外交易编号:
	                        </label>
	                        <input id="outTradeNo" name="outTradeNo" value="${outTradeNo}" type="text" class="input-medium" title="对外交易编号只能是数字">
	                    </div>
	
	                
	                    <div class="span6">
	                        <label class="label-control" for="havePaidDatestr" title="交易完成日期">
	                            交易完成日期:
	                        </label>
	                        <input id="comletedTimeStart" name="comletedTimeStart" value='<fmt:formatDate value="${comletedTimeStart}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-small Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly="readonly">到
	                        <input id="comletedTimeEnd" name="comletedTimeEnd" value='<fmt:formatDate value="${comletedTimeEnd}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-small Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly="readonly">
	                    </div>
	                    <div class="span2">
	                        <button class="btn btn-primary" id="btnQuery">查询</button>
	                    </div>
	                </div>
	            </div>
                
                </form>
            </div>
            <div class="container-fluid">
                <form id="searchForm" name="searchForm" method="post">
                    <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
                    <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
                <table id="treeTable" class="table table-striped table-bordered table-condensed">
                    <tr>
                        <th>对外交易编号</th>
                        <th>订单金额</th>
                        <th>手续费</th>
                        <th>订单状态</th>
                        <th>清算状态</th>
                        <th>交易完成时间</th>
                        <th>银行返回时间</th>
                        <th>导入银行</th>
                        <th>处理标记</th>
                        <th>备注1</th>
                        <th>备注2</th>
                        <th>导入用户ID</th>
                        <th>创建时间</th>
                    </tr>
                    <c:forEach items="${page.list}" var="item" varStatus="s">
                        <tr id="tr${item.id}">
                            <td>${item.outTradeNo}</td>
                            <td>${item.orderAmount}</td>
                            <td>${item.factorage}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.orderStatus==1}">失败</c:when>
                                    <c:when test="${item.orderStatus==2}">成功</c:when>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.liquidateStatus==1}">未清算</c:when>
                                    <c:when test="${item.liquidateStatus==2}">已清算</c:when>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${item.completedTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><fmt:formatDate value="${item.callBankTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.bankType=='CB'}">京东支付</c:when>
                                    <c:when test="${item.bankType=='AP'}">支付宝</c:when>
                                    <c:when test="${item.bankType=='WX'}">微信</c:when>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.dealFlag==1}">未处理</c:when>
                                    <c:otherwise>已处理</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${item.remark1}</td>
                            <td>${item.remark2}</td>
                            <td>${item.createId}</td>
                            <td>
                                <fmt:formatDate value="${item.created}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
