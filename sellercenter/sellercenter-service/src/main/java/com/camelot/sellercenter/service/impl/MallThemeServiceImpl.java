package com.camelot.sellercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.dao.MallThemeDAO;
import com.camelot.sellercenter.domain.MallTheme;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;

@Service("mallThemeService")
public class MallThemeServiceImpl implements MallThemeService {

	@Resource
	private MallThemeDAO mallThemeDao;
	private final static Logger LOGGER = LoggerFactory.getLogger(MallThemeServiceImpl.class);
	
	@Override
	
	public DataGrid<MallThemeDTO> queryMallThemeList(MallThemeDTO mallThemeDTO,String publishFlag,
			Pager page) {
		DataGrid<MallThemeDTO> dg = new DataGrid<MallThemeDTO>();
		//try{
			List<MallThemeDTO> list = mallThemeDao.queryListDTO(mallThemeDTO, publishFlag, page);
			dg.setRows(list);
			dg.setTotal(mallThemeDao.queryListCountDTO(mallThemeDTO, publishFlag));
		//}catch(Exception e){
		//	LOGGER.debug("---------queryMallThemeList----------", e);;
		//}
		return dg;
	}

	@Override
	public MallThemeDTO getMallThemeById(long id) {
		MallThemeDTO dto = new MallThemeDTO();
		//try{
		dto = mallThemeDao.queryById(id);
		//}catch(Exception e){
		//	LOGGER.debug("-------MallThemeServiceImpl getMallThemeById 报错了--------",e);
		//}
		return dto;
	}

	@Override
	public ExecuteResult<String> addMallTheme(MallThemeDTO mallThemeDTO) {
		
		ExecuteResult er = new ExecuteResult();
		//try{
		MallTheme mt =new MallTheme();
		if(mallThemeDTO.getAddressId()!=null || ("").equals(mallThemeDTO.getAddressId())){
			mt.setAddressId(mallThemeDTO.getAddressId());
		}
		if(mallThemeDTO.getProvinceCode()!=null){
			mt.setProvinceCode(mallThemeDTO.getProvinceCode());
		}
		if(mallThemeDTO.getCityCode()!=null){
			mt.setCityCode(mallThemeDTO.getCityCode());
		}
		if(mallThemeDTO.getVillageCode()!=null){
			mt.setVillageCode(mallThemeDTO.getVillageCode());
		}
		if(mallThemeDTO.getcId()!=null || ("").equals(mallThemeDTO.getcId())){
			mt.setcId(mallThemeDTO.getcId());
		}
		if(mallThemeDTO.getClev()!=null || ("").equals(mallThemeDTO.getClev())){
			mt.setClev(mallThemeDTO.getClev());
		}
		if(mallThemeDTO.getColor()!=null || ("").equals(mallThemeDTO.getColor())){
			mt.setColor(mallThemeDTO.getColor());
		}
		if(mallThemeDTO.getColorb()!=null || ("").equals(mallThemeDTO.getColorb())){
			mt.setColorb(mallThemeDTO.getColorb());
		}
		if(mallThemeDTO.getSortNum()!=null || ("").equals(mallThemeDTO.getSortNum())){
			mt.setSortNum(mallThemeDTO.getSortNum());
		}
		if(mallThemeDTO.getPrimaryCid() != null){
			mt.setPrimaryCid(mallThemeDTO.getPrimaryCid());
		}
		if(mallThemeDTO.getcId() != null){
			mt.setcId(mallThemeDTO.getcId());
		}
		mt.setStatus(mallThemeDTO.getStatus());
		mt.setThemeName(mallThemeDTO.getThemeName());
		mt.setType(mallThemeDTO.getType());
		mt.setUserId(mallThemeDTO.getUserId());
		//BeanUtils.copyProperties(mallThemeDTO, mt);
			mallThemeDao.add(mt);
		//}catch(Exception e){
		//	System.out.println("-----------addMallTheme123321--------------");
		//	LOGGER.debug("--------addMallTheme-----------",e);
		//	er.addErrorMessage("添加失败！");
		//}
		return er;
	}

