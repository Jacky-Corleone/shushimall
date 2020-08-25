package com.camelot.common;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * 测试公用类
 * </pre>
 *
 * @author liujun
 * @version V0.1, 2015/3/13O
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/test.xml")
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
@Transactional
public class CommonTest {

}
