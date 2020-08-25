<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>确认退款</title>
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
            var page=p;
            var rows=$("#pageSize").val();
            var tkcode= $("#tkcodeid").val();
            var tkstace= $("#tkstaceid").val();
            var orderid= $("#orderid").val();
            var refundTimeBegin= $("#refundTimeBegin").val();
            var refundTimeEnd= $("#refundTimeEnd").val();
            var confirmStatus=$("#confirmStatusId").val();
            var payBank=$("#payBankId").val();
            $.ajax({
                url:"${ctx}/customerservice/customerserviceselectjson/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    tkcode:tkcode,
                    tkstace:tkstace,
                    orderid:orderid,
                    refundTimeBegin:refundTimeBegin,
                    refundTimeEnd:refundTimeEnd,
                    confirmStatus:confirmStatus,
                    payBank:payBank
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
                            //html2=html2+"<tr><td>"+num+"</td><td>"+getcode(item.thcode)+"</td><td>"+getcode(item.statename)+"</td><td>"+getcode(item.orderid)+"</td><td>"+getcode(item.refundTime)+"</td><td>"+getcode(item.refungood)
                            //        +"</td><td>"+getcode(item.factorage)+"</td><td>"+getcode(item.orderPayBank)+"</td>";
                                    
                            html2=html2+"<tr><td>"+num+"</td><td>"+getcode(item.thcode)+"</td><td>"+getcode(item.statename)+"</td><td>"+getcode(item.orderid)+"</td><td>"+getcode(item.refundTime)+"</td><td>"+getcode(item.refungood)
                                    +"</td><td>"+getcode(item.orderPayBank)+"</td>";
                                    
                            html2=html2+"<td>";                                   
                            if(item.id){
                                html2=html2+"<a onclick='showmj(\""+item.id+"\")' href='javascript:void(0)' >查看详情</a>&nbsp;&nbsp;"
                            }else{
                                html2=html2+"<td></td>"
                            }
                            if(item.confirmStatus==0){
                            	html2=html2+"<a onclick='agreeRefund(\""+item.id+"\")' href='javascript:void(0)' >处理</a>"
                            }
                            if(item.confirmStatus==2){
                            	html2=html2+"<a onclick='agreeRefund(\""+item.id+"\")' href='javascript:void(0)' >处理</a>"
                            }
                            html2=html2+"</td>";
                            html2=html2+"</tr>";
                        });
                        $("#tabletbody").html(html2);
                        $("#pageNo").val(data.obj.pageNo);
                        $("#pageSize").val(data.obj.pageSize);
                    }
                },error:function(){
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
        //查看退款协议
        function showmj(id){
            var url = "${ctx}/customerservice/showdetail?id="+id;
            var title = "退款或售后协议";
            var id = "退款或售后协议";
            parent.openTab(url,title,id);
        }
        //处理操作
        function manage(id){
        	$.jBox.confirm("确定要处理退款？","提示",function(value){
	  			if(value=='ok'){
	  				$.ajax({
	  					url : "${ctx}/customerservice/manageMent",
	  					type : "POST",
	  					data :"returnId="+id,
	  					dataType : "json",
	  					success : function(data) {
	  						agreeRefund(id);
	  					},
	  					error : function(xmlHttpRequest, textStatus, errorThrown) {
	  						$.jBox.info(data.msg);
	  					}
	  				});
	  			}
	  		});
        }
        //同意退款
        function agreeRefund(id){
        	$.jBox.confirm("确定要处理退款？","提示",function(value){
	  			if(value=='ok'){
	  				$.ajax({
	  					url : "${ctx}/customerservice/agreeRefund",
	  					type : "POST",
	  					data :"returnId="+id,
	  					dataType : "json",
	  					success : function(data) {
	  						if(data.msg && data.msg.length > 80){
	  							window.open(data.msg,"_blank");
	  							top.$.jBox.confirm("确定进行退款了么？","系统提示",function(v,h,f){
	  				                if(v == "ok"){
	  				                	updateApStatue(id);
	  				                }
	  				            },{buttonsFocus:1});
	  						}else{
	  							$.jBox.prompt(data.msg, '消息', 'info', { closed: function () { var page=$("#pageNo").val();
	  			                selectpage(page);} });
	  							$.jBox.closeTip();
	  						}
	  					},
	  					error : function(xmlHttpRequest, textStatus, errorThrown) {
	  						$.jBox.closeTip();
	  						$.jBox.info(data.msg);
	  					}
	  				});
	  			}
        	});
        }
        
		function updateApStatue(id){
			$.ajax({
				url : "${ctx}/customerservice/updateApStatue",
				type : "POST",
				data :"returnId="+id,
				dataType : "json",
				success : function(data) {
					$.jBox.prompt(data.msg, '消息', 'info', { closed: function () { var page=$("#pageNo").val();
	                selectpage(page);} });
					$.jBox.closeTip();
				},
				error : function(xmlHttpRequest, textStatus, errorThrown) {
					$.jBox.closeTip();
					$.jBox.info(data.msg);
				}
			});
			
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
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/customerservice/confirmRefundList/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${map.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${map.page.pageSize}" />
            <input name="confirmStatus" id="confirmStatusId" type="hidden" class="form-control" value="0"/>
            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label"  for="tkcodeid" title="涉及订单">
                        退款编码：
                    </label>
                    <input name="tkcode" id="tkcodeid" style="width:50%" type="text" class="form-control" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="payBankId" title="投诉类型">
                        支付渠道：
                    </label>
                    <select name="tkstace" id="payBankId"  class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${map.payBankList}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row-fluid" style="margin-top:10px;">
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
                       同意退款时间：
                    </label>
                    <input name="refundTimeBegin" id="refundTimeBegin" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'refundTimeEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                    至
                    <input name="refundTimeEnd" id="refundTimeEnd" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'refundTimeBegin\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                </div>
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
                <th width="12%">退款状态</th>
                <th width="12%">订单号</th>
                <th width="12%">卖家同意退款时间</th>
                <th width="7%">退款金额</th>
                <!-- <th width="7%">补足手续费</th> -->
                <th width="7%">支付渠道</th>
                <th width="8%">操作</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${map.page.list}" var="item" varStatus="s">
                <tr>
                    <td><c:out value="${s.count}" /></td>
                    <td>${item.thcode}</td>
                    <td>${item.statename}</td>
                    <td>${item.orderid}</td>
                    <td>${item.refundTime}</td>
                    <td>${item.refungood}</td>
                    <%-- <td>${item.factorage}</td> --%>
                    <td>${item.orderPayBank}</td>
                    <td>
                        <c:if test="${not empty item.id}">
                            <a onclick="showmj('${item.id}')" href="javascript:void(0)">查看详情</a>
                        </c:if>
                        <c:if test="${item.confirmStatus eq '0'}">
                        <a onclick="agreeRefund('${item.id}')" href="javascript:void(0)">处理</a>
                        </c:if>
                        <c:if test="${item.confirmStatus eq '2'}">
                        <a onclick="agreeRefund('${item.id}')" href="javascript:void(0)">处理</a>
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
