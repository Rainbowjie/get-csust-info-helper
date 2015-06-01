package com.yinbro.wx.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
	
	public static void appendLog(String s){
		File logFile = new File("./myLog.txt");
		SimpleDateFormat sdf = new SimpleDateFormat();
		String dateStr = sdf.format(new Date());
		try {
			//FileWriter(,TURE),第二个参数表示以追加的形式写入文档
			FileWriter writer = new FileWriter(logFile,true);
			writer.append("\n"+  dateStr +"\n");
			writer.append(s+"\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
		public static String readLog() throws IOException{
			File f = new File("./myLog.txt");
			FileReader reader = new FileReader(f);
			String s = null;
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(reader);
			while ((s=br.readLine())!=null) {
				sb.append(s);
			}
			return sb.toString();
			
		
	}
		
		
		public static void main(String[] args) throws IOException {
			appendLog("124231212S");
			System.out.println(readLog());
		}

}
