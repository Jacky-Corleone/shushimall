package com.camelot.usercenter.enums;


public class UserEnums {

	/**
	 * 用户类型
	 *
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-4
	 */
	public enum UserType {
		ORDINARY(1, "普通用户"), BUYER(2, "买家用户"), SELLER(3, "卖家用户"), PLATFORM(4,
				"平台用户");

		private int code;
		private String label;

		UserType(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public static UserType getEnumBycode(Integer code) {
			if (code != null) {
				for (UserType userType : UserType.values()) {
					if (userType.getCode() == code) {
						return userType;
					}
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}
	}
	public enum UserStatus{
		NOR_UNVERIFIED(1, "普通用户待验证"),
        NOR_VERIFIED(2, "普通用户验证通过"),
        BUYER_UNAUDIT(3, "买家未审核"),
        BUYER_AUDIT(4, "买家审核通过"),
        SELLER_UNAUDIT(5, "卖家未审核"),
        SELLER_AUDIT(6,"卖家审核通过");

		private int code;
		private String label;

		UserStatus(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public static UserStatus getEnumByOrdinal(Integer ordinal) {
			if (ordinal != null) {
				for (UserStatus userStatus : UserStatus.values()) {
					if (userStatus.getCode() == ordinal) {
						return userStatus;
					}
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}
	}


	/**
	 * 用户审核状态
	 *
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-4
	 */
	public enum UserAuditStatus {
		REJECT(0, "驳回"), AUTH(1, "待审核"), PASS(2, "审核通过");

		private int code;
		private String label;

		UserAuditStatus(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public static UserAuditStatus getEnumByOrdinal(Integer ordinal) {
			if (ordinal != null) {
				for (UserAuditStatus userAuditStatus : UserAuditStatus.values()) {
					if (userAuditStatus.getCode() == ordinal) {
						return userAuditStatus;
					}
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}

	}

	/**
	 * 实地认证审核状态
	 */
	public enum FieldIdentificationAuditStatus {
		UNACCEPT(0, "待受理"), AUTH(1, "待审核"), PASS(2, "审核通过"), REJECT(3, "审核驳回"), ACCEPTED(4, "已受理");

		private int code;
		private String label;

		FieldIdentificationAuditStatus(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}

		public static String getLabelByCode(int code){
			FieldIdentificationAuditStatus[] enums = FieldIdentificationAuditStatus.values();
			int enumsLength = enums.length;
			for(int i=0; i<enumsLength; i++){
				if(enums[i].getCode() == code){
					return enums[i].label;
				}
			}
			return null;
		}

	}

	/**
	 * 实地认证图片类型
	 */
	public enum FieldIdentificationPictureType {
		WORKSHOP(0, "厂房/产品"), DOOR(1, "企业大门"), OFFICIAL(2, "办公场所"), CERTIFICATE(3, "其他证书");

		private int code;
		private String label;

		FieldIdentificationPictureType(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}

		public static String getLabelByCode(int code){
			FieldIdentificationPictureType[] enums = FieldIdentificationPictureType.values();
			int enumsLength = enums.length;
			for(int i=0; i<enumsLength; i++){
				if(enums[i].getCode() == code){
					return enums[i].label;
				}
			}
			return null;
		}

	}

	/**
	 * 用户扩展类型
	 *
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-4
	 */
	public enum UserExtendsType {
		BUSINESS("用户营业执照"), ORGANIZATION("用户组织机构"), TAX("用户税务"), ACCOUNT("用户账户"),CTIBANK("中信账号"),MANAGE("公司运营");

		private String label;

		UserExtendsType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	/**
	 * 合同类型
	 *
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-5
	 */
	public enum ContractType {
		RECRUITMENT(1, "商家入驻"), RENEWAL(2, "续签合同"), CHANGE(3, "调整类目");

		private int code;
		private String label;

		ContractType(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public static ContractType getEnumByCode(Integer code) {
			if (code != null) {
				for (ContractType contractType : ContractType.values()) {
					if (contractType.getCode() == code) {
						return contractType;
					}
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}
	}

    /**
     * 用户日志类型
     *
     * @Description -
     * @author - learrings
     * @createDate - 2015-3-5  1   2
     */
    public enum UserOperaType {
        RZSH(1, "入驻审核"), XGSH(2, "修改审核"), ZFZHSH(3, "子父级账号申请调换审核");

        private int code;
        private String label;

        UserOperaType(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static UserOperaType getEnumByCode(Integer code) {
            if (code != null) {
                for (UserOperaType userOperaType : UserOperaType.values()) {
                    if (userOperaType.getCode() == code) {
                        return userOperaType;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }
    }

	/**
	 *  合同状态
	 *
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-5
	 */
	public enum ContractStatus {

		REJECT (0, "已驳回" ), UPLOAD(1, "待上传" ), UNPASS(2, "待审核" ), PASS(3, "已确认" );

		private int code;
		private String label;

		ContractStatus(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public static ContractStatus getEnumByCode(Integer code) {
			if (code != null) {
				for (ContractStatus contractStatus : ContractStatus.values()) {
					if (contractStatus.getCode() == code) {
						return contractStatus;
					}
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}

	}

	/**
	 *  设备类型
	 *
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-7
	 */
	public enum DeviceType {
		PREPRESS(10, "印前设备"), PRINTING(20, "印刷设备"),  POSTPRESS (30, "印后设备");

		private int code;
		private String label;

		DeviceType(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public static ContractStatus getEnumByCode(Integer code) {
			if (code != null) {
				for (ContractStatus contractStatus : ContractStatus.values()) {
					if (contractStatus.getCode() == code) {
						return contractStatus;
					}
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}

	}

    /**
     *  购买类型用途
     *
     * @Description -
     * @author - learrings
     * @createDate - 2015-3-7
     */
    public enum UsePurchaseType{
        PREPRESS(10, "DEVICE"), DIG(20, "数码通讯"),   OFFICE(30, "办公用品");

        private int code;
        private String label;

        UsePurchaseType(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static UsePurchaseType getEnumByCode(Integer code) {
            if (code != null) {
                for (UsePurchaseType usePurchaseType : UsePurchaseType.values()) {
                    if (usePurchaseType.getCode() == code) {
                        return usePurchaseType;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }

    /**
     * 企业人数
     */
    public enum CompanyPeopleNum{
        TO49(10, "1-49"), TO99(20, "50-99"),TO499 (30, "100-499"),TO999 (40, "500-999"),THAN1000 (50, "1000以上");

        private int code;
        private String label;

        CompanyPeopleNum(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static CompanyPeopleNum getEnumByCode(Integer code) {
            if (code != null) {
                for (CompanyPeopleNum companyPeopleNum : CompanyPeopleNum.values()) {
                    if (companyPeopleNum.getCode() == code) {
                        return companyPeopleNum;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }

    /**
     * 公司行业
     */
    public enum CompanyIndustry{
        BOOK(10, "图书出版"), NEWS(20, "报社出版");

        private int code;
        private String label;

        CompanyIndustry(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static CompanyIndustry getEnumByCode(Integer code) {
            if (code != null) {
                for (CompanyIndustry companyIndustry : CompanyIndustry.values()) {
                    if (companyIndustry.getCode() == code) {
                        return companyIndustry;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }
    /**
     * 经营规模
     */
    public enum BusinessScale{
        LESS1000(10, "1000万以下"), TO3000(20, "1000-3000万"),TO5000(30, "3000-5000万"),TO1E(40, "5000万到1亿"),THEN1E(50, "1亿以上");

        private int code;
        private String label;

        BusinessScale(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static BusinessScale getEnumByCode(Integer code) {
            if (code != null) {
                for (BusinessScale businessScale : BusinessScale.values()) {
                    if (businessScale.getCode() == code) {
                        return businessScale;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }

    /**
     * 公司性质
     */
    public enum CompanyQualt{
        ZF(10, "政府机关/事业单位"), GY(20, "国营"),SY(30, "私营"),ZWHZ(40, "中外合资"),WZ(50, "外资"),QT(60, "其他");

        private int code;
        private String label;

        CompanyQualt(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static CompanyQualt getEnumByCode(Integer code) {
            if (code != null) {
                for (CompanyQualt companyQualt : CompanyQualt.values()) {
                    if (companyQualt.getCode() == code) {
                        return companyQualt;
                    }
                }
            }
            return null;
        }

        public static String getLabelByCode(int code){
        	CompanyQualt[] enums = CompanyQualt.values();
			int enumsLength = enums.length;
			for(int i=0; i<enumsLength; i++){
				if(enums[i].getCode() == code){
					return enums[i].label;
				}
			}
			return null;
		}

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }

    /**
     * 所在部门
     */
    public enum DepartMent{
        XZB(10, "行政部"), CGB(20, "采购部"),SBB(30, "设备部"),JSB(40, "技术部"),SCB(50, "生产部"),QT(60, "其他");

        private int code;
        private String label;

        DepartMent(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static DepartMent getEnumByCode(Integer code) {
            if (code != null) {
                for (DepartMent departMent : DepartMent.values()) {
                    if (departMent.getCode() == code) {
                        return departMent;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }
    /**
     * taxType 纳税类型代码
     */

    public enum TaxType{
        NON(10, "0%"), THREE(20, "3%"),SIX(30, "6%"),SEVEN(40, "7%"),ELEVEN(50, "11%"),THITEEN(60, "13%"),THITEENNO(60, "图书13%免税"),SIXTEEN(60, "17%");

        private int code;
        private String label;

        TaxType(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static TaxType getEnumByCode(Integer code) {
            if (code != null) {
                for (TaxType taxType : TaxType.values()) {
                    if (taxType.getCode() == code) {
                        return taxType;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }
    /**
     * 经销商类型
     */

    public enum DealerType{
        PP(10, "品牌商"), DL(20, "经销商");

        private int code;
        private String label;

        DealerType(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static DealerType getEnumByCode(Integer code) {
            if (code != null) {
                for (DealerType dealerType : DealerType.values()) {
                    if (dealerType.getCode() == code) {
                        return dealerType;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }
    /**
     * ERP类型
     */
    public enum ERPType{
        ZYERP(10, "自有ERP"), DSFERP(20, "第三方ERP代运营"),QT(30, "无");

        private int code;
        private String label;

        ERPType(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static ERPType getEnumByCode(Integer code) {
            if (code != null) {
                for (ERPType eRPType : ERPType.values()) {
                    if (eRPType.getCode() == code) {
                        return eRPType;
                    }
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

    }
}
