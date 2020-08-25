package com.camelot.aftersale.service.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.common.util.DateUtils;

public class AutoAgreeRefundGoodsJob {

	@Resource
	private TradeReturnExportService tradeReturnExportService;
    @Resource
    private TradeReturnGoodsService tradeReturnGoodsService;

    /**
     * 买家发回货物后 卖家10天不处理  自动退款
     */
	public void execute(){


        TradeReturnGoodsDTO tradeReturnDto=new TradeReturnGoodsDTO();
        tradeReturnDto.setState(4);  // 4 买家已退货,等待卖家确认收货
        Date preDate= DateUtils.dayOffset(new Date(), -10);
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
