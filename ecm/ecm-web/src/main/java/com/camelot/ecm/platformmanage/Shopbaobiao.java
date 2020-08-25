package com.camelot.ecm.platformmanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.servlet.ModelAndView;

import com.camelot.common.Json;
import com.camelot.ecm.shop.ShopReport;
import com.camelot.ecm.usercenter.UserBuyer;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.PayBuyerInfo;
import com.camelot.report.dto.ReportQueryDTO;
import com.camelot.report.dto.ShopReportDTO;
import com.camelot.report.service.TradeReportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * Created by sevelli on 15-3-3.
 * @description 店铺报表Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/shopbaobiao")
public class Shopbaobiao extends BaseController {
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private TradeReportService tradeReportService;
    @RequestMapping(value = "shopbaobiao")
    //列表查询
    public String list(Model model,@ModelAttribute("shopReportDTO")ShopReportDTO shopReportDTO,@ModelAttribute("pager") Pager<ShopReportDTO> pager){
        Page page=new Page();
        page.setPageSize(pager.getRows());
        page.setPageNo(pager.getPage());
        try{
        	DataGrid<ShopReportDTO> dataGrid=  tradeReportService.getShopReportList(shopReportDTO, pager);
            //DataGrid<ShopReportDTO> dataGrid=  null;
            if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
                page.setCount(dataGrid.getTotal());
                page.setList(listMap(dataGrid.getRows()));
            }else{
                page.setCount(0);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        model.addAttribute("page",page);
        model.addAttribute("shopReportDTO", shopReportDTO);
        //TODO 服务规则列表查询
        return "/platformmanage/shopbaobiao";
    }
    //数据转换
    private List<Map<String,String>> listMap(List<ShopReportDTO> list){
        List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        for(int i=0;i<list.size();i++){
            ShopReportDTO shopReportDTO=list.get(i);
            Map<String,String> map=new HashMap<String, String>();
            map.put("uno",String.valueOf(i+1));
            //店铺名称
            map.put("shopName",shopReportDTO.getShopName());
            //店铺开通时间
            if(shopReportDTO.getPassTime()!=null){
                map.put("paseTime",simpleDateFormat.format(shopReportDTO.getPassTime()));
            }
            //店铺卖出的订单数量
            if(shopReportDTO.getOrderNum()!=null){
                map.put("orderNum",String.valueOf(shopReportDTO.getOrderNum()));
            }
            //销量
            if(shopReportDTO.getSaleNum()!=null){
                map.put("saleNum",String.valueOf(shopReportDTO.getSaleNum()));
            }
            //联系方式,电话
            map.put("phoneNum",String.valueOf(shopReportDTO.getMobile()));
            //客户数
            if(shopReportDTO.getCustomerNum()!=null){
                map.put("customNum",String.valueOf(shopReportDTO.getCustomerNum()));
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
            String passdatec=request.getParameter("start");
            String passdatee=request.getParameter("end");
            String shopName=request.getParameter("shopName");
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

            ShopReportDTO reportQueryDTO=new ShopReportDTO();
            if(shopName!=null&&!"".equals(shopName)){
            	reportQueryDTO.setShopName(shopName);
            }
          //  SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            if(passdatec!=null&&!"".equals(passdatec)){
            	reportQueryDTO.setPassTimeStart(passdatec);
            }
            if(passdatee!=null&&!"".equals(passdatee)){
            	reportQueryDTO.setPassTimeEnd(passdatee);
            }
            Pager pager=new Pager();
            pager.setRows(page.getPageSize());
            pager.setPage(page.getPageNo());
            DataGrid<ShopReportDTO> dataGrid= tradeReportService.getShopReportList(reportQueryDTO, pager);
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
    @RequestMapping(value = "shoptotalzx")
    @ResponseBody
    public Json shopTotalzx(HttpServletRequest request){
        Json json=new Json();
        try{
            String date=request.getParameter("monthtime");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd");
            Date starttime;
            Date endtime;
            if(date!=null&&!"".equals(date)){
                Date start=simpleDateFormat.parse(date);
                Calendar cDay = Calendar.getInstance();
                cDay.setTime(start);
                cDay.set(Calendar.DAY_OF_MONTH,cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date end=cDay.getTime();
                cDay.setTime(end);
                cDay.set(Calendar.HOUR_OF_DAY,23);
                cDay.set( Calendar.MINUTE,59);
                cDay.set(Calendar.SECOND,59);
                end=cDay.getTime();
                starttime=start;
                endtime=end;
            }else{
                Date end=new Date();
                Calendar cDay = Calendar.getInstance();
                cDay.setTime(end);
                cDay.set(Calendar.DAY_OF_MONTH,1);
                Date start=cDay.getTime();
                SimpleDateFormat sidate=new SimpleDateFormat("yyyy-MM-dd");
                start=sidate.parse(sidate.format(start));
                starttime=start;
                endtime=end;
            }
            ShopDTO shopDTO=new ShopDTO();
            Pager<ShopDTO> pager=new Pager<ShopDTO>();
            pager.setPage(1);
            pager.setRows(1);
            shopDTO.setCreatedend(starttime);
            //shopDTO.setCreatedend(endtime);
            Long toatalshop;
            ExecuteResult<DataGrid<ShopDTO>> executeResult= shopExportService.findShopInfoByCondition(shopDTO, pager);
            if(executeResult!=null&&executeResult.getResult()!=null){
                toatalshop=executeResult.getResult().getTotal();
            }else{
                toatalshop=0L;
            }
            if(endtime.after(new Date())){
                endtime=new Date();
            }
            Map<String,Long> shopAddMap=shopAddMap(starttime,endtime);
            Calendar calendar = Calendar.getInstance();
            List<Object> axList=new ArrayList<Object>();
            List<Object> dataList=new ArrayList<Object>();
            Long addcount=0L;
            while(starttime.before(endtime)){
                Long c=shopAddMap.get(simpleDateFormat1.format(starttime));
                if(c!=null){
                    addcount=addcount+c;
                }
                axList.add(simpleDateFormat1.format(starttime));
                dataList.add(toatalshop+addcount);
                calendar.setTime(starttime);
                //当前时间加一天
                calendar.add(calendar.DATE,1);
                starttime=calendar.getTime();
            }
            Map<String,List<Object>> map=new HashMap<String, List<Object>>();
            map.put("axList", axList);
            map.put("dataList",dataList);
            json.setMsg("查询成功");
            json.setSuccess(true);
            json.setObj(map);
        }catch(Exception e){
            logger.error(e.getMessage());
            json.setMsg("查询出错");
            json.setSuccess(false);
        }
        return json;
    }
    @RequestMapping(value = "shopaddcount")
    @ResponseBody
    public Json shopaddCount(HttpServletRequest request){
        Json json=new Json();
        try{
            String date=request.getParameter("monthtime");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd");
            Date starttime;
            Date endtime;
            if(date!=null&&!"".equals(date)){
                Date start=simpleDateFormat.parse(date);
                Calendar cDay = Calendar.getInstance();
                cDay.setTime(start);
                cDay.set(Calendar.DAY_OF_MONTH,cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date end=cDay.getTime();
                cDay.setTime(end);
                cDay.set(Calendar.HOUR_OF_DAY,23);
                cDay.set( Calendar.MINUTE,59);
                cDay.set(Calendar.SECOND,59);
                end=cDay.getTime();
                starttime=start;
                endtime=end;
            }else{
                Date end=new Date();
                Calendar cDay = Calendar.getInstance();
                cDay.setTime(end);
                cDay.set(Calendar.DAY_OF_MONTH,1);
                Date start=cDay.getTime();
                SimpleDateFormat sidate=new SimpleDateFormat("yyyy-MM-dd");
                start=sidate.parse(sidate.format(start));
                starttime=start;
                endtime=end;
            }
            if(endtime.after(new Date())){
                endtime=new Date();
            }
            Map<String,Long> shopAddMap=shopAddMap(starttime,endtime);
            Calendar calendar = Calendar.getInstance();
            List<Object> axList=new ArrayList<Object>();
            List<Object> dataList=new ArrayList<Object>();
            Long c;
            while(starttime.before(endtime)){
                c=shopAddMap.get(simpleDateFormat1.format(starttime));
                axList.add(simpleDateFormat1.format(starttime));
                if(c!=null){
                    dataList.add(c);
                }else{
                    dataList.add(0L);
                }
                calendar.setTime(starttime);
                //当前时间加一天
                calendar.add(calendar.DATE,1);
                starttime=calendar.getTime();
            }
            Map<String,List<Object>> map=new HashMap<String, List<Object>>();
            map.put("axList", axList);
            map.put("dataList",dataList);
            json.setMsg("查询成功");
            json.setSuccess(true);
            json.setObj(map);
        }catch(Exception e){
            logger.error(e.getMessage());
            json.setMsg("查询出错");
            json.setSuccess(false);
        }
        return json;
    }
    private Map<String,Long> shopAddMap(Date start,Date end){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Long> map=new HashMap<String, Long>();
        Calendar calendar = Calendar.getInstance();
        Date startdate=start;
        while(start.before(end)){
            map.put(simpleDateFormat.format(start), 0L);
            calendar.setTime(start);
            //当前时间加一天
            calendar.add(calendar.DATE,1);
            start=calendar.getTime();
        }
        ShopDTO shopDTO=new ShopDTO();
        Pager<ShopDTO> pager=new Pager<ShopDTO>();
        pager.setPage(1);
        pager.setRows(Integer.MAX_VALUE);
        shopDTO.setCreatedstr(startdate);
        shopDTO.setCreatedend(end);
        ExecuteResult<DataGrid<ShopDTO>> executeResult= shopExportService.findShopInfoByCondition(shopDTO, pager);
        if(executeResult!=null&&executeResult.getResult()!=null){
            DataGrid<ShopDTO> shopDTODataGrid=executeResult.getResult();
            List<ShopDTO> list=shopDTODataGrid.getRows();
            if(list!=null&&list.size()>0){
                for(int i=0;i<list.size();i++){
                    ShopDTO shopDTO1=list.get(i);
                    if(shopDTO1.getCreated()!=null){
                        String dateString=simpleDateFormat.format(shopDTO1.getCreated());
                        map.put(dateString,map.get(dateString)+1);
                    }
                }
            }
        }
        return map;
    }
    
    /**
	 * 导出店铺报表当前页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "exportCurrentpage")
    public String exportBuypage(HttpServletRequest request,HttpServletResponse response){
        Json<Page<Map<String,String>>> json=selectShopReportList(request);
        if(json.isSuccess()){
            if(json.getObj()!=null){
                try{
                    String fileName = "店铺相关数据"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
                    Page<Map<String,String>> page=json.getObj();
                    if(page!=null&&page.getList()!=null&&page.getList().size()>0){
                    	List<Map<String,String>> listMap=page.getList();
                        List<ShopReport> listuser=new ArrayList<ShopReport>();
                        for(int i=0;i<listMap.size();i++){
                        	ShopReport shopReport=new ShopReport();
                            PropertyUtils.copyProperties(shopReport,listMap.get(i));
                            listuser.add(shopReport);
                        }
                        new ExportExcel("店铺数据", ShopReport.class).setDataList(listuser).write(response, fileName).dispose();
                    }

                }catch(Exception e){
                    logger.error("导出店铺列表数据出现异常"+e.getMessage());
                }
            }
        }
        return null;
    } 	
	
	 /**
     * 查询店铺报表分页查询
     * @author - wangp
     * @createDate - 2015-7-16
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectPageList")
    public Json<Page<Map<String,String>>> selectShopReportList(HttpServletRequest request){
        Json json=new Json();
        try{
            Page page =new Page();
            com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
            ShopReportDTO reportQueryDTO=new ShopReportDTO();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            //商品名称
            String shopName=request.getParameter("shopName");
            if(shopName!=null&&!"".equals(shopName)){
            	reportQueryDTO.setShopName(shopName);
            }
            //创建开始时间
            String start=request.getParameter("passTimeStart");
            //创建结束时间
            String end=request.getParameter("passTimeEnd");
        //  SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            if(start!=null&&!"".equals(start)){
                reportQueryDTO.setPassTimeStart(start);
            }
            if(end!=null&&!"".equals(end)){
                reportQueryDTO.setPassTimeEnd(end);
            }
            DataGrid<ShopReportDTO> dataGrid=  tradeReportService.getShopReportList(reportQueryDTO, pager);
            List<ShopReportDTO> listDto=dataGrid.getRows();
            if(listDto!=null&&listDto.size()>0){
                Iterator<ShopReportDTO> iterator=listDto.iterator();
                List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                int i=0;
                while(iterator.hasNext()){
                	ShopReportDTO shopReportDTO1=iterator.next();
                    Map<String,String> map=new HashMap<String, String>();
                    i++;
                    map.put("num",String.valueOf(i));
                    map.put("shopName",shopReportDTO1.getShopName());
                    if(shopReportDTO1.getPassTime()!=null){
                        map.put("passTime",simpleDateFormat.format(shopReportDTO1.getPassTime()));
                    }
                    map.put("orderNum",shopReportDTO1.getOrderNum()!=null?shopReportDTO1.getOrderNum().toString():"");
                    map.put("saleNum",shopReportDTO1.getSaleNum()!=null?shopReportDTO1.getSaleNum().toString():"");
                    map.put("phoneNum",shopReportDTO1.getMobile()!=null?shopReportDTO1.getMobile().toString():"");
                    map.put("customerNum",shopReportDTO1.getCustomerNum()!=null?shopReportDTO1.getCustomerNum().toString():"");
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
