package com.camelot.mall.service.impl;



import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.service.ItemInfoService;
import com.camelot.openplatform.common.ExecuteResult;

@Service
public class ItemInofSeriveImpl implements ItemInfoService{

	@Resource
	public ItemExportService itemExportService;
	
	@Override
	public ItemDTO getItemInfoById(String id) {
		
		ExecuteResult<ItemDTO> res = itemExportService.getItemById(Long.valueOf(id));

		

		ItemDTO itemDTO = res.getResult();
		
		
		/*
		Item item=new Item();
		item.setItemName("52°茅台集团茅乡酒6 500ml （6瓶套装）ss");
		item.setHasPrice(1);  // 1 有报价 2  没报价
		item.setMarketPrice(BigDecimal.valueOf(200.00)); //市场价
		item.setMarketPrice2(BigDecimal.valueOf(80.00)); //成本价
		item.setProductId(Long.valueOf(11223344));
		//店铺名称  未实现
		item.setTimingListing(new Date());
		item.setWeight(BigDecimal.valueOf(22));
		item.setOrigin("产地中国青岛");
		
		
		item.setDescribeUrl("");
		
		item.setPackingList("这是包装清单");
		item.setAfterService("这是售后服务");
		
		List<SellPrice> listsp=new ArrayList<SellPrice>();
		SellPrice sp=new SellPrice();
		sp.setSellPrice(BigDecimal.valueOf(180.00));
		sp.setMinNum(1);
		sp.setMaxNum(9);
		sp.setAreaId(Long.valueOf(1));
		sp.setAreaName("全国");
		sp.setStepIndex(1);
		listsp.add(sp);
		sp.setSellPrice(BigDecimal.valueOf(160.00));
		sp.setMinNum(9);
		sp.setMaxNum(null);
		sp.setAreaId(Long.valueOf(1));
		sp.setAreaName("全国");
		sp.setStepIndex(1);
		listsp.add(sp);
		item.setSellPrices(listsp);
		
*/
		
		return itemDTO;
	}

/*	@Resource
	public ItemInfoExportService itemInfoExportService;

	@Override
	public ItemDTO getItemInfoById(String id) {
    	ExecuteResult<com.camelot.goodscenter.dto.ItemDTO> res = itemInfoExportService.getItemInfoById(Long.valueOf(id));
		return res.getResult();
	}
*/
}
