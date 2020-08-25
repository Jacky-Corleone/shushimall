package com.camelot.ecm.goodscenter.itemcategory;

import com.camelot.common.Json;
import com.camelot.goodscenter.dto.*;
import com.camelot.goodscenter.service.ItemAttrValueItemExportService;
import com.camelot.goodscenter.service.ItemAttributeExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * <p>Description: [类目管理]</p>
 * Created on 2015年2月28日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("${adminPath}/item")
public class ItemCategoryController {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
    private ItemAttributeExportService itemAttributeExportService;
    private ItemAttrValueItemExportService itemAttrValueItemExportService;
    @RequestMapping("index")
    public String index() {
        return "/goodscenter/itemcategory/index";
    }

    @ResponseBody
    @RequestMapping(value = "updateattrname")
    public Json updateAttrName(HttpServletRequest request,String[] ids){
        Json json=new Json();
        try{
            if(ids!=null&&ids.length>0){
                for(String id:ids){
                    if(id!=null&&!"".equals(id)){
                        String name=request.getParameter("name"+id);
                        String senior = request.getParameter("senior"+id);
                        if(name!=null&&!"".equals(name)){
                            ItemAttr itemAttr=new ItemAttr();
                            itemAttr.setStatus(1);
                            itemAttr.setId(new Long(id));
                            itemAttr.setName(name);
                            itemAttr.setIsSenior(Integer.parseInt(senior));
                            itemAttributeExportService.modifyItemAttribute(itemAttr);
                        }
                    }
                }
                json.setMsg("属性修改完成");
                json.setSuccess(true);
            }else{
                json.setMsg("属性没有改变，不需要修改");
                json.setSuccess(false);
            }
        }catch(Exception e){
            json.setMsg("系统异常，请稍后再试");
            json.setSuccess(false);
            logger.error("修改类目属性时出现异常",e);
        }
        return json;
    }
    @ResponseBody
    @RequestMapping(value = "updateattrnamevalue")
    public Json updateAttrNameValue(HttpServletRequest request,String[] ids){
        Json json=new Json();
        try{
            if(ids!=null&&ids.length>0){
                for(String id:ids){
                    if(id!=null&&!"".equals(id)){
                        String name=request.getParameter("name"+id);
                        if(name!=null&&!"".equals(name)){
                            ItemAttrValue itemAttr=new ItemAttrValue();
                            itemAttr.setStatus(1);
                            itemAttr.setId(new Long(id));
                            itemAttr.setName(name);
                            itemAttributeExportService.modifyItemAttrValue(itemAttr);
                        }
                    }
                }
                json.setMsg("属性值修改完成");
                json.setSuccess(true);
            }else{
                json.setMsg("属性值没有改变，不需要修改");
                json.setSuccess(false);
            }
        }catch(Exception e){
            json.setMsg("系统异常，请稍后再试");
            json.setSuccess(false);
            logger.error("修改类目属性值时出现异常",e);
        }
        return json;
    }
    @ResponseBody
    @RequestMapping("/datagrid")
    public DataGrid<ItemCategoryDTO> datagrid(Long parentCid){
        if(parentCid==null){
            parentCid = 0L;
        }
        DataGrid<ItemCategoryDTO> dg = itemCategoryService.queryItemCategoryList(parentCid);
        return dg;
    }
    //类目属性查询
    @ResponseBody
    @RequestMapping("/queryCategoryAttr")
    public DataGrid<CategoryAttrDTO> queryCategoryAttr(Long cid, Integer attrType){
        DataGrid<CategoryAttrDTO> dg = itemCategoryService.queryCategoryAttrList(cid, attrType);
        return dg;
    }
    
