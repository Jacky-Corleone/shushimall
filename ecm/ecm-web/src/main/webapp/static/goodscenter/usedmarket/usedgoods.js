/**
 *
 *二手市场商品审核类，其中包括页面中的所有方法
 *
 * @author  王东晓
 *
 * Created on 2015年5月9日
 *
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

var UsedGoods = {};
/**
 * 当singleId为0时，表示页面选中多个列表，不为0时表示选中的列表商品ID
 */
UsedGoods.singleId=0;

/**
 * 平台分类查询方法
 */
UsedGoods.loadsub =function(flag){
        var html;
        var url;
        switch (flag){
        case '1':
            var pid = $("#platformId1").val();
            if(pid==null||pid<=0){
            	pid= '${goods.platformId1}';
            }
            html = "<option value=''>二级分类</option>";
            url = ctx+"/brand/getChildCategory?pCid="+pid;
            $("#platformId2").html("<option value=''>二级分类</option>");
        	$("#platformId2").select2("val","");
        	$("#cid").html("<option value=''>三级分类</option>");
        	$("#cid").select2("val","");
            break;
        case '2':
            var pid = $("#platformId2").val();
            if(pid==null||pid<=0){
            	pid = '${goods.platformId2}';
            }
            html = "<option value=''>三级分类</option>";
            url = ctx+"/brand/getChildCategory?pCid="+pid;
            $("#cid").html("<option value=''>三级分类</option>");
        	$("#cid").select2("val","");
            break;
    	}
        $.ajax({
            url:url,
            type:"post",
            dataType:'json',
            success:function(data){
                $(data).each(function(i,item){
                    if('1'==flag){
                        html += "<option ";
                    	if(item.categoryCid=='${goods.platformId2}'){
                        	html +=" selected";
                        }
                    	html +=" value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                    }else if('2'==flag){
                        html += "<option ";
                        if(item.categoryCid=='${goods.cid}'){
                        	html +=" selected ";
                        }
                        html +="value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                    }
                });
                switch (flag){
                    //加载第二级
                    case '1':
                        $("#platformId2").html(html);
                        break;
                    //加载第三级
                    case '2':
                        $("#cid").html(html);
                        break;
                }
            }
        });

    };
UsedGoods.checkCategory = function(){
		if($('#platformId1 option:selected').val()){
			if(!$('#platformId2 option:selected').val()){
                $.jBox.info("请选择二级目录");
				return false;
			}else if(!$('#cid option:selected').val()){
                $.jBox.info("请选择三级目录");
				return false;
			}
		}
		return true;
}
UsedGoods.pagesub =	function (n,s){
		//if(this.checkCategory()){
			if(n>0){

			}else{
				n =1;
			}
			if(s>0){

			}else{
				s =10;
			}
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
		//}
}
UsedGoods.itemCheck =	function(value){
		var flag = true;
		if($("input[name='checkAll']:checked").size()==0){
			$.jBox.info("请择数据后进行操作！");
	  	 return;
		}
		$("input[name='checkAll']:checked").each(function(){
			if($(this).attr("status")!=value){
				flag = false;
			}
		});
		if(!flag){
			if(value == "2"){
				$.jBox.info("请保证选择的商品的状态全是待审核状态!");
			}else if(value=="3"){
			$.jBox.info("请保证选择的商品的状态全是审核通过状态!");
		}else if(value=="20"){
			$.jBox.info("请保证选择的商品的状态全是审核驳回状态!");
		}
		}
		return flag;
	}



UsedGoods.selAll = document.getElementById("selAll");
UsedGoods.selectAll = function() {
	var obj = document.getElementsByName("checkAll");
	if (document.getElementById("selAll").checked == false) {
		for ( var i = 0; i < obj.length; i++) {
			obj[i].checked = false;
		}
	} else {
		for ( var i = 0; i < obj.length; i++) {
			obj[i].checked = true;
		}
	}

}
//当选中所有的时候，全选按钮会勾上
UsedGoods.setSelectAll = function() {
	var obj = document.getElementsByName("checkAll");
	var count = obj.length;
	var selectCount = 0;

	for ( var i = 0; i < count; i++) {
		if (obj[i].checked == true) {
			selectCount++;
		}
	}
	if (count == selectCount) {
		document.all.selAll.checked = true;
	} else {
		document.all.selAll.checked = false;
	}
}
/**
 * 二手市场商品审核页面，审核通过按钮触发的功能
 */
UsedGoods.passItem = function(id){
	if(id>0){//单条数据操作
		$.jBox.confirm("确定要审核通过？","提示",function(value){
  			if(value=='ok'){
  				var data = "checkAll="+id;
  				var url = ctx+"/usedGoods/passAll";
  				UsedGoods.ajaxOperate(url,data);
  			}
  		});
	}else{//批量操作
		if(this.itemCheck("2")){//判断是否选中记录，判断所选记录是否全是待审核状态
			$.jBox.confirm("确定要全部审核通过？","提示",function(value){
	  			if(value=='ok'){
	  				var data = $("#soldOutAll").serialize();
	  				var url = ctx+"/usedGoods/passAll";
	  				UsedGoods.ajaxOperate(url,data);
	  			}
	  		});
		}
	}
};
/**
 * 二手市场商品审核页面，审核驳回按钮触发的功能
 * @param id
 */
UsedGoods.rejectItem = function(id){
	if(id==0){
		if(!this.itemCheck("2")){//判断是否选中记录，判断所选记录是否全是待审核状态
			return;
		}
	}
	UsedGoods.singleId = id;
	$("#rejectDiv").modal("show");
};

/**
 * 填写驳回信息，提交驳回信息
 */
UsedGoods.savereject = function(){
	//驳回原因验证必填
	if($('#rejectContext').val().trim() != ''){
		//单条数据操作
		if(this.singleId!=0){
			$.jBox.confirm("确定要审核驳回？","提示",function(value){
				if(value=='ok'){
					var data = "checkAll="+UsedGoods.singleId+"&comment="+$("#rejectContext").val();
					var url = ctx+"/usedGoods/rejectAll";
					UsedGoods.ajaxOperate(url,data);
				}
			});
		}else{
			//批量操作
			$.jBox.confirm("确定要全部审核驳回？","提示",function(value){
				if(value=='ok'){
					var data = $("#soldOutAll").serialize()+"&comment="+$("#rejectContext").val();
					var url = ctx+"/usedGoods/rejectAll";
					UsedGoods.ajaxOperate(url,data);
				}
			});
		}
	}else{
		alert("请填写驳回理由！");
		return;
	}

};
/**
 * 删除二手商品信息
 * @param id
 */
UsedGoods.removeItem = function(id){

	if(id>0){//单条数据操作
		$.jBox.confirm("确定要删除商品记录？","提示",function(value){
  			if(value=='ok'){
  				var data = "checkAll="+id;
  				var url = ctx+"/usedGoods/removeAll";
  				UsedGoods.ajaxOperate(url,data);
  			}
  		});
	}else{//批量操作
		var flag = true;
  		if($("input[name='checkAll']:checked").size()==0){
  			$.jBox.info("请择数据后进行操作！");
		  	 return;
  		}
  		$("input[name='checkAll']:checked").each(function(){
  			if($(this).attr("status")=='2'){
  				flag = false;
  				$.jBox.info("请保证选择的商品的状态全不是待审核状态!");
  				return;
  			}
  		});
		if(flag){//判断是否选中记录，判断所选记录是否全是审核通过或者审核驳回状态
			$.jBox.confirm("确定要全部删除？","提示",function(value){
	  			if(value=='ok'){
	  				var data = $("#soldOutAll").serialize();// 你的formid
	  				var url = ctx+"/usedGoods/removeAll";
	  				UsedGoods.ajaxOperate(url,data);
	  			}
	  		});
		}
	}
};

/**
 * 二手商品什么页面ajax交互方法
 */
UsedGoods.ajaxOperate = function(url,data){
	$.ajax({
		url : url,
		type : "POST",
		data :data,// 你的formid
		dataType : "json",
		success : function(obj) {
			if(obj.success==true){
				$.jBox.info("操作成功!");
				$("#searchForm").submit();
			}else{
				$.jBox.info("后台查询数据失败！");
			}
		},
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			$.jBox.info("系统错误！请稍后再试！");
		}
	});

};
/*$(document).ready(function() {
	$("#treeTable").tableSorter();
});*/