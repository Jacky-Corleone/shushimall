package com.camelot.mall.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoQueryDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.service.MallDocumentService;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.common.util.Signature;
import com.camelot.example.controller.WeChatMsgProcess;
import com.camelot.example.controller.WeChatProcess;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.bank.BankOrder;
import com.camelot.mall.orderWx.SendWeiXinMessage;
import com.camelot.mall.service.ShopCartService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.PaymentWxExportService;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.citic.auxiliary.AffiliatedBalance;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserWxDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserWxExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.SendWeiXinMessageUtil;
import com.camelot.util.WebUtil;
import com.camelot.util.WeiXinMessageModeId;
import com.camelot.weixin.api.WxSnsAPI;
import com.camelot.weixin.bean.json.WxSnsAccessToken;
import com.camelot.weixin.bean.json.WxUserInfo;

@Controller
@RequestMapping("/person")
public class PersonController {
    private static final String VALUE_REGISTER_DUPLICATE_REDIS = "0";
    private Logger logger = LoggerFactory.getLogger(PersonController.class);
    @Resource
    private UserExportService userExportService;
    @Resource
    private TradeReturnExportService tradeReturnExportService;
    @Resource
    private StatementExportService statementExportService;
    @Resource
    private PaymentExportService paymentExportService;
    @Resource
    private PaymentWxExportService paymentWxExportService;
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private TradeOrderExportService tradeOrderExportService;
    @Resource
    private ItemExportService itemService;
    @Resource
    private ComplainExportService complainExportService;
    @Resource
    private ShopCartService shopCartService;
    @Resource
    private UserExtendsService userExtendsService;
    @Resource
    private UserWxExportService userWxExtendsService;
    @Resource
    private RedisDB redisDB;
    @Resource
    private CiticExportService citicExportService;
    @Resource
    private MallDocumentService mallDocumentService;

    /**
     * <p>Discription:[个人中心主页]</p>
     * Created on 2015-6-9
     *
     * @return
     * @author:[gec]
     */
    @RequestMapping("/personMain")
    public String personMain(String userName, String phone) {
        //快速登录注册
        RegisterInfoDTO registerInfoDTO = new RegisterInfoDTO();
        registerInfoDTO.setLoginname(userName);
        registerInfoDTO.setLinkPhoneNum(phone);
        //		userExportService.registerUser(registerInfoDTO);
        return "/person/personMain";
    }


    /**
     * 快速注册
     */
    @RequestMapping("/quickRegist")
    public String quickRegist() {
        return "/person/quickRegist";
    }

    /**
     * 快速注册成功
     */
    @RequestMapping("/quickRegSuc")
    public String quickRegSuc(Model model, HttpSession session, String loginname, String loginPhone, String personalPhoneCaptchaKey) {
        String keyPersonalRegisterDuplicateRedis = WebUtil.getInstance().getRegisterToken();
        redisDB.addObject(keyPersonalRegisterDuplicateRedis, VALUE_REGISTER_DUPLICATE_REDIS, 1800);
        session.setAttribute("loginname", loginname);
        session.setAttribute("loginPhone", loginPhone);
        model.addAttribute("keyPersonalRegisterDuplicateRedis", keyPersonalRegisterDuplicateRedis);
        model.addAttribute("loginname", loginname);
        model.addAttribute("loginPhone", loginPhone);
        return "/person/quickRegSuc";
    }


    /**
     * 账号绑定
     */
    @RequestMapping("/bindingAccountMsg")
    public String bindingAccountMsg(HttpServletRequest request, String pwd, String cpwd, String msgVer) {
        JSONObject jores = new JSONObject();
        if (pwd.equals(cpwd)) {
            Long uid = WebUtil.getInstance().getUserId(request);
            jores.put("msg", "账号绑定成功!");
            logger.info("账号绑定成功!");
        } else {
            jores.put("msg", "两次输入的密码不一致,账号绑定失败!");
            logger.error("两次输入的密码不一致,账号绑定失败!");
        }
        return jores.toJSONString();
    }

    /**
     * 忘记密码
     */
    @RequestMapping("/modifyPwd")
    public String modifyPwd() {
        return "/person/modifyPwd";
    }

    /**
     * 忘记密码 重置密码
     */
    @RequestMapping("/modifyPwdSuc")
    public String modifyPwdSuc(Model model, HttpSession sesson, String loginname, String personalPhoneCaptchaKey) {
        String keyPersonalRegisterDuplicateRedis = WebUtil.getInstance().getRegisterToken();
        redisDB.addObject(keyPersonalRegisterDuplicateRedis, VALUE_REGISTER_DUPLICATE_REDIS, 1800);
        sesson.setAttribute("loginname", loginname);
        model.addAttribute("keyPersonalRegisterDuplicateRedis", keyPersonalRegisterDuplicateRedis);
        model.addAttribute("loginname", loginname);
        return "/person/modifyPwdSuc";
    }

    /**
     * 忘记密码
     */
    @RequestMapping("/loginModifyPwd")
    public String loginModifyPwd(String loginname,Model model) {
        model.addAttribute("loginname",loginname);
        return "/person/loginModifyPwd";
    }

