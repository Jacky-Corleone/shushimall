package com.camelot.aftersale.service.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.common.util.DateUtils;

public class AutoCancleAppealReturnGoodsJob {

	@Resource
	private TradeReturnExportService tradeReturnExportService;
    @Resource
    private TradeReturnGoodsService tradeReturnGoodsService;

    /**
     *  买家可以申诉 买家不申诉 7天后自动关闭
     */
	public void execute(){


        TradeReturnGoodsDTO tradeReturnDto=new TradeReturnGoodsDTO();
        tradeReturnDto.setState(2);  // 3 卖家不同意协议,等待买家修改
        Date preDate= DateUtils.dayOffset(new Date(), -7);
        tradeReturnDto.setTimeNode(preDate);

        List<TradeReturnGoodsDTO>  resData=tradeReturnGoodsService.searchByCondition (tradeReturnDto);
        if(resData!=null&&resData.size()>0){

            for(TradeReturnGoodsDTO item:resData){
                item.setState(5); //5 退款关闭
//                item.setLastUpdDt(new Date());
//
//                tradeReturnGoodsService.updateSelective(item);
                tradeReturnExportService.updateTradeReturnStatus(item, TradeReturnStatusEnum.CLOSE);

            }
        }
	}
	
}
