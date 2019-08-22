package org.xdemo.app.xutils.j2se;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 字符串工具类，部分方法来自网上<br>
 * 感谢hunray：http://hunray.iteye.com/blog/1849585
 *
 * @author Goofy <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>/">http://<a href="http://www.xdemo.org">www.xdemo.org</a>/</a>
 * 252878950@qq.com
 */
public class StringUtils {

    /**
     * 将字符串有某种编码转变成另一种编码
     *
     * @param string        编码的字符串
     * @param originCharset 原始编码格式
     * @param targetCharset 目标编码格式
     * @return String 编码后的字符串
     */
    public static String encodeString(String string, Charset originCharset, Charset targetCharset) {
        return string = new String(string.getBytes(originCharset), targetCharset);
    }

    /**
     * URL编码
     *
     * @param string  编码字符串
     * @param charset 编码格式
     * @return String
     */
    @SuppressWarnings("deprecation")
    public static String encodeUrl(String string, String charset) {
        if (null != charset && !charset.isEmpty()) {
            try {
                return URLEncoder.encode(string, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return URLEncoder.encode(string);
    }

    /**
     * URL编码
     *
     * @param string  解码字符串
     * @param charset 解码格式
     * @return String
     */
    @SuppressWarnings("deprecation")
    public static String decodeUrl(String string, String charset) {
        if (null != charset && !charset.isEmpty()) {
            try {
                return URLDecoder.decode(string, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return URLDecoder.decode(string);
    }

    /**
     * 判断字符串是否是空的 方法摘自commons.lang
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>
     * 判断字符串是否是""," ",null,注意和isEmpty的区别
     * </p>
     * 方法摘自commons.lang
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 首字母转大写
     *
     * @param str
     * @return
     */
    public static String firstCharToUpperCase(String str) {
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            char[] cs = str.toCharArray();
            cs[0] -= 32;
            return String.valueOf(cs);
        }
    }

    /**
     * 首字母转小写
     *
     * @param str
     * @return
     */
    public static String firstCharToLowerCase(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            char[] cs = str.toCharArray();
            cs[0] += 32;
            return String.valueOf(cs);
        }
    }

    /**
     * 去字符串两端空格
     *
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str.trim();
    }

    /**
     * 字符串判断是否相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        return str1.equals(str2);
    }

    /**
     * 字符串判断是否相等,忽略大小写
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 去掉所有的空格，中文空格，Tab制表符，中文全角空格
     *
     * @param str
     * @return
     */
    public static String trimAll(String str) {
        return str.trim().replace(" ", "").replace(" ", "").replace("	", "").replace("　", "");
    }

    /**
     * 格式化字符串 如 "字符串%1$s字符串%2$s"
     *
     * @param src
     * @param src
     * @return
     */
    public static String format(String src, Object... args) {
        return String.format(src, args);
    }

    /**
     * @param b 字节数组
     * @return 16进制字符串
     * @throws
     * @Description:字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     */
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return temp;
    }

    /**
     * @param src
     * @return
     * @throws
     * @Title:char2Byte
     * @Description:字符转成字节数据char-->integer-->byte
     */
    public static Byte char2Byte(Character src) {
        return Integer.valueOf((int) src).byteValue();
    }

    /**
     * @param a   转化数据
     * @param len 占用字节数
     * @return
     * @throws
     * @Title:intToHexString
     * @Description:10进制数字转成16进制
     */
    public static String intToHexString(int a, int len) {
        len <<= 1;
        String hexString = Integer.toHexString(a);
        int b = len - hexString.length();
        if (b > 0) {
            for (int i = 0; i < b; i++) {
                hexString = "0" + hexString;
            }
        }
        return hexString;
    }

    /**
     * 从输入流中读取字符串
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String readFromInputStream(InputStream is){
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
