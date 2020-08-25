package com.camelot.basecenter.malldocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.MallDocumentDAO;
import com.camelot.basecenter.domain.MallDocument;
import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.dto.MallTypeDTO;
import com.camelot.basecenter.service.MallDocumentService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("mallDocumentService")
public class MallDocumentServiceImpl implements MallDocumentService{
	private final static Logger logger = LoggerFactory.getLogger(MallDocumentServiceImpl.class);
	@Resource
	private MallDocumentDAO mallDocumentDAO;
	/**
	 * <p>Discription:[帮助文档列表查询]</p>
	 * @param mallDocumentDTO,pager
	 * @return DataGrid
	 * Created on 2015-1-25
	 * @author:周立明
	 */
	@Override
	public DataGrid<MallDocumentDTO> queryMallDocumentList(MallDocumentDTO mallDocumentDTO,Pager pager){
		List<MallDocumentDTO> documents = mallDocumentDAO.queryMallDocumentList(mallDocumentDTO, pager);
		long counts = mallDocumentDAO.queryMallDocumentCount(mallDocumentDTO);
		DataGrid<MallDocumentDTO> datagrid = new DataGrid<MallDocumentDTO>();
		datagrid.setRows(documents);
		datagrid.setTotal(counts);
		return datagrid;
	}
	/**
	 * 
	 * <p>Discription:[帮助文档详情查询]</p>
	 * @param id
	 * @return MallDocumentDTO
	 * Created on 2015-1-25
	 * @author:周立明
	 */
	@Override
	public MallDocumentDTO getMallDocumentById(Long id){
		logger.info("getMallDocumentById---id---:{}", id);
		MallDocumentDTO mallDocumentDTO = mallDocumentDAO.getMallDocumentById(id);
		return mallDocumentDTO;
	}
	/**
	 * 
	 * <p>Discription:[帮助文档添加]</p>
	 * @param mallDocumentDTO
	 * @return ExecuteResult
	 * Created on 2015-1-27
	 * @author:周立明
	 */
	@Override
	public ExecuteResult<String> addMallDocument(MallDocumentDTO mallDocumentDTO){
		logger.info("addMallDocument---mallDocumentDTO---:{}", mallDocumentDTO);
		ExecuteResult<String> result = new ExecuteResult<String>();
		MallDocument mallDocument = new MallDocument();
		try {
			mallDocument.setClassifyId(mallDocumentDTO.getMallClassifyId());
			mallDocument.setContentUrl(mallDocumentDTO.getMallContentUrl());
			mallDocument.setEndTime(mallDocumentDTO.getMallEndTime());
			mallDocument.setPlatformId(mallDocumentDTO.getMallPlatformId());
			mallDocument.setSortNum(mallDocumentDTO.getMallSortNum());
			mallDocument.setStartTime(mallDocumentDTO.getMallStartTime());
			mallDocument.setStatus(mallDocumentDTO.getMallStatus());
			mallDocument.setTitle(mallDocumentDTO.getMallTitle());
			mallDocument.setType(mallDocumentDTO.getMallType());
			mallDocument.setCreated(new Date());
			mallDocument.setModified(new Date());	
			//默认为未删除
			mallDocument.setIsDeleted("0");
			mallDocumentDAO.add(mallDocument);
			result.setResultMessage("操作成功");
		} catch (Exception e) {
			result.addErrorMsg(e.getMessage());
			result.setResultMessage("操作失败");
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * 
	 * <p>Discription:[帮助文档修改]</p>
	 * @param mallDocumentDTO
	 * @return ExecuteResult
	 * Created on 2015-1-27
	 * @author:周立明
	 */
	@Override
	public ExecuteResult<String> modifyInfoById(MallDocumentDTO mallDocumentDTO) {
		logger.info("modifyInfoById---mallDocumentDTO---:{}", mallDocumentDTO);
		ExecuteResult<String> result = new ExecuteResult<String>();
		MallDocument mallDocument = new MallDocument();
		try {
//			mallDocument = EntityTranslator.transToDomain(mallDocumentDTO, MallDocument.class);
			mallDocument.setId(mallDocumentDTO.getMallId());
			mallDocument.setClassifyId(mallDocumentDTO.getMallClassifyId());
			mallDocument.setContentUrl(mallDocumentDTO.getMallContentUrl());
			mallDocument.setEndTime(mallDocumentDTO.getMallEndTime());
			mallDocument.setPlatformId(mallDocumentDTO.getMallPlatformId());
			mallDocument.setSortNum(mallDocumentDTO.getMallSortNum());
			mallDocument.setStartTime(mallDocumentDTO.getMallStartTime());
			mallDocument.setStatus(mallDocumentDTO.getMallStatus());
			mallDocument.setTitle(mallDocumentDTO.getMallTitle());
			mallDocument.setType(mallDocumentDTO.getMallType());
			mallDocument.setModified(new Date());	
			mallDocument.setIsDeleted(mallDocumentDTO.getMallIsDeleted()!=null?mallDocumentDTO.getMallIsDeleted().toString():null);
			mallDocumentDAO.update(mallDocument);
			result.setResultMessage("操作成功！");
		} catch (Exception e) {
			result.addErrorMsg("操作失败！");
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * 
	 * <p>Discription:[帮助文档上下架]</p>
	 * @param id,status
	 * @return ExecuteResult
	 * Created on 2015-1-27
	 * @author:周立明
	 */
	@Override
	public ExecuteResult<String> modifyStatusById(int id, int status) {
		logger.info("modifyInfoById---id-status--:{}{}", id,status);
		ExecuteResult<String> result = new ExecuteResult<String>();
//		MallDocumentDTO mallDocumentDTO = new MallDocumentDTO();
//		mallDocumentDTO.setMallId(id);
//		mallDocumentDTO.setMallStatus(status);
		MallDocument mallDocument = new MallDocument();
		try {
			mallDocument.setId(id);
			mallDocument.setStatus(status);
			mallDocument.setModified(new Date());
			mallDocument.setStartTime(status==1?null:new Date());
//			mallDocument = EntityTranslator.transToDomain(mallDocumentDTO, MallDocument.class);
			mallDocumentDAO.update(mallDocument);
			result.setResultMessage("操作成功");
		} catch (Exception e) {
			result.addErrorMsg(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[根据类型查询分类列表]</p>
	 * Created on 2015年1月29日
	 * @param type
	 * @return
	 * @author:[周乐]
	 */
	
	@Override
	public List<MallTypeDTO> queryMallDocumentListByType(String type) {
		List<MallTypeDTO> listRecord = new ArrayList<MallTypeDTO>();
		if(StringUtils.isEmpty(type)){
			return listRecord;
		}
		return mallDocumentDAO.queryMallDocumentListByType(Integer.parseInt(type));
	}
}
