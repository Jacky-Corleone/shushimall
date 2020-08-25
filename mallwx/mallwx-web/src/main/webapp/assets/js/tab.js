// JavaScript Document
function nTabs(thisObj, Num) {
	if (thisObj.className == "active")
		return;
	var tabObj = thisObj.parentNode.id;
	var tabList = document.getElementById(tabObj).getElementsByTagName("li");

	for (i = 0; i < tabList.length; i++) {

		if (i == Num) {
			thisObj.className = "active";
			document.getElementById(tabObj + "_Content" + i).style.display = "block";
		} else {

			tabList[i].className = "normal";
			document.getElementById(tabObj + "_Content" + i).style.display = "none";
		}
	}
}
window.onload = function() {
	var $li = $('#tabs li');
	var $ul = $('#con .con');
	$li.click(function() {
		var $this = $(this);
		var $t = $this.index();
		$li.removeClass();
		$this.addClass('active');
		$ul.css('display', 'none');
		$ul.eq($t).css('display', 'block');
	})
}