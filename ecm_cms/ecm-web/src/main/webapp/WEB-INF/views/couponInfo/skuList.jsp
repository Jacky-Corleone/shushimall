<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript">
// 全选checkbox点击的处理
function selectAll_sku() {
	var isAllCheckedOn = $("#selAllCheck").prop('checked');
	$('[name=checkAllCheck]').each(function() {
		// when checkOn
		if (isAllCheckedOn) {
			if (!this.checked) {
				$(this).prop('checked', true);
				//add to list;
				var skuId = $(this).parent().next().next().next().next().text();
				realList.push(skuId);
			}
		}
		// when checkOff
		else {
			if (this.checked) {
				$(this).prop('checked', false);
				// delete from list
				processItemCheck_sku(this);
			}
		}
	})
}

// check某一项的处理
function processItemCheck_sku(obj) {
	var skuId = $(obj).parent().next().next().next().next().text();
	if (obj.checked) {
		//add to list;
		realList.push(skuId);
		// 是否checkon所有子条目
		checkOnFullCheck_sku();
	} else {
		realList = delFromList(realList, skuId);
		$("#selAllCheck").prop('checked',false);
	}
}
function checkOnFullCheck_sku() {
	var allItemsNum = $("#treeTable input[type=checkbox]:gt(0)").length;
	var allCheckedItemsNum = $("#treeTable input[type=checkbox]:checked").length;
	$("#selAllCheck").prop('checked',allItemsNum===allCheckedItemsNum);
}

function getSku(){
	backedList = realList.slice();
	$("#itemCount").html(realList.length);
	skuListToShow(1,5);
	$("#skuList").modal('hide');
}

</script>
<form name="soldOutAll" id="soldOutAll" method="post" >
            <table id="treeTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
                    <tr>
                        <th>
                        <input  style="margin:3px 3px" type="checkbox" value="" id="selAllCheck" onclick="selectAll_sku();"/>
                        </th>
                        <th>序号</th>
                        <th style="width: 240px;">商品信息</th>
                        <th>商品编号</th>
                        <th>SKU编码</th> 
                        <th>销售属性</th> 
                        <th>所属类目</th>
                        <th>销售价</th>
                        <th>商品状态</th>
                        <th style="width: 60px;">库存</th>
                    </tr>

                 <c:forEach items="${page.list}" var="tradeInventoryOutDTO" varStatus="s">
                    <tr>
                        <td>
                            <input style="margin:3px 3px" 
                            name="checkAllCheck" id="checkAllCheck" type="checkbox" value="${tradeInventoryOutDTO.skuId }" onclick="processItemCheck_sku(this);"/>
                        	<input id="${tradeInventoryOutDTO.itemId }" type="hidden"/>
                        	<input id="${tradeInventoryOutDTO.cid }" type="hidden"/>
                        </td>
                        <td><c:out value="${s.count}" /></td>
                        <td>
                        <c:if test="${fn:length(tradeInventoryOutDTO.skuPicture)>0}">
                        	<img class="showimg" style="height: 100px;width: 100px;padding-left: 10px"  src="${filePath}${tradeInventoryOutDTO.skuPicture[0].picUrl }" >
                        </c:if>
                        <label style="width: 120px;">
                        <a href="${mallPath}/productController/details?id=${tradeInventoryOutDTO.itemId}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">
                        ${tradeInventoryOutDTO.itemName}
                        </a>
                        </label>
                        </td>
                        <td>${tradeInventoryOutDTO.itemId}</td>
                        <td>${tradeInventoryOutDTO.skuId}</td>
                        <td>
                        	<c:forEach items="${tradeInventoryOutDTO.itemAttr }" var="itemAttrName">
                        		${itemAttrName.name } :
                        		<c:forEach items="${itemAttrName.values }" var="itemAttrValue">
                        			${itemAttrValue.name}
                        		</c:forEach>
                        	</c:forEach>
                        </td>
                        <td>
							<c:forEach items="${tradeInventoryOutDTO.itemCatCascadeDTO }" var="itemCatCascadeDTO">
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
							<c:if test="${tradeInventoryOutDTO.areaPrices != null}">
								<c:forEach items="${tradeInventoryOutDTO.areaPrices }" var="areaPrices" varStatus="index">
									<c:if test="${index.count eq 1}">
										${areaPrices.sellPrice}
									</c:if>
								</c:forEach>
							</c:if>
						</td>
                        <td>
                            <c:if test="${tradeInventoryOutDTO.itemStatus=='1'}">
                                <p style="color:#a0ba59;">待发布</p>
                            </c:if>
                            <c:if test="${tradeInventoryOutDTO.itemStatus=='2'}">
                                <p style="color:#a0ba59;">待审核</p>
                            </c:if>
                            <c:if test="${tradeInventoryOutDTO.itemStatus=='20'}">
                                <p style="color:#a0ba59;">审核驳回</p>
                            </c:if>
                            <c:if test="${tradeInventoryOutDTO.itemStatus=='3'}">
                                <p style="color:#a0ba59;">待上架</p>
                            </c:if>
                            <c:if test="${tradeInventoryOutDTO.itemStatus=='4'}">
                                <p style="color:#a0ba59;">在售</p>
                            </c:if>
                            <c:if test="${tradeInventoryOutDTO.itemStatus=='5'}">
                                <p style="color:#a0ba59;">已下架</p>
                            </c:if>
                            <c:if test="${tradeInventoryOutDTO.itemStatus=='6'}">
                                <p style="color:#a0ba59;">锁定</p>
                            </c:if>
                        </td>
                        <td>
                        	${tradeInventoryOutDTO.totalInventory}
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </form>
		<div class="pagination">${page}</div>
	

