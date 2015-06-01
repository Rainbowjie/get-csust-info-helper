package com.yinbro.wx.pojo;

public class LessonBean {
	private String lessonID;
	private String lessonName;
	private String teacherName;
	private String deptName;
	
	public String getLessonID() {
		return lessonID;
	}
	public void setLessonID(String lessonID) {
		this.lessonID = lessonID;
	}
	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	@Override
	public String toString() {
		return "LessonBean [lessonID=" + lessonID + ", lessonName="
				+ lessonName + ", teacherName=" + teacherName + ", deptName="
				+ deptName + "]";
	}
	
	

}
