package com.camelot.goodscenter.dao;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.InquiryMatDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

public interface InquiryMatDAO extends BaseDAO<InquiryMatDTO>{

	public List<Map> queryPage(@Param("pager") Pager pager, @Param("inquiryMat") InquiryMatDTO inquiryMat);
	
	public Long queryPageCount(@Param("inquiryMat") InquiryMatDTO inquiryMat);
	
	public InquiryMatDTO findById(Long id);
	
	public Integer insert(InquiryMatDTO inquiryMat);
	
	public Integer update(InquiryMatDTO inquiryMat);

	public void delete(@Param("codes") List<String> codes);
	
	public List<Map<String,Object>> findAll();

	public InquiryMatDTO findByInquiryMatDTO(@Param("inquiryMat")InquiryMatDTO inquiryMat);
}
