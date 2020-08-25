package com.camelot.ecm.sellercenter.sellerlist;

import com.camelot.common.Json;
import com.camelot.ecm.usercenter.UserAll;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.dialect.Ingres10Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  商家管理，用户信息查询
 * 
 * @author - 门光耀
 * @message- 商家信息查询导出
 * @createDate - 2015-3-2
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping(value = "${adminPath}/sellerlist")
public class SellerListController extends BaseController {
    @Resource
    private UserExportService userExportService;
    @Resource
    private UserExtendsService userExtendsService;
    /**
     *
     * @author - 门光耀
     * @message- 商家信息查询
     * @createDate - 2015-3-2
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/sellerlist")
    public ModelAndView sellerSelect(){
        Page<Map<String,Object>> p = new Page<Map<String,Object>>();
        UserDTO userdto=new UserDTO();
        com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
        pager.setPage(1);
        pager.setRows(10);
        userdto.setParentId(-1L);
        //DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto, UserEnums.UserType.SELLER,pager);
        DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto, null,pager);
        if(datagrid != null && datagrid.getRows() != null && datagrid.getRows().size() > 0){
        	for(UserDTO userDTO : datagrid.getRows()){
        		//根据用户id查询店铺信息
        		if(userDTO.getParentId() != null){
        			ExecuteResult<UserInfoDTO> result = userExtendsService.findUserInfo(userDTO.getParentId());
        			String companyName = result != null && result.isSuccess() && result.getResult() != null?
        						result.getResult().getUserBusinessDTO().getCompanyName():"";
        			userDTO.setCompanyName(companyName);
        		}
        	}
        }
        p.setCount(datagrid.getTotal());
        p.setList(zylx(datagrid.getRows()));
        p.setPageNo(pager.getPage());
        p.setPageSize(pager.getRows());
        Map<String,Object> userlist=new HashMap<String, Object>();
        userlist.put("page",p);
        UserEnums.UserStatus[] userStatuss=UserEnums.UserStatus.values();
        userlist.put("buflag",userStatuss);
		return new ModelAndView("/sellercenter/sellerlist/sellerlist","userlist",userlist);
	}
    /**
     *
     * @author - 门光耀
     * @message- 将或得到的商家信息转移成map集合信息返回到前台
     * @createDate - 2015-3-2
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    public List<Map<String,Object>> zylx (List<UserDTO> list){
        List<Map<String,Object>> listmaps=new ArrayList<Map<String, Object>>();
        if(list!=null&&list.size()>0){
            UserEnums.UserStatus[] userStatuss=UserEnums.UserStatus.values();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Map<Integer,String> qkmap=new HashMap<Integer, String>();
            //写死的注册方式
            qkmap.put(1,"普通注册");
            qkmap.put(2,"快速注册");
            Map<Integer,String> stmap=new HashMap<Integer, String>();
            if(userStatuss!=null&&userStatuss.length>0){
                for(UserEnums.UserStatus us:userStatuss){
                    stmap.put(us.getCode(),us.getLabel());
                }
            }
            Iterator<UserDTO> i=list.iterator();
            int num=1;
            while(i.hasNext()){
                UserDTO ud=i.next();
                Map<String,Object> map=new HashMap<String, Object>();
                if(ud.getUid()!=null){
                    map.put("uid",ud.getUid().toString());
                }
                map.put("uname",ud.getUname());
                map.put("nickname",ud.getNickname());
                map.put("usertype",ud.getUsertype());
                map.put("userstatus",ud.getUserstatus());
                map.put("userstatusname",stmap.get(ud.getUserstatus()));
                map.put("umobile",ud.getUmobile());
                map.put("companyName",ud.getCompanyName());
                map.put("userEmail",ud.getUserEmail());
                map.put("platformId",ud.getPlatformId());
                if(ud.getCreated()!=null){
                    map.put("created", df.format(ud.getCreated()));
                }
                map.put("quickType",qkmap.get(ud.getQuickType()));
                //所属平台
                map.put("platformId",ud.getPlatformId());
                map.put("num",String.valueOf(num));
                num++;
                listmaps.add(map);
            }
        }
        return listmaps;
    }
    /**
     *
     * @author - 门光耀
     * @message- 商家信息查询
     * @createDate - 2015-3-2
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/selectuser")
    @ResponseBody
    public Json<Page<Map<String,Object>>> selectuser(HttpServletRequest request){
        Json json=new Json();
            UserDTO userdto=new UserDTO();
            com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
            //设置分页
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
            //获取查询条件
            String uid=request.getParameter("uid");
            if(uid!=null&&!"".equals(uid)){
                userdto.setUid(new Long(uid));
            }
            String quickType=request.getParameter("quickType");
            if(quickType!=null&&!"".equals(quickType)){
                userdto.setQuickType(new Integer(quickType));
            }
            String userstatus=request.getParameter("userstatus");
            if(userstatus!=null&&!"".equals(userstatus)){
                userdto.setUserstatus(new Integer(userstatus));
            }
          //所属平台id
            String platformId=request.getParameter("platformId");
            if(platformId!=null&&!"".equals(platformId)){
            	userdto.setPlatformId(new Integer(platformId));
            }
            userdto.setUname(request.getParameter("uname"));
            userdto.setNickname(request.getParameter("nickname"));
            userdto.setCompanyName(request.getParameter("companyName"));
            String createTimeBegin=request.getParameter("createTimeBegin");
            userdto.setCreateTimeBegin(createTimeBegin);
            String createTimeEnd=request.getParameter("createTimeEnd");
            if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
                try{
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    Calendar   calendar   =   new   GregorianCalendar();
                    calendar.setTime(simpleDateFormat.parse(createTimeEnd));
                    //calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                    Date end=calendar.getTime();
                    userdto.setCreateTimeEnd(simpleDateFormat.format(end));
                }catch(Exception e){
                    json.setSuccess(false);
                }
            }
            userdto.setParentId(-1L);
            //DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto, UserEnums.UserType.SELLER,pager);
            DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto,null,pager);
            if(datagrid != null && datagrid.getRows() != null && datagrid.getRows().size() > 0){
            	for(UserDTO userDTO : datagrid.getRows()){
            		//根据用户id查询店铺信息
            		if(userDTO.getParentId() != null){
            			ExecuteResult<UserInfoDTO> result = userExtendsService.findUserInfo(userDTO.getParentId());
            			String companyName = result != null && result.isSuccess() && result.getResult() != null?result.getResult().getUserBusinessDTO().getCompanyName():"";
            			userDTO.setCompanyName(companyName);
            		}
            	}
            }
            Page<Map<String,Object>> page = new Page<Map<String,Object>>();
            page.setCount(datagrid.getTotal());
            page.setList(zylx(datagrid.getRows()));
            page.setPageNo(pager.getPage());
            page.setPageSize(pager.getRows());
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        return json;
    }
    /**
     *
     * @author - 门光耀
     * @message- 商家信息导出
     * @createDate - 2015-3-2
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping(value = "exportpage", method= RequestMethod.POST)
    public String exportFilePage(HttpServletRequest request,HttpServletResponse response) {
        try {
            Json<Page<Map<String,Object>>> json=selectuser(request);
            String fileName = "date" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<Map<String,Object>> listMap=json.getObj().getList();
            List<UserAll> listuasrall=new ArrayList<UserAll>();
            if(listMap!=null&&listMap.size()>0){
                for(int i=0;i<listMap.size();i++){
                    UserAll userAll=new UserAll();
                    PropertyUtils.copyProperties(userAll, listMap.get(i));
                    listuasrall.add(userAll);
                }

            }
            new ExportExcel("用户数据", UserAll.class).setDataList(listuasrall).write(response, fileName).dispose();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     *
     * @author - 门光耀
     * @message- 商家信息查询
     * @createDate - 2015-3-2
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/selectusercount")
    @ResponseBody
    public Json<Page<Map<String,Object>>> selectuserCount(HttpServletRequest request){
        Json json=new Json();
        UserDTO userdto=new UserDTO();
        com.camelot.openplatform.common.Pager pager=new com.camelot.openplatform.common.Pager();
        //设置分页
        pager.setPage(1);
        pager.setRows(1);
        //获取查询条件
        String uid=request.getParameter("uid");
        if(uid!=null&&!"".equals(uid)){
            userdto.setUid(new Long(uid));
        }
        String quickType=request.getParameter("quickType");
        if(quickType!=null&&!"".equals(quickType)){
            userdto.setQuickType(new Integer(quickType));
        }
        String userstatus=request.getParameter("userstatus");
        if(userstatus!=null&&!"".equals(userstatus)){
            userdto.setUserstatus(new Integer(userstatus));
        }
        userdto.setUname(request.getParameter("uname"));
        userdto.setNickname(request.getParameter("nickname"));
        userdto.setCompanyName(request.getParameter("companyName"));
        String createTimeBegin=request.getParameter("createTimeBegin");
        userdto.setCreateTimeBegin(createTimeBegin);
        String createTimeEnd=request.getParameter("createTimeEnd");
      //所属平台id
        String platformId=request.getParameter("platformId");
        if(platformId!=null&&!"".equals(platformId)){
        	userdto.setPlatformId(new Integer(platformId));
        }
        if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
            try{
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                Calendar   calendar   =   new   GregorianCalendar();
                calendar.setTime(simpleDateFormat.parse(createTimeEnd));
                calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                Date end=calendar.getTime();
                userdto.setCreateTimeEnd(simpleDateFormat.format(end));
            }catch(Exception e){
                json.setSuccess(false);
            }
        }
        DataGrid<UserDTO> datagrid=userExportService.findUserListByCondition(userdto,null,pager);
        Page<Map<String,Object>> page = new Page<Map<String,Object>>();
        page.setCount(datagrid.getTotal());
        json.setObj(page);
        json.setSuccess(true);
        json.setMsg(page.toString());
        return json;
    }
}
