package com.camelot.ecm.translationInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.common.Json;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.thinkgem.jeesite.common.persistence.Page;

@Controller
@RequestMapping(value = "${adminPath}/translationInfo")
public class TranslationInfoController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TranslationExportService translationexportservice;
	
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private ShopExportService shopExportService;
	
	@Resource
    private ItemCategoryService itemCategoryService;
	
	
	@RequestMapping(value = "listTranslation")
    public String listTranslation(@ModelAttribute("pager") Pager<?> pager,Model model,@ModelAttribute("translationInfoDTO") TranslationInfoDTO translationInfoDTO){
		translationInfoDTO.setActiveFlag("1");
		List<String> statusList=new ArrayList<String>();
		statusList.add("1");//待审核
		statusList.add("2");//审核通过
		statusList.add("4");//审核驳回
    	translationInfoDTO.setStatusList(statusList);
		ExecuteResult<DataGrid<TranslationInfoDTO>> er= new ExecuteResult<DataGrid<TranslationInfoDTO>>();
		er = translationexportservice.queryTranslationInfoList(translationInfoDTO, pager);
		Page<TranslationInfoDTO> page = new Page<TranslationInfoDTO>();
		List<TranslationInfoDTO> dtos = new ArrayList<TranslationInfoDTO>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if( er.isSuccess() && er.getResult() != null ){
			for( TranslationInfoDTO dto: er.getResult().getRows() ){
				UserDTO seller = this.userExportService.queryUserById(Long.parseLong(dto.getCreateBy()));
				if(seller == null || seller.getCompanyName()== null || "null".equals(seller.getCompanyName())){
					dto.setAlternate1("公司名称为空");
				}else{
					dto.setAlternate1(seller.getCompanyName());
				}
				dto.setTranslationNo(dto.getTranslationNo());
				dtos.add(dto);
			}
			page.setCount(er.getResult().getTotal().intValue());
			page.setList(dtos);
		}
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		model.addAttribute("pager", page);
    	return "/translationInfo/translationInfo";
    }
	
	@ResponseBody
    @RequestMapping(value="approve")
    public Map<String, String> modifyStatus(HttpServletRequest request){
		Map<String, String> resultMap = new HashMap<String, String>();
		List<Map<String,Object>> idsList = new ArrayList<Map<String,Object>>();
    	 try{
    		    String ids = request.getParameter("ids");
    		    String status=request.getParameter("status");
    		    String refuseReason=request.getParameter("auditRemark");
    			TranslationInfoDTO dto = new TranslationInfoDTO();
    			TranslationMatDTO dtoDetail = new TranslationMatDTO();
    			Pager pager= new Pager();
    			pager.setRows(Integer.MAX_VALUE);
    			String[] idList = ids.split(",");
    			ExecuteResult<String> dtoresult = new ExecuteResult<String>();
    			ExecuteResult<String> resultDetail = new ExecuteResult<String>();
    			for(String id : idList){
    				if(StringUtils.isNotEmpty(refuseReason)){
    					dto.setRefuseReason(refuseReason);
        		    }
    				dto.setStatus(status);
    				dto.setActiveFlag("1");
    				dto.setTranslationNo(id);
    				dto.setUpdateBy("1");
    				dto.setUpdateDate(new Date());
    				TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
    				translationInfoDTO.setTranslationNo(id);
    				translationInfoDTO.setActiveFlag("1");
    				ExecuteResult<DataGrid<Map>> trans =translationexportservice.queryTranslationInfoPager(translationInfoDTO, pager);
    				if(trans.isSuccess() && trans.getResult().getTotal().longValue() > 0L){
    					for(Map map : trans.getResult().getRows()){
    						Map<String, Object> tmpMap = new HashMap<String, Object>();
    						tmpMap.put("id", id);
    						tmpMap.put("buyerId", map.get("createBy"));
    						tmpMap.put("translationName", map.get("translationName"));
    						idsList.add(tmpMap);
    						dto.setId(Long.parseLong(""+map.get("id")));
    						dtoresult = translationexportservice.modifyTranslationInfo(dto);
    						if(!dtoresult.isSuccess()){
    							resultMap.put("result", "审核失败!"+dtoresult.getErrorMessages().get(0));
    							resultMap.put("success","false");
    							return resultMap;
    						}
    					}
    				}else{
    					resultMap.put("result", "审核失败!"+trans.getErrorMessages());
    				}
    			}
    			resultMap.put("result", "审核成功!");
    			resultMap.put("success","true");
    			request.setAttribute("status", status);
    			request.setAttribute("success", "true");
    			request.setAttribute("idsList", idsList);
    	 }catch(Exception e){
    		 logger.error(e.getMessage());
    		 resultMap.put("result","系统出现意外错误，请联系管理员");
    		 resultMap.put("success","false");
    		 request.setAttribute("success", "false");
    	 }
    	return resultMap;
    } 
	@RequestMapping(value="detailList")
	public String detailList(HttpServletRequest request,Model model){
		String translationNo = request.getParameter("translationNo");
		String flag = request.getParameter("flag");
		TranslationInfoDTO dto = new TranslationInfoDTO();
		Pager pager = new Pager();
		pager.setRows(1);
		dto.setTranslationNo(translationNo);
		dto.setActiveFlag("1");

		ExecuteResult<DataGrid<TranslationInfoDTO>> translationDTOS =  translationexportservice.queryTranslationInfoList(dto,pager);
		//查询明细中的supplyID为登陆人所属的supplyId相同的才展示
		TranslationMatDTO detailDTO = new TranslationMatDTO();
		detailDTO.setActiveFlag("1");
		detailDTO.setTranslationNo(translationNo);
		//暂时把人员公司关系注释
		pager.setRows(Integer.MAX_VALUE);
		ExecuteResult<DataGrid<Map>> translationDTOList =  translationexportservice.queryTranslationInfoPager(dto, pager);
		ExecuteResult<DataGrid<Map>> detailDTOList =  translationexportservice.queryTranslationMatList(detailDTO, pager);
		List<Map> mapList = new ArrayList<Map>();
		//标示字符串，i标示保存时插入，u标示修改
		String addFlag ="i";

		if(translationDTOS.isSuccess()){
			TranslationInfoDTO translationInfoDTO = translationDTOS.getResult().getRows().get(0);
			UserDTO user = this.userExportService.queryUserById(Long.parseLong(translationInfoDTO.getCreateBy()));
			model.addAttribute("translationId", translationInfoDTO.getId());
			model.addAttribute("translationNo", translationInfoDTO.getTranslationNo());
			model.addAttribute("translationName", translationInfoDTO.getTranslationName());
			if(user == null || user.getCompanyName()== null || "null".equals(user.getCompanyName())){
				model.addAttribute("printerName", "公司名字为空");
			}else{
				model.addAttribute("printerName", user.getCompanyName());
			}
			model.addAttribute("printerId", translationInfoDTO.getPrinterId());
			model.addAttribute("supplierId", translationInfoDTO.getSupplierId());
			if(translationInfoDTO.getAnnex() == null ||  "".equals(translationInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				model.addAttribute("annex", translationInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", translationInfoDTO.getBeginDate());
			model.addAttribute("endDate", translationInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", translationInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", translationInfoDTO.getRemarks());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			int i = 1;
			//取出map放入list中，前台展示用
			//明细中有此供应商的则只修改此供应商的这条记录，否则，查询主单的物品名、数量信息
			if(detailDTOList.getResult().getTotal() > 0){
				mapList = detailDTOList.getResult().getRows();
				addFlag = "u";
			}else{
				mapList = translationDTOList.getResult().getRows();
			}
			for(Map dtoItem : mapList){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))) {
					ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(Long.parseLong("" + dtoItem.get("shopId")));
					map.put("shopName", shop.getResult().getShopName());
				}
				map.put("category_ids", ""+dtoItem.get("categoryId"));
				map.put("category_names", getCategory(Long.parseLong(dtoItem.get("categoryId")+"")));
				if("u".equals(addFlag)){
					map.put("matDesc", ""+dtoItem.get("matDesc"));
					SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
					String detailStartDate=sd.format(dtoItem.get("beginDate"));
					map.put("detailStartDate", detailStartDate);
					String detailEndDate=sd.format(dtoItem.get("endDate"));
					map.put("detailEndDate", detailEndDate);
				}else{
					map.put("matDesc", ""+dtoItem.get("matCd"));
				}

				map.put("quantity", ""+dtoItem.get("quantity"));
				if("null".equals(dtoItem.get("matPrice")) || dtoItem.get("matPrice") == null){
					map.put("matPrice",  "");
				}else{
					map.put("matPrice",  ""+dtoItem.get("matPrice"));
				}
				map.put("matAttribute", ""+dtoItem.get("matAttribute"));
				map.put("addFlag", addFlag);
				map.put("id", ""+dtoItem.get("id"));
				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
			model.addAttribute("update_flag", "1");
		}
		model.addAttribute("flag", flag);
		
		return "/translationInfo/detailList";
	}
	public String getCategory(Long cid){
    	String oneName="";
        String twoName = "";
        String threeName = "";
        StringBuilder category = new StringBuilder();
        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory =  itemCategoryService.queryParentCategoryList(cid);
        for(ItemCatCascadeDTO itemCatCascadeOne : resultCategory.getResult()){
			for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
				for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
					if(cid.equals(itemCatCascadeThree.getCid())){
						if(threeName.length()==0){
							threeName = itemCatCascadeThree.getCname();
						}
						if(oneName.length()==0){
							oneName = itemCatCascadeOne.getCname();
						}
						if(twoName.length()==0){
							twoName = itemCatCascadeTwo.getCname();
						}
						itemCatCascadeTwo.getCname();
						/*ItemCatCascadeLevelTwoList = itemCatCascadeOne.getChildCats();
						ItemCatCascadeLevelThreeList = itemCatCascadeTwo.getChildCats();*/
						break ;
					}
				}
			}
		}
        category.append(oneName+"/"+twoName+"/"+threeName);
        return category.toString();
    }
	
}
