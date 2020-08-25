<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid" style="text-align: center;">
        <input type="hidden" id="meetPriceTemp" value="${meetPrice}">
        <input type="hidden" id="discountPriceTemp" value="${discountPrice}">
        <input type="hidden" id="discountPercentTemp" value="${discountPercent}">
              <table id="treeTable" style="text-align: center;" class="table table-striped table-bordered table-condensed td-cen hhtd">
                 <thead>
                     <th>序号</th>
                     <th style="width: 240px;">商品信息</th>
                     <th>商品编号</th>
                     <th>市场指导价</th>
                     <th style="width: 240px;">店铺名称</th>
                     <th>商品状态</th>
                 </thead>
			 	 <tbody>
                   <c:forEach items="${selectedItemList}" var="itemDTO" varStatus="s">
                      <tr>
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