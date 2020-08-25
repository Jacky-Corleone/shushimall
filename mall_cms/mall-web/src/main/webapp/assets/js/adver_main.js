// JavaScript Document
function SetActiveIndex(jElms, jElmNums, i,obj) {
	if (jElms.length == 0)
		return;
	jElms.hide();
	$(jElms[i]).show();

	jElmNums.removeClass("focus_num2");
    $(jElmNums[i]).addClass("focus_num2");
}

function GetActiveIndex(jElms) {
	return jElms.index(jElms.filter(":visible"));
}

function SetNextActive(jElms, jElmNums) {
	if (jElms.length == 0)
		return;
	var i = GetActiveIndex(jElms) + 1;
	if (i >= jElms.length)
		i = 0;
	SetActiveIndex(jElms, jElmNums, i);
}

var mainFlashTimer = null,centerListTimer = null;
$(function() {
	mainFlashTimer = window.setInterval(function() {
		SetNextActive($('.main_flash_bg>.focusPic>.lbp'),
				$(".main_flash_bg>.focusPic>.num_bg2>ul>li"));
	}, 3000);
	centerListTimer = window.setInterval(function() {
		SetNextActive($('.centerList>.focusPic1'),
				$(".centerList>.focusPic1>.num_bg>ul>li"));
	}, 3000);
});

	$(".main_flash").mouseover(function(){
		clearInterval(mainFlashTimer);
		clearInterval(centerListTimer);
	});
	function clearFlashTimer(){
		clearInterval(mainFlashTimer);
		clearInterval(centerListTimer);
	}
	function leaveFlashTimer(){
		mainFlashTimer = window.setInterval(function() {
			SetNextActive($('.main_flash_bg>.focusPic>.lbp'), $(".main_flash_bg>.focusPic>.num_bg2>ul>li"));
		}, 3000);
		centerListTimer = window.setInterval(function() {
			SetNextActive($('.centerList>.focusPic1'), $(".centerList>.focusPic1>.num_bg>ul>li"));
		}, 3000);
		
	}
	$(".main_flash").mouseleave(function(){
		mainFlashTimer = window.setInterval(function() {
			SetNextActive($('.main_flash_bg>.focusPic>.lbp'), $(".main_flash_bg>.focusPic>.num_bg2>ul>li"));
		}, 3000);
		centerListTimer = window.setInterval(function() {
			SetNextActive($('.centerList>.focusPic1'), $(".centerList>.focusPic1>.num_bg>ul>li"));
		}, 3000);
	});