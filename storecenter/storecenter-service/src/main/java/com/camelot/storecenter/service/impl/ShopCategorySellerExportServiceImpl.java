package com.camelot.storecenter.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import com.camelot.storecenter.dao.ShopCategorySellerDAO;

import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
@Service("shopCategorySellerExportService")
public class ShopCategorySellerExportServiceImpl implements ShopCategorySellerExportService {
	private final static Logger logger = LoggerFactory.getLogger(ShopCategorySellerExportServiceImpl.class);
	@Resource
	private ShopCategorySellerDAO shopCategorySellerDAO;
	
	/**
	 * <p>Discription:[店铺分类修改]</p>
	 * Created on 2015年3月5日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> update(ShopCategorySellerDTO dto){
	  ExecuteResult<String> result=new ExecuteResult<String>();
	  try {
		ShopCategorySellerDTO scd=shopCategorySellerDAO.selectById(dto.getCid());
			  if(scd==null){
				  result.setResultMessage("该类目不存在！");
			  }else {
				      if(dto.getParentCid()==0L){scd.setParentCid(dto.getParentCid());}
					  if(dto.getSellerId()!=0L){scd.setSellerId(dto.getSellerId());}
					  if(dto.getShopId()!=0L){scd.setShopId(dto.getShopId());}
					  if(dto.getCname()!=null && dto.getCname()!=""){ scd.setCname(dto.getCname());}
					  if(dto.getStatus()!=null && dto.getStatus()!=0){ scd.setStatus(dto.getStatus());}
					  if(dto.getLev()!=null && dto.getLev()!=0){scd.setLev(dto.getLev());}
					  if(dto.getHasLeaf()!=null){scd.setHasLeaf(dto.getHasLeaf());}
					  if(dto.getSortNumber()!=null){scd.setSortNumber(dto.getSortNumber());}
					  if(dto.getHomeShow()!=null && dto.getHomeShow()!=0){scd.setHomeShow(dto.getHomeShow());}
					  if(dto.getExpand()!=null && dto.getExpand()!=0){scd.setExpand(dto.getExpand());}
					  if(dto.getCreated()!=null ){scd.setCreated(dto.getCreated());}
					  if(shopCategorySellerDAO.update(scd)>0){ result.setResultMessage("修改成功！"); } 
			  }
			  result.setResultMessage("success");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	  return result;
	}
	
	/**
	 * <p>Discription:[店铺分类列表查询]</p>
	 * Created on 2015年3月5日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[杨芳]
	 */

	@Override
	public ExecuteResult<DataGrid<ShopCategorySellerDTO>> queryShopCategoryList(ShopCategorySellerDTO dto, Pager pager) {
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> result=new ExecuteResult<DataGrid<ShopCategorySellerDTO>>();
		try{
			DataGrid<ShopCategorySellerDTO> dg=new DataGrid<ShopCategorySellerDTO>();
			dg.setRows(shopCategorySellerDAO.selectListByCondition(dto, pager));
			dg.setTotal(shopCategorySellerDAO.selectCountByCondition(dto));
			result.setResult(dg);
			result.setResultMessage("success");
		}catch(Exception e){
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * <p>Discription:[店铺分类单个删除]</p>
	 * Created on 2015年3月6日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
	
	@Override
	public ExecuteResult<String> deleteById(long id) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		
		try {
			Integer count = shopCategorySellerDAO.delete(id);
			result.setResultMessage("success");
			result.setResult(count.toString());
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * <p>Discription:[店铺分类添加]</p>
	 * Created on 2015年3月6日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	
	@Override
	public ExecuteResult<Long> addShopCategory(ShopCategorySellerDTO dto) {
		ExecuteResult<Long> result=new ExecuteResult<Long>();
		try{
			dto.setStatus(1);
			dto.setCreated(new Date());
			shopCategorySellerDAO.insertShopCategory(dto);
			result.setResult(dto.getCid());
			result.setResultMessage("success");
		}catch(Exception e){
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	  /**
		 * <p>Discription:[店铺分类全部删除]</p>
		 * Created on 2015年3月5日
		 * @param dto
		 * @param pager
		 * @return
		 * @author:[杨芳]
		 */
	public ExecuteResult<String> deletes(Long[] ids){
	  ExecuteResult<String> result=new ExecuteResult<String>();
	    try {
			int count = shopCategorySellerDAO.deletes(ids);
			result.setResultMessage("success");
			result.setResult(count+"");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	    return result;
	}
}
