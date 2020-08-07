package com.wang.utils;

import java.lang.reflect.Method;

/**
 * ReflectUtils封装常用反射操作
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class ReflectUtils {

	/**
	 * 调用obj对象对应属性fieldname的get方法
	 * @param fieldName
	 * @param object
	 * @return
	 */
	public static Object invokeGet(String fieldName, Object object) {
		try {
			Class cls = object.getClass();
			Method method = cls.getMethod("get" + StringUtils.firstChar2UpperCase(fieldName), null);
			return method.invoke(object, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
