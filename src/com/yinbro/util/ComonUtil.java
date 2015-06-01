package com.yinbro.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ComonUtil {
	

	/**
	 * 
	 * @param inputstream
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static String getStrFromInstream(InputStream inputstream)
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
