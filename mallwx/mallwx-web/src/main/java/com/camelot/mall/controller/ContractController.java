package com.camelot.mall.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.ContractMatDTO;
import com.camelot.goodscenter.dto.ContractPaymentTermDTO;
import com.camelot.goodscenter.dto.ContractUrlShowDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.contract.ContractSearchModel;
import com.camelot.mall.contract.ContractService;
import com.camelot.mall.contract.ContractStatus;
import com.camelot.mall.orderWx.ChildUserListPost;
import com.camelot.mall.orderWx.SendWeiXinMessage;
import com.camelot.mall.service.impl.CommonServiceImpl;
import com.camelot.mall.shopcart.Product;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.ChildUserDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserStorePermissionExportService;
import com.camelot.usercenter.service.UserWxExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.SendWeiXinMessageUtil;
import com.camelot.util.WebUtil;
import com.camelot.util.WeiXinMessageModeId;


/**
 * 
 * <p>Description: [描述该类概要功能介绍:协议页面控制器]</p>
 * Created on 2015/5/28
 * @author  <a href="mailto: zihanmin@camelotchina.com">訾瀚民</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */

@Controller
@RequestMapping("/contract")
public class ContractController {
    @Resource
    private ContractService contractService;
    @Resource
    private ProtocolExportService protocolExportService;
    @Resource
    private UserExportService userExportService;
    @Resource
    private SearchItemExportService searchItemExportService;
    @Resource
    private CommonServiceImpl commonServiceImpl;
    @Resource
    private UserWxExportService userWxExportService;
    @Resource
    private RedisDB redisDB;
    @Resource
    private ItemExportService itemService;
    @Resource
    private UserStorePermissionExportService userStorePermissionExportService;
    
    @Resource
    private TradeOrderExportService tradeOrderExportService;
    
    
    

    private Logger LOG = Logger.getLogger(this.getClass());
    
    /**
     * 
     * <p>Discription:[方法功能中文描述:年度协议 跳转到年度协议页面,根据查询条件进行特定协议查询]</p>
     * Created on 2016年1月28日
     * @param model
     * @param request
     * @param showMenu
     * @param contractSearchModel 页面中输入的查询条件,根据页面传来的json进行转换,如不为Null则根据查询条件加载协议
     * @param sourcePage
     * @return
     * @author:[王帅]
     */
    @RequestMapping("/contractPage")
    public String contrackPage(Model model, HttpServletRequest request,
                               @RequestParam(value = "showMenu", required = false, defaultValue = "") String showMenu,
                               String contractSearchModel,
                               String sourcePage) {

        // 设置Pager对象
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        //用户未登录跳转登录页面
        if (registerDTO == null) {
            return "redirect:/";
        }
        ContractInfoDTO conInfo = new ContractInfoDTO();
        conInfo.setActiveFlag("1");
        List<String> idList = new ArrayList<String>();

        //判断传入参数，传入条件不为空，依靠条件查询
        ContractSearchModel searchModel = JSONObject.parseObject(contractSearchModel, ContractSearchModel.class);
        if (searchModel != null) {
            if (StringUtils.isNotEmpty(searchModel.getContractName())) {
                conInfo.setContractName(searchModel.getContractName());
            }
            if (StringUtils.isNotEmpty(searchModel.getItemName())) {
                conInfo.setItemName(searchModel.getItemName());
            }
            //根据公司名称查询
            if (StringUtils.isNotEmpty(searchModel.getCompanyName())) {
                UserDTO userDTO = new UserDTO();
                userDTO.setCompanyName(searchModel.getCompanyName());
                //先模糊查询输入的公司名称,将有关的公司下属用户角色查询出来
                List<UserDTO> userList = userExportService.findUserListByCondition(userDTO, null, null).getRows();
                //将查询出来的用户ID进行整理放入同一个list中
                if (userList.size() > 0) {
                    for (UserDTO temp : userList) {
                        idList.add(temp.getUid().toString());
                    }
                } else {
                    idList.add("没有这么个ID");
                }
            }
        }
        //获取主子帐号  
        ExecuteResult<List<Long>> userIdList = userExportService.queryUserIds(registerDTO.getUid());
      //根据用户进入的买/卖中心设置查询条件
        if ("seller".equals(sourcePage)) {
            //卖家中心进入协议页面的需要设置卖方为当前登录用户进行查询
        	if(userIdList.getResult() != null){
            	List<String> supplierIdList = new ArrayList<String>();
            	for(Long userId : userIdList.getResult()){
            		supplierIdList.add(userId.intValue() + "");
            	}
            	conInfo.setSupplierIdList(supplierIdList);
            	conInfo.setSupplierId(null);
            }else{
            	conInfo.setSupplierId(registerDTO.getUid().intValue());
            }
            //如果输入了公司名称查询条件,从卖家中心进入协议页面的应设置查询条件中的公司名称下的用户列表为买方
            if (idList.size() > 0) {
                conInfo.setPrinterIdList(idList);
            }
        }
        if ("buyer".equals(sourcePage)) {
            //从买家中心进入协议页面的需要设置买方为当前登录用户进行查询
            if(userIdList.getResult() != null){
            	List<String> printerIdList = new ArrayList<String>();
            	for(Long userId : userIdList.getResult()){
            		printerIdList.add(userId.intValue() + "");
            	}
            	conInfo.setPrinterIdList(printerIdList);
            	conInfo.setUserList(userIdList.getResult());
            	conInfo.setPrinterId(null);
            }else{
            	conInfo.setPrinterId(registerDTO.getUid().intValue());
            }
            //如果输入了公司名称查询条件,从买家中心进入协议页面的应设置查询条件中的公司名称下的用户列表为卖方
            if (idList.size() > 0) {
                conInfo.setSupplierIdList(idList);
            }
        }

        //进行数据查询及组装
        List<ContractInfoDTO> conInfos = contractService.assembleContracInfos(conInfo);
        //获取物品搜索分类信息
        JSONArray ja = commonServiceImpl.findCategory();
        model.addAttribute("itemList", ja);
        if (StringUtils.isEmpty(showMenu)) {
            model.addAttribute("showMenu", 0);
        } else {
            model.addAttribute("showMenu", 1);
        }
        model.addAttribute("conInfos", conInfos);
        model.addAttribute("register", registerDTO);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
        showApproveList1(request, model);
        //根据查询条件判断需要跳转的页面
        if (contractSearchModel != null && !"".equals(contractSearchModel)) {
            return "/contract/contractInfoList";
        }
        return "/contract/contractPage";
    }

