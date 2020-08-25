package com.camelot.mall.delivery;

/**
 * 
 * @author 咸玛瑞
 * 快递100枚举类
 */
public enum DeliveryEnum {
	

	CODE_200("提交成功", "200"),
	CODE_500("服务器错误", "500"), 
	CODE_501("重复订阅", "501"),
	CODE_600("您不是合法的订阅者", "600"),  
	CODE_700("订阅数据错误", "700"), 
	CODE_701("拒绝订阅的快递公司", "701"); 
    
	// 成员变量  
    private String message;  
    private String returnCode;  
	
    // 构造方法  
    private DeliveryEnum(String message, String returnCode) {  
        this.message = message;  
        this.returnCode = returnCode;  
    }  
    
    // 普通方法  
    public static String getMessage(String returnCode) {  
        for (DeliveryEnum c : DeliveryEnum.values()) {  
            if (c.getReturnCode() .equals(returnCode) ) {  
                return c.message;  
            }  
        }  
        return null;  
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

       
}
