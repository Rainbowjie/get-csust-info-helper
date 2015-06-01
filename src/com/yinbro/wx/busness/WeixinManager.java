package com.yinbro.wx.busness;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.yinbro.wx.pojo.ButtonBean;
import com.yinbro.wx.pojo.MenuBean;

public class WeixinManager {
	//微信开发者口令
	private static String APPID = "wx7c072d7efed2ed25";
	private static String APPSECRET = "869cb72db6b2096a4a6c3f083a8744b8";
	
	//获取access_token
	//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
	/**
	 * 
	 * @param APPID
	 * @param APPSECRET
	 * @return
	 */
	public static String getAccessToken(String APPID,String APPSECRET){
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET;
		String accessToken = null;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(get);
			HttpEntity enity = response.getEntity();
			InputStream in = enity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            
            JSONObject j = (JSONObject) JSONSerializer.toJSON(sb.toString());
            if (j.containsKey("access_token")) {
            	accessToken = j.getString("access_token");
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
		
		
		return accessToken;
	}
	
	/**
	 * 
	 */
	public static void createMenu(){
		
		//封装按钮参数，后续自定义
		ButtonBean button1 = new ButtonBean();
		button1.setKey("#");
		button1.setName("按鈕A");
		button1.setType("click");
		
		ButtonBean button2 = new ButtonBean();
		button2.setKey("#");
		button2.setName("按鈕2");
		button2.setType("click");
		
		List<ButtonBean> list = new ArrayList<>();
		list.add(button1);
		list.add(button2);
		
		MenuBean menueBean = new MenuBean();
		menueBean.setButton(list);
		JSONObject j = (JSONObject) JSONSerializer.toJSON(menueBean);
		
		System.out.println(j.toString());
		
		
		
		//开始连接微信
		String accessToken = getAccessToken(APPID, APPSECRET);
		if(accessToken==null)
			return;
		
		String urlStr = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
		CloseableHttpClient client = HttpClients.createDefault();
		
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String s = getAccessToken(APPID, APPSECRET);
		System.out.println(s);
		createMenu();
		
	}
}
