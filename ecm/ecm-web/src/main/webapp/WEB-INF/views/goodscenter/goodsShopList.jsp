<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@page import="com.camelot.goodscenter.dto.enums.ItemAddSourceEnum"%>

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
				 $("#soldOutAll").attr("action","${ctx}/goodList/soldOutAll").submit();
			 }
		}
		/* function soldOutall(){
			var idStr="";
			 if ($("input[name='checkAll']:checked").size() <= 0) {
			  $.jBox.info("请选择数据后进行操作！");
			  return;
			 }
			 if (confirm('是否确认下架！')) {
				 $("#soldOutAll").attr("action","${ctx}/goodList/soldOutAll").submit();
			 }
		} */
		function soldOutall(id){
			var flag=false;
			if(itemCheck("4")){
				if(id>0){
					$("input[name='checkAll']").attr("checked",false);
					 $("#checkItem_"+id).attr("checked",true);
				}else{
					if ($("input[name='checkAll']:checked").size() <= 0) {
                        $.jBox.info("请择数据后进行操作！");
						  return;
						 }
				}
				 if (confirm('是否确认下架！')) {
					 $.ajax({
							url : "${ctx}/goodList/isInShop",
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
										url : "${ctx}/goodList/soldOutAll",
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
                                $.jBox.info("系统错误！请稍后再试！");
							}
						});
				 }
			}
		}
	/* function passItem(id){
		$("input[name='checkAll']").attr("checked",false);
		$("#checkItem_"+id).attr("checked",true);
		$("#soldOutAll").attr("action","${ctx}/goodList/passAll").submit();
	} */
	function itemCheck(value){
		var flag = true;
		$("input[name='checkAll']:checked").each(function(){
			$(this).attr("status");
			if($(this).attr("status")!=value){
				flag = false;
			}
		});
		if(!flag){
			if(value=="2"){
				$.jBox.info("请保证选择的商品的状态全是待审核状态!");
			}else if(value=="4"){
				$.jBox.info("请保证选择的商品的状态全是在售状态!");
			}
			
		}
		return flag;
	}
	function passItem(id){
		if(itemCheck("2")){
			if(id>0){
				$("input[name='checkAll']").attr("checked",false);
				 $("#checkItem_"+id).attr("checked",true);
			}else{
				if ($("input[name='checkAll']:checked").size() <= 0) {
					  $.jBox.info("请择数据后进行操作！");
					  return;
		        }
			}
			$.jBox.confirm("确定要审核通过？","提示",function(value){
	  			if(value=='ok'){
	  				passajax();
	  			}
	  		});
            //$("#approveDiv").modal('show');
		}
	}
    function closemodel(){
        $("#approveDiv").modal('hide');
    }
    function rejectClose(){
    	$("#rejectDiv").modal('hide');
    }
    function getcode(date){
        if(date){
            return date;
        }else{
            return '';
        }
    }
     //通过审核
    function passajax(){
        var auditRemark=$("#auditRemark").val();
        $("#popUpDiv").modal('show');
        $.ajax({
            url : "${ctx}/goodList/passAll",
            type : "POST",
            data :$("#soldOutAll").serialize()+"&auditStatus="+getcode("2")+"&auditRemark="+getcode(auditRemark),// 你的formid
            dataType : "json",
            success : function(data) {
                $("#popUpDiv").modal('hide');
                if(data.success){
                    $("#approveDiv").modal('hide');
                    $.jBox.prompt('操作成功！', '消息', 'info', { closed: function () { $("#searchForm").submit(); } });
                }else{
                    $.jBox.info(data.msg);
                }
            },
            error : function() {
                $("#popUpDiv").modal('hide');
                $.jBox.info("系统繁忙！请稍后再试！");
            }
        });
    }
	/* function rejectItem(id){
		$("input[name='checkAll']").attr("checked",false);
		$("#checkItem_"+id).attr("checked",true);
		$("#soldOutAll").attr("action","${ctx}/goodList/rejectAll").submit();
	} */
	function rejectItem(id){
		if(itemCheck("2")){
			if(id>0){
				$("input[name='checkAll']").attr("checked",false);
				$("#checkItem_"+id).attr("checked",true);
			}else{
				if ($("input[name='checkAll']:checked").size() <= 0) {
					  $.jBox.info("请择数据后进行操作！");
					  return;
					 }
			}
			$("#rejectId").validate({
                rules: {
                    auditRemark:"required"
                }
            });
			 $("#rejectDiv").modal('show');
		}
		 
	}
	 //驳回通过
    function rejectAjax(){
    	var isValide = $("#rejectId").valid();
        if(isValide){
        var auditRemark=$("#rejectRemark").val();
        $("#popUpDiv").modal('show');
        $.ajax({
			url : "${ctx}/goodList/rejectAll",
			type : "POST",
			data :$("#soldOutAll").serialize()+"&auditRemark="+getcode(auditRemark),// 你的formid
			dataType : "json",
			success : function(data) {
				$("#popUpDiv").modal('hide');
				$.jBox.prompt('操作成功！', '消息', 'info', { closed: function () { $("#searchForm").submit(); } });
			},
			error : function(xmlHttpRequest, textStatus, errorThrown) {
				$("#popUpDiv").modal('hide');
				$.jBox.info("系统错误！请稍后再试！");
			}
		});
       }
    }
	
	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s); 
		$("#searchForm").attr("action","${ctx}/goodList/gsListShop/").submit();
    	return false;
    }
	/* function lockItem(id){
		if (confirm('是否确认锁定！')) {
			$("input[name='checkAll']").attr("checked",false);
			$("#checkItem_"+id).attr("checked",true);
			$("#soldOutAll").attr("action","${ctx}/goodList/lockAll").submit();
		}
	} */
	function lockItem(id){
		 $("input[name='checkAll']").attr("checked",false);
		 $("#checkItem_"+id).attr("checked",true);
		 if (confirm('是否确认锁定！')) {
			 $.ajax({
					url : "${ctx}/goodList/lockAll",
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
            break;
    	}
        $.ajax({
            url:url,
            type:"post",
            dataType:'json',
            success:function(data){
                $(data).each(function(i,item){
                    if('1'==flag){
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
                }
            }
        });
        
    }
	function inStore(id,status){
        $.ajax({
            url : "${ctx}/goodList/putInStore2?id="+id+"&status="+status,
            type : "POST",
            dataType : "json",
            success : function(data) {
                if(data.success){
                    $.jBox.info("操作成功!");
                    $("#searchForm").submit();
                }else{
                    $.jBox.info(data.msg);
                }


            },
            error : function(xmlHttpRequest, textStatus, errorThrown) {
                $.jBox.info("系统错误！请稍后再试！");
            }
        });
    }
	function updatePlacedTop(itemId,placedTop){
		$.post(
			"${ctx}/goodList/updatePlacedTop",
			{
				itemId: itemId,
				placedTop: placedTop
			},
			function(data) {
                if(data.success){
                    $.jBox.info("操作成功!");
                    $("#searchForm").submit();
                }else{
                    $.jBox.info(data.msg[0]);
                }
            },
            "json"
		).fail(function() {
			$.jBox.info("系统错误！请稍后再试！");
		})
    }
        function showGoods(id){
            var url="${ctx}/goodList/viewGood?id="+id;
            var title = "查看商品";
            parent.openTab(url,title,"gs"+id);
        }
	</script>
    <shiro:hasPermission name="goods:payrate">
        <script>
            function setPayRate(itemId,payRate){
                $("#payRateForm").validate({
                    rules: {
                        payRate: {
                            number: true,
                            max:1,
                            min:0
                        }
                    }
                });
                $("#setPayRateDiv").modal("show");
                $("#tempItemId").val(itemId);
                $("#payRate").val($($("#tr"+itemId).children()[7]).text());
            }
            function savePayRate(){
                var isValide = $("#payRateForm").valid();
                if(isValide){
                    var itemId = $("#tempItemId").val();
                    var payRate = $("#payRate").val();
                    $.ajax({
                        url:"${ctx}/goodsRate/update",
                        dataType:'json',
                        data:"itemId="+itemId+"&payRate="+payRate,
                        type:"post",
                        success:function(data){
                            if(data.success){
                                $.jBox.info("设置返点成功");
                                $("#setPayRateDiv").modal("hide");
                                $($("#tr"+itemId).children()[7]).html(payRate);
                            }else{
                                $.jBox.info(data.msg);
                            }
                        },
                        error:function(xmlHttpRequest, textStatus, errorThrown){
                            $.jBox.info("设置返点出错");
                        }
                    });
                }

            }
        </script>
    </shiro:hasPermission>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
        <tags:message content="${message}"/>
        <form:form name="searchForm" modelAttribute="goods" id="searchForm" method="post" action="${ctx}/goodList/gsListShop">
		    <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
			<div class="row-fluid">
				<div class="span7"><label>平台分类：</label>
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
                <div class="span3"><label>商品编码：</label><input type="text" value="${goods.itemQueryInDTO.id}" name="itemQueryInDTO.id" class="input-medium" title="只能是数字"/></div>
                    <%-- <div class="span3">SKU编码：<input type="text" value="${goods.itemQueryInDTO.skuId}" name="itemQueryInDTO.sku" class="input-medium" /></div> --%>
                <div class="span3">
	               <label title="商品类型"> 商品类型： </label>
			       <form:select path="itemQueryInDTO.addSource"   class="input-medium">
				          <form:option value="">请选择</form:option>
				          <c:set var="addSources" value="<%= ItemAddSourceEnum.values() %>" />
				          <c:forEach var="addSource" items="${addSources}">
				          <c:if test="${addSource.code!=2}">
				          <form:option value="${addSource.code}">${addSource.label}</form:option>
				          </c:if>
			              </c:forEach>
			       </form:select>
				   </div>
				   <div class="span3"><label>商品状态：</label> 
                        <form:select path="itemQueryInDTO.itemStatus" cssClass="input-medium">
							<form:option value="" label="请选择"></form:option>
					   <!-- <form:option value="1" label="未发布"/> -->
							<form:option value="2" label="待审核"/>
							<form:option value="20" label="审核驳回"/>
							<form:option value="3" label="待上架"/>
							<form:option value="4" label="在售"/>
							<form:option value="5" label="已下架"/>
							<form:option value="6" label="锁定"/>
						</form:select>
                </div>
                </div>
			<div class="row-fluid" >
                <div class="span3"><label>店铺名称：</label><input type="text" value="${goods.shopName}" name="shopName" class="input-medium" /></div>
                <div class="span3" ><label >品牌名称：</label> 	
		                     <select name="itemQueryInDTO.brandIdList" cssClass="input-medium" style="margin-left:-2px;width: 163px;">
		                         <option value="">请选择</option>
								 <c:forEach items="${brandList}" var="brand" >
								  <option <c:if test="${goods.itemQueryInDTO.brandIdList[0]==brand.brandId}"> selected="true"</c:if> value="${brand.brandId}">${brand.brandName}</option>
								 </c:forEach>
							</select>
				   	 </div>
                <div class="span3"><label >是否置顶：</label> 	
                    <form:select path="itemQueryInDTO.placedTop" cssClass="input-medium" style="margin-left:-2px;width: 163px;">
                         <form:option value="">请选择</form:option>
                         <form:option value="1">是</form:option>
                         <form:option value="2">否</form:option>
					</form:select>
			   	 </div>
				<div class="span2">
            	<input type="button" class="btn btn-primary" onclick="return pagesub();" value="搜索">
               </div>
            </div>
	    </form:form>
        </div>
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="controls">
                    <button class="btn btn-primary" onclick="passItem(0)">批量通过</button>
                    <button class="btn btn-primary" onclick="rejectItem(0)">批量驳回</button>
                    <button class="btn btn-primary" onclick="soldOutall(0)">批量下架</button>
                </div>
            </div>
            <div class="row-fluid" style="margin-top: 15px">
            <form name="soldOutAll" id="soldOutAll" method="post" >
            <table id="treeTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
                    <tr>
                        <th><input type="checkbox" value="" id="selAll" onclick="selectAll();"  /></th>
                        <th>序号</th>
                        <th style="width: 240px;">商品信息</th>
                        <th style="width: 71px">商品编号</th>
                        <th>所属类目</th>
                        <th>销售价</th>
                        <th>成本价</th>
                        <shiro:hasPermission name="goods:payrate" >
                            <th>返点</th>
                        </shiro:hasPermission>
                        <th>商品类型</th>
                        <th>店铺名称</th>
                        <th>商品状态</th>
                        <th>上传时间</th>
                        <th>操作</th>
                    </tr>

                 <c:forEach items="${page.list}" var="t" varStatus="s">
                    <tr id="tr${t.itemQueryOutDTO.itemId}" >
                        <td><input id="checkItem_${t.itemQueryOutDTO.itemId }" type="checkbox" status="${t.itemQueryOutDTO.itemStatus}" value="${t.itemQueryOutDTO.itemId}"  name="checkAll" id="checkAll" onclick="setSelectAll();" /></td>
                        <td><c:out value="${s.count}" /></td>
                        <td>
                        <img class="showimg" style="height: 100px;width: 100px;padding-left: 10px"  src="${filePath}${t.itemQueryOutDTO.pictureUrl}" >
                        <label style="width: 120px;">
                        <a href="${mallPath}/productController/details?id=${t.itemQueryOutDTO.itemId}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">
                        ${t.itemQueryOutDTO.itemName}
                        </a>
                        </label>
                        </td>
                        <td>${t.itemQueryOutDTO.itemId}</td>
                        <td>${t.category}</td>
                        <%-- <td>${office.itemQueryOutDTO.itemName}</td> --%>
                        <td>${t.itemQueryOutDTO.marketPrice}</td>
                        <td>${t.itemQueryOutDTO.marketPrice2}</td>
                        <shiro:hasPermission name="goods:payrate" >
                            <td>${t.settleItemExpenseDTO.rebateRate}</td>
                        </shiro:hasPermission>
                        <td>
                        <c:if test="${not empty t.itemQueryOutDTO.addSource}">
                            <c:if test="${t.itemQueryOutDTO.addSource==1}">
                                                               普通商品
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.addSource==3}">
                                                                套装商品
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.addSource==4}">
                                                                基础服务商品
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.addSource==5}">
                                                                增值服务商品
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.addSource==6}">
                                                                辅助材料商品
                            </c:if>
                        </c:if>
                        </td>
                        <td>${t.shopDTO.shopName}</td>
                        <td>
                            <c:if test="${t.itemQueryOutDTO.itemStatus=='2'}">
                                <p style="color:#a0ba59;">待审核</p>
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.itemStatus=='20'}">
                                <p style="color:#a0ba59;">审核驳回</p>
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.itemStatus=='3'}">
                                <p style="color:#a0ba59;">待上架</p>
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.itemStatus=='4'}">
                                <p style="color:#a0ba59;">在售</p>
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.itemStatus=='5'}">
                                <p style="color:#a0ba59;">已下架</p>
                            </c:if>
                            <c:if test="${t.itemQueryOutDTO.itemStatus=='6'}">
                                <p style="color:#a0ba59;">锁定</p>
                            </c:if>
                        </td>
                        <td>
                        <fmt:formatDate value="${t.itemQueryOutDTO.created}" pattern="yyyy-MM-dd HH:mm" type="date" dateStyle="long" />
                        </td>
                        <td>
                            <c:if test="${t.itemQueryOutDTO.itemStatus=='2'}">
                                <a href="javascript:void(0);" onclick="passItem('${t.itemQueryOutDTO.itemId}')">通过</a>
                                <a href="javascript:void(0);" onclick="rejectItem('${t.itemQueryOutDTO.itemId}')">驳回</a>
                            </c:if>
                            <%-- <c:if test="${office.itemQueryOutDTO.itemStatus=='5'}">
                            <a href="javascript:void(0);" onclick="lockItem('${office.itemQueryOutDTO.itemId}')">锁定</a>
                            </c:if> --%>
                            <c:if test="${t.itemQueryOutDTO.itemStatus=='4'}">
                                <a href="javascript:void(0);" onclick="lockItem('${t.itemQueryOutDTO.itemId}')">锁定</a>
                                <a href="javascript:void(0);" onclick="soldOutall('${t.itemQueryOutDTO.itemId}')">下架</a>
                            </c:if>
                            <a href="javascript:void(0)" onclick="showGoods(${t.itemQueryOutDTO.itemId})">查看</a>

                            <shiro:hasPermission name="goods:payrate" >
                                <a href="javascript:void(0);" onclick="setPayRate('${t.itemQueryOutDTO.itemId}','${t.settleItemExpenseDTO.rebateRate}')">设置返点</a>
                            </shiro:hasPermission>
                            <c:choose>
	                            <c:when test="${t.itemQueryOutDTO.placedTop == 1}">
	                            	<a href="javascript:void(0)" onclick="updatePlacedTop(${t.itemQueryOutDTO.itemId},2)">取消置顶</a>
	                            </c:when>
	                            <c:otherwise>
	                            	<a href="javascript:void(0)" onclick="updatePlacedTop(${t.itemQueryOutDTO.itemId},1)">置顶</a>
	                            </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </form>
            </div>
            <div style="display: none;">
                <form action=""></form>
            </div>
            <div class="pagination">${page}</div>
            </div>
        </div>
    </div>
