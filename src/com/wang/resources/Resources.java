package com.wang.resources;

/**
 * 模拟资源文件
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class Resources {

	private final static String USINGDB = "mysql";
	private final static String CALSSNAME = "com.mysql.jdbc.Driver";
	private final static String URL = "jdbc:mysql://localhost:3306/emp_db";
	private final static String USER = "root";
	private final static String PASSWORD = "wang";
	private final static String SRC_PATH = "E:/JAVA_MyCode/SORMpro/src";
	private final static String PO_PACKAGE = "com.wang.po";
	private final static String QUERY_CLASS = "com.wang.core.MySqlQuery";
	private static final int POOL_MAX_SIZE = 100;
	private static final int POOL_MIN_SIZE = 100;

	public static String getUsingdb() {
		return USINGDB;
	}

	public static String getCalssname() {
		return CALSSNAME;
	}

	public static String getUrl() {
		return URL;
	}

	public static String getUser() {
		return USER;
	}

	public static String getPassword() {
		return PASSWORD;
	}

	public static String getSrcPath() {
		return SRC_PATH;
	}

	public static String getPoPackage() {
		return PO_PACKAGE;
	}

	public static String getQueryClass() {
		return QUERY_CLASS;
	}

	public static int getPoolMaxSize() {
		return POOL_MAX_SIZE;
	}

	public static int getPoolMinSize() {
		return POOL_MIN_SIZE;
	}

}
