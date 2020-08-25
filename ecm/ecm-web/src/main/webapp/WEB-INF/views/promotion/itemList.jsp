<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<script type="text/javascript">
		//全选按钮
		function selectAll(){
			var obj = document.getElementsByName("checkAll");
			if (document.getElementById("selAll").checked == false) {
				for ( var i = 0; i < obj.length; i++) {
					obj[i].checked = false;
					checkItem(obj[i]);
				}
			} else {
				for ( var i = 0; i < obj.length; i++) {
					obj[i].checked = true;
					checkItem(obj[i]);
				}
			}
		};
	
		function checkItem(thiz){
			var itemId = thiz.value;
			var selectItemKey = $("#selectItemKey1").val();
			saveItemInfo(itemId,selectItemKey,thiz.checked);
			$.ajax({
	            url:"${ctx}/frPromotion/checkItemForFrPromotion",
	            type:"post",
	            data:{
	            	itemId:itemId,
	            	selectItemKey:selectItemKey,
	            	checkedType:thiz.checked
	            },
	            dataType:'json',
	            success:function(data){
	            	if(data.success){
	            		$("#itemCount").html(data.result);
	            	}
	            }
	        });
			
		}
		function saveItemInfo(itemId,selectItemKey,checkedType){
			var $this = $("#checkItem_"+itemId);
			var $itemName = $this.parents("tr").find("td:eq(2)").html();
			var $itemId = $this.parents("tr").find("td:eq(3)").html();
			var $guidePrice = $this.parents("tr").find("td:eq(4)").html();
			var $shopName = $this.parents("tr").find("td:eq(5)").html();
			var $status = $this.parents("tr").find("td:eq(6)").html();
			var $itemInfo = $itemName + "," + $itemId + "," + $guidePrice + "," + $shopName + "," + $status;
			$.ajax({
	            url:"${ctx}/frPromotion/saveItemInfoForShow",
	            type:"post",
	            data:{
	            	itemId:itemId,
	            	selectItemKey:selectItemKey,
	            	checkedType:checkedType,
	            	itemInfo:$itemInfo
	            }
				
	        });
		}
	</script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid" style="text-align: center;">
              <table id="treeTable" style="text-align: center;" class="table table-striped table-bordered table-condensed td-cen hhtd">
                 <thead>
                     <th>
                     	<input  style="margin:3px 3px" type="checkbox" value="" id="selAll" onclick="selectAll();"/>
                     </th>
                     <th>序号</th>
                     <th style="width: 240px;">商品信息</th>
                     <th>商品编号</th>
                     <th>报价</th>
                     <th style="width: 240px;">店铺名称</th>
                     <th>商品状态</th>
                 </thead>
			 	 <tbody>
                   <c:forEach items="${itemList}" var="itemDTO" varStatus="s">
                      <tr id="${itemDTO.itemQueryOutDTO.itemId}" >
	                      <c:if test="${itemDTO.checked}">
	                      	<td><input style="margin:3px 3px" name="checkAll" id="checkItem_${itemDTO.itemQueryOutDTO.itemId }" type="checkbox" value="${itemDTO.itemQueryOutDTO.itemId}" onclick="checkItem(this);" checked="checked"/></td>
	                      </c:if>
	                      <c:if test="${itemDTO.checked==false}">
	                      	<td><input style="margin:3px 3px" name="checkAll" id="checkItem_${itemDTO.itemQueryOutDTO.itemId }" type="checkbox" value="${itemDTO.itemQueryOutDTO.itemId}" onclick="checkItem(this);" /></td>
	                      </c:if>
                          <td><c:out value="${s.count}" /></td>
                          <td>${itemDTO.itemQueryOutDTO.itemName}</td>
                          <td>${itemDTO.itemQueryOutDTO.itemId}</td>
                          <td>${itemDTO.itemQueryOutDTO.guidePrice}</td>
                          <td>${itemDTO.shopDTO.shopName}</td>
                          <td>在售</td>
                      </tr>
                   </c:forEach>
                 </tbody>
            </table>
            <div class="pagination">${page}</div>
        </div>
	</div>
</div>
</body>
</html>