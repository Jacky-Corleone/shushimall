package com.camelot.cms.web;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.cmscenter.service.CmsImageService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by jh on 2016/3/3.
 */
@Controller
@RequestMapping("/projectController")
public class ProjectController {
    @Resource
    private CmsImageService cmsImageService;
    @Resource
    private CmsArticleService cmsArticleService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @RequestMapping("/list")
    public String projectList(CmsArticleDTO cmsArticleDTO, Model model,Pager<CmsArticleDTO> pager,HttpServletRequest request) {
        String id=request.getParameter("id");
        cmsArticleDTO.setAcateid("工程案例");
        DataGrid<CmsArticleDTO> pageGrid= cmsArticleService.queryCmsArticleList(pager,cmsArticleDTO);
        DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
        pager.setTotalCount(pageGrid.getTotal().intValue());
        pager.setRecords(pageGrid.getRows());
        model.addAttribute("pager", pager);
        model.addAttribute("pageGrid", pageGrid);
        return "/cms/projectList";
    }
}

