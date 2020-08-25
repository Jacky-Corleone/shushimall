package com.camelot.maketcenter.service;

import java.util.Date;
import java.util.List;

import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.dto.indto.PromotionFullReducitonInDTO;
import com.camelot.maketcenter.dto.indto.PromotionMarkdownInDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [活动查询]</p>
 * Created on 2015-3-4
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface PromotionService {
	/**
	 * 
	 * <p>Discription:[活动查询]</p>
	 * Created on 2015-3-20
	 * @param promotionInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<PromotionOutDTO>> getPromotion(PromotionInDTO promotionInDTO,Pager page);
	
	/**
	 * 
	 * <p>Discription:[活动上下架状态修改]</p>
	 * Created on 2015-3-17
	 * @param onlineState 1 上线 2 下线 
	 * @param promotionInfoId  
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String>  modifyPromotionOnlineState(Integer onlineState,Long...promotionInfoId );
	
	/**
	 * 
	 * <p>Discription:[促销满减添加]</p>
	 * Created on 2015-3-18
	 * @param promotionFullReducitonInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String>  addPromotionFullReduciton(PromotionFullReducitonInDTO promotionFullReducitonInDTO);
	 
	/**
	 * 
	 * <p>Discription:[促销满减修改]</p>
	 * Created on 2015-11-06
	 * @param promotionFullReducitonInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String>  editPromotionFullReduciton(PromotionFullReducitonInDTO promotionFullReducitonInDTO);
	
	/**
	 * 
	 * <p>Discription:[促销折扣添加]</p>
	 * Created on 2015-3-18
	 * @param promotionMarkdownInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> addPromotionMarkdown(PromotionMarkdownInDTO promotionMarkdownInDTO);
	
	/**
	 * 
	 * <p>Discription:[促销折扣添加]</p>
	 * Created on 2015-3-18
	 * @param promotionMarkdownInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> editPromotionMarkdown(PromotionMarkdownInDTO promotionMarkdownInDTO);
	/**
	 * 
	 * <p>Discription:[促销删除]</p>
	 * Created on 2015-3-18
	 * @param promotionInfoId
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> deletePromotion(Long...promotionInfoId);

	/**
	 * 修改活动
	 * @param promotionInfo
	 * @return
	 */
	public ExecuteResult<String> updatePromotion(PromotionInfo promotionInfo);
	/**
     * 根据店铺shopId,活动开始时间、活动结束时间，查询是否有直降活动和该活动冲突
     * @param shopId  如果传入0，则表示平台活动
     * @param startTime
     * @param endTime
     * @return
     */
    public ExecuteResult<List<Long>> getMdPromotionConflict(Long shopId,Date startTime,Date endTime);
}
