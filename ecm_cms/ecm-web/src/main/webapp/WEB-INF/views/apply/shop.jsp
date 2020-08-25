<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>卖家店铺信息</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%@ include file="/WEB-INF/views/include/dialog.jsp"%>
    <script type="text/javascript">
        $(document).ready(function() {


            $("#treeTable").treeTable({expandLevel : 5});
            $("#searchForm").validate({
                rules: {
                    sellerId: {
                        digits: true
                    }
                }
            });
            $("#btnQuery").click(function(){
            	if(!checkInputDate()){
            		return false;
            	}else{
            		$("#searchForm").submit();
            	}
            });

            $("#btnSubApprove").click(function(){
                //店铺审核通过2
                var shopId = $("#shopId").val();
                var comment = $("#comment").val();
                var dat = "shopId="+shopId+"&status=2&comment="+comment;
                $.ajax({
                    url:"saveAudit",
                    data:dat,
                    dataType:"json",
                    success:function(data){
                        $("#approveDiv").modal('hide');
                        if(data.success){
                            $($("#tr"+shopId).children()[6]).html("审核通过");
                            var html = "<a href='javascript:void(0)' onclick='showShopInfo("+shopId+")'>查看审核信息</a>";
                            $($("#tr"+shopId).children()[9]).html(html);
                            $.jBox.info("审核成功");
                        }else{
                            $.jBox.info(data.msg);
                        }


                    }
                });
            });
            $("#btnBackApprove").click(function(){
                var auditRemarkmen=$("#comment").val();
                if(!auditRemarkmen){
                    $.jBox.info("请输入审核意见");
                    return;
                }
                // 店铺审核驳回3
                var shopId = $("#shopId").val();
                var comment = $("#comment").val();
                var dat = "shopId="+shopId+"&status=3&comment="+comment;
                $.ajax({
                    url:"saveAudit",
                    data:dat,
                    dataType:"json",
                    success:function(data){
                        $("#approveDiv").modal('hide');
                        if(data.success){
                            $($("#tr"+shopId).children()[6]).html("已驳回");
                            var html = "<a href='javascript:void(0)' onclick='showShopInfo("+shopId+")'>查看审核信息</a>";
                            $($("#tr"+shopId).children()[9]).html(html);
                            $.jBox.info("驳回成功");
                        }else{
                            $.jBox.info(data.msg);
                        }
                    }
                });
            });
            $("#btnCloseApprove").click(function(){
                $("#approveDiv").modal('hide');
            });
            $("#btnCloseInfo").click(function(){
                $("#shopInfoDiv").modal('hide');
            });
        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function approveShop(id){
            //先检查一下合同
            $.ajax({
                url:"checkContract",
                data:"shopId="+id,
                dataType:"json",
                success:function(data){
                    if(data.success){
                        $("#shopId").val(id);
                        $("#approveDiv").modal('show');
                        $.ajax({
                            url:"shopInfo",
                            data:"id="+id,
                            dataType:"json",
                            success:function(data){
                                $("#shopIdInfo1").html(data.shop.shopId);
                                if(data.companyName){
                                    $("#companyNameInfo1").html(data.companyName);
                                }else{
                                    $("#companyNameInfo1").html("无");
                                }

                                $("#shopNameInfo1").html(data.shop.shopName);
                                if(1==data.shop.shopType){
                                    $("#shopTypeInfo1").html("品牌商");//店铺类型 1 品牌商 2经销商
                                }
                                if(2==data.shop.shopType){
                                    $("#shopTypeInfo1").html("经销商");//店铺类型 1 品牌商 2经销商
                                }
                                if(1==data.shop.brandType){
                                    $("#brandTypeInfo1").html("国内品牌");//品牌类型  1 国内品牌 2国际品牌
                                }
                                if(2==data.shop.brandType){
                                    $("#brandTypeInfo1").html("国际品牌");//品牌类型  1 国内品牌 2国际品牌
                                }
                                if(1==data.shop.businessType){
                                    $("#businessTypeInfo1").html("自有品牌"); //经营类型  1自有品牌 2 代理品牌
                                }
                                if(2==data.shop.businessType){
                                    $("#businessTypeInfo1").html("代理品牌"); //经营类型  1自有品牌 2 代理品牌
                                }

                                if(null!=data.shop.disclaimer&&""!=data.shop.disclaimer){
                                    $("#disclaimer1").html("<img class='showimg' src='${filePath}"+data.shop.disclaimer+"' style='width:80px;height:20px'>");//免责声明
                                }else{
                                    $("#disclaimer1").html("无");//免责声明
                                }
                                if(null!=data.shop.trademarkRegistCert&&""!=data.shop.trademarkRegistCert){
                                    $("#trademarkRegistCert1").html("<img class='showimg' src='${filePath}"+data.shop.trademarkRegistCert+"' style='width:80px;height:20px'>");//商标注册证/商品注册申请书扫描件
                                }else{
                                    $("#trademarkRegistCert1").html("无");
                                }
                                if(null!=data.shop.inspectionReport&&""!=data.shop.inspectionReport){
                                    $("#inspectionReport1").html("<img class='showimg' src='${filePath}"+data.shop.inspectionReport+"' style='width:80px;height:20px'>");//质检、检疫、检验报告/报关单类扫描件
                                }else{
                                    $("#inspectionRepor1").html("无");
                                }
                                if(null!=data.shop.productionLicense&&""!=data.shop.productionLicense){
                                    $("#productionLicense1").html("<img class='showimg' src='${filePath}"+data.shop.productionLicense+"' style='width:80px;height:20px'>");//卫生/生产许可证扫描件
                                }else{
                                    $("#productionLicense1").html("无");
                                }
                                if(null!=data.shop.marketingAuth&&""!=data.shop.marketingAuth){
                                    if(data.shop.marketingAuth.toLowerCase().indexOf(".rar") != -1
                                			|| data.shop.marketingAuth.toLowerCase().indexOf(".zip") != -1
                                			|| data.shop.marketingAuth.toLowerCase().indexOf(".7z") != -1){
                                		$("#marketingAuth1").html("<a href='${filePath}"+data.shop.marketingAuth+"'><img src='${pageContext.request.contextPath}/static/images/zip.png' style='width:32px;height:32px'></a>");//销售授权书扫描件
                                	}else{
                                		$("#marketingAuth1").html("<img class='showimg' src='${filePath}"+data.shop.marketingAuth+"' style='width:80px;height:20px'>");//销售授权书扫描件
                                	}
                                }else{
                                    $("#marketingAuth1").html("无");
                                }
                                if(null!=data.shop.gpCommitmentBook&&""!=data.shop.gpCommitmentBook){
                                    $("#gpCommitmentBook1").html("<img class='showimg' src='${filePath}"+data.shop.gpCommitmentBook+"' style='width:80px;height:20px'>");//承诺书
                                }else{
                                	$("#gpCommitmentBook1").html("无");
                                }
                                if(data.itemCaList){
                                    var items = "";
                                    $(data.itemCaList).each(function(i,t){
                                        items += t+"<br>";
                                    });
                                    $("#itemCatogaryInfo1").html(items);
                                }
                                if(data.brandList){
                                    var brans = "";
                                    $(data.brandList).each(function(i,item){
                                        brans +="<img class='showimg' src='${filePath}"+item+"' style='width:80px;height:20px'>";
                                    });
                                    $("#brandInfo1").html(brans);
                                    //图片查看
                                    $('.showimg').fancyzoom({
                                        Speed: 400,
                                        imagezindex:1100,
                                        showoverlay: false,
                                        imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
                                    });
                                }

                            }
                        });
                    }else{
                        $.jBox.info("店铺合同还未审核通过，请先去审核合同");
                    }
                }
            });

        }
        //打开店铺信息窗口
        function showShopInfo(id){
            $("#shopId").val(id);
            $("#shopInfoDiv").modal('show');
            $.ajax({
                url:"shopInfo",
                data:"id="+id,
                dataType:"json",
                success:function(data){
                    $("#shopIdInfo2").html(data.shop.shopId);
                    if(data.companyName){
                        $("#companyNameInfo2").html(data.companyName);
                    }else{
                        $("#companyNameInfo2").html("无");
                    }
                    $("#auditInfo2").html(data.shop.comment);
                    $("#shopNameInfo2").html(data.shop.shopName);

                    if(1==data.shop.shopType){
                        $("#shopTypeInfo2").html("品牌商");//店铺类型 1 品牌商 2经销商
                    }
                    if(2==data.shop.shopType){
                        $("#shopTypeInfo2").html("经销商");//店铺类型 1 品牌商 2经销商
                    }
                    if(1==data.shop.brandType){
                        $("#brandTypeInfo2").html("国内品牌");//品牌类型  1 国内品牌 2国际品牌
                    }
                    if(2==data.shop.brandType){
                        $("#brandTypeInfo2").html("国际品牌");//品牌类型  1 国内品牌 2国际品牌
                    }
                    if(1==data.shop.businessType){
                        $("#businessTypeInfo2").html("自有品牌"); //经营类型  1自有品牌 2 代理品牌
                    }
                    if(2==data.shop.businessType){
                        $("#businessTypeInfo2").html("代理品牌"); //经营类型  1自有品牌 2 代理品牌
                    }
                    if(null!=data.shop.disclaimer&&""!=data.shop.disclaimer){
                        $("#disclaimer2").html("<img class='showimg' src='${filePath}"+data.shop.disclaimer+"' style='width:80px;height:20px'>");//免责声明
                    }else{
                        $("#disclaimer2").html("无");//免责声明
                    }
                    if(null!=data.shop.trademarkRegistCert&&""!=data.shop.trademarkRegistCert){
                        $("#trademarkRegistCert2").html("<img class='showimg' src='${filePath}"+data.shop.trademarkRegistCert+"' style='width:80px;height:20px'>");//商标注册证/商品注册申请书扫描件
                    }else{
                        $("#trademarkRegistCert2").html("无");
                    }
                    if(null!=data.shop.inspectionReport&&""!=data.shop.inspectionReport){
                        $("#inspectionReport2").html("<img class='showimg' src='${filePath}"+data.shop.inspectionReport+"' style='width:80px;height:20px'>");//质检、检疫、检验报告/报关单类扫描件
                    }else{
                        $("#inspectionReport2").html("无");
                    }
                    if(null!=data.shop.productionLicense&&""!=data.shop.productionLicense){
                        $("#productionLicense2").html("<img class='showimg' src='${filePath}"+data.shop.productionLicense+"' style='width:80px;height:20px'>");//卫生/生产许可证扫描件
                    }else{
                        $("#productionLicense2").html("无");
                    }
                    if(null!=data.shop.marketingAuth&&""!=data.shop.marketingAuth){
                    	if(data.shop.marketingAuth.toLowerCase().indexOf(".rar") != -1
                    			|| data.shop.marketingAuth.toLowerCase().indexOf(".zip") != -1
                    			|| data.shop.marketingAuth.toLowerCase().indexOf(".7z") != -1){
                    		$("#marketingAuth2").html("<a href='${filePath}"+data.shop.marketingAuth+"'><img src='${pageContext.request.contextPath}/static/images/zip.png' style='width:32px;height:32px'></a>");//销售授权书扫描件
                    	}else{
                    		$("#marketingAuth2").html("<img class='showimg' src='${filePath}"+data.shop.marketingAuth+"' style='width:80px;height:20px'>");//销售授权书扫描件
                    	}
                    }else{
                        $("#marketingAuth2").html("无");
                    }
                    if(null!=data.shop.gpCommitmentBook&&""!=data.shop.gpCommitmentBook){
                        $("#gpCommitmentBook").html("<img class='showimg' src='${filePath}"+data.shop.gpCommitmentBook+"' style='width:80px;height:20px'>");//承诺书
                    }else{
                    	$("#gpCommitmentBook").html("无");
                    }

                    if(data.itemCaList){
                        var items = "";
                        $(data.itemCaList).each(function(i,t){
                            items += t+"<br>";
                        });
                        $("#itemCatogaryInfo2").html(items);
                    }
                    if(data.brandList){
                        var brans = "";
                        $(data.brandList).each(function(i,item){
                            brans +="<img class='showimg' src='${filePath}"+item+"' style='width:80px;height:20px'>";
                        });
                        $("#brandInfo2").html(brans);
                        //图片查看
                        $('.showimg').fancyzoom({
                            Speed: 400,
                            showoverlay: false,
                            imagezindex:1100,
                            imgDir: '${ctxStatic}/jquery-fancyzoom/ressources/'
                        });
                    }

                }
            });

        }

        function switchShop(id,status){
            $.ajax({
                url:"shopSwitch",
                data:"shopId="+id+"&status="+status,
                dataType:"json",
                success:function(data){
                    if(data.success){
                        $.jBox.info(data.msg);
                        if(status==4){
                            $($("#tr"+id).children()[6]).html("已开通");
                            var html = "<a href='javascript:void(0)' onclick='switchShop("+id+",5)'>关闭店铺</a>|";
                            html += "<a href='javascript:void(0)' onclick='showShopInfo("+id+")'>查看审核信息</a>";
                            $($("#tr"+id).children()[9]).html(html);
                        }
                        if(status==5){
                            $($("#tr"+id).children()[6]).html("平台关闭");
                            var html = "<a href='javascript:void(0)' onclick='switchShop("+id+",4)'>开通店铺</a>|";
                            html += "<a href='javascript:void(0)' onclick='showShopInfo("+id+")'>查看审核信息</a>";
                            $($("#tr"+id).children()[9]).html(html);
                        }
                    }else{
                        $.jBox.info(data.msg);
                    }
                }
            });
        }
        function checkInputDate(){
       	 var timeBegin = $("#createdstr").val();
            var timeEnd = $("#createdend").val();
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
</head>
<body>
<div class="content sub-content">
    <div class="content-body content-sub-body">
        <tags:message content="${message}"/>
        <form name="searchForm" id="searchForm" method="post" action="${ctx}/shop/list"  >
            <input id="pageNo" name="page" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="rows" type="hidden" value="${page.pageSize}" />
            <div class="container-fluid">
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="shopName" title="店铺名称">
                            店铺名称
                        </label>
                        <input name="shopName" id="shopName"  type="text" class="form-control" value="${shop.shopName}" />
                    </div>
                    <div class="span6">
                        <label class="label-control" for="sellerId" title="商家编号">
                            商家编号
                        </label>
                        <input type="text"  id="sellerId" name="sellerId"  value="${shop.sellerId}" title="只能是数字"/>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        <label class="label-control" for="status" title="店铺状态">
                            店铺状态
                        </label>
                        <select name="status" id="status">
                            <option value="">全部</option>
                            <option value="1" <c:if test="${shop.status==1}">selected="selected" </c:if> >待审核</option>
                            <option value="2" <c:if test="${shop.status==2}">selected="selected" </c:if>>审核通过</option>
                            <option value="3" <c:if test="${shop.status==3}">selected="selected" </c:if>>已驳回</option>
                            <option value="4" <c:if test="${shop.status==4}">selected="selected" </c:if>>平台关闭</option>
                            <option value="5" <c:if test="${shop.status==5}">selected="selected" </c:if>>已开通</option>
                        </select>
                    </div>
                    <div class="span6">
                        <label class="label-control" for="createdstr" title="申请时间">
                            申请时间
                        </label>
                        <input id="createdstr" name="createdstr" value='<fmt:formatDate value="${shop.createdstr}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-small Wdate" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'createdend\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly">到
                        <input id="createdend" name="createdend" value='<fmt:formatDate value="${shop.createdend}" pattern="yyyy-MM-dd"/>' type="text" class="form-control input-small Wdate" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createdstr\')}',dateFmt:'yyyy-MM-dd'});" readonly="readonly">
                    </div>
                </div>
                <div class="row-fluid">
                  <div class="span6">
								<label class="label-left control-label" for="platformId"
									title="平台类型">平台类型</label>
								<select name="platformId" id="platformId" 
									class="form-control">
									<option value="">所有</option>
									<option value="0" <c:if test="${shop.platformId==0}">selected="selected"</c:if>>舒适100平台 </option>
<%-- 									<option value="2" <c:if test="${shop.platformId==2}">selected="selected"</c:if>>绿印平台 </option> --%>
								</select>
				</div>
                    <div class="span6">
                        <button class="btn btn-primary" id="btnQuery">查询</button>
                    </div>
                </div>
            </div>
        </form>

            <table id="treeTable" class="table table-striped table-bordered table-condensed">
                <tr>
                    <th>编号</th>
                    <th>店铺编号</th>
                    <th>店铺名称</th>
                    <th>商家编号</th>
                    <th>公司名称</th>
                    <th>申请提交时间</th>
                    <th>店铺状态</th>
                    <th>平台类型</th>
                    <th>店铺域名</th>
                    <th>操作</th>
                </tr>
                <c:forEach items="${page.list}" var="s" varStatus="status">
                    <tr id="tr${s.shopId}">
                        <td>${status.count}</td>
                        <td>${s.shopId}</td>
                        <td>${s.shopName}</td>
                        <td>${s.sellerId}</td>
                        <td>${s.companyName}</td>
                        <td><fmt:formatDate value="${s.created}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
                        <td>
                            <c:choose>
                                <c:when test="${null==s.status||1==s.status}">
                                    待审核
                                </c:when>
                                <c:when test="${null!=s.status&&2==s.status}">
                                    审核通过
                                </c:when>
                                <c:when test="${null!=s.status&&3==s.status}">
                                    已驳回
                                </c:when>
                                <c:when test="${null!=s.status&&4==s.status}">
                                    平台关闭
                                </c:when>
                                <c:when test="${null!=s.status&&5==s.status}">
                                    已开通
                                </c:when>
                                <c:otherwise>
                                   无法识别
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${s.platformId==null}">舒适100平台</c:when>
<%--                                 <c:when test="${s.platformId==2}">绿印平台</c:when> --%>
                            </c:choose>
                        </td>
                                                <td>
                            <c:choose>
                                <c:when test="${s.platformId==null}">
                                    ${s.shopUrl}.shushi100.com  
                                </c:when>
                                <c:when test="${s.platformId==2}">
                                    ${s.shopUrl}
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${null==s.status||1==s.status}">
                                    <a href="javascript:void(0)" onclick="approveShop(${s.shopId});">审核店铺</a>
                                </c:when>
                                <c:when test="${null==s.status||4==s.status}">
                                    <a href="javascript:void(0)" onclick="switchShop(${s.shopId},${s.status});">开通店铺</a>|
                                    <a href="javascript:void(0)" onclick="showShopInfo(${s.shopId})">查看审核信息</a>
                                </c:when>
                                <c:when test="${null==s.status||5==s.status}">
                                    <a href="javascript:void(0)" onclick="switchShop(${s.shopId},${s.status});">关闭店铺</a>|
                                    <a href="javascript:void(0)" onclick="showShopInfo(${s.shopId})">查看审核信息</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:void(0)" onclick="showShopInfo(${s.shopId})">查看审核信息</a>
                                </c:otherwise>
                            </c:choose>

                        </td>
                    </tr>
                </c:forEach>
            </table>
        <div class="pagination">${page}</div>
    </div>
</div>

<input type="hidden" name="shopId" value="" id="shopId">
<!--店铺审核弹出框-->
<div class="modal hide fade" id="approveDiv" style="height: 400px;overflow-y: scroll">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>店铺审核</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <div class="span6">
                <label>商家编号:<span id="shopIdInfo1"></span></label>
            </div>
            <div class="span6">
                <label>公司名称:<span id="companyNameInfo1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <label>店铺名称:<span id="shopNameInfo1"></span></label>
            </div>
            <div class="span6">
                <label>店铺类型:<span id="shopTypeInfo1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label>运营分类:<span id="itemCatogaryInfo1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span6">
                <label>品牌类型:<span id="brandTypeInfo1"></span></label>
            </div>
            <div class="span6">
                <label>经营类型:<span id="businessTypeInfo1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label>运营品牌:<span id="brandInfo1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label>免责声明:<span id="disclaimer1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label>商标注册:<span id="trademarkRegistCert1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label>报关单:<span id="inspectionReport1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label>卫生/生产许可:<span id="productionLicense1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12">
                <label>销售授权书:<span id="marketingAuth1"></span></label>
            </div>
        </div>
        <div class="row-fluid">
                    <div class="span12">
                        <label>承诺书:<span id="gpCommitmentBook1"></span></label>
                    </div>
                </div>
        <div class="row-fluid">
            <div class="span12">
                <label class="label-control" for="comment" title="审核说明">
                    审核说明:
                </label>
                <textarea rows="3" id="comment"></textarea>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn btn-primary" id="btnSubApprove">通过</a>
        <a href="#" class="btn btn-primary" id="btnBackApprove">驳回</a>
        <a href="#" class="btn" id="btnCloseApprove">取消</a>
    </div>
</div>
<!--店铺信息出框-->
<div class="modal hide fade" id="shopInfoDiv" style="height: 400px;  overflow-y: scroll">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>店铺信息</h3>
    </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="span6">
                    <label>商家编号:<span id="shopIdInfo2"></span></label>
                </div>
                <div class="span6">
                    <label>公司名称:<span id="companyNameInfo2"></span></label>
                </div>
            </div>

            <div class="row-fluid">
                <div class="span6">
                    <label>店铺名称:<span id="shopNameInfo2"></span></label>
                </div>
                <div class="span6">
                    <label>店铺类型:<span id="shopTypeInfo2"></span></label>
                </div>
            </div>

            <div class="row-fluid">
                <div class="span12">
                    <label>运营分类:<span id="itemCatogaryInfo2"></span></label>
                </div>
            </div>

            <div class="row-fluid">
                <div class="span6">
                    <label>品牌类型:<span id="brandTypeInfo2"></span></label>
                </div>
                <div class="span6">
                    <label>经营类型:<span id="businessTypeInfo2"></span></label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <label>运营品牌:<span id="brandInfo2"></span></label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <label>免责声明:<span id="disclaimer2"></span></label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <label>商标注册:<span id="trademarkRegistCert2"></span></label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <label>报关单:<span id="inspectionReport2"></span></label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <label>卫生/生产许可:<span id="productionLicense2"></span></label>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <label>销售授权书:<span id="marketingAuth2"></span></label>
                </div>
            </div>
            <div class="row-fluid">
                    <div class="span12">
                        <label>承诺书:<span id="gpCommitmentBook"></span></label>
                    </div>
                </div>
            <div class="row-fluid">
                <div class="span12">
                    <label>审核说明:<span id="auditInfo2"></span></label>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" id="btnCloseInfo">取消</a>
        </div>
</div>
</body>
</html>