package com.yinbro.wx.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MessageUtil {
	
	/**
	 * 来自微信的请求转换成Weixin消息实体
	 * @param request
	 * @return
	 */
	public static WeixinMessageBean reqToWXBean(HttpServletRequest request){
		WeixinMessageBean bean = new WeixinMessageBean();
		StringBuffer sb = new StringBuffer();
		try {
			InputStream in = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(in,"UTF-8");
			BufferedReader bf = new BufferedReader(isr);
			String line = null;
			while((line = bf.readLine())!=null)
				sb.append(line);
			Document doc = Jsoup.parse(sb.toString());
		
			//封装到WXMessageBean bean中
			bean.setFromUserName(doc.getElementsByTag("fromusername").text());
			bean.setToUserName(doc.getElementsByTag("tousername").text());
			bean.setCreateTime(Integer.parseInt(doc.getElementsByTag("createtime").text()));
			bean.setMsgType(doc.getElementsByTag("msgtype").text());
			bean.setMsgId(doc.getElementsByTag("msgid").text());
			bean.setContent(doc.getElementsByTag("content").text());
			
			
			
			//收到的XML添加到日志文件中
			LogUtil.appendLog(sb.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	
	/**@param receiveMess
	 * @param content
	 * 消息对象转换成规范XML*/
	public static String wxmbToXmlStr(WeixinMessageBean messBean) {
		StringBuffer sb = new StringBuffer();
		WeixinMessageBean sendMess = new WeixinMessageBean();
		
		sendMess.setFromUserName(messBean.getToUserName());
		sendMess.setToUserName(messBean.getFromUserName());
		sendMess.setCreateTime((int)new Date().getTime());
		sendMess.setMsgType(messBean.getMsgType());
		sendMess.setMsgId(messBean.getMsgId());
		sendMess.setContent(messBean.getContent());
		
		sb.append("<xml><ToUserName><![CDATA["+  sendMess.getToUserName()  + "]]></ToUserName>"
				+ "<FromUserName><![CDATA["+  sendMess.getFromUserName()  + "]]></FromUserName>"
				+ "<CreateTime>"+  sendMess.getCreateTime() + "</CreateTime>"
				+ "<MsgType><![CDATA["+  "text"  + "]]></MsgType>"
				+ "<Content><![CDATA["+  sendMess.getContent()  + "]]></Content>"
				+ "<MsgId>"+  sendMess.getMsgId()  + "</MsgId>"
				+ "</xml>");
		return sb.toString();
	}

}
