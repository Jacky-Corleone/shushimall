package com.camelot.ecm.apply;

import com.camelot.ecm.itemcategory.ItemService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sevelli on 15-3-6.
 * @description 店铺缴费信息管理
 */
@Controller
@RequestMapping(value = "${adminPath}/shopFee")
public class ShopFeeController extends BaseController {
    @Resource
    private UserExportService userExportService;

    @Resource
    private SattleCatExpenseExportService sattleCatExpenseExportService;
    @Resource
    private ShopCategoryExportService shopCategoryExportService;
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private ItemService itemService;

    @RequestMapping(value = "list")
    public String list(@ModelAttribute("user")UserDTO user,Pager<UserDTO> pager,Model model){
        if(pager.getPage()<1){
            pager.setPage(1);
        }
        if(pager.getRows()<1){
            pager.setRows(20);
        }


        Pager pager2 = new Pager();
        pager2.setRows(Integer.MAX_VALUE);
        pager2.setPage(1);
        ShopDTO shopDTO = new ShopDTO();
//        shopDTO.setStatus(2);
        Integer[] status = new Integer[]{2,5};
        shopDTO.setStatuss(status);
        ExecuteResult<DataGrid<ShopDTO>> results = shopExportService.findShopInfoByCondition(shopDTO,pager2);
        List<ShopDTO> shopDTOList = null;
        Long[] shopIds = null;
        Map<Long,ShopDTO> map = new HashMap<Long,ShopDTO>();
        if(results.getResult()!=null&&results.getResult().getRows()!=null){
            shopDTOList = results.getResult().getRows();
            shopIds = new Long[shopDTOList.size()];
            int i = 0;
            for (ShopDTO shopDTO1:shopDTOList){
                map.put(shopDTO1.getShopId(),shopDTO1);
                shopIds[i] = shopDTO1.getShopId();
                i++;
            }

        }
        Page<UserDTO> page = new Page<UserDTO>();


        if(shopIds!=null&&shopIds.length>0){
            user.setShopIds(shopIds);
            DataGrid<UserDTO> dataGrid = userExportService.findUserListByCondition(user, UserEnums.UserType.SELLER,pager);
            page.setCount(dataGrid.getTotal());
            List<UserDTO> users = dataGrid.getRows();
            page.setList(users);
        }else{
            page.setCount(0);
            page.setList(new ArrayList<UserDTO>());
        }

        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        model.addAttribute("page",page);
        model.addAttribute("user",user);
        return "apply/shopFee";
    }
    @RequestMapping(value = "updateShopFee")
    @ResponseBody
    public Map updateShopFee(Long shopId,HttpServletRequest request){
        Map map = new HashMap();
        UserDTO user = new UserDTO();
        user.setShopId(shopId);
        DataGrid<UserDTO> dataGrid = userExportService.findUserListByCondition(user, UserEnums.UserType.SELLER,null);
        if (dataGrid!=null&&dataGrid.getRows()!=null){
            List<UserDTO> userDTOList = dataGrid.getRows();
            //开通店铺，成功以后调用用户状态修改，失败了再把店铺关掉
//            ExecuteResult<String> r1 =  shopExportService.modifyShopRunStatus(shopId, 1);
            ExecuteResult<String> r1 = shopExportService.modifyShopstatusAndRunstatus(shopId);
            if(r1.isSuccess()){
            	map.put("uid", userDTOList.get(0).getUid());
                ExecuteResult<String> result =  userExportService.modifyPayStatusByUId(userDTOList.get(0).getUid(), CommonEnums.ComStatus.PASS);
                map.put("success",result.isSuccess());
                if(result.isSuccess()){
                    map.put("msg","确认缴费成功，用户店铺开通");
                    request.setAttribute("map",map);
                }else {
                    map.put("msg", "确认缴费失败：" + result.getErrorMessages());
//                    shopExportService.modifyShopRunStatus(shopId, 2);//关闭店铺
                    // 修改店铺
                    shopExportService.modifyShopstatusAndRunstatusBack(shopId);
                }
            }else{
                map.put("success",false);
                map.put("msg","处理卖家店铺信息出错："+r1.getErrorMessages());
            }


        }else{
            map.put("success",false);
            map.put("msg","未查询到对应用户");
        }

        return map;
    }

    /**
     * 根据店铺查询出费用
     * @param shopId
     * @return
     */
    @RequestMapping(value = "feeInfo")
    @ResponseBody
    public Map feeInfo(Long shopId){
        ExecuteResult<ShopDTO> result = shopExportService.findShopInfoById(shopId);
        ShopDTO shopDTO = result.getResult();
        Map map = new HashMap();
        //根据店铺查询出类目
        ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
        shopCategoryDTO.setShopId(shopId);
//        shopCategoryDTO.setStatus(shopDTO.getStatus());
        ExecuteResult<DataGrid<ShopCategoryDTO>> resultd  = shopCategoryExportService.queryShopCategoryAll(shopCategoryDTO,null);
        if(resultd!=null&&resultd.getResult()!=null&&resultd.getResult().getRows()!=null){
            DataGrid<ShopCategoryDTO> shopC = resultd.getResult();
            int len = shopC.getRows().size();
            Long[] ids = new Long[len];
            for (int i=0;i<len;i++){
                ids[i] = shopC.getRows().get(i).getCid();
            }
            //查询出父类目封装到map

            Map<Long,Map> itemMap = itemService.getAllParent(ids);

            //根据类目ID查询保证金和反点
            ExecuteResult<List<SettleCatExpenseDTO>> sceresult = sattleCatExpenseExportService.queryByIds(ids);
            List<Map> feeList = new ArrayList<Map>();

            for (SettleCatExpenseDTO scDTO:sceresult.getResult()){
                Map m = itemMap.get(scDTO.getCategoryId());
                Map sMap = new HashMap();
                sMap.put("rebateRate",scDTO.getRebateRate());
                sMap.put("serviceFee",scDTO.getServiceFee());
                sMap.put("cashDeposit",scDTO.getCashDeposit());

                sMap.put("cid",m.get("cid"));
                sMap.put("cname",m.get("cname"));
                sMap.put("subcid",m.get("subcid"));
                sMap.put("subcname",m.get("subcname"));
                sMap.put("tcid",m.get("tcid"));
                sMap.put("tcname",m.get("tcname"));
                feeList.add(sMap);
            }
            map.put("feeList",feeList);
        }
        return map;
    }
}
