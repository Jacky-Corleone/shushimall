package com.camelot.basecenter.platformservice;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.common.utils.StringUtils;

import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.PlatformServiceRuleDAO;
import com.camelot.basecenter.dto.PlatformServiceRuleDTO;
import com.camelot.basecenter.service.PlatformServiceRuleExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("platformServiceRuleExportService")
public class PlatformServiceRuleServiceImpl implements PlatformServiceRuleExportService {

	@Resource
	private PlatformServiceRuleDAO platformServiceRuleDAO;
	
	/**
	 * <p>Discription:[根据服务规则id数组查询]</p>
	 * Created on 2015年3月19日
	 * @param ruleIds
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<List<PlatformServiceRuleDTO>> queryRuleByRuleIds(Long[] ruleIds){
		ExecuteResult<List<PlatformServiceRuleDTO>> er=new ExecuteResult<List<PlatformServiceRuleDTO>>();
		try{
			er.setResult(platformServiceRuleDAO.queryByIds(ruleIds));
			er.setResultMessage("success");
		}catch(Exception e){
		   er.setResultMessage(e.getMessage());
		   throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据服务规则的id查询详情]</p>
	 * Created on 2015年3月18日
	 * @param ruleId
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<PlatformServiceRuleDTO> queryByRuleId(Long ruleId){
	  ExecuteResult<PlatformServiceRuleDTO> er=new ExecuteResult<PlatformServiceRuleDTO>();
	  try{
		  er.setResult(platformServiceRuleDAO.queryById(ruleId));
		  er.setResultMessage("success");
	  }catch(Exception e){
		  er.setResultMessage(e.getMessage());
		  throw new RuntimeException(e);
	  }
	  return er;
  }
	/**
	 * <p>Discription:[平台服务规则的查询]</p>
	 * Created on 2015年3月9日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<DataGrid<PlatformServiceRuleDTO>> queryList(PlatformServiceRuleDTO dto,Pager page) {
		ExecuteResult<DataGrid<PlatformServiceRuleDTO>> er=new ExecuteResult<DataGrid<PlatformServiceRuleDTO>>();
		DataGrid<PlatformServiceRuleDTO> dg=new DataGrid<PlatformServiceRuleDTO>();
		dg.setRows(platformServiceRuleDAO.queryList(dto, page));
		dg.setTotal(platformServiceRuleDAO.queryCount(dto));
		er.setResult(dg);
		return er;
	}

	/**
	   * <p>Discription:[平台服务规则的修改]</p>
	   * Created on 2015年3月10日
	   * @param dto
	   * @return
	   * @author:[杨芳]
	   */
	  public ExecuteResult<String> modifyPlatformServiceRule(PlatformServiceRuleDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		PlatformServiceRuleDTO psr=platformServiceRuleDAO.queryById(dto.getRuleId());
	    if(psr==null){
	    	er.setResult("该服务规则不存在");
	    }else{
	    	if(dto.getRuleId()!=0){
	    	psr.setRuleId(dto.getRuleId());
	    	}
	    	if(dto.getCreateTime()!=null){
	    	 psr.setCreateTime(dto.getCreateTime());
	    	}
	    	if(StringUtils.isNotEmpty(dto.getCreateUserId()) ){
	    	  psr.setCreateUserId(dto.getCreateUserId());	
	    	}
	    	if(dto.getDetails()!=null && dto.getDetails()!=""){
	    		psr.setDetails(dto.getDetails());
	    	}
	    	if(dto.getHelpDocId()!=0){
	    		psr.setHelpDocId(dto.getHelpDocId());
	    	}
	    	if(dto.getIconImageSrc()!=null && dto.getIconImageSrc()!=""){
	    		psr.setIconImageSrc(dto.getIconImageSrc());
	    	}
	    	if(dto.getPlatformId()!=0){
	    	   psr.setPlatformId(dto.getPlatformId());
	    	}
	    	if(dto.getRuleName()!=null && dto.getRuleName()!=""){
	    		psr.setRuleName(dto.getRuleName());
	    	}
	    	if(dto.getSimpleIntro()!=null && dto.getSimpleIntro()!=""){
	    		psr.setSimpleIntro(dto.getSimpleIntro());
	    	}
	    	platformServiceRuleDAO.updateBySelect(psr);
	    	er.setResult("修改成功！");
	     }
		return er;
	  }
	  /**
	   * <p>Discription:[添加服务规则]</p>
	   * Created on 2015年3月18日
	   * @param dto
	   * @return
	   * @author:[杨芳]
	   */
	  public ExecuteResult<String> addServiceRule(PlatformServiceRuleDTO dto){
		  ExecuteResult<String> er=new ExecuteResult<String>();
		  try{
			  platformServiceRuleDAO.add(dto);
			  er.setResult("添加成功");
		  }catch(Exception e){
			  er.setResult(e.getMessage());
			  throw new RuntimeException(e);
		  }
		  return er;
	  }
  /**
   * <p>Discription:[修改服务规则的状态]</p>
   * @param platformServiceRuleDTO   status字段：0：代表被删除
   * @return
   * @author:[王东晓]
   */
	@Override
	public ExecuteResult<String> modifyPlatformServiceRuleStatus(
			PlatformServiceRuleDTO platformServiceRuleDTO) {
		ExecuteResult<String> er=new ExecuteResult<String>();
		try{
			platformServiceRuleDAO.deleteRuleByid(platformServiceRuleDTO);
			er.setResult("添加成功");
		}catch(Exception e){
			er.setResult(e.getMessage());
			  throw new RuntimeException(e);
		}
		return er;
	}
}
