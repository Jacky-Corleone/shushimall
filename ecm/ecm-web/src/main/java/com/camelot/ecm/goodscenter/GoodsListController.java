package com.camelot.ecm.goodscenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.camelot.common.Json;
import com.camelot.ecm.goodscenter.view.ItemQueryInDTOView;
import com.camelot.ecm.goodscenter.view.ItemQueryOutView;
import com.camelot.ecm.itemcategory.ItemService;
import com.camelot.goodscenter.dto.BrandOfShopDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValueItemDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.enums.HouseTypeEnum;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemAttrValueItemExportService;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.settlecenter.dto.SettleItemExpenseDTO;
import com.camelot.settlecenter.service.SettleItemExpenseExportService;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopFreightTemplateDTO;
import com.camelot.storecenter.dto.ShopRenovationDTO;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFreightTemplateService;
import com.camelot.storecenter.service.ShopRenovationExportService;
import com.thinkgem.jeesite.common.persistence.Page;

/**
 * Created by jiawg on 2015/2/26 0026.
 */
/**
 * 商品Controller
 * @author yuht
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/goodList")
public class GoodsListController
{
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ItemExportService itemExportService;
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
	private ItemBrandExportService itemBrandExportService;
    @Resource
	private ShopCategorySellerExportService shopCategorySellerExportService;
    @Resource
    private SettleItemExpenseExportService settleItemExpenseExportService;

    @Resource
    private ItemService itemService;

    @Resource
    private ItemAttrValueItemExportService itemAttrValueItemExportService;
    @Resource
    private ShopRenovationExportService shopRenovationExportService;
    @Resource
    private ShopFreightTemplateService shopFreightTemplateService;
    @Resource
	private ItemSkuPackageService itemSkuPackageService;

    @ResponseBody
    @RequestMapping(value="soldOutAll")
    public Map soldOutAll(Long[] checkAll){
        Map map = new HashMap();
    	ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
    	List<Long> longList = new ArrayList<Long>();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			longList.add(checkAll[i]);
    		}
    	}
    	itemStatusModifyDTO.setItemIds(longList);
    	itemStatusModifyDTO.setChangeReason("理由无");
    	itemStatusModifyDTO.setItemStatus(ItemStatusEnum.UNSHELVED.getCode());
    	itemStatusModifyDTO.setOperator(1);
        ExecuteResult<String> result = itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
    	//itemExportService.modifyItemStatusById(checkAll, "理由无", 5);
        if(result.isSuccess()){
            map.put("success",true);
            map.put("msg","下架成功");
        }else {
            map.put("success",false);
            map.put("msg",result.getErrorMessages());
        }
    	return map;
    }
    @ResponseBody
    @RequestMapping(value="isInShop")
    public Map isInshop(Long[] checkAll){
    	Map map = new HashMap();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			DataGrid<ShopRenovationDTO> res=shopRenovationExportService.queryShopRenovationByItemId(checkAll[i]);
    			if(!res.getRows().isEmpty()){
    				map.put("success",false);
    				map.put("msg","该平台库商品正在被卖家展示在商铺首页，下架此商品，会让商铺首页出现无效商品。");
    			}else{
    				map.put("success",true);
    			}
    		}
    	}
    	return map;
    }
    @ResponseBody
    @RequestMapping(value="isplatItemIdInShop")
    public Map isplatItemIdInShop(Long[] checkAll){
    Map map = new HashMap();
    ItemQueryInDTO dto=new ItemQueryInDTO();
    if(checkAll.length>0){
    for(int i=0;i<checkAll.length;i++){
		dto.setPlatItemId(checkAll[i]);
		DataGrid<ItemQueryOutDTO> result=itemExportService.queryItemList(dto,null);
		if(!result.getRows().isEmpty()){
			for(ItemQueryOutDTO outDto:result.getRows()){
				DataGrid<ShopRenovationDTO> res=shopRenovationExportService.queryShopRenovationByItemId(outDto.getItemId());
				if(!res.getRows().isEmpty()){
					map.put("success",false);
					map.put("msg","该平台库商品正在被卖家展示在商铺首页，下架此商品，会让商铺首页出现无效商品。");
				}else{
					map.put("success",true);
				}
			 }
		  }
       }
    }
    return map;
  }  
    
    @RequestMapping(value="soldGoodsOutAll")
    public String soldGoodsOutAll(Long[] checkAll){
    	ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
    	List<Long> longList = new ArrayList<Long>();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			longList.add(checkAll[i]);
    		}
    	}
    	itemStatusModifyDTO.setItemIds(longList);
    	itemStatusModifyDTO.setChangeReason("理由无");
    	itemStatusModifyDTO.setItemStatus(ItemStatusEnum.UNSHELVED.getCode());
    	itemStatusModifyDTO.setOperator(2);
    	itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
    	//itemExportService.modifyItemStatusById(checkAll, "理由无", 5);
    	
    	  return "redirect:"+SysProperties.getAdminPath()+"/goodList/goodsShopList"; 
    }
    @RequestMapping(value="passAll")
    @ResponseBody
    public Json passAll(Long[] checkAll,String auditStatus,String auditRemark,HttpServletRequest request,Model model){
        Json json=new Json();
        try{
            ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
            List<Long> longList = new ArrayList<Long>();
            if(checkAll.length>0){
                for(int i=0;i<checkAll.length;i++){
                    longList.add(checkAll[i]);
                }
            }
            itemStatusModifyDTO.setItemIds(longList);
            if(auditRemark!=null&&!"".equals(auditRemark)){
                itemStatusModifyDTO.setChangeReason(auditRemark);
            }else{
                itemStatusModifyDTO.setChangeReason("理由无");
            }
            itemStatusModifyDTO.setItemStatus(ItemStatusEnum.SHELVING.getCode());
            itemStatusModifyDTO.setOperator(1);
/*            if(auditStatus!=null&&"1".equals(auditStatus)){
                itemStatusModifyDTO.setCreatePlatItem(true);
            }else{
                itemStatusModifyDTO.setCreatePlatItem(false);
            }*/
            itemStatusModifyDTO.setCreatePlatItem(false);
            ExecuteResult<String> executeResult= itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
            //修改店铺装修中商品的价格
            for(Long itemId : longList){
//            	ExecuteResult<ShopTemplatesCombinDTO> resultMap =  shopRenovationExportService.queryShopRenovation(shopRenovationDTO);
            	DataGrid<ShopRenovationDTO> dataGrid = shopRenovationExportService.queryShopRenovationByItemId(itemId);
            	ExecuteResult<ItemDTO> itemDTO = itemExportService.getItemById(itemId);
            	if(dataGrid != null&&dataGrid.getRows() != null&&itemDTO != null&&itemDTO.getResult() != null){
            		for(ShopRenovationDTO shopRenovationDTO : dataGrid.getRows()){
            			boolean flag = true;
            			//如果是否有价格发生变化
            			if(shopRenovationDTO.getHasPrice().compareTo(itemDTO.getResult().getHasPrice())!=0){
            				shopRenovationDTO.setHasPrice(itemDTO.getResult().getHasPrice());
            				
            				flag = false;
            			}
            			//如果报价发生变化，则修改
            			if(shopRenovationDTO.getPrice().compareTo(itemDTO.getResult().getGuidePrice())!=0){
            				shopRenovationDTO.setPrice(itemDTO.getResult().getGuidePrice());
            				flag = false;
            			}
            			//商品图片是否发生变化
            			if(!shopRenovationDTO.getPictureUrl().equals(itemDTO.getResult().getPicUrls()[0])){
            				shopRenovationDTO.setPictureUrl(itemDTO.getResult().getPicUrls()[0]);
            				flag = false;
            			}
            			//商品名称是否发生变化
            			if(!shopRenovationDTO.getModuleName().equals(itemDTO.getResult().getItemName())){
            				shopRenovationDTO.setModuleName(itemDTO.getResult().getItemName());
            				flag = false;
            			}
            			if(flag == false){
            				ExecuteResult<String> result = shopRenovationExportService.modifyShopRenovation(shopRenovationDTO);
            			}
            		}
            	}
            }
            
            json.setMsg("审核完成");
            json.setSuccess(true);
        }catch(Exception e){
            logger.error(e.getMessage());
            json.setMsg("系统出现意外错误，请联系管理员");
            json.setSuccess(false);
        }
    	return json;
    }
    @ResponseBody
    @RequestMapping(value="rejectAll")
    public Map rejectAll(Long[] checkAll,@ModelAttribute("goods") ItemQueryInDTO goods,String auditRemark,@ModelAttribute("pager") Pager<?> pager,Model model){
    	Map map = new HashMap();
        ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
    	List<Long> longList = new ArrayList<Long>();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			longList.add(checkAll[i]);
    		}
    	}
    	itemStatusModifyDTO.setItemIds(longList);
    	if(auditRemark!=null&&!"".equals(auditRemark)){
    		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    		Matcher m = p.matcher(auditRemark);
    		auditRemark = m.replaceAll("");//去掉驳回原因中空格、回车、换行符、制表符，否则前台商城编辑商品会有问题
            itemStatusModifyDTO.setChangeReason(auditRemark);
        }else{
            itemStatusModifyDTO.setChangeReason("理由无");
        }
    	itemStatusModifyDTO.setItemStatus(ItemStatusEnum.REJECTED.getCode());
    	itemStatusModifyDTO.setOperator(1);
        ExecuteResult<String> result = itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
        if(result.isSuccess()){
            map.put("success",true);
            map.put("msg","驳回成功");
        }else {
            map.put("success",false);
            map.put("msg",result.getErrorMessages());
        }
        return map;
    }
    @RequestMapping(value = "gsListShop")
    public String gsListShop(@ModelAttribute("goods") ItemQueryInDTOView goods,@ModelAttribute("pager") Pager<?> pager,Model model){

    	goods.getItemQueryInDTO().setOperator(1);
    	//查询类目优先级 3>2>1
    	if(goods.getCid()!=null&&goods.getCid()>0){
    		goods.getItemQueryInDTO().setCid(Long.valueOf(goods.getCid()));
    	}else if (goods.getPlatformId2()!=null && goods.getPlatformId2()>0){
    		goods.getItemQueryInDTO().setCid(Long.valueOf(goods.getPlatformId2()));
    	}else if(goods.getPlatformId1()!=null && goods.getPlatformId1()>0){
    		goods.getItemQueryInDTO().setCid(Long.valueOf(goods.getPlatformId1()));
    	}
    	if(goods.getShopName()!=null&&goods.getShopName().length()>0){
    		goods.getItemQueryInDTO().setShopIds(getShopIdsByName(goods.getShopName()));
    	}
    	List<Integer> itemStatus=new ArrayList<Integer>();
    	itemStatus.add(2);
    	itemStatus.add(20);
    	itemStatus.add(3);
    	itemStatus.add(4);
    	itemStatus.add(5);
    	itemStatus.add(6);
    	goods.getItemQueryInDTO().setItemStatusList(itemStatus);
    	DataGrid<ItemQueryOutDTO> data= itemExportService.queryItemList(goods.getItemQueryInDTO(), pager);
    	DataGrid<ItemQueryOutView> viewData = new DataGrid<ItemQueryOutView>();
    	List<ItemQueryOutView> viewList = new ArrayList<ItemQueryOutView>();
    	if(data.getRows()!=null&&data.getRows().size()>0){
    		for(ItemQueryOutDTO itemQueryOutDTO: data.getRows()){
    			ItemQueryOutView itemQueryOutView = new ItemQueryOutView();
    			itemQueryOutView.setItemQueryOutDTO(itemQueryOutDTO);
    			if(itemQueryOutDTO.getShopId()!=null){
    				itemQueryOutView.setShopDTO(shopExportService.findShopInfoById(itemQueryOutDTO.getShopId()).getResult());
                }
    			if(itemQueryOutDTO.getCid()!=null){
    				itemQueryOutView.setCategory(getCategory(Long.valueOf(itemQueryOutDTO.getCid())));
    			}
                SettleItemExpenseDTO dto = new SettleItemExpenseDTO();
                dto.setItemId(itemQueryOutDTO.getItemId());
                ExecuteResult<DataGrid<SettleItemExpenseDTO>> result =  settleItemExpenseExportService.querySettleItemExpense(dto, null);
                if(result.isSuccess()&&result.getResult()!=null&&result.getResult().getRows()!=null
                        &&result.getResult().getRows().size()>0){
                    itemQueryOutView.setSettleItemExpenseDTO(result.getResult().getRows().get(0));
                }

                viewList.add(itemQueryOutView);
    		}
    	}
    	viewData.setRows(viewList);
    	viewData.setTotal(data.getTotal());
    	Page<ItemQueryOutView> p = new Page<ItemQueryOutView>();
		p.setPageNo(pager.getPage());
		p.setPageSize(pager.getRows());
		p.setCount(viewData.getTotal());
		p.setList(viewData.getRows());
		p.setOrderBy(pager.getOrder());
		 DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
         if(dataGrid!=null){
             model.addAttribute("platformList",dataGrid.getRows());
         }else{
             model.addAttribute("platformList",new ArrayList());
         }
		List<ItemCategoryDTO> twoList = new ArrayList<ItemCategoryDTO>();
        if(goods.getPlatformId1()!=null&&goods.getPlatformId1()>0){
        	twoList = itemCategoryService.queryItemCategoryList(Long.valueOf(goods.getPlatformId1())).getRows();
        }
		List<ItemCategoryDTO> thirdList = new ArrayList<ItemCategoryDTO>();
		if(goods.getPlatformId2()!=null&&goods.getPlatformId2()>0){
			thirdList = itemCategoryService.queryItemCategoryList(Long.valueOf(goods.getPlatformId2())).getRows();
        }
		Pager brandPager = new Pager();
		brandPager.setPage(1);
		brandPager.setRows(Integer.MAX_VALUE);
		List<ItemBrandDTO> brandList = itemBrandExportService.queryItemBrandAllList(brandPager).getRows();
        model.addAttribute("thirdList", thirdList);
        model.addAttribute("twoList", twoList);
    	model.addAttribute("goods", goods);
    	model.addAttribute("page", p);
    	model.addAttribute("brandList",brandList);
        return "goodscenter/goodsShopList";
    }
    public Long[] getShopIdsByName(String shopName){
    	int size = 100;
    	Long[] numbers = new Long[size];
    	List<Long> longList = new ArrayList<Long>();
    	ShopDTO shopDTO = new ShopDTO();
    	shopDTO.setShopName(shopName);
    	Pager<ShopDTO> pager = new Pager<ShopDTO>();
    	pager.setPage(1);
    	pager.setRows(100);
    	ExecuteResult<DataGrid<ShopDTO>> result =shopExportService.findShopInfoByCondition(shopDTO, pager);
    	List<ShopDTO> shopList = result.getResult().getRows();
    	if(shopList.size()>0){
    		for(ShopDTO shopDTOModel : shopList){
    			longList.add(shopDTOModel.getShopId());
    		}
    	}
    	numbers = longList.toArray(new Long[100]);
    	return numbers;
    }
    @RequestMapping(value = "gsList")
    public String gsList(@ModelAttribute("goods") ItemQueryInDTOView goods,Pager pager,Model model){
    	 DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
         if(dataGrid!=null){
             model.addAttribute("platformList",dataGrid.getRows());
         }else{
             model.addAttribute("platformList",new ArrayList());
         }
    	goods.getItemQueryInDTO().setOperator(2);
    	//查询类目优先级 3>2>1
    	if(goods.getCid()!=null&&goods.getCid()>0){
    		goods.getItemQueryInDTO().setCid(Long.valueOf(goods.getCid()));
    	}else if (goods.getPlatformId2()!=null && goods.getPlatformId2()>0){
    		goods.getItemQueryInDTO().setCid(Long.valueOf(goods.getPlatformId2()));
    	}else if(goods.getPlatformId1()!=null && goods.getPlatformId1()>0){
    		goods.getItemQueryInDTO().setCid(Long.valueOf(goods.getPlatformId1()));
    	}
    	if(goods.getShopName()!=null&&goods.getShopName().length()>0){
    		goods.getItemQueryInDTO().setShopIds(getShopIdsByName(goods.getShopName()));
    	}
        if(goods.getItemQueryInDTO().getItemStatus()==null){
            List<Integer> itemStatusList = new ArrayList<Integer>();
            itemStatusList.add(1);//商品状态,1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁
            itemStatusList.add(2);
            itemStatusList.add(20);
            itemStatusList.add(3);
            itemStatusList.add(4);
            itemStatusList.add(5);
            itemStatusList.add(6);
            itemStatusList.add(7);
            goods.getItemQueryInDTO().setItemStatusList(itemStatusList);
        }


    	DataGrid<ItemQueryOutDTO> data= itemExportService.queryItemList(goods.getItemQueryInDTO(), pager);
    	DataGrid<ItemQueryOutView> viewData = new DataGrid<ItemQueryOutView>();
    	List<ItemQueryOutView> viewList = new ArrayList<ItemQueryOutView>();
    	if(data.getRows()!=null&&data.getRows().size()>0){
    		for(ItemQueryOutDTO itemQueryOutDTO: data.getRows()){
    			ItemQueryOutView itemQueryOutView = new ItemQueryOutView();
    			itemQueryOutView.setItemQueryOutDTO(itemQueryOutDTO);
    			if(itemQueryOutDTO.getCid()!=null){
    				itemQueryOutView.setCategory(getCategory(Long.valueOf(itemQueryOutDTO.getCid())));
    			}
    			//itemQueryOutView.setCategory(getCategory(itemQueryOutDTO.get));
    			//itemQueryOutView.setShopDTO(shopExportService.findShopInfoById(itemQueryOutDTO.getShopId()).getResult());
    			/*if(isShow(itemQueryOutDTO)){*/
    				viewList.add(itemQueryOutView);
    			/*}*/
    		}
    	}
    	viewData.setRows(viewList);
    	viewData.setTotal(data.getTotal());
    	Page<ItemQueryOutView> p = new Page<ItemQueryOutView>();
		p.setPageNo(pager.getPage());
		p.setPageSize(pager.getRows());
		p.setCount(viewData.getTotal());
		p.setList(viewData.getRows());
		p.setOrderBy(pager.getOrder());
		List<ItemCategoryDTO> twoList = new ArrayList<ItemCategoryDTO>();
        if(goods.getPlatformId1()!=null&&goods.getPlatformId1()>0){
        	twoList = itemCategoryService.queryItemCategoryList(Long.valueOf(goods.getPlatformId1())).getRows();
        }
		List<ItemCategoryDTO> thirdList = new ArrayList<ItemCategoryDTO>();
		if(goods.getPlatformId2()!=null&&goods.getPlatformId2()>0){
			thirdList = itemCategoryService.queryItemCategoryList(Long.valueOf(goods.getPlatformId2())).getRows();
        }
		Pager brandPager = new Pager();
		brandPager.setPage(1);
		brandPager.setRows(Integer.MAX_VALUE);
		List<ItemBrandDTO> brandList = itemBrandExportService.queryItemBrandAllList(brandPager).getRows();
        model.addAttribute("thirdList", thirdList);
        model.addAttribute("twoList", twoList);
    	model.addAttribute("goods", goods);
    	model.addAttribute("page", p);
    	model.addAttribute("brandList",brandList);
        return "goodscenter/goodsList";
    }
    public boolean isShow(ItemQueryOutDTO itemQueryOutDTO){
    	boolean flag = false;
    	if((itemQueryOutDTO.getItemStatus().equals(4)||itemQueryOutDTO.getItemStatus().equals(5))&&
    			(itemQueryOutDTO.getPlatLinkStatus().equals(2)||itemQueryOutDTO.getPlatLinkStatus().equals(3))){
    		return true;
    	}
    	return flag;
    }

    @RequestMapping(value="lockAll")
    @ResponseBody
    public Map lockAll(Long[] checkAll,@ModelAttribute("goods") ItemQueryInDTO goods,@ModelAttribute("pager") Pager<?> pager,Model model){
    	Map map = new HashMap();
        ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
    	List<Long> longList = new ArrayList<Long>();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			longList.add(checkAll[i]);
    		}
    	}
    	itemStatusModifyDTO.setItemIds(longList);
    	itemStatusModifyDTO.setChangeReason("理由无");
    	itemStatusModifyDTO.setItemStatus(ItemStatusEnum.LOCKED.getCode());
    	itemStatusModifyDTO.setOperator(1);
        ExecuteResult<String> result = itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
        if(result.isSuccess()){
            map.put("success",true);
            map.put("msg","锁定成功");
        }else {
            map.put("success",false);
            map.put("msg",result.getErrorMessages());
        }
    	return map;
    }
    @RequestMapping(value = "editGood")
    public String form(Long id,Model model){
    	ExecuteResult<ItemDTO> result =  itemExportService.getItemById(id);
        DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
        model.addAttribute("itemDTO", result.getResult());
        if(result.getResult().getPicUrls().length>0){
        	 model.addAttribute("picUrl", result.getResult().getPicUrls()[0]);
        	 StringBuilder picUrls = new StringBuilder();
        	 for(int i=0;i<result.getResult().getPicUrls().length;i++){
        		 picUrls.append(result.getResult().getPicUrls()[i]);
        		 if((i+1)!=result.getResult().getPicUrls().length){
        			 picUrls.append(",");
        		 }
        	 }
        	 model.addAttribute("picUrls", picUrls.toString());
        }else{
        	 model.addAttribute("picUrl", "");
        	 model.addAttribute("picUrls", "");
        }
        //二级一级id处理
        Long twoLevelId=0l;
        Long oneLevelId=0l;
        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory =  itemCategoryService.queryParentCategoryList(result.getResult().getCid());
        for(ItemCatCascadeDTO itemCatCascadeOne : resultCategory.getResult()){
			for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
				for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
					if(result.getResult().getCid().equals(itemCatCascadeThree.getCid())){
						twoLevelId = itemCatCascadeTwo.getCid();
						oneLevelId = itemCatCascadeOne.getCid();
						/*ItemCatCascadeLevelTwoList = itemCatCascadeOne.getChildCats();
						ItemCatCascadeLevelThreeList = itemCatCascadeTwo.getChildCats();*/
						break ;
					}
				}
			}
		}
        List<ItemCategoryDTO> towList = itemCategoryService.queryItemCategoryList(oneLevelId).getRows();
        List<ItemCategoryDTO> thirdList = itemCategoryService.queryItemCategoryList(twoLevelId).getRows();
        model.addAttribute("twoLevelId", twoLevelId);
        model.addAttribute("oneLevelId", oneLevelId);
        model.addAttribute("towList", towList);
        model.addAttribute("thirdList", thirdList);
        if(result.getResult().getBrand()<0){
        	model.addAttribute("brandId", result.getResult().getBrand());
        }else{
        	model.addAttribute("brandId", 0);
        }
        
        BrandOfShopDTO param = new BrandOfShopDTO();
		param.setSecondCid(twoLevelId);
		param.setThirdCid(result.getResult().getCid());
		List<ItemBrandDTO> brandList = itemBrandExportService.queryItemBrandList(param).getRows();
		model.addAttribute("brandList", brandList);
        return "goodscenter/publishEdit";
    }
    @RequestMapping(value = "viewGoodInit")
    public String viewGoodInit(Long itemId,Model model){
        ExecuteResult<ItemDTO> result =  itemExportService.getItemById(itemId);
        ItemDTO itemDTO = result.getResult();
        model.addAttribute("goods",itemDTO);
        Map<Long,Map> itemMaps = itemService.getAllParent(itemDTO.getCid());
        Map itemMap = itemMaps.get(itemDTO.getCid());
        model.addAttribute("itemMap",itemMap);

        BrandOfShopDTO brandOfShopDTO = new BrandOfShopDTO();
        brandOfShopDTO.setSecondCid((Long)itemMap.get("subcid"));
        brandOfShopDTO.setThirdCid(itemDTO.getCid());
        List<ItemBrandDTO> brandList = itemBrandExportService.queryItemBrandByIds(itemDTO.getBrand()).getResult();
        model.addAttribute("brand", brandList==null?null:brandList.get(0));

        ItemAttrValueItemDTO itemAttrValueItemDTO = new ItemAttrValueItemDTO();
        itemAttrValueItemDTO.setItemId(itemId);
        itemAttrValueItemDTO.setAttrType(1);//销售属性
        ExecuteResult<List<ItemAttr>> result1 = itemAttrValueItemExportService.queryItemAttrValueItem(itemAttrValueItemDTO);
        model.addAttribute("itemSalesAttrList",result1.getResult());

        itemAttrValueItemDTO.setAttrType(2);
        ExecuteResult<List<ItemAttr>> result2 = itemAttrValueItemExportService.queryItemAttrValueItem(itemAttrValueItemDTO);
        model.addAttribute("itemAttrList",result2.getResult());
        return "goodscenter/goodViewInit";
    }
    @RequestMapping(value = "viewGood")
    public String viewGood(Long id,Model model){
        ExecuteResult<ItemDTO> result =  itemExportService.getItemById(id);

        ItemDTO itemDTO = result.getResult();

        Map<Long,Map> itemMaps = itemService.getAllParent(itemDTO.getCid());
        Map itemMap = itemMaps.get(itemDTO.getCid());
        model.addAttribute("itemMap",itemMap);

        //2、店铺分类
        /*long shopCid = itemDTO.getShopCid();
        ShopCategorySellerDTO scLevTwo = getShopCategoryByCid(shopCid);
        String scLevOneName = "";//店铺类目一级分类名称
        String scLevTwoName = "";//店铺类目二级分类名称
        if(null != scLevTwo){
        	scLevTwoName = scLevTwo.getCname();
        	ShopCategorySellerDTO scLevOne = getShopCategoryByCid(scLevTwo.getParentCid());
        	if(null != scLevOne){
        		scLevOneName = scLevOne.getCname();
        	}
        }
        model.addAttribute("scLevOneName", scLevOneName);
        model.addAttribute("scLevTwoName", scLevTwoName);*/

		
        /*****二、商品属性*****/
        ExecuteResult<List<ItemAttr>> productAttrs = this.itemCategoryService.queryCatAttrByKeyVals(itemDTO.getAttributesStr());
		model.addAttribute("productAttrs", productAttrs.getResult());
        
		/*****三、商品信息*****/
        model.addAttribute("itemDTO", itemDTO);
		
		/*****四、销售属性*****/
        ExecuteResult<List<ItemAttr>> saleAttrs = this.itemCategoryService.queryCatAttrByKeyVals(itemDTO.getAttrSaleStr());
		model.addAttribute("saleAttrs", saleAttrs.getResult());
		
		/*****五、基本信息(SKU图片：包含在itemDTO中)*****/
		
		/*****六、商品详情：itemDTO对象中的describeUrl*****/
		
		/*****七、其他信息：(包装清单、售后服务在itemDTO中)*****/
		
		/*****八、运费模版*****/
		ExecuteResult<ShopFreightTemplateDTO> freightTemplateResult = shopFreightTemplateService.queryById(itemDTO.getShopFreightTemplateId());
		model.addAttribute("shopFreightTemplateDTO", freightTemplateResult.getResult());
    	//套餐商品
		if(itemDTO.getAddSource()==ItemAddSourceEnum.COMBINATION.getCode()){
			Map<Long,ItemDTO> auxMap=new HashMap();
	    	Map<Long,List<ItemAttr>> baseMap=new HashMap();
	    	Map<Long,ItemDTO> addMap=new HashMap();
	    	ItemSkuPackageDTO dto=new ItemSkuPackageDTO();
	    	dto.setPackageItemId(itemDTO.getItemId());
	    	List<ItemSkuPackageDTO> auxiliaryItemSkuPackageDTOs=itemSkuPackageService.getPackages(dto);
			if(auxiliaryItemSkuPackageDTOs != null && auxiliaryItemSkuPackageDTOs.size() > 0){
				for(ItemSkuPackageDTO itemSkuPackageDTO : auxiliaryItemSkuPackageDTOs){
					ExecuteResult<ItemDTO> auxiliaryItem = itemExportService.getItemById(itemSkuPackageDTO.getSubItemId());
					if(itemSkuPackageDTO.getAddSource()==ItemAddSourceEnum.NORMAL.getCode()){
						if(!addMap.containsKey(auxiliaryItem.getResult().getItemId())){
							 auxiliaryItem.getResult().setSubNum(itemSkuPackageDTO.getSubNum()+"");
							 addMap.put(auxiliaryItem.getResult().getItemId(), auxiliaryItem.getResult());
						 }
					}else{
						if(!auxMap.containsKey(auxiliaryItem.getResult().getItemId())){
							 auxiliaryItem.getResult().setSubNum(itemSkuPackageDTO.getSubNum()+"");
							 auxMap.put(auxiliaryItem.getResult().getItemId(), auxiliaryItem.getResult());
						 }
						for(SkuInfo info:auxiliaryItem.getResult().getSkuInfos()){
							if(itemSkuPackageDTO.getSubSkuId().equals(info.getSkuId())){
								 String attributes =info.getAttributes();
								 ExecuteResult<List<ItemAttr>> itemAttr = itemCategoryService.queryCatAttrByKeyVals(attributes);
								 if(baseMap.containsKey(info.getSkuId())){
									 baseMap.get(info.getSkuId()).addAll( itemAttr.getResult());
									} else{
										baseMap.put(info.getSkuId(), itemAttr.getResult());
									}
							}
						}
					}
				}
			}
			model.addAttribute("auxMap", auxMap);
			model.addAttribute("baseMap", baseMap);
			model.addAttribute("addMap", addMap);
		}
		model.addAttribute("housetypes", HouseTypeEnum.values());
        return "goodscenter/goodView";
    }
    public List<ItemSkuPackageDTO> getSubItemId1(Long itemId){
    	ItemSkuPackageDTO dto=new ItemSkuPackageDTO();
    	dto.setPackageItemId(itemId);
    	List<ItemSkuPackageDTO> lists=itemSkuPackageService.getPackages(dto);
    		List<Long> listTemp= new ArrayList<Long>();  
    		 Iterator<ItemSkuPackageDTO> it=lists.iterator(); 
    		 while(it.hasNext()){  
    			 ItemSkuPackageDTO a=it.next(); 
    				 if(listTemp.contains(a.getSubItemId())){  
    		    		   it.remove();  
    		    		  }  
    		    		  else{  
    		    		   listTemp.add(a.getSubItemId());  
    		    		  }  
      }  
    	return lists;
    }
    private ShopCategorySellerDTO getShopCategoryByCid(long shopCid) {
    	ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
        dto.setCid(shopCid);
        ExecuteResult<DataGrid<ShopCategorySellerDTO>> scResult = shopCategorySellerExportService.queryShopCategoryList(dto, null);
        if(null == scResult || scResult.getResult()==null || scResult.getResult().getRows()==null||scResult.getResult().getRows().size()==0){
        	return null;
        }
        List<ShopCategorySellerDTO> listSc = scResult.getResult().getRows();
        ShopCategorySellerDTO record = listSc.get(0);
        return record;
	}
    
    public String getCategory(Long cid){
    	String oneName="";
        String twoName = "";
        String threeName = "";
        StringBuilder category = new StringBuilder();
        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory =  itemCategoryService.queryParentCategoryList(cid);
        for(ItemCatCascadeDTO itemCatCascadeOne : resultCategory.getResult()){
			for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
				for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
					if(cid.equals(itemCatCascadeThree.getCid())){
						if(threeName.length()==0){
							threeName = itemCatCascadeThree.getCname();
						}
						if(oneName.length()==0){
							oneName = itemCatCascadeOne.getCname();
						}
						if(twoName.length()==0){
							twoName = itemCatCascadeTwo.getCname();
						}
						itemCatCascadeTwo.getCname();
						/*ItemCatCascadeLevelTwoList = itemCatCascadeOne.getChildCats();
						ItemCatCascadeLevelThreeList = itemCatCascadeTwo.getChildCats();*/
						break ;
					}
				}
			}
		}
        category.append(oneName+"/"+twoName+"/"+threeName);
        return category.toString();
    }
    @RequestMapping(value="soldOut")
    public String soldOut(Long[] checkAll){
    	ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
    	List<Long> longList = new ArrayList<Long>();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			longList.add(checkAll[i]);
    		}
    	}
    	itemStatusModifyDTO.setItemIds(longList);
    	itemStatusModifyDTO.setChangeReason("理由无");
    	itemStatusModifyDTO.setItemStatus(ItemStatusEnum.UNSHELVED.getCode());
    	itemStatusModifyDTO.setOperator(1);
    	itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
    	//itemExportService.modifyItemStatusById(checkAll, "理由无", 5);
    	
    	  return "redirect:"+SysProperties.getAdminPath()+"/goodList/gsShopList"; 
    }
    @ResponseBody
    @RequestMapping(value="soldOutByGoods")
    public Map soldOutByGoods(Long[] checkAll){
    	Map map = new HashMap();
        ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
    	List<Long> longList = new ArrayList<Long>();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			longList.add(checkAll[i]);
    		}
    	}
    	itemStatusModifyDTO.setItemIds(longList);
    	itemStatusModifyDTO.setChangeReason("理由无");
    	itemStatusModifyDTO.setItemStatus(ItemStatusEnum.UNSHELVED.getCode());
    	itemStatusModifyDTO.setOperator(2);
    	ExecuteResult<String> result = itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
    	if (result.isSuccess()){
            map.put("success",true);
            map.put("msg","成功");
        }else{
            map.put("success",false);
            map.put("msg",result.getErrorMessages());
        }
    	return map;
    	 
    }
    @ResponseBody
    @RequestMapping("putInStore")
    public Map putInStore(Long [] checkAll){
        Map map = new HashMap();
    	List<Long> list = new ArrayList<Long>();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			list.add(checkAll[i]);
    		}
    	}
        ExecuteResult<String> result = itemExportService.modifyItemPlatStatus(list, 3);
        if (result.isSuccess()){
            map.put("success",true);
            map.put("msg","成功");
        }else{
            map.put("success",false);
            map.put("msg",result.getErrorMessages());
        }
        return map;
    }
    
    /**
     * <p>更新商品的是否置顶状态</p>
     * Created on 2016年2月22日
     * @param itemId
     * @param placedTop
     * @return
     * @author: 顾雨
     */
    @ResponseBody
    @RequestMapping("updatePlacedTop")
    public Object updatePlacedTop(Long itemId, int placedTop){
    	Map<String, Object> response = new HashMap<String, Object>();
    	
    	ExecuteResult<String> execResult = itemExportService.updatePlacedTop(Arrays.asList(itemId), placedTop);
    	
		if (execResult.isSuccess()){
    		response.put("success",true);
    		response.put("msg","成功");
    	}else{
    		response.put("success",false);
    		response.put("msg",execResult.getErrorMessages());
    	}
    	return response;
    }
    @ResponseBody
    @RequestMapping("putInStore2")
    public Map putInStore2(Long id,Integer status){
        Map map = new HashMap();
        List<Long> list = new ArrayList<Long>();
        list.add(id);
        ExecuteResult<ItemDTO> res=itemExportService.getItemById(id);
        if(res.isSuccess()&&null!=res.getResult()){
			if (res.getResult().getAddSource() == ItemAddSourceEnum.PLATFORM.getCode()
					&& res.getResult().getPlstItemId() != null) {
				map.put("success", false);
				map.put("msg", "该商品是从平台库上传的商品，不需要再次入库。");
				return map;
			}
        }
        ExecuteResult<String> result = itemExportService.addItemToPlat(id);
        if (result.isSuccess()){
            map.put("success",true);
            map.put("msg","成功");
        }else{
            map.put("success",false);
            map.put("msg",result.getErrorMessages());
        }
        return map;
    }
    //上架
    @ResponseBody
    @RequestMapping(value="shelves")
    public Map shelves(Long[] checkAll){
        Map map = new HashMap();
    	ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
    	List<Long> longList = new ArrayList<Long>();
    	if(checkAll.length>0){
    		for(int i=0;i<checkAll.length;i++){
    			longList.add(checkAll[i]);
    		}
    	}
    	itemStatusModifyDTO.setItemIds(longList);
    	itemStatusModifyDTO.setChangeReason("理由无");
    	itemStatusModifyDTO.setItemStatus(ItemStatusEnum.SALING.getCode());
    	itemStatusModifyDTO.setOperator(2);
        ExecuteResult<String> result = itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
    	//itemExportService.modifyItemStatusById(checkAll, "理由无", 5);
        if (result.isSuccess()){
            map.put("success",true);
            map.put("msg","成功");
        }else{
            map.put("success",false);
            map.put("msg",result.getErrorMessages());
        }
        return map;
    }
    @RequestMapping(value = "deletegoods")
    @ResponseBody
    public Json deleteGoods(HttpServletRequest request){
        Json json=new Json();
        try{
            String itemid=request.getParameter("itemid");
            if(itemid!=null&&!"".equals(itemid)){
            	ItemQueryInDTO dto=new ItemQueryInDTO();
            	dto.setPlatItemId(Long.parseLong(itemid));
            	dto.setAddSource(ItemAddSourceEnum.PLATFORM.getCode());
            	DataGrid<ItemQueryOutDTO> res=itemExportService.queryItemList(dto, null);
            	if(!res.getRows().isEmpty()){
            		for(ItemQueryOutDTO itemQueryOutDTO:res.getRows()){
            			if(itemQueryOutDTO.getItemStatus()!=30){
            				json.setMsg("该商品正在被店铺使用，不允许删除!");
                            json.setSuccess(false);
                            return json;
            			}
            		}
            	}
                ExecuteResult<String> executeResult=itemExportService.deleteItem(new Long(itemid));
                if(executeResult!=null&&executeResult.isSuccess()){
                    json.setMsg("删除成功！");
                    json.setSuccess(true);
                }else{
                    String msg="删除失败：";
                    if(executeResult!=null&&executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
                        for(String message:executeResult.getErrorMessages()){
                            msg=msg+message+"；";
                        }
                    }
                    json.setMsg(msg);
                    json.setSuccess(false);
                }
            }else{
                json.setMsg("后台无法获取商品id,无法删除商品");
                json.setSuccess(false);
            }
        }catch(Exception e){
            logger.error("删除商品出错："+e.getMessage());
            json.setMsg("删除商品出现错误，请稍后再试");
            json.setSuccess(false);
        }
        return json;
    }
    

}
