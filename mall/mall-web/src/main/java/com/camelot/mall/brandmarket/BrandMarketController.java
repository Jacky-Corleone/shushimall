package com.camelot.mall.brandmarket;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

@Controller
@RequestMapping("/brandMarket")
public class BrandMarketController {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private ShopExportService shopService;
	@Resource
	private UserExportService userService;
	@Resource
	private ItemBrandExportService brandService;
	

	
	@RequestMapping( value={"","/"} )
	public String index(){
		return "redirect:/brandMarket/A";
	}
	@RequestMapping( "/{simpleCode}" )
	public String index(@PathVariable String simpleCode, Model model){
		if( "ABCDEFGHIJKLMNOPQRSTUVWXYZ其它".indexOf(simpleCode) == -1 )
			simpleCode = "A";
		model.addAttribute("simpleCode", simpleCode);
		ItemBrandDTO dto = new ItemBrandDTO();
		dto.setBrandKey(simpleCode);
		dto.setBrandStatus(1);
		ExecuteResult<DataGrid<ItemBrandDTO>> er = this.brandService.queryBrandList(dto, null);
		if( er.isSuccess() && er.getResult() != null )
			model.addAttribute("brands", er.getResult().getRows());
		return "/brandmarket/index";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/brand")
	public String shop(String id,Pager page,Model model){
		page.setRows(20);
		model.addAttribute("brandId", id);
		Long brandId = Long.valueOf(id);
		ExecuteResult<DataGrid<ShopDTO>> er = this.shopService.queryShopInfoByBrandId(brandId, page);
		LOG.debug("获取品牌店铺："+JSON.toJSONString(er));
		if( er.isSuccess() && er.getResult() != null ){
			
			JSONArray shops = new JSONArray();
			for( ShopDTO shop: er.getResult().getRows() ){
				
				JSONObject sp = JSON.parseObject(JSON.toJSONString(shop));
				
				UserDTO user = this.userService.queryUserById(shop.getSellerId());
				if( user != null )
					sp.put("sellerName", user.getUname());
				
				shops.add(sp);
			}
			
			page.setTotalCount(er.getResult().getTotal().intValue());
			page.setRecords( shops );
		}
		model.addAttribute("page", page);
		return "/brandmarket/shop";
	}
}
