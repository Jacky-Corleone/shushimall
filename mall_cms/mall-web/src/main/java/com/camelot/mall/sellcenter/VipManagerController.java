package com.camelot.mall.sellcenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.ReportQueryDTO;
import com.camelot.report.dto.ShopCustomerReportDTO;
import com.camelot.report.service.TradeReportService;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserCenterOperaService;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [会员管理]</p>
 * Created on 2015年4月8日
 * @author  <a href="mailto: menguangyao@camelotchina.com">门光耀</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/sellcenter/vipmanager")
public class VipManagerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VipManagerController.class);
    @Resource
    private UserExportService userExportService;
    @Resource
    private AddressBaseService addressBaseService;
    @Resource
	private AddressInfoService addressInfoService;
    @Resource
    private UserExtendsService userExtendsService;
    @Resource
    private UserCompanyService userCompanyService;
    @Resource
    private TradeReportService tradeReportService;
    @Resource
	private UserCenterOperaService userCenterOperaService;
    /**
     * <p>Description: [会员管理查询]</p>
     * Created on 2015年4月8日
     * @author  门光耀
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/vipselect")
    public String sellectYM(HttpServletRequest request, Model model){
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        if(page==null||"".equals(page)){
            page="1";
        }
        if(rows==null||"".equals(rows)){
            rows="10";
        }
        Pager<ShopCustomerReportDTO> pager=new Pager<ShopCustomerReportDTO>();
        pager.setRows(new Integer(rows));
        pager.setPage(new Integer(page));
        pager.setTotalCount(0);
        //获取店铺id
        Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
        //获取店铺会员查询条件
        ReportQueryDTO queryDto = new ReportQueryDTO();
        queryDto.setShopId(shopId);
        //获取买过当前卖家商品的用户，这些用户默认都为该店铺的会员
//        DataGrid<UserDTO> datagrid=userExportService.queryUserListByCondition(userDTO,null,null,pager);
        DataGrid<ShopCustomerReportDTO> customerData = tradeReportService.queryShopCustomerList(queryDto, pager);
        if(customerData!=null&&customerData.getTotal()!=null){
        	//解析公司地址，由地址代码转为名称
        	for(int i = 0 ; i < customerData.getRows().size() ;i++ ){
        		ShopCustomerReportDTO customerInfo = customerData.getRows().get(i);
        		String code = customerInfo.getAddress();
        		if(!StringUtils.isEmpty(code)){
        			String[] codeArray = code.split(",");
        			String address = queryDiYu(codeArray);
        			customerData.getRows().get(i).setAddress(address.substring(0,address.length()-1));
        		}
        		
        	}
        	pager.setRecords(customerData.getRows());
            pager.setTotalCount(customerData.getTotal().intValue());
        }
        model.addAttribute("pageNo",new Integer(page));
        model.addAttribute("pager",pager);

        return "/sellcenter/vipmanager/vipmanager";
    }
    @RequestMapping("/vipselectjson")
    @ResponseBody
    public Map<String,Object> selectJson(HttpServletRequest request){
        Map<String,Object> json=new HashMap<String, Object>();
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        if(page==null||"".equals(page)){
            page="1";
        }
        if(rows==null||"".equals(rows)){
            rows="10";
        }
        Pager<ShopCustomerReportDTO> pager=new Pager<ShopCustomerReportDTO>();
        pager.setRows(new Integer(rows));
        pager.setPage(new Integer(page));
        pager.setTotalCount(0);
        //根据查询条件查询数据
        String userName=request.getParameter("userName");
        String companyName=request.getParameter("companyName");
        String sheng=request.getParameter("sheng");
        String shi=request.getParameter("shi");
        String qu=request.getParameter("qu");
        String createTimeStart=request.getParameter("createTimeStart");
        String createTimeEnd=request.getParameter("createTimeEnd");
        //如果为true表明前台已经输入了查询条件
        boolean ifse=false;
        ReportQueryDTO queryDto = new ReportQueryDTO();
        //获取店铺id
        Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
        //获取店铺会员查询条件
        queryDto.setShopId(shopId);
        UserDTO userDto=new UserDTO();
        if(userName!=null&&!"".equals(userName)){
        	queryDto.setUserName(userName);
            ifse=true;
        }
        if(companyName!=null&&!"".equals(companyName)){
        	queryDto.setCompanyName(companyName);
            ifse=true;
        }
        String addr="";
        if(sheng!=null&&!"".equals(sheng)){
            addr=sheng;
        }
        if(shi!=null&&!"".equals(shi)){
            addr=addr+","+shi;
        }
        if(qu!=null&&!"".equals(qu)){
            addr=addr+","+qu;
        }
        if(addr!=null&&!"".equals(addr)){
        	queryDto.setCompanyAddress(addr);
            ifse=true;
        }
        	
    	SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotBlank(createTimeStart)){
			Date createdstrDate = null;
			try {
				createdstrDate = sdf.parse(createTimeStart);
			} catch (ParseException e) {
				LOGGER.error("会员列表查询，传入的起始日期类型解析失败",e);
			}
			queryDto.setCreateBegin(createdstrDate);
			 ifse=true;
	    }
        	
        	
        if(StringUtils.isNotBlank(createTimeEnd)){
        	Date createdendDate = null;
			try {
				createdendDate = sdf.parse(createTimeEnd);
			} catch (ParseException e) {
				LOGGER.error("会员列表查询，传入的截至日期类型解析失败",e);
			}
			queryDto.setCreateEnd(createdendDate);
			 ifse=true;
        }
        
        DataGrid<ShopCustomerReportDTO> customerData = tradeReportService.queryShopCustomerList(queryDto, pager);
        if(customerData!=null&&customerData.getTotal()!=null){
        	//解析公司地址，由地址代码转为名称
        	for(int i = 0 ; i < customerData.getRows().size() ;i++ ){
        		ShopCustomerReportDTO customerInfo = customerData.getRows().get(i);
        		String code = customerInfo.getAddress();
        		if(!StringUtils.isEmpty(code)){
        			String[] codeArray = code.split(",");
        			String address = queryDiYu(codeArray);
        			customerData.getRows().get(i).setAddress(address.substring(0,address.length()-1));
        		}
        		
        	}
        	pager.setRecords(customerData.getRows());
            pager.setTotalCount(customerData.getTotal().intValue());
        }
        json.put("msg", "查询成功");
        json.put("success",true);
        json.put("obj", pager);
        return json;
    }
    /**
     * <p>Description: [会员管理查询明细]</p>
     * Created on 2015年4月9日
     * @author  门光耀
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/vipuserdetial")
    @ResponseBody
    public Map<String,Object> vipUserDetial(Long uid) {
        Map<String,Object> by=new HashMap<String,Object>();
        if(uid==null){
        	by.put("success",false);
            by.put("msg","获取失败");
            return by;
        }
        Pager pager=new Pager();
        pager.setPage(1);
        pager.setRows(1);
        //获取会员详细信息
        ExecuteResult<UserInfoDTO>  executeResult = userExtendsService.findUserInfo(uid);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        //如果没有查询出数据
        if(executeResult.getResult()==null){
        	//说明当前这个会员没有扩展信息
        	//查询这个会员的基本信息
        	UserDTO userDTO = userExportService.queryUserById(uid);
        	userInfoDTO.setUserDTO(userDTO);
        }else{
        	userInfoDTO =  executeResult.getResult();
        	//解析公司地址，由地址编码转换为地址名称
        	String code = userInfoDTO.getUserBusinessDTO().getCompanyAddress();
        	if(code!=null&&!code.equals("")){
        		String[] codeArray = code.split(",");
        		String address = this.queryDiYu(codeArray);
        		userInfoDTO.getUserBusinessDTO().setCompanyAddress(address.substring(0,address.length()-1));
        	}
        	
        }
        
        by.put("userInfoDTO", userInfoDTO);
        
        //根据uid获取到用户的收获地址
        DataGrid<AddressInfoDTO>  dataGrid = addressInfoService.queryAddressinfo(uid);
        
        if(dataGrid.getTotal()==0){
        	by.put("success",false);
        	by.put("msg","收获地址获取失败");
        	return by;
        }
        List<AddressInfoDTO> addressInfoDTOList = dataGrid.getRows();
        
        //根据获取的收货地址列表中的省市县街道编码，获取省市县街道名称，组成收获地址
        
        for(int i = 0 ; i <addressInfoDTOList.size() ; i++){
        	AddressInfoDTO addressInfo = addressInfoDTOList.get(i);
        	//获取编码数组
        	String[] codeArray = {addressInfo.getProvicecode(),addressInfo.getCitycode(),addressInfo.getCountrycode(),addressInfo.getTowncode(),addressInfo.getVillagecode()};
        	String address = queryDiYu(codeArray);
        	if(address.equals("")){
        		addressInfoDTOList.get(i).setFulladdress("");
        	}else{
        		addressInfoDTOList.get(i).setFulladdress(address+addressInfo.getFulladdress());
        	}
        }
        
        by.put("addressInfoDTOList", addressInfoDTOList);
    	by.put("success",true);
        by.put("msg","获取成功");
        return by;
        
    }
    /**
     * 根据地域code获取String 型name
     * @author - 门光耀
     * @createDate - 2015-3-9
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    private String queryDiYu(String[] codeArray){
    	String fullAddress ="";
    	
    	ExecuteResult<List<AddressBaseDTO>> result = addressBaseService.queryNameByCode(codeArray);
    	
    	if(result.isSuccess()){
        	for(int j = 0 ; j < result.getResult().size();j++){
        		fullAddress = fullAddress+result.getResult().get(j).getName()+",";
        	}
    	}
    	
    	return fullAddress;
    }
    /**
     * <p>Description: [会员管理]</p>
     * Created on 2015年4月8日
     * @author  <a href="mailto: menguangyao@camelotchina.com">门光耀</a>
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/vipdetail")
    @ResponseBody
    public Map<String,Object> deposit(HttpServletRequest request) {
        return null;
    }
    
    /**
     * 跳转到子主账号切换申请列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/toSwitchList")
	public String toAcctSwitchList(HttpServletRequest request, Model model){
    	Long uid = WebUtil.getInstance().getUserId(request);
    	UserAuditLogDTO userAuditLog = new UserAuditLogDTO();
		userAuditLog.setAuditId(uid);
		List<UserAuditLogDTO> listRecord = userCenterOperaService.findListByCondition(userAuditLog, UserEnums.UserOperaType.ZFZHSH);
		model.addAttribute("listRecord", listRecord);
		
    	return "/sellcenter/vipmanager/accountswitchlist";
	}
    
    /**
     * 切换主子账号
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/accountSwitch")
    @ResponseBody
	public Map<String, String> acctSwitch(Long childAccountUid, HttpServletRequest request, Model model){
    	Map<String, String> result = new HashMap<String, String>();
		result.put("result","success");
    	Long parentAccountUid = WebUtil.getInstance().getUserId(request);
    	UserDTO userDTO = new UserDTO();
    	userDTO.setUid(childAccountUid);
    	userDTO.setParentId(parentAccountUid);
    	ExecuteResult<UserDTO> retVal = userCenterOperaService.changeParentAndSonAccount(userDTO);
		
    	if(!retVal.isSuccess()){
    		result.put("result", "failure");
    	}
    	return result;
	}
}