    /**
     * 
     * <p>Discription:[方法功能中文描述:获取协议详情（物品信息） 根据协议进行查询并展示协议详细信息]</p>
     * Created on 2016年1月28日
     * @param pager
     * @param contractId
     * @param model
     * @return
     * @author:[王帅]
     */
    
    @RequestMapping("/getContractDetail")
    public String getContractDetail(Pager<ContractMatDTO> pager, String contractId, Model model) {
        //创建协议查询DTO
        ContractInfoDTO contractInfoDTO = new ContractInfoDTO();
        //创建协议账期查询DTO
        ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
        //创建协议物品查询DTO
        ContractMatDTO contractMatDTO = new ContractMatDTO();
        //创建物品详情Map
        List<Map> matTemp = new ArrayList<Map>();
        //设置协议ID并根据ID进行查询
        contractInfoDTO.setId(Long.valueOf(contractId));
        ContractInfoDTO result = protocolExportService.queryByContractInfo(contractInfoDTO).getResult();
        
        
        //物品与账期信息都是通过协议编号与协议主表进行关联的,因此用协议编号进行物品及账期查询
        contractMatDTO.setContractNo(result.getContractNo());
        //物品与账期可能存在删除或修改的情况,需要查询当前有效的物品及账期信息因此设置有效性标识为1
        contractMatDTO.setActiveFlag("1");
        contractPaymentTermDTO.setContractNo(result.getContractNo());
        contractPaymentTermDTO.setActiveFlag("1");
        pager.setRows(Integer.MAX_VALUE);
        //根据协议编号查询协议物品详细信息
        ExecuteResult<DataGrid<Map>> mats = protocolExportService.queryContractMatList(contractMatDTO, pager);
        if (null != mats.getResult().getRows()) {
            matTemp = mats.getResult().getRows();
            for (Map e : matTemp) {
                //增加销售属性字段
                contractService.putSalerAttr(e);
            }
        }
        UserDTO approveBy = new UserDTO();
        result.setContractMatDTOs(matTemp);
        ContractPaymentTermDTO contractPayment = protocolExportService.queryByContractPaymentTerm(contractPaymentTermDTO).getResult();
        UserDTO seller = userExportService.queryUserById(result.getSupplierId());
        /**
         * 判断登入账号是否是子账号
         */
        UserDTO  buyercx = userExportService.queryUserById(result.getPrinterId());//查询是否是父账号
        UserDTO buyer=null;
        	  if(buyercx.getParentId()==null){
        		  buyer = userExportService.queryUserById(result.getPrinterId());//是父账号查询内容
        	  }else{
        		  buyer = userExportService.queryUserByfId(result.getPrinterId());//子账号查询内容
        	  }
        UserDTO publishedBy = userExportService.queryUserById(Long.valueOf(result.getCreateBy()));
        if(StringUtils.isNotBlank(result.getApproveBy())){
            approveBy = userExportService.queryUserById(Long.valueOf(result.getApproveBy()));
        }
        
        String fileName = null;
        fileName = result.getAnnex().substring(result.getAnnex().lastIndexOf("/") + 1);
        ContractUrlShowDTO contractUrlShowDTO =new ContractUrlShowDTO();
        contractUrlShowDTO.setContractInfoId(result.getContractNo());
        ExecuteResult<DataGrid<ContractUrlShowDTO>> contractUrlShowList = protocolExportService.queryContractUrlShow(contractUrlShowDTO);
        model.addAttribute("contractUrlShowList", contractUrlShowList.getResult().getRows());
        model.addAttribute("ftpServerDir",SysProperties.getProperty("ftp_server_dir"));
        model.addAttribute("seller", seller);
        model.addAttribute("buyer", buyer);
        model.addAttribute("publishedBy", publishedBy);
        model.addAttribute("approveBy", approveBy);
        model.addAttribute("contractPayment", contractPayment);
        //通过协议状态枚举类获取协议状态
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("contract", result);
        return "/contract/contractInfoDetail";
    }


    /**
     * 
     * <p>Discription:[方法功能中文描述:协议下单跳转]</p>
     * Created on 2016年1月28日
     * @param pager
     * @param contractId
     * @param model
     * @return
     * @author:[王帅]
     */
    
