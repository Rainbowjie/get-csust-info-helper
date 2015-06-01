package com.yinbro.libsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class BookStoreDetail {
	
	public static void main(String[] args) {
		new BookStoreDetail().getStoreInfo();
	}
	
	private void getStoreInfo() {
		String strLibUrl = "http://210.43.188.8:8088/opac/jdjsjg.jsp?kzh=";
		String strkzh = "zyk6036545";
		StringBuffer rsHtml = new StringBuffer();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(strLibUrl);
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStreamReader reader = new InputStreamReader(entity.getContent(),"gb2312");
			BufferedReader buffReader = new BufferedReader(reader);
			String line;
			while((line = buffReader.readLine())!=null){
				rsHtml.append(line);
			}
			System.out.println(rsHtml.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
