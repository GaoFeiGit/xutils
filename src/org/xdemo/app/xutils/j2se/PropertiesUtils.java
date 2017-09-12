package org.xdemo.app.xutils.j2se;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tools.ant.filters.StringInputStream;

/**
 * Properties文件的读写
 * @author <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>/">http://<a href="http://www.xdemo.org">www.xdemo.org</a>/</a>
 * 252878950@qq.com
 */
public class PropertiesUtils {
	
	/**
	 * 读取Properties配置文件内容
	 * @param filePath
	 * @return Properties
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Properties load(String filePath) throws FileNotFoundException, IOException{
		Properties ps=new Properties();
		ps.load(new FileInputStream(new File(filePath)));
		return ps;
	}
	
	/**
	 * 从字符串加载
	 * @param str
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static Properties load(String str,String charset) throws IOException{
		InputStream is=new StringInputStream(str, charset);
		Properties ps=new Properties();
		ps.load(is);
		return ps;
	}
	
	/**
	 * 从输入流加载
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static Properties load(InputStream is) throws IOException{
		Properties ps=new Properties();
		ps.load(is);
		return ps;
	}
	
	/**
	 * 写key-value到properties文件 相同的key会被覆盖 追加不同的key-value
	 * @param key 键
	 * @param value 值
	 * @param filePath 文件路径
	 * @param comment key-value的注释
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void write(String key,String value,String comment,String filePath) throws FileNotFoundException, IOException{
		Properties ps=new Properties();
		
		File file=new File(filePath);
		if(file.exists()){
			FileInputStream fis=new FileInputStream(file);
			ps.load(fis);
			fis.close();
		}
		ps.setProperty(key, value);
		ps.store(new FileOutputStream(new File(filePath)), comment);
	}

}
