<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>品牌管理</title>
<meta name="decorator" content="default" />
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
	.btn{margin: 5px 10px 0 20px;}
	#oneCategory{ padding: 0 20px;}
	.select2-container-multi .select2-choices {
    padding: -30px -50px -30px -50px;
    min-height: 26px;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".btn").bind("click", function(){
			$(this).removeClass("btn-default").addClass("btn-primary");
			$(this).siblings().removeClass("btn-primary").addClass("btn-default");
		});
	});
	function onFirstCatClick(cid) {
		$('#brandDiv').hide();
		$('#firstSelectedId').val(cid);
		$("#imgEditDiv").hide();
		$("#imgAddDiv").hide();
		$("#imgDiv").html("");
		$("#secCatDiv").html("");
		$("#thirdCatDiv").html("");
		$.ajax({
					url : "${ctx}/brand/getChildCategory",
					type : "POST",
					data : "pCid=" + cid,
					dataType : "json",
					success : function(data) {
						var html = "<h3> <div style='float:left;'>二级类目:</div> </h3>";
						$(data)
								.each(
										function(i, catogery) {
											html = html
													+ '<button type="button" class="btn onebtn btn-default"  id="'
													+ catogery.categoryCid
													+ '" onclick="onSecdCatClick(this.id);">'
													+ catogery.categoryCName
													+'</button>'
										});
						$("#secCatDiv").html(html);
					},
					error : function(xmlHttpRequest, textStatus, errorThrown) {
						$.jBox.error("系统错误！请稍后再试！");
					}
				});
	}

	function onSecdCatClick(cid) {
		$('#brandDiv').hide();
		$('#'+cid).removeClass("btn-default").addClass("btn-primary");
		$('#'+cid).siblings().removeClass("btn-primary").addClass("btn-default");
		$('#secondSelectedId').val(cid);
		$("#imgEditDiv").hide();
		$("#imgAddDiv").hide();
		$("#imgDiv").html("");
		$("#thirdCatLab").html("");
		$.ajax({
					url : "${ctx}/brand/getChildCategory",
					type : "POST",
					data : "pCid=" + cid,
					dataType : "json",
					success : function(data) {
						var html = "<h3> <div style='float:left;'>三级类目:</div> </h3>";
						$(data)
								.each(
										function(i, catogery) {
											html = html
													+ '<button type="button" class="btn onebtn btn-default" id="'
													+ catogery.categoryCid
													+ '" onclick="onThirdCatClick('
													+ cid + ',this.id);">'
													+ catogery.categoryCName
													+'</button>'
										});
						$("#thirdCatDiv").html(html);
					},
					error : function(xmlHttpRequest, textStatus, errorThrown) {
                        $.jBox.error("系统错误！请稍后再试！");
					}
				});
	}
	function onThirdCatClick(secondCid, thirdCid) {
		$('#brandDiv').show();
		$('#imgAddDiv').hide();
		$('#brandEditDiv').hide();
		$('#brandAddDiv').hide();
		$("input[name='brandName']").each(
			function(){
			$(this).attr('value','');
		});
		$('#s2id_brandSelect li').each(function (i,index){


		});
		$('.select2-search-choice').each(function (i,index){
			$(this).remove();
		});
		$('#picDiv img').remove();
		/*$('#editPicDiv img').remove();*/
		$('#'+thirdCid).removeClass("btn-default").addClass("btn-primary");
		$('#'+thirdCid).siblings().removeClass("btn-primary").addClass("btn-default");
		$('#thirdSelectedId').val(thirdCid);
		$("#thirdCatSelectedId").val(thirdCid);
        $("#thirdCatSelectedIdedit").val(thirdCid);
		$("#secCatSelectedId").val(secondCid);
		$("#imgDiv").html("");
		$.ajax({
			url : "${ctx}/brand/getCategoryBrand",
			type : "POST",
			data : "secondCid=" + secondCid + "&thirdCid=" + thirdCid,
			dataType : "json",
			success : function(data) {
				var html = "";
				$(data).each(
						function(i, brand) {
							html = html + '<img id="brandImg_' + brand.brandId
									+ '" alt="' + brand.brandName + '" src="${filePath}'
									+ brand.brandLogoUrl
									+ '" onclick="onBrandImgClick(this)" title="' + brand.brandName + '" style="width:100px;height:50px">';
						});
				$("#imgDiv").html(html);
			},
			error : function(xmlHttpRequest, textStatus, errorThrown) {
                $.jBox.error("系统错误！请稍后再试！");
			}
		});
	}
	function checkBrandName(brandName){
		var secondCid = $("#secCatSelectedId").val();
		var thirdCid = $("#thirdCatSelectedId").val();
		var flag = false;
		var i =0;
		var brandString ="";
		$.ajax({
			url : "${ctx}/brand/getCategoryBrand",
			type : "POST",
			data : "secondCid=" + secondCid + "&thirdCid=" + thirdCid,
			dataType : "json",
			success : function(data) {
				var html = "";
				$(data).each(
						function(i, brand) {
							brandString +=brand.brandName+",";
							if(brandName==brand.brandName){
								/* $.jBox.info(brand.brandName);
								i = 1;
								$.jBox.info("名称已经存在");
								flag = true;
								return flag; */
							}
						});
			},
			error : function(xmlHttpRequest, textStatus, errorThrown) {
                $.jBox.error("系统错误！请稍后再试！");
			}
		});
//		$.jBox.info(brandString);
		/* $.jBox.info(i);
		$.jBox.info(flag);
		return flag; */
	}
	function cancelEditForm() {
		$("#imgEditDiv").hide();
	}

	/* function submitEditForm() {
		$.ajax({
			cache : true,
			type : "POST",
			url : "${ctx}/brand/modifyBrand",
			data : $('#imgEditForm').serialize(),// 你的formid
			async : true,
			error : function(request) {
				$.jBox.info("系统错误！请稍后再试！");
			},
			success : function(data) {
				onThirdCatClick($("#secCatSelectedId").val(), $(
						"#thirdCatSelectedId").val());
				$('#imgEditDiv').hide();
			}
		});
	} */

	function addBrand() {
		$("#uploadPicDiv").modal('show');
		$("#addBrandDiv").hide();
        $('#picDiv img').remove();
        $("#addbrandNameid").val("");
		/* if(!$("#firstSelectedId").val()){
			$.jBox.info("请选择一级类目！");
		}else if(!$("#secondSelectedId").val()){
			$.jBox.info("请选择二级类目！");
		}else if(!$("#thirdSelectedId").val()){
			$.jBox.info("请选择三级类目！");
		}else{
			$("#imgAddDiv").show();
		} */
	}
	function getAddPicUrls(){
		var picUrls = [];

        $("#picDiv").find("img").each(function(i,item){
            picUrls.push($(item).attr("src").replace("${filePath}",""));
        });
        return picUrls;
	}
	//提交添加的品牌
	function submitAddForm() {
		var brandName =$('#uploadPicDiv input[name="brandName"]').val();
		var brand=$("#brandId").val();
    	if(brandName.length==0){
            $.jBox.info("名称不能为空!");
    		return;
    	}else{
    		var picUrls = getAddPicUrls();
    		var secondCid = $("#secCatSelectedId").val();
       		var thirdCid = $("#thirdCatSelectedId").val();
            $("#picUrls").val(picUrls);
            if(picUrls.length<=0){
                $.jBox.info("请选择图片!");
            }else{
            	$.ajax({
           			url : "${ctx}/brand/getBrandList",
           			type : "POST",
           			data : "brandName=" + brandName+"&brandId=" + brand,
           			dataType : "json",
           			success : function(data) {
           				var html = "";
           				var flag = false;
           				if(data==""||data=="undefined"||data==null){
           					flag = false;
           				}else{
           					flag=true;
           				}
           				
           				if(flag){
                            $.jBox.info("品牌名称已经存在,请填写不同名称!");
                            return;
           				}else{
           					$.ajax({
           		    			cache : true,
           		    			type : "POST",
           		    			url : "${ctx}/brand/addBrand",
           		    			data : $("#addBrandForm").serialize(),// 你的formid
           		    			async : true,
                                dataType : "json",
           		    			error : function(request) {
                                    $.jBox.error("系统错误！请稍后再试！");
           		    			},
           		    			success : function(data) {
                                    if(data.success){
                                        /*$('#brandSelect').empty();*/
                                        $.jBox.prompt('新增完成', '消息', 'info', { closed: function () {
                                            /*                                        var html="";
                                             $(data).each(function(i,brand){
                                             html += "<option value='"+brand.brandId+"'>"+brand.brandName+"</option>";
                                             });*/
                                            cancelAddForm();
                                            /* $("#brandSelect").html(html);*/
                                            $('#imgAddDiv').hide();
                                            $('#picDiv img').remove();
                                            $("#uploadPicDiv").modal('hide');
                                            onThirdCatClick($("#secCatSelectedId").val(), $(
                                                    "#thirdCatSelectedId").val());
                                        }});
                                    }else{
                                        $.jBox.error(data.msg);
                                    }
           		    			}
           		    		});
           				}

           			},
           			error : function(xmlHttpRequest, textStatus, errorThrown) {
                        $.jBox.error("系统错误！请稍后再试！");
           			}
           		});

            }
    	}
	}

	function cancelAddForm() {
		$("#brandAddDiv").hide();
		$("#brandAddDiv input[name='brandName']").val("");
		$("#brandAddDiv input[name='brandLogoUrl']").val("");
	}

	//提交选择的品牌
	function submitBrandSelectForm(){

		if($('#brandSelect option:selected').val()){
			$.ajax({
				cache : true,
				type : "POST",
				url : "${ctx}/brand/addCategoryBrandBatch",
				data : $("#imgAddForm").serialize(),// 你的formid
				async : true,
				error : function(request) {
                    $.jBox.error("系统错误！请稍后再试！");
				},
				success : function(data) {
					onThirdCatClick($("#secCatSelectedId").val(), $(
							"#thirdCatSelectedId").val());
					cancelBrandSelectForm();
				}
			});
		}else{
            $.jBox.info("请选择品牌!");
		}

	}

	function cancelBrandSelectForm(){
		cancelAddForm();
		$("#brandSelect").val("");
		$("#imgAddDiv").hide();
	}
	//显示添加品牌的DIV
	function showBrandAddDiv(){
		$("#brandAddDiv").show();
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
	function editCloseUploadDiv(){
        $("#editUploadPicDiv").modal('hide');
    }
	function startUpload(){
		var brandName =$('#uploadPicDiv input[name="brandName"]').val();
		var brand=$("#brandId").val();
    	if(brandName.length==0){
            $.jBox.info("名称不能为空!");
    		return;
    	}
		if($('#uploadPic').val()&&brandName){
			$.ajax({
  	   			url : "${ctx}/brand/getBrandList",
  	   			type : "POST",
  	   			data : "brandName=" + brandName+"&brandId=" + brand,
  	   			dataType : "json",
  	   			success : function(data) {
  	   				var html = "";
  	   				var flag = false;
  	   				if(data==""||data=="undefined"||data==null){
  	   					flag = false;
  	   				}else{
  	   					flag=true;
  	   				}
  	   				if(flag){
  	                    $.jBox.info("品牌名称已经存在,请填写不同名称!");
  	   				}else{
			        $('#picDiv img').remove();
			        $.ajaxFileUpload({
			                    url: '${ctx}/fileUpload/upload?date='+new Date(), //用于文件上传的服务器端请求地址
			                    secureuri: false, //是否需要安全协议，一般设置为false
			                    fileElementId: 'uploadPic', //文件上传域的ID
			                    dataType: 'json', //返回值类型 一般设置为json
			                    type:"post",
			                    success: function (data, status){
			                      if(data.success){
		                              //服务器成功响应处理函数
		                              var html = "<img src='${filePath}"+data.url+"' class='img-polaroid' style='width:50px;height:50px'>";
		                              $("#picDiv").prepend(html);
		                              submitAddForm();
		                          }else{
		                              $.jBox.info(data.msg);
		                          }
			                    },
			                    error: function (data, status, e){//服务器响应失败处理函数
			                        $.jBox.error(e);
			                    }
			              });
  	   				}
  	   			}
			})
		}else{
			if(!$('#uploadPic').val()){
                $.jBox.info("请选择上传图片!");
			}
			if(!brandName){
                $.jBox.info("请填写品牌名称!");
			}
		}

    }
    function onBrandImgClick(brandImg) {
    	$("#editUploadPicDiv").modal('show');
		$("#editBrandDiv").show();
		$('#editBrandName').val(brandImg.alt);
		$('#brandLogoUrl').val("${filePath}"+brandImg.src);
		$('#brandId').val(brandImg.id.substring(9));

		/*$('#imgPicUrl').attr("src","${filePath}"+brandImg.src);*/
        $("#editimgid").attr("src",brandImg.src);
        //图片查看
        $('#editimgid').fancyzoom({
            Speed: 400,
            imagezindex:1100,
            showoverlay: false,
            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
        });
		/* var html = "<img src='"+brandImg.src+"' class='img-polaroid' style='width:50px;height:50px'>";
        $("#editBrandNameDiv").append(html); */
		/* $("#imgEditForm").html("");
		var html = "";
		var brandId = brandImg.id.substring(9);
		html = html
				+ '<input type="hidden" name="brandId" id="brandId" value="'+brandId+'" />';
		html = html + '<input type="hidden"  name="editPicUrls" id="editPicUrls" />';
		html = html
				+ '<input type="hidden" id="brandLogoUrl" name="brandLogoUrl" value="'+brandImg.src+'" />';
		html = html+'<div id="brandEditDiv">';
		html = html+'<label style="margin-left:20px;">品牌名称：</label><input name="brandName" class="input-medium" value="'+brandImg.alt+'" /> ';
		html = html+'<div class="row-fluid" id="editPicDiv"> ';
		html = html+'<div class="span3" style="margin-top:10px;width:400px;">';
		html = html+'<a class="btn" href="javascript:editPic('+1+')"><i class="icon-plus"></i>编辑图片</a> ';

		html = html+'<input class="btn" type="button" value="确定" onclick="submitEditForm()" /> ';
		html = html+'<input class="btn" type="button" value="取消" onclick="cancelEditForm()" /> ';
		html = html+'</div> ';
		html = html+'</div> ';
		html = html+'</div> ';
		$("#imgEditDiv").show();
		$("#imgEditForm").html(html); */
	}
    function editPic(flag){
        switch (flag){
            case '1':
                $("#editTempFlag").val("1");
                break;
            case '2':
                $("#editTempFlag").val("2");
                break;
        }
        $("#editUploadPicDiv").modal('show');
    }
    function editStartUpload(){
    	/*$('#editPicDiv img').remove();*/
        $.ajaxFileUpload({
                    url: '${ctx}/fileUpload/upload?date='+new Date(), //用于文件上传的服务器端请求地址
                    secureuri: false, //是否需要安全协议，一般设置为false
                    fileElementId: 'editUploadPic', //文件上传域的ID
                    dataType: 'json', //返回值类型 一般设置为json
                    type:"post",
                    success: function (data, status){  //服务器成功响应处理函数
                        /* var tempFlag = $("#editTempFlag").val();
                        if('1'==tempFlag){ */
                            //图片上传
                            var html = "<img src='${filePath}"+data.url+"' class='img-polaroid' style='width:50px;height:50px'>";
                            $("#editPicDiv").prepend(html);
                        /* }else{
                            //相册的图片上传
                            var html = "<img src='"+data.url+"' class='img-polaroid' style='width:50px;height:50px'>";
                            $("#picDiv2").prepend(html);
                        } */
                        $("#editUploadPicDiv").modal('hide');
                    },
                    error: function (data, status, e){//服务器响应失败处理函数
                        $.jBox.error(e);
                    }
                }
        );
        return false;
    }
    function getEditPicUrls(){
		var picUrls = [];

        $("#editPicDiv").find("img").each(function(i,item){
            picUrls.push($(item).attr("src"));
        });
        return picUrls;
	}
    function submitEditForm() {
    	var brandName =$('#editBrandName').val();
    	var brand=$("#brandId").val();
    	if(brandName.length==0){
            $.jBox.info("名称不能为空!");
    		return;
    	}
   		var picUrls = getEditPicUrls();
       	if(picUrls.length==0){
       		picUrls.push($("#brandLogoUrl").val());
       	}
        $("#editPicUrls").val(picUrls);
        var secondCid = $("#secCatSelectedId").val();
   		var thirdCid = $("#thirdCatSelectedId").val();
   		var brand=$("#brandId").val();
   		var flag = false;
   		var i =0;
   		var brandString ="";
   		$.ajax({
   			url : "${ctx}/brand/getBrandList",
   			type : "POST",
   			data : "brandName=" + brandName+"&brandId=" + brand,
   			dataType : "json",
   			success : function(data) {
   				var html = "";
   				var flag = false;
   				if(data==""||data=="undefined"||data==null){
   					flag = false;
   				}else{
   					flag=true;
   				}
   				if(flag){
                    $.jBox.info("品牌名称已经存在,请填写不同名称!");
   				}else{
   					$.ajax({
		    			cache : true,
		    			type : "POST",
		    			url : "${ctx}/brand/modifyBrand",
		    			data : $('#editBrandForm').serialize(),// 你的formid
		    			async : true,
		    			error : function(request) {
                            $.jBox.info("系统错误！请稍后再试！");
		    			},
		    			success : function(data) {
                            $.jBox.info("修改成功!");
		    				/*var html="";
   		    				$(data).each(function(i,brand){
   		    				 html += "<option value='"+brand.brandId+"'>"+brand.brandName+"</option>";
   		    				});*/
		    				onThirdCatClick($("#secCatSelectedId").val(), $(
		    						"#thirdCatSelectedId").val());
		    				$('#imgEditDiv').hide();
		    				$('#editUploadPicDiv').hide();
                            /*$('#editPicDiv img').remove();*/
                            $("#editUploadPicDiv").modal('hide');
		    				/*$("#brandSelect").html(html);*/
		    			}
		    		});
   				}

   			},
   			error : function(xmlHttpRequest, textStatus, errorThrown) {
                $.jBox.error("系统错误！请稍后再试！");
   			}
   		});

	}
    function showAddBrandDiv(){
    	$('#addBrandDiv').show();
    }
    function hideAddBrandDiv(){
    	$('#addBrandDiv').hide();
    }
  //提交选择的品牌
	function submitBrandAddSelectForm(){
        var check=$("input[name='brandck']:checked").val();
		if(check){
            var checkids="";
            var formserialize= $("#addBrandForm").serialize();
            $("input[name='brandck']:checked").each(
                    function(i, brand) {
                        checkids=checkids+"&brandIds="+$(brand).val();
                    });
			$.ajax({
				cache : true,
				type : "POST",
				url : "${ctx}/brand/addCategoryBrandBatch",
				data : formserialize+checkids,// 你的formid
				async : true,
				error : function(request) {
                    $.jBox.error("系统错误！请稍后再试！");
				},
				success : function(data) {
					onThirdCatClick($("#secCatSelectedId").val(), $(
							"#thirdCatSelectedId").val());
					$('#uploadPicDiv').hide();
					$("#uploadPicDiv").modal('hide');
				}
			});
		}else{
            $.jBox.info("请在列表中选择品牌!");
		}
	}
  	//图片编辑保存
	function editUpload(){
		var brandName =$('#editBrandName').val();
		var brand=$("#brandId").val();
  		if($('#editUploadPic').val()&&brandName){
  			$.ajax({
  	   			url : "${ctx}/brand/getBrandList",
  	   			type : "POST",
  	   			data : "brandName=" + brandName+"&brandId=" + brand,
  	   			dataType : "json",
  	   			success : function(data) {
  	   				var html = "";
  	   				var flag = false;
  	   				if(data==""||data=="undefined"||data==null){
  	   					flag = false;
  	   				}else{
  	   					flag=true;
  	   				}
  	   				if(flag){
  	                    $.jBox.info("品牌名称已经存在,请填写不同名称!");
  	   				}else{
			  			$.ajaxFileUpload({
			                url: '${ctx}/fileUpload/upload?date='+new Date(), //用于文件上传的服务器端请求地址
			                secureuri: false, //是否需要安全协议，一般设置为false
			                fileElementId: 'editUploadPic', //文件上传域的ID
			                dataType: 'json', //返回值类型 一般设置为json
			                type:"post",
			                success: function (data, status){  //服务器成功响应处理函数
			                    if(data.success){
			                        $("#editimgid").attr("src",data.url);
			                        //图片查看
			                        $('#editimgid').fancyzoom({
			                            Speed: 400,
			                            showoverlay: false,
			                            imagezindex:1100,
			                            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
			                        });
			                        $("#editUploadPicDiv").modal('hide');
			                        submitEditForm();
			                    }else{
			                        $.jBox.info(data.msg);
			                    }
			                },
			                error: function (data, status, e){//服务器响应失败处理函数
			                    $.jBox.error(e);
			                }
			            });
  	                 }
  	   			}
  	   	})				
  		}else if(!brandName){
            $.jBox.info("请填写品牌名称!");
  		}else{

  			$.jBox.info("请上传图片!");

  		}


        return ;
    }
	function  uploadPicPreviewImg(obj) {
    	if( !obj.value.match( /.jpg|.jpeg|.png|.bmp/i ) ){
            $.jBox.info("图片格式错误！");
    		$('#uploadPic').val("");
    		return false;
    	}else{
    		return true;
    	}
    }
	function  editUploadPicPreviewImg(obj) {
    	if( !obj.value.match( /.jpg|.jpeg|.png|.bmp/i ) ){
            $.jBox.info("图片格式错误！");
    		$('#uploadPic').val("");
    		return false;
    	}else{
    		return true;
    	}
    }
   function cancle(){
       jBox.confirm('是否删除该品牌？', '提示', function (v, h, f) {
           if(v=='ok'){
               cancle1();
           }
       });
   }
   function cancle1(){
       var brandid=$("#brandId").val();
       var thirdid=$("#thirdCatSelectedIdedit").val();
       if(brandid){
           $.ajax({
               cache : true,
               type : "POST",
               url : "${ctx}/brand/canclebrand",
               data : {
                   brandid:brandid,
                   thirdid:thirdid
               },// 你的formid
               dataType:'json',
               error : function() {
                   $.jBox.error("系统错误！请稍后再试！");
               },
               success : function(data) {
                   if(data.success){
                       $.jBox.prompt('删除成功', '消息', 'info', { closed: function () {
                           $("#editUploadPicDiv").modal('hide');
                           //$('#uploadPicDiv').hide();
                           onThirdCatClick($("#secCatSelectedId").val(), $("#thirdCatSelectedId").val());
                       } });
                   }else{
                       $.jBox.info(data.msg);
                   }
               }
           });
       }else{
           $.jBox.info("无法获取品牌id，无法删除！");
       }
   }

    function page(n,s){
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        selectpage(n);
    }
    function selectpage(p){
        var page=p;
        var rows=$("#pageSize").val();
        var brandName=$("#brandNameid").val();
        $.ajax({
            url:"${ctx}/brand/selectbrandlist/",
            type:"post",
            data:{
                page:page,
                rows:rows,
                brandName:brandName
            },
            dataType:'json',
            success:function(data){
                if(data.success){
                    var html="<div class='pagination'>"+data.msg+"</div>";
                    $(".pagination").replaceWith(html);
                    var html2="";
                    $(data.obj.list).each(function(i,item){
                        i++;
                        var check='<input type="checkbox" id="ck'+getcode(item.brandId)+'" name="brandck" value="'+getcode(item.brandId)+'" data="">';
                        html2=html2+"<tr><td>"+i+check+"</td><td>"+getcode(item.brandName)+"</td><td>";
                        html2=html2+"</tr>";
                    });
                    $("#tabletbody").html(html2);
                    $("#pageNo").val(data.obj.pageNo);
                    $("#pageSize").val(data.obj.pageSize);
                }
            }
        });
    }
    function getcode(date){
        if(date){
            return date;
        }else{
            return '';
        }
    }
</script>
</head>

<body>
	<div >
		<div >
			<form id="searchForm">
				<div class="control-group">
					<h3>
						<div style="float:left;">一级类目:</div>

						<c:forEach items="${cList}" var="ic">
						<button  type="button" class="btn onebtn btn-default" id="${ic.categoryCid}"
								onclick="onFirstCatClick(this.id)" >${ic.categoryCName}</button>
						</c:forEach>
					</h3>
					<div id="oneCategory">

						<!-- <button type="button" class="btn btn-default">Button</button>
						<button type="button" class="btn btn-primary">选中效果</button> -->
					</div>
				</div>
				<%-- <div class="btn-group">
					<label>一级类目：</label>
					<c:forEach items="${cList}" var="ic">
						<button  type="button" class="btn" id="${ic.categoryCid}"
							onclick="onFirstCatClick(this.id)" >${ic.categoryCName}</button>
					</c:forEach>
				</div> --%>
				<div id="secCatDiv" class="control-group">
					<h3>
						<div style="float:left;">二级类目:</div>
					</h3>
					<div id="twoCategory">

					</div>
				</div>
				<!-- <div id="secCatDiv" class="control-group">
					<div style="float:left;">二级类目:</div>
				</div> -->
				<div id="thirdCatDiv" class="control-group"></div>
			</form>

			<div id="brandDiv" style="margin-bottom: 20px;display: none;">
				<a href="javascript:void(0)" class="btn" onclick="addBrand()" ><i class="icon-plus"></i>添加品牌</a>
			</div>
			<div id="imgDiv" class="contrl-group"></div>
			<div id="imgEditDiv" style="display: none;">
				<form id="imgEditForm">

				</form>
				<!-- <input type="button" value="确定" onclick="submitEditForm()" /> <input
					type="button" value="取消" onclick="cancelEditForm()" /> -->
			</div>

			<%-- <div id="imgAddDiv" style="display: none;">
				<form id="imgAddForm" >
					<!-- <input type="hidden" name="picUrls" id="picUrls"> -->
					<!-- <input id="secCatSelectedId" name="secondLevCid" type="hidden" />
					<input id="thirdCatSelectedId" name="thirdLevCid" type="hidden" /> -->
					<input id="firstSelectedId" type="hidden">
					<input id="secondSelectedId" type="hidden">
					<input id="thirdSelectedId" type="hidden">
					<select id="brandSelect" multiple="multiple" name="brandIds">
						<c:forEach items="${brandList }" var="brand">
							<option value="${brand.brandId }">${brand.brandName }</option>
						</c:forEach>
					</select>
					<div id="brandAddDiv" style="display: none;margin-top: 10px;">
						<label>品牌名称：</label><input type="text" name="brandName" class="input-medium"/>
						<div class="row-fluid" id="picDiv">
			                <div class="span3" style="width: 400px;margin-left: -20px;">
			                    <a class="btn" href="javascript:addPic('1')"><i class="icon-plus"></i>添加图片</a>
			                    <input class="btn" type="button" value="确定" onclick="submitAddForm()" />
								<input class="btn" type="button" value="取消" onclick="cancelAddForm()" />
			                </div>

			            </div>
					</div>

				</form>
				<input class="btn" type="button" value="添加" onclick="showBrandAddDiv()" />
				<input class="btn" type="button" value="保存" onclick="submitBrandSelectForm()" />
				<input class="btn" type="button" value="取消" onclick="cancelBrandSelectForm()" />
			</div> --%>
		</div>
	</div>
	<!--图片新增弹出框-->
	<form:form id="addBrandForm" modelAttribute="brand" >
	<div class="modal hide fade" id="uploadPicDiv">

	    <input type="hidden" name="tempFlag" id="tempFlag">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h3>添加品牌</h3>
	        </div>

	        <div class="modal-body" style="text-align: center;">
	        	<input id="secCatSelectedId"  name="secondLevCid" type="hidden" />
				<input id="thirdCatSelectedId" name="thirdLevCid" type="hidden" />
				<input type="hidden" name="picUrls" id="picUrls">
                <input id="pageNo" name="page" type="hidden" value="${p.pageNo}" />
                <input id="pageSize" name="rows" type="hidden" value="${p.pageSize}" />
	        	<div>
<%--	        	<select id="brandSelect"  multiple="multiple" name="brandIds">
					<c:forEach items="${brandList }" var="brand">
						<option value="${brand.brandId }">${brand.brandName }</option>
					</c:forEach>
				</select>--%>
                    品牌名称：<input class="input-medium" type="text" name="brandNameselect" id="brandNameid" >
                    <input class="btn btn-primary" type="button" style="margin:10px 0 5px 0;" value="搜索" onclick="selectpage(1)" />
                    <table id="contentTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
                        <thead>
                        <tr>
                            <th width="40%">序号</th>
                            <th width="60%">品牌名称</th>
                        </tr>
                        </thead>
                        <tbody id="tabletbody">
                        <c:forEach items="${p.list}" var="user" varStatus="s">
                            <tr>
                                <td>
                                ${s.count}
                                <input type="checkbox" id="ck${user.brandId}" name="brandck" value="${user.brandId}" data="">
                                </td>
                                <td>${user.brandName}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="pagination">${p}</div>
				<div class="row-fluid">
	            	<input class="btn" type="button" style="margin:10px 0 5px 0;" value="保存品牌" onclick="submitBrandAddSelectForm()" />
					<a class="btn" href="javascript:showAddBrandDiv()"><i class="icon-plus"></i>新增品牌</a>
				</div>
				<hr>
				</div>
				<div id="addBrandDiv" style="display:none ">
				<div style="margin-top: 10px;" id="picDiv">
				<div id="addBrandNameDiv">
				<label>品牌名称：</label><input type="text" name="brandName" id="addbrandNameid" class="input-medium"/>
				</div>
				</div>
	            <p>
	                <input type="file" id="uploadPic" onchange="uploadPicPreviewImg(this)" name="file" />
	            </p>

	        	<a href="javascript:startUpload()" class="btn btn-primary">确定</a>
	            <a href="javascript:closeUploadDiv()" class="btn">关闭</a>
	            <a href="javascript:hideAddBrandDiv()" class="btn"><i class="icon-plus"></i>取消新增</a>
	        </div>
	        </div>
	</div>
	</form:form>
	<!--图片编辑弹出框-->
	<form:form id="editBrandForm" modelAttribute="brand" >
	<div class="modal hide fade" id="editUploadPicDiv">
	    	<input type="hidden" name="tempFlag" id="tempFlag">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	            <h3>编辑品牌</h3>
	        </div>
	        <div class="modal-body" style="text-align: center;">
	        	<input id="secCatSelectedId"  name="secondLevCid" type="hidden" />
				<input id="thirdCatSelectedId" name="thirdLevCid" type="hidden" />
				<input id="thirdCatSelectedIdedit" name="thirdLevCidedit" type="hidden" />
				<input type="hidden" id="brandLogoUrl" name="brandLogoUrl" />
				<input type="hidden" name="picUrls" id="picUrls">
				<input type="hidden" name="brandId" id="brandId"/>
				<input type="hidden"  name="editPicUrls" id="editPicUrls" />
	        	<%-- <div>
	        	<select id="brandSelect"  multiple="multiple" name="brandIds">
					<c:forEach items="${brandList }" var="brand">
						<option value="${brand.brandId }">${brand.brandName }</option>
					</c:forEach>
				</select>
				<div class="row-fluid">
	            	<input class="btn" type="button" style="margin:10px 0 5px -140px;" value="保存品牌" onclick="submitBrandAddSelectForm()" />
				</div>
				<div class="row-fluid">
					<a class="btn" href="javascript:showAddBrandDiv()"><i class="icon-plus"></i>添加品牌</a>
	            	<a class="btn" href="javascript:hideAddBrandDiv()"><i class="icon-plus"></i>关闭品牌</a>
				</div>
				<hr>
				</div> --%>
				<div id="editBrandDiv" style="display:none ">
					<div style="margin-top: 10px;" id="editPicDiv">
					<div id="editBrandNameDiv">
					<label>品牌名称：</label><input id="editBrandName" type="text" name="brandName" class="input-medium"/>
					</div>
                    <div id="editBrandimgDiv">
                    <label>品牌图片：</label>
                    <img src='' id="editimgid" class='img-polaroid' style='width:150px;height:100px'>
                    </div>
					</div>
		            <p>
		                <input type="file" id="editUploadPic" onchange="editUploadPicPreviewImg(this)" name="file" />
		            </p>

		        	<a href="javascript:editUpload()" class="btn btn-primary">确定</a>
                    <a href="javascript:cancle()" class="btn btn-primary">删除</a>
		            <a href="javascript:editCloseUploadDiv()" class="btn">关闭</a>
		        </div>
	        </div>
	</div>
	</form:form>
</body>
</html>