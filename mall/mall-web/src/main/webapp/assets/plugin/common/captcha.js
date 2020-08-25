/**
 * 点击发送验证码时，判断此手机号是否符合发送验证码的资格，
 * 一个手机号每天只能收到3条相同的验证码
 */
function isSendCaptcha(contactInputId,type){
	var phoneRegex = /^[1]([3|5|7|8][0-9]{1}|59|58|88|89)[0-9]{8}$/;
	var contact = $("#"+contactInputId).val();
	if(!phoneRegex.test(contact)){
		alert("手机格式不正确!");
		return false;
	}
	var data = {
			contact:contact
	};
	$.ajax({
		url:ctx+"/captcha/isSendCaptcha",
		type:"post",
		timeout:180000,
		data:data,
		success:function(data){
			//可以发送验证码，弹出图片验证码框
			
			if(data.isOK == 2){
				if("e_regiter" == type){
					enterprise_chage();
					$('#enterprisePicCaptchaDiv').attr('style','');
				}else if("p_regiter" == type){
					p_chage();
					$('#personPicCaptchaDiv').attr('style','');
				}
			}else{
				if(data.message!=''){
					alert(data.message);
					$("#"+sendCaptchaButtonId).html("获取短信验证码").attr("disabled",false);
				}
			}
		}
	})
}


/*
 * 注册发送验证码工具类
 * captchaKeyId Redis中存储验证码KEY的输入框ID
 * sendCaptchaButtonId 发送验证码按钮ID
 * contactInputId 手机/邮箱输入框ID
 * ifRegisterCode 是否为注册：0为非注册，1为注册
 * contactTypeCode 通信方式：1邮箱，2手机
 * registerType 注册类型：0个人注册，1企业注册
 * */
