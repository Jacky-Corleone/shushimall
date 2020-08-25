package com.camelot.tradecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dao.TradeOrdersEnclosureDAO;
import com.camelot.tradecenter.dto.TradeOrdersEnclosureDTO;
import com.camelot.tradecenter.service.TradeOrdersEnclosureService;

@Service("tradeOrdersEnclosureService")
public class TradeOrdersEnclosureServiceImpl implements TradeOrdersEnclosureService{
	
	private static final Logger logger = LoggerFactory.getLogger(TradeOrdersEnclosureServiceImpl.class);
	@Resource
	private TradeOrdersEnclosureDAO tradeOrdersEnclosureDao;
	/**
	 * 
	 * <p>Discription:[增加订单附件]</p>
	 * Created on 2016年2月17日
	 * @param tradeOrdersEnclosureDTO
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<TradeOrdersEnclosureDTO> addEnclosure(TradeOrdersEnclosureDTO tradeOrdersEnclosureDTO) {
		logger.info("\n 方法[{}]，入参：[{}]", "TradeOrdersEnclosureServiceImpl-addEnclosure", JSONObject.toJSONString(tradeOrdersEnclosureDTO));
		ExecuteResult<TradeOrdersEnclosureDTO> result = new ExecuteResult<TradeOrdersEnclosureDTO>();
		try {
			if (tradeOrdersEnclosureDTO.getId() != 0) {
				//修改备注信息
				tradeOrdersEnclosureDao.update(tradeOrdersEnclosureDTO);
			}else {
				//增加该订单号的附件
				tradeOrdersEnclosureDao.add(tradeOrdersEnclosureDTO);
			}
			result.setResult(tradeOrdersEnclosureDTO);
			result.setResultMessage("添加成功");
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.info("\n 方法[{}]，异常：[{}]", "TradeOrdersEnclosureServiceImpl-addEnclosure", e);
		}
		logger.info("\n 方法[{}]，出参：[{}]", "TradeOrdersEnclosureServiceImpl-addEnclosure", JSONObject.toJSONString(result));
		return result;
	}
	/**
	 * 
	 * <p>Discription:[分页查询订单附件]</p>
	 * Created on 2016年2月17日
	 * @param tradeOrdersEnclosureDTO
	 * @param pager
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public DataGrid<TradeOrdersEnclosureDTO> queryEnclosures(TradeOrdersEnclosureDTO tradeOrdersEnclosureDTO,Pager<TradeOrdersEnclosureDTO> pager) {
		DataGrid<TradeOrdersEnclosureDTO> resultPager = new DataGrid<TradeOrdersEnclosureDTO>();
		List<TradeOrdersEnclosureDTO> TradeOrdersEnclosureDTOs = tradeOrdersEnclosureDao.queryList(tradeOrdersEnclosureDTO, pager);
		long size = tradeOrdersEnclosureDao.queryCount(tradeOrdersEnclosureDTO);
		resultPager.setRows(TradeOrdersEnclosureDTOs);
		resultPager.setTotal(size);
		logger.info("\n 方法[{}]，出参：[{}]", "TradeOrdersEnclosureServiceImpl-queryEnclosures", JSONObject.toJSONString(resultPager));
		return resultPager;
	}
	
	@Override
	public ExecuteResult<String> delEnclosure(long id) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			tradeOrdersEnclosureDao.delete(id);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			logger.info("\n 方法[{}]，异常：[{}]", "TradeOrdersEnclosureServiceImpl-delEnclosure", e);
		}
		return result;
	}
}
