<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getProperty('productName')}</title>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<meta name="decorator" content="default"/>
	<style type="text/css">
        body,thead tr th{ font-family: "微软雅黑"; font-size: 12px;}
		#main {padding:0;margin:0;} #main .container-fluid{padding:0 7px 0 10px;}
		#header {margin:0 0 10px;position:static;} #header li {font-size:14px;_font-size:12px;}
		#header .brand {font-family:Helvetica, Georgia, Arial, sans-serif, 黑体;font-size:26px;padding-left:33px;}
		#footer {margin:8px 0 0 0;padding:3px 0 0 0;font-size:11px;text-align:center;border-top:2px solid #0663A2;}
		#footer, #footer a {color:#999;} 
	</style>
	<script type="text/javascript">
        var menuList;
		$(document).ready(function() {
            loadNav();
            $('#navTab').delegate('a','click',function (e) {
                e.preventDefault();
                $(this).tab('show');
            });

		});
        /**
        *加载导航条
         */
        function loadNav(){
            $.ajax({
                url:"${ctx}/sys/menu/menuData",
                dataType:"json",
                success:function(data){
                    menuList = data;
                    var navHtml = "";
                    $(data).each(function(i,menu){
//                        menu.href = $.parseHTML(menu.href, document, true);
                        if(menu.pId=='1'&&menu.isShow=='1'){
                            navHtml +="<li class='menu'><a class='menu' href='javascript:void(0)' onclick=loadMenu('"+menu.id+"')>"+menu.name+"</a></li>";
                        }
                    });
                    $("#menu1").html(navHtml);
                    $("#menu1 a.menu").click(function(){
                        $("#menu1 li.menu").removeClass("active");
                        $(this).parent().addClass("active");
                        if(!$("#openClose").hasClass("close")){
                            $("#openClose").click();
                        }
                    });
                    $(".accordion-heading a").click(function(){
                        $('.accordion-toggle i').removeClass('icon-chevron-down');
                        $('.accordion-toggle i').addClass('icon-chevron-right');
                        if(!$($(this).attr('href')).hasClass('in')){
                            $(this).children('i').removeClass('icon-chevron-right');
                            $(this).children('i').addClass('icon-chevron-down');
                        }
                    });
                    $(".accordion-body a").click(function(){
                        $("#menu li").removeClass("active");
                        $("#menu li i").removeClass("icon-white");
                        $(this).parent().addClass("active");
                        $(this).children("i").addClass("icon-white");
                    });
                    $("#menu1 li:first a").click();
                    $(".accordion-heading a:first").click();
//                    $(".accordion-body a:first").click();
                }
            });
        }
        /**
         *打开标签页
         * url:要打开的连接
         * title:要显示的标题
         * id,标签唯一ID，这个自己可以随便指定
         */
        function openTab(url,title,id){
            var tempTab =  $(".tab-pane ,.active");
            if(tempTab.length>0){
                $(".tab-pane ,.active").removeClass("active");
            }
            var tempLi = $("#li"+id);
            if(tempLi.length>0){
                $("#li"+id).addClass("active");
                $("#tab"+id).addClass("active");
                //$("#tab"+id).find("iframe").attr("src",$("#tab"+id).find("iframe").attr("src"));
                $("#tab"+id).find("iframe").attr("src",url);
            }else{
                var l = "<li class='active' id='li"+id+"' style='position:relative;cursor:pointer;width:160px;height:30px;overflow: hidden;'><a  href='#tab"+id+"'"+"id='"+id+"'"+">"+title+"</a>&nbsp;&nbsp;&nbsp;&nbsp;<span id='close"+id+"' style='width:20px;height:20px;position:absolute;top:0px;right:-5px;'>&times;</span></li>";
                var d = "<div class='tab-pane active' style='min-height:100%;' id='tab"+id+"'>"+
                        "<iframe src='"+url+"'  scrolling='yes' frameborder='no' width='100%' height='100%' id=if"+id+"></iframe>"+
                        "</div>";
                $("#navTab").append(l);
                $("#mainTab").append(d);

                $("#close"+id).hover(
                        function () {
                            $(this).addClass("hover");
                        },
                        function () {
                            $(this).removeClass("hover");
                        }
                );

                $("#close"+id).click(function(){
                    $("#li"+id).remove();
                    $("#tab"+id).remove();
                    $("#navTab li:last").addClass("active");
                    $("#mainTab .tab-pane:last").addClass("active");
                });
            }

        }
        //关闭当前的tab并且刷新指定的tab
        function getTabandrefress(title,gbid,ifrefresh){
            //获取说有的title超链接
            var as=$("#navTab li a");
            var a=null;
            if(as&&as.length>0){
                var i=0;
                for(i;i<as.length;i++){
                    if($(as[i]).html()==title){
                        a=as[i];
                    }
                }
            }
            if(a!=null){
                var id=$(a).attr('id');
                if(id){
                    $("#li"+gbid).remove();
                    $("#tab"+gbid).remove();
                    $("#li"+id).addClass("active");
                    $("#tab"+id).addClass("active");
                    if(ifrefresh){
                        var ifr = document.getElementById('if'+id);
                        var win = ifr.window || ifr.contentWindow;
                        win.selectpager(ifrefresh);
                    }
                }
            }
        }
        function closeTab(tabid){
            $("#li"+tabid).remove();
            $("#tab"+tabid).remove();
            $("#navTab li:last").addClass("active");
            $("#mainTab .tab-pane:last").addClass("active");
        }
        function getMenuIdByName(name){
        	return $("#left").find("a:contains("+name+")").prop("id");
        }
        function getNewTabAndCloseOld(url,title,newId,oldId){
        	openTab(url,title,newId);
        	closeTab(oldId);
        }
        /**
        *加载菜单
         * @param pid
         */
        function loadMenu(pid){
            var menuHtml = "";
            $(menuList).each(function(i,t){
                if(pid==t.pId&&'1' == t.isShow){
                    menuHtml += "<div class='accordion-group'>";
                    menuHtml += "<div class='accordion-heading'>";
                    menuHtml += "<a class='accordion-toggle' data-toggle='collapse' data-parent='#menu' href='#collapse"+t.id+"' title='"+t.name+"'><i class='icon-chevron-";
                    if(i==0){
                        menuHtml += "down";
                    }else{
                        menuHtml += "right";
                    }
                    menuHtml += "'></i>&nbsp;"+t.name+"</a>";
                    menuHtml += "</div>";
                    menuHtml += "<div id='collapse"+t.id+"' class='accordion-body collapse ";
                    if(i==0){
                        menuHtml += "in";
                    }
                    menuHtml += "'>";
                    menuHtml += "<div class='accordion-inner'>";
                    menuHtml += "<ul class='nav nav-list'>";
                    $(menuList).each(function(j,menuChild){
                        if (menuChild.pId == t.id && menuChild.isShow =='1'){
                            menuHtml += "<li>";
                            menuHtml += "<a href='javascript:void(0)' id='"+menuChild.id+"' onclick=openTab('";
                            if(null!==menuChild.href&&''!=menuChild.href){
                                menuHtml += ctx + menuChild.href;
                            }else{
                                menuHtml += ctx+"/404";
                            }
                            menuHtml += "','"+menuChild.name+"','"+menuChild.id+"')";
                            menuHtml += ">";
                            menuHtml += "<i class='icon-";
                            if(null==menuChild.icon||""==menuChild.icon){
                                menuHtml += "circle-arrow-right'";
                            }else{
                                menuHtml += menuChild.icon+"'";
                            }
                            menuHtml += "></i>";
                            menuHtml += "&nbsp;"+menuChild.name;
                            menuHtml += "</a>";
                            menuHtml += "</li>";
                        }
                    });
                    menuHtml += "</ul>";
                    menuHtml += "</div>";
                    menuHtml += "</div>";
                    menuHtml += "</div>";
                }
            });
            $("#menu").html(menuHtml);
            $(".accordion-heading a:first").click();
//            $(".accordion-body a:first").click();
        }
	</script>
