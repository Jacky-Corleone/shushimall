<%--
  Created by IntelliJ IDEA.
  User: menpg
  Date: 2015/3/2
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>订单明细</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <style>
        table{}
        .y-tb1{
            width: 100%;
            border:1px solid #fda99e;
            background: #ffeee6;
            height: 50px;
        }.y-tb1 td{
            border-top: 0px solid #eee;
            border-bottom: 0px solid #eee;
            border-left:0px solid #eee;
            border-right: 0px solid:#eee;
        }
        .mb20 {
            margin-bottom: 20px;
        }
        .z-tbl {
            width: 100%;
            border-spacing: 0;
        }
        .z-tbl td {
            padding: 5px 5px;
            line-height: 16px;
            text-align: left;
        }

        div.y-box {
            width: 95%;
            border: 1px solid #ddd;
        }.z-box{
            border: 1px solid #dadada;
            background: #ededed;
            padding: 4px;
            margin-bottom: 10px;
        }.z-box2{
            background: #fff;
            padding: 15px;
        }.item {
             padding: 10px;
             border-bottom: 1px solid #eee;
         }
        h3{
            color:#000000;
            height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }.hx{
            line-height: 6px;
            border-left: 3px solid #e94645;
            font-size: 14px;
            text-indent: 5px;
            margin-bottom: 10px;
            margin-top: 10px;
        }
        h5{
            color:#000000;
            height: 30px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
            padding: 5px 5px;
        }
        .icon-del{
			background: url(${ctxStatic}/images/close_hover.png) no-repeat center center;
			background-size:16px 16px;
			cursor: pointer;
		}
		.down {
			position: absolute;
			width: 15px;
			height: 17px;
			margin-top:26px;
			background:url(${ctxStatic}/images/result-huge-bg.png) no-repeat -0px -127px;
			cursor: pointer;
		}
		.down:hover{
			background:url(${ctxStatic}/images/result-huge-bg.png) no-repeat -0px -103px;
		}
    </style>
    <style type="text/css">
	/*图片上传按钮样式*/
	.z-upload{overflow:hidden; width:100px;height:30px;line-height:30px;color:#fff;position:relative;}
	.z-upload .file-img{font-size:40px;width:100px;height:30px;filter:alpha(opacity=0);opacity:0;position:absolute;left:0px;top:0px;}
	.c-upload{position:absolute;left:0;bottom:0px;overflow:hidden;height:20px;line-height:20px;color:#fff;width:100%;background:rgba(0, 0, 0, .5);z-index:999;}
	.c-upload .file-img{width:80px;height:50px;filter:alpha(opacity=0);opacity:0;z-index:999;text-align:center;}
	.c-upload span{width:80px;height:50px; padding-left:15px;}
	</style>
    <script type="text/javascript">

        $(document).ready(function() {
            //图片查看
            $('.showimg').fancyzoom({
                overlayColor: '#000',
                overlay: 0.6,
                imagezindex:100,
                showoverlay:false,
                Speed:400,
                shadow:true,
                shadowOpts:{ color: "#000", offset: 4, opacity: 0.2 },
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/',
                imgResizeScript:null,
                autoresize:true
            });
        });
    </script>
    <script type="text/javascript">
        function opengooddetail(itemid,skuid){
            var url="${mallPath}/productController/details?id="+itemid+"&skuId="+skuid;
            window.open(url);
        }
        function startUploadEnclosure(fileElementId, showImg, urlInput){
        	var fileInput = $("#"+fileElementId).val();
        	var nameStart = fileInput.lastIndexOf("\\");
    		var fileName = stripscript(fileInput.substring(nameStart+1,fileInput.length));
    		if($("#"+fileName).length<=0){
    			$.ajax({
					url: "${ctx}/salesorder/verifyCount",
					type: "post",
					data: {
						orderId : "${map.id}",
						isImg :isImg
					},
					dataType: 'json',
					success: function(data){
						if(data.resultMessage == 'success'){
				        	$.ajaxFileUpload({
				                url: '${ctx}/fileUpload/uploadsize?size=2048000&date='+new Date(), //用于文件上传的服务器端请求地址
				    			secureuri: false, //是否需要安全协议，一般设置为false
				    			fileElementId: fileElementId, //文件上传域的ID
				    			dataType: 'json',
				    			type:"post",
				    			success: function (data, status){
				    				if(!data.success){
				                        $.jBox.info(data.msg);
				    				}else{
				    					var name = stripscript(data.name);
				    					var html = "<span id='"+name+"'><a href='${filePath}"+data.url+"' target='_blank'>"+data.name+"</a> </span>";
				                        $("#"+showImg).append(html);
				                        $("#"+urlInput).val(data.url);
				                    }
				    				addEnclosure(data.url,"${map.id}",data.name,0,null,"${map.buyerId}",2);
				    			},
				    			error: function (data, status, e){
				                    $.jBox.error("系统繁忙，请稍后再试");
				    			}
				    		});
						}else if(data.resultMessage == 'error'){
	    					$.jBox.info("上传数量有限，请勿再次上传。");
	    				}else{
	    					
	    				}
					}
				});
				        	
			}else{
				$.jBox.error("该文件已经上传，请勿重复上传。");
    		}
        }
        //工具方法：上传图片
    	function startUpload(fileElementId, showImg, urlInput){
    		var fileInput = $("#"+fileElementId).val();
    		var nameStart = fileInput.lastIndexOf("\\");
    		var extStart = fileInput.lastIndexOf(".");
    		var ext = fileInput.substring(extStart,fileInput.length).toUpperCase();
    		var fileName = stripscript(fileInput.substring(nameStart+1,fileInput.length));
    		if($("#"+fileName).length<=0){
    		
	    		if(ext!=".JPG" && ext!=".jpg" && ext!=".JPEG" && ext!=".jpeg" && ext!=".PNG" && ext!=".png" && ext!=".BMP" && ext!=".bmp"){
	    			alert("图片限于JPG,JPEG,PNG,BMP格式");
	    			return false;
	    		}
	    		$.ajax({
					url: "${ctx}/salesorder/verifyCount",
					type: "post",
					data: {
						orderId : orderId,
						isImg :isImg
					},
					dataType: 'json',
					success: function(data){
						if(data.resultMessage == 'success'){
							$.ajaxFileUpload({
				                url: '${ctx}/fileUpload/uploadsize?size=1048576&date='+new Date(), //用于文件上传的服务器端请求地址
				    			secureuri: false, //是否需要安全协议，一般设置为false
				    			fileElementId: fileElementId, //文件上传域的ID
				    			dataType: 'json',
				    			type:"post",
				    			success: function (data, status){
				    				if(!data.success){
				                        $.jBox.info(data.msg);
				    				}else{
				    					var name = stripscript(data.name);
				    					var html = "<span id='"+name+"'><img class='showimg'  title='点击查看大图' src='${filePath}"+data.url+"' style='height: 40px;width: 65px'> </span>";
				                        $("#"+showImg).append(html);
				                        $("#"+urlInput).val(data.url);
				                    }
				    				$('.showimg').fancyzoom({
				    		            Speed: 400,
				    		            showoverlay: false,
				    		            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
				    		        });
				  	               addEnclosure(data.url,"${map.id}",data.name,0,null,"${map.buyerId}",1);
				    			},
				    			error: function (data, status, e){
				                    $.jBox.error("系统繁忙，请稍后再试");
				    			}
				    		});
						}else if(data.resultMessage == 'error'){
	    					$.jBox.info("上传数量有限，请勿再次上传。");
	    				}else{
	    					
	    				}
					}
				});
			}else{
				$.jBox.error("该文件已经上传，请勿重复上传。");
    		}
    	};
    	
    	function addEnclosure(url,orderId,enclosureName,id,remark,buyerId,isImg){
    		$.ajax({
    			url: "${ctx}/salesorder/addEnclosure",
    			type: "post",
    			data: {
    				url : url,
    				orderId : orderId,
    				enclosureName : enclosureName,
    				remark : remark,
    				type : "1",
    				buyerId : buyerId,
    				isImg : isImg,
    				id : id
    			},
    			dataType: 'json',
    			success: function(data){
    				if(data.resultMessage == 'success'){
    					if(url != null){
    						var del= "<i class='icon-del' title='删除' onclick='delEnclosure(this,"+data.result+")'>&nbsp;&nbsp;&nbsp;&nbsp;</i>&nbsp;&nbsp;&nbsp;&nbsp";
    						var name = stripscript(enclosureName);
    						$("#"+name).append(del);
    					}
    					if(remark!=null && remark!=""){
    						$("#enclosureId").val(data.result);
    					}
    					$.jBox.info('保存成功！');
    				}else{
    					$.jBox.info('保存失败！');
    				}
    			}
    		});
    	}
    	
    	function delEnclosure(obj,id){
    		$.ajax({
    			url: "${ctx}/salesorder/delEnclosure",
    			type: "post",
    			data: {
    				id : id
    			},
    			dataType: 'json',
    			success: function(data){
    				if(data.resultMessage == 'success'){
    					$(obj).parent('span').remove();
    					$.jBox.info('删除成功！');
    				}else{
    					$.jBox.info('删除失败！');
    				}
    			}
    		});
    	}
    	
    	function stripscript(s) {
    	    var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、 ？　]")
    	        var rs = "";
    	    for (var i = 0; i < s.length; i++) {
    	        rs = rs + s.substr(i, 1).replace(pattern, '');
    	    }
    	    return rs;
    	}
    	
    	function addremark(){
    		var id = $("#enclosureId").val();
    		var remark = $("#remark").val();
    		if(remark==null || remark==""){
    			alert("输入备注信息");
    			return;
    		}
    		if(id==null || id==""){
    			id=0;
    		}
    		addEnclosure(null,"${map.id}",null,id,remark,"${map.buyerId}",null);
    	}
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
        <div class="y-box">
        <h3 class="h3">
            订单详情
        </h3>
        <table id="contentTable1" class="y-tb1">
            <colgroup>
                <col width="10%">
                <col width="23.33%">
                <col width="10%">
                <col width="23.33%">
                <col width="10%">
                <col width="23.33%">
            </colgroup>
            <tbody>
                <tr>
                    <td style="text-align: right">订单号：</td>
                    <td style="text-align: left">${map.id}</td>
                    <td style="text-align: right">订单状态：</td>
                    <td style="text-align: left">${map.orderStace}</td>
                    <td style="text-align: right">下单时间：</td>
                    <td style="text-align: left">
                        ${map.ordertime}
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="z-box">
            <div class="z-box2">
            <table class="z-tbl">
                <colgroup>
                    <col width="50%">
                    <col width="50%">
                </colgroup>
                 <tr>
                     <td>订单编号：${map.id}</td>
                     <td>订单状态：${map.orderStace}</td>
                 </tr>
                <tr>
                    <td>商家账号：${map.sellerName}</td>
                    <td>客户账号：${map.buyerName}</td>
                </tr>
                <tr>
                    <td>下单时间：${map.ordertime}</td>
                    <td>订单完成时间：${map.updateTime}</td>
                </tr>
            </table>
            </div>
        </div>
           <%-- <div class="z-box">
                <h3 class="h3">订单跟踪</h3>
                <div class="z-box2">
                    <table class="z-tbl">
                        <colgroup>
                            <col width="50%">
                            <col width="50%">
                        </colgroup>
                        <tr>
                            <td>物流公司：${map.wlname}</td>
                            <td>运单号：${map.wlcode}</td>
                        </tr>
                    </table>
                </div>
            </div>--%>


            <div class="z-box">
                <h3 class="h3">订单商品</h3>
                <div class="z-box2">
                    <table class="z-tbl">
                        <colgroup>
                            <col width="20%">
                            <col width="19%">
                            <col width="15%">
                            <col width="15%">
                            <col width="15%">
                            <col width="16%">
                        </colgroup>
                        <c:forEach items="${map.itemList}" var="map">
                        <tr>
                            <td >商品名称：${map.itemName}</td>
                            <td >商品编号：${map.itemId}</td>
                            <td >商品单价：${map.price}</td>
                            <td >商品数量：${map.num}</td>
                            <td >商品总价：${map.priceTotal}</td>
                            <td >
                                <a href="${mallPath}/productController/details?id=${map.itemId}&skuId=${map.skuId}" target="_Blank" class="z-link01" style="margin:2px 0;display: block;">订单商品:
                                <img src="${filePath}${map.skuUrl}" style="width: 40px;height: 30px">
                                </a>
                            </td>
                        </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>


            <div class="z-box">
                <h3 class="h3">订单信息</h3>
                <div class="z-box2">
                    <div class="item">
                        <h5 class="h5" style="margin-bottom: 10px;">收货人信息</h5>
                        <table class="z-tbl">
                            <colgroup>
                                <col width="100%">
                            </colgroup>
                            <tr>
                                <td>收货人：${map.name}</td>
                            </tr>
                            <tr>
                                <td>地址：${map.addressp}</td>
                            </tr>
                            <tr>
                                <td>手机号码：${map.mobile}</td>
                            </tr>
                        </table>
                    </div>

                    <div class="item">
                    	<h5 class="h5" style="margin-bottom: 10px;">房型图信息</h5>
                        <table class="z-tbl">
                            <colgroup>
                                <col width="100%">
                            </colgroup>
							<tr>
								<td>房型图：
									<c:forEach items="${map.ishouse }" var="enclosureDTO">
										<c:if test="${enclosureDTO.isImg == 1 }">
											<c:if test="${enclosureDTO.type == 0 && enclosureDTO.enclosureName !='' }">
												<img class="showimg" title="点击查看大图" src="${filePath}${enclosureDTO.enclosureUrl }" style="height: 40px;width: 65px">
												<a class="down" title="下载原图"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</c:if>
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td>附件：
									<c:forEach items="${map.ishouse }" var="enclosureDTO">
										<c:if test="${enclosureDTO.isImg == 2 }">
											<c:if test="${enclosureDTO.type == 0 && enclosureDTO.enclosureName !=''}">
	<%-- 											<img class="showimg" title="点击查看大图" id="gpProductCertificationImg"src="${filePath}${enclosureDTO.enclosureUrl }" style="height: 80px;width: 100px"> --%>
												<a href='${filePath}${enclosureDTO.enclosureUrl }' target="_blank">${enclosureDTO.enclosureName }</a>&nbsp;&nbsp;&nbsp;&nbsp;
											</c:if>
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td>备注：
									<c:forEach items="${map.ishouse }" var="enclosureDTO">
										<c:if test="${enclosureDTO.type == 0 && enclosureDTO.remark !=''}">
											${enclosureDTO.remark }
										</c:if>
									</c:forEach>
								</td>
							</tr>
                        </table>
                    </div>

                    <div class="item">
                        <h5 class="h5" style="margin-bottom: 10px;">设计图信息</h5>
                        <table class="z-tbl">
	                        <colgroup>
	                        	<col width="100%">
	                        </colgroup>
	                        <tr>
								<td>设计图:
					                <span id="imgspan" style="color: red">仅支持JPG、JPEG、PNG、BMP格式，大小不得超过1M</span>
									<span class="z-upload">
										<button type="button" class="button_4 font_12 border-1 button_small">浏览图片</button>
										<input type="file" id="enclosureUrlFile" name="file" class="file-img" onchange="startUpload('enclosureUrlFile','enclosureUrlImg','enclosureUrl')">
									</span>
					                <input id="enclosureUrl" name="enclosureUrl" type="hidden" value="" required="true" title="请上传图片"/>
								</td>
							</tr>
							<tr>
								<td id="enclosureUrlImg">
									<c:forEach items="${map.ishouse }" var="enclosureDTO">
										<c:if test="${fn:endsWith(enclosureDTO.enclosureName, '.jpg') ||  fn:endsWith(enclosureDTO.enclosureName, '.png')}">
											<c:if test="${enclosureDTO.type == 1 && enclosureDTO.enclosureName !='' }">
											<span>
												<img class="showimg" title="点击查看大图" src="${filePath}${enclosureDTO.enclosureUrl }" style="height: 40px;width: 65px">
												<i class="icon-del" title="删除" onclick="delEnclosure(this,'${enclosureDTO.id}')">&nbsp;&nbsp;&nbsp;&nbsp;</i>&nbsp;&nbsp;&nbsp;&nbsp;
											</span>
											</c:if>
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td>附件:
									<span style="color: red">大小不得超过1M</span>
									<span class="z-upload">
										<button type="button" class="button_4 font_12 border-1 button_small">浏览文件</button>
										<input type="file" id="Oradefile" name="file" class="file-img" onchange="startUploadEnclosure('Oradefile','OradefileSrc','OradefileUrl')">
									</span>
					                <input id="enclosureUrl" name="OradefileUrl" type="hidden" value="" required="true" title="请上传附件"/>
								</td>
							</tr>
							<tr>
								<td id="OradefileSrc">
									<c:forEach items="${map.ishouse }" var="enclosureDTO">
										<c:if test="${!fn:endsWith(enclosureDTO.enclosureName, '.jpg') && !fn:endsWith(enclosureDTO.enclosureName, '.png')}">
											<c:if test="${enclosureDTO.type == 1 && enclosureDTO.enclosureName !=''}">
											<span>
												<a href='${filePath}${enclosureDTO.enclosureUrl }' target="_blank">${enclosureDTO.enclosureName }</a>&nbsp;&nbsp;&nbsp;&nbsp;
												<i class="icon-del" title="删除" onclick="delEnclosure(this,'${enclosureDTO.id}')">&nbsp;&nbsp;&nbsp;&nbsp;</i>&nbsp;&nbsp;&nbsp;&nbsp;
											</span>
											</c:if>
										</c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td>备注：
									<textarea id="remark" name="remark" rows="4" maxlength="300">${map.enclosureRemark }</textarea>
									<input type="hidden" id="enclosureId" value="${map.enclosureId }">
								</td>
							</tr>
                        </table>
                    </div>
					<div class="control-group offset9 text-right">
						<div class="controls">
							<input id="btnSubmit" class="btn" type="button" onclick="addremark()" value="保 存" />&nbsp; 
							<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)" />
						</div>
					</div>
				</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
