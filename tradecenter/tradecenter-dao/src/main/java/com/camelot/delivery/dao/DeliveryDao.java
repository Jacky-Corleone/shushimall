package com.camelot.delivery.dao;

import java.util.List;

import com.camelot.delivery.dto.DeliveryDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface DeliveryDao extends BaseDAO<DeliveryDao> {

	void saveDeliverStatus(DeliveryDTO delibery);

	void updateDeliverMessage(DeliveryDTO delibery);

	DeliveryDTO selectDeliveryInfoByItemId(String orderItemId);
	
	void updateDeliverStatus(DeliveryDTO delibery);

	List<DeliveryDTO> selectDliverUnusualStatus();

	void updateDeliverInfo(DeliveryDTO delibery);
    
}
