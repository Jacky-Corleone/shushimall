/**
 * 保存按钮禁用和启用插件，用于点击保存按钮时加载提示文字和禁用按钮，保存完毕后，隐藏提示文字启用按钮
 * 使用方法：<button type="button" loadMsg="正在保存...">保存</button>
 * */
(function($){
	//加载提示信息
	$.fn.loadingMsg = function(){
		var ele = $(this);
		var flag = ele.text();
		var origVal = "";//button原来的文本或者input原来的value
		var loadMsg = ele.attr("loadMsg");//提示信息
		if(flag){//button
			origVal = ele.text();
			ele.text(loadMsg);
			ele.hideMsg.flag = "button"; 
		}else{//input
			origVal = ele.val();
			ele.val(loadMsg);
			ele.hideMsg.flag = "input"; 
		}
		ele.attr("disabled", "disabled");
		ele.hideMsg.origVal = origVal;
	}
	//隐藏提示信息
	$.fn.hideMsg = function(){
		var ele = $(this);
		ele.removeAttr("disabled");
		if(ele.hideMsg.flag == "button"){
			ele.text(ele.hideMsg.origVal);
		}else{
			ele.val(ele.hideMsg.origVal);
		}
	}
})(jQuery);