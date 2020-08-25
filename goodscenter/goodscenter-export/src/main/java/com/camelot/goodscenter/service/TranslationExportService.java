package com.camelot.goodscenter.service;

import com.camelot.goodscenter.dto.*;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import java.util.Map;

/** 
 * <p>Description: [商品SKU求购Service]</p>
 * Created on 2015年06月08日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface TranslationExportService {
	/**
	 * <p>Discription:[根据求购编号更新求购主表的卖家id]</p>
	 * Created on 2015年11月23日
	 * @param dto
	 * @return
	 * @author:李伟龙
	 */
    public ExecuteResult<String> updateByTranslationNo(TranslationInfoDTO dto);
	/**
	 * <p>Discription:[根据条件查询求购详情]</p>
	 * Created on 2015年06月08日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
    public ExecuteResult<TranslationInfoDTO> queryByTranslationInfo(TranslationInfoDTO dto);
	/**
	 * <p>Discription:[根据条件查询求购的列表]</p>
	 * Created on 2015年06月08日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
   public ExecuteResult<DataGrid<TranslationInfoDTO>> queryTranslationInfoList(TranslationInfoDTO dto, Pager page);
   /**
    * <p>Discription:[根据条件查询求购的列表包含物料名称等信息]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<Map>> queryTranslationInfoPager(TranslationInfoDTO dto, Pager page);

   /**
    * <p>Discription:[根据条件查询求购明细详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<TranslationMatDTO> queryByTranslationMat(TranslationMatDTO dto);
   /**
    * <p>Discription:[根据条件查询求购明细的列表]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<Map>> queryTranslationMatList(TranslationMatDTO dto, Pager page);

   /**
    * <p>Discription:[根据条件查询求购订单详情]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<TranslationOrderDTO> queryByTranslationOrder(TranslationOrderDTO dto);
   /**
    * <p>Discription:[根据条件查询求购订单的列表]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<TranslationOrderDTO>> queryTranslationOrderList(TranslationOrderDTO dto, Pager page);

/**
    * <p>Discription:[生成求购详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addTranslationInfo(TranslationInfoDTO dto);
   /**
    * <p>Discription:[生成求购详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addTranslationMat(TranslationMatDTO dto);
   /**
    * <p>Discription:[生成求购订单]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addTranslationOrder(TranslationOrderDTO dto);

   /**
    * <p>Discription:[修改求购详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyTranslationInfo(TranslationInfoDTO dto);
   /**
    * <p>Discription:[修改求购明细]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyTranslationMat(TranslationMatDTO dto);
   /**
    * <p>Discription:[修改求购明细]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyTranslationOrder(TranslationOrderDTO dto);
   /**
    * <p>Discription:[生成询价编码]</p>
    * Created on 2015年06月23日
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> createTranslationNo();
}
