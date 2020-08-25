package com.camelot.aftersale.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.aftersale.dao.TradeReturnGoodsDetailMybatisDAO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.service.TradeReturnGoodsDetailService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务逻辑实现类
 * </p>
 * 
 * @author
 * @createDate 
 **/
@Service("tradeReturnGoodsDetailService")
public class TradeReturnGoodsDetailServiceImpl implements TradeReturnGoodsDetailService {
	private final static Logger logger = LoggerFactory.getLogger(TradeReturnGoodsDetailServiceImpl.class);
	@Resource
	private TradeReturnGoodsDetailMybatisDAO tradeReturnGoodsDetailMybatisDAO;

	
	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> createTradeReturnGoodsDetailDTO(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail){
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();
		Date 	curDate = new Date();
        tradeReturnGoodsDetail.setCreatedDt(curDate);
		tradeReturnGoodsDetailMybatisDAO.add(tradeReturnGoodsDetail);
		executeResult.setResult(tradeReturnGoodsDetail);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> updateTradeReturnGoodsDetailDTO(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail){
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();

		tradeReturnGoodsDetailMybatisDAO.update(tradeReturnGoodsDetail);
		executeResult.setResult(tradeReturnGoodsDetail);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> deleteById(Long tradeReturnGoodsDetailId){
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();
		
		tradeReturnGoodsDetailMybatisDAO.delete(tradeReturnGoodsDetailId);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> deleteAll(List<Long> idList){
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();
		tradeReturnGoodsDetailMybatisDAO.deleteAll(idList);
		return executeResult;
	}
	@Override
	public DataGrid<TradeReturnGoodsDetailDTO> searchTradeReturnGoodsDetailDTOs(Pager<TradeReturnGoodsDetailDTO> pager, TradeReturnGoodsDetailDTO tradeReturnGoodsDetail){
		DataGrid<TradeReturnGoodsDetailDTO> resultPager = new DataGrid<TradeReturnGoodsDetailDTO>();
		List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetails = tradeReturnGoodsDetailMybatisDAO.queryList( tradeReturnGoodsDetail,pager);
		long size = tradeReturnGoodsDetailMybatisDAO.queryCount(tradeReturnGoodsDetail);
		resultPager.setRows(tradeReturnGoodsDetails);
		resultPager.setTotal(size);
		return resultPager;
	}

	@Override
	public TradeReturnGoodsDetailDTO getTradeReturnGoodsDetailDTOById(Long tradeReturnGoodsDetailId){
		return tradeReturnGoodsDetailMybatisDAO.queryById(tradeReturnGoodsDetailId);
	}

	
	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> updateSelective(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail){
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();

		tradeReturnGoodsDetailMybatisDAO.updateSelective(tradeReturnGoodsDetail);
		executeResult.setResult(tradeReturnGoodsDetail);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> updateSelectiveWithDateTimeCheck(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail,	Date prevUpdDt) {
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();

		int resInt=tradeReturnGoodsDetailMybatisDAO.updateSelectiveWithDateTimeCheck(tradeReturnGoodsDetail,prevUpdDt);
		if (0 == resInt) {

 		}
		executeResult.setResult(tradeReturnGoodsDetail);
		return executeResult;
	}

	@Override
	public List<TradeReturnGoodsDetailDTO> searchByCondition(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail){

		return tradeReturnGoodsDetailMybatisDAO.searchByCondition(tradeReturnGoodsDetail);
	}


	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> updateSelectiveByIdList(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail,List<Long> idList){
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();
		if(idList!=null&&idList.size()>0&& tradeReturnGoodsDetail!=null){

			tradeReturnGoodsDetailMybatisDAO.updateSelectiveByIdList(tradeReturnGoodsDetail,idList);
		}
		executeResult.setResult(tradeReturnGoodsDetail);
		return executeResult;
	}

    @Override
    public ExecuteResult<TradeReturnGoodsDetailDTO> updateAllByIdList(TradeReturnGoodsDetailDTO tradeReturnGoodsDetail, List<Long> idList) {
        return null;
    }

    @Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> defunctById(Long tradeReturnGoodsDetailId){
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();
		TradeReturnGoodsDetailDTO tradeReturnGoodsDetail=new TradeReturnGoodsDetailDTO();

		tradeReturnGoodsDetailMybatisDAO.updateSelective(tradeReturnGoodsDetail);
		executeResult.setResult(tradeReturnGoodsDetail);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDetailDTO> defunctByIdList(List<Long> idList){
		ExecuteResult<TradeReturnGoodsDetailDTO> executeResult = new ExecuteResult<TradeReturnGoodsDetailDTO>();
		TradeReturnGoodsDetailDTO tradeReturnGoodsDetail=new TradeReturnGoodsDetailDTO();

		tradeReturnGoodsDetailMybatisDAO.updateSelectiveByIdList(tradeReturnGoodsDetail,idList);
		executeResult.setResult(tradeReturnGoodsDetail);
		return executeResult;
	}

	
}
