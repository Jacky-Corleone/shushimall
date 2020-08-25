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
        function sm(){
            var auditRemarkmen=$("#auditRemark").val();
            if(!auditRemarkmen){
                $.jBox.info("请填写驳回原因");
                return;
            }
            var data=$('#approveForm').serialize();
            var url="${ctx}/informationchangingaudit/auditppresult";
            var id=$("#id").val();
            audit(id,url,data);
        }
        function hd(){
            $("#approveDiv").modal('hide');
        }
        function openm(shopid,brandid,company,uid,id){
            $("#shopid").val(shopid);
            $("#brandid").val(brandid);
            $("#company").val(company);
            $("#uid").val(uid);
            $("#id").val(id);
            $("#approveDiv").modal('show');
        }
        function showmj(uid){
            var url = "${ctx}/sellerdetail/sellerdetail?uid="+uid;
            var title = "卖家明细";
            var id = "卖家明细";
            parent.openTab(url,title,id);
        }
        function audit(id,url,da){
            $.ajax({
                url:url,
                type:"post",
                data:da,
                dataType:'json',
                success:function(data){
                    if(data.success){
                        $.jBox.prompt(data.msg, '消息', 'info', { closed: function () {
                            $("#"+id).remove();
                            $("#approveDiv").modal('hide');
                            var tablebody=$("#tablebodyid tr");
                            if(!tablebody||tablebody.length<1){
                                parent.getTabandrefress('店铺信息变更审核','店铺新增品牌审核',true);
                            }
                        } });
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(){
                    $.jBox.info("亲，系统繁忙请耐性等待");
                }
            });
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
                        <span class="gray-color">商家编号：</span>${detailPp.uid}
                    </li>
                    <li class="li1">
                        <span class="gray-color">公司名称：</span></span>${detailPp.company}
                    </li>
                    <li class="li1" style= "display:none">
                        <a class="y-hx" onclick="showmj('${detailPp.uid}')" href="javascript:void(0)">查看商家信息</a>
                    </li>
                </ul>
            </div>
            <table id="contentTable1" class="y-tb1 z-tbl mb20">
                <colgroup>
                    <col width="40%">
                    <col width="40%">
                    <col width="20%">
                </colgroup>
                <thead>
                <tr>
                    <th style="background: #fbfaf8;">品牌所属类目</th>
                    <th style="background: #fbfaf8;">品牌名称</th>
                    <th style="background: #fbfaf8;">审核</th>
                </tr>
                </thead>
                <tbody id="tablebodyid">
                <c:forEach items="${detailPp.listMap}" var="user">
                    <tr id="${user.id}">
                        <td>${user.sj}</td>
                        <td>${user.brandName}</td>
                        <td>
                            <a class="y-hx" href="javascript:audit('${user.id}','${ctx}/informationchangingaudit/auditppresult','uid=${detailPp.uid}&company=${detailPp.company}&static=2&shopid=${detailPp.shopid}&brandid=${user.brandid}&id=${user.id}')">通过</a>
                            <a class="y-hx" href="#" onclick="openm('${detailPp.shopid}','${user.brandid}','${detailPp.company}','${detailPp.uid}','${user.id}')">驳回</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="modal hide fade" id="approveDiv">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>请填写审核意见</h3>
    </div>
    <form name="approveForm" id="approveForm" action="${ctx}/informationchangingaudit/auditppresult">
        <input type="hidden" name="uid" value="" id="uid">
        <input type="hidden" name="shopid" value="" id="shopid">
        <input type="hidden" name="brandid" value="" id="brandid">
        <input type="hidden" name="company" value="" id="company">
        <input type="hidden" name="static" value="3" id="static">
        <input type="hidden" name="id" value="" id="id">
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span8">
                    <label class="label-control" for="auditRemark" title="备注">备注</label>
                    <textarea rows="3"  name="ms" id="auditRemark"></textarea>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-primary" onclick="sm()" id="btnSubApprove">提交</a>
            <a href="#" class="btn" onclick="hd()" id="btnCloseApprove">取消</a>
        </div>
    </form>
</div>
</body>
</html>
