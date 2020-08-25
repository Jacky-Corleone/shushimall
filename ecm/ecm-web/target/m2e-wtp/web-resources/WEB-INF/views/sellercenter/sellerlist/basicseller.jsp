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
    	var previouskey = 0;
        $(document).ready(function() {
            //用于弹出提示框
            $("[data-toggle='popover']").popover();
            // 表格排序
            //tableSort({callBack : page});
            /*$("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                    if(v == "ok"){
                        $("#searchForm").attr("action","${ctx}/businesslist/exportpage").submit();
                    }
                },{buttonsFocus:1});
            });*/
            
            $("#registResourceid").keydown(
				function(event) {
					if(!event.shiftKey){
						if ((event.which > 47 && event.which < 58)
								|| event.which == 8 || event.which == 37 || event.which == 39 ) {
							return true;
						} else if (event.which == 86 && previouskey == 17) {
							return true;
						}
					}
					previouskey = event.which;
					
					return false;
				});
            
            // 禁止粘贴非数字的字符
            $('#registResourceid').bind('paste',function(e){
				var pastedText = undefined;
				if (window.clipboardData && window.clipboardData.getData) { // IE
					pastedText = window.clipboardData.getData('Text');
				} else {
					pastedText = e.originalEvent.clipboardData.getData('Text');//e.clipboardData.getData('text/plain');
				}
				if (/^[0-9]*$/.test(pastedText)) {
					return true;
				} else {
					return false;
				}
		    });
        });
        
        function unset(){
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            $("#searchForm").attr("action","${ctx}/businesslist/basicseller").submit();
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            selectpage(n);
            return false;
        }
        function selectpage(p){
        	if(!checkInputDate()){
        		return false;
        	}
            $.jBox.tip("正在刷新列表，请稍等",'loading',{opacity:0});
            var page=p;
            var rows=$("#pageSize").val();
            var companyName=$("#userNameid").val();
            var bingenDate =$("#beginDateid").val();
            var endDateid=$("#endDateid").val();
            var shopflag=$("#shopflag2").val();
            var userflag=$("#shopflag").val();
            var uid=$("#registResourceid").val();
            var shopleibie1=$("#shopleibie1").val();
            var shopleibie2=$("#shopleibie2").val();
            var shopleibie3=$("#shopleibie3").val();
            var shopName=$("#userFlagid").val();
            var sheng=$("#sheng").val();
            var shi=$("#shi").val();
            //var xian=$("#xian").val();
            var platformId=$("#platformId").val();
            $.ajax({
                url:"${ctx}/businesslist/selectbasicseller/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    companyName:companyName,
                    bingenDate:bingenDate,
                    endDateid:endDateid,
                    shopflag:shopflag,
                    userflag:userflag,
                    uid:uid,
                    shopleibie1:shopleibie1,
                    shopleibie2:shopleibie2,
                    shopleibie3:shopleibie3,
                    shopName:shopName,
                    sheng:sheng,
                    shi:shi,
                    xian:'',
                    platformId:platformId
                },
                dataType:'json',
                success:function(data){
                    $.jBox.closeTip();
                    if(data.success){
                        var html="<div class='pagination'>"+data.msg+"</div>";
                        $(".pagination").replaceWith(html);
                        var html2="";

                        $(data.obj.list).each(function(i,item){
                            html2=html2+"<tr><td>"+getcode(item.num)+"</td><td>"+"卖家</td><td>"+getcode(item.uid)+"</td><td>"+getcode(item.dpni)+"</td><td>"+getpla(item)+"</td><td>"+getcode(item.companyName)+"</td><td>"+getcode(item.userstatus)
                            +"</td><td>"+getcode(item.status)+"</td><td>"+getcode(item.passtime)+"</td><td>"+getcode(item.address)+"</td>"
                            if(item.platformId==2){
                            	html2=html2+"<td>绿印平台</td>"
                            }else if(item.platformId==null){
                            	html2=html2+"<td>舒适100平台</td>"
                            }
                            if(item.uid){
                                html2=html2+"<td><a href='javascript:void(0)' onclick='showmj(\""+item.uid+"\")'>查看<a></td>"
                            }else{
                                html2=html2+"<td></td>"
                            }
                            html2=html2+"</tr>";
                        });
                         $("#tabletbody").html(html2);
                        $("#pageNo").val(data.obj.pageNo);
                        $("#pageSize").val(data.obj.pageSize);
                        $("[data-toggle='popover']").popover();
                    }else{
                        $.jBox.info(data.msg);
                    }
                },error:function(){
                    $.jBox.closeTip();
                }
            });
        }
        //根据省的编号来获取城市的下拉框
        function citysOfeastern(){
            var parentCode= $("#sheng").val();
            $("#shi").html("<option value=''>请选择</option>");
        	$('#shi').select2("val", "");
    		/* $("#xian").html("<option value=''>请选择</option>");
    		$("#xian").select2("val",""); */
            $.ajax({
                url:"${ctx}/businesslist/selectcity/",
                type:"post",
                data:{
                	parentCode:parentCode
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var html="<option value=''>请选择</option>";
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                                 html += "<option value='"+item.code+"'>"+item.name+"</option>";
                            });
                        }
                        $("#shi").html(html);
                    }
                }
            });
        }
      	//根市的编号来获取县的下拉框
        /* function xiansOfshi(){
        	var parentCode = $("#shi").val();
        	$("#xian").html("<option value=''>请选择</option>");
    		$("#xian").select2("val","");
            $.ajax({
                url:"${ctx}/businesslist/selectcity/",
                type:"post",
                data:{
                	parentCode:parentCode
                },
                dataType:'json',
                success:function(data){
                    if(data.success){
                        var html="<option value=''>请选择</option>";
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                                 html += "<option value='"+item.code+"'>"+item.name+"</option>";
                            });
                        }
                        $("#xian").html(html);
                    }
                }
            });
        } */
        //经营商品分类级联，类目
        function leimu(parentid,childid){
            var parentNum= $("#"+parentid).val();
            $.ajax({
                url:"${ctx}/businesslist/selectlm/",
                type:"post",
                data:{
                    parentNum:parentNum
                },
                dataType:'json',
                success:function(data){
                	if(parentid == "shopleibie1"){
                		$("#shopleibie2").select2("val","");
                		$("#shopleibie3").html("<option value=''>请选择</option>");
                		$("#shopleibie3").select2("val","");
                	}else if(parentid == "shopleibie2"){
                		$("#shopleibie3").select2("val","");
                	}
                    if(data.success){
                        var html="<option value=''>请选择</option>";
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                                html += "<option value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
                            });
                        }
                        $("#"+childid).html(html);
                    }
                }
            });
        }
        function getpla(item){
         var as="<a data-toggle='popover' data-trigger='hover' data-title='店铺品类' data-placement='right' data-content='"+getcode(item.pl)+"'>"+getcode(item.plt)+"...</a>";
         return as
        }
        function getcode(date){
            if(date){
                return date;
            }else{
                return '';
            }
        }
        function showmj(uid){
            var url = "${ctx}/sellerdetail/sellerdetail?uid="+uid;
            var title = "卖家明细";
            var id = "卖家明细";
            parent.openTab(url,title,id);
        }
        function exportall(){
            var size=500;
            var page=1;
            var rows=$("#pageSize").val();
            var companyName=$("#userNameid").val();
            var bingenDate =$("#beginDateid").val();
            var endDateid=$("#endDateid").val();
            var shopflag=$("#shopflag2").val();
            var userflag=$("#shopflag").val();
            var uid=$("#registResourceid").val();
            var shopleibie3=$("#shopleibie3").val();
            var shopName=$("#userFlagid").val();
            var sheng=$("#sheng").val();
            var shi=$("#shi").val();
            //var xian=$("#xian").val();
            var platformId=$("#platformId").val();
            $.ajax({
                url:"${ctx}/businesslist/querydatacount/",
                type:"post",
                data:{
                    page:page,
                    rows:rows,
                    companyName:companyName,
                    bingenDate:bingenDate,
                    endDateid:endDateid,
                    shopflag:shopflag,
                    userflag:userflag,
                    uid:uid,
                    shopleibie3:shopleibie3,
                    shopName:shopName,
                    sheng:sheng,
                    shi:shi,
                    xian:'',
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
                        $("#searchForm").attr("action","${ctx}/businesslist/exportpage").submit();
                        page=page+1;
                        callback(up,size,page,count,callback);
                    }else{
                        page=page+1;
                        callback(up,size,page,count,callback);
                    }
                },{buttonsFocus:1});
            }
        }
        function checkInputDate(){
       	 	var timeBegin = $("#beginDateid").val();
            var timeEnd = $("#endDateid").val();
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
            width: 25%;
            text-align: right;
        }
        table.td-cen th,.td-cen td{
            text-align: center;
        }.hhtd td{
             word-wrap:break-word;
             word-break:break-all;
         }
        a.y-button {
            width: 78px;
            height: 28px;
            line-height: 28px;
            text-align: center;
            border: 1px solid #999;
            border-radius: 4px;
            margin-right: 8px;
            float: left;
            display: block;
            cursor: pointer;
            text-decoration:none;
        }.sellerHover {
             background: #e9f8f6;
             border-color: #2bb8aa;
             color: #2bb8aa;
         }.bullerHover{
             background:white;
         }
        i.buyerIcon, .sellerIcon {
            width: 10px;
            height: 12px;
            vertical-align: middle;
            /*background: url(http://management.printhome.com/resources/v1/images/company.png) no-repeat;*/
            background: url(${ctx}/../static/picture/company.png) no-repeat;
            display: inline-block;
            overflow: hidden;
            margin-right: 3px;
        }
        i.buyerIcon{
            background-position: -20px -1px;
        }.sellerIcon{
            background-position: -44px -2px;
        }
        div.div-hang{
            text-overflow:ellipsis;
            white-space: nowrap;
            height: 40px;
        }.div-flot{
            width: 25%;
            float:left;
            text-align: right;
            margin-left: 10px;
        }
    </style>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form:form id="searchForm" modelAttribute="user" action="${ctx}/businesslist/basicseller" method="post" class="breadcrumb form-search">
            <input id="pageNo" name="page" type="hidden" value="${bacicseller.page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${bacicseller.page.pageSize}" />

            <div class="row-fluid">
                <div class="span5 div-hang">
                    <div class="div-flot">
                        <label class="control-label"  title="商家类型">
                            商家类型：
                        </label>
                    </div>
                    <div>
                    <a style="margin-left:5px;" class="y-button bullerHover" href="${ctx}/businesslist/basicbuller/"><i class="buyerIcon"></i>买家</a>
                    <a class="y-button sellerHover" href="#"><i class="sellerIcon"></i>卖家</a>
                    </div>
                </div>
                 <div class="span5">
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
            <div class="row-fluid">
                <div class="span5">
                    <label class="label-left control-label"  for="userNameid" title="公司名称">
                        公司名称：
                    </label>
                    <input name="companyName" id="userNameid" style="width:50%" type="text" class="form-control" />
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="beginDateid" title="店铺开通时间">
                        店铺开通时间：
                    </label>
                    <input name="bingenDate" id="beginDateid" readonly="readonly" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDateid\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                    至
                    <input name="endDateid" id="endDateid" readonly="readonly" onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginDateid\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width: 25%" type="text" class="input-small Wdate" />
                </div>
            </div>
            <div class="row-fluid" style="margin-top:10px;">
            <div class="span5">
                <label class="label-left control-label" for="shopflag" title="商家状态">
                    商家状态：
                </label>
                <select name="userflag" id="shopflag" style="width:50%"  class="form-control">
                    <option value="">请选择</option>
                    <c:forEach items="${bacicseller.buflag}" var="item">
                        <option value="${item.code}">${item.label}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="span5">
                <label class="label-left control-label" for="registResourceid" title="商家编号">
                    商家编号：
                </label>
                <input name="uid" id="registResourceid" style="width:50%;ime-mode:disabled" type="text" class="form-control" maxlength="18" />
            </div>
            </div>
            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label" for="shopleibie1" title="经营商品分类">
                        经营品类：
                    </label>
                    <select name="shopleibie1" id="shopleibie1" style="width:23%" onchange="leimu('shopleibie1','shopleibie2')" class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${bacicseller.cates}" var="item">
                            <option value="${item.categoryCid}">${item.categoryCName}</option>
                        </c:forEach>
                    </select>
                    <select name="shopleibie2" id="shopleibie2" style="width:23%" onchange="leimu('shopleibie2','shopleibie3')"  class="form-control">
                        <option value="">请选择</option>
                    </select>
                    <select name="shopleibie3" id="shopleibie3" style="width:23%"  class="form-control">
                        <option value="">请选择</option>
                    </select>
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="userFlagid" title="店铺名称">
                        店铺名称：
                    </label>
                    <input name="shopName" id="userFlagid" style="width:50%" type="text" class="form-control" />
                </div>
            </div>

            <div class="row-fluid" style="margin-top:10px;">
                <div class="span5">
                    <label class="label-left control-label" for="shopflag2" title="店铺状态">
                        店铺状态：
                    </label>
                    <select name="shopflag" id="shopflag2" style="width:50%"  class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${bacicseller.dpflag}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="span5">
                    <label class="label-left control-label" for="sheng" title="商家地域">
                        商家地域：
                    </label>
                    <select name="sheng" id="sheng" style="width:22%" onchange="citysOfeastern()" class="form-control">
                        <option value="">请选择</option>
                        <c:forEach items="${bacicseller.address}" var="item">
                            <option value="${item.code}">${item.name}</option>
                        </c:forEach>
                    </select>
                    <select name="shi" id="shi" style="width:22%" onchange="xiansOfshi()" class="form-control">
                        <option value="" >请选择</option>
                    </select>
                     <!-- <select name="xian" id="xian" style="width:22%" class="form-control">
                        <option value="" >请选择</option>
                    </select> -->
                </div>
            </div>

            <div class="row-fluid" style="margin-top: 8px;">
                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btnquery1"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="selectpage(1)" value="查询" />
                    <input id="btncancle"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="unset();" value="重置"  />

                </div>
                <div class="span5">
                    <label class="label-left control-label"></label>
                    <%--<input id="btnExport"  class="btn  btn-primary" style="width: 26%;" type="button" value="导出当前页" />
                    <input id="btnExportAll"  class="btn  btn-primary" style="width: 26%;" type="button" onclick="exportall()" value="导出所有" />--%>
                </div>
