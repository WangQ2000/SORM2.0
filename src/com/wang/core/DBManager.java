package com.wang.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.wang.bean.Configuration;
import com.wang.resources.Resources;

/**
 * 根据配置信息，维持连接对象的管理（增加连接池）
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class DBManager {
	private static Configuration conf;

	static {// 静态代码块，第一次加载类时执行
		conf = new Configuration();
		conf.setClassname(Resources.getCalssname());
		conf.setUrl(Resources.getUrl());
		conf.setUser(Resources.getUser());
		conf.setPassword(Resources.getPassword());
		conf.setUsingDB(Resources.getUsingdb());
		conf.setSrc_path(Resources.getSrcPath());
		conf.setPo_package(Resources.getPoPackage());
	}

	// 获取连接
	public static Connection getConnection() {
		Connection connection = null;
		try {
			// 加载驱动类
			Class.forName(conf.getClassname());
			// 建立连接
			connection = DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	// 关闭服务
	public static void close(ResultSet rs, Statement ps, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Configuration getConf() {
		return conf;
	}

}
