package com.camelot.ecm.goodscenter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;

public class SkuUtil {

	/**
	 * 获取一个sku属性值的名称
	 * @param attributes
	 * @return
	 */
	public static String getAttrNameByIdArr(List<ItemAttr> itemAttrList, String attributes, Integer index){
		if(itemAttrList==null || StringUtils.isBlank(attributes) || index==null){
			return "";
		}
		String[] ids = attributes.split("\\;");  //属性名id1:属性值id2;属性名id1:属性值id2
		String attr = ids[index];    //属性名id:属性值id
		String attrNameId = attr.split("\\:")[0];//属性名id
		String attrValId = attr.split("\\:")[1];//属性值id
		for(int i=0; i<itemAttrList.size(); i++){
			ItemAttr ia = itemAttrList.get(i);
			for(ItemAttrValue av : ia.getValues()){
				if(attrNameId.equals(String.valueOf(ia.getId())) && attrValId.equals(String.valueOf(av.getId()))){
					return av.getName();
				}
			}
		}
		return "哈哈s哈哈";
	}
}
