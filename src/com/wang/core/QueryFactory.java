package com.wang.core;

/**
 * QueryFactory类，负责根据配置信息创建query对象
 * 
 * @author wangQ
 *
 * @date 2020-8-8
 */
public class QueryFactory {
	//静态属性
	private static Query prototypeObj;//原型对象
	//静态语句块
	static {
		 try {
			 Class clas = Class.forName(DBManager.getConf().getQuery_class());
			 prototypeObj = (Query) clas.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//私有构造器
	private QueryFactory() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static Query createQuery() {
		try {
			//return (Query) clas.newInstance();//可能会降低效率
			//采用克隆获取
			return (Query) prototypeObj.clone();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
