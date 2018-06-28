package org.xdemo.app.xutils.j2se;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 获取 项目/文件 路径
 * @author <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>/">http://<a href="http://www.xdemo.org">www.xdemo.org</a>/</a>
 * 252878950@qq.com
 */
public class PathUtils {
	
	
	/**
	 * 获取所在的盘符
	 * @return String
	 */
	public static String getDrive() {
		  return new File("/").getAbsolutePath();
	}
	
	/**
	 * 获取项目
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String classPath(){
		return Thread.currentThread().getContextClassLoader().getResource("").getPath();
	}
	
	public static String getJarPath() throws IOException, URISyntaxException {
		URL url = PathUtils.class.getProtectionDomain().getCodeSource().getLocation();
		String filePath = null;
		try {
			filePath = URLDecoder.decode(url.getPath(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);

		return new File(filePath).getAbsolutePath();
	}
	
	public static String toOsPath(String path){
		String separator=File.separator;
		path=path.replace("\\", separator).replace("/", separator);
		return path;
	}

}
