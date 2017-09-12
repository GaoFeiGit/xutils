package org.xdemo.app.xutils.j2se.mail;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月12日 下午12:51:00
 */
public class Email {
	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort;
	// 邮件发送者的地址
	private String fromAddress;
	//邮件发送者名称
	private String fromNickName;
	// 邮件接收者的地址
	private String toAddress;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate = false;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private List<File> attachments;

	/**
	 * 获得邮件会话属性
	 * @throws GeneralSecurityException 
	 */
	public Properties getProperties() throws GeneralSecurityException {
		Properties props = new Properties();
		props.put("mail.smtp.host", this.mailServerHost);
		props.put("mail.smtp.port", this.mailServerPort);
		props.put("mail.smtp.auth", validate ? "true" : "false");
		//SSL
		/*MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.socketFactory", sf);
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.transport.protocol", "smtps");
		props.put("mail.smtp.starttls.enable", "true");*/
		return props;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}

	public String getFromNickName() {
		return fromNickName;
	}

	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
	}
	
}
