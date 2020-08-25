// JavaScript Document
$(document).ready(function(){
	$(".news_title").click(function(){	
		$(".news_title").removeClass("highlight")
		$(".news_title01").addClass("highlight")	
			$(".news_content").show().end()			//将子节点的a元素显示出来并重新定位到上次操作的元素ht类
			$(".news_content01").hide().end()
	});
	$(".news_title01").click(function(){
		$(".news_title01").removeClass("highlight")
		$(".news_title").addClass("highlight")		
			$(".news_content01").show().end()			//将子节点的a元素显示出来并重新定位到上次操作的元素ht类
			$(".news_content").hide().end()
	});
});

$(document).ready(function(){
  	var currentIndex = 1;
	$(".pTitle").mouseover(function(){
	 var w=this.id.replace("pTitle","");		
	 var v_width = $(".product").width() ;
	 var page_count = 200;
	  	 if( !$(".product").is(":animated") ){    //判断"视频内容展示区域"是否正在处于动画
			 if(currentIndex < w)
			 {
				 var length = page_count*(w-currentIndex);
				 $(".listArrow").animate({ left : '+=' + length}, "fast"); 
			 } else 
			 {
				$(".listArrow").animate({ left : '-='+page_count*(currentIndex-w) }, "fast");
				
			 } 
			 currentIndex = w;
		 }
	 $(this).addClass("p_hightlight");
	 $(this).siblings(".pTitle").removeClass("p_hightlight ");
	  $(".pList").each(function(){  
	 	if($(this).attr("nex")==w)
		{
			$(this).show();
		    $(this).siblings(".pList").hide();
		}
		
	  });
	 });	 
});

$(document).ready(function(){
  	var currentIndex = 1;
	$(".cTitle01").mouseover(function(){
	 var w=this.id.replace("cTitle","");		
	 var v_width = $(".c_product").width() ;
	 var page_count = 158;
	  	 if( !$(".c_product").is(":animated") ){    //判断"视频内容展示区域"是否正在处于动画
			 if(currentIndex < w)
			 {
				 var length = page_count*(w-currentIndex);
				 $(".c_listArrow01").animate({ left : '+=' + length}, "fast"); 
			 } else 
			 {
				$(".c_listArrow01").animate({ left : '-='+page_count*(currentIndex-w) }, "fast");
				
			 } 
			 currentIndex = w;
		 }
	 $(this).addClass("p_hightlight");
	 $(this).siblings(".cTitle01").removeClass("p_hightlight ");
	  $(".centerList01").each(function(){  
	 	if($(this).attr("nex")==w)
		{
			$(this).show();
		    $(this).siblings(".centerList01").hide();
		}
		
	  });
	 });	 
});
$(document).ready(function(){
  	var currentIndex = 1;
	$(".cTitle02").mouseover(function(){
	 var w=this.id.replace("cTitlef","");		
	 var v_width = $(".c_product").width() ;
	 var page_count = 158;
	  	 if( !$(".c_product").is(":animated") ){    //判断"视频内容展示区域"是否正在处于动画
			 if(currentIndex < w)
			 {
				 var length = page_count*(w-currentIndex);
				 $(".c_listArrow02").animate({ left : '+=' + length}, "fast"); 
			 } else 
			 {
				$(".c_listArrow02").animate({ left : '-='+page_count*(currentIndex-w) }, "fast");
				
			 } 
			 currentIndex = w;
		 }
	 $(this).addClass("p_hightlight");
	 $(this).siblings(".cTitle02").removeClass("p_hightlight ");
	  $(".centerList02").each(function(){  
	 	if($(this).attr("nex")==w)
		{
			$(this).show();
		    $(this).siblings(".centerList02").hide();
		}
		
	  });
	 });	 
});

$(document).ready(function(){
  	var currentIndex = 1;
	$(".cTitle03").mouseover(function(){
	 var w=this.id.replace("cTitlet","");		
	 var v_width = $(".c_product").width() ;
	 var page_count = 158;
	  	 if( !$(".c_product").is(":animated") ){    //判断"视频内容展示区域"是否正在处于动画
			 if(currentIndex < w)
			 {
				 var length = page_count*(w-currentIndex);
				 $(".c_listArrow03").animate({ left : '+=' + length}, "fast"); 
			 } else 
			 {
				$(".c_listArrow03").animate({ left : '-='+page_count*(currentIndex-w) }, "fast");
				
			 } 
			 currentIndex = w;
		 }
	 $(this).addClass("p_hightlight");
	 $(this).siblings(".cTitle03").removeClass("p_hightlight ");
	  $(".centerList03").each(function(){  
	 	if($(this).attr("nex")==w)
		{
			$(this).show();
		    $(this).siblings(".centerList03").hide();
		}
		
	  });
	 });	 
});