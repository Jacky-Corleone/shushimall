<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>品牌列表</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style>
        h3{
            color:#000000;
            height: 46px;
            line-height: 46px;
            text-indent: 20px;
            font-size: 15px;
            font-family: \5FAE\8F6F\96C5\9ED1;
            font-weight: 500;
        }
        table.td-cen th,.td-cen td{
            text-align: center;
            vertical-align: middle;
        }.hhtd td{
             word-wrap:break-word;
             word-break:break-all;
         }
    </style>
<script type="text/javascript">
		$(document).ready(function() {

            //图片查看
            $('.showimg').fancyzoom({
                Speed: 400,
                showoverlay: false,
                imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
            });
            $("#treeTable").treeTable({expandLevel : 5});

		});
		
		
		function page(n,s){
			if(n>0){
				
			}else{
				n =1;
			}
			if(s>0){
				
			}else{
				s =10;
			}
			$("#pageNo").val(n);
			$("#pageSize").val(s); 
			$("#searchForm").attr("action","${ctx}/brand/brandsList").submit();
	    	return false;
	    }
		function pagesub(n,s){
			if(n>0){
				
			}else{
				n =1;
			}
			if(s>0){
				
			}else{
				s =10;
			}
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#searchForm").attr("action","${ctx}/brand/brandsList").submit();
	    }
		function deleteBrand(brandId){
			
			top.$.jBox.confirm("确认要删除吗？","系统提示",function(v,h,f){
                if(v == "ok"){
                	deleteBrandById(brandId);
                }
            },{buttonsFocus:1});
			
			
		}
		
		function deleteBrandById(brandId){
			 $.ajax({
		            url : "${ctx}/brand/deleteBrand?brandId="+brandId,
		            type : "POST",
		            dataType : "json",
		            success : function(data) {
		                if(data.success){
		                    $.jBox.info("操作成功!");
		                    location.href="${ctx}/brand/brandsList";
		                }else{
		                    $.jBox.info(data.msg);
		                }
		            },
		            error : function(xmlHttpRequest, textStatus, errorThrown) {
		                $.jBox.info("系统错误！请稍后再试！");
		            }
		        });
			
			
			
			
		}
		//当选中所有的时候，全选按钮会勾上
		function setSelectAll() {
			var obj = document.getElementsByName("checkAll");
			var count = obj.length;
			var selectCount = 0;

			for ( var i = 0; i < count; i++) {
				if (obj[i].checked == true) {
					selectCount++;
				}
			}
			if (count == selectCount) {
				document.all.selAll.checked = true;
			} else {
				document.all.selAll.checked = false;
			}
		}
		
		function selectAll() {
			var obj = document.getElementsByName("checkAll");
			if (document.getElementById("selAll").checked == false) {
				for ( var i = 0; i < obj.length; i++) {
					obj[i].checked = false;
				}
			} else {
				for ( var i = 0; i < obj.length; i++) {
					obj[i].checked = true;
				}
			}

		}
</script>
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <div class="container-fluid">
        <tags:message content="${message}"/>
        <form:form name="searchForm" modelAttribute="itemBrandDTO" id="searchForm" method="post" action="${ctx}/brand/brandsList">
		    <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="row-fluid" style="margin-top: 10px;">
                <div class="span3"><label>品牌名称：&nbsp;</label><form:input path="brandName"  class="input-medium"></form:input></div>
                <div class="span3">
                    <label>
                                                             品牌状态：      
                    </label>
                    <form:select path="brandStatus" cssClass="input-medium">
                        <form:option value="0"  label="全部" />
                        <form:option value="1"  label="有效"/>
                        <form:option value="2"  label="无效"/>
                    </form:select>
                </div>
                 <div class="span2">
                    <button class="btn btn-primary" onclick="return pagesub();" >搜索</button>
                </div>
            </div>
	    </form:form>
        
        </div>
        
        <div class="container-fluid">
        <div class="row-fluid" style="margin-top: 15px">
        <form name="soldOutAll" id="soldOutAll" method="post" >
            <table id="treeTable" class="table table-striped table-bordered table-condensed td-cen hhtd">
                    <tr>
                        <th style="width: 30px;"><input type="checkbox" id="selAll" onclick="selectAll();"  /></th>
                        <th style="width: 50px;">序号</th>
                        <th style="width: 350px;">品牌信息</th>
                        <th>是否有效</th>
                        <th>操作</th>
                    </tr>

                 <c:forEach items="${page.list}" var="t" varStatus="s">
                    <tr id="tr${t.brandId}" >
                        <td style="width: 30px;"><input id="checkItem_${t.brandId }" type="checkbox" value="${t.brandId}"  name="checkAll" id="checkAll" onclick="setSelectAll();" /></td>
                        <td><c:out value="${s.count}" /></td>
                        <td>
                        <img class="showimg" style="height: 100px;width: 100px;padding-left: 10px"  src="${filePath}${t.brandLogoUrl}" >
                        <label style="width: 160px;">
                        <a  class="z-link01" style="margin:2px 0;display: block;">
                        ${t.brandName}
                        </a>
                        </label>
                        </td>
                        <td>
                        <c:if test="${not empty t.brandStatus}">
                            <c:if test="${t.brandStatus=='1'}">
                               <p style="color:#a0ba59;">有效</p>
                            </c:if>
                            <c:if test="${t.brandStatus=='2'}">
                             <p style="color:#a0ba59;">无效</p>
                            </c:if>
                        </c:if>
                        </td>
						<td>
						<c:if test="${t.brandStatus=='1'}">
						 <a href="javascript:void(0);" onclick="deleteBrand('${t.brandId}')">删除</a>
						 </c:if>
						 <c:if test="${t.brandStatus=='2'}">
						  <p style="color:#a0ba59;">删除</p>
						 </c:if>
						</td>
                    </tr>
                </c:forEach>
            </table>
            </form>
            </div>
            </div>
        
       <div class="pagination">${page}</div> 
        
        </div>
        </div>
</body>
</html>