package com.camelot.ecm.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.camelot.ecm.shop.AdReport;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * Created by sevelli[贾伟光] on 2015/5/5 0005.
 */
@Controller
@RequestMapping(value = "${adminPath}/carouselreport")
public class CarouselReportController extends BaseController{
    @Resource
    private MallBannerExportService mallBannerExportService;
    
    @RequestMapping(value = "carousellist")
    public String carouselList(Model model,@ModelAttribute("mallAdCountDTO")AdReportInDto dto,@ModelAttribute("pager") Pager<?> pager){
    	boolean beginFlag=StringUtils.isEmpty(dto.getClickDateBegin());
    	boolean endFlag=StringUtils.isEmpty(dto.getClickDateEnd());
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	if(beginFlag && endFlag){
	    	Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, -1);
			dto.setClickDate(dateFormat.format(calendar.getTime()));
    	}else if(beginFlag && !endFlag){
    		dto.setClickDateBegin(dto.getClickDateEnd());
    	}else if(!beginFlag && endFlag){
    		dto.setClickDateEnd(dateFormat.format(new Date()));
    	}
    	
    	dto.setDateFormat("yyyy-MM-dd");
    	DataGrid<AdReportOutDto> dataGrid=mallBannerExportService.queryReportList(dto, pager);
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
        //结束时间不为空，开始时间为空 ， 则默认给开始时间=结束时间
        //开始时间不为空，结束时间为空 ，则不做处理 ，所以把结束时间置空
        if(!beginFlag && endFlag){
       	 dto.setClickDateEnd("");
        }
        model.addAttribute("mallAdCountDTO",dto);
        //TODO 服务规则列表查询
        return "report/carouselreport";
    }
  //数据转换
    private List<Map<String,String>> listMap(List<AdReportOutDto> list){
        List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
        for(int i=0;i<list.size();i++){
        	AdReportOutDto adReportOutDto=list.get(i);
            Map<String,String> map=new HashMap<String, String>();
	            map.put("uno",String.valueOf(i+1));
	            //广告名称
	            map.put("AdName",adReportOutDto.getMallAdName());
                //统计时间
            if(adReportOutDto.getClickDate()!=null){
                map.put("clickDate",adReportOutDto.getClickDate());
            }
                //广告类型
                map.put("AdType",adReportOutDto.getMallAdType());
               //点击次数
            if(adReportOutDto.getAdCount()!=null){
                map.put("adCount",String.valueOf(adReportOutDto.getAdCount()));
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

            AdReportInDto adReportInDto=new AdReportInDto();
            adReportInDto.setClickDateBegin(createTimeBegin);
            adReportInDto.setClickDateEnd(createTimeEnd);
            Pager pager=new Pager();
            pager.setRows(page.getPageSize());
            pager.setPage(page.getPageNo());
            DataGrid<AdReportOutDto> dataGrid= mallBannerExportService.queryReportList(adReportInDto, pager);
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
    
    @RequestMapping(value = "exportCurrentpage")
    public String exportBuypage(HttpServletRequest request,HttpServletResponse response){
        Json<Page<Map<String,String>>> json=selectBannerReportList(request);
        if(json.isSuccess()){
            if(json.getObj()!=null){
                try{
                    String fileName = "轮播图点击量相关数据"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
                    Page<Map<String,String>> page=json.getObj();
                    if(page!=null&&page.getList()!=null&&page.getList().size()>0){
                    	List<Map<String,String>> listMap=page.getList();
                        List<AdReport> listuser=new ArrayList<AdReport>();
                        for(int i=0;i<listMap.size();i++){
                        	AdReport buyerReport=new AdReport();
                            PropertyUtils.copyProperties(buyerReport,listMap.get(i));
                            buyerReport.setAdType("轮播图");
                            listuser.add(buyerReport);
                        }
                        new ExportExcel("轮播图点击量相关数据", AdReport.class).setDataList(listuser).write(response, fileName).dispose();
                    }

                }catch(Exception e){
                    logger.error("导出轮播图点击量列表数据出现异常"+e.getMessage());
                }
            }
        }
        return null;
    } 	
	
	 /**
     * 查询轮播图点击量报表分页查询
     * @author - wangp
     * @createDate - 2015-7-16
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectPageList")
    public Json<Page<Map<String,String>>> selectBannerReportList(HttpServletRequest request){
        Json json=new Json();
        try{
            Page page =new Page();
            com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
            AdReportInDto adReportInDto=new AdReportInDto();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            
            //创建开始时间
            String start=request.getParameter("clickDateBegin");
            //创建结束时间
            String end=request.getParameter("clickDateEnd");
            adReportInDto.setClickDateBegin(start);
            adReportInDto.setClickDateEnd(end);
            
            boolean beginFlag=StringUtils.isEmpty(adReportInDto.getClickDateBegin());
        	boolean endFlag=StringUtils.isEmpty(adReportInDto.getClickDateEnd());
        	
        	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	if(beginFlag && endFlag){
    	    	Calendar calendar = new GregorianCalendar();
    			calendar.setTime(new Date());
    			calendar.add(Calendar.DATE, -1);
    			adReportInDto.setClickDate(dateFormat.format(calendar.getTime()));
        	}else if(beginFlag && !endFlag){
        		adReportInDto.setClickDateBegin(adReportInDto.getClickDateEnd());
        	}else if(!beginFlag && endFlag){
        		adReportInDto.setClickDateEnd(dateFormat.format(new Date()));
        	}
            
            adReportInDto.setDateFormat("yyyy-MM-dd");
            DataGrid<AdReportOutDto> dataGrid=  mallBannerExportService.queryReportList(adReportInDto, pager);
            List<AdReportOutDto> listDto=dataGrid.getRows();
            if(listDto!=null&&listDto.size()>0){
                Iterator<AdReportOutDto> iterator=listDto.iterator();
                List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
                int i=0;
                while(iterator.hasNext()){
                	AdReportOutDto outDto=iterator.next();
                    Map<String,String> map=new HashMap<String, String>();
                    i++;
                    map.put("uno",String.valueOf(i));
                    map.put("adName",outDto.getMallAdName());
                    map.put("clickDate",outDto.getClickDate());
                    map.put("adType",outDto.getMallAdType());
                    map.put("adCount",outDto.getAdCount()!=null?outDto.getAdCount().toString():"0");
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
    @RequestMapping(value = "carouseltotal")
    @ResponseBody
    public Json carouselTotal(HttpServletRequest request){
        Json json=new Json();
        Map<String,List<Object>> map=new HashMap<String, List<Object>>();
        try{
            List<Object> axList=new ArrayList<Object>();
            List<Object> listLineMaps=new ArrayList<Object>();
            List<Object> listName=new ArrayList<Object>();
            axList.add("2015-02-01");
            Map<String,Object> lineMap=new HashMap<String, Object>();
            List<Object> dataList=new ArrayList<Object>();
            dataList.add(4L);
            lineMap.put("data",dataList);
            listLineMaps.add(lineMap);
            Map<String,Object> lineMap1=new HashMap<String, Object>();
            map.put("axlist", axList);
            map.put("datalist",listLineMaps);
            map.put("namelist",listName);
            json.setMsg("查询成功");
            json.setSuccess(true);
            json.setObj(map);
        }catch(Exception e){
            json.setMsg("获取报表信息失败");
            json.setSuccess(false);
        }
        return json;
    }
}
