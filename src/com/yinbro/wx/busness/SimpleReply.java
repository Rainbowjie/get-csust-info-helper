package com.yinbro.wx.busness;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;







import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.csust.pt.PtHandler;
import com.yinbro.libsearch.LibSearcher;
import com.yinbro.libsearch.SearchResultBean;
import com.yinbro.wx.pojo.LessonBean;
import com.yinbro.wx.pojo.StudentBean;
import com.yinbro.wx.util.WeixinMessageBean;

public class SimpleReply {
	
	
	
	/**
	 * 查询教学平台的平台信息*/
	public static WeixinMessageBean checkPT(WeixinMessageBean inBean){
		WeixinMessageBean outBean = new WeixinMessageBean();
		StringBuffer sbContent = new StringBuffer();
		outBean.setFromUserName(inBean.getFromUserName());
		outBean.setToUserName(inBean.getToUserName());
		outBean.setCreateTime((int)new Date().getTime());
		outBean.setMsgType(inBean.getMsgType());
		outBean.setMsgId(inBean.getMsgId());
		
		String reqContent = inBean.getContent();
		String[] strArray = reqContent.split("#");
		if(strArray.length!=2)
			return inBean;
		String nunStr = strArray[0];
		String passStr = strArray[1];
		
		PtHandler loger = null;
		StudentBean studentBean = null;
		ArrayList<LessonBean> lessonList = null;
		try {
			loger = new PtHandler(nunStr, passStr);
			studentBean = loger.getStudentInfo();
			lessonList = loger.getAllLesson();
			
			sbContent.append(studentBean.toString());
			sbContent.append("\n -----------\n");
			sbContent.append("***共有 " + lessonList.size() + " 门课程***\n");
			for(LessonBean lesson:lessonList){
				sbContent.append(lesson.getLessonName()+":"+lesson.getTeacherName()+"\n");
			}
			//查到的学生信息发送回微信用户
			outBean.setContent(sbContent.toString());
			lessonList.clear();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outBean;
	}
	
	
	/**
	 * 图书馆馆藏信息查询*/
	public static WeixinMessageBean checkLib(WeixinMessageBean inBean){
		WeixinMessageBean outBean = new WeixinMessageBean();
		StringBuffer sbContent = new StringBuffer();
		outBean.setFromUserName(inBean.getFromUserName());
		outBean.setToUserName(inBean.getToUserName());
		outBean.setCreateTime((int)new Date().getTime());
		outBean.setMsgType(inBean.getMsgType());
		outBean.setMsgId(inBean.getMsgId());
		String reqContent = inBean.getContent();
		reqContent = reqContent.substring(3);
		SearchResultBean searchRs = LibSearcher.getLibInfoByBookName(reqContent);
		if(searchRs ==null)
			outBean.setContent("查无此书！");
		else{
			sbContent.append("\n长沙理工大学馆藏查询:\n");
			sbContent.append("\n关键字:" + reqContent);
			sbContent.append(searchRs.toString());
			outBean.setContent(sbContent.toString());
		}
			
		return outBean;
	}
	
	

	
	/**
	 * ? 帮助文档*/
	public static WeixinMessageBean help(WeixinMessageBean inBean){
		WeixinMessageBean outBean = new WeixinMessageBean();
		StringBuffer sbContent = new StringBuffer();
		outBean.setFromUserName(inBean.getFromUserName());
		outBean.setToUserName(inBean.getToUserName());
		outBean.setCreateTime((int)new Date().getTime());
		outBean.setMsgType(inBean.getMsgType());
		outBean.setMsgId(inBean.getMsgId());

		//默认菜单！
		sbContent.append("感谢您帮助yinbro学习微信开发！");
		sbContent.append("\n\t\t现在实现的功能有~");
		sbContent.append("\n\t 1.长沙理工大学教学平台信息查询");
		sbContent.append("\n（学号#密码）");
		sbContent.append("\n\t 2.长沙理工大学图书馆藏信息查询");
		sbContent.append("\n（图书-书名，如“图书-货币战争”）");
		sbContent.append("\n\t 3.即时英译汉");
		sbContent.append("\n接入了第三方翻译API，通过“翻译-this a a simple test”之类的命令使用");
		sbContent.append("\n\t 4.简单图灵机器人");
		sbContent.append("\n接入了第三方机器人API，可以进行天气、百度、聊天等机器人功能，正常回复都将调用机器人");
	
		sbContent.append("\n\t\t我还在学习中，即将推出:");
		sbContent.append("\n\t 长沙理工大学教学平台作业、未读提醒等实用信息");
		sbContent.append("\n\t 长沙理工大学馆藏明细信息");
		sbContent.append("\n\t 机器人列车查询详细");
		
		sbContent.append("\n***2015年5月30日23:22:13");
		sbContent.append("\n\n有好玩的点子?Mail to: yinbro@foxmail.com");
		
		outBean.setContent(sbContent.toString());

			
		return outBean;
	}
	
	
	/**
	 * 图灵机器人回复*/
	public static WeixinMessageBean robotReply(WeixinMessageBean inBean){
		String apiKey = "6b5023bc558cf5bc61e7e8e5716a4a64";
		String apiUrl = "http://www.tuling123.com/openapi/api";
		WeixinMessageBean outBean = new WeixinMessageBean();
		StringBuffer sbContent = new StringBuffer();
		outBean.setFromUserName(inBean.getFromUserName());
		outBean.setToUserName(inBean.getToUserName());
		outBean.setCreateTime((int)new Date().getTime());
		outBean.setMsgType(inBean.getMsgType());
		outBean.setMsgId(inBean.getMsgId());
		String reqContent = inBean.getContent();
		
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		String result = null;
		try {
			String fullUrl = "http://www.tuling123.com/openapi/api?key="+apiKey+"&info="+reqContent;
//			http://www.tuling123.com/openapi/api?key=KEY&info=%E4%BD%A0%E6%BC%82%E4%BA%AE%E4%B9%88
            CloseableHttpClient httpclient = HttpClients.createDefault();  
            HttpGet get = new HttpGet(fullUrl);
            CloseableHttpResponse response = httpclient.execute(get);
            HttpEntity httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
            	
            	//JSONSerializer.toJava();
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            JSONObject jsonObject = (JSONObject)JSONSerializer.toJSON(result);
            String text = jsonObject.getString("text");
            
            System.out.println(result);
            if(jsonObject.containsKey("list"))
                 sbContent.append("说来话长，自己<a href='www.baidu.com'>百度</a>去吧");
            sbContent.append(text);
		}catch (Exception e){
			e.printStackTrace();
		}finally{
//			TODO
		}
		
		
		
		
		outBean.setContent(sbContent.toString());
			
		return outBean;
	}
	
	/**
     * @param urlAll
     *            :请求接口
     * @param httpArg
     *            :参数
     * @return 返回结果
     */
    public static WeixinMessageBean translate(WeixinMessageBean inBean) {
    	String httpUrl = "http://apis.baidu.com/apistore/tranlateservice/translate";
        WeixinMessageBean outBean = new WeixinMessageBean();
		StringBuffer sbContent = new StringBuffer();
		outBean.setFromUserName(inBean.getFromUserName());
		outBean.setToUserName(inBean.getToUserName());
		outBean.setCreateTime((int)new Date().getTime());
		outBean.setMsgType(inBean.getMsgType());
		outBean.setMsgId(inBean.getMsgId());
		String reqContent = inBean.getContent();
		reqContent = reqContent.substring(3);
		
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        String urlAll = httpUrl + "?query=" + reqContent+"&from=auto&to=auto";
//        System.out.println(urlAll);
        try {
            URL url = new URL(urlAll);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            // 填入HTTP header参数
            connection.setRequestProperty("apikey", "0c139d95715ac0485a4cf4f7724be785");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            System.out.println(result);
            
            //姑且认为在线翻译失败
            if(!result.contains("success")){
            	return inBean;
            }
            
           
            Pattern p = Pattern.compile("dst.*\"");
            Matcher m = p.matcher(result);
            m.find();
            String findRs = m.group();
            findRs = findRs.substring(6, findRs.length()-1);
            String s = decodeUnicode(findRs);
            
            
            
//            System.out.println(findRs);
//            System.out.println(s);
            sbContent.append("翻译翻译:\n        ");
            sbContent.append(s);
            outBean.setContent(sbContent.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outBean;
    }	
	
    
    
    
     private static String decodeUnicode(String theString) {    
    	 char aChar;    
    	      int len = theString.length();    
    	     StringBuffer outBuffer = new StringBuffer(len);    
    	     for (int x = 0; x < len;) {    
    	      aChar = theString.charAt(x++);    
    	      if (aChar == '\\') {    
    	       aChar = theString.charAt(x++);    
    	       if (aChar == 'u') {    
    	        int value = 0;    
    	        for (int i = 0; i < 4; i++) {    
    	         aChar = theString.charAt(x++);    
    	         switch (aChar) {    
    	         case '0':    
    	         case '1':    
    	         case '2':    
    	         case '3':    
    	         case '4':    
    	         case '5':    
    	         case '6':    
    	         case '7':    
    	         case '8':    
    	         case '9':    
    	        	 value = (value << 4) + aChar - '0';    
    	        	 break;    
    	           case 'a':    
    	           case 'b':    
    	           case 'c':    
    	           case 'd':    
    	           case 'e':    
    	           case 'f':    
    	            value = (value << 4) + 10 + aChar - 'a';    
    	           break;    
    	           case 'A':    
    	           case 'B':    
    	           case 'C':    
    	           case 'D':    
    	           case 'E':    
    	           case 'F':    
    	            value = (value << 4) + 10 + aChar - 'A';    
    	            break;    
    	           default:    
    	            throw new IllegalArgumentException(    
    	              "Malformed   \\uxxxx   encoding.");    
    	           }    
    	         }    
    	          outBuffer.append((char) value);    
    	         } else {    
    	          if (aChar == 't')    
    	           aChar = '\t';    
    	          else if (aChar == 'r')    
    	           aChar = '\r';    
    	          else if (aChar == 'n')    
    	           aChar = '\n';    
    	          else if (aChar == 'f')    
    	           aChar = '\f';    
    	          outBuffer.append(aChar);    
    	         }    
    	        } else   
    	        outBuffer.append(aChar);    
    	       }    
    	       return outBuffer.toString();    
    	      }   
}
