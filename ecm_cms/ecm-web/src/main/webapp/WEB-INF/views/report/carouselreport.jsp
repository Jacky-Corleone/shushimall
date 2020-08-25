<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>轮播图报表</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <%@include file="/WEB-INF/views/include/baobiao.jsp" %>
    <script type="text/javascript">
    $(document).ready(function() {
        // 表格排序
        //tableSort({callBack : page});
        $("#btnExport").click(function(){
        	var page=1;
            var rows=$("#pageSize").val();
            var createTimeBegin=$("#createTimeBegin").val();
            var createTimeEnd =$("#createTimeEnd").val();
            $.ajax({
                url:"${ctx}/carouselreport/selectPageList/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    clickDateBegin:createTimeBegin,
                    clickDateEnd:createTimeEnd
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var count=data.obj.count;
                        if(count>0){
                        top.$.jBox.confirm("确认要导出当前页数据吗？","系统提示",function(v,h,f){
                            if(v == "ok"){
                                $("#searchForm").attr("action","${ctx}/carouselreport/exportCurrentpage").submit();
                            }
                        },{buttonsFocus:1});
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                        }
                    }
                }
            })
        });
    });
    
    
        $(document).ready(function() {
            //初始化图形报表
            //queryshoptotal('main');
            //queryshoptotal1('main1');
        });
        function unload(){
        	 $("#createTimeBegin").val("");
         	 $("#createTimeEnd").val("");
         	 $("#pageNo").val(1);
			 $("#pageSize").val(10);
          	 $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
          	 $("#searchForm").attr("action","${ctx}/carouselreport/carousellist").submit();
          }
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
			$("#searchForm").attr("action","${ctx}/carouselreport/carousellist").submit();
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
			$("#searchForm").attr("action","${ctx}/carouselreport/carousellist").submit();
        }
           function exportall(){
        	   
	           var size=500;
               var page=1;
               var rows=$("#pageSize").val();
               var createTimeBegin=$("#createTimeBegin").val();
               var createTimeEnd =$("#createTimeEnd").val();
               $.ajax({
                   url:"${ctx}/carouselreport/selectPageList/",
                   type:"post",
                   data:{
                       page:page,
                       rows:rows,
                       clickDateBegin:createTimeBegin,
                       clickDateEnd:createTimeEnd
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
                   var tiao1=(page-1)*size+1;
                   var tiao2=tiao1+size-1;
                   if(tiao2>count){
                       tiao2=count;
                   }
                   var qurenmsg="确认要导出"+tiao1+"~"+tiao2+"的数据吗?"
                   top.$.jBox.confirm(qurenmsg,"系统提示",function(v,h,f){
                       if(v=='ok'){
                           $("#searchForm").attr("action","${ctx}/carouselreport/exportCurrentpage").submit();
                           page=page+1;
                           callback(up,size,page,count,callback);
                       }else{
                           page=page+1;
                           callback(up,size,page,count,callback);
                       }
                   },{buttonsFocus:1});
               }
           }
        function selectpage(p){
            var page=p;
            var rows=$("#pageSize").val();
            var createTimeBegin=$("#createTimeBegin").val();
            var createTimeEnd =$("#createTimeEnd").val();
            $.ajax({
                url:"${ctx}/carouselreport/selectPageList/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    createTimeBegin:createTimeBegin,
                    createTimeEnd:createTimeEnd
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var html="<div class='pagination'>"+data.msg+"</div>";
                        $(".pagination").replaceWith(html);
                        var html2="";
                        $(data.obj.list).each(function(i,item){
                            html2=html2+"<tr><td>"+getcode(item.uno)+"</td><td>"+getcode(item.clickDate)+"</td><td>"+getcode(item.AdName)+"</td><td>"+getcode(item.AdType)+"</td><td>"+getcode(item.adCount)+"</td>"
                            html2=html2+"</tr>";
                        });
                        $("#tabletbody").html(html2);
                        $("#pageNo").val(date.obj.pageNo);
                        $("#pageSize").val(date.obj.pageSize);
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
        }
        function queryshoptotal(divid,them){
            var monthtime=$("#shopdateinputid").val();
            $.ajax({
                url:"${ctx}/carouselreport/carouseltotal/",
                type:"post",
                data:{
                    monthtime:monthtime
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var axList=data.obj.axList;
                        var dataList=data.obj.datalist;
                        var namelist=data.obj.namelist;
                        showbb(axList,dataList,namelist,divid,them);
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(){
                    $.jBox.info("亲系统繁忙，请稍后再试");
                }
            });
        }
        function showbb(axList,dataList,namelist,divid,them) {
            var chartContainer = document.getElementById(divid);
            myChart = echarmen.init(chartContainer);
            myChart.setTheme(them);
            var option = {
                title : {
                    text: '轮播图点击量量',
                    subtext: 'ecm管理平台'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:namelist
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
                series : dataList
            };
            myChart.setOption(option);
        }
    function shopzx(){
        var nowtime= $("#shopdateinputid").val();
        if(beforetime&&beforetime===nowtime){
            return false;
        }
        beforetime=nowtime;
        queryshoptotal('main',thems);
    }
    function changethem(){
        var themname=$("#themeid").val();
        if(themname){
            require(['${ctxStatic}/echarts/theme/'+ themname], function(tarTheme){
                thems = tarTheme;
                myChart.setTheme(tarTheme);
            });
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
<ul class="nav nav-tabs">
		<li><a href="${ctx}/advertisementreport/advertisementlist">广告类型</a></li>
		<li class="active"><a href="${ctx}/carouselreport/carousellist">轮播图</a></li>
	</ul>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/carouselreport/carousellist" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="row-fluid">
                <div class="span5">
                    <label class="label-left control-label" for="createTimeBegin" title="创建时间">
                    点击时间：
                    </label>
                    <input name="clickDateBegin" id="createTimeBegin" value="${mallAdCountDTO.clickDateBegin}" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createTimeEnd\')||\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd',isShowClear:true});" style="width: 25%" type="text" class="input-small Wdate" />
                    至
                    <input name="clickDateEnd" id="createTimeEnd" value="${mallAdCountDTO.clickDateEnd}" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createTimeBegin\')}',dateFmt:'yyyy-MM-dd',isShowClear:true,maxDate: '%y-%M-%d'});" style="width: 25%" type="text" class="input-small Wdate" />
                </div>
            </div>
            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label" ></label>
                    <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="pagesub()" value="查询" />
                    <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="unload();" value="重置" />
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
                <th width="30%">日期</th>
                <th width="35%">广告名称</th>
                <th width="15%">广告类型</th>
                <th width="10%">点击次数</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${page.list}" var="user">
                <tr>
                    <td>${user.uno}</td>
                    <td>${user.clickDate}</td>
                    <td>${user.AdName}</td>
                    <td>轮播图</td>
                    <td>${user.adCount}</td>
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
        </div> -->
    </div>
</div>
</body>
</html>
