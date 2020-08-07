package com.wang.bean;

import javax.sound.midi.Soundbank;

/**
 * 封装java属性的get、set方法
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
@SuppressWarnings("all")
public class JavaFieldGetSet {
	/**
	 * 属性的源码信息如：private int userId;
	 */
	private String fieldInfo;
	/**
	 * get方法的源码信息，如：public int getUserId(){}
	 */
	private String getInfo;
	/**
	 * set方法的源码信息，如：public void setUserId(int userId){}
	 */
	private String setInfo;

	public String getFieldInfo() {
		return fieldInfo;
	}

	public void setFieldInfo(String fieldInfo) {
		this.fieldInfo = fieldInfo;
	}

	public String getGetInfo() {
		return getInfo;
	}

	public void setGetInfo(String getInfo) {
		this.getInfo = getInfo;
	}

	public String getSetInfo() {
		return setInfo;
	}

	public void setSetInfo(String setInfo) {
		this.setInfo = setInfo;
	}

	public JavaFieldGetSet() {
		super();
	}

	public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
		super();
		this.fieldInfo = fieldInfo;
		this.getInfo = getInfo;
		this.setInfo = setInfo;
	}
	
	@Override
	public String toString() {
		System.out.println(fieldInfo);
		System.out.println(getInfo);
		System.out.println(setInfo);
		return super.toString();
	}

}
