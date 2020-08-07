package com.wang.utils;

/**
 * StringUtils封装常用的字符串操作
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class StringUtils {
	/**
	 * 将目标字符串首字母大写
	 * 
	 * @param str 目标字符串
	 * @return 首字母大写的字符串
	 */
	public static String firstChar2UpperCase(String str) {

		return str.toUpperCase().substring(0, 1) + str.substring(1);
	}
}
