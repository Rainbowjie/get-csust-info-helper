package com.csust.pt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.ws.spi.http.HttpHandler;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yinbro.util.ComonUtil;
import com.yinbro.wx.pojo.LessonBean;
import com.yinbro.wx.pojo.LessonNews;
import com.yinbro.wx.pojo.StudentBean;
public class PtHandler {
	private String num = null;
	private String pass = null;
	private static String cookie= null;
	//有参构造器
	public PtHandler(String num,String pass) {
		this.num = num;
		this.pass = pass;
	}

	//课程列表
	static ArrayList<LessonBean> lessonList = new ArrayList<LessonBean>();
		
	

	//判断是否登陆成功，获取cookie
	private String getCookie() throws Exception{
		if(cookie!=null)
			return cookie;
        CloseableHttpClient httpclient = HttpClients.createDefault(); 
        HttpPost post = new HttpPost("http://pt.csust.edu.cn/eol/homepage/common/login.jsp");
		BasicNameValuePair bnvpUserName = new BasicNameValuePair("IPT_LOGINUSERNAME",num);
        BasicNameValuePair bnvpPassWord = new BasicNameValuePair("IPT_LOGINPASSWORD",pass);
        List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
        nvpList.add(bnvpUserName);
        nvpList.add(bnvpPassWord);
		UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(nvpList);
        post.setEntity(postEntity);
        CloseableHttpResponse response = httpclient.execute(post);
        int contentLength = Integer.parseInt(response.getFirstHeader("Content-Length").getValue());
        if(contentLength!=0){
        	System.out.println("授权访问异常");
        	return null;
        }
       
        String resCookie = response.getFirstHeader("Set-Cookie").getValue();
        System.out.println( num + "授权访问成功！ Cookie:"+resCookie);
        cookie = resCookie;
		return cookie;
	}
	
	
	
