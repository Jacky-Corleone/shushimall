<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://www.camelot.com/jsp/el/sku/functions" prefix="skuFn" %>
<html>
<head>
	<title>店铺商品查看</title>
	<meta name="decorator" content="default"/>

    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.js"></script>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"/>
	<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		$(function(){
			if($("#platformId").val()=='2'){
				$("#showId").attr("style","display:block");
				$("#editor_authId").html($("#editor_auth").val());
			}
			if($("#addSourceId").val()==3){
				$("#normalId").show();
				$("#addService").show();
				$("#baseService").show();
				$("#auxService").show();
			}else{
				$("#normalId").hide();
				$("#addService").hide();
				$("#baseService").hide();
				$("#auxService").hide()
			}
			if($("#addSourceId").val()==1){
				$("#searchId").hide();
				$("#roomtypeId").hide();
				
			}else{
				$("#searchId").show();
				$("#roomtypeId").show();
			}
			
			var ue = UE.getEditor('editor',{
				serverUrl:'${ctx}/ueditor/exec',
				imageUrlPrefix:"${filePath}"
			});
			ue.addListener('ready',function(){
				ue.setContent($("#editor1").val());
				var imgObjs = $("#ueditor_0").contents().find("img");
				imgObjs.each(function () {
		           if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
		        	   $(this).attr("src","${filePath}"+$(this).attr("src"));
		           }
		           if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
		        	   $(this).attr("_src","${filePath}"+$(this).attr("_src"));
		           }
		   	 	});
		   	 	
		        ue.setDisabled('fullscreen');
	   	 	});
			var specifications = UE.getEditor('specifications',{
				serverUrl:'${ctx}/ueditor/exec',
				imageUrlPrefix:"${filePath}"
			});
			specifications.addListener('ready',function(){
				specifications.setContent($("#specifications1").val());
				var imgObjs = $("#ueditor_1").contents().find("img");
				imgObjs.each(function () {
		           if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
		        	   $(this).attr("src","${filePath}"+$(this).attr("src"));
		           }
		           if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
		        	   $(this).attr("_src","${filePath}"+$(this).attr("_src"));
		           }
		   	 	});
		   	 	
				specifications.setDisabled('fullscreen');
	   	 	});
			
			var afterServiceUE = UE.getEditor('afterService',{
				serverUrl:'${ctx}/ueditor/exec',
				imageUrlPrefix:"${filePath}"
			});
			afterServiceUE.addListener('ready',function(){
				afterServiceUE.setContent($("#afterService1").val());
				var imgObjs = $("#ueditor_2").contents().find("img");
				imgObjs.each(function () {
		           if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
		        	   $(this).attr("src","${filePath}"+$(this).attr("src"));
		           }
		           if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
		        	   $(this).attr("_src","${filePath}"+$(this).attr("_src"));
		           }
		   	 	});
		   	 	
				afterServiceUE.setDisabled('fullscreen');
	   	 	});
			
			var aueEitor = UE.getEditor('editor_authId',{
				serverUrl:'${ctx}/ueditor/exec',
				imageUrlPrefix:"${filePath}"
			});
			
			aueEitor.addListener('ready',function(){
				aueEitor.setContent($("#editor_authId1").val());
				var imgObjs = $("#ueditor_3").contents().find("img");
				imgObjs.each(function () {
		           if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
		        	   $(this).attr("src","${filePath}"+$(this).attr("src"));
		           }
		           if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
		        	   $(this).attr("_src","${filePath}"+$(this).attr("_src"));
		           }
		   	 	});
		   	 	
		        aueEitor.setDisabled('fullscreen');
	   	 	});
		});
		
	</script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
        <input type="hidden" id="platformId" value="${itemDTO.platformId}"/>
        <input type="hidden" id="addSourceId" value="${itemDTO.addSource}"/>
            <legend ><span class="content-body-bg">基本信息</span></legend>
            <div class="row-fluid">
                <div class="span9">
                    <label class="label-control" for="cid" title="平台分类">
                        平台分类
                    </label>
                    <label>${itemMap['cname']}--></label>
                    <label>${itemMap['subcname']}--></label>
                    <label>${itemMap['tcname']}</label>

                </div>

            </div>
            <!-- <div class="row-fluid">
                <div class="span6">
                    <label class="label-control" title="店铺类目">
                        店铺类目：
                    </label>
                    <label>${scLevOneName}--></label>
                   <!--  <label>${scLevTwoName}</label>
                </div>
            </div>
             -->
            <div class="row-fluid">
                <div class="span6">
                    <label class="label-control" title="商品名称">
                        	商品名称:
                    </label>
                    <label>${itemDTO.itemName}</label>
                </div>
                <div class="span6">
                    <label class="label-control"  title="商品名称">
                        	广告语:
                    </label>
                    <label>${itemDTO.ad}</label>
                </div>
                </div>
                 <div class="row-fluid">
                 <div class="span6">
                    <label class="label-control"  title="商品类型">
                        	商品类型:
                    </label>
                    <label>
                    <c:if test="${itemDTO.addSource==1}">普通商品</c:if>
                    <c:if test="${itemDTO.addSource==3}">套装商品</c:if>
                    <c:if test="${itemDTO.addSource==4}">基础服务商品</c:if>
                    <c:if test="${itemDTO.addSource==5}">增值服务商品</c:if>
                    <c:if test="${itemDTO.addSource==6}">辅助材料商品</c:if>
                   </label>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <legend ><span class="content-body-bg">商品属性</span></legend>
            <div class="row-fluid stay" id="brandLineDiv" >
                <div class="span6 stay">
                    <label class="label-control" title="品牌">品牌：
                    </label>
                    <label>${itemDTO.brandName}</label>
                 </div>
             </div>
             <c:forEach items="${productAttrs}" var="attr" varStatus="vs">
	             <div class="row-fluid">
	                 <div class="span6 input-append">
	                     <label class="label-control" title="${attr.name}">&nbsp;${attr.name}:</label>
	                     <label>
	                     	<c:forEach items="${attr.values}" var="attrVal">
	                     		${attrVal.name}<c:if test="${vs.count}>1">,</c:if>
	                     	</c:forEach>
	                     </label>
	                 </div>
	             </div>
             </c:forEach>
         </div>

		 <div class="container-fluid">
             <legend ><span class="content-body-bg">商品信息</span></legend>
             <div class="row-fluid">
                 <div class="span6 input-append">
                     <label class="label-control" title="市场指导价">
                         	市场指导价:
                     </label>
                     <label>${itemDTO.marketPrice}</label>
                </div>
                <div class="span6">
                    <label class="label-control" title="库存量">库存量:</label>
                    <label>${itemDTO.inventory}</label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6 input-append">
                    <label class="label-control" title="底价">&nbsp;底价:</label>
                    <label>${itemDTO.marketPrice2} 元</label>
                </div>
                <div class="span6">
                    <label class="label-control" title="商品毛重">商品毛重:</label>
                    <label>${itemDTO.weight} ${empty itemDTO.weight ? "" : itemDTO.weightUnit}</label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6 input-append">
                    <label class="label-control" title="报价">&nbsp;报价:</label>
                    <label>${itemDTO.guidePrice} 元</label>
                </div>
                <div class="span6">
                    <label class="label-control" title="商品产地">商品产地:</label>
                    <label>${itemDTO.origin}</label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6 input-append">
                    <label class="label-control" title="商品体积">商品体积:</label>
                    <label>${itemDTO.volume} ${empty itemDTO.volume ? "" : "m³"}</label>
                </div>
                <div class="span6">
                    <label class="label-control" title="运费模版">运费模版:</label>
                    <label>${shopFreightTemplateDTO.templateName}</label>
                </div>
            </div>
            <div class="row-fluid">
                <table class="table table-striped table-bordered table-condensed td-cen hhtd" style="width:51%;">
		            <thead>
			            <tr>
			            	<th width="20">编号</th>
			                <th width="40%">采购量</th>
			                <th width="20%">销售价</th>
			                <th width="20%">适用范围</th>
			            </tr>
		            </thead>
		            <tbody>
		            	<c:forEach items="${itemDTO.sellPrices}" var="sellPrice" varStatus="vs">
			            	<tr>
			            		<td>${vs.count}</td>
			                    <td>${sellPrice.minNum} ~ ${sellPrice.maxNum}</td>
			                    <td>${sellPrice.sellPrice}</td>
			                    <td>${sellPrice.areaName}</td>
			                </tr>
		                </c:forEach>
		            </tbody>
		        </table>
            </div>
        </div>
        
        <div class="container-fluid">
            <legend ><span class="content-body-bg">销售属性</span></legend>
                <div class="row-fluid" id="normalId" style="display:none">
			           <c:forEach items="${addMap}" var="addmap">
			               <label class="label-control" > 商品名称：     ${addmap.value.itemName} &nbsp;&nbsp;数量  ： ${addmap.value.subNum} </label>
			               <br/>
			           </c:forEach>
			           <br/>
		         </div>
	             <c:forEach items="${saleAttrs}" var="attr" varStatus="vs">
		             <div class="row-fluid">
		                 <div class="span6 input-append">
		                     <label class="label-control" title="${attr.name}">&nbsp;${attr.name}:</label>
		                     <label>
		                     	<c:forEach items="${attr.values}" var="attrVal" varStatus="vsAttrVal">
		                     		<c:if test="${vsAttrVal.count > 1}">,</c:if>${attrVal.name}
		                     	</c:forEach>
		                     </label>
		                 </div>
		             </div>
	             </c:forEach>
	             <div class="row-fluid">
	                 <table class="table table-striped table-bordered table-condensed td-cen hhtd" style="width:71%;">
			            <thead>
				            <tr>
				            	<c:forEach items="${saleAttrs}" var="saleAttr" varStatus="vs">
				                	<th width="20">${saleAttr.name}</th>
				                </c:forEach>
				                <th width="40%">销售价</th>
				                <th width="15%">成本价</th>
				                <th width="15%">库存</th>
				            </tr>
			            </thead>
			            <tbody>
			            <c:forEach items="${itemDTO.skuInfos}" var="sku">
			            	<tr>
			            		<c:forEach items="${saleAttrs}" var="saleAttr" varStatus="vsSaleAttr">
				                	<td>${skuFn:getAttrNameByIdArr(saleAttrs, sku.attributes, vsSaleAttr.index)}</td>
				                </c:forEach>
			                    <td>
			                    	<table class="table table-striped table-bordered table-condensed td-cen hhtd" style="width:100%;">
										<tr>
							            	<th width="20">编号</th>
							                <th width="50%">采购量</th>
							                <th width="15%">销售价</th>
							                <th width="15%">适用范围</th>
							            </tr>
										<c:forEach items="${sku.sellPrices}" var="sellPrice" varStatus="vsSellPrice">
											<tr>
							            		<td>${vsSellPrice.count}</td>
							                    <td>${sellPrice.minNum} ~ ${sellPrice.maxNum}</td>
							                    <td>${sellPrice.sellPrice}</td>
							                    <td>${sellPrice.areaName}</td>
							                </tr>
										</c:forEach>			                    
									</table>
			                    </td>
			                    <td>${sku.sellPrices[0].costPrice}</td>
			                    <td>${sku.skuInventory}</td>
			                </tr>
			            </c:forEach>
			            </tbody>
			        </table>
	             </div>
         </div>
    <div class="container-fluid" id="baseService" style="display:none">
    <legend ><span class="content-body-bg">基础服务商品</span></legend>
      <c:forEach items="${auxMap}" var="baseItem">
	      <c:if test="${baseItem.value.addSource==4}">
	                             商品名称：${baseItem.value.itemName}  &nbsp;&nbsp;数量：${baseItem.value.subNum}
		             <c:forEach items="${baseItem.value.skuInfos}" var="skuInfo" >
			            <c:forEach items="${baseMap}" var="baseMap">
				            <c:if test="${baseMap.key== skuInfo.skuId}">
					                <div class="row-fluid">
								         <div class="span6 input-append">
								              <c:forEach items="${baseMap.value}" var="attr" varStatus="vs">
										                     <label class="label-control" title="${attr.name}">&nbsp;${attr.name}:</label>
										                     <label>
										                     	<c:forEach items="${attr.values}" var="attrVal" varStatus="vsAttrVal">
										                     		<c:if test="${vsAttrVal.count > 1}">,</c:if>${attrVal.name}
										                     	</c:forEach>
										                     </label>
									             </c:forEach>
							              </div>
								  </div>
				            </c:if>
			            </c:forEach>
		            </c:forEach>
	      </c:if>
    </c:forEach>
