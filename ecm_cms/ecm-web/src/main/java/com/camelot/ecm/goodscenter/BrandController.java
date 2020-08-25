package com.camelot.ecm.goodscenter;

import com.camelot.common.Json;
import com.camelot.goodscenter.dto.BrandOfShopDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Description: [品牌controller类]</p>
 * Created on 2015-2-27
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Controller
@RequestMapping("${adminPath}/brand")
public class BrandController {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ItemBrandExportService itemBrandExportService;
    @Resource
	private ShopBrandExportService shopBrandExportService;
    @Resource
    private ShopCategoryExportService shopCategoryExportService;
	@RequiresPermissions("brand:view")
	@RequestMapping({"index",""})
	public String index(Model model){
		List<ItemCategoryDTO> cList = itemCategoryService.queryItemCategoryList(0L).getRows();
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(10);
        ItemBrandDTO itemBrandDTO=new ItemBrandDTO();
        itemBrandDTO.setBrandStatus(1);
        ExecuteResult<DataGrid<ItemBrandDTO>> executeResult = itemBrandExportService.queryBrandList(itemBrandDTO,pager);
        Page p=new Page();
        if(executeResult.getResult()!=null){
            p.setCount(executeResult.getResult().getTotal());
            p.setList(executeResult.getResult().getRows());
        }else{
            p.setCount(0);
        }
        p.setPageSize(pager.getRows());
        p.setPageNo(pager.getPage());
        model.addAttribute("p",p);
		model.addAttribute("cList", cList);
		//model.addAttribute("brandList", executeResult.getResult().getRows());
		return "brand/index";
	}
    @RequestMapping(value = "selectbrandlist")
    @ResponseBody
    public Json selectBrandlist(HttpServletRequest request){
        Json json=new Json();
        try{
            String page=request.getParameter("page");
            String rows=request.getParameter("rows");
            String brandName=request.getParameter("brandName");
            Pager pager=new Pager();
            if(page!=null&&!"".equals(page)){
                pager.setPage(new Integer(page));
            }else{
                pager.setPage(1);
            }
            if(rows!=null&&!"".equals(rows)){
                pager.setRows(new Integer(rows));
            }else{
                pager.setRows(10);
            }
            Page p=new Page();
            p.setPageSize(pager.getRows());
            p.setPageNo(pager.getPage());
            ItemBrandDTO itemBrandDTO=new ItemBrandDTO();
            if(brandName!=null&&!"".equals(brandName)){
                itemBrandDTO.setBrandName(brandName);
            }
            itemBrandDTO.setBrandStatus(new Integer(1));
            ExecuteResult<DataGrid<ItemBrandDTO>> executeResult=itemBrandExportService.queryBrandList(itemBrandDTO,pager);
            if(executeResult.getResult()!=null){
                p.setCount(executeResult.getResult().getTotal());
                p.setList(executeResult.getResult().getRows());
            }else{
                p.setCount(0);
            }
            json.setMsg(p.toString());
            json.setSuccess(true);
            json.setObj(p);
        }catch(Exception e){
            logger.error("查询品牌失败",e);
            json.setMsg("查询品牌失败");
            json.setSuccess(false);
        }
        return json;
    }

	@RequiresPermissions("brand:view")
	@RequestMapping("getChildCategory")
	@ResponseBody
	public List<ItemCategoryDTO> getChildCategory(Long pCid){
		List<ItemCategoryDTO> cList = itemCategoryService.queryItemCategoryList(pCid).getRows();
		return cList;
	}
	
	@RequiresPermissions("brand:view")
	@RequestMapping("getCategoryBrand")
	@ResponseBody
	public List<ItemBrandDTO> getCategoryBrand(Long secondCid,Long thirdCid){
		BrandOfShopDTO param = new BrandOfShopDTO();
		param.setSecondCid(secondCid);
		param.setThirdCid(thirdCid);
		List<ItemBrandDTO> brandList = itemBrandExportService.queryItemBrandList(param).getRows();
		return brandList;
	}
	
	@RequiresPermissions("brand:view")
	@RequestMapping("getBrandList")
	@ResponseBody
	public List<ItemBrandDTO> queryBrandList(String brandName,String brandId){
		ItemBrandDTO itemBrandDTO=new ItemBrandDTO();
		itemBrandDTO.setBrandName(brandName);
		itemBrandDTO.setBrandStatus(1);
		List<ItemBrandDTO> brandLists=new ArrayList<ItemBrandDTO>();
		ExecuteResult<DataGrid<ItemBrandDTO>> list=itemBrandExportService.queryBrandList(itemBrandDTO, null);
		if(!list.isSuccess()){
			return brandLists;
		}
		  if(null==list.getResult().getRows()||list.getResult().getRows().size()==0){
			  return brandLists;
		  }
			for(ItemBrandDTO dto:list.getResult().getRows()){
				if(StringUtils.isNotEmpty(brandId)){
					if(Long.valueOf(brandId).longValue()!=dto.getBrandId().longValue()){
						brandLists=list.getResult().getRows();
					   }
					}else{
						brandLists=list.getResult().getRows();
					}
			    }
		return brandLists;
	}
	
