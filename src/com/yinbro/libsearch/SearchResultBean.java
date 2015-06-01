package com.yinbro.libsearch;

import java.util.ArrayList;

public class SearchResultBean {
	//搜索用时
	private double longTomeCost = 0;
	
	//搜索结果数量
	private int intRsCount = 0;
	
	private int page = 1;
	
	//搜索结果的BookBean集合
	private ArrayList<BookBean> arrayBookBeans = new ArrayList<BookBean>();

	

	//无参构造方法
	public SearchResultBean() {

	}

	//getter and setter
	public double getLongTomeCost() {
		return longTomeCost;
	}


	public void setLongTomeCost(double longTomeCost) {
		this.longTomeCost = longTomeCost;
	}


	public int getIntRsCount() {
		return intRsCount;
	}


	public void setIntRsCount(int intRsCount) {
		this.intRsCount = intRsCount;
	}


	public ArrayList<BookBean> getArrayBookBeans() {
		return arrayBookBeans;
	}


	public void setArrayBookBeans(ArrayList<BookBean> arrayBookBeans) {
		this.arrayBookBeans = arrayBookBeans;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
	//for WX
	public String toString() {
		return 	  "\n 搜索用时:" + longTomeCost
				+ "\n 匹配条数:" + intRsCount;
	}

//	@Override
//	public String toString() {
//		return "SearchResultBean [longTomeCost=" + longTomeCost
//				+ ", intRsCount=" + intRsCount + ", page=" + page
//				+ ", arrayBookBeans=" + arrayBookBeans + "]";
//	}
//	
	
	
}
