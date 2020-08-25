package com.camelot.aftersale.dto.returngoods;


import java.io.Serializable;

public class TradeReturnInfoQueryDto implements Serializable {
    private static final long serialVersionUID = -6227072344974518310L;
    private TradeReturnGoodsDTO tradeReturnDto;

    public TradeReturnGoodsDTO getTradeReturnDto() {
        return tradeReturnDto;
    }
    public void setTradeReturnDto(TradeReturnGoodsDTO tradeReturnDto) {
        this.tradeReturnDto = tradeReturnDto;
    }




}
