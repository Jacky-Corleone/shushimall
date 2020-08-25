package com.camelot.usercenter.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.util.DateUtil;
import com.camelot.common.util.ErrorUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserContractAuditMybatisDAO;
import com.camelot.usercenter.dao.UserContractMybatisDAO;
import com.camelot.usercenter.dto.contract.UserContractAuditDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.service.UserContractService;

@SuppressWarnings("rawtypes")
@Service("userContractService")
public class UserContractServiceImpl implements UserContractService {
	private final static Logger logger = LoggerFactory.getLogger(UserExtendsServiceImpl.class);
	@Resource
	UserContractMybatisDAO userContractMybatisDAO;
	@Resource
	UserContractAuditMybatisDAO userContractAuditMybatisDAO;
	
	@Override
	public ExecuteResult<UserContractDTO> modifyUserContractById(UserContractDTO userContractDTO,String currentUserId) {
		logger.info("方法[{}]，入参：[{}]","UserContractService-modifyUserContractById",JSONObject.toJSON(userContractDTO));
		ExecuteResult<UserContractDTO> result =new ExecuteResult<UserContractDTO>();
		try{
			Integer count =userContractMybatisDAO.update(userContractDTO);
			if(count>0){

				if(userContractDTO.getContractStatus()!=null){
					UserContractAuditDTO dto=new UserContractAuditDTO();
					dto.setCid(userContractDTO.getId());
					dto.setAuditDate(new Date());
					dto.setAuditId(currentUserId);
					dto.setStatus(userContractDTO.getContractStatus());
                    dto.setRemark(userContractDTO.getAuditRemark());
					userContractAuditMybatisDAO.insert(dto);
				}
				result.setResultMessage("修改成功");
			}else{
				result.setResultMessage("修改失败");
			}
            UserContractDTO  resUserContractDTO= userContractMybatisDAO.selectById(userContractDTO.getId());
			result.setResult(resUserContractDTO);
			logger.info("方法[{}]，出参：[{}]","UserContractService-modifyUserContractById",JSONObject.toJSON(result));
		} catch (Exception e) {
			result.getErrorMessages().add(ErrorUtil.buildErrorMsg("修改异常", e.getMessage()));
			logger.info("方法[{}]，异常：[{}]","UserContractService-modifyUserContractById",e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@Override
	public UserContractDTO findUserContractById(Long id,Pager pager) {
		logger.info("方法[{}]，入参：[{}][{}]","UserContractService-modifyUserContractById",id,pager);
		UserContractDTO userContractDTO=userContractMybatisDAO.selectById(id);
		if(userContractDTO!=null){
			DataGrid<UserContractAuditDTO> dataGridContractAudit = findAuditListByCId(userContractDTO.getId(), new Pager());
			userContractDTO.setDataGridContractAudit(dataGridContractAudit);
		}
		logger.info("方法[{}]，出参：[{}]","UserContractService-modifyUserContractById",JSONObject.toJSON(userContractDTO));
		return userContractDTO;
	}

	
	@Override
	public DataGrid<UserContractDTO> findListByCondition(UserContractDTO userContractDTO, Pager pager) {
		logger.info("方法[{}]，入参：[{}][{}]","UserContractService-findListByCondition",JSONObject.toJSON(userContractDTO),pager);
		DataGrid<UserContractDTO> dataGrid=new DataGrid<UserContractDTO>();
		if(userContractDTO!=null&&StringUtils.isNotBlank(userContractDTO.getCreatedEnd())){
			userContractDTO.setCreatedEnd(DateUtil.addDateStr(userContractDTO.getCreatedEnd(), 1, null));
		}
		long size=userContractMybatisDAO.selectCount(userContractDTO);
		if(size>0){
			List<UserContractDTO> listUserContractDTO=userContractMybatisDAO.selectList(userContractDTO, pager);
			dataGrid.setRows(listUserContractDTO);
			dataGrid.setTotal(size);
		}
		logger.info("方法[{}]，出参：[{}]","UserContractService-findUserCompanyByUId",JSONObject.toJSON(dataGrid));
		return dataGrid;
	}
	
	@Override
	public DataGrid<UserContractAuditDTO> findAuditListByCId(Long cid, Pager pager) {
		logger.info("方法[{}]，入参：[{}][{}]","UserContractService-findAuditListByCId",cid,pager);
		DataGrid<UserContractAuditDTO> dataGrid=new DataGrid<UserContractAuditDTO>();
		long size=userContractAuditMybatisDAO.selectCount(cid);
		if(size>0){
			List<UserContractAuditDTO> listUserContractAudit =userContractAuditMybatisDAO.selectList(cid, pager);
			dataGrid.setRows(listUserContractAudit);
			dataGrid.setTotal(size);
		}
		logger.info("方法[{}]，出参：[{}]","UserContractService-findAuditListByCId",JSONObject.toJSON(dataGrid));
		return dataGrid;
	}

	@Override
	public ExecuteResult<UserContractDTO> findUserContractByCondition(UserContractDTO userContractDTO) {
		logger.info("方法[{}]，入参：[{}][{}]","UserContractService-findAuditListByCId",userContractDTO);
		Pager pager=new Pager();
		List<UserContractDTO> listUserContractDTO=userContractMybatisDAO.selectList(userContractDTO, pager);
		ExecuteResult<UserContractDTO> res=new ExecuteResult<UserContractDTO>();
		if(listUserContractDTO!=null&&listUserContractDTO.size()>0){
			res.setResult(listUserContractDTO.get(0));
		}
		logger.info("方法[{}]，出参：[{}]","UserContractService-findAuditListByCId",JSONObject.toJSON(res));
		return res;
	}
	/**
	 * 
	 * <p>Discription:[创建或者更新 用户合同]</p>
	 * Created on 2015-3-20
	 * @param userContract
	 * @return
	 * @author:[liuqingshan]
	 */
	@Override
	public ExecuteResult<UserContractDTO> createOrUpdateContract(UserContractDTO userContract) {
		ExecuteResult<UserContractDTO> res=new ExecuteResult<UserContractDTO>();
		if(userContract!=null){
			if(userContract.getCreatorId()!=null){
				List<UserContractDTO> dto=userContractMybatisDAO.selectByUId(userContract.getCreatorId());
				if(dto!=null&&dto.size()>0){
					userContractMybatisDAO.update(userContract);
				}
				else{
					userContractMybatisDAO.insert(userContract);
				}
			}
		}
		res.setResult(userContract);
		return res;
	}

    @Override
    public ExecuteResult<String> deleteUserContractById(Long id) {
        ExecuteResult<String> res=new ExecuteResult<String>();
        userContractMybatisDAO.delete(id);
        return res;
    }
}