	@RequiresPermissions("brand:edit")
	@RequestMapping("modifyBrand")
	@ResponseBody
	public List<ItemBrandDTO> modifyBrand(ItemBrandDTO brand,String [] editPicUrls){
		ExecuteResult<ItemBrandDTO> result = new ExecuteResult<ItemBrandDTO>();
		if(editPicUrls.length>0){
			brand.setBrandLogoUrl(editPicUrls[0].replaceAll(SysProperties.getProperty("ngIp"),""));
		}
		result = itemBrandExportService.modifyItemBrand(brand);
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		List<ItemBrandDTO> brandList = itemBrandExportService.queryItemBrandAllList(pager).getRows();
		return brandList;
	}
	
	@RequiresPermissions("brand:edit")
	@RequestMapping("addBrand")
	@ResponseBody
	public Json addBrand(ItemBrandDTO brand,String [] picUrls){
        Json json=new Json();
        try{
            ExecuteResult<ItemBrandDTO> result = new ExecuteResult<ItemBrandDTO>();
            if(picUrls.length>0){
                brand.setBrandLogoUrl(picUrls[0].replaceAll(SysProperties.getProperty("ngIp"),""));
            }
            result = itemBrandExportService.addItemBrand(brand);
            if(result!=null&&result.isSuccess()){
                json.setMsg("新增成功");
                json.setSuccess(true);
            }else{
                if(result.getErrorMessages()!=null&&result.getErrorMessages().size()>0){
                    json.setMsg("添加品牌失败: "+result.getErrorMessages().get(0));
                    json.setSuccess(false);
                }else{
                    json.setMsg("添加品牌失败");
                    json.setSuccess(false);
                }
            }
        }catch(Exception e){
            json.setMsg("添加品牌失败"+e.getMessage());
            json.setSuccess(false);
            logger.error("添加品牌失败",e);
        }
		return json;
	}
	
	@RequiresPermissions("brand:edit")
	@RequestMapping("addCategoryBrandBatch")
	@ResponseBody
	public ExecuteResult<ItemBrandDTO>addCategoryBrandBatch(ItemBrandDTO brand){
		ExecuteResult<ItemBrandDTO> result = new ExecuteResult<ItemBrandDTO>();
		result = itemBrandExportService.addCategoryBrandBatch(brand);
		return result;
	}
	
	
	public void setItemCategoryService(ItemCategoryService itemCategoryService) {
		this.itemCategoryService = itemCategoryService;
	}

	public void setItemBrandExportService(ItemBrandExportService itemBrandExportService) {
		this.itemBrandExportService = itemBrandExportService;
	}
    @RequestMapping(value = "canclebrand")
    @ResponseBody
    public Json cancleBrand(HttpServletRequest request){
        Json json=new Json();
        try{
            String brandid=request.getParameter("brandid");
            String thirdid=request.getParameter("thirdid");
            if(thirdid!=null&&!"".equals(thirdid)){
                if(brandid!=null&&!"".equals(brandid)){
                    //判断是否可以删除品牌
                    Boolean ifcancancle=ifcancanclebrand(thirdid,brandid);
                    if(ifcancancle){
                        ExecuteResult<String> execute= itemBrandExportService.deleteCategoryBrand(new Long(thirdid),new Long(brandid));
                        if(execute.isSuccess()){
                            if(execute.getResult()!=null&&!"0".equals(execute.getResult())){
                                json.setMsg("删除成功。");
                                json.setSuccess(true);
                            }else{
                                json.setMsg("删除失败！"+execute.getResultMessage()!=null?execute.getResultMessage():"");
                                json.setSuccess(false);
                            }
                        }else{
                            if(execute.getErrorMessages()!=null&&execute.getErrorMessages().size()>0){
                                String msg="删除失败：";
                                for(String message:execute.getErrorMessages()){
                                    msg=msg+message+"；";
                                }
                                json.setMsg(msg);
                                json.setSuccess(false);
                            }else{
                                json.setMsg("删除失败,请联系管理员！");
                                json.setSuccess(false);
                            }
                        }
                    }else{
                        json.setMsg("删除失败，品牌和类目都已经分配给同一个店铺");
                        json.setSuccess(false);
                    }
                }else{
                    json.setMsg("品牌id为空，无法指定删除的类目");
                    json.setSuccess(false);
                }
            }else{
                json.setSuccess(false);
                json.setMsg("三级类目id为空，无法删除指定品牌");
            }
        }catch(Exception e){
            logger.error("删除品牌异常信息："+e.getMessage());
            json.setMsg("删除失败,请联系管理员");
            json.setSuccess(false);
        }
        return json;
    }

