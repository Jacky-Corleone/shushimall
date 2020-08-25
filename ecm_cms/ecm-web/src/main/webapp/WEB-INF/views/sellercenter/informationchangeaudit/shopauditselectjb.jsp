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
        function selectpager(ifselect){
            if(ifselect){
                var page=$("#pageNo").val();
                //$.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
                selectpage(page);
                //$.jBox.closeTip();
            }
        }
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
        function selectpage(p){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            var page=p;
            var rows=$("#pageSize").val();
            var companyName=$("#companyNameid").val();
            var uid=$("#userCodeid").val();
            $.ajax({
                url:"${ctx}/informationchangingaudit/selectjbaudit/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    companyName:companyName,
                    uid:uid
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
                            html2=html2+"<tr><td>"+num+"</td><td>"+"基本信息修改</td><td>"+getcode(item.uid)+"</td><td>"+getcode(item.companyname)+"</td><td>"+getcode(item.sqdate)+"</td>"
                            if(item.id){
                                html2=html2+"<td><a href='javascript:void(0)' onclick='showdetaile(\""+getcode(item.id)+"\",\""+getcode(item.companyname)+"\",\""+getcode(item.uid)+"\",\""+getcode(item.shopid)+"\")'>查看申请<a></td>";
                            }else{
                                html2=html2+"<td></td>";
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
        function getcode(date){
            if(date){
                return date;
            }else{
                return '';
            }
        }
        function showdetaile(id,companyname,uid,shopid){
            var url="${ctx}/informationchangingaudit/shopauditdetailjb?id="+id+"&company="+companyname+"&uid="+uid+"&shopid="+shopid;
            var title = "店铺基本信息变更审核";
            var id = "店铺基本信息变更审核";
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
            width: 82px;
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
            background: #fdecec;
            border-color: #e94544;
            color: #e94544;
         }.bullerHover{
             background:white;
             color: #333;
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
            width: 12.5%;
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
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/informationchangingaudit/shopauditselectjb/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="row-fluid">
                <div class="span10 div-hang">
                    <div class="div-flot">
                        <label class="control-label"  title="商家类型">
                            审核类型：
                        </label>
                    </div>
                    <div>
                        <a class="y-button sellerHover" href="#">基本信息修改</a>
                        <a style="margin-left:5px;" class="y-button bullerHover" href="${ctx}/informationchangingaudit/shopauditselectlm/">新类目申请</a>
                        <a style="margin-left:5px;" class="y-button bullerHover" href="${ctx}/informationchangingaudit/shopauditselectpp/">新品牌申请</a>
                    </div>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span5">
                    <label class="label-left control-label"  for="companyNameid" title="公司名称">
                        公司名称：
                    </label>
                    <input name="companyName" id="companyNameid" style="width:50%" type="text" class="form-control" />
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
                <th width="9%">序号</th>
                <th width="18%">申请类型</th>
                <th width="18%">商家编号</th>
                <th width="27%">公司名称</th>
                <th width="17%">申请时间</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${page.list}" var="user" varStatus="status">
                <tr>
                    <td><c:out value="${status.count}" /></td>
                    <td>基本信息修改</td>
                    <td>${user.uid}</td>
                    <td>${user.companyname}</td>
                    <td>${user.sqdate}</td>
                    <td>
                    <c:if test="${user.id!=''}">
                        <a href="javascript:void(0)" onclick="showdetaile('${user.id}','${user.companyname}','${user.uid}','${user.shopid}')">查看申请</a>
                    </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${page}</div>
    </div>
</div>
</body>
</html>
