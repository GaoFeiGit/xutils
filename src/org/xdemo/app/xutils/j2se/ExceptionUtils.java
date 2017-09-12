package org.xdemo.app.xutils.j2se;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月21日 下午7:23:19
 * 感谢：https://github.com/0opslab/opslabJutil
 */
public class ExceptionUtils {
	/**
	 * 只返回指定包中的异常堆栈信息
	 * 
	 * @param e
	 *            异常信息
	 * @param packageName
	 *            只转换某个包下的信息
	 * @return string
	 */
	public static String parse(Throwable e, String packageName) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		String str = sw.toString();
		String[] arrs = str.split("\n");
		StringBuffer sb = new StringBuffer();
		sb.append(arrs[0] + "\n");
		for (int i = 0; i < arrs.length; i++) {
			String temp = arrs[i];
			if (temp != null && temp.indexOf(packageName) > 0) {
				sb.append(temp + "\n");
			}
		}
		return sb.toString();
	}
	
	public static void print(Throwable e, String packageName) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		String str = sw.toString();
		String[] arrs = str.split("\n");
		StringBuffer sb = new StringBuffer();
		sb.append(arrs[0] + "\n");
		for (int i = 0; i < arrs.length; i++) {
			String temp = arrs[i];
			if (temp != null && temp.indexOf(packageName) > 0) {
				sb.append(temp + "\n");
			}
		}
		System.out.println(sb.toString());
	}

	/**
	 * 获取异常信息
	 * 
	 * @param e
	 *            异常信息
	 * @return string
	 */
	public static String parse(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		return sw.toString();
	}
	
	public static void print(Throwable e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		System.out.println(sw.toString());
	}
	
}
