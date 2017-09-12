package org.xdemo.app.xutils.test;

import org.xdemo.app.xutils.j2se.ObjectUtils;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月18日 上午8:48:13
 */
public class Test {
	
	public static void main(String[] args) throws Exception{
		Foo foo=new Foo("1","3");
		Foo foo2=ObjectUtils.copy(foo);
		
		System.out.println(foo2.getA());
		
	}

}
