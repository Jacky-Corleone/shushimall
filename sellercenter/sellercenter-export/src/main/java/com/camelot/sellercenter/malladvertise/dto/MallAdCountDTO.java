package com.camelot.sellercenter.malladvertise.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
/**
 * 广告点击量DTO
 * 
 * @author learrings
 * 
 */
public class MallAdCountDTO  implements Serializable {
    
	private static final long serialVersionUID = 6377548276789885414L;
	private java.lang.Long id;//  id
	private java.lang.Long mallAdId;//  广告id
	private Long tableType;//  广告隶属表
	private java.lang.Long adCount;//  广告链接点击次数
	private String clickDate;//  统计日期
	
	private String mallAdName;//广告名称
	private String mallAdType;//广告类型
	
	private List<Long> ids;//  id组
	private Long adCountMin;// 最大次数
	private Long adCountMax;// 最小次数
	private String clickDateBegin;//开始时间
	private String clickDateEnd;// 结束时间
	
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getMallAdId() {
		return mallAdId;
	}
	public void setMallAdId(java.lang.Long mallAdId) {
		this.mallAdId = mallAdId;
	}
	public Long getTableType() {
		return tableType;
	}
	public void setTableType(Long tableType) {
		this.tableType = tableType;
	}
	public java.lang.Long getAdCount() {
		return adCount;
	}
	public void setAdCount(java.lang.Long adCount) {
		this.adCount = adCount;
	}
	
	public String getClickDate() {
		return clickDate;
	}
	public void setClickDate(String clickDate) {
		this.clickDate = clickDate;
	}
	public String getMallAdName() {
		return mallAdName;
	}
	public void setMallAdName(String mallAdName) {
		this.mallAdName = mallAdName;
	}
	public String getMallAdType() {
		return mallAdType;
	}
	public void setMallAdType(String mallAdType) {
		this.mallAdType = mallAdType;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public Long getAdCountMin() {
		return adCountMin;
	}
	public void setAdCountMin(Long adCountMin) {
		this.adCountMin = adCountMin;
	}
	public Long getAdCountMax() {
		return adCountMax;
	}
	public void setAdCountMax(Long adCountMax) {
		this.adCountMax = adCountMax;
	}
	public String getClickDateBegin() {
		return clickDateBegin;
	}
	public void setClickDateBegin(String clickDateBegin) {
		this.clickDateBegin = clickDateBegin;
	}
	public String getClickDateEnd() {
		return clickDateEnd;
	}
	public void setClickDateEnd(String clickDateEnd) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(dateFormat.parse(clickDateEnd));
			calendar.add(Calendar.DATE, 1);
			this.clickDateEnd = dateFormat.format(calendar.getTime());
		} catch (ParseException e) {
		}
	}
	
}