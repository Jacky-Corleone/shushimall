package com.camelot.basecenter.service;

import com.camelot.basecenter.domain.AddressInfo;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;

/** 
 * <p>Description: [收货人地址服务]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface AddressInfoService {
	
	/**
	 * <p>Discription:[收货人地址列表查询]</p>
	 * Created on 2015年3月10日
	 * @param buyerId
	 * @return
	 * @author:[杨芳]
	 */
	public DataGrid<AddressInfoDTO> queryAddressinfo(long buyerId);
	/**
	 * <p>Discription:[收货人地址详情查询]</p>
	 * Created on 2015年3月10日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<AddressInfoDTO> queryAddressinfoById(long id);
	/**
	 * <p>Discription:[收货地址添加]</p>
	 * Created on 2015年3月10日
	 * @param addressInofDTO
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> addAddressInfo(AddressInfoDTO addressInofDTO);
	/**
	 * <p>Discription:[收货地址修改]</p>
	 * Created on 2015年3月10日
	 * @param addressInofDTO
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> modifyAddressInfo(AddressInfoDTO addressInfoDTO);
	/**
	 * <p>Discription:[删除收货地址]</p>
	 * Created on 2015年3月10日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> removeAddresBase(Long id);
	/**
	 * <p>Discription:[修改默认收货地址]</p>
	 * Created on 2015年3月10日
	 * @param id 地址的id ,userId 买家的id
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> modifyDefaultAddress(Long id,long buyerId);

}
