package com.camelot.mall.requestPriceService;

import java.util.Map;

import com.camelot.goodscenter.dto.InquiryInfoDTO;

public interface RequestPriceService {
	/**
	 * <p>Discription:[询价新增]</p>
	 * Created on 2015年7月20日
	 * @param inquiryInfoDTO
	 * @param deleteId
	 * @param flag
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> addInquiry(InquiryInfoDTO inquiryInfoDTO ,String uid, String deleteId,  
			String[] id, String[] shopId, String[] nums ,  String[] flag, String[] deliveryDates);
	
	/**
	 * <p>Discription:[询价新增]</p>
	 * Created on 2015年11月10日
	 * @param inquiryInfoDTO
	 * @param deleteId
	 * @param flag
	 * @return
	 * @author:[liwl]
	 */
	Map<String, String> addInquiryNew(InquiryInfoDTO inquiryInfoDTO ,String uid, String deleteId,  
			String[] id, String[] shopId, String[] nums ,  String[] flag, String[] deliveryDates,String[] skuId);
	/**
	 * <p>Discription:[询价发布]</p>
	 * Created on 2015年11月2日11:51:46
	 * @param inquiryInfoDTO
	 * @param deleteId
	 * @param flag
	 * @return
	 * @author:[chengwt]
	 */
	
	Map<String, String> upimmediately(InquiryInfoDTO inquiryInfoDTO ,String uid, String deleteId,  
			String[] id, String[] shopId, String[] nums ,  String[] flag, String[] deliveryDates);
	
	
	
	
	/**
	 * <p>Discription:[询价删除]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> deleteInquiry(String uid, String ids);
	
	/**
	 * <p>Discription:[买家提交询价]</p>
	 * Created on 2015年7月20日
	 * @param ids
	 * @param uid
	 * @param inquiryInfoDTO
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> commitInquiry(String ids, String uid, InquiryInfoDTO inquiryInfoDTO);
	
	/**
	 * <p>Discription:[卖家保存询价信息]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> resopnseModifyInquiry(InquiryInfoDTO inquiryInfoDTO, String uid, String[] id, 
			String[] nums, String[] price, String[] detailstartDate , String[] detailendDate );
	
	/**
	 * <p>Discription:[卖家提交报价]</p>
	 * Created on 2015年7月20日
	 * @param dto
	 * @param uid
	 * @param ids
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> responseCommitInquiry(InquiryInfoDTO dto,String uid, String ids);
	
	/**
	 * <p>Discription:[重新询价]</p>
	 * Created on 2015年7月20日
	 * @param dto
	 * @param uid
	 * @param ids
	 * @return
	 * @author:[wanghao]
	 */
	Map<String, String> commitInquiryRe(InquiryInfoDTO dto,String uid, String ids);
	
	/**
	 * <p>Discription:[买家接受价格]</p>
	 * Created on 2015年7月20日
	 * @param dto
	 * @param uid
	 * @param ids
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> commitInquiryYes(InquiryInfoDTO dto,String uid, String ids ,String detailIds);
}
