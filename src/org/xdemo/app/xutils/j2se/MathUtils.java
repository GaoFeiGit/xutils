package org.xdemo.app.xutils.j2se;

import java.math.BigDecimal;

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.exception.util.LocalizedFormats;

/**
 * 算数运算,感谢<a
 * href="http://my.oschina.net/return/blog/228905">http://my.oschina.net
 * /return/blog/228905</a>
 * 
 * @author Goofy
 * @Date 2015年12月4日 上午11:30:02
 */
public class MathUtils {
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2, int precision) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2, int precision) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2, int precision) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param precision
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int precision) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, precision, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param precision
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int precision) {
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, precision, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 返回多个数字的最小公倍数
	 * 
	 * @param intNumber
	 * @return
	 */
	public static int LCM(int[] intNumber) {
		int result = 0;
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			int num = intNumber.length;
			while (num > 0) {
				int count = 0;
				for (int array : intNumber) {
					if (i % array != 0) {
						break;
					} else {
						count++;
					}
				}
				if (count == intNumber.length) {
					result = i;
					break;
				}
				num--;
			}
			if (result > 0) {
				break;
			}
		}
		return result;
	}
	
	/**
	 * 返回多个数字的最小公倍数
	 * 
	 * @param intNumber
	 * @return
	 */
	public static int lcm(int[] intNumber) {
		int result = 0;
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			int num = intNumber.length;
			while (num > 0) {
				int count = 0;
				for (int array : intNumber) {
					if (i % array != 0) {
						break;
					} else {
						count++;
					}
				}
				if (count == intNumber.length) {
					result = i;
					break;
				}
				num--;
			}
			if (result > 0) {
				break;
			}
		}
		return result;
	}


	/**
	 * 求多个数最大公约数
	 * @param numbers
	 * @return
	 */
	public static int gcd(int[] numbers) {
		int x, y, gys, temp;
		gys = numbers[0];
		for (int i = 0; i < numbers.length - 1; i++) {

			x = gys;
			y = numbers[i + 1];

			while (y != 0) {
				temp = x % y;
				x = y;
				y = temp;
			}
			gys = x;
		}
		return gys;
	}
	
	/**
	 * 求多个数最大公约数
	 * @param numbers
	 * @return
	 */
	public static int gcd(Integer[] numbers) {
		int x, y, gys, temp;
		gys = numbers[0];
		for (int i = 0; i < numbers.length - 1; i++) {

			x = gys;
			y = numbers[i + 1];

			while (y != 0) {
				temp = x % y;
				x = y;
				y = temp;
			}
			gys = x;
		}
		return gys;
	}
	
	/**
	 * 次方值
	 * 
	 * @param k
	 *            基数
	 * @param e
	 *            次方
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static int pow(final int k, int e) throws IllegalArgumentException {
		if (e < 0) {
			throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.POWER_NEGATIVE_PARAMETERS, k, e);
		}

		int result = 1;
		int k2p = k;
		while (e != 0) {
			if ((e & 0x1) != 0) {
				result *= k2p;
			}
			k2p *= k2p;
			e = e >> 1;
		}
		return result;
	}
	
}