	@Override
	public ExecuteResult<String> delete(Long id) {
		ExecuteResult er = new ExecuteResult();
		//try{
			mallThemeDao.delete(id);
		//}catch(Exception e){
		//	LOGGER.debug("-----------delete debug--------------",e);
		//	er.addErrorMessage("删除失败");
		//}
		return er;
	}

	@Override
	public ExecuteResult<String> motifyMallThemeStatus(Long id,
			String publishFlag) {
		ExecuteResult er = new ExecuteResult();
		//try{
			int idd = mallThemeDao.updateStatusById(id, publishFlag);
			er.setResult(idd);
		//}catch(Exception e){
		//	LOGGER.debug("--------------motifyMallThemeStatus----------------",e);
		//	er.addErrorMessage("更新失败！");
		//}
		return er;
	}

	@Override
	public ExecuteResult<String> motifyMallTheme(MallThemeDTO mallThemeDTO) {
		ExecuteResult er = new ExecuteResult();
		//try{
		MallTheme mt = new MallTheme();
		if(mallThemeDTO.getAddressId()!=null || ("").equals(mallThemeDTO.getAddressId())){
			mt.setAddressId(mallThemeDTO.getAddressId());
		}
		if(mallThemeDTO.getProvinceCode()!=null ){
			mt.setProvinceCode(mallThemeDTO.getProvinceCode());
		}
		if(mallThemeDTO.getCityCode()!=null ){
			mt.setCityCode(mallThemeDTO.getCityCode());
		}
		if(mallThemeDTO.getVillageCode()!=null ){
			mt.setVillageCode(mallThemeDTO.getVillageCode());
		}
		if(mallThemeDTO.getcId()!=null || ("").equals(mallThemeDTO.getcId())){
			mt.setcId(mallThemeDTO.getcId());
		}
		if(mallThemeDTO.getClev()!=null || ("").equals(mallThemeDTO.getClev())){
			mt.setClev(mallThemeDTO.getClev());
		}
		if(mallThemeDTO.getColor()!=null || ("").equals(mallThemeDTO.getColor())){
			mt.setColor(mallThemeDTO.getColor());
		}
		if(mallThemeDTO.getColorb()!=null || ("").equals(mallThemeDTO.getColorb())){
			mt.setColorb(mallThemeDTO.getColorb());
		}
		if(mallThemeDTO.getSortNum()!=null || ("").equals(mallThemeDTO.getSortNum())){
			mt.setSortNum(mallThemeDTO.getSortNum());
		}
		if(mallThemeDTO.getCreated() !=null || ("").equals(mallThemeDTO.getCreated())){
			mt.setCreated(mallThemeDTO.getCreated());
		}
		if(mallThemeDTO.getId() !=null || ("").equals(mallThemeDTO.getId())){
			mt.setId(mallThemeDTO.getId());
		}
		if(mallThemeDTO.getStatus() !=null || ("").equals(mallThemeDTO.getStatus())){
			mt.setStatus(mallThemeDTO.getStatus());
		}
		if(mallThemeDTO.getThemeName() !=null || ("").equals(mallThemeDTO.getThemeName())){
			mt.setThemeName(mallThemeDTO.getThemeName());
		}
		if(mallThemeDTO.getType() !=null || ("").equals(mallThemeDTO.getType())){
			mt.setType(mallThemeDTO.getType());
		}
		if(mallThemeDTO.getUserId() !=null || ("").equals(mallThemeDTO.getUserId())){
			mt.setUserId(mallThemeDTO.getUserId());
		}
			int add = mallThemeDao.update(mt);
			er.setResult(add);
		//}catch(Exception e){
		//	LOGGER.debug("---------------motifyMallTheme------------------",e);
		//	er.addErrorMessage("更新失败！");
		//}
		return er;
	}
	
	/**
	 * 
	 * <p>Discription:[获取所有地域子站的地区编码]</p>
	 * Created on 2016年3月18日
	 * @return
	 * @author:[王宏伟]
	 */
	public String[] queryGroupCityCode(){
		List<MallThemeDTO> mallThemeDTOs = mallThemeDao.queryGroupCityCode();
		String[] codes = new String[mallThemeDTOs.size()];
		for(int i = 0 ;i<codes.length;i++){
			codes[i] = mallThemeDTOs.get(i).getCityCode().toString();
		}
		return codes;
	}
}
