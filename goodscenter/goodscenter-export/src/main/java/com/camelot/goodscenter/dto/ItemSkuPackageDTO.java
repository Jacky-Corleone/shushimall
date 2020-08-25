package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 套装商品与子商品关联关系DTO
 * </p>
 * 
 * Created on 2016年2月17日
 * 
 * @author <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class ItemSkuPackageDTO implements Serializable {

	private static final long serialVersionUID = 171519557988063115L;
    private Long packageId;//主键id
	private Long packageItemId; // 套装商品itemId
	private Long packageSkuId; // 套装商品skuId
	private Long subItemId; // 子商品itemId
	private Long subSkuId; // 子商品skuId
	private Integer addSource; // 商品类型（1：普通商品 2：平台上传 3：套装商品 4：基础服务商品 5：增值服务商品 6：辅助材料商品）
	private Integer subNum;//组合商品中来源sku的数量
	private List<Long> subSkuIds; // 一组套装子skuId
	
	private List<Integer> addSources;//商品来源集合

	public ItemSkuPackageDTO() {
	}

	public ItemSkuPackageDTO(Long packageItemId, Long packageSkuId, List<Long> subSkuIds) {
		this.packageItemId = packageItemId;
		this.packageSkuId = packageSkuId;
		this.subSkuIds = subSkuIds;
	}

	public Long getPackageItemId() {
		return packageItemId;
	}

	public void setPackageItemId(Long packageItemId) {
		this.packageItemId = packageItemId;
	}

	public Long getPackageSkuId() {
		return packageSkuId;
	}

	public void setPackageSkuId(Long packageSkuId) {
		this.packageSkuId = packageSkuId;
	}

	public List<Long> getSubSkuIds() {
		return subSkuIds;
	}

	public void setSubSkuIds(List<Long> subSkuIds) {
		this.subSkuIds = subSkuIds;
	}

	public void setSubSkuIds(Long... subSkuIds) {
		this.subSkuIds = Arrays.asList(subSkuIds);
	}

	public Long getSubItemId() {
		return subItemId;
	}

	public void setSubItemId(Long subItemId) {
		this.subItemId = subItemId;
	}

	public Long getSubSkuId() {
		return subSkuId;
	}

	public void setSubSkuId(Long subSkuId) {
		this.subSkuId = subSkuId;
	}

	public Integer getAddSource() {
		return addSource;
	}

	public void setAddSource(Integer addSource) {
		this.addSource = addSource;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	public List<Integer> getAddSources() {
		return addSources;
	}

	public void setAddSources(List<Integer> addSources) {
		this.addSources = addSources;
	}

	public Integer getSubNum() {
		return subNum;
	}

	public void setSubNum(Integer subNum) {
		this.subNum = subNum;
	}
	
}
