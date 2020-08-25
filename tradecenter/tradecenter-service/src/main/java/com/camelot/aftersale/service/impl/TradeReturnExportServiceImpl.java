package com.camelot.aftersale.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.activity.dto.ActivityStatementsDTO;
import com.camelot.activity.service.ActivityStatementSerice;
import com.camelot.aftersale.dao.TradeReturnGoodsMybatisDAO;
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
import com.camelot.aftersale.service.TradeRefundService;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.aftersale.service.TradeReturnGoodsDetailService;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.aftersale.service.TradeReturnPicService;
import com.camelot.common.enums.ActivityTypeEnum;
import com.camelot.common.enums.BankSettleTypeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.maketcenter.dto.emums.IntegralTypeEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.AlipayErrorCodeProperties;
import com.camelot.payment.dao.RefundOrderDAO;
import com.camelot.payment.dao.RefundTransationsDAO;
import com.camelot.payment.domain.RefundOrder;
import com.camelot.payment.domain.RefundTransations;
import com.camelot.payment.service.alipay.util.AlipayNotify;
import com.camelot.payment.service.unionpay.util.SDKUtil;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.tradecenter.service.TradeOrderItemsDiscountExportService;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;

@Service("tradeReturnExportService")
public class TradeReturnExportServiceImpl implements TradeReturnExportService {
    private final static Logger logger = LoggerFactory.getLogger(TradeReturnExportService.class);
    @Resource
    private TradeReturnGoodsService tradeReturnGoodsService;
    @Resource
    private TradeReturnGoodsDetailService tradeReturnGoodsDetailService;
    @Resource
    private TradeRefundService tradeRefundService;
    @Resource
    private TradeOrderExportService tradeOrderExportService;
    @Resource
    private TradeReturnPicService tradeReturnPicService;
    @Resource
	private ComplainExportService complainExportService;
    @Resource
	private RefundOrderDAO refundOrderDAO;
    @Resource
    private StatementExportService statementExportService;
    @Resource
    private TradeReturnGoodsMybatisDAO tradeReturnGoods;
    @Resource
    private RefundTransationsDAO refundTransationsDAO;
    @Resource
    private RefundTransationsService refundTransationsService;
    @Resource
    private UserIntegralTrajectoryExportService userIntegralTrajectoryService;
    @Resource
    private ActivityStatementSerice activityStatementSerice;
    @Resource
	private TradeOrderItemsDiscountExportService  tradeOrderItemsDiscountExportService;
    
    @Override
    public ExecuteResult<TradeReturnGoodsDTO> createTradeReturn(TradeReturnInfoDto insertDto, TradeReturnStatusEnum status) {
        logger.info("==============创建退货单开始");
        ExecuteResult<TradeReturnGoodsDTO> res = new ExecuteResult<TradeReturnGoodsDTO>();
        try {
            if (insertDto != null && insertDto.getTradeReturnDto() != null && status != null) {
                insertDto.getTradeReturnDto().setState(status.getCode());
                insertDto.getTradeReturnDto().setAfterService(2);//售后服务是
                ExecuteResult<TradeReturnGoodsDTO> returnGoods = tradeReturnGoodsService.createTradeReturnGoodsDTO(insertDto.getTradeReturnDto());
                if (returnGoods != null) {
                	res.setResult(returnGoods.getResult());
//                    TradeReturnGoodsDTO queryReturn = new TradeReturnGoodsDTO();
//                    queryReturn.setCodeNo(returnGoods.getResult().getCodeNo());
//                    List<TradeReturnGoodsDTO> tradeDtoResList = tradeReturnGoodsService.searchByCondition(queryReturn);
//                    if (tradeDtoResList != null && tradeDtoResList.size() > 0) {
                        TradeReturnGoodsDTO tradeDtoRes = returnGoods.getResult();
                        //1创建退货原因图片
                        if (insertDto.getTradeReturnDto().getPicDTOList() != null && insertDto.getTradeReturnDto().getPicDTOList().size() > 0) {
                            for (TradeReturnPicDTO item : insertDto.getTradeReturnDto().getPicDTOList()) {
                                item.setReturnGoodsId(tradeDtoRes.getId());
                                tradeReturnPicService.createTradeReturnPicDTO(item);
                            }

                        }
                        //2 退货单成功创建退款单
                        tradeDtoRes.setState(status.getCode());
                        Long itemId = insertDto.getTradeReturnGoodsDetailList().get(0).getGoodsId();
                        Long skuId = insertDto.getTradeReturnGoodsDetailList().get(0).getSkuId();
                        createRefund(tradeDtoRes,itemId,skuId);
                        //3 更新订单退货记录
                        TradeOrdersDTO orderDTO = new TradeOrdersDTO();
                        orderDTO.setOrderId(insertDto.getTradeReturnDto().getOrderId());
                        orderDTO.setRefund(2); //已退货
                        orderDTO.setRefundTime(new Date());
                       // orderDTO.setAfterService(2);
                        tradeOrderExportService.updateTradeOrdersDTOSelective(orderDTO);

                        //创建订单列表
                        if (tradeDtoRes != null && tradeDtoRes.getId() != null) {
                            if (insertDto.getTradeReturnGoodsDetailList() != null && insertDto.getTradeReturnGoodsDetailList().size() > 0) {
                                for (TradeReturnGoodsDetailDTO item : insertDto.getTradeReturnGoodsDetailList()) {
                                    try {
                                        item.setReturnGoodsId(tradeDtoRes.getId());
                                        tradeReturnGoodsDetailService.createTradeReturnGoodsDetailDTO(item);
                                    } catch (Exception e) {
                                        logger.info("==============创建退货单Exception", e);
                                    }
                                }
                            }
                        }
//                    }
                }
            }
        } catch (Exception e) {
            logger.info("==============创建退货单Exception", e);
            throw new RuntimeException("==============创建退货单Exception" + e);
        }
        logger.info("==============创建退货单完成");
        return res;
    }

    private void createRefund(TradeReturnGoodsDTO tradeDtoRes,Long itemId , Long skuId) {
        RefundPayParam refundPayParam = new RefundPayParam();
        refundPayParam.setOrderNo(tradeDtoRes.getOrderId());
//        refundPayParam.setStatus(tradeDtoRes.getState());
        refundPayParam.setBuyerId(Long.parseLong(tradeDtoRes.getBuyId()));
        refundPayParam.setReproNo(tradeDtoRes.getCodeNo());
        refundPayParam.setBuyerName(tradeDtoRes.getBuyerName());
        refundPayParam.setRefundAmount(tradeDtoRes.getRefundGoods());
        ExecuteResult<TradeOrdersDTO> orderRes = tradeOrderExportService.getOrderById(tradeDtoRes.getOrderId());
        if (orderRes != null && orderRes.getResult() != null) {
            TradeOrdersDTO orderItem = orderRes.getResult();
            refundPayParam.setOrderPayBank(orderItem.getPaymentType());
            //查询父订单的订单金额，此处获取订单的paymentPrice字段，为买家实际支付金额，不包含积分替换金额
            String parentOrderId = orderRes.getResult().getParentOrderId();
            if(!parentOrderId.equals("0")){
            	ExecuteResult<TradeOrdersDTO> parentOrder = tradeOrderExportService.getOrderById(parentOrderId);
            	if(parentOrder != null && parentOrder.getResult() != null){
            		refundPayParam.setOrderPrice(parentOrder.getResult().getPaymentPrice());
            	}
            }else{
            	refundPayParam.setOrderPrice(orderItem.getPaymentPrice());
            }
            
        }
		refundPayParam.setItemId(itemId);//商品id
		refundPayParam.setSkuId(skuId);//skuid
		
        //非必传
        refundPayParam.setBuyerPhone(tradeDtoRes.getBuyerPhone());
        refundPayParam.setOrderLabel(tradeDtoRes.getOrderName());
        refundPayParam.setRefundReason(tradeDtoRes.getReturnResult());

        ExecuteResult<RefundPayParam> payRes=  tradeRefundService.refundApply(refundPayParam);
        if(payRes!=null&&payRes.getResult()!=null){
            if(!payRes.isSuccess()){
                throw new RuntimeException("创建退款单失败");
            }
        }
    }

