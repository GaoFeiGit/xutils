package org.xdemo.app.xutils.ext.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**<font color=red>
 * 注意FTP，FTPS的区别，以及FTPS和SFTP的区别，参考http://www.cnblogs.com/OLDMAN-LU/p/6428274.html
 * </font>
 * 功能列表
 * 1.多文件上传，支持自动创建目录<br>
 * 2.下载文件，支持下载多个文件、目录到本地目录，到ZIP压缩包，到HttpServletResponse输出流，下载到客户机<br>
 * 3.删除文件，支持删除单个、多个文件，删除目录（包括非空目录）<br>
 * 4.创建目录，支持创建多级目录<br>
 * 5.判断是否是文件夹<br>
 * 6.判断是否是文件<br>
 * 7.文件重命名<br>
 * 8.递归列出指定目录下的所有文件，包括子目录<br>
 * 9.其他上述类似的重载方法，支持不同的参数<br>
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月6日 下午3:36:12
 */
public class FTPUtils {

	private static FTPClient ftp = null;

	final String UTF_8 = "UTF-8";
	final String ISO_8859_1 = "ISO-8859-1";

	private FTPUtils() {
	}

	/**
	 * 获取FTP实例
	 * 
	 * @param host
	 * @param port
	 * @param userName
	 * @param userPassword
	 * @param showWelcome
	 * @return
	 * @throws Exception
	 */
	public static FTPUtils getInstance(String host, int port, String userName, String userPassword,boolean useSSL, boolean showWelcome) throws Exception {
		ftp = new FTPClient();
		if(useSSL)
			ftp=new FTPSClient(true);
		ftp.connect(host, port);
		if (showWelcome) {
			System.err.println(ftp.getReplyString());
		}
		boolean suc = ftp.login(userName, userPassword);
		if (suc == false) {
			System.err.println("登陆失败");
			throw new Exception();
		}
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		return new FTPUtils();
	}

	/**
	 * 重命名文件
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws IOException
	 */
	public boolean rename(String from, String to) throws IOException {
		return ftp.rename(from, to);
	}

	/**
	 * 列出目录下的所有文件,包括子目录
	 * 
	 * @param path
	 * @return
	 */
	public List<FtpFile> listAllFiles(String path) {
		List<FtpFile> list = new ArrayList<FtpFile>();
		try {
			path = ftpEncoding(path);
			FTPFile[] files = ftp.listFiles(path);
			FtpFile file = null;
			for (FTPFile ftpFile : files) {
				file = new FtpFile(ftpFile, path + "/" + ftpFile.getName());
				list.add(file);
				if (ftpFile.isDirectory()) {
					list.addAll(listAllFiles(ftpDecoding(file.getPathname())));
				}
			}
		} catch (Exception e) {
		}
		return list;
	}

	/**
	 * 列出目录下的文件，不包含子目录
	 * 
	 * @return
	 * @throws IOException
	 */
	public FTPFile[] listFiles() throws IOException {
		return ftp.listFiles();
	}

