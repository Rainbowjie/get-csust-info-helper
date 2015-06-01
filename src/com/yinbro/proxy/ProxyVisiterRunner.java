package com.yinbro.proxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProxyVisiterRunner {
	
	//test
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		String begin = sdf.format(new Date().getTime());
		System.out.println("开始时间:"+ begin);
		start("http://www.jiguo.com/article/index/704.html");
		
		String end = sdf.format(new Date().getTime());
		System.out.println("结束时间："+ end);
	}
	
	
	public static void start(String strUrl){
		System.out.println("Go for "+ strUrl);
			try {
				int intThread = 20;
				HashMap<String, Integer> ipMap = ProxyIPCacher.getIPSet();
				// deliver ipMap into 5 parts
				ArrayList<HashMap<String, Integer>> ipMapArray = new ArrayList<>();
				// new arraylist contains 5 HashMap of ipset
				for(int i=0;i<intThread;i++){
					ipMapArray.add(new HashMap<String, Integer>());
				}
				
				int n =1;
				for(String ip:ipMap.keySet()){
					int port = ipMap.get(ip);
					ipMapArray.get((n++)%intThread).put(ip, port);
				}
				
				System.out.println(intThread + " working group is running...");
				
				for(int i=0;i<intThread;i++){
					System.out.println("Group "+ i+1 + ", size : "+ipMapArray.get(i).size());
					System.out.println("proxys:  "+ipMapArray.get(i).toString());
				}
				
				//5 threads 
				Thread[] doProxyThreads = new Thread[intThread];
				for(int ii = 0;ii<intThread;ii++){
					doProxyThreads[ii] = new ProxyVisiter(strUrl,ipMapArray.get(ii));
				}
				//check thread info:
				
				for(Thread t : doProxyThreads){
					t.start();
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
