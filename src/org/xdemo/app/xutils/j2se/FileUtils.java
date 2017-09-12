package org.xdemo.app.xutils.j2se;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *文件操作工具类 拷贝|删除|写入|读取
 * @author Goofy <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>/">http://<a href="http://www.xdemo.org">www.xdemo.org</a>/</a>
 * 252878950@qq.com
 */
@SuppressWarnings("deprecation")
public class FileUtils {

	/**
	 * 拷贝文件
	 * 
	 * @param src
	 *            源文件
	 * @param dest
	 *            目标文件
	 * @param bufferSize
	 *            每次读取的字节数
	 * @throws IOException
	 */
	public static void copyFile(String src, String dest, int bufferSize)
			throws IOException {

		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(dest);
		byte[] buffer = new byte[bufferSize];
		int length;

		while ((length = fis.read(buffer)) != -1) {
			fos.write(buffer, 0, length);
		}
		fis.close();
		fos.close();
	}

	/**
	 * 删除文件
	 * 
	 * @param src
	 *            源文件
	 */
	public static void deleteFile(String src) {
		new File(src).delete();
	}

	/**
	 * 删除多个文件
	 * 
	 * @param src
	 *            源文件数组
	 */
	public static void deleteFiles(String... src) {
		for (String s : src) {
			deleteFile(s);
		}
	}

	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     */
	public static boolean deleteByDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteByDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	/**
	 * 递归删除目录下的所有文件及子目录下指定类型的所有文件
	 * @param dir
	 * @param fileSuffix 文件名后缀，如：jpg,doc，不含“.”,<font color=red>区分JPG和jpg的大小写</font>
	 * @return
	 */
	public static boolean deleteByFileTypes(File dir,boolean deleteEmptyDir,String... fileSuffix) {
		if(dir.isFile())return false;
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
            	//如果是目录
            	if(children[i].isDirectory()){
            		 if(!deleteByFileTypes(children[i],deleteEmptyDir, fileSuffix)){
            			 return false;
            		 }
            	}
            	
            	//匹配文件后缀
            	if(matchFileType(children[i], fileSuffix)){
            		if(!children[i].delete()){
            			return false;
            		}
            	}
            }
        }
        //删除空文件夹
        if(deleteEmptyDir){
        	if(dir.list().length==0){
        		if(!dir.delete()){
        			return false;
        		}
        	}
        }
        return true;
    }
	
	/**
	 * '匹配文件的后缀
	 * @param file
	 * @param fileSuffix
	 * @return
	 */
	private static boolean matchFileType(File file,String...fileSuffix){
		String fileName=file.getName();
		for(String suffix:fileSuffix){
			if(fileName.endsWith("."+suffix)){
				return true;
			}
		}
		return false;
	}
	

	/**
	 * 将内容写入文件<font color=red>写文件不要用FileWriter，因为这个无法设置编码，使用系统的编码格式</font>
	 * 
	 * @param content
	 *            写入的内容
	 * @param dest
	 *            写入的文件
	 * @param append
	 *            是否追加
	 * @param newLine
	 *            是否换行
	 * @throws IOException
	 */
	public static void write(String content, String dest, boolean append, boolean newLine) throws IOException {
		write(content, dest, append, newLine,Charset.forName("UTF-8"));
	}
	
	/**
	 * 将内容写入文件
	 * 
	 * @param content
	 *            写入的内容
	 * @param dest
	 *            写入的文件
	 * @param append
	 *            是否追加
	 * @param newLine
	 *            是否换行
	 * @param charset 字符编码
	 * @throws IOException
	 */
	public static void write(String content, String dest, boolean append,
			boolean newLine,Charset charset) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dest, append),charset);
		writer.write(content + (newLine == true ? System.getProperty("line.separator") : ""));
		writer.close();
	}
	
	/**
	 * 写文件，写入是其他线程无法同时写入该文件
	 * @param src
	 * @param content
	 */
	public static void writeWithLock(String src,List<String> content){
		RandomAccessFile out=null;
		FileChannel channel=null;
		FileLock lock=null;
		try {
			out = new RandomAccessFile(src, "rwd");
			channel=out.getChannel();
			//文件加锁，独占
			lock=channel.lock();
			//清空文件
			channel.truncate(0);
			
			//写入内容
			for(String str:content){
				out.writeUTF(str);
			}
			
			lock.release();
			channel.close();
			out.close();
		} catch (Exception e) {
			if(lock!=null){
				try {
					lock.release();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(channel!=null){
				try {
					channel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(out!=null){
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 写二进制数据到文件
	 * @param bytes
	 * @param file
	 * @throws Exception
	 */
	public static void write(byte[] bytes, String file) throws Exception {
		InputStream in = new ByteArrayInputStream(bytes);
		File destFile = new File(file);
		destFile.createNewFile();
		OutputStream out = new FileOutputStream(destFile);
		byte[] cache = new byte[1024];
		int nRead = 0;
		while ((nRead = in.read(cache)) != -1) {
			out.write(cache, 0, nRead);
			out.flush();
		}
		out.close();
		in.close();
	}
	
	/**
	 * 读二进制数据
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] readByte(String filePath) throws Exception {
		byte[] data = new byte[0];
		File file = new File(filePath);
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
			byte[] cache = new byte[1024];
			int nRead = 0;
			while ((nRead = in.read(cache)) != -1) {
				out.write(cache, 0, nRead);
				out.flush();
			}
			out.close();
			in.close();
			data = out.toByteArray();
		}
		return data;
	}
	
	/**
	 * 清空文件
	 * @param src 源文件
	 */
	public static void emptyFile(String src){
		RandomAccessFile out=null;
		FileChannel channel=null;
		FileLock lock=null;
		try {
			out = new RandomAccessFile(src, "rwd");
			channel=out.getChannel();
			//文件加锁，独占
			lock=channel.lock();
			//清空文件
			channel.truncate(0);
			lock.release();
			channel.close();
			out.close();
		} catch (Exception e) {
			if(lock!=null){
				try {
					lock.release();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(channel!=null){
				try {
					channel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(out!=null){
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取文件内容
	 * 
	 * @param src
	 *            源文件
	 * @return String[] 文件内容数组，每行占一个数组空间
	 * @throws IOException
	 */
	public static String[] read(String src, Charset charset)
			throws IOException {
		FileReader reader = new FileReader(src);
		BufferedReader br = new BufferedReader(reader);
		String[] array = new String[FileUtils.readLineNumber(src)];
		String line;
		int lineNumber = 0;
		while ((line = br.readLine()) != null) {
			array[lineNumber] = line;
			lineNumber++;
		}
		reader.close();
		br.close();
		return array;
	}

	/**
	 * 获取文件内容
	 * 
	 * @param src
	 *            源文件
	 * @return String[] 文件内容数组，每行占一个数组空间
	 * @throws IOException
	 */
	public static String read(String src) throws IOException {
		FileReader reader = new FileReader(src);
		BufferedReader br = new BufferedReader(reader);
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		br.close();
		return sb.toString();
	}

	/**
	 * 获取文件行数
	 * 
	 * @param src
	 *            源文件
	 * @return int 内容行数
	 * @throws IOException
	 */
	public static int readLineNumber(String src) throws IOException {
		FileReader reader = new FileReader(src);
		BufferedReader br = new BufferedReader(reader);
		int lineNumber = 0;
		while (br.readLine() != null) {
			lineNumber++;
		}
		reader.close();
		br.close();
		return lineNumber;
	}

	/**
	 * 获取目录下的文件和文件夹列表
	 * 
	 * @param dir
	 *            源目录
	 * @return LinkedHashMap<String,Boolean> true表示目录
	 * @throws Exception
	 */
	public static LinkedHashMap<String, Boolean> readDir(String dir)
			throws Exception {
		File d = new File(dir);

		if (!d.isDirectory())
			throw new Exception("\"" + dir + "\"" + "不是一个目录");

		String[] array = d.list();
		if (array == null)
			return null;

		LinkedHashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		for (int i = 0; i < array.length; i++) {
			map.put(array[i], new File(dir + File.separatorChar + array[i])
					.isDirectory() == true ? true : false);
		}
		return map;
	}

	/**
	 * 移动文件,不可以移动文件家
	 * 
	 * @param src
	 *            源文件
	 * @param dest
	 *            目标文件
	 */
	public static void moveFile(String src, String dest) {
		new File(src).renameTo(new File(dest));
	}

	/**
	 * 重命名文件||实际上调用本类的moveFile方法
	 * 
	 * @param src
	 *            源文件
	 * @param dest
	 *            目标文件
	 */
	public static void renameFile(String src, String dest) {
		moveFile(src, dest);
	}

	/**
	 * 从URL抓取一个文件写到本地<br>，有可能会出现403的情况
	 * 这个方法摘自 <a href="http://commons.apache.org/proper/commons-io/apidocs/org/apache/commons/io/FileUtils.html">org.apache.commons.io.FileUtils.copyURLToFile(URL source, File destination)</a> 
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
	public static void copyFileFromURL(URL source, File destination) throws IOException {

		InputStream input = null;
		FileOutputStream output = null;
		byte[] buffer = new byte[1024];
		
		input = source.openStream();

		if (destination.exists()) {
			if (destination.isDirectory()) {
				throw new IOException("File '" + destination
						+ "' exists but is a directory");
			}
			if (destination.canWrite() == false) {
				throw new IOException("File '" + destination
						+ "' cannot be written to");
			}
		} else {
			File parent = destination.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent
							+ "' could not be created");
				}
			}
		}

		output = new FileOutputStream(destination, true);

		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		output.close();
		input.close();
	}
	
	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyDir(String _src, String _target) throws IOException {
		
		File src=new File(_src);
		File dest=new File(_target);
		
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdirs();
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// 递归复制
				copyDir(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
			}
		} else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}
	
	/**
	 * 目录重命名
	 * @param sourceDirName
	 * @param targetDirName
	 */
	public static void renameDirectory(String sourceDirName,String targetDirName){
		new File(sourceDirName).renameTo(new File(targetDirName));
	}
	
	/**
	 * 获取目录下所有文件
	 * @param dir
	 * @param files
	 * @param listSubDir 是否包含子目录
	 * @return
	 */
    public static List<File> getFiles(String dir, boolean listSubDir) {
    	List<File> files=new ArrayList<File>();
        File realFile = new File(dir);
        if (realFile.isDirectory()) {
            File[] _files = realFile.listFiles();
            if(!listSubDir)return ArrayUtils.asList(_files);
            for (File file : _files) {
                if (file.isDirectory()) {
                    files.addAll(getFiles(file.getAbsolutePath(),true));
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }
	
	/**
     * 
     * 获取目录下所有文件(包括子文件夹)
     * 递归调用
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String dir) {
    	List<File> files=new ArrayList<File>();
        File realFile = new File(dir);
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
     * 获取目录下所有文件(按修改时间排序)
     * 
     * @param path
     * @return
     */
    public static List<File> getFileSort(String path) {
 
        List<File> list = getFiles(path);
 
        if (list != null && list.size() > 0) {
 
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }
 
                }
            });
        }
        
        return list;
    }
    
    /**
     * 从网络下载文件
     * @param url
     * @param filePathName
     */
    public static void download(String url, String filePathName) {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet(url);

			// 伪装成google的爬虫,一般服务器会进行请求的校验，如果不是http请求会进行拦截
			httpget.setHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
			HttpResponse response = httpclient.execute(httpget);

			File storeFile = new File(filePathName);
			FileOutputStream output = new FileOutputStream(storeFile);

			// 得到网络资源的字节数组,并写入文件
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					byte b[] = new byte[1024];
					int j = 0;
					while ((j = instream.read(b)) != -1) {
						output.write(b, 0, j);
					}
					output.flush();
					output.close();
				} catch (IOException ex) {
					throw ex;
				} catch (RuntimeException ex) {
					httpget.abort();
					throw ex;
				} finally {
					try {
						instream.close();
						httpget.releaseConnection();
					} catch (Exception ignore) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
    
    /**
     * 获取文件后缀名
     * @param file
     * @return
     */
    public static String getFileSuffix(File file){
    	if(!file.isFile()||!file.exists())
    		return null;
    	
    	String name=file.getName();
    	int pos=name.lastIndexOf(".");
    	if(pos==-1){
    		return null;
    	}
    	return name.substring(pos+1, name.length());
    }
    
    /**
     * 访问Jar包文件
     * @param jarFilePath Jar路径
     * @param jarInnerFilePath Jar中文件路径
     * @return
     * @throws IOException
     */
    public static String readStringFromJar(String jarFilePath,String jarInnerFilePath) throws IOException{
    	StringBuffer sb=new StringBuffer();
    	JarFile jar=null;
		try {
			jar = new JarFile(jarFilePath);
			Enumeration<JarEntry> entries = jar.entries();
	    	JarEntry ent=null;
	    	while(entries.hasMoreElements()){
	    		ent=entries.nextElement();
	    		if(ent.getName().equals(jarInnerFilePath)){
	    	    	InputStream is = jar.getInputStream(ent);
	    			BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    			String s = "";
	    			while ((s = br.readLine()) != null){
	    				sb.append(s+"\n");
	    			}
	    			break;
	    		}
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(jar!=null)
				jar.close();
		}
		return sb.toString();
    }
    
    
    /**
     * 访问Jar包文件
     * @param jarFilePath Jar路径
     * @param jarInnerFilePath Jar中文件路径
     * @return
     * @throws IOException
     */
    public static InputStream getInputStreamFromJar(String jarFilePath,String jarInnerFilePath) throws IOException{
    	JarFile jar=null;
		try {
			jar = new JarFile(jarFilePath);
			Enumeration<JarEntry> entries = jar.entries();
	    	JarEntry ent=null;
	    	while(entries.hasMoreElements()){
	    		ent=entries.nextElement();
	    		if(ent.getName().equals(jarInnerFilePath)){
	    	    	InputStream is = jar.getInputStream(ent);
	    			return is;
	    		}
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(jar!=null)
				jar.close();
		}
    	return null;
    }

}
