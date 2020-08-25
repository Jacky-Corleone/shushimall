<%--
  Created by IntelliJ IDEA.
  User: menpg
  Date: 2015/3/2
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>用户查询</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            // 表格排序
            //tableSort({callBack : page});
            $("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        $("#searchForm").attr("action","${ctx}/businesslist/exportbuypage").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });
        });
        function unset(){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            $("#searchForm").attr("action","${ctx}/businesslist/basicbuller/").submit();
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            selectpage(n);
            return false;
        }
        function selectpage(p){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            var page=p;
            var rows=$("#pageSize").val();
            var companyName=$("#userNameid").val();
            var uid=$("#registResourceid").val();
            var sheng=$("#sheng").val();
            var shi=$("#shi").val();
            var qu=$("#quid").val();
            var userStace=$("#userStace").val();
            var platformId=$("#platformId").val();
            $.ajax({
                url:"${ctx}/businesslist/selectbasicbuller/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    uid:uid,
                    companyName:companyName,
                    sheng:sheng,
                    shi:shi,
                    qu:qu,
                    userStace:userStace,
                    platformId:platformId
                },
                dataType:'json',
                success:function(data){
                    $.jBox.closeTip();
                    if(data.success){
                        var html="<div class='pagination'>"+data.msg+"</div>";
                        $(".pagination").replaceWith(html);
                        var html2="";
                        $(data.obj.list).each(function(i,item){
                            html2=html2+"<tr><td>"+getcode(item.num)+"</td><td>"+"买家</td><td>"+getcode(item.uid)+"</td><td>"+getcode(item.companyName)+"</td><td>"+getcode(item.created)+"</td><td>"+getcode(item.companyAddr)+"</td><td>"+getcode(item.userStace)+"</td>";
                            if(item.platformId==2){
                            	html2=html2+"<td>绿印平台</td>"
                            }else if(item.platformId==null){
                            	html2=html2+"<td>舒适100平台</td>"
                            }
                            if(item.uid){
                                html2=html2+"<td><a href='javascript:void(0)' onclick='showmjj(\""+item.uid+"\")'>查看<a></td>"
                            }else{
                                html2=html2+"<td></td>"
                            }
                            html2=html2+"</tr>";
                        });
                        $("#tabletbody").html(html2);
                        $("#pageNo").val(data.obj.pageNo);
                        $("#pageSize").val(data.obj.pageSize);
                    }
                },error:function(){
                    $.jBox.closeTip();
                }
            });
        }
		//根据省的编号来获取城市的下拉框
        function citysOfeastern(parentid,childid){
            var shengcode= $("#"+parentid).val();
            if(parentid == "sheng"){
				$("#shi").html("<option value=''>请选择</option>");
             	$('#shi').select2("val", "");
         		$("#quid").html("<option value=''>请选择</option>");
         		$("#quid").select2("val","");
            }else if(parentid == "shi"){
            	$("#quid").html("<option value=''>请选择</option>");
        		$("#quid").select2("val","");
            }
            $.ajax({
                url:"${ctx}/businesslist/selectcity/",
                type:"post",
                data:{
                	parentCode:shengcode
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var html="<option value=''>请选择</option>";
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                                html += "<option value='"+item.code+"'>"+item.name+"</option>";
                            });
                        }
                        $("#"+childid).html(html);
                    }
                }
            });
        }
        function getcode(date){
            if(date){
                return date;
            }else{
                return '';
            }
        }
        function showmjj(uid){
            var url = "${ctx}/sellerdetail/bullerdetail?uid="+uid;
            var title = "买家明细";
            var id = "买家明细";
            parent.openTab(url,title,id);
        }
        function exportall(){
            var size=500;
            var page=1;
            var rows=$("#pageSize").val();
            var companyName=$("#userNameid").val();
            var uid=$("#registResourceid").val();
            var sheng=$("#sheng").val();
            var shi=$("#shi").val();
            var qu=$("#quid").val();
            var userStace=$("#userStace").val();
            var platformId=$("#platformId").val();
            $.ajax({
                url:"${ctx}/businesslist/selectbuyercount/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    uid:uid,
                    companyName:companyName,
                    sheng:sheng,
                    shi:shi,
                    qu:qu,
                    userStace:userStace,
                    platformId:platformId
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var count=data.obj.count;
                        if(count&&count>0){
                            var up=count/size;
                            var uy=count%size;
                            if(uy>0){
                                up=up+1;
                            }
                            exporthd(up,size,1,count,exporthd);
                        }
                    }else{
                        $.jBox.info("亲，系统繁忙请稍后再导出");
                    }
                },error:function(){
                    $.jBox.info("亲，系统繁忙请稍后再导出");
                }
            });
        }
        function exporthd(up,size,page,count,callback){
            if(page<=up){
                $("#pageNo").val(page);
                $("#pageSize").val(size);
                var tiao1=(page-1)*size+1;
                var tiao2=tiao1+size-1;
                if(tiao2>count){
                    tiao2=count;
                }
                var qurenmsg="确认要导出"+tiao1+"~"+tiao2+"的数据吗?"
                top.$.jBox.confirm(qurenmsg,"系统提示",function(v,h,f){
                    if(v=='ok'){
                        $("#searchForm").attr("action","${ctx}/businesslist/exportbuypage").submit();
                        page=page+1;
                        callback(up,size,page,count,callback);
                    }else{
                        page=page+1;
                        callback(up,size,page,count,callback);
                    }
                },{buttonsFocus:1});
            }
        }
    </script>
    <style>
        h3{
            color:#000000;
            height: 46px;
            line-height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }.h3-current{
             width:160px;
             height: 36px;
             text-align: center;
         }
        label.label-left{
            width: 25%;
            text-align: right;
        }
        table.td-cen th,.td-cen td{
            text-align: center;
        }.hhtd td{
             word-wrap:break-word;
             word-break:break-all;
         }
        a.y-button {
            width: 78px;
            height: 28px;
            line-height: 28px;
            text-align: center;
            border: 1px solid #999;
            border-radius: 4px;
            margin-right: 8px;
            float: left;
            display: block;
            cursor: pointer;
            text-decoration:none;
        }.sellerHover {
             background:white;
             border-color: #2bb8aa;
             color: #2bb8aa;
         }.buyerHover {
             background: #fdecec;
             border-color: #e94544;
             color: #e94544;
           }
        i.buyerIcon, .sellerIcon {
            width: 10px;
            height: 12px;
            vertical-align: middle;
            /*background: url(http://management.printhome.com/resources/v1/images/company.png) no-repeat;*/
            background: url(${ctx}/../static/picture/company.png) no-repeat;
            display: inline-block;
            overflow: hidden;
            margin-right: 3px;
        }
        i.buyerIcon{
            background-position: -20px -1px;
        }.sellerIcon{
             background-position: -44px -2px;
         }
        div.div-hang{
            text-overflow:ellipsis;
            white-space: nowrap;
            height: 30px;
        }.div-flot{
            width: 25%;
            float:left;
            text-align: right;
            margin-left: 10px;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/businesslist/basicbuller/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${bacicsbuller.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${bacicsbuller.page.pageSize}" />

            <div class="row-fluid">
                <div class="span5 div-hang">
                    <div class="div-flot">
                        <label class="control-label"  title="商家类型">
                            商家类型：
                        </label>
                    </div>
                    <div>
                    <a style="margin-left:5px;" class="y-button buyerHover" href="#"><i class="buyerIcon"></i>买家</a>
                    <a class="y-button sellerHover" href="${ctx}/businesslist/basicseller/"><i class="sellerIcon"></i>卖家</a>
                    </div>
                </div>
                <div class="span5">
								<label class="label-left control-label" for="platformId"
									title="平台类型">平台类型：</label>
								<select name="platformId" id="platformId" style="width:50%"
									class="form-control">
									<option value="">所有</option>
									<option value="0">舒适100平台</option>
<!-- 									<option value="2">绿印平台</option> -->
								</select>
				</div>
            </div>

            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label"  for="userNameid" title="公司名称">
                        公司名称：
                    </label>
                    <input name="companyName" id="userNameid" style="width:50%" type="text" class="form-control" />
                </div>

                <div class="span5">
                    <label class="label-left control-label" for="registResourceid" title="商家编号">
                        商家编号：
                    </label>
                    <input name="uid" id="registResourceid" style="width:50%" type="text" class="form-control" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="18"/>
                </div>

            </div>
            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label" for="userStace" title="买家状态">
                        买家状态：
                    </label>
                    <select name="userStace" id="userStace" style="width:27%" class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${bacicsbuller.userStace}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="sheng" title="商家地域">
                        商家地域：
                    </label>
                    <select name="sheng" id="sheng" style="width:23%" onchange="citysOfeastern('sheng','shi')" class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${bacicsbuller.address}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                    <select name="shi" id="shi" style="width:23%" onchange="citysOfeastern('shi','quid')" class="form-control">
                        <option value="">请选择</option>
                    </select>
                    <select name="qu" id="quid" style="width:23%"  class="form-control">
                        <option value="" >请选择</option>
                    </select>
                </div>
            </div>
            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="selectpage(1)" value="查询" />
                    <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="button" value="重置" onclick="unset();" />
                </div>
                <!-- <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btnExport"  class="btn  btn-primary" style="width: 26%;" type="button" value="导出当前页"  />
                    <input id="btnExportALL"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="exportall()" value="导出全部"  />
                </div> -->
            </div>
        </form:form>
        <table id="contentTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
            <thead>
            <tr>
                <th width="6%">序号</th>
                <th width="10%">商家类型</th>
                <th width="10%">商家编号</th>
                <th width="17%">公司名称</th>
                <th width="12%">开通时间</th>
                <th width="19%">商家地域</th>
                <th width="10%">用户状态</th>
                <th width="6%">平台类型</th>
                <th width="15%">操作</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${bacicsbuller.page.list}" var="user">
                <tr>
                    <td>${user.num}</td>
                    <td>买家</td>
                    <td>${user.uid}</td>
                    <td>${user.companyName}</td>
                    <td>${user.created}</td>
                    <td>${user.companyAddr}</td>
                    <td>${user.userStace}</td>
                    <td>    
                            <c:choose>
                                <c:when test="${user.platformId==null}">舒适100平台</c:when>
<%--                                 <c:when test="${user.platformId==2}">绿印平台</c:when> --%>
                            </c:choose></td>
                    <td>
                        <c:if test="${user.uid!=''}">
                        <a onclick="showmjj('${user.uid}')" href="javascript:void(0)">查看</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${bacicsbuller.page}</div>
    </div>
</div>
</body>
</html>
