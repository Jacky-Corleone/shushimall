package com.camelot.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.dto.*;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.contract.ContractSearchModel;
import com.camelot.mall.contract.ContractService;
import com.camelot.mall.contract.ContractStatus;
import com.camelot.mall.sellcenter.ChildUserListPost;
import com.camelot.mall.shopcart.Product;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.encrypt.EncrypUtil;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.ChildUserDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserStorePermissionExportService;
import com.camelot.usercenter.service.UserWxExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 王帅
 * @version V1.0
 * @Title: ContractController
 * @Package com.camelot.mall.controller
 * @Description: 协议页面控制器
 * @date 2015/5/28
 */
@Controller
public class ContractController {
    @Resource
    private ContractService contractService;
    @Resource
    private ProtocolExportService protocolExportService;
    @Resource
    private UserExportService userExportService = null;
    @Resource
    private UserWxExportService userWxExportService;
    @Resource
    private SearchItemExportService searchItemExportService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
    private RedisDB redisDB;
    @Resource
    private ItemExportService itemService;
    @Resource
    private UserStorePermissionExportService userStorePermissionExportService;
    @Resource
    private TradeOrderExportService tradeOrderExportService;

    /**
     * 年度协议页面
     *
     * @param request
     * @param model
     * @param pager
     * @return
     */
    @RequestMapping("/contract")
    public String contrackPage(HttpServletRequest request, Model model,
                               @RequestParam(value = "sourcePage", required = false, defaultValue = "") String sourcePage,
                               Pager<ContractInfoDTO> pager) {
        // 设置Pager对象
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);

        if (registerDTO == null) {
            return "redirect:/user/login";
        }
        if (StringUtils.isEmpty(sourcePage)) {
            return "redirect:/user/login";
        }
		//没有认证通过的用户，不支持此功能
		if(registerDTO.getUserType() == 1){
			return "/no_authentication";
		}
		
		//获取主子帐号  
        ExecuteResult<List<Long>> userIdList = userExportService.queryUserIds(registerDTO.getUid());
        // 将用户信息加入协议查询条件
        ContractInfoDTO conInfo = new ContractInfoDTO();
        conInfo.setActiveFlag("1");
        if ("buyer".equals(sourcePage)) {
        	if(userIdList.getResult() != null){
        		//用主子账号的集合查询
            	List<String> printerIdList = new ArrayList<String>();
            	for(Long userId : userIdList.getResult()){
            		printerIdList.add(userId.intValue() + "");
            	}
            	conInfo.setPrinterIdList(printerIdList);
            }else{
            	 conInfo.setPrinterId(registerDTO.getUid().intValue());
            }
        }

        if ("seller".equals(sourcePage)) {
        	//用主子账号的集合查询
        	if(userIdList.getResult() != null){
		        List<String> supplierIdList = new ArrayList<String>();
		    	for(Long userId : userIdList.getResult()){
		    		supplierIdList.add(userId.intValue() + "");
		    	}
		    	conInfo.setSupplierIdList(supplierIdList);
        	}else{
        		conInfo.setSupplierId(registerDTO.getUid().intValue());
        	}
        }

        ExecuteResult<DataGrid<ContractInfoDTO>> conInfos = contractService.assembleContracInfos(conInfo, pager);

        // 查询当前用户所有创建的协议信息
        Map<String, String> map = contractService.pPUser(conInfos.getResult().getRows(), true, true);
        Map<String, String> cmap = contractService.cPUser(conInfos.getResult().getRows());
        pager.setTotalCount(conInfos.getResult().getTotal().intValue());
        pager.setRecords(conInfos.getResult().getRows());
        model.addAttribute("conInfos", conInfos.getResult().getRows());
        model.addAttribute("pager", pager);
        model.addAttribute("cmap", cmap);
        model.addAttribute("register", registerDTO);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("map", map);
        model.addAttribute("sourcePage", sourcePage);
        return "/contract/contractPage";
    }

    /**
     * 协议搜索
     *
     * @param pager
     * @param model
     * @param contractSearchModel
     * @return
     */
    @RequestMapping("/searchContractInfo")
    public String contractSearch(HttpServletRequest request, Pager<ContractInfoDTO> pager, Model model,
                                 String sourcePage, String contractSearchModel) {
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        //用户未登录跳转登录页面
        if (registerDTO == null) {
            return "redirect:/user/login";
        }
        ContractInfoDTO contractInfo = new ContractInfoDTO();
        ContractSearchModel searchModel = JSONObject.parseObject(contractSearchModel, ContractSearchModel.class);
        UserDTO userDTO = new UserDTO();
        contractInfo.setActiveFlag("1");
        List<String> idList = new ArrayList<String>();
        if (null != searchModel) {
            if (StringUtils.isNotEmpty(searchModel.getContractName())) {
                contractInfo.setContractName(searchModel.getContractName());
            }
            if (StringUtils.isNotEmpty(searchModel.getItemName())) {
                contractInfo.setItemName(searchModel.getItemName());
            }
            if (StringUtils.isNotEmpty(searchModel.getStatus())) {
                contractInfo.setStatus(searchModel.getStatus());
            }
            if (StringUtils.isNotBlank(searchModel.getSearchType())) {
                //审批查询类型为1
                if ("1".equals(searchModel.getSearchType())) {
                    contractInfo.setStatus("1");
                }
                //确认查询类型为2状态为3
                if ("2".equals(searchModel.getSearchType())) {
                    contractInfo.setStatus("3");
                }
            }
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
                if ("seller".equals(sourcePage)) {
                	contractInfo.setPrinterIdList(idList);
                } else if ("buyer".equals(sourcePage)) {
                	contractInfo.setSupplierIdList(idList);
                }
            }

        }

