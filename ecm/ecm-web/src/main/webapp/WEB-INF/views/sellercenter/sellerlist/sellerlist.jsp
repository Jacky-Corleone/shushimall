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
            // 表格排序
            //tableSort({callBack : page});
            /*$("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        $("#searchForm").attr("action","${ctx}/sellerlist/exportpage").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });*/
        });
        function unset(){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            $("#searchForm").attr("action","${ctx}/sellerlist/sellerlist/").submit();
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            selectpage(n);
        }
        function selectpage(p){
        	if(!checkInputDate()){
        		return false;
        	}
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            var page=p;
            var rows=$("#pageSize").val();
            var uname= $("#userNameid").val();
            var uid=$("#useCodeid").val();
            var quickType=$("#quickTypeid").val();
            var nickname=$("#nickNameid").val();
            var userstatus=$("#userstatusid").val();
            var companyName=$("#companyNameid").val();
            var createTimeBegin=$("#createTimeBegin").val();
            var createTimeEnd =$("#createTimeEnd").val();
            var platformId=$("#platformId").val();
            var usertype = $("#usertype").val();
            $.ajax({
                url:"${ctx}/sellerlist/selectuser/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    uname:uname,
                    uid:uid,
                    quickType:quickType,
                    nickname:nickname,
                    userstatus:userstatus,
                    companyName:companyName,
                    createTimeBegin:createTimeBegin,
                    createTimeEnd:createTimeEnd,
                    platformId:platformId,
                    usertype:usertype
                },
                dataType:'json',
                success:function(data){
                    $.jBox.closeTip();
                    if(data.success){
                        var html="<div class='pagination'>"+data.msg+"</div>";
                        $(".pagination").replaceWith(html);
                        var html2="";
                        $(data.obj.list).each(function(i,item){
                            //html2=html2+"<tr><td>"+getcode(item.num)+"</td><td>"+getcode(item.uid)+"</td><td>"+getcode(item.uname)+"</td><td>"+getcode(item.nickname)+"</td><td>"+getcode(item.userstatusname)+"</td><td>"+getcode(item.umobile)+"</td><td>"+getcode(item.userEmail)
                            //        +"</td><td>"+getcode(item.companyName)+"</td><td>"+getcode(item.created)+"</td><td>"+getcode(item.quickType)+"</td>"
                            
                            var usertype='';
                            switch(item.usertype) {
	                            case(2):
		                            usertype = '会员';
		                            break;
	                            case(3):
		                            usertype = '管理员';
		                            break;
                            }
                            html2=html2+"<tr><td>"+getcode(item.num)+"</td><td>"+getcode(item.uid)+"</td><td>"+getcode(item.uname)+"</td><td>"+getcode(item.nickname)+"</td><td>"+usertype+"</td><td>"+getcode(item.shopname)+"</td><td>"+getcode(item.umobile)+"</td><td>"+getcode(item.userEmail)
                                    +"</td><td>"+getcode(item.created)+"</td>"
                            
                            /* if(item.platformId==2){
                            	html2=html2+"<td>绿印平台</td>"
                            }else if(item.platformId==null){
                            	html2=html2+"<td>舒适100平台</td>"
                            }
                            if(item.usertype=='3'){
                                html2=html2+"<td><a href='javascript:void(0)' onclick='showmj(\""+item.uid+"\")'>卖家明细<a></td>"
                            }else if(item.usertype=='2'){
                                html2=html2+"<td><a href='javascript:void(0)' onclick='showmjj(\""+item.uid+"\")'>买家明细<a></td>"
                            }else if(item.userstatus=='2'){
                            	if(item.platformId == null){
                            		html2=html2+"<td><a href='javascript:void(0)' onclick='goToApproveFast(\""+item.uid+"\",\"\")'>快捷认证<a></td>"
                            	}else{
                            		html2=html2+"<td><a href='javascript:void(0)' onclick='goToApproveFast(\""+item.uid+"\",\""+item.platformId+"\")'>快捷认证<a></td>"
                            	}
                                
                            }else{
                                html2=html2+"<td></td>"
                            } */
                            html2=html2+"</tr>";
                        });
                        $("#tabletbody").html(html2);
                        $("#pageNo").val(data.obj.pageNo);
                        $("#pageSize").val(data.obj.pageSize);
                    }
                },error:function(){
                    $.jBox.closeTip();
                }
            });
        }
        /**
        *买家快捷认证
        */
        function approveFast(uid,platformId,userstatusname){
        	$("#approveDiv").modal('show');
        	$("#userStatusSpan").html(userstatusname);
        	$("#uid").val(uid);
        	$("#platformId").val(platformId);
        }
        
        function goToApproveFast(uid,platformId){
        	top.$.jBox.confirm("确认要快捷认证买家吗？","系统提示",function(v,h,f){
                if(v == "ok"){
                	hideApproveDiv();
                    $.ajax({
                        url:"${ctx}/apply/fastApprove",
                        //data:"uid="+$("#uid").val()+"&platformId="+$("#platformId").val(),
                        data:"uid=" + uid + "&platformId=" + platformId,
                        dataType:"json",
                        success:function(data){
                            if(data.success){
                                $.jBox.info("认证成功");
                                //刷新当前页面	 
                                //selectpage($("#pageNo").val());
                                selectpage(1);
                            }else{
                                $.jBox.error(data.msg);
                            }
                        }
                    });
                }
            });
        }
        
        function hideApproveDiv(){
        	$("#approveDiv").modal('hide');
        }
        
        function exportall(){
            var size=500;
            var page=1;
            var rows=$("#pageSize").val();
            var uname= $("#userNameid").val();
            var uid=$("#useCodeid").val();
            var quickType=$("#quickTypeid").val();
            var nickname=$("#nickNameid").val();
            var userstatus=$("#userstatusid").val();
            var companyName=$("#companyNameid").val();
            var createTimeBegin=$("#createTimeBegin").val();
            var createTimeEnd =$("#createTimeEnd").val();
            var platformId=$("#platformId").val();
            $.ajax({
                url:"${ctx}/sellerlist/selectusercount/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    uname:uname,
                    uid:uid,
                    quickType:quickType,
                    nickname:nickname,
                    userstatus:userstatus,
                    companyName:companyName,
                    createTimeBegin:createTimeBegin,
                    createTimeEnd:createTimeEnd,
                    platformId:platformId
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
                        $("#searchForm").attr("action","${ctx}/sellerlist/exportpage").submit();
                        page=page+1;
                        callback(up,size,page,count,callback);
                    }else{
                        page=page+1;
                        callback(up,size,page,count,callback);
                    }
                },{buttonsFocus:1});
            }
        }
        function getcode(date){
            if(date){
                return date;
            }else{
                return '';
            }
        }
        //查看被选择人资质
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
      //输入字符显示
        function numInput(obj,length){
        	if(obj.value==obj.value2)
        		return;
        	if(length == 0 && obj.value.search(/^\d*$/)==-1)
        		obj.value=(obj.value2)?obj.value2:'';
        	else if(obj.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)
        		obj.value=(obj.value2)?obj.value2:'';
        	else obj.value2=obj.value;
        }
      
        function checkInputDate(){
        	 var timeBegin = $("#createTimeBegin").val();
             var timeEnd = $("#createTimeEnd").val();
             if($.trim(timeBegin) == "" || $.trim(timeEnd) == ""){
            	 return true;
             }
             var arrStart = timeBegin.split("-");
             var tmpIntStartYear = parseInt(arrStart[0],10);
             var tmpIntStartMonth = parseInt(arrStart[1],10);
             var tmpIntStartDay = parseInt(arrStart[2],10);
             
             var arrEnd = timeEnd.split("-"); 
             var tmpIntEndYear = parseInt(arrEnd[0],10); 
             var tmpIntEndMonth = parseInt(arrEnd[1],10); 
             var tmpIntEndDay = parseInt(arrEnd[2],10);

             if( tmpIntStartYear < tmpIntEndYear ){
            	 return true;
             }else if(tmpIntStartYear == tmpIntEndYear ){
            	 if( tmpIntStartMonth < tmpIntEndMonth ){ 
            		 return true;
            	 }else if(tmpIntStartMonth == tmpIntEndMonth ){
            		 if( tmpIntStartDay <= tmpIntEndDay ){ 
                		 return true;
                	 }else{
                		 $.jBox.info("开始日期不能晚于结束日期");
                    	 return false;
                     }
            	 }else{
            		 $.jBox.info("开始日期不能晚于结束日期");
                	 return false;
                 }
             }else{
            	 $.jBox.info("开始日期不能晚于结束日期");
            	 return false;
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
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/sellerlist/sellerlist/" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${userlist.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${userlist.page.pageSize}" />
            <div class="row-fluid">
                <div class="span5">
                    <label class="label-left control-label"  for="userNameid" title="用户名">
                        用户名：
                    </label>
                    <input name="uname" id="userNameid" style="width:50%" type="text" class="form-control" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="createTimeBegin" title="注册时间">
                        注册时间：
                    </label>
                    <input name="createTimeBegin" id="createTimeBegin" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createTimeEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                    至
                    <input name="createTimeEnd" id="createTimeEnd" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createTimeBegin\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                </div>
            </div>
				<div class="row-fluid" style="margin-top: 10px;">
					<div class="span5">
						<label class="label-left control-label" for="useCodeid"
							title="用户编号"> 用户编号： </label> <input name="uid" id="useCodeid"
							style="width: 50%" type="text" class="form-control"
							onkeyup="numInput(this,0)"
							onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
							maxlength="18" />
					</div>
					<div class="span5">
						<label class="label-left control-label" for="nickNameid"
							title="用户昵称"> 用户昵称： </label> <input name="nickname"
							id="nickNameid" style="width: 50%" type="text"
							class="form-control" />
					</div>
				</div>
				<div class="row-fluid" style="margin-top:10px;">
					<div class="span5">
						<label class="label-left control-label" for="usertype"
							title="用户类型"> 用户类型： </label><select name="usertype" id="usertype"
									class="form-control">
							<option value="">请选择</option>
							<option value="2">会员</option>
							<option value="3">管理员</option>
							</select>
					</div>
				</div>
				<div class="row-fluid" style="margin-top:10px;">
                
                <div class="span5" style="display:none;">
                    <label class="label-left control-label" for="userstatusid" title="用户状态">
                        用户状态：
                    </label>
                    <select name="userstatus" id="userstatusid" style="width:50%"  class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${userlist.buflag}" var="item">
                            <option value="${item.code}">${item.label}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="row-fluid" style="margin-top:10px;display:none;">
                <div class="span5">
                    <label class="label-left control-label" for="companyNameid" title="用户昵称">
                        公司名称：
                    </label>
                    <input name="companyName" id="companyNameid" style="width:50%" type="text" class="form-control" />
                </div>
                <div class="span5" style="display:none;">
								<label class="label-left control-label" for="platformId"
									title="平台类型">平台类型：</label>
								<select name="platformId" id="platformId" style="width:50%"
									class="form-control">
									<option value="">所有</option>
									<option value="0">舒适100平台</option>
<!-- 									<option value="2">绿印平台</option> -->
								</select>
				</div>
            </div>

            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label" ></label>
                    <input id="btnquery"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="selectpage(1)" value="查询" />
                    <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="button" value="重置" onclick="unset();" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" ></label>
                    <%--<input id="btnExport"  class="btn  btn-primary" style="width: 26%;" type="button"  value="导出当前页" />
                    <input id="btnExportAll"  class="btn  btn-primary" style="width: 26%;" type="button" value="导出全部" onclick="exportall()" />--%>
                </div>
            </div>
        </form:form>
        <table id="contentTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
            <thead>
            <tr>
                <th width="3%">序号</th>
                <th width="8%">用户编号</th>
                <th width="9%">用户名</th>
                <th width="3%">用户昵称</th>
                <th width="9%">用户类型</th>
                <th width="9%">店铺名称</th>
                <!-- <th width="12%">用户状态</th> -->
                <th width="9%">认证手机</th>
                <th width="11%">认证邮箱</th>
                <!-- <th width="12%">公司名称</th> -->
                <th width="10%">注册时间</th>
                <!-- <th width="5%">平台类型</th>
                <th width="8%">操作</th> -->
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${userlist.page.list}" var="user">
                <tr>
                    <td>${user.num}</td>
                    <td>${user.uid}</td>
                    <td>${user.uname}</td>
                    <td>${user.nickname}</td>
                    <td>
                    	<c:choose>
                    		<c:when test="${user.usertype ==2}">
                    			会员
                    		</c:when>
                    		<c:when test="${user.usertype ==3}">
                    			管理员
                    		</c:when>
                    	</c:choose>
                    </td>
                    <td>
                    	<c:if test="${user.usertype ==3}">
                    	<c:set var="uid" value="${user.uid}" ></c:set>
                    		<c:forEach items="${usernameMapToShopName }" var="map">
                    			<c:if test="${map.key == uid}">
                    				${map.value }
                    			</c:if>
                    		</c:forEach>
                    	</c:if>
                    </td>
                    <%-- <td>${user.userstatusname}</td> --%>
                    <td>${user.umobile}</td>
                    <td>${user.userEmail}</td>
                    <%-- <td>${user.companyName}</td> --%>
                    <td>${user.created}</td>
                   <%--  <td>
                            <c:choose>
                                <c:when test="${user.platformId==null}">舒适100平台</c:when>
                                <c:when test="${user.platformId==2}">绿印平台</c:when>
                            </c:choose>
                        </td> --%>
                   <%--  <td>
                        <c:if test="${user.usertype==3}">
                        <a onclick="showmj('${user.uid}')" href="javascript:void(0)">卖家明细</a>
                        </c:if>
                        <c:if test="${user.usertype==2}">
                            <a onclick="showmjj('${user.uid}')" href="javascript:void(0)">买家明细</a>
                        </c:if>
                        <c:if test="${user.userstatus==2}">
                            <a onclick="approveFast('${user.uid}','${user.platformId}','${user.userstatusname}')" href="javascript:void(0)">快捷认证</a>
                            <a onclick="goToApproveFast('${user.uid}','${user.platformId}')" href="javascript:void(0)">快捷认证</a>
                        </c:if>
                    </td> --%>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${userlist.page}</div>
    </div>
     <!--审核弹出框-->
    <div class="modal hide fade" id="approveDiv">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3>快捷认证买家</h3>
        </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span4">商家状态：<span id="userStatusSpan"></span></div>
            </div>
        <div class="modal-footer">
        	<input type="hidden" value="" id="uid">
            <input type="hidden" value="" id="platformId">
            <a href="#" class="btn btn-primary" onclick="goToApproveFast()">提交</a>
            <a href="#" class="btn" onclick="hideApproveDiv()">取消</a>
        </div>
    </div>
</div>
</body>
</html>