</div>     
 <div class="container-fluid" id="addService" style="display:none">
    <legend ><span class="content-body-bg">增值服务商品</span></legend>
      <c:forEach items="${auxMap}" var="baseItem">
	      <c:if test="${baseItem.value.addSource==5}">
	                             商品名称：${baseItem.value.itemName}  &nbsp;&nbsp;数量：${baseItem.value.subNum}
		             <c:forEach items="${baseItem.value.skuInfos}" var="skuInfo" >
			            <c:forEach items="${baseMap}" var="baseMap">
				            <c:if test="${baseMap.key== skuInfo.skuId}">
					                <div class="row-fluid">
								         <div class="span6 input-append">
								              <c:forEach items="${baseMap.value}" var="attr" varStatus="vs">
										                     <label class="label-control" title="${attr.name}">&nbsp;${attr.name}:</label>
										                     <label>
										                     	<c:forEach items="${attr.values}" var="attrVal" varStatus="vsAttrVal">
										                     		<c:if test="${vsAttrVal.count > 1}">,</c:if>${attrVal.name}
										                     	</c:forEach>
										                     </label>
									             </c:forEach>
							              </div>
								  </div>
				            </c:if>
			            </c:forEach>
		            </c:forEach>
	      </c:if>
    </c:forEach>
