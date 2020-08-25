var SHOPITEMLIST = {};

/**
 * 加载商品列表
 */
SHOPITEMLIST.loadItemList = function(orderSort){
	var form = $("#contentForm").get(0);
	$("#orderSort").val(orderSort);
	form.submit();
};
