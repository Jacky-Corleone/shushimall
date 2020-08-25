package com.camelot.mall.service.impl;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.mall.dto.ShopInfoInRedisDTO;
import com.camelot.mall.service.ShopClientService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;

@Service("shopClientService")
public class ShopClientServiceImpl implements ShopClientService{
	@Resource
	private ShopCategoryExportService shopCategoryExportService;

	@Resource
	private ShopBrandExportService shopBrandExportService;

	/**组装一级、二级、三级类目的名称*/
	@Override
	public Map<String, String> buildCategoryNameList(List<ItemCatCascadeDTO> result) {
		Map<String, String> namelist = new HashMap<String, String>();
		for(int i=0; result!=null && i<result.size(); i++){
			ItemCatCascadeDTO lev1 = result.get(i);//一级类目
			List<ItemCatCascadeDTO> lev2list = lev1.getChildCats();
			for(int j=0; lev2list!=null && j<lev2list.size(); j++){
				ItemCatCascadeDTO lev2 = lev2list.get(j);
				List<ItemCatCascadeDTO> lev3list = lev2.getChildCats();
				for(int k=0; lev3list!=null && k<lev3list.size(); k++){
					ItemCatCascadeDTO lev3 = lev3list.get(k);
					String cname = lev1.getCname()+">" + lev2.getCname()+">" + lev3.getCname();
					namelist.put(lev3.getCid()+"", cname);
				}
			}
		}
		return namelist;
	}

