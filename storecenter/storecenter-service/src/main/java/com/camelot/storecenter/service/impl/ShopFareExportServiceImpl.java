package com.camelot.storecenter.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopFareDAO;
import com.camelot.storecenter.dto.ShopFareDTO;
import com.camelot.storecenter.service.ShopFareExportService;

/** 
 * <p>Description: [店铺费用的实现类]</p>
 * Created on 2015年3月17日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("shopFareExportService")
public class ShopFareExportServiceImpl implements ShopFareExportService {
	 private final static Logger logger = LoggerFactory.getLogger(ShopFareExportServiceImpl.class);
	@Resource
	private ShopFareDAO shopFareDAO;
	/**
	 * <p>Discription:[根据运送地址查询店铺运费信息]</p>
	 * Created on 2015年3月25日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<ShopFareDTO> queryShopFareByFareRegion(ShopFareDTO dto){
		ExecuteResult<ShopFareDTO> er=new ExecuteResult<ShopFareDTO>();	
		try{
			ShopFareDTO sf=shopFareDAO.selectByFareRegion(dto);
			if(sf != null && !sf.equals("")){
				er.setResult(sf);
			}else{
				ShopFareDTO  sh=shopFareDAO.selectByFareRegions(dto);
				if(sh != null && !sh.equals("")){
					er.setResult(sh);
				}else{
					er.setResultMessage("不存在该地址");
				}
			}
			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[查询输入的地址是否已存在，若存在则返回已存在的地址]</p>
	 * Created on 2015年3月23日
	 * @param sellerId   卖家id
	 * @param shopId   店铺id
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> queryBysellerIdAndShopId(ShopFareDTO dto){
	  ExecuteResult<String> er=new ExecuteResult<String>();
	  String result="";
	  if(dto.getSellerId()!=null && dto.getShopId()!=null){
		  if(dto.getFareRegion() !=null && dto.getFareRegion()!=""){
			  String[] str=dto.getFareRegion().split(",");
			  //根据卖家id和店铺id查询出所有的运送范围
			  String fareRegion=shopFareDAO.queryBysellerIdAndShopId(dto);
			 if(fareRegion!=null){
				 for(String i:str){
					  if((","+fareRegion+",").contains(","+i+",")){
						  result+=i+",";
						  er.setResultMessage("该地址已存在");
					  }
				  }
				  if(result.length()>0){
					  er.setResult(result.substring(0, result.length()-1)); 	
					} 
			 }else{
				 er.setResultMessage("该卖家和店铺不存在");
			 }
		  }else{
			  er.setResultMessage("运送地址不能为空");
		  }
	  }else{
		  er.setResultMessage("卖家id、店铺id不能为空");
	  }
	  return er;
	}
	/**
	 * <p>Discription:[根据id复制模板]</p>
	 * Created on 2015年3月18日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<String> copyShopFare(Long id) {
		ExecuteResult<String> er=new ExecuteResult<String>();
		try{
		  ShopFareDTO shopFareDto=shopFareDAO.selectById(id);
		  shopFareDAO.insert(shopFareDto);
		  er.setResult("复制成功");
		}catch(Exception e){
		  logger.error(e.getMessage());
		  er.setResult("复制失败");
		  throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据条件查询店铺费用列表]</p>
	 * Created on 2015年3月17日
	 * @param dto
	 * @param page
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public DataGrid<ShopFareDTO> queryShopFareList(ShopFareDTO dto, Pager<ShopFareDTO> page) {
		DataGrid<ShopFareDTO> dg=new DataGrid<ShopFareDTO>();
		 try{

			 dg.setRows(shopFareDAO.selectListByCondition(dto, page));
			   dg.setTotal(shopFareDAO.selectCountByCondition(dto));   
		   }catch(Exception e){
			   logger.error(e.getMessage());
			   throw new RuntimeException(e);
		   }
		return dg;
	}
	/**
	 * <p>Discription:[根据id修改店铺费用的信息]</p>
	 * Created on 2015年3月17日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<String> modifyShopFareById(ShopFareDTO dto) {
	  ExecuteResult<String> er=new ExecuteResult<String>();
	  try{
		  ShopFareDTO sf= shopFareDAO.selectById(dto.getId());
		  if(sf != null){
			if(dto.getFareName() != null && dto.getFareName() !=""){
				sf.setFareName(dto.getFareName());
			}
			if(dto.getFareType() != null && dto.getFareType() !=""){
				sf.setFareType(dto.getFareType());
			}
			if(dto.getPaymentType() !=0){
				sf.setPaymentType(dto.getPaymentType());
			}
			if(dto.getFareRegion() != null && dto.getFareRegion() != ""){
				sf.setFareRegion(dto.getFareRegion());
			}
			if(dto.getFirstWeightPrice() != null){
				sf.setFirstWeightPrice(dto.getFirstWeightPrice());
			}
			if(dto.getContinueWeightPrice() != null){
			   sf.setContinueWeightPrice(dto.getContinueWeightPrice());
			}
			if(shopFareDAO.update(sf)>0){
				er.setResult("修改成功！");
			}
		  }
	  }catch(Exception e){
		 logger.error(e.getMessage()); 
		 er.setResult("修改失败！");
		 throw new RuntimeException(e);
	  }
	 return er;
	}
	
	/**
	 * <p>Discription:[根据id删除店铺费用]</p>
	 * Created on 2015年3月17日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<String> deleteShopFareById(Long id) {
	  ExecuteResult<String> er=new ExecuteResult<String>();
	  try{
		 if(shopFareDAO.delete(id)>0){
			 er.setResult("删除成功！");
		 }
	  }catch(Exception e){
		 logger.error(e.getMessage());
		 er.setResult("删除失败");
		 throw new RuntimeException(e);
	  }
	  return er;
	}
	/**
	 * <p>Discription:[添加店铺费用]</p>
	 * Created on 2015年3月17日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<String> addShopFare(ShopFareDTO dto) {
	 ExecuteResult<String> er=new  ExecuteResult<String>();
	  try{
		  shopFareDAO.insert(dto);
		  er.setResult("添加成功");
	  }catch(Exception e){
		  logger.error(e.getMessage());
		  er.setResult("添加失败");
		  throw new RuntimeException(e);
	  }
		return er;
	}
	

}
