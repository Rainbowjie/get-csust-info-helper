package com.yinbro.proxy;

import java.util.ArrayList;
import java.util.HashMap;

public class ProxyVisiterWindowRunner {

//	public static void main(String[] args) {
//		String strUrl = "http://www.qikoo.com/try/paper?id=53e24002c84a8a2825000051";
//		doInWindow(strUrl);
//	}
	/**
	 * *
	 * @param strUrl
	 * @return -1 failure
	 * @return  n progress
	 */
	public static int doInWindow(String strUrl){
		
		System.out.println("Go for "+ strUrl);
			try {
				
				HashMap<String, Integer> ipMap = ProxyIPCacher.getIPSet();
				// deliver ipMap into 5 parts
				ArrayList<HashMap<String, Integer>> ipMapArray = new ArrayList<>();
				//new arraylist contains 5 HashMap of ipset
				for(int i=0;i<5;i++){
					ipMapArray.add(new HashMap<String, Integer>());
				}
				
				int n =1;
				for(String ip:ipMap.keySet()){
					int port = ipMap.get(ip);
					ipMapArray.get((n++)%5).put(ip, port);
				}
				
				System.out.println("5 working group is running...");
				for(int i=0;i<5;i++){
					System.out.println("Group "+ i+ ", size : "+ipMapArray.get(i).size());
					System.out.println("proxys:  "+ipMapArray.get(i).toString());
				}
				
				//5 threads 
				Thread[] doProxyThreads = {
						new ProxyVisiter(strUrl,ipMap),
						new ProxyVisiter(strUrl,ipMap),
						new ProxyVisiter(strUrl,ipMap),
						new ProxyVisiter(strUrl,ipMap),
						new ProxyVisiter(strUrl,ipMap)
				};
				//check thread info:
				
				for(Thread t : doProxyThreads){
					t.start();
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			return 1;
	}
}
