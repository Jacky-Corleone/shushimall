package com.camelot.sellercenter.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.sellercenter.dao.MallAdCountDAO;
import com.camelot.sellercenter.dao.MallBannerDAO;
import com.camelot.sellercenter.domain.MallBanner;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.dto.MallBannerInDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;
import com.camelot.sellercenter.utils.DateDealUtils;

@Service("mallBannerExportService")
public class MallBannerExportServiceImpl implements MallBannerExportService {
	
	private static final Logger logger = LoggerFactory.getLogger(MallBannerExportServiceImpl.class);
	
	@Resource
	private MallBannerDAO mallBannerDAO;
	
	@Resource
	private MallAdCountDAO mallAdCountDAO;
	
	public DataGrid<MallBannerDTO> queryMallBannerList(String publishFlag, Integer bannerType, Pager page) {
		logger.debug("------------------前台轮播列表查询接口------------------");
		DataGrid<MallBannerDTO> dg = new DataGrid<MallBannerDTO>();
		try {
			/**
			 * publishFlag 展示标记默认为0
			 * 传入参数不正确也默认为0
			 */
			logger.debug("------------------前台轮播列表查询接口,传入参数publishFlag="+publishFlag+",page="+JSON.toJSONString(page));
//			if(StringUtils.isEmpty(publishFlag)){
//				publishFlag = "0";
//			}
//			if(!"0".equals(publishFlag) && !"1".equals(publishFlag)){
//				publishFlag = "0";
//			}
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = format.format(date);
			List<MallBannerDTO> list = this.mallBannerDAO.queryListDTO(nowTime, publishFlag, bannerType, page);
			dg.setTotal(this.mallBannerDAO.queryCountDTO(nowTime, publishFlag, bannerType));
			dg.setRows(list);
			logger.debug("------------------前台轮播列表查询接口，返回结果："+JSON.toJSONString(dg));
		} catch (Exception e) {
			logger.error("执行方法【queryMallBannerList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return dg;
	}
	public DataGrid<MallBannerDTO> queryMallBannerList(MallBannerDTO mallBannerDTO, String publishFlag, Pager page) {
		logger.debug("------------------后台轮播列表查询接口------------------");
		DataGrid<MallBannerDTO> dg = new DataGrid<MallBannerDTO>();
		try {
			/**
			 * publishFlag 展示标记默认为0 显示未生效数据   1 显示前台展示的数据
			 * 传入参数不正确也默认为0
			 */
			logger.debug("------------------后台轮播列表查询接口,传入参数publishFlag="+publishFlag+",page="+JSON.toJSONString(page)+",对象MallBannerDTO=="+JSON.toJSONString(mallBannerDTO));
//			if(StringUtils.isEmpty(publishFlag)){
//				publishFlag = "0";
//			}
//			if(!"0".equals(publishFlag) && !"1".equals(publishFlag)){
//				publishFlag = "0";
//			}
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = format.format(date);
			List<MallBannerDTO> list = this.mallBannerDAO.queryListDTOToAdmin(mallBannerDTO, nowTime, publishFlag, page);
			dg.setTotal(this.mallBannerDAO.queryCountDTOToAdmin(mallBannerDTO, nowTime, publishFlag));
			dg.setRows(list);
			logger.debug("------------------后台轮播列表查询接口，返回结果："+JSON.toJSONString(dg));
		} catch (Exception e) {
			logger.error("执行方法【queryMallBannerList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return dg;
	}
	public MallBannerDTO getMallBannerById(long id) {
		MallBannerDTO mallBannerDTO = new MallBannerDTO();
		try {
			logger.debug("------------------轮播详情查询接口------------------");
			MallBanner mallBanner = this.mallBannerDAO.queryById(id);
			logger.debug("------------------轮播详情查询接口，根据id【"+id+"】查询到对象信息："+JSON.toJSONString(mallBanner));
			mallBannerDTO.setId(mallBanner.getId());
			mallBannerDTO.setBannerLink(mallBanner.getBannerLink());
			mallBannerDTO.setBannerUrl(mallBanner.getBannerUrl());
			mallBannerDTO.setTitle(mallBanner.getTitle());
			mallBannerDTO.setSortNumber(mallBanner.getSortNumber());
			mallBannerDTO.setStatus(mallBanner.getStatus());
			mallBannerDTO.setCreated(mallBanner.getCreated());
			mallBannerDTO.setStartTime(mallBanner.getStartTime());
			mallBannerDTO.setEndTime(mallBanner.getEndTime());
			mallBannerDTO.setModified(mallBanner.getModified());
			mallBannerDTO.setThemeId(mallBanner.getThemeId());
			mallBannerDTO.setSkuId(mallBanner.getSkuId());
			mallBannerDTO.setIntegral(mallBanner.getIntegral());
			mallBannerDTO.setBannerType(mallBanner.getBannerType());
			logger.debug("------------------轮播详情查询接口，返回结果："+JSON.toJSONString(mallBannerDTO));
		} catch (Exception e) {
			logger.error("执行方法【getMallBannerById】报错！{}",e);
			throw new RuntimeException(e);
		}
		return mallBannerDTO;
	}

	
	public ExecuteResult<String> addMallBanner(MallBannerInDTO mallBannerInDTO) {
		logger.debug("------------------轮播添加架接口------------------");
		ExecuteResult<String> re = checkDTO(mallBannerInDTO);
		logger.debug("------------------轮播添加架接口，对传入对象MallBannerInDTO的校验结果："+JSON.toJSONString(re.getErrorMessages()));
		if(re.getErrorMessages()!=null && !re.getErrorMessages().isEmpty()){
			re.setResult("false");
			re.setResultMessage("修改失败");
			return re;
		}
		try {
			MallBanner mallBanner = new MallBanner();
			mallBanner.setBannerLink(mallBannerInDTO.getBannerLink());
			mallBanner.setBannerUrl(mallBannerInDTO.getBannerUrl());
			mallBanner.setTitle(mallBannerInDTO.getTitle());
			mallBanner.setSortNumber(mallBannerInDTO.getSortNumber());
			String status = mallBannerInDTO.getStatus();
			mallBanner.setStatus(mallBannerInDTO.getStatus());
			mallBanner.setSkuId(mallBannerInDTO.getSkuId());
			mallBanner.setIntegral(mallBannerInDTO.getIntegral());
			mallBanner.setBannerType(mallBannerInDTO.getBannerType());
			mallBanner.setThemeId(mallBannerInDTO.getThemeId());
			if(status!=null && "1".equals(status)){
				mallBanner.setStartTime(mallBannerInDTO.getStartTime());
				mallBanner.setEndTime(mallBannerInDTO.getEndTime());
				mallBanner.setStatus("0");
			}else{
				mallBanner.setStatus("1");
			}
			
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = format.format(date);
			mallBanner.setCreated(nowTime);
			mallBanner.setModified(nowTime);
			logger.debug("------------------轮播添加架接口，传入对象MallBannerInDTO转换成MallBanner对象结果："+JSON.toJSONString(mallBanner));
			this.mallBannerDAO.add(mallBanner);
			re.setResult("true");
			re.setResultMessage("保存成功");
		} catch (Exception e) {
			logger.error("执行方法【addMallBanner】报错！{}",e);
			re.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		logger.debug("------------------轮播详情查询接口，返回结果："+JSON.toJSONString(re));
		return re;
	}


	public ExecuteResult<String> modifyMallBanner(MallBannerInDTO mallBannerInDTO) {
		logger.debug("------------------轮播修改架接口------------------");
		ExecuteResult<String> re = checkDTO(mallBannerInDTO);
		logger.debug("------------------轮播修改架接口，对传入对象MallBannerInDTO的校验结果："+JSON.toJSONString(re.getErrorMessages()));
		if(re.getErrorMessages()!=null && !re.getErrorMessages().isEmpty()){
			re.setResult("false");
			re.setResultMessage("修改失败");
			return re;
		}
		try {
			MallBanner mallBanner = new MallBanner();
			mallBanner.setId(mallBannerInDTO.getId());
			mallBanner.setBannerLink(mallBannerInDTO.getBannerLink());
			mallBanner.setBannerUrl(mallBannerInDTO.getBannerUrl());
			mallBanner.setTitle(mallBannerInDTO.getTitle());
			mallBanner.setSortNumber(mallBannerInDTO.getSortNumber());
			mallBanner.setStatus(mallBannerInDTO.getStatus());
			mallBanner.setSkuId(mallBannerInDTO.getSkuId());
			mallBanner.setIntegral(mallBannerInDTO.getIntegral());
			mallBanner.setBannerType(mallBannerInDTO.getBannerType());
			mallBanner.setThemeId(mallBannerInDTO.getThemeId());
			String timeFlag = mallBannerInDTO.getTimeFlag();
			if(timeFlag!=null && "0".equals(timeFlag)){
				if(mallBannerInDTO.getStartTime()!=null && mallBannerInDTO.getStartTime().toString().length()>0){
					mallBanner.setStartTime(mallBannerInDTO.getStartTime());
				}else{
					mallBanner.setStartTime(null);
				}
				if(mallBannerInDTO.getEndTime()!=null  && mallBannerInDTO.getEndTime().toString().length()>0){
					mallBanner.setEndTime(mallBannerInDTO.getEndTime());
				}else{
					mallBanner.setEndTime(null);
				}
			}else{
				mallBanner.setStartTime(null);
				mallBanner.setEndTime(null);
			}
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = format.format(date);
			mallBanner.setModified(nowTime);
			logger.debug("------------------轮播修改架接口，传入对象MallBannerInDTO转换成MallBanner对象："+JSON.toJSONString(mallBanner));
			Integer num = this.mallBannerDAO.update(mallBanner);
			if(num>0){
				re.setResult("true");
				re.setResultMessage("修改成功");
			}else{
				re.setResult("false");
				re.setResultMessage("修改失败");
			}
		} catch (Exception e) {
			logger.error("执行方法【modifyMallBanner】报错！{}",e);
			re.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		logger.debug("------------------轮播修改架接口，返回结果："+JSON.toJSONString(re));
		return re;
	}

	public ExecuteResult<String> motifyMallBannerStatus(Long id, String publishFlag) {
		logger.debug("------------------轮播上下架接口------------------");
		ExecuteResult<String> re = new ExecuteResult<String>();
		try {
			boolean bl = checkPublishFlag(publishFlag);
			logger.debug("------------------轮播上下架接口,参数校验结果："+bl);
			if(bl){
				Integer num = this.mallBannerDAO.updateStatusById(id, publishFlag);
				if(num>0){
					re.setResult("true");
					re.setResultMessage("修改成功");
				}else{
					re.setResult("false");
					re.setResultMessage("修改失败");
				}
			}else{
				List<String> errorMessages = new ArrayList<String>();
				errorMessages.add("上下架标记不合法");
				re.setErrorMessages(errorMessages);
			}
			logger.debug("------------------轮播上下架接口，返回结果："+JSON.toJSONString(re));
		} catch (Exception e) {
			logger.error("执行方法【motifyMallBannerStatus】报错！{}",e);
			re.getErrorMessages().add(e.getMessage());
		}
		return re;
	}

	public ExecuteResult<String> motifyMallBannerStatusBatch(String publishFlag,Long... ids) {
		logger.debug("------------------轮播上下架接口------------------");
		ExecuteResult<String> re = new ExecuteResult<String>();
		try {
			boolean bl = checkPublishFlag(publishFlag);
			logger.debug("------------------轮播上下架接口,参数校验结果："+bl);
			if(bl){
				Integer num = this.mallBannerDAO.updateBatchStatusById(publishFlag, ids);
				if(num>0){
					re.setResult("true");
					re.setResultMessage("修改成功");
				}else{
					re.setResult("false");
					re.setResultMessage("修改失败");
				}
			}else{
				List<String> errorMessages = new ArrayList<String>();
				errorMessages.add("上下架标记不合法");
				re.setErrorMessages(errorMessages);
			}
			logger.debug("------------------轮播上下架接口，返回结果："+JSON.toJSONString(re));
		} catch (Exception e) {
			logger.error("执行方法【motifyMallBannerStatus】报错！{}",e);
			re.getErrorMessages().add(e.getMessage());
		}
		return re;
	}
	public ExecuteResult<String> modifyMallBannerSort(Long id, Integer sortNum) {
		logger.debug("------------------轮播排序架接口------------------");
		ExecuteResult<String> re = new ExecuteResult<String>();
		try {
			List<String> errorMessages = new ArrayList<String>();
			logger.debug("------------------轮播排序架接口,传入参数id="+id+",sortNum="+sortNum);
			if(sortNum==null){
				re.setResult("false");
				errorMessages.add("排序号能为空！");
				re.setErrorMessages(errorMessages);
				return re;
			}else if(sortNum<0){
				re.setResult("false");
				errorMessages.add("排序号不合法，必须是大于0的正整数！");
				re.setErrorMessages(errorMessages);
				return re;
			}
			Integer num = this.mallBannerDAO.updateSortNumberById(id, sortNum);
			if(num>0){
				re.setResult("true");
				re.setResultMessage("修改成功");
			}else{
				re.setResult("false");
				re.setResultMessage("修改失败");
			}
			logger.debug("------------------轮播上下架接口，返回结果："+JSON.toJSONString(re));
		} catch (Exception e) {
			logger.error("执行方法【modifyMallBannerSort】报错！{}",e);
			re.getErrorMessages().add(e.getMessage());
		}
		return re;
	}
	
	private ExecuteResult<String> checkDTO(MallBannerInDTO mallBannerInDTO){
		ExecuteResult<String> re = new ExecuteResult<String>();
		List<String> errorMessages = new ArrayList<String>();
		if(StringUtils.isEmpty(mallBannerInDTO.getBannerUrl())){
			errorMessages.add("图片URL不能为空！");
		}
		if (mallBannerInDTO.getBannerType() == BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue()) {
			if (StringUtils.isEmpty(mallBannerInDTO.getBannerLink())) {
				errorMessages.add("跳转链接不能为空！");
			}
		} else if (mallBannerInDTO.getBannerType() == BasicEnum.INT_ENUM_THEMETYPE_INTEGRAL.getIntVlue()) {
			if (mallBannerInDTO.getSkuId() == null) {
				errorMessages.add("SkuId不能为空！");
			}
		}
		if (mallBannerInDTO.getBannerType() == BasicEnum.INT_ENUM_THEMETYPE_INTEGRAL.getIntVlue()) {
			if (mallBannerInDTO.getIntegral() == null) {
				errorMessages.add("积分不能为空！");
			} else {
				Integer integral = mallBannerInDTO.getIntegral();
				if (integral < 0 || integral > 10000) {
					errorMessages.add("积分不合法，必须是大于0小于10000的整数！");
				}
			}
		}
		if(StringUtils.isEmpty(mallBannerInDTO.getTitle())){
			errorMessages.add("轮播图名称不能为空！");
		}else if(mallBannerInDTO.getTitle()!=null && mallBannerInDTO.getTitle().length()>30){
			errorMessages.add("轮播图名称不能超过30字！");
		}
		Integer sortNumber = mallBannerInDTO.getSortNumber();
		if(sortNumber==null){
			errorMessages.add("排序号能为空！");
		}else if(sortNumber<0 || sortNumber>1000){
			errorMessages.add("排序号不合法，必须是大于0小于1000的整数！");
		}
//		if(StringUtils.isEmpty(mallBannerInDTO.getTimeFlag())){
//			errorMessages.add("时间标记不能为空！");
//		}
		re.setErrorMessages(errorMessages);
		return re;
	}
	private boolean checkPublishFlag(String publishFlag){
		if(StringUtils.isEmpty(publishFlag)){
			logger.error("轮播列表查询,传入的参数展示标记publishFlag为空");
			return false;
		}
		if(!"0".equals(publishFlag) && !"1".equals(publishFlag)){
			logger.error("轮播列表查询,传入的参数展示标记publishFlag不合法");
			return false;
		}
		return true;
	}
	@Override
	public DataGrid<AdReportOutDto> queryReportList(AdReportInDto adReportInDto, Pager pager) {
		//去掉日期格式
		adReportInDto.setClickDate(DateDealUtils.dateWithoutFormat(adReportInDto.getClickDate()));
		adReportInDto.setClickDateBegin(DateDealUtils.dateWithoutFormat(adReportInDto.getClickDateBegin()));
		adReportInDto.setClickDateEnd(DateDealUtils.dateWithoutFormat(adReportInDto.getClickDateEnd()));
		DataGrid<AdReportOutDto> dtos=new DataGrid<AdReportOutDto>();
		List<AdReportOutDto> res=mallAdCountDAO.queryBannerReportList(adReportInDto, pager);
		long count=mallAdCountDAO.queryBannerReportCount(adReportInDto);
		if(adReportInDto.getDateFormat()!=null && adReportInDto.getDateFormat().length()>0){
			for (AdReportOutDto dto : res) {
				dto.setClickDate(DateDealUtils.getDateStrToStr(dto.getClickDate(), "yyyyMMdd", adReportInDto.getDateFormat()));
			}
		}
		dtos.setRows(res);
		dtos.setTotal(count);
		return dtos;
	}
	/**
	 * 自动批量上架，定时上架时间小于当前时间
	 * 
	 * @return
	 */
	@Override
	public ExecuteResult<Integer> autoBatchOffShelves(){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		Integer count = mallBannerDAO.autoBatchOffShelves();
		result.setResult(count);
		return result;
	}
	/**
	 * 自动批量下架，定时下架时间小于当前时间
	 * 
	 * @return
	 */
	@Override
	public ExecuteResult<Integer> autoBatchRelease(){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		Integer count = mallBannerDAO.autoBatchRelease();
		result.setResult(count);
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[删除轮播图]</p>
	 * Created on 2015年12月29日
	 * @param id
	 * @return
	 * @author:[化亚会]
	 */
	public boolean delete(MallBannerInDTO mallBannerInDTO){
		boolean bol = false;
		MallBanner mallBanner = new MallBanner();
		mallBanner.setId(mallBannerInDTO.getId());
		mallBanner.setStatus(mallBannerInDTO.getStatus());
		try {
			mallBannerDAO.update(mallBanner);
			bol = true;
		} catch (Exception e) {
			bol = false;
			e.printStackTrace();
		}
		return bol;
	}
}