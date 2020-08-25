package com.camelot.ecm.complain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.aftersale.dto.RefundTransationsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoQueryDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.aftersale.service.RefundTransationsService;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.common.Json;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 退款，售后
 * @author - 门光耀
 * @message- 售后查询
 * @createDate - 2015-4-14
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/customerservice")
public class CustomerServiceController extends BaseController {
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
     @Resource
     private UserExtendsService userExtendsService;
     @Resource
     private RefundTransationsService refundTransationsService;
    /**
     * 售后退款
     * @author - 门光耀
     * @message- 退款查询
     * @createDate - 2015-4-14
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "customerservicelist")
    public ModelAndView list(HttpServletRequest request){
        Map<String,Object> map=new HashMap<String, Object>();
        Page page=new Page();
        page.setCount(0);
        page.setPageNo(1);
        page.setPageSize(10);
        map.put("tkstace",tkstaceList());
        TradeReturnInfoQueryDto queryDto=new TradeReturnInfoQueryDto();
        TradeReturnGoodsDTO tradeReturnGoodsDTO=new TradeReturnGoodsDTO();
        tradeReturnGoodsDTO.setIsCustomerService("0");
        queryDto.setTradeReturnDto(tradeReturnGoodsDTO);
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(10);
        DataGrid<TradeReturnGoodsDTO> dataGrid=tradeReturnExportService.getTradeReturnInfoDto(pager,queryDto);
        if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
            page.setList(tableList(dataGrid.getRows()));
            page.setCount(dataGrid.getTotal());
        }
        map.put("page",page);
        //TODO 服务规则列表查询
        return new ModelAndView("/complain/customerservicelist","map",map);
    }
    @RequestMapping(value = "customerserviceselectjson")
    @ResponseBody
    public Json customerServiceSelectJson(HttpServletRequest request){
        Json json=new Json();
        try{
            Pager pager=new Pager();
            String page=request.getParameter("page");
            TradeReturnGoodsDTO tradeReturnGoodsDTO=new TradeReturnGoodsDTO();
            String confirmStatus=request.getParameter("confirmStatus");
            if(StringUtils.isEmpty(confirmStatus)){
            	tradeReturnGoodsDTO.setIsCustomerService("0");
            }
            if(page!=null&&!"".equals(page)){
                pager.setPage(new Integer(page));
            }else{
                pager.setPage(1);
            }
            String rows=request.getParameter("rows");
            if(rows!=null&&!"".equals(rows)){
                pager.setRows(new Integer(rows));
            }else{
                pager.setRows(10);
            }
            Page page1=new Page();
            page1.setCount(0);
            page1.setPageNo(pager.getPage());
            page1.setPageSize(pager.getRows());
            //退款编码用退货编码
            String tkcode=request.getParameter("tkcode");
            if(tkcode!=null&&!"".equals(tkcode)){
                tradeReturnGoodsDTO.setCodeNo(tkcode);
            }
            String buyerNum=request.getParameter("buyerNum");
/*            if(buyerNum!=null&&!"".equals(buyerNum)){
                RegisterDTO registerDTO= userExportService.getUserInfoByUsername(buyerNum);
                if(registerDTO!=null&&registerDTO.getUid()!=null){
                    tradeReturnGoodsDTO.setBuyId(String.valueOf(registerDTO.getUid()));
                }else{
                    json.setMsg(page1.toString());
                    json.setObj(page1);
                    json.setSuccess(true);
                    return json;
                }
            }*/
            if(StringUtils.isNotBlank(confirmStatus)){
            	List<String> statusList=new ArrayList<String>();
        	    statusList.add("0");//待确认
        	    statusList.add("1");//平台处理中
        	    statusList.add("2");//平台已确认
        	    tradeReturnGoodsDTO.setConfirmStatusList(statusList);
            }
            tradeReturnGoodsDTO.setBuyerName(buyerNum);
            String tkstace=request.getParameter("tkstace");
            if(tkstace!=null&&!"".equals(tkstace)){
                tradeReturnGoodsDTO.setState(new Integer(tkstace));
            }
            String payBank=request.getParameter("payBank");
            if(payBank!=null&&!"".equals(payBank)){
            	tradeReturnGoodsDTO.setOrderPayBank(payBank);
            }
            String orderid=request.getParameter("orderid");
            if(orderid!=null&&!"".equals(orderid)){
                tradeReturnGoodsDTO.setOrderId(orderid);
            }
            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
            String createTimeBegin=request.getParameter("createTimeBegin");
            if(createTimeBegin!=null&&!"".equals(createTimeBegin)){
                tradeReturnGoodsDTO.setApplyDtBegin(sdf.parse(createTimeBegin));
            }
            String createTimeEnd=request.getParameter("createTimeEnd");
            if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                tradeReturnGoodsDTO.setApplyDtEnd(sdf.parse(createTimeEnd));
            }
            String refundTimeBegin=request.getParameter("refundTimeBegin");
            if(refundTimeBegin!=null&&!"".equals(refundTimeBegin)){
            	tradeReturnGoodsDTO.setRefundTimeBegin(sdf.parse(refundTimeBegin));
            }
            String refundTimeEnd=request.getParameter("refundTimeEnd");
            if(refundTimeEnd!=null&&!"".equals(refundTimeEnd)){
            	tradeReturnGoodsDTO.setRefundTimeEnd(sdf.parse(refundTimeEnd));
            }
            TradeReturnInfoQueryDto queryDto=new TradeReturnInfoQueryDto();
            queryDto.setTradeReturnDto(tradeReturnGoodsDTO);
            DataGrid<TradeReturnGoodsDTO> dataGrid=tradeReturnExportService.getTradeReturnInfoDto(pager,queryDto);
            if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
                page1.setList(tableList(dataGrid.getRows()));
                page1.setCount(dataGrid.getTotal());
            }
            json.setMsg(page1.toString());
            json.setObj(page1);
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("查询错误");
            json.setSuccess(false);
            logger.error("退款查询错误"+e.getMessage());
        }
        return json;
    }
    /**
     * 
     * <p>Discription:[平台确认退款列表]</p>
     * Created on 2015-9-28
     * @param request
     * @return
     * @author:[王鹏]
     */
    @RequestMapping(value = "confirmRefundList")
	public ModelAndView confirmRefundList(HttpServletRequest request){
	    Map<String,Object> map=new HashMap<String, Object>();
	    Page page=new Page();
	    page.setCount(0);
	    page.setPageNo(1);
	    page.setPageSize(10);
	    map.put("tkstace",tkstaceList());
	    map.put("payBankList", payBankList());
	    TradeReturnInfoQueryDto queryDto=new TradeReturnInfoQueryDto();
	    TradeReturnGoodsDTO tradeReturnGoodsDTO=new TradeReturnGoodsDTO();
	    List<String> statusList=new ArrayList<String>();
	    statusList.add("0");//待确认
	    statusList.add("1");//平台已确认
	    statusList.add("2");//平台处理中
	    tradeReturnGoodsDTO.setConfirmStatusList(statusList);
	    List<String> payBankList=new ArrayList<String>();
	    payBankList.add("0");//支付宝
//	    payBankList.add("100");//支付宝_手机端
//	    payBankList.add("1");//网银在线
//	    payBankList.add("101");//网银在线_手机端
//	    payBankList.add("2");//中信银行
//	    payBankList.add("3");//线下支付
//	    payBankList.add("4");//其他方式支付
//	    payBankList.add("5");//微信
//	    payBankList.add("6");//微信PC端
	    payBankList.add("8");//银联
	    tradeReturnGoodsDTO.setOrderPayBankList(payBankList);
	    queryDto.setTradeReturnDto(tradeReturnGoodsDTO);
	    Pager pager=new Pager();
	    pager.setPage(1);
	    pager.setRows(10);
	    DataGrid<TradeReturnGoodsDTO> dataGrid=tradeReturnExportService.getTradeReturnInfoDto(pager,queryDto);
	    if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
	    	page.setList(tableList(dataGrid.getRows()));
	        page.setCount(dataGrid.getTotal());
	    }
	    map.put("page",page);
	    //TODO 服务规则列表查询
	    return new ModelAndView("/complain/confirmRefundList","map",map);
	}
    /**。
     * 售后退款
     * @author - 门光耀
     * @message- 退款查询
     * @createDate - 2015-4-14
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "customerserviceshlist")
    public ModelAndView listSh(HttpServletRequest request){
        Map<String,Object> map=new HashMap<String, Object>();
        Page page=new Page();
        page.setCount(0);
        page.setPageNo(1);
        page.setPageSize(10);
        map.put("tkstace",tkstaceList());
        TradeReturnInfoQueryDto queryDto=new TradeReturnInfoQueryDto();
        TradeReturnGoodsDTO tradeReturnGoodsDTO=new TradeReturnGoodsDTO();
        tradeReturnGoodsDTO.setIsCustomerService("1");
        queryDto.setTradeReturnDto(tradeReturnGoodsDTO);
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(10);
        DataGrid<TradeReturnGoodsDTO> dataGrid=tradeReturnExportService.getTradeReturnInfoDto(pager,queryDto);
        if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
            page.setList(tableList(dataGrid.getRows()));
            page.setCount(dataGrid.getTotal());
        }
        map.put("page",page);
        //TODO 服务规则列表查询
        return new ModelAndView("/complain/customerserviceshlist","map",map);
    }
    @RequestMapping(value = "customerserviceselectshjson")
    @ResponseBody
    public Json customerServiceSelectShJson(HttpServletRequest request){
        Json json=new Json();
		try {
			Pager pager = new Pager();
			String page = request.getParameter("page");
			TradeReturnGoodsDTO tradeReturnGoodsDTO = new TradeReturnGoodsDTO();
			tradeReturnGoodsDTO.setIsCustomerService("1");
			if (page != null && !"".equals(page)) {
				pager.setPage(new Integer(page));
			} else {
				pager.setPage(1);
			}
			String rows = request.getParameter("rows");
			if (rows != null && !"".equals(rows)) {
				pager.setRows(new Integer(rows));
			} else {
				pager.setRows(10);
			}
			Page page1 = new Page();
			page1.setCount(0);
			page1.setPageNo(pager.getPage());
			page1.setPageSize(pager.getRows());
			String tkcode = request.getParameter("tkcode");
			if (tkcode != null && !"".equals(tkcode)) {
				tradeReturnGoodsDTO.setCodeNo(tkcode);
			}
			String buyerNum = request.getParameter("buyerNum");
			/*
			 * if(buyerNum!=null&&!"".equals(buyerNum)){ RegisterDTO
			 * registerDTO= userExportService.getUserInfoByUsername(buyerNum);
			 * if(registerDTO!=null&&registerDTO.getUid()!=null){
			 * tradeReturnGoodsDTO
			 * .setBuyId(String.valueOf(registerDTO.getUid())); }else{
			 * json.setMsg(page1.toString()); json.setObj(page1);
			 * json.setSuccess(true); return json; } }
			 */
			tradeReturnGoodsDTO.setBuyerName(buyerNum);
			String tkstace = request.getParameter("tkstace");
			if (tkstace != null && !"".equals(tkstace)) {
				tradeReturnGoodsDTO.setState(new Integer(tkstace));
			}
			String orderid = request.getParameter("orderid");
			if (orderid != null && !"".equals(orderid)) {
				tradeReturnGoodsDTO.setOrderId(orderid);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String createTimeBegin = request.getParameter("createTimeBegin");
			if (createTimeBegin != null && !"".equals(createTimeBegin)) {
				tradeReturnGoodsDTO.setApplyDtBegin(sdf.parse(createTimeBegin));
			}
			String createTimeEnd = request.getParameter("createTimeEnd");
			if (createTimeEnd != null && !"".equals(createTimeEnd)) {
				tradeReturnGoodsDTO.setApplyDtEnd(sdf.parse(createTimeEnd));
			}
			TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
			queryDto.setTradeReturnDto(tradeReturnGoodsDTO);
			DataGrid<TradeReturnGoodsDTO> dataGrid = tradeReturnExportService
					.getTradeReturnInfoDto(pager, queryDto);
			if (dataGrid != null && dataGrid.getRows() != null
					&& dataGrid.getRows().size() > 0) {
				page1.setList(tableList(dataGrid.getRows()));
				page1.setCount(dataGrid.getTotal());
			}
			json.setMsg(page1.toString());
			json.setObj(page1);
			json.setSuccess(true);
		}catch(Exception e){
            json.setMsg("查询错误");
            json.setSuccess(false);
            logger.error("退款查询错误"+e.getMessage());
        }
        return json;
    }
    private List<Map<String,String>> tableList(List<TradeReturnGoodsDTO> list){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        Iterator<TradeReturnGoodsDTO> iterator=list.iterator();
        Map<Integer,String> tkstaceMap=tkstaceMap();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Map<String,String> uands= selectUserAndShop(list);
        Map<String,String> tkCodeMap=tkCodeMap(list);
        Map<String,String> paybank=payBankMap();
        while(iterator.hasNext()){
            TradeReturnGoodsDTO tradeReturnGoodsDTO=iterator.next();
            Map<String,String> map=new HashMap<String, String>();

            if(tradeReturnGoodsDTO.getCodeNo()!=null&&!"".equals(tradeReturnGoodsDTO.getCodeNo())){
                //退款编码
                map.put("tkcode",tkCodeMap.get(tradeReturnGoodsDTO.getCodeNo()));
                //退货编码
                map.put("thcode",tradeReturnGoodsDTO.getCodeNo());
            }
            //退货单状态
            if(tradeReturnGoodsDTO.getState()!=null){
                map.put("statename",tkstaceMap.get(tradeReturnGoodsDTO.getState()));
            }
            //支付方式
            if(tradeReturnGoodsDTO.getOrderPayBank()!=null){
            	map.put("orderPayBank",paybank.get(tradeReturnGoodsDTO.getOrderPayBank()));
            	map.put("orderPayBankCode", tradeReturnGoodsDTO.getOrderPayBank());
            }
            //订单编号
            map.put("orderid",tradeReturnGoodsDTO.getOrderId());

            if(tradeReturnGoodsDTO.getSellerId()!=null&&!"".equals(tradeReturnGoodsDTO.getSellerId())){
                //卖家账号
                map.put("userName",uands.get(tradeReturnGoodsDTO.getSellerId()+"n"));
                //店铺名称
                map.put("shopName",uands.get(tradeReturnGoodsDTO.getSellerId()+"s"));
            }
            //买家账号
            map.put("buyername",tradeReturnGoodsDTO.getBuyerName());
            //申请时间
            if(tradeReturnGoodsDTO.getCreatedDt()!=null){
                map.put("credatedate",simpleDateFormat.format(tradeReturnGoodsDTO.getCreatedDt()));
            }
            if(tradeReturnGoodsDTO.getRefundTime()!=null){
            	map.put("refundTime",simpleDateFormat.format(tradeReturnGoodsDTO.getRefundTime()));
            }
            //退款金额
            if(tradeReturnGoodsDTO.getRefundGoods()!=null){
                map.put("refungood",tradeReturnGoodsDTO.getRefundGoods().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            }
            //申请运费
            if(tradeReturnGoodsDTO.getRefundFreight()!=null){
                map.put("refunfreight",tradeReturnGoodsDTO.getRefundFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            }
            //补足手续费
            if(tradeReturnGoodsDTO.getFactorage()!=null){
                map.put("factorage",tradeReturnGoodsDTO.getFactorage().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            }
            //查看详情退货单id
            if(tradeReturnGoodsDTO.getId()!=null){
                map.put("id",String.valueOf(tradeReturnGoodsDTO.getId()));
            }
            //查看手续费
            if(tradeReturnGoodsDTO.getFactorage()!=null){
                map.put("factorage",String.valueOf(tradeReturnGoodsDTO.getFactorage()));
            }
            if(tradeReturnGoodsDTO.getConfirmStatus()!=null){
            	map.put("confirmStatus", tradeReturnGoodsDTO.getConfirmStatus());
            }
            listMap.add(map);
        }
        return listMap;
    }
    private Map<String,String> selectUserAndShop(List<TradeReturnGoodsDTO> list){
        Map<String,String> uands=new HashMap<String, String>();
        Iterator<TradeReturnGoodsDTO> iterator=list.iterator();
        Map<String,String> useridmap=new HashMap<String, String>();
        while(iterator.hasNext()){
            TradeReturnGoodsDTO tradeReturnGoodsDTO=iterator.next();
            if(tradeReturnGoodsDTO.getSellerId()!=null&&!"".equals(tradeReturnGoodsDTO.getSellerId())){
                useridmap.put(tradeReturnGoodsDTO.getSellerId(),"");
            }
        }
        if(useridmap.size()>0){
            Iterator<String> iteratorids=useridmap.keySet().iterator();
            List<String> ids=new ArrayList<String>();
            //根据ids查询用户
            while(iteratorids.hasNext()){
                ids.add(iteratorids.next());
            }
            ExecuteResult<List<UserDTO>> executeResult =userExportService.findUserListByUserIds(ids);
            List<UserDTO> listuser=executeResult.getResult();
            Map<Long,String> mapshopid=new HashMap<Long, String>();
            if(listuser!=null&&listuser.size()>0){
                Iterator<UserDTO> iteratordto=listuser.iterator();
                while(iteratordto.hasNext()){
                    UserDTO userDTO=iteratordto.next();
                    if(userDTO.getUid()!=null){
                        uands.put(String.valueOf(userDTO.getUid())+"n",userDTO.getUname());
                        if(userDTO.getShopId()!=null){
                            mapshopid.put(userDTO.getShopId(),"");
                            uands.put(String.valueOf(userDTO.getShopId()),String.valueOf(userDTO.getUid())+"s");
                        }
                    }
                }
            }
            if(mapshopid.size()>0){
                Iterator<Long> iterator1=mapshopid.keySet().iterator();
                Long[] shopids=new Long[mapshopid.keySet().size()];
                int i=0;
                while(iterator1.hasNext()){
                    shopids[i]=iterator1.next();
                    i++;
                }
                ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
                shopAudiinDTO.setShopIds(shopids);
                ExecuteResult<List<ShopDTO>> executeResultshop=shopExportService.queryShopInfoByids(shopAudiinDTO);
                List<ShopDTO> listshopDto=executeResultshop.getResult();
                if(listshopDto!=null&&listshopDto.size()>0){
                    Iterator<ShopDTO> iterator2=listshopDto.iterator();
                    while(iterator2.hasNext()){
                        ShopDTO shopDTO=iterator2.next();
                        if(shopDTO.getShopId()!=null){
                            String us=uands.get(String.valueOf(shopDTO.getShopId()));
                            if(us!=null&&!"".equals(us)){
                                uands.put(us,shopDTO.getShopName());
                            }
                        }
                    }
                }
            }
        }
        return uands;
    }
    private Map<String,String> tkCodeMap(List<TradeReturnGoodsDTO> list){
        Map<String,String> map=new HashMap<String, String>();
        Iterator<TradeReturnGoodsDTO> iterator=list.iterator();
        Map<String,String> map1=new HashMap<String, String>();
        while(iterator.hasNext()){
            TradeReturnGoodsDTO tradeReturnGoodsDTO=iterator.next();
            if(tradeReturnGoodsDTO.getCodeNo()!=null&&!"".equals(tradeReturnGoodsDTO.getCodeNo())){
                map1.put(tradeReturnGoodsDTO.getCodeNo(),"");
            }
        }
        if(map1.size()>0){
            Iterator<String> iterator1=map1.keySet().iterator();
            Long refNos[]=new Long[map1.size()];
            int i=0;
            while(iterator1.hasNext()){
                refNos[i]=new Long(iterator1.next());
                i++;
            }
            RefundPayParam refundPayParam=new RefundPayParam();
            refundPayParam.setReproNos(refNos);
            /*DataGrid<RefundPayParam> dataGrid=paymentExportService.findRefInfoByCondition(refundPayParam,null);
            //DataGrid<RefundPayParam> dataGrid=null;
            if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
                Iterator<RefundPayParam> iterator2=dataGrid.getRows().iterator();
                while(iterator2.hasNext()){
                    RefundPayParam refundPayParam1=iterator2.next();
                    if(refundPayParam1.getReproNo()!=null&&!"".equals(refundPayParam1.getReproNo())){
                        map.put(String.valueOf(refundPayParam1.getReproNo()),refundPayParam1.getRefundNo());
                    }
                }
            }*/
        }
        return map;
    }
    @RequestMapping(value = "showdetail")
    public ModelAndView showDetail(HttpServletRequest request){
        String returngooid=request.getParameter("id");
        String url="/complain/tkshowdetail";

        Map<String,Object> map=new HashMap<String, Object>();
        if(returngooid!=null&&!"".equals(returngooid)){
            ExecuteResult<TradeReturnInfoDto> executeResult=tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returngooid);
            if(executeResult!=null&&executeResult.getResult()!=null){
                TradeReturnInfoDto tradeReturnInfoDto=executeResult.getResult();
                TradeReturnGoodsDTO tradeReturnGoodsDTO=tradeReturnInfoDto.getTradeReturnDto();
                List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailList=tradeReturnInfoDto.getTradeReturnGoodsDetailList();
                Map<Integer,String> tkStep=tkStep();
                Map<Integer,String> tkstaceMap=tkstaceMap();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                if(tradeReturnGoodsDTO!=null){
                    if(tradeReturnGoodsDTO.getId()!=null){
                        ComplainDTO comp=new ComplainDTO();
                        comp.setRefundId(tradeReturnGoodsDTO.getId());
                        DataGrid<ComplainDTO> da=complainExportService.findInfoByCondition(comp,null);
                        if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
                            List<ComplainDTO> list=da.getRows();
                            Iterator<ComplainDTO> iterator=list.iterator();
                            Map<Integer,String> complainMap=new HashMap<Integer, String>();
                            while(iterator.hasNext()){
                                ComplainDTO complainDTO=iterator.next();
                                if(complainDTO.getStatus()!=null&&complainDTO.getStatus()!=2){
                                    if(complainDTO.getStatus()==0){
                                        complainMap.put(0,"0");
                                    }else if(complainDTO.getStatus()==1){
                                        complainMap.put(1,"1");
                                    }
                                }
                            }
                            String complainStace1=complainMap.get(0);
                            String complainStace2=complainMap.get(1);
                            if(complainStace1!=null&&!"".equals(complainStace1)){
                                map.put("complainStace","买家已投诉~平台未仲裁！");
                            }else if(complainStace2!=null&&!"".equals(complainStace2)){
                                map.put("complainStace","买家已投诉~平台已仲裁！");
                            }
                        }
                    }
                    map.put("step","step0");
                    if(tradeReturnGoodsDTO.getIsCustomerService()!=null&&"1".equals(tradeReturnGoodsDTO.getIsCustomerService())){
                        url="/complain/shshowdetail";
                        //快递单号
                        map.put("exportNo",tradeReturnGoodsDTO.getExpressNo());
                        //快递公司
                        map.put("exportName",tradeReturnGoodsDTO.getExpressName());
                    }
                    if(tradeReturnGoodsDTO.getState()!=null){
                        map.put("step",tkStep.get(tradeReturnGoodsDTO.getState()));
                        map.put("staceName",tkstaceMap.get(tradeReturnGoodsDTO.getState()));
                    }
                    //退款原因
                    map.put("returnResion",tradeReturnGoodsDTO.getReturnResult());
                    //退款货品金额
                    BigDecimal a1=new BigDecimal(0);
                    if(tradeReturnGoodsDTO.getRefundGoods()!=null){
                        a1=tradeReturnGoodsDTO.getRefundGoods();
                        map.put("refundGoods",tradeReturnGoodsDTO.getRefundGoods().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                    }
                    //BigDecimal a2=new BigDecimal(0);
                    //退款运费
                    if(tradeReturnGoodsDTO.getRefundFreight()!=null){
                        //a2=tradeReturnGoodsDTO.getRefundFreight();
                        map.put("refundFreight",tradeReturnGoodsDTO.getRefundFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                    }
                    map.put("refundAll",a1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                    //退款说明
                    map.put("remark",tradeReturnGoodsDTO.getRemark());
                    List<TradeReturnPicDTO> picDTOList=tradeReturnGoodsDTO.getPicDTOList();
                    if(picDTOList!=null&&picDTOList.size()>0){
                        Iterator<TradeReturnPicDTO> itpic=picDTOList.iterator();
                        //凭证图片url
                        List<String> listpic=new ArrayList<String>();
                        while(itpic.hasNext()){
                            String picurl=itpic.next().getPicUrl();
                            if(picurl!=null&&!"".equals(picurl)){
                                listpic.add(picurl);
                            }
                        }
                        map.put("listpic",listpic);
                    }
                    //订单信息
                    if(tradeReturnGoodsDTO.getOrderId()!=null&&!"".equals(tradeReturnGoodsDTO.getOrderId())){
                        map.put("orderId",tradeReturnGoodsDTO.getOrderId());
                        ExecuteResult<TradeOrdersDTO> executeResult1=tradeOrderExportService.getOrderById(tradeReturnGoodsDTO.getOrderId());
                        if(executeResult1!=null&&executeResult1.getResult()!=null){
                            TradeOrdersDTO tradeOrdersDTO=executeResult1.getResult();
                            //订单创建时间
                            if(tradeOrdersDTO.getCreateTime()!=null){
                                map.put("orderCreatedate",simpleDateFormat.format(tradeOrdersDTO.getCreateTime()));
                            }
                            //订单付款时间
                            if(tradeOrdersDTO.getPaymentTime()!=null){
                                map.put("orderPaydate",simpleDateFormat.format(tradeOrdersDTO.getPaymentTime()));
                            }
                            Map<Integer,String> orderstaceMap=orderstaceMap();
                            //订单状态
                            if(tradeOrdersDTO.getState()!=null){
                                map.put("orderStace",orderstaceMap.get(tradeOrdersDTO.getState()));
                            }
                            //订单实付金额
                            if(tradeOrdersDTO.getPaymentPrice()!=null){
                                map.put("paymentPrice",tradeOrdersDTO.getPaymentPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                            }
                            //订单运费
                            if(tradeOrdersDTO.getFreight()!=null){
                                map.put("freight",tradeOrdersDTO.getFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                            }
                        }
                    }
                    //买家信息
                    map.put("buyerName",tradeReturnGoodsDTO.getBuyerName());
                    map.put("buyerPhone",tradeReturnGoodsDTO.getBuyerPhone());
                    if(tradeReturnGoodsDTO.getSellerId()!=null&&!"".equals(tradeReturnGoodsDTO.getSellerId())){
                        ExecuteResult<UserInfoDTO> userex =userExtendsService.findUserInfo(new Long(tradeReturnGoodsDTO.getSellerId()));
                        if(userex!=null&&userex.getResult()!=null){
                            UserInfoDTO userInfoDTO=userex.getResult();
                            if(userInfoDTO!=null&&userInfoDTO.getUserDTO()!=null){
                                //卖家账号
                                map.put("sellerName",userInfoDTO.getUserDTO().getUname());
                                //卖家电话
                                map.put("sellerPhone",userInfoDTO.getUserDTO().getUmobile());
                                if(userInfoDTO.getUserDTO().getShopId()!=null){
                                    ExecuteResult<ShopDTO> shopex=shopExportService.findShopInfoById(userInfoDTO.getUserDTO().getShopId());
                                    if(shopex!=null&&shopex.getResult()!=null){
                                        //卖家店铺名称
                                        map.put("sellerShopName",shopex.getResult().getShopName());
                                    }
                                }
                            }
                        }
                    }
                }
                if(tradeReturnGoodsDetailList!=null&&tradeReturnGoodsDetailList.size()>0){
                    List<Map<String,String>> detailList=new ArrayList<Map<String, String>>();
                    Iterator<TradeReturnGoodsDetailDTO> iterator=tradeReturnGoodsDetailList.iterator();
                    while(iterator.hasNext()){
                        Map<String,String> detail=new HashMap<String, String>();
                        TradeReturnGoodsDetailDTO tradeReturnGoodsDetailDTO=iterator.next();
                        //sku图片地址
                        detail.put("picUrl",tradeReturnGoodsDetailDTO.getGoodsPicUrl());
                        if(tradeReturnGoodsDetailDTO.getSkuId()!=null){
                            //skuid
                            detail.put("skuId",String.valueOf(tradeReturnGoodsDetailDTO.getSkuId()));
                        }
                        //item id
                        if(tradeReturnGoodsDetailDTO.getGoodsId()!=null){
                            detail.put("itemId",String.valueOf(tradeReturnGoodsDetailDTO.getGoodsId()));
                        }
                        detail.put("goodName",tradeReturnGoodsDetailDTO.getGoodsName());
                        if(tradeReturnGoodsDetailDTO.getPayPrice()!=null){
                            //单价
                            detail.put("payPrice",tradeReturnGoodsDetailDTO.getPayPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                        }
                        //数量
                        if(tradeReturnGoodsDetailDTO.getRerurnCount()!=null){
                            detail.put("returnCount",String.valueOf(tradeReturnGoodsDetailDTO.getRerurnCount()));
                        }
                        //金额
                        if(tradeReturnGoodsDetailDTO.getReturnAmount()!=null){
                            detail.put("returnAmount", tradeReturnGoodsDetailDTO.getReturnAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                        }
                        detailList.add(detail);
                    }
                    map.put("detailList",detailList);
                    map.put("size",String.valueOf(tradeReturnGoodsDetailList.size()));
                }else{
                    map.put("size","0");
                }
            }
        }
        return new ModelAndView(url,"map",map);
    }
    private Map<Integer,String> tkstaceMap(){
        //1 退款申请等待卖家确认中  2 卖家不同意协议,等待买家修改 3 退款申请达成,等待买家发货   4 买家已退货,等待卖家确认收货  5 退款关闭  6 退款成功 ,7 退款中
        Map<Integer,String> staceMap=new HashMap<Integer, String>();
        staceMap.put(1,"退款申请等待卖家确认中");
        staceMap.put(2,"卖家不同意协议,等待买家修改");
        staceMap.put(3,"退款申请达成,等待买家发货");
        staceMap.put(4,"买家已退货,等待卖家确认收货");
        staceMap.put(5,"退款关闭");
        staceMap.put(6,"退款成功");
        staceMap.put(7,"待买家确认收款");
        staceMap.put(8,"待平台处理退款");
        staceMap.put(9,"平台处理退款中");
        staceMap.put(10,"退款失败");
        staceMap.put(11,"退款申请成功，支付宝处理中");
        staceMap.put(12,"退款申请成功,银联处理中");
        staceMap.put(13,"待买家确认收款");
        return staceMap;
    }
    private Map<String,String> payBankMap(){
    	Map<String,String> staceMap=new HashMap<String, String>();
        staceMap.put("0","支付宝");
        staceMap.put("100","支付宝_手机端");
        staceMap.put("1","网银在线");
        staceMap.put("101","网银在线_手机端");
        staceMap.put("2","中信银行");
        staceMap.put("3","线下支付");
        staceMap.put("4","支付宝其他银行");
        staceMap.put("5","微信");
        staceMap.put("6","微信PC端");
        staceMap.put("8","银联");
        return staceMap;
    }
    private Map<Integer,String> tkStep(){
        Map<Integer,String> staceMap=new HashMap<Integer, String>();
        staceMap.put(1,"step3");
        staceMap.put(2,"step3");
        staceMap.put(3,"step3");
        staceMap.put(4,"step3");
        staceMap.put(5,"step0");
        staceMap.put(6,"step4");
        staceMap.put(7,"step3");
        staceMap.put(8,"step3");
        staceMap.put(9,"step3");
        staceMap.put(10,"step3");
        staceMap.put(11,"step3");
        return staceMap;
    }
    private List<Map<String,String>> tkstaceList(){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        Map<String,String> map1=new HashMap<String, String>();
        map1.put("code","1");
        map1.put("name","退款申请等待卖家确认中");
        listMap.add(map1);
        Map<String,String> map2=new HashMap<String, String>();
        map2.put("code","2");
        map2.put("name","卖家不同意协议,等待买家修改");
        listMap.add(map2);
        Map<String,String> map3=new HashMap<String, String>();
        map3.put("code","3");
        map3.put("name","退款申请达成,等待买家发货");
        listMap.add(map3);
        Map<String,String> map4=new HashMap<String, String>();
        map4.put("code","4");
        map4.put("name","买家已退货,等待卖家确认收货");
        listMap.add(map4);
        Map<String,String> map7=new HashMap<String, String>();
        map7.put("code","7");
        map7.put("name","待买家确认收款");
        listMap.add(map7);
        Map<String,String> map5=new HashMap<String, String>();
        map5.put("code","5");
        map5.put("name","退款关闭");
        listMap.add(map5);
        Map<String,String> map6=new HashMap<String, String>();
        map6.put("code","6");
        map6.put("name","退款成功");
        listMap.add(map6);
        Map<String,String> map8=new HashMap<String, String>();
        map8.put("code","8");
        map8.put("name","待平台处理退款");
        listMap.add(map8);
        Map<String,String> map9=new HashMap<String, String>();
        map9.put("code","9");
        map9.put("name","平台处理退款中");
        listMap.add(map9);
        Map<String,String> map10=new HashMap<String, String>();
        map10.put("code","10");
        map10.put("name","退款失败");
        listMap.add(map10);
        Map<String,String> map11=new HashMap<String, String>();
        map11.put("code","11");
        map11.put("name","退款申请成功，支付宝处理中");
        listMap.add(map11);
        
        Map<String,String> map12=new HashMap<String, String>();
        map12.put("code","12");
        map12.put("name","退款申请成功，银联处理中");
        listMap.add(map12);
        
        Map<String,String> map13=new HashMap<String, String>();
        map13.put("code","13");
        map13.put("name","待买家确认收款");
        listMap.add(map13);
        return listMap;
    }
    private Map<Integer,String> orderstaceMap(){
        //1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
        Map<Integer,String> staceMap=new HashMap<Integer, String>();
        staceMap.put(1,"待付款");
        staceMap.put(2,"待配送");
        staceMap.put(3,"待收货");
        staceMap.put(4,"待评价");
        staceMap.put(5,"已完成");
        staceMap.put(6,"已取消");
        staceMap.put(7,"已关闭");
        return staceMap;
    }
    
    private List<Map<String,String>> payBankList(){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        Map<String,String> map1=new HashMap<String, String>();
        map1.put("code","0");
        map1.put("name","支付宝");
        listMap.add(map1);
//        Map<String,String> map2=new HashMap<String, String>();
//        map2.put("code","100");
//        map2.put("name","支付宝_手机端");
//        listMap.add(map2);
//        Map<String,String> map3=new HashMap<String, String>();
//        map3.put("code","1");
//        map3.put("name","网银在线");
//        listMap.add(map3);
//        Map<String,String> map4=new HashMap<String, String>();
//        map4.put("code","101");
//        map4.put("name","网银在线_手机端");
//        listMap.add(map4);
//        Map<String,String> map7=new HashMap<String, String>();
//        map7.put("code","2");
//        map7.put("name","中信银行");
//        listMap.add(map7);
//        Map<String,String> map5=new HashMap<String, String>();
//        map5.put("code","3");
//        map5.put("name","线下支付");
//        listMap.add(map5);
//        Map<String,String> map6=new HashMap<String, String>();
//        map6.put("code","4");
//        map6.put("name","支付宝其他银行");
//        listMap.add(map6);
//        Map<String,String> map8=new HashMap<String, String>();
//        map8.put("code","5");
//        map8.put("name","微信");
//        listMap.add(map8);
//        Map<String,String> map9=new HashMap<String, String>();
//        map9.put("code","6");
//        map9.put("name","微信PC端");
//        listMap.add(map9);
        Map<String,String> map10=new HashMap<String, String>();
        map10.put("code","8");
        map10.put("name","银联");
        listMap.add(map10);
        return listMap;
    }
    /**
     * 
     * <p>Discription:[平台确认退款处理退款操作]</p>
     * Created on 2015-9-28
     * @return
     * @author:[王鹏]
     */
    @ResponseBody
    @RequestMapping(value="manageMent")
    public Json manage(HttpServletRequest request){
    	Json json = new Json();
    	String returnId=request.getParameter("returnId");
    	try{
			if(StringUtils.isNotBlank(returnId)){
	    		ExecuteResult<TradeReturnInfoDto> executeResult=tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
	    		TradeReturnInfoDto tradeReturnInfoDto=executeResult.getResult();
	    		TradeReturnGoodsDTO tradeReturnGoodsDTO=tradeReturnInfoDto.getTradeReturnDto();
	    		tradeReturnGoodsDTO.setConfirmStatus("2");//确认处理中
	    		tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.PLATFORMDEALING.getCode());//平台处理退款中
	    		ExecuteResult<TradeReturnGoodsDTO> returnGood = tradeReturnExportService.updateTradeReturnGoods(tradeReturnGoodsDTO);
	    		//直接更改状态为待平台处理，默认为成功
	    		json.setMsg("");
	    		json.setSuccess(true);
	//    		if(returnGood.isSuccess()){
	//    			json.setMsg("请通过购买虚拟商品向第三方支付账户中充值，并同意退款！");
	//           	    json.setSuccess(true);
	//    		}else{
	//    			json.setMsg("处理失败");
	//           	    json.setSuccess(false);
	//    		}
	    	 }else{
	    		 json.setMsg("退货单id为空");
	        	 json.setSuccess(false);
	    	 }
	    	
    	}catch(Exception e){
    		logger.error(e.getMessage());
            json.setMsg("系统出现意外错误，请联系管理员");
            json.setSuccess(false);
    	}
    	
    	return json;
    }
    /**
     * 
     * <p>Discription:[同意退款方法，调用退款接口]</p>
     * Created on 2015-9-28
     * @param request
     * @return
     * @author:[王鹏]
     */
    @ResponseBody
    @RequestMapping(value="agreeRefund")
    public Json agreeRefund(HttpServletRequest request){
    	Json json = new Json();
    	String returnId=request.getParameter("returnId");
    	try{
    	if(StringUtils.isNotBlank(returnId)){
    		ExecuteResult<TradeReturnInfoDto> executeResult=tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
    		TradeReturnInfoDto tradeReturnInfoDto=executeResult.getResult();
    		TradeReturnGoodsDTO tradeReturnGoodsDTO=tradeReturnInfoDto.getTradeReturnDto();
    		if(null!=tradeReturnGoodsDTO){
    			List<TradeReturnGoodsDetailDTO> details=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(tradeReturnGoodsDTO.getId()));
    			RefundReqParam param=new RefundReqParam();
    			param.setOrderNo(tradeReturnGoodsDTO.getOrderId());//订单id
        		param.setTotalAmount(tradeReturnGoodsDTO.getOrderPrice());//订单总金额
        		param.setRefundAmount(details.get(0).getReturnAmount());//退款金额
        		param.setPayBank(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name());//支付方式 
        		param.setRefundReason(tradeReturnGoodsDTO.getReturnResult());//退款原因
        		param.setDesc(tradeReturnGoodsDTO.getRemark());//退款描述
        		User currentUser = UserUtils.getUser();
        		param.setId(currentUser.getId());
        		param.setCodeNo(tradeReturnGoodsDTO.getCodeNo());//退款单号
        		//refundTransationsDTO.setOutRefundNo(param.getOutRefundNo());//对外交易号
        		ExecuteResult<Integer> res=paymentExportService.refundApply(param);
        		if(1==res.getResult()){//退款成功
        			tradeReturnGoodsDTO.setConfirmStatus("1");//确认同意退款
            		tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDING.getCode());//退款中
            		ExecuteResult<TradeReturnGoodsDTO> returnGood = tradeReturnExportService.updateTradeReturnGoods(tradeReturnGoodsDTO);
            		json.setMsg("操作成功");
        			json.setSuccess(true);
        		}else if(3==res.getResult()){
        			//退款申请成功
        			tradeReturnGoodsDTO.setConfirmStatus("1");//确认同意退款
            		if("AP".equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())){
						tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDAPPLICATION.getCode());// 退款申请成功
					}else if("CUP".equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())){
						tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDAPPLICATION_CUP.getCode());// 退款申请成功
						tradeReturnExportService.updateTradeReturnGoods(tradeReturnGoodsDTO);
					}
            		//如果是支付宝退款或银联退款
            		if("0".equals(tradeReturnGoodsDTO.getOrderPayBank()) ){
            			//不更新状态，传递url到页面并且弹出
            			json.setMsg(res.getResultMessage());
            			json.setSuccess(true);
            		}else if("8".equals(tradeReturnGoodsDTO.getOrderPayBank())){
            			json.setMsg("操作成功");
            			json.setSuccess(true);
            		}else{
            			tradeReturnExportService.updateTradeReturnGoods(tradeReturnGoodsDTO);
            			json.setMsg("操作成功");
            			json.setSuccess(true);
            		}
        		}else{//退款失败
        			json.setMsg(res.getErrorMessages().toString());
        			json.setSuccess(false);
        		}
    		}
    	}else{
    		json.setMsg("退货单id为空");
       	    json.setSuccess(false);
    	}
     }catch(Exception e){
 		logger.error(e.getMessage());
        json.setMsg("系统出现意外错误，请联系管理员");
        json.setSuccess(false); 
	}
    	return json;
    }
    
    
    
    @ResponseBody
    @RequestMapping(value="updateApStatue")
    public Json updateApStatue(HttpServletRequest request){
    	Json json = new Json();
    	String returnId=request.getParameter("returnId");
    	try{
    		if(StringUtils.isNotBlank(returnId)){
        		ExecuteResult<TradeReturnInfoDto> executeResult=tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
        		TradeReturnInfoDto tradeReturnInfoDto=executeResult.getResult();
        		TradeReturnGoodsDTO tradeReturnGoodsDTO=tradeReturnInfoDto.getTradeReturnDto();
        		if(null!=tradeReturnGoodsDTO){
        			if(tradeReturnGoodsDTO.getState() != 13 && tradeReturnGoodsDTO.getState() != 6 ){
        				List<TradeReturnGoodsDetailDTO> details=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(tradeReturnGoodsDTO.getId()));
            			RefundReqParam param=new RefundReqParam();
            			param.setOrderNo(tradeReturnGoodsDTO.getOrderId());//订单id
                		param.setTotalAmount(tradeReturnGoodsDTO.getOrderPrice());//订单总金额
                		param.setRefundAmount(details.get(0).getReturnAmount());//退款金额
                		param.setPayBank(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name());//支付方式 
                		param.setRefundReason(tradeReturnGoodsDTO.getReturnResult());//退款原因
                		param.setDesc(tradeReturnGoodsDTO.getRemark());//退款描述
                		User currentUser = UserUtils.getUser();
                		param.setId(currentUser.getId());
                		param.setCodeNo(tradeReturnGoodsDTO.getCodeNo());//退款单号
                		ExecuteResult<Integer> res=paymentExportService.refundApply(param);
            			//退款申请成功
            			tradeReturnGoodsDTO.setConfirmStatus("1");//确认同意退款
            			tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDAPPLICATION.getCode());// 退款申请成功
            			tradeReturnExportService.updateTradeReturnGoods(tradeReturnGoodsDTO);
        			}
        			json.setMsg("操作成功");
        			json.setSuccess(true);
        		}
        	}else{
        		json.setMsg("退货单id为空");
           	    json.setSuccess(false);
        	}
	     }catch(Exception e){
	 		logger.error(e.getMessage());
	        json.setMsg("系统出现意外错误，请联系管理员");
	        json.setSuccess(false); 
		}
	    	return json;
    }
    
    
}