    //发布商品时用---类目属性查询,新属性接口
    @ResponseBody
    @RequestMapping("/queryCategoryAttr2")
    public List<ItemAttr> queryCategoryAttr2(CatAttrSellerDTO catAttrSellerDTO){
//        private Long sellerId;//卖家ID 必填
//        private Long shopId;//商家ID 必填
//        private Long cid;//平台类目ID 必填
//        private Integer attrType;//属性类型:1:销售属性;2:非销售属性 必填

        ExecuteResult<List<ItemAttr>> result = itemAttributeExportService.addItemAttrValueBack(catAttrSellerDTO,2);

        return result.getResult();
    }
    //编辑商品时用--类目属性查询,新属性接口
    @ResponseBody
    @RequestMapping("/queryCategoryAttr3")
    public List<ItemAttr> queryCategoryAttr3(ItemAttrValueItemDTO itemAttrValueItemDTO){
//        private java.lang.Long itemId;//  商品ID
//        private java.lang.Integer attrType;//  属性类型:1:销售属性;2:非销售属性
        ExecuteResult<List<ItemAttr>> result = itemAttrValueItemExportService.queryItemAttrValueItem(itemAttrValueItemDTO);
        return result.getResult();
    }
    //添加类目
    @ResponseBody
    @RequestMapping("/addItemCategory")
    public ExecuteResult<String> addItemCategory(Long categoryParentCid, String categoryCName, Integer categoryLev){
        ExecuteResult<String> result = new ExecuteResult<String>();
        List<String> error = new ArrayList<String>();
        if(categoryParentCid==null){
            error.add("父级categoryParentCid不能为空！");
        }
        if(StringUtils.isEmpty(categoryCName)){
            error.add("类目名称categoryCName不能为空！");
        }
        if(categoryLev!=1 && categoryLev!=2 && categoryLev!=3){
            error.add("类目级别categoryLev值不合法！");
        }
        if(error.size()>0){
            result.setErrorMessages(error);
            return result;
        }else{
            ItemCategoryDTO itemCategoryDTO = new ItemCategoryDTO();
            itemCategoryDTO.setCategoryParentCid(categoryParentCid);
            itemCategoryDTO.setCategoryCName(categoryCName);
            itemCategoryDTO.setCategoryLev(categoryLev);
            result = itemCategoryService.addItemCategory(itemCategoryDTO);
            //System.out.println("--------"+JSON.toJSONString(result));
            return result;
        }

    }
    //添加类目属性名称
    @ResponseBody
    @RequestMapping("/addAttrName")
    public ExecuteResult<Long> addAttrName(Long cid, String attrName, Integer attrType,Integer isSenior){

        ExecuteResult<Long> result = new ExecuteResult<Long>();

        List<String> error = new ArrayList<String>();
        if(cid==null){
            error.add("类目cid不能为空！");
        }
        if(StringUtils.isEmpty(attrName)){
            error.add("属性名attrName不能为空！");
        }
        if(attrType!=1 && attrType!=2){
            error.add("属性类型值不合法！");
        }
        if(error.size()>0){
            result.setErrorMessages(error);
            return result;
        }else{
            result = itemCategoryService.addCategoryAttr(cid, attrName, attrType,isSenior);
//			System.out.println("添加类目属性名称--------"+JSON.toJSONString(result));
            return result;
        }
    }
    
    //添加类目属性值
    @RequestMapping("/addValueName")
    @ResponseBody
    public ExecuteResult<Long> addValueName(Long cid, Long attrId, String valueNames){
        ExecuteResult<Long> result = new ExecuteResult<Long>();
        List<String> error = new ArrayList<String>();
        if(cid==null){
            error.add("类目cid不能为空！");
        }
        if(StringUtils.isEmpty(valueNames)){
            error.add("属性值不能为空！");
        }
        if(attrId==null){
            error.add("属性Id不能为空！");
        }
        if(error.size()>0){
            result.setErrorMessages(error);
            return result;
        }else{
            String[] names = valueNames.split(",");
            if(names.length>0){
                for(String valuename : names){

                    ExecuteResult<Long> resultLong = itemCategoryService.addCategoryAttrValue(cid, attrId, valuename);
//					System.out.println("添加类目属性值--------"+JSON.toJSONString(resultLong));
                }
            }
            return result;
        }
    }

    /**
     * 添加类目属性
     * @param cid
     * @param attrType
     * @param itemName
     * @param itemValues
     * @return
     */

