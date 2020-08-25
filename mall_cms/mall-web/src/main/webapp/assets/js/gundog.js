;( function(window,$){
	$(function(){
		var wid = $('.list li:first').outerWidth(true),
		    step = 1,
		    stepWidth = wid * step,
		    timer;
		$('.bt-r').bind('click',function(){ play(true) });
		$('.bt-l').bind('click',function(){ play(false) });
		function play( flag ){
			if(!$('.list ul').is(':animated')){
				var dir = flag?-1:1,
			    objli;
				if( flag ){  
	                objli= $('.list li').slice(0,step);
	                objli.clone().appendTo($('.list ul'));

				}else{
					objli = $('.list li').slice(-step);
					objli.clone().prependTo($('.list ul'));
					$('.list ul').css({marginLeft:-stepWidth});
				}
				$('.list ul').animate({marginLeft:'+='+stepWidth*dir},500,function(){
					if(flag){
						$('.list ul').css({marginLeft:0});
					}
					objli.remove();
				})
			}
		}
	});
})(window,jQuery);