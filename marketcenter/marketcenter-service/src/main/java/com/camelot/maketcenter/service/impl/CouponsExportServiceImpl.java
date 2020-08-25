package com.camelot.maketcenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.maketcenter.dao.CouponsDao;
import com.camelot.maketcenter.dao.CouponsUserDao;
import com.camelot.maketcenter.dao.CouponsUsingRangeDao;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponUsingRangeDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [优惠券]</p>
 * Created on 2015年12月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("couponsExportService")
public class CouponsExportServiceImpl implements CouponsExportService {
	private final static Logger logger = LoggerFactory.getLogger(CouponsExportServiceImpl.class);
	@Resource
    private CouponsDao couponsDao;
	@Resource
    private CouponsUsingRangeDao couponsUsingRangeDao;
	@Resource
    private CouponsUserDao couponsUserDao;
	
	/**
	 * <p>Discription:[优惠券主表新增方法]</p>
	 * Created on 2015年12月3日
	 * @param couponsDTO 实体
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<String> addCoupons(CouponsDTO couponsDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
        	couponsDao.add(couponsDTO);
            result.setResult("");
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

	/**
	 * <p>Discription:[优惠券用户关联表新增方法]</p>
	 * Created on 2015年12月3日
	 * @param couponsDTO 实体
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<String> addCouponsUser(CouponUserDTO couponUserDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
        	couponsUserDao.add(couponUserDTO);
            result.setResult("");
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

	/**
	 * <p>Discription:[优惠券子表使用范围新增方法]</p>
	 * Created on 2015年12月3日
	 * @param couponsDTO 实体
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<String> addCouponUsingRange(CouponUsingRangeDTO couponUsingRangeDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
        	couponsUsingRangeDao.add(couponUsingRangeDTO);
            result.setResult("");
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }
	
	/**
	 * <p>Discription:[优惠券主表查询方法]</p>
	 * Created on 2015年12月4日
	 * @param CouponsDto 条件实体
	 * @param page	分页
	 * @return	优惠券实体list集合
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<DataGrid<CouponsDTO>> queryCouponsList(CouponsDTO couponsDto, Pager page) {
		ExecuteResult<DataGrid<CouponsDTO>> result=new ExecuteResult<DataGrid<CouponsDTO>>();
		try {
			DataGrid<CouponsDTO> dataGrid=new DataGrid<CouponsDTO>();
			List<CouponsDTO> list = couponsDao.queryList(couponsDto, page);
			Long count = couponsDao.queryCount(couponsDto);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * <p>Discription:[删除优惠券主表信息]</p>
	 * Created on 2015年12月4日
	 * @param couponsId	优惠券编号
	 * @return 删除的数量
	 * @author:[李伟龙]
	 */
	@Override
	public int deleteCoupons(String couponsId) {
		return couponsDao.delete(couponsId);
	}

