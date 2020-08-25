package com.camelot.basecenter.growth;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.GrowthDAO;
import com.camelot.basecenter.dao.MallClassifyDAO;
import com.camelot.basecenter.domain.Growth;
import com.camelot.basecenter.domain.MallClassify;
import com.camelot.basecenter.dto.GrowthDTO;
import com.camelot.basecenter.dto.MallClassifyDTO;
import com.camelot.basecenter.service.GrowthService;
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
@Service("growthService")
public class GrowthServiceImpl implements GrowthService {
	private final static Logger logger = LoggerFactory.getLogger(GrowthServiceImpl.class);
	@Resource
	private GrowthDAO growthDAO;



	@Override
	public DataGrid<GrowthDTO> queryGrowthList(Pager page,
			GrowthDTO growthDTO) {
		DataGrid<GrowthDTO> dg=new DataGrid<GrowthDTO>();
		dg.setRows(growthDAO.queryList(growthDTO,page));
		dg.setTotal(growthDAO.queryCount(growthDTO));
		return dg;
	}

	@Override
	public ExecuteResult<String> addGrowth(GrowthDTO growthDTO) {
		ExecuteResult<String> er=new ExecuteResult<String>();
		try {
			growthDAO.add(growthDTO);
			er.setResult("操作成功");
			
		} catch (Exception e) {
			er.addErrorMsg("操作失败");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	@Override
	public ExecuteResult<String> updateGrowth(GrowthDTO growthDto) {
		ExecuteResult<String> er=new ExecuteResult<String>();
		
		try {
			growthDAO.update(growthDto);
			er.setResult("操作成功");
		} catch (Exception e) {
			er.addErrorMsg("操作失败");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	@Override
	public GrowthDTO getGrowthDTO(GrowthDTO growthDto) {
		List<GrowthDTO> dataGrid = queryGrowthList(new Pager<GrowthDTO>(), growthDto).getRows();
		if (dataGrid!=null && dataGrid.size()>0) {
			return dataGrid.get(0);
		}
		return new GrowthDTO();
	}

}
