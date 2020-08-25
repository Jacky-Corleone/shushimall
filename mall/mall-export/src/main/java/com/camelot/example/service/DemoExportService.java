package com.camelot.example.service;

import com.camelot.example.dto.DemoDTO;

/**
 * 供客户端调用的远程接口
 * 
 * @author
 * 
 */
public interface DemoExportService {

	public DemoDTO getUserByIdTestMybatis(long userId);

	public DemoDTO getUserByIdTestRedis(long userId);

	public DemoDTO getUserByIdTestNo(long userId);
}
