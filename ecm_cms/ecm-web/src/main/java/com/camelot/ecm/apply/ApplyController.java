package com.camelot.ecm.apply;

import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.ecm.UserService;
import com.camelot.ecm.usercenter.UserQuery;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sevelli on 15-3-3.
 */
@Controller
@RequestMapping(value = "${adminPath}/apply")
public class ApplyController extends BaseController{
    @Resource
    private UserExportService userExportService;
    @Resource
    private UserExtendsService userExtendsService;
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private ShopCategoryExportService shopCategoryExportService;
    @Resource
    private ShopBrandExportService shopBrandExportService;
    @Resource
    private UserService userService;
    @Resource
    private UserCompanyService userCompanyService;
    @Resource
    private AddressBaseService addressBaseService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
    private ItemBrandExportService itemBrandExportService;
    /**
     * 买家信息
     * @return
     */
    @RequestMapping(value = "buyerinfo")
    public String buyerinfo(@ModelAttribute("buyerinfo")UserDTO user,Pager pager,Model model){

        if(pager.getPage()<1){
            pager.setPage(1);
        }
        if(pager.getRows()<1){
            pager.setRows(20);
        }

        user.setParentId(-1L);
        //初始化userstatues
        user.setUserstatus(null);
        //获取买家认证userstatus传入到list
        List<Integer> list = new ArrayList<Integer>();
        list.add(3);
        list.add(4);
        user.setUserStatusList(list);
//        DataGrid dataGrid = userExportService.findUserListByCondition(user, UserEnums.UserType.BUYER,pager);
        DataGrid dataGrid = userExportService.findUserListByCondition(user, null,pager);
        Page<UserDTO> page = new Page<UserDTO>();
        page.setCount(dataGrid.getTotal());
        page.setList(dataGrid.getRows());
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        model.addAttribute("page",page);
        model.addAttribute("buyerinfo",user);
        model.addAttribute("allstatus",UserEnums.UserAuditStatus.values());
        return "apply/buyerinfo";
    }

