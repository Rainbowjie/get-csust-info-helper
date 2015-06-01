package com.yinbro.wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinbro.wx.busness.SimpleReply;
import com.yinbro.wx.util.LogUtil;
import com.yinbro.wx.util.MessageUtil;
import com.yinbro.wx.util.WeixinMessageBean;


@SuppressWarnings("serial")
public class WeixinInServlet extends HttpServlet {
	
	final String TOKEN = "yinbro";
	
	//doPost用来处理业务请求
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		//收到的文本信息，进行转发控制
		WeixinMessageBean receiveMess = MessageUtil.reqToWXBean(req);
		
		//发回给微信服务器的消息
		String xmlStr = null;
		//简单转发器
		
		//content 201250080222#password 访问教学平台信息
		if(receiveMess.getContent().matches(".*#.*")){
			WeixinMessageBean sendMess = SimpleReply.checkPT(receiveMess);
			xmlStr =  MessageUtil.wxmbToXmlStr(sendMess);
		}
		//content 图书-书名   馆藏查询
		else if(receiveMess.getContent().matches("图书-.*")){
			WeixinMessageBean sendMess = SimpleReply.checkLib(receiveMess);
			xmlStr =  MessageUtil.wxmbToXmlStr(sendMess);
		}
		//content 翻译-“中文 ”
		else if(receiveMess.getContent().matches("翻译-.*")){
			WeixinMessageBean sendMess = SimpleReply.translate(receiveMess);
			xmlStr =  MessageUtil.wxmbToXmlStr(sendMess);
		}
		//? ？ 帮助文档
		else if(receiveMess.getContent().equals("?")||receiveMess.getContent().equals("？")){
			WeixinMessageBean sendMess = SimpleReply.help(receiveMess);
			xmlStr =  MessageUtil.wxmbToXmlStr(sendMess);
		}
				
		//未知指令,默认的菜单回复
		else {
			WeixinMessageBean sendMess = SimpleReply.robotReply(receiveMess);
			xmlStr =  MessageUtil.wxmbToXmlStr(sendMess);
//			LogUtil.appendLog("日志记录");
			
		}
		
				//other  翻译
		out.print(xmlStr);
		LogUtil.appendLog(xmlStr);
	}
	
	
	
	
	//doGet 用来验证服务器
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//简单的请求拦截
		if( request.getParameter("signature")==null)
			return;
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//微信加密签名
        String signature = request.getParameter("signature");
        //时间戳
        String timestamp = request.getParameter("timestamp");
        //随机数
        String nonce = request.getParameter("nonce");
        //随机字符串
        String echostr = request.getParameter("echostr");
        echostr = checkAuthentication(signature,timestamp,nonce,echostr); 
        //验证通过返回随即字串
        out.write(echostr);
		out.flush();
		out.close();
	}
	  
	 /**
	  * 微信接入认证
	  * */
	 private String  checkAuthentication(String signature,String timestamp,String nonce,String echostr) {
	        String result ="";
	        // 将获取到的参数放入数组
	        String[] ArrTmp = { TOKEN, timestamp, nonce };
	        // 按微信提供的方法，对数据内容进行排序
	        Arrays.sort(ArrTmp);
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < ArrTmp.length; i++) {
	            sb.append(ArrTmp[i]);
	        }
	        // 对排序后的字符串进行SHA-1加密
	        String pwd = Encrypt(sb.toString());
	        if (pwd.equals(signature)) { 
	            try {
	                System.out.println("微信平台签名消息验证成功！"); 
	                result = echostr;
	            } catch (Exception e) {
	                e.printStackTrace();
	            } 
	        } else {
	            System.out.println("微信平台签名消息验证失败！");
	        }
	        return result;
	    }

	    /**
	     * 用SHA-1算法加密字符串并返回16进制串
	     * 
	     * @param strSrc
	     * @return
	     */
	    private String Encrypt(String strSrc) {
	        MessageDigest md = null;
	        String strDes = null;
	        byte[] bt = strSrc.getBytes();
	        try {
	            md = MessageDigest.getInstance("SHA-1");
	            md.update(bt);
	            strDes = bytes2Hex(md.digest());
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println("错误");
	            return null;
	        }
	        return strDes;
	    }

	    private String bytes2Hex(byte[] bts) {
	        String des = "";
	        String tmp = null;
	        for (int i = 0; i < bts.length; i++) {
	            tmp = (Integer.toHexString(bts[i] & 0xFF));
	            if (tmp.length() == 1) {
	                des += "0";
	            }
	            des += tmp;
	        }
	        return des;
	    }

	
}
