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
    <title>投诉仲裁</title>
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
            var orderNum= $("#orderNumid").val();
            var buyerNum=$("#buyerNumid").val();
            var type=$("#typeid").val();
            var flag=$("#flagid").val();
            var createTimeBegin=$("#createTimeBegin").val();
            var createTimeEnd=$("#createTimeEnd").val();
            var complaintype=$("#complaintypeid").val();
            $.ajax({
                url:"${ctx}/complain/complainlistjson/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    orderNum:orderNum,
                    buyerNum:buyerNum,
                    type:type,
                    flag:flag,
                    createTimeBegin:createTimeBegin,
                    createTimeEnd:createTimeEnd,
                    complaintype:complaintype
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
                            html2=html2+"<tr><td>"+num+"</td><td>"+getcode(item.tsnr)+"</td><td>"+getcode(item.typeName)+"</td><td>"+getcode(item.tsf)+"</td><td>"+getcode(item.bjf)+"</td><td>"+getcode(item.buyerNum)+"</td><td>"+getcode(item.sellerNum)+"</td><td>"+getcode(item.shopName)+"</td><td>"+getcode(item.orderNum)+"</td><td>"+getcode(item.createTime)
                                    +"</td><td>"+getcode(item.flagName)+"</td>"
                                if(item.flag=='0'){
                                    html2=html2+"<td><a href='javascript:void(0)' onclick='showmj(\""+item.id+"\",\""+item.refundId+"\",\""+item.complainGroup+"\")'>仲裁<a></td>"
                                }else if(item.flag=='1'||item.flag=='2'){
                                    html2=html2+"<td><a href='javascript:void(0)' onclick='showmjj(\""+item.id+"\",\""+item.refundId+"\",\""+item.complainGroup+"\")'>查看<a></td>"
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
        function showmj(id,refundId,complainGroup){
            var url = "${ctx}/complain/complaindetail?id="+id+"&refundId="+refundId+"&complainGroup="+complainGroup+"&flag=0";
            var title = "办理仲裁";
            var id = "办理仲裁";
            parent.openTab(url,title,id);
        }
        //查看被选择人资质
        function showmjj(id,refundId,complainGroup){
            var url = "${ctx}/complain/complaindetail?id="+id+"&refundId="+refundId+"&complainGroup="+complainGroup+"&flag=1";
            var title = "仲裁明细";
            var id = "仲裁明细";
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
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/complain/complainlist/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${map.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${map.page.pageSize}" />
            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label"  for="orderNumid" title="涉及订单">
                        涉及订单：
                    </label>
                    <input name="orderNum" id="orderNumid" style="width:50%" type="text" class="form-control" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="buyerNumid" title="买家账号">
                        买家账号：
                    </label>
                    <input name="buyerNum" id="buyerNumid" style="width:50%" type="text" class="form-control" />
                </div>

            </div>
            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label" for="typeid" title="投诉类型">
                        投诉类型：
                    </label>
                    <select name="type" id="typeid" style="width:27%" class="form-control">
                        <option value="">请选择</option>
                        <option value="1">退款申请</option>
                        <option value="2">申请售后</option>
                    </select>
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="flagid" title="投诉状态">
                        投诉状态：
                    </label>
                    <select name="flag" id="flagid" style="width:27%" class="form-control">
                        <option value="">请选择</option>
                        <option value="0">待仲裁</option>
                        <option value="1">已仲裁</option>
                        <option value="2">已撤销</option>
                    </select>
                </div>
            </div>

            <div class="row-fluid" style="margin-top:8px ">

                <div class="span5">
                    <label class="label-left control-label" for="createTimeBegin" title="下单时间">
                        申请时间：
                    </label>
                    <input name="createTimeBegin" id="createTimeBegin" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createTimeEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width: 25%" type="text" class="input-small Wdate" />
                    至
                    <input name="createTimeEnd" id="createTimeEnd" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createTimeBegin\')}',dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width: 25%" type="text" class="input-small Wdate" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="complaintypeid" title="投诉方">
                        投诉方：
                    </label>
                    <select name="complaintype" id="complaintypeid" style="width:27%" class="form-control">
                        <option value="">请选择</option>
                        <option value="1">买家</option>
                        <option value="2">卖家</option>
                    </select>
                </div>
            </div>
            <div class="row-fluid" style="margin-top:8px ">
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
                <th width="3%">序号</th>
                <th width="10%">投诉内容</th>
                <th width="7%">类型</th>
                <th width="7%">投诉方</th>
                <th width="7%">辩解</th>
                <th width="9%">买家账号</th>
                <th width="9%">卖家账号</th>
                <th width="11%">店铺名称</th>
                <th width="11%">涉及订单</th>
                <th width="12%">提交时间</th>
                <th width="7%">状态</th>
                <th width="6%">操作</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${map.page.list}" var="item" varStatus="s">
                <tr>
                    <td><c:out value="${s.count}" /></td>
                    <td>${item.tsnr}</td>
                    <td>${item.typeName}</td>
                    <td>${item.tsf}</td>
                    <td>${item.bjf}</td>
                    <td>${item.buyerNum}</td>
                    <td>${item.sellerNum}</td>
                    <td>${item.shopName}</td>
                    <td>${item.orderNum}</td>
                    <td>${item.createTime}</td>
                    <td>${item.flagName}</td>
                    <td>
                        <c:choose>
                    		<c:when test="${item.flag=='0'}">
                    			<a onclick="showmj('${item.id}','${item.refundId }','${item.complainGroup }')" href="javascript:void(0)">仲裁</a>
                    		</c:when>
                    		<c:otherwise>
                    			 <a onclick="showmjj('${item.id}','${item.refundId}','${item.complainGroup }')" href="javascript:void(0)">查看</a>
                    		</c:otherwise>
                    	</c:choose>
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
