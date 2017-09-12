package org.xdemo.app.xutils.j2se;

import org.xdemo.app.xutils.ext.GsonTools;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月21日 下午7:00:12
 */
public class PrintUtils {
	public static void print(Object obj){
		System.out.println(GsonTools.toJson(obj));
	}
}
