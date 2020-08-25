package com.camelot.pricecenter.dto.outdto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.camelot.pricecenter.dto.ProductAttrDTO;
import com.camelot.pricecenter.dto.PromotionDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;

/**
 * <p>
 * Description: 描述该类概要功能介绍
 * </p>
 * Created on 2015年3月4日
 * 
 * @author <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ProductOutPriceDTO implements Serializable {
	private static final long serialVersionUID = 1736213044844797716L;
	private String uid; // 当前登录用户ID
	private String ctoken; // 临时购物车Token
	private String regionId; // 区域编码
	private boolean checked = true;

	private Long sellerId; // 卖家ID
	private Long shopId; // 店铺ID
	private Long itemId; // 商品ID
	private Long skuId; // SKU ID
	private String title; // 商品名称
	private String src; // 商品图片
	private BigDecimal weight; // 重量
	private String weightUnit; // 重量单位
	private BigDecimal volume; // 体积

	private BigDecimal skuPrice; // 商品价格
	private Integer quantity; // 数量
	private BigDecimal total; // 总金额

	private List<ProductAttrDTO> attrs; // 商品属性

	private BigDecimal payPrice; // 付款单价
	private BigDecimal payTotal; // 付款总额

	private Long promId; // 选中活动ID
	private Integer promType; // 活动类型
	private List<PromotionDTO> promotions; // 商品营销活动

	private Long cid; // 商品类目ID
	private Integer qty; // 库存
	private Integer status; // 商品状态

	private Integer unusualState; // null: 正常商品或商品未选中； 1：商品已下架；2：不符合店铺规则, 3：库存不足, 4: 商品暂无报价！,5：集采商品不能和正常商品一起结算，6该集采商品不存在,7该集采商品不在活动有效期之内,8集采数量超过了每个用户可购买的最大数量,9超过了每个用户可购买的最大限制 10 买家不能购买自己店铺的商品 11 抱歉！该套装中存在非销售的组合单品/服务，暂时不能购买。12抱歉！该套装中存在非在售的组合单品，暂时不能购买。
	private List<String> unusualMsg = new ArrayList<String>(); // 商品异常状态描述
	private boolean hasPrice; // 是否有报价

	private Long shopFreightTemplateId; // 运费模版ID
	private Integer deliveryType; // 运送方式：1快递，2EMS，3平邮
	// vip卡用到字段
	private BigDecimal discountTotal = BigDecimal.ZERO;// 商品的vip卡的折扣金额（包括余额充足及余额不足，余额充足下discountTotal和originalDiscount值相等）

	private String ignoreName;// vip卡过滤商品的名字

	private BigDecimal originalDiscount = BigDecimal.ZERO;// 余额充足的条件下折扣金额
	
	private Long activitesDetailsId; // 集采活动详情ID
	
	private Map<String,Long> map=new HashMap<String,Long>();//直降活动map ,key:店铺还是平台活动，value:活动id
	
	// 每个用户可购买的最大数量
    private Integer perPerchaseMaxNum;
    //优惠金额
    private BigDecimal discountAmount =BigDecimal.ZERO;
    // 优惠券优惠金额
    private BigDecimal couponDiscount = BigDecimal.ZERO;
	// 积分优惠金额
    private BigDecimal integralDiscount = BigDecimal.ZERO;
    private Integer addSource;//   1：普通商品 2：平台上传 3：套装商品 4：基础服务商品 5：增值服务商品 6：辅助材料商品
    private Map<Long,Long> valueAddedMap = new HashMap<Long,Long>();//增值服务,key=增值服务的itemId，value=增值服务的skuId
    private List<String> valueAddedSkuIds = new ArrayList<String>();//增值服务,value=增值服务的itemId-增值服务的skuId
    private List<ProductOutPriceDTO> valueAddedProducts = new ArrayList<ProductOutPriceDTO>();//套装商品增值服务信息
    private List<ProductOutPriceDTO> subProducts = new ArrayList<ProductOutPriceDTO>();//套装商品的单品信息
    private List<ProductInPriceDTO> auxiliaryProducts = new ArrayList<ProductInPriceDTO>();//套装商品的辅料信息
    private List<ProductInPriceDTO> basicProducts = new ArrayList<ProductInPriceDTO>();//套装商品的基础服务信息
    private List<ProductInPriceDTO> showBasicProducts = new ArrayList<ProductInPriceDTO>();//套装商品的基础服务信息(在购物车和订单核对页展示用)
    private List<ProductInPriceDTO> showAuxiliaryProducts = new ArrayList<ProductInPriceDTO>();//套装商品的辅料信息(在购物车和订单核对页展示用)

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCtoken() {
		return ctoken;
	}

	public void setCtoken(String ctoken) {
		this.ctoken = ctoken;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public List<ProductAttrDTO> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<ProductAttrDTO> attrs) {
		this.attrs = attrs;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getPromId() {
		return promId;
	}

	public void setPromId(Long promId) {
		this.promId = promId;
	}

	public List<PromotionDTO> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<PromotionDTO> promotions) {
		this.promotions = promotions;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public BigDecimal getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(BigDecimal payTotal) {
		this.payTotal = payTotal;
	}

	public Integer getPromType() {
		return promType;
	}

	public void setPromType(Integer promType) {
		this.promType = promType;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUnusualState() {
		return unusualState;
	}

	public void setUnusualState(Integer unusualState) {
		this.unusualState = unusualState;
	}

	public List<String> getUnusualMsg() {
		return unusualMsg;
	}

	public void setUnusualMsg(List<String> unusualMsg) {
		this.unusualMsg = unusualMsg;
	}

	public boolean isHasPrice() {
		return hasPrice;
	}

	public void setHasPrice(boolean hasPrice) {
		this.hasPrice = hasPrice;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public Long getShopFreightTemplateId() {
		return shopFreightTemplateId;
	}

	public void setShopFreightTemplateId(Long shopFreightTemplateId) {
		this.shopFreightTemplateId = shopFreightTemplateId;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public BigDecimal getDiscountTotal() {
		return discountTotal;
	}

	public void setDiscountTotal(BigDecimal discountTotal) {
		this.discountTotal = discountTotal;
	}

	public String getIgnoreName() {
		return ignoreName;
	}

	public void setIgnoreName(String ignoreName) {
		this.ignoreName = ignoreName;
	}

	public BigDecimal getOriginalDiscount() {
		return originalDiscount;
	}

	public void setOriginalDiscount(BigDecimal originalDiscount) {
		this.originalDiscount = originalDiscount;
	}

	public Long getActivitesDetailsId() {
		return activitesDetailsId;
	}

	public void setActivitesDetailsId(Long activitesDetailsId) {
		this.activitesDetailsId = activitesDetailsId;
	}

	public Map<String, Long> getMap() {
		return map;
	}

	public void setMap(Map<String, Long> map) {
		this.map = map;
	}

	public Integer getPerPerchaseMaxNum() {
		return perPerchaseMaxNum;
	}

	public void setPerPerchaseMaxNum(Integer perPerchaseMaxNum) {
		this.perPerchaseMaxNum = perPerchaseMaxNum;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public BigDecimal getIntegralDiscount() {
		return integralDiscount;
	}

	public void setIntegralDiscount(BigDecimal integralDiscount) {
		this.integralDiscount = integralDiscount;
	}

	public Integer getAddSource() {
		return addSource;
	}

	public void setAddSource(Integer addSource) {
		this.addSource = addSource;
	}

	public Map<Long, Long> getValueAddedMap() {
		return valueAddedMap;
	}

	public void setValueAddedMap(Map<Long, Long> valueAddedMap) {
		this.valueAddedMap = valueAddedMap;
	}

	public List<String> getValueAddedSkuIds() {
		return valueAddedSkuIds;
	}

	public void setValueAddedSkuIds(List<String> valueAddedSkuIds) {
		this.valueAddedSkuIds = valueAddedSkuIds;
	}

	public List<ProductOutPriceDTO> getSubProducts() {
		return subProducts;
	}

	public void setSubProducts(List<ProductOutPriceDTO> subProducts) {
		this.subProducts = subProducts;
	}

	public List<ProductOutPriceDTO> getValueAddedProducts() {
		return valueAddedProducts;
	}

	public void setValueAddedProducts(List<ProductOutPriceDTO> valueAddedProducts) {
		this.valueAddedProducts = valueAddedProducts;
	}

	public List<ProductInPriceDTO> getAuxiliaryProducts() {
		return auxiliaryProducts;
	}

	public void setAuxiliaryProducts(List<ProductInPriceDTO> auxiliaryProducts) {
		this.auxiliaryProducts = auxiliaryProducts;
	}

	public List<ProductInPriceDTO> getBasicProducts() {
		return basicProducts;
	}

	public void setBasicProducts(List<ProductInPriceDTO> basicProducts) {
		this.basicProducts = basicProducts;
	}

	public List<ProductInPriceDTO> getShowBasicProducts() {
		return showBasicProducts;
	}

	public void setShowBasicProducts(List<ProductInPriceDTO> showBasicProducts) {
		this.showBasicProducts = showBasicProducts;
	}

	public List<ProductInPriceDTO> getShowAuxiliaryProducts() {
		return showAuxiliaryProducts;
	}

	public void setShowAuxiliaryProducts(List<ProductInPriceDTO> showAuxiliaryProducts) {
		this.showAuxiliaryProducts = showAuxiliaryProducts;
	}
	
}
