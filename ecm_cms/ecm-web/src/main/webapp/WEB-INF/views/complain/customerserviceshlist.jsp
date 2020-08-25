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
    <title>售后查询</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <%@include file="/WEB-INF/views/include/baobiao.jsp" %>
    <script type="text/javascript">
        var echarmen;
        $(document).ready(function() {
            //初始化图形报表

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
                selectpage(page);
            }
        }
        function selectpage(p){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            var page=p;
            var rows=$("#pageSize").val();
            var tkcode= $("#tkcodeid").val();
            var buyerNum= $("#buyerNumid").val();
            var tkstace= $("#tkstaceid").val();
            var orderid= $("#orderid").val();
            var createTimeBegin= $("#createTimeBegin").val();
            var createTimeEnd= $("#createTimeEnd").val();
            $.ajax({
                url:"${ctx}/customerservice/customerserviceselectshjson/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    tkcode:tkcode,
                    buyerNum:buyerNum,
                    tkstace:tkstace,
                    orderid:orderid,
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
                        var num=0;
                        $(data.obj.list).each(function(i,item){
                            num++;
                            html2=html2+"<tr><td>"+num+"</td><td>"+getcode(item.thcode)+"</td><td>"+getcode(item.statename)+"</td><td>"+getcode(item.orderid)+"</td><td>"+getcode(item.buyername)+"</td><td>"+getcode(item.credatedate)+"</td><td>"+getcode(item.refungood)
                                    +"</td><td>"+getcode(item.factorage)+"</td><td>"+getcode(item.shopName)+"</td>";
                            if(item.id){
                                html2=html2+"<td><a href='javascript:void(0)' onclick='showmj(\""+item.id+"\")'>查看<a></td>"
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
                    alert("亲系统繁忙，请稍后再试");
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
        //查看被选择人资质
        function showmj(id){
            var url = "${ctx}/customerservice/showdetail?id="+id;
            var title = "退款或售后协议";
            var id = "退款或售后协议";
            parent.openTab(url,title,id);
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
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/customerservice/customerserviceshlist/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${map.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${map.page.pageSize}" />
            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label"  for="tkcodeid" title="涉及订单">
                        退款编码：
                    </label>
                    <input name="tkcode" id="tkcodeid" style="width:50%" type="text" class="form-control" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="buyerNumid" title="创建者账号">
                        买家账号：
                    </label>
                    <input name="buyerNum" id="buyerNumid" style="width:50%" type="text" class="form-control" />
                </div>

            </div>

            <div class="row-fluid" style="margin-top:8px ">
                <div class="span5">
                    <label class="label-left control-label" for="tkstaceid" title="投诉类型">
                        退款状态：
                    </label>
                    <select name="tkstace" id="tkstaceid"  class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${map.tkstace}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="orderid" title="投诉状态">
                        订单号：
                    </label>
                    <input name="orderid" id="orderid" style="width:50%" type="text" class="form-control" />
                </div>

            </div>
            <div class="row-fluid" style="margin-top:8px ">
                <div class="span5">
                    <label class="label-left control-label" for="createTimeBegin" title="下单时间">
                        申请时间：
                    </label>
                    <input name="createTimeBegin" id="createTimeBegin" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createTimeEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                    至
                    <input name="createTimeEnd" id="createTimeEnd" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createTimeBegin\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
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
        <table id="contentTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
            <thead>
            <tr>
                <th width="5%">序号</th>
                <th width="14%">退款编码</th>
                <th width="11%">退款状态</th>
                <th width="12%">订单号</th>
                <th width="9%">买家账号</th>
                <th width="14%">申请时间</th>
                <th width="7%">退款金额</th>
                <th width="7%">手续费</th>
                <th width="14%">处理卖家</th>
                <th width="7%">查看详情</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${map.page.list}" var="item" varStatus="s">
                <tr>
                    <td><c:out value="${s.count}" /></td>
                    <td>${item.thcode}</td>
                    <td>${item.statename}</td>
                    <td>${item.orderid}</td>
                    <td>${item.buyername}</td>
                    <td>${item.credatedate}</td>
                    <td>${item.refungood}</td>
                    <td>${item.factorage}</td>
                    <td>${item.shopName}</td>
                    <td>
                        <c:if test="${not empty item.id}">
                            <a onclick="showmj('${item.id}')" href="javascript:void(0)">查看</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${map.page}</div>
    </div>
</div>
</body>
</html>
