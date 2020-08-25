package com.camelot.usercenter.dao;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.userInfo.UserPersonalInfoDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface UserPersonalInfoMybatisDAO  extends BaseDAO<UserPersonalInfoDTO> {
	public void deleteAll(@Param("idList") List<Long> idList);
	public int updateSelective(@Param("userPersonalInfo") UserPersonalInfoDTO userPersonalInfo);
	public int updateAllWithDateTimeCheck(@Param("userPersonalInfoDTO") UserPersonalInfoDTO userPersonalInfo, @Param("prevUpdDt") Date prevUpdDt);
	public int updateSelectiveWithDateTimeCheck(@Param("userPersonalInfoDTO") UserPersonalInfoDTO userPersonalInfo, @Param("prevUpdDt") Date prevUpdDt);
	public List<UserPersonalInfoDTO> searchByCondition(@Param("entity") UserPersonalInfoDTO userPersonalInfo);
	public long searchByConditionCount(@Param("userPersonalInfo") UserPersonalInfoDTO userPersonalInfo);
	public List<UserPersonalInfoDTO> searchByConditionByPager(@Param("pager") Pager<UserPersonalInfoDTO> pager, @Param("userPersonalInfoDTO") UserPersonalInfoDTO userPersonalInfo);
	public long updateSelectiveByIdList(@Param("userPersonalInfoDTO") UserPersonalInfoDTO userPersonalInfo, @Param("idList") List<Long> idList);
	public long updateAllByIdList(@Param("userPersonalInfoDTO") UserPersonalInfoDTO userPersonalInfo, @Param("idList") List<Long> idList);
}