package org.xdemo.app.xutils.j2se;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Java反射工具类
 * 
 * @author <a href="xdemo.org">xdemo.org</a>
 */
public class ReflectUtils {

	/**
	 * 根据成员变量名称获取其值
	 * 
	 * @param obj
	 * @param field
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T> Object getFieldValue(T t, String targetField) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Class clazz = t.getClass();
		while (true) {
			try {
				Field field = clazz.getDeclaredField(targetField);
				field.setAccessible(true);
				return field.get(t);
			} catch (Exception e) {
				if (clazz.getSuperclass() != null&&clazz.getSuperclass()!=Object.class) {
					clazz = clazz.getSuperclass();
					continue;
				} else {
					throw e;
				}
			}
		}
	}

	public static <T> void setFieldValue(T t, String field, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Class clazz = t.getClass();
		while (true) {
			try {
				Field target = clazz.getDeclaredField(field);
				target.setAccessible(true);
				target.set(t,value);
				break;
			} catch (Exception e) {
				if (clazz.getSuperclass() != null&&clazz.getSuperclass()!=Object.class) {
					clazz = clazz.getSuperclass();
					continue;
				} else {
					throw e;
				}
			}
		}
	}

	public static <T> List<Field> getFields(Class<T> clazz, boolean containSupperClass) {

		List<Field> list = new ArrayList<Field>();

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			list.add(field);
		}
		if (containSupperClass) {
			if (clazz.getSuperclass() != null && !clazz.getSuperclass().getSimpleName().equals(Object.class.getSimpleName())) {
				list.addAll(getFields(clazz.getSuperclass(), containSupperClass));
			}
		}

		return list;
	}

	public static <T> List<Method> getMethods(Class<T> clazz, boolean containSupperClass) {

		List<Method> list = new ArrayList<Method>();

		Method[] ms = clazz.getDeclaredMethods();

		for (Method m : ms) {
			list.add(m);
		}
		if (containSupperClass) {
			if (clazz.getSuperclass() != null && !clazz.getSuperclass().getSimpleName().equals(Object.class.getSimpleName())) {
				list.addAll(getMethods(clazz.getSuperclass(), containSupperClass));
			}
		}

		return list;
	}

	/**
	 * 获取类的所有set方法
	 * 
	 * @param clazz
	 * @param containSupperClass
	 * @return
	 */
	public static <T> List<Method> setterMethods(Class<T> clazz, boolean containSupperClass) {

		List<Method> list = new ArrayList<Method>();

		Method[] ms = clazz.getDeclaredMethods();

		for (Method m : ms) {
			if (m.getName().startsWith("set"))
				list.add(m);
		}
		if (containSupperClass) {
			if (clazz.getSuperclass() != null && !clazz.getSuperclass().getSimpleName().equals(Object.class.getSimpleName())) {
				list.addAll(setterMethods(clazz.getSuperclass(), containSupperClass));
			}
		}

		return list;
	}

	/**
	 * 获取类的所有get方法
	 * 
	 * @param clazz
	 * @param containSupperClass
	 * @return
	 */
	public static <T> List<Method> getterMethods(Class<T> clazz, boolean containSupperClass) {

		List<Method> list = new ArrayList<Method>();

		Method[] ms = clazz.getDeclaredMethods();

		for (Method m : ms) {
			if (m.getName().startsWith("get"))
				list.add(m);
		}
		if (containSupperClass) {
			if (clazz.getSuperclass() != null && !clazz.getSuperclass().getSimpleName().equals(Object.class.getSimpleName())) {
				list.addAll(getterMethods(clazz.getSuperclass(), containSupperClass));
			}
		}

		return list;
	}

	/**
	 * 调用对象的无参方法
	 * 
	 * @param instance
	 * @param method
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> Object invoke(Object instance, String method) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Method m = instance.getClass().getMethod(method, new Class[] {});
		return m.invoke(instance, new Object[] {});
	}

	/**
	 * 通过类的实例，调用指定的方法
	 * 
	 * @param instance
	 * @param method
	 * @param paramClasses
	 * @param params
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> Object invoke(Object instance, String method, Class<T>[] paramClasses, Object[] params) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		Method _m = instance.getClass().getMethod(method, paramClasses);
		return _m.invoke(instance, params);
	}

	public static void main(String[] args) {
	}
}
