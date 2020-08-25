package com.camelot.example.controller;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:菜单管理器类]</p>
 * Created on 2015-06-29
 * @author  <a href="mailto: zihanmin@camelotchina.com">訾瀚民</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class CustomMenu {
	private final static Logger LOGGER = LoggerFactory.getLogger(CustomMenu.class);

    public static String APPID,APPSECRET;

    //http客户端
    public static DefaultHttpClient httpclient;

    static {
        httpclient = new DefaultHttpClient();
        httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient); // 接受任何证书的浏览器客户端
        APPID = "wx0bb1318b6268a9cf";//你的APPID
        APPSECRET = "e8d6dcafbd591a48c42114af1258c3de"; //你的APPSECRET
    }
    

    /**
     * 
     * <p>Discription:[方法功能中文描述:创建菜单]</p>
     * Created on 2015-06-29
     * @param params
     * @param accessToken
     * @return
     * @throws Exception
     * @author:[lupeng]
     */
    public static String createMenu(String params, String accessToken) throws Exception {
        HttpPost httpost = HttpClientConnectionManager.getPostMethod("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken);
        httpost.setEntity(new StringEntity(params, "UTF-8"));
        HttpResponse response = httpclient.execute(httpost);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONObject demoJson = JSONObject.parseObject(jsonStr);
        return demoJson.getString("errmsg");

    }

    /**
     * 
     * <p>Discription:[方法功能中文描述: 获取accessToken]</p>
     * Created on 2015-06-29
     * @param appid
     * @param secret
     * @return
     * @throws Exception
     * @author:[lupeng]
     */
    public static String getAccessToken(String appid, String secret) throws Exception {
        HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret);
        HttpResponse response = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONObject demoJson = JSONObject.parseObject(jsonStr);
        return demoJson.getString("access_token");
    }
 
    /**
     * 
     * <p>Discription:[方法功能中文描述:查询菜单]</p>
     * Created on 2015-06-29
     * @param accessToken
     * @return
     * @throws Exception
     * @author:[lupeng]
     */
    
    
    public static String getMenuInfo(String accessToken) throws Exception {
        HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken);
        HttpResponse response = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        return jsonStr;
    }

    
    /**
     * 
     * <p>Discription:[方法功能中文描述: 删除菜单]</p>
     * Created on 2016年1月28日
     * @param accessToken
     * @return
     * @throws Exception
     * @author:[訾瀚民]
     */
    
    public static String deleteMenuInfo(String accessToken) throws Exception {
        HttpGet get = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + accessToken);
        HttpResponse response = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONObject demoJson = JSONObject.parseObject(jsonStr);
        return demoJson.getString("errmsg");
    }
    /**
     * 
     * <p>Discription:[方法功能中文描述:测试]</p>
     * Created on 2016年1月28日
     * @author:[訾瀚民]
     */
    public static void testmain() {

        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append(" \"button\":[");
        sb.append("     {");
        sb.append("         \"name\":\"第一个菜单\",");       //第一个菜单
        sb.append("         \"sub_button\":[");
        sb.append("             {");
        sb.append("                 \"type\":\"click\",");
        sb.append("                 \"name\":\"子菜单1\",");
        sb.append("                 \"key\":\"M1\"");
        sb.append("             },");
        sb.append("             {");
        sb.append("                 \"type\":\"click\",");
        sb.append("                 \"name\":\"子菜单2\",");
        sb.append("                 \"key\":\"M2\"");
        sb.append("             },");
        sb.append("             {");
        sb.append("                 \"type\":\"click\",");
        sb.append("                 \"name\":\"子菜单3\",");
        sb.append("                 \"key\":\"M3\"");
        sb.append("             }");
        sb.append("         ]");
        sb.append("     },");
        sb.append("     {");
        sb.append("         \"name\":\"第二个菜单\",");
        sb.append("         \"sub_button\":[");
        sb.append("             {");
        sb.append("                 \"type\":\"click\",");
        sb.append("                 \"name\":\"子菜单\",");
        sb.append("                 \"key\":\"M4\"");
        sb.append("             }");
        sb.append("         ]");
        sb.append("     },");
        sb.append("     {");
        sb.append("         \"name\":\"URL菜单\",");             //URL 连接
        sb.append("         \"sub_button\":[");
        sb.append("             {");
        sb.append("                 \"type\":\"view\",");
        sb.append("                 \"name\":\"无主题\",");
        sb.append("                 \"url\":\"http://www.wuzhuti.cn\"");    //连接地址
        sb.append("             }");
        sb.append("         ]");
        sb.append("     }");
        sb.append(" ]");
        sb.append("}");

        try {
            // 获取accessToken -参数appid，secret
            String accessToken = getAccessToken(APPID, APPSECRET);
            String res="";
            res = createMenu(sb.toString(), accessToken);
            LOGGER.info(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
