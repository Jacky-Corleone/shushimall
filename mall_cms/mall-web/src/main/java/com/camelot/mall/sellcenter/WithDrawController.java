package com.camelot.mall.sellcenter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.common.enums.CiticEnums;
import com.camelot.common.enums.WithdrawEnums;
import com.camelot.common.util.Signature;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.FinanceWithdrawRecordExportService;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.FinanceWithdrawRecordDTO;
import com.camelot.payment.dto.citic.auxiliary.AffiliatedBalance;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserAccountDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.WebUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>Description: [提现管理]</p>
 * Created on 2015年3月17日
 * @author  <a href="mailto: menguangyao@camelotchina.com">门光耀</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/sellcenter/withdraw")
public class WithDrawController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithDrawController.class);
    @Resource
    private CiticExportService citicExportService;
    @Resource
    private UserExtendsService userExtendsService;
    @Resource
    private FinanceWithdrawRecordExportService financeWithdrawRecordExportService;
    @Resource
    private UserExportService userExportService;
    private static String	PRIKEY = SysProperties.getProperty("transfer.prikey");
    private static String MALL= SysProperties.getProperty("transfer.system");
    /**
     * <p>Description: [提现查询]</p>
     * Created on 2015年3月17日
     * @author  <a href="mailto: menguangyao@camelotchina.com">门光耀</a>
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/withdrawselect")
    public String WithDrawSelect(HttpServletRequest request, Model model){
        String page=request.getParameter("page");
        String rows=request.getParameter("rows");
        if(page==null||"".equals(page)){
            page="1";
        }
        if(rows==null||"".equals(rows)){
            rows="10";
        }
        Long uid = WebUtil.getInstance().getUserId(request);
        CommonEnums.ComStatus[] comStatuses= CommonEnums.ComStatus.values();
        Map<Integer,String> zxstatus=new HashMap<Integer, String>();
        for(CommonEnums.ComStatus cc:comStatuses){
            zxstatus.put(new Integer(cc.getCode()),cc.getLabel());
        }
        model.addAttribute("account","0.00");
        if(uid!=null){
            ExecuteResult<UserInfoDTO> executeResult= userExtendsService.findUserInfo(new Long(uid));
            UserInfoDTO userInfoDTO=executeResult.getResult();
            if(userInfoDTO!=null){
                //中信账号信息
                UserCiticDTO userCiticDTO=userInfoDTO.getUserCiticDTO();
                if(userCiticDTO!=null){
                    if(userCiticDTO.getSellerWithdrawsCashAccount()!=null){
                        //收款账户
                        model.addAttribute("cashAccount", userCiticDTO.getSellerWithdrawsCashAccount());
                        //根据卖家收款账户查询卖家的可提现金额
                        if(userCiticDTO.getSellerWithdrawsCashAccount()!=null&&!"".equals(userCiticDTO.getSellerWithdrawsCashAccount())){
                            //可提现金额
                            try{
                                Map<String,String> withmap=new HashMap<String, String>();
                                withmap.put("system",MALL);
                                withmap.put("subAccNo",userCiticDTO.getSellerWithdrawsCashAccount());
                                withmap.put("sign", Signature.createSign(withmap,PRIKEY));
                                ExecuteResult<AffiliatedBalanceDto> affx= citicExportService.querySubBalance(withmap);
                                //ExecuteResult<AffiliatedBalanceDto> affx= null;
                                if(affx!=null){
                                    String msg=affx.getResultMessage();
                                    LOGGER.error("提现接口调用说明："+msg);
                                    if(affx.getErrorMessages()!=null&&affx.getErrorMessages().size()>0){
                                        LOGGER.error("提现接口调用错误："+affx.getErrorMessages().get(0));
                                        model.addAttribute("errorMsg","查询可提现金额失败,错误原因:"+affx.getErrorMessages().get(0));
                                    }
                                    AffiliatedBalanceDto affiliatedBalanceDto=affx.getResult();
                                    if(affiliatedBalanceDto!=null){
                                        LOGGER.error("调用查询提现接口状态码:"+affiliatedBalanceDto.getStatus()+"; 问题描述:"+affiliatedBalanceDto.getStatusText());
                                        List<AffiliatedBalance> listaff=affiliatedBalanceDto.getList();
                                        if(listaff!=null&&listaff.size()>0){
                                            AffiliatedBalance affiliatedBalance=listaff.get(0);
                                            //结算账户 可用金额
                                            model.addAttribute("account",affiliatedBalance.getKYAMT());
                                        }
                                        if(affiliatedBalanceDto.getStatus()!=null&&!"AAAAAAA".equals(affiliatedBalanceDto.getStatus())){
                                            model.addAttribute("staceCode","查询可提现金额失败,错误码:"+affiliatedBalanceDto.getStatus()+"问题描述:"+affiliatedBalanceDto.getStatusText());
                                        }
                                    }
                                }
                                
                            }catch(Exception e){
                                LOGGER.error(e.getMessage());
                                model.addAttribute("account","0.00");
                                model.addAttribute("staceCode","查询可提现金额失败");
                            }
                        }
                        //冻结账户
                        model.addAttribute("frozenAccount",userCiticDTO.getSellerFrozenAccount());
                        try {
                        	Map<String,String> withmap=new HashMap<String, String>();
                            withmap.put("system",MALL);
                            withmap.put("subAccNo",userCiticDTO.getSellerFrozenAccount());
                            withmap.put("sign", Signature.createSign(withmap,PRIKEY));
                            ExecuteResult<AffiliatedBalanceDto> affx= citicExportService.querySubBalance(withmap);
                            AffiliatedBalance affiliatedBalance = affx.getResult().getList().get(0);
                            //冻结账户余额	2015.6.15 加
                            model.addAttribute("djAccount",affiliatedBalance.getKYAMT());
						} catch (Exception e) {
							 LOGGER.error(e.getMessage());
                             model.addAttribute("djAccount","0.00");
                             model.addAttribute("djStaceCode","查询冻结账户余额失败");
						}
                        //卖家中信账户（冻结账号，收款账号）状态
                        model.addAttribute("state", zxstatus.get(userCiticDTO.getAccountState()));
                    }
                }
                //卖家开户行信息
                UserAccountDTO userAccountDTO=userInfoDTO.getUserAccountDTO();
                if(userAccountDTO!=null){
                    //开户行银行所在名
                    String bankName=userAccountDTO.getBankName();
                    model.addAttribute("bankName", bankName);
                    //用户账号
                    String bankAccount=userAccountDTO.getBankAccount();
                    if(bankAccount!=null&&!"".equals(bankAccount.trim())){
                        StringBuffer stringBuffer=new StringBuffer(bankAccount.trim());
                        if(stringBuffer.length()>9){
                            String startcode=stringBuffer.substring(0,5);
                            String endcode=stringBuffer.substring(stringBuffer.length()-4);
                            String bankaccount=startcode+"****"+endcode;
                            model.addAttribute("bankaccount",bankaccount);
                        }else if(stringBuffer.length()>4){
                            String endcode=stringBuffer.substring(stringBuffer.length()-4);
                            String bankaccount="****"+endcode;
                            model.addAttribute("bankaccount",bankaccount);
                        }else{
                            String bankaccount="****"+stringBuffer;
                            model.addAttribute("bankaccount",bankaccount);
                        }
                    }
                    //用户账号名称
                    String bankAccountName=userAccountDTO.getBankAccountName();
                    if(bankAccountName!=null&&!"".equals(bankAccountName.trim())){
                        StringBuffer stringBuffer=new StringBuffer(bankAccountName.trim());
                        if(stringBuffer.length()>0){
                            String bankaccountname=stringBuffer.substring(0,1)+"****";
                            model.addAttribute("bankaccountname",bankaccountname);
                        }
                    }
                }
                Pager pager=new Pager();
                pager.setRows(new Integer(rows));
                pager.setPage(new Integer(page));
                model.addAttribute("pageNo",new Integer(page));
                pager.setTotalCount(0);
                FinanceWithdrawRecordDTO financeWithdrawRecordDTO=new FinanceWithdrawRecordDTO();
                ExecuteResult<List<Long>> erIds = userExportService.queryUserIds(uid);
//                financeWithdrawRecordDTO.setUserId(new Long(uid));
                financeWithdrawRecordDTO.setUserIds(erIds.getResult());
                DataGrid<FinanceWithdrawRecordDTO> dataGrid= financeWithdrawRecordExportService.queryFinanceWithdrawByCondition(financeWithdrawRecordDTO,pager);
                if(dataGrid!=null){
                    List<FinanceWithdrawRecordDTO> list=dataGrid.getRows();
                    if(list!=null&&list.size()>0){
                        Iterator<FinanceWithdrawRecordDTO> iterator=list.iterator();
                        List<Map<String,String>> listMal=new ArrayList<Map<String, String>>();
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        int i=1;
                        while(iterator.hasNext()){
                            FinanceWithdrawRecordDTO financeWithdrawRecordDTO1=iterator.next();
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("no",new Long(i).toString());
                            //提现金额
                            if(financeWithdrawRecordDTO1.getAmount()!=null){
                                map.put("je",financeWithdrawRecordDTO1.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                            }
                            if(financeWithdrawRecordDTO1.getCreatedTime()!=null){
                                map.put("date",simpleDateFormat.format(financeWithdrawRecordDTO1.getCreatedTime()));
                                map.put("codestate",financeWithdrawRecordDTO1.getStatus().toString());

                            }
                            //10：提现处理中  20：提现申请失败 30 提现申请成功  40 提现失败  50 提现成功
                            if(financeWithdrawRecordDTO1.getStatus()!=null){
                                WithdrawEnums withdrawEnums=WithdrawEnums.getEnumByCode(financeWithdrawRecordDTO1.getStatus());
                                if(withdrawEnums!=null){
                                    map.put("stace",withdrawEnums.getLabel());
                                }
                            }
                            if(StringUtils.isNotEmpty(financeWithdrawRecordDTO1.getFailReason())){
                                    map.put("failReason",financeWithdrawRecordDTO1.getFailReason());
                            }
                            if(null!=financeWithdrawRecordDTO1.getUserName()){
                            	map.put("userName", financeWithdrawRecordDTO1.getUserName());
                            }
                            listMal.add(map);
                            i++;
                        }
                        pager.setRecords(listMal);
                    }
                    pager.setTotalCount(dataGrid.getTotal()!=null?dataGrid.getTotal().intValue():0);
                }else{
                    pager.setTotalCount(0);
                }
                model.addAttribute("pager",pager);
            }
        }
        //model.addAttribute("errorMsg","中信接口报错了，无法查询出提现金额");
        //model.addAttribute("staceCode","错误码:10086 错误信息，查询超时了");
        return "/sellcenter/withdraw/withdrawselect";
    }
    /**
     * <p>Description: [提现查询]</p>
     * Created on 2015年3月17日
     * @author  <a href="mailto: menguangyao@camelotchina.com">门光耀</a>
     * @version 1.0
     * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
     */
    @RequestMapping("/deposit")
    @ResponseBody
    public Map<String,Object> deposit(HttpServletRequest request){
        Map<String,Object> map=new HashMap<String, Object>();
        request.setAttribute("map",map);
        try{
            //密码
            String passWard=request.getParameter("passWard");
            //提现金额
            String amount=request.getParameter("amount");
            if(passWard!=null&&!"".equals(passWard.trim())){
                if(amount!=null&&!"".equals(amount.trim())){
                    Long uid = WebUtil.getInstance().getUserId(request);
                    if(uid!=null){
                    	// 获取主账号ID
                    	UserDTO userInfo = userExportService.queryUserById(uid);                    	
                    	Long userId = userInfo.getParentId()==null?userInfo.getUid():userInfo.getParentId();
                    	
                        ExecuteResult<UserInfoDTO> executeResult= userExtendsService.findUserInfo(userId);
                        UserInfoDTO userInfoDTO=executeResult.getResult();
                        if(userInfoDTO!=null) {
                            UserDTO userDTO=userInfoDTO.getUserDTO();
                            if(userDTO!=null){
                                boolean isPwdRight=false;
                                //校验用户支付密码
                                String passWardMd5= MD5.encipher(passWard.trim());
                                //校验用户所输入的密码和支付密码是否一致
                                ExecuteResult<String> executeResult1=userExportService.validatePayPassword(userId,passWardMd5);
                                String miMaresult=executeResult1.getResult();
                                if(miMaresult==null||"".equals(miMaresult)){
                                    isPwdRight=false;
                                }else if("1".equals(miMaresult)){
                                    isPwdRight=true;
                                }else{
                                    isPwdRight=false;
                                }
                                //userPassWord=userDTO.getpa
                                if(isPwdRight){
                                    //银行账号信息
                                    UserAccountDTO userAccountDTO = userInfoDTO.getUserAccountDTO();
                                    if(userAccountDTO!=null){
                                        //中信账户信息
                                        UserCiticDTO userCiticDTO=userInfoDTO.getUserCiticDTO();
                                        if(userCiticDTO!=null){
                                            //卖家收款账户
                                            String accNo=userCiticDTO.getSellerWithdrawsCashAccount();
                                            if(accNo==null||"".equals(accNo)){
                                                map.put("success",false);
                                                map.put("msg","用户中信账户信息不全无法完成提现");
                                                return map;
                                            }
                                            //银行账户
                                            String bankAccount= userAccountDTO.getBankAccount();
                                            if(bankAccount==null||"".equals(bankAccount)){
                                                map.put("success",false);
                                                map.put("msg","开户行账户为空无法转账");
                                                return map;
                                            }

                                            //开户名
                                            String bankAccountName=userAccountDTO.getBankAccountName();
                                            if(bankAccountName==null||"".equals(bankAccountName)){
                                                map.put("success",false);
                                                map.put("msg","开户名为空无法转账");
                                                return map;
                                            }
                                            //开户行
                                            String bankName=userAccountDTO.getBankName();
                                            //开户行联行号
                                            String banklhh=userAccountDTO.getBankBranchJointLine();
                                            if((bankName==null||"".equals(bankName))&&(banklhh==null||"".equals(banklhh))){
                                                map.put("success",false);
                                                map.put("msg","开户行名称和开户行联行号都为空无法转账");
                                                return map;
                                            }
                                            //开户行是否是中心银行,1是，0不是
                                            Integer isCiticBank=userAccountDTO.getIsCiticBank();
                                            Integer ifsamebank=null;
                                            if(isCiticBank==null){
                                                map.put("success",false);
                                                map.put("msg","无法判断开户行是否中信银行");
                                                return map;
                                            }else if(isCiticBank==1){
                                                //是本行
                                                ifsamebank=new Integer(1);
                                            }else if(isCiticBank==0){
                                                //不是本行
                                                ifsamebank=new Integer(0);
                                            }else{
                                                map.put("success",false);
                                                map.put("msg","无法判断开户行是否中信银行");
                                                return map;
                                            }
                                            BigDecimal amountje=new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
                                            if(amountje!=null&&amountje.doubleValue()>0){
                                                //调用提现接口
                                                /********************************/
                                                try{
                                                    Map<String,String> mapSign = new HashMap<String, String>();
                                                    mapSign.put("system", MALL);
                                                    mapSign.put("uid", userId.toString());
                                                    mapSign.put("accType", CiticEnums.AccountType.AccSellReceipt.name());
                                                    mapSign.put("withdrawPrice",amountje.toPlainString() );
                                                    String sign= Signature.createSign(mapSign,PRIKEY);

                                                    AccountInfoDto accountInfoDto=new AccountInfoDto();
                                                    //用户id
                                                    accountInfoDto.setUserId(userId);
                                                    //是个账户中的哪个
                                                    accountInfoDto.setAccType(CiticEnums.AccountType.AccSellReceipt);
                                                    //提现金额
                                                    accountInfoDto.setWithdrawPrice(amountje);
                                                    accountInfoDto.setSystem(MALL);
                                                    accountInfoDto.setSign(sign);
                                                    ExecuteResult<String> result=citicExportService.outPlatformTransfer(accountInfoDto,uid);
                                                    //中信账户，卖家收款账户
                                                    //accountInfoDto.setSubAccNo(accNo);
                                                    //卖家银行账户
                                                    //accountInfoDto.setBindingAccNo(bankAccount);
                                                    //卖家开户名
                                                    //accountInfoDto.setBindingAccNm(bankAccountName);
                                                    //开户行名称
                                                    //accountInfoDto.setBankName(bankName);
                                                    //开户行联行号
                                                    //accountInfoDto.setBankBranchJointLine(banklhh);
                                                    //是否中信银行
                                                    //accountInfoDto.setSameBank(ifsamebank);
                                                    String msg=result.getResultMessage();
                                                    String su=result.getResult();
                                                    LOGGER.error("用户"+uid+"提现结果:  返回msg"+msg+"；  返回结果result："+su);
                                                    if(result.getErrorMessages()!=null&&result.getErrorMessages().size()>0){
                                                        LOGGER.error("用户"+uid+"提现异常:errorMsg："+result.getErrorMessages().get(0));
                                                    }
                                                    /********************************/
                                                    if(result.isSuccess()){
                                                            /*FinanceWithdrawRecordDTO financeWithdrawRecordDTO=new FinanceWithdrawRecordDTO();
                                                            //卖家id
                                                            financeWithdrawRecordDTO.setUserId(new Long(uid));
                                                            //提现金额
                                                            financeWithdrawRecordDTO.setAmount(amountje);
                                                            //提现时间
                                                            financeWithdrawRecordDTO.setCreatedTime(new Date());
                                                            //卖家提现账户
                                                            financeWithdrawRecordDTO.setType(3);
                                                            //20失败，30成功
                                                            financeWithdrawRecordDTO.setStatus(30);
                                                            financeWithdrawRecordExportService.addRecord(financeWithdrawRecordDTO);*/
                                                        map.put("success",true);
                                                        map.put("msg","提现成功:"+(msg!=null?msg:""));
                                                    }else{
                                                            /*FinanceWithdrawRecordDTO financeWithdrawRecordDTO=new FinanceWithdrawRecordDTO();
                                                            //卖家id
                                                            financeWithdrawRecordDTO.setUserId(new Long(uid));
                                                            //提现金额
                                                            financeWithdrawRecordDTO.setAmount(amountje);
                                                            //提现时间
                                                            financeWithdrawRecordDTO.setCreatedTime(new Date());
                                                            //卖家提现账户
                                                            financeWithdrawRecordDTO.setType(3);
                                                            //20失败，30成功
                                                            financeWithdrawRecordDTO.setStatus(20);
                                                            financeWithdrawRecordExportService.addRecord(financeWithdrawRecordDTO);*/
                                                        map.put("success",false);
                                                        if(result.getErrorMessages()!=null&&result.getErrorMessages().size()>0){
                                                            map.put("msg","提现失败:"+result.getErrorMessages().get(0));
                                                        }else{
                                                            map.put("msg","提现失败,请联系后台管理人员");
                                                        }
                                                    }
                                                }catch(Exception e){
                                                    map.put("success",false);
                                                    map.put("msg","提现失败");
                                                    FinanceWithdrawRecordDTO financeWithdrawRecordDTO=new FinanceWithdrawRecordDTO();
                                                    //卖家id
                                                    //financeWithdrawRecordDTO.setUserId(new Long(uid));
                                                    financeWithdrawRecordDTO.setUserId(userId);
                                                    //提现金额
                                                    financeWithdrawRecordDTO.setAmount(amountje);
                                                    //提现时间
                                                    financeWithdrawRecordDTO.setCreatedTime(new Date());
                                                    //卖家提现账户
                                                    financeWithdrawRecordDTO.setType(3);
                                                    //20失败，30成功
                                                    financeWithdrawRecordDTO.setStatus(20);
                                                    financeWithdrawRecordExportService.addRecord(financeWithdrawRecordDTO);
                                                    LOGGER.error("提现异常信息:"+e.getMessage());
                                                }
                                            }else{
                                                map.put("success",false);
                                                map.put("msg","金额必须大于零");
                                            }
                                        }else{
                                            map.put("success",false);
                                            map.put("msg","用户中信账户信息不全无法完成提现");
                                        }
                                    }else{
                                        map.put("success",false);
                                        map.put("msg","用户银行账户信息不全无法完成提现");
                                    }
                                }else{
                                    map.put("success",false);
                                    map.put("msg","支付密码不正确无法完成提现");
                                }

                            }else{
                                map.put("success",false);
                                map.put("msg","用户基本信息不全无法完成提现");
                            }
                        }else{
                            map.put("success",false);
                            map.put("msg","用户基本信息不全无法完成提现");
                        }
                    }else{
                        map.put("success",false);
                        map.put("msg","登录超时请重新登录");
                    }
                }else{
                    map.put("success",false);
                    map.put("msg","请填写支付金额");
                }
            }else{
                map.put("success",false);
                map.put("msg","支付密码不能为空或全为空格");
            }
        }catch(Exception e){
            LOGGER.error("提现异常信息"+e.getMessage());
            map.put("success",false);
            map.put("msg","提现失败");
        }
        return map;
    }
}