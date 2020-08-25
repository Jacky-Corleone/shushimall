package com.camelot.maketcenter.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.centralPurchasing.domain.IntegralConfig;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
/**
 * 集采活动
 * @author 周志军
 *
 */
public interface IntegralConfigDAO extends BaseDAO<IntegralConfig> {
	/**
	 * 依据money获取积分配置信息
	 * @param money
	 * @author 周志军
	 * create on 2015-12-8
	 * @return
	 */
	public List<IntegralConfig> queryByMoney(@Param("money")BigDecimal money,@Param("platformId")Long platformId);
	/**
	 * 
	 * <p>Discription:[根据获取积分类型跟平台类型]</p>
	 * Created on 2015-12-16
	 * @param integralConfig
	 * @param page
	 * @return
	 * @author:[范东藏]
	 */
	public List<IntegralConfig> queryOneType(@Param("entity")IntegralConfig integralConfig,@Param("page") Pager<IntegralConfig> page);
	/**
	 * 
	 * <p>Discription:[根据类型删除相应积分用来修改]</p>
	 * Created on 2015-12-16
	 * @param integralConfig
	 * @return
	 * @author:[范东藏]
	 */
	public Integer deleteByType(@Param("entity")IntegralConfig integralConfig);
}
