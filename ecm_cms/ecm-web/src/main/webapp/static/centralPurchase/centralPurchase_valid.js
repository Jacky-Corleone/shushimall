$(function(){
	$("#centralPurchasing").validate({
        rules: {
        	activityName:{
        		required:true,
        		maxlength:20
        	},
        	activitySignUpTime:{
        		required:true,
        	},
        	activitySignUpEndTime:{
        		required:true,
        	},
        	activityBeginTime:{
        		required:true,
        	},
        	activityEndTime:{
        		required:true,
        	},
        	activityImg:"required",
        	skuId:"skuValid",
        	perPerchaseMaxNum:{
        		required:true,
        		number:true
        	},
        	releaseGoodsMaxNum:{
        		required:true,
        		number:true
        	},
        	estimatePrice:"estimatePrice"
        },
        messages : {
        	activityName:{
        		required:"活动名称不可为空",
        		maxlength:"活动名称不大于{0}字符"
        	}
        }
	});
});
//输入数字显示
function numInput(obj,length){
	if(obj.value==obj.value2)
		return;
	if(length == 0 && obj.value.search(/^\d*$/)==-1)
		obj.value=(obj.value2)?obj.value2:'';
	else if(obj.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)
		obj.value=(obj.value2)?obj.value2:'';
	else obj.value2=obj.value;
}

jQuery.validator.addMethod("estimatePrice", function(value, element, param) {
    var type = $("input:radio:checked[name='chooseEstimatePriceType']").prop("id");
    if(type=="radio0"){
    	var flag = $("#estimatePrice1").val();
    	if(flag){
    		$("#estimatePrice").val($("#estimatePrice1").val());
    		return true;
    	}else{
    		return false;
    	}
    }else{
    	var flag1 =  $("#estimatePrice1").val();
    	var flag2 =  $("#estimatePrice2").val();
    	if(flag1&&flag2&&parseFloat(flag2)>parseFloat(flag1)){
    		$("#estimatePrice").val($("#estimatePrice1").val()+"-"+$("#estimatePrice2").val());
    		return true;
    	}else{
    		return false;
    	}
    }
}, $.validator.format("未输入预估价或预估价不合法"));
jQuery.validator.addMethod("skuValid", function(value, element, param) {
	var $skuId = $("#skuId").val();
	var $itemId = $("#itemId").val();
	if($skuId && $itemId){
		return true;
	} else {
		return false;
	}
}, $.validator.format("请选择商品"));

