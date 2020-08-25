package com.camelot.mall.requestPriceWX.impl;

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
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.requestPriceWX.RequestPriceWXService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.service.UserExportService;

@Service(value="requestPriceWXService")
public class RequestPriceWXServiceImpl implements RequestPriceWXService {
	@Resource
	private InquiryExportService inquiryService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private InquiryExportService inquiryExportService;
	@Resource
	private ItemExportService itemExportService;
	
	
	/**
	 * <p>Discription:[询价新增]</p>
	 * Created on 2015年7月16日
	 * @param inquiryInfoDTO
	 * @param itemIds
	 * @param nums
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String,String> addInquiry(InquiryInfoDTO inquiryInfoDTO,String[] shopId, String[] itemIds, String[] nums, String[] deliveryDates) {
		ExecuteResult<String> result = new ExecuteResult<String>();
    	Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "保存成功!");
    	for(int i=0; i< itemIds.length ; i++){
    		inquiryInfoDTO.setMatCd(itemIds[i]);
    		//将供应商id的字符串存入
    		inquiryInfoDTO.setSupplierId(Integer.parseInt(shopId[i]));
    		inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
			//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			try {
				inquiryInfoDTO.setDeliveryDate2(simpleDateFormat.parse(deliveryDates[i]));
			} catch (ParseException e) {
				resultMap.put("result", "保存失败!交货时间转换失败！");
				return resultMap;
			}
			result = inquiryService.addInquiryInfo(inquiryInfoDTO);
			if(!result.isSuccess()){
				resultMap.put("result", "保存失败!"+result.getErrorMessages().get(0));
			}
    	}
		return resultMap;
	}
	
	
	/**
	 * <p>Discription:[询价新增李伟龙修改]</p>
	 * Created on 2015年11月11日
	 * @param inquiryInfoDTO
	 * @param itemIds
	 * @param nums
	 * @return
	 * @author:[liwl]
	 */
	@Override
	public Map<String,String> addInquiryNew(InquiryInfoDTO inquiryInfoDTO,String[] shopId, String[] itemIds, String[] nums, String[] deliveryDates,String[] skuIds) {
		ExecuteResult<String> result = new ExecuteResult<String>();
    	Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "保存成功!");
    	for(int i=0; i< itemIds.length ; i++){
    		inquiryInfoDTO.setMatCd(itemIds[i]);
    		//将供应商id的字符串存入
    		inquiryInfoDTO.setSupplierId(Integer.parseInt(shopId[i]));
    		inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
    		//存入skuid
    		inquiryInfoDTO.setSkuId(Integer.parseInt(skuIds[i]));
			//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			try {
				inquiryInfoDTO.setDeliveryDate2(simpleDateFormat.parse(deliveryDates[i]));
			} catch (ParseException e) {
				resultMap.put("result", "保存失败!交货时间转换失败！");
				return resultMap;
			}
			result = inquiryService.addInquiryInfo(inquiryInfoDTO);
			if(!result.isSuccess()){
				resultMap.put("result", "保存失败!"+result.getErrorMessages().get(0));
			}
    	}
		return resultMap;
	}


	/**
	 * <p>Discription:[询价提交，询价单status字段置为2]</p>
	 * Created on 2015年7月16日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitInquiry(InquiryInfoDTO dto, Pager pager, String uid) {
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "发布成功!");
		//根据编号查询其下的物品id
		ExecuteResult<DataGrid<Map>>  result = inquiryExportService.queryInquiryInfoPager(dto, pager);
		ExecuteResult<String> dtoresult = new ExecuteResult<String>();
		ExecuteResult<String> resultDetail = new ExecuteResult<String>();
		if(result.isSuccess() && result.getResult().getTotal() > 0){
			//根据物品id获取平台下有此物品的店铺的id
			for(Map map : result.getResult().getRows()){
				String itemId = ""+map.get("matCd");//物品id
				
				//李伟龙注释掉，因为可以直接获取到物品id
//				//调用dubbo获取店铺的id的list
//				ItemQueryInDTO itemDto = new ItemQueryInDTO();
//				itemDto.setPlatItemId(Long.parseLong(itemId));
//				//将主单中的shops放入list，查询物品的信息
//				Long[] shopL = new Long[]{Long.parseLong(""+map.get("supplierId"))};
//				itemDto.setShopIds(shopL);
//				DataGrid<ItemQueryOutDTO> itemList = itemExportService.queryItemList(itemDto, pager);
//				if( itemList.getTotal() > 0){
//					for(ItemQueryOutDTO itemQueryOutDTO : itemList.getRows()){
//						//此段代码原来为下面提出代码
//					}
//				}else{
//					 resultMap.put("result", "发布失败，没有找到物品信息!");
//					 return resultMap;
//				}
				//提出代码 start
				ExecuteResult<ItemDTO> itemDtos =  itemExportService.getItemById(Long.valueOf(itemId));
				if(itemDtos.isSuccess() && itemDtos.getResult().getSkuInfos().size() > 0){
					//因为改成根据sku询价，所以不需要获取skuid，注释掉
//					for(SkuInfo skuInfo : itemDtos.getResult().getSkuInfos()){
					InquiryMatDTO detailDto = new InquiryMatDTO();
					detailDto.setActiveFlag("1");
					detailDto.setInquiryNo(dto.getInquiryNo());
					detailDto.setQuantity(Integer.parseInt("" + map.get("quantity")));
					detailDto.setMatCd("" + map.get("skuId"));
					detailDto.setCreateBy(uid);
					detailDto.setCreateDate(new Date());
					detailDto.setSupplierId(itemDtos.getResult().getSellerId().intValue());
					detailDto.setShopId(itemDtos.getResult().getShopId().intValue());
					//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
					detailDto.setDeliveryDate2( (Date)(map.get("deliveryDate2")));
					//生成明细信息
					resultDetail = inquiryExportService.addInquiryMat(detailDto);
					if(!resultDetail.isSuccess()){
						resultMap.put("result", "发布失败!"+resultDetail.getErrorMessages().get(0));
						return resultMap;
					}
				
//					}
				}else{
					//查询店铺信息失败
					resultMap.put("result", "发布失败，根据物品没有找到店铺信息!");
					 return resultMap;
				}
				dto.setMatCd(itemId);
				dto.setSupplierId(null);
				dtoresult = inquiryExportService.modifyInquiryInfo(dto);
				if(!dtoresult.isSuccess()){
					resultMap.put("result",  "发布失败!"+dtoresult.getErrorMessages().get(0));
				}
				//提出代码 end
			}
		}else{
			resultMap.put("result", "发布失败!询价单信息查询失败！");
			return resultMap;
		}
		
		
		return resultMap;
	}


	/**
	 * <p>Discription:[询价更新，更新的信息都是询价主单的，此时还未生成询价单明细]</p>
	 * Created on 2015年7月16日
	 * @param inquiryInfoDTO
	 * @param deleteId
	 * @param itemIds
	 * @param nums
	 * @param statusDetail
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> updateInquiry(String uid, InquiryInfoDTO inquiryInfoDTO, String[] deleteId, String[] detailId, String[] itemIds, String[] shopId,
			String[] nums, String[] statusDetail, String[] deliveryDates) {
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "更新成功!");
    	ExecuteResult<String> result = new ExecuteResult<String>();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    	for(int i= 0; i< statusDetail.length ; i++){
    		if("i".equals(statusDetail[i])){
    			inquiryInfoDTO.setActiveFlag("1");
    	    	inquiryInfoDTO.setCreateBy(uid);
    	    	inquiryInfoDTO.setCreateDate(new Date());
    	    	inquiryInfoDTO.setStatus("1");
    	    	inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
	    		inquiryInfoDTO.setMatCd(itemIds[i]);
				//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释

				try {
					inquiryInfoDTO.setDeliveryDate2(simpleDateFormat.parse(deliveryDates[i]));
				} catch (ParseException e) {

				}
				inquiryInfoDTO.setSupplierId(Integer.parseInt(shopId[i]));
				result = inquiryService.addInquiryInfo(inquiryInfoDTO);
				if(!result.isSuccess()){
					resultMap.put("result","更新失败!"+result.getErrorMessages().get(0));
					return resultMap;
				}
    		}else if("o".equals(statusDetail[i])){
    			inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
    			inquiryInfoDTO.setId(Long.parseLong(detailId[i]));
    			inquiryInfoDTO.setActiveFlag("1");
    			inquiryInfoDTO.setUpdateBy(uid);
				//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
				try {
					inquiryInfoDTO.setDeliveryDate2(simpleDateFormat.parse(deliveryDates[i]));
				}catch (ParseException e) {}
	    		inquiryInfoDTO.setUpdateDate(new Date());
	    		result = inquiryService.modifyInquiryInfoById(inquiryInfoDTO);
				if(!result.isSuccess()){
					resultMap.put("result","更新失败!"+result.getErrorMessages().get(0));
					return resultMap;
				}
    		}
    	}
    	String inquiryNo = inquiryInfoDTO.getInquiryNo();
    	//物品删除的，将询价单据删除
    	if(deleteId.length > 0){
	    	for(String id : deleteId){
	    		if(!"".equals(id)){
		    		inquiryInfoDTO = new InquiryInfoDTO(); 
		    		inquiryInfoDTO.setId(Long.parseLong(id));
		    		inquiryInfoDTO.setActiveFlag("0");
		    		inquiryInfoDTO.setUpdateBy(uid);
		    		inquiryInfoDTO.setUpdateDate(new Date());
					result = inquiryService.modifyInquiryInfoById(inquiryInfoDTO);
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
	@Override
	public Map<String, String> updateInquiryNew(String uid, InquiryInfoDTO inquiryInfoDTO, String[] deleteId, String[] detailId, String[] itemIds, String[] shopId,
			String[] nums, String[] statusDetail, String[] deliveryDates, String[] skuId) {
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "更新成功!");
    	ExecuteResult<String> result = new ExecuteResult<String>();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    	for(int i= 0; i< statusDetail.length ; i++){
    		if("i".equals(statusDetail[i])){
    			inquiryInfoDTO.setActiveFlag("1");
    	    	inquiryInfoDTO.setCreateBy(uid);
    	    	inquiryInfoDTO.setCreateDate(new Date());
    	    	inquiryInfoDTO.setStatus("1");
    	    	inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
	    		inquiryInfoDTO.setMatCd(itemIds[i]);
	    		inquiryInfoDTO.setSkuId(Integer.parseInt(skuId[i]));
				//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释

				try {
					inquiryInfoDTO.setDeliveryDate2(simpleDateFormat.parse(deliveryDates[i]));
				} catch (ParseException e) {

				}
				inquiryInfoDTO.setSupplierId(Integer.parseInt(shopId[i]));
				result = inquiryService.addInquiryInfo(inquiryInfoDTO);
				if(!result.isSuccess()){
					resultMap.put("result","更新失败!"+result.getErrorMessages().get(0));
					return resultMap;
				}
    		}else if("o".equals(statusDetail[i])){
    			inquiryInfoDTO.setQuantity(Integer.parseInt(nums[i]));
    			inquiryInfoDTO.setId(Long.parseLong(detailId[i]));
    			inquiryInfoDTO.setActiveFlag("1");
    			inquiryInfoDTO.setUpdateBy(uid);
    			inquiryInfoDTO.setSkuId(Integer.parseInt(skuId[i]));
				//询价、求购新增 交货日期、产品类目字段 待dubbo发布后打开此注释
				try {
					inquiryInfoDTO.setDeliveryDate2(simpleDateFormat.parse(deliveryDates[i]));
				}catch (ParseException e) {}
	    		inquiryInfoDTO.setUpdateDate(new Date());
	    		result = inquiryService.modifyInquiryInfoById(inquiryInfoDTO);
				if(!result.isSuccess()){
					resultMap.put("result","更新失败!"+result.getErrorMessages().get(0));
					return resultMap;
				}
    		}
    	}
    	String inquiryNo = inquiryInfoDTO.getInquiryNo();
    	//物品删除的，将询价单据删除
    	if(deleteId.length > 0){
	    	for(String id : deleteId){
	    		if(!"".equals(id)){
		    		inquiryInfoDTO = new InquiryInfoDTO(); 
		    		inquiryInfoDTO.setId(Long.parseLong(id));
		    		inquiryInfoDTO.setActiveFlag("0");
		    		inquiryInfoDTO.setUpdateBy(uid);
		    		inquiryInfoDTO.setUpdateDate(new Date());
					result = inquiryService.modifyInquiryInfoById(inquiryInfoDTO);
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
	 * <p>Discription:[卖家更新报价]</p>
	 * Created on 2015年7月16日
	 * @param inquiryInfoDTO
	 * @param price
	 * @param detailstartDate
	 * @param detailendDate
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> resopnseModifyInquiry(InquiryInfoDTO inquiryInfoDTO, String uid, String[] detailId, String[] price,
			String[] nums, String[] detailstartDate, String[] detailendDate) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "更新成功!");
    	//将主单的状态改为3，以便前台确认价格时，3以后的菜可以直接确认价格
    	inquiryInfoDTO.setUpdateBy(uid);
    	inquiryInfoDTO.setUpdateDate(new Date());
//    	result = inquiryExportService.modifyInquiryInfo(inquiryInfoDTO);
    	SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
    	ExecuteResult<String> resultDetail = new ExecuteResult<String>();
//    	if(result.isSuccess()){
		for(int i = 0 ; i < detailId.length ; i++){
			//主单保存成功才保存明细信息
			InquiryMatDTO inquiryMatDTO = new InquiryMatDTO();
			inquiryMatDTO.setInquiryNo(inquiryInfoDTO.getInquiryNo());
			inquiryMatDTO.setQuantity(Integer.parseInt(nums[i]));
			if(price[i] != null && !"".equals(price[i]) && !"null".equals(price[i]) && !"undefined".equals(price[i])){
				inquiryMatDTO.setMatPrice(Double.parseDouble(price[i]));
				try {
					inquiryMatDTO.setBeginDate(sim.parse(detailstartDate[i]));
					inquiryMatDTO.setEndDate(sim.parse(detailendDate[i]));
				} catch (ParseException e) {
					resultMap.put("result", "交货时间转化失败");
					e.printStackTrace();
				}
			}

			inquiryMatDTO.setId(Long.parseLong(detailId[i]));
			resultDetail = inquiryExportService.modifyInquiryMat(inquiryMatDTO);
			if(!resultDetail.isSuccess()){
				resultMap.put("result", "明细信息更新失败!"+resultDetail.getErrorMessages().get(0));
				return resultMap;
			}
		}
//    	}else{
//    		resultMap.put("result","主单信息更新失败!"+result.getErrorMessages().get(0));
//    	}
		return resultMap;
	}


	/**
	 * <p>Discription:[买家更新价格]</p>
	 * Created on 2015年7月16日
	 * @param dto
	 * @param detailId
	 * @return
	 * @author:[wanghao]
	 */
	@Override
	public Map<String, String> commitRequestInquiry(InquiryInfoDTO dto, String uid, String[] detailId) {
		Map<String,String> resultMap = new HashMap<String, String>(); 
    	resultMap.put("result", "操作成功!");
    	//更新主单状态
		ExecuteResult<String>  result = inquiryExportService.modifyInquiryInfo(dto);
		if(!result.isSuccess()){
			resultMap.put("result", "操作失败!"+result.getErrorMessages().get(0));
			return resultMap;
		}
		if(detailId.length > 0){
			for(String id : detailId){
				InquiryMatDTO inquiryMatDTO = new InquiryMatDTO();
				inquiryMatDTO.setId(Long.parseLong(id));
				inquiryMatDTO.setStatus("1");
				inquiryMatDTO.setUpdateBy(uid);
				inquiryMatDTO.setUpdateDate(new Date());
				ExecuteResult<String>  resultDetail = inquiryExportService.modifyInquiryMat(inquiryMatDTO);
				if(!resultDetail.isSuccess()){
					resultMap.put("result", "操作失败!"+resultDetail.getErrorMessages().get(0));
					return resultMap;
				}
			}
		}
		return resultMap;
	}

	/**
	 * <p>Discription:[重新询价]</p>
	 * Created on 2015年7月16日
	 * @return
	 * @author:[chengwt]
	 */
	@Override
	public Map<String, String> commitnquiryRe(InquiryInfoDTO dto, String uid) {
		Map<String,String> resultMap = new HashMap<String, String>();
		//根据编号查询其下的物品id
		ExecuteResult<String>  result = inquiryExportService.modifyInquiryInfo(dto);
		if(!result.isSuccess()){
			resultMap.put("result", "操作失败!"+result.getErrorMessages().get(0));
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
				matDto.setId(Long.parseLong("" + mapp.get("id")));
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
		resultMap.put("result", "操作成功!");
		return resultMap;
	}

}
