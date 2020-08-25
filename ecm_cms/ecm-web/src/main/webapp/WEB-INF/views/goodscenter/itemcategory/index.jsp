<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>类目属性管理</title>
	<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<style type="text/css">
		.btn{margin: 5px 10px 0 20px;}
		.add{margin: 5px 10px 0 20px; width: 100px;}
		.icon-del{
			background: url(${ctxStatic}/images/close_hover.png) no-repeat center center;
			background-size:16px 16px;
			cursor: pointer;
		}
		.senior{}
		#oneCategory{ padding: 0 20px;}
		#attrType{text-align: center;}
		.proInput{border:1px solid #ccc; margin:5px 0 5px;}
        input.updateattr{
            border:3px solid #008000;
        }
        input.updateattrvalue{
            border:3px solid #FFA500;
        }
	</style>
	<script type="text/javascript">
	
		$(document).ready(function() {
			$(".btn").bind("click", function(){
				$(this).removeClass("btn-default").addClass("btn-primary");
				$(this).siblings().removeClass("btn-primary").addClass("btn-default");
			});
			oneCategory();
			//类目属性查询
			$(".js_queryCategoryAttr").bind("click", queryCategoryAttr);
			

			
			//添加属性
			$("#js_attrName").click(function(){
				var attrNameObj = $("#attrName").val();
				if(attrNameObj==null || typeof(attrNameObj)=="undefined"){
					var attrNameHTML = '<div class="proInput">'
						+'<input type="text" id="attrName" name="attrName" maxlength="20" class="add" placeholder="属性名">'
						//+'是否高级选项：'
						//+'是<input type="radio" name="isSenior" value="1"/>'
						//+'否<input type="radio" name="isSenior" value="0" checked="checked"/>'
						+'<span><input type="button" class="btn btn-info js_addAttrName" onclick="addAttrName(this);" value="确定"><input type="button" class="btn btn-default js_cancelAttrName" value="取消"></span>'
						+'</div>';
					$("#attrNames").append(attrNameHTML);
					
					$(".js_cancelAttrName").bind("click", function(){
						$(this).parents("div.proInput").remove();
					});
				}
			});
			
			
			//添加类目
			$(".js_addItemCategory").click(function(){
				var whichone = $(this).attr("name");
				var newCategory = $("#"+whichone+"Category").children("input.newCategory").val();
				if(newCategory==null || newCategory.trim()==""){
					$.jBox.info("请先填写类目");
					return;
				}
				//当前选中的元素值
				var categoryCid = $("."+whichone+"btn").filter("button.btn-primary").val();
				
				var categoryParentCid, categoryLev;
				if(whichone=="one"){
					categoryParentCid = 0;
					categoryLev = 1;
				}else if(whichone=="two"){
					categoryParentCid = $(".onebtn").filter("button.btn-primary").val();
					categoryLev = 2;
				}else if(whichone=="three"){
					categoryParentCid = $(".twobtn").filter("button.btn-primary").val();
					categoryLev = 3;
				}
				if(newCategory!=null && categoryParentCid!=null && categoryLev!=null){
                    $("#popUpDiv").modal("show");
					$.ajax({
		    			url: "${ctx}/item/addItemCategory",
		    			dataType:"json",
		    			data:{
		    				categoryParentCid: categoryParentCid,
		    				categoryCName: newCategory,
		    				categoryLev: categoryLev
		    			},
		    			cache:false,
		    			success: function(data){
		    				if(data.success){
		    					if(whichone=="one"){
		    						oneCategory(categoryCid);
		    					}else if(whichone=="two"){
		    						twoCategory(categoryParentCid, categoryCid);
		    					}else if(whichone=="three"){
		    						threeCategory(categoryParentCid, categoryCid);
		    					}
		    				}
                            $("#popUpDiv").modal("hide");
		    			}
		    		});
					
				}
			});
			
			
		});
        //一级类目
        //参数：defaultValue 默认选中的类目id
        function oneCategory(defaultValue){
            $("#oneCategory").empty();
            $.ajax({
                url: "${ctx}/item/datagrid",
                dataType:"json",
                data:{
                    parentCid: 0
                },
                cache:false,
                success: function(data){
                    var total = data.total;
                    var rows = data.rows;
                    for(var i=0; i<total; i++){
                        if(defaultValue!=null && rows[i].categoryCid == defaultValue){
                            $("#oneCategory").append('<button type="button" id='+rows[i].categoryCid+' value="'+rows[i].categoryCid+'" class="btn btn-primary onebtn">'+rows[i].categoryCName+'</button>');
                        }else{
                            $("#oneCategory").append('<button type="button" id='+rows[i].categoryCid+' value="'+rows[i].categoryCid+'" class="btn btn-default onebtn">'+rows[i].categoryCName+'</button>');
                        }
                    }
                    $("#oneCategory").append('<input type="text" maxlength="20" style="height:31px;margin-top:16px" placeholder="输入类目名称" class="add newCategory">');
                    $(".onebtn").bind("click", function(){
                        $(this).removeClass("btn-default").addClass("btn-primary");
                        $(this).siblings().removeClass("btn-primary").addClass("btn-default");
                        var categoryCid = $(this).val();
                        twoCategory(categoryCid,null);
                    });
                }
            });
        }
        //二级类目
        //参数：categoryCid 一级栏目的id, defaultValue 默认选中的二级类目id
        function twoCategory(categoryCid, defaultValue){
            $("#two").show();
            $("#twoCategory").empty();
            if(defaultValue==null){
                $("#three").hide();
                $("#attrNames").empty();
                $("#addpro").hide();
            }
            $.ajax({
                url: "${ctx}/item/datagrid",
                dataType:"json",
                data:{
                    parentCid: categoryCid
                },
                cache:false,
                success: function(data){
                    var total = data.total;
                    var rows = data.rows;
                    for(var i=0; i<total; i++){
                        if(defaultValue!=null && rows[i].categoryCid == defaultValue){
                            $("#twoCategory").append('<button type="button" id='+rows[i].categoryCid+' value="'+rows[i].categoryCid+'" class="btn btn-primary twobtn">'+rows[i].categoryCName+'</button>');
                        }else{
                            $("#twoCategory").append('<button type="button" id='+rows[i].categoryCid+' value="'+rows[i].categoryCid+'" class="btn btn-default twobtn">'+rows[i].categoryCName+'</button>');
                        }
                    }
                    $("#twoCategory").append('<input type="text" maxlength="20" style="height:31px;margin-top:16px" placeholder="输入类目名称" class="add newCategory">');
                    $(".twobtn").bind("click", function(){
                        $(this).removeClass("btn-default").addClass("btn-primary");
                        $(this).siblings().removeClass("btn-primary").addClass("btn-default");
                        var categoryCid = $(this).val();
                        threeCategory(categoryCid);
                    });
                }
            });
        }
        //三级类目
        //参数：categoryCid 二级栏目的id, defaultValue 默认选中的三级类目id
        function threeCategory(categoryCid, defaultValue){

            $("#three").show();
            $("#threeCategory").empty();
            if(defaultValue==null){
                $("#attrNames").empty();
                $("#addpro").hide();
            }
            $.ajax({
                url: "${ctx}/item/datagrid",
                dataType:"json",
                data:{
                    parentCid: categoryCid
                },
                cache:false,
                success: function(data){
                    var total = data.total;
                    var rows = data.rows;
                    for(var i=0; i<total; i++){
                        if(defaultValue!=null && rows[i].categoryCid == defaultValue){
                            $("#threeCategory").append('<button type="button" id='+rows[i].categoryCid+' value="'+rows[i].categoryCid+'" class="btn btn-primary threebtn">'+rows[i].categoryCName+'</button>');
                        }else{
                            $("#threeCategory").append('<button type="button" id='+rows[i].categoryCid+' value="'+rows[i].categoryCid+'" class="btn btn-default threebtn">'+rows[i].categoryCName+'</button>');
                        }
                    }
                    $("#threeCategory").append('<input type="text" maxlength="20" style="height:31px;margin-top:16px" placeholder="输入类目名称" class="add newCategory">');
                    $(".threebtn").bind("click", function(){
                        $(this).removeClass("btn-default").addClass("btn-primary");
                        $(this).siblings().removeClass("btn-primary").addClass("btn-default");
                        //添加属性div显示
                        $("#addpro").show();
                        queryCategoryAttr();
                    });
                }
            });
        }
		function queryCategoryAttr(){
			$("#attrNames").empty();
			var cidObj = $(".threebtn").filter("button.btn-primary");
			var cid = cidObj.val();
			if(cid==null){
				//$.jBox.info("请选择类目");
				return;
			}
			var attrTypeObj = $(".btnattrType").filter("button.btn-primary");
			var attrType = attrTypeObj.val();
			if(attrType==null){
				$.jBox.info("请选择销售属性信息");
				return;
			}
			$.ajax({
    			url: "${ctx}/item/queryCategoryAttr",
    			dataType:"json",
    			data:{
    				cid: cid,
    				attrType: attrType
    			},
    			cache:false,
    			success: function(data){
    				var total = data.total;
    				var rows = data.rows;
    				for(var i=0; i<total; i++){
    					var attrNameHTML = '<div class="proInput">';
    					attrNameHTML += '<input type="text" maxlength="20" name="'+rows[i].attrAttrId+'"  class="add itemattr"  value="'+rows[i].attrAttrName+'">';
    					attrNameHTML += '<i class="icon-del" title="删除" onclick="deleteCategoryAttr(\''+rows[i].attrAttrId+'\')"></i>';
    					var is = rows[i].isSenior==1?"checked='checked'":"";
    					var no = rows[i].isSenior==0?"checked='checked'":"";
    					//XXX:thinking 2016-03-01 delete 高级属性
						//attrNameHTML += '是否高级选项111：是<input type="radio" class="senior" name="isSenior'+rows[i].attrAttrId+'" value="1" '+is+'/>否<input type="radio" class="senior" name="isSenior'+rows[i].attrAttrId+'" value="0" '+no+'/>'    					
						attrNameHTML += '<span class="js_valueName" style="cursor:pointer;">&nbsp;&nbsp;&nbsp;+添加属性值</span>';
    					var valueList = rows[i].valueList;
    					if(valueList!=null){
    						attrNameHTML += '<div>';
	    					attrNameHTML += '<span class="valueNames">';
    						for(var j=0; j<valueList.length; j++){
	    						attrNameHTML += '<input  type="text" maxlength="28" name="'+valueList[j].attrValueId+'" class="add itemattrvalue"  value="'+valueList[j].attrValueName+'">';
	    						attrNameHTML += '<i class="icon-del" title="删除" onclick="deleteCategoryAttrValue(\''+rows[i].attrAttrId+'\',\''+valueList[j].attrValueId+'\')"></i>';
	    					}
    						attrNameHTML += '</span>';
	    					//attrNameHTML += '<input type="button" class="btn btn-info js_addValueName" value="确定">';
	    					//attrNameHTML += '<input type="button" class="btn btn-default js_cancelValueName" value="取消">';
	    					attrNameHTML += '</div>';
    					}
    					attrNameHTML += '</div>';
    					$("#attrNames").append(attrNameHTML);
    				}
    				$(".itemattr").bind("change",function(){
                        changeclass(this,"add itemattr updateattr"); 
                    })
                    $(".senior").bind("change",function(){
                     	changeclass(this.parentNode.firstChild,"add itemattr updateattr"); 
                    })
                    $(".itemattrvalue").bind("change",function(){
                        changeclass(this,"add itemattrvalue updateattrvalue");
                    })
    				$(".js_valueName").bind("click", function(){
    					//当前点击元素的同辈元素div 下的 class="valueNames" 
    					var flag = false;
    					$(this).parent().find(".js_addValueName").each(function(i){
    						$(this).remove();
    					});
    					$(this).parent().find(".js_addValueName_agin").each(function(i){
    						$(this).remove();
    					});
    					$(this).parent().find(".js_cancelValueName").each(function(i){
    						$(this).remove();
    					});
    					$(this).parent().find(".js_cancelValueName_agin").each(function(i){
    						$(this).remove();
    					});
    					
   						$(this).siblings("div").find(".valueNames").append('<input type="text" name="valueName" class="add newValueName" maxlength="28" placeholder="属性值">');
	    				$(this).siblings("div").find(".valueNames").append('<input type="button" class="btn btn-info js_addValueName" onclick="addValueName(this);" value="确定">');
	    				$(this).siblings("div").find(".valueNames").append('<input type="button" class="btn btn-default js_cancelValueName" onclick="cancelValueName(this);" value="取消">');
    					
    				});
    			}
    		});
		}
		
		function deleteCategoryAttr(attr_id){
			var cidObj = $(".threebtn").filter("button.btn-primary");
			var cid = cidObj.val();
			if(cid==null){
				$.jBox.info("请选择类目");
				return;
			}
			var attrTypeObj = $(".btnattrType").filter("button.btn-primary");
			var attrType = attrTypeObj.val();
			if(attrType==null){
				$.jBox.info("请选择销售属性信息");
				return;
			}
			jBox.confirm('确定要删除属性吗？', '提示', function (v, h, f) {
                if (v === 'ok') {
                	//alert("删除属性：" + attr_id +" 类型："+ attrType);
                	$.ajax({
                        url: "${ctx}/item/deleteCategoryAttr",
                        dataType:"json",
                        data:{cid:cid,attr_id:attr_id,attrType:attrType},
                        success: function(data){
                            if(data.success){
                                $.jBox.prompt(data.result, '消息', 'info', { closed: function () {
                                    queryCategoryAttr();
                                } });
                            }else{
                                $.jBox.info(data.errorMessages[0]);
                            }
                        },error:function(data){
                            $.jBox.info("系统繁忙，请稍后再试");
                        }
                    });
                }
            });
		}
		
		function deleteCategoryAttrValue(attr_id,value_id){
			var cidObj = $(".threebtn").filter("button.btn-primary");
			var cid = cidObj.val();
			if(cid==null){
				$.jBox.info("请选择类目");
				return;
			}
			var attrTypeObj = $(".btnattrType").filter("button.btn-primary");
			var attrType = attrTypeObj.val();
			if(attrType==null){
				$.jBox.info("请选择销售属性信息");
				return;
			}
			jBox.confirm('确定要删除属性值吗？', '提示', function (v, h, f) {
                if (v === 'ok') {
                	// alert("删除属性值：" + attr_id + "-" + value_id +" 类型："+ attrType);
                	$.ajax({
                        url: "${ctx}/item/deleteCategoryAttrValue",
                        dataType:"json",
                        data:{cid:cid,attr_id:attr_id,value_id:value_id,attrType:attrType},
                        success: function(data){
                            if(data.success){
                                $.jBox.prompt(data.result, '消息', 'info', { closed: function () {
                                    queryCategoryAttr();
                                } });
                            }else{
                            	$.jBox.info(data.errorMessages[0]);
                            }
                        },error:function(data){
                            $.jBox.info("系统繁忙，请稍后再试");
                        }
                    });  
                }
            });
		}
		
        function updateattr(){
            var inputs=$(".updateattr");
            var idname="";
            var ids="";
            var seniors = "";
            var haha="";
            if(inputs&&inputs.length>0){
                for(var i=0;i<inputs.length;i++){
                    var name=$(inputs[i]).val();
                    var id=$(inputs[i]).attr("name");
                    //var senior = $("input[name=isSenior"+id+"]:checked").val();
                    var senior = 0;
                    if(i==0){
                        ids="ids="+id;
                        idname="name"+id+"="+name;
                        seniors="senior"+id+"="+senior
                    }else{
                        ids=ids+"&ids="+id;
                        idname=idname+"&name"+id+"="+name;
                        seniors=seniors+"&senior"+id+"="+senior;
                    }
                }
            }else{
                $.jBox.info("数据没有变动不需要保存修改");
                return false;
            }
            haha=ids+"&"+idname+"&"+seniors;
            $.ajax({
                url: "${ctx}/item/updateattrname",
                dataType:"json",
                data:haha,
                success: function(data){
                    if(data.success){
                        $.jBox.prompt(data.msg, '消息', 'info', { closed: function () {
                            queryCategoryAttr();
                        } });
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(data){
                    $.jBox.info("系统繁忙，请稍后再试");
                }
            });
        }
        function updateattrvalue(){
            var inputs=$(".updateattrvalue");
            var idname="";
            var ids="";
            var haha="";
            if(inputs&&inputs.length>0){
                for(var i=0;i<inputs.length;i++){
                    var name=$(inputs[i]).val();
                    var id=$(inputs[i]).attr("name");
                    if(i==0){
                        ids="ids="+id;
                        idname="name"+id+"="+name;
                    }else{
                        ids=ids+"&ids="+id;
                        idname=idname+"&name"+id+"="+name;
                    }
                }
            }else{
                $.jBox.info("数据没有变动不需要保存修改");
                return false;
            }
            haha=ids+"&"+idname;
            $.ajax({
                url: "${ctx}/item/updateattrnamevalue",
                dataType:"json",
                data:haha,
                success: function(data){
                    if(data.success){
                        $.jBox.prompt(data.msg, '消息', 'info', { closed: function () {
                            queryCategoryAttr();
                        } });
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(data){
                    $.jBox.info("系统繁忙，请稍后再试");
                }
            });
        }
        function changeclass(th,cla){
            $(th).attr("class",cla);
        }
		//属性值添加操作：取消按钮
		function cancelValueName(obj){
			$(obj).parent().find(".newValueName").remove();
			$(obj).parent().find(".js_addValueName").remove();
			$(obj).remove();
		}
		//属性值添加操作：确定按钮
		function addValueName(obj){
			//类目id
			var cidObj = $(".threebtn").filter("button.btn-primary");
			var cid = cidObj.val();
			if(cid==null){
				$.jBox.info("请选择类目");
				return;
			}
			//属性id
			var attrAttrId = $(obj).parent().parent().siblings("input").attr("name");
			//新增的属性值
			var newValueNameParent = $(obj).parent();
			var newValueNames = newValueNameParent.find(".newValueName");
			var arrayValueName = new Array();
			var blcheck = false;
			newValueNames.each(function(i){
				var valueName_input = $(this).val();
				if(valueName_input==null || valueName_input.trim() == ""){
					blcheck = true;
					return false;
				}else{
					arrayValueName.push(valueName_input.trim());
				}
			});
			if(blcheck){
				$.jBox.info("新增属性值输入框还有未填写的，请完善！");
				return;
			}
			var valueNames = arrayValueName.join(",");
			if(valueNames==null || valueNames.length==0){
				return;
			}
			
			$.ajax({
    			url: "${ctx}/item/addValueName",
    			dataType:"json",
    			data:{
    				cid: cid,
    				attrId: attrAttrId,
    				valueNames: valueNames
    			},
    			cache:false,
    			success: function(data){
    				$.jBox.info("添加成功");
                    queryCategoryAttr();
    			},error:function(data){
                    $.jBox.info("系统繁忙，请稍后再试");
                }
    		});
			
		}
		function addAttrName(obj){
			var cidObj = $(".threebtn").filter("button.btn-primary");
			var cid = cidObj.val();
			if(cid==null){
				$.jBox.info("请选择类目");
				return;
			}
			var attrTypeObj = $(".btnattrType").filter("button.btn-primary");
			var attrType = attrTypeObj.val();
			if(attrType==null){
				$.jBox.info("请选择销售属性信息");
				return;
			}
			var attrName = $(obj).parent().siblings("input").val();
			//var isSenior = $("input[name=isSenior]:checked").val();
			var isSenior = 0;
			if(attrName!=null && attrName.length>0){
				$.ajax({
	    			url: "${ctx}/item/addAttrName",
	    			dataType:"json",
	    			data:{
	    				cid: cid,
	    				attrName: attrName,
	    				attrType: attrType,
	    				isSenior: isSenior
	    			},
	    			cache:false,
	    			success: function(data){
	    				if(data.success){
	    					queryCategoryAttr();
	    				}
	    			},error:function(){
                        $.jBox.info("系统繁忙，请稍后再试");
                    }
	    		});
			}else{
				$.jBox.info("请填写属性名称");
			}
			
			
		}
    function canclecid(whichbutton){
        if(whichbutton){
            //获取选中的button对象
            var xzbutton=$("."+whichbutton).filter("button.btn-primary")
            if(xzbutton){
                var cid=xzbutton.val();
                if(cid){
                    jBox.confirm('确定要删除类目吗？', '提示', function (v, h, f) {
                        if (v === 'ok') {
                            //处理
                            $("#popUpDiv").modal("show");
                            $.ajax({
                                url: "${ctx}/item/canclecid",
                                dataType:"json",
                                data:{
                                    cid: cid
                                },
                                cache:false,
                                type:"POST",
                                success: function(data){
                                    $("#popUpDiv").modal("hide");
                                    if(data.success){
                                        $.jBox.prompt('类目删除成功', '消息', 'info', { closed: function () {aftercancle(whichbutton)} });
                                    }else{
                                        $.jBox.info(data.msg);
                                    }
                                },error:function(){
                                    $("#popUpDiv").modal("hide");
                                    $.jBox.info("系统繁忙，请稍后再试");
                                }
                            });
                        }
                    });
                }else{
                    $.jBox.info("请选中需要删除的类目");
                }
            }else{
                $.jBox.info("请选中需要删除的类目");
            }
        }
    }
    function updateCategory(whichbutton){
    	var xzbutton=$("."+whichbutton).filter("button.btn-primary")
    	if(xzbutton){
            var cid=xzbutton.val();
            if(cid){
            	$("#editForm").modal("show");
            	$("#editCid").val($("#"+cid).val());
            	$("#whichbuttonId").val(whichbutton)
                $("#editCategoryId").val($("#"+cid).text());
            }else{
            	 $.jBox.info("请选中需要修改的类目");
            }
    	}else{
    		$.jBox.info("请选中需要修改的类目");
    	}
    }
    function edit(){
	    var editCid=$("#editCid").val();
	    var name=$("#editCategoryId").val();
	    var whichbutton=$("#whichbuttonId").val();
	    if(name==null||name==""){
	    	$.jBox.info("请输入类目名字");
	    }else{
	    	$.jBox.confirm("修改后，所有此类目下的商品类目会随之更改，请谨慎修改！","提示",function(value){
	  			if(value=='ok'){
	  				passajax(editCid,name,whichbutton);
	  			}
	  		});
	   }	
    }
    function passajax(editCid,name,whichbutton){
    	$.ajax({
	        url: "${ctx}/item/editCategory",
	        dataType:"json",
	        data:{
	            cid: editCid,
	            name:name
	        },
	        cache:false,
	        type:"POST",
	        success: function(data){
	        	$("#editForm").modal("hide");
	            if(data.success){
	                $.jBox.prompt('类目修改成功', '消息', 'info', { closed: function () {aftercancle(whichbutton)} });
	            }else{
	                $.jBox.info(data.msg);
	            }
	        },error:function(){
	        	$("#editForm").modal("hide");
	            $.jBox.info("系统繁忙，请稍后再试");
	        }
	    });
    }
    
    function editCloseDiv(){
    	$("#editForm").modal("hide");
    }
    function aftercancle(whichbutton){
        if(whichbutton){
            if(whichbutton=='onebtn'){
                $("#two").hide();
                $("#three").hide();
                $("#attrNames").empty();
                $("#addpro").hide();
                oneCategory();
            }else if(whichbutton=='twobtn'){
                var parentcid=$(".onebtn").filter("button.btn-primary").val();
                twoCategory(parentcid)
            }else if(whichbutton=='threebtn'){
                var parentcid=$(".twobtn").filter("button.btn-primary").val();
                threeCategory(parentcid)
            }
        }
    }
	</script>
</head>
<body>

	<div id="one" class="control-group">
		<h3>
			<div style="float:left;">一级类目:</div>
			<div>
                <button type="button" name="one" class="btn btn-info js_addItemCategory">+添加类目</button>
                <button type="button" name="one" class="btn btn-info" onclick="canclecid('onebtn')">-删除选中类目</button>
                <button type="button" name="one" class="btn btn-info" onclick="updateCategory('onebtn')">修改选中类目</button>
            </div>
		</h3>
		<div id="oneCategory">
			
			<!-- <button type="button" class="btn btn-default">Button</button>
			<button type="button" class="btn btn-primary">选中效果</button> -->
		</div>
	</div>
	<div id="two" class="control-group" style="display: none;">
		<h3>
			<div style="float:left;">二级类目:</div>
			<div>
                <button type="button" name="two" class="btn btn-info js_addItemCategory">+添加类目</button>
                <button type="button" name="one" class="btn btn-info" onclick="canclecid('twobtn')">-删除选中类目</button>
                <button type="button" name="one" class="btn btn-info" onclick="updateCategory('twobtn')">修改选中类目</button>
            </div>
		</h3>
		<div id="twoCategory">
		
		</div>
	</div>
	<div id="three" class="control-group" style="display: none;">
		<h3>
			<div style="float:left;">三级类目:</div>
			<div>
                <button type="button" name="three" class="btn btn-info js_addItemCategory">+添加类目</button>
                <button type="button" name="one" class="btn btn-info" onclick="canclecid('threebtn')">-删除选中类目</button>
                <button type="button" name="one" class="btn btn-info" onclick="updateCategory('threebtn')">修改选中类目</button>
            </div>
		</h3>
		<div id="threeCategory">
		
		</div>
	</div>
	<div id="attrType" class="control-group">
        <button type="button" class="btn btn-primary btnattrType js_queryCategoryAttr" value="2">平台类目属性</button>
		<button type="button" class="btn btn-default btnattrType js_queryCategoryAttr" value="1">销售类目属性</button>
	</div>
	<div id="addpro" class="control-group" style="display: none;">
		<div id="attrNames">
			<!-- 
			<div class="proInput">
				<input type="text"  id="attrName" name="attrName" class="add" placeholder="属性名">
				<span><input type="button" class="btn btn-info js_addAttrName" value="确定"><input type="button" class="btn btn-default js_cancelAttrName" value="取消"></span>
			-->
				<!-- 
				<span class="js_addValueName">+添加属性值</span>
				<div>
					<span class="valueNames">
						<input type="text"  id="valueName" name="valueName" class="add" placeholder="属性值">
						<input type="text"  id="valueName" name="valueName" class="add" placeholder="属性值">
					</span>
					<input type="button" class="btn btn-info" value="确定">
					<input type="button" class="btn btn-default" value="取消">
				</div>
				 -->
			<!-- 
			</div>
			 -->
		</div>
		<h6>
			<div id="js_attrName" style="cursor:pointer;">+添加属性</div>
            <input type="button" class="btn btn-info" style="background: #008000" onclick="updateattr();" value="保存修改的属性">
            <input type="button" class="btn btn-info" style="background: #FFA500" onclick="updateattrvalue();" value="保存修改的属性值">
		</h6>
	</div>

    <div class="modal hide fade" id="popUpDiv">
        <div class="modal-body">
            <div class="progress progress-striped active">
                <div class="bar" style="width: 100%;"></div>
            </div>
        </div>
    </div>
    <div class="modal hide fade" id="editForm">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h3>编辑类目</h3>
	        </div>
	        <div class="modal-body" style="text-align: center;">
					<div style="margin-top: 10px;">
					<input id="editCid" type="hidden" name="cid" class="input-medium"/>
					<input id="whichbuttonId" type="hidden" name="button" class="input-medium"/>
					<input id="editCategoryId" type="text" name="category" class="input-medium" style="margin-top:15px"/>
		        	<a href="javascript:edit()" class="btn btn-primary">确定</a>
		            <a href="javascript:editCloseDiv()" class="btn">关闭</a>
		        </div>
	        </div>
	</div>
</body>
</html>