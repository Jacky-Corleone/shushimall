package com.camelot.mall.sellcenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.mall.dto.ShopCategoryChildDTO;
import com.camelot.mall.dto.ShopCategoryPageDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.JsonHelper;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [店铺类目管理]</p>
 * Created on 2015年3月6日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/categoryController")
public class ShopCategoryController {
    private Logger LOG = LoggerFactory.getLogger(ShopCategoryController.class);

	@Resource
	private ShopCategorySellerExportService shopCategorySellerExportService;
	
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private UserExtendsService userExtendsService;
	
	@Resource
	private SearchItemExportService searchItemExportService;
	
	@RequestMapping("/tolist")
	public ModelAndView tolist(HttpServletRequest request){
		ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
		dto.setShopId(WebUtil.getInstance().getShopId(request));
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(dto, null);
		
		List<ShopCategorySellerDTO> list = result.getResult().getRows();
		
		return new ModelAndView("/sellcenter/shop/shopCategory", "categorylist", buildCategoryLev(list));
	}

	//组装list：将子类目加入到对应的父类目中去
	private List<ShopCategorySellerDTO> buildCategoryLev(List<ShopCategorySellerDTO> list) {
		List<ShopCategorySellerDTO> buildlist = new ArrayList<ShopCategorySellerDTO>();
		for(int i=0; null!=list && i<list.size(); i++){
			ShopCategorySellerDTO scParent = list.get(i);
			for(int j=0; j<list.size(); j++){
				//将一级类目的所有二级类目加入到一级类目的list中
				ShopCategorySellerDTO scChild = list.get(j);
				if(scParent.getCid() == scChild.getParentCid()){
					scParent.getChildShopCategory().add(scChild);
				}
			}
			//先将一级类目全部加入到buildlist中
			if(scParent.getLev()==1){
				buildlist.add(scParent);
			}
		}
		return buildlist;
	}
	
	/**
	 * 需要在首页显示该店铺类目
	 */
	@RequestMapping("/homeShow")
	public ModelAndView homeShow(Long id, HttpServletRequest request, HttpServletResponse response){
		try{
			upHomeShowStatus(id, 1);
			JsonHelper.success(response);
		}catch(Exception e){
			JsonHelper.failure(response);
		}
		return null;
	}

	private void upHomeShowStatus(Long id, Integer status) {
		//更新父级类目
		ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
		dto.setCid(id);
		dto.setHomeShow(status);
		shopCategorySellerExportService.update(dto);
		//更新子类目状态
        List<ShopCategorySellerDTO> list = getCategoryByParentCid(id);
		for (int i = 0; list != null && i < list.size(); i++) {
			ShopCategorySellerDTO child = list.get(i);
			child.setHomeShow(status);
			shopCategorySellerExportService.update(child);
		}
	}
	
	/**
	 * 不需要在首页显示该店铺类目
	 */
	@RequestMapping("/homeNotShow")
	public ModelAndView homeNotShow(Long id, HttpServletRequest request, HttpServletResponse response){
		try{
			upHomeShowStatus(id, 2);
			JsonHelper.success(response);
		}catch(Exception e){
			JsonHelper.failure(response);
		}
		return null;
	}
	
	/**
	 * 更新类目名称
	 */
	@RequestMapping("/updateCName")
	public ModelAndView updateCName(Long id, String cname,Long parentCid, HttpServletResponse response){
		ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
		dto.setCid(id);
		dto.setCname(cname);
        if(null != parentCid){
            dto.setParentCid(parentCid);
        }
		try{
			shopCategorySellerExportService.update(dto);
			JsonHelper.success(response);			
		}catch(Exception e){
			JsonHelper.failure(response);
		}
		return null;
	}
	