    @RequestMapping(value = "addItemAttr")
    @ResponseBody
    public Map addItemAttr(Long cid,Integer attrType,Long itemAttrId, String itemName,@RequestParam(required=false)String[] itemValues){
        Map json = new HashMap();
        try {
            json.put("success",true);
            json.put("msg","添加成功");
            if(cid==null){
                json.put("msg","类目cid不能为空！");
                json.put("success", false);
                return json;
            }
            if(StringUtils.isEmpty(itemName)){
                json.put("msg","属性名不能为空！");
                json.put("success",false);
                return json;
            }
            if(attrType!=1 && attrType!=2){
                json.put("msg","请确认要添加的属性类型！");
                json.put("success",false);
                return json;
            }
            if(null == itemAttrId || 0L == itemAttrId){
                ExecuteResult result = itemCategoryService.addCategoryAttr(cid, itemName, attrType,0);
                if (result.isSuccess()){
                    itemAttrId = (Long)result.getResult();
                    json.put("itemAttrId",itemAttrId);
                    json.put("msg","添加成功");
                    json.put("success",true);
                }else{
                    json.put("msg","添加失败");
                    json.put("success",false);
                }

            }
            List itemValueList = new ArrayList();
            if (itemValues!=null&&itemValues.length>0){
                for (int i=0;i<itemValues.length; i++) {
                    Map m = new HashMap();
                    m.put("itemAttrValueId",itemCategoryService.addCategoryAttrValue(cid, itemAttrId, itemValues[i]).getResult());
                    m.put("itemAttrValueName",itemValues[i]);
                    itemValueList.add(m);
                }
            }
            json.put("itemValues",itemValueList);
        }catch (Exception e){
            json.put("msg","添加失败"+e.getMessage());
            json.put("success",false);
        }

        return json;
    }
    

