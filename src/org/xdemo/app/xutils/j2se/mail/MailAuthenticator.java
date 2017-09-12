package org.xdemo.app.xutils.j2se.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月12日 下午12:51:18
 */
public class MailAuthenticator extends Authenticator{
	
	String userName = null;  
    String password = null;  
  
    public MailAuthenticator() {  
    }  
  
    public MailAuthenticator(String userName, String password) {  
        this.userName = userName;  
        this.password = password;  
    }  
  
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);  
    }  

}