</head>
<body>
	<div id="main">
		<div id="header" class="navbar navbar-fixed-top">
	      <div class="navbar-inner">
	      	 <div class="brand">${fns:getProperty('productName')}</div>
	         <div class="nav-collapse">
	           <ul id="menu1" class="nav">


			<%--	 <shiro:hasPermission name="cms:site:select">
					<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fnc:getSite(fnc:getCurrentSiteId()).name}<b class="caret"></b></a>
						<ul class="dropdown-menu">
						<c:forEach items="${fnc:getSiteList()}" var="site"><li><a href="${ctx}/cms/site/select?id=${site.id}&flag=1">${site.name}</a></li></c:forEach>
						</ul>
					</li>
				 </shiro:hasPermission>--%>
	           </ul>
	           <ul class="nav pull-right">
				 <li><a href="${pageContext.request.contextPath}" title="访问主页"><i class="icon-home"></i></a></li>
			  	 <li id="themeSwitch" class="dropdown">
			       	<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="主题切换"><i class="icon-th-large"></i></a>
				    <ul class="dropdown-menu">
				      <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>
				    </ul>
				    <!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
			     </li>
			  	 <li class="dropdown">
				    <a class="dropdown-toggle" data-toggle="dropdown" href="#" title="个人信息">您好, <shiro:principal property="name"/></a>
				    <ul class="dropdown-menu">
				      <li><a href="${ctx}/sys/user/info" target="mainFrame"><i class="icon-user"></i>&nbsp; 个人信息</a></li>
				      <li><a href="${ctx}/sys/user/modifyPwd" target="mainFrame"><i class="icon-lock"></i>&nbsp;  修改密码</a></li>
				    </ul>
			  	 </li>
			  	 <li><a href="${ctx}/logout" title="退出登录">退出</a></li>
			  	 <li>&nbsp;</li>
	           </ul>
	         </div><!--/.nav-collapse -->
	      </div>
	    </div>
	    <div class="container-fluid">
			<div id="content" class="row-fluid">
				<div id="left">
                        <div class="accordion" id="menu">

                        </div>
                </div>
				<div id="openClose" class="close">&nbsp;</div>
				<div id="right" style="overflow-y: auto">
                        <ul class="nav nav-tabs" id="navTab">
                            <li class='active' id='lidashboard' style='position:relative;cursor:pointer;width:150px;height:30px;overflow: hidden;'><a  href='#tabdashboard'>欢迎使用本系统！</a></li>
                        </ul>
                        <div class="tab-content" id="mainTab" >
                            <div class='tab-pane active' id='tabdashboard'><iframe src='${ctx}/portal/index'  scrolling='yes' frameborder='no' width='100%' height='100%'></iframe></div>
                        </div>
				</div>
			</div>

		</div>
        <div style="clear:both"></div>
        <div id="footer" class="row-fluid" style="height: 20px">
            Copyright &copy; 2005-2014 舒适100 版权所有，并保留所有权利
        </div>

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