function online_advice(goupId,goupName){
	var s="siteid=8206177&ucid=19272508&lastsubid=0&from=&bid=2c333f6e4c8174170289a9ec&chattype=1&ref="+encodeURIComponent(window.location.href);
	var o=(window.screen.availWidth-796)/2;
	var x=(window.screen.availHeight-562)/2;
	var v="left="+o+",top="+x+",resizable=yes,width=795,height=561";
	var k=window.open("http://qiao.baidu.com//im/index?"+s,"BrIdgeIMWinDow",v);
	k.focus()
}