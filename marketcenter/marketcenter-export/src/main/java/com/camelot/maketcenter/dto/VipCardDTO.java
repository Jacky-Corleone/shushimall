package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class VipCardDTO implements Serializable {
	//id
	private Long id;
	//vip卡号
	private Integer vip_id;
	//卖家ID
	private Long buyer_id;
	//折扣类型
	private String discount_type;
	//折扣率
	private String discount_percent;
	//余额
	private BigDecimal residual_amount;
	//起始日期
	private Date start_date;
	//结束日期
	private Date end_data;
	//创建日期
	private Date create_time;
	//状态 1-有效，2-无效，空值-未启用
	private Integer status;
	//备注
	private String memo;
	//忽略的商品类型
	private String ignore_type;
	//预留字段1
	private String obligate1;
	//预留字段2
	private String obligate2;
	//预留字段3
	private String obligate3;
	//有效时间
	private String effective_time;
	
	//忽略的商品类型集合
	private List<String> ignoreTypeArray;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVip_id() {
		return vip_id;
	}

	public void setVip_id(Integer vip_id) {
		this.vip_id = vip_id;
	}

	public Long getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(Long buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getDiscount_type() {
		return discount_type;
	}

	public void setDiscount_type(String discount_type) {
		this.discount_type = discount_type;
	}

	public String getDiscount_percent() {
		return discount_percent;
	}

	public void setDiscount_percent(String discount_percent) {
		this.discount_percent = discount_percent;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_data() {
		return end_data;
	}

	public void setEnd_data(Date end_data) {
		this.end_data = end_data;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIgnore_type() {
		return ignore_type;
	}

	public void setIgnore_type(String ignore_type) {
		this.ignore_type = ignore_type;
	}

	public String getObligate1() {
		return obligate1;
	}

	public void setObligate1(String obligate1) {
		this.obligate1 = obligate1;
	}

	public String getObligate2() {
		return obligate2;
	}

	public void setObligate2(String obligate2) {
		this.obligate2 = obligate2;
	}

	public String getObligate3() {
		return obligate3;
	}

	public void setObligate3(String obligate3) {
		this.obligate3 = obligate3;
	}

	public List<String> getIgnoreTypeArray() {
		return ignoreTypeArray;
	}

	public void setIgnoreTypeArray(List<String> ignoreTypeArray) {
		this.ignoreTypeArray = ignoreTypeArray;
	}

	public BigDecimal getResidual_amount() {
		return residual_amount;
	}

	public void setResidual_amount(BigDecimal residual_amount) {
		this.residual_amount = residual_amount;
	}

	public String getEffective_time() {
		return effective_time;
	}

	public void setEffective_time(String effective_time) {
		this.effective_time = effective_time;
	}
	
	
	
}
