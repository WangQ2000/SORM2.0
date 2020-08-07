package com.wang.core;

import java.lang.reflect.Method;
import java.util.List;

import com.wang.bean.ColumnInfo;
import com.wang.bean.TableInfo;
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

	@Override
	public int excuteDML(String sql, Object[] params) {

		return 0;
	}

	@Override
	public void insert(Object object) {

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
		//通过反射机制获取主键值
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(), object);
		return delete(cls,priKeyValue);
		
	}

	@Override
	public int update(Object object, String[] fieldNames) {

		return 0;
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
