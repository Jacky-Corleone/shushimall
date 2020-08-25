package com.camelot.goodscenter.service.impl;

import com.camelot.goodscenter.dao.*;
import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.goodscenter.dto.InquiryMatDTO;
import com.camelot.goodscenter.dto.InquiryOrderDTO;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/** 
 * <p>Description: [协议功能接口实现]</p>
 * Created on 2015年06月13日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("inquiryExportService")
public class InquiryExportServiceImpl implements InquiryExportService {

	@Resource
	private InquiryInfoDAO inquiryInfoDAO;
	@Resource
	private InquiryMatDAO inquiryMatDAO;
	@Resource
	private InquiryOrderDAO inquiryOrderDAO;
	/**
	 * <p>Discription:[根据条件查询详情接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:[鲁鹏]
	 */
	  public ExecuteResult<InquiryInfoDTO> queryByInquiryInfo(InquiryInfoDTO dto){
		  ExecuteResult<InquiryInfoDTO> er=new  ExecuteResult<InquiryInfoDTO>();
		  InquiryInfoDTO inquiryInfoDTO=inquiryInfoDAO.findByInquiryInfoDTO(dto);
		  try{
			  if(inquiryInfoDTO != null){
				  er.setResult(inquiryInfoDTO);
				  er.setResultMessage("success");
			  }else{
				 er.setResultMessage("您要查询的数据不存在"); 
			  }
		  }catch(Exception e){
			  er.setResultMessage(e.getMessage());
			  throw new RuntimeException(e);
		  }
		  return er;
	  }
	/**
	 * <p>Discription:[根据条件查询协议的列表接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<DataGrid<InquiryInfoDTO>> queryInquiryInfoList(InquiryInfoDTO dto, Pager page){
		ExecuteResult<DataGrid<InquiryInfoDTO>> er=new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		DataGrid<InquiryInfoDTO> dg=new DataGrid<InquiryInfoDTO>();
		List<InquiryInfoDTO> list=inquiryInfoDAO.queryPage(page, dto);
		Long count=inquiryInfoDAO.queryPageCount(dto);
		try{
			if(list != null ){
				dg.setRows(list);
				dg.setTotal(count);
				er.setResult(dg);
			}else{
				er.setResultMessage("要查询的数据不存在");
			}

			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据条件查询协议的列表包含物料名称等信息接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<DataGrid<Map>> queryInquiryInfoPager(InquiryInfoDTO dto, Pager page){
		ExecuteResult<DataGrid<Map>> er=new ExecuteResult<DataGrid<Map>>();
		DataGrid<Map> dg=new DataGrid<Map>();
		List<Map> inquiryInfoDTOs=inquiryInfoDAO.queryInquiryInfoPager(page, dto);
		Long count=inquiryInfoDAO.queryInquiryInfoPagerCount(dto);
		try{
			if(inquiryInfoDTOs != null ){
				dg.setRows(inquiryInfoDTOs);
				dg.setTotal(count);
				er.setResult(dg);
			}else{
				er.setResultMessage("要查询的数据不存在");
			}

			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据条件查询协议明细详情接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<InquiryMatDTO> queryByInquiryMat(InquiryMatDTO dto){
		ExecuteResult<InquiryMatDTO> er=new  ExecuteResult<InquiryMatDTO>();
		InquiryMatDTO inquiryMatDTO=inquiryMatDAO.findByInquiryMatDTO(dto);
		try{
			if(inquiryMatDTO != null){
				er.setResult(inquiryMatDTO);
				er.setResultMessage("success");
			}else{
				er.setResultMessage("您要查询的数据不存在");
			}
		}catch(Exception e){
			er.setResultMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据条件查询协议明细的列表接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<DataGrid<Map>> queryInquiryMatList(InquiryMatDTO dto, Pager page){
		ExecuteResult<DataGrid<Map>> er=new ExecuteResult<DataGrid<Map>>();
		DataGrid<Map> dg=new DataGrid<Map>();
		List<Map> list=inquiryMatDAO.queryPage(page, dto);
		Long count=inquiryMatDAO.queryPageCount(dto);
		try{
			if(list != null ){
				dg.setRows(list);
				dg.setTotal(count);
				er.setResult(dg);
			}else{
				er.setResultMessage("要查询的数据不存在");
			}

			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据条件查询协议订单详情]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<InquiryOrderDTO> queryByInquiryOrder(InquiryOrderDTO dto){
		ExecuteResult<InquiryOrderDTO> er=new  ExecuteResult<InquiryOrderDTO>();
		InquiryOrderDTO inquiryOrderDTO=inquiryOrderDAO.findByInquiryOrderDTO(dto);
		try{
			if(inquiryOrderDTO != null){
				er.setResult(inquiryOrderDTO);
				er.setResultMessage("success");
			}else{
				er.setResultMessage("您要查询的数据不存在");
			}
		}catch(Exception e){
			er.setResultMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据条件查询协议订单的列表]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<DataGrid<InquiryOrderDTO>> queryInquiryOrderList(InquiryOrderDTO dto, Pager page){
		ExecuteResult<DataGrid<InquiryOrderDTO>> er=new ExecuteResult<DataGrid<InquiryOrderDTO>>();
		DataGrid<InquiryOrderDTO> dg=new DataGrid<InquiryOrderDTO>();
		List<InquiryOrderDTO> list=inquiryOrderDAO.queryPage(page, dto);
		Long count=inquiryOrderDAO.queryPageCount(dto);
		try{
			if(list != null ){
				dg.setRows(list);
				dg.setTotal(count);
				er.setResult(dg);
			}else{
				er.setResultMessage("要查询的数据不存在");
			}

			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[生成协议详情接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> addInquiryInfo(InquiryInfoDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		try{
			if(dto.getInquiryNo()==null || dto.getInquiryNo().equals(0L)){
				er.setResultMessage("询价编码不能为空");
				return er;
			}
			inquiryInfoDAO.insert(dto);
			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[生成协议详情接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> addInquiryMat(InquiryMatDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		try{
			if(dto.getInquiryNo()==null || dto.getInquiryNo().equals(0L)){
				er.setResultMessage("询价编码不能为空");
				return er;
			}
			inquiryMatDAO.insert(dto);
			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[生成协议订单]</p>
	 * Created on 2015年06月10日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> addInquiryOrder(InquiryOrderDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		try{
			if(dto.getInquiryNo()==null || dto.getInquiryNo().equals(0L)){
				er.setResultMessage("协议编码不能为空");
				return er;
			}

			//判断传递订单号是多个还是一个
			if(dto.getOrderNos()!=null && dto.getOrderNos().size()>0){
				for (String orderNo:dto.getOrderNos()){
					dto.setOrderNo(orderNo);
					inquiryOrderDAO.insert(dto);
				}
			}else{
				inquiryOrderDAO.insert(dto);
			}
			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[修改协议详情接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> modifyInquiryInfo(InquiryInfoDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		InquiryInfoDTO inquiryInfo=new InquiryInfoDTO();
		inquiryInfo.setInquiryNo(dto.getInquiryNo());
		inquiryInfo.setMatCd(dto.getMatCd());
		inquiryInfo.setActiveFlag("1");
		List<InquiryInfoDTO> list=inquiryInfoDAO.findAll(inquiryInfo);
		for(InquiryInfoDTO inquiryInfoDTO:list) {
			try {
				if (inquiryInfoDTO != null) {
					if (dto.getBeginDate() != null) {
						inquiryInfoDTO.setBeginDate(dto.getBeginDate());
					}
					if (dto.getInquiryName() != null) {
						inquiryInfoDTO.setInquiryName(dto.getInquiryName());
					}
					if (dto.getEndDate() != null) {
						inquiryInfoDTO.setEndDate(dto.getEndDate());
					}
					if (dto.getActiveFlag() != null) {
						inquiryInfoDTO.setActiveFlag(dto.getActiveFlag());
					}
					if (dto.getStatus() != null) {
						inquiryInfoDTO.setStatus(dto.getStatus());
					}
					if (dto.getCreateDate() != null) {
						inquiryInfoDTO.setCreateDate(dto.getCreateDate());
					}
					if (dto.getDeliveryDate() != null) {
						inquiryInfoDTO.setDeliveryDate(dto.getDeliveryDate());
					}
					if (dto.getQuantity() != null && dto.getQuantity()!=0) {
						inquiryInfoDTO.setQuantity(dto.getQuantity());
					}
					if (dto.getRemarks() != null) {
						inquiryInfoDTO.setRemarks(dto.getRemarks());
					}
					if (dto.getAnnex() != null) {
						inquiryInfoDTO.setAnnex(dto.getAnnex());
					}
					if (dto.getSupplierId() != null) {
						inquiryInfoDTO.setSupplierId(dto.getSupplierId());
					}
					if (dto.getPrinterId() != null) {
						inquiryInfoDTO.setPrinterId(dto.getPrinterId());
					}
					if (dto.getTempIds() != null) {
						inquiryInfoDTO.setTempIds(dto.getTempIds());
					}
					if (dto.getDeliveryDate2() != null) {
						inquiryInfoDTO.setDeliveryDate2(dto.getDeliveryDate2());
					}
					if (inquiryInfoDAO.update(inquiryInfoDTO) > 0) {
						er.setResult("修改成功");
					} else {
						er.setResult("修改失败");
					}
				}
			} catch (Exception e) {
				er.setResult("修改失败");
				throw new RuntimeException(e);
			}
		}
		return er;
	}
	/**
	 * <p>Discription:[修改协议详情根据ID接口实现]</p>
	 * Created on 2015年07月24日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> modifyInquiryInfoById(InquiryInfoDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		InquiryInfoDTO inquiryInfoDTO=inquiryInfoDAO.findById(dto.getId());
			try {
				if (inquiryInfoDTO != null) {
					if (dto.getBeginDate() != null) {
						inquiryInfoDTO.setBeginDate(dto.getBeginDate());
					}
					if (dto.getInquiryName() != null) {
						inquiryInfoDTO.setInquiryName(dto.getInquiryName());
					}
					if (dto.getEndDate() != null) {
						inquiryInfoDTO.setEndDate(dto.getEndDate());
					}
					if (dto.getActiveFlag() != null) {
						inquiryInfoDTO.setActiveFlag(dto.getActiveFlag());
					}
					if (dto.getStatus() != null) {
						inquiryInfoDTO.setStatus(dto.getStatus());
					}
					if (dto.getCreateDate() != null) {
						inquiryInfoDTO.setCreateDate(dto.getCreateDate());
					}
					if (dto.getDeliveryDate() != null) {
						inquiryInfoDTO.setDeliveryDate(dto.getDeliveryDate());
					}
					if (dto.getQuantity() != null && dto.getQuantity()!=0) {
						inquiryInfoDTO.setQuantity(dto.getQuantity());
					}
					if (dto.getRemarks() != null) {
						inquiryInfoDTO.setRemarks(dto.getRemarks());
					}
					if (dto.getAnnex() != null) {
						inquiryInfoDTO.setAnnex(dto.getAnnex());
					}
					if (dto.getSupplierId() != null) {
						inquiryInfoDTO.setSupplierId(dto.getSupplierId());
					}
					if (dto.getPrinterId() != null) {
						inquiryInfoDTO.setPrinterId(dto.getPrinterId());
					}
					if (dto.getTempIds() != null) {
						inquiryInfoDTO.setTempIds(dto.getTempIds());
					}
					if(dto.getSkuId() !=null){
						inquiryInfoDTO.setSkuId(dto.getSkuId());
					}
					//增加修改询价sku商品 的交货时间
					if(dto.getDeliveryDate2() !=null){
						inquiryInfoDTO.setDeliveryDate2(dto.getDeliveryDate2());
					}
					
					if (inquiryInfoDAO.update(inquiryInfoDTO) > 0) {
						er.setResult("修改成功");
					} else {
						er.setResult("修改失败");
					}
				}
			} catch (Exception e) {
				er.setResult("修改失败");
				throw new RuntimeException(e);
			}

		return er;
	}
	/**
	 * <p>Discription:[修改协议明细接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> modifyInquiryMat(InquiryMatDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		InquiryMatDTO inquiryMatDTO = inquiryMatDAO.findById(dto.getId());
		try{
			if(inquiryMatDTO != null){
				if(dto.getMatPrice()!=null){
					inquiryMatDTO.setMatPrice(dto.getMatPrice());
				}
				if(dto.getActiveFlag()!=null){
					inquiryMatDTO.setActiveFlag(dto.getActiveFlag());
				}
				if(dto.getBeginDate()!=null){
					inquiryMatDTO.setBeginDate(dto.getBeginDate());
				}
				if(dto.getEndDate()!=null){
					inquiryMatDTO.setEndDate(dto.getEndDate());
				}
				if(dto.getQuantity()!=null){
					inquiryMatDTO.setQuantity(dto.getQuantity());
				}
				if(dto.getStatus()!=null){
					inquiryMatDTO.setStatus(dto.getStatus());
				}
				if(inquiryMatDAO.update(inquiryMatDTO) >0){
					er.setResult("修改成功");
				}
				else{
					er.setResult("修改失败");
				}
			}
		}catch(Exception e){
			er.setResult("error");
			throw new RuntimeException(e);
		}

		return er;
	}
	/**
	 * <p>Discription:[修改协议明细]</p>
	 * Created on 2015年06月10日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> modifyInquiryOrder(InquiryOrderDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		InquiryOrderDTO inquiryOrderDTO=inquiryOrderDAO.findById(dto.getId());
		try{
			if(inquiryOrderDTO != null){
				if(dto.getActiveFlag()!=null){
					inquiryOrderDTO.setActiveFlag(dto.getActiveFlag());
				}
				if(inquiryOrderDAO.update(inquiryOrderDTO) >0){
					er.setResult("修改成功");
				}
				else{
					er.setResult("修改失败");
				}
			}
		}catch(Exception e){
			er.setResult("error");
			throw new RuntimeException(e);
		}

		return er;
	}
	/**
	 * <p>Discription:[生成询价编码]</p>
	 * Created on 2015年06月23日
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> createInquiryNo() {
		ExecuteResult<String> er=new ExecuteResult<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Integer b = (int) (Math.random() * 10000);
		String a = "XJ" + sdf.format(new Date()) + b.toString();
		er.setResult(a);
		return er;
	}
	
	public ExecuteResult<DataGrid<InquiryInfoDTO>> queryByMids(String detailIds){
		ExecuteResult<DataGrid<InquiryInfoDTO>> er=new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		DataGrid<InquiryInfoDTO> dg = new DataGrid<InquiryInfoDTO>();
		if(detailIds!=null&&!"".equals(detailIds)){
			String[] idStrs = detailIds.split(",");
			Long[] ids = new Long[idStrs.length];
			for(int i = 0;i<ids.length;i++){
				ids[i] = new Long(idStrs[i]);
			}
			List<InquiryInfoDTO> list = inquiryInfoDAO.queryByMids(ids);
			dg.setRows(list);
			dg.setTotal(new Long(list.size()));
			er.setResult(dg);
		}
		return er;
	}
	
	public ExecuteResult<DataGrid<InquiryInfoDTO>> queryByInquiryNos(String ids){
		ExecuteResult<DataGrid<InquiryInfoDTO>> er=new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		DataGrid<InquiryInfoDTO> dg = new DataGrid<InquiryInfoDTO>();
		if(ids!=null&&!"".equals(ids)){
			String[] idStrs = ids.split(",");
			List<InquiryInfoDTO> list = inquiryInfoDAO.queryByInquiryNos(idStrs);
			dg.setRows(list);
			dg.setTotal(new Long(list.size()));
			er.setResult(dg);
		}
		return er;
	}
}
