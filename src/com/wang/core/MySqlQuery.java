package com.wang.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.PerConnectionLRUFactory;
import com.wang.bean.ColumnInfo;
import com.wang.bean.TableInfo;
import com.wang.po.Emp_info;
import com.wang.utils.JDBCUtils;
import com.wang.utils.ReflectUtils;
import com.wang.utils.StringUtils;

/**
 * 负责针对MySql数据库的查询
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
@SuppressWarnings("all")
public class MySqlQuery implements Query {
	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;

	public static void main(String[] args) {
		Emp_info emp_info = new Emp_info();
		emp_info.setId(4);
		emp_info.setEname("WANGQING1");
		emp_info.setAge(20);
		emp_info.setHireDate(new Date(System.currentTimeMillis()));
		emp_info.setSalary(8000.0);
		new MySqlQuery().update(emp_info, new String[]{"age"});
	}

	@Override
	public int excuteDML(String sql, Object[] params) {

		connection = DBManager.getConnection();
		int count = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			// 设参数
			JDBCUtils.handleParams(preparedStatement, params);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			DBManager.close(resultSet, preparedStatement, connection);
		}
	}

	@Override
	public void insert(Object object) {
		// object -->表中 insert into 表名（id,name) values(?,?,?)
		Class cls = object.getClass();
		// 通过Class对象找到TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		List<Object> params = new ArrayList<>();// 存储参数对象
		StringBuilder sql = new StringBuilder();
		sql.append("insert into " + tableInfo.getTname() + " ( ");
		Field[] fs = cls.getDeclaredFields();
		int count = 0;
		for (Field f : fs) {
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, object);
			if (fieldValue != null) {
				count++;
				sql.append(fieldName + ",");
				params.add(fieldValue);
			}
		}
		sql.setCharAt(sql.length() - 1, ')');
		sql.append(" values (");
		for (int i = 0; i < count; i++) {
			sql.append("?,");
		}
		sql.setCharAt(sql.length() - 1, ')');
		// sql.append(";");
		excuteDML(sql.toString(), params.toArray());
	}

	@Override
	public int delete(Class cls, Object id) {
		// 根据class对象和id值删除对应表中的数据 Emp.class,2-->delete from emp where id = 2;

		// 通过Class对象找到TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// 主键
		String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " = ?";

		return excuteDML(sql, new Object[] { id });
	}

	@Override
	public int delete(Object object) {
		Class cls = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// 主键
		// 通过反射机制获取主键值
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(), object);
		return delete(cls, priKeyValue);

	}

	@Override
	public int update(Object object, String[] fieldNames) {
		//object{"username","pwd"}-->update 表名 set username = ?,pwd = ? wherer id = ?
		
		Class cls = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		List<Object> params = new ArrayList<>();// 存储参数对象
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// 主键
		StringBuilder sql = new StringBuilder();
		sql.append("update " + tableInfo.getTname() + " set ");
		
		for(String fname:fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(fname, object);
			params.add(fvalue);
			sql.append(fname+"=?,");
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append("where "+onlyPriKey.getName()+"=?;");
		//将主键对应的值也放入列表中
		params.add(ReflectUtils.invokeGet(onlyPriKey.getName(), object));

		return excuteDML(sql.toString(), params.toArray());
	}

	@Override
	public List queryRows(String sql, Class clas, Object[] params) {

		return null;
	}

	@Override
	public Object queryUniqueRow(String sql, Class clas, Object[] params) {

		return null;
	}

	@Override
	public Object queryValue(String sql, Object[] params) {

		return null;
	}

	@Override
	public Number queryNumber(String sql, Object[] params) {

		return null;
	}

}