    @Override
    public ExecuteResult<TradeReturnGoodsDTO> updateTradeReturnStatus(TradeReturnGoodsDTO dto, TradeReturnStatusEnum status) {
        ExecuteResult<TradeReturnGoodsDTO> res = new ExecuteResult<TradeReturnGoodsDTO>();
        String errorMsg="";
        logger.info("==============更新退货单状态开始");
        if (dto != null && dto.getId() != null && status != null) {
            try {

                TradeReturnGoodsDTO beforeDto=  tradeReturnGoodsService.getTradeReturnGoodsDTOById(dto.getId());

                dto.setState(status.getCode());
                ExecuteResult<TradeReturnGoodsDTO> returnGood = tradeReturnGoodsService.updateSelective(dto);
                if (returnGood!=null&&returnGood.getResult()!=null) {

                    //1 处理图片
                    updateReturnGoodsPic(dto);

                    TradeReturnGoodsDTO resGood = returnGood.getResult();
                    List<TradeReturnGoodsDetailDTO> details=this.getTradeReturnGoodsDetailReturnId(String.valueOf(resGood.getId()));
                    //2 更新退款单状态
//                    ExecuteResult<RefundPayParam> paymentRes = paymentExportService.findRefInfoByReturnGoodsCode(resGood.getCodeNo());
                    if(status.getCode()==TradeReturnStatusEnum.PLATFORMTOREFUND.getCode() || (status.getCode()==TradeReturnStatusEnum.REFUNDING.getCode()&&((PayBankEnum.CITIC.getQrCode()+"").equals(beforeDto.getOrderPayBank())||(PayBankEnum.INTEGRAL.getQrCode()+"").equals(beforeDto.getOrderPayBank())))) {

                    	ExecuteResult<TradeOrdersDTO> orderRes = tradeOrderExportService.getOrderById(resGood.getOrderId());
                    	logger.info("orderRes：{}", JSON.toJSONString(orderRes));
                        if (orderRes != null && orderRes.getResult() != null) {
                        
                            TradeOrdersDTO orderItem = orderRes.getResult();
                            //如果是已经支付 再退款 并且订单状态 是待评价或者 已完成
                            if(orderItem.getPaid()==2&&orderItem.getLocked()!=2&&orderItem.getPaymentType()!=3){
                                RefundPayParam refundPayParam = new RefundPayParam();
                                if(details!=null&&details.size()>0){
                                	refundPayParam.setItemId(details.get(0).getGoodsId());//商品id
                                }
                                refundPayParam.setReproNo(resGood.getCodeNo());
                                refundPayParam.setOrderNo(orderItem.getOrderId());
                                ExecuteResult<String> paryRes = new ExecuteResult<String>();
                                if(orderItem.getPaymentMethod() == PayMethodEnum.PAY_INTEGRAL.getCode()){
                                	//给买家返回积分
                                	UserIntegralTrajectoryDTO userIntegralTrajectoryDTO = new UserIntegralTrajectoryDTO();
                                	userIntegralTrajectoryDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_REFUND.getCode());
                                	userIntegralTrajectoryDTO.setOrderId(orderItem.getOrderId());
                                	userIntegralTrajectoryDTO.setIntegralValue(beforeDto.getRefundGoods().longValue());
                                	userIntegralTrajectoryDTO.setUsingTime(new Date());
                                	userIntegralTrajectoryDTO.setUserId(orderItem.getBuyerId());
                                	userIntegralTrajectoryDTO.setShopId(orderItem.getShopId());
                                	userIntegralTrajectoryDTO.setInsertBy(orderItem.getBuyerId());
                                	userIntegralTrajectoryDTO.setInsertTime(new Date());
                                	ExecuteResult<UserIntegralTrajectoryDTO> result = userIntegralTrajectoryService.addUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO);
                                	if(!result.isSuccess()){
                                		paryRes.setErrorMessages(result.getErrorMessages());
                                	}
                                }
                                //修改结算单数据
                                else{
                                	paryRes= tradeRefundService.refundDeal(refundPayParam,orderItem.getState()>=4?true:false,false,false);
                                	logger.info("paryRes：{}", JSON.toJSONString(paryRes));
                                }
                              //注释掉资金流转直接返回
//                                else{
//                                	paryRes= tradeRefundService.refundDeal(refundPayParam,orderItem.getState()>=4?true:false,true,true);
//                                	logger.info("paryRes：{}", JSON.toJSONString(paryRes));
//                                }
                                if(!paryRes.isSuccess()){
                                    errorMsg=paryRes.getErrorMessages().get(0);
                                    res.addErrorMessage(errorMsg);
                                    dto.setState(beforeDto.getState());
                                    dto.setConfirmStatus("4");
                                    tradeReturnGoodsService.updateSelective(dto);
                                    res.setErrorMessages(paryRes.getErrorMessages());
                                    return res;
                                }else {
                                	//如果订单是已确认收获状态，则不做处理
                                	if(orderItem.getState() <= 3){
		                            	//判断所有商品是否都已经退货/退款
		                            	Integer[] states = {TradeReturnStatusEnum.SUCCESS.getCode(),TradeReturnStatusEnum.REFUNDING.getCode(),TradeReturnStatusEnum.PLATFORMTOREFUND.getCode(),TradeReturnStatusEnum.PLATFORMDEALING.getCode(),};
		                    			boolean flag=isAllItemReturn(orderItem,states);
		                    			if(flag){
		                    				TradeOrdersDTO orderDTO = new TradeOrdersDTO();
		                                	orderDTO.setState(7); //订单已关闭
		                                	orderDTO.setRefundTime(new Date()); //退货时间
		                                    orderDTO.setOrderId(resGood.getOrderId());
		                                    tradeOrderExportService.updateTradeOrdersDTOSelective(orderDTO);//更新订单状态
		                    				//判断所有的支付的金额都已经退款
		                    				List<RefundOrder> refundOrderList = refundOrderDAO.selectRefundOrderByOrderIdAndRefundGoodsStatus(orderItem.getOrderId(), states);//.searchByOrderIdAndState(tradeOrder.getOrderId(),states);
		                    				BigDecimal totalRefundAmount = new BigDecimal(0.00);
		                    				for(RefundOrder refundOrder : refundOrderList){
		                    					totalRefundAmount = totalRefundAmount.add(refundOrder.getRefundAmount());
		                    				}
		                    				//如果没有全部退完，则生成结算单，将剩余的资金结算给卖家
		                    				if(!orderItem.getPaymentMethod().equals(PayMethodEnum.PAY_INTEGRAL.getCode()) && totalRefundAmount.compareTo(orderItem.getPaymentPrice()==null?new BigDecimal("0"):orderItem.getPaymentPrice()) == -1){
		                    					//生成结算单
		                    					ExecuteResult<String> createSettleResult = statementExportService.createSettleDetail(orderItem,refundOrderList);
		                    					res.setErrorMessages(createSettleResult.getErrorMessages());
		                    				}
		                    			}
                                	}
                                }
                            }else{
                                errorMsg="订单未支付或订单被锁定 退款失败";
                                res.addErrorMessage(errorMsg);
                            }
                        }

                    }
                    if(status.getCode().equals(TradeReturnStatusEnum.AUTH.getCode())){
                    	RefundOrder refundOrder=refundOrderDAO.selectRefundByRePro(beforeDto.getCodeNo());//根据退货单号查询退款单记录
                    	if(null!=refundOrder){
                    		refundOrder.setRefundAmount(dto.getRefundGoods());//退款金额
                    		refundOrder.setRefundReason(dto.getReturnResult());//退款原因
                    		refundOrderDAO.update(refundOrder);
                    	}
                    }
                    //如果 退款成功
                    if (status.getCode().equals(TradeReturnStatusEnum.SUCCESS.getCode())) {  //  退款成功 更新退货 退款单 状态
                    	Integer[] states = {TradeReturnStatusEnum.SUCCESS.getCode(),TradeReturnStatusEnum.REFUNDING.getCode(),TradeReturnStatusEnum.PLATFORMTOREFUND.getCode(),TradeReturnStatusEnum.PLATFORMDEALING.getCode(),};
                    	boolean tab=false;
                    	boolean flag=updateOrderStatus(resGood,states);
                    	// 根据单据编号查询退款记录
                        TradeOrdersDTO orderDTO = new TradeOrdersDTO();
                        if(flag){
                    		orderDTO.setState(7); //订单已关闭
                    		orderDTO.setRefundTime(new Date()); //退货时间
                    	}
                        orderDTO.setOrderId(resGood.getOrderId());
                        //orderDTO.setAfterService(3);
                        tradeOrderExportService.updateTradeOrdersDTOSelective(orderDTO);
                        ExecuteResult<TradeOrdersDTO> tradeOrders=tradeOrderExportService.getOrderById(resGood.getOrderId());
                        List<TradeOrderItemsDTO> items=tradeOrders.getResult().getItems();//该订单下的所有商品 
                      //查询退款单id
                		TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
                		TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
                		tradeReturnDto.setOrderId(resGood.getOrderId().toString());
                		queryDto.setTradeReturnDto(tradeReturnDto);
                		DataGrid<TradeReturnGoodsDTO> dg = this.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
                        for(TradeOrderItemsDTO item:items){
                        	int sum=0;
                        	for (TradeReturnGoodsDTO trg : dg.getRows()) {
                        		List<Integer> list=new ArrayList<Integer>();
                				int num=0;
                				List<TradeReturnGoodsDetailDTO> deDto=this.getTradeReturnGoodsDetailReturnId(String.valueOf(trg.getId()));
                				if(item.getSkuId().equals(details.get(0).getSkuId())&&
                						deDto.get(0).getSkuId().equals(details.get(0).getSkuId())){
                                     if(new Integer(5).equals(trg.getAfterService())&&"1".equals(trg.getDeletedFlag())){//部分商品已退款完成并且标记状态为已删除即已经申请过的退款商品数量
                                    	 num=deDto.get(0).getRerurnCount();
                                     }else if("0".equals(trg.getDeletedFlag())){//当前正在操作的状态
                                    	 num=details.get(0).getRerurnCount();
                                     }
                                     list.add(num);
                			    }
                				for(int i=0;i<list.size();i++){
                        			int j=(Integer) list.get(i);
                        			sum+=j;
                        		}
                            	if(item.getNum()==sum){
                            		tab=true;
                            	}
                		  }
                        	
                        }
                        if(tab){
                        	dto.setAfterService(3);//已完成
                        }else{
                        	dto.setAfterService(5);//部分商品已退款
                        }
                       tradeReturnGoodsService.updateSelective(dto);
                    }else if(status.getCode().equals(TradeReturnStatusEnum.CLOSE.getCode())){ //如果是退货关闭
                    	
                    	dto.setAfterService(4);//售后已关闭
                    	ExecuteResult<TradeReturnGoodsDTO> goodsDto = tradeReturnGoodsService.updateSelective(dto);
                        TradeOrdersDTO orderDTO = new TradeOrdersDTO();
                        orderDTO.setOrderId(resGood.getOrderId());
                       // orderDTO.setAfterService(1); //售后改为1 无
                        orderDTO.setRefund(1); //是否退款改为否
                        tradeOrderExportService.updateTradeOrdersDTOSelective(orderDTO);
                        //更新 申请的仲裁 状态为取消
                        ComplainDTO complainDTO=new ComplainDTO();
                        complainDTO.setRefundId(resGood.getId());
                        complainDTO.setStatus(0);
                        DataGrid<ComplainDTO> complainData = complainExportService.findInfoByCondition(complainDTO,null);
                        for (ComplainDTO comp:complainData.getRows()) {
                        	comp.setStatus(2);
                        	complainExportService.modifyComplainInfo(comp);
						}
                    }


                    res.setResult(returnGood.getResult());
                }else{
                    errorMsg="退货退款失败 退货单ID不能为空";
                    res.addErrorMessage(errorMsg);
                    throw new RuntimeException("退货退款失败 退货单ID,订单ID 不能为空");
                }
            } catch (Exception e) {
                logger.info("==============更新退货单Exception", e);
                throw new RuntimeException("退货退款失败"+e.getMessage());

            }
        } else {
            logger.info("==============更新退货单失败退货单ID或者退货单状态为空");
            res.addErrorMessage("退货单ID或者退货单状态为空");
        }
        logger.info("==============更新退货单状态完成");
        return res;
    }

    private void updateReturnGoodsPic(TradeReturnGoodsDTO dto) {
        //更新的时候 先把退货单图片删掉再重新上传
        if (dto.getPicDTOList() != null && dto.getPicDTOList().size() > 0) {
            tradeReturnPicService.defunctByReturnGoodsId(dto.getId());
            for (TradeReturnPicDTO item : dto.getPicDTOList()) {
                item.setReturnGoodsId(dto.getId());
                tradeReturnPicService.createTradeReturnPicDTO(item);
            }
        }
    }

    @Override
    public DataGrid<TradeReturnGoodsDTO> getTradeReturnInfoDto(Pager<TradeReturnGoodsDTO> pager, TradeReturnInfoQueryDto queryDto) {
        DataGrid<TradeReturnGoodsDTO> res = new DataGrid<TradeReturnGoodsDTO>();
        if (queryDto.getTradeReturnDto() != null) {
            DataGrid<TradeReturnGoodsDTO> goodRes = tradeReturnGoodsService.searchTradeReturnGoodsDTOs(pager, queryDto.getTradeReturnDto());
            if (goodRes != null) {
                return goodRes;
            }
        }
        return res;
    }

    @Override
    public List<TradeReturnGoodsDetailDTO> getTradeReturnGoodsDetailReturnId(String returnGoodsId) {
        List<TradeReturnGoodsDetailDTO> res = new ArrayList<TradeReturnGoodsDetailDTO>();
        try {
            if (StringUtils.isNotEmpty(returnGoodsId)) {
                TradeReturnGoodsDetailDTO tradeReturnGoodsDetail = new TradeReturnGoodsDetailDTO();
                tradeReturnGoodsDetail.setReturnGoodsId(Long.parseLong(returnGoodsId));
                res = tradeReturnGoodsDetailService.searchByCondition(tradeReturnGoodsDetail);
            }
        } catch (Exception e) {
            logger.info("==============查询退货单商品Exception", e);
        }

        return res;
    }

    @Override
    public ExecuteResult<TradeReturnInfoDto> getTradeReturnInfoByReturnGoodsId(String returnGoodId) {
        ExecuteResult<TradeReturnInfoDto> res = new ExecuteResult<TradeReturnInfoDto>();
        TradeReturnInfoDto infoDto = new TradeReturnInfoDto();
        TradeReturnGoodsDTO tradeReturn = tradeReturnGoodsService.getTradeReturnGoodsDTOById(Long.parseLong(returnGoodId));
        //查询退货原因图片地址
        res.setResult(infoDto);
        if (tradeReturn != null) {
            TradeReturnPicDTO tradeReturnPicDTO = new TradeReturnPicDTO();
            tradeReturnPicDTO.setReturnGoodsId(Long.parseLong(returnGoodId));
            List<TradeReturnPicDTO> detailList = tradeReturnPicService.searchByCondition(tradeReturnPicDTO);
            tradeReturn.setPicDTOList(detailList);

        }

        infoDto.setTradeReturnDto(tradeReturn);
        //查询商品明细
        if (tradeReturn != null) {
            TradeReturnGoodsDetailDTO tradeReturnGoodsDetail = new TradeReturnGoodsDetailDTO();
            tradeReturnGoodsDetail.setReturnGoodsId(Long.parseLong(returnGoodId));
            List<TradeReturnGoodsDetailDTO> detailList = tradeReturnGoodsDetailService.searchByCondition(tradeReturnGoodsDetail);
            infoDto.setTradeReturnGoodsDetailList(detailList);

        }

        return res;
    }

    @Override
    public ExecuteResult<List<TradeReturnGoodsDetailDTO>> getTradeReturnGoodsDetaiByCondition(TradeReturnGoodsDetailDTO dto) {
        ExecuteResult<List<TradeReturnGoodsDetailDTO>> res = new ExecuteResult<List<TradeReturnGoodsDetailDTO>>();
        List<TradeReturnGoodsDetailDTO> resList = tradeReturnGoodsDetailService.searchByCondition(dto);
        res.setResult(resList);
        return res;
    }

    @Override
    public ExecuteResult<TradeReturnPicDTO> updateTradeReturnPicDTO(TradeReturnPicDTO dto) {
        ExecuteResult<TradeReturnPicDTO> res = new ExecuteResult<TradeReturnPicDTO>();
        if (dto != null && dto.getId() != null) {

            tradeReturnPicService.updateSelective(dto);
        }
        return res;
    }

	@Override
	public ExecuteResult<TradeReturnGoodsDTO> updateTradeReturnGoods(TradeReturnGoodsDTO dto) {
		ExecuteResult<TradeReturnGoodsDTO> res = new ExecuteResult<TradeReturnGoodsDTO>();
		if(dto!=null&&dto.getId()!=null){
		  res=tradeReturnGoodsService.updateTradeReturnGoodsDTO(dto);
		}
		return res;
	}
    private boolean updateOrderStatus(TradeReturnGoodsDTO goodsDto,Integer... state){
    	int returnNum = 0;
    	ExecuteResult<TradeOrdersDTO> tradeOrders=tradeOrderExportService.getOrderById(goodsDto.getOrderId());
    	List<TradeOrderItemsDTO> items=tradeOrders.getResult().getItems();//该订单下的所有商品 
    	
    	List<TradeReturnGoodsDTO> tradeReturnGoodsList = tradeReturnGoodsService.searchByOrderIdAndState(goodsDto.getOrderId(),state);
		for(TradeReturnGoodsDTO tradeReturnGoodsDTO :tradeReturnGoodsList){
			if("0".equals(tradeReturnGoodsDTO.getDeletedFlag())||("1".equals(tradeReturnGoodsDTO.getDeletedFlag())&&5==tradeReturnGoodsDTO.getAfterService().intValue())){
				Long returnGoodsId = tradeReturnGoodsDTO.getId();
	    		TradeReturnGoodsDetailDTO tradeReturnGoodsDetail = new TradeReturnGoodsDetailDTO();
	    		tradeReturnGoodsDetail.setReturnGoodsId(returnGoodsId);
	    		List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailDTOList = tradeReturnGoodsDetailService.searchByCondition(tradeReturnGoodsDetail);
	    		for(TradeReturnGoodsDetailDTO tradeReturnGoodsDetailDTO : tradeReturnGoodsDetailDTOList){
	    			returnNum = returnNum + tradeReturnGoodsDetailDTO.getRerurnCount();
	    		}
	    	}
	    	//获取订单下面的所有商品个数
	    	int orderItemNum = 0;
	    	for(TradeOrderItemsDTO tradeOrderItemsDTO : items){
	    		orderItemNum = orderItemNum + tradeOrderItemsDTO.getNum();
	    	}
	    	if(returnNum == orderItemNum){
	    		return true;
	    	 }
			}
		return false;
   }
    /**
     * 根据订单ID判断该订单下的商品是否已经全部退款
     * 
     * @param orderItem 订单
     * @return
     */
    public boolean isAllItemReturn(TradeOrdersDTO orderItem,Integer... state){
    	
    	int returnNum = 0;
    	
    	List<TradeReturnGoodsDTO> tradeReturnGoodsList = tradeReturnGoodsService.searchByOrderIdAndState(orderItem.getOrderId(),state);
    	
    	for(TradeReturnGoodsDTO tradeReturnGoodsDTO : tradeReturnGoodsList){
    		Long returnGoodsId = tradeReturnGoodsDTO.getId();
    		TradeReturnGoodsDetailDTO tradeReturnGoodsDetail = new TradeReturnGoodsDetailDTO();
    		tradeReturnGoodsDetail.setReturnGoodsId(returnGoodsId);
    		List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailDTOList = tradeReturnGoodsDetailService.searchByCondition(tradeReturnGoodsDetail);
    		for(TradeReturnGoodsDetailDTO tradeReturnGoodsDetailDTO : tradeReturnGoodsDetailDTOList){
    			returnNum = returnNum + tradeReturnGoodsDetailDTO.getRerurnCount();
    		}
    	}
    	//获取订单下面的所有商品个数
    	int orderItemNum = 0;
    	List<TradeOrderItemsDTO> orderItemDTOList = orderItem.getItems();
    	for(TradeOrderItemsDTO tradeOrderItemsDTO : orderItemDTOList){
    		orderItemNum = orderItemNum + tradeOrderItemsDTO.getNum();
    	}
    	if(returnNum == orderItemNum){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 
     * <p>Discription:[根据订单ID查询该订单下商品的退款数量、积分退还数量、金额退还数量]</p>
     * Created on 2015年12月30日
     * @param orderItem
     * @param state
     * @return key：itemReturnNum：商品的退款数量；integralReturnNum：积分退还数量；returnAmount：金额退还数量
     * @author:[宋文斌]
     */
    @Override
	public ExecuteResult<Map<String,Object>> getReturnNum(TradeOrdersDTO orderItem, Integer... state) {
		logger.info("\n 方法[{}]，入参：[{}][{}]", "TradeReturnExportServiceImpl-getItemReturnNum", orderItem, state);
		ExecuteResult<Map<String,Object>> result = new ExecuteResult<Map<String,Object>>();
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			Integer itemReturnNum = 0;
			Integer integralReturnNum = 0;
			BigDecimal returnAmount = BigDecimal.ZERO;

			List<TradeReturnGoodsDTO> tradeReturnGoodsList = tradeReturnGoodsService.searchByOrderIdAndState(
					orderItem.getOrderId(), state);

			if(tradeReturnGoodsList != null && tradeReturnGoodsList.size() > 0){
				for (TradeReturnGoodsDTO tradeReturnGoodsDTO : tradeReturnGoodsList) {
					Long returnGoodsId = tradeReturnGoodsDTO.getId();
					TradeReturnGoodsDetailDTO tradeReturnGoodsDetail = new TradeReturnGoodsDetailDTO();
					tradeReturnGoodsDetail.setReturnGoodsId(returnGoodsId);
					List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailDTOList = tradeReturnGoodsDetailService
							.searchByCondition(tradeReturnGoodsDetail);
					for (TradeReturnGoodsDetailDTO tradeReturnGoodsDetailDTO : tradeReturnGoodsDetailDTOList) {
						if(tradeReturnGoodsDetailDTO.getRerurnCount() != null){
							itemReturnNum = itemReturnNum + tradeReturnGoodsDetailDTO.getRerurnCount();
						}
						if(tradeReturnGoodsDetailDTO.getReturnIntegral() != null){
							integralReturnNum = integralReturnNum + tradeReturnGoodsDetailDTO.getReturnIntegral();
						}
						if(tradeReturnGoodsDetailDTO.getReturnAmount() != null){
							returnAmount = returnAmount.add(tradeReturnGoodsDetailDTO.getReturnAmount());
						}
					}
				}
			}
			map.put("itemReturnNum", itemReturnNum);
			map.put("integralReturnNum", integralReturnNum);
			map.put("returnAmount", returnAmount);
			result.setResult(map);
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.info("\n 方法[{}]，异常：[{}]", "TradeReturnExportServiceImpl-getItemReturnNum", e);
		}
		logger.info("\n 方法[{}]，出参：[{}]", "TradeReturnExportServiceImpl-getItemReturnNum", JSONObject.toJSONString(result));
		return result;
	}
    
    /**
     * 
     * <p>Discription:[获取订单下面的所有商品个数]</p>
     * Created on 2015年12月30日
     * @param orderItem
     * @return
     * @author:[宋文斌]
     */
	@Override
	public ExecuteResult<Integer> getOrderItemNum(TradeOrdersDTO orderItem) {
		logger.info("\n 方法[{}]，入参：[{}]", "TradeReturnExportServiceImpl-getOrderItemNum", orderItem);
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		try {
			Integer orderItemNum = 0;
			List<TradeOrderItemsDTO> orderItemDTOList = orderItem.getItems();
			for (TradeOrderItemsDTO tradeOrderItemsDTO : orderItemDTOList) {
				orderItemNum = orderItemNum + tradeOrderItemsDTO.getNum();
			}
			result.setResult(orderItemNum);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.info("\n 方法[{}]，异常：[{}]", "TradeReturnExportServiceImpl-getOrderItemNum", e);
		}
		logger.info("\n 方法[{}]，出参：[{}]", "TradeReturnExportServiceImpl-getOrderItemNum", JSONObject.toJSONString(result));
		return result;
	}
    
	@Override
	public ExecuteResult<RefundPayParam> findRefInfoByRepro(String repro) {
		ExecuteResult<RefundPayParam> res = new ExecuteResult<RefundPayParam>();
		if (repro != null ) {
			res=tradeRefundService.selectRefundByRePro(repro);
		}
		return res;
	}

	
	@Override
	public ExecuteResult<Integer> refundResult(Map<String,String> params,String payBank) {
		logger.info("\n 方法[{}]，入参：[{}]","TradeReturnExportServiceImpl-refundResult",params);
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		/*//TODO 解析退款xml
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = XMLUtil.doXMLParse(xml);
		} catch (JDOMException e) {
			er.addErrorMessage("解析xml失败，失败原因："+e.getMessage());
			logger.info("解析xml失败，失败原因："+e.getMessage());
		} catch (IOException e) {
			er.addErrorMessage("解析xml失败，失败原因："+e.getMessage());
			logger.info("解析xml失败，失败原因："+e.getMessage());
		}*/
		
		if("AP".equals(payBank)){
			if(AlipayNotify.verify(params)){//验证成功
				String refundNo="";
				logger.info("wwwwwwww++++++++++++++++"+AlipayNotify.verify(params));
				try {
					// 截取掉对外交易号（退款）前的日期标识
					refundNo = new String(params.get("batch_no").getBytes("ISO-8859-1"),"UTF-8").substring(8);
					params.put("batch_no", refundNo);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					logger.error("获取refundNo失败，失败原因："+e.getMessage());
				}
				er = managerDetails(params);
				if(StringUtils.isNotEmpty(refundNo)){
					er = updateTradeReturnGoodsState(refundNo,er);
				}
				if(er.isSuccess()){
					er.setResultMessage("success");
				}
			}else{
				er.addErrorMessage("数据验证失败");
			}
		}else if("CUP".equals(payBank)){
			Map<String, String> valideData = null;
			if (null != params && !params.isEmpty()) {
				Iterator<Entry<String, String>> it = params.entrySet()
						.iterator();
				valideData = new HashMap<String, String>(params.size());
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					String key = (String) e.getKey();
					String value = (String) e.getValue();
					try {
						value = new String(value.getBytes("ISO-8859-1"), SDKUtil.encoding_UTF8);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					valideData.put(key, value);
				}
			}
			if (SDKUtil.validate(valideData, SDKUtil.encoding_UTF8)) {
				String refundNo=params.get("orderId");
				er = managerDetailsCup(params);
				if(StringUtils.isNotEmpty(refundNo)){
					List<RefundTransations> refundTransationsList = refundTransationsDAO.queryRefundTransationByRefundNo(refundNo);
					if(null!=refundTransationsList && refundTransationsList.size()>0 ){
						RefundTransations refundTransations = refundTransationsList.get(0);
						if(null!=refundTransations){
							String codeNo = refundTransations.getCodeNo();
							// 依据退款单号更新退款状态 
							TradeReturnGoodsDTO tradeReturnGoodsDTO = new TradeReturnGoodsDTO();
							tradeReturnGoodsDTO.setAfterService(3);
							tradeReturnGoodsDTO.setCodeNo(codeNo);
							tradeReturnGoodsDTO.setConfirmStatus("1");// 确认同意退款
							tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDING.getCode());// 退款申请成功更改为待买家确认收款
							int updateFlag = tradeReturnGoods.updateByCodeNo(tradeReturnGoodsDTO);
							if(updateFlag == 0){
								logger.info("trade_return_goods表更新状态失败");
								er.addErrorMessage("退款失败");
							}
						}
					}
//					er = updateTradeReturnGoodsState(refundNo,er);
				}
				if(er.isSuccess()){
					er.setResultMessage("success");
				}
			}else{
				er.addErrorMessage("数据验证失败");
			}
		}
		return er;
	}
	/**
	 * 解析支付宝退款异步响应报文detail
	 * @param details
	 * @return
	 */
	private ExecuteResult<Integer> managerDetails(Map<String,String> map){
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
//		String[] detailList = details.split("\\\\");
//		for(String detail:detailList){
//			String[] params = detail.split("\\^");
//			
//		}
		String details = map.get("result_details");
		map.put("outRefundNo", map.get("batch_no"));
		// 假定只有一个退款明细
		String[] params = details.split("\\^");
		String result = params[params.length-1]; 
		if(null!=result&&"SUCCESS".equals(result)){
			map.put("state", "1");
			map.put("resultCode", result);
			map.put("resultMessage", "操作成功");
			logger.info("退款成功");
			er.setResultMessage("退款成功");
		}else{
			map.put("state", "2");
			map.put("resultCode", result);
			map.put("resultMessage", AlipayErrorCodeProperties.getProperty(result));
			logger.info("退款失败");
			er.addErrorMessage("退款失败");
		}
		// 修改退款记录信息
		ExecuteResult<RefundTransationsDTO> flag = updateRefundTransation(map);
		if(!flag.isSuccess()){
			logger.info("退款交易表数据更新失败");
			er.addErrorMessage("退款交易表数据更新失败");
		}
		return er;
	}
	
	/**
	 * 解析银联退款异步响应报文detail
	 * @param details
	 * @return
	 */
	private ExecuteResult<Integer> managerDetailsCup(Map<String,String> map){
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		
		String respCode=map.get("respCode");
		String respMsg=map.get("respMsg");
		String outRefundNo=map.get("orderId");
		map.put("outRefundNo", outRefundNo);
		if(null!=respCode&&"00".equals(respCode)){
			map.put("state", "1");
			map.put("resultCode", respCode);
			map.put("resultMessage", "操作成功");
			logger.info("退款成功");
			er.setResultMessage("退款成功");
		}else{
			map.put("state", "2");
			map.put("resultCode", respCode);
			map.put("resultMessage", respMsg);
			logger.info("退款失败");
			er.addErrorMessage("退款失败");
		}
		// 修改退款记录信息
		ExecuteResult<RefundTransationsDTO> flag = updateRefundTransation(map);
		if(!flag.isSuccess()){
			logger.info("退款交易表数据更新失败");
			er.addErrorMessage("退款交易表数据更新失败");
		}
		return er;
	}
	
	/**
	 * 依据退款结果修改退款商品表的状态
	 * @param refundNo
	 * @param er
	 * @return
	 */
	private ExecuteResult<Integer> updateTradeReturnGoodsState(String outRefundNo,ExecuteResult<Integer> er){
		// 依据退款批次号，获取退款订单数据
		List<RefundTransations> refundTransationsList = refundTransationsDAO.queryRefundTransationByRefundNo(outRefundNo);
		if(null!=refundTransationsList && refundTransationsList.size()>0 ){
			RefundTransations refundTransations = refundTransationsList.get(0);
			if(null!=refundTransations){
					
				String codeNo = refundTransations.getCodeNo();
				// 依据退款单号更新退款状态 
				TradeReturnGoodsDTO tradeReturnGoodsDTO = new TradeReturnGoodsDTO();
				tradeReturnGoodsDTO.setCodeNo(codeNo);
				if(er.isSuccess()){
					tradeReturnGoodsDTO.setState(7);//支付宝退款成功
				}else{
					tradeReturnGoodsDTO.setState(10);//支付宝退款失败
					tradeReturnGoodsDTO.setConfirmStatus("0");//确认状态改为待平台处理状态
				}
				int updateFlag = tradeReturnGoods.updateByCodeNo(tradeReturnGoodsDTO);
				if(updateFlag == 0){
					logger.info("trade_return_goods表更新状态失败");
					er.addErrorMessage("退款失败");
				}
			}else{
				logger.info("不存在退款交易信息");
				er.addErrorMessage("不存在退款交易信息");
			}
		}else{
			logger.info("不存在退款交易信息");
			er.addErrorMessage("不存在退款交易信息");
		}
		return er;
	}
	/**
	 * 更新退款交易表
	 * @param map 退款交易结果，state必填
	 */
	public ExecuteResult<RefundTransationsDTO> updateRefundTransation(Map<String,String> map){
		String code = map.get("resultCode");
		String message = map.get("resultMessage");
		String state = map.get("state");
		String outRefundNo = map.get("outRefundNo");
		RefundTransations refundTransations = new RefundTransations();
		refundTransations.setOutRefundNo(outRefundNo);
		// 获取退款记录
		List<RefundTransations> refundTransationsList = refundTransationsDAO.queryList(refundTransations, null);
		RefundTransationsDTO refundTransationsDTO = new RefundTransationsDTO();
		// 依据创建新记录
		BeanUtils.copyProperties(refundTransationsList.get(0), refundTransationsDTO);
		refundTransationsDTO.setResultCode(code);
		refundTransationsDTO.setResultMessage(message);
		refundTransationsDTO.setState(Integer.parseInt(state));
		refundTransationsDTO.setId(null);
		return refundTransationsService.createRefundTransationsDTO(refundTransationsDTO);
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> updateTradeReturnGoodsDetailDTO(
			TradeReturnGoodsDetailDTO tradeReturnGoodsDetail) {
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();
		tradeReturnGoodsDetailService.updateTradeReturnGoodsDetailDTO(tradeReturnGoodsDetail);
		executeResult.setResult(tradeReturnGoodsDetail);
		return executeResult;
	}
	
	/**
     * 
     * <p>Discription:[计算优惠活动退款金额]</p>
     * Created on 2015-12-29
     * @param dto
     * @author:[王鹏]
     */
    public ExecuteResult<String> calTotalRefundGoods(String returnId){
    	ExecuteResult<String> execu=new ExecuteResult<String>();
    	BigDecimal paymentPrice=BigDecimal.ZERO;//参加平台优惠活动的商品总金额
        BigDecimal refundPromotionMoney=BigDecimal.ZERO;//退款优惠总金额
        TradeReturnGoodsDTO resGood=tradeReturnGoodsService.getTradeReturnGoodsDTOById(Long.valueOf(returnId));
    	if(resGood==null){
    		execu.addErrorMessage("退款单不能为空！");
    		return execu;
    	}	
	    ExecuteResult<TradeOrdersDTO> orderRes = tradeOrderExportService.getOrderById(resGood.getOrderId());
		BigDecimal refundGoods=resGood.getRefundGoods();//退款金额
		if (orderRes == null || orderRes.getResult() == null) {
			execu.addErrorMessage("订单不能为空！");
			return execu;
		}
		List<TradeReturnGoodsDetailDTO> details=this.getTradeReturnGoodsDetailReturnId(returnId);
		TradeOrdersDTO tradeOrdersDTO = orderRes.getResult();
        if(!tradeOrdersDTO.getPaymentMethod().equals(PayMethodEnum.PAY_INTEGRAL.getCode())){
        	//更新促销活动结算单中促销退款总金额
    		ExecuteResult<ActivityStatementsDTO> ext=activityStatementSerice.queryActivityStatementsByOrderId(resGood.getOrderId());//查询该订单的平台优惠总金额
        	List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
        	for(TradeOrderItemsDTO item:tradeOrderItemsDTOs){//计算参加平台优惠活动的商品总金额
				boolean flag=item.getSkuId().equals(details.get(0).getSkuId());
				if(flag){
					ExecuteResult<TradeOrderItemsDiscountDTO>res=tradeOrderItemsDiscountExportService.queryOrderItemsDiscountById(item.getOrderItemId().intValue());				
					if(res!=null&&res.getResult()!=null){
						if(ActivityTypeEnum.PLATFORMCOUPONS.getStatus().equals(res.getResult().getCouponType())||ActivityTypeEnum.PLATFORMFULLREDUCTION.getStatus().equals(res.getResult().getFullReductionType())
								||ActivityTypeEnum.PLATFORMINTEGRATION.getStatus().equals(res.getResult().getIntegralType())||ActivityTypeEnum.PLATFORMMARKDOWN.getStatus().equals(res.getResult().getMarkdownType())){
							//该商品的平台优惠总金额
							if(ActivityTypeEnum.PLATFORMCOUPONS.getStatus().equals(res.getResult().getCouponType())){
								paymentPrice=paymentPrice.add(res.getResult().getCouponDiscount());//平台优惠卷金额
							}if(ActivityTypeEnum.PLATFORMFULLREDUCTION.getStatus().equals(res.getResult().getFullReductionType())){
								paymentPrice=paymentPrice.add(res.getResult().getFullReductionDiscount());//平台满减金额
							}if(ActivityTypeEnum.PLATFORMMARKDOWN.getStatus().equals(res.getResult().getMarkdownType())){
								paymentPrice=paymentPrice.add(res.getResult().getMarkdownDiscount());//平台直降金额
							}if(ActivityTypeEnum.PLATFORMINTEGRATION.getStatus().equals(res.getResult().getIntegralType())){
								paymentPrice=paymentPrice.add(res.getResult().getIntegralDiscount());//积分优惠金额
							}
						}
					//退款金额/参加平台活动商品的实际支付总金额*平台活动优惠总金额
					if(resGood.getRefundFreight()!=null){
						refundPromotionMoney=refundGoods.subtract(resGood.getRefundFreight()).divide(item.getPayPriceTotal(),10, RoundingMode.DOWN).multiply(paymentPrice).setScale(4,BigDecimal.ROUND_DOWN).setScale(2,BigDecimal.ROUND_UP);
					}else{
						refundPromotionMoney=refundGoods.divide(item.getPayPriceTotal(),10, RoundingMode.DOWN).multiply(paymentPrice).setScale(4,BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_UP);
					}
					if(ext!=null&&ext.getResult()!=null){
        				ActivityStatementsDTO activityStatementsDTO=ext.getResult();
    					if(activityStatementsDTO.getTotalRefundAmount()==null){
    						activityStatementsDTO.setTotalRefundAmount(refundPromotionMoney);
    					}else{
    						activityStatementsDTO.setTotalRefundAmount(activityStatementsDTO.getTotalRefundAmount().add(refundPromotionMoney));
    					}
    					//优惠退款金额大于总优惠，则取优惠总金额
    					if(activityStatementsDTO.getTotalRefundAmount().compareTo(activityStatementsDTO.getTotalDiscountAmount())>0){
    						activityStatementsDTO.setTotalRefundAmount(activityStatementsDTO.getTotalDiscountAmount());
    					}
    					activityStatementSerice.updateActivityStatement(activityStatementsDTO);
        			}
				   }
				}
        	}
        	//判断所有商品是否都已经退货/退款
        	Integer[] states = {TradeReturnStatusEnum.SUCCESS.getCode(),TradeReturnStatusEnum.REFUNDING.getCode(),TradeReturnStatusEnum.PLATFORMTOREFUND.getCode(),TradeReturnStatusEnum.PLATFORMDEALING.getCode(),};
	        boolean flag=this.isAllItemReturn(tradeOrdersDTO, states);
			if(flag){
				if(ext!=null&&ext.getResult()!=null){
    				ActivityStatementsDTO activityStatementsDTO=ext.getResult();
    				activityStatementsDTO.setState(1);//更新为有效
    				activityStatementSerice.updateActivityStatement(activityStatementsDTO);
    			}
			}
      	}
        return execu;
     }
    /**
     * 
     * <p>Discription:[计算积分退还]</p>
     * Created on 2015年12月30日
     * @param dto
     * @author:[宋文斌]
     */
	public ExecuteResult<String> calcRefundIntegral(String returnId) {
		ExecuteResult<String> res=new ExecuteResult<String>();
		TradeReturnGoodsDTO resGood = tradeReturnGoodsService.getTradeReturnGoodsDTOById(Long.valueOf(returnId));
		if (resGood != null) {
			ExecuteResult<TradeOrdersDTO> orderRes = tradeOrderExportService.getOrderById(resGood.getOrderId());
			if (orderRes.isSuccess() && orderRes.getResult() != null) {
				TradeOrdersDTO orderItem = orderRes.getResult();
				if (!orderItem.getPaymentMethod().equals(PayMethodEnum.PAY_INTEGRAL.getCode())) {
					// 查询订单使用的积分
        			Integer integral = orderItem.getIntegral();
        			if(integral != null && integral.intValue() > 0){
        				// 退款金额
            			BigDecimal refundGoods = resGood.getRefundGoods();
            			// 查询退款金额中是否包含运费
            			BigDecimal refundFreight = resGood.getRefundFreight();
            			if(refundFreight != null){
            				refundGoods = refundGoods.subtract(refundFreight);
            			}
            			// 订单实际支付总金额
						BigDecimal payMentPrice = orderItem.getPaymentPrice().subtract(orderItem.getFreight() == null ? BigDecimal.ZERO : orderItem.getFreight());//该订单实际支付总金额减去运费总金额
						// 应退积分
						BigDecimal refundIntegral = refundGoods.divide(payMentPrice, 10, RoundingMode.DOWN).multiply(BigDecimal.valueOf(integral)).setScale(0, BigDecimal.ROUND_DOWN);
						Integer[] states = { TradeReturnStatusEnum.SUCCESS.getCode(),
								TradeReturnStatusEnum.REFUNDING.getCode(),
								TradeReturnStatusEnum.PLATFORMTOREFUND.getCode(),
								TradeReturnStatusEnum.PLATFORMDEALING.getCode(), };
						// 该订单已退款的商品数量
						Integer itemReturnNum = null;
						// 该订单已退积分数量
						Integer integralReturnNum = null;
						// 该订单已退金额
						BigDecimal returnAmount = null;
						// 该订单下全部的商品数量
						Integer orderItemNum = null;
						ExecuteResult<Map<String, Object>> returnNumResult = this.getReturnNum(orderItem, states);
						if(returnNumResult.isSuccess() && returnNumResult.getResult() != null){
							itemReturnNum = (Integer) returnNumResult.getResult().get("itemReturnNum");
							integralReturnNum = (Integer) returnNumResult.getResult().get("integralReturnNum");
							returnAmount = (BigDecimal) returnNumResult.getResult().get("returnAmount");
						}
						ExecuteResult<Integer> orderItemNumResult = this.getOrderItemNum(orderItem);
						if(orderItemNumResult.isSuccess() && orderItemNumResult.getResult() != null){
							orderItemNum = orderItemNumResult.getResult();
						}
						if(itemReturnNum != null && integralReturnNum != null && returnAmount != null && orderItemNum != null ){
							if(itemReturnNum.intValue() == orderItemNum.intValue() && returnAmount.compareTo(payMentPrice) >= 0){
								if(integral.intValue() > integralReturnNum.intValue()){
									// 剩余积分
									refundIntegral = BigDecimal.valueOf(integral.intValue() - integralReturnNum.intValue());
								}
							}
						}
						if(refundIntegral != null && refundIntegral.compareTo(BigDecimal.ZERO) > 0){
							// 给买家返回积分
	                    	UserIntegralTrajectoryDTO userIntegralTrajectoryDTO = new UserIntegralTrajectoryDTO();
	                    	userIntegralTrajectoryDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_REFUND.getCode());
	                    	userIntegralTrajectoryDTO.setOrderId(orderItem.getOrderId());
	                    	userIntegralTrajectoryDTO.setIntegralValue(refundIntegral.longValue());
	                    	userIntegralTrajectoryDTO.setUsingTime(new Date());
	                    	userIntegralTrajectoryDTO.setUserId(orderItem.getBuyerId());
	                    	userIntegralTrajectoryDTO.setShopId(orderItem.getShopId());
	                    	userIntegralTrajectoryDTO.setInsertBy(orderItem.getBuyerId());
	                    	userIntegralTrajectoryDTO.setInsertTime(new Date());
	                    	userIntegralTrajectoryService.addUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO);
	                    	// 更新退款单详情的退还积分数量
	                    	TradeReturnGoodsDetailDTO tradeReturnGoodsDetail = new TradeReturnGoodsDetailDTO();
	        				tradeReturnGoodsDetail.setReturnGoodsId(Long.valueOf(returnId));
	        				List<TradeReturnGoodsDetailDTO> detailDTOs = tradeReturnGoodsDetailService.searchByCondition(tradeReturnGoodsDetail);
	        				if(detailDTOs != null && detailDTOs.size() > 0){
	        					TradeReturnGoodsDetailDTO detailDTO = detailDTOs.get(0);
	        					detailDTO.setReturnIntegral(refundIntegral.intValue());
	        					tradeReturnGoodsDetailService.updateSelective(detailDTO);
	        				}
						}
        			}
				}
			}
		}
		return res;
	}

	@Override
	public ExecuteResult<Integer> apRefundResult(Map<String, String> params, String payBank) {
		logger.info("\n 方法[{}]，入参：[{}]","TradeReturnExportServiceImpl-refundResult",params);
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		if(PayBankEnum.AP.getLable().equals(payBank) || "AP".equals(payBank) ){
			if(AlipayNotify.verify(params)){//验证成功
				String refundNo="";
				try {
					// 截取掉对外交易号（退款）前的日期标识
					refundNo = new String(params.get("batch_no").getBytes("ISO-8859-1"),"UTF-8").substring(8);
					params.put("batch_no", refundNo);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					logger.error("获取refundNo失败，失败原因："+e.getMessage());
				}
				if(StringUtils.isNotEmpty(refundNo)){
					// 依据退款批次号，获取退款订单数据
					List<RefundTransations> refundTransationsList = refundTransationsDAO.queryRefundTransationByRefundNo(refundNo);
					if(null!=refundTransationsList && refundTransationsList.size()>0 ){
						RefundTransations refundTransations = refundTransationsList.get(0);
						if(null!=refundTransations){
							String codeNo = refundTransations.getCodeNo();
							// 依据退款单号更新退款状态 
							TradeReturnGoodsDTO tradeReturnGoodsDTO = new TradeReturnGoodsDTO();
							tradeReturnGoodsDTO.setAfterService(3);
							tradeReturnGoodsDTO.setCodeNo(codeNo);
							tradeReturnGoodsDTO.setConfirmStatus("1");// 确认同意退款
							tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDING.getCode());// 退款申请成功更改为待买家确认收款
							int updateFlag = tradeReturnGoods.updateByCodeNo(tradeReturnGoodsDTO);
							if(updateFlag == 0){
								logger.info("trade_return_goods表更新状态失败");
								er.addErrorMessage("退款失败");
							}
						}else{
							logger.info("不存在退款交易信息");
							er.addErrorMessage("不存在退款交易信息");
						}
					}else{
						logger.info("不存在退款交易信息");
						er.addErrorMessage("不存在退款交易信息");
					}
				}
				if(er.isSuccess()){
					er.setResultMessage("success");
				}
			}else{
				er.addErrorMessage("数据验证失败");
			}
		}else if(PayBankEnum.CUP.getLable().equals(payBank)){
			Map<String, String> valideData = null;
			if (null != params && !params.isEmpty()) {
				Iterator<Entry<String, String>> it = params.entrySet()
						.iterator();
				valideData = new HashMap<String, String>(params.size());
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					String key = (String) e.getKey();
					String value = (String) e.getValue();
					try {
						value = new String(value.getBytes("ISO-8859-1"), SDKUtil.encoding_UTF8);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					valideData.put(key, value);
				}
			}
			if (SDKUtil.validate(valideData, SDKUtil.encoding_UTF8)) {
				String refundNo=params.get("orderId");
				er = managerDetailsCup(params);
				if(StringUtils.isNotEmpty(refundNo)){
					List<RefundTransations> refundTransationsList = refundTransationsDAO.queryRefundTransationByRefundNo(refundNo);
					if(null!=refundTransationsList && refundTransationsList.size()>0 ){
						RefundTransations refundTransations = refundTransationsList.get(0);
						if(null!=refundTransations){
							String codeNo = refundTransations.getCodeNo();
							// 依据退款单号更新退款状态 
							TradeReturnGoodsDTO tradeReturnGoodsDTO = new TradeReturnGoodsDTO();
							tradeReturnGoodsDTO.setAfterService(3);
							tradeReturnGoodsDTO.setCodeNo(codeNo);
							tradeReturnGoodsDTO.setConfirmStatus("1");// 确认同意退款
							tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDING.getCode());// 退款申请成功更改为待买家确认收款
							int updateFlag = tradeReturnGoods.updateByCodeNo(tradeReturnGoodsDTO);
							if(updateFlag == 0){
								logger.info("trade_return_goods表更新状态失败");
								er.addErrorMessage("退款失败");
							}
						}
					}
//					er = updateTradeReturnGoodsState(refundNo,er);
				}
				if(er.isSuccess()){
					er.setResultMessage("success");
				}
			}else{
				er.addErrorMessage("数据验证失败");
			}
		}
		return er;
	}
    
}
