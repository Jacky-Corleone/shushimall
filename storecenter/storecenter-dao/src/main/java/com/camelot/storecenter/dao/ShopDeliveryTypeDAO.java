package com.camelot.storecenter.dao;


import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopDeliveryTypeDTO;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>Description: [运费模板-优惠方式dao层]</p>
 * Created on 2015年10月22日
 * @author  <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopDeliveryTypeDAO extends BaseDAO<ShopDeliveryTypeDTO>{

    public List<ShopDeliveryTypeDTO> selectListByCondition(@Param("entity") ShopDeliveryTypeDTO dto);
}