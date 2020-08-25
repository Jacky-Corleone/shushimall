package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.domain.Iptable;
import com.camelot.basecenter.dto.IptableDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface IptableDAO extends BaseDAO<Iptable>{
	/**
	 * <p>Discription:[分页查询XXXX集合数据]</p>
	 * Created on 2016年02月24日
	 * @param pager 分页数据
	 * @param iptableDTO 条件
	 * @return XXXX集合数据
	 * @author:中文名字
	 */
	public List<IptableDTO> queryList(@Param("entity") IptableDTO iptableDTO);
	
	
	/**
	 * <p>Discription:[查询XXXX]</p>
	 * Created on 2016年02月24日
	 * @param iptableDTO XXXX
	 * @return IptableDTO XXXXX
	 * @author:中文名字
	 */
	public IptableDTO queryIptableDTO(@Param("entity") IptableDTO iptableDTO);
	
}
