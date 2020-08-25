package com.camelot.tradecenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.activity.dto.ActivityStatementsDTO;
import com.camelot.activity.service.ActivityStatementSerice;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.ActivityTypeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.goodscenter.dto.ContractOrderDTO;
import com.camelot.goodscenter.dto.InquiryOrderDTO;
import com.camelot.goodscenter.dto.InventoryModifyDTO;
import com.camelot.goodscenter.dto.ItemBaseDTO;
import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.TradeInventoryDTO;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.maketcenter.dto.CentralPurchasingRefOrderDTO;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.IntegralConfigDTO;
import com.camelot.maketcenter.dto.emums.IntegralTypeEnum;
import com.camelot.maketcenter.service.CentralPurchasingExportService;
import com.camelot.maketcenter.service.CentralPurchasingRefOrderExportService;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.maketcenter.service.IntegralConfigExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.dao.RefundOrderDAO;
import com.camelot.payment.domain.RefundOrder;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.storecenter.dto.ShopEvaluationDTO;
import com.camelot.storecenter.service.ShopEvaluationService;
import com.camelot.tradecenter.dao.TradeApprovedOrdersDao;
import com.camelot.tradecenter.dao.TradeOrderItemsDAO;
import com.camelot.tradecenter.dao.TradeOrderPriceHistoryDAO;
import com.camelot.tradecenter.dao.TradeOrdersDAO;
import com.camelot.tradecenter.domain.TradeApprovedOrders;
import com.camelot.tradecenter.domain.TradeOrderItems;
import com.camelot.tradecenter.domain.TradeOrderPriceHistory;
import com.camelot.tradecenter.domain.TradeOrders;
import com.camelot.tradecenter.dto.InvoiceDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsPackageDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;
import com.camelot.tradecenter.dto.enums.OrderTypeEnum;
import com.camelot.tradecenter.dto.enums.TradeOrdersStateEnum;
import com.camelot.tradecenter.service.InvoiceExportService;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.tradecenter.service.TradeOrderItemsDiscountExportService;
import com.camelot.tradecenter.service.TradeOrderItemsPackageExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
import com.camelot.usercenter.service.UserCreditExportService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;

@Service("tradeOrderExportService")
public class TradeOrderExportServiceImpl implements TradeOrderExportService {

    private static final Logger logger = LoggerFactory.getLogger(TradeOrderExportServiceImpl.class);

    @Resource
    private TradeOrdersDAO tradeOrdersDAO;
    @Resource
    private TradeOrderItemsDAO tradeOrderItemsDAO;
    @Resource
    private TradeInventoryExportService tradeInventoryExportService;
    @Resource
    private StatementExportService statementExportService;
    @Resource
    private TradeOrderPriceHistoryDAO tradeOrderPriceHistoryDAO;
    @Resource
    private UserCreditExportService userCreditExportService;
    @Resource
    private TradeApprovedOrdersDao tradeApprovedOrdersDAO;
    @Resource
	private ProtocolExportService protocolExportService;
    @Resource
    private InquiryExportService inquiryExportService;
    @Resource
    private InvoiceExportService invoiceExportService;
    @Resource
	private AddressBaseService addressBaseService;
    @Resource
	private UserExportService userExportService;
    @Resource
	private RefundOrderDAO refundOrderDAO;
    @Resource
	private TradeOrderExportService tradeOrderExportService;
    @Resource
	private UserIntegralTrajectoryExportService userIntegralTrajectoryService;
	@Resource
	private IntegralConfigExportService integralConfigService;
	@Resource
	private ActivityStatementSerice activityStatementSerice;
	@Resource
	private CouponsExportService couponsExportService;
	@Resource
	private CentralPurchasingExportService centralPurchasingExportService;
	@Resource
	private CentralPurchasingRefOrderExportService centralPurchasingRefOrderExportService;
	@Resource
	private TradeOrderItemsDiscountExportService  tradeOrderItemsDiscountExportService;
	@Resource
	private TradeOrderItemsPackageExportService  tradeOrderItemsPackageExportService;

	@Resource
	private ShopEvaluationService shopEvaluationService;
	
	@Resource
	private ItemEvaluationService itemEvaluationService;
	@Resource
	private ItemExportService itemExportService;
	
