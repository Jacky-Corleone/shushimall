<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>求购审核</title>
	<meta name="decorator" content="default"/>
	<style>
        h3{
            color:#000000;
            height: 46px;
            line-height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }
        table.td-cen th,.td-cen td{
            text-align: center;
            vertical-align: middle;
        }.hhtd td{
             word-wrap:break-word;
             word-break:break-all;
         }

    </style>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			 $("#allPass").click(function(){
				 if ($("input[name='checkAll']:checked").size() <= 0) {
					  $.jBox.info("请择数据后进行操作！");
					  return;
					 }
				 if(itemCheck("1")){
				 $.jBox.confirm("确定要审核通过？","提示",function(value){
			  			if(value=='ok'){
			  			// 批量结算
			                var dat = [];
			                $("input:checked:not(#selAll)").each(function(i,t){
			                    dat.push($(t).val());
			                });
			                $.ajax({
			                    url:"${ctx}/translationInfo/approve",
			                    type:"post",
			                    dataType:"json",
			                    data:"ids="+dat+"&status=2",
			                    success:function(data){
			        				$.jBox.prompt('审核通过！', '消息', 'info', { closed: function () { $("#searchForm").submit(); } });
			        				 //$("#searchForm").attr("action","${ctx}/translationInfo/listTranslation").submit();
			        			},
			        			error:function(data){
			        				$.jBox.info(data.result);
			        				 $("#searchForm").attr("action","${ctx}/translationInfo/listTranslation").submit();
			        			}
			                });
			  			}
			  		});
				 }
	            });
		});
		function pagesub(n,s){
				if(n>0){
					
				}else{
					n =1;
				}
				if(s>0){
					
				}else{
					s =10;
				}
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#searchForm").submit();
	    }
		function selectAll() {
			var obj = document.getElementsByName("checkAll");
			if (document.getElementById("selAll").checked == false) {
				for ( var i = 0; i < obj.length; i++) {
					obj[i].checked = false;
				}
			} else {
				for ( var i = 0; i < obj.length; i++) {
					obj[i].checked = true;
				}
			}

		}

		//当选中所有的时候，全选按钮会勾上
		function setSelectAll() {
			var obj = document.getElementsByName("checkAll");
			var count = obj.length;
			var selectCount = 0;

			for ( var i = 0; i < count; i++) {
				if (obj[i].checked == true) {
					selectCount++;
				}
			}
			if (count == selectCount) {
				document.all.selAll.checked = true;
			} else {
				document.all.selAll.checked = false;
			}
		}

		//反选按钮
		function inverse() {
			var checkboxs = document.getElementsByName("checkAll");
			for ( var i = 0; i < checkboxs.length; i++) {
				var e = checkboxs[i];
				e.checked = !e.checked;
				setSelectAll();
			}
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s); 
			$("#searchForm").attr("action","${ctx}/translationInfo/listTranslation").submit();
	    	return false;
	    }
		function pass(id,status){
			$.jBox.confirm("确定要审核通过？","提示",function(value){
	  			if(value=='ok'){
	  				passajax(id,status);
	  			}
	  		});
		}
		function passajax(id,status){
			if(itemCheck("1")){
			$.ajax({
    			url:"${ctx}/translationInfo/approve",
    			type:"post",
    			data:{
    				ids:id,
    				status:status
    			},
    			dataType: "json",
    			success:function(data){
    				$.jBox.prompt('审核通过！', '消息', 'info', { closed: function () { $("#searchForm").submit(); } });
    				 //$("#searchForm").attr("action","${ctx}/translationInfo/listTranslation").submit();
    			},
    			error:function(data){
    				$.jBox.info(data.result);
    				 $("#searchForm").attr("action","${ctx}/translationInfo/listTranslation").submit();
    			}
    		});
			}
		}
		function noPass(id){
				if(id!=0){
					$("input[name='checkAll']").attr("checked",false);
					$("#checkItem_"+id).attr("checked",true);
					$("#tranNo").val(id);
				}else{
					if ($("input[name='checkAll']:checked").size() <= 0) {
						  $.jBox.info("请择数据后进行操作！");
						  return;
						 }
				}
				if(itemCheck("1")){
				$("#rejectId").validate({
	                rules: {
	                	refuleReason:"required"
	                }
	            });
				 $("#rejectDiv").modal('show');
				}
		}
		function rejectAjax(){
			var isValide = $("#rejectId").valid();
			var dat = [];
            $("input:checked:not(#selAll)").each(function(i,t){
                dat.push($(t).val());
            });
	        if(isValide){
	        var auditRemark=$("#rejectRemark").val();
	        $("#popUpDiv").modal('show');
	        $.ajax({
				url : "${ctx}/translationInfo/approve",
				type : "POST",
				data :"ids="+dat+"&status=4&auditRemark="+auditRemark,
				dataType : "json",
				success : function(data) {
					$("#popUpDiv").modal('hide');
					$("#rejectDiv").modal('hide');
					$.jBox.prompt('驳回成功！', '消息', 'info', { closed: function () { $("#searchForm").submit(); } });
				},
				error : function(xmlHttpRequest, textStatus, errorThrown) {
					$("#popUpDiv").modal('hide');
					$.jBox.info("系统错误！请稍后再试！");
				}
			});
	       }
		}
		
		 function rejectClose(){
			 $("#rejectDiv").modal('hide');
		 }
		 function show(id){
	            var url="${ctx}/translationInfo/detailList?translationNo="+id;
	            var title = "查看明细";
	            parent.openTab(url,title,"gs"+id);
	        }
		 function itemCheck(value){
				var flag = true;
				$("input[name='checkAll']:checked").each(function(){
					$(this).attr("status");
					if($(this).attr("status")!=value){
						flag = false;
					}
				});
				if(!flag){
					if(value=="1"){
						$.jBox.info("请保证选择的求购的状态全是待审核状态!");
					}
				}
				return flag;
			}
	</script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
            <tags:message content="${message}"/>
            <form:form name="searchForm" modelAttribute="translationInfoDTO"  id="searchForm" method="post" action="${ctx}/translationInfo/listTranslation"  >
                <input id="pageNo" name="page" type="hidden" value="${pager.pageNo}" />
                <input id="pageSize" name="rows" type="hidden" value="${pager.pageSize}" />
                <div class="row-fluid" style="margin-top:5px">
                <div class="span4" ><label>求购编号：&nbsp;</label><input type="text" value="${translationInfoDTO.translationNo}" name="translationNo"  class="input-medium" style="margin-top:7px"/></div>
                <div class="span4"><label>求购名称：&nbsp;</label><input type="text" value="${translationInfoDTO.translationName}" name="translationName" class="input-medium" style="margin-top:7px"/></div>
                </div>
                <div class="row-fluid">
                <div class="span4">
                    <label>
                                         审核状态：
                    </label>
                    <form:select path="status" cssClass="input-medium">
                        <form:option value=""  label="所有" />
                        <form:option value="1"  label="待审核"/>
                        <form:option value="2"  label="审核通过"/>
                        <form:option value="4"  label="审核驳回"/>
                    </form:select>
                </div>
                <div class="span4">
                <label class="control-label" for="createTimeBegin" title="求购提交时间">
                        提交日期：
              </label>
              <input name="createdstr" id="createdStart" value="${translationInfoDTO.createdstr}" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"  type="text" class="input-small Wdate" />
                    至
              <input name="createdend" id="createdEnd" value="${translationInfoDTO.createdend}" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"  type="text" class="input-small Wdate" />
                </div>
                <div class="span2">
                    <button class="btn btn-primary" onclick="return pagesub();" >搜索</button>
                </div>
                </div>
            </form:form>
        </div>
        <div class="container-fluid">
        <div class="row-fluid">
                <div class="controls">
                    <button class="btn btn-primary" id="allPass">批量通过</button>
                    <button class="btn btn-primary" onclick="noPass('0')" >批量驳回</button>
                </div>
            </div>
            <div class="row-fluid" style="margin-top: 15px">
            <form name="soldOutAll" id="soldOutAll" method="post" action="${ctx}/goodList/soldGoodsOutAll"  >
                <table id="treeTable" style="text-align: center;" class="table table-striped table-bordered table-condensed td-cen hhtd">
                    <tr>
                        <th><input type="checkbox" value="" id="selAll" onclick="selectAll();"  /></th>
                        <th>序号</th>
                        <th style="width: 180px;">求购编号</th>
                        <th style="width: 160px;">求购名称</th>
                        <th >录入方</th>
                        <th >求购提交日期</th>
                        <th>截止报价日期</th>
                        <th>审核状态</th>
                        <th >操作</th>
                    </tr>
					<tbody>
                    <c:forEach items="${pager.list}" var="translation" varStatus="s">
                        <tr id="${translation.translationNo}" >
                            <td><input id="checkItem_${translation.translationNo}" type="checkbox" value="${translation.translationNo}"  status="${translation.status}" name="checkAll" id="checkAll" onclick="setSelectAll();" /></td>
                            <td><c:out value="${s.count}" /></td>
                            <td>${translation.translationNo}</td>
                            <td>${translation.translationName}</td>
                            <td>${translation.alternate1}</td>
                            <td><fmt:formatDate value="${translation.createDate}" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${translation.endDate}" pattern="yyyy-MM-dd"/></td>
                             <td>
                             <c:if test="${translation.status=='1' }">
                                                                                                  待审核
                             </c:if>
                              <c:if test="${translation.status=='2' }">
                                                                                            审核通过
                              </c:if>
                              <c:if test="${translation.status=='4' }">
                                                                                            审核驳回
                              </c:if>
                            </td>
                            <td>
                            <a href="javascript:void(0)" onclick="show('${translation.translationNo}')">查看详情</a>
                            <c:if test="${translation.status=='1'}">
                            <a href="javascript:void(0)" onclick="pass('${translation.translationNo}','2')">通过</a>
                            <a href="javascript:void(0)" onclick="noPass('${translation.translationNo}')">驳回</a>
                            </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </form>
            </div>
            <div class="pagination">${pager}</div>
        </div>
        <!--审核驳回-->
<div class="modal hide fade" id="rejectDiv">
<form id="rejectId">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>买家求购商品驳回</h3>
    </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span8">
                    <input id="tranNo" type="hidden"/>
                    <label class="label-control" for="auditRemark" title="备注">备注</label>
                    <textarea rows="3"  name="refuleReason" id="rejectRemark" style="resize: none;" maxlength="150" ></textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary" id="btnSubApprove" onclick="rejectAjax()">提交</a>
            <a href="#" class="btn" id="btnCloseApprove" onclick="rejectClose()">取消</a>
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