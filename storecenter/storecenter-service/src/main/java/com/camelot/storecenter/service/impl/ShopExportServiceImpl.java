package com.camelot.storecenter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.outdto.QueryChildCategoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.shop.domain.Area;
import com.camelot.storecenter.dao.ShopBrandDAO;
import com.camelot.storecenter.dao.ShopCategoryDAO;
import com.camelot.storecenter.dao.ShopInfoDAO;
import com.camelot.storecenter.dao.ShopModifyDetailDAO;
import com.camelot.storecenter.dao.ShopModifyInfoDAO;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopModifyDetailDTO;
import com.camelot.storecenter.dto.ShopModifyInfoDTO;
import com.camelot.storecenter.dto.emums.ShopModify.ShopModifyColumn;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.dto.indto.ShopInfoModifyInDTO;
import com.camelot.storecenter.service.ShopExportService;

@Service("shopExportService")
public class ShopExportServiceImpl implements ShopExportService {
	private final static Logger logger = LoggerFactory.getLogger(ShopExportServiceImpl.class);

	@Resource
	private ShopInfoDAO shopInfoDAO;
	@Resource
	private ShopBrandDAO shopBrandDAO;
	@Resource
	private ShopCategoryDAO shopCategoryDAO;
	@Resource
	private ShopModifyInfoDAO shopModifyInfoDAO;
	@Resource
	private ShopModifyDetailDAO shopModifyDetailDAO;
	@Resource
    private RedisDB redisDB;
//	@Resource
//	private ItemCategoryService itemCategoryService;

