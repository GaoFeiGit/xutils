package org.xdemo.app.xutils.j2se;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Object工具类
 * 
 * @author Goofy
 */
public class ObjectUtils {
	
	/**
	 * 简化三元表达式，替换空值
	 * @author Goofy
	 * @Date 2018年6月22日 上午9:23:24
	 * @param source 原对象
	 * @param nullResult 如果空，返回这个参数
	 * @param notNullResult 如果非空，返回这个参数
	 * @return 
	 */
	public static Object replaceNull(Object source,Object nullResult,Object notNullResult){
		if(isNull(source)){
			return nullResult;
		}
		return notNullResult;
	}

	public static boolean equals(Object o1, Object o2) {
		if (o1.equals(o2)) {
			return true;
		}
		return false;
	}

	private static boolean check(Object o) {
		return o == null ? false : true;
	}

	public static Integer intValue(Object o) {
		if (!check(o))
			return null;
		if (o instanceof Integer)
			return (Integer) o;
		return Integer.parseInt(o.toString());
	}

	public static Long longValue(Object o) {
		if (!check(o))
			return null;
		if (o instanceof Long)
			return (Long) o;
		return Long.parseLong(o.toString());
	}

	public static Short shortValue(Object o) {
		if (!check(o))
			return null;
		if (o instanceof Short)
			return (Short) o;
		return Short.parseShort(o.toString());
	}

	public static Float floatValue(Object o) {
		if (!check(o))
			return null;
		if (o instanceof Float)
			return (Float) o;
		return Float.parseFloat(o.toString());
	}

	public static Double doubleValue(Object o) {
		if (!check(o))
			return null;
		if (o instanceof Double)
			return (Double) o;
		return Double.parseDouble(o.toString());
	}

	public static Boolean boolValue(Object o) {
		if (!check(o))
			return null;
		if (o instanceof Boolean)
			return (Boolean) o;
		return Boolean.parseBoolean(o.toString());
	}

	public static boolean isNull(Object o) {
		return o == null ? true : false;
	}

	public static Date dateValue(Object o, String dateFormat) throws Exception {
		if (!check(o))
			return null;
		if (o instanceof Date)
			return (Date) o;
		if (o instanceof Long)
			return new Date((Long) o);
		if (o instanceof String)
			return new SimpleDateFormat(dateFormat).parse((String) o);
		throw new Exception("Sorry,I don't know how to parse this value to a date");
	}
	
	/**
	 * 对象克隆（深度）
	 * @param t
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @author Goofy 2019年7月24日 下午1:41:22
	 */
	public static <T> T clone(T t) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(os));
		oos.writeObject(t);
		oos.flush();
		
		ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(os.toByteArray()));
		@SuppressWarnings("unchecked")
		T ret=(T) ois.readObject();
		
		ois.close();
		oos.close();
		os.close();
		
		return ret;
	}

}
