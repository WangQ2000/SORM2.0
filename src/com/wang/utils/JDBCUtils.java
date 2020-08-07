package com.wang.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.wang.resources.Resources;

/**
 * JDBCUtils封装常用的JDBC操作
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class JDBCUtils {
	// 获取连接
	public static Connection getConnection() {
		Connection connection = null;
		try {
			// 加载驱动类
			Class.forName(Resources.getCalssname());
			// 建立连接
			connection = DriverManager.getConnection(Resources.getUrl(), Resources.getUser(), Resources.getPassword());
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
}