	/**
	 * 删除类目:更新数据状态为删除
	 */
	@RequestMapping("/deleteShopCategory")
    @ResponseBody
	public Map<String, String> deleteShopCategory(Long id, HttpServletResponse response){
        Map<String, String> retMap = new HashMap<String, String>();
        retMap.put("result", "success");
        if(id==null){
            retMap.put("result", "failure");
            retMap.put("message", "服务器繁忙，请稍后再试！");
            return retMap;
        }
		try{
            List<ShopCategorySellerDTO> listCategory = getCategoryByParentCid(id);
			if(listCategory!=null && listCategory.size()>0){//有子类目：该类目为父级
                if(itemUnderListCategory(retMap, listCategory)){//校验子类目是否包含商品
                    return retMap;
                }
                updateDelFlag(listCategory);//批量更新类目及其子类目状态为删除
            }else{//无子类目：本身就是子类目
                if(containsItem(id)){
                    retMap.put("result", "failure");
                    retMap.put("message", "该类目下已有商品，无法删除！");
                    return retMap;
                }
            }
            updateDelFlag(id);
        }catch (Exception e) {
            retMap.put("result", "failure");
            retMap.put("message", "服务器繁忙，请稍后再试！");
            LOG.error("删除店铺类目出错："+e);
        }
        return retMap;
	}


	/**
	 * 需要在首页显示该店铺类目
	 */
	@RequestMapping("/expand")
	public ModelAndView expand(Long id,Integer expand, HttpServletResponse response){
		try{
			//更新父级类目
			ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
			dto.setCid(id);
			dto.setExpand(expand);
			shopCategorySellerExportService.update(dto);
			JsonHelper.success(response);
		}catch(Exception e){
			JsonHelper.failure(response);
		}
		return null;
	}
	
    /**
     * 更新单个商品状态为删除
     * @param cid
     */
    private void updateDelFlag(Long cid) {
        ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
        dto.setCid(cid);
        dto.setStatus(2);
        shopCategorySellerExportService.update(dto);
    }

    /**
     * 根据父级类目id获取所有子类目信息
     * @param parentId
     * @return
     */
    private List<ShopCategorySellerDTO> getCategoryByParentCid(Long parentId) {
        ShopCategorySellerDTO childDTO = new ShopCategorySellerDTO();
        childDTO.setParentCid(parentId);
        ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(childDTO, null);

        return result.getResult().getRows();
    }

    /**
     * 检查类目下是否包含商品
     * @param cid
     * @return
     */
    private boolean containsItem(Long cid) {
        SearchInDTO itemInDTO = new SearchInDTO();
        itemInDTO.setShopCid(cid);
        SearchOutDTO searchOutDTO = searchItemExportService.searchItem(itemInDTO, null);
        return searchOutDTO.getItemDTOs().getRows() != null && searchOutDTO.getItemDTOs().getRows().size() > 0;
    }

