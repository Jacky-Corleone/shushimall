package com.camelot.goodscenter.service;

import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.goodscenter.dto.InquiryMatDTO;
import com.camelot.goodscenter.dto.InquiryOrderDTO;
import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import java.util.List;
import java.util.Map;

/** 
 * <p>Description: [商品SKU协议Service]</p>
 * Created on 2015年06月08日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface InquiryExportService {
	/**
	 * <p>Discription:[根据条件查询协议详情]</p>
	 * Created on 2015年06月08日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
    public ExecuteResult<InquiryInfoDTO> queryByInquiryInfo(InquiryInfoDTO dto);
	/**
	 * <p>Discription:[根据条件查询协议的列表]</p>
	 * Created on 2015年06月08日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
   public ExecuteResult<DataGrid<InquiryInfoDTO>> queryInquiryInfoList(InquiryInfoDTO dto, Pager page);
   /**
    * <p>Discription:[根据条件查询协议的列表包含物料名称等信息]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<Map>> queryInquiryInfoPager(InquiryInfoDTO dto,Pager page);

   /**
    * <p>Discription:[根据条件查询协议明细详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<InquiryMatDTO> queryByInquiryMat(InquiryMatDTO dto);
   /**
    * <p>Discription:[根据条件查询协议明细的列表]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<Map>> queryInquiryMatList(InquiryMatDTO dto, Pager page);

   /**
    * <p>Discription:[根据条件查询协议订单详情]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<InquiryOrderDTO> queryByInquiryOrder(InquiryOrderDTO dto);
   /**
    * <p>Discription:[根据条件查询协议订单的列表]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<InquiryOrderDTO>> queryInquiryOrderList(InquiryOrderDTO dto, Pager page);

/**
    * <p>Discription:[生成协议详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addInquiryInfo(InquiryInfoDTO dto);
   /**
    * <p>Discription:[生成协议详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addInquiryMat(InquiryMatDTO dto);
   /**
    * <p>Discription:[生成协议订单]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addInquiryOrder(InquiryOrderDTO dto);

   /**
    * <p>Discription:[修改协议详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyInquiryInfo(InquiryInfoDTO dto);
   /**
    * <p>Discription:[修改协议详情根据ID]</p>
    * Created on 2015年07月24日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyInquiryInfoById(InquiryInfoDTO dto);

   /**
    * <p>Discription:[修改协议明细]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyInquiryMat(InquiryMatDTO dto);
   /**
    * <p>Discription:[修改协议明细]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyInquiryOrder(InquiryOrderDTO dto);
   /**
    * <p>Discription:[生成询价编码]</p>
    * Created on 2015年06月23日
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> createInquiryNo();
   
   /**
    * <p>Discription:[通子表ids查询询价信息]</p>
    * Created on 2015年12月30日
    * @return
    * @author:WHW
    */
   public ExecuteResult<DataGrid<InquiryInfoDTO>> queryByMids(String detailIds);
   
   /**
    * <p>Discription:[通过询价编码查询询价信息]</p>
    * Created on 2015年12月30日
    * @return
    * @author:WHW
    */
   public ExecuteResult<DataGrid<InquiryInfoDTO>> queryByInquiryNos(String nos);
}
