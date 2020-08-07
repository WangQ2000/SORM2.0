package com.wang.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.wang.bean.ColumnInfo;
import com.wang.bean.TableInfo;
import com.wang.utils.JavaFileUtiles;

/**
 * 负责获取管理数据库所有表结构和类结构的关系，并根据表结构生成类结构
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
@SuppressWarnings("all")
public class TableContext {

	/**
	 * 表名为key，表信息对象为value
	 */
	private static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();

	/**
	 * 将po的class对象和表信息关联起来，便于重用
	 */
	public static Map<Class, TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();

	private TableContext() {
	}

	static {
		try {
			Connection connection = DBManager.getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();

			ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });

			while (tableRet.next()) {
				String tablename = (String) tableRet.getObject("TABLE_NAME");

				TableInfo tiInfo = new TableInfo(tablename, new HashMap<String, ColumnInfo>(),
						new ArrayList<ColumnInfo>());
				tables.put(tablename, tiInfo);

				ResultSet set = dbmd.getColumns(null, "%", tablename, "%");
				while (set.next()) {
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), set.getString("TYPE_NAME"), 0);
					tiInfo.getColumns().put(set.getString("COLUMN_NAME"), ci);
				}

				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tablename);
				while (set2.next()) {
					ColumnInfo ci2 = tiInfo.getColumns().get(set2.getObject("COLUMN_NAME"));
					ci2.setKeyType(1);
					tiInfo.getPriKeys().add(ci2);
				}

				if (tiInfo.getPriKeys().size() > 0) {
					tiInfo.setOnlyPriKey(tiInfo.getPriKeys().get(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 更新类结构
		updateJavaPOFile();

	}

	/**
	 * 获取现有表结构
	 * 
	 * @return 所有的表结构
	 */
	public static Map<String, TableInfo> getTableInfos() {
		return tables;
	}

	/**
	 * 根据现有表结构更新po包下对应的Java类
	 */
	public static void updateJavaPOFile() {
		Map<String, TableInfo> map = getTableInfos();
		for (TableInfo t : map.values()) {
			JavaFileUtiles.createJavaPoFile(t, new MysqlTypeConvertor());
			System.out.println(t.getTname());
		}
	}
}
