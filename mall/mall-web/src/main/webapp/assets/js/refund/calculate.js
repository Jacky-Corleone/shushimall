//除法计算
function numDiv(num1, num2) { 
	var baseNum1 = 0, baseNum2 = 0; 
	var baseNum3, baseNum4; 
	try { 
	baseNum1 = num1.toString().split(".")[1].length; 
	} catch (e) { 
	baseNum1 = 0; 
	} 
	try { 
	baseNum2 = num2.toString().split(".")[1].length; 
	} catch (e) { 
	baseNum2 = 0; 
	} 
	with (Math) { 
	baseNum3 = Number(num1.toString().replace(".", "")); 
	baseNum4 = Number(num2.toString().replace(".", "")); 
	return (baseNum3 / baseNum4) * pow(10, baseNum2 - baseNum1); 
	} 
};

function clearNoNum(event,obj){
	//响应鼠标事件，允许左右方向键移动
	event = window.event||event;
	if(event.keyCode == 37 | event.keyCode == 39){
		return;
	}
	//先把非数字的都替换掉，除了数字和.
	obj.value = obj.value.replace(/[^\d.]/g,"");
	//必须保证第一个为数字而不是.
	obj.value = obj.value.replace(/^\./g,"");
	//保证只有出现一个.而没有多个.
	obj.value = obj.value.replace(/\.{2,}/g,".");
	//保证.只出现一次，而不能出现两次以上
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
}
function checkNum(obj){
	//为了去除最后一个.
	obj.value = obj.value.replace(/\.$/g,"");
}

function numAdd(num1, num2) {
    var baseNum, baseNum1, baseNum2;
    try {
        baseNum1 = num1.toString().split(".")[1].length;
    } catch (e) {
        baseNum1 = 0;
    }
    try {
        baseNum2 = num2.toString().split(".")[1].length;
    } catch (e) {
        baseNum2 = 0;
    }
    baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
    return (num1 * baseNum + num2 * baseNum) / baseNum;
};
function numSub(num1, num2) { 
	var baseNum, baseNum1, baseNum2; 
	var precision;// 精度 
	try { 
	baseNum1 = num1.toString().split(".")[1].length; 
	} catch (e) { 
	baseNum1 = 0; 
	} 
	try { 
	baseNum2 = num2.toString().split(".")[1].length; 
	} catch (e) { 
	baseNum2 = 0; 
	} 
	baseNum = Math.pow(10, Math.max(baseNum1, baseNum2)); 
	precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2; 
	return ((num1 * baseNum - num2 * baseNum) / baseNum).toFixed(precision); 
	}; 
