package com.camelot.util;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import java.util.Map;

/**
 * Created by Admin on 2015/7/14.
 */
public class CustomReportView extends JasperReportsMultiFormatView {
	private JasperReport report;

	public CustomReportView() {
		super();
	}

	protected JasperPrint fillReport(Map<String, Object> model) throws Exception {
		if (model.containsKey("url")) {
			setUrl(String.valueOf(model.get("url")));
			this.report = loadReport();
		}

		return super.fillReport(model);
	}

	protected JasperReport getReport() {
		return this.report;
	}
}
