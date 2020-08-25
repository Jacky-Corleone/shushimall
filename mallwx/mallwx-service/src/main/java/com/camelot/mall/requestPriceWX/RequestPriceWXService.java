package com.camelot.mall.requestPriceWX;

import java.util.Map;

import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.openplatform.common.Pager;

public interface RequestPriceWXService {
	
    /**
     * <p>Discription:[询价新增]</p>
     * Created on 2015年7月16日
     * @param inquiryInfoDTO
     * @param itemIds
     * @param nums
     * @return
     * @author:[chengwt]
     */
	Map<String,String> addInquiry(InquiryInfoDTO inquiryInfoDTO ,String[] shopId,  String[] itemIds, String[] nums, String[] deliveryDates);
	
	
	/**
	 * <p>Discription:[询价更新，更新的信息都是询价主单的，此时还未生成询价单明细]</p>
	 * Created on 2015年7月16日
	 * @param inquiryInfoDTO
	 * @param deleteId
	 * @param itemIds
	 * @param nums
	 * @param statusDetail
	 * @return
	 * @author:[李伟龙]
	 */
	Map<String, String> updateInquiryNew(String uid, InquiryInfoDTO inquiryInfoDTO, String[] deleteId, String[] detailId, String[] itemIds, String[] shopId,
			String[] nums, String[] statusDetail, String[] deliveryDates, String[] skuId);
	/**
	 * <p>Discription:[询价新增李伟龙修改]</p>
	 * Created on 2015年11月11日
	 * @param inquiryInfoDTO
	 * @param itemIds
	 * @param nums
	 * @return
	 * @author:[liwl]
	 */
	Map<String,String> addInquiryNew(InquiryInfoDTO inquiryInfoDTO,String[] shopId, String[] itemIds, String[] nums, String[] deliveryDates,String[] skuIds);
	
	/**
	 * <p>Discription:[询价提交]</p>
	 * Created on 2015年7月16日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[chengwt]
	 */
	Map<String,String> commitInquiry(InquiryInfoDTO dto, Pager pager, String uid);
	
	/**
	 * <p>Discription:[询价更新]</p>
	 * Created on 2015年7月16日
	 * @param inquiryInfoDTO
	 * @param deleteId
	 * @param itemIds
	 * @param nums
	 * @param statusDetail
	 * @return
	 * @author:[chengwt]
	 */
	Map<String,String> updateInquiry(String uid, InquiryInfoDTO inquiryInfoDTO ,
			String[] deleteId ,String[] detailId,  String[] itemIds, String[] shopId, String[] nums, String[] statusDetail, String[] deliveryDates);
	/**
	 * <p>Discription:[卖家更新报价]</p>
	 * Created on 2015年7月16日
	 * @param inquiryInfoDTO
	 * @param price
	 * @param detailstartDate
	 * @param detailendDate
	 * @return
	 * @author:[wanghao]
	 */
	Map<String,String> resopnseModifyInquiry(InquiryInfoDTO inquiryInfoDTO  , String uid, String[] detailId, 
			String[] price, String[] nums, String[] detailstartDate , String[] detailendDate);
	
	/**
	 * <p>Discription:[买家确认价格]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	Map<String,String> commitRequestInquiry(InquiryInfoDTO dto , String uid, String[] detailId);

	/**
	 * <p>Discription:[重新询价]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	Map<String,String> commitnquiryRe(InquiryInfoDTO dto , String uid);
}