    /**
     * 确认修改密码
     */
    @RequestMapping("/conModifyPwd")
    @ResponseBody
    public String conModifyPwd(HttpServletRequest request, HttpServletResponse response, String loginname, String cpwd) {
        Map map = new HashMap();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            JSONObject jores = new JSONObject();
            String token = LoginToken.getLoginToken(request);
            RegisterDTO registerDTO = userExportService.getUserByRedis(token);
            Long uid = registerDTO.getUid();
            userExportService.resetUserPassword(uid, MD5.encipher(cpwd));
            //初始化登陆
            this.initLogin(loginname, cpwd, request, response);
            map.put("result", true);
            map.put("message", "密码修改成功!");
            //发送信息
            UserWxDTO userWxDTO = new UserWxDTO();
            userWxDTO.setUname(loginname);
            ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.getUserInfoByOpenId(userWxDTO);
            if (executeResult.getResult() != null) {
                UserWxDTO userWx = executeResult.getResult();
                if (userWx.getWxopenid() != null) {
                	logger.info("账号密码修改成功----------------------");
                    WeChatProcess weChatProcess = new WeChatMsgProcess();
                    Map simap = new HashMap();
                    simap.put("openId", userWx.getWxopenid());
                    simap.put("modeId", WeiXinMessageModeId.CHANGE_PASSWORD);
                    simap.put("first", "修改成功！");
                    simap.put("productName", loginname);
                    simap.put("time", df.format(new Date()));
                    simap.put("remark", "账号密码修改成功！");
                    weChatProcess.SendInformation(simap, request, response);
                }
            }
        } catch (Exception e) {
            map.put("result", false);
            map.put("message", "密码修改失败!");
        }
        return JSONObject.toJSONString(map);
    }


    /**
     * 历史信息
     */
    @RequestMapping("/hisMsg")
    public String hisMsg() {
        return "/person/hisMsg";
    }

    /**
     * 个人信息
     */
    @RequestMapping("/perMsg")
    public String perMsg() {
        return "/person/perMsg";
    }

    /**
     * 个人信息跳转
     */
    @RequestMapping("/sendPerMsg")
    public String sendPerMsg(Model model) {
        //抓取登录用户ID
        //	    Long uid = WebUtil.getInstance().getUserId(request);
        //	    System.out.println("===========================" + uid);
        //	    UserDTO uu = userExportService.queryUserById(uid);
        UserDTO uu = new UserDTO();
        uu.setUname("12312");
        model.addAttribute("uu", uu);
        return "/person/perMsg";
    }

    /**
     * 绑定公司账户
     */
    @RequestMapping("/companyBinding")
    public String companyBinding() {
        return "/person/companyBinding";
    }


    /**
     * 支付买家按钮功能
     * 暂时不用
     *
     * @throws Exception
     */

    public String payOrder(HttpServletRequest request) throws Exception {
        //判断买家进行认证没有
        Long uid = WebUtil.getInstance().getUserId(request);
        UserDTO user = userExportService.queryUserById(uid);
        if ("4".equals(user.getUserstatus())) {
            //金融通
            //代表买家通过验证
            Map<String, String> payParams = new HashMap<String, String>();
            payParams.put("system", "MALL");
            //前台接受的参数outTradeNo
            payParams.put("outTradeNo", "12345667");
            payParams.put("uid", uid.toString());
            payParams.put("accType", AccountType.AccBuyPay.getCode() + "");
            payParams.put("sign", Signature.createSign(payParams, "123456"));
            Map<String, String> map = citicExportService.payCitic(payParams);
            paymentExportService.payResult(payParams, PayBankEnum.CITIC.name());

        } else {
            //没有通过验证
        }
        return "";
    }

    /**
     * 支付买家按钮功能金融通
     *
     * @throws Exception
     */
    @RequestMapping("/payOrderJ")
    @ResponseBody
    public ExecuteResult<Integer> payOrderJ(String orderNo, String type, String paymentMethod) {
        ExecuteResult<Integer> er = new ExecuteResult<Integer>();
        PayReqParam param = new PayReqParam();
        param.setOrderNo(orderNo);

        if ("1".equals(type)) {
            param.setPayOrderType(PayOrderTypeEnum.child);
        } else {
            param.setPayOrderType(PayOrderTypeEnum.Parent);
        }
        param.setPayBank(PayBankEnum.getEnumByName(paymentMethod));

        try {
            er = this.paymentExportService.pay(param);
        } catch (Exception e) {
            logger.error("支付订单失败！", e);
            er.addErrorMessage(e.getMessage());
        }
        return er;
    }


    /**
     * 支付买家按钮功能网银
     *
     * @throws Exception
     */

    @RequestMapping("/payOrderW")
    @ResponseBody
    public ExecuteResult<String> payOrderW(HttpServletRequest request, BankOrder order) {
        String sUid = CookieHelper.getCookieVal(request, Constants.USER_ID);
        Long uid = Long.valueOf(sUid);
        logger.debug(JSON.toJSONString(order));
        ExecuteResult<String> erStr = this.userExportService.validatePayPassword(uid, MD5.encipher(order.getPayPassword()));
        logger.debug("支付密码校验：" + erStr);
        order.setBuyer(uid);

        if (erStr.isSuccess() && "1".equals(erStr.getResult())) {
            ExecuteResult<OrderInfoPay> er = new ExecuteResult<OrderInfoPay>();
            try {
                // 必填信息设置
                Map<String, String> parameterMap = new HashMap<String, String>();
                parameterMap = new HashMap<String, String>();
                parameterMap.put("system", SysProperties.getProperty("transfer.system"));
                parameterMap.put("outTradeNo", order.getOutTradeNo());
                parameterMap.put("accType", AccountType.AccBuyPay.getCode() + "");
                parameterMap.put("uid", order.getBuyer().toString());
                parameterMap.put("sign", Signature.createSign(parameterMap, SysProperties.getProperty("transfer.prikey")));

                Map<String, String> payResult = this.citicExportService.payCitic(parameterMap);

                logger.debug("CITIC PAY PARAMS：" + parameterMap);
                er = this.paymentExportService.payResult(payResult, PayBankEnum.CITIC.name());
                logger.debug("CITIC PAY RESULT：" + er);

                if (!er.isSuccess()) {
                    erStr.addErrorMessage("订单支付失败!");
                    erStr.setErrorMessages(er.getErrorMessages());
                }
            } catch (Exception e) {
                logger.error("中信支付失败！", e);
                erStr.addErrorMessage("订单支付失败!");
            }
        } else {
            erStr.addErrorMessage("支付密码错误！");
        }

        return erStr;
    }

    /**
     * 平台点击结算
     *
     * @throws Exception
     */
    public ExecuteResult<String> panelCon(@PathVariable Long id) throws Exception {
        //前台参数结算单settlementId
        ExecuteResult<String> result = statementExportService.modifySettlementStates(id);
        return result;
    }

    /**
     * <p>Discription:[卖家点击结算]</p>
     * Created on 2015-7-3
     *
     * @return
     * @throws Exception
     * @author:[创建者中文名字]
     */
    public ExecuteResult<String> sellerCon(@PathVariable Long settlementId) throws Exception {
        //前台参数结算单settlementId
        ExecuteResult<String> result = statementExportService.proceedSettle(settlementId);
        return result;
    }

    /**
     * <p>Discription:[确认收货]</p>
     *
     * @return
     * @author:[创建者中文名字]
     */
    @ResponseBody
    @RequestMapping({"confirmReceipt"})
    public ExecuteResult<String> confirmReceipt(HttpServletRequest request, Model model) {
        String orderId = request.getParameter("orderId");
        String userToken = LoginToken.getLoginToken(request);
        String paypwd = request.getParameter("paypwd");
        ExecuteResult<String> result = new ExecuteResult<String>();
        List<String> errs = new ArrayList<String>();
        if (StringUtils.isEmpty(userToken)) {
            errs.add("用户未登录！");
            result.setErrorMessages(errs);
            request.setAttribute("result", result);
            return result;
        }
        RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
        if (registerDto == null) {
            errs.add("未找到登录用户的信息！");
            result.setErrorMessages(errs);
            request.setAttribute("result", result);
            return result;
        }
        if (StringUtils.isEmpty(orderId) && !StringUtils.isNumeric(orderId)) {
            errs.add("订单号不能为空，并且必须是数字");
            result.setErrorMessages(errs);
            request.setAttribute("result", result);
            return result;
        }
        if (StringUtils.isEmpty(paypwd)) {
            errs.add("支付密码不能为空");
            result.setErrorMessages(errs);
            request.setAttribute("result", result);
            return result;
        }
        Long uid = registerDto.getUid();
        paypwd = MD5.encipher(paypwd);
        ExecuteResult<String> payResult = userExportService.validatePayPassword(uid, paypwd);
        //System.out.println("payResult===="+JSON.toJSONString(payResult));
        if ("1".equals(payResult.getResult())) {
            ExecuteResult<TradeOrdersDTO> resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
            if (resultTradeOrdersDTO != null && resultTradeOrdersDTO.getResult() != null) {
                Integer nstate = resultTradeOrdersDTO.getResult().getState();
                Integer refund = resultTradeOrdersDTO.getResult().getRefund();
                if (nstate == 3) {//确认收货状态
                    if (refund == 2) {//订单是在申请退款
                        TradeOrdersDTO orderDTO = new TradeOrdersDTO();
                        orderDTO.setOrderId(orderId);
                        orderDTO.setRefund(1);
                        tradeOrderExportService.updateTradeOrdersDTOSelective(orderDTO);

                        //先查出退款订单，然后再修改退款订单
                        TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
                        TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
                        tradeReturnDto.setOrderId(orderId);
                        queryDto.setTradeReturnDto(tradeReturnDto);
                        DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
                        if (dg != null && dg.getTotal() > 0) {
                            for (TradeReturnGoodsDTO dto : dg.getRows()) {
                                dto.setDeletedFlag("1");
                                dto.setState(TradeReturnStatusEnum.CLOSE.getCode());
                                tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.CLOSE);

                                //取消退款单的投诉
                                complainCancle1(dto.getId().toString());
                            }
                        }
                    }
                    //“确认收货”这一步完成后下一状态应该是 4待评价
                    result = tradeOrderExportService.modifyOrderStatus(orderId, 4);
                }
            }
        } else {
            errs.add("支付密码错误，请重新输入！");
            result.setErrorMessages(errs);
            request.setAttribute("result", result);
            return result;
        }
        return result;
    }


    /**
     * 网银同步回传结果
     *
     * @throws Exception
     */
    @RequestMapping("/payCallBack/{payBank}")
    public String payCallBack(@PathVariable("payBank") String payBank, HttpServletRequest request, HttpServletResponse respons,HttpServletResponse response, Model model) {
        logger.info("银行回调同步方法");
        @SuppressWarnings("unchecked")
        Enumeration<String> names = request.getParameterNames();
        Map<String, String> params = new HashMap<String, String>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            params.put(name, value);
        }
        logger.error("银行回调同步+++++++++++++++++" + params);
        logger.info("银行回调同步方法 参数解析..");
        params.put("isNotify", "false");
        logger.info("银行回调同步方法  调用执行接口");
        ExecuteResult<OrderInfoPay> er = this.paymentWxExportService.payResult(params, payBank);
        model.addAttribute("executeResult", er);
        model.addAttribute("payBank", payBank);
        request.setAttribute("er", er);
        
        logger.info("-------------------------买家已成功完成付款----------------------");
        SendWeiXinMessage message = new SendWeiXinMessage();
		message.setModeId(WeiXinMessageModeId.PAYMENT_SUCCESS);
		message.setFirst("【印刷家】尊敬的用户，买家已成功完成付款，订单号（" + er.getResult().getOrderNo() + "），印刷家提醒您及时查看并尽快完成发货。");
		message.setOrderMoneySum(er.getResult().getOrderAmount().toString());
		//查询到订单号
        String orderNo=er.getResult().getOrderNo();
        //查询到订单
        ExecuteResult<TradeOrdersDTO> todres= tradeOrderExportService.getOrderById(orderNo);

        Long sellerid=todres.getResult().getSellerId();

        Set<String> sellerSet=new HashSet<String>();
        sellerSet.add(null==sellerid?null:sellerid.toString());
        //根据订单查询子订单
        TradeOrdersQueryInDTO toqid=new TradeOrdersQueryInDTO();
        toqid.setParentOrderId(orderNo);
        Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
        pager.setPage(1);
        pager.setRows(Integer.MAX_VALUE);
        ExecuteResult<DataGrid<TradeOrdersDTO>> childrenOrdersRes= tradeOrderExportService.queryOrders(toqid, pager);
        DataGrid dg01=childrenOrdersRes.getResult();
        //把子订单的店铺id保存到set中
        if(null!=dg01){
            List<TradeOrdersDTO> childrenOrderRows=dg01.getRows();
            if(null!=childrenOrderRows && childrenOrderRows.size()>0){
                for(TradeOrdersDTO todto : childrenOrderRows){
                    sellerSet.add(null==todto.getSellerId()?null:todto.getSellerId().toString());
                }
            }
        }
        //全部发送微信消息
        for(String _sellid : sellerSet) {
            if(StringUtils.isNotEmpty(_sellid)){
                SendWeiXinMessageUtil.sendWeiXinMessage(_sellid, message, request, response);
            }
        }
        return "/person/pay_result";
    }

    /**
     * 网银同步回传结果
     *
     * @throws Exception
     */
    @RequestMapping("/payCallBackSuccess/{payBank}")
    public String payCallBackSuccess(@PathVariable("payBank") String payBank, HttpServletRequest request, HttpServletResponse respons, Model model) {
        logger.info("银行回调同步方法");
        /*ExecuteResult<String> executeResult = new ExecuteResult<String>();
        executeResult.setResult("2");
        request.setAttribute("er", executeResult);*/
        
        logger.info("银行回调同步方法--京东支付方式");
        @SuppressWarnings("unchecked")
        Enumeration<String> names = request.getParameterNames();
        Map<String, String> params = new HashMap<String, String>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            params.put(name, value);
        }
        logger.error("银行回调同步+++++++++++++++++" + params);
        logger.info("银行回调同步方法 参数解析..");
        params.put("isNotify", "false");
        logger.info("银行回调同步方法  调用执行接口");
        ExecuteResult<OrderInfoPay> er = this.paymentWxExportService.payResult(params, payBank);
        model.addAttribute("executeResult", er);
        model.addAttribute("payBank", payBank);
        request.setAttribute("er", er);
        
        logger.info("-------------------------买家已成功完成付款--------京东---同步-----------");
        
        
        return "/person/pay_result";
    }

    /**
     * 网银同步回传结果
     *
     * @throws Exception
     */
    @RequestMapping("/payCallBackFailure/{payBank}")
    public String payCallBackFailure(@PathVariable("payBank") String payBank, HttpServletRequest request, HttpServletResponse respons, Model model) {
        logger.info("银行回调同步方法");
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        List<String> list = new ArrayList<String>();
        list.add("支付失败，请重新支付！");
        executeResult.setErrorMessages(list);
        request.setAttribute("er", executeResult);
        return "/person/pay_result";
    }

    /**
     * 网银异步回传结果
     *
     * @throws Exception
     */

    @RequestMapping("/payResult/{payBank}")
    @ResponseBody
    public String payResult(@PathVariable("payBank") String payBank, HttpServletRequest request, HttpServletResponse response) {
        logger.info("银行回调异步方法");
        logger.info("------------------------------------------哈哈------------------------------------------------");
        Enumeration<String> names = request.getParameterNames();
        Map<String, String> params = new HashMap<String, String>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            params.put(name, value);
        }
        logger.error("银行回调异步+++++++++++++++++" + JSONObject.toJSON(params));
        params.put("isNotify", "true");
        ExecuteResult<OrderInfoPay> er = this.paymentWxExportService.payResult(params, payBank);
        logger.error("银行回调异步+++++++++++++++++" + JSONObject.toJSON(er));
        request.setAttribute("er", er);
        logger.info("-------------------------买家异步已成功完成付款----------------------");
        //发送信息
