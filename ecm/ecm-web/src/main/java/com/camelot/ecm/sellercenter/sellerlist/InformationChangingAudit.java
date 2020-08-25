package com.camelot.ecm.sellercenter.sellerlist;

import com.alibaba.dubbo.common.json.JSONReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.common.Json;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.storecenter.dto.*;
import com.camelot.storecenter.dto.emums.ShopModify;
import com.camelot.storecenter.dto.indto.ShopInfoModifyInDTO;
import com.camelot.storecenter.service.*;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.contract.UserApplyAuditInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.enums.UserInfoModifyEmums;
import com.camelot.usercenter.service.UserApplyAuditService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  商家管理，信息变更审核
 * @author - 门光耀
 * @message- 商家信息审核，店铺信息审核
 * @createDate - 2015-3-9
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/informationchangingaudit")
public class InformationChangingAudit extends BaseController {
    @Resource
    private UserApplyAuditService userApplyAuditService;
    @Resource
    private ShopCategoryExportService shopCategoryExportService;
    @Resource
    private UserExportService userExportService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
    private ShopBrandExportService shopBrandExportService;
    @Resource
    private ShopModifyDetailExportService shopModifyDetailExportService;
    @Resource
    private ShopModifyInfoExportService shopModifyInfoExportService;
    @Resource
    private ItemBrandExportService itemBrandExportService;
    @Resource
    private UserExtendsService userExtendsService;
    @Resource
    private AddressBaseService addressBaseService;
    @Resource
    private ShopExportService shopExportService;
    /**
     * @author - 门光耀
     * @message- 商家信息审核查询
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/userauditselect")
        public ModelAndView userAuditSelect(){
        Map<String,Object> auditinfo=new HashMap<String, Object>();
        //申请所处状态 0, 新建(待审核); 1, 审核通过 2, 审核不通过;
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        Map<String,String> map1=new HashMap<String, String>();
        map1.put("code","0");
        map1.put("name","待审核");
        Map<String,String> map2=new HashMap<String, String>();
        map2.put("code","1");
        map2.put("name","审核通过");
        Map<String,String> map3=new HashMap<String, String>();
        map3.put("code","2");
        map3.put("name","审核不通过");
        Map<String,String> map4=new HashMap<String, String>();
        map4.put("name","全部");
        listMap.add(map1);
        listMap.add(map2);
        listMap.add(map3);
        listMap.add(map4);
        auditinfo.put("liststace",listMap);
        UserModifyInfoDTO userModifyInfoDTO=new UserModifyInfoDTO();
        userModifyInfoDTO.setApplyStatus(new Integer(0));
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(10);
        DataGrid<UserModifyInfoDTO> dataGrid= userApplyAuditService.findUserAuditListByCondition(userModifyInfoDTO,pager);
        Page page=new Page();
        page.setPageNo(1);
        page.setPageSize(10);
        page.setCount(dataGrid.getTotal());
        page.setList(userModifyInfotoMap(dataGrid.getRows()));
        auditinfo.put("page",page);
        return new ModelAndView("/sellercenter/informationchangeaudit/userauditselect","auditinfo",auditinfo);
    }
    private List<Map<String,String>> userModifyInfotoMap(List<UserModifyInfoDTO> listuser){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(listuser!=null&&listuser.size()>0){
            Map<Integer,String> applystace=new HashMap<Integer, String>();
            //  申请所处状态 0, 新建(待审核); 1, 审核通过 2, 审核不通过;
         // applystace.put(new Integer(-1),"审核过期");
            applystace.put(new Integer(0),"待审核");
            applystace.put(new Integer(1),"审核通过");
            applystace.put(new Integer(2),"审核不通过");
            Iterator<UserModifyInfoDTO> iterator=listuser.iterator();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            while(iterator.hasNext()){
                UserModifyInfoDTO userModifyInfoDTO=iterator.next();
                Map<String,String> map=new HashMap<String, String>();
                //用户id
                map.put("uid",userModifyInfoDTO.getApplicantUserid()!=null?userModifyInfoDTO.getApplicantUserid().toString():"");
                //申请人名称
                map.put("applicantName",userModifyInfoDTO.getApplicantName());
                if(userModifyInfoDTO.getCreateTime()!=null){
                    map.put("createTime",simpleDateFormat.format(userModifyInfoDTO.getCreateTime()));
                }
                map.put("id",userModifyInfoDTO.getId()!=null?userModifyInfoDTO.getId().toString():"");
                //审核状态
                if(userModifyInfoDTO.getApplyStatus()!=null){
                    map.put("stace",applystace.get(userModifyInfoDTO.getApplyStatus()));
                    map.put("stacecode",new BigDecimal(userModifyInfoDTO.getApplyStatus()).toPlainString());
                }
                map.put("modifyType",userModifyInfoDTO.getModifyType());
                listMap.add(map);
            }
        }
        return listMap;
    }
    /**
     * @author - 门光耀
     * @message- 商家信息审核查询ajax查询
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/selectuseraudit")
    @ResponseBody
    public Json selectUserAudit(HttpServletRequest request){
        Json json=new Json();
        Pager pager=new Pager();
        pager.setPage(new Integer(request.getParameter("page").toString()));
        pager.setRows(new Integer(request.getParameter("rows").toString()));
        String userName=request.getParameter("userName");
        String uid=request.getParameter("userCode");
        //状态
        String stace=request.getParameter("stace");
        UserModifyInfoDTO userModifyInfoDTO=new UserModifyInfoDTO();
        if(uid!=null&&!"".equals(uid)){
            userModifyInfoDTO.setApplicantUserid(new Long(uid));
        }
        //设置查询状态
        if(stace!=null&&!"".equals(stace)){
            userModifyInfoDTO.setApplyStatus(new Integer(stace));
        }
        userModifyInfoDTO.setApplicantName(userName);
        DataGrid<UserModifyInfoDTO> dataGrid= userApplyAuditService.findUserAuditListByCondition(userModifyInfoDTO, pager);
        Page page=new Page();
        page.setCount(dataGrid.getTotal());
        page.setList(userModifyInfotoMap(dataGrid.getRows()));
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        json.setSuccess(true);
        json.setMsg(page.toString());
        json.setObj(page);
        return json;
    }
    /**
     *
     * @author - 门光耀
     * @message- 商家信息审核--商家信息变更审核
     * @createDate - 2015-3-10
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/userauditdetail")
    public ModelAndView userAuditDetail(HttpServletRequest  request){
        String id=request.getParameter("id");
        String uid=request.getParameter("uid");
        UserModifyInfoDTO userModifyInfoDTO=new UserModifyInfoDTO();
        Map<String,Object> auditMap=new HashMap<String, Object>();
        Pager pager=new Pager();
        pager.setPage(1);
        //查询一百条修改信息
        pager.setRows(300);
        DataGrid<UserModifyDetailDTO> dataGrid= null;
        if(id!=null&&!"".equals(id)){
            auditMap.put("id",id);
            userModifyInfoDTO.setId(new Long(id));
            dataGrid=userApplyAuditService.findUserModifyDetailByCondition(userModifyInfoDTO,pager);
        }
        if(dataGrid!=null){
            List<UserModifyDetailDTO> listdetail=dataGrid.getRows();
            Map<String,String> ifAddUrl =ifAddUrl();
            Map<String,String> addressMap=addressMap();
            //需要转移的字段
            Map<String,Map<String,String>> userZy=userZy();
            if(listdetail!=null&&listdetail.size()>0){
                Iterator<UserModifyDetailDTO> iterator=listdetail.iterator();
                while(iterator.hasNext()){
                    UserModifyDetailDTO um=iterator.next();
                    String coname=ifAddUrl.get(um.getColumnName());
                    String coname1=addressMap.get(um.getColumnName());
                    Map<String,String> mapzy=userZy.get(um.getColumnName());
                    if(coname!=null&&!"".equals(coname)){
                        String bf=um.getBeforeChangeValue();
                        String af=um.getAfterChangeValue();
                        if(bf!=null&&!"".equals(bf)){
                            bf= SysProperties.getProperty("ngIp")+bf;
                            int www= bf.indexOf("www");
                            int http=bf.indexOf("http");
                            int ftp=bf.indexOf("ftp");
                            int rar = bf.indexOf(".rar");
                            int zip = bf.indexOf(".zip");
                            int z = bf.indexOf(".7z");
                            if(www>=0||http>=0||ftp>=0){
                            	if(rar>=0||zip>=0||z>=0){
                            		bf="<a href=\""+bf+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+bf+"</a>";
                              	}else{
                              		bf = "<img src=\""+bf+"\" id=\"bannerUrlImg\" alt=\"\"  class=\"mar_lr10 fl showimg\" style=\"cursor: pointer; opacity: 1;width:150px;height:40px;\">";
                              	}
                            }
                            um.setBeforeChangeValue(bf);
                        }
                        if(af!=null&&!"".equals(af)) {
                            af = SysProperties.getProperty("ngIp") + af;
                            int www= af.indexOf("www");
                            int http=af.indexOf("http");
                            int ftp=af.indexOf("ftp");
                            int rar = af.indexOf(".rar");
                            int zip = af.indexOf(".zip");
                            int z = af.indexOf(".7z");
                            if(www>=0||http>=0||ftp>=0){
                            	if(rar>=0||zip>=0||z>=0){
                            		af="<a href=\""+af+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+af+"</a>";
                              	}else{
                              		af = "<img src=\""+af+"\" id=\"bannerUrlImg\" alt=\"\"  class=\"mar_lr10 fl showimg\" style=\"cursor: pointer; opacity: 1;width:150px;height:40px;\">";
                              	}
                            }
                            um.setAfterChangeValue(af);
                        }
                    }else if(coname1!=null&&!"".equals(coname1)){
                        if(um.getBeforeChangeValue()!=null&&!"".equals(um.getBeforeChangeValue())){
                            um.setBeforeChangeValue(queryDy(um.getBeforeChangeValue()));
                        }
                        if(um.getAfterChangeValue()!=null&&!"".equals(um.getAfterChangeValue())){
                            um.setAfterChangeValue(queryDy(um.getAfterChangeValue()));
                        }
                    }else if(mapzy!=null){
                        if(um.getBeforeChangeValue()!=null&&!"".equals(um.getBeforeChangeValue())){
                            um.setBeforeChangeValue(mapzy.get(um.getBeforeChangeValue()));
                        }
                        if(um.getAfterChangeValue()!=null&&!"".equals(um.getAfterChangeValue())){
                            um.setAfterChangeValue(mapzy.get(um.getAfterChangeValue()));
                        }
                    }else{
                        String bf=um.getBeforeChangeValue();
                        String af=um.getAfterChangeValue();
                        if(bf!=null&&!"".equals(bf)){
                            int www= bf.indexOf("www");
                            int http=bf.indexOf("http");
                            int ftp=bf.indexOf("ftp");
                            int rar = bf.indexOf(".rar");
                            int zip = bf.indexOf(".zip");
                            int z = bf.indexOf(".7z");
                            if(www>=0||http>=0||ftp>=0){
                            	if(rar>=0||zip>=0||z>=0){
                            		bf="<a href=\""+bf+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+bf+"</a>";
                              	}else{
                              		bf = "<img src=\""+bf+"\" id=\"bannerUrlImg\" alt=\"\"  class=\"mar_lr10 fl showimg\" style=\"cursor: pointer; opacity: 1;width:150px;height:40px;\">";
                              	}
                            }
                            um.setBeforeChangeValue(bf);
                        }
                        if(af!=null&&!"".equals(af)){
                            int www= af.indexOf("www");
                            int http=af.indexOf("http");
                            int ftp=af.indexOf("ftp");
                            int rar = af.indexOf(".rar");
                            int zip = af.indexOf(".zip");
                            int z = af.indexOf(".7z");
                            if(www>=0||http>=0||ftp>=0){
                            	if(rar>=0||zip>=0||z>=0){
                            		af="<a href=\""+af+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+af+"</a>";
                              	}else{
                              		af = "<img src=\""+af+"\" id=\"bannerUrlImg\" alt=\"\"  class=\"mar_lr10 fl showimg\" style=\"cursor: pointer; opacity: 1;width:150px;height:40px;\">";
                              	}
                            }
                            um.setAfterChangeValue(af);
                        }
                    }
                }
                auditMap.put("list",listdetail);
            }
        }
        auditMap.put("uid",uid);
        UserDTO userdto=new UserDTO();
        Pager pager1=new Pager();
        pager1.setPage(1);
        pager1.setRows(10);
        //DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto, UserEnums.UserType.SELLER,pager);
        ExecuteResult<UserInfoDTO> result= userExtendsService.findUserInfo(new Long(uid));
        if(result!=null){
            UserInfoDTO userInfoDTO=result.getResult();
            if(userInfoDTO!=null){
                UserDTO userDTO=userInfoDTO.getUserDTO();
                if(userDTO!=null){
                    auditMap.put("userType",userDTO.getUsertype()!=null?userDTO.getUsertype().toString():"");
                }
                UserBusinessDTO userBusinessDTO=userInfoDTO.getUserBusinessDTO();
                if(userBusinessDTO!=null){
                    auditMap.put("company",userBusinessDTO.getCompanyName());
                }
            }
        }
        return new ModelAndView("/sellercenter/informationchangeaudit/userauditdetail","auditMap",auditMap);
    }
    private String queryDy(String addressid){
        String address="";
        if(addressid!=null&&!"".equals(addressid)){
            try{
                String[] addids=addressid.split(",");
                Map<String,String> mapcodes=new HashMap<String, String>();
                for(String id:addids){
                    if(id!=null&&!"".equals(id)){
                        mapcodes.put(id,"");
                    }
                }
                Map<String,String> queryDiYu=queryDiYu(mapcodes);
                if(queryDiYu.size()>0){
                    int i=0;
                    for(String id:addids){
                        if(id!=null&&!"".equals(id)){
                            if(i==0){
                                address=queryDiYu.get(id);
                            }else{
                                address=address+">>"+queryDiYu.get(id);
                            }
                            i++;
                        }
                    }
                }
            }catch(Exception e){
                logger.error(e.getMessage());
                address=addressid;
            }
        }
        return address;
    }
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
     *
     * @author - 门光耀
     * @message- 商家信息审核--商家信息变更审核
     * @createDate - 2015-3-10
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/showuserauditdetail")
    public ModelAndView showuserAuditDetail(HttpServletRequest  request){
        String id=request.getParameter("id");
        String uid=request.getParameter("uid");
        UserModifyInfoDTO userModifyInfoDTO=new UserModifyInfoDTO();
        Map<String,Object> auditMap=new HashMap<String, Object>();
        Pager pager=new Pager();
        pager.setPage(1);
        //查询一百条修改信息
        pager.setRows(300);
        DataGrid<UserModifyDetailDTO> dataGrid= null;
        if(id!=null&&!"".equals(id)){
            auditMap.put("id",id);
            userModifyInfoDTO.setId(new Long(id));
            dataGrid=userApplyAuditService.findUserModifyDetailByCondition(userModifyInfoDTO,pager);
        }
        if(dataGrid!=null){
            List<UserModifyDetailDTO> listdetail=dataGrid.getRows();
            Map<String,String> ifAddUrl =ifAddUrl();
            Map<String,String> addressMap=addressMap();
            //需要转移的字段
            Map<String,Map<String,String>> userZy=userZy();
            if(listdetail!=null&&listdetail.size()>0){
                Iterator<UserModifyDetailDTO> iterator=listdetail.iterator();
                while(iterator.hasNext()){
                    UserModifyDetailDTO um=iterator.next();
                    String coname=ifAddUrl.get(um.getColumnName());
                    String coname1=addressMap.get(um.getColumnName());
                    Map<String,String> mapzy=userZy.get(um.getColumnName());
                    if(coname!=null&&!"".equals(coname)){
                        String bf=um.getBeforeChangeValue();
                        String af=um.getAfterChangeValue();
                        if(bf!=null&&!"".equals(bf)){
                            bf= SysProperties.getProperty("ngIp")+bf;
                            int www= bf.indexOf("www");
                            int http=bf.indexOf("http");
                            int ftp=bf.indexOf("ftp");
                            if(www>=0||http>=0||ftp>=0){
                                bf="<a href=\""+bf+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+bf+"</a>";
                            }
                            um.setBeforeChangeValue(bf);
                        }
                        if(af!=null&&!"".equals(af)){
                            af= SysProperties.getProperty("ngIp")+af;
                            int www= af.indexOf("www");
                            int http=af.indexOf("http");
                            int ftp=af.indexOf("ftp");
                            if(www>=0||http>=0||ftp>=0){
                                af="<a href=\""+af+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+af+"</a>";
                            }
                            um.setAfterChangeValue(af);
                        }
                    }else if(coname1!=null&&!"".equals(coname1)){
                        if(um.getBeforeChangeValue()!=null&&!"".equals(um.getBeforeChangeValue())){
                            um.setBeforeChangeValue(queryDy(um.getBeforeChangeValue()));
                        }
                        if(um.getAfterChangeValue()!=null&&!"".equals(um.getAfterChangeValue())){
                            um.setAfterChangeValue(queryDy(um.getAfterChangeValue()));
                        }
                    }else if(mapzy!=null){
                        if(um.getBeforeChangeValue()!=null&&!"".equals(um.getBeforeChangeValue())){
                            um.setBeforeChangeValue(mapzy.get(um.getBeforeChangeValue()));
                        }
                        if(um.getAfterChangeValue()!=null&&!"".equals(um.getAfterChangeValue())){
                            um.setAfterChangeValue(mapzy.get(um.getAfterChangeValue()));
                        }
                    }else{
                        String bf=um.getBeforeChangeValue();
                        String af=um.getAfterChangeValue();
                        if(bf!=null&&!"".equals(bf)){
                            int www= bf.indexOf("www");
                            int http=bf.indexOf("http");
                            int ftp=bf.indexOf("ftp");
                            if(www>=0||http>=0||ftp>=0){
                                bf="<a href=\""+bf+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+bf+"</a>";
                            }
                            um.setBeforeChangeValue(bf);
                        }
                        if(af!=null&&!"".equals(af)){
                            int www= af.indexOf("www");
                            int http=af.indexOf("http");
                            int ftp=af.indexOf("ftp");
                            if(www>=0||http>=0||ftp>=0){
                                af="<a href=\""+af+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+af+"</a>";
                            }
                            um.setAfterChangeValue(af);
                        }
                    }
                }
                auditMap.put("list",listdetail);
            }
        }
        auditMap.put("uid",uid);
        UserDTO userdto=new UserDTO();
        Pager pager1=new Pager();
        pager1.setPage(1);
        pager1.setRows(10);
        //DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto, UserEnums.UserType.SELLER,pager);
        DataGrid<UserDTO> dataGrid1=userExportService.findUserListByCondition(userdto, null,pager1);
        if(dataGrid1!=null){
            List<UserDTO> listuser=dataGrid1.getRows();
            if(listuser!=null&&listuser.size()>0){
                UserDTO userDTO=listuser.get(0);
                auditMap.put("company",userDTO.getCompanyName());
                auditMap.put("userType",userDTO.getUsertype()!=null?userDTO.getUsertype().toString():"");
            }
        }
        return new ModelAndView("/sellercenter/informationchangeaudit/showuserauditdetail","auditMap",auditMap);
    }
    /**
     *
     * @author - 门光耀
     * @message- 商家信息审核--商家信息变更审核结果
     * @createDate - 2015-3-10
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/auditresult")
    @ResponseBody
    public Json auditResult(HttpServletRequest request,HttpServletResponse response,Model model){
        Json json=new Json();
        //用户id
        String uid=request.getParameter("uid");
        //审核id
        String id=request.getParameter("id");
        String stace=request.getParameter("stace");
        String ms=request.getParameter("ms");
        //审核人id
        String idd=UserUtils.getUser().getId();
        try{
            //2通过，0驳回
            if(id!=null&&!"".equals(id)){
                if("1".equals(stace)){
                    ms="审批通过";
                    ExecuteResult<Integer> executeResult=userApplyAuditService.moifyUserApplyAudit(id,idd, CommonEnums.ComStatus.PASS,ms);
                    Integer xgcount=executeResult.getResult();
                    if(xgcount!=null&&xgcount>0){
                        json.setMsg("审核成功");
                        json.setSuccess(true);
                    }else{
                        json.setSuccess(false);
                        json.setMsg("审核失败");
                    }
                }else if("2".equals(stace)){
                    ExecuteResult<Integer> executeResult=userApplyAuditService.moifyUserApplyAudit(id,idd, CommonEnums.ComStatus.REJECT,ms);
                    Integer xgcount=executeResult.getResult();
                    if(xgcount!=null&&xgcount>0){
                        json.setMsg("审核成功");
                        json.setSuccess(true);
                    }else{
                        json.setSuccess(false);
                        json.setMsg("审核失败");
                    }
                }
            }else{
                json.setSuccess(false);
                json.setMsg("后台获取的审核id为空，请联系管理员");
                return json;
            }
        }catch(Exception e){
            json.setMsg("审核出现未知错误，请联系运维人员"+e.getMessage());
            logger.error(e.getMessage());
            logger.error(e.toString());
            json.setSuccess(false);
        }
        request.setAttribute("json",json);
        return json;
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--基本信息修改
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/shopauditselectjb")
    public ModelAndView shopAuditSelectJB(){
        Page page=new Page();
        page.setPageNo(1);
        page.setPageSize(10);
        ShopModifyInfoDTO shopModifyInfoDTO=new ShopModifyInfoDTO();
        Pager pager=new Pager();
        pager.setRows(10);
        pager.setPage(1);
        ExecuteResult<DataGrid<ShopModifyInfoDTO>> executeResult=shopModifyInfoExportService.queryShopModifyInfo(shopModifyInfoDTO,pager);
        DataGrid<ShopModifyInfoDTO> dataGrid=executeResult.getResult();
        Map<String,Object> auditLm=new HashMap<String, Object>();
        auditLm.put("page",page);
        if(dataGrid!=null){
            page.setCount(dataGrid.getTotal());
            List<ShopModifyInfoDTO> list=dataGrid.getRows();
            //用户信息查询
            List<UserDTO> list1=null;
            //首先查询出需要审核的店铺信息，如果有数据则根据店铺的shopid去查询用户信息，再将两者的信息拼接成列表数据用于前台显示
            if(list!=null&&list.size()>0){
                Iterator<ShopModifyInfoDTO> iterator=list.iterator();
                Map<Long,Long> shopidMap=new HashMap<Long, Long>();
                while(iterator.hasNext()){
                    ShopModifyInfoDTO shopCategoryDTO1=iterator.next();
                    if(shopCategoryDTO1.getShopId()!=null){
                        shopidMap.put(shopCategoryDTO1.getShopId(),shopCategoryDTO1.getShopId());
                    }
                }
                if(shopidMap.size()>0){
                    Long[] shipids=new Long[shopidMap.size()];
                    Iterator<Long> iterator1=shopidMap.keySet().iterator();
                    int i=0;
                    while(iterator1.hasNext()){
                        shipids[i]=iterator1.next();
                        i++;
                    }
                    UserDTO userDTO=new UserDTO();
                    //将店铺的shopids放于用户的查询条件中，查询出用户的数据
                    userDTO.setShopIds(shipids);
                    Pager pager1=new Pager();
                    pager1.setPage(1);
                    pager1.setRows(Integer.MAX_VALUE);
                    DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO,null,pager1);
                    list1=userDTODataGrid.getRows();
                }
            }
            page.setList(listDtoTolistMapJb(list, list1));
        }else{
            page.setCount(0);
        }
        return new ModelAndView("/sellercenter/informationchangeaudit/shopauditselectjb","page",page);
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--基本信息修改ajax查询
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/selectjbaudit")
    @ResponseBody
    public Json selectJbaudit(HttpServletRequest request){
        Json json=new Json();
        Page page=new Page();
        //获取查询条件
        String companyName=request.getParameter("companyName");
        String uid=request.getParameter("uid");
        boolean ifselectuser=false;
        UserDTO userDTO=new UserDTO();
        if(companyName!=null&&!"".equals(companyName)){
            ifselectuser=true;
            userDTO.setCompanyName(companyName);
        }
        if(uid!=null&&!"".equals(uid)){
            userDTO.setUid(new Long(uid));
            ifselectuser=true;
        }
        List<UserDTO> listuserDto=null;
        //如果为true标示需要再查询审核shop
        boolean ifselectshop=true;
        Long[] shopids=null;
        if(ifselectuser){
            Pager pager1=new Pager();
            pager1.setPage(1);
            pager1.setRows(Integer.MAX_VALUE);
            DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO,null,pager1);
            if(userDTODataGrid!=null){
                listuserDto=userDTODataGrid.getRows();
                if(listuserDto!=null&&listuserDto.size()>0){
                    Iterator<UserDTO> iterator=listuserDto.iterator();
                    Map<Long,Long> mapus=new HashMap<Long, Long>();
                    while(iterator.hasNext()){
                        UserDTO userDTO1=iterator.next();
                        if(userDTO1.getShopId()!=null){
                            mapus.put(userDTO1.getShopId(),userDTO1.getShopId());
                        }
                    }
                    if(mapus.size()>0){
                        shopids=new Long[mapus.size()];
                        Iterator<Long> iterator1=mapus.keySet().iterator();
                        int i=0;
                        while(iterator1.hasNext()){
                            shopids[i]=iterator1.next();
                            i++;
                        }
                    }else{
                        ifselectshop=false;
                    }
                }else{
                    ifselectshop=false;
                }
            }else{
                ifselectshop=false;
            }

        }
        List<ShopModifyInfoDTO> listshop=null;
        if(ifselectshop){
            Pager pager=new Pager();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            ShopModifyInfoDTO shopModifyInfoDTO=new ShopModifyInfoDTO();
            if(shopids!=null&&shopids.length>0){
                shopModifyInfoDTO.setShopIds(shopids);
            }
            //审核信息查询
            ExecuteResult<DataGrid<ShopModifyInfoDTO>> executeResult=shopModifyInfoExportService.queryShopModifyInfo(shopModifyInfoDTO,pager);
            DataGrid<ShopModifyInfoDTO> dataGrid=executeResult.getResult();
            if(dataGrid!=null){
                page.setCount(dataGrid.getTotal());
                listshop=dataGrid.getRows();
            }else{
                page.setCount(0);
            }
        }else{
            page.setCount(0);
        }
        //页面大小
        page.setPageNo(new Integer(request.getParameter("page").toString()));
        page.setPageSize(new Integer(request.getParameter("rows").toString()));
        if(listshop!=null&&listshop.size()>0&&ifselectuser==false){
            Iterator<ShopModifyInfoDTO> iterator=listshop.iterator();
            Map<Long,Long> shopidMap=new HashMap<Long, Long>();
            while(iterator.hasNext()){
                ShopModifyInfoDTO shopCategoryDTO1=iterator.next();
                if(shopCategoryDTO1.getShopId()!=null){
                    shopidMap.put(shopCategoryDTO1.getShopId(),shopCategoryDTO1.getShopId());
                }
            }
            if(shopidMap.size()>0){
                Long[] shipids=new Long[shopidMap.size()];
                Iterator<Long> iterator1=shopidMap.keySet().iterator();
                int i=0;
                while(iterator1.hasNext()){
                    shipids[i]=iterator1.next();
                    i++;
                }
                UserDTO userDTO1=new UserDTO();
                //将店铺的shopids放于用户的查询条件中，查询出用户的数据
                userDTO1.setShopIds(shipids);
                Pager pager1=new Pager();
                pager1.setPage(1);
                pager1.setRows(Integer.MAX_VALUE);
                DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO1, null,pager1);
                listuserDto=userDTODataGrid.getRows();
            }
        }
        //首先查询出需要审核的店铺信息，如果有数据则根据店铺的shopid去查询用户信息，再将两者的信息拼接成列表数据用于前台显示
        page.setList(listDtoTolistMapJb(listshop,listuserDto));
        json.setMsg(page.toString());
        json.setObj(page);
        json.setSuccess(true);
        return json;
    }
    /**
     *
     * @author - 将用户和店铺的信息封装到一个map集合中，同时转化时间类型
     * @message- 店铺信息审核--新类目申请审核明细页面
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,String>> listDtoTolistMapJb(List<ShopModifyInfoDTO> listShop,List<UserDTO> listUserDto){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(listShop!=null&&listShop.size()>0){
            Map<Long,Map<Long,Map<String,String>>> userMap=new HashMap<Long, Map<Long, Map<String, String>>>();
            if(listUserDto!=null&&listUserDto.size()>0){
                Iterator<UserDTO> iterator=listUserDto.iterator();
                while(iterator.hasNext()){
                    UserDTO userDTO=iterator.next();
                    if(userDTO.getShopId()!=null&&(userDTO.getParentId()==null||userDTO.getParentId().equals(1L))){
                        //查看shipid是否已经存了 用户信息
                        Map<Long,Map<String,String>> map=userMap.get(userDTO.getShopId());
                        if(map!=null){
                            //查看店铺对应的此用户信息是否已经存在里面
                            Map<String,String> map1=map.get(userDTO.getUid());
                            if(map1!=null){
                                map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                                map1.put("companyname",userDTO.getCompanyName());
                            }else{
                                map1=new HashMap<String, String>();
                                map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                                map1.put("companyname",userDTO.getCompanyName());
                                map.put(userDTO.getUid(),map1);
                            }
                        }else{
                            map=new HashMap<Long, Map<String, String>>();
                            Map<String,String> map1=new HashMap<String, String>();
                            map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                            map1.put("companyname",userDTO.getCompanyName());
                            map.put(userDTO.getUid(),map1);
                            userMap.put(userDTO.getShopId(),map);
                        }
                    }
                }
            }
            Iterator<ShopModifyInfoDTO> iterator=listShop.iterator();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            while(iterator.hasNext()){
                Map<String,String> shopMap=new HashMap<String, String>();
                ShopModifyInfoDTO shopCategoryDTO=iterator.next();
                if(shopCategoryDTO.getShopId()!=null){
                    //商铺id
                    shopMap.put("shopid",shopCategoryDTO.getShopId().toString());
                    if(shopCategoryDTO.getCreateTime()!=null){
                        //提交时间
                        shopMap.put("sqdate",simpleDateFormat.format(shopCategoryDTO.getCreateTime()));
                    }
                    shopMap.put("id",shopCategoryDTO.getId()!=null?shopCategoryDTO.getId().toString():"");
                    Map<Long,Map<String,String>> map=userMap.get(shopCategoryDTO.getShopId());
                    if(map!=null&&map.size()>0){
                        //防止出现一个shop店铺对应多个用户的情况，所以需要便利店铺对应的所有用户，生成多行数据(如不需要只取第一条就行)
                        Iterator<Map.Entry<Long,Map<String,String>>> entrys=map.entrySet().iterator();
                        int i=0;
                        while(entrys.hasNext()){
                            Map.Entry<Long,Map<String,String>> entry=entrys.next();
                            Map<String,String> map1=entry.getValue();
                            if(i==0){
                                shopMap.put("uid",map1.get("uid"));
                                shopMap.put("companyname",map1.get("companyname"));
                                listMap.add(shopMap);
                            }else{
                                Map<String,String> shopMap1=new HashMap<String, String>();
                                //商铺id
                                shopMap1.put("shopid",shopCategoryDTO.getShopId().toString());
                                if(shopCategoryDTO.getCreateTime()!=null){
                                    //提交时间
                                    shopMap1.put("sqdate",simpleDateFormat.format(shopCategoryDTO.getCreateTime()));
                                }
                                shopMap1.put("uid",map1.get("uid"));
                                shopMap1.put("companyname",map1.get("companyname"));
                                shopMap1.put("id",shopCategoryDTO.getId()!=null?shopCategoryDTO.getId().toString():"");
                                listMap.add(shopMap1);
                            }
                            i++;
                        }
                    }else{
                        listMap.add(shopMap);
                    }
                }
            }
        }
        return listMap;
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--店铺基本信息变更审核
     * @createDate - 2015-3-10
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/shopauditdetailjb")
    public ModelAndView shopAuditdetailJB(HttpServletRequest request){
        String uid=request.getParameter("uid");
        String company=request.getParameter("company");
        String ifoid=request.getParameter("id");
        String shopid=request.getParameter("shopid");
        Map<String,Object> detailJb=new HashMap<String, Object>();
        detailJb.put("uid",uid);
        detailJb.put("company",company);
        detailJb.put("id",ifoid);
        detailJb.put("shopid",shopid);
        if(ifoid!=null&&!"".equals(ifoid)){
            ShopModifyDetailDTO shopModifyDetailDTO=new ShopModifyDetailDTO();
            shopModifyDetailDTO.setModifyInfoId(new Long(ifoid));
            Pager pager=new Pager();
            pager.setPage(1);
            pager.setRows(Integer.MAX_VALUE);
            ExecuteResult<DataGrid<ShopModifyDetailDTO>> executeResult= shopModifyDetailExportService.queryShopModifyDetail(shopModifyDetailDTO, pager);
            DataGrid<ShopModifyDetailDTO> dataGrid=executeResult.getResult();
            if(dataGrid!=null){
                List<ShopModifyDetailDTO> listdetial=dataGrid.getRows();
                if(listdetial!=null&&listdetial.size()>0){
                    Iterator<ShopModifyDetailDTO> iterator=listdetial.iterator();
                    List<Map<String,String>> list=new ArrayList<Map<String, String>>();
                    ShopModify.ShopModifyColumn[] columns= ShopModify.ShopModifyColumn.values();
                    Map<String,String> columMaps=new HashMap<String, String>();
                    for(ShopModify.ShopModifyColumn column:columns){
                        columMaps.put(column.name(),column.getLabel());
                    }
                    columMaps.put("pcdCombin","地址修改");
                    Map<String,String> ifaddA=ifaddA();
                    Map<String,Map<String,String>> zyMap=shopZy();
                    while(iterator.hasNext()){
                        Map<String,String> maptr=new HashMap<String, String>();
                        ShopModifyDetailDTO shopModifyDetailDTO1=iterator.next();
                        //修改的列
                        String columnstr=shopModifyDetailDTO1.getPropertiesColumn();
                        Map<String,String> shopzyMap=null;
                        if(columnstr!=null&&!"".equals(columnstr)){
                            maptr.put("columName",columMaps.get(columnstr));
                            shopzyMap=zyMap.get(columnstr);
                        }
                        if("pcdCombin".equals(columnstr)){
                            //修改前的数据
                            String beforeChange=shopModifyDetailDTO1.getBeforeChange();
                            maptr.put("bc",changeString(beforeChange));
                            //修改后的数据
                            String afterChange=shopModifyDetailDTO1.getAfterChange();
                            maptr.put("ac",changeString(afterChange));
                        }else if("mutilPrice".equals(columnstr)){
                            //修改前的数据
                            String beforeChange=shopModifyDetailDTO1.getBeforeChange();
                            //修改后的数据
                            String afterChange=shopModifyDetailDTO1.getAfterChange();
                            if("1".equals(beforeChange)){
                                maptr.put("bc","混批");
                            }else if("2".equals(beforeChange)){
                                maptr.put("bc","不混批");
                            }/*else if(beforeChange!=null&&!"".equals(beforeChange)){
                                maptr.put("bc","无法识别是否混批");
                            }*/
                            if("1".equals(afterChange)){
                                maptr.put("ac","混批");
                            }else if("2".equals(afterChange)){
                                maptr.put("ac","不混批");
                            }/*else if(afterChange!=null&&!"".equals(afterChange)){
                                maptr.put("ac","无法识别是否混批");
                            }*/
                        }else if(shopzyMap!=null){
                            String bc=shopzyMap.get(shopModifyDetailDTO1.getBeforeChange());
                            String ac=shopzyMap.get(shopModifyDetailDTO1.getAfterChange());
                            maptr.put("bc",bc);
                            maptr.put("ac",ac);
                        }else{
                            String logol=ifaddA.get(columnstr);
                            //修改前的数据
                            String beforeChange=shopModifyDetailDTO1.getBeforeChange();
                            if(beforeChange!=null&&!"".equals(beforeChange)){
                                if(logol!=null&&!"".equals(logol)){
                                    beforeChange= SysProperties.getProperty("ngIp")+beforeChange;
                                }
                                int www= beforeChange.indexOf("www");
                                int http=beforeChange.indexOf("http");
                                int ftp=beforeChange.indexOf("ftp");
                                int rar = beforeChange.indexOf(".rar");
                                int zip = beforeChange.indexOf(".zip");
                                int z = beforeChange.indexOf(".7z");
                                if(www>=0||http>=0||ftp>=0){
                                	if(rar>=0||zip>=0||z>=0){
                                      beforeChange="<a href=\""+beforeChange+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+beforeChange+"</a>";
                                	}else{
                                      beforeChange = "<img src=\""+beforeChange+"\" id=\"bannerUrlImg\" alt=\"\"  class=\"mar_lr10 fl showimg\" style=\"cursor: pointer; opacity: 1;width:150px;height:40px;\">";
                                	}
                                }
                                maptr.put("bc",beforeChange);
                            }
                            //修改后的数据
                            String afterChange=shopModifyDetailDTO1.getAfterChange();
                            if(afterChange!=null&&!"".equals(afterChange)){
                                if(logol!=null&&!"".equals(logol)){
                                    afterChange=SysProperties.getProperty("ngIp")+afterChange;
                                }
                                int www= afterChange.indexOf("www");
                                int http=afterChange.indexOf("http");
                                int ftp=afterChange.indexOf("ftp");
                                int rar = afterChange.indexOf(".rar");
                                int zip = afterChange.indexOf(".zip");
                                int z = afterChange.indexOf(".7z");
                                if(www>=0||http>=0||ftp>=0){
                                	if(rar>=0||zip>=0||z>=0){
                                		afterChange="<a href=\""+afterChange+"\" target=\"_Blank\" class=\"z-link01\" style=\"margin:2px 0;display: block;\" >"+afterChange+"</a>";
                                  	}else{
                                  		afterChange = "<img src=\""+afterChange+"\" id=\"bannerUrlImg\" alt=\"\"  class=\"mar_lr10 fl showimg\" style=\"cursor: pointer; opacity: 1;width:150px;height:40px;\">";
                                  	}
                                }
                                maptr.put("ac",afterChange);
                            }

                        }
                        list.add(maptr);
                    }
                    detailJb.put("list",list);
                }
            }
        }
        return new ModelAndView("/sellercenter/informationchangeaudit/shopauditdetailjb","detailJb",detailJb);
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--店铺基本信息变更审核修改审核状态
     * @createDate - 2015-3-10
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/auditjb")
    @ResponseBody
    public Json auditJb(HttpServletRequest request){
        Json json=new Json();
        //状态
        String auditStatic=request.getParameter("stace");
        //店铺id
        String shopid=request.getParameter("shopid");
        //描述
        String ms=request.getParameter("ms");
        //当前登录人
        String idd=UserUtils.getUser().getId();
        //info的id
        ShopModifyInfoDTO shopModifyInfoDTO=new ShopModifyInfoDTO();
        String id=request.getParameter("id");
        try{
            if("1".equals(auditStatic)){
                //modify的id
                if(id==null||"".equals(id)){
                    json.setSuccess(false);
                    json.setMsg("后台获取的modif的id为空，请联系管理员");
                    return json;
                }
                shopModifyInfoDTO.setId(new Long(id));
                //店铺id
                if(shopid==null||"".equals(shopid)){
                    json.setSuccess(false);
                    json.setMsg("后台获取的店铺的id为空，请联系管理员");
                    return json;
                }
                shopModifyInfoDTO.setShopId(new Long(shopid));
                //审批状态
                shopModifyInfoDTO.setApplyStatus(new Integer(auditStatic));
                ExecuteResult<String> executeResult=shopModifyInfoExportService.modifyShopModifyStatus(shopModifyInfoDTO);
                String resultMsg=executeResult.getResultMessage();
                if("success".equals(resultMsg)){
/*                    ExecuteResult<ShopDTO> execute=shopExportService.findShopInfoById(new Long(shopid));
                    if(execute!=null&&execute.getResult()!=null){
                        ShopDTO shopDTO=execute.getResult();
                        ShopInfoModifyInDTO shopInfoModifyInDTO=new ShopInfoModifyInDTO();
                        shopDTO.setComment(ms);
                        shopInfoModifyInDTO.setShopDTO(shopDTO);
                        shopExportService.modifyShopInfoUpdate(shopInfoModifyInDTO);
                    }*/
                    json.setSuccess(true);
                    json.setMsg("审核完成");
                }else{
                    json.setSuccess(false);
                    json.setMsg("审核出现异常无法完成审核："+resultMsg);
                }
            }else if("2".equals(auditStatic)){
                //modify的id
                if(id==null||"".equals(id)){
                    json.setSuccess(false);
                    json.setMsg("后台获取的modif的id为空，请联系管理员");
                    return json;
                }
                shopModifyInfoDTO.setId(new Long(id));
                //店铺id
                if(shopid==null||"".equals(shopid)){
                    json.setSuccess(false);
                    json.setMsg("后台获取的店铺的id为空，请联系管理员");
                    return json;
                }
                shopModifyInfoDTO.setShopId(new Long(shopid));
                //审批状态
                shopModifyInfoDTO.setApplyStatus(new Integer(auditStatic));
                ExecuteResult<String> executeResult=shopModifyInfoExportService.modifyShopModifyStatus(shopModifyInfoDTO);
                String resultMsg=executeResult.getResultMessage();
                if("success".equals(resultMsg)){
                    ExecuteResult<ShopDTO> execute=shopExportService.findShopInfoById(new Long(shopid));
                    if(execute!=null&&execute.getResult()!=null){
                        ShopDTO shopDTO=execute.getResult();
                        ShopInfoModifyInDTO shopInfoModifyInDTO=new ShopInfoModifyInDTO();
                        shopDTO.setComment(ms);
                        shopInfoModifyInDTO.setShopDTO(shopDTO);
                        shopExportService.modifyShopInfoUpdate(shopInfoModifyInDTO);
                    }
                    json.setSuccess(true);
                    json.setMsg("审核完成");
                }else{
                    json.setSuccess(false);
                    json.setMsg("审核出现异常无法完成审核："+resultMsg);
                }
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            logger.error(e.toString());
            json.setSuccess(false);
            json.setMsg("审核时出现意外错误，请联系运维人员"+e.getMessage());
        }
        return json;
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--如果是json则将json踹封装
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private String changeString(String change){
        String zhi="";
        if(change!=null&&!"".equals(change)){
                try{
                    JSONObject jsonObject=JSON.parseObject(change);
                    //省
                    if(jsonObject.get("provinceName")!=null){
                        zhi=jsonObject.get("provinceName").toString();
                    }
                    //市
                    if(jsonObject.get("cityName")!=null){
                        zhi=zhi+jsonObject.get("cityName").toString();
                    }
                    //区
                    if(jsonObject.get("districtName")!=null){
                        zhi=zhi+jsonObject.get("districtName");
                    }
                }catch(Exception e){
                    logger.error(e.getMessage());
                    logger.error(e.toString());
                    //e.printStackTrace();
                     zhi=change;
                }
            }
        return zhi;
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--新类目申请
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/shopauditselectlm")
    public ModelAndView shopAuditSelectLM(){
        Page page=new Page();
        page.setPageNo(1);
        page.setPageSize(10);
        ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
        //状态为申请
        shopCategoryDTO.setStatus(new Integer(1));
        shopCategoryDTO.setIsGroupBy(1L);
        Pager pager=new Pager();
        pager.setRows(10);
        pager.setPage(1);
        //审核信息查询
        ExecuteResult<DataGrid<ShopCategoryDTO>> executeResult= shopCategoryExportService.queryShopCategory(shopCategoryDTO, pager);
        DataGrid<ShopCategoryDTO> dataGrid=executeResult.getResult();
        Map<String,Object> auditLm=new HashMap<String, Object>();
        if(dataGrid!=null){

            page.setCount(dataGrid.getTotal());
            List<ShopCategoryDTO> list=dataGrid.getRows();
            //用户信息查询
            List<UserDTO> list1=null;
            //首先查询出需要审核的店铺信息，如果有数据则根据店铺的shopid去查询用户信息，再将两者的信息拼接成列表数据用于前台显示
            if(list!=null&&list.size()>0){
                Iterator<ShopCategoryDTO> iterator=list.iterator();
                Map<Long,Long> shopidMap=new HashMap<Long, Long>();
                while(iterator.hasNext()){
                    ShopCategoryDTO shopCategoryDTO1=iterator.next();
                    if(shopCategoryDTO1.getShopId()!=null){
                        shopidMap.put(shopCategoryDTO1.getShopId(),shopCategoryDTO1.getShopId());
                    }
                }
                if(shopidMap.size()>0){
                    Long[] shipids=new Long[shopidMap.size()];
                    Iterator<Long> iterator1=shopidMap.keySet().iterator();
                    int i=0;
                    while(iterator1.hasNext()){
                        shipids[i]=iterator1.next();
                        i++;
                    }
                    UserDTO userDTO=new UserDTO();
                    //将店铺的shopids放于用户的查询条件中，查询出用户的数据
                    userDTO.setShopIds(shipids);
                    Pager pager1=new Pager();
                    pager1.setPage(1);
                    pager1.setRows(Integer.MAX_VALUE);
                    DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO,null,pager1);
                    list1=userDTODataGrid.getRows();
                }
            }
            page.setList(listDtoTolistMap(list, list1));
        }
        auditLm.put("page",page);
        return new ModelAndView("/sellercenter/informationchangeaudit/shopauditselectlm","auditLm",auditLm);
    }
    /**
     * 商家基本信息买家查询分页
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @ResponseBody
    @RequestMapping("/selectlmaudit")
    public Json selectLmAudit(HttpServletRequest request,HttpServletResponse response,Model model){
        Json json=new Json();
        Page page=new Page();
        //获取查询条件
        String companyName=request.getParameter("companyName");
        String uid=request.getParameter("uid");
        boolean ifselectuser=false;
        UserDTO userDTO=new UserDTO();
        if(companyName!=null&&!"".equals(companyName)){
            ifselectuser=true;
            userDTO.setCompanyName(companyName);
        }
        if(uid!=null&&!"".equals(uid)){
            userDTO.setUid(new Long(uid));
            ifselectuser=true;
        }
        List<UserDTO> listuserDto=null;
        //如果为true标示需要再查询审核shop
        boolean ifselectshop=true;
        Long[] shopids=null;
        if(ifselectuser){
            Pager pager1=new Pager();
            pager1.setPage(1);
            pager1.setRows(Integer.MAX_VALUE);
            DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO,null,pager1);
            listuserDto=userDTODataGrid.getRows();
            if(listuserDto!=null&&listuserDto.size()>0){
                Iterator<UserDTO> iterator=listuserDto.iterator();
                Map<Long,Long> mapus=new HashMap<Long, Long>();
                while(iterator.hasNext()){
                    UserDTO userDTO1=iterator.next();
                    if(userDTO1.getShopId()!=null){
                        mapus.put(userDTO1.getShopId(),userDTO1.getShopId());
                    }
                }
                if(mapus.size()>0){
                    shopids=new Long[mapus.size()];
                    Iterator<Long> iterator1=mapus.keySet().iterator();
                    int i=0;
                    while(iterator1.hasNext()){
                        shopids[i]=iterator1.next();
                        i++;
                    }
                }else{
                    ifselectshop=false;
                }
            }else{
                ifselectshop=false;
            }
        }
        List<ShopCategoryDTO> listshop=null;
        if(ifselectshop){
            Pager pager=new Pager();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
            //状态为申请
            shopCategoryDTO.setStatus(new Integer(1));
            shopCategoryDTO.setIsGroupBy(1L);
            shopCategoryDTO.setShopIds(shopids);
            //审核信息查询
            ExecuteResult<DataGrid<ShopCategoryDTO>> executeResult= shopCategoryExportService.queryShopCategory(shopCategoryDTO, pager);
            DataGrid<ShopCategoryDTO> dataGrid=executeResult.getResult();
            if(dataGrid!=null){
                page.setCount(dataGrid.getTotal());
                listshop=dataGrid.getRows();
            }else{
                page.setCount(0);
            }
        }else{
            page.setCount(0);
        }
        //页面大小
        page.setPageNo(new Integer(request.getParameter("page").toString()));
        page.setPageSize(new Integer(request.getParameter("rows").toString()));
        if(listshop!=null&&listshop.size()>0&&ifselectuser==false){
            Iterator<ShopCategoryDTO> iterator=listshop.iterator();
            Map<Long,Long> shopidMap=new HashMap<Long, Long>();
            while(iterator.hasNext()){
                ShopCategoryDTO shopCategoryDTO1=iterator.next();
                if(shopCategoryDTO1.getShopId()!=null){
                    shopidMap.put(shopCategoryDTO1.getShopId(),shopCategoryDTO1.getShopId());
                }
            }
            if(shopidMap.size()>0){
                Long[] shipids=new Long[shopidMap.size()];
                Iterator<Long> iterator1=shopidMap.keySet().iterator();
                int i=0;
                while(iterator1.hasNext()){
                    shipids[i]=iterator1.next();
                    i++;
                }
                UserDTO userDTO1=new UserDTO();
                //将店铺的shopids放于用户的查询条件中，查询出用户的数据
                userDTO1.setShopIds(shipids);
                Pager pager1=new Pager();
                pager1.setPage(1);
                pager1.setRows(Integer.MAX_VALUE);
                DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO1, null,pager1);
                listuserDto=userDTODataGrid.getRows();
            }
        }
        //首先查询出需要审核的店铺信息，如果有数据则根据店铺的shopid去查询用户信息，再将两者的信息拼接成列表数据用于前台显示
        page.setList(listDtoTolistMap(listshop,listuserDto));
        json.setMsg(page.toString());
        json.setObj(page);
        json.setSuccess(true);
        return json;
    }
    /**
     *
     * @author - 将用户和店铺的信息封装到一个map集合中，同时转化时间类型
     * @message- 店铺信息审核--新类目申请审核明细页面
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,String>> listDtoTolistMap(List<ShopCategoryDTO> listShop,List<UserDTO> listUserDto){
         List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(listShop!=null&&listShop.size()>0){
            Map<Long,Map<Long,Map<String,String>>> userMap=new HashMap<Long, Map<Long, Map<String, String>>>();
            if(listUserDto!=null&&listUserDto.size()>0){
                Iterator<UserDTO> iterator=listUserDto.iterator();
                while(iterator.hasNext()){
                    UserDTO userDTO=iterator.next();
                    if(userDTO.getShopId()!=null&&(userDTO.getParentId()==null||userDTO.getParentId().equals(1L))){
                        //查看shipid是否已经存了 用户信息
                        Map<Long,Map<String,String>> map=userMap.get(userDTO.getShopId());
                        if(map!=null){
                            //查看店铺对应的此用户信息是否已经存在里面
                            Map<String,String> map1=map.get(userDTO.getUid());
                            if(map1!=null){
                                map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                                map1.put("companyname",userDTO.getCompanyName());
                            }else{
                                map1=new HashMap<String, String>();
                                map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                                map1.put("companyname",userDTO.getCompanyName());
                                map.put(userDTO.getUid(),map1);
                            }
                        }else{
                            map=new HashMap<Long, Map<String, String>>();
                            Map<String,String> map1=new HashMap<String, String>();
                            map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                            map1.put("companyname",userDTO.getCompanyName());
                            map.put(userDTO.getUid(),map1);
                            userMap.put(userDTO.getShopId(),map);
                        }
                    }
                }
            }
            Iterator<ShopCategoryDTO> iterator=listShop.iterator();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            while(iterator.hasNext()){
                Map<String,String> shopMap=new HashMap<String, String>();
                ShopCategoryDTO shopCategoryDTO=iterator.next();
                if(shopCategoryDTO.getShopId()!=null){
                    //商铺id
                    shopMap.put("shopid",shopCategoryDTO.getShopId().toString());
                    if(shopCategoryDTO.getCreated()!=null){
                        //提交时间
                        shopMap.put("sqdate",simpleDateFormat.format(shopCategoryDTO.getCreated()));
                    }
                    Map<Long,Map<String,String>> map=userMap.get(shopCategoryDTO.getShopId());
                    if(map!=null&&map.size()>0){
                        //防止出现一个shop店铺对应多个用户的情况，所以需要便利店铺对应的所有用户，生成多行数据(如不需要只取第一条就行)
                        Iterator<Map.Entry<Long,Map<String,String>>> entrys=map.entrySet().iterator();
                        int i=0;
                        while(entrys.hasNext()){
                            Map.Entry<Long,Map<String,String>> entry=entrys.next();
                            Map<String,String> map1=entry.getValue();
                            if(i==0){
                                shopMap.put("uid",map1.get("uid"));
                                shopMap.put("companyname",map1.get("companyname"));
                                listMap.add(shopMap);
                            }else{
                                Map<String,String> shopMap1=new HashMap<String, String>();
                                //商铺id
                                shopMap1.put("shopid",shopCategoryDTO.getShopId().toString());
                                if(shopCategoryDTO.getCreated()!=null){
                                    //提交时间
                                    shopMap1.put("sqdate",simpleDateFormat.format(shopCategoryDTO.getCreated()));
                                }
                                shopMap1.put("uid",map1.get("uid"));
                                shopMap1.put("companyname",map1.get("companyname"));
                                listMap.add(shopMap1);
                            }
                            i++;
                        }
                    }else{
                        listMap.add(shopMap);
                    }
                }
            }
        }
        return listMap;
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--新类目申请审核明细页面
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/shopauditdetaillm")
    public ModelAndView shopAuditDetailLM(HttpServletRequest request){
        Long shopid=new Long(request.getParameter("shopid"));
        Map<String,Object> detailLm=new HashMap<String, Object>();
        detailLm.put("uid",request.getParameter("uid"));
        detailLm.put("company",request.getParameter("company"));
        detailLm.put("shopid",request.getParameter("shopid"));
        ExecuteResult<List<ShopCategoryDTO>> executeResult= shopCategoryExportService.queryShopCategoryList(shopid,new Integer(1));
        List<ShopCategoryDTO> list=executeResult.getResult();
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(list!=null&&list.size()>0){
            //根据类目id获取三级类目的名字
            Map<Long,Long> mapcid=new HashMap<Long, Long>();
            Iterator<ShopCategoryDTO> iterator=list.iterator();
            while(iterator.hasNext()){
                ShopCategoryDTO shopCategoryDTO=iterator.next();
                if(shopCategoryDTO.getCid()!=null){
                    mapcid.put(shopCategoryDTO.getCid(),shopCategoryDTO.getCid());
                }
            }
            Long[] cids=null;
            if(mapcid.size()>0){
               cids=new Long[mapcid.size()];
                Iterator<Long> iterator1=mapcid.keySet().iterator();
                int i=0;
                while(iterator1.hasNext()){
                    cids[i]=iterator1.next();
                    i++;
                }
            }
            Map<Long,Map<String,String>> leiMu=new HashMap<Long, Map<String, String>>();
            if(cids!=null&&cids.length>0){
                leiMu=leiMu(cids);
            }
            Iterator<ShopCategoryDTO> iterator1=list.iterator();
            while(iterator1.hasNext()){
                ShopCategoryDTO shopCategoryDTO=iterator1.next();
                if(shopCategoryDTO.getCid()!=null){
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("shopid",shopCategoryDTO.getShopId().toString());
                    map.put("cid",shopCategoryDTO.getCid()!=null?shopCategoryDTO.getCid().toString():"");
                    map.put("id",shopCategoryDTO.getId()!=null?shopCategoryDTO.getId().toString():"");
                    Map<String,String> map1=leiMu.get(shopCategoryDTO.getCid());
                    if(map1!=null){
                        map.put("sj",map1.get("yn")+">>"+map1.get("bn")+">>"+map1.get("sn"));
                        map.put("sn",map1.get("sn"));
                    }
                    listMap.add(map);
                }
            }
        }
        detailLm.put("listMap",listMap);
        return new ModelAndView("/sellercenter/informationchangeaudit/shopauditdetaillm","detailLm",detailLm);
    }
    /**
     * 店铺类目审核
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/auditlmresult")
    @ResponseBody
    public Json auditLmResult(HttpServletRequest request){
        Json json =new Json();
        try{
            String auditStatic=request.getParameter("static");
            String shopid=request.getParameter("shopid");
            String cid=request.getParameter("cid");
            String ms=request.getParameter("ms");
            String id=UserUtils.getUser().getId();
            String idd=request.getParameter("id");
            ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
            //2为通过
            if("2".equals(auditStatic)){
                shopCategoryDTO.setId(new Long(idd));
                ms="审核通过";
                shopCategoryDTO.setShopId(new Long(shopid));
                shopCategoryDTO.setStatus(new Integer(2));
                shopCategoryDTO.setComment(ms);
                shopCategoryDTO.setCid(new Long(cid));
                //用户id
                shopCategoryDTO.setPlatformUserId(id);
                shopCategoryDTO.setPassTime(new Date());
            }else{
            	
                shopCategoryDTO.setId(new Long(idd));
                shopCategoryDTO.setShopId(new Long(shopid));
                shopCategoryDTO.setStatus(new Integer(3));
                shopCategoryDTO.setComment(ms);
                shopCategoryDTO.setCid(new Long(cid));
                //用户id
                shopCategoryDTO.setPlatformUserId(id);
                shopCategoryDTO.setPassTime(new Date());
            }
            //将被驳回的过期无用的消息的status状态值设置为-1
            deleteOldRejectedshopCategory(idd);
            ExecuteResult<String> executeResult=shopCategoryExportService.modifyShopCategoryStatus(shopCategoryDTO);
            
            String resultmsg=executeResult.getResult();
            if(executeResult.getResult()!=null&&!"".equals(executeResult.getResult())&&new Integer(executeResult.getResult())>0){
                json.setMsg("审核成功");
                json.setSuccess(true);
            }else{
                if(executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
                    for(int i=0;i<executeResult.getErrorMessages().size();i++){
                        logger.error("新增类目审核异常:"+executeResult.getErrorMessages().get(i));
                    }
                    json.setMsg("审核结果异常");
                    json.setSuccess(false);
                }
            }
        }catch(Exception e){
            logger.error("新增类目审核异常:"+e.getMessage());
            json.setMsg("系统繁忙，请稍后再试");
            json.setSuccess(false);
        }
        return json;
    }
    /**
     * 将被驳回的过期无用的消息的status状态值设置为-1
     * @author - 江鹏
     * @createDate - 2015-7-29
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	private void deleteOldRejectedshopCategory(String idd) {
		List<ShopCategoryDTO> shopCategoryDTOslist = new ArrayList<ShopCategoryDTO>();
		shopCategoryDTOslist = shopCategoryExportService.selectShopIdById(new Long(idd));
		int shopCategoryDTOslistsize = shopCategoryDTOslist.size();
		if (shopCategoryDTOslistsize > 0) {
			Long shopId = shopCategoryDTOslist.get(0).getShopId();
			shopCategoryExportService.updateStatusByIdAndShopId(shopId);
		}
	}
    /**
     * 获取并封装店铺的三级类目
     * @author - 门光耀
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private Map<Long,Map<String,String>> leiMu(Long[] cids){
        //封装类目的map
        Map<Long,Map<String,String>> lmmaps=new HashMap<Long, Map<String, String>>();
                ExecuteResult<List<ItemCatCascadeDTO>> result =itemCategoryService.queryParentCategoryList(cids);
                List<ItemCatCascadeDTO> listcasedto=result.getResult();
                if(listcasedto!=null&&listcasedto.size()>0) {
                    //一级类目便利 bn/
                    Iterator<ItemCatCascadeDTO> iterator2 = listcasedto.iterator();
                    while (iterator2.hasNext()) {
                        ItemCatCascadeDTO item1 = iterator2.next();
                        if (item1.getChildCats() != null && item1.getChildCats().size() > 0) {
                            //二级类目遍历
                            Iterator<ItemCatCascadeDTO> it3 = item1.getChildCats().iterator();
                            while (it3.hasNext()) {
                                ItemCatCascadeDTO item2 = it3.next();
                                if (item2.getChildCats() != null && item2.getChildCats().size() > 0) {
                                    //三级类目遍历
                                    Iterator<ItemCatCascadeDTO> it4 = item2.getChildCats().iterator();
                                    while (it4.hasNext()) {
                                        ItemCatCascadeDTO item3 = it4.next();
                                        Map<String, String> map3 = new HashMap<String, String>();
                                        //一级类目的code和name
                                        map3.put("yc", item1.getCid().toString());
                                        map3.put("yn", item1.getCname());
                                        //二级类目的code和name
                                        map3.put("bc", item2.getCid().toString());
                                        map3.put("bn", item2.getCname());
                                        //三级类目的code和name
                                        map3.put("sc", item3.getCid().toString());
                                        map3.put("sn", item3.getCname());
                                        lmmaps.put(item3.getCid(), map3);
                                    }
                                }
                            }
                        }
                    }
                }
        return lmmaps;
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--新类目申请
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/shopauditselectpp")
    public ModelAndView shopAuditSelectPP(){
        Page page=new Page();
        page.setPageNo(1);
        page.setPageSize(10);
        ShopBrandDTO shopCategoryDTO=new ShopBrandDTO();
        //状态为申请
        shopCategoryDTO.setStatus(new Integer(1));
        shopCategoryDTO.setIsGroupBy(1L);
        Pager pager=new Pager();
        pager.setRows(10);
        pager.setPage(1);
        //审核信息查询
        ExecuteResult<DataGrid<ShopBrandDTO>> executeResult= shopBrandExportService.queryShopBrand(shopCategoryDTO, pager);

        DataGrid<ShopBrandDTO> dataGrid=executeResult.getResult();
        if(dataGrid!=null){
            Map<String,Object> auditLm=new HashMap<String, Object>();
            auditLm.put("page",page);
            page.setCount(dataGrid.getTotal());
            List<ShopBrandDTO> list=dataGrid.getRows();
            //用户信息查询
            List<UserDTO> list1=null;
            //首先查询出需要审核的店铺信息，如果有数据则根据店铺的shopid去查询用户信息，再将两者的信息拼接成列表数据用于前台显示
            if(list!=null&&list.size()>0){
                Iterator<ShopBrandDTO> iterator=list.iterator();
                Map<Long,Long> shopidMap=new HashMap<Long, Long>();
                while(iterator.hasNext()){
                    ShopBrandDTO shopCategoryDTO1=iterator.next();
                    if(shopCategoryDTO1.getShopId()!=null){
                        shopidMap.put(shopCategoryDTO1.getShopId(),shopCategoryDTO1.getShopId());
                    }
                }
                if(shopidMap.size()>0){
                    Long[] shipids=new Long[shopidMap.size()];
                    Iterator<Long> iterator1=shopidMap.keySet().iterator();
                    int i=0;
                    while(iterator1.hasNext()){
                        shipids[i]=iterator1.next();
                        i++;
                    }
                    UserDTO userDTO=new UserDTO();
                    //将店铺的shopids放于用户的查询条件中，查询出用户的数据
                    userDTO.setShopIds(shipids);
                    Pager pager1=new Pager();
                    pager1.setPage(1);
                    pager1.setRows(Integer.MAX_VALUE);
                    DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO,null,pager1);
                    list1=userDTODataGrid.getRows();
                }
            }
            page.setList(listDtoTolistMapPP(list, list1));
        }

        return new ModelAndView("/sellercenter/informationchangeaudit/shopauditselectpp","page",page);
    }
    /**
     *
     * @author - 将用户和店铺的信息封装到一个map集合中，同时转化时间类型
     * @message- 店铺信息审核--新品牌审核ajax查询
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/selectpp")
    @ResponseBody
    public Json selectPP(HttpServletRequest request){
        Json json=new Json();
        Page page=new Page();
        //获取查询条件
        String companyName=request.getParameter("companyName");
        String uid=request.getParameter("uid");
        boolean ifselectuser=false;
        UserDTO userDTO=new UserDTO();
        if(companyName!=null&&!"".equals(companyName)){
            ifselectuser=true;
            userDTO.setCompanyName(companyName);
        }
        if(uid!=null&&!"".equals(uid)){
            userDTO.setUid(new Long(uid));
            ifselectuser=true;
        }
        List<UserDTO> listuserDto=null;
        //如果为true标示需要再查询审核shop
        boolean ifselectshop=true;
        Long[] shopids=null;
        if(ifselectuser){
            Pager pager1=new Pager();
            pager1.setPage(1);
            pager1.setRows(Integer.MAX_VALUE);
            DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO,null,pager1);
            listuserDto=userDTODataGrid.getRows();
            if(listuserDto!=null&&listuserDto.size()>0){
                Iterator<UserDTO> iterator=listuserDto.iterator();
                Map<Long,Long> mapus=new HashMap<Long, Long>();
                while(iterator.hasNext()){
                    UserDTO userDTO1=iterator.next();
                    if(userDTO1.getShopId()!=null){
                        mapus.put(userDTO1.getShopId(),userDTO1.getShopId());
                    }
                }
                if(mapus.size()>0){
                    shopids=new Long[mapus.size()];
                    Iterator<Long> iterator1=mapus.keySet().iterator();
                    int i=0;
                    while(iterator1.hasNext()){
                        shopids[i]=iterator1.next();
                        i++;
                    }
                }else{
                    ifselectshop=false;
                }
            }else{
                ifselectshop=false;
            }
        }
        List<ShopBrandDTO> listshop=null;
        if(ifselectshop){
            Pager pager=new Pager();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            ShopBrandDTO shopCategoryDTO=new ShopBrandDTO();
            //状态为申请
            shopCategoryDTO.setStatus(new Integer(1));
            shopCategoryDTO.setIsGroupBy(1L);
            shopCategoryDTO.setShopIds(shopids);
            //审核信息查询
            ExecuteResult<DataGrid<ShopBrandDTO>> executeResult= shopBrandExportService.queryShopBrand(shopCategoryDTO, pager);
            DataGrid<ShopBrandDTO> dataGrid=executeResult.getResult();
            if(dataGrid!=null){
                page.setCount(dataGrid.getTotal());
                listshop=dataGrid.getRows();
            }else{
                page.setCount(0);
            }
        }else{
            page.setCount(0);
        }
        //页面大小
        page.setPageNo(new Integer(request.getParameter("page").toString()));
        page.setPageSize(new Integer(request.getParameter("rows").toString()));
        if(listshop!=null&&listshop.size()>0&&ifselectuser==false){
            Iterator<ShopBrandDTO> iterator=listshop.iterator();
            Map<Long,Long> shopidMap=new HashMap<Long, Long>();
            while(iterator.hasNext()){
                ShopBrandDTO shopCategoryDTO1=iterator.next();
                if(shopCategoryDTO1.getShopId()!=null){
                    shopidMap.put(shopCategoryDTO1.getShopId(),shopCategoryDTO1.getShopId());
                }
            }
            if(shopidMap.size()>0){
                Long[] shipids=new Long[shopidMap.size()];
                Iterator<Long> iterator1=shopidMap.keySet().iterator();
                int i=0;
                while(iterator1.hasNext()){
                    shipids[i]=iterator1.next();
                    i++;
                }
                UserDTO userDTO1=new UserDTO();
                //将店铺的shopids放于用户的查询条件中，查询出用户的数据
                userDTO1.setShopIds(shipids);
                Pager pager1=new Pager();
                pager1.setPage(1);
                pager1.setRows(Integer.MAX_VALUE);
                DataGrid<UserDTO> userDTODataGrid=userExportService.findUserListByCondition(userDTO1, null,pager1);
                listuserDto=userDTODataGrid.getRows();
            }
        }
        //首先查询出需要审核的店铺信息，如果有数据则根据店铺的shopid去查询用户信息，再将两者的信息拼接成列表数据用于前台显示
        page.setList(listDtoTolistMapPP(listshop,listuserDto));
        json.setMsg(page.toString());
        json.setObj(page);
        json.setSuccess(true);
        return json;
    }
    /**
     *
     * @author - 将用户和店铺的信息封装到一个map集合中，同时转化时间类型
     * @message- 店铺信息审核--新类目申请审核明细页面
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private List<Map<String,String>> listDtoTolistMapPP(List<ShopBrandDTO> listShop,List<UserDTO> listUserDto){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(listShop!=null&&listShop.size()>0){
            Map<Long,Map<Long,Map<String,String>>> userMap=new HashMap<Long, Map<Long, Map<String, String>>>();
            if(listUserDto!=null&&listUserDto.size()>0){
                Iterator<UserDTO> iterator=listUserDto.iterator();
                while(iterator.hasNext()){
                    UserDTO userDTO=iterator.next();
                    if(userDTO.getShopId()!=null&&(userDTO.getParentId()==null||userDTO.getParentId().equals(1L))){
                        //查看shipid是否已经存了 用户信息
                        Map<Long,Map<String,String>> map=userMap.get(userDTO.getShopId());
                        if(map!=null){
                            //查看店铺对应的此用户信息是否已经存在里面
                            Map<String,String> map1=map.get(userDTO.getUid());
                            if(map1!=null){
                                map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                                map1.put("companyname",userDTO.getCompanyName());
                            }else{
                                map1=new HashMap<String, String>();
                                map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                                map1.put("companyname",userDTO.getCompanyName());
                                map.put(userDTO.getUid(),map1);
                            }
                        }else{
                            map=new HashMap<Long, Map<String, String>>();
                            Map<String,String> map1=new HashMap<String, String>();
                            map1.put("uid", userDTO.getUid()!=null?userDTO.getUid().toString():"");
                            map1.put("companyname",userDTO.getCompanyName());
                            map.put(userDTO.getUid(),map1);
                            userMap.put(userDTO.getShopId(),map);
                        }
                    }
                }
            }
            Iterator<ShopBrandDTO> iterator=listShop.iterator();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            while(iterator.hasNext()){
                Map<String,String> shopMap=new HashMap<String, String>();
                ShopBrandDTO shopCategoryDTO=iterator.next();
                if(shopCategoryDTO.getShopId()!=null){
                    //商铺id
                    shopMap.put("shopid",shopCategoryDTO.getShopId().toString());
                    if(shopCategoryDTO.getCreated()!=null){
                        //提交时间
                        shopMap.put("sqdate",simpleDateFormat.format(shopCategoryDTO.getCreated()));
                    }
                    Map<Long,Map<String,String>> map=userMap.get(shopCategoryDTO.getShopId());
                    if(map!=null&&map.size()>0){
                        //防止出现一个shop店铺对应多个用户的情况，所以需要便利店铺对应的所有用户，生成多行数据(如不需要只取第一条就行)
                        Iterator<Map.Entry<Long,Map<String,String>>> entrys=map.entrySet().iterator();
                        int i=0;
                        while(entrys.hasNext()){
                            Map.Entry<Long,Map<String,String>> entry=entrys.next();
                            Map<String,String> map1=entry.getValue();
                            if(i==0){
                                shopMap.put("uid",map1.get("uid"));
                                shopMap.put("companyname",map1.get("companyname"));
                                listMap.add(shopMap);
                            }else{
                                Map<String,String> shopMap1=new HashMap<String, String>();
                                //商铺id
                                shopMap1.put("shopid",shopCategoryDTO.getShopId().toString());
                                if(shopCategoryDTO.getCreated()!=null){
                                    //提交时间
                                    shopMap1.put("sqdate",simpleDateFormat.format(shopCategoryDTO.getCreated()));
                                }
                                shopMap1.put("uid",map1.get("uid"));
                                shopMap1.put("companyname",map1.get("companyname"));
                                listMap.add(shopMap1);
                            }
                            i++;
                        }
                    }else{
                        listMap.add(shopMap);
                    }
                }
            }
        }
        return listMap;
    }
    /**
     *
     * @author - 门光耀
     * @message- 店铺信息审核--新申请审核明细页面
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/shopauditdetailpp")
    public ModelAndView shopAuditDetailPP(HttpServletRequest request){
        Map<String,Object> detailPp=new HashMap<String, Object>();
        Long shopid=new Long(request.getParameter("shopid"));
        detailPp.put("uid",request.getParameter("uid"));
        detailPp.put("company",request.getParameter("company"));
        detailPp.put("shopid",request.getParameter("shopid"));
        ExecuteResult<List<ShopBrandDTO>>  executeResult= shopBrandExportService .queryShopBrandList(shopid,new Integer(1));
        List<ShopBrandDTO> list=executeResult.getResult();
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(list!=null&&list.size()>0){
            //根据类目id获取三级类目的名字
            Map<Long,Long> mapcid=new HashMap<Long, Long>();
            Iterator<ShopBrandDTO> iterator2=list.iterator();
            while(iterator2.hasNext()){
                ShopBrandDTO shopBrandDTO=iterator2.next();
                if(shopBrandDTO.getCid()!=null){
                    mapcid.put(shopBrandDTO.getCid(),shopBrandDTO.getCid());
                }
            }
            Long[] cids=null;
            if(mapcid.size()>0){
                cids=new Long[mapcid.size()];
                Iterator<Long> iterator1=mapcid.keySet().iterator();
                int i=0;
                while(iterator1.hasNext()){
                    cids[i]=iterator1.next();
                    i++;
                }
            }
            Map<Long,Map<String,String>> leiMu=new HashMap<Long, Map<String, String>>();
            if(cids!=null&&cids.length>0){
                leiMu=leiMu(cids);
            }
            //根据品牌id获取牌品信息
            Map<Long,Long> mapbrandids=new HashMap<Long, Long>();
            Iterator<ShopBrandDTO> iterator=list.iterator();
            while(iterator.hasNext()){
                ShopBrandDTO shopCategoryDTO=iterator.next();
                if(shopCategoryDTO.getBrandId()!=null){
                    mapbrandids.put(shopCategoryDTO.getBrandId(),shopCategoryDTO.getBrandId());
                }
            }
            Long[] brandids=null;
            if(mapbrandids.size()>0){
                brandids=new Long[mapbrandids.size()];
                Iterator<Long> iterator1=mapbrandids.keySet().iterator();
                int i=0;
                while(iterator1.hasNext()){
                    brandids[i]=iterator1.next();
                    i++;
                }
            }
            Map<Long,String> brnadName=new HashMap<Long,String>();
            if(brandids!=null&&brandids.length>0){
                brnadName=selectBrandmap(brandids);
            }
            Iterator<ShopBrandDTO> iterator1=list.iterator();
            while(iterator1.hasNext()){
                ShopBrandDTO shopCategoryDTO=iterator1.next();
                Map<String,String> map=new HashMap<String, String>();
                map.put("shopid",shopCategoryDTO.getShopId().toString());
                if(shopCategoryDTO.getBrandId()!=null){
                    map.put("brandid",String.valueOf(shopCategoryDTO.getBrandId()));
                    map.put("brandName",brnadName.get(shopCategoryDTO.getBrandId()));
                }
                map.put("id",shopCategoryDTO.getId()!=null?shopCategoryDTO.getId().toString():"");
                Map<String,String> map1=leiMu.get(shopCategoryDTO.getCid());
                if(map1!=null){
                    map.put("sj",map1.get("yn")+">>"+map1.get("bn")+">>"+map1.get("sn"));
                    map.put("sn",map1.get("sn"));
                }
                listMap.add(map);
            }
        }
        detailPp.put("listMap",listMap);
        return new ModelAndView("/sellercenter/informationchangeaudit/shopauditdetailpp","detailPp",detailPp);
    }
    private Map<Long,String> selectBrandmap(Long[] brandids){
        Map<Long,String> ppMap=new HashMap<Long, String>();
        if(brandids!=null&&brandids.length>0){
            ExecuteResult<List<ItemBrandDTO>> executeResult= itemBrandExportService.queryItemBrandByIds(brandids);
            List<ItemBrandDTO> itemBrandDTOs=executeResult.getResult();
            if(itemBrandDTOs!=null&&itemBrandDTOs.size()>0){
                Iterator<ItemBrandDTO> iterator=itemBrandDTOs.iterator();
                while(iterator.hasNext()){
                    ItemBrandDTO itemBrandDTO=iterator.next();
                    if(itemBrandDTO.getBrandId()!=null){
                        ppMap.put(itemBrandDTO.getBrandId(),itemBrandDTO.getBrandName());
                    }
                }
            }
        }
        return ppMap;
    }
    /**
     * 店铺类目审核
     * @author - 门光耀
     * @createDate - 2015-3-4
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/auditppresult")
    @ResponseBody
    public Json auditPPResult(HttpServletRequest request){
        Json json=new Json();
        try{
            String auditStatic=request.getParameter("static");
            String shopid=request.getParameter("shopid");
            String brandid=request.getParameter("brandid");
            String ms=request.getParameter("ms");
            String id=UserUtils.getUser().getId();
            String idd=request.getParameter("id");
            ShopBrandDTO  shopCategoryDTO=new ShopBrandDTO ();
            //2为通过
            if("2".equals(auditStatic)){
                shopCategoryDTO.setId(new Long(idd));
                ms="审核通过";
                shopCategoryDTO.setShopId(new Long(shopid));
                shopCategoryDTO.setStatus(new Integer(2));
                shopCategoryDTO.setComment(ms);
                shopCategoryDTO.setBrandId(new Long(brandid));
                //用户id
                shopCategoryDTO.setOperatorId(id);
                shopCategoryDTO.setModified(new Date());
            }else{
                shopCategoryDTO.setId(new Long(idd));
                shopCategoryDTO.setShopId(new Long(shopid));
                shopCategoryDTO.setStatus(new Integer(3));
                shopCategoryDTO.setComment(ms);
                shopCategoryDTO.setBrandId(new Long(brandid));
                //用户id
                shopCategoryDTO.setOperatorId(id);
                shopCategoryDTO.setModified(new Date());
            }
            //将被驳回的过期无用的消息的status状态值设置为-1
            deleteOldRejectedshopBrand(idd);
            
            ExecuteResult<String> executeResult=shopBrandExportService.modifyShopBrandStatus(shopCategoryDTO);
            if(executeResult.getResult()!=null&&!"".equals(executeResult.getResult())&&new Integer(executeResult.getResult())>0){
                json.setMsg("审核完成");
                json.setSuccess(true);
            }else{
                if(executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
                    for(int i=0;i<executeResult.getErrorMessages().size();i++){
                        logger.error(executeResult.getErrorMessages().get(i));
                    }
                    json.setMsg("审核结果异常");
                    json.setSuccess(false);
                }
            }
        }catch(Exception e){
            json.setMsg("系统异常请稍后再试");
            json.setSuccess(false);
            logger.error("新增品牌审核异常:"+e.getMessage());
        }
        return json;
    }
    /**
     * 将被驳回的过期无用的消息的status状态值设置为-1
     * @author - 江鹏
     * @createDate - 2015-7-29
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
	private void deleteOldRejectedshopBrand(String idd) {
		List<ShopBrandDTO> brandDTOslist=new ArrayList<ShopBrandDTO>();  
		brandDTOslist=shopBrandExportService.selectBrandIdById(new Long(idd));
		int brandDTOslistsize=brandDTOslist.size();
		    if(brandDTOslistsize>0){
		    	Long brandId=  brandDTOslist.get(0).getBrandId();
		    	shopBrandExportService.updateStatusByIdAndBrandId(brandId);
		    }
	}
    //店铺url添加 ftp前段地址
    private Map<String,String> ifaddA(){
        Map<String,String> map=new HashMap<String, String>();
        map.put("logo_url","logo_url");
        map.put(ShopModify.ShopModifyColumn.trademark_regist_cert.name(),ShopModify.ShopModifyColumn.trademark_regist_cert.getLabel());
        map.put(ShopModify.ShopModifyColumn.inspection_report.name(),ShopModify.ShopModifyColumn.inspection_report.getLabel());
        map.put(ShopModify.ShopModifyColumn.production_license.name(),ShopModify.ShopModifyColumn.production_license.getLabel());
        map.put(ShopModify.ShopModifyColumn.marketing_auth.name(),ShopModify.ShopModifyColumn.marketing_auth.getLabel());
        map.put(ShopModify.ShopModifyColumn.gp_commitment_book.name(),ShopModify.ShopModifyColumn.gp_commitment_book.getLabel());
        map.put(ShopModify.ShopModifyColumn.disclaimer.name(),ShopModify.ShopModifyColumn.disclaimer.getLabel());
        return map;
    }
    //店铺字段转义
    private Map<String,Map<String,String>> shopZy(){
        Map<String,Map<String,String>> zymap=new HashMap<String, Map<String, String>>();
        //mutil_condition 混批条件
        Map<String,String> map1=new HashMap<String, String>();
        map1.put("1","或");
        map1.put("2","且");
        zymap.put(ShopModify.ShopModifyColumn.mutil_condition.name(),map1);

        //initial_condition 起批条件
        Map<String,String> map2=new HashMap<String, String>();
        map2.put("1","或");
        map2.put("2","且");
        zymap.put(ShopModify.ShopModifyColumn.initial_condition.name(),map2);
        //shop_type 店铺类型
        Map<String,String> map3=new HashMap<String, String>();
        map3.put("1","品牌商");
        map3.put("2","经销商");
        zymap.put(ShopModify.ShopModifyColumn.shop_type.name(),map3);
        //business_type 经营类型
        Map<String,String> map4=new HashMap<String, String>();
        map4.put("1","自有品牌");
        map4.put("2","代理品牌");
        zymap.put(ShopModify.ShopModifyColumn.business_type.name(),map4);
        //brand_type 品牌类型
        Map<String,String> map5=new HashMap<String, String>();
        map5.put("1","国内品牌");
        map5.put("2","代理品牌");
        zymap.put(ShopModify.ShopModifyColumn.brand_type.name(),map5);

        //financing 融资需求
        Map<String,String> map6=new HashMap<String, String>();
        map6.put("1","是");
        map6.put("2","否");
        zymap.put(ShopModify.ShopModifyColumn.financing.name(),map6);
        return zymap;
    }
    //用户图片添加ftp台头
    private Map<String,String> ifAddUrl(){
        Map<String,String> map=new HashMap<String, String>();
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.artificial_person_pic_src.name(),UserInfoModifyEmums.UserInfoModifyColumn.artificial_person_pic_src.getLabel());
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.artificial_person_pic_back_src.name(),UserInfoModifyEmums.UserInfoModifyColumn.artificial_person_pic_back_src.getLabel());
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.business_licence_pic_src.name(),UserInfoModifyEmums.UserInfoModifyColumn.business_licence_pic_src.getLabel());
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.organization_pic_src.name(),UserInfoModifyEmums.UserInfoModifyColumn.organization_pic_src.getLabel());
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.tax_registration_certificate_pic_src.name(),UserInfoModifyEmums.UserInfoModifyColumn.tax_registration_certificate_pic_src.getLabel());
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.taxpayer_certificate_pic_src.name(),UserInfoModifyEmums.UserInfoModifyColumn.taxpayer_certificate_pic_src.getLabel());
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.bank_account_permits_pic_src.name(),UserInfoModifyEmums.UserInfoModifyColumn.bank_account_permits_pic_src.getLabel());
        return map;
    }
    //用户地址转义
    private Map<String,String> addressMap(){
        Map<String,String> map=new HashMap<String, String>();
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.company_address.name(),UserInfoModifyEmums.UserInfoModifyColumn.company_address.getLabel());
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.bank_branch_is_located.name(),UserInfoModifyEmums.UserInfoModifyColumn.bank_branch_is_located.getLabel());
        map.put(UserInfoModifyEmums.UserInfoModifyColumn.business_licence_address.name(),UserInfoModifyEmums.UserInfoModifyColumn.business_licence_address.getLabel());
        //map.put(UserInfoModifyEmums.UserInfoModifyColumn.delivery_address.name(),UserInfoModifyEmums.UserInfoModifyColumn.delivery_address.getLabel());
        return map;
    }
    //用户审核特殊字段转义成对应的描述
    private Map<String,Map<String,String>> userZy(){
        Map<String,Map<String,String>> zymap=new HashMap<String, Map<String, String>>();
        //taxpayer_type,纳税人类型 不需要
        //is_citic_bank,是否中信银行
        Map<String,String> map1=new HashMap<String, String>();
        map1.put("1","是");
        map1.put("0","不是");
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.is_citic_bank.name(),map1);
        //company_scale 公司规模 不需要
        //service_type 业务类型 不需要
        //company_peo_num 公司人数规模
        Map<String,String> map2=new HashMap<String, String>();
        UserEnums.CompanyPeopleNum pn[]=UserEnums.CompanyPeopleNum.values();
        for(UserEnums.CompanyPeopleNum peopleNum:pn){
            map2.put(peopleNum.name(),peopleNum.getLabel());
        }
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.company_peo_num.name(),map2);
        //company_qualt 公司性质
        Map<String,String> map3=new HashMap<String, String>();
        UserEnums.CompanyQualt cq[]=UserEnums.CompanyQualt.values();
        for(UserEnums.CompanyQualt cqt:cq){
            map3.put(cqt.name(),cqt.getLabel());
        }
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.company_qualt.name(),map3);
        //business_scale 经营规模
        Map<String,String> map4=new HashMap<String, String>();
        UserEnums.BusinessScale bs[]=UserEnums.BusinessScale.values();
        for(UserEnums.BusinessScale cqt:bs){
            map4.put(cqt.name(),cqt.getLabel());
        }
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.business_scale.name(),map4);
        //is_financing 是否融资
        Map<String,String> map5=new HashMap<String, String>();
        map5.put("1","是");
        map5.put("0","否");
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.is_financing.name(),map5);
        //tax_type 纳税类型
        Map<String,String> map6=new HashMap<String, String>();
        UserEnums.TaxType taxtypes[]=UserEnums.TaxType.values();
        for(UserEnums.TaxType cqt:taxtypes){
            map6.put(cqt.name(),cqt.getLabel());
        }
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.tax_type.name(),map6);
        //dealer_type 经营类型
        Map<String,String> map7=new HashMap<String, String>();
        UserEnums.DealerType dt[]=UserEnums.DealerType.values();
        for(UserEnums.DealerType cqt:dt){
            map7.put(cqt.name(),cqt.getLabel());
        }
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.dealer_type.name(),map7);
        //is_have_ebusiness 是否有电子商务经验
        Map<String,String> map8=new HashMap<String, String>();
        map8.put("1","有");
        map8.put("0","无");
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.is_have_ebusiness.name(),map8);
        //erp_type erp类型
        Map<String,String> map9=new HashMap<String, String>();
        UserEnums.ERPType erp[]=UserEnums.ERPType.values();
        for(UserEnums.ERPType cqt:erp){
            map9.put(cqt.name(),cqt.getLabel());
        }
        zymap.put(UserInfoModifyEmums.UserInfoModifyColumn.erp_type.name(),map9);
        return zymap;
    }
}
