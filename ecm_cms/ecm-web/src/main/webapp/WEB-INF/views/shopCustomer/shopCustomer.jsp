<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>维护客服</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">
		var previouskey = 0;
        $(document).ready(function() {
            $("#treeTable").treeTable({expandLevel : 5});
            $("#btnQuery").click(function(){
                $("#searchForm").submit();
            });
            $("#shopId").keydown(
   				function(event) {
   					if(!event.shiftKey){
   						if ((event.which > 47 && event.which < 58)
   								|| event.which == 8 || event.which == 37 || event.which == 39 ) {
   							return true;
   						} else if (event.which == 86 && previouskey == 17) {
   							return true;
   						}
   					}
   					previouskey = event.which;
   					
   					return false;
   				});
               
               // 禁止粘贴非数字的字符
               $('#shopId').bind('paste',function(e){
   				var pastedText = undefined;
   				if (window.clipboardData && window.clipboardData.getData) { // IE
   					pastedText = window.clipboardData.getData('Text');
   				} else {
   					pastedText = e.originalEvent.clipboardData.getData('Text');//e.clipboardData.getData('text/plain');
   				}
   				if (/^[0-9]*$/.test(pastedText)) {
   					return true;
   				} else {
   					return false;
   				}
   		    });

        });

        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function addShopCustomerShow(addShopId, addShopName){
        	$("#addShopCustomerDiv").modal('show');
        	$("#addShopId").val(addShopId);
        	$("#addShopName").val(addShopName);
        }
        function doAdd(){
    		$.ajax({
    			data:$("#addShopCustomerForm").serializeArray(),
    			dataType: "json",
    			success: function(data){
    				alert(data.messages);
    				if(data.ifUnique == true){
	    				$("#addShopCustomerDiv").modal('hide');
	    				window.location.href="listShopCustomer";
    				}
    			},
    			type: "POST",
    			url:"addShopCustomer"
    		});
    	}
        function cancelAdd() {
        	$("#addShopCustomerDiv").modal('hide');
        	$("#addShopId").val("");
        	$("#addShopName").val("");
        	$("#addStationId").val("");
    	}

        function deleteShopCustomer(shopCustomerId){
        	if(confirm("您确定要删除商铺站点吗？")){
        		$.ajax({
        			data: {
        				shopCustomerId: shopCustomerId
        			},
        			dataType: "json",
        			success: function(data){
        				alert(data.messages);
        				window.location.href="listShopCustomer";
        			},
        			type: "POST",
        			url:"deleteShopCustomer"
        		});
        	}
        }

        function updateShopCustomerShow(updateShopCustomerId, updateShopId, updateShopName, updateStationid){
        	$("#updateShopCustomerDiv").modal('show');
        	$("#updateShopCustomerId").val(updateShopCustomerId);
        	$("#updateShopId").val(updateShopId);
        	$("#updateShopName").val(updateShopName);
        	$("#updateId").val(updateStationid);
        }
        function doUpdate(){
    		$.ajax({
    			data:$("#updateShopCustomerForm").serializeArray(),
    			dataType: "json",
    			success: function(data){
    				alert(data.messages);
    				if(data.ifUnique == true){
	    				$("#updateShopCustomerDiv").modal('hide');
	    				window.location.href="listShopCustomer";
    				}
    			},
    			type: "POST",
    			url:"updateShopCustomer"
    		});
    	}
        function cancelUpdate() {
        	$("#updateShopCustomerDiv").modal('hide');
        	$("#updateShopCustomerId").val("");
        	$("#updateShopName").val("");
        	$("#updateStationid").val("");
    	}

        //输入字符显示
          function numInput(obj,length){
          	if(obj.value==obj.value2)
          		return;
          	if(length == 0 && obj.value.search(/^\d*$/)==-1)
          		obj.value=(obj.value2)?obj.value2:'';
          	else if(obj.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)
          		obj.value=(obj.value2)?obj.value2:'';
          	else obj.value2=obj.value;
          }
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/shopCustomer/listShopCustomer"  >
           	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
      		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span3">
                    	<label class="label-control" title="店铺名称">店铺名称：</label>
                        <input name="shopName" id="shopName" type="text" class="input-medium" value="${shopAndCustomerQueryDTO.shopName}"/>
                    </div>
                    <div class="span3">
                    	<label class="label-control" title="店铺编码">店铺编码：</label>
                        <input name="shopId" id="shopId" onkeyup="numInput(this,0)" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
                        type="text" title="店铺编号只能是数字" class="input-medium" value="${shopAndCustomerQueryDTO.shopId}"/>
                    </div>
                    <div class="span3">
                    	<label class="label-control" title="商铺站点">商铺站点：</label>
                        <input name="stationId" id="stationId" type="text" class="input-medium" value="${shopAndCustomerQueryDTO.stationId}"/>
                    </div>
                    <div class="span2">
                        <button class="btn btn-primary" id="btnQuery">查询</button>
                    </div>
                </div>
            </div>
        </form>

        <table id="treeTable" class="table table-striped table-bordered table-condensed">
            <tr>
                <th>编号</th>
                <th>店铺编码</th>
                <th>商铺名称</th>
                <th>商铺站点</th>
                <th>操作</th>
            </tr>
            <c:forEach items="${page.list}" var="shopAndCustomerDTO" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${shopAndCustomerDTO.shopId}</td>
                    <td>${shopAndCustomerDTO.shopName}</td>
                    <c:if test="${empty shopAndCustomerDTO.stationId }">
                          <td> 该商铺暂时未绑定站点</td>
                          <td>
                                <a href="#" onclick="addShopCustomerShow('${shopAndCustomerDTO.shopId}','${shopAndCustomerDTO.shopName}')">绑定站点</a>
		                  </td>
                    </c:if>
                    <c:if test="${not empty shopAndCustomerDTO.stationId}">
                    <td>
                    ${shopAndCustomerDTO.stationId}
                    </td>
                     <td>
                     <a href="#" onclick="deleteShopCustomer('${shopAndCustomerDTO.id}')">删除站点</a>
                     <a href="#" onclick="updateShopCustomerShow('${shopAndCustomerDTO.id}','${shopAndCustomerDTO.shopId}','${shopAndCustomerDTO.shopName}','${shopAndCustomerDTO.stationId}')">修改站点</a>
                     </td>
                    </c:if>
                   <%--  <c:choose>
                    	<c:when test="${shopAndCustomerDTO.customerList != null && fn:length(shopAndCustomerDTO.customerList) != 0}">
			               	<c:forEach items="${shopAndCustomerDTO.customerList}" var="shopCustomerServiceDTO">
			                    <td>
                           			${shopCustomerServiceDTO.stationId}
			                    </td>
			                    <td>
	                                <a href="#" onclick="deleteShopCustomer('${shopCustomerServiceDTO.id}')">删除站点</a>
	                                <a href="#" onclick="updateShopCustomerShow('${shopCustomerServiceDTO.id}','${shopAndCustomerDTO.shopId}','${shopAndCustomerDTO.shopName}','${shopCustomerServiceDTO.stationId}')">修改站点</a>
			                    </td>
			           		</c:forEach>
	                    </c:when>
	                    <c:otherwise>
	                    	<td>
                           		该商铺暂时未绑定站点
			                </td>
		                    <td>
                                <a href="#" onclick="addShopCustomerShow('${shopAndCustomerDTO.shopId}','${shopAndCustomerDTO.shopName}')">绑定站点</a>
		                    </td>
                        </c:otherwise>
                    </c:choose> --%>
                </tr>
            </c:forEach>
        </table>
        <div class="pagination">${page}</div>
    </div>
