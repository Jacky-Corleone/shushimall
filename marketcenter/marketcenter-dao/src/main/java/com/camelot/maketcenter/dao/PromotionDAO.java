package com.camelot.maketcenter.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.example.domain.Demo;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * 数据库操作类
 */
public interface PromotionDAO extends BaseDAO<Demo> {
	
	/**
	 * 
	 * <p>Discription:[活动信息列表查询]</p>
	 * Created on 2015-3-17
	 * @param promotionInDTO
	 * @return
	 * @author:yuht
	 */
	List<PromotionOutDTO> getPromotion(@Param("entity") PromotionInDTO promotionInDTO,@Param("page")Pager page);
	/**
	 * 
	 * <p>Discription:[活动上下架状态修改]</p>
	 * Created on 2015-3-17
	 * @param promotionInfo
	 * @return
	 * @author:yuht
	 */
	Integer modifyPromotionOnlineState(@Param("entity")PromotionInfo promotionInfo);
	
	/**
	 * 
	 * <p>Discription:[查询活动总条数]</p>
	 * Created on 2015-3-23
	 * @param promotionInDTO
	 * @return
	 * @author:yuht
	 */
	Long getPromotionCount(@Param("entity")PromotionInDTO promotionInDTO);
	

}
