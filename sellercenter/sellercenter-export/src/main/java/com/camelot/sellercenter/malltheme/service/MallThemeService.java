package com.camelot.sellercenter.malltheme.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;


public interface MallThemeService {

	/**
	 * 
	* @Title: queryMallThemeList 
	* @Description: 分页查询，page为null时查询全部
	* @param mallThemeDTO
	* @param publishFlag
	* @param page
	* @return    设定文件 
	* @return DataGrid<MallThemeDTO>    返回类型 
	* @throws 
	* @author 刘振江
	 */
	public DataGrid<MallThemeDTO> queryMallThemeList(MallThemeDTO mallThemeDTO,String publishFlag, Pager page);
	
	/**
	 * 
	* @Title: getMallThemeById 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param id
	* @return    设定文件 
	* @return MallThemeDTO    返回类型 
	* @throws 
	* @author 刘振江
	 */
	public MallThemeDTO getMallThemeById(long id);
	/**
	 * 
	* @Title: addMallTheme 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param mallThemeDTO
	* @return    设定文件 
	* @return ExecuteResult<String>    返回类型 
	* @throws 
	* @author 刘振江
	 */
	public ExecuteResult<String> addMallTheme(MallThemeDTO mallThemeDTO);
	
	/**
	 * 
	* @Title: delete 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param id
	* @return    设定文件 
	* @return ExecuteResult<String>    返回类型 
	* @throws 
	* @author 刘振江
	 */
	public ExecuteResult<String> delete(Long id);
	/**
	 * 
	* @Title: motifyMallThemeStatus 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param id
	* @param publishFlag
	* @return    设定文件 
	* @return ExecuteResult<String>    返回类型 
	* @throws 
	* @author 刘振江
	 */
	public ExecuteResult<String> motifyMallThemeStatus(Long id, String publishFlag);
	
	
	/**
	 * 
	* @Title: motifyMallTheme 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param mallThemeDTO
	* @return    设定文件 
	* @return ExecuteResult<String>    返回类型 
	* @throws 
	* @author 刘振江
	 */
	public ExecuteResult<String> motifyMallTheme(MallThemeDTO mallThemeDTO);
	
	/**
	 * 
	 * <p>Discription:[获取所有地域子站的地区编码]</p>
	 * Created on 2016年3月18日
	 * @return
	 * @author:[王宏伟]
	 */
	public String[] queryGroupCityCode();
}
