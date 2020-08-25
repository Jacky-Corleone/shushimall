package com.camelot.delivery.service;

import java.util.List;

import com.camelot.delivery.dto.DeliveryDTO;
import com.camelot.delivery.dto.SendBodyDTO;
import com.camelot.openplatform.common.ExecuteResult;
/** 
 * <p>Description: [快递100service]</p>
 * Created on 2015年8月10日
 * @author  <a href="mailto: liufangyi@camelotchina.com">刘芳义</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface DeliveryService {
   /**
    * 保存快递100订阅是否成功
    */
   public String  saveDeliverStatus(SendBodyDTO sendBodyDTO,DeliveryDTO delivery);
   
   /**
    * 查询订单物流信息
    */
   public DeliveryDTO getDeliveryInfoByItemId(String orderItemId);
   
   /**
    * 查询异常订单信息
    */
   public List<DeliveryDTO> selectDliverUnusualStatus();
  
   /**
    * 
    *<p>Discription:[新增订阅信息]</p>
    * @return
    */
   public String addDeliverInfo(SendBodyDTO sendBodyDTO,DeliveryDTO delivery);
   
   /**
    * 
    * <p>Discription:[新增订阅信息]</p>
    * Created on 2016年1月14日
    * @param sendBodyDTO
    * @param delibery
    * @return
    * @author:[宋文斌]
    */
   public ExecuteResult<String> batchAddDeliverInfo(SendBodyDTO sendBodyDTO, DeliveryDTO delibery);
   
   /**
    * 
    *<p>Discription:[修改订阅信息]</p>
    * @return
    */
   public String updateDeliverInfo(SendBodyDTO sendBodyDTO,DeliveryDTO delivery);
   /**
    * 更新物流信息
    */
   public void updateDeliverMessage(String Message);
   /**
    * 更新快递100订阅状态
    */
   public String updateDeliverStatus(SendBodyDTO sendBodyDTO,DeliveryDTO delivery);
 
}