	/**
	 * <p>Discription:[优惠券用户关联表查询方法]</p>
	 * Created on 2015年12月4日
	 * @param CouponUserDTO 条件实体
	 * @param page	分页
	 * @return	优惠券实体list集合
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<DataGrid<CouponUserDTO>> queryCouponsUserList(CouponUserDTO couponUserDTO, Pager page) {
		ExecuteResult<DataGrid<CouponUserDTO>> result=new ExecuteResult<DataGrid<CouponUserDTO>>();
		try {
			DataGrid<CouponUserDTO> dataGrid=new DataGrid<CouponUserDTO>();
			List<CouponUserDTO> list = couponsUserDao.queryList(couponUserDTO, page);
			Long count = couponsUserDao.queryCount(couponUserDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	
	/**
	 * <p>Discription:[查询已经领取的优惠券的数量]</p>
	 * Created on 2015年12月4日
	 * @param couponUserDTO 实体
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public Integer queryReceivedNumber(CouponUserDTO couponUserDTO) {
		Long count = couponsUserDao.queryCount(couponUserDTO);
		return count.intValue();
	}

	@Override
	public ExecuteResult<CouponsDTO> queryById(String couponId) {
		logger.info("\n 方法[{}]，入参：[{}]", "CouponsExportServiceImpl-queryById", couponId);
		ExecuteResult<CouponsDTO> result = new ExecuteResult<CouponsDTO>();
		try {
			CouponsDTO dto = couponsDao.queryById(couponId);
			result.setResult(dto);
		} catch (Exception e) {
			logger.error("CouponsExportServiceImpl-queryById", e);
			result.addErrorMessage("查询优惠券失败！");
		}
		return result;
	}
	
	/**
	 * <p>Discription:[优惠券子表信息查询 sku or shop or 平台]</p>
	 * Created on 2015年12月7日
	 * @param couponUsingRangeDTO 查询条件子表实体
	 * @param page
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<DataGrid<CouponUsingRangeDTO>> queryCouponUsingRangeList(
			CouponUsingRangeDTO couponUsingRangeDTO, Pager page) {
		ExecuteResult<DataGrid<CouponUsingRangeDTO>> result=new ExecuteResult<DataGrid<CouponUsingRangeDTO>>();
		try {
			DataGrid<CouponUsingRangeDTO> dataGrid=new DataGrid<CouponUsingRangeDTO>();
			List<CouponUsingRangeDTO> list = couponsUsingRangeDao.queryList(couponUsingRangeDTO, page);
			Long count = couponsUsingRangeDao.queryCount(couponUsingRangeDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * <p>Discription:[update优惠券信息]</p>
	 * Created on 2015年12月7日
	 * @param couponsDTO
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<String> updateCouponsInfo(CouponsDTO couponsDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
            Integer num=couponsDao.update(couponsDTO);
            if(num>0){
                result.setResultMessage("success");
            }else{
                result.setResultMessage("error");
            }
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }
	
	/**
	 * <p>Discription:[删除用户优惠券]</p>
	 * Created on 2015年12月8日
	 * @param id优惠券用户关联表主键
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public int deleteCouponsUser(Long id) {
		return couponsUserDao.delete(id);
	}
	
	/**
	 * <p>Discription:[更新优惠券用户关联表]</p>
	 * Created on 2015年12月8日
	 * @param couponUserDTO优惠券编号必须传
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<String> updateCouponUser(CouponUserDTO couponUserDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
            Integer num=couponsUserDao.update(couponUserDTO);
            if(num>0){
                result.setResultMessage("success");
            }else{
                result.setResultMessage("error");
            }
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

	@Override
	public int deleteCouponUsingRangeDTO(String couponId) {
		
		if("".equals(couponId)){
			return 0;
		}
		return couponsUsingRangeDao.delete(couponId);
	}

	/**
	 * 
	 * <p>Discription:[根据店铺id获取可以领取的优惠券，包含平台优惠券]</p>
	 * Created on 2016年2月27日
	 * @param shopId
	 * @param userId 当前登录用户id
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public List<CouponsDTO> getCouponsByShopId(Long shopId,Long userId,List<Long> skuIdList ,List<Long> cIdList) {
		List<CouponsDTO> list = new ArrayList<CouponsDTO>();
		
		CouponsDTO couponsDTO = new CouponsDTO();
		Pager<CouponsDTO> pager = new Pager<CouponsDTO>();
		pager.setRows(Integer.MAX_VALUE);
		// 只查询已经开始和未开始的优惠券
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(0);
		stateList.add(1);
		couponsDTO.setStateList(stateList);
		// 只查询未发放用户的优惠券,首页优惠券领取
		couponsDTO.setSendCouponType(1);
		//查询不过期的优惠券
		couponsDTO.setIsEffective("5");
		//平台优惠券
		CouponsDTO ecmDto = couponsDTO;
		// 只显示平台的优惠券
		ecmDto.setCostAllocation(1);
		List<CouponsDTO> ecmList = couponsDao.queryList(ecmDto, pager);
		//店铺优惠卷
		CouponsDTO shopDto = couponsDTO;
		// 只显示店铺的优惠券
		shopDto.setCostAllocation(2);
		shopDto.setShopId(shopId);
		List<CouponsDTO> shopList = couponsDao.queryList(shopDto, pager);
		//查询是否领取过优惠券变量
		CouponUserDTO couponUserDTO = new CouponUserDTO();
		if (userId == null ) {
			userId=0L;
		}
		couponUserDTO.setUserId(userId);
		couponUserDTO.setDeleted(1);
		
		// 保存用户优惠券变量
		CouponUserDTO couponsIsGetAll = new CouponUserDTO();
		// 优惠券编号
		couponsIsGetAll.setDeleted(1);
		//添加ecm优惠券
		if(null != ecmList && !ecmList.isEmpty() && ecmList.size()>0){
			for(int  i=0; i < ecmList.size() ; i++){
				//判断用户是否领取过优惠券   
				couponUserDTO.setCouponId(ecmList.get(i).getCouponId());
				// 判断是否超出优惠券的最大领取数量
				couponsIsGetAll.setCouponId(ecmList.get(i).getCouponId());
				if(0 == couponsUserDao.queryCount(couponUserDTO).intValue() && ecmList.get(i).getCouponNum() > queryReceivedNumber(couponsIsGetAll)){
					//判断是否限制店铺 ,限制品类 ，限制sku
					if(isGetCoupon(ecmList.get(i),shopId,skuIdList,cIdList)){
						// 优惠券描述
						if (ecmList.get(i).getCouponType() == 1) {
							ecmList.get(i).setDescr("满" + moneyToSting(ecmList.get(i).getMeetPrice().toString()) + "元，减" + moneyToSting(ecmList.get(i).getCouponAmount().toString()) + "元");
						} else if (ecmList.get(i).getCouponType() == 2) {
							ecmList.get(i).setDescr(moneyToSting(ecmList.get(i).getCouponAmount().toString()) + "折，限额" + moneyToSting(ecmList.get(i).getCouponMax().toString()) + "元");
						} else if (ecmList.get(i).getCouponType() == 3) {
							ecmList.get(i).setDescr("直降" + moneyToSting(ecmList.get(i).getCouponAmount().toString()) + "元");
						}
						list.add(ecmList.get(i));
					}
				}
			}
		}
		
		//添加店铺
		if(null != shopList && !shopList.isEmpty() && shopList.size()>0){
			for(int  i=0; i < shopList.size() ; i++){
				//判断用户是否领取过优惠券
				couponUserDTO.setCouponId(shopList.get(i).getCouponId());
				// 判断是否超出优惠券的最大领取数量
				couponsIsGetAll.setCouponId(shopList.get(i).getCouponId());
				if(0 == couponsUserDao.queryCount(couponUserDTO).intValue() && shopList.get(i).getCouponNum() > queryReceivedNumber(couponsIsGetAll)){
					//判断是否限制店铺 ,限制品类 ，限制sku
					if(isGetCoupon(shopList.get(i),shopId,skuIdList,cIdList)){
						// 优惠券描述
						if (shopList.get(i).getCouponType() == 1) {
							shopList.get(i).setDescr("满" + moneyToSting(shopList.get(i).getMeetPrice().toString()) + "元，减" + moneyToSting(shopList.get(i).getCouponAmount().toString()) + "元");
						} else if (shopList.get(i).getCouponType() == 2) {
							shopList.get(i).setDescr(moneyToSting(shopList.get(i).getCouponAmount().toString()) + "折，限额" + moneyToSting(shopList.get(i).getCouponMax().toString()) + "元");
						} else if (shopList.get(i).getCouponType() == 3) {
							shopList.get(i).setDescr("直降" + moneyToSting(shopList.get(i).getCouponAmount().toString()) + "元");
						}
						list.add(shopList.get(i));
					}
				}
			}
		}
		return list;
	}
	
	/**
     * <p>Discription:[去掉最后边无用的0]</p>
     * Created on 2015年12月30日
     * @param str
     * @return
     * @author:[李伟龙]
     */
    public String moneyToSting(String str) {
	while (str.endsWith("0")) {
	    str = str.substring(0, str.length() - 1);
	}
	if (str.endsWith(".")) {
	    str = str.substring(0, str.length() - 1);
	}
	return str;
    }
	
