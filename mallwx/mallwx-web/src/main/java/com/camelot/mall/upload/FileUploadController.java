package com.camelot.mall.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.camelot.mall.util.Json;
import com.camelot.openplatform.common.FTPUtils;
import com.camelot.openplatform.common.FileUtil;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.util.ImageType;


/**
 * Created by sevelli on 15-3-2.
 * @author sevelli
 * @description 文件上传。图片上传与文件上传分开写是为了防止额外处理图片
 */
@Controller
@RequestMapping(value = "/fileUpload")
public class FileUploadController {
	/**ftp服务器ip地址*/
    private static String FILE_SERVER_IP;
    /**ftp服务器用户名*/
    private static String LOGIN_NAME;
    /**ftp服务器密码*/
    private static String LOGIN_PASSWORD;
    
	private FTPUtils ftpUtils = new FTPUtils(FILE_SERVER_IP, LOGIN_NAME, LOGIN_PASSWORD);
	
	/**图片服务器信息初始化*/
	static{
		FILE_SERVER_IP = SysProperties.getProperty("file_server_ip");
		LOGIN_NAME = SysProperties.getProperty("login_name");
		LOGIN_PASSWORD = SysProperties.getProperty("login_password");
	}

    /**
     * 文件上传
     * @return
     */
    @RequestMapping(value = "upload")
    @ResponseBody
    public Map<String, Object> fileUpload(@RequestParam("file")MultipartFile file, ImageType imageType){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","上传成功");
        try {
        	String suffix = getSuffix(file);
        	String uploadDir = getUploadDir(imageType);
            String returnUrl = ftpUtils.upload(uploadDir, file.getInputStream(), suffix);
            map.put("url",returnUrl);
        } catch (IOException e) {
            map.put("success",false);
            map.put("msg","文件上传出现问题");
        }
        return map;
    }

    /**
     * 上传固定大小的文件
     * @param file 
     * @param imageType 图片类型：商品图片、资质图片
     * @param height 	限定文件高度
     * @param width		限定文件宽度
     * @param size		限定文件大小
     * @return
     */
    @RequestMapping(value = "fixedUpload")
    @ResponseBody
    public Map<String, Object> fixedFileUpload(@RequestParam("file")MultipartFile file, ImageType imageType, Integer height, Integer width, Long size, HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",false);
        map.put("msg","上传失败");
        try {
        	/**构建图片保存的目录**/    
            String pathDir = File.separator + "pictemp";     
            /**得到图片保存目录的真实路径**/    
            String realPathDir = request.getSession().getServletContext().getRealPath(pathDir); 
            /**根据真实路径创建目录**/    
            File saveFileDir = new File(realPathDir);
            if(!saveFileDir.exists() || !saveFileDir.isDirectory()){
            	saveFileDir.mkdirs();
            }
            /**文件保存路径：采用时间戳格式命名，避免重名时，图片传输混淆*/
            String savePath = realPathDir + File.separator + (new Date().getTime()) + "_" + file.getOriginalFilename();
            File saveFile = new File(savePath);
            /**将文件存储到应用服务器磁盘**/
        	file.transferTo(saveFile);
        	/**文件格式校验*/
        	if(!FileUtil.isImage(saveFile)){
        		map.put("msg", "图片格式不正确！");
        		return map;
        	}
        	if(!FileUtil.rightSuffix(saveFile, new String[]{"jpg","jpeg","png","bmp"})){
        		map.put("msg", "只允许上传jpg、jpeg、png、bmp的图片！");
        		return map;
        	}
        	FileUtil util = new FileUtil(saveFile);
        	if(null!=height && height.intValue() != util.getHeight()){
        		map.put("msg", "请上传"+width+"*"+height+"大小的图片！");
        		saveFile.delete();
        		return map;
        	}
        	if(null!=width && width.intValue() != util.getWidth()){
        		map.put("msg", "请上传"+height+"*"+width+"大小的图片！");
        		saveFile.delete();
        		return map;
        	}
        	if(null!=size && size.longValue() < util.getSize()){
        		map.put("msg", "文件太大，请重新上传！");
        		saveFile.delete();
        		return map;
        	}
        	String suffix = util.getSuffix();
        	String uploadDir = getUploadDir(imageType);
            String returnUrl = ftpUtils.upload(uploadDir,new FileInputStream(saveFile), suffix);
            saveFile.delete();
            
            map.put("success", true);
            map.put("msg", "文件上传成功！");
            map.put("url",returnUrl);
        } catch (IOException e) {
            map.put("msg","文件上传出现问题");
        }
        return map;
    }

    /**
     * 根据上传文件类型(资质图片或者商品图片)获取上传的父级目录
     * @param imageType
     * @return
     */
	private String getUploadDir(ImageType imageType) {
		String uploadDir = "/album";
		if(null != imageType){
			uploadDir += "/" + imageType.name().toLowerCase();
		}
		return uploadDir;
	}
    
    /**
     * 获取文件后缀名
     * @param filer
     * @return
     */
	private String getSuffix(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring( fileName.lastIndexOf("."), fileName.length());
		return suffix;
	}
    
    @RequestMapping(value = "mupload")
    @ResponseBody
    public Map<String, Object> fileUpload(@RequestParam("files")MultipartFile[] files, ImageType imageType){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","上传成功");
        try {
        	String uploadDir = getUploadDir(imageType);
            if(files!=null){
                List<String> list = new ArrayList<String>();
                for (int i=0;i<files.length;i++){
                	String suffix = getSuffix(files[i]);
                    String returnUrl = ftpUtils.upload(uploadDir,files[i].getInputStream(), suffix);
                    list.add(returnUrl);
                }
                map.put("urls",list);
            }

        } catch (IOException e) {
            map.put("success",false);
            map.put("msg","文件上传出现问题");
        }
        return map;
    }
    
    /**
     * 文件上传
     * @return
     */
    @RequestMapping(value = "keupload")
    @ResponseBody
    public Json<Map<String, Object>> keupload(@RequestParam("file")MultipartFile file, ImageType imageType){
        Json<Map<String, Object>> json=new Json<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("error","0");
        map.put("message","上传成功");
        try {
        	String uploadDir = getUploadDir(imageType);
            String fileName = file.getOriginalFilename();
            String pix = fileName.substring( fileName.lastIndexOf("."), fileName.length());

            String returnUrl = ftpUtils.upload(uploadDir,file.getInputStream(), pix);
            map.put("url",returnUrl);
            json.setSuccess(true);
            json.setObj(map);
            json.setMsg("上传成功");
        } catch (IOException e) {
            json.setSuccess(false);
            //json.setObj(map);
            json.setMsg("失败");
        }
        return json;
    }
}
