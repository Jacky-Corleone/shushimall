package com.camelot.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import com.camelot.openplatform.common.Pager;

/** 
 * Description: [分页模板标签]
 * Created on 20160125
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">zhangzq</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部 
 */
public class PageVelocity extends Directive {
	
	private static final VelocityEngine velocityEngine = new VelocityEngine(); 
	
	@Override
	public String getName() {
		return "pageVelocity";
	}

	@Override
	public int getType() {
		return LINE;
	}
	
	
	@Override
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		//获取分页参数
		SimpleNode sn_region = (SimpleNode) node.jjtGetChild(0);
		Object obj = sn_region.value(context);
		//获取页面跳转方法
		String page_method = "topage";
		//获取参数数量，根据数量判断是否传入分页方法
		int num = node.jjtGetNumChildren();
		if(num > 1){
			SimpleNode sn_region_page = (SimpleNode) node.jjtGetChild(1);
			Object pageObj = sn_region_page.value(context);
			page_method = pageObj == null ? page_method:pageObj.toString();
		}
		Pager pager = (Pager)obj;
		Map map = new HashMap();
		map.put("pager", pager);
		map.put("page_method", page_method);
		//渲染模板并输出到页面
		writer.write(renderTemplate(map,pageTemp(pager,page_method)));
		return false;
	}
	
	/**
	 * 渲染模板
	 * @param params 参数
	 * @param vimStr 页面模板代码
	 * @return 页面输出
	 */
	public static String renderTemplate(Map params,String vimStr){  
        VelocityContext context = new VelocityContext(params);  
        StringWriter writer = new StringWriter();  
        try {  
            velocityEngine.evaluate(context, writer, "", vimStr);  
        } catch (ParseErrorException e) {  
            e.printStackTrace();  
        } catch (MethodInvocationException e) {  
            e.printStackTrace();  
        } catch (ResourceNotFoundException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return writer.toString();  
    }  
	
	/**
	 * 获取分页页面代码模板
	 * @param pager 分页实体
	 * @return 模板代码
	 */
	private String pageTemp(Pager pager,String page_method){
		StringBuffer pageHtml = new StringBuffer("<div class=\"page ml14px\"><ul><li class=\"lastPage\"><a href=\"javascript:"+page_method+"(1)\">首页</a></li>");
		pageHtml.append("<li class=\"lastPage\"><a href=\"javascript:"+page_method+"($pager.previewPage)\">上一页</a></li>");
		pageHtml.append("#foreach($pageNo in [$!pager.startPageIndex .. $!pager.endPageIndex])");
		pageHtml.append("<li>");
		pageHtml.append("<a #if($pageNo==$pager.page) class=\"curr\" #end href=\"javascript:"+page_method+"($pageNo)\">$pageNo</a>");
		pageHtml.append("</li>");
		pageHtml.append("#end");
		pageHtml.append("<li class=\"nextPage\"><a href=\"javascript:"+page_method+"($pager.nextPage)\">下一页</a></li>");
		pageHtml.append("<li class=\"nextPage\"><a href=\"javascript:"+page_method+"($pager.totalPage)\">末页</a>&nbsp;共$pager.totalPage页</li>");
		pageHtml.append("<li class=\"nextPage\">，第<input class=\"input_Style2 wid_20 hei_26\" value=\"$pager.page\" onkeyup=\"value=this.value.replace(/\\D+/g,'')\" onkeydown=\"enter_press_page(event.keyCode||event.which);\" id=\"pageNum\"/>页 <span class=\"cursor font_fe\" id=\""+page_method+"Num\" onclick=\"page_num()\">[跳转]&nbsp;&nbsp;</span></li>");
		pageHtml.append("</ul></div>");
		pageHtml.append("<script>function page_num(){var pageNumVal=$(\"#pageNum\").val();if(pageNumVal == ''){"+page_method+"(1);$(\"#pageNum\").val(1)}else if(parseInt(pageNumVal)>=parseInt($pager.totalPage)){"+page_method+"($pager.totalPage);}else{"+page_method+"(pageNumVal)}}");
		pageHtml.append("function enter_press_page(e){if(e == 13|| e == 32){page_num()}}");
		pageHtml.append("</script>");
		return pageHtml.toString();
	}

}