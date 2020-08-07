package com.wang.bean;

import java.util.List;
import java.util.Map;

/**
 * --封装一张表结构的信息
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class TableInfo {

	/**
	 * 表名
	 */
	private String tname;
	/**
	 * 所有字段信息
	 */
	private Map<String, ColumnInfo> columns;
	/**
	 * 唯一主键（目前只支持单一主键）
	 */
	private ColumnInfo onlyPriKey;
	/**
	 * 	存储联合主键
	 */
	private List<ColumnInfo> priKeys;

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}

	public ColumnInfo getOnlyPriKey() {
		return onlyPriKey;
	}

	public void setOnlyPriKey(ColumnInfo onlyPriKey) {
		this.onlyPriKey = onlyPriKey;
	}

	public List<ColumnInfo> getPriKeys() {
		return priKeys;
	}

	public void setPriKeys(List<ColumnInfo> priKeys) {
		this.priKeys = priKeys;
	}

	public TableInfo() {
		super();
	}

	public TableInfo(String tname, Map<String, ColumnInfo> columns, ColumnInfo onlyPriKey) {
		super();
		this.tname = tname;
		this.columns = columns;
		this.onlyPriKey = onlyPriKey;
	}

	public TableInfo(String tname, Map<String, ColumnInfo> columns, List<ColumnInfo> priKeys) {
		super();
		this.tname = tname;
		this.columns = columns;
		this.priKeys = priKeys;
	}

}
