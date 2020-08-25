package com.camelot.usercenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserPlatformServiceRuleMybatisDAO;
import com.camelot.usercenter.domain.UserPlatformServiceRule;
import com.camelot.usercenter.dto.userrule.UserPlatformServiceRuleDTO;
import com.camelot.usercenter.service.UserPlatformServiceRuleService;

@Service("userPlatformServiceRuleService")
public class UserPlatformServiceRuleServiceImpl implements UserPlatformServiceRuleService{
	
	@Resource
	private UserPlatformServiceRuleMybatisDAO userPlatformServiceRuleMybatisDAO;

	@Override
	public ExecuteResult<UserPlatformServiceRuleDTO> getUserPlatformServiceRuleById(long id) {
		ExecuteResult<UserPlatformServiceRuleDTO> result=new ExecuteResult<UserPlatformServiceRuleDTO>();
		UserPlatformServiceRule userPlatformRule=userPlatformServiceRuleMybatisDAO.selectById(id);
		UserPlatformServiceRuleDTO dto=new UserPlatformServiceRuleDTO();
		dto.setId(userPlatformRule.getId());
		dto.setCreateTime(userPlatformRule.getCreateTime());
		dto.setIsDeleted(userPlatformRule.getIsDeleted());
		dto.setModifyTime(userPlatformRule.getModifyTime());
		dto.setRuleId(userPlatformRule.getRuleId());
		dto.setUserId(userPlatformRule.getUserId());
		result.setResult(dto);
		return result;
	}

	@Override
	public ExecuteResult<UserPlatformServiceRuleDTO> saveUserPlatformServiceRule(
			UserPlatformServiceRuleDTO userPlatformServiceRuleDTO) {
		UserPlatformServiceRule insertObj=new UserPlatformServiceRule();
		insertObj.setCreateTime(new Date());
		insertObj.setIsDeleted(0); 
		insertObj.setRuleId(userPlatformServiceRuleDTO.getRuleId());
		insertObj.setUserId(userPlatformServiceRuleDTO.getUserId());
		userPlatformServiceRuleMybatisDAO.insert(insertObj);
		ExecuteResult<UserPlatformServiceRuleDTO> result=new ExecuteResult<UserPlatformServiceRuleDTO>();
		result.setResult(userPlatformServiceRuleDTO);
		return result;
	}

	@Override
	public ExecuteResult<UserPlatformServiceRuleDTO> modifyUserPlatformServiceRule(
			UserPlatformServiceRuleDTO userPlatformServiceRuleDTO) {
		 ExecuteResult<UserPlatformServiceRuleDTO> result=new  ExecuteResult<UserPlatformServiceRuleDTO>();
		
		UserPlatformServiceRule updateObj=new UserPlatformServiceRule();
		updateObj.setModifyTime(new Date());
		updateObj.setIsDeleted(userPlatformServiceRuleDTO.getIsDeleted()); 
		updateObj.setId(userPlatformServiceRuleDTO.getId());
		updateObj.setRuleId(userPlatformServiceRuleDTO.getRuleId());
		updateObj.setUserId(userPlatformServiceRuleDTO.getUserId());
		
		Integer count=userPlatformServiceRuleMybatisDAO.update(updateObj);
		if(count>0){
			result.setResultMessage("修改成功");
		}else{
			result.setResultMessage("修改失败");
		}
		result.setResult(userPlatformServiceRuleDTO);
		return result;
	}

	@Override
	public ExecuteResult<List<UserPlatformServiceRuleDTO>> getUserPlatformRuleList(String userId) {
		ExecuteResult<List<UserPlatformServiceRuleDTO>> result=new ExecuteResult<List<UserPlatformServiceRuleDTO>>();
		List<UserPlatformServiceRuleDTO> ruleDtoList=new ArrayList<UserPlatformServiceRuleDTO>();
		List<UserPlatformServiceRule> ruleList= userPlatformServiceRuleMybatisDAO.getUserPlatformRuleListByUserId(userId);
		for(UserPlatformServiceRule userRule:ruleList){
			UserPlatformServiceRuleDTO dto=new UserPlatformServiceRuleDTO();
			dto.setCreateTime(userRule.getCreateTime());
			dto.setId(userRule.getId());
			dto.setIsDeleted(userRule.getIsDeleted());
			dto.setModifyTime(userRule.getModifyTime());
			dto.setRuleId(userRule.getRuleId());
			dto.setUserId(userRule.getUserId());
			ruleDtoList.add(dto);
		}
		result.setResult(ruleDtoList);
		return result;
	}

