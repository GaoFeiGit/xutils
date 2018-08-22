package org.xdemo.app.xutils.ext;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.xdemo.app.xutils.j2se.FileUtils;

/**
 * @author Goofy 
 * Http下载工具类
 */
public class HttpUtils {
	
	/**
	 * 获取项目网络路径
	 * @param request
	 * @return
	 */
	public static String getContentpath(HttpServletRequest request){
		return request.getContextPath();
	}
	
	/**
     * 获取项目磁盘绝对路径
     */
    public static String getRealPath(HttpServletRequest request){
        return request.getSession().getServletContext().getRealPath("/");
    }
    
    /**
     * 使用了代理服务器的，无法获取正确地址的，使用这个方法获取访问者的IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
    	String ipAddress = request.getHeader("x-forwarded-for");  
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getHeader("Proxy-Client-IP");  
        }  
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = request.getRemoteAddr();  
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                //根据网卡取本机配置的IP  
                InetAddress inet=null;  
                try {  
                    inet = InetAddress.getLocalHost();  
                } catch (UnknownHostException e) {  
                    e.printStackTrace();  
                }  
                ipAddress= inet.getHostAddress();  
            }  
        }  
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
            if(ipAddress.indexOf(",")>0){  
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
            }  
        }
        return ipAddress;
    }

	/**
	 * 下载多个文件
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param files
	 *            File
	 */
	public static void download(HttpServletResponse response, File... files) {
		byte buffer[] = new byte[40960];
		try {
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(files[0].getName()+"等多个文件.zip", "UTF-8"));
			BufferedInputStream bis = null;
			FileInputStream fis = null;
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));

			ZipEntry entry = null;

			zos.setEncoding("GBK");

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
			if(bis!=null)
				bis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 下载整个目录
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param files
	 *            File
	 */
	public static void downloadDir(HttpServletResponse response, File dir) {
		byte buffer[] = new byte[40960];
		List<File> files = FileUtils.getFiles(dir.getAbsolutePath());
		String separator=File.separator;
		String dirName=dir.getAbsolutePath();
		dirName=dirName.replace("\\", separator).replace("/", separator);
		if(!dirName.endsWith(separator)){
			dirName+=separator;
		}
		
		try {
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(dir.getName()+".zip", "UTF-8"));
			BufferedInputStream bis = null;
			FileInputStream fis = null;
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));

			ZipEntry entry = null;

			zos.setEncoding("GBK");

			for (File file : files) {
				
				
				String entryName = file.getAbsolutePath().replace("\\", separator).replace("/", separator).replace(dir.getAbsolutePath().replace("\\", separator).replace("/", separator), "");
				
				if (file.isDirectory())
					entryName+=separator;
				
				entry = new ZipEntry(entryName);
				
				if (file.isDirectory())continue;
				
				zos.putNextEntry(entry);
				
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
			if(bis!=null)
				bis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 下载文件
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param file
	 *            文件
	 * @param fileName
	 *            下载的输出文件名
	 */
	public static void download(HttpServletResponse response, File file, String fileName) {
		InputStream is = null;
		String _fileName = null;
		try {
			is = new FileInputStream(file);
			_fileName = fileName == null ? file.getName() : fileName;
			download(response, is, _fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param is
	 *            输入流
	 * @param fileName
	 *            文件名
	 * @param response
	 */
	public static void download(HttpServletResponse response, InputStream is, String fileName) {

		OutputStream os = null;

		try {
			response.reset();
			response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"),"ISO-8859-1"));

			os = response.getOutputStream();

			byte buffer[] = new byte[1024];
			int len = 0;

			while ((len = is.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}

			os.flush();
			os.close();

			is.close();

		} catch (Exception e) {
			try {
				if (os != null)
					os.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (is != null)
					is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";

    
    /**
     * 发送HttpGet请求
     * 
     * @param url
     *            请求地址
     * @return 返回字符串
     */
    public static String get(String url) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", userAgent);
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    /**
     * 发送HttpPost请求，参数为map
     * 
     * @param url
     *            请求地址
     * @param map
     *            请求参数
     * @return 返回字符串
     */
    public static String post(String url, Map<String, String> map) {
        // 设置参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        // 编码
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        // 取得HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        // 防止被当成攻击添加的
        httpPost.setHeader("User-Agent", userAgent);
        // 参数放入Entity
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 执行post请求
            response = httpclient.execute(httpPost);
            // 得到entity
            HttpEntity entity = response.getEntity();
            // 得到字符串
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
        } finally {
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    /**
     * 发送HttpPost请求，参数为文件，适用于微信上传素材
     * 
     * @param url
     *            请求地址
     * @param file
     *            上传的文件
     * @return 返回字符串
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String post(String url, File file) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        // 防止被当成攻击添加的
        httpPost.setHeader("User-Agent", userAgent);
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("media", new FileBody(file));
        httpPost.setEntity(multipartEntity.build());
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
        } finally {
            // 关闭CloseableHttpResponse
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return result;

    }

    /**
     * 发送HttpPost请求，参数为json字符串
     * 
     * @param url
     * @param jsonStr
     * @return
     */
    public static String post(String url, String jsonStr) {
        String result = null;
        // 字符串编码
        StringEntity entity = new StringEntity(jsonStr, Consts.UTF_8);
        // 设置content-type
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        // 防止被当成攻击添加的
        httpPost.setHeader("User-Agent", userAgent);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
        } finally {
            // 关闭CloseableHttpResponse
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    /**
     * 发送不带参数的HttpPost请求
     * 
     * @param url
     * @return
     */
    public static String post(String url) {
        String result = null;
        // 得到一个HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        // 防止被当成攻击添加的
        httpPost.setHeader("User-Agent", userAgent);
        CloseableHttpResponse response = null;
        try {
            // 执行HttpPost请求，并得到一个CloseableHttpResponse
            response = httpclient.execute(httpPost);
            // 从CloseableHttpResponse中拿到HttpEntity
            HttpEntity entity = response.getEntity();
            // 将HttpEntity转换为字符串
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
        } finally {
            // 关闭CloseableHttpResponse
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }
    
}
