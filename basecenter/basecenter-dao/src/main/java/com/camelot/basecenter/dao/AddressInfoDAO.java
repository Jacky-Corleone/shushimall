package com.camelot.basecenter.dao;

import java.util.List;

import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface AddressInfoDAO extends BaseDAO<AddressInfoDTO>{
	
	public List<AddressInfoDTO> queryAddressinfo(long buyerId);
	public long queryAddressinfoCount(long buyerId);
	public void updateIsDefaultById(Long id);
	public void updateIsDefault(long buyerId);
}
