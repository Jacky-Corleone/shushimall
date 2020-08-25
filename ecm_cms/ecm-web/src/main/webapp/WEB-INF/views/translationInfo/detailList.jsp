<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>明细查看</title>
<meta name="decorator" content="default"/>

<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<link href="${ctxStatic}/kindeditor/themes/default/default.css" rel="stylesheet" />
<script src="${ctxStatic}/kindeditor/kindeditor.js" type="text/javascript"></script>
<script src="${ctxStatic}/kindeditor/lang/zh_CN.js" type="text/javascript"></script>
<style>
    .row-fluid .span1 {
        width:8.5%;
    }
</style>
<script type="text/javascript">
		$(document).ready(function() {
			if($("#hideId").val()!='null'){
				$("#showId").attr("style", "display:block");
			}
		});
		
		
		
		</script>		
</head>
<body>
<div class="content sub-content">
<div class="content-body content-sub-body">
<form id="goodsForm" name="goodsForm" method="post" action="editS">
 <div class="container-fluid">
 <input name="annex" id="hideId" type="hidden" value="${annex}"/>
            <legend ><span class="content-body-bg">求购信息</span></legend>
            <div class="row-fluid">
            <div class="span4">
                    <label class="label-control" for="cid" title="求购编码">
                       求购编码：
                    </label>
                    <label>${translationNo}</label>
                </div>
                <div class="span4">
                    <label class="label-control" for="cid" title="求购名称">
                       求购名称：
                    </label>
                    <label>${translationName}</label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span4">
                    <label class="label-control" for="cid" title="求购方">
                      求购方：
                    </label>
                    <c:if test="${fn:length(printerName)>22}">
                    <label style="margin-left:50px;margin-top:-20px">${printerName}</label>
                    </c:if>
                    <c:if test="${fn:length(printerName)<=22}">
                    <label style="margin-top:-20px">${printerName}</label>
                    </c:if>
                    
                </div>
                <div class="span4">
                    <label class="label-control" for="cid" title="附件">
                       附件：
                    </label>
                    <label>
                    <div style="display:none" id="showId">
                    <img src="${filePath}${annex}" width="80" height="50">
                    </div>
                    </label>
                </div>
            </div>
  </div>
 <div class="container-fluid">
 
 <legend ><span class="content-body-bg">求购条件</span></legend>
            <div class="row-fluid">
            <div class="span4">
            <label class="label-control" for="cid" title="求购日期">
                     求购日期：
                    </label>
                    <label><fmt:formatDate value="${beginDate}" pattern="yyyy-MM-dd"/></label>
            
            </div>
            
            <div class="span4">
            <label class="label-control" for="cid" title="截止报价日期">
                     截止报价日期：
                    </label>
            <label><fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/></label>
            </div>            
            </div>
            <div class="row-fluid">
            <div class="span4">
            <label class="label-control" for="cid" title="交货日期 ">
                    交货日期：
                    </label>
            <label><fmt:formatDate value="${deliveryDate}" pattern="yyyy-MM-dd"/></label>
            </div>
            
            <div class="span4">
            <label class="label-control" for="cid" title="备注">
                    备注：
                    </label>
                    <label>${remarks}</label>
            
            </div>
            </div>
</div>
<div class="container-fluid">
 <legend ><span class="content-body-bg">求购物品</span></legend>
            <div class="row-fluid">
           <table class="table table-striped table-bordered table-condensed td-cen hhtd" style="width:51%;">
		            <thead>
			            <tr>
			            	<th width="80%">类目名称</th>
			                <th width="40%">物品名称</th>
			                <th width="20%">商品属性</th>
			                <th width="20%">数量</th>
			                <th width="20%">价格</th>
			                <th width="20%">有效时间(始)</th>
			                <th width="20%">有效时间(止)</th>
			            </tr>
		            </thead>
		            <tbody>
		            	<c:forEach items="${details}" var="detail" varStatus="vs">
			            	<tr>
			            		<td>
			            		${detail.category_names}
			            		</td>
			                    <td>${detail.matDesc}</td>
			                    <td>${detail.matAttribute}</td>
			                    <td>${detail.quantity}</td>
			                    <td>${detail.matPrice}</td>
			                    <td>${detail.detailStartDate}</td>
			                    <td>${detail.detailEndDate}</td>
			                </tr>
		                </c:forEach>
		            </tbody>
		        </table>
    </div>
    </div>        
</form>
</div>
</div>
</body>
</html>