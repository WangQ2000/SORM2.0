package com.wang.po;

public class Emp_info{

	private String ename;
	private java.sql.Date hireDate;
	private Integer deptid;
	private Integer id;
	private Double salary;
	private Integer age;


	public String getEname(){
		return ename;
	}
	public java.sql.Date getHireDate(){
		return hireDate;
	}
	public Integer getDeptid(){
		return deptid;
	}
	public Integer getId(){
		return id;
	}
	public Double getSalary(){
		return salary;
	}
	public Integer getAge(){
		return age;
	}
	public void setEname(String ename){
		this.ename = ename;
	}
	public void setHireDate(java.sql.Date hireDate){
		this.hireDate = hireDate;
	}
	public void setDeptid(Integer deptid){
		this.deptid = deptid;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public void setSalary(Double salary){
		this.salary = salary;
	}
	public void setAge(Integer age){
		this.age = age;
	}
}
