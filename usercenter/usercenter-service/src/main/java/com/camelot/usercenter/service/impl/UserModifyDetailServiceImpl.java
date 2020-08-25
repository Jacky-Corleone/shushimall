package com.camelot.usercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserModifyDetailMybatisDAO;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.service.UserModifyDetailService;

@Service("userModifyDetailService")
public class UserModifyDetailServiceImpl implements UserModifyDetailService{
	@Resource
	UserModifyDetailMybatisDAO userModifyDetailMybatisDAO;
	
	@Override
	public DataGrid<UserModifyDetailDTO> getUserModifyDetailList(UserModifyDetailDTO userModifyDetailDTO, Pager pager) {
		DataGrid<UserModifyDetailDTO>  res=new DataGrid<UserModifyDetailDTO>();
		List<UserModifyDetailDTO> rows=userModifyDetailMybatisDAO.selectList(userModifyDetailDTO, pager);
		long count=userModifyDetailMybatisDAO.selectCount(userModifyDetailDTO);
		res.setRows(rows);
		res.setTotal(count);
		return res;
		
	}
	@Override
	public boolean updateModfiyAllTablesAndCoumn(Long modifyInfoId,String remark,String reviewUserId) {
		boolean res=false;
		try{
		Pager pager=new Pager();
		UserModifyDetailDTO userModifyDetailDTO=new UserModifyDetailDTO();
		userModifyDetailDTO.setModifyInfoId(modifyInfoId);
		
		List<UserModifyDetailDTO> modfyDetailList=userModifyDetailMybatisDAO.selectList(userModifyDetailDTO, pager);
		if(modfyDetailList!=null&&modfyDetailList.size()>0){
			Integer allRes=0;
			for(UserModifyDetailDTO userDetail:modfyDetailList){
				if(StringUtils.isNotBlank(userDetail.getTableName())&&StringUtils.isNotBlank(userDetail.getColumnName())&&
						StringUtils.isNotBlank(userDetail.getAfterChangeValue())&&StringUtils.isNotBlank(userDetail.getChangeValueId())){
					UserModifyDetailDTO dto=new UserModifyDetailDTO();
					dto.setTableName(userDetail.getTableName());
					dto.setColumnName(userDetail.getColumnName());
					dto.setAfterChangeValue(userDetail.getAfterChangeValue());
					dto.setReviewReason(remark);
					dto.setReviewUserId(reviewUserId);
					dto.setChangeValueId(userDetail.getChangeValueId());
					dto.setChangeColumnName(userDetail.getChangeColumnName());
					Integer detailRes=userModifyDetailMybatisDAO.updateTableColumnValue(dto);
					allRes=allRes+detailRes;
				}
				
			}
			if(allRes<modfyDetailList.size()){
				throw new RuntimeException();
			}else{
				res=true;
			}
		}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return res;
	}
	
	
}
