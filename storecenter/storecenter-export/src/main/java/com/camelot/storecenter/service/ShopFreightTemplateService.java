package com.camelot.storecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFreightTemplateDTO;

/**
 * <p>Description: [运费模板service层]</p>
 * Created on 2015年10月26日
 * @author  <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopFreightTemplateService {


    /**
     * <p>Discription:[运费模板修改]</p>
     * Created on 2015年10月22日
     * @param dto
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<ShopFreightTemplateDTO> update(ShopFreightTemplateDTO dto);

    /**
     * <p>Discription:[运费模板分页查询]</p>
     * Created on 2015年10月22日
     * @param dto
     * @param pager
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<DataGrid<ShopFreightTemplateDTO>> queryShopFreightTemplateList(ShopFreightTemplateDTO dto, Pager pager);
    /**
     * <p>Discription:[运费模板单个删除]</p>
     * Created on 2015年10月22日
     * @param id
     * @return
     * @author:[郭建宁]
     */
    public ExecuteResult<String> deleteShopFreightTemplateById(long id);
    /**
     * <p>Discription:[运费模板添加]</p>
     * Created on 2015年10月22日
     * @param dto
     * @return
     * @author:[杨芳]
     */
    public ExecuteResult<ShopFreightTemplateDTO> addShopFreightTemplate(ShopFreightTemplateDTO dto);

    /**
     * 根据id获取运费模板内容
     * Created on 2015年10月22日
     * @param id
     * @return
     * @author:[郭建宁]
     */
    public ShopFreightTemplateDTO queryShopFreightTemplateById(Long id);
    
    /**
	 * 
	 * <p>
	 * Description: [根据ID查询]
	 * </p>
	 * Created on 2015年10月23日
	 * 
	 * @param shopFreightTemplateId
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<ShopFreightTemplateDTO> queryById(Long shopFreightTemplateId);
	
	/**
	 * 
	 * <p>Description: [根据运费模版Id查询运费模版，同时把运费策略和优惠策略查出来，并根据regionId过滤运费策略]</p>
	 * Created on 2015年11月9日
	 * @param regionId
	 * @param shopFreightTemplateId
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<ShopFreightTemplateDTO> queryByRegionIdAndTemplateId(Long regionId, Long shopFreightTemplateId);

	/**
	 * 
	 * <p>Description: [模版复制]</p>
	 * Created on 2015年11月13日
	 * @param shopFreightTemplateId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<ShopFreightTemplateDTO> copy(Long shopFreightTemplateId);
}
