package com.camelot.ecm.settlement;

import com.camelot.common.ExcelField;

public class ActivityStatements {
	// 序号
	private String num;
	// 订单id
	private String orderId;
	// 店铺id
	private String shopId;
	// 店铺名称
	private String shopName;
	// 创建时间
	private String createTime;
	// 更新时间
	private String updateTime;
	// 活动总优惠金额
	private String totalDiscountAmount;
	// 总退款金额
	private String totalRefundAmount;
	// 活动结算金额
	private String totalSettleAmount;
	// 状态1：有效 2：无效
	private String state;

	@ExcelField(title = "序号", align = 2, sort = 10)
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@ExcelField(title = "订单编号", align = 2, sort = 20)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@ExcelField(title = "店铺ID", align = 2, sort = 30)
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	@ExcelField(title = "店铺名称", align = 2, sort = 40)
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@ExcelField(title = "创建时间", align = 2, sort = 50)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ExcelField(title = "更新时间", align = 2, sort = 60)
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	@ExcelField(title = "活动优惠总金额", align = 2, sort = 70)
	public String getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(String totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	@ExcelField(title = "活动退款总金额", align = 2, sort = 80)
	public String getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(String totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	@ExcelField(title = "活动结算总金额", align = 2, sort = 90)
	public String getTotalSettleAmount() {
		return totalSettleAmount;
	}

	public void setTotalSettleAmount(String totalSettleAmount) {
		this.totalSettleAmount = totalSettleAmount;
	}
	
	@ExcelField(title = "状态", align = 2, sort = 100)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
