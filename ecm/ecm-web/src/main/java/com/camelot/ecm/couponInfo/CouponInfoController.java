package com.camelot.ecm.couponInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.common.Json;
import com.camelot.ecm.goodscenter.view.ItemQueryInDTOView;
import com.camelot.ecm.shop.ShopQuery;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.TradeInventoryDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponUsingRangeDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 
 * <p>Description: [平台优惠券管理]</p>
 * Created on 2015-12-4
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/couponInfo")
public class CouponInfoController extends BaseController{
	
	@Resource
	private CouponsExportService couponsExportService;
	
	@Resource
	private SystemService systemService;
	
	@Resource
    private ShopExportService shopExportService;
	
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private TradeInventoryExportService tradeInventoryExportService;
	
	@Resource
	private ItemCategoryService itemCategoryService;
	
	@Resource
	private ItemExportService itemExportService;
	
	
	/**
	 * 
	 * <p>Discription:[平台优惠券列表]</p>
	 * Created on 2015-12-4
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping(value = "couponInfoList")
	public String list(CouponsDTO  couponsDTO,Page page,Model model){
		//作为页面条件的回显
		int state = 0 ;
		//判断是否需要页面回显
		boolean is = false;
		if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		couponsDTO.setShopId(0l);
		couponsDTO.setDeleted(0);//未删除
		if(couponsDTO.getState() != null && couponsDTO.getState() == 1){
			//已开始状态
			//活动状态为已开始，并且开始时间小于当前时间，结束时间大于当前时间
			couponsDTO.setIsEffective("2");
			//作为页面条件的回显
			state = couponsDTO.getState();
			//将状态置空 sql已经拼接 ，所以不需要在传状态值
			couponsDTO.setState(null);
			is = true;
		}else if(couponsDTO.getState()!= null && couponsDTO.getState() == 0){
			//已开始状态
			//活动状态为已开始，并且开始时间小于当前时间，结束时间大于当前时间
			couponsDTO.setIsEffective("1");
			//作为页面条件的回显
			state = couponsDTO.getState();
			//将状态置空 sql已经拼接 ，所以不需要在传状态值
			couponsDTO.setState(null);
			is = true;
		}else if(couponsDTO.getState() != null && couponsDTO.getState() == 2){
			//已开始状态
			//活动状态为已开始，并且开始时间小于当前时间，结束时间大于当前时间
			couponsDTO.setIsEffective("3");
			//作为页面条件的回显
			state = couponsDTO.getState();
			//将状态置空 sql已经拼接 ，所以不需要在传状态值
			couponsDTO.setState(null);
			is = true;
		}
		ExecuteResult<DataGrid<CouponsDTO>> er = couponsExportService.queryCouponsList(couponsDTO, pager);
		JSONArray array = new JSONArray();
		for(CouponsDTO dto:er.getResult().getRows()){
			//用来判断优惠券是否已经过期
	   	     Date date = new Date();
			//当优惠券状态为未开始或者已开始时
			if(dto.getState() != null){
				if(dto.getState() == 1 || dto.getState() ==0){
	    			//判断优惠券是否已过期，
	   	    		if(dto.getCouponStartTime().getTime() > date.getTime()){
	   	    			dto.setState(0);
	   	    		}else if( dto.getCouponStartTime().getTime() < date.getTime() && dto.getCouponEndTime().getTime() > date.getTime()){
	   	    			dto.setState(1);
		   	  		}else if( dto.getCouponEndTime().getTime() < date.getTime()){
		   	  		    dto.setState(2);
		   	  		}
	    		 }
			}
			JSONObject obj = JSON.parseObject(JSON.toJSONString(dto));
			User user=systemService.getUser(dto.getCreateUser().toString());
			if(user!=null){
				obj.put("userName", user.getName());
			}
			obj.put("updateDate", dto.getUpdateDate());
			obj.put("couponStartTime",dto.getCouponStartTime());
			obj.put("couponEndTime",dto.getCouponEndTime());
			CouponUserDTO couponUserDTO=new CouponUserDTO();
			couponUserDTO.setCouponId(dto.getCouponId());
			ExecuteResult<DataGrid<CouponUserDTO>> dg=couponsExportService.queryCouponsUserList(couponUserDTO, null);
			if(dg.isSuccess()&&dg.getResult()!=null){
				Long count=dg.getResult().getTotal();
				obj.put("count", count);
			}
			array.add(obj);
		}
		if(is){
			couponsDTO.setState(state);
		}
		if(er.isSuccess()){
			DataGrid<CouponsDTO> dg = er.getResult();
			page.setCount(dg.getTotal().intValue());
			page.setList(array);
		}
		model.addAttribute("pager", page);
		model.addAttribute("couponsDTO", couponsDTO);
		return  "couponInfo/couponInfoList";
	}
	
	/**
	 * 跳转到优惠券活动添加页面
	 * @return
	 */
	@RequestMapping(value = "addcouponInfo")
	public String addcouponInfo(@ModelAttribute("goods") ItemQueryInDTOView goods,Model model){
		User user = UserUtils.getUser();
		model.addAttribute("user",user);
		DataGrid dg =  itemCategoryService.queryItemCategoryList(0L);
        if(dg!=null){
            model.addAttribute("platformList",dg.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
		DataGrid<ItemCategoryDTO> res=itemCategoryService.queryItemCategoryAllList(null);
		model.addAttribute("allCid", res.getRows());
		String couponsId=createCouponsId();
		model.addAttribute("couponsId",couponsId);
		return "couponInfo/addCouponInfo";
	}
	
	
	@RequestMapping(value = "getShopList")
	public String list(HttpServletRequest request,@ModelAttribute("shop")ShopQuery shop,@ModelAttribute("pager")Pager<ShopDTO> pager, Model model,Integer shopPlatformId){
       String num=request.getParameter("num");
		if(pager.getPage()<1){
            pager.setPage(1);
        }
        if(pager.getRows()<1){
            pager.setRows(10);
        }
        Page<ShopDTO> page = new Page<ShopDTO>();
        if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
        ShopDTO shopDTO = new ShopDTO();
        BeanUtils.copyProperties(shop,shopDTO);
        shopDTO.setStatus(5);
        shopDTO.setPlatformId(shopPlatformId);
        ExecuteResult<DataGrid<ShopDTO>> result =  shopExportService.findShopInfoByCondition(shopDTO,pager);
        DataGrid<ShopDTO> dataGrid = result.getResult();
        List<ShopDTO> shopDTOList = dataGrid.getRows();
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        page.setList(shopDTOList);
        page.setCount(dataGrid.getTotal());
        model.addAttribute("page",page);
        model.addAttribute("shop",shop);
        model.addAttribute("num", num);
        return "couponInfo/shopList";
    }
	/**
	 * 商品列表
	 * @param goods
	 * @param pager
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "gsListShop")
    public String gsListShop(HttpServletRequest request,@ModelAttribute("goods") ItemQueryInDTOView goods,@ModelAttribute("pager") Pager<SearchItemSkuOutDTO> pager,Integer platformId,Model model){
		TradeInventoryInDTO tradeInventoryInDTO = new TradeInventoryInDTO();
		String num=request.getParameter("num");
		List<ShopDTO> shopList = new ArrayList<ShopDTO>();
		tradeInventoryInDTO.setPlatformId(platformId);
    	//查询类目优先级 3>2>1
    	if(goods.getCid()!=null&&goods.getCid()>0){
    		tradeInventoryInDTO.setCid(goods.getCid().longValue());
    	}else if (goods.getPlatformId2()!=null && goods.getPlatformId2()>0){
    		tradeInventoryInDTO.setCid(goods.getPlatformId2().longValue());
    	}else if(goods.getPlatformId1()!=null && goods.getPlatformId1()>0){
    		tradeInventoryInDTO.setCid(goods.getPlatformId1().longValue());
    	}
    	if(goods.getShopName()!=null&&goods.getShopName().length()>0){
    		shopList = getShopsByName(goods.getShopName());
    		tradeInventoryInDTO.setShopIds(getShopIdsByName(shopList));
    	}
    	if(StringUtils.isNotBlank(goods.getItemQueryInDTO().getItemName())){
    		tradeInventoryInDTO.setItemName(goods.getItemQueryInDTO().getItemName());
    	}
    	if(goods.getItemQueryInDTO().getId() != null){
    		tradeInventoryInDTO.setItemId(goods.getItemQueryInDTO().getId().longValue());
    	}
    	tradeInventoryInDTO.setPlatformId(goods.getItemQueryInDTO().getPlatformId());//平台id
    	// 在售商品
    	tradeInventoryInDTO.setItemStatus(4);
    	tradeInventoryInDTO.setHasPrice(1);
		ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(tradeInventoryInDTO, pager);
    	Page<TradeInventoryOutDTO> p = new Page<TradeInventoryOutDTO>();
		p.setPageNo(pager.getPage());
		p.setPageSize(pager.getRows());
		p.setCount(dg.getResult().getTotal());
		p.setList(dg.getResult().getRows());
		p.setOrderBy(pager.getOrder());
//		model.addAttribute("num", num);
    	model.addAttribute("page", p);
        return "couponInfo/skuList";
    }
	private List<ShopDTO> getShopsByName(String shopName){
    	ShopDTO shopDTO = new ShopDTO();
    	shopDTO.setShopName(shopName);
    	Pager<ShopDTO> pager = new Pager<ShopDTO>();
    	pager.setPage(1);
    	pager.setRows(100);
    	ExecuteResult<DataGrid<ShopDTO>> result =shopExportService.findShopInfoByCondition(shopDTO, pager);
    	List<ShopDTO> shopList = result.getResult().getRows();
    	return shopList;
	}
	/**
	 * 查询店铺
	 * @param shopName
	 * @return
	 */
	private List<Long> getShopIdsByName(List<ShopDTO> shopList){
		List<Long> longList = new ArrayList<Long>();
    	if(shopList.size()>0){
    		for(ShopDTO shopDTOModel : shopList){
    			longList.add(shopDTOModel.getShopId());
    		}
    	}
    	return longList;
    }
	
	/**
	 * 
	 * <p>Discription:[查看平台优惠劵活动]</p>
	 * Created on 2015-12-7
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping(value="viewCouponInfo")
	public String viewCouponInfo(HttpServletRequest request,Model model,Page page){
		String id=request.getParameter("id");
		ExecuteResult<CouponsDTO> res=couponsExportService.queryById(id.toString());
		if(res!=null&&res.getResult()!=null){
			model.addAttribute("couponsDTO", res.getResult());
			CouponUsingRangeDTO couponUsingRangeDTO=new CouponUsingRangeDTO();
			couponUsingRangeDTO.setCouponId(res.getResult().getCouponId());
			Pager pager = new Pager();
			pager.setPage(page.getPageNo());
			pager.setRows(page.getPageSize());
			JSONArray array = new JSONArray();
			ExecuteResult<DataGrid<CouponUsingRangeDTO>> dg=couponsExportService.queryCouponUsingRangeList(couponUsingRangeDTO, pager);
			for(CouponUsingRangeDTO dto:dg.getResult().getRows()){//店铺通用
				if(res.getResult().getCouponUsingRange()==2){
					ExecuteResult<ShopDTO> shop=shopExportService.findShopInfoById(dto.getCouponUsingId());
					dto.setItemName(shop.getResult().getShopName());//店铺名称
					dto.setItemId(shop.getResult().getShopId().toString());//店铺id
					dto.setSkuAttr(shop.getResult().getSellerId().toString());//卖家id
					dto.setPrice(shop.getResult().getCreated().toString());//申请时间
					page.setList(dg.getResult().getRows());
				}else if(res.getResult().getCouponUsingRange()==3){//类目通用
					String oneName="";
			        String twoName = "";
			        String threeName = "";
			        StringBuilder category = new StringBuilder();
			        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory =  itemCategoryService.queryParentCategoryList(dto.getCouponUsingId());
			        for(ItemCatCascadeDTO itemCatCascadeOne : resultCategory.getResult()){
						for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
							for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
								if(dto.getCouponUsingId().equals(itemCatCascadeThree.getCid())){
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
									break ;
								}
							}
						}
					}
			        dto.setItemName(oneName);//一级类目名称
					dto.setItemId(twoName);//二级类目名称
					dto.setSkuAttr(threeName);//三级类目名称
					page.setList(dg.getResult().getRows());
				}else if(res.getResult().getCouponUsingRange()==4){//sku同用
					TradeInventoryInDTO tradeInventoryInDTO = new TradeInventoryInDTO();
					tradeInventoryInDTO.setSkuId(dto.getCouponUsingId());
					ExecuteResult<DataGrid<TradeInventoryOutDTO>> re = tradeInventoryExportService.queryTradeInventoryList(tradeInventoryInDTO, null);
					JSONObject obj = JSON.parseObject(JSON.toJSONString(dto));
					
					obj.put("itemName", re.getResult().getRows().get(0).getItemName());
					obj.put("itemId",re.getResult().getRows().get(0).getSkuId());
					obj.put("skuAttr", re.getResult().getRows().get(0).getItemAttr());
					obj.put("categoryNme", re.getResult().getRows().get(0).getItemCatCascadeDTO());
					obj.put("price", re.getResult().getRows().get(0).getAreaPrices());
					array.add(obj);
					page.setList(array);
				}
			}
			page.setCount(dg.getResult().getTotal().longValue());
			page.setPageNo(pager.getPage());
	    	page.setPageSize(pager.getRows());
		    model.addAttribute("pager",page);
		    User user = UserUtils.getUser();
			model.addAttribute("user",user);
			model.addAttribute("id",id);
		}
		return "couponInfo/viewCouponInfo";
	}
	/**
	 * 
	 * <p>Discription:[编辑平台优惠券活动]</p>
	 * Created on 2015-12-7
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping(value="editCouponInfo")
	public String editCouponInfo(@ModelAttribute("goods") ItemQueryInDTOView goods,HttpServletRequest request,Model model,Page page){
		int num=0;
		String str="";
		String id=request.getParameter("id");
		ExecuteResult<CouponsDTO> res=couponsExportService.queryById(id.toString());
		if(res!=null&&res.getResult()!=null){
			model.addAttribute("couponInfo", res.getResult());
			model.addAttribute("couponsId", res.getResult().getCouponId());
			CouponUsingRangeDTO couponUsingRangeDTO=new CouponUsingRangeDTO();
			couponUsingRangeDTO.setCouponId(res.getResult().getCouponId());
			ExecuteResult<DataGrid<CouponUsingRangeDTO>> dg=couponsExportService.queryCouponUsingRangeList(couponUsingRangeDTO, null);
			for(CouponUsingRangeDTO dto:dg.getResult().getRows()){//店铺通用
				if(res.getResult().getCouponUsingRange()==2){
					ExecuteResult<ShopDTO> shop=shopExportService.findShopInfoById(dto.getCouponUsingId());
					dto.setItemName(shop.getResult().getShopName());
					dto.setItemId(shop.getResult().getShopUrl());
				}
				/*else if(res.getResult().getCouponUsingRange()==3){//类目通用
					str+=dto.getCouponUsingId()+",";
				    model.addAttribute("str", str);
				}else if(res.getResult().getCouponUsingRange()==4){//sku同用
					ExecuteResult<String> rs=itemExportService.queryAttrBySkuId(dto.getCouponUsingId());
//					dto.setItemName(rs.getResult().get);
//					dto.setItemId(shop.getResult().getShopUrl());
				}*/
				num=num+1;
				model.addAttribute("num", num);
				str+=dto.getCouponUsingId()+",";
			    model.addAttribute("str", str);
			}
			User user = UserUtils.getUser();
			model.addAttribute("user",user);
			DataGrid<ItemCategoryDTO> rs=itemCategoryService.queryItemCategoryAllList(null);
			model.addAttribute("allCid", rs.getRows());
			page.setCount(dg.getResult().getTotal().longValue());
			page.setList(dg.getResult().getRows());
		    model.addAttribute("pager",page);
		}
		return "couponInfo/addCouponInfo";
	}
	/**
	 * 
	 * <p>Discription:[删除优惠券活动]</p>
	 * Created on 2015-12-7
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping(value="deleteCouponInfo")
	@ResponseBody
	public Json deleteCouponInfo(HttpServletRequest request){
		Json res=new Json();
		String id=request.getParameter("id");
		try{
			couponsExportService.deleteCoupons(id);
			res.setSuccess(true);
			res.setMsg("删除成功");
		}catch(Exception e){
			res.setSuccess(true);
			res.setMsg("删除失败");
		}
		return res;
	}
	/**
	 * 
	 * <p>Discription:[更新优惠券状态]</p>
	 * Created on 2015-12-7
	 * @param request
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping(value="updateCouponInfoStatus")
	@ResponseBody
	public ExecuteResult<String> updateCouponInfoStatus(HttpServletRequest request){
		ExecuteResult<String> res=new ExecuteResult<String>();
		String id=request.getParameter("id");
		String status=request.getParameter("state");
		String auditRemark=request.getParameter("auditRemark");
		CouponsDTO dto=new CouponsDTO();
		if(StringUtils.isNotBlank(id)){
			dto.setCouponId(id);
		}else{
			res.setResultMessage("id不能为空");
			return res;
		}
		if(StringUtils.isNotBlank(status)){
			dto.setState(Integer.parseInt(status));
		}else{
			res.setResultMessage("状态不能为空");
			return res;
		}
		if(StringUtils.isNotBlank(auditRemark)){
			dto.setRejectReason(auditRemark);
		}
		ExecuteResult<CouponsDTO> ext=couponsExportService.queryById(id);
		//送审操作或者审核通过操作
		if("5".equals(status)||"1".equals(status)){
			if(ext!=null&&ext.getResult()!=null){
				if(ext.getResult().getCouponEndTime().getTime()<new Date().getTime()){//优惠券结束时间小于当前时间
					res.addErrorMessage("该优惠劵已经过期，请重新创建优惠劵");
					return res;
				}
			}
		}
		res=couponsExportService.updateCouponsInfo(dto);
		return res;
	}
	/**
	 * 
	 * <p>Discription:[审核活动列表]</p>
	 * Created on 2015-12-7
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping(value="couponsInfoVerifyIndex")
	public String couponsInfoVerifyIndex(CouponsDTO  couponsDTO,Page page,Model model){
		if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		couponsDTO.setShopId(0l);
		couponsDTO.setState(5);
		ExecuteResult<DataGrid<CouponsDTO>> er = couponsExportService.queryCouponsList(couponsDTO, pager);
		JSONArray array = new JSONArray();
		for(CouponsDTO dto:er.getResult().getRows()){
			JSONObject obj = JSON.parseObject(JSON.toJSONString(dto));
			User user=systemService.getUser(dto.getCreateUser().toString());
			if(user!=null){
				obj.put("userName", user.getName());
			}
			obj.put("updateDate", dto.getUpdateDate());
			obj.put("couponStartTime",dto.getCouponStartTime());
			obj.put("couponEndTime",dto.getCouponEndTime());
			CouponUserDTO couponUserDTO=new CouponUserDTO();
			couponUserDTO.setCouponId(dto.getId().toString());
			ExecuteResult<DataGrid<CouponUserDTO>> dg=couponsExportService.queryCouponsUserList(couponUserDTO, null);
			if(dg.isSuccess()&&dg.getResult()!=null){
				Long count=dg.getResult().getTotal();
				obj.put("count", count);
			}
			array.add(obj);
		}
		if(er.isSuccess()){
			DataGrid<CouponsDTO> dg = er.getResult();
			page.setCount(dg.getTotal().intValue());
			page.setList(array);
		}
		model.addAttribute("pager", page);
		return "couponInfo/couponsInfoVerifyIndex";
	}
	@RequestMapping("saveCouponsInfo")
	public String saveCouponsInfo(Model model, HttpServletRequest request,CouponsDTO couponsDTO) {
		if(null != couponsDTO){
			couponsDTO.setShopId(0l);
			//修改时间
			couponsDTO.setUpdateDate(new Date());
			//优惠券状态
			couponsDTO.setState(4);
			//未删除
			couponsDTO.setDeleted(0);
			if(couponsDTO.getId()==null){
				//插入优惠券主表
				couponsExportService.addCoupons(couponsDTO);
			}else{
				couponsExportService.updateCouponsInfo(couponsDTO);
			}
			//是否限制sku
			if(null != couponsDTO.getCouponUsingRange()){
				//适用范围表 实体
				CouponUsingRangeDTO couponUsingRangeDTO = new CouponUsingRangeDTO();
				//优惠编号
				couponUsingRangeDTO.setCouponId(couponsDTO.getCouponId());
				//适用范围为店铺通用类 限制sku 限制类目 此时skuIds 为 skuId或者cid或者shid的集合
				if(couponsDTO.getCouponUsingRange() != 1){
					if(null != couponsDTO.getSkuIds()){
						String[] skuIdArry = couponsDTO.getSkuIds().split(",");
						//编辑时先删除在插入
						if(couponsDTO.getId()!=null&&couponsDTO.getCouponId()!=null){
							couponsExportService.deleteCouponUsingRangeDTO(couponsDTO.getCouponId());
						}
						for(String couponUsingId : skuIdArry){
							if (StringUtils.isNotEmpty(couponUsingId)) {
								//skuid
								couponUsingRangeDTO.setCouponUsingId(Long.parseLong(couponUsingId));
								//插入数据
								couponsExportService.addCouponUsingRange(couponUsingRangeDTO);
							}
						}
					}
				//平台通用通用
				}else if(couponsDTO.getCouponUsingRange() == 1){
					//shopId 店铺Id 0
					couponUsingRangeDTO.setCouponUsingId(couponsDTO.getShopId());
					//插入数据
					couponsExportService.addCouponUsingRange(couponUsingRangeDTO);
				}
			}
		}
		return "redirect:" + SysProperties.getAdminPath() + "/couponInfo/couponInfoList";
	}
	

	@RequestMapping("modifyCouponsNum")
	@ResponseBody
	public ExecuteResult<Boolean> modifyCouponsNum(String couponId, int couponNum , HttpServletRequest request) {
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		//获取优惠券信息
		ExecuteResult<CouponsDTO> couponsResult = couponsExportService.queryById(couponId);
		CouponsDTO couponsDTO = couponsResult.getResult();
		//更新优惠券数量
		couponsDTO.setCouponNum((couponNum + couponsDTO.getCouponNum()));
		ExecuteResult<String> updateResult= couponsExportService.updateCouponsInfo(couponsDTO);
		if(updateResult.getResultMessage().equals("success")){
			result.setResultMessage("增加成功");
		}else{
			result.setResultMessage("增加失败");
		}
		return result;
	}
	public String createCouponsId(){
		Date date = new Date();
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date) +(random.nextInt(99)+100);
		return str;
	}
	/**
	 * <p>Discription:[添加优惠券显示店铺信息]</p>
	 * Created on 2016-01-25
	 * @param 
	 * @return
	 * @author:周志军
	 */
	@RequestMapping("getShopsToShow")
	public String getShopsToShow(Long[] shopIds,Page page,Model model){
		List<ShopDTO> shopList = new ArrayList<ShopDTO>();
		if(null == shopIds){
			return "couponInfo/showShopInfo";
		}
		for(Long id : shopIds){
			if(null != id){
				ExecuteResult<ShopDTO> er = shopExportService.findShopInfoById(id);
				if(er.isSuccess() && er.getResult() != null){
					shopList.add(er.getResult());
				}
			}
		}
		page.setList(shopList);
		model.addAttribute("page",page);
		return "couponInfo/showShopInfo";
	}
	/**
	 * <p>Discription:[添加优惠券显示SKU信息]</p>
	 * Created on 2016-01-25
	 * @param 
	 * @return
	 * @author:周志军
	 */
	@RequestMapping("getSkusToShow")
	public String getSkusToShow(Long[] skuIds,Page page,Model model){
		List<TradeInventoryOutDTO> skuList = new ArrayList<TradeInventoryOutDTO>();
		if(null == skuIds){
			return "couponInfo/showShopInfo";
		}
		for(Long id : skuIds){
			TradeInventoryInDTO tradeInventoryInDTO = new TradeInventoryInDTO();
			tradeInventoryInDTO.setSkuId(id);
			ExecuteResult<DataGrid<TradeInventoryOutDTO>> er = tradeInventoryExportService.queryTradeInventoryList(tradeInventoryInDTO, null);
			if(er.isSuccess() && er.getResult() != null){
				DataGrid<TradeInventoryOutDTO> dg = er.getResult();
				if(dg.getTotal() > 1){
					continue;
				}
				skuList.addAll(dg.getRows());
			}
		}
		page.setList(skuList);
		model.addAttribute("page",page);
		return "couponInfo/showSkuInfo";
	}
}
