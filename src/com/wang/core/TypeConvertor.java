package com.wang.core;

/**
 * --负责java数据类型和数据库数据类型的互相转换
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public interface TypeConvertor {

	/**
	 * 	将数据库数据类型转换为Java数据类型
	 * @param columnType 数据库数据类型
	 * @return Java数据类型
	 */
	public String databaseType2JavaType(String columnType);
	
	/**
	 * 	将Java数据类型转换为数据库数据类型
	 * @param javaDataType Java数据类型
	 * @return 数据库数据类型
	 */
	public String JavaType2databaseType(String javaDataType);
}
