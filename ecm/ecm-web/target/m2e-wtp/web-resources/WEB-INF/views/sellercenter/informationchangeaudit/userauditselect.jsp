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
            $("#btnexport").click(function(){
                top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        $("#searchForm").attr("action","${ctx}/sellerlist/export").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });
        });
        function unset(){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            $("#searchForm").submit();
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            selectpage(n);
            return false;
        }
        function selectpager(ifselect){
            if(ifselect){
                var page=$("#pageNo").val();
                //$.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
                selectpage(page);
                //$.jBox.closeTip();
            }
        }
        function selectpage(p){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            var page=p;
            var rows=$("#pageSize").val();
            var userName=$("#userNameid").val();
            var userCode =$("#userCodeid").val();
            var stace=$("#auditflag").val();
            $.ajax({
                url:"${ctx}/informationchangingaudit/selectuseraudit/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    userName:userName,
                    userCode:userCode,
                    stace:stace
                },
                dataType:'json',
                success:function(data){
                    $.jBox.closeTip();
                    if(data.success){
                        var html="<div class='pagination'>"+data.msg+"</div>";
                        $(".pagination").replaceWith(html);
                        var html2="";
                        var num=0;
                        $(data.obj.list).each(function(i,item){
                            num++;
                            html2=html2+"<tr><td>"+num+"</td><td>"+getcode(item.modifyType)+"</td><td>"+getcode(item.uid)+"</td><td>"+getcode(item.applicantName)+"</td><td>"+getcode(item.stace)+"</td><td>"+getcode(item.createTime)+"</td>";
                            if(item.uid&&item.stacecode=='0'){
                                html2=html2+"<td><a href='javascript:goaudit(\""+getcode(item.id)+"\",\""+getcode(item.uid)+"\")'>审核<a></td>"
                            }else{
                                html2=html2+"<td><a href='javascript:godetail(\""+getcode(item.id)+"\",\""+getcode(item.uid)+"\")'>查看<a></td>"
                            }
                            html2=html2+"</tr>";
                        });
                         $("#tabletbody").html(html2);
                        $("#pageNo").val(data.obj.pageNo);
                        $("#pageSize").val(data.obj.pageSize);
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(){
                    $.jBox.closeTip();
                }
            });
        }
        //根据省的编号来获取城市的下拉框
        function citysOfeastern(){
            var shengcode= $("#sheng").val();
            $.ajax({
                url:"${ctx}/businesslist/selectcity/",
                type:"post",
                data:{
                    shengcode:shengcode
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var html="<option value=''>请选择</option>"
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                                 html += "<option value='"+item.code+"'>"+item.name+"</option>";
                            });
                        }
                        $("#shi").html(html);
                    }
                }
            });
        }
        //经营商品分类级联，类目
        function leimu(parentid,childid){
            var parentNum= $("#"+parentid).val();
            $.ajax({
                url:"${ctx}/businesslist/selectlm/",
                type:"post",
                data:{
                    parentNum:parentNum
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var html="<option value=''>请选择</option>"
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                                html += "<option value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
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
        function goaudit(id,uid){
           var url="${ctx}/informationchangingaudit/userauditdetail?id="+id+"&uid="+uid;
            var title = "商家变更审核办理";
            var id = "商家变更审核办理";
            parent.openTab(url,title,id);
        }

        function godetail(id,uid){
            var url="${ctx}/informationchangingaudit/showuserauditdetail?id="+id+"&uid="+uid;
            var title = "商家变更审核明细";
            var id = "商家变更审核明细";
            parent.openTab(url,title,id);
         }
      //输入字符显示
        function numInput(obj,length){
        	if(obj.value==obj.value2)
        		return;
        	if(length == 0 && obj.value.search(/^\d*$/)==-1)
        		obj.value=(obj.value2)?obj.value2:'';
        	else if(obj.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)
        		obj.value=(obj.value2)?obj.value2:'';
        	else obj.value2=obj.value;
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
             background: #e9f8f6;
             border-color: #2bb8aa;
             color: #2bb8aa;
         }.bullerHover{
             background:white;
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
            height: 40px;
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
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/informationchangingaudit/userauditselect/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${auditinfo.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${auditinfo.page.pageSize}" />
            <div class="row-fluid">
                <div class="span5">
                    <label class="label-left control-label"  for="userNameid" title="公司名称">
                        申请人：
                    </label>
                    <input name="userName" id="userNameid" style="width:50%" type="text" class="form-control" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="userCodeid" title="店铺开通时间">
                        商家编码：
                    </label>
                    <input name="userCode" id="userCodeid" style="width:50%" type="text" class="form-control" onkeyup="numInput(this,0)" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" maxlength="18" />

                </div>
            </div>
             <div class="row-fluid" style="margin-top: 8px;">
            <div class="span5">
                <label class="label-left control-label" for="auditflag" title="审核状态">
                    申请状态：
                </label>
                <select name="auditflag" id="auditflag" style="width:27%" class="form-control">
                    <c:forEach items="${auditinfo.liststace}" var="item">
                        <option value="${item.code}">${item.name}</option>
                    </c:forEach>
                </select>
            </div>
            </div>
            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btnquery1"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="selectpage(1)" value="查询" />
                    <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="unset();" value="重置"  />
                </div>
<%--                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btnexport"  class="btn  btn-primary" style="width: 26%;" type="button" value="导出"  />
                </div>--%>
            </div>
        </form:form>
        <table id="contentTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
            <thead>
            <tr>
                <th width="10%">序号</th>
                <th width="15%">申请类型</th>
                <th width="15%">商家编号</th>
                <th width="20%">申请人</th>
                <th width="10%">申请状态</th>
                <th width="20%">申请时间</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${auditinfo.page.list}" var="user" varStatus="status">
                <tr>
                    <td><c:out value="${status.count}" /></td>
                    <td>${user.modifyType}</td>
                    <td>${user.uid}</td>
                    <td>${user.applicantName}</td>
                    <td>${user.stace}</td>
                    <td>${user.createTime}</td>
                    <td>
                        <c:if test="${user.id!=''&&user.stacecode=='0'}">
                            <a href="javascript:goaudit('${user.id}','${user.uid}')">审核</a>
                        </c:if>
                        <c:if test="${user.id!=''&&user.stacecode!='0'}">
                            <a href="javascript:godetail('${user.id}','${user.uid}')">查看</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${auditinfo.page}</div>
    </div>
</div>
</body>
</html>
