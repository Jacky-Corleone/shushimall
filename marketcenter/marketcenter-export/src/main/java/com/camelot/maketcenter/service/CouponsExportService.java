package com.camelot.maketcenter.service;

import java.util.List;
import java.util.Map;

import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponUsingRangeDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * <p>Description: [优惠券]</p>
 * Created on 2015年12月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CouponsExportService {
	
	/**
	 * <p>Discription:[优惠券主表新增方法]</p>
	 * Created on 2015年12月3日
	 * @param couponsDTO 实体
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<String> addCoupons(CouponsDTO couponsDTO);
	
	/**
	 * <p>Discription:[优惠券用户关联表新增方法]</p>
	 * Created on 2015年12月3日
	 * @param couponsDTO 实体
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<String> addCouponsUser(CouponUserDTO couponUserDTO);
	
	/**
	 * <p>Discription:[优惠券子表使用范围新增方法]</p>
	 * Created on 2015年12月3日
	 * @param couponsDTO 实体
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<String> addCouponUsingRange(CouponUsingRangeDTO couponUsingRangeDTO);
	
	/**
	 * <p>Discription:[优惠券主表查询方法]</p>
	 * Created on 2015年12月4日
	 * @param CouponsDto 条件实体
	 * @param page	分页
	 * @return	优惠券实体list集合
	 * @author:[李伟龙]
	 */
	public ExecuteResult<DataGrid<CouponsDTO>> queryCouponsList(CouponsDTO couponsDto,Pager page);
	
	/**
	 * <p>Discription:[删除优惠券主表信息]</p>
	 * Created on 2015年12月4日
	 * @param couponsId	优惠券编号
	 * @return 删除的数量
	 * @author:[创建者中文名字]
	 */
	public int deleteCoupons(String couponsId);
	
	
	/**
	 * 
	 * <p>Discription:[删除用户优惠券]</p>
	 * Created on 2015年12月8日
	 * @param id	优惠券用户关联表主键
	 * @return
	 * @author:[李伟龙]
	 */
	public int deleteCouponsUser(Long id);
	
	/**
	 * <p>Discription:[优惠券用户关联表查询方法]</p>
	 * Created on 2015年12月4日
	 * @param CouponUserDTO 条件实体
	 * @param page	分页
	 * @return	优惠券实体list集合
	 * @author:[李伟龙]
	 */
	public ExecuteResult<DataGrid<CouponUserDTO>> queryCouponsUserList(CouponUserDTO couponUserDTO,Pager page);
	
	/**
	 * 
	 * <p>Discription:[查询已经领取的优惠券的数量]</p>
	 * Created on 2015年12月4日
	 * @param couponUserDTO 实体
	 * @return
	 * @author:[李伟龙]
	 */
	public Integer queryReceivedNumber(CouponUserDTO couponUserDTO);
	
	/**
	 * 
	 * <p>Description: [根据ID查询优惠券]</p>
	 * Created on 2015年12月7日
	 * @param couponId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<CouponsDTO> queryById(String couponId);
	
	/**
	 * <p>Discription:[优惠券子表信息查询 sku or shop or 平台]</p>
	 * Created on 2015年12月7日
	 * @param couponUsingRangeDTO 查询条件子表实体
	 * @param page
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<DataGrid<CouponUsingRangeDTO>> queryCouponUsingRangeList(CouponUsingRangeDTO couponUsingRangeDTO,Pager page);
	
	/**
	 * 
	 * <p>Discription:[update优惠券信息]</p>
	 * Created on 2015年12月7日
	 * @param couponsDTO 
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<String> updateCouponsInfo(CouponsDTO couponsDTO);
	
	/**
	 * <p>Discription:[更新优惠券用户关联表]</p>
	 * Created on 2015年12月8日
	 * @param couponUserDTO优惠券编号必须传
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<String> updateCouponUser(CouponUserDTO couponUserDTO);
	
	/**
	 * 
	 * <p>Discription:[根据优惠卷编号删除所有的的适用范围关联信息]</p>
	 * Created on 2015-12-8
	 * @param couponUsingRangeDTO
	 * @return
	 * @author:[王鹏]
	 */
	public int deleteCouponUsingRangeDTO(String couponId);
	
	/**
	 * 
	 * <p>Discription:[根据店铺id,用户id，skuid，类目ID获取可以领取的优惠券，包含平台优惠券]</p>
	 * Created on 2016年2月27日
	 * @param shopId
	 * @return
	 * @author:[李伟龙]
	 */
	public List<CouponsDTO> getCouponsByShopId(Long shopId,Long userId,List<Long> skuIdList ,List<Long> cIdList);
}
