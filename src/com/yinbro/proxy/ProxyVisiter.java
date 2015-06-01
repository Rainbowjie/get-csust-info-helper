package com.yinbro.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;


@SuppressWarnings("deprecation")
public class ProxyVisiter extends Thread {
	
	static Object objLock = new Object();
	
	HttpResponse response;
	String strSourceWeb;
	InputStream in;
	HttpEntity responseEntity;	
	String strUrl;
	HashMap<String, Integer> ipMap;
	//constructor
	public ProxyVisiter(String strUrl,HashMap<String, Integer> ipMap) { 
		this.strUrl = strUrl; 
		this.ipMap = ipMap;
	} 


	//do visit to URL
	@Override
	public void run(){
		HttpGet httpGet = new HttpGet(strUrl);
		
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"); 
        httpGet.addHeader("Host","user.qzone.qq.com");
        httpGet.addHeader("Referer","http://ic2.s8.qzone.qq.com/cgi-bin/feeds/feeds_html_module?i_uin=1240005669&i_login_uin=1240005669&mode=4&previewV8=1&style=21&version=8&needDelOpr=true&transparence=true&hideExtend=false&showcount=5&MORE_FEEDS_CGI=http%3A%2F%2Fic2.s8.qzone.qq.com%2Fcgi-bin%2Ffeeds%2Ffeeds_html_act_all&refer=2&paramstring=os-win7|100");
        httpGet.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"); 
        
		for(String ip:ipMap.keySet()){
			synchronized (objLock) {
				Counter.intTime++;
				System.out.println(Counter.intTime + " th "  + " is running..." );
			}
			int port = ipMap.get(ip);
			//already get the ip and port
			//own method to get proxyed httpClient
			HttpClient httpClient = getHttpClient(ip, port);
			try {
				response = httpClient.execute(httpGet);
			    responseEntity = response.getEntity();
			    in = responseEntity.getContent();
			    strSourceWeb = getStrFromInstream(in);
			    synchronized (objLock) {
			    	Counter.intSucsess++;	
				}
			} catch (Exception e) {
				synchronized (objLock) {
					Counter.intFailure++;
					System.out.println("***  failed to visit with this proxy  ***" );
				}
				
			}finally{
				//Source codes of the website
				synchronized (objLock) {
					System.out.println("Source codes:" );
					System.out.println( strSourceWeb );
					System.out.println("---------->   success: "+ Counter.intSucsess);
					System.out.println("---------->   failure: "+ Counter.intFailure);
					System.out.println();
					System.out.println();
				}
				
				try {
					in.close();
				} catch (Exception e) {
					// do nothing
				}
			}
		}
	}
    DefaultHttpClient httpClient = new DefaultHttpClient();
	//get Proxyed httpClient
	public HttpClient getHttpClient(String proxyHost, int proxyPort) {

		 //DefaultHttpClient 	 
		 String userName = "";
		 String password = "";
		 httpClient.getCredentialsProvider().setCredentials(
		   new AuthScope(proxyHost, proxyPort),
		   new UsernamePasswordCredentials(userName, password));
		 HttpHost proxy = new HttpHost(proxyHost,proxyPort);
		 httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		 return httpClient;
		}
		
	
	
	/*
	 * IP代理访问IP地址&&端口号*/
		public Proxy getProxy(String strHost,int intPort ) throws IOException{
			//给IP和端口号赋值
			InetSocketAddress isa = new InetSocketAddress(strHost,intPort);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, isa);
			return proxy;
		}
		
		
	
			
			//InputSteam --> String
			public String getStrFromInstream(InputStream inputstream)
					throws UnsupportedEncodingException, IOException {
				StringBuffer sb = new StringBuffer();
				InputStreamReader inReader = new InputStreamReader(inputstream,"GB2312");
		        BufferedReader buffReader = new BufferedReader(inReader);
		        
		        String line;
		        while ((line = buffReader.readLine()) != null){
		        	sb.append(line);
		        }
		        return sb.toString();
			}
	

	
}
