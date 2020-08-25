package com.camelot.aftersale.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.aftersale.dao.TradeReturnPicMybatisDAO;
import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
import com.camelot.aftersale.service.TradeReturnPicService;
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
@Service("tradeReturnPicService")
public class TradeReturnPicServiceImpl implements TradeReturnPicService {
	private final static Logger logger = LoggerFactory.getLogger(TradeReturnPicServiceImpl.class);
	@Resource
	private TradeReturnPicMybatisDAO tradeReturnPicMybatisDAO;
	

	
	@Override
	public ExecuteResult<TradeReturnPicDTO> createTradeReturnPicDTO(TradeReturnPicDTO tradeReturnPic){
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();

        tradeReturnPic.setCreatedDt(new Date());
        tradeReturnPic.setDeletedFlag("0");

		tradeReturnPicMybatisDAO.add(tradeReturnPic);
		executeResult.setResult(tradeReturnPic);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnPicDTO> updateTradeReturnPicDTO(TradeReturnPicDTO tradeReturnPic){
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();
		setLastUpdInfo(tradeReturnPic);
		tradeReturnPicMybatisDAO.update(tradeReturnPic);
		executeResult.setResult(tradeReturnPic);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnPicDTO> deleteById(Long tradeReturnPicId){
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();
		
		tradeReturnPicMybatisDAO.delete(tradeReturnPicId);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnPicDTO> deleteAll(List<Long> idList){
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();
		tradeReturnPicMybatisDAO.deleteAll(idList);
		return executeResult;
	}
	@Override
	public DataGrid<TradeReturnPicDTO> searchTradeReturnPicDTOs(Pager<TradeReturnPicDTO> pager, TradeReturnPicDTO tradeReturnPic){
		DataGrid<TradeReturnPicDTO> resultPager = new DataGrid<TradeReturnPicDTO>();
		List<TradeReturnPicDTO> tradeReturnPics = tradeReturnPicMybatisDAO.queryList(tradeReturnPic, pager);
		long size = tradeReturnPicMybatisDAO.queryCount(tradeReturnPic);
		resultPager.setRows(tradeReturnPics);
		resultPager.setTotal(size);
		return resultPager;
	}

	@Override
	public TradeReturnPicDTO getTradeReturnPicDTOById(Long tradeReturnPicId){
		return tradeReturnPicMybatisDAO.queryById(tradeReturnPicId);
	}

	
	@Override
	public ExecuteResult<TradeReturnPicDTO> updateSelective(TradeReturnPicDTO tradeReturnPic){
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();
		setLastUpdInfo(tradeReturnPic);
		tradeReturnPicMybatisDAO.updateSelective(tradeReturnPic);
		executeResult.setResult(tradeReturnPic);
		return executeResult;
	}
	@Override
	public ExecuteResult<TradeReturnPicDTO> updateSelectiveWithDateTimeCheck(TradeReturnPicDTO tradeReturnPic,	Date prevUpdDt) {
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();
		setLastUpdInfo(tradeReturnPic);
		int resInt=tradeReturnPicMybatisDAO.updateSelectiveWithDateTimeCheck(tradeReturnPic,prevUpdDt);

		executeResult.setResult(tradeReturnPic);
		return executeResult;
	}

	@Override
	public List<TradeReturnPicDTO> searchByCondition(TradeReturnPicDTO tradeReturnPic){
		tradeReturnPic.setDeletedFlag("0");
		return tradeReturnPicMybatisDAO.searchByCondition(tradeReturnPic);
	}


	@Override
	public ExecuteResult<TradeReturnPicDTO> updateSelectiveByIdList(TradeReturnPicDTO tradeReturnPic,List<Long> idList){
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();
		if(idList!=null&&idList.size()>0&& tradeReturnPic!=null){
			setLastUpdInfo(tradeReturnPic);
			tradeReturnPicMybatisDAO.updateSelectiveByIdList(tradeReturnPic,idList);
		}
		executeResult.setResult(tradeReturnPic);
		return executeResult;
	}

    @Override
    public ExecuteResult<TradeReturnPicDTO> updateAllByIdList(TradeReturnPicDTO tradeReturnPic, List<Long> idList) {
        return null;
    }

    @Override
	public ExecuteResult<TradeReturnPicDTO> defunctById(Long tradeReturnPicId){
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();
		TradeReturnPicDTO tradeReturnPic=new TradeReturnPicDTO();
		tradeReturnPic.setDeletedFlag("1");
		tradeReturnPic.setId(tradeReturnPicId);
		tradeReturnPicMybatisDAO.updateSelective(tradeReturnPic);
		executeResult.setResult(tradeReturnPic);
		return executeResult;
	}

    @Override
    public ExecuteResult<String> defunctByReturnGoodsId(Long returnGoodsId) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        TradeReturnPicDTO tradeReturnPic=new TradeReturnPicDTO();
        tradeReturnPic.setDeletedFlag("1");
        tradeReturnPic.setReturnGoodsId(returnGoodsId);
        tradeReturnPicMybatisDAO.updateAllByReturnGoodsId(tradeReturnPic);
        return executeResult;
    }

    @Override
	public ExecuteResult<TradeReturnPicDTO> defunctByIdList(List<Long> idList){
		ExecuteResult<TradeReturnPicDTO> executeResult = new ExecuteResult<TradeReturnPicDTO>();
		TradeReturnPicDTO tradeReturnPic=new TradeReturnPicDTO();
		tradeReturnPic.setDeletedFlag("1");
		tradeReturnPicMybatisDAO.updateSelectiveByIdList(tradeReturnPic,idList);
		executeResult.setResult(tradeReturnPic);
		return executeResult;
	}
	private void setLastUpdInfo(TradeReturnPicDTO tradeReturnPic){
	
		Date 	curDate = new Date();
		
		tradeReturnPic.setLastUpdDt(curDate);
	}
	
}
