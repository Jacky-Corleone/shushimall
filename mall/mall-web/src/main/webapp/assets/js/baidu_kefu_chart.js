function online_advice() {
	var s = "siteid=212132&lastsubid=270010&from=&bid=880c314015dddb2a63e61be5&chattype=1&ref=" + encodeURIComponent(window.location.href);
	var o = (window.screen.availWidth - 796) / 2;
	var x = (window.screen.availHeight - 562) / 2;
	var v = "left=" + o + ",top=" + x + ",resizable=yes,width=795,height=561";
	var k = window.open("http://qiao.baidu.com/v3/?module=default&controller=webim&action=index&" + s, "BrIdgeIMWinDow", v);
	k.focus();
}