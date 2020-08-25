<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>轮播图设置</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<!-- ECharts单文件引入 -->
	<script src="${ctxStatic}/echarts/dist/echarts.js" type="text/javascript"></script>
	<script type="text/javascript">
	//路径配置
	require.config({
	    paths: {
	        echarts: '${ctxStatic}/echarts/dist'
	    }
	});
	// 使用
    require(
        [
            'echarts',
            'echarts/chart/line' //使用模块，按需加载
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('myChart')); 
            
            var option = {
           		title : {
           	        text: '数据图'
           	    },
           		tooltip : {
           	        trigger: 'axis'
           	    },
           	    legend: {
           	        data:['激活用户数量','认证商家总数','买家总数','卖家/店铺数量']
           	    },
           	    xAxis : [
           	        {
           	            type : 'category',
           	            boundaryGap : false,
           	            data : ['2015-04-02','2015-04-03','2015-04-04','2015-04-05','2015-04-06','2015-04-07','2015-04-08']
           	        }
           	    ],
           	    yAxis : [
           	        {
           	            type : 'value'
           	        }
           	    ],
           	    series : [
           	        {
           	            name:'激活用户数量',
           	            type:'line',
           	            data:[20, 30, 40, 50, 40, 30, 20]
           	        },
           	        {
           	            name:'认证商家总数',
           	            type:'line',
           	            data:[10, 15, 30, 50, 20, 10, 15]
           	        },
           	        {
           	            name:'买家总数',
           	            type:'line',
           	            data:[5, 10, 15, 45, 10, 3, 10]
           	        },
           	        {
           	            name:'卖家/店铺数量',
           	            type:'line',
           	            data:[5, 5, 15, 5, 10, 7, 5]
           	        }
           	    ]
            };
    
            // 为echarts对象加载数据 
            myChart.setOption(option); 
        }
    );
	</script>
</head>
<body>
	<span>公司及店铺统计</span>	
	<table id="contentTable" class="table table-striped table-bordered table-condensed" >
		<thead><tr>
			<th>激活用户数量</th>
			<th>认证商家总数</th>
			<th>买家总数</th>
			<th>卖家/店铺数量</th>
		</tr></thead>
		<tr>
			<td>854</td>
			<td>92</td>
			<td>9</td>
			<td>83</td>
		</tr>
	</table>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	<div id="myChart" style="height:400px"></div>
	<span>待审核的商家</span>	
	<table id="contentTable" class="table table-striped table-bordered table-condensed" >
		<thead><tr>
			<th>商家总数</th>
			<th>买家总数</th>
			<th>卖家总数</th>
		</tr></thead>
		<tr>
			<td>0</td>
			<td>0</td>
			<td>0</td>
		</tr>
	</table>
</body>
</html>