<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
    	<tr>
			<th>商品名称</th>
			<th>sku编码</th>
			<th>销售属性</th>
			<th>所属类目</th>
			<th>销售价</th>
		</tr>
	</thead>
	<tbody>  
		<c:forEach items="${page.list}" var="rangeDTO" varStatus="s">
		<tr>
			<td>${rangeDTO.itemName}</td>
			<td>${rangeDTO.skuId}</td>
			<td>
			  	<c:forEach items="${rangeDTO.itemAttr }" var="itemAttrName">
					${itemAttrName.name } :
					<c:forEach items="${itemAttrName.values }" var="itemAttrValue">
					${itemAttrValue.name}
					</c:forEach>
				</c:forEach>
			</td>
			<td>
				<c:forEach items="${rangeDTO.itemCatCascadeDTO }" var="itemCatCascadeDTO">
					${itemCatCascadeDTO.cname} /
					<c:forEach items="${itemCatCascadeDTO.childCats }" var="childCats">
						${childCats.cname} /
						<c:forEach items="${childCats.childCats }" var="childCat">
							${childCat.cname }
						</c:forEach>
					</c:forEach>
				</c:forEach>
			</td>
			<td>
				<c:if test="${rangeDTO.areaPrices != null}">
					<c:forEach items="${rangeDTO.areaPrices }" var="areaPrices">
					${areaPrices.sellPrice}
					</c:forEach>
				</c:if>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
<div class="pagination">${page}</div>