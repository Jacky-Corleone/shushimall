(function($){
	//密码强度检查
	$.fn.checkPwdStrong = function(level_1, level_2, level_3)
	{
		//如果没有传入参数，则使用默认参数
		if(!level_1)
		{
			level_1 = 'pwd_level_1';
		}
		if(!level_2)
		{
			level_2 = 'pwd_level_2';
		}
		if(!level_3)
		{
			level_3 = 'pwd_level_3';
		}
		var ele = $(this);
		var val = ele.val();
		var level = $('#level_id');
		if(val.length < 6)
		{
			$('#' + level_1).css('display', 'none');
			$('#' + level_2).css('display', 'none');
			$('#' + level_3).css('display', 'none');
		}else
		{
			var reg = /[.,_ !@#$^%&*?`~():"<>\-=_+;',|\/{}\[\]\\]+/;
			if(level)
			{
				level.val('1');
			}
			//弱
			$('#' + level_1).css('display', 'block');
			if((/[0-9]+/.test(val) && /[a-zA-Z]+/.test(val)) || (/[a-zA-Z]+/.test(val) && reg.test(val)) || (reg.test(val) && /[0-9]+/.test(val)))
			{
				//中
				$('#' + level_2).css('display', 'block');
				if(level)
				{
					level.val('2');
				}
				if(/[a-zA-Z]+/.test(val) && /[0-9]+/.test(val) && reg.test(val))
				{
					////强
					$('#' + level_3).css('display', 'block');
					if(level)
					{
						level.val('3');
					}
				}
				else
				{
					$('#' + level_3).css('display', 'none');
				}
			}else
			{
				$('#' + level_2).css('display', 'none');
				$('#' + level_3).css('display', 'none');
			}
		}
		
	}
	
	//keydown
	$.fn.checkPwdIn = function()
	{
		$("#loginpwd_span").html("6-20位字符，建议由字母数字和符号组合");
		$("#loginpwd_span").css("color", "#dedede");
		$("#icon_pwd1_hint").hide();
	}
	
	//keyup
	$.fn.checkPwdOut = function(pwd_id, pwd_sp, pwd_hint)
	{
		if(!pwd_id)
		{
			pwd_id = "loginpwd_id";
		}
		if(!pwd_sp)
		{
			pwd_sp = "mag";
		}
		if(!pwd_hint)
		{
			pwd_hint = "icon_pwd1_hint";	
		}
		var pwd = $("#" + pwd_id).val();
		if(pwd.length == 0){
			$("#" + pwd_sp).html("请输入新密码");
			return false;
		}
		if(pwd.length < 6){
			$("#" + pwd_sp).html("密码不得少于六位");
			return false;
		} else {
			var pwdFormat = /^(\d+[a-zA-Z.,_ !@#$^%&*?`~():"<>\-=_+;',|\/{}\[\]\\]+)|([a-zA-Z]+[0-9.,_ !@#$^%&*?`~():"<>\-=_+;',|\/{}\[\]\\]+)|([.,_ !@#$^%&*?`~():"<>\-=_+;',|\/{}\[\]\\]+[0-9a-zA-Z]+)$/;
			var chineseFormat = /^[\u4e00-\u9fa5]+$/;
			if(!pwdFormat.test(pwd)){
				$("#" + pwd_sp).html("至少由数字、字母、符号两个以上组合");
				return false
			} else {
				$("#" + pwd_sp).html("");
				return true;
			}
		}
	}
	
	$.fn.reNewPwdfocusOn = function()
	{
		$("#pwd_span").html("");
		$("#icon_pwd_hint").removeClass("fa-exclamation-circle font_fe").addClass("fa-check-circle font_7a").hide();
	}
	
	//重复密码检查
	$.fn.checkPwdToo = function()
	{
		var pwdToo = $("#pwd_id").val();
		if(pwdToo.length == 0){
			$("#mag").html("请输入确认密码");
			$("#mag").css("color","red");
			$("#icon_pwd_hint").removeClass("fa-check-circle font_7a").addClass("fa-exclamation-circle font_fe").show();
			return false;
		}
		var pwd = $("#loginpwd_id").val();
		if(pwdToo != pwd){
			$("#mag").html("两次密码不同，请重新输入");
			$("#mag").css("color","red");
			$("#icon_pwd_hint").removeClass("fa-check-circle font_7a").addClass("fa-exclamation-circle font_fe").show();
			return false;
		} else {
			$("#pwd_span").html("");
			$("#icon_pwd_hint").removeClass("fa-exclamation-circle font_fe").addClass("fa-check-circle font_7a").show();
			return true;
		}
	}
	
})(jQuery);