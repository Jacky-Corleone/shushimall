package com.camelot.sellercenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.Constants;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.sellercenter.dao.MallInfoDAO;
import com.camelot.sellercenter.domain.MallInfo;
import com.camelot.sellercenter.logo.dto.LogoDTO;
import com.camelot.sellercenter.logo.service.LogoExportService;

@Service
public class LogoServiceImpl implements LogoExportService {
	
	private static final Logger logger = LoggerFactory.getLogger(LogoServiceImpl.class);
	
	@Resource
	private MallInfoDAO mallInfoDAO;
	
	@Override
	public ExecuteResult<LogoDTO> getMallLogo() {
		ExecuteResult<LogoDTO> executeResult = new ExecuteResult<LogoDTO>();
		try {
			List<LogoDTO> logoDTOList = mallInfoDAO.findByPlatformId(null);
			if (logoDTOList != null && logoDTOList.size() > 0) {
				LogoDTO logoDTO = logoDTOList.get(0);
				executeResult.setResult(logoDTO);
			}
			executeResult.setResultMessage("Logo查询成功");
		} catch (Exception e) {
			logger.error("执行方法：【getMallLogo】报错！{}",e);
			executeResult.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return executeResult;
	}
	
	@Override
	public ExecuteResult<LogoDTO> getMallLogoByPlatformId(Integer platformId) {
		ExecuteResult<LogoDTO> executeResult = new ExecuteResult<LogoDTO>();
		try {
			List<LogoDTO> logoDTOList = new ArrayList<LogoDTO>();
			if (platformId == null || platformId != Constants.PLATFORM_ID) {// 科印
				logoDTOList = mallInfoDAO.findByPlatformId(null);
			} else {// 绿印
				logoDTOList = mallInfoDAO.findByPlatformId(Constants.PLATFORM_ID);
			}
			if (logoDTOList != null && logoDTOList.size() > 0) {
				LogoDTO logoDTO = logoDTOList.get(0);
				executeResult.setResult(logoDTO);
			}
			executeResult.setResultMessage("Logo查询成功");
		} catch (Exception e) {
			logger.error("执行方法：【getMallLogoByPlatformId】报错！{}",e);
			executeResult.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return executeResult;
	}

	/**
	 * <p>Discription:[修改科印logo]</p>
	 * Created on 2015年1月23日
	 * @return
	 * @author:[马桂雷]
	 */
	@Override
	public ExecuteResult<String> modifyMallLogo(String logoName, String picUrl) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		try {
			List<LogoDTO> logoDTOList = mallInfoDAO.findByPlatformId(null);
			if (logoDTOList != null && logoDTOList.size() > 0) {
				Integer num = mallInfoDAO.updateByPlatformId(logoName, picUrl, null);
				executeResult.setResult(num + "");
				executeResult.setResultMessage("Logo修改成功");
			} else {
				MallInfo mallInfo = new MallInfo();
				mallInfo.setTitle(logoName);
				mallInfo.setLogo(picUrl);
				mallInfoDAO.add(mallInfo);
			}
		} catch (Exception e) {
			logger.error("执行方法：【modifyMallLogo】报错！{}",e);
			executeResult.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return executeResult;
	}
	
	@Override
	public ExecuteResult<String> modifyMallLogoByPlatformId(String logoName, String picUrl, Integer platformId) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		try {
			List<LogoDTO> logoDTOList= mallInfoDAO.findByPlatformId(platformId);
			if (logoDTOList != null && logoDTOList.size() > 0) {
				Integer num = mallInfoDAO.updateByPlatformId(logoName, picUrl, platformId);
				executeResult.setResult(num + "");
				executeResult.setResultMessage("Logo修改成功");
			} else {
				MallInfo mallInfo = new MallInfo();
				mallInfo.setTitle(logoName);
				mallInfo.setLogo(picUrl);
				mallInfo.setPlatformId(platformId);
				mallInfoDAO.add(mallInfo);
			}
		} catch (Exception e) {
			logger.error("执行方法：【modifyMallLogoByPlatformId】报错！{}",e);
			executeResult.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return executeResult;
	}

}