package com.camelot.ecm.apply;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserContractService;
import com.camelot.usercenter.service.UserExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sevelli on 15-3-4.
 * @description 合同
 * @author sevelli
 * @version 2015-03-05
 */
@Controller
@RequestMapping(value = "${adminPath}/contract")
public class ContractController extends BaseController {
    @Resource
    private UserContractService userContractService;

    /**
     * 合同列表
     * @param contractDTO
     * @param model
     * @param pager
     * @return
     */
    @RequestMapping(value = "list")
    public String list(@ModelAttribute("contract")UserContractDTO contractDTO,Pager pager, Model model){
        if(pager.getPage()<1){
            pager.setPage(1);
        }
        if(pager.getRows()<1){
            pager.setRows(20);
        }
        Page<UserContractDTO> page = new Page<UserContractDTO>();
        DataGrid<UserContractDTO> dataGrid =  userContractService.findListByCondition(contractDTO, pager);
        page.setCount(dataGrid.getTotal());
        page.setList(dataGrid.getRows());
        page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
        model.addAttribute("page",page);
        model.addAttribute("contract",contractDTO);
        model.addAttribute("alltypes", UserEnums.ContractType.values());
        model.addAttribute("allstatus", UserEnums.ContractStatus.values());
//        CommonEnums.ComStatus.values();
        return "apply/contract";
    }

    /**
     * 合同上传后更新合同文件地址
     * @param flag=1时，是重新上传,flag=0新上传
     * @return
     */
    @RequestMapping(value = "updateUrl")
    @ResponseBody
    public Map updateUrl(@ModelAttribute("contract")UserContractDTO contractDTO,Integer flag,HttpServletRequest request){
        Map map = new HashMap();
        if(null==contractDTO.getContractId()||"".equals(contractDTO.getContractId())){
            map.put("success",false);
            map.put("msg","请填写合同编号");
            return map;
        }
        UserContractDTO queryDto = new UserContractDTO();
        queryDto.setContractId(contractDTO.getContractId());
        ExecuteResult<UserContractDTO> resultQuery = userContractService.findUserContractByCondition(queryDto);
        if(flag==0){//新上传
            if(resultQuery.isSuccess()&&resultQuery.getResult()!=null&&resultQuery.getResult().getContractStatus()!=0){
                map.put("success",false);
                map.put("msg","合同"+contractDTO.getContractId()+"已经存在");
                return map;
            }else{
                contractDTO.setContractStatus(UserEnums.ContractStatus.UNPASS.getCode());//合同上传后要修改为待审核状态
            }

        }else {//重新上传

        }
        String userId = UserUtils.getUser().getId();
        
        ExecuteResult<UserContractDTO> result =  userContractService.modifyUserContractById(contractDTO, userId);
        map.put("success",result.isSuccess());
        if (result.isSuccess()){
            map.put("msg","成功");
            map.put("contract",result.getResult());
        }else{
            map.put("msg",result.getErrorMessages());
        }
        request.setAttribute("map", map);
        return map;
    }

    /**
     * 根据id查询合同详细信息并返回
     * @param id
     * @return
     */
    @RequestMapping(value = "info")
    @ResponseBody
    public UserContractDTO info(@RequestParam String id){
        UserContractDTO contractDTO = userContractService.findUserContractById(new Long(id), null);
        return contractDTO;
    }
}
