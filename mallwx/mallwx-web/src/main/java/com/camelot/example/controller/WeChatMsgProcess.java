package com.camelot.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SpringApplicationContextHolder;
import com.camelot.util.Constants;
import com.camelot.util.SHA1;
import com.camelot.weixin.api.WxAccessTokenAPI;
import com.camelot.weixin.api.WxMessageAPI;
import com.camelot.weixin.bean.json.WxAccessToken;
import com.camelot.weixin.bean.json.WxTemplateMessage;
import com.camelot.weixin.bean.json.WxTemplateMessageItem;
import com.camelot.weixin.bean.json.WxTemplateMessageResult;

@Component
public class WeChatMsgProcess implements WeChatProcess {
	private final static Logger LOGGER = LoggerFactory.getLogger(WeChatMsgProcess.class);

	public static final String Token = "myweixintest";
        @Resource
        private RedisDB redisDB;

	/**
	 * 微信校验
	 */
	@Override
	public void validataWeChat(HttpServletRequest request,
			HttpServletResponse response) {
		LOGGER.info("微信校验有效性");
//		try {
//			request.setCharacterEncoding("UTF-8");
//			response.setCharacterEncoding("UTF-8");
////			/** 读取接收到的xml消息 */
////			StringBuffer sb = new StringBuffer();
////			InputStream is = request.getInputStream();
////			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
////			BufferedReader br = new BufferedReader(isr);
////			String s = "";
////			while ((s = br.readLine()) != null) {
////				sb.append(s);
////			}
//			
//			String result = "";
//			/** 判断是否是微信接入激活验证，只有首次接入验证时才会收到echostr参数，此时需要把它直接返回 */
//			String echostr = request.getParameter("echostr");
//			System.out.println("echostr="+echostr);
//			if (echostr != null && echostr.length() > 1) {
//				result = echostr;
//			}
//		
//			OutputStream os = response.getOutputStream();
//			os.write(result.getBytes("UTF-8"));
//			os.flush();
//			os.close();  
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");// 随机字符串
        List<String> params = new ArrayList<String>();
        LOGGER.info("signature="+signature);
        LOGGER.info("timestamp="+timestamp);
        LOGGER.info("nonce="+nonce);
        LOGGER.info("echostr="+echostr);
        LOGGER.info("================================");
        params.add(Token);
        params.add(timestamp);
        params.add(nonce);
        // 1. 将token、timestamp、nonce三个参数进行字典序排序
        Collections.sort(params, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        String temp = SHA1.encode(params.get(0) + params.get(1) + params.get(2));
        LOGGER.info("temp="+temp);
        LOGGER.info("signature="+signature);
        LOGGER.info("echostr="+echostr);
        if (temp.equals(signature) && null!=echostr) {
            try {
            	LOGGER.info("成功返回 echostr：" + echostr);
                response.getWriter().write(echostr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
        	LOGGER.info("认证失败 ");
        }
            CustomMenu.testmain();
	}

	@Override
	public void doText(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response) {
		//文本消息  
        LOGGER.info("开发者微信号：" + inputMsg.getToUserName());  
        LOGGER.info("发送方帐号：" + inputMsg.getFromUserName());  
        LOGGER.info("消息创建时间：" + inputMsg.getCreateTime());  
        LOGGER.info("消息内容：" + inputMsg.getContent());  
        LOGGER.info("消息Id：" + inputMsg.getMsgId());  
        
        
        StringBuffer str = new StringBuffer();  
        str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + inputMsg.getFromUserName() + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + inputMsg.getToUserName() + "]]></FromUserName>");  
        str.append("<CreateTime>" + inputMsg.getCreateTime() + "</CreateTime>");  
        str.append("<MsgType><![CDATA[" + "text" + "]]></MsgType>");
        StringBuffer sb = new StringBuffer();
        sb.append("(๑•ᴗ•๑) 嗨~我是小印~\n");
        sb.append("非常开心的和您说一声：\n");
        sb.append("印刷家小管家正式上线啦！\n");
        sb.append("欢迎您来我们的商城做客！\n");
        sb.append("小印在此恭候您！\n");
        sb.append("要来哦 ~\\(^o^)/~ \n");
//        sb.append("您好，我是小印~非常感谢您关注印刷家采购小管家！").append("很遗憾，印刷家采购小管家预计9月10日正式上线，暂时无法为您提供服务");
//        sb.append("不过不用担心，您可以先登录www.printhome.com浏览我们的网站，我们的网站一样精彩！").append("上线后我会在第一时间通知您，欢迎您前来体验！届时我们将竭诚为您服务！");
        str.append("<Content><![CDATA["+sb.toString()+"]]></Content>");
//        str.append("<Content><![CDATA[您好,欢迎关注印刷家！]]></Content>");
        str.append("</xml>");  
        LOGGER.info(str.toString());  
        try {
			response.getWriter().write(str.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void doLocation(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response) {
		LOGGER.info("位置信息处理");
    	LOGGER.info(inputMsg.getLabel());
    	
    	StringBuffer str = new StringBuffer();  
        str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + inputMsg.getFromUserName() + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + inputMsg.getToUserName() + "]]></FromUserName>");  
        str.append("<CreateTime>" + inputMsg.getCreateTime() + "</CreateTime>");  
        str.append("<MsgType><![CDATA[" + "text" + "]]></MsgType>");  
        str.append("<Content><![CDATA[你的地址：" + inputMsg.getLabel() + ",已经发送,如需其他操作请联系工程师]]></Content>");  
        str.append("</xml>");  
        LOGGER.info(str.toString());  
        try {
			response.getWriter().write(str.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void doImage(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMusic(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doVideo(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doVoice(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doLink(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doShortvideo(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
        @Override
        public void doevent(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response,MsgType msgType){
                //文本消息
                LOGGER.info("开发者微信号：" + inputMsg.getToUserName());
                LOGGER.info("发送方帐号：" + inputMsg.getFromUserName());
                LOGGER.info("消息创建时间：" + inputMsg.getCreateTime());
                LOGGER.info("消息内容：" + inputMsg.getContent());
                LOGGER.info("消息Id：" + inputMsg.getMsgId());


                StringBuffer str = new StringBuffer();
                str.append("<xml>");
                str.append("<ToUserName><![CDATA[" + inputMsg.getFromUserName() + "]]></ToUserName>");
                str.append("<FromUserName><![CDATA[" + inputMsg.getToUserName() + "]]></FromUserName>");
                str.append("<CreateTime>" + inputMsg.getCreateTime() + "</CreateTime>");
                str.append("<MsgType><![CDATA[" + "text" + "]]></MsgType>");
                StringBuffer sb = new StringBuffer();
                sb.append("(๑•ᴗ•๑) 嗨~我是小印~\n");
                sb.append("非常开心的和您说一声：\n");
                sb.append("印刷家小管家正式上线啦！\n");
                sb.append("欢迎您来我们的商城做客！\n");
                sb.append("小印在此恭候您！\n");
                sb.append("要来哦 ~\\(^o^)/~ \n");
//                sb.append("您好，我是小印~非常感谢您关注印刷家采购小管家！").append("很遗憾，印刷家采购小管家预计9月10日正式上线，暂时无法为您提供服务");
//                sb.append("不过不用担心，您可以先登录www.printhome.com浏览我们的网站，我们的网站一样精彩！").append("上线后我会在第一时间通知您，欢迎您前来体验！届时我们将竭诚为您服务！");
                str.append("<Content><![CDATA["+sb.toString()+"]]></Content>");
                str.append("</xml>");
                LOGGER.info(str.toString());
                try {
                        response.getWriter().write(str.toString());
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }


        @Override
        public void SendInformation(Map map,HttpServletRequest request,HttpServletResponse response){
                LOGGER.info("发送微信信息入参：" + JSONObject.toJSONString(map));

                this.redisDB = SpringApplicationContextHolder.getBean("redisDB");

                WxTemplateMessage tm = new WxTemplateMessage();

                //token过期了才请求新的，间隔7000秒
                String _token=redisDB.get("thekeyofwechattoken");
                if(StringUtils.isEmpty(_token)){
                        WxAccessToken token=WxAccessTokenAPI.getAccessToken(Constants.WXAPPID, Constants.WXAPPSECRET);
                        _token=token.getAccess_token();
                        if(StringUtils.isNotEmpty(_token)){
                                redisDB.setAndExpire("thekeyofwechattoken",_token,7000);
                        }
                }

                //此Openid为用户在此公众号的唯一ID
                tm.setTouser(map.get("openId").toString());
                //模板ID
                tm.setTemplate_id(map.get("modeId").toString());
                tm.setTopcolor("#FF0000");
                tm.setUrl(Constants.Url);
                LinkedHashMap<String, WxTemplateMessageItem> data = new LinkedHashMap<String, WxTemplateMessageItem>();
                for (Object key : map.keySet()) {
                	if(null!=map.get(key)){
                		data.put(key.toString(), new WxTemplateMessageItem(map.get(key).toString(), "#173177"));
                	}
                }
                tm.setData(data);
                if(StringUtils.isEmpty(_token)){
                        //没得到_token，不发信息了
                }else{
                        WxTemplateMessageResult wxTemplateMessageResult=WxMessageAPI.sendTemplateMsg(_token, tm);
                        if(wxTemplateMessageResult.getErrmsg()!=null){
                                //发送消息失败，再发送一次
                                WxAccessToken token=WxAccessTokenAPI.getAccessToken(Constants.WXAPPID, Constants.WXAPPSECRET);
                                _token=token.getAccess_token();
                                if(StringUtils.isNotEmpty(_token)){
                                        redisDB.setAndExpire("thekeyofwechattoken",_token,7000);
                                }
                                //此Openid为用户在此公众号的唯一ID
                                tm.setTouser(map.get("openId").toString());
                                //模板ID
                                tm.setTemplate_id(map.get("modeId").toString());
                                tm.setTopcolor("#FF0000");
                                tm.setUrl(Constants.Url);
                                data = new LinkedHashMap<String, WxTemplateMessageItem>();
                                for (Object key : map.keySet()) {
                                        if(null!=map.get(key)){
                                                data.put(key.toString(), new WxTemplateMessageItem(map.get(key).toString(), "#173177"));
                                        }
                                }
                                tm.setData(data);
                                if(StringUtils.isNotEmpty(_token)){
                                        wxTemplateMessageResult=WxMessageAPI.sendTemplateMsg(_token, tm);
                                }
                        }
            }
        }

}