    /**
	 * 删除全部类目:更新数据状态为删除
	 */
	@RequestMapping("/deleteAll")
    @ResponseBody
	public Map<String, String> deleteAll(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> retMap = new HashMap<String, String>();
        retMap.put("result", "success");
        Long shopId = WebUtil.getInstance().getShopId(request);
		if(shopId == null){
			return null;
		}
		try {
            //店铺下有商品则不能删除所有
			ShopCategorySellerDTO shopCategorySellerDTO = new ShopCategorySellerDTO();
			shopCategorySellerDTO.setShopId(shopId);
			ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO, null);
			List<ShopCategorySellerDTO> list = result.getResult().getRows();
			//任何一个类目下有商品都不能删除
            if (itemUnderListCategory(retMap, list)) {
                return retMap;
            }
            updateDelFlag(list);
        } catch (Exception e) {
            retMap.put("result", "failure");
            retMap.put("message", "服务器繁忙，请稍后再试！");
            LOG.error("删除店铺类目出错："+e);
        }
		return retMap;
	}

    /**
     * 批量更新类目状态为删除
     * @param list
     */
    private void updateDelFlag(List<ShopCategorySellerDTO> list) {
        for (int i = 0; list != null && i < list.size(); i++) {
            ShopCategorySellerDTO dto = list.get(i);
            dto.setStatus(2);
            shopCategorySellerExportService.update(dto);
        }
    }

    /**
     * 验证一个类目集合中是否包含商品
     * @param retMap
     * @param list
     * @return
     */
    private boolean itemUnderListCategory(Map<String, String> retMap, List<ShopCategorySellerDTO> list) {
        for (int i = 0; list != null && i < list.size(); i++) {
            ShopCategorySellerDTO dto = list.get(i);
            if(containsItem(dto.getCid())){
                retMap.put("result", "failure");
                retMap.put("message", "类目【"+dto.getCname()+"】下已有商品，无法删除！");
                return true;
            }
        }
        return false;
    }

    /**
	 * 新增类目
	 */
	@RequestMapping("/saveShopCategory")
	@ResponseBody
	public Map<String, Object> saveShopCategory(ShopCategoryPageDTO shopCategoryDTO, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		result.put("msg", "保存成功！");

		Long userId = WebUtil.getInstance().getUserId(request);  //用户id（卖家id）
		Long shopId = WebUtil.getInstance().getShopId(request);   //店铺id
		
		//父级类目
		List<String> parentNames = shopCategoryDTO.getParentName();
        addParentCg(userId, shopId, parentNames);

        //子级类目
        List<ShopCategoryChildDTO> childNames = shopCategoryDTO.getChildList();
        addChildCg(userId, shopId, childNames);

        return result;
	}

    /**
     * 新增子级类目
     * @param userId
     * @param shopId
     * @param childNames
     */
    private void addChildCg(Long userId, Long shopId, List<ShopCategoryChildDTO> childNames) {
        //新增子级类目
        for(int i=0; i<childNames.size(); i++){
            ShopCategoryChildDTO childCg = childNames.get(i);
            if(StringUtils.isBlank(childCg.getChildCategoryName())){
                continue;
            }
            addCategory(childCg.getParentCid(), childCg.getChildCategoryName(), shopId, userId);
        }
    }

    /**
     * 新增父级类目
     * @param userId 用户id
     * @param shopId 店铺id
     * @param parentNames 父级类目名称集合
     */
    private void addParentCg(Long userId, Long shopId, List<String> parentNames) {
        //新增父级类目
        for(int i=0; i<parentNames.size(); i++){
            if(StringUtils.isBlank(parentNames.get(i))){
                continue;
            }
            addCategory(0L, parentNames.get(i), shopId, userId);
        }
    }

    /**
	 * 新增类目
	 * @param parentCid
	 * @param cname
	 * @param shopId
	 * @param sellerId
	 */
	private void addCategory(Long parentCid, String cname, Long shopId, Long sellerId){
		ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
		dto.setExpand(2);  //默认不在首页展开
		dto.setHasLeaf(1); //默认为叶子借点
		if(parentCid != 0){
			dto.setLev(2);   //类目级别：父级类目为1级，子级类目为2级
			ShopCategorySellerDTO shopCategorySellerDTO = new ShopCategorySellerDTO();
			shopCategorySellerDTO.setCid(parentCid);
			ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO,null);
			List<ShopCategorySellerDTO> shopCategoryList = result.getResult().getRows();
			if(shopCategoryList != null && shopCategoryList.size() != 0){
				ShopCategorySellerDTO shopCategorySeller = shopCategoryList.get(0);
				dto.setHomeShow(shopCategorySeller.getHomeShow());//根据父级类目判断是否在前台显示
			}else{
				dto.setHomeShow(2);//默认不在前台显示该类目
			}
			
		}else{
			dto.setLev(1);   //类目级别：父级类目为1级，子级类目为2级
			dto.setHomeShow(2);//默认不在前台显示该类目
		}
		
		
		
		dto.setSellerId(sellerId);      //卖家id
		dto.setParentCid(parentCid);    //父级id
		dto.setShopId(shopId);          //店铺id
		dto.setSortNumber(1);      //排序默认在第一位
		dto.setStatus(1);  //状态默认为有效
		dto.setCname(cname);
		dto.setCreated(new Date());
		shopCategorySellerExportService.addShopCategory(dto);
	}
}
