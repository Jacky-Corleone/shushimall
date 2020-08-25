package com.camelot.fastdfs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PicReqInterceptor {
	
	//private FileManager fileManager;
	
	//http://192.168.1.186:8000/group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066.JPG!80x80
	public static String handlePicReq(String reqFileUrlString){
		
		String[] cutReqFileUrlString = cutReqFileUrlString(reqFileUrlString);
		if(cutReqFileUrlString != null){
			if(cutReqFileUrlString.length > 2){
				return reqFileUrlString;
			}
			if(isPicReq(cutReqFileUrlString[0])){
				try {
					URL reqFileUrl = new URL(reqFileUrlString);
					String[] reqFilePath = cutReqFileUrl(reqFileUrl);
					if(reqFilePath != null){
						if(reqFilePath.length < 2){
							return reqFileUrlString;
						}
						String subFileName = getSubFileName(reqFilePath);
						// 判断缩略图是否存在  存在 直接返回
						if(FileManager.fileIsExist(subFileName.substring(1))){
							return FileManager.PROTOCOL+reqFileUrl.getHost()+FileManager.PORT_SEPARATOR+reqFileUrl.getPort()+  subFileName;
						}else {
							//准备尺寸 后缀 reqFilePath[0] 扩展名 ( PictureUtils.getExtensionName(cutReqFileUrlString[0]) )
							return FileManager.generateThumbnail(reqFilePath[0].substring(1), reqFilePath[1], PictureUtils.getExtensionName(cutReqFileUrlString[0]));
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		
		
		
		
		
		
		/*if(isPicReq(reqFileUrlString)){
			try {
				URL reqFileUrl = new URL(reqFileUrlString);
				String[] reqFilePath = cutReqFileUrl(reqFileUrl);
				if(reqFilePath != null){
					if(reqFilePath.length < 2){
						return reqFileUrlString;
					}
					String subFileName = getSubFileName(reqFilePath);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		return reqFileUrlString;
	}
	
	
	//http://192.168.1.186:8000/group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066_80x80.JPG
	public static String handlePicReq2(String reqFileUrlString){
		
		if(PictureUtils.isPicType(reqFileUrlString)){
			try {
				URL reqFileUrl = new URL(reqFileUrlString);
				List<String> reqFilePath = cutReqFileUrl2(reqFileUrl);
				if(reqFilePath != null){
					if(reqFilePath.size() < 3){
						return reqFileUrlString;
					}
					//String subFileName = getSubFileName(reqFilePath);
					// 判断缩略图是否存在  存在 直接返回
					if(FileManager.fileIsExist(reqFilePath.get(0).substring(1) + Constant.SUB_FILE_NAME_EXTEND + reqFilePath.get(2) + Constant.FILE_SUFFIX_NAME+reqFilePath.get(1))){
						return reqFileUrlString;
					}else {
						//准备尺寸 后缀 reqFilePath[0] 扩展名 ( PictureUtils.getExtensionName(cutReqFileUrlString[0]) )
						return FileManager.generateThumbnail(reqFilePath.get(0).substring(1)+Constant.FILE_SUFFIX_NAME+reqFilePath.get(1), reqFilePath.get(2), reqFilePath.get(1));
					}
				}
				
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			}
			
		}else {
			return reqFileUrlString;
		}
		
		return reqFileUrlString;
	}
	
	
	private static String[] cutReqFileUrlString(String reqFileUrlString) {
		String[] urlAndArgs = reqFileUrlString.split(Constant.URL_ARGS_DELIMITER);
		return urlAndArgs;
		
	}


	private static String getSubFileName(String[] reqFilePath) {
		String[] split = reqFilePath[0].split(Constant.FILE_SUFFIX_DELIMITER);
		//String size = reqFilePath[1].replace(Constant.PIC_HEIGHT_WIDTH_DELIMITER, Constant.SUB_FILE_NAME_EXTEND);
		String subFileName = split[0]+ Constant.SUB_FILE_NAME_EXTEND + reqFilePath[1] + Constant.FILE_SUFFIX_NAME +split[1];
		return subFileName;
	}


	private static boolean isPicReq(String reqFileUrlString) {
		String extensionName = PictureUtils.getExtensionName(reqFileUrlString);
		return PictureUtils.isPicType(extensionName);
	}


	public static String[] cutReqFileUrl(URL reqFileUrl){
		
		try {
			String path = reqFileUrl.getPath();
			String[] urlAndArgs = path.split(Constant.URL_ARGS_DELIMITER);
			return urlAndArgs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	//http://192.168.1.186:8000/group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066_80x80.JPG
	public static List<String> cutReqFileUrl2(URL reqFileUrl){
		
		try {
			String path = reqFileUrl.getPath();
			
			String fileNameNoEx = PictureUtils.getFileNameNoEx(path);
			//获取扩展名
			String extensionName = PictureUtils.getExtensionName(path);
			//获取尺寸参数
			String sizeArgs = PictureUtils.getSizeArgs(fileNameNoEx);
			//获取文件名
			String fileNameNoArgs = PictureUtils.getFileNameNoArgs(fileNameNoEx);
			ArrayList<String> pathConstructionList = new ArrayList<String>();
			pathConstructionList.add(fileNameNoArgs);
			pathConstructionList.add(extensionName);
			if(sizeArgs != null){
				pathConstructionList.add(sizeArgs);
			}
			
			return pathConstructionList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	/*public static void main(String[] args) {
		System.out.println(new PicReqInterceptor().handlePicReq("http://192.168.1.186:8000/group1/M00/00/00/wKgBu1cGhGaANk-3AErASNF-ON0487.jpg!100x100"));
	}*/
	
}
