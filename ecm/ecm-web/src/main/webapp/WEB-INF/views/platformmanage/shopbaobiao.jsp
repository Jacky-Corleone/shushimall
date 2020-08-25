<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>店铺报表</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <%@include file="/WEB-INF/views/include/baobiao.jsp" %>
    <script type="text/javascript">
    $(document).ready(function() {
        // 表格排序
        $("#btnExport").click(function(){
        	var page=1;
            var rows=$("#pageSize").val();
            var shopName=$("#shopName").val();
            var start=$("#start").val();
            var end=$("#end").val();
            $.ajax({
                url:"${ctx}/shopbaobiao/selectPageList/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    shopName:shopName,
                    passTimeStart:start,
                    passTimeEnd:end
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var count=data.obj.count;
                        if(count>0){
                        	top.$.jBox.confirm("确认要导出当前页数据吗？","系统提示",function(v,h,f){
                                if(v == "ok"){
                                    $("#searchForm").attr("action","${ctx}/shopbaobiao/exportCurrentpage").submit();
                                }
                            },{buttonsFocus:1});
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                        }
                    }
                }
            })
        });
    });
    function pagesub(n,s){
		if(n>0){
			
		}else{
			n =1;
		}
		if(s>0){
			
		}else{
			s =10;
		}
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		 $("#searchForm").attr("action","${ctx}/shopbaobiao/shopbaobiao").submit();
}
    
    function page(n,s){
        if(n>0){
			
		}else{
			n =1;
		}
		if(s>0){
			
		}else{
			s =10;
		}
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		 $("#searchForm").attr("action","${ctx}/shopbaobiao/shopbaobiao").submit();
    }
    function exportall(){
        var size=500;
        var page=1;
        var rows=$("#pageSize").val();
        var shopName=$("#shopName").val();
        var start=$("#start").val();
        var end=$("#end").val();
        $.ajax({
            url:"${ctx}/shopbaobiao/selectPageList/",
            type:"post",
            data:{
                page:page,
                rows:rows,
                shopName:shopName,
                passTimeStart:start,
                passTimeEnd:end
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
                    $("#searchForm").attr("action","${ctx}/shopbaobiao/exportCurrentpage").submit();
                    page=page+1;
                    callback(up,size,page,count,callback);
                }else{
                    page=page+1;
                    callback(up,size,page,count,callback);
                }
            },{buttonsFocus:1});
        }
    }
    
    
        function unload(){
	        	$("#shopName").val("");
	            $("#start").val("");
	            $("#end").val("");
	            $("#pageNo").val(1);
	            $("#pageSize").val(10);
        	 $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
        	 $("#searchForm").attr("action","${ctx}/shopbaobiao/shopbaobiao").submit();
        }
        /* function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            selectpage(n);
            return false;
        } */
       /*  function selectpage(p){
            var page=p;
            var rows=$("#pageSize").val();
            var shopName= $("#shopName").val();
            var start=$("#start").val();
            var end=$("#end").val();
            $.ajax({
                url:"${ctx}/shopbaobiao/ajaxlist/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    shopName:shopName,
                    start:start,
                    end:end
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var html="<div class='pagination'>"+data.msg+"</div>";
                        $(".pagination").replaceWith(html);
                        var html2="";
                        $(data.obj.list).each(function(i,item){
                            html2=html2+"<tr><td>"+getcode(item.uno)+"</td><td>"+getcode(item.shopName)+"</td><td>"+getcode(item.paseTime)+"</td><td>"+getcode(item.orderNum)+"</td><td>"+getcode(item.saleNum)+"</td><td>"+getcode1(item.phoneNum)
                                    +"</td><td>"+getcode(item.customNum)+"</td>"
                            html2=html2+"</tr>";
                        });
                        $("#tabletbody").html(html2);
                        $("#pageNo").val(date.obj.pageNo);
                        $("#pageSize").val(date.obj.pageSize);
                    }
                },error:function(){
                    $.jBox.info("系统繁忙，请稍后再试");
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
        function getcode1(date){
            if(date!='null'){
                return date;
            }else{
                return '';
            }
        } */
       /*  //查看被选择人资质
        function showmj(uid){
            var url = "${ctx}/sellerdetail/sellerdetail?uid="+uid;
            var title = "卖家明细";
            var id = "卖家明细";
            parent.openTab(url,title,id);
        }
        function showmjj(uid){
            var url = "${ctx}/sellerdetail/bullerdetail?uid="+uid;
            var title = "买家明细";
            var id = "买家明细";
            parent.openTab(url,title,id);
        }
        var echarmen;
        var themc;
        var myChart;
        var myChart1;
        var beforetime;
        require.config({
            paths: {
                 'echarts': '${ctxStatic}/echarts/echarts',
                'echarts/theme/default':'${ctxStatic}/echarts/theme/default',
                'echarts/chart/line':'${ctxStatic}/echarts/chart/line',
                'echarts/chart/bar':'${ctxStatic}/echarts/chart/bar'
            }
        });
        require(
                [
                    'echarts',
                    'echarts/theme/default',
                    'echarts/chart/line',
                    'echarts/chart/bar'
                ],
                //回调函数
                initechart
        );
        function initechart(en,theme){
            echarmen=en;
            thems=theme;
            queryshoptotal('main',thems);
            queryshoptotal1('main1',thems);
        }
        function queryshoptotal(divid,them){
            var monthtime=$("#shopdateinputid").val();
            $.ajax({
                url:"${ctx}/shopbaobiao/shoptotalzx/",
                type:"post",
                data:{
                    monthtime:monthtime
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var axList=data.obj.axList;
                        var dataList=data.obj.dataList;
                        var axarry=new Array();
                        var dataarry=new Array();
                        if(axList){
                            $(axList).each(function(i,item){
                                 axarry.push(item);
                            });
                        }
                        if(dataList){
                            $(dataList).each(function(i,item){
                                dataarry.push(item);
                            });
                        }
                        showbb(axarry,dataarry,divid,them);
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(){
                    $.jBox.info("亲系统繁忙，请稍后再试");
                }
            });
        }
        function showbb(axList,dataList,divid,them) {
            var chartContainer = document.getElementById(divid);
            myChart = echarmen.init(chartContainer);
            myChart.setTheme(them);
            var option = {
                title : {
                    text: '累计店铺数量',
                    subtext: 'ecm管理平台'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:['累计店铺数量']
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : axList
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'累计店铺数量',
                        type:'line',
                        stack: '总量',
                        data:dataList
                    }
                ]
            };
            myChart.setOption(option);
        }
        function queryshoptotal1(divid,them){
            var monthtime=$("#shopdateinputid").val();
            $.ajax({
                url:"${ctx}/shopbaobiao/shopaddcount/",
                type:"post",
                data:{
                    monthtime:monthtime
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var axList=data.obj.axList;
                        var dataList=data.obj.dataList;
                        var axarry=new Array();
                        var dataarry=new Array();
                        if(axList){
                            $(axList).each(function(i,item){
                                axarry.push(item);
                            });
                        }
                        if(dataList){
                            $(dataList).each(function(i,item){
                                dataarry.push(item);
                            });
                        }
                        showbb1(axarry,dataarry,divid,them);
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(){
                    $.jBox.info("亲系统繁忙，请稍后再试");
                }
            });
        }
        function showbb1(axList,dataList,divid,them) {
            var chartContainer = document.getElementById(divid);
            myChart1 = echarmen.init(chartContainer);
            myChart1.setTheme(them);
            var option = {
                title : {
                    text: '店铺新增数量',
                    subtext: 'ecm管理平台'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:['店铺新增']
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : axList
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'店铺新增',
                        type:'line',
                        stack: '总量',
                        data:dataList
                    }
                ]
            };
            myChart1.setOption(option);
        }
    function shopzx(){
        var nowtime= $("#shopdateinputid").val();
        if(beforetime&&beforetime===nowtime){
            return false;
        }
        beforetime=nowtime;
        queryshoptotal('main',thems);
        queryshoptotal1('main1',thems);
    }
    function changethem(){
        var themname=$("#themeid").val();
        if(themname){
            require(['${ctxStatic}/echarts/theme/'+ themname], function(tarTheme){
                thems = tarTheme;
                myChart.setTheme(tarTheme);
                myChart1.setTheme(tarTheme);
            });
        }
   } */ 
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
            width: 20%;
            text-align: right;
        }
        div.div-w{
            width:20%;
        }
        table.td-cen th,.td-cen td{
            text-align: center;
        }.hhtd td{
             word-wrap:break-word;
             word-break:break-all;
         }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form:form id="searchForm" modelAttribute="shopReportDTO" action="${ctx}/shopbaobiao/shopbaobiao/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="row-fluid">
                <div class="span5">
                    <label class="label-left control-label"  for="shopName" title="店铺名称">
                        店铺名称：
                    </label>
                    <input name="shopName" id="shopName" value="${shopReportDTO.shopName}"   style="width:50%" type="text" class="form-control" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="start" title="创建时间">
                        创建时间：
                    </label>
                    <input name="passTimeStart" id="start" readonly="readonly" value="${shopReportDTO.passTimeStart }" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'end\')}',dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width: 25%" type="text" class="input-small Wdate" />
                    至
                    <input name="passTimeEnd" id="end" readonly="readonly" value="${shopReportDTO.passTimeEnd }" onclick="WdatePicker({minDate:'#F{$dp.$D(\'start\')}',dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width: 25%" type="text" class="input-small Wdate" />
                </div>
            </div>
            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label" ></label>
                    <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="pagesub()" value="查询" />
                    <input id="unset"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="unload()" value="重置" />
                </div>
                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btnExport"  class="btn  btn-primary" style="width: 26%;" type="button" value="导出当前页"  />
                    <input id="btnExportALL"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="exportall()" value="导出全部"  />
                </div>
            </div>
        </form:form>
        <table id="contentTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
            <thead>
            <tr>
                <th width="10%">序号</th>
                <th width="23%">店铺名称</th>
                <th width="17%">开店时间</th>
                <th width="10%">订单总数</th>
                <th width="10%">销量</th>
                <th width="15%">联系方式</th>
                <th width="15%">客户数</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${page.list}" var="shop">
                <tr>
                    <td>${shop.uno}</td>
                    <td>${shop.shopName}</td>
                    <td>${shop.paseTime}</td>
                    <td>${shop.orderNum}</td>
                    <td>${shop.saleNum}</td>
                    <td>
                    <c:if test="${shop.phoneNum!='null'}">
                             ${shop.phoneNum}
                    </c:if>
                    </td>
                    <td>${shop.customNum}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${page}</div>
        <!-- <div id="shopdatediv" class="row-fluid">
            <div class="span5">
            <label class="label-left control-label"  for="shopdateinputid" title="选择月份">
                选择月份：
            </label>
            <input  name="createTime" id="shopdateinputid" readonly="readonly" onclick="WdatePicker({onpicked:function(){shopzx();},lang:'zh-cn',dateFmt:'yyyy-MM'});" style="width:45%" type="text" class="input-small Wdate" />
            </div>
        </div>
        <div id="themediv" class="row-fluid">
            <div class="span5">
                <label class="label-left control-label"  for="themeid" title="切换主题">
                    切换主题：
                </label>
                <select name="theme" id="themeid" class="input-medium" onchange="changethem()">
                    <option value="default">默认</option>
                    <option value="dark">深色</option>
                    <option value="blue">蓝色</option>
                    <option value="gray">灰色</option>
                    <option value="green">绿色</option>
                    <option value="helianthus">向日葵</option>
                    <option value="infographic">图表</option>
                    <option value="macarons">macarons</option>
                    <option value="red">红色</option>
                    <option value="shine">深红</option>
                </select>
            </div>
        </div>
        <div id="graphic" class="col-md-8" style="margin-top:30px;margin-left:30px;width: 900px;height:400px;background-color: #f5f5f5;">
            <div id="main" class="main" style="width: 890px;height:382px;"></div>
        </div>
        <div id="graphic1" class="col-md-8" style="margin-top:50px;margin-left:30px;width: 900px;height:400px;background-color: #f5f5f5;">
            <div id="main1" class="main" style="width: 890px;height:382px;"></div>
        </div> -->
    </div>
</div>
</body>
</html>
