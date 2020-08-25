package com.camelot.openplatform.common.enums;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.camelot.openplatform.common.constants.Constants;

/**
 * <p>
 * Description: [会员等级]
 * </p>
 * Created on 2015年12月10日
 * 
 * @author <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum VipLevelEnum {
	REGULAR(1, "红象会员"), BRONZE(2, "铜象会员"), SILVER(3, "银象会员"), GOLD(4, "金象会员"), DIAMOND(5, "钻石象会员");

	private Integer id;
	private String name;

	private VipLevelEnum(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static VipLevelEnum getEnumByName(String name) {
		for (VipLevelEnum vipLevelEnum : VipLevelEnum.values()) {
			if (vipLevelEnum.getName().equals(name)) {
				return vipLevelEnum;
			}
		}
		return null;
	}

	public static VipLevelEnum getEnumById(Integer id) {
		for (VipLevelEnum vipLevelEnum : VipLevelEnum.values()) {
			if (vipLevelEnum.getId().compareTo(id)==0) {
				return vipLevelEnum;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		getEnumById(null);
	}

	/**
	 * 获取会员等级中文名称的字符串
	 * <p>Discription:[根据会员等级id拼接的字符串，获取会员等级中文名称的拼接字符串]</p>
	 * <p>例如：</p>
	 * <p>"1,2,3"——>"普通会员 铜牌会员 银牌会员"</p>
	 * <p>"3,5"——>"银牌会员 钻石会员"</p>
	 * Created on 2015年12月13日
	 * @param idStr 会员等级id拼接的字符串，可携带分隔符，如"1,2,3"、"1,4,5"等
	 * @return 会员等级中文名称的拼接字符串
	 * @author:[顾雨]
	 */
	public static String getNameStrByIdStr(String idStr) {
		if (StringUtils.isEmpty(idStr)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (VipLevelEnum vipLevel : VipLevelEnum.values()) {
			if (idStr.contains(vipLevel.id + ""))
				sb.append(vipLevel.name).append(" ");
		}
		
		if (sb.length()!=0) {
			sb.delete(sb.length()-1,sb.length());
		}

		return sb.toString();
	}
	
	/**
	 * 根据成长值获取会员等级
	 * @param growthValue
	 * @return
	 */
	public static VipLevelEnum getVipLevelEnumByGrowthValue(BigDecimal growthValue){
		int code = 1;
		if(growthValue.compareTo(Constants.RED_ELEPHANT)<0){
			code = 1;
		}else if(growthValue.compareTo(Constants.COPPER_ELEPHANT)<0){
			code = 2;
		}else if(growthValue.compareTo(Constants.SILVER_ELEPHANT)<0){
			code = 3;
		}else if(growthValue.compareTo(Constants.GOLD_ELEPHANT)<0){
			code = 4;
		}else{
			code = 5;
		}
		for (VipLevelEnum vipLevelEnum : VipLevelEnum.values()) {
			if (vipLevelEnum.getId().equals(code)) {
				return vipLevelEnum;
			}
		}
		return null;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
