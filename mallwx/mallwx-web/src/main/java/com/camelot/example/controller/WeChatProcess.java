package com.camelot.example.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WeChatProcess {
	
	/**
	 * 
	 * @Title: validataWeChat
	 * @Description: 微信校验
	 * @param @param request
	 * @param @param response 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void validataWeChat(HttpServletRequest request,HttpServletResponse response);
	/**
	 * 
	 * @Title: doText
	 * @Description: 微信文本处理
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doText(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 
	 * @Title: doLocation
	 * @Description: 微信位置信息处理
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doLocation(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 
	 * @Title: doImage
	 * @Description:微信图片消息处理
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doImage(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 
	 * @Title: doMusic
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doMusic(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 
	 * @Title: doVideo
	 * @Description: 微信视频处理
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doVideo(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 
	 * @Title: doVoice
	 * @Description: 微信语音处理
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doVoice(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 
	 * @Title: doLink
	 * @Description: 微信链接处理
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doLink(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 
	 * @Title: doShortvideo
	 * @Description:微信小视频处理
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doShortvideo(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response);
	/**
	 *
	 * @Title: doShortvideo
	 * @Description:微信发送模板信息
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void doevent(InputMessage inputMsg,HttpServletRequest request,HttpServletResponse response,MsgType msgType);

	/**
	 *
	 * @Title: doShortvideo
	 * @Description:微信发送模板信息
	 * @param @param inputMsg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void SendInformation(Map map,HttpServletRequest request,HttpServletResponse response);


}
