package com.camelot.openplatform.common.fastdfs;

import java.net.URL;

public class PicReqInterceptor {
	
	//private FileManager fileManager;
	
	//http://192.168.1.186:8000/group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066.JPG!80x80
	public String handlePicReq(String reqFileUrlString){
		
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
						if(FileManager.fileIsExist(subFileName.substring(1))){
							return subFileName;
						}else {
							//准备尺寸 后缀 reqFilePath[0] 扩展名 ( PictureUtils.getExtensionName(cutReqFileUrlString[0]) )
							FileManager.generateThumbnail(reqFilePath[0].substring(1), reqFilePath[1], PictureUtils.getExtensionName(cutReqFileUrlString[0]));
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
	
	
	private String[] cutReqFileUrlString(String reqFileUrlString) {
		String[] urlAndArgs = reqFileUrlString.split(Constant.URL_ARGS_DELIMITER);
		return urlAndArgs;
		
	}


	private String getSubFileName(String[] reqFilePath) {
		String[] split = reqFilePath[0].split(Constant.FILE_SUFFIX_DELIMITER);
		//String size = reqFilePath[1].replace(Constant.PIC_HEIGHT_WIDTH_DELIMITER, Constant.SUB_FILE_NAME_EXTEND);
		String subFileName = split[0]+ Constant.SUB_FILE_NAME_EXTEND + reqFilePath[1] + Constant.FILE_SUFFIX_NAME +split[1];
		return subFileName;
	}


	private boolean isPicReq(String reqFileUrlString) {
		String extensionName = PictureUtils.getExtensionName(reqFileUrlString);
		return PictureUtils.isPicType(extensionName);
	}


	public String[] cutReqFileUrl(URL reqFileUrl){
		
		try {
			String path = reqFileUrl.getPath();
			String[] urlAndArgs = path.split(Constant.URL_ARGS_DELIMITER);
			return urlAndArgs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public static void main(String[] args) {
		System.out.println(new PicReqInterceptor().handlePicReq("http://192.168.1.186:8000/group1/M00/00/00/wKgBu1cGhGaANk-3AErASNF-ON0487.jpg!100x100"));
	}
	
}
