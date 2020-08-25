package com.camelot.aftersale.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.aftersale.dao.ComplainDAO;
import com.camelot.aftersale.domain.Complain;
import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.aftersale.service.util.AftersaleUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("complainExportService")
public class ComplainExportServiceImpl extends AftersaleUtil  implements ComplainExportService {
	private final static Logger logger = LoggerFactory.getLogger(ComplainExportServiceImpl.class);
	
	@Resource
	private ComplainDAO complainDAO;
	
	@Override
	public ExecuteResult<ComplainDTO> addComplainInfo(ComplainDTO complainDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","ComplainExportServiceImpl-addComplainInfo",JSONObject.toJSONString(complainDTO));
		ExecuteResult<ComplainDTO> result =new ExecuteResult<ComplainDTO>();
		try {
			Complain complain =this.buildComplain(complainDTO);
			complainDAO.add(complain);
			result.setResult(this.buildComplainDTO(complain));
			result.setResultMessage("添加成功");
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.info("\n 方法[{}]，异常：[{}]","ComplainExportServiceImpl-addComplainInfo",e.getMessage());
		}
		logger.info("\n 方法[{}]，出参：[{}]","ComplainExportServiceImpl-addComplainInfo",JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public ExecuteResult<String> modifyComplainInfo(ComplainDTO complainDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","ComplainExportServiceImpl-modifyComplainInfo",JSONObject.toJSONString(complainDTO));
		ExecuteResult<String> result =new ExecuteResult<String>();
		try {
			Complain complain =this.buildComplain(complainDTO);
			complainDAO.update(complain);
			result.setResultMessage("修改成功");
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.info("\n 方法[{}]，异常：[{}]","ComplainExportServiceImpl-modifyComplainInfo",e.getMessage());
		}
		logger.info("\n 方法[{}]，出参：[{}]","ComplainExportServiceImpl-modifyComplainInfo",JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public ExecuteResult<ComplainDTO> findInfoById(Long id) {
		logger.info("\n 方法[{}]，入参：[{}]","ComplainExportServiceImpl-findInfoById",JSONObject.toJSONString(id));
		ExecuteResult<ComplainDTO> result =new ExecuteResult<ComplainDTO>();
		Complain complain = complainDAO.queryById(id);
		if(complain!=null){
			result.setResult(this.buildComplainDTO(complain));
		}
		logger.info("\n 方法[{}]，出参：[{}]","ComplainExportServiceImpl-findInfoById",JSONObject.toJSONString(result));
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<ComplainDTO> findInfoByCondition(ComplainDTO complainDTO,Pager pager) {
		logger.info("\n 方法[{}]，入参：[{}]","ComplainExportServiceImpl-findInfoById",JSONObject.toJSONString(complainDTO));
		DataGrid<ComplainDTO> result =new DataGrid<ComplainDTO>();
		Complain complain = this.buildComplain(complainDTO);
		
		long count =complainDAO.queryCount(complain);
		if(count>0){
			List<ComplainDTO> listComplainDTO= new ArrayList<ComplainDTO>();
			List<Complain> listComplain=complainDAO.queryList(complain, pager);
			for (Complain com : listComplain) {
				listComplainDTO.add(this.buildComplainDTO(com));
			}
			result.setRows(listComplainDTO);
		}
		
		result.setTotal(count);
		logger.info("\n 方法[{}]，出参：[{}]","ComplainExportServiceImpl-findInfoById",JSONObject.toJSONString(result));
		return result;
	}

    @Override
    public List<ComplainDTO> getComplainByCondition(ComplainDTO complainDTO) {
        List<ComplainDTO> res=new ArrayList<ComplainDTO>();
        Complain complain = this.buildComplain(complainDTO);
        
        long count =complainDAO.queryCount(complain);
        if(count>0){
            List<ComplainDTO> listComplainDTO= new ArrayList<ComplainDTO>();
            List<Complain> listComplain=complainDAO.getComplainByCondition(complain);
            for (Complain com : listComplain) {
                listComplainDTO.add(this.buildComplainDTO(com));
            }
           res=listComplainDTO;
        }

        return res;
    }

    /**
     * 根据条件查询投诉信息 (在ECM的投诉仲裁页面列表显示中用到)
     * 
     * @param complainDTO
     * @return
     */
    @SuppressWarnings("rawtypes")
	@Override
	public DataGrid<ComplainDTO> findEarlyComplainInfoByCondition(ComplainDTO complainDTO,Pager pager) {
		logger.info("\n 方法[{}]，入参：[{}]","ComplainExportServiceImpl-findEarlyComplainInfoByCondition",JSONObject.toJSONString(complainDTO));
		DataGrid<ComplainDTO> result =new DataGrid<ComplainDTO>();
		Complain complain = this.buildComplain(complainDTO);
		
		long count =complainDAO.getCountEarlyComplainByCondition(complain);
		if(count>0){
			List<ComplainDTO> listComplainDTO= new ArrayList<ComplainDTO>();
			List<Complain> listComplain=complainDAO.findEarlyComplainListByCondition(complain, pager);
			for (Complain com : listComplain) {
				listComplainDTO.add(this.buildComplainDTO(com));
			}
			result.setRows(listComplainDTO);
		}
		
		result.setTotal(count);
		logger.info("\n 方法[{}]，出参：[{}]","ComplainExportServiceImpl-findEarlyComplainInfoByCondition",JSONObject.toJSONString(result));
		return result;
	}


}
