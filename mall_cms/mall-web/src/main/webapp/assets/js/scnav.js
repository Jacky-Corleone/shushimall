
$(document).ready(function(){
	$('#leftBar').css("top",$(window).height()/2);
	$('#leftBar').hide();
	$(window).scroll( function(){
		var scrollTop = $(document).scrollTop();
		var windowHeight = $(window).height();
		var floorsHeight = $("#floors").height();
		if( scrollTop > 200 && scrollTop < floorsHeight + 20000 ){
			$('#leftBar').show();
			$("#floors").find("div[name^='floor']").each(function(i,obj){
				var floorTop = $(obj).offset().top;
				var floorHeight = $(obj).height();
				var tmp = floorTop - windowHeight;
				if( scrollTop >= tmp + 360 ){
					$("#leftBar").find("div").removeClass("current");
					$("#leftBar").find("[code='" + ( i + 1 ) + "']").parent().addClass("current");
				}
			});
		}else{
			$('#leftBar').hide();
		}
	});
	
	$("#leftBar").find("a").click(function(){
		var code = $(this).attr("code");
		var windowHeight = $(window).height();
		
		var menu = $("[name='f"+code+"']");
		menu.parent().addClass("current");
		var floorTop = menu.offset().top;
		$("html,body").animate({scrollTop: floorTop-windowHeight+560},100);	
	});
});
