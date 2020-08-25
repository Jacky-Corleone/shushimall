package com.camelot.basecenter;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.BaseConsultingSmsDAO;
import com.camelot.basecenter.dto.BaseConsultingSmsDTO;
import com.camelot.basecenter.service.BaseConsultingSmsService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [询价的实现]</p>
 * Created on 2015年3月20日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("baseConsultingSmsService")
public class BaseConsultingSmsServiceImpl implements BaseConsultingSmsService {
    
	@Resource
	private BaseConsultingSmsDAO baseConsultingSmsDAO;
	
	 /**
	   * <p>Discription:[根据id删除咨询记录（将有效标记status改为失效2）]</p>
	   * Created on 2015年3月20日
	   * @param id
	   * @return
	   * @author:[创建者中文名字]
	   */
	  public ExecuteResult<String> deleteById(Long id){
		  ExecuteResult<String> er=new ExecuteResult<String>(); 
		  baseConsultingSmsDAO.delete(id);
		  er.setResult("success");
		  return er;
	  }
	/**
	 * <p>Discription:[增加咨询]</p>
	 * Created on 2015年3月20日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<String> addBaseConsultingSms(BaseConsultingSmsDTO dto){
	  ExecuteResult<String> er=new ExecuteResult<String>();
	 try{
		 if(dto != null){
			  baseConsultingSmsDAO.add(dto); 
			  er.setResult("添加成功");
		  }else{
			  er.setResult("不能插入空数据");
		  }
	 }catch(Exception e){
		 er.setResult(e.getMessage());
		 throw new RuntimeException(e);
	 }
	  return er;
  }
	/**
	 * <p>Discription:[根据条件查询询价列表]</p>
	 * Created on 2015年3月20日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */

	@Override
	public DataGrid<BaseConsultingSmsDTO> queryList(BaseConsultingSmsDTO dto,Pager<BaseConsultingSmsDTO> page) {
		DataGrid<BaseConsultingSmsDTO> dg=new DataGrid<BaseConsultingSmsDTO>();
		try{
			dg.setRows(baseConsultingSmsDAO.queryList(dto, page));
			dg.setTotal(baseConsultingSmsDAO.queryCount(dto));
		}catch(Exception e){
		  e.getMessage();
		  throw new RuntimeException(e);
		}
		return dg;
	}
	  /**
	   * <p>Discription:[根据id修改咨询]</p>
	   * Created on 2015年3月20日
	   * @param id
	   * @return
	   * @author:[杨芳]
	   */
	  public ExecuteResult<String> modifyById(BaseConsultingSmsDTO dto){
		  ExecuteResult<String> er=new ExecuteResult<String>(); 
		  BaseConsultingSmsDTO base=baseConsultingSmsDAO.queryById(dto.getId());
		  if(base != null){
			  if(dto.getReply() != null && dto.getReply() != ""){
				  base.setReply(dto.getReply());
			  }
			  baseConsultingSmsDAO.updateBySelect(base);  
			  er.setResult("修改成功");
		  }else{
			  er.setResult("该条记录不存在");
		  }
		  return er;
	  }
}
