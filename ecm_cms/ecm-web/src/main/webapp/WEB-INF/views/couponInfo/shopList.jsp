<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>卖家店铺信息</title>
    <script type="text/javascript">
			// 全选checkbox点击的处理
    		function selectAll_shop() {
				var isAllCheckedOn = $("#selAll").prop('checked');
				$('[name=checkAll]').each(function() {
					// when checkOn
					if (isAllCheckedOn) {
						if (!this.checked) {
							$(this).prop('checked', true);
							//add to list;
							var shopId = $(this).parent().next().next().text();
							realList.push(shopId);
						}
					}
					// when checkOff
					else {
						if (this.checked) {
							$(this).prop('checked', false);
							// delete from list
							processItemCheck_shop(this);
						}
					}
				})
    		}
    		
    		// check某一项的处理
    		function processItemCheck_shop(obj) {
    			var shopId = $(obj).parent().next().next().text();
    			if (obj.checked) {
					//add to list;
					realList.push(shopId);
					// 是否checkon所有子条目
					checkOnFullCheck_shop();
				} else {
					realList = delFromList(realList, shopId);
					$("#selAll").prop('checked',false);
				}
    		}
    		function checkOnFullCheck_shop() {
    			var allItemsNum = $("#treeTable input[type=checkbox]:gt(0)").length;
    			var allCheckedItemsNum = $("#treeTable input[type=checkbox]:checked").length;
    			$("#selAll").prop('checked',allItemsNum===allCheckedItemsNum);
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
    				setSelectAll(checkboxs[i]);
    			}
    		}
    		function getShopList(){
    			backedList = realList.slice();
    			$("#itemCount").html(realList.length);
    			shopListToShow(1,5);
    			$("#addItemDiv").modal('hide');
    		}
    		
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
            <table id="treeTable" class="table table-striped table-bordered table-condensed">
                <tr>
                    <th>
                     	<input  style="margin:3px 3px" type="checkbox" value="" id="selAll" onclick="selectAll_shop();"/>
                    </th>
                    <th>编号</th>
                    <th>店铺编号</th>
                    <th>店铺名称</th>
                    <th>商家编号</th>
                    <th>申请提交时间</th>
                    <th>店铺状态</th>
                    <th>店铺域名</th>
                </tr>
                <c:forEach items="${page.list}" var="s" varStatus="status">
                    <tr id="tr${s.shopId}">
	                    <td><input style="margin:3px 3px" 
	                    name="checkAll" id="checkAll" type="checkbox" value="${s.shopId}" onclick="processItemCheck_shop(this);"/></td>
                        <td>${status.count}</td>
                        <td>${s.shopId}</td>
                        <td>${s.shopName}</td>
                        <td>${s.sellerId}</td>
                        <td><fmt:formatDate value="${s.created}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
                         <td>
                            <c:choose>
                                <c:when test="${null==s.status||1==s.status}">
                                    待审核
                                </c:when>
                                <c:when test="${null!=s.status&&2==s.status}">
                                    审核通过
                                </c:when>
                                <c:when test="${null!=s.status&&3==s.status}">
                                    已驳回
                                </c:when>
                                <c:when test="${null!=s.status&&4==s.status}">
                                    平台关闭
                                </c:when>
                                <c:when test="${null!=s.status&&5==s.status}">
                                    已开通
                                </c:when>
                                <c:otherwise>
                                   无法识别
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${s.platformId==null}">
                                    ${s.shopUrl}.shushi100.com 
                                </c:when>
                                <c:when test="${s.platformId==2}">
                                    ${s.shopUrl}
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        <div class="pagination">${page}</div>
    </div>
</div>

<input type="hidden" name="shopId" value="" id="shopId">
</body>
</html>