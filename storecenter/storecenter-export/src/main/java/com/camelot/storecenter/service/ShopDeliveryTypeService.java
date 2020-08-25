package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopDeliveryTypeDTO;

/**
 * <p>Description: [运送方式service]</p>
 * Created on 2015年10月26日
 *
 * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopDeliveryTypeService {

    /**
     * <p>Discription:[运送方式添加]</p>
     * Created on 2015年10月26日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<String> addShopDeliveryType(ShopDeliveryTypeDTO dto);
    /**
     * <p>Discription:[运送方式删除]</p>
     * Created on 2015年10月26日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<String> deleteShopDeliveryType(ShopDeliveryTypeDTO dto);
    /**
     * <p>Discription:[运送方式修改]</p>
     * Created on 2015年10月26日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<String> updateShopDeliveryType(ShopDeliveryTypeDTO dto);
    /**
     * <p>Discription:[运送方式添加]</p>
     * Created on 2015年10月26日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<List<ShopDeliveryTypeDTO>> queryShopDeliveryType(ShopDeliveryTypeDTO dto);
    
    /**
	 * 
	 * <p>Description: [根据运费模版Id和地区Id查询运费策略，如果找不到运费策略，就使用默认的（全国）运费策略]</p>
	 * Created on 2015年10月26日
	 * @param regionId 地区ID
	 * @param shopFreightTemplateId 运费模版ID
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<List<ShopDeliveryTypeDTO>> queryByRegionIdAndTemplateId(Long regionId, Long shopFreightTemplateId);
}
