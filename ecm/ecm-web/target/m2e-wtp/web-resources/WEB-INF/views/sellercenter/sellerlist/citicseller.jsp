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
            $("#searchForm").attr("action","${ctx}/businesslist/citicseller/").submit();
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
            var uid=$("#registResourceid").val();
            var shopName=$("#userFlagid").val();
            var citicflag=$("#citicflag").val();
            var platformId=$("#platformId").val();
            $.ajax({
                url:"${ctx}/businesslist/selectciticseller/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    uid:uid,
                    shopName:shopName,
                    citicflag:citicflag,
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
                            html2=html2+"<tr><td>"+getcode(item.num)+"</td><td>"+getcode(item.uid)+"</td><td>"+getcode(item.shopname)+"</td><td>"+getcode(item.sellers)+"</td><td>"+getcode(item.sellersstatus)+"</td><td>"+getcode(item.sellerd)
                                    +"</td><td>"+getcode(item.sellerdstatus)+"</td>"
                            if(item.platformId==2){
                            	html2=html2+"<td>绿印平台</td>"
                            }else if(item.platformId==null){
                            	html2=html2+"<td>舒适100平台</td>"
                            }
                            if(item.uid){
                                html2=html2+"<td><a href='javascript:void(0)' onclick='showmj(\""+item.uid+"\")'>查看<a></td>"
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
        function getcode(date){
            if(date){
                return date;
            }else{
                return '';
            }
        }

        function showmj(uid){
            var url = "${ctx}/sellerdetail/sellerdetail?uid="+uid;
            var title = "卖家明细";
            var id = "卖家明细";
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
            background: url(${ctx}/../static/picture/company.png) no-repeat;
            /*background: url(http://management.printhome.com/resources/v1/images/company.png) no-repeat;*/
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
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/businesslist/citicseller/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${citicseller.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${citicseller.page.pageSize}" />

            <div class="row-fluid">
                <div class="span5 div-hang">
                    <div class="div-flot">
                        <label class="control-label"  title="商家类型">
                            商家类型：
                        </label>
                    </div>
                    <div>
                    <a style="margin-left:5px;" class="y-button bullerHover" href="${ctx}/businesslist/citicbuller/"><i class="buyerIcon"></i>买家</a>
                    <a class="y-button sellerHover" href="#"><i class="sellerIcon"></i>卖家</a>
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
                <label class="label-left control-label" for="registResourceid" title="商家编号">
                    商家编号：
                </label>
                <input name="registResource" id="registResourceid" style="width:50%" type="text" class="form-control" onkeyup="numInput(this,0)" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" maxlength="18" />
            </div>
            <div class="span5">
                <label class="label-left control-label" for="userFlagid" title="店铺名称">
                    店铺名称：
                </label>
                <input name="userFlag" id="userFlagid" style="width:50%" type="text" class="form-control" />
            </div>
            </div>
            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label" for="citicflag" title="中信账户状态">
                        中信账户状态：
                    </label>
                    <select name="citicflag" id="citicflag" style="width:50%"  class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${citicseller.zxstatus}" var="item">
                            <option value="${item.code}">${item.label}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="selectpage(1)" value="查询" />
                    <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="button" value="重置" onclick="unset()" />
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
                <th width="6%">序号</th>
                <th width="10%">商家编号</th>
                <th width="20%">店铺名称/店铺ID</th>
                <th width="17%">收款账户</th>
                <th width="10%">收款账户状态</th>
                <th width="17%">冻结账户</th>
                <th width="10%">冻结账户状态</th>
                <th width="10%">平台类型</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${citicseller.page.list}" var="user">
                <tr>
                    <td>${user.num}</td>
                    <td>${user.uid}</td>
                    <td>${user.shopname}</td>
                    <td>${user.sellers}</td>
                    <td>${user.sellersstatus}</td>
                    <td>${user.sellerd}</td>
                    <td>${user.sellerdstatus}</td>
                    <td>    
                            <c:choose>
                                <c:when test="${user.platformId==null}">舒适100平台</c:when>
<%--                                 <c:when test="${user.platformId==2}">绿印平台</c:when> --%>
                            </c:choose></td>
                    <td>
                        <a href="javascript:void(0)" onclick="showmj('${user.uid}')">查看</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${citicseller.page}</div>
    </div>
</div>
</body>
</html>
