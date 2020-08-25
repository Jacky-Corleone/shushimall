package com.camelot.ecm.ueditor;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.camelot.common.Json;
import com.camelot.ecm.ueditor.define.ActionMap;
import com.camelot.ecm.ueditor.define.AppInfo;
import com.camelot.ecm.ueditor.define.BaseState;
import com.camelot.ecm.ueditor.define.State;
import com.camelot.ecm.ueditor.hunter.FileManager;
import com.camelot.ecm.ueditor.hunter.ImageHunter;
import com.camelot.ecm.ueditor.upload.Uploader;
import com.camelot.ecm.upload.FileUploadController;
import com.camelot.openplatform.common.FTPUtils;
import com.camelot.openplatform.util.SpringApplicationContextHolder;
@Controller
@RequestMapping(value = "${adminPath}/ueditor")
public class ActionEnter {
	@Resource
    private FTPUtils ftpUtils;
	private HttpServletRequest request = null;
	
	private String rootPath = null;
	private String contextPath = null;
	
	private String actionType = null;
	
	private ConfigManager configManager = null;

	public void init( HttpServletRequest request, String rootPath ) {
		
		this.request = request;
		this.rootPath = rootPath;
		this.actionType = request.getParameter( "action" );
		this.contextPath = request.getContextPath();
		this.configManager = ConfigManager.getInstance( this.rootPath, this.contextPath, request.getRequestURI() );
		
	}
	@RequestMapping("exec")
	@ResponseBody
	public String exec (HttpServletRequest request,HttpServletResponse response) {
		
		String rootPath = request.getRealPath("/");
		init(request, rootPath);
		String callbackName = this.request.getParameter("callback");
		try{
			
			if ( callbackName != null ) {
	
				if ( !validCallbackName( callbackName ) ) {
					return new BaseState( false, AppInfo.ILLEGAL ).toJSONString();
				}
				response.getWriter().write(callbackName+"("+this.invoke()+");");
				
			} else {
				response.getWriter().write(this.invoke());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String invoke() {
		
		if ( actionType == null || !ActionMap.mapping.containsKey( actionType ) ) {
			return new BaseState( false, AppInfo.INVALID_ACTION ).toJSONString();
		}
		
		if ( this.configManager == null || !this.configManager.valid() ) {
			return new BaseState( false, AppInfo.CONFIG_ERROR ).toJSONString();
		}
		
		State state = null;
		
		int actionCode = ActionMap.getType( this.actionType );
		
		Map<String, Object> conf = null;
		
		switch ( actionCode ) {
		
			case ActionMap.CONFIG:
				return this.configManager.getAllConfig().toString();
				
			case ActionMap.UPLOAD_IMAGE:
			case ActionMap.UPLOAD_SCRAWL:
			case ActionMap.UPLOAD_VIDEO:
			case ActionMap.UPLOAD_FILE:
				conf = this.configManager.getConfig( actionCode );
//				state = uploadMultipartFiles();
				state = new Uploader( request, conf ).doExec();
				break;
				
			case ActionMap.CATCH_IMAGE:
				conf = configManager.getConfig( actionCode );
				String[] list = this.request.getParameterValues( (String)conf.get( "fieldName" ) );
				state = new ImageHunter( conf ).capture( list );
				break;
				
			case ActionMap.LIST_IMAGE:
			case ActionMap.LIST_FILE:
				conf = configManager.getConfig( actionCode );
				int start = this.getStartIndex();
				state = new FileManager( conf ).listFile( start );
				break;
				
		}
		
		return state.toJSONString();
		
	}
	
	public int getStartIndex () {
		
		String start = this.request.getParameter( "start" );
		
		try {
			return Integer.parseInt( start );
		} catch ( Exception e ) {
			return 0;
		}
		
	}
	
	/**
	 * callback参数验证
	 */
	public boolean validCallbackName ( String name ) {
		
		if ( name.matches( "^[a-zA-Z_]+[\\w0-9_]*$" ) ) {
			return true;
		}
		
		return false;
		
	}
//	
//	private State uploadMultipartFiles(){
//		MultiValueMap<String, MultipartFile> multiValueMap = ((DefaultMultipartHttpServletRequest)request).getMultiFileMap();
//		List<MultipartFile> list = multiValueMap.get("file");
//		
//		FileUploadController fileUploadController = new FileUploadController();
//		fileUploadController.setFtpUtils(ftpUtils);
//		Json<Map<String, Object>> json = fileUploadController.keupload2(list.get(0), null);
//		Map<String, Object> map = json.getObj();
//		State storageState = new BaseState();
//		if (storageState.isSuccess()) {
//			storageState.putInfo("url", (String)map.get("url"));
//			storageState.putInfo("type", (String) map.get("pix"));
//			storageState.putInfo("original", (String) map.get("fileName"));
//		}
//
//		return storageState;
//	}
//	
}