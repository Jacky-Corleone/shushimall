var newSlideSize = function slideSize() {
	var w = Math.ceil($(".swiper-container").width() / 1.96);
	$(".swiper-container,.swiper-wrapper,.swiper-slide").height(w);
};
$(window).resize(function() {
	newSlideSize();
});

window.onload = function() {
	var mySwiper = new Swiper('.swiper-container', {
		pagination : '.pagination',
		loop : true,
		paginationClickable : true,
		onSlideChangeStart : function() {
		}
	});
};