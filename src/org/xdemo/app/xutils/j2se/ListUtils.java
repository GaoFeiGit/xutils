/**
 * 
 */
package org.xdemo.app.xutils.j2se;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author Goofy List工具类
 * 更强大工具类参考{@link org.apache.commons.collections4.ListUtils}
 */
public class ListUtils {
	/**
	 * 判断List是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean isEmpty(List<T> list) {
		if (list == null || list.size() == 0)
			return true;
		return false;
	}

	/**
	 * 判断List中是否含有某个对象
	 * 
	 * @param list
	 * @param t
	 * @return
	 */
	public static <T> boolean has(List<T> list, T t) {
		if (list == null || list.size() == 0)
			return false;
		if (list.contains(t))
			return true;
		return false;
	}

	/**
	 * List转数组
	 * 
	 * @param list
	 * @param array
	 * @return
	 */
	public static <T> T[] asArray(List<T> list, T[] array) {
		if (list == null || list.size() == 0)
			return null;
		return list.toArray(array);
	}

	/**
	 * 排序
	 * 
	 * @param list
	 * @param c
	 * @return
	 */
	public static <T> List<T> sort(List<T> list, Comparator<T> c) {
		Collections.sort(list, c);
		return list;
	}

	/**
	 * List toString
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static <T> String toString(List<T> list,String joinStr,String dateFormat) throws Exception {
		StringBuffer sb = new StringBuffer("");
		if (isEmpty(list))
			return sb.toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!StringUtils.isBlank(dateFormat))
			sdf=new SimpleDateFormat(dateFormat);
		int i = 0;
		for (T t : list) {
			if ((t instanceof Integer) || (t instanceof Long) || (t instanceof Short) || (t instanceof Boolean) || (t instanceof Byte) || (t instanceof String) || (t instanceof Character) || (t instanceof Float) || (t instanceof Double) || (t instanceof Date)) {
				if (t instanceof Date) {
					sb.append((i++ == 0) ? sdf.format(t) : (joinStr + sdf.format(t)));
				} else {
					sb.append((i++ == 0) ? t.toString() : (joinStr + t.toString()));
				}
			} else {
				try {
					throw new Exception("List.toString()方法仅支持Integer,Short,Long,Boolean,Byte,String,Character,Float,Double,Date");
				} catch (Exception e) {
					e.printStackTrace();
					return sb.toString();
				}

			}
		}

		return sb.toString();
	}

	/**
	 * 判断两个List是否相等
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static boolean isEqualList(final Collection<?> list1, final Collection<?> list2) {
		if (list1 == list2) {
			return true;
		}
		if (list1 == null || list2 == null || list1.size() != list2.size()) {
			return false;
		}

		final Iterator<?> it1 = list1.iterator();
		final Iterator<?> it2 = list2.iterator();
		Object obj1 = null;
		Object obj2 = null;

		while (it1.hasNext() && it2.hasNext()) {
			obj1 = it1.next();
			obj2 = it2.next();

			if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))) {
				return false;
			}
		}

		return !(it1.hasNext() || it2.hasNext());
	}

	/**
	 * 两个集合交集
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static <E> List<E> intersection(final List<? extends E> list1, final List<? extends E> list2) {
		final List<E> result = new ArrayList<E>();

		List<? extends E> smaller = list1;
		List<? extends E> larger = list2;
		if (list1.size() > list2.size()) {
			smaller = list2;
			larger = list1;
		}

		final HashSet<E> hashSet = new HashSet<E>(smaller);

		for (final E e : larger) {
			if (hashSet.contains(e)) {
				result.add(e);
				hashSet.remove(e);
			}
		}
		return result;
	}
	
	public static <E> List<E> merge(List<E> list1,List<E> list2){
		list1.addAll(list2);
		return list1;
	}

}
