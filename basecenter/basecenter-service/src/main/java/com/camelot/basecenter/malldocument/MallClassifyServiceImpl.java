package com.camelot.basecenter.malldocument;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.MallClassifyDAO;
import com.camelot.basecenter.domain.MallClassify;
import com.camelot.basecenter.dto.MallClassifyDTO;
import com.camelot.basecenter.service.MallClassifyService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("mallClassifyService")
public class MallClassifyServiceImpl implements MallClassifyService {
	private final static Logger logger = LoggerFactory.getLogger(MallClassifyServiceImpl.class);
	
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年1月26日
	 * @param mallCassifyDTO
	 * @return
	 * @author:[创建者中文名字]
	 */
	@Resource
	private MallClassifyDAO mallClassifyDAO;

	@Override
	public DataGrid<MallClassifyDTO> queryMallCassifyList(MallClassifyDTO mallCassifyDTO,Pager page) {
		
		DataGrid<MallClassifyDTO> dg=new DataGrid<MallClassifyDTO>();
		dg.setRows(mallClassifyDAO.queryMallCassifyList(mallCassifyDTO,page));
		dg.setTotal(mallClassifyDAO.queryMallCassifyCount(mallCassifyDTO));
		return dg;
	}

	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年1月26日
	 * @param mallClassifyDTO
	 * @return
	 * @author:[liuqsh]
	 */
	
	@Override
	public ExecuteResult<String> addMallCassify(MallClassifyDTO mallClassifyDTO) {
		ExecuteResult<String> er=new ExecuteResult<String>();
		try {
			MallClassify insertObj=new MallClassify();
			insertObj.setCreated(new Date());
			insertObj.setEndTime(mallClassifyDTO.getEndTime());
			insertObj.setModified(mallClassifyDTO.getModified());
			insertObj.setPlatformId(mallClassifyDTO.getPlatformId());
			insertObj.setStartTime(mallClassifyDTO.getStartTime());
			insertObj.setStatus(mallClassifyDTO.getStatus());
			insertObj.setTitle(mallClassifyDTO.getTitle());
			insertObj.setType(mallClassifyDTO.getType());
//			MallClassify mallClassify=EntityTranslator.transObj(mallClassifyDTO,MallClassify.class,false );
			if(mallClassifyDAO.addMallCassify(insertObj)>0){
				er.setResult("操作成功");
			}else {
				er.addErrorMsg("操作失败");
			}
		} catch (Exception e) {
			er.addErrorMsg("操作失败");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年1月26日
	 * @param mallCassifyDTO
	 * @return
	 * @author:[创建者中文名字]
	 */
	//文档分类修改
	@Override
	public ExecuteResult<String> modifyInfoById(MallClassifyDTO mallCassifyDTO) {
		ExecuteResult<String> er =new ExecuteResult<String>();
		try {
			MallClassify updateObj=new MallClassify();
			updateObj.setEndTime(mallCassifyDTO.getEndTime());
			updateObj.setId(mallCassifyDTO.getId());
			updateObj.setIsDeleted(mallCassifyDTO.getIsDeleted());
			updateObj.setModified(new Date());
			updateObj.setPlatformId(mallCassifyDTO.getPlatformId());
			updateObj.setStartTime(mallCassifyDTO.getStartTime());
			updateObj.setStatus(mallCassifyDTO.getStatus());
			updateObj.setTitle(mallCassifyDTO.getTitle());
			updateObj.setType(mallCassifyDTO.getType());
			
//			MallClassify mc=EntityTranslator.transDTOToDomin(MallClassify.class, mallCassifyDTO);
//			mc.setModified(new Date());
			mallClassifyDAO.update(updateObj);
			er.setResultMessage("修改成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
			er.addErrorMsg("修改失败");
			throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年1月26日
	 * @param id
	 * @param status 分类状态 1，已下架 2，已发布
	 * @return
	 * @author:[创建者中文名字]
	 */
	
	@Override
	public ExecuteResult<String> modifyStatusById(int id, int status) {
		ExecuteResult<String> er=new ExecuteResult<String>();
		if(mallClassifyDAO.modifyStatusById(id, status)>0){
			er.setResult("操作成功");;
		}else{
			er.addErrorMsg("操作失败");
		}
		return er;
	}

}