</div>     
 <div class="container-fluid" id="auxService" style="display:none">
    <legend ><span class="content-body-bg">辅助材料商品</span></legend>
      <c:forEach items="${auxMap}" var="baseItem">
	      <c:if test="${baseItem.value.addSource==6}">
	                             商品名称：${baseItem.value.itemName}  &nbsp;&nbsp;数量：${baseItem.value.subNum}
		             <c:forEach items="${baseItem.value.skuInfos}" var="skuInfo" >
			            <c:forEach items="${baseMap}" var="baseMap">
				            <c:if test="${baseMap.key== skuInfo.skuId}">
					                <div class="row-fluid">
								         <div class="span6 input-append">
								              <c:forEach items="${baseMap.value}" var="attr" varStatus="vs">
										                     <label class="label-control" title="${attr.name}">&nbsp;${attr.name}:</label>
										                     <label>
										                     	<c:forEach items="${attr.values}" var="attrVal" varStatus="vsAttrVal">
										                     		<c:if test="${vsAttrVal.count > 1}">,</c:if>${attrVal.name}
										                     	</c:forEach>
										                     </label>
									             </c:forEach>
							              </div>
								  </div>
				            </c:if>
			            </c:forEach>
		            </c:forEach>
	      </c:if>
    </c:forEach>
