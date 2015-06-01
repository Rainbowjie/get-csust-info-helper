package com.yinbro.wx.pojo;

public class StudentBean {
	private String stuNum;
	private String stuPass;
	private String sid;
	private String realName;
	private String totalOnlie;
	private int loginTime;
	private String gender;
	private String emial;
	private String department;
	private String major;
	private String grade;
	private String className;
	
	public String getStuPass() {
		return stuPass;
	}
	public void setStuPass(String stuPass) {
		this.stuPass = stuPass;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getStuNum() {
		return stuNum;
	}
	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getTotalOnlie() {
		return totalOnlie;
	}
	public void setTotalOnlie(String totalOnlie) {
		this.totalOnlie = totalOnlie;
	}
	public int getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(int loginTime) {
		this.loginTime = loginTime;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmial() {
		return emial;
	}
	public void setEmial(String emial) {
		this.emial = emial;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@Override
	public String toString() {
		return " 真实姓名:" + realName + "\n 学生学号:" + stuNum
				+ " \n 内部编号:" + sid + "\n 在线时长:" + totalOnlie
				+ "\n 登录次数:" + loginTime + "\n 学生性别:" + gender
				+ "\n 电子邮件:" + emial + "\n 学生学院:" + department
				+ "\n 学生专业:" + major + "\n 学生年级:" + grade + "\n 学生班级:" + className;
	}

	
	
	
	
}
