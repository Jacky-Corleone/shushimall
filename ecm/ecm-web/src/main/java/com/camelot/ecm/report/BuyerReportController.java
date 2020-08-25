package com.camelot.ecm.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.common.Json;
import com.camelot.ecm.shop.BuyerReport;
import com.camelot.ecm.shop.ShopReport;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.PayBuyerInfo;
import com.camelot.report.dto.ReportQueryDTO;
import com.camelot.report.dto.ShopCustomerReportDTO;
import com.camelot.report.service.TradeReportService;
import com.camelot.usercenter.service.UserReportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * Created by sevelli[贾伟光] on 2015/5/5 0005.
 */
@Controller
@RequestMapping(value = "${adminPath}/buyerreport")
public class BuyerReportController extends BaseController{
    @Resource
    private UserReportService userReportService;
    
    @Resource
    private TradeReportService tradeReportService;

    @RequestMapping(value = "buyerlist")
    public String buyerList(Model model,@ModelAttribute("PayBuyerInfo")PayBuyerInfo payBuyerInfo,@ModelAttribute("pager") Pager<?> pager){
    	DataGrid<PayBuyerInfo> dataGrid=tradeReportService.getCustomerReport(payBuyerInfo, (Pager<PayBuyerInfo>) pager);
        Page page=new Page();
        if(null!=dataGrid.getRows()&&dataGrid.getRows().size()>0){
        page.setList(listMap(dataGrid.getRows()));
        page.setCount(dataGrid.getTotal());
        }else{
        page.setCount(0);	
        }
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        model.addAttribute("page",page);
        model.addAttribute("payBuyerInfo",payBuyerInfo);
        return "report/buyerreport";
    }
    
