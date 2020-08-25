;(function(window,$){
	$.fn.star = function( options ){
		var settings = $.extend({},$.fn.star.default,options);
		return this.each( function(){
			var img = $(this).find('img'),
			    count;
			// img.bind('mousemove',function(){ var index = $(this).index()});
			img.each( function( index ){
					$(this).bind('mousemove',function( e ){
						//img.attr('src','img/star-off-big.png');
						for(var i = 0; i< index; i++){
							img.eq(i).attr('src','images/star-on-big.png');
						}
						var x = e.pageX - $(this).parent().offset().left;
						if( x / 24 < index +0.5){
							$(this).attr('src','images/star-half-big.png');
							count = index + 0.5;
						}else{
							$(this).attr('src','images/star-on-big.png');
							count = index + 1;
						}
				   });
				
			});
			$(this).bind('mouseleave',function(){	
				img.attr('src','images/star-off-big.png');	
			});
			$(this).one('click',function(){
				$(this).unbind('mouseleave');
				img.unbind('mousemove');
				$(this).append( count );
			})
		});
	};
	$.fn.star.default ={
		both:'',
		on:'',
		half:''
	}
})(window,jQuery);