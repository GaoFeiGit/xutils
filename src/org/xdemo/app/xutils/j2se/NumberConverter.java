package org.xdemo.app.xutils.j2se;

/**
 * 数字转换
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月22日 上午9:59:47
 */
public class NumberConverter {
	
	public static String intToBinary(int x){
		return Integer.toBinaryString(x);
	}
	
	public static String intToOctal(int x){
		return Integer.toOctalString(x);
	}
	
	public static String intToHex(int x){
		return Integer.toHexString(x);
	}
	
	public static int binaryToInt(String x){
		return Integer.valueOf(x,2);
	}
	
	public static String binaryToOctal(String x){
		return intToHex(binaryToInt(x));
	}
	
	public static String binaryToHex(String x){
		return intToHex(binaryToInt(x));
	}
	
	public static int hexToInt(String x){
		return Integer.valueOf(x, 16);
	}
	
	public static String hexToOctal(String x){
		return Integer.toOctalString(Integer.valueOf(x,16));
	}
	
	public static String hexToBinary(String x){
		return Integer.toBinaryString(Integer.valueOf(x,16));
	}
	
	public static int octalToInt(String x){
		return Integer.valueOf(x,8);
	}
	
	public static String octalToBinary(String x){
		return Integer.toBinaryString(Integer.valueOf(x,8));
	}
	
	public static String octalToHex(String x){
		return Integer.toHexString(Integer.valueOf(x,8));
	}
	
}
