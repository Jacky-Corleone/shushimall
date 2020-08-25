package com.camelot.mall.askItemInfoWX.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.mall.askItemInfoWX.AskItemInfoWXService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
@Service(value="askItemInfoWXService")
public class AskItemInfoWxServiceImpl implements AskItemInfoWXService {
	@Resource
	private UserExportService userExportService;
	@Resource
	private TranslationExportService translationExportService;
	@Resource
	private ItemExportService itemExportService;

	/**
	 * <p>Discription:[求购新增]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> addTranslation(TranslationInfoDTO translationDTO, String[] category_ids, String[] itemNames, String[] nums, String[] matAttributes) {
		ExecuteResult<String> result = new ExecuteResult<String>();
    	Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "保存成功!");
    	for(int i=0; i< itemNames.length ; i++){
    		translationDTO.setMatCd(itemNames[i]);
    		translationDTO.setItemName(itemNames[i]);
			translationDTO.setMatAttribute(matAttributes[i]);
    		translationDTO.setQuantity(Integer.parseInt(nums[i]));
			//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
			translationDTO.setCategoryId(Integer.parseInt(category_ids[i]));
			result = translationExportService.addTranslationInfo(translationDTO);
			if(!result.isSuccess()){
				resultMap.put("result", "保存失败!"+result.getErrorMessages().get(0));
				return resultMap;
			}
    	}
		return resultMap;
	}

	/**
	 * <p>Discription:[求购单信息更新]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> updateTranslation(String uid, TranslationInfoDTO dto, String[] deleteId,
			String[] ids, String[] category_ids, String[] itemNames, String[] nums, String[] statusDetail, String[] matAttributes) {
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "更新成功!");
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	for(int i= 0; i< statusDetail.length ; i++){
    		if("i".equals(statusDetail[i])){
    			dto.setActiveFlag("1");
    			dto.setCreateBy(uid);
    			dto.setCreateDate(new Date());
    			dto.setStatus("0");
    			dto.setQuantity(Integer.parseInt(nums[i]));
    			dto.setMatCd(itemNames[i]);
				dto.setMatAttribute(matAttributes[i]);
				//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
				dto.setCategoryId(Integer.parseInt(category_ids[i]));
				result = translationExportService.addTranslationInfo(dto);
				if(!result.isSuccess()){
					resultMap.put("result","更新失败!"+result.getErrorMessages().get(0));
					return resultMap;
				}
    		}else if("u".equals(statusDetail[i])){
    			dto.setId(Long.parseLong(ids[i]));
    			dto.setQuantity(Integer.parseInt(nums[i]));
    			dto.setMatCd(itemNames[i]);
    			dto.setActiveFlag("1");
    			dto.setUpdateBy(uid);
    			dto.setUpdateDate(new Date());
				dto.setMatAttribute(matAttributes[i]);
				//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
				dto.setCategoryId(Integer.parseInt(category_ids[i]));
	    		result = translationExportService.modifyTranslationInfo(dto);
				if(!result.isSuccess()){
					resultMap.put("result","更新失败!"+result.getErrorMessages().get(0));
					return resultMap;
				}
    		}
    	}
    	String translationNo = dto.getTranslationNo();
    	//物品删除的，将单据删除
    	if(deleteId.length > 0){
	    	for(String id : deleteId){
	    		if(!"".equals(id)){
	    			dto = new TranslationInfoDTO(); 
	    			dto.setId(Long.parseLong(id));
					dto.setActiveFlag("0");
	    			dto.setUpdateBy(uid);
	    			dto.setUpdateDate(new Date());
					result = translationExportService.modifyTranslationInfo(dto);
					if(!result.isSuccess()){
						resultMap.put("result","更新失败!"+result.getErrorMessages().get(0));
						return resultMap;
					}
	    		}
	    	}
    	}
		return resultMap;
	}

	/**
	 * <p>Discription:[求购单删除]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> deleteTranslation(TranslationInfoDTO dto,
			String uid) {
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "删除成功!");
		Pager pager = new Pager();
		pager.setRows(Integer.MAX_VALUE);
		pager.setPage(1);
		ExecuteResult<DataGrid<Map>> translatioDTOList =  translationExportService.queryTranslationInfoPager(dto, pager);
		if(translatioDTOList.isSuccess()) {
			for(Map map : translatioDTOList.getResult().getRows()) {
				TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
				translationInfoDTO.setId(Long.parseLong(""+map.get("id")));
				translationInfoDTO.setActiveFlag("0");
				translationInfoDTO.setUpdateBy(uid);
				translationInfoDTO.setUpdateDate(new Date());
				ExecuteResult<String> result = translationExportService.modifyTranslationInfo(translationInfoDTO);
				if (!result.isSuccess()) {
					resultMap.put("result", "删除失败!" + result.getResultMessage());
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "删除失败!" + translatioDTOList.getResultMessage());
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * <p>Discription:[求购单提交]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitTranslation(String uid, TranslationInfoDTO dto, Pager pager) {
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "提交成功!");
		//根据编号查询其下的物品id
		ExecuteResult<DataGrid<Map>>  trans = translationExportService.queryTranslationInfoPager(dto, pager);
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
			for(Map map : trans.getResult().getRows()){
				dto.setId(Long.parseLong(""+map.get("id")));
				dtoresult = translationExportService.modifyTranslationInfo(dto);
				if(!dtoresult.isSuccess()){
					resultMap.put("result", "提交失败!"+dtoresult.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "提交失败!"+trans.getErrorMessages());
		}
		return resultMap;
	}

	/**
	 * <p>Discription:[求购单审核，暂时不用]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> auditTranslation(TranslationInfoDTO dto,
			String uid) {
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "审核成功!");
		Pager<TranslationInfoDTO> pager = new Pager<TranslationInfoDTO>();
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		pager.setRows(Integer.MAX_VALUE);
		pager.setPage(1);
		ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(dto, pager);
		if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
			for(Map map : trans.getResult().getRows()){
				dto.setId(Long.parseLong(""+map.get("id")));
				dtoresult = translationExportService.modifyTranslationInfo(dto);
				if(!dtoresult.isSuccess()){
					resultMap.put("result", "审核失败!"+dtoresult.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "审核失败!"+trans.getErrorMessages());
			return resultMap;
		}
		
		return resultMap;
	}
	
	/**
	 * <p>Discription:[卖家保存报价信息，生成报价明细]</p>
	 * Created on 2015年7月18日
	 * @param dto
	 * @param id
	 * @param price
	 * @param detailstartDate
	 * @param detailendDate
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> resopnseModifyTranslation(TranslationInfoDTO dto,String uid, String[] flag_status, String[] id, String[] itemNames, String[] quantity, String[] price,
			String[] detailstartDate, String[] detailendDate, String[] matAttributes, String[] category_ids) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "更新成功!");
    	//将主单的状态改为3，以便前台确认价格时，3以后的才可以直接确认价格
    	SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
    	UserDTO userDTO = userExportService.queryUserById(Long.parseLong(uid));
    	ExecuteResult<String> resultDetail = new ExecuteResult<String>();
    	for(int i= 0; i< flag_status.length; i++){
			if("i".equals(flag_status[i])){
				//add detail
				TranslationMatDTO detailDto = new TranslationMatDTO();
				detailDto.setTranslationNo(dto.getTranslationNo());
				detailDto.setMatDesc(itemNames[i]);
				detailDto.setQuantity(Integer.parseInt(quantity[i]));
				detailDto.setCreateBy(uid);
				detailDto.setActiveFlag("1");
				detailDto.setCreateDate(new Date());
				detailDto.setMatAttribute(matAttributes[i]);
				if(price[i] != null && !"".equals(price[i])) {
					detailDto.setMatPrice(Double.parseDouble(price[i]));
					try {
						detailDto.setBeginDate(sim.parse(detailstartDate[i]));
						detailDto.setEndDate(sim.parse(detailendDate[i]));
					} catch (ParseException e) {
						resultMap.put("result", "交货时间转化失败");
						e.printStackTrace();
					}
				}
				detailDto.setShopId(userDTO.getShopId().intValue());
				detailDto.setSupplierId(dto.getSupplierId());
				//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
				detailDto.setCategoryId(Integer.parseInt(category_ids[i]));
				ExecuteResult<String> resultDet = translationExportService.addTranslationMat(detailDto);
				if(!resultDet.isSuccess()){
					resultMap.put("result", "保存失败!"+resultDet.getResultMessage());
					return resultMap;
				}else{
					//卖家插入明细后更新主表卖家id
					TranslationInfoDTO translationInfo = new TranslationInfoDTO();
					translationInfo.setSupplierId(Integer.parseInt(uid));
					translationInfo.setUpdateBy(dto.getCreateBy());
					translationInfo.setUpdateDate(new Date());
					translationInfo.setTranslationNo(dto.getTranslationNo());
					translationExportService.updateByTranslationNo(translationInfo);
				}
			}else{
				
				TranslationMatDTO detailDto = new TranslationMatDTO();
				detailDto.setId(Long.parseLong(id[i]));
				detailDto.setUpdateBy(uid);
				detailDto.setUpdateDate(new Date());
				if(price[i] != null && !"".equals(price[i])) {
					detailDto.setMatPrice(Double.parseDouble(price[i]));
					try {
						detailDto.setBeginDate(sim.parse(detailstartDate[i]));
						detailDto.setEndDate(sim.parse(detailendDate[i]));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						resultMap.put("result", "交货时间转化失败");
						e.printStackTrace();
					}
				}
				ExecuteResult<String> resultDet = translationExportService.modifyTranslationMat(detailDto);
				if(!resultDet.isSuccess()){
					resultMap.put("result", "更新失败!"+resultDet.getResultMessage());
					return resultMap;
				}else{
					//卖家插入明细后更新主表卖家id
					TranslationInfoDTO translationInfo = new TranslationInfoDTO();
					translationInfo.setSupplierId(Integer.parseInt(uid));
					translationInfo.setUpdateBy(dto.getCreateBy());
					translationInfo.setUpdateDate(new Date());
					translationInfo.setTranslationNo(dto.getTranslationNo());
					translationExportService.updateByTranslationNo(translationInfo);
				}
				
			}
		}
		/*Pager<TranslationInfoDTO> pager = new Pager<TranslationInfoDTO>();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(dto, pager);
		if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
			for(Map map : trans.getResult().getRows()){
				dto.setId(Long.parseLong(""+map.get("id")));
				dtoresult = translationExportService.modifyTranslationInfo(dto);
				if(!dtoresult.isSuccess()){
					resultMap.put("result", "审核失败!"+dtoresult.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "审核失败!"+trans.getErrorMessages());
			return resultMap;
		}*/
		return resultMap;
	}

	/**
	 * <p>Discription:[卖家确认报价信息（暂时不用）]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitRepTranslation(TranslationInfoDTO dto,	 String uid) {
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "确认成功!");
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(dto, pager);
		if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
			for(Map map : trans.getResult().getRows()){
				dto.setId(Long.parseLong(""+map.get("id")));
				dtoresult = translationExportService.modifyTranslationInfo(dto);
				if(!dtoresult.isSuccess()){
					resultMap.put("result", "确认失败!"+dtoresult.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "确认失败!"+trans.getErrorMessages());
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * <p>Discription:[买家接收报价]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitRequestTranslation(TranslationInfoDTO dto, String uid, String[] detailId) {
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "接收成功!");
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		//先将每条明细的status置为1，表明接收此条信息
		for(int i= 0 ; i< detailId.length; i++){
			TranslationMatDTO detail = new TranslationMatDTO();
			detail.setId(Long.parseLong(detailId[i]));
			detail.setStatus(1);
			dtoresult = translationExportService.modifyTranslationMat(detail);
			if(!dtoresult.isSuccess()){
				resultMap.put("result", "接收失败!"+dtoresult.getErrorMessages().get(0));
				return resultMap;
			}
		}
		Pager<TranslationInfoDTO> pager = new Pager<TranslationInfoDTO>();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(dto, pager);
		if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
			for(Map map : trans.getResult().getRows()){
				dto.setStatus("3");
				dto.setUpdateBy(uid);
				dto.setUpdateDate(new Date());
				dto.setId(Long.parseLong(""+map.get("id")));
				dtoresult = translationExportService.modifyTranslationInfo(dto);
				if(!dtoresult.isSuccess()){
					resultMap.put("result", "接收失败!"+dtoresult.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "接收失败!"+trans.getErrorMessages());
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * <p>Discription:[重新求购]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitTranslationRe(TranslationInfoDTO dto, String uid) {
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "操作成功!");
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(dto, pager);
		if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
			for(Map map : trans.getResult().getRows()){
				dto.setStatus("2");
				dto.setUpdateBy(uid);
				dto.setUpdateDate(new Date());
				dto.setId(Long.parseLong(""+map.get("id")));
				dtoresult = translationExportService.modifyTranslationInfo(dto);
				if(!dtoresult.isSuccess()){
					resultMap.put("result", "操作失败!"+dtoresult.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "操作失败!"+trans.getErrorMessages());
			return resultMap;
		}
		//将明细的status字段滞空
		TranslationMatDTO matDTO = new TranslationMatDTO();
		matDTO.setTranslationNo(dto.getTranslationNo());
		matDTO.setActiveFlag("1");
		ExecuteResult<DataGrid<Map>> mapResult = translationExportService.queryTranslationMatList(matDTO, pager);
		if(mapResult.isSuccess()){
			for(Map mapp : mapResult.getResult().getRows()){
				matDTO = new TranslationMatDTO();
				matDTO.setId(Long.parseLong(""+mapp.get("id")));
				matDTO.setStatus(null);
				ExecuteResult<String> resultStr = translationExportService.modifyTranslationMat(matDTO);
				if(!resultStr.isSuccess()){
					resultMap.put("result", "操作失败!"+resultStr.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "操作失败!"+mapResult.getErrorMessages().get(0));
			return resultMap;
		}
		return resultMap;
	}
}
