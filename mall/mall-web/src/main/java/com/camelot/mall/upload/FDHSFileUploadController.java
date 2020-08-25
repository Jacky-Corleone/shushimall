package com.camelot.mall.upload;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.fastdfs.FileManager;



@Controller
@RequestMapping(value = "/fdfsfileupload")
public class FDHSFileUploadController {
	
	
	private Logger LOG = LoggerFactory.getLogger(FDHSFileUploadController.class);
	
	@RequestMapping(value = "/uploadoriginal", method=RequestMethod.POST)
	@ResponseBody
	public JSON uploadOriginal(@RequestParam("file")MultipartFile file){
		LOG.info("----->> start handling upload request");
		JSONObject result = new JSONObject();
		try {
			 JSONArray fileUrlList = new JSONArray();
			 InputStream inputStream = file.getInputStream();
			 String fileName = FileManager.uploadOriginal(inputStream,file.getOriginalFilename());
			 fileUrlList.add(fileName);
				   
			 result.put("success", true);
			 result.put("msg","上传成功");
			 result.put("fileUrlList", fileUrlList);
			 System.out.println(result);
			 return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg","上传失败");
			return result;
		}
	}

}
