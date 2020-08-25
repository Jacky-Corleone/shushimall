package com.camelot.openplatform.common.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

/** 
 * <p>Description: [HTTP 工具类]</p>
 * Created on 2016年3月22日
 * @author  <a href="mailto: liuxiangping86@camelotchina.com">刘香平</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部 
 */
public class HttpUtils { 
	   
    private String defaultContentEncoding; 
   
    public HttpUtils() { 
        this.defaultContentEncoding = Charset.defaultCharset().name(); 
    } 
   
    /**
     * <p>Discription:[发送GET请求]</p>
     * Created on 2016年3月22日
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     * @author:[刘香平]
     */
    public ResponseParam sendGet(String urlString) throws IOException { 
        return this.send(urlString, "GET", null, null); 
    } 
   
    /**
     * <p>Discription:[发送GET请求]</p>
     * Created on 2016年3月22日
     * @param urlString URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException
     * @author:[刘香平]
     */
    public ResponseParam sendGet(String urlString, Map<String, String> params) throws IOException { 
        return this.send(urlString, "GET", params, null); 
    } 
   
    /**
     * <p>Discription:[发送GET请求]</p>
     * Created on 2016年3月22日
     * @param urlString URL地址
     * @param params 参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     * @author:[刘香平]
     */
    public ResponseParam sendGet(String urlString, Map<String, String> params, Map<String, String> propertys) 
            throws IOException { 
        return this.send(urlString, "GET", params, propertys); 
    } 
   
    /**
     * <p>Discription:[发送POST请求]</p>
     * Created on 2016年3月22日
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     * @author:[刘香平]
     */
    public ResponseParam sendPost(String urlString) throws IOException { 
        return this.send(urlString, "POST", null, null); 
    } 
   
    /**
     * <p>Discription:[发送POST请求]</p>
     * Created on 2016年3月22日
     * @param urlString URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException
     * @author:[刘香平]
     */
    public ResponseParam sendPost(String urlString, Map<String, String> params) throws IOException { 
        return this.send(urlString, "POST", params, null); 
    } 
   
    /**
     * <p>Discription:[发送POST请求]</p>
     * Created on 2016年3月22日
     * @param urlString URL地址
     * @param params 参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     * @author:[刘香平]
     */
    public ResponseParam sendPost(String urlString, Map<String, String> params, Map<String, String> propertys) 
            throws IOException { 
        return this.send(urlString, "POST", params, propertys); 
    } 
   
    /**
     * <p>Discription:[发送HTTP请求]</p>
     * Created on 2016年3月22日
     * @param urlString URL地址
     * @param method  get/post
     * @param parameters  添加由键值对指定的请求参数
     * @param propertys  添加由键值对指定的一般请求属性
     * @return 响映对象
     * @throws IOException
     * @author:[刘香平]
     */
    private ResponseParam send(String urlString, String method, Map<String, String> parameters, 
            Map<String, String> propertys) throws IOException { 
        HttpURLConnection urlConnection = null; 
   
        if (method.equalsIgnoreCase("GET") && parameters != null) { 
            StringBuffer param = new StringBuffer(); 
            int i = 0; 
            for (String key : parameters.keySet()) { 
                if (i == 0) 
                    param.append("?"); 
                else 
                    param.append("&"); 
                param.append(key).append("=").append(parameters.get(key)); 
                i++; 
            } 
            urlString += param; 
        } 
   
        URL url = new URL(urlString); 
        urlConnection = (HttpURLConnection) url.openConnection(); 
        urlConnection.setRequestMethod(method); 
        urlConnection.setDoOutput(true); 
        urlConnection.setDoInput(true); 
        urlConnection.setUseCaches(false); 
   
        if (propertys != null) 
            for (String key : propertys.keySet()) { 
                urlConnection.addRequestProperty(key, propertys.get(key)); 
            } 
   
        if (method.equalsIgnoreCase("POST") && parameters != null) { 
            StringBuffer param = new StringBuffer(); 
            for (String key : parameters.keySet()) { 
                param.append("&"); 
                param.append(key).append("=").append(parameters.get(key)); 
            } 
            urlConnection.getOutputStream().write(param.toString().getBytes()); 
            urlConnection.getOutputStream().flush(); 
            urlConnection.getOutputStream().close(); 
        } 
        return this.makeContent(urlString, urlConnection); 
    } 
   