//        String token = LoginToken.getLoginToken(request);
//        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
//        UserWxDTO userWxDTO = new UserWxDTO();
//        userWxDTO.setUname(registerDTO.getLoginname());
//        ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.getUserInfoByOpenId(userWxDTO);
//        logger.info("executeResult============="+JSON.toJSONString(executeResult));
//        if (executeResult.isSuccess() && executeResult.getResult() != null) {
//            UserWxDTO userWx = executeResult.getResult();
//            if (userWx.getWxopenid() != null) {
            	//呵呵这是我改的发消息的
//                WeChatProcess weChatProcess = new WeChatMsgProcess();
//                Map simap = new HashMap();
//                simap.put("openId", userWx.getWxopenid());
//                simap.put("modeId", WeiXinMessageModeId.PAYMENT_SUCCESS);
//                simap.put("first", "支付成功！");
//                simap.put("orderMoneySum", er.getResult().getOrderAmount().toString());
//                ExecuteResult<TradeOrdersDTO> tradeOrdersDTO = tradeOrderExportService.getOrderById(Long.valueOf(er.getResult().getOrderNo()));;
//                List<TradeOrderItemsDTO> items = tradeOrdersDTO.getResult().getItems();
//                StringBuffer sb = new StringBuffer();
//                for(TradeOrderItemsDTO i : items){
//                	sb.append(i.getSkuName()).append(";");
//                }
//                simap.put("orderProductName", sb.toString());
//                simap.put("remark", "支付成功！");
//                weChatProcess.SendInformation(simap, request, response);
//            }
//        }
        //如果不支付，直接跳转到全部订单页面
        if(er.getResult() == null){
        	try {
				response.sendRedirect(request.getContextPath()+"/orderWx/toOrderSubmit?orderSource=buyers");
			} catch (IOException e) {
				e.printStackTrace();
			}
        	return null;
        }else{
        	SendWeiXinMessage message = new SendWeiXinMessage();
            message.setModeId(WeiXinMessageModeId.PAYMENT_SUCCESS);
            message.setFirst("【印刷家】尊敬的用户，买家已成功完成付款，订单号（"+ er.getResult().getOrderNo() +"），印刷家提醒您及时查看并尽快完成发货。");
            message.setOrderMoneySum(er.getResult().getOrderAmount().toString());
            ExecuteResult<TradeOrdersDTO> tradeOrdersDTO = tradeOrderExportService.getOrderById(er.getResult().getOrderNo());;
            List<TradeOrderItemsDTO> items = tradeOrdersDTO.getResult().getItems();
            StringBuffer sb = new StringBuffer();
            for(TradeOrderItemsDTO i : items){
            	sb.append(i.getSkuName()).append(";");
            }
            message.setOrderProductName(sb.toString());
            message.setRemark("支付成功!");
            SendWeiXinMessageUtil.sendWeiXinMessage(er.getResult().getSeller(), message, request, response);
        }
        
        return er.getResultMessage();
    }

    /**
     * 买家退款，生成退款单
     *
     * @throws Exception
     */
    @RequestMapping("/payBack")
    @ResponseBody
    public ExecuteResult<TradeReturnGoodsDTO> payBack(HttpServletRequest request, Model model) {
        ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
        request.setAttribute("result", result);
        List<String> errorMessages = new ArrayList<String>();
        String orderId = request.getParameter("orderId");
        if (StringUtils.isEmpty(orderId) && !StringUtils.isNumeric(orderId)) {
            errorMessages.add("订单id不正确");
            result.setErrorMessages(errorMessages);
            return result;
        }
        /*String jsonRefundItem = request.getParameter("jsonRefundItem");
		if(StringUtils.isEmpty(jsonRefundItem)){
			errorMessages.add("订单退货商品不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}*/
        String returnResult = request.getParameter("returnResult");
        if (StringUtils.isEmpty(returnResult)) {
            errorMessages.add("退款原因不能为空");
            result.setErrorMessages(errorMessages);
            return result;
        }
        String remark = request.getParameter("remark");
        if (StringUtils.isEmpty(remark)) {
            errorMessages.add("退款说明不能为空");
            result.setErrorMessages(errorMessages);
            return result;
        }
		/*String refundGoods = request.getParameter("refundGoods");//  退款货品总金额
		if(StringUtils.isEmpty(refundGoods) && !StringUtils.isNumeric(refundGoods)){
			errorMessages.add("退款金额不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}*/
        String refundFreight = request.getParameter("refundFreight");//退款运费金额
        String picUrl = request.getParameter("picUrl");//上传凭证
        List<TradeReturnPicDTO> picDTOList = new ArrayList<TradeReturnPicDTO>();
        if (StringUtils.isNotEmpty(picUrl)) {
            String[] arrayPic = picUrl.split(";");
            for (String pic : arrayPic) {
                TradeReturnPicDTO picDto = new TradeReturnPicDTO();
                picDto.setPicUrl(pic);
                picDTOList.add(picDto);
            }
        }
        TradeReturnInfoDto insertDto = new TradeReturnInfoDto();

        TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
        tradeOrdersQueryInDTO.setOrderId(orderId);
        ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, null);
        DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
        if (executeResult != null) {
            dataGrid = executeResult.getResult();
            List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
            if (tradeOrdersDTOs != null && tradeOrdersDTOs.size() > 0) {
                //根据订单id 查询只会查询出一条记录
                TradeOrdersDTO tradeOrdersDTO = tradeOrdersDTOs.get(0);
                //构造订单信息
                TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
                tradeReturnDto.setOrderId(orderId);
                Integer state = tradeOrdersDTO.getState();
                if (state != null) {
                    tradeReturnDto.setOrderStatus(state.toString());
                    if (state > 3 && state < 6) {
                        tradeReturnDto.setIsCustomerService("1");
                    } else {
                        tradeReturnDto.setIsCustomerService("0");
                    }
                }
                tradeReturnDto.setBuyId(tradeOrdersDTO.getBuyerId() + "");
                UserDTO user = userExportService.queryUserById(tradeOrdersDTO.getBuyerId());
                if (user != null) {
                    tradeReturnDto.setBuyerName(user.getUname());
                }
                tradeReturnDto.setBuyerAddress(tradeOrdersDTO.getFullAddress());
                tradeReturnDto.setBuyerPhone(tradeOrdersDTO.getMobile());
                tradeReturnDto.setSellerId(tradeOrdersDTO.getSellerId() + "");
                tradeReturnDto.setOrderPrice(tradeOrdersDTO.getPaymentPrice());
                tradeReturnDto.setReturnResult(returnResult);//  退货原因
                tradeReturnDto.setRemark(remark);//  问题描述
                if (StringUtils.isNotEmpty(refundFreight)) {
                    tradeReturnDto.setRefundFreight(new BigDecimal(refundFreight));
                }

                //tradeReturnDto.setRefundGoods(new BigDecimal(refundGoods));
                tradeReturnDto.setRefundGoods(tradeOrdersDTO.getPaymentPrice());

                //获取店铺名称
                ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
                if (result_shopDto != null && result_shopDto.getResult() != null) {
                    ShopDTO shopDTO = result_shopDto.getResult();
                    tradeReturnDto.setReturnAddress(shopDTO.getProvinceName() + "  " + shopDTO.getDistrictName() + "  " + shopDTO.getStreetName());
                    tradeReturnDto.setReturnPhone(shopDTO.getMobile() + "");
                    tradeReturnDto.setReturnPostcode(shopDTO.getZcode());
                }
                tradeReturnDto.setPicDTOList(picDTOList);
                //System.out.println("订单信息tradeReturnDto========="+JSON.toJSONString(tradeReturnDto));
                insertDto.setTradeReturnDto(tradeReturnDto);

                //构造订单下退货商品的信息
                List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailList = new ArrayList<TradeReturnGoodsDetailDTO>();
                //JSONArray jsonArray = JSON.parseArray(jsonRefundItem);

                List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
                for (TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs) {
                    //for(int i=0;i<jsonArray.size(); i++){
                    //	JSONObject jsonobj = (JSONObject) jsonArray.get(i);
                    //	Long itemId = Long.valueOf((String) jsonobj.get("itemId"));
                    //	Long skuId = Long.valueOf((String) jsonobj.get("skuId"));
                    //	if(tradeOrderItemsDTO.getItemId().equals(itemId) && tradeOrderItemsDTO.getSkuId().equals(skuId)){
                    TradeReturnGoodsDetailDTO goodGto = new TradeReturnGoodsDetailDTO();
                    goodGto.setGoodsId(tradeOrderItemsDTO.getItemId());
                    goodGto.setSkuId(tradeOrderItemsDTO.getSkuId());
                    goodGto.setRerurnCount(tradeOrderItemsDTO.getNum());
                    goodGto.setReturnAmount(tradeOrderItemsDTO.getPayPriceTotal());
                    goodGto.setPayPrice(tradeOrderItemsDTO.getPayPrice());
                    //获取商品图片
                    ItemShopCartDTO dto = new ItemShopCartDTO();
                    dto.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");//省市区编码
                    dto.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
                    dto.setQty(tradeOrderItemsDTO.getNum());//数量
                    dto.setShopId(tradeOrdersDTO.getShopId());//店铺ID
                    dto.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
                    ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
                    ItemShopCartDTO itemShopCartDTO = er.getResult();
                    if (null != itemShopCartDTO) {
                        goodGto.setGoodsPicUrl(itemShopCartDTO.getSkuPicUrl());
                    }
                    //获取商品名称
                    ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
                    if (result_itemDTO != null && result_itemDTO.getResult() != null) {
                        ItemDTO itemDTO = result_itemDTO.getResult();
                        goodGto.setGoodsName(itemDTO.getItemName());
                    }
                    tradeReturnGoodsDetailList.add(goodGto);
                    //	}
                    //}
                }
                //System.out.println("商品列表tradeReturnGoodsDetailList========="+JSON.toJSONString(tradeReturnGoodsDetailList));
                insertDto.setTradeReturnGoodsDetailList(tradeReturnGoodsDetailList);
            }
            result = tradeReturnExportService.createTradeReturn(insertDto, TradeReturnStatusEnum.AUTH);
        }
        return result;
    }

    /**
     * 卖家点击确认
     *
     * @throws Exception
     */
    @RequestMapping("/sellerBack")
    @ResponseBody
    public ExecuteResult<TradeReturnGoodsDTO> sellerBack(HttpServletRequest request, Model model) {
        ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
        request.setAttribute("result", result);
        List<String> errorMessages = new ArrayList<String>();
        String returnId = request.getParameter("returnId");
        if (StringUtils.isEmpty(returnId) || !StringUtils.isNumeric(returnId)) {
            errorMessages.add("退款id不能为空，必须是数字");
            result.setErrorMessages(errorMessages);
            return result;
        }
        String type = request.getParameter("type");//1、同意退款  2、不同意退款
        if (StringUtils.isEmpty(type) || !StringUtils.isNumeric(type)) {
            errorMessages.add("操作类型type不能为空，必须是数字");
            result.setErrorMessages(errorMessages);
            return result;
        }
        Long userId = -1L;
        String userToken = LoginToken.getLoginToken(request);
        if (StringUtils.isNotEmpty(userToken)) {
            RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
            if (registerDto != null) {
                userId = registerDto.getUid();
            }
        }
        Long parentId = -1L;
        UserDTO userDto = userExportService.queryUserById(userId);
        if (userDto != null) {
            if (userDto.getParentId() != null) {
                parentId = userDto.getParentId();
            }
        }
        if ("1".equals(type)) {
            //同意退款
            String orderId = request.getParameter("orderId");
            if (StringUtils.isEmpty(orderId) || !StringUtils.isNumeric(orderId)) {
                errorMessages.add("订单Id不能为空，必须是数字");
                result.setErrorMessages(errorMessages);
                return result;
            }
            ExecuteResult<TradeOrdersDTO> resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
            if (resultTradeOrdersDTO != null && resultTradeOrdersDTO.getResult() != null) {
                Integer state = resultTradeOrdersDTO.getResult().getState();
                if (state > 3 && state < 6) {
                    //买家已收到货
                    //卖家填写收货地址，让买家发货
                    String returnAddress = request.getParameter("returnAddress");
                    String returnPhone = request.getParameter("returnPhone");
                    String returnPostcode = request.getParameter("returnPostcode");

                    TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
                    dto.setId(Long.valueOf(returnId));
                    dto.setState(TradeReturnStatusEnum.PASS.getCode());
                    if (StringUtils.isNotEmpty(returnAddress)) {
                        dto.setReturnAddress(returnAddress);
                    }
                    if (StringUtils.isNotEmpty(returnPhone)) {
                        dto.setReturnPhone(returnPhone);
                    }
                    if (StringUtils.isNotEmpty(returnPostcode)) {
                        dto.setReturnPostcode(returnPostcode);
                    }
                    if (parentId != null && parentId.intValue() > 0) {
                        dto.setSellerId(parentId.toString());
                    } else {
                        dto.setSellerId(userId.toString());
                    }
                    dto.setOrderId(orderId);
                    result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.PASS);
                } else {
                    //买家未收货
                    String paypwd = request.getParameter("paypwd");
                    if (userId.equals(new Long(-1))) {//判断用户是否登录
                        errorMessages.add("用户未登录！");
                        result.setErrorMessages(errorMessages);
                        return result;
                    }
                    if (StringUtils.isEmpty(paypwd)) {
                        errorMessages.add("支付密码不能为空");
                        result.setErrorMessages(errorMessages);
                        return result;
                    }
                    paypwd = MD5.encipher(paypwd);
                    ExecuteResult<String> payResult = userExportService.validatePayPassword(userId, paypwd);
                    if ("1".equals(payResult.getResult())) {//验证支付码是否正确
                        TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
                        dto.setId(Long.valueOf(returnId));
                        dto.setOrderId(orderId);
                        dto.setState(TradeReturnStatusEnum.REFUNDING.getCode());
                        if (parentId != null && parentId.intValue() > 0) {
                            dto.setSellerId(parentId.toString());
                        } else {
                            dto.setSellerId(userId.toString());
                        }
                        result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.REFUNDING);
                    } else {
                        errorMessages.add("支付密码错误，请重新输入！");
                        result.setErrorMessages(errorMessages);
                        return result;
                    }
                }
            }
        } else if ("2".equals(type)) {
            //不同意退款
            String auditRemark = request.getParameter("auditRemark");
            if (StringUtils.isEmpty(auditRemark) && StringUtils.isNumeric(auditRemark)) {
                errorMessages.add("拒绝退款原因不能为空");
                result.setErrorMessages(errorMessages);
                return result;
            }
            TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
            dto.setId(Long.valueOf(returnId));
            dto.setState(TradeReturnStatusEnum.DISAGRESS.getCode());
            if (parentId != null && parentId.intValue() > 0) {
                dto.setSellerId(parentId.toString());
            } else {
                dto.setSellerId(userId.toString());
            }
            dto.setAuditRemark(auditRemark);
            result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.DISAGRESS);
        } else if ("3".equals(type)) {
            //卖家确认收货
            String paypwd = request.getParameter("paypwd");
            if (userId.equals(new Long(-1))) {//判断用户是否登录
                errorMessages.add("用户未登录！");
                result.setErrorMessages(errorMessages);
                return result;
            }
            if (StringUtils.isEmpty(paypwd)) {
                errorMessages.add("支付密码不能为空");
                result.setErrorMessages(errorMessages);
                return result;
            }
            paypwd = MD5.encipher(paypwd);
            ExecuteResult<String> payResult = userExportService.validatePayPassword(userId, paypwd);
            if ("1".equals(payResult.getResult())) {//验证支付码是否正确
                TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
                dto.setId(Long.valueOf(returnId));
                dto.setState(TradeReturnStatusEnum.REFUNDING.getCode());
                if (parentId != null && parentId.intValue() > 0) {
                    dto.setSellerId(parentId.toString());
                } else {
                    dto.setSellerId(userId.toString());
                }
                result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.REFUNDING);
            } else {
                errorMessages.add("支付密码错误，请重新输入！");
                result.setErrorMessages(errorMessages);
                return result;
            }

        } else if ("4".equals(type)) {
            //买家 确认收款
            TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
            dto.setId(Long.valueOf(returnId));
            dto.setState(TradeReturnStatusEnum.SUCCESS.getCode());
            dto.setBuyId(userId.toString());
            dto.setOrderStatus("5");//订单设为已完成状态
            result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.SUCCESS);
            //退款成功后调用
            if (result.isSuccess()) {
                complainCancle1(returnId);
            }
        }
        return result;
    }

    //取消投诉
    //tradeReturnid是退货单id
    private Map<String, String> complainCancle1(String tradeReturnid) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            ComplainDTO comp = new ComplainDTO();
            comp.setRefundId(new Long(tradeReturnid));
            comp.setStatus(0);
            //获取尚未投诉尚未处理完的投诉单
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            DataGrid<ComplainDTO> da = complainExportService.findInfoByCondition(comp, null);
            if (da != null && da.getRows() != null && da.getRows().size() > 0) {
                List<ComplainDTO> list = da.getRows();
                for (int i = 0; i < list.size(); i++) {
                    ComplainDTO complainDTO = list.get(0);
                    //取消仲裁
                    complainDTO.setStatus(new Integer(2));
                    complainDTO.setModified(new Date());
                    complainDTO.setResolutionTime(new Date());
                    ExecuteResult<String> executeResul = complainExportService.modifyComplainInfo(complainDTO);
                }
            }
            map.put("flag", "success");
            map.put("msg", "取消完成");
        } catch (Exception e) {
            map.put("flag", "error");
            map.put("msg", "取消投诉出现意外错误：" + e.getMessage());
        }
        return map;
    }

    /**
     * <p>Discription:[提交订单]</p>
     * Created on 2015年3月12日
     *
     * @param request
     * @return
     * @author:[123]
     */
    @RequestMapping("/orderSubmit")
    @ResponseBody
    public String orderSubmit(HttpServletRequest request, TradeOrdersDTO dto, Model model) {
        String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
        String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
        dto.setBuyerId(Long.valueOf(uid));
        ExecuteResult<String> result = this.shopCartService.subimtOrder(ctoken, uid, dto);
        logger.debug("===========>" + JSON.toJSONString(result));
        //将结果放入request中，仅供站内信发送拦截器使用
        request.setAttribute("result", result);
        model.addAttribute("executeResult", result);
        if (result.isSuccess() && dto.getShipmentType() == 1) {
            return "redirect:/shopCart/payView?orderNo=" + result.getResult();
        } else {
            return "/shopcart/submit_result";
        }
    }

    @RequestMapping("/payView")
    public String toPayView(HttpServletRequest request, String orderNo, Model model) {
        logger.debug("PAY VIEW ORDER NO:" + orderNo);
        model.addAttribute("orderNo", orderNo);
        String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
        ExecuteResult<UserInfoDTO> erUi = this.userExtendsService.findUserInfo(Long.valueOf(uid));
        int accountstatus = -1;
        if (erUi.isSuccess() && erUi.getResult() != null) {
            if (erUi.getResult().getUserCiticDTO() != null)
                accountstatus = erUi.getResult().getUserCiticDTO().getAccountState();
        }
        model.addAttribute("accountstatus", accountstatus);
        ExecuteResult<TradeOrdersDTO> erTo = this.tradeOrderExportService.getOrderById(orderNo);
        if (erTo.isSuccess()) {
            String orderType = "1";
            if (erTo.getResult().getParentOrderId().equals("-1"))
                orderType = "0";
            model.addAttribute("payType", erTo.getResult().getShipmentType());
            model.addAttribute("orderType", orderType);
            model.addAttribute("payTotal", erTo.getResult().getPaymentPrice());
        }
        return "/shopcart/pay_view";
    }


    /**
     * 常见问题
     */
    @RequestMapping("/questions")
    public String questions(HttpServletRequest request, Model model) {
        MallDocumentDTO document = this.mallDocumentService.getMallDocumentById(Constants.DOCUMENT_QUESTIONS);
        model.addAttribute("document", document);
        return "/person/questions";
    }

    /**
     * 意见反馈
     */
    @RequestMapping("/command")
    public String command() {
        return "/person/command";
    }

    /**
     * 进入支付页面
     */
    @RequestMapping("/pay")
    public String pay(HttpServletRequest request, HttpServletResponse response, String orderNo, Model model) {
        //用户信息
        ExecuteResult<UserInfoDTO> userInfo = userExtendsService.findUserInfo(Long.parseLong(CookieHelper.getCookieVal(request, Constants.USER_ID)));
        if (userInfo.getResult() != null) {
            UserCiticDTO userCiticDTO = userInfo.getResult().getUserCiticDTO();
            if (userCiticDTO.getAccountState() >= 2) {
                model.addAttribute("jrtFalg", true);
            } else {
                model.addAttribute("jrtFalg", false);
            }
        }
        //订单信息
        ExecuteResult<TradeOrdersDTO> executeResult = tradeOrderExportService.getOrderById(orderNo);
        if (executeResult.getResult() != null) {
            TradeOrdersDTO tradeOrdersDTO = executeResult.getResult();
            model.addAttribute("orderNo", tradeOrdersDTO.getOrderId());
            model.addAttribute("totalPrice", tradeOrdersDTO.getPaymentPrice());
            model.addAttribute("wxUrl", SysProperties.getProperty(Constants.WX_OPENID_URL));
            model.addAttribute("appid", SysProperties.getProperty(Constants.WX_APP_ID));
            model.addAttribute("redirecturi", SysProperties.getProperty(Constants.WX_REDIRECT_URI));
            if (null !=tradeOrdersDTO.getPaymentType() && 1 ==tradeOrdersDTO.getPaymentType()) {
                model.addAttribute("jrtFalg", false);
            }
        }
        return "/person/pay";
    }

    /**
     * 进入支付接口
     */
    @RequestMapping("/payOrder")
    public String payOrder(String orderNo, String paymentMethod, Double totalPrice, Model model, HttpServletRequest request) {
    	logger.info("\n 方法[{}]，入参：[{},{},{},{}]","PersonController-payOrder",orderNo,paymentMethod,totalPrice,JSON.toJSONString(model));
    	PayReqParam param = new PayReqParam();
        param.setOrderNo(orderNo);
        if ("zfb".equals(paymentMethod)) {
            param.setPayBank(PayBankEnum.AP_MOBILE);
        } else if ("wnzx".equals(paymentMethod)) {
            param.setPayBank(PayBankEnum.CB_MOBILE);
        } else if ("jrt".equals(paymentMethod)) {
            param.setPayBank(PayBankEnum.CITIC);
        } else if (null == paymentMethod || "".equals(paymentMethod)) {
        	String code = request.getParameter("code");
        	String orderId = request.getParameter("state");
        	String openid = this.getOpenId(code,SysProperties.getProperty(Constants.WX_APP_ID),SysProperties.getProperty(Constants.WX_APP_SECRET));
            param.setOpenid(openid);
            param.setOrderNo(orderId);
            param.setPayBank(PayBankEnum.WX);
            paymentMethod = "wx";
        }

        try {
            ExecuteResult<Integer> executeResult = paymentWxExportService.pay(param);
            if (executeResult.getResult() == 1) {
                //正在支付
                if ("jrt".equals(paymentMethod)) {
                    //中信支付
                    model.addAttribute("orderNo", orderNo);//订单号
                    model.addAttribute("totalPrice", totalPrice);//总金额
                    model.addAttribute("formHtml", executeResult.getResultMessage());
                    //买家支付信息
                    String token = LoginToken.getLoginToken(request);
                    RegisterDTO registerDTO = userExportService.getUserByRedis(token);
                    ExecuteResult<UserInfoDTO> userInfo = userExtendsService.findUserInfo(registerDTO.getUid());
                    if (userInfo.getResult() != null) {
                        UserCiticDTO userCiticDTO = userInfo.getResult().getUserCiticDTO();
                        //金融通用户 判断一下余额是否足够
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("system", "MALL");//-系统编号
                        map.put("subAccNo", userCiticDTO.getBuyerPaysAccount());//-附属账号
                        map.put("sign", Signature.createSign(map, "123456"));//-签名
                        ExecuteResult<AffiliatedBalanceDto> result = citicExportService.querySubBalance(map);
                        if (result.getResult() != null && result.getResult().getList().size() > 0) {
                            AffiliatedBalance affiliatedBalance = result.getResult().getList().get(0);
                            if (Double.parseDouble(affiliatedBalance.getKYAMT()) >= totalPrice) {
                                //余额足够 进入支付界面
                                return "/person/citic_pay";
                            } else {
                                //余额不足 进入充值界面
                                model.addAttribute("balance", affiliatedBalance.getKYAMT());//买家余额
                                //买家家账户信息
                                model.addAttribute("buyerPaysAccount", affiliatedBalance.getSubAccNo());//买家账号
                                model.addAttribute("bankAccountName", affiliatedBalance.getSUBACCNM());//买家账号名称
                                return "/person/pay_citic_recharge";
                            }
                        }
                    }
                } else if ("wx".equals(paymentMethod)) {
                	if(executeResult.isSuccess()){
                		model.addAttribute("success", true);
                	}
                    model.addAttribute("formHtml", executeResult.getResultMessage());
//                    if (executeResult.getResult() == 1) {
//                        executeResult.setResult(2);
//                        model.addAttribute("er", executeResult);
//                        return "redirect:" + executeResult.getResultMessage();
//                    } else {
                        model.addAttribute("er", executeResult);
                        return "/person/pay_redirect2";
//                    }
                } else {
                    model.addAttribute("success", true);
//                    String xml = executeResult.getResultMessage() + "</form>";
//                    xml = xml.replace("<auth_and_execute_req><request_token>", "").replace("</request_token></auth_and_execute_req>", "");
//                    Document document = DocumentHelper.parseText(xml);
//                    Element root = document.getRootElement();
//                    String url = "http://wappaygw.alipay.com/service/rest.htm?_input_charset=utf-8";
//                    for(Iterator it=root.elementIterator();it.hasNext();){        
//                        Element element = (Element) it.next();      
//                        Attribute nameAttribute=element.attribute("name");  
//                        String name=nameAttribute.getText();
//                        Attribute valueAttribute=element.attribute("value");  
//                        String value=valueAttribute.getText();
//                        url +="&"+name+"="+value;
//                    }  
//                    String form = "<form id='form' name='alipaysubmit' action='"+url+"' method='get'></form>";
                    model.addAttribute("formHtml", executeResult.getResultMessage());
                    model.addAttribute("er", executeResult);
                    return "/person/pay_redirect";
                }
            } else if (executeResult.getResult() == 2) {
                //支付完成
                model.addAttribute("success", true);
                model.addAttribute("er", executeResult);
                return "/person/pay_redirect";
            } else {
                //支付失败
                model.addAttribute("success", false);
                model.addAttribute("er", executeResult);
                return "/person/pay_redirect";
            }
        } catch (Exception e) {
            ExecuteResult<Integer> executeResult = new ExecuteResult<Integer>();
            executeResult.setResultMessage("支付失败！");
            //支付失败
            model.addAttribute("success", false);
            model.addAttribute("er", executeResult);
            return "/person/pay_redirect";
        }
        return "/person/citic_pay";
    }

    /**
     * 进入中信金融通支付接口
     */
    @RequestMapping("/payCitic")
    public String payCitic(String outOrderNo, Model model, HttpServletRequest request,HttpServletResponse response) {
        Map<String, String> payParams = new HashMap<String, String>();
        payParams.put("system", "MALL");//系统编码
        payParams.put("outTradeNo", outOrderNo);//对外订单号
        payParams.put("uid", CookieHelper.getCookieVal(request, Constants.USER_ID));//买家用户ID
        payParams.put("accType", AccountType.AccBuyPay.getCode() + "");//买家用户类型
        payParams.put("sign", Signature.createSign(payParams, "123456"));
        try {
            Map<String, String> payCiticMap = citicExportService.payCitic(payParams);
            ExecuteResult<OrderInfoPay> payResult = paymentExportService.payResult(payCiticMap, PayBankEnum.CITIC.name());
            if ("0".equals(payResult.getResultMessage())) {
                //支付失败
                model.addAttribute("success", false);
                //model.addAttribute("er", payResult);
                request.setAttribute("er", payResult);
            } else {
                //支付成功
                model.addAttribute("success", true);
                //model.addAttribute("er", payResult);
                request.setAttribute("er", payResult);
                
                //发送信息
//                String token = LoginToken.getLoginToken(request);
//                RegisterDTO registerDTO = userExportService.getUserByRedis(token);
//                UserWxDTO userWxDTO = new UserWxDTO();
//                userWxDTO.setUname(registerDTO.getLoginname());
//                ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.getUserInfoByOpenId(userWxDTO);
//                logger.info("executeResult============="+JSON.toJSONString(executeResult));
//                if (executeResult.isSuccess() && executeResult.getResult() != null) {
//                    UserWxDTO userWx = executeResult.getResult();
//                    if (userWx.getWxopenid() != null) {
//                    	logger.info("-------------------------买家异步已成功完成付款----------------------");
//                    	//呵呵这是我改的发消息的
//                        WeChatProcess weChatProcess = new WeChatMsgProcess();
//                        Map simap = new HashMap();
//                        simap.put("openId", userWx.getWxopenid());
//                        simap.put("modeId", WeiXinMessageModeId.PAYMENT_SUCCESS);
//                        simap.put("first", "支付成功！");
//                        simap.put("orderMoneySum", payResult.getResult().getOrderAmount().toString());
//                        ExecuteResult<TradeOrdersDTO> tradeOrdersDTO = tradeOrderExportService.getOrderById(Long.valueOf(payResult.getResult().getOrderNo()));;
//                        List<TradeOrderItemsDTO> items = tradeOrdersDTO.getResult().getItems();
//                        StringBuffer sb = new StringBuffer();
//                        for(TradeOrderItemsDTO i : items){
//                        	sb.append(i.getSkuName()).append(";");
//                        }
//                        simap.put("orderProductName", sb.toString());
//                        simap.put("remark", "支付成功！");
//                        weChatProcess.SendInformation(simap, request, response);
//                    }
//                }
                
                
                SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.PAYMENT_SUCCESS);
				message.setFirst("【印刷家】尊敬的用户，买家已成功完成付款，订单号（"+ payResult.getResult().getOrderNo() +"），印刷家提醒您及时查看并尽快完成发货。");
				message.setOrderMoneySum(payResult.getResult().getOrderAmount().toString());
				ExecuteResult<TradeOrdersDTO> tradeOrdersDTO = tradeOrderExportService.getOrderById(payResult.getResult().getOrderNo());;
                List<TradeOrderItemsDTO> items = tradeOrdersDTO.getResult().getItems();
                StringBuffer sb = new StringBuffer();
                for(TradeOrderItemsDTO i : items){
                	sb.append(i.getSkuName()).append(";");
                }
				message.setOrderProductName(sb.toString());
				message.setRemark("支付成功!");
				
				
				//========================李伟龙修改，2015年10月26日 18:20:18
				String sellerid = payResult.getResult().getSeller();
				Set<String> sellerSet=new HashSet<String>();
		        sellerSet.add(null==sellerid?null:sellerid.toString());
		        //根据订单查询子订单
		        TradeOrdersQueryInDTO toqid=new TradeOrdersQueryInDTO();
		        toqid.setParentOrderId(payResult.getResult().getOrderNo());
		        Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
		        pager.setPage(1);
		        pager.setRows(Integer.MAX_VALUE);
		        ExecuteResult<DataGrid<TradeOrdersDTO>> childrenOrdersRes= tradeOrderExportService.queryOrders(toqid, pager);
		        DataGrid dg01=childrenOrdersRes.getResult();
		        //把子订单的店铺id保存到set中
		        if(null!=dg01){
		            List<TradeOrdersDTO> childrenOrderRows=dg01.getRows();
		            if(null!=childrenOrderRows && childrenOrderRows.size()>0){
		                for(TradeOrdersDTO todto : childrenOrderRows){
		                    sellerSet.add(null==todto.getSellerId()?null:todto.getSellerId().toString());
		                }
		            }
		        }
		        //全部发送微信消息
		        for(String _sellid : sellerSet) {
		            if(StringUtils.isNotEmpty(_sellid)){
		                SendWeiXinMessageUtil.sendWeiXinMessage(_sellid, message, request, response);
		            }
		        }
		        //=====================李伟龙修改
//				SendWeiXinMessageUtil.sendWeiXinMessage(payResult.getResult().getSeller(), message, request, response);
                
            }
            return "/person/pay_citic_redirect";
        } catch (Exception e) {
            ExecuteResult<Integer> executeResult = new ExecuteResult<Integer>();
            executeResult.setResultMessage("支付失败！");
            //支付失败
            model.addAttribute("success", false);
            //model.addAttribute("er", executeResult);
            request.setAttribute("er", executeResult);
            return "/person/pay_citic_redirect";
        }
    }

    /**
     * 验证支付密码是否正确
     */
    @RequestMapping("/validatePayPassword")
    @ResponseBody
    public String validatePayPassword(Long uid, String paypassword, HttpServletRequest request, HttpServletResponse response) {
        uid = Long.parseLong(CookieHelper.getCookieVal(request, Constants.USER_ID));
        ExecuteResult<String> executeResult = userExportService.validatePayPassword(uid, MD5.encipher(paypassword));
        JSONObject jores = new JSONObject();
        //1成功 2失败
        if ("1".equals(executeResult.getResult())) {
            jores.put("success", true);
            jores.put("resultMessage", executeResult.getResultMessage());
        } else {
            jores.put("success", false);
            jores.put("resultMessage", executeResult.getResultMessage());
        }
        return jores.toString();
    }

    /*
     * 注册完成后初始化登陆
     */
    private void initLogin(String loginname, String loginpwd, HttpServletRequest request, HttpServletResponse response) {
        //生成用户登录token
        StringBuffer buffer = new StringBuffer();
        buffer.append(MD5.encipher(loginname));
        buffer.append("|");
        //buffer.append(request.getRemoteAddr());
        //buffer.append("|");
        buffer.append(SysProperties.getProperty("token.suffix"));
        String key = buffer.toString();
        //用户登陆
        ExecuteResult<LoginResDTO> er = userExportService.login(loginname, loginpwd, key);
        if (er.isSuccess()) {
            LoginResDTO loginResDTO = er.getResult();
            CookieHelper.setCookie(response, Constants.AUTO_LOGON, "0");
            CookieHelper.setCookie(response, "logging_status", MD5.encipher(loginname));
            CookieHelper.setCookie(response, Constants.USER_NAME, loginResDTO.getNickname());
            CookieHelper.setCookie(response, Constants.LOG_NAME, loginResDTO.getNickname());
            CookieHelper.setCookie(response, Constants.USER_ID, loginResDTO.getUid().toString());
        }
    }

    /**
     * 微信绑定登陆
     *
     * @param model
     * @param request
     * @param response
     * @param url
     * @param userToken
     * @param remember
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping("/bingWxLogin")
    @ResponseBody
    public String bingWxLogin(Model model,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              String url,
                              @CookieValue(value = Constants.USER_TOKEN, required = false, defaultValue = "") String userToken,
                              @RequestParam(value = "remember", required = false, defaultValue = "") String remember,
                              @RequestParam("userName") String userName,
                              @RequestParam("password") String password) {
        password = MD5.encipher(password);
        String key = getToken(userName, request);
        ExecuteResult<LoginResDTO> er = userExportService.login(userName, password, key);
        if (er.isSuccess()) {
            LoginResDTO loginResDTO = er.getResult();
            if (loginResDTO.getUstatus() > 1) {
                //登陆成功了
                CookieHelper.setCookie(response, "logging_status", MD5.encipher(userName));
                CookieHelper.setCookie(response, Constants.USER_NAME, loginResDTO.getNickname());
                CookieHelper.setCookie(response, Constants.LOG_NAME, loginResDTO.getNickname());
                CookieHelper.setCookie(response, Constants.USER_ID, loginResDTO.getUid().toString());
                JSONObject jores = new JSONObject();
                jores.put("success", true);
                return jores.toJSONString();
            } else {
                JSONObject jores = new JSONObject();
                jores.put("success", false);
                jores.put("errmsg", "登陆失败！");
                return jores.toJSONString();
            }
        } else {
            String message = "";
            if (er.getErrorMessages() != null && er.getErrorMessages().size() > 0)
                message = er.getErrorMessages().get(0);
            logger.info(message);
            JSONObject jores = new JSONObject();
            jores.put("success", false);
            jores.put("errmsg", message);
            jores.put("logname", userName);
            jores.put("url", url);
            return jores.toJSONString();
        }
    }

    /**
     * <p>Discription:[拷贝生成用户登录token]</p>
     * Created on 2015年4月1日
     *
     * @param logname
     * @param request
     * @return
     * @author:[胡恒心]
     */
    public static String getToken(String logname, HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(MD5.encipher(logname));
        buffer.append("|");
        //buffer.append(request.getRemoteAddr());
        //buffer.append("|");
        buffer.append(SysProperties.getProperty("token.suffix"));
        return buffer.toString();
    }

    /**
     * 微信绑定
     */
    @RequestMapping("/bindingAccount")
    public String bindingAccount() {
        //验证该账号是否已经绑定
        String url = WxSnsAPI.genOauth2Url(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.Url + "/person/checkOpenId");
        return "redirect:" + url;

    }

    //微信验证该账号是否绑定回调函数
    @RequestMapping("/checkOpenId")
    public String checkOpenId(HttpServletRequest request, HttpServletResponse response, String code, Model model) {
        WxSnsAccessToken wxSnsAccessToken = WxSnsAPI.getOauth2AccessToken(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.WXAPPSECRET, code);
        WxUserInfo wxUserInfo = WxSnsAPI.getSnsUserInfo(wxSnsAccessToken.getAccess_token(), com.camelot.util.Constants.WXAPPID);
        logger.debug(wxUserInfo.getOpenid());
        //查询该微信是否已经绑定账号
        UserWxDTO userWxDTO = new UserWxDTO();
        userWxDTO.setWxopenid(wxUserInfo.getOpenid());
        ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.getUserInfoByOpenId(userWxDTO);
        //如果已经绑定 跳转到解绑页面
        if (executeResult.getResult() != null && executeResult.getResult().getUname() != null) {
            UserWxDTO userWx = executeResult.getResult();
            this.initLogin(userWx.getUname(), userWx.getPassword(), request, response);
            model.addAttribute("uname", userWx.getUname());
            return "/person/cancelBinding";
        } else {
            return "/person/bindingAccount";
        }
    }

    //微信验证该账户是否绑定
    @RequestMapping("/checkUname")
    @ResponseBody
    public String checkUname(Model model,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             String url,
                             @CookieValue(value = Constants.USER_TOKEN, required = false, defaultValue = "") String userToken,
                             @RequestParam(value = "remember", required = false, defaultValue = "") String remember,
                             @RequestParam("userName") String userName) {
        UserWxDTO userWxDTO = new UserWxDTO();
        userWxDTO.setUname(userName);
        ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.getUserInfoByOpenId(userWxDTO);
        JSONObject jores = new JSONObject();
        if (executeResult.getResult() != null && executeResult.getResult().getWxopenid() != null) {
            jores.put("success", true);
            jores.put("errmsg", "该账户已经绑定微信账号！");
            //------------------add by xj  绑定微信消息发送 ----------------------------//
//            SendWeiXinMessage message = new SendWeiXinMessage();
//			message.setModeId(WeiXinMessageModeId.BINDING_WECHAT);
//			message.setFirst("【印刷家】尊敬的用户，您的微信账号已与小管家绑定成功。");
//			message.setKeyword1(userName);
//			message.setKeyword2("绑定成功");
//			SendWeiXinMessageUtil.sendWeiXinMessage(String.valueOf(executeResult.getResult().getUid()), message, request, response);
            //------------------add by xj  绑定微信消息发送 ----------------------------//
        } else {
            jores.put("success", false);
            jores.put("errmsg", "");
        }
        return jores.toJSONString();
    }

    /**
     * 微信解绑回掉函数
     *
     * @return
     */
    @RequestMapping("/cancelBinding")
    public String cancelBinding() {
        String url = WxSnsAPI.genOauth2Url(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.Url + "/person/cancelBindingSec");
        return "redirect:" + url;
    }

    /**
     * 微信回掉函数获取用户信息 解绑账号
     *
     * @param request
     * @param response
     * @param code
     * @return
     */
    @RequestMapping("/cancelBindingSec")
    public String cancelBindingSec(HttpServletRequest request, HttpServletResponse response, String code) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        //微信账号解绑
        UserWxDTO userWxDTO = new UserWxDTO();
        userWxDTO.setUid(registerDTO.getUid());
        userWxExtendsService.cancelBinding(userWxDTO);
        //发送信息
        WxSnsAccessToken wxSnsAccessToken = WxSnsAPI.getOauth2AccessToken(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.WXAPPSECRET, code);
        WxUserInfo wxUserInfo = WxSnsAPI.getSnsUserInfo(wxSnsAccessToken.getAccess_token(), com.camelot.util.Constants.WXAPPID);
        logger.debug(wxUserInfo.getOpenid());
        WeChatProcess weChatProcess = new WeChatMsgProcess();
        Map map = new HashMap();
        map.put("openId", wxUserInfo.getOpenid());
        map.put("modeId", WeiXinMessageModeId.UNBUNDLING_WECHAT);
        map.put("first", "解绑成功！");
        map.put("keyword1", registerDTO.getLoginname());
        map.put("keyword2", "解绑成功！");
        map.put("remark", "【印刷家】尊敬的用户，您的微信账号已与小管家解绑成功。");
        weChatProcess.SendInformation(map, request, response);
        return "/person/cancelBindingSec";
    }

    /**
     * 微信绑定回掉函数
     *
     * @return
     */
    @RequestMapping("/genOauth2Url")
    public String genOauth2Url() {
        String url = WxSnsAPI.genOauth2Url(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.Url + "/person/oauth2UserInfo");
        return "redirect:" + url;
    }

    /**
     * 微信回掉函数获取用户信息 绑定账号
     *
     * @param request
     * @param response
     * @param code
     * @return
     */
    @RequestMapping("/oauth2UserInfo")
    public String oauth2UserInfo(HttpServletRequest request, HttpServletResponse response, String code) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WxSnsAccessToken wxSnsAccessToken = WxSnsAPI.getOauth2AccessToken(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.WXAPPSECRET, code);
        WxUserInfo wxUserInfo = WxSnsAPI.getSnsUserInfo(wxSnsAccessToken.getAccess_token(), com.camelot.util.Constants.WXAPPID);
        logger.debug(wxUserInfo.getOpenid());
        //微信账号绑定
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);


        UserWxDTO userWxDTO = new UserWxDTO();
        userWxDTO.setWxopenid(wxUserInfo.getOpenid());
        userWxDTO.setUid(registerDTO.getUid());
        ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.bindingWX(userWxDTO);
        if (null != executeResult && executeResult.isSuccess() && StringUtils.isNotEmpty(code)) {
            WeChatProcess weChatProcess = new WeChatMsgProcess();
            Map map = new HashMap();
            map.put("openId", wxUserInfo.getOpenid());
            map.put("modeId", WeiXinMessageModeId.BINDING_WECHAT);
            map.put("first", "绑定成功！");
            map.put("keyword1", registerDTO.getLoginname());
            map.put("keyword2", "绑定成功");
            map.put("remark", "【印刷家】尊敬的用户，您的微信账号已与小管家绑定成功。");
            weChatProcess.SendInformation(map, request, response);
        }
        //发送信息
        return "/person/microBinding";
    }

    /**
     * 微信绑定的账号获取用户信息
     *
     * @return
     */
    @RequestMapping("/automaticGetOpenId")
    public String automaticGetOpenId() {
        String url = WxSnsAPI.genOauth2Url(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.Url + "/person/automaticLogin");
        return "redirect:" + url;
    }

    /**
     * 微信绑定的账号获取用户信息自动登录
     *
     * @return
     */
    @RequestMapping("/automaticLogin")
    public String automaticLogin(HttpServletRequest request, HttpServletResponse response, String code) {
        //获取token
        WxSnsAccessToken wxSnsAccessToken = WxSnsAPI.getOauth2AccessToken(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.WXAPPSECRET, code);
        //获取用户信息
        WxUserInfo wxUserInfo = WxSnsAPI.getSnsUserInfo(wxSnsAccessToken.getAccess_token(), com.camelot.util.Constants.WXAPPID);
        logger.debug(wxUserInfo.getOpenid());
        UserWxDTO userWxDTO = new UserWxDTO();
        userWxDTO.setWxopenid(wxUserInfo.getOpenid());
        ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.getUserInfoByOpenId(userWxDTO);
        if (executeResult.getResult() != null) {
            UserWxDTO userWx = executeResult.getResult();
            this.initLogin(userWx.getUname(), userWx.getPassword(), request, response);
        }
        return "redirect:../";
    }

    /**
     * 微信绑定修改密码回调函数
     */
    @RequestMapping("/bindingChagePwdGetOpenId")
    public String bindingChagePwdGetOpenId() {
        String url = WxSnsAPI.genOauth2Url(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.Url + "/person/bindingChagePwd");
        return "redirect:" + url;
    }

    /**
     * 微信绑定修改密码
     *
     * @return
     */
    @RequestMapping("/bindingChagePwd")
    public String bindingChagePwd(HttpServletRequest request, HttpServletResponse response, String code, Model model) {
        WxSnsAccessToken wxSnsAccessToken = WxSnsAPI.getOauth2AccessToken(com.camelot.util.Constants.WXAPPID, com.camelot.util.Constants.WXAPPSECRET, code);
        WxUserInfo wxUserInfo = WxSnsAPI.getSnsUserInfo(wxSnsAccessToken.getAccess_token(), com.camelot.util.Constants.WXAPPID);
        UserWxDTO userWxDTO = new UserWxDTO();
        userWxDTO.setWxopenid(wxUserInfo.getOpenid());
        ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.getUserInfoByOpenId(userWxDTO);

        if (executeResult.getResult() != null) {
            UserWxDTO userWx = executeResult.getResult();
            model.addAttribute("success", true);
            model.addAttribute("uName", userWx.getUname());
            model.addAttribute("uMobile", userWx.getUmobile());
            if (userWx.getUmobile() != null && userWx.getUmobile().length() == 11) {
                String mobile = userWx.getUmobile().substring(0, 3) + "*****" + userWx.getUmobile().substring(7, 11);
                model.addAttribute("mobile", mobile);
                this.initLogin(userWx.getUname(), userWx.getPassword(), request, response);
            }

        } else {
            model.addAttribute("success", false);
        }
        return "/person/bingdingModifyPwd";
    }

    /**
     * 前台验证后根据用户名修改密码
     */
    @RequestMapping("/modifyPwdByname")
    @ResponseBody
    public String modifyPwdByname(HttpServletRequest request, HttpServletResponse response, String loginname, String cpwd) {
        Map map = new HashMap();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            UserWxDTO userWxDTO = new UserWxDTO();
            userWxDTO.setUname(loginname);
            ExecuteResult<UserWxDTO> executeResult = userWxExtendsService.getUserInfoByOpenId(userWxDTO);
            if (executeResult.getResult() != null) {
                UserWxDTO userWx = executeResult.getResult();
                Long uid = userWx.getUid();
                userExportService.resetUserPassword(uid, MD5.encipher(cpwd));
                //注册完成后初始化登陆
                this.initLogin(loginname, cpwd, request, response);
                map.put("result", true);
                map.put("message", "密码修改成功!");
                //发送信息
                if (userWx.getWxopenid() != null) {
                	logger.info("账号密码修改成功===========================");
                    WeChatProcess weChatProcess = new WeChatMsgProcess();
                    Map simap = new HashMap();
                    simap.put("openId", userWx.getWxopenid());
                    simap.put("modeId", WeiXinMessageModeId.CHANGE_PASSWORD);
                    simap.put("first", "修改成功！");
                    simap.put("productName", loginname);
                    simap.put("time", df.format(new Date()));
                    simap.put("remark", "账号密码修改成功！");
                    weChatProcess.SendInformation(map, request, response);
                }
            } else {
                map.put("result", false);
                map.put("message", "密码修改失败!");
            }
        } catch (Exception e) {
            map.put("result", false);
            map.put("message", "密码修改失败!");
            logger.error(e.getMessage());
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * 前台验证手机号码是否唯一
     */
    @RequestMapping("/checkMobileUnique")
    @ResponseBody
    public String checkMobileUnique(String mobile) {
        String mag = "false";
        if (userExportService.verifyMobile(mobile))
            mag = "true";
        return mag;

    }
    
    /**
	 * 依据获得的code获取openId
	 * @param code
	 * @param appid
	 * @param secret
	 * @return
	 */
	private String getOpenId(String code,String appid,String secret){
		logger.info("\n 方法[{}]，入参：[{},{},{}]","PersonController-getOpenId",code,appid,secret);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url); 
        String result = null;
        try {
            client.executeMethod(post);
            result = post.getResponseBodyAsString();
            post.releaseConnection();
        }catch (Exception e) {
			
		}
        JSONObject o = JSONObject.parseObject(result);
        logger.info("getOpenid result:"+result);
		return o.getString("openid");
	}
}
