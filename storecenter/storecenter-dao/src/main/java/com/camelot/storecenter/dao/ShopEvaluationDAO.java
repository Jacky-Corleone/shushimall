package com.camelot.storecenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopEvaluationDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalQueryDTO;

import java.util.*;
/**
 * <p>Description: [店铺评价DAO]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopEvaluationDAO extends BaseDAO<ShopEvaluationDTO>{
	/**
	 * 
	 * <p>Discription:[添加店铺评价]</p>
	 * Created on 2015-4-10
	 * @param ItemEvaluationDTO
	 * @author:zhangzq
	 */
	void insertShopEvaluation(ShopEvaluationDTO shopEvaluationDTO);
	
	/**
	 * 
	 * <p>Discription:[店铺评价统计]</p>
	 * Created on 2015-4-10
	 * @param Map<String,List<ShopEvaluationTotalQueryDTO>>
	 * @author:zhangzq
	 */
	List<Map<String,List<ShopEvaluationTotalQueryDTO>>> queryShopEvaluationTotal(@Param("entity") ShopEvaluationQueryInDTO entity);
	
	/**
	 * 查询总数量
	 * @param shopEvaluationDTO
	 * @return
	 */
	Long queryCount(@Param("entity")ShopEvaluationQueryInDTO entity);
	
	/**
	 * 分页查询数据
	 * @param shopEvaluationDTO
	 * @param page
	 * @return
	 */
	List<ShopEvaluationDTO> queryShopEvaluationList(@Param("entity")ShopEvaluationQueryInDTO entity,@Param("page") Pager page);
	
	
}
