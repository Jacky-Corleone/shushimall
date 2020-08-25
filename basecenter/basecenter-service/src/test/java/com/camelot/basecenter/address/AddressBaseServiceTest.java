package com.camelot.basecenter.address;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.PagerModelDTO;
import com.camelot.basecenter.dto.ZtreeNodeDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.openplatform.common.ExecuteResult;

public class AddressBaseServiceTest {
	private AddressBaseService addressBaseService;
    ApplicationContext ctx;
    
    public Log logger = LogFactory.getLog(AddressBaseServiceTest.class);
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		addressBaseService = (AddressBaseService) ctx.getBean("addressBaseService");
	}
	//@Test
	public void queryAddressBaseTest(){
		List<AddressBase> ss = addressBaseService.queryAddressBase("0");
		System.out.println(ss.size());
	}
	//@Test
	public void queryNameByIdTest(){
		Integer id=6;
		ExecuteResult<AddressBaseDTO> er=addressBaseService.queryNameById(id);
		System.out.println(er.getResult().getName());
	}
	//@Test
	public void queryNameByCodeTest(){
		String code="";
		ExecuteResult<List<AddressBaseDTO>> er=addressBaseService.queryNameByCode(code);
		System.out.println(JSON.toJSONString(er.getResult()));
	}
	//@Test
	public void queryByNameLetterTest(){
		List<AddressBaseDTO> ss = addressBaseService.queryByNameLetter("ceshi");
		System.out.println(ss.size());
	}
	
	
	
	/**
	 * @author thinking
	 */
	@Test
	public void getAddressBaseTest(){
		
		List<AddressBaseDTO> ss = addressBaseService.getAddressBaseByparentCode("0");
		int[] levels = {0,1,2}; 
		List<ZtreeNodeDTO> resultLevels =   addressBaseService.getAddressesByLevels();
		PagerModelDTO<AddressBaseDTO>  page= addressBaseService.getAddressesWithPage(1,10,"0");
		AddressBaseDTO addressBaseDTO = getAddressBaseBean("9999999999999999999999999999");
		addressBaseDTO.setId(47503);
		//addressBaseService.newSave(addressBaseDTO);
		//addressBaseService.save(addressBaseDTO);
		//addressBaseService.delete("ggggggg");
		
		System.out.println("=====================");
	}
	
	public AddressBaseDTO getAddressBaseBean(String name) {
		AddressBaseDTO addressBaseDTO  = new AddressBaseDTO();
		addressBaseDTO.setParentcode("0");
		addressBaseDTO.setCode("ggggggg");
		addressBaseDTO.setName("thinking");
		addressBaseDTO.setYn(1);
		if(name != null ){
			addressBaseDTO.setName(name);
		}
		return addressBaseDTO;
	}
	
	
	public static void main(String[] args) {
		
		AddressBaseDTO addressBaseDTO  = new AddressBaseDTO();
		addressBaseDTO.setParentcode("0");
		addressBaseDTO.setCode("ggggggg");
		addressBaseDTO.setName("thinking");
		//addressBaseDTO.setCreatetime(new Date());
		//addressBaseDTO.setUpdatetime(new Date());
		AddressBase po = new AddressBase();
		try {
			PropertyUtils.copyProperties(po, addressBaseDTO);
			BeanUtils.copyProperties(po, addressBaseDTO);
			org.springframework.beans.BeanUtils.copyProperties(addressBaseDTO, po);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		}
}
