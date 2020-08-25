package com.camelot.goodscenter;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.Base;
import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.openplatform.common.Pager;
 
public class SearchServiceImpAlllTest extends Base {

	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpAlllTest.class);
	ApplicationContext ctx = null;
    private SearchItemExportService searchService;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		searchService = (SearchItemExportService) ctx.getBean("searchItemExportService");
	}
    @Test
    public void testSearchItem(){
    	SearchInDTO inDTO = new SearchInDTO();
    	Pager p = null;
    	try {
			ObjectInputStream aos = new ObjectInputStream(new FileInputStream("e:/a"));
			ObjectInputStream bos = new ObjectInputStream(new FileInputStream("e:/b"));
			inDTO = (SearchInDTO) aos.readObject();
			p = (Pager) bos.readObject();
		} catch (Exception e) {
			
		}
//    	inDTO.setAreaCode("11");
//    	inDTO.setAttributes("");
//    	List<Long> ids = new ArrayList<Long>();
//    	ids.add(223L);
//    	inDTO.setBrandIds(ids);
//    	inDTO.setCid(195L);
//    	inDTO.setOrderSort(2);
//    	Pager<ItemDTO> pager = new Pager<ItemDTO>();
//    	inDTO.setOrderSort(7);
//    	inDTO.setShopId(2000000206L);
//    	inDTO.setBuyerId(712L);
//    	inDTO.setAttributes("410:781;410:782;");
    	SearchOutDTO result = this.searchService.searchItem(inDTO, p);
    	Assert.assertNotEquals(null, result);
    }
	
}
