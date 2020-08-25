package com.camelot.sellercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.dao.MallRecMybatisDAO;
import com.camelot.sellercenter.domain.MallRec;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:供远程调用的接口的实现  商城楼层推荐]</p>
 * Created on 2015年1月22日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("mallRecExportService")
public class MallRecExportServiceImpl implements MallRecExportService {
	
	private static final Logger logger = LoggerFactory.getLogger(MallRecExportServiceImpl.class);
	
	@Resource
	private MallRecMybatisDAO mallRecMybatisDAO;

	/**
	 * 
	 * <p>Discription:[方法功能中文描述:新建楼层的远程调用方法的接口实现]</p>
	 * Created on 2015年1月22日
	 * @param mallRecDTO
	 * @return
	 * @author:[chenx]
	 */
	@Override
	public ExecuteResult<String> addMallRec(MallRecDTO mallRecDTO) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		try {
			//首先将dto转换成domain  false
			MallRec mallRec = EntityTranslator.transObj(mallRecDTO, MallRec.class, false);
			mallRecMybatisDAO.add(mallRec);
			executeResult.setResultMessage("保存成功");
		} catch (Exception e) {
			logger.error("执行方法【addMallRec】报错！{}",e);
			executeResult.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return executeResult;
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述:根据id查询楼层的详情]</p>
	 * Created on 2015年1月22日
	 * @param id
	 * @return
	 * @author:[chenx]
	 */
	@Override
	public MallRecDTO getMallRecById(Long id) {
		MallRecDTO dto = null;
		try {
			dto = mallRecMybatisDAO.queryDTOById(id);
		} catch (Exception e) {
			logger.error("执行方法【getMallRecById】报错！{}",e);
			throw new RuntimeException(e);
		}
		
		return dto;
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询楼层的列表  分页]</p>
	 * Created on 2015年1月22日
	 * @param mallRecDTO
	 * @param page
	 * @return
	 * @author:[chenx]
	 */
	@Override
	public DataGrid<MallRecDTO> queryMallRecList(MallRecDTO mallRecDTO, Pager page) {
		//查询数据库中所有的数据  返回list
		DataGrid<MallRecDTO> dataGrid = new DataGrid<MallRecDTO>();
		try {
			List<MallRecDTO> listMallRecDTO = mallRecMybatisDAO.queryDTOList(mallRecDTO, page);
			//首先将dto转换成domain
//			MallRec mallRec = EntityTranslator.transObj(mallRecDTO, MallRec.class, false);
			//查询数据库中所有的数据  返回个数
			long size = mallRecMybatisDAO.queryCount(mallRecDTO);
			//将数据库中的总条数set到dataGrid的total中
			dataGrid.setTotal(size);
			//将数据库的数据set到dataGrid的总行数中
			dataGrid.setRows(listMallRecDTO);
			
		} catch (Exception e) {
			logger.error("执行方法【queryMallRecList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return dataGrid;
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述:修改楼层]</p>
	 * Created on 2015年1月22日
	 * @param mallRecDTO
	 * @return
	 * @author:[chenx]
	 */
	@Override
	public ExecuteResult<String> modifyMallRec(MallRecDTO mallRecDTO) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		try {
			//将dto转换成domain
			MallRec mallRec = EntityTranslator.transObj(mallRecDTO, MallRec.class, false);
			mallRecMybatisDAO.update(mallRec);
			executeResult.setResultMessage("编辑成功");
		} catch (Exception e) {
			logger.error("执行方法【modifyMallRec】报错！{}",e);
			executeResult.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return executeResult;
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述:楼层的上下架]</p>
	 * Created on 2015年1月22日
	 * @param id
	 * @param publishFlag
	 * @return
	 * @author:[chenx]
	 */
	@Override
	public ExecuteResult<String> modifyMallRecStatus(Long id, String publishFlag) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		try {
			//上架状态
			if ("1".equals(publishFlag)) {
				//根据id获取楼层信息
//				MallRecDTO mallRecDTO = mallRecMybatisDAO.queryDTOById(id);
//				MallRecDTO mallRec = new MallRecDTO();
				//状态（上架）
//				mallRec.setStatusDTO(1);
//				mallRec.setFloorNumDTO(mallRecDTO.getFloorNumDTO());
//				mallRec.setRecTypeDTO(mallRecDTO.getRecTypeDTO());
//				mallRec.setThemeId(mallRecDTO.getThemeId());
//				//获取楼层号
////				mallRec.setFloorNum(mallRecDTO.getFloorNumDTO());
//				//根据 楼层号和状态  获取到当前楼层号发布的数量
//				long size = mallRecMybatisDAO.queryCount(mallRec);
//				//如果当前楼层号已经发布的数量>0   不允许将这个楼层号进行发布  返回错误信息给前台
//				if (size > 0) {
//					executeResult.setResultMessage("存在已经发布的楼层");
//					executeResult.setResult("0");
//					return executeResult;
//				}
			}
			//不是上架状态  可以 发布
			mallRecMybatisDAO.modifyMallRecStatus(id, publishFlag);
			executeResult.setResultMessage("保存成功");
			executeResult.setResult("1");
		} catch (Exception e) {
			logger.error("执行方法【modifyMallRecStatus】报错！{}",e);
			executeResult.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return executeResult;
	}

	@Override
	public ExecuteResult<String> deleteMallRecById(Long id) {
		ExecuteResult<String> res=new ExecuteResult<String>();
		long count=mallRecMybatisDAO.delete(id);
		if(count>0){
			res.setResult("1");
		}else{
			res.setResult("0");
		}
		return  res;
	}

	/**
     * 
     * <p>Discription:[方法功能中文描述:热卖单品上架前其他单品全部下架]</p>
     * Created on 2016年2月18日
     * @author:[王宏伟]
     */
	@Override
	public void updateStatusByFloorType(String themeId) {
		mallRecMybatisDAO.updateStatusByFloorType(themeId);
	}
}
