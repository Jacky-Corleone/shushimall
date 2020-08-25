<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>全国体验店管理</title>
	<meta name="decorator" content="default"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=60fMGMicjNNAxQPn92zXdLYo"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" /> 
	<script type="text/javascript">
		$(document).ready(function() {
			$("#reContent").focus();
			$("#inputForm").validate();
		});
		
		function cancel(){
			window.location.href="${ctx}/cms/experStore/list";
		}
		function save(){
			$("#inputForm").submit();
		}
		
        function closeUploadDiv(){
            $("#uploadPicDiv").modal('hide');
        }
        
        function closeUploadDiv(){
            $("#uploadPicDiv").modal('hide');
        }
	</script>
	<style type="text/css">
		#allmap {height: 500px;width:100%;overflow: hidden;}
		#result {width:100%;font-size:12px;}
	</style> 
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/cms/experStore/list">体验店列表</a></li>
		<li class="active"><a href="${ctx}/cms/experStore/addOfPage">体验店添加</a></li>
	</ul><br/>
	<form:form  id="inputForm" modelAttribute="cmsExperStoreDTO" action="${ctx}/cms/experStore/add" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:hidden path="id" id="id" value=""/>
		<form:hidden id="coordinateId" path="coordinate" value="${cmsExperStoreDTO.coordinate}"/>
		<div class="control-group">
			<label class="control-label">省级：</label>
			<div class="controls">
				<form:select path="provinceId" value="" id="provinceId">
					<form:option value="请选择" />
					<%-- <form:options items="${cmsExperStoreDTO.addressList}" itemLabel="name" itemValue="code" htmlEscape="false" />  --%>
						<c:forEach items="${cmsExperStoreDTO.addressList}" var="item">
						<option
							<c:if test="${item.code==cmsExperStoreDTO.provinceId}">selected="true"</c:if>
							value="${item.code}">${item.name}</option>
					</c:forEach>
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">市级：</label>
			<div class="controls">
                  <form:select path="cityId" id="cityId">
                  	<form:option value="请选择" />
                  	<c:forEach items="${cityList}" var="item">
						<option
							<c:if test="${item.code==cmsExperStoreDTO.cityId}">selected="true"</c:if>
							value="${item.code}">${item.name}</option>
					</c:forEach>
                  </form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">店铺名称：</label>
			<div class="controls">
				<form:input path="storeName" id="storeNameId" value="${cmsExperStoreDTO.storeName}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">店铺介绍：</label>
			<div class="controls">
				<form:textarea path="introduce" value="${cmsExperStoreDTO.introduce}"/>
			</div>
		</div>
		
		<div class="control-group" >
			<label class="control-label">店铺地址:：</label>
			<div class="controls">
				<form:input path="address" id="addressId" value="${cmsExperStoreDTO.address}"/>
			</div>
		</div>
		
		<div class="control-group" >
			<label class="control-label">选择您的具体位置</label>
			<div class="controls">
				<div id="allmap">	
				</div>
			</div>
		</div> 
		
		<div class="control-group" >
			<label class="control-label">您选择的坐标是：</label>
			<div class="controls">
				X:<input id="coordinateX" value="${coorX}"/>&nbsp;&nbsp;&nbsp;Y:<input id="coordinateY" value="${coorY}"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="save()">&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="cancel()">
		</div>
	</form:form>
	<script type="text/javascript">
		//下拉框   定位地图区域
		$("#provinceId").change(function(){
			optionItems("${ctx}/cms/experStore/queryAddress",$("#provinceId").val(),"cityId","");
			var province = $("#provinceId").find("option:selected").text();
			var city=$("#cityId").find("option:selected").text();
			var address = "";
			if(province!="" && province != null){
				address=address+province;
			}
			if(city!="" && city != null){
				address=address+city;
			}
			map.centerAndZoom("'"+address+"'",12);
		});
		//下拉框   定位地图区域
		$("#cityId").change(function(){
			var province = $("#provinceId").find("option:selected").text();
			var city=$("#cityId").find("option:selected").text();
			var address = "";
			if(province!="" && province != null){
				address=address+province;
			}
			if(city!="" && city != null){
				address=address+city;
			}
			map.centerAndZoom("'"+address+"'",12);
		});
		
		//省市下拉框关联事件
		function optionItems(url, parentCode, optionId, selectVal){
			$.ajax({
				url:url,
				type:"post",
				data:{
					parentCode:parentCode
				},
				dataType: "json",
				success:function(data){
					var optionItem = $("#"+optionId);
					optionItem.empty();
					optionItem.append($("<option>").text("请选择").val(""));
					$.each(data, function (n, address) {
						var option = $("<option>").text(address.name).val(address.code);
						if(address.code == selectVal){
							option.attr("selected",true);
						}
						optionItem.append(option);
					});
				}
			});
		}
		// 添加标注   设置地图可放大拖拽
	    var map = new BMap.Map('allmap');
 	    if($("#coordinateX").val()!="" && $("#coordinateY").val()!=""){
 	    	var point = new BMap.Point($("#coordinateX").val(),$("#coordinateY").val());
 			map.centerAndZoom(point, 12);
 			var marker = new BMap.Marker(point);  // 创建标注
 			map.addOverlay(marker);               // 将标注添加到地图中
 			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
 	 		map.enableScrollWheelZoom();   //启用滚轮放大缩小，默认禁用
 	 		map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
 	    }else{
 	    	 map.centerAndZoom("北京市",12);
 	 	     map.enableScrollWheelZoom();
 	 	  	 map.enableContinuousZoom();
 	    }
 	   
 	   	var opts = {
 			  width : 200,     // 信息窗口宽度
 			  height: 100,     // 信息窗口高度
 			  title : $("#storeNameId").val() , // 信息窗口标题
 			  enableMessage:true,//设置允许信息窗发送短息
 			  message:"舒适100全国体验店欢迎您的光临！"
 			}
 			var infoWindow = new BMap.InfoWindow($("#addressId").val(), opts);  // 创建信息窗口对象
		//点击鼠标选取坐标
		  map.addEventListener("click",function(e){
			map.clearOverlays(); 
			$("#coordinateX").val(e.point.lng);
			$("#coordinateY").val(e.point.lat);
			$("#coordinateId").val(e.point.lng + "," + e.point.lat);
			 var markerPoint = new BMap.Marker(new BMap.Point(e.point.lng,e.point.lat)); // 创建点
			 map.addOverlay(markerPoint);
			 map.openInfoWindow(infoWindow,point);
		});
		
	</script>
</body>
</html>