package com.camelot.ecm.sellercenter.mallrec.view;

import com.camelot.sellercenter.mallRec.dto.MallRecDTO;

public class MallRecView {
	private MallRecDTO mallRec;
	private String cName;
	
	public MallRecDTO getMallRec() {
		return mallRec;
	}
	public void setMallRec(MallRecDTO mallRec) {
		this.mallRec = mallRec;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
}
