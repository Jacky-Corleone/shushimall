<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
	<title>楼层基本信息</title>
	<meta name="decorator" content="default"/>
	<%@ include file="/WEB-INF/views/include/spectrum.jsp"%>
	<script type="text/javascript" src="${ctxStatic}/ganged.js"></script>
	<script type="text/javascript">
	$(function(){
			$("#titleDTO").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
				}
			});
			
			//颜色选择器
			$("#triggerSetBg").spectrum({
			    /*是否弹框显示选择颜色*/
			    /* flat: true, */
			    color: "rgb(${mallRec.bgColorDTO})",
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
					$("#triggerSetBg").val(value);
		        },change: function(color) {
		        	var value=color.toString().substring(4,color.toString().length-1);
					$("#triggerSetBg").val(value);
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
			$("#triggerSetBg").show();

		//颜色选择器
		$("#triggerSet").spectrum({
			/*是否弹框显示选择颜色*/
			/* flat: true, */
			color: "rgb(${mallRec.colorrefDTO})",
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
				$("#triggerSet").val(value);
	        },change: function(color) {
	        	var value=color.toString().substring(4,color.toString().length-1);
				$("#triggerSet").val(value);
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
		$("#triggerSet").show();

		$("#inputForm").validate();
		});

	
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="mallRec" onsubmit="return validate()" action="${ctx}/station/mallRec/save?themeType=${mallRec.recTypeDTO}" method="post" class="form-horizontal">
		<form:hidden path="idDTO"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">频道:</label>
			<div class="controls">
				<label class="lbl">
					<form:select id="themeId" path="themeId" class="required">
						<form:option value="" label="请选择频道"/>
						<form:options items="${themeList}"  itemLabel="themeName"  itemValue="id" class="required"/>
					</form:select>
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="titleDTO">楼层名称:</label>
			<div class="controls">
				<form:input path="titleDTO" htmlEscape="false" maxlength="7" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="floorNumDTO">楼层排序:</label>
			<div class="controls">
				<form:input path="floorNumDTO"  htmlEscape="false" maxlength="10" class="required number"/>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label" for="colorrefDTO">色值:</label>
			<div class="controls">
				<form:input path="colorrefDTO"  htmlEscape="false" maxlength="30" class="required triggerSet"  type="text"  id="triggerSet"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="colorrefDTO">楼层导航栏背景色值:</label>
			<div class="controls">
				<form:input path="bgColorDTO"  htmlEscape="false" maxlength="30" class="required triggerSet"  type="text"  id="triggerSetBg"/>
			</div>
		</div> --%>
		<div class="control-group">
            <div class="controls">
                <input class="btn btn-primary" type="submit"  value="保存"/>
                <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
        </div>
	</form:form>
</body>
</html>