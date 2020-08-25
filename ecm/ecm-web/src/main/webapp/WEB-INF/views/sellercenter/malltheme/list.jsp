<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>平台主题</title>
<meta name="decorator" content="default"/>
	 <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
	<script type="text/javascript">
		$(function()
		{
			if('${msg}' == 'addSuccess')
			{
				$.jBox.info("添加成功！");
			}else if( '${msg}' == 'delSuccess')
			{
				$.jBox.info("删除成功！");
			}else if( '${msg}' == 'editSuccess')
			{
				$.jBox.info("编辑成功！");
			}else if( '${msg}' == 'upSame')
			{
				$.jBox.info("不允许上架相同的二级类目");
			}
			
		});
		//初始化类目
		$(document).ready(function() {
			 var c1 = $("#platformId1").val();
			 var c2 = $("#platformId2").val();
			 if(c1 == ""||c2 == ""){
				 var html = "<option value=''>二级类目</option>";
				 $("#platformId2").html(html);
			 }
			 citysOfeastern('provinceCode','shi','${ mallThemeDTO.cityCode}');
		})
		//根据一级类目 得到二级类目
		function onSelect(cid){
			 var c1 = $("#platformId1").val();
			 if(c1 == ""){
				 var html = "<option value=''>二级类目</option>";
				 $("#s2id_platformId2>a>span").text("二级类目");
				 $("#platformId2").html(html);
				 return;
			 }
			var html = "<option value=''>二级类目</option>";
	    	$.ajax({
	    		type:"post",
	    		url:"${ctx}/brand/getChildCategory?pCid="+cid,
	    		dataType:"json",
	    		success:function(data){
	                console.log(data);
	                $(data).each(function(i,item){
	                	html += "<option ";
	                    if(item.categoryCid=='${platformId2}'){
	                        html +=" selected='selected'";
	                    }
	                    html +=" value='"+item.categoryCid+"'>"+item.categoryCName+"</option>";
	                })
	                $("#platformId2").html(html);
	    		},
	    		error:function(){
	 				$.jBox.info('请求失败！');
	 			}
	    	})
		}
		//下架
		function setStatusUse(id,status){
			var url = "${ctx}/sellercenter/mallTheme/upAndDown";
	         $.ajax({
	             url : url,
	             type : "post",
	             dataType : "json",
	             data:{
	            	 id:id,
	            	 status:status
	             },
	             success : function(data) {
	                 if(data.success){
	                     $.jBox.success("操作成功!",'提示',{closed:function(){
	                         $("#mallThemeForm").submit();
	                     }});
	                 }else{
	                     $.jBox.info(data.msg);
	                 }
	             },
	             error : function(xmlHttpRequest, textStatus, errorThrown) {
	                 $.jBox.info("系统错误！请稍后再试！");
	             }
	         });
		}
		
		//上架
		function upStatus(id,status,pId,Cid){
			var url = "${ctx}/sellercenter/mallTheme/upStatus";
	         $.ajax({
	             url : url,
	             type : "post",
	             dataType : "json",
	             data:{
	            	 id:id,
	            	 status:status,
	            	 primaryCid:pId,
	            	 cid:Cid
	             },
	             success : function(data) {
	            	 if(data.obj=="no"){
	            		 $.jBox.info(data.msg);
	            		 return;
	            	 }
	                 if(data.success){
	                     $.jBox.success("操作成功!",'提示',{closed:function(){
	                         $("#mallThemeForm").submit();
	                     }});
	                 }else{
	                     $.jBox.info(data.msg);
	                 }
	             },
	             error : function(xmlHttpRequest, textStatus, errorThrown) {
	                 $.jBox.info("系统错误！请稍后再试！");
	             }
	         });
		}
		//编辑
		function toform(id){
			$("#mallThemeForm").attr("action","${ctx}/sellercenter/mallTheme/toform?id=" + id).submit();
		}
		//删除
		function todelete(id){
			var submit = function (v, h, f) { 
				if (v == true)  
					$("#mallThemeForm").attr("action","${ctx}/sellercenter/mallTheme/delete?id=" + id).submit();
				else  
					jBox.tip("取消", 'info');  
				return true;  
			};  
			// 自定义按钮  
			$.jBox.confirm("子站包含广告、楼层、公告等信息，确定要删除子站吗？", "提示", submit, { buttons: { '确定': true, '取消': false} }); 
		}
		
		function unset(){ //重置
            $(':input','#mallThemeForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
            $("#pageNo").val(1);
            $("#pageSize").val(10);
            $("#mallThemeForm").submit();
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#mallThemeForm").attr("action","${ctx}/sellercenter/mallTheme/list").submit();
	    	return false;
	    }
		
		//根据省的编号来获取城市的下拉框
        function citysOfeastern(parentid,childid,value){
            var shengcode= $("#"+parentid).val();
            $.ajax({
                url:"${ctx}/businesslist/selectcity/",
                type:"post",
                data:{
                	parentCode:shengcode
                },
                dataType:'json',
                success:function(data){
                    console.log(data);
                    if(data.success){
                        var html="<option value=''>请选择</option>"
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                                html += "<option ";
                                	if(item.code == value){
                 			        	html +=" selected='selected'";
                 			        	$("#child_label span:first").html(item.name);
                 			        }
                               	html += " value='"+item.code+"'>"+item.name+"</option>";
                            });
                        }
                        $("#"+childid).html(html);
                    }
                }
            });
        }
	</script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
			<form:form id="mallThemeForm"  modelAttribute="mallThemeDTO" action="${ctx}/sellercenter/mallTheme/list" method="post">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="row-fluid">
				<%--
	        	<div class="span6">
 					<label>类目：</label><!-- onchange="onSelect(this.value)" 去掉一级类目关联到二级类目-->
					<form:select path="platformId1" id="platformId1" cssClass="input-medium" onchange="onSelect(this.value)">
						<form:option value="" label="一级类目"></form:option>
						<form:options items="${ categoryList}" itemLabel="categoryCName" itemValue="categoryCid"/>
					</form:select>
					<form:select path="platformId2" id="platformId2"  cssClass="input-medium">
	                     <form:option value="" label="二级分类"></form:option>
	                     <form:options items="${ categoryLev2List}" itemLabel="categoryCName" itemValue="categoryCid"/>
	                </form:select>
				</div>
				--%>
				 <div class="span6">
					<label>地区</label>
					<form:select path="provinceCode" onchange="citysOfeastern('provinceCode','shi','${ mallThemeDTO.cityCode}')">
						<form:option value="" label="请选择省份"></form:option>
						<form:options items="${ addresList}" itemLabel="name" itemValue="code"/>
					</form:select>
					<label class="lbl" id="child_label">
						<form:select id="shi" path="cityCode">
							<option value="" >请选择城市</option>
						</form:select>
					</label>
				</div> 
			</div>
			<div class="row-fluid" style="margin-top: 10px;">
				<%-- <div class="span6">
					<label>主题类型</label>
					<form:select path="type">
						<form:option value="0" label="请选择类型"></form:option>
						<form:option value="1" label="首页"></form:option>
						<form:option value="2" label="类目频道"></form:option>
						<form:option value="3" label="地区频道"></form:option>
					</form:select>
				</div> --%>
			 	<div class="span6">
                    <input class="btn  btn-primary"  type="button" value="查询" onclick="page(1,10);" />
                    <input id="btnunset"  class="btn  btn-primary"  type="reset" value="重置" onclick="unset();" />
                </div>
			</div>
		</div>
		<div>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
					<tr>
						<th>编号</th>
						<th>频道名称</th>
						<!-- <th>颜色</th> -->
						<!-- <th>选择颜色</th> -->
						<th>频道类型</th>
						<th>省份</th>
						<th>城市</th>