	public boolean isGetCoupon(CouponsDTO couponsDTO,Long shopId ,List<Long> skuIdList ,List<Long> cIdList){
		boolean is = false;
		CouponUsingRangeDTO couponUsingRangeDTO = new CouponUsingRangeDTO();
		couponUsingRangeDTO.setCouponId(couponsDTO.getCouponId());
		// 优惠券使用范围(1:平台通用类,2:店铺通用类,3:品类通用类,4:SKU使用类)
		if(couponsDTO.getCouponUsingRange() == 2){
			couponUsingRangeDTO.setCouponUsingId(shopId);
			if(couponsUsingRangeDao.queryCount(couponUsingRangeDTO).intValue() > 0){
				is = true;
			}
		}else if (couponsDTO.getCouponUsingRange() == 3){
			couponUsingRangeDTO.setSkuIdList(null);
			couponUsingRangeDTO.setcIdList(cIdList);
			if(couponsUsingRangeDao.queryCount(couponUsingRangeDTO).intValue() > 0){
				is = true;
			}
		}else if (couponsDTO.getCouponUsingRange() == 4){
			couponUsingRangeDTO.setcIdList(null);
			couponUsingRangeDTO.setSkuIdList(skuIdList);
			if(couponsUsingRangeDao.queryCount(couponUsingRangeDTO).intValue() > 0){
				is = true;
			}
		}else {
			is = true;
		}
		return is;
	}
	
}