</div>              
         <div class="container-fluid">
            <legend ><span class="content-body-bg">商品图片</span></legend>
            <div class="row-fluid">
            	<c:forEach items="${itemDTO.picUrls}" var="picUrl">
            		<img src="${filePath}${picUrl}" width="80" height="50">
            	</c:forEach>
            </div>
         </div>
        
        <div class="container-fluid">
            <legend ><span class="content-body-bg">SKU图片</span></legend>
            <div class="row-fluid">
            	<table class="table table-striped table-bordered table-condensed td-cen hhtd" style="width:71%;">
		            <tbody>
			            <c:forEach items="${itemDTO.skuInfos}" var="sku">
			            	<tr>
			            		<c:forEach items="${saleAttrs}" var="saleAttr" varStatus="vsSaleAttr">
				                	<td width="15%">${skuFn:getAttrNameByIdArr(saleAttrs, sku.attributes, vsSaleAttr.index)}</td>
				                </c:forEach>
			                    <c:forEach items="${sku.skuPics}" var="skuPic">
				                	<td><img src="${filePath}${skuPic.picUrl}" width="80" height="50">
				                	</td>
				                </c:forEach>
			                </tr>
			            </c:forEach>
		            </tbody>
		        </table>	
            </div>
        </div>
        
        <div class="container-fluid">
            <legend ><span class="content-body-bg">商品详情</span></legend>
            <div class="row-fluid">
            	<textarea id="editor" name="content" style="width:700px;height:300px;"></textarea>
            	<textarea id="editor1" style="display:none">${itemDTO.describeUrl}</textarea>
            </div>
        </div>
         <div class="container-fluid">
            <legend ><span class="content-body-bg">商品规格</span></legend>
            <div class="row-fluid">
            	<textarea id="specifications" name="specification" style="width:700px;height:300px;"></textarea>
            	<textarea id="specifications1" style="display:none">${itemDTO.specification}</textarea>
            </div>
        </div>
         <div class="container-fluid">
            <legend ><span class="content-body-bg">服务支持</span></legend>
            <div class="row-fluid">
            	<textarea id="afterService" name="afterService" style="width:700px;height:300px;"></textarea>
            	<textarea id=afterService1 style="display:none">${itemDTO.afterService}</textarea>
            </div>
        </div>
        
        <div class="container-fluid" id="showId" style="display:none">
            <legend ><span class="content-body-bg">认证信息</span></legend>
            <div class="row-fluid">
            	<textarea id="editor_authId" name="contentAuth" style="width:700px;height:300px;"></textarea>
            	<textarea id="editor_authId1" style="display:none;">${itemDTO.authentication}</textarea>
            </div>
        </div>
        
         <div class="container-fluid">
            <legend ><span class="content-body-bg">其他信息</span></legend>
            <div class="row-fluid">
                <div class="span6">
                    <label class="label-control" title="包装清单">
                                                                                    包装清单：
                    </label>
                    <label>${itemDTO.packingList}</label>
                </div>
            </div>
             <div class="row-fluid" id="searchId" style="disply:none">
                <div class="span6">
                    <label class="label-control" title="可否被搜索">
                                                                                  可否被搜索：
                    </label>
                    <label>
                    <input type="radio" name="searched" value="1" <c:if test="${itemDTO.searched==1}"> checked </c:if> disabled="disabled">是
		            <input type="radio" name="searched" value="2" <c:if test="${itemDTO.searched==2}"> checked </c:if> disabled="disabled">否
		            </label>
                </div>
            </div>
            <div class="row-fluid" id="roomtypeId" style="disply:none">
                <div class="span6">
                    <label class="label-control" title="户型">
                                                                              户型：
                    </label>
                    <c:forEach items="${housetypes}" var="roomtype">
                     <input type="checkbox" name="housetype" value="${roomtype.id}"  disabled="disabled"
                     <c:if test="${itemDTO.housetype.contains(roomtype.id.toString())}">checked</c:if> />
                    ${roomtype.name}
                    </c:forEach>
                    <label>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>