
//功能：发送验证码
//时间：2015年05月29日星期五19点04分
//作者：董其超
//参数：sendCaptchaButtonId--发送手机短信验证码按钮ID
//参数：contactInputId--手机号输入框ID
//参数: ifRegisterCode--是否为注册：0为非注册，1为注册
//参数: contactTypeCode--通信方式代码：邮箱1;手机2;新邮箱3;新手机4;由于页面要隐藏手机号或邮箱中间四位， 所以type = 1,2要使用从数据库中查询的来的手机号或密码，3,4表示更新或绑定的手机号或密码，需要用新的手机号或邮箱接收验证码
//参数: ifDiv--是否为弹出框：0为非弹出框，1为弹出框
function sendCaptcha(captchaKeyId,sendCaptchaButtonId,contactInputId,ifRegisterCode,contactTypeCode,ifDiv){
	var mailRegex = /^[a-zA-Z0-9]+([._\\-]*[a-zA-Z0-9])*@([a-zA-Z0-9]+[-a-zA-Z0-9]*[a-zA-Z0-9]+.){1,63}[a-zA-Z0-9]+$/;
	var phoneRegex = /^[1]([3|5|7|8][0-9]{1}|59|58|88|89)[0-9]{8}$/;
	var contact = $("#"+contactInputId).val();
	if(contactTypeCode == 1 || contactTypeCode == 3){
		if(!mailRegex.test(contact)){
			toast("请输入正确的邮箱!");
			return false;
		}
	}else if(contactTypeCode == 2 || contactTypeCode == 4){
		if(!phoneRegex.test(contact)){
			toast("请输入正确的手机号!");
			return false;
		}
	}
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
	}else if(ifRegisterCode == 2){
		ifRegister = 'UPLOGINPWD';
	}
	var data = {
		type:contactTypeCode,
		contact:contact,
		smsType:ifRegister
	};
	$.ajax({
		url:ctx+"/captcha/send",
		type:"post",
		timeout:180000,
		data:data,
		success:function(datas){
			var data=JSON.parse(datas);
			if(data.isOK == 2){
				$("#"+captchaKeyId).val(data.codeKey);
				if(contactTypeCode == 1 || contactTypeCode == 3){//邮箱
					$("#"+sendCaptchaButtonId).html("重新发送(<span id='mail_span_time'>180</span>)");
				}else if(contactTypeCode == 2 || contactTypeCode == 4){//手机
					if(ifDiv == 0){
						$("#"+sendCaptchaButtonId).html("重新发送(<span id='phone_span_time'>180</span>)");
					}else if(ifDiv == 1){
						$("#"+sendCaptchaButtonId).html("重新发送(<span id='phone_span_time_div'>180</span>)");
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
						if(contactTypeCode == 1 || contactTypeCode == 3){//邮箱
							$("#mail_span_time").html(time);
						}else if(contactTypeCode == 2 || contactTypeCode == 4){//手机
							if(ifDiv == 0){
								$("#phone_span_time").html(time);
							}else if(ifDiv == 1){
								$("#phone_span_time_div").html(time);
							}
						}
					} else {
                        $("#"+sendCaptchaButtonId).removeAttr("disabled");
						if(contactTypeCode == 1 || contactTypeCode == 3){//邮箱
							$("#"+sendCaptchaButtonId).html("获取邮箱验证码");
						}else if(contactTypeCode == 2 || contactTypeCode == 4){//手机
							$("#"+sendCaptchaButtonId).html("获取手机验证码");
						}
						window.clearInterval(timer);
						time = 180;
					}
				};
				sand_timer();
			} else {
				$("#"+sendCaptchaButtonId).html("重新发送");
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
				toast("邮箱验证码不正确!");
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
							toast("邮箱验证码不正确!");
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
				toast("邮箱验证码不正确!");
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
							toast("邮箱验证码不正确!");
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
				toast("手机验证码不正确!",null);
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
							toast("手机验证码不正确!",null);
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
				toast("手机验证码不正确!",null);
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
							toast("手机验证码不正确!",null);
						}
					}
				});
			}
			return phoneCaptchaBoolean_div;
		}
	}
};



