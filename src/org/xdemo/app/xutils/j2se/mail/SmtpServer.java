package org.xdemo.app.xutils.j2se.mail;

import java.util.HashMap;
import java.util.Map;

/**
 * 常用的SMTP服务器
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月12日 下午12:51:31
 */
@SuppressWarnings("serial")
public interface SmtpServer {

	public static Map<String, String> SMTP_163 = new HashMap<String, String>() {
		{

			put("smtp", "smtp.163.com");
			put("port", "25");

		}
	};

	public static Map<String, String> SMTP_SINA = new HashMap<String, String>() {
		{

			put("smtp", "smtp.sina.com");
			put("port", "25");

		}
	};

	public static Map<String, String> SMTP_SOHU = new HashMap<String, String>() {
		{

			put("smtp", "smtp.sohu.com");
			put("port", "25");

		}
	};

	public static Map<String, String> SMTP_126 = new HashMap<String, String>() {
		{

			put("smtp", "smtp.126.com");
			put("port", "25");

		}
	};

	public static Map<String, String> SMTP_YAHOO = new HashMap<String, String>() {
		{

			put("smtp", "smtp.mail.yahoo.com");
			put("port", "25");

		}
	};

	public static Map<String, String> SMTP_FOXMAIL = new HashMap<String, String>() {
		{

			put("smtp", "smtp.foxmail.com");
			put("port", "25");

		}
	};

}
