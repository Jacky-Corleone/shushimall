package com.camelot.ecm.sellercenter.sellerlist;

import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.common.DateUtil;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.service.ShopAudiExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.UserContractService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.thinkgem.jeesite.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商家信息明细
 * @author - 门光耀
 * @message- 商家信息导出
 * @createDate - 2015-3-3
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/sellerdetail")
public class SellerDetail extends BaseController {
    @Resource
    private UserExtendsService userExtendsService;
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private UserContractService userContractService;
    @Resource
    private UserCompanyService userCompanyService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
    private AddressBaseService addressBaseService;
    @Resource
    private SattleCatExpenseExportService sattleCatExpenseExportService;
    @Resource
    private UserExportService userExportService;
    /**
     * 卖家明细信息
     * @author - 门光耀
     * @message- 获取卖家明细信息，并跳转到卖家公司明细信息页面
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/sellerdetail")
    public ModelAndView sellerDetailCompany(Long uid){
        Map<String,Object> company=new HashMap<String,Object>();
        if(uid!=null){
            ExecuteResult<UserInfoDTO> result= userExtendsService.findUserInfo(uid);
            UserInfoDTO utdo=result.getResult();
            if(utdo!=null){
                //System.out.println(sellerCode);
                company.put("name","柯莱特交付部");
                //公司明细信息页面
                List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
                company.put("user",utdo);
                company.put("uid",uid);
                company.put("shopid",utdo.getUserDTO().getShopId());
                //店铺明细信息页面
                if(utdo.getUserDTO().getShopId()!=null){
                    ExecuteResult<ShopDTO> rs=shopExportService.findShopInfoById(utdo.getUserDTO().getShopId());
                    ShopDTO shopDTO=rs.getResult();
                    //1是申请，2是通过，3是驳回，4是平台关闭，5是开通
                    Map<Integer,String> shopstatus=new HashMap<Integer,String>();
                    shopstatus.put(1,"申请");
                    shopstatus.put(2,"通过");
                    shopstatus.put(3,"驳回");
                    shopstatus.put(4,"平台关闭");
                    shopstatus.put(5,"开通");
                    company.put("shopDTO",shopDTO);
                    if(shopDTO!=null){
                        company.put("shopstatus",shopstatus.get(shopDTO.getStatus()));
                    }
                }
                //获取公司地址code的map集合
                /**************/
                if(utdo.getUserBusinessDTO()!=null){
                    //公司地址
                    Map<String,String> codeMap= codeMap(utdo.getUserBusinessDTO().getCompanyAddress());
                    //营业执照所在地
                    Map<String,String> codeMap1=codeMap(utdo.getUserBusinessDTO().getBusinessLicenceAddress());
                    codeMap.putAll(codeMap1);
                    Map<String,String> dy= queryDiYu(codeMap);
                    company.put("address",duName(utdo.getUserBusinessDTO().getCompanyAddress(), dy));
                    company.put("businesslicenceaddress",duName(utdo.getUserBusinessDTO().getBusinessLicenceAddress(),dy));
                    if(utdo.getUserBusinessDTO().getIsFinancing()!=null&&"1".equals(utdo.getUserBusinessDTO().getIsFinancing())){
                        company.put("isfinacing","是");
                    }else{
                        company.put("isfinacing","否");
                    }
                    //营业执照有效期
                    if(utdo.getUserBusinessDTO().getBusinessLicenceIndate()!=null){
                        company.put("businesslicenceindate", DateUtil.formatDate(utdo.getUserBusinessDTO().getBusinessLicenceIndate()));
                    }else{
                        company.put("businesslicenceindate","长期有效");
                    }
                    //营业执照成立日期
                    if(utdo.getUserBusinessDTO().getBusinessLicenceDate()!=null){
                        company.put("businesslicencedate",DateUtil.formatDate(utdo.getUserBusinessDTO().getBusinessLicenceDate()));
                    }
                }
                /*****************/
                //经营信息
                if(utdo.getUserManageDTO()!=null){
                    if(utdo.getUserManageDTO().getDealerType()!=null){
                        //经营类型
                        company.put("dealertype",utdo.getUserManageDTO().getDealerType().getLabel());
                    }
                    //是否有同类电商经验
                    if(utdo.getUserManageDTO().getIsHaveEbusiness()!=null&&"1".equals(utdo.getUserManageDTO().getIsHaveEbusiness())){
                        company.put("ishavebusiness","是");
                    }else if(utdo.getUserManageDTO().getIsHaveEbusiness()!=null&&"0".equals(utdo.getUserManageDTO().getIsHaveEbusiness())){
                        company.put("ishavebusiness","否");
                    }
                    if(utdo.getUserManageDTO().getErpType()!=null){
                        company.put("erptype",utdo.getUserManageDTO().getErpType().getLabel());
                    }
                }
            }
        }
        return new ModelAndView("/sellercenter/sellerlist/sellerdetail","company",company);
    }
    /**
     * 卖家明细信息
     * @author - 门光耀
     * @message- 获取卖家明细信息，并跳转到卖家店铺明细信息页面
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/sellerdetailshop")
    public ModelAndView sellerDetailShop(Long uid){
        Map<String,Object> shop=new HashMap<String,Object>();
        if(uid!=null){
            //店铺状态
            Map<Integer,String> shopstatus=new HashMap<Integer,String>();
            shopstatus.put(1,"申请");
            shopstatus.put(2,"通过");
            shopstatus.put(3,"驳回");
            shopstatus.put(4,"平台关闭");
            shopstatus.put(5,"开通");
            Map<Integer,String> userStatus=new HashMap<Integer,String>();
            UserEnums.UserStatus[] userStatuses=UserEnums.UserStatus.values();
            for(UserEnums.UserStatus uu:userStatuses){
                userStatus.put(uu.getCode(),uu.getLabel());
            }
            ExecuteResult<UserInfoDTO> result= userExtendsService.findUserInfo(uid);
            UserInfoDTO utdo=result.getResult();
            if(utdo!=null){
                //店铺明细信息页面
                if(utdo.getUserDTO().getShopId()!=null){

                    ExecuteResult<ShopDTO> rs=shopExportService.findShopInfoById(utdo.getUserDTO().getShopId());
                    ShopDTO shopDTO=rs.getResult();
                    shop.put("shopDTO",shopDTO);
                    if(shopDTO!=null){
                        Map<String,String> mapcode=new HashMap<String, String>();
                        if(shopDTO.getProvinceCode()!=null&&!"".equals(shopDTO.getProvinceCode())){
                            mapcode.put(shopDTO.getProvinceCode(),"1");
                        }
                        if(shopDTO.getCityCode()!=null&&!"".equals(shopDTO.getCityCode())){
                            mapcode.put(shopDTO.getCityCode(),"1");
                        }
                        if(shopDTO.getDistrictCode()!=null&&!"".equals(shopDTO.getDistrictCode())){
                            mapcode.put(shopDTO.getDistrictCode(),"1");
                        }
                        Map<String,String> dy=queryDiYu(mapcode);
                        
	                   if(dy.isEmpty()){
	                	   shop.put("shopaddress","");
	                   }else{
	                	   shop.put("shopaddress",dy.get(shopDTO.getProvinceCode())+">>"+dy.get(shopDTO.getCityCode())+">>"/*+dy.get(shopDTO.getDistrictCode())+">>"*/+shopDTO.getStreetName()); 
	                   }
                    }
                    if(shopDTO!=null){
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                        //店铺开通 结束时间
                        if(shopDTO.getPassTime()!=null){
                            shop.put("passTime",simpleDateFormat.format(shopDTO.getPassTime()));
                        }
                        if(shopDTO.getEndTime()!=null){
                            shop.put("endTime",simpleDateFormat.format(shopDTO.getEndTime()));
                        }
                        //店铺状态
                        if(shopDTO.getStatus()!=null){
                            shop.put("status",shopstatus.get(shopDTO.getStatus()));
                        }
                        //获取店铺的三级类目
                        Map<Long,Long> cids=new HashMap<Long, Long>();
                        List<Map<String,String>> listleimu= selectLeim(shopDTO,cids);
                        shop.put("leimu",listleimu);
                    }
                }
                //商家状态
                if(utdo.getUserDTO().getUserstatus()!=null){
                    shop.put("userstatus",userStatus.get(utdo.getUserDTO().getUserstatus()));
                }
                shop.put("user",utdo);
            }
            shop.put("uid",uid);
        }
        return new ModelAndView("/sellercenter/sellerlist/sellerdetailshop","shop",shop);
    }
    /**
     * 获取三级类目
     * @author - 门光耀
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,String>> selectLeim(ShopDTO shopDTO,Map<Long,Long> lcmaps){
        List<Map<String,String>> listm=new ArrayList<Map<String, String>>();
        Long shipId=shopDTO.getShopId();
        Long[] shipids=new Long[1];
        shipids[0]=shipId;
        ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
        shopAudiinDTO.setShopIds(shipids);
        ExecuteResult<List<ShopDTO>> executeResult=shopExportService.queryShopInfoByids(shopAudiinDTO);
        List<ShopDTO> listshopDto=executeResult.getResult();
        if(listshopDto!=null&&listshopDto.size()>0){
            listm=leiMu(listshopDto,lcmaps);
        }
        return listm;
    }
    /**
     * 获取并封装店铺的三级类目
     * @author - 门光耀
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,String>> leiMu(List<ShopDTO> listShop,Map<Long,Long> lcmaps){
        Map<String,String> lmstace=new HashMap<String, String>();
        //封装类目的map
        List<Map<String,String>> lmmaps=new ArrayList<Map<String, String>>();
        if(listShop!=null&&listShop.size()>0){
            Iterator<ShopDTO> iterator=listShop.iterator();
            while(iterator.hasNext()){
                ShopDTO shopDTO=iterator.next();
                if(shopDTO.getcStatus()!=null&&shopDTO.getcStatus()==1){
                    if(shopDTO.getCid()!=null){
                        lcmaps.put(shopDTO.getCid(),shopDTO.getCid());
                        lmstace.put(String.valueOf(shopDTO.getCid()),"待审核");
                    }
                }
                if(shopDTO.getcStatus()!=null&&shopDTO.getcStatus()==2){
                    if(shopDTO.getCid()!=null){
                        lcmaps.put(shopDTO.getCid(),shopDTO.getCid());
                        lmstace.put(String.valueOf(shopDTO.getCid()),"审核通过");
                    }
                }
            }
            if(lcmaps.keySet().size()>0){
                Long[] listcids=new Long[lcmaps.keySet().size()];
                Iterator<Long> iterator1=lcmaps.keySet().iterator();
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
                                        map3.put("cstace",lmstace.get(String.valueOf(item3.getCid())));
                                        lmmaps.add(map3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return lmmaps;
    }
    /**
     * 卖家明细信息
     * @author - 门光耀
     * @message- 获取卖家明细信息，并跳转到卖家合同及财务明细信息页面
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/sellerdetailcontract")
    public ModelAndView sellerDetailContract(Long uid){
        Map<String,Object> contract=new HashMap<String,Object>();
        if(uid!=null){
            //contract.put("yyzzurl","http://img30.360buyimg.com/ecc-b2b/jfs/t589/113/223048201/84793/638c3c59/54573250Nc1346b1f.png");
            //合同明细信息页面
            contract.put("uid",uid);
            ExecuteResult<UserInfoDTO> result= userExtendsService.findUserInfo(uid);
            UserInfoDTO utdo=result.getResult();
            if(utdo!=null){
                contract.put("user",utdo);
                if(utdo.getUserDTO()!=null){
                    contract.put("shopid",utdo.getUserDTO().getShopId());
                }
                UserContractDTO contractDTO=new UserContractDTO();
                contractDTO.setCreatorId(uid);
                com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
                pager.setPage(1);
                pager.setRows(10);
                DataGrid<UserContractDTO> result11=userContractService.findListByCondition(contractDTO,pager);
                if(result11.getRows()!=null&&result11.getRows().size()>0){
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                    UserContractDTO condto=result11.getRows().get(0);
                    if(condto!=null){
                        contract.put("contractJssAddr",condto.getContractJssAddr());
                        if(condto.getContractStarttime()!=null){
                            contract.put("starttime",format.format(condto.getContractStarttime()));
                        }
                        if(condto.getContractEndtime()!=null){
                            contract.put("endtime",format.format(condto.getContractEndtime()));
                        }
                    }
                }
                //中心账户信息
                if(utdo.getUserCiticDTO()!=null){
                    Map<String,String> map=new HashMap<String, String>();
                    //收款账户
                    map.put("sellerWithdrawsCashAccount",utdo.getUserCiticDTO().getSellerWithdrawsCashAccount());
                    map.put("sellerFrozenAccount",utdo.getUserCiticDTO().getSellerFrozenAccount());
                    if(utdo.getUserCiticDTO().getAccountState()!=null){
                        CommonEnums.ComStatus[] comStatuses= CommonEnums.ComStatus.values();
                        Map<Integer,String> zxstatus=new HashMap<Integer, String>();
                        for(CommonEnums.ComStatus cc:comStatuses){
                            zxstatus.put(new Integer(cc.getCode()),cc.getLabel());
                        }
                        if(map.get("sellerWithdrawsCashAccount")!=null&&!"".equals(map.get("sellerWithdrawsCashAccount"))){
                            map.put("sellerWithdrawsCashAccount",map.get("sellerWithdrawsCashAccount")+"("+zxstatus.get(utdo.getUserCiticDTO().getAccountState())+")");
                        }
                        if(map.get("sellerFrozenAccount")!=null&&!"".equals(map.get("sellerFrozenAccount"))){
                            map.put("sellerFrozenAccount",map.get("sellerFrozenAccount")+"("+zxstatus.get(utdo.getUserCiticDTO().getAccountState())+")");
                        }
                    }
                    contract.put("citimap",map);
                }
                //店铺类目
                if(utdo.getUserDTO().getShopId()!=null&&utdo.getUserDTO().getShopId()!=null){
                    ExecuteResult<ShopDTO> rs=shopExportService.findShopInfoById(utdo.getUserDTO().getShopId());
                    ShopDTO shopDTO=rs.getResult();
                    if(shopDTO!=null){
                        //获取店铺的三级类目
                        Map<Long,Long> cidMaps=new HashMap<Long, Long>();
                        List<Map<String,String>> listleimu= selectLeim(shopDTO,cidMaps);
                        contract.put("leimu",listleimu);
                        if(cidMaps!=null&&cidMaps.size()>0){
                            Long[] cids=new Long[cidMaps.size()];
                            Iterator<Long> itecids=cidMaps.keySet().iterator();
                            int i=0;
                            while(itecids.hasNext()){
                                cids[i]=itecids.next();
                                i++;
                            }
                            ExecuteResult<List<SettleCatExpenseDTO>> listExecuteResult=sattleCatExpenseExportService.queryByIds(cids);
                            List<SettleCatExpenseDTO> listset=listExecuteResult.getResult();
                            if(listset!=null&&listset.size()>0){
                                Map<String,String> mapkd=new HashMap<String, String>();
                                Iterator<SettleCatExpenseDTO> iteratorset=listset.iterator();
                                //平台使用费
                                BigDecimal pt=new BigDecimal(0);
                                //质保金
                                BigDecimal zb=new BigDecimal(0);
                                //将扣点信息和类目信息相关联
                                while(iteratorset.hasNext()){
                                    SettleCatExpenseDTO settleCatExpenseDTO=iteratorset.next();
                                    if(settleCatExpenseDTO.getCategoryId()!=null&&settleCatExpenseDTO.getRebateRate()!=null){
                                        Double kd=settleCatExpenseDTO.getRebateRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                        mapkd.put(settleCatExpenseDTO.getCategoryId().toString(),kd.toString());
                                    }
                                    //质保金
                                    if(settleCatExpenseDTO.getCashDeposit()!=null){
                                        zb=zb.add(settleCatExpenseDTO.getCashDeposit());
                                    }
                                    //平台使用费
                                    if(settleCatExpenseDTO.getServiceFee()!=null){
                                        pt=pt.add(settleCatExpenseDTO.getServiceFee());
                                    }
                                }
                                //平台使用费
                                contract.put("pt",pt.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                                //质保金
                                contract.put("zb",zb.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                                for(Map lmmap:listleimu){
                                    lmmap.put("kd",mapkd.get(lmmap.get("sc")));
                                }
                            }
                        }
                    }
                }
                //获取开户银行地址code的map集合
                /**************/
                if(utdo.getUserAccountDTO()!=null){
                    Map<String,String> codeMap= codeMap(utdo.getUserAccountDTO().getBankBranchIsLocated());
                    Map<String,String> dy= queryDiYu(codeMap);
                    contract.put("address",duName(utdo.getUserAccountDTO().getBankBranchIsLocated(),dy));
                    if(utdo.getUserAccountDTO().getIsCiticBank()!=null&&utdo.getUserAccountDTO().getIsCiticBank().intValue()==1){
                        contract.put("isciticbank","是");
                    }else if(utdo.getUserAccountDTO().getIsCiticBank()!=null&&utdo.getUserAccountDTO().getIsCiticBank().intValue()==0){}{
                        contract.put("isciticbank","否");
                    }
                }
                /*****************/
            }
        }
        return new ModelAndView("/sellercenter/sellerlist/sellerdetailcontract","contract",contract);
    }
    /**
     * 买家明细信息
     * @author - 门光耀
     * @message- 获取买家明细信息
     * @createDate - 2015-3-6
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/bullerdetail")
    public ModelAndView gobuydetail(Long uid){
        Map<String,Object> by=new HashMap<String,Object>();
        ExecuteResult<UserInfoDTO> result= userExtendsService.findUserInfo(uid);
        UserInfoDTO utdo=result.getResult();
        by.put("user",utdo);
        UserDTO userDTO= userExportService.queryUserById(uid);
        by.put("userdto",userDTO);
        ExecuteResult<UserCompanyDTO> executeResult=userCompanyService.findUserCompanyByUId(uid);
        UserCompanyDTO userCompanyDTO=executeResult.getResult();
        by.put("company",userCompanyDTO);
        by.put("yyzzurl","http://img30.360buyimg.com/ecc-b2b/jfs/t589/113/223048201/84793/638c3c59/54573250Nc1346b1f.png");
        if(utdo!=null){
            //获取开户银行和商家公司地址code的map集合
            /**************/
            Map<String,String> mapcodes=new HashMap<String, String>();
            if(utdo.getUserAccountDTO()!=null){
                Map<String,String> codeMap= codeMap(utdo.getUserAccountDTO().getBankBranchIsLocated());
                mapcodes.putAll(codeMap);
            }
            if(utdo.getUserBusinessDTO()!=null){
                if(utdo.getUserBusinessDTO().getCompanyQualt()!=null){
                    //公司性质
                    by.put("companyqualt",utdo.getUserBusinessDTO().getCompanyQualt().getLabel());
                }
                //公司人数
                if(utdo.getUserBusinessDTO().getCompanyPeoNum()!=null){
                    by.put("companypeonum",utdo.getUserBusinessDTO().getCompanyPeoNum().getLabel());
                }
                //公司规模
                if(utdo.getUserBusinessDTO().getBusinessScale()!=null){
                    by.put("companyscale",utdo.getUserBusinessDTO().getBusinessScale().getLabel());
                }
                if(utdo.getUserBusinessDTO().getIsFinancing()!=null&&"1".equals(utdo.getUserBusinessDTO().getIsFinancing())){
                    by.put("isfinacing","是");
                }else{
                    by.put("isfinacing","否");
                }
                Map<String,String> codeMap= codeMap(utdo.getUserBusinessDTO().getCompanyAddress());
                mapcodes.putAll(codeMap);
            }
            Map<String,String> dy= queryDiYu(mapcodes);
            //银行地址
            if(utdo.getUserAccountDTO()!=null){
                by.put("yhaddress",duName(utdo.getUserAccountDTO().getBankBranchIsLocated(),dy));
            }
            //公司地址
            if(utdo.getUserBusinessDTO()!=null){
                by.put("gsaddress",duName(utdo.getUserBusinessDTO().getCompanyAddress(),dy));
            }
            if(userDTO.getDepartment()!=null){
                by.put("department",userDTO.getDepartment().getLabel());
            }
        }
        return new ModelAndView("/sellercenter/sellerlist/bullerdetail","by",by);
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
}