//        if ("buyer".equals(sourcePage)) {
//            if (StringUtils.isNotBlank(searchModel.getSearchType())) {
//                if ("0".equals(searchModel.getSearchType())) {
//                    contractInfo.setPrinterId(registerDTO.getUid().intValue());
//                }
//                if ("1".equals(searchModel.getSearchType())) {
//                    contractInfo.setApproveBy(registerDTO.getUid().toString());
//                }
//                if ("2".equals(searchModel.getSearchType())) {
//                    contractInfo.setConfirmBy(registerDTO.getUid().toString());
//                }
//            } else {
//                contractInfo.setPrinterId(registerDTO.getUid().intValue());
//            }
//            if (idList.size() > 0) {
//                contractInfo.setSupplierIdList(idList);
//            }
//        }
//
//        if ("seller".equals(sourcePage)) {
//            if (StringUtils.isNotBlank(searchModel.getSearchType())) {
//                if ("0".equals(searchModel.getSearchType())) {
//                    contractInfo.setSupplierId(registerDTO.getUid().intValue());
//                }
//                if ("1".equals(searchModel.getSearchType())) {
//                    contractInfo.setApproveBy(registerDTO.getUid().toString());
//                }
//                if ("2".equals(searchModel.getSearchType())) {
//                    contractInfo.setConfirmBy(registerDTO.getUid().toString());
//                }
//            } else {
//                contractInfo.setSupplierId(registerDTO.getUid().intValue());
//            }
//
//            if (idList.size() > 0) {
//                contractInfo.setPrinterIdList(idList);
//            }
//        }
        
        if ("1".equals(searchModel.getSearchType())) {
            contractInfo.setApproveBy(registerDTO.getUid().toString());
        }
        if ("2".equals(searchModel.getSearchType())) {
            contractInfo.setConfirmBy(registerDTO.getUid().toString());
        }