	/**构建店铺已有的经营类目id字符串*/
	@Override
	public String buildExistsCids(Long[] scids) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; scids!=null && i<scids.length; i++){
			if(i>0){
				sb.append(",");
			}
			sb.append(scids[i]);
		}
		return sb.toString();
	}

	//从redis取出的对象中获取品牌id数组
	@Override
	public Long[] getBrandIds(ShopInfoInRedisDTO shopInfoRedis) {
		String cid_brands_str = shopInfoRedis.getBrandIds();
		if(StringUtils.isBlank(cid_brands_str)){
			return new Long[]{};
		}
		String[] cidBrandIds = cid_brands_str.split("\\,");//类目品牌id集合，格式为： cid:brandId,cid:brandId
		List<Long> brandIds = new ArrayList<Long>();
		for(int i=0; i<cidBrandIds.length; i++){
            String cidbrandid = cidBrandIds[i];
            if(StringUtils.isBlank(cidbrandid)){
                continue;
            }
            String brandid = cidbrandid.split("\\:")[1];
            if(StringUtils.isBlank(brandid)){
                continue;
            }
            brandIds.add(Long.parseLong(brandid));
		}
        Long[] bids = new Long[brandIds.size()];
        for(int i=0; i<brandIds.size(); i++){
            bids[i] = brandIds.get(i);
        }
		return bids;
	}

	//从redis取出的对象中获取类目id数组
	@Override
	public Long[] getCids(ShopInfoInRedisDTO shopInfoRedis) {
		String cidsStr = shopInfoRedis.getCids();
		if(StringUtils.isBlank(cidsStr)){
			return new Long[]{};
		}
        List<Long> lList = new ArrayList<Long>();
		String[] cids = cidsStr.split("\\,");//新增加的店铺类目的类目id数组
		for(int i=0; i<cids.length; i++){
            if(StringUtils.isNotBlank(cids[i])){
                lList.add(Long.parseLong(cids[i]));
            }
		}
		Long[] cidArr = new Long[lList.size()];
		for(int i=0; i<lList.size(); i++){
			cidArr[i] = lList.get(i);
		}
        return cidArr;
	}

	/**组装店铺的平台类目id数组*/
	public Long[] buildShopCategoryIds(List<ShopCategoryDTO> list) {
		if(list==null){
			return new Long[]{};
		}
		Long[] cids = new Long[list.size()];
		for(int i=0; i<list.size(); i++){
			cids[i] = list.get(i).getCid();
		}
		return cids;
	}

	/**组装品牌id*/
	public Long[] buildBrandIds(ExecuteResult<DataGrid<ShopBrandDTO>> shopBrandList) {
		if(null == shopBrandList || null==shopBrandList.getResult()){
			return new Long[]{};
		}
		List<ShopBrandDTO> list = shopBrandList.getResult().getRows();
		Long[] brandids = new Long[list.size()];
		for(int i=0; i<list.size(); i++){
			brandids[i] = list.get(i).getBrandId();
		}
		return brandids;
	}

	//批量保存类目数据
	@Override
	public void addShopCategory(String addCategoryCids, Long userId,
			Long shopId) {
		if(StringUtils.isBlank(addCategoryCids)){
			return;
		}
		String[] cids = addCategoryCids.split("\\,");
		for(int i=0; i<cids.length; i++){
			if(StringUtils.isBlank(cids[i])){
				continue;
			}
			ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
			shopCategoryDTO.setCid(Long.parseLong(cids[i]));
			shopCategoryDTO.setCreated(new Date());
			shopCategoryDTO.setModified(new Date());
			shopCategoryDTO.setSellerId(userId);
			shopCategoryDTO.setShopId(shopId);
			shopCategoryDTO.setStatus(ShopCategoryStatus.APPLY.getCode());
			shopCategoryExportService.addShopCategory(shopCategoryDTO);
		}
	}

	//批量保存品牌数据
	public void addShopBrand(String addBrandIds, Long userId, Long shopId) {
		if(StringUtils.isBlank(addBrandIds)){
			return;
		}
		String[] cidBrandIds = addBrandIds.split("\\,");
		for(int i=0; i<cidBrandIds.length; i++){
			if(StringUtils.isBlank(cidBrandIds[i])){
				continue;
			}
			ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
			String[] cid_brandid = cidBrandIds[i].split("\\:");
			if(StringUtils.isBlank(cid_brandid[0]) || StringUtils.isBlank(cid_brandid[1])){
				continue;
			}
			shopBrandDTO.setCid(Long.parseLong(cid_brandid[0]));
			shopBrandDTO.setBrandId(Long.parseLong(cid_brandid[1]));
			shopBrandDTO.setStatus(1);
			shopBrandDTO.setSellerId(userId);
			shopBrandDTO.setShopId(shopId);
			shopBrandDTO.setOperatorId(userId+"");
			shopBrandExportService.addShopBrand(shopBrandDTO);
		}
	}

	/**新增店铺经营的类目信息*/
	@Override
	public List<ShopCategoryDTO> buildShopCategoryList(String addCategoryCids, Long userId, Long shopId) {
		List<ShopCategoryDTO> shopCategoryList = new ArrayList<ShopCategoryDTO>();
		if(StringUtils.isBlank(addCategoryCids)){
			return shopCategoryList;
		}
		String[] cids = addCategoryCids.split("\\,");//新增加的店铺类目的类目id数组
		for(int i=0; i<cids.length; i++){
			ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
			shopCategoryDTO.setCid(Long.parseLong(cids[i]));
			shopCategoryDTO.setCreated(new Date());
			shopCategoryDTO.setModified(new Date());
			shopCategoryDTO.setSellerId(userId);
			shopCategoryDTO.setShopId(shopId);
			shopCategoryDTO.setStatus(ShopCategoryStatus.APPLY.getCode());
			shopCategoryList.add(shopCategoryDTO);
		}
		return shopCategoryList;
	}

	/**新增店铺经营的品牌信息*/
	@Override
	public List<ShopBrandDTO> buildShopBrandList(String addBrandIds, Long userId, Long shopId) {
		List<ShopBrandDTO> shopBrandList = new ArrayList<ShopBrandDTO>();
		if(StringUtils.isBlank(addBrandIds)){
			return shopBrandList;
		}
		String[] cidBrandIds = addBrandIds.split("\\,");//类目品牌id集合，格式为： cid:brandId,cid:brandId
		for(int i=0; i<cidBrandIds.length; i++){
			ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
			String[] cid_brandid = cidBrandIds[i].split("\\:");
			shopBrandDTO.setCid(Long.parseLong(cid_brandid[0]));
			shopBrandDTO.setBrandId(Long.parseLong(cid_brandid[1]));
			shopBrandDTO.setStatus(1);
			shopBrandDTO.setSellerId(userId);
			shopBrandDTO.setShopId(shopId);
			shopBrandDTO.setOperatorId(userId+"");
			shopBrandList.add(shopBrandDTO);
		}
		return shopBrandList;
	}
}