function sendCaptcha(captchaKeyId,sendCaptchaButtonId,contactInputId,ifRegisterCode,contactTypeCode,registerType,picCaptchaType){
	//获取验证码
	var code = '';
	if(picCaptchaType == 'p_register'){
		code = $("#pic_captcha_id").val();
	}else if(picCaptchaType == 'e_register'){
		code = $("#enterprise_pic_captcha_id").val();
	}
	
	var mailRegex = /^[a-zA-Z0-9]+([._\\-]*[a-zA-Z0-9])*@([a-zA-Z0-9]+[-a-zA-Z0-9]*[a-zA-Z0-9]+.){1,63}[a-zA-Z0-9]+$/;
	var phoneRegex = /^[1]([3|5|7|8][0-9]{1}|59|58|88|89)[0-9]{8}$/;
	//手机/邮箱
	var contact = $("#"+contactInputId).val();
	//邮箱
	if(contactTypeCode == 1){
		if(!mailRegex.test(contact)){
			alert("邮箱格式不正确!");
			return false;
		}
		//个人注册
		if(registerType == 0){
			//邮箱唯一性验证
			var personalEmailPass = false;
			$.ajax({
				async: false,
	            url:ctx+"/information/register/verifyPersonalEmail",
	            type:"post",
	            data:{
	            	personalMailInput_div: $("#personalMailInput_div").val()
	            },
	            dataType: "json",
	            success:function(data){
	                if(!data){
	                	personalEmailPass = false;
	                }else{
	                	personalEmailPass = true;
	                }
	            }
	        });
			if(!personalEmailPass){
				alert("该邮箱已被注册，暂时无法获取验证码，请注册其他邮箱后，再获取验证码!");
				return false;
			}
		//企业注册
		} if(registerType == 1){
			//邮箱唯一性验证
			var enterpriseEmailPass = false;
			$.ajax({
				async: false,
	            url:ctx+"/information/register/verifyEnterpriseEmail",
	            type:"post",
	            data:{
	            	userEmail: $("#enterpriseMailInput").val()
	            },
	            dataType: "json",
	            success:function(data){
	                if(!data){
	                	enterpriseEmailPass = false;
	                }else{
	                	enterpriseEmailPass = true;
	                }
	            }
	        });
			if(!enterpriseEmailPass){
				alert("该邮箱已被注册，暂时无法获取验证码，请注册其他邮箱后，再获取验证码!");
				return false;
			}
		}
	//手机
	}/*else if(contactTypeCode == 2){
		if(!phoneRegex.test(contact)){
			alert("手机格式不正确!");
			return false;
		}
		//个人注册
		if(registerType == 0){
			//手机唯一性验证
			var personalPhonePass = false;
			$.ajax({
				async: false,
	            url:ctx+"/information/register/verifyPersonalMobile",
	            type:"post",
	            data:{
	                personalPhoneInput: $("#personalPhoneInput").val()
	            },
	            dataType: "json",
	            success:function(data){
	                if(!data){
	                	personalPhonePass = false;
	                }else{
	                	personalPhonePass = true;
	                }
	            }
	        });
			if(!personalPhonePass){
				alert("该手机已被注册，暂时无法获取验证码，请注册其他手机后，再获取验证码!");
				return false;
			}
		//企业注册
		}else if(registerType == 1){
			//手机唯一性验证
			var enterprisePhonePass = false;
			$.ajax({
				async: false,
	            url:ctx+"/information/register/verifyEnterpriseMobile",
	            type:"post",
	            data:{
	            	userMobile: $("#enterprisePhoneInput").val()
	            },
	            dataType: "json",
	            success:function(data){
	                if(!data){
	                	enterprisePhonePass = false;
	                }else{
	                	enterprisePhonePass = true;
	                }
	            }
	        });
			if(!enterprisePhonePass){
				alert("该手机已被注册，暂时无法获取验证码，请注册其他手机后，再获取验证码!");
				return false;
			}
		}
	}*/
    $("#"+sendCaptchaButtonId).attr("disabled", "disabled");
    $("#"+sendCaptchaButtonId).html("请稍等···");
	var ifRegister = null;
	if(ifRegisterCode == 0){
	}else if(ifRegisterCode == 1){
		if(contactTypeCode == 1){//邮箱注册
			ifRegister = 'EMAIL_REGISTER';
		}else if(contactTypeCode == 2){//手机注册
			ifRegister = 'REGISTER';
		}

	}
	var data = {
		type:contactTypeCode,
		contact:contact,
		smsType:ifRegister,
		code:code,
		picCaptchaType:picCaptchaType
	};
	$.ajax({
		url:ctx+"/captcha/send",
		type:"post",
		timeout:180000,
		data:data,
		success:function(data){
			if(data.isOK == 2){
				//取消图片验证码层
				$('#personPicCaptchaDiv').attr('style','display:none');
				$('#enterprisePicCaptchaDiv').attr('style','display:none');
				$("#"+captchaKeyId).val(data.codeKey);
				if(contactTypeCode == 1){//邮箱
					//个人注册邮箱倒计时
					if(registerType == 0){
						$("#"+sendCaptchaButtonId).html("重新发送(<span id='personal_mail_span_time'>180</span>)");
						$("#personalMailInput_div").attr("readonly","readonly");
						
					//企业注册邮箱倒计时
					}else if(registerType == 1){
						$("#"+sendCaptchaButtonId).html("重新发送(<span id='enterprise_mail_span_time'>180</span>)");
						$("#enterpriseMailInput").attr("readonly","readonly");
					}
				}else if(contactTypeCode == 2){//手机
					//个人注册手机倒计时
					if(registerType == 0){
						$("#"+sendCaptchaButtonId).html("重新发送(<span id='personal_phone_span_time'>180</span>)");
						$("#personalPhoneInput").attr("readonly","readonly");
					//企业注册手机倒计时
					}else if(registerType == 1){
						$("#"+sendCaptchaButtonId).html("重新发送(<span id='enterprise_phone_span_time'>180</span>)");
						$("#enterprisePhoneInput").attr("readonly","readonly");
					}
				}
				var time = 180;
				var timer;
				function sand_timer(){
					timer = window.setInterval(showalert, 1000);
				}
				function showalert(){
					time--;
					if(time > 0){
						if(contactTypeCode == 1){//邮箱
							//个人注册邮箱倒计时
							if(registerType == 0){
								$("#personal_mail_span_time").html(time);
							//企业注册邮箱倒计时
							}else if(registerType == 1){
								$("#enterprise_mail_span_time").html(time);
							}
						}else if(contactTypeCode == 2){//手机
							//个人注册手机倒计时
							if(registerType == 0){
								$("#personal_phone_span_time").html(time);
							//企业注册手机倒计时
							}else if(registerType == 1){
								$("#enterprise_phone_span_time").html(time);
							}
						}
					} else {
                        $("#"+sendCaptchaButtonId).removeAttr("readonly");
						if(contactTypeCode == 1){//邮箱
							$("#"+sendCaptchaButtonId).html("获取邮箱验证码").attr("disabled",false);
							$("#personalMailInput_div").removeAttr("readonly");
							$("#enterpriseMailInput").removeAttr("readonly");
						}else if(contactTypeCode == 2){//手机
							$("#"+sendCaptchaButtonId).html("获取手机验证码").attr("disabled",false);
							$("#personalPhoneInput").removeAttr("readonly");
							$("#enterprisePhoneInput").removeAttr("readonly");
						}
						window.clearInterval(timer);
						time = 180;
					}
				};
				sand_timer();
			} else {
				//发送失败则刷新一遍图片验证码
				p_chage();//个人注册
				enterprise_chage();//企业注册
				if(data.message!=''){
					alert(data.message);
					$("#"+sendCaptchaButtonId).removeAttr("readonly");
					if(contactTypeCode == 1){//邮箱
						$("#"+sendCaptchaButtonId).html("获取邮箱验证码").attr("disabled",false);
					}else if(contactTypeCode == 2){//短信
						$("#"+sendCaptchaButtonId).html("获取短信验证码").attr("disabled",false);
					}
				}else{
					$("#"+sendCaptchaButtonId).html("失败，重新发送");
				}
			}
		},
		error:function(){

		}
	});
};


