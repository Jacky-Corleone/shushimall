<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>商品管理</title>
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

            //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });

			$("#treeTable").treeTable({expandLevel : 5});
            $("#searchForm").validate({
                rules: {
                    "itemQueryInDTO.id": {
                        digits: true
                    }
                }
            });
		});
		function checkCategory(){
			if($('#platformId1 option:selected').val()){
				if(!$('#platformId2 option:selected').val()){
					$.jBox.info("请选择二级目录");
					return false;
				}else if(!$('#cid option:selected').val()){
                    $.jBox.info("请选择三级目录");
					return false;
				}
			}
			return true;
		}
		function pagesub(n,s){
			//if(checkCategory()){
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
			//}
	    }
		var selAll = document.getElementById("selAll");
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
		function soldOutall(){
			var idStr="";
			 if ($("input[name='checkAll']:checked").size() <= 0) {
                 $.jBox.info("请选择数据后进行操作！");
			  return;
			 }
			 if (confirm('是否确认下架！')) {
				 $("#soldOutAll").submit();
			 } else {
			  return;
			 }
		}
		 
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s); 
			$("#searchForm").attr("action","${ctx}/goodList/gsList/").submit();
	    	return false;
	    }
		function editGood(id){
			
		}
		function soldOutall(id){
			 var flag=false;
			 $("input[name='checkAll']").attr("checked",false);
			 $("#checkItem_"+id).attr("checked",true);
			 if (confirm('是否确认下架！')) {
				 $.ajax({
						url : "${ctx}/goodList/isplatItemIdInShop",
						type : "POST",
						data :$("#soldOutAll").serialize(),// 你的formid
						dataType : "json",
						success : function(data) {
							if(data.success==false){
								 if (confirm(data.msg+'是否继续？')){
									flag=true;
								}
							}else{
								flag=true;
							}
							if(flag){
								$.ajax({
									url : "${ctx}/goodList/soldOutByGoods",
									type : "POST",
									data :$("#soldOutAll").serialize(),// 你的formid
									dataType : "json",
									success : function(data) {
										$.jBox.info("操作成功!");
										$("#searchForm").submit();
									},
									error : function(xmlHttpRequest, textStatus, errorThrown) {
		                                $.jBox.info("系统错误！请稍后再试！");
									}
								});
							}
						},
						error : function(xmlHttpRequest, textStatus, errorThrown) {
                            $.jBox.error("系统错误！请稍后再试！");
						}
					});
				 //$("#soldOutAll").attr("action","${ctx}/goodList/soldOutByGoods").submit();
			 }
			 
		}
		function loadsub(flag){
            var html;
            var url;
            switch (flag){
            case '1':
                var pid = $("#platformId1").val();
                if(pid==null||pid<=0){
                	pid= '${goods.platformId1}';
                }
                html = "<option value=''>二级分类</option>";
                url = "${ctx}/brand/getChildCategory?pCid="+pid;
                $("#platformId2").html("<option value=''>二级分类</option>");
            	$("#platformId2").select2("val","");
            	$("#cid").html("<option value=''>三级分类</option>");
            	$("#cid").select2("val","");
            	$("#brand").empty();
                break;
            case '2':
                var pid = $("#platformId2").val();
                if(pid==null||pid<=0){
                	pid = '${goods.platformId2}';
                }
                html = "<option value=''>三级分类</option>";
                url = "${ctx}/brand/getChildCategory?pCid="+pid;
                $("#cid").html("<option value=''>三级分类</option>");
            	$("#cid").select2("val","");
            	$("#brand").empty();
                break;
            case '3':
                var cid = $("#cid").val();
                var secondCid = $("#platformId2").val();
                
                url = "${ctx}/brand/getCategoryBrand?secondCid="+secondCid+"&thirdCid="+cid;
                break;
        	}
            $.ajax({
                url:url,
                type:"post",
                dataType:'json',
                success:function(data){
                    $(data).each(function(i,item){
                        if('1'==flag){
                        	/* var a =item.categoryCid;
                        	$.jBox.info(a); */
                        	/* var b ='${twoLevelId}';
                        	$.jBox.info(b); */
                            html += "<option ";
                        	if(item.categoryCid=='${goods.platformId2}'){
                            	html +=" selected";
                            }
                        	html +=" value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                        }else if('2'==flag){
                        	/* $.jBox.info(${itemDTO.cid});
                        	$.jBox.info(item.categoryCid); */
                            html += "<option ";
                            if(item.categoryCid=='${goods.cid}'){
                            	html +=" selected ";
                            }
                            html +="value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                        }
                    });
                    switch (flag){
                        //加载第二级
                        case '1':
                            $("#platformId2").html(html);
                            break;
                        //加载第三级
                        case '2':
                            $("#cid").html(html);
                            break;
                        // 加载品牌
                        case '3':
                            $("#brand").html(html);
                            break;
                    }
                }
            });
            
        }
		function putInStore(id){
			 $("input[name='checkAll']").attr("checked",false);
			 $("#checkItem_"+id).attr("checked",true);
			 if (confirm('是否确认立即入库！')) {
				 $.ajax({
						url : "${ctx}/goodList/putInStore",
						type : "POST",
						data :$("#soldOutAll").serialize(),// 你的formid
						dataType : "json",
						success : function(data) {
                            $.jBox.info("操作成功!");
							$("#searchForm").submit();
						},
						error : function(xmlHttpRequest, textStatus, errorThrown) {
                            $.jBox.error("系统错误！请稍后再试！");
						}
					});
			 }
		}
		function shelves(id){
			$("input[name='checkAll']").attr("checked",false);
			 $("#checkItem_"+id).attr("checked",true);
			 if (confirm('是否确认上架！')) {
				 $.ajax({
						url : "${ctx}/goodList/shelves",
						type : "POST",
						data :$("#soldOutAll").serialize(),// 你的formid
						dataType : "json",
						success : function(data) {
                            $.jBox.info("操作成功!");
							$("#searchForm").submit();
						},
						error : function(xmlHttpRequest, textStatus, errorThrown) {
                            $.jBox.error("系统错误！请稍后再试！");
						}
					});
			 }
		}
        function edit(id){
            var url="${ctx}/goodscenter/editGoods?itemId="+id;
            var title = "编辑商品";
            parent.openTab(url,title,"ge"+id);
        }
        function showGoods(id){
            var url="${ctx}/goodList/viewGoodInit?itemId="+id;
            var title = "查看商品";
            parent.openTab(url,title,"gs"+id);
        }
    function cancle(id){
        if (confirm('是否删除商品！')) {
            $.ajax({
                url : "${ctx}/goodList/deletegoods",
                type : "POST",
                data :{
                    itemid:id
                },// 你的formid
                dataType : "json",
                success : function(data) {
                    if(data.success){
                        $.jBox.prompt('删除成功', '消息', 'info', { closed: function () {
                            $("#searchForm").submit();
                        } });
                    }else{
                        $.jBox.error(data.msg);
                    }
                },
                error : function() {
                    $.jBox.error("系统错误！请稍后再试！");
                }
            });
        }
    }
	</script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
            <tags:message content="${message}"/>
            <form:form name="searchForm"  modelAttribute="goods"  id="searchForm" method="post" action="${ctx}/goodList/gsList"  >
                <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
                <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
                <div class="row-fluid">
                    <div class="span12"><label>平台分类：</label>
                        <form:select path="platformId1" cssClass="input-medium" onchange="loadsub('1')">
                            <form:option value="" label="一级分类"></form:option>
                            <c:forEach items="${platformList}" var="item">
                                <option <c:if test="${item.categoryCid==goods.platformId1}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
                            </c:forEach>
                        </form:select>
                        <form:select path="platformId2" cssClass="input-medium" onchange="loadsub('2')">
                            <form:option value="" label="二级分类"></form:option>
                            <c:forEach items="${twoList}" var="item">
                                <option <c:if test="${item.categoryCid==goods.platformId2}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
                            </c:forEach>
                        </form:select>
                        <form:select path="cid" cssClass="input-medium">
                            <form:option value="" label="三级分类"></form:option>
                            <c:forEach items="${thirdList}" var="item">
                                <option <c:if test="${item.categoryCid==goods.cid}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="row-fluid" style="margin-top: 10px;">
                    <div class="span3"><label>商品名称：&nbsp;</label><input type="text" value="${goods.itemQueryInDTO.itemName}" name="itemQueryInDTO.itemName"  class="input-medium"/></div>
                    <div class="span3"><label>商&nbsp;品&nbsp;编&nbsp;码：&nbsp;</label><input type="text" value="${goods.itemQueryInDTO.id}" name="itemQueryInDTO.id" class="input-medium" title="只能是数字"/></div>
                    <div class="span4" ><label style="margin-top: 8px;">品牌名称：</label> 	
		                     <select name="itemQueryInDTO.brandIdList" cssClass="input-medium" style="margin-right:2px;width: 163px;">
		                         <option value="">请选择</option>
								 <c:forEach items="${brandList}" var="brand" >
								  <option <c:if test="${goods.itemQueryInDTO.brandIdList[0]==brand.brandId}"> selected="true"</c:if> value="${brand.brandId}">${brand.brandName}</option>
								 </c:forEach>
							</select>
				   	 </div>    
                </div>
                <div class="row-fluid">
                    <div class="span3"><label>商品状态：</label>
                        <form:select path="itemQueryInDTO.itemStatus" cssClass="input-medium">
                            <form:option value="" label="请选择"></form:option>
                            <form:option value="1" label="未发布"/>
                            <form:option value="2" label="待审核"/>
                            <form:option value="20" label="审核驳回"/>
                            <form:option value="3" label="待上架"/>
                            <form:option value="4" label="在售"/>
                            <form:option value="5" label="已下架"/>
                            <form:option value="6" label="锁定"/>
                            <form:option value="7" label="申请解锁"/>
                            <form:option value="30" label="已删除"/>
                        </form:select>
                    </div>
                    <div class="span3"><label>商品库状态：</label>
                        <form:select path="itemQueryInDTO.platLinkStatus" cssClass="input-medium">
                            <form:option value="" label="请选择"></form:option>
                            <form:option value="2" label="待入库"/>
                            <form:option value="3" label="已入库"/>
                        </form:select>
                    </div>
                    <div class="span2">
                    	<input type="button" class="btn btn-primary" onclick="pagesub();" value="搜索">
                    </div>
                </div>

            </form:form>
        </div>
        <div class="container-fluid" style="text-align: center;">
            <form name="soldOutAll" id="soldOutAll" method="post" action="${ctx}/goodList/soldGoodsOutAll"  >
                <table id="treeTable" style="text-align: center;" class="table table-striped table-bordered table-condensed td-cen hhtd">
                    <tr>
                        <th><input type="checkbox" value="" id="selAll" onclick="selectAll();"  /></th>
                        <th>序号</th>
                        <th style="width: 240px;">商品信息</th>
                       <!--  <th>商品货号</th> -->
                        <th>商品库编号</th>
                        <th>所属类目</th>
                        <!-- <th>销售价</th>
                        <th>成本价</th> -->
                        <th>市场指导价</th>
                        <!-- <th>店铺名称</th> -->
                        <th>商品状态</th>
                        <th>商品库状态</th>
                        <th>操作</th>
                    </tr>
					<tbody>
                    <c:forEach items="${page.list}" var="office" varStatus="s">
                        <tr id="${office.itemQueryOutDTO.itemId}" >
                            <td><input id="checkItem_${office.itemQueryOutDTO.itemId }" type="checkbox" value="${office.itemQueryOutDTO.itemId}"  name="checkAll" id="checkAll" onclick="setSelectAll();" /></td>
                            <td><c:out value="${s.count}" /></td>
                            <td>
                                <img class="showimg" style="height: 100px;width: 100px;padding-left: 10px"  src="${filePath}${office.itemQueryOutDTO.pictureUrl}" >
                                    <label style="width: 120px;">
                                    <a href="${mallPath}/productController/categoryItemDetail?itemId=${office.itemQueryOutDTO.itemId}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">
                                    ${office.itemQueryOutDTO.itemName}
                                    </a>
                                    </label></td>
                            <%-- <td>${office.itemQueryOutDTO.productId}</td> --%>
                            <td>${office.itemQueryOutDTO.itemId}</td>
                            <td>${office.category}</td>
                                <%-- <td>${office.itemQueryOutDTO.marketPrice}</td>
                                <td>${office.itemQueryOutDTO.marketPrice2}</td> --%>
                            <td>${office.itemQueryOutDTO.marketPrice}</td>
                                <%-- <td>${office.shopDTO.shopName}</td> --%>
                            <td>
                            <c:if test="${office.itemQueryOutDTO.itemStatus=='1'}">
                                <p style="color:#a0ba59;">未发布</p>
                            </c:if><c:if test="${office.itemQueryOutDTO.itemStatus=='2'}">
                            <p style="color:#a0ba59;">待审核</p>
                        </c:if><c:if test="${office.itemQueryOutDTO.itemStatus=='20'}">
                            <p style="color:#a0ba59;">审核驳回</p>
                        </c:if><c:if test="${office.itemQueryOutDTO.itemStatus=='3'}">
                            <p style="color:#a0ba59;">待上架</p>
                        </c:if><c:if test="${office.itemQueryOutDTO.itemStatus=='4'}">
                            <p style="color:#a0ba59;">在售</p>
                        </c:if><c:if test="${office.itemQueryOutDTO.itemStatus=='5'}">
                            <p style="color:#a0ba59;">已下架</p>
                        </c:if><c:if test="${office.itemQueryOutDTO.itemStatus=='6'}">
                            <p style="color:#a0ba59;">锁定</p>
                        </c:if><c:if test="${office.itemQueryOutDTO.itemStatus=='30'}">
                            <p style="color:#a0ba59;">已删除</p>
                        </c:if>

                            </td>
                            <td>
                            <c:if test="${office.itemQueryOutDTO.platLinkStatus=='1'}">
                                <p style="color:#a0ba59;">未符合待入库</p>
                            </c:if>
                            <c:if test="${office.itemQueryOutDTO.platLinkStatus=='2'}">
                                <p style="color:#a0ba59;">待入库</p>
                            </c:if>
                            <c:if test="${office.itemQueryOutDTO.platLinkStatus=='3'}">
                                <p style="color:#a0ba59;">已入库</p>
                            </c:if>
                            <c:if test="${office.itemQueryOutDTO.platLinkStatus=='4'}">
                                <p style="color:#a0ba59;">已删除</p>
                            </c:if>
                            </td>
                            <td>
                                <c:if test="${not empty office.itemQueryOutDTO.itemStatus and office.itemQueryOutDTO.itemStatus!='30'}">
                                <c:if test="${office.itemQueryOutDTO.platLinkStatus=='2'}">
                                    <a href="javascript:void(0);" onclick="putInStore('${office.itemQueryOutDTO.itemId}')">立即入库</a>

                                    <a href="javascript:void(0)" onclick="edit(${office.itemQueryOutDTO.itemId})">编辑</a>
                                </c:if>
                                <c:if test="${office.itemQueryOutDTO.platLinkStatus=='3'}">
                                    <a href="javascript:void(0)" onclick="edit(${office.itemQueryOutDTO.itemId})">编辑</a>
                                </c:if>

                                <a href="javascript:void(0)" onclick="showGoods(${office.itemQueryOutDTO.itemId})">查看</a>
                                <c:if test="${office.itemQueryOutDTO.itemStatus!='5' && office.itemQueryOutDTO.platLinkStatus!='2'}">
                                    <a href="javascript:void(0);" onclick="soldOutall('${office.itemQueryOutDTO.itemId}')">下架</a>
                                </c:if>
                                <c:if test="${office.itemQueryOutDTO.itemStatus=='5'}">
                                    <a href="javascript:void(0);" onclick="shelves('${office.itemQueryOutDTO.itemId}')">上架</a>
                                </c:if>
                                <c:if test="${office.itemQueryOutDTO.itemStatus!='2' and office.itemQueryOutDTO.itemStatus!='4'}">
                                <a href="javascript:void(0);" onclick="cancle('${office.itemQueryOutDTO.itemId}')">删除</a>
                                </c:if>
                                </c:if>
                                <c:if test="${office.itemQueryOutDTO.itemStatus=='30'}">
                                            已删除
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </form>
            <div class="pagination">${page}</div>
        </div>

</div></div>
</body>
</html>