package com.camelot.usercenter.enums;

public class CommonEnums {
	
	/**
	 * 通用状态
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-4
	 */
	public enum ComStatus {
		REJECT(0, "驳回"), AUTH(1, "待确认"), PASS(2, "已确认");

		private int code;
		private String label;

		ComStatus(int code, String label) {
			this.code = code;
			this.label = label;
		}

		public static ComStatus getEnumByCode(Integer code) {
			if (code != null) {
				for (ComStatus comStatus : ComStatus.values()) {
					if (comStatus.getCode() == code) {
						return comStatus;
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
     * 冻结状态
     */
    public enum FrozenStatus{
        FROZEN(1, "冻结"), UNFROZEN(0, "恢复");

        private int code;
        private String label;

        FrozenStatus(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static FrozenStatus getEnumByCode(Integer code) {
            if (code != null) {
                for (FrozenStatus frozenStatus : FrozenStatus.values()) {
                    if (frozenStatus.getCode() == code) {
                        return frozenStatus;
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
