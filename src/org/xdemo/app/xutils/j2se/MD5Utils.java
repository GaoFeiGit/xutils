package org.xdemo.app.xutils.j2se;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * @author <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>/">http://<a href="http://www.xdemo.org">www.xdemo.org</a>/</a>
 * 252878950@qq.com
 */
public class MD5Utils {
	
	/**
	 * 获取字符串的MD5
	 * @param string 字符串
	 * @return String MD5字符串
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String string,String salt) throws NoSuchAlgorithmException{
		
		String target=null;
		if(StringUtils.isBlank(salt)){
			target=string;
		}else{
			target=md5(salt+string+salt, null);
		}
		
		byte[] byteString=target.getBytes(Charset.forName("utf-8"));
		MessageDigest md=MessageDigest.getInstance("MD5");
		byte[] array=md.digest(byteString);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	    }
		return sb.toString();
	}
	
	/**
	 * 获取文件MD5
	 * @param filePath 文件路径
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws DigestException
	 */
	public static String md5(String filePath) throws NoSuchAlgorithmException, IOException, DigestException{
		MessageDigest md=MessageDigest.getInstance("MD5");
		int length=0;
		byte[] buffer=new byte[1024];
		byte[] array=null;
		InputStream is=new FileInputStream(new File(filePath));
		while((length=is.read(buffer))!=-1){
			md.update(buffer,0,length);
		}
		
		is.close();
		
		array=md.digest();
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	    }
		
		return sb.toString();
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(md5("123456", "6d02d09506f651a26bdc3fef63494e5b"));
	}
}
