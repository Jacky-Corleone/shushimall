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
        $(document).ready(function() {
        	   //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });
        });
        //通过
        function sm(){
            $("#stace").val(1);
            //$('#approveForm').submit();
            $("#popUpDiv").modal("show");
            $('#approveForm').ajaxSubmit(
                    {type:'POST',
                        dataType:'json',
                    success:function(data){
                $("#popUpDiv").modal("hide");
                if(data.success){
                    $.jBox.prompt('审核完成', '消息', 'info', { closed: function () { parent.getTabandrefress('店铺信息变更审核','店铺基本信息变更审核',true); } });
                }else{
                    $.jBox.prompt(data.msg, '消息', 'info');
                }
            }
                    });
        }
        //驳回
        function sm1(){
            var auditRemarkmen=$("#auditRemark").val();
            if(!auditRemarkmen){
                $.jBox.info("请填写驳回原因");
                return;
            }
            $("#stace").val(2);
            //$('#approveForm').submit();
            $("#popUpDiv").modal("show");
            $('#approveForm').ajaxSubmit(
                    {type:'POST',
                        dataType:'json',
                        success: function (data) {
                            $("#popUpDiv").modal("hide");
                            if (data.success) {
                                $.jBox.prompt('审核完成', '消息', 'info', { closed: function () {
                                    parent.getTabandrefress('店铺信息变更审核', '店铺基本信息变更审核', true);
                                } });
                            } else {
                                $.jBox.prompt(data.msg, '消息', 'info');
                            }
                        }
                    });
        }
        function hd(){
            $("#approveDiv").modal('hide');
        }
        function openm(){
            $("#approveDiv").modal('show');
        }

        function showmj(uid){
            var url = "${ctx}/sellerdetail/sellerdetail?uid="+uid;
            var title = "卖家明细";
            var id = "卖家明细";
            parent.openTab(url,title,id);
        }
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
        div
        .y-box {
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
            <div class="">
                <ul class="ul1">
                    <li class="li1">
                        <span class="gray-color">商家编号：</span>${detailJb.uid}
                    </li>
                    <li class="li1">
                        <span class="gray-color">公司名称：</span>${detailJb.company}
                    </li>
                    <li class="li1" style= "display:none">
                        <a class="y-hx" onclick="showmj('${detailJb.uid}')" href="javascript:void(0)">查看商家信息</a>
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
                <c:forEach items="${detailJb.list}" var="user">
                    <tr>
                        <td>${user.columName}</td>
                        <td>
                        	<c:if test="${user.bc!=null}">
								${user.bc}                       		
							</c:if>
                        </td>
                        <td>
                           <c:if test="${user.ac!=null}">
								${user.ac}                           
							</c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="tongguo"  class="btn  btn-primary " style="width: 35%;" type="button" onclick="sm()" value="通过" />
                    <input id="bohui"  class="btn  btn-primary " style="width: 35%;" type="button" onclick="openm()" value="驳回"/>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal hide fade" id="approveDiv">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>请填写审核意见</h3>
    </div>
    <form name="approveForm" id="approveForm" action="${ctx}/informationchangingaudit/auditjb">
        <input type="hidden" name="id" value="${detailJb.id}" id="id">
        <input type="hidden" name="shopid" value="${detailJb.shopid}" id="shopid">
        <input type="hidden" name="stace" value="" id="stace">
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span8">
                    <label class="label-control" for="auditRemark" title="备注">备注</label>
                    <textarea rows="3"  name="ms" id="auditRemark"></textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary" onclick="sm1()" id="btnSubApprove">提交</a>
            <a href="#" class="btn" onclick="hd()" id="btnCloseApprove">取消</a>
        </div>
    </form>
</div>

<div class="modal hide fade" id="popUpDiv">
    <div class="modal-body">
        <div class="progress progress-striped active">
            <div class="bar" style="width: 100%;"></div>
        </div>
    </div>
</div>
</body>
</html>
