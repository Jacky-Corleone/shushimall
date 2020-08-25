package com.camelot.openplatform.common;

import java.io.IOException;

import com.camelot.openplatform.common.exception.CommonCoreException;

/**
 * <pre>
 * 图片压缩工具类 ImgCompressUtil
 * </pre>
 *
 * @author liujun
 * @version V0.1, 2015/2/6
 */
public class ImgCompressUtil {
	
	private ImgCompressUtil(){
		
	}
	
    /**
     * @param filePath 源图片路径【图片全路径】
     * @param destPath 生成图片路径【图片全路径】
     * @param width 宽度
     * @param height 高度
     */
    public static void comparess(String filePath,String destPath,int width,int height) {
        try{
            ImgCompress imgCom = new ImgCompress(filePath,destPath);
            imgCom.resizeFix(width, height);
        }catch (IOException e){
            throw new CommonCoreException("压缩图片失败",e);
        }
    }
}
