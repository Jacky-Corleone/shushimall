package com.camelot.goodscenter;

import java.util.ArrayList;
import java.util.List;

import com.camelot.Base;
import com.camelot.goodscenter.dto.ItemSalesVolumeDTO;
import com.camelot.goodscenter.service.ItemSalesVolumeExportService;
import com.camelot.openplatform.common.ExecuteResult;

import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
 
public class ItemSalesVolumeExportServiceImpAlllTest extends Base {

    @Resource
    private ItemSalesVolumeExportService itemSalesVolumeExportService;
    
    @Test
    public void testUpdateItemSalesVolume(){
    	List<ItemSalesVolumeDTO> inList = new ArrayList<ItemSalesVolumeDTO>();
    	ItemSalesVolumeDTO inDTO = new ItemSalesVolumeDTO();
    	inDTO.setItemId(1L);
    	inDTO.setSalesVolume(100);
    	inDTO.setSellerId(1L);
    	inDTO.setShopId(1L);
    	inDTO.setSkuId(1L);
    	inList.add(inDTO);
    	ExecuteResult<String> result = this.itemSalesVolumeExportService.updateItemSalesVolume(inList);
    	Assert.assertNotEquals(null, result);
    }
	
}
