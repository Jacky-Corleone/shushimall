package com.camelot.ecm.serverule;

import com.camelot.basecenter.dto.MallClassifyDTO;
import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.dto.PlatformServiceRuleDTO;
import com.camelot.basecenter.service.MallClassifyService;
import com.camelot.basecenter.service.MallDocumentService;
import com.camelot.basecenter.service.PlatformServiceRuleExportService;
import com.camelot.common.Json;
import com.camelot.common.MallType;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userrule.UserPlatformServiceRuleDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserPlatformServiceRuleService;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sevelli on 15-3-3.
 * @description 服务规则Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/serverule")
public class ServeruleController extends BaseController {
    @Resource
    private PlatformServiceRuleExportService platformServiceRuleExportService;
    @Resource
    private MallDocumentService mallDocumentService;
    @Resource
    private MallClassifyService mallClassifyService;
    @Resource
    private UserPlatformServiceRuleService userPlatformServiceRuleService;
    @Resource
    private UserExportService userExportService;
    @Resource
    private ShopExportService shopExportService;
    @RequestMapping(value = "list")
    public ModelAndView list(HttpServletRequest request){
        Page p=new Page();
        List<Map<String,Object>> listMap=new ArrayList<Map<String, Object>>();
        PlatformServiceRuleDTO dto=new PlatformServiceRuleDTO();
        Pager pager=new Pager();
        String pageNo=request.getParameter("pageNo");
        String pageSize=request.getParameter("pageSize");
        //设置查询的每页大小和第几页
        if(pageNo!=null&&!"".equals(pageNo)&&pageSize!=null&&!"".equals(pageSize)){
            pager.setPage(new Integer(pageNo));
            pager.setRows(new Integer(pageSize));
        }else{
            pager.setPage(1);
            pager.setRows(10);
        }
        ExecuteResult<DataGrid<PlatformServiceRuleDTO>> executeResult= platformServiceRuleExportService.queryList(dto, pager);
        DataGrid<PlatformServiceRuleDTO> dataGrid=executeResult.getResult();
        if(dataGrid!=null){
            List<PlatformServiceRuleDTO> listdto=dataGrid.getRows();
            if(listdto!=null&&listdto.size()>0){
                Map<String,String> userMap=findUserlist(listdto);
                Iterator<PlatformServiceRuleDTO> iterator=listdto.iterator();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                int i=1;
                while(iterator.hasNext()){
                    Map<String,Object> maptable=new HashMap<String, Object>();
                    PlatformServiceRuleDTO platformServiceRuleDTO=iterator.next();
                    //规则id
                    maptable.put("ruleId",platformServiceRuleDTO.getRuleId());
                    //规则name
                    maptable.put("ruleName",platformServiceRuleDTO.getRuleName());
                    //创建时间
                    if(platformServiceRuleDTO.getCreateTime()!=null){
                       maptable.put("createTime",simpleDateFormat.format(platformServiceRuleDTO.getCreateTime()));
                    }
                    maptable.put("createName",userMap.get(String.valueOf(platformServiceRuleDTO.getCreateUserId())));
                    maptable.put("num",String.valueOf(i));
                    maptable.put("url",platformServiceRuleDTO.getIconImageSrc());
                    i++;
                    listMap.add(maptable);
                }
            }
            p.setCount(dataGrid.getTotal());
        }else{
            p.setCount(0);
        }
        p.setList(listMap);
        //页码，每页条数
        p.setPageSize(pager.getRows());
        p.setPageNo(pager.getPage());
        //TODO 服务规则列表查询
        return new ModelAndView("/serverule/list","page",p);
    }
    private Map<String,String> findUserlist(List<PlatformServiceRuleDTO> listPlat){
        Map<String,String> listMap=new HashMap<String, String>();
        if(listPlat!=null&&listPlat.size()>0){
            Iterator<PlatformServiceRuleDTO> iterator=listPlat.iterator();
            Map<String,String> map=new HashMap<String, String>();
            List<String> ids=new ArrayList<String>();
            while(iterator.hasNext()){
                PlatformServiceRuleDTO platformServiceRuleDTO=iterator.next();
                map.put(String.valueOf(platformServiceRuleDTO.getCreateUserId()),"1");
            }
            if(map.size()>0){
                Iterator<String> iterator1=map.keySet().iterator();
                while(iterator1.hasNext()){
                    ids.add(iterator1.next());
                }
                List<User> listUsers=UserUtils.selectUsersByIds(ids);
                if(listUsers!=null&&listUsers.size()>0){
                    Iterator<User> iterator2=listUsers.iterator();
                    while(iterator2.hasNext()){
                        User user=iterator2.next();
                        listMap.put(user.getId(),user.getName());
                    }
                }
            }
        }
        return listMap;
    }
    /**
     * 服务规则明细查看
     * @author - 门光耀
     * @createDate - 2015-3-19
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/showdetail")
    public ModelAndView showDetail(HttpServletRequest request){
        //获取服务id
        String ruleId=request.getParameter("ruleId");
        Map<String,String> map=new HashMap<String,String>();
        if(ruleId!=null&&!"".equals(ruleId)){
            PlatformServiceRuleDTO dto=new PlatformServiceRuleDTO();
            dto.setRuleId(new Long(ruleId));
            Pager pager=new Pager();
            pager.setPage(1);
            pager.setRows(10);
            ExecuteResult<DataGrid<PlatformServiceRuleDTO>> executeResult= platformServiceRuleExportService.queryList(dto, pager);
            DataGrid<PlatformServiceRuleDTO> dtoDataGrid=executeResult.getResult();
            if(dtoDataGrid!=null){
                List<PlatformServiceRuleDTO> list=dtoDataGrid.getRows();
                if(list!=null&&list.size()>0){
                    PlatformServiceRuleDTO plt=list.get(0);
                    //规则名称
                    map.put("ruleName",plt.getRuleName());
                    map.put("ruleUrl",plt.getIconImageSrc());
                    Long docid=plt.getHelpDocId();
                    if(docid!=null){
                        MallDocumentDTO docTto=mallDocumentService.getMallDocumentById(docid);
                        if(docTto!=null){
                            //帮助文档
                            map.put("docTitle",docTto.getMallTitle());
                        }
                    }
                    //简介
                    map.put("simple",plt.getSimpleIntro());
                    //详情描述
                    map.put("details",plt.getDetails());
                }
            }
        }
        return new ModelAndView("/serverule/showdetail","map",map);
    }
    /**
     * 跳往新增页面
     * @author - 门光耀
     * @createDate - 2015-3-19
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/goadd")
    public ModelAndView goAdd(){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("typeList",getTypeList());
        return new ModelAndView("/serverule/platformruleadd","map",map);
    }
    /**
     * 获取文档类型集合
     * @param
     * @return
     */
    public List<Map<Integer, Object>> getTypeList() {
        List<Map<Integer, Object>> mapList = Lists.newArrayList();
        //获取所有的文档类型
        EnumSet<MallType> currEnumSet = EnumSet.allOf (MallType. class );
        for(MallType mallType : currEnumSet){
            Map<Integer,Object> typeMap = new HashMap<Integer,Object>();
            typeMap.put(mallType.getKey(), mallType.getDesc());
            mapList.add(typeMap);
        }
        return mapList;
    }
    /**
     * 获取帮助文档
     * @author - 门光耀
     * @createDate - 2015-3-19
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("selectdoc")
    @ResponseBody
    public Json selectDoc(String docclass){
        Json json=new Json();
        MallDocumentDTO mallDocumentDTO=new MallDocumentDTO();
        if(docclass!=null&&!"".equals(docclass)){
            mallDocumentDTO.setMallClassifyId(new Integer(docclass));
            Pager pager=new Pager();
            pager.setPage(1);
            pager.setRows(Integer.MAX_VALUE);
            mallDocumentDTO.setMallStatus(2);
            DataGrid<MallDocumentDTO> dataGrid=mallDocumentService.queryMallDocumentList( mallDocumentDTO,pager);
            if(dataGrid!=null){
                json.setObj(dataGrid.getRows());
            }
            json.setMsg("获取成功");
            json.setSuccess(true);
        }else{
            json.setSuccess(false);
            json.setMsg("后台没有获取类型编码");
        }
        return json;
    }
    @RequestMapping("/addrult")
    @ResponseBody
    public Json addRult(HttpServletRequest request){
        Json json=new Json();
        try{
            //图片url
            String fileurlid=request.getParameter("fileurlid");
            //规则名称
            String name=request.getParameter("name");
            //帮助文档
            String mx=request.getParameter("mx");
            //简介
            String simple=request.getParameter("simple");
            //详情
            String detail=request.getParameter("detail");
            PlatformServiceRuleDTO pla=new PlatformServiceRuleDTO();
            pla.setIconImageSrc(fileurlid);
            pla.setRuleName(name);
            pla.setSimpleIntro(simple);
            pla.setDetails(detail);
            if(mx!=null&&!"".equals(mx)){
                pla.setHelpDocId(new Integer(mx));
            }
            //当前登录人
            String uid= UserUtils.getUser().getId();
            if(uid!=null&&!"".equals(uid)){
                pla.setCreateUserId(uid);
            }
            pla.setCreateTime(new Date());
            ExecuteResult<String> result=platformServiceRuleExportService.addServiceRule(pla);
            json.setMsg("新增规则成功");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("新增失败,请联系程序猿");
            json.setSuccess(false);
        }
        return json;
    }

    /**
     * 服务规则明细查看
     * @author - 门光耀
     * @createDate - 2015-3-19
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/goupdate")
    public ModelAndView goUpdate(HttpServletRequest request){
        //获取服务id
        String ruleId=request.getParameter("ruleId");
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("ruleId",ruleId);
//        Integer classfid=null;
        Integer mallType=null;
        if(ruleId!=null&&!"".equals(ruleId)){
            PlatformServiceRuleDTO dto=new PlatformServiceRuleDTO();
            dto.setRuleId(new Long(ruleId));
            Pager pager=new Pager();
            pager.setPage(1);
            pager.setRows(10);
            ExecuteResult<DataGrid<PlatformServiceRuleDTO>> executeResult= platformServiceRuleExportService.queryList(dto, pager);
            DataGrid<PlatformServiceRuleDTO> dtoDataGrid=executeResult.getResult();
            if(dtoDataGrid!=null){
                List<PlatformServiceRuleDTO> list=dtoDataGrid.getRows();
                if(list!=null&&list.size()>0){
                    PlatformServiceRuleDTO plt=list.get(0);
                    //规则名称
                    map.put("ruleName",plt.getRuleName());
                    map.put("ruleUrl",plt.getIconImageSrc());
                    //用于显示帮助文档的下拉框
                    List<Map<String,String>> listMxMap=new ArrayList<Map<String, String>>();
                    Map<String,String> mxMap=new HashMap<String,String>();
                    Long docid=plt.getHelpDocId();
                    if(docid!=null){

                        map.put("docid",new BigDecimal(docid).toPlainString());
                        mxMap.put(new BigDecimal(docid).toPlainString(),"");
                        MallDocumentDTO docTto=mallDocumentService.getMallDocumentById(docid);
                        if(docTto!=null){
                            //帮助文档
                            map.put("docTitle",docTto.getMallTitle());
                            
                            mallType = docTto.getMallType();
                            map.put("mallType",mallType);

                            mxMap.put(new BigDecimal(docid).toPlainString(),docTto.getMallTitle());
                            if(docTto.getMallClassifyId()!=null){
                               // classfid=docTto.getMallClassifyId();
                                map.put("docclassid",new BigDecimal(docTto.getMallClassifyId()).toPlainString());
                                MallDocumentDTO mallDocumentDTO=new MallDocumentDTO();
                                mallDocumentDTO.setMallClassifyId(docTto.getMallClassifyId());
                                Pager pager1=new Pager();
                                pager1.setPage(1);
                                pager1.setRows(Integer.MAX_VALUE);
                                DataGrid<MallDocumentDTO> dataGrid=mallDocumentService.queryMallDocumentList(mallDocumentDTO,pager1);
                                if(dataGrid!=null){
                                    List<MallDocumentDTO> listdocs=dataGrid.getRows();
                                    if(listdocs!=null&listdocs.size()>0){
                                        Iterator<MallDocumentDTO> iterator=listdocs.iterator();
                                        while(iterator.hasNext()){
                                            MallDocumentDTO mallDocumentDTO1=iterator.next();
                                            mxMap.put(new BigDecimal(mallDocumentDTO1.getMallId()).toPlainString(),mallDocumentDTO1.getMallTitle());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(mxMap.size()>0){
                        Iterator<Map.Entry<String,String>> iterator=mxMap.entrySet().iterator();
                        while(iterator.hasNext()){
                            Map.Entry<String,String> entry=iterator.next();
                            Map<String,String> map1=new HashMap<String, String>();
                            map1.put("code",entry.getKey());
                            map1.put("name",entry.getValue());
                            listMxMap.add(map1);
                        }
                        map.put("listMxMap",listMxMap);
                    }
                    //简介
                    map.put("simple",plt.getSimpleIntro());
                    //详情描述
                    map.put("details",plt.getDetails());
                }
                if(null != mallType){
                	MallClassifyDTO mallCassifyDTO1=new MallClassifyDTO();
                	mallCassifyDTO1.setType(mallType);
                	mallCassifyDTO1.setStatus(2);
                	Pager page3=new Pager();
                    page3.setRows(Integer.MAX_VALUE);
                    page3.setPage(1);
                    DataGrid<MallClassifyDTO> dataGrid=mallClassifyService.queryMallCassifyList(mallCassifyDTO1,page3);
                    if(dataGrid!=null){
                        List<MallClassifyDTO> listc=dataGrid.getRows();
                        if(list!=null&&list.size()>0){
                            map.put("list",listc);
                        }
                    }
                }
                /*if(classfid!=null){
	                MallClassifyDTO mallCassifyDTO1=new MallClassifyDTO();
                    mallCassifyDTO1.setId(classfid);
                    Pager page3=new Pager();
                    page3.setRows(Integer.MAX_VALUE);
                    page3.setPage(1);
                    DataGrid<MallClassifyDTO> dataGrid1=mallClassifyService.queryMallCassifyList(mallCassifyDTO1,page3);
                    if(dataGrid1!=null&&dataGrid1.getRows()!=null&&dataGrid1.getRows().size()>0){
                        MallClassifyDTO maldto=dataGrid1.getRows().get(0);
                        if(maldto.getType()!=null){
                            map.put("classfytype",maldto.getType());
                            MallClassifyDTO mallCassifyDTO=new MallClassifyDTO();
                            mallCassifyDTO.setType(maldto.getType());
                            Pager page2=new Pager();
                            page2.setRows(Integer.MAX_VALUE);
                            page2.setPage(1);
                            DataGrid<MallClassifyDTO> dataGrid=mallClassifyService.queryMallCassifyList(mallCassifyDTO,page2);
                            if(dataGrid!=null){
                                List<MallClassifyDTO> listc=dataGrid.getRows();
                                if(list!=null&&list.size()>0){
                                    map.put("list",listc);
                                }
                            }
                        }
                    }

                }*/
            }
        }
        map.put("typeList",getTypeList());
        return new ModelAndView("/serverule/platformruleupdate","map",map);
    }
    @RequestMapping("/updaterule")
    @ResponseBody
    public Json updateRule(HttpServletRequest request){
        Json json=new Json();
        try{
            //图片url
            String fileurlid=request.getParameter("fileurlid");
            //规则名称
            String name=request.getParameter("name");
            //帮助文档
            String mx=request.getParameter("mx");
            //简介
            String simple=request.getParameter("simple");
            //详情
            String detail=request.getParameter("detail");
            String ruleId=request.getParameter("ruleId");
            PlatformServiceRuleDTO pla=new PlatformServiceRuleDTO();
            pla.setIconImageSrc(fileurlid);
            pla.setRuleName(name);
            pla.setSimpleIntro(simple);
            pla.setDetails(detail);
            pla.setRuleId(new Long(ruleId));
            if(mx!=null&&!"".equals(mx)){
                pla.setHelpDocId(new Integer(mx));
            }
            //当前登录人
            String uid= UserUtils.getUser().getId();
            if(uid!=null&&!"".equals(uid)){
                pla.setCreateUserId(uid);
            }
            pla.setUpdateTime(new Date());
            ExecuteResult<String> result=platformServiceRuleExportService.modifyPlatformServiceRule(pla);
            json.setMsg("修改规则成功");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("规则修改失败,请联系程序猿");
            json.setSuccess(false);
        }
        return json;
    }
    /**
     * 服务规则认证统计
     * @author - 门光耀
     * @createDate - 2015-3-24
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "count")
    public ModelAndView count(HttpServletRequest request,Model model){
        UserPlatformServiceRuleDTO dto=new UserPlatformServiceRuleDTO();
        Page page=new Page();
        Pager pager=new Pager();
        String pageNo=request.getParameter("pageNo");
        String pageSize=request.getParameter("pageSize");
        //如果没有收到前台传来的翻页信息，系统默认显示第一页，每页10条数据
        if(pageNo!=null&&!"".equals(pageNo)){
            page.setPageNo(new Integer(pageNo));
            pager.setPage(new Integer(pageNo));
        }else{
            page.setPageNo(1);
            pager.setPage(1);
        }
        if(pageSize!=null&&!"".equals(pageSize)){
            page.setPageSize(new Integer(pageSize));
            pager.setRows(new Integer(pageSize));
        }else{
            page.setPageSize(10);
            pager.setRows(10);
        }
        DataGrid<UserPlatformServiceRuleDTO> dataGrid= userPlatformServiceRuleService.getUserPlatformRuleStatistics(dto,pager);
        if(dataGrid!=null){
            List<UserPlatformServiceRuleDTO> listrule=dataGrid.getRows();
            if(listrule!=null&&listrule.size()>0){
                page.setList(ruleCount(listrule));
                page.setCount(dataGrid.getTotal());
            }else{
                page.setCount(0);
            }
        }else{
            page.setCount(0);
        }
        //TODO 认证统计
        return new ModelAndView("serverule/listtj","page",page);
    }
    private List<Map<String,String>> ruleCount(List<UserPlatformServiceRuleDTO> listrule){
        List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
        if(listrule!=null&&listrule.size()>0){
            Map<Long,String> mapruleids=new HashMap<Long, String>();
            Iterator<UserPlatformServiceRuleDTO> iterator=listrule.iterator();
            Map<Long,String> mapRuleName=new HashMap<Long, String>();
            while(iterator.hasNext()){
                UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=iterator.next();
                if(userPlatformServiceRuleDTO.getRuleId()!=null){
                    mapruleids.put(userPlatformServiceRuleDTO.getRuleId(),"1");
                }
            }
            //将汇总查询的服务规则汇总成一个数组
            if(mapruleids.size()>0){
                Long[] ruleids=new Long[mapruleids.size()];
                Iterator<Long> iterator1=mapruleids.keySet().iterator();
                int i=0;
                while(iterator1.hasNext()){
                    ruleids[i]=iterator1.next();
                    i++;
                }
                //根据服务规则的ids获取服务规则
                ExecuteResult<List<PlatformServiceRuleDTO>> executeResult=platformServiceRuleExportService.queryRuleByRuleIds(ruleids);
                if(executeResult!=null){
                    List<PlatformServiceRuleDTO> list=executeResult.getResult();
                    if(list!=null&&list.size()>0){
                        Iterator<PlatformServiceRuleDTO> iterator2=list.iterator();
                        while(iterator2.hasNext()){
                            PlatformServiceRuleDTO platformServiceRuleDTO=iterator2.next();
                            mapRuleName.put(new Long(platformServiceRuleDTO.getRuleId()),platformServiceRuleDTO.getRuleName());
                        }
                    }
                }
            }
            Iterator<UserPlatformServiceRuleDTO> iterator1=listrule.iterator();
            int i=1;
            while(iterator1.hasNext()){
                Map<String,String> map1=new HashMap<String, String>();
                UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=iterator1.next();
                if(userPlatformServiceRuleDTO.getRuleId()!=null){
                    map1.put("ruleId",userPlatformServiceRuleDTO.getRuleId().toString());
                    map1.put("ruleName",mapRuleName.get(userPlatformServiceRuleDTO.getRuleId()));
                }
                map1.put("count",String.valueOf(userPlatformServiceRuleDTO.getUserCount()));
                map1.put("num",String.valueOf(i));
                i++;
                listMap.add(map1);
            }
        }
        return listMap;
    }
    @RequestMapping("/showcountdetail")
    public ModelAndView showCountDetail(HttpServletRequest request){
        PlatformServiceRuleDTO dto1=new PlatformServiceRuleDTO();
        Pager pager1=new Pager();
        pager1.setPage(1);
        pager1.setRows(Integer.MAX_VALUE);
        ExecuteResult<DataGrid<PlatformServiceRuleDTO>> executeResult= platformServiceRuleExportService.queryList(dto1, pager1);
        //将会向前台返回的值放到此map中
        Map<String,Object> countdetail=new HashMap<String, Object>();
        Map<Long,String> ruleMapnames=new HashMap<Long, String>();
        if(executeResult!=null){
            DataGrid<PlatformServiceRuleDTO> dtoDataGrid=executeResult.getResult();
            if(dtoDataGrid!=null){
                List<PlatformServiceRuleDTO> list=dtoDataGrid.getRows();
                if(list!=null&&list.size()>0){
                    List<Map<String,String>> ruleMaps=new ArrayList<Map<String, String>>();
                    Iterator<PlatformServiceRuleDTO> iterator=list.iterator();
                    while(iterator.hasNext()){
                        PlatformServiceRuleDTO platformServiceRuleDTO=iterator.next();
                        Map<String,String> ruleMap=new HashMap<String, String>();
                        ruleMap.put("code",String.valueOf(platformServiceRuleDTO.getRuleId()));
                        ruleMap.put("name",platformServiceRuleDTO.getRuleName());
                        ruleMaps.add(ruleMap);
                        ruleMapnames.put(new Long(platformServiceRuleDTO.getRuleId()),platformServiceRuleDTO.getRuleName());
                    }
                    countdetail.put("ruleMaps",ruleMaps);
                }
            }
        }
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(10);
        Page page=new Page();
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        String ruleId=request.getParameter("ruleId");
        if(ruleId!=null&&!"".equals(ruleId)){
            countdetail.put("ruleId",ruleId);
        }
        UserPlatformServiceRuleDTO dto=new UserPlatformServiceRuleDTO();
        //设置ruleid用来查询服务规则下的明细用户
        if(ruleId!=null&&!"".equals(ruleId)){
            dto.setRuleId(new Long(ruleId));
        }
        dto.setIsDeleted(new Integer(0));
        DataGrid<UserPlatformServiceRuleDTO> dataGrid= userPlatformServiceRuleService.getUserPlatformRuleDetail(dto,pager);
        if(dataGrid!=null){
            List<UserPlatformServiceRuleDTO> list2=dataGrid.getRows();
            if(list2!=null&&list2.size()>0){
                Map<String,Map<String,String>> shopuserMap=shopuserMap(list2);
                //封装查询到的用户规则
                List<Map<String,String>> listuser=new ArrayList<Map<String, String>>();
                Iterator<UserPlatformServiceRuleDTO> iterator=list2.iterator();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                while(iterator.hasNext()){
                    UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=iterator.next();
                    Map<String,String> mapUser=new HashMap<String, String>();
                    //商家名称(公司名称)
                    mapUser.put("userName",userPlatformServiceRuleDTO.getCompanyName());
                    if(userPlatformServiceRuleDTO.getUserId()!=null){
                        mapUser.put("uid",String.valueOf(userPlatformServiceRuleDTO.getUserId()));
                    }
                    //规则名称
                    if(userPlatformServiceRuleDTO.getRuleId()!=null){
                        mapUser.put("ruleName",ruleMapnames.get(userPlatformServiceRuleDTO.getRuleId()));
                    }
                    //认证日期
                    if(userPlatformServiceRuleDTO.getCreateTime()!=null){
                        mapUser.put("rzdate",simpleDateFormat.format(userPlatformServiceRuleDTO.getCreateTime()));
                    }
                    Map<String,String> shopMap=shopuserMap.get(String.valueOf(userPlatformServiceRuleDTO.getUserId()));
                    if(shopMap!=null){
                        mapUser.put("shopName",shopMap.get("shopName"));
                        mapUser.put("shopUrl",shopMap.get("shopUrl"));
                    }
                    listuser.add(mapUser);
                }
                page.setCount(dataGrid.getTotal());
                page.setList(listuser);
            }else{
                page.setCount(0);
            }
        }else{
            page.setCount(0);
        }
        countdetail.put("page", page);
        return new ModelAndView("serverule/countdetail","countdetail",countdetail);
    }
    @RequestMapping("/selectuser")
    @ResponseBody
    public Json selectUser(HttpServletRequest request){
        Json json=new Json();
        PlatformServiceRuleDTO dto1=new PlatformServiceRuleDTO();
        Pager pager1=new Pager();
        pager1.setPage(1);
        pager1.setRows(Integer.MAX_VALUE);
        ExecuteResult<DataGrid<PlatformServiceRuleDTO>> executeResult= platformServiceRuleExportService.queryList(dto1, pager1);
        Map<Long,String> ruleMapnames=new HashMap<Long, String>();
        if(executeResult!=null){
            DataGrid<PlatformServiceRuleDTO> dtoDataGrid=executeResult.getResult();
            if(dtoDataGrid!=null){
                List<PlatformServiceRuleDTO> list=dtoDataGrid.getRows();
                if(list!=null&&list.size()>0){
                    Iterator<PlatformServiceRuleDTO> iterator=list.iterator();
                    while(iterator.hasNext()){
                        PlatformServiceRuleDTO platformServiceRuleDTO=iterator.next();
                        ruleMapnames.put(new Long(platformServiceRuleDTO.getRuleId()),platformServiceRuleDTO.getRuleName());
                    }
                }
            }
        }
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(10);
        Page page=new Page();
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        String ruleId=request.getParameter("ruleId");
        UserPlatformServiceRuleDTO dto=new UserPlatformServiceRuleDTO();
        //设置ruleid用来查询服务规则下的明细用户
        if(ruleId!=null&&!"".equals(ruleId)){
            dto.setRuleId(new Long(ruleId));
        }
        try{
            String createTimeBegin=request.getParameter("createTimeBegin");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            if(createTimeBegin!=null&&!"".equals(createTimeBegin)){
                dto.setCreateTimeBegin(simpleDateFormat.parse(createTimeBegin));
            }
            String createTimeEnd=request.getParameter("createTimeEnd");
            if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                dto.setCreateTimeEnd(simpleDateFormat.parse(createTimeEnd));
            }
        }catch(Exception e){
            json.setMsg("报错");
        }
        dto.setIsDeleted(new Integer(0));
        DataGrid<UserPlatformServiceRuleDTO> dataGrid= userPlatformServiceRuleService.getUserPlatformRuleDetail(dto,pager);
        if(dataGrid!=null){
            List<UserPlatformServiceRuleDTO> list2=dataGrid.getRows();
            if(list2!=null&&list2.size()>0){
                Map<String,Map<String,String>> shopuserMap=shopuserMap(list2);
                //封装查询到的用户规则
                List<Map<String,String>> listuser=new ArrayList<Map<String, String>>();
                Iterator<UserPlatformServiceRuleDTO> iterator=list2.iterator();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                while(iterator.hasNext()){
                    UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=iterator.next();
                    Map<String,String> mapUser=new HashMap<String, String>();
                    //商家名称(公司名称)
                    mapUser.put("userName",userPlatformServiceRuleDTO.getCompanyName());
                    if(userPlatformServiceRuleDTO.getUserId()!=null){
                        mapUser.put("uid",String.valueOf(userPlatformServiceRuleDTO.getUserId()));
                    }
                    //规则名称
                    if(userPlatformServiceRuleDTO.getRuleId()!=null){
                        mapUser.put("ruleName",ruleMapnames.get(userPlatformServiceRuleDTO.getRuleId()));
                    }
                    //认证日期
                    if(userPlatformServiceRuleDTO.getCreateTime()!=null){
                        mapUser.put("rzdate",simpleDateFormat.format(userPlatformServiceRuleDTO.getCreateTime()));
                    }
                    Map<String,String> shopMap=shopuserMap.get(String.valueOf(userPlatformServiceRuleDTO.getUserId()));
                    if(shopMap!=null){
                        mapUser.put("shopName",shopMap.get("shopName"));
                        mapUser.put("shopUrl",shopMap.get("shopUrl"));
                    }
                    listuser.add(mapUser);
                }
                page.setCount(dataGrid.getTotal());
                page.setList(listuser);
            }else{
                page.setCount(0);
            }
        }else{
            page.setCount(0);
        }
        json.setMsg(page.toString());
        json.setSuccess(true);
        json.setObj(page);
        return json;
    }
    public Map<String,Map<String,String>> shopuserMap(List<UserPlatformServiceRuleDTO> list2){
        Map<String,Map<String,String>> mapList=new HashMap<String,Map<String, String>>();
        Map<String,String> mapusershop=new HashMap<String, String>();
        Map<String,String> mapshopname=new HashMap<String, String>();
        Map<String,String> mapUrl=new HashMap<String, String>();
        if(list2!=null&&list2.size()>0){
            Map<Long,Long> userPlat=new HashMap<Long, Long>();
            Iterator<UserPlatformServiceRuleDTO> iterator=list2.iterator();
            while(iterator.hasNext()){
                UserPlatformServiceRuleDTO userPlatformServiceRuleDTO=iterator.next();
                if(userPlatformServiceRuleDTO.getUserId()!=null){
                    userPlat.put(userPlatformServiceRuleDTO.getUserId(),1L);
                }
            }
            //根据用户ids组查询用户中店铺的ids猪
            if(userPlat.size()>0){
                List<String> listusids=new ArrayList<String>();
                Iterator<Long> iterator1=userPlat.keySet().iterator();
                while(iterator1.hasNext()){
                    listusids.add(String.valueOf(iterator1.next()));
                }
                ExecuteResult<List<UserDTO>> executeResult =userExportService.findUserListByUserIds(listusids);
                List<UserDTO> listUserdto=executeResult.getResult();
                if(listUserdto!=null&&listUserdto.size()>0){

                    Map<String,String> mapshopUser=new HashMap<String, String>();
                    Iterator<UserDTO> iterator2=listUserdto.iterator();
                    while(iterator2.hasNext()){
                        UserDTO userDTO=iterator2.next();
                        if(userDTO.getShopId()!=null&&userDTO.getUid()!=null){
                            //封装用户和店铺的对应关系
                            mapusershop.put(String.valueOf(userDTO.getUid()), String.valueOf(userDTO.getShopId()));
                            //店铺id去重
                            mapshopUser.put(String.valueOf(userDTO.getShopId()),"1");
                        }
                    }
                    if(mapshopUser.size()>0){
                        Iterator<String> iterator3=mapshopUser.keySet().iterator();
                        Long[] shopids=new Long[mapshopUser.size()];
                        int i=0;
                        while(iterator3.hasNext()){
                            shopids[i]=new Long(iterator3.next());
                            i++;
                        }
                        ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
                        shopAudiinDTO.setShopIds(shopids);
                        ExecuteResult<List<ShopDTO>> executeResult1=shopExportService.queryShopInfoByids(shopAudiinDTO);
                        List<ShopDTO> shopDTOList=executeResult1.getResult();
                        if(shopDTOList!=null&&shopDTOList.size()>0){
                            Iterator<ShopDTO> iterator4=shopDTOList.iterator();
                            while(iterator4.hasNext()){
                                ShopDTO shopDTO=iterator4.next();
                                if(shopDTO.getShopId()!=null){
                                    mapshopname.put(String.valueOf(shopDTO.getShopId()),shopDTO.getShopName());
                                    mapUrl.put(String.valueOf(shopDTO.getShopId()),shopDTO.getShopUrl());
                                }
                            }
                        }
                    }
                }
            }
            if(mapusershop.size()>0){
                Iterator<Map.Entry<String,String>> iterator1=mapusershop.entrySet().iterator();
                while(iterator1.hasNext()){
                    Map.Entry<String,String> entry=iterator1.next();
                    Map<String,String> shopnu=new HashMap<String, String>();
                    shopnu.put("shopName",mapshopname.get(entry.getValue()));
                    shopnu.put("shopUrl",mapUrl.get(entry.getValue()));
                    mapList.put(entry.getKey(),shopnu);
                }
            }
        }
        return mapList;
    }
    /**
     * 删除服务规则
     * @param ruleId  
     * @return
     * @author 王东晓
     */
    @RequestMapping(value="deleteRule")
	@ResponseBody
    public ExecuteResult<String> deleteRule(int ruleId){
    	PlatformServiceRuleDTO platformServiceRuleDTO = new PlatformServiceRuleDTO();
    	platformServiceRuleDTO.setRuleId(ruleId);
    	platformServiceRuleDTO.setStatus(0);
    	ExecuteResult<String> result = platformServiceRuleExportService.modifyPlatformServiceRuleStatus(platformServiceRuleDTO);
    	return result;
    }
}
