package com.camelot.goodscenter.dto.enums;

import org.apache.commons.lang.StringUtils;


/**
 * <p>
 * Description: [户型]
 * </p>
 * Created on 2015年12月10日
 * 
 * @author <a href="mailto: luoshaohua@camelotchina.com">罗少华</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public enum HouseTypeEnum {
	ONEBEDROOM(11, "一室一厅"), TWOBEDROOM(21, "两室一厅"),TWOROOMSTWOHALL(22, "两室两厅"),THREEBEDROOM(31, "三室一厅"),
	THREEROOMSTWOHALL(32, "三室两厅"),FOURROOMSTWOHALL(42, "四室两厅"),FOURROOMSTHREEHALL(43, "四室三厅"),
	FIVEROOMSTWOHALL(52, "五室两厅"),FIVEROOMSThREEHALL(53, "五室三厅"),DOUBLEENTRYBUILDING(66, "复式楼"),
	VILLA(67,"别墅"),PACIFICOCEANLAYER(68,"大平层"),GUESTRESTAURANT(68,"客餐厅"),OTHERROOM(69,"其他房型");

	private Integer id;
	private String name;

	private HouseTypeEnum(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static HouseTypeEnum getEnumByName(String name) {
		for (HouseTypeEnum houseTypeEnum : HouseTypeEnum.values()) {
			if (houseTypeEnum.getName().equals(name)) {
				return houseTypeEnum;
			}
		}
		return null;
	}

	public static HouseTypeEnum getEnumById(Integer id) {
		for (HouseTypeEnum houseTypeEnum : HouseTypeEnum.values()) {
			if (houseTypeEnum.getId().compareTo(id)==0) {
				return houseTypeEnum;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		getEnumById(null);
	}

	/**
	 * 获取户型中文名称的字符串
	 * <p>Discription:[根据户型id拼接的字符串，获取户型中文名称的拼接字符串]</p>
	 * <p>例如：</p>
	 * <p>"11,21,31"——>"一室一厅, 两室一厅,三室一厅"</p>
	 * Created on 2015年12月13日
	 * @param idStr 户型id拼接的字符串，可携带分隔符，如"11,21,31"等
	 * @return 户型中文名称的拼接字符串
	 * @author:[罗少华]
	 */
	public static String getNameStrByIdStr(String idStr) {
		if (StringUtils.isEmpty(idStr)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (HouseTypeEnum houseType : HouseTypeEnum.values()) {
			if (idStr.contains(houseType.id + ""))
				sb.append(houseType.name).append(" ");
		}
		
		if (sb.length()!=0) {
			sb.delete(sb.length()-1,sb.length());
		}

		return sb.toString();
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
