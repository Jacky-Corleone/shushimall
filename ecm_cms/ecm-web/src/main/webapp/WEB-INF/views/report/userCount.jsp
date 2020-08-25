<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>商家入驻统计</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <%@include file="/WEB-INF/views/include/baobiao.jsp" %>
    <script>
        var echart;
        $(document).ready(function() {
            require.config({
                paths: {
                    echarts: '${ctxStatic}/echarts/echarts',
                    'echarts/chart/line':'${ctxStatic}/echarts/chart/line',
                    'echarts/chart/bar':'${ctxStatic}/echarts/chart/bar'
                }
            });
            require(
                    [
                        'echarts',
                        'echarts/chart/line',
                        'echarts/chart/bar'
                    ],
                    //回调函数
                    initechart
            );


        });
        function initechart(echart){
            var myChart = echart.init(document.getElementById('mainChart'));

            // 过渡---------------------
            myChart.showLoading({
                text: '正在努力的读取数据中...'  //loading话术
            });

            // ajax getting data...............
            $.ajax({
                url:"${ctx}/report/userCountAjax",
                data:"",
                dataType:"json",
                success:function(data){
                    var xAxis = [];
                    var userAll = [];
                    var activeAll = [];
                    var buyerAll = [];
                    var sellerAll = [];
                    $.each(data.list,function(i,item){
                        xAxis.push(convertDate(new Date(item.createDt)));
                        userAll.push(item.authCustomerNum);
                        activeAll.push(item.activeCustomerNum);
                        buyerAll.push(item.buyerNum);
                        sellerAll.push(item.sellerNum);
                    });
                    // ajax callback
                    myChart.hideLoading();

                    // 图表使用-------------------
                    var option = {
                        legend: {                                   // 图例配置
                            padding: 5,                             // 图例内边距，单位px，默认上下左右内边距为5
                            itemGap: 10,                            // Legend各个item之间的间隔，横向布局时为水平间隔，纵向布局时为纵向间隔
                            data: ['认证商家总数', '激活用户数量','买家总数','卖家/店铺数量']

                        },
                        tooltip: {                                  // 气泡提示配置
                            trigger: 'item'                      // 触发类型，默认数据触发，可选为：'axis'
                        },
                        xAxis: [                                    // 直角坐标系中横轴数组
                            {
                                type: 'category',                   // 坐标轴类型，横轴默认为类目轴，数值轴则参考yAxis说明
                                data: xAxis
                            }
                        ],
                        yAxis: [                                    // 直角坐标系中纵轴数组
                            {
                                type: 'value',                      // 坐标轴类型，纵轴默认为数值轴，类目轴则参考xAxis说明
                                boundaryGap: [0.1, 0.1],            // 坐标轴两端空白策略，数组内数值代表百分比
                                splitNumber: 4                      // 数值轴用，分割段数，默认为5
                            }
                        ],
                        series: [
                            {
                                name: '认证商家总数',                        // 系列名称
                                type: 'line',                       // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
                                data: userAll
                            },
                            {
                                name: '激活用户数量',                    // 系列名称
                                type: 'line',                       // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
                                data: activeAll
                            },
                            {
                                name: '买家总数',                        // 系列名称
                                type: 'line',                       // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
                                data: buyerAll
                            },
                            {
                                name: '卖家/店铺数量',                    // 系列名称
                                type: 'line',                       // 图表类型，折线图line、散点图scatter、柱状图bar、饼图pie、雷达图radar
                                data: sellerAll
                            }
                        ]
                    };
                    myChart.setOption(option);
                }

            });

        }

    </script>
    <script type="text/javascript">

        function convertDate(date){
            if(date!=null){
                var year = date.getFullYear();
                var month = (date.getMonth()+1)<10?'0'+(date.getMonth()+1):(date.getMonth()+1);
                var date1 = date.getDate()<10?'0'+date.getDate():date.getDate();

                return  year+"-"+month+"-"+date1;
            }else{
                return "";
            }

        }
    </script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">

        </div>
        <div class="container-fluid">
            <legend ><span class="content-body-bg">公司及店铺统计</span></legend>
            <table id="treeTable" class="table table-striped table-bordered table-condensed">
                <tr>
                    <th>认证商家总数</th>
                    <th>激活用户数量</th>
                    <th>买家总数</th>
                    <th>卖家/店铺数量</th>
                </tr>
                <tr>
                    <td>${userCount.authCustomerNum}</td>
                    <td>${userCount.activeCustomerNum}</td>
                    <td>${userCount.buyerNum}</td>
                    <td>${userCount.sellerNum}</td>
                </tr>
            </table>
        </div>
        <div class="container-fluid">
            <div id="mainChart" style="width: 800px;height:400px;"></div>
        </div>
        <div class="container-fluid">
            <legend ><span class="content-body-bg">待审核商家</span></legend>
            <table id="treeTable2" class="table table-striped table-bordered table-condensed">
                <tr>
                    <th>商家总数</th>
                    <th>买家总数</th>
                    <th>卖家总数</th>
                </tr>
                <tr>
                    <td>${userCount.auditCustomer}</td>
                    <td>${userCount.auditBuyerNum}</td>
                    <td>${userCount.auditSellerNum}</td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>
