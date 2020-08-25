package com.camelot.basecenter.service;

import java.util.List;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.PagerModelDTO;
import com.camelot.basecenter.dto.ResultWrapperDTO;
import com.camelot.basecenter.dto.ZtreeNodeDTO;
import com.camelot.openplatform.common.ExecuteResult;

/** 
 * <p>Description: [省市区县服务]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface AddressBaseService {
     
	/*
	 * 省市区县查询
	 */
	public List<AddressBase> queryAddressBase(String parentCode);
    /**
     * <p>Discription:[根据id查找地区名称]</p>
     * Created on 2015年3月5日
     * @param id 编号
     * @return
     * @author:[杨芳]
     */
	public ExecuteResult<AddressBaseDTO> queryNameById(Integer id);
	/**
	 * <p>Discription:[根据省市编码查询]</p>
	 * Created on 2015年3月5日
	 * @param code
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<List<AddressBaseDTO>> queryNameByCode(String... code);
	
	/**
	 * <p>通过<code>areaCode</code>检索完整的地址名称</p>
	 * Created on 2016年2月14日
	 * @param areaCode 地址的code
	 * @return 完整的地址名称
	 * @author 顾雨
	 */
	public String getQualifiedName(int areaCode);
	
	/**
	 * <p>取得指定{@code level}对应的地址</p>
	 * Created on 2016年2月17日
	 * @param level 区域等级
	 * @return
	 * @author: 顾雨
	 */
	List<AddressBaseDTO> getAddressesByLevel(int level);
	
	/**
	 * <p>取得指定{@code level}对应的地址</p>
	 * Created on 2016年2月17日
	 * @param level 区域等级
	 * @return
	 * @author: 顾雨
	 */
	List<AddressBaseDTO> queryAddressListByName(String name);
	
	/**
	 * <p>根据拼音获取地址</p>
	 * Created on 2016年3月10日
	 * @param nameLetter 区域名称(拼音)
	 * @return
	 * @author: [王宏伟]
	 */
	List<AddressBaseDTO> queryByNameLetter(String nameLetter);
	
	
	
	public void save(AddressBaseDTO addressBaseDTO);
	
	public ResultWrapperDTO delete(String code);
	
	public ResultWrapperDTO newSave(AddressBaseDTO addressBaseDTO);
	
	
	/**
	 * 根据父节点查询子节点列表
	 * @param parentCode
	 * @return
	 */
	public List<AddressBaseDTO> getAddressBaseByparentCode(String parentCode);
	/**
	 * 根据code查询地址信息
	 * @param code
	 * @return
	 */
	public AddressBaseDTO getAddressBaseByCode(String code);
	
	
	public List<ZtreeNodeDTO> getAddressesByLevels(int... levels);
	
	
	public ZtreeNodeDTO getTreeRoot();
	
	public List<ZtreeNodeDTO> getTreeByParentCode(String parentCode);
	
	
	public PagerModelDTO<AddressBaseDTO> getAddressesWithPage(int pageNow,int pageSize,String parentCode);
	
	public boolean isAddressesCodeExist(String code);
	
	
	
	
	
}
