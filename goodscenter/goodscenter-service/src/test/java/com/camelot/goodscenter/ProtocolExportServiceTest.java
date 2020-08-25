package com.camelot.goodscenter;

import com.camelot.goodscenter.dto.*;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年06月08日
 *
 * @author 鲁鹏
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ProtocolExportServiceTest {

    ProtocolExportService ProtocolExportService = null;
   ApplicationContext ctx = null;

    @Before
    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
        ProtocolExportService = (ProtocolExportService) ctx.getBean("protocolExportService");
    }

    @Test
    public void queryByContractInfo() {
        ContractInfoDTO dto = new ContractInfoDTO();
        dto.setContractNo("XGJ201506230001");
        dto.setCreateBy("1000000639");
        ExecuteResult<ContractInfoDTO> er = ProtocolExportService.queryByContractInfo(dto);
        System.out.println(er.getResult());
        System.out.println(er.getResult().getContractName());
        System.out.println(er.getResultMessage());
    }
    
    @Test
    public void findByContractUrlShowDTO() {
    	ContractUrlShowDTO  dto = new ContractUrlShowDTO();
        dto.setContractInfoId("XY201512221727389416");
        ExecuteResult<DataGrid<ContractUrlShowDTO>> er = ProtocolExportService.queryContractUrlShow(dto);
        for (ContractUrlShowDTO d : er.getResult().getRows()) {
            System.out.println(d.getContractInfoId());
        }
        System.out.println(er.getResult());
  
    }
    

    @Test
    public void queryContractInfoList() {
        Pager page = new Pager();
        ContractInfoDTO dto = new ContractInfoDTO();
        dto.setActiveFlag("1");
        dto.setLoginId("1000000639");
        dto.setContractName("测试");
        dto.setItemName("测试");
        List<String> printerIds = new ArrayList<String>();
        List<String> supplierIds = new ArrayList<String>();
        printerIds.add("1000000639");
        printerIds.add("1");
        ExecuteResult<DataGrid<ContractInfoDTO>> er = ProtocolExportService.queryContractInfoList(dto, page);
        for (ContractInfoDTO d : er.getResult().getRows()) {
            System.out.println(d.getContractName());
        }
        System.out.println(er.getResult().getTotal());
        System.out.println(er.getResultMessage());
    }

    @Test
    public void queryContractInfoDTOPager() {
        Pager page = new Pager();
        ContractInfoDTO dto = new ContractInfoDTO();
        List<String> list = new ArrayList<String>();
        list.add("1000000639");
        dto.setSupplierIdList(list);
        dto.setActiveFlag("0");
        ExecuteResult<DataGrid<ContractInfoDTO>> er = ProtocolExportService.queryContractInfoPager(dto, page);
        for (ContractInfoDTO d : er.getResult().getRows()) {
            System.out.println(d.getContractName());
        }
        System.out.println(er.getResult().getTotal());
        System.out.println(er.getResultMessage());
    }

    @Test
    public void queryByContractMat() {
        ContractMatDTO dto = new ContractMatDTO();
        dto.setContractNo("11111");
        ExecuteResult<ContractMatDTO> er = ProtocolExportService.queryByContractMat(dto);
        System.out.println(er.getResult());
        System.out.println(er.getResult().getMatDesc());
        System.out.println(er.getResultMessage());
    }

    @Test
    public void queryContractmMatList() {
        Pager page = new Pager();
        ContractMatDTO dto = new ContractMatDTO();
        dto.setContractNo("XGJ201506230001");
        dto.setCreateBy("1000000639");
        ExecuteResult<DataGrid<Map>> er = ProtocolExportService.queryContractMatList(dto, page);
        for (Map d : er.getResult().getRows()) {
            System.out.println(d.get("itemName"));
        }
        System.out.println(er.getResult().getTotal());
        System.out.println(er.getResultMessage());
    }

    @Test
    public void queryByContractOrder() {
        ContractOrderDTO dto = new ContractOrderDTO();
        dto.setContractNo("11111");
        ExecuteResult<ContractOrderDTO> er = ProtocolExportService.queryByContractOrder(dto);
        System.out.println(er.getResult());
        System.out.println(er.getResult().getOrderNo());
        System.out.println(er.getResultMessage());
    }

    @Test
    public void queryContractOrderList() {
        Pager page = new Pager();
        ContractOrderDTO dto = new ContractOrderDTO();
        List list = new ArrayList();
        list.add("1000799");
        dto.setIds(list);
        dto.setState("0");
        dto.setActiveFlag("0");
        ExecuteResult<DataGrid<ContractOrderDTO>> er = ProtocolExportService.queryContractOrderList(dto, page);
        for (ContractOrderDTO d : er.getResult().getRows()) {
            System.out.println(d.getOrderNo());
        }
        System.out.println(er.getResult().getTotal());
        System.out.println(er.getResultMessage());
    }

    @Test
    public void addContractInfo() {
        ContractInfoDTO dto = new ContractInfoDTO();
        dto.setContractOrderNo("1000992848855");
        dto.setContractNo("XGJ201506260001");
        dto.setContractName("徐杰来袭男1");
        dto.setPrinterId(1000000639);//印刷厂用户主键
        dto.setSupplierId(1000000639);//供货方主键
        dto.setBeginDate(new Date());//合同有效期--开始
        dto.setEndDate(new Date());//合同有效期--结束
        dto.setPayType("1");//支付约定 支付类型
        dto.setInvoiceFlag("0");//发票状态 0-开票 1-不开票
        dto.setInvoiceType("1");//发票类型
        dto.setInvoiceName("徐杰来袭男女");//发票抬头
        dto.setDeliverMethod("");//配送方式
        dto.setRemark("徐杰来袭男");//备注
        dto.setConfirmBy("1000000639");//确认人
        dto.setConfirmDate(new Date());//确认时间
        dto.setApproveBy("1000000639");//审批人
        dto.setApproveDate(new Date());//审批时间
        dto.setStatus("1");//合同状态
        dto.setCreateRole(0);//创建者角色（卖0、买1）
        dto.setCreateBy("1");//
        dto.setCreateDate(new Date());//
        dto.setUpdateBy("0");//
        dto.setUpdateDate(new Date());//
        dto.setActiveFlag("0");//有效标记 0-有效 1-无效
        ExecuteResult<String> str = ProtocolExportService.addContractInfo(dto);
        System.out.println(str.getResultMessage() + str.getResult());

    }

    @Test
    public void addContractMat() {
        ContractMatDTO dto = new ContractMatDTO();
        dto.setContractNo("XGJ201506230001");
        dto.setMatCd("1000000006");//物料号
        dto.setMatDesc("宝马");//物料描述
        dto.setLable1Cd("10");//类别1
        dto.setLable1Desc("Junit测试");//类别1描述
        dto.setLable2Cd("1010");//类别2
        dto.setLable2Desc("Junit测试");//类别2描述
        dto.setLable3Cd("101010");//类别3
        dto.setLable3Desc("Junit测试");//类别3描述
        dto.setMatSpec("Junit测试");//物料规格
        dto.setMatBrand("Junit测试");//品牌
        dto.setMatDiscount("5");//折扣
        dto.setMatPrice(100.00);//价格
        dto.setMatUnit("PC");//单位
        dto.setCreateBy("1");//创建人
        dto.setCreateDate(new Date());//创建日期
        dto.setUpdateBy("1");//修改人
        dto.setUpdateDate(new Date());//修改时间
        dto.setActiveFlag("0");//有效标记 0-有效 1-无效
        ExecuteResult<String> str = ProtocolExportService.addContractMat(dto);
        System.out.println(str.getResultMessage() + str.getResult());


        dto.setContractNo("XGJ201506230002");
        dto.setMatCd("1000000005");//物料号
        dto.setMatDesc("商品1");//物料描述
        dto.setLable1Cd("10");//类别1
        dto.setLable1Desc("Junit测试");//类别1描述
        dto.setLable2Cd("1010");//类别2
        dto.setLable2Desc("Junit测试");//类别2描述
        dto.setLable3Cd("101010");//类别3
        dto.setLable3Desc("Junit测试");//类别3描述
        dto.setMatSpec("Junit测试");//物料规格
        dto.setMatBrand("Junit测试");//品牌
        dto.setMatDiscount("5");//折扣
        dto.setMatPrice(88.88);//价格
        dto.setMatUnit("PC");//单位
        dto.setCreateBy("1");//创建人
        dto.setCreateDate(new Date());//创建日期
        dto.setUpdateBy("1");//修改人
        dto.setUpdateDate(new Date());//修改时间
        dto.setActiveFlag("0");//有效标记 0-有效 1-无效
        ProtocolExportService.addContractMat(dto);


        dto.setContractNo("XGJ201506230003");
        dto.setMatCd("1000000003");//物料号
        dto.setMatDesc("商品11");//物料描述
        dto.setLable1Cd("10");//类别1
        dto.setLable1Desc("Junit测试");//类别1描述
        dto.setLable2Cd("1010");//类别2
        dto.setLable2Desc("Junit测试");//类别2描述
        dto.setLable3Cd("101010");//类别3
        dto.setLable3Desc("Junit测试");//类别3描述
        dto.setMatSpec("Junit测试");//物料规格
        dto.setMatBrand("Junit测试");//品牌
        dto.setMatDiscount("5");//折扣
        dto.setMatPrice(99.01);//价格
        dto.setMatUnit("PC");//单位
        dto.setCreateBy("1");//创建人
        dto.setCreateDate(new Date());//创建日期
        dto.setUpdateBy("1");//修改人
        dto.setUpdateDate(new Date());//修改时间
        dto.setActiveFlag("0");//有效标记 0-有效 1-无效
        ProtocolExportService.addContractMat(dto);
    }

    @Test
    public void addContractOrder() {
        ContractOrderDTO dto = new ContractOrderDTO();
        dto.setContractNo("10001");
        dto.setOrderNo("00000101");
        dto.setState("1");
        dto.setCreateBy("1");//创建人
        dto.setCreateDate(new Date());//创建日期
        dto.setUpdateBy("1");//修改人
        dto.setUpdateDate(new Date());//修改时间
        dto.setRemark("Junit测试");
        dto.setActiveFlag("0");//有效标记 0-有效 1-无效
        ExecuteResult<String> str = ProtocolExportService.addContractOrder(dto);
        System.out.println(str.getResultMessage() + str.getResult());
    }

    @Test
    public void addcontractUrlShow() {
    	ContractUrlShowDTO dto = new ContractUrlShowDTO();
    	//dto.setContractInfoId((long) 270);
    	dto.setImgUrl("11111");
    	dto.setIsDelete("1");
        ExecuteResult<String> str = ProtocolExportService.addcontractUrlShow(dto);
        System.out.println(str.getResultMessage() + str.getResult());
    }
    
    
    
    
    @Test
    public void modifyContractInfo() {
        ContractInfoDTO dto = new ContractInfoDTO();
        dto.setContractNo("XY201507220712388683");
        dto.setRefusalReason("王帅 ");
        ExecuteResult<String> str = ProtocolExportService.modifyContractInfo(dto);
        System.out.println(str.getResult());
    }

    @Test
    public void modifyContractMat() {
        ContractMatDTO dto = new ContractMatDTO();
        dto.setContractNo("XGJ201506230001");
        dto.setMatPrice(200.00);
        dto.setCost(2000.00);
        ExecuteResult<String> str = ProtocolExportService.modifyContractMat(dto);
        System.out.println(str.getResult());
    }

    @Test
    public void modifyContractOrder() {
        ContractOrderDTO dto = new ContractOrderDTO();
        dto.setId(1L);
        dto.setActiveFlag("1");
        ExecuteResult<String> str = ProtocolExportService.modifyContractOrder(dto);
        System.out.println(str.getResult());
    }

    @Test
    public void createContractNo() {
        ExecuteResult<String> str = ProtocolExportService.createContractNo();
        System.out.println(str.getResult());
    }

    @Test
    public void queryByContractPaymentTerm() {
        ContractPaymentTermDTO dto = new ContractPaymentTermDTO();
        dto.setContractNo("10001");
        ExecuteResult<ContractPaymentTermDTO> er = ProtocolExportService.queryByContractPaymentTerm(dto);
        System.out.println(er.getResult());
        System.out.println(er.getResult().getPaymentDays());
        System.out.println(er.getResultMessage());
    }

    @Test
    public void queryContractPaymentTerm() {
        Pager page = new Pager();
        ContractPaymentTermDTO dto = new ContractPaymentTermDTO();
        dto.setContractNo("");
        ExecuteResult<DataGrid<ContractPaymentTermDTO>> er = ProtocolExportService.queryContractPaymentTermList(dto, page);
        for (ContractPaymentTermDTO d : er.getResult().getRows()) {
            System.out.println(d.getPaymentDays());
        }
        System.out.println(er.getResult().getTotal());
        System.out.println(er.getResultMessage());
    }

    @Test
    public void addContractPaymentTerm() {
        ContractPaymentTermDTO dto = new ContractPaymentTermDTO();
        dto.setContractNo("10002");
        dto.setPaymentDays(1000);
        dto.setPaymentType(1);
        dto.setPaymentProportion(1);
        dto.setCreateBy("1");//创建人
        dto.setActiveFlag("0");//有效标记 0-有效 1-无效
        ExecuteResult<String> str = ProtocolExportService.addContractPaymentTerm(dto);
        System.out.println(str.getResultMessage() + str.getResult());
    }

    @Test
    public void modifyContractPaymentTerm() {
        ContractPaymentTermDTO dto = new ContractPaymentTermDTO();
        dto.setContractNo("10001");
        dto.setActiveFlag("1");
        ExecuteResult<String> str = ProtocolExportService.modifyContractPaymentTerm(dto);
        System.out.println(str.getResult());
    }
    
    @Test
    public void deleteContractUrlShow() {
    	
        ExecuteResult<String> str = ProtocolExportService.deleteContractUrlShow("18");
        System.out.println(str.getResult());
    }
    
    
}
