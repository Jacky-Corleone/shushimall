package com.camelot.mall.test;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/test_r")
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate);
		
		return "home";
	}
	
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public String report(Model model) {
		// 报表数据源
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(JavaBeanPerson.getList());
		
		// 动态指定报表模板url
		model.addAttribute("url", "/WEB-INF/jasper/spring_report.jasper");
		model.addAttribute("format", "xls"); // 报表格式
		model.addAttribute("jrMainDataSource", jrDataSource);
			
		return "reportView"; // 对应jasper-views.xml中的bean id
	}
	
}
