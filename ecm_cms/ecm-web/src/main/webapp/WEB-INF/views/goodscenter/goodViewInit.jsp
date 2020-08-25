<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商品查看</title>
<meta name="decorator" content="default"/>

<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/lang/zh_cn/zh_cn.js" type="text/javascript"></script>


<style>
    .row-fluid .span1 {
        width:8.5%;
    }
</style>
<script type="application/javascript">
var allItemSalesAttrString;
var allItemAttrString;
var editor;
$(document).ready(function(){
    	var describeUrl = UE.getEditor('describeUrl',{
			serverUrl:'${ctx}/ueditor/exec',
				imageUrlPrefix:"${filePath}"
		});
	
		describeUrl.addListener('ready',function(){
			describeUrl.setContent($("#describeUrl1").val());
			var imgObjs = $("#ueditor_0").contents().find("img");
			imgObjs.each(function () {
				if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
					$(this).attr("src","${filePath}"+$(this).attr("src"));
           		}
           		if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
        	   		$(this).attr("_src","${filePath}"+$(this).attr("_src"));
           		}
   	 		});
   	 		describeUrl.setDisabled('fullscreen');
		});


});


</script>
</head>
<body>
<div class="content sub-content">
<div class="content-body content-sub-body">
<form id="goodsForm" name="goodsForm" method="post" action="editS">

<div class="container-fluid">
    <legend ><span class="content-body-bg">基本信息</span></legend>
    <div class="row-fluid">
        <div class="span1">
            平台分类
        </div>
        <div class="span9">
            <select name="platformId1" id="platformId1"  class="form-control input-medium" readonly>
                <option value="${itemMap['cid']}">${itemMap['cname']}</option>
            </select>
            <select name="platformId2" id="platformId2"  class="form-control input-medium" readonly>
                <option value="${itemMap['subcid']}">${itemMap['subcname']}</option>
            </select>
            <select name="cid" id="cid"   class="form-control input-medium"  readonly>
                <option value="${itemMap['tcid']}">${itemMap['tcname']}</option>
            </select>
        </div>
    </div>

    <div class="row-fluid" style="margin-top: 10px">
        <div class="span1">
            商品名称
        </div>
        <div class="span7">
            <input name="itemName" id="itemName" value="${goods.itemName}"  type="text" class="form-control" readonly/>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span1">
            市场指导价
        </div>
        <div class="span3 input-append">
            <input name="marketPrice" id="marketPrice" value="${goods.marketPrice}"  type="text" class="form-control" readonly/>
            <span class="add-on">元</span>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span1">
            商品毛重
        </div>
        <div class="span7">
            <input name="weight" id="weight" value="${goods.weight}"  type="text" class="form-control" readonly/>
            <select name="weightUnit" class="input-mini" style="margin-bottom: 10px" readonly>
                <option value="${goods.weightUnit=='kg'}">${goods.weightUnit}</option>
            </select>
        </div>

    </div>
    <div class="row-fluid">
        <div class="span1">
            商品体积
        </div>
        <div class="span3 input-append">
            <input name="volume" id="volume" value="${goods.volume}"  type="text" class="form-control" readonly/>
            <span class="add-on">m³</span>
        </div>

    </div>
    <div class="row-fluid">
        <div class="span1">
            商品产地
        </div>
        <div class="span7">
            <input name="origin" id="origin" value="${goods.origin}"  type="text" class="form-control" readonly/>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span1">
            商品关键字
        </div>
        <div class="span7">
            <input type="text" name="keywords" id="keywords" value="${goods.keywords}" readonly/>
        </div>
    </div>
    <div class="row-fluid" >
        <div class="span1">
            品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌
        </div>
        <div class="span7">
            <select name="brand" id="brand"  title="请选择品牌">
                <c:if test="${brand!=null}">
                    <option value="${brand.brandId}">${brand.brandName}</option>
                </c:if>
            </select>
        </div>
    </div>
</div>

<div class="container-fluid" id="itemAttrDivAll">
    <legend ><span class="content-body-bg">类目属性</span></legend>

    <c:forEach items="${itemAttrList}" var="t">

        <div class='row-fluid' style='margin-top: 10px'>
            <div class='span6'>
                <input type='text' name='itemAttrName' value='${t.name}' class='input-medium' readonly/>
            </div>
        </div>
        <div class='row-fluid'>
            <c:forEach items="${t.values}" var="n">
                <c:set var="tempStr" value="${t.id}${':'}${n.id}${';'}"/>
                <div class='span1'>
                    <input type='checkbox' name='itemAttrValueId' value='${n.id}' <c:if test="${fn:contains(goods.attributesStr, tempStr) }">checked</c:if> >
                    <input type='text' name='itemAttrValue' value='${n.name}' class='input-mini' readonly>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
    <div class="row-fluid stay">
        <div class="span12 stay"></div>
    </div>

</div>
<div class="container-fluid" id="salesAttrDivAll">
    <legend ><span class="content-body-bg">销售属性</span></legend>
    <c:forEach items="${itemSalesAttrList}" var="t">
        <div class='row-fluid' style='margin-top: 10px'>
            <div class='span6'>
                <input type='text' name='itemAttrName' value='${t.name}' class='input-medium' readonly/>
            </div>
        </div>
        <div class='row-fluid'>
            <c:forEach items="${t.values}" var="n">
                <c:set var="tempStr" value="${t.id}${':'}${n.id}${';'}"/>
                <div class='span1'>
                    <input type='checkbox' name='itemAttrValueId' value='${n.id}' <c:if test="${fn:contains(goods.attrSaleStr, tempStr) }">checked</c:if> >
                    <input type='text' name='itemAttrValue' value='${n.name}' class='input-mini' readonly>
                </div>
            </c:forEach>

        </div>
    </c:forEach>
    <div class="row-fluid stay">
        <div class="span12 stay"></div>
    </div>
</div>
<div class="container-fluid">
    <legend ><span class="content-body-bg">商品图片</span></legend>
    <div class="row-fluid" id="picAddDiv">
        <c:forEach items="${goods.picUrls}" var="pic">
            <a href='${filePath}${pic}' target='_blank'><img src='${filePath}${pic}' class='img-polaroid' style='width:50px;height:50px'></a>
        </c:forEach>
    </div>
    <div class="row-fluid">
        <div class="span12"></div>
    </div>
</div>
<div class="container-fluid">
    <legend ><span class="content-body-bg">商品详情</span></legend>
    <div class="row-fluid">
        <div class="span9">
            <textarea id="describeUrl" name="describeUrl" style="width:700px;height:300px;" title="请填写商品描述"></textarea>
            <textarea id="describeUrl1" style="display:none;" title="请填写商品描述">${goods.describeUrl}</textarea>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span12"></div>
    </div>
</div>
<div class="container-fluid">
    <legend ><span class="content-body-bg">其它信息</span></legend>
    <div class="row-fluid">
        <div class="span6">
            <label class="label-control" for="packingList" title="包装清单">包装清单</label>
            <input name="packingList" id="packingList" value="${goods.packingList}"  type="text" class="form-control" title="最大只能输入16个字符"/>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span6">
            <label class="label-control" for="afterService" title="售后服务">售后服务</label>
            <input name="afterService" id="afterService" value="${goods.afterService}" type="text" class="form-control" title="最大只能输入16个字符"/>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12"></div>
    </div>


</div>

</form>
</div>
</div>


</body>
</html>