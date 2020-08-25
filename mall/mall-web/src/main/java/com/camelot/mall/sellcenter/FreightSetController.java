package com.camelot.mall.sellcenter;


import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFareDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFareExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.JsonHelper;
import com.camelot.util.WebUtil;
import org.springframework.web.bind.annotation.ResponseBody;

/** 
 * <p>Description: [运费模板设置]</p>
 * Created on 2015年3月18日
 * @author <a href="mailto: guochao@camelotchina.com">呙超</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/freightSet")
public class FreightSetController {
	private Logger LOG = LoggerFactory.getLogger(FreightSetController.class);
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private ShopExportService shopExportService;

	@Resource
	private ShopFareExportService shopFareExportService ;
	
	/**
	 * 
	 * <p>Discription:[运费设置主页]</p>
	 * Created on 2015年3月13日
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/freightList")
	public String freightSetList(Integer page,HttpServletRequest request, Model model){
		
		/*** 取shopid ***/
		long uid = WebUtil.getInstance().getUserId(request);
		long shopId = WebUtil.getInstance().getShopId(request);
		UserDTO user = userExportService.queryUserById(uid);
		
		ShopFareDTO dto = new ShopFareDTO();
		dto.setShopId(shopId);
		//dto.setSellerId(uid);
		Pager<ShopFareDTO> pager = new Pager<ShopFareDTO>() ;
		if(page == null){
			page = 1;
		}
		pager.setPage(page);
		
		DataGrid<ShopFareDTO> dataGrid = shopFareExportService.queryShopFareList(dto, pager);
		
		pager.setTotalCount(dataGrid.getTotal().intValue());
		pager.setRecords(dataGrid.getRows() );
		
		if (pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
			pager.setEndPageIndex(pager.getStartPageIndex());
		}
		
		model.addAttribute("pager", pager);
		model.addAttribute("userName", user.getUname()); //取店铺名
		return "/sellcenter/shop/freightSet";
	}
	
	/**
	 * 
	 * <p>Discription:[运费设置，城市选择校验]</p>
	 * Created on 2015年3月23日
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/freightCheck")
    @ResponseBody
	public Map<String, String> freightCheckAction(HttpServletResponse response,
			String checkbox, HttpServletRequest request,String city_id) {
		Map<String, String> map = new HashMap<String, String>();
        map.put("result","failure");
        if(StringUtils.isBlank(checkbox)){
            return map;
        }
        /*** 取userid，shopid ***/
		long uid = WebUtil.getInstance().getUserId(request);
		long shopId = WebUtil.getInstance().getShopId(request);
		try {
            ShopFareDTO shopFareDTO = new ShopFareDTO();
            shopFareDTO.setShopId(shopId);
            Pager<ShopFareDTO> pager = new Pager<ShopFareDTO>() ;
            DataGrid<ShopFareDTO> dataGrid = shopFareExportService.queryShopFareList(shopFareDTO, null);
            if(dataGrid==null || dataGrid.getRows()==null || dataGrid.getRows().size()==0){
                map.put("result", "success");
            }
            //存储店铺所有的模版省份id
            List<String> provinceIds = buildShopExistFareRegionIds(dataGrid);
            String[] pids = checkbox.split(",");
            for(int i=0; i<pids.length; i++){
                if(provinceIds.contains(pids[i])){
                    return map;
                }
            }
            map.put("result", "success");
            return map;
		} catch (Exception e) {
		    LOG.error(e.toString());
		}
        return map;
	}

    /**
     * 组装店铺省份信息的id集合
     * @param dataGrid
     * @return
     */
    private List<String> buildShopExistFareRegionIds(DataGrid<ShopFareDTO> dataGrid) {
        List<String> provinceIds = new ArrayList<String>();
        List<ShopFareDTO> shopFareList = dataGrid.getRows();
        for(int i=0; i<shopFareList.size(); i++){
            ShopFareDTO sfdto = shopFareList.get(i);
            String fareRegion = sfdto.getFareRegion();
            String[] fareRegionArr = fareRegion.split("\\,");
            for(int j=0; j<fareRegionArr.length; j++){
                provinceIds.add(fareRegionArr[j]);
            }
        }
        return provinceIds;
    }

    /**
	 * 
	 * <p>Discription:[运费设置操作功能，增删改考]</p>
	 * Created on 2015年3月14日
	 * @param request
	 * @param model
	 * @param uid
	 * @return
	 * @author:[GC呙超]
	 */
	@RequestMapping("/freightAction")
	public String freightSetAction(HttpServletRequest request,HttpServletResponse response,Model model, String setData, String action) {
		ExecuteResult<String> executeResult = null;
		if (action.equals("delete")||action.equals("copy")) {
			if(action.equals("delete")){
				executeResult = shopFareExportService.deleteShopFareById(Long.parseLong(setData));
			}else {
				executeResult = shopFareExportService.copyShopFare(Long.parseLong(setData));
			}
		}else if (action.equals("save") ) {	//修改与新增时都传save
            String[] datas = setData.split("&");
            long uid = WebUtil.getInstance().getUserId(request);
            long shopId = WebUtil.getInstance().getShopId(request);

            //校验店铺的某个地区的运费模版是否已经设置过
            ShopFareDTO sfdto = new ShopFareDTO();
            sfdto.setFareRegion(datas[2]);
            sfdto.setShopId(shopId);
            DataGrid<ShopFareDTO> result = shopFareExportService.queryShopFareList(sfdto, null);
            if(datas[0].length()<1 && result!=null && result.getRows()!=null && result.getRows().size()>0){
                JsonHelper.failure(response);
                return null;
            }
            ShopFareDTO dto = new ShopFareDTO();
            dto.setFareName(datas[1]);
            dto.setFareRegion(datas[2]);
            dto.setFirstWeightPrice(new BigDecimal(datas[4]));
			if(datas[0].length()<1){
				dto.setShopId(shopId);//新增时存入shopid
				dto.setSellerId(uid);
				executeResult = shopFareExportService.addShopFare(dto);
			}else{
				dto.setId(Long.parseLong(datas[0]) );//修改时需要传入id
				executeResult = shopFareExportService.modifyShopFareById(dto);
			}
			
		}

		JsonHelper.success(response);
		return null;
	}
}