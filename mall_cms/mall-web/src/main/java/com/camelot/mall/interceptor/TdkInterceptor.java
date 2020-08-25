package com.camelot.mall.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.basecenter.service.BaseTDKConfigService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.sellercenter.logo.dto.LogoDTO;
import com.camelot.sellercenter.logo.service.LogoExportService;

/**
 * Created by Administrator on 2015/5/28.
 */
public class TdkInterceptor implements HandlerInterceptor {
//    private static final Logger LOG = LoggerFactory.getLogger(TdkInterceptor.class);
    //tdk存储在redis中的key值
    private static final String KEY_TDK_REDIS = "tdk";

    //logo存储在redis中的key值
    private static final String KEY_LOGO_REDIS = "logo";

    @Resource
    private BaseTDKConfigService baseTDKConfigService;

    @Resource
	private LogoExportService logoService;

    @Resource
    private RedisDB redisDB;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//TDK
        Object obj = redisDB.getObject(KEY_TDK_REDIS);
        BaseTDKConfigDTO baseTDKConfigDTO = new BaseTDKConfigDTO();
        if(obj == null){
            //查询TDK详细属性信息
            ExecuteResult<BaseTDKConfigDTO> executeResult = baseTDKConfigService.queryBaseTDKConfig(null);
            if(null != executeResult.getResult()){
                baseTDKConfigDTO = executeResult.getResult();
                redisDB.addObject(KEY_TDK_REDIS, baseTDKConfigDTO,86400);
            }
        }else{
            baseTDKConfigDTO = (BaseTDKConfigDTO)obj;
        }
        request.setAttribute("baseTDKConfigDTO", baseTDKConfigDTO);

        //LOGO
        Object logoObj = redisDB.getObject(KEY_LOGO_REDIS);
        LogoDTO logoDTO = new LogoDTO();
        if(logoObj == null){
        	ExecuteResult<LogoDTO> logoExecuteResult = this.logoService.getMallLogo();
            if(null != logoExecuteResult.getResult()){
            	logoDTO = logoExecuteResult.getResult();
                redisDB.addObject(KEY_LOGO_REDIS, logoDTO);
            }
        }else{
        	logoDTO = (LogoDTO)logoObj;
        }
        request.setAttribute("logoDTO", logoDTO);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
