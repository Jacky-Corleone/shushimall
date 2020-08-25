package com.camelot.storecenter.service.impl;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.QqCustomerDAO;
import com.camelot.storecenter.dto.QqCustomerDTO;
import com.camelot.storecenter.service.QqCustomerService;

/**
 * 
 * <p>Description: [客服接口实现类]</p>
 * Created on 2016年2月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
@Service("qqCustomerService")
public class QqCustomerServiceImpl implements QqCustomerService{
	private final static Logger logger = LoggerFactory.getLogger(ShopBrandExportServiceImpl.class);

	@Resource
	private QqCustomerDAO qqCustomerDAO;
	/**
	 * 
	 * <p>Discription:[查询客服列表]</p>
	 * Created on 2016年2月2日
	 * @param pager 分页
	 * @param qqCustomerDTO 过滤条件
	 * @return 
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<DataGrid<QqCustomerDTO>> selectListByConditionAll(Pager<QqCustomerDTO> pager, QqCustomerDTO qqCustomerDTO) {
        ExecuteResult<DataGrid<QqCustomerDTO>> result=new ExecuteResult<DataGrid<QqCustomerDTO>>();
		try {
			DataGrid<QqCustomerDTO> dataGrid=new DataGrid<QqCustomerDTO>();
			List<QqCustomerDTO> list = qqCustomerDAO.selectListByCondition(qqCustomerDTO, pager);
			Long counbt = qqCustomerDAO.selectCountByCondition(qqCustomerDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(counbt);
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
	 * 
	 * <p>Discription:[更新客服信息，包含逻辑删除方法]</p>
	 * Created on 2016年2月2日
	 * @param qqCustomerDTO 更新的内容
	 * @param id 主键 
	 * @return 影响的条数
	 * @author:[李伟龙]
	 */
	@Override
	public int updateQqCustomer(QqCustomerDTO qqCustomerDTO) {
		return qqCustomerDAO.update(qqCustomerDTO);
	}
	
	/**
	 * 
	 * <p>Discription:[新增客服]</p>
	 * Created on 2016年2月2日
	 * @param qqCustomerDTO 新增的内容
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<String> addQqCustomer(QqCustomerDTO qqCustomerDTO){
		ExecuteResult<String>  result=new ExecuteResult<String> ();
		try {
			qqCustomerDAO.insert(qqCustomerDTO);
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
	 * 
	 * <p>Discription:[根据idlist与客服类型获取QQ号码]</p>
	 * Created on 2016年2月3日
	 * @param idlst
	 * @param customerType '客服类型：0平台客服，1店铺客服'
	 * @return	QQ号码
	 * @author:[李伟龙]
	 */
	@Override
	public String getQqCustomerByIds(List<Long> idlst, Integer customerType) {
		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
		qqCustomerDTO.setUserIds(idlst);
		qqCustomerDTO.setCustomerType(customerType);
		//查询在用的QQ客服
		qqCustomerDTO.setDeletedFlag(0);
		Pager pager = new Pager();
		pager.setRows(Integer.MAX_VALUE);
		List<QqCustomerDTO> list = qqCustomerDAO.selectListByCondition(qqCustomerDTO, pager);
		String qq = "";
		if(null != list && list.size() > 0){
			if(list.size() == 1){
				return list.get(0).getCustomerQqNumber();
			}else{
				for (int i = 0; i < list.size(); i++) {
					//判断是否为默认客服
					if(1 == list.get(i).getIsDefault()){
						qq = list.get(i).getCustomerQqNumber();
						break;
					}
				}
				//如果没有默认则随机给出一个客服
				if("".equals(qq)){
					Random r = new Random();
					int r_int = r.nextInt(list.size());
					qq = list.get(r_int).getCustomerQqNumber();
				}
			}
		}
		return qq;
	}
	
	/**
	 * 
	 * <p>Discription:[根据条件修改所有客服为非默认客服]</p>
	 * Created on 2016年2月17日
	 * @param qqCustomerDTO '客服'
	 * @author:[王宏伟]
	 */
	public void updateMRCustomer(QqCustomerDTO qqCustomerDTO){
		qqCustomerDAO.updateMRCustomer(qqCustomerDTO);
	}
	
}
