package com.camelot.ecm.goodscenter.usedmarket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.ecm.goodscenter.view.ItemOldInDTOView;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemOldDTO;
import com.camelot.goodscenter.dto.ItemPictureDTO;
import com.camelot.goodscenter.dto.outdto.ItemOldOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemOldExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.service.UserExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * <p>Description: [二手市场商品审核]</p>
 * Created on 2015年5月5日
 * @author  王东晓
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Controller
@RequestMapping(value = "${adminPath}/usedGoods")
public class UsedGoodsController {
	
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
    private ShopExportService shopExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ItemOldExportService itemOldExportService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	/**
     * <p>Description: [二手商品查询]</p>
     * Created on 2015年5月5日
     * @author  王东晓
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	@RequestMapping(value="getUsedGoodsList")
	public String getUsedGoodsList(@ModelAttribute("oldGoods") ItemOldInDTOView oldGoods,String createdStart,String createdEnd,@ModelAttribute("pager") Pager<?> pager, Model model){

		//获取平台类目信息
		DataGrid<ItemCategoryDTO> dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList<Object>());
        }
        //获取第二平台类目信息
        List<ItemCategoryDTO> twoList = new ArrayList<ItemCategoryDTO>();
        if(oldGoods.getPlatformId1() != null && oldGoods.getPlatformId1() > 0){
        	twoList = itemCategoryService.queryItemCategoryList(Long.valueOf(oldGoods.getPlatformId1())).getRows();
        }
        //获取第三平台类目信息
        List<ItemCategoryDTO> thirdList = new ArrayList<ItemCategoryDTO>();
        if(oldGoods.getPlatformId2()!=null&&oldGoods.getPlatformId2()>0){
        	thirdList = itemCategoryService.queryItemCategoryList(Long.valueOf(oldGoods.getPlatformId2())).getRows();
        }
       
        model.addAttribute("thirdList", thirdList);
        model.addAttribute("twoList", twoList);
        model.addAttribute("oldGoods",oldGoods);
		//获取二手商品信息,获取前台传递过来的查询条件
        //可以看到的二手商品状态：2、待审核 ；20、审核驳回；3、待上架（审核通过）
        ItemOldDTO itemOldDTO = new ItemOldDTO();
        if(oldGoods.getItemOldInDTO()!=null){
        	itemOldDTO = oldGoods.getItemOldInDTO().getItemOldDTO();
        	//向二手商品实体中添加查询条件
    		//添加类目查询条件，获取到查询条件中的类目信息
    		if(oldGoods.getCid()!=null&&oldGoods.getCid()>0){
    			itemOldDTO.setCid(Long.valueOf(oldGoods.getCid()));
    		}
    		//查询类目优先级 3>2>1
        	if(oldGoods.getCid()!=null&&oldGoods.getCid()>0){
        		itemOldDTO.setCid(Long.valueOf(oldGoods.getCid()));
        	}else if (oldGoods.getPlatformId2()!=null && oldGoods.getPlatformId2()>0){
        		itemOldDTO.setCid(Long.valueOf(oldGoods.getPlatformId2()));
        	}else if(oldGoods.getPlatformId1()!=null && oldGoods.getPlatformId1()>0){
        		itemOldDTO.setCid(Long.valueOf(oldGoods.getPlatformId1()));
        	}
    		//添加发布时间查询条件，转化前台传递过来的字符串类型日期为Date类型
    		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
    		if(StringUtils.isNotBlank(createdStart)){
    			Date createdstrDate = null;
				try {
					createdstrDate = sdf.parse(createdStart);
				} catch (ParseException e) {
					logger.error("二手商品审核列表查询，传入的起始日期类型解析失败",e);
				}
    			itemOldDTO.setCreatedstr(createdstrDate);
    	    }
    		if(StringUtils.isNotBlank(createdEnd)){
    			Date createdendDate=null;
				try {
					createdendDate = sdf.parse(createdEnd);
				} catch (ParseException e) {
					logger.error("二手商品审核列表查询，传入的截至日期类型解析失败",e);
				}
    			itemOldDTO.setCreatedend(createdendDate);
    	    }
    		//获取查询条件中的审核状态，传递到itemOldDTO中的statuss数组中,如果查询条件中不存在status，则查询全部状态（2待审核、3审核通过、20审核驳回、4在售、5已下架）
    		
    		if(itemOldDTO.getStatus()!=null){
    			Integer[] statuss = {itemOldDTO.getStatus()}; 
    			itemOldDTO.setStatuss(statuss);
    		}
			
        }
        //添加需要查询的商品审核状态，如果想查询所有状态（2待审核、3审核通过、20审核驳回、4在售、5已下架）的商品，需要将三个状态都传递到itemOldDTO中的statuss数组中
        if(itemOldDTO.getStatus()==null){
			Integer[] statuss = {2,20,3,4,5}; 
			itemOldDTO.setStatuss(statuss);
        }
      
		//根据上面代码获得的ItemOldDTO实体，调用dubbo服务，获取到二手商品信息列表
		ExecuteResult<DataGrid<ItemOldDTO>> excuteResult = itemOldExportService.queryItemOld(itemOldDTO, pager);
		
		//如果调用dubbo服务返回成功
		if(excuteResult.isSuccess()){
			List<ItemOldDTO> itemOldList = excuteResult.getResult().getRows();
			for(int i = 0 ; i < itemOldList.size() ;i++){
				ItemOldDTO itemOld = itemOldList.get(i);
				if(itemOld.getCid()!=null){
					Map<String,String> categoryMap= this.getCategoryName(itemOld.getCid());
					String cName = "";
					if(categoryMap.get("oneName")!=null){
						cName = categoryMap.get("oneName")+"/";
					}
					if(categoryMap.get("twoName")!=null){
						cName =cName+ categoryMap.get("twoName")+"/";
					}
					if(categoryMap.get("threeName")!=null){
						cName =cName+ categoryMap.get("threeName");
					}
					itemOld.setcName(cName);
				}
				
			}
			
			//前台传递二手产品信息列表
			model.addAttribute("itemOldLists",itemOldList);
		}else{
			model.addAttribute("errormsgList", excuteResult.getErrorMessages());
		}
		//页面分页
		Page<ItemOldDTO> page = new Page<ItemOldDTO>();
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		page.setCount(excuteResult.getResult().getTotal());
		page.setList(excuteResult.getResult().getRows());
		model.addAttribute("page", page);
		return "goodscenter/usedmarket/usedgoodsList";
	}
	
	public Long[] getShopIdsByName(String shopName){
    	//int size = 100;
    	Long[] numbers = null;
    	ShopDTO shopDTO = new ShopDTO();
    	shopDTO.setShopName(shopName);
    	Pager<ShopDTO> pager = new Pager<ShopDTO>();
    	pager.setPage(1);
    	pager.setRows(100);
    	ExecuteResult<DataGrid<ShopDTO>> result =shopExportService.findShopInfoByCondition(shopDTO, pager);
    	List<ShopDTO> shopList = result.getResult().getRows();
    	if(shopList.size()>0){
    		numbers = new Long[shopList.size()];
    		int num = 0;
    		for(ShopDTO shopDTOModel : shopList){
    			//longList.add(shopDTOModel.getShopId());
    			numbers[num] = shopDTOModel.getShopId();
    		}
    	}
    	//numbers = (Long[])longList.toArray();
    	return numbers;
    }
	/**
     * <p>Description: [二手商品批量审核通过,根据前台传递过来的itemId数组，修改二手商品的状态]</p>
     * Created on 2015年5月6日
     * @author  王东晓
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	@RequestMapping(value="passAll")
	@ResponseBody
	public ExecuteResult<String> passAll(Long[] checkAll){
		String uid = UserUtils.getUser().getId();
		if(StringUtils.isEmpty(uid)){
			return null;
		}
		ExecuteResult<String> executeResult = itemOldExportService.modifyItemOldStatus("",uid,3L, checkAll);
		return executeResult;
	}
	/**
     * <p>Description: [二手商品批量审核驳回，根据前台传递过来的itemId数组，修改二手商品的状态]</p>
     * Created on 2015年5月6日
     * @author  王东晓
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	@RequestMapping(value="rejectAll")
	@ResponseBody
	public ExecuteResult<String> rejectAll(String comment,Long[] checkAll){
		String  uid = UserUtils.getUser().getId();
		if(StringUtils.isEmpty(uid)){
			return null;
		}
		ExecuteResult<String> executeResult = itemOldExportService.modifyItemOldStatus(comment,uid,20L, checkAll);
		return executeResult;
	}
	/**
     * <p>Description: [二手商品批量删除,根据前台传递过来的itemId数组，修改二手商品的状态]</p>
     * Created on 2015年5月6日
     * @author  王东晓
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	@RequestMapping(value="removeAll")
	@ResponseBody
	public ExecuteResult<String> removeAll(Long[] checkAll){
		String uid = UserUtils.getUser().getId();
		if(StringUtils.isEmpty(uid)){
			return null;
		}
		ExecuteResult<String> executeResult = itemOldExportService.modifyItemOldStatus("","0",8L, checkAll);
		return executeResult;
	}
	/**
     * <p>Description: [二手商品详细信息查看]</p>
     * Created on 2015年5月7日
     * @author  王东晓
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	@RequestMapping(value="viewUsedGoods")
	public String viewUsedGoods(Long itemId,Model model){
		//根据前台传递过来的itemId获取到该itemId的详细属性
		ExecuteResult<ItemOldOutDTO> executeResult = itemOldExportService.getItemOld(itemId);
		//如果调用dubbo服务不成功
		if(!executeResult.isSuccess()){
			model.addAttribute("msg","商品查询失败");
			return "goodscenter/usedmarket/usedgoodsView";
		}
		ItemOldDTO itemOldDTO = executeResult.getResult().getItemOldDTO();
		List<ItemPictureDTO> itemOldPicList = executeResult.getResult().getItemPictureDTO(); 
		//把商品实体类中的属性传递到前台页面
		model.addAttribute("itemOldDTO", itemOldDTO);
		//商品的图片信息传递到前台页面
		model.addAttribute("itemOldPicList",itemOldPicList);
		/****获取商品的基本信息*****/
        //获取商品的cid类目编码
        Long cid = itemOldDTO.getCid();
        Map<String,String> categoryMap= this.getCategoryName(cid);
        
