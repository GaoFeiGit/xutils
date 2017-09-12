package org.xdemo.app.xutils.j2se;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数组操作
 * 
 * @author Goofy
 * @Date 2015年11月22日 上午11:09:45 空判断<br>
 *       长度<br>
 *       最大值<br>
 *       最小值<br>
 *       克隆<br>
 *       int数组转Integer，shor-Short,long-Long。。。。。<br>
 *       转List<br>
 *       包含<br>
 *       总和<br>
 *       平均值--无，通过总和自己计算<br>
 *       排序<br>
 */
public class ArrayUtils {

	/**
	 * 判断数组是否为空
	 * 
	 * @param array
	 * @return
	 */
	public static <T> boolean isEmpty(T[] array) {
		if (array == null || array.length == 0)
			return true;
		return false;
	}

	/**
	 * 获取数组长度
	 * 
	 * @param array
	 * @return
	 */
	public static <T> int length(T[] array) {
		if (array == null || array.length == 0)
			return 0;
		return array.length;
	}

	/**
	 * 判断数组是否含有某个元素
	 * 
	 * @param array
	 * @param t
	 * @return
	 */
	public static <T> boolean contains(T[] array, T t) {
		if (length(array) == 0)
			return false;
		for (T _t : array) {
			if (_t == t)
				return true;
		}
		return false;
	}

	/**
	 * 数组转List
	 * 
	 * @param array
	 * @return
	 */
	public static <T> List<T> asList(T[] array) {
		return Arrays.asList(array);
	}

