package com.camelot.mall.askItemInfoService.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.mall.askItemInfoService.AskItemInfoService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.searchcenter.service.SearchExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
@Service(value="askItemInfoService")
public class AskItemInfoServiceImpl implements AskItemInfoService {
	@Resource
	private TranslationExportService translationExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private SearchExportService searchExportService;
	@Resource
	private ItemExportService itemExportService;
	/**
	 * <p>Discription:[新增求购]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> addTranslationInfo(TranslationInfoDTO translationInfoDTO, String uid, String deleteId,
			String[] ids, String[] category_ids, String[] itemNames, String[] quantity, String[] flag, String[] matAttributes) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> result = new ExecuteResult<String>();
    	String message = "true";
		for(int i = 0 ; i < itemNames.length ; i++){
			//flag区分是新增还是修改的标识
	    	if("i".equals(flag[i])){
	    		translationInfoDTO.setMatCd(itemNames[i]);
	    		translationInfoDTO.setItemName(itemNames[i]);
	    		translationInfoDTO.setMatAttribute(matAttributes[i]);
	    		translationInfoDTO.setCreateBy(uid);
	    		translationInfoDTO.setCreateDate(new Date());
	    		translationInfoDTO.setQuantity(Integer.parseInt(quantity[i]));
				translationInfoDTO.setCategoryId(Integer.parseInt(category_ids[i]));
				result = translationExportService.addTranslationInfo(translationInfoDTO);
	    	}else{
	    		translationInfoDTO.setId(Long.parseLong(ids[i]));
	    		translationInfoDTO.setMatCd(itemNames[i]);
	    		translationInfoDTO.setItemName(itemNames[i]);
	    		translationInfoDTO.setMatAttribute(matAttributes[i]);
	    		translationInfoDTO.setUpdateBy(uid);
	    		translationInfoDTO.setUpdateDate(new Date());
	    		translationInfoDTO.setQuantity(Integer.parseInt(quantity[i]));
				translationInfoDTO.setCategoryId(Integer.parseInt(category_ids[i]));
	    		result = translationExportService.modifyTranslationInfo(translationInfoDTO);
	    	}
    		if(!result.isSuccess()){
    	    	message = "false";
    	    	resultMap.put("result", "保存失败!"+result.getErrorMessages().get(0));
    	    	return resultMap;
    		}
    	}
    	if("true".equals(message)){
	    	//获取需要删除的求购的id
    		String[] idArr = deleteId.split(",");
    		for(String id : idArr){
    			if(id != null && !"".equals(id)){
    				translationInfoDTO = new TranslationInfoDTO();
    				translationInfoDTO.setActiveFlag("0");
    				translationInfoDTO.setUpdateBy(uid);
    				translationInfoDTO.setUpdateDate(new Date());
    				translationInfoDTO.setId(Long.parseLong(id));
	    			//根据物品的id，删除掉相应的主求购单
	    			result = translationExportService.modifyTranslationInfo(translationInfoDTO);
	    			if(result.isSuccess()){
	    				message = "保存成功";
	    			}
    			}
    		}
    	}
    	resultMap.put("result", "保存成功!");
		return resultMap;
	}
	/**
	 * <p>Discription:[求购单删除]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> deleteTranslation(String uid, String ids, TranslationInfoDTO translationInfoDTO) {
		Map<String, String> resultMap = new HashMap<String, String>();
		String[] idList = ids.split(",");
		for(String id : idList){
			translationInfoDTO.setTranslationNo(id);
			Pager pager = new Pager();
			pager.setRows(Integer.MAX_VALUE);
			pager.setPage(1);
			//求购单主单查询(未去重)，当做明细查询出所有id后将active置为0
			ExecuteResult<DataGrid<Map>> translatioDTOList =  translationExportService.queryTranslationInfoPager(translationInfoDTO, pager);
			if(translatioDTOList.isSuccess()) {
				for(Map map : translatioDTOList.getResult().getRows()) {
					TranslationInfoDTO dto = new TranslationInfoDTO();
					dto.setId(Long.parseLong(""+map.get("id")));
					dto.setActiveFlag("0");
					dto.setUpdateBy(uid);
					dto.setUpdateDate(new Date());
					ExecuteResult<String> re = translationExportService.modifyTranslationInfo(dto);
					if (!re.isSuccess()) {
						resultMap.put("result", "删除失败!" + re.getResultMessage());
						return resultMap;
					}
				}
			}else{
				resultMap.put("result", "删除失败!" + translatioDTOList.getResultMessage());
				return resultMap;
			}
		}
		resultMap.put("result", "删除成功!");
		return resultMap;
	}

	/**
	 * <p>Discription:[求购提交，status字段置为1]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitTranslation(String uid, TranslationInfoDTO dto, String ids) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Pager pager= new Pager();
		pager.setRows(Integer.MAX_VALUE);
		String[] idList = ids.split(",");
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		for(String id : idList){
			dto.setStatus("1");
			dto.setActiveFlag("1");
			dto.setTranslationNo(id);
			dto.setUpdateBy("1");
			dto.setUpdateDate(new Date());
			TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
			translationInfoDTO.setTranslationNo(id);
			translationInfoDTO.setActiveFlag("1");
			ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(translationInfoDTO, pager);
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
				return resultMap;
			}
		}
		resultMap.put("result", "提交成功!");
		return resultMap;
	}

	/**
	 * <p>Discription:[暂未使用]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> auditTranslation(String uid, TranslationInfoDTO dto, String ids) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Pager pager= new Pager();
		pager.setRows(Integer.MAX_VALUE);
		String[] idList = ids.split(",");
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		for(String id : idList){
			dto.setStatus("2");
			dto.setActiveFlag("1");
			dto.setTranslationNo(id);
			dto.setUpdateBy("1");
			dto.setUpdateDate(new Date());
			TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
			translationInfoDTO.setTranslationNo(id);
			translationInfoDTO.setActiveFlag("1");
			ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(translationInfoDTO, pager);
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
			}
		}
		resultMap.put("result", "审核成功!");
		return resultMap;
	}

	/**
	 * <p>Discription:[卖家保存报价信息，生成求购明细]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> saveRepTranslationInfo(String uid, TranslationInfoDTO translationInfoDTO, String[] id,
			String[] price, String[] addFlag, String[] matDesc, String[] quantity, String[] detailstartDate, String[] detailendDate, String[] matAttributes,String[] category_ids) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> result = new ExecuteResult<String>();
    	ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
    	//将主单的状态改为3，以便前台确认价格时，3以后的菜可以直接确认价格
		translationInfoDTO.setActiveFlag("1");
		UserDTO userDTO = userExportService.queryUserById(Long.parseLong(uid));
		translationInfoDTO.setStatus("3");
		for(int i= 0; i< addFlag.length; i++){
			if("i".equals(addFlag[i])){
				//add detail
				TranslationMatDTO detailDto = new TranslationMatDTO();
				detailDto.setTranslationNo(translationInfoDTO.getTranslationNo());
				detailDto.setMatDesc(matDesc[i]);
				detailDto.setQuantity(Integer.parseInt(quantity[i]));
				detailDto.setCreateBy(uid);
				detailDto.setActiveFlag("1");
				detailDto.setMatAttribute(matAttributes[i]);
				detailDto.setCreateDate(new Date());
				if(price.length > 0 && price[i] != null && !"".equals(price[i]) && !"null".equals(price[i])) {
					detailDto.setMatPrice(Double.parseDouble(price[i]));
					try {
						detailDto.setBeginDate(sim.parse(detailstartDate[i]));
						detailDto.setEndDate(sim.parse(detailendDate[i]));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				detailDto.setShopId(userDTO.getShopId().intValue());
				//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
				detailDto.setCategoryId(Integer.parseInt(category_ids[i]));
				ExecuteResult<String> resultDet = translationExportService.addTranslationMat(detailDto);
				if(!resultDet.isSuccess()){
					resultMap.put("result", "保存失败!"+resultDet.getResultMessage());
					return resultMap;
				}
			}else{
				
				TranslationMatDTO detailDto = new TranslationMatDTO();
				detailDto.setId(Long.parseLong(id[i]));
				detailDto.setUpdateBy(uid);
				detailDto.setQuantity(Integer.parseInt(quantity[i]));
				detailDto.setUpdateDate(new Date());
				if(price.length >0 && price[i] != null && !"".equals(price[i]) && !"null".equals(price[i])) {
					detailDto.setMatPrice(Double.parseDouble(price[i]));
					try {
						detailDto.setBeginDate(sim.parse(detailstartDate[i]));
						detailDto.setEndDate(sim.parse(detailendDate[i]));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ExecuteResult<String> resultDet = translationExportService.modifyTranslationMat(detailDto);
				if(!resultDet.isSuccess()){
					resultMap.put("result", "更新失败!"+resultDet.getResultMessage());
					return resultMap;
				}
				
			}
		}
		
		/*Pager<TranslationInfoDTO> pager = new Pager<TranslationInfoDTO>();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(translationInfoDTO, pager);
		if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
			for(Map map : trans.getResult().getRows()){
				translationInfoDTO.setId(Long.parseLong(""+map.get("id")));
				dtoresult = translationExportService.modifyTranslationInfo(translationInfoDTO);
				if(!dtoresult.isSuccess()){
					resultMap.put("result", "主单状态更新失败!"+dtoresult.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}else{
			resultMap.put("result", "主单状态更新失败!"+trans.getErrorMessages());
			return resultMap;
		}*/
		
