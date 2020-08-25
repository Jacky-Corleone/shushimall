package com.camelot.storecenter.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dao.ShopDeliveryTypeDAO;
import com.camelot.storecenter.dto.ShopDeliveryTypeDTO;
import com.camelot.storecenter.service.ShopDeliveryTypeService;


/**
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月18日
 *
 * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopDeliveryTypeService")
public class ShopDeliveryTypeServiceImpl implements ShopDeliveryTypeService {
    private final static Logger logger= LoggerFactory.getLogger(ShopDeliveryTypeDTO.class);
    @Resource
    private ShopDeliveryTypeDAO shopDeliveryTypeDAO;
    @Override
    public ExecuteResult<String> addShopDeliveryType(ShopDeliveryTypeDTO dto) {
        ExecuteResult<String> ex=new ExecuteResult<String>();
        try {
        	shopDeliveryTypeDAO.insert(dto);
            ex.setResultMessage("添加成功！");
        }catch(Exception e){
            logger.error(e.getMessage());
            ex.setResultMessage("添加失败！");
            throw new RuntimeException();
        }
        return ex;
    }

    @Override
    public ExecuteResult<String> deleteShopDeliveryType(ShopDeliveryTypeDTO dto) {
        ExecuteResult<String> ex=new ExecuteResult<String>();
        try {
            Long id=dto.getId();
            if(shopDeliveryTypeDAO.delete(id)>0){
                ex.setResultMessage("删除成功！");
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            ex.setResultMessage("删除失败！");
            throw new RuntimeException();
        }
        return ex;
    }

    @Override
    public ExecuteResult<String> updateShopDeliveryType(ShopDeliveryTypeDTO dto) {
        ExecuteResult<String> ex=new ExecuteResult<String>();
        try {
            if(shopDeliveryTypeDAO.update(dto)>0){
                ex.setResultMessage("修改成功！");
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            ex.setResultMessage("修改失败！");
            throw new RuntimeException();
        }
        return ex;
    }

    @Override
    public ExecuteResult<List<ShopDeliveryTypeDTO>> queryShopDeliveryType(ShopDeliveryTypeDTO dto) {
        ExecuteResult<List<ShopDeliveryTypeDTO>> ex=new ExecuteResult<List<ShopDeliveryTypeDTO>>();
        try {
            List<ShopDeliveryTypeDTO> list=shopDeliveryTypeDAO.selectListByCondition(dto);
            ex.setResult(list);
            ex.setResultMessage("success");
        }catch (Exception e){
            ex.setResultMessage("error");
            logger.error(e.getMessage());
            throw new RuntimeException();
        }
        return ex;
    }
    
    @Override
	public ExecuteResult<List<ShopDeliveryTypeDTO>> queryByRegionIdAndTemplateId(Long regionId, Long shopFreightTemplateId) {
		logger.info("\n 方法[{}]，入参：[{},{}]", "ShopDeliveryTypeServiceImpl-queryByRegionIdAndTemplateId", regionId,
				shopFreightTemplateId);
		ExecuteResult<List<ShopDeliveryTypeDTO>> executeResult = new ExecuteResult<List<ShopDeliveryTypeDTO>>();
		try {
			// 根据运费模版和运送方式查询出运费策略
			ShopDeliveryTypeDTO inDTO = new ShopDeliveryTypeDTO();
			inDTO.setTemplateId(shopFreightTemplateId);
			inDTO.setDelState("1");
			List<ShopDeliveryTypeDTO> deliveryTypeDTOs = shopDeliveryTypeDAO.selectListByCondition(inDTO);
			// 存放结果
			List<ShopDeliveryTypeDTO> resultDTOs = new ArrayList<ShopDeliveryTypeDTO>();
			// 默认的运送方式
			List<ShopDeliveryTypeDTO> defaultDTOs = new ArrayList<ShopDeliveryTypeDTO>();
			if (deliveryTypeDTOs != null && deliveryTypeDTOs.size() > 0) {
				// 根据regionId过滤
				if (regionId != null) {
					for (int i = 0; i < deliveryTypeDTOs.size(); i++) {
						String deliveryTo = deliveryTypeDTOs.get(i).getDeliveryTo();
						if (!StringUtils.isBlank(deliveryTo) && !"0".equals(deliveryTo)) {
							String[] deliveryToStrs = deliveryTo.split("、");
							for (String regionStrId : deliveryToStrs) {
								if (regionId.longValue() == Long.parseLong(regionStrId)) {
									resultDTOs.add(deliveryTypeDTOs.get(i));
									break;
								}
							}
						}else{
							defaultDTOs.add(deliveryTypeDTOs.get(i));
						}
					}
				}
				// 采用默认的运费策略
				if (resultDTOs.size() == 0) {
					resultDTOs = defaultDTOs;
				} else {
					if (defaultDTOs.size() != 0) {
						// 合并运送方式
						for (ShopDeliveryTypeDTO defaultDTO : defaultDTOs) {
							boolean existDeliveryType = false;
							for (ShopDeliveryTypeDTO resultDTO : resultDTOs) {
								if (defaultDTO.getDeliveryType() == resultDTO.getDeliveryType()) {
									existDeliveryType = true;
									break;
								}
							}
							if (!existDeliveryType) {
								resultDTOs.add(defaultDTO);
							}
						}
					}
				}
				// 根据运费价格升序排序,价格相同的按运送方式升序排序
				Collections.sort(resultDTOs, new Comparator<ShopDeliveryTypeDTO>() {

					@Override
					public int compare(ShopDeliveryTypeDTO o1, ShopDeliveryTypeDTO o2) {
						if (o1.getFirstPrice().compareTo(o2.getFirstPrice()) > 0) {
							return 1;
						} else if (o1.getFirstPrice().compareTo(o2.getFirstPrice()) < 0) {
							return -1;
						}
						if (o1.getDeliveryType() > o2.getDeliveryType()) {
							return 1;
						} else if (o1.getDeliveryType() < o2.getDeliveryType()) {
							return -1;
						}
						return 0;
					}

				});
			}
			executeResult.setResult(resultDTOs);
			logger.info("\n 方法[{}]，出参：[{}]", "ShopDeliveryTypeServiceImpl-queryByRegionIdAndTemplateId", JSONObject.toJSONString(executeResult));
		} catch (Exception e) {
			executeResult.addErrorMessage(e.getMessage());
			logger.info("\n 方法[{}]，异常：[{}]", "ShopDeliveryTypeServiceImpl-queryByRegionIdAndTemplateId", e.getMessage());
		}
		return executeResult;
	}
}