	/**
	 * 列出指定目录下的文件，不包含子目录
	 * 
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	public FTPFile[] listFiles(String pathname) throws IOException {
		return ftp.listFiles(pathname);
	}

	/**
	 * 下载文件夹到本地目录
	 * 
	 * @param ftpDir
	 * @param localDir
	 * @return
	 * @throws FileNotFoundException
	 */
	public boolean downloadDir(String ftpDir, String localDir) throws FileNotFoundException {
		FileOutputStream fops = null;
		try {
			ftpDir = ftpEncoding(ftpDir);
			if (!ftp.changeWorkingDirectory(ftpDir))
				return false;
			FTPFile[] ftpFiles = ftp.listFiles();
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.isDirectory()) {
					new File(localDir + File.separator + ftpDecoding(ftpFile.getName())).mkdirs();
					if (!downloadDir(ftpDecoding(ftpDir + "/" + ftpFile.getName()), localDir + File.separator + ftpDecoding(ftpFile.getName()) + File.separator)) {
						return false;
					}
				} else {
					fops = new FileOutputStream(localDir + File.separator + ftpDecoding(ftpFile.getName()));
					if (!ftp.retrieveFile(ftpFile.getName(), fops)) {
						fops.close();
						return false;
					}
					fops.close();
				}
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 将远程目录打包下载
	 * 
	 * @param ftpDir
	 * @param localZipPathName
	 * @return
	 */
	public boolean downloadDirToZip(String ftpDir, String localZipPathName) {

		byte[] buf = new byte[1024];

		List<FtpFile> list = listAllFiles(ftpDir);
		try {
			FileOutputStream fos = new FileOutputStream(localZipPathName);
			ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(fos));
			zipStream.setEncoding("GBK");
			for (FtpFile ftpFile : list) {
				String entryName = ftpDecoding(ftpFile.getPathname());
				if (entryName.startsWith("/")) {
					entryName = entryName.substring(1);
				}
				if (ftpFile.getFtpFile().isDirectory()) {
					entryName += "/";
				}
				ZipEntry entry = new ZipEntry(entryName);
				zipStream.putNextEntry(entry);
				if (ftpFile.getFtpFile().isFile()) {
					InputStream is = ftp.retrieveFileStream(ftpFile.getPathname());
					if (is != null) {
						int readLen = -1;
						while ((readLen = is.read(buf, 0, 1024)) != -1) {
							zipStream.write(buf, 0, readLen);
						}
						is.close();
					}
				}
			}

			zipStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 通过HTTP下载文件夹到客户机
	 * 
	 * @param path
	 * @param zipName
	 * @param response
	 * @throws IOException
	 */
	public void downloadDir(String path, String zipName, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipName, UTF_8));
		byte[] buf = new byte[1024];

		try {

			List<FtpFile> list = listAllFiles(path);

			ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
			zipStream.setEncoding("GBK");
			for (FtpFile ftpFile : list) {
				String entryName = ftpDecoding(ftpFile.getPathname());
				if (ftpFile.getFtpFile().isDirectory())
					entryName += "/";
				ZipEntry entry = new ZipEntry(entryName);
				zipStream.putNextEntry(entry);
				if (ftpFile.getFtpFile().isDirectory())
					continue;
				InputStream is = ftp.retrieveFileStream(ftpFile.getPathname());
				if (is != null) {
					int readLen = -1;
					while ((readLen = is.read(buf, 0, 1024)) != -1) {
						zipStream.write(buf, 0, readLen);
					}
					is.close();
				}
			}

			zipStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载指定路径指定文件到本地
	 * 
	 * @param path
	 * @param file
	 * @param localFile
	 * @return
	 */
	public boolean downloadFile(String path, String file, String localFile) {
		FileOutputStream fops = null;
		try {
			path = ftpEncoding(path);
			file = ftpEncoding(file);
			fops = new FileOutputStream(localFile);
			if (!ftp.changeWorkingDirectory(path))
				return false;
			if (!ftp.retrieveFile(file, fops)) {
				return false;
			}
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (fops != null)
				try {
					fops.close();
				} catch (IOException e1) {
				}
		}
	}

	/**
	 * 通过HTTP下载指定目录下的指定文件到客户机
	 * 
	 * @param path
	 * @param file
	 * @param response
	 * @return
	 */
	public boolean downloadFile(String path, String file, HttpServletResponse response) {
		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file, UTF_8));
			path = ftpEncoding(path);
			file = ftpEncoding(file);
			if (!ftp.changeWorkingDirectory(path))
				return false;
			if (!ftp.retrieveFile(file, response.getOutputStream())) {
				return false;
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 通过HTTP下载多个文件到客户机
	 * 
	 * @param response
	 * @param zipName
	 * @param pathnames
	 * @throws IOException
	 */
	public void downloadFiles(HttpServletResponse response, String zipName, String... pathnames) throws IOException {
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipName, UTF_8));
		byte[] buf = new byte[1024];

		try {
			ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));

			for (String pathname : pathnames) {
				ZipEntry entry = new ZipEntry(pathname.substring(pathname.lastIndexOf("/") + 1));
				zipStream.putNextEntry(entry);
				InputStream is = ftp.retrieveFileStream(pathname);
				if (is != null) {
					int readLen = -1;
					while ((readLen = is.read(buf, 0, 1024)) != -1) {
						zipStream.write(buf, 0, readLen);
					}
					is.close();
				}
			}

			zipStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载多个文件到本地压缩包
	 * 
	 * @param localZipPathName
	 * @param pathnames
	 * @throws IOException
	 */
	public void downloadFiles(String localZipPathName, String... pathnames) throws IOException {
		byte[] buf = new byte[1024];
		FileOutputStream fos = new FileOutputStream(localZipPathName);
		try {
			ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(fos));

			for (String pathname : pathnames) {
				ZipEntry entry = new ZipEntry(pathname.substring(pathname.lastIndexOf("/") + 1));
				zipStream.putNextEntry(entry);
				InputStream is = ftp.retrieveFileStream(pathname);
				if (is != null) {
					int readLen = -1;
					while ((readLen = is.read(buf, 0, 1024)) != -1) {
						zipStream.write(buf, 0, readLen);
					}
					is.close();
				}
			}

			zipStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列出指定路径的所有目录
	 * 
	 * @param parent
	 * @return
	 * @throws IOException
	 */
	public FTPFile[] listDirs(String parent) throws IOException {
		if (StringUtils.isEmpty(parent)) {
			return ftp.listDirectories();
		}
		return ftp.listDirectories(parent);
	}

	/**
	 * 删除文件
	 * 
	 * @param pathnames
	 *            单个或者数组 文件路径
	 * @return
	 * @throws IOException
	 */
	public boolean deleteFile(String... pathnames) throws IOException {
		for (String pathname : pathnames) {
			pathname = ftpEncoding(pathname);
			if (!isFile(pathname)) {
				return false;
			}
			if (!ftp.deleteFile(pathname)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 删除目录
	 * 
	 * @param dirPath
	 * @return
	 * @throws IOException
	 */
	public boolean deleteDir(String dirPath) throws IOException {
		if (!isDir(dirPath))
			return false;
		dirPath = ftpEncoding(dirPath);
		FTPFile[] files = ftp.listFiles(dirPath);
		if (files.length == 0) {// 如果等于0，表示空的目录
		} else if (files.length == 1) {// 如果是文件或者目录中都含有一个文件或者含有一个子目录，那么是1
			for (FTPFile file : files) {
				if (file.isDirectory()) {
					if (!deleteDir(ftpDecoding(dirPath + "/" + file.getName()))) {
						return false;
					}
				} else {
					return ftp.deleteFile(dirPath);
				}
			}
		} else {
			for (FTPFile file : files) {
				if (file.isDirectory()) {
					if (!deleteDir(dirPath + "/" + file.getName())) {
						return false;
					}
				} else {
					if (!ftp.deleteFile(dirPath + "/" + file.getName())) {
						return false;
					}
				}
			}
		}
		return ftp.removeDirectory(dirPath);
	}

	/**
	 * 删除文件或者文件夹，如果文件夹是空的，非空不删除
	 * 
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	public boolean deleteIfNull(String pathname) throws IOException {
		pathname = ftpEncoding(pathname);
		return ftp.removeDirectory(pathname) || ftp.deleteFile(pathname);
	}

	/**
	 * UTF-8 > ISO-8859-1
	 * 
	 * @param string
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String ftpEncoding(String string) throws UnsupportedEncodingException {
		return new String(string.getBytes(UTF_8), ISO_8859_1);
	}

	/**
	 * UTF-8 < ISO-8859-1
	 * 
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String ftpDecoding(String fileName) throws UnsupportedEncodingException {
		return new String(fileName.getBytes(ISO_8859_1), UTF_8);
	}

	/**
	 * 关闭FTP连接
	 */
	public void close() {
		try {
			if (ftp != null && ftp.isConnected())
				ftp.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否是目录，如果不存在，也返回false
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean isDir(String path) throws IOException {
		path = ftpEncoding(path);
		boolean isDir = ftp.changeWorkingDirectory(path);
		if (isDir)
			// 如果是目录，在切换回去，并返回切换结果，如果切换回去出错，对于当前的上下文来说，也是错误的
			return ftp.changeToParentDirectory();
		return isDir;
	}

	/**
	 * 判断是否是文件
	 * 
	 * @param path
	 * @return
	 */
	public boolean isFile(String path) {
		InputStream is = null;
		try {
			path = ftpEncoding(path);
			is = ftp.retrieveFileStream(path);
			if (is == null || ftp.getReplyCode() == 550)
				return false;
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public boolean upload(String ftpDir,boolean createNewDir,File... files) throws UnsupportedEncodingException, IOException {
		if(!ftpDir.endsWith("/")){
			ftpDir+="/";
		}
		if(!isDir(ftpDir)){
			if(createNewDir){
				if(!createDir(ftpDir)){
					return false;
				}
			}
		}
		ftp.changeWorkingDirectory(ftpEncoding(ftpDir));
		for(File file:files){
			InputStream is=new FileInputStream(file);
			if(!ftp.storeFile(ftpEncoding(ftpDir+file.getName()), is)){
				is.close();
				return false;
			}
			is.close();
		}
		return true;
	}

	public boolean upload(String ftpDir,boolean createNewDir,String... files) throws UnsupportedEncodingException, IOException {
		File[] array=new File[files.length];
		int i=0;
		for(String file:files){
			array[i++]=new File(file);
		}
		return upload(ftpDir, createNewDir, array);
	}
	
	public boolean createDir(String fullPathName) throws UnsupportedEncodingException, IOException{
		
		if(fullPathName.startsWith("/"))
			fullPathName=fullPathName.substring(1);
		if(fullPathName.endsWith("/"))
			fullPathName=fullPathName.substring(0,fullPathName.lastIndexOf("/"));
		fullPathName.replace("//", "/");
		if(!isDir(fullPathName)){
			String[] path=fullPathName.split("/");
			String base="/";
			for(int i=0;i<path.length-1;i++){
				base+=path[i]+"/";
				if(!createDir(base,path[i+1])){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean createDir(String parent,String dir) throws UnsupportedEncodingException, IOException{
		if(!isDir(parent)){
			if(!ftp.makeDirectory(parent)){
				return false;
			}
		}
		return ftp.makeDirectory(ftpEncoding(parent)+"/"+ftpEncoding(dir));
	}

	public static void main(String[] args) throws Exception {
		FTPUtils fu = FTPUtils.getInstance("192.168.1.222", 990, "g", "",true, true);
		// System.out.println(fu.deleteIfNull("/此次"));
		// List<FtpFile> files = fu.listAllFiles("/张三");
		// for (FtpFile file : files)
		// System.out.println(fu.ftpDecoding(file.getPathname()));
//		fu.downloadDirToZip("/张三", "D:\\22.zip");
//		fu.createDir("/x1/y13/z3/5/");
		fu.upload("/张三/xxx", true, new File("D:\\测试.zip"));
	}

}