    @RequestMapping("/contractOrder")
    public String contractOrder(Pager<ContractMatDTO> pager, String contractId, Model model) {
        //用户未登录跳转登录页面
        ContractInfoDTO contractInfoDTO = new ContractInfoDTO();
        ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
        ContractMatDTO contractMatDTO = new ContractMatDTO();
        List<Map> matTemp = new ArrayList<Map>();
        contractInfoDTO.setId(Long.valueOf(contractId));
        ContractInfoDTO result = protocolExportService.queryByContractInfo(contractInfoDTO).getResult();
        contractMatDTO.setContractNo(result.getContractNo());
        contractMatDTO.setActiveFlag("1");
        contractPaymentTermDTO.setContractNo(result.getContractNo());
        pager.setRows(Integer.MAX_VALUE);
        //根据协议编号查询协议信息
        //为协议物品增加销售属性
        ExecuteResult<DataGrid<Map>> mats = protocolExportService.queryContractMatList(contractMatDTO, pager);
        if (null != mats.getResult() && null != mats.getResult().getRows()) {
            matTemp = mats.getResult().getRows();
            for (Map e : matTemp) {
                contractService.putSalerAttr(e);
            }
        }
        
        result.setContractMatDTOs(matTemp);
        
        //协议类型等于3时，查询协议总价值
        if(result.getProtocolType().equals("3")){
            ExecuteResult<List<TradeOrderItemsDTO>> countCostList =tradeOrderExportService.countCost(result.getContractNo());
            model.addAttribute("countCostList", countCostList.getResult());
        	
        }
        //协议类型等于2时，查询协议总数量
        if(result.getProtocolType().equals("2")){
        	ExecuteResult<List<TradeOrderItemsDTO>> countCostList =tradeOrderExportService.countNumber(result.getContractNo());
            model.addAttribute("countCostList", countCostList.getResult());
        	
        }
        
        
        ContractPaymentTermDTO contractPayment = protocolExportService.queryByContractPaymentTerm(contractPaymentTermDTO).getResult();
        //判断当前登录用户的类型
        UserDTO seller = userExportService.queryUserById(result.getSupplierId());
        UserDTO buyer = userExportService.queryUserById(result.getPrinterId());

        model.addAttribute("seller", seller);
        model.addAttribute("buyer", buyer);
        model.addAttribute("contractPayment", contractPayment);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("contract", result);
        return "/contract/contractOrder";
    }
    /**
     * 
     * <p>Discription:[方法功能中文描述:获取买卖双方信息]</p>
     * Created on 2016年1月28日
     * @param company
     * @param model
     * @param uType
     * @param pager
     * @return
     * @author:[王帅]
     */
    @RequestMapping("/getCompanyNameList")
    public String getPartBDetail(String company, Model model, Integer uType, Pager<UserDTO> pager) {
        UserDTO userSearchDTO = new UserDTO();
        DataGrid<UserDTO> users = new DataGrid<UserDTO>();
        pager.setRows(5);
        //根据传入的用户类型进行用户信息的查询
        if (uType != null) {
            //3为认证卖家，查询谁卖家的信息
            if (uType == 3) {

                userSearchDTO.setUsertype(3);
                //如果传入的公司条件不为空则按照公司名称查询卖家信息
                if (StringUtils.isNotEmpty(company)) {
                    userSearchDTO.setCompanyName(company);
                }
                users = userExportService.findUserListPid(userSearchDTO, null, pager);
            }
            //类型为2应该查询买家信息,但是只要是注册用户即可为买家包括认证卖家,所以不设置用户类型
            if (uType == 2) {
                //如果传入的公司条件不为空则按照公司名称查询买家信息
                if (StringUtils.isNotEmpty(company)) {
                    userSearchDTO.setCompanyName(company);
                }
                users = userWxExportService.queryUser(userSearchDTO, pager).getResult();
            }
            pager.setRecords(users.getRows());
            pager.setTotalCount(users.getTotal().intValue());
            model.addAttribute("pager", pager);
            model.addAttribute("companyList", users);
        }
        return "/contract/companyNameList";
    }
    /**
     * 
     * <p>Discription:[方法功能中文描述:创建协议]</p>
     * Created on 2016年1月28日
     * @param request
     * @param response
     * @param contractInfo
     * @param contractMatDTOs
     * @param contractPaymentTerm
     * @param writer
     * @param sourcePage
     * @param annexcc
     * @author:[王帅]
     */
    @RequestMapping("/contractAdd")
    public void contractCreateAdd(HttpServletRequest request,HttpServletResponse response,
                                  String contractInfo, String contractMatDTOs, String contractPaymentTerm,
                                  PrintWriter writer, String sourcePage,String annexcc) {
        ExecuteResult<String> result = new ExecuteResult<String>();
        List<ContractMatDTO> contractMats = JSONObject.parseArray(contractMatDTOs, ContractMatDTO.class);
        ContractInfoDTO contractInfoDTO = JSONObject.parseObject(contractInfo, ContractInfoDTO.class);
        ContractPaymentTermDTO contractPaymentTermDTO = JSONObject.parseObject(contractPaymentTerm, ContractPaymentTermDTO.class);
        ContractUrlShowDTO contractUrlShowDTO = new ContractUrlShowDTO();
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        //用户未登录跳转登录页面
        if (registerDTO == null) {
            return;
        }
        contractInfoDTO.setContractNo(protocolExportService.createContractNo().getResult());
        contractInfoDTO.setCreateBy(registerDTO.getUid().toString());
        if ("buyer".equals(sourcePage)) {
            if (contractInfoDTO.getSupplierId() == null ) {
                result.addErrorMessage("卖方为必填字段，请选择卖方");
            }
            contractInfoDTO.setCreateRole(1);
        }
        if ("seller".equals(sourcePage)) {
            //数据必填字段验证
            if (contractInfoDTO.getPrinterId() == null ) {
                result.addErrorMessage("买方为必填字段，请选择买方");
            }
            contractInfoDTO.setCreateRole(0);
        }
        //合同物品验证
        if (contractMats.size() == 0) {
            result.addErrorMessage("请选择合同物品");
        }
        if (contractInfoDTO.getBeginDate() == null || contractInfoDTO.getEndDate() == null) {
            result.addErrorMessage("合同日期必须设置");
        } else if (contractInfoDTO.getBeginDate().compareTo(contractInfoDTO.getEndDate()) == 0) {
            result.addErrorMessage("合同开始与结束日期不能相同");
        } else if (contractInfoDTO.getBeginDate().compareTo(contractInfoDTO.getEndDate()) > 0) {
            result.addErrorMessage("合同开始日期不能大于结束日期");
        }

        ContractInfoDTO searchDTO = new ContractInfoDTO();
        searchDTO.setContractNo(contractInfoDTO.getContractNo());
        ExecuteResult<ContractInfoDTO> testResult = protocolExportService.queryByContractInfo(searchDTO);
        if (null != testResult.getResult()) {
            result.addErrorMessage("该协议已存在");
        }

        if (!result.isSuccess()) {
            writer.write(JSON.toJSONString(result));
            return;
        }
        if ("seller".equals(sourcePage)) {
            contractInfoDTO.setSupplierId(registerDTO.getUid().intValue());
            contractInfoDTO.setConfirmBy(contractInfoDTO.getPrinterId().toString());

        }
        if ("buyer".equals(sourcePage)) {
            contractInfoDTO.setPrinterId(registerDTO.getUid().intValue());
            contractInfoDTO.setConfirmBy(contractInfoDTO.getSupplierId().toString());
        }
        contractInfoDTO.setActiveFlag("1");
        contractInfoDTO.setProtocolType(contractInfoDTO.getProtocolType());
        if (StringUtils.isEmpty(contractInfoDTO.getStatus())) {
            contractInfoDTO.setStatus("0");
        }
        //设置合同号
        contractPaymentTermDTO.setContractNo(contractInfoDTO.getContractNo());
        contractPaymentTermDTO.setActiveFlag("1");
        contractPaymentTermDTO.setCreateBy(registerDTO.getUid().toString());
        //执行创建协议动作
        protocolExportService.addContractInfo(contractInfoDTO);
        //执行协议创建协议账期操作
        result = protocolExportService.addContractPaymentTerm(contractPaymentTermDTO);
        //执行协议物品增加操作
        for (ContractMatDTO matDTO : contractMats) {
            matDTO.setContractNo(contractInfoDTO.getContractNo());
            matDTO.setCreateBy(registerDTO.getUid().toString());
            matDTO.setActiveFlag("1");
            //协议类型
            matDTO.setProtocolType(contractInfoDTO.getProtocolType());
            result = protocolExportService.addContractMat(matDTO);
        }
        if(StringUtils.isNotEmpty(annexcc)){
            String annexurlList=annexcc.substring(0, annexcc.length()-1);
           	String str[] = annexurlList.split(",");  
        	for (int i = 0; i < str.length; i++) { 
	            contractUrlShowDTO.setIsDelete("0");
	            contractUrlShowDTO.setContractInfoId(contractInfoDTO.getContractNo());
	            contractUrlShowDTO.setImgUrl(str[i]);
	            result = protocolExportService.addcontractUrlShow(contractUrlShowDTO);
        	}
        	
        }
        writer.write(JSON.toJSONString(result));
        return;
    }
    /**
     * 
     * <p>Discription:[方法功能中文描述：获取用户信息(暂时未用到)]</p>
     * Created on 2016年1月28日
     * @param userDTO
     * @param model
     * @param pager
     * @return
     * @author:[王帅]
     */
    @RequestMapping("/getConectorList")
    public String getConectorList(UserDTO userDTO, Model model, Pager<UserDTO> pager) {
        DataGrid<UserDTO> conector = userExportService.findUserListByCondition(userDTO, null, pager);
        pager.setTotalCount(conector.getTotal().intValue());
        pager.setRecords(conector.getRows());
        model.addAttribute("pager", pager);
        return "/contract/conectorList";
    }


