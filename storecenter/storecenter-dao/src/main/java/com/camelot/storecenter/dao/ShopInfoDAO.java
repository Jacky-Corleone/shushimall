package com.camelot.storecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopModifyDetailDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;


/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface ShopInfoDAO  extends BaseDAO<ShopDTO>{
	
	/**
	 * 
	 * <p>Discription:[修改店铺状态]</p>
	 * Created on 2015-3-19
	 * @param shopDTO
	 * @author:yuht
	 */
	public void modifyShopInfostatus(ShopDTO shopDTO);
	
	/**
	 * 
	 * <p>Discription:[查询店铺信息]</p>
	 * Created on 2015-3-19
	 * @param shopAudiinDTO
	 * @return
	 * @author:yuht
	 */
	public List<ShopDTO> queryShopInfoByIds(@Param("entity")ShopAudiinDTO shopAudiinDTO);
	
	/**
	 * 
	 * <p>Discription:[修改店铺信息]</p>
	 * Created on 2015-3-19
	 * @param shopModifyDetailDTO
	 * @author:yuht
	 */
	public void updateShopInfo(ShopModifyDetailDTO shopModifyDetailDTO);
	/**
	 * 
	 * <p>Discription:[获取店铺ID方法]</p>
	 * Created on 2015-3-17
	 * @return
	 * @author:yuht
	 */
	public Long getShopId();
	/**
	 * 
	 * <p>Discription:[修改店铺运营状态]</p>
	 * Created on 2015-3-19
	 * @param shopDTO
	 * @return
	 * @author:yuht
	 */
	public Integer modifyShopRunStatus(ShopDTO shopDTO);
	
	/**
	 * 
	 * <p>Discription:[根据品牌Id 查询店铺信息]</p>
	 * Created on 2015-4-14
	 * @param brandId
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public List<ShopDTO> queryShopInfoByBrandId(@Param("brandId")Long brandId, @Param("page") Pager page);
	/**
	 * 
	 * <p>Discription:[根据品牌Id 查询店铺信息条数]</p>
	 * Created on 2015-4-14
	 * @param brandId
	 * @return
	 * @author:yuht
	 */
	public Long queryCountShopInfoByBrandId(Long brandId);

	/**
	 * 
	 * <p>Description: [查询所有的科印的店铺信息]</p>
	 * Created on 2015年9月7日
	 * @return
	 * @author:[宋文斌]
	 */
	public List<ShopDTO> findAllKeyinShopInfo();
}