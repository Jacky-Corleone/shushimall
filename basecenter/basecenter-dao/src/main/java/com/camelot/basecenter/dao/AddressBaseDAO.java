package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.ZtreeNodeDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface AddressBaseDAO extends BaseDAO<AddressBase>{
	
	public List<AddressBase> queryAddressBase(String parentCode);
	
	public AddressBaseDTO queryNameById(@Param("id")Integer id);

	public List<AddressBaseDTO> queryNameByCode(@Param("code")String... code);

	public String getQualifiedName(@Param("areaCode")int areaCode);
	
	List<AddressBaseDTO> getAddressesByLevel(int level);
	
	public List<AddressBaseDTO> queryAddressListByName(@Param("name")String name);

	public List<AddressBaseDTO> queryByNameLetter(@Param("nameLetter")String nameLetter);
	
	//-------------------------------------------------------------
	
	
	public void newAdd(AddressBaseDTO addressBaseDTO);
	public void newUpdate(AddressBaseDTO addressBaseDTO);
	
	/**
	 * @author thinking
	 * @param code
	 * @return
	 */
	public AddressBaseDTO getByCode(String code);
	/**
	 * @author thinking
	 * @param id
	 * @return
	 */
	public List<AddressBaseDTO> getChildsByParentCode(@Param("parentCode")String parentCode);
	
	
	
	public List<ZtreeNodeDTO> getAddressesByLevels(int... levels);
	
	public ZtreeNodeDTO getTreeRoot();
	
	public List<ZtreeNodeDTO> getTreeByParentCode(String parentCode);
	
	
	public List<AddressBaseDTO> getAddressesWithPage(@Param("start")int start,@Param("maxRow")int maxRow,@Param("parentCode")String parentCode);
	
	public long getAddressesCountByParent(@Param("parentCode")String parentCode);
	
	public int isAddressesCodeExist(@Param("code")String code);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