<!-- 						<th>区县</th> -->
						<!-- <th>地区</th> -->
						<th>排序号</th>
						<th>操作</th>
					</tr>
					<c:forEach items="${page.getList()}" var="mallTheme" varStatus="s">
						<tr>
							<td><c:out value="${s.count}" /></td>
							<td>${mallTheme.themeName }</td>
							<%-- <td>${mallTheme.color }</td>
							<td>${mallTheme.colorb }</td> --%>
							<td>
								<c:if test="${mallTheme.type == '1'}">首页</c:if>
								<c:if test="${mallTheme.type == '2'}">类目频道</c:if>
								<c:if test="${mallTheme.type == '3'}">地区频道</c:if>
							</td>
							<td>
							 	<c:forEach items="${addresList}" var="province">
							 		<c:if test="${province.code==mallTheme.provinceCode }">
							 			${province.name }
							 		</c:if>
							 	</c:forEach>
							</td>
							<td>
								<c:forEach items="${addressArray}" var="city">
							 		<c:if test="${city.code==mallTheme.cityCode }">
							 			${city.name }
							 		</c:if>
							 	</c:forEach>
							</td>
							<%-- <td>
								<c:forEach items="${addressArray}" var="village">
							 		<c:if test="${village.code==mallTheme.villageCode }">
							 			${village.name }
							 		</c:if>
							 	</c:forEach>
							</td> --%>
							<%-- <td>${mallTheme.addName }
								<c:forEach items="${addresList }" var="addList">
									<c:if test="${mallTheme.addressId == addList.code}">
									${addList.name }
									</c:if>
								</c:forEach>
							</td> --%>
							<td>${mallTheme.sortNum }</td>
							<td>
								<c:choose>
									<c:when test="${mallTheme.status==1}">
                                        <a href="javascript:void(0)" onclick="setStatusUse(${mallTheme.id},'2')">下架</a>
                                    </c:when>
									<c:when test="${mallTheme.status==2}">
           	 							<a href="javascript:void(0)" onclick="upStatus(${mallTheme.id},'1','${mallTheme.primaryCid}','${mallTheme.cId}')">上架</a>  
           	 							<a href="javascript:void(0)" onclick="toform(${mallTheme.id})">编辑</a>
										<a href="javascript:void(0)" onclick="todelete(${mallTheme.id})">删除</a>                           
                                    </c:when>
								</c:choose>
								
							</td>
						</tr>
					</c:forEach>
				</table>
				<div class="pagination">${page}</div>
			</div>
		</form:form>
		</div>
	</div>
</body>
</html>