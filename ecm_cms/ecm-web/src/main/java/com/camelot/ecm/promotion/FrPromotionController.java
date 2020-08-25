package com.camelot.ecm.promotion;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.ecm.goodscenter.view.ItemQueryInDTOView;
import com.camelot.ecm.goodscenter.view.ItemQueryOutView;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.maketcenter.dto.PromotionFrDTO;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionInfoDTO;
import com.camelot.maketcenter.dto.indto.PromotionFullReducitonInDTO;
import com.camelot.maketcenter.service.PromotionFrExportService;
import com.camelot.maketcenter.service.PromotionInfoExportService;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.thinkgem.jeesite.common.persistence.Page;

/**
 * 满减活动的Controller
 * 主要包括满减的创建、修改、查看、操作
 * 
 * @author 王东晓
 * @createDate 2015-11-27
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/frPromotion")
public class FrPromotionController {
	@Resource
    private ItemExportService itemExportService;
	@Resource
    private ShopExportService shopExportService;
	@Resource
	private PromotionService promotionService;
	@Resource
	private PromotionInfoExportService promotionInfoExportService;
	@Resource
	private PromotionFrExportService promotionFrExportService;
	@Resource
	private RedisDB redisDB;
	@Resource
	private ItemCategoryService itemCategoryService;
	/**
	 * 查询满减促销活动列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "frPromotionIndex")
	public String frPromotionIndex(PromotionInfo promotionInfo ,@ModelAttribute("pager") Pager<?> pager, Model model){
		//作为页面条件的回显
		int onlineState = 0 ;
		//判断是否需要页面回显
		boolean is = false;
		//查询平台的活动
		promotionInfo.setShopId(0l);
		//查询满减活动
		promotionInfo.setType(2);
		if(promotionInfo.getOnlineState() != null && promotionInfo.getOnlineState() == 1){
			//已开始状态
			//活动状态为已开始，并且开始时间小于当前时间，结束时间大于当前时间
			promotionInfo.setIsEffective("2");
			//作为页面条件的回显
			onlineState = promotionInfo.getOnlineState();
			//将状态置空 sql已经拼接 ，所以不需要在传状态值
			promotionInfo.setOnlineState(null);
			is = true;
		}else if(promotionInfo.getOnlineState() != null && promotionInfo.getOnlineState() == 0){
			//未开始状态
			//活动状态为未开始，并且开始时间小于当前时间
			promotionInfo.setIsEffective("1");
			//作为页面条件的回显
			onlineState = promotionInfo.getOnlineState();
			//将状态置空 sql已经拼接 ，所以不需要在传状态值
			promotionInfo.setOnlineState(null);
			is = true;
		}else if(promotionInfo.getOnlineState() != null && promotionInfo.getOnlineState() == 2){
			//已结束状态
			//活动状态为结束，并且结束时间小于当前时间
			promotionInfo.setIsEffective("3");
			//作为页面条件的回显
			onlineState = promotionInfo.getOnlineState();
			//将状态置空 sql已经拼接 ，所以不需要在传状态值
			promotionInfo.setOnlineState(null);
			is = true;
		}

		ExecuteResult<DataGrid<PromotionInfo>> result = promotionInfoExportService.queryPromotionInfoList(promotionInfo, pager);//.queryPromotionFrList(promotionFullReduction, page);
		//		ExecuteResult<DataGrid<PromotionOutDTO>> result = promotionService.getPromotion(promotionInDTO, pager);
		if(result.isSuccess()){
			DataGrid<PromotionInfo> data = result.getResult();
			List<PromotionInfo> promotionInfoList = data.getRows();
			for(PromotionInfo promotion : promotionInfoList){
				//如果活动开始时间在当前时间之后，活动未开始状态
				if(promotion.getOnlineState() != null && promotion.getOnlineState()==1 && promotion.getStartTime().after(new Date())){
					promotion.setOnlineState(0);
				}else if(promotion.getOnlineState() != null && promotion.getOnlineState()==1 && promotion.getStartTime().before(new Date()) && promotion.getEndTime().after(new Date())){
					promotion.setOnlineState(1);
				}else if(promotion.getOnlineState() != null && promotion.getOnlineState()==1 && promotion.getEndTime().before(new Date())){
					promotion.setOnlineState(2);
				}
			}
			if(is){
				promotionInfo.setOnlineState(onlineState);
			}
			Page<PromotionInfo> page = new Page<PromotionInfo>();
	    	page.setPageNo(pager.getPage());
	    	page.setPageSize(pager.getRows());
	    	page.setCount(data.getTotal());
	    	page.setList(promotionInfoList);
	    	page.setOrderBy(pager.getOrder());
	    	model.addAttribute("page",page);
		}		
		return "promotion/frPromotionIndex";
	}
	/**
	 * 跳转到满减活动添加页面
	 * @return
	 */
	@RequestMapping(value = "addFrPromotionView")
	public String addFrPromotionView(@ModelAttribute("goods") ItemQueryInDTOView goods,Pager pager,Model model){
		String selectItemKey = UUID.randomUUID().toString();
		DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
		model.addAttribute("selectItemKey", selectItemKey);
		model.addAttribute("viewType", "add");
		return "promotion/addFrPromotion";
	}
	/**
	 * 编辑促销活动
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "editFrPromotion")
	public String editFrPromition(@ModelAttribute("goods") ItemQueryInDTOView goods,Pager pager,Long id ,Model model){
		model.addAttribute("viewType", "edit");
		DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
		PromotionInfo promotionInfo  = new PromotionInfo();
		promotionInfo.setId(id);
		//根据ID查询促销活动
		ExecuteResult<DataGrid<PromotionInfo>> promotionResult = promotionInfoExportService.queryPromotionInfoList(promotionInfo, null);
		if(promotionResult.isSuccess()){
			if(promotionResult.getResult() != null && promotionResult.getResult().getRows() != null){
				PromotionInfo promotion = promotionResult.getResult().getRows().get(0);
				model.addAttribute("promotion", promotion);
				model.addAttribute("viewType", "edit");
				
				Long promotionInfoId = promotion.getId();
				//用于标识页面的key,选择商品时，保存到reids中
				model.addAttribute("selectItemKey", "promotion"+promotionInfoId);
				PromotionFullReduction promotionFullReduction = new PromotionFullReduction();
				promotionFullReduction.setPromotionInfoId(promotionInfoId);
				ExecuteResult<DataGrid<PromotionFullReduction>> frPromotionResult = promotionFrExportService.queryPromotionFrList(promotionFullReduction, null);
				if(frPromotionResult.isSuccess() && frPromotionResult.getResult() != null && frPromotionResult.getResult().getRows() != null){
					List<PromotionFullReduction> frPromotionList = frPromotionResult.getResult().getRows();
					if(frPromotionList.size() != 0){
						PromotionFullReduction frPromotion = frPromotionList.get(0); 
						model.addAttribute("frPromotion", frPromotion);
					}
					//如果该活动适用于部分商品
					if(promotion.getIsAllItem() == 2){
						this.saveItemInfoForShow(promotionInfoId);
						model.addAttribute("selectedItemCount", frPromotionList.size());
						for(PromotionFullReduction frPromotion : frPromotionList){
							redisDB.setHash("promotion"+promotionInfoId, frPromotion.getItemId()+"", frPromotion.getItemId()+"", 1800);
						}
					}
					if(promotion.getIsAllItem() == 1){
						redisDB.del("promotion"+promotionInfoId+"itemInfoShow");
					}
				}
				
			}
			
		}
		return "promotion/addFrPromotion";
	}
	/**
	 * 根据促销活动ID查看促销详情
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewFrPromotion")
	public String viewFrPromotion(Long id , Model model){
		
		PromotionInfo promotionInfo  = new PromotionInfo();
		promotionInfo.setId(id);
		//根据ID查询促销活动
		ExecuteResult<DataGrid<PromotionInfo>> promotionResult = promotionInfoExportService.queryPromotionInfoList(promotionInfo, null);
		if(promotionResult.isSuccess()){
			if(promotionResult.getResult() != null && promotionResult.getResult().getRows() != null){
				PromotionInfo promotion = promotionResult.getResult().getRows().get(0);
				model.addAttribute("promotion", promotion);
				model.addAttribute("viewType", "edit");
				//如果是所有商品
				if(promotion.getIsAllItem() == 1){
					PromotionFullReduction promotionFullReduction = new PromotionFullReduction();
					promotionFullReduction.setPromotionInfoId(id);
					ExecuteResult<DataGrid<PromotionFullReduction>> frPromotionResult = promotionFrExportService.queryPromotionFrList(promotionFullReduction, null);
					if(frPromotionResult.isSuccess() && frPromotionResult.getResult() != null && frPromotionResult.getResult().getRows() != null){
						List<PromotionFullReduction> frPromotionList = frPromotionResult.getResult().getRows();
						if(frPromotionList.size() != 0){
							BigDecimal meetPrice = frPromotionList.get(0).getMeetPrice();
							BigDecimal discountPrice = frPromotionList.get(0).getDiscountPrice();
							model.addAttribute("meetPrice", meetPrice);
							model.addAttribute("discountPrice", discountPrice);
						}
					}
					
				}
			}
		}
		
		return "promotion/viewFrPromotion";
	}
	/**
	 * g根据商品id查看商品详情
	 * @param id
	 * @param pager
	 * @param model
	 * @return
	 */
	@RequestMapping(value="findSelectedFrItem")
	public String findSelectedFrItem(Long id,@ModelAttribute("pager") Pager<?> pager,Model model){
		PromotionFullReduction promotionFullReduction = new PromotionFullReduction();
		promotionFullReduction.setPromotionInfoId(id);
		ExecuteResult<DataGrid<PromotionFullReduction>> frPromotionResult = promotionFrExportService.queryPromotionFrList(promotionFullReduction, pager);
		if(frPromotionResult.isSuccess() && frPromotionResult.getResult() != null && frPromotionResult.getResult().getRows() != null){
			List<PromotionFullReduction> frPromotionList = frPromotionResult.getResult().getRows();
			if(frPromotionList.size() != 0){
				BigDecimal meetPrice = frPromotionList.get(0).getMeetPrice();
				BigDecimal discountPrice = frPromotionList.get(0).getDiscountPrice();
				model.addAttribute("meetPrice", meetPrice);
				model.addAttribute("discountPrice", discountPrice);
			}
			List<ItemQueryOutView> itemList = new LinkedList<ItemQueryOutView>();
			for(PromotionFullReduction frPromotion : frPromotionList){
				Long itemId = frPromotion.getItemId();
//				DataGrid<ItemQueryOutDTO> data= itemExportService.queryItemList(itemQueryInDTO, null);
				ExecuteResult<ItemDTO> itemReuslt = itemExportService.getItemById(itemId);
				if(itemReuslt.isSuccess() && itemReuslt.getResult() != null){
					ItemDTO itemDTO = itemReuslt.getResult();
					Long shopId = itemDTO.getShopId();
					ExecuteResult<ShopDTO> shopResult =shopExportService.findShopInfoById(shopId);
					if(shopResult.isSuccess()){
						ItemQueryOutView itemQueryOutView = new ItemQueryOutView();
						ItemQueryOutDTO itemQueryOutDTO = new ItemQueryOutDTO();
						itemQueryOutDTO.setItemName(itemDTO.getItemName());
						itemQueryOutDTO.setItemId(itemDTO.getItemId());
						itemQueryOutDTO.setGuidePrice(itemDTO.getGuidePrice());
						itemQueryOutView.setItemQueryOutDTO(itemQueryOutDTO);
						itemQueryOutView.setShopDTO(shopResult.getResult());
						itemList.add(itemQueryOutView);
					}
				}
			}
			model.addAttribute("selectedItemList", itemList);
			Page<PromotionFullReduction> page = new Page<PromotionFullReduction>();
	    	page.setPageNo(pager.getPage());
	    	page.setPageSize(pager.getRows());
	    	page.setCount(frPromotionResult.getResult().getTotal());
	    	page.setOrderBy(pager.getOrder());
	    	model.addAttribute("page",page);
		}
		return "promotion/selectedFrItem";
	}
	/**
	 * 满减活动添加
	 * @param promotionFullReducitonInDTO
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveFrPromotion")
	public String saveFrPromotion(@ModelAttribute("promotion")PromotionFullReducitonInDTO promotionFullReducitonInDTO ,HttpServletRequest request, Model model){
		String meetPrice = request.getParameter("meetPrice");
		if(StringUtils.isEmpty(meetPrice)){
			return "404";
		}
		String discountPrice = request.getParameter("discountPrice");
		if(StringUtils.isEmpty(discountPrice)){
			return "404";
		}
		String startTimeStr = request.getParameter("startTimeStr");
		if(StringUtils.isEmpty(startTimeStr)){
			return "404";
		}
		
		String endTimeStr = request.getParameter("endTimeStr");
		if(StringUtils.isEmpty(endTimeStr)){
			return "404";
		}
		PromotionInfoDTO promotionInfoDTO = promotionFullReducitonInDTO.getPromotionInfoDTO();
		if(StringUtils.isEmpty(promotionInfoDTO.getActivityName()) 
				|| promotionInfoDTO.getPlatformId() == null 
				|| promotionInfoDTO.getIsAllItem() == null){
			return "404";
		}
		String selectItemKey = request.getParameter("selectItemKey");
		if(StringUtils.isEmpty(selectItemKey)){
			return "404";
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			promotionInfoDTO.setStartTime(sf.parse(startTimeStr));
			promotionInfoDTO.setEndTime(sf.parse(endTimeStr));
		} catch (ParseException e) {
			//时间转换异常
			e.printStackTrace();
		}
		//满减活动
		promotionInfoDTO.setType(2);
		//设置shopId为0，表示平台发起的活动
		promotionInfoDTO.setShopId(0l);
		//新添加的活动状态为待送审状态
		if(promotionInfoDTO.getOnlineState() == null){
			promotionInfoDTO.setOnlineState(4);
		}
		
		//多少商品参加活动，则满减表中添加多少记录
		List<PromotionFrDTO> promotionFrDTOList = new LinkedList<PromotionFrDTO>();
		PromotionFrDTO promotionFrDTO = null;
		//选择所有商品，则满减表中的itemId填写为0，表示所有商品都使用此活动规则
		if(promotionInfoDTO.getIsAllItem() == 1){
			promotionFrDTO = new PromotionFrDTO();
		    promotionFrDTO.setItemId(0l);
		    promotionFrDTO.setMeetPrice(new BigDecimal(meetPrice));
		    promotionFrDTO.setDiscountPrice(new BigDecimal(discountPrice));
		    promotionFrDTO.setCreateTime(new Date());
		    promotionFrDTO.setUpdateTime(new Date());
		    promotionFrDTOList.add(promotionFrDTO);
		    promotionFullReducitonInDTO.setPromotionFrDTOList(promotionFrDTOList);
		}else
		//如果选择部分商品
		if(promotionInfoDTO.getIsAllItem() == 2){
			Map<String, String> selectedItemMap = redisDB.getHashByKey(selectItemKey);
			//如果map为空，则说明没有选中商品
			if(selectedItemMap.size() == 0){
				return null;
			}
			//为每一个商品添加活动规则
			for (String value : selectedItemMap.values()) {
				  if(StringUtils.isNotEmpty(value)){
					  promotionFrDTO = new PromotionFrDTO();
					  promotionFrDTO.setItemId(Long.parseLong(value));
					  promotionFrDTO.setMeetPrice(new BigDecimal(meetPrice));
					  promotionFrDTO.setDiscountPrice(new BigDecimal(discountPrice));
					  promotionFrDTO.setCreateTime(new Date());
					  promotionFrDTO.setUpdateTime(new Date());
				  }
				  promotionFrDTOList.add(promotionFrDTO);
			}
			promotionFullReducitonInDTO.setPromotionFrDTOList(promotionFrDTOList);
		}
		//保存促销信息
		ExecuteResult<String> result = new ExecuteResult<String>();
		String viewType = request.getParameter("viewType");
		if("add".equals(viewType)){
			result = promotionService.addPromotionFullReduciton(promotionFullReducitonInDTO);
		}else if("edit".equals(viewType)){
			result = promotionService.editPromotionFullReduciton(promotionFullReducitonInDTO);
		}
        //活动信息保存成功，则删除redis存放的商品信息,即便是选择的所有商品，也需要清一下redis
        if(result.isSuccess()){
        	redisDB.del(selectItemKey);
        	redisDB.del(selectItemKey+"itemInfoShow");
        }
		return "redirect:" + SysProperties.getAdminPath() + "/frPromotion/frPromotionIndex";// "promotion/addFrPromotion";
	}
	/**
	 * 查询在售的商品
	 */
	@RequestMapping(value = "getItemList")
	public String getItemList(@ModelAttribute("goods") ItemQueryInDTOView goods, String selectItemKey,@ModelAttribute("pager") Pager<?> pager,Model model,String changeFlag){
		if (changeFlag!=null&&!"".equals(changeFlag)) {
			redisDB.del(selectItemKey);
			redisDB.del(selectItemKey+"itemInfoShow");
		}
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
    		goods.getItemQueryInDTO().setShopIds(this.getShopIdsByName(goods.getShopName()));
    	}
    	goods.getItemQueryInDTO().setItemStatus(4);
    	DataGrid<ItemQueryOutDTO> data= itemExportService.queryItemList(goods.getItemQueryInDTO(), pager);
    	List<ItemQueryOutView> itemList = new LinkedList<ItemQueryOutView>();
    	Page<ItemQueryOutView> page = new Page<ItemQueryOutView>();
    	page.setPageNo(pager.getPage());
    	page.setPageSize(pager.getRows());
    	page.setCount(data.getTotal());
    	page.setOrderBy(pager.getOrder());
    	model.addAttribute("page",page);
    	if(StringUtils.isEmpty(selectItemKey)){
    		return "404";
    	}
    	Map<String, String> selectedItemMap = redisDB.getHashByKey(selectItemKey);
    	List<ItemQueryOutDTO> itemDTOList = data.getRows();
    	ItemQueryOutView itemView = null;
    	for(ItemQueryOutDTO itemDTO : itemDTOList){
    		itemView = new ItemQueryOutView();
    		itemView.setItemQueryOutDTO(itemDTO);
    		if(itemDTO.getShopId()!=null){
    			itemView.setShopDTO(shopExportService.findShopInfoById(itemDTO.getShopId()).getResult());
            }
    		if(StringUtils.isEmpty(selectedItemMap.get(itemDTO.getItemId().toString()))){
    			itemView.setChecked(false);
    		}else{
    			itemView.setChecked(true);
    		}
    		itemList.add(itemView);
    	}
    	model.addAttribute("itemList", itemList);
    	
    	
		return "promotion/itemList";
	}
	
	@RequestMapping(value = "checkItemForFrPromotion")
	@ResponseBody
	public ExecuteResult<Integer> checkItemForFrPromotion(Long itemId,String selectItemKey,boolean checkedType){
		if(itemId == null){
			return null;
		}
		if(StringUtils.isEmpty(selectItemKey)){
			return null;
		}
		if(checkedType){
			//将选中的商品存到redis中，有效时间为30分钟
			redisDB.setHash(selectItemKey, itemId.toString(), itemId.toString(),1800);
		}else{
			redisDB.delHash(selectItemKey, itemId.toString());
		}
		//查询reids中存放的商品个数
		Map<String, String> itemMap =  redisDB.getHashByKey(selectItemKey);
		Integer itemCount = itemMap.size();
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		result.setResult(itemCount);
		return result;
	}
	/**
	 * 临时保存用于显示使用的商品信息
	 * 
	 * create by 周志军
	 * @param itemId
	 * @param selectItemKey
	 * @param checkedType
	 * @param itemInfo
	 */
	private void saveItemInfoForShow(Long promotionInfoId){
		
		PromotionFullReduction promotionFullReduction = new PromotionFullReduction();
		promotionFullReduction.setPromotionInfoId(promotionInfoId);
		ExecuteResult<DataGrid<PromotionFullReduction>> frPromotionResult = promotionFrExportService.queryPromotionFrList(promotionFullReduction, null);
		if(frPromotionResult.isSuccess() && frPromotionResult.getResult() != null && frPromotionResult.getResult().getRows() != null){
			List<PromotionFullReduction> frPromotionList = frPromotionResult.getResult().getRows();
			for(PromotionFullReduction frPromotion : frPromotionList){
				Long itemId = frPromotion.getItemId();
				ExecuteResult<ItemDTO> itemReuslt = itemExportService.getItemById(itemId);
				if(itemReuslt.isSuccess() && itemReuslt.getResult() != null){
					ItemDTO itemDTO = itemReuslt.getResult();
					Long shopId = itemDTO.getShopId();
					ExecuteResult<ShopDTO> shopResult =shopExportService.findShopInfoById(shopId);
					if(shopResult.isSuccess()){
						String status = ItemStatusEnum.getEnumBycode(itemDTO.getItemStatus()) == null ? null:ItemStatusEnum.getEnumBycode(itemDTO.getItemStatus()).getLabel();
						String itemInfo = itemDTO.getItemName() + "," + itemDTO.getItemId() + "," + itemDTO.getGuidePrice()
							+ "," + shopResult.getResult().getShopName() + "," + status;
						redisDB.setHash("promotion"+promotionInfoId+"itemInfoShow", itemDTO.getItemId().toString(), itemInfo,1800);
					}
				}
			}
		}
	}
	
	/**
	 * 临时保存用于显示使用的商品信息
	 * 
	 * create by 周志军
	 * @param itemId
	 * @param selectItemKey
	 * @param checkedType
	 * @param itemInfo
	 */
	@RequestMapping(value = "saveItemInfoForShow")
	public void saveItemInfoForShow(Long itemId,String selectItemKey,boolean checkedType,String itemInfo){
		if(itemId == null){
			return;
		}
		if(StringUtils.isEmpty(selectItemKey)){
			return;
		}
		if(checkedType){
			//将选中的商品存到redis中，有效时间为30分钟
			redisDB.setHash(selectItemKey+"itemInfoShow", itemId.toString(), itemInfo,1800);
		}else{
			redisDB.delHash(selectItemKey+"itemInfoShow", itemId.toString());
		}
	}
	/**
	 * 获取临时保存用于显示使用的商品信息
	 * 
	 * create by 周志军
	 * @param itemId
	 * @param selectItemKey
	 * @param checkedType
	 * @param itemInfo
	 * 
	 */
	@RequestMapping(value = "getItemInfoForShow")
	public String getItemInfoForShow(Pager<String> pager,String selectItemKey,Model model){
		//查询reids中存放的商品个数
		Map<String, String> itemMap =  redisDB.getHashByKey(selectItemKey+"itemInfoShow");
		int i = 0;
		List<String> list = new ArrayList<String>();
		for(String item : itemMap.values()){
			i++;
			if(i > pager.getPageOffset() && i <= pager.getPageOffset() + pager.getRows()){
				list.add(item);
			}
		}
		Page page = new Page();
		page.setList(list);
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		page.setCount(itemMap.size());
		page.setFuncName("showItemInfo");
		model.addAttribute("page", page);
		return "promotion/showItemInfo";
	}
	
	/**
	 * 根据id删除促销活动
	 * @param id
	 * @return
	 */
	@RequestMapping(value="deleteFrPromotion")
	@ResponseBody
	public ExecuteResult<String> deleteFrPromotion(Long id){
		ExecuteResult<String> deleteResult = promotionService.deletePromotion(id);
		return deleteResult;
	}
	
	/**
	 * 根据id将促销信息的状态修改为onlineState
	 * @param id
	 * @param onlineState
	 * @return
	 */
	@RequestMapping(value="updateFrPromotionStatus")
	@ResponseBody
	public ExecuteResult<String> updateFrPromotionStatus(Long id,Integer onlineState,String auditDismissedMsg){
		ExecuteResult<String> result  = null;
		//审核驳回的状态修改 以及驳回原因信息的录入
		if(onlineState == 6){ //若是审核请求  不光修改状态  还要录入驳回信息
			PromotionInfo promotionInfo2 = new PromotionInfo();
			promotionInfo2.setId(id);
			promotionInfo2.setOnlineState(onlineState);
			promotionInfo2.setAuditDismissedMsg(auditDismissedMsg);
			result = promotionService.updatePromotion(promotionInfo2);
		}else{
			result = promotionService.modifyPromotionOnlineState(onlineState, id);
		}
		return result;
	}
	/**
	 * 查询满减促销活动审核列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "frPromotionVerifyIndex")
	public String frPromotionVerifyIndex(PromotionInfo promotionInfo ,@ModelAttribute("pager") Pager<?> pager, Model model){
		//查询平台的活动
		promotionInfo.setShopId(0l);
		//查询满减活动
		promotionInfo.setType(2);
		//查询待审核活动
		promotionInfo.setOnlineState(5);
		ExecuteResult<DataGrid<PromotionInfo>> result = promotionInfoExportService.queryPromotionInfoList(promotionInfo, pager);//.queryPromotionFrList(promotionFullReduction, page);
//		ExecuteResult<DataGrid<PromotionOutDTO>> result = promotionService.getPromotion(promotionInDTO, pager);
		if(result.isSuccess()){
			DataGrid<PromotionInfo> data = result.getResult();
			List<PromotionInfo> promotionInfoList = data.getRows();
			Page<PromotionInfo> page = new Page<PromotionInfo>();
	    	page.setPageNo(pager.getPage());
	    	page.setPageSize(pager.getRows());
	    	page.setCount(data.getTotal());
	    	page.setList(promotionInfoList);
	    	page.setOrderBy(pager.getOrder());
	    	model.addAttribute("page",page);
		}
		return "promotion/frPromotionVerifyIndex";
	}
	private Long[] getShopIdsByName(String shopName){
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
}
