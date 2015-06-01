package com.yinbro.wx.pojo;

public class LessonNews {
	private String title;
	private String time;
	private String content;
	private String nid;
	private String poster;
	private boolean isRead = true;
	
	
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	@Override
	public String toString() {
		return "课程提醒 [标题=" + title + ", 时间=" + time + ", 内容="
				+ content + ", 发布人=" + poster + ", 是否已读=" + isRead + "]";
	}
	
	
}