//功能：同步AJAX校验验证码
//时间：2015年05月30日星期六18点27分
//作者：董其超
//参数：captchaInputId--验证码输入框ID
//参数: contactTypeCode--通信方式代码：邮箱1;手机2;新邮箱3;新手机4;由于页面要隐藏手机号或邮箱中间四位， 所以type = 1,2要使用从数据库中查询的来的手机号或密码，3,4表示更新或绑定的手机号或密码，需要用新的手机号或邮箱接收验证码
//参数: ifDiv--是否为弹出框：0为非弹出框，1为弹出框
function checkSynchronizedCaptcha(captchaKeyId,captchaInputId,contactTypeCode,ifDiv){

	if(contactTypeCode == 1 || contactTypeCode == 3){//邮箱
		if(ifDiv == 0){
			var mailCaptchaBoolean = false;
			var mailCaptcha = $("#"+captchaInputId).val();
			var mailCaptchaFormat = /^[a-zA-Z0-9]{6}$/;
			if(!mailCaptchaFormat.test(mailCaptcha)){
				alert("邮箱验证码不正确!");
			}else{
				var mailData = {
					codeKey: $("#"+captchaKeyId).val(),
					code:mailCaptcha
				};
				$.ajax({
					async: false,//同步请求以获取返回值（待优化）
					url: ctx+"/captcha/check",
					type: "post",
					data: mailData,
					success:function(data){
						if(data.message){
							mailCaptchaBoolean = true;
						} else {
							alert("邮箱验证码不正确!");
						}
					}
				});
			}
			return mailCaptchaBoolean;
		}else if(ifDiv == 1){
			var mailCaptchaBoolean_div = false;
			var mailCaptcha_div = $("#"+captchaInputId).val();
			var mailCaptchaFormat_div = /^[a-zA-Z0-9]{6}$/;
			if(!mailCaptchaFormat_div.test(mailCaptcha_div)){
				alert("邮箱验证码不正确!");
			}else{
				var mailData_div = {
					codeKey: $("#"+captchaKeyId).val(),
					code:mailCaptcha_div
				};
				$.ajax({
					async: false,//同步请求以获取返回值（待优化）
					url: ctx+"/captcha/check",
					type: "post",
					data: mailData_div,
					success:function(data){
						if(data.message){
							mailCaptchaBoolean_div = true;
						} else {
							alert("邮箱验证码不正确!");
						}
					}
				});
			}
			return mailCaptchaBoolean_div;
		}
	}else if(contactTypeCode == 2 || contactTypeCode == 4){//手机
		if(ifDiv == 0){
			var phoneCaptchaBoolean = false;
			var phoneCaptcha = $("#"+captchaInputId).val();
			var phoneCaptchaFormat = /^[a-zA-Z0-9]{6}$/;
			if(!phoneCaptchaFormat.test(phoneCaptcha)){
				alert("手机验证码不正确!");
			}else{
				var phoneData = {
					codeKey: $("#"+captchaKeyId).val(),
					code:phoneCaptcha
				};
				$.ajax({
					async: false,//同步请求以获取返回值（待优化）
					url: ctx+"/captcha/check",
					type: "post",
					data: phoneData,
					success:function(data){
						if(data.message){
							phoneCaptchaBoolean = true;
						} else {
							alert("手机验证码不正确!");
						}
					}
				});
			}
			return phoneCaptchaBoolean;
		}else if(ifDiv == 1){
			var phoneCaptchaBoolean_div = false;
			var phoneCaptcha_div = $("#"+captchaInputId).val();
			var phoneCaptchaFormat_div = /^[a-zA-Z0-9]{6}$/;
			if(!phoneCaptchaFormat_div.test(phoneCaptcha_div)){
				alert("手机验证码不正确!");
			}else{
				var phoneData_div = {
					codeKey: $("#"+captchaKeyId).val(),
					code:phoneCaptcha_div
				};
				$.ajax({
					async: false,//同步请求以获取返回值（待优化）
					url: ctx+"/captcha/check",
					type: "post",
					data: phoneData_div,
					success:function(data){
						if(data.message){
							phoneCaptchaBoolean_div = true;
						} else {
							alert("手机验证码不正确!");
						}
					}
				});
			}
			return phoneCaptchaBoolean_div;
		}
	}
};



