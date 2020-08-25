package com.camelot.goodscenter.dao;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.InquiryOrderDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

public interface InquiryOrderDAO extends BaseDAO<InquiryOrderDTO> {

	public List<InquiryOrderDTO> queryPage(@Param("pager") Pager pager, @Param("inquiryOrder") InquiryOrderDTO inquiryOrder);
	
	public Long queryPageCount(@Param("inquiryOrder") InquiryOrderDTO inquiryOrder);
	
	public InquiryOrderDTO findById(Long id);
	
	public Integer insert(InquiryOrderDTO inquiryOrder);
	
	public Integer update(InquiryOrderDTO inquiryOrder);

	public void delete(@Param("codes") List<String> codes);
	
	public List<Map<String,Object>> findAll();

	public InquiryOrderDTO findByInquiryOrderDTO(@Param("inquiryOrder")InquiryOrderDTO inquiryOrder);
}
