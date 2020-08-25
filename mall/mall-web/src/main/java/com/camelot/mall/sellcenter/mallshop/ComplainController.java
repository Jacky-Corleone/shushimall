package com.camelot.mall.sellcenter.mallshop;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.mall.util.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.encrypt.EncrypUtil;
import com.camelot.payment.PaymentExportService;
import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * <p>
 * Description: [前台投诉管理]
 * </p>
 * Created on 2015-4-17
 * 
 * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀你</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Controller
@RequestMapping(value = "/complain")
public class ComplainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComplainController.class);
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	
/*	@Resource
	private ItemExportService itemService;

	
	@Resource
	private UserExportService userExportService;*/
	
	@Resource
	private TradeReturnExportService tradeReturnExportService;
    @Resource
    private PaymentExportService paymentExportService;
    @Resource
    private ComplainExportService complainExportService;
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private UserExportService userExportService;
    /**
     *
     * <p>
     * Description: [跳到投诉新增界面,买家投诉]
     * </p>
     * Created on 2015-4-17
     *
     * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
     * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "gocomplainadd")
    public String gocomplainAdd(HttpServletRequest request,Model model){
    	//flag 标识是从买家还是卖家跳转过来，防止菜单错误
    	String flag=request.getParameter("flag");
        String url= "sellcenter/order/complainadd";
    	if(flag!=null && "2".equals(flag)){
    		url= "sellcenter/order/complainselleradd";
    	}
        String tradeReturnid=request.getParameter("tradeReturnid");
        String status=request.getParameter("status");
        //String tradeReturnid="24";
        model.addAttribute("right1","block");
        model.addAttribute("right","none");
        //买家投诉
        model.addAttribute("sellerOrBuyer","1");
        ComplainDTO comp=new ComplainDTO();
        comp.setRefundId(new Long(tradeReturnid));
        comp.setComplainType("1");
        if(status!=null && status.length()>0){
        	comp.setStatus(Integer.parseInt(status));
        }else{
        	comp.setStatus(0);
        }
        //获取尚未投诉尚未处理完的投诉单
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        DataGrid<ComplainDTO> da=complainExportService.findInfoByCondition(comp,null);
        //若为查看仲裁信息，则返回详细页面
        //progressBar为进度条当前到第几步
        if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
        	ComplainDTO complainDTO = da.getRows().get(0);
        	model.addAttribute("complainDTO",complainDTO);
        	if(complainDTO.getStatus()==0){
        		model.addAttribute("progressBar",2);	//审核中
        	}else if(complainDTO.getStatus()==1){
        		model.addAttribute("progressBar",3);	//仲裁完成
        	}
        }else{
        	model.addAttribute("progressBar",1);	//申请仲裁
        }
        //武超强修改 2015.7.3  注释以下代码
        /*if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
            model.addAttribute("right1","none");
            model.addAttribute("right","bolck");
            Long orderId=da.getRows().get(0).getOrderId();
            if(orderId!=null){
                model.addAttribute("orderId",String.valueOf(orderId));
            }
            Integer type=da.getRows().get(0).getType();
            if(type!=null&&type==2){
                model.addAttribute("typeName","售后相关");
            }else{
                model.addAttribute("typeName","退款相关");
            }
            if(da.getRows().get(0).getComplainType()!=null&&"1".equals(da.getRows().get(0).getComplainType())){
                //是卖家还是买家投诉的
                model.addAttribute("tusufang","买家");
            }else if(da.getRows().get(0).getComplainType()!=null&&"2".equals(da.getRows().get(0).getComplainType())){
                model.addAttribute("tusufang","卖家");
            }else{
                model.addAttribute("tusufang","无法识别");
            }
            //投诉状态
            model.addAttribute("zcstace","等待客服仲裁");
            model.addAttribute("stace",String.valueOf(0));
            model.addAttribute("remark",da.getRows().get(0).getComplainContent());
            if(da.getRows().get(0).getCreated()!=null){
                model.addAttribute("createdate",simpleDateFormat.format(da.getRows().get(0).getCreated()));
            }
            return url;
        }*/

        //是否退款true表示为退款投诉，false表示为售后投诉
        model.addAttribute("type","1");
        model.addAttribute("iftk",true);
        model.addAttribute("typeName","退款相关");
        Map<String,String> map=new HashMap<String, String>();
        //退款单状态
        Map<Integer,String> tkstaceMap=tkstaceMap();
        //订单状态
        Map<Integer,String> orderstaceMap=orderstaceMap();
        ExecuteResult<TradeReturnInfoDto> executeResult= tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(tradeReturnid);
        if(executeResult!=null){
            TradeReturnInfoDto infoDto=executeResult.getResult();
            if(infoDto!=null){
                TradeReturnGoodsDTO tradeReturnDto=infoDto.getTradeReturnDto();
                if(tradeReturnDto!=null){
                    if(tradeReturnDto.getSellerId()!=null){
                        //卖家id
                        model.addAttribute("sellerid",String.valueOf(tradeReturnDto.getSellerId()));
                    }
                    if(tradeReturnDto.getBuyId()!=null){
                        //买家id
                        model.addAttribute("buyerid",String.valueOf(tradeReturnDto.getBuyId()));
                    }
                    //获取获取退货单号
                    String reproNo=tradeReturnDto.getCodeNo();

                    //将退货id反到前台
                    model.addAttribute("thid",tradeReturnDto.getId());
                    if(reproNo!=null&&!"".equals(reproNo)){
                        RefundPayParam refundPayParam=new RefundPayParam();
                        refundPayParam.setReproNo(reproNo);
                        //DataGrid<RefundPayParam> dataGrid= paymentExportService.findRefInfoByCondition(refundPayParam,null);
/*                        if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
                            *//*****************************************//*
                            //获取退款单和退货单信息
                            RefundPayParam refundPayParam1=dataGrid.getRows().get(0);

                            //获取退款id
                            model.addAttribute("tkid",refundPayParam1.getId());
                            //退款单据号
                            map.put("refundNo",refundPayParam1.getRefundNo());
                        }*/
                        map.put("reproNo",reproNo);
                            if(tradeReturnDto.getApplyDt()!=null){
                                //退款申请时间
                                map.put("refcreated",simpleDateFormat.format(tradeReturnDto.getApplyDt()));
                            }
                            //退款原因
                            map.put("returnResult",tradeReturnDto.getReturnResult());
                            //退货单单状态
                            if(tradeReturnDto.getState()!=null){
                                map.put("tkstaceName",tkstaceMap.get(tradeReturnDto.getState()));
                            }
                            //订单退款货品金额
                            if(tradeReturnDto.getRefundGoods()!=null){
                                map.put("ordertkJe",tradeReturnDto.getRefundGoods().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                            }else{
                                map.put("ordertkJe","0.00");
                            }
                            //订单退货运费金额
                            if(tradeReturnDto.getRefundFreight()!=null){
                                map.put("ordertkYf",tradeReturnDto.getRefundFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                            }else{
                                map.put("ordertkYf","0.00");
                            }
                            //判断是否是售后申请
                            if(tradeReturnDto.getIsCustomerService()!=null&&"1".equals(tradeReturnDto.getIsCustomerService())){
                                model.addAttribute("iftk",false);
                                model.addAttribute("type","2");
                                model.addAttribute("typeName","售后相关");
                            }
                            /*********************************************/
                            //获取订单信息
                            String orderId=tradeReturnDto.getOrderId();
                            //订单号
                            model.addAttribute("orderId",orderId);
                            if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
                                TradeOrdersQueryInDTO tradeOrdersQueryInDTO=new TradeOrdersQueryInDTO();
                                Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
                                pager.setPage(1);
                                pager.setRows(10);
                                tradeOrdersQueryInDTO.setOrderId(orderId);
                                ExecuteResult<DataGrid<TradeOrdersDTO>> executeResultorder= tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
                                DataGrid<TradeOrdersDTO> dataGridorder=executeResultorder.getResult();
                                if(dataGridorder!=null&&dataGridorder.getRows()!=null&&dataGridorder.getRows().size()>0){
                                    TradeOrdersDTO tradeOrdersDTO=dataGridorder.getRows().get(0);
                                    //订单实际支付金额
                                    if(tradeOrdersDTO.getPaymentPrice()!=null){
                                        map.put("orderje",tradeOrdersDTO.getPaymentPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                                    }else{
                                        map.put("orderje","0.00");
                                    }
                                    //订单创建时间
                                    if(tradeOrdersDTO.getCreateTime()!=null){
                                        map.put("orderCreated",simpleDateFormat.format(tradeOrdersDTO.getCreateTime()));
                                    }
                                    //订单付款时间
                                    if(tradeOrdersDTO.getPaymentTime()!=null){
                                        map.put("orderpayTime",simpleDateFormat.format(tradeOrdersDTO.getPaymentTime()));
                                    }
                                    //订单状态
                                    if(tradeOrdersDTO.getState()!=null){
                                        map.put("orderStace",orderstaceMap.get(tradeOrdersDTO.getState()));
                                    }
                                    //获取退货明细信息
                                }
                            }
                            /**********************************************/

                    }
                }
            }
        }
        model.addAttribute("map",map);
        return url;
    }
    /**
     *
     * <p>
     * Description: [跳到投诉查看页面,买家投诉]
     * </p>
     * Created on 2015-4-17
     *
     * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
     * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "gocomplaindetail")
    public String gocomplainDetail(HttpServletRequest request,Model model){
        String url= "sellcenter/order/complaindetail";
        String complainid=request.getParameter("complainid");
        //String tradeReturnid="24";
        model.addAttribute("sellerOrBuyer","seller");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(complainid));
        if(executeResult!=null&&executeResult.getResult()!=null){
            ComplainDTO complainDTO=executeResult.getResult();
            String orderId=complainDTO.getOrderId();
            if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
                model.addAttribute("orderId",orderId);
            }
            Integer type=complainDTO.getType();
            if(type!=null&&type==2){
                model.addAttribute("typeName","售后相关");
            }else{
                model.addAttribute("typeName","退款相关");
            }
            if(complainDTO.getComplainType()!=null&&"1".equals(complainDTO.getComplainType())){
            	//是卖家还是买家投诉的
            	model.addAttribute("tusufang","买家");
            }else if(complainDTO.getComplainType()!=null&&"2".equals(complainDTO.getComplainType())){
            	model.addAttribute("tusufang","卖家");
            }else{
            	model.addAttribute("tusufang","无法识别");
            }
            //判断传过来的仲裁id 是不是申请仲裁的  如果相等则是仲裁的数据 否则是辩解的数据
            if(String.valueOf(complainDTO.getId()).equals(String.valueOf(complainDTO.getComplainGroup()))){
            	model.addAttribute("tusuFlag","投诉");
            }else{
            	model.addAttribute("tusuFlag","辩解");
            }
            Integer status=complainDTO.getStatus();
            if(status!=null&&status==1){
                model.addAttribute("zcstace","已经仲裁");
                model.addAttribute("zcjgms","已经仲裁完毕，如有疑问请联系客服，谢谢理解：-）");
                model.addAttribute("stace",String.valueOf(status));
            }else if(status!=null&&status==2){
                model.addAttribute("zcstace","已撤消投诉");
                model.addAttribute("zcjgms","仲裁已经撤销，如有疑问请重新提交仲裁，谢谢理解：-）");
                model.addAttribute("stace",String.valueOf(status));
            }else{
                model.addAttribute("zcstace","等待客服仲裁");
                model.addAttribute("zcjgms","正在等待客服处理，请耐心等待，谢谢理解：-）");
                model.addAttribute("stace",String.valueOf(0));
            }
            model.addAttribute("remark",complainDTO.getComplainContent());
            if(complainDTO.getCreated()!=null){
                model.addAttribute("createdate",simpleDateFormat.format(complainDTO.getCreated()));
            }
            //投诉原因
            model.addAttribute("complainresion",complainDTO.getComplainResion());
            //处理结果
            model.addAttribute("stacetext",complainDTO.getStatusText());
            //处理意见
            model.addAttribute("comment",complainDTO.getComment());
        }
        return url;
    }
    /**
     *
     * <p>
     * Description: [跳到投诉新增界面,卖家投诉]
     * </p>
     * Created on 2015-4-17
     *
     * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
     * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "gocomplainselleradd")
    public String gocomplainSellerAdd(HttpServletRequest request,Model model){
    	//flag 标识是从买家还是卖家跳转过来，防止菜单错误
    	String flag=request.getParameter("flag");
    	String url= "sellcenter/order/complainselleradd";
    	if(flag!=null && "1".equals(flag)){
    		url="sellcenter/order/complainadd";
    	}
        String tradeReturnid=request.getParameter("tradeReturnid");
        String status=request.getParameter("status");
        //String tradeReturnid="24";
        model.addAttribute("right1","block");
        model.addAttribute("right","none");
        //卖家投诉
        model.addAttribute("sellerOrBuyer","2");
        ComplainDTO comp=new ComplainDTO();
        comp.setRefundId(new Long(tradeReturnid));
        comp.setComplainType("2");
        if(status!=null && status.length()>0){
        	comp.setStatus(Integer.parseInt(status));
        }else{
        	comp.setStatus(0);
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        DataGrid<ComplainDTO> da=complainExportService.findInfoByCondition(comp,null);
        
        //若为查看仲裁信息，则返回详细页面
        //progressBar为进度条当前到第几步
        if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
        	ComplainDTO complainDTO = da.getRows().get(0);
        	model.addAttribute("complainDTO",complainDTO);
        	if(complainDTO.getStatus()==0){
        		model.addAttribute("progressBar",2);	//审核中
        	}else if(complainDTO.getStatus()==1){
        		model.addAttribute("progressBar",3);	//仲裁完成
        	}
        }else{
        	model.addAttribute("progressBar",1);	//申请仲裁
        }
        //武超强修改 2015.7.3  注释以下代码
        /*if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
            model.addAttribute("right1","none");
            model.addAttribute("right","bolck");
            Long orderId=da.getRows().get(0).getOrderId();
            if(orderId!=null){
                model.addAttribute("orderId",String.valueOf(orderId));
            }
            Integer type=da.getRows().get(0).getType();
            if(type!=null&&type==2){
                model.addAttribute("typeName","售后相关");
            }else{
                model.addAttribute("typeName","退款相关");
            }
            model.addAttribute("zcstace","等待客服仲裁");
            model.addAttribute("stace",String.valueOf(0));
            model.addAttribute("remark",da.getRows().get(0).getComplainContent());
            if(da.getRows().get(0).getCreated()!=null){
                model.addAttribute("createdate",simpleDateFormat.format(da.getRows().get(0).getCreated()));
            }
            return url;
        }*/

        //是否退款true表示为退款投诉，false表示为售后投诉
        model.addAttribute("type","1");
        model.addAttribute("iftk",true);
        model.addAttribute("typeName","退款相关");
        Map<String,String> map=new HashMap<String, String>();
        //退款单状态
        Map<Integer,String> tkstaceMap=tkstaceMap();
        //订单状态
        Map<Integer,String> orderstaceMap=orderstaceMap();
        ExecuteResult<TradeReturnInfoDto> executeResult= tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(tradeReturnid);
        if(executeResult!=null){
            TradeReturnInfoDto infoDto=executeResult.getResult();
            if(infoDto!=null){
                TradeReturnGoodsDTO tradeReturnDto=infoDto.getTradeReturnDto();
                if(tradeReturnDto!=null){
                    if(tradeReturnDto.getSellerId()!=null){
                        //卖家id
                        model.addAttribute("sellerid",String.valueOf(tradeReturnDto.getSellerId()));
                    }
                    if(tradeReturnDto.getBuyId()!=null){
                        //买家id
                        model.addAttribute("buyerid",String.valueOf(tradeReturnDto.getBuyId()));
                    }
                    //获取获取退货单号
                    String reproNo=tradeReturnDto.getCodeNo();
                    //将退货id反到前台
                    model.addAttribute("thid",tradeReturnDto.getId());
                    if(reproNo!=null&&!"".equals(reproNo)){
                        RefundPayParam refundPayParam=new RefundPayParam();
                        refundPayParam.setReproNo(reproNo);
/*                        DataGrid<RefundPayParam> dataGrid= paymentExportService.findRefInfoByCondition(refundPayParam,null);
                        if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0) {
                            *//*****************************************//*
                            //获取退款单和退货单信息
                            RefundPayParam refundPayParam1 = dataGrid.getRows().get(0);
                            //获取退款id
                            model.addAttribute("tkid", refundPayParam1.getId());
                            //退款单据号
                            map.put("refundNo", refundPayParam1.getRefundNo());
                        }*/
                            map.put("reproNo",reproNo);
                            if(tradeReturnDto.getApplyDt()!=null){
                                //退款申请时间
                                map.put("refcreated",simpleDateFormat.format(tradeReturnDto.getApplyDt()));
                            }
                            //退款原因
                            map.put("returnResult",tradeReturnDto.getReturnResult());
                            //退货单单状态
                            if(tradeReturnDto.getState()!=null){
                                map.put("tkstaceName",tkstaceMap.get(tradeReturnDto.getState()));
                            }
                            //订单退款货品金额
                            if(tradeReturnDto.getRefundGoods()!=null){
                                map.put("ordertkJe",tradeReturnDto.getRefundGoods().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                            }else{
                                map.put("ordertkJe","0.00");
                            }
                            //订单退货运费金额
                            if(tradeReturnDto.getRefundFreight()!=null){
                                map.put("ordertkYf",tradeReturnDto.getRefundFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                            }else{
                                map.put("ordertkYf","0.00");
                            }
                            //判断是否是售后申请
                            if(tradeReturnDto.getIsCustomerService()!=null&&"1".equals(tradeReturnDto.getIsCustomerService())){
                                model.addAttribute("iftk",false);
                                model.addAttribute("type","2");
                                model.addAttribute("typeName","售后相关");
                            }
                            /*********************************************/
                            //获取订单信息
                            String orderId=tradeReturnDto.getOrderId();
                            //订单号
                            model.addAttribute("orderId",orderId);
                            if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
                                TradeOrdersQueryInDTO tradeOrdersQueryInDTO=new TradeOrdersQueryInDTO();
                                Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
                                pager.setPage(1);
                                pager.setRows(10);
                                tradeOrdersQueryInDTO.setOrderId(orderId);
                                ExecuteResult<DataGrid<TradeOrdersDTO>> executeResultorder= tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
                                DataGrid<TradeOrdersDTO> dataGridorder=executeResultorder.getResult();
                                if(dataGridorder!=null&&dataGridorder.getRows()!=null&&dataGridorder.getRows().size()>0){
                                    TradeOrdersDTO tradeOrdersDTO=dataGridorder.getRows().get(0);
                                    //订单实际支付金额
                                    if(tradeOrdersDTO.getPaymentPrice()!=null){
                                        map.put("orderje",tradeOrdersDTO.getPaymentPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                                    }else{
                                        map.put("orderje","0.00");
                                    }
                                    //订单创建时间
                                    if(tradeOrdersDTO.getCreateTime()!=null){
                                        map.put("orderCreated",simpleDateFormat.format(tradeOrdersDTO.getCreateTime()));
                                    }
                                    //订单付款时间
                                    if(tradeOrdersDTO.getPaymentTime()!=null){
                                        map.put("orderpayTime",simpleDateFormat.format(tradeOrdersDTO.getPaymentTime()));
                                    }
                                    //订单状态
                                    if(tradeOrdersDTO.getState()!=null){
                                        map.put("orderStace",orderstaceMap.get(tradeOrdersDTO.getState()));
                                    }
                                    //获取退货明细信息
                                }
                            }
                            /**********************************************/

                    }
                }
            }
        }
        model.addAttribute("map",map);
        return url;
    }
    /**
     *
     * <p>
     * Description: [跳到投诉查看页面,买家投诉]
     * </p>
     * Created on 2015-4-17
     *
     * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
     * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "gocomplainsellerdetail")
    public String gocomplainSellerDetail(HttpServletRequest request,Model model){
        String url= "sellcenter/order/complainsellerdetail";
        String complainid=request.getParameter("complainid");
        //String tradeReturnid="24";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(complainid));
        if(executeResult!=null&&executeResult.getResult()!=null){
            ComplainDTO complainDTO=executeResult.getResult();
            String orderId=complainDTO.getOrderId();
            if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
                model.addAttribute("orderId",orderId);
            }
            Integer type=complainDTO.getType();
            if(type!=null&&type==2){
                model.addAttribute("typeName","售后相关");
            }else{
                model.addAttribute("typeName","退款相关");
            }
            if(complainDTO.getComplainType()!=null&&"1".equals(complainDTO.getComplainType())){
                //是卖家还是买家投诉的
                model.addAttribute("tusufang","买家");
            }else if(complainDTO.getComplainType()!=null&&"2".equals(complainDTO.getComplainType())){
                model.addAttribute("tusufang","卖家");
            }else{
                model.addAttribute("tusufang","无法识别");
            }
            Integer status=complainDTO.getStatus();
            if(status!=null&&status==1){
                model.addAttribute("zcstace","已经仲裁");
                model.addAttribute("zcjgms","已经仲裁完毕，如有疑问请联系客服，谢谢理解：-）");
                model.addAttribute("stace",String.valueOf(status));
            }else if(status!=null&&status==2){
                model.addAttribute("zcstace","已撤消投诉");
                model.addAttribute("zcjgms","仲裁已经撤销，如有疑问请重新提交仲裁，谢谢理解：-）");
                model.addAttribute("stace",String.valueOf(status));
            }else{
                model.addAttribute("zcstace","等待客服仲裁");
                model.addAttribute("zcjgms","正在等待客服处理，请耐心等待，谢谢理解：-）");
                model.addAttribute("stace",String.valueOf(0));
            }
            model.addAttribute("remark",complainDTO.getComplainContent());
            if(complainDTO.getCreated()!=null){
                model.addAttribute("createdate",simpleDateFormat.format(complainDTO.getCreated()));
            }
            //投诉原因
            model.addAttribute("complainresion",complainDTO.getComplainResion());
            //处理结果
            model.addAttribute("stacetext",complainDTO.getStatusText());
            //处理意见
            model.addAttribute("comment",complainDTO.getComment());
        }
        return url;
    }
    @RequestMapping(value = "complainadd")
    @ResponseBody
    public Json complainAdd(HttpServletRequest request,Model model){
        Json json=new Json();
        try{
            //订单号
            String orderId=request.getParameter("orderId");
            //退货id
            String thid=request.getParameter("thid");
            //退款id
            String tkid=request.getParameter("tkid");
            //投诉说明
            String remark=request.getParameter("remark");
            //买家电话
            String buyermobile=request.getParameter("buyermobile");
            //买家邮箱
            String buyeremail=request.getParameter("buyeremail");
            //投诉原因
            String complainResion=request.getParameter("complainResion");
            //投诉类型，是售后还是退款投诉
            String type=request.getParameter("type");
            //卖家
            String sellerid=request.getParameter("sellerid");
            //买家
            String buyerid=request.getParameter("buyerid");
            String url=request.getParameter("url");
            String sellerOrBuyer=request.getParameter("sellerOrBuyer");

            ComplainDTO complainDTO=new ComplainDTO();
            if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
                complainDTO.setOrderId(orderId);
            }
            if(thid!=null&&!"".equals(thid)){
                complainDTO.setReturnGoodsId(new Long(thid));
                complainDTO.setRefundId(new Long(thid));
            }
            if(type!=null&&!"".equals(type)){
                complainDTO.setType(new Integer(type));
            }
            if(sellerid!=null&&!"".equals(sellerid)){
                complainDTO.setSellerId(new Long(sellerid));
                ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
                shopAudiinDTO.setSellerId(new Long(sellerid));
                ExecuteResult<List<ShopDTO>> executeResult= shopExportService.queryShopInfoByids(shopAudiinDTO);
                if(executeResult.getResult()!=null&&executeResult.getResult().size()>0){
                    //设置店铺名称
                    complainDTO.setShopName(executeResult.getResult().get(0).getShopName());
                }
            }
            complainDTO.setComplainContent(remark);
            complainDTO.setComplainEmail(buyeremail);
            complainDTO.setComplainPhone(buyermobile);
            complainDTO.setComplainResion(complainResion);
            complainDTO.setComplainPicUrl(url);
            //1买家投诉,2卖家投诉
            complainDTO.setComplainType(sellerOrBuyer);
            //待仲裁
            complainDTO.setStatus(new Integer(0));
            //Long uid = WebUtil.getInstance().getUserId(request);
            if(buyerid!=null&&!"".equals(buyerid)){
                complainDTO.setBuyerId(new Long(buyerid));
            }
            ExecuteResult<ComplainDTO> executeResult=complainExportService.addComplainInfo(complainDTO);
            if(executeResult.isSuccess()){
            	//更新仲裁分组 
            	ExecuteResult<String> updateComplainGroupInfo = updateComplainGroup(new Long(thid), executeResult.getResult().getId(),sellerOrBuyer);
            	
                Map<String,String> returnMap=new HashMap<String, String>();
                returnMap.put("complainContent",remark);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                returnMap.put("date",simpleDateFormat.format(new Date()));
                json.setObj(returnMap);
                returnMap.put("complainId", executeResult.getResult().getId().toString());
                if(executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
                    Iterator<String> iterator=executeResult.getErrorMessages().iterator();
                    while(iterator.hasNext()){
                        LOGGER.error(iterator.next());
                    }
                }
                json.setMsg("投诉提交成功");
                json.setSuccess(true);
            }else{
                if(executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
                    Iterator<String> iterator=executeResult.getErrorMessages().iterator();
                    while(iterator.hasNext()){
                        LOGGER.error(iterator.next());
                    }
                }
                json.setMsg("投诉提交失败");
                json.setSuccess(false);
            }
        }catch(Exception e){
            json.setMsg("投诉提交失败");
            LOGGER.error(e.getMessage()+e.toString());
            json.setSuccess(false);
        }
        request.setAttribute("json",json);
        return json;
    }


    /**
     *
     * <p>
     * Description: [跳到投诉查询页面]
     * </p>
     * Created on 2015-4-17
     *
     * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
     * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "gocomplainlist")
    public String goComplainList(HttpServletRequest request,Model model){
        List<Map<String,Object>> listMap=new ArrayList<Map<String, Object>>();
        String url= "sellcenter/order/complainlist";
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        if(page==null||"".equals(page)){
            page="1";
        }
        if(rows==null||"".equals(rows)){
            rows="10";
        }
        String orderId=request.getParameter("orderId");
        String createTimeStart=request.getParameter("createTimeStart");
        String createTimeEnd=request.getParameter("createTimeEnd");
        String complaintype=request.getParameter("complaintype");
        //用户id
        Long uid = WebUtil.getInstance().getUserId(request);
        Pager pager=new Pager();
        pager.setRows(new Integer(rows));
        pager.setPage(new Integer(page));
        pager.setTotalCount(0);
        ComplainDTO complainDTO=new ComplainDTO();
        //complainDTO.setStatusSelect("2");	//投诉页面使用标识
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(uid);
		complainDTO.setBuyIdList(idsEr.getResult());
//        complainDTO.setBuyerId(uid);
        if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
            complainDTO.setOrderId(orderId);
        }
        if(createTimeStart!=null&&!"".equals(createTimeStart)){
            complainDTO.setCreatedBegin(createTimeStart);
        }
        if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
            complainDTO.setCreatedEnd(createTimeEnd);
        }
        if(complaintype!=null&&!"".equals(complaintype)){
            complainDTO.setComplainType(complaintype);
        }
        DataGrid<ComplainDTO> dataGrid=complainExportService.findEarlyComplainInfoByCondition(complainDTO,pager);
        if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
            List<ComplainDTO> list=dataGrid.getRows();
            Map<String,List<Map<String,String>>> goodsDetail=goodsDetail(list);
            if(list!=null){
                Iterator<ComplainDTO> iterator=list.iterator();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                while(iterator.hasNext()){
                    ComplainDTO comp=iterator.next();
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("orderId",comp.getOrderId());
                    map.put("shopName",comp.getShopName());
                    if(comp.getCreated()!=null){
                        map.put("created",simpleDateFormat.format(comp.getCreated()));
                    }
                    if(comp.getSellerId()!=null){
                        map.put("sellerId",String.valueOf(comp.getSellerId()));
                    }
                    if(comp.getRefundId()!=null){
                        map.put("thid",String.valueOf(comp.getRefundId()));
                        map.put("skuId",String.valueOf(comp.getSkuId()));
                        map.put("refundId",String.valueOf(comp.getRefundId()));
                        ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", comp.getRefundId()+""+comp.getSkuId());
                        if(passKeyEr.isSuccess()){
                        	map.put("passKey", passKeyEr.getResult());
                        }
                    }
                    if(comp.getId()!=null){
                        map.put("complainid",String.valueOf(comp.getId()));
                        ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", comp.getId()+"");
                        if(passKeyEr.isSuccess()){
                        	map.put("passKey2", passKeyEr.getResult());
                        }
                    }
                    if(comp.getStatus()!=null&&comp.getStatus()==1){
                        map.put("staceName","退款已仲裁");
                        map.put("stace","1");
                    }else if(comp.getStatus()!=null&&comp.getStatus()==2){
                        map.put("staceName","已取消");
                        map.put("stace","2");
                    }else{
                        map.put("staceName","待客服仲裁");
                        map.put("stace","0");
                    }
                    if(comp.getComplainType()!=null&&"1".equals(comp.getComplainType())){
                        map.put("sellerOrBuyer","1");
                        map.put("tousufang","买家投诉");
                    }else if(comp.getComplainType()!=null&&"2".equals(comp.getComplainType())){
                        map.put("sellerOrBuyer","2");
                        map.put("tousufang","店铺投诉");
                    }else{
                        map.put("sellerOrBuyer","3");
                        map.put("tousufang","无法判定投诉方");
                    }
                    if(comp.getRefundId()!=null){
                        map.put("list",goodsDetail.get(String.valueOf(comp.getRefundId())));
                    }
                    listMap.add(map);
                }
            }
            pager.setTotalCount(dataGrid.getTotal().intValue());
        }
        pager.setRecords(listMap);
        model.addAttribute("pageNo",new Integer(page));
        model.addAttribute("pager",pager);
        model.addAttribute("complainDTO",complainDTO);
        return url;
    }
    /**
     *
     * <p>
     * Description: [跳到卖家投诉查询页面]
     * </p>
     * Created on 2015-4-17
     *
     * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
     * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "gocomplainsellerlist")
    public String goComplainSellerList(HttpServletRequest request,Model model){
        List<Map<String,Object>> listMap=new ArrayList<Map<String, Object>>();
        String url= "sellcenter/order/complainsellerlist";
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        String complaintype=request.getParameter("complaintype");
        if(page==null||"".equals(page)){
            page="1";
        }
        if(rows==null||"".equals(rows)){
            rows="10";
        }
        String orderId=request.getParameter("orderId");
        String createTimeStart=request.getParameter("createTimeStart");
        String createTimeEnd=request.getParameter("createTimeEnd");
        //用户id
        Long uid = WebUtil.getInstance().getUserId(request);
        UserDTO userDTO = userExportService.queryUserById(uid);
        Pager pager=new Pager();
        pager.setRows(new Integer(rows));
        pager.setPage(new Integer(page));
        pager.setTotalCount(0);
        ComplainDTO complainDTO=new ComplainDTO();
        complainDTO.setStatusSelect("2");	//投诉页面使用标识
        if(userDTO.getParentId() == null){//主账号id
            complainDTO.setSellerId(uid);
        }else{//子账号id
            complainDTO.setSellerId(userDTO.getParentId());
        }
        if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
            complainDTO.setOrderId(orderId);
        }
        if(createTimeStart!=null&&!"".equals(createTimeStart)){
            complainDTO.setCreatedBegin(createTimeStart);
        }
        if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
            complainDTO.setCreatedEnd(createTimeEnd);
        }
        if(complaintype!=null&&!"".equals(complaintype)){
            complainDTO.setComplainType(complaintype);
        }
        DataGrid<ComplainDTO> dataGrid=complainExportService.findEarlyComplainInfoByCondition(complainDTO,pager);
        if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
            List<ComplainDTO> list=dataGrid.getRows();
            Map<String,List<Map<String,String>>> goodsDetail=goodsDetail(list);
            if(list!=null){
                Iterator<ComplainDTO> iterator=list.iterator();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                while(iterator.hasNext()){
                    ComplainDTO comp=iterator.next();
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("orderId",comp.getOrderId());
                    map.put("shopName",comp.getShopName());
                    if(comp.getCreated()!=null){
                        map.put("created",simpleDateFormat.format(comp.getCreated()));
                    }
                    if(comp.getSellerId()!=null){
                        map.put("sellerId",String.valueOf(comp.getSellerId()));
                    }
                    if(comp.getRefundId()!=null){
                        map.put("thid",String.valueOf(comp.getRefundId()));
                        ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", comp.getRefundId()+"");
                        if(passKeyEr.isSuccess()){
                        	map.put("passKey",passKeyEr.getResult());
                        }
                        map.put("refundId",String.valueOf(comp.getRefundId()));
                    }
                    if(comp.getId()!=null){
                        map.put("complainid",String.valueOf(comp.getId()));
                        ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", comp.getId()+"");
                        if(passKeyEr.isSuccess()){
                        	map.put("passKey2",passKeyEr.getResult());
                        }
                    }
                    if(comp.getStatus()!=null&&comp.getStatus()==1){
                        map.put("staceName","退款已仲裁");
                        map.put("stace","1");
                    }else if(comp.getStatus()!=null&&comp.getStatus()==2){
                        map.put("staceName","已取消");
                        map.put("stace","2");
                    }else{
                        map.put("staceName","待客服仲裁");
                        map.put("stace","0");
                    }
                    if(comp.getComplainType()!=null&&"1".equals(comp.getComplainType())){
                        map.put("sellerOrBuyer","1");
                        map.put("tousufang","买家投诉");
                    }else if(comp.getComplainType()!=null&&"2".equals(comp.getComplainType())){
                        map.put("sellerOrBuyer","2");
                        map.put("tousufang","店铺投诉");
                    }else{
                        map.put("sellerOrBuyer","3");
                        map.put("tousufang","无法判定投诉方");
                    }
                    if(comp.getRefundId()!=null){
                        map.put("list",goodsDetail.get(String.valueOf(comp.getRefundId())));
                    }
                    listMap.add(map);
                }
            }
            pager.setTotalCount(dataGrid.getTotal().intValue());
        }
        pager.setRecords(listMap);
        model.addAttribute("pageNo",new Integer(page));
        model.addAttribute("pager",pager);
        model.addAttribute("complainDTO",complainDTO);
        return url;
    }
    @RequestMapping(value = "selectconplainlist")
    @ResponseBody
    public Json selectConplainList(HttpServletRequest request){
        Json json=new Json();
        try{
            List<Map<String,Object>> listMap=new ArrayList<Map<String, Object>>();
            String page=request.getParameter("page");
            String rows=request.getParameter("rows");
            String complaintype=request.getParameter("complaintype");
            if(page==null||"".equals(page)){
                page="1";
            }
            if(rows==null||"".equals(rows)){
                rows="10";
            }
            String orderId=request.getParameter("orderId");
            String createTimeStart=request.getParameter("createTimeStart");
            String createTimeEnd=request.getParameter("createTimeEnd");
            //用户id
            Long uid = WebUtil.getInstance().getUserId(request);
            Pager pager=new Pager();
            pager.setRows(new Integer(rows));
            pager.setPage(new Integer(page));
            pager.setTotalCount(0);
            ComplainDTO complainDTO=new ComplainDTO();
            complainDTO.setBuyerId(uid);
            if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
                complainDTO.setOrderId(orderId);
            }
            if(createTimeStart!=null&&!"".equals(createTimeStart)){
                complainDTO.setCreatedBegin(createTimeStart);
            }
            if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                complainDTO.setCreatedEnd(createTimeEnd);
            }
            if(complaintype!=null&&!"".equals(complaintype)){
                complainDTO.setComplainType(complaintype);
            }
            DataGrid<ComplainDTO> dataGrid=complainExportService.findInfoByCondition(complainDTO,pager);
            if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
                List<ComplainDTO> list=dataGrid.getRows();
                Map<String,List<Map<String,String>>> goodsDetail=goodsDetail(list);
                    Iterator<ComplainDTO> iterator=list.iterator();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                    while(iterator.hasNext()){
                        ComplainDTO comp=iterator.next();
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put("orderId",comp.getOrderId());
                        map.put("shopName",comp.getShopName());
                        if(comp.getCreated()!=null){
                            map.put("created",simpleDateFormat.format(comp.getCreated()));
                        }
                        if(comp.getSellerId()!=null){
                            map.put("sellerId",String.valueOf(comp.getSellerId()));
                        }
                        if(comp.getRefundId()!=null){
                            map.put("thid",String.valueOf(comp.getRefundId()));
                        }
                        if(comp.getId()!=null){
                            map.put("complainid",String.valueOf(comp.getId()));
                        }
                        if(comp.getStatus()!=null&&comp.getStatus()==1){
                            map.put("staceName","退款已仲裁");
                            map.put("stace","1");
                        }else if(comp.getStatus()!=null&&comp.getStatus()==2){
                            map.put("staceName","已取消");
                            map.put("stace","2");
                        }else{
                            map.put("staceName","待客服仲裁");
                            map.put("stace","0");
                        }
                        if(comp.getComplainType()!=null&&"1".equals(comp.getComplainType())){
                            map.put("sellerOrBuyer","1");
                            map.put("tousufang","买家投诉");
                        }else if(comp.getComplainType()!=null&&"2".equals(comp.getComplainType())){
                            map.put("sellerOrBuyer","2");
                            map.put("tousufang","店铺投诉");
                        }else{
                            map.put("sellerOrBuyer","3");
                            map.put("tousufang","无法判定投诉方");
                        }
                        if(comp.getRefundId()!=null){
                            map.put("list",goodsDetail.get(String.valueOf(comp.getRefundId())));
                        }
                        listMap.add(map);
                    }

                pager.setTotalCount(dataGrid.getTotal().intValue());
            }
            pager.setRecords(listMap);
            json.setSuccess(true);
            json.setMsg("查询成功");
            json.setObj(pager);
        }catch(Exception e){
            LOGGER.error(e.getMessage());
            LOGGER.error(e.toString());
            json.setSuccess(false);
            json.setMsg("查询失败");
        }
        return json;
    }
    @RequestMapping(value = "selectconplainsellerlist")
    @ResponseBody
    public Json selectConplainSellerList(HttpServletRequest request){
        Json json=new Json();
        try{
            List<Map<String,Object>> listMap=new ArrayList<Map<String, Object>>();
            String page=request.getParameter("page");
            String rows=request.getParameter("rows");
            String complaintype=request.getParameter("complaintype");
            if(page==null||"".equals(page)){
                page="1";
            }
            if(rows==null||"".equals(rows)){
                rows="10";
            }
            String orderId=request.getParameter("orderId");
            String createTimeStart=request.getParameter("createTimeStart");
            String createTimeEnd=request.getParameter("createTimeEnd");
            //用户id
            Long uid = WebUtil.getInstance().getUserId(request);
            UserDTO userDTO = userExportService.queryUserById(uid);
            Pager pager=new Pager();
            pager.setRows(new Integer(rows));
            pager.setPage(new Integer(page));
            pager.setTotalCount(0);
            ComplainDTO complainDTO=new ComplainDTO();
            if(userDTO.getParentId() == null){//主账号id
                complainDTO.setSellerId(uid);
            }else{//子账号id
                complainDTO.setSellerId(userDTO.getParentId());
            }
            if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
                complainDTO.setOrderId(orderId);
            }
            if(createTimeStart!=null&&!"".equals(createTimeStart)){
                complainDTO.setCreatedBegin(createTimeStart);
            }
            if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                complainDTO.setCreatedEnd(createTimeEnd);
            }
            if(complaintype!=null&&!"".equals(complaintype)){
                complainDTO.setComplainType(complaintype);
            }
            DataGrid<ComplainDTO> dataGrid=complainExportService.findInfoByCondition(complainDTO,pager);
            if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
                List<ComplainDTO> list=dataGrid.getRows();
                Map<String,List<Map<String,String>>> goodsDetail=goodsDetail(list);
                Iterator<ComplainDTO> iterator=list.iterator();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                while(iterator.hasNext()){
                    ComplainDTO comp=iterator.next();
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("orderId",comp.getOrderId());
                    map.put("shopName",comp.getShopName());
                    if(comp.getCreated()!=null){
                        map.put("created",simpleDateFormat.format(comp.getCreated()));
                    }
                    if(comp.getSellerId()!=null){
                        map.put("sellerId",String.valueOf(comp.getSellerId()));
                    }
                    if(comp.getRefundId()!=null){
                        map.put("thid",String.valueOf(comp.getRefundId()));
                    }
                    if(comp.getId()!=null){
                        map.put("complainid",String.valueOf(comp.getId()));
                    }
                    if(comp.getStatus()!=null&&comp.getStatus()==1){
                        map.put("staceName","退款已仲裁");
                        map.put("stace","1");
                    }else if(comp.getStatus()!=null&&comp.getStatus()==2){
                        map.put("staceName","已取消");
                        map.put("stace","2");
                    }else{
                        map.put("staceName","待客服仲裁");
                        map.put("stace","0");
                    }
                    if(comp.getComplainType()!=null&&"1".equals(comp.getComplainType())){
                        map.put("sellerOrBuyer","1");
                        map.put("tousufang","买家投诉");
                    }else if(comp.getComplainType()!=null&&"2".equals(comp.getComplainType())){
                        map.put("sellerOrBuyer","2");
                        map.put("tousufang","店铺投诉");
                    }else{
                        map.put("sellerOrBuyer","3");
                        map.put("tousufang","无法判定投诉方");
                    }
                    if(comp.getRefundId()!=null){
                        map.put("list",goodsDetail.get(String.valueOf(comp.getRefundId())));
                    }
                    listMap.add(map);
                }

                pager.setTotalCount(dataGrid.getTotal().intValue());
            }
            pager.setRecords(listMap);
            json.setSuccess(true);
            json.setMsg("查询成功");
            json.setObj(pager);
        }catch(Exception e){
            LOGGER.error(e.getMessage());
            LOGGER.error(e.toString());
            json.setSuccess(false);
            json.setMsg("查询失败");
        }
        return json;
    }
    private Map<String,List<Map<String,String>>> goodsDetail(List<ComplainDTO> list){
        Map<String,List<Map<String,String>>> detailMaps=new HashMap<String, List<Map<String, String>>>();
        Iterator<ComplainDTO> iterator=list.iterator();
        Map<Long,String> mapgoodid=new HashMap<Long, String>();
        while(iterator.hasNext()){
            ComplainDTO complainDTO=iterator.next();
            if(complainDTO.getReturnGoodsId()!=null){
                mapgoodid.put(complainDTO.getReturnGoodsId(),"");
            }
        }
        if(mapgoodid.size()>0){
            Long[] goodids=new Long[mapgoodid.size()];
            Iterator<Long> iterator1=mapgoodid.keySet().iterator();
            int i=0;
            while(iterator1.hasNext()){
                goodids[i]=iterator1.next();
                i++;
            }
            TradeReturnGoodsDetailDTO dto=new TradeReturnGoodsDetailDTO();
            dto.setReturnGoodsIds(goodids);
            ExecuteResult<List<TradeReturnGoodsDetailDTO>> executeResult=tradeReturnExportService.getTradeReturnGoodsDetaiByCondition(dto);
            if(executeResult!=null&&executeResult.getResult()!=null&&executeResult.getResult().size()>0){
                List<TradeReturnGoodsDetailDTO> list1=executeResult.getResult();
                Iterator<TradeReturnGoodsDetailDTO> iterator2=list1.iterator();
                while(iterator2.hasNext()){
                    TradeReturnGoodsDetailDTO tra=iterator2.next();
                    if(tra.getGoodsId()!=null){
                        String id=String.valueOf(tra.getReturnGoodsId());
                        List<Map<String,String>> listMaps=detailMaps.get(id);
                        if(listMaps!=null){
                            Map<String,String> mapdetail=new HashMap<String, String>();
                            //skuurl
                            mapdetail.put("skuUrl",tra.getGoodsPicUrl());
                            mapdetail.put("skuName",tra.getGoodsName());
                            if(tra.getSkuId()!=null){
                                mapdetail.put("skuId",String.valueOf(tra.getSkuId()));
                            }
                            if(tra.getGoodsId()!=null){
                                mapdetail.put("itemid",String.valueOf((tra.getGoodsId())));
                            }
                            if(tra.getPayPrice()!=null){
                                mapdetail.put("payPrice",String.valueOf(tra.getPayPrice()));
                            }
                            if(tra.getRerurnCount()!=null){
                                mapdetail.put("goodCount",String.valueOf(tra.getRerurnCount()));
                            }
                            if(tra.getReturnAmount()!=null){
                                mapdetail.put("amount",String.valueOf(tra.getReturnAmount()));
                            }
                            listMaps.add(mapdetail);
                        }else{
                            listMaps=new ArrayList<Map<String, String>>();
                            Map<String,String> mapdetail=new HashMap<String, String>();
                            //skuurl
                            mapdetail.put("skuUrl",tra.getGoodsPicUrl());
                            mapdetail.put("skuName",tra.getGoodsName());
                            if(tra.getSkuId()!=null){
                                mapdetail.put("skuId",String.valueOf(tra.getSkuId()));
                            }
                            if(tra.getGoodsId()!=null){
                                mapdetail.put("itemid",String.valueOf((tra.getGoodsId())));
                            }
                            if(tra.getPayPrice()!=null){
                                mapdetail.put("payPrice",String.valueOf(tra.getPayPrice()));
                            }
                            if(tra.getRerurnCount()!=null){
                                mapdetail.put("goodCount",String.valueOf(tra.getRerurnCount()));
                            }
                            if(tra.getReturnAmount()!=null){
                                mapdetail.put("amount",String.valueOf(tra.getReturnAmount()));
                            }
                            listMaps.add(mapdetail);
                            detailMaps.put(id,listMaps);
                        }
                    }
                }
            }
        }
        return detailMaps;
    }
    @RequestMapping(value = "complaincancle")
    @ResponseBody
    public Json complainCancle(HttpServletRequest request){
        Json json=new Json();
        //仲裁id
        String id=request.getParameter("id");
        try{
        	ComplainDTO complainDTO=new ComplainDTO();
        	complainDTO.setRefundId(Long.parseLong(id));
        	complainDTO.setStatus(0);
        	
            DataGrid<ComplainDTO> executeResult=complainExportService.findInfoByCondition(complainDTO, null);
            if(executeResult.getTotal()>0){
            	for (ComplainDTO comp : executeResult.getRows()) {
            		//取消仲裁
            		comp.setStatus(new Integer(2));
            		comp.setModified(new Date());
            		comp.setResolutionTime(new Date());
            		ExecuteResult<String> executeResul= complainExportService.modifyComplainInfo(comp);
				}
                json.setMsg("取消成功");
                json.setSuccess(true);
            }else{
                json.setMsg("取消失败");
                json.setSuccess(false);
                LOGGER.error("取消失败了，没有查询到投诉信息");
            }
        }catch(Exception e){
            json.setMsg("取消失败，请联系管理员");
            json.setSuccess(false);
            LOGGER.error(e.getMessage());
            LOGGER.error(e.toString());
        }
        return json;
    }

    private Map<Integer,String> tkstaceMap(){
        //1 退款申请等待卖家确认中  2 卖家不同意协议,等待买家修改 3 退款申请达成,等待买家发货   4 买家已退货,等待卖家确认收货  5 退款关闭  6 退款成功
        Map<Integer,String> staceMap=new HashMap<Integer, String>();
        staceMap.put(1,"退款申请等待卖家确认中");
        staceMap.put(2,"卖家不同意协议,等待买家修改");
        staceMap.put(3,"退款申请达成,等待买家发货");
        staceMap.put(4,"买家已退货,等待卖家确认收货");
        staceMap.put(5,"退款关闭");
        staceMap.put(6,"退款成功");
        staceMap.put(7,"退款中");
        staceMap.put(8,"待平台处理退款");
        staceMap.put(9,"平台处理退款中");
        return staceMap;
    }
    private Map<Integer,String> orderstaceMap(){
        //1:待付款，2：待配送，3：待收货，4：待评价，5：已完成
        Map<Integer,String> staceMap=new HashMap<Integer, String>();
        staceMap.put(1,"待付款");
        staceMap.put(2,"待配送");
        staceMap.put(3,"待收货");
        staceMap.put(4,"待评价");
        staceMap.put(5,"已完成");
        return staceMap;
    }
    
    public ExecuteResult<String> updateComplainGroup(Long refundId,Long complainId,String complainType){
    	//若仲裁添加成功，判断是提交投诉还是申请辩解
    	ComplainDTO queryComplainDTO=new ComplainDTO();
    	if("1".equals(complainType)){
    		queryComplainDTO.setComplainType("2");
    	}else if("2".equals(complainType)){
    		queryComplainDTO.setComplainType("1");
    	}
    	queryComplainDTO.setStatus(0);
    	queryComplainDTO.setRefundId(refundId);
    	List<ComplainDTO> list = complainExportService.getComplainByCondition(queryComplainDTO);
    	//updateComplainDTO更新仲裁分组，若提交投诉，则complainGroup=id，  若申请辩解 ，则complainGroup=投诉的记录的id
    	ComplainDTO updateComplainDTO=new ComplainDTO();
    	updateComplainDTO.setId(complainId);
    	if(list!=null && list.size()==1){
    		updateComplainDTO.setComplainGroup(list.get(0).getId());
    	}else{
    		updateComplainDTO.setComplainGroup(complainId);
    	}
    	ExecuteResult<String> modifyComplainInfo = complainExportService.modifyComplainInfo(updateComplainDTO);
    	return modifyComplainInfo;
    }
 

}