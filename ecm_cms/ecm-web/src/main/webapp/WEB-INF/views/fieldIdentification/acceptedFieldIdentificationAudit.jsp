<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<script type="text/javascript">
		//查询
		function query(){
			$("#queryForm").submit();
		}
		//选页
		function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#queryForm").submit();
            return false;
        }
		//重置查询条件
		function reset(){
			$("#companyName").val('');
			$("#createDateBegin").val('');
			$("#createDateEnd").val('');
			$("#auditStatus").val('');
		}
		//显示审核窗口
	    function showApprove(approvePrimaryKey){
	    	//设置表单主键字段值
	        $("#approvePrimaryKey").val(approvePrimaryKey);
	        $("#approveDiv").modal('show');
	    }
	  	//隐藏审核窗口
	    function closeApprove(){
	        $("#approveDiv").modal('hide');
	    }
	  	//显示进度条
	    function showProgressBar(){
	    	$("#progressBarDiv").modal("show");
	    }
	  	//隐藏进度条
	    function closeProgressBar(){
	    	$("#progressBarDiv").modal("hide");
	    }
	  	//启用提交审核按钮
	    function enableSubmitButton(){
	    	$("#submitApproveButton").attr("disabled","disabled");
	    }
	  	//失效提交审核按钮
	    function disableSubmitButton(){
	    	$("#submitApproveButton").attr("disabled",false);
	    }
		//提交审核窗口
		function submitApprove(){
			//获取认证通过和驳回认证单选按钮的值
			var auditstatus = $('input:radio[name=auditStatusRadio]:checked').val();
			//如果选择认证通过
            if(auditstatus==2 || auditstatus=='2'){
            	//设置表单审核状态字段值
                $("#approveAuditStatus").val(2);
            }
          	//如果选择驳回认证单选按钮
            if(auditstatus==3 || auditstatus=='3'){
            	//获取审核意见输入框的值
                var auditRemark=$("#auditRemark").val();
            	//驳回认证时审核意见必须填写
                if(!auditRemark){
                    $.jBox.info("请输写审核意见");
                    return false;
                }
              	//设置表单审核状态字段值
                $("#approveAuditStatus").val(3);
            }
          	//隐藏审核窗口
            closeApprove();
          	//显示进度条
          	showProgressBar();
          	//表单提交
            $('#approveForm').ajaxSubmit({
                type:'post',
                dataType:'json',
                success:function(data){
                	//启用提交审核按钮
                	enableSubmitButton();
	                if(data.success && data.auditStatus==2){
	                    $("#auditRemark").val("");
	                    $.jBox.info("审核成功");
	                }
	                if(data.success && data.auditStatus==3){
	                    $("#auditRemark").val("");
	                    $.jBox.info("驳回成功");
	                }
	                window.location.reload();
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){
                	alert("AJAX请求错误!");
                	alert("XMLHttpRequest.status:" + XMLHttpRequest.status);
                }
            })
            //失效提交审核按钮
            disableSubmitButton();
            //隐藏进度条
            closeProgressBar();
		}
		//上传卖家实地认证图片
		function showUpload(userId,extendId,acceptStatus){
			var url = "${ctx}/fieldIdentificationPicture/list?userId="+userId+"&extendId="+extendId+"&acceptStatus="+acceptStatus;
	        var title = "上传实地认证图片";
	        var id = "u"+userId;
	        parent.openTab(url,title,id);
		}
		//查看卖家实地认证信息
		function showAuditDetail(userId){
			var url = "${ctx}/fieldIdentificationAudit/showAuditDetail?userId="+userId;
	        var title = "查看实地认证信息";
	        var id = "u"+userId;
	        parent.openTab(url,title,id);
		}
	</script>