    /**
     * 
     * <p>Discription:[方法功能中文描述：跳转协议修改页面]</p>
     * Created on 2016年1月28日
     * @param request
     * @param sourcePage
     * @param showMenu
     * @param model
     * @param contractId
     * @param pager
     * @return
     * @author:[王帅]
     */ 
    @RequestMapping("/toContractUpdate")
    public String contractModify(HttpServletRequest request, String sourcePage,
                                 @RequestParam(value = "showMenu", required = false, defaultValue = "") String showMenu,
                                 Model model, String contractId, Pager<Map> pager) {
        //创建查询数据
        ContractInfoDTO contractInfoDTO = new ContractInfoDTO();
        ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
        ContractMatDTO contractMatDTO = new ContractMatDTO();
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        //使用合同号进行数据查询
        contractInfoDTO.setId(Long.valueOf(contractId));
        ExecuteResult<ContractInfoDTO> results = protocolExportService.queryByContractInfo(contractInfoDTO);
        contractMatDTO.setContractNo(results.getResult().getContractNo());
        contractMatDTO.setActiveFlag("1");
        contractPaymentTermDTO.setContractNo(results.getResult().getContractNo());
        contractPaymentTermDTO.setActiveFlag("1");
        pager.setRows(Integer.MAX_VALUE);
        JSONArray ja = commonServiceImpl.findCategory();
        model.addAttribute("itemList", ja);
        if (StringUtils.isEmpty(showMenu)) {
            model.addAttribute("showMenu", 0);
        } else {
            model.addAttribute("showMenu", 1);
        }
        //查询协议信息
        showApproveList1(request, model);
        //查询买卖双方用户信息
        UserDTO user = userExportService.queryUserById(results.getResult().getPrinterId().longValue());
        UserDTO seller = userExportService.queryUserById(results.getResult().getSupplierId().longValue());
        //查询账期及物品
        String fileName = null;
        //附件连接地址不为空说明有上传的附件,因此获取文件名字,以便前台展示
//        if (null != results.getResult().getAnnex()) {
         fileName = results.getResult().getAnnex().substring(results.getResult().getAnnex().lastIndexOf("/") + 1);
//        }
        //附件连接地址不为空说明有上传的附件,因此获取文件名字,以便前台展示
        ContractUrlShowDTO contractUrlShowDTO =new ContractUrlShowDTO();
        contractUrlShowDTO.setContractInfoId(results.getResult().getContractNo());
        ExecuteResult<DataGrid<ContractUrlShowDTO>> contractUrlShowList = protocolExportService.queryContractUrlShow(contractUrlShowDTO);
        model.addAttribute("contractUrlShowList", contractUrlShowList.getResult().getRows());
    	model.addAttribute("ftpServerDir",SysProperties.getProperty("ftp_server_dir"));

    	
      
        //查询账期信息及物品信息
        ExecuteResult<ContractPaymentTermDTO> paymentTermDTO = protocolExportService.queryByContractPaymentTerm(contractPaymentTermDTO);
        ExecuteResult<DataGrid<Map>> contractMat = protocolExportService.queryContractMatList(contractMatDTO, pager);
        //增加物品销售属性
        for (Map e : contractMat.getResult().getRows()) {
            contractService.putSalerAttr(e);
        }
        pager.setTotalCount(contractMat.getResult().getTotal().intValue());
        pager.setRecords(contractMat.getResult().getRows());
        model.addAttribute("contractMat", contractMat.getResult().getRows());
        model.addAttribute("register", registerDTO);
        model.addAttribute("user", user);
        model.addAttribute("seller", seller);
        model.addAttribute("fileName", fileName);
        model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("paymentTermDTO", paymentTermDTO.getResult());
        model.addAttribute("contractInfo", results.getResult());
        model.addAttribute("protocolTypeId", results.getResult().getProtocolType());

        return "/contract/contractUpdatePage";
    }
    /**
     * 
     * <p>Discription:[方法功能中文描述：修改协议]</p>
     * Created on 2016年1月28日
     * @param request
     * @param contractInfo
     * @param contractMatDTOs
     * @param contractPaymentTerm
     * @param writer
     * @param removeMat
     * @param needApprove
     * @param model
     * @param annexcc
     * @author:[王帅]
     */
    @RequestMapping("/contractUpdate")
    public void contractUpdate(HttpServletRequest request, String contractInfo,
                               String contractMatDTOs, String contractPaymentTerm, PrintWriter writer,
                               String removeMat, String needApprove, Model model,String annexcc) {
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        //用户未登录跳转登录页面
        if (registerDTO == null) {
            return;
        }

        ExecuteResult<String> result = new ExecuteResult<String>();
        List<ContractMatDTO> contractMats = JSONObject.parseArray(contractMatDTOs, ContractMatDTO.class);
        ContractInfoDTO contractInfoDTO = JSONObject.parseObject(contractInfo, ContractInfoDTO.class);
        ContractPaymentTermDTO contractPaymentTermDTO = JSONObject.parseObject(contractPaymentTerm, ContractPaymentTermDTO.class);
        List<Long> removeMatIds = JSONObject.parseArray(removeMat, Long.class);
        //合同物品验证
        if (contractMats.get(0) == null) {
            result.addErrorMessage("请选择合同物品");
        }
        //数据必填字段验证
        if (contractInfoDTO.getPrinterId() == null ) {
            result.addErrorMessage("印刷厂为必填字段，请选择印刷厂");
        }
        if (contractInfoDTO.getSupplierId() == null ) {
            result.addErrorMessage("供应商为必填字段，请选择供应商");
        }
        if (contractInfoDTO.getBeginDate() == null || contractInfoDTO.getEndDate() == null) {
            result.addErrorMessage("合同日期必须设置");
        } else if (contractInfoDTO.getBeginDate().compareTo(contractInfoDTO.getEndDate()) == 0) {
            result.addErrorMessage("合同开始与结束日期不能相同");
        } else if (contractInfoDTO.getBeginDate().compareTo(contractInfoDTO.getEndDate()) > 0) {
            result.addErrorMessage("合同开始日期不能大于结束日期");
        }

        if (!result.isSuccess()) {
            writer.write(JSON.toJSONString(result));
            return;
        }
        contractInfoDTO.setActiveFlag("1");
        contractInfoDTO.setUpdateBy(registerDTO.getUid().toString());
        contractInfoDTO.setUpdateDate(new Date());
        contractPaymentTermDTO.setActiveFlag("1");
        ContractUrlShowDTO contractUrlShowDTO = new ContractUrlShowDTO();
        protocolExportService.deleteContractUrlShow(contractInfoDTO.getContractNo());
        if(StringUtils.isNotEmpty(annexcc)){
            String annexurlList=annexcc.substring(0, annexcc.length()-1);
           	String str[] = annexurlList.split(",");  
        	for (int i = 0; i < str.length; i++) { 
	            contractUrlShowDTO.setIsDelete("0");
	            contractUrlShowDTO.setContractInfoId(contractInfoDTO.getContractNo());
	            contractUrlShowDTO.setImgUrl(str[i]);
	            result = protocolExportService.addcontractUrlShow(contractUrlShowDTO);
        	}	
        }
        //执行修改协议动作
        contractService.updateContractInfos(contractInfoDTO, contractMats, contractPaymentTermDTO, registerDTO, needApprove, removeMatIds);
        model.addAttribute("result", result.getResultMessage());
        writer.write(JSON.toJSONString(result));
        return;
    }
    /**
     * 
     * <p>Discription:[方法功能中文描述:删除协议]</p>
     * Created on 2016年1月28日
     * @param contractId
     * @param sourcePag
     * @return
     * @author:[王帅]
     */
    @RequestMapping("/deleteContractInfo")
    public String deleteContractInfo(String contractId,String sourcePag) {

        ContractInfoDTO temp = new ContractInfoDTO();
        temp.setId(Long.valueOf(contractId));
        ContractInfoDTO resultTemp = protocolExportService.queryByContractInfo(temp).getResult();
        resultTemp.setActiveFlag("0");
        protocolExportService.modifyContractInfo(resultTemp);
        return "redirect:contractPage?sourcePage="+sourcePag;
    }



