package com.camelot.aftersale.service.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.common.util.DateUtils;

public class AutoCancleReturnGoodsJob {

	@Resource
	private TradeReturnExportService tradeReturnExportService;
    @Resource
    private TradeReturnGoodsService tradeReturnGoodsService;

    /**
     *    如果 卖家同意退货 让买家发回货物 买家7天 不处理 自动取消退货-
     */
	public void execute(){


        TradeReturnGoodsDTO tradeReturnDto=new TradeReturnGoodsDTO();
        tradeReturnDto.setState(3);  // 3 退款申请达成,等待买家发货
        Date preDate= DateUtils.dayOffset(new Date(), -7);
        tradeReturnDto.setTimeNode(preDate);

        List<TradeReturnGoodsDTO>  resData=tradeReturnGoodsService.searchByCondition (tradeReturnDto);
        if(resData!=null&&resData.size()>0){

            for(TradeReturnGoodsDTO item:resData){
                item.setState(5); // 改为 退款关闭
                item.setLastUpdDt(new Date());
//                tradeReturnGoodsService.updateSelective(item);
                tradeReturnExportService.updateTradeReturnStatus(item, TradeReturnStatusEnum.CLOSE);
            }
        }
	}
	
}
