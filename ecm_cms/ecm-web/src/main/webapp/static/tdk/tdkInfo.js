/**
 * 
 *TDK类，其中包括TDK页面中的所有方法 
 *
 * @author  王东晓
 * 
 * Created on 2015年5月12日
 * 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
var TDK = {};

/**
 * 添加TDK信息
 */
TDK.addTDK = function(){
	var url =ctx +"/tdk/addTDK";
	this.ajaxTDK(url);
	
};
/**
 * 更新TDK信息
 */
TDK.modifyTDK = function(){
	var url = ctx+"/tdk/modifyTDK";
	this.ajaxTDK(url);
};
/**
 * TDK信息更新
 */
TDK.ajaxTDK = function(url){
	$.ajax({
		url : url,
		type : "post",
		data :$("#tdkForm").serialize(),// 你的formid
		dataType : "json",
		success : function(obj) {
			$.jBox.success(obj.resultMsg,'提示',{closed:function(){
				location.reload();
			}});
		},
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			$.jBox.info("系统错误！请稍后再试！");
		}
	});
};