    /**
     * 
     * <p>Discription:[方法功能中文描述 ：协议审批 包含条件查询及审批操作(同意审批和拒绝审批)通过opration操作符的值来控制程序执行的操作]</p>
     * Created on 2016年1月28日
     * @param request
     * @param contractId 通过传入要执行审批操作的协议ID进行操作
     * @param operation
     * @param contractSearchModel
     * @param reason
     * @param sourcePage
     * @param model
     * @return
     * @author:[王帅]
     */
    @RequestMapping("/approveContractInfo")
    public String approveContractInfo(HttpServletRequest request, String contractId, String operation, String contractSearchModel,
                                      String reason, String sourcePage, Model model) {
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        ContractInfoDTO con = new ContractInfoDTO();
        con.setActiveFlag("1");
        UserDTO userDTO = new UserDTO();
        List<String> idList = new ArrayList<String>();
        //根据用户类型设置查询条件
        con.setApproveBy(registerDTO.getUid().toString());
        con.setStatus("1");
        //判断传入参数，传入条件不为空，依靠条件查询
        ContractSearchModel searchModel = JSONObject.parseObject(contractSearchModel, ContractSearchModel.class);
        if (searchModel != null) {
            //判断searchModel的值,目前只进行了协议名称查询
            if (StringUtils.isNotEmpty(searchModel.getContractName())) {
                con.setContractName(searchModel.getContractName());
            }
            //根据协议中物品进行查询

            if (StringUtils.isNotEmpty(searchModel.getItemName())) {
                con.setItemName(searchModel.getItemName());
            }
            //根据所签署协议的公司名称进行查询
            if (StringUtils.isNotEmpty(searchModel.getCompanyName())) {
                userDTO.setCompanyName(searchModel.getCompanyName());
                List<UserDTO> userList = userExportService.findUserListByCondition(userDTO, null, null).getRows();
                if (null != userList && userList.size() > 0) {
                    for (UserDTO temp : userList) {
                        idList.add(temp.getUid().toString());
                    }
                } else {
                    idList.add("没有这么个ID");
                }
            }
        }
        //根据用户进入的买/卖中心设置查询条件
        if ("seller".equals(sourcePage) && idList.size() > 0) {
            //卖家中心进入协议页面的需要设置卖方为当前登录用户进行查询
            //如果输入了公司名称查询条件,从卖家中心进入协议页面的应设置查询条件中的公司名称下的用户列表为买方
            con.setPrinterIdList(idList);
        }
        if ("buyer".equals(sourcePage) && idList.size() > 0) {
            //从买家中心进入协议页面的需要设置买方为当前登录用户进行查询
            //如果输入了公司名称查询条件,从买家中心进入协议页面的应设置查询条件中的公司名称下的用户列表为卖方
            con.setSupplierIdList(idList);
        }
        //如果传入的合同号列表不为空则执行审批操作
        if (contractId != null && !"".equals(contractId)) {
            ExecuteResult<String> result;
            ContractInfoDTO temp = new ContractInfoDTO();
            temp.setId(Long.valueOf(contractId));
            ContractInfoDTO resultTemp = protocolExportService.queryByContractInfo(temp).getResult();
            if ("拒绝".equals(operation)) {
                resultTemp.setStatus("2");
                resultTemp.setRefusalReason(reason);
            }
            if ("同意".equals(operation)) {
                resultTemp.setStatus("3");
            }
            if ("重新提交".equals(operation)) {
                resultTemp.setStatus("1");
            }
            resultTemp.setApproveDate(new Date());
            result = protocolExportService.modifyContractInfo(resultTemp);
            model.addAttribute("result", result.getResult());
        }
        List<ContractInfoDTO> conInfos = contractService.assembleContracInfos(con);
        model.addAttribute("contracts", conInfos);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        return "/contract/contractApprove";
    }


