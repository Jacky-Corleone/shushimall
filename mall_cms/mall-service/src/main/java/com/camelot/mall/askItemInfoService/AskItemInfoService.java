package com.camelot.mall.askItemInfoService;

import java.util.Map;

import com.camelot.goodscenter.dto.TranslationInfoDTO;

public interface AskItemInfoService {
	/**
	 * <p>Discription:[新增求购]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> addTranslationInfo(TranslationInfoDTO translationInfoDTO , String uid, String deleteId,String[] ids, String[] category_ids,  String[] itemNames , String[] quantity , String[] flag, String[] matAttributes);

	/**
	 * <p>Discription:[求购删除]</p>
	 * Created on 2015年7月20日
	 * @param uid
	 * @param ids
	 * @param translationInfoDTO
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> deleteTranslation(String uid, String ids, TranslationInfoDTO translationInfoDTO);
	
	/**
	 * <p>Discription:[求购提交]</p>
	 * Created on 2015年7月20日
	 * @param uid
	 * @param dto
	 * @param ids
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> commitTranslation(String uid, TranslationInfoDTO dto, String ids);
	
	/**
	 * <p>Discription:[求购审核]</p>
	 * Created on 2015年7月20日
	 * @param uid
	 * @param dto
	 * @param ids
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> auditTranslation(String uid, TranslationInfoDTO dto, String ids);
	
	/**
	 * <p>Discription:[求购报价保存]</p>
	 * Created on 2015年7月20日
	 * @param uid
	 * @param translationInfoDTO
	 * @param id
	 * @param price
	 * @param detailstartDate
	 * @param detailendDate
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> saveRepTranslationInfo(String uid, TranslationInfoDTO translationInfoDTO  ,
			String[] id, String[] price, String[] addFlag, String[] matDesc, String[] quantity, String[] detailstartDate , String[] detailendDate, String[] matAttributes, String[] category_ids);
	
	
	/**
	 * <p>Discription:[卖家确认价格]</p>
	 * Created on 2015年7月20日
	 * @param uid
	 * @param dto
	 * @param ids
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> commitRepTranslationInfo(String uid, TranslationInfoDTO dto, String ids);
	
	
	/**
	 * <p>Discription:[重新求购]</p>
	 * Created on 2015年7月20日
	 * @param uid
	 * @param dto
	 * @param ids
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> commitTranslationRe(String uid, TranslationInfoDTO dto, String ids);
	
	/**
	 * <p>Discription:[买家接受价格]</p>
	 * Created on 2015年7月20日
	 * @param uid
	 * @param dto
	 * @param id
	 * @return
	 * @author:[chengwt]
	 */
	Map<String, String> acceptTranslationInfo(String uid, TranslationInfoDTO dto, String id, String detailId);
	
}
