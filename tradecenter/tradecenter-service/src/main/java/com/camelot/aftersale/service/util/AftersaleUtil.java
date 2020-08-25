package com.camelot.aftersale.service.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.camelot.aftersale.domain.Complain;
import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.aftersale.dto.RefundTransationsDTO;
import com.camelot.common.enums.MsgCodeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.domain.RefundOrder;
import com.camelot.payment.domain.RefundTransations;

/**
 *  Impl中所要用到的封装，验证等方法
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-27
 */
public class AftersaleUtil{
	
	 /**
	 * 验证refundApply,
	 * 
	 * @param refundPayParam
	 * @return
	 */
	protected ExecuteResult<RefundPayParam> verifyRefundApply(RefundPayParam refundPayParam){
		ExecuteResult<RefundPayParam> result= new ExecuteResult<RefundPayParam>();
		
		if(refundPayParam!=null){
			// 参数验证
			if(StringUtils.isBlank(refundPayParam.getOrderNo())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "orderNo"));
			}else if(refundPayParam.getBuyerId()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "buyerId"));
			}else if(refundPayParam.getReproNo()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "reproNo"));
			}else if(refundPayParam.getRefundAmount()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "refundAmount"));
			}else if(refundPayParam.getOrderPayBank()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "orderPayBank"));
			}else if(refundPayParam.getOrderPrice() == null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "orderPrice"));
			}
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "对象不存在"));
		}
		return result;
	}
	
	/**
	 * 构建投诉数据库对象
	 * 
	 * @param complainDTO
	 * 
	 * @return
	 */
	protected Complain buildComplain(ComplainDTO complainDTO) {
		
		if(complainDTO!=null){
			Complain complain =new Complain();
			complain.setId(complainDTO.getId());
			complain.setOrderId(complainDTO.getOrderId());
			complain.setSkuId(complainDTO.getSkuId());
			complain.setRefundId(complainDTO.getRefundId());
			complain.setBuyerId(complainDTO.getBuyerId());
            complain.setSellerId(complainDTO.getSellerId());
            complain.setShopName(complainDTO.getShopName());
            complain.setType(complainDTO.getType());
            complain.setComplainContent(complainDTO.getComplainContent());
            complain.setComplainPicUrl(complainDTO.getComplainPicUrl());
            complain.setComplainResion(complainDTO.getComplainResion());
            complain.setStatus(complainDTO.getStatus());
            complain.setStatusText(complainDTO.getStatusText());
            complain.setCreated(complainDTO.getCreated());
            complain.setCreatedBegin(complainDTO.getCreatedBegin());
            complain.setCreatedEnd(complainDTO.getCreatedEnd());
            complain.setComment(complainDTO.getComment());
            complain.setComplainPhone(complainDTO.getComplainPhone());
            complain.setComplainEmail(complainDTO.getComplainEmail());
            complain.setReturnGoodsId(complainDTO.getReturnGoodsId());
            complain.setComplainType(complainDTO.getComplainType());
            complain.setBuyIdList(complainDTO.getBuyIdList());
            complain.setStatusSelect(complainDTO.getStatusSelect());
            complain.setComplainGroup(complainDTO.getComplainGroup());
            complain.setResolutionTime(complainDTO.getResolutionTime());
            return complain;
		}
		return null;
	}
	
	/**
	 * 构建投诉出参对象
	 * 
	 * @param complain
	 * 
	 * @return
	 */
	protected ComplainDTO buildComplainDTO(Complain complain) {
		
		if(complain!=null){
			ComplainDTO complainDTO =new ComplainDTO();
			complainDTO.setId(complain.getId());
			complainDTO.setOrderId(complain.getOrderId());
			complainDTO.setSkuId(complain.getSkuId());
			complainDTO.setRefundId(complain.getRefundId());
			complainDTO.setBuyerId(complain.getBuyerId());
            complainDTO.setSellerId(complain.getSellerId());
            complainDTO.setShopName(complain.getShopName());
            complainDTO.setType(complain.getType());
            complainDTO.setComplainContent(complain.getComplainContent());
            complainDTO.setComplainPicUrl(complain.getComplainPicUrl());
            complainDTO.setComplainResion(complain.getComplainResion());
            complainDTO.setStatus(complain.getStatus());
            complainDTO.setStatusText(complain.getStatusText());
            complainDTO.setCreated(complain.getCreated());
            complainDTO.setCreatedBegin(complain.getCreatedBegin());
            complainDTO.setCreatedEnd(complain.getCreatedEnd());
            complainDTO.setComment(complain.getComment());
            complainDTO.setComplainPhone(complain.getComplainPhone());
            complainDTO.setComplainEmail(complain.getComplainEmail());
            complainDTO.setReturnGoodsId(complain.getReturnGoodsId());
            complainDTO.setComplainType(complain.getComplainType());
            complainDTO.setComplainTypeTotal(complain.getComplainTypeTotal());
            complainDTO.setModified(complain.getModified());
            complainDTO.setComplainGroup(complain.getComplainGroup());
			return complainDTO;
		}
		return null;
	}
	
	/**
	 * 构建退款数据库对象
	 * 
	 * @param refundPayParam
	 * 
	 * @return
	 */
	protected RefundOrder buildRefundOrder(RefundPayParam refundPayParam) {
		RefundOrder refundOrder =new RefundOrder();
//		BeanUtils.copyProperties(refundPayParam, refundOrder);
		refundOrder.setOrderNo(refundPayParam.getOrderNo());
		refundOrder.setOrderLabel(refundPayParam.getOrderLabel());
		refundOrder.setBuyerId(refundPayParam.getBuyerId());
		refundOrder.setBuyerName(refundPayParam.getBuyerName());
		refundOrder.setBuyerPhone(refundPayParam.getBuyerPhone());
		refundOrder.setRefundAmount(refundPayParam.getRefundAmount());
		refundOrder.setRefundReason(refundPayParam.getRefundReason());
		refundOrder.setOrderPayBank(PayBankEnum.getEnumByQrCode(refundPayParam.getOrderPayBank()).name());
		refundOrder.setReproNo(refundPayParam.getReproNo());
		refundOrder.setStatus(refundPayParam.getStatus());
		refundOrder.setItemId(refundPayParam.getItemId());
		refundOrder.setSkuId(refundPayParam.getSkuId());
		refundOrder.setOrderPrice(refundPayParam.getOrderPrice());
		return refundOrder;
	}
	/**
	 * 构建退款出参对象
	 * 
	 * @param refundOrder
	 * 
	 * @return
	 */
	protected RefundPayParam buildRefundPayParam(RefundOrder refundOrder) {
		if(refundOrder!=null){
			
			RefundPayParam refundPayParam =new RefundPayParam();
//			BeanUtils.copyProperties(refundOrder, refundPayParam);
			refundPayParam.setRefundNo(refundOrder.getRefundNo());
			refundPayParam.setOrderNo(refundOrder.getOrderNo());
			refundPayParam.setOrderLabel(refundOrder.getOrderLabel());
			refundPayParam.setOutTradeNo(refundOrder.getOutTradeNo());
			refundPayParam.setBuyerId(refundOrder.getBuyerId());
			refundPayParam.setRefundAmount(refundOrder.getRefundAmount());
            refundPayParam.setReproNo(refundOrder.getReproNo());
            refundPayParam.setId(refundOrder.getId());
			return refundPayParam;
		}
		return null;
	}
	/**
	 * 根据退款金额，计算应退手续费
	 * @param refundAmount 退款金额
	 * @param orderAmount  订单金额
	 * @param factorage    总手续费
	 * @return  应退手续费
	 * @author 王东晓
	 */
	protected BigDecimal getPlatformFactorage(BigDecimal refundAmount,BigDecimal orderAmount,BigDecimal factorage){
		if(refundAmount == null || orderAmount == null || factorage == null){
			return null;
		}
		//平台应退手续费 = （退款金额/支付金额）*总手续费
		return new BigDecimal(refundAmount.divide(orderAmount,8, BigDecimal.ROUND_UP).multiply(factorage).doubleValue()+"").setScale(2, BigDecimal.ROUND_UP);
	}
	public static void main(String[] args){
		BigDecimal refundAmount = new BigDecimal(0.08);
		BigDecimal orderAmount = new BigDecimal(0.08);
		BigDecimal factorage = new BigDecimal(0.03);
//		System.out.println(refundAmount.divide(orderAmount,2,BigDecimal.ROUND_UP));
		System.out.println(new BigDecimal(refundAmount.divide(orderAmount,8, BigDecimal.ROUND_UP).multiply(factorage).doubleValue()+"").setScale(2, BigDecimal.ROUND_UP));
		RefundPayParam refundPayParam = new RefundPayParam();
		refundPayParam.setOrderNo("1111111111111111111");
		refundPayParam.setId(111l);
		refundPayParam.setCreated(new Date());
		RefundOrder refundOrder =new RefundOrder();
		BeanUtils.copyProperties(refundPayParam, refundOrder);
		System.out.println("aaa");
		
	}
	/**
	 * 
	 * <p>Discription:[构建退款记录的数据库对象]</p>
	 * Created on 2015-10-15
	 * @param refundTransations
	 * @return
	 * @author:[王鹏]
	 */
	protected RefundTransations buildRefundTransations(RefundTransationsDTO refundTransationsDTO) {
		RefundTransations refundTransations=new RefundTransations();
		refundTransations.setId(refundTransationsDTO.getId());//主键
		refundTransations.setCodeNo(refundTransationsDTO.getCodeNo());//退款单号
		refundTransations.setOutTradeNo(refundTransationsDTO.getOutTradeNo());//对外交易号（支付）
		refundTransations.setOutRefundNo(refundTransationsDTO.getOutRefundNo());//对外交易号（退款）
		refundTransations.setOrderNo(refundTransationsDTO.getOrderNo());//订单号
		refundTransations.setPayBank(refundTransationsDTO.getPayBank());//支付方式
		refundTransations.setRefundAmount(refundTransationsDTO.getRefundAmount());//退款金额
		refundTransations.setTotalAmount(refundTransationsDTO.getTotalAmount());//支付总金额
		refundTransations.setState(refundTransationsDTO.getState());//状态
		refundTransations.setResultCode(refundTransationsDTO.getResultCode());//退款结果code
		refundTransations.setResultMessage(refundTransationsDTO.getResultMessage());//退款返回信息
		refundTransations.setInsertBy(refundTransationsDTO.getInsertBy());//操作法人
		refundTransations.setInsertTime(refundTransationsDTO.getInsertTime());//操作时间
		return refundTransations;
	}
	/**
	 * 
	 * <p>Discription:[构建退款记录的出参对象]</p>
	 * Created on 2015-10-15
	 * @param refundTransations
	 * @return
	 * @author:[王鹏]
	 */
	protected RefundTransationsDTO buildRefundTransationsDTO(RefundTransations refundTransations) {
		RefundTransationsDTO refundTransationsDTO=new RefundTransationsDTO();
		refundTransationsDTO.setId(refundTransations.getId());
		refundTransationsDTO.setCodeNo(refundTransations.getCodeNo());//退款单号
		refundTransationsDTO.setOutTradeNo(refundTransations.getOutTradeNo());//对外交易号（支付）
		refundTransationsDTO.setOutRefundNo(refundTransations.getOutRefundNo());//对外交易号（退款）
		refundTransationsDTO.setOrderNo(refundTransations.getOrderNo());//订单号
		refundTransationsDTO.setPayBank(refundTransations.getPayBank());//支付方式
		refundTransationsDTO.setRefundAmount(refundTransations.getRefundAmount());//退款金额
		refundTransationsDTO.setTotalAmount(refundTransations.getTotalAmount());//支付总金额
		refundTransationsDTO.setState(refundTransations.getState());//状态
		refundTransationsDTO.setResultCode(refundTransations.getResultCode());//退款结果code
		refundTransationsDTO.setResultMessage(refundTransations.getResultMessage());//退款返回信息
		refundTransationsDTO.setInsertBy(refundTransations.getInsertBy());//操作法人
		refundTransationsDTO.setInsertTime(refundTransations.getInsertTime());//操作时间
		return refundTransationsDTO;
	}
}
