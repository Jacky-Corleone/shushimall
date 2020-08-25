package com.camelot.payment.domain.cbmobile;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  网银在线移动端回传
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
@XStreamAlias("DATA")
public class CbMobileResult implements Serializable{

	private static final long serialVersionUID = 8985453306561608756L;
	@XStreamAlias("TRADE")
	private CbMobileTrade cbMobileTrade;
	@XStreamAlias("RETURN")
	private CbMobileReturn cbMobileReturn;
	public CbMobileTrade getCbMobileTrade() {
		return cbMobileTrade;
	}
	public void setCbMobileTrade(CbMobileTrade cbMobileTrade) {
		this.cbMobileTrade = cbMobileTrade;
	}
	public CbMobileReturn getCbMobileReturn() {
		return cbMobileReturn;
	}
	public void setCbMobileReturn(CbMobileReturn cbMobileReturn) {
		this.cbMobileReturn = cbMobileReturn;
	}
}