    /**
     * <p>Discription:[得到响应对象]</p>
     * Created on 2016年3月22日
     * @param urlString URL地址
     * @param urlConnection URL连接
     * @return 响应对象
     * @throws IOException
     * @author:[刘香平]
     */
    private ResponseParam makeContent(String urlString, HttpURLConnection urlConnection) throws IOException { 
    	ResponseParam responseParam = new ResponseParam(); 
        try { 
            InputStream in = urlConnection.getInputStream(); 
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in)); 
            responseParam.contentCollection = new Vector<String>(); 
            StringBuffer temp = new StringBuffer(); 
            String line = bufferedReader.readLine(); 
            while (line != null) { 
            	responseParam.contentCollection.add(line); 
                temp.append(line).append("\r\n"); 
                line = bufferedReader.readLine(); 
            } 
            bufferedReader.close(); 
            String ecod = urlConnection.getContentEncoding(); 
            if (ecod == null) 
                ecod = this.defaultContentEncoding; 
            responseParam.urlString = urlString; 
            responseParam.defaultPort = urlConnection.getURL().getDefaultPort(); 
            responseParam.file = urlConnection.getURL().getFile(); 
            responseParam.host = urlConnection.getURL().getHost(); 
            responseParam.path = urlConnection.getURL().getPath(); 
            responseParam.port = urlConnection.getURL().getPort(); 
            responseParam.protocol = urlConnection.getURL().getProtocol(); 
            responseParam.query = urlConnection.getURL().getQuery(); 
            responseParam.ref = urlConnection.getURL().getRef(); 
            responseParam.userInfo = urlConnection.getURL().getUserInfo(); 
            responseParam.content = new String(temp.toString().getBytes(), ecod); 
            responseParam.contentEncoding = ecod; 
            responseParam.code = urlConnection.getResponseCode(); 
            responseParam.message = urlConnection.getResponseMessage(); 
            responseParam.contentType = urlConnection.getContentType(); 
            responseParam.method = urlConnection.getRequestMethod(); 
            responseParam.connectTimeout = urlConnection.getConnectTimeout(); 
            responseParam.readTimeout = urlConnection.getReadTimeout(); 
            return responseParam; 
        } catch (IOException e) { 
            throw e; 
        } finally { 
            if (urlConnection != null) 
                urlConnection.disconnect(); 
        } 
    } 
   
    /**
     * <p>Discription:[默认的响应字符集]</p>
     * Created on 2016年3月22日
     * @return 默认的响应字符集
     * @author:[刘香平]
     */
    public String getDefaultContentEncoding() { 
        return this.defaultContentEncoding; 
    } 
    
    /**
     * <p>Discription:[发送GET请求]</p>
     * Created on 2016年3月22日
     * @param url URL地址
     * @param params 添加由键值对指定的请求参数
     * @param headers 添加由键值对指定的一般请求属性
     * @return URL连接
     * @throws Exception
     * @author:[刘香平]
     */
    public static URLConnection sendGetRequest(String url, 
            Map<String, String> params, Map<String, String> headers) 
            throws Exception { 
        StringBuilder buf = new StringBuilder(url); 
        Set<Entry<String, String>> entrys = null; 
        // 如果是GET请求，则请求参数在URL中 
        if (params != null && !params.isEmpty()) { 
            buf.append("?"); 
            entrys = params.entrySet(); 
            for (Map.Entry<String, String> entry : entrys) { 
                buf.append(entry.getKey()).append("=") 
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8")) 
                        .append("&"); 
            } 
            buf.deleteCharAt(buf.length() - 1); 
        } 
        URL url1 = new URL(buf.toString()); 
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection(); 
        conn.setRequestMethod("GET"); 
        // 设置请求头 
        if (headers != null && !headers.isEmpty()) { 
            entrys = headers.entrySet(); 
            for (Map.Entry<String, String> entry : entrys) { 
                conn.setRequestProperty(entry.getKey(), entry.getValue()); 
            } 
        } 
        conn.getResponseCode(); 
        return conn; 
    } 
    
    /**
     * <p>Discription:[发送POST请求]</p>
     * Created on 2016年3月22日
     * @param url URL地址
     * @param params 添加由键值对指定的请求参数
     * @param headers 添加由键值对指定的一般请求属性
     * @return URL连接
     * @throws Exception
     * @author:[刘香平]
     */
    public static URLConnection sendPostRequest(String url, 
            Map<String, String> params, Map<String, String> headers) 
            throws Exception { 
        StringBuilder buf = new StringBuilder(); 
        Set<Entry<String, String>> entrys = null; 
        // 如果存在参数，则放在HTTP请求体，形如name=aaa&age=10 
        if (params != null && !params.isEmpty()) { 
            entrys = params.entrySet(); 
            for (Map.Entry<String, String> entry : entrys) { 
                buf.append(entry.getKey()).append("=") 
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8")) 
                        .append("&"); 
            } 
            buf.deleteCharAt(buf.length() - 1); 
        } 
        URL url1 = new URL(url); 
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection(); 
        conn.setRequestMethod("POST"); 
        conn.setDoOutput(true); 
        OutputStream out = conn.getOutputStream(); 
        out.write(buf.toString().getBytes("UTF-8")); 
        if (headers != null && !headers.isEmpty()) { 
            entrys = headers.entrySet(); 
            for (Map.Entry<String, String> entry : entrys) { 
                conn.setRequestProperty(entry.getKey(), entry.getValue()); 
            } 
        } 
        conn.getResponseCode(); // 为了发送成功 
        return conn; 
    }
    
    /**
     * <p>Discription:[直接通过HTTP协议提交数据到服务器]</p>
     * 实现如下面表单提交功能:
     * <FORM METHOD=POST ACTION="http://localhost:8080/test/fileload" enctype="multipart/form-data">
     * 		<INPUT TYPE="text" NAME="name">
     * 		<INPUT TYPE="text" NAME="id">
     * 		<input type="file" name="imagefile"/>
     * 		<input type="file" name="zip"/>
     * </FORM>
     * Created on 2016年3月22日
     * @param path URL地址
     * @param params 添加由键值对指定的请求参数
     * @param files 文件列表
     * @return 上传文件是否成功：false 失败,true 成功
     * @throws Exception
     * @author:[刘香平]
     */
    public static boolean uploadFiles(String path, Map<String, String> params, FormFile[] files) throws Exception{      
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线 
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志 
           
        int fileDataLength = 0; 
        if(files!=null&&files.length!=0){ 
            for(FormFile uploadFile : files){//得到文件类型数据的总长度 
                StringBuilder fileExplain = new StringBuilder(); 
                fileExplain.append("--"); 
                fileExplain.append(BOUNDARY); 
                fileExplain.append("\r\n"); 
                fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n"); 
                fileExplain.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n"); 
                fileExplain.append("\r\n"); 
                fileDataLength += fileExplain.length(); 
                if(uploadFile.getInStream()!=null){ 
                    fileDataLength += uploadFile.getFile().length(); 
                }else{ 
                    fileDataLength += uploadFile.getData().length; 
                } 
            } 
        } 
        StringBuilder textEntity = new StringBuilder(); 
        if(params!=null&&!params.isEmpty()){ 
            for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据 
                textEntity.append("--"); 
                textEntity.append(BOUNDARY); 
                textEntity.append("\r\n"); 
                textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n"); 
                textEntity.append(entry.getValue()); 
                textEntity.append("\r\n"); 
            } 
        } 
        //计算传输给服务器的实体数据总长度 
        int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length; 
           
        URL url = new URL(path); 
        int port = url.getPort()==-1 ? 80 : url.getPort(); 
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);         
        OutputStream outStream = socket.getOutputStream(); 
        //下面完成HTTP请求头的发送 
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n"; 
        outStream.write(requestmethod.getBytes()); 
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n"; 
        outStream.write(accept.getBytes()); 
        String language = "Accept-Language: zh-CN\r\n"; 
        outStream.write(language.getBytes()); 
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n"; 
        outStream.write(contenttype.getBytes()); 
        String contentlength = "Content-Length: "+ dataLength + "\r\n"; 
        outStream.write(contentlength.getBytes()); 
        String alive = "Connection: Keep-Alive\r\n"; 
        outStream.write(alive.getBytes()); 
        String host = "Host: "+ url.getHost() +":"+ port +"\r\n"; 
        outStream.write(host.getBytes()); 
        //写完HTTP请求头后根据HTTP协议再写一个回车换行 
        outStream.write("\r\n".getBytes()); 
        //把所有文本类型的实体数据发送出来 
        outStream.write(textEntity.toString().getBytes());          
        //把所有文件类型的实体数据发送出来 
        if(files!=null&&files.length!=0){ 
            for(FormFile uploadFile : files){ 
                StringBuilder fileEntity = new StringBuilder(); 
                fileEntity.append("--"); 
                fileEntity.append(BOUNDARY); 
                fileEntity.append("\r\n"); 
                fileEntity.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n"); 
                fileEntity.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n"); 
                outStream.write(fileEntity.toString().getBytes()); 
                if(uploadFile.getInStream()!=null){ 
                    byte[] buffer = new byte[1024]; 
                    int len = 0; 
                    while((len = uploadFile.getInStream().read(buffer, 0, 1024))!=-1){ 
                        outStream.write(buffer, 0, len); 
                    } 
                    uploadFile.getInStream().close(); 
                }else{ 
                    outStream.write(uploadFile.getData(), 0, uploadFile.getData().length); 
                } 
                outStream.write("\r\n".getBytes()); 
            } 
        } 
        //下面发送数据结束标志，表示数据已经结束 
        outStream.write(endline.getBytes()); 
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        if(reader.readLine().indexOf("200")==-1){//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败 
            return false; 
        } 
        outStream.flush(); 
        outStream.close(); 
        reader.close(); 
        socket.close(); 
        return true; 
    } 

    /**
     * <p>Discription:[提交数据到服务器 ]</p>
     * Created on 2016年3月22日
     * @param path 上传路径
     * @param paramss 请求参数 key为参数名,value为参数值 
     * @param file 上传文件 
     * @return 上传文件是否成功：false 失败,true 成功
     * @throws Exception
     * @author:[刘香平]
     */
    public static boolean uploadFile(String path, Map<String, String> params, FormFile file) throws Exception{ 
       return uploadFiles(path, params, new FormFile[]{file}); 
    } 
    
    /**
     * <p>Discription:[将输入流转为字节数组]</p>
     * Created on 2016年3月22日
     * @param inStream 源流
     * @return 返回转换后的字节数组
     * @throws Exception
     * @author:[刘香平]
     */
    public static byte[] read2Byte(InputStream inStream)throws Exception{ 
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[1024]; 
        int len = 0; 
        while( (len = inStream.read(buffer)) !=-1 ){ 
            outSteam.write(buffer, 0, len); 
        } 
        outSteam.close(); 
        inStream.close(); 
        return outSteam.toByteArray(); 
    } 
    
    /**
     * <p>Discription:[将输入流转为字符串]</p>
     * Created on 2016年3月22日
     * @param inStream 源流
     * @return 转换结果
     * @throws Exception
     * @author:[刘香平]
     */
    public static String read2String(InputStream inStream)throws Exception{ 
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[1024]; 
        int len = 0; 
        while( (len = inStream.read(buffer)) !=-1 ){ 
            outSteam.write(buffer, 0, len); 
        } 
        outSteam.close(); 
        inStream.close(); 
        return new String(outSteam.toByteArray(),"UTF-8"); 
    } 

    /**
     * <p>Discription:[发送xml数据]</p>
     * Created on 2016年3月22日
     * @param path 请求地址
     * @param xml xml数据
     * @param encoding 编码
     * @return 以字节数据组形式返回响应数据
     * @throws Exception
     * @author:[刘香平]
     */
    public static byte[] postXml(String path, String xml, String encoding) throws Exception{ 
        byte[] data = xml.getBytes(encoding); 
        URL url = new URL(path); 
        HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
        conn.setRequestMethod("POST"); 
        conn.setDoOutput(true); 
        conn.setRequestProperty("Content-Type", "text/xml; charset="+ encoding); 
        conn.setRequestProperty("Content-Length", String.valueOf(data.length)); 
        conn.setConnectTimeout(5 * 1000); 
        OutputStream outStream = conn.getOutputStream(); 
        outStream.write(data); 
        outStream.flush(); 
        outStream.close(); 
        if(conn.getResponseCode()==200){ 
            return read2Byte(conn.getInputStream()); 
        } 
        return null; 
    }
    
	 /**
	  * <p>Discription:[设置默认的响应字符集]</p>
	  * Created on 2016年3月22日
	  * @param defaultContentEncoding 默认字符集
	  * @author:[刘香平]
	  */
	 public void setDefaultContentEncoding(String defaultContentEncoding) { 
	     this.defaultContentEncoding = defaultContentEncoding; 
	 } 
    
    
    //测试函数 
    public static void main(String args[]) throws Exception { 
        Map<String, String> params = new HashMap<String, String>(); 
        params.put("name", "xiazdong"); 
        params.put("age", "10"); 
        HttpURLConnection conn = (HttpURLConnection) 
                sendGetRequest( 
                        "http://192.168.0.103:8080/Server/PrintServlet", 
                        params, null); 
        int code = conn.getResponseCode(); 
        InputStream in = conn.getInputStream(); 
        byte[]data = read2Byte(in); 
    } 
}
