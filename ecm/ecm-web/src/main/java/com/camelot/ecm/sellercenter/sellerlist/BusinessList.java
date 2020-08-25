package com.camelot.ecm.sellercenter.sellerlist;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.common.Json;
import com.camelot.ecm.usercenter.UserBuyer;
import com.camelot.ecm.usercenter.UserSeller;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商家信息管理
 * @author - 门光耀
 * @message- 商家信息管理
 * @createDate - 2015-3-3
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/businesslist")
public class BusinessList extends BaseController {
    @Resource
    private AddressBaseService addressBaseService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
    private UserExportService userExportService;
    @Resource
    private UserExtendsService userExtendsService;
    @Resource
    private ShopExportService shopExportService;
    /**
     * 商家基本信息卖家
     * @author - 门光耀
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/basicseller")
    public ModelAndView basicSeller(){
        UserDTO userdto=new UserDTO();
        Map<String,Object> bacicseller=new HashMap<String, Object>();
        Page page =new Page();
        bacicseller.put("page",page);
        //获取区域下拉框
        List<AddressBase> listshen=addressBaseService.queryAddressBase("0");
        bacicseller.put("address",listshen);
        //获取商品分类下拉数据
        DataGrid<ItemCategoryDTO> dategrid=itemCategoryService.queryItemCategoryList(0L);
        List<ItemCategoryDTO> itemcates=dategrid.getRows();
        bacicseller.put("cates",itemcates);
        //商家状态下拉
        UserEnums.UserStatus[] userStatuss={UserEnums.UserStatus.getEnumByOrdinal(5),UserEnums.UserStatus.getEnumByOrdinal(6)};
        bacicseller.put("buflag",userStatuss);
        //店铺状态下拉
        Map<String,String> dpmap1=new HashMap<String,String>();
        Map<String,String> dpmap2=new HashMap<String,String>();
        Map<String,String> dpmap3=new HashMap<String,String>();
        Map<String,String> dpmap4=new HashMap<String,String>();
        Map<String,String> dpmap5=new HashMap<String,String>();
        List<Map<String,String>> dpflag=new ArrayList<Map<String, String>>();
        dpmap1.put("code","1");
        dpmap1.put("name","申请");
        dpflag.add(dpmap1);
        dpmap2.put("code","2");
        dpmap2.put("name","通过");
        dpflag.add(dpmap2);
        dpmap3.put("code","3");
        dpmap3.put("name","驳回");
        dpflag.add(dpmap3);
        dpmap4.put("code","4");
        dpmap4.put("name","平台关闭");
        dpflag.add(dpmap4);
        dpmap5.put("code","5");
        dpmap5.put("name","开通");
        dpflag.add(dpmap5);
        bacicseller.put("dpflag",dpflag);
        com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
        pager.setPage(1);
        pager.setRows(10);
        DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto, UserEnums.UserType.SELLER,pager);
        //获取店铺信息
        ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
        List<ShopDTO> listshopDto=null;
        page.setCount(datagrid.getTotal());
        if(datagrid.getRows()!=null&&datagrid.getRows().size()>0){
            Iterator<UserDTO> iterator=datagrid.getRows().iterator();
            Map<Long,Long> mapshopids=new HashMap<Long, Long>();
            while(iterator.hasNext()){
                UserDTO userDTO1=iterator.next();
                if(userDTO1.getShopId()!=null){
                    mapshopids.put(userDTO1.getShopId(),userDTO1.getShopId());
                }
            }
            if(mapshopids.keySet().size()>0){
                Iterator<Long> iterator1=mapshopids.keySet().iterator();
                Long[] shopids=new Long[mapshopids.keySet().size()];
                int i=0;
                while(iterator1.hasNext()){
                    shopids[i]=iterator1.next();
                    i++;
                }
                shopAudiinDTO.setShopIds(shopids);
                ExecuteResult<List<ShopDTO>> executeResult=shopExportService.queryShopInfoByids(shopAudiinDTO);
                listshopDto=executeResult.getResult();
            }
        }
        page.setList(selectSellerShop(datagrid.getRows(),listshopDto));
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        return new ModelAndView("/sellercenter/sellerlist/basicseller","bacicseller",bacicseller);
    }

    /**
     * 商家基本信息买家查询分页
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectbasicseller")
    public Json<Page<Map<String,String>>> selectBasicSeller(HttpServletRequest request){
        Json<Page<Map<String,String>>> json=new Json<Page<Map<String,String>>>();
        try{
            UserDTO userDTO=new UserDTO();
            //公司名称
            String companyName=request.getParameter("companyName");
            userDTO.setCompanyName(companyName);
            //店铺开通时间从
            String bingenDate=request.getParameter("bingenDate");
            //店铺开通时间到
            String endDateid=request.getParameter("endDateid");
            //店铺状态 
            String shopflag=request.getParameter("shopflag");
            //商家code(id)
            String uid=request.getParameter("uid");
            if(uid!=null&&!"".equals(uid)){
                userDTO.setUid(new Long(uid));
            }
            //商家状态：
            String userflag=request.getParameter("userflag");
            if(userflag!=null&&!"".equals(userflag)){
                userDTO.setUserstatus(new Integer(userflag));
            }
            //经营商品分类 类目第一级
            String shopleibie1=request.getParameter("shopleibie1");
            //经营商品分类 类目第二级
            String shopleibie2=request.getParameter("shopleibie2");
            //经营商品分类 类目第三级
            String shopleibie3=request.getParameter("shopleibie3");
            //店铺名称
            String shopName=request.getParameter("shopName");
            //用于判断是否已经调用店铺信息的标示(false标示尚未查询，true标示已经查询)
            boolean ifselectShop=false;
            //获取店铺信息
            ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
            if(bingenDate!=null&&!"".equals(bingenDate)){
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(bingenDate);
                shopAudiinDTO.setPassTimestr(date);
                ifselectShop=true;
            }
            if(endDateid!=null&&!"".equals(endDateid)){
                Date date=new SimpleDateFormat("yyyy-MM-dd").parse(endDateid);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = calendar.getTime();
                shopAudiinDTO.setPassTimeend(date);
                ifselectShop=true;
            }
            if(shopflag!=null&&!"".equals(shopflag)){
                shopAudiinDTO.setStatus(new Integer(shopflag));
                ifselectShop=true;
            }
            if(shopName!=null&&!"".equals(shopName)){
                shopAudiinDTO.setShopName(shopName);
                ifselectShop=true;
            }
            if(shopleibie3!=null&&!"".equals(shopleibie3)){
                shopAudiinDTO.setCid(new Long(shopleibie3));
                ifselectShop=true;
            }else if(shopleibie2!=null&&!"".equals(shopleibie2)){
            	shopAudiinDTO.setCid(new Long(shopleibie2));
                ifselectShop=true;
            }else if(shopleibie1!=null&&!"".equals(shopleibie1)){
            	shopAudiinDTO.setCid(new Long(shopleibie1));
                ifselectShop=true;
            }
            /*
             * 添加判断条件1
             */
            if((shopleibie3==null||"".equals(shopleibie3))&&(shopleibie2==null||"".equals(shopleibie2))&&shopleibie1!=null&&!"".equals(shopleibie1)){
                shopAudiinDTO.setCid(new Long(shopleibie1));
                ifselectShop=true;
            }
            /*
             * 添加判断条件2
             */
            if((shopleibie3==null||"".equals(shopleibie3))&&shopleibie2!=null&&!"".equals(shopleibie2)){
                shopAudiinDTO.setCid(new Long(shopleibie2));
                ifselectShop=true;
            }
            if(shopleibie3!=null&&!"".equals(shopleibie3)){
                shopAudiinDTO.setCid(new Long(shopleibie3));
                ifselectShop=true;
            }
            List<ShopDTO> listshopDto=null;
            boolean ifselectuser=true;
            if(ifselectShop){
                ExecuteResult<List<ShopDTO>> executeResult=shopExportService.queryShopInfoByids(shopAudiinDTO);
                listshopDto=executeResult.getResult();
                //封装shop的id
                if(listshopDto!=null&&listshopDto.size()>0){
                    Iterator<ShopDTO> iterator=listshopDto.iterator();
                    Map<Long,Long> mapshopids=new HashMap<Long, Long>();
                    while(iterator.hasNext()){
                        ShopDTO shopDTO=iterator.next();
                        if(shopDTO.getShopId()!=null){
                            mapshopids.put(shopDTO.getShopId(),shopDTO.getShopId());
                        }
                    }
                    if(mapshopids.size()>0){
                        Long[] shopids=new Long[mapshopids.size()];
                        Iterator<Long> iterator1=mapshopids.keySet().iterator();
                        int i=0;
                        while(iterator1.hasNext()){
                            shopids[i]=iterator1.next();
                            i++;
                        }
                        userDTO.setShopIds(shopids);
                    }else{
                        ifselectuser=false;
                    }
                }else{
                    ifselectuser=false;
                }
            }
            //省code
            String sheng=request.getParameter("sheng");
            //市code
            String shi=request.getParameter("shi");
            //区县code
            String xian=request.getParameter("xian");
            String address="";
            if(shi!=null&&!"".equals(shi)){
                address=sheng+","+shi;
            }else{
                address=sheng;
            }
            if(xian!=null&&!"".equals(xian)){
            	address += "," +xian;
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
            	userDTO.setPlatformId(new Integer(platformId));
            }
            userDTO.setCompanyAddr(address);
            Page page =new Page();
            com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            DataGrid<UserDTO> datagrid=null;
            if(ifselectuser){
                datagrid=userExportService.findUserListByCondition(userDTO, UserEnums.UserType.SELLER,pager);
            }else{
                datagrid=new DataGrid<UserDTO>();
            }
            if(ifselectShop==false){
                if(datagrid.getRows()!=null&&datagrid.getRows().size()>0){
                    Iterator<UserDTO> iterator=datagrid.getRows().iterator();
                    Map<Long,Long> mapshopids=new HashMap<Long, Long>();
                    while(iterator.hasNext()){
                        UserDTO userDTO1=iterator.next();
                        if(userDTO1.getShopId()!=null){
                            mapshopids.put(userDTO1.getShopId(),userDTO1.getShopId());
                        }
                    }
                    if(mapshopids.keySet().size()>0){
                        Iterator<Long> iterator1=mapshopids.keySet().iterator();
                        Long[] shopids=new Long[mapshopids.keySet().size()];
                        int i=0;
                        while(iterator1.hasNext()){
                            shopids[i]=iterator1.next();
                            i++;
                        }
                        shopAudiinDTO.setShopIds(shopids);
                        ExecuteResult<List<ShopDTO>> executeResult=shopExportService.queryShopInfoByids(shopAudiinDTO);
                        listshopDto=executeResult.getResult();
                    }
                }
            }
            page.setCount(datagrid.getTotal());
            //page.setCount(100);
            page.setList(selectSellerShop(datagrid.getRows(),listshopDto));
            page.setPageNo(pager.getPage());
            page.setPageSize(pager.getRows());
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("查询方法出错"+e.getMessage());
        }
        return json;
    }
    /**
     * 商家基本信息买家查询分页
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("querydatacount")
    public Json<Page<Map<String,String>>> queryDataCount(HttpServletRequest request){
        Json<Page<Map<String,String>>> json=new Json<Page<Map<String,String>>>();
        try{
            UserDTO userDTO=new UserDTO();
            //公司名称
            String companyName=request.getParameter("companyName");
            userDTO.setCompanyName(companyName);
            //店铺开通时间从
            String bingenDate=request.getParameter("bingenDate");
            //店铺开通时间到
            String endDateid=request.getParameter("endDateid");
            //店铺状态
            String shopflag=request.getParameter("shopflag");
            //商家code(id)
            String uid=request.getParameter("uid");
            if(uid!=null&&!"".equals(uid)){
                userDTO.setUid(new Long(uid));
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
            	userDTO.setPlatformId(new Integer(platformId));
            }
            String userflag=request.getParameter("userflag");
            if(userflag!=null&&!"".equals(userflag)){
                userDTO.setUserstatus(new Integer(userflag));
            }
            //经营商品分类 类目第三级
            String shopleibie3=request.getParameter("shopleibie3");
            //店铺名称
            String shopName=request.getParameter("shopName");
            //用于判断是否已经调用店铺信息的标示(false标示尚未查询，true标示已经查询)
            boolean ifselectShop=false;
            //获取店铺信息
            ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
            if(bingenDate!=null&&!"".equals(bingenDate)){
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(bingenDate);
                shopAudiinDTO.setPassTimestr(date);
                ifselectShop=true;
            }
            if(endDateid!=null&&!"".equals(endDateid)){
                Date date=new SimpleDateFormat("yyyy-MM-dd").parse(endDateid);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = calendar.getTime();
                shopAudiinDTO.setPassTimeend(date);
                ifselectShop=true;
            }
            if(shopflag!=null&&!"".equals(shopflag)){
                shopAudiinDTO.setStatus(new Integer(shopflag));
                ifselectShop=true;
            }
            if(shopName!=null&&!"".equals(shopName)){
                shopAudiinDTO.setShopName(shopName);
                ifselectShop=true;
            }
            if(shopleibie3!=null&&!"".equals(shopleibie3)){
                shopAudiinDTO.setCid(new Long(shopleibie3));
                ifselectShop=true;
            }
            List<ShopDTO> listshopDto=null;
            boolean ifselectuser=true;
            if(ifselectShop){
                ExecuteResult<List<ShopDTO>> executeResult=shopExportService.queryShopInfoByids(shopAudiinDTO);
                listshopDto=executeResult.getResult();
                //封装shop的id
                if(listshopDto!=null&&listshopDto.size()>0){
                    Iterator<ShopDTO> iterator=listshopDto.iterator();
                    Map<Long,Long> mapshopids=new HashMap<Long, Long>();
                    while(iterator.hasNext()){
                        ShopDTO shopDTO=iterator.next();
                        if(shopDTO.getShopId()!=null){
                            mapshopids.put(shopDTO.getShopId(),shopDTO.getShopId());
                        }
                    }
                    if(mapshopids.size()>0){
                        Long[] shopids=new Long[mapshopids.size()];
                        Iterator<Long> iterator1=mapshopids.keySet().iterator();
                        int i=0;
                        while(iterator1.hasNext()){
                            shopids[i]=iterator1.next();
                            i++;
                        }
                        userDTO.setShopIds(shopids);
                    }else{
                        ifselectuser=false;
                    }
                }else{
                    ifselectuser=false;
                }
            }
            //省code
            String sheng=request.getParameter("sheng");
            //市code
            String shi=request.getParameter("shi");
            //区县code
            String xian=request.getParameter("xian");
            String address="";
            if(shi!=null&&!"".equals(shi)){
                address=sheng+","+shi;
            }else{
                address=sheng;
            }
            if(xian!=null&&!"".equals(xian)){
                address += "," +xian;
            }
            userDTO.setCompanyAddr(address);
            Page page =new Page();
            com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
            pager.setPage(1);
            pager.setRows(1);
            DataGrid<UserDTO> datagrid=null;
            if(ifselectuser){
                datagrid=userExportService.findUserListByCondition(userDTO, UserEnums.UserType.SELLER,pager);
            }else{
                datagrid=new DataGrid<UserDTO>();
            }
            page.setCount(datagrid.getTotal());
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("用户导出错误"+e.getMessage());
        }
        return json;
    }
    @RequestMapping(value = "exportpage")
    public String exportPage(HttpServletRequest request,HttpServletResponse response){
        Json<Page<Map<String,String>>> json=selectBasicSeller(request);
        if(json.isSuccess()){
            if(json.getObj()!=null){
                try{
                    String fileName = "卖家数据"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
                    Page<Map<String,String>> page=json.getObj();
                    if(page!=null&&page.getList()!=null&&page.getList().size()>0){
                        List<Map<String,String>> listMap=page.getList();
                        List<UserSeller> listuser=new ArrayList<UserSeller>();
                        for(int i=0;i<listMap.size();i++){
                            UserSeller userSeller=new UserSeller();
                            PropertyUtils.copyProperties(userSeller,listMap.get(i));
                            listuser.add(userSeller);
                        }
                        new ExportExcel("卖家数据", UserSeller.class).setDataList(listuser).write(response, fileName).dispose();
                    }

                }catch(Exception e){
                    logger.error("导出卖家列表出现异常"+e.getMessage());
                }
            }
        }
        return null;
    }
    @RequestMapping(value = "exportall")
    public String exportAll(HttpServletRequest request,HttpServletResponse response){
        String rows="20";
        long rowsnum=new Long(rows).longValue();
        request.setAttribute("page","1");
        request.setAttribute("rows",rows);
        Json<Page<Map<String,String>>> json=selectBasicSeller(request);
        if(json.isSuccess()){
            if(json.getObj()!=null){
                try{
                    String fileNames = "卖家数据"+ DateUtils.getDate("yyyyMMddHHmmss");
                    String fileName = "卖家数据"+ DateUtils.getDate("yyyyMMddHHmmss");
                    Page<Map<String,String>> page=json.getObj();
                    if(page!=null&&page.getList()!=null&&page.getList().size()>0){
                        if(page.getCount()>rowsnum){
                            fileName=fileName+"第1部分";
                        }
                        fileName=fileName+".xlsx";
                        List<Map<String,String>> listMap=page.getList();
                        List<UserSeller> listuser=new ArrayList<UserSeller>();
                        for(int i=0;i<listMap.size();i++){
                            UserSeller userSeller=new UserSeller();
                            PropertyUtils.copyProperties(userSeller,listMap.get(i));
                            listuser.add(userSeller);
                        }
                        new ExportExcel("卖家数据", UserSeller.class).setDataList(listuser).write(response, fileName).dispose();
                        long p=page.getCount()/rowsnum;
                        long y=page.getCount()%rowsnum;
                        if(y>0){
                            p++;
                        }
                        if(p>1){
                            for(int i=1;i<p;i++){

                            }
                        }
                    }
                }catch(Exception e){
                    logger.error("导出卖家列表出现异常"+e.getMessage());
                }
            }
        }
        return null;
    }
    /**
     * 列表查询重新封装
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,String>> selectSellerShop(List<UserDTO> listDto,List<ShopDTO> listShop){
        Map<String,Map<String,String>> maps=storeShopMap(listShop);
        List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
        if(listDto!=null&&listDto.size()>0){
            //获取公司地址code的map集合
            /**************/
            Map<String,String> addmap=new HashMap<String, String>();
            Iterator<UserDTO> ite=listDto.iterator();
            while(ite.hasNext()){
                UserDTO us=ite.next();
                Map<String,String> codeMap= codeMap(us.getCompanyAddr());
                addmap.putAll(codeMap);
            }
            Map<String,String> dy= queryDiYu(addmap);
            /*****************/

            Map<Integer,String> userstatus=new HashMap<Integer,String>();
            //用户状态
            UserEnums.UserStatus[] userStatuss=UserEnums.UserStatus.values();
            for(UserEnums.UserStatus ut:userStatuss){
                userstatus.put(ut.getCode(),ut.getLabel());
            }
            Iterator<UserDTO> iterator=listDto.iterator();
            //封装字段
            int i=1;
            while(iterator.hasNext()){
                UserDTO userDTO=iterator.next();
                Map<String,String> mapuser=new HashMap<String, String>();
                mapuser.put("num",String.valueOf(i));
                i++;
                mapuser.put("uid",userDTO.getUid().toString());
                mapuser.put("companyName",userDTO.getCompanyName());
                mapuser.put("userstatus",userstatus.get(userDTO.getUserstatus()));
                if(null!=userDTO.getPlatformId()){
                mapuser.put("platformId",userDTO.getPlatformId().toString());
                }
                mapuser.put("address",duName(userDTO.getCompanyAddr(),dy));
                if(userDTO.getShopId()!=null){
                    Map shopMap=maps.get(userDTO.getShopId().toString());
                    if(shopMap!=null){
                        mapuser.putAll(shopMap);
                    }
                }
                mapList.add(mapuser);
            }
        }
        return mapList;
    }
    /**
     * 重新封装店铺信息
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private  Map<String,Map<String,String>> storeShopMap(List<ShopDTO> listShop){
        //1是申请，2是通过，3是驳回， 4是平台关闭，5是开通
        Map<Integer,String> shopstatus=new HashMap<Integer, String>();
        shopstatus.put(1,"申请");
        shopstatus.put(2,"通过");
        shopstatus.put(3,"驳回");
        shopstatus.put(4,"平台关机");
        shopstatus.put(5,"开通");
        //获取三级类目
        Map<Long,Map<String,String>> lmmaps= leiMu(listShop);
        Map<String,Map<String,String>> maps=new HashMap<String, Map<String, String>>();
        if(listShop!=null&&listShop.size()>0){
           Iterator<ShopDTO>  iterator=listShop.iterator();
           SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            while(iterator.hasNext()){
                ShopDTO dto=iterator.next();
                Map<String,String> map=maps.get(dto.getShopId().toString());
                if(map!=null){
                    //将品类拼写到一块
                    String pl=map.get("pl");
                    if(pl!=null&&!"".equals(pl)){
                        if(dto.getCid()!=null){
                            if(dto.getcStatus()!=null&&(dto.getcStatus()==1||dto.getcStatus()==2)){
                                Map<String,String> mpl=lmmaps.get(dto.getCid());
                                if(mpl!=null){
                                    String plx=mpl.get("sn");
                                    if(plx!=null&&!"".equals(plx)){
                                        map.put("pl",pl+"/"+plx);
                                    }else{
                                        map.put("pl",pl+"/"+dto.getCid().toString());
                                    }
                                }
                            }
                        }
                    }else{
                        if(dto.getCid()!=null){
                            if(dto.getcStatus()!=null&&(dto.getcStatus()==1||dto.getcStatus()==2)) {
                                Map<String, String> mpl = lmmaps.get(dto.getCid());
                                if (mpl != null) {
                                    String plx = mpl.get("sn");
                                    if (plx != null && !"".equals(plx)) {
                                        map.put("pl", plx);
                                        map.put("plt", plx);
                                    } else {
                                        map.put("pl", dto.getCid().toString());
                                        map.put("plt", dto.getCid().toString());
                                    }
                                }
                            }
                        }
                    }
                }else{
                    map=new HashMap<String, String>();
                    //将店铺名称/店铺code,品类，店铺状态，开通时间放到map中
                    map.put("dpni",dto.getShopName()+"/"+dto.getShopId());
                    if(dto.getCid()!=null){
                        if(dto.getcStatus()!=null&&(dto.getcStatus()==1||dto.getcStatus()==2)) {
                            Map<String, String> mpl = lmmaps.get(dto.getCid());
                            if (mpl != null) {
                                String plx = mpl.get("sn");
                                if (plx != null && !"".equals(plx)) {
                                    map.put("pl", plx);
                                    map.put("plt", plx);
                                } else {
                                    map.put("pl", dto.getCid().toString());
                                    map.put("plt", dto.getCid().toString());
                                }
                            } else {
                                map.put("pl", dto.getCid().toString());
                                map.put("plt", dto.getCid().toString());
                            }
                        }
                    }
                    map.put("status",shopstatus.get(dto.getStatus()));
                    if(dto.getPassTime()!=null){
                        map.put("passtime",simpleDateFormat.format(dto.getPassTime()));
                    }
                    maps.put(dto.getShopId().toString(),map);
                }
            }
        }
        return maps;
    }
    /**
     * 获取并封装店铺的三级类目
     * @author - 门光耀
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private Map<Long,Map<String,String>> leiMu(List<ShopDTO> listShop){
        //封装类目的map
        Map<Long,Map<String,String>> lmmaps=new HashMap<Long, Map<String, String>>();
        Map<Long,Long> lcmaps=new HashMap<Long, Long>();
        if(listShop!=null&&listShop.size()>0){
            Iterator<ShopDTO> iterator=listShop.iterator();
            while(iterator.hasNext()){
                ShopDTO shopDTO=iterator.next();
                if(shopDTO.getCid()!=null){
                    lcmaps.put(shopDTO.getCid(),shopDTO.getCid());
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
                    //一级类目便利 bn/
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
                                         map3.put("yc",item1.getCid().toString());
                                         map3.put("yn",item1.getCname());
                                         //二级类目的code和name
                                         map3.put("bc",item2.getCid().toString());
                                         map3.put("bn",item2.getCname());
                                         //三级类目的code和name
                                         map3.put("sc",item3.getCid().toString());
                                         map3.put("sn",item3.getCname());
                                         lmmaps.put(item3.getCid(),map3);
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
     * 区域级联
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectcity")
    public Json citysOfeastern(String parentCode){
        Json<List<AddressBase>> json=new Json<List<AddressBase>>();
        try{
            List<AddressBase> listshen=addressBaseService.queryAddressBase(parentCode);
            json.setSuccess(true);
            json.setMsg("获取成功");
            json.setObj(listshen);
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("获取失败"+e.getMessage());
        }
        return json;
    }
    /**
     * 商品分类级联级联
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectlm")
    public Json selectLm(Long parentNum){
        Json<List<ItemCategoryDTO>> json=new Json<List<ItemCategoryDTO>>();
        try{
            DataGrid<ItemCategoryDTO> dategrid=itemCategoryService.queryItemCategoryList(parentNum);
            json.setSuccess(true);
            json.setMsg("获取成功");
            json.setObj(dategrid.getRows());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("获取失败"+e.getMessage());
        }
        return json;
    }
    /**
     * 商家基本信息买家
     * @author - 门光耀
     * @message- 获取卖家明细信息，并跳转到卖家店铺明细信息页面
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/basicbuller")
    public ModelAndView basicBuller(){
        Map<String,String> map1=new HashMap<String, String>();
        map1.put("code","3");
        map1.put("name","买家待审核");
        Map<String,String> map2=new HashMap<String, String>();
        map2.put("code","4");
        map2.put("name","买家审核通过");
        List<Map<String,String>> listMap1=new ArrayList<Map<String, String>>();
        Map<String,String> map3=new HashMap<String, String>();
        map3.put("3","买家待审核");
        map3.put("4","买家审核通过");
        listMap1.add(map1);
        listMap1.add(map2);
        UserDTO userdto=new UserDTO();
        Map<String,Object> bacicsbuller=new HashMap<String, Object>();
        Page page =new Page();
        bacicsbuller.put("page",page);
        //店铺明细信息页面
        List<AddressBase> listshen=addressBaseService.queryAddressBase("0");
        //System.out.println(listshen.toString());
        bacicsbuller.put("address",listshen);
        com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
        pager.setPage(1);
        pager.setRows(10);
        userdto.setParentId(-1L);
        DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto, UserEnums.UserType.BUYER,pager);
        Map<String,String> addmap=new HashMap<String, String>();
        List<UserDTO> listDto=datagrid.getRows();
        if(listDto!=null&&listDto.size()>0){

            Iterator<UserDTO> ite=listDto.iterator();
            while(ite.hasNext()){
                UserDTO us=ite.next();
                Map<String,String> codeMap= codeMap(us.getCompanyAddr());
                addmap.putAll(codeMap);
            }
            //获取地域
            Map<String,String> dy= queryDiYu(addmap);
            Iterator<UserDTO> iterator=listDto.iterator();
            List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            int i=1;
            while(iterator.hasNext()){
                UserDTO userDTO=iterator.next();
                Map<String,String> map=new HashMap<String, String>();
                map.put("num",String.valueOf(i));
                i++;
                map.put("uid",userDTO.getUid()!=null?userDTO.getUid().toString():"");
                map.put("companyName",userDTO.getCompanyName());
                if(userDTO.getPlatformId()!=null){
                	map.put("platformId", userDTO.getPlatformId().toString());
                }
                if(userDTO.getCreated()!=null){
                    map.put("created",simpleDateFormat.format(userDTO.getCreated()));
                }
                map.put("companyAddr",duName(userDTO.getCompanyAddr(),dy));
                if(userDTO.getUserstatus()!=null){
                    map.put("userStace",map3.get(userDTO.getUserstatus().toString()));
                }
                listMap.add(map);
            }
            page.setList(listMap);
        }
        page.setCount(datagrid.getTotal());
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        //买家用户状态
        bacicsbuller.put("userStace",listMap1);
        return new ModelAndView("/sellercenter/sellerlist/basicbuller","bacicsbuller",bacicsbuller);
    }
    @RequestMapping(value = "exportbuypage")
    public String exportBuypage(HttpServletRequest request,HttpServletResponse response){
        Json<Page<Map<String,String>>> json=selectBasicBuller(request);
        if(json.isSuccess()){
            if(json.getObj()!=null){
                try{
                    String fileName = "买家数据"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
                    Page<Map<String,String>> page=json.getObj();
                    if(page!=null&&page.getList()!=null&&page.getList().size()>0){
                        List<Map<String,String>> listMap=page.getList();
                        List<UserBuyer> listuser=new ArrayList<UserBuyer>();
                        for(int i=0;i<listMap.size();i++){
                            UserBuyer userSeller=new UserBuyer();
                            PropertyUtils.copyProperties(userSeller,listMap.get(i));
                            listuser.add(userSeller);
                        }
                        new ExportExcel("买家数据", UserBuyer.class).setDataList(listuser).write(response, fileName).dispose();
                    }

                }catch(Exception e){
                    logger.error("导出买家列表出现异常"+e.getMessage());
                }
            }
        }
        return null;
    }
    /**
     * 商家基本信息买家查询分页
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectbuyercount")
    public Json<Page<Map<String,String>>> selectBuyerCount(HttpServletRequest request){
        Json json=new Json();
        try{
            Page page =new Page();
            com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
            UserDTO userDTO=new UserDTO();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            //用户状态
            String userStace=request.getParameter("userStace");
            if(userStace!=null&&!"".equals(userStace)){
                userDTO.setUserstatus(new Integer(userStace));
            }
            userDTO.setCompanyName(request.getParameter("companyName"));
            String uid=request.getParameter("uid");
            if(uid!=null&&!"".equals(uid)){
                userDTO.setUid(new Long(uid));
            }
            //省code
            String sheng=request.getParameter("sheng");
            //市code
            String shi=request.getParameter("shi");
            //区code
            String qu=request.getParameter("qu");
            String address="";
            if(shi!=null&&!"".equals(shi)){
                address=sheng+","+shi;
                if(qu!=null&&!"".equals(qu)){
                    address=address+","+qu;
                }
            }else{
                address=sheng;
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
            	userDTO.setPlatformId(new Integer(platformId));
            }
            userDTO.setCompanyAddr(address);
            DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userDTO, UserEnums.UserType.BUYER,pager);
            page.setCount(datagrid.getTotal());
            page.setPageNo(pager.getPage());
            page.setPageSize(pager.getRows());
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("查询方法出错"+e.getMessage());
        }
        return json;
    }
    /**
     * 商家基本信息买家查询分页
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectbasicbuller")
    public Json<Page<Map<String,String>>> selectBasicBuller(HttpServletRequest request){
        Json json=new Json();
        try{
            Page page =new Page();
            com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
            UserDTO userDTO=new UserDTO();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            //用户状态
            String userStace=request.getParameter("userStace");
            if(userStace!=null&&!"".equals(userStace)){
                userDTO.setUserstatus(new Integer(userStace));
            }
            userDTO.setCompanyName(request.getParameter("companyName"));
            String uid=request.getParameter("uid");
            if(uid!=null&&!"".equals(uid)){
                userDTO.setUid(new Long(uid));
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
            	userDTO.setPlatformId(new Integer(platformId));
            }
            //省code
            String sheng=request.getParameter("sheng");
            //市code
            String shi=request.getParameter("shi");
            //区code
            String qu=request.getParameter("qu");
            String address="";
            if(shi!=null&&!"".equals(shi)){
                address=sheng+","+shi;
                if(qu!=null&&!"".equals(qu)){
                    address=address+","+qu;
                }
            }else{
                address=sheng;
            }
            userDTO.setCompanyAddr(address);
            userDTO.setParentId(-1L);
            DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userDTO, UserEnums.UserType.BUYER,pager);
            Map<String,String> addmap=new HashMap<String, String>();
            List<UserDTO> listDto=datagrid.getRows();
            if(listDto!=null&&listDto.size()>0){
                Map<String,String> map3=new HashMap<String, String>();
                map3.put("3","买家待审核");
                map3.put("4","买家审核通过");
                Iterator<UserDTO> ite=listDto.iterator();
                while(ite.hasNext()){
                    UserDTO us=ite.next();
                    Map<String,String> codeMap= codeMap(us.getCompanyAddr());
                    addmap.putAll(codeMap);
                }
                //获取地域
                Map<String,String> dy= queryDiYu(addmap);
                Iterator<UserDTO> iterator=listDto.iterator();
                List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                int i=0;
                while(iterator.hasNext()){
                    UserDTO userDTO1=iterator.next();
                    Map<String,String> map=new HashMap<String, String>();
                    i++;
                    map.put("num",String.valueOf(i));
                    map.put("uid",userDTO1.getUid()!=null?userDTO1.getUid().toString():"");
                    map.put("companyName",userDTO1.getCompanyName());
                    if(userDTO1.getPlatformId()!=null){
                    	map.put("platformId", userDTO1.getPlatformId().toString());//所属平台
                    }
                    if(userDTO1.getCreated()!=null){
                        map.put("created",simpleDateFormat.format(userDTO1.getCreated()));
                    }
                    map.put("companyAddr",duName(userDTO1.getCompanyAddr(),dy));
                    if(userDTO1.getUserstatus()!=null){
                        map.put("userStace",map3.get(userDTO1.getUserstatus().toString()));
                    }
                    listMap.add(map);
                }
                page.setList(listMap);
            }
            page.setCount(datagrid.getTotal());
            page.setPageNo(pager.getPage());
            page.setPageSize(pager.getRows());
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("查询方法出错"+e.getMessage());
        }
        return json;
    }

    /**
     * 中信信息卖家
     * @author - 门光耀
     * @message- 获取卖家明细信息，并跳转到卖家店铺明细信息页面
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/citicseller")
    public ModelAndView citicSeller(){
        //中心账户信息

        Map<String,Object> citicseller=new HashMap<String,Object>();
        Page page =new Page();
        UserInfoDTO userInfoDTO=new UserInfoDTO();
        userInfoDTO.setIsHaveSellerCashAccount("1");
        UserDTO userDTO=new UserDTO();
        userDTO.setUsertype(UserEnums.UserType.SELLER.getCode());
        userInfoDTO.setUserType(UserEnums.UserType.SELLER);
        userInfoDTO.setUserDTO(userDTO);
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(10);
        DataGrid<UserInfoDTO> dataGrid=userExtendsService.findUserInfoList(userInfoDTO,pager);

        //封装查询信息和页面的数据
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        page.setCount(dataGrid.getTotal());
        //获取店铺信息
        ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
        List<ShopDTO> listshopDto=null;
        if(dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
            Iterator<UserInfoDTO> iterator=dataGrid.getRows().iterator();
            Map<Long,Long> mapshopids=new HashMap<Long, Long>();
            while(iterator.hasNext()){
                UserInfoDTO userDTO1=iterator.next();
                if(userDTO1.getUserDTO().getShopId()!=null){
                    mapshopids.put(userDTO1.getUserDTO().getShopId(),userDTO1.getUserDTO().getShopId());
                }
            }
            if(mapshopids.keySet().size()>0){
                Iterator<Long> iterator1=mapshopids.keySet().iterator();
                Long[] shopids=new Long[mapshopids.keySet().size()];
                int i=0;
                while(iterator1.hasNext()){
                    shopids[i]=iterator1.next();
                    i++;
                }
                shopAudiinDTO.setShopIds(shopids);
                ExecuteResult<List<ShopDTO>> executeResult=shopExportService.queryShopInfoByids(shopAudiinDTO);
                listshopDto=executeResult.getResult();
            }
        }
        page.setList(tdoToMap(dataGrid.getRows(),listshopDto));
        citicseller.put("page",page);
        //中心账户状态
        CommonEnums.ComStatus[] comStatus=CommonEnums.ComStatus.values();
        citicseller.put("zxstatus",comStatus);
        //店铺明细信息页面
        return new ModelAndView("/sellercenter/sellerlist/citicseller","citicseller",citicseller);
    }
    /**
     * 商家中信信息卖家查询
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectciticseller")
    public Json selectCiticSeller(HttpServletRequest request,HttpServletResponse response,Model model){
        Json json=new Json();
        try{
            Page page =new Page();
            UserInfoDTO userInfoDTO=new UserInfoDTO();
            UserDTO userDTO=new UserDTO();
            userDTO.setUsertype(UserEnums.UserType.SELLER.getCode());
            userInfoDTO.setUserType(UserEnums.UserType.SELLER);
            userInfoDTO.setIsHaveSellerCashAccount("1");
            userInfoDTO.setUserDTO(userDTO);
            //用户编号
            String uid=request.getParameter("uid");
            if(uid!=null&&!"".equals(uid)){
                userInfoDTO.setUserId(new Long(uid));
                userDTO.setUid(new Long(uid));
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
            	userDTO.setPlatformId(new Integer(platformId));
            }
            //店铺名称
            String shopName=request.getParameter("shopName");
            List<ShopDTO> listshopDto=null;
            boolean ifselectShop=true;
            //获取店铺信息
            ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
            if(shopName!=null&&!"".equals(shopName)){
                shopAudiinDTO.setShopName(shopName);
            }else{
                ifselectShop=false;
            }
            boolean ifselectuser=true;
            if(ifselectShop){
                ExecuteResult<List<ShopDTO>> executeResult=shopExportService.queryShopInfoByids(shopAudiinDTO);
                listshopDto=executeResult.getResult();
                //封装shop的id
                if(listshopDto!=null&&listshopDto.size()>0){
                    Iterator<ShopDTO> iterator=listshopDto.iterator();
                    Map<Long,Long> mapshopids=new HashMap<Long, Long>();
                    while(iterator.hasNext()){
                        ShopDTO shopDTO=iterator.next();
                        if(shopDTO.getShopId()!=null){
                            mapshopids.put(shopDTO.getShopId(),shopDTO.getShopId());
                        }
                    }
                    if(mapshopids.size()>0){
                        Long[] shopids=new Long[mapshopids.size()];
                        Iterator<Long> iterator1=mapshopids.keySet().iterator();
                        int i=0;
                        while(iterator1.hasNext()){
                            shopids[i]=iterator1.next();
                            i++;
                        }
                        userDTO.setShopIds(shopids);
                    }else{
                        ifselectuser=false;
                    }
                }else{
                    ifselectuser=false;
                }
            }
            //中心账户状态
            String citicflag=request.getParameter("citicflag");
            if(citicflag!=null&&!"".equals(citicflag)){
                UserCiticDTO userCiticDTO=new UserCiticDTO();
                userCiticDTO.setAccountState(new Integer(citicflag));
                userInfoDTO.setUserCiticDTO(userCiticDTO);
            }
            Pager pager=new Pager();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            DataGrid<UserInfoDTO> dataGrid=null;
            if(ifselectuser){
                dataGrid=userExtendsService.findUserInfoList(userInfoDTO,pager);
            }else{
                dataGrid=new DataGrid<UserInfoDTO>();
            }
            //封装查询信息和页面的数据
            page.setPageNo(pager.getPage());
            page.setPageSize(pager.getRows());
            page.setCount(dataGrid.getTotal());
            if(ifselectShop==false){
                if(dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
                    Iterator<UserInfoDTO> iterator=dataGrid.getRows().iterator();
                    Map<Long,Long> mapshopids=new HashMap<Long, Long>();
                    while(iterator.hasNext()){
                        UserInfoDTO userDTO1=iterator.next();
                        if(userDTO1.getUserDTO().getShopId()!=null){
                            mapshopids.put(userDTO1.getUserDTO().getShopId(),userDTO1.getUserDTO().getShopId());
                        }
                    }
                    if(mapshopids.keySet().size()>0){
                        Iterator<Long> iterator1=mapshopids.keySet().iterator();
                        Long[] shopids=new Long[mapshopids.keySet().size()];
                        int i=0;
                        while(iterator1.hasNext()){
                            shopids[i]=iterator1.next();
                            i++;
                        }
                        shopAudiinDTO.setShopIds(shopids);
                        ExecuteResult<List<ShopDTO>> executeResult=shopExportService.queryShopInfoByids(shopAudiinDTO);
                        listshopDto=executeResult.getResult();
                    }
                }
            }
            page.setList(tdoToMap(dataGrid.getRows(),listshopDto));
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("查询方法出错"+e.getMessage());
        }
        return json;
    }
    public List<Map<String,String>> tdoToMap(List<UserInfoDTO> listdto,List<ShopDTO> shopDTOList){
        List<Map<String,String>> listmap=new ArrayList<Map<String, String>>();
        Map<Long,String> maps=new HashMap<Long, String>();
        if(shopDTOList!=null&&shopDTOList.size()>0){
            Iterator<ShopDTO> iterator=shopDTOList.iterator();
            while(iterator.hasNext()){
                ShopDTO shopDTO=iterator.next();
                maps.put(shopDTO.getShopId(),shopDTO.getShopName()+"/"+shopDTO.getShopId().toString());
            }
        }
        CommonEnums.ComStatus[] comStatuses= CommonEnums.ComStatus.values();
        Map<Integer,String> zxstatus=new HashMap<Integer, String>();
        for(CommonEnums.ComStatus cc:comStatuses){
            zxstatus.put(new Integer(cc.getCode()),cc.getLabel());
        }
        if(listdto!=null&&listdto.size()>0){
            Iterator<UserInfoDTO> iterator= listdto.iterator();
            int i=1;
            while(iterator.hasNext()){
                UserInfoDTO udto=iterator.next();
                Map<String,String> map=new HashMap<String, String>();
                map.put("num",String.valueOf(i));
                i++;
                map.put("uid",udto.getUserId()!=null?udto.getUserId().toString():"");
                if(udto.getUserCiticDTO()!=null){
                    //卖家收款账户
                    map.put("sellers",udto.getUserCiticDTO().getSellerWithdrawsCashAccount());
                    //卖家冻结账户
                    map.put("sellerd",udto.getUserCiticDTO().getSellerFrozenAccount());
                    //卖家账户状态
                    map.put("sellersstatus",zxstatus.get(udto.getUserCiticDTO().getAccountState()));
                    map.put("sellerdstatus",zxstatus.get(udto.getUserCiticDTO().getAccountState()));
                    //买家融资账户
                    map.put("rz",udto.getUserCiticDTO().getBuyerFinancingAccount());
                    //买家支付账户
                    map.put("zf",udto.getUserCiticDTO().getBuyerPaysAccount());
                    map.put("rzs",zxstatus.get(udto.getUserCiticDTO().getAccountState()));
                    map.put("zfs",zxstatus.get(udto.getUserCiticDTO().getAccountState()));
                }
                if(udto.getUserDTO()!=null){
                    map.put("shopname",maps.get(udto.getUserDTO().getShopId()));
                }
                if(udto.getUserBusinessDTO()!=null){
                    //公司名称
                    map.put("companyname",udto.getUserBusinessDTO().getCompanyName());
                }
                if(udto.getUserDTO().getPlatformId()!=null){
                	map.put("platformId", udto.getUserDTO().getPlatformId().toString());
                }
                listmap.add(map);
            }
        }
        return listmap;
    }
    /**
     * 中心信息买家
     * @author - 门光耀
     * @message- 获取卖家明细信息，并跳转到卖家店铺明细信息页面
     * @createDate - 2015-3-3
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/citicbuller")
    public ModelAndView citicBuller(){
        Map<String,Object> citicbuller=new HashMap<String,Object>();
        Page page =new Page();
        UserInfoDTO userInfoDTO=new UserInfoDTO();
        UserDTO userDTO=new UserDTO();
        userDTO.setUsertype(UserEnums.UserType.BUYER.getCode());
        userInfoDTO.setUserType(UserEnums.UserType.BUYER);
        userInfoDTO.setIsHaveBuyerPaysAccount("1");
        userInfoDTO.setUserDTO(userDTO);
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(10);
        DataGrid<UserInfoDTO> dataGrid=userExtendsService.findUserInfoList(userInfoDTO,pager);
        //封装查询信息和页面的数据
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        page.setCount(dataGrid.getTotal());
        page.setList(tdoToMap(dataGrid.getRows(),null));
        citicbuller.put("page",page);
        //中心账户状态
        CommonEnums.ComStatus[] comStatus=CommonEnums.ComStatus.values();
        citicbuller.put("zxstatus",comStatus);
        //店铺明细信息页面
        return new ModelAndView("/sellercenter/sellerlist/citicbuller","citicbuller",citicbuller);
    }
    /**
     * 商家中信信息卖家查询
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("selectciticbuller")
    public Json selectCiticBuller(HttpServletRequest request,HttpServletResponse response,Model model){
        Json json=new Json();
        try{
            Page page =new Page();
            UserInfoDTO userInfoDTO=new UserInfoDTO();
            UserDTO userDTO=new UserDTO();
            userDTO.setUsertype(UserEnums.UserType.BUYER.getCode());
            userInfoDTO.setUserType(UserEnums.UserType.BUYER);
            userInfoDTO.setIsHaveBuyerPaysAccount("1");
            userInfoDTO.setUserDTO(userDTO);
            //用户编号
            String uid=request.getParameter("uid");
            if(uid!=null&&!"".equals(uid)){
                userInfoDTO.setUserId(new Long(uid));
                userDTO.setUid(new Long(uid));
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
            	userDTO.setPlatformId(new Integer(platformId));
            }
            //公司名称
            String shopName=request.getParameter("shopName");
            userDTO.setCompanyName(shopName);
            //中心账户状态
            String citicflag=request.getParameter("citicflag");
            if(citicflag!=null&&!"".equals(citicflag)){
                UserCiticDTO userCiticDTO=new UserCiticDTO();
                userCiticDTO.setAccountState(new Integer(citicflag));
                userInfoDTO.setUserCiticDTO(userCiticDTO);
            }
            Pager pager=new Pager();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            DataGrid<UserInfoDTO> dataGrid=userExtendsService.findUserInfoList(userInfoDTO,pager);
            //封装查询信息和页面的数据
            page.setPageNo(pager.getPage());
            page.setPageSize(pager.getRows());
            page.setCount(dataGrid.getTotal());
            page.setList(tdoToMap(dataGrid.getRows(),null));
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("查询方法出错"+e.getMessage());
        }
        return json;
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