    @Override
    public ExecuteResult<List<UserPlatformServiceRuleDTO>> getUserPlatformRuleList(String[] userIds) {
        ExecuteResult<List<UserPlatformServiceRuleDTO>> result=new ExecuteResult<List<UserPlatformServiceRuleDTO>>();
        List<UserPlatformServiceRuleDTO> ruleDtoList=new ArrayList<UserPlatformServiceRuleDTO>();
        if(userIds!=null && userIds.length>0){
            List<UserPlatformServiceRule> ruleList= userPlatformServiceRuleMybatisDAO.getUserPlatformRuleListByUserIds(userIds);
            for(UserPlatformServiceRule userRule:ruleList){
                UserPlatformServiceRuleDTO dto=new UserPlatformServiceRuleDTO();
                dto.setCreateTime(userRule.getCreateTime());
                dto.setId(userRule.getId());
                dto.setIsDeleted(userRule.getIsDeleted());
                dto.setModifyTime(userRule.getModifyTime());
                dto.setRuleId(userRule.getRuleId());
                dto.setUserId(userRule.getUserId());
                ruleDtoList.add(dto);
            }
        }
        result.setResult(ruleDtoList);
        return result;
    }

	/**
	 * 
	 * <p>Discription:[用户  退出平台规则 逻辑删除]</p>
	 * Created on 2015-3-11
	 * @param userPlatformServiceRuleDTO
	 * @return
	 * @author:[创建者中文名字]
	 */
	@Override
	public ExecuteResult<Integer> delUserPlatformService(UserPlatformServiceRuleDTO userPlatformServiceRuleDTO) {
		 ExecuteResult<Integer> result=new  ExecuteResult<Integer>();
			
			UserPlatformServiceRule updateObj=new UserPlatformServiceRule();
			updateObj.setModifyTime(new Date());
			updateObj.setIsDeleted(1); 
			if(userPlatformServiceRuleDTO.getRuleId()!=null&&userPlatformServiceRuleDTO.getUserId()!=null){
				updateObj.setRuleId(userPlatformServiceRuleDTO.getRuleId());
				updateObj.setUserId(userPlatformServiceRuleDTO.getUserId());
				
				Integer count=userPlatformServiceRuleMybatisDAO.updateUserRuleByUserIdAndRuleId(updateObj);
				if(count>0){
					result.setResultMessage("修改成功");
				}else{
					result.setResultMessage("修改失败");
				}
				result.setResult(count);
			}else{
				result.setResult(0);
				result.setResultMessage("用户ID 和 平台规则ID 不能为空");
				
			}
			
			return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<UserPlatformServiceRuleDTO> getUserPlatformRuleStatistics(UserPlatformServiceRuleDTO dto,
			Pager pager) {
		DataGrid<UserPlatformServiceRuleDTO> res=new DataGrid<UserPlatformServiceRuleDTO>();
		UserPlatformServiceRule rule=new UserPlatformServiceRule();
		
		List<UserPlatformServiceRule> ruleList=	userPlatformServiceRuleMybatisDAO.selectRuleStatistics(rule,pager);
		Long count=	userPlatformServiceRuleMybatisDAO.selectRuleStatisticsCount(rule);
		
		List<UserPlatformServiceRuleDTO> resList=new ArrayList<UserPlatformServiceRuleDTO>();
		if(ruleList!=null&&ruleList.size()>0){
			for(UserPlatformServiceRule item:ruleList){
				UserPlatformServiceRuleDTO dotItem=new UserPlatformServiceRuleDTO();
				dotItem.setRuleId(item.getRuleId());
				dotItem.setUserCount(item.getUserCount());
				
				resList.add(dotItem);
			}
		}
		res.setRows(resList);
		res.setTotal(count);
		return  res;
	}

	@Override
	public DataGrid<UserPlatformServiceRuleDTO> getUserPlatformRuleDetail(UserPlatformServiceRuleDTO dto, Pager pager) {
		DataGrid<UserPlatformServiceRuleDTO> res=new DataGrid<UserPlatformServiceRuleDTO>();
		UserPlatformServiceRule rule=new UserPlatformServiceRule();
		
		rule.setCreateTimeBegin(dto.getCreateTimeBegin());
		rule.setCreateTimeEnd(dto.getCreateTimeBegin());
		rule.setRuleId(dto.getRuleId());
		
		List<UserPlatformServiceRule> ruleList=	userPlatformServiceRuleMybatisDAO.selectRuleDetails(rule,pager);
		Long count=	userPlatformServiceRuleMybatisDAO.selectRuleDetailsCount(rule);
		
		List<UserPlatformServiceRuleDTO> resList=new ArrayList<UserPlatformServiceRuleDTO>();
		if(ruleList!=null&&ruleList.size()>0){
			for(UserPlatformServiceRule item:ruleList){
				UserPlatformServiceRuleDTO dotItem=new UserPlatformServiceRuleDTO();
				dotItem.setRuleId(item.getRuleId());
				dotItem.setUserId(item.getUserId());
				dotItem.setCompanyName(item.getCompanyName());
				dotItem.setCreateTime(item.getCreateTime());
				
				resList.add(dotItem);
			}
		}
		res.setRows(resList);
		res.setTotal(count);
		return  res;
	}

	
	
	
}
