package com.camelot.mall.requestPriceService.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.goodscenter.dto.InquiryMatDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.requestPriceService.RequestPriceService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.searchcenter.service.SearchExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.service.UserExportService;

@Service(value="requestPriceServiceImpl")
public class RequestPriceServiceImpl implements RequestPriceService{
	@Resource
	private InquiryExportService inquiryExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private SearchExportService searchExportService;
	@Resource
	private ItemExportService itemExportService;
	


	

	/**
	 * <p>Discription:[新增询价]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> addInquiry(InquiryInfoDTO inquiryInfoDTO , String uid, String deleteId,  String[] detailId,String[] shopId, String[] nums , String[] flag,String[] deliveryDates) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> result = new ExecuteResult<String>();
    	ExecuteResult<String> resultDetail = new ExecuteResult<String>();
    	String message = "true";
		for(int i = 0 ; i < detailId.length ; i++){
	    	if(flag.length  == 0 || flag[i] == null || "".equals(flag[i])){
	    		inquiryInfoDTO.setSupplierId(Integer.parseInt(shopId[i]));
	    		inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
				if(deliveryDates != null && !"".equals(deliveryDates[i])) {
					SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date deliveryDate2 = sm.parse(deliveryDates[i]);
						inquiryInfoDTO.setDeliveryDate2(deliveryDate2);
					} catch (ParseException e) {
						resultMap.put("result", "保存失败！时间转换失败");
						return resultMap;
					}
				}
				inquiryInfoDTO.setMatCd(detailId[i]);
	    		result = inquiryExportService.addInquiryInfo(inquiryInfoDTO);
	    	}else{
	    		inquiryInfoDTO.setId(Long.parseLong(detailId[i]));
	    		inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
	    		inquiryInfoDTO.setActiveFlag("1");
	    		inquiryInfoDTO.setUpdateBy(uid);
				if(deliveryDates != null && !"".equals(deliveryDates[i])) {
					SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date deliveryDate2 = sm.parse(deliveryDates[i]);
						inquiryInfoDTO.setDeliveryDate2(deliveryDate2);
					} catch (ParseException e) {
						resultMap.put("result", "保存失败！时间转换失败");
						return resultMap;
					}
				}
	    		inquiryInfoDTO.setUpdateDate(new Date());
				inquiryInfoDTO.setMatCd(null);
	    		//根据id修改主单。明细信息
	    		result = inquiryExportService.modifyInquiryInfoById(inquiryInfoDTO);
	    	}
    		if(result.isSuccess()){
    			//主单保存成功才保存明细信息
    	    	message = "true";
    		} else {
    			message = "false";
    			resultMap.put("result", "保存失败！"+result.getErrorMessages().get(0));
    			return resultMap;
    		}
    	}
    	if("true".equals(message)){
	    	//获取需要删除的询价的id
    		String[] ids = deleteId.split(",");
    		for(String id : ids){
    			if(id != null && !"".equals(id)){
	    			inquiryInfoDTO = new InquiryInfoDTO();
	    			inquiryInfoDTO.setActiveFlag("0");
	    			inquiryInfoDTO.setId(Long.parseLong(id));
	    			inquiryInfoDTO.setUpdateBy(uid);
	    			inquiryInfoDTO.setUpdateDate(new Date());
	    			//根据物品的id，删除掉相应的主询价单
	    			result = inquiryExportService.modifyInquiryInfoById(inquiryInfoDTO);
	    			if(result.isSuccess()){
	    				message = "true";
	    			}else{
	    				resultMap.put("result", "保存失败！"+result.getErrorMessages().get(0));
	        			return resultMap;
	    			}
    			}
    		}
      
    	}

    	resultMap.put("result", "保存成功！");
    	
    	
    	
		return resultMap;
	}
	
	
	
	/**
	 * <p>Discription:[新增询价]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> addInquiryNew(InquiryInfoDTO inquiryInfoDTO , String uid, String deleteId,  String[] detailId,String[] shopId, String[] nums , String[] flag,String[] deliveryDates,String[] skuId) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> result = new ExecuteResult<String>();
    	ExecuteResult<String> resultDetail = new ExecuteResult<String>();
    	String message = "true";
		for(int i = 0 ; i < detailId.length ; i++){
	    	if(flag.length  == 0 || flag[i] == null || "".equals(flag[i])){
	    		inquiryInfoDTO.setSupplierId(Integer.parseInt(shopId[i]));
	    		inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
				if(deliveryDates != null && !"".equals(deliveryDates[i])) {
					SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date deliveryDate2 = sm.parse(deliveryDates[i]);
						inquiryInfoDTO.setDeliveryDate2(deliveryDate2);
					} catch (ParseException e) {
						resultMap.put("result", "保存失败！时间转换失败");
						return resultMap;
					}
				}
				inquiryInfoDTO.setSkuId(Integer.parseInt(skuId[i]));
				inquiryInfoDTO.setMatCd(detailId[i]);
	    		result = inquiryExportService.addInquiryInfo(inquiryInfoDTO);
	    	}else{
	    		inquiryInfoDTO.setId(Long.parseLong(detailId[i]));
	    		inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
	    		inquiryInfoDTO.setActiveFlag("1");
	    		inquiryInfoDTO.setUpdateBy(uid);
				if(deliveryDates != null && !"".equals(deliveryDates[i])) {
					SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date deliveryDate2 = sm.parse(deliveryDates[i]);
						inquiryInfoDTO.setDeliveryDate2(deliveryDate2);
					} catch (ParseException e) {
						resultMap.put("result", "保存失败！时间转换失败");
						return resultMap;
					}
				}
	    		inquiryInfoDTO.setUpdateDate(new Date());
				inquiryInfoDTO.setMatCd(null);
	    		//根据id修改主单。明细信息
	    		result = inquiryExportService.modifyInquiryInfoById(inquiryInfoDTO);
	    	}
    		if(result.isSuccess()){
    			//主单保存成功才保存明细信息
    	    	message = "true";
    		} else {
    			message = "false";
    			resultMap.put("result", "保存失败！"+result.getErrorMessages().get(0));
    			return resultMap;
    		}
    	}
    	if("true".equals(message)){
	    	//获取需要删除的询价的id
    		String[] ids = deleteId.split(",");
    		for(String id : ids){
    			if(id != null && !"".equals(id)){
	    			inquiryInfoDTO = new InquiryInfoDTO();
	    			inquiryInfoDTO.setActiveFlag("0");
	    			inquiryInfoDTO.setId(Long.parseLong(id));
	    			inquiryInfoDTO.setUpdateBy(uid);
	    			inquiryInfoDTO.setUpdateDate(new Date());
	    			//根据物品的id，删除掉相应的主询价单
	    			result = inquiryExportService.modifyInquiryInfoById(inquiryInfoDTO);
	    			if(result.isSuccess()){
	    				message = "true";
	    			}else{
	    				resultMap.put("result", "保存失败！"+result.getErrorMessages().get(0));
	        			return resultMap;
	    			}
    			}
    		}
      
    	}
    	resultMap.put("result", "保存成功！");
		return resultMap;
	}


	/**
	 * <p>Discription:[删除询价，将active字段置为0]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> deleteInquiry(String uid, String ids) {
		Map<String, String> resultMap = new HashMap<String, String>();
		InquiryInfoDTO dto = new InquiryInfoDTO();
		InquiryMatDTO dtoDetail = new InquiryMatDTO();
		String[] idList = ids.split(",");
		for(String id : idList){
			dto.setInquiryNo(id);
			dto.setActiveFlag("0");
			ExecuteResult<String>  result = inquiryExportService.modifyInquiryInfo(dto);
			if(!result.isSuccess()){
				resultMap.put("result", "删除失败!"+result.getErrorMessages().get(0));
				return resultMap;
			}
		}
		resultMap.put("result", "删除成功！");
		return resultMap;
	}

	/**
	 * <p>Discription:[提交询价，status字段置为2]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitInquiry(String ids, String uid, InquiryInfoDTO dto) {
		Map<String, String> resultMap = new HashMap<String, String>();
		String[] idList = ids.split(",");
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		Pager pager= new Pager();
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		for(String id : idList){
			dto.setStatus("2");
			dto.setActiveFlag("1");
			dto.setInquiryNo(id);
			dto.setUpdateBy(uid);
			dto.setUpdateDate(new Date());
			//根据编号查询其下的物品id
			ExecuteResult<DataGrid<Map>>  result = inquiryExportService.queryInquiryInfoPager(dto, pager);
			if(result.isSuccess() && result.getResult().getTotal() > 0){
				//根据物品id获取平台下有此物品的店铺的id
				for(Map map : result.getResult().getRows()){
					String itemId = ""+map.get("matCd");//物品id
//					//调用dubbo获取itemId,查询sku
//					ItemQueryInDTO itemDto = new ItemQueryInDTO();
//					itemDto.setItemId(Integer.parseInt(itemId));
//					//将主单中的shops放入list，查询物品的信息
//					Long shopStr = Long.parseLong(""+map.get("supplierId"));
//					Long[] shopL = new Long[]{shopStr};
//					itemDto.setShopIds(shopL);
//					DataGrid<ItemQueryOutDTO> itemList = itemExportService.queryItemList(itemDto, pager);
//					if( itemList.getTotal() > 0){
//					for(ItemQueryOutDTO itemQueryOutDTO : itemList.getRows()){
//					ExecuteResult<ItemDTO> itemDtos =  itemExportService.getItemById(itemQueryOutDTO.getItemId());
//					if(itemDtos.isSuccess()  && itemDtos.getResult().getSkuInfos().size() > 0){
//					for(SkuInfo skuInfo : itemDtos.getResult().getSkuInfos()){
					InquiryMatDTO detailDto = new InquiryMatDTO();
					detailDto.setActiveFlag("1");
					detailDto.setInquiryNo(id);
					detailDto.setMatCd("" + map.get("skuId"));
					detailDto.setQuantity(Integer.parseInt("" + map.get("quantity")));
					detailDto.setMatDesc("" + map.get("matDesc"));
					//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
					if(map.get("deliveryDate2") != null && !"".equals(map.get("deliveryDate2"))) {
						SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
						try {
							Date deliveryDate2 = sm.parse("" + map.get("deliveryDate2"));
							detailDto.setDeliveryDate2(deliveryDate2);
						} catch (ParseException e) {
							resultMap.put("result", "询价失败！时间转换失败");
							return resultMap;
						}
					}
					detailDto.setCreateBy(uid);
					detailDto.setCreateDate(new Date());
					ExecuteResult<ItemDTO> itemDtos =  itemExportService.getItemById(Long.valueOf(itemId));
					detailDto.setSupplierId(itemDtos.getResult().getSellerId().intValue());
					detailDto.setShopId(itemDtos.getResult().getShopId().intValue());
					//生成明细信息
					resultDetail = inquiryExportService.addInquiryMat(detailDto);
					if(!resultDetail.isSuccess()){
						resultMap.put("result", "询价失败!"+resultDetail.getErrorMessages().get(0));
						return resultMap;
					}
//					}
//					}else{
//						resultMap.put("result", "询价失败,根据没有物品没有找到相关店铺信息!");
//						return resultMap;
//					}
					dto.setSupplierId(null);
					dtoresult = inquiryExportService.modifyInquiryInfo(dto);
					if(!dtoresult.isSuccess()){
						resultMap.put("result", "询价失败!"+dtoresult.getErrorMessages().get(0));
						return resultMap;
					}
//					}
//					}else{
//						resultMap.put("result", "询价失败,没有找到相关物品信息!");
//						return resultMap;
//					}
				}
			}else{
				resultMap.put("result", "询价失败!"+result.getErrorMessages().get(0));
				return resultMap;
			}
		}
		resultMap.put("result", "发布成功!");
		return resultMap;
	}

	/**
	 * <p>Discription:[卖家保存询价单信息]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> resopnseModifyInquiry(InquiryInfoDTO inquiryInfoDTO, String uid, 
			String[] id, String[] nums, String[] price, String[] detailstartDate , String[] detailendDate ) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> result = new ExecuteResult<String>();
    	ExecuteResult<String> resultDetail = new ExecuteResult<String>();
//    	inquiryInfoDTO.setUpdateBy(uid);
//    	inquiryInfoDTO.setUpdateDate(new Date());
//    	inquiryInfoDTO.setActiveFlag("1");
//		result = inquiryExportService.modifyInquiryInfo(inquiryInfoDTO);
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
//    	if(result.isSuccess()){
		for(int i = 0 ; i < id.length ; i++){
				//主单保存成功才保存明细信息
				InquiryMatDTO inquiryMatDTO = new InquiryMatDTO();
				inquiryMatDTO.setInquiryNo(inquiryInfoDTO.getInquiryNo());
				inquiryMatDTO.setQuantity(Integer.parseInt(nums[i]));
				if(price.length > 0 && price[i] != null && !"".equals(price[i]) && !"null".equals(price[i])) {
					inquiryMatDTO.setMatPrice(Double.parseDouble(price[i]));
					try {
						inquiryMatDTO.setBeginDate(sim.parse(detailstartDate[i]));
						inquiryMatDTO.setEndDate(sim.parse(detailendDate[i]));
					} catch (ParseException e) {
						resultMap.put("result", "保存失败！时间转换失败");
						return resultMap;
					}
				}
				inquiryMatDTO.setId(Long.parseLong(id[i]));
				resultDetail = inquiryExportService.modifyInquiryMat(inquiryMatDTO);
				if(!resultDetail.isSuccess()){
					resultMap.put("result", "保存失败！"+resultDetail.getErrorMessages().get(0));
					return resultMap;
				}
		}
//    	}
    	resultMap.put("result", "保存成功！");
    	return resultMap;
	}

	/**
	 * <p>Discription:[卖家确认报价信息，停用]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> responseCommitInquiry(InquiryInfoDTO dto, String uid, String ids) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> result = new ExecuteResult<String>();
		String[] idList = ids.split(",");
		for(String id : idList){
			dto.setStatus("4");
			dto.setActiveFlag("1");
			dto.setInquiryNo(id);
			dto.setUpdateBy(uid);
			dto.setUpdateDate(new Date());
			result = inquiryExportService.modifyInquiryInfo(dto);
			if(!result.isSuccess()){
				resultMap.put("result", "提交失败!"+result.getErrorMessages().get(0));
				return resultMap;
			}
		}
		resultMap.put("result", "提交成功!");
		return resultMap;
	}

	/**
	 * <p>Discription:[重新询价，将status字段置为2，明细表的status字段置空]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitInquiryRe(InquiryInfoDTO dto, String uid, String ids) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		String[] idList = ids.split(",");
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		for(String id : idList){
			dto.setStatus("2");
			dto.setActiveFlag("1");
			dto.setInquiryNo(id);
			dto.setUpdateBy(uid);
			dto.setUpdateDate(new Date());
			resultDetail = inquiryExportService.modifyInquiryInfo(dto);
			if(!resultDetail.isSuccess()){
				resultMap.put("result", "操作失败!"+resultDetail.getErrorMessages().get(0));
				return resultMap;
			}
			//将明细的status字段滞空
			InquiryMatDTO matDTO = new InquiryMatDTO();
			matDTO.setInquiryNo(dto.getInquiryNo());
			matDTO.setActiveFlag("1");
			Pager<InquiryMatDTO> pager = new Pager<InquiryMatDTO>();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			ExecuteResult<DataGrid<Map>> mapResult = inquiryExportService.queryInquiryMatList(matDTO, pager);
			if(mapResult.isSuccess()){
				for(Map mapp : mapResult.getResult().getRows()){
					InquiryMatDTO matDto = new InquiryMatDTO();
					matDto.setId(Long.parseLong(""+mapp.get("id")));
					matDto.setStatus("0");
					ExecuteResult<String> resultStr = inquiryExportService.modifyInquiryMat(matDto);
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
	 * <p>Discription:[接收询价报价信息，主单status字段置为3，相应明细表的status字段置为1]</p>
	 * Created on 2015年7月20日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitInquiryYes(InquiryInfoDTO dto, String uid, String inquiryNo ,String detailId) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		//将明细的status字段更新为1
		String[] detail = detailId.split(",");
		for(String detId : detail){
			InquiryMatDTO matDTO = new InquiryMatDTO();
			matDTO.setId(Long.parseLong(detId));
			matDTO.setStatus("1");
			matDTO.setUpdateBy(uid);
			matDTO.setUpdateDate(new Date());
			dtoresult = inquiryExportService.modifyInquiryMat(matDTO);
			if(!dtoresult.isSuccess()){
				resultMap.put("result", "接收失败!"+dtoresult.getErrorMessages().get(0));
				return resultMap;
			}
		}
		dto.setStatus("3");
		dto.setActiveFlag("1");
		dto.setInquiryNo(inquiryNo);
		dto.setUpdateBy(uid);
		dto.setUpdateDate(new Date());
		resultDetail = inquiryExportService.modifyInquiryInfo(dto);
		if(!resultDetail.isSuccess()){
			resultMap.put("result", "接收失败!"+resultDetail.getErrorMessages().get(0));
			return resultMap;
		}
		resultMap.put("result", "接收成功!");
		return resultMap;
	}

	/**
	 * <p>Discription:[立即发布，主单status字段置为2]</p>
	 * Created on 2015年11月2日11:53:49
	 * @return
	 * @author:[chengwt]
	 */
	
	 
	