    /**
     * 
     * <p>Discription:[方法功能中文描述：协议确认]</p>
     * Created on 2016年1月28日
     * @param request
     * @param response
     * @param contractId
     * @param contractSearchModel
     * @param operation
     * @param sourcePage
     * @param reason
     * @param model
     * @return
     * @author:[王帅]
     */
    @RequestMapping("/confirmContractInfo")
    public String confirmContractInfo(HttpServletRequest request,HttpServletResponse response, String contractId, String contractSearchModel,
                                      String operation, String sourcePage, String reason, Model model) {
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        ContractInfoDTO con = new ContractInfoDTO();
        con.setActiveFlag("1");
        UserDTO userDTO = new UserDTO();
        List<String> idList = new ArrayList<String>();
        //根据用户类型设置查询条件
        if ("seller".equals(sourcePage)) {
            con.setSupplierId(registerDTO.getUid().intValue());
            con.setConfirmBy(con.getSupplierId().toString());
        }
        if ("buyer".equals(sourcePage)) {
            con.setPrinterId(registerDTO.getUid().intValue());
            con.setConfirmBy(con.getPrinterId().toString());
        }

        ContractSearchModel searchModel = JSONObject.parseObject(contractSearchModel, ContractSearchModel.class);
        if (searchModel != null) {
            //判断searchModel的值,目前只进行了协议名称查询
            if (StringUtils.isNotEmpty(searchModel.getContractName())) {
                con.setContractName(searchModel.getContractName());
            }
            //根据协议中物品进行查询

            if (StringUtils.isNotEmpty(searchModel.getItemName())) {
                con.setItemName(searchModel.getItemName());
            }
            //根据所签署协议的公司名称进行查询
            if (StringUtils.isNotEmpty(searchModel.getCompanyName())) {
                userDTO.setCompanyName(searchModel.getCompanyName());
                List<UserDTO> userList = userExportService.findUserListByCondition(userDTO, null, null).getRows();
                if (null != userList && userList.size() > 0) {
                    for (UserDTO temp : userList) {
                        idList.add(temp.getUid().toString());
                    }
                } else {
                    idList.add("没有这么个ID");
                }
            }
        }
        con.setStatus("3");
        //根据用户进入的买/卖中心设置查询条件
        if ("seller".equals(sourcePage) && idList.size() > 0) {
            //卖家中心进入协议页面的需要设置卖方为当前登录用户进行查询
            //如果输入了公司名称查询条件,从卖家中心进入协议页面的应设置查询条件中的公司名称下的用户列表为买方
            con.setPrinterIdList(idList);
        }
        if ("buyer".equals(sourcePage) && idList.size() > 0) {
            //从买家中心进入协议页面的需要设置买方为当前登录用户进行查询
            //如果输入了公司名称查询条件,从买家中心进入协议页面的应设置查询条件中的公司名称下的用户列表为卖方
            con.setSupplierIdList(idList);
        }
        //如果传入的合同号列表不为空则执行确认操作
        if (contractId != null && !"".equals(contractId)) {
        	ExecuteResult <String> result;
            ContractInfoDTO temp = new ContractInfoDTO();
            temp.setId(Long.valueOf(contractId));
            ContractInfoDTO resultTemp = protocolExportService.queryByContractInfo(temp).getResult();
            if ("拒绝".equals(operation)) {
                resultTemp.setStatus("4");
                resultTemp.setRefusalReason(reason);
            }
            if ("同意".equals(operation)) {
                resultTemp.setStatus("5");
            }
            if ("重新提交".equals(operation)) {
                resultTemp.setStatus("3");
            }
            if ("发布".equals(operation)) {
                if (StringUtils.isNotBlank(resultTemp.getUpdatedContractNo())) {
                    ContractInfoDTO searchDto = new ContractInfoDTO();
                    //dubbo发版后可用
                    searchDto.setUpdatedContractNo(resultTemp.getUpdatedContractNo());
                    searchDto.setActiveFlag("1");
                    ContractInfoDTO lastCon = protocolExportService.queryByContractInfo(searchDto).getResult();
                    if (null != lastCon) {
                        lastCon.setActiveFlag("0");
                        protocolExportService.modifyContractInfo(lastCon);
                    }
                }
                resultTemp.setStatus("6");
            }
            resultTemp.setConfirmDate(new Date());
            result = protocolExportService.modifyContractInfo(resultTemp);
            model.addAttribute("result", result.getResult());

            if(result!=null && result.isSuccess()){
                LOG.info("----------------有一条协议已经成功确认--------------------");
                SendWeiXinMessage message = new SendWeiXinMessage();
                message.setModeId(WeiXinMessageModeId.CONTRACT_ADD_SUCCESS);
                message.setFirst("【印刷家】尊敬的用户，您有一条协议已经成功确认，印刷家提醒您及时查看。");
                message.setKeyword1(resultTemp.getContractOrderNo());
                message.setKeyword2(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                String uid = null;
                if("buyer".equals(sourcePage)){
                    uid = resultTemp.getSupplierId().toString();
                }else if("seller".equals(sourcePage)){
                    uid = resultTemp.getPrinterId().toString();
                }
                SendWeiXinMessageUtil.sendWeiXinMessage(uid, message, request, response);
            }
        }

        List<ContractInfoDTO> conInfos = contractService.assembleContracInfos(con);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("contracts", conInfos);
        model.addAttribute("uid", registerDTO.getUid());
        model.addAttribute("sourcePage", sourcePage);


        return "/contract/contractConfirm";
    }


    /**
     * 
     * <p>Discription:[方法功能中文描述：跳转商品搜索页(暂时不用)]</p>
     * Created on 2016年1月28日
     * @param model
     * @param request
     * @param response
     * @return
     * @author:[王帅]
     */
    
    
    @RequestMapping("/goodsListContract")
    public String goGoodsListPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        String number = request.getParameter("number");
        model.addAttribute("number", number);
        return "/contract/goodsListContract";
    }

