package com.wang.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wang.core.DBManager;

/**
 * 数据库连接池
 * 
 * @author wangQ
 *
 * @date 2020-8-8
 */
public class DBConnPool {
	/**
	 * 连接池
	 */
	private static List<Connection> pool = null;
	/**
	 * 连接池中连接数最大值
	 */
	private static final int POOL_MAX_SIZE = DBManager.getConf().getPOOL_MAX_SIZE();
	/**
	 * 连接池中连接数最小值
	 */
	private static final int POOL_MIN_SIZE = DBManager.getConf().getPOOL_MIN_SIZE();
	
	static {
		init();
	}
	
	/**
	 * 初始化连接池，使连接池的连接数达到最小值
	 */
	public static void init() {
		if(pool == null) {
			pool = new ArrayList<Connection>();
		}
		while(pool.size()<POOL_MIN_SIZE) {
			pool.add(DBManager.createConnection());
		}
	}
	/**
	 * 获取连接池中的连接对象
	 * @return
	 */
	public static synchronized Connection getConnection() {
		int last_index = pool.size()-1;
		Connection connection = null;
		if(last_index>=0) {
			connection = pool.get(last_index);
			pool.remove(last_index);
		}else {
			pool.add(DBManager.createConnection());
			connection = pool.get(0);
			pool.remove(0);
		}
		return connection;
	}
	/**
	 * 回收连接对象
	 * @param connection
	 */
	public static synchronized void closeConnection(Connection connection) {
		if(pool.size()==POOL_MAX_SIZE) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			pool.add(connection);
		}
	}
}
