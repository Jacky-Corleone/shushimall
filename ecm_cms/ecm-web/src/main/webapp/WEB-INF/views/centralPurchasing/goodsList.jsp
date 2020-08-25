<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="camelot" uri="/WEB-INF/tlds/camelotTag.tld" %>
<form name="soldOutAll" id="soldOutAll" method="post" >
            <table id="treeTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
                    <tr>
                        <th></th>
                        <th>序号</th>
                        <th style="width: 240px;">商品信息</th>
                        <th>商品编号</th>
                        <th>SKU编码</th> 
                        <th>销售属性</th> 
                        <th>销售价</th>
                        <th>店铺名称</th>
                        <th style="width: 60px">平台类型</th>
                    </tr>

                 <c:forEach items="${page.list}" var="tradeInventoryOutDTO" varStatus="s">
                    <tr>
                        <td>
                        	<input id="${tradeInventoryOutDTO.skuId }" type="radio" name="tr_skuId"/>
                        	<input id="${tradeInventoryOutDTO.itemId }" type="hidden"/>
                        	<input id="${tradeInventoryOutDTO.cid }" type="hidden"/>
                        </td>
                        <td><c:out value="${s.count}" /></td>
                        <td>
                        <c:forEach items="${tradeInventoryOutDTO.skuPicture }" var="skuPicture" varStatus="skuIndex">
	                        <c:if test="${skuIndex.count == 1 }">
	                        	<img class="showimg" style="height: 100px;width: 100px;padding-left: 10px"  src="${filePath}${skuPicture.picUrl}" >
	                        </c:if>
                        </c:forEach>
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
							<c:if test="${tradeInventoryOutDTO.areaPrices != null}">
								<c:forEach items="${tradeInventoryOutDTO.areaPrices }" var="areaPrices" varStatus="index">
									<c:if test="${index.count eq 1}">
										${areaPrices.sellPrice}
									</c:if>
								</c:forEach>
							</c:if>
						</td>
                        <td>
                        	<camelot:getShopNameById shopId="${tradeInventoryOutDTO.shopId }"/>
                        </td>
                        <td>
                        	<c:choose>
                        		<c:when test="${tradeInventoryOutDTO.platformId == 2}">
                        			绿印平台
                        		</c:when>
                        		<c:otherwise>
                        			舒适100平台
                        		</c:otherwise>
                        	</c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </form>
		<div class="pagination">${page}</div>
	

