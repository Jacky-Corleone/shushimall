package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.dto.indto.ShopInfoModifyInDTO;


public interface ShopExportService  {
    /**
     * 二级域名存放在redis中的key
     */
    public static final String SECOND_DOMAIN_KEY= "second_domain_key";
    /**
     * 三级域名存放在redis中的key
     */
    public static final String THIRD_DOMAIN_KEY= "third_domain_key";
	/**
	 * 
	 * <p>Discription:[添加店铺信息]</p>
	 * Created on 2015-3-5
	 * @param shopDTO
	 * @return
	 * @author:yuht
	 */
	public  ExecuteResult<String> saveShopInfo(ShopDTO shopDTO);
	/**
	 * 
	 * <p>Discription:[查询店铺信息详情]</p>
	 * Created on 2015-3-5
	 * @param id
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<ShopDTO> findShopInfoById(long id);
	/**
	 * 
	 * <p>Discription:[根据条件查询店铺信息列表]</p>
	 * Created on 2015-3-5
	 * @param shopDTO
	 * @param pager
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ShopDTO>> findShopInfoByCondition(ShopDTO shopDTO, Pager<ShopDTO> pager);
	
	/**
	 * 
	 * <p>Description: [查询所有的科印的店铺信息]</p>
	 * Created on 2015年9月7日
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<List<ShopDTO>> findAllKeyinShopInfo();
	
	/**
	 * 
	 * <p>Discription:[根据店铺id修改新店铺申请审核状态]</p>
	 * Created on 2015-3-5
	 * @param shopID
	 * @param status
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopInfostatus(Long shopId,int status);
	
	/**
	 * 
	 * <p>Discription:[批量查询操作]</p>
	 * Created on 2015年3月6日
	 * @param ids
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<List<ShopDTO>> queryShopInfoByids(ShopAudiinDTO shopAudiinDTO);
	
	/**
	 * 
	 * <p>Discription:[店铺信息修改--申请提交(需要审核的 插入明细表)]</p>
	 * Created on 2015-3-12
	 * @param shopInfoModifyInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopInfo(ShopInfoModifyInDTO shopInfoModifyInDTO);
	
	/**
	 * 
	 * <p>Discription:[直接修改店铺信息]</p>
	 * Created on 2015-3-25
	 * @param shopInfoModifyInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopInfoUpdate(ShopInfoModifyInDTO shopInfoModifyInDTO);
	
	
	/**
	 * 
	 * <p>Discription:[修改店铺运行状态]</p>
	 * Created on 2015-3-19
	 * @param shopId
	 * @param runStatus 1表示卖家开启铺店，2表示卖家关闭店铺
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopRunStatus(Long shopId,Integer runStatus );
	/**
	 * 
	 * <p>Discription:[通过店铺信息审核  和品牌 类目 审核]</p>
	 * Created on 2015-3-19
	 * @param shopId
	 * @param status  2是通过，3是驳回
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopInfoAndcbstatus(ShopDTO shopDTO);
	
	/**
	 * 
	 * <p>Discription:[修改店铺状态为开通 店铺运行状态开启]</p>
	 * Created on 2015-4-2
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopstatusAndRunstatus(Long shopId);
	/**
	 * 
	 * <p>Discription:[修改店铺状态为审核通过 店铺运行状态关闭]</p>
	 * Created on 2015-4-2
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyShopstatusAndRunstatusBack(Long shopId);
	
	/**]
	 * 
	 * <p>Discription:[根据品牌id查询店铺信息]</p>
	 * Created on 2015-4-14
	 * @param brandId
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ShopDTO>> queryShopInfoByBrandId(Long brandId,Pager page);
    /**
     *
     * <p>Discription:[将一个店铺域名信息放入redis]</p>
     * Created on 2015-6-15
     * @param shopUrl 二级域名
     * @param shopId 店铺id
     * @return
     * @author:zhoule
     */
    public ExecuteResult<String> addSecondDomainToRedis(String shopUrl, Long shopId);

    /**
     *
     * <p>Discription:[将所有店铺域名放入redis]</p>
     * Created on 2015-6-15
     * @return
     * @author:zhoule
     */
    public ExecuteResult<String> addSecondDomainToRedis();
    
    /**
     * 
     * <p>Description: [将所有店铺三级域名放入redis]</p>
     * Created on 2015年9月7日
     * @return
     * @author:[宋文斌]
     */
    public ExecuteResult<String> addThirdDomainToRedis();

    /**
     * 将店铺同步到redis中
     * @param shopUrl
     * @param shopId
     * @return
     */
    public ExecuteResult<String> addGreenPrintThirdDomainToRedis(String shopUrl, Long shopId);

}
