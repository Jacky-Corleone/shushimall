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
    <%@ include file="/WEB-INF/views/include/baobiao.jsp" %>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
        /*$(function () {
            $('#container').highcharts({
                title: {
                    text: '新增用户统计',
                    x: -20 //center
                },
*//*                credits: {
                    text: 'camelot.cn',
                    href: 'http://www.baidu.cn'
                },*//*
                subtitle: {
                    text: '前六天按天统计',
                    x: -20
                },
                xAxis: {
                    title:{
                        text: '日期(日)'
                    },
                    categories: ['03-05', '03-06', '03-07', '03-08', '03-09', '03-10']
                },
                yAxis: {
                    title: {
                        text: '数量 (家)'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                },
                tooltip: {
                    valueSuffix: '个'
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle',
                    borderWidth: 0
                },
                series: [{
                    name: '认证商家总数',
                    data: [7.0, 6.9, 10, 14.5, 18.2, 21.5]
                }, {
                    name: '激活用户数量',
                    data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0]
                }, {
                    name: '买家总数',
                    data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0]
                }, {
                    name: '卖家总数',
                    data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2]
                }]
            });
        });*/
        $(function () {
            $.jqplot('container', [[1,2,3,4,5,6,7,8,9]]);
        });
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
        div.y-box {
            width: 95%;
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
        }.h3-current{
             width:160px;
             height: 36px;
             text-align: center;
         }
        button.btn-bac{
            background: #ccc;;
        }
        a.y-hx {
            text-decoration:none;
        }
        ul.ul1{
            display:inline;
            list-style:none;
            display:block;
        }
        li.li1{
            margin-bottom: 10px;
            margin-right: 20px;
            float:left;
            display:block;
        }
        span.gray-color{
            color: #999;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
        <div class="y-box">
            <h3 class="h3">公司及店铺统计</h3>
            <table id="contentTable1" class="y-tb1 z-tbl mb20">
                <colgroup>
                    <col width="25%">
                    <col width="25%">
                    <col width="25%">
                    <col width="25%">
                </colgroup>
                <thead>
                <tr>
                    <th style="background: #fbfaf8;">认证商家总数</th>
                    <th style="background: #fbfaf8;">激活用户数量</th>
                    <th style="background: #fbfaf8;">买家总数</th>
                    <th style="background: #fbfaf8;">卖家/店铺数量</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                </tr>
                </tbody>
            </table>
            <h3 class="h3">
                图形统计
            </h3>
            <div style="width:100%;height: 500px;">
                <div id="container" style="min-width: 100%; height: 400px; margin: 0 auto"></div>
            </div>
            <h3 class="h3">待审核的商家</h3>
            <table id="contentTable2" class="y-tb1 z-tbl mb20">
                <colgroup>
                    <col width="34%">
                    <col width="33%">
                    <col width="33%">
                </colgroup>
                <thead>
                <tr>
                    <th style="background: #fbfaf8;">商家总数</th>
                    <th style="background: #fbfaf8;">买家总数</th>
                    <th style="background: #fbfaf8;">卖家总数</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
