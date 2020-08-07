package com.wang.resources;

/**
 * 	模拟资源文件
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class Resources {
	
	private final static String CALSSNAME = "com.mysql.jdbc.Driver";
	private final static String URL = "jdbc:mysql://localhost:3306/emp_db";
	private final static String USER = "root";
	private final static String PASSWORD = "wang";

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

}
