package com.camelot.fastdfs;

import java.io.File;
import java.io.FileInputStream;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.statements.SpringFailOnTimeout;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Configuration
@Import(FdfsClientConfig.class)

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:*.yml",
    "classpath:*.conf",
    "classpath:*.xml"
})
public class ClientPooltest {
	@Resource
	FastFileStorageClient  defaultFastFileStorageClient;
	@Test  
    public void upload() throws Exception { 
		 File content = new File("D:/sofeware/pic/DSC_0680.JPG");  
         
	        FileInputStream fis = new FileInputStream(content);  
	        byte[] file_buff = null;  
	        if (fis != null) {  
	            int len = fis.available();  
	            file_buff = new byte[len];  
	            fis.read(file_buff);  
	        }  
	        
		/*DefaultFastFileStorageClient defaultFastFileStorageClient = new DefaultFastFileStorageClient();*/
		StorePath uploadFile = defaultFastFileStorageClient.uploadFile(fis, file_buff.length, "JPG", null);
		System.out.println(uploadFile);
	}

}
