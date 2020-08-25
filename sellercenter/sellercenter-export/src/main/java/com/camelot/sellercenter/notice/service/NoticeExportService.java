package com.camelot.sellercenter.notice.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;

/**
 * 
 * <p>Description: [公告服务的处理接口]</p>
 * Created on 2015年1月22日
 * @author  <a href="mailto: xxx@camelotchina.com">胡恒心</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface NoticeExportService {
	
	/**
	 * 
	 * <p>Discription:[公告列表查询  dto 传入查询 参数 noticeStatus 1生效 2是无效]</p>
	 * Created on 2015年1月22日
	 * @param page
	 * @return
	 * @author:[胡恒心]
	 */

	public DataGrid<MallNoticeDTO> queryNoticeList(Pager page,MallNoticeDTO dto);

	
	/**
	 * 
	 * <p>Discription:[公告详情查询]</p>
	 * Created on 2015年1月22日
	 * @param id
	 * @return
	 * @author:[胡恒心]
	 */
	public MallNoticeDTO getNoticeInfo(Long id);
	
	/**
	 * 
	 * <p>Discription:[公告置顶/取消置顶]</p>
	 * Created on 2015年1月22日
	 * @param id
	 * @param isRecommend
	 * @return
	 * @author:[胡恒心]
	 */
	public ExecuteResult<String> modifyNoticeRecommend(Long id, Integer isRecommend);
	
	/**
	 * 
	 * <p>Discription:[公告上下架]</p>
	 * Created on 2015年1月22日
	 * @param id
	 * @param status
	 * @return
	 * @author:[胡恒心]
	 */
	public ExecuteResult<String> modifyNoticeStatus(Long id, Integer status);
	
	/**
	 * 
	 * <p>Discription:[公告添加
	 * 	公告类型 ：  1 文字公告  公告内容不能为空
	 * 2 链接公告 公告url不能为空
	 * ]</p>
	 * Created on 2015年1月22日
	 * @param mallNoticeDTO
	 * @return
	 * @author:[胡恒心]
	 */
	public ExecuteResult<String> addNotice(MallNoticeDTO mallNoticeDTO);
	
	/**
	 * <p>Discription:[公告修改]</p>
	 * Created on 2015年1月22日
	 * @param mallNoticeDTO
	 * @return
	 * @author:[胡恒心]
	 */
	public ExecuteResult<String> modifyNoticeInfo(MallNoticeDTO mallNoticeDTO);
	/**
	 * 
	 * <p>Discription:[公告上下移动   上移动传入1 下移传入-1 ]</p>
	 * Created on 2015-4-2
	 * @param mallNoticeDTO
	 * @param modifyNum
	 * @return
	 * @author:[liuqingshan]
	 */
	 public ExecuteResult<MallNoticeDTO> updateNoticSortNum(MallNoticeDTO mallNoticeDTO,Integer modifyNum);
	 /**
	  * 
	  * <p>Discription:[删除公告根据ID]</p>
	  * Created on 2015-4-2
	  * @param id
	  * @return
	  * @author:[liuqingshan]
	  */
	 public ExecuteResult<String> deleteNoticeById(Long id);
}
