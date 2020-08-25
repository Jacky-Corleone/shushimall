package com.camelot.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;

/**
 * Created by zhoule on 2015/6/14.
 * 本过滤器的主要作用是根据二级域名查找店铺对应的id，根据店铺id，将请求转发到具体的店铺首页
 */
public class DomainFilter implements Filter {
    private static final String WWW = "www";
    private static final String SECOND_DOMAIN_KEY= "second_domain_key";
    /**
     * 应用上下文
     */
    private static final String APPLICATION_CONTEXT = "mall-web";

    private RedisDB redisDB = null;

    private ShopExportService shopExportService = null;

    /**
     * 需要过滤的不作为二级域名处理的域名
     */
    private List<String> filterDomain = new ArrayList<String>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //初始化spring对象
        initBean(filterConfig);
        //将二级域名存放到redis中
        shopExportService.addSecondDomainToRedis();
        //初始化需要过滤的店铺域名
        initFilterDomain();
    }

    /**
     * 初始化需要过滤的域名
     */
    private void initFilterDomain() {
        filterDomain.add("www.shushi100.com");
        filterDomain.add("localhost");
        filterDomain.add("shushi100.com");
        filterDomain.add("www1.shushi100.com");
        filterDomain.add("qc.shushi100.com");
        filterDomain.add("z1.shushi100.com");
    }


    private void initBean(FilterConfig filterConfig) {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        redisDB = (RedisDB)context.getBean("redisDB");
        shopExportService = (ShopExportService)context.getBean("shopExportService");
        if(redisDB==null || shopExportService==null){
            throw new RuntimeException("spring实例【redisDB、shopExportService】获取失败！");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        String serverName = req.getServerName();//服务器名
        String uri = req.getRequestURI();//请求的uri
        String realURI = getRealRequestURI(serverName, uri);//将uri转化为店铺的uri
        if(StringUtils.isBlank(realURI)){//域名不存在：重定向到提示页面
            req.getRequestDispatcher("/shopBaseInfoController/message").forward(request, response);
            return;
        }
        realURI = realURI.replace(APPLICATION_CONTEXT, "");
        req.getRequestDispatcher(realURI).forward(request, response);
        return;
    }

    /**
     * 获取真是的请求路径
     * @param serverName 服务器地址
     * @param uri 请求路径
     * @return
     */
    private String getRealRequestURI(String serverName, String uri) {
        if(filterDomain.contains(serverName) || uri.startsWith("/assets/")){
            return uri;
        }

        int firstPointIndex = serverName.indexOf(".");
        String secondDomain = serverName.substring(0, firstPointIndex);
        if(WWW.equals(secondDomain)){
            return uri;
        }

        String regex = "\\w*\\.shushi100\\.com";
        if(serverName.matches(regex) && (StringUtils.isBlank(uri) || "/".equals(uri))) {
            String retSecondDomain = getShopIdBySecondDomain(secondDomain);
            if(StringUtils.isBlank(retSecondDomain)){
            	return getShopIdInRedis(secondDomain);
            }
            return "/shopItemListIndexController/toIndex?shopId=" + retSecondDomain;
        }
        return uri;
    }

    /**
     * 根据二级域名获取店铺id
     * @param secondDomain
     * @return
     */
    @SuppressWarnings("unchecked")
    private String getShopIdBySecondDomain(String secondDomain) {
        Object obj = redisDB.getObject(SECOND_DOMAIN_KEY);
        if(null == obj){
            return "";
        }
        Map<String, String> secondDomainMap = (Map<String, String>)obj;

        return secondDomainMap.get(secondDomain.toLowerCase());
    }

    @Override
    public void destroy() {

    }
    /**
     * 根据店铺二级域名获得店铺id,重新放入到redis中
     */
    private String getShopIdInRedis(String secondDomain){
    	ShopDTO dto=new ShopDTO();
    	dto.setShopUrl(secondDomain);
    	dto.setRunStatus(1);
    	ExecuteResult<DataGrid<ShopDTO>> result=shopExportService.findShopInfoByCondition(dto, null);
    	if(!result.isSuccess()){
    		return "";
    	}
		DataGrid<ShopDTO> shopDto=result.getResult();
		if(shopDto.getRows() == null || shopDto.getRows().size() == 0){
			return "";
		}
		ShopDTO shopinfo=shopDto.getRows().get(0);
		Object obj = redisDB.getObject(SECOND_DOMAIN_KEY);
		if(obj == null){
			obj = new HashMap<String,String>();
		}
		Map<String, String> secondDomainMap = (Map<String, String>)obj;
        if(StringUtils.isNotBlank(shopinfo.getShopUrl()) && null!=shopinfo.getShopId()){
            secondDomainMap.put(shopinfo.getShopUrl().toLowerCase(), shopinfo.getShopId() + "");
            redisDB.addObject(SECOND_DOMAIN_KEY, secondDomainMap);
            return "/shopItemListIndexController/toIndex?shopId=" + shopinfo.getShopId();
        }
    	return "";
    }
}
