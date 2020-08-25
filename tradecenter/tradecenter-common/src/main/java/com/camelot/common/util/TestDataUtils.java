package com.camelot.common.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;


/**
 *  测试数据模拟演示类，后期删除
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2014-12-18
 */
public class TestDataUtils {

	
    /**
     * 模拟演示银行通知参数
     * */
	public static Map<String, String> getPayResultData(String payBank){
    	Map<String, String> map = new HashMap<String, String>();
    	 JSONObject   jSONObject =JSONObject.parseObject(getStr(payBank));
    	 for ( String a:jSONObject.keySet()) {
    		 map.put(a, jSONObject.getString(a));
		}
    	return map;
    }	
    
    public static String getStr(String payBank){
    	String str="";
    	if("AP".equals(payBank)){
    		str="{\"buyer_email\":\"18306420578\",\"buyer_id\":\"2088702925441121\",\"discount\":\"0.00\",\"gmt_create\":\"2015-02-05 18:01:20\",\"gmt_payment\":\"2015-02-05 18:01:58\",\"is_total_fee_adjust\":\"N\",\"notify_id\":\"c9508eea2f469d5380ab3cce86b8ee692o\",\"notify_time\":\"2015-02-05 21:25:41\",\"notify_type\":\"trade_status_sync\",\"out_trade_no\":\"0000066638\",\"payment_type\":\"1\",\"price\":\"0.01\",\"quantity\":\"1\",\"seller_email\":\"dzswcw@wangfujing.com\",\"seller_id\":\"2088801546560023\",\"sign\":\"53701722103d6c448ac6551375211a54\",\"sign_type\":\"MD5\",\"subject\":\"小甜甜 芙蓉石镶锆石S925银耳钉\",\"total_fee\":\"0.01\",\"trade_no\":\"2015020500001000120049761141\",\"trade_status\":\"TRADE_SUCCESS\",\"use_coupon\":\"N\"}";   
    	}else if("WX".equals(payBank)){
    		str="{\"appid\":\"wx8460fee7faef47a5\",\"bank_type\":\"CFT\",\"cash_fee\":\"1\",\"fee_type\":\"CNY\",\"is_subscribe\":\"N\",\"mch_id\":\"10019196\",\"nonce_str\":\"2fd0fd3efa7c4cfb034317b21f3c2d93\",\"openid\":\"o3GOGjleAB-N0AynZeOTZx4DcPrc\",\"out_trade_no\":\"0000066639\",\"result_code\":\"SUCCESS\",\"return_code\":\"SUCCESS\",\"sign\":\"F4470F538CB9F639315DCD14E8CA5521\",\"time_end\":\"20150205180400\",\"total_fee\":\"1\",\"trade_type\":\"NATIVE\",\"transaction_id\":\"1003100380201502050014191652\"}"; 	
    	}else if("CB".equals(payBank)){
    		str="{\"v_oid\":\"30120150328000437\",\"v_pmode\":\"中行长城信用卡\",\"v_pstatus\":\"20\",\"v_pstring\":\"20\",\"v_amount\":\"0.3\",\"v_moneytype\":\"CNY\",\"v_md5str\":\"37524D1C0D941BA01E5B48F401936B26\",\"remark1\":\"remark1\",\"remark2\":\"remark2\"}"; 	
    	}else if("CB_MOBILE".equals(payBank)){
			str="{\"resp\":\"PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4NCjxDSElOQUJBTks+CiAgPFZFUlNJT04+MS4wLjA8L1ZFUlNJT04+CiAgPE1FUkNIQU5UPjExMDA5ODgwNzAwMjwvTUVSQ0hBTlQ+CiAgPFRFUk1JTkFMPjAwMDAwMDAxPC9URVJNSU5BTD4KICA8REFUQT5HaUxPLzJQRGRRbk94Q1UzYlIvNEFPZmRKYS90cXhFVzlkOHNCMHNYRVdQK25GUXBhbmxnOWNIbnBQcUZMVjlwajQ5ZXA5K0V6UXNMRkFPWXBTc0YvenM2NjhUSldKN2tWUjBkbWtTSE1hTmRGcTVPNUtzSS9kRTFxNVR1VVpwVnR2ODBoUmVLRWQrNUtRQnZock92V3lmTGJiNGFwek4rSitmMnNXeVJVSW8yWG1ma2NsUXhaZ1V4Vlo2WkJteHQ3OGNIZCtiZXpyVmUzZW5QVkRSZ0EzU3pkTnBaRUxkV216TG1NWkJIUTZQeEkrUjRnUW9ENUd0U09Ed3NFalhPT3h3TGpKVjl3RHpOMnE5YWlwMGVON2pKVDA3L3RxekxtUFhYU0xGZVBZVnhPdmNZTHFiYXNmQ3pUR01MbzJJck1aM2hCdlBvVzcya0xxY3pXNFRUWkYrRmxYdlI5bEVONG9QbEc1QUJBK0w1L0NsUmYxREtDYVoyUVE1ZlJ1VEVCb3RhU2pxMEJnTVVqcyt2OTNUUlV0ZlRKSkFqS1VPaHJDYmNtOVpkTzBZTTJDU1d3WkhWZmViYmNVWGdFNzRzZUdhTVUzQ1ljL01xUmgzQ1k4RWd2Zz09PC9EQVRBPgogIDxTSUdOPjVkNzMzNGE4ZjYzMzJjMGNjMTA4MzdiOGUyZDc5MmQ0PC9TSUdOPgo8L0NISU5BQkFOSz4=\"}";
		}else if("AP_MOBILE".equals(payBank)){
			str="{\"service\":\"alipay.wap.trade.create.direct\",\"sign\":\"f1bd3d156183faff6cb4f401687978e0\",\"sec_id\":\"MD5\",\"v\":\"1.0\",\"notify_data\":\"<notify><payment_type>1</payment_type><subject>null</subject><trade_no>2015081300001000610059001848</trade_no><buyer_email>250785171@qq.com</buyer_email><gmt_create>2015-08-13 10:39:37</gmt_create><notify_type>trade_status_sync</notify_type><quantity>1</quantity><out_trade_no>30120150813002522</out_trade_no><notify_time>2015-08-13 10:39:38</notify_time><seller_id>2088911503294873</seller_id><trade_status>TRADE_SUCCESS</trade_status><is_total_fee_adjust>N</is_total_fee_adjust><total_fee>0.01</total_fee><gmt_payment>2015-08-13 10:39:37</gmt_payment><seller_email>printhome@printhome.com</seller_email><price>0.01</price><buyer_id>2088002209559615</buyer_id><notify_id>892e1a7e389697b43328ad92899d21285e</notify_id><use_coupon>N</use_coupon></notify>\"}";
		}
    	return str;
    }
}
