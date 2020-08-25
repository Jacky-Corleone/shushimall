<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://www.camelot.com/jsp/el/sku/functions"
	prefix="skuFn"%>
<html>
<head>
<title>二手商品信息</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/lang/zh_cn/zh_cn.js" type="text/javascript"></script>

<script type="text/javascript">
//响应后台错误，给出提示	
$(document).ready(function() {
	
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
	var describeDetail = UE.getEditor('describeDetail',{
		serverUrl:'${ctx}/ueditor/exec',
		imageUrlPrefix:"${filePath}"
	});
	
	describeDetail.addListener('ready',function(){
		describeDetail.setContent($("#describeDetail1").val());
		var imgObjs = $("#ueditor_1").contents().find("img");
		imgObjs.each(function () {
           if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
        	   $(this).attr("src","${filePath}"+$(this).attr("src"));
           }
           if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
        	   $(this).attr("_src","${filePath}"+$(this).attr("_src"));
           }
   	 	});
   	 	describeDetail.setDisabled('fullscreen');
	});
	
    //图片查看
    $('.showimg').fancyzoom({
        Speed: 400,
        showoverlay: false,
        imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
    });

	if('${msg}'!=null&&'${msg}'!=""){
		$.jBox.info('${msg}');
	}
});
</script>
</head>
<body>
	<div class="content sub-content">
		<div class="content-body content-sub-body">
			<div class="container-fluid">
				<legend>
					<span class="content-body-bg">基本信息</span>
				</legend>
				<div class="row-fluid">
					<div class="span6">
						<label class="label-control" for="cid" title="平台分类"> 平台分类：
						</label> <label>${oneName}--></label> <label>${twoName}--></label> <label>${threeName}</label>
					</div>
					<div class="span6">
						<label class="label-control" for="itemName" title="商品名称">
							商品名称: </label> <label>${itemOldDTO.itemName}</label>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span12"></div>
				</div>
			</div>
		</div>

		<div class="container-fluid">
			<legend>
				<span class="content-body-bg">商品信息</span>
			</legend>
			<div class="row-fluid">
				<div class="span6">
					<label class="label-control" for="price" title="价格">
						价格: </label> <label>${itemOldDTO.price} 元</label>
				</div>
				<div class="span6">
					<label class="label-control" for="priceOld" title="原价">
						原价: </label> <label>${itemOldDTO.priceOld} 元</label>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<label class="label-control" for="recency" title="新旧程度"> 新旧程度：</label>
					<label>
						<c:if test="${itemOldDTO.recency==10}">全新</c:if> 
						<c:if test="${itemOldDTO.recency==9}">9成新</c:if>
						<c:if test="${itemOldDTO.recency==8}">8成新</c:if>
						<c:if test="${itemOldDTO.recency==7}">7成新</c:if>
						<c:if test="${itemOldDTO.recency==6}">6成新</c:if>
						<c:if test="${itemOldDTO.recency==5}">5成新</c:if>
					</label>
				</div>
				<div class="span6">
					<label class="label-control" for="freight" title="运费">
						运费: </label> <label>${itemOldDTO.freight} 元</label>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<label class="label-control" for="sellerName" title="联系人"> 联系人：</label>
					<label>${itemOldDTO.sellerName}</label>
				</div>
				<div class="span6">
					<label class="label-control" for="sellerTel" title="联系电话">
						联系方式: </label> <label>${itemOldDTO.sellerTel}</label>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<label class="label-control" for="cityName" title="所在地"> 所在地：</label>
					<label>${itemOldDTO.provinceName}   ${itemOldDTO.cityName}   ${itemOldDTO.districtName}</label>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12"></div>
			</div>
		</div>

		<div class="container-fluid">
			<legend>
				<span class="content-body-bg">商品详情</span>
			</legend>
			<div class="row-fluid">
				<div class="span12">
				    <legend ><span class="content-body-bg">商品图片：</span></legend>
					<c:forEach items="${itemOldPicList}" var="itemOldPic">
						<c:if test="${itemOldPic.pictureUrl!=null&&itemOldPic.pictureUrl!=''}">
							<img class="showimg" style="height: 100px;width: 100px;padding-left: 20px"  src="${filePath}/${itemOldPic.pictureUrl}" >
						</c:if>
					</c:forEach>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12"></div>
			</div>
			</div>
		<div class="container-fluid">
			<legend ><span class="content-body-bg" for="describeUr">商品描述：</span></legend>
			<div class="row-fluid">
				<div class="span9">
                        <textarea id="describeUrl" name="describeUrl" style="width:700px;height:300px;" title="请填写商品描述"></textarea>
                        <textarea id="describeUrl1" style="display:none;" title="请填写商品描述">${itemOldDTO.describeUr}</textarea>
                </div>
			</div>
			
			<div class="row-fluid">
				<div class="span12"></div>
			</div>
		</div>
		<div class="container-fluid">
			<legend ><span class="content-body-bg" for="describeUr">商品详情：</span></legend>
			<div class="row-fluid">
				<div class="span9">
                        <textarea id="describeDetail" name="describeDetail" style="width:700px;height:300px;"></textarea>
                        <textarea id="describeDetail1" style="display:none;">${itemOldDTO.describeDetail}</textarea>
                    </div>
			</div>
			
			<div class="row-fluid">
				<div class="span12"></div>
			</div>
		</div>
		<div class="container-fluid">
			<legend>
				<span class="content-body-bg"></span>
			</legend>
			<div class="row-fluid">
				<label>驳回原因：${itemOldDTO.comment}</label>
			</div>
		</div>
			
		<div class="row-fluid">
			<div class="span12"></div>
		</div>
		<div class="row-fluid">
			<div class="span12"></div>
		</div>
		<div class="row-fluid">
			<div class="span3">
				<input class="btn btn-primary" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</div>
	</div>

</body>
</html>