</head>
<body>
	<div class="content sub-content">
	    <div class="content-body content-sub-body">
	        <form id="queryForm" method="post" action="${ctx}/fieldIdentificationAudit/accepted">
	            <input id="pageNo" name="page"  type="hidden" value="${page.pageNo}" />
	            <input id="pageSize" name="rows"  type="hidden" value="${page.pageSize}" />
	            <div class="container-fluid">
	                <div class="row-fluid">
	                    <div class="span5">
	                        <label>企业名称：</label>
	                        <input name="companyName" id="companyName" type="text" value='${auditDTO.companyName}'/>
	                    </div>
	                    <div class="span5">
	                        <label>提交日期：</label>
	                        <input type="text" name="createDateBegin" id="createDateBegin" value='${auditDTO.createDateBegin}' onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createDateEnd\')}',dateFmt:'yyyy-MM-dd'})" class="form-control input-medium Wdate" readonly="readonly">
	                                                                到
                            <input type="text" name="createDateEnd" id="createDateEnd" value='${auditDTO.createDateEnd}' onclick="WdatePicker({minDate:'#F{$dp.$D(\'createDateBegin\')}',dateFmt:'yyyy-MM-dd'})" class="form-control input-medium Wdate" readonly="readonly">
	                    </div>
	                 </div>
                    <div class="row-fluid">
	                    <div class="span3">
	                        <label>审核状态：</label>
	                        <select name="auditStatus" id="auditStatus" class="input-medium">
	                            <option value="">全部</option>
	                            <c:forEach items="${allStatus}" var="t">
	                            	<c:if test="${t.code != 0 && t.code != 4}">
		                                <c:choose>
		                                    <c:when test="${auditDTO.auditStatus==t.code}">
		                                        <option value="${t.code}" selected="selected">${t.label}</option>
		                                    </c:when>
		                                    <c:otherwise>
		                                        <option value="${t.code}">${t.label}</option>
		                                    </c:otherwise>
		                                </c:choose>
                        			</c:if>
	                            </c:forEach>
	                        </select>
                    	</div>
		                <div class="span3">
		                	<a href="#" onclick="query()" class="btn btn-primary">查询</a>
		                    <a href="#" onclick="reset()" class="btn btn-primary">重置</a>
		                </div>
                	</div>
                </div>
	        </form>
			<table id="treeTable" class="table table-striped table-bordered table-condensed">
			    <tr>
			        <th>序号</th>
			        <th>企业名称</th>
			        <th>提交日期</th>
			        <th>审核状态</th>
			        <th>操作</th>
			    </tr>
			    <c:forEach items="${page.list}" var="item" varStatus="status">
			    <tr id="tr${item.id}" >
			    	<td>${status.count}</td>
			        <td>${item.companyName}</td>
			        <td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd"/></td>
			        <td>
			       		<c:if test="${item.auditStatus == 1}">
                        	待审核
                        </c:if>
			       		<c:if test="${item.auditStatus == 2}">
                        	审核通过
                        </c:if>
			       		<c:if test="${item.auditStatus == 3}">
                        	审核驳回
                        </c:if>
			        </td>
			        <td>
			        	<c:if test="${item.auditStatus != 2}">
			         		<a href="#" onclick="showApprove(${item.id})">审核&nbsp;</a>
			         	</c:if>
			         	<a href="#" onclick="showUpload(${item.userId},${item.extendId},'accepted')">上传&nbsp;</a>
			         	<a href="#" onclick="showAuditDetail(${item.userId})">查看</a>
			        </td>
			     </tr>
			 </c:forEach>
			</table>
	        <div class="pagination">${page}</div>
	    </div>
	</div>

	<!--审核窗口Div开始-->
    <div class="modal hide fade" id="approveDiv">
    	<!-- 标题Div开始 -->
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>实地认证审核</h3>
        </div>
        <!-- 标题Div结束 -->
        <!-- 表单Form开始 -->
        <form id="approveForm" action="${ctx}/fieldIdentificationAudit/modifyAuditStatus">
	        <input type="hidden" name="primaryKey" id="approvePrimaryKey">
	        <input type="hidden" name="auditStatus" id="approveAuditStatus">
	        <div class="modal-body">
	            <div class="row-fluid">
	                <div class="span8">
	                    <label class="radio"><input type="radio" name="auditStatusRadio" value="2" checked>认证通过</label>
	                    <label class="radio"><input type="radio" name="auditStatusRadio" value="3">驳回认证</label>
	                </div>
	            </div>
	            <div class="row-fluid">
	                <div class="span8">
	                    <label>审核意见</label>
	                    <textarea rows="3"  name="auditRemark" id="auditRemark"></textarea>
	                </div>
	            </div>
	        </div>
	        <div class="modal-footer">
	            <a id="submitApproveButton" href="#" onclick="submitApprove()" class="btn btn-primary">提交</a>
	            <a href="#" onclick="closeApprove()" class="btn">取消</a>
	        </div>
        </form>
        <!-- 表单Form结束 -->
    </div>
    <!--审核窗口Div结束-->

    <!--进度条Div开始-->
    <div class="modal hide fade" id="progressBarDiv">
        <div class="modal-body">
            <div class="progress progress-striped active">
                <div class="bar" style="width: 100%;"></div>
            </div>
        </div>
    </div>
    <!--进度条Div结束-->
</body>
</html>