    private Boolean ifcancanclebrand(String cid,String brandid){
        //如果品牌和三级类目都有对应的店铺且 两者店铺有重叠的部分则不能删除，其他情况都可删除
        Boolean ifcan=true;
        ShopBrandDTO shopBrandDTO=new ShopBrandDTO();
        shopBrandDTO.setBrandId(new Long(brandid));
        shopBrandDTO.setStatus(2);
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(Integer.MAX_VALUE);
        //根据品牌查询具有该品牌的所有店铺
        ExecuteResult<DataGrid<ShopBrandDTO>> executeResult=shopBrandExportService.queryShopBrandAll(shopBrandDTO, pager);
        if(executeResult==null||executeResult.getResult()==null||
                executeResult.getResult().getRows()==null||executeResult.getResult().getRows().size()<1){
            ifcan=true;
            return ifcan;
        }else{
        	//2015-8-11宋文斌start
//            ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
//            shopCategoryDTO.setCid(new Long(cid));
//            //查询具有该类目的所有店铺
//            ExecuteResult<DataGrid<ShopCategoryDTO>> executeResult1=shopCategoryExportService.queryShopCategoryAll(shopCategoryDTO,pager);
//            if(executeResult1==null||executeResult1.getResult()==null||
//                    executeResult1.getResult().getRows()==null||executeResult1.getResult().getRows().size()<1){
//                ifcan=true;
//                return ifcan;
//            }else{
//                List<ShopBrandDTO> listbrand=executeResult.getResult().getRows();
//                List<ShopCategoryDTO> listcate=executeResult1.getResult().getRows();
//                Map<Long,Long> map=new HashMap<Long, Long>();
//                for(ShopBrandDTO shop1:listbrand){
//                    if(shop1.getShopId()!=null){
//                        map.put(shop1.getShopId(),shop1.getShopId());
//                    }
//                }
//                for(ShopCategoryDTO shop2:listcate){
//                    if(shop2.getShopId()!=null){
//                        Long shopid=map.get(shop2.getShopId());
//                        if(shopid!=null){
//                            ifcan=false;
//                            break;
//                        }
//                    }
//                }
//            }
			List<ShopBrandDTO> listbrand = executeResult.getResult().getRows();
			for (int i = 0; i < listbrand.size(); i++) {
				// 如果店铺具有该类目下的品牌，就不能被删除
				if (listbrand.get(i).getCid().longValue() == Long.parseLong(cid)) {
					ifcan = false;
					break;
				}
			}
			//2015-8-11宋文斌end
        }
        return ifcan;
    }
    /**
     * 品牌列表展示
     * @return
     */
	@RequestMapping(value = "brandsList")
    public String queryAllBrandsList(@ModelAttribute("itemBrandDTO")ItemBrandDTO itemBrandDTO,@ModelAttribute("pager") Pager<?> pager,Model model){
        if(itemBrandDTO.getBrandStatus()==null){
		  itemBrandDTO.setBrandStatus(1);//查询品牌有效的
    	}
        if("".equals(itemBrandDTO.getBrandName())){
        	itemBrandDTO.setBrandName(null);
        }
        if(itemBrandDTO.getBrandStatus()==0){
        	itemBrandDTO.setBrandStatus(null);
        }
    	ExecuteResult<DataGrid<ItemBrandDTO>> itembrandDtos=itemBrandExportService.queryBrandList(itemBrandDTO, pager);
    	Page<ItemBrandDTO> p = new Page<ItemBrandDTO>();
		p.setPageNo(pager.getPage());
		p.setPageSize(pager.getRows());
		p.setCount(itembrandDtos.getResult().getTotal());
		p.setList(itembrandDtos.getResult().getRows());
		p.setOrderBy(pager.getOrder());
		model.addAttribute("itembrandDtos", itembrandDtos);
        model.addAttribute("page",p);
        return  "brand/brandsList";
        
    }
	/**
	 * 删除品牌
	 * @param brandId
	 */
	@RequestMapping(value = "deleteBrand")
    @ResponseBody
	public Map deleteBrand(String brandId,Model model){
		Map map = new HashMap();
		ExecuteResult<String> result=itemBrandExportService.deleteBrand(Long.valueOf(brandId));
		if(result.isSuccess()){
			if("1".equals(result.getResult())){
				 map.put("success",true);
				map.put("msg","删除成功");
			}else{
				 map.put("success",false);
				map.put("msg","删除失败,该品牌和类目有关联不能删除");
			}
		}else{
			map.put("success",false);
			map.put("msg","删除失败,该品牌和类目有关联不能删除");
		}
		
		return map;
	}
}
