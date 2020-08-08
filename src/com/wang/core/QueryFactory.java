package com.wang.core;

/**
 * QueryFactory�࣬�������������Ϣ����query����
 * 
 * @author wangQ
 *
 * @date 2020-8-8
 */
public class QueryFactory {
	//��̬����
	private static Query prototypeObj;//ԭ�Ͷ���
	//��̬����
	static {
		 try {
			 Class clas = Class.forName(DBManager.getConf().getQuery_class());
			 prototypeObj = (Query) clas.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//˽�й�����
	private QueryFactory() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static Query createQuery() {
		try {
			//return (Query) clas.newInstance();//���ܻή��Ч��
			//���ÿ�¡��ȡ
			return (Query) prototypeObj.clone();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
