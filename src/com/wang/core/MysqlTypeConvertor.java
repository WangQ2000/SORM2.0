package com.wang.core;

/**
 * mysql数据库的类型转换器
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class MysqlTypeConvertor implements TypeConvertor {

	@Override
	public String databaseType2JavaType(String columnType) {

		// varchar-->String
		
		if ("varchar".equalsIgnoreCase(columnType) || "char".equalsIgnoreCase(columnType)) {
			return "String";
		} else if ("int".equalsIgnoreCase(columnType) || "tinyint".equalsIgnoreCase(columnType)
				|| "smallint".equalsIgnoreCase(columnType) || "integer".equalsIgnoreCase(columnType)) {
			return "Integer";
		} else if ("bigint".equalsIgnoreCase(columnType)) {
			return "Long";
		} else if ("double".equalsIgnoreCase(columnType)) {
			return "Double";
		} else if ("float".equalsIgnoreCase(columnType)) {
			return "Float";
		} else if ("clob".equalsIgnoreCase(columnType)) {
			return "java.sql.Clob";
		} else if ("blob".equalsIgnoreCase(columnType)) {
			return "java.sql.Blob";
		} else if ("date".equalsIgnoreCase(columnType)) {
			return "java.sql.Date";
		} else if ("time".equalsIgnoreCase(columnType)) {
			return "java.sql.Time";
		} else if ("timestamp".equalsIgnoreCase(columnType)) {
			return "java.sql.Timestamp";
		}
		return null;
	}

	@Override
	public String JavaType2databaseType(String javaDataType) {

		return null;
	}

}
