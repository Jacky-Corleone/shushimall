package com.camelot.payment.service.alipay.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.camelot.payment.service.alipay.util.httpClient.HttpProtocolHandler;
import com.camelot.payment.service.alipay.util.httpClient.HttpRequest;
import com.camelot.payment.service.alipay.util.httpClient.HttpResponse;
import com.camelot.payment.service.alipay.util.httpClient.HttpResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipaySubmit {
    private static final Logger logger = LoggerFactory
            .getLogger(AlipaySubmit.class);
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(AlipayConfig.SIGN_TYPE.equals("MD5") ) {
        	mysign = MD5.sign(prestr, AlipayConfig.KEY, AlipayConfig.INPUT_CHARSET);
        }
        return mysign;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayConfig.SIGN_TYPE);

        return sPara;
    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName) {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"form\" name=\"form\" action=\"" + AlipayConfig.ALIPAY_GATEWAY_NEW
                      + "_input_charset=" + AlipayConfig.INPUT_CHARSET + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
//        sbHtml.append("<script>document.forms[0].submit();</script>");

        return sbHtml.toString();
    }
    
    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     * @paramALIPAY_GATEWAY_NEW 支付宝网关地址
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @param sParaTemp 请求参数数组
     * @return 支付宝处理结果
     * @throws Exception
     */
    public static String buildRequest(String ALIPAY_GATEWAY_NEW, String strParaFileName, String strFilePath,Map<String, String> sParaTemp) throws Exception {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AlipayConfig.INPUT_CHARSET);
        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(ALIPAY_GATEWAY_NEW+"_input_charset="+AlipayConfig.INPUT_CHARSET);
        HttpResponse response = httpProtocolHandler.execute(request,strParaFileName,strFilePath);
        if (response == null) {
            return null;
        }
        
        String strResult = response.getStringResult();

        return strResult;
    }
    
    /**
     * 解析远程模拟提交后返回的信息，获得token
     * @param text 要解析的字符串
     * @return 解析结果
     * @throws Exception 
     */
    public static String getRequestToken(String text) throws Exception {
    	String request_token = "";
    	//以“&”字符切割字符串
    	String[] strSplitText = text.split("&");
    	//把切割后的字符串数组变成变量与数值组合的字典数组
    	Map<String, String> paraText = new HashMap<String, String>();
        for (int i = 0; i < strSplitText.length; i++) {

    		//获得第一个=字符的位置
        	int nPos = strSplitText[i].indexOf("=");
        	//获得字符串长度
    		int nLen = strSplitText[i].length();
    		//获得变量名
    		String strKey = strSplitText[i].substring(0, nPos);
    		//获得数值
    		String strValue = strSplitText[i].substring(nPos+1,nLen);
    		//放入MAP类中
    		paraText.put(strKey, strValue);
        }
    	if (paraText.get("res_data") != null) {
    		String res_data = paraText.get("res_data");
    		//解析加密部分字符串（RSA与MD5区别仅此一句）
    		if(AlipayConfig.SIGN_TYPE.equals("0001")) {
    			res_data = RSA.decrypt(res_data, AlipayConfig.PRIVATE_KEY, AlipayConfig.INPUT_CHARSET);
    		}

    		//token从res_data中解析出来（也就是说res_data中已经包含token的内容）
    		Document document = DocumentHelper.parseText(res_data);
    		request_token = document.selectSingleNode( "//direct_trade_create_res/request_token" ).getText();
    	}
    	return request_token;
    }
    
    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @paramALIPAY_GATEWAY_NEW 支付宝网关地址
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(String ALIPAY_GATEWAY_NEW, Map<String, String> sParaTemp, String strMethod, String strButtonName) {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"form\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW
                      + "_input_charset=" + AlipayConfig.INPUT_CHARSET + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        return sbHtml.toString();
    }
    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @param sParaTemp 请求参数数组
     * @return 支付宝处理结果
     * @throws Exception
     */
    public static String buildRequest(String strParaFileName, String strFilePath,Map<String, String> sParaTemp) throws Exception {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(AlipayConfig.INPUT_CHARSET);
        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(AlipayConfig.ALIPAY_GATEWAY_NEW+"_input_charset="+AlipayConfig.INPUT_CHARSET);
        HttpResponse response = httpProtocolHandler.execute(request,strParaFileName,strFilePath);
        if (response == null) {
            return null;
        }
        String strResult = response.getStringResult();
        return strResult;
    }

    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }
    
    /**
     * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数
     * 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
     * @return 时间戳字符串
     * @throws java.io.IOException
     * @throws org.dom4j.DocumentException
     * @throws java.net.MalformedURLException
     */
	@SuppressWarnings("unchecked")
	public static String query_timestamp() throws MalformedURLException,
                                                        DocumentException, IOException {

        //构造访问query_timestamp接口的URL串
        String strUrl = AlipayConfig.ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner=" + AlipayConfig.PARTNER + "&_input_charset" +AlipayConfig.INPUT_CHARSET;
        StringBuffer result = new StringBuffer();

        SAXReader reader = new SAXReader();
        Document doc = reader.read(new URL(strUrl).openStream());

        List<Node> nodeList = doc.selectNodes("//alipay/*");

        for (Node node : nodeList) {
            // 截取部分不需要解析的信息
            if (node.getName().equals("is_success") && node.getText().equals("T")) {
                // 判断是否有成功标示
                List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
                for (Node node1 : nodeList1) {
                    result.append(node1.getText());
                }
            }
        }

        return result.toString();
    }
	
}
