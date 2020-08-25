package com.camelot.openplatform.dao.interceptor.mybatis;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class StatementHandlerInterceptor implements Interceptor {
	private Logger logger = Logger.getLogger(StatementHandlerInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		
		//通过反射获取到当前RoutingStatementHandler对象的delegate属性  
		BaseStatementHandler delegate = (BaseStatementHandler)ReflectUtil.getFieldValue(handler, "delegate");
		BoundSql boundSql = delegate.getBoundSql();

		//获得MyBatis xml配置中的sqlId
		MappedStatement ms = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
		String id = ms != null ? ms.getId() : "";
		logger.debug("===>MappedStatement.id:" + id);
		
		String sql = boundSql.getSql();
		logger.debug( "StatementHanderInteceptor:" + sql );
//		if( sql.indexOf("where") != -1 ){
//			sql = sql + " AND 1 = 1";
//		}
		
		Object obj = boundSql.getParameterObject();
		logger.info(sql + "\n" +JSONObject.toJSONString(obj));
		
		ReflectUtil.setFieldValue(boundSql, "sql", sql);
		return invocation.proceed();  
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		String prop1 = properties.getProperty("prop1");
		String prop2 = properties.getProperty("prop2");
		logger.debug("StatementHanderInteceptor properties:" + prop1 + "----" + prop2);  
	}
}
