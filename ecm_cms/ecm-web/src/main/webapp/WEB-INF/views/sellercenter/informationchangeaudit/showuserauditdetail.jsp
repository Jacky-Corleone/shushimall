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
        }.z-btn {
             display: inline-block;
             background: #00a2c9;
             color: #fff;
             width: 140px;
             height: 36px;
             line-height: 36px;
             text-align: center;
             border-radius: 2px;
             -moz-border-radius: 2px;
             -webkit-border-radius: 2px;
             font-family: \5FAE\8F6F\96C5\9ED1;
             font-size: 14px;
         }.z-btn02 {
              background: #ccc;
              color: #666;
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
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
        <div class="y-box">
            <div class="">
                <ul class="ul1">
                    <li class="li1">
                        <span class="gray-color">商家编号：${auditMap.uid}</span>
                    </li>
                    <li class="li1">
                        <span class="gray-color">公司名称：${auditMap.company}</span>
                    </li>
                    <li class="li1" style= "display:none">
                        <c:if test="${auditMap.userType=='3'}">
                            <a class="y-hx" href="${ctx}/sellerdetail/sellerdetail?uid=${auditMap.uid}">查看商家信息</a>
                        </c:if>
                        <c:if test="${auditMap.userType=='2'}">
                            <a class="y-hx" href="${ctx}/sellerdetail/bullerdetail?uid=${auditMap.uid}">查看商家信息</a>
                        </c:if>
                    </li>
                </ul>
            </div>
            <table id="contentTable1" class="y-tb1 z-tbl mb20">
                <colgroup>
                    <col width="20%">
                    <col width="40%">
                    <col width="40%">
                </colgroup>
                <thead>
                <tr>
                    <th style="background: #fbfaf8;">修改字段</th>
                    <th style="background: #fbfaf8;">修改前</th>
                    <th style="background: #fbfaf8;">修改后</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${auditMap.list}" var="user">
                    <tr>
                        <td>${user.propertiesColumn}</td>
                        <td>${user.beforeChangeValue}</td>
                        <td>${user.afterChangeValue}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </div>
    </div>
</div>
</body>
</html>