<%--                <div class="span5">
                    <label class="label-left control-label"></label>
                    <input id="btnexport"  class="btn  btn-primary" style="width: 26%;" type="button" value="导出"  />
                </div>--%>
            </div>
        </form:form>
        <table id="contentTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
            <thead>
            <tr>
                <th width="3%">序号</th>
                <th width="5%">商家类型</th>
                <th width="8%">商家编号</th>
                <th width="15%">店铺名称/ID</th>
                <th width="10%">经营品类</th>
                <th width="14%">公司名称</th>
                <th width="12%">商家状态</th>
                <th width="5%">店铺状态</th>
                <th width="8%">店铺开通时间</th>
                <th width="14%">商家地域</th>
                <th width="6%">平台类型</th>
                <th width="5%">操作</th>
            </tr>
            </thead>
            <tbody id="tabletbody">
            <c:forEach items="${bacicseller.page.list}" var="user">
                <tr>
                    <td>${user.num}</td>
                    <td>卖家</td>
                    <td>${user.uid}</td>
                    <td>${user.dpni}</td>
                    <td >
                    <a data-toggle="popover" data-trigger="hover" data-title="店铺品类" data-placement="right" data-content="${user.pl}" >${user.plt}...</a>
                    </td>
                    <td>${user.companyName}</td>
                    <td>${user.userstatus}</td>
                    <td>${user.status}</td>
                    <td>${user.passtime}</td>
                    <td>${user.address}</td>
                    <td>
                            <c:choose>
                                <c:when test="${user.platformId==null}">舒适100平台</c:when>
<%--                                 <c:when test="${user.platformId==2}">绿印平台</c:when> --%>
                            </c:choose>
                    <td>
                        <c:if test="${user.uid!=''}">
                            <a onclick="showmj('${user.uid}')" href="javascript:void(0)">查看</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${bacicseller.page}</div>
    </div>
</div>
</body>
</html>
