package org.xdemo.app.xutils.ext;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * XML序列化与反序列化工具类
 * @author GOofy 252878950@qq.com <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>">xdemo.org</a>
 * @Date 2015年2月28日
 * @see <a href="http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php">Simple-XML</a>
 */
public class SimpleXmlUtils {
	
	/**
	 * 将Bean转换成XML（需要Simple-XML提供的注解）
	 * @throws Exception 
	 */
	public static void beanToXml(Object o,String xmlFile) throws Exception{
		Serializer serializer = new Persister();
		File file = new File(xmlFile);
		serializer.write(o, file);
	}
	
	/**
	 * Xml转Bean
	 * @param xml
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static Object xmlToBean(String xml,Object bean) throws Exception{
		File file = new File(xml);  
		Serializer serializer = new Persister();
		return serializer.read(bean,file);
	}

}
