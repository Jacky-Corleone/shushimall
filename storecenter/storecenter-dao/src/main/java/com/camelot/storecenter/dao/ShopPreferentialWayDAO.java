package com.camelot.storecenter.dao;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Description: [优惠方式dao]</p>
 * Created on 2015年10月26日
 *
 * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopPreferentialWayDAO extends BaseDAO<ShopPreferentialWayDTO> {


    public List<ShopPreferentialWayDTO> selectListByCondition(@Param("entity") ShopPreferentialWayDTO dto);

}
