package org.xdemo.app.xutils.j2se;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Map操作，忽略大小写，指定返回类型
 * @author Goofy 252878950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 */
public class MapUtils {
	public static Long getLong(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof Long) {
			return (Long) v;
		} else {
			return Long.parseLong(v.toString());
		}
	}

	public static Integer getInt(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof Integer) {
			return (Integer) v;
		} else {
			return Integer.parseInt(v.toString());
		}
	}

	public static String getString(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof String) {
			return (String) v;
		} else {
			return v.toString();
		}
	}

	public static Float getFloat(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof Float) {
			return (Float) v;
		} else {
			return Float.parseFloat(v.toString());
		}
	}

	public static Double getDouble(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof Double) {
			return (Double) v;
		} else {
			return Double.parseDouble(v.toString());
		}
	}

	public static Date getDate(Map<String, Object> map, Object key, String dateFormat) throws Exception {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof Date) {
			return (Date) v;
		} else if (v instanceof Long) {
			return new Date((Long) v);
		} else if (v instanceof String) {
			if (StringUtils.isBlank(dateFormat))
				throw new Exception("Please make a date format");
			return new SimpleDateFormat(dateFormat).parse(v.toString());
		} else {
			throw new Exception("Sorry,I don't know how to parse this value to a date");
		}
	}

	public static Character getChar(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof Character) {
			return (Character) v;
		} else {
			return v.toString().charAt(0);
		}
	}

	public static Short getShort(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof Short) {
			return (Short) v;
		} else {
			return Short.parseShort(v.toString());
		}
	}

	public static Object getObject(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		return map.get(key);
	}

	public static Boolean getBoolean(Map<String, Object> map, Object key) {
		if (!check(map, key))
			return null;
		Object v = map.get(key);
		if (v == null)
			return null;
		if (v instanceof Short) {
			return (Boolean) v;
		} else {
			return Boolean.parseBoolean(v.toString());
		}
	}

	private static boolean check(Map<String, Object> map, Object key) {
		if (map == null || map.size() == 0 || key == null)
			return false;
		return true;
	}

	public static <K, V> Properties toProperties(final Map<K, V> map) {
		final Properties answer = new Properties();
		if (map != null) {
			for (final Entry<K, V> entry2 : map.entrySet()) {
				final Map.Entry<?, ?> entry = entry2;
				final Object key = entry.getKey();
				final Object value = entry.getValue();
				answer.put(key, value);
			}
		}
		return answer;
	}

	public static <T> T mapToEntity(Map<String, Object> map, Class<T> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		T t = clazz.newInstance();
		List<Method> setter = ReflectUtils.setterMethods(clazz, true);
		String field;
		for (Method m : setter) {
			field = StringUtils.firstCharToLowerCase(m.getName().replace("set", ""));
			m.invoke(t, new Object[] { map.get(field) });
		}
		return t;
	}

	public static <T> Map<String, Object> entityToMap(T t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Method> getter = ReflectUtils.getterMethods(t.getClass(), true);
		String field;
		Object value = null;
		for (Method m : getter) {
			field = StringUtils.firstCharToLowerCase(m.getName().replace("get", ""));
			value = m.invoke(t, new Object[] {});
			if (value == null) {
				continue;
			}
			if (value instanceof String) {
				if (value.equals("")) {
					continue;
				}
			}
			if (field.equals("productMemo"))
				continue;
			map.put(field, value);
		}
		return map;
	}

}
