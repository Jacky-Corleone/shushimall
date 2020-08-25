// JavaScript Document
 $(document).ready(function() {
  $('.mc_classify_list').mouseover(function(){
	 $(this).addClass("mc_classify_list01 ");
	 var v = this.id.replace("classify", "");
	 $(".mc_classify_prompt").each(function(){
	 	if($(this).attr("pref")==v)$(this).show();
	 });
	 $(".mc_classify_list01 b").hide();
	  });
  $('.mc_classify_list').mouseout(function(){ 
	 $(this).removeClass("mc_classify_list01")
	 $(".mc_classify_prompt").hide();
	 $(".mc_classify_list b").show();
	  });
	  	  
  $('.mc_classify_prompt').mouseover(function(){
	 $(this).show();
	 $("#classify"+$(this).attr("pref")).addClass("mc_classify_list01"); 
	  });
  $('.mc_classify_prompt').mouseout(function(){ 
	 $(".mc_classify_list").removeClass("mc_classify_list01")
	 $(".mc_classify_prompt").hide();
	 
	 $(".mc_classify_list01 b").show();
	  });	  
	  
  $(".mc_prompt_close").click(function(){
	 $(".mc_classify_prompt").hide();
	  });
	  
 });
