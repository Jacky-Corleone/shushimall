package com.camelot.ecm.notice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.common.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;
import com.camelot.sellercenter.notice.service.NoticeExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 
 * <p>
 * Description: [公告管理]
 * </p>
 * Created on 2015-2-27
 * 
 * @author <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/notice")
public class NoticeController extends BaseController {

	@Resource
	private NoticeExportService noticeExportService;

	/**
	 * 获取公告列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({ "list", "" })
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
		Page page1 = new Page();
		// 将分页page转换为服务所需要的pager
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(10);
        page1.setPageNo(1);
        page1.setPageSize(10);
		pager.setSort("sortNum");
		pager.setOrder("asc");
		MallNoticeDTO mallNoticeDTO = new MallNoticeDTO();
		
		
        String id=request.getParameter("id");
        if(id!=null&&!"".equals(id)){
            mallNoticeDTO.setNoticeId(new Long(id));
        }
        String page=request.getParameter("pageNo");
        String rows=request.getParameter("pageSize");
        String noticeTitle=request.getParameter("noticeTitle");
        String noticeType=request.getParameter("noticeType");
        String noticeStatus=request.getParameter("noticeStatus");
        String platformId=request.getParameter("platformId");
        String createDtBegin=request.getParameter("createDtBegin");
        String createDtEnd=request.getParameter("createDtEnd");
        String publishDtBegin=request.getParameter("publishDtBegin");
        String publishDtEnd=request.getParameter("publishDtEnd");
        if(page!=null&&!"".equals(page)){
            pager.setPage(new Integer(page));
            page1.setPageNo(new Integer(page));
        }
        if(rows!=null&&!"".equals(rows)){
            pager.setRows(new Integer(rows));
            page1.setPageSize(new Integer(rows));
        }
        if(noticeType!=null&&!"".equals(noticeType)){
            mallNoticeDTO.setNoticeType(new Integer(noticeType));
        }
        // 默认查询舒适100平台
        if(null!=platformId&&!"".equals(platformId)){
        	mallNoticeDTO.setPlatformId(Long.valueOf(platformId));
        }else{
        	mallNoticeDTO.setPlatformId(0L);
        }
        mallNoticeDTO.setThemeType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
        if(noticeStatus!=null&&!"".equals(noticeStatus)){
            mallNoticeDTO.setNoticeStatus(new Integer(noticeStatus));
        }
        try{
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            if(createDtBegin!=null&&!"".equals(createDtBegin)){
                mallNoticeDTO.setCreateDtBegin(format.parse(createDtBegin));
            }
            if(createDtEnd!=null&&!"".equals(createDtEnd)){
                Date date=format.parse(createDtEnd);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = calendar.getTime();
                mallNoticeDTO.setCreateDtEnd(date);
            }
            if(publishDtBegin!=null&&!"".equals(publishDtBegin)){
                mallNoticeDTO.setPublishDtBegin(format.parse(publishDtBegin));
            }
            if(publishDtEnd!=null&&!"".equals(publishDtEnd)){
                Date date=format.parse(publishDtEnd);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = calendar.getTime();
                mallNoticeDTO.setPublishDtEnd(date);
            }
        }catch(Exception e){
            logger.error("类型转化出现异常");
        }
        mallNoticeDTO.setNoticeTitle(noticeTitle);
		DataGrid dg = noticeExportService.queryNoticeList(pager, mallNoticeDTO);
		page1.setList(dg.getRows());
		page1.setCount(dg.getTotal());
		model.addAttribute("page", page1);
        try{
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            if(createDtEnd!=null&&!"".equals(createDtEnd)){
                Date date=format.parse(createDtEnd);
                mallNoticeDTO.setCreateDtEnd(date);
            }
            if(publishDtEnd!=null&&!"".equals(publishDtEnd)){
                Date date=format.parse(publishDtEnd);
                mallNoticeDTO.setPublishDtEnd(date);
            }
        }catch(Exception e){
            logger.error("类型转化出现异常");
        }
        model.addAttribute("mallNoticeDTO",mallNoticeDTO);
		return "notice/noticeList";
	}
    @RequestMapping(value = "selectlist")
    @ResponseBody
    public Json selectList(HttpServletRequest request,Model model){
        Json json=new Json();
        try{
            Pager pager = new Pager();
            pager.setPage(1);
            pager.setRows(10);
            pager.setSort("sortNum");
            pager.setOrder("asc");
            MallNoticeDTO mallNoticeDTO = new MallNoticeDTO();
            //通过设置platformId此处显示平台公告
            mallNoticeDTO.setPlatformId(0L);
            String page=request.getParameter("page");
            String rows=request.getParameter("rows");
            String noticeTitle=request.getParameter("noticeTitle");
            String noticeType=request.getParameter("noticeType");
            if(page!=null&&!"".equals(page)){
                pager.setPage(new Integer(page));
            }
            if(rows!=null&&!"".equals(rows)){
                pager.setRows(new Integer(rows));
            }

        }catch(Exception e){
            json.setMsg("系统繁忙，请稍后再试");
            json.setSuccess(false);
            logger.error("公告查询出现异常:"+e.getMessage());
        }
        return json;
    }
	/**
	 * 跳转到公告添加/编辑页面
	 * 
	 * @param mallNoticeDTO
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({ "form" })
	public String form(MallNoticeDTO mallNoticeDTO, Model model) {
		if (mallNoticeDTO.getNoticeId() != null) {
			mallNoticeDTO = noticeExportService.getNoticeInfo(mallNoticeDTO.getNoticeId());
		}
		model.addAttribute("mallNoticeDTO", mallNoticeDTO);
		return "notice/noticeForm";
	}

	/**
	 * 保存公告
	 * 
	 * @param mallNoticeDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("save")
	public String save(MallNoticeDTO mallNoticeDTO, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String path = SysProperties.getProperty("ngIp");
		mallNoticeDTO.setThemeType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		// 将图片路径保存为相对路径
		mallNoticeDTO.setNoticeContent(mallNoticeDTO.getNoticeContent().replaceAll(path,""));
		ExecuteResult<String> result = noticeExportService.addNotice(mallNoticeDTO);
		addMessage(redirectAttributes, result.getResultMessage());
        String noticeTitle="";
        if(mallNoticeDTO.getNoticeTitle()!=null){
            noticeTitle=String.valueOf(mallNoticeDTO.getNoticeTitle());
        }
        redirectAttributes.addAttribute("noticeTitle",noticeTitle);
		return "redirect:" + SysProperties.getAdminPath() + "/notice/list";
	}

	/**
	 * 编辑公告
	 * 
	 * @param mallNoticeDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("edit")
	public String edit(MallNoticeDTO mallNoticeDTO, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String path = SysProperties.getProperty("ngIp");
		// 将图片路径保存为相对路径
		mallNoticeDTO.setNoticeContent(mallNoticeDTO.getNoticeContent().replaceAll(path,""));
		ExecuteResult<String> result = noticeExportService.modifyNoticeInfo(mallNoticeDTO);
		addMessage(redirectAttributes, result.getResultMessage());
        String noticeTitle="";
        if(mallNoticeDTO.getNoticeTitle()!=null){
            noticeTitle=String.valueOf(mallNoticeDTO.getNoticeTitle());
        }
        redirectAttributes.addAttribute("noticeTitle",noticeTitle);
		return "redirect:" + SysProperties.getAdminPath() + "/notice/list";
	}

	/**
	 * 置顶/取消置顶(是否置顶: 0 否 1是)
	 * @param mallNoticeDTO
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("recommend")
    @ResponseBody
	public Json recommend(MallNoticeDTO mallNoticeDTO) {
        Json json=new Json();
        try{
        	//如果状态为：1-已置顶，则修改为：0-取消置顶，否则相反操作。
        	if(mallNoticeDTO.getIsRecommend()==1){
        		mallNoticeDTO.setIsRecommend(0);
        	}else{
        		mallNoticeDTO.setIsRecommend(1);
        	}
            ExecuteResult<String> result = noticeExportService.modifyNoticeRecommend(mallNoticeDTO.getNoticeId(), mallNoticeDTO.getIsRecommend());
            if(result.isSuccess()){
                json.setMsg("置顶完成");
                json.setSuccess(true);
            }else{
                json.setMsg("置顶出现异常，请稍后再试");
                json.setSuccess(false);
                if(result.getErrorMessages()!=null&&result.getErrorMessages().size()>0){
                    for(int i=0;i<result.getErrorMessages().size();i++){
                        logger.error("公告置顶出现异常："+result.getErrorMessages().get(i));
                    }
                }
            }
        }catch(Exception e){
            json.setMsg("系统繁忙请稍后再试");
            json.setSuccess(false);
            logger.error("公告置顶出现异常："+e.getMessage());
        }
		return json;
	}

	/**
	 * 发布/下架
	 * 
	 * @param mallNoticeDTO
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("status")
    @ResponseBody
	public Json status(MallNoticeDTO mallNoticeDTO) {
        Json json=new Json();
        try{
            // 调用发布/下架接口
            ExecuteResult<String> result = noticeExportService.modifyNoticeStatus(mallNoticeDTO.getNoticeId(), mallNoticeDTO.getNoticeStatus());
            if(result.isSuccess()){
                json.setMsg("操作完成");
                json.setSuccess(true);
            }else{
                json.setMsg("操作失败,请稍后再试");
                json.setSuccess(false);
                if(result.getErrorMessages()!=null&&result.getErrorMessages().size()>0){
                    for(int i=0;i<result.getErrorMessages().size();i++){
                        logger.error("公告上下架出现异常:"+result.getErrorMessages().get(i));
                    }
                }
            }
        }catch(Exception e){
            json.setMsg("操作出现意外错误，请稍后再试");
            json.setSuccess(false);
        }
		return json;
	}

	/**
	 * 上移/下移
	 * 
	 * @param mallNoticeDTO
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("moveNotice")
	public ExecuteResult<MallNoticeDTO> moveNotice(MallNoticeDTO mallNoticeDTO) {
		
		ExecuteResult<MallNoticeDTO> executeResult =noticeExportService.updateNoticSortNum(mallNoticeDTO, mallNoticeDTO.getIsRecommend());
		return executeResult;
	}

	/**
	 * 跳转到公告添加/编辑页面
	 *
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({ "viewNotice" })
	public String viewNotice(Long noticeId, Model model) {
		MallNoticeDTO mallNoticeDTOModel = new MallNoticeDTO();
		if (noticeId != null) {
			mallNoticeDTOModel = noticeExportService.getNoticeInfo(noticeId);
		}
		model.addAttribute("mallNoticeDTO", mallNoticeDTOModel);
		return "notice/noticeView";
	}

	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("deleteNotice")
	public ExecuteResult<String> deleteNotice(MallNoticeDTO mallNoticeDTO) {
		ExecuteResult<String> executeResult = noticeExportService.deleteNoticeById(mallNoticeDTO.getNoticeId());
		return executeResult;
	}
}
