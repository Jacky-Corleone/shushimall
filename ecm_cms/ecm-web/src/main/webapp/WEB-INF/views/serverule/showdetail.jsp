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
    <title>服务规则明细</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });
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
        div{
        }
        .y-box {
            width: 95%;
            border: 1px solid #eee;
            border-top: 2px solid #00a2ca;
            margin-bottom: 20px;
        }.bd {
             padding: 14px 0;
         }.z-item {
              overflow: hidden;
              padding: 6px 0;
          }.z-tt {
               display:inline;
               float: left;
               width: 70px;
               padding-left: 20px;
               height: 30px;
               line-height: 30px;
               color: #999;
           }.z-bd {
                display:inline;
                overflow: hidden;
                color: #666;
                float: left;
            }.preview-pic {
                 display: inline-block;
                 zoom: 1;
                 border: 1px solid #eee;
                 border-radius: 2px;
                 -moz-border-radius: 2px;
                 -webkit-border-radius: 2px;
             }.hd {
                  width: 100%;
                  height: 44px;
                  line-height: 44px;
                  border-bottom: 1px solid #eee;
                  position: relative;
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
        input.z-input01 {
            padding: 6px;
            border: 1px solid #eee;
            color: #666;
            background: #f9f9f9;
            width: 286px;
            height: 35px;
            border-radius: 2px;
            -moz-border-radius: 2px;
            -webkit-border-radius: 2px;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body" style="margin-left:3%;">
            <div class="y-box">
                <div class="hd">
                    <h3 class="h3">查看规则</h3>
                </div>
                <div class="bd">
                    <div class="z-item">
                        <div class="z-tt">
                            <span>图片预览:</span>
                        </div>
                        <div class="z-bd">
                            <div class="preview-pic">
                              <img class="showimg" src="${filePath}${map.ruleUrl}" style="width: 68px;height: 68px;">
                            </div>
                        </div>
                    </div>
                    <div class="z-item">
                        <div class="z-tt">
                            <span>名称:</span>
                        </div>
                        <div class="z-bd">
                            <input class="z-input01" value="${map.ruleName}" disabled/>
                        </div>
                    </div>
                    <div class="z-item">
                        <div class="z-tt">
                            <span>帮助文档:</span>
                        </div>
                        <div class="z-bd">
                            <input class="z-input01" value="${map.docTitle}" disabled/>
                        </div>
                    </div>
                    <div class="z-item">
                        <div class="z-tt">
                            <span>简介:</span>
                        </div>
                        <div class="z-bd">
                            <input class="z-input01" value="${map.simple}" disabled/>
                        </div>
                    </div>
                    <div class="z-item">
                        <div class="z-tt">
                            <span>详情描述:</span>
                        </div>
                        <div class="z-bd">
                            <textarea style="height: 130px;width: 500px;" disabled>${map.details}</textarea>
                            <%--<form:textarea path="describeUrl"  htmlEscape="false"  maxlength="16" class="required" style="width:700px;height:300px;">--%>
                        </div>
                    </div>
                </div>
            </div>
    </div>
</div>
</body>
</html>
