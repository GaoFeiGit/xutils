package org.xdemo.app.xutils.ext.ftp;
import org.apache.commons.net.ftp.FTPFile;


/**
 * 扩展FTPFile，新增path属性
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月8日 上午10:31:21
 */
public class FtpFile{
	
	private String pathname;
	
	private FTPFile ftpFile;

	
	public FtpFile(FTPFile ftpFile,String pathname){
		this.ftpFile=ftpFile;
		this.pathname=pathname;
	}

	public FTPFile getFtpFile() {
		return ftpFile;
	}

	public void setFtpFile(FTPFile ftpFile) {
		this.ftpFile = ftpFile;
	}



	public String getPathname() {
		return pathname;
	}



	public void setPathname(String pathname) {
		this.pathname = pathname;
	}
	
	
}
