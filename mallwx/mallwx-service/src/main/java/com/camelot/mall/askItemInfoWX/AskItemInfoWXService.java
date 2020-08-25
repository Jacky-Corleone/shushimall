package com.camelot.mall.askItemInfoWX;

import java.util.Map;

import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.openplatform.common.Pager;

public interface AskItemInfoWXService {

    /**
     * <p>Discription:[求购新增]</p>
     * Created on 2015年7月16日
     * @param nums
     * @return
     * @author:[chengwt]
     */
	Map<String,String> addTranslation(TranslationInfoDTO translationDTO ,  String[] category_ids, String[] itemNames, String[] nums, String[] matAttributes);
	
	
	/**
	 * <p>Discription:[求购更新]</p>
	 * Created on 2015年7月16日
	 * @param deleteId
	 * @param nums
	 * @param statusDetail
	 * @return
	 * @author:[chengwt]
	 */
	Map<String,String> updateTranslation(String uid, TranslationInfoDTO dto ,
			String[] deleteId , String[] ids, String[] category_ids, String[] itemNames, String[] nums, String[] statusDetail, String[] matAttributes);
	
	
	/**
	 * <p>Discription:[求购提交]</p>
	 * Created on 2015年7月16日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[chengwt]
	 */
	Map<String,String> commitTranslation(String uid, TranslationInfoDTO dto, Pager pager);
	
	/**删除求购
	 * @param uid
	 * @return
	 */
	Map<String,String> deleteTranslation(TranslationInfoDTO dto , String uid);
	
	/**审核求购
	 * @param uid
	 * @return
	 */
	Map<String,String> auditTranslation(TranslationInfoDTO dto , String uid);
	/**
	 * <p>Discription:[卖家更新报价]</p>
	 * Created on 2015年7月16日
	 * @param id
	 * @param price
	 * @param detailstartDate
	 * @param detailendDate
	 * @return
	 * @author:[chengwt]
	 */
	Map<String,String> resopnseModifyTranslation(TranslationInfoDTO dto  ,String u_id, String[] flag_status, String[] id,  String[] itemNames,
			String[] quantity,String[] price, String[] detailstartDate , String[] detailendDate,String[] matAttributes, String[] category_ids);


	/**卖家确认求购
	 * @param uid
	 * @return
	 */
	Map<String,String> commitRepTranslation(TranslationInfoDTO dto , String uid);

	/**
	 * <p>Discription:[买家确认价格]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	Map<String,String> commitRequestTranslation(TranslationInfoDTO dto , String uid, String[] detailId);

	Map<String, String> commitTranslationRe(TranslationInfoDTO dto , String uid);
}
