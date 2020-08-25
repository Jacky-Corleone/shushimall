//检查密码强度
//CharMode函数
//测试某个字符是属于哪一类.  
function CharMode(iN) {
    if (iN >= 48 && iN <= 57) //数字  
        return 1;
    if ((iN >= 65 && iN <= 90) || (iN >= 97 && iN <= 122)) //大写小写  字母  
        return 2;
  //  if (iN >= 97 && iN <= 122) //
   //     return 4;
    else
        return 8; //特殊字符  
}
//bitTotal函数  `
//计算出当前密码当中一共有多少种模式  
function bitTotal(num) {
    var modes = 0;
    for (i = 0; i < 4; i++) {
        if (num & 1) {modes++;}
        num >>>= 1;
    }
    return modes;
}
//checkStrong函数  
//返回密码的强度级别  
function checkStrong(sPW) {
	var pwdStr = 0
	var Modes = 0;
    if (sPW.length < 6){
//    	$("#loginpwd_span").html("密码不得少于六位，请重新输入");
//		$("#loginpwd_span").css("color","red");
        return 0; //密码太短  
    } else {
    	for (i = 0; i < sPW.length; i++) {
        //测试每一个字符的类别并统计一共有多少种模式.  
        	Modes |= CharMode(sPW.charCodeAt(i));
    	}
    	pwdStr = bitTotal(Modes);
    	if(pwdStr > 1)
    	{
	    	$("#loginpwd_span").html("");
	//		$("#loginpwd_span").css("color","#747474");
			$("#icon_pwd1_hint").removeClass("fa-exclamation-circle font_fe").addClass("fa-check-circle font_7a").show();
		}else
		{
			$("#loginpwd_span").html("至少由数字、字母、符号两个以上组合");
			$("#loginpwd_span").css("color","red");
			$("#icon_pwd1_hint").removeClass("fa-check-circle font_7a").addClass("fa-exclamation-circle font_fe").show();
		}
    }
    
    return pwdStr;
}
//pwStrength函数  
//当用户放开键盘或密码输入框失去焦点时,根据不同的级别显示不同的颜色  
function pwStrength(pwd) {
    if (pwd == null || pwd == '') {
    	$("#loginpwd_span").html("请输入密码");
		$("#loginpwd_span").css("color","red");
		$("#icon_pwd1_hint").removeClass("fa-check-circle font_7a").addClass("fa-exclamation-circle font_fe").show();
    }
    else {
        var S_level = checkStrong(pwd);
        switch (S_level) {
            case 0:
            //	$("#pwd_level_1").removeClass("bg_11").addClass("bg_09");
            	$("#pwd_level_1").hide();
	        // 	$("#pwd_level_2").removeClass("bg_11").addClass("bg_09");
	        	$("#pwd_level_2").hide();
	        // 	$("#pwd_level_3").removeClass("bg_11").addClass("bg_09");
	        	$("#pwd_level_3").hide();
                break;
            case 1:
            //	$("#pwd_level_1").removeClass("bg_09").addClass("bg_11");
            	$("#pwd_level_1").show();
            //	$("#pwd_level_2").removeClass("bg_11").addClass("bg_09");
           		$("#pwd_level_2").hide();
            //	$("#pwd_level_3").removeClass("bg_11").addClass("bg_09");
            	$("#pwd_level_3").hide();
            	$("#level_id").val("1");
                break;
            case 2:
            //	$("#pwd_level_1").removeClass("bg_09").addClass("bg_11");
            	$("#pwd_level_1").show();
            //	$("#pwd_level_2").removeClass("bg_09").addClass("bg_11");
            	$("#pwd_level_2").show();
            //	$("#pwd_level_3").removeClass("bg_11").addClass("bg_09");
            	$("#pwd_level_3").hide();
            	$("#level_id").val("2");
                break;
            default:
            //	$("#pwd_level_1").removeClass("bg_09").addClass("bg_11");
            	$("#pwd_level_1").show();
	        //	$("#pwd_level_2").removeClass("bg_09").addClass("bg_11");
	        	$("#pwd_level_2").show();
	        //	$("#pwd_level_3").removeClass("bg_09").addClass("bg_11");
	        	$("#pwd_level_3").show();
            	$("#level_id").val("3");
        }
    }
    return;
}

function pwdStrength(pwd) {
	if(pwd.length < 6){
		$("#loginpwd_span").html("密码不得少于六位");
		$("#loginpwd_span").css("color","red");
		$("#icon_pwd1_hint").removeClass("fa-check-circle font_7a").addClass("fa-exclamation-circle font_fe").show();
	} else {
		$("#loginpwd_span").html("");
//		$("#loginpwd_span").css("color","#747474");
		$("#icon_pwd1_hint").removeClass("fa-exclamation-circle font_fe").addClass("fa-check-circle font_7a").show();
		pwStrength(pwd);
	}
}

function pwdFormat(pwd){
	if(pwd.length < 6){
		return false;
	}
	return true;
}