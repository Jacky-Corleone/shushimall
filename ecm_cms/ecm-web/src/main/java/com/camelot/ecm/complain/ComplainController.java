package com.camelot.ecm.complain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.common.Json;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.PaymentExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 投诉仲裁
 * @author - 门光耀
 * @message- 投诉仲裁
 * @createDate - 2015-4-14
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/complain")
public class ComplainController extends BaseController {
     @Resource
     private ComplainExportService complainExportService;
     @Resource
     private UserExportService userExportService;
     @Resource
     private ShopExportService shopExportService;
     @Resource
     private TradeReturnExportService tradeReturnExportService;
     @Resource
     private PaymentExportService paymentExportService;
     @Resource
     private TradeOrderExportService tradeOrderExportService;
    /**
     * 投诉仲裁
     * @author - 门光耀
     * @message- 投诉仲裁查询
     * @createDate - 2015-4-14
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "complainlist")
    public ModelAndView list(HttpServletRequest request){
        Map<String,Object> map=new HashMap<String, Object>();
        Page page=new Page();
        page.setPageSize(10);
        page.setPageNo(1);
        ComplainDTO complainDTO=new ComplainDTO();
       // complainDTO.setStatusSelect("2");	//显示status<2的数据
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(10);
        DataGrid<ComplainDTO> datagrid= complainExportService.findEarlyComplainInfoByCondition(complainDTO, pager);
        if(datagrid!=null){
            page.setList(objToMap(datagrid.getRows()));
            page.setCount(datagrid.getTotal());
        }else{
            page.setCount(0);
        }
        map.put("page",page);
        //TODO 服务规则列表查询
        return new ModelAndView("/complain/complainlist","map",map);
    }
    @RequestMapping(value="complainlistjson")
    @ResponseBody
    public Json listJson(HttpServletRequest request){
        Json json=new Json();
        try{
            Page pagetable=new Page();
            ComplainDTO complainDTO=new ComplainDTO();
            //页数
            String page=request.getParameter("page");
            if(page!=null&&!"".equals(page)){
                pagetable.setPageNo(new Integer(page));
            }else{
                pagetable.setPageNo(1);
            }
            //每页行数
            String rows=request.getParameter("rows");
            if(rows!=null&&!"".equals(rows)){
                pagetable.setPageSize(new Integer(rows));
            }else{
                pagetable.setPageSize(10);
            }
            //订单号
            String orderNum=request.getParameter("orderNum");
			if (orderNum != null && !"".equals(orderNum)) {
				complainDTO.setOrderId(orderNum);
			}
            String complaintype=request.getParameter("complaintype");
            if(complaintype!=null&&!"".equals(complaintype)){
                complainDTO.setComplainType(complaintype);
            }
            //投诉人
            String buyerNum=request.getParameter("buyerNum");
            if(buyerNum!=null&&!"".equals(buyerNum)){
                UserDTO userdto=new UserDTO();
                userdto.setUname(buyerNum);
                DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto, null,null);
                if(datagrid!=null){
                    if(datagrid.getRows()!=null&&datagrid.getRows().size()>0){
                        UserDTO u=datagrid.getRows().get(0);
                        complainDTO.setBuyerId(u.getUid());
                    }else{
                        json.setMsg(pagetable.toString());
                        json.setSuccess(true);
                        return json;
                    }
                }
            }
            //头数类型（退款仲裁，售后仲裁）
            String type=request.getParameter("type");
            if(type!=null&&!"".equals(type)){
                complainDTO.setType(new Integer(type));
            }
            //仲裁状态
            String flag=request.getParameter("flag");
            if(flag!=null&&!"".equals(flag)){
                complainDTO.setStatus(new Integer(flag));
            }
            //投诉时间从
            String createTimeBegin=request.getParameter("createTimeBegin");
            complainDTO.setCreatedBegin(createTimeBegin);
            //投诉时间到
            String createTimeEnd=request.getParameter("createTimeEnd");
            if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                Calendar   calendar   =   new   GregorianCalendar();
                calendar.setTime(sdf.parse(createTimeEnd));
                calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                Date end=calendar.getTime();
                createTimeEnd=sdf.format(end);
                complainDTO.setCreatedEnd(createTimeEnd);
            }
            Pager pager=new Pager();
            pager.setPage(pagetable.getPageNo());
            pager.setRows(pagetable.getPageSize());
            complainDTO.setStatusSelect("2");	//显示status<2的数据
            DataGrid<ComplainDTO> datagrid= complainExportService.findEarlyComplainInfoByCondition(complainDTO, pager);
            if(datagrid!=null){
                pagetable.setList(objToMap(datagrid.getRows()));
                pagetable.setCount(datagrid.getTotal());
            }else{
                pagetable.setCount(0L);
            }
            json.setObj(pagetable);
            json.setMsg(pagetable.toString());
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("查询失败");
            json.setSuccess(false);
        }
        return json;
    }
    /**
     * 投诉仲裁
     * @author - 门光耀
     * @message- 投诉仲裁列表数据转换
     * @createDate - 2015-4-14
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,String>> objToMap(List<ComplainDTO> list){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(list!=null&&list.size()>0){
            Iterator<ComplainDTO> iteratoruser=list.iterator();
            Map<Long,Long> mapUserid=new HashMap<Long, Long>();
            while(iteratoruser.hasNext()){
                ComplainDTO complainDTO=iteratoruser.next();
                if(complainDTO.getBuyerId()!=null){
                    mapUserid.put(complainDTO.getBuyerId(),1L);
                }
                if(complainDTO.getSellerId()!=null){
                    mapUserid.put(complainDTO.getSellerId(),1L);
                }
            }
            Map<Long,String> userMap=userMap(mapUserid);
            Iterator<ComplainDTO> iterator=list.iterator();
            Map<Integer,String> typeMap=new HashMap<Integer, String>();
            typeMap.put(new Integer(1),"退款申请");
            typeMap.put(new Integer(2),"申请售后");
            Map<Integer,String> staceMap=new HashMap<Integer, String>();
            staceMap.put(new Integer(0),"待仲裁");
            staceMap.put(new Integer(1),"已仲裁");
            staceMap.put(new Integer(2),"已撤销");
            Map<String,String> tsf=new HashMap<String, String>();
            tsf.put("1","买家");
            tsf.put("2","卖家");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while(iterator.hasNext()){
                ComplainDTO complainDTO=iterator.next();
                Map<String,String> map=new HashMap<String, String>();
                //投诉内容
                map.put("tsnr",complainDTO.getComplainResion());
                //退款单id
                map.put("refundId", complainDTO.getRefundId()+"");
                //申请类型
                if(complainDTO.getType()!=null){
                    map.put("typeName",typeMap.get(complainDTO.getType()));
                }
                //买家账号
                if(complainDTO.getBuyerId()!=null){
                    map.put("buyerNum",userMap.get(complainDTO.getBuyerId()));
                }
                if(complainDTO.getSellerId()!=null){
                    map.put("sellerNum",userMap.get(complainDTO.getSellerId()));
                }
                map.put("shopName",complainDTO.getShopName());
                map.put("orderNum",complainDTO.getOrderId()!=null?String.valueOf(complainDTO.getOrderId()):"");
                if(complainDTO.getCreated()!=null){
                    map.put("createTime",simpleDateFormat.format(complainDTO.getCreated()));
                }
                if(complainDTO.getStatus()!=null){
                    map.put("flagName",staceMap.get(complainDTO.getStatus()));
                }
                map.put("flag",complainDTO.getStatus()!=null?String.valueOf(complainDTO.getStatus()):"");
                map.put("id",complainDTO.getId()!=null?String.valueOf(complainDTO.getId()):"");
                //投诉方
                if(complainDTO.getComplainType()!=null&&!"".equals(complainDTO.getComplainType())){
                    map.put("tsf",tsf.get(complainDTO.getComplainType()));
                }
                if(complainDTO.getComplainTypeTotal()!=null&&!"".equals(complainDTO.getComplainTypeTotal())){
                	map.put("bjf","2".equals(complainDTO.getComplainTypeTotal())?"已辩解":"");
                }
                if(complainDTO.getComplainGroup()!=null){
               	 map.put("complainGroup",String.valueOf(complainDTO.getComplainGroup()));
               }
                listMap.add(map);
            }
        }
        return listMap;
    }
    private Map<Long,String> userMap(Map<Long,Long> mapUserid){
        Map<Long,String> userMap=new HashMap<Long, String>();
        if(mapUserid.size()>0){
            Iterator<Long> iteratorid=mapUserid.keySet().iterator();
            List<String> idlist=new ArrayList<String>();
            while(iteratorid.hasNext()){
                idlist.add(String.valueOf(iteratorid.next()));
            }
            ExecuteResult<List<UserDTO>> executeResult =userExportService.findUserListByUserIds(idlist);
            if(executeResult!=null&&executeResult.getResult()!=null&&executeResult.getResult().size()>0){
                Iterator<UserDTO> iterator=executeResult.getResult().iterator();
                while(iterator.hasNext()){
                    UserDTO userDTO=iterator.next();
                    userMap.put(userDTO.getUid(),userDTO.getUname());
                }
            }
        }
        return userMap;
    }
    /**
     * 投诉仲裁
     * @author - 门光耀
     * @message- 投诉仲裁明细页面，仲裁页面
     * @createDate - 2015-4-14
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value="complaindetail")
    public ModelAndView complainzx(HttpServletRequest request){
        String id=request.getParameter("id");
        String refundId=request.getParameter("refundId");
        String complainGroup=request.getParameter("complainGroup");
        String flag=request.getParameter("flag");//0代表待仲裁,1代表仲裁完成2.已取消
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id",id);
        map.put("refundId", refundId);
        
        ComplainDTO comp=new ComplainDTO();
        comp.setRefundId(Long.parseLong(refundId));	//退款单id
        //comp.setStatus(Integer.parseInt(flag));	//设置待仲裁
        comp.setComplainGroup(Long.parseLong(complainGroup));
        List<ComplainDTO> list = complainExportService.getComplainByCondition(comp);
        
        //ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(id));
        //1代表退款2代表售后
        String type="1";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(list!=null&&list.size()>0){
            ComplainDTO complainDTO=list.get(0);
            if(complainDTO!=null){
                flag=complainDTO.getStatus()!=null?String.valueOf(complainDTO.getStatus()):"";
                type=complainDTO.getType()!=null?String.valueOf(complainDTO.getType()):"";
                //订单号
                map.put("orderId",complainDTO.getOrderId());
                if(complainDTO.getOrderId()!=null){
                    TradeOrdersQueryInDTO inDTO=new TradeOrdersQueryInDTO();
                    inDTO.setOrderId(complainDTO.getOrderId());
                    ExecuteResult<DataGrid<TradeOrdersDTO>> execute= tradeOrderExportService.queryOrders(inDTO,null);
                    //ExecuteResult<TradeOrdersDTO> execute= tradeOrderExportService.getOrderById(complainDTO.getOrderId());
                    if(execute!=null&&execute.getResult()!=null&&execute.getResult().getRows()!=null&&execute.getResult().getRows().size()>0){
                        if(execute.getResult().getRows().size()==1){
                            if(execute.getResult().getRows().get(0).getLocked()!=null){
                            	if(!"2".equals(flag)){
                            		map.put("lock",String.valueOf(execute.getResult().getRows().get(0).getLocked()));
                            	}
                            }else{
                            	if(!"2".equals(flag)){
                            	  map.put("lock","1");
                            	}
                            }
                        }
                    }
                }
                if(complainDTO.getModified()!=null){
                    map.put("modified",simpleDateFormat.format(complainDTO.getModified()));
                }
                Map<Integer,String> typeMap=new HashMap<Integer, String>();
                typeMap.put(new Integer(1),"退款申请");
                typeMap.put(new Integer(2),"申请售后");
                if(complainDTO.getType()!=null){
                    map.put("typeName",typeMap.get(complainDTO.getType()));
                }
                Long buyerid=complainDTO.getBuyerId();
                //根据买家id获取买家信息
                if(buyerid!=null){
                    List<String> listids=new ArrayList<String>();
                    listids.add(String.valueOf(buyerid));
                    ExecuteResult<List<UserDTO>> executeResult1=userExportService.findUserListByUserIds(listids);
                    List<UserDTO> dtoList=executeResult1.getResult();
                    if(dtoList!=null&&dtoList.size()>0){
                        UserDTO userDTO=dtoList.get(0);
                        map.put("buyerName",userDTO.getUname());
                        map.put("buyerCode",userDTO.getUid());
                        map.put("buyerMobile",userDTO.getUmobile());
                    }
                }
                Long sellerid=complainDTO.getSellerId();
                if(sellerid!=null){
                    List<String> listids=new ArrayList<String>();
                    listids.add(String.valueOf(sellerid));
                    ExecuteResult<List<UserDTO>> executeResult1=userExportService.findUserListByUserIds(listids);
                    List<UserDTO> dtoList=executeResult1.getResult();
                    if(dtoList!=null&&dtoList.size()>0){
                        UserDTO userDTO=dtoList.get(0);
                        if(userDTO.getShopId()!=null){
                            ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
                            Long[] shopids={userDTO.getShopId()};
                            shopAudiinDTO.setShopIds(shopids);
                            ExecuteResult<List<ShopDTO>> executeResult2=shopExportService.queryShopInfoByids(shopAudiinDTO);
                            List<ShopDTO> listshopDto=executeResult2.getResult();
                            if(listshopDto!=null&&listshopDto.size()>0){
                                ShopDTO shopDTO=listshopDto.get(0);
                                //店铺名称
                                map.put("shopName",shopDTO.getShopName());
                                //店铺编码
                                map.put("shopCode",shopDTO.getShopId());
                                //店铺电话
                                map.put("shopMobile",shopDTO.getMobile());
                            }
                        }
                    }
                }
            }
            if(complainDTO.getRefundId()!=null){
                String tradeReturnid=String.valueOf(complainDTO.getRefundId());
                map.put("thid",tradeReturnid);
                ExecuteResult<TradeReturnInfoDto> executeResult1= tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(tradeReturnid);
                if(executeResult1!=null&&executeResult1.getResult()!=null){
                    TradeReturnInfoDto tradeReturnInfoDto=executeResult1.getResult();
                    TradeReturnGoodsDTO tradeReturnGoodsDTO=tradeReturnInfoDto.getTradeReturnDto();
                    if(tradeReturnGoodsDTO!=null&&tradeReturnGoodsDTO.getCodeNo()!=null&&!"".equals(tradeReturnGoodsDTO.getCodeNo())){
                        String reproNo=tradeReturnGoodsDTO.getCodeNo();
                            //退款单据号
                            map.put("refundNo", reproNo);
                            if (tradeReturnGoodsDTO.getCreatedDt() != null) {
                                //退款申请时间
                                map.put("refcreated", simpleDateFormat.format(tradeReturnGoodsDTO.getCreatedDt()));
                            }
                            BigDecimal ordertkamount=new BigDecimal(0);
                            //订单退款货品金额
                            if(tradeReturnGoodsDTO.getRefundGoods()!=null){
                                map.put("ordertkJe",tradeReturnGoodsDTO.getRefundGoods().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                                ordertkamount=ordertkamount.add(tradeReturnGoodsDTO.getRefundGoods());
                            }else{
                                map.put("ordertkJe","0.00");
                            }
                            //订单退货运费金额
                            if(tradeReturnGoodsDTO.getRefundFreight()!=null){
                                map.put("ordertkYf",tradeReturnGoodsDTO.getRefundFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                            }else{
                                map.put("ordertkYf","0.00");
                            }
                            map.put("ordertkamount", ordertkamount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                            map.put("thresion",tradeReturnGoodsDTO.getReturnResult());
                            map.put("rekark",tradeReturnGoodsDTO.getRemark());
                            map.put("kdcompany",tradeReturnGoodsDTO.getExpressName());
                            map.put("kdno",tradeReturnGoodsDTO.getExpressNo());

                    }
                }
            }
        }
        List<Map<String,Object>> tsList=new ArrayList<Map<String,Object>>();
        
        for (int i=list.size()-1;i>=0;i--) {
        	ComplainDTO complainDTO=list.get(i);
        	Map<String,Object> tsMap=new HashMap<String,Object>();
        	tsMap.put("complainType", complainDTO.getComplainType());
        	tsMap.put("tsnr",complainDTO.getComplainResion());	//投诉内容
            //url
            if(complainDTO.getComplainPicUrl()!=null&&!"".equals(complainDTO.getComplainPicUrl())){
            	tsMap.putAll(geturlMap(complainDTO.getComplainPicUrl(),3));
            }else{
            	tsMap.put("listurlsize1","4");
            }
            tsMap.put("comment",complainDTO.getComment());
            //投诉说明
            tsMap.put("tscomment",complainDTO.getComplainContent());
            tsMap.put("mjmobile",complainDTO.getComplainPhone());	//投诉人电话
            tsMap.put("mjemail",complainDTO.getComplainEmail());
            tsMap.put("staceText",complainDTO.getStatusText());
            //创建时间
            if(complainDTO.getCreated()!=null){
            	tsMap.put("createDate",simpleDateFormat.format(complainDTO.getCreated()));
            }
            tsList.add(tsMap);
		}
        map.put("tsList", tsList);
        
        String url;
        if("0".equals(flag)){
            //仲裁办理页面
            url="/complain/complainbl";
        }else{
            url="/complain/complaindetail";
        }
        map.put("flag",flag);
        map.put("type",type);
        return new ModelAndView(url,"map",map);
    }
    private Map<String,Object> geturlMap(String url,int tds){
        Map<String,Object> map=new HashMap<String, Object>();
        String urls[]=url.split(";");
        if(urls!=null&&urls.length>0){
            int length=urls.length;
            int cs=length/tds;
            int ys=length%tds;
            for(int i=0;i<=cs;i++){
                if(i==cs){
                    if(ys>0){
                        List<String> listurl=new ArrayList<String>();
                        for(int y=0;y<ys;y++){
                            int index=y+i*tds;
                            listurl.add(urls[index]);
                        }
                        map.put("listurl"+(i+1),listurl);
                    }
                }else{
                    List<String> listurl=new ArrayList<String>();
                    for(int y=0;y<tds;y++){
                        int index=y+i*tds;
                        listurl.add(urls[index]);
                    }
                    map.put("listurl"+(i+1),listurl);
                }
            }
            if(map.size()>3){
                map.put("listurlsize1","1");
                map.put("listurlsize2","1");
                map.put("listurlsize3","1");
                map.put("listurlsize4","1");
            }else if(map.size()>2){
                map.put("listurlsize1","2");
                map.put("listurlsize2","1");
                map.put("listurlsize3","1");
            }else if(map.size()>1){
                map.put("listurlsize1","2");
                map.put("listurlsize2","2");
            }else{
                map.put("listurlsize1","4");
            }
        }else{
            map.put("listurlsize1","4");
        }
        return map;
    }
    /**
     * 投诉仲裁
     * @author - 门光耀
     * @message- 仲裁买家赢
     * @createDate - 2015-4-16
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "buyerwin")
    @ResponseBody
    public Json buyerWin(HttpServletRequest request){
        Json json=new Json();
        json.setMsg("仲裁完成");
        json.setSuccess(true);
        try{
            //仲裁id
            String id=request.getParameter("id");
            //退款单id
            String tkdid=request.getParameter("tkdid");
            //是否退款yes退no不退
            String orderPriceflag=request.getParameter("orderPriceflag");
            //退款金额
            String orderPrice=request.getParameter("orderPrice");
            //是否扣质保金yes扣，no不扣
            String zbPriceflag=request.getParameter("zbPriceflag");
            //将要扣除的质保金金额
            String zbPrice=request.getParameter("zbPrice");
            //仲裁说明
            String comment=request.getParameter("comment");
            String orderPriceCheckboxseller=request.getParameter("orderPriceCheckboxseller");
            
            ComplainDTO comp=new ComplainDTO();
            comp.setRefundId(Long.parseLong(tkdid));	//退款单id
            comp.setStatus(0);	//设置待仲裁
            List<ComplainDTO> list = complainExportService.getComplainByCondition(comp);
            
           // ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(id));
            if(list!=null && list.size()>0){
            	
                ComplainDTO complainDTO=list.get(0);
                
                String text="";
                boolean ifendcomplain=true;
                if(orderPriceCheckboxseller==null||"".equals(orderPriceCheckboxseller)||"no".equals(orderPriceCheckboxseller)){
                    //为yes表示需要退款，调用青山退款接口
                    if(orderPriceflag!=null&&"yes".equals(orderPriceflag)){
                        TradeReturnGoodsDTO tradeReturnGoodsDTO=new TradeReturnGoodsDTO();
                        tradeReturnGoodsDTO.setId(complainDTO.getRefundId());
                        tradeReturnGoodsDTO.setOrderId(String.valueOf(complainDTO.getOrderId()));
                        ExecuteResult executeResult1=tradeReturnExportService.updateTradeReturnStatus(tradeReturnGoodsDTO, TradeReturnStatusEnum.AUTH);
                        if(executeResult1.isSuccess()){	
                            ifendcomplain=true;
                        }else{
                            ifendcomplain=false;
                            if(executeResult1.getErrorMessages()!=null&&executeResult1.getErrorMessages().size()>0){
                                for(int i=0;i<executeResult1.getErrorMessages().size();i++){
                                    logger.error("仲裁时，系统完成线上退款时发生异常:"+executeResult1.getErrorMessages().get(i));
                                }
                            }
                        }
                        text="买家赢得仲裁：退款给买家￥"+orderPrice+"；";
                    }else{
                    }
                    //yes表示需要扣除质保金，所以需要扣除质保金
                    if(zbPriceflag!=null&&"yes".equals(zbPriceflag)){
                        text=text+"扣除卖家质保金"+zbPrice+"。";
                    }
                    if(text==null||"".equals(text)){
                        text="仲裁关闭：系统不强制退款，退款需要卖家和买家协商。";
                    }
                }else{
                    TradeReturnGoodsDTO tradeReturnGoodsDTO=new TradeReturnGoodsDTO();
                    tradeReturnGoodsDTO.setId(complainDTO.getRefundId());
                    tradeReturnGoodsDTO.setOrderId(String.valueOf(complainDTO.getOrderId()));
                    ExecuteResult executeResult1=tradeReturnExportService.updateTradeReturnStatus(tradeReturnGoodsDTO, TradeReturnStatusEnum.CLOSE);
                    if(executeResult1.isSuccess()){
                        ifendcomplain=true;
                    }else{
                        ifendcomplain=false;
                        if(executeResult1.getErrorMessages()!=null&&executeResult1.getErrorMessages().size()>0){
                            for(int i=0;i<executeResult1.getErrorMessages().size();i++){
                                logger.error("仲裁时，系统完成线上退款关闭时发生异常:"+executeResult1.getErrorMessages().get(i));
                            }
                        }
                    }
                    text="卖家赢得仲裁：退款流程关闭，订单按照正常流程走。";
                }
                if(ifendcomplain){
                	for (ComplainDTO compd: list) {
                		compd.setComment(comment);
                		//已经仲裁
                		compd.setStatus(new Integer(1));
                		compd.setModified(new Date());
                		compd.setResolutionTime(new Date());
                		compd.setStatusText(text);
                		complainExportService.modifyComplainInfo(compd);
					}
                }else{
                    json.setMsg("仲裁完成出现异常，请稍后再试");
                    json.setSuccess(false);
                }
                
                request.setAttribute("complainResult", text);
            }
        }catch(Exception e){
            json.setMsg("仲裁出现意外错误，请联系管理员！");
            json.setSuccess(false);
            logger.error("仲裁出现异常:"+e.getMessage());
        }
        request.setAttribute("json",json);
        return json;
    }
    /**
     * 投诉仲裁
     * @author - 门光耀
     * @message- 仲裁买家赢
     * @createDate - 2015-4-16
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "sellerwin")
    @ResponseBody
    public Json sellererWin(HttpServletRequest request){
        Json json=new Json();
        try{
            //仲裁id
            String id=request.getParameter("id");
            //退款单id
            String tkdid=request.getParameter("tkdid");
            //仲裁说明
            String comment=request.getParameter("comment");
            ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(id));
            if(executeResult.getResult()!=null){
                ComplainDTO complainDTO=executeResult.getResult();
                complainDTO.setComment(comment);
                //已经仲裁
                complainDTO.setStatus(new Integer(1));
                complainDTO.setModified(new Date());
                complainDTO.setResolutionTime(new Date());
                complainDTO.setStatusText("仲裁关闭，买家卖家可能已达成协议，或卖家赢得仲裁，订单继续按照退款流程进行");
                complainExportService.modifyComplainInfo(complainDTO);
            }
            json.setMsg("仲裁完成");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("仲裁粗线意外错误，请联系管理员！");
            json.setSuccess(false);
        }
        return json;
    }
    /**
     * 投诉仲裁
     * @author - 门光耀
     * @message- 锁定订单
     * @createDate - 2015-4-16
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "lockorder")
    @ResponseBody
    public Json lockOrder(HttpServletRequest request){
        Json json=new Json();
        String orderId=request.getParameter("orderId");
        if(orderId!=null&&!"".equals(orderId)){
            TradeOrdersQueryInDTO inDTO=new TradeOrdersQueryInDTO();
			inDTO.setOrderId(orderId);
            ExecuteResult<DataGrid<TradeOrdersDTO>> execute= tradeOrderExportService.queryOrders(inDTO,null);
            //ExecuteResult<TradeOrdersDTO> executeResult= tradeOrderExportService.getOrderById(new Long(orderId));
            if(execute!=null&&execute.getResult()!=null&&execute.getResult().getRows()!=null&&execute.getResult().getRows().size()>0){
                if(execute.getResult().getRows().size()==1){
                    TradeOrdersDTO tradeOrdersDTO=execute.getResult().getRows().get(0);
                    tradeOrdersDTO.setLocked(2);
                    tradeOrdersDTO.setLockTime(new Date());
                    ExecuteResult<TradeOrdersDTO> executeResult1=tradeOrderExportService.updateTradeOrdersDTOSelective(tradeOrdersDTO);
                    TradeOrdersDTO tradeOrdersDTO1=executeResult1.getResult();
                    if(tradeOrdersDTO1.getLocked()!=null&&tradeOrdersDTO1.getLocked()==2){
                        json.setMsg("锁定订单完成");
                        json.setSuccess(true);
                    }else{
                        json.setMsg("锁定订单失败，请联系管理员");
                        json.setSuccess(false);
                    }
                }else{
                    json.setMsg("订单号不唯一无法锁定订单");
                    json.setSuccess(false);
                }
            }else{
                json.setMsg("无法获取订单信息");
                json.setSuccess(false);
            }
        }else{
            json.setMsg("后台无法获取订单号，锁定操作无法处理");
            json.setSuccess(false);
        }
        return json;
    }
    /**
     * 投诉仲裁
     * @author - 门光耀
     * @message- 锁定订单
     * @createDate - 2015-4-16
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "unlockorder")
    @ResponseBody
    public Json unLockOrder(HttpServletRequest request){
        Json json=new Json();
        String orderId=request.getParameter("orderId");
        if(orderId!=null&&!"".equals(orderId)){
            TradeOrdersQueryInDTO inDTO=new TradeOrdersQueryInDTO();
            inDTO.setOrderId(orderId);
            ExecuteResult<DataGrid<TradeOrdersDTO>> execute= tradeOrderExportService.queryOrders(inDTO,null);
            //ExecuteResult<TradeOrdersDTO> executeResult= tradeOrderExportService.getOrderById(new Long(orderId));
            if(execute!=null&&execute.getResult()!=null&&execute.getResult().getRows()!=null&&execute.getResult().getRows().size()>0){
                if(execute.getResult().getRows().size()==1){
                    TradeOrdersDTO tradeOrdersDTO=execute.getResult().getRows().get(0);
                    tradeOrdersDTO.setLocked(1);
                    //tradeOrdersDTO.setLockTime(new Date());
                    ExecuteResult<TradeOrdersDTO> executeResult1=tradeOrderExportService.updateTradeOrdersDTOSelective(tradeOrdersDTO);
                    TradeOrdersDTO tradeOrdersDTO1=executeResult1.getResult();
                    if(tradeOrdersDTO1.getLocked()!=null&&tradeOrdersDTO1.getLocked()==1){
                        json.setMsg("解锁订单完成");
                        json.setSuccess(true);
                    }else{
                        json.setMsg("解锁订单失败，请联系管理员");
                        json.setSuccess(false);
                    }
                }else{
                    json.setMsg("订单号不唯一无法解锁订单");
                    json.setSuccess(false);
                }
            }else{
                json.setMsg("无法获取订单信息");
                json.setSuccess(false);
            }
        }else{
            json.setMsg("后台无法获取订单号，解锁操作无法处理");
            json.setSuccess(false);
        }
        return json;
    }
}