	/**
	 * 数组转String
	 * 
	 * @param array
	 * @return
	 */
	public static <T> String toString(T[] array, String joinStr, String dateFormat) {
		StringBuffer sb = new StringBuffer("");
		if (isEmpty(array))
			return sb.toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (!StringUtils.isBlank(dateFormat))
			sdf = new SimpleDateFormat(dateFormat);

		int i = 0;
		for (T t : array) {
			if ((t instanceof Integer) || (t instanceof Long) || (t instanceof Short) || (t instanceof Boolean) || (t instanceof Byte) || (t instanceof String) || (t instanceof Character) || (t instanceof Float) || (t instanceof Double) || (t instanceof Date)) {
				if (t instanceof Date) {
					sb.append((i++ == 0) ? sdf.format(t) : (joinStr + sdf.format(t)));
				} else {
					sb.append((i++ == 0) ? t.toString() : (joinStr + t.toString()));
				}
			} else {
				try {
					throw new Exception("仅支持Integer,Short,Long,Boolean,Byte,String,Character,Float,Double,Date");
				} catch (Exception e) {
					e.printStackTrace();
					return sb.toString();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 两个数组合并
	 * 
	 * @param a1
	 *            第一个数组
	 * @param a2
	 *            第二个数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] merge(T[] a1, T[] a2) {
		final Class<?> clazz = a1.getClass().getComponentType();
		final T[] ret = (T[]) Array.newInstance(clazz, a1.length + a2.length);
		System.arraycopy(a1, 0, ret, 0, a1.length);
		System.arraycopy(a2, 0, ret, a1.length, a2.length);
		return ret;
	}

	/**
	 * 两个数组的并集,并去重，如果不去重，使用{@link #merge(Object[], Object[])}
	 * 
	 * @param a1
	 *            第一个数组
	 * @param a2
	 *            第二个数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] union(T[] a1, T[] a2) {
		Set<T> set = new HashSet<T>();

		for (T t : a1) {
			set.add(t);
		}
		for (T t : a2) {
			set.add(t);
		}

		final Class<?> clazz = a1.getClass().getComponentType();
		T[] ret = (T[]) Array.newInstance(clazz, set.size());

		return set.toArray(ret);
	}

	/**
	 * 两个数组的交集
	 * 
	 * @param a1
	 *            第一个数组
	 * @param a2
	 *            第二个数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] intersection(T[] a1, T[] a2) {

		Map<T, Boolean> map = new HashMap<T, Boolean>();

		// 遍历第一个数组
		for (T t : a1) {
			if (!map.containsKey(t)) {
				// 去重，默认不是交集数据
				map.put(t, Boolean.FALSE);
			}
		}

		// 遍历第二个数组
		for (T t : a2) {
			if (map.containsKey(t)) {
				// 如果存在相同的KEY，那么则是并集的一个元素，标记为TRUE
				map.put(t, Boolean.TRUE);
			}
		}

		Set<T> set = new HashSet<T>();

		for (T t : map.keySet()) {
			// 获取标记为TRUE的元素，即为交集数据
			if (map.get(t)) {
				set.add(t);
			}
		}

		final Class<?> clazz = a1.getClass().getComponentType();
		T[] ret = (T[]) Array.newInstance(clazz, set.size());

		return set.toArray(ret);
	}

	/**
	 * 差集，即a1不在a2中的元素
	 * 
	 * @param a1
	 *            第一个数组
	 * @param a2
	 *            第二个数组
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] difference(T[] a1, T[] a2) {
		ArrayList<T> l1 = new ArrayList<T>(asList(a1));
		ArrayList<T> l2 = new ArrayList<T>(asList(a2));

		l1.removeAll(l2);

		final Class<?> clazz = a1.getClass().getComponentType();

		T[] ret = (T[]) Array.newInstance(clazz, l1.size());

		return l1.toArray(ret);
	}

	public static int min(int[] arr) {
		return minMax(arr)[0];
	}

	public static short min(short[] arr) {
		return minMax(arr)[0];
	}

	public static long min(long[] arr) {
		return minMax(arr)[0];
	}

	public static float min(float[] arr) {
		return minMax(arr)[0];
	}

	public static double min(double[] arr) {
		return minMax(arr)[0];
	}

	public static char min(char[] arr) {
		return minMax(arr)[0];
	}

	public static int max(int[] arr) {
		return minMax(arr)[1];
	}

	public static short max(short[] arr) {
		return minMax(arr)[1];
	}

	public static long max(long[] arr) {
		return minMax(arr)[1];
	}

	public static float max(float[] arr) {
		return minMax(arr)[1];
	}

	public static double max(double[] arr) {
		return minMax(arr)[1];
	}

	public static char max(char[] arr) {
		return minMax(arr)[1];
	}

	public static int[] minMax(int[] arr) {
		int max = 0;
		int min = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > arr[max])
				max = x;
			if (arr[x] < arr[min])
				min = x;
		}

		int[] ret = new int[2];
		ret[0] = arr[min];
		ret[1] = arr[max];

		return ret;
	}

	public static short[] minMax(short[] arr) {
		int max = 0;
		int min = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > arr[max])
				max = x;
			if (arr[x] < arr[min])
				min = x;
		}

		short[] ret = new short[2];
		ret[0] = arr[min];
		ret[1] = arr[max];

		return ret;
	}

	public static long[] minMax(long[] arr) {
		int max = 0;
		int min = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > arr[max])
				max = x;
			if (arr[x] < arr[min])
				min = x;
		}

		long[] ret = new long[2];
		ret[0] = arr[min];
		ret[1] = arr[max];

		return ret;
	}

	public static float[] minMax(float[] arr) {
		int max = 0;
		int min = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > arr[max])
				max = x;
			if (arr[x] < arr[min])
				min = x;
		}

		float[] ret = new float[2];
		ret[0] = arr[min];
		ret[1] = arr[max];

		return ret;
	}

	public static double[] minMax(double[] arr) {
		int max = 0;
		int min = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > arr[max])
				max = x;
			if (arr[x] < arr[min])
				min = x;
		}

		double[] ret = new double[2];
		ret[0] = arr[min];
		ret[1] = arr[max];

		return ret;
	}

	public static char[] minMax(char[] arr) {
		int max = 0;
		int min = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > arr[max])
				max = x;
			if (arr[x] < arr[min])
				min = x;
		}

		char[] ret = new char[2];
		ret[0] = arr[min];
		ret[1] = arr[max];

		return ret;
	}

	public static Integer[] toObject(int[] arr) {
		Integer[] ret = new Integer[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i];
		}
		return ret;
	}

	public static Short[] toObject(short[] arr) {
		Short[] ret = new Short[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i];
		}
		return ret;
	}

	public static Long[] toObject(long[] arr) {
		Long[] ret = new Long[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i];
		}
		return ret;
	}

	public static Float[] toObject(float[] arr) {
		Float[] ret = new Float[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i];
		}
		return ret;
	}

	public static Double[] toObject(double[] arr) {
		Double[] ret = new Double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i];
		}
		return ret;
	}

	public static Character[] toObject(char[] arr) {
		Character[] ret = new Character[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i];
		}
		return ret;
	}

	public static int[] toPrimitive(Integer[] arr) {
		int[] ret = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i].intValue();
		}
		return ret;
	}

	public static short[] toPrimitive(Short[] arr) {
		short[] ret = new short[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i].shortValue();
		}
		return ret;
	}

	public static long[] toPrimitive(Long[] arr) {
		long[] ret = new long[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i].longValue();
		}
		return ret;
	}

	public static float[] toPrimitive(Float[] arr) {
		float[] ret = new float[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i].floatValue();
		}
		return ret;
	}

	public static double[] toPrimitive(Double[] arr) {
		double[] ret = new double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i].doubleValue();
		}
		return ret;
	}

	public static char[] toPrimitive(Character[] arr) {
		char[] ret = new char[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i].charValue();
		}
		return ret;
	}

	public static long sum(int[] arr) {
		long sum = 0L;
		for (int i : arr)
			sum += i;
		return sum;
	}

	public static long sum(Integer[] arr) {
		long sum = 0L;
		for (Integer i : arr)
			sum += i.longValue();
		return sum;
	}

	public static long sum(short[] arr) {
		long sum = 0L;
		for (short i : arr)
			sum += i;
		return sum;
	}

	public static long sum(Short[] arr) {
		long sum = 0L;
		for (Short i : arr)
			sum += i.longValue();
		return sum;
	}

	public static long sum(long[] arr) {
		long sum = 0L;
		for (long i : arr)
			sum += i;
		return sum;
	}

	public static long sum(Long[] arr) {
		long sum = 0L;
		for (Long i : arr)
			sum += i.longValue();
		return sum;
	}

	public static float sum(float[] arr) {
		float sum = 0F;
		for (float i : arr)
			sum += i;
		return sum;
	}

	public static float sum(Float[] arr) {
		float sum = 0F;
		for (Float i : arr)
			sum += i.floatValue();
		return sum;
	}

	public static double sum(double[] arr) {
		double sum = 0;
		for (double i : arr)
			sum += i;
		return sum;
	}

	public static double sum(Double[] arr) {
		double sum = 0;
		for (Double i : arr)
			sum += i.doubleValue();
		return sum;
	}

	public static <T> T[] clone(T[] t) {
		return t.clone();
	}

	public static void sort(byte[] a) {
		DualPivotQuicksort.sort(a);
	}

	public static void sort(char[] a) {
		DualPivotQuicksort.sort(a);
	}

	public static void sort(double[] a) {
		DualPivotQuicksort.sort(a);
	}

	public static void sort(float[] a) {
		DualPivotQuicksort.sort(a);
	}

	public static void sort(int[] a) {
		DualPivotQuicksort.sort(a);
	}

	public static void sort(long[] a) {
		DualPivotQuicksort.sort(a);
	}

	public static void sort(short[] a) {
		DualPivotQuicksort.sort(a);
	}

	public static <T> void sort(T[] a, Comparator<? super T> c) {
		Collections.sort(asList(a), c);
	}
}
