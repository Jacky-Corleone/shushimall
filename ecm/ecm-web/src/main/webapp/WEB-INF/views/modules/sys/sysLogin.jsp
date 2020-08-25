<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getProperty('productName')} 登录</title>
	<meta name="decorator" content="default"/>
    <meta name="renderer" content="webkit" />
    <link href="${ctxStatic}/css/font-google.css" type="text/css"  rel="stylesheet" />
    <link href="${ctxStatic}/css/bootstrap-responsive.css" type="text/css"  rel="stylesheet" />
    <link href="${ctxStatic}/css/bootstrap-responsive-custom.css" type="text/css"  rel="stylesheet" />

    <link id="style-base" href="${ctxStatic}/css/stilearn.css" type="text/css"  rel="stylesheet" />
    <link id="style-responsive" href="${ctxStatic}/css/stilearn-responsive.css" type="text/css"  rel="stylesheet" />
    <link id="style-helper" href="${ctxStatic}/css/stilearn-helper.css" type="text/css"  rel="stylesheet" />
    <link href="${ctxStatic}/css/stilearn-icon.css" type="text/css"  rel="stylesheet" />
    <link href="${ctxStatic}/css/font-awesome.css" type="text/css"  rel="stylesheet" />
    <link href="${ctxStatic}/css/uniform.default.css" type="text/css"  rel="stylesheet" />

    <style type="text/css">
		.control-group{border-bottom:0px;}
        .form-actions{
            padding:5px 20px 5px ;
            background:#fff;
            border-top:none;
        }
	</style>
    <style>

        #sign-in .control-group{
            margin-bottom: 5px;
        }
        div.signin-form{
            /* margin-top: 90px; */
        }

    </style>

	<script type="text/javascript">
		$(document).ready(function() {
            $("#loginForm").validate({
                errorLabelContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    error.appendTo($("#loginError").parent());
                }
            });

            // 如果在框架中，则跳转刷新上级页面
            if(self.frameElement && self.frameElement.tagName=="IFRAME"){
                parent.location.reload();
            }
		});

	</script>
</head>
<body>
<!--[if lte IE 9]><br/><div class='alert alert-block' style="text-align:left;padding-bottom:10px;"><a class="close" data-dismiss="alert">x</a><h4>温馨提示：</h4><p>你使用的浏览器版本过低。为了获得更好的浏览体验，我们强烈建议您 <a href="http://browsehappy.com" target="_blank">升级</a> 到最新版本的IE浏览器，或者使用较新版本的 Chrome、Firefox、Safari 等。</p></div><![endif]-->
<header class="header" data-spy="affix" data-offset-top="0">
    <!--nav bar helper-->
    <div class="navbar-helper">
        <div class="row-fluid">
            <!--panel site-name-->
            <div class="span8">
                <div class="panel-sitename fl">
                    <img src="${ctxStatic}/images/logo.jpg" style="float:left;"/>
                    <span class="logo_sp">欢迎登录</span>
                </div>
            </div>
            <!--/panel name-->
        </div>
    </div><!--/nav bar helper-->
</header>

<section class="section" style="margin-top: 6%;">
	<div class="container_bg">
    <div class="container">
        <div class="signin-form row-fluid" >
            <!--Sign In-->
            <div class="span7"> <img src="${ctxStatic}/images/loginimg.png" style="height: 420px"/></div>
            <div class="span3">
                <div class="box corner-all mar_t30" >
                <b></b>
                    <div class="box-header grd-teal color-white corner-top">
                        <span style="font-family: '微软雅黑'; font-size: 20px; color:#666;">账户登录</span>
                    </div>
                    <div class="box-body bg-white">
                        <form id="sign-in" method="post" action="${ctx}/login" target="_top"/>
                        <div class="control-group">
                            <label class="control-label">用户名</label>
                            <div class="controls">
                                <input type="text" class="input-block-level required"  name="username" id="username" title="用户名不能为空" />
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">密码</label>
                            <div class="controls">
                                <input type="password" class="input-block-level required" name="password" id="password" title="密码不能为空"  />
                            </div>
                        </div>
                        <div class="control-group">
                            <!-- <label class="checkbox">
                                <input type="checkbox" data-form="uniform" name="remember_me" id="remember_me_yes" value="yes" /> Remember me
                            </label> -->
                            <%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
                            <div style="height:40px;">
	                            <div id="messageBox" class="alert alert-error <%=error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>
	                                <label id="loginError" class="error"><%=error==null?"":"用户或密码错误, 请重试." %></label>
	                            </div>
                            </div>
                        </div>
                        <div class="form-actions">
                            <input type="submit" class="btn btn-primary fr" value="登录" />

                            <!--<p class="recover-account">Recover your <a href="#modal-recover" class="link" data-toggle="modal">username or password</a></p>-->
                        </div>
                        </form>
                    </div>
                </div>
            </div><!--/Sign In-->
        </div>
    </div>
   </div>
</section>

<!-- section footer -->
<footer style="text-align: center;">
<p style="color:#999;">鄂ICP备09016424号<br>
Copyright &copy; 舒适100 版权所有，并保留所有权利。<br>
<!-- 舒适100舒适100Copyright &copy; 舒适100 版权所有，并保留所有权利。<br> --></p>
</footer>

  </body>
</html>