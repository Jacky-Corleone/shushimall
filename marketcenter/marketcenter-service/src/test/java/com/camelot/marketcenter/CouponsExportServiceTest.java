package com.camelot.marketcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.dto.emums.CouponUsingPlatformEnum;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public class CouponsExportServiceTest {
	private CouponsExportService couponsExportService;
	ApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		couponsExportService = (CouponsExportService) ctx.getBean("couponsExportService");
	}
	
	/**
	 * <p>Discription:[测试新增优惠券主表]</p>
	 * Created on 2015年12月3日
	 * @author:[李伟龙]
	 */
//	@Test
//	public void testaddCoupons() {
//		CouponsDTO couponsDTO = new CouponsDTO();
//		couponsDTO.setCouponEndTime(new Date());
//		String StrBd="10.24";   
//		BigDecimal bd=new BigDecimal(StrBd);
//		couponsDTO.setCouponAmount(bd);
//		couponsDTO.setShopId(8898l);
//		couponsDTO.setCouponMax(bd);
//		couponsDTO.setCouponEndTime(new Date());
//		couponsDTO.setCouponId("21222222222222222");
//		couponsDTO.setCouponName("优惠券测试");
//		couponsDTO.setCouponNum(222);
//		couponsDTO.setCouponStartTime(new Date());
//		couponsDTO.setCouponType(1);
//		couponsDTO.setCouponUserMembershipLevel(2);
//		couponsDTO.setCouponUserType(3);
//		couponsDTO.setCouponUsingRange(4);
//		couponsDTO.setDeleted(0);
//		couponsDTO.setMeetPrice(bd);
//		couponsDTO.setPlatformId(2);
//		couponsDTO.setSendCouponType(5);
//		couponsDTO.setState(6);
//		couponsDTO.setUpdateDate(new Date());
//		couponsDTO.setSendCouponType(7);
//		couponsDTO.setCreateUser(111l);
//		couponsDTO.setVerifyUser(222l);
//		couponsExportService.addCoupons(couponsDTO);
//	}
	
	/**
	 * <p>Discription:[测试新增优惠券用户关联表信息]</p>
	 * Created on 2015年12月3日
	 * @author:[李伟龙]
	 */
//	@Test
//	public void testaddCouponsUser() {
//		CouponUserDTO couponUserDTO = new CouponUserDTO();
//		couponUserDTO.setCouponReceiveTime(new Date());
//		couponUserDTO.setCouponId("2122");
//		couponUserDTO.setDeleted(0);
//		couponUserDTO.setUserCouponType(1);
//		couponUserDTO.setUserId(12782987);
//		couponsExportService.addCouponsUser(couponUserDTO);
//	}
	
	/**
	 * <p>Discription:[测试新增优惠券使用范围表信息]</p>
	 * Created on 2015年12月3日
	 * @author:[李伟龙]
	 */
//	@Test
//	public void testaddCouponUsingRange() {
//		CouponUsingRangeDTO couponUsingRangeDTO = new CouponUsingRangeDTO();
//		couponUsingRangeDTO.setCouponId("2122");
//		couponUsingRangeDTO.setCouponUsingId(280928l);
//		couponUsingRangeDTO.setRemark("23133");
//		couponsExportService.addCouponUsingRange(couponUsingRangeDTO);
//	}
	
//	/**
//	 * <p>Discription:[测试查询优惠券方法]</p>
//	 * Created on 2015年12月3日
//	 * @author:[李伟龙]
//	 */
//	@Test
//	public void testqueryCouponsList() {
//		Pager page = new Pager() ;
//		page.setRows(10);
//		page.setPage(1);
//		CouponsDTO CouponsDto = new CouponsDTO();
//		List<Integer> platformIdList = new ArrayList<Integer>();
//	    	platformIdList.add(3);
//	    	platformIdList.add(1);
//	    	CouponsDto.setPlatformIdList(platformIdList);
//		couponsExportService.queryCouponsList(CouponsDto , page);
//	}
	
	/**
	 * <p>Discription:[测试查询优惠券方法]</p>
	 * Created on 2015年12月3日
	 * @author:[李伟龙]
	 */
//	@Test
//	public void testqueryCouponsList() {
//		Pager page = new Pager() ;
//		page.setRows(10);
//		page.setPage(1);
//		CouponsDTO CouponsDto = new CouponsDTO();
//		CouponsDto.setCouponId("y22222222222");
//		couponsExportService.queryCouponsList(CouponsDto , page);
//	}
//		@Test
//	public void testCouponUsingRangeList() {
//		Pager page = new Pager() ;
//		page.setRows(10);
//		page.setPage(1);
//		CouponUsingRangeDTO couponUsingRangeDTO = new CouponUsingRangeDTO();
//		couponUsingRangeDTO.setCouponId("y20151207124156193");
//		ExecuteResult<DataGrid<CouponUsingRangeDTO>> couponUsingRangeResult= couponsExportService.queryCouponUsingRangeList(couponUsingRangeDTO, page);
//		
//	}
//	@Test
//	public void testUpdateCouponInfo() {
//		ExecuteResult<CouponsDTO> resutlt = couponsExportService.queryById("y20151207141118156");
//		CouponsDTO dto = resutlt.getResult();
//		dto.setCouponName("测试修改名称");
//		couponsExportService.updateCouponsInfo(dto);
//	}

	
	/**
	 * <p>Discription:[测试查询优惠券方法]</p>
	 * Created on 2015年12月3日
	 * @author:[李伟龙]
	 */
//	@Test
//	public void testdeleteCoupons() {
//		couponsExportService.deleteCoupons("y22222222222");
//	}
//	
//	@Test
//	public void testQueryById() {
//		couponsExportService.queryById("123");
//	}
	
	//删除用户优惠券
//	@Test
//	public void testdeleteCouponsUser() {
//		couponsExportService.deleteCouponsUser(42l);
//	}
	
	
	//更新用户优惠券
//	@Test
//	public void testUpdateCouponInfo() {
//		CouponUserDTO cc = new CouponUserDTO();
//		cc.setCouponId("224545");
//		cc.setDeleted(1);
//		cc.setUserId(29093783L);
//		couponsExportService.updateCouponUser(cc);
//	}
	
	//删除适用范围关联表
//	@Test
//	public void testDeleteCouponUsingRangeDTO(){
	
//		couponsExportService.deleteCouponUsingRangeDTO("121");
//	}
	
	//查询我的优惠券方法
//	@Test
//	public void testQueryCouponsUserList(){
//	    	CouponUserDTO couponUserDTO = new CouponUserDTO();
//	    	couponUserDTO.setUserId(1000000008l);
//	    	List<Integer> platformIdList = new ArrayList<Integer>();
//	    	platformIdList.add(3);
//	    	platformIdList.add(1);
//	    	couponUserDTO.setPlatformIdList(platformIdList);
//	    	couponUserDTO.setDeleted(1);
//		couponsExportService.queryCouponsUserList(couponUserDTO , new Pager());
//	}
	
	//查询可领取的优惠券
	@Test
	public void getCouponsByShopIdTest(){
		List<Long> skuIdList = new ArrayList<Long>();
		skuIdList.add(1000000125L);
		List<Long> cIdList = new ArrayList<Long>();
		cIdList.add(538L);
		couponsExportService.getCouponsByShopId(2000000002L, null,skuIdList ,cIdList);
	}
	
}