    /**
     * 卖家信息
     * @return
     */
    @RequestMapping(value = "sellerinfo")
    public String sellerinfo(@ModelAttribute("sellerinfo")UserDTO user,Pager pager,Model model){
        if(pager.getPage()<1){
            pager.setPage(1);
        }
        if(pager.getRows()<1){
            pager.setRows(20);
        }
        
        user.setParentId(-1L);//查询所有主账号的数据，子账号的数据不应该在卖家认证审核中出现
        //初始化userstatues
        user.setUserstatus(null);
        List<Integer> list = new ArrayList<Integer>();
        list.add(5);
        list.add(6);
        user.setUserStatusList(list);
//        DataGrid dataGrid = userExportService.findUserListByCondition(user, UserEnums.UserType.SELLER,pager);
        DataGrid dataGrid = userExportService.findUserListByCondition(user, null,pager);
        Page<UserDTO> page = new Page<UserDTO>();
        page.setCount(dataGrid.getTotal());
        page.setList(dataGrid.getRows());
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        model.addAttribute("page",page);
        model.addAttribute("sellerinfo",user);
        model.addAttribute("allstatus",UserEnums.UserAuditStatus.values());
        return "apply/sellerinfo";
    }
    @RequestMapping(value = "saveApprove")
    @ResponseBody
    public Map saveAppove(@ModelAttribute("user")UserDTO user,HttpServletRequest request){
//        boolean flag = userExportService.modifyUserInfo(user);
        String userId = UserUtils.getUser().getId();
        logger.debug(userId);
        //userstatus 1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过
        Integer auditststus = user.getAuditStatus();// 1待审核//0驳回//2审核通过//3暂不合作
        Map map = new HashMap();
        try {
        	
        	//判断是否为驳回，驳回不修改type
        	if(0 == auditststus){
        		 UserDTO userDTO = userExportService.queryUserById(user.getUid());
        		 user.setUsertype(userDTO.getUsertype());
        	}
        		
            map = userService.approveUser(user,userId);
            //买家认证通过，发送站内信、短信、邮件提示
//        map.put("user",returnUser);
            map.put("auditStatus",auditststus);
            map.put("userstatus", user.getUserstatus());
            map.put("uid",user.getUid());
            request.setAttribute("map",map);
            return map;
        }catch (Exception e){
            logger.error("审核用户出现异常",e);
            map.put("success",false);
            map.put("msg","审核用户出现异常");
        }
        request.setAttribute("map",map);
        return map;
    }
    /**
     * 跳转到卖家详情页
     * @author - 门光耀
     * @createDate - 2015-3-24
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "detailm")
    public String showDetailm(Long uid,Model model){
        Map<String,String> mapcodes=new HashMap<String, String>();
        //获得用户信息和扩展信息
        ExecuteResult<UserInfoDTO> result =   userExtendsService.findUserInfo(uid);
        UserInfoDTO userInfo = result.getResult();
        model.addAttribute("userInfo",userInfo);
        ShopDTO shopDTO=null;
        if(userInfo!=null){
            //获取开户银行和商家公司地址code的map集合
            /**************/
            if(userInfo.getUserAccountDTO()!=null){
                Map<String,String> codeMap= codeMap(userInfo.getUserAccountDTO().getBankBranchIsLocated());
                mapcodes.putAll(codeMap);
            }
            if(userInfo.getUserBusinessDTO()!=null){
                Map<String,String> codeMap= codeMap(userInfo.getUserBusinessDTO().getCompanyAddress());
                mapcodes.putAll(codeMap);
                Map<String,String> codeMap1= codeMap(userInfo.getUserBusinessDTO().getBusinessLicenceAddress());
                mapcodes.putAll(codeMap1);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                if(userInfo.getUserBusinessDTO().getBusinessLicenceIndate()!=null){

                    model.addAttribute("businessLicenceIndate",simpleDateFormat.format(userInfo.getUserBusinessDTO().getBusinessLicenceIndate()));
                }else{
                    model.addAttribute("businessLicenceIndate","长期有效");
                }
                //营业执照成立日期
                if(userInfo.getUserBusinessDTO().getBusinessLicenceDate()!=null){
                    model.addAttribute("businessLicenceDate",simpleDateFormat.format(userInfo.getUserBusinessDTO().getBusinessLicenceDate()));
                }
            }
            List<UserAuditLogDTO> userAuditLogList=userInfo.getUserAuditLogList();
            if(userAuditLogList!=null&&userAuditLogList.size()>0){
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                Iterator<UserAuditLogDTO> iterable=userAuditLogList.iterator();
                List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
                Map<String,String> stace=new HashMap<String, String>();
                stace.put("0","驳回");
                stace.put("2","通过");
                stace.put("1","待审核");
                int i=1;
                while(iterable.hasNext()){
                    UserAuditLogDTO userAuditLogDTO=iterable.next();
                    if("1".equals(userAuditLogDTO.getAuditLogType())){
                        Map<String,String> maptd=new HashMap<String, String>();
                        //序号
                        maptd.put("no",new Integer(i).toString());
                        //审核状态
                        if(userAuditLogDTO.getAuditStatus()!=null){
                            maptd.put("stace",stace.get(userAuditLogDTO.getAuditStatus().toString()));
                        }
                        //审核时间
                        if(userAuditLogDTO.getAuditDate()!=null){
                            maptd.put("auditdate",simpleDateFormat.format(userAuditLogDTO.getAuditDate()));
                        }
                        maptd.put("remark",userAuditLogDTO.getRemark());
                        mapList.add(maptd);
                        i++;
                    }
                }
                model.addAttribute("maplist",mapList);
            }
            if(userInfo.getUserManageDTO()!=null){
                //经营类型
                if(userInfo.getUserManageDTO().getDealerType()!=null){
                    model.addAttribute("dealertype",userInfo.getUserManageDTO().getDealerType().getLabel());
                }
                if(userInfo.getUserManageDTO().getIsHaveEbusiness()!=null){
                    if("1".equals(userInfo.getUserManageDTO().getIsHaveEbusiness())){
                        model.addAttribute("ishavebussiness","有");
                    }else if("0".equals(userInfo.getUserManageDTO().getIsHaveEbusiness())){
                        model.addAttribute("ishavebussiness","无");
                    }else{
                        model.addAttribute("ishavebussiness","无");
                    }
                }else{
                    model.addAttribute("ishavebussiness","无");
                }
                if(userInfo.getUserManageDTO().getErpType()!=null){
                    model.addAttribute("erptype",userInfo.getUserManageDTO().getErpType().getLabel());
                }
            }
        }
        ExecuteResult<UserCompanyDTO> executeResult=userCompanyService.findUserCompanyByUId(uid);
        if(executeResult!=null){
            model.addAttribute("company",executeResult.getResult());
        }
        UserDTO user = userExportService.queryUserById(uid);
        if(user!=null){
            if(user.getShopId()!=null){
                ExecuteResult<ShopDTO> result1 = shopExportService.findShopInfoById(user.getShopId());
                shopDTO= result1.getResult();
                model.addAttribute("shop",shopDTO);
                if(shopDTO!=null&&shopDTO.getStatus()!=null){
                    model.addAttribute("shopStace","("+shopStaceMap().get(shopDTO.getStatus())+")");
                }
            }
        }
        if(userInfo!=null&&userInfo.getUserDTO()!=null){
            if (userInfo.getUserDTO().getUsertype()!=null){
                if(shopDTO!=null){
                    if(shopDTO.getProvinceCode()!=null&&!"".equals(shopDTO.getProvinceCode())){
                        mapcodes.put(shopDTO.getProvinceCode(),"1");
                    }
                    if(shopDTO.getCityCode()!=null&&!"".equals(shopDTO.getCityCode())){
                        mapcodes.put(shopDTO.getCityCode(),"1");
                    }
                    if(shopDTO.getDistrictCode()!=null&&!"".equals(shopDTO.getDistrictCode())){
                        mapcodes.put(shopDTO.getDistrictCode(),"1");
                    }
                }
                Map<String,String> dy= queryDiYu(mapcodes);
                //银行地址
                if(userInfo.getUserAccountDTO()!=null){
                    model.addAttribute("yhaddress",duName(userInfo.getUserAccountDTO().getBankBranchIsLocated(),dy));
                    //是否中信银行
                    Integer iszx=userInfo.getUserAccountDTO().getIsCiticBank();
                    if(iszx!=null&&iszx.intValue()==1){
                        model.addAttribute("iszx","是");
                    }else if(iszx!=null&&iszx.intValue()==0){
                        model.addAttribute("iszx","否");
                    }
                }
                //公司地址
                if(userInfo.getUserBusinessDTO()!=null){
                    if(userInfo.getUserBusinessDTO().getIsFinancing()!=null&&"1".equals(userInfo.getUserBusinessDTO().getIsFinancing())){
                        model.addAttribute("isfinacing","是");
                    }else{
                        model.addAttribute("isfinacing","否");
                    }
                    //公司地址
                    model.addAttribute("gsaddress",duName(userInfo.getUserBusinessDTO().getCompanyAddress(),dy));
                    //营业执照省市区地址
                    model.addAttribute("yyzzaddress",duName(userInfo.getUserBusinessDTO().getBusinessLicenceAddress(),dy));
                }
                if(shopDTO!=null){
                    model.addAttribute("shopaddress",dy.get(shopDTO.getProvinceCode())+">>"+dy.get(shopDTO.getCityCode())/*+">>"+dy.get(shopDTO.getDistrictCode())*/+">>"+shopDTO.getStreetName());
                }
                //获取商检店铺类目的集合
                if(user!=null&&user.getShopId()!=null){
                    ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
                    shopCategoryDTO.setShopId(user.getShopId());
                    ExecuteResult<DataGrid<ShopCategoryDTO>> result2 = shopCategoryExportService.queryShopCategoryAll(shopCategoryDTO,null);
                    if(result2.getResult()!=null){
                        model.addAttribute("shopCategories",selectLm(result2.getResult().getRows()));
                    }
                    ShopBrandDTO shopBrandDTO =new ShopBrandDTO();
                    shopBrandDTO.setShopId(user.getShopId());
                    ExecuteResult<DataGrid<ShopBrandDTO>> result3 = shopBrandExportService.queryShopBrandAll(shopBrandDTO,null);
                    if(result3.getResult()!=null){
                        model.addAttribute("shopBrands",selectBrand(result3.getResult().getRows()));
                    }
                }

            }
        }
        return "apply/sellerdetail";
    }
    /**
     * 跳转到买家详情页
     * @author - 门光耀
     * @createDate - 2015-3-24
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "detailj")
    public String showDetailj(Long uid,Model model){
        if(uid!=null){
            Map<String,String> mapcodes=new HashMap<String, String>();
            //获得用户信息和扩展信息
            ExecuteResult<UserInfoDTO> result =   userExtendsService.findUserInfo(uid);
            UserDTO userDTO=userExportService.queryUserById(uid);
            model.addAttribute("userdtom",userDTO);
            UserInfoDTO userInfo = result.getResult();
            model.addAttribute("userInfo",userInfo);
            ShopDTO shopDTO=null;
            if(userInfo!=null){
                //获取开户银行和商家公司地址code的map集合
                /**************/
                if(userInfo.getUserAccountDTO()!=null){
                    Map<String,String> codeMap= codeMap(userInfo.getUserAccountDTO().getBankBranchIsLocated());
                    mapcodes.putAll(codeMap);
                }
                if(userInfo.getUserBusinessDTO()!=null){
                    Map<String,String> codeMap= codeMap(userInfo.getUserBusinessDTO().getCompanyAddress());
                    mapcodes.putAll(codeMap);
                    if(userInfo.getUserBusinessDTO().getBusinessLicenceIndate()!=null){
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                        model.addAttribute("businessLicenceIndate",simpleDateFormat.format(userInfo.getUserBusinessDTO().getBusinessLicenceIndate()));
                    }else{
                        model.addAttribute("businessLicenceIndate","长期有效");
                    }
                    //是否融资需求
                    if(userInfo.getUserBusinessDTO().getIsFinancing()!=null){
                        if("1".equals(userInfo.getUserBusinessDTO().getIsFinancing())){
                            model.addAttribute("isfinaceing","是");
                        }else if("0".equals(userInfo.getUserBusinessDTO().getIsFinancing())){
                            model.addAttribute("isfinaceing","否");
                        }else{
                            model.addAttribute("isfinaceing","否");
                        }
                    }else{
                        model.addAttribute("isfinaceing","否");
                    }
                }
                List<UserAuditLogDTO> userAuditLogList=userInfo.getUserAuditLogList();
                if(userAuditLogList!=null&&userAuditLogList.size()>0){
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    Iterator<UserAuditLogDTO> iterable=userAuditLogList.iterator();
                    List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
                    Map<String,String> stace=new HashMap<String, String>();
                    stace.put("0","驳回");
                    stace.put("2","通过");
                    stace.put("1","待审核");
                    int i=1;
                    while(iterable.hasNext()){
                        UserAuditLogDTO userAuditLogDTO=iterable.next();
                        if("1".equals(userAuditLogDTO.getAuditLogType())){
                            Map<String,String> maptd=new HashMap<String, String>();
                            //序号
                            maptd.put("no",new Integer(i).toString());
                            //审核状态
                            if(userAuditLogDTO.getAuditStatus()!=null){
                                maptd.put("stace",stace.get(userAuditLogDTO.getAuditStatus().toString()));
                            }
                            //审核时间
                            if(userAuditLogDTO.getAuditDate()!=null){
                                maptd.put("auditdate",simpleDateFormat.format(userAuditLogDTO.getAuditDate()));
                            }
                            maptd.put("remark",userAuditLogDTO.getRemark());
                            mapList.add(maptd);
                            i++;
                        }
                    }
                    model.addAttribute("maplist",mapList);
                }
            }
            ExecuteResult<UserCompanyDTO> executeResult=userCompanyService.findUserCompanyByUId(uid);
            if(executeResult!=null){
                model.addAttribute("company",executeResult.getResult());
            }
            if(userInfo!=null&&userInfo.getUserDTO()!=null){
                if(userDTO.getDepartment()!=null){
                    model.addAttribute("userdepartment",userDTO.getDepartment().getLabel());
                }
                if (userInfo.getUserDTO().getUsertype()!=null&&userInfo.getUserDTO().getUsertype() == UserEnums.UserType.BUYER.getCode()){
                    Map<String,String> dy= queryDiYu(mapcodes);
                    //银行地址
                    if(userInfo.getUserAccountDTO()!=null){
                        model.addAttribute("yhaddress",duName(userInfo.getUserAccountDTO().getBankBranchIsLocated(),dy));
                        //是否中信银行
                        Integer iszx=userInfo.getUserAccountDTO().getIsCiticBank();
                        if(iszx!=null&&iszx.intValue()==1){
                            model.addAttribute("iszx","是");
                        }else if(iszx!=null&&iszx.intValue()==0){
                            model.addAttribute("iszx","否");
                        }
                    }
                    //公司地址
                    if(userInfo.getUserBusinessDTO()!=null){
                        model.addAttribute("gsaddress",duName(userInfo.getUserBusinessDTO().getCompanyAddress(),dy));
                        //公司性质
                        if(userInfo.getUserBusinessDTO().getCompanyQualt()!=null){
                            model.addAttribute("companyxz",userInfo.getUserBusinessDTO().getCompanyQualt().getLabel());
                        }
                        //公司规模
                        if(userInfo.getUserBusinessDTO().getBusinessScale()!=null){
                            model.addAttribute("companygm",userInfo.getUserBusinessDTO().getBusinessScale().getLabel());
                        }
                        //公司人数
                        if(userInfo.getUserBusinessDTO().getCompanyPeoNum()!=null){
                            model.addAttribute("companyrs",userInfo.getUserBusinessDTO().getCompanyPeoNum().getLabel());
                        }
                    }

                }
            }
        }

        return "apply/buyerdetail";
    }
     private List<Map<String,String>> selectLm(List<ShopCategoryDTO> shopCategoryDTOList){
         List<Map<String,String>> lmMap=new ArrayList<Map<String, String>>();
         if(shopCategoryDTOList!=null&&shopCategoryDTOList.size()>0){
             Iterator<ShopCategoryDTO> iterator=shopCategoryDTOList.iterator();
             Map<Long,Long> map=new HashMap<Long, Long>();
             //归类类目状态
             Map<Long,String> lmstace=new HashMap<Long, String>();
             while(iterator.hasNext()){
                 ShopCategoryDTO shopCategoryDTO=iterator.next();
                 if(shopCategoryDTO.getCid()!=null){
                     if(shopCategoryDTO.getStatus()!=null&&shopCategoryDTO.getStatus()==1){
                         map.put(shopCategoryDTO.getCid(),1L);
                         lmstace.put(shopCategoryDTO.getCid(),"待审核");
                     }else if(shopCategoryDTO.getStatus()!=null&&shopCategoryDTO.getStatus()==2){
                         map.put(shopCategoryDTO.getCid(),1L);
                         lmstace.put(shopCategoryDTO.getCid(),"审核通过");
                     }
                 }
             }
             if(map.size()>0){
                 Long[] listcids=new Long[map.keySet().size()];
                 Iterator<Long> iterator1=map.keySet().iterator();
                 int i=0;
                 while(iterator1.hasNext()){
                     listcids[i]=iterator1.next();
                     i++;
                 }
                 ExecuteResult<List<ItemCatCascadeDTO>> result =itemCategoryService.queryParentCategoryList(listcids);
                 List<ItemCatCascadeDTO> listcasedto=result.getResult();
                 if(listcasedto!=null&&listcasedto.size()>0){
                     //一级类目便利
                     Iterator<ItemCatCascadeDTO> iterator2=listcasedto.iterator();
                     while(iterator2.hasNext()){
                         ItemCatCascadeDTO item1=iterator2.next();
                         if(item1.getChildCats()!=null&&item1.getChildCats().size()>0){
                             //二级类目遍历
                             Iterator<ItemCatCascadeDTO> it3=item1.getChildCats().iterator();
                             while(it3.hasNext()){
                                 ItemCatCascadeDTO item2=it3.next();
                                 if(item2.getChildCats()!=null&&item2.getChildCats().size()>0){
                                     //三级类目遍历
                                     Iterator<ItemCatCascadeDTO> it4=item2.getChildCats().iterator();
                                     while(it4.hasNext()){
                                         ItemCatCascadeDTO item3=it4.next();
                                         Map<String,String> map3=new HashMap<String, String>();
                                         //一级类目的code和name
                                         map3.put("yc",item1.getCid()!=null?item1.getCid().toString():"");
                                         map3.put("yn",item1.getCname());
                                         //二级类目的code和name
                                         map3.put("bc",item2.getCid()!=null?item2.getCid().toString():"");
                                         map3.put("bn",item2.getCname());
                                         //三级类目的code和name
                                         map3.put("sc",item3.getCid()!=null?item3.getCid().toString():"");
                                         map3.put("sn",item3.getCname());
                                         map3.put("stace",lmstace.get(item3.getCid()));
                                         lmMap.add(map3);
                                     }
                                 }
                             }
                         }
                     }
                 }
             }
         }
         return lmMap;
     }
    private List<Map<String,String>> selectBrand(List<ShopBrandDTO> shopBrandDTOList){
        List<Map<String,String>> brandMaps=new ArrayList<Map<String, String>>();
        if(shopBrandDTOList!=null&&shopBrandDTOList.size()>0){
            Iterator<ShopBrandDTO> iterator=shopBrandDTOList.iterator();
            Map<Long,Long> map=new HashMap<Long, Long>();
            Map<Long,String> brandstace=new HashMap<Long, String>();
            while(iterator.hasNext()){
                ShopBrandDTO shopBrandDTO=iterator.next();
                if(shopBrandDTO.getBrandId()!=null){
                    if(shopBrandDTO.getStatus()!=null&&shopBrandDTO.getStatus()==1){
                        map.put(shopBrandDTO.getBrandId(),1L);
                        brandstace.put(shopBrandDTO.getBrandId(),"未审核");
                    }else if(shopBrandDTO.getStatus()!=null&&shopBrandDTO.getStatus()==2){
                        map.put(shopBrandDTO.getBrandId(),1L);
                        brandstace.put(shopBrandDTO.getBrandId(),"审核通过");
                    }
                }
            }
            if(map.size()>0){
                Long[] listbids=new Long[map.keySet().size()];
                Iterator<Long> iterator1=map.keySet().iterator();
                int i=0;
                while(iterator1.hasNext()){
                    listbids[i]=iterator1.next();
                    i++;
                }
                ExecuteResult<List<ItemBrandDTO>> executeResult= itemBrandExportService.queryItemBrandByIds(listbids);
                List<ItemBrandDTO> listbrand=executeResult.getResult();
                if(listbrand!=null&&listbrand.size()>0){
                    Iterator<ItemBrandDTO> iterator2=listbrand.iterator();
                    while(iterator2.hasNext()){
                        ItemBrandDTO itemBrandDTO=iterator2.next();
                        Map<String,String> mapss=new HashMap<String, String>();
                        mapss.put("url",itemBrandDTO.getBrandLogoUrl());
                        mapss.put("stace",brandstace.get(itemBrandDTO.getBrandId()));
                        brandMaps.add(mapss);
                    }
                }
            }
        }
        return brandMaps;
    }
    /**
     * 根据地域code获取code和name的对应
     * @author - 门光耀
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private Map<String,String> queryDiYu(Map<String,String> mapcodes){
        Map<String,String> mapdy=new HashMap<String, String>();
        if(mapcodes!=null&&mapcodes.size()>0){
            String[] dyCodes =new String[mapcodes.keySet().size()];
            Iterator<String> i=mapcodes.keySet().iterator();
            int y=0;
            while(i.hasNext()){
                dyCodes[y]=i.next();
                y++;
            }
            ExecuteResult<List<AddressBaseDTO>> result=addressBaseService.queryNameByCode(dyCodes);
            List<AddressBaseDTO> listadds=result.getResult();
            if(listadds!=null&&listadds.size()>0){
                Iterator<AddressBaseDTO> iterator=listadds.iterator();
                while(iterator.hasNext()){
                    AddressBaseDTO addDto=iterator.next();
                    mapdy.put(addDto.getCode(),addDto.getName());
                }
            }
        }
        return mapdy;
    }
    /**
     * 根据地域code获取地域
     * @author - 门光耀
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private String duName(String codes,Map<String,String> maps){
        String name="";
        if(codes!=null&&!"".equals(codes)){
            if(maps!=null&&maps.size()>0){
                String[] codess=codes.split(",");
                for(String code:codess){
                    if(code!=null&&!"".equals(code)){
                        String name1=maps.get(code);
                        if(name!=null&&!"".equals(name)){
                            if(name1!=null&&!"".equals(name1)){
                                name=name+","+name1;
                            }
                        }else{
                            name=name1;
                        }
                    }
                }
                return name;
            }else{
                return codes;
            }
        }else{
            return "";
        }
    }
    /**
     * 地域code字符串重新封装成map
     * @author - 门光耀
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private Map<String,String> codeMap(String addressCode){
        Map<String,String> codeMap=new HashMap<String, String>();
        if(addressCode!=null&!"".equals(addressCode)){
            String[] codes=addressCode.split(",");
            for(String code:codes){
                if(code!=null&&!"".equals(code)){
                    codeMap.put(code,code);
                }
            }
        }
        return codeMap;
    }
    private Map<Integer,String> shopStaceMap(){
        //1是申请，2是通过，3是驳回， 4是平台关闭，5是开通
        Map<Integer,String> shopStaceMap=new HashMap<Integer, String>();
        shopStaceMap.put(new Integer(1),"审核申请");
        shopStaceMap.put(new Integer(2),"审核通过");
        shopStaceMap.put(new Integer(3),"审核驳回");
        shopStaceMap.put(new Integer(4),"平台关闭");
        shopStaceMap.put(new Integer(5),"店铺开通");
        return shopStaceMap;
    }
    @RequestMapping(value = "exportUser")
    public String exportUser(UserDTO user,Integer flag,Pager pager,HttpServletResponse response,Model model){
        try {
            String fileName = "用户记录"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            DataGrid<UserDTO> dataGrid = null;
            if (0==flag){////导出分页
                dataGrid = userExportService.findUserListByCondition(user, UserEnums.UserType.getEnumBycode(user.getUsertype()),pager);
            }
            if (1==flag){//导出所有数据
                dataGrid = userExportService.findUserListByCondition(user, UserEnums.UserType.getEnumBycode(user.getUsertype()),null);
            }
            List orderList = new ArrayList();
            for (UserDTO dto:dataGrid.getRows()){
                UserQuery query = new UserQuery();
                PropertyUtils.copyProperties(query,dto);
                orderList.add(query);
            }

            new ExportExcel("用户数据", UserQuery.class).setDataList(orderList).write(response, fileName).dispose();
            addMessage(model,"成功导出");
            return null;
        }catch (Exception e){
            addMessage(model,"导出失败");
            logger.error("",e);
        }

        if(UserEnums.UserType.getEnumBycode(user.getUsertype())== UserEnums.UserType.BUYER){
//            return buyerinfo(user,pager,model);
            return "redirect:" + SysProperties.getAdminPath() + "/apply/buyerinfo";
        }
        if(UserEnums.UserType.getEnumBycode(user.getUsertype())== UserEnums.UserType.SELLER){
//            return sellerinfo(user,pager,model);
            return "redirect:" + SysProperties.getAdminPath() + "/apply/sellerinfo";
        }

        return  null;
    }
    @RequestMapping(value = "fastApprove")
    @ResponseBody
    public Map fastApprove(Long uid){
        UserDTO dto = new UserDTO();
        dto.setUid(uid);
        dto.setUsertype(UserEnums.UserType.BUYER.getCode());
        dto.setAuditStatus(2);
        dto.setUserstatus(4);
        dto.setAuditRemark("快捷认证");
        Map map = userService.fastApproveUser(dto, UserUtils.getUser().getId());
        return map;
    }

    /**
     *
     * @param uid 用户ID
     * @param flag 1冻结，0解冻
     * @return
     */
    @RequestMapping(value = "frozeUser")
    @ResponseBody
    public Map frozeUser(Long uid,Integer flag){
        UserDTO dto = new UserDTO();
        dto.setUid(uid);
        dto.setUsertype(UserEnums.UserType.BUYER.getCode());
        dto.setAuditStatus(2);
        dto.setUserstatus(4);
        dto.setAuditRemark("快捷认证");
        Map map = new HashMap();
        ExecuteResult<String> result = userExportService.frozenUser(uid==null?"":uid.toString(), CommonEnums.FrozenStatus.getEnumByCode(flag));
        if (result.isSuccess()){
            map.put("success",true);
            map.put("msg","操作成功");
        }else {
            map.put("success",false);
            map.put("msg",result.getErrorMessages());
        }
        return map;
    }

}
