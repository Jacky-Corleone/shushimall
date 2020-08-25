package com.camelot.aftersale.service.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.camelot.aftersale.dao.TradeReturnGoodsMybatisDAO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * <p>
 * 业务逻辑实现类
 * </p>
 * 
 * @author
 * @createDate 
 **/
@Service("tradeReturnGoodsService")
class TradeReturnGoodsServiceImpl implements TradeReturnGoodsService {
	private final static Logger logger = LoggerFactory.getLogger(TradeReturnGoodsServiceImpl.class);
	@Resource
	private TradeReturnGoodsMybatisDAO tradeReturnGoodsMybatisDAO;


    @Override
    public String getCodeNo() {
        return tradeReturnGoodsMybatisDAO.getCodeNo();
    }

    @Override
	public ExecuteResult<TradeReturnGoodsDTO> createTradeReturnGoodsDTO(TradeReturnGoodsDTO tradeReturnGoods){
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();

        tradeReturnGoods.setCreatedDt(new Date());
        tradeReturnGoods.setLastUpdDt(new Date());
        tradeReturnGoods.setDeletedFlag("0");
        tradeReturnGoods.setApplyDt(new Date());
        tradeReturnGoods.setCodeNo(getCodeNo());
		tradeReturnGoodsMybatisDAO.add(tradeReturnGoods);
		executeResult.setResult(tradeReturnGoods);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDTO> updateTradeReturnGoodsDTO(TradeReturnGoodsDTO tradeReturnGoods){
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();
		setLastUpdInfo(tradeReturnGoods);
		tradeReturnGoodsMybatisDAO.update(tradeReturnGoods);
		executeResult.setResult(tradeReturnGoods);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDTO> deleteById(Long tradeReturnGoodsId){
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();
		
		tradeReturnGoodsMybatisDAO.delete(tradeReturnGoodsId);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDTO> deleteAll(List<Long> idList){
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();
		tradeReturnGoodsMybatisDAO.deleteAll(idList);
		return executeResult;
	}
	@Override
	public DataGrid<TradeReturnGoodsDTO> searchTradeReturnGoodsDTOs(Pager<TradeReturnGoodsDTO> pager, TradeReturnGoodsDTO tradeReturnGoods){
		DataGrid<TradeReturnGoodsDTO> resultPager = new DataGrid<TradeReturnGoodsDTO>();
        if(tradeReturnGoods.getApplyDtEnd()!=null) {
            tradeReturnGoods.setApplyDtEnd(DateUtils.dayOffset(tradeReturnGoods.getApplyDtEnd(), 1));
        }
		List<TradeReturnGoodsDTO> tradeReturnGoodss = tradeReturnGoodsMybatisDAO.queryList(tradeReturnGoods,pager);
		long size = tradeReturnGoodsMybatisDAO.queryCount(tradeReturnGoods);
		resultPager.setRows(tradeReturnGoodss);
		resultPager.setTotal(size);
		return resultPager;
	}

	@Override
	public TradeReturnGoodsDTO getTradeReturnGoodsDTOById(Long tradeReturnGoodsId){
		return tradeReturnGoodsMybatisDAO.queryById(tradeReturnGoodsId);
	}

	
	@Override
	public ExecuteResult<TradeReturnGoodsDTO> updateSelective(TradeReturnGoodsDTO tradeReturnGoods){
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();
		setLastUpdInfo(tradeReturnGoods);
		tradeReturnGoodsMybatisDAO.updateSelective(tradeReturnGoods);
        tradeReturnGoods=tradeReturnGoodsMybatisDAO.queryById(tradeReturnGoods.getId());
		executeResult.setResult(tradeReturnGoods);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDTO> updateSelectiveWithDateTimeCheck(TradeReturnGoodsDTO tradeReturnGoods,	Date prevUpdDt) {
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();
		setLastUpdInfo(tradeReturnGoods);
		int resInt=tradeReturnGoodsMybatisDAO.updateSelectiveWithDateTimeCheck(tradeReturnGoods,prevUpdDt);
		if (0 == resInt) {

 		}
		executeResult.setResult(tradeReturnGoods);
		return executeResult;
	}

	@Override
	public List<TradeReturnGoodsDTO> searchByCondition(TradeReturnGoodsDTO tradeReturnGoods){
		tradeReturnGoods.setDeletedFlag("0");
		return tradeReturnGoodsMybatisDAO.searchByCondition(tradeReturnGoods);
	}


	@Override
	public ExecuteResult<TradeReturnGoodsDTO> updateSelectiveByIdList(TradeReturnGoodsDTO tradeReturnGoods,List<Long> idList){
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();
		if(idList!=null&&idList.size()>0&& tradeReturnGoods!=null){
			setLastUpdInfo(tradeReturnGoods);
			tradeReturnGoodsMybatisDAO.updateSelectiveByIdList(tradeReturnGoods,idList);
		}
		executeResult.setResult(tradeReturnGoods);
		return executeResult;
	}

    @Override
    public ExecuteResult<TradeReturnGoodsDTO> updateAllByIdList(TradeReturnGoodsDTO tradeReturnGoods, List<Long> idList) {
        return null;
    }

    @Override
	public ExecuteResult<TradeReturnGoodsDTO> defunctById(Long tradeReturnGoodsId){
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();
		TradeReturnGoodsDTO tradeReturnGoods=new TradeReturnGoodsDTO();
		tradeReturnGoods.setDeletedFlag("1");
		tradeReturnGoods.setId(tradeReturnGoodsId);
		tradeReturnGoodsMybatisDAO.updateSelective(tradeReturnGoods);
		executeResult.setResult(tradeReturnGoods);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnGoodsDTO> defunctByIdList(List<Long> idList){
		ExecuteResult<TradeReturnGoodsDTO> executeResult = new ExecuteResult<TradeReturnGoodsDTO>();
		TradeReturnGoodsDTO tradeReturnGoods=new TradeReturnGoodsDTO();
		tradeReturnGoods.setDeletedFlag("1");
		tradeReturnGoodsMybatisDAO.updateSelectiveByIdList(tradeReturnGoods,idList);
		executeResult.setResult(tradeReturnGoods);
		return executeResult;
	}
	private void setLastUpdInfo(TradeReturnGoodsDTO tradeReturnGoods){
	
		Date 	curDate = new Date();
		
		tradeReturnGoods.setLastUpdDt(curDate);
	}

	@Override
	public List<TradeReturnGoodsDTO> searchByOrderIdAndState(String orderId,
			Integer... state) {
		return tradeReturnGoodsMybatisDAO.searchByOrderIdAndState(orderId,state);
	}
	
}
