
// JavaScript Document
function nTabs(thisObj,Num){
if(thisObj.className == "active")return;
var tabObj = thisObj.parentNode.id;
var tabList = document.getElementById(tabObj).getElementsByTagName("li");
for(i=0; i < tabList.length+1; i++)
{
  if (i == Num)
  {
	  thisObj.className = "active"; 
      var tem= "block";
  }else{
	  tabList[i].className = "normal"; 
	  var tem= "none";
  }
  document.getElementById(tabObj+"_Content"+i).style.display = tem;
} 
}
