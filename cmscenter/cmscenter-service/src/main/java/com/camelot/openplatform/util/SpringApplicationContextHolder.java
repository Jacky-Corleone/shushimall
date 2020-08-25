package com.camelot.openplatform.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringApplicationContextHolder implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringApplicationContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 获取applicationContext对象
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据beanid获取spring中的bean对象
	 * 
	 * @param beanId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId) {
		return (T) applicationContext.getBean(beanId);
	}
	
	/**
	 * 通过bean类型获取bean的名称
	 * @param cla
	 */
	public static String[] getBeanNameByType(Class<?> cla)
	{
		return applicationContext.getBeanNamesForType(cla);
	}
	
	
	
	/**
     * @param <T>
	 * @desc 向spring容器注册bean
     * @param beanName 要注册的bean的名称
     * @param cla 类
     * constructorArgValueList 构造参数
     * paramsValMap 值属性
     * referenceValMap 映射属性
     */
    public static void registerBean(String beanName,Class cla,List<Object> constructorArgValueList,Map<String,Object> paramsValMap,Map<String,String> referenceValMap) {
    	ConfigurableApplicationContext configurableApplicationContext =  (ConfigurableApplicationContext) applicationContext; 
    	DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
    	BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cla);    
    	// 设置构造属性
    	if(constructorArgValueList != null && constructorArgValueList.size() > 0){
    		for(Object obj : constructorArgValueList){
    			beanDefinitionBuilder.addConstructorArgValue(obj);
    		}
    	}
    	//为属性赋值
    	if(paramsValMap != null && paramsValMap.size() > 0){
    		for (Map.Entry<String,Object> entry : paramsValMap.entrySet()) {  
    			beanDefinitionBuilder.addPropertyValue(entry.getKey(), entry.getValue());
    		}
    	}
    	//为属性赋值，使用映射的形式
    	if(referenceValMap != null && referenceValMap.size() > 0){
    		for (Map.Entry<String,String> entry : referenceValMap.entrySet()) {  
    	    	beanDefinitionBuilder.addPropertyReference(entry.getKey(), entry.getValue());
    		}
    	}
    	// 注册bean
    	defaultListableBeanFactory.registerBeanDefinition(beanName,beanDefinitionBuilder.getRawBeanDefinition()); 
    }
}
