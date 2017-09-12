package org.xdemo.app.xutils.j2se;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * 图像处理工具类（本类完全可以使用，但是更加推荐ImageMagick +Jmagick，采用C++实现的一个类库，提供了Java的Api，非常强大和高效）
 * 而且要为JVM分配较大的堆内存，否则内存溢出
 * @author <a href="http://<a href="http://www.xdemo.org">www.xdemo.org</a>/">http://<a href="http://www.xdemo.org">www.xdemo.org</a>/</a>
 * 252878950@qq.com
 */
public class ImageUtils {

	/**
	 * 缩放图片
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param ratio 缩放比例，如 0.1,0.8,1.2,2.4
	 * @throws IOException 
	 */
	public static void zoom(String src,String dest,double ratio) throws IOException{
		//获取文件扩展名
		String suffix=src.substring(src.lastIndexOf(".")+1);
		//读入文件
		BufferedImage bi=ImageIO.read(new File(src));
		//计算目标文件宽度
		int targetWidth=Integer.parseInt(new DecimalFormat("0").format(bi.getWidth()*ratio));
		//计算目标文件高度
		int targetHeight=Integer.parseInt(new DecimalFormat("0").format(bi.getHeight()*ratio));
		//获取BufferedImage读入的图片的一个缩放的版本
		Image image=bi.getScaledInstance(targetWidth,targetHeight,Image.SCALE_DEFAULT);
		//创建一张空白的缓存图片
		BufferedImage target=new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		//返回一张2D图像
		Graphics g=target.createGraphics();
		//将BufferedImage读入的图片画到上一步创建的对象上
		g.drawImage(image, 0, 0, null);
		//释放
		g.dispose();
		//图像写入文件
		ImageIO.write(target, suffix, new File(dest));
	}
	
	/**
	 * 切图
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param startX 起点x坐标
	 * @param startY 起点y坐标
	 * @param endX 结束点x坐标
	 * @param endY 结束点y坐标
	 * @throws IOException
	 */
	public static void cut(String src,String dest,int startX,int startY,int endX,int endY) throws IOException{
		//获取文件扩展名
		String suffix=src.substring(src.lastIndexOf(".")+1);
		//读入文件
		BufferedImage bi=ImageIO.read(new File(src));
		//计算宽度
		int width=Math.abs(startX-endX);
		//计算高度
		int height=Math.abs(startY-endY);
		BufferedImage target= bi.getSubimage(startX, startY, width, height);
		ImageIO.write(target, suffix, new File(dest));
	}
	
	/**
	 * 旋转图片
	 * 
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param degree 旋转角度
	 * @param bgcolor 背景色，无背景色为null
	 * @throws IOException
	 */
	public static void rotate(String src, String dest, int degree, Color bgcolor) throws IOException {
		BufferedImage image = ImageIO.read(new File(src));
		int iw = image.getWidth();// 原始图象的宽度
		int ih = image.getHeight();// 原始图象的高度
		int w = 0;
		int h = 0;
		int x = 0;
		int y = 0;
		degree = degree % 360;
		if (degree < 0)
			degree = 360 + degree;// 将角度转换到0-360度之间
		double ang = Math.toRadians(degree);// 将角度转为弧度

		/**
		 * 确定旋转后的图象的高度和宽度
		 */

		if (degree == 180 || degree == 0 || degree == 360) {
			w = iw;
			h = ih;
		} else if (degree == 90 || degree == 270) {
			w = ih;
			h = iw;
		} else {
			double cosVal = Math.abs(Math.cos(ang));
			double sinVal = Math.abs(Math.sin(ang));
			w = (int) (sinVal * ih) + (int) (cosVal * iw);
			h = (int) (sinVal * iw) + (int) (cosVal * ih);
		}

		x = (w / 2) - (iw / 2);// 确定原点坐标
		y = (h / 2) - (ih / 2);
		BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();
		if (bgcolor == null) {
			rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(w,h, Transparency.TRANSLUCENT);
		} else {
			gs.setColor(bgcolor);
			gs.fillRect(0, 0, w, h);// 以给定颜色绘制旋转后图片的背景
		}

		AffineTransform at = new AffineTransform();
		at.rotate(ang, w / 2, h / 2);// 旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at,
				AffineTransformOp.TYPE_BICUBIC);
		op.filter(image, rotatedImage);
		image = rotatedImage;

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ImageOutputStream iamgeOut = ImageIO.createImageOutputStream(byteOut);

		ImageIO.write(image, "png", iamgeOut);
		InputStream is = new ByteArrayInputStream(byteOut.toByteArray());

		OutputStream os = new FileOutputStream(new File(dest));

		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		os.close();
		is.close();
		byteOut.close();
	}

}
