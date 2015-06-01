package com.yinbro.libsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//测试函数
public class LibSearcher {
	

	
	private static String getRsHtmlByBookName(String strBookName){
		//查询结果 html  string
		StringBuffer rsHtml = new StringBuffer();
		
		String strLibUrl = "http://210.43.188.8:8088/opac/jdjsjg.jsp";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(strLibUrl);
		//添加  基本命名值对 bnvp
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("jsc", strBookName));
		nameValuePairs.add(new BasicNameValuePair("ifface","true"));
		nameValuePairs.add(new BasicNameValuePair("jstj", "keyword2"));
		nameValuePairs.add(new BasicNameValuePair("sort", "datestr"));
		nameValuePairs.add(new BasicNameValuePair("orderby", "desc"));
		nameValuePairs.add(new BasicNameValuePair("geshi", "bgfm"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "gb2312"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStreamReader reader = new InputStreamReader(entity.getContent(),"gb2312");
			BufferedReader buffReader = new BufferedReader(reader);
			String line;
			while((line = buffReader.readLine())!=null){
				rsHtml.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rsHtml.toString();
	}
	

	//解析HTML文档，得到图书信息
	public static SearchResultBean getLibInfoByBookName(String bookName) {
		//封装解析到的图书信息到ArrayList 集合中
		SearchResultBean searchRsBean = new SearchResultBean();
		//图书信息集合
		ArrayList<BookBean> arrayBookBeans = new ArrayList<BookBean>();
		LibSearcher searcher = new LibSearcher();
		String rsHtml = LibSearcher.getRsHtmlByBookName(bookName);
		Document doc = Jsoup.parse(rsHtml);

		//获取搜索结果中，匹配结果的条数
		//获取属性来自于网页的特定元素
		searchRsBean.setIntRsCount(Integer.parseInt(doc.getElementsByAttributeValue("color", "#CC0000").first().text()));
		searchRsBean.setPage(Integer.parseInt(doc.getElementsByAttributeValue("color", "blue").first().text()));
		String tempStr = doc.getElementsMatchingOwnText("搜索用时").toString();
		Pattern patter = Pattern.compile("搜索用时\\d*\\.\\d*");
		Matcher matcher = patter.matcher(tempStr);
		//查询耗时
		double costTime = 0;
		while(matcher.find()){
			costTime =Double.parseDouble(matcher.group().substring(4)) ;
		}
		searchRsBean.setLongTomeCost(costTime);
		Elements nodes = doc.getElementsByAttributeValue("style", "padding-top:5px;padding-bottom:3px;");
		Iterator it = nodes.iterator();
		while(it.hasNext()){
			Element el = (Element) it.next();
			//单条记录的String
			Element tempEl = el.getElementsByTag("td").get(1);
			//书名
			String rsBookName = tempEl.getElementsByTag("a").text();
			
			//馆藏编码libCode
			Pattern ptLibCode = Pattern.compile("&nbsp;.*</b></span><br><br>");
			Matcher m = ptLibCode.matcher(tempEl.toString());
			m.find();
			String strLibCode = m.group();
			String libCode = strLibCode.substring(6,strLibCode.length()-20);
			
			//馆藏编码kzh
			Pattern ptKzh = Pattern.compile(".*");
			Matcher mKzh = ptKzh.matcher(tempEl.toString());
			mKzh.find();
			String strKzh = mKzh.group();
			String kzh = strKzh.substring(18,strKzh.length()-4);
//			System.out.println(kzh);
			
			//ISBN
			//TODO
			Pattern ptISBN = Pattern.compile("&nbsp;&nbsp;.*[&nbsp;]");
			Matcher mISBN = ptISBN.matcher(tempEl.toString());
			mISBN.find();
			String strISBN = mISBN.group();
			String ISBN = strISBN.substring(12,strISBN.length()-12);
			
			
			
			//作者+出版社+出版
			Pattern ptInfo = Pattern.compile("</b></span><br><br>.*</td>");
			Matcher mInfo = ptInfo.matcher(tempEl.toString());
			mInfo.find();
			String strInfo = mInfo.group();
			strInfo = strInfo.substring(19, strInfo.length()-5);
			String[]strArrayInfo = strInfo.split("&nbsp;&nbsp;");
			String author = strArrayInfo[0];
			String publisher = strArrayInfo[2];
			String pubTime = strArrayInfo[3];
			//将查询到的书对象封装到bookBean
			BookBean book = new BookBean();
			book.setAuthor(author);
			book.setIsbn(ISBN);
			book.setLibCode(libCode);
			book.setName(rsBookName);
			book.setPublisher(publisher);
			book.setPubTime(pubTime);
			book.setLibCode(libCode);
			arrayBookBeans.add(book);
//			System.out.println(book);
		}
		//跳出循环，将查到的结果书集合保存到searchRsBean
		searchRsBean.setArrayBookBeans(arrayBookBeans);
//		System.out.println(searchRsBean);
		return searchRsBean;
	}
	
}