    @Override
    public ExecuteResult<TradeOrderCreateDTO> createOrder(TradeOrderCreateDTO dto) {
    	logger.info("\n 方法[{}]，入参：[{}]", "TradeOrderExportServiceImpl-createOrder", JSONObject.toJSONString(dto));
        logger.info("==============开始创建订单===================");
        ExecuteResult<TradeOrderCreateDTO> result = new ExecuteResult<TradeOrderCreateDTO>();
        try {
            List<TradeOrdersDTO> orders = dto.getSubOrders();
            if (orders == null || orders.size() <= 0) {
                result.addErrorMessage("参数为空！");
                return result;
            }
            if(dto.getParentOrder().getPaymentMethod() != null 
            		&& dto.getParentOrder().getPaymentMethod() != PayMethodEnum.PAY_INTEGRAL.getCode()
            		&& BigDecimal.ZERO.compareTo(dto.getParentOrder().getPaymentPrice()) >= 0){
            	result.addErrorMessage("支付金额不能为0！");
                return result;
            }
			String parentId = "";
			// 查询用户名，判断是不是退款冲抵用户，如果是退款冲抵用户，那么订单编号以“R”开头
			if (dto.getParentOrder() != null && dto.getParentOrder().getBuyerId() != null) {
				Long uid = dto.getParentOrder().getBuyerId();
				UserDTO userDTO = this.userExportService.queryUserById(uid);
				if (userDTO != null) {
					String refund_offset_account = SysProperties.getProperty(SysConstants.REFUND_OFFSET_ACCOUNT);
					if (refund_offset_account.equals(userDTO.getUname())) {
						// 查询父级订单号
						parentId = this.tradeOrdersDAO.getOrderId("R");
					} else {
						// 查询父级订单号
						parentId = this.tradeOrdersDAO.getOrderId("");
					}
				} else {
					result.addErrorMessage("用户不存在！");
					return result;
				}
			}
            // System.out.println("orderType:"+porder.getOrderType());
            //生成父级 订单
            if(dto.getParentOrder().isNotApproved()){//不走审核流程
            	TradeOrders porder = this.getTradeOrdersFromDTO(dto.getParentOrder());
                porder.setOrderId(parentId);
                porder.setParentOrderId("0");
            	this.tradeOrdersDAO.add(porder);
            	dto.getParentOrder().setOrderId(porder.getOrderId());
            }else{//走审核流程
            	TradeApprovedOrders porder=this.getTradeOrdersFromApprovedDTO(dto.getParentOrder());
            	porder.setAuditorId(dto.getParentOrder().getAuditorId());
                porder.setApproveStatus(dto.getParentOrder().getApproveStatus());
                porder.setOrderId(parentId);
                porder.setParentOrderId("0");
                this.tradeApprovedOrdersDAO.add(porder);
                dto.getParentOrder().setOrderId(porder.getOrderId());
            }
            TradeOrders paramOrder = null;
            TradeOrdersDTO order = null;
            TradeApprovedOrders approveOrders=null;
            for (int i = 0; i < orders.size(); i++) {
            	BigDecimal amount=BigDecimal.ZERO;
            	order = orders.get(i);
                order.setParentOrderId(parentId);
                //查询订单号
				order.setOrderId(parentId + "" + StringUtils.leftPad(Integer.toString(i + 1), 2, "0"));
                //添加订单
				if (order.isNotApproved()) {// 不走审核流程
					paramOrder = this.getTradeOrdersFromDTO(order);
					if (2 == order.getShipmentType()) {// 延期付款的订单 创建订单的时候直接待配送
						paramOrder.setState(TradeOrdersStateEnum.DELIVERING.getCode());
					} else {
						// 如果是积分兑换，订单状态直接为待配送
						if (paramOrder.getPaymentMethod() == PayMethodEnum.PAY_INTEGRAL.getCode()) {
							paramOrder.setState(TradeOrdersStateEnum.DELIVERING.getCode());
						} else {
							paramOrder.setState(TradeOrdersStateEnum.PAYING.getCode());
						}
					}
					this.tradeOrdersDAO.add(paramOrder);
				} else {// 走审核流程
					approveOrders = this.getTradeOrdersFromApprovedDTO(order);
					if (2 == order.getShipmentType()) {// 延期付款的订单 创建订单的时候直接待配送
						approveOrders.setState(TradeOrdersStateEnum.DELIVERING.getCode());
					} else {
						approveOrders.setState(TradeOrdersStateEnum.PAYING.getCode());
					}
					approveOrders.setAuditorId(order.getAuditorId());
					approveOrders.setApproveStatus(order.getApproveStatus());
					this.tradeApprovedOrdersDAO.add(approveOrders);
				}
				// 添加订单行项目
				for (TradeOrderItemsDTO item : order.getItems()) {
					TradeOrderItems tradeOrderItems = new TradeOrderItems();
					item.setOrderId(order.getOrderId());
					BeanUtils.copyProperties(item, tradeOrderItems);
					this.tradeOrderItemsDAO.add(tradeOrderItems);
					// 活动明细信息
					this.setTradeOrderItemsDiscountDTO(order, tradeOrderItems);
					// 添加套装商品订单记录
					if(item.getTradeOrderItemsPackages() != null && item.getTradeOrderItemsPackages().size() > 0){
						for(TradeOrderItemsPackageDTO tradeOrderItemsPackageDTO : item.getTradeOrderItemsPackages()){
							tradeOrderItemsPackageDTO.setOrderId(order.getOrderId());
							tradeOrderItemsPackageDTO.setOrderItemsId(tradeOrderItems.getId());
							tradeOrderItemsPackageExportService.add(tradeOrderItemsPackageDTO);
						}
					}
				}
                //延期付款的订单 直接扣除库存
				//积分兑换商品 直接扣除库存
				if (paramOrder != null && (Integer.valueOf(2).equals(order.getShipmentType())
						|| PayMethodEnum.PAY_INTEGRAL.getCode().equals(paramOrder.getPaymentMethod()))) {
					this.deductInventory(order.getItems());
				}
                if (order.getOrderType() != null) {
					if (order.getOrderType() == OrderTypeEnum.INQUIRY.getCode()) {// 询价订单
                		InquiryOrderDTO inquiryOrderDTO = new InquiryOrderDTO();
                		inquiryOrderDTO.setOrderNo(order.getOrderId());
                		inquiryOrderDTO.setInquiryNo(order.getContractNo());
                		inquiryOrderDTO.setCreateBy(String.valueOf(order.getBuyerId()));
                		inquiryOrderDTO.setCreateDate(new Date());
                		inquiryOrderDTO.setUpdateBy(String.valueOf(order.getBuyerId()));
                		inquiryOrderDTO.setUpdateDate(new Date());
                		inquiryOrderDTO.setState("0");
                		inquiryOrderDTO.setActiveFlag("0");
                		inquiryOrderDTO.setRemark("");
                		inquiryExportService.addInquiryOrder(inquiryOrderDTO);
					} else if (order.getOrderType() == OrderTypeEnum.AGREEMENT.getCode()) {// 协议订单
                		ContractOrderDTO contractOrderDTO = new ContractOrderDTO();
    					contractOrderDTO.setOrderNo(order.getOrderId());
    					contractOrderDTO.setContractNo(order.getContractNo());
    					contractOrderDTO.setCreateBy(String.valueOf(order.getBuyerId()));
    					contractOrderDTO.setCreateDate(new Date());
    					contractOrderDTO.setUpdateBy(String.valueOf(order.getBuyerId()));
    					contractOrderDTO.setUpdateDate(new Date());
    					contractOrderDTO.setState("0");
    					contractOrderDTO.setActiveFlag("0");
    					contractOrderDTO.setRemark("");
    					protocolExportService.addContractOrder(contractOrderDTO);
                	}
				}
				if (order.getInvoice() != null && (order.getInvoice() == 2 || order.getInvoice() == 3)) {// 普通发票或增值税发票
					if (order.getInvoiceId() != null) {
						// 查询发票是否存在
						InvoiceDTO invoiceDTO = new InvoiceDTO();
						invoiceDTO.setId(order.getInvoiceId());
						invoiceDTO.setBuyerId(order.getBuyerId());
						DataGrid<InvoiceDTO> dataGrid = invoiceExportService.queryInvoices(invoiceDTO, null);
						if (dataGrid != null && dataGrid.getRows() != null && dataGrid.getRows().size() > 0) {
							// 添加订单发票信息
							invoiceExportService.addTradeOrderInvoice(order.getOrderId(), order.getInvoiceId());
						}
					}
				}
				//插入促销活动记录信息
				this.setActivityRecordDTO(order);
				//活动记录信息
				this.setActivityStatementsDTO(order, amount);
            }
            // 将优惠券状态更新为已使用
            if(StringUtils.isNotBlank(dto.getParentOrder().getCouponId())){
            	CouponUserDTO couponUserDTO = new CouponUserDTO();
    			couponUserDTO.setCouponId(dto.getParentOrder().getCouponId());
    			couponUserDTO.setUserCouponType(1);
    			couponsExportService.updateCouponUser(couponUserDTO);
            }
            result.setResult(dto);
            
        } catch (Exception e) {
            logger.error("执行方法【createOrder】报错：{}", e);
            result.addErrorMessage("执行方法【createOrder】报错：" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            logger.info("==============结束创建订单===================");
        }
        return result;
    }

//    /**
//     * 
//     * <p>Discription:[调用积分接口增加积分]</p>
//     * Created on 2015-4-21
//     * @param totalPayment
//     * @param buyerId
//     * @author:wangcs
//     */
//    private void addCredit(BigDecimal totalPayment, Long buyerId) {
//    	UserCreditAddIn param = new UserCreditAddIn();
//    	param.setUserId(buyerId);
//    	param.setSorceType(1);
//    	//TODO 一分钱一积分 测试需要  发版修改
//    	String payStr = totalPayment.divide(new BigDecimal("1"), 0, RoundingMode.HALF_UP).toString();
//    	logger.info("获取积分："+payStr);
//    	param.setCredit(Long.parseLong(payStr));
//    	param.setDescription("订单获取");
//    	userCreditExportService.addUserCredit(param);
//	}
    /**
     * 
     * <p>Discription:[调用积分接口增加积分]</p>
     * Created on 2015-12-9
     * @param orderId
     * @author:周志军
     */
    private void addIntegral(String orderId) {
    	ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
    	if(!resultTradeOrdersDTO.isSuccess() || resultTradeOrdersDTO.getResult().getPaymentMethod() == PayMethodEnum.PAY_INTEGRAL.getCode()){
			logger.info("获取积分失败，商品不存在或该商品为积分商品");
			return ;
		}
    	if(resultTradeOrdersDTO.getResult().getPaymentType() == PayBankEnum.OFFLINE.getQrCode()){
    		logger.info("获取积分失败，线下支付无法获取积分");
			return ;
		}
    	//用户积分信息
		UserIntegralTrajectoryDTO userIntegralTrajectoryDTO = new UserIntegralTrajectoryDTO();
    	userIntegralTrajectoryDTO.setOrderId(resultTradeOrdersDTO.getResult().getOrderId());
		userIntegralTrajectoryDTO.setUserId(resultTradeOrdersDTO.getResult().getBuyerId());
		userIntegralTrajectoryDTO.setShopId(resultTradeOrdersDTO.getResult().getShopId());
		userIntegralTrajectoryDTO.setIntegralType(1);
		Integer platformId = resultTradeOrdersDTO.getResult().getPlatformId();
		platformId = null == platformId ? 0 : platformId;
		userIntegralTrajectoryDTO = getIntegralValue(userIntegralTrajectoryDTO,resultTradeOrdersDTO.getResult().getPaymentPrice(),platformId.longValue());
		if(null == userIntegralTrajectoryDTO){
			return ;
		}else{
			userIntegralTrajectoryService.addUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO);
			logger.info("评论获取积分成功");
		}
    }

