package com.camelot.storecenter.dto.emums;

/**
 * <p>Description: [运费模板枚举]</p>
 * Created on 2015年10月27日
 *
 * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ShopFreightTemplateEnum {

    /**
     * <p>Description: [计价方式枚举]</p>
     * Created on 2015年10月27日
     *
     * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
     * @version 1.0
     *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    public enum ShopDeliveryTypeEnum{
    NUMBERS(1, "按件数"), WEIGHT(2, "按重量"),VOLUME(3,"按体积");
    private int code;
    private String label;
    ShopDeliveryTypeEnum(int code, String label) {
        this.code = code;
        this.label = label;
    }
    public int getCode() {
        return code;
    }
    public String getLabel() {
        return label;
    }
    }

    /**
     * <p>Description: [运送方式枚举]</p>
     * Created on 2015年10月27日
     *
     * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
     * @version 1.0
     *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    public enum ShopPreferentialWayEnum{
        EXPRESSAGE(1, "快递"), EMS(2, "EMS"), SURFEMAIL(3,"平邮");
        private int code;
        private String label;
        ShopPreferentialWayEnum(int code, String label) {
            this.code = code;
            this.label = label;
        }
        public int getCode() {
            return code;
        }
        public String getLabel() {
            return label;
        }
    }

    /**
     * <p>Description: [优惠方式枚举]</p>
     * Created on 2015年10月27日
     *
     * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
     * @version 1.0
     *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    public enum ShopDeliveryTimeEnum{
        DELIVERY("4个小时内发货,8个小时内发货,12个小时内发货,16个小时内发货,20个小时内发货,1天内发货,2天内发货,3天内发货,5天内发货,7天内发货,8天内发货,10天内发货,12天内发货,15天内发货,17天内发货,20天内发货,25天内发货,30天内发货,35天内发货,45天内发货,确认订单后第一时间为您发货");
        private String label;
        ShopDeliveryTimeEnum(String label) {
            this.label = label;
        }
        public String getLabel() {
            return label;
        }
    }
}
