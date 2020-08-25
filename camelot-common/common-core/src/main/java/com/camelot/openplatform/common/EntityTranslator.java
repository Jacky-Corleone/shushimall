package com.camelot.openplatform.common;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.camelot.openplatform.common.exception.CommonCoreException;

/**
 * 实体类转换器
 * 
 * @Description -
 * @author -huhengxin@camelotchina.com
 * @createDate - 2015-1-22
 */
public class EntityTranslator {
	/**
	 * set方法前缀
	 */
	private static String SET = "set";
	/**
	 * get方法前缀
	 */
	private static String GET = "get";

	@Deprecated
	public static <T> T transToDto(Object obj, Class<T> dtoClass) {
		// 获取domain类
		Class<?> domainClass = obj.getClass();
		// 获取dto的类属性
		Field[] dtoFields = dtoClass.getDeclaredFields();
		T dto = null;
		try {
			dto = dtoClass.newInstance();
		} catch (Exception e) {
			throw new CommonCoreException("获取对象实例出错:"+e.getMessage());
		}
		for (Field field : dtoFields) {
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof PropertyMapping) {
					// 获取domain属性名
					String proVal = ((PropertyMapping) annotation).value();
					// domain Get方法名
					String domainMethodName = getMethodName(GET, proVal);
					// domain Get方法
					Method domainMethod;
					try {
						domainMethod = domainClass.getDeclaredMethod(domainMethodName);
					} catch (Exception e) {
						throw new CommonCoreException(e);
					}
					// 获取domain属性的值
					Object val;
					try {
						val = domainMethod.invoke(obj);
						// dto的Set方法名
						String dtoMethodName = getMethodName(SET, field.getName());
						// dto Set方法
						Method dtoMethod = dtoClass.getDeclaredMethod(dtoMethodName);
						// 赋值
						dtoMethod.invoke(dto, val);
					} catch (Exception e) {
						throw new CommonCoreException(e);
					}
				}
			}
		}
		return dto;

	}

	/**
	 * <p>Discription:[将dto转换成domain类]</p>
	 * Created on 2015年2月9日
	 * @param obj
	 * @param domainClass
	 * @return
	 * @author:[胡恒心]
	 */
	@Deprecated
	public static <T> T transToDomain(Object obj, Class<T> domainClass) {
		// 获取domain类
		Class<?> dtoClass = obj.getClass();
		// 获取dto的类属性
		Field[] dtoFields = dtoClass.getDeclaredFields();
		T dto;
		try {
			dto = domainClass.newInstance();
		} catch (Exception e) {
			throw new CommonCoreException(e);
		}
		for (Field field : dtoFields) {
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof PropertyMapping) {
					// 获取domain属性名
					String proVal = ((PropertyMapping) annotation).value();
					// domain Get方法名
					String domainMethodName = getMethodName(GET, proVal);
					// domain Get方法
					Method domainMethod;
					try {
						domainMethod = domainClass.getDeclaredMethod(domainMethodName);
						// 获取domain属性的值
						Object val = domainMethod.invoke(obj);
						// dto的Set方法名
						String dtoMethodName = getMethodName(SET, field.getName());
						// dto Set方法
						Method dtoMethod = dtoClass.getDeclaredMethod(dtoMethodName);
						// 赋值
						dtoMethod.invoke(dto, val);
					} catch (Exception e) {
						throw new CommonCoreException(e);
					}
				}
			}
		}
		return dto;
	}

	private static String getMethodName(String annotationName, String type) {
		return new StringBuffer(type)
				.append(Character.toUpperCase(annotationName.charAt(0)))
				.append(annotationName.substring(1)).toString();
	}

	/**
	 * 对象转换
	 * 
	 * @param obj
	 *            - 待转换对象
	 * @param objClazz
	 *            - 生成对象
	 * @param isDo2Dto
	 *            - 转换方式：true(domain-->dto)/false(dto-->domain)
	 * @return objClazz
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T transObj(Object obj, Class<T> objClazz, boolean isDo2Dto) {

		Method domainMethod, dtoMethod;
		Object value;
		Class domainClass, dtoClass;

		T objOut;
		try {
			objOut = objClazz.newInstance();
		} catch (Exception e) {
			throw new CommonCoreException(e);
		}
		Map<String, Method> methods = new HashMap<String, Method>();

		if (isDo2Dto) {
			domainClass = obj.getClass();
			dtoClass = objClazz;
		} else {
			domainClass = objClazz;
			dtoClass = obj.getClass();
		}

		Field[] domainFields = domainClass.getDeclaredFields();
		try {
			for (Field field : domainFields) {
				Annotation[] annotations = field.getAnnotations();
				String domainUpper = Character.toUpperCase(field.getName()
						.charAt(0)) + field.getName().substring(1);
				for (Annotation annotation : annotations) {
					if (annotation instanceof PropertyMapping) {
						if (isDo2Dto) {
							domainMethod = domainClass.getMethod("get" + domainUpper);
						} else {
							domainMethod = domainClass.getMethod("set" + domainUpper, field.getType());
						}
						methods.put(((PropertyMapping) annotation).value(), domainMethod);
					}
				}
			}
		} catch (Exception e) {
			throw new CommonCoreException(e);
		}

		Field[] dtoFields = dtoClass.getDeclaredFields();
		try {
			for (Field field : dtoFields) {
				String dtoFieldName = field.getName();
				domainMethod = methods.get(dtoFieldName);
				if (domainMethod != null) {
					String dtoUpper = Character.toUpperCase(dtoFieldName.charAt(0)) + dtoFieldName.substring(1);
					if (isDo2Dto) {
						dtoMethod = dtoClass.getMethod("set" + dtoUpper, field.getType());
						value = domainMethod.invoke(obj);
						dtoMethod.invoke(objOut, value);
					} else {
						dtoMethod = dtoClass.getMethod("get" + dtoUpper);
						value = dtoMethod.invoke(obj);
						domainMethod.invoke(objOut, value);
					}
				}
			}
		} catch (Exception e) {
			throw new CommonCoreException(e);
		}
		return objOut;
	}

	/**
	 * 转换List
	 * 
	 * @param objList
	 *            - 待转换list对象
	 * @param objClazz
	 *            - 生成对象
	 * @param isDo2Dto
	 *            - 转换方式：true(domain-->dto)/false(dto-->domain)
	 * @return List
	 * @throws Exception
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static <T, E> List<T> transList(List<E> objList, Class<T> objClazz, boolean isDo2Dto) {
		List<T> objOutList = new ArrayList<T>();
		List<Object> list = (List<Object>) objList;
		for (Object obj : list) {
			objOutList.add(transObj(obj, objClazz, isDo2Dto));
		}
		return objOutList;
	}
	
	/**
	 * 
	 * <p>Discription:[domain转DTO类]</p>
	 * Created on 2015年2月3日
	 * @param clazz
	 * @param domains
	 * @return
	 * @author:[胡恒心]
	 * @throws Exception 
	 */
	public static <T> T transDomainToDTO(Class<T> clazz, Object... domains) {
		Field[] domainFields = null;  //每个domain类的所有属性
		Field[] dtoFields = clazz.getDeclaredFields();  //获取需要转换的DTO类的所有属性
		Annotation[] annotations = null; //每个属性上的所有注解
		Method getMethod = null, setMethod = null;
		PropertyDescriptor pd = null;
	  //Map<String, Method> domainGetMethods = new HashMap<String, Method>();  //存储所有标有注解的字段的get方法,key->标注value
		Map<String, Object> domainFieldValues = new HashMap<String, Object>();  //存储所有标有自定义注解的字段的值,key->标注value
		T dto = null;
		Object value = null;
		
		//循环获取domain的所有字段
		try {
			for (Object domain : domains) {
				domainFields = domain.getClass().getDeclaredFields(); //获取所有的字段
				for (Field field : domainFields) {
					annotations = field.getAnnotations(); //获取字段上的所有注解
					for (Annotation annotation : annotations) {
						if (annotation instanceof PropertyMapping) { //判断注解是否为自定义类型的
							pd = new PropertyDescriptor(field.getName(),
									domain.getClass());
							getMethod = pd.getReadMethod(); //获取字段的get方法
							//domainGetMethods.put(field.getName(), getMethod);

							value = getMethod.invoke(domain); //通过反射获取执行get方法获取属性值
							domainFieldValues.put(
									((PropertyMapping) annotation).value(),
									value);
						}
					}
				}
			}
			dto = clazz.newInstance(); //通过反射new一个对象
			//循环给每个DTO的属性赋值
			for (Field dtofield : dtoFields) {
				if (dtofield.getName() == "serialVersionUID") {
					continue;
				}
				pd = new PropertyDescriptor(dtofield.getName(), clazz);
				setMethod = pd.getWriteMethod(); //获取字段的set方法
				value = domainFieldValues.get(dtofield.getName()); //获取Map中存储的对应字段的值
				if (value != null) {
					setMethod.invoke(dto, value); //通过反射给DTO对象赋值
				}
			}
		} catch (Exception e) {
			throw new CommonCoreException(e);
		}
		return dto;
	}
	
	/**
	 * 
	 * <p>Discription:[DTO转domain类]</p>
	 * Created on 2015年2月3日
	 * @param clazz
	 * @param dto
	 * @return
	 * @author:[胡恒心]
	 */
	public static <T> T transDTOToDomin(Class<T> clazz, Object dto) {
		Field[] domainFields = clazz.getDeclaredFields();  //获取需要转换的domain类的所有属性
		Field[] dtoFields = dto.getClass().getDeclaredFields();
		Annotation[] annotations = null; //每个属性上的所有注解
		Method getMethod = null, setMethod = null;
		PropertyDescriptor pd = null;
		Map<String, Method> domainSetMethods = new HashMap<String, Method>();  //存储所有标有自定义注解的字段的值,key->标注value
		T domain = null;
		try {
			clazz.newInstance();
			Object value = null;
			String annotationValue = null;

			for (Field field : domainFields) {
				annotations = field.getAnnotations(); //获取字段上的所有注解
				for (Annotation annotation : annotations) {
					if (annotation instanceof PropertyMapping) { //判断注解是否为自定义类型的
						annotationValue = ((PropertyMapping) annotation)
								.value();
						pd = new PropertyDescriptor(field.getName(), clazz);
						setMethod = pd.getWriteMethod(); //获取字段的set方法
						domainSetMethods.put(annotationValue, setMethod);
					}
				}
			}

			for (Field field : dtoFields) {
				if (field.getName() == "serialVersionUID") {
					continue;
				}
				pd = new PropertyDescriptor(field.getName(), dto.getClass());
				getMethod = pd.getReadMethod(); //获取字段的get方法
				setMethod = domainSetMethods.get(field.getName());
				if (setMethod != null) {
					value = getMethod.invoke(dto);
					setMethod.invoke(domain, value);
				}
			}
		} catch (Exception e) {
			throw new CommonCoreException(e);
		}
		
		return domain;
	}
	
	/**
	 * <p>Discription:[List<domain>转List<DTO>]</p>
	 * Created on 2015年2月3日
	 * @param domainList
	 * @param clazz
	 * @return
	 * @throws Exception
	 * @author:[胡恒心]
	 */
	@SuppressWarnings("unchecked")
	public static <T, E> List<T> transDomainToDTOList(List<E> domainList, Class<T> clazz) {
		List<T> dtoList = new ArrayList<T>();
		List<Object> list = (List<Object>) domainList;
		
		for (Object domain : list) {
			dtoList.add(transDomainToDTO(clazz, domain));
		}
		
		return dtoList;
	}

}
