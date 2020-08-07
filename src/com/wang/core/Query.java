package com.wang.core;

import java.util.List;

/**
 * Query接口，负责查询（对外提供服务的核心类）
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */

@SuppressWarnings("all")
public interface Query {
	
	/**
	 * 	直接执行一个DML语句
	 * @param sql sql语句
	 * @param params 对应参数
	 * @return 执行语句影响的记录行数
	 */
	public int excuteDML(String sql,Object[] params);
	
	/**
	 * 	将对象存储到数据库中
	 * @param object 要存储的对象
	 */
	public void insert(Object object);
	
	/**
	 * 	删除cls表示类对应的表中的记录（指定主键id的值）
	 * @param cls 跟表对应的类的Class对象
	 * @param id 主键值
	 * @return 执行语句影响的记录行数
	 */
	public int delete(Class cls,Object id);//delete from cls where id = id;
	
	/**
	 * 	删除对象在数据库中对应的记录（类对应到表，主键值对应到记录）
	 * @param object
	 * @return 执行语句影响的记录行数
	 */
	public int delete(Object object);
	
	/**
	 *	 更新对象对应的记录，只更新对应的字段值
	 * @param object 要更新的对象
	 * @param fieldNames 需要更新的属性列表
	 * @return 执行语句影响的记录行数
	 */
	public int update(Object object,String[] fieldNames);
	
	/**
	 * 	查询返回多行记录，并将每行记录封装到clas指定的类的对象中
	 * @param sql sql语句
	 * @param clas 封装数据的JavaBean类的Class对象
	 * @param params 对应参数
	 * @return 查询到的结果
	 */
	public List queryRows(String sql,Class clas,Object[] params);
	
	/**
	 * 	查询返回一行记录，并将记录封装到clas指定的类的对象中
	 * @param sql sql语句
	 * @param clas 封装数据的JavaBean类的Class对象
	 * @param params 对应参数
	 * @return 查询到的结果
	 */
	public Object queryUniqueRow(String sql,Class clas,Object[] params);
	
	/**
	 * 	查询返回一个值（一行一列），并将该值返回
	 * @param sql sql语句
	 * @param params 对应参数
	 * @return 查询到的结果
	 */
	public Object queryValue(String sql,Object[] params);
	
	/**
	 * 	查询返回一个值（一行一列），并将该值返回
	 * @param sql sql语句
	 * @param params 对应参数
	 * @return 查询到的数字(int,double,float,byte,long)
	 */
	public Number queryNumber(String sql,Object[] params);
	
}
