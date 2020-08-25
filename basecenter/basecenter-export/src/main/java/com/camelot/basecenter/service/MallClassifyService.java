package com.camelot.basecenter.service;

import com.camelot.basecenter.dto.MallClassifyDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [文档分类查询、添加、下架、修改]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface MallClassifyService {
   
	/**
	 * 
	 * <p>Discription:文档分类 列表查询</p>
	 * Created on 2015-3-3
	 * @param mallCassifyDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public DataGrid<MallClassifyDTO> queryMallCassifyList(MallClassifyDTO mallCassifyDTO,Pager page);
	/**
	 * 
	 * <p>Discription:文档分类添加</p>
	 * Created on 2015-3-3
	 * @param mallClassifyDTO
	 * @return
	 * @author:yuht
	 */
    public ExecuteResult<String> addMallCassify(MallClassifyDTO mallClassifyDTO);
    /**
     * 
     * <p>Discription:文档分类修改</p>
     * Created on 2015-3-3
     * @param mallCassifyDTO
     * @return
     * @author:yuht
     */
    public ExecuteResult<String> modifyInfoById(MallClassifyDTO mallCassifyDTO);
    /**
     * 
     * <p>Discription:文档分类上下架</p>
     * Created on 2015-3-3
     * @param id
     * @param status
     * @return
     * @author:yuht
     */
    public ExecuteResult<String> modifyStatusById(int id,int status);
}
