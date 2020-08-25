package com.camelot.aftersale.dto.returngoods;

import java.io.Serializable;
import java.util.List;


public class TradeReturnInfoDto implements Serializable {
    private static final long serialVersionUID = 8520101895098666861L;
    private TradeReturnGoodsDTO tradeReturnDto;
    private List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailList;

    public TradeReturnGoodsDTO getTradeReturnDto() {
        return tradeReturnDto;
    }

    public void setTradeReturnDto(TradeReturnGoodsDTO tradeReturnDto) {
        this.tradeReturnDto = tradeReturnDto;
    }

    public List<TradeReturnGoodsDetailDTO> getTradeReturnGoodsDetailList() {
        return tradeReturnGoodsDetailList;
    }

    public void setTradeReturnGoodsDetailList(List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailList) {
        this.tradeReturnGoodsDetailList = tradeReturnGoodsDetailList;
    }
}
