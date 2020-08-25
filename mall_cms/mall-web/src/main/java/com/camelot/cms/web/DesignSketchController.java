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
@RequestMapping("/designSketchController")
public class DesignSketchController {
    @Resource
    private CmsImageService cmsImageService;
    @Resource
    private CmsArticleService cmsArticleService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @RequestMapping("/list")
    public String designSketchList(CmsArticleDTO cmsArticleDTO, Model model,Pager<CmsArticleDTO> pager,HttpServletRequest request) {
        String id=request.getParameter("id");
        cmsArticleDTO.setAcateid("效果图");
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
        return "/cms/designSketchList";
    }

    /*public String categoryItems( Pager<ItemQueryOutDTO> pager,String cid , Model model ) {
        JSONArray categoryes = this.commonService.findCategory();
        model.addAttribute("categoryes", categoryes);
        LOG.debug("ALL CATEGORYES:" + categoryes.toJSONString());

        Long id = null;
        if( cid != null && !"".equals(cid) ){
            if( cid.indexOf(":") != -1 ){
                String tmp = cid.substring(cid.lastIndexOf(":")+1, cid.length());
                id = Long.valueOf(tmp);
            }else{
                id = Long.valueOf(cid);
            }
        }
        LOG.debug("产品大全-商品列表查询参数："+id);
        pager.setRows(30);
        ExecuteResult<DataGrid<ItemQueryOutDTO>> er = this.itemService.queryItemByCid(id, pager);

        if(er.isSuccess()){
            pager.setTotalCount(er.getResult().getTotal().intValue());
            pager.setRecords(er.getResult().getRows());
        }

        model.addAttribute("pager", pager);
        model.addAttribute("cid", cid);
        return "/goodscenter/product/categoryItems";
    }*/
}

