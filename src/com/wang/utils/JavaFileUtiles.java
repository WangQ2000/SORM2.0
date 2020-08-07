package com.wang.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sound.midi.Soundbank;
import javax.swing.text.TabableView;

import com.wang.bean.ColumnInfo;
import com.wang.bean.JavaFieldGetSet;
import com.wang.bean.TableInfo;
import com.wang.core.DBManager;
import com.wang.core.MysqlTypeConvertor;
import com.wang.core.TableContext;
import com.wang.core.TypeConvertor;

/**
 * JavaFileUtiles封装java文件(源代码)操作
 * 
 * @author wangQ
 *
 * @date 2020-8-7
 */
public class JavaFileUtiles {

	/**
	 * 根据字段信息生产java属性信息。如：varchar name -->private String name;以及相应的set/get方法
	 * 
	 * @param columnInfo    字段信息
	 * @param typeConvertor 类型转换器
	 * @return java属性的set/get方法源码
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo columnInfo, TypeConvertor typeConvertor) {
		JavaFieldGetSet jfgSet = new JavaFieldGetSet();

		String javaFieldType = typeConvertor.databaseType2JavaType(columnInfo.getDataType());
		// private String username;
		jfgSet.setFieldInfo("\tprivate " + javaFieldType + " " + columnInfo.getName() + ";\n");

		// public String getUsername(){return username;}
		StringBuilder getSrc = new StringBuilder();
		getSrc.append(
				"\tpublic " + javaFieldType + " get" + StringUtils.firstChar2UpperCase(columnInfo.getName()) + "(){\n");
		getSrc.append("\t\treturn " + columnInfo.getName() + ";\n");
		getSrc.append("\t}\n");
		jfgSet.setGetInfo(getSrc.toString());

		// public void setUsername(String username){this.username = username;}
		StringBuilder setSrc = new StringBuilder();
		setSrc.append("\tpublic void set" + StringUtils.firstChar2UpperCase(columnInfo.getName()) + "(");
		setSrc.append(javaFieldType + " " + columnInfo.getName() + "){\n");
		setSrc.append("\t\tthis." + columnInfo.getName() + " = " + columnInfo.getName() + ";\n");
		setSrc.append("\t}\n");
		jfgSet.setSetInfo(setSrc.toString());

		return jfgSet;
	}

	/**
	 * 根据表信息生成java类
	 * 
	 * @param tableInfo 表信息
	 * @param convertor 数据类型转化器
	 * @return java类的源码
	 */
	public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor) {

		Map<String, ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javaField = new ArrayList<JavaFieldGetSet>();

		for (ColumnInfo c : columns.values()) {
			javaField.add(createFieldGetSetSRC(c, convertor));
		}

		StringBuilder src = new StringBuilder();

		// 生成package语句
		src.append("package " + DBManager.getConf().getPo_package() + ";\n\n");
		// 生成import语句
		src.equals("import java.sql.*;\n");
		src.equals("import java.util.*;\n\n");
		// 生成类声明语句
		src.append("public class " + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + "{\n\n");
		// 生成属性列表
		for (JavaFieldGetSet f : javaField) {
			src.append(f.getFieldInfo());
		}
		src.append("\n\n");
		// 生成get方法列表
		for (JavaFieldGetSet f : javaField) {
			src.append(f.getGetInfo());
		}
		// 生成set方法列表
		for (JavaFieldGetSet f : javaField) {
			src.append(f.getSetInfo());
		}
		// 生成类结束
		src.append("}\n");

		return src.toString();
	}

	/**
	 * 生产java文件到相应的包中
	 * 
	 * @param tableInfo 表信息
	 * @param convertor 数据类型转化器
	 */
	public static void createJavaPoFile(TableInfo tableInfo, TypeConvertor convertor) {
		String src = createJavaSrc(tableInfo, convertor);

		String srcPath = DBManager.getConf().getSrc_path() + "/";
		String srcPackage = DBManager.getConf().getPo_package().replace(".", "/");
		// java文件名
		String srcFile = "/" + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + ".java";

		File f = new File(srcPath + srcPackage);

		if (!f.exists()) {
			f.mkdirs();// 目录不存在就新建
		}
		BufferedWriter bw = null;
		try {
			f = new File(f.getAbsolutePath() + srcFile);
			if (!f.exists()) {
				f.createNewFile();// 文件不存在就新建
			}
			bw = new BufferedWriter(new FileWriter(f));
			bw.write(src);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
