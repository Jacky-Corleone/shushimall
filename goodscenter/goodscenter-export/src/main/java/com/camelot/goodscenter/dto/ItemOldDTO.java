package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author
 * 
 */
public class ItemOldDTO  implements Serializable {
    
		private static final long serialVersionUID =1l;
		private java.lang.Long id;//  主键
		private java.lang.Long itemId;//  商品编码
		private java.lang.Long cid;//类目ID 三级类目
		private java.lang.String cName;//类目名称
		private java.lang.Long sellerId;//卖家id
		private java.lang.String itemName;//  二手商品名称
		private java.lang.Integer recency;//  新旧程度 全新10   非全新9
		private BigDecimal priceOld;//  原价
		private BigDecimal price;//  现价
		private BigDecimal freight;//  运费
		private java.lang.String sellerTel;//  卖家联系方式
		private java.lang.String sellerName;//  卖家姓名
		private java.lang.String provinceCode;//  省编码
		private java.lang.String provinceName;//  省名称
		private java.lang.String cityCode;//  市编码
		private java.lang.String cityName;//  市名称
		private java.lang.String districtCode;//  区编码
		private java.lang.String districtName;//  区名称
		private java.lang.String describeUr;//  商品描述url，存在jfs中
		private java.lang.String describeDetail;//商品详情
		private java.util.Date created;//  创建时间
		private java.util.Date modified;//  修改时间
		private java.lang.String comment;//  批注
		private java.lang.String platformUserId;//  批注人id
		private java.lang.Integer status;//  商品状态,1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁 8删除
		
		
		// 表外后加字段
		private Integer[] statuss;//状态数组 为空时查询状态为4在售的商品
		private String imgUrl;//查询商品图片get 0 
		private java.util.Date createdstr;//  创建时间开始
		private java.util.Date createdend;//  创建时间结束
		private Long[] sellerIds; //用户ID组
		private String timeDifference;// 当前时间与发布时间差
		
		
		

		public String getTimeDifference() {
			return timeDifference;
		}
		public void setTimeDifference(String timeDifference) {
			this.timeDifference = timeDifference;
		}
		public void setcName(java.lang.String cName) {
			this.cName = cName;
		}
		public java.lang.String getcName() {
			return cName;
		}
		public Integer[] getStatuss() {
			return statuss;
		}

		public void setStatuss(Integer[] statuss) {
			this.statuss = statuss;
		}

		public java.util.Date getCreatedstr() {
			return createdstr;
		}

		public void setCreatedstr(java.util.Date createdstr) {
			this.createdstr = createdstr;
		}

		public java.util.Date getCreatedend() {
			return createdend;
		}

		public void setCreatedend(java.util.Date createdend) {
			this.createdend = createdend;
		}

		public Long[] getSellerIds() {
			return sellerIds;
		}

		public void setSellerIds(Long[] sellerIds) {
			this.sellerIds = sellerIds;
		}

		public java.lang.Long getSellerId() {
			return sellerId;
		}

		public void setSellerId(java.lang.Long sellerId) {
			this.sellerId = sellerId;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public java.lang.Long getCid() {
			return cid;
		}

		public void setCid(java.lang.Long cid) {
			this.cid = cid;
		}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getItemId() {
		return this.itemId;
	}

	public void setItemId(java.lang.Long value) {
		this.itemId = value;
	}
	
	public java.lang.String getItemName() {
		return this.itemName;
	}

	public void setItemName(java.lang.String value) {
		this.itemName = value;
	}
	
	public java.lang.Integer getRecency() {
		return this.recency;
	}

	public void setRecency(java.lang.Integer value) {
		this.recency = value;
	}
	
	public BigDecimal getPriceOld() {
		return priceOld;
	}

	public void setPriceOld(BigDecimal priceOld) {
		this.priceOld = priceOld;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public java.lang.String getSellerTel() {
		return this.sellerTel;
	}

	public void setSellerTel(java.lang.String value) {
		this.sellerTel = value;
	}
	
	public java.lang.String getSellerName() {
		return this.sellerName;
	}

	public void setSellerName(java.lang.String value) {
		this.sellerName = value;
	}
	
	public java.lang.String getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceCode(java.lang.String value) {
		this.provinceCode = value;
	}
	
	public java.lang.String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(java.lang.String value) {
		this.provinceName = value;
	}
	
	public java.lang.String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(java.lang.String value) {
		this.cityCode = value;
	}
	
	public java.lang.String getCityName() {
		return this.cityName;
	}

	public void setCityName(java.lang.String value) {
		this.cityName = value;
	}
	
	public java.lang.String getDistrictCode() {
		return this.districtCode;
	}

	public void setDistrictCode(java.lang.String value) {
		this.districtCode = value;
	}
	
	public java.lang.String getDistrictName() {
		return this.districtName;
	}

	public void setDistrictName(java.lang.String value) {
		this.districtName = value;
	}
	
	public java.lang.String getDescribeUr() {
		return this.describeUr;
	}

	public void setDescribeUr(java.lang.String value) {
		this.describeUr = value;
	}
	
	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}
	
	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}
	
	public java.lang.String getComment() {
		return this.comment;
	}

	public void setComment(java.lang.String value) {
		this.comment = value;
	}
	
	public java.lang.String getPlatformUserId() {
		return this.platformUserId;
	}

	public void setPlatformUserId(java.lang.String value) {
		this.platformUserId = value;
	}
	
	public java.lang.String getDescribeDetail() {
		return describeDetail;
	}
	public void setDescribeDetail(java.lang.String describeDetail) {
		this.describeDetail = describeDetail;
	}
	public java.lang.Integer getStatus() {
		return this.status;
	}

	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	

}

