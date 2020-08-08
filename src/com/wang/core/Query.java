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
 * Query���࣬�����ѯ�������ṩ����ĺ����ࣩ �ṩmysql��oracle��ͬ�ķ���
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
		// ֱ�ӵ���object�����clone()����
		return super.clone();
	}

	/**
	 * ֱ��ִ��һ��DML���
	 * 
	 * @param sql    sql���
	 * @param params ��Ӧ����
	 * @return ִ�����Ӱ��ļ�¼����
	 */
	public int excuteDML(String sql, Object[] params) {
		connection = DBManager.getConnection();
		int count = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			// �����
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
	 * ������洢�����ݿ��� �Ѷ����в�Ϊnull���������ݴ洢�����ݿ⣬�������Ϊnull��洢0
	 * 
	 * @param object Ҫ�洢�Ķ���
	 */
	public void insert(Object object) {
		// object -->���� insert into ������id,name) values(?,?,?)
		Class cls = object.getClass();
		// ͨ��Class�����ҵ�TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		List<Object> params = new ArrayList<>();// �洢��������
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
	 * ɾ��cls��ʾ���Ӧ�ı��еļ�¼��ָ������id��ֵ��
	 * 
	 * @param cls �����Ӧ�����Class����
	 * @param id  ����ֵ
	 * @return ִ�����Ӱ��ļ�¼����
	 */
	public int delete(Class cls, Object id) {
		// ����class�����idֵɾ����Ӧ���е����� Emp.class,2-->delete from emp where id = 2;

		// ͨ��Class�����ҵ�TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// ����
		String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + " = ?";

		return excuteDML(sql, new Object[] { id });
	}// delete from cls where id = id;

	/**
	 * ɾ�����������ݿ��ж�Ӧ�ļ�¼�����Ӧ��������ֵ��Ӧ����¼��
	 * 
	 * @param object
	 * @return ִ�����Ӱ��ļ�¼����
	 */
	public int delete(Object object) {
		Class cls = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// ����
		// ͨ��������ƻ�ȡ����ֵ
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(), object);
		return delete(cls, priKeyValue);
	}

	/**
	 * ���¶����Ӧ�ļ�¼��ֻ���¶�Ӧ���ֶ�ֵ
	 * 
	 * @param object     Ҫ���µĶ���
	 * @param fieldNames ��Ҫ���µ������б�
	 * @return ִ�����Ӱ��ļ�¼����
	 */
	public int update(Object object, String[] fieldNames) {
		// object{"username","pwd"}-->update ���� set username = ?,pwd = ? wherer id = ?

		Class cls = object.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(cls);
		List<Object> params = new ArrayList<>();// �洢��������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();// ����
		StringBuilder sql = new StringBuilder();
		sql.append("update " + tableInfo.getTname() + " set ");

		for (String fname : fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(fname, object);
			params.add(fvalue);
			sql.append(fname + "=?,");
		}
		sql.setCharAt(sql.length() - 1, ' ');
		sql.append("where " + onlyPriKey.getName() + "=?;");
		// ��������Ӧ��ֵҲ�����б���
		params.add(ReflectUtils.invokeGet(onlyPriKey.getName(), object));

		return excuteDML(sql.toString(), params.toArray());
	}

	/**
	 * ����ģ�巽����JDBC�������з�װ����������
	 * 
	 * @param sql      sql���
	 * @param params   ����
	 * @param clas     ��¼Ҫ��װ����java��
	 * @param callBack CallBack��ʵ���࣬ʵ�ֻص�
	 * @return ���ز�ѯ���Ķ���
	 */
	public Object excuteQueryTemplate(String sql, Object[] params, Class clas, CallBack callBack) {
		connection = DBManager.getConnection();
		List list = null;// ��Ų�ѯ���
		try {
			preparedStatement = connection.prepareStatement(sql);
			// �����
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
	 * ��ѯ���ض��м�¼������ÿ�м�¼��װ��clasָ������Ķ�����
	 * 
	 * @param sql    sql���
	 * @param clas   ��װ���ݵ�JavaBean���Class����
	 * @param params ��Ӧ����
	 * @return ��ѯ���Ľ��
	 */
	public List queryRows(String sql, Class clas, Object[] params) {

		return (List) excuteQueryTemplate(sql, params, clas, new CallBack() {

			@Override
			public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
				ResultSetMetaData metaData;
				List list = null;// ��Ų�ѯ���
				try {
					metaData = (ResultSetMetaData) resultSet.getMetaData();
					while (resultSet.next()) {// ����
						Object rowObject = clas.newInstance();// �����޲ι��췽��
						if (list == null) {
							list = new ArrayList<>();
						}
						for (int i = 0; i < metaData.getColumnCount(); i++) {// ���� select username,pwd,age from user
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
	 * ��ѯ����һ�м�¼��������¼��װ��clasָ������Ķ�����
	 * 
	 * @param sql    sql���
	 * @param clas   ��װ���ݵ�JavaBean���Class����
	 * @param params ��Ӧ����
	 * @return ��ѯ���Ľ��
	 */
	public Object queryUniqueRow(String sql, Class clas, Object[] params) {
		List list = queryRows(sql, clas, params);
		return (list == null && list.size() <= 0) ? null : list.get(0);
	}

	/**
	 * ��ѯ����һ��ֵ��һ��һ�У���������ֵ����
	 * 
	 * @param sql    sql���
	 * @param params ��Ӧ����
	 * @return ��ѯ���Ľ��
	 */
	public Object queryValue(String sql, Object[] params) {
		return excuteQueryTemplate(sql, params, null, new CallBack() {

			@Override
			public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

				Object value = null;
				try {
					while (resultSet.next()) {// ����
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
	 * ��ѯ����һ��ֵ��һ��һ�У���������ֵ����
	 * 
	 * @param sql    sql���
	 * @param params ��Ӧ����
	 * @return ��ѯ��������(int,double,float,byte,long)
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number) queryValue(sql, params);
	}

	/**
	 * ��ҳ���ѯ
	 * 
	 * @param pageNum �ڼ�ҳ
	 * @param size    ÿҳ��ʾ�ļ�¼��
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum, int size);

}