	//获取课程列表 new method
	public ArrayList<LessonBean> getAllLesson() throws Exception {
		String homePage = "http://pt.csust.edu.cn/eol/lesson/student.lesson.list.jsp";
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpGet httpGet = new HttpGet(homePage);
		httpGet.setHeader("Cookie",getCookie());
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity resEntity = response.getEntity();
        InputStream in = resEntity.getContent();
        String html = ComonUtil.getStrFromInstream(in);
        //获取到解析后的主页
        Document doc = Jsoup.parse(html);
        //获取课程列表的总节点
        Elements eleCources = doc.getElementsByAttributeValue("align", "center");
        Iterator<Element> it = eleCources.iterator();
        Element lessonEle = null;
        while (it.hasNext()) {
        	LessonBean lessonBean = new LessonBean();
        	lessonEle = it.next();
//        	System.out.println(lessonEle.toString());
        	String lessonName =	 lessonEle.getElementsByTag("td").get(0).text();
        	String deptName = 	 lessonEle.getElementsByTag("td").get(1).text();
        	String teacherName = lessonEle.getElementsByTag("td").get(2).text();
        	lessonBean.setLessonName(lessonName);
        	lessonBean.setDeptName(deptName);
        	lessonBean.setTeacherName(teacherName);
        	
        	//抓取lessonID
        	String idStr =	 lessonEle.getElementsByTag("td").get(0).getElementsByAttribute("onclick").toString();
        	 //得到lid=11494
            Pattern p = Pattern.compile("lid=\\d*");
            Matcher m = p.matcher(idStr);
            m.find();
            String lessonID = m.group(0).substring(4);
        	lessonBean.setLessonID(lessonID);
        	lessonList.add(lessonBean);
		}
        return lessonList;
	}
	
	
	/**
	 * 
	 * @return StudentBean
	 * @throws Exception
	 * 获取个人信息
	 * 封装到StudentBean
	 */
	public StudentBean getStudentInfo() throws Exception {
		//学生bean
		StudentBean studentBean = new StudentBean();
		String homePage = "http://pt.csust.edu.cn/eol/welcomepage/student/index.jsp";
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpGet httpGet = new HttpGet(homePage);
		httpGet.setHeader("Cookie",getCookie());
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity resEntity = response.getEntity();
        InputStream in = resEntity.getContent();
        String html = ComonUtil.getStrFromInstream(in);
        //得到SID
        Pattern p = Pattern.compile("SID=\\d*");
        Matcher m = p.matcher(html);
        m.find();
        String sid = m.group(0).substring(4);
        studentBean.setSid(sid);
       //在线时长
        Document doc = Jsoup.parse(html);
        Pattern p1 = Pattern.compile("<li>在线总时长：.*分        </li>");
        Matcher m1 = p1.matcher(html);
        m1.find();
        String onlineTime = m1.group(0);
        onlineTime = (String) onlineTime.subSequence(22, onlineTime.length()-10);
        studentBean.setTotalOnlie(onlineTime);
        //登录次数
        Pattern p2 = Pattern.compile("<li>登录次数：\\d*");
        Matcher m2 = p2.matcher(html);
        m2.find();
        String loginTime = m2.group(0);
        loginTime = loginTime.substring(9);
        studentBean.setLoginTime(Integer.parseInt(loginTime));
        //进入信息详情页面,得到html2
        String stuDetailUrl = "http://pt.csust.edu.cn/eol/popups/viewstudent_info.jsp?SID=" + studentBean.getSid()+"&from=welcomepage";
		HttpGet httpGet2 = new HttpGet(stuDetailUrl);
		httpGet2.setHeader("Cookie",getCookie());
        CloseableHttpResponse response2 = httpclient.execute(httpGet2);
        HttpEntity resEntity2 = response2.getEntity();
        InputStream in2 = resEntity2.getContent();
        String html2 =  ComonUtil.getStrFromInstream(in2);
        
        Element e =Jsoup.parse(html2);
        e = e.getElementsByAttributeValue("class", "infotable_bw_vt").first();
        Elements eleInfos = e.getElementsByTag("tr");
        Iterator<Element> eleIterator = eleInfos.iterator();
        
        ArrayList<String> list = new ArrayList<>();
        while (eleIterator.hasNext()) {
        	Element ee = eleIterator.next();
        	String str = ee.getElementsByTag("td").first().text();
        	list.add(str);
		}
        
        studentBean.setRealName(list.get(1));
        studentBean.setEmial(list.get(2));
        studentBean.setGender(list.get(5));
        studentBean.setMajor(list.get(8));
        studentBean.setStuNum(list.get(6));
        studentBean.setDepartment(list.get(7));
        studentBean.setGrade(list.get(9));
        studentBean.setClassName(list.get(10));
        studentBean.setStuPass(pass);
        return studentBean;
	}
	
	
	//获取课程通知
	/**
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public ArrayList<LessonNews> getAllLessonNews(String lessonId) throws Exception{
		ArrayList<LessonNews> list = new ArrayList<LessonNews>();
		String reqUrl = "http://pt.csust.edu.cn/eol/common/inform/index_stu.jsp?s_order=0&lid="+lessonId;
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpGet httpGet = new HttpGet(reqUrl);
		httpGet.setHeader("Cookie",getCookie());
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity resEntity = response.getEntity();
        InputStream in = resEntity.getContent();
        String html = ComonUtil.getStrFromInstream(in);
        Document root = Jsoup.parse(html);
        Elements newsEles = root.getElementsByAttributeValue("class", "neirong_hui");
        LessonNews lessNews = new LessonNews();
        for(Element e : newsEles){
        	 Element a = e.getElementsByTag("a").get(0);
        	 //newsTitle
        	 lessNews.setTitle(a.text());
        	 //nid
	       	 Pattern p = Pattern.compile("nid=\\d*");
	         Matcher m = p.matcher(a.toString());
	         m.find();
	         String nid = m.group(0).substring(4);
	         lessNews.setNid(nid);
	         Elements center = e.getElementsByAttributeValue("align", "center");
	         lessNews.setTime(center.get(0).text());
	         lessNews.setPoster(center.get(1).text());
	         lessNews.setContent(getNewsContentByNID(nid));
	         
	         System.out.println(lessNews.toString());
	         list.add(lessNews);
        }
		//TODO
		return list;
	}

	//根据nid获取消息内容
	private String getNewsContentByNID(String nid) throws Exception{
		String reqUrl = "http://pt.csust.edu.cn/eol/common/inform/message_content.jsp?nid="+nid;
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		HttpGet httpGet = new HttpGet(reqUrl);
		httpGet.setHeader("Cookie",getCookie());
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity resEntity = response.getEntity();
        InputStream in = resEntity.getContent();
        String html = ComonUtil.getStrFromInstream(in);
        Document root = Jsoup.parse(html);
        
        Element abody = root.getElementsByAttributeValue("class", "abody").get(0);
        
        //TODO
        //抓出课程通知内容
        String bfter = StringEscapeUtils.unescapeHtml(abody.toString());
//        System.out.println(bfter);
		return "课程通知body部分";
	}
	
	
	/**
	 * Test
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		PtHandler pt = new  PtHandler("201250080222", "22222222");
		ArrayList<LessonBean> lessList = pt.getAllLesson();
		LessonBean l = lessList.get(7);
		String lessonId = l.getLessonID();
		System.out.println(lessonId);
		System.out.println(l.getLessonName());
		ArrayList<LessonNews> newsList = pt.getAllLessonNews(lessonId);
		//注意为空的情况
		System.out.println(newsList.toString());
	}
	
}

