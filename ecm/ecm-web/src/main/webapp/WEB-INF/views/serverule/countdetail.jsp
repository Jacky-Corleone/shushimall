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
    <title>服务规则认证统计</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
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
            var ruleId=$("#ruleId").val();
            var createTimeBegin=$("#createTimeBegin").val();
            var createTimeEnd=$("#createTimeEnd").val();
            $.ajax({
                url:"${ctx}/serverule/selectuser/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    ruleId:ruleId,
                    createTimeBegin:createTimeBegin,
                    createTimeEnd:createTimeEnd
                },
                dataType:'json',
                success:function(data){
                    $.jBox.closeTip();
                    if(data.success){
                        var html="<div class='pagination'>"+data.msg+"</div>";
                        $(".pagination").replaceWith(html);
                        var html2="";
                        $(data.obj.list).each(function(i,item){
                            html2=html2+"<tr><td>"+getcode(item.shopName)+"</td><td>"+getcode(item.ruleName)+"</td><td>"+getcode(item.rzdate)+"</td>";
                            /*if(item.shopUrl){
                                html2=html2+"<td><a href='javascript:void(0)' target='_Blank'  onclick='showdianpu(\""+item.shopUrl+"\")"+"'>查看店铺</a></td>";
                            }else{
                                html2=html2+"<td></td>"
                            }*/
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
        //显示明细
        function showdianpu(url){
            window.open("http://"+url);
        }
        function unset(){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            $("#searchForm").submit();
        }
    </script>
    <style>
        table {
            max-width: 100%;
            background-color: transparent;
            border-collapse: collapse;
            border-spacing: 0;
        }
        .y-tb1{
            border-top: 1px solid #eee;
            border-bottom: 1px solid #eee;
            border-left:1px solid #eee;
            border-right: 1px solid:#eee;
        }.y-tb1 td{
             border-top: 1px solid #eee;
             border-bottom: 1px solid #eee;
             border-left:1px solid #eee;
             border-right: 1px solid:#eee;
         }.y-tb1 th{
              border-top: 1px solid #eee;
              border-bottom: 1px solid #eee;
              border-left:1px solid #eee;
              border-right: 1px solid:#eee;
              font-weight:normal;
              color:#0000ff;
          }
        .mb20 {
            margin-bottom: 20px;
        }
        .z-tbl {
            width: 100%;
            text-align: center;
        }
        .z-tbl td {
            border-bottom: 1px dotted #ccc;
            padding: 10px 5px;
            line-height: 16px;
            color: #666;
            text-align: center;
        }.z-tbl th {
             border-bottom: 1px dotted #ccc;
             padding: 10px 5px;
             line-height: 16px;
             color: #666;
             text-align: center;
         }
        div{
            width: 95%;
        }
        .y-box {
            border: 1px solid #eee;
            border-top: 2px solid #00a2ca;
            margin-bottom: 20px;
        }

        h3{
            color:#000000;
            height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }
        a.create-btn {
            position: absolute;
            top: 17px;
            right: 15%;
            padding: 10px 13px;
            line-height: 12px;
            color: #00a2ca;
            border: 1px solid #7ecbdf;
            background: #eafbfe;
            border-radius: 2px;
            -moz-border-radius: 2px;
            -webkit-border-radius: 2px;
        }
        ul.ul1{
            display:inline;
            list-style:none;
            display:block;
        }
        li.li1{
            margin-bottom: 10px;
            margin-right: 30px;
            float:left;
            display:block;
        }
        span.gray-color{
            color: #999;
        }
        label.label-left{
            width: 25%;
            text-align: right;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body y-box" style="margin-left:3%;width: 95%;">

            <form:form id="searchForm" modelAttribute="user" action="${ctx}/serverule/showcountdetail/" method="post">
                <input id="pageNo" name="pageNo" type="hidden" value="${countdetail.page.pageNo}" />
                <input id="pageSize" name="pageSize" type="hidden" value="${countdetail.page.pageSize}" />

                <div class="row-fluid" style="margin-top:10px;">
                    <div class="span5">
                        <label class="label-left control-label" for="ruleId" title="认证类型">
                            认证类型：
                        </label>
                        <select name="ruleId" id="ruleId" style="width:27%"  class="form-control">
                            <c:forEach items="${countdetail.ruleMaps}" var="item">
                                <c:if test="${countdetail.ruleId==item.code}">
                                    <option selected="selected" value="${item.code}">${item.name}</option>
                                </c:if>
                                <c:if test="${countdetail.ruleId!=item.code}">
                                    <option  value="${item.code}">${item.name}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="span5">
                        <label class="label-left control-label" for="createTimeBegin" title="认证时间">
                            认证时间：
                        </label>
                        <input name="createTimeBegin" id="createTimeBegin" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                        至
                        <input name="createTimeEnd" id="createTimeEnd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                    </div>
                </div>
                <div class="row-fluid" style="margin-top: 8px;">
                    <div class="span5">
                        <label class="label-left control-label"></label>
                        <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="selectpage(1)" value="查询" />
                        <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="button" value="重置" onclick="unset();" />
                    </div>
                </div>
            </form:form>
            <table id="contentTable1" class="y-tb1 z-tbl mb20">
                <colgroup>
                    <col width="33.33%">
                    <col width="33.33%">
                    <col width="33.33%">
                </colgroup>
                <thead>
                <tr>
                    <th style="background: #fbfaf8;">商家名称</th>
                    <th style="background: #fbfaf8;">认证类型</th>
                    <th style="background: #fbfaf8;">认证时间</th>
                </tr>
                </thead>
                <tbody id="tabletbody">
                <c:forEach items="${countdetail.page.list}" var="user">
                    <tr>
                        <td>${user.shopName}</td>
                        <td>${user.ruleName}</td>
                        <td>${user.rzdate}</td>
<%--                        <td>
                            <c:if test="${not empty user.shopUrl}">
                                <a href="javascript:void(0)" target="_Blank"  onclick="showdianpu('${user.shopUrl}')">查看店铺</a>
                            </c:if>
                        </td>--%>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination">${countdetail.page}</div>
    </div>
</div>
</body>
</html>
