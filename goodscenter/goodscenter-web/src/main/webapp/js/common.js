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

