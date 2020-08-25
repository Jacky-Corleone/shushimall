package com.camelot.ecm.goodscenter;

import com.camelot.ecm.itemcategory.ItemService;
import com.camelot.goodscenter.dto.*;
import com.camelot.goodscenter.service.ItemAttrValueItemExportService;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by jiawg on 2015/2/26 0026.
 */
/**
 * 商品Controller
 * @author jiawg
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/goodscenter")
public class GoodsController extends BaseController {
    @Resource
    private ItemExportService itemExportService;
    @Resource
    private ItemCategoryService itemCategoryService;
    @Resource
    private ItemService itemService;
    @Resource
    private ItemBrandExportService itemBrandExportService;
    @Resource
    private ItemAttrValueItemExportService itemAttrValueItemExportService;

    @ModelAttribute("goods")
    public ItemDTO get(@RequestParam(required=false) String id) {
        if (StringUtils.isNotBlank(id)){
            ExecuteResult<ItemDTO> result =  itemExportService.getItemById(new Long(id));
            if(result!=null){
                return result.getResult();
            }else{
                return new ItemDTO();
            }

        }else{
            return new ItemDTO();
        }
    }

    /**
     * 商品发布
     * @param goods
     * @param model
     * @return
     */
    @RequestMapping(value = "publish")
    public String form(@ModelAttribute("goods")ItemDTO goods,Model model){
    	 DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
         if(dataGrid!=null){
             model.addAttribute("platformList",dataGrid.getRows());
         }else{
             model.addAttribute("platformList",new ArrayList());
         }

         return "goodscenter/publishForm";
    }

    /**
     * 保存商品
     * @param goods
     * @return
     */
//    @ResponseBody
    @RequestMapping(value = "save")
    public ModelAndView save(@ModelAttribute ItemDTO goods,HttpServletRequest  request){
//        Map json = new HashMap();
        goods.setOperator(2);//平台添加
        goods.setHasPrice(1);//有价格
        goods.setItemStatus(4);//未发布
        goods.setGuidePrice(new BigDecimal("0"));
        String allItemSalesAttrString = request.getParameter("allItemSalesAttrString");
        String  allItemAttrString = request.getParameter("allItemAttrString");
        goods.setAttributes(getAttr(allItemAttrString));
        goods.setAttrSale(getAttr(allItemSalesAttrString));
        String path = SysProperties.getProperty("ngIp");
		// 将图片路径保存为相对路径
        goods.setDescribeUrl(goods.getDescribeUrl().replaceAll(path,""));
        ExecuteResult<ItemDTO> result = itemExportService.addItemInfo(goods);
        logger.debug("####"+result.getResultMessage());
//       json.put("success",result.isSuccess());
        if (!result.isSuccess()){
            logger.error("添加商品出现问题"+result.getErrorMessages());
//            json.put("msg",result.getErrorMessages());
        }else {
//            json.put("item",result.getResult());
//            json.put("msg","添加成功");
        }
        logger.debug(request.getServletPath());
        logger.debug(request.getContextPath()+"/admin/goodList/gsList");
        return new ModelAndView("redirect:/admin/goodList/gsList");
    }
    /**
     * 保存商品
     * @param goods
     * @return
     */
    @RequestMapping(value = "editS")
    public String edit(@ModelAttribute ItemDTO goods,HttpServletRequest  request){
        goods.setOperator(2);//平台添加
        goods.setHasPrice(1);//有价格
        goods.setItemStatus(4);//未发布

        String allItemSalesAttrString = request.getParameter("allItemSalesAttrString");
        String  allItemAttrString = request.getParameter("allItemAttrString");
        goods.setAttributes(getAttr(allItemAttrString));
        goods.setAttrSale(getAttr(allItemSalesAttrString));

        ExecuteResult<ItemDTO> result = itemExportService.modifyItemById(goods);
        if(!result.isSuccess()){
            logger.error("error is: {}",result.getErrorMessages());
        }else{

        }
        return "redirect:/admin/goodList/gsList";
    }
    @RequestMapping(value = "editGoods")
    public String editGoods(Long itemId,Model model){
        // 编辑商品
        ExecuteResult<ItemDTO> result =  itemExportService.getItemById(itemId);
        ItemDTO itemDTO = result.getResult();
        model.addAttribute("goods",itemDTO);
        Map<Long,Map> itemMaps = itemService.getAllParent(itemDTO.getCid());
        Map itemMap = itemMaps.get(itemDTO.getCid());
        model.addAttribute("itemMap",itemMap);

        BrandOfShopDTO brandOfShopDTO = new BrandOfShopDTO();
        brandOfShopDTO.setSecondCid((Long)itemMap.get("subcid"));
        brandOfShopDTO.setThirdCid(itemDTO.getCid());
        List<ItemBrandDTO> brandList = itemBrandExportService.queryItemBrandByIds(itemDTO.getBrand()).getResult();
        model.addAttribute("brand", brandList==null?null:brandList.get(0));

        ItemAttrValueItemDTO itemAttrValueItemDTO = new ItemAttrValueItemDTO();
        itemAttrValueItemDTO.setItemId(itemId);
        itemAttrValueItemDTO.setAttrType(1);//销售属性
        ExecuteResult<List<ItemAttr>> result1 = itemAttrValueItemExportService.queryItemAttrValueItem(itemAttrValueItemDTO);
        model.addAttribute("itemSalesAttrList",result1.getResult());

        itemAttrValueItemDTO.setAttrType(2);
        ExecuteResult<List<ItemAttr>> result2 = itemAttrValueItemExportService.queryItemAttrValueItem(itemAttrValueItemDTO);
        model.addAttribute("itemAttrList",result2.getResult());

        return "goodscenter/editForm";
    }

    /**
     *把合成的字符串分解成list
     * @param str 前台传入的合成的属性字符串
     * @return
     */
    private List<ItemAttr> getAttr(String str){
        Map<String,List<ItemAttrValue>> attrMap = new HashMap<String,List<ItemAttrValue>>();
        // 分解前台传入的属性组字符串
        String[] strs = str.split("#");
        for (String s:strs){
            if(s!=null&&!"".equals(s)){
                String[] strs2 = s.split(":");
                if(attrMap.get(strs2[0])==null){
                    List<ItemAttrValue> valueList = new ArrayList<ItemAttrValue>();
                    ItemAttrValue attrValue = new ItemAttrValue();
                    attrValue.setId(new Long(strs2[1]));
                    attrValue.setAttrId(new Long(strs2[0]));
                    valueList.add(attrValue);
                    attrMap.put(strs2[0],valueList);
                }else{
                    List<ItemAttrValue> valueList = attrMap.get(strs2[0]);
                    ItemAttrValue attrValue = new ItemAttrValue();
                    attrValue.setId(new Long(strs2[1]));
                    attrValue.setAttrId(new Long(strs2[0]));
                    valueList.add(attrValue);
                    attrMap.put(strs2[0],valueList);
                }
            }
        }
        List<ItemAttr> list = new ArrayList<ItemAttr>();
        Iterator<Map.Entry<String,List<ItemAttrValue>>> it = attrMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,List<ItemAttrValue>> e =  it.next();
            ItemAttr itemAttr = new ItemAttr();
            itemAttr.setId(new Long(e.getKey()));
            itemAttr.setValues(e.getValue());
            list.add(itemAttr);
        }
        return list;
    }
}
