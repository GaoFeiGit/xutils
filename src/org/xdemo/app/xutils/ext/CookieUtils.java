package org.xdemo.app.xutils.ext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie操作
 * @author Goofy
 */
public class CookieUtils {
	
	/**
	 * 从Cookie中取出某个key的值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getValue(HttpServletRequest request,String name){
		Cookie[] cookies=request.getCookies();
		if(cookies==null)return null;
		String v=null;
        for(Cookie cookie:cookies){
        	if(cookie.getName().equalsIgnoreCase(name)){
        		v=cookie.getValue();
        		break;
        	}
        }
        return v;
	}
	
	/**
	 * 设置Cookie中的某个值
	 * @param request
	 * @param name
	 * @return
	 */
	public static void setValue(HttpServletRequest request,HttpServletResponse response,String name,String value){
		Cookie[] cookies=request.getCookies();
		if(cookies==null)return;
        for(Cookie cookie:cookies){
        	if(cookie.getName().equalsIgnoreCase(name)){
        		cookie.setValue(value);
        		response.addCookie(cookie);
        		break;
        	}
        }
	}
	
	/**
	 * 判断Cookie中是否含有某个key
	 * @param request
	 * @param name
	 * @return
	 */
	public static boolean hasCookie(HttpServletRequest request,String name){
		Cookie[] cookies=request.getCookies();
		if(cookies==null)return false;
		boolean has=false;
        for(Cookie cookie:cookies){
        	if(cookie.getName().equalsIgnoreCase(name)){
        		has=true;
        		break;
        	}
        }
        return has;
	}
	
	
	/**
	 * 写Cookie
	 * @param response HttpServletResponse
	 * @param name Cookie的名字
	 * @param value Cookie的值
	 * @param secure 是否安全
	 * @param expiry 过期时间，-1表示关闭浏览器即过期，请给出一个合理有效的值
	 */
	public static void addCookie(HttpServletRequest request,HttpServletResponse response,String name,String value,boolean secure,int expiry,String path){
		Cookie cookie=new Cookie(name, value);
		if(hasCookie(request, name)){
			cookie.setValue(value);
		}
		cookie.setSecure(secure);
		cookie.setMaxAge(expiry);
		response.addCookie(cookie);
	}
	
	/**
	 * 移除Cookie中的没个Key
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void clearCookie(HttpServletRequest request,HttpServletResponse response,String name){
		Cookie[] cookies=request.getCookies();
		if(cookies==null)return;
        for(Cookie cookie:cookies){
        	if(cookie.getName().equalsIgnoreCase(name)){
        		cookie.setMaxAge(0);
            	cookie.setValue(null);
            	response.addCookie(cookie);
        	}
        }
	}
	
	/**
	 * 清除所有Cookie
	 * @param request
	 * @param response
	 */
	public static void clearAll(HttpServletRequest request,HttpServletResponse response){
		Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
        	cookie.setMaxAge(0);
        	cookie.setValue(null);
        	response.addCookie(cookie);
        }
	}
	
}
