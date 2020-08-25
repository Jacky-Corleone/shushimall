package com.camelot.basecenter.service;


import java.util.List;

import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.dto.MallTypeDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 供客户端调用的远程接口
 * 帮助文档列表接口
 * @author 周立明
 * 
 */
public interface MallDocumentService {
	/**
	 * <p>分页查询帮助文档列表信息 </p>
	 * Created on 2015年1月27日
	 * @param mallDocumentDTO，pager
	 * @return DataGrid
	 * @author:[周立明]
	 */
	public DataGrid<MallDocumentDTO> queryMallDocumentList(MallDocumentDTO mallDocumentDTO,Pager pager);
	/**
	 * <p>根据id查询帮助文档信息</p>
	 * Created on 2015年1月27日
	 * @param id
	 * @return MallDocumentDTO
	 * @author:[周立明]
	 */
	public MallDocumentDTO getMallDocumentById(Long id);
	/**
	 * <p>帮助文档添加功能</p>
	 * Created on 2015年1月27日
	 * @param id
	 * @return MallDocumentDTO
	 * @author:[周立明]
	 */
	public ExecuteResult<String> addMallDocument(MallDocumentDTO mallDocumentDTO);
	/**
	 * <p>通过id修改帮助文档内容</p>
	 * Created on 2015年1月27日
	 * @param mallDocumentDTO
	 * @return ExecuteResult
	 * @author:[周立明]
	 */
	public ExecuteResult<String> modifyInfoById(MallDocumentDTO mallDocumentDTO);
	/**
	 * <p>通过id修改帮助文档状态</p>
	 * Created on 2015年1月27日
	 * @param mallDocumentDTO
	 * @return ExecuteResult
	 * @author:[周立明]
	 */
	public ExecuteResult<String> modifyStatusById(int id,int status);
	/**
	 * <p>根据类型获取分类</p>
	 * Created on 2015年1月29日
	 * @param type
	 * @return
	 * @author:[周乐]
	 */
	public List<MallTypeDTO> queryMallDocumentListByType(String type);
}