</div>

<div class="modal hide fade" id="addShopCustomerDiv" style="overflow-y:auto">
	<form id="addShopCustomerForm">
		<input id="addShopId" name="shopId" type="hidden">
	    <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h3>绑定站点</h3>
	    </div>
	    <div class="modal-body" style="overflow-y: auto; height: 80px">
	        <div class="container-fluid">
		        <span class="font_wid">店铺名称：</span>
				<input id="addShopName" class="input_Style2 wid_90 hei_30" type="text" />
	        </div>
	        <div class="container-fluid">
		        <span class="font_wid">店铺站点：</span>
				<input id="addStationId" name="stationId" class="input_Style2 wid_90 hei_30" type="text" />
	        </div>
	    </div>
	    <div class="modal-footer">
	        <a href="#" class="btn" onclick="doAdd()">保存</a>
	        <a href="#" class="btn" onclick="cancelAdd()">取消</a>
	    </div>
    </form>
</div>

<div class="modal hide fade" id="updateShopCustomerDiv" style="overflow-y:auto">
	<form id="updateShopCustomerForm">
		<input id="updateShopCustomerId" name="id" type="hidden">
		<input id="updateShopId" name="shopId" type="hidden">
	    <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	        <h3>修改站点</h3>
	    </div>
	    <div class="modal-body" style="overflow-y: auto; height: 80px">
	        <div class="container-fluid">
		        <span class="font_wid">店铺名称：</span>
				<input id="updateShopName" class="input_Style2 wid_90 hei_30" type="text" />
	        </div>
	        <div class="container-fluid">
		        <span class="font_wid">店铺站点：</span>
				<input id="updateId" name="stationId" class="input_Style2 wid_90 hei_30" type="text" />
	        </div>
	    </div>
	    <div class="modal-footer">
	        <a href="#" class="btn" onclick="doUpdate()">保存</a>
	        <a href="#" class="btn" onclick="cancelUpdate()">取消</a>
	    </div>
    </form>
</div>

</body>
</html>