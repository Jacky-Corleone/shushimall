package com.camelot.ecm.itemcategory;

import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.ExecuteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sevelli on 15-3-20.
 */
@Service("itemService")
public class ItemService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ItemCategoryService itemCategoryService;
    public Map<Long,Map> getAllParent(Long... ids){
        Map<Long,Map> itemMap = new HashMap<Long,Map>();
        ExecuteResult<List<ItemCatCascadeDTO>> items = itemCategoryService.queryParentCategoryList(ids);

        for (ItemCatCascadeDTO item:items.getResult()){
            for (ItemCatCascadeDTO subItem:item.getChildCats()){
                for (ItemCatCascadeDTO tItem:subItem.getChildCats()){
                    Map tmap = new HashMap();
                    tmap.put("cid",item.getCid());
                    tmap.put("cname",item.getCname());
                    tmap.put("subcid",subItem.getCid());
                    tmap.put("subcname",subItem.getCname());

                    tmap.put("tcid",tItem.getCid());
                    tmap.put("tcname",tItem.getCname());
                    itemMap.put(tItem.getCid(),tmap);
                }
            }

        }
        return itemMap;
    }
}