        model.addAttribute("oneName", categoryMap.get("oneName"));
        model.addAttribute("twoName", categoryMap.get("twoName"));
        model.addAttribute("threeName", categoryMap.get("threeName"));
        
		return "goodscenter/usedmarket/usedgoodsView";
	}
	
	/**
	 * 根据第三级类目获取平台一二级类目
	 * 
	 * @param cid 平台三级类目id
	 */
	@SuppressWarnings("null")
	private Map<String,String> getCategoryName(long cid){
		Map<String,String> resultMap = new HashMap<String,String>();
		 //获取三级类目的父类目信息
        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory = itemCategoryService.queryParentCategoryList(cid);
        //如果获取类目不成功
        //遍历父类类目，获取到一级类目和二级类目、三级类目
        for(ItemCatCascadeDTO itemCatCascadeDTOOne :resultCategory.getResult()){
        	for(ItemCatCascadeDTO itemCatCascadeDTOTwo :itemCatCascadeDTOOne.getChildCats()){
        		for(ItemCatCascadeDTO itemCatCascadeDTOThree :itemCatCascadeDTOTwo.getChildCats()){
        			if(cid==itemCatCascadeDTOThree.getCid()){
						resultMap.put("threeName", itemCatCascadeDTOThree.getCname());
						resultMap.put("oneName", itemCatCascadeDTOOne.getCname());
						resultMap.put("twoName", itemCatCascadeDTOTwo.getCname());
						break ;
					}
        		}
        	}
        	
        }
        return resultMap;
	}
}
