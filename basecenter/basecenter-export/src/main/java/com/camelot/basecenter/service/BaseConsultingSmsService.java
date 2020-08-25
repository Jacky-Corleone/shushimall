package com.camelot.basecenter.service;

import com.camelot.basecenter.dto.BaseConsultingSmsDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [咨询service]</p>
 * Created on 2015年3月20日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface BaseConsultingSmsService {
	/**
	 * <p>Discription:[增加咨询]</p>
	 * Created on 2015年3月20日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<String> addBaseConsultingSms(BaseConsultingSmsDTO dto);
	/**
	 * <p>Discription:[根据条件查询询价列表]</p>
	 * Created on 2015年3月20日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
  public DataGrid<BaseConsultingSmsDTO> queryList(BaseConsultingSmsDTO dto,Pager<BaseConsultingSmsDTO> page);
  /**
   * <p>Discription:[根据id修改咨询]</p>
   * Created on 2015年3月20日
   * @param id
   * @return
   * @author:[杨芳]
   */
  public ExecuteResult<String> modifyById(BaseConsultingSmsDTO dto);
  /**
   * <p>Discription:[根据id删除咨询记录（将有效标记status改为失效2）]</p>
   * Created on 2015年3月20日
   * @param id
   * @return
   * @author:[创建者中文名字]
   */
  public ExecuteResult<String> deleteById(Long id);
}
