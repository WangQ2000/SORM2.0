package com.wang.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBCUtils封装常用的JDBC操作
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class JDBCUtils {
	/**
	 * 给SQL语句设置参数
	 * @param preparedStatement
	 * @param params
	 */
	public static void handleParams(PreparedStatement preparedStatement,Object[] params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				try {
					preparedStatement.setObject(i + 1, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
