package com.camelot.ecm.apply;

import com.camelot.ecm.shop.ShopQuery;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserContractService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

import org.springframework.beans.BeanUtils;
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
 * Created by sevelli on 15-3-6.
 * @description 商铺controller
 */
@Controller
@RequestMapping(value = "${adminPath}/shop")
public class ShopController extends BaseController{
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private ShopCategoryExportService shopCategoryExportService;
    @Resource
    private ShopBrandExportService shopBrandExportService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
    private ItemBrandExportService itemBrandExportService;
    @Resource
    private UserExtendsService userExtendsService;
    @Resource
    private UserExportService userExportService;
    @Resource
    private UserContractService userContractService;
    @Resource
    private ItemExportService itemExportService;
    @RequestMapping(value = "list")
    public String list(@ModelAttribute("shop")ShopQuery shop,Pager<ShopQuery> pager, Model model){
        if(pager.getPage()<1){
            pager.setPage(1);
        }
        if(pager.getRows()<1){
            pager.setRows(20);
        }
        Page<ShopQuery> page = new Page<ShopQuery>();

        ShopDTO shopDTO = new ShopDTO();
        BeanUtils.copyProperties(shop,shopDTO);
        // 封装店铺查询条件
        //auditStatus;//审核状态，0待审核，1已驳回，2待缴费，3确认
//        if (null!=shop.getAuditStatus()&&0==shop.getAuditStatus()){
//            shopDTO.setStatus(1);//1是申请，2是通过，3是驳回， 4是平台关闭，5是开通
//        }
//        if (null!=shop.getAuditStatus()&&1==shop.getAuditStatus()){
//            shopDTO.setStatus(3);
//        }
//        if (null!=shop.getAuditStatus()&&2==shop.getAuditStatus()){
//            shopDTO.setStatus(2);
//        }
//        shopDTO.setRunStatus(2);//未开通

        Pager<ShopDTO> pager2 = new Pager<ShopDTO>();
        pager2.setRows(Integer.MAX_VALUE);
        pager2.setPage(1);
        ExecuteResult<DataGrid<ShopDTO>> result =  shopExportService.findShopInfoByCondition(shopDTO,pager2);
        DataGrid<ShopDTO> dataGrid = result.getResult();
        List<ShopDTO> shopDTOList = dataGrid.getRows();

        List<ShopQuery> shopQueryList = new ArrayList<ShopQuery>();
        if(shopDTOList!=null&&shopDTOList.size()>0){
            int len = shopDTOList.size();
            Long[] shopIds = new Long[len];
            UserDTO userDTO = new UserDTO();
            int i=0;
            for(ShopDTO dto:shopDTOList){
                //封装查询结果
                shopIds[i] = dto.getShopId();
                i++;
            }
            userDTO.setShopIds(shopIds);

            DataGrid<UserDTO> d =  userExportService.findUserListByCondition(userDTO, UserEnums.UserType.SELLER, pager);
            page.setCount(d.getTotal());
            Map<Long,UserDTO> shopMap = new HashMap<Long,UserDTO>();
            if (d!=null&&d.getRows()!=null){
                for (UserDTO u:d.getRows()){
                    shopMap.put(u.getShopId(),u);
                }

            }
            UserDTO tempDTO = null;
            for(ShopDTO dto:shopDTOList){
                tempDTO = shopMap.get(dto.getShopId());
               if (tempDTO!=null){
                   ShopQuery s = new ShopQuery();
                   BeanUtils.copyProperties(dto,s);
                   if (null!=dto.getStatus()&&dto.getStatus()==1){//1是申请，2是通过，3是驳回， 4是平台关闭，5是开通
                       s.setAuditStatus(0);//0待审核//1已驳回2待缴费
                   }
                   if (null!=dto.getStatus()&&dto.getStatus()==3){
                       s.setAuditStatus(1);
                   }
                   if (null!=dto.getStatus()&&dto.getStatus()==2){
                       s.setAuditStatus(2);
                   }
                   s.setSellerName(tempDTO.getUname());
                   s.setCompanyName(tempDTO.getCompanyName());
                   shopQueryList.add(s);
               }

            }
        }
        page.setList(shopQueryList);
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        model.addAttribute("page",page);
        model.addAttribute("shop",shop);
        return "apply/shop";
    }
    @RequestMapping(value = "shopInfo")
    @ResponseBody
    public Map shopInfo(Long id){
        Map map = new HashMap();
        ExecuteResult<ShopDTO> result =  shopExportService.findShopInfoById(id);
        ShopDTO shopDTO = result.getResult();
        if(shopDTO!=null){
            map.put("shop",shopDTO);
            ExecuteResult<UserInfoDTO> seller = userExtendsService.findUserInfo(shopDTO.getSellerId());
            if (seller.getResult()!=null&&seller.getResult().getUserBusinessDTO()!=null){
                //公司名称
                map.put("companyName",seller.getResult().getUserBusinessDTO().getCompanyName());
            }
            ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
            shopCategoryDTO.setShopId(shopDTO.getShopId());
//            shopCategoryDTO.setStatus(shopDTO.getStatus());
            //店铺类目
            ExecuteResult<DataGrid<ShopCategoryDTO>> scd = shopCategoryExportService.queryShopCategoryAll(shopCategoryDTO, null);
            if(scd.getResult()!= null){
                int len = scd.getResult().getRows().size();
                Long[] ids = new Long[len];
                for (int i=0;i<len;i++){
                    ids[i] = scd.getResult().getRows().get(i).getCid();
                }
                ExecuteResult<List<ItemCatCascadeDTO>> items = itemCategoryService.queryParentCategoryList(ids);
                if(items!=null&&items.getResult()!=null){
                    List<ItemCatCascadeDTO> iccs = items.getResult();
                    List itemCaList = new ArrayList();
                    for (ItemCatCascadeDTO iccd:iccs){//一级类目
                        if(iccd.getChildCats()!=null){
                            List<ItemCatCascadeDTO> subList = iccd.getChildCats();
                            for (ItemCatCascadeDTO sicc:subList){//二级类目
                                if (sicc.getChildCats()!=null){
                                    for (ItemCatCascadeDTO ticc:sicc.getChildCats()){//三级类目
                                        itemCaList.add(iccd.getCname()+">>"+sicc.getCname()+">>"+ticc.getCname());
                                    }
                                }else{
                                    itemCaList.add(iccd.getCname()+">>"+sicc.getCname());
                                }
                            }
                        }else{
                            itemCaList.add(iccd.getCname());
                        }
                    }
                    map.put("itemCaList",itemCaList);
                }
            }
            ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
            shopBrandDTO.setShopId(shopDTO.getShopId());
//            shopBrandDTO.setStatus(shopDTO.getStatus());
            //店铺品牌
            ExecuteResult<DataGrid<ShopBrandDTO>> sbd =  shopBrandExportService.queryShopBrandAll(shopBrandDTO,null);
            if (sbd.getResult()!=null){
                int len = sbd.getResult().getRows().size();
                Long[] ids = new Long[len];
                for (int i=0;i<len;i++){
                    ids[i] = sbd.getResult().getRows().get(i).getBrandId();
                }
                ExecuteResult<List<ItemBrandDTO>> items =  itemBrandExportService.queryItemBrandByIds(ids);
                if (items!=null&&items.getResult()!=null){
                    List<ItemBrandDTO> ibs = items.getResult();
                    int blen = ibs.size();
                    String[] brandList = new String[blen];
                    for (int i=0;i<blen;i++){
                        brandList[i] = ibs.get(i).getBrandLogoUrl();
                    }
                    map.put("brandList",brandList);
                }
            }
        }else{
            map.put("success",false);
            map.put("msg","没有找到对应的店铺");
        }

        return map;
    }
    @RequestMapping(value = "saveAudit")
    @ResponseBody
    public Map saveApprove(Long shopId,Integer status,String comment,HttpServletRequest request){
        Map map = new HashMap();
        ExecuteResult<ShopDTO> shopDTOExecuteResult = shopExportService.findShopInfoById(shopId);
        Long sellerId = null;
        if(shopDTOExecuteResult!=null&&shopDTOExecuteResult.getResult()!=null){
			ExecuteResult<UserInfoDTO> userInfoDTOExecuteResult = userExtendsService
					.findUserInfo(shopDTOExecuteResult.getResult().getSellerId());
			sellerId = shopDTOExecuteResult.getResult().getSellerId();
//			UserCiticDTO userCiticDTO = userInfoDTOExecuteResult.getResult().getUserCiticDTO();
			// if(userCiticDTO.getAccountState()!=null&&userCiticDTO.getAccountState()==2){//检查中信账户
			ShopDTO shopDTO = new ShopDTO();
			shopDTO.setShopId(shopId);
			shopDTO.setStatus(status);
			shopDTO.setComment(comment);
			ExecuteResult<String> result = shopExportService.modifyShopInfoAndcbstatus(shopDTO);
			if (result.isSuccess()) {

				int platformId = shopDTOExecuteResult.getResult().getPlatformId() == null ? 0
						: shopDTOExecuteResult.getResult().getPlatformId();
				if (2 == platformId) {
					shopExportService.addGreenPrintThirdDomainToRedis(shopDTOExecuteResult.getResult().getShopUrl(),
							shopId);
				} else {
					shopExportService.addSecondDomainToRedis(shopDTOExecuteResult.getResult().getShopUrl(), shopId);
				}
				map.put("msg", "店铺审核通过");
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("msg", result.getErrorMessages());
			}
			/*
			 * }else{ map.put("success",false);
			 * map.put("msg","卖家的中信账户审核中，店铺审核失败，请中信账户审核通过后再审核店铺"); return map; }
			 */

        }else{
            map.put("success",false);
            map.put("msg","查无此店铺");
            return map;
        }
        //存放审核状态、用户id 仅供发送站内消息使用
        request.setAttribute("map",map);
        request.setAttribute("sellerId",sellerId);
        return map;
    }
    @RequestMapping(value = "checkContract")
    @ResponseBody
    public Map checkContract(Long shopId){
        Map map = new HashMap();
        map.put("success",false);
        UserContractDTO userContractDTO = new UserContractDTO();
        userContractDTO.setShopId(shopId);
        DataGrid<UserContractDTO> dataGrid  = userContractService.findListByCondition(userContractDTO, null);
        if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
            UserContractDTO contractDTO = dataGrid.getRows().get(0);
            int status = contractDTO.getContractStatus();
            if (3==status){
                map.put("success",true);
            }
        }
        return map;
    }

    @RequestMapping(value = "shopSwitch")
    @ResponseBody
    public Map shopSwitch(Long shopId,Integer status){
        Map map = new HashMap();
        map.put("success",false);
        if(status==4){//重新开店
            ExecuteResult<String> shopResult = shopExportService.modifyShopInfostatus(shopId,5);
            shopExportService.modifyShopRunStatus(shopId,1);
            if(shopResult.isSuccess()){
                map.put("success",true);
                map.put("msg","开启店铺成功");
            }else {
                map.put("success",false);
                map.put("msg",shopResult.getErrorMessages());
            }
        }
        if(status==5){//关闭店铺
            ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
            itemStatusModifyDTO.setShopId(shopId);
            itemStatusModifyDTO.setItemStatus(5);
            ExecuteResult<String> itemResult = itemExportService.modifyShopItemStatus(itemStatusModifyDTO);

            ExecuteResult<String> shopResult = shopExportService.modifyShopInfostatus(shopId,4);
            shopExportService.modifyShopRunStatus(shopId,2);
            if(itemResult.isSuccess()&&shopResult.isSuccess()){
                map.put("success",true);
                map.put("msg","关闭店铺成功");
            }else{
                map.put("success",false);
                map.put("msg",itemResult.getErrorMessages().addAll(shopResult.getErrorMessages()));
            }
        }


        return map;
    }
}