    	resultMap.put("result", "操作成功!");
		return resultMap;
	}

	/**
	 * <p>Discription:[卖家确认求购信息，暂未使用]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitRepTranslationInfo(String uid, TranslationInfoDTO dto, String ids) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> result = new ExecuteResult<String>();
		resultMap.put("result", "报价成功!");
		String[] idList = ids.split(",");
		for(String id : idList){
			ExecuteResult<String> dtoresult = new ExecuteResult<String>();
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			dto.setTranslationNo(id);
			dto.setStatus("4");
			ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(dto, pager);
			if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
				for(Map map : trans.getResult().getRows()){
					dto.setId(Long.parseLong(""+map.get("id")));
					dtoresult = translationExportService.modifyTranslationInfo(dto);
					if(!dtoresult.isSuccess()){
						resultMap.put("result", "报价失败!"+dtoresult.getErrorMessages().get(0));
						return resultMap;
					}
				}
			}else{
				resultMap.put("result", "报价失败!"+trans.getErrorMessages());
				return resultMap;
			}
		}
		return resultMap;
	}

	/**
	 * <p>Discription:[重新求购，status字段置为2]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitTranslationRe(String uid, TranslationInfoDTO dto, String ids) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		String[] idList = ids.split(",");
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		for(String id : idList){
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			dto.setTranslationNo(id);
			dto.setStatus("2");
			ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(dto, pager);
			if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
				for(Map map : trans.getResult().getRows()){
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
			matDTO.setTranslationNo(id);
			matDTO.setActiveFlag("1");
			ExecuteResult<DataGrid<Map>> mapResult = translationExportService.queryTranslationMatList(matDTO, pager);
			if(mapResult.isSuccess()){
				for(Map mapp : mapResult.getResult().getRows()){
					TranslationMatDTO matDto = new TranslationMatDTO();
					matDto.setId(Long.parseLong(""+mapp.get("id")));
					matDto.setStatus(0);
					ExecuteResult<String> resultStr = translationExportService.modifyTranslationMat(matDto);
					if(!resultStr.isSuccess()){
						resultMap.put("result", "操作失败!"+resultStr.getErrorMessages().get(0));
						return resultMap;
					}
				}
			}else{
				resultMap.put("result", "操作失败!"+mapResult.getErrorMessages().get(0));
				return resultMap;
			}

		}
		resultMap.put("result", "操作成功!");
		return resultMap;
	}

	/**
	 * <p>Discription:[买家接收求购，主单status置为3，相应明细表的status置为1]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> acceptTranslationInfo(String uid, TranslationInfoDTO dto, String id ,String detailId) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		//将明细的status字段更新为1
		String[] detail = detailId.split(",");
		for(String detId : detail){
			TranslationMatDTO matDTO = new TranslationMatDTO();
			matDTO.setId(Long.parseLong(detId));
			matDTO.setStatus(1);
			matDTO.setUpdateBy(uid);
			matDTO.setUpdateDate(new Date());
			dtoresult = translationExportService.modifyTranslationMat(matDTO);
			if(!dtoresult.isSuccess()){
				resultMap.put("result", "接收失败!"+dtoresult.getErrorMessages().get(0));
				return resultMap;
			}
		}
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		dto.setTranslationNo(id);
		dto.setStatus("3");
		ExecuteResult<DataGrid<Map>> trans =translationExportService.queryTranslationInfoPager(dto, pager);
		if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
			for(Map map : trans.getResult().getRows()){
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
		resultMap.put("result", "接收成功!");
		return resultMap;
	}
}