    /**
     * 
     * <p>Discription:[方法功能中文描述：搜索商品]</p>
     * Created on 2016年1月28日
     * @param keyword
     * @param model
     * @param supplierId
     * @param request
     * @return
     * @author:[王帅]
     */
    
    
    @RequestMapping("/searchItem")
    public String searchItem(String keyword, Model model, String supplierId, HttpServletRequest request) {
        SearchInDTO inDto = new SearchInDTO();
        Long shopId;

        if (StringUtils.isNoneBlank(supplierId)) {
            shopId = userExportService.queryUserById(Long.valueOf(supplierId)).getShopId();
        } else {
            String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
            shopId = userExportService.queryUserById(Long.valueOf(uid)).getShopId();
        }

        String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);

        if (shopId != null && shopId != 0L) {
            inDto.setShopId(shopId);
        }
        if (StringUtils.isNoneBlank(region)) {
            inDto.setAreaCode(region);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            inDto.setKeyword(keyword);
        }
        Pager<ItemSkuDTO> pager = new Pager<ItemSkuDTO>();
        pager.setRows(Integer.MAX_VALUE);
        //默认降序排序
        SearchOutDTO outDTO = searchItemExportService.searchItem(inDto, pager);
        List<ItemSkuDTO> items = outDTO.getItemDTOs().getRows();
        for (ItemSkuDTO e : items) {
            ItemShopCartDTO dto = new ItemShopCartDTO();
            dto.setAreaCode("0");
            dto.setShopId(Long.valueOf(e.getShopId().toString()));
            dto.setSkuId(Long.valueOf(e.getSkuId().toString()));
            dto.setItemId(Long.valueOf(e.getItemId().toString()));
            ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
            ItemShopCartDTO itemShopCartDTO = er.getResult();
            if (null != itemShopCartDTO) {
                //商品属性
                String skuName = "";
                for (ItemAttr itemAttr : itemShopCartDTO.getAttrSales()) {
                    skuName += itemAttr.getName();
                    for (ItemAttrValue itemAttrValue : itemAttr.getValues()) {
                        skuName += ":" + itemAttrValue.getName() + ";";
                    }
                }
                e.setSkuAttributeStr(skuName);

            }
        }
        model.addAttribute("items", items);
        model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
        return "/contract/searchItemResult";
    }
    /**
     * 
     * <p>Discription:[方法功能中文描述：获取审批人列表(已替换)]</p>
     * Created on 2016年1月28日
     * @param writer
     * @param uid
     * @author:[王帅]
     */ 
    @RequestMapping("/approveList")
    public void showApproveList(PrintWriter writer, String uid) {
        UserDTO userDTO = userExportService.queryUserById(Long.valueOf(uid));
        UserDTO searchDTO = new UserDTO();
        //根据用户uid查询店铺ID,如果没有店铺ID则根据Uid进行查询
        if (null == userDTO.getShopId()) {
            searchDTO.setUid(Long.valueOf(uid));
        } else {
            searchDTO.setShopId(userDTO.getShopId());
        }
        List<UserDTO> userDTOs = userExportService.findUserListByCondition(searchDTO, null, null).getRows();
        writer.write(JSONArray.toJSONString(userDTOs));
    }
    /**
     * 
     * <p>Discription:[方法功能中文描述:查询协议明细的列表]</p>
     * Created on 2016年1月28日
     * @param uid
     * @param jsonProducts
     * @return
     * @author:王帅]
     */
    @RequestMapping("/orderItem")
    @ResponseBody
    public String orderItem(@CookieValue(value = Constants.USER_ID) String uid, String jsonProducts) {
        String result = "success";
        Pager<ContractMatDTO> pager = new Pager<ContractMatDTO>();
        ContractMatDTO matDTO = new ContractMatDTO();
        List<Product> products = new ArrayList<Product>();
        List<Map> mapProducts = JSONArray.parseArray(jsonProducts, Map.class);
        for (Map e : mapProducts) {
            matDTO.setId(Long.valueOf(e.get("matId").toString()));
            List<Map> temps = protocolExportService.queryContractMatList(matDTO, pager).getResult().getRows();
            Product product = new Product();
            product.setCid(Long.valueOf(temps.get(0).get("cid").toString()));
            product.setShopId(Long.valueOf(temps.get(0).get("shopId").toString()));
            product.setItemId(Long.valueOf(temps.get(0).get("itemId").toString()));
            product.setQuantity(Integer.valueOf(e.get("quantity").toString()));
            product.setSkuPrice(new BigDecimal(temps.get(0).get("matPrice").toString()));
            if(e.get("protocolType").toString().equals("2")){
            	int number=Integer.valueOf(e.get("number").toString());
            	int quantity=Integer.valueOf(e.get("quantity").toString());
            	int	urplusNumber=number-quantity;
            	product.setNumber(urplusNumber);
            	
            }
            product.setSellerId(Long.valueOf(temps.get(0).get("sellerId").toString()));
            product.setSkuId(Long.valueOf(temps.get(0).get("skuId").toString()));
            product.setRegionId("0");
            products.add(product);
        }
        String jsonProduct = JSONArray.toJSONString(products);
        this.redisDB.set(uid + "Redis", jsonProduct);
        return result;
    }


    /**
     * 
     * <p>Discription:[方法功能中文描述; 获取审批人列表]</p>
     * Created on 2016年1月28日
     * @param request
     * @param model
     * @author:[王帅]
     */

    public void showApproveList1(HttpServletRequest request, Model model) {
        List<ChildUserListPost> childUserListPosts = new ArrayList<ChildUserListPost>();
        //查询登录账号的父账号userID
        Long parentId = WebUtil.getInstance().getUserId(request);
        UserDTO userDTO = userExportService.queryUserById(parentId);

        if (userDTO!=null&&userDTO.getParentId() != null) {
            UserDTO dto = userExportService.queryUserById(userDTO.getParentId());
            if (dto != null) {
                ChildUserListPost childUserListPost = new ChildUserListPost();
                childUserListPost.setShopId(dto.getShopId());
                childUserListPost.setUserId(dto.getUid());
                childUserListPost.setUsername(dto.getUname());
                childUserListPost.setNickName(dto.getNickname());
                childUserListPosts.add(childUserListPost);
            }
        }

        if (userDTO != null) {
            ChildUserListPost childUserListPost = new ChildUserListPost();
            childUserListPost.setShopId(userDTO.getShopId());
            childUserListPost.setUserId(userDTO.getUid());
            childUserListPost.setUsername(userDTO.getUname());
            childUserListPost.setNickName(userDTO.getNickname());
            childUserListPosts.add(childUserListPost);
        }
        Integer moduleType = 1;
        ExecuteResult<DataGrid<ChildUserDTO>> executeResult = userStorePermissionExportService.queryChildUserList(parentId, moduleType, null);
        DataGrid<ChildUserDTO> dataGrid = executeResult.getResult();

        List<ChildUserDTO> childUserDTOs = dataGrid.getRows();
        for (ChildUserDTO childUserDTO : childUserDTOs) {
            String resourceName = "";
            List<UserMallResourceDTO> userMallResourceDTOs = childUserDTO.getUserMallResourceList();
            for (UserMallResourceDTO userMallResourceDTO : userMallResourceDTOs) {
                resourceName = resourceName + userMallResourceDTO.getResourceName() + "|";
            }
            if (resourceName.length() > 0) {
                resourceName = resourceName.substring(0, resourceName.length() - 1);
            }

            //查询店铺名称
            ChildUserListPost childUserListPost = new ChildUserListPost();
            childUserListPost.setShopId(childUserDTO.getShopId());
            childUserListPost.setUpdateTime(childUserDTO.getUpdateTime());
            childUserListPost.setUserId(childUserDTO.getUserId());
            childUserListPost.setUsername(childUserDTO.getUsername());
            childUserListPost.setResourceIds(resourceName);
            childUserListPost.setNickName(childUserDTO.getNickName());
            childUserListPosts.add(childUserListPost);
        }
        model.addAttribute("approveList", childUserListPosts);
    }

}
