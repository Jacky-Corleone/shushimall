<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>
  	<title>二手市场商品审核</title>
  	<meta name="decorator" content="default"/>
  	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
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
    <script type="text/javascript">
        $(document).ready(function() {
            //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });
        });
    	function page(n,s){
	    	$("#pageNo").val(n);
	    	$("#pageSize").val(s); 
	    	$("#searchForm").attr("action","${ctx}/usedGoods/getUsedGoodsList/").submit();
	    	return false;
    	}
    </script>
    <script type="text/javascript" src="${ctxStatic}/goodscenter/usedmarket/usedgoods.js"></script>
  </head>
  
  <body>
  <form:form id="searchForm" action="${ctx}/usedGoods/getUsedGoodsList" modelAttribute="oldGoods" >
  	  <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
      <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
	  <div class="row-fluid">
	      <div class="span12"><label>平台分类：</label>
	          <form:select path="platformId1" cssClass="input-medium" onchange="UsedGoods.loadsub('1')">
	              <form:option value="" label="一级分类"></form:option>
	              <c:forEach items="${platformList}" var="item">
	              	<option <c:if test="${item.categoryCid==oldGoods.platformId1}">selected="true"</c:if>value="${item.categoryCid}">${item.categoryCName}</option>
	              </c:forEach>
	          </form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          <form:select path="platformId2" cssClass="input-medium" onchange="UsedGoods.loadsub('2')">
	              <form:option value="" label="二级分类"></form:option>
	              <c:forEach items="${twoList}" var="item">
	              	<option <c:if test="${item.categoryCid==oldGoods.platformId2}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
	              </c:forEach>
	          </form:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          <form:select path="cid" cssClass="input-medium">
	              <form:option value="" label="三级分类"></form:option>
	              <c:forEach items="${thirdList}" var="item">
                      <option <c:if test="${item.categoryCid==oldGoods.cid}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
                  </c:forEach>
	          </form:select>
	      </div>
	  </div>
	  <div class="row-fluid" style="margin-top: 10px;">
	      <div class="span4"><label>商品名称：&nbsp;</label><input type="text" value="${oldGoods.itemOldInDTO.itemOldDTO.itemName}" name="itemOldInDTO.itemOldDTO.itemName"  class="input-medium"/></div>
	      <div class="span4"><label>商品编码：&nbsp;</label><input type="number" value="${oldGoods.itemOldInDTO.itemOldDTO.itemId}" name="itemOldInDTO.itemOldDTO.itemId" class="input-medium" title="只能是数字"/></div>
	      <%-- <div class="span4"><label>买家用户名：&nbsp;</label><input type="text" value="${oldGoods.itemOldInDTO.itemOldDTO.sellerName}" name="itemOldInDTO.itemOldDTO.sellerName"  class="input-medium"/></div> --%>
	  </div>
	  <div class="row-fluid" style="margin-top: 10px;">
	  		<div class="span4"><label>审核状态：</label>
		      	<form:select path="itemOldInDTO.itemOldDTO.status" cssClass="input-medium">
					<form:option value="" label="请选择"></form:option>
					<form:option value="2" label="待审核"/>
					<form:option value="20" label="审核驳回"/>
					<form:option value="3" label="待上架"/>
					<form:option value="4" label="在售"/>
					<form:option value="5" label="已下架"/>
				</form:select>
	       </div>
	  	   <div class="span5">
              <label class="control-label" for="createTimeBegin" title="发布时间">
                        发布时间：
              </label>
              <input name="createdStart" id="createdStart" value="<fmt:formatDate value='${oldGoods.itemOldInDTO.itemOldDTO.createdstr}' pattern='yyyy-MM-dd' /> " readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createdEnd\')}',dateFmt:'yyyy-MM-dd'});"  type="text" class="input-small Wdate" />
                    至
              <input name="createdEnd" id="createdEnd" value="<fmt:formatDate value='${oldGoods.itemOldInDTO.itemOldDTO.createdend}' pattern='yyyy-MM-dd' /> " readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createdStart\')}',dateFmt:'yyyy-MM-dd'});"  type="text" class="input-small Wdate" />
          </div>
	      
	      <div class="span2">
	      	<input type="button" class="btn btn-primary" onclick="return UsedGoods.pagesub();" value="搜索">
          </div>
	  </div>
  </form:form>
  
  <div class="container-fluid">
  	<div class="row-fluid">
                <div class="controls">
                    <button class="btn btn-primary" onclick="UsedGoods.passItem(0)">批量通过</button>
                    <button class="btn btn-primary" onclick="UsedGoods.rejectItem(0)">批量驳回</button>
                    <button class="btn btn-primary" onclick="UsedGoods.removeItem(0)">批量删除</button>
                </div>
    </div>
    <div class="row-fluid" style="margin-top: 15px">
    <form name="soldOutAll" id="soldOutAll" method="post" >
          <table id="treeTable" style="text-align: center;" class="table table-striped table-bordered table-condensed td-cen hhtd">
              <tr>
                  <th><input type="checkbox" value="" id="selAll" onclick="UsedGoods.selectAll();"  /></th>
                  <th style="width: 40px;">序号</th>
                   <th style="width:100px">商品信息</th>
                   <th style="width:100px">商品编码</th>
                   <th style="width:200px">所属类目</th>
                   <th style="width:50px">转卖价（元）</th>
                   <th style="width:50px">原价（元）</th>
                   <th style="width:60px">新旧程度</th>
                   <th style="width:120px">联系人</th>
                   <th style="width:100px">发布时间</th>
                   <th style="width:70px">商品状态</th>
                   <th style="width:100px">操作</th>
               </tr>
			   <tbody>
			   
			   		<c:forEach items="${itemOldLists}" var="itemOldList" varStatus="s">
                    <tr id="tr${itemOldList.itemId}" >
                        <td><input id="checkItem_${itemOldList.itemId }" type="checkbox" status="${itemOldList.status}" value="${itemOldList.itemId}"  name="checkAll" id="checkAll" onclick="UsedGoods.setSelectAll();" /></td>
                        <td><c:out value="${s.count}" /></td>
                        <td>
                        <img class="showimg" style="height: 100px;width: 100px;padding-left: 0px"  src="${filePath}/${itemOldList.imgUrl}" >
                        ${itemOldList.itemName}</td>
                        <td>${itemOldList.itemId}</td>
                        <td>${itemOldList.cName}</td>
                        <td>${itemOldList.price}</td>
                        <td>${itemOldList.priceOld}</td>
                        <td>
                        <c:if test="${itemOldList.recency=='10'}">
                        	<p>全新</p>
                        </c:if>
                        <c:if test="${itemOldList.recency=='9'}">
                        	<p>9成新</p>
                        </c:if>
                        <c:if test="${itemOldList.recency=='8'}">
                        	<p>8成新</p>
                        </c:if>
                        <c:if test="${itemOldList.recency=='7'}">
                        	<p>7成新</p>
                        </c:if>
                        <c:if test="${itemOldList.recency=='6'}">
                        	<p>6成新</p>
                        </c:if>
                        <c:if test="${itemOldList.recency=='5'}">
                        	<p>5成新</p>
                        </c:if>
                        </td>
                        <td>${itemOldList.sellerName}</td>
                        <td>
                        	<fmt:formatDate value='${itemOldList.created}' pattern='yyyy-MM-dd' /> 
                        </td>
                        <td>
                            <c:if test="${itemOldList.status=='2'}">
                                <p style="color:#a0ba59;">待审核</p>
                            </c:if>
                            <c:if test="${itemOldList.status=='20'}">
                                <p style="color:#a0ba59;">审核驳回</p>
                            </c:if>
                            <c:if test="${itemOldList.status=='3'}">
                                <p style="color:#a0ba59;">待上架</p>
                            </c:if>
                            <c:if test="${itemOldList.status=='4'}">
                                <p style="color:#a0ba59;">在售</p>
                            </c:if>
                            <c:if test="${itemOldList.status=='5'}">
                                <p style="color:#a0ba59;">已下架</p>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${itemOldList.status=='2'}">
                            <a href="javascript:void(0);" onclick="UsedGoods.passItem('${itemOldList.itemId}')">通过</a>
                            <a href="javascript:void(0);" onclick="UsedGoods.rejectItem('${itemOldList.itemId}')">驳回</a>
                            </c:if>
                            <%-- <c:if test="${office.itemQueryOutDTO.itemStatus=='5'}">
                            <a href="javascript:void(0);" onclick="lockItem('${office.itemQueryOutDTO.itemId}')">锁定</a>
                            </c:if> --%>
                            <c:if test="${itemOldList.status=='20'||itemOldList.status=='3'||itemOldList.status=='4'||itemOldList.status=='5'}">
                            <a href="javascript:void(0);" onclick="UsedGoods.removeItem('${itemOldList.itemId}')">删除</a>
                            </c:if>
                            <a href="${ctx}/usedGoods/viewUsedGoods?itemId=${itemOldList.itemId}">查看</a>
                        </td>
                    </tr>
                </c:forEach>
                
              </tbody>
          </table>
      </form>
      <div class="pagination">${page}</div>
    </div>
	</div>
  </div>
  <%--驳回提示框 --%>
  <div class="modal hide fade" id="rejectDiv">
    <form id="rejectForm" name="rejectForm">
    <div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>审核驳回</h3>
    </div>
    <div>
        <p>
        	<textarea id="rejectContext" style="width:560px;height:100px;" rows="5" cols="9"></textarea>
        </p>
    </div>
    <div class="modal-footer">
        <a href="javascript:void(0)" onclick="UsedGoods.savereject()" class="btn btn-primary">确定</a>
    </div>
    </form>
</div>
  
  </body>
</html>
