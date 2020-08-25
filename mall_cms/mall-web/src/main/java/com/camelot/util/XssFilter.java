package com.camelot.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class XssFilter implements Filter{
	private Logger LOG = Logger.getLogger(this.getClass());
	
	private static String APPLICATION_CONTEXT = "/mall-web";
	
	FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    	String url = ((HttpServletRequest) request).getRequestURI();
    	url = url.replace(APPLICATION_CONTEXT, "");
    	//需要配置不需要拦截的url
    	if(XssFilterUrlUtil.unXssFilterUrlMap.get(url)!=null){
    		LOG.info(((HttpServletRequest) request).getRequestURI()+"：不需要XSS拦截");
    		chain.doFilter(new XssHttpServletRequestWrapper(
                    (HttpServletRequest) request,XssFilterUrlUtil.unXssFilterUrlMap.get(url)), response); 
    	}else{
    		chain.doFilter(new XssHttpServletRequestWrapper(
                    (HttpServletRequest) request), response);
    	}
        
    }
}
