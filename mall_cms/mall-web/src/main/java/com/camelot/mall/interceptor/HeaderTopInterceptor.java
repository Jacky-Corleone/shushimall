package com.camelot.mall.interceptor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.service.MallDocumentService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
/**
 * 公共top页拦截ecm后台向前台展示的数据
 * @author wangp-c
 *
 */
public class HeaderTopInterceptor implements HandlerInterceptor{
	@Autowired
	private MallDocumentService mallDocumentService;

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		List arr=new ArrayList();
		MallDocumentDTO mallDocumentDTO=new MallDocumentDTO();
		mallDocumentDTO.setMallStatus(2);
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(100);
		pager.setSort("mallCreated");
		pager.setOrder("desc");
		DataGrid<MallDocumentDTO> dg = mallDocumentService.queryMallDocumentList(mallDocumentDTO, pager);
		List<MallDocumentDTO> lists=dg.getRows();
		//ecm后台向前台客服中心展示服务规则下的客服中心的所有主题内容
		if(null!=lists&&lists.size()>0){
			for(MallDocumentDTO dto:lists){
			    if("客服中心".equals(dto.getMallClassifyTitle())&& 4==dto.getMallType().intValue()){
			    	arr.add(dto);
			    	request.setAttribute("dto",dto);
			    }
			 }
		}
		request.setAttribute("mallDocumentDTOs",arr);
		return true;
	}

	
	
}