//        List<Long> idListLong = new ArrayList<Long>();
//        idListLong.add(registerDTO.getUid());
//        contractInfo.setUserList(idListLong);
        

        ExecuteResult<DataGrid<ContractInfoDTO>> conInfos = contractService.assembleContracInfos(contractInfo, pager);
        List<ContractInfoDTO> tempList = conInfos.getResult().getRows();
        pager.setRecords(tempList);
        Map<String, String> map = contractService.pPUser(pager.getRecords(), true, true);
        Map<String, String> cmap = contractService.cPUser(pager.getRecords());
        pager.setTotalCount(conInfos.getResult().getTotal().intValue());
        model.addAttribute("conInfos", tempList);
        model.addAttribute("pager", pager);
        model.addAttribute("map", map);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("register", registerDTO);
        model.addAttribute("cmap", cmap);
        model.addAttribute("sourcePage", sourcePage);
        return "/contract/contractInfoList";
    }


    @RequestMapping("/contractCreate")
    public String contractCreatePage(String sourcePage, HttpServletRequest request,
                                     Model model) {

        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO= userExportService.getUserByRedis(token);
        if (registerDTO == null) {
            return "redirect:/user/login";
        }

       // UserDTO userDTO = userExportService.queryUserById(registerDTO.getUid());
        
        UserDTO  buyercx = userExportService.queryUserById(registerDTO.getUid());//查询是否是父账号
        UserDTO userDTO=null;
        	  if(buyercx.getParentId()==null){
        		  userDTO = userExportService.queryUserById(registerDTO.getUid());//是父账号查询内容
        	  }else{
        		  userDTO = userExportService.queryUserByfId(registerDTO.getUid());//子账号查询内容
        	  }
        
        
        //根据登陆用户的类型查询对应的乙方信息
        String contractNo = protocolExportService.createContractNo().getResult();
        showApproveList(request, model);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("user", registerDTO);
        model.addAttribute("contractNo", contractNo);
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
        return "/contract/contractCreatePage";
    }

    /**
     * 获取买卖双方信息
     *
     * @param pager
     * @param model
     * @param uType
     * @return
     */
    @RequestMapping("/getSellerBuyerDetail")
    public String getPartBDetail(String company,
                                 Pager<UserDTO> pager, Model model, Integer uType) {

        UserDTO userSearchDTO = new UserDTO();
        DataGrid<UserDTO> users = new DataGrid<UserDTO>();
        if (uType != null) {
            if (uType == 3) {
                userSearchDTO.setUsertype(3);
                if (StringUtils.isNotEmpty(company)) {
                    userSearchDTO.setCompanyName(company);
                }
                userSearchDTO.setParentId(-1l);
                users = userExportService.findUserListByCondition(userSearchDTO, null, pager);
            }
            if (uType == 2) {
                if (StringUtils.isNotEmpty(company)) {
                    userSearchDTO.setCompanyName(company);
                }
                users = userWxExportService.queryUser(userSearchDTO, pager).getResult();
            }
            pager.setRecords(users.getRows());
            pager.setTotalCount(users.getTotal().intValue());
            model.addAttribute("pager", pager);
            model.addAttribute("uType", uType);
        }
        return "/contract/contractPartBDetail";

    }

    /**
     * 创建协议
     *
     * @param contractInfo
     * @param contractMatDTOs
     * @param contractPaymentTerm
     * @return
     */

    @RequestMapping("/contractAdd")
    public void contractCreateAdd(HttpServletRequest request, String contractInfo, String contractMatDTOs,
                                  String contractPaymentTerm, String sourcePage, PrintWriter writer
    ) {
        ExecuteResult<String> result = new ExecuteResult<String>();
        List<ContractMatDTO> contractMats = JSONObject.parseArray(contractMatDTOs, ContractMatDTO.class);
        ContractInfoDTO contractInfoDTO = JSONObject.parseObject(contractInfo, ContractInfoDTO.class);
        ContractPaymentTermDTO contractPaymentTermDTO = JSONObject.parseObject(contractPaymentTerm, ContractPaymentTermDTO.class);
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
            if (contractInfoDTO.getSupplierId() == null) {
                result.addErrorMessage("卖方为必填字段，请选择卖方");
            }
            contractInfoDTO.setCreateRole(1);
        }
        if ("seller".equals(sourcePage)) {
            //数据必填字段验证
            if (contractInfoDTO.getPrinterId() == null) {
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
        if(contractPaymentTermDTO.getPaymentType()==null||contractPaymentTermDTO.getPaymentDays()==null){
       	    result.addErrorMessage("协议账期 不能为空！");
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
        if (StringUtils.isEmpty(contractInfoDTO.getStatus())) {
            contractInfoDTO.setStatus("0");
        }
        //设置合同号
        contractPaymentTermDTO.setContractNo(contractInfoDTO.getContractNo());
        contractPaymentTermDTO.setActiveFlag("1");
        contractPaymentTermDTO.setCreateBy(registerDTO.getUid().toString());
        //执行创建协议动作
        protocolExportService.addContractInfo(contractInfoDTO);
        result = protocolExportService.addContractPaymentTerm(contractPaymentTermDTO);
        for (ContractMatDTO matDTO : contractMats) {
            matDTO.setContractNo(contractInfoDTO.getContractNo());
            matDTO.setCreateBy(registerDTO.getUid().toString());
            matDTO.setActiveFlag("1");
            //协议类型
            matDTO.setProtocolType(contractInfoDTO.getProtocolType());
            result = protocolExportService.addContractMat(matDTO);
        }
        writer.write(JSON.toJSONString(result));
        return;
    }
    
    
    /**
     * 立即发布
     *
     * @param contractInfo
     * @param contractMatDTOs
     * @param contractPaymentTerm
     * @return
     */

    @RequestMapping("/immediatelyup")
    public void immediatelyup(HttpServletRequest request, String contractInfo, String contractMatDTOs,
                                  String contractPaymentTerm, String sourcePage, PrintWriter writer
    ) {
        ExecuteResult<String> result = new ExecuteResult<String>();
        List<ContractMatDTO> contractMats = JSONObject.parseArray(contractMatDTOs, ContractMatDTO.class);
        ContractInfoDTO contractInfoDTO = JSONObject.parseObject(contractInfo, ContractInfoDTO.class);
        ContractPaymentTermDTO contractPaymentTermDTO = JSONObject.parseObject(contractPaymentTerm, ContractPaymentTermDTO.class);
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
            if (contractInfoDTO.getSupplierId() == null) {
                result.addErrorMessage("卖方为必填字段，请选择卖方");
            }
        }
        if ("seller".equals(sourcePage)) {
            //数据必填字段验证
            if (contractInfoDTO.getPrinterId() == null) {
                result.addErrorMessage("买方为必填字段，请选择买方");
            }
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
        if(contractPaymentTermDTO.getPaymentType()==null||contractPaymentTermDTO.getPaymentDays()==null){
       	    result.addErrorMessage("协议账期 不能为空！");
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
        if (StringUtils.isEmpty(contractInfoDTO.getStatus())) {
            contractInfoDTO.setStatus("3");
        }
        //设置合同号
        contractPaymentTermDTO.setContractNo(contractInfoDTO.getContractNo());
        contractPaymentTermDTO.setActiveFlag("1");
        contractPaymentTermDTO.setCreateBy(registerDTO.getUid().toString());
        //执行创建协议动作
        protocolExportService.addContractInfo(contractInfoDTO);
        result = protocolExportService.addContractPaymentTerm(contractPaymentTermDTO);
        for (ContractMatDTO matDTO : contractMats) {
            matDTO.setContractNo(contractInfoDTO.getContractNo());
            matDTO.setCreateBy(registerDTO.getUid().toString());
            matDTO.setActiveFlag("1");
            //协议类型
            matDTO.setProtocolType(contractInfoDTO.getProtocolType());
            result = protocolExportService.addContractMat(matDTO);
        }
        writer.write(JSON.toJSONString(result));
        return;
    }
    
    
    
    
    
    
    

    /**
     * 获取用户信息
     *
     * @param userDTO
     * @param model
     * @param pager
     * @return
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
     * 跳转协议修改页面
     *
     * @param model
     * @param contractId
     * @param sourcePage
     * @param pager
     * @param request
     * @return
     */
    @RequestMapping("/toContractUpdate")
    public String contractModify(Model model, String contractId, String sourcePage, Pager<Map> pager, HttpServletRequest request) {
        //创建查询数据
        ContractInfoDTO contractInfoDTO = new ContractInfoDTO();
        ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
        ContractMatDTO contractMatDTO = new ContractMatDTO();
        RegisterDTO registerDTO = new RegisterDTO();
        //使用协议号进行数据查询
        contractInfoDTO.setId(Long.valueOf(contractId));
        //查询协议信息
        ExecuteResult<ContractInfoDTO> results = protocolExportService.queryByContractInfo(contractInfoDTO);
        contractMatDTO.setContractNo(results.getResult().getContractNo());
        contractMatDTO.setActiveFlag("1");
        contractPaymentTermDTO.setContractNo(results.getResult().getContractNo());
        contractPaymentTermDTO.setActiveFlag("1");
        registerDTO.setUserType(2);
        //查询买卖双方用户信息
        UserDTO user = userExportService.queryUserById(results.getResult().getPrinterId().longValue());
        UserDTO seller = userExportService.queryUserById(results.getResult().getSupplierId().longValue());
        //查询账期及物品
        ExecuteResult<ContractPaymentTermDTO> paymentTermDTO = protocolExportService.queryByContractPaymentTerm(contractPaymentTermDTO);
        pager.setRows(Integer.MAX_VALUE);
        ExecuteResult<DataGrid<Map>> contractMat = protocolExportService.queryContractMatList(contractMatDTO, pager);
        for (Map e : contractMat.getResult().getRows()) {
            contractService.putSalerAttr(e);
        }
        String fileName = results.getResult().getAnnex().substring(results.getResult().getAnnex().lastIndexOf("/") + 1);
        showApproveList(request, model);
        pager.setTotalCount(contractMat.getResult().getTotal().intValue());
        pager.setRecords(contractMat.getResult().getRows());
        model.addAttribute("contractMatPager", pager);
        model.addAttribute("register", registerDTO);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
        model.addAttribute("user", user);
        model.addAttribute("seller", seller);
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("contractMat", contractMat);
        model.addAttribute("fileName", fileName);
        model.addAttribute("paymentTermDTO", paymentTermDTO.getResult());
        model.addAttribute("contractInfo", results.getResult());

        return "/contract/contractUpdatePage";
    }

    @RequestMapping("/contractUpdate")
    public String contractUpdate(HttpServletRequest request, String contractInfo, String contractMatDTOs, String contractPaymentTerm,
                                 Model model, String needApprove, String removeMat) {
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        //用户未登录跳转登录页面
        if (registerDTO == null) {
        	return "redirect:/user/login";
        }
        ExecuteResult<String> result = new ExecuteResult<String>();
        List<ContractMatDTO> contractMats = JSONObject.parseArray(contractMatDTOs, ContractMatDTO.class);
        ContractInfoDTO contractInfoDTO = JSONObject.parseObject(contractInfo, ContractInfoDTO.class);
        ContractPaymentTermDTO contractPaymentTermDTO = JSONObject.parseObject(contractPaymentTerm, ContractPaymentTermDTO.class);
        List<Long> removeMatIds = JSONObject.parseArray(removeMat, Long.class);

        //协议物品验证
        if (contractMats.get(0) == null) {
            result.addErrorMessage("请选择协议物品");
            model.addAttribute("errmess", result.getErrorMessages());
            return "/contract/contractResultMessage";
        }

        //数据必填字段验证
        if (contractInfoDTO.getPrinterId() == null) {
            result.addErrorMessage("印刷厂为必填字段，请选择印刷厂");
            model.addAttribute("errmess", result.getErrorMessages());
            return "/contract/contractResultMessage";
        }
        if (contractInfoDTO.getSupplierId() == null) {
            result.addErrorMessage("供应商为必填字段，请选择供应商");
            model.addAttribute("errmess", result.getErrorMessages());
            return "/contract/contractResultMessage";
        }
        if (contractInfoDTO.getCreateBy() == null || "".equals(contractInfoDTO.getCreateBy())) {
            result.addErrorMessage("协议创建人为必填字段，请选择协议创建人");
            model.addAttribute("errmess", result.getErrorMessages());
            return "/contract/contractResultMessage";
        }
        if (contractInfoDTO.getBeginDate() == null || contractInfoDTO.getEndDate() == null) {
            result.addErrorMessage("协议日期必须设置");
            model.addAttribute("errmess", result.getErrorMessages());
            return "/contract/contractResultMessage";
        } else if (contractInfoDTO.getBeginDate().compareTo(contractInfoDTO.getEndDate()) == 0) {
            result.addErrorMessage("协议开始与结束日期不能相同");
            model.addAttribute("errmess", result.getErrorMessages());
            return "/contract/contractResultMessage";
        } else if (contractInfoDTO.getBeginDate().compareTo(contractInfoDTO.getEndDate()) > 0) {
            result.addErrorMessage("协议开始日期不能大于结束日期");
            model.addAttribute("errmess", result.getErrorMessages());
            return "/contract/contractResultMessage";
        }
         if(contractPaymentTermDTO.getPaymentType()==null||contractPaymentTermDTO.getPaymentDays()==null){
        	 result.addErrorMessage("协议账期 不能为空！");
             model.addAttribute("errmess", result.getErrorMessages());
             return "/contract/contractResultMessage";
         }
        //设置协议号
        contractInfoDTO.setUpdateBy(registerDTO.getUid().toString());
        contractInfoDTO.setUpdateDate(new Date());
        contractInfoDTO.setActiveFlag("1");
        contractPaymentTermDTO.setContractNo(contractInfoDTO.getContractNo());
        contractPaymentTermDTO.setActiveFlag("1");
        contractPaymentTermDTO.setCreateBy(registerDTO.getUid().toString());
   
        //执行修改协议动作
        contractService.updateContractInfos(contractInfoDTO, contractMats, contractPaymentTermDTO, registerDTO, needApprove, removeMatIds);
        model.addAttribute("result", result.getResultMessage());

        return "/contract/contractResultMessage";
    }


    /**
     * 删除协议
     *
     * @param contractNos
     * @param model
     * @return
     */
    @RequestMapping("/deleteContractInfo")
    public String deleteContractInfo(String contractNos, Model model) {
        List<String> contractNo = JSONObject.parseArray(contractNos, String.class);
        ContractInfoDTO temp = new ContractInfoDTO();
        ExecuteResult<ContractInfoDTO> resultTemp;
        for (String s : contractNo) {
            temp.setContractNo(s);
            resultTemp = protocolExportService.queryByContractInfo(temp);
            resultTemp.getResult().setActiveFlag("0");
            protocolExportService.modifyContractInfo(resultTemp.getResult());
        }
        return "/contract/contractResultMessage";
    }

    /**
     * 协议审批
     *
     * @param contractNos
     * @param model
     * @return
     */
    @RequestMapping("/approveContractInfo")
    public String approveContractInfo(HttpServletRequest request, String contractNos, String operation,
                                      String reason, String sourcePage, Model model, Pager<ContractInfoDTO> pager) {
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);

        if (null == registerDTO) {
            return "redirect:/user/login";
        }
        //没有认证通过的用户，不支持此功能
  		if(registerDTO.getUserType() == 1){
  			return "/no_authentication";
  		}
        ContractInfoDTO con = new ContractInfoDTO();
        con.setActiveFlag("1");
        con.setApproveBy(registerDTO.getUid().toString());
        List<Long> idListLong = new ArrayList<Long>();
        idListLong.add(registerDTO.getUid());
        con.setUserList(idListLong);
        con.setStatus("1");

        if (StringUtils.isNotEmpty(contractNos)) {
            List<String> contractNo = JSONObject.parseArray(contractNos, String.class);
            ContractInfoDTO temp = new ContractInfoDTO();
            for (String s : contractNo) {
                temp.setId(Long.valueOf(s));
                ContractInfoDTO resultTemp = protocolExportService.queryByContractInfo(temp).getResult();
                if ("拒绝".equals(operation)) {
                    resultTemp.setStatus("2");
                    resultTemp.setRefusalReason(reason);
                }
                if ("同意".equals(operation)) {
                    resultTemp.setStatus("3");
                    resultTemp.setApproveDate(new Date());
                }
                if ("重新提交".equals(operation)) {
                    resultTemp.setStatus("1");
                }
                protocolExportService.modifyContractInfo(resultTemp);
            }
        }
        DataGrid<ContractInfoDTO> conInfos = contractService.assembleContracInfos(con, pager).getResult();
        Map<String, String> map = contractService.pPUser(conInfos.getRows(), true, true);
        Map<String, String> cmap = contractService.cPUser(conInfos.getRows());
        pager.setRecords(conInfos.getRows());
        pager.setTotalCount(conInfos.getTotal().intValue());
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("map", map);
        model.addAttribute("cmap", cmap);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("pager", pager);
        return "/contract/contractApprove";
    }

    /**
     * 协议确认
     *
     * @param contractNos
     * @param model
     * @return
     */
    @RequestMapping("/confirmContractInfo")
    public String confirmContractInfo(HttpServletRequest request, String contractNos,
                                      String operation, String sourcePage, String reason, String contractSearchModel,
                                      Model model, Pager<ContractInfoDTO> pager) {
        // 获取当前登录用户信息
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        if (null == registerDTO) {
            return "redirect:/user/login";
        }
        //没有认证通过的用户，不支持此功能
  		if(registerDTO.getUserType() == 1){
  			return "/no_authentication";
  		}
        ContractInfoDTO con = new ContractInfoDTO();
        con.setActiveFlag("1");
        con.setStatus("3");
        con.setConfirmBy(registerDTO.getUid().toString());
        List<Long> idListLong = new ArrayList<Long>();
        idListLong.add(registerDTO.getUid());
        con.setUserList(idListLong);
        //如果传入的协议号列表不为空则执行确认操作
        if (contractNos != null && !"".equals(contractNos)) {
            List<String> contractNo = JSONObject.parseArray(contractNos, String.class);
            ExecuteResult<String> result;
            for (String s : contractNo) {
                ContractInfoDTO temp = new ContractInfoDTO();
                temp.setId(Long.valueOf(s));
                ContractInfoDTO resultTemp = protocolExportService.queryByContractInfo(temp).getResult();
                if ("拒绝".equals(operation)) {
                    resultTemp.setStatus("4");
                    resultTemp.setRefusalReason(reason);
                }
                if ("同意".equals(operation)) {
                    resultTemp.setStatus("5");
                    resultTemp.setConfirmDate(new Date());
                }
                if ("重新提交".equals(operation)) {
                    resultTemp.setStatus("3");
                }
                if ("发布".equals(operation)) {
                    resultTemp.setStatus("6");
                }
                if("终止".equals(operation)){
                	resultTemp.setStatus("10");
                }

                result = protocolExportService.modifyContractInfo(resultTemp);
                model.addAttribute("result", result.getResult());
            }
        }

        DataGrid<ContractInfoDTO> conInfos = contractService.assembleContracInfos(con, pager).getResult();
        Map<String, String> map = contractService.pPUser(conInfos.getRows(), true, true);
        Map<String, String> cmap = contractService.cPUser(conInfos.getRows());
        pager.setRecords(conInfos.getRows());
        pager.setTotalCount(conInfos.getTotal().intValue());
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("contracts", conInfos);
        model.addAttribute("cmap", cmap);
        model.addAttribute("statusMap", ContractStatus.valueMap());
        model.addAttribute("map", map);
        model.addAttribute("pager", pager);
        return "/contract/contractConfirm";
    }

    @RequestMapping("/getDetailInfo")
    public String getContractDetailInfo(String contractId, Pager<ContractInfoDTO> pager, Model model) {
        if (StringUtils.isNotEmpty(contractId)) {
            ContractInfoDTO contractInfoDTO = new ContractInfoDTO();
            ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
            ContractMatDTO contractMatDTO = new ContractMatDTO();
            contractInfoDTO.setId(Long.valueOf(contractId));
            ContractInfoDTO result = protocolExportService.queryByContractInfo(contractInfoDTO).getResult();
            contractMatDTO.setContractNo(result.getContractNo());
            contractPaymentTermDTO.setContractNo(result.getContractNo());
            ContractOrderDTO orderDTO = new ContractOrderDTO();
            orderDTO.setContractNo(contractInfoDTO.getContractNo());
            //根据协议编号查询协议信息
            pager.setRows(Integer.MAX_VALUE);
            ExecuteResult<DataGrid<Map>> matMaps = protocolExportService.queryContractMatList(contractMatDTO, pager);
            List<Map> matMap = new ArrayList<Map>();
            if (null != matMaps && null != matMaps.getResult() && null != matMaps.getResult().getRows()) {
                matMap = matMaps.getResult().getRows();
                for (Map e : matMap) {
                    contractService.putSalerAttr(e);
                }
            }
            result.setContractMatDTOs(matMap);
            ContractPaymentTermDTO contractPayment = protocolExportService.queryByContractPaymentTerm(contractPaymentTermDTO).getResult();

            UserDTO seller = userExportService.queryUserById(result.getSupplierId());
            UserDTO buyer = userExportService.queryUserById(result.getPrinterId());
            UserDTO publishedBy = userExportService.queryUserById(Long.valueOf(result.getCreateBy()));
            UserDTO approveBy = new UserDTO();
            if (StringUtils.isNotBlank(result.getApproveBy())) {
                approveBy = userExportService.queryUserById(Long.valueOf(result.getApproveBy()));
            }
            //查询协议信息
            String fileName = result.getAnnex().substring(result.getAnnex().lastIndexOf("/") + 1);
            model.addAttribute("fileName", fileName);
            model.addAttribute("seller", seller);
            model.addAttribute("buyer", buyer);
            model.addAttribute("publishedBy", publishedBy);
            model.addAttribute("approveBy", approveBy);
            model.addAttribute("statusMap", ContractStatus.valueMap());
            model.addAttribute("contractPayment", contractPayment);
            model.addAttribute("contract", result);
            model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
        }
        return "/contract/contractDetailInfo";
    }

    /**
     * 搜索商品
     *
     * @return
     * @auther wsh
     */
    @RequestMapping("/contract/searchItem")
    public String searchItem(String keyword, Model model, String shopId, HttpServletRequest request, Pager<ItemSkuDTO> pager) {
        SearchInDTO inDto = new SearchInDTO();
        ItemShopCartDTO dto = new ItemShopCartDTO();
        String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);

        if (StringUtils.isNotEmpty(shopId)) {
            inDto.setShopId(Long.valueOf(shopId));
        }
        if (StringUtils.isNoneBlank(region)) {
            inDto.setAreaCode(region);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            inDto.setKeyword(keyword);
        }
        //默认降序排序
        SearchOutDTO outDTO = searchItemExportService.searchItem(inDto, pager);
        DataGrid<ItemCategoryDTO> categories = itemCategoryService.queryItemCategoryAllList(null);
        Map<String, String> catMap = new HashMap<String, String>();
        for (ItemCategoryDTO e : categories.getRows()) {
            catMap.put(e.getCategoryCid().toString(), e.getCategoryCName());
        }
        if (null != outDTO) {
            List<ItemSkuDTO> items = outDTO.getItemDTOs().getRows();

            if (null != items && 0 < items.size()) {
                for (ItemSkuDTO e : items) {
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
            }

            pager.setRecords(items);
            pager.setTotalCount(outDTO.getItemDTOs().getTotal().intValue());
            model.addAttribute("pager", pager);
//            model.addAttribute("catMap", catMap);
        }
        model.addAttribute("shopId", shopId);
        model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
        model.addAttribute("catMap", catMap);
        return "/contract/searchItemResult";
    }

    /**
     * 获取审批人列表
     *
     * @param request
     * @param model
     */

    public void showApproveList(HttpServletRequest request, Model model) {
        List<ChildUserListPost> childUserListPosts = new ArrayList<ChildUserListPost>();

        Long parentId = WebUtil.getInstance().getUserId(request);
        UserDTO userDTO = userExportService.queryUserById(parentId);
        if (userDTO.getParentId() != null) {
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

    /**
     * 跳转协议下单
     *
     * @param request
     * @param contractId
     * @param model
     * @param sourcePage
     * @param pager
     * @return
     */
    @RequestMapping("/contract/contractOrder")
    public String contractOrder(HttpServletRequest request, String contractId, Model model, String sourcePage, Pager<ContractInfoDTO> pager) {
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        if (null == registerDTO) {
            return "redirect:/user/login";
        }
        if (StringUtils.isNotEmpty(contractId)) {
            ContractInfoDTO contractInfoDTO = new ContractInfoDTO();
            ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
            ContractMatDTO contractMatDTO = new ContractMatDTO();
            contractInfoDTO.setId(Long.valueOf(contractId));
            ContractInfoDTO result = protocolExportService.queryByContractInfo(contractInfoDTO).getResult();
            contractMatDTO.setContractNo(result.getContractNo());
            contractPaymentTermDTO.setContractNo(result.getContractNo());
            //根据协议编号查询协议信息

            ExecuteResult<DataGrid<Map>> matMaps = protocolExportService.queryContractMatList(contractMatDTO, pager);
            List<Map> matMap = new ArrayList<Map>();
            if (null != matMaps && null != matMaps.getResult() && null != matMaps.getResult().getRows()) {
                matMap = matMaps.getResult().getRows();
                for (Map e : matMap) {
                    contractService.putSalerAttr(e);
                }
            }
            result.setContractMatDTOs(matMap);
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

            UserDTO seller = userExportService.queryUserById(result.getSupplierId());
            UserDTO buyer = userExportService.queryUserById(result.getPrinterId());

            model.addAttribute("seller", seller);
            model.addAttribute("buyer", buyer);
            model.addAttribute("statusMap", ContractStatus.valueMap());
            model.addAttribute("contractPayment", contractPayment);
            model.addAttribute("contract", result);
        }
        model.addAttribute("sourcePage", sourcePage);
        return "/contract/contractOrder";
    }

    /**
     * 协议下单
     *
     * @param uid
     * @param jsonProducts
     * @return
     */
    @RequestMapping("/contract/orderItem")
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
            product.setSellerId(Long.valueOf(temps.get(0).get("sellerId").toString()));
            product.setSkuId(Long.valueOf(temps.get(0).get("skuId").toString()));
            product.setRegionId("0");
            products.add(product);
        }
        String jsonProduct = JSONArray.toJSONString(products);
        this.redisDB.set(uid + "Redis", jsonProduct);
        return result;
    }

    @RequestMapping("/contract/getContractOrder")
    public String getContractOrder(HttpServletRequest request, String contractNo, Pager<TradeOrdersDTO> pager, Model model, String sourcePage) {
		Long userId = WebUtil.getInstance().getUserId(request);
        ContractOrderDTO orderDTO = new ContractOrderDTO();
        List<TradeOrdersDTO> orderResult = new ArrayList<TradeOrdersDTO>();
        int counter = 0;
        orderDTO.setActiveFlag("0");
        orderDTO.setContractNo(contractNo);
        ContractInfoDTO contractInfoDTO = new ContractInfoDTO();
        contractInfoDTO.setContractNo(contractNo);
        ContractInfoDTO result = protocolExportService.queryByContractInfo(contractInfoDTO).getResult();
        ExecuteResult<DataGrid<ContractOrderDTO>> contractOrderResult = protocolExportService.queryContractOrderList(orderDTO, new Pager());
        if (contractOrderResult.isSuccess()) {
            List<ContractOrderDTO> tempList = contractOrderResult.getResult().getRows();
            for (ContractOrderDTO e : tempList) {
                TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
                tradeOrdersQueryInDTO.setOrderId(e.getOrderNo());
                ExecuteResult<DataGrid<TradeOrdersDTO>> tempOrderList = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, new Pager<TradeOrdersQueryInDTO>());
                if (tempOrderList.isSuccess()) {
                    counter += tempOrderList.getResult().getTotal().intValue();
                    orderResult.addAll(tempOrderList.getResult().getRows());
                }
            }
			// 加密orderId
			for (TradeOrdersDTO tradeOrdersDTO : orderResult) {
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"", tradeOrdersDTO.getOrderId());
				if (passKeyEr.isSuccess()) {
					tradeOrdersDTO.setPassKey(passKeyEr.getResult());
				}
			}
        }
        Map<Integer, String> orderStateMap = new HashMap<Integer, String>();
        orderStateMap.put(1, "待付款");
        orderStateMap.put(2, "待配送");
        orderStateMap.put(3, "待确认送货");
        orderStateMap.put(4, "待评价");
        orderStateMap.put(5, "已完成");
        pager.setRecords(orderResult);
        pager.setTotalCount(counter);
        model.addAttribute("orderPager", pager);
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("orderStateMap", orderStateMap);
        model.addAttribute("contractNo", contractNo);
        model.addAttribute("contract", result.getContractOrderNo());

        return "/contract/contractOrderDetail";
    }
}
