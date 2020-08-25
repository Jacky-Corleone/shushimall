Handlebars.registerHelper('formatnumber', function(num, dot,options){
	if(dot === undefined || dot === null || dot==="" || isNaN(dot)) {
		dot=2;
	}
	if(isNaN(num)){
		return "";
	}else{
    	return num.toFixed(dot);
	} 
});

var operators = {
      '==':   function(l, r) {return l == r; },
      '===':  function(l, r) {return l === r; },
      '!=':   function(l, r) {return l != r; },
      '!==':  function(l, r) {return l !== r; },
      '>':   function(l, r) {return l > r; },
      '<':   function(l, r) {return l < r; },
      '>=':   function(l, r) {return l >= r; },
      '<=':   function(l, r) {return l <= r; },
      'typeof': function(l, r) {return typeof l == r; }
     };
Handlebars.registerHelper('compare', function(num1,oper, num2,options){
	if(operators[oper](num1,num2)){
		return options.fn(this);
	}else{
		return options.inverse(this);
	}
});

Handlebars.registerHelper('stamp2Date', function(stamp){
	if(stamp==undefined){
		return "";
	}
	return getLocalTime(stamp);
});
Handlebars.registerHelper('imgGix', function(imgurl){
	return _imgGix+imgurl;
});

function getLocalTime(nS) {
    var now=new Date(parseInt(nS));
    var   year=now.getFullYear();
    var   month=now.getMonth()+1;
    var   date=now.getDate();
    return year+"/"+month+"/"+date;
}
