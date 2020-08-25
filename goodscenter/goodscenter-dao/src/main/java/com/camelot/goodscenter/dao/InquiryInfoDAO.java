package com.camelot.goodscenter.dao;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

public interface InquiryInfoDAO extends BaseDAO<InquiryInfoDTO> {

	public List<InquiryInfoDTO> queryPage(@Param("pager") Pager pager, @Param("inquiryInfo") InquiryInfoDTO inquiryInfo);
	
	public Long queryPageCount(@Param("inquiryInfo") InquiryInfoDTO inquiryInfo);
	
	public InquiryInfoDTO findById(Long id);
	
	public Integer insert(InquiryInfoDTO inquiryInfo);
	
	public Integer update(InquiryInfoDTO inquiryInfo);

	public void delete(@Param("codes") List<String> codes);
	
	public List<InquiryInfoDTO> findAll(@Param("inquiryInfo")InquiryInfoDTO inquiryInfo);

	public InquiryInfoDTO findByInquiryInfoDTO(@Param("inquiryInfo")InquiryInfoDTO inquiryInfo);

	public List<Map> queryInquiryInfoPager(@Param("pager") Pager pager, @Param("inquiryInfo") InquiryInfoDTO inquiryInfo);

	public Long queryInquiryInfoPagerCount(@Param("inquiryInfo") InquiryInfoDTO inquiryInfo);

	public List<InquiryInfoDTO> queryByMids(@Param("ids")Long[] ids);

	public List<InquiryInfoDTO> queryByInquiryNos(@Param("ids")String[] ids);
}