<shiro:hasPermission name="goods:payrate">
<div class="modal hide fade" id="setPayRateDiv">
    <form id="payRateForm" name="payRateForm">
    <input type="hidden" id="tempItemId" name="tempItemId">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>设置返点</h3>
    </div>
    <div class="modal-body">
        <p>
            <input type="text" id="payRate" name="payRate" title="返点只能是小于1的数字"/>
        </p>
    </div>
    <div class="modal-footer">
        <a href="javascript:savePayRate()" class="btn btn-primary">确定</a>
    </div>
    </form>
</div>
</shiro:hasPermission>

<!--审核通过-->
<div class="modal hide fade" id="approveDiv">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>卖家发布商品审核</h3>
    </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span8">
                    <label class="label-control" for="auditRemark" title="备注">备注</label>
                    <textarea rows="3"  name="auditRemark" style="resize: none;" id="auditRemark" maxlength="150"></textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary" id="btnSubApprove" onclick="passajax()">提交</a>
            <a href="#" class="btn" id="btnCloseApprove" onclick="closemodel()">取消</a>
        </div>
</div>
<!--审核驳回-->
<div class="modal hide fade" id="rejectDiv">
<form id="rejectId">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>卖家发布商品驳回</h3>
    </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span8">
                    <label class="label-control" for="auditRemark" title="备注">备注</label>
                    <textarea rows="3"  name="auditRemark" id="rejectRemark" style="resize: none;" maxlength="150" ></textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary" id="btnSubApprove" onclick="rejectAjax()">提交</a>
            <a href="#" class="btn" id="btnCloseApprove" onclick="rejectClose()">取消</a>
        </div>
 </form>
</div>
<!--进度条-->
<div class="modal hide fade" id="popUpDiv">
    <div class="modal-body">
        <div class="progress progress-striped active">
            <div class="bar" style="width: 100%;"></div>
        </div>
    </div>
</div>
</body>
</html>