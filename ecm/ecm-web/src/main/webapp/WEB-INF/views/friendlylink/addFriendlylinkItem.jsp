<%@page import="com.camelot.openplatform.common.enums.VipLevelEnum"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>优惠劵活动添加</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<%@include file="/WEB-INF/views/include/treeview.jsp" %>
<script type="text/javascript" src="${ctxStatic}/loadMsg/loadMsg.js"></script>
<style type="text/css">
td {
	text-align: center;
}
.row-fluid .span1 {
    width: 6.7%;
}
</style>
<script type="text/javascript">
var tree;
var backedList;
var realList;
var firstLoadShop = true, firstLoadSku =true;
	$(document).ready(function() {
			resetTwoList();
			var ids = "${str}".split(",");
			if(ids.length > 0){
				realList = ids;
			}
			//1 满减券
	    	if(${couponInfo.couponType=='1'}){
	    		$("#full").show();
	    		$("#meetPrice").show();
	    		$("#reduction").show();
	    		$("#xianjin").hide();
	    		$("#discount").hide();
	    		$("#quota").hide();
	    		$("#couponMax").hide();
	    		$("#couponAmount").bind("keyup",numInputWithLength);
	    	//2 折扣券   
	    	}else if(${couponInfo.couponType=='2'}){
	    		$("#quota").show();
	    		$("#couponMax").show();
	    		$("#discount").show();
	    		$("#xianjin").hide();
	    		$("#full").hide();
	    		$("#meetPrice").hide();
	    		$("#reduction").hide();
	    		$("#couponAmount").bind("keyup",numInput4Discount);
	    	//3 现金券
	    	}else if(${couponInfo.couponType=='3'}){
	    		$("#xianjin").show();
	    		$("#discount").hide();
	    		$("#full").hide();
	    		$("#meetPrice").hide();
	    		$("#reduction").hide();
	    		$("#quota").hide();
	    		$("#couponMax").hide();
	    		$("#couponAmount").bind("keyup",numInputWithLength);
	    	} else {
	    		$("#couponAmount").bind("keyup",numInputWithLength);
	    	}
			if($("input[name='couponUsingRange']:checked").val()=='3'){
				$("#ztreeid").show();
			}else if($("input[name='couponUsingRange']:checked").val()=='2'){
	            $("#itemCountShow").show();
	            $("#goodId").html("店铺"); 
	            $("#itemCount").html(${num});
	            
	            shopListToShow(1,5);
	        }else if($("input[name='couponUsingRange']:checked").val()=='4'){
	        	$("#itemCountShow").show();
	            $("#goodId").html("商品"); 
	            $("#itemCount").html(${num});
	            skuListToShow(1,5);
	        }
		var setting = {
				check:{enable:true,nocheckInherit:true},
				view:{selectedMulti:false},
				data:{
					simpleData:{enable:true}
				},
				callback:{
					beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}
				}
			};
			
			
        	// 用户-菜单
			var zNodes=[
				<c:forEach items="${allCid}" var="menu">
					{id:'${menu.categoryCid}', pId:'${not empty menu.categoryParentCid?menu.categoryParentCid:0}', name:"${not empty menu.categoryParentCid?menu.categoryCName:'类目列表'}"},
	            </c:forEach>];
			// 初始化树结构
			tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			
			// 默认选择节点
			 
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", ids[i]);
				try{tree.checkNode(node, true, false);}catch(e){}
			} 
			// 默认展开全部节点
			tree.expandAll(false);
		$("input[name='couponType']").click(function(){
			//1 满减券
	    	if(this.value == '1'){
	    		$("#full").show();
	    		$("#meetPrice").show();
	    		$("#reduction").show();
	    		$("#xianjin").hide();
	    		$("#discount").hide();
	    		$("#quota").hide();
	    		$("#couponMax").hide();
	    		$("#couponAmount").unbind("keyup");
	    		$("#couponAmount").bind("keyup",numInputWithLength);
	    	//2 折扣券   
	    	}else if(this.value == '2'){
	    		$("#quota").show();
	    		$("#couponMax").show();
	    		$("#discount").show();
	    		$("#xianjin").hide();
	    		$("#full").hide();
	    		$("#meetPrice").hide();
	    		$("#reduction").hide();
	    		$("#couponAmount").unbind("keyup");
	    		$("#couponAmount").bind("keyup",numInput4Discount);
	    	//3 现金券
	    	}else if(this.value == '3'){
	    		$("#xianjin").show();
	    		$("#discount").hide();
	    		$("#full").hide();
	    		$("#meetPrice").hide();
	    		$("#reduction").hide();
	    		$("#quota").hide();
	    		$("#couponMax").hide();
	    		$("#couponAmount").unbind("keyup");
	    		$("#couponAmount").bind("keyup",numInputWithLength);
	    	}
			
			$("#meetPrice").val("");
			$("#couponAmount").val("");
			document.getElementById("couponAmount").value2='';
			$("#couponMax").val("");
			
		});
		$("input[name='couponUsingRange']").click(function(){
			$("#redioShopBottonSpan").hide();
			$("#redioGoodBottonSpan").hide();
			$("#ztreeid").hide();
			//店铺通用类
			if($(this).val()==2){
				$("#redioShopBottonSpan").show();
				$("#goodId").html("店铺");
				$("#itemCount").html(0);
				resetTwoList();
			}else if($(this).val()==3){
				$("#ztreeid").show();
				$("#itemCountShow").hide();
			}else if($(this).val()==4){
				$("#itemCount").html(0);
				$("#goodId").html("商品");
				$("#redioGoodBottonSpan").show();
				resetTwoList();
			}else{
				$("#itemCountShow").hide();
				$("#ztreeid").hide();
			}
			$("#numId").val("");
			$("#ListToShow").empty();
		});
		
	});

	function skuListToShow(n,s){
		$.ajax({
	        url:"${ctx}/couponInfo/getSkusToShow",
	        type:"post",
	        traditional: true, 
	        data:{
	        	skuIds : realList.slice((n - 1) * s,n*s),
	        	pageNo : n,
	        	pageSize : s,
	        	count : realList.length,
	        	funcName : 'skuListToShow'
	        },
	        dataType:'html',
	        success:function(data){
	        	$("#ListToShow").html(data);
	        }
	    });
	}
	
	function shopListToShow(n,s){
		$.ajax({
            url:"${ctx}/couponInfo/getShopsToShow",
            type:"post",
            traditional: true, 
            data:{
            	shopIds : realList.slice((n - 1) * s,n*s),
            	pageNo : n,
            	pageSize : s,
            	count : realList.length,
            	funcName : 'shopListToShow'
            },
            dataType:'html',
            success:function(data){
            	$("#ListToShow").html(data);
            }
        });
	}
	// 两个数组重置
	function resetTwoList() {
		backedList = new Array();
		realList = new Array();
	}
	
	function checkListConsistency() {
		if ($(realList).not(backedList).length !== 0
				|| $(backedList).not(realList).length !== 0) {
			realList = backedList.slice();
		}
	}
	
	function redioShopBotton(){
		if (firstLoadShop) {
			backedList = $("#numId").val().split(",");
			backedList.pop();
			realList = backedList.slice();
			firstLoadShop = false;
		}
		checkListConsistency();
		showItem();
		$("#itemCountShow").show();
		$("#ztreeid").hide();
	}
	
	function redioGoodBotton(){
		if (firstLoadSku) {
			backedList = $("#numId").val().split(",");
			backedList.pop();
			realList = backedList.slice();
			firstLoadSku = false;
		}
		checkListConsistency();
		pagesub();
		$("#itemCountShow").show();
		$("#ztreeid").hide();
	}
	
	//弹出商品选择框
	function selectItemWin(){
		if($("#treeTable").length == 0){
			showItem();
		}else{
			$("#addItemDiv").modal("show");
		}
	}
	//查询商品
	function showItem(){
		page(1,10);
	}
	// 列表加载完了，要初始化checkbox的状态
	function checkboxsInit(colIndex, divId) {
		var isAllCheckOn = true;
		if ($(divId + ' ' + "#treeTable tr:gt(0)").length == 0) {
			isAllCheckOn=false;
		} else {
			$(divId + ' ' + "#treeTable tr:gt(0)").each(function() {
				var tr = $(this).children();
				var id = $(tr[colIndex]).text();
				if (realList.indexOf(id) != -1) {
					$(tr[0]).find("input").prop("checked",true);
				} else {
					isAllCheckOn = false;
				}
			});
		}
		return isAllCheckOn;
	}
	//翻页
	function page(n,s){
		$.jBox.tip("正在加载列表，请稍等",'loading',{opacity:0});
		if($("input[name='couponUsingRange']:checked").val()==2){
			$("#page").val(n);
			$("#rows").val(s);
			$.ajax({
	            url:"${ctx}/couponInfo/getShopList",
	            type:"post",
	            data:$("#selectItemForm").serialize()+"&num="+$("#numId").val(),
	            dataType:'html',
	            success:function(data){
	            	$("#itemListDiv").html(data);
	            	$.jBox.closeTip();
	            	$("#addItemDiv").modal("show");
	            	if (checkboxsInit(2,'#itemListDiv')) {
	            		$("#selAll").prop('checked',true);
	            	};
	            }
	        });
		}else if($("input[name='couponUsingRange']:checked").val()==4){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			var url = $("#searchForm1").attr("action");
			var searchFormObj = $('#searchForm1').serialize()+"&num="+$("#numId").val();
			$.ajax({
				url : url,
				type : "post",
				data : searchFormObj,
				cache : false,
				success : function(html) {
					$("#skuList .goodList").html(html);
					$.jBox.closeTip();
					if (checkboxsInit(4,'#skuList .goodList')) {
	            		$("#selAllCheck").prop('checked',true);
	            	};
				}
			});
		}
    }
	
	function loadsub(flag) {
		var html;
		var url;
		switch (flag) {
		case '1':
			var pid = $("#platformId1").val();
			if (pid == null || pid <= 0) {
				pid = '${goods.platformId1}';
			}
			html = "<option value=''>二级分类</option>";
			url = "${ctx}/brand/getChildCategory?pCid=" + pid;
			$("#platformId2").html("<option value=''>二级分类</option>");
			$("#platformId2").val();
			$("#cid").html("<option value=''>三级分类</option>");
			$("#cid").val();
			$("#brand").empty();
			break;
		case '2':
			var pid = $("#platformId2").val();
			if (pid == null || pid <= 0) {
				pid = '${goods.platformId2}';
			}
			html = "<option value=''>三级分类</option>";
			url = "${ctx}/brand/getChildCategory?pCid=" + pid;
			$("#cid").html("<option value=''>三级分类</option>");
			$("#cid").val();
			$("#brand").empty();
			break;
		case '3':
			var cid = $("#cid").val();
			var secondCid = $("#platformId2").val();

			url = "${ctx}/brand/getCategoryBrand?secondCid=" + secondCid
					+ "&thirdCid=" + cid;
			break;
		}
		$
				.ajax({
					url : url,
					type : "post",
					dataType : 'json',
					success : function(data) {
						$(data)
								.each(
										function(i, item) {
											if ('1' == flag) {
												/* var a =item.categoryCid;
												$.jBox.info(a); */
												/* var b ='${twoLevelId}';
												$.jBox.info(b); */
												html += "<option ";
												if (item.categoryCid == '${goods.platformId2}') {
													html += " selected";
												}
												html += " value='"
														+ item.categoryCid
														+ "'>"
														+ item.categoryCName
														+ "</option>";
											} else if ('2' == flag) {
												/* $.jBox.info(${itemDTO.cid});
												$.jBox.info(item.categoryCid); */
												html += "<option ";
												if (item.categoryCid == '${goods.cid}') {
													html += " selected ";
												}
												html += "value='"
														+ item.categoryCid
														+ "'>"
														+ item.categoryCName
														+ "</option>";
											}
										});
						switch (flag) {
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
	function pagesub(n, s) {
		$.jBox.tip("正在加载列表，请稍等",'loading',{opacity:0});
		if(!'${isView }'){
		$("#skuList").modal('show');
		if (n > 0) {

		} else {
			n = 1;
		}
		if (s > 0) {

		} else {
			s = 10;
		}
		$("#pageNo").val(n);
		$("#pageSize").val(s);

		var url = $("#searchForm1").attr("action");
		var searchFormObj = $('#searchForm1').serialize()+"&num="+$("#numId").val();
		$.ajax({
			url : url,
			type : "post",
			data : searchFormObj,
			cache : false,
			success : function(html) {
				$.jBox.closeTip();
				$("#skuList .goodList").html(html);
				if (checkboxsInit(4,'#skuList .goodList')) {
            		$("#selAllCheck").prop('checked',true);
            	};
			}
		});
		}
	}
	function closeskuListDiv() {
		realList = backedList.slice();
		$("#skuList").modal('hide');
	}
	function closeshopListDiv() {
		realList = backedList.slice();
		$("#addItemDiv").modal('hide');
	}
	//保存促销信息
	function saveFrPromotion(){
		
		$("#linkName").val();
		$("#linkUrl").val();
		$("#addFriendlylinkItemForm").validate({
            rules: {
            	"linkName": "required",
            	"linkUrl": "required"
            }
        });
		var isValide = $("#addFriendlylinkItemForm").valid();
		if(isValide){
			var ids = [];
			
			$("#addFriendlylinkItemForm").submit();
		}else{
			 $("#subId").hideMsg();
		}
		
	}
	
	
	function numInput4Discount() {
		var obj = this;
		if (obj.value2===undefined) {
			obj.value2='';
		}
		var value= obj.value;
		if(!value)
			return;
		if(value.search(/^\d(\.\d{0,2})?$/)==-1 // 找不到x.xx
				||value.search(/^0\.00$/)==0) { // 找到0.00
			if (value.search(/^10(\.0{0,2})?$/)==-1) { // 找不到10.00
				obj.value=obj.value2;
			}
			else {
				obj.value2=value;
			}
		}
		else {
			obj.value2=value;
		}
	}
	function numInputWithLength(length,obj){
		if (typeof length !== "number") {
			length = 6;
		}
		obj=obj||this;
		if(obj.value==obj.value2)
			return;
		if (parseInt(obj.value)==0) {
			obj.value=(obj.value2)?obj.value2:'';
			return;
		}
		eval("var re = /^\\d{0,"+length+"}$/;");
		if(obj.value.search(re)==-1)
			obj.value=(obj.value2)?obj.value2:'';
		else
			obj.value2=obj.value;
	}
	 //改变选择框平台id
	function changePlatformId(val){
		top.$.jBox.confirm("改变平台类型后先前选择的部分商品将清空!","系统提示",function(v,h,f){
            if(v == "ok"){
          	  	if(val == "1" || val == "3" || val == "4") val = "0";
      		    $("input[name='itemQueryInDTO.platformId']").val(val);
      		    $("#shopPlatformId").val(val);
      			var itemCount = $("#itemCount").html("0");
      			$("#numId").val("");
            }
        },{buttonsFocus:1});
	}
	Array.prototype.del=function(n) {
		if(n<0)
			return this;
		else
			return this.slice(0,n).concat(this.slice(n+1,this.length));
	}
	
	// 删除某个元素并返回新数组
	function delFromList(list, id) {
		var delIndex = -1;
		for (var i=0; i<list.length; i++) {
			if (list[i]==id) {
				delIndex = i;
				break;
			}
		}
		if (delIndex == -1)
			return list;
		else
			return list.del(delIndex);
	}
</script>
<style>
label.label-left {
	width: 25%;
	text-align: right;
}
</style>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<div class="container-fluid">
				<form name="addFriendlylinkItemForm" modelAttribute="friendlylinkItem" method="post" id="addFriendlylinkItemForm" action="${ctx}/friendlylink/savefriendlylinkitem" >
	                <legend ><span class="content-body-bg">基本信息</span></legend>
	                <input name="id" id="id" maxlength="50"  value="${friendlylinkItemDB.id}"  type="hidden" class="form-control"/>
	                 <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">链接名称：</div>
	                    <div class="span7">
	                        <input name="linkName" id="linkName" maxlength="100" type="text" value="${friendlylinkItemDB.linkName}" class="form-control" title="请输入友情链接名称"/>
	                    </div>
	                </div>
	                <div class="row-fluid" style="margin-top: 10px">
	                    <div class="span1">链接URL：</div>
	                    <div class="span7">
	                        <input name="linkUrl" id="linkUrl" maxlength="20" type="text" value="${friendlylinkItemDB.linkUrl}" class="form-control" title="请输入友情链接名称"/>
	                    </div>
	                </div>
	                
	                
	                </br>
	                
			        <div style="display:none" id="ztreeid">
			               <ul id="menuTree" class="ztree" style="width:230px; overflow:auto;"></ul>
		            </div>
		            <br/>
					<div class="span2">
                	<input type="button" class="btn btn-primary" onclick="saveFrPromotion()" id="subId" loadMsg="正在保存..." value="保存"/>
                    </div>
				</form>
            </div>
		</div>
	</div>	
	<!--店铺显示-->
<div class="modal hide fade" id="addItemDiv" style="margin-top:-40px;margin-left:-540px;width:980px">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h5 id="titleId">选择店铺</h5>
    </div>
    <div class="container-fluid">
	    <form name="selectItemForm" id="selectItemForm" action="" method="post">
		    <input id="page" name="page" type="hidden" value="" />
		    <input id="rows" name="rows" type="hidden" value="" />
		    <input id="shopPlatformId" name="shopPlatformId" type="hidden" value="0" />
	    	<div class="row-fluid">
	            <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="shopName" title="店铺名称">
                                                                  店铺名称
                        </label>
                        <input name="shopName" id="shopName"  type="text" class="form-control" value="${shop.shopName}" />
                    </div>
                    <div class="span6">
                        <label class="label-control" for="sellerId" title="商家编号">
                                                                   商家编号
                        </label>
                        <input type="text"  id="sellerId" name="sellerId"  value="${shop.sellerId}" title="只能是数字"/>
                        &nbsp;&nbsp;&nbsp;<input type="button" class="btn btn-primary" onclick="showItem()" value="搜索">
                    </div>
                </div>
	        </div>
	    </form>
    </div>
    <div id="itemListDiv" style="max-height:310px;overflow-y:auto;overflow-x:hidden;"></div>
    <div class="modal-footer">
			<a href="javascript:closeshopListDiv()" class="btn">关闭</a> <a
				href="javascript:getShopList()" class="btn btn-primary">确定</a>
	</div>
</div>
	
	
	
<!--商品弹出框-->
	<div class="modal hide fade" id="skuList"
		style="margin-top:-40px;margin-left:-540px;width:980px;height:450px">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>选择商品</h3>
		</div>
		<div class="modal-body" style="max-height:310px;overflow-x:hidden" >
			<div class="container-fluid">
				<form:form name="searchForm" modelAttribute="friendlylinkItem" id="searchForm1"
					method="post" action="${ctx}/couponInfo/gsListShop">
					<input id="pageNo" name="page" type="hidden" />
					<input id="pageSize" name="rows" type="hidden" />
					<input id="ItemPlatformId" name="itemQueryInDTO.platformId" type="hidden" value='0'/>
					<div class="row-fluid" style="margin-top: 10px;width:980px">
						<div class="span8" style="width:980px">
							<label>平台分类：</label>
						</div>
					</div>
					<div class="row-fluid" style="margin-top: 10px;width:980px">
						<div class="span8" style="width:980px">
						<div class="span4" style="width:250px">
							<label>商品名称：&nbsp;</label><input type="text"
								name="itemQueryInDTO.itemName" class="input-medium" />
						</div>
						<div class="span4" style="width:250px">
							<label>商品编码：</label><input type="text"
								class="input-medium" title="只能是数字" name="itemQueryInDTO.id"/>
						</div>
						</div>
					</div>
				</form:form>
			</div>
			<div class="container-fluid goodList" style="text-align: center;height:200px" >
			</div>
		</div>
		<div class="modal-footer">
			<a href="javascript:closeskuListDiv()" class="btn">关闭</a> <a
				href="javascript:getSku()" class="btn btn-primary">确定</a>
		</div>
	</div>	
</body>
</html>