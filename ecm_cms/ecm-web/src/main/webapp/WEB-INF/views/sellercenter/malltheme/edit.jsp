<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台主题编辑</title>
	<meta name="decorator" content="default"/>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <%@ include file="/WEB-INF/views/include/spectrum.jsp"%>
    <script type="text/javascript">
    
    $(document).ready(function() {
    	var province_code = "${mallThemeDTO.provinceCode}";
    	var city_code  = "${mallThemeDTO.cityCode}";
    	var village_code = "${mallThemeDTO.villageCode}";
    	$("#clevDiv").hide();	
		$("#addressDiv").show();	
		$("#categoryDiv").hide();
    	if(province_code!=null&&province_code!=""){
    		befforeCitysOfeastern('shi',province_code,city_code);
    	}
    	if(city_code!=null&&city_code!=""){
    		befforeCitysOfeastern('quid',city_code,village_code);
    	}
    	
    	$('#type').change(function(){
			var type=$("#type").val();
			if(type=="1"){
				$("#clevDiv").hide();	
				$("#addressDiv").hide();	
				$("#categoryDiv").hide();	
// 				$("#categoryLev2Div").hide();	
			}
			if(type=="2"){
				$("#clevDiv").show();	
				$("#addressDiv").hide();	
				$("#categoryDiv").show();	
// 				$("#categoryLev2Div").hide();	
    		}
			if(type=="3"){
				$("#clevDiv").hide();	
				$("#addressDiv").show();	
				$("#categoryDiv").hide();	
// 				$("#categoryLev2Div").hide();	
			}
		});
		$("#clev").change(function(){
			var clev = $("#clev").val();
			if(clev=="1"){
				$("#clevDiv").show();	
				$("#addressDiv").hide();	
				$("#categoryDiv").show();	
// 				$("#categoryLev2Div").hide();
			}
			/* 选择二级类目子站 显示二级类目信息
			if(clev=="2"){
				$("#clevDiv").show();	
				$("#addressDiv").hide();	
				$("#categoryDiv").hide();	
				$("#categoryLev2Div").show();
			} */
		})
		
		var type=$("#type").val();
		var clev = $("#clev").val();
		if(type=="1"){
			$("#clevDiv").hide();	
			$("#addressDiv").hide();	
			$("#categoryDiv").hide();	
// 			$("#categoryLev2Div").hide();	
		}
		if(type=="2"){
			$("#clevDiv").show();	
			$("#addressDiv").hide();	
			$("#categoryDiv").show();	
// 			$("#categoryLev2Div").hide();	
			if(clev=="1"){
				$("#clevDiv").show();	
				$("#addressDiv").hide();	
				$("#categoryDiv").show();	
//	 			$("#categoryLev2Div").hide();
			}
		}
		if(type=="3"){
			$("#clevDiv").hide();	
			$("#addressDiv").show();	
			$("#categoryDiv").hide();	
// 			$("#categoryLev2Div").hide();	
		}
		
		/* 选择二级类目子站 显示二级类目信息
		if(clev=="2"){
			$("#clevDiv").show();	
			$("#addressDiv").hide();	
			$("#categoryDiv").hide();	
			$("#categoryLev2Div").show();
		} */
		
		$("#inputForm").validate({
            rules:{
                themeName:"required",
                color:"required",
                colorb:"required",
                type:"required",
                sortNum:{
                    required:true,
                    digits:true
                }
            }
        })
        $("#btnSubmit").click(function(){
            if($("#inputForm").valid()){
            	var id = $("#id").val();
            	var sheng = $("#sheng").val();
            	var shi = $("#shi").val();
            	$.ajax({
                    url: "${ctx}/sellercenter/mallTheme/validCity",
                    dataType:"json",
                    data:{themeId:id,provinceCode:sheng,cityCode:shi},
                    success: function(data){
                        if(data.success){
                             $("#inputForm").submit();
                        }else{
                            $.jBox.info(data.errorMessages[0]);
                        }
                    },error:function(data){
                        $.jBox.info("系统繁忙，请稍后再试");
                    }
                });
            }
        })
    });
    
	 $(function(){   
	    $("#color").spectrum({
			/*是否弹框显示选择颜色*/
			/* flat: true, */
			color: "rgb(${mallThemeDTO.color})",
			showInput: true,
			className: "full-spectrum",
			showInitial: true,
			showPalette: true,
			showSelectionPalette: true,
			maxPaletteSize: 10,
			/*控制选取的颜色格式如：#444 hex,255, 128, 0 rgb*/
			preferredFormat: "rgb",
			localStorageKey: "spectrum.example",
	        hide: function (color) {
				var value=color.toString().substring(4,color.toString().length-1);
				$("#color").val(value);
	        },change: function(color) {
	        	var value=color.toString().substring(4,color.toString().length-1);
				$("#color").val(value);
			},
			palette: [
			            ["#000000", "#434343", "#666666", "#999999", "#b7b7b7", "#cccccc", "#d9d9d9", "#efefef", "#f3f3f3", "#ffffff"],
					    ["#980000", "#ff0000", "#ff9900", "#ffff00", "#00ff00", "#00ffff", "#4a86e8", "#0000ff", "#9900ff", "#ff00ff"],
					    ["#e6b8af", "#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d9ead3", "#c9daf8", "#cfe2f3", "#d9d2e9", "#ead1dc"],
					    ["#dd7e6b", "#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#a4c2f4", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
					    ["#cc4125", "#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6d9eeb", "#6fa8dc", "#8e7cc3", "#c27ba0"],
					    ["#a61c00", "#cc0000", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3c78d8", "#3d85c6", "#674ea7", "#a64d79"],
					    ["#85200c", "#990000", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#1155cc", "#0b5394", "#351c75", "#741b47"],
					    ["#5b0f00", "#660000", "#783f04", "#7f6000", "#274e13", "#0c343d", "#1c4587", "#073763", "#20124d", "#4c1130"]
			        ]
		});
	    
	    $("#colorb").spectrum({
			/*是否弹框显示选择颜色*/
			/* flat: true, */
			color: "rgb(${mallThemeDTO.colorb})",
			showInput: true,
			className: "full-spectrum",
			showInitial: true,
			showPalette: true,
			showSelectionPalette: true,
			maxPaletteSize: 10,
			/*控制选取的颜色格式如：#444 hex,255, 128, 0 rgb*/
			preferredFormat: "rgb",
			localStorageKey: "spectrum.example",
	        hide: function (color) {
				var value=color.toString().substring(4,color.toString().length-1);
				$("#colorb").val(value);
	        },change: function(color) {
	        	var value=color.toString().substring(4,color.toString().length-1);
				$("#colorb").val(value);
			},
			palette: [
			            ["#000000", "#434343", "#666666", "#999999", "#b7b7b7", "#cccccc", "#d9d9d9", "#efefef", "#f3f3f3", "#ffffff"],
					    ["#980000", "#ff0000", "#ff9900", "#ffff00", "#00ff00", "#00ffff", "#4a86e8", "#0000ff", "#9900ff", "#ff00ff"],
					    ["#e6b8af", "#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d9ead3", "#c9daf8", "#cfe2f3", "#d9d2e9", "#ead1dc"],
					    ["#dd7e6b", "#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#a4c2f4", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
					    ["#cc4125", "#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6d9eeb", "#6fa8dc", "#8e7cc3", "#c27ba0"],
					    ["#a61c00", "#cc0000", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3c78d8", "#3d85c6", "#674ea7", "#a64d79"],
					    ["#85200c", "#990000", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#1155cc", "#0b5394", "#351c75", "#741b47"],
					    ["#5b0f00", "#660000", "#783f04", "#7f6000", "#274e13", "#0c343d", "#1c4587", "#073763", "#20124d", "#4c1130"]
			        ]
		});
	    $("#colorb").show();
	    $("#color").show();
	});
	 
	//根据一级类目 得到二级类目
		function onSelect(cid){
			var html = "<option value=''>二级类目</option>";
	    	$.ajax({
	    		type:"post",
	    		url:"${ctx}/brand/getChildCategory?pCid="+cid,
	    		dataType:"json",
	    		success:function(data){
	                $(data).each(function(i,item){
	                	html += "<option ";
	                    if(item.categoryCid=='${mallThemeDTO.platformId2}'){
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
	
		//根据省的编号来获取城市的下拉框
        function citysOfeastern(parentid,childid){
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
                                html += "<option value='"+item.code+"'>"+item.name+"</option>";
                            });
                        }
                        $("#"+childid).html(html);
                    }
                }
            });
        }
		
        function befforeCitysOfeastern(childid,code,ziCode){
            $.ajax({
                url:"${ctx}/businesslist/selectcity/",
                type:"post",
                data:{
                	parentCode:code
                },
                dataType:'json',
                success:function(data){
                    console.log(data);
                    if(data.success){
                        var html=""
                        if(data.obj){
                            $(data.obj).each(function(i,item){
                            	if(item.code==ziCode){
	                                html += "<option value='"+item.code+"'>"+item.name+"</option>";
			                        $("#s2id_"+childid+" span:first-child").html(item.name);
                            	}else{
	                                html += "<option value='"+item.code+"'>"+item.name+"</option>";
                            	}
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
	<form:form id="inputForm" modelAttribute="mallThemeDTO" method="post" action="${ctx}/sellercenter/mallTheme/addOrUpdate" class="form-horizontal">
		 <input type="hidden" name="status" id="status" value="1"/>
		<div class="control-group">
			<label class="control-label">子站导航名称:</label>
			<div class="controls">
				<label class="lbl">
					<input type="hidden" value="${mallThemeDTO.id}" name="id" id="id" maxlength="25" style="width:220px;">
					<form:input path="themeName" maxlength="50" style="width:220px;" required="true" />
					<span>限50个字符以内</span>
				</label>
			</div>
		</div>
		<%-- 
		<div class="control-group">
			<label class="control-label">子站类型:</label>
			<div class="controls">
				<label class="lbl">
					<form:select path="type">
						<form:option value="1" label="首页"></form:option> 
						<form:option value="2" label="类目子站"></form:option>
						<form:option value="3" label="地区子站"></form:option>
					</form:select>
				</label>
			</div>
		</div>
		--%>
		<input type = "hidden" id="type" name = "type" value ="3" />
		<div class="control-group" id="clevDiv" style="display: block;">
			<label class="control-label">类目级别:</label>
			<div class="controls">
				<label class="lbl">
					<form:select path="clev">
<%-- 						<form:option value="" label="请选择类型"></form:option> --%>
						<form:option value="1" label="一级类目子站"></form:option>
<%-- 						<form:option value="2" label="二级类目子站"></form:option> --%>
					</form:select>
				</label>
			</div>
		</div>
		
		<div class="control-group" id="addressDiv" style="display: block;">
			<label class="control-label">地区:</label>
			<div class="controls">
				<label class="lbl">
					<select name = "provinceCode" id="sheng" onchange="citysOfeastern('sheng','shi')" required="true">
						<option value = "">请选择地区</option>
						<c:forEach items="${ addresList}" var="address">
							<option value = "${address.code }" <c:if test="${address.code==mallThemeDTO.provinceCode}">selected="selected"</c:if> >${address.name }</option>
						</c:forEach>
					</select><br/>
					<br/>
					<select name="cityCode" id="shi" class="form-control" class="required" required="true">
                        <option value="">请选择</option>
                    </select><br/><br/>
				</label>
			</div>
		</div>
		
		<div class="control-group" id="categoryDiv" style="display: block;">
			<label class="control-label">一级类目:</label>
			<div class="controls">
				<form:select path="cId">
					<form:option value="" label="选择一级类目"></form:option>
					<form:options items="${ categoryList}" itemLabel="categoryCName" itemValue="categoryCid"/>
				</form:select>
			</div>
		</div>
		
		<%-- 添加二级类目子站功能屏蔽  功能以实现
		<div class="control-group" id="categoryLev2Div" style="display: block;">
			<label class="control-label">二级类目:</label>
			<div class="controls">
                <form:select path="platformId1" cssClass="input-medium" onchange="onSelect(this.value)">
						<form:option value="" label="一级类目"></form:option>
						<form:options items="${ categoryList}" itemLabel="categoryCName" itemValue="categoryCid"/>
					</form:select>
					<form:select path="platformId2" cssClass="input-medium">
	                     <form:option value="" label="二级分类"></form:option>
	                     <form:options items="${ categoryLev2List}" itemLabel="categoryCName" itemValue="categoryCid"/>
	                </form:select>
			</div>
		</div> 
		<div class="control-group">
			<label class="control-label">颜色:</label>
			<div class="controls">
				<form:input path="color" htmlEscape="false" maxlength="30" type="text"
					class="required triggerSet" required="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">选择背景色:</label>
			<div class="controls">
				<form:input path="colorb" htmlEscape="false" maxlength="30" type="text"
					class="required triggerSet" required="true"/>
			</div>
		</div> --%>
		
		<div class="control-group">
			<label class="control-label">排序号:</label>
			<div class="controls">
				<form:input path="sortNum" htmlEscape="false" maxlength="10" class="required" required="true" title="序号只能是整数"/>
			</div>
		</div>
		
		<div class="control-group">
            <div class="controls">
                <input id="btnSubmit" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
                <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
		</div>
	</form:form>
</body>
</html>