	@Override
	public ExecuteResult<String> saveShopInfo(ShopDTO shopDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			Long shopId=shopInfoDAO.getShopId();
			shopDTO.setShopId(shopId);
			shopInfoDAO.insert(shopDTO);
			result.setResult(shopId.toString());
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<ShopDTO> findShopInfoById(long id) {
		 ExecuteResult<ShopDTO> result=new  ExecuteResult<ShopDTO>();
		try {
			ShopDTO shopInfo=new ShopDTO();
			shopInfo = shopInfoDAO.selectById(id);
			result.setResult(shopInfo);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<ShopDTO>> findShopInfoByCondition(ShopDTO shopDTO, Pager<ShopDTO> pager) {
		ExecuteResult<DataGrid<ShopDTO>> result=new ExecuteResult<DataGrid<ShopDTO>>();
		try {
			List<ShopDTO> listShopInfo=shopInfoDAO.selectListByCondition(shopDTO, pager);
			Long size=shopInfoDAO.selectCountByCondition(shopDTO);
			DataGrid<ShopDTO> dataGrid=new DataGrid<ShopDTO>();
			dataGrid.setRows(listShopInfo);
			dataGrid.setTotal(size);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<String> modifyShopInfostatus(Long shopId, int status) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		
		try {
			ShopDTO shopDTO=new ShopDTO();
			shopDTO.setShopId(shopId);
			shopDTO.setStatus(status);
			this.shopInfoDAO.modifyShopInfostatus(shopDTO);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}
   
	/**
	 * <p>Discription:[批量查询操作]</p>
	 * Created on 2015年3月6日
	 * @param shopAudiinDTO
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<List<ShopDTO>> queryShopInfoByids(ShopAudiinDTO shopAudiinDTO){
		ExecuteResult<List<ShopDTO>> result=new ExecuteResult<List<ShopDTO>>(); 
		try {
			//根据父id查询子id的集合
			 if(shopAudiinDTO.getCid()!=null && shopAudiinDTO.getCid()>0){
				 ItemCategoryDTO itemCategoryDTO=new ItemCategoryDTO();
				 itemCategoryDTO.setCategoryCid(shopAudiinDTO.getCid());
//				 ExecuteResult<QueryChildCategoryOutDTO> queryChildCategory = itemCategoryService.queryAllChildCategory(itemCategoryDTO);
//				 if(queryChildCategory.isSuccess()){
//					 shopAudiinDTO.setChildCategorys(queryChildCategory.getResult().getChildCategorys());
//				 }
			 }
			result.setResult(shopInfoDAO.queryShopInfoByIds(shopAudiinDTO));
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		   return result;
	}

	@Override
	public ExecuteResult<String> modifyShopInfoUpdate(ShopInfoModifyInDTO shopInfoModifyInDTO) {
		ExecuteResult<String> result=new ExecuteResult<String> ();
		try {
			//修改类目
			if(shopInfoModifyInDTO.getShopCategoryList()!=null&&shopInfoModifyInDTO.getShopCategoryList().size()>0){
				shopCategoryDAO.deleteByShopId(shopInfoModifyInDTO.getShopDTO().getShopId());
				for (ShopCategoryDTO shopCategoryDTO:shopInfoModifyInDTO.getShopCategoryList()) {
					shopCategoryDTO.setShopId(shopInfoModifyInDTO.getShopDTO().getShopId());
					shopCategoryDTO.setSellerId(shopInfoModifyInDTO.getShopDTO().getSellerId());
					shopCategoryDAO.insert(shopCategoryDTO);
				}
			}
			//修改品牌
			if(shopInfoModifyInDTO.getShopBrandList()!=null&&shopInfoModifyInDTO.getShopBrandList().size()>0){
				shopBrandDAO.deleteByShopId(shopInfoModifyInDTO.getShopDTO().getShopId());
				for (ShopBrandDTO shopBrandDTO:shopInfoModifyInDTO.getShopBrandList()) {
					shopBrandDTO.setShopId(shopInfoModifyInDTO.getShopDTO().getShopId());
					shopBrandDTO.setSellerId(shopInfoModifyInDTO.getShopDTO().getSellerId());
					shopBrandDAO.insert(shopBrandDTO);
				}
			}
			shopInfoDAO.update(shopInfoModifyInDTO.getShopDTO());
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}
	@Override
	public ExecuteResult<String> modifyShopInfo(ShopInfoModifyInDTO shopInfoModifyInDTO) {
		ExecuteResult<String> result=new ExecuteResult<String> ();
		
		try {
			ShopDTO shopInfo = shopInfoModifyInDTO.getShopDTO();
			ShopDTO shopBefor= shopInfoDAO.selectById(shopInfo.getShopId());
			Long[] ids=new Long[]{shopInfo.getShopId()};
			List<ShopModifyInfoDTO> listsif = shopModifyInfoDAO.selectByIds(ids);
			if(listsif.size()>0&&listsif.get(0).getApplyStatus()==0){
				 result.setResultMessage("信息已经在审核中，不可重复申请！");
			}else{
				//添加新增品牌
				if(shopInfoModifyInDTO.getShopCategoryList()!=null&&shopInfoModifyInDTO.getShopCategoryList().size()>0){
					for (ShopCategoryDTO shopCategoryDTO:shopInfoModifyInDTO.getShopCategoryList()) {
						shopCategoryDTO.setShopId(shopInfoModifyInDTO.getShopDTO().getShopId());
						shopCategoryDTO.setSellerId(shopInfoModifyInDTO.getShopDTO().getSellerId());
						shopCategoryDAO.insert(shopCategoryDTO);
					}
				}
				//添加新增类目
				if(shopInfoModifyInDTO.getShopBrandList()!=null&&shopInfoModifyInDTO.getShopBrandList().size()>0){
					for (ShopBrandDTO shopBrandDTO:shopInfoModifyInDTO.getShopBrandList()) {
						shopBrandDTO.setShopId(shopInfoModifyInDTO.getShopDTO().getShopId());
						shopBrandDTO.setSellerId(shopInfoModifyInDTO.getShopDTO().getSellerId());
						shopBrandDAO.insert(shopBrandDTO);
					}
				}
	
				//添加店铺信息
				
				ShopDTO shopAfterNo=new ShopDTO(); // 不需要审核店铺信息
					//将不需要审核的信息添加到shopAfterNo 中并清空原对象中的该数据
					shopAfterNo.setShopId(shopInfo.getShopId());
					shopAfterNo.setKeyword(shopInfo.getKeyword());
					shopInfo.setKeyword(null);
					shopAfterNo.setMobile(shopInfo.getMobile());
					shopInfo.setMobile(null);
					shopAfterNo.setAreaCode(shopInfo.getAreaCode());
					shopInfo.setAreaCode(null);
					shopAfterNo.setLandline(shopInfo.getLandline());
					shopInfo.setLandline(null);
					shopAfterNo.setExtensionNumber(shopInfo.getExtensionNumber());
					shopInfo.setExtensionNumber(null);
					shopAfterNo.setLinkMan1(shopInfo.getLinkMan1());
					shopInfo.setLinkMan1(null);
					shopAfterNo.setLinkPhoneNum1(shopInfo.getLinkPhoneNum1());
					shopInfo.setLinkPhoneNum1(null);
					shopAfterNo.setLinkMan2(shopInfo.getLinkMan2());
					shopInfo.setLinkMan2(null);
					shopAfterNo.setLinkPhoneNum2(shopInfo.getLinkPhoneNum2());
					shopInfo.setLinkPhoneNum2(null);
					shopAfterNo.setRemark(shopInfo.getRemark());
					shopInfo.setRemark(null);
					
					
					shopInfoDAO.update(shopAfterNo);  //修改不需要审核的店铺信息
					//判断需要审核的信息是否更改 
					this.isChange(shopInfo, shopBefor,listsif);
		
				result.setResultMessage("success");
			}
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[判断店铺信息是否变更 变更插入申请表]</p>
	 * Created on 2015-3-12
	 * @param shopInfo
	 * @param shopBefor
	 * @author:yuht
	 */
	private void isChange(ShopDTO shopInfo,ShopDTO shopBefor,List<ShopModifyInfoDTO> listsif){
		//查询修改总表是否存在记录 
		boolean ischange=false;
		ShopModifyInfoDTO smiDTO=new ShopModifyInfoDTO();
		//如果总表不存在该店铺修改信息 添加一条
		if(listsif.size()<=0){
			smiDTO.setApplicantUserid(shopInfo.getSellerId());
			smiDTO.setShopId(shopInfo.getShopId());
			shopModifyInfoDAO.insert(smiDTO);
		}else{
			smiDTO.setId(listsif.get(0).getId());
		}
		
		ShopModifyDetailDTO smdDTO=new ShopModifyDetailDTO();
		smdDTO.setModifyInfoId(smiDTO.getId());
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getShopName(),shopInfo.getShopName())){
			//店铺名称
			smdDTO.setPropertiesColumn(ShopModifyColumn.shop_name.name());
			smdDTO.setBeforeChange(shopBefor.getShopName());
			smdDTO.setAfterChange(shopInfo.getShopName());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getLogoUrl(),shopInfo.getLogoUrl())){
			//lONG 地址 
			smdDTO.setPropertiesColumn(ShopModifyColumn.logo_url.name());
			smdDTO.setBeforeChange(shopBefor.getLogoUrl());
			smdDTO.setAfterChange(shopInfo.getLogoUrl());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getIntroduce(),shopInfo.getIntroduce())){
			//店铺介绍
			smdDTO.setPropertiesColumn(ShopModifyColumn.introduce.name());
			smdDTO.setBeforeChange(shopBefor.getIntroduce());
			smdDTO.setAfterChange(shopInfo.getIntroduce());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getMainSell(),shopInfo.getMainSell())){
			//店铺主营
			smdDTO.setPropertiesColumn(ShopModifyColumn.main_sell.name());
			smdDTO.setBeforeChange(shopBefor.getMainSell());
			smdDTO.setAfterChange(shopInfo.getMainSell());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		
		
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getMutilPrice(),shopInfo.getMutilPrice())){
			//是否混批（1为混批，2为不混批）
			smdDTO.setPropertiesColumn(ShopModifyColumn.mutilPrice.name());
			smdDTO.setBeforeChange(shopBefor.getMutilPrice()==null?"":shopBefor.getMutilPrice().toString());
			smdDTO.setAfterChange(shopInfo.getMutilPrice()==null?"":shopInfo.getMutilPrice().toString());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(shopInfo.getMutilPrice()==1){
			if(!shopInfo.getMutilCondition().equals(shopBefor.getMutilCondition())){
				//混批条件，1为或，2为且
				smdDTO.setPropertiesColumn(ShopModifyColumn.mutil_condition.name());
				smdDTO.setBeforeChange(shopBefor.getMutilCondition()==null?"":shopBefor.getMutilCondition().toString());
				smdDTO.setAfterChange(shopInfo.getMutilCondition()==null?"":shopInfo.getMutilCondition().toString());
				shopModifyDetailDAO.insert(smdDTO);
				ischange=true;
			}
			if(this.validaIsNotSameBoforeAndAfter(shopBefor.getPriceMin(),shopInfo.getPriceMin())){
				//混批金额要求
				smdDTO.setPropertiesColumn(ShopModifyColumn.price_min.name());
				smdDTO.setBeforeChange(shopBefor.getPriceMin()==null?"":shopBefor.getPriceMin().toString());
				smdDTO.setAfterChange(shopInfo.getPriceMin()==null?"":shopInfo.getPriceMin().toString());
				shopModifyDetailDAO.insert(smdDTO);
				ischange=true;
			}
			if(this.validaIsNotSameBoforeAndAfter(shopBefor.getMountMin(),shopInfo.getMountMin())){
				//混批数量要求
				smdDTO.setPropertiesColumn(ShopModifyColumn.mount_min.name());
				smdDTO.setBeforeChange(shopBefor.getMountMin()==null?"":shopBefor.getMountMin().toString());
				smdDTO.setAfterChange(shopInfo.getMountMin()==null?"":shopInfo.getMountMin().toString());
				shopModifyDetailDAO.insert(smdDTO);
				ischange=true;
			}
		}
		
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getInitialPrice(),shopInfo.getInitialPrice())){
			// 起批价格
			smdDTO.setPropertiesColumn(ShopModifyColumn.initial_price.name());
			smdDTO.setBeforeChange(shopBefor.getInitialPrice()==null?"":shopBefor.getInitialPrice().toString());
			smdDTO.setAfterChange(shopInfo.getInitialPrice()==null?"":shopInfo.getInitialPrice().toString());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getInitialMount(),shopInfo.getInitialMount())){
			//起批数量
			smdDTO.setPropertiesColumn(ShopModifyColumn.initial_mount.name());
			smdDTO.setBeforeChange(shopBefor.getInitialMount()==null?"":shopBefor.getInitialMount().toString());
			smdDTO.setAfterChange(shopInfo.getInitialMount()==null?"":shopInfo.getInitialMount().toString());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getInitialCondition(),shopInfo.getInitialCondition())){
			//起批条件，1为或，2为且
			smdDTO.setPropertiesColumn(ShopModifyColumn.initial_condition.name());
			smdDTO.setBeforeChange(shopBefor.getInitialCondition()==null?"":shopBefor.getInitialCondition().toString());
			smdDTO.setAfterChange(shopInfo.getInitialCondition()==null?"":shopInfo.getInitialCondition().toString());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getCityCode(),shopInfo.getCityCode())||this.validaIsNotSameBoforeAndAfter(shopBefor.getDistrictCode(),shopInfo.getDistrictCode())||
				this.validaIsNotSameBoforeAndAfter(shopBefor.getProvinceCode(),shopInfo.getProvinceCode())	){
			//城市编码 市  区  
			Area  area=new Area();
/*			area.setProvinceCode(ShopModifyColumn.province_code.name());
			area.setProvinceName(ShopModifyColumn.province_name.name());
			area.setCityCode(ShopModifyColumn.city_code.name());
			area.setCityName(ShopModifyColumn.city_name.name());
			area.setDistrictCode(ShopModifyColumn.district_code.name());
			area.setDistrictName(ShopModifyColumn.district_name.name());*/
			smdDTO.setPropertiesColumn("pcdCombin");
			
			area.setProvinceCode(shopBefor.getProvinceCode());
			area.setProvinceName(shopBefor.getProvinceName());
			area.setCityCode(shopBefor.getCityCode()==null?"":shopBefor.getCityCode().toString());
			area.setCityName(shopBefor.getCityName());
			area.setDistrictCode(shopBefor.getDistrictCode());
			area.setDistrictName(shopBefor.getDistrictName());
			smdDTO.setBeforeChange(JSON.toJSONString(area));
			
			area.setProvinceCode(shopInfo.getProvinceCode());
			area.setProvinceName(shopInfo.getProvinceName());
			area.setCityCode(shopInfo.getCityCode()==null?"":shopInfo.getCityCode().toString());
			area.setCityName(shopInfo.getCityName());
			area.setDistrictCode(shopInfo.getDistrictCode());
			area.setDistrictName(shopInfo.getDistrictName());
			smdDTO.setAfterChange(JSON.toJSONString(area));
			
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getZcode(),shopInfo.getZcode())){
			//邮政编码
			smdDTO.setPropertiesColumn(ShopModifyColumn.zcode.name());
			smdDTO.setBeforeChange(shopBefor.getZcode());
			smdDTO.setAfterChange(shopInfo.getZcode());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getStreetName(),shopInfo.getStreetName())){
			//街道名称
			smdDTO.setPropertiesColumn(ShopModifyColumn.street_name.name());
			smdDTO.setBeforeChange(shopBefor.getStreetName());
			smdDTO.setAfterChange(shopInfo.getStreetName());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		//新添加需要校验字段  wangcunshan 
		
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getShopType(),shopInfo.getShopType())){
			//店铺类型
			smdDTO.setPropertiesColumn(ShopModifyColumn.shop_type.name());
			smdDTO.setBeforeChange(shopBefor.getShopType()+"");
			smdDTO.setAfterChange(shopInfo.getShopType()+"");
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getBrandType(),shopInfo.getBrandType())){
			//品牌类型
			smdDTO.setPropertiesColumn(ShopModifyColumn.brand_type.name());
			smdDTO.setBeforeChange(shopBefor.getBrandType()+"");
			smdDTO.setAfterChange(shopInfo.getBrandType()+"");
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getBusinessType(),shopInfo.getBusinessType())){
			//经营类型
			smdDTO.setPropertiesColumn(ShopModifyColumn.business_type.name());
			smdDTO.setBeforeChange(shopBefor.getBusinessType()+"");
			smdDTO.setAfterChange(shopInfo.getBusinessType()+"");
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getDisclaimer(),shopInfo.getDisclaimer())){
			//免责声明
			smdDTO.setPropertiesColumn(ShopModifyColumn.disclaimer.name());
			smdDTO.setBeforeChange(shopBefor.getDisclaimer());
			smdDTO.setAfterChange(shopInfo.getDisclaimer());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getTrademarkRegistCert(),shopInfo.getTrademarkRegistCert())){
			//商标注册证/商品注册申请书扫描件
			smdDTO.setPropertiesColumn(ShopModifyColumn.trademark_regist_cert.name());
			smdDTO.setBeforeChange(shopBefor.getTrademarkRegistCert());
			smdDTO.setAfterChange(shopInfo.getTrademarkRegistCert());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getInspectionReport(),shopInfo.getInspectionReport())){
			//质检、检疫、检验报告/报关单类扫描件
			smdDTO.setPropertiesColumn(ShopModifyColumn.inspection_report.name());
			smdDTO.setBeforeChange(shopBefor.getInspectionReport());
			smdDTO.setAfterChange(shopInfo.getInspectionReport());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getProductionLicense(),shopInfo.getProductionLicense())){
			//卫生/生产许可证扫描件
			smdDTO.setPropertiesColumn(ShopModifyColumn.production_license.name());
			smdDTO.setBeforeChange(shopBefor.getProductionLicense());
			smdDTO.setAfterChange(shopInfo.getProductionLicense());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getMarketingAuth(),shopInfo.getMarketingAuth())){
			//销售授权书扫描件
			smdDTO.setPropertiesColumn(ShopModifyColumn.marketing_auth.name());
			smdDTO.setBeforeChange(shopBefor.getMarketingAuth());
			smdDTO.setAfterChange(shopInfo.getMarketingAuth());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getFinancing(),shopInfo.getFinancing())){
			//是否有融资需求
			smdDTO.setPropertiesColumn(ShopModifyColumn.financing.name());
			smdDTO.setBeforeChange(shopBefor.getFinancing()+"");
			smdDTO.setAfterChange(shopInfo.getFinancing()+"");
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getFinancingAmt(),shopInfo.getFinancingAmt())){
			//融资金额  以万元为单位
			smdDTO.setPropertiesColumn(ShopModifyColumn.financing_amt.name());
			smdDTO.setBeforeChange(shopBefor.getFinancingAmt()+"");
			smdDTO.setAfterChange(shopInfo.getFinancingAmt()+"");
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		//承诺书
		if(this.validaIsNotSameBoforeAndAfter(shopBefor.getGpCommitmentBook(),shopInfo.getGpCommitmentBook())){
			smdDTO.setPropertiesColumn(ShopModifyColumn.gp_commitment_book.name());
			smdDTO.setBeforeChange(shopBefor.getGpCommitmentBook());
			smdDTO.setAfterChange(shopInfo.getGpCommitmentBook());
			shopModifyDetailDAO.insert(smdDTO);
			ischange=true;
		}
		if(ischange){
			shopModifyInfoDAO.updateDate(shopInfo.getShopId());
		}
	
	}
	private boolean validaIsNotSameBoforeAndAfter(Object before,Object after){
    	
    	if(before==null&&after!=null){
    		return true;
    	}else if(before!=null&&after!=null&&!before.equals(after)){
    		return true;
    	}else{
    		return false;
    	}
    }
	@Override
	public ExecuteResult<String> modifyShopRunStatus(Long shopId, Integer runStatus) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			ShopDTO shopDTO=new ShopDTO();
			shopDTO.setShopId(shopId);
			shopDTO.setRunStatus(runStatus);
			Integer count=shopInfoDAO.modifyShopRunStatus(shopDTO);
			result.setResult(count+"");
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> modifyShopInfoAndcbstatus(ShopDTO shopDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			
			shopInfoDAO.modifyShopInfostatus(shopDTO);
			ShopBrandDTO sbDTO=new ShopBrandDTO();
			sbDTO.setStatus(1);
			sbDTO.setShopId(shopDTO.getShopId());
			List<ShopBrandDTO> brandList = shopBrandDAO.selectListByConditionAll(sbDTO, null);
			for (ShopBrandDTO sbDto1:brandList) {
				ShopBrandDTO shopBrandDTO=new ShopBrandDTO();
				shopBrandDTO.setId(sbDto1.getId());
				shopBrandDTO.setStatus(shopDTO.getStatus());
				shopBrandDAO.modifyShopCategoryStatus(shopBrandDTO);
			}
			ShopCategoryDTO scDTO=new ShopCategoryDTO();
			scDTO.setStatus(1);
			scDTO.setShopId(shopDTO.getShopId());
			List<ShopCategoryDTO> sclist = shopCategoryDAO.selectListByConditionAll(scDTO, null);
			for (ShopCategoryDTO scDto1:sclist) {
				ShopCategoryDTO shopCategoryDTO=new ShopCategoryDTO();
				shopCategoryDTO.setId(scDto1.getId());
				shopCategoryDTO.setStatus(shopDTO.getStatus());
				shopCategoryDAO.modifyShopCategoryStatus(shopCategoryDTO);
			}
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> modifyShopstatusAndRunstatus(Long shopId) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			ShopDTO shopDTO=new ShopDTO();
			shopDTO.setShopId(shopId);
			shopDTO.setRunStatus(1);
			shopDTO.setStatus(5);
			shopInfoDAO.modifyShopRunStatus(shopDTO);
			shopInfoDAO.modifyShopInfostatus(shopDTO);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	@Override
	public ExecuteResult<String> modifyShopstatusAndRunstatusBack(Long shopId) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			ShopDTO shopDTO=new ShopDTO();
			shopDTO.setShopId(shopId);
			shopDTO.setRunStatus(2);
			shopDTO.setStatus(2);
			shopInfoDAO.modifyShopRunStatus(shopDTO);
			shopInfoDAO.modifyShopInfostatus(shopDTO);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<ShopDTO>> queryShopInfoByBrandId(Long brandId, Pager page) {
		ExecuteResult<DataGrid<ShopDTO>> result=new ExecuteResult<DataGrid<ShopDTO>>();
		try {
			DataGrid<ShopDTO> dataGrid=new DataGrid<ShopDTO>();
			List<ShopDTO> list=shopInfoDAO.queryShopInfoByBrandId(brandId,page);
			Long count=shopInfoDAO.queryCountShopInfoByBrandId(brandId);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

    @Override
    public ExecuteResult<String> addSecondDomainToRedis(String shopUrl, Long shopId) {
        ExecuteResult<String> exeResult = new ExecuteResult<String>();
        exeResult.setResultMessage("success");
        Object obj = redisDB.getObject(ShopExportService.SECOND_DOMAIN_KEY);
        if(obj == null){
            addSecondDomainToRedis();
            return exeResult;
        }
        Map<String, String> secondDomainMap = (Map<String, String>)obj;
        if(StringUtils.isNotBlank(shopUrl) && null!=shopId){
            secondDomainMap.put(shopUrl.toLowerCase(), shopId + "");
            redisDB.addObject(ShopExportService.SECOND_DOMAIN_KEY, secondDomainMap);
        }
        return exeResult;
    }

    @Override
	public ExecuteResult<String> addSecondDomainToRedis() {
    	logger.debug("add secondDomain to redis start....");
		ExecuteResult<String> exeResult = new ExecuteResult<String>();
		exeResult.setResultMessage("success");
		Map<String, String> secondDomainMap = new HashMap<String, String>();
		ExecuteResult<List<ShopDTO>> result = this.findAllKeyinShopInfo();
		if (result == null) {
			return exeResult;
		}
		List<ShopDTO> shopList = result.getResult();
		if (shopList == null) {
			return exeResult;
		}
		for (int i = 0, size = shopList.size(); null != shopList && i < size; i++) {
			ShopDTO shopDTO = shopList.get(i);
			if (StringUtils.isNotBlank(shopDTO.getShopUrl()) && null != shopDTO.getShopId()) {
				secondDomainMap.put(shopDTO.getShopUrl().toLowerCase(), shopDTO.getShopId() + "");
			}
		}
		redisDB.addObject(ShopExportService.SECOND_DOMAIN_KEY, secondDomainMap);
		logger.debug("add secondDomain to redis end....");
		return exeResult;
	}

    @Override
    public ExecuteResult<String> addGreenPrintThirdDomainToRedis(String shopUrl, Long shopId)
    {
        ExecuteResult<String> exeResult = new ExecuteResult<String>();
        exeResult.setResultMessage("success");
        Object obj = redisDB.getObject(ShopExportService.THIRD_DOMAIN_KEY);
        if(obj == null){
            addThirdDomainToRedis();
            return exeResult;
        }
        Map<String, String> thirdDomainMap = (Map<String, String>)obj;
        if(StringUtils.isNotBlank(shopUrl) && null!=shopId){
            thirdDomainMap.put(shopUrl.toLowerCase(), shopId + "");
            redisDB.addObject(ShopExportService.THIRD_DOMAIN_KEY, thirdDomainMap);
        }
        return exeResult;
    }

    @Override
	public ExecuteResult<String> addThirdDomainToRedis() {
		ExecuteResult<String> exeResult = new ExecuteResult<String>();
		exeResult.setResultMessage("success");
		Map<String, String> thirdDomainMap = new HashMap<String, String>();
		ShopDTO shopDTO = new ShopDTO();
		shopDTO.setPlatformId(Constants.PLATFORM_ID);
		ExecuteResult<DataGrid<ShopDTO>> result = this.findShopInfoByCondition(shopDTO, null);
		if (result == null) {
			return exeResult;
		}
		DataGrid<ShopDTO> dategrid = result.getResult();
		if (dategrid == null) {
			return exeResult;
		}
		List<ShopDTO> shopList = dategrid.getRows();
		for (int i = 0, size = shopList.size(); null != shopList && i < size; i++) {
			shopDTO = shopList.get(i);
			if (StringUtils.isNotBlank(shopDTO.getShopUrl()) && null != shopDTO.getShopId()) {
				thirdDomainMap.put(shopDTO.getShopUrl().toLowerCase(), shopDTO.getShopId() + "");
			}
		}
		redisDB.addObject(ShopExportService.THIRD_DOMAIN_KEY, thirdDomainMap);
		return exeResult;
	}
	
	@Override
	public ExecuteResult<List<ShopDTO>> findAllKeyinShopInfo() {
		ExecuteResult<List<ShopDTO>>  result=new ExecuteResult<List<ShopDTO>>();
		try {
			List<ShopDTO> listShopInfo=shopInfoDAO.findAllKeyinShopInfo();
			result.setResult(listShopInfo);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}
}
