package com.yinbro.libsearch;

public class BookBean {

	String pubTime;
	String name;
	String isbn;
	String author;
	String publisher;
	String libCode;//藏书编号，用来查找书籍位置
	String libName;//图书书库名
	String kzh;
	
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getLibCode() {
		return libCode;
	}
	public void setLibCode(String libCode) {
		this.libCode = libCode;
	}
	public String getLibName() {
		return libName;
	}
	public void setLibName(String libName) {
		this.libName = libName;
	}
	public String getKzh() {
		return kzh;
	}
	public void setKzh(String kzh) {
		this.kzh = kzh;
	}

	

	@Override
	public String toString() {
		return "name=" + name + ", pubTime=" + pubTime + ", isbn="
				+ isbn + ", author=" + author + ", publisher=" + publisher
				+ ", libCode=" + libCode + ", libName=" + libName + ", kzh="
				+ kzh;
	}
	public BookBean() {
		super();
	}
	
	

}
