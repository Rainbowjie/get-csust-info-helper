package com.yinbro.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ProxyIPCacher {
	
	
	//getIPMap: ip --> port
			public static HashMap<String, Integer> getIPSet() throws Exception, IOException {
				 System.out.println("searching proxy from the Internet...");
				
				StringBuffer sbHtml = new StringBuffer();
				//how many pages to get
				int intPageofIP = 4;
				for(int i=1;i<=intPageofIP;i++){
					CloseableHttpClient httpclient = HttpClients.createDefault();  
			        HttpGet get = new HttpGet("http://www.proxy.com.ru/list_"+ i +".html");
			        CloseableHttpResponse response = httpclient.execute(get);
			        HttpEntity responseEntity = response.getEntity();
			        InputStream in = responseEntity.getContent();
			        
			        sbHtml.append(getStrFromInstream(in));
			        
			        in.close();
			        response.close();
			        httpclient.close();
				}
		        
		        //test ip method
		        HashMap<String , Integer > ipMap = matchIPfromHtml(sbHtml.toString());
		        System.out.println(ipMap.size() + "  proxy are ready ");
		        return ipMap;
			}

			//match IP_Map from html
			public static HashMap<String, Integer> matchIPfromHtml(String html) {
				String regex =   "((25[0-5]|2[0-4]\\d|1?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1?\\d?\\d)</td><td>\\d*";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(html);
				HashMap<String , Integer > ipMap = new HashMap<>();
				String []ipLine;
				while(matcher.find()){
					ipLine = matcher.group().split("</td><td>");
					ipMap.put(ipLine[0], Integer.parseInt(ipLine[1]));
				}
				
				return ipMap;
				
			}
			//InputSteam --> String
			public static String getStrFromInstream(InputStream inputstream)
					throws UnsupportedEncodingException, IOException {
				StringBuffer sb = new StringBuffer();
				InputStreamReader inReader = new InputStreamReader(inputstream,"UTF-8");
		        BufferedReader buffReader = new BufferedReader(inReader);
		        
		        String line;
		        while ((line = buffReader.readLine()) != null){
		        	sb.append(line);
		        }
		        return sb.toString();
			}

}
