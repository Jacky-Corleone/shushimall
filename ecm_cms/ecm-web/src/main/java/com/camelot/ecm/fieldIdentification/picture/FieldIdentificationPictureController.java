package com.camelot.ecm.fieldIdentification.picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationPackagePictureDTO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationPictureDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.FieldIdentificationPictureService;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 实地认证图片控制层
 * @author Klaus
 */
@Controller
@RequestMapping(value = "${adminPath}/fieldIdentificationPicture")
public class FieldIdentificationPictureController extends BaseController {

	@Resource
	private FieldIdentificationPictureService pictureService;

	/**
	 * 图片列表
	 * @param request
	 * @param model
	 * @return 上传实地认证图片页面
	 */
	@RequestMapping(value = "list")
	public String list(HttpServletRequest request, Model model) {
		//页面初始化
		String userId = request.getParameter("userId");
		String extendId = request.getParameter("extendId");
		String acceptStatus = request.getParameter("acceptStatus");
		String uploadorId = UserUtils.getUser().getId();
		initData(model, userId, extendId, acceptStatus, uploadorId);
		//查询卖家所有实地认证图片集合
        FieldIdentificationPictureDTO queryPictureDTO = new FieldIdentificationPictureDTO();
        queryPictureDTO.setUserId(Long.valueOf(userId));
        queryPictureDTO.setExtendId(Long.valueOf(extendId));
        DataGrid<FieldIdentificationPictureDTO> pictureDataGrid = pictureService.findPictureListByCondition(queryPictureDTO, null);
        List<FieldIdentificationPictureDTO> pictureList = pictureDataGrid.getRows();
        //组装图片DTO数组到页面
	    Map<String, FieldIdentificationPictureDTO> pictureMap = new HashMap<String, FieldIdentificationPictureDTO>();
	    FieldIdentificationPictureDTO pictureDTO = new FieldIdentificationPictureDTO();
	    int pictureListSize = pictureList.size();
	    for(int i=0; i<pictureListSize; i++){
	    	pictureDTO = pictureList.get(i);
	    	pictureMap.put(pictureDTO.getPictureType()+"-"+pictureDTO.getSortNumber(), pictureDTO);
	    }
        model.addAttribute("pictureMap", pictureMap);
		return "fieldIdentification/uploadFieldIdentificationPicture";
	}

	/**
	 * 图片上传
	 * @param userId 卖家用户Id
	 * @param extendId 卖家扩展Id
	 * @param packagePictureDTO 实地认证图片包装DTO
	 * @return AJAX结果数组
	 */
	@RequestMapping(value = "upload")
	@ResponseBody
	public Map<String, Object> upload(Long userId, Long extendId, FieldIdentificationPackagePictureDTO packagePictureDTO) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		List<FieldIdentificationPictureDTO> pictureDTOList = packagePictureDTO.getPictureDTOList();
		int pictureDTOListSize = pictureDTOList.size();
		FieldIdentificationPictureDTO pictureDTO = new FieldIdentificationPictureDTO();
		int pictureType = 0;
		int sortNumber = 0;
		String pictureSrc = "";
		for(int i=0; i<pictureDTOListSize; i++){
			pictureDTO = pictureDTOList.get(i);
			pictureType = pictureDTO.getPictureType();
			sortNumber = pictureDTO.getSortNumber();
			pictureSrc = pictureDTO.getPictureSrc();
			//如果前台未上传图片，跳出本次循环
			if(StringUtils.isBlank(pictureSrc)){
				continue;
			}
			Long id = pictureDTO.getId();
			//如果是第1次上传，新增图片
			if(null == id){
				executeResult = pictureService.addPicture(pictureDTO);
			//如果是第n次上传，修改图片
			}else{
				executeResult = pictureService.modifyPicture(pictureDTO);
			}
			if(!executeResult.isSuccess()){
				list.add(pictureType+""+sortNumber);
			}
		}
		int size = list.size();
		//如果有图片上传失败
		if(null != list && size > 0){
			retMap.put("success", false);
			String message = "";
			String pictureIndex = "";
			int pictureTypeInt = 0;
			int sortNumberInt = 0;
			for(int i = 0; i<size; i++){
				pictureIndex = list.get(i);
				pictureTypeInt = (int)(pictureIndex.charAt(0));
				sortNumberInt = (int)(pictureIndex.charAt(1)) + 1;
				message += UserEnums.FieldIdentificationPictureType.getLabelByCode(pictureTypeInt)+"第"+sortNumberInt+"张  ";
			}
			retMap.put("message", message+"上传失败！");
		//如果所有图片都上传成功
		}else{
			retMap.put("success", executeResult.isSuccess());
			retMap.put("message", "所有图片上传成功！");
		}
		return retMap;
	}

	/**
	 * 页面初始化
	 * @param model
	 * @param userId 卖家用户Id
	 * @param extendId 卖家扩展Id
	 * @param acceptStatus 受理状态：未受理、已受理（待审核、审核通过、审核驳回）
	 * @param uploadorId 图片上传人Id
	 */
	private void initData(Model model, String userId, String extendId, String acceptStatus, String uploadorId) {
	    model.addAttribute("userId", userId);
		model.addAttribute("extendId", extendId);
		model.addAttribute("acceptStatus", acceptStatus);
		model.addAttribute("uploadorId", uploadorId);
    }

}
