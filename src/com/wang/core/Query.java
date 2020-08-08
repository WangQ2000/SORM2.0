package com.wang.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.ResultSetMetaData;
import com.wang.bean.ColumnInfo;
import com.wang.bean.TableInfo;
import com.wang.utils.JDBCUtils;
import com.wang.utils.ReflectUtils;

/**
 * Query父类，负责查询（对外提供服务的核心类） 提供mysql和oracle共同的方法
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */

@SuppressWarnings("all")
public abstract class Query implements Cloneable {

	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// 直接调用object对象的clone()方法
		return super.clone();
	}

	/**
	 * 直接执行一个DML语句
	 * 
	 * @param sql    sql语句
	 * @param params 对应参数
	 * @return 执行语句影响的记录行数
	 */
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

	/**
	 * 将对象存储到数据库中 把对象中不为null的属性数据存储到数据库，如果数字为null则存储0
	 * 
	 * @param object 要存储的对象
	 */
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

	/**
	 * 删除cls表示类对应的表中的记录（指定主键id的值）
	 * 
	 * @param cls 跟表对应的类的Class对象
	 * @param id  主键值
	 * @return 执行语句影响的记录行数
	 */
	public int delete(Class cls, Object id) {
		// 根据class对象和id值删除对应表中的数据 Emp.class,2-->delete from emp where id = 2;

		// 通过Class对象找到TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// 主键
		String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " = ?";

		return excuteDML(sql, new Object[] { id });
	}// delete from cls where id = id;

	/**
	 * 删除对象在数据库中对应的记录（类对应到表，主键值对应到记录）
	 * 
	 * @param object
	 * @return 执行语句影响的记录行数
	 */
	public int delete(Object object) {
		Class cls = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// 主键
		// 通过反射机制获取主键值
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(), object);
		return delete(cls, priKeyValue);
	}

	/**
	 * 更新对象对应的记录，只更新对应的字段值
	 * 
	 * @param object     要更新的对象
	 * @param fieldNames 需要更新的属性列表
	 * @return 执行语句影响的记录行数
	 */
	public int update(Object object, String[] fieldNames) {
		// object{"username","pwd"}-->update 表名 set username = ?,pwd = ? wherer id = ?

		Class cls = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		List<Object> params = new ArrayList<>();// 存储参数对象
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// 主键
		StringBuilder sql = new StringBuilder();
		sql.append("update " + tableInfo.getTname() + " set ");

		for (String fname : fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(fname, object);
			params.add(fvalue);
			sql.append(fname + "=?,");
		}
		sql.setCharAt(sql.length() - 1, ' ');
		sql.append("where " + onlyPriKey.getName() + "=?;");
		// 将主键对应的值也放入列表中
		params.add(ReflectUtils.invokeGet(onlyPriKey.getName(), object));

		return excuteDML(sql.toString(), params.toArray());
	}

	/**
	 * 采用模板方法将JDBC操作进行封装，便于重用
	 * 
	 * @param sql      sql语句
	 * @param params   参数
	 * @param clas     记录要封装到的java类
	 * @param callBack CallBack的实现类，实现回调
	 * @return 返回查询到的对象
	 */
	public Object excuteQueryTemplate(String sql, Object[] params, Class clas, CallBack callBack) {
		connection = DBManager.getConnection();
		List list = null;// 存放查询结果
		try {
			preparedStatement = connection.prepareStatement(sql);
			// 设参数
			JDBCUtils.handleParams(preparedStatement, params);

			resultSet = preparedStatement.executeQuery();
			return callBack.doExecute(connection, preparedStatement, resultSet);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBManager.close(resultSet, preparedStatement, connection);
		}
	}

	/**
	 * 查询返回多行记录，并将每行记录封装到clas指定的类的对象中
	 * 
	 * @param sql    sql语句
	 * @param clas   封装数据的JavaBean类的Class对象
	 * @param params 对应参数
	 * @return 查询到的结果
	 */
	public List queryRows(String sql, Class clas, Object[] params) {

		return (List) excuteQueryTemplate(sql, params, clas, new CallBack() {

			@Override
			public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
				ResultSetMetaData metaData;
				List list = null;// 存放查询结果
				try {
					metaData = (ResultSetMetaData) resultSet.getMetaData();
					while (resultSet.next()) {// 多行
						Object rowObject = clas.newInstance();// 调用无参构造方法
						if (list == null) {
							list = new ArrayList<>();
						}
						for (int i = 0; i < metaData.getColumnCount(); i++) {// 多列 select username,pwd,age from user
																				// where id =
																				// ?
							String fieldName = metaData.getColumnLabel(i + 1);
							Object columnValue = resultSet.getObject(i + 1);
							ReflectUtils.invokeSet(rowObject, fieldName, columnValue);
						}
						list.add(rowObject);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
				return list;
			}
		});
	}

	/**
	 * 查询返回一行记录，并将记录封装到clas指定的类的对象中
	 * 
	 * @param sql    sql语句
	 * @param clas   封装数据的JavaBean类的Class对象
	 * @param params 对应参数
	 * @return 查询到的结果
	 */
	public Object queryUniqueRow(String sql, Class clas, Object[] params) {
		List list = queryRows(sql, clas, params);
		return (list == null && list.size() <= 0) ? null : list.get(0);
	}

	/**
	 * 查询返回一个值（一行一列），并将该值返回
	 * 
	 * @param sql    sql语句
	 * @param params 对应参数
	 * @return 查询到的结果
	 */
	public Object queryValue(String sql, Object[] params) {
		return excuteQueryTemplate(sql, params, null, new CallBack() {

			@Override
			public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

				Object value = null;
				try {
					while (resultSet.next()) {// 多行
						value = resultSet.getObject(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return value;
			}
		});
	}

	/**
	 * 查询返回一个值（一行一列），并将该值返回
	 * 
	 * @param sql    sql语句
	 * @param params 对应参数
	 * @return 查询到的数字(int,double,float,byte,long)
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number) queryValue(sql, params);
	}

	/**
	 * 分页拆查询
	 * 
	 * @param pageNum 第几页
	 * @param size    每页显示的记录数
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum, int size);

}
