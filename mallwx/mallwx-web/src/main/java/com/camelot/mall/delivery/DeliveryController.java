package com.camelot.mall.delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.dto.DictionaryDTO;
import com.camelot.basecenter.service.DictionaryService;
import com.camelot.delivery.dto.DeliveryDTO;
import com.camelot.delivery.dto.PushDelibery;
import com.camelot.delivery.dto.SendBodyDTO;
import com.camelot.delivery.service.DeliveryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.service.TradeOrderExportService;

/**
 * <p>
 * Description: [快递100Controller]
 * </p>
 * Created on 2015年11月10日
 * 
 * @author <a href="mailto: liufangyi@camelotchina.com">咸玛瑞</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Controller
@RequestMapping("/delivery")
public class DeliveryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryController.class);
	
	//物流（快递100）接口
	@Resource
	private DeliveryService deliveryService;
	//字典服务接口
	@Resource
	private DictionaryService dictionaryService;
	//订单服务接口
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	//商品服务接口
	@Resource
	private ItemExportService itemExportService;

	/**
	 * 查看物流信息
	 * @param orderItemId：商品ID
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getDeliveryInfo")
	public String getDeliveryInfo(String orderItemId, Model model) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		//是否查询成功的标示
		boolean success = true;
		
		DeliveryDTO deliveryDTO = deliveryService.getDeliveryInfoByItemId(orderItemId);
		PushDelibery pushDelibery = new PushDelibery();
		
		if(deliveryDTO!=null){
			//根据物流公司编号获取名称
			ExecuteResult<List<DictionaryDTO>> er = dictionaryService.queryDictionaryByCode(deliveryDTO.getDeliveryCompanyCode(),DeliveryConstant.TYPE);
			if(null!=er && er.getResult().size()>0){
				deliveryDTO.setDeliveryCompanyName(er.getResult().get(0).getName());
			}
			
			//如果运单信息为空，则提示暂无数据
			if(null == deliveryDTO.getMessage() || ""==deliveryDTO.getMessage().toString()){
				deliveryDTO.setMessage("由于快递100接口暂未开放，因此无法获取运单详细信息。");
			}else{
				//运单状态详情
				pushDelibery = JSON.parseObject(deliveryDTO.getMessage(), PushDelibery.class);
			}
			
		}else{
			deliveryDTO = new DeliveryDTO();
			//避免页面显示null
			deliveryDTO.setMessage("");
			deliveryDTO.setDeliveryRemark("");
			success = true;
		}
			
		returnMap.put("success", success);
		returnMap.put("deliveryDTO", deliveryDTO);
		returnMap.put("pushDelibery", pushDelibery);
		
		return JSON.toJSONString(returnMap);
	}
	
	/**
	 * 查询订单物流信息
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("toAddDelivery")
	public String toAddDelivery(String orderItemId) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		//是否查询成功的标示
		boolean success = true;
		
		//根据订单详情查询物流表中是否有记录
		DeliveryDTO deliveryDTO = deliveryService.getDeliveryInfoByItemId(orderItemId);
		if(deliveryDTO!=null){
			//根据编号获取名称
			ExecuteResult<List<DictionaryDTO>> er = dictionaryService.queryDictionaryByCode(deliveryDTO.getDeliveryCompanyCode(),DeliveryConstant.TYPE);
			if(null!=er && er.getResult().size()>0){
				deliveryDTO.setDeliveryCompanyName(er.getResult().get(0).getName());
			}
		}else{
			deliveryDTO = new DeliveryDTO();
			success = false;
		}
		map.put("success", success);
		map.put("deliveryDTO", deliveryDTO);
		return JSON.toJSONString(map);
	}
	
	/**
	 * 订阅快递100物流信息
	 * 
	 * @param sendBodyDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("sendDeliberyMessage")
	public String sendDeliberyMessage(SendBodyDTO sendBodyDTO, DeliveryDTO deliberyDto, Model model){
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String code = DeliveryEnum.CODE_500.getReturnCode();//默认500
		
		ExecuteResult<String> codeResult = deliveryService.batchAddDeliverInfo(sendBodyDTO, deliberyDto);
		code = codeResult.getResult();
		
		//返回页面的【快递100订阅状态】提示信息，在枚举常量中定义具体状态及提示信息。
		String message = DeliveryEnum.getMessage(code);
		boolean success = false;
		//如果状态为200,表示订阅成功，返回true,除此之外其他新状态均视为异常（即订阅不成功！）
		if (DeliveryEnum.CODE_200.getReturnCode().equals(code)) {
			success = true;
		}
	
		returnMap.put("success", success);
		returnMap.put("message", message);
		
		return JSON.toJSONString(returnMap);
		// 调用原来物流service修改状态
	}

	/**
	 * Discription:[修改订阅信息，重新订阅update表信息，接口不支持直接修改订阅信息]
	 * 
	 * @param sendBodyDTO
	 * @param delibery
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("updateDeliberyMessage")
	public String updateDeliberyMessage(SendBodyDTO sendBodyDTO, DeliveryDTO delibery, Model model){
		String message = "";
		
		// 保存之前判断订单是否在发送表中存在 将读取的属性文件传送到后台
		DeliveryDTO dd = deliveryService.getDeliveryInfoByItemId(delibery.getOrderItemId()+"");
		if (dd == null) {
			message = "该订单还没有提交物流信息";
		}
		String code = deliveryService.updateDeliverInfo(sendBodyDTO, delibery);
		
		if ("200".equals(code)) {
			message = "成功";
		} else if ("501".equals(code)) {
			message = "重复订阅！！！";
		} else if ("700".equals(code)) {
			message = "快递号格式错误！！";
		} else {
			message = "系统超时，请重新订阅！！";
		}
		// 调用原来物流service修改状态
		return message;
		// 调用原来物流service修改状态
	}

	/**
	 * 修改物流信息回显
	 */

	/**
	 * 第一次订阅失败定时任务，定时提交
	 * 
	 * @param sendBodyDTO
	 * @return
	 */
	@RequestMapping("sendDeliberyMessageAgain")
	public void sendDeliberyMessageAgain(SendBodyDTO sendBodyDTO, DeliveryDTO delibery) {
		
		List<DeliveryDTO> sdus = deliveryService.selectDliverUnusualStatus();
		for (DeliveryDTO deliberyDTO : sdus) {
			sendBodyDTO.setCompany(deliberyDTO.getDeliveryCompanyCode());
			sendBodyDTO.setNumber(deliberyDTO.getDeliveryNumber());
			deliveryService.updateDeliverStatus(sendBodyDTO, delibery);
		}
	}

	/**
	 * 接收快递100回推信息
	 * 
	 * @param message
	 * @return 有返回json确认接收成功
	 */
	@ResponseBody
	@RequestMapping("updateDeliverMessage")
	public String updateDeliverMessage(String param) {
		
		DeliveryDTO delibery = new DeliveryDTO();
		delibery.setMessage(param);
		deliveryService.updateDeliverMessage(param);
		
		String returnMessage = "{'result':'true','returnCode':'200','message':'成功'}";
		String returnErrorMessge = "{'result':'false','returnCode':'500','message':'推送信息为空失败'}";
		if (param != null) {
			return returnMessage;
		} else {
			return returnErrorMessge;
		}
		
	}

	/**
	 * 查询所有物流公司名称和编码
	 * @param curPage:单独分页参数，表示当前页。
	 * @param model
	 * @param uType
	 * @return
	 */
	@RequestMapping("/getDeliveryCompany")
	public String getDeliveryCompany(Pager<DictionaryDTO> page, Model model, String companyName, int curPage) {
		//设置当前页数
		page.setPage(curPage);
		//设置显示3个页码数量：翻页条工显示多少页的索引
		page.setPageCode(3);
		
		DictionaryDTO dictionaryDTO = new DictionaryDTO();
		DataGrid<DictionaryDTO> dictionaryDTOs = new DataGrid<DictionaryDTO>();
		if(!"".equals(companyName)){
			dictionaryDTO.setName(companyName);
		}
		dictionaryDTO.setType(DeliveryConstant.TYPE);
		dictionaryDTOs = dictionaryService.queryDictionaryByType(dictionaryDTO, page);
		
		page.setTotalCount(dictionaryDTOs.getTotal().intValue());
		page.setRecords(dictionaryDTOs.getRows());
		model.addAttribute("deliveryCompanyList", dictionaryDTOs.getRows());
		model.addAttribute("pager", page);
		
		return "/order/order_seller_deliveryCompany";

	}
	
	
}


