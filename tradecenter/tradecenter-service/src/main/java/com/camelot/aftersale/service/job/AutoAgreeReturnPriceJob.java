package com.camelot.aftersale.service.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.common.util.DateUtils;

public class AutoAgreeReturnPriceJob {

	@Resource
	private TradeReturnExportService tradeReturnExportService;
    @Resource
    private TradeReturnGoodsService tradeReturnGoodsService;

    /**
     * 如果 卖家已经退款 买家不确认  7天后自动打款
     */
	public void execute(){


        TradeReturnGoodsDTO tradeReturnDto=new TradeReturnGoodsDTO();
        tradeReturnDto.setState(7);  // 7 退款中
        Date preDate= DateUtils.dayOffset(new Date(), -7);
        tradeReturnDto.setTimeNode(preDate);

        List<TradeReturnGoodsDTO>  resData=tradeReturnGoodsService.searchByCondition (tradeReturnDto);
        if(resData!=null&&resData.size()>0){

            for(TradeReturnGoodsDTO item:resData){
                item.setState(6); //6 退款成功
                item.setLastUpdDt(new Date());

                tradeReturnExportService.updateTradeReturnStatus(item,TradeReturnStatusEnum.SUCCESS);

            }
        }
	}
	
}
