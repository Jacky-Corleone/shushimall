/*---------------------------------------------------------------------Trade平台常用基本脚本函数（可扩展）-------------------------------------------------------------------------*/


/*预加载图片*/
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

/*选项卡切换*/
function Set_Tab(tab_name,tab_no,tab_all,class_hove,class_out)
{
    var i;
    for(i=1;i<=tab_all;i++)
    {
        if(i==tab_no)
        {
            $("#"+tab_name+"_"+i).attr("class","hover");
            $("#"+tab_name+"_"+i+"_content").show();
        }
        else
        {
            $("#"+tab_name+"_"+i).attr("class","");
            $("#"+tab_name+"_"+i+"_content").hide();
        }
    }
}

/**
 * 省市区数据填充
 * parentCode 父级编码
 * opitonId 需要填充数据的下拉列表框的id
 * selectVal 默认选中的值
 * name 服务器返回的数据中，作为下拉框option的text的字段名，默认为"name"
 * code 服务器返回的数据中，作为下拉框option的value的字段名，默认为"code"
 */
function optionItems(url, parentCode, optionId, selectVal, name, code){
	$.ajax({
		url:url,
		type:"post",
		data:{
			parentCode:parentCode
		},
		dataType: "json",
		success:function(data){
			var optionItem = $("#"+optionId);
			optionItem.empty();
			optionItem.append($("<option>").text("请选择").val(""));
			$.each(data, function (n, address) {
				if(!name){
					name = "name"; 
				}
				if(!code){
					code = "code";
				}
				var option = $("<option>").text(address[name]).val(address[code]);
				if(address.code == selectVal){
					option.attr("selected",true);
				}
				optionItem.append(option);
			});
		}
	});
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
