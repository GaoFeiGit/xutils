package org.xdemo.app.xutils.j2se;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.CloneFailedException;

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
	 * 对象复制
	 * @param t1
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copy(T t1) throws Exception {
		T t2=(T) t1.getClass().newInstance();
		List<Method> setters = ReflectUtils.setterMethods(t1.getClass(), true);
		for (Method m : setters) {
			m.invoke(t2, new Object[] { t1.getClass().getMethod(m.getName().replaceFirst("s", "g"), null).invoke(t1, null) });
		}
		return t2;
	}
	
	/**
	 * 对象克隆，要求被克隆对象必须实现Cloneable接口,切需要重写close接口，并将protected改为public
	 * @param obj
	 * @return
	 */
	public static <T> T clone(final T obj) {
        if (obj instanceof Cloneable) {
            final Object result;
            if (obj.getClass().isArray()) {
                final Class<?> componentType = obj.getClass().getComponentType();
                if (!componentType.isPrimitive()) {
                    result = ((Object[]) obj).clone();
                } else {
                    int length = Array.getLength(obj);
                    result = Array.newInstance(componentType, length);
                    while (length-- > 0) {
                        Array.set(result, length, Array.get(obj, length));
                    }
                }
            } else {
                try {
                    final Method clone = obj.getClass().getMethod("clone");
                    result = clone.invoke(obj);
                } catch (final NoSuchMethodException e) {
                    throw new CloneFailedException("Cloneable type "
                        + obj.getClass().getName()
                        + " has no clone method", e);
                } catch (final IllegalAccessException e) {
                    throw new CloneFailedException("Cannot clone Cloneable type "
                        + obj.getClass().getName(), e);
                } catch (final InvocationTargetException e) {
                    throw new CloneFailedException("Exception cloning Cloneable type "
                        + obj.getClass().getName(), e.getCause());
                }
            }
            @SuppressWarnings("unchecked") // OK because input is of type T
            final T checked = (T) result;
            return checked;
        }

        return null;
    }

}
