
/**
 * 
 * <p>
 * Description: [倒计时]
 * </p>
 * Created on 2015-5-17
 * 
 * @author 王东晓
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 * 
 * 使用方法：<div  countDown='true' startTime = "2015/05/16 14:30:00" noStartTip="还没有开始" endTime="2015/05/18 14:30:00" endTip="已结束" callBack="回调函数，当倒计时为0后调用此方法，为空或者不设置时，不执行">此处显示倒计时</div>
 */


/**
 * 倒计时构造函数
 * @param element 使用倒计时的元素
 * @param startTime 开始时间
 * @param noStartTip 未开始提示
 * @param endTime 结束时间
 * @param endTip 倒计时结束提示
 * 
 */
Timer = function(element,startTime,noStartTip,endTime,endTip,callBackFunction){
	this.element = element;
	this.startTime = startTime;
	this.noStartTip = noStartTip==null?"未开始":noStartTip;
	this.endTime = endTime;
	this.endTip = endTip==null?"已结束":endTip;
	this.callBackFunction = callBackFunction;
}
/**
 * 倒计时显示时间
 * 
 * @param text 倒计时信息
 */
Timer.prototype.innerHTML = function(text){
	this.element.innerHTML = text;
}
/**
 * 倒计时结束后，执行回调函数
 * @param callBack
 */
Timer.prototype.callBack = function(callBackFunction){
	if(callBackFunction == null || callBackFunction == ""){
		return ;
	}
	eval(callBackFunction);
	
}
/**
 * 倒计时计算器
 */
Timer.prototype.countDown =function(){

	var timer =this;
	
	var interval = setInterval(function(){
		if(new Date(timer.startTime) > new Date()){
			timer.innerHTML(timer.noStartTip);
			clearInterval(interval);
			return ;
		}

		if(new Date(timer.endTime) < new Date()){
			timer.innerHTML(timer.endTip);
			clearInterval(interval);
			timer.callBack(timer.callBackFunction);
			return ;
		}

		var ts = new Date(timer.endTime) - (new Date());//计算剩余的毫秒数  
		var dd = parseInt(ts / 1000 / 60 / 60 / 24, 10);//计算剩余的天数  
		var hh = parseInt(ts / 1000 / 60 / 60 % 24, 10);//计算剩余的小时数  
		var mm = parseInt(ts / 1000 / 60 % 60, 10);//计算剩余的分钟数  
		var ss = parseInt(ts / 1000 % 60, 10);//计算剩余的秒数  
		dd = timer.checkTime(dd);  
		hh = timer.checkTime(hh);  
		mm = timer.checkTime(mm);  
		ss = timer.checkTime(ss);  
		
		var text = dd + "天" + hh + "时" + mm + "分" + ss + "秒";  
		timer.innerHTML(text);
	},1000); 
}

Timer.prototype.checkTime =function(number){
	if (number < 10) {    
           number = "0" + number;    
        }    
       return number;
}

$(document).ready(function() {
	//查询带有countDown属性的元素
	$("[countDown]").each(function(){
		if(this.getAttribute('countDown')=="true"){
			var startTime = this.getAttribute('startTime');
			var noStartTip = this.getAttribute('noStartTip');
			var endTime = this.getAttribute('endTime');
			var endTip = this.getAttribute('endTip');
			var callBackFunction = this.getAttribute('callBack');
			(new Timer(this,startTime,noStartTip,endTime,endTip,callBackFunction)).countDown();
		}
		
	});
});