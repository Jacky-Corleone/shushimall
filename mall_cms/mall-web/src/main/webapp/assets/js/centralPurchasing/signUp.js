$(function(){
	// 报名弹框取消
	$(".js_central_purchasing_cancle").click(function(){
		// 清空
		$(':input','#signUpForm').not(':button,:submit,:reset,:hidden').val('').removeAttr('checked').removeAttr('selected');
		$(".js_central_purchasing").hide();
	});
	
	//校验
	$("#signUpForm").validate({
        rules: {
        	enterpriseName:"required",
        	enterpriseLinkman: "required",
        	phoneNo:{
        		required:true,
        		phoneNoValidate:true
        	}
		}
    });
	
	//校验手机号码
	jQuery.validator.addMethod("phoneNoValidate", function(value, element, param) {
		var chrnum = /^[1]([3|5|7|8][0-9]{1}|59|58|88|89)[0-9]{8}$/;
		return chrnum.test($("#phoneNo").val());
	}, $.validator.format("手机号码不正确!"));
});

//报名
function editCentralPurchasing(o, activitesDetailsId){
	$("#activitesDetailsId").val(activitesDetailsId);
	var href = $(o).parents(".Product_box_1").find(".big_img a").attr("href");
	if(href){
		$("#signUpTargetUrl").val(href);
	}
	$(".js_central_purchasing").show();
}

//输入字符显示
function numInput(obj,length){
	if(obj.value==obj.value2)
		return;
	if(length == 0 && obj.value.search(/^\d*$/)==-1)
		obj.value=(obj.value2)?obj.value2:'';
	else if(obj.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)
		obj.value=(obj.value2)?obj.value2:'';
	else obj.value2=obj.value;
}

/**
 * 报名
 */
function signUp(btnId){
	$("#"+btnId).loadingMsg();
	//校验
	var isValid = $("#signUpForm").valid();
	if(!isValid){
		$("#"+btnId).hideMsg();
		return;
	}
	
	// 提交
	var url = $("#signUpForm").attr("action");
	var signUpFormObj = $('#signUpForm').serialize();
	$.ajax({
		url:url,
		type:"post",
		data:signUpFormObj,
		dataType: "json",
		success:function(data){
			if(data.success){
				alert("报名成功!");
				// 清空
				$(':input','#signUpForm').not(':button,:submit,:reset,:hidden').val('').removeAttr('checked').removeAttr('selected');
				if($("#signUpTargetUrl")){
					window.location.href=$("#signUpTargetUrl").val();
				} else{
					window.location.reload();
				}
			} else if(data.errorMessages){
				alert(data.errorMessages[0]);
			} else{
				alert("报名失败!");
			}
			$("#"+btnId).hideMsg();
		}
	});
}