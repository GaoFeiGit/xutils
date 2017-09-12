package org.xdemo.app.xutils.ext.sftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.sftp.SftpClientFactory;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**<font color=red>
 * 注意FTP，FTPS的区别，以及FTPS和SFTP的区别，参考http://www.cnblogs.com/OLDMAN-LU/p/6428274.html<br>
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
 * @Date 2017年9月8日 下午4:05:28
 */
public class SFTPUtils {

	private static ChannelSftp cs;

	private static Session session;
	
	private static final String separator="/";
	
	private static final String UTF_8="UTF-8";

	private SFTPUtils() {
	}

	/**
	 * 获取SFTP实例
	 * @param host 主机
	 * @param port 端口
	 * @param user 用户
	 * @param password 密码
	 * @return
	 * @throws FileSystemException
	 * @throws JSchException
	 */
	public static SFTPUtils getInstance(String host, int port, String user, String password) throws FileSystemException, JSchException {
		FileSystemOptions vfs = new FileSystemOptions();
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(vfs, "no");
		session = SftpClientFactory.createConnection(host, port, user.toCharArray(), password.toCharArray(), vfs);
		Channel channel = session.openChannel("sftp");
		channel.connect();
		cs = (ChannelSftp) channel;
		return new SFTPUtils();
	}

	/**
	 * 断开连接
	 */
	public void close() {
		if (cs != null) {
			cs.exit();
		}
		if (session != null) {
			session.disconnect();
		}
		cs = null;
	}
	
	/**
	 * 列出路径下的文件和目录
	 * @param path
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("unchecked")
	public Vector<SFTPFile> list(String path) throws SftpException{
		Vector<LsEntry> v=cs.ls(path);
		if(!path.endsWith(separator))
			path+=separator;
		Vector<SFTPFile> files=new Vector<SFTPFile>();
		for(LsEntry entry:v){
			if(entry.getFilename().equals(".")||entry.getFilename().equals(".."))continue;
			files.add(new SFTPFile(entry.getAttrs().isDir(), entry.getAttrs().isFifo(), entry.getAttrs().getSize(), entry.getFilename(),path, path+entry.getFilename(), (long)entry.getAttrs().getMTime()));
		}
		return files;
	}
	
	/**
	 * 列出指定路径下的文件和目录，以及所有的子目录
	 * @param path
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("unchecked")
	public Vector<SFTPFile> listAll(String path) throws SftpException{
		Vector<LsEntry> v=cs.ls(path);
		if(!path.endsWith(separator))
			path+=separator;
		Vector<SFTPFile> files=new Vector<SFTPFile>();
		for(LsEntry entry:v){
			if(entry.getFilename().equals(".")||entry.getFilename().equals(".."))continue;
			files.add(new SFTPFile(entry.getAttrs().isDir(), entry.getAttrs().isFifo(), entry.getAttrs().getSize(), entry.getFilename(),path, path+entry.getFilename(), (long)entry.getAttrs().getMTime()));
			if(entry.getAttrs().isDir())
				files.addAll(listAll(path+entry.getFilename()));
		}
		return files;
	}
	
	/**
	 * 删除目录，
	 * @param dir 目录路径
	 * @param deleteIfNotNull 如果目录非空，是否删除
	 * @throws Exception
	 */
	public void deleteDir(String dir,boolean deleteIfNotNull) throws Exception{
		System.out.println(dir);
		Vector<SFTPFile> files=list(dir);
		if(files.size()==0)
			cs.rmdir(dir);
		else{
			if(deleteIfNotNull){
				for(SFTPFile file:files){
					System.out.println(file.getPathname());
					if(file.isDir()){
						deleteDir(file.getPathname(), true);
					}
					else{
						deleteFile(file.getPathname());
					}
				}
			}else{
				throw new Exception("目录非空，无法删除");
			}
		}
		cs.rmdir(dir);
	}
	
	/**
	 * 删除多个文件
	 * @param files
	 * @throws SftpException
	 */
	public void deleteFile(String...files) throws SftpException{
		for(String file:files)
			cs.rm(file);
	}
	
	/**
	 * 重命名文件
	 * @param src
	 * @param target
	 * @throws SftpException
	 */
	public void rename(String src,String target) throws SftpException{
		cs.rename(src, target);
	}
	
	/**
	 * 是否是文件
	 * @param file
	 * @return
	 */
	public boolean isFile(String file){
		InputStream is=null;
		try {
			is=cs.get(file);
			if(is==null){
				return false;
			}
		} catch (Exception e) {
			return false;
		}finally{
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}
		return true;
	}
	
	/**
	 * 是否是目录
	 * @param dir
	 * @return
	 */
	public boolean isDir(String dir){
		try {
			cs.cd(dir);
			cs.cd("..");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 创建目录,支持多级创建
	 * @param fullPathName 全路径，如/x/y/z
	 * @throws SftpException
	 */
	public void createDir(String fullPathName) throws SftpException{
		
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
				createDir(base,path[i+1]);
			}
		}
	}
	
