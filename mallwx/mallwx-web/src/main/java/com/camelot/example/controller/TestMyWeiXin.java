package com.camelot.example.controller;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.openplatform.util.SysProperties;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Controller
@RequestMapping("/testMyWeiXin")
public class TestMyWeiXin {
	private final static Logger LOGGER = LoggerFactory.getLogger(TestMyWeiXin.class);

	@RequestMapping("/test")
	public String testMyWeiXin(Model model){
		LOGGER.info("testMyWeiXin");
		model.addAttribute("attr1","测试属性");
		String conf=SysProperties.getProperty("token.suffix");
		model.addAttribute("attr2", conf);
		return "test";
	}
	
	@RequestMapping("/testMyWeiXin")
	public void testMyWeiXin1(HttpServletRequest request, HttpServletResponse response) throws Exception{
		LOGGER.info("--------------------------------------------------------");
		LOGGER.info(request.getMethod());
		WeChatProcess weChatProcess = new WeChatMsgProcess();
		if("GET".equals(request.getMethod().toUpperCase())){
			weChatProcess.validataWeChat(request, response);
		}else{
			LOGGER.info("微信消息处理");
			//处理接收消息
            ServletInputStream in = request.getInputStream();
            //将POST流转换为XStream对象
            XStream xs = new XStream(new DomDriver());
            //将指定节点下的xml节点数据映射为对象
            xs.alias("xml", InputMessage.class);
            //将流转换为字符串
            StringBuilder xmlMsg = new StringBuilder();
            byte[] b = new byte[4096];  
            for (int n; (n = in.read(b)) != -1;) {
                xmlMsg.append(new String(b, 0, n, "UTF-8"));
            }
            //将xml内容转换为InputMessage对象  
            InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());
            // 取得消息类型
            String msgType = inputMsg.getMsgType();
            LOGGER.info(msgType);
            //根据消息类型获取对应的消息内容
            if (msgType.equals(MsgType.Text.toString())) {
//				CustomMenu.testmain();
            	weChatProcess.doText(inputMsg, request, response);
            }else if(msgType.equals(MsgType.Location.toString())){
            	weChatProcess.doLocation(inputMsg, request, response); 
            }else if(msgType.equals(MsgType.Image.toString())){
            	weChatProcess.doImage(inputMsg, request, response);
            }else if(msgType.equals(MsgType.Link.toString())){
            	weChatProcess.doLink(inputMsg, request, response);
            }else if(msgType.equals(MsgType.Shortvideo.toString())){
            	weChatProcess.doShortvideo(inputMsg, request, response);
            }else if(msgType.equals(MsgType.Video.toString())){
            	weChatProcess.doVideo(inputMsg, request, response);
            }else if(msgType.equals(MsgType.Voice.toString())){
            	weChatProcess.doVoice(inputMsg, request, response);
            }else if(msgType.equals(MsgType.Music.toString())){
            	weChatProcess.doMusic(inputMsg, request, response);
            }else if(msgType.equals(MsgType.event.toString())){
				weChatProcess.doevent(inputMsg, request, response,MsgType.subscribe);
				// 订阅
				if (msgType.equals(MsgType.subscribe.toString()))
				{
					weChatProcess.doevent(inputMsg, request, response,MsgType.subscribe);
				}
				// 取消订阅
				else if (msgType.equals(MsgType.unsubscribe.toString())) {
					weChatProcess.doevent(inputMsg, request, response,MsgType.unsubscribe);
				}

			}
        }
	}
}
