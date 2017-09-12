package org.xdemo.app.xutils.j2se.FileWatch;


/**
 * 文件变动监控使用方法，JDK 1.7及以上
 * @author Goofy 252878950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2015年7月6日 上午11:22:32
 */
public class Usage {

	public static void main(String[] args) throws Exception {
		new FileWatch("D:\\x.xml") {
			@Override
			public void onChange() {
				System.out.println("...");
			}
		};
	}

}
