<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品发布</title>
	<meta name="decorator" content="default"/>

    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>

	<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
	<script src="${ctxStatic}/ueditor/lang/zh_cn/zh_cn.js" type="text/javascript"></script>

    <script type="application/javascript">
        var allItemSalesAttrString;
        var allItemAttrString;
    	function onBrandImgClick(brandImg){
    		$("#picAddDiv input[type='button']").remove();
    		var imgUrl = brandImg.src;
    		var html = "<input src=${filePath}"+imgUrl+" type='button' class='btn' onclick='delImg(this)' value='删除'/> <input type='button' class='btn' onclick='cancelImg()' value='取消'/>";
    		$(brandImg).after(html);
    	}  
    	function cancelImg(){
    		$("#picAddDiv input[type='button']").remove();
    	}
    	function delImg(imgUrl){
    		$("#picAddDiv").find("img").each(function(i,item){
                if($(item).attr("src").replace("${filePath}",'')==imgUrl.src){
                	$(item).remove();
                }
            });
    		$("#picAddDiv input[type='button']").remove();
    	}
        $(document).ready(function(){
        	var picurls='${picUrls}'.split(",");
        	if(${itemDTO.cid}>0){
        		loadsub('3');
        		var html = "";
        		for(var i =0;i<picurls.length;i++){
        			html += "<img src='${filePath}"+picurls[i]+"' onclick='onBrandImgClick(this)' class='img-polaroid' style='width:50px;height:50px'>";
        		}
                $("#picAddDiv").prepend(html);
        	}
            var ue = UE.getEditor('describeUrl',{
				serverUrl:'${ctx}/ueditor/exec',
				imageUrlPrefix:"${filePath}"
			});
			
			ue.addListener('ready',function(){
		
				var imgObjs = $("#ueditor_0").contents().find("img");
				imgObjs.each(function () {
		           if($(this).attr("src") != "" && $(this).attr("src").indexOf("http://") < 0){
		        	   $(this).attr("src","${filePath}"+$(this).attr("src"));
		           }
		           if($(this).attr("_src") != "" && $(this).attr("_src").indexOf("http://") < 0){
		        	   $(this).attr("_src","${filePath}"+$(this).attr("_src"));
		           }
		   	 	});
		   	 	
	   	 	});
	   	 	



            $("#btnSave").click(function(){
                top.$.jBox.confirm("确认要保存吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        $("#platLinkStatus").val(2);
                        saveItem();
                    }
                },{buttonsFocus:1});
            });
            $("#btnConfirm").click(function(){
                top.$.jBox.confirm("确认要提交吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        $("#platLinkStatus").val(3);
                        saveItem();
                    }
                },{buttonsFocus:1});
            });
            $("#btnCancel").click(function(){
                top.$.jBox.confirm("确认要取消吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                    	history.go(-1);
                    }
                },{buttonsFocus:1});
            });
            $("#btnEdit").click(function(){
                top.$.jBox.confirm("确认要保存吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        $("#platLinkStatus").val(2);
                        saveItem();
                    }
                },{buttonsFocus:1});
            });

        });

        function saveItem(){
            var picUrls = [];
            $("#picAddDiv").find("img").each(function(i,item){
                picUrls.push($(item).attr("src").replace("${filePath}",''));
            });
            $("#picAddDiv2").find("img").each(function(i,item){
                picUrls.push($(item).attr("src").replace("${filePath}",''));
            });
            $("#picUrls").val(picUrls);
            $("#describeUrl").val(UE.getEditor('describeUrl').getContent());
            // 查找出类目属性封装好
            $("#attributesStr").val(getAttributes1(1));
            // 查找出销售属性封装好
            $("#attrSaleStr").val(getAttributes1(2));

            $("#allItemSalesAttrString").val(allItemSalesAttrString);
            $("#allItemAttrString").val(allItemAttrString);

            $("#goodsForm").validate({
                rules: {
                    itemAttrName: "required",
                    cid: "required",
                    itemAttrName: "required",
                    itemName: "required",
                    brand:"required",
                    attributesStr:"required",
                    attrSaleStr:"required",
                    picUrls:"required",
                    guidePrice: {
                        required: true,
                        number: true
                    },
                    weight: {
                        number: true
                    },
                    describeUrl:"required",
                    packingList:{
                        maxlength:16
                    },
                    afterService:{
                        maxlength:16
                    },
                    keywords:{
                        required: true,
                        maxlength:200
                    }
                }
            });
            var isValide = $("#goodsForm").valid();
            if(isValide){
//                    dat = JSON.parse(dat);

//                    dat = JSON.stringify(dat);
                var dat = $('#goodsForm').serialize();
                	$.ajax({
                        url:"${ctx}/goodscenter/editS",
                        data:dat,
                        dataType:"json",
//                            contentType : "application/json",
                        type:"post",
                        success:function(data){
                            if(data.success){
                                $.jBox.info(data.msg);
                                location.href ="${ctx}/goodList/gsList/";
                            }else{
                                $.jBox.info(data.msg);
                            }


                        }
                    });
            }else{
                $("#itemName").focus();
            }
        }

        //封装成字符串
        function getAttributes1(flag){
            var allItemAttr = "";
            var allItem;
            switch (flag){
                //组装类目属性
                case 1:
                    allItem = $("#itemAttrDivAll>div");
                    var len = $(allItem).length;
                    if(len>2){
                    	var i = $('#itemAttrDivAll input[type="checkbox"]:checked');
                		var j=0;
                		$('#itemAttrDivAll input[type="checkbox"]:checked').each(function () {
                			j++;
                			allItemAttr+=$(this).attr('value');
                			allItemAttr +=";";
                        });
                        /* var itemAttr = "";
                        for(var j=2;j<len;j=j+2){
                            var id = $($("#itemAttrDivAll>div").get(j)).find("input[name='itemAttrId']").val();
                            $($($("#itemAttrDivAll>div").get(j+1)).children()).each(function(i,n){
                                if($(n).find("input[name='itemAttrValueId']").attr('checked')){
                                    var aid = $(n).find("input[name='itemAttrValueId']").val();
                                    itemAttr = id+":"+aid+";";
                                }

                            });
                            allItemAttr += itemAttr;
                        } */
                    }
                    break;
                //组装销售属性
                case 2:
                    allItem = $("#salesAttrDivAll>div");
                    var len = $(allItem).length;
                    if(len>2){
//                    	var i = $('#salesAttrDivAll input[type="checkbox"]:checked');
                		var j=0;
                		$('#salesAttrDivAll input[type="checkbox"]:checked').each(function () {
                			j++;
                			allItemAttr+=$(this).attr('value');
                			allItemAttr +=";";
                        });
                    }
                    break;
            }

            return allItemAttr;
        }


        /**
        *加载下拉框
        * @param flag
         */
        function loadsub(flag){
            var html = "<option value=''></option>";
            var url;

            switch (flag){
                case '1':
                    var pid = $("#platformId1").val();
                    if(pid==null||pid<=0){
                    	pid= ${oneLevelId};
                    }
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $("#platformId2").empty();
                    $("#cid").empty();
                    $("#brand").empty();
                    break;
                case '2':
                    var pid = $("#platformId2").val();
                    if(pid==null||pid<=0){
                    	pid = ${twoLevelId};
                    }
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $("#cid").empty();
                   	$("#brand").empty();
                    break;
                case '3':
                	var cid =$("#cid").val();
                	if(cid==null||pid<=0){
                		cid = ${itemDTO.cid};
                	}
                    /* var cid = $("#cid").val(); */
                    var secondCid = $("#platformId2").val();
                    url = "${ctx}/brand/getCategoryBrand?secondCid="+secondCid+"&thirdCid="+cid;
                    $("#brand").empty();
//                    $("#brandLineDiv").show();
                    //获取已经存在的类目属性
                    $.ajax({
                        url:"${ctx}/item/queryCategoryAttr3",
                        data:"attrType=2&itemId=${itemDTO.itemId}",
                        dataType:"json",
                        success:function(data){
                            var html = "";
                            $(data).each(function(i,t){
                                html += "<div class='row-fluid'><div class='span12'></div></div>";
                                html += "<div class='row-fluid'><span class='span6'><input type='hidden' name='itemAttrId' value='"+ t.id +"'><input type='text' name='itemAttrName' value='"+ t.name+"' class='input-medium' title='不能为空'/></div></div>";
                                html += "<div class='row-fluid'>"
                                if(t.values){
                                    $(t.values).each(function(j,n){
                                        html += "<div class='span3' ";
                                        if(j==0){
                                        	html += " style='margin-left:23px;' ";
                                        }
                                        html += ">";
                                        html += "<input "; 
                                        if("${itemDTO.attributesStr}".indexOf(t.id+":"+n.id+";")>=0){
                                        	 html += "checked='checked'";
                    					}
                                        
                                        html += "type='checkbox' name='itemAttrValueId' value='"+ t.id+":"+n.id+"'><input type='text' name='itemAttrValue' value='"+n.name+"' class='input-medium' title='不能为空'></div>";
                                    });
                                }
                                html +="<div class='span3'>" ;
                                html +="<a class='btn' href='javascript:void(0)' onclick='addItemAttrValue(this)'>" ;
                                html +="<i class='icon-plus'></i>添加属性值" ;
                                html +="</a>" ;
                                html +="<button class='btn btn-primary' onclick='saveItemAttr(2,this)' type='button'>保存</button>" ;
                                html +="<button class='btn' type='button' onclick='cancelAdd(2,this)'>取消</button>" ;
                                html +="</div>" ;
                                html += "</div>";
                            });
                            $.each($("#itemAttrDivAll>div:not(.stay)"),function(i,item){
                                $(item).remove();
                            });

                            $("#brandLineDiv").after(html);
                        }
                    });
                    //获取已经存在的销售属性
                    $.ajax({
                        url:"${ctx}/item/queryCategoryAttr3",
                        data:"itemId=${itemDTO.itemId}&attrType=1",
                        dataType:"json",
                        success:function(data){
                            var html = "";
                            $(data).each(function(i,t){
                                html += "<div class='row-fluid'><div class='span12'></div></div>";
                                html += "<div class='row-fluid'><span class='span6'><input type='hidden' name='itemAttrId' value='"+ t.id +"'><input type='text' name='itemAttrName' value='"+ t.name+"'class='input-medium' title='不能为空'/></div></div>";
                                html += "<div class='row-fluid'>";
                                if(t.values){
                                    $(t.values).each(function(j,n){
                                        html += "<div class='span3' ";
                                        if(j==0){
                                        	html += " style='margin-left:23px;' ";
                                        }
                                        html += ">";
                                        html +=" <input ";
                                       	if("${itemDTO.attrSaleStr}".indexOf(t.id+":"+n.id+";")>=0){
                                          	 html += "checked='checked' ";
                       					}
                                       	html += " type='checkbox' name='itemAttrValueId' value='"+ t.id+":"+n.id+"'><input type='text' name='itemAttrValue' value='"+n.name+"' class='input-medium' title='不能为空'></div>";
                                    });
                                }
                                html +="<div class='span3'>" ;
                                html +="<a class='btn' href='javascript:void(0)' onclick='addItemAttrValue(this)'>" ;
                                html +="<i class='icon-plus'></i>添加属性值" ;
                                html +="</a>" ;
                                html +="<button class='btn btn-primary' onclick='saveItemAttr(1,this)' type='button'>保存</button>" ;
                                html +="<button class='btn' type='button' onclick='cancelAdd(1,this)'>取消</button>" ;
                                html +="</div>" ;
                                html += "</div>";
                            });
                            html += "<div class='row-fluid'><div class='span12'></div></div>";
                            $.each($("#salesAttrDivAll>div:not(.stay)"),function(i,item){
                                $(item).remove();
                            });

                            $("#salesAttr").after(html);

                        }
                    });
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
                        	if(item.categoryCid==${twoLevelId}){
                            	html +=" selected";
                            }
                        	html +=" value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                        }else if('2'==flag){
                            html += "<option ";
                            if(item.categoryCid==${itemDTO.cid}){
                            	html +=" selected ";
                            }
                            html +="value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                        }else{
                            html += "<option "; 
                            if(${itemDTO.cid}>0){
                            	if(item.brandId==${itemDTO.brand}){
                                	html +=" selected ";
                                } 
                            }
                            html += " value='"+item.brandId+"'>"+item.brandName+"</option>";
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
                        case '3':
                            $("#brand").html(html);
                            break;
                    }
                }
            });

        }

        function addItemAttr(){
            var html = "<div class='row-fluid'><div class='span12'></div></div>"+
            "<div class='row-fluid'><div class='span6'><input type='hidden' name='itemAttrId'><input name='itemAttrName' type='text' title='不能为空'/></div></div>"+
            "<div class='row-fluid'>" +
                "<div class='span3'>" +
                    "<a class='btn' href='javascript:void(0)' onclick='addItemAttrValue(this)'>" +
                        "<i class='icon-plus'></i>添加属性值" +
                    "</a>" +
                    "<button class='btn btn-primary' onclick='saveItemAttr(2,this)'>保存</button>" +
                    "<button class='btn' >取消</button>" +
                "</div>" +
            "</div>";

            $("#itemAttrDiv").prev().before(html);
        }
        function addItemAttrValue(node){
            var html =  ""+
                "<div class='span3'>" +
                    "<input type='checkbox' name='itemAttrValueId' value=''/><input name='itemAttrValue' value='' type='text' class='input-medium' title='不能为空'/>" +
                "</div>";
            $(node).parent().before(html);
        }
        function saveItemAttr(flag,node){
            var itemAttrId = $(node).parent().parent().prev().find("input[name='itemAttrId']").val();
            var itemAttrName = $(node).parent().parent().prev().find("input[name='itemAttrName']").val();
            var itemAttrs = [];

            var itemAttrValueId;
            var itemAttrValue;
            $($(node).parent().siblings()).each(function(i,n){
                itemAttrValueId = $(n).find("input:checkbox").val();
                itemAttrValue = $(n).find("input:text").val();
                itemAttrs.push(itemAttrValueId+"#"+itemAttrValue);

            });
            var cid = $("#cid").val();
            if(null==cid){
                $.jBox.info("请先选择完三级类目");
                return;
            }
            if(null==itemName||""==itemName){
                $.jBox.info("请先填写属性名称");
                return;
            }
            var data ="cid="+cid+"&attrType="+flag+"&itemName="+itemAttrName+"&itemValues="+itemAttrs+"&itemAttrId="+itemAttrId;
            $.ajax({
                url:"${ctx}/item/addItemAttr2",
                data:data,
                dataType:'json',
                success:function(data){
                    $.jBox.info(data.msg);
                    if(null == itemAttrId || ""==itemAttrId){
                        $(node).parent().parent().prev().find("input[name='itemAttrId']").val(data.itemAttrId);
                    }

                    $($(node).parent().siblings()).each(function(i,n){
                        itemAttrValueId = data[$(n).find("input:text").val()];
                        //没有ID的才是新添加的
                        if(itemAttrValueId!=null||itemAttrValueId!=''){
                            $(n).find("input:checkbox").val(itemAttrValueId);
                        }

                    });
                }
            });
        }
        function addSalesAttr(){
            var html = "<div class='row-fluid'><div class='span12'></div></div>"+
                    "<div class='row-fluid'><div class='span6'><input type='hidden' name='itemAttrId'　value=''><input name='itemAttrName' type='text' title='不能为空'/></div></div>"+
                    "<div class='row-fluid'>" +
                    "<div class='span3'>" +
                    "<a class='btn' href='javascript:void(0)' onclick='addItemAttrValue(this)'>" +
                    "<i class='icon-plus'></i>添加属性值" +
                    "</a>" +
                    "<button class='btn btn-primary' onclick='saveItemAttr(1,this)'>保存</button>" +
                    "<button class='btn' >取消</button>" +
                    "</div>" +
                    "</div>";
            $("#salesAttrDiv").prev().before(html);
        }
        /**
         *取消
         */
        function cancelAdd(flag,node){
            var itemAttrId = $(node).parent().parent().prev().find("input[name='itemAttrId']").val();
            if(itemAttrId==null||itemAttrId==''){
                $(node).parent().parent().prev().remove();
                $(node).parent().parent().remove();
            }else{
                $($(node).parent().siblings()).each(function(i,n){
                    var itemAttrValueId = $(n).find("input:checkbox").val();
                    //没有ID的才是新添加的
                    if(itemAttrValueId==null||itemAttrValueId==''){
                        $(n).remove();
                    }

                });
            }
        }
        function addPic(flag){
            switch (flag){
                case '1':
                    $("#tempFlag").val("1");
                    break;
                case '2':
                    $("#tempFlag").val("2");
                    break;
            }
            $("#uploadPicDiv").modal('show');
        }
        function closeUploadDiv(){
            $("#uploadPicDiv").modal('hide');
        }
        function startUpload(){
        	/* $('#picAddDiv img').remove(); */
        	if($('#uploadPic').val()){
	        		$.ajaxFileUpload({
	                    url: '${ctx}/fileUpload/uploadImg?date='+new Date(), //用于文件上传的服务器端请求地址
	                    secureuri: false, //是否需要安全协议，一般设置为false
	                    fileElementId: 'uploadPic', //文件上传域的ID
	                    dataType: 'json', //返回值类型 一般设置为json
	                    type:"post",
	                    success: function (data, status){  //服务器成功响应处理函数
                            if(data.success){
                                //图片上传
                                var html = "<img src='${filePath}"+data.url+"' onclick='onBrandImgClick(this)' class='img-polaroid' style='width:50px;height:50px'>";
                                $("#picAddDiv").append(html);
                                $("#uploadPicDiv").modal('hide');
                            }else{
                                $.jBox.error(data.msg);
                            }

	                    },
	                    error: function (data, status, e){//服务器响应失败处理函数
	                        $.jBox.error(e);
	                    }
	                }
	        	);
        	}else{
        		$.jBox.info("请选择图片!");
        	}
            
            
        }
        /* function getNodes(id){
        	var ids ="";
        	if(id==1){
        		var i = $('#itemAttrDivAll input[type="checkbox"]:checked');
        		int j=0;
        		$('#itemAttrDivAll input[type="checkbox"]:checked').each(function () {
        			j++;
        			ids+=$(this).attr('value');
        			if((j)!=i){
        				ids +";";
        			}
                });
        	}else{
        		var i = $('#salesAttrDivAll input[type="checkbox"]:checked');
        		int j=0;
        		$('#salesAttrDivAll input[type="checkbox"]:checked').each(function () {
        			j++;
        			ids+=$(this).attr('value');
        			if((j)!=i){
        				ids +";";
        			}
                });
        	}
        	return ids;
        } */
        function  PreviewImg(obj) { 
        	if( !obj.value.match( /.jpg|.jpeg|.png|.bmp/i ) ){ 
        		$.jBox.info("图片格式错误！");
        		$('#uploadPic').val("");
        		return false; 
        	}else{
        		return true;
        	} 
        }
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
    	<form:form id="goodsForm" name="goodsForm" modelAttribute="itemDTO">
        <!-- <form id="goodsForm" name="goodsForm" modelAttribute="itemDTO"> -->
            <!-- <input type="hidden" name="picUrls" id="picUrls"> -->
            <form:hidden path="picUrls"/>
            <form:hidden path="attributesStr"/>
            <form:hidden path="attrSaleStr"/>
            <form:hidden path="platLinkStatus"/>
            <form:hidden path="itemId"/>
            <input type="hidden" name="allItemSalesAttrString" id="allItemSalesAttrString">
            <input type="hidden" name="allItemAttrString" id="allItemAttrString">
            <!-- <input type="hidden" name="attributesStr" id="attributesStr">
            <input type="hidden" name="attrSaleStr" id="attrSaleStr">
            <input type="hidden" name="platLinkStatus" id="platLinkStatus"> -->
            <div class="container-fluid">
                <legend ><span class="content-body-bg">基本信息</span></legend>
                <div class="row-fluid">
                    <div class="span9">
                        <label class="label-control" for="cid" title="平台分类">
                            *平台分类
                        </label>
                        <select name="platformId1" id="platformId1"  class="form-control input-medium" onchange="loadsub('1')">
                            <option value=""></option>
                            <c:forEach items="${platformList}" var="item">
                                <option <c:if test="${item.categoryCid==oneLevelId}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                        <select name="platformId2" id="platformId2"  class="form-control input-medium" onchange="loadsub('2')">
                            <option value=""></option>
                            <c:forEach items="${towList}" var="item">
                                <option <c:if test="${item.categoryCid==twoLevelId}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                        <select name="cid" id="cid"  class="form-control input-medium" onchange="loadsub('3')">
                            <option value=""></option>
                            <c:forEach items="${thirdList}" var="item">
                                <option <c:if test="${item.categoryCid==itemDTO.cid}">selected="true"</c:if> value="${item.categoryCid}">${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row-fluid" style="margin-top: 10px">
                    <div class="span1">
                        商品名称
                    </div>
                    <div class="span5">
                        <form:input path="itemName" htmlEscape="false" maxlength="16" class="required"/>
                        <span style="color: #FF0000">*</span>
                        <!-- <input name="itemName" id="itemName"  type="text" class="form-control" title="请输入商品名称"/> -->
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                       市场指导价
                    </div>
                    <div class="span5 input-append">
                        <form:input path="marketPrice" htmlEscape="false" maxlength="16" class="required" title="不能为空且只能是数字"/>
                        <!-- <input name="guidePrice" id="guidePrice"  type="text" class="form-control" title="只能是数字"/> -->
                        <span class="add-on">元</span>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        商品毛重
                    </div>
                    <div class="span5">
                        <form:input path="weight" htmlEscape="false" maxlength="16" class="required" title="只能是数字"/>
                        <!-- <input name="weight" id="weight"  type="text" class="form-control" title="只能是数字"/> -->
                        <select name="weightUnit" class="input-mini" style="margin-bottom: 10px">
                            <option value="kg">kg</option>
                            <option value="g">g</option>
                            <option value="mg">mg</option>
                            <option value="ug">ug</option>
                        </select>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        商品产地
                    </div>
                    <div class="span5">
                        <form:input path="origin" htmlEscape="false" maxlength="16" class="required"/>
                        <span style="color: #FF0000">*</span>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        商品关键字
                    </div>
                    <div class="span5">
                        <form:input path="keywords"   htmlEscape="false" class="required" maxlength="200" title="不可为空，最长为200字符"/>
                        <span style="color: #FF0000">*</span>
                    </div>
                </div>
            </div>

            <div class="container-fluid" id="itemAttrDivAll">
                <legend ><span class="content-body-bg">类目属性</span></legend>
                <div class="row-fluid stay" id="brandLineDiv" >
                    <div class="span6 stay">
                        <label class="label-control" title="品牌">品牌
                            <!-- <select name="brand" id="brand"></select> -->
                            <select name="brand" id="brand" class="form-control input-medium">
	                            <c:forEach items="${brandList}" var="item">
	                            	<option <c:if test="${item.brandId==itemDTO.brand}">selected="selected"</c:if>  value="${item.brandId}">${item.brandName}</option>
	                            </c:forEach>
	                        </select>
                            <span style="color: #FF0000">*</span>
                        </label>
                    </div>
                </div>
                <div class="row-fluid stay">
                    <div class="span12 stay"></div>
                </div>
                <div class="row-fluid stay" id="itemAttrDiv">
                    <div class="span3 stay">
                        <a class="btn" href="javascript:addItemAttr(this)"><i class="icon-plus"></i>添加类目属性</a>
                    </div>
                </div>
                <div class="row-fluid stay">
                    <div class="span12 stay"></div>
                </div>
            </div>
            <div class="container-fluid" id="salesAttrDivAll">
                <legend ><span class="content-body-bg">销售属性</span></legend>
                <div class="row-fluid stay" id="salesAttr">
                    <div class="span12 stay"></div>
                </div>
                <div class="row-fluid stay" id="salesAttrDiv">
                    <div class="span3 stay">
                        <a class="btn" href="javascript:addSalesAttr(this)"><i class="icon-plus"></i>添加销售属性</a>
                    </div>
                </div>
                <div class="row-fluid stay">
                    <div class="span12 stay"></div>
                </div>
            </div>
            <div class="container-fluid">
                <legend ><span class="content-body-bg">商品图片</span></legend>
                （单张图片不能大于500K；允许的图片格式有jpg、jpeg、png、JPG、JPEG、PNG）
                <div class="row-fluid" id="picAddDiv">
                </div>
                <div class="span3">
                    <a class="btn" href="javascript:addPic('1')"><i class="icon-plus"></i>添加图片</a>
                </div>
                <div class="row-fluid">
                    <div class="span12"></div>
                </div>
            </div>
            <div class="container-fluid">
                <legend ><span class="content-body-bg">*商品详情(提示：建议上传的图片大小限制为3M,尺寸宽度为980，否则系统会自动压缩和宽度调整可能会使图片失真。)</span></legend>
                <div class="row-fluid">
                    <div class="span9">
                        <!-- <textarea id="describeUrl" name="describeUrl" style="width:700px;height:300px;"></textarea> -->
                        <form:textarea path="describeUrl"  htmlEscape="false" class="required" style="width:700px;height:300px;"/>
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
                        <form:input path="packingList" htmlEscape="false" maxlength="200" class="required"/>
                        <!-- <input name="packingList" id="packingList"  type="text" class="form-control" /> -->
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="afterService" title="售后服务">售后服务</label>
                        <form:input path="afterService" htmlEscape="false" maxlength="300" class="required"/>
                        <!-- <input name="afterService" id="afterService"  type="text" class="form-control" /> -->
                    </div>
                </div>

                <div class="row-fluid">
                    <div class="span12"></div>
                </div>
                <div class="row-fluid">
                    <c:if test="${itemDTO.itemId>0}">
                		 <div class="span3">
	                        <button class="btn btn-success btn-block" type="button" id="btnEdit">保存</button>
	                    </div>
	                    <div class="span3">
	                        <button class="btn btn-danger btn-block" type="button" id="btnConfirm">提交</button>
	                    </div>
                	</c:if>
                   
                    <div class="span3">
                        <button class="btn btn-inverse btn-block"  type="button" id="btnCancel
                        ">取消</button>
                    </div>
                </div>

            </div>

        </form:form>
    </div>
</div>
<!--图片上传弹出框-->
<div class="modal hide fade" id="uploadPicDiv">
    <input type="hidden" name="tempFlag" id="tempFlag">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>添加上传图片</h3>
        </div>
        <div class="modal-body">
            <p>
                <input type="file"  onchange="PreviewImg(this)" id="uploadPic" name="file" />
            </p>
        </div>
        <div class="modal-footer">
            <a href="javascript:closeUploadDiv()" class="btn">关闭</a>
            <a href="javascript:startUpload()" class="btn btn-primary">确定</a>
        </div>
</div>

</body>
</html>