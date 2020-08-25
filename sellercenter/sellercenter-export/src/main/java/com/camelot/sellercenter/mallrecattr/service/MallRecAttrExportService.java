package com.camelot.sellercenter.mallrecattr.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrInDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrQueryDTO;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月28日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface MallRecAttrExportService {
	/**
	 * 
	 * <p>Discription:楼层属性列表查询</p>
	 * Created on 2015年1月28日
	 * @param page
	 * @param mallRecAttrQueryDTO
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public DataGrid<MallRecAttrDTO> queryMallRecAttrList(Pager page,MallRecAttrQueryDTO mallRecAttrQueryDTO);
	
	/**
	 * 
	 * <p>Discription:[楼层属性详情查询]</p>
	 * Created on 2015年1月28日
	 * @param id
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public MallRecAttrDTO getMallRecAttrById(Long id);
	
	/**
	 * 
	 * <p>Discription:[楼层属性添加]</p>
	 * Created on 2015年1月28日
	 * @param mallRecAttrInDTO
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> addMallRecAttr(MallRecAttrInDTO mallRecAttrInDTO);
	
	/**
	 * 
	 * <p>Discription:[楼层属性修改]</p>
	 * Created on 2015年1月28日
	 * @param mallRecAttrInDTO
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> modifyMallRecAttr(MallRecAttrInDTO mallRecAttrInDTO);
	
	/**
	 * 
	 * <p>Discription:[楼层属性上下架]</p>
	 * Created on 2015年1月28日
	 * @param id
	 * @param publishFlag  1、上架  2、下架
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> modifyMallRecAttrStatus(Long id,String publishFlag);

	/**
	 * <p>Discription:删除楼层推荐位信息</p>
	 * Created on 2015年8月5日
	 * @param id
	 * @return
	 */
	public ExecuteResult<String> deleteMallRecAttrById(Long id);
	/**
	 * <p>Discription:定时上架时间小于当前时间的楼层上架</p>
	 * Created on 2015年8月5日
	 * @param id
	 * @return
	 */
	public ExecuteResult<Integer> autoBatchPublishMallRec();
	/**
	 * <p>Discription:定时下架时间小于当前时间的楼层下架</p>
	 * Created on 2015年8月5日
	 * @param id
	 * @return
	 */
	public ExecuteResult<Integer> autoBatchCancelMallRec();
}