	@Override
	
	public Map<String, String> upimmediately(InquiryInfoDTO inquiryInfoDTO , String uid, String deleteId,  String[] detailId,String[] shopId, String[] nums , String[] flag,String[] deliveryDates) {
		Map<String, String> resultMap = new HashMap<String, String>();
		ExecuteResult<String> result = new ExecuteResult<String>();
    	ExecuteResult<String> resultDetail = new ExecuteResult<String>();
    	String message = "true";
		for(int i = 0 ; i < detailId.length ; i++){
	    	if(flag.length  == 0 || flag[i] == null || "".equals(flag[i])){
	    		inquiryInfoDTO.setSupplierId(Integer.parseInt(shopId[i]));
	    		inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
				if(deliveryDates != null && !"".equals(deliveryDates[i])) {
					SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date deliveryDate2 = sm.parse(deliveryDates[i]);
						inquiryInfoDTO.setDeliveryDate2(deliveryDate2);
					} catch (ParseException e) {
						resultMap.put("result", "发布失败！时间转换失败");
						return resultMap;
					}
				}
				inquiryInfoDTO.setMatCd(detailId[i]);
	    		result = inquiryExportService.addInquiryInfo(inquiryInfoDTO);
	    	}else{
	    		inquiryInfoDTO.setId(Long.parseLong(detailId[i]));
	    		inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
	    		inquiryInfoDTO.setActiveFlag("1");
	    		inquiryInfoDTO.setUpdateBy(uid);
				if(deliveryDates != null && !"".equals(deliveryDates[i])) {
					SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date deliveryDate2 = sm.parse(deliveryDates[i]);
						inquiryInfoDTO.setDeliveryDate2(deliveryDate2);
					} catch (ParseException e) {
						resultMap.put("result", "发布失败！时间转换失败");
						return resultMap;
					}
				}
	    		inquiryInfoDTO.setUpdateDate(new Date());
				inquiryInfoDTO.setMatCd(null);
	    		//根据id修改主单。明细信息
	    		result = inquiryExportService.modifyInquiryInfoById(inquiryInfoDTO);
	    	}
    		if(result.isSuccess()){
    			//主单保存成功才保存明细信息
    	    	message = "true";
    		} else {
    			message = "false";
    			resultMap.put("result", "发布失败！"+result.getErrorMessages().get(0));
    			return resultMap;
    		}
    	}
    	if("true".equals(message)){
	    	//获取需要删除的询价的id
    		String[] ids = deleteId.split(",");
    		for(String id : ids){
    			if(id != null && !"".equals(id)){
	    			inquiryInfoDTO = new InquiryInfoDTO();
	    			inquiryInfoDTO.setActiveFlag("0");
	    			inquiryInfoDTO.setId(Long.parseLong(id));
	    			inquiryInfoDTO.setUpdateBy(uid);
	    			inquiryInfoDTO.setUpdateDate(new Date());
	    			//根据物品的id，删除掉相应的主询价单
	    			result = inquiryExportService.modifyInquiryInfoById(inquiryInfoDTO);
	    			if(result.isSuccess()){
	    				message = "true";
	    			}else{
	    				resultMap.put("result", "发布失败！"+result.getErrorMessages().get(0));
	        			return resultMap;
	    			}
    			}
    		}
    	}
    	resultMap.put("result", "发布成功！");
		return resultMap;
	}


}
