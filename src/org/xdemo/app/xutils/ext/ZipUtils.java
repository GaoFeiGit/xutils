package org.xdemo.app.xutils.ext;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
/**
 * 文件压缩工具类
 * @author <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>">xdemo.org</a>
 * 2015年2月28日
 */
/**
 * 压缩文件
 * @author <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>">xdemo.org</a>
 */
public class ZipUtils {

	/**
	 * 目录压缩
	 * @param comments zip注释
	 * @param targetZip zip路径
	 * @param dir 要压缩的目录
	 */
	public static void zipDir(String dir,String targetZip,String comments) {
		String separator=System.getProperty("file.separator");
		
		List<File> files = getFiles(dir);
		
		dir=dir.replace("\\", separator).replace("/", separator);
		
		if(!dir.endsWith(separator)){
			dir+=separator;
		}
		
		byte buffer[] = new byte[40960];

		try {

			BufferedInputStream bis = null;

			FileOutputStream fos = new FileOutputStream(targetZip);

			FileInputStream fis = null;

			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

			ZipEntry entry = null;

			zos.setEncoding("GBK");
			zos.setComment(comments);
			
			for (File file : files) {
				
				String entryName = file.getAbsolutePath().replace("\\", separator).replace("/", separator).replace(dir.replace("\\", separator).replace("/", separator), "");
				if(file.isDirectory()){
					entryName+=separator;
				}
				entry = new ZipEntry(entryName);
				zos.putNextEntry(entry);
				if(file.isDirectory())continue;
				
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				int count;
				while ((count = bis.read(buffer)) != -1) {
					zos.write(buffer, 0, count);
				}
				bis.close();

			}
			zos.close();
			if(fis!=null)
				fis.close();
			if(fos!=null)
				fos.close();
			if(bis!=null)
				bis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 获取目录下所有文件(包括子文件夹) 递归调用
	 * 
	 * @param realpath
	 * @param files
	 * @return
	 */
	private static List<File> getFiles(String realpath) {
		List<File> files=new ArrayList<File>();
		File realFile = new File(realpath);
		if (realFile.isDirectory()) {
			File[] subfiles = realFile.listFiles();
			for (File file : subfiles) {
				files.add(file);
				if (file.isDirectory()) {
					files.addAll(getFiles(file.getAbsolutePath()));
				}
			}
		}
		return files;
	}

	/**
	 * 支持单个或者多个文件压缩到一个压缩包，不支持目录压缩，没有层级关系
	 * 
	 * @param comments
	 *            压缩包右侧的注释
	 * @param targetZip
	 *            压缩包文件地址
	 * @param files
	 *            一个File或者File[]数组
	 */
	public static void zip(String comments, String targetZip, File... files) {

		byte buffer[] = new byte[40960];

		try {

			BufferedInputStream bis = null;

			FileOutputStream fos = new FileOutputStream(targetZip);

			FileInputStream fis = null;

			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

			ZipEntry entry = null;

			zos.setEncoding("GBK");
			zos.setComment(comments);

			for (File file : files) {
				if (file.isDirectory())
					continue;
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);

				int count;
				while ((count = bis.read(buffer)) != -1) {
					zos.write(buffer, 0, count);
				}
				bis.close();
			}
			zos.close();
			if(fis!=null)
				fis.close();
			if(fos!=null)
				fos.close();
			if(bis!=null)
				bis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压缩
	 * @param zipFile zip文件
	 * @param target 目标目录
	 */
	@SuppressWarnings("unchecked")
	public static void unZip(String zipFile,String target){
		FileOutputStream fileOut;
		File file;
		InputStream inputStream;
		byte[] buff = new byte[1024];
		int length = 0;
		if(!new File(target).exists()){
			new File(target).mkdirs();
		}
		try {
			ZipFile zf = new ZipFile(zipFile);

			for (Enumeration<ZipEntry> ent = zf.getEntries(); ent.hasMoreElements();) {
				ZipEntry entry = ent.nextElement();
				file = new File(target + entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}

					inputStream = zf.getInputStream(entry);

					fileOut = new FileOutputStream(file);

					while ((length = inputStream.read(buff)) > 0) {
						fileOut.write(buff, 0, length);
					}

					fileOut.close();
					inputStream.close();
				}
			}
			zf.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * 解压缩
	 * @param zipFile zip文件
	 * @param target 目标目录
	 * @param prefix 前缀，可不填为null
	 * @param suffix 后缀，可不填为null
	 */
	public static void unZip(String zipFile,String target,String prefix,String suffix){
		FileOutputStream fileOut;
		File file;
		InputStream inputStream;
		byte[] buff = new byte[1024];
		int length = 0;
		if(!new File(target).exists()){
			new File(target).mkdirs();
		}
		try {
			ZipFile zf = new ZipFile(zipFile);

			for (Enumeration<ZipEntry> ent = zf.getEntries(); ent.hasMoreElements();) {
				ZipEntry entry = ent.nextElement();
				if(!StringUtils.isBlank(prefix)){
					if(!entry.getName().startsWith(prefix)){
						continue;
					}
				}
				if(!StringUtils.isBlank(suffix)){
					if(!entry.getName().endsWith(suffix)){
						continue;
					}
				}
				file = new File(target + entry.getName());
				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}

					inputStream = zf.getInputStream(entry);

					fileOut = new FileOutputStream(file);

					while ((length = inputStream.read(buff)) > 0) {
						fileOut.write(buff, 0, length);
					}

					fileOut.close();
					inputStream.close();
				}
			}
			zf.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		zipDir("D:\\fff", "D:\\666.zip", "测试");
	}
	
}