	/**
	 * 
	 * <p>Description: [根据商品属性删除商品类别属性关系]</p>
	 * Created on 2015年8月16日
	 * @param cid
	 * @param attr_id
	 * @param attrType
	 * @return
	 * @author:[宋文斌]
	 */
    @ResponseBody
    @RequestMapping("/deleteCategoryAttr")
    public ExecuteResult<String> deleteCategoryAttr(Long cid,Long attr_id, Integer attrType){
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			result = itemCategoryService.deleteCategoryAttr(cid,attr_id, attrType);
		} catch (Exception e) {
			result.addErrorMessage("删除属性时出现异常，请稍后再试或联系管理员");
			logger.error("删除属性" + e.getMessage());
		}
    	return result;
    }
    
    /**
     * 
     * <p>Description: [根据商品属性值删除商品类别属性值关系]</p>
     * Created on 2015年8月16日
     * @param cid
     * @param attr_id
     * @param value_id
     * @param attrType
     * @return
     * @author:[宋文斌]
     */
    @ResponseBody
    @RequestMapping("/deleteCategoryAttrValue")
    public ExecuteResult<String> deleteCategoryAttrValue(Long cid, Long attr_id, Long value_id, Integer attrType){
    	ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			result = itemCategoryService.deleteCategoryAttrValue(cid, attr_id,
					value_id, attrType);
		} catch (Exception e) {
			result.addErrorMessage("删除属性值时出现异常，请稍后再试或联系管理员");
			logger.error("删除属性值" + e.getMessage());
		}
    	return result;
    }

    /**
     * usr 新版本商品发布页面的添加类目属性
     * @param attrType
     * @param itemAttrId
     * @param itemName
     * @param itemValues
     * @return
     */
    @RequestMapping(value = "addItemAttr2")
    @ResponseBody
    public Map addItemAttr2(Integer attrType,Long itemAttrId, String itemName,String[] itemValues){
        Map json = new HashMap();
        try {
            json.put("success",true);
            json.put("msg","添加成功");

            if(StringUtils.isEmpty(itemName)){
                json.put("msg","属性名不能为空！");
                json.put("success",false);
                return json;
            }
            if(attrType!=1 && attrType!=2){
                json.put("msg","请确认要添加的属性类型！");
                json.put("success",false);
                return json;
            }
            if(null == itemAttrId || 0L == itemAttrId){
                ItemAttr itemAttr = new ItemAttr();
                itemAttr.setName(itemName);
                ExecuteResult<ItemAttr> result = itemAttributeExportService.addItemAttribute(itemAttr);

                if (result.isSuccess()){
                    itemAttrId = result.getResult().getId();
                    json.put("itemAttrId",itemAttrId);
                    json.put("msg","添加成功");
                    json.put("success",true);
                }else{
                    json.put("msg","添加失败");
                    json.put("success",false);
                }
            }else{
                ItemAttr itemAttr = new ItemAttr();
                itemAttr.setId(itemAttrId);
                itemAttr.setName(itemName);
                ExecuteResult<ItemAttr> result = itemAttributeExportService.modifyItemAttribute(itemAttr);
                if (result.isSuccess()){
//                    itemAttrId = result.getResult().getId();
                    json.put("itemAttrId",itemAttrId);
                    json.put("msg","修改成功");
                    json.put("success",true);
                }else{
                    json.put("msg","修改失败");
                    json.put("success",false);
                }
            }
            List itemValueList = new ArrayList();
            if (itemValues!=null&&itemValues.length>0){
                for (int i=0;i<itemValues.length; i++) {
                    Map m = new HashMap();
                    ItemAttrValue itemAttrValue = new ItemAttrValue();
                    itemAttrValue.setAttrId(itemAttrId);

                    String[] values = itemValues[i].split("#");
                    if(null!=values[0]&&!"".equals(values[0])){
                        Long itemAttrValueId = new Long(values[0]);
                        itemAttrValue.setId(itemAttrValueId);
                        itemAttrValue.setName(values[1]);
                        ExecuteResult<ItemAttrValue> valueResult = itemAttributeExportService.modifyItemAttrValue(itemAttrValue);
                        m.put("itemAttrValueId",values[0]);
                        m.put("itemAttrValueName",values[1]);
                        itemValueList.add(m);
                    }else{
                        itemAttrValue.setName(values[1]);
                        ExecuteResult<ItemAttrValue> valueResult = itemAttributeExportService.addItemAttrValue(itemAttrValue);
                        m.put("itemAttrValueId",valueResult.getResult().getId());
                        m.put("itemAttrValueName",valueResult.getResult().getName());
                        itemValueList.add(m);
                    }

                }
            }
            json.put("itemValues",itemValueList);
        }catch (Exception e){
            logger.error("添加出现异常",e);
            json.put("msg","添加失败"+e.getMessage());
            json.put("success",false);
        }

        return json;
    }
    @RequestMapping(value = "canclecid")
    @ResponseBody
    public Json cancleCid(HttpServletRequest request){
        Json json=new Json();
        try{
            String cid=request.getParameter("cid");
            if(cid!=null&&!"".equals(cid)){
                ExecuteResult<String> executeResult=itemCategoryService.deleteItemCategory(new Long(cid));
                if(executeResult.isSuccess()){
                    json.setMsg("删除成功");
                    json.setSuccess(true);
                }else{
                    if(executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
                        String restul="";
                        List<String> listerror=executeResult.getErrorMessages();
                        Iterator<String> iterator=listerror.iterator();
                        while(iterator.hasNext()){
                            if(restul==null||"".equals(restul)){
                                restul=iterator.next();
                            }else{
                                restul=restul+"；"+iterator.next();
                            }
                        }
                        json.setMsg(restul);
                        json.setSuccess(false);
                    }else{
                        json.setMsg("删除失败，可能类目下存在品牌");
                        json.setSuccess(false);
                    }
                }
            }else{
                json.setMsg("后台没有获取要删除的类目信息，无法删除");
                json.setSuccess(false);
            }
        }catch(Exception e){
            json.setMsg("删除类目时出现异常，请稍后再试或联系管理员");
            json.setSuccess(false);
            logger.error("删除类目"+e.getMessage());
        }
        return json;
    }
    @RequestMapping(value = "editCategory")
    @ResponseBody
    public Json editCategory(HttpServletRequest request){
        Json json=new Json();
        try{
        	ItemCategoryDTO dto=new ItemCategoryDTO();
        	String cid=request.getParameter("cid");
        	String categoryName=request.getParameter("name");
        	dto.setCategoryCid(Long.parseLong(cid));
        	dto.setCategoryCName(categoryName);
            if(cid!=null&&!"".equals(cid)){
                ExecuteResult<String> executeResult=itemCategoryService.updateCategory(dto);
                if(executeResult.isSuccess()){
                    json.setMsg("修改成功");
                    json.setSuccess(true);
                }else{
                    if(executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
                        String restul="";
                        List<String> listerror=executeResult.getErrorMessages();
                        Iterator<String> iterator=listerror.iterator();
                        while(iterator.hasNext()){
                            if(restul==null||"".equals(restul)){
                                restul=iterator.next();
                            }else{
                                restul=restul+"；"+iterator.next();
                            }
                        }
                        json.setMsg(restul);
                        json.setSuccess(false);
                    }
                }
            }else{
                json.setMsg("后台没有获取要修改的类目信息，无法修改");
                json.setSuccess(false);
            }
        }catch(Exception e){
            json.setMsg("修改类目时出现异常，请稍后再试或联系管理员");
            json.setSuccess(false);
            logger.error("修改类目"+e.getMessage());
        }
        return json;
    }
}
