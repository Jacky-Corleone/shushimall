<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品发布</title>
	<meta name="decorator" content="default"/>

    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>

    <script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="${ctxStatic}/ueditor/ueditor.all.js" type="text/javascript"></script>
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

         // 判断数值类型，包括整数和浮点数
            jQuery.validator.addMethod("isNumber", function(value, element) {       
                 return this.optional(element) || /^\d+$/.test(value) || /^\d+(\.\d{0,2})?$/.test(value);       
            }, "匹配数值类型，包括整数和浮点数"); 
        });

        function saveItem(){
            var picUrls = [];

            $("#picAddDiv").find("img").each(function(i,item){
                picUrls.push($(item).attr("src").replace('${filePath}',''));
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
                    picUrls:"required",

                    weight: {
                    	isNumber: true
                    },
                    volume: {
                    	isNumber: true
                    },
                    describeUrl:"required",
                    packingList:{
                        maxlength:200
                    },
                    afterService:{
                        maxlength:300
                    },
                    keywords:{
                        required: true,
                        maxlength:200
                    }
                }
            });
            var isValide = $("#goodsForm").valid();
            if(isValide){
                $('#goodsForm').submit();
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
                    allItemAttrString = "";
                    allItem = $("#itemAttrDivAll>div");
                    var len = $(allItem).length;
                    if(len>2){
                        for(var j=0;j<len;j=j+2){
                            var id = $($("#itemAttrDivAll>div").get(j)).find("input[name='itemAttrId']").val();
                            $($($($("#itemAttrDivAll>div").get(j+1)).children()).find(":checked")).each(function(i,n){
                                allItemAttr += id+":"+$(n).val()+";";
                            });
                            //var allItemSalesAttrString;
                            //var allItemAttrString;
                            $($($($("#itemAttrDivAll>div").get(j+1)).children()).find("input:checkbox")).each(function(i,n){
                                allItemAttrString += id+":"+$(n).val()+"#";
                            });
                        }
                    }
                    break;
                //组装销售属性
                case 2:
                    allItemSalesAttrString = "";
                    allItem = $("#salesAttrDivAll>div");
                    var len = $(allItem).length;
                    if(len>2){
                        for(var j=0;j<len;j=j+2){
                            var id = $($("#salesAttrDivAll>div").get(j)).find("input[name='itemAttrId']").val();
                            $($($($("#salesAttrDivAll>div").get(j+1)).children()).find(":checked")).each(function(i,n){
                                allItemAttr += id+":"+$(n).val()+";";
                            });
                            $($($($("#salesAttrDivAll>div").get(j+1)).children()).find("input:checkbox")).each(function(i,n){
                                allItemSalesAttrString += id+":"+$(n).val()+"#";
                            });
                        }
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
            var html;
            var url;

            switch (flag){
                case '1':
                    var pid = $("#platformId1").val();
                    html = "<option value=''>二级类目</option>";
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $("#platformId2").html("<option value=''>二级类目</option>");
                	$("#platformId2").select2("val","");
                	$("#cid").html("<option value=''>三级类目</option>");
                	$("#cid").select2("val","");
                    $("#brand").empty();
                    $("#brand").select2("val","");
                    break;
                case '2':
                    var pid = $("#platformId2").val();
                    html = "<option value=''>三级类目</option>";
                    url = "${ctx}/brand/getChildCategory?pCid="+pid;
                    $("#cid").html("<option value=''>三级类目</option>");
                	$("#cid").select2("val","");
                    $("#brand").empty();
                    $("#brand").select2("val","");
                    break;
                case '3':
                    var cid = $("#cid").val();
                    var secondCid = $("#platformId2").val();
                    url = "${ctx}/brand/getCategoryBrand?secondCid="+secondCid+"&thirdCid="+cid;
                    $("#brand").empty();
                    $("#brand").select2("val","");
//                    $("#brandLineDiv").show();
                    //获取已经存在的类目属性
                    $.ajax({
                        url:"${ctx}/item/queryCategoryAttr2",
                        data:"cid="+cid+"&attrType=2",
                        dataType:"json",
                        success:function(data){
                            var html = "";
                            $(data).each(function(i,t){
                                html += "<div class='row-fluid' style='margin-top: 10px'><div class='span6'><input type='hidden' name='itemAttrId' value='"+ t.id +"'><input type='text' name='itemAttrName' value='"+ t.name+"' class='input-medium' title='不能为空'/></div></div>";
                                html += "<div class='row-fluid'>"
                                if(t.values){
                                    $(t.values).each(function(j,n){
                                        html += "<div class='span1'><input type='checkbox' name='itemAttrValueId' value='"+ n.id+"'><input type='text' name='itemAttrValue' value='"+n.name+"' class='input-mini' title='不能为空'></div>";
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
                            html += "<div class='row-fluid'><div class='span12'></div></div>";
                            $.each($("#itemAttrDivAll>div:not(.stay)"),function(i,item){
                                $(item).remove();
                            });

                            $("#itemAttrDiv").before(html);
                        }
                    });
                    //获取已经存在的销售属性
                    $.ajax({
                        url:"${ctx}/item/queryCategoryAttr2",
                        data:"cid="+cid+"&attrType=1",
                        dataType:"json",
                        success:function(data){
                            var html = "";
                            $(data).each(function(i,t){
                                html += "";
                                html += "<div class='row-fluid' style='margin-top: 10px'><div class='span6'><input type='hidden' name='itemAttrId' value='"+ t.id +"'><input type='text'  name='itemAttrName' value='"+ t.name+"' class='input-medium' title='不能为空'/></div></div>";
                                html += "<div class='row-fluid'>";
                                if(t.values){
                                    $(t.values).each(function(j,n){
                                        html += "<div class='span1'><input type='checkbox' name='itemAttrValueId' value='"+ n.id+"'><input type='text' name='itemAttrValue' value='"+n.name+"' class='input-mini' title='不能为空'></div>";
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

                            $("#salesAttrDiv").before(html);

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
                        if('3'!=flag){
                            html += "<option value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                        }else{
                            html += "<option value='"+item.brandId+"'>"+item.brandName+"</option>";
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
            var html = ""+
            "<div class='row-fluid' style='margin-top: 10px'><div class='span4'><input type='hidden' name='itemAttrId'><input name='itemAttrName' type='text' class='input-medium' title='不能为空'/></div></div>"+
            "<div class='row-fluid'>" +
                "<div class='span3'>" +
                    "<a class='btn' href='javascript:void(0)' onclick='addItemAttrValue(this)'>" +
                        "<i class='icon-plus'></i>添加属性值" +
                    "</a>" +
                    "<button class='btn btn-primary' onclick='saveItemAttr(2,this)' type='button'>保存</button>" +
                    "<button class='btn' type='button' onclick='cancelAdd(2,this)'>取消</button>" +
                "</div>" +
            "</div>";

            $("#itemAttrDiv").prev().before(html);
        }
        function addItemAttrValue(node){
            var html =  ""+
                "<div class='span1'>" +
                    "<input type='checkbox' name='itemAttrValueId' value='' checked/><input name='itemAttrValue' value='' type='text' maxlength='28' class='input-mini' title='不能为空'/>" +
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
//            var data ="cid="+cid+"&attrType="+flag+"&itemName="+itemAttrName+"&itemValues="+itemAttrs+"&itemAttrId="+itemAttrId;
            var data = {
                "attrType":flag,
                "itemName":itemAttrName,
                "itemValues": itemAttrs,
                "itemAttrId":itemAttrId
            };
            $.ajax({
                url:"${ctx}/item/addItemAttr2",
                type:"post",
                traditional:true,
                data:data,
                dataType:'json',
                success:function(data){
                    if(data.success){
                        $.jBox.info(data.msg);
                        if(null == itemAttrId || ""==itemAttrId){
                            $(node).parent().parent().prev().find("input[name='itemAttrId']").val(data.itemAttrId);
                        }

                        $($(node).parent().siblings()).each(function(i,n){
                            itemAttrValueId = $(n).find("input:checkbox").val();
                            itemAttrValue = $(n).find("input:text").val();
                            //没有ID的才是新添加的
                            if(itemAttrValueId==null||itemAttrValueId==''){
                                $(data.itemValues).each(function(i,m){
                                    if(itemAttrValue!=""&&itemAttrValue==m.itemAttrValueName){
                                        $(n).find("input:checkbox").val(m.itemAttrValueId);
                                    }
                                });
                            }
                        });
                    }else{
                        $.jBox.info(data.msg);
                    }

                }
            });
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

        function addSalesAttr(){
            var html = ""+
                    "<div class='row-fluid' style='margin-top: 10px'><div class='span4'><input type='hidden' name='itemAttrId'　value=''><input name='itemAttrName' type='text' maxlength='6' title='不能为空' class='input-medium'/></div></div>"+
                    "<div class='row-fluid'>" +
                    "<div class='span3'>" +
                    "<a class='btn' href='javascript:void(0)' onclick='addItemAttrValue(this)'>" +
                    "<i class='icon-plus'></i>添加属性值" +
                    "</a>" +
                    "<button type='button' class='btn btn-primary' onclick='saveItemAttr(1,this)'>保存</button>" +
                    "<button class='btn' type='button' onclick='cancelAdd(2,this)'>取消</button>" +
                    "</div>" +
                    "</div>";
            $("#salesAttrDiv").prev().before(html);
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
            $.ajaxFileUpload({
                        url: '${ctx}/fileUpload/uploadImg?date='+new Date(), //用于文件上传的服务器端请求地址
                        secureuri: false, //是否需要安全协议，一般设置为false
                        fileElementId: 'uploadPic', //文件上传域的ID
                        dataType: 'json', //返回值类型 一般设置为json
                        type:"post",
                        success: function (data, status){  //服务器成功响应处理函数
                            if(data.success){
                                var html = "<a href='${filePath}"+data.url+"' target='_blank'><img src='${filePath}"+data.url+"' class='img-polaroid' style='width:50px;height:50px'></a>";
                                $("#picAddDiv").prepend(html);
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
        }
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <form id="goodsForm" name="goodsForm" method="post" action="save">
            <input type="hidden" name="platLinkStatus" id="platLinkStatus">
            <input type="hidden" name="allItemSalesAttrString" id="allItemSalesAttrString">
            <input type="hidden" name="allItemAttrString" id="allItemAttrString">

            <div class="container-fluid">
                <legend ><span class="content-body-bg">基本信息</span></legend>
                <div class="row-fluid">
                    <div class="span1">
                        平台分类
                    </div>
                    <div class="span9">
                        <select name="platformId1" id="platformId1"  class="form-control input-medium" onchange="loadsub('1')">
                            <option value="">一级类目</option>
                            <c:forEach items="${platformList}" var="item">
                                <option value="${item.categoryCid}">${item.categoryCName}</option>
                            </c:forEach>
                        </select>
                        <select name="platformId2" id="platformId2"  class="form-control input-medium" onchange="loadsub('2')">
                            <option value="">二级类目</option>
                        </select>
                        <select name="cid" id="cid"   class="form-control input-medium" onchange="loadsub('3')" title="请选择类目">
                            <option value="">三级类目</option>
                        </select><span style="color: #FF0000">*</span>
                    </div>
                </div>

                <div class="row-fluid" style="margin-top: 10px">
                    <div class="span1">
                        商品名称
                    </div>
                    <div class="span7">
                        <input name="itemName" id="itemName"  type="text" class="form-control" title="请输入商品名称"/><span style="color: #FF0000">*</span>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        市场指导价
                    </div>
                    <div class="span3 input-append">
                        <input name="marketPrice" id="marketPrice"  type="text" class="form-control" title="不能为空且只能是数字"/>
                        <span class="add-on">元</span>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        商品毛重
                    </div>
                    <div class="span7">
                        <input name="weight" id="weight"  type="text" class="form-control" title="只能是数字或两位小数"/>
                        <select name="weightUnit" class="input-mini" style="margin-bottom: 10px">
                            <option value="kg">kg</option>
                            <option value="g">g</option>
                            <option value="mg">mg</option>
                            <option value="μg">μg</option>
                        </select>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        商品体积
                    </div>
                    <div class="span3 input-append">
                        <input name="volume" id="volume"  type="text" class="form-control" title="只能是数字或两位小数"/>
                        <span class="add-on">m³</span>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        商品产地
                    </div>
                    <div class="span7">
                        <input name="origin" id="origin"  type="text" class="form-control"/>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        商品关键字
                    </div>
                    <div class="span7">
                        <input type="text" name="keywords" id="keywords"   title="不可为空，最长为200字符"/>
                        <span style="color: #FF0000">*</span>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span1">
                        品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌
                    </div>
                    <div class="span7">
                        <select name="brand" id="brand"  title="请选择品牌"></select>
                        <span style="color: #FF0000">*</span>
                    </div>
                </div>
            </div>

            <div class="container-fluid" id="itemAttrDivAll">
                <legend ><span class="content-body-bg">类目属性</span></legend>
                <input type="hidden" name="attributesStr" id="attributesStr" title="请至少选择一项类目属性">

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
                <input type="hidden"  "attrSaleStr" id="attrSaleStr" title="请至少选择一项销售属性">

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
                <span style="color: red;display: block;" id="imgSize1" >（允许的图片格式有JPG、JPEG、PNG、BMP，单张图片不能大于1M）</span>
                <div class="row-fluid" id="picAddDiv">
                    <input type="hidden" name="picUrls" id="picUrls" title="请上传至少一张图片">
                    <div><a class="btn" href="javascript:addPic('1')"><i class="icon-plus"></i>添加图片</a></div>
                </div>
                <div class="row-fluid">
                    <div class="span12"></div>
                </div>
            </div>
            <div class="container-fluid">
                <legend ><span class="content-body-bg">商品详情<span style="color: #FF0000">*</span>(提示：建议上传的图片大小限制为3M,尺寸宽度为980，否则系统会自动压缩和宽度调整可能会使图片失真。)</span></legend>
                <div class="row-fluid">
                    <div class="span9">
                        <textarea id="describeUrl" name="describeUrl" style="width:700px;height:300px;" title="请填写商品描述"></textarea>
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
                        <input name="packingList" id="packingList"  type="text" class="form-control" title="最大只能输入200个字符"/>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="afterService" title="售后服务">售后服务</label>
                        <input name="afterService" id="afterService"  type="text" class="form-control" title="最大只能输入300个字符"/>
                    </div>
                </div>

                <div class="row-fluid">
                    <div class="span12"></div>
                </div>
                <div class="row-fluid">
                    <div class="span3">
                        <button class="btn btn-success btn-block" type="button" id="btnSave">保存</button>
                    </div>
                    <div class="span3">
                        <button class="btn btn-danger btn-block" type="button" id="btnConfirm">提交</button>
                    </div>
                    <div class="span3">
                        <button class="btn btn-inverse btn-block" type="button" id="btnCancel">取消</button>
                    </div>
                </div>

            </div>

        </form>
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
                <input type="file" id="uploadPic" name="file" />
            </p>
        </div>
        <div class="modal-footer">
            <a href="javascript:closeUploadDiv()" class="btn">关闭</a>
            <a href="javascript:startUpload()" class="btn btn-primary">确定</a>
        </div>
</div>

</body>
</html>