    private UserIntegralTrajectoryDTO getIntegralValue(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO,BigDecimal amount,Long platformId){
		logger.info("\n 方法[{}]，入参：[{},{}]","UserIntegralTrajectoryServiceImpl-getIntegralValue",JSON.toJSONString(userIntegralTrajectoryDTO),amount);
		// 校验是否已获取积分
		ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> userIntegral = userIntegralTrajectoryService.queryUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO, null);
		if(!userIntegral.isSuccess() || 0 < userIntegral.getResult().getTotal()){
			logger.info("获取积分失败，已获取积分");
			return null;
		}
		platformId = null == platformId ? 0L : platformId;
		ExecuteResult<List<IntegralConfigDTO>> integralEr = integralConfigService.queryIntegralConfigDTOByMoney(amount,platformId);
		if(integralEr.isSuccess() && integralEr.getResult().size() == 1){
			IntegralConfigDTO backIntegral = integralEr.getResult().get(0);
			Long val = backIntegral.getGetIntegral().multiply(amount).longValue();
			if(val > 0){
				userIntegralTrajectoryDTO.setIntegralValue(val);
			}else{
				logger.info("订单获取积分失败，积分值为0");
				return null;
			}
			return userIntegralTrajectoryDTO;
		}else{
			logger.info("订单获取积分失败，配置信息不存在或不唯一");
			return null;
		}
	}
    
	/**
     * <p>Discription:[TradeOrdersDTO to TradeOrders]</p>
     * Created on 2015-3-10
     *
     * @param order
     * @return
     * @author:wangcs
     */
    private TradeOrders getTradeOrdersFromDTO(TradeOrdersDTO dto) {
        TradeOrders order = new TradeOrders();
        order.setBuyerId(dto.getBuyerId());
        order.setCityId(dto.getCityId());
        order.setCountyId(dto.getCountyId());
        order.setDetailAddress(dto.getDetailAddress());
        order.setEmail(dto.getEmail());
        order.setFreight(dto.getFreight());
        order.setFullAddress(dto.getFullAddress());
        order.setInvoice(dto.getInvoice());
        order.setInvoiceTitle(dto.getInvoiceTitle());
        order.setLogisticsCompany(dto.getLogisticsCompany());
        order.setLogisticsNo(dto.getLogisticsNo());
        order.setMemo(dto.getMemo());
        order.setMobile(dto.getMobile());
        order.setName(dto.getName());
        order.setOrderId(dto.getOrderId());
        order.setParentOrderId(dto.getParentOrderId());//母订单编号
        order.setPaymentPrice(dto.getPaymentPrice());
        order.setPaymentType(dto.getPaymentType());
        order.setPaymentTime(dto.getPaymentTime());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setPhone(dto.getPhone());
        order.setProvinceId(dto.getProvinceId());
        order.setSellerId(dto.getSellerId());
        order.setShipmentType(dto.getShipmentType());
        order.setShopId(dto.getShopId());
        order.setTotalDiscount(dto.getTotalDiscount());
        order.setTotalPrice(dto.getTotalPrice());
        order.setPayPeriod(dto.getPayPeriod());
        order.setPaymentType(dto.getPaymentType());
        order.setState(dto.getState());//订单状态
        order.setPromoCode(dto.getPromoCode());//优惠码
        order.setOrderType(dto.getOrderType()); //订单类型
        order.setPlatformId(dto.getPlatformId());//平台类型
        order.setDiscountAmount(dto.getDiscountAmount());//折扣金额
        order.setPaid(dto.getPaid());
        order.setIntegral(dto.getIntegral());
        order.setIntegralDiscount(dto.getIntegralDiscount());
        order.setExchangeRate(dto.getExchangeRate());
        order.setCentralPurchasing(dto.getCentralPurchasing());
        order.setIsService(dto.getIsService());
        order.setCreatorId(dto.getCreatorId());
        return order;
    }
    private TradeApprovedOrders getTradeOrdersFromApprovedDTO(TradeOrdersDTO dto) {
    	TradeApprovedOrders order = new TradeApprovedOrders();
        order.setBuyerId(dto.getBuyerId());
        order.setCityId(dto.getCityId());
        order.setCountyId(dto.getCountyId());
        order.setDetailAddress(dto.getDetailAddress());
        order.setEmail(dto.getEmail());
        order.setFreight(dto.getFreight());
        order.setFullAddress(dto.getFullAddress());
        order.setInvoice(dto.getInvoice());
        order.setInvoiceTitle(dto.getInvoiceTitle());
        order.setLogisticsCompany(dto.getLogisticsCompany());
        order.setLogisticsNo(dto.getLogisticsNo());
        order.setMemo(dto.getMemo());
        order.setMobile(dto.getMobile());
        order.setName(dto.getName());
        order.setOrderId(dto.getOrderId());
        order.setParentOrderId(dto.getParentOrderId());//母订单编号
        order.setPaymentPrice(dto.getPaymentPrice());
        order.setPaymentType(dto.getPaymentType());
        order.setPaymentTime(dto.getPaymentTime());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setPhone(dto.getPhone());
        order.setProvinceId(dto.getProvinceId());
        order.setSellerId(dto.getSellerId());
        order.setShipmentType(dto.getShipmentType());
        order.setShopId(dto.getShopId());
        order.setTotalDiscount(dto.getTotalDiscount());
        order.setTotalPrice(dto.getTotalPrice());
        order.setPayPeriod(dto.getPayPeriod());
        order.setPaymentType(dto.getPaymentType());
        order.setState(dto.getState());//订单状态
        order.setPromoCode(dto.getPromoCode());//优惠码
        order.setOrderType(dto.getOrderType()); //订单类型
        order.setAuditorId(dto.getAuditorId());
        order.setApproveStatus(dto.getApproveStatus());
        order.setPlatformId(dto.getPlatformId());
        order.setCentralPurchasing(dto.getCentralPurchasing());
        order.setIntegral(dto.getIntegral());
        order.setExchangeRate(dto.getExchangeRate());
        order.setIntegralDiscount(dto.getIntegralDiscount());
        order.setIsService(dto.getIsService());
        order.setCreatorId(dto.getCreatorId());
        return order;
    }
    @Override
    public ExecuteResult<DataGrid<TradeOrdersDTO>> queryOrders(TradeOrdersQueryInDTO inDTO,
                                                               Pager<TradeOrdersQueryInDTO> pager) {
        logger.info("==============开始订单查询=====================");
        ExecuteResult<DataGrid<TradeOrdersDTO>> result = new ExecuteResult<DataGrid<TradeOrdersDTO>>();
        try {
            DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
            //查询父级订单 暂时不用
//			List<TradeOrdersDTO> parentOrders = this.tradeOrdersDAO.queryParentOrders(inDTO,pager);
            List<TradeOrdersDTO> subOrders=new ArrayList<TradeOrdersDTO>();
            Long count=null;
            //查询子订单
            if(inDTO.isNotApproved()){//不走审核流程
            	subOrders = this.tradeOrdersDAO.queryTradeOrders(inDTO, pager);
            	count = this.tradeOrdersDAO.queryTradeOrdersCount(inDTO);
            }else{//走审核流程
            	subOrders = this.tradeApprovedOrdersDAO.queryTradeOrders(inDTO, pager);
            	count = this.tradeApprovedOrdersDAO.queryTradeOrdersCount(inDTO);
            }
            List<TradeOrderItemsDTO> items = null;
            for (TradeOrdersDTO order : subOrders) {
                //查询子订单行项目
                items = this.tradeOrderItemsDAO.queryItemsByOrderId(order.getOrderId());
                order.setItems(items);
            }
            // 根据运送方式分组子订单行项目
            for (TradeOrdersDTO order : subOrders) {
            	for(TradeOrderItemsDTO itemsDTO : order.getItems()){
            		if(order.getGroupItems().get(itemsDTO.getDeliveryType()) != null){
            			order.getGroupItems().get(itemsDTO.getDeliveryType()).add(itemsDTO);
            		} else{
            			List<TradeOrderItemsDTO> itemsDTOs = new ArrayList<TradeOrderItemsDTO>();
            			itemsDTOs.add(itemsDTO);
            			order.getGroupItems().put(itemsDTO.getDeliveryType(), itemsDTOs);
            		}
            	}
            }
            dataGrid.setTotal(count);
            dataGrid.setRows(subOrders);
            result.setResult(dataGrid);
        } catch (Exception e) {
            logger.error("执行方法【queryOrders】报错：{}", e);
            result.addErrorMessage("执行方法【queryOrders】报错：" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            logger.info("==============结束订单查询=====================");
        }
        return result;
    }


    @Override
    public ExecuteResult<Long> queryOrderQty(TradeOrdersQueryInDTO inDTO) {
        logger.info("==============开始订单数量查询=====================");
        ExecuteResult<Long> result = new ExecuteResult<Long>();
        try {
//        	inDTO.setDeleted(1);
            result.setResult(this.tradeOrdersDAO.queryTradeOrdersCount(inDTO));
        } catch (Exception e) {
            logger.error("执行方法【queryOrderQty】报错：{}", e);
            result.addErrorMessage("执行方法【queryOrderQty】报错：" + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("==============结束订单数量查询=====================");
        return result;
    }

    @Override
    public ExecuteResult<String> modifyOrderStatus(String orderId, Integer orderStatus) {
        ExecuteResult<String> result = new ExecuteResult<String>();
        logger.info("==============开始修改订单状态=====================");
        try {
            if (orderStatus == null || StringUtils.isBlank(orderId)) {
                result.addErrorMessage("参数为空！");
                return result;
            }
            TradeOrdersQueryInDTO param = new TradeOrdersQueryInDTO();
            param.setOrderId(orderId);
            TradeOrdersDTO order = this.getOrderById(orderId).getResult();
            if (order == null) {
                result.addErrorMessage("订单不存在！");
                return result;
            }
            if ("0".equals(order.getParentOrderId())) {
                result.addErrorMessage("父级订单不能修改状态！");
                return result;
            }
            TradeOrders dbParam = new TradeOrders();
            dbParam.setOrderId(orderId);
            dbParam.setState(orderStatus);
            if (TradeOrdersStateEnum.FINISHED.getCode() == orderStatus) {
                //完成订单
                dbParam.setOrderFinishTime(new Date());
            } else if (TradeOrdersStateEnum.EVALUATING.getCode() == orderStatus) {
                //订单确认收货
            	dbParam.setConfirmReceiptTime(new Date());
            	//如果是积分兑换订单，则不生成结算单
            	if(PayMethodEnum.PAY_INTEGRAL.getCode() != order.getPaymentMethod()){
            		//查询退款情况
                	Integer[] states = {TradeReturnStatusEnum.SUCCESS.getCode(),TradeReturnStatusEnum.REFUNDING.getCode(),TradeReturnStatusEnum.PLATFORMTOREFUND.getCode(),TradeReturnStatusEnum.PLATFORMDEALING.getCode(),};
    				List<RefundOrder> refundOrderList = refundOrderDAO.selectRefundOrderByOrderIdAndRefundGoodsStatus(order.getOrderId(), states);//.searchByOrderIdAndState(tradeOrder.getOrderId(),states);
                	ExecuteResult<String> re = this.statementExportService.createSettleDetail(order,refundOrderList);
                	if(!re.isSuccess()){
                		return re;
                	}
                	//更新促销活动结算单中促销退款总金额
					ExecuteResult<ActivityStatementsDTO> ext=activityStatementSerice.queryActivityStatementsByOrderId(orderId);//查询该订单的平台优惠总金额
					if(ext!=null&&ext.getResult()!=null){
						//退款促销优惠金额=退款总金额/订单实际支付总金额   * 平台优惠总金额
						ActivityStatementsDTO activityStatementsDTO=ext.getResult();
						activityStatementsDTO.setState(1);//更新为有效
						activityStatementSerice.updateActivityStatement(activityStatementsDTO);
					}
            	}
                //增加积分
//                this.addCredit(order.getPaymentPrice(), order.getBuyerId());
                try {
					this.addIntegral(order.getOrderId());
				} catch (Exception e) {
					logger.error("执行方法【addIntegral】报错：{}", e);
				}
                
            } else if(TradeOrdersStateEnum.CANCELED.getCode() == orderStatus){//取消订单只能是未支付的
            	if(order.getPaid() == 1 && order.getState() < TradeOrdersStateEnum.CONFIRMING.getCode()){
            		dbParam.setYn(2);
            		if (order.getIntegral() != null) {
            			// 如果下单使用了积分，取消订单需要退还积分
	            		UserIntegralTrajectoryDTO userIntegralTrajectoryDTO = new UserIntegralTrajectoryDTO();
	                	userIntegralTrajectoryDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_CANCEL.getCode());
	                	userIntegralTrajectoryDTO.setOrderId(order.getOrderId());
	                	userIntegralTrajectoryDTO.setIntegralValue(order.getIntegral().longValue());
	                	userIntegralTrajectoryDTO.setUsingTime(new Date());
	                	userIntegralTrajectoryDTO.setUserId(order.getBuyerId());
	                	userIntegralTrajectoryDTO.setShopId(order.getShopId());
	                	userIntegralTrajectoryDTO.setInsertBy(order.getBuyerId());
	                	userIntegralTrajectoryDTO.setInsertTime(new Date());
	                	ExecuteResult<UserIntegralTrajectoryDTO> ReturnIntegralsAfterCancelOrder = userIntegralTrajectoryService.addUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO);
	                	if(!ReturnIntegralsAfterCancelOrder.isSuccess()){
	                		result.setErrorMessages(result.getErrorMessages());
	                	}
            		}
            	}else{
            		result.addErrorMessage("只能取消未支付成功的订单！");
            		return result;
            	}
            }
            this.tradeOrdersDAO.update(dbParam);
            
        } catch (Exception e) {
            logger.error("执行方法【modifyOrderStatus】报错：{}", e);
            result.addErrorMessage("执行方法【modifyOrderStatus】报错：" + e.getMessage());
            throw new RuntimeException(e);
        }
        logger.info("==============结束修改订单状态=====================");
        return result;
    }

    @Override
    public ExecuteResult<String> deleteOrderById(String orderId) {
        logger.info("=============开始删除订单================");
        ExecuteResult<String> result = new ExecuteResult<String>();
        if (orderId == null) {
            result.addErrorMessage("订单号为空！");
            return result;
        }
        TradeOrdersDTO dbOrder = this.tradeOrdersDAO.queryTradeOrderById(orderId);
        if (dbOrder == null) {
            result.addErrorMessage("订单不存在");
            return result;
        }

        if (TradeOrdersStateEnum.FINISHED.getCode() == dbOrder.getState()
                || TradeOrdersStateEnum.CANCELED.getCode() == dbOrder.getState()
                || TradeOrdersStateEnum.COLSE.getCode() == dbOrder.getState()) {
            //只有完成的订单才能删除
            TradeOrders param = new TradeOrders();
            param.setOrderId(orderId);
            param.setDeleted(2);
            param.setDeleteTime(new Date());
            this.tradeOrdersDAO.update(param);
        } else {
            result.addErrorMessage("该订单未完成、未取消或未关闭，不能删除！");
            return result;
        }

        logger.info("=============结束删除订单================");
        return result;
    }

    @Override
    public ExecuteResult<TradeOrdersDTO> getOrderById(String orderId) {
        logger.info("=============开始按订单号查询订单================");
        ExecuteResult<TradeOrdersDTO> result = new ExecuteResult<TradeOrdersDTO>();
        TradeOrdersDTO dbOrder = this.tradeOrdersDAO.queryTradeOrderById(orderId);
        if (dbOrder == null) {
            result.addErrorMessage("该订单不存在！");
            return result;
        }
        dbOrder.setItems(this.tradeOrderItemsDAO.queryItemsByOrderId(orderId));
        result.setResult(dbOrder);
        logger.info("=============结束按订单号查询订单================");
        return result;
    }

    @Override
    public ExecuteResult<String> modifyOrderPayStatus(TradeOrdersDTO inDTO) {
        logger.info("=============开始修改订单支付状态================");
        ExecuteResult<String> result = new ExecuteResult<String>();
        if (inDTO == null) {
            result.addErrorMessage("参数为空！");
            return result;
        }
        if (inDTO.getOrderId() == null) {
            result.addErrorMessage("订单号为空！");
            return result;
        }
        if (inDTO.getPaymentType() == null) {
            result.addErrorMessage("支付类型为空！");
            return result;
        }
        TradeOrdersDTO dbOrder = this.getOrderById(inDTO.getOrderId()).getResult();
        if (dbOrder == null) {
            result.addErrorMessage("该订单不存在！");
            return result;
        }
        if(dbOrder.getPaid()==2){
        	result.addErrorMessage("该订单已经支付完成！");
            return result;
        }
        TradeOrders dbParam = new TradeOrders();
        dbParam.setOrderId(inDTO.getOrderId());
        dbParam.setPaymentType(inDTO.getPaymentType());
        dbParam.setPaymentTime(new Date());
        dbParam.setPaid(2);
        if(1==dbOrder.getShipmentType()){//非延期付款的订单
        	if("0".equals(dbOrder.getParentOrderId())){//父订单
        		TradeOrdersQueryInDTO param = new TradeOrdersQueryInDTO();
        		param.setParentOrderId(inDTO.getOrderId());
        		ExecuteResult<DataGrid<TradeOrdersDTO>> results=this.queryOrders(param, null);
        		if(results.isSuccess()&&null!=results.getResult().getRows()){
        			for(TradeOrdersDTO dto:results.getResult().getRows()){
        				//扣减库存
        	            this.deductInventory(dto.getItems());
        	            // 集采订单
        	            this.addCentralPurchasingOrder(dto);
        			}
        		}
        	}else{
        		//扣减库存
                this.deductInventory(dbOrder.getItems());
                // 集采订单
	            this.addCentralPurchasingOrder(dbOrder);
        	}
            //修改订单支付状态
            dbParam.setState(TradeOrdersStateEnum.DELIVERING.getCode());
            this.tradeOrdersDAO.update(dbParam);
        } else if(2==dbOrder.getShipmentType()){//延期付款的订单
        	//修改订单支付状态
            this.tradeOrdersDAO.update(dbParam);
            // 集采订单
            this.addCentralPurchasingOrder(dbOrder);
        }
        logger.info("=============结束修改订单支付状态================");
        return result;
    }
    
    /**
     * 
     * <p>Discription:[添加集采订单]</p>
     * Created on 2015年12月18日
     * @param tradeOrdersDTO 子订单
     * @author:[宋文斌]
     */
    private void addCentralPurchasingOrder(TradeOrdersDTO tradeOrdersDTO){
    	// 判断当前订单是否为集采订单
    	
    	if(tradeOrdersDTO.getCentralPurchasing()!=null&&tradeOrdersDTO.getCentralPurchasing() == 1){
    		for (TradeOrderItemsDTO orderItem : tradeOrdersDTO.getItems()) {
    			CentralPurchasingRefOrderDTO centralPurchasingRefOrderDTO = new CentralPurchasingRefOrderDTO();
    			centralPurchasingRefOrderDTO.setActivitesDetailsId(orderItem.getActivitesDetailsId());
    			centralPurchasingRefOrderDTO.setInsertBy(tradeOrdersDTO.getBuyerId());
    			centralPurchasingRefOrderDTO.setInsertTime(new Date());
    			centralPurchasingRefOrderDTO.setItemId(orderItem.getItemId());
    			centralPurchasingRefOrderDTO.setOrderId(tradeOrdersDTO.getOrderId());
    			centralPurchasingRefOrderDTO.setPurchaseNum(orderItem.getNum());
    			centralPurchasingRefOrderDTO.setPurchasePrice(orderItem.getPayPrice());
    			centralPurchasingRefOrderDTO.setSkuId(orderItem.getSkuId());
    			centralPurchasingRefOrderDTO.setUpdateBy(tradeOrdersDTO.getBuyerId());
    			centralPurchasingRefOrderDTO.setUpdateTime(new Date());
    			ExecuteResult<Boolean> result = centralPurchasingRefOrderExportService.addCentralPurchasingRefOrder(centralPurchasingRefOrderDTO);
    			if(result.isSuccess()){
    				// 更新集采活动已付款人数
    				centralPurchasingExportService.plusPaidNum(orderItem.getActivitesDetailsId(), 1);
    			}
    		}
    	}
    }
    /**
     * 
     * <p>Discription:[扣减库存]</p>
     * Created on 2015-5-12
     * @param items
     * @author:wangcs
     */
    private void deductInventory(List<TradeOrderItemsDTO> items) {
    	logger.info("=============开始更新库存================");
    	for (TradeOrderItemsDTO orderItem : items) {
            List<InventoryModifyDTO> skuInves = new ArrayList<InventoryModifyDTO>();
            TradeInventoryDTO skuInv = this.tradeInventoryExportService.queryTradeInventoryBySkuId(orderItem.getSkuId()).getResult();
            InventoryModifyDTO mSkuInv = new InventoryModifyDTO();
            mSkuInv.setSkuId(orderItem.getSkuId());
            mSkuInv.setTotalInventory(skuInv.getTotalInventory() - orderItem.getNum());
            skuInves.add(mSkuInv);
            //TODO 扣减库存  如果不成功  进行回滚
            this.tradeInventoryExportService.modifyInventoryByIds(skuInves);
           
            //套装商品扣减组合套装服务商品的sku库存
            TradeOrderItemsPackageDTO tradeOrderItemsPackageDTO=new TradeOrderItemsPackageDTO();
            List<Integer> arrInt=new ArrayList<Integer>();
            arrInt.add(ItemAddSourceEnum.AUXILIARYMATERIAL.getCode());//辅材包
            arrInt.add(ItemAddSourceEnum.BASICSERVICE.getCode());//基础服务商品
            arrInt.add(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());//增值服务商品
            tradeOrderItemsPackageDTO.setItemId(orderItem.getItemId());
            tradeOrderItemsPackageDTO.setAddSources(arrInt);
            ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> res=tradeOrderItemsPackageExportService.queryTradeOrderItemsPackageDTOs(tradeOrderItemsPackageDTO, null);
            if(!res.getResult().getRows().isEmpty()&&res.getResult().getTotal()>0){
            	for(TradeOrderItemsPackageDTO dTO: res.getResult().getRows()){
            		List<InventoryModifyDTO> infos = new ArrayList<InventoryModifyDTO>();
            		TradeInventoryDTO inventory = this.tradeInventoryExportService.queryTradeInventoryBySkuId(dTO.getSubSkuId()).getResult();
                    InventoryModifyDTO modiyinventory = new InventoryModifyDTO();
                    modiyinventory.setSkuId(dTO.getSkuId());
                    modiyinventory.setTotalInventory(inventory.getTotalInventory() - dTO.getSubNum());
                    infos.add(modiyinventory);
                    this.tradeInventoryExportService.modifyInventoryByIds(infos);
            	}
            }
            logger.info("=============更新库存结束================");
        }
	}
	@Override
    public ExecuteResult<String> modifyOrderPrice(TradeOrdersDTO orderDTO) {
        ExecuteResult<String> result = new ExecuteResult<String>();
        logger.info("=============开始订单改价================");
        //校验必填项目
        if (orderDTO.getOrderId() == null) {
            result.addErrorMessage("订单号不能为空！");
            return result;
        }
        //查询数据库订单
        TradeOrdersDTO dbOrder = this.getOrderById(orderDTO.getOrderId()).getResult();
        if (dbOrder == null) {
            result.addErrorMessage("订单不存在！");
            return result;
        }
        //计算价格
        BigDecimal totalPrice = new BigDecimal("0");
        BigDecimal totalDiscount = new BigDecimal("0");
        //更新行项目价格
        Map<Long, TradeOrderItemsDTO> paramMap = new HashMap<Long, TradeOrderItemsDTO>();
        for (TradeOrderItemsDTO itemDTO : orderDTO.getItems()) {
            paramMap.put(itemDTO.getSkuId(), itemDTO);
        }
        TradeOrderItems paramItem = null;
        for (TradeOrderItemsDTO dbItem : dbOrder.getItems()) {
            TradeOrderItemsDTO paraItem = paramMap.get(dbItem.getSkuId());
            paraItem = paraItem==null? dbItem:paraItem;
            BigDecimal primitivePriceTotal = dbItem.getPrimitivePrice();//原始价格
            BigDecimal discountTotal = paraItem.getPrimitivePrice().subtract(paraItem.getPayPriceTotal());//修改后的优惠总价格
            if (primitivePriceTotal == null) {
                // primitivePriceTotal = dbItem.getPrimitivePrice().multiply(new BigDecimal(dbItem.getNum()));
            	 primitivePriceTotal = dbItem.getPayPriceTotal();
            }
            if (discountTotal == null) {
            	if(dbItem.getPromotionDiscount()==null){
            		dbItem.setPromotionDiscount(new BigDecimal("0"));
            	}
                discountTotal = dbItem.getPromotionDiscount();//.multiply(new BigDecimal(dbItem.getNum()));
            }
            if (paramMap.containsKey(dbItem.getSkuId())) {//存在改价的Item
//				dbItem.setPrimitivePrice(paramDTO.getPrimitivePrice()==null? dbItem.getPrimitivePrice():paramDTO.getPrimitivePrice());//原价
//				dbItem.setPromotionDiscount(paramDTO.getPromotionDiscount()==null? dbItem.getPromotionDiscount():paramDTO.getPromotionDiscount());//优惠总价格
                dbItem.setPayPriceTotal(primitivePriceTotal.subtract(discountTotal));//实际支付金额
                paramItem = new TradeOrderItems();
                paramItem.setOrderId(dbItem.getOrderId());
                paramItem.setSkuId(dbItem.getSkuId());
                paramItem.setPromotionDiscount(discountTotal);
                paramItem.setPayPriceTotal(dbItem.getPayPriceTotal());
                this.tradeOrderItemsDAO.update(paramItem);
            }
            totalPrice = totalPrice.add(primitivePriceTotal);
            totalDiscount = totalDiscount.add(discountTotal);
        }
        BigDecimal freight = orderDTO.getFreight() == null ? dbOrder.getFreight() : orderDTO.getFreight();
//        orderDTO.setTotalPrice(totalPrice);//优惠前总价格
        orderDTO.setTotalDiscount(totalDiscount);//优惠总金额
        orderDTO.setPaymentPrice(totalPrice.subtract(totalDiscount).add(freight));//实际支付金额
        orderDTO.setFreight(freight);//运费
        //修改价格
        this.tradeOrdersDAO.updateSubOrderPrice(orderDTO);
        
        //获取父订单信息
        TradeOrdersDTO parentOrderDTO = this.getOrderById(dbOrder.getParentOrderId()).getResult();
        if(parentOrderDTO != null){
        	BigDecimal lessPaymentPrice = dbOrder.getPaymentPrice().subtract(orderDTO.getPaymentPrice());//改价前实际支付总金额=商品金额-优惠金额+运费   与现在的差价
            BigDecimal lessTotalPrice = dbOrder.getTotalPrice().subtract(totalPrice);//改价前商品总额   与现在的差价
            BigDecimal lessDiscount = dbOrder.getTotalDiscount().subtract(orderDTO.getTotalDiscount());//改价前优惠金额   与现在的差价
            BigDecimal lessFreight = dbOrder.getFreight().subtract(orderDTO.getFreight());//改价前运费
            parentOrderDTO.setPaymentPrice(parentOrderDTO.getPaymentPrice().subtract(lessPaymentPrice));
            parentOrderDTO.setTotalPrice(parentOrderDTO.getTotalPrice().subtract(lessTotalPrice));
            parentOrderDTO.setFreight(parentOrderDTO.getFreight().subtract(lessFreight));
            //修改父订单的价格
            this.tradeOrdersDAO.updateSubOrderPrice(parentOrderDTO);
        }
        
        //修改价格日志
        TradeOrderPriceHistory his = new TradeOrderPriceHistory();
        his.setOrderId(orderDTO.getOrderId());
        his.setAfterFreight(orderDTO.getFreight());
        his.setAfterTotalDiscount(orderDTO.getTotalDiscount());
        his.setAfterPaymentPrice(orderDTO.getPaymentPrice());
        his.setAfterTotalPrice(orderDTO.getTotalPrice());
        his.setBeforeFreight(dbOrder.getFreight());
        his.setBeforePaymentPrice(dbOrder.getPaymentPrice());
        his.setBeforeTotalDiscount(dbOrder.getTotalDiscount());
        his.setBeforeTotalPrice(dbOrder.getTotalPrice());
        his.setBuyerId(dbOrder.getBuyerId());
        his.setSellerId(dbOrder.getSellerId());
        his.setOperationUser(orderDTO.getSellerId());
        his.setShopId(dbOrder.getShopId());
        this.tradeOrderPriceHistoryDAO.add(his);
        logger.info("=============结束订单改价================");
        return result;
    }

	@Override
	public void confirmOrderAuto() {
		try {
			TradeOrdersQueryInDTO tradeOrders=new TradeOrdersQueryInDTO();
			tradeOrders.setDelay(3);
			tradeOrders.setState(3);
			tradeOrders.setRefund(1);
			List<TradeOrdersDTO> orderList = tradeOrdersDAO.queryTradeOrders(tradeOrders, null);
			outer:for (TradeOrdersDTO to : orderList) {//自动确认收货过滤掉服务商品
				    List<TradeOrderItemsDTO> itemDtos = this.tradeOrderItemsDAO.queryItemsByOrderId(to.getOrderId());
					for(TradeOrderItemsDTO dto:itemDtos){
						ExecuteResult<ItemBaseDTO> res=itemExportService.getItemBaseInfoById(dto.getItemId());
						if(ItemAddSourceEnum.BASICSERVICE.getCode()==res.getResult().getAddSource()||
						   ItemAddSourceEnum.AUXILIARYMATERIAL.getCode()==res.getResult().getAddSource()||
						   ItemAddSourceEnum.VALUEADDEDSERVICE.getCode()==res.getResult().getAddSource()){
						          continue outer;
						}
					}
				this.modifyOrderStatus(to.getOrderId(), 4);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public ExecuteResult<String> delayDelivery(String orderId) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			TradeOrdersDTO to = tradeOrdersDAO.queryTradeOrderById(orderId);
			if(to==null){
				result.setResult("订单不存在");
				result.setResultMessage("error");
			}else{
			Calendar c = Calendar.getInstance();  
			c.setTime(to.getDeliverTime()); 
			c.add(Calendar.DATE, 14);  
			Date date = c.getTime();  
			to.setDelayOverTime(date);
			tradeOrdersDAO.updateDelayDelivery(to);
			result.setResultMessage("success");
			}
		} catch (Exception e) {
			logger.error("执行方法【delayDelivery】报错：{}",e);
			result.addErrorMessage("执行方法【delayDelivery】报错："+e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}


    @Override
    public ExecuteResult<TradeOrdersDTO> updateTradeOrdersDTOSelective(TradeOrdersDTO orderDTO) {
        ExecuteResult<TradeOrdersDTO> res = new ExecuteResult<TradeOrdersDTO>();
        TradeOrders dbParam = new TradeOrders();
        dbParam.setOrderId(orderDTO.getOrderId());
        dbParam.setRefund(orderDTO.getRefund());
        dbParam.setState(orderDTO.getState());
        dbParam.setUpdateTime(new Date());
        dbParam.setRefundTime(orderDTO.getRefundTime());
        dbParam.setLocked(orderDTO.getLocked());
        dbParam.setLockTime(orderDTO.getLockTime());
        dbParam.setAfterService(orderDTO.getAfterService());
        dbParam.setDeliverTime(orderDTO.getDeliverTime());
//        dbParam.setState(orderDTO.getState());
        this.tradeOrdersDAO.update(dbParam);
        res.setResult(orderDTO);
        return res;
    }

	@Override
	public ExecuteResult<String> deleteTradeOrders(String orderId) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
		inDTO.setParentOrderId(orderId);
		List<TradeOrdersDTO> orders = this.tradeOrdersDAO.queryTradeOrders(inDTO, null);
		if(orders.size()<=0){
			return result;
		}
		//删除订单
		this.tradeOrdersDAO.delete(orderId);
		//删除行项目
		this.tradeOrderItemsDAO.deleteOrders(orders);
		return result;
	}

	@Override
	public ExecuteResult<String> modifyEvaluationStatus(TradeOrdersDTO inDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if(inDTO==null){
			result.addErrorMessage("参数为空！");
			return result;
		}
		if (inDTO.getOrderId() == null ) {
			result.addErrorMessage("订单号为空！");
			return result;
		}
		if (inDTO.getSellerEvaluate() == null && inDTO.getEvaluate() == null) {
			result.addErrorMessage("评价标记不能都为空！");
			return result;
		}
		if (inDTO.getSellerEvaluate() != null && inDTO.getEvaluate() != null) {
			result.addErrorMessage("评价标记只能有一个有值！");
			return result;
		}
		TradeOrdersDTO order = this.tradeOrdersDAO.queryTradeOrderById(inDTO.getOrderId());
		if(order==null){
			result.addErrorMessage("订单不存在！");
			return result;
		}
		TradeOrders param = new TradeOrders();
		if(inDTO.getEvaluate()!=null){//买家评价
			if(TradeOrdersStateEnum.EVALUATING.getCode()!=order.getState()){//非待评价的订单不能修改
				result.addErrorMessage("订单不是待评价状态无法修改评价状态！");
				return result;
			}
			param.setEvaluate(inDTO.getEvaluate());
			param.setState(TradeOrdersStateEnum.FINISHED.getCode());
		}else if(inDTO.getSellerEvaluate()!=null){//卖家评价
			param.setSellerEvaluate(inDTO.getSellerEvaluate());
			if(TradeOrdersStateEnum.FINISHED.getCode()!=order.getState()){//非待评价的订单不能修改
				result.addErrorMessage("订单不是完成状态无法修改评价状态！");
				return result;
			}else if(1==order.getEvaluate()){
				result.addErrorMessage("买家未对订单做修改不能修改评价状态！");
				return result;
			}
		}
		param.setOrderId(inDTO.getOrderId());
		this.tradeOrdersDAO.update(param);
		return result;
	}

	@Override
	public ExecuteResult<String> modifyLogisticsInfo(TradeOrdersDTO inDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if(inDTO == null){
			result.addErrorMessage("参数不能为空！");
			return result;
		}
		if(inDTO.getOrderId()==null){
			result.addErrorMessage("订单号不能为空！");
			return result;
		}
		if(StringUtils.isBlank(inDTO.getLogisticsCompany())){
			result.addErrorMessage("物流信息不能为空！");
			return result;
		}
		TradeOrders param = new TradeOrders();
		param.setOrderId(inDTO.getOrderId());
		param.setLogisticsCompany(inDTO.getLogisticsCompany());
		param.setLogisticsNo(inDTO.getLogisticsNo());
		param.setLogisticsRemark(inDTO.getLogisticsRemark());
		this.tradeOrdersDAO.update(param);
		return result;
	}

	@Override
	public ExecuteResult<List<TradeOrderItemsDTO>> countNumber(String  contractNo) {
		ExecuteResult<List<TradeOrderItemsDTO>> result = new ExecuteResult<List<TradeOrderItemsDTO>>();
		result.setResult(this.tradeOrderItemsDAO.countNumber(contractNo));
		return result;
	}
	
	@Override
	public ExecuteResult<List<TradeOrderItemsDTO>> countCost(String  contractNo) {
		ExecuteResult<List<TradeOrderItemsDTO>> result = new ExecuteResult<List<TradeOrderItemsDTO>>();
		result.setResult(this.tradeOrderItemsDAO.countCost(contractNo));
		return result;
	}
	//插入促销活动记录信息
	private void setActivityRecordDTO(TradeOrdersDTO order){
		for(ActivityRecordDTO activityRecordDTO:order.getActivityRecordDTOs()){
			activityRecordDTO.setOrderId(order.getOrderId());
			// 判断该订单是否使用了积分
			if (activityRecordDTO.getType() == ActivityTypeEnum.PLATFORMINTEGRATION.getStatus()) {
				// 添加积分使用记录
				UserIntegralTrajectoryDTO userIntegralTrajectoryDTO = new UserIntegralTrajectoryDTO();
				userIntegralTrajectoryDTO.setInsertBy(order.getBuyerId());
				userIntegralTrajectoryDTO.setInsertTime(new Date());
				userIntegralTrajectoryDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_USING.getCode());
				userIntegralTrajectoryDTO.setIntegralValue(0 - order.getIntegral().longValue());
				userIntegralTrajectoryDTO.setOrderId(order.getOrderId());
				userIntegralTrajectoryDTO.setShopId(order.getShopId());
				userIntegralTrajectoryDTO.setUpdateBy(order.getBuyerId());
				userIntegralTrajectoryDTO.setUpdateTime(new Date());
				userIntegralTrajectoryDTO.setUserId(order.getBuyerId());
				userIntegralTrajectoryDTO.setUsingTime(new Date());
				ExecuteResult<UserIntegralTrajectoryDTO> executeResult = userIntegralTrajectoryService.addUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO);
				if (executeResult.isSuccess() && executeResult.getResult() != null) {
					// 设置活动记录的活动ID为积分使用记录的ID
					activityRecordDTO.setPromotionId(String.valueOf(executeResult.getResult().getId()));
				}
			}
			ExecuteResult<String> res = activityStatementSerice.addActivityRecord(activityRecordDTO);
		}
  }
	//操作活动结算信息
	private void setActivityStatementsDTO(TradeOrdersDTO order,BigDecimal amount){
		ActivityRecordDTO activityRecordDTO=new ActivityRecordDTO();
		activityRecordDTO.setOrderId(order.getOrderId());
		List<Integer> promotionTypes=new ArrayList<Integer>();
		promotionTypes.add(ActivityTypeEnum.PLATFORMCOUPONS.getStatus());//平台优惠券活动
		promotionTypes.add(ActivityTypeEnum.PLATFORMFULLREDUCTION.getStatus());//平台满减活动
		promotionTypes.add(ActivityTypeEnum.PLATFORMINTEGRATION.getStatus());//平台积分活动
		promotionTypes.add(ActivityTypeEnum.PLATFORMMARKDOWN.getStatus());//平台直降活动
		activityRecordDTO.setPromotionTypes(promotionTypes);
		ExecuteResult<DataGrid<ActivityRecordDTO>> res=activityStatementSerice.queryActivityRecordDTO(activityRecordDTO, null);
		if(res.getResult()!=null&&res.getResult().getTotal()>0){
			for(ActivityRecordDTO record:res.getResult().getRows()){
				amount=amount.add(record.getDiscountAmount());//优惠金额累加
			}
			//插入促销活动结算信息
			ActivityStatementsDTO activityStatementsDTO=new ActivityStatementsDTO();
			activityStatementsDTO.setOrderId(order.getOrderId());//订单id
			activityStatementsDTO.setShopId(order.getShopId());//店铺id
			activityStatementsDTO.setState(2);//无效，确认收货更新改表变为有效
			activityStatementsDTO.setTotalDiscountAmount(amount);//活动总优惠金额
			activityStatementsDTO.setCreateTime(new Date());//创建时间
			activityStatementsDTO.setUpdateTime(new Date());//更新时间
			ExecuteResult<String> ext=activityStatementSerice.addActivityStatement(activityStatementsDTO);
		}
	}
	//插入活动明细信息
	private void setTradeOrderItemsDiscountDTO(TradeOrdersDTO order, TradeOrderItems tradeOrderItems) {
		if (order.getTradeOrderItemsDiscountDTO() != null && order.getTradeOrderItemsDiscountDTO().size() > 0) {
			for (TradeOrderItemsDiscountDTO dto : order.getTradeOrderItemsDiscountDTO()) {
				dto.setOrderId(order.getOrderId());
				if (dto.getOrderId().equals(tradeOrderItems.getOrderId())
						&& dto.getSkuId().equals(tradeOrderItems.getSkuId())) {
					dto.setOrderItemsId(tradeOrderItems.getId());
					tradeOrderItemsDiscountExportService.createOrderItemsDiscount(dto);
				}
				
			}
		}
	}
	
	public void updateOrderDefaultEvaluate(Date date){
		logger.info("\n 方法[{}]，入参：[{}]", "TradeOrderExportServiceImpl-updateOrderDefaultEvaluate", JSONObject.toJSONString(date));
		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
		inDTO.setDeleted(1);
		inDTO.setState(4);	//查询待评价的订单
		ExecuteResult<DataGrid<TradeOrdersDTO>> orders = tradeOrderExportService.queryOrders(inDTO, null);
		if(orders.isSuccess() && orders.getResult()!=null){
			
			List<TradeOrdersDTO> list = orders.getResult().getRows();
			for (TradeOrdersDTO tradeOrdersDTO : list) {
				if(tradeOrdersDTO.getConfirmReceiptTime()==null || date.before(tradeOrdersDTO.getConfirmReceiptTime()) ){
					continue;
				}
				//-----------------店铺评价------------------
				ShopEvaluationDTO shopEvaluationDTO =new ShopEvaluationDTO();
				shopEvaluationDTO.setResource("1");								//1：默认评价 2:手动评价
				shopEvaluationDTO.setShopArrivalScope(5);	//  到货速度评分
				shopEvaluationDTO.setShopServiceScope(5);	//  服务态度评分
				shopEvaluationDTO.setShopDescriptionScope(5);	//  描述相符评分
				shopEvaluationDTO.setOrderId(tradeOrdersDTO.getOrderId());	//订单号
				shopEvaluationDTO.setUserId(tradeOrdersDTO.getBuyerId());	//买家id
				shopEvaluationDTO.setByShopId(tradeOrdersDTO.getShopId());	//店铺id
				ExecuteResult<ShopEvaluationDTO> shopEvaluationResult = shopEvaluationService.addShopEvaluation(shopEvaluationDTO);
				if( !shopEvaluationResult.isSuccess() ){
					logger.info("订单号：\""+tradeOrdersDTO.getOrderId()+"\"店铺评价失败!错误信息:"+shopEvaluationResult.getErrorMessages());
					continue;
				} 
				
				//-------------------商品评价------------------
				List<ItemEvaluationDTO> itemEvaluationDTOList = new ArrayList<ItemEvaluationDTO>();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrdersDTO.getItems()){
					//组装数据
					ItemEvaluationDTO itemEvaluation = new ItemEvaluationDTO();
					itemEvaluation.setOrderId(tradeOrdersDTO.getOrderId());		//订单ID
					itemEvaluation.setUserId(tradeOrdersDTO.getBuyerId());		//评价人
					itemEvaluation.setByUserId(tradeOrdersDTO.getSellerId());	//被评价人
					itemEvaluation.setByShopId(tradeOrdersDTO.getShopId());	//被评价店铺id
					itemEvaluation.setItemId(tradeOrderItemsDTO.getItemId());							//商品id
					itemEvaluation.setSkuId(tradeOrderItemsDTO.getSkuId());								//商品sku
					itemEvaluation.setType("1");									//类型 1:来自买家的评论 2:来自卖家的评价 3:售后评价
					itemEvaluation.setResource("1");								//1：默认评价 2:手动评价
					itemEvaluation.setSkuScope(5);						//sku评分
					itemEvaluation.setContent("好评！");						
					itemEvaluationDTOList.add(itemEvaluation);
				}
				
				//批量提交并且返回实体list
				ExecuteResult<List<ItemEvaluationDTO>> itemEvaluationResult = itemEvaluationService.addItemEvaluationsReturnList(itemEvaluationDTOList);
				if(!itemEvaluationResult.isSuccess()){
					logger.info("订单号：\""+tradeOrdersDTO.getOrderId()+"\"商品评价失败!错误信息:"+itemEvaluationResult.getErrorMessages());
					continue;
				}
				
				//更新：订单状态
				TradeOrdersDTO updateDTO=new TradeOrdersDTO();
				updateDTO.setOrderId(tradeOrdersDTO.getOrderId());
				updateDTO.setEvaluate(2);
				ExecuteResult<String> result = modifyEvaluationStatus(updateDTO);
				if(!result.isSuccess()){
					logger.info("订单号：\""+tradeOrdersDTO.getOrderId()+"\"订单状态修改失败!错误信息:"+result.getErrorMessages());
				}
				
			}
		}
	}
}