	/**
	 * 创建目录
	 * @param parent
	 * @param dir
	 * @throws SftpException
	 */
	public void createDir(String parent,String dir) throws SftpException {
		if(!isDir(parent)){
			cs.mkdir(parent);
		}
		cs.mkdir(parent+dir);
	}
	
	/**
	 * 上传多个文件
	 * @param ftpDir
	 * @param files
	 * @throws SftpException
	 */
	public void upload(String ftpDir,String...files) throws SftpException{
		if(!isDir(ftpDir)){
			createDir(ftpDir);
		}
		for(String file:files)
			cs.put(file, ftpDir);
	}
	
	/**
	 * 下载目录到本地目录
	 * @param ftpDir
	 * @param localDir
	 * @throws SftpException
	 */
	public void downloadDir(String ftpDir,String localDir) throws SftpException{
		if(!localDir.endsWith(File.separator)){
			localDir+=File.separator;
		}
		Vector<SFTPFile> files=listAll(ftpDir);
		for(SFTPFile file:files){
			if(file.isDir()){
				//如果是路径，创建目录
				new File(localDir+file.getPathname().replace(ftpDir, "")).mkdirs();
			}else{
				File path=new File(localDir+file.getPathname().replace(ftpDir, "")).getParentFile();
				if(!path.exists()){
					path.mkdirs(); 
				}
				cs.get(file.getPathname(), localDir+file.getPathname().replace(ftpDir, ""));
			}
		}
	}
	
	/**
	 * 下载目录到ZIP文件
	 * @param ftpDir
	 * @param localZipPathName
	 * @throws SftpException
	 */
	public void downloadDirToZip(String ftpDir,String localZipPathName) throws SftpException{
		byte[] buf = new byte[1024];

		Vector<SFTPFile> list = listAll(ftpDir);
		try {
			FileOutputStream fos = new FileOutputStream(localZipPathName);
			ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(fos));
			zipStream.setEncoding("GBK");
			for (SFTPFile ftpFile : list) {
				String entryName = ftpFile.getPathname().replace(ftpDir, "");
				if (entryName.startsWith("/")) {
					entryName = entryName.substring(1);
				}
				if (ftpFile.isDir()) {
					entryName += "/";
				}
				ZipEntry entry = new ZipEntry(entryName);
				zipStream.putNextEntry(entry);
				if (ftpFile.isFile()) {
					InputStream is = cs.get(ftpFile.getPathname());
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过HTTP下载文件夹到客户机
	 * 
	 * @param ftpDir
	 * @param zipName
	 * @param response
	 * @throws IOException
	 */
	public void downloadDir(String ftpDir, String zipName, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipName, UTF_8));
		byte[] buf = new byte[1024];

		try {

			Vector<SFTPFile> list = listAll(ftpDir);

			ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
			zipStream.setEncoding("GBK");
			for (SFTPFile ftpFile : list) {
				String entryName = ftpFile.getPathname().replace(ftpDir, "");
				if (ftpFile.isDir())
					entryName += "/";
				ZipEntry entry = new ZipEntry(entryName);
				zipStream.putNextEntry(entry);
				if (ftpFile.isDir())
					continue;
				InputStream is = cs.get(ftpFile.getPathname());
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
				InputStream is = cs.get(pathname);
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
	 * 通过HTTP下载指定目录下的指定文件到客户机
	 * 
	 * @param path
	 * @param file
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SftpException 
	 */
	public void downloadFile(String ftpFilePath, HttpServletResponse response) throws SftpException, IOException {
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(ftpFilePath.substring(ftpFilePath.lastIndexOf("/")+1), UTF_8));
		cs.get(ftpFilePath, response.getOutputStream());
	}
	
	/**
	 * 下载指定路径指定文件到本地
	 * 
	 * @param ftpFilePath
	 * @param file
	 * @param localFile
	 * @return
	 * @throws SftpException 
	 */
	public void downloadFile(String ftpFilePath, String localFile) throws SftpException {
		cs.get(ftpFilePath, localFile);
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
				InputStream is = cs.get(pathname);
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
	
	
	public static void main(String[] args) throws Exception {
		SFTPUtils sftp = SFTPUtils.getInstance("192.168.1.150", 22, "g", "g");
//		Vector<SFTPFile> v=sftp.listAll("/2010");
//		for(SFTPFile f:v){
//			System.out.println(f.toString());
//		}
//		sftp.deleteDir("/2010 - 副本 - 副本",true);
//		System.out.println(sftp.isFile("/2019/3.doc"));
//		sftp.rename("/xx.bat", "/xxxx.bat");
//		sftp.createDir("/xxx/fdsa");
//		sftp.upload("/dd/fd", "D:\\测试.zip","D:\\pom.xml");
//		sftp.downloadDirToZip("/2010 - 副本 (2)", "D:\\x.zip");
		sftp.downloadFiles("D:\\xxxxxx.zip", "/dd/fd/pom.xml");
	}
	
	

}
