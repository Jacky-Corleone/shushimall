package com.camelot.example.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.example.dao.DemoMybatisDAO;
import com.camelot.example.domain.Demo;
import com.camelot.example.dto.DemoDTO;
import com.camelot.example.service.DemoExportService;
import com.camelot.openplatform.dao.util.RedisDB;

@Service("demoExportService")
public class DemoExportServiceImpl implements DemoExportService {
	private final static Logger logger = LoggerFactory.getLogger(DemoExportServiceImpl.class);

	@Resource
	private RedisDB redisDB;

	@Resource
	private DemoMybatisDAO demoMybatisDAO;

	@Override
	public DemoDTO getUserByIdTestMybatis(long userId) {

		logger.info("getUserByIdTestMybatis---userId---:{}", userId);

		DemoDTO demoDTO = new DemoDTO();

		Demo demo = demoMybatisDAO.queryById(userId);

		demoDTO.setName(demo.getName());
		demoDTO.setStatus(demo.getStatus());

		return demoDTO;
	}

	/**
	 * 
	 * <p>Discription:[getUserByIdTestRedis]</p>
	 * @param userId
	 * @return
	 * Created on 2015-1-22
	 * @author:[创建者中文名字]
	 */
	@Override
	public DemoDTO getUserByIdTestRedis(long userId) {

		DemoDTO demoDTO = new DemoDTO();

		logger.info("getUserByIdTestRedis---userId---:{}", userId);

		String userIdS = String.valueOf(userId);
		Demo demo = new Demo();
		demo.setName("测试-----getUserTestRedis----redis OK-----------");

		//保存redis
		redisDB.set(userIdS, JSON.toJSONString(demo));

		//获取返回
		demo = JSON.parseObject(redisDB.get(userIdS), Demo.class);

		demoDTO.setName(demo.getName());

		return demoDTO;
	}

	/**
	 * 
	 * <p>Discription:[getUserByIdTestNo]</p>
	 * @param userId
	 * @return
	 * Created on 2015-1-22
	 * @author:[创建者中文名字]
	 */
	@Override
	public DemoDTO getUserByIdTestNo(long userId) {

		logger.info("DemoExportServiceImpl---userId---:{}", userId);

		DemoDTO demoDTO = new DemoDTO();
		demoDTO.setName("测试-getUserByIdTestNo--OK---");

		return demoDTO;

	}

}
