package com.camelot.cmscenter.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.cmscenter.dao.CMSSiteDAO;
import com.camelot.cmscenter.dto.CmsSiteDTO;
import com.camelot.cmscenter.service.CmsSiteService;


@Service("cmsSiteService")
public class CmsSiteServiceImpl implements CmsSiteService{
	private static final Logger logger = LoggerFactory.getLogger(CmsSiteService.class);
	
	@Resource
	private CMSSiteDAO cmsSiteDAO;;


	@Override
	public CmsSiteDTO getCmsSiteById(String id) {
		return cmsSiteDAO.queryById(id);
	}

}
