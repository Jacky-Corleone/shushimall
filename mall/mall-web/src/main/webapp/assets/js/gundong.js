;( function(window,$){
	$(function(){
		var wid = $('.Sale_order_list li:first').outerWidth( true ),
            step = 1,
            stepWidth = step * wid;// 多张图滚动的宽度
		//点击右按钮，图往左走（marginLeft负）
		$('.bt-r').bind('click',function(){
			if(!$('.Sale_order_list ul').is(':animated')){
				var objli = $('.Sale_order_list li').slice(0,step);
				objli.clone().appendTo( $('.list ul'));
				//先滚动后更改坐标
				$('.Sale_order_list ul').animate({marginLeft:-stepWidth},500,'easeInOutBounce',function(){
					$('.Sale_order_list ul').css({marginLeft:0});
					objli.remove();
				});
			}
		});
		//点击左按钮，图往右走（marginLeft正）
		$('.bt-l').bind('click',function(){
			if(!$('.Sale_order_list ul').is(':animated')){
				var objli = $('.Sale_order_list li').slice(-step);
				objli.clone().prependTo($('.Sale_order_list ul'));
				//先改坐标后滚动
				$('.Sale_order_list ul').css({marginLeft:-stepWidth});
				$('.Sale_order_list ul').animate({marginLeft:0},500,'easeInOutBounce',function(){
					objli.remove();
				});
			}
		});
		// 自动播放，鼠标滑过停止，鼠标滑离继续播放
	});
})(window,jQuery);