  //数据转换
    private List<Map<String,String>> listMap(List<PayBuyerInfo> list){
        List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        for(int i=0;i<list.size();i++){
        	PayBuyerInfo payBuyerInfo=list.get(i);
            Map<String,String> map=new HashMap<String, String>();
	            map.put("uno",String.valueOf(i+1));
	            //买家名称
	            map.put("userName",payBuyerInfo.getUserName());
                //注册时间
            if(payBuyerInfo.getCreateTime()!=null){
                map.put("createDt",simpleDateFormat.format(payBuyerInfo.getCreateTime()));
            }
                //下单总数
            if(payBuyerInfo.getOrderNum()!=null){
                map.put("orderNum",String.valueOf(payBuyerInfo.getOrderNum()));
            }
                //购买商品总额
            if(payBuyerInfo.getPayPriceTotal()!=null){
                map.put("goodsPriceSum",String.valueOf(payBuyerInfo.getPayPriceTotal()));
            }
	            //联系方式,电话
	            map.put("phone",payBuyerInfo.getUmobile());
                //购买过的店铺总数
            if(payBuyerInfo.getShopNum()!=null){
                map.put("buyShopNum",(payBuyerInfo.getShopNum()+""));
            }
            mapList.add(map);
        }
        return mapList;
    }
    @RequestMapping(value = "ajaxlist")
    @ResponseBody
    public Json ajaxList(HttpServletRequest request){
        Json json=new Json();
        try{
            String pageNo=request.getParameter("page");
            String rows=request.getParameter("rows");
            String userName=request.getParameter("userName");
            String createTimeBegin=request.getParameter("createTimeBegin");
            String createTimeEnd=request.getParameter("createTimeEnd");
            Page page=new Page();
            if(pageNo!=null&&!"".equals(pageNo)){
                page.setPageNo(new Integer(pageNo));
            }else{
                page.setPageNo(1);
            }
            if(rows!=null&&!"".equals(rows)){
                page.setPageSize(new Integer(rows));
            }else{
                page.setPageSize(10);
            }

            PayBuyerInfo reportQueryDTO=new PayBuyerInfo();
            if(userName!=null&&!"".equals(userName)){
            	 reportQueryDTO.setUserName(userName);
            }
            if(createTimeBegin!=null&&!"".equals(createTimeBegin)){
                reportQueryDTO.setCreateTimeStart(createTimeBegin);
            }
            if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                reportQueryDTO.setCreateTimeEnd(createTimeEnd);
            }
            Pager pager=new Pager();
            pager.setRows(page.getPageSize());
            pager.setPage(page.getPageNo());
            DataGrid<PayBuyerInfo> dataGrid= tradeReportService.getCustomerReport(reportQueryDTO, pager);
            if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
                page.setCount(dataGrid.getTotal());
                page.setList(listMap(dataGrid.getRows()));
            }else{
                page.setCount(0);
            }
            json.setMsg(page.toString());
            json.setSuccess(true);
            json.setObj(page);
        }catch(Exception e){
            logger.error(e.getMessage());
            json.setMsg("查询失败");
            json.setSuccess(false);
        }
        return json;
    }
    @RequestMapping(value = "usertotal")
    @ResponseBody
    public Json userTotal(HttpServletRequest request){
        Json json=new Json();
        Map<String,List<Object>> map=new HashMap<String, List<Object>>();
        try{
            List<Object> axList=new ArrayList<Object>();
            List<Object> dataList=new ArrayList<Object>();
            axList.add("2015-02-01");
            map.put("axList", axList);
            dataList.add(4L);
            map.put("dataList",dataList);
            json.setMsg("查询成功");
            json.setSuccess(true);
            json.setObj(map);
        }catch(Exception e){
            json.setMsg("获取报表信息失败");
            json.setSuccess(false);
        }
        return json;
    }
    @RequestMapping(value = "useradd")
    @ResponseBody
    public Json userAdd(HttpServletRequest request){
        Json json=new Json();
        Map<String,List<Object>> map=new HashMap<String, List<Object>>();
        try{
            List<Object> axList=new ArrayList<Object>();
            List<Object> dataList=new ArrayList<Object>();
            axList.add("2015-02-01");
            map.put("axList", axList);
            dataList.add(1);
            map.put("dataList",dataList);
            json.setMsg("查询成功");
            json.setSuccess(true);
            json.setObj(map);
        }catch(Exception e){
            json.setMsg("获取报表信息失败");
            json.setSuccess(false);
        }
        return json;
    }
    @RequestMapping(value = "exportCurrentpage")
    public String exportBuypage(HttpServletRequest request,HttpServletResponse response){
        Json<Page<Map<String,String>>> json=selectBuyerReportList(request);
        if(json.isSuccess()){
            if(json.getObj()!=null){
                try{
                    String fileName = "买家相关数据"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
                    Page<Map<String,String>> page=json.getObj();
                    if(page!=null&&page.getList()!=null&&page.getList().size()>0){
                    	List<Map<String,String>> listMap=page.getList();
                        List<BuyerReport> listuser=new ArrayList<BuyerReport>();
                        for(int i=0;i<listMap.size();i++){
                        	BuyerReport buyerReport=new BuyerReport();
                            PropertyUtils.copyProperties(buyerReport,listMap.get(i));
                            listuser.add(buyerReport);
                        }
                        new ExportExcel("买家相关数据", BuyerReport.class).setDataList(listuser).write(response, fileName).dispose();
                    }

                }catch(Exception e){
                    logger.error("导出买家列表数据出现异常"+e.getMessage());
                }
            }
        }
        return null;
    } 	
	
	 /**
     * 查询买家报表分页查询
     * @author - wangp
     * @createDate - 2015-7-16
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectPageList")
    public Json<Page<Map<String,String>>> selectBuyerReportList(HttpServletRequest request){
        Json json=new Json();
        try{
            Page page =new Page();
            com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
            PayBuyerInfo  reportQueryDTO=new PayBuyerInfo();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            //买家名称
            String userName=request.getParameter("userName");
            if(userName!=null&&!"".equals(userName)){
            	reportQueryDTO.setUserName(userName);
            }
            //创建开始时间
            String start=request.getParameter("createTimeStart");
            //创建结束时间
            String end=request.getParameter("createTimeEnd");
            if(start!=null&&!"".equals(start)){
                reportQueryDTO.setCreateTimeStart(start);
            }
            if(end!=null&&!"".equals(end)){
                reportQueryDTO.setCreateTimeEnd(end);
            }
            DataGrid<PayBuyerInfo> dataGrid=  tradeReportService.getCustomerReport(reportQueryDTO, pager);
            List<PayBuyerInfo> listDto=dataGrid.getRows();
            if(listDto!=null&&listDto.size()>0){
                Iterator<PayBuyerInfo> iterator=listDto.iterator();
                List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                int i=0;
                while(iterator.hasNext()){
                	PayBuyerInfo ShopCustomerReportDTO1=iterator.next();
                    Map<String,String> map=new HashMap<String, String>();
                    i++;
                    map.put("num",String.valueOf(i));
                    map.put("userName",ShopCustomerReportDTO1.getUserName());
                    if(ShopCustomerReportDTO1.getCreateTime()!=null){
                        map.put("createTime",simpleDateFormat.format(ShopCustomerReportDTO1.getCreateTime()));
                    }
                    map.put("orderNum",ShopCustomerReportDTO1.getOrderNum()!=null?ShopCustomerReportDTO1.getOrderNum().toString():"");
                    map.put("payPriceTotal",ShopCustomerReportDTO1.getPayPriceTotal()!=null?ShopCustomerReportDTO1.getPayPriceTotal().toString():"");
                    map.put("umobile",ShopCustomerReportDTO1.getUmobile());
                    map.put("shopNum",ShopCustomerReportDTO1.getShopNum()!=null?String.valueOf(ShopCustomerReportDTO1.getShopNum()):"");
                    listMap.add(map);
                }
                page.setList(listMap);
            }
            page.setCount(dataGrid.getTotal());
            page.setPageNo(pager.getPage());
            page.setPageSize(pager.getRows());
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("查询方法出错"+e.getMessage());
        }
        return json;
    }
}
