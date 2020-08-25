package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.sellercenter.domain.MallRec;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:楼层数据交互接口]</p>
 * Created on 2015年1月22日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface MallRecMybatisDAO  extends BaseDAO<MallRec>{
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:楼层的上下架  根据id ]</p>
	 * Created on 2015年1月22日
	 * @param page
	 * @return
	 * @author:[chenx]
	 */
	public void modifyMallRecStatus(@Param("id")Long id, @Param("status")String publishFlag);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询楼层的列表  ]</p>
	 * Created on 2015年1月23日
	 * @param mallRecDTO
	 * @param page
	 * @return
	 * @author:[chenx]
	 */
	public List<MallRecDTO> queryDTOList(@Param("entity") MallRecDTO mallRecDTO, @Param("page") Pager page);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:根据id查询楼层列表的详情]</p>
	 * Created on 2015年1月23日
	 * @param id
	 * @return
	 * @author:[chenx]
	 */
	public MallRecDTO queryDTOById(@Param("idDTO")Long idDTO);
	
	public Long queryCount(@Param("entity") MallRecDTO mallRecDTO);
	
	/**
     * 
     * <p>Discription:[方法功能中文描述:热卖单品上架前其他单品全部下架]</p>
     * Created on 2016年2月18日
     * @author:[王宏伟]
     */
	public void updateStatusByFloorType(@Param("themeId")String themeId);
}