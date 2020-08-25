<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>fafsafsaf</title>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<meta name="decorator" content="default"/>
	<style type="text/css">
        body,thead tr th{ font-family: "微软雅黑"; font-size: 12px;}
		#main {padding:0;margin:0;} #main .container-fluid{padding:0 7px 0 10px;}
		#header {margin:0 0 10px;position:static;} #header li {font-size:14px;_font-size:12px;}
		#header .brand {font-family:Helvetica, Georgia, Arial, sans-serif, 黑体;font-size:26px;padding-left:33px;}
		#footer {margin:8px 0 0 0;padding:3px 0 0 0;font-size:11px;text-align:center;border-top:2px solid #0663A2;}
		#footer, #footer a {color:#999;} 
	</style>

</head>
<body>
	<div id="main">
	    </div>
	    <div class="container-fluid">
			<div id="content" class="row-fluid">
				<div id="left">
				<IFRAME scrolling="yes" width="100%" height="100%" frameBorder="0" id="iframeTree" name="iframeTree" src="${ctx}/tag/treeselectadress" allowTransparency="true"></IFRAME>
                </div>
				<div id="openClose" class="close">&nbsp;</div>
				<div id="right" style="overflow-y: auto">
                     <iframe src='${ctx}/tag/treeselectadresstab'  scrolling='yes' id="iframeTreeWithTab" name="iframeTreeWithTab" frameborder='no' width='100%' height='100%'></iframe>
				</div>
			</div>

		</div>
        <div style="clear:both"></div>

	</div>
	<script type="text/javascript"> 
		var leftWidth = "200"; // 左侧窗口大小
		function wSize(){
			var minHeight = 500, minWidth = 980;
			var strs=getWindowSize().toString().split(",");
            var swidth = (strs[0]<minHeight?minHeight:strs[0])-$("#header").height()-$("#footer").height()-30;
			$("#left, #right,#openClose").height(swidth);
			$("#mainTab").height(swidth-45);
            $("#openClose").height($("#openClose").height()-5);
			if(strs[1]<minWidth){
				$("#main").css("width",minWidth-10);
				$("html,body").css({"overflow":"auto","overflow-x":"auto","overflow-y":"auto"});
			}else{
				$("#main").css("width","auto");
				$("html,body").css({"overflow":"hidden","overflow-x":"hidden","overflow-y":"hidden"});
			}
			$("#right").width($("#content").width()-$("#left").width()-$("#openClose").width()-5);
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>