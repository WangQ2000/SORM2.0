package com.wang.bean;

/**
 * 封装配置文件(xml)信息
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class Configuration {

	/**
	 * 正在使用的数据库
	 */
	private String usingDB;
	/**
	 * 驱动类
	 */
	private String classname;
	/**
	 * JDBC的url
	 */
	private String url;
	/**
	 * 数据库用户名
	 */
	private String user;
	/**
	 * 数据库密码
	 */
	private String password;
	/**
	 * 项目的源码路径 
	 */
	private String src_path;
	/**
	 * 扫描生成Java类的包（po：Persistence object 持久化对象）
	 */
	private String po_package;

	public Configuration() {
		super();
	}

	public Configuration(String usingDB, String classname, String url, String user, String password, String src_path,
			String po_package) {
		super();
		this.usingDB = usingDB;
		this.classname = classname;
		this.url = url;
		this.user = user;
		this.password = password;
		this.src_path = src_path;
		this.po_package = po_package;
	}

	public String getUsingDB() {
		return usingDB;
	}

	public void setUsingDB(String usingDB) {
		this.usingDB = usingDB;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSrc_path() {
		return src_path;
	}

	public void setSrc_path(String src_path) {
		this.src_path = src_path;
	}

	public String getPo_package() {
		return po_package;
	}

	public void setPo_package(String po_package) {
		this.po_package = po_package;
	}

}
