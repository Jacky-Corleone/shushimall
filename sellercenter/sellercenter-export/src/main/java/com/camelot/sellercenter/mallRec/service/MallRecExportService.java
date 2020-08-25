package com.camelot.sellercenter.mallRec.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:楼层数据 接口]</p>
 * Created on 2015年1月22日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface MallRecExportService {
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:新增楼层]</p>
	 * Created on 2015年1月26日
	 * @param mallRecDTO
	 * @return
	 * @author:[chenx]
	 */
	public ExecuteResult<String> addMallRec(MallRecDTO mallRecDTO) ;
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:楼层详情]</p>
	 * Created on 2015年1月26日
	 * @param id
	 * @return
	 * @author:[chenx]
	 */
	public MallRecDTO getMallRecById(Long id);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述：查询楼层列表]</p>
	 * Created on 2015年1月26日
	 * @param mallRecDTO
	 * @param page
	 * @return
	 * @author:[创建者中文名字]
	 */
	public DataGrid<MallRecDTO> queryMallRecList(MallRecDTO mallRecDTO, Pager page);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述：修改楼层]</p>
	 * Created on 2015年1月26日
	 * @param mallRecDTO
	 * @return
	 * @author:[chenx]
	 */
    public ExecuteResult<String> modifyMallRec(MallRecDTO mallRecDTO);
    /**
     * 
     * <p>Discription:[方法功能中文描述:楼层上下架]</p>
     * Created on 2015年1月26日
     * @param id
     * @param publishFlag
     * @return
     * @author:[chenx]
     */
    public ExecuteResult<String> 
    modifyMallRecStatus(Long id, String publishFlag);

	/**
	 * <p>Discription:删除下架楼层信息</p>
	 * Created on 2015年8月5日
	 * @param id
	 * @return
	 */
	public ExecuteResult<String> deleteMallRecById(Long id);
	
	/**
     * 
     * <p>Discription:[方法功能中文描述:热卖单品上架前其他单品全部下架]</p>
     * Created on 2016年2月18日
     * @author:[王宏伟]
     */
	public void updateStatusByFloorType(